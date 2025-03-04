package com.cyfrifpro.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.cyfrifpro.DTO.NotificationMessage;

@Service
public class NotificationService {

	private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

	private final SimpMessagingTemplate messagingTemplate;

	public NotificationService(SimpMessagingTemplate messagingTemplate) {
		this.messagingTemplate = messagingTemplate;
	}

	public void notifySupportAndTeamLeader(NotificationMessage message) {
		// Send to a topic that clients subscribed to (e.g., support and team leader
		// could subscribe to /topic/notifications)
		messagingTemplate.convertAndSend("/topic/notifications", message);
		logger.info("Notification sent: {} - {}", message.getTitle(), message.getContent());
	}
}
