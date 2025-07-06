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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.QuickBites.app.DTO.AddToCartRequest;
import com.QuickBites.app.DTO.CartResponseDTO;
import com.QuickBites.app.services.CartService;

@RestController
@RequestMapping("/api/v1/cart")
public class CartController {

    private  CartService cartService;

    @GetMapping
    public ResponseEntity<CartResponseDTO> getCart(Authentication authentication) {
        String username = authentication.getName();
        CartResponseDTO cart = cartService.getCartWithTotal(username);
        return ResponseEntity.ok(cart);
    }

    @PostMapping("/add")
    public ResponseEntity<Void> addToCart(@RequestBody AddToCartRequest request,
                                          Authentication authentication) {
        cartService.addItemToCart(request, authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/update/{itemId}")
    public ResponseEntity<Void> updateQuantity(@PathVariable Long itemId,
                                               @RequestParam int quantity) {
        cartService.updateItemQuantity(itemId, quantity);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/remove/{itemId}")
    public ResponseEntity<Void> removeItem(@PathVariable Long itemId) {
        cartService.removeItem(itemId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/clear")
    public ResponseEntity<Void> clearCart(Authentication authentication) {
        cartService.clearCart(authentication.getName());
        return ResponseEntity.noContent().build();
    }
}