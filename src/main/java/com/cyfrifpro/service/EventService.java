package com.cyfrifpro.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.cyfrifpro.DTO.EventDTO;

public interface EventService {
	EventDTO createEvent(EventDTO eventDTO, Long topLevelId);

	EventDTO updateEvent(Long eventId, EventDTO eventDTO);

	EventDTO getEventById(Long eventId);

	List<EventDTO> getAllEvents();

	void deleteEvent(Long eventId);

	List<EventDTO> getUpcomingEvents();

	// Create event with image (using MultipartFile)
	EventDTO createEventWithImage(EventDTO eventDTO, MultipartFile eventImage, Long topLevelId);
}
