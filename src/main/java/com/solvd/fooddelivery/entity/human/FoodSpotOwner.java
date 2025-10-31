package com.solvd.fooddelivery.entity.human;

import com.solvd.fooddelivery.entity.foodspot.FoodSpot;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
public class FoodSpotOwner extends Human{

    private String businessLicense;
    private Set<FoodSpot> foodSpots;

    public FoodSpotOwner(long id, String firstname, String lastname,
                         String phoneNumber, String email,
                         String businessLicense) {
        super(id, firstname, lastname, phoneNumber, email);
        this.businessLicense = businessLicense;
        this.foodSpots = new HashSet<>();
    }
}
