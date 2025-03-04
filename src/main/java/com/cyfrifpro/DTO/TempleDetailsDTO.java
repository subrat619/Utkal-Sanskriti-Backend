package com.cyfrifpro.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TempleDetailsDTO {
	private Long id;

	@NotBlank(message = "Temple name is mandatory")
	private String name;

	@NotBlank(message = "Location is mandatory")
	private String location;

	private String description;

	@NotBlank(message = "District name is mandatory")
	private String districtName;

	private String imageUrl;

}
