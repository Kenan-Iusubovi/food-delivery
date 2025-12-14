package com.solvd.fooddelivery.repository.mybatis.dao;

import com.solvd.fooddelivery.entity.foodspot.Product;

import java.util.List;

public interface ProductDao {

    int create(Product product);

    Product findById(Long id);

    List<Product> findAll();

    int update(Product product);

    int deleteById(Long id);
}
