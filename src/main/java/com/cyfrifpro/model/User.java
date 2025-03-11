package com.cyfrifpro.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long userId;

	@Column(nullable = false, length = 50)
	@NotBlank(message = "First name is mandatory")
	@Size(max = 50, message = "First name must be at most 50 characters")
	private String firstName;

	@Column(nullable = false, length = 50)
	@NotBlank(message = "Last name is mandatory")
	@Size(max = 50, message = "Last name must be at most 50 characters")
	private String lastName;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private Role role;

	@Column(nullable = false, unique = true)
	@Email(message = "Email should be valid")
	@NotBlank(message = "Email is mandatory")
	private String email;

	@Column(nullable = false, length = 10)
	@NotBlank(message = "Contact number is mandatory")
	@Pattern(regexp = "^\\+?[0-9]{10,10}$", message = "Contact number must be valid")
	private String contactNumber;

	@Column(nullable = false)
	@NotBlank(message = "Password is mandatory")
	@Size(min = 4, message = "Password must be at least 4 characters long")
	private String password;

	@ManyToOne
	@JoinColumn(name = "created_by_id", updatable = false) // This will store the creator's user ID in the database
	@JsonIgnore // This will prevent the fetching of parent roles
	private User createdBy;

	@Column(name = "created_at", updatable = false)
	private LocalDateTime createdAt;

	@Column(nullable = false)
	private String status;

	@PrePersist
	protected void onCreate() {
		this.createdAt = LocalDateTime.now();
		if (this.status == null) {
			if (this.role != null && this.role.equals(Role.CLIENT)) {
				this.status = "NOT_SCHEDULED";
			} else {
				// For other roles, set an alternative default status
				this.status = "ACTIVE";
			}
		}
	}

}
