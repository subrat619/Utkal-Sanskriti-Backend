package com.cyfrifpro.service;

import java.util.List;

import com.cyfrifpro.DTO.DestinationDTO;

public interface DestinationService {
//	List<PopularDestinationDTO> getPopularDestinations();

	DestinationDTO createDestination(DestinationDTO destinationDTO);

	DestinationDTO createDestinationWithImage(DestinationDTO destinationDTO,
			org.springframework.web.multipart.MultipartFile image);

	DestinationDTO getDestinationById(Long id);

	List<DestinationDTO> getAllDestinations();
}
