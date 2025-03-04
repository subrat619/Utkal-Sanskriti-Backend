package com.cyfrifpro.serviceImpl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.cyfrifpro.service.EmailService;

@Service
public class EmailServiceImpl implements EmailService {

	private final JavaMailSender mailSender;

	@Value("${master.email}")
	private String masterEmail;

//    @Value("${teamleader.email}")
//    private String teamLeaderEmail;

	public EmailServiceImpl(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	@Async
	@Override
	public void sendEmail(String to, String subject, String body) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom("helpdesk@cyfrif.com");
		message.setTo(to);
		message.setSubject(subject);
		message.setText(body);
		mailSender.send(message);
	}

	@Async
	@Override
	public void notifyMasterAdmin(String subject, String body) {
		sendEmail(masterEmail, subject, body);
//        sendEmail(teamLeaderEmail, subject, body);
	}
}
