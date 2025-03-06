package com.cyfrifpro.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cyfrifpro.DTO.PopularTempleDTO;
import com.cyfrifpro.model.TempleDetails;

@Repository
public interface TempleDetailsRepository extends JpaRepository<TempleDetails, Long> {

	List<TempleDetails> findByNameContainingIgnoreCase(String name);

	List<TempleDetails> findByDistrict_Name(String districtName);

	List<TempleDetails> findByDistrict_NameIn(Collection<String> districtNames);

//	method to find popular temples based on booking count
	@Query("SELECT new com.cyfrifpro.DTO.PopularTempleDTO(t.id, t.name, t.location, d.name, t.imageData, COUNT(bi)) "
			+ "FROM BookingItem bi JOIN bi.temple t JOIN t.district d "
			+ "GROUP BY t.id, t.name, t.location, d.name, t.imageData " + "ORDER BY COUNT(bi) DESC")
	List<PopularTempleDTO> findPopularTemples();

}
