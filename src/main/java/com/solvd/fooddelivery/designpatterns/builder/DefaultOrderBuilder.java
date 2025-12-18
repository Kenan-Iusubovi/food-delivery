package com.solvd.fooddelivery.designpatterns.builder;

import com.solvd.fooddelivery.entity.foodspot.FoodSpot;
import com.solvd.fooddelivery.entity.foodspot.Product;
import com.solvd.fooddelivery.entity.human.Customer;
import com.solvd.fooddelivery.entity.human.courier.Courier;
import com.solvd.fooddelivery.entity.order.Order;

import java.time.LocalDateTime;
import java.util.List;

public class DefaultOrderBuilder implements OrderBuilder{

    private final Order order;

    public DefaultOrderBuilder() {
        this.order = new Order();
        this.order.setOrderDateTime(LocalDateTime.now());
        this.order.setFinished(false);
    }

    @Override
    public OrderBuilder withCustomer(Customer customer) {
        order.setCustomer(customer);
        return this;
    }

    @Override
    public OrderBuilder withCourier(Courier courier) {
        order.setCourier(courier);
        return this;
    }

    @Override
    public OrderBuilder withFoodSpot(FoodSpot foodSpot) {
        order.setFoodSpot(foodSpot);
        return this;
    }

    @Override
    public OrderBuilder withProducts(List<Product> products) {
        order.setProducts(products);
        return this;
    }

    @Override
    public OrderBuilder withTakeAddress(String takeAddress) {
        order.setTakeAddress(takeAddress);
        return this;
    }

    @Override
    public OrderBuilder withBringAddress(String bringAddress) {
        order.setBringAddress(bringAddress);
        return this;
    }

    @Override
    public Order build() {
        return order;
    }
}