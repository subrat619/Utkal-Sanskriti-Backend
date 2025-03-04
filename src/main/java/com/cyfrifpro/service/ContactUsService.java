package com.cyfrifpro.service;

import java.util.List;

import com.cyfrifpro.DTO.ContactUsDTO;

public interface ContactUsService {
	ContactUsDTO getContactUsById(Long id);

	ContactUsDTO saveContactUs(ContactUsDTO contactUsDTO);

	void deleteContactUs(Long id);

	List<ContactUsDTO> getAllContactUsLists();
}
