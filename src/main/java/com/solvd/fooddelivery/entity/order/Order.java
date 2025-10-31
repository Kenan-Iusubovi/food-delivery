package com.solvd.fooddelivery.entity.order;

import com.solvd.fooddelivery.entity.foodspot.Product;
import com.solvd.fooddelivery.entity.foodspot.FoodSpot;
import com.solvd.fooddelivery.entity.human.Courier;
import com.solvd.fooddelivery.entity.human.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class Order {

    private long id;
    private UUID orderNumber;
    private FoodSpot foodSpot;
    private Courier courier;
    private User user;
    private double totalPrice;
    private List<Product> products;
    private String takeAddress;
    private String bringAddress;
    private boolean finished;
    private LocalDateTime orderDateTime;
}
