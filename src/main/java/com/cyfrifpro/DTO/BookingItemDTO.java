package com.cyfrifpro.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingItemDTO {
	private Long bookingItemId;

	private Long templeId;

	// Added field for full temple details.
	private TempleDetailsDTO temple;

	private boolean poojaSelected;
	private boolean rudrabhisekhSelected;
	private boolean prasadSelected;
}
