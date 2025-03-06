package com.cyfrifpro.DTO;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingDTO {

	private Long bookingId;

//	@NotNull(message = "Booking date is required")
	private LocalDateTime bookingDate;

	private UserDTO client;

	private List<BookingItemDTO> bookingItems;

	private String bookingStatus;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "updated_by_team_leader_id")
	private UserDTO updatedByTopLevel;

	private Long updatedByMidLevel;

	private Long updatedByTeamLeader;

	private Long supportServiceId;

	private Long templeAdminId;

	private Long assignedGuideId;

}