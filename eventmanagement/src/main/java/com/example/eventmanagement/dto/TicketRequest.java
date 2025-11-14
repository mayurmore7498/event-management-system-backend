package com.example.eventmanagement.dto;

public class TicketRequest {
    private Long eventId;
    private Long userId;
    private Integer seatNumber;
    private Integer quantity;
    private Double price;

    
    public TicketRequest() {
    }

    public TicketRequest(Long eventId, Long userId, Integer seatNumber, Integer quantity, Double price) {
        this.eventId = eventId;
        this.userId = userId;
        this.seatNumber = seatNumber;
        this.quantity = quantity;
        this.price = price;
    }

    
    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(Integer seatNumber) {
        this.seatNumber = seatNumber;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
