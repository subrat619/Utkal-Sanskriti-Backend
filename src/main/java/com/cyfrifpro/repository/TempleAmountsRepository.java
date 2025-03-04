package com.cyfrifpro.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cyfrifpro.model.TempleAmounts;

@Repository
public interface TempleAmountsRepository extends JpaRepository<TempleAmounts, Long> {
	Optional<TempleAmounts> findByTemple_Id(Long templeId);
}
