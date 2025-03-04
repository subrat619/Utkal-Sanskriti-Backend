package com.cyfrifpro.service;

import java.util.List;

import com.cyfrifpro.DTO.MembershipDTO;

public interface MembershipService {
	MembershipDTO subscribeMembership(MembershipDTO membershipDTO);

	MembershipDTO getMembershipByClientId(Long clientId);

	List<MembershipDTO> getActiveMemberships();
}
