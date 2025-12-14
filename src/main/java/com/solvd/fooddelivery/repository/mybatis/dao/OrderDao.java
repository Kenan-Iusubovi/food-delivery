package com.solvd.fooddelivery.repository.mybatis.dao;

import com.solvd.fooddelivery.entity.order.Order;
import java.util.List;

public interface OrderDao {

    int create(Order order);

    int update(Order order);

    Order findById(Long id);

    List<Order> findAll();

    int deleteById(Long id);
}
