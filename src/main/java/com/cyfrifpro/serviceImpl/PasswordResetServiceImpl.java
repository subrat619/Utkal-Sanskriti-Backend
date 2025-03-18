package com.cyfrifpro.serviceImpl;

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cyfrifpro.DTO.ResetPasswordRequest;
import com.cyfrifpro.DTO.ResetPasswordWithOldPasswordRequest;
import com.cyfrifpro.model.User;
import com.cyfrifpro.repository.UserRepository;
import com.cyfrifpro.service.EmailService;
import com.cyfrifpro.service.PasswordResetService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class PasswordResetServiceImpl implements PasswordResetService {

	private static final Logger logger = LoggerFactory.getLogger(PasswordResetServiceImpl.class);

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final EmailService emailService;

	// In-memory storage for OTPs (email -> OTP)
	private final ConcurrentHashMap<String, String> otpStorage = new ConcurrentHashMap<>();

	// In-memory storage for verified emails (after OTP verification)
	private final ConcurrentHashMap<String, Boolean> verifiedEmails = new ConcurrentHashMap<>();

	public PasswordResetServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder,
			EmailService emailService) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.emailService = emailService;
	}

	@Async
	@Override
	public void sendOtp(String email) {
		// Check if user exists
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new RuntimeException("User not found with email: " + email));
		System.out.println(user);
		// Generate a 6-digit OTP
		String otp = String.format("%06d", new Random().nextInt(999999));
		otpStorage.put(email, otp);
		logger.info("Generated OTP {} for email {}", otp, email);
		// Prepare email content
		String subject = "Password Reset OTP";
		String body = "Dear user,\n\nYour OTP for password reset is: " + otp + "\n\nRegards,\nSupport Team";
		emailService.sendEmail(email, subject, body);
		logger.info("OTP email sent to {}", email);
	}

	@Async
	@Override
	public void resetPassword(ResetPasswordRequest request) {
		String storedOtp = otpStorage.get(request.getEmail());
		if (storedOtp == null || !storedOtp.equals(request.getOtp())) {
			throw new RuntimeException("Invalid or expired OTP");
		}
		User user = userRepository.findByEmail(request.getEmail())
				.orElseThrow(() -> new RuntimeException("User not found with email: " + request.getEmail()));
		user.setPassword(passwordEncoder.encode(request.getNewPassword()));
		userRepository.save(user);
		otpStorage.remove(request.getEmail());
		// Also remove from verifiedEmails if present
		verifiedEmails.remove(request.getEmail());
		logger.info("Password reset successful for email {}", request.getEmail());
	}

	@Override
	public void resetPasswordWithOldPassword(ResetPasswordWithOldPasswordRequest request) {
		String storedOtp = otpStorage.get(request.getEmail());
		if (storedOtp == null || !storedOtp.equals(request.getOtp())) {
			logger.warn("Invalid or expired OTP for email {}", request.getEmail());
			throw new IllegalArgumentException("Invalid or expired OTP");
		}
		User user = userRepository.findByEmail(request.getEmail())
				.orElseThrow(() -> new IllegalArgumentException("User not found with email: " + request.getEmail()));
		if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
			logger.warn("Old password does not match for email {}", request.getEmail());
			throw new IllegalArgumentException("Old password is incorrect");
		}
		if (!request.getNewPassword().equals(request.getConfirmNewPassword())) {
			logger.warn("New password and confirmation do not match for email {}", request.getEmail());
			throw new IllegalArgumentException("New password and confirmation do not match");
		}
		user.setPassword(passwordEncoder.encode(request.getNewPassword()));
		userRepository.save(user);
		otpStorage.remove(request.getEmail());
		verifiedEmails.remove(request.getEmail());
		logger.info("Password reset successfully for email {}", request.getEmail());
	}

	@Override
	public void verifyOtp(String email, String otp) {
		String storedOtp = otpStorage.get(email);
		if (storedOtp == null || !storedOtp.equals(otp)) {
			throw new RuntimeException("Invalid or expired OTP");
		}
		// Mark the email as verified
		verifiedEmails.put(email, Boolean.TRUE);
		logger.info("OTP verified for email {}", email);
	}

	@Override
	public void changePasswordAfterOtpVerification(String email, String newPassword, String confirmNewPassword) {
		if (!newPassword.equals(confirmNewPassword)) {
			throw new RuntimeException("New password and confirm new password do not match");
		}
		if (!verifiedEmails.containsKey(email)) {
			throw new RuntimeException("OTP not verified for email: " + email);
		}
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new RuntimeException("User not found with email: " + email));
		user.setPassword(passwordEncoder.encode(newPassword));
		userRepository.save(user);
		verifiedEmails.remove(email);
		logger.info("Password changed successfully for email {}", email);
	}
}
