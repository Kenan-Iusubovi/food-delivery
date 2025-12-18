package com.solvd.fooddelivery.designpatterns.abstractfactory;

import com.solvd.fooddelivery.designpatterns.strategy.NotificationStrategy;
import com.solvd.fooddelivery.designpatterns.strategy.PriceCalculationStrategy;
import com.solvd.fooddelivery.entity.order.Order;

public interface OrderProcessingFactory {

    PriceCalculationStrategy createPriceCalculationStrategy(Order order);

    NotificationStrategy createNotificationStrategy();
}
