package com.solvd.fooddelivery.entity.human.courier;

import com.solvd.fooddelivery.entity.human.Human;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "courier")
public class Courier extends Human {

    private String licenseNumber;
    @XmlElement(name = "workingExperience")
    private WorkingExperience workingExperience;

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public WorkingExperience getWorkingExperience() {
        return workingExperience;
    }

    public void setWorkingExperience(WorkingExperience workingExperience) {
        this.workingExperience = workingExperience;
    }
}
