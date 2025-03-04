package com.cyfrifpro.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactUsDTO {

	@NotBlank(message = "Full Name is required.")
	@Size(min = 3, max = 50, message = "Full Name must be between 3 and 50 characters.")
	private String fullName;

	@NotBlank(message = "Email is required.")
	@Email(message = "Invalid email format.")
	private String email;

	@NotBlank(message = "Phone is required.")
	@Pattern(regexp = "^\\d{10}$", message = "Phone number must be 10 digits.")
	private String phone;

	@NotBlank(message = "Message is required.")
	@Size(max = 500, message = "Message cannot exceed 500 characters.")
	private String msg;
}
