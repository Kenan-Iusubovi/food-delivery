package com.solvd.fooddelivery.entity.human;

import com.solvd.fooddelivery.entity.foodspot.FoodSpot;
import jakarta.xml.bind.annotation.*;

import java.util.Set;

@XmlRootElement(name = "foodSpotOwners")
@XmlAccessorType(XmlAccessType.FIELD)
public class FoodSpotOwner extends Human {

    @XmlElement
    private String businessLicense;
    @XmlElementWrapper
    @XmlElement(name = "foodSpot")
    private Set<FoodSpot> foodSpots;

    public String getBusinessLicense() {
        return businessLicense;
    }

    public void setBusinessLicense(String businessLicense) {
        this.businessLicense = businessLicense;
    }

    public Set<FoodSpot> getFoodSpots() {
        return foodSpots;
    }

    public void setFoodSpots(Set<FoodSpot> foodSpots) {
        this.foodSpots = foodSpots;
    }
}
