package com.QuickBites.app.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.QuickBites.app.entities.FoodItem;

@Repository
public interface FoodItemRepository extends JpaRepository<FoodItem,Long>{
		boolean existsByNameIgnoreCase(String name);
		
		@Query("SELECT f from FoodItem f LEFT JOIN FETCH f.foodVariants")
		List<FoodItem> findAllWithVariants();
}
