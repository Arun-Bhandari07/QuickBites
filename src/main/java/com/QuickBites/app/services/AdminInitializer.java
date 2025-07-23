package com.QuickBites.app.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.QuickBites.app.Exception.ResourceNotFoundException;
import com.QuickBites.app.entities.Address;
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

	@Autowired
	OTPService otpService;
	
	@Autowired
	DeliveryRouteService deliveryChargeService;
	
	@PostConstruct
	public void test() {
		deliveryChargeService.calculateDeliveryChargeAndTime(27.678785243594245,83.4647827158915 );
	}
	
	
	@PostConstruct
	public void testInitializer() {
		String userName = "Kitchen_Staff";
		if(!userRepo.existsByUserName(userName)) {
			UserRole userRole = userRoleRepo.findByRole(RoleName.ROLE_RESTURANTSTAFF)
					.orElseThrow(()->new ResourceNotFoundException("Cannot find user ROle"));
			
			
			
			
			
			Address address = new Address();
			address.setTitle("Official Resturant Location");
			address.setLatitude(83.4642);
			address.setLongitude(27.6883);
			address.setFullAddress("Butwal, Milanchowk");
			
			
			User user = new User();
			user.setUserName(userName);
			user.setFirstName("Kitchen");
			user.setLastName("Staff");
		
		
			user.getRoles().add(userRole);
			user.getAddress().add(address);
			userRepo.save(user);
		}
		
		
		
	
	}
	

	
}
