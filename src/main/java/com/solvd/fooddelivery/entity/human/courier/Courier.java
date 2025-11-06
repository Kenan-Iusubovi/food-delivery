package com.solvd.fooddelivery.entity.human.courier;

import com.solvd.fooddelivery.entity.human.Human;
import com.solvd.fooddelivery.entity.order.Order;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class Courier extends Human {

    private String licenseNumber;
    private WorkingExperience workingExperience;
}
