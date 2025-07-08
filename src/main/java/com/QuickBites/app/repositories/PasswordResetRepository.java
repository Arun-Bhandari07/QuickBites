package com.QuickBites.app.repositories;

import com.QuickBites.app.entities.PasswordReset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PasswordResetRepository extends JpaRepository<PasswordReset, Integer> {

    // Finds a request by the user's ID and the OTP they provided.
    Optional<PasswordReset> findByUserIdAndOtp(int userId, String otp);
    
    // Deletes any existing requests for a user to ensure only one is active at a time.
    void deleteByUserId(int userId);
}