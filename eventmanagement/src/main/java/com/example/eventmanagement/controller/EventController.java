package com.example.eventmanagement.controller;

import com.example.eventmanagement.model.Booking;
import com.example.eventmanagement.model.Event;
import com.example.eventmanagement.repository.EventRepository;
import com.example.eventmanagement.service.EventService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/events")
@CrossOrigin(origins = "http://localhost:5173")
public class EventController {

    @Autowired
    private EventService service;

    @Autowired
    private EventRepository eventRepository;

    
    @PostMapping("/create")
    public Event createEvent(@RequestBody Event event) {

      
        String organizerName = service.getOrganizerNameById(event.getOrganizerId());
        event.setOrganizerName(organizerName);

        return service.createEvent(event);
    }

  
    @GetMapping("/organizer/{id}")
    public List<Event> getOrganizerEvents(@PathVariable Long id) {
        return service.getOrganizerEvents(id);
    }

    
    @GetMapping("/approved")
    public List<Event> getApprovedEvents() {
        return service.getApprovedEvents();
    }

  
    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable Long id) {
        Event event = service.getEventById(id);
        if (event == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(event);
    }

    
    @PutMapping("/update/{id}")
    public ResponseEntity<Event> updateEvent(@PathVariable Long id, @RequestBody Event updatedEvent) {
        Event event = service.updateEvent(id, updatedEvent);
        if (event == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(event);
    }

    
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteEvent(@PathVariable Long id) {
        boolean deleted = service.deleteEvent(id);
        if (!deleted) {
            return ResponseEntity.badRequest().body("Event not found!");
        }
        return ResponseEntity.ok("Event deleted successfully ✅");
    }

   
    @GetMapping("/organizer/summary/{organizerId}")
    public List<Map<String, Object>> getOrganizerEventSummary(@PathVariable Long organizerId) {

        List<Object[]> rows = eventRepository.getOrganizerEventSummary(organizerId);

        List<Map<String, Object>> result = new ArrayList<>();

        for (Object[] r : rows) {
            Map<String, Object> map = new HashMap<>();
            map.put("eventId", r[0]);
            map.put("title", r[1]);
            map.put("date", r[2]);
            map.put("location", r[3]);
            map.put("capacity", r[4]);
            map.put("price", r[5]);
            map.put("ticketsSold", ((Long) r[6]));
            map.put("revenue", ((Double) r[7]));
            result.add(map);
        }

        return result;
    }
}
