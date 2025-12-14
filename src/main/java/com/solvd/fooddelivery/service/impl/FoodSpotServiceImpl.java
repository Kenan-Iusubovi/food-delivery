package com.solvd.fooddelivery.service.impl;

import com.solvd.fooddelivery.config.MyBatisSessionFactory;
import com.solvd.fooddelivery.entity.foodspot.FoodSpot;
import com.solvd.fooddelivery.entity.human.FoodSpotOwner;
import com.solvd.fooddelivery.repository.mybatis.dao.FoodSpotDao;
import com.solvd.fooddelivery.service.FoodSpotService;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class FoodSpotServiceImpl implements FoodSpotService {

    private static final Logger log = LoggerFactory.getLogger(FoodSpotServiceImpl.class);

    @Override
    public FoodSpot create(FoodSpot foodSpot) {

        log.info("Creating FoodSpot");

        validateForCreate(foodSpot);

        try (SqlSession session =
                     MyBatisSessionFactory.getSqlSessionFactory().openSession(false)) {

            FoodSpotDao dao = session.getMapper(FoodSpotDao.class);

            int rows = dao.create(foodSpot);
            if (rows != 1) {
                session.rollback();
                log.error("Failed to create FoodSpot, affectedRows={}", rows);
                throw new IllegalStateException("Failed to create FoodSpot");
            }

            session.commit();
            log.info("FoodSpot created successfully, id={}", foodSpot.getId());
            return foodSpot;

        } catch (RuntimeException e) {
            log.error("Unexpected error while creating FoodSpot", e);
            throw e;
        }
    }

    @Override
    public FoodSpot update(FoodSpot foodSpot) {

        log.info("Updating FoodSpot id={}", foodSpot != null ? foodSpot.getId() : null);

        if (foodSpot == null || foodSpot.getId() == null || foodSpot.getId() <= 0) {
            log.warn("Invalid FoodSpot for update");
            throw new IllegalArgumentException("FoodSpot id must be provided for update");
        }

        validateForUpdate(foodSpot);

        try (SqlSession session =
                     MyBatisSessionFactory.getSqlSessionFactory().openSession(false)) {

            FoodSpotDao dao = session.getMapper(FoodSpotDao.class);

            int rows = dao.update(foodSpot);
            if (rows != 1) {
                session.rollback();
                log.warn("FoodSpot update failed, id={}", foodSpot.getId());
                throw new IllegalStateException(
                        "FoodSpot with id " + foodSpot.getId() + " does not exist");
            }

            session.commit();
            log.info("FoodSpot updated successfully, id={}", foodSpot.getId());
            return foodSpot;

        } catch (RuntimeException e) {
            log.error("Unexpected error while updating FoodSpot id={}",
                    foodSpot.getId(), e);
            throw e;
        }
    }

    @Override
    public Optional<FoodSpot> findById(Long id) {

        log.debug("Finding FoodSpot by id={}", id);

        if (id == null || id <= 0) {
            log.warn("Invalid FoodSpot id for findById");
            throw new IllegalArgumentException("FoodSpot id must be positive");
        }

        try (SqlSession session =
                     MyBatisSessionFactory.getSqlSessionFactory().openSession()) {

            FoodSpotDao dao = session.getMapper(FoodSpotDao.class);
            return Optional.ofNullable(dao.findById(id));
        }
    }

    @Override
    public List<FoodSpot> findAll() {

        log.debug("Fetching all FoodSpots");

        try (SqlSession session =
                     MyBatisSessionFactory.getSqlSessionFactory().openSession()) {

            FoodSpotDao dao = session.getMapper(FoodSpotDao.class);
            return dao.findAll();
        }
    }

    @Override
    public void deleteById(Long id) {

        log.info("Deleting FoodSpot id={}", id);

        if (id == null || id <= 0) {
            log.warn("Invalid FoodSpot id for delete");
            throw new IllegalArgumentException("FoodSpot id must be positive");
        }

        try (SqlSession session =
                     MyBatisSessionFactory.getSqlSessionFactory().openSession(false)) {

            FoodSpotDao dao = session.getMapper(FoodSpotDao.class);

            int rows = dao.deleteById(id);
            if (rows != 1) {
                session.rollback();
                log.warn("FoodSpot delete failed, id={}", id);
                throw new IllegalStateException(
                        "FoodSpot with id " + id + " does not exist");
            }

            session.commit();
            log.info("FoodSpot deleted successfully, id={}", id);

        } catch (RuntimeException e) {
            log.error("Unexpected error while deleting FoodSpot id={}", id, e);
            throw e;
        }
    }

    private void validateForCreate(FoodSpot foodSpot) {

        if (foodSpot == null) {
            log.warn("FoodSpot validation failed: null");
            throw new IllegalArgumentException("FoodSpot cannot be null");
        }

        if (foodSpot.getId() != null) {
            log.warn("FoodSpot validation failed: id must be null on create");
            throw new IllegalArgumentException("New FoodSpot must not have an id");
        }

        validateCommon(foodSpot);
        validateOwner(foodSpot);
    }

    private void validateForUpdate(FoodSpot foodSpot) {
        validateCommon(foodSpot);
        validateOwner(foodSpot);
    }

    private void validateCommon(FoodSpot foodSpot) {

        if (isBlank(foodSpot.getName())) {
            throw new IllegalArgumentException("FoodSpot name is required");
        }

        if (isBlank(foodSpot.getAddress())) {
            throw new IllegalArgumentException("FoodSpot address is required");
        }

        if (isBlank(foodSpot.getPhoneNumber())) {
            throw new IllegalArgumentException("FoodSpot phone number is required");
        }

        if (foodSpot.getOpeningTime() == null || foodSpot.getClosingTime() == null) {
            throw new IllegalArgumentException("Opening and closing times are required");
        }

        if (foodSpot.getOpeningTime().isAfter(foodSpot.getClosingTime())) {
            throw new IllegalArgumentException("Opening time must be before closing time");
        }
    }

    private void validateOwner(FoodSpot foodSpot) {

        FoodSpotOwner owner = foodSpot.getOwner();

        if (owner == null) {
            throw new IllegalArgumentException("FoodSpot must have an owner");
        }

        if (owner.getId() == null || owner.getId() <= 0) {
            throw new IllegalArgumentException("FoodSpot owner must have a valid id");
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
