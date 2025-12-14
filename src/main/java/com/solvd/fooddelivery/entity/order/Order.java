package com.solvd.fooddelivery.entity.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.solvd.fooddelivery.entity.ProductContainer;
import com.solvd.fooddelivery.entity.foodspot.FoodSpot;
import com.solvd.fooddelivery.entity.foodspot.Product;
import com.solvd.fooddelivery.entity.human.Customer;
import com.solvd.fooddelivery.entity.human.courier.Courier;
import com.solvd.fooddelivery.utils.parsers.jaxbframework.adapter.LocalDateTimeAdapter;
import jakarta.xml.bind.annotation.*;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@XmlRootElement(name = "order")
@XmlAccessorType(XmlAccessType.FIELD)
public class Order implements ProductContainer {

    @XmlAttribute
    private Long id;
    private UUID orderNumber;
    private Courier courier;
    private BigDecimal totalPrice;
    @XmlElementWrapper
    @XmlElement(name = "product")
    private List<Product> products;
    private String takeAddress;
    private String bringAddress;
    private boolean finished;
    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime orderDateTime;
    private Customer customer;
    private FoodSpot foodSpot;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(UUID orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Courier getCourier() {
        return courier;
    }

    public void setCourier(Courier courier) {
        this.courier = courier;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Override
    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public String getTakeAddress() {
        return takeAddress;
    }

    public void setTakeAddress(String takeAddress) {
        this.takeAddress = takeAddress;
    }

    public String getBringAddress() {
        return bringAddress;
    }

    public void setBringAddress(String bringAddress) {
        this.bringAddress = bringAddress;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public LocalDateTime getOrderDateTime() {
        return orderDateTime;
    }

    public void setOrderDateTime(LocalDateTime orderDateTime) {
        this.orderDateTime = orderDateTime;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public FoodSpot getFoodSpot() {
        return foodSpot;
    }

    public void setFoodSpot(FoodSpot foodSpot) {
        this.foodSpot = foodSpot;
    }
}
