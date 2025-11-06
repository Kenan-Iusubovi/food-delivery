package com.solvd.fooddelivery.entity.foodspot;

import com.sun.java.accessibility.util.AccessibilityListenerList;
import lombok.*;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    private Long id;
    private String name;
    private BigDecimal price;
    private String description;
    private boolean available;
}
