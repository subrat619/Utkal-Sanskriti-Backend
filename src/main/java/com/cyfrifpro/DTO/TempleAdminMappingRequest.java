package com.cyfrifpro.DTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TempleAdminMappingRequest {
	@NotNull(message = "Temple admin data is required")
	@Valid
	private UserDTO templeAdminDTO;

	@NotNull(message = "Government user ID is required")
	private Long governmentId;

	@NotNull(message = "Temple ID is required")
	private Long templeId;
}
