package com.cyfrifpro.model;

import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "destinations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Destination {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "Destination name is mandatory")
	@Column(nullable = false)
	private String name;

	@NotBlank(message = "Location is mandatory")
	@Column(nullable = false)
	private String location;

	@Column(length = 1000)
	private String description;

	@Column(name = "image_url")
	private String imageUrl;

	// Field to store binary image data
	@Lob
	@Column(name = "image_data", columnDefinition = "LONGBLOB")
	private byte[] imageData;

	@Column(name = "created_at", updatable = false)
	private LocalDateTime createdAt;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "district_id", nullable = false)
	private District district;

//	@OneToMany(mappedBy = "destination")
//	private List<Booking> bookings;

	@PrePersist
	protected void onCreate() {
		this.createdAt = LocalDateTime.now();
	}
}
