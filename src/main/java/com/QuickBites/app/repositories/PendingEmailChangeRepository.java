package com.QuickBites.app.repositories;

import com.QuickBites.app.entities.PendingEmailChange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PendingEmailChangeRepository extends JpaRepository<PendingEmailChange, Integer> {

    Optional<PendingEmailChange> findByUserIdAndOtp(int userId, String otp);
    
    void deleteByUserId(int userId);
}