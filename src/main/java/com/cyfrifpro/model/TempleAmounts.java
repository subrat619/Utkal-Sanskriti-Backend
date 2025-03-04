package com.cyfrifpro.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "temple_amounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TempleAmounts {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne // One-to-one relation with temple details
	@JoinColumn(name = "temple_id", nullable = false)
	private TempleDetails temple;

	@DecimalMin(value = "0.0", inclusive = true, message = "Pooja fee must be non-negative")
	@Column(precision = 10, scale = 2)
	private BigDecimal poojaFee;

	@DecimalMin(value = "0.0", inclusive = true, message = "Rudrabhisekh fee must be non-negative")
	@Column(precision = 10, scale = 2)
	private BigDecimal rudrabhisekhFee;

	@DecimalMin(value = "0.0", inclusive = true, message = "Prasad fee must be non-negative")
	@Column(precision = 10, scale = 2)
	private BigDecimal prasadFee;

	@Column(name = "created_at", updatable = false)
	private LocalDateTime createdAt;

	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	@PrePersist
	protected void onCreate() {
		this.createdAt = LocalDateTime.now();
	}

	@PreUpdate
	protected void onUpdate() {
		this.updatedAt = LocalDateTime.now();
	}

}
