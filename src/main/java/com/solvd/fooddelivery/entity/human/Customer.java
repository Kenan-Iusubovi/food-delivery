package com.solvd.fooddelivery.entity.human;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@Data
public class Customer extends Human {

    private BigDecimal balance;
    private boolean subscription;
}
