package com.solvd.fooddelivery.entity;

import com.solvd.fooddelivery.entity.human.FoodSpotOwner;
import jakarta.xml.bind.annotation.*;

import java.util.Set;

@XmlRootElement(name = "foodDelivery")
@XmlAccessorType(XmlAccessType.FIELD)
public class FoodDelivery {

    @XmlElementWrapper
    @XmlElement(name = "foodSpotOwner")
    private Set<FoodSpotOwner> foodSpotOwners;

    public Set<FoodSpotOwner> getFoodSpotOwners() {
        return foodSpotOwners;
    }

    public void setFoodSpotOwners(Set<FoodSpotOwner> foodSpotOwners) {
        this.foodSpotOwners = foodSpotOwners;
    }
}