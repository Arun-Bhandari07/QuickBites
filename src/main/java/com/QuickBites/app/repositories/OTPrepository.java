package com.QuickBites.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.QuickBites.app.entities.OTPVerification;

public interface OTPrepository extends JpaRepository<OTPVerification, Integer>{
			
}
