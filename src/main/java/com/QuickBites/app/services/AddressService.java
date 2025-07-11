package com.QuickBites.app.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.QuickBites.app.DTO.AddressRequestDTO;
import com.QuickBites.app.DTO.AddressResponseDTO;
import com.QuickBites.app.Exception.ResourceNotFoundException;
import com.QuickBites.app.entities.Address;
import com.QuickBites.app.entities.User;
import com.QuickBites.app.repositories.AddressRepository;
import com.QuickBites.app.repositories.UserRepository;

@Service
public class AddressService {

    @Autowired
    private AddressRepository addressRepo;

    @Autowired
    private UserRepository userRepo;

    
    
    public AddressResponseDTO saveAddress(AddressRequestDTO dto, String username) {
        User user = userRepo.findByUserName(username)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Address address = new Address();
        address.setUser(user);
        address.setTitle(dto.getTitle());
        address.setFullAddress(dto.getFullAddress());
        address.setLatitude(dto.getLatitude());
        address.setLongitude(dto.getLongitude());

        Address saved = addressRepo.save(address);
        return new AddressResponseDTO(saved.getId(), saved.getTitle(), saved.getFullAddress(), saved.getLatitude(), saved.getLongitude());
    }

    
    
    public List<AddressResponseDTO> getAddressesForUser(String username) {
        List<Address> addresses = addressRepo.findByUserUserName(username);
        return addresses.stream()
                .map(addr -> new AddressResponseDTO(addr.getId(), addr.getTitle(), addr.getFullAddress(),
                        addr.getLatitude(), addr.getLongitude()))
                .collect(Collectors.toList());
    }

    
    
    public void deleteAddress(Long addressId, String username) {
        Address address = addressRepo.findByIdAndUserUserName(addressId, username)
            .orElseThrow(() -> new AccessDeniedException("Address not found or not owned by user"));

        addressRepo.delete(address);
    }
}

