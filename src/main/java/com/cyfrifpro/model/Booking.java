package com.cyfrifpro.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "bookings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Booking {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long bookingId;

	// The client who made the booking
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "client_id", nullable = false)
	private User client;

	@NotNull(message = "Booking date is required")
	private LocalDateTime bookingDate;

	// One booking can have multiple booking items
	@OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<BookingItem> bookingItems = new ArrayList<>();

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 50)
	private BookingStatus status = BookingStatus.NEW;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "guide_id") // The assigned guide id
	private User assignedGuide;

	@ManyToOne(fetch = FetchType.LAZY) //
	@JoinColumn(name = "updated_by_team_leader_id") // Team leader id who assign the guide and support service
	private User updatedBy; //

	@ManyToOne(fetch = FetchType.LAZY) //
	@JoinColumn(name = "support_service_id") // The assigned support service
	private User supportService; //

	@ManyToOne(fetch = FetchType.LAZY) //
	@JoinColumn(name = "updated_by_mid_level_id") // Mid level id who assign the team leader
	private User midLevel; //

	@ManyToOne(fetch = FetchType.LAZY) //
	@JoinColumn(name = "updated_by_top_level_id") // Top level id who assign the mid level
	private User topLevel; //

	@ManyToOne(fetch = FetchType.LAZY) //
	@JoinColumn(name = "updated_by_temple_admin_id") // Temple admin id who assign the guide
	private User templeAdmin; //

	@Column(name = "created_at", updatable = false)
	private LocalDateTime createdAt;

//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "destination_id")
//	private Destination destination;

	@PrePersist
	protected void onCreate() {
		this.createdAt = LocalDateTime.now();
		if (this.bookingDate == null) {
			this.bookingDate = LocalDateTime.now();
		}
	}

}
