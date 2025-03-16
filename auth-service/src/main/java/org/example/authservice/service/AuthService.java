package org.example.authservice.service;

import org.example.authservice.exception.PasswordResetLinkException;
import org.example.authservice.exception.ResourceAlreadyInUseException;
import org.example.authservice.exception.ResourceNotFoundException;
import org.example.authservice.model.CustomUserDetails;
import org.example.authservice.model.DeviceInfo;
import org.example.authservice.model.dto.*;
import org.example.authservice.model.entity.EmailVerificationToken;
import org.example.authservice.model.entity.PasswordResetToken;
import org.example.authservice.model.entity.User;
import org.example.authservice.repository.UserRepository;
import org.example.authservice.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor

public class AuthService {
    private final UserService userService;
    private final UserRepository userRepository;
    private final EmailVerificationTokenService emailVerificationTokenService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordResetTokenService passwordResetTokenService;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenService refreshTokenService;

    /**
     * Checks if the given email already exists in the database repository or not
     *
     * @return true if the email exists else false
     */
    public Boolean emailAlreadyExists(String email) {
        return userService.existsByEmail(email);
    }

    /**
     * Checks if the given email already exists in the database repository or not
     *
     * @return true if the email exists else false
     */
    public Boolean usernameAlreadyExists(String username) {
        return userService.existsByUsername(username);
    }

    /**
     * Check is the user exists given the email: naturalId
     */
    public Boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    /**
     * Registers a new user in the database by performing a series of quick checks.
     *
     * @return A user object if successfully created
     */
    public Optional<User> registerUser(RegistrationRequestDto newRegistrationRequest) {
        String newRegistrationRequestEmail = newRegistrationRequest.getEmail();
        if (emailAlreadyExists(newRegistrationRequestEmail)) {
            log.error("Email already exists: " + newRegistrationRequestEmail);
            throw new ResourceAlreadyInUseException("Email", "Address", newRegistrationRequestEmail);
        }
        log.info("Trying to register new user [" + newRegistrationRequestEmail + "]");
        User newUser = userService.createUser(newRegistrationRequest);
        User registeredNewUser = userService.save(newUser);
        return Optional.ofNullable(registeredNewUser);
    }

    /**
     * Confirms the user verification based on the token expiry and mark the user as
     * active.
     * If user is already verified, save the unnecessary database calls.
     */
    public Optional<Map<String, Object>> confirmEmailRegistration(String token) {

        EmailVerificationToken tokenDb = emailVerificationTokenService.findByToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Token", "Email verification", token));
        Map<String, Object> result = new HashMap<>();

        User registeredUser = tokenDb.getUser();
        if (registeredUser.getEmailVerified()) {
            log.info("User [" + token + "] already registered.");
            result.put("User", registeredUser);
            result.put("alreadyVerified", true);

            return Optional.of(result);

        }
        emailVerificationTokenService.verifyExpiration(tokenDb);
        tokenDb.setConfirmedStatus();
        emailVerificationTokenService.save(tokenDb);
        registeredUser.markVerificationConfirmed();
        userService.save(registeredUser);
        result.put("User", registeredUser);
        result.put("alreadyVerified", Boolean.valueOf(false));

        return Optional.of(result);
    }

    /**
     * Attempt to regenerate a new email verification token given a valid
     * previous expired token. If the previous token is valid, increase its expiry
     * else update the token value and add a new expiration.
     */
    public Optional<EmailVerificationToken> recreateRegistrationToken(String existingToken) {
        EmailVerificationToken emailVerificationToken = emailVerificationTokenService.findByToken(existingToken)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Token", "Existing email verification", existingToken));

        if (emailVerificationToken.getUser().getEmailVerified()) {
            return Optional.empty();
        }

        return Optional
                .ofNullable(emailVerificationTokenService.updateExistingTokenWithNameAndExpiry(emailVerificationToken));

    }

    /**
     * Authenticate user and log them in given a loginRequest
     */
    public Optional<Authentication> authenticateUser(LoginRequestDto loginRequestdDto) {
        return Optional.ofNullable(
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                        loginRequestdDto.getEmail(), loginRequestdDto.getPassword())));
    }

 

    /**
     * Generates a JWT token for the validated client
     */
    public String generateJWTToken(CustomUserDetails userDetails) {

        return jwtTokenProvider.generateToken(userDetails);
    }

    /**
     * Generates a password reset token from the given reset request
     */
    public Optional<PasswordResetToken> generatePasswordResetToken(PasswordResetLinkRequestDto passwordResetLinkRequest) {
        String email = passwordResetLinkRequest.getEmail();
        return userService.findByEmail(email)
                .map(passwordResetTokenService::createToken)
                .orElseThrow(() -> new PasswordResetLinkException(email, "No matching user found for the given request"));
    }

    /**
     * Reset a password given a reset request and return the updated user
     * The reset token must match the email for the user and cannot be used again
     */
    public Optional<User> resetPassword(PasswordResetRequestDto passwordResetRequestDto) {
        PasswordResetToken passwordResetToken = passwordResetTokenService.getValidToken(passwordResetRequestDto);
        String encodedPass = passwordEncoder.encode(passwordResetRequestDto.getPassword());
        log.info(passwordResetToken.toString());

        return Optional.of(passwordResetToken).map(passwordResetTokenService::claimToken).map(PasswordResetToken::getUser).map(
                user -> {
                    user.setPassword(encodedPass);
                    userService.save(user);
                    return user;
                }
        );

    }
    /**
     * refresh token for AUthenticated user
     */
    public Optional<String> createRefreshToken(Authentication authentication, DeviceInfo deviceInfo) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

return       Optional.of(  refreshTokenService.refreshToken(userDetails.getUsername(),deviceInfo));


    }

/**
 * Refresh the expired jwt token using a refresh token and device info. The
 * * refresh token is mapped to a specific device and if it is unexpired, can help
 * * generate a new jwt. If the refresh token is inactive for a device or it is expired,
 * * throw appropriate errors.
 */
//public Optional<String> refreshJwtToken(RefreshTokenReqeust refreshTokenReqeust) {
//    String refreshedToken = refreshTokenReqeust.getRefreshToken();
//    log.info("Refreshed token: " + refreshedToken);
//
//}


}
