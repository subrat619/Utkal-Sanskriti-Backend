package com.cyfrifpro.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cyfrifpro.model.TempleDetails;

@Repository
public interface TempleDetailsRepository extends JpaRepository<TempleDetails, Long> {
	List<TempleDetails> findByDistrict_Name(String districtName);

	List<TempleDetails> findByDistrict_NameIn(Collection<String> districtNames);

}
