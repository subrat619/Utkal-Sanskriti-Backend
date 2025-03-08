package com.cyfrifpro.service;

import java.util.List;

import com.cyfrifpro.DTO.ComplaintDTO;

public interface ComplaintService {
	ComplaintDTO postComplaint(ComplaintDTO complaintDTO);

	List<ComplaintDTO> getAllComplaints();

	List<ComplaintDTO> getComplaintsByUserId(Long userId);
}
