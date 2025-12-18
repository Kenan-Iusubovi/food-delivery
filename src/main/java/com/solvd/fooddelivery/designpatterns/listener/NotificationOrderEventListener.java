package com.solvd.fooddelivery.listener;

import com.solvd.fooddelivery.designpatterns.listener.OrderEventListener;
import com.solvd.fooddelivery.designpatterns.strategy.NotificationStrategy;
import com.solvd.fooddelivery.entity.order.Order;


public class NotificationOrderEventListener implements OrderEventListener {

    private final NotificationStrategy notificationStrategy;

    public NotificationOrderEventListener(NotificationStrategy notificationStrategy) {
        this.notificationStrategy = notificationStrategy;
    }

    @Override
    public void onOrderCreated(Order order) {
        notificationStrategy.notify(
                "Order created: " + order.getOrderNumber());
    }

    @Override
    public void onOrderFinished(Order order) {
        notificationStrategy.notify(
                "Order finished: " + order.getOrderNumber());
    }
}
