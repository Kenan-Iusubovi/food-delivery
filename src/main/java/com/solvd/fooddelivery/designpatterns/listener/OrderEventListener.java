package com.solvd.fooddelivery.designpatterns.listener;

import com.solvd.fooddelivery.entity.order.Order;

public interface OrderEventListener {

    void onOrderCreated(Order order);

    void onOrderFinished(Order order);
}
