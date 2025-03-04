package com.cyfrifpro.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cyfrifpro.model.Membership;

@Repository
public interface MembershipRepository extends JpaRepository<Membership, Long> {
	Optional<Membership> findByClient_UserId(Long clientId);

	List<Membership> findByStatus(String status);
}
