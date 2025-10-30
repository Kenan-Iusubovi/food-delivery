package com.solvd.domain.foodspot;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Menu {

    private long id;
    private String name;
    private FoodSpot foodSpot;
    private List<Dish> dishes;
}
