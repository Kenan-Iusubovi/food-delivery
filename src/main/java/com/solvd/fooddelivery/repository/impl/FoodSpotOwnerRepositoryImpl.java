package com.solvd.fooddelivery.repository.impl;

import com.solvd.fooddelivery.entity.human.FoodSpotOwner;
import com.solvd.fooddelivery.repository.CrudRepository;
import com.solvd.fooddelivery.repository.connection.ConnectionPool;
import com.solvd.fooddelivery.repository.mappers.FoodSpotOwnerMapper;

import java.sql.*;
import java.util.List;
import java.util.Optional;

public class FoodSpotOwnerRepositoryImpl implements CrudRepository<FoodSpotOwner, Long> {

    private static final ConnectionPool CONNECTION_POOL = ConnectionPool.getInstance();

    private static final String CREATE_QUERY =
            "INSERT INTO food_spot_owners (name, surname, phone_number, email, business_license) " +
                    "VALUES (?, ?, ?, ?, ?)";

    private static final String FIND_BY_ID_QUERY =
            "SELECT id, name, surname, phone_number, email, business_license " +
                    "FROM food_spot_owners WHERE id = ?";

    private static final String FIND_ALL_QUERY =
            "SELECT id, name, surname, phone_number, email, business_license " +
                    "FROM food_spot_owners";

    private static final String UPDATE_QUERY =
            "UPDATE food_spot_owners SET name = ?, surname = ?, phone_number = ?, email = ?, business_license = ? " +
                    "WHERE id = ?";

    private static final String DELETE_QUERY =
            "DELETE FROM food_spot_owners WHERE id = ?";

    @Override
    public boolean create(FoodSpotOwner entity) {

        Connection connection = CONNECTION_POOL.getConnection();

        try (PreparedStatement statement = connection.prepareStatement(
                CREATE_QUERY, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, entity.getName());
            statement.setString(2, entity.getSurname());
            statement.setString(3, entity.getPhoneNumber());
            statement.setString(4, entity.getEmail());
            statement.setString(5, entity.getBusinessLicense());

            statement.executeUpdate();

            ResultSet keys = statement.getGeneratedKeys();
            if (keys.next()) {
                entity.setId(keys.getLong(1));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error creating FoodSpotOwner", e);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }

        return entity.getId() != null;
    }

    @Override
    public Optional<FoodSpotOwner> findById(Long id) {

        Connection connection = CONNECTION_POOL.getConnection();
        FoodSpotOwner owner = null;

        try (PreparedStatement statement = connection.prepareStatement(FIND_BY_ID_QUERY)) {

            statement.setLong(1, id);
            ResultSet result = statement.executeQuery();

            if (result.next()) {
                owner = FoodSpotOwnerMapper.mapToFoodSpotOwner(result, "");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error finding FoodSpotOwner by ID", e);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }

        return Optional.ofNullable(owner);
    }

    @Override
    public List<FoodSpotOwner> findAll() {

        Connection connection = CONNECTION_POOL.getConnection();
        List<FoodSpotOwner> owners;

        try (PreparedStatement statement = connection.prepareStatement(FIND_ALL_QUERY)) {

            ResultSet result = statement.executeQuery();
            owners = FoodSpotOwnerMapper.mapToFoodSpotOwnerList(result);

        } catch (SQLException e) {
            throw new RuntimeException("Error finding all FoodSpotOwners", e);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }

        return owners;
    }

    @Override
    public boolean update(FoodSpotOwner entity) {

        Connection connection = CONNECTION_POOL.getConnection();

        try (PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {

            statement.setString(1, entity.getName());
            statement.setString(2, entity.getSurname());
            statement.setString(3, entity.getPhoneNumber());
            statement.setString(4, entity.getEmail());
            statement.setString(5, entity.getBusinessLicense());
            statement.setLong(6, entity.getId());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error updating FoodSpotOwner", e);
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
            throw new RuntimeException("Error deleting FoodSpotOwner", e);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }
    }
}
