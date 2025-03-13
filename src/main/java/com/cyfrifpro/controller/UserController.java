package com.cyfrifpro.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cyfrifpro.DTO.UserContactDTO;
import com.cyfrifpro.DTO.UserDTO;
import com.cyfrifpro.DTO.UserHierarchyCountDTO;
import com.cyfrifpro.DTO.UserProfileUpdateDTO;
import com.cyfrifpro.DTO.UserSummaryDTO;
import com.cyfrifpro.model.Role;
import com.cyfrifpro.model.User;
import com.cyfrifpro.service.UserService;
import com.cyfrifpro.service.UserService2;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
//@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	private final UserService userService;
	private final UserService2 userService2;

	public UserController(UserService userService, UserService2 userService2) {
		this.userService = userService;
		this.userService2 = userService2;
	}

	@Operation(summary = "Method to fetch the first chield role by parent id")
	@GetMapping("/children/{creatorId}")
	public ResponseEntity<List<User>> getChildUsers(@PathVariable Long creatorId) {
		List<User> childUsers = userService.getChildUsersByCreatorId(creatorId);
		return ResponseEntity.ok(childUsers);
	}

	@Operation(summary = "Method by which update the user profile by user id")
	@PutMapping("/profile/{userId}")
	public ResponseEntity<UserDTO> updateUserProfile(@PathVariable Long userId,
			@Valid @RequestBody UserProfileUpdateDTO dto) {
		logger.info("Received profile update request for userId: {}", userId);
		UserDTO updatedUser = userService2.updateUserProfile(userId, dto);
		return ResponseEntity.ok(updatedUser);
	}

	@Operation(summary = "Method to fetch all the chield roles by parent id")
	@GetMapping("/{creatorId}/descendants")
	public ResponseEntity<List<User>> getAllDescendantUsers(@PathVariable Long creatorId) {
		List<User> descendants = userService2.getAllDescendantUsers(creatorId);
		return ResponseEntity.ok(descendants);
	}

	@Operation(summary = "Method to fetch all clients")
	@GetMapping("/clients")
	public ResponseEntity<List<User>> getRegisteredClients() {
		List<User> clients = userService2.getRegisteredClients();
		return ResponseEntity.ok(clients);
	}

	@Operation(summary = "Method to fetch MID_LEVEL users by TOP_LEVEL id which he created")
	@GetMapping("/midlevel/{topLevelId}")
	public ResponseEntity<List<User>> getMidLevelUsers(@PathVariable Long topLevelId) {
		List<User> midLevelUsers = userService2.getMidLevelUsersByTopLevelId(topLevelId);
		return ResponseEntity.ok(midLevelUsers);
	}

	@Operation(summary = "Method to fetch TEAM_LEADER users by MID_LEVEL id which he created")
	@GetMapping("/teamleader/{midLevelId}")
	public ResponseEntity<List<User>> getTeamLeadersByMidLevelId(@PathVariable Long midLevelId) {
		List<User> teamLeaders = userService2.getTeamLeadersByMidLevelId(midLevelId);
		return ResponseEntity.ok(teamLeaders);
	}

	@Operation(summary = "Method to fetch SUPPORT_SERVICE users by TEAM_LEADER id which he created")
	@GetMapping("/support/{teamleaderId}")
	public ResponseEntity<List<User>> getSupportServiceByTeamLeaderId(@PathVariable Long teamleaderId) {
		List<User> teamLeaders = userService2.getSupportServiceByTeamLeaderId(teamleaderId);
		return ResponseEntity.ok(teamLeaders);
	}

	@Operation(summary = "This method is not returning anything because TEAM_LEADER is not creating TEMPLE_ADMIN")
	@GetMapping("/templeAdmin/{teamleaderId}")
	public ResponseEntity<List<User>> getTempleAdminByTeamLeaderId(@PathVariable Long teamleaderId) {
		List<User> teamLeaders = userService2.getTempleAdminByTeamLeaderId(teamleaderId);
		return ResponseEntity.ok(teamLeaders);
	}

	@Operation(summary = "Method to fetch GUIDE users by TEMPLE_ADMIN id which he created")
	@GetMapping("/guide/{templeAdminId}")
	public ResponseEntity<List<User>> getGuidesByTempleAdminId(@PathVariable Long templeAdminId) {
		List<User> guids = userService2.getGuidesByTempleAdminId(templeAdminId);
		return ResponseEntity.ok(guids);
	}

	@Operation(summary = "Method to fetch users by status")
	@GetMapping("/status/{status}")
	public ResponseEntity<List<UserDTO>> getUsersByStatus(@PathVariable String status) {
		List<UserDTO> users = userService2.getUsersByStatus(status);
		return ResponseEntity.ok(users);
	}

	@Operation(summary = "Method to fetch all user datails( Like name, email and contact )")
	@GetMapping("/contacts")
	public ResponseEntity<List<UserContactDTO>> getUserContacts() {
		List<UserContactDTO> contacts = userService2.getUserContacts();
		return ResponseEntity.ok(contacts);
	}

	@Operation(summary = "Method to fetch user datails( Like name, email and contact ) for a perticular user by his id")
	@GetMapping("/summary/{userId}")
	public ResponseEntity<UserSummaryDTO> getUserSummary(@PathVariable Long userId) {
		UserSummaryDTO summary = userService2.getUserSummaryById(userId);
		return ResponseEntity.ok(summary);
	}

	@Operation(summary = "Method to count all the roles in number")
	@GetMapping("/counts_by_role")
	public ResponseEntity<Map<Role, Long>> getCountsByRole() {
		Map<Role, Long> counts = userService2.countUsersByRole();
		return ResponseEntity.ok(counts);
	}

	// Endpoint for direct children count
	@Operation(summary = "Method to count the just chield role in number")
	@GetMapping("/created_by/{creatorId}/direct_counts")
	public ResponseEntity<Map<Role, Long>> getDirectChildrenCounts(@PathVariable Long creatorId) {
		Map<Role, Long> counts = userService2.countDirectChildrenByCreatorId(creatorId);
		return ResponseEntity.ok(counts);
	}

	// Endpoint for full hierarchical count
	@Operation(summary = "Method to count and fetch role details all the chield roles by parent id in number")
	@GetMapping("/created_by/{creatorId}/hierarchy")
	public ResponseEntity<UserHierarchyCountDTO> getUserHierarchy(@PathVariable Long creatorId) {
		UserHierarchyCountDTO hierarchy = userService2.getUserHierarchy(creatorId);
		return ResponseEntity.ok(hierarchy);
	}

	// Endpoint to fetch overall count of users created by a given creator
	@Operation(summary = "Method to count all the chield roles by parent role id in number")
	@GetMapping("/created_by/{creatorId}/overall_counts")
	public ResponseEntity<Map<Role, Long>> getOverallDescendantCounts(@PathVariable Long creatorId) {
		Map<Role, Long> counts = userService2.countOverallDescendantsByCreatorId(creatorId);
		return ResponseEntity.ok(counts);
	}

	// Endpoint to fetch all descendant users of a creator, filtered by role.
	// For example: /api/users/created-by/5/descendants?role=MID_LEVEL
	@Operation(summary = "Method fetch all the chield roles by parent role id with their details")
	@GetMapping("/created_by/{creatorId}/descendants")
	public ResponseEntity<List<UserDTO>> getDescendantsByRole(@PathVariable Long creatorId, @RequestParam String role) {
		Role filterRole = Role.valueOf(role.toUpperCase());
		List<UserDTO> descendantUsers = userService2.getDescendantsByRole(creatorId, filterRole);
		return ResponseEntity.ok(descendantUsers);
	}

}
