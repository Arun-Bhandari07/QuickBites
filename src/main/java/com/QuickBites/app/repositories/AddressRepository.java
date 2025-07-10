package com.QuickBites.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.QuickBites.app.entities.Address;

public interface AddressRepository extends JpaRepository<Address,Long>{

	
	
}
