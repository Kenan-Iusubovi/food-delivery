package com.solvd.fooddelivery.designpatterns.decoration;

import com.solvd.fooddelivery.designpatterns.strategy.PriceCalculationStrategy;
import com.solvd.fooddelivery.entity.order.Order;

import java.math.BigDecimal;

public class SubscriptionDiscountDecorator extends PriceCalculationDecorator{

    private static final BigDecimal DISCOUNT = new BigDecimal("0.5");

    protected SubscriptionDiscountDecorator(PriceCalculationStrategy delegate) {
        super(delegate);
    }

    @Override
    public BigDecimal calculate(Order order) {

        BigDecimal price = super.calculate(order);

        if (order.getCustomer().isSubscription()){
            return price.multiply(DISCOUNT);
        }
        return price;
    }
}
