package com.example.eventmanagement.service;

import com.example.eventmanagement.model.Ticket;
import com.example.eventmanagement.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TicketService {

    @Autowired
    private TicketRepository repo;

    public Ticket bookTicket(Ticket ticket) {
        boolean alreadyBooked = repo.existsByEventIdAndSeatNumber(ticket.getEventId(), ticket.getSeatNumber());
        if (alreadyBooked) throw new RuntimeException("Seat already booked!");
        return repo.save(ticket);
    }

    public Ticket getTicketById(Long id) {
        return repo.findById(id).orElse(null);
    }

    public List<Ticket> getTicketsByEvent(Long eventId) {
        return repo.findByEventId(eventId);
    }

    public Ticket markAsPaid(Long ticketId) {
        Ticket t = repo.findById(ticketId).orElseThrow(() -> new RuntimeException("Ticket not found"));
        t.setIsPaid(true);
        return repo.save(t);
    }
}
