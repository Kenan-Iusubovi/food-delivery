package com.solvd.fooddelivery.entity.human;

import com.solvd.fooddelivery.entity.foodspot.FoodSpot;
import jakarta.xml.bind.annotation.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@XmlRootElement(name = "foodSpotOwners")
@XmlAccessorType(XmlAccessType.FIELD)
public class FoodSpotOwner extends Human {

    @XmlElement
    private String businessLicense;
    @XmlElementWrapper
    @XmlElement(name = "foodSpot")
    private Set<FoodSpot> foodSpots = new HashSet<>();

    public FoodSpotOwner(long id, String firstname, String lastname,
                         String phoneNumber, String email,
                         String businessLicense) {
        super(id, firstname, lastname, phoneNumber, email);
        this.businessLicense = businessLicense;
    }
}
