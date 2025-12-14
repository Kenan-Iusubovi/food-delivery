package com.solvd.fooddelivery.service;

import com.solvd.fooddelivery.entity.human.FoodSpotOwner;

import java.util.List;
import java.util.Optional;

public interface FoodSpotOwnerService {

    FoodSpotOwner create(FoodSpotOwner owner);

    FoodSpotOwner update(FoodSpotOwner owner);

    Optional<FoodSpotOwner> findById(Long id);

    List<FoodSpotOwner> findAll();

    void deleteById(Long id);
}
