package com.solvd.fooddelivery.repository.mappers;

import com.solvd.fooddelivery.entity.foodspot.Product;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductMapper {

    public static Product mapToProduct(ResultSet resultSet, String prefix) throws SQLException {

        Product product = new Product();
        product.setId(resultSet.getLong(prefix + "id"));
        product.setName(resultSet.getString(prefix + "name"));
        product.setPrice(resultSet.getBigDecimal(prefix + "price"));
        product.setDescription(resultSet.getString(prefix + "description"));
        product.setAvailable(resultSet.getBoolean(prefix + "available"));

        return product;
    }

    public static List<Product> mapToProductsList(ResultSet resultSet) throws SQLException {

        List<Product> products = new ArrayList<>();
        while (resultSet.next()) {
            products.add(mapToProduct(resultSet,""));
        }
        return products;
    }
}
