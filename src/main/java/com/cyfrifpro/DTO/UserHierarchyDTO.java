package com.cyfrifpro.DTO;

import java.util.List;

import com.cyfrifpro.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserHierarchyDTO {
	private Long userId;
	private String firstName;
	private String lastName;
	private Role role;
	private List<UserHierarchyDTO> subordinates;
}
