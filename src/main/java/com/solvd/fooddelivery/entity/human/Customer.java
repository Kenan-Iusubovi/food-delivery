package com.solvd.fooddelivery.entity.human;

import com.solvd.fooddelivery.entity.order.Order;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Data
public class Customer extends Human{

    private BigDecimal balance;
    private boolean subscription;
}
