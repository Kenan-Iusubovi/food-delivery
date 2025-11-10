package com.solvd.fooddelivery.entity.foodspot;


import com.solvd.fooddelivery.entity.order.Order;
import com.solvd.fooddelivery.jaxbfraemwork.adapter.LocalTimeAdapter;
import jakarta.xml.bind.annotation.*;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@XmlRootElement(name = "foodSpot")
@XmlAccessorType(XmlAccessType.FIELD)
public class FoodSpot {

    @XmlAttribute
    private Long id;
    private String name;
    private String address;
    private String phoneNumber;
    @XmlElementWrapper
    @XmlElement(name = "menu")
    private List<Menu> menus = new ArrayList<>();
    @XmlJavaTypeAdapter(LocalTimeAdapter.class)
    private LocalTime openingTime;
    @XmlJavaTypeAdapter(LocalTimeAdapter.class)
    private LocalTime closingTime;
    @XmlElementWrapper
    @XmlElement(name = "order")
    private List<Order> orders = new ArrayList<>();
}

