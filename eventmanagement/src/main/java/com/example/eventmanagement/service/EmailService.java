package com.example.eventmanagement.service;

import com.example.eventmanagement.model.Booking;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.util.Base64;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

   
    public void sendTicketEmailWithAttachment(Booking booking)
            throws MessagingException, DocumentException {

        if (booking.getUserEmail() == null || booking.getUserEmail().isEmpty()) {
            System.err.println("⚠️ Email address not found for booking ID: " + booking.getId());
            return;
        }

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(booking.getUserEmail());
        helper.setSubject("🎟 Your Ticket Confirmation – " + booking.getEvent().getTitle());

       
        String htmlContent = """
                <div style="font-family: Arial, sans-serif; color: #333; line-height: 1.6;">
                    <h2 style="color:#2E86C1;">Hello %s,</h2>
                    <p>Thank you for booking with <b>EventEase</b> 🎉</p>
                    <p>Your ticket for <b>%s</b> has been <b>successfully confirmed</b>.</p>
                    <table style="margin-top:10px; border-collapse: collapse;">
                        <tr><td><b> Location:</b></td><td>%s</td></tr>
                        <tr><td><b> Date:</b></td><td>%s</td></tr>
                        <tr><td><b> Seats:</b></td><td>%s</td></tr>
                        <tr><td><b> Amount Paid:</b></td><td>₹%s</td></tr>
                    </table>
                    <p style="margin-top:15px;">Your ticket PDF and QR code are attached below.</p>
                    <p style="color: #5D6D7E;">We look forward to seeing you at the event!</p>
                    <hr style="margin-top:20px;">
                    <p style="font-size:13px;">Best regards,<br><b>EventEase Team</b><br>eventease-support@gmail.com</p>
                </div>
                """.formatted(
                booking.getUserName(),
                booking.getEvent().getTitle(),
                booking.getEvent().getLocation(),
                booking.getEvent().getDate(),
                booking.getSeats(),
                booking.getAmount()
        );

        helper.setText(htmlContent, true);

        
        ByteArrayOutputStream pdfOut = new ByteArrayOutputStream();
        Document doc = new Document();
        PdfWriter.getInstance(doc, pdfOut);
        doc.open();

        Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
        doc.add(new Paragraph("🎟 EVENT TICKET", titleFont));
        doc.add(new Paragraph(" "));
        doc.add(new Paragraph("Booking ID: " + booking.getId()));
        doc.add(new Paragraph("User: " + booking.getUserName()));
        doc.add(new Paragraph("Email: " + booking.getUserEmail()));
        doc.add(new Paragraph("Event: " + booking.getEvent().getTitle()));
        doc.add(new Paragraph("Location: " + booking.getEvent().getLocation()));
        doc.add(new Paragraph("Date: " + booking.getEvent().getDate()));
        doc.add(new Paragraph("Seats: " + booking.getSeats()));
        doc.add(new Paragraph("Amount Paid: ₹" + booking.getAmount()));
        doc.add(new Paragraph("Status: CONFIRMED ✅"));
        doc.close();

        
        helper.addAttachment("Ticket.pdf", new ByteArrayResource(pdfOut.toByteArray()));

        
        if (booking.getQrCode() != null) {
            byte[] qrBytes = Base64.getDecoder().decode(booking.getQrCode());
            helper.addAttachment("QR.png", new ByteArrayResource(qrBytes));
        }

        mailSender.send(message);
        System.out.println("✅ Ticket email sent successfully to " + booking.getUserEmail());
    }

    public void sendCancellationEmail(Booking booking) throws MessagingException {
        if (booking.getUserEmail() == null || booking.getUserEmail().isEmpty()) {
            System.err.println("⚠️ Email address not found for cancellation of booking ID: " + booking.getId());
            return;
        }

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(booking.getUserEmail());
        helper.setSubject("❌ Ticket Cancelled – " + booking.getEvent().getTitle());

        String htmlContent = """
                <div style="font-family: Arial, sans-serif; color: #333; line-height: 1.6;">
                    <h2 style="color:#CB4335;">Hello %s,</h2>
                    <p>Your booking for the event <b>%s</b> has been <b>cancelled successfully</b>.</p>
                    <p>If you made a payment, your refund will be processed as per our policy.</p>
                    <p>Refund Policy: 
                    <ul>
                        <li>Cancellation before 12 hours → 80%% refund.</li>
                        <li>Cancellation within 12 hours → No refund.</li>
                    </ul></p>
                    <hr style="margin-top:20px;">
                    <p style="font-size:13px;">Best regards,<br><b>EventEase Team</b><br>eventease-support@gmail.com</p>
                </div>
                """.formatted(
                booking.getUserName(),
                booking.getEvent().getTitle()
        );

        helper.setText(htmlContent, true);

        mailSender.send(message);
        System.out.println("📩 Cancellation email sent to " + booking.getUserEmail());
    }
}
