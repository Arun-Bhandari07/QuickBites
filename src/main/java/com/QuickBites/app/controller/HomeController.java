package com.QuickBites.app.controller;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping("/public")
@Tag(name="Home Controller")
public class HomeController {

	@GetMapping("/test")
	public String testingPublic() {
		return "Testing purppose";
	}
	
	@GetMapping("/currentUser")
	public String getCurrentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails)authentication.getPrincipal();
		return userDetails.getUsername()+userDetails.getAuthorities();	
	}

}
