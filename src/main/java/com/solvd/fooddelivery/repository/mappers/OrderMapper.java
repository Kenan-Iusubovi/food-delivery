package com.solvd.fooddelivery.repository.mappers;

import com.solvd.fooddelivery.entity.order.Order;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class OrderMapper {

    public static Order mapToOrder(ResultSet resultSet, String prefix) throws SQLException {

        Order order = new Order();
        order.setId(resultSet.getLong(prefix + "id"));
        order.setOrderNumber(UUID.fromString(resultSet.getString(prefix + "order_number")));
        order.setTotalPrice(resultSet.getBigDecimal(prefix + "total_price"));
        order.setTakeAddress(resultSet.getString(prefix + "take_address"));
        order.setBringAddress(resultSet.getString(prefix + "bring_address"));
        order.setFinished(resultSet.getBoolean(prefix + "finished"));
        order.setOrderDateTime(resultSet.getTimestamp(prefix + "order_date_time").toLocalDateTime());

        return order;
    }

    public static List<Order> mapToOrderList(ResultSet resultSet) throws SQLException {

        List<Order> orders = new ArrayList<>();
        while (resultSet.next()) {
            orders.add(mapToOrder(resultSet, ""));
        }
        return orders;
    }
}
