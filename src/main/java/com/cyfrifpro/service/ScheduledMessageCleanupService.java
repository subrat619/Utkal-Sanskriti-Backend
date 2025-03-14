package com.cyfrifpro.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.cyfrifpro.model.Message;
import com.cyfrifpro.model.User;
import com.cyfrifpro.repository.MessageRepository;
import com.cyfrifpro.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class ScheduledMessageCleanupService {

	private static final Logger logger = LoggerFactory.getLogger(ScheduledMessageCleanupService.class);

	private final MessageRepository messageRepository;
	private final UserRepository userRepository;
	private final EmailService emailService;

	public ScheduledMessageCleanupService(MessageRepository messageRepository, UserRepository userRepository,
			EmailService emailService) {
		this.messageRepository = messageRepository;
		this.userRepository = userRepository;
		this.emailService = emailService;
	}

	// Scheduled to run daily at 2 AM
	@Scheduled(cron = "0 0 2 * * *")
	// Scheduled to run after 2 minutes
//	@Scheduled(cron = "0 */2 * * * *")
	@Transactional
	public void processOldMessages() {

		LocalDateTime cutoff = LocalDateTime.now().minusMonths(1);
//		LocalDateTime cutoff = LocalDateTime.now().minusMinutes(3);

		logger.info("Starting scheduled task to process messages older than {}", cutoff);

		List<Message> oldMessages = messageRepository.findByTimestampBefore(cutoff);

		if (oldMessages.isEmpty()) {
			logger.info("No messages older than cutoff found.");
			return;
		}

		// Group messages by conversation, using a key that ignores order.
		Map<String, List<Message>> conversationGroups = oldMessages.stream().collect(Collectors.groupingBy(message -> {
			Long senderId = message.getSender().getUserId();
			Long recipientId = message.getRecipient().getUserId();
			List<Long> pair = new ArrayList<>(Arrays.asList(senderId, recipientId));
			Collections.sort(pair);
			return pair.get(0) + "_" + pair.get(1);
		}));

		for (Map.Entry<String, List<Message>> entry : conversationGroups.entrySet()) {
			List<Message> conversation = entry.getValue();
			// Sort messages by timestamp
			conversation.sort(Comparator.comparing(Message::getTimestamp));
			StringBuilder emailBody = new StringBuilder("Chat History:\n");
			for (Message msg : conversation) {
				// Optionally, format the timestamp and sender's name.
				emailBody.append(msg.getTimestamp()).append(" - ").append(msg.getSender().getFirstName()).append(": ")
						.append(msg.getContent()).append("\n");
			}
			// Extract user ids from conversation key (they are sorted)
			String[] ids = entry.getKey().split("_");
			Long userId1 = Long.valueOf(ids[0]);
			Long userId2 = Long.valueOf(ids[1]);

			Optional<User> user1Opt = userRepository.findById(userId1);
			Optional<User> user2Opt = userRepository.findById(userId2);
			if (user1Opt.isPresent() && user2Opt.isPresent()) {
				String email1 = user1Opt.get().getEmail();
				String email2 = user2Opt.get().getEmail();
				String subject = "Monthly Chat History";
//				String subject = "2 Minute Chat History";

				// Send email to both participants
				emailService.sendEmail(email1, subject, emailBody.toString());
				emailService.sendEmail(email2, subject, emailBody.toString());
				logger.info("Sent monthly chat history email to {} and {}", email1, email2);
			}
			// Delete these messages from the database
			messageRepository.deleteAll(conversation);
			logger.info("Deleted {} messages for conversation key: {}", conversation.size(), entry.getKey());
		}
	}
}
