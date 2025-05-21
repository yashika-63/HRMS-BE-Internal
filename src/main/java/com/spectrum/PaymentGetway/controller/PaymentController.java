package com.spectrum.PaymentGetway.controller;

import com.razorpay.RazorpayException;
import com.spectrum.PaymentGetway.service.PaymentService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/create-order")
    public ResponseEntity<String> createOrder(@RequestParam double amount) {
        try {
            String orderResponse = paymentService.createOrder(amount);
            return ResponseEntity.ok(orderResponse);
        } catch (RazorpayException e) {
            return ResponseEntity.status(500).body("Error creating order: " + e.getMessage());
        }
    }

    @PostMapping("/update-payment-status")
    public ResponseEntity<String> updatePaymentStatus(
            @RequestParam String razorpayOrderId,
            @RequestParam String razorpayPaymentId) {
        paymentService.updatePaymentStatus(razorpayOrderId, razorpayPaymentId);
        return ResponseEntity.ok("Payment status updated successfully.");
    }
}
