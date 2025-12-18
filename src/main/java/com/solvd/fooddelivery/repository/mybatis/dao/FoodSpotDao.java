package com.solvd.fooddelivery.repository.mybatis.dao;

import com.solvd.fooddelivery.entity.foodspot.FoodSpot;

import java.util.List;

public interface FoodSpotDao {

    int create(FoodSpot foodSpot);

    int update(FoodSpot foodSpot);

    FoodSpot findById(Long id);

    List<FoodSpot> findAll();

    int deleteById(Long id);
}
