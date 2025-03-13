package com.cyfrifpro.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cyfrifpro.model.Destination;

@Repository
public interface DestinationRepository extends JpaRepository<Destination, Long> {

	// Custom JPQL query to count bookings for each destination.
	// This query assumes that your Booking entity has a relationship with
	// Destination.
//	@Query("SELECT new com.cyfrifpro.DTO.PopularDestinationDTO(d.id, d.name, d.location, d.imageUrl, COUNT(b)) "
//			+ "FROM Destination d LEFT JOIN d.bookings b " + "GROUP BY d.id, d.name, d.location, d.imageUrl "
//			+ "ORDER BY COUNT(b) DESC")
//	List<PopularDestinationDTO> findPopularDestinations();

}
