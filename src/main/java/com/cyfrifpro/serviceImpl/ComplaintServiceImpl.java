package com.cyfrifpro.serviceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.cyfrifpro.DTO.ComplaintDTO;
import com.cyfrifpro.Exception.ResourceNotFoundException;
import com.cyfrifpro.model.Complaint;
import com.cyfrifpro.model.User;
import com.cyfrifpro.repository.ComplaintRepository;
import com.cyfrifpro.repository.UserRepository;
import com.cyfrifpro.service.ComplaintService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ComplaintServiceImpl implements ComplaintService {

	private final ComplaintRepository complaintRepository;
	private final UserRepository userRepository;
	private final ModelMapper modelMapper;

	public ComplaintServiceImpl(ComplaintRepository complaintRepository, UserRepository userRepository,
			ModelMapper modelMapper) {
		this.complaintRepository = complaintRepository;
		this.userRepository = userRepository;
		this.modelMapper = modelMapper;
	}

	@Override
	public ComplaintDTO postComplaint(ComplaintDTO complaintDTO) {
		// Verify that the user exists
		User user = userRepository.findById(complaintDTO.getUserId())
				.orElseThrow(() -> new ResourceNotFoundException("User", "userId", complaintDTO.getUserId()));

		// Map DTO to entity and set the user reference
		Complaint complaint = modelMapper.map(complaintDTO, Complaint.class);
		complaint.setUser(user);
		Complaint saved = complaintRepository.save(complaint);
		return modelMapper.map(saved, ComplaintDTO.class);
	}

	@Override
	public List<ComplaintDTO> getAllComplaints() {
		return complaintRepository.findAll().stream().map(c -> modelMapper.map(c, ComplaintDTO.class))
				.collect(Collectors.toList());
	}

	@Override
	public List<ComplaintDTO> getComplaintsByUserId(Long userId) {
		return complaintRepository.findByUser_UserId(userId).stream().map(c -> modelMapper.map(c, ComplaintDTO.class))
				.collect(Collectors.toList());
	}
}
