package com.cyfrifpro.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cyfrifpro.DTO.BookingAssociationDTO;
import com.cyfrifpro.DTO.BookingDTO;
import com.cyfrifpro.DTO.UserDTO;
import com.cyfrifpro.service.BookingService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/bookings")
@Validated
@CrossOrigin(origins = "http://localhost:3000")
public class BookingController {

	private final BookingService bookingService;

	public BookingController(BookingService bookingService) {
		this.bookingService = bookingService;
	}

	// Create a new booking. The clientId can be passed as a request parameter.
	@PostMapping
	public ResponseEntity<BookingDTO> createBooking(@Valid @RequestBody BookingDTO bookingDTO,
			@RequestParam Long clientId) {
		BookingDTO createdBooking = bookingService.createBooking(bookingDTO, clientId);
		return new ResponseEntity<>(createdBooking, HttpStatus.CREATED);
	}

	// Update an existing booking (for simplicity, updating only booking date here)
	@PutMapping("/{bookingId}")
	public ResponseEntity<BookingDTO> updateBooking(@PathVariable Long bookingId,
			@Valid @RequestBody BookingDTO bookingDTO) {
		BookingDTO updatedBooking = bookingService.updateBooking(bookingId, bookingDTO);
		return ResponseEntity.ok(updatedBooking);
	}

	// Get a booking by its id
	@GetMapping("/{bookingId}")
	public ResponseEntity<BookingDTO> getBooking(@PathVariable Long bookingId) {
		BookingDTO booking = bookingService.getBookingById(bookingId);
		return ResponseEntity.ok(booking);
	}

	@GetMapping("/client/{clientId}")
	public ResponseEntity<List<BookingDTO>> getBookingsByClientId(@PathVariable Long clientId) {
		List<BookingDTO> bookings = bookingService.getBookingsByClientId(clientId);
		return ResponseEntity.ok(bookings);
	}

	// Get all bookings
	@GetMapping
	public ResponseEntity<List<BookingDTO>> getAllBookings() {
		List<BookingDTO> bookings = bookingService.getAllBookings();
		return ResponseEntity.ok(bookings);
	}

	// Endpoint for top level who assign the mid level
//	@PreAuthorize("hasRole('TOP_LEVEL')")
	@PutMapping("/assign_by_top_level/{bookingId}")
	public ResponseEntity<BookingDTO> updateBookingByTopLevel(@PathVariable Long bookingId,
			@Valid @RequestBody BookingDTO bookingDTO, @RequestParam Long topLevelId) {
		BookingDTO updated = bookingService.updateBookingByTopLevel(bookingId, bookingDTO, topLevelId);
		return ResponseEntity.ok(updated);
	}

	// Endpoint for mid level who assign the team leader
//		@PreAuthorize("hasRole('TOP_LEVEL')")
	@PutMapping("/assign_by_mid_level/{bookingId}")
	public ResponseEntity<BookingDTO> updateBookingByMidLevel(@PathVariable Long bookingId,
			@Valid @RequestBody BookingDTO bookingDTO, @RequestParam Long midLevelId) {
		BookingDTO updated = bookingService.updateBookingByMidLevel(bookingId, bookingDTO, midLevelId);
		return ResponseEntity.ok(updated);
	}

	// Endpoint for team leader who assign the support service and temple admin
//	@PreAuthorize("hasRole('TOP_LEVEL')")
	@PutMapping("/assign_by_team_leader/{bookingId}")
	public ResponseEntity<BookingDTO> updateBookingDetailsByTeamLeader(@PathVariable Long bookingId,
			@Valid @RequestBody BookingDTO bookingDTO, @RequestParam Long teamLeaderId) {
		BookingDTO updated = bookingService.updateBookingDetailsByTeamLeader(bookingId, bookingDTO, teamLeaderId);
		return ResponseEntity.ok(updated);
	}

	// Endpoint for temple admin who assign the guide
//	@PreAuthorize("hasRole('TEMPLE_ADMIN')")
	@PutMapping("/assign_guide/{bookingId}")
	public ResponseEntity<BookingDTO> assignGuideToBooking(@PathVariable Long bookingId, @RequestParam Long guideId,
			@RequestParam Long templeAdminId) {
		BookingDTO updated = bookingService.assignGuideToBooking(bookingId, guideId, templeAdminId);
		return ResponseEntity.ok(updated);
	}

	@GetMapping("/{bookingId}/assigned_guide")
	public ResponseEntity<UserDTO> getAssignedGuideDetails(@PathVariable Long bookingId) {
		UserDTO guideDetails = bookingService.getAssignedGuideDetails(bookingId);
		return ResponseEntity.ok(guideDetails);
	}

	@GetMapping("/{bookingId}/assigned_support_service")
	public ResponseEntity<UserDTO> getAssignedSupportServiceDetails(@PathVariable Long bookingId) {
		UserDTO supportServiceDetails = bookingService.getAssignedSupportServiceDetails(bookingId);
		return ResponseEntity.ok(supportServiceDetails);
	}

	// New endpoint to fetch team leader details
	@GetMapping("/{bookingId}/team_leader")
	public ResponseEntity<UserDTO> getTeamLeaderDetails(@PathVariable Long bookingId) {
		UserDTO teamLeaderDetails = bookingService.getTeamLeaderDetails(bookingId);
		return ResponseEntity.ok(teamLeaderDetails);
	}

	// End point for finding bookings whose status new (This End point specially for
	// top level)
	@GetMapping("/status/new")
	public ResponseEntity<List<BookingDTO>> getNewBookings() {
		List<BookingDTO> bookings = bookingService.getNewBookings();
		return ResponseEntity.ok(bookings);
	}

	// End point for finding bookings whose status update_by_top_level
	@GetMapping("/status/update_by_top_level")
	public ResponseEntity<List<BookingDTO>> getUpdateByTopLevelBookings() {
		List<BookingDTO> bookings = bookingService.getUpdateByTopLevelBookings();
		return ResponseEntity.ok(bookings);
	}

	// End point for finding bookings whose status update_by_mid_level
	@GetMapping("/status/update_by_mid_level")
	public ResponseEntity<List<BookingDTO>> getUpdateByMidLevelBookings() {
		List<BookingDTO> bookings = bookingService.getUpdateByMidLevelBookings();
		return ResponseEntity.ok(bookings);
	}

	// End point for finding bookings whose status updated_by_team_leader
	@GetMapping("/status/updated_by_team_leader")
	public ResponseEntity<List<BookingDTO>> getUpdatedByTeamLeaderBookings() {
		List<BookingDTO> bookings = bookingService.getUpdatedByTeamLeaderBookings();
		return ResponseEntity.ok(bookings);
	}

	// End point for finding bookings whose status assigned_by_temple_admin
	@GetMapping("/status/assigned_by_temple_admin")
	public ResponseEntity<List<BookingDTO>> getAssignedByTempleAdminBookings() {
		List<BookingDTO> bookings = bookingService.getAssignedByTempleAdminBookings();
		return ResponseEntity.ok(bookings);
	}

	// Method to fetch bookings by mid-level user's ID
	@GetMapping("/midlevel/{midLevelId}")
	public ResponseEntity<List<BookingDTO>> getBookingsByMidLevelId(@PathVariable Long midLevelId) {
		List<BookingDTO> bookings = bookingService.getBookingsByMidLevelId(midLevelId);
		return ResponseEntity.ok(bookings);
	}

	// Method to fetch bookings by Team Leader user's ID
	@GetMapping("/teamLeader/{teamLeaderId}")
	public ResponseEntity<List<BookingDTO>> getBookingsByTeamLeaderId(@PathVariable Long teamLeaderId) {
		List<BookingDTO> bookings = bookingService.getBookingsByteamLeaderId(teamLeaderId);
		return ResponseEntity.ok(bookings);
	}

	// Method to fetch bookings by Support Service user's ID
	@GetMapping("/support_service/{supportId}")
	public ResponseEntity<List<BookingDTO>> getBookingsBySupportId(@PathVariable Long supportId) {
		List<BookingDTO> bookings = bookingService.getBookingsBySupportId(supportId);
		return ResponseEntity.ok(bookings);
	}

	// Method to fetch bookings by Temple Admin user's ID
	@GetMapping("/temple_admin/{templeAdminId}")
	public ResponseEntity<List<BookingDTO>> getBookingsByTempleAdminId(@PathVariable Long templeAdminId) {
		List<BookingDTO> bookings = bookingService.getBookingsByTempleAdminId(templeAdminId);
		return ResponseEntity.ok(bookings);
	}

	// Method to fetch bookings by guide's user ID
	@GetMapping("/guide/{guideId}")
	public ResponseEntity<List<BookingDTO>> getBookingsByGuideId(@PathVariable Long guideId) {
		List<BookingDTO> bookings = bookingService.getBookingsByGuideId(guideId);
		return ResponseEntity.ok(bookings);
	}

	// End point to fetch associations (support service, temple admin, guide) for
	// a given client id
	@GetMapping("/associations/{clientId}")
	public ResponseEntity<List<BookingAssociationDTO>> getAssociationsByClientId(@PathVariable Long clientId) {
		List<BookingAssociationDTO> associations = bookingService.getAssociationsByClientId(clientId);
		return ResponseEntity.ok(associations);
	}

	// Fetch Temple Admin By booking id
	@GetMapping("/{bookingId}/temple_admin")
	public ResponseEntity<UserDTO> getAssignedTempleAdmin(@PathVariable Long bookingId) {
		UserDTO templeAdmin = bookingService.getAssignedTempleAdminByBookingId(bookingId);
		return ResponseEntity.ok(templeAdmin);
	}

	@PutMapping("/confirm/{bookingId}")
	public ResponseEntity<BookingDTO> confirmBookingByTopLevel(@PathVariable Long bookingId,
			@RequestParam Long topLevelId) {
		BookingDTO updatedBooking = bookingService.confirmBookingByTopLevel(bookingId, topLevelId);
		return ResponseEntity.ok(updatedBooking);
	}

	@PutMapping("/complete/{bookingId}")
	public ResponseEntity<BookingDTO> completeBooking(@PathVariable Long bookingId, @RequestParam Long guideId) {
		BookingDTO updatedBooking = bookingService.completeBooking(bookingId, guideId);
		return ResponseEntity.ok(updatedBooking);
	}

	// End point by which support service get bookings which he got assigned.....
	@GetMapping("/supportService/{supportServiceId}")
	public ResponseEntity<List<BookingDTO>> getBookingsBySupportServiceId(@PathVariable Long supportServiceId) {
		List<BookingDTO> bookings = bookingService.getBookingsBySupportServiceId(supportServiceId);
		return ResponseEntity.ok(bookings);
	}

	// Endpoint to fetch assigned guide details by client ID
	@GetMapping("/by_client/{clientId}/assigned_guide")
	public ResponseEntity<List<UserDTO>> getAssignedGuideDetailsByClientId(@PathVariable Long clientId) {
		List<UserDTO> guideDetails = bookingService.getAssignedGuideDetailsByClientId(clientId);
		return ResponseEntity.ok(guideDetails);
	}

	// Endpoint to fetch assigned temple admin details by client ID
	@GetMapping("/by_client/{clientId}/temple_admin")
	public ResponseEntity<List<UserDTO>> getAssignedTempleAdminDetailsByClientId(@PathVariable Long clientId) {
		List<UserDTO> templeAdminDetails = bookingService.getAssignedTempleAdminDetailsByClientId(clientId);
		return ResponseEntity.ok(templeAdminDetails);
	}

	// Endpoint to fetch assigned support service details by client ID
	@GetMapping("/by_client/{clientId}/support_service")
	public ResponseEntity<List<UserDTO>> getAssignedSupportServiceDetailsByClientId(@PathVariable Long clientId) {
		List<UserDTO> supportServiceDetails = bookingService.getAssignedSupportServiceDetailsByClientId(clientId);
		return ResponseEntity.ok(supportServiceDetails);
	}
}
