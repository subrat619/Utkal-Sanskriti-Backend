package com.cyfrifpro.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.cyfrifpro.DTO.MessageDTO;

@Controller
@CrossOrigin(origins = "http://localhost:5500")
public class ChatController {

	@MessageMapping("/chat")
	@SendTo("/topic/messages")
	public MessageDTO send(MessageDTO messageDTO) throws Exception {
		// Optionally, you can save the message to DB here if needed.
		return messageDTO;
	}
}
