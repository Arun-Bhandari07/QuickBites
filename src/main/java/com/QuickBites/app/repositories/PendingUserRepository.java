package com.QuickBites.app.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.QuickBites.app.entities.PendingUser;

public interface PendingUserRepository extends JpaRepository<PendingUser,Integer> {
		Optional<PendingUser> findByEmail(String email);
		boolean existsByEmail(String email);
}
