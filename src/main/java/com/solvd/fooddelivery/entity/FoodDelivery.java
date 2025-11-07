package com.solvd.fooddelivery.entity;

import com.solvd.fooddelivery.entity.human.FoodSpotOwner;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FoodDelivery {

    private Set<FoodSpotOwner> foodSpotOwners = new HashSet<>();
}