package com.QuickBites.app.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.QuickBites.app.entities.RoleName;
import com.QuickBites.app.entities.UserRole;

public interface UserRoleRepository extends JpaRepository<UserRole,Integer> {
	Optional<UserRole> findByRole(RoleName role);
	
}
