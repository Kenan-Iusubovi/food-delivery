package com.solvd.fooddelivery.entity.human;

import lombok.*;

@AllArgsConstructor
@Data
@NoArgsConstructor
public abstract class Human {

    private Long id;
    private String name;
    private String surname;
    private String phoneNumber;
    private String email;

    public String getFullName(){
        return name + " " + surname;
    }
}
