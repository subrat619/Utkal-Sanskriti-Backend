package com.cyfrifpro.DTO;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransportHelpMessageDTO {
	private Long helpMessageId;
	private Long userId;
//	private String subject;
	private String message;
	private String status;
	private LocalDateTime createdAt;
}
