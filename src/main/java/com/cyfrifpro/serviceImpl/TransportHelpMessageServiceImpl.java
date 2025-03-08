package com.cyfrifpro.serviceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.cyfrifpro.DTO.TransportHelpMessageDTO;
import com.cyfrifpro.Exception.ResourceNotFoundException;
import com.cyfrifpro.model.TransportHelpMessage;
import com.cyfrifpro.model.User;
import com.cyfrifpro.repository.TransportHelpMessageRepository;
import com.cyfrifpro.repository.UserRepository;
import com.cyfrifpro.service.TransportHelpMessageService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class TransportHelpMessageServiceImpl implements TransportHelpMessageService {

	private final TransportHelpMessageRepository helpMessageRepository;
	private final UserRepository userRepository;
	private final ModelMapper modelMapper;

	public TransportHelpMessageServiceImpl(TransportHelpMessageRepository helpMessageRepository,
			UserRepository userRepository, ModelMapper modelMapper) {
		this.helpMessageRepository = helpMessageRepository;
		this.userRepository = userRepository;
		this.modelMapper = modelMapper;
	}

	@Override
	public TransportHelpMessageDTO postHelpMessage(TransportHelpMessageDTO helpMessageDTO) {
		// Validate user existence
		User user = userRepository.findById(helpMessageDTO.getUserId())
				.orElseThrow(() -> new ResourceNotFoundException("User", "userId", helpMessageDTO.getUserId()));

		TransportHelpMessage helpMessage = modelMapper.map(helpMessageDTO, TransportHelpMessage.class);
		// Ensure that the help message has the correct user and default status
		helpMessage.setUser(user);
		helpMessage.setStatus("OPEN");
		TransportHelpMessage saved = helpMessageRepository.save(helpMessage);
		return modelMapper.map(saved, TransportHelpMessageDTO.class);
	}

	@Override
	public List<TransportHelpMessageDTO> getAllHelpMessages() {
		return helpMessageRepository.findAll().stream()
				.map(message -> modelMapper.map(message, TransportHelpMessageDTO.class)).collect(Collectors.toList());
	}

	@Override
	public List<TransportHelpMessageDTO> getHelpMessagesByUserId(Long userId) {
		return helpMessageRepository.findByUser_UserId(userId).stream()
				.map(message -> modelMapper.map(message, TransportHelpMessageDTO.class)).collect(Collectors.toList());
	}
}
