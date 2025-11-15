package com.eventmanager.controller;

import com.eventmanager.dto.ApplicationDTO;
import com.eventmanager.entity.Application.ApplicationStatus;
import com.eventmanager.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
public class ApplicationController {
    
    private final ApplicationService applicationService;
    
    @PostMapping("/apply/{eventId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApplicationDTO.Response> applyForEvent(@PathVariable Long eventId) {
        return ResponseEntity.ok(applicationService.applyForEvent(eventId));
    }
    
    @GetMapping("/my-applications")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<ApplicationDTO.Response>> getUserApplications() {
        return ResponseEntity.ok(applicationService.getUserApplications());
    }
    
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ApplicationDTO.Response>> getAllApplications(
            @RequestParam(required = false) Long eventId,
            @RequestParam(required = false) ApplicationStatus status) {
        return ResponseEntity.ok(applicationService.getFilteredApplications(eventId, status));
    }
    
    @PutMapping("/{id}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApplicationDTO.StatusResponse> approveApplication(@PathVariable Long id) {
        return ResponseEntity.ok(applicationService.approveApplication(id));
    }
    
    @PutMapping("/{id}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApplicationDTO.StatusResponse> rejectApplication(@PathVariable Long id) {
        return ResponseEntity.ok(applicationService.rejectApplication(id));
    }
}