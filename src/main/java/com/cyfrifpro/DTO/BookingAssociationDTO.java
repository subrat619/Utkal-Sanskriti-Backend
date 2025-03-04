package com.cyfrifpro.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingAssociationDTO {
	private Long bookingId;
	private UserDTO assignedGuide;
	private UserDTO supportService;
	private UserDTO templeAdmin;
}
