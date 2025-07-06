package com.QuickBites.app.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.QuickBites.app.entities.Cart;

public interface CartRepository extends JpaRepository<Cart,Long>{
	@Query("SELECT c FROM Cart c JOIN FETCH c.items WHERE c.user.userName=:username")
	Optional<Cart> fetchCartWithItems(String username);
	Optional<Cart> findByUserUserName(String username);
//	boolean existsByUsername(String username);
}
