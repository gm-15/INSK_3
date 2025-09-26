package com.insk.insk_backend.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

public class UserDto {

    /**
     * 회원가입 요청을 위한 DTO
     */
    @Getter
    @NoArgsConstructor
    public static class SignupRequest {
        private String email;
        private String password;
    }

    /**
     * 회원가입 성공 시 응답을 위한 DTO
     */
    @Getter
    public static class SignupResponse {
        private Long userId;
        private String email;

        public SignupResponse(Long userId, String email) {
            this.userId = userId;
            this.email = email;
        }
    }
}

