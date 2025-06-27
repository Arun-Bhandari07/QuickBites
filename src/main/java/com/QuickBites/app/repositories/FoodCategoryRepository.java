package com.QuickBites.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.QuickBites.app.entities.FoodCategory;

@Repository
public interface FoodCategoryRepository extends JpaRepository<FoodCategory,Long> {
	boolean existsByNameIgnoreCase(String categoryName);
}
