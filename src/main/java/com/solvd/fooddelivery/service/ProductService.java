package com.solvd.fooddelivery.service;

import com.solvd.fooddelivery.entity.foodspot.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    Product create(Product product);

    Product update(Product product);

    Optional<Product> findById(Long id);

    List<Product> findAll();

    void deleteById(Long id);
}
