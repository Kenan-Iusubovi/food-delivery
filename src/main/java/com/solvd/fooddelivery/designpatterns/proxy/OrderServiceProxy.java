package com.solvd.fooddelivery.designpatterns.proxy;

import com.solvd.fooddelivery.designpatterns.proxy.context.UserContext;
import com.solvd.fooddelivery.entity.order.Order;
import com.solvd.fooddelivery.service.OrderService;

import java.util.List;
import java.util.Optional;

public class OrderServiceProxy implements OrderService {

    private final OrderService target;
    private final UserContext userContext;

    public OrderServiceProxy(OrderService target, UserContext userContext) {
        this.target = target;
        this.userContext = userContext;
    }

    @Override
    public Order create(Order order) {
        System.out.println("[PROXY] User " + userContext.getUserId()
                + " is creating an order");

        return target.create(order);
    }

    @Override
    public Order update(Order order) {
        if (order.isFinished()) {
            throw new IllegalStateException(
                    "Finished orders cannot be updated");
        }

        System.out.println("[PROXY] Updating order id=" + order.getId());
        return target.update(order);
    }

    @Override
    public Optional<Order> findById(Long id) {
        System.out.println("[PROXY] Reading order id=" + id);
        return target.findById(id);
    }

    @Override
    public List<Order> findAll() {
        System.out.println("[PROXY] Reading all orders");
        return target.findAll();
    }

    @Override
    public void deleteById(Long id) {
        if (!userContext.isAdmin()) {
            throw new SecurityException(
                    "Only admins can delete orders");
        }

        System.out.println("[PROXY] Admin deleting order id=" + id);
        target.deleteById(id);
    }
}
