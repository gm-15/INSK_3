package com.insk.insk_backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UserDto {

    // --- 회원가입 DTO ---
    @Getter
    @NoArgsConstructor
    public static class SignUpRequest {

        @NotBlank(message = "이메일은 필수 입력 값입니다.")
        @Email(message = "이메일 형식에 맞지 않습니다.")
        private String email;

        @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
        @Size(min = 8, message = "비밀번호는 8자 이상이어야 합니다.")
        private String password;
    }

    @Getter
    public static class SignUpResponse {
        private final Long userId;
        private final String email;

        public SignUpResponse(Long userId, String email) {
            this.userId = userId;
            this.email = email;
        }
    }

    // --- 로그인 DTO ---
    @Getter
    @NoArgsConstructor
    public static class LoginRequest {
        @NotBlank(message = "이메일은 필수 입력 값입니다.")
        @Email(message = "이메일 형식에 맞지 않습니다.")
        private String email;

        @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
        private String password;
    }

    @Getter
    public static class LoginResponse {
        private final String accessToken;

        public LoginResponse(String accessToken) {
            this.accessToken = accessToken;
        }
    }
}

