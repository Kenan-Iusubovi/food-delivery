package com.solvd.fooddelivery.service.impl;

import com.solvd.fooddelivery.config.MyBatisSessionFactory;
import com.solvd.fooddelivery.entity.human.Customer;
import com.solvd.fooddelivery.repository.mybatis.dao.CustomerDao;
import com.solvd.fooddelivery.service.CustomerService;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class CustomerServiceImpl implements CustomerService {

    private static final Logger log = LoggerFactory.getLogger(CustomerServiceImpl.class);

    @Override
    public Customer create(Customer customer) {

        log.info("Creating customer");

        validateForCreate(customer);

        try (SqlSession session =
                     MyBatisSessionFactory.getSqlSessionFactory().openSession(false)) {

            CustomerDao dao = session.getMapper(CustomerDao.class);

            if (dao.findByEmail(customer.getEmail()) != null) {
                log.warn("Customer creation failed: duplicate email={}", customer.getEmail());
                throw new IllegalArgumentException(
                        "Customer with email '" + customer.getEmail() + "' already exists");
            }

            if (dao.findByPhoneNumber(customer.getPhoneNumber()) != null) {
                log.warn("Customer creation failed: duplicate phoneNumber={}",
                        customer.getPhoneNumber());
                throw new IllegalArgumentException(
                        "Customer with phone number '" + customer.getPhoneNumber() + "' already exists");
            }

            int rows = dao.create(customer);
            if (rows != 1) {
                session.rollback();
                log.error("Failed to create customer, affectedRows={}", rows);
                throw new IllegalStateException("Failed to create customer");
            }

            session.commit();
            log.info("Customer created successfully, id={}", customer.getId());
            return customer;

        } catch (RuntimeException e) {
            log.error("Unexpected error while creating customer", e);
            throw e;
        }
    }

    @Override
    public Customer update(Customer customer) {

        log.info("Updating customer id={}", customer != null ? customer.getId() : null);

        if (customer == null || customer.getId() == null || customer.getId() <= 0) {
            log.warn("Invalid customer for update");
            throw new IllegalArgumentException("Customer id must be provided for update");
        }

        validateCommon(customer);

        try (SqlSession session =
                     MyBatisSessionFactory.getSqlSessionFactory().openSession(false)) {

            CustomerDao dao = session.getMapper(CustomerDao.class);

            int rows = dao.update(customer);
            if (rows != 1) {
                session.rollback();
                log.warn("Customer update failed, id={}", customer.getId());
                throw new IllegalStateException(
                        "Customer with id " + customer.getId() + " does not exist");
            }

            session.commit();
            log.info("Customer updated successfully, id={}", customer.getId());
            return customer;

        } catch (RuntimeException e) {
            log.error("Unexpected error while updating customer id={}", customer.getId(), e);
            throw e;
        }
    }

    @Override
    public Optional<Customer> findById(Long id) {

        log.debug("Finding customer by id={}", id);

        if (id == null || id <= 0) {
            log.warn("Invalid customer id for findById");
            throw new IllegalArgumentException("Customer id must be positive");
        }

        try (SqlSession session =
                     MyBatisSessionFactory.getSqlSessionFactory().openSession()) {

            Customer customer = session.getMapper(CustomerDao.class).findById(id);
            return Optional.ofNullable(customer);
        }
    }

    @Override
    public List<Customer> findAll() {

        log.debug("Fetching all customers");

        try (SqlSession session =
                     MyBatisSessionFactory.getSqlSessionFactory().openSession()) {

            return session.getMapper(CustomerDao.class).findAll();
        }
    }

    @Override
    public void deleteById(Long id) {

        log.info("Deleting customer id={}", id);

        if (id == null || id <= 0) {
            log.warn("Invalid customer id for delete");
            throw new IllegalArgumentException("Customer id must be positive");
        }

        try (SqlSession session =
                     MyBatisSessionFactory.getSqlSessionFactory().openSession(false)) {

            CustomerDao dao = session.getMapper(CustomerDao.class);

            int rows = dao.deleteById(id);
            if (rows != 1) {
                session.rollback();
                log.warn("Customer delete failed, id={}", id);
                throw new IllegalStateException(
                        "Customer with id " + id + " does not exist");
            }

            session.commit();
            log.info("Customer deleted successfully, id={}", id);

        } catch (RuntimeException e) {
            log.error("Unexpected error while deleting customer id={}", id, e);
            throw e;
        }
    }

    private void validateForCreate(Customer customer) {

        if (customer == null) {
            log.warn("Customer validation failed: null customer");
            throw new IllegalArgumentException("Customer cannot be null");
        }

        if (customer.getId() != null) {
            log.warn("Customer validation failed: id must be null on create");
            throw new IllegalArgumentException("New customer must not have an id");
        }

        validateCommon(customer);

        if (customer.getBalance() == null) {
            customer.setBalance(BigDecimal.ZERO);
        }
    }

    private void validateCommon(Customer customer) {

        if (isBlank(customer.getName())) {
            throw new IllegalArgumentException("Customer name is required");
        }

        if (isBlank(customer.getSurname())) {
            throw new IllegalArgumentException("Customer surname is required");
        }

        if (isBlank(customer.getPhoneNumber())) {
            throw new IllegalArgumentException("Customer phone number is required");
        }

        if (isBlank(customer.getEmail())) {
            throw new IllegalArgumentException("Customer email is required");
        }

        if (customer.getBalance() != null &&
                customer.getBalance().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Customer balance cannot be negative");
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
