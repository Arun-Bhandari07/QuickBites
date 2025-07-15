package com.QuickBites.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.QuickBites.app.DTO.AddressRequestDTO;
import com.QuickBites.app.DTO.AddressResponseDTO;
import com.QuickBites.app.services.AddressService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/user/addresses")
@Tag(name="Address Access", description = "Access address of User")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @PostMapping
    public ResponseEntity<AddressResponseDTO> saveAddress(@Valid @RequestBody AddressRequestDTO dto,
                                                          Authentication authentication) {
        String username = authentication.getName();
        AddressResponseDTO savedAddress = addressService.saveAddress(dto, username);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedAddress);
    }

    @GetMapping
    public ResponseEntity<List<AddressResponseDTO>> getUserAddresses(Authentication authentication) {
        String username = authentication.getName();
        List<AddressResponseDTO> addresses = addressService.getAddressesForUser(username);
        return ResponseEntity.ok(addresses);
    }

    @DeleteMapping("/{addressId}")
    public ResponseEntity<Void> deleteAddress(@PathVariable(name="addressId") Long addressId,
                                              Authentication authentication) {
        String username = authentication.getName();
        addressService.deleteAddress(addressId, username);
        return ResponseEntity.noContent().build();
    }
}

