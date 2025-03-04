package com.cyfrifpro.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmergencyAlertDTO {

	@NotBlank(message = "Description is required")
	private String description;

	private Long bookingId;

	private String currentLocation;
	
	private boolean sendEmailToGuide;

}
