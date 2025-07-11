package com.QuickBites.app.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.QuickBites.app.entities.Address;

public interface AddressRepository extends JpaRepository<Address,Long>{
	 List<Address> findByUserUserName(String username);
	Optional<Address> findByIdAndUserId(Long addressId,int userId );
	Optional<Address>   findByIdAndUserUserName(Long addressId, String userName);
	
}
