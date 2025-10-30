package com.solvd.domain.human;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public abstract class Human {

    private long id;
    private String firstname;
    private String lastname;
    private String phoneNumber;
    private String email;

    public String getFullName(){
        return firstname + " " + lastname;
    }
}
