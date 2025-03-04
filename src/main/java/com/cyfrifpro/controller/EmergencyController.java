package com.cyfrifpro.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cyfrifpro.DTO.EmergencyAlertDTO;
import com.cyfrifpro.service.EmergencyService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/emergency")
@CrossOrigin(origins="http://localhost:3000")
@Validated
public class EmergencyController {

	private static final Logger logger = LoggerFactory.getLogger(EmergencyController.class);

	private final EmergencyService emergencyService;

	public EmergencyController(EmergencyService emergencyService) {
		this.emergencyService = emergencyService;
	}

	// Endpoint for clients to trigger an emergency alert
	@PostMapping("/alert")
	public ResponseEntity<String> triggerEmergency(@Valid @RequestBody EmergencyAlertDTO alertDTO,
			@RequestParam Long clientId) {

		logger.info("Received emergency alert from client: {}", clientId);
		emergencyService.processEmergencyAlert(clientId, alertDTO);
		return ResponseEntity.status(HttpStatus.OK).body("Emergency alert received. Support has been notified.");
	}
}
