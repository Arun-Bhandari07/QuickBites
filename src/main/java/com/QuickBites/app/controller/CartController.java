package com.QuickBites.app.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.QuickBites.app.DTO.AddToCartRequest;
import com.QuickBites.app.DTO.CartItemResponseDTO;
import com.QuickBites.app.DTO.CartResponseDTO;
import com.QuickBites.app.DTO.CartUpdateRequest;
import com.QuickBites.app.services.CartService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/cart")
@Tag(name="Cart Operations", description="Add,Update,Remove Items to Cart")
public class CartController {

	
    private  CartService cartService;
    
    
    public CartController(CartService cartService) {
    	this.cartService= cartService;
    }

    @GetMapping
    public ResponseEntity<CartResponseDTO> getCart(Authentication authentication) {
        String username = authentication.getName();
        CartResponseDTO cart = cartService.getCartWithTotal(username);
        return ResponseEntity.ok(cart);
    }

    @PostMapping("/add")
    public ResponseEntity<CartItemResponseDTO> addToCart(@Valid @RequestBody AddToCartRequest request,
                                          Authentication authentication) {
    	 CartItemResponseDTO addedItemDTO = cartService.addItemToCart(request, authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(addedItemDTO);
    }

    @PutMapping("/update")
    public ResponseEntity<Void> updateItem(@Valid @RequestBody CartUpdateRequest req, Authentication auth) {
        cartService.updateCartItem(req, auth.getName());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/remove/{itemId}")
    public ResponseEntity<Void> removeItem(@PathVariable("itemId") Long itemId, Authentication authentication) {
        cartService.removeItem(itemId, authentication.getName());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/clear")
    public ResponseEntity<Void> clearCart(Authentication authentication) {
        cartService.clearCart(authentication.getName());
        return ResponseEntity.noContent().build();
    }
}