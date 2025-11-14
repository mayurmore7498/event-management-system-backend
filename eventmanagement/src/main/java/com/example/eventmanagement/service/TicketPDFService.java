package com.example.eventmanagement.service;

import com.example.eventmanagement.model.Booking;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.Base64;

@Service
public class TicketPDFService {

    public byte[] generateTicketPDF(Booking booking) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document();

        PdfWriter.getInstance(document, out);
        document.open();

        document.add(new Paragraph("🎟 EVENT TICKET", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18)));
        document.add(new Paragraph(" "));

        document.add(new Paragraph("Event: " + booking.getEvent().getTitle()));
        document.add(new Paragraph("Date: " + booking.getEvent().getDate()));
        document.add(new Paragraph("Venue: " + booking.getEvent().getLocation()));
        document.add(new Paragraph("Seats: " + booking.getSeats()));
        document.add(new Paragraph("Amount Paid: ₹" + booking.getAmount()));
        document.add(new Paragraph(" "));

        
        if (booking.getQrCode() != null) {
            byte[] qrBytes = Base64.getDecoder().decode(booking.getQrCode());
            Image qrImage = Image.getInstance(qrBytes);
            qrImage.scaleAbsolute(150, 150);
            document.add(qrImage);
        }

        document.close();
        return out.toByteArray();
    }
}
