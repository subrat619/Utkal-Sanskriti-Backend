package com.cyfrifpro.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cyfrifpro.model.User;
import com.cyfrifpro.model.Role;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	List<User> findByRole(Role role);

	Optional<User> findByEmail(String username);

	List<User> findAllByCreatedBy(User user);

	List<User> findByCreatedBy_UserId(Long createdById);

	List<User> findByCreatedBy_UserIdAndRole(Long creatorId, Role role);

	List<User> findByStatus(String status);

	// Custom query to fetch user by ID and validate role hierarchy
	@Query("SELECT u FROM User u WHERE u.userId = :userId AND u.role <= :currentUserRole")
	Optional<User> findLowerRoleUserById(@Param("userId") Long userId, @Param("currentUserRole") Role currentUserRole);
}
