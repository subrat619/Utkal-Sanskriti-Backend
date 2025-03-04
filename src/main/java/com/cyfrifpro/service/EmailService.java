package com.cyfrifpro.service;

public interface EmailService {
	void sendEmail(String to, String subject, String body);

	// This method sends email to master admin
	void notifyMasterAdmin(String subject, String body);
}
