package com.cyfrifpro.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cyfrifpro.DTO.EventDTO;
import com.cyfrifpro.service.EventService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/events")
//@CrossOrigin(origins = "http://127.0.0.1:5500/")
public class EventController {

	private final EventService eventService;

	public EventController(EventService eventService) {
		this.eventService = eventService;
	}

	@PreAuthorize("hasRole('TOP_LEVEL')")
	@Operation(summary = "Method to create an event by TOP_LEVEL")
	@PostMapping("/add")
	public ResponseEntity<EventDTO> createEvent(@Valid @RequestBody EventDTO eventDTO, @RequestParam Long topLevelId) {
		EventDTO createdEvent = eventService.createEvent(eventDTO, topLevelId);
		return ResponseEntity.ok(createdEvent);
	}

	@Operation(summary = "Method to update an event")
	@PutMapping("/{eventId}")
	public ResponseEntity<EventDTO> updateEvent(@PathVariable Long eventId, @Valid @RequestBody EventDTO eventDTO) {
		EventDTO updatedEvent = eventService.updateEvent(eventId, eventDTO);
		return ResponseEntity.ok(updatedEvent);
	}

	@Operation(summary = "Method to get an event details by event id")
	@GetMapping("/{eventId}")
	public ResponseEntity<EventDTO> getEventById(@PathVariable Long eventId) {
		EventDTO eventDTO = eventService.getEventById(eventId);
		return ResponseEntity.ok(eventDTO);
	}

	@Operation(summary = "Method to get all events")
	@GetMapping
	public ResponseEntity<List<EventDTO>> getAllEvents() {
		List<EventDTO> events = eventService.getAllEvents();
		return ResponseEntity.ok(events);
	}

	@Operation(summary = "Method to delete an event by event id")
	@DeleteMapping("/{eventId}")
	public ResponseEntity<Void> deleteEvent(@PathVariable Long eventId) {
		eventService.deleteEvent(eventId);
		return ResponseEntity.noContent().build();
	}

	@Operation(summary = "Method to fetch an upcoming event")
	@GetMapping("/upcoming")
	public ResponseEntity<List<EventDTO>> getUpcomingEvents() {
		List<EventDTO> upcomingEvents = eventService.getUpcomingEvents();
		return ResponseEntity.ok(upcomingEvents);
	}

	// Endpoint for creating an event with image using multipart/form-data
//	@PreAuthorize("hasRole('TOP_LEVEL')")
	@Operation(summary = "Method to create an event with event image by TOP_LEVEL id")
	@PostMapping(value = "/add-with-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<EventDTO> createEventWithImage(@RequestParam("eventName") String eventName,
			@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate,
			@RequestParam("description") String description, @RequestParam("location") String location,
			@RequestParam("eventImage") MultipartFile eventImage, @RequestParam("topLevelId") Long topLevelId) {

		EventDTO dto = new EventDTO();
		dto.setEventName(eventName);
		dto.setStartDate(LocalDateTime.parse(startDate));
		dto.setEndDate(LocalDateTime.parse(endDate));
		dto.setDescription(description);
		dto.setLocation(location);

		EventDTO created = eventService.createEventWithImage(dto, eventImage, topLevelId);
		return ResponseEntity.ok(created);
	}
}
