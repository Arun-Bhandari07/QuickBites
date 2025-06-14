package com.QuickBites.app.repositories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.QuickBites.app.entities.OTPVerification;

@Repository
public interface OTPrepository extends JpaRepository<OTPVerification, Integer>{
			void deleteByEmail(String email);
			Optional<OTPVerification> findByEmail(String email);
			List<OTPVerification> findAllByEmail(String email);
			List<OTPVerification> findByExpiryAtBefore(LocalDateTime time);
}
