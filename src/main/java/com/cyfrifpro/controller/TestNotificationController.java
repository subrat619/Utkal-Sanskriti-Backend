package com.cyfrifpro.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cyfrifpro.DTO.NotificationMessage;
import com.cyfrifpro.service.NotificationService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/test-notification")
//@CrossOrigin(origins="http://localhost:3000")
public class TestNotificationController {

	private final NotificationService notificationService;

	public TestNotificationController(NotificationService notificationService) {
		this.notificationService = notificationService;
	}

	@Operation(summary = "I dont know what is the work of this method.... 😒😒😒")
	@PostMapping
	public ResponseEntity<String> sendTestNotification() {
		NotificationMessage message = new NotificationMessage("Test Notification",
				"This is a test notification from Utkal Sanskriti.");
		notificationService.notifySupportAndTeamLeader(message);
		return ResponseEntity.ok("Notification sent");
	}
}
