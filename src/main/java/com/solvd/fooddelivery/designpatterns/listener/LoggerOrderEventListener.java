package com.solvd.fooddelivery.designpatterns.listener;

import com.solvd.fooddelivery.entity.order.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerOrderEventListener implements OrderEventListener{

    private static final Logger log = LoggerFactory.getLogger(LoggerOrderEventListener.class);

    @Override
    public void onOrderCreated(Order order) {
        log.info("Order created: {}", order.getOrderNumber());
    }

    @Override
    public void onOrderFinished(Order order) {
        log.info("Order finished: {}", order.getOrderNumber() );
    }
}
