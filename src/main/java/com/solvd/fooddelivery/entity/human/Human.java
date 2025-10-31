package com.solvd.fooddelivery.entity.human;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;

@AllArgsConstructor
@Data
public abstract class Human {

    @Setter(AccessLevel.NONE)
    private long id;
    private String firstname;
    private String lastname;
    private String phoneNumber;
    private String email;

    public String getFullName(){
        return firstname + " " + lastname;
    }
}
