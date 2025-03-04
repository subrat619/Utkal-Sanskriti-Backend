package com.cyfrifpro.DTO;

import java.time.LocalDateTime;

import com.cyfrifpro.model.Role;
import com.cyfrifpro.model.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

	private long userId;

	private String firstName;

	private String lastName;

	private Role role;

	private String email;

	private String contactNumber;

	private String password;
	
	private String status;

	private LocalDateTime createdDate;

	private User createdBy;

	private LocalDateTime createdAt;

	private LocalDateTime deletedAt;

}
