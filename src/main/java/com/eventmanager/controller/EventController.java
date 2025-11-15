package com.eventmanager.controller;

import com.eventmanager.dto.EventDTO;
import com.eventmanager.service.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {
    
    private final EventService eventService;
    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EventDTO.Response> createEvent(@Valid @RequestBody EventDTO.CreateRequest request) {
        return ResponseEntity.ok(eventService.createEvent(request));
    }
    
    // Add this method to your existing EventController
@GetMapping("/available")
public ResponseEntity<List<EventDTO.Response>> getAvailableEvents() {
    return ResponseEntity.ok(eventService.getActiveEvents());
}

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<EventDTO.Response>> getAllEvents() {
        return ResponseEntity.ok(eventService.getAllEvents());
    }
    
    @GetMapping("/active")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<EventDTO.Response>> getActiveEvents() {
        return ResponseEntity.ok(eventService.getActiveEvents());
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<EventDTO.Response> getEventById(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.getEventById(id));
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EventDTO.Response> updateEvent(
            @PathVariable Long id, 
            @Valid @RequestBody EventDTO.UpdateRequest request) {
        return ResponseEntity.ok(eventService.updateEvent(id, request));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return ResponseEntity.ok().build();
    }
}