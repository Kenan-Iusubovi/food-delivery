package com.solvd.fooddelivery.repository.mappers;

import com.solvd.fooddelivery.entity.human.Customer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerMapper {

    public static Customer mapToCustomer(ResultSet resultSet, String prefix) throws SQLException {

        Customer customer = new Customer();
        customer.setId(resultSet.getLong(prefix + "id"));
        customer.setName(resultSet.getString(prefix + "name"));
        customer.setSurname(resultSet.getString(prefix + "surname"));
        customer.setPhoneNumber(resultSet.getString(prefix + "phone_number"));
        customer.setEmail(resultSet.getString(prefix + "email"));
        customer.setBalance(resultSet.getBigDecimal(prefix + "balance"));
        customer.setSubscription(resultSet.getBoolean(prefix + "subscription"));

        return customer;
    }

    public static List<Customer> mapToCustomerList(ResultSet resultSet) throws SQLException {

        List<Customer> customers = new ArrayList<>();
        while (resultSet.next()) {
            customers.add(mapToCustomer(resultSet, ""));
        }
        return customers;
    }
}
