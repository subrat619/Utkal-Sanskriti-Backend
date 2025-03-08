package com.cyfrifpro.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserContactDTO {
	private String fullName;
	private String email;
	private String contact;
}
