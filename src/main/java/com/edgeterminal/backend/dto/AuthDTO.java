package com.edgeterminal.backend.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class AuthDTO {

    @Data
    public static class LoginRequest {
        @NotBlank(message = "Username is required")
        private String username;

        @NotBlank(message = "Password is required")
        private String password;

        private String code;
        private String uuid;
    }

    @Data
    public static class LoginResponse {
        private String token;
        private String tokenType = "Bearer";
        private Long expiresIn;
        private UserInfo user;

        @Data
        public static class UserInfo {
            private Long id;
            private String username;
            private String nickname;
            private String avatar;
            private String email;
        }
    }
}