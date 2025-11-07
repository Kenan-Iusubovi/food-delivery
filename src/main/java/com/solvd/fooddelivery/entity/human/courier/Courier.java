package com.solvd.fooddelivery.entity.human.courier;

import com.solvd.fooddelivery.entity.human.Human;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Courier extends Human {

    private String licenseNumber;
    private WorkingExperience workingExperience;
}
