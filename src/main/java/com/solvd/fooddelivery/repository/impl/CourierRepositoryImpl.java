package com.solvd.fooddelivery.repository.impl;

import com.solvd.fooddelivery.entity.human.courier.Courier;
import com.solvd.fooddelivery.entity.human.courier.WorkingExperience;
import com.solvd.fooddelivery.repository.CourierRepository;
import com.solvd.fooddelivery.repository.CrudRepository;
import com.solvd.fooddelivery.repository.connection.ConnectionPool;
import com.solvd.fooddelivery.repository.mappers.CourierMapper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CourierRepositoryImpl implements CourierRepository, CrudRepository<Courier, Long> {

    private static final ConnectionPool CONNECTION_POOL = ConnectionPool.getInstance();

    private static final String CREATE_QUERY =
            "INSERT INTO couriers (name, surname, phone_number, email, license_number, years, months, days) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String FIND_BY_ID_QUERY =
            "SELECT id, name, surname, phone_number, email, license_number, years, months, days " +
                    "FROM couriers WHERE id = ?";

    private static final String FIND_ALL_QUERY =
            "SELECT id, name, surname, phone_number, email, license_number, years, months, days " +
                    "FROM couriers";

    private static final String UPDATE_QUERY =
            "UPDATE couriers SET name = ?, surname = ?, phone_number = ?, email = ?, license_number = ?, " +
                    "years = ?, months = ?, days = ? WHERE id = ?";

    private static final String DELETE_QUERY =
            "DELETE FROM couriers WHERE id = ?";

    private static final String FIND_BY_LICENSE_NUMBER_QUERY =
            "SELECT id, name, surname, phone_number," +
                    " email, license_number, years, days FROM couriers WHERE license_number = ?";

    private static final String FIND_BY_EXPERIENCE_QUERY =
            "SELECT id, name, surname, phone_number, email, license_number, years, days " +
                    "FROM couriers WHERE years >= ?";

    @Override
    public boolean create(Courier entity) {

        Connection connection = CONNECTION_POOL.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(
                CREATE_QUERY, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, entity.getName());
            statement.setString(2, entity.getSurname());
            statement.setString(3, entity.getPhoneNumber());
            statement.setString(4, entity.getEmail());
            statement.setString(5, entity.getLicenseNumber());

            WorkingExperience exp = entity.getWorkingExperience();
            if (exp != null) {
                statement.setInt(6, exp.getYears());
                statement.setInt(7, exp.getMonths());
                statement.setInt(8, exp.getDays());
            } else {
                statement.setNull(6, Types.INTEGER);
                statement.setNull(7, Types.INTEGER);
                statement.setNull(8, Types.INTEGER);
            }

            statement.executeUpdate();
            ResultSet keys = statement.getGeneratedKeys();
            if (keys.next()) {
                entity.setId(keys.getLong(1));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }

        return entity.getId() != null;
    }

    @Override
    public Optional<Courier> findById(Long id) {

        Connection connection = CONNECTION_POOL.getConnection();
        Courier courier = null;

        try (PreparedStatement statement = connection.prepareStatement(FIND_BY_ID_QUERY)) {

            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                courier = CourierMapper.mapToCourier(rs, "");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }

        return Optional.ofNullable(courier);
    }

    @Override
    public List<Courier> findAll() {

        Connection connection = CONNECTION_POOL.getConnection();
        List<Courier> couriers;

        try (PreparedStatement statement = connection.prepareStatement(FIND_ALL_QUERY)) {

            ResultSet rs = statement.executeQuery();
            couriers = CourierMapper.mapToListCourier(rs);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }

        return couriers;
    }

    @Override
    public boolean update(Courier entity) {

        Connection connection = CONNECTION_POOL.getConnection();

        try (PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {

            statement.setString(1, entity.getName());
            statement.setString(2, entity.getSurname());
            statement.setString(3, entity.getPhoneNumber());
            statement.setString(4, entity.getEmail());
            statement.setString(5, entity.getLicenseNumber());

            WorkingExperience exp = entity.getWorkingExperience();
            statement.setInt(6, exp.getYears());
            statement.setInt(7, exp.getMonths());
            statement.setInt(8, exp.getDays());

            statement.setLong(9, entity.getId());

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

    @Override
    public Optional<Courier> findByLicenseNumber(String licenseNumber) {

        Connection connection = CONNECTION_POOL.getConnection();
        Courier courier = null;

        try (PreparedStatement statement = connection.prepareStatement(FIND_BY_LICENSE_NUMBER_QUERY)) {

            statement.setString(1, licenseNumber);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                courier = CourierMapper.mapToCourier(resultSet, "");
            }

        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }

        return Optional.ofNullable(courier);
    }

    @Override
    public List<Courier> findByExperience(int years) {

        Connection connection = CONNECTION_POOL.getConnection();
        List<Courier> couriers = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(FIND_BY_EXPERIENCE_QUERY)) {

            statement.setInt(1, years);
            ResultSet resultSet = statement.executeQuery();

            couriers = CourierMapper.mapToListCourier(resultSet);

        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        } finally {
            CONNECTION_POOL.releaseConnection(connection);
        }

        return couriers;
    }
}
