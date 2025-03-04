package com.cyfrifpro.service;

public interface PaymentService {
	// Create a Razorpay order for membership payment
	String createMembershipOrder(double amount, String receipt);
}
