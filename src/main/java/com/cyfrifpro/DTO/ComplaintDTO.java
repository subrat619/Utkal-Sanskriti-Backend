package com.cyfrifpro.DTO;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComplaintDTO {
	private Long complaintId;
	private Long userId; // ID of the user posting the complaint
	private String text;
	private LocalDateTime createdAt;
}
