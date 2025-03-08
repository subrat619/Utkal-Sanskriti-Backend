package com.cyfrifpro.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cyfrifpro.model.TransportHelpMessage;

@Repository
public interface TransportHelpMessageRepository extends JpaRepository<TransportHelpMessage, Long> {
	// Optional: Find messages by user if needed
	List<TransportHelpMessage> findByUser_UserId(Long userId);
}
