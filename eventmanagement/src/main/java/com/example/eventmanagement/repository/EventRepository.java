package com.example.eventmanagement.repository;

import com.example.eventmanagement.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findByStatus(Event.Status status);
    List<Event> findByOrganizerId(Long organizerId);
    List<Event> findByTitleContainingIgnoreCase(String keyword);

    @Query("""
    	    SELECT e.id, e.title, e.date, e.location, e.capacity, e.ticketPrice,
    	           COALESCE(SUM(size(b.seats)), 0) AS ticketsSold,
    	           COALESCE(SUM(b.amount), 0) AS revenue
    	    FROM Event e
    	    LEFT JOIN Booking b ON e.id = b.event.id AND b.status = 'CONFIRMED'
    	    WHERE e.organizerId = :organizerId
    	    GROUP BY e.id, e.title, e.date, e.location, e.capacity, e.ticketPrice
    	""")
    	List<Object[]> getOrganizerEventSummary(Long organizerId);



}
