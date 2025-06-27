package com.QuickBites.app.services;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.QuickBites.app.entities.User;

public class CustomUserDetails implements UserDetails {

	private final User user;
	
	public CustomUserDetails(User user) {
		this.user = user;
	}
	
	@Override
	public String getUsername() {
		return user.getUserName();
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities(){
		return user.getRoles()
				.stream()
				.map(role->new SimpleGrantedAuthority(role.getRole().name()))
				.collect(Collectors.toList());
	}
	
	@Override
	public String getPassword() {
		return user.getPassword();
	}
	
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}
	
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}
	
}
