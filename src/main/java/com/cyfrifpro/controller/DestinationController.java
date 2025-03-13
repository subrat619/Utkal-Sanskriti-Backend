package com.cyfrifpro.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cyfrifpro.DTO.DestinationDTO;
import com.cyfrifpro.service.DestinationService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/destinations")
//@CrossOrigin(origins = "http://127.0.0.1:5500")
public class DestinationController {

	private final DestinationService destinationService;

	public DestinationController(DestinationService destinationService) {
		this.destinationService = destinationService;
	}

	// Endpoint to get popular destinations
//	@GetMapping("/popular")
//	public ResponseEntity<List<PopularDestinationDTO>> getPopularDestinations() {
//		List<PopularDestinationDTO> popularDestinations = destinationService.getPopularDestinations();
//		return ResponseEntity.ok(popularDestinations);
//	}

	@Operation(summary = "Method for create popular destination")
	@PostMapping
	public ResponseEntity<DestinationDTO> createDestination(@Valid @RequestBody DestinationDTO destinationDTO) {
		DestinationDTO createdDestination = destinationService.createDestination(destinationDTO);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdDestination);
	}

	@Operation(summary = "Method for create popular destination with destination photo")
	@PostMapping(value = "/upload", consumes = { "multipart/form-data" })
	public ResponseEntity<DestinationDTO> createDestinationWithImage(@RequestParam("name") String name,
			@RequestParam("location") String location, @RequestParam("description") String description,
			@RequestParam("districtName") String districtName, @RequestParam("image") MultipartFile image) {

		DestinationDTO dto = new DestinationDTO();
		dto.setName(name);
		dto.setLocation(location);
		dto.setDescription(description);
		dto.setDistrictName(districtName);
		DestinationDTO createdDestination = destinationService.createDestinationWithImage(dto, image);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdDestination);
	}

	@Operation(summary = "Method for get popular destination by id")
	@GetMapping("/{id}")
	public ResponseEntity<DestinationDTO> getDestinationById(@PathVariable Long id) {
		DestinationDTO destinationDTO = destinationService.getDestinationById(id);
		return ResponseEntity.ok(destinationDTO);
	}

	@Operation(summary = "Method for get all popular destination")
	@GetMapping
	public ResponseEntity<List<DestinationDTO>> getAllDestinations() {
		List<DestinationDTO> destinations = destinationService.getAllDestinations();
		return ResponseEntity.ok(destinations);
	}
}
