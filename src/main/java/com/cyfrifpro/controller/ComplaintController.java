package com.cyfrifpro.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cyfrifpro.DTO.ComplaintDTO;
import com.cyfrifpro.service.ComplaintService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/complaints")
//@CrossOrigin(origins = "http://localhost:3000")
public class ComplaintController {

	private final ComplaintService complaintService;

	public ComplaintController(ComplaintService complaintService) {
		this.complaintService = complaintService;
	}

	// Endpoint for user to post a complaint (or suggestion)
	@PostMapping("/post")
	public ResponseEntity<ComplaintDTO> postComplaint(@Valid @RequestBody ComplaintDTO complaintDTO) {
		ComplaintDTO savedComplaint = complaintService.postComplaint(complaintDTO);
		return ResponseEntity.ok(savedComplaint);
	}

	// Endpoint for admin to fetch all complaints
	@GetMapping("/all")
	public ResponseEntity<List<ComplaintDTO>> getAllComplaints() {
		List<ComplaintDTO> complaints = complaintService.getAllComplaints();
		return ResponseEntity.ok(complaints);
	}

	@GetMapping("/by_user/{userId}")
	public ResponseEntity<List<ComplaintDTO>> getComplaintsByUserId(@PathVariable Long userId) {
		List<ComplaintDTO> complaints = complaintService.getComplaintsByUserId(userId);
		return ResponseEntity.ok(complaints);
	}
}
