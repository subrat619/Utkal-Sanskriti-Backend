package com.cyfrifpro.serviceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.cyfrifpro.DTO.BookingAssociationDTO;
import com.cyfrifpro.DTO.BookingDTO;
import com.cyfrifpro.DTO.BookingItemDTO;
import com.cyfrifpro.DTO.TempleDetailsDTO;
import com.cyfrifpro.DTO.UserDTO;
import com.cyfrifpro.Exception.ResourceNotFoundException;
import com.cyfrifpro.model.Booking;
import com.cyfrifpro.model.BookingItem;
import com.cyfrifpro.model.BookingStatus;
import com.cyfrifpro.model.TempleDetails;
import com.cyfrifpro.model.User;
import com.cyfrifpro.repository.BookingRepository;
import com.cyfrifpro.repository.TempleDetailsRepository;
import com.cyfrifpro.repository.UserRepository;
import com.cyfrifpro.service.BookingService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class BookingServiceImpl implements BookingService {

	private static final Logger logger = LoggerFactory.getLogger(BookingServiceImpl.class);

	private final BookingRepository bookingRepository;
	private final UserRepository userRepository;
	private final TempleDetailsRepository templeDetailsRepository;
	private final ModelMapper modelMapper;

	public BookingServiceImpl(BookingRepository bookingRepository, UserRepository userRepository,
			TempleDetailsRepository templeDetailsRepository, ModelMapper modelMapper) {
		this.bookingRepository = bookingRepository;
		this.userRepository = userRepository;
		this.templeDetailsRepository = templeDetailsRepository;
		this.modelMapper = modelMapper;
	}

	@Override
	public BookingDTO createBooking(BookingDTO bookingDTO, Long clientId) {
		logger.info("Creating booking for clientId: {}", clientId);

		// Fetch the client
		User client = userRepository.findById(clientId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "clientId", clientId));

		// Create new booking and set basic details
		Booking booking = new Booking();
		booking.setBookingDate(bookingDTO.getBookingDate());
		booking.setClient(client);
		booking.setStatus(BookingStatus.NEW);

		// Process booking items if provided
		if (bookingDTO.getBookingItems() != null) {
			for (BookingItemDTO itemDTO : bookingDTO.getBookingItems()) {
				BookingItem bookingItem = new BookingItem();
				TempleDetails temple = templeDetailsRepository.findById(itemDTO.getTempleId()).orElseThrow(
						() -> new ResourceNotFoundException("TempleDetails", "templeId", itemDTO.getTempleId()));
				bookingItem.setTemple(temple);
				bookingItem.setPoojaSelected(itemDTO.isPoojaSelected());
				bookingItem.setRudrabhisekhSelected(itemDTO.isRudrabhisekhSelected());
				bookingItem.setPrasadSelected(itemDTO.isPrasadSelected());
				bookingItem.setBooking(booking);
				booking.getBookingItems().add(bookingItem);
			}
		}

		// Save the booking
		Booking savedBooking = bookingRepository.save(booking);
		logger.info("Booking created with id: {}", savedBooking.getBookingId());

		// If the client's status is "NOT_SCHEDULED", update it to "UPCOMING"
		if ("NOT_SCHEDULED".equalsIgnoreCase(client.getStatus())) {
			client.setStatus("UPCOMING");
			userRepository.save(client);
			logger.info("Updated client status to UPCOMING for userId: {}", client.getUserId());
		}

		return modelMapper.map(savedBooking, BookingDTO.class);
	}

	@Override
	public List<BookingDTO> getBookingsByClientId(Long clientId) {
		List<Booking> bookings = bookingRepository.findByClient_UserId(clientId);
		return bookings.stream().map(booking -> modelMapper.map(booking, BookingDTO.class))
				.collect(Collectors.toList());
	}

	@Override
	public BookingDTO updateBooking(Long bookingId, BookingDTO bookingDTO) {
		logger.info("Updating booking with id: {}", bookingId);
		Booking existingBooking = bookingRepository.findById(bookingId)
				.orElseThrow(() -> new ResourceNotFoundException("Booking", "bookingId", bookingId));
		existingBooking.setBookingDate(bookingDTO.getBookingDate());
		// Optionally, update other fields
		Booking updatedBooking = bookingRepository.save(existingBooking);
		logger.info("Booking updated with id: {}", updatedBooking.getBookingId());
		return modelMapper.map(updatedBooking, BookingDTO.class);
	}

	// Endpoint for top level who assign the mid level
	@Override
	public BookingDTO updateBookingByTopLevel(Long bookingId, BookingDTO bookingDTO, Long topLevelId) {
		logger.info("Top Level {} updating booking with id: {}", topLevelId, bookingId);

		// Fetch the booking by its ID.
		Booking booking = bookingRepository.findById(bookingId)
				.orElseThrow(() -> new ResourceNotFoundException("Booking", "bookingId", bookingId));

		// Update booking date if provided.
		if (bookingDTO.getBookingDate() != null) {
			booking.setBookingDate(bookingDTO.getBookingDate());
		}

		// Set booking status to UPDATE_BY_TOP_LEVEL.
		booking.setStatus(BookingStatus.UPDATE_BY_TOP_LEVEL);

		// Retrieve and set the top level user.
		User topLevel = userRepository.findById(topLevelId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "topLevelId", topLevelId));
		booking.setTopLevel(topLevel);

		// Ensure that updatedByMidLevel id is provided.
		if (bookingDTO.getUpdatedByMidLevel() == null) {
			throw new IllegalArgumentException("The updatedByMidLevel id must be provided.");
		} else {
			User midLevel = userRepository.findById(bookingDTO.getUpdatedByMidLevel()).orElseThrow(
					() -> new ResourceNotFoundException("User", "midLevelId", bookingDTO.getUpdatedByMidLevel()));
			booking.setMidLevel(midLevel);
		}

		Booking updatedBooking = bookingRepository.save(booking);
		logger.info("Booking updated by top level with id: {}", updatedBooking.getBookingId());
		return modelMapper.map(updatedBooking, BookingDTO.class);
	}

	// Endpoint for mid level who assign the team leader
	@Override
	public BookingDTO updateBookingByMidLevel(Long bookingId, BookingDTO bookingDTO, Long midLevelId) {
		logger.info("Mid Level {} updating booking with id: {}", midLevelId, bookingId);
		Booking booking = bookingRepository.findById(bookingId)
				.orElseThrow(() -> new ResourceNotFoundException("Booking", "bookingId", bookingId));

		// Update booking date (and any other fields as required)
//		booking.setBookingDate(bookingDTO.getBookingDate());
		// Update booking date if provided.
		if (bookingDTO.getBookingDate() != null) {
			booking.setBookingDate(bookingDTO.getBookingDate());
		}

		// Set booking status to UPDATE_BY_TOP_LEVEL
		booking.setStatus(BookingStatus.UPDATE_BY_MID_LEVEL);

		// Retrieve team leader and set updatedBy field
		User midLevel = userRepository.findById(midLevelId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "midLevelId", midLevelId));
		booking.setMidLevel(midLevel);

		// Update team leader user if provided in the DTO
		if (bookingDTO.getUpdatedByTeamLeader() != null) {
			User teamLeader = userRepository.findById(bookingDTO.getUpdatedByTeamLeader()).orElseThrow(
					() -> new ResourceNotFoundException("User", "teamLeaderId", bookingDTO.getUpdatedByTeamLeader()));
			booking.setUpdatedBy(teamLeader);
		}

		Booking updatedBooking = bookingRepository.save(booking);
		logger.info("Booking updated by top level with id: {}", updatedBooking.getBookingId());
		return modelMapper.map(updatedBooking, BookingDTO.class);
	}

	// Endpoint for team leader who assign the support service and temple admin
	@Override
	public BookingDTO updateBookingDetailsByTeamLeader(Long bookingId, BookingDTO bookingDTO, Long teamLeaderId) {
		logger.info("Mid Level {} updating booking with id: {}", teamLeaderId, bookingId);
		Booking booking = bookingRepository.findById(bookingId)
				.orElseThrow(() -> new ResourceNotFoundException("Booking", "bookingId", bookingId));

		// Update booking date (and any other fields as required)
//		booking.setBookingDate(bookingDTO.getBookingDate());
		// Update booking date if provided.
		if (bookingDTO.getBookingDate() != null) {
			booking.setBookingDate(bookingDTO.getBookingDate());
		}

		// Set booking status to UPDATE_BY_TOP_LEVEL
		booking.setStatus(BookingStatus.UPDATED_BY_TEAM_LEADER);

		// Retrieve team leader and set updatedBy field
		User teamLeader = userRepository.findById(teamLeaderId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "teamLeaderId", teamLeaderId));
		booking.setUpdatedBy(teamLeader);

		// Update support service user if provided in the DTO
		if (bookingDTO.getSupportServiceId() != null) {
			User support = userRepository.findById(bookingDTO.getSupportServiceId()).orElseThrow(
					() -> new ResourceNotFoundException("User", "supportId", bookingDTO.getSupportServiceId()));
			booking.setSupportService(support);
		}
		// Update temple admin user if provided in the DTO
		if (bookingDTO.getTempleAdminId() != null) {
			User templeAdmin = userRepository.findById(bookingDTO.getTempleAdminId()).orElseThrow(
					() -> new ResourceNotFoundException("User", "templeAdminId", bookingDTO.getTempleAdminId()));
			booking.setTempleAdmin(templeAdmin);
		}

		Booking updatedBooking = bookingRepository.save(booking);
		logger.info("Booking updated by top level with id: {}", updatedBooking.getBookingId());
		return modelMapper.map(updatedBooking, BookingDTO.class);
	}

	// Endpoint for temple admin who assign the guide
	@Override
	public BookingDTO assignGuideToBooking(Long bookingId, Long guideId, Long templeAdminId) {
		logger.info("Temple Admin {} assigning guide {} to booking {}", templeAdminId, guideId, bookingId);

		// Fetch the booking
		Booking booking = bookingRepository.findById(bookingId)
				.orElseThrow(() -> new ResourceNotFoundException("Booking", "bookingId", bookingId));

		// Fetch the guide
		User guide = userRepository.findById(guideId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "guideId", guideId));

		// Assign the guide to the booking and update booking status
		booking.setAssignedGuide(guide);
		booking.setStatus(BookingStatus.ASSIGNED_BY_TEMPLE_ADMIN);
		Booking updatedBooking = bookingRepository.save(booking);

		// Update the client's status from "UPCOMING" to "PROGRESS"
		User client = booking.getClient();
		if (client != null && "UPCOMING".equalsIgnoreCase(client.getStatus())) {
			client.setStatus("PROGRESS");
			userRepository.save(client);
			logger.info("Updated client status to PROGRESS for clientId: {}", client.getUserId());
		}

		logger.info("Guide assigned to booking {} successfully", updatedBooking.getBookingId());
		return modelMapper.map(updatedBooking, BookingDTO.class);
	}

	@Override
	public BookingDTO getBookingById(Long bookingId) {
		Booking booking = bookingRepository.findById(bookingId)
				.orElseThrow(() -> new ResourceNotFoundException("Booking", "bookingId", bookingId));

		// First, map the basic fields
		BookingDTO bookingDTO = modelMapper.map(booking, BookingDTO.class);

		// Now, manually map the fields with mismatched names/types
		if (booking.getTopLevel() != null) {
			bookingDTO.setUpdatedByTopLevel(modelMapper.map(booking.getTopLevel(), UserDTO.class));
		}
		if (booking.getMidLevel() != null) {
			bookingDTO.setUpdatedByMidLevel(booking.getMidLevel().getUserId());
		}
		if (booking.getUpdatedBy() != null) {
			bookingDTO.setUpdatedByTeamLeader(booking.getUpdatedBy().getUserId());
		}

		// Similarly, if you want to map supportService or templeAdmin, do it here

		return bookingDTO;
	}

	@Override
	public List<BookingDTO> getAllBookings() {
		List<Booking> bookings = bookingRepository.findAll();
		return bookings.stream().map(booking -> {
			BookingDTO dto = modelMapper.map(booking, BookingDTO.class);
			dto.setBookingItems(booking.getBookingItems().stream().map(item -> {
				BookingItemDTO itemDto = modelMapper.map(item, BookingItemDTO.class);
				// Map full temple details
				itemDto.setTemple(modelMapper.map(item.getTemple(), TempleDetailsDTO.class));
				return itemDto;
			}).collect(Collectors.toList()));
			return dto;
		}).collect(Collectors.toList());
	}

	@Override
	public UserDTO getAssignedGuideDetails(Long bookingId) {
		logger.info("Fetching assigned guide details for bookingId: {}", bookingId);
		Booking booking = bookingRepository.findById(bookingId)
				.orElseThrow(() -> new ResourceNotFoundException("Booking", "bookingId", bookingId));
		User guide = booking.getAssignedGuide();
		if (guide == null) {
			throw new ResourceNotFoundException("Guide", "bookingId", bookingId);
		}
		return modelMapper.map(guide, UserDTO.class);
	}

	@Override
	public UserDTO getAssignedSupportServiceDetails(Long bookingId) {
		logger.info("Fetching assigned support service details for bookingId: {}", bookingId);
		Booking booking = bookingRepository.findById(bookingId)
				.orElseThrow(() -> new ResourceNotFoundException("Booking", "bookingId", bookingId));
		User supportService = booking.getSupportService();
		if (supportService == null) {
			throw new ResourceNotFoundException("Support Service", "bookingId", bookingId);
		}
		return modelMapper.map(supportService, UserDTO.class);
	}

	@Override
	public UserDTO getTeamLeaderDetails(Long bookingId) {
		logger.info("Fetching team leader details for bookingId: {}", bookingId);
		Booking booking = bookingRepository.findById(bookingId)
				.orElseThrow(() -> new ResourceNotFoundException("Booking", "bookingId", bookingId));
		User teamLeader = booking.getUpdatedBy();
		if (teamLeader == null) {
			throw new ResourceNotFoundException("Team Leader", "bookingId", bookingId);
		}
		return modelMapper.map(teamLeader, UserDTO.class);
	}

	@Override
	public List<BookingDTO> getNewBookings() {
		logger.info("Fetching bookings with status NEW");
		List<Booking> bookings = bookingRepository.findByStatus(BookingStatus.NEW);
		return bookings.stream().map(booking -> modelMapper.map(booking, BookingDTO.class))
				.collect(Collectors.toList());
	}

	@Override
	public List<BookingDTO> getUpdatedByTeamLeaderBookings() {
		logger.info("Fetching bookings with status UPDATED_BY_TEAM_LEADER");
		List<Booking> bookings = bookingRepository.findByStatus(BookingStatus.UPDATED_BY_TEAM_LEADER);
		return bookings.stream().map(booking -> modelMapper.map(booking, BookingDTO.class))
				.collect(Collectors.toList());
	}

	@Override
	public List<BookingDTO> getAssignedByTempleAdminBookings() {
		logger.info("Fetching bookings with status ASSIGNED_BY_TEMPLE_ADMIN");
		List<Booking> bookings = bookingRepository.findByStatus(BookingStatus.ASSIGNED_BY_TEMPLE_ADMIN);
		return bookings.stream().map(booking -> modelMapper.map(booking, BookingDTO.class))
				.collect(Collectors.toList());
	}

	@Override
	public List<BookingDTO> getUpdateByTopLevelBookings() {
		logger.info("Fetching bookings with status UPDATE_BY_TOP_LEVEL");
		List<Booking> bookings = bookingRepository.findByStatus(BookingStatus.UPDATE_BY_TOP_LEVEL);
		return bookings.stream().map(booking -> modelMapper.map(booking, BookingDTO.class))
				.collect(Collectors.toList());
	}

	@Override
	public List<BookingDTO> getUpdateByMidLevelBookings() {
		logger.info("Fetching bookings with status UPDATE_BY_MID_LEVEL");
		List<Booking> bookings = bookingRepository.findByStatus(BookingStatus.UPDATE_BY_MID_LEVEL);
		return bookings.stream().map(booking -> modelMapper.map(booking, BookingDTO.class))
				.collect(Collectors.toList());
	}

	@Override
	public List<BookingDTO> getBookingsByMidLevelId(Long midLevelId) {
		logger.info("Fetching bookings with midLevel id: {}", midLevelId);
		List<Booking> bookings = bookingRepository.findByMidLevel_UserId(midLevelId);
		return bookings.stream().map(booking -> modelMapper.map(booking, BookingDTO.class))
				.collect(Collectors.toList());
	}

	@Override
	public List<BookingDTO> getBookingsByteamLeaderId(Long teamLeaderId) {
		logger.info("Fetching bookings with team leader id: {}", teamLeaderId);
		List<Booking> bookings = bookingRepository.findByUpdatedBy_UserId(teamLeaderId);
		return bookings.stream().map(booking -> modelMapper.map(booking, BookingDTO.class))
				.collect(Collectors.toList());
	}

	@Override
	public List<BookingDTO> getBookingsBySupportId(Long supportId) {
		logger.info("Fetching bookings with support id: {}", supportId);
		List<Booking> bookings = bookingRepository.findBySupportService_UserId(supportId);
		return bookings.stream().map(booking -> modelMapper.map(booking, BookingDTO.class))
				.collect(Collectors.toList());
	}

	@Override
	public List<BookingDTO> getBookingsByTempleAdminId(Long templeAdminId) {
		logger.info("Fetching bookings with temple admin id: {}", templeAdminId);
		List<Booking> bookings = bookingRepository.findByTempleAdmin_UserId(templeAdminId);
		return bookings.stream().map(booking -> modelMapper.map(booking, BookingDTO.class))
				.collect(Collectors.toList());
	}

	@Override
	public List<BookingDTO> getBookingsByGuideId(Long guideId) {
		logger.info("Fetching bookings with guide id: {}", guideId);
		List<Booking> bookings = bookingRepository.findByAssignedGuide_UserId(guideId);
		return bookings.stream().map(booking -> modelMapper.map(booking, BookingDTO.class))
				.collect(Collectors.toList());
	}

	@Override
	public List<BookingAssociationDTO> getAssociationsByClientId(Long clientId) {
		logger.info("Fetching booking associations for clientId: {}", clientId);
		List<Booking> bookings = bookingRepository.findByClient_UserId(clientId);
		if (bookings.isEmpty()) {
			throw new ResourceNotFoundException("Booking", "clientId", clientId);
		}
		return bookings.stream().map(booking -> {
			BookingAssociationDTO dto = new BookingAssociationDTO();
			dto.setBookingId(booking.getBookingId());
			// Map associated guide if available
			if (booking.getAssignedGuide() != null) {
				dto.setAssignedGuide(modelMapper.map(booking.getAssignedGuide(), UserDTO.class));
			}
			// Map associated support service if available
			if (booking.getSupportService() != null) {
				dto.setSupportService(modelMapper.map(booking.getSupportService(), UserDTO.class));
			}
			// Map associated temple admin if available
			if (booking.getTempleAdmin() != null) {
				dto.setTempleAdmin(modelMapper.map(booking.getTempleAdmin(), UserDTO.class));
			}
			return dto;
		}).collect(Collectors.toList());
	}

	@Override
	public BookingDTO completeBooking(Long bookingId, Long guideId) {
		logger.info("Guide {} completing booking {}", guideId, bookingId);

		// Fetch the booking by its ID
		Booking booking = bookingRepository.findById(bookingId)
				.orElseThrow(() -> new ResourceNotFoundException("Booking", "bookingId", bookingId));

		// Optionally validate that the assigned guide matches the guideId provided
		if (booking.getAssignedGuide() == null || booking.getAssignedGuide().getUserId() != guideId) {
			throw new ResourceNotFoundException("Guide", "guideId", guideId);
		}

		// Update booking status to COMPLETED
		booking.setStatus(BookingStatus.COMPLETED);
		Booking updatedBooking = bookingRepository.save(booking);

		// Update client's status to COMPLETED
		User client = booking.getClient();
		if (client != null) {
			client.setStatus("COMPLETED");
			userRepository.save(client);
			logger.info("Updated client status to COMPLETED for clientId: {}", client.getUserId());
		}

		logger.info("Booking {} completed by guide {}", bookingId, guideId);
		return modelMapper.map(updatedBooking, BookingDTO.class);
	}

	@Override
	public UserDTO getAssignedTempleAdminByBookingId(Long bookingId) {
		// Fetch the booking by its ID
		Booking booking = bookingRepository.findById(bookingId)
				.orElseThrow(() -> new ResourceNotFoundException("Booking", "bookingId", bookingId));

		// Check if there are booking items available
		if (booking.getBookingItems() == null || booking.getBookingItems().isEmpty()) {
			throw new ResourceNotFoundException("BookingItems", "bookingId", bookingId);
		}

		// Get the temple from the first booking item
		TempleDetails temple = booking.getBookingItems().get(0).getTemple();
		if (temple == null || temple.getTempleAdmin() == null) {
			throw new ResourceNotFoundException("TempleAdmin", "bookingId", bookingId);
		}

		// Map and return the temple admin details
		return modelMapper.map(temple.getTempleAdmin(), UserDTO.class);
	}

	@Override
	public BookingDTO confirmBookingByTopLevel(Long bookingId, Long topLevelId) {
		// Fetch the booking by its ID
		Booking booking = bookingRepository.findById(bookingId)
				.orElseThrow(() -> new ResourceNotFoundException("Booking", "bookingId", bookingId));

		// Verify that the current status is ASSIGNED_BY_TEMPLE_ADMIN
		if (!BookingStatus.ASSIGNED_BY_TEMPLE_ADMIN.equals(booking.getStatus())) {
			throw new IllegalStateException(
					"Booking cannot be confirmed unless its status is ASSIGNED_BY_TEMPLE_ADMIN.");
		}

		// Retrieve the top-level user and set it to the booking (if needed)
		User topLevel = userRepository.findById(topLevelId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "topLevelId", topLevelId));
		booking.setTopLevel(topLevel);

		// Update the booking status to CONFIRM
		booking.setStatus(BookingStatus.CONFIRM);

		// Save the updated booking
		Booking updatedBooking = bookingRepository.save(booking);

		logger.info("Booking {} confirmed by top level user {}", updatedBooking.getBookingId(), topLevelId);
		return modelMapper.map(updatedBooking, BookingDTO.class);
	}

	@Override
	public List<BookingDTO> getBookingsBySupportServiceId(Long supportServiceId) {
		List<Booking> bookings = bookingRepository.findBySupportService_UserId(supportServiceId);
		return bookings.stream().map(booking -> modelMapper.map(booking, BookingDTO.class))
				.collect(Collectors.toList());
	}

}
