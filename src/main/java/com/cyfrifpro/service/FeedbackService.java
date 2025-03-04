package com.cyfrifpro.service;

import java.util.List;

import com.cyfrifpro.DTO.FeedbackDTO;

public interface FeedbackService {
	FeedbackDTO createFeedback(FeedbackDTO feedbackDTO);

	FeedbackDTO updateFeedback(Long feedbackId, FeedbackDTO feedbackDTO);

	List<FeedbackDTO> getFeedbackByBookingId(Long bookingId);

	List<FeedbackDTO> getAllFeedback();
}
