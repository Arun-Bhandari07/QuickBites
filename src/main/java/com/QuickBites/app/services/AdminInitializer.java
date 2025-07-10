package com.QuickBites.app.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.QuickBites.app.entities.User;
import com.QuickBites.app.entities.UserRole;
import com.QuickBites.app.enums.RoleName;
import com.QuickBites.app.repositories.UserRepository;
import com.QuickBites.app.repositories.UserRoleRepository;

import jakarta.annotation.PostConstruct;

@Component
public class AdminInitializer {
	
	@Autowired
	UserRepository userRepo;
	
	@Autowired
	UserRoleRepository userRoleRepo;

	
	@PostConstruct
	public void testInitializer() {
		if(!userRepo.existsByUserName("admin")) {
			UserRole adminRole = userRoleRepo.findByRole(RoleName.ROLE_ADMIN).get();
			UserRole userRole = userRoleRepo.findByRole(RoleName.ROLE_CUSTOMER).get();
			User user = new User();
			user.setFirstName("admin");
			user.setLastName("01");
			user.setUserName("admin");
			user.setEmail("admin001@gmail.com");
			user.getRoles().add(adminRole);
			user.getRoles().add(userRole);
			
			userRepo.save(user);
		}
	}
	

	
}
