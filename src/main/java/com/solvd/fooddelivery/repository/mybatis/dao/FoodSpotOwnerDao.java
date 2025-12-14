package com.solvd.fooddelivery.repository.mybatis.dao;

import com.solvd.fooddelivery.entity.human.FoodSpotOwner;
import java.util.List;

public interface FoodSpotOwnerDao {

    int create(FoodSpotOwner owner);

    int update(FoodSpotOwner owner);

    FoodSpotOwner findById(Long id);

    List<FoodSpotOwner> findAll();

    int deleteById(Long id);

    FoodSpotOwner findByEmail(String email);

    FoodSpotOwner findByPhoneNumber(String phoneNumber);
}
