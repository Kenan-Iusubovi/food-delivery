package com.solvd.fooddelivery.repository.impl;

import com.solvd.fooddelivery.entity.foodspot.FoodSpot;
import com.solvd.fooddelivery.repository.CrudRepository;
import com.solvd.fooddelivery.repository.FoodSpotRepository;
import com.solvd.fooddelivery.repository.connection.ConnectionPool;
import com.solvd.fooddelivery.repository.mappers.FoodSpotMapper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FoodSpotRepositoryImpl implements FoodSpotRepository, CrudRepository<FoodSpot, Long> {

    private static final ConnectionPool CONNECTION_POOL = ConnectionPool.getInstance();

    private static final String CREATE_QUERY =
            "INSERT INTO food_spots (name, address, phone, opening_time, closing_time, food_spot_owner_id) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";

    private static final String FIND_BY_ID_QUERY =
            "SELECT id, name, address, phone, opening_time, closing_time " +
                    "FROM food_spots WHERE id = ?";

    private static final String FIND_ALL_QUERY =
            "SELECT id, name, address, phone, opening_time, closing_time " +
                    "FROM food_spots";

    private static final String UPDATE_QUERY =
            "UPDATE food_spots SET name = ?, address = ?, phone = ?, opening_time = ?, closing_time = ? " +
                    "WHERE id = ?";

    private static final String DELETE_QUERY =
            "DELETE FROM food_spots WHERE id = ?";

    private static final String FIND_BY_OWNER_ID_QUERY =
            "SELECT id, name, address, phone, opening_time, closing_time " +
                    "FROM food_spots WHERE food_spot_owner_id = ?";

    private static final String FIND_BY_NAME_QUERY =
            "SELECT id, name, address, phone, opening_time, closing_time " +
                    "FROM food_spots WHERE name LIKE ?";

    private final MenuRepositoryImpl menuRepositoryImpl = new MenuRepositoryImpl();
    private final OrderRepositoryImpl orderRepositoryImpl = new OrderRepositoryImpl();

    @Override
    public boolean create(FoodSpot foodSpot) {
        throw new UnsupportedOperationException("Create FoodSpot not implemented because FoodSpot has no owner field to map food_spot_owner_id.");
    }

    @Override
    public Optional<FoodSpot> findById(Long id) {

        Connection connection = CONNECTION_POOL.getConnection();
        FoodSpot foodSpot = null;

        try (PreparedStatement statement = connection.prepareStatement(FIND_BY_ID_QUERY)) {

            statement.setLong(1, id);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {

                foodSpot = FoodSpotMapper.mapToFoodSpot(resultSet, "");

                foodSpot.setMenus(menuRepositoryImpl.findMenusByFoodSpotId(id));
                foodSpot.setOrders(orderRepositoryImpl.findOrdersByFoodSpotId(id));
            }

        } catch (SQLException exception) {
            throw new RuntimeException("Error while finding FoodSpot by id", exception);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }

        return Optional.ofNullable(foodSpot);
    }

    @Override
    public List<FoodSpot> findAll() {

        Connection connection = CONNECTION_POOL.getConnection();
        List<FoodSpot> foodSpots;

        try (PreparedStatement statement = connection.prepareStatement(FIND_ALL_QUERY)) {

            ResultSet resultSet = statement.executeQuery();
            foodSpots = FoodSpotMapper.mapToFoodSpotList(resultSet);

        } catch (SQLException exception) {
            throw new RuntimeException("Error while finding all FoodSpots", exception);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }

        return foodSpots;
    }

    @Override
    public boolean update(FoodSpot foodSpot) {

        Connection connection = CONNECTION_POOL.getConnection();

        try (PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {

            statement.setString(1, foodSpot.getName());
            statement.setString(2, foodSpot.getAddress());
            statement.setString(3, foodSpot.getPhoneNumber());
            statement.setTime(4, Time.valueOf(foodSpot.getOpeningTime()));
            statement.setTime(5, Time.valueOf(foodSpot.getClosingTime()));
            statement.setLong(6, foodSpot.getId());

            return statement.executeUpdate() > 0;

        } catch (SQLException exception) {
            throw new RuntimeException("Error while updating FoodSpot", exception);
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
            throw new RuntimeException("Error while deleting FoodSpot", exception);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }
    }

    @Override
    public List<FoodSpot> findByOwnerId(Long ownerId) {

        Connection connection = CONNECTION_POOL.getConnection();
        List<FoodSpot> foodSpots = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(FIND_BY_OWNER_ID_QUERY)) {

            statement.setLong(1, ownerId);
            ResultSet resultSet = statement.executeQuery();

            foodSpots = FoodSpotMapper.mapToFoodSpotList(resultSet);

        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }

        return foodSpots;
    }

    @Override
    public List<FoodSpot> findByName(String namePattern) {

        Connection connection = CONNECTION_POOL.getConnection();
        List<FoodSpot> foodSpots = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(FIND_BY_NAME_QUERY)) {

            statement.setString(1, "%" + namePattern + "%");
            ResultSet resultSet = statement.executeQuery();

            foodSpots = FoodSpotMapper.mapToFoodSpotList(resultSet);

        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }

        return foodSpots;
    }

}
