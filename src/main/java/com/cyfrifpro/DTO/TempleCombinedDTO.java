package com.cyfrifpro.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TempleCombinedDTO {
	private TempleDetailsDTO templeDetails;
	private TempleAmountsDTO templeAmounts;

}
