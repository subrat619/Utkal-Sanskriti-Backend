package com.cyfrifpro.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cyfrifpro.model.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    // Optionally, fetch messages between two users (e.g., a conversation)
    List<Message> findBySender_UserIdAndRecipient_UserId(Long senderId, Long recipientId);
    
    // Or fetch all messages involving a user (as sender or recipient)
    List<Message> findBySender_UserIdOrRecipient_UserId(Long senderId, Long recipientId);
    
    List<Message> findByTimestampBefore(LocalDateTime timestamp);
}
