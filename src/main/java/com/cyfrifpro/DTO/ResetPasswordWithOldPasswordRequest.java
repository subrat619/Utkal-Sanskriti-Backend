package com.cyfrifpro.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResetPasswordWithOldPasswordRequest {

	@NotBlank(message = "Email is required")
	@Email(message = "Invalid email format")
	private String email;

	@NotBlank(message = "Old password is required")
	private String oldPassword;

	@NotBlank(message = "OTP is required")
	private String otp;

	@NotBlank(message = "New password is required")
	@Size(min = 4, message = "New password must be at least 4 characters long")
	private String newPassword;

	@NotBlank(message = "Confirm new password is required")
	private String confirmNewPassword;

}
