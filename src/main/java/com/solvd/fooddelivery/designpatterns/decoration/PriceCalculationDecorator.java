package com.solvd.fooddelivery.designpatterns.decoration;

import com.solvd.fooddelivery.designpatterns.strategy.PriceCalculationStrategy;
import com.solvd.fooddelivery.entity.order.Order;


import java.math.BigDecimal;

public abstract class PriceCalculationDecorator implements PriceCalculationStrategy {

    protected final PriceCalculationStrategy delegate;

    protected PriceCalculationDecorator(PriceCalculationStrategy delegate) {
        this.delegate = delegate;
    }

    @Override
    public BigDecimal calculate(Order order) {
        return delegate.calculate(order);
    }
}
