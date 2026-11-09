package com.seatallocation.service;

import com.seatallocation.dto.AuthResponse;
import com.seatallocation.dto.SignInRequest;
import com.seatallocation.dto.SignUpRequest;
import com.seatallocation.entity.User;
import com.seatallocation.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthService authService;

    @Test
    void signUpShouldPersistUserWithEncodedPassword() {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        authService = new AuthService(userRepository, passwordEncoder);

        when(userRepository.existsByUsername("alice")).thenReturn(false);
        when(userRepository.existsByEmail("alice@example.com")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        AuthResponse response = authService.signUp(new SignUpRequest("Alice", "alice", "alice@example.com", "password123"));

        assertTrue(response.isSuccess());
        assertEquals("alice", response.getUsername());
        assertNotNull(response.getUserId());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void signInShouldFailForWrongPassword() {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        authService = new AuthService(userRepository, passwordEncoder);

        User user = User.builder()
                .id(1L)
                .fullName("Alice")
                .username("alice")
                .email("alice@example.com")
                .password(passwordEncoder.encode("password123"))
                .build();

        when(userRepository.findByUsername("alice")).thenReturn(java.util.Optional.of(user));

        AuthResponse response = authService.signIn(new SignInRequest("alice", "wrong-password"));

        assertFalse(response.isSuccess());
        assertEquals("Invalid username or password", response.getMessage());
    }
}
