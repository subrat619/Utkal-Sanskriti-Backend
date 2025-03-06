package com.cyfrifpro.service;

import java.util.Collection;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.cyfrifpro.DTO.PopularTempleDTO;
import com.cyfrifpro.DTO.TempleDetailsDTO;

public interface TempleDetailsService {
	TempleDetailsDTO createTempleDetails(TempleDetailsDTO dto);

	TempleDetailsDTO createTempleDetailsWithImage(TempleDetailsDTO dto, MultipartFile image);

	TempleDetailsDTO updateTempleDetails(Long id, TempleDetailsDTO dto);

	TempleDetailsDTO getTempleDetailsById(Long id);

	List<TempleDetailsDTO> getAllTempleDetails();

	void deleteTempleDetails(Long id);

	List<TempleDetailsDTO> getTemplesByDistrict(String districtName);

	List<TempleDetailsDTO> getTempleDetailsByDistrictNames(Collection<String> districtNames);

	List<TempleDetailsDTO> searchTemplesByName(String name);

	List<PopularTempleDTO> getPopularTemples();

}
