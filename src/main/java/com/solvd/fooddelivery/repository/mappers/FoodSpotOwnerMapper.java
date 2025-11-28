package com.solvd.fooddelivery.repository.mappers;

import com.solvd.fooddelivery.entity.human.FoodSpotOwner;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FoodSpotOwnerMapper {

    public static FoodSpotOwner mapToFoodSpotOwner(ResultSet result, String prefix) throws SQLException {

        FoodSpotOwner owner = new FoodSpotOwner();

        owner.setId(result.getLong(prefix + "id"));
        owner.setName(result.getString(prefix + "name"));
        owner.setSurname(result.getString(prefix + "surname"));
        owner.setPhoneNumber(result.getString(prefix + "phone_number"));
        owner.setEmail(result.getString(prefix + "email"));
        owner.setBusinessLicense(result.getString(prefix + "business_license"));

        return owner;
    }

    public static List<FoodSpotOwner> mapToFoodSpotOwnerList(ResultSet result) throws SQLException {

        List<FoodSpotOwner> owners = new ArrayList<>();
        while (result.next()) {
            owners.add(mapToFoodSpotOwner(result, ""));
        }
        return owners;
    }
}
