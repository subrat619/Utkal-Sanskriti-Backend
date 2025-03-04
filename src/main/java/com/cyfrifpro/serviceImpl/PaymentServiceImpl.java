package com.cyfrifpro.serviceImpl;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.cyfrifpro.service.PaymentService;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;

import jakarta.annotation.PostConstruct;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Value("${razorpay.keyId}")
    private String keyId;

    @Value("${razorpay.keySecret}")
    private String keySecret;

    private RazorpayClient razorpayClient;

    @PostConstruct
    public void init() throws Exception {
        razorpayClient = new RazorpayClient(keyId, keySecret);
    }

    @Override
    public String createMembershipOrder(double amount, String receipt) {
        try {
            JSONObject orderRequest = new JSONObject();
            // Convert amount to paise (assuming fee is in INR)
            orderRequest.put("amount", (int) (amount * 100));
            orderRequest.put("currency", "INR");
            orderRequest.put("receipt", receipt);
            orderRequest.put("payment_capture", 1);
            Order order = razorpayClient.Orders.create(orderRequest);
            // Return the order details as JSON string (you can adjust as needed)
            return order.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error while creating Razorpay order", e);
        }
    }
}
