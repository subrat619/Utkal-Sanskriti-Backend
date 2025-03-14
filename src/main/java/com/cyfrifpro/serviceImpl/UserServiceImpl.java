package com.cyfrifpro.serviceImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cyfrifpro.DTO.ChangePasswordRequest;
import com.cyfrifpro.DTO.UserContactDTO;
import com.cyfrifpro.DTO.UserDTO;
import com.cyfrifpro.DTO.UserHierarchyCountDTO;
import com.cyfrifpro.DTO.UserProfileUpdateDTO;
import com.cyfrifpro.DTO.UserSummaryDTO;
import com.cyfrifpro.Exception.ResourceNotFoundException;
import com.cyfrifpro.model.Role;
import com.cyfrifpro.model.User;
import com.cyfrifpro.repository.UserRepository;
import com.cyfrifpro.service.UserService2;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService2 {

	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	private final UserRepository userRepo;
	private final PasswordEncoder passwordEncoder;
	private final UserRepository userRepository;
	private final ModelMapper modelMapper;

	public UserServiceImpl(UserRepository userRepo, PasswordEncoder passwordEncoder, ModelMapper modelMapper,
			UserRepository userRepository) {
		this.userRepo = userRepo;
		this.passwordEncoder = passwordEncoder;
		this.modelMapper = modelMapper;
		this.userRepository = userRepository;
	}

	@Override
	public void changePassword(Long userId, ChangePasswordRequest request) {
		User user = userRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));

		// Check if the provided old password matches the user's current password
		if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
			logger.warn("Old password does not match for user with ID {}", userId);
			throw new IllegalArgumentException("Old password is incorrect.");
		}

		// Optionally, check if newPassword and confirmNewPassword match
		if (!request.getNewPassword().equals(request.getConfirmNewPassword())) {
			logger.warn("New password and confirm new password do not match for user with ID {}", userId);
			throw new IllegalArgumentException("New password and confirmation do not match.");
		}

		// Update the user's password
		user.setPassword(passwordEncoder.encode(request.getNewPassword()));
		userRepo.save(user);
		logger.info("Password successfully updated for user with ID {}", userId);
	}

	@Override
	public UserDTO updateUserProfile(Long userId, UserProfileUpdateDTO dto) {
		logger.info("Updating profile for userId: {}", userId);
		User user = userRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));

		user.setFirstName(dto.getFirstName());
		user.setLastName(dto.getLastName());
		user.setEmail(dto.getEmail());
		user.setContactNumber(dto.getContactNumber());

		User savedUser = userRepo.save(user);
		logger.info("User profile updated for userId: {}", savedUser.getUserId());

		return modelMapper.map(savedUser, UserDTO.class);
	}

	@Override
	public List<User> getAllDescendantUsers(Long creatorId) {
		List<User> directChildren = userRepository.findByCreatedBy_UserId(creatorId);
		List<User> allDescendants = new ArrayList<>(directChildren);
		for (User child : directChildren) {
			allDescendants.addAll(getAllDescendantUsers(child.getUserId()));
		}
		return allDescendants;
	}

	@Override
	public List<User> getRegisteredClients() {
		logger.info("Fetching all registered clients");
		return userRepository.findByRole(Role.CLIENT);
	}

	@Override
	public List<User> getMidLevelUsersByTopLevelId(Long topLevelId) {
		return userRepo.findByCreatedBy_UserIdAndRole(topLevelId, Role.MID_LEVEL);
	}

	@Override
	public List<User> getTeamLeadersByMidLevelId(Long midLevelId) {
		return userRepo.findByCreatedBy_UserIdAndRole(midLevelId, Role.TEAM_LEADER);
	}

	@Override
	public List<User> getSupportServiceByTeamLeaderId(Long teamLeaderId) {
		return userRepo.findByCreatedBy_UserIdAndRole(teamLeaderId, Role.SUPPORT_SERVICE);
	}

	@Override
	public List<User> getTempleAdminByTeamLeaderId(Long teamLeaderId) {
		return userRepo.findByCreatedBy_UserIdAndRole(teamLeaderId, Role.TEMPLE_ADMIN);
	}

	@Override
	public List<User> getGuidesByTempleAdminId(Long templeAdminId) {
		return userRepo.findByCreatedBy_UserIdAndRole(templeAdminId, Role.GUIDE);
	}

	public List<UserDTO> getUsersByStatus(String status) {
		logger.info("Fetching users with status: {}", status);
		List<User> users = userRepo.findByStatus(status);
		return users.stream().map(user -> modelMapper.map(user, UserDTO.class)).collect(Collectors.toList());
	}

	public List<UserContactDTO> getUserContacts() {
		List<User> users = userRepo.findAll();
		return users.stream().map(user -> {
			String fullName = user.getFirstName() + " " + user.getLastName();
			return new UserContactDTO(fullName, user.getEmail(), user.getContactNumber());
		}).collect(Collectors.toList());
	}

	public UserSummaryDTO getUserSummaryById(Long userId) {
		User user = userRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));
		String fullName = user.getFirstName() + " " + user.getLastName();
		return new UserSummaryDTO(fullName, user.getEmail(), user.getContactNumber());
	}

	@Override
	public Map<Role, Long> countUsersByRole() {
		Map<Role, Long> counts = new HashMap<>();
		for (Role role : Role.values()) {
			counts.put(role, userRepo.countByRole(role));
		}
		return counts;
	}

	@Override
	public Map<Role, Long> countDirectChildrenByCreatorId(Long creatorId) {
		List<Object[]> results = userRepository.countByCreatedByGroupByRole(creatorId);
		Map<Role, Long> counts = new HashMap<>();
		for (Object[] row : results) {
			Role role = (Role) row[0];
			Long count = (Long) row[1];
			counts.put(role, count);
		}
		return counts;
	}

	@Override
	public UserHierarchyCountDTO getUserHierarchy(Long userId) {
		// Fetch the user (creator)
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));

		// Get direct children counts (using the direct count method above)
		Map<Role, Long> directCounts = countDirectChildrenByCreatorId(userId);

		// Get direct children as entities
		List<User> directChildren = userRepository.findByCreatedBy_UserId(userId);

		// Recursively build the hierarchy for each child
		List<UserHierarchyCountDTO> childrenHierarchy = directChildren.stream()
				.map(child -> getUserHierarchy(child.getUserId())).collect(Collectors.toList());

		// Build and return the hierarchy DTO for this user
		UserHierarchyCountDTO hierarchy = new UserHierarchyCountDTO();
		hierarchy.setUserId(userId);
		hierarchy.setRole(user.getRole());
		hierarchy.setDirectChildrenCount(directCounts);
		hierarchy.setChildren(childrenHierarchy);

		return hierarchy;
	}

	@Override
	public Map<Role, Long> countOverallDescendantsByCreatorId(Long creatorId) {
		List<User> allDescendants = getAllDescendants(creatorId);
		// Group the descendants by role and count
		return allDescendants.stream().collect(Collectors.groupingBy(User::getRole, Collectors.counting()));
	}

	/**
	 * Recursively fetches all users whose 'createdBy' equals the given creatorId.
	 *
	 * @param creatorId the creator's user ID
	 * @return a list of all descendant users
	 */
//	private List<User> getAllDescendants(Long creatorId) {
//		List<User> directChildren = userRepo.findByCreatedBy_UserId(creatorId);
//		List<User> allDescendants = new ArrayList<>(directChildren);
//		for (User child : directChildren) {
//			allDescendants.addAll(getAllDescendants(child.getUserId()));
//		}
//		return allDescendants;
//	}
	
	/**
     * Recursively retrieves all descendant users created by the given creatorId.
     */
    private List<User> getAllDescendants(Long creatorId) {
        List<User> directChildren = userRepo.findByCreatedBy_UserId(creatorId);
        List<User> allDescendants = new ArrayList<>(directChildren);
        for (User child : directChildren) {
            allDescendants.addAll(getAllDescendants(child.getUserId()));
        }
        return allDescendants;
    }

    @Override
    public List<UserDTO> getDescendantsByRole(Long creatorId, Role role) {
        // Fetch all descendants recursively
        List<User> descendants = getAllDescendants(creatorId);
        // Filter by the specified role
        List<User> filtered = descendants.stream()
                .filter(user -> role.equals(user.getRole()))
                .collect(Collectors.toList());
        // Map entities to DTOs
        return filtered.stream()
                .map(user -> modelMapper.map(user, UserDTO.class))
                .collect(Collectors.toList());
    }

}
