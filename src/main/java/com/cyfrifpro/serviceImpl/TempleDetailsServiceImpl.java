package com.cyfrifpro.serviceImpl;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cyfrifpro.DTO.TempleDetailsDTO;
import com.cyfrifpro.Exception.ResourceNotFoundException;
import com.cyfrifpro.model.District;
import com.cyfrifpro.model.TempleDetails;
import com.cyfrifpro.repository.DistrictRepository;
import com.cyfrifpro.repository.TempleDetailsRepository;
import com.cyfrifpro.service.TempleDetailsService;

import jakarta.transaction.Transactional;

@Service
@Transactional
@CacheConfig(cacheNames = { "templeDetails" })
public class TempleDetailsServiceImpl implements TempleDetailsService {

	private static final Logger logger = LoggerFactory.getLogger(TempleDetailsServiceImpl.class);

	private final TempleDetailsRepository templeDetailsRepository;
	private final DistrictRepository districtRepository;
	private final ModelMapper modelMapper;

	public TempleDetailsServiceImpl(TempleDetailsRepository templeDetailsRepository,
			DistrictRepository districtRepository, ModelMapper modelMapper) {
		this.templeDetailsRepository = templeDetailsRepository;
		this.districtRepository = districtRepository;
		this.modelMapper = modelMapper;
	}

	@Override
	public TempleDetailsDTO createTempleDetails(TempleDetailsDTO dto) {
		logger.info("Creating temple details for: {}", dto.getName());
		TempleDetails temple = modelMapper.map(dto, TempleDetails.class);

		// Automatically create the district if it doesn't exist
		District district = districtRepository.findByName(dto.getDistrictName()).orElseGet(() -> {
			District newDistrict = new District();
			newDistrict.setName(dto.getDistrictName());
			logger.info("District not found, creating new district: {}", dto.getDistrictName());
			return districtRepository.save(newDistrict);
		});
		temple.setDistrict(district);

		TempleDetails saved = templeDetailsRepository.save(temple);
		return modelMapper.map(saved, TempleDetailsDTO.class);
	}

	@Override
	@CachePut(key = "#id")
	public TempleDetailsDTO updateTempleDetails(Long id, TempleDetailsDTO dto) {
		logger.info("Updating temple details for id: {}", id);
		TempleDetails temple = templeDetailsRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("TempleDetails", "id", id));
		temple.setName(dto.getName());
		temple.setLocation(dto.getLocation());
		temple.setDescription(dto.getDescription());
		if (dto.getDistrictName() != null) {
			District district = districtRepository.findByName(dto.getDistrictName())
					.orElseThrow(() -> new ResourceNotFoundException("District", "name", dto.getDistrictName()));
			temple.setDistrict(district);
		}
		TempleDetails updated = templeDetailsRepository.save(temple);
		return modelMapper.map(updated, TempleDetailsDTO.class);
	}

	@Override
	public TempleDetailsDTO createTempleDetailsWithImage(TempleDetailsDTO dto, MultipartFile image) {
		logger.info("Creating temple details for: {}", dto.getName());
		// Map DTO to entity
		TempleDetails temple = modelMapper.map(dto, TempleDetails.class);

		// Automatically create the district if it doesn't exist
		District district = districtRepository.findByName(dto.getDistrictName()).orElseGet(() -> {
			District newDistrict = new District();
			newDistrict.setName(dto.getDistrictName());
			logger.info("District not found, creating new district: {}", dto.getDistrictName());
			return districtRepository.save(newDistrict);
		});
		temple.setDistrict(district);

		try {
			// Set image data from the uploaded file
			temple.setImageData(image.getBytes());
		} catch (Exception e) {
			logger.error("Error reading image file", e);
			throw new RuntimeException("Could not read image file", e);
		}

		// Save the temple details
		TempleDetails saved = templeDetailsRepository.save(temple);
		return modelMapper.map(saved, TempleDetailsDTO.class);
	}

	@Override
	@Cacheable(key = "#id")
	public TempleDetailsDTO getTempleDetailsById(Long id) {
		logger.debug("Fetching temple details for id: {}", id);
		TempleDetails temple = templeDetailsRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("TempleDetails", "id", id));

		TempleDetailsDTO dto = modelMapper.map(temple, TempleDetailsDTO.class);

		dto.setDistrictName(temple.getDistrict().getName());
		return dto;
	}

	@Override
	public List<TempleDetailsDTO> getAllTempleDetails() {
		return templeDetailsRepository.findAll().stream().map(t -> {
			TempleDetailsDTO dto = modelMapper.map(t, TempleDetailsDTO.class);
			dto.setDistrictName(t.getDistrict().getName());
			return dto;
		}).collect(Collectors.toList());
	}

	@CacheEvict(key = "#id")
	@Override
	public void deleteTempleDetails(Long id) {
		TempleDetails temple = templeDetailsRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("TempleDetails", "id", id));
		templeDetailsRepository.delete(temple);
		logger.info("Deleted temple details with id: {}", id);
	}

	@Override
	public List<TempleDetailsDTO> getTemplesByDistrict(String districtName) {
		List<TempleDetails> temples = templeDetailsRepository.findByDistrict_Name(districtName);
		return temples.stream().map(t -> {
			TempleDetailsDTO dto = modelMapper.map(t, TempleDetailsDTO.class);
			dto.setDistrictName(t.getDistrict().getName());
			return dto;
		}).collect(Collectors.toList());
	}

	@Override
	public List<TempleDetailsDTO> getTempleDetailsByDistrictNames(Collection<String> districtNames) {
		List<TempleDetails> temples = templeDetailsRepository.findByDistrict_NameIn(districtNames);
		return temples.stream().map(t -> {
			TempleDetailsDTO dto = modelMapper.map(t, TempleDetailsDTO.class);
			dto.setDistrictName(t.getDistrict().getName());
			return dto;
		}).collect(Collectors.toList());
	}

}
