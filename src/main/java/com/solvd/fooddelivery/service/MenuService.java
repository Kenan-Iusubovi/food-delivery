package com.solvd.fooddelivery.service;

import com.solvd.fooddelivery.entity.foodspot.Menu;

import java.util.List;
import java.util.Optional;

public interface MenuService {

    Menu create(Menu menu);

    Menu update(Menu menu);

    Optional<Menu> findById(Long id);

    List<Menu> findAll();

    void deleteById(Long id);
}
