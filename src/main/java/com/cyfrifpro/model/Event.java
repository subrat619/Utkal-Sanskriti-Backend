package com.cyfrifpro.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "events")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Event {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long eventId;

	@NotBlank(message = "Event name is mandatory")
	@Column(nullable = false)
	private String eventName;

	// Event start date (when the event begins)
	@Column(nullable = false)
	private LocalDateTime startDate;

	// Event end date (when the event ends)
	@Column(nullable = false)
	private LocalDateTime endDate;

	@Column(length = 1000)
	private String description;

	@NotBlank(message = "Location is mandatory")
	@Column(nullable = false)
	private String location;

	// New field: store event image data as binary
	@Lob
	@Column(name = "event_image_data", columnDefinition = "LONGBLOB")
	private byte[] eventImageData;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "posted_by_id")
	private User postedBy; // New field

	@Column(name = "created_at", updatable = false)
	private LocalDateTime createdAt;

	@PrePersist
	protected void onCreate() {
		this.createdAt = LocalDateTime.now();
	}
}
