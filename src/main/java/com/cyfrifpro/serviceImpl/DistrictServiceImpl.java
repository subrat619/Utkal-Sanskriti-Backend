package com.cyfrifpro.serviceImpl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cyfrifpro.model.District;
import com.cyfrifpro.repository.DistrictRepository;
import com.cyfrifpro.service.DistrictService;

@Service
public class DistrictServiceImpl implements DistrictService {

	private final DistrictRepository districtRepository;

	public DistrictServiceImpl(DistrictRepository districtRepository) {
		this.districtRepository = districtRepository;
	}

	@Override
	public District addDistrict(District district) {
		return districtRepository.save(district);
	}

	@Override
	public List<District> getAllDistricts() {
		return districtRepository.findAll();
	}

	@Override
	public List<District> addAllDistricts(List<District> districts) {
		return districtRepository.saveAll(districts);
	}
}
