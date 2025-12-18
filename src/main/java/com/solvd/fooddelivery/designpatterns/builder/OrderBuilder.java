package com.solvd.fooddelivery.designpatterns.builder;

import com.solvd.fooddelivery.entity.foodspot.FoodSpot;
import com.solvd.fooddelivery.entity.foodspot.Product;
import com.solvd.fooddelivery.entity.human.Customer;
import com.solvd.fooddelivery.entity.human.courier.Courier;
import com.solvd.fooddelivery.entity.order.Order;

import java.util.List;

public interface OrderBuilder {

    OrderBuilder withCustomer(Customer customer);

    OrderBuilder withCourier(Courier courier);

    OrderBuilder withFoodSpot(FoodSpot foodSpot);

    OrderBuilder withProducts(List<Product> products);

    OrderBuilder withTakeAddress(String takeAddress);

    OrderBuilder withBringAddress(String bringAddress);

    Order build();
}
