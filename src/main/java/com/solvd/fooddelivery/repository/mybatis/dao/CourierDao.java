package com.solvd.fooddelivery.repository.mybatis.dao;

import com.solvd.fooddelivery.entity.human.courier.Courier;

import java.util.List;

public interface CourierDao {

    int create(Courier courier);

    Courier findById(Long id);

    List<Courier> findAll();

    int update(Courier courier);

    int deleteById(Long id);

    Courier findByLicenseNumber(String licenseNumber);

    List<Courier> findByExperience(int years);

    Courier findByEmail(String email);

}
