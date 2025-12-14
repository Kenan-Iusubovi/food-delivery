package com.solvd.fooddelivery.service.impl;

import com.solvd.fooddelivery.config.MyBatisSessionFactory;
import com.solvd.fooddelivery.entity.human.FoodSpotOwner;
import com.solvd.fooddelivery.repository.mybatis.dao.FoodSpotOwnerDao;
import com.solvd.fooddelivery.service.FoodSpotOwnerService;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class FoodSpotOwnerServiceImpl implements FoodSpotOwnerService {

    private static final Logger log = LoggerFactory.getLogger(FoodSpotOwnerServiceImpl.class);

    @Override
    public FoodSpotOwner create(FoodSpotOwner owner) {

        log.info("Creating FoodSpotOwner");

        validateForCreate(owner);

        try (SqlSession session =
                     MyBatisSessionFactory.getSqlSessionFactory().openSession(false)) {

            FoodSpotOwnerDao dao = session.getMapper(FoodSpotOwnerDao.class);

            if (dao.findByEmail(owner.getEmail()) != null) {
                log.warn("FoodSpotOwner creation failed: duplicate email={}", owner.getEmail());
                throw new IllegalArgumentException(
                        "FoodSpotOwner with email '" + owner.getEmail() + "' already exists");
            }

            if (dao.findByPhoneNumber(owner.getPhoneNumber()) != null) {
                log.warn("FoodSpotOwner creation failed: duplicate phoneNumber={}",
                        owner.getPhoneNumber());
                throw new IllegalArgumentException(
                        "FoodSpotOwner with phone number '" + owner.getPhoneNumber() + "' already exists");
            }

            int rows = dao.create(owner);
            if (rows != 1) {
                session.rollback();
                log.error("Failed to create FoodSpotOwner, affectedRows={}", rows);
                throw new IllegalStateException("Failed to create food spot owner");
            }

            session.commit();
            log.info("FoodSpotOwner created successfully, id={}", owner.getId());
            return owner;

        } catch (RuntimeException e) {
            log.error("Unexpected error while creating FoodSpotOwner", e);
            throw e;
        }
    }

    @Override
    public FoodSpotOwner update(FoodSpotOwner owner) {

        log.info("Updating FoodSpotOwner id={}", owner != null ? owner.getId() : null);

        if (owner == null || owner.getId() == null || owner.getId() <= 0) {
            log.warn("Invalid FoodSpotOwner for update");
            throw new IllegalArgumentException("FoodSpotOwner id must be provided for update");
        }

        validateCommon(owner);

        try (SqlSession session =
                     MyBatisSessionFactory.getSqlSessionFactory().openSession(false)) {

            FoodSpotOwnerDao dao = session.getMapper(FoodSpotOwnerDao.class);

            int rows = dao.update(owner);
            if (rows != 1) {
                session.rollback();
                log.warn("FoodSpotOwner update failed, id={}", owner.getId());
                throw new IllegalStateException(
                        "FoodSpotOwner with id " + owner.getId() + " does not exist");
            }

            session.commit();
            log.info("FoodSpotOwner updated successfully, id={}", owner.getId());
            return owner;

        } catch (RuntimeException e) {
            log.error("Unexpected error while updating FoodSpotOwner id={}", owner.getId(), e);
            throw e;
        }
    }

    @Override
    public Optional<FoodSpotOwner> findById(Long id) {

        log.debug("Finding FoodSpotOwner by id={}", id);

        if (id == null || id <= 0) {
            log.warn("Invalid FoodSpotOwner id for findById");
            throw new IllegalArgumentException("FoodSpotOwner id must be positive");
        }

        try (SqlSession session =
                     MyBatisSessionFactory.getSqlSessionFactory().openSession()) {

            FoodSpotOwner owner = session.getMapper(FoodSpotOwnerDao.class).findById(id);
            return Optional.ofNullable(owner);
        }
    }

    @Override
    public List<FoodSpotOwner> findAll() {

        log.debug("Fetching all FoodSpotOwners");

        try (SqlSession session =
                     MyBatisSessionFactory.getSqlSessionFactory().openSession()) {

            return session.getMapper(FoodSpotOwnerDao.class).findAll();
        }
    }

    @Override
    public void deleteById(Long id) {

        log.info("Deleting FoodSpotOwner id={}", id);

        if (id == null || id <= 0) {
            log.warn("Invalid FoodSpotOwner id for delete");
            throw new IllegalArgumentException("FoodSpotOwner id must be positive");
        }

        try (SqlSession session =
                     MyBatisSessionFactory.getSqlSessionFactory().openSession(false)) {

            FoodSpotOwnerDao dao = session.getMapper(FoodSpotOwnerDao.class);

            int rows = dao.deleteById(id);
            if (rows != 1) {
                session.rollback();
                log.warn("FoodSpotOwner delete failed, id={}", id);
                throw new IllegalStateException(
                        "FoodSpotOwner with id " + id + " does not exist");
            }

            session.commit();
            log.info("FoodSpotOwner deleted successfully, id={}", id);

        } catch (RuntimeException e) {
            log.error("Unexpected error while deleting FoodSpotOwner id={}", id, e);
            throw e;
        }
    }

    private void validateForCreate(FoodSpotOwner owner) {

        if (owner == null) {
            log.warn("FoodSpotOwner validation failed: null owner");
            throw new IllegalArgumentException("FoodSpotOwner cannot be null");
        }

        if (owner.getId() != null) {
            log.warn("FoodSpotOwner validation failed: id must be null on create");
            throw new IllegalArgumentException("New FoodSpotOwner must not have an id");
        }

        validateCommon(owner);
    }

    private void validateCommon(FoodSpotOwner owner) {

        if (isBlank(owner.getName())) {
            throw new IllegalArgumentException("FoodSpotOwner name is required");
        }

        if (isBlank(owner.getSurname())) {
            throw new IllegalArgumentException("FoodSpotOwner surname is required");
        }

        if (isBlank(owner.getPhoneNumber())) {
            throw new IllegalArgumentException("FoodSpotOwner phone number is required");
        }

        if (isBlank(owner.getEmail())) {
            throw new IllegalArgumentException("FoodSpotOwner email is required");
        }

        if (isBlank(owner.getBusinessLicense())) {
            throw new IllegalArgumentException("Business license is required");
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
