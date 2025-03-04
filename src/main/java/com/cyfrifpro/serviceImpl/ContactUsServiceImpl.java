package com.cyfrifpro.serviceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cyfrifpro.DTO.ContactUsDTO;
import com.cyfrifpro.Exception.ResourceNotFoundException;
import com.cyfrifpro.mapper.ContactUsMapper;
import com.cyfrifpro.model.ContactUs;
import com.cyfrifpro.repository.ContactUsRepository;
import com.cyfrifpro.service.ContactUsService;

@Service
public class ContactUsServiceImpl implements ContactUsService {

	@Autowired
	private ContactUsRepository contactUsRepository;

	@Autowired
	private ContactUsMapper contactUsMapper;

	@Override
	public ContactUsDTO getContactUsById(Long id) {
		ContactUs contactUs = contactUsRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Contact Us not found for ID: " + id));
		return contactUsMapper.toDTO(contactUs);
	}

	@Override
	public ContactUsDTO saveContactUs(ContactUsDTO contactUsDTO) {
		ContactUs contactUs = contactUsMapper.toEntity(contactUsDTO);
		ContactUs savedContactUs = contactUsRepository.save(contactUs);
		return contactUsMapper.toDTO(savedContactUs);
	}

	@Override
	public void deleteContactUs(Long id) {
		if (!contactUsRepository.existsById(id)) {
			throw new ResourceNotFoundException("Contact Us not found for ID: " + id);
		}
		contactUsRepository.deleteById(id);
	}

	@Override
	public List<ContactUsDTO> getAllContactUsLists() {
//		ContactUs contactUs = new ContactUs();
		return contactUsRepository.findAll().stream().map(contactUsMapper::toDTO).collect(Collectors.toList());
	}
}
