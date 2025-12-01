package com.solvd.fooddelivery.repository.impl;

import com.solvd.fooddelivery.entity.foodspot.Product;
import com.solvd.fooddelivery.repository.CrudRepository;
import com.solvd.fooddelivery.repository.ProductRepository;
import com.solvd.fooddelivery.repository.connection.ConnectionPool;
import com.solvd.fooddelivery.repository.mappers.ProductMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductRepositoryImpl implements ProductRepository, CrudRepository<Product, Long> {

    private final ConnectionPool connectionPool;
    private final String createQuery;
    private final String findByIdQuery;
    private final String findAllQuery;
    private final String updateQuery;
    private final String deleteQuery;

    public ProductRepositoryImpl() {

        this.connectionPool = ConnectionPool.getInstance();

        this.createQuery =
                "INSERT INTO products (name, price, description, available) VALUES (?, ?, ?, ?)";

        this.findByIdQuery =
                "SELECT id, name, price, description, available FROM products WHERE id = ?";

        this.findAllQuery =
                "SELECT id, name, price, description, available FROM products";

        this.updateQuery =
                "UPDATE products SET name = ?, price = ?, description = ?, available = ? WHERE id = ?";

        this.deleteQuery =
                "DELETE FROM products WHERE id = ?";
    }

    @Override
    public boolean create(Product entity) {

        Connection connection = connectionPool.getConnection();

        try (PreparedStatement statement = connection.prepareStatement(
                createQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, entity.getName());
            statement.setBigDecimal(2, entity.getPrice());
            statement.setString(3, entity.getDescription());
            statement.setBoolean(4, entity.isAvailable());
            statement.executeUpdate();

            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                entity.setId(resultSet.getLong(1));
            }

        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        } finally {
            connectionPool.releaseConnection(connection);
        }

        return entity.getId() != null;
    }

    @Override
    public Optional<Product> findById(Long id) {

        Connection connection = connectionPool.getConnection();
        Product product = null;

        try (PreparedStatement statement = connection.prepareStatement(findByIdQuery)) {

            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                product = ProductMapper.mapToProduct(resultSet, "");
            }

        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        } finally {
            connectionPool.releaseConnection(connection);
        }

        return Optional.ofNullable(product);
    }

    @Override
    public List<Product> findAll() {

        Connection connection = connectionPool.getConnection();
        List<Product> products;

        try (PreparedStatement statement = connection.prepareStatement(findAllQuery)) {

            ResultSet resultSet = statement.executeQuery();
            products = ProductMapper.mapToProductsList(resultSet);

        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        } finally {
            connectionPool.releaseConnection(connection);
        }

        return products;
    }

    @Override
    public boolean update(Product entity) {

        Connection connection = connectionPool.getConnection();

        try (PreparedStatement statement = connection.prepareStatement(updateQuery)) {

            statement.setString(1, entity.getName());
            statement.setBigDecimal(2, entity.getPrice());
            statement.setString(3, entity.getDescription());
            statement.setBoolean(4, entity.isAvailable());
            statement.setLong(5, entity.getId());

            return statement.executeUpdate() > 0;

        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        } finally {
            connectionPool.releaseConnection(connection);
        }
    }

    @Override
    public boolean deleteById(Long id) {

        Connection connection = connectionPool.getConnection();

        try (PreparedStatement statement = connection.prepareStatement(deleteQuery)) {

            statement.setLong(1, id);
            return statement.executeUpdate() > 0;

        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        } finally {
            connectionPool.releaseConnection(connection);
        }
    }

    @Override
    public List<Product> findAvailableProducts() {

        Connection connection = connectionPool.getConnection();
        List<Product> products = new ArrayList<>();

        String query =
                "SELECT id, name, price, description, available FROM products WHERE available = 1";

        try (PreparedStatement statement = connection.prepareStatement(query)) {

            ResultSet resultSet = statement.executeQuery();
            products = ProductMapper.mapToProductsList(resultSet);

        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        } finally {
            connectionPool.releaseConnection(connection);
        }

        return products;
    }

    @Override
    public List<Product> findByNameLike(String namePattern) {

        Connection connection = connectionPool.getConnection();
        List<Product> products = new ArrayList<>();

        String query =
                "SELECT id, name, price, description, available FROM products WHERE LOWER(name) LIKE LOWER(?)";

        try (PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, "%" + namePattern + "%");
            ResultSet resultSet = statement.executeQuery();

            products = ProductMapper.mapToProductsList(resultSet);

        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        } finally {
            connectionPool.releaseConnection(connection);
        }

        return products;
    }

    public List<Product> findProductsByIds(List<Long> ids) {

        if (ids == null || ids.isEmpty()) {
            return new ArrayList<>();
        }

        Connection connection = connectionPool.getConnection();
        List<Product> products;

        StringBuilder query = new StringBuilder(
                "SELECT id, name, price, description, available FROM products WHERE id IN ("
        );

        for (int i = 0; i < ids.size(); i++) {
            query.append("?");
            if (i < ids.size() - 1) {
                query.append(", ");
            }
        }
        query.append(")");

        try (PreparedStatement statement = connection.prepareStatement(query.toString())) {

            for (int i = 0; i < ids.size(); i++) {
                statement.setLong(i + 1, ids.get(i));
            }

            ResultSet resultSet = statement.executeQuery();
            products = ProductMapper.mapToProductsList(resultSet);

        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        } finally {
            connectionPool.releaseConnection(connection);
        }

        return products;
    }
}
