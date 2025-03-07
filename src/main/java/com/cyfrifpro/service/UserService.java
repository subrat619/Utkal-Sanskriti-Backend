package com.cyfrifpro.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cyfrifpro.DTO.TempleAdminMappingRequest;
import com.cyfrifpro.DTO.TempleDetailsDTO;
import com.cyfrifpro.DTO.UserDTO;
import com.cyfrifpro.Exception.ResourceNotFoundException;
import com.cyfrifpro.model.Role;
import com.cyfrifpro.model.TempleDetails;
import com.cyfrifpro.model.User;
import com.cyfrifpro.repository.TempleDetailsRepository;
import com.cyfrifpro.repository.UserRepository;
import com.cyfrifpro.util.RoleHierarchy;

import jakarta.transaction.Transactional;

@Transactional
@Service
public class UserService {

	private static final Logger logger = LoggerFactory.getLogger(UserService.class);

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private TempleDetailsRepository templeDetailsRepository;

	public UserDTO registerUser(UserDTO userDTO, Long creatorUserId) {
		try {
			logger.info("Attempting to register user with email: {}", userDTO.getEmail());
			User user = modelMapper.map(userDTO, User.class);
			user.setPassword(passwordEncoder.encode(user.getPassword()));

			if (userRepo.count() == 0) {
				logger.info("No users found. Registering first user as MASTER_ADMIN.");
				user.setCreatedBy(null);
				user.setRole(Role.MASTER_ADMIN); // Assign MASTER_ADMIN to the first user
			} else {
				// If the role is CLIENT, allow registration without a creator.
				if (user.getRole() == Role.CLIENT) {
					user.setCreatedBy(null);
				} else {
					User creator = userRepo.findById(creatorUserId)
							.orElseThrow(() -> new ResourceNotFoundException("User", "userId", creatorUserId));

					logger.info("Creator {} with role {} is registering new user", creator.getEmail(),
							creator.getRole());

					// Validate role creation using RoleHierarchy
					if (!RoleHierarchy.canCreateRole(creator.getRole(), user.getRole())) {
						logger.warn("User with role {} cannot create a user with role {}", creator.getRole(),
								user.getRole());
						throw new IllegalArgumentException(
								"Role " + creator.getRole() + " cannot create users with role " + user.getRole());
					}

					user.setCreatedBy(creator);
				}
			}

			User savedUser = userRepo.save(user);
			logger.info("User registered successfully with email: {}", savedUser.getEmail());
			return modelMapper.map(savedUser, UserDTO.class);
		} catch (DataIntegrityViolationException e) {
			logger.error("Data integrity violation during registration for email: {}", userDTO.getEmail(), e);
			throw new IllegalArgumentException("User already exists with emailId: " + userDTO.getEmail(), e);
		} catch (Exception e) {
			logger.error("Unexpected error during user registration for email: {}", userDTO.getEmail(), e);
			throw e;
		}
	}

	public List<User> getChildUsersByCreatorId(Long creatorId) {
		return userRepo.findByCreatedBy_UserId(creatorId);
	}

	public Map<String, Object> registerTempleAdminMapping(TempleAdminMappingRequest request) {
		// Fetch the government user (creator) and verify role
		User government = userRepo.findById(request.getGovernmentId())
				.orElseThrow(() -> new ResourceNotFoundException("User", "governmentId", request.getGovernmentId()));
		if (!government.getRole().equals(Role.GOVERMENT)) {
			throw new IllegalArgumentException("Only users with GOVERMENT role can map a temple admin to a temple.");
		}

		// Prepare the temple admin data by enforcing the TEMPLE_ADMIN role
		UserDTO templeAdminDTO = request.getTempleAdminDTO();
		templeAdminDTO.setRole(Role.TEMPLE_ADMIN);

		// Register the temple admin (using existing registration method)
		UserDTO registeredTempleAdmin = registerUser(templeAdminDTO, request.getGovernmentId());

		// Retrieve the actual temple admin entity
		User templeAdminUser = userRepo.findById(registeredTempleAdmin.getUserId()).orElseThrow(
				() -> new ResourceNotFoundException("User", "templeAdminId", registeredTempleAdmin.getUserId()));

		// Fetch the existing temple by its ID
		TempleDetails temple = templeDetailsRepository.findById(request.getTempleId())
				.orElseThrow(() -> new ResourceNotFoundException("TempleDetails", "templeId", request.getTempleId()));

		// Associate the temple admin with the temple
		temple.setTempleAdmin(templeAdminUser);
		templeDetailsRepository.save(temple);

		// Prepare a response map containing both the temple admin and the updated
		// temple details
		Map<String, Object> response = new HashMap<>();
		response.put("templeAdmin", registeredTempleAdmin);
		// Assuming you have a mapping from TempleDetails to TempleDetailsDTO
		response.put("templeDetails", modelMapper.map(temple, TempleDetailsDTO.class));

		return response;
	}
}
