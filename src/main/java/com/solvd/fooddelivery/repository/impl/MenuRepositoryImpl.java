package com.solvd.fooddelivery.repository.impl;

import com.solvd.fooddelivery.entity.foodspot.Menu;
import com.solvd.fooddelivery.entity.foodspot.Product;
import com.solvd.fooddelivery.repository.CrudRepository;
import com.solvd.fooddelivery.repository.connection.ConnectionPool;
import com.solvd.fooddelivery.repository.mappers.MenuMapper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MenuRepositoryImpl implements CrudRepository<Menu, Long> {

    private static final ConnectionPool CONNECTION_POOL = ConnectionPool.getInstance();

    private static final String CREATE_QUERY =
            "INSERT INTO menus (name, food_spot_id) VALUES (?, ?)";

    private static final String FIND_BY_ID_QUERY =
            "SELECT id, name FROM menus WHERE id = ?";

    private static final String FIND_ALL_QUERY =
            "SELECT id, name FROM menus";

    private static final String UPDATE_QUERY =
            "UPDATE menus SET name = ? WHERE id = ?";

    private static final String DELETE_QUERY =
            "DELETE FROM menus WHERE id = ?";

    private static final String FIND_PRODUCT_IDS_QUERY =
            "SELECT product_id FROM menus_has_products WHERE menu_id = ?";

    private static final String FIND_BY_FOOD_SPOT_ID_QUERY =
            "SELECT id, name FROM menus WHERE food_spot_id = ?";

    private final ProductRepositoryImpl productRepositoryImpl = new ProductRepositoryImpl();

    @Override
    public boolean create(Menu menu) {

        Connection connection = CONNECTION_POOL.getConnection();

        try (PreparedStatement statement = connection.prepareStatement(
                CREATE_QUERY, PreparedStatement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, menu.getName());
            statement.setLong(2, menu.getId());

            statement.executeUpdate();

            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                menu.setId(resultSet.getLong(1));
            }

        } catch (SQLException exception) {
            throw new RuntimeException("Error while creating Menu", exception);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }
        return menu.getId() != null;
    }

    @Override
    public Optional<Menu> findById(Long id) {

        Connection connection = CONNECTION_POOL.getConnection();
        Menu menu = null;

        try (PreparedStatement statement = connection.prepareStatement(FIND_BY_ID_QUERY)) {

            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {

                menu = MenuMapper.mapToMenu(resultSet, "");

                List<Long> productIds = findProductIds(id);
                List<Product> products = productRepositoryImpl.findProductsByIds(productIds);

                menu.setProducts(products);
            }

        } catch (SQLException exception) {
            throw new RuntimeException("Error while finding Menu by id", exception);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }

        return Optional.ofNullable(menu);
    }

    @Override
    public List<Menu> findAll() {

        Connection connection = CONNECTION_POOL.getConnection();
        List<Menu> menus;

        try (PreparedStatement statement = connection.prepareStatement(FIND_ALL_QUERY)) {

            ResultSet resultSet = statement.executeQuery();
            menus = MenuMapper.mapToMenuList(resultSet);

        } catch (SQLException exception) {
            throw new RuntimeException("Error while finding all Menus", exception);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }

        return menus;
    }

    @Override
    public boolean update(Menu menu) {

        Connection connection = CONNECTION_POOL.getConnection();

        try (PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {

            statement.setString(1, menu.getName());
            statement.setLong(2, menu.getId());

            return statement.executeUpdate() > 0;

        } catch (SQLException exception) {
            throw new RuntimeException("Error while updating Menu", exception);
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
            throw new RuntimeException("Error while deleting Menu", exception);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }
    }

    public List<Menu> findMenusByFoodSpotId(Long foodSpotId) {

        Connection connection = CONNECTION_POOL.getConnection();
        List<Menu> menus;

        try (PreparedStatement statement = connection.prepareStatement(FIND_BY_FOOD_SPOT_ID_QUERY)) {

            statement.setLong(1, foodSpotId);
            ResultSet resultSet = statement.executeQuery();

            menus = MenuMapper.mapToMenuList(resultSet);

        } catch (SQLException exception) {
            throw new RuntimeException("Error while finding Menus by foodSpotId", exception);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }

        return menus;
    }

    private List<Long> findProductIds(Long menuId) {

        Connection connection = CONNECTION_POOL.getConnection();
        List<Long> productIds = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(FIND_PRODUCT_IDS_QUERY)) {

            statement.setLong(1, menuId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                productIds.add(resultSet.getLong("product_id"));
            }

        } catch (SQLException exception) {
            throw new RuntimeException("Error while finding product IDs for Menu", exception);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }

        return productIds;
    }
}
