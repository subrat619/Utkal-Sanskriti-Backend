package com.cyfrifpro.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cyfrifpro.DTO.UserHierarchyDTO;
import com.cyfrifpro.model.User;

@Configuration
public class AppConfig {

	@Bean
	public ModelMapper modelMapper() {
		ModelMapper modelMapper = new ModelMapper();

		modelMapper.typeMap(User.class, UserHierarchyDTO.class)
				.addMappings(mapper -> mapper.skip(UserHierarchyDTO::setSubordinates)); // Skip subordinates initially

		return modelMapper;
	}
}
