package com.cyfrifpro.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.cyfrifpro.DTO.NotificationMessage;

@Controller
public class NotificationController {

	// This method receives notifications sent to "/app/notify" and broadcasts them
	// to "/topic/notifications"
	@MessageMapping("/notify")
	@SendTo("/topic/notifications")
	public NotificationMessage sendNotification(NotificationMessage message) throws Exception {
		// Optionally add logic (e.g., save notification, filter based on roles, etc.)
		return message;
	}
}
