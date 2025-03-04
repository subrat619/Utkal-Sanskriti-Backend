package com.cyfrifpro.serviceImpl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.cyfrifpro.DTO.TempleAmountsDTO;
import com.cyfrifpro.DTO.TempleCombinedDTO;
import com.cyfrifpro.DTO.TempleDetailsDTO;
import com.cyfrifpro.Exception.ResourceNotFoundException;
import com.cyfrifpro.service.TempleAmountsService;
import com.cyfrifpro.service.TempleCombinedService;
import com.cyfrifpro.service.TempleDetailsService;

@Service
public class TempleCombinedServiceImpl implements TempleCombinedService {

	private final TempleDetailsService templeDetailsService;
	private final TempleAmountsService templeAmountsService;

	public TempleCombinedServiceImpl(TempleDetailsService templeDetailsService,
			TempleAmountsService templeAmountsService, ModelMapper modelMapper) {
		this.templeDetailsService = templeDetailsService;
		this.templeAmountsService = templeAmountsService;
	}

	@Override
	public TempleCombinedDTO getFullTempleDetails(Long templeId) {
		// Fetch temple details
		TempleDetailsDTO detailsDTO = templeDetailsService.getTempleDetailsById(templeId);

		// Try fetching temple amounts; if not found, set as null.
		TempleAmountsDTO amountsDTO = null;
		try {
			amountsDTO = templeAmountsService.getTempleAmountsByTempleId(templeId);
		} catch (ResourceNotFoundException ex) {
			// If no amounts exist for this temple, we can leave it null.
		}

		// Create and return the combined DTO
		TempleCombinedDTO combinedDTO = new TempleCombinedDTO();
		combinedDTO.setTempleDetails(detailsDTO);
		combinedDTO.setTempleAmounts(amountsDTO);
		return combinedDTO;
	}

}
