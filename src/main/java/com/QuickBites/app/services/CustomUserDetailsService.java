package com.QuickBites.app.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import com.QuickBites.app.entities.User;
import com.QuickBites.app.repositories.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {
	
	public CustomUserDetailsService() {}

	@Autowired
	UserRepository userRepo;
	
	@Override
	public UserDetails loadUserByUsername(String userName){
		 User user = userRepo.findByUserName(userName)
				.orElseThrow(()-> new UsernameNotFoundException("Cannot find username:"+userName));        
		return new CustomUserDetails(user);
	}
	
}
