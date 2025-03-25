package org.example.authservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.authservice.contoller.AuthController;
import org.example.authservice.model.CustomUserDetails;
import org.example.authservice.model.dto.LoginRequestDto;
import org.example.authservice.security.JwtTokenProvider;
import org.example.authservice.service.AuthService;
import org.example.authservice.service.RefreshTokenService;
import org.example.authservice.utils.Utils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.token.TokenService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthControllerTest {
    private MockMvc mockMvc;

    @Mock
    private AuthService authService;

    @Mock
    private JwtTokenProvider tokenProvider;
    @Mock
    private HttpServletRequest httpServletRequest;

    @Mock
    private RefreshTokenService refreshTokenService;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @Test
    void testAuthenticateUser_Success() throws Exception {
        // Arrange
        String email = "user@example.com";
        String password = "password";
        String jwtToken = "mocked-jwt-token";
        String refreshToken = "mocked-refresh-token";
        long expiryDuration = 3600;

        LoginRequestDto loginRequestDto = new LoginRequestDto(email, password);
        Authentication authentication = mock(Authentication.class);
        CustomUserDetails userDetails = mock(CustomUserDetails.class);

        when(userDetails.getUsername()).thenReturn(email);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(authService.authenticateUser(any(LoginRequestDto.class))).thenReturn(Optional.of(authentication));
        when(authService.createRefreshToken(eq(authentication), any())).thenReturn(Optional.of(refreshToken));
        when(authService.generateJWTToken(userDetails)).thenReturn(jwtToken);
        when(tokenProvider.getExpiryDuration()).thenReturn(expiryDuration);
        when(refreshTokenService.getExpiryDuration()).thenReturn(expiryDuration);
        when(Utils.getClientIpAddress(httpServletRequest)).thenReturn("127.0.0.1");

        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("User-Agent", "Test-Agent")
                        .content(new ObjectMapper().writeValueAsString(loginRequestDto)))
                .andExpect(status().isOk())
;
        verify(authService, times(1)).authenticateUser(any(LoginRequestDto.class));
    }
}