package com.cloudcompiler.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.UUID;

public class AuthDtos {

    public static class LoginRequest {
        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        private String email;

        @NotBlank(message = "Password is required")
        private String password;

        public LoginRequest() {}

        public LoginRequest(String email, String password) {
            this.email = email;
            this.password = password;
        }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    public static class RegisterRequest {
        @NotBlank(message = "Name is required")
        @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
        private String name;

        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        private String email;

        @NotBlank(message = "Password is required")
        @Size(min = 6, max = 100, message = "Password must be at least 6 characters")
        private String password;

        public RegisterRequest() {}

        public RegisterRequest(String name, String email, String password) {
            this.name = name;
            this.email = email;
            this.password = password;
        }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    public static class AuthResponse {
        private String token;
        private String tokenType = "Bearer";
        private UserDto user;

        public AuthResponse() {}

        public AuthResponse(String token, String tokenType, UserDto user) {
            this.token = token;
            this.tokenType = tokenType != null ? tokenType : "Bearer";
            this.user = user;
        }

        public static AuthResponseBuilder builder() {
            return new AuthResponseBuilder();
        }

        public static class AuthResponseBuilder {
            private String token;
            private String tokenType = "Bearer";
            private UserDto user;

            public AuthResponseBuilder token(String token) { this.token = token; return this; }
            public AuthResponseBuilder tokenType(String tokenType) { this.tokenType = tokenType; return this; }
            public AuthResponseBuilder user(UserDto user) { this.user = user; return this; }

            public AuthResponse build() {
                return new AuthResponse(token, tokenType, user);
            }
        }

        public String getToken() { return token; }
        public void setToken(String token) { this.token = token; }
        public String getTokenType() { return tokenType; }
        public void setTokenType(String tokenType) { this.tokenType = tokenType; }
        public UserDto getUser() { return user; }
        public void setUser(UserDto user) { this.user = user; }
    }

    public static class UserDto {
        private UUID id;
        private String name;
        private String email;

        public UserDto() {}

        public UserDto(UUID id, String name, String email) {
            this.id = id;
            this.name = name;
            this.email = email;
        }

        public static UserDtoBuilder builder() {
            return new UserDtoBuilder();
        }

        public static class UserDtoBuilder {
            private UUID id;
            private String name;
            private String email;

            public UserDtoBuilder id(UUID id) { this.id = id; return this; }
            public UserDtoBuilder name(String name) { this.name = name; return this; }
            public UserDtoBuilder email(String email) { this.email = email; return this; }

            public UserDto build() {
                return new UserDto(id, name, email);
            }
        }

        public UUID getId() { return id; }
        public void setId(UUID id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
    }
}
