package com.cyfrifpro.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cyfrifpro.DTO.UserDTO;
import com.cyfrifpro.model.Role;
import com.cyfrifpro.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	List<User> findByRole(Role role);

	Optional<User> findByEmail(String username);

	List<User> findAllByCreatedBy(User user);

	List<User> findByCreatedBy_UserId(Long createdById);

	List<User> findByCreatedBy_UserIdAndRole(Long creatorId, Role role);

	List<User> findByStatus(String status);

	long countByRole(Role role);

	@Query("SELECT u.role, COUNT(u) FROM User u WHERE u.createdBy.userId = :creatorId GROUP BY u.role")
	List<Object[]> countByCreatedByGroupByRole(@Param("creatorId") Long creatorId);

	@Query("SELECT COUNT(u) FROM User u WHERE u.createdBy.userId = :creatorId")
	Long countByCreatedByUserId(@Param("creatorId") Long creatorId);

	/**
	 * Recursively retrieves all descendant users (directly and indirectly) created
	 * by the specified user and groups them by their role.
	 *
	 * @param creatorId the ID of the creator user
	 * @return a map where the key is the Role and the value is a list of UserDTOs
	 */

	/**
	 * Recursively fetch all descendant users (children, grandchildren, etc.)
	 * created by the given creatorId that match the specified role.
	 *
	 * @param creatorId the ID of the parent user
	 * @param role      the role to filter descendant users
	 * @return list of UserDTOs matching the specified role among all descendants
	 */
//	List<UserDTO> getDescendantsByRole(Long creatorId, Role role);

	// Custom query to fetch user by ID and validate role hierarchy
	@Query("SELECT u FROM User u WHERE u.userId = :userId AND u.role <= :currentUserRole")
	Optional<User> findLowerRoleUserById(@Param("userId") Long userId, @Param("currentUserRole") Role currentUserRole);
}
