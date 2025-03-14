package com.cyfrifpro.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cyfrifpro.DTO.TempleCombinedDTO;
import com.cyfrifpro.service.TempleCombinedService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/temples")
//@CrossOrigin(origins="http://localhost:3000")
public class TempleCombinedController {

	private final TempleCombinedService templeCombinedService;

	public TempleCombinedController(TempleCombinedService templeCombinedService) {
		this.templeCombinedService = templeCombinedService;
	}

	// Endpoint to fetch temple details along with its amounts
	@Operation(summary = "Method for get temple details with pooja ammount of that temple")
	@GetMapping("/full/{templeId}")
	public ResponseEntity<TempleCombinedDTO> getFullTempleDetails(@PathVariable Long templeId) {
		TempleCombinedDTO combinedDTO = templeCombinedService.getFullTempleDetails(templeId);
		return ResponseEntity.ok(combinedDTO);
	}
}
