package com.solvd.fooddelivery.entity.foodspot;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.solvd.fooddelivery.entity.order.Order;
import com.solvd.fooddelivery.utils.parsers.jaxbframework.adapter.LocalTimeAdapter;
import jakarta.xml.bind.annotation.*;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import java.time.LocalTime;
import java.util.List;

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
    private List<Menu> menus;
    @XmlJavaTypeAdapter(LocalTimeAdapter.class)
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime openingTime;
    @XmlJavaTypeAdapter(LocalTimeAdapter.class)
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime closingTime;
    @XmlElementWrapper
    @XmlElement(name = "order")
    private List<Order> orders;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public List<Menu> getMenus() {
        return menus;
    }

    public void setMenus(List<Menu> menus) {
        this.menus = menus;
    }

    public LocalTime getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(LocalTime openingTime) {
        this.openingTime = openingTime;
    }

    public LocalTime getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(LocalTime closingTime) {
        this.closingTime = closingTime;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
}

