package com.cyfrifpro.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cyfrifpro.model.Complaint;

@Repository
public interface ComplaintRepository extends JpaRepository<Complaint, Long> {
	// You can add custom query methods if needed
	List<Complaint> findByUser_UserId(Long userId);
}
