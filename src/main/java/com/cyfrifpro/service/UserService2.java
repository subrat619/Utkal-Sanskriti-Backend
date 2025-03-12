package com.cyfrifpro.service;

import java.util.List;
import java.util.Map;

import com.cyfrifpro.DTO.ChangePasswordRequest;
import com.cyfrifpro.DTO.UserContactDTO;
import com.cyfrifpro.DTO.UserDTO;
import com.cyfrifpro.DTO.UserHierarchyCountDTO;
import com.cyfrifpro.DTO.UserProfileUpdateDTO;
import com.cyfrifpro.DTO.UserSummaryDTO;
import com.cyfrifpro.model.Role;
import com.cyfrifpro.model.User;

public interface UserService2 {
	void changePassword(Long userId, ChangePasswordRequest request);

	UserDTO updateUserProfile(Long userId, UserProfileUpdateDTO dto);

	List<User> getAllDescendantUsers(Long creatorId);

	List<User> getRegisteredClients();

	List<User> getMidLevelUsersByTopLevelId(Long topLevelId);

	List<User> getTeamLeadersByMidLevelId(Long midLevelId);

	List<User> getSupportServiceByTeamLeaderId(Long teamLeaderId);

	List<User> getTempleAdminByTeamLeaderId(Long teamLeaderId);

	List<User> getGuidesByTempleAdminId(Long templeAdminId);

	List<UserDTO> getUsersByStatus(String status);

//	Map<String, Object> registerTempleAdminWithTemple(TempleAdminRegistrationRequest request);

	List<UserContactDTO> getUserContacts();

	UserSummaryDTO getUserSummaryById(Long userId);

	/**
	 * Counts the number of users for each role.
	 * 
	 * @return a map with keys as roles and values as the count of users for that
	 *         role.
	 */
	Map<Role, Long> countUsersByRole();

	/**
	 * Counts the number of users created by the given creator (via the createdBy
	 * relation), grouped by role.
	 *
	 * @param creatorId the ID of the creator user
	 * @return a map where keys are roles and values are counts of users created by
	 *         that creator
	 */
//	Map<Role, Long> countUsersByCreatorId(Long creatorId);

	Map<Role, Long> countDirectChildrenByCreatorId(Long creatorId);

	/**
	 * Recursively retrieves a hierarchical count of all users created by the given
	 * user. This returns the direct children counts and for each child, their own
	 * hierarchical counts.
	 * 
	 * @param userId the creator's user id
	 * @return the hierarchical count as a UserHierarchyCountDTO
	 */
	UserHierarchyCountDTO getUserHierarchy(Long userId);

	/**
	 * Recursively counts all descendant users created (directly and indirectly) by
	 * the given creator, grouped by their role.
	 *
	 * @param creatorId the ID of the creator user
	 * @return a map with roles as keys and the overall count as values
	 */
	Map<Role, Long> countOverallDescendantsByCreatorId(Long creatorId);

	List<UserDTO> getDescendantsByRole(Long creatorId, Role role);
}
