package com.solvd.fooddelivery.entity.human;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public abstract class Human {

    private Long id;
    private String name;
    private String surname;
    private String phoneNumber;
    private String email;

    public String getFullName() {
        return name + " " + surname;
    }
}
