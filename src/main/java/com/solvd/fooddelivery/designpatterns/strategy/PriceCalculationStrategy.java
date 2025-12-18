package com.solvd.fooddelivery.designpatterns.strategy;

import com.solvd.fooddelivery.entity.order.Order;

import java.math.BigDecimal;

public interface PriceCalculationStrategy {

    BigDecimal calculate(Order order);
}
