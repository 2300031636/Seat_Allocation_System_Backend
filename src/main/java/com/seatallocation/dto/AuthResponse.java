package com.seatallocation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse {
    private boolean success;
    private String message;
    private Long userId;
    private String username;
    private String fullName;
    private String email;

    public static AuthResponse success(String message, Long userId, String username, String fullName, String email) {
        return AuthResponse.builder()
                .success(true)
                .message(message)
                .userId(userId)
                .username(username)
                .fullName(fullName)
                .email(email)
                .build();
    }

    public static AuthResponse failure(String message) {
        return AuthResponse.builder()
                .success(false)
                .message(message)
                .build();
    }
}
