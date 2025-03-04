package com.cyfrifpro.service;

import java.util.List;

import com.cyfrifpro.model.District;

public interface DistrictService {
	District addDistrict(District district);

	List<District> getAllDistricts();

	List<District> addAllDistricts(List<District> districts);
}
