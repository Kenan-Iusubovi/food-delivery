package com.solvd.fooddelivery.designpatterns.strategy;

import com.solvd.fooddelivery.entity.order.Order;

import java.math.BigDecimal;

public class SubscriptionDiscountPriceStrategy implements PriceCalculationStrategy{

    private static final BigDecimal DISCOUNT = new BigDecimal("0.7");

    @Override
    public BigDecimal calculate(Order order) {

        BigDecimal basePrice = order.getProducts().stream()
                .map(product -> product.getPrice())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (order.getCustomer().isSubscription()){
            return basePrice.multiply(DISCOUNT);
        }
        return basePrice;
    }
}
