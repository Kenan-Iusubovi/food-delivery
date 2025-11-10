package com.solvd.fooddelivery.entity.human;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class Human {

    @XmlAttribute
    private Long id;
    @XmlElement
    private String name;
    @XmlElement
    private String surname;
    @XmlElement
    private String phoneNumber;
    @XmlElement
    private String email;

    public String getFullName() {
        return name + " " + surname;
    }
}
