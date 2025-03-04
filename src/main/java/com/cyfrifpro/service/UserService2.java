package com.cyfrifpro.service;

import java.util.List;

import com.cyfrifpro.DTO.ChangePasswordRequest;
import com.cyfrifpro.DTO.UserDTO;
import com.cyfrifpro.DTO.UserProfileUpdateDTO;
import com.cyfrifpro.model.User;

public interface UserService2 {
	void changePassword(Long userId, ChangePasswordRequest request);

	UserDTO updateUserProfile(Long userId, UserProfileUpdateDTO dto);

	List<User> getAllDescendantUsers(Long creatorId);

	List<User> getRegisteredClients();

	List<User> getMidLevelUsersByTopLevelId(Long topLevelId);

	List<UserDTO> getUsersByStatus(String status);
}
