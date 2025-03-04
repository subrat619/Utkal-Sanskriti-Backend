package com.cyfrifpro.serviceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.cyfrifpro.DTO.FeedbackDTO;
import com.cyfrifpro.Exception.ResourceNotFoundException;
import com.cyfrifpro.model.Booking;
import com.cyfrifpro.model.Feedback;
import com.cyfrifpro.repository.BookingRepository;
import com.cyfrifpro.repository.FeedbackRepository;
import com.cyfrifpro.service.FeedbackService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class FeedbackServiceImpl implements FeedbackService {

	private static final Logger logger = LoggerFactory.getLogger(FeedbackServiceImpl.class);

	private final FeedbackRepository feedbackRepository;
	private final BookingRepository bookingRepository;
	private final ModelMapper modelMapper;

	public FeedbackServiceImpl(FeedbackRepository feedbackRepository, BookingRepository bookingRepository,
			ModelMapper modelMapper) {
		this.feedbackRepository = feedbackRepository;
		this.bookingRepository = bookingRepository;
		this.modelMapper = modelMapper;
	}

	@Override
	public FeedbackDTO createFeedback(FeedbackDTO feedbackDTO) {
		logger.info("Creating feedback for bookingId: {}", feedbackDTO.getBookingId());
		Booking booking = bookingRepository.findById(feedbackDTO.getBookingId())
				.orElseThrow(() -> new ResourceNotFoundException("Booking", "bookingId", feedbackDTO.getBookingId()));
		Feedback feedback = new Feedback();
		feedback.setBooking(booking);
		feedback.setRating(feedbackDTO.getRating());
		feedback.setComment(feedbackDTO.getComment());
		Feedback saved = feedbackRepository.save(feedback);
		logger.info("Feedback created with id: {}", saved.getFeedbackId());
		return modelMapper.map(saved, FeedbackDTO.class);
	}

	@Override
	public FeedbackDTO updateFeedback(Long feedbackId, FeedbackDTO feedbackDTO) {
		logger.info("Updating feedback with id: {}", feedbackId);
		Feedback feedback = feedbackRepository.findById(feedbackId)
				.orElseThrow(() -> new ResourceNotFoundException("Feedback", "feedbackId", feedbackId));
		feedback.setRating(feedbackDTO.getRating());
		feedback.setComment(feedbackDTO.getComment());
		Feedback updated = feedbackRepository.save(feedback);
		logger.info("Feedback updated with id: {}", updated.getFeedbackId());
		return modelMapper.map(updated, FeedbackDTO.class);
	}

	@Override
	public List<FeedbackDTO> getFeedbackByBookingId(Long bookingId) {
		List<Feedback> feedbackList = feedbackRepository.findByBooking_BookingId(bookingId);
		return feedbackList.stream().map(feedback -> modelMapper.map(feedback, FeedbackDTO.class))
				.collect(Collectors.toList());
	}

	@Override
	public List<FeedbackDTO> getAllFeedback() {
		List<Feedback> feedbackList = feedbackRepository.findAll();
		return feedbackList.stream().map(feedback -> modelMapper.map(feedback, FeedbackDTO.class))
				.collect(Collectors.toList());
	}
}
