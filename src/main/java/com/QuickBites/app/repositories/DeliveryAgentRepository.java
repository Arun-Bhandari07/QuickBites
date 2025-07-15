package com.QuickBites.app.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.QuickBites.app.entities.DeliveryAgent;

public interface DeliveryAgentRepository extends JpaRepository<DeliveryAgent, Integer> {
    List<DeliveryAgent> findByIsActive(boolean isActive);
}
