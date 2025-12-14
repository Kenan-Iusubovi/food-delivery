package com.solvd.fooddelivery.service;

import com.solvd.fooddelivery.entity.human.courier.Courier;

import java.util.List;
import java.util.Optional;

public interface CourierService {

    Courier create(Courier courier);

    Courier update(Courier courier);

    Optional<Courier> findById(Long id);

    List<Courier> findAll();

    void deleteById(Long id);
}
