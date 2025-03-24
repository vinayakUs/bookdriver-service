package org.example.authservice.service;

import org.example.authservice.model.dto.RegistrationRequestDto;
import org.example.authservice.model.entity.User;
import org.example.authservice.repository.UserRepository;
import org.example.authservice.security.JwtTokenProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
     class AuthServiceTest {

        @Mock
        private UserService userService;

        @Mock
        private UserRepository userRepository;

        @Mock
        private JwtTokenProvider jwtTokenProvider;

        @Mock
        private AuthenticationManager authenticationManager;

        @Mock
        private PasswordEncoder passwordEncoder;

        @InjectMocks
        private AuthService authService;

        @BeforeEach
        void setUp() {
//           registrationRequest = new RegistrationRequestDto();
//           registrationRequest.setEmail("test@example.com");
//           registrationRequest.setUsername("testUser");
//           registrationRequest.setPassword("password123");
//
//           user = new User();
//           user.setEmail("test@example.com");
//           user.setUsername("testUser");

        }

        @Test
         void shouldRegisterUser() {

           // Arrange
           RegistrationRequestDto request = new RegistrationRequestDto();
           request.setEmail("test@example.com");
           request.setUsername("testUser");

           User user = new User();
           user.setEmail(request.getEmail());
           user.setUsername(request.getUsername());

           when(userService.existsByEmail(request.getEmail())).thenReturn(false);
           when(userService.createUser(request)).thenReturn(user);
           when(userService.save(user)).thenReturn(user);
           Optional<User> user1 = authService.registerUser(request);
           assertTrue(user1.isPresent());







        }


}