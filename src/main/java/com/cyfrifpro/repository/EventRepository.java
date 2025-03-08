package com.cyfrifpro.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cyfrifpro.model.Event;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

	// Fetch upcoming events (events with a startDate in the future)
	@Query("SELECT e FROM Event e WHERE e.startDate > CURRENT_TIMESTAMP ORDER BY e.startDate ASC")
	List<Event> findUpcomingEvents();
}
