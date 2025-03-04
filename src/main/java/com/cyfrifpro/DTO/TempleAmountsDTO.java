package com.cyfrifpro.DTO;

import java.math.BigDecimal;
import com.cyfrifpro.model.TempleDetails;
import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TempleAmountsDTO {
	private Long id;

	private Long templeId;

	@DecimalMin(value = "0.0", inclusive = true, message = "Pooja fee must be non-negative")
	private BigDecimal poojaFee;

	@DecimalMin(value = "0.0", inclusive = true, message = "Rudrabhisekh fee must be non-negative")
	private BigDecimal rudrabhisekhFee;

	@DecimalMin(value = "0.0", inclusive = true, message = "Prasad fee must be non-negative")
	private BigDecimal prasadFee;

	private TempleDetails temple;
}
