package com.solvd.fooddelivery.repository.impl;

import com.solvd.fooddelivery.entity.order.Order;
import com.solvd.fooddelivery.entity.foodspot.Product;
import com.solvd.fooddelivery.repository.CrudRepository;
import com.solvd.fooddelivery.repository.connection.ConnectionPool;
import com.solvd.fooddelivery.repository.mappers.OrderMapper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrderRepositoryImpl implements CrudRepository<Order, Long> {

    private static final ConnectionPool CONNECTION_POOL = ConnectionPool.getInstance();

    private static final String CREATE_QUERY =
            "INSERT INTO orders (order_number, total_price, take_address, bring_address, finished, " +
                    "order_date_time, courier_id, customer_id, food_spot_id, delivery_instructions) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String FIND_BY_ID_QUERY =
            "SELECT id, order_number, total_price, take_address, bring_address, finished, " +
                    "order_date_time, courier_id, customer_id, food_spot_id, delivery_instructions " +
                    "FROM orders WHERE id = ?";

    private static final String FIND_ALL_QUERY =
            "SELECT id, order_number, total_price, order_date_time FROM orders";

    private static final String UPDATE_QUERY =
            "UPDATE orders SET take_address = ?, bring_address = ?, finished = ?, delivery_instructions = ? WHERE id = ?";

    private static final String DELETE_QUERY =
            "DELETE FROM orders WHERE id = ?";

    private static final String FIND_PRODUCT_IDS_QUERY =
            "SELECT product_id FROM products_has_orders WHERE order_id = ?";

    private static final String FIND_ORDERS_BY_FS_ID_QUERY =
            "SELECT id, order_number, total_price, take_address, bring_address, finished, order_date_time, " +
                    "courier_id, customer_id, food_spot_id, delivery_instructions FROM orders WHERE food_spot_id = ?";

    private final ProductRepositoryImpl productRepositoryImpl = new ProductRepositoryImpl();
    private final CustomerRepositoryImpl customerRepositoryImpl = new CustomerRepositoryImpl();
    private final CourierRepositoryImpl courierRepositoryImpl = new CourierRepositoryImpl();


    @Override
    public boolean create(Order order) {

        Connection connection = CONNECTION_POOL.getConnection();

        try (PreparedStatement statement =
                     connection.prepareStatement(CREATE_QUERY, PreparedStatement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, order.getOrderNumber().toString());
            statement.setBigDecimal(2, order.getTotalPrice());
            statement.setString(3, order.getTakeAddress());
            statement.setString(4, order.getBringAddress());
            statement.setBoolean(5, order.isFinished());
            statement.setTimestamp(6, Timestamp.valueOf(order.getOrderDateTime()));
            statement.setLong(7, order.getCourier().getId());
            statement.setLong(8, order.getCustomer().getId());
            statement.setLong(9, order.getCustomer().getId());
            statement.setString(10, null);

            statement.executeUpdate();

            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                order.setId(resultSet.getLong(1));
            }

        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }

        return order.getId() != null;
    }

    @Override
    public Optional<Order> findById(Long id) {

        Connection connection = CONNECTION_POOL.getConnection();
        Order order = null;

        try (PreparedStatement statement = connection.prepareStatement(FIND_BY_ID_QUERY)) {

            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {

                order = OrderMapper.mapToOrder(resultSet, "");

                order.setCustomer(customerRepositoryImpl
                        .findById(resultSet.getLong("customer_id"))
                        .orElse(null));

                order.setCourier(courierRepositoryImpl
                        .findById(resultSet.getLong("courier_id"))
                        .orElse(null));

                List<Long> productIds = findProductIds(id);
                List<Product> products = productRepositoryImpl.findProductsByIds(productIds);
                order.setProducts(products);
            }

        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }

        return Optional.ofNullable(order);
    }

    @Override
    public List<Order> findAll() {

        Connection connection = CONNECTION_POOL.getConnection();
        List<Order> orders = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(FIND_ALL_QUERY)) {

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {

                Order order = new Order();
                order.setId(resultSet.getLong("id"));
                order.setOrderNumber(java.util.UUID.fromString(resultSet.getString("order_number")));
                order.setTotalPrice(resultSet.getBigDecimal("total_price"));
                order.setOrderDateTime(resultSet.getTimestamp("order_date_time").toLocalDateTime());
                orders.add(order);
            }

        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }

        return orders;
    }

    @Override
    public boolean update(Order order) {

        Connection connection = CONNECTION_POOL.getConnection();

        try (PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {

            statement.setString(1, order.getTakeAddress());
            statement.setString(2, order.getBringAddress());
            statement.setBoolean(3, order.isFinished());
            statement.setString(4, null);
            statement.setLong(5, order.getId());

            return statement.executeUpdate() > 0;

        } catch (SQLException exception) {
            throw new RuntimeException(exception);
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

        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }
    }

    public List<Order> findOrdersByFoodSpotId(Long foodSpotId) {

        Connection connection = CONNECTION_POOL.getConnection();
        List<Order> orders = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(FIND_ORDERS_BY_FS_ID_QUERY)) {

            statement.setLong(1, foodSpotId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {

                Order order = OrderMapper.mapToOrder(resultSet, "");

                order.setCustomer(customerRepositoryImpl
                        .findById(resultSet.getLong("customer_id"))
                        .orElse(null));

                order.setCourier(courierRepositoryImpl
                        .findById(resultSet.getLong("courier_id"))
                        .orElse(null));

                List<Long> productIds = findProductIds(order.getId());
                List<Product> products = productRepositoryImpl.findProductsByIds(productIds);
                order.setProducts(products);

                orders.add(order);
            }

        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }

        return orders;
    }

    private List<Long> findProductIds(Long orderId) {

        Connection connection = CONNECTION_POOL.getConnection();
        List<Long> productIds = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(FIND_PRODUCT_IDS_QUERY)) {

            statement.setLong(1, orderId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                productIds.add(resultSet.getLong("product_id"));
            }

        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }

        return productIds;
    }
}
