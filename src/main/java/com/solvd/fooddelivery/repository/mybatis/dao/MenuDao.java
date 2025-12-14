package com.solvd.fooddelivery.repository.mybatis.dao;

import com.solvd.fooddelivery.entity.foodspot.Menu;

import java.util.List;

public interface MenuDao {

    int create(Menu menu);

    int update(Menu menu);

    Menu findById(Long id);

    List<Menu> findAll();

    int deleteById(Long id);
}
