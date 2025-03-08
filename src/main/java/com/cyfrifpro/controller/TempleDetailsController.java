package com.cyfrifpro.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cyfrifpro.DTO.PopularTempleDTO;
import com.cyfrifpro.DTO.TempleDetailsDTO;
import com.cyfrifpro.service.TempleDetailsService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/temple_details")
@Validated
@CrossOrigin(origins = "http://127.0.0.1:5500/")
public class TempleDetailsController {

	private final TempleDetailsService templeDetailsService;

	public TempleDetailsController(TempleDetailsService templeDetailsService) {
		this.templeDetailsService = templeDetailsService;
	}

	// Allowed for MID_LEVEL, TEMPLE_ADMIN, and TOP_LEVEL
	@PreAuthorize("hasAnyRole('TOP_LEVEL')")
	@PostMapping("/add")
	public ResponseEntity<TempleDetailsDTO> createTempleDetails(@Valid @RequestBody TempleDetailsDTO dto) {
		TempleDetailsDTO created = templeDetailsService.createTempleDetails(dto);
		return new ResponseEntity<>(created, HttpStatus.CREATED);
	}

//	@PreAuthorize("hasAnyRole('TOP_LEVEL')")
	@PostMapping("/add_with_image")
	public ResponseEntity<TempleDetailsDTO> createTempleDetailsWithImage(@RequestParam("name") String name,
			@RequestParam("location") String location, @RequestParam("description") String description,
			@RequestParam("districtName") String districtName, @RequestParam("image") MultipartFile image) {

		// Build the DTO from request parameters
		TempleDetailsDTO dto = new TempleDetailsDTO();
		dto.setName(name);
		dto.setLocation(location);
		dto.setDescription(description);
		dto.setDistrictName(districtName);

		TempleDetailsDTO created = templeDetailsService.createTempleDetailsWithImage(dto, image);
		return new ResponseEntity<>(created, HttpStatus.CREATED);
	}

	@PreAuthorize("hasAnyRole('MID_LEVEL','TEMPLE_ADMIN','TOP_LEVEL')")
	@PutMapping("/{id}")
	public ResponseEntity<TempleDetailsDTO> updateTempleDetails(@PathVariable Long id,
			@Valid @RequestBody TempleDetailsDTO dto) {
		TempleDetailsDTO updated = templeDetailsService.updateTempleDetails(id, dto);
		return ResponseEntity.ok(updated);
	}

	@GetMapping("/{id}")
	public ResponseEntity<TempleDetailsDTO> getTempleDetails(@PathVariable Long id) {
		TempleDetailsDTO dto = templeDetailsService.getTempleDetailsById(id);
		return ResponseEntity.ok(dto);
	}

	@GetMapping("/all")
	public ResponseEntity<List<TempleDetailsDTO>> getAllTempleDetails() {
		List<TempleDetailsDTO> dtos = templeDetailsService.getAllTempleDetails();
		return ResponseEntity.ok(dtos);
	}

	@PreAuthorize("hasAnyRole('MID_LEVEL','TEMPLE_ADMIN','TOP_LEVEL')")
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteTempleDetails(@PathVariable Long id) {
		templeDetailsService.deleteTempleDetails(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/by_district/{districtName}")
	public ResponseEntity<List<TempleDetailsDTO>> getTemplesByDistrict(@PathVariable String districtName) {
		List<TempleDetailsDTO> temples = templeDetailsService.getTemplesByDistrict(districtName);
		return ResponseEntity.ok(temples);
	}

	@GetMapping("/by_districts")
	public ResponseEntity<List<TempleDetailsDTO>> getTemplesByDistricts(@RequestParam String districts) {
		// Split the comma-separated list into a Collection
		List<String> districtNames = Arrays.asList(districts.split(","));
		List<TempleDetailsDTO> temples = templeDetailsService.getTempleDetailsByDistrictNames(districtNames);
		return ResponseEntity.ok(temples);
	}

	@GetMapping("/search")
	public ResponseEntity<List<TempleDetailsDTO>> searchTemplesByName(@RequestParam("name") String name) {
		List<TempleDetailsDTO> dtos = templeDetailsService.searchTemplesByName(name);
		return ResponseEntity.ok(dtos);
	}

	@GetMapping("/popular")
	public ResponseEntity<List<PopularTempleDTO>> getPopularTemples() {
		List<PopularTempleDTO> popularTemples = templeDetailsService.getPopularTemples();
		return ResponseEntity.ok(popularTemples);
	}

}
