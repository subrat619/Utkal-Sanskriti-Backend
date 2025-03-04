package com.cyfrifpro.controller;

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
import org.springframework.web.bind.annotation.RestController;

import com.cyfrifpro.DTO.TempleAmountsDTO;
import com.cyfrifpro.service.TempleAmountsService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/temple_amounts")
@Validated
@CrossOrigin(origins="http://localhost:3000")
public class TempleAmountsController {

	private final TempleAmountsService templeAmountsService;

	public TempleAmountsController(TempleAmountsService templeAmountsService) {
		this.templeAmountsService = templeAmountsService;
	}

	// Only TEMPLE_ADMIN can create amounts
	@PreAuthorize("hasAnyRole('TEMPLE_ADMIN')")
	@PostMapping("/add")
	public ResponseEntity<TempleAmountsDTO> createTempleAmounts(@Valid @RequestBody TempleAmountsDTO dto) {
		TempleAmountsDTO created = templeAmountsService.createTempleAmounts(dto);
		return new ResponseEntity<>(created, HttpStatus.CREATED);
	}

	// TOP_LEVEL and TEMPLE_ADMIN can update amounts
	@PreAuthorize("hasAnyRole('TEMPLE_ADMIN','TOP_LEVEL')")
	@PutMapping("/{templeId}")
	public ResponseEntity<TempleAmountsDTO> updateTempleAmounts(@PathVariable Long templeId,
			@Valid @RequestBody TempleAmountsDTO dto) {
		TempleAmountsDTO updated = templeAmountsService.updateTempleAmounts(templeId, dto);
		return ResponseEntity.ok(updated);
	}

	@GetMapping("/{templeId}")
	public ResponseEntity<TempleAmountsDTO> getTempleAmounts(@PathVariable Long templeId) {
		TempleAmountsDTO dto = templeAmountsService.getTempleAmountsByTempleId(templeId);
		return ResponseEntity.ok(dto);
	}

	// Only TOP_LEVEL can delete amounts (if desired, you can also allow
	// TEMPLE_ADMIN)
	@PreAuthorize("hasAnyRole('TOP_LEVEL','TEMPLE_ADMIN')")
	@DeleteMapping("/{templeId}")
	public ResponseEntity<Void> deleteTempleAmounts(@PathVariable Long templeId) {
		templeAmountsService.deleteTempleAmounts(templeId);
		return ResponseEntity.noContent().build();
	}
}
