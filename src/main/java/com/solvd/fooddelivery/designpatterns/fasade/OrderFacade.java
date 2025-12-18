package com.solvd.fooddelivery.designpatterns.fasade;

import com.solvd.fooddelivery.entity.order.Order;
import com.solvd.fooddelivery.service.CourierService;
import com.solvd.fooddelivery.service.CustomerService;
import com.solvd.fooddelivery.service.OrderService;
import com.solvd.fooddelivery.service.ProductService;

public class OrderFacade {

    private final OrderService orderService;
    private final ProductService productService;
    private final CustomerService customerService;
    private final CourierService courierService;

    public OrderFacade(OrderService orderService,
                       ProductService productService,
                       CustomerService customerService,
                       CourierService courierService) {
        this.orderService = orderService;
        this.productService = productService;
        this.customerService = customerService;
        this.courierService = courierService;
    }

    public Order placeOrder(Order order) {
        return orderService.create(order);
    }
}
