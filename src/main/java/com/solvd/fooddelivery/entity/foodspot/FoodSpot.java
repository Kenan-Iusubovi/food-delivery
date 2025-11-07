package com.solvd.fooddelivery.entity.foodspot;


import com.solvd.fooddelivery.entity.order.Order;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class FoodSpot {

    private Long id;
    private String name;
    private String address;
    private String phoneNumber;
    private List<Menu> menus = new ArrayList<>();
    private LocalTime openingTime;
    private LocalTime closingTime;
    private List<Order> orders = new ArrayList<>();
}

