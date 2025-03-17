package org.example.authservice.contoller;

import jakarta.servlet.http.HttpServletRequest;
import org.example.authservice.event.OnGenerateResetLinkEvent;
import org.example.authservice.event.OnRegenerateEmailVerificationEvent;
import org.example.authservice.event.OnUserAccountChangeEvent;
import org.example.authservice.event.OnUserRegistrationCompleteEvent;
import org.example.authservice.exception.*;
import org.example.authservice.model.CustomUserDetails;
import org.example.authservice.model.DeviceInfo;
import org.example.authservice.model.dto.*;
import org.example.authservice.model.entity.EmailVerificationToken;
import org.example.authservice.model.entity.PasswordResetToken;
import org.example.authservice.security.JwtTokenProvider;
import org.example.authservice.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.authservice.service.RefreshTokenService;
import org.example.authservice.utils.Utils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authorization Rest API", description = "Defines endpoints that can be hit only when the user is not " +
        "logged in. It's not secured by default.")
@Slf4j
@RequiredArgsConstructor

public class AuthController {
    private final AuthService authService;
    private final ApplicationEventPublisher eventPublisher;
    private final JwtTokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;

    /**
     * Checks is a given email is in use or not.
     */
    @Operation(summary = "Checks if the given email is in use")
    @GetMapping("/checkEmailInUse")
    public ResponseEntity<?> checkEmailInUse(
            @Param(value = "Email id to check against") @RequestParam("email") String email) {
        Boolean emailExists = authService.emailAlreadyExists(email);
        return ResponseEntity.ok(new ApiResponseDto(true, emailExists.toString()));
    }

    /**
     * Checks is a given username is in use or not.
     */
    @Operation(summary = "Checks if the given username is in use")
    @GetMapping("/checkUsernameInUse")
    public ResponseEntity<?> checkUsernameInUse(
            @Param(value = "Username to check against") @RequestParam("username") String username) {
        Boolean usernameExists = authService.usernameAlreadyExists(username);
        return ResponseEntity.ok(new ApiResponseDto(true, usernameExists.toString()));
    }

    /**
     * Entry point for the user registration process. On successful registration,
     * publish an event to generate email verification token
     */
    @PostMapping("/register")
    @Operation(summary = "Registers the user and publishes an event to generate the email verification")
    public ResponseEntity<?> registerUser(
            @Param(value = "The RegistrationRequest payload") @Valid @RequestBody RegistrationRequestDto registrationRequest) {

        return authService.registerUser(registrationRequest)
                .map(user -> {

                    UriComponentsBuilder builder = ServletUriComponentsBuilder
                            .fromCurrentContextPath()
                            .path("/api" + "/auth/registrationConfirmation");

                    OnUserRegistrationCompleteEvent onUserRegistrationCompleteEvent = new OnUserRegistrationCompleteEvent(
                            user, builder);
                    eventPublisher.publishEvent(onUserRegistrationCompleteEvent);
                    log.info("Registered User returned [API: {}]", user);
                    return ResponseEntity.ok(new ApiResponseDto(true,
                            "User registered successfully. Check your email " +
                                    "for verification"));
                })
                .orElseThrow(() -> new UserRegistrationException(registrationRequest.getEmail(),
                        "Missing user object" +
                                " in database"));
    }

    /**
     * Resend the email registration mail with an updated token expiry. Safe to
     * assume that the user would always click on the last re-verification email and
     * any attempts at generating new token from past (possibly archived/deleted)
     * tokens should fail and report an exception.
     */

    @GetMapping("/resendRegistrationToken")
    @Operation(summary = "Resend the email registration with an updated token expiry. Safe to " +
            "assume that the user would always click on the last re-verification email and " +
            "any attempts at generating new token from past (possibly archived/deleted)" +
            "tokens should fail and report an exception. ")
    public ResponseEntity<?> resendRegistrationToken(
            @Param(value = "Initial token sent to user when it was created after registration") @RequestParam("token") String existingToken) {

        EmailVerificationToken token = authService.recreateRegistrationToken(existingToken)
                .orElseThrow(() -> new InvalidTokenRequestException("Email Verification Token",
                        existingToken, "User " +
                        "is already registered. No need to re-generate token"));

        return Optional.ofNullable(token.getUser()).map(user -> {
            UriComponentsBuilder builder = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/api" + "/auth/registrationConfirmation");
            OnRegenerateEmailVerificationEvent regenerateEmailVerificationEvent = new OnRegenerateEmailVerificationEvent(
                    this, token, builder);
            eventPublisher.publishEvent(regenerateEmailVerificationEvent);
            return ResponseEntity.ok()
                    .body(new ApiResponseDto(true, "Email verification resent successfully "));
        }).orElseThrow(() -> new InvalidTokenRequestException("Email Verification Token", existingToken, "No " +
                "user associated with this request. Re-verification denied"));
    }

    /**
     * Confirm the email verification token generated for the user during
     * registration. If token is invalid or token is expired, report error.
     */
    @GetMapping("/registrationConfirmation")
    @Operation(summary = "Confirms the email verification atoken that has been generated for the user during " +
            "registration")
    public ResponseEntity<?> confirmRegistration(
            @Param(value = "the token that was sent to the user email") @RequestParam("token") String token) {

        return authService.confirmEmailRegistration(token)
                .map(user -> {
                    if ((boolean) user.get("alreadyVerified")) {
                        return ResponseEntity.ok().body(
                                new ApiResponseDto(true, "User already verified"));
                    }
                    return ResponseEntity.ok()
                            .body(new ApiResponseDto(true, "User verified successfully"));

                })
                .orElseThrow(() -> new InvalidTokenRequestException("Email Verification Token", token,
                        "Failed to " +
                                "confirm. Please generate a new email verification request"));
    }

    /**
     * Entry point for the user log in. Return the jwt auth token and the refresh
     * token
     */
    @PostMapping("/login")
    @Operation(summary = "Logs the user in to the system and return the auth tokens")
    public ResponseEntity<?> authenticateUser(
            @Param(value = "The LoginRequest payload") @Valid @RequestBody LoginRequestDto loginRequestDto
    , HttpServletRequest httpServletRequest,
            @RequestHeader("User-Agent") String useragent
            ) {
        String clientIp = Utils.getClientIpAddress(httpServletRequest);
         DeviceInfo deviceInfo = new DeviceInfo(clientIp, useragent);
        System.out.println("deviceInfo: /login " + deviceInfo);


        Authentication authentication = authService.authenticateUser(loginRequestDto).orElseThrow(
                () -> new UserLoginException("Couldn't login user [" + loginRequestDto + "]"));

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        log.info("Logged in User returned [API]: " + userDetails.getUsername());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return authService.createRefreshToken(authentication,deviceInfo).map(
                refreshToken->{
                    String jwtToken = authService.generateJWTToken(userDetails);
                    return ResponseEntity.ok().body(new JwtAuthenticationResponseDto(jwtToken, refreshToken,
                            tokenProvider.getExpiryDuration(),refreshTokenService.getExpiryDuration()));

                }
        ).orElseThrow(
                ()->new UserLoginException("Couldn't login user [" + loginRequestDto + "]")
        );

//        return ResponseEntity.ok().body(new JwtAuthenticationResponseDto(jwtToken, "",
//                tokenProvider.getExpiryDuration()));

    }

    /**
     * Receives the reset link request and publishes an event to send email id containing
     * the reset link if the request is valid. In future the deeplink should open within
     * the app itself.
     */
    @PostMapping("/password/resetlink")
    public ResponseEntity<?> sendResetLink(@Valid @RequestBody PasswordResetLinkRequestDto resetLinkRequestDto) {

        return authService.generatePasswordResetToken(resetLinkRequestDto)
                .map((PasswordResetToken passwordResetToken) -> {

                    UriComponentsBuilder urlBuilder = ServletUriComponentsBuilder
                            .fromCurrentContextPath().path("/api" + "/auth/password/reset");

                    OnGenerateResetLinkEvent generateResetLinkMailEvent = new OnGenerateResetLinkEvent(
                            passwordResetToken, urlBuilder);
                    eventPublisher.publishEvent(generateResetLinkMailEvent);

                    return ResponseEntity.ok().body(new ApiResponseDto(true,
                            "Password Reset Link sent successfully."));
                }).orElseThrow(() -> {

                    return new PasswordResetLinkException(resetLinkRequestDto.getEmail(),
                            "Couldn't " +
                                    "create a valid token");
                });

    }

    /**
     * Receives a new passwordResetRequestDto and sends the acknowledgement after
     * changing the password to the user's mail through the event.
     */
    @PostMapping("/password/reset")
    public ResponseEntity<?> resetPassword(
            @Param(value = "The password reset request payload") @Valid @RequestBody PasswordResetRequestDto passwordResetRequestDto) {
        System.out.printf("password reset request payload: %s\n", passwordResetRequestDto.toString());
        return authService.resetPassword(passwordResetRequestDto).map(changedUser -> {
            OnUserAccountChangeEvent onUserAccountChangeEvent = new OnUserAccountChangeEvent(changedUser, "Reset Password", "Changed.");
            eventPublisher.publishEvent(onUserAccountChangeEvent);
            return ResponseEntity.ok(new ApiResponseDto(true, "Password changed successfully"));

        }).orElseThrow(() -> new PasswordResetException(passwordResetRequestDto.getToken(), "Error in resetting " +
                "password"));

    }

    /**
     * Refresh the expired jwt token using a refresh token for the specific device
     * and return a new token to the caller
     */
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(
            @RequestHeader("User-Agent") String userAgent,
HttpServletRequest httpServletRequest,
//            @RequestHeader("X-Forwarded-For") String ipAddress,
            @RequestBody TokenRefreshRequest tokenRefreshRequest
    ){
        String clientIp = Utils.getClientIpAddress(httpServletRequest);
        DeviceInfo deviceInfo = new DeviceInfo(clientIp, userAgent);
        System.out.println("deviceInfo: /refresh " + deviceInfo);
       return authService.refreshJwtToken(tokenRefreshRequest, deviceInfo).map(token->{
//            return new ResponseEntity<>(token, HttpStatus.OK);

           String updatedRefToken = refreshTokenService.refreshToken(refreshTokenService.getClaimsFromToken(tokenRefreshRequest.getRefreshToken()).getSubject(),deviceInfo);
           return ResponseEntity.ok().body(new JwtAuthenticationResponseDto(token, updatedRefToken,
                   tokenProvider.getExpiryDuration(),refreshTokenService.getExpiryDuration()));

       }).orElseThrow(()->new TokenRefreshException(tokenRefreshRequest.getRefreshToken(),"INside error"));


    }

}
