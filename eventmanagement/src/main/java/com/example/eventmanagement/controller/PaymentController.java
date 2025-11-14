package com.example.eventmanagement.controller;

import com.example.eventmanagement.model.Payment;
import com.example.eventmanagement.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "http://localhost:5173")
public class PaymentController {

    @Autowired
    private PaymentService service;

    @PostMapping("/save")
    public Payment savePayment(@RequestBody Payment payment) {
        return service.savePayment(payment);
    }
}
