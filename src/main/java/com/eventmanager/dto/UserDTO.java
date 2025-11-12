package com.eventmanager.dto;

import com.eventmanager.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class UserDTO {
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateRequest {
        private String username;
        private String email;
        private String password;
        private User.UserRole role;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Long id;
        private String username;
        private String email;
        private User.UserRole role;
        private LocalDateTime createdDate;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateRequest {
        private String username;
        private String email;
    }
}