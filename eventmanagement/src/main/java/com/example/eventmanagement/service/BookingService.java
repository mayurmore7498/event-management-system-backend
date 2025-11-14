package com.example.eventmanagement.service;

import com.example.eventmanagement.model.Booking;
import com.example.eventmanagement.model.Event;
import com.example.eventmanagement.repository.BookingRepository;
import com.example.eventmanagement.repository.EventRepository;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.google.zxing.*;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.client.j2se.MatrixToImageWriter;

import java.io.ByteArrayOutputStream;
import java.util.*;
import java.util.Base64;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EmailService emailService;

    
    private static final String RAZORPAY_KEY = "rzp_test_RZkjudPIMVA8G7";
    private static final String RAZORPAY_SECRET = "825Pv2lbTpFW5Ycao45lkdPm";

   
    public Booking reserveSeats(Long eventId, Long userId, String userName, String userEmail,
                                List<String> seats, Double amount) throws Exception {

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        
        List<Booking> existing = bookingRepository.findByEventId(eventId);
        Set<String> bookedSeats = new HashSet<>();
        for (Booking b : existing) bookedSeats.addAll(b.getSeats());

        for (String s : seats)
            if (bookedSeats.contains(s))
                throw new RuntimeException("Seat " + s + " already booked!");

        
        RazorpayClient client = new RazorpayClient(RAZORPAY_KEY, RAZORPAY_SECRET);
        JSONObject orderReq = new JSONObject();
        orderReq.put("amount", amount.intValue() * 100);
        orderReq.put("currency", "INR");
        orderReq.put("receipt", "order_rcpt_" + System.currentTimeMillis());
        Order order = client.orders.create(orderReq);

       
        Booking booking = new Booking();
        booking.setEvent(event);
        booking.setUserId(userId);
        booking.setUserName(userName);
        booking.setUserEmail(userEmail); 
        booking.setSeats(seats);
        booking.setAmount(amount);
        booking.setRazorpayOrderId(order.get("id"));
        booking.setStatus(Booking.Status.PENDING);

        return bookingRepository.save(booking);
    }

   
    public Booking confirmPayment(Long bookingId, String paymentId, String orderId, String signature) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        booking.setPaymentId(paymentId);
        booking.setRazorpayOrderId(orderId);
        booking.setSignature(signature);
        booking.setStatus(Booking.Status.CONFIRMED);

       
        try {
            String qrData = "Event Ticket\n\n" +
                    "Booking ID: " + booking.getId() + "\n" +
                    "User: " + booking.getUserName() + "\n" +
                    "Event: " + booking.getEvent().getTitle() + "\n" +
                    "Location: " + booking.getEvent().getLocation() + "\n" +
                    "Seats: " + booking.getSeats() + "\n" +
                    "Amount: ₹" + booking.getAmount() + "\n" +
                    "Status: CONFIRMED ";
            booking.setQrCode(generateQRCodeBase64(qrData));
        } catch (Exception e) {
            e.printStackTrace();
        }

        bookingRepository.save(booking);

        
        try {
            emailService.sendTicketEmailWithAttachment(booking);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return booking;
    }

    
    public Booking cancelBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (booking.getStatus() != Booking.Status.CONFIRMED) {
            throw new RuntimeException("Only confirmed tickets can be cancelled");
        }

        booking.setStatus(Booking.Status.CANCELLED);
        bookingRepository.save(booking);

        try {
            emailService.sendCancellationEmail(booking);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return booking;
    }

   
    private String generateQRCodeBase64(String text) throws WriterException {
        QRCodeWriter qrWriter = new QRCodeWriter();
        BitMatrix matrix = qrWriter.encode(text, BarcodeFormat.QR_CODE, 200, 200);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            MatrixToImageWriter.writeToStream(matrix, "PNG", output);
        } catch (Exception e) {
            throw new RuntimeException("QR generation failed: " + e.getMessage());
        }
        return Base64.getEncoder().encodeToString(output.toByteArray());
    }

   
    public Optional<Booking> getBooking(Long id) { return bookingRepository.findById(id); }
    public List<Booking> getBookingsByEvent(Long eventId) { return bookingRepository.findByEventId(eventId); }
    public List<Booking> getUserBookings(Long userId) { return bookingRepository.findByUserId(userId); }

    public List<Booking> getBookingsByOrganizer(Long organizerId) {
        List<Event> events = eventRepository.findByOrganizerId(organizerId);
        List<Booking> result = new ArrayList<>();
        for (Event e : events) result.addAll(bookingRepository.findByEventId(e.getId()));
        return result;
    }
}
