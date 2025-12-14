package com.solvd.fooddelivery.service;

import com.solvd.fooddelivery.entity.order.Order;

import java.util.List;
import java.util.Optional;

public interface OrderService {

    Order create(Order order);

    Order update(Order order);

    Optional<Order> findById(Long id);

    List<Order> findAll();

    void deleteById(Long id);
}
