package com.solvd.fooddelivery.entity;

import com.solvd.fooddelivery.entity.human.FoodSpotOwner;
import jakarta.xml.bind.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement(name = "foodDelivery")
@XmlAccessorType(XmlAccessType.FIELD)
public class FoodDelivery {

    @XmlElementWrapper
    @XmlElement(name = "foodSpotOwner")
    private Set<FoodSpotOwner> foodSpotOwners = new HashSet<>();
}