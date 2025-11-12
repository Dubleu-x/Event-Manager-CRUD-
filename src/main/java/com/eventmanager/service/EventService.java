package com.eventmanager.service;

import com.eventmanager.dto.EventDTO;
import com.eventmanager.entity.Event;
import com.eventmanager.entity.User;
import com.eventmanager.repository.EventRepository;
import com.eventmanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventService {
    
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    
    public EventDTO.Response createEvent(EventDTO.CreateRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User organizer = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Organizer not found"));
        
        Event event = Event.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .uploadDate(LocalDate.now())
                .expiryDate(request.getExpiryDate())
                .organizer(organizer)
                .build();
        
        Event savedEvent = eventRepository.save(event);
        return convertToDTO(savedEvent);
    }
    
    public List<EventDTO.Response> getAllEvents() {
        return eventRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public List<EventDTO.Response> getActiveEvents() {
        return eventRepository.findActiveEvents(LocalDate.now()).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public EventDTO.Response getEventById(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));
        return convertToDTO(event);
    }
    
    public EventDTO.Response updateEvent(Long id, EventDTO.UpdateRequest request) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));
        
        // Check if current user is the organizer
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!event.getOrganizer().getUsername().equals(username)) {
            throw new RuntimeException("Only the organizer can update this event");
        }
        
        if (request.getTitle() != null && !request.getTitle().isEmpty()) {
            event.setTitle(request.getTitle());
        }
        if (request.getDescription() != null && !request.getDescription().isEmpty()) {
            event.setDescription(request.getDescription());
        }
        if (request.getExpiryDate() != null) {
            event.setExpiryDate(request.getExpiryDate());
        }
        
        Event updatedEvent = eventRepository.save(event);
        return convertToDTO(updatedEvent);
    }
    
    public void deleteEvent(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));
        
        // Check if current user is the organizer
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!event.getOrganizer().getUsername().equals(username)) {
            throw new RuntimeException("Only the organizer can delete this event");
        }
        
        eventRepository.deleteById(id);
    }
    
    private EventDTO.Response convertToDTO(Event event) {
        return new EventDTO.Response(
            event.getId(),
            event.getTitle(),
            event.getDescription(),
            event.getUploadDate(),
            event.getExpiryDate(),
            event.getOrganizer().getId(),
            event.getOrganizer().getUsername()
        );
    }
}