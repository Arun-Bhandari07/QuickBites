package com.QuickBites.app.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.QuickBites.app.entities.User;


@Repository
public interface UserRepository extends JpaRepository<User, Integer>{
	Optional<User> findByUserName(String userName);
	Optional<User> findByEmail(String email);
	boolean existsByUserName(String userName);
	boolean existsByEmail(String email);
	
}
	

	//my rough implementation
//	public Optional<User> findByUsername(String userName) {
//		user.setUserName("Ram");
//		user.setPassword("Shyam");
//		user.setEmail("ram@gmail.com");
//		
//		UserRole myRole = new UserRole();
//		myRole.setRole(RoleName.ROLE_CUSTOMER);
//		Set<UserRole> myRoles = user.getRoles();
//		myRoles.add(myRole);
//		
//		user.setRoles(myRoles);
//		
//		return Optional.ofNullable(user);
//		
//	}

