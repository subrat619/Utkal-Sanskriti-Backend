package com.cyfrifpro.serviceImpl;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.cyfrifpro.DTO.TempleAmountsDTO;
import com.cyfrifpro.Exception.ResourceNotFoundException;
import com.cyfrifpro.model.TempleAmounts;
import com.cyfrifpro.model.TempleDetails;
import com.cyfrifpro.repository.TempleAmountsRepository;
import com.cyfrifpro.repository.TempleDetailsRepository;
import com.cyfrifpro.service.TempleAmountsService;

import jakarta.transaction.Transactional;

@Service
@Transactional
@CacheConfig(cacheNames = { "templeAmounts" })
public class TempleAmountsServiceImpl implements TempleAmountsService {

	private static final Logger logger = LoggerFactory.getLogger(TempleAmountsServiceImpl.class);

	private final TempleAmountsRepository templeAmountsRepository;
	private final TempleDetailsRepository templeDetailsRepository;
	private final ModelMapper modelMapper;

	public TempleAmountsServiceImpl(TempleAmountsRepository templeAmountsRepository,
			TempleDetailsRepository templeDetailsRepository, ModelMapper modelMapper) {
		this.templeAmountsRepository = templeAmountsRepository;
		this.templeDetailsRepository = templeDetailsRepository;
		this.modelMapper = modelMapper;
	}

	@Override
	public TempleAmountsDTO createTempleAmounts(TempleAmountsDTO dto) {
		logger.info("Creating temple amounts for temple id: {}", dto.getTempleId());
		// Retrieve the temple details for association
		TempleDetails temple = templeDetailsRepository.findById(dto.getTempleId())
				.orElseThrow(() -> new ResourceNotFoundException("TempleDetails", "id", dto.getTempleId()));
		TempleAmounts amounts = new TempleAmounts();
		amounts.setTemple(temple);
		amounts.setPoojaFee(dto.getPoojaFee());
		amounts.setRudrabhisekhFee(dto.getRudrabhisekhFee());
		amounts.setPrasadFee(dto.getPrasadFee());
		TempleAmounts saved = templeAmountsRepository.save(amounts);
		return modelMapper.map(saved, TempleAmountsDTO.class);
	}

	@Override
	@CachePut(key = "#templeId")
	public TempleAmountsDTO updateTempleAmounts(Long templeId, TempleAmountsDTO dto) {
		logger.info("Updating temple amounts for temple id: {}", templeId);
		TempleAmounts amounts = templeAmountsRepository.findByTemple_Id(templeId)
				.orElseThrow(() -> new ResourceNotFoundException("TempleAmounts", "templeId", templeId));
		amounts.setPoojaFee(dto.getPoojaFee());
		amounts.setRudrabhisekhFee(dto.getRudrabhisekhFee());
		amounts.setPrasadFee(dto.getPrasadFee());
		TempleAmounts updated = templeAmountsRepository.save(amounts);
		return modelMapper.map(updated, TempleAmountsDTO.class);
	}

	@Override
	@Cacheable(key = "#templeId")
	public TempleAmountsDTO getTempleAmountsByTempleId(Long templeId) {
		logger.debug("Fetching temple amounts for temple id: {}", templeId);
		TempleAmounts amounts = templeAmountsRepository.findByTemple_Id(templeId)
				.orElseThrow(() -> new ResourceNotFoundException("TempleAmounts", "templeId", templeId));
		return modelMapper.map(amounts, TempleAmountsDTO.class);
	}

	@Override
	@CacheEvict(key = "#templeId")
	public void deleteTempleAmounts(Long templeId) {
		TempleAmounts amounts = templeAmountsRepository.findByTemple_Id(templeId)
				.orElseThrow(() -> new ResourceNotFoundException("TempleAmounts", "templeId", templeId));
		templeAmountsRepository.delete(amounts);
		logger.info("Deleted temple amounts for temple id: {}", templeId);
	}
}
