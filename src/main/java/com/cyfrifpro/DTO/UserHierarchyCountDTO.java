package com.cyfrifpro.DTO;

import java.util.List;
import java.util.Map;

import com.cyfrifpro.model.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserHierarchyCountDTO {
	private Long userId;
	private Role role;
	// Direct children count grouped by role
	private Map<Role, Long> directChildrenCount;
	// Recursively, a list of hierarchy DTOs for each direct child
	private List<UserHierarchyCountDTO> children;
}
