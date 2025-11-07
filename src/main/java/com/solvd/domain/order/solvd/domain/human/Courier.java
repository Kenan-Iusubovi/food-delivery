package com.solvd.domain.order.solvd.domain.human;

import com.solvd.domain.order.solvd.domain.order.Order;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Courier extends Human {

    private String licenseNumber;
    private LocalDateTime workingExperience;
    private List<Order> orders;

    public Courier(long id, String firstname, String lastname,
                   String phoneNumber, String email, String licenseNumber,
                   LocalDateTime workingExperience) {
        super(id, firstname, lastname, phoneNumber, email);
        this.licenseNumber = licenseNumber;
        this.workingExperience = workingExperience;
        this.orders = new ArrayList<>();
    }
}
