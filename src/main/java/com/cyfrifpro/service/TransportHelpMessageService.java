package com.cyfrifpro.service;

import java.util.List;

import com.cyfrifpro.DTO.TransportHelpMessageDTO;

public interface TransportHelpMessageService {
	TransportHelpMessageDTO postHelpMessage(TransportHelpMessageDTO helpMessageDTO);

	List<TransportHelpMessageDTO> getAllHelpMessages();

	// Optionally, add a method to get messages by user:
	List<TransportHelpMessageDTO> getHelpMessagesByUserId(Long userId);
}
