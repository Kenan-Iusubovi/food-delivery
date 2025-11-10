package com.solvd.fooddelivery.entity.foodspot;

import com.solvd.fooddelivery.entity.ProductContainer;
import jakarta.xml.bind.annotation.*;

import java.util.List;

@XmlRootElement(name = "menu")
@XmlAccessorType(XmlAccessType.FIELD)
public class Menu implements ProductContainer {

    @XmlAttribute
    private Long id;
    private String name;
    @XmlElementWrapper
    @XmlElement(name = "product")
    private List<Product> products;

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

    @Override
    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
