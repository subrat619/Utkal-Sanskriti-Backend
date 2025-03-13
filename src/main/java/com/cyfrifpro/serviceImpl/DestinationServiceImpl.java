package com.cyfrifpro.serviceImpl;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.cyfrifpro.DTO.DestinationDTO;
import com.cyfrifpro.model.Destination;
import com.cyfrifpro.model.District;
import com.cyfrifpro.repository.DestinationRepository;
import com.cyfrifpro.repository.DistrictRepository;
import com.cyfrifpro.service.DestinationService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class DestinationServiceImpl implements DestinationService {

	private static final Logger logger = LoggerFactory.getLogger(DestinationServiceImpl.class);
	private final DestinationRepository destinationRepository;
	private final DistrictRepository districtRepository;
	private final ModelMapper modelMapper;

	public DestinationServiceImpl(DestinationRepository destinationRepository, DistrictRepository districtRepository,
			ModelMapper modelMapper) {
		this.destinationRepository = destinationRepository;
		this.districtRepository = districtRepository;
		this.modelMapper = modelMapper;
	}

//	@Override
//	public List<PopularDestinationDTO> getPopularDestinations() {
//		logger.info("Fetching popular destinations");
//		return destinationRepository.findPopularDestinations();
//	}

	@Override
	public DestinationDTO createDestination(DestinationDTO destinationDTO) {
		// Map DTO to entity
		Destination destination = modelMapper.map(destinationDTO, Destination.class);

		// If you need to set the district from the district name, you can fetch it from
		// districtRepository
		// For example, assuming District has a unique name:
		District district = districtRepository.findByName(destinationDTO.getDistrictName()).orElseGet(() -> {
			District newDistrict = new District();
			newDistrict.setName(destinationDTO.getDistrictName());
			return districtRepository.save(newDistrict);
		});
		destination.setDistrict(district);

		Destination savedDestination = destinationRepository.save(destination);
		logger.info("Destination {} created successfully", savedDestination.getName());
		return modelMapper.map(savedDestination, DestinationDTO.class);
	}

	@Override
	public DestinationDTO createDestinationWithImage(DestinationDTO destinationDTO,
			org.springframework.web.multipart.MultipartFile image) {
		// Map DTO to entity
		Destination destination = modelMapper.map(destinationDTO, Destination.class);

		// Fetch or create District based on districtName
		District district = districtRepository.findByName(destinationDTO.getDistrictName()).orElseGet(() -> {
			District newDistrict = new District();
			newDistrict.setName(destinationDTO.getDistrictName());
			return districtRepository.save(newDistrict);
		});
		destination.setDistrict(district);

		try {
			destination.setImageData(image.getBytes());
		} catch (IOException e) {
			logger.error("Error reading image file", e);
			throw new RuntimeException("Could not read image file", e);
		}

		Destination saved = destinationRepository.save(destination);
		DestinationDTO savedDto = modelMapper.map(saved, DestinationDTO.class);
		if (saved.getImageData() != null) {
			savedDto.setImageData(java.util.Base64.getEncoder().encodeToString(saved.getImageData()));
		}
		return savedDto;
	}

	@Override
	public DestinationDTO getDestinationById(Long id) {
		Destination destination = destinationRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Destination not found with id: " + id));
		DestinationDTO dto = modelMapper.map(destination, DestinationDTO.class);
		if (destination.getImageData() != null) {
			dto.setImageData(Base64.getEncoder().encodeToString(destination.getImageData()));
		}
		return dto;
	}

	@Override
	public List<DestinationDTO> getAllDestinations() {
		logger.info("Fetching all destinations");
		List<Destination> destinations = destinationRepository.findAll();
		return destinations.stream().map(destination -> {
			DestinationDTO dto = modelMapper.map(destination, DestinationDTO.class);
			if (destination.getImageData() != null) {
				dto.setImageData(Base64.getEncoder().encodeToString(destination.getImageData()));
			}
			return dto;
		}).collect(Collectors.toList());
	}

}
