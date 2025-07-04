package com.QuickBites.app.services;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
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
			user.setPassword(BCrypt.hashpw("admin",BCrypt.gensalt()));
			user.setUserName("admin");
			user.setCreatedAt(LocalDateTime.now());
			user.setAddress("Nepal");
			user.setPhone("9866150743");
			user.setEmail("admin001@gmail.com");
			user.getRoles().add(adminRole);
			user.getRoles().add(userRole);
			
			userRepo.save(user);
		}
	}
	
//	@PostConstruct
//	public void adminInitializer() {
//		if(userRepo.count()==0) {
//			
//			
//			
//			
//			UserRole userRole = new UserRole();
//			userRole.setDescription("This is a customer");
//			userRole.setRole(RoleName.ROLE_CUSTOMER);
//			userRoleRepo.save(userRole);
//			
//			UserRole agentRole = new UserRole();
//			agentRole.setDescription("This is delivery agent");
//			agentRole.setRole(RoleName.ROLE_DELIVERYAGENT);
//			userRoleRepo.save(agentRole);
//		
//			
//		}
//	}
	
}
