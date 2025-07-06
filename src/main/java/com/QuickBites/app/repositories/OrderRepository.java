package com.QuickBites.app.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.QuickBites.app.entities.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUserUserName(String username); // for order history

    Optional<Order> findByIdAndUserUserName(Long orderId, String username);
}