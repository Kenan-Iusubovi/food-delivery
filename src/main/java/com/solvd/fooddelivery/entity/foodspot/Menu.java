package com.solvd.fooddelivery.entity.foodspot;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;

import java.util.List;

@Data
@AllArgsConstructor
public class Menu {

    @Setter(AccessLevel.NONE)
    private long id;
    private String name;
    private List<Product> products;
}
