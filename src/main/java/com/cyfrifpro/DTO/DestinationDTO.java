package com.cyfrifpro.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DestinationDTO {
	private Long id;

	@NotBlank(message = "Destination name is mandatory")
	private String name;

	@NotBlank(message = "Location is mandatory")
	private String location;

	private String description;

	@NotBlank(message = "District name is mandatory")
	private String districtName;

	// Optional: imageUrl field if you plan to store a URL instead of binary data
	private String imageUrl;

	// This field will hold the Base64-encoded image data (for responses)
	private String imageData;
}
