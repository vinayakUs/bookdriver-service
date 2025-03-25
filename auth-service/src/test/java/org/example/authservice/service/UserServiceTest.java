package org.example.authservice.service;

import org.example.authservice.model.RoleName;
import org.example.authservice.model.dto.RegistrationRequestDto;
import org.example.authservice.model.entity.Role;
import org.example.authservice.model.entity.User;
import org.example.authservice.repository.RoleRepository;
import org.example.authservice.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Optional;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private RoleService roleService;
    @Mock
    private PasswordEncoder encoder;


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
    @Test
    void existsByEmailAndUsername() {
        when(userRepository.existsByUsername("admin")).thenReturn(true);
        when(userRepository.existsByEmail("admin@example.com")).thenReturn(true);
        boolean res = userService.existsByUsername("admin");
        boolean res2 = userService.existsByEmail("admin@example.com");
        assertTrue(res);
        assertTrue(res2);
        verify(userRepository).existsByEmail("admin@example.com");
        verify(userRepository).existsByUsername("admin");
    }

    @Test
    void findUserByIdAndEmail() {
        User user = new User();
        user.setUsername("admin");
        user.setPassword("password");
        user.setEmail("admin@example.com");
        user.setId(1L);
        when(userRepository.findByEmail("admin@example.com")).thenReturn(Optional.of(user));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        Optional<User> user1 = userService.findByEmail("admin@example.com");
        assertTrue(user1.isPresent());
        assertEquals("admin@example.com",user1.get().getEmail());

        Optional<User> user2 = userService.findById(1L);
        assertTrue(user2.isPresent());
        assertEquals(1L,user2.get().getId());

        verify(userRepository).findByEmail("admin@example.com");
        verify(userRepository).findById(1L);
    }
    @Test
    void testCreateUser_AsUser() {
        RegistrationRequestDto request = new RegistrationRequestDto();
        request.setEmail("user@example.com");
        request.setUsername("regularUser");
        request.setPassword("password");
        request.setRegisterAsAdmin(false);

        when(encoder.encode(anyString())).thenReturn("encodedPassword");
        Role user = new Role(RoleName.ROLE_USER);
        Role admin = new Role(RoleName.ROLE_ADMIN);

        when(roleService.findAll()).thenReturn(Set.of(user,admin));

        User createdUser = userService.createUser(request);

        assertNotNull(createdUser);
        assertEquals("user@example.com", createdUser.getEmail());
        assertEquals("encodedPassword", createdUser.getPassword());
        assertEquals(createdUser.getRoles(), Set.of(user));
        assertFalse(createdUser.getRoles().contains(admin)); // Should not have admin role
        assertTrue(createdUser.getRoles().contains(user));
        assertTrue(createdUser.getActive());

        verify(encoder, times(1)).encode("password");
        verify(roleService, times(1)).findAll();
    }
}
