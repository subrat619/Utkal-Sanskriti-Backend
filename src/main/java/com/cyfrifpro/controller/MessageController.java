package com.cyfrifpro.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cyfrifpro.DTO.MessageDTO;
import com.cyfrifpro.service.MessageService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/messages")
//@CrossOrigin(origins = "http://127.0.0.1:5500")
public class MessageController {

	private final MessageService messageService;

	public MessageController(MessageService messageService) {
		this.messageService = messageService;
	}

	@Operation(summary = "Method for send message to an user from an user")
	@PostMapping
	public ResponseEntity<MessageDTO> sendMessage(@Valid @RequestBody MessageDTO messageDTO) {
		MessageDTO sentMessage = messageService.sendMessage(messageDTO);
		return ResponseEntity.status(201).body(sentMessage);
	}

	@Operation(summary = "Method for get conversasion in between two users")
	@GetMapping("/conversation")
	public ResponseEntity<List<MessageDTO>> getConversation(@RequestParam Long userId1, @RequestParam Long userId2) {
		List<MessageDTO> conversation = messageService.getMessagesBetweenUsers(userId1, userId2);
		return ResponseEntity.ok(conversation);
	}

	@Operation(summary = "Method for get message conversasion by user id")
	@GetMapping("/user/{userId}")
	public ResponseEntity<List<MessageDTO>> getMessagesForUser(@PathVariable Long userId) {
		List<MessageDTO> messages = messageService.getMessagesForUser(userId);
		return ResponseEntity.ok(messages);
	}
}
