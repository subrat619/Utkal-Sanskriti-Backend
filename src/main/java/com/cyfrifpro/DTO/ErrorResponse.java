package com.cyfrifpro.DTO;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
	private LocalDateTime timestamp;
	private String message;
	private String details;

	public ErrorResponse(String message, String details) {
		this.timestamp = LocalDateTime.now();
		this.message = message;
		this.details = details;
	}

}
