package com.solvd.fooddelivery.designpatterns.abstractfactory;

import com.solvd.fooddelivery.designpatterns.factory.PriceCalculationStrategyFactory;
import com.solvd.fooddelivery.designpatterns.strategy.LoggerNotificationStrateggy;
import com.solvd.fooddelivery.designpatterns.strategy.NotificationStrategy;
import com.solvd.fooddelivery.designpatterns.strategy.PriceCalculationStrategy;
import com.solvd.fooddelivery.entity.order.Order;

public class DefaultOrderProcessingFactory implements OrderProcessingFactory{


    @Override
    public PriceCalculationStrategy createPriceCalculationStrategy(Order order) {
        return PriceCalculationStrategyFactory.create(order);
    }

    @Override
    public NotificationStrategy createNotificationStrategy() {
        return new LoggerNotificationStrateggy();
    }
}
