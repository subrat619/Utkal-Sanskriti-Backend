package com.cyfrifpro.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cyfrifpro.DTO.UserContactDTO;
import com.cyfrifpro.DTO.UserDTO;
import com.cyfrifpro.DTO.UserProfileUpdateDTO;
import com.cyfrifpro.DTO.UserSummaryDTO;
import com.cyfrifpro.model.User;
import com.cyfrifpro.service.UserService;
import com.cyfrifpro.service.UserService2;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	private final UserService userService;
	private final UserService2 userService2;

	public UserController(UserService userService, UserService2 userService2) {
		this.userService = userService;
		this.userService2 = userService2;
	}

	@GetMapping("/children/{creatorId}")
	public ResponseEntity<List<User>> getChildUsers(@PathVariable Long creatorId) {
		List<User> childUsers = userService.getChildUsersByCreatorId(creatorId);
		return ResponseEntity.ok(childUsers);
	}

	@PutMapping("/profile/{userId}")
	public ResponseEntity<UserDTO> updateUserProfile(@PathVariable Long userId,
			@Valid @RequestBody UserProfileUpdateDTO dto) {
		logger.info("Received profile update request for userId: {}", userId);
		UserDTO updatedUser = userService2.updateUserProfile(userId, dto);
		return ResponseEntity.ok(updatedUser);
	}

	@GetMapping("/{creatorId}/descendants")
	public ResponseEntity<List<User>> getAllDescendantUsers(@PathVariable Long creatorId) {
		List<User> descendants = userService2.getAllDescendantUsers(creatorId);
		return ResponseEntity.ok(descendants);
	}

	@GetMapping("/clients")
	public ResponseEntity<List<User>> getRegisteredClients() {
		List<User> clients = userService2.getRegisteredClients();
		return ResponseEntity.ok(clients);
	}

	@GetMapping("/midlevel/{topLevelId}")
	public ResponseEntity<List<User>> getMidLevelUsers(@PathVariable Long topLevelId) {
		List<User> midLevelUsers = userService2.getMidLevelUsersByTopLevelId(topLevelId);
		return ResponseEntity.ok(midLevelUsers);
	}

	@GetMapping("/teamleader/{midLevelId}")
	public ResponseEntity<List<User>> getTeamLeadersByMidLevelId(@PathVariable Long midLevelId) {
		List<User> teamLeaders = userService2.getTeamLeadersByMidLevelId(midLevelId);
		return ResponseEntity.ok(teamLeaders);
	}

	@GetMapping("/support/{teamleaderId}")
	public ResponseEntity<List<User>> getSupportServiceByTeamLeaderId(@PathVariable Long teamleaderId) {
		List<User> teamLeaders = userService2.getSupportServiceByTeamLeaderId(teamleaderId);
		return ResponseEntity.ok(teamLeaders);
	}

	@GetMapping("/templeAdmin/{teamleaderId}")
	public ResponseEntity<List<User>> getTempleAdminByTeamLeaderId(@PathVariable Long teamleaderId) {
		List<User> teamLeaders = userService2.getTempleAdminByTeamLeaderId(teamleaderId);
		return ResponseEntity.ok(teamLeaders);
	}

	@GetMapping("/status/{status}")
	public ResponseEntity<List<UserDTO>> getUsersByStatus(@PathVariable String status) {
		List<UserDTO> users = userService2.getUsersByStatus(status);
		return ResponseEntity.ok(users);
	}

	@GetMapping("/contacts")
	public ResponseEntity<List<UserContactDTO>> getUserContacts() {
		List<UserContactDTO> contacts = userService2.getUserContacts();
		return ResponseEntity.ok(contacts);
	}

	@GetMapping("/summary/{userId}")
	public ResponseEntity<UserSummaryDTO> getUserSummary(@PathVariable Long userId) {
		UserSummaryDTO summary = userService2.getUserSummaryById(userId);
		return ResponseEntity.ok(summary);
	}
}
