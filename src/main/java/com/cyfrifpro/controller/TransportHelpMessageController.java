package com.cyfrifpro.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cyfrifpro.DTO.TransportHelpMessageDTO;
import com.cyfrifpro.service.TransportHelpMessageService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/transport_help")
//@CrossOrigin(origins = "http://localhost:3000")
public class TransportHelpMessageController {

	private final TransportHelpMessageService helpMessageService;

	public TransportHelpMessageController(TransportHelpMessageService helpMessageService) {
		this.helpMessageService = helpMessageService;
	}

	// Endpoint for a user to post a help message
	@PostMapping("/post")
	public ResponseEntity<TransportHelpMessageDTO> postHelpMessage(
			@Valid @RequestBody TransportHelpMessageDTO helpMessageDTO) {
		TransportHelpMessageDTO savedMessage = helpMessageService.postHelpMessage(helpMessageDTO);
		return ResponseEntity.ok(savedMessage);
	}

	// Endpoint for admin to fetch all help messages
	@GetMapping("/all")
	public ResponseEntity<List<TransportHelpMessageDTO>> getAllHelpMessages() {
		List<TransportHelpMessageDTO> messages = helpMessageService.getAllHelpMessages();
		return ResponseEntity.ok(messages);
	}

	// Optional: Endpoint to get help messages by user
	@GetMapping("/by_user/{userId}")
	public ResponseEntity<List<TransportHelpMessageDTO>> getHelpMessagesByUserId(@PathVariable Long userId) {
		List<TransportHelpMessageDTO> messages = helpMessageService.getHelpMessagesByUserId(userId);
		return ResponseEntity.ok(messages);
	}
}
