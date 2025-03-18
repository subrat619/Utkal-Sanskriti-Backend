package com.cyfrifpro.service;

import com.cyfrifpro.DTO.ResetPasswordRequest;
import com.cyfrifpro.DTO.ResetPasswordWithOldPasswordRequest;

public interface PasswordResetService {
	void sendOtp(String email);

	void resetPassword(ResetPasswordRequest request);

	void resetPasswordWithOldPassword(ResetPasswordWithOldPasswordRequest request);

	void verifyOtp(String email, String otp);

	void changePasswordAfterOtpVerification(String email, String newPassword, String confirmNewPassword);
}
