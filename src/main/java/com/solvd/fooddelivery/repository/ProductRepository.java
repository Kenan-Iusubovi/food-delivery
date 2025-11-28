package com.solvd.fooddelivery.repository;

import com.solvd.fooddelivery.entity.foodspot.Product;

import java.util.List;

public interface ProductRepository {

    List<Product> findAvailableProducts();

    List<Product> findByNameLike(String namePattern);
}
