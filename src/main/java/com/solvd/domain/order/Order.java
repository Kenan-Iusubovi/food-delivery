package com.solvd.domain.order;

import com.solvd.domain.foodspot.Dish;
import com.solvd.domain.foodspot.FoodSpot;
import com.solvd.domain.human.Courier;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class Order {

    private long id;
    private UUID orderNumber;
    private FoodSpot foodSpot;
    private Courier courier;
    private double totalPrice;
    private List<Dish> dishes;
    private String takeAddress;
    private String bringAddress;

}
