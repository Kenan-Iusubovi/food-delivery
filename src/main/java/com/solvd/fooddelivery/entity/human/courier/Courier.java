package com.solvd.fooddelivery.entity.human.courier;

import com.solvd.fooddelivery.entity.human.Human;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@XmlRootElement(name = "courier")
public class Courier extends Human {

    private String licenseNumber;
    @XmlElement(name = "workingExperience")
    private WorkingExperience workingExperience;
}
