package com.cyfrifpro.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cyfrifpro.DTO.FeedbackDTO;
import com.cyfrifpro.service.FeedbackService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/feedback")
@Validated
//@CrossOrigin(origins="http://localhost:3000")
public class FeedbackController {

	private final FeedbackService feedbackService;

	public FeedbackController(FeedbackService feedbackService) {
		this.feedbackService = feedbackService;
	}

	// Create Feedback
	@Operation(summary = "Method to give feedback on a booking")
	@PostMapping
	public ResponseEntity<FeedbackDTO> createFeedback(@Valid @RequestBody FeedbackDTO feedbackDTO) {
		FeedbackDTO created = feedbackService.createFeedback(feedbackDTO);
		return new ResponseEntity<>(created, HttpStatus.CREATED);
	}

	// Update Feedback
	@Operation(summary = "Method to update feedback on a booking")
	@PutMapping("/{feedbackId}")
	public ResponseEntity<FeedbackDTO> updateFeedback(@PathVariable Long feedbackId,
			@Valid @RequestBody FeedbackDTO feedbackDTO) {
		FeedbackDTO updated = feedbackService.updateFeedback(feedbackId, feedbackDTO);
		return ResponseEntity.ok(updated);
	}

	// Get Feedback for a Booking
	@Operation(summary = "Method to get feedback on a booking by booking id")
	@GetMapping("/booking/{bookingId}")
	public ResponseEntity<List<FeedbackDTO>> getFeedbackByBooking(@PathVariable Long bookingId) {
		List<FeedbackDTO> feedbacks = feedbackService.getFeedbackByBookingId(bookingId);
		return ResponseEntity.ok(feedbacks);
	}

	// Get All Feedback
	@Operation(summary = "Method to get feedbacks on all bookings")
	@GetMapping
	public ResponseEntity<List<FeedbackDTO>> getAllFeedback() {
		List<FeedbackDTO> feedbacks = feedbackService.getAllFeedback();
		return ResponseEntity.ok(feedbacks);
	}
}
