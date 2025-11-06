package com.solvd.fooddelivery.entity.foodspot;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Menu {

    private Long id;
    private String name;
    private List<Product> products;
}
