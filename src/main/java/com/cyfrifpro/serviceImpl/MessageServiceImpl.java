package com.cyfrifpro.serviceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.cyfrifpro.DTO.MessageDTO;
import com.cyfrifpro.Exception.ResourceNotFoundException;
import com.cyfrifpro.model.Message;
import com.cyfrifpro.model.User;
import com.cyfrifpro.repository.MessageRepository;
import com.cyfrifpro.repository.UserRepository;
import com.cyfrifpro.service.MessageService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class MessageServiceImpl implements MessageService {

    private static final Logger logger = LoggerFactory.getLogger(MessageServiceImpl.class);

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public MessageServiceImpl(MessageRepository messageRepository, UserRepository userRepository, ModelMapper modelMapper) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public MessageDTO sendMessage(MessageDTO messageDTO) {
        // Validate that sender and recipient exist
        User sender = userRepository.findById(messageDTO.getSenderId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "senderId", messageDTO.getSenderId()));
        User recipient = userRepository.findById(messageDTO.getRecipientId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "recipientId", messageDTO.getRecipientId()));

        Message message = new Message();
        message.setSender(sender);
        message.setRecipient(recipient);
        message.setContent(messageDTO.getContent());
        // timestamp is set via @PrePersist

        Message savedMessage = messageRepository.save(message);
        logger.info("Message sent from {} to {} with id: {}", sender.getUserId(), recipient.getUserId(), savedMessage.getMessageId());
        return modelMapper.map(savedMessage, MessageDTO.class);
    }

    @Override
    public List<MessageDTO> getMessagesBetweenUsers(Long userId1, Long userId2) {
        List<Message> messages = messageRepository.findBySender_UserIdAndRecipient_UserId(userId1, userId2);
        // Optionally, merge with messages in reverse order if needed:
        messages.addAll(messageRepository.findBySender_UserIdAndRecipient_UserId(userId2, userId1));
        return messages.stream()
                .sorted((m1, m2) -> m1.getTimestamp().compareTo(m2.getTimestamp()))
                .map(message -> modelMapper.map(message, MessageDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<MessageDTO> getMessagesForUser(Long userId) {
        List<Message> messages = messageRepository.findBySender_UserIdOrRecipient_UserId(userId, userId);
        return messages.stream()
                .sorted((m1, m2) -> m1.getTimestamp().compareTo(m2.getTimestamp()))
                .map(message -> modelMapper.map(message, MessageDTO.class))
                .collect(Collectors.toList());
    }
}
