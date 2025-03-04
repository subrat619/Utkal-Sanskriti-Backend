package com.cyfrifpro.mapper;

import org.springframework.stereotype.Component;

import com.cyfrifpro.DTO.ContactUsDTO;
import com.cyfrifpro.model.ContactUs;

@Component
public class ContactUsMapper {

	public ContactUs toEntity(ContactUsDTO dto) {
		ContactUs entity = new ContactUs();
		entity.setFullName(dto.getFullName());
		entity.setEmail(dto.getEmail());
		entity.setPhone(dto.getPhone());
		entity.setMsg(dto.getMsg());
		return entity;
	}

	public ContactUsDTO toDTO(ContactUs entity) {
		ContactUsDTO dto = new ContactUsDTO();
		dto.setFullName(entity.getFullName());
		dto.setEmail(entity.getEmail());
		dto.setPhone(entity.getPhone());
		dto.setMsg(entity.getMsg());
		return dto;
	}
}
