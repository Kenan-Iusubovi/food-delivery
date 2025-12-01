package com.solvd.fooddelivery.repository.impl;

import com.solvd.fooddelivery.entity.human.Customer;
import com.solvd.fooddelivery.repository.CrudRepository;
import com.solvd.fooddelivery.repository.CustomerRepository;
import com.solvd.fooddelivery.repository.connection.ConnectionPool;
import com.solvd.fooddelivery.repository.mappers.CustomerMapper;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CustomerRepositoryImpl implements CustomerRepository, CrudRepository<Customer, Long> {

    private static final ConnectionPool CONNECTION_POOL = ConnectionPool.getInstance();

    private static final String CREATE_QUERY =
            "INSERT INTO customers (name, surname, phone_number, email, balance, subscription) VALUES (?, ?, ?, ?, ?, ?)";

    private static final String FIND_BY_ID_QUERY =
            "SELECT id, name, surname, phone_number, email, balance, subscription FROM customers WHERE id = ?";

    private static final String FIND_ALL_QUERY =
            "SELECT id, name, surname, phone_number, email, balance, subscription FROM customers";

    private static final String UPDATE_QUERY =
            "UPDATE customers SET name = ?, surname = ?, phone_number = ?, email = ?, balance = ?, subscription = ? WHERE id = ?";

    private static final String DELETE_QUERY =
            "DELETE FROM customers WHERE id = ?";

    private static final String FIND_BY_EMAIL_QUERY =
            "SELECT id, name, surname, phone_number, email, balance, subscription, created_at " +
                    "FROM customers WHERE email = ?";

    private static final String UPDATE_BALANCE_QUERY =
            "UPDATE customers SET balance = balance + ? WHERE id = ?";

    private static final String FIND_SUBSCRIBED_CUSTOMERS_QUERY =
            "SELECT id, name, surname, phone_number, email, balance, subscription, created_at " +
                    "FROM customers WHERE subscription = 1";


    @Override
    public boolean create(Customer entity) {

        Connection connection = CONNECTION_POOL.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(
                CREATE_QUERY, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, entity.getName());
            statement.setString(2, entity.getSurname());
            statement.setString(3, entity.getPhoneNumber());
            statement.setString(4, entity.getEmail());
            statement.setBigDecimal(5, entity.getBalance());
            statement.setBoolean(6, entity.isSubscription());

            statement.executeUpdate();

            ResultSet keys = statement.getGeneratedKeys();
            if (keys.next()) {
                entity.setId(keys.getLong(1));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error while creating customer", e);
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
                customer = CustomerMapper.mapToCustomer(result, "");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error while finding customer by id", e);
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
            customers = CustomerMapper.mapToCustomerList(result);

        } catch (SQLException e) {
            throw new RuntimeException("Error while finding all customers", e);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }

        return customers;
    }

    @Override
    public boolean update(Customer entity) {

        Connection connection = CONNECTION_POOL.getConnection();

        try (PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {

            statement.setString(1, entity.getName());
            statement.setString(2, entity.getSurname());
            statement.setString(3, entity.getPhoneNumber());
            statement.setString(4, entity.getEmail());
            statement.setBigDecimal(5, entity.getBalance());
            statement.setBoolean(6, entity.isSubscription());
            statement.setLong(7, entity.getId());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error while updating customer", e);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }
    }

    @Override
    public boolean deleteById(Long id) {

        Connection connection = CONNECTION_POOL.getConnection();

        try (PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)) {

            statement.setLong(1, id);
            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error while deleting customer", e);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }
    }

    @Override
    public Optional<Customer> findByEmail(String email) {

        Connection connection = CONNECTION_POOL.getConnection();
        Customer customer = null;

        try (PreparedStatement statement = connection.prepareStatement(FIND_BY_EMAIL_QUERY)) {

            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                customer = CustomerMapper.mapToCustomer(resultSet, "");
            }

        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }

        return Optional.ofNullable(customer);
    }


    @Override
    public boolean updateBalance(Long customerId, BigDecimal amount) {

        Connection connection = CONNECTION_POOL.getConnection();

        try (PreparedStatement statement = connection.prepareStatement(UPDATE_BALANCE_QUERY)) {

            statement.setBigDecimal(1, amount);
            statement.setLong(2, customerId);

            return statement.executeUpdate() > 0;

        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }
    }

    @Override
    public List<Customer> findSubscribedCustomers() {

        Connection connection = CONNECTION_POOL.getConnection();
        List<Customer> customers = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(FIND_SUBSCRIBED_CUSTOMERS_QUERY)) {

            ResultSet resultSet = statement.executeQuery();
            customers = CustomerMapper.mapToCustomerList(resultSet);

        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }

        return customers;
    }

}
