package com.solvd.fooddelivery.entity.foodspot;

import com.solvd.fooddelivery.entity.ProductContainer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Menu implements ProductContainer {

    private Long id;
    private String name;
    private List<Product> products = new ArrayList<>();
}
