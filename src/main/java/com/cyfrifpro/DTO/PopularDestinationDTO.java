package com.cyfrifpro.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PopularDestinationDTO {
	private Long id;
	private String name;
	private String location;
	private String imageUrl;
//	private Long bookingCount;
}
