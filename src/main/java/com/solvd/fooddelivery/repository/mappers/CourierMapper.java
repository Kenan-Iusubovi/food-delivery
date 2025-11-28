package com.solvd.fooddelivery.repository.mappers;

import com.solvd.fooddelivery.entity.human.courier.Courier;
import com.solvd.fooddelivery.entity.human.courier.WorkingExperience;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CourierMapper {

    public static Courier mapToCourier(ResultSet resultSet, String prefix) throws SQLException {

        Courier courier = new Courier();
        courier.setWorkingExperience(new WorkingExperience());
        courier.setId(resultSet.getLong(prefix + "id"));
        courier.setName(resultSet.getString(prefix + "name"));
        courier.setSurname(resultSet.getString(prefix + "surname"));
        courier.setPhoneNumber(resultSet.getString(prefix + "phone_number"));
        courier.setEmail(resultSet.getString(prefix + "email"));
        courier.setLicenseNumber(resultSet.getString(prefix + "license_number"));
        courier.getWorkingExperience().setYears(resultSet.getInt(prefix + "years"));
        courier.getWorkingExperience().setMonths(resultSet.getInt(prefix + "months"));
        courier.getWorkingExperience().setDays(resultSet.getInt(prefix + "days"));

        return courier;
    }

    public static List<Courier> mapToListCourier(ResultSet resultSet) throws SQLException {

        List<Courier> couriers = new ArrayList<>();
        while (resultSet.next()) {
            couriers.add(mapToCourier(resultSet, ""));
        }
        return couriers;
    }
}
