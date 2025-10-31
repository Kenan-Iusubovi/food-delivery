package com.solvd.fooddelivery.entity.human;

import com.solvd.fooddelivery.entity.order.Order;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Courier extends Human{

    private String licenseNumber;
    private Period workingExperience;
    private List<Order> orders;

    public Courier(long id, String firstname, String lastname,
                   String phoneNumber, String email, String licenseNumber,
                   Period workingExperience) {
        super(id, firstname, lastname, phoneNumber, email);
        this.licenseNumber = licenseNumber;
        this.workingExperience = workingExperience;
        this.orders = new ArrayList<>();
    }
}
