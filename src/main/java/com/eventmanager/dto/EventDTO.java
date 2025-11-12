package com.eventmanager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

public class EventDTO {
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateRequest {
        private String title;
        private String description;
        private LocalDate expiryDate;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Long id;
        private String title;
        private String description;
        private LocalDate uploadDate;
        private LocalDate expiryDate;
        private Long organizerId;
        private String organizerName;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateRequest {
        private String title;
        private String description;
        private LocalDate expiryDate;
    }
} 
