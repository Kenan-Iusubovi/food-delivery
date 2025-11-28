package com.solvd.fooddelivery.repository;

import com.solvd.fooddelivery.entity.foodspot.FoodSpot;

import java.util.List;

public interface FoodSpotRepository {

    List<FoodSpot> findByOwnerId(Long ownerId);

    List<FoodSpot> findByName(String namePattern);
}
