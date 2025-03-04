package com.cyfrifpro.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cyfrifpro.DTO.MembershipDTO;
import com.cyfrifpro.DTO.MembershipPaymentResponse;
import com.cyfrifpro.service.MembershipService;
import com.cyfrifpro.service.PaymentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/memberships")
@Validated
@CrossOrigin(origins = "http://localhost:3000")
//@CrossOrigin(origins = "http://127.0.0.1:5500")
public class MembershipController {

	private final MembershipService membershipService;
	private final PaymentService paymentService;

	public MembershipController(MembershipService membershipService, PaymentService paymentService) {
		this.membershipService = membershipService;
		this.paymentService = paymentService;
	}

	@PostMapping
	public ResponseEntity<MembershipPaymentResponse> subscribeMembership(
			@Valid @RequestBody MembershipDTO membershipDTO) {
		// First, subscribe the membership (could be marked as pending payment)
		MembershipDTO subscribed = membershipService.subscribeMembership(membershipDTO);

		// Create a Razorpay order using the fee from membershipDTO
		// Generate a unique receipt; you could use the membership id (once created) or
		// a UUID.
		String receipt = "MEM_" + subscribed.getMembershipId();
		String orderData = paymentService.createMembershipOrder(subscribed.getFee(), receipt);

		// Build the composite response
		MembershipPaymentResponse response = new MembershipPaymentResponse();
		response.setMembership(subscribed);
		response.setRazorpayOrderData(orderData);

		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	// Endpoint for retrieving membership details by client ID
	@GetMapping("/{clientId}")
	public ResponseEntity<MembershipDTO> getMembershipByClientId(@PathVariable Long clientId) {
		MembershipDTO membership = membershipService.getMembershipByClientId(clientId);
		return ResponseEntity.ok(membership);
	}

	// New endpoint to fetch all active memberships
	@GetMapping("/active")
	public ResponseEntity<List<MembershipDTO>> getActiveMemberships() {
		List<MembershipDTO> activeMemberships = membershipService.getActiveMemberships();
		return ResponseEntity.ok(activeMemberships);
	}
}
