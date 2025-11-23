package com.solvd.fooddelivery.repository.impl;

import com.solvd.fooddelivery.entity.human.courier.Courier;
import com.solvd.fooddelivery.entity.human.courier.WorkingExperience;
import com.solvd.fooddelivery.repository.CrudRepository;
import com.solvd.fooddelivery.repository.connection.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CourierRepository implements CrudRepository<Courier, Long> {

    private static final ConnectionPool CONNECTION_POOL;
    private static final String CREATE_QUERY;
    private static final String FIND_BY_ID_QUERY;
    private static final String FIND_ALL_QUERY;
    private static final String UPDATE_QUERY;
    private static final String DELETE_QUERY;

    static {
        CONNECTION_POOL = ConnectionPool.getInstance();

        CREATE_QUERY = "insert into couriers (name, surname, email, license_number, years, months, days)" +
                " values (?, ?, ?, ?, ?, ?, ?)";

        FIND_BY_ID_QUERY = "select c.id, c.name, c.surname, c.phone_number, c.email, c.license_number, " +
                "c.years, c.months, c.days from couriers c where c.id = ?";

        FIND_ALL_QUERY = "select id, name, surname, phone_number, email, license_number, years," +
                " months, days from couriers";

        UPDATE_QUERY = "update couriers set name = ?, surname = ?, phone_number = ?, email = ?, " +
                "license_number = ?, years = ?, months = ?, days = ? where id = ?";

        DELETE_QUERY = "delete from couriers where id = ?";
    }

    @Override
    public boolean create(Courier entity) {

        Connection connection = CONNECTION_POOL.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(
                CREATE_QUERY, PreparedStatement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, entity.getName());
            statement.setString(2, entity.getSurname());
            statement.setString(3, entity.getEmail());
            statement.setString(4, entity.getLicenseNumber());
            statement.setInt(5, 0);
            statement.setInt(6, 0);
            statement.setInt(7, 0);

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
    public Optional<Courier> findById(Long id) {

        Connection connection = CONNECTION_POOL.getConnection();
        Courier courier = null;
        try (PreparedStatement statement = connection.prepareStatement(FIND_BY_ID_QUERY)) {

            statement.setLong(1, id);

            ResultSet result = statement.executeQuery();
            if (result.next()) {
                courier = mapToCourier(result);
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

            ResultSet result = statement.executeQuery();
            couriers = mapToListCourier(result);

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

            int years = 0, months = 0, days = 0;
            if (entity.getWorkingExperience() != null) {
                years = entity.getWorkingExperience().getYears();
                months = entity.getWorkingExperience().getMonths();
                days = entity.getWorkingExperience().getDays();
            }

            statement.setInt(6, years);
            statement.setInt(7, months);
            statement.setInt(8, days);
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

    public Courier mapToCourier(ResultSet resultSet) throws SQLException {

        Courier courier = new Courier();
        courier.setWorkingExperience(new WorkingExperience());
        courier.setId(resultSet.getLong("id"));
        courier.setName(resultSet.getString("name"));
        courier.setSurname(resultSet.getString("surname"));
        courier.setPhoneNumber(resultSet.getString("phone_number"));
        courier.setEmail(resultSet.getString("email"));
        courier.setLicenseNumber(resultSet.getString("license_number"));
        courier.getWorkingExperience().setYears(resultSet.getInt("years"));
        courier.getWorkingExperience().setMonths(resultSet.getInt("months"));
        courier.getWorkingExperience().setDays(resultSet.getInt("days"));

        return courier;
    }

    public List<Courier> mapToListCourier(ResultSet resultSet) throws SQLException {

        List<Courier> couriers = new ArrayList<>();
        while (resultSet.next()) {
            couriers.add(mapToCourier(resultSet));
        }
        return couriers;
    }
}
