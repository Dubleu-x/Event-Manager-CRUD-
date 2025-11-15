package com.eventmanager.dto;

import com.eventmanager.entity.Application.ApplicationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class ApplicationDTO {
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Long id;
        private Long eventId;
        private String eventTitle;
        private Long userId;
        private String userName;
        private String userEmail;
        private LocalDateTime applicationDate;
        private ApplicationStatus status;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StatusResponse {
        private String message;
        private ApplicationStatus status;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FilterRequest {
        private Long eventId;
        private ApplicationStatus status;
    }
}