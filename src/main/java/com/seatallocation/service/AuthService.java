package com.seatallocation.service;

import com.seatallocation.dto.AuthResponse;
import com.seatallocation.dto.SignInRequest;
import com.seatallocation.dto.SignUpRequest;
import com.seatallocation.entity.User;
import com.seatallocation.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public AuthResponse signUp(SignUpRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            return AuthResponse.failure("Username already exists");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            return AuthResponse.failure("Email already exists");
        }

        User user = User.builder()
                .fullName(request.getFullName())
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        User savedUser = userRepository.save(user);
        return AuthResponse.success("Account created successfully", savedUser.getId(), savedUser.getUsername(), savedUser.getFullName(), savedUser.getEmail());
    }

    @Transactional(readOnly = true)
    public AuthResponse signIn(SignInRequest request) {
        return userRepository.findByUsername(request.getUsername())
                .filter(user -> passwordEncoder.matches(request.getPassword(), user.getPassword()))
                .map(user -> AuthResponse.success("Signed in successfully", user.getId(), user.getUsername(), user.getFullName(), user.getEmail()))
                .orElseGet(() -> AuthResponse.failure("Invalid username or password"));
    }
}
