package com.solvd.fooddelivery.designpatterns.factory;

import com.solvd.fooddelivery.designpatterns.strategy.BasePriceStrategy;
import com.solvd.fooddelivery.designpatterns.strategy.PriceCalculationStrategy;
import com.solvd.fooddelivery.designpatterns.strategy.SubscriptionDiscountPriceStrategy;
import com.solvd.fooddelivery.entity.order.Order;

public class PriceCalculationStrategyFactory {

    private PriceCalculationStrategyFactory() {
    }

    public static PriceCalculationStrategy create(Order order){

        if (order == null) {
            throw new IllegalArgumentException("Order must not be null");
        }

        if (order.getCustomer() == null) {
            throw new IllegalStateException("Order must have a customer");
        }

        if (order.getProducts() == null || order.getProducts().isEmpty()){
            throw new RuntimeException("Order has no products");
        }

        if (order.getCustomer().isSubscription()){
            return new SubscriptionDiscountPriceStrategy();
        }
        return new BasePriceStrategy();
    }
}
