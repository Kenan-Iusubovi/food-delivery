package com.solvd.fooddelivery.repository;

import com.solvd.fooddelivery.entity.human.Customer;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface CustomerRepository {

    Optional<Customer> findByEmail(String email);

    boolean updateBalance(Long customerId, BigDecimal amount);

    List<Customer> findSubscribedCustomers();
}
