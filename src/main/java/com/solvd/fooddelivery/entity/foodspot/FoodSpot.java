package com.solvd.fooddelivery.entity.foodspot;


import com.solvd.fooddelivery.entity.order.Order;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class FoodSpot {

    @Setter(AccessLevel.NONE)
    private long id;
    private String name;
    private String address;
    private String phoneNumber;
    private List<Menu> menus;
    private LocalTime openingTime;
    private LocalTime closingTime;
    private List<Order> orders;

    public FoodSpot(long id, String name, String address,
                    String phoneNumber, List<Menu> menus,
                    LocalTime openingTime, LocalTime closingTime) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.menus = menus;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
        this.orders = new ArrayList<>();
    }
}
