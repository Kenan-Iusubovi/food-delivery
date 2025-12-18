package com.solvd.fooddelivery.repository.mybatis.dao;

import com.solvd.fooddelivery.entity.human.Customer;

import java.util.List;

public interface CustomerDao {

    int create(Customer customer);

    int update(Customer customer);

    Customer findById(Long id);

    List<Customer> findAll();

    int deleteById(Long id);

    Customer findByEmail(String email);

    Customer findByPhoneNumber(String phoneNumber);
}
