package com.cyfrifpro.serviceImpl;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cyfrifpro.DTO.EventDTO;
import com.cyfrifpro.Exception.ResourceNotFoundException;
import com.cyfrifpro.model.Event;
import com.cyfrifpro.model.User;
import com.cyfrifpro.repository.EventRepository;
import com.cyfrifpro.repository.UserRepository;
import com.cyfrifpro.service.EventService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class EventServiceImpl implements EventService {

	private static final Logger logger = LoggerFactory.getLogger(EventServiceImpl.class);
	private final EventRepository eventRepository;
	private final UserRepository userRepository; // Inject UserRepository
	private final ModelMapper modelMapper;

	public EventServiceImpl(EventRepository eventRepository, UserRepository userRepository, ModelMapper modelMapper) {
		this.eventRepository = eventRepository;
		this.userRepository = userRepository;
		this.modelMapper = modelMapper;
	}

	@Override
	public EventDTO createEvent(EventDTO eventDTO, Long topLevelId) {
		// Map DTO to entity
		Event event = modelMapper.map(eventDTO, Event.class);
		// Fetch the top-level user and set as postedBy
		User topLevelUser = userRepository.findById(topLevelId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "topLevelId", topLevelId));
		event.setPostedBy(topLevelUser);

		Event savedEvent = eventRepository.save(event);
		EventDTO savedDto = modelMapper.map(savedEvent, EventDTO.class);
		if (savedEvent.getEventImageData() != null) {
			savedDto.setEventImageData(java.util.Base64.getEncoder().encodeToString(savedEvent.getEventImageData()));
		}
		return savedDto;
	}

	@Override
	public EventDTO updateEvent(Long eventId, EventDTO eventDTO) {
		Event event = eventRepository.findById(eventId).orElseThrow(() -> new RuntimeException("Event not found"));
		event.setEventName(eventDTO.getEventName());
		event.setStartDate(eventDTO.getStartDate());
		event.setEndDate(eventDTO.getEndDate());
		event.setDescription(eventDTO.getDescription());
		event.setLocation(eventDTO.getLocation());
		Event updatedEvent = eventRepository.save(event);
		return modelMapper.map(updatedEvent, EventDTO.class);
	}

	@Override
	public EventDTO getEventById(Long eventId) {
		Event event = eventRepository.findById(eventId).orElseThrow(() -> new RuntimeException("Event not found"));
		EventDTO dto = modelMapper.map(event, EventDTO.class);
		if (event.getEventImageData() != null) {
			dto.setEventImageData(java.util.Base64.getEncoder().encodeToString(event.getEventImageData()));
		}
		return dto;
	}

	@Override
	public List<EventDTO> getAllEvents() {
		return eventRepository.findAll().stream().map(event -> {
			EventDTO dto = modelMapper.map(event, EventDTO.class);
			if (event.getEventImageData() != null) {
				dto.setEventImageData(java.util.Base64.getEncoder().encodeToString(event.getEventImageData()));
			}
			return dto;
		}).collect(Collectors.toList());
	}

	@Override
	public void deleteEvent(Long eventId) {
		eventRepository.deleteById(eventId);
	}

	@Override
	public List<EventDTO> getUpcomingEvents() {
		logger.info("Fetching upcoming events");
		List<Event> events = eventRepository.findUpcomingEvents();
		return events.stream().map(event -> {
			EventDTO dto = modelMapper.map(event, EventDTO.class);
			if (event.getEventImageData() != null) {
				dto.setEventImageData(java.util.Base64.getEncoder().encodeToString(event.getEventImageData()));
			}
			return dto;
		}).collect(Collectors.toList());
	}

	@Override
	public EventDTO createEventWithImage(EventDTO eventDTO, MultipartFile eventImage, Long topLevelId) {
		// Map DTO to entity
		Event event = modelMapper.map(eventDTO, Event.class);

		// Fetch the top-level user
		User topLevelUser = userRepository.findById(topLevelId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "topLevelId", topLevelId));
		// Set the top-level user as the poster of the event
		event.setPostedBy(topLevelUser);

		try {
			event.setEventImageData(eventImage.getBytes());
		} catch (IOException e) {
			logger.error("Error reading event image file", e);
			throw new RuntimeException("Could not read event image file", e);
		}
		Event savedEvent = eventRepository.save(event);
		EventDTO savedDto = modelMapper.map(savedEvent, EventDTO.class);
		if (savedEvent.getEventImageData() != null) {
			savedDto.setEventImageData(java.util.Base64.getEncoder().encodeToString(savedEvent.getEventImageData()));
		}
		return savedDto;
	}

}
