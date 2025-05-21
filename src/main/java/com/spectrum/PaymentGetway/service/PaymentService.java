package com.spectrum.PaymentGetway.service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.spectrum.PaymentGetway.model.Payment;
import com.spectrum.PaymentGetway.repository.PaymentRepository;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class PaymentService {

    @Value("${razorpay.api.key}")
    private String razorpayApiKey;

    @Value("${razorpay.api.secret}")
    private String razorpayApiSecret;

    private final com.spectrum.PaymentGetway.repository.PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public String createOrder(double amount) throws RazorpayException {
        RazorpayClient razorpayClient = new RazorpayClient(razorpayApiKey, razorpayApiSecret);

        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", (int) (amount * 100)); // Convert to paise (Indian currency)
        orderRequest.put("currency", "INR");
        orderRequest.put("receipt", "txn_123456");

        Order order = razorpayClient.orders.create(orderRequest);

        // Save order details in the database
        Payment payment = new Payment();
        payment.setRazorpayOrderId(order.get("id"));
        payment.setAmount(amount);
        payment.setStatus("CREATED");
        paymentRepository.save(payment);

        return order.toString();
    }

    public void updatePaymentStatus(String razorpayOrderId, String razorpayPaymentId) {
        Payment payment = paymentRepository.findByRazorpayOrderId(razorpayOrderId);
        if (payment != null) {
            payment.setStatus("PAID");
            payment.setPaymentId(razorpayPaymentId);
            payment.setPaymentDate(new Date());
            paymentRepository.save(payment);
        }
    }
}
