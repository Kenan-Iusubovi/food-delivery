package com.solvd.fooddelivery.designpatterns.listener;

import com.solvd.fooddelivery.entity.order.Order;

import java.util.ArrayList;
import java.util.List;

public class OrderEventPublisher {

    private final List<OrderEventListener> listeners = new ArrayList<>();

    public void register(OrderEventListener listener){

        listeners.add(listener);
    }

    public void notifyOrderCreated(Order order){

        for (OrderEventListener listener : listeners){
            listener.onOrderCreated(order);
        }
    }

    public void notifycationOnFinished(Order order){
        for (OrderEventListener listener : listeners){
            listener.onOrderFinished(order);
        }
    }
}
