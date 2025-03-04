package com.cyfrifpro.DTO;

import lombok.Data;

@Data
public class MembershipPaymentResponse {
	private MembershipDTO membership;
	private String razorpayOrderData; // This could be a JSON string or an object if you prefer
}
