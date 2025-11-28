package com.solvd.fooddelivery.repository.mappers;

import com.solvd.fooddelivery.entity.foodspot.FoodSpot;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FoodSpotMapper {

    public static FoodSpot mapToFoodSpot(ResultSet resultSet, String prefix) throws SQLException {

        FoodSpot foodSpot = new FoodSpot();

        foodSpot.setId(resultSet.getLong(prefix + "id"));
        foodSpot.setName(resultSet.getString(prefix + "name"));
        foodSpot.setAddress(resultSet.getString(prefix + "address"));
        foodSpot.setPhoneNumber(resultSet.getString(prefix + "phone_number"));
        foodSpot.setOpeningTime(resultSet.getTime(prefix + "opening_time").toLocalTime());
        foodSpot.setClosingTime(resultSet.getTime(prefix + "closing_time").toLocalTime());

        return foodSpot;
    }

    public static List<FoodSpot> mapToFoodSpotList(ResultSet resultSet) throws SQLException {

        List<FoodSpot> foodSpots = new ArrayList<>();
        while (resultSet.next()) {
            foodSpots.add(mapToFoodSpot(resultSet, ""));
        }
        return foodSpots;
    }
}
