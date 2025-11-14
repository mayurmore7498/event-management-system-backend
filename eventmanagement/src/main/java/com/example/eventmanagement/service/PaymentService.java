package com.example.eventmanagement.service;

import com.example.eventmanagement.model.Payment;
import com.example.eventmanagement.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository repo;

    public Payment savePayment(Payment payment) {
        return repo.save(payment);
    }
}
