package com.solvd.fooddelivery.service.impl;

import com.solvd.fooddelivery.config.MyBatisSessionFactory;
import com.solvd.fooddelivery.entity.human.courier.Courier;
import com.solvd.fooddelivery.entity.human.courier.WorkingExperience;
import com.solvd.fooddelivery.repository.mybatis.dao.CourierDao;
import com.solvd.fooddelivery.service.CourierService;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class CourierServiceImpl implements CourierService {

    private static final Logger log = LoggerFactory.getLogger(CourierServiceImpl.class);

    @Override
    public Courier create(Courier courier) {

        log.info("Creating courier");

        validateCourierForCreate(courier);

        try (SqlSession session =
                     MyBatisSessionFactory.getSqlSessionFactory().openSession(false)) {

            CourierDao dao = session.getMapper(CourierDao.class);

            if (dao.findByEmail(courier.getEmail()) != null) {
                log.warn("Courier creation failed: duplicate email={}", courier.getEmail());
                throw new IllegalArgumentException(
                        "Courier with email '" + courier.getEmail() + "' already exists");
            }

            if (dao.findByLicenseNumber(courier.getLicenseNumber()) != null) {
                log.warn("Courier creation failed: duplicate licenseNumber={}",
                        courier.getLicenseNumber());
                throw new IllegalArgumentException(
                        "Courier with license number '" + courier.getLicenseNumber() + "' already exists");
            }

            int rows = dao.create(courier);
            if (rows != 1) {
                session.rollback();
                log.error("Failed to create courier, affectedRows={}", rows);
                throw new IllegalStateException("Failed to create courier");
            }

            session.commit();
            log.info("Courier created successfully, id={}", courier.getId());
            return courier;

        } catch (RuntimeException e) {
            log.error("Unexpected error while creating courier", e);
            throw e;
        }
    }

    @Override
    public Courier update(Courier courier) {

        log.info("Updating courier id={}", courier != null ? courier.getId() : null);

        if (courier == null || courier.getId() == null || courier.getId() <= 0) {
            log.warn("Invalid courier for update");
            throw new IllegalArgumentException("Courier id must be provided for update");
        }

        validateCourierForUpdate(courier);

        try (SqlSession session =
                     MyBatisSessionFactory.getSqlSessionFactory().openSession(false)) {

            CourierDao dao = session.getMapper(CourierDao.class);

            int rows = dao.update(courier);
            if (rows != 1) {
                session.rollback();
                log.warn("Courier update failed, id={}", courier.getId());
                throw new IllegalStateException(
                        "Courier with id " + courier.getId() + " does not exist");
            }

            session.commit();
            log.info("Courier updated successfully, id={}", courier.getId());
            return courier;

        } catch (RuntimeException e) {
            log.error("Unexpected error while updating courier id={}", courier.getId(), e);
            throw e;
        }
    }

    @Override
    public Optional<Courier> findById(Long id) {

        log.debug("Finding courier by id={}", id);

        if (id == null || id <= 0) {
            log.warn("Invalid courier id for findById");
            throw new IllegalArgumentException("Courier id must be positive");
        }

        try (SqlSession session =
                     MyBatisSessionFactory.getSqlSessionFactory().openSession()) {

            Courier courier = session.getMapper(CourierDao.class).findById(id);
            return Optional.ofNullable(courier);
        }
    }

    @Override
    public List<Courier> findAll() {

        log.debug("Fetching all couriers");

        try (SqlSession session =
                     MyBatisSessionFactory.getSqlSessionFactory().openSession()) {

            return session.getMapper(CourierDao.class).findAll();
        }
    }

    @Override
    public void deleteById(Long id) {

        log.info("Deleting courier id={}", id);

        if (id == null || id <= 0) {
            log.warn("Invalid courier id for delete");
            throw new IllegalArgumentException("Courier id must be positive");
        }

        try (SqlSession session =
                     MyBatisSessionFactory.getSqlSessionFactory().openSession(false)) {

            CourierDao dao = session.getMapper(CourierDao.class);

            int rows = dao.deleteById(id);
            if (rows != 1) {
                session.rollback();
                log.warn("Courier delete failed, id={}", id);
                throw new IllegalStateException(
                        "Courier with id " + id + " does not exist");
            }

            session.commit();
            log.info("Courier deleted successfully, id={}", id);

        } catch (RuntimeException e) {
            log.error("Unexpected error while deleting courier id={}", id, e);
            throw e;
        }
    }

    private void validateCourierForCreate(Courier courier) {

        if (courier == null) {
            log.warn("Courier validation failed: null courier");
            throw new IllegalArgumentException("Courier cannot be null");
        }

        if (courier.getId() != null) {
            log.warn("Courier validation failed: id must be null on create");
            throw new IllegalArgumentException("New courier must not have an id");
        }

        validateCommonFields(courier);
    }

    private void validateCourierForUpdate(Courier courier) {
        validateCommonFields(courier);
    }

    private void validateCommonFields(Courier courier) {

        if (isBlank(courier.getName())) {
            throw new IllegalArgumentException("Courier name is required");
        }

        if (isBlank(courier.getSurname())) {
            throw new IllegalArgumentException("Courier surname is required");
        }

        if (isBlank(courier.getPhoneNumber())) {
            throw new IllegalArgumentException("Courier phone number is required");
        }

        if (isBlank(courier.getEmail())) {
            throw new IllegalArgumentException("Courier email is required");
        }

        if (isBlank(courier.getLicenseNumber())) {
            throw new IllegalArgumentException("Courier license number is required");
        }

        WorkingExperience exp = courier.getWorkingExperience();
        if (exp == null) {
            throw new IllegalArgumentException("Working experience must be provided");
        }

        if (exp.getYears() < 0 || exp.getMonths() < 0 || exp.getDays() < 0) {
            throw new IllegalArgumentException(
                    "Working experience values must be non-negative");
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
