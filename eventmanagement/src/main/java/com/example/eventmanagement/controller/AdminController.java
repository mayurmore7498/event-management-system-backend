package com.example.eventmanagement.controller;

import com.example.eventmanagement.model.Event;
import com.example.eventmanagement.model.User;
import com.example.eventmanagement.service.EventService;
import com.example.eventmanagement.repository.EventRepository;
import com.example.eventmanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:5173")
public class AdminController {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventService eventService; 

   
    @GetMapping("/pending")
    public List<Event> getPendingEvents() {
        return eventRepository.findByStatus(Event.Status.PENDING);
    }

  
    @PutMapping("/approve/{id}")
    public String approveEvent(@PathVariable Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));
        event.setStatus(Event.Status.APPROVED);
        eventRepository.save(event);
        return "Event approved successfully!";
    }

   
    @PutMapping("/reject/{id}")
    public String rejectEvent(@PathVariable Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));
        event.setStatus(Event.Status.REJECTED);
        eventRepository.save(event);
        return "Event rejected successfully!";
    }

  
    @GetMapping("/organizers")
    public List<User> getAllOrganizers() {
        return userRepository.findByRole(User.Role.ORGANIZER);
    }

    
    @GetMapping("/events")
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

   
    @DeleteMapping("/events/{id}")
    public String deleteEvent(@PathVariable Long id) {
        boolean deleted = eventService.deleteEvent(id);

        if (!deleted) {
            return "Event not found!";
        }

        return "Event deleted successfully ✅";
    }
}