package com.ewallet.dto;

public class AuthResponse {

    private String token;
    private UserDto user;

    public AuthResponse(String token, UserDto user) {
        this.token = token;
        this.user = user;
    }

    public String getToken() { return token; }
    public UserDto getUser() { return user; }

    public static class UserDto {
        private String id;
        private String fullName;
        private String email;

        public UserDto(String id, String fullName, String email) {
            this.id = id;
            this.fullName = fullName;
            this.email = email;
        }

        public String getId()       { return id; }
        public String getFullName() { return fullName; }
        public String getEmail()    { return email; }
    }
}