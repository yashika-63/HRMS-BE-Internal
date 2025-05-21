package com.spectrum.PaymentGetway.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "payment")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "razorpay_order_id", nullable = false)
    private String razorpayOrderId;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private double amount;

    @Column(name = "payment_id", nullable = true)
    private String paymentId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "payment_date")
    private Date paymentDate;

    public Payment() {
        super();

    }

    public Payment(Long id, String razorpayOrderId, String status, double amount, String paymentId, Date paymentDate) {
        this.id = id;
        this.razorpayOrderId = razorpayOrderId;
        this.status = status;
        this.amount = amount;
        this.paymentId = paymentId;
        this.paymentDate = paymentDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRazorpayOrderId() {
        return razorpayOrderId;
    }

    public void setRazorpayOrderId(String razorpayOrderId) {
        this.razorpayOrderId = razorpayOrderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    @Override
    public String toString() {
        return "Payment [id=" + id + ", razorpayOrderId=" + razorpayOrderId + ", status=" + status + ", amount="
                + amount + ", paymentId=" + paymentId + ", paymentDate=" + paymentDate + "]";
    }

    // Getters and Setters

}
