package com.cyfrifpro.service;

import java.util.List;

import com.cyfrifpro.DTO.MessageDTO;

public interface MessageService {
	MessageDTO sendMessage(MessageDTO messageDTO);

	List<MessageDTO> getMessagesBetweenUsers(Long userId1, Long userId2);

	List<MessageDTO> getMessagesForUser(Long userId);
}
