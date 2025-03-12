package com.cyfrifpro.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cyfrifpro.model.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

	List<Message> findByTimestampBefore(LocalDateTime timestamp);

	// Get conversation between two users (both directions)
	List<Message> findBySender_UserIdAndRecipient_UserId(Long senderId, Long recipientId);

	List<Message> findBySender_UserIdOrRecipient_UserId(Long senderId, Long recipientId);
}
