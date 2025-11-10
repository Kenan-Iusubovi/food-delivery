package com.solvd.fooddelivery.entity.order;

import com.solvd.fooddelivery.entity.ProductContainer;
import com.solvd.fooddelivery.entity.foodspot.Product;
import com.solvd.fooddelivery.entity.human.Customer;
import com.solvd.fooddelivery.entity.human.courier.Courier;
import com.solvd.fooddelivery.jaxbfraemwork.adapter.LocalDateTimeAdapter;
import jakarta.xml.bind.annotation.*;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
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
    private List<Product> products = new ArrayList<>();
    private String takeAddress;
    private String bringAddress;
    private boolean finished;
    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    private LocalDateTime orderDateTime;
    private Customer customer;
}
