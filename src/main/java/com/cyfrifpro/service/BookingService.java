package com.cyfrifpro.service;

import java.util.List;

import com.cyfrifpro.DTO.BookingAssociationDTO;
import com.cyfrifpro.DTO.BookingDTO;
import com.cyfrifpro.DTO.UserDTO;

public interface BookingService {
	BookingDTO createBooking(BookingDTO bookingDTO, Long clientId);

	BookingDTO updateBooking(Long bookingId, BookingDTO bookingDTO);

//	BookingDTO updateBookingByTeamLeader(Long bookingId, BookingDTO bookingDTO, Long teamLeaderId);

	// Endpoint for top level who assign the mid level
	BookingDTO updateBookingByTopLevel(Long bookingId, BookingDTO bookingDTO, Long topLevelId);

	// Endpoint for mid level who assign the team leader
	BookingDTO updateBookingByMidLevel(Long bookingId, BookingDTO bookingDTO, Long midLevelId);

	// Endpoint for team leader who assign the support service and temple admin
	BookingDTO updateBookingDetailsByTeamLeader(Long bookingId, BookingDTO bookingDTO, Long teamLeaderId);

	// Endpoint for temple admin who assign the guide
	BookingDTO assignGuideToBooking(Long bookingId, Long guideId, Long templeAdminId);

	BookingDTO getBookingById(Long bookingId);

	List<BookingDTO> getAllBookings();

	UserDTO getAssignedGuideDetails(Long bookingId);

	UserDTO getAssignedSupportServiceDetails(Long bookingId);

	UserDTO getTeamLeaderDetails(Long bookingId);

	// New methods for fetching bookings by each status
	List<BookingDTO> getNewBookings();

	List<BookingDTO> getUpdatedByTeamLeaderBookings();

	List<BookingDTO> getAssignedByTempleAdminBookings();

	List<BookingDTO> getUpdateByTopLevelBookings();

	List<BookingDTO> getUpdateByMidLevelBookings();

	// Method to fetch bookings by mid-level id
	List<BookingDTO> getBookingsByMidLevelId(Long midLevelId);

	// Method to fetch bookings by team leader id
	List<BookingDTO> getBookingsByteamLeaderId(Long teamLeaderId);

	// Method to fetch bookings by team leader id
	List<BookingDTO> getBookingsBySupportId(Long supportId);

	// Method to fetch bookings by temple admin id
	List<BookingDTO> getBookingsByTempleAdminId(Long templeAdminId);

	// Method to fetch bookings by guide id
	List<BookingDTO> getBookingsByGuideId(Long guideId);

	// Method to fetch associations by client id
	List<BookingAssociationDTO> getAssociationsByClientId(Long clientId);

	// Method for completing a booking by the assigned guide
	BookingDTO completeBooking(Long bookingId, Long guideId);

}
