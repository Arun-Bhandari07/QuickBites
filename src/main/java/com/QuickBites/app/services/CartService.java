package com.QuickBites.app.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.QuickBites.app.DTO.AddToCartRequest;
import com.QuickBites.app.DTO.CartItemResponseDTO;
import com.QuickBites.app.DTO.CartResponseDTO;
import com.QuickBites.app.DTO.CartUpdateRequest;
import com.QuickBites.app.Exception.ResourceNotFoundException;
import com.QuickBites.app.Exception.UserNotFoundException;
import com.QuickBites.app.entities.Cart;
import com.QuickBites.app.entities.CartItem;
import com.QuickBites.app.entities.FoodItem;
import com.QuickBites.app.entities.FoodVariant;
import com.QuickBites.app.entities.User;
import com.QuickBites.app.repositories.CartItemRepository;
import com.QuickBites.app.repositories.CartRepository;
import com.QuickBites.app.repositories.FoodItemRepository;
import com.QuickBites.app.repositories.FoodVariantRepository;
import com.QuickBites.app.repositories.UserRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CartService {
	@Autowired
	 private  CartRepository cartRepo;
	@Autowired
	 private  CartItemRepository cartItemRepo;
	@Autowired
	 private  UserRepository userRepo;
	@Autowired
	 private  FoodItemRepository foodItemRepo;
	@Autowired
	 private  FoodVariantRepository foodVariantRepo;

	
	public Cart getCartForUser(String username) {
		return cartRepo.findByUserUserName(username)
				.orElseGet(()->createCartForUser(username));
	}
	
	
	private Cart createCartForUser(String username) {
		User user = userRepo.findByUserName(username)
				.orElseThrow(()-> new UserNotFoundException("Cannot find User"));
		Cart cart = new Cart();
		cart.setUser(user);
		return cartRepo.save(cart);
	}
	
	
	
	public CartItemResponseDTO  addItemToCart(AddToCartRequest request, String username) {
		Cart cart = getCartForUser(username);
		
	FoodItem foodItem = foodItemRepo.findById(request.getFoodItemId())
							.orElseThrow(()->new ResourceNotFoundException("Food Item doesn't exist to add to cart"));
	
	FoodVariant variant = null;
    if (request.getFoodVariantId() != null) {
        variant = foodVariantRepo.findById(request.getFoodVariantId())
                .orElseThrow(() -> new ResourceNotFoundException("Variant not found"));
    }

    Optional<CartItem> existingItem = cartItemRepo
            .findByCartIdAndFoodItemIdAndVariantId(cart.getId(), foodItem.getId(),
                    variant != null ? variant.getId() : null);

    CartItem savedItem;
    if (existingItem.isPresent()) {
        CartItem item = existingItem.get();
        item.setQuantity(item.getQuantity() + request.getQuantity());
        savedItem = cartItemRepo.save(item);
    } else {
        CartItem newItem = new CartItem();
        newItem.setCart(cart);
        newItem.setFoodItem(foodItem);
        newItem.setVariant(variant);
        newItem.setQuantity(request.getQuantity());
        savedItem = cartItemRepo.save(newItem);
    }
    String foodName = savedItem.getFoodItem().getName();
    String variantName = (savedItem.getVariant() != null) ? savedItem.getVariant().getName() : "Default";
    BigDecimal unitPrice = savedItem.getFoodItem().getPrice(); // Consider using variant override logic if needed
    int quantity = savedItem.getQuantity();
    BigDecimal subtotal = unitPrice.multiply(BigDecimal.valueOf(quantity));

    return new CartItemResponseDTO(
        savedItem.getId(),
        foodName,
        variantName,
        unitPrice,
        quantity,
        subtotal
    );
    
    
}
	public void updateCartItem(CartUpdateRequest request, String username) {
	    Cart cart = getCartForUser(username);

	    CartItem item = cartItemRepo.findById(request.getCartItemId())
	        .orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));

	    if (!item.getCart().getId().equals(cart.getId())) {
	        throw new AccessDeniedException("Item does not belong to user's cart");
	    }

	    int quantity = request.getQuantity();
	    if (quantity <= 0) {
	        cartItemRepo.delete(item); 
	    } else {
	        item.setQuantity(quantity);
	        cartItemRepo.save(item);
	    }
	}
	
	public void removeItem(Long cartItemId, String username) {
	    CartItem item = cartItemRepo.findByIdAndCartUserUserName(cartItemId, username)
	        .orElseThrow(() -> new AccessDeniedException("Item not found in your cart"));

	    cartItemRepo.delete(item);
	}
	
	public void clearCart(String username) {
	    Cart cart = cartRepo.findByUserUserName(username)
	            .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

	    cartItemRepo.deleteByCartId(cart.getId());
	}
	
	public CartResponseDTO getCartWithTotal(String username) {
	    Cart cart = getCartForUser(username); // already exists
	    List<CartItem> items = cart.getItem();

	    List<CartItemResponseDTO> itemDTOs = items.stream().map(item -> {
	        String foodName = item.getFoodItem().getName();
	        String variantName = (item.getVariant() != null) ? item.getVariant().getName() : "Default";
	        BigDecimal unitPrice = item.getFoodItem().getPrice(); // Adjust if variant overrides price
	        int quantity = item.getQuantity();
	        BigDecimal subtotal = unitPrice.multiply(BigDecimal.valueOf(quantity));

	        return new CartItemResponseDTO(
	                item.getId(),
	                foodName,
	                variantName,
	                unitPrice,
	                quantity,
	                subtotal
	        );
	    }).toList();

	    BigDecimal totalPrice = itemDTOs.stream()
	            .map(CartItemResponseDTO::getSubtotal)
	            .reduce(BigDecimal.ZERO, BigDecimal::add);

	    return new CartResponseDTO(itemDTOs, totalPrice);
	}
	
}
