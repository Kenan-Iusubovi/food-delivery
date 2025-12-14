package com.solvd.fooddelivery.service;

import com.solvd.fooddelivery.entity.foodspot.FoodSpot;

import java.util.List;
import java.util.Optional;

public interface FoodSpotService {

    FoodSpot create(FoodSpot foodSpot);

    FoodSpot update(FoodSpot foodSpot);

    Optional<FoodSpot> findById(Long id);

    List<FoodSpot> findAll();

    void deleteById(Long id);
}
