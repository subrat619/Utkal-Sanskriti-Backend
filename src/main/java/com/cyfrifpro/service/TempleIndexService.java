//package com.cyfrifpro.service;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//import org.springframework.stereotype.Service;
//
//import com.cyfrifpro.DTO.TempleDocument;
//import com.cyfrifpro.model.TempleDetails;
//import com.cyfrifpro.repository.TempleDetailsRepository;
//import com.cyfrifpro.repository.TempleSearchRepository;
//
//import jakarta.transaction.Transactional;
//
//@Service
//@Transactional
//public class TempleIndexService {
//
//	private final TempleDetailsRepository templeDetailsRepository;
//	private final TempleSearchRepository templeSearchRepository;
//
//	public TempleIndexService(TempleDetailsRepository templeDetailsRepository,
//			TempleSearchRepository templeSearchRepository) {
//		this.templeDetailsRepository = templeDetailsRepository;
//		this.templeSearchRepository = templeSearchRepository;
//	}
//
//	// Method to re-index all temple details from MySQL to Elasticsearch
//	public void reindexAllTemples() {
//		List<TempleDetails> temples = templeDetailsRepository.findAll();
//		List<TempleDocument> documents = temples.stream().map(temple -> {
//			TempleDocument doc = new TempleDocument();
//			doc.setId(temple.getId());
//			doc.setName(temple.getName());
//			doc.setLocation(temple.getLocation());
//			doc.setDescription(temple.getDescription());
//			doc.setDistrictName(temple.getDistrict().getName());
//			doc.setImageUrl(temple.getImageUrl());
//			doc.setCreatedAt(temple.getCreatedAt());
//			return doc;
//		}).collect(Collectors.toList());
//		templeSearchRepository.saveAll(documents);
//	}
//}
