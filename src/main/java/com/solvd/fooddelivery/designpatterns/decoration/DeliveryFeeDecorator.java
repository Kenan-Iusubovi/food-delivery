package com.solvd.fooddelivery.designpatterns.decoration;

import com.solvd.fooddelivery.designpatterns.strategy.PriceCalculationStrategy;
import com.solvd.fooddelivery.entity.order.Order;

import java.math.BigDecimal;

public class DeliveryFeeDecorator extends PriceCalculationDecorator{

    private static final BigDecimal DELIVERY_FEE = new BigDecimal("3");

    protected DeliveryFeeDecorator(PriceCalculationStrategy delegate) {
        super(delegate);
    }

    @Override
    public BigDecimal calculate(Order order) {
        return super.calculate(order).add(DELIVERY_FEE);
    }
}
