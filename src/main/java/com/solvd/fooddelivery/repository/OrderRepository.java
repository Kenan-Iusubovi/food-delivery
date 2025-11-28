package com.solvd.fooddelivery.repository;

import com.solvd.fooddelivery.entity.order.Order;

import java.util.List;

public interface OrderRepository {

    List<Order> findOrdersByFoodSpotId(Long foodSpotId);

    List<Order> findOrdersByCustomerId(Long customerId);

    List<Order> findOrdersByCourierId(Long courierId);

    List<Order> findFinishedOrders();

    List<Order> findUnfinishedOrders();
}
