package com.QuickBites.app.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.QuickBites.app.entities.UserRole;
import com.QuickBites.app.enums.RoleName;

public interface UserRoleRepository extends JpaRepository<UserRole,Integer> {
	Optional<UserRole> findByRole(RoleName role);
	
}
