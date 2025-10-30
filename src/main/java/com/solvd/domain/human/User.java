package com.solvd.domain.human;

import com.solvd.domain.order.Order;

import java.util.ArrayList;
import java.util.List;

public class User extends Human{

    private double balance;
    private boolean subscription;
    private List<Order> orders;

    public User(long id, String firstname, String lastname,
                String phoneNumber, String email, double balance,
                boolean subscription) {
        super(id, firstname, lastname, phoneNumber, email);
        this.balance = balance;
        this.subscription = subscription;
        this.orders = new ArrayList<>();
    }
}
