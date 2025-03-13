package com.cyfrifpro.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cyfrifpro.DTO.ContactUsDTO;
import com.cyfrifpro.service.ContactUsService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/contacts")
@Validated
//@CrossOrigin(origins="http://localhost:3000")
public class ContactUsController {

	@Autowired
	private ContactUsService contactUsService;

	@GetMapping("/{id}")
	public ResponseEntity<ContactUsDTO> getContactUsById(@PathVariable Long id) {
		ContactUsDTO contactUs = contactUsService.getContactUsById(id);
		return ResponseEntity.ok(contactUs);
	}

	@PostMapping("/send")
	public ResponseEntity<ContactUsDTO> saveContactUs(@Valid @RequestBody ContactUsDTO contactUsDTO) {
		ContactUsDTO savedContactUs = contactUsService.saveContactUs(contactUsDTO);
		return ResponseEntity.status(HttpStatus.CREATED).body(savedContactUs);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteContactUs(@PathVariable Long id) {
		contactUsService.deleteContactUs(id);
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/all")
    public List<ContactUsDTO> getAllContactUs() {
		return contactUsService.getAllContactUsLists();
	}
}
