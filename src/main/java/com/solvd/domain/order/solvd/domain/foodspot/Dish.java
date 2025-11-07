package com.solvd.domain.order.solvd.domain.foodspot;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Dish {

    private long id;
    private String name;
    private double price;
    private String description;
    private boolean available;

}
