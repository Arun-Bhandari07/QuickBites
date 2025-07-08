package com.QuickBites.app.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.QuickBites.app.UserExistence;
import com.QuickBites.app.entities.PendingUser;

public interface PendingUserRepository extends JpaRepository<PendingUser,Integer> {
	@Query("SELECT p.userName AS userName, p.email AS email FROM PendingUser p WHERE p.userName=:userName OR email=:email")
	List<UserExistence> findConflicts(@Param("userName")String userName,@Param("email") String email);
		Optional<PendingUser> findByEmail(String email);
		boolean existsByEmail(String email);
}
