package com.cyfrifpro.DTO;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventDTO {
	private Long eventId;
	private String eventName;
	private LocalDateTime startDate;
	private LocalDateTime endDate;
	private String description;
	private String location;
	private LocalDateTime createdAt;

	// Field to hold Base64-encoded event image data
	private String eventImageData;
}
