package com.example.eventmanagement.controller;

import com.example.eventmanagement.model.Booking;
import com.example.eventmanagement.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins = "http://localhost:5173")
public class BookingController {

    @Autowired
    private BookingService bookingService;

   
    @GetMapping("/{id}")
    public Booking getBooking(@PathVariable Long id) {
        return bookingService.getBooking(id).orElse(null);
    }

    
    @GetMapping("/event/{eventId}")
    public List<Booking> getBookingsByEvent(@PathVariable Long eventId) {
        return bookingService.getBookingsByEvent(eventId);
    }

    
    @PostMapping("/reserve")
    public Booking reserveBooking(@RequestBody ReserveRequest request) throws Exception {
        return bookingService.reserveSeats(
                request.getEventId(),
                request.getUserId(),
                request.getUserName(),
                request.getUserEmail(),  
                request.getSeats(),
                request.getAmount()
        );
    }

   
    @PostMapping("/confirm")
    public Booking confirmPayment(@RequestBody ConfirmRequest request) throws Exception {
        return bookingService.confirmPayment(
                request.getBookingId(),
                request.getPaymentId(),
                request.getOrderId(),
                request.getSignature()
        );
    }

   
    @GetMapping("/user/{userId}")
    public List<Booking> getUserBookings(@PathVariable Long userId) {
        return bookingService.getUserBookings(userId);
    }

    
    @GetMapping("/organizer/{organizerId}")
    public List<Booking> getBookingsByOrganizer(@PathVariable Long organizerId) {
        return bookingService.getBookingsByOrganizer(organizerId);
    }

   
    @PutMapping("/cancel/{bookingId}")
    public ResponseEntity<String> cancelBooking(@PathVariable Long bookingId) {
        bookingService.cancelBooking(bookingId);
        return ResponseEntity.ok("Ticket Cancelled Successfully!");
    }

    
    public static class ReserveRequest {
        private Long eventId;
        private Long userId;
        private String userName;
        private String userEmail;
        private List<String> seats;
        private Double amount;

        public Long getEventId() { return eventId; }
        public void setEventId(Long eventId) { this.eventId = eventId; }

        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }

        public String getUserName() { return userName; }
        public void setUserName(String userName) { this.userName = userName; }

        public String getUserEmail() { return userEmail; }
        public void setUserEmail(String userEmail) { this.userEmail = userEmail; }

        public List<String> getSeats() { return seats; }
        public void setSeats(List<String> seats) { this.seats = seats; }

        public Double getAmount() { return amount; }
        public void setAmount(Double amount) { this.amount = amount; }
    }

   
    public static class ConfirmRequest {
        private Long bookingId;
        private String paymentId;
        private String orderId;
        private String signature;

        public Long getBookingId() { return bookingId; }
        public void setBookingId(Long bookingId) { this.bookingId = bookingId; }

        public String getPaymentId() { return paymentId; }
        public void setPaymentId(String paymentId) { this.paymentId = paymentId; }

        public String getOrderId() { return orderId; }
        public void setOrderId(String orderId) { this.orderId = orderId; }

        public String getSignature() { return signature; }
        public void setSignature(String signature) { this.signature = signature; }
    }
}
