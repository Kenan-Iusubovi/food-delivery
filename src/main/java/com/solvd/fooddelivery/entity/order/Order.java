package com.solvd.fooddelivery.entity.order;

import com.solvd.fooddelivery.entity.foodspot.Product;
import com.solvd.fooddelivery.entity.foodspot.FoodSpot;
import com.solvd.fooddelivery.entity.human.Customer;
import com.solvd.fooddelivery.entity.human.courier.Courier;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    private Long id;
    private UUID orderNumber;
    private Courier courier;
    private BigDecimal totalPrice;
    private List<Product> products;
    private String takeAddress;
    private String bringAddress;
    private boolean finished;
    private LocalDateTime orderDateTime;
    private Customer customer;
}
