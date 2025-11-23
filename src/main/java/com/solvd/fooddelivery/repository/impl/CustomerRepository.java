package com.solvd.fooddelivery.repository.impl;

import com.solvd.fooddelivery.entity.foodspot.Product;
import com.solvd.fooddelivery.entity.human.Customer;
import com.solvd.fooddelivery.repository.CrudRepository;
import com.solvd.fooddelivery.repository.connection.ConnectionPool;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CustomerRepository implements CrudRepository<Customer, Long> {

    private static final ConnectionPool CONNECTION_POOL;
    private static final String CREATE_QUERY;
    private static final String FIND_BY_ID_QUERY;
    private static final String FIND_ALL_QUERY;
    private static final String UPDATE_QUERY;
    private static final String DELETE_QUERY;


    static {
        CONNECTION_POOL = ConnectionPool.getInstance();

        CREATE_QUERY = "insert into customers (name, surname, email, balance, subscription)" +
                " values (?, ?, ?, ?, ?)";

        FIND_BY_ID_QUERY = "select c.id, c.name, c.surname, c.phone_number, c.email, c.balance, c.subscription"
                + " from customers c where c.id = ?";

        FIND_ALL_QUERY = "select id, name, surname, phone_number, email, balance, subscription from customers";

        UPDATE_QUERY = "update customers set name = ?, surname = ?, phone_number = ?, email = ?, " +
                "balance = ?, subscription = ? where id = ?";

        DELETE_QUERY = "delete from customers where id = ?";
    }

    @Override
    public boolean create(Customer entity) {

        Connection connection = CONNECTION_POOL.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(
                CREATE_QUERY, PreparedStatement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, entity.getName());
            statement.setString(2, entity.getSurname());
            statement.setString(3, entity.getEmail());
            statement.setBigDecimal(4, BigDecimal.valueOf(0));
            statement.setBoolean(5,false);

            statement.executeUpdate();
            ResultSet result = statement.getGeneratedKeys();
            while (result.next()) {
                entity.setId(result.getLong(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }
        return entity.getId() != null;
    }

    @Override
    public Optional<Customer> findById(Long id) {

        Connection connection = CONNECTION_POOL.getConnection();
        Customer customer = null;
        try (PreparedStatement statement = connection.prepareStatement(FIND_BY_ID_QUERY)) {

            statement.setLong(1, id);

            ResultSet result = statement.executeQuery();
            if (result.next()) {
                customer = mapToCustomer(result);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }
        return Optional.ofNullable(customer);
    }

    @Override
    public List<Customer> findAll() {

        Connection connection = CONNECTION_POOL.getConnection();
        List<Customer> customers;
        try (PreparedStatement statement = connection.prepareStatement(FIND_ALL_QUERY)) {

            ResultSet result = statement.executeQuery();
            customers = mapToCustomerList(result);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }
        return customers;
    }

    @Override
    public boolean update(Customer entity) {

        Connection connection = CONNECTION_POOL.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)){

            statement.setString(1, entity.getName());
            statement.setString(2, entity.getSurname());
            statement.setString(3, entity.getPhoneNumber());
            statement.setString(4, entity.getEmail());
            statement.setBigDecimal(5, entity.getBalance());
            statement.setBoolean(6, entity.isSubscription());
            statement.setLong(7, entity.getId());

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            CONNECTION_POOL.releaseConnection(connection);
        }
    }

    @Override
    public boolean deleteById(Long id) {

        Connection connection = CONNECTION_POOL.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)){

            statement.setLong(1, id);

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            CONNECTION_POOL.releaseConnection(connection);
        }
    }

    public Customer mapToCustomer(ResultSet resultSet) throws SQLException {

        Customer customer = new Customer();
        customer.setId(resultSet.getLong("id"));
        customer.setName(resultSet.getString("name"));
        customer.setSurname(resultSet.getString("surname"));
        customer.setPhoneNumber(resultSet.getString("phone_number"));
        customer.setEmail(resultSet.getString("email"));
        customer.setBalance(resultSet.getBigDecimal("balance"));
        customer.setSubscription(resultSet.getBoolean("subscription"));

        return customer;
    }

    public List<Customer> mapToCustomerList(ResultSet resultSet) throws SQLException {

        List<Customer> customers = new ArrayList<>();
        while (resultSet.next()) {
            customers.add(mapToCustomer(resultSet));
        }
        return customers;
    }
}
