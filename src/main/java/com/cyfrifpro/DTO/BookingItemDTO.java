package com.cyfrifpro.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingItemDTO {

	private Long bookingItemId;

	@NotNull(message = "Temple ID is required")
	private Long templeId;

	private boolean poojaSelected;
	private boolean rudrabhisekhSelected;
	private boolean prasadSelected;

}
