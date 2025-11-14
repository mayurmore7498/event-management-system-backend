package com.example.eventmanagement.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "bookings")
public class Booking {

   
    public enum Status {
        PENDING,
        CONFIRMED,
        CANCELLED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Event event;

    private Long userId;
    private String userName;
    private String userEmail;

    @ElementCollection
    private List<String> seats;

    private Double amount;
    private String orderId;
    private String paymentId;
    private String signature;

    @Enumerated(EnumType.STRING)
    private Status status; 

    private String razorpayOrderId;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String qrCode; 

   

    public Long getId() { 
        return id; 
    }
    public void setId(Long id) { 
        this.id = id; 
    }

    public Event getEvent() { 
        return event; 
    }
    public void setEvent(Event event) { 
        this.event = event; 
    }

    public Long getUserId() { 
        return userId; 
    }
    public void setUserId(Long userId) { 
        this.userId = userId; 
    }

    public String getUserName() { 
        return userName; 
    }
    public void setUserName(String userName) { 
        this.userName = userName; 
    }

    public List<String> getSeats() { 
        return seats; 
    }
    public void setSeats(List<String> seats) { 
        this.seats = seats; 
    }

    public Double getAmount() { 
        return amount; 
    }
    public void setAmount(Double amount) { 
        this.amount = amount; 
    }

    public String getOrderId() { 
        return orderId; 
    }
    public void setOrderId(String orderId) { 
        this.orderId = orderId; 
    }

    public String getPaymentId() { 
        return paymentId; 
    }
    public void setPaymentId(String paymentId) { 
        this.paymentId = paymentId; 
    }

    public String getSignature() { 
        return signature; 
    }
    public void setSignature(String signature) { 
        this.signature = signature; 
    }

    public Status getStatus() { 
        return status; 
    }
    public void setStatus(Status status) { 
        this.status = status; 
    }

    public String getRazorpayOrderId() { 
        return razorpayOrderId; 
    }
    public void setRazorpayOrderId(String razorpayOrderId) { 
        this.razorpayOrderId = razorpayOrderId; 
    }

    public String getQrCode() { 
        return qrCode; 
    }
    public void setQrCode(String qrCode) { 
        this.qrCode = qrCode; 
    }
    
    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }


}
