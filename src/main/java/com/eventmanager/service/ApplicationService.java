package com.eventmanager.service;

import com.eventmanager.dto.ApplicationDTO;
import com.eventmanager.entity.Application;
import com.eventmanager.entity.Event;
import com.eventmanager.entity.User;
import com.eventmanager.repository.ApplicationRepository;
import com.eventmanager.repository.EventRepository;
import com.eventmanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApplicationService {
    
    private final ApplicationRepository applicationRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    
    public ApplicationDTO.Response applyForEvent(Long eventId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));
        
        // Check if event is expired
        if (event.getExpiryDate().isBefore(java.time.LocalDate.now())) {
            throw new RuntimeException("Cannot apply for expired event");
        }
        
        // Check if user has already applied
        if (applicationRepository.existsByEventAndUser(event, user)) {
            throw new RuntimeException("You have already applied for this event");
        }
        
        Application application = Application.builder()
                .event(event)
                .user(user)
                .status(Application.ApplicationStatus.PENDING)
                .build();
        
        Application savedApplication = applicationRepository.save(application);
        return convertToDTO(savedApplication);
    }
    
    public List<ApplicationDTO.Response> getUserApplications() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        return applicationRepository.findByUser(user).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public List<ApplicationDTO.Response> getAllApplications() {
        // Only admin can access all applications
        checkAdminRole();
        
        return applicationRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public List<ApplicationDTO.Response> getFilteredApplications(Long eventId, Application.ApplicationStatus status) {
        // Only admin can filter applications
        checkAdminRole();
        
        if (eventId != null && status != null) {
            return applicationRepository.findByEventIdAndStatus(eventId, status).stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } else if (eventId != null) {
            Event event = eventRepository.findById(eventId)
                    .orElseThrow(() -> new RuntimeException("Event not found"));
            return applicationRepository.findByEvent(event).stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } else if (status != null) {
            return applicationRepository.findByStatus(status).stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } else {
            return getAllApplications();
        }
    }
    
    public ApplicationDTO.StatusResponse approveApplication(Long applicationId) {
        return updateApplicationStatus(applicationId, Application.ApplicationStatus.APPROVED, 
                                     "Application approved successfully");
    }
    
    public ApplicationDTO.StatusResponse rejectApplication(Long applicationId) {
        return updateApplicationStatus(applicationId, Application.ApplicationStatus.REJECTED, 
                                     "Application rejected successfully");
    }
    
    private ApplicationDTO.StatusResponse updateApplicationStatus(Long applicationId, 
                                                                Application.ApplicationStatus status, 
                                                                String message) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));
        
        // Only admin can update application status
        checkAdminRole();
        
        // Prevent changing status from approved/rejected back to pending
        if (application.getStatus() != Application.ApplicationStatus.PENDING) {
            throw new RuntimeException("Cannot change status of already processed application");
        }
        
        application.setStatus(status);
        applicationRepository.save(application);
        
        return new ApplicationDTO.StatusResponse(message, status);
    }
    
    private void checkAdminRole() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        if (user.getRole() != User.UserRole.ADMIN) {
            throw new RuntimeException("Only administrators can perform this action");
        }
    }
    
    private ApplicationDTO.Response convertToDTO(Application application) {
        return new ApplicationDTO.Response(
                application.getId(),
                application.getEvent().getId(),
                application.getEvent().getTitle(),
                application.getUser().getId(),
                application.getUser().getUsername(),
                application.getUser().getEmail(),
                application.getApplicationDate(),
                application.getStatus()
        );
    }
}