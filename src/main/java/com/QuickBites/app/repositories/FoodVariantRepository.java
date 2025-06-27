package com.QuickBites.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.QuickBites.app.entities.FoodVariant;

@Repository
public interface FoodVariantRepository extends JpaRepository<FoodVariant,Integer> {

}
