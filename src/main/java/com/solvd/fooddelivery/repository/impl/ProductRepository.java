package com.solvd.fooddelivery.repository.impl;

import com.solvd.fooddelivery.entity.foodspot.Product;
import com.solvd.fooddelivery.repository.CrudRepository;
import com.solvd.fooddelivery.repository.connection.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductRepository implements CrudRepository<Product, Long> {

    private static final ConnectionPool CONNECTION_POOL;
    private static final String CREATE_QUERY;
    private static final String FIND_BY_ID_QUERY;
    private static final String FIND_ALL_QUERY;
    private static final String UPDATE_QUERY;
    private static final String DELETE_QUERY;


    static {
        CONNECTION_POOL = ConnectionPool.getInstance();

        CREATE_QUERY = "insert into products (name, price, description, available) values (?, ?, ?, ?)";

        FIND_BY_ID_QUERY = "select p.id, p.name, p.price, p.description," +
                " p.available from products p where p.id = ?";

        FIND_ALL_QUERY = "select id, name, price, description, available from products";

        UPDATE_QUERY = "update products set name = ?, price = ?, description = ?, available = ? where id = ?";

        DELETE_QUERY = "delete from products where id = ?";
    }

    @Override
    public boolean create(Product entity) {

        Connection connection = CONNECTION_POOL.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(
                CREATE_QUERY, PreparedStatement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, entity.getName());
            statement.setBigDecimal(2, entity.getPrice());
            statement.setString(3, entity.getDescription());
            statement.setBoolean(4, entity.isAvailable());

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
    public Optional<Product> findById(Long id) {

        Connection connection = CONNECTION_POOL.getConnection();
        Product product;
        try (PreparedStatement statement = connection.prepareStatement(FIND_BY_ID_QUERY)) {

            statement.setLong(1, id);

            ResultSet result = statement.executeQuery();
            product = mapToProduct(result);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }
        return Optional.ofNullable(product);
    }

    @Override
    public List<Product> findAll() {

        Connection connection = CONNECTION_POOL.getConnection();
        List<Product> products;
        try (PreparedStatement statement = connection.prepareStatement(FIND_ALL_QUERY)) {

            ResultSet result = statement.executeQuery();
            products = mapProductsList(result);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }
        return products;
    }

    @Override
    public boolean update(Product entity) {

        Connection connection = CONNECTION_POOL.getConnection();

        try (PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {

            statement.setString(1, entity.getName());
            statement.setBigDecimal(2, entity.getPrice());
            statement.setString(3, entity.getDescription());
            statement.setBoolean(4, entity.isAvailable());
            statement.setLong(5, entity.getId());

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
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
            throw new RuntimeException(e);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }
    }

    public Product mapToProduct(ResultSet resultSet) throws SQLException {

        Product product = new Product();
        if (resultSet.next()) {
            product.setId(resultSet.getLong("id"));
            product.setName(resultSet.getString("name"));
            product.setPrice(resultSet.getBigDecimal("price"));
            product.setDescription(resultSet.getString("description"));
            product.setAvailable(resultSet.getBoolean("available"));
        }
        return product;
    }

    public List<Product> mapProductsList(ResultSet resultSet) throws SQLException {

        List<Product> products = new ArrayList<>();
        while (resultSet.next()) {
            products.add(mapToProduct(resultSet));
        }
        return products;
    }
}
