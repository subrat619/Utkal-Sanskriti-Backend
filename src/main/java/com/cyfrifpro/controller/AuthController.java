package com.cyfrifpro.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cyfrifpro.DTO.ChangePasswordRequest;
import com.cyfrifpro.DTO.ForgotPasswordRequest;
import com.cyfrifpro.DTO.LoginCredentials;
import com.cyfrifpro.DTO.ResetPasswordRequest;
import com.cyfrifpro.DTO.ResetPasswordWithOldPasswordRequest;
import com.cyfrifpro.DTO.TempleAdminMappingRequest;
import com.cyfrifpro.DTO.UserDTO;
import com.cyfrifpro.config.JWTUtil;
import com.cyfrifpro.model.User;
import com.cyfrifpro.repository.UserRepository;
import com.cyfrifpro.service.PasswordResetService;
import com.cyfrifpro.service.UserService;
import com.cyfrifpro.service.UserService2;
import com.cyfrifpro.util.SecurityUtils;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
//@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

	private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

	@Autowired
	private UserService userService;

	@Autowired
	private JWTUtil jwtUtil;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserRepository userRepository;

	private final PasswordResetService passwordResetService;
	private final UserService2 userService2;

	public AuthController(PasswordResetService passwordResetService, UserService2 userService2) {
		this.passwordResetService = passwordResetService;
		this.userService2 = userService2;
	}

	// Register
	@Operation(summary = "Method for registration... 👍")
	@PostMapping("/register")
	public ResponseEntity<Map<String, Object>> registerHandler(@Valid @RequestBody UserDTO userDTO) {
		try {
			logger.info("Registration attempt for email: {}", userDTO.getEmail());
			Long creatorUserId = null;
			if (userRepository.count() > 0) {
				creatorUserId = SecurityUtils.getLoggedInUserId();
				logger.info("Creator User ID from security context: {}", creatorUserId);
			}
			UserDTO registeredUser = userService.registerUser(userDTO, creatorUserId);
			String token = jwtUtil.generateToken(registeredUser.getUserId(), registeredUser.getEmail(),
					registeredUser.getRole().name());
			logger.info("User registered successfully. Token generated for email: {}", registeredUser.getEmail());
			return new ResponseEntity<>(Collections.singletonMap("jwt-token", token), HttpStatus.CREATED);
		} catch (IllegalArgumentException ex) {
			logger.error("Registration error: ", ex);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(Collections.singletonMap("error", ex.getMessage()));
		} catch (Exception e) {
			logger.error("Unexpected registration error: ", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Collections.singletonMap("error", "An unexpected error occurred"));
		}
	}

	// login
	@Operation(summary = "Method for login... 👍")
	@PostMapping("/login")
	public ResponseEntity<Map<String, Object>> loginHandler(@Valid @RequestBody LoginCredentials credentials) {
		try {
			logger.info("Login attempt for email: {}", credentials.getEmail());
			UsernamePasswordAuthenticationToken authCredentials = new UsernamePasswordAuthenticationToken(
					credentials.getEmail(), credentials.getPassword());
			Authentication auth = authenticationManager.authenticate(authCredentials);
			UserDetails userDetails = (UserDetails) auth.getPrincipal();
			String email = userDetails.getUsername();
			String roleName = userDetails.getAuthorities().stream().findFirst()
					.map(authority -> authority.getAuthority().replace("ROLE_", "")).orElse("NULL");
			logger.info("User {} authenticated with role {}", email, roleName);
			User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
			String token = jwtUtil.generateToken(user.getUserId(), email, roleName);
			Map<String, Object> response = new HashMap<>();
			response.put("jwt-token", token);
			logger.info("JWT token generated for user {}", email);
			return ResponseEntity.ok(response);
		} catch (BadCredentialsException e) {
			logger.warn("Invalid credentials for email: {}", credentials.getEmail());
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(Collections.singletonMap("error", "Invalid email or password"));
		} catch (Exception e) {
			logger.error("Unexpected error during login", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Collections.singletonMap("error", "An unexpected error occurred"));
		}
	}

	// Forget password
	@Operation(summary = "Method for forget password... 👍")
	@PostMapping("/forgot_password")
	public ResponseEntity<Map<String, String>> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
		try {
			logger.info("Forgot password request for email: {}", request.getEmail());
			passwordResetService.sendOtp(request.getEmail());
			return ResponseEntity.ok(Collections.singletonMap("message", "OTP sent to your email"));
		} catch (Exception e) {
			logger.error("Error in forgotPassword", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Collections.singletonMap("error", "An error occurred while processing your request"));
		}
	}

	// Reset password through otp
	@Operation(summary = "Method for reset password through otp... 👍")
	@PostMapping("/reset_password")
	public ResponseEntity<Map<String, String>> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
		try {
			logger.info("Reset password request for email: {}", request.getEmail());
			passwordResetService.resetPassword(request);
			return ResponseEntity.ok(Collections.singletonMap("message", "Password reset successful"));
		} catch (Exception e) {
			logger.error("Error in resetPassword", e);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(Collections.singletonMap("error", e.getMessage()));
		}
	}

	// Change password without otp
	@Operation(summary = "Method for change password... 👍")
	@PostMapping("/change_password")
	public ResponseEntity<Map<String, String>> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
		try {
			// Get the currently logged-in user's ID (assumes you have implemented
			// SecurityUtils.getLoggedInUserId())
			Long userId = SecurityUtils.getLoggedInUserId();
			if (userId == null) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
						.body(Collections.singletonMap("error", "User not authenticated."));
			}
			userService2.changePassword(userId, request);
			return ResponseEntity.ok(Collections.singletonMap("message", "Password changed successfully."));
		} catch (IllegalArgumentException e) {
			logger.error("Change password error: ", e);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(Collections.singletonMap("error", e.getMessage()));
		} catch (Exception e) {
			logger.error("Unexpected error during password change: ", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Collections.singletonMap("error", "An unexpected error occurred."));
		}
	}

	// Change password with otp
	@Operation(summary = "Method for change password through old password... 👍")
	@PostMapping("/reset_password_old")
	public ResponseEntity<Map<String, String>> resetPasswordWithOldPassword(
			@Valid @RequestBody ResetPasswordWithOldPasswordRequest request) {
		try {
			logger.info("Reset password request received for email: {}", request.getEmail());
			passwordResetService.resetPasswordWithOldPassword(request);
			return ResponseEntity.ok(Collections.singletonMap("message", "Password reset successful"));
		} catch (IllegalArgumentException e) {
			logger.error("Error during password reset: ", e);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(Collections.singletonMap("error", e.getMessage()));
		} catch (Exception e) {
			logger.error("Unexpected error during password reset: ", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Collections.singletonMap("error", "An unexpected error occurred"));
		}
	}

	@Operation(summary = "Method for register temple admin with temple... 👍")
	@PostMapping("/register_temple_admin_with_temple")
	public ResponseEntity<Map<String, Object>> registerTempleAdminMapping(
			@Valid @RequestBody TempleAdminMappingRequest request) {
		Map<String, Object> result = userService.registerTempleAdminMapping(request);
		return new ResponseEntity<>(result, HttpStatus.CREATED);
	}

}
