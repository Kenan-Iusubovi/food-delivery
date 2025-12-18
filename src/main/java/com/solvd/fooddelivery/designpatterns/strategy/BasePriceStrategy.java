package com.solvd.fooddelivery.designpatterns.strategy;

import com.solvd.fooddelivery.entity.order.Order;

import java.math.BigDecimal;

public class BasePriceStrategy implements PriceCalculationStrategy {

    @Override
    public BigDecimal calculate(Order order) {
        return order.getProducts().stream()
                .map(product -> product.getPrice())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
