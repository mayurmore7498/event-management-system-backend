package com.example.eventmanagement.service;

import com.example.eventmanagement.model.Event;
import com.example.eventmanagement.model.User;
import com.example.eventmanagement.repository.EventRepository;
import com.example.eventmanagement.repository.BookingRepository;
import com.example.eventmanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class EventService {

    @Autowired
    private EventRepository repo;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookingRepository bookingRepository; 

   
    public List<Event> getAllEvents() {
        return repo.findAll();
    }

    
    public List<Event> getPendingEvents() {
        return repo.findByStatus(Event.Status.PENDING);
    }

  
    public List<Event> getApprovedEvents() {
        LocalDate today = LocalDate.now();
        List<Event> approved = repo.findByStatus(Event.Status.APPROVED);
        approved.removeIf(e -> e.getDate().isBefore(today));
        return approved;
    }

   
    public List<Event> getOrganizerEvents(Long organizerId) {
        return repo.findByOrganizerId(organizerId);
    }

   
    public Event createEvent(Event e) {
        String organizerName = getOrganizerNameById(e.getOrganizerId());
        e.setOrganizerName(organizerName);
        return repo.save(e);
    }

    public String getOrganizerNameById(Long id) {
        return userRepository.findById(id)
                .map(User::getUsername)
                .orElse("Unknown Organizer");
    }

    
    public Event approveEvent(Long id) {
        Event e = repo.findById(id).orElseThrow(() -> new RuntimeException("Event not found"));
        e.setStatus(Event.Status.APPROVED);
        return repo.save(e);
    }

    
    public Event rejectEvent(Long id) {
        Event e = repo.findById(id).orElseThrow(() -> new RuntimeException("Event not found"));
        e.setStatus(Event.Status.REJECTED);
        return repo.save(e);
    }

   
    public Event getEventById(Long id) {
        return repo.findById(id).orElse(null);
    }

   
    public Event updateEvent(Long id, Event updatedEvent) {
        Event existing = repo.findById(id).orElse(null);
        if (existing == null) return null;

        existing.setTitle(updatedEvent.getTitle());
        existing.setDate(updatedEvent.getDate());
        existing.setLocation(updatedEvent.getLocation());
        existing.setCapacity(updatedEvent.getCapacity());
        existing.setTicketPrice(updatedEvent.getTicketPrice());
        existing.setDescription(updatedEvent.getDescription());
        existing.setImage(updatedEvent.getImage());

        return repo.save(existing);
    }

   
    public boolean deleteEvent(Long id) {

        if (!repo.existsById(id)) {
            return false;
        }

        
        bookingRepository.deleteByEventId(id);

        
        repo.deleteById(id);

        return true;
    }
}
