package com.solvd.fooddelivery.repository;

import com.solvd.fooddelivery.entity.human.courier.Courier;

import java.util.List;
import java.util.Optional;

public interface CourierRepository {

    Optional<Courier> findByLicenseNumber(String license);

    List<Courier> findByExperience(int years);
}
