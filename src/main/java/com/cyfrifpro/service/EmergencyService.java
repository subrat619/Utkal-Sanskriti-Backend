package com.cyfrifpro.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.cyfrifpro.DTO.EmergencyAlertDTO;
import com.cyfrifpro.model.Booking;
import com.cyfrifpro.model.User;
import com.cyfrifpro.repository.BookingRepository;
import com.cyfrifpro.repository.UserRepository;

@Service
public class EmergencyService {

	private static final Logger logger = LoggerFactory.getLogger(EmergencyService.class);

	private final EmailService emailService;
	private final BookingRepository bookingRepository;
	private final UserRepository userRepository;

	public EmergencyService(EmailService emailService, BookingRepository bookingRepository,
			UserRepository userRepository) {
		this.emailService = emailService;
		this.bookingRepository = bookingRepository;
		this.userRepository = userRepository;
	}

	@Async
	public void processEmergencyAlert(Long clientId, EmergencyAlertDTO alertDTO) {
	    logger.warn("Emergency alert triggered by client {}: {}", clientId, alertDTO.getDescription());

	    String guideInfo = "";
	    String templeAdminInfo = "";
	    String teamLeaderInfo = "";
	    String supportServiceInfo = "";
	    String midLevelInfo = "";
	    String topLevelInfo = "";
	    String locationInfo = "";

	    User associatedGuide = null;
	    User associatedTempleAdmin = null;
	    User updatingTeamLeader = null;
	    User associatedSupportService = null;
	    User associatedMidLevel = null;
	    User associatedTopLevel = null;

	    if (alertDTO.getBookingId() != null) {
	        Booking booking = bookingRepository.findById(alertDTO.getBookingId()).orElse(null);
	        if (booking != null) {
	            // Retrieve assigned guide if available
	            if (booking.getAssignedGuide() != null) {
	                associatedGuide = userRepository.findById(booking.getAssignedGuide().getUserId()).orElse(null);
	                if (associatedGuide != null) {
	                    guideInfo = "\nAssociated Guide: " 
	                            + associatedGuide.getFirstName() + " " 
	                            + associatedGuide.getLastName() 
	                            + "\nContact: " + associatedGuide.getContactNumber()
	                            + "\nEmail: " + associatedGuide.getEmail();
	                    // Retrieve temple admin from the guide's createdBy field (if set)
	                    if (associatedGuide.getCreatedBy() != null) {
	                        associatedTempleAdmin = userRepository.findById(associatedGuide.getCreatedBy().getUserId()).orElse(null);
	                        if (associatedTempleAdmin != null) {
	                            templeAdminInfo = "\nTemple Admin Details:\n" +
	                                "Name: " + associatedTempleAdmin.getFirstName() + " " + associatedTempleAdmin.getLastName() + "\n" +
	                                "Email: " + associatedTempleAdmin.getEmail() + "\n" +
	                                "Contact: " + associatedTempleAdmin.getContactNumber();
	                        }
	                    }
	                }
	            }
	            // Retrieve the team leader who updated the booking, if available
	            if (booking.getUpdatedBy() != null) {
	                updatingTeamLeader = userRepository.findById(booking.getUpdatedBy().getUserId()).orElse(null);
	                if (updatingTeamLeader != null) {
	                    teamLeaderInfo = "\nUpdated By Team Leader:\n" +
	                        "Name: " + updatingTeamLeader.getFirstName() + " " + updatingTeamLeader.getLastName() + "\n" +
	                        "Email: " + updatingTeamLeader.getEmail() + "\n" +
	                        "Contact: " + updatingTeamLeader.getContactNumber();
	                    // Retrieve the mid-level user who created the team leader, if available
	                    if (updatingTeamLeader.getCreatedBy() != null) {
	                        associatedMidLevel = userRepository.findById(updatingTeamLeader.getCreatedBy().getUserId()).orElse(null);
	                        if (associatedMidLevel != null) {
	                            midLevelInfo = "\nMid Level Details:\n" +
	                                "Name: " + associatedMidLevel.getFirstName() + " " + associatedMidLevel.getLastName() + "\n" +
	                                "Email: " + associatedMidLevel.getEmail() + "\n" +
	                                "Contact: " + associatedMidLevel.getContactNumber();
	                            // Retrieve the top-level user who created the mid-level user, if available
	                            if (associatedMidLevel.getCreatedBy() != null) {
	                                associatedTopLevel = userRepository.findById(associatedMidLevel.getCreatedBy().getUserId()).orElse(null);
	                                if (associatedTopLevel != null) {
	                                    topLevelInfo = "\nTop Level Details:\n" +
	                                        "Name: " + associatedTopLevel.getFirstName() + " " + associatedTopLevel.getLastName() + "\n" +
	                                        "Email: " + associatedTopLevel.getEmail() + "\n" +
	                                        "Contact: " + associatedTopLevel.getContactNumber();
	                                }
	                            }
	                        }
	                    }
	                }
	            }
	            // Retrieve support service user associated with this booking, if available
	            if (booking.getSupportService() != null) {
	                associatedSupportService = userRepository.findById(booking.getSupportService().getUserId()).orElse(null);
	                if (associatedSupportService != null) {
	                    supportServiceInfo = "\nSupport Service Details:\n" +
	                        "Name: " + associatedSupportService.getFirstName() + " " + associatedSupportService.getLastName() + "\n" +
	                        "Email: " + associatedSupportService.getEmail() + "\n" +
	                        "Contact: " + associatedSupportService.getContactNumber();
	                }
	            }
	        }
	    }
	    
	    // Append current location if provided
	    if (alertDTO.getCurrentLocation() != null && !alertDTO.getCurrentLocation().isEmpty()) {
	        locationInfo = "\nCurrent Location: " + alertDTO.getCurrentLocation();
	    }

	    String subject = "Emergency Alert from Client ID: " + clientId;
	    String body = "Emergency Details:\n" + alertDTO.getDescription() +
	                  locationInfo +
	                  guideInfo +
	                  templeAdminInfo +
	                  teamLeaderInfo +
	                  midLevelInfo +
	                  topLevelInfo +
	                  supportServiceInfo +
	                  "\nPlease take immediate action.";

	    // Notify support and team leader via default method
	    emailService.notifyMasterAdmin(subject, body);

	    // Conditionally send email to the associated guide if available and if flag is true
//	    if (alertDTO.isSendEmailToGuide() && associatedGuide != null && associatedGuide.getEmail() != null) {
//	        emailService.sendEmail(associatedGuide.getEmail(), subject, body);
//	        logger.info("Emergency email sent to associated guide: {}", associatedGuide.getEmail());
//	    }

	    // Send email to temple admin if available
//	    if (associatedTempleAdmin != null && associatedTempleAdmin.getEmail() != null) {
//	        emailService.sendEmail(associatedTempleAdmin.getEmail(), subject, body);
//	        logger.info("Emergency email sent to temple admin: {}", associatedTempleAdmin.getEmail());
//	    }

	    // Send email to team leader if available
//	    if (updatingTeamLeader != null && updatingTeamLeader.getEmail() != null) {
//	        emailService.sendEmail(updatingTeamLeader.getEmail(), subject, body);
//	        logger.info("Emergency email sent to team leader: {}", updatingTeamLeader.getEmail());
//	    }
	    
	    // Send email to support service if available
//	    if (associatedSupportService != null && associatedSupportService.getEmail() != null) {
//	        emailService.sendEmail(associatedSupportService.getEmail(), subject, body);
//	        logger.info("Emergency email sent to support service: {}", associatedSupportService.getEmail());
//	    }
	    
	    // Send email to mid level if available
//	    if (associatedMidLevel != null && associatedMidLevel.getEmail() != null) {
//	        emailService.sendEmail(associatedMidLevel.getEmail(), subject, body);
//	        logger.info("Emergency email sent to mid level: {}", associatedMidLevel.getEmail());
//	    }
	    
	    // Send email to top level if available
//	    if (associatedTopLevel != null && associatedTopLevel.getEmail() != null) {
//	        emailService.sendEmail(associatedTopLevel.getEmail(), subject, body);
//	        logger.info("Emergency email sent to top level: {}", associatedTopLevel.getEmail());
//	    }

	    logger.info("Notified support, team leader(s), associated guide (if flag true), temple admin, support service, mid level, and top level about the emergency.");
	}

}
