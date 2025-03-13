//package com.cyfrifpro.service;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//import org.springframework.stereotype.Service;
//
//import com.cyfrifpro.DTO.TempleDetailsDTO;
//import com.cyfrifpro.DTO.TempleDocument;
//import com.cyfrifpro.repository.TempleSearchRepository;
//
//import jakarta.transaction.Transactional;
//
//@Service
//@Transactional
//public class TempleSearchService {
//
//	private final TempleSearchRepository templeSearchRepository;
//
//	public TempleSearchService(TempleSearchRepository templeSearchRepository) {
//		this.templeSearchRepository = templeSearchRepository;
//	}
//
//	public List<TempleDetailsDTO> searchTemplesByName(String name) {
//		List<TempleDocument> results = templeSearchRepository.findByNameContainingIgnoreCase(name);
//		return results.stream().map(doc -> {
//			TempleDetailsDTO dto = new TempleDetailsDTO();
//			dto.setId(doc.getId());
//			dto.setName(doc.getName());
//			dto.setLocation(doc.getLocation());
//			dto.setDescription(doc.getDescription());
//			dto.setDistrictName(doc.getDistrictName());
//			dto.setImageUrl(doc.getImageUrl());
//			return dto;
//		}).collect(Collectors.toList());
//	}
//}
