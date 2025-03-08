package com.cyfrifpro.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "transport_help_messages")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransportHelpMessage {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long helpMessageId;

	// The user who submitted the help message
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	// Subject of the help message
//	@Column(nullable = false, length = 255)
//	private String subject;

	// The help message text
	@Column(nullable = false, length = 2000)
	private String message;

	// Status of the help message (for example, OPEN, CLOSED)
	@Column(nullable = false, length = 50)
	private String status = "OPEN";

	@Column(name = "created_at", updatable = false)
	private LocalDateTime createdAt;

	@PrePersist
	protected void onCreate() {
		this.createdAt = LocalDateTime.now();
	}
}
