package com.cyfrifpro.serviceImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.cyfrifpro.DTO.MembershipDTO;
import com.cyfrifpro.Exception.ResourceNotFoundException;
import com.cyfrifpro.model.Membership;
import com.cyfrifpro.model.User;
import com.cyfrifpro.repository.MembershipRepository;
import com.cyfrifpro.repository.UserRepository;
import com.cyfrifpro.service.MembershipService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class MembershipServiceImpl implements MembershipService {

	private static final Logger logger = LoggerFactory.getLogger(MembershipServiceImpl.class);

	private final MembershipRepository membershipRepository;
	private final UserRepository userRepository;
	private final ModelMapper modelMapper;

	public MembershipServiceImpl(MembershipRepository membershipRepository, UserRepository userRepository,
			ModelMapper modelMapper) {
		this.membershipRepository = membershipRepository;
		this.userRepository = userRepository;
		this.modelMapper = modelMapper;
	}

	@Override
	public MembershipDTO subscribeMembership(MembershipDTO membershipDTO) {
		// Retrieve the client using the clientId from the DTO
		User client = userRepository.findById(membershipDTO.getClientId())
				.orElseThrow(() -> new ResourceNotFoundException("User", "clientId", membershipDTO.getClientId()));

		Membership membership = new Membership();
		membership.setClient(client);
		membership.setMembershipType(membershipDTO.getMembershipType());
		membership.setFee(membershipDTO.getFee());

		// Set the start date to now
		membership.setStartDate(LocalDateTime.now());

		// Set expiry date based on membership type
		if (membershipDTO.getMembershipType().equalsIgnoreCase("Gold")) {
			membership.setExpiryDate(LocalDateTime.now().plusMonths(6));
		} else if (membershipDTO.getMembershipType().equalsIgnoreCase("Premium")) {
			membership.setExpiryDate(LocalDateTime.now().plusYears(1));
		} else {
			// Default expiry date (you can adjust this logic for other types)
			membership.setExpiryDate(LocalDateTime.now().plusYears(1));
		}

		membership.setStatus("ACTIVE");

		Membership savedMembership = membershipRepository.save(membership);
		logger.info("Membership subscribed for clientId: {}", client.getUserId());
		return modelMapper.map(savedMembership, MembershipDTO.class);
	}

	@Override
	public MembershipDTO getMembershipByClientId(Long clientId) {
		Membership membership = membershipRepository.findByClient_UserId(clientId)
				.orElseThrow(() -> new ResourceNotFoundException("Membership", "clientId", clientId));
		return modelMapper.map(membership, MembershipDTO.class);
	}

	@Override
	public List<MembershipDTO> getActiveMemberships() {
		List<Membership> activeMemberships = membershipRepository.findByStatus("ACTIVE");
		return activeMemberships.stream().map(membership -> modelMapper.map(membership, MembershipDTO.class))
				.collect(Collectors.toList());
	}
}
