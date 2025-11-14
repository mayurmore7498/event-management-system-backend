package com.example.eventmanagement.controller;

import com.example.eventmanagement.model.Ticket;
import com.example.eventmanagement.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/tickets")
@CrossOrigin(origins = "http://localhost:5173")
public class TicketController {

    @Autowired
    private TicketService service;

    @PostMapping("/book")
    public Ticket bookTicket(@RequestBody Ticket ticket) {
        return service.bookTicket(ticket);
    }

    @GetMapping("/{id}")
    public Ticket getTicket(@PathVariable Long id) {
        return service.getTicketById(id);
    }

    @PutMapping("/pay/{id}")
    public Ticket markPaid(@PathVariable Long id) {
        return service.markAsPaid(id);
    }

    @GetMapping("/event/{eventId}")
    public List<Ticket> getEventTickets(@PathVariable Long eventId) {
        return service.getTicketsByEvent(eventId);
    }
}
