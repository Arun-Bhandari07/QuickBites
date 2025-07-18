package com.QuickBites.app.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.QuickBites.app.entities.DeliveryAgent;

public interface DeliveryAgentRepository extends JpaRepository<DeliveryAgent, Long> {
    List<DeliveryAgent> findByIsActive(boolean isActive);
    
    @Query("SELECT d FROM DeliveryAgent d WHERE d.isActive = true AND d.lastSeen >= :cutoff")
    List<DeliveryAgent> findOnlineAgents(@Param("cutoff") LocalDateTime cutoff);
}
