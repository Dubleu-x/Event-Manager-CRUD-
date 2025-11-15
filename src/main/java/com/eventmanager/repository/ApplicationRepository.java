package com.eventmanager.repository;

import com.eventmanager.entity.Application;
import com.eventmanager.entity.Event;
import com.eventmanager.entity.User;
import com.eventmanager.entity.Application.ApplicationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {
    
    Optional<Application> findByEventAndUser(Event event, User user);
    List<Application> findByEvent(Event event);
    List<Application> findByUser(User user);
    List<Application> findByEventAndStatus(Event event, ApplicationStatus status);
    List<Application> findByStatus(ApplicationStatus status);
    boolean existsByEventAndUser(Event event, User user);
    
    @Query("SELECT a FROM Application a WHERE " +
           "(:eventId IS NULL OR a.event.id = :eventId) AND " +
           "(:status IS NULL OR a.status = :status)")
    List<Application> findByEventIdAndStatus(@Param("eventId") Long eventId, 
                                           @Param("status") ApplicationStatus status);
}