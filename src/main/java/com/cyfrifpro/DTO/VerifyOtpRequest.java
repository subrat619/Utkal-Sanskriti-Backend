package com.cyfrifpro.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VerifyOtpRequest {
	@NotBlank(message = "Email is required")
	private String email;

	@NotBlank(message = "OTP is required")
	private String otp;
}
