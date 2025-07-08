package com.QuickBites.app.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.QuickBites.app.UserExistence;
import com.QuickBites.app.entities.User;


@Repository
public interface UserRepository extends JpaRepository<User, Integer>{
	@Query("SELECT u.userName AS userName, u.email AS email FROM User u WHERE u.userName=:userName OR u.email=:email")
	List<UserExistence> findConflicts(@Param("userName")String userName, @Param("email") String email);
	Optional<User> findByUserName(String userName);
	Optional<User> findByEmail(String email);
	boolean existsByUserName(String userName);
	boolean existsByEmail(String email);
	
}
	



