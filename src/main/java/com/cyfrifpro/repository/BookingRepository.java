package com.cyfrifpro.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cyfrifpro.model.Booking;
import com.cyfrifpro.model.BookingStatus;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
	List<Booking> findByStatus(BookingStatus status);

	// Method to fetch bookings by mid-level user's ID
	List<Booking> findByMidLevel_UserId(Long midLevelId);

	// Method to fetch bookings by Team Leader user's ID
	List<Booking> findByUpdatedBy_UserId(Long teamLeaderId);

	// Method to fetch bookings by Support Service user's ID
	List<Booking> findBySupportService_UserId(Long supportId);

	// Method to fetch bookings by Temple Admin user's ID
	List<Booking> findByTempleAdmin_UserId(Long templeAdminId);

	// Method to fetch bookings by guide's user ID
	List<Booking> findByAssignedGuide_UserId(Long guideId);
	
	// Fetch the asociated guide, temple admin and the support service who assigned to the client
	List<Booking> findByClient_UserId(Long clientId);

}
