package com.solvd.fooddelivery.service;

import com.solvd.fooddelivery.entity.human.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerService {

    Customer create(Customer customer);

    Customer update(Customer customer);

    Optional<Customer> findById(Long id);

    List<Customer> findAll();

    void deleteById(Long id);
}
