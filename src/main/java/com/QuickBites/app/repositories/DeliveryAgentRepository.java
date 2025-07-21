package com.QuickBites.app.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.QuickBites.app.entities.DeliveryAgent;

public interface DeliveryAgentRepository extends JpaRepository<DeliveryAgent, Long> {
   
	@Query("SELECT a from DeliveryAgent a WHERE a.isAvailable = true ORDER BY a.id ASC")
	Optional<DeliveryAgent> findFirstAvailableAgent();
	
}
