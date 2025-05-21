package com.spectrum.PaymentGetway.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spectrum.PaymentGetway.model.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Payment findByRazorpayOrderId(String razorpayOrderId);
}
