package com.solvd.fooddelivery.entity.foodspot;

import com.solvd.fooddelivery.entity.ProductContainer;
import jakarta.xml.bind.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement(name = "menu")
@XmlAccessorType(XmlAccessType.FIELD)
public class Menu implements ProductContainer {

    @XmlAttribute
    private Long id;
    private String name;
    @XmlElementWrapper
    @XmlElement(name = "product")
    private List<Product> products = new ArrayList<>();
}
