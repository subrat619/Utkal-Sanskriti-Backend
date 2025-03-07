package com.cyfrifpro.DTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TempleAdminRegistrationRequest {
	// The temple admin user data
	@NotNull
	@Valid
	private UserDTO templeAdminDTO;

	// The government user's ID who is creating this temple admin
	@NotNull(message = "Government user ID is required")
	private Long governmentId;

	// The temple details data for the temple admin
	@NotNull
	@Valid
	private TempleDetailsDTO templeDetailsDTO;
}
