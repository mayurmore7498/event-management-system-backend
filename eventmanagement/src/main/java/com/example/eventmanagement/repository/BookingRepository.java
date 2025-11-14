package com.example.eventmanagement.repository;

import com.example.eventmanagement.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByEventId(Long eventId);

    List<Booking> findByUserId(Long userId);
   

   
    List<Booking> findByEventOrganizerId(Long organizerId);

    
    @Query("SELECT b FROM Booking b WHERE b.event.organizerId = :organizerId")
    List<Booking> findByOrganizerId(@Param("organizerId") Long organizerId);

   
    @Transactional
    @Modifying
    @Query("DELETE FROM Booking b WHERE b.event.id = :eventId")
    void deleteByEventId(@Param("eventId") Long eventId);
}
