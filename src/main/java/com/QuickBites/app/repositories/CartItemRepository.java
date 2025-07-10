package com.QuickBites.app.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.QuickBites.app.entities.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem,Long> {
		
	Optional<CartItem> findByCartIdAndFoodItemIdAndVariantId(Long cartId,Long foodItemId,Long VariantId);
		List<CartItem>	findAllByCartId(Long cartId);
		Optional<CartItem> findByIdAndCartUserUserName(Long id, String username);
		void deleteByCartId(Long cartId);
}
