package org.example.authservice.service;

import jakarta.validation.constraints.AssertTrue;
import org.example.authservice.model.RoleName;
import org.example.authservice.model.entity.Role;
import org.example.authservice.model.entity.User;
import org.example.authservice.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void shouldReturnTrueifExistByEmail() {
        when(userRepository.existsByEmail("admin@example.com")).thenReturn(true);
        boolean res = userService.existsByEmail("admin@example.com");
        assertTrue(res);
        verify(userRepository).existsByEmail("admin@example.com");
    }
    @Test
    void shouldReturnFalseifNotExistByEmail() {
        when(userRepository.existsByEmail("notfound@example.com")).thenReturn(false);
        boolean res = userService.existsByEmail("notfound@example.com");
        assertFalse(res);
        verify(userRepository).existsByEmail("notfound@example.com");
    }
    @Test
    void saveUserToDatabase() {
        User user = new User();
        user.setUsername("username");
        user.setPassword("password");
        user.setEmail("email@example.com");

        when(userService.save(user)).thenReturn(user);
        User userSaved = userService.save(user);
        System.out.println(userSaved.getUsername());
        assertNotNull(userSaved);
        assertEquals("email@example.com",userSaved.getEmail());
        verify(userRepository).save(user);
    }
}
