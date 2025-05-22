package com.QuickBites.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.QuickBites.app.entities.DeliveryAgent;

public interface DeliveryAgentRepository extends JpaRepository<DeliveryAgent, Integer> {
	
}
