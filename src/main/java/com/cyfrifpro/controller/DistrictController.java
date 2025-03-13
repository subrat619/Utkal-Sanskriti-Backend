package com.cyfrifpro.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cyfrifpro.model.District;
import com.cyfrifpro.service.DistrictService;

@RestController
@RequestMapping("/api/districts")
//@CrossOrigin(origins = "http://localhost:3000")
public class DistrictController {

	private final DistrictService districtService;

	public DistrictController(DistrictService districtService) {
		this.districtService = districtService;
	}

	// Endpoint to add a single district
	@PostMapping
	public ResponseEntity<District> addDistrict(@RequestBody District district) {
		District savedDistrict = districtService.addDistrict(district);
		return ResponseEntity.ok(savedDistrict);
	}

	// Endpoint to fetch all districts
	@GetMapping
	public ResponseEntity<List<District>> getAllDistricts() {
		List<District> districts = districtService.getAllDistricts();
		return ResponseEntity.ok(districts);
	}

	// New endpoint for adding multiple districts at once
	@PostMapping("/bulk")
	public ResponseEntity<List<District>> addAllDistricts(@RequestBody List<District> districts) {
		List<District> savedDistricts = districtService.addAllDistricts(districts);
		return ResponseEntity.ok(savedDistricts);
	}
}
