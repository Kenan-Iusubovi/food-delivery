package com.solvd.fooddelivery.service.impl;

import com.solvd.fooddelivery.config.MyBatisSessionFactory;
import com.solvd.fooddelivery.designpatterns.abstractfactory.DefaultOrderProcessingFactory;
import com.solvd.fooddelivery.designpatterns.abstractfactory.OrderProcessingFactory;
import com.solvd.fooddelivery.designpatterns.listener.OrderEventPublisher;
import com.solvd.fooddelivery.entity.order.Order;
import com.solvd.fooddelivery.repository.mybatis.dao.OrderDao;
import com.solvd.fooddelivery.service.OrderService;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class OrderServiceImpl implements OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

    private final OrderProcessingFactory processingFactory = new DefaultOrderProcessingFactory();

    private final OrderEventPublisher eventPublisher;


    public OrderServiceImpl(OrderEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public Order create(Order order) {

        log.info("Creating order");

        validateForCreate(order);

        order.setOrderNumber(UUID.randomUUID());
        order.setFinished(false);

        BigDecimal totalPrice =
                processingFactory
                        .createPriceCalculationStrategy(order)
                        .calculate(order);

        order.setTotalPrice(totalPrice);

        try (SqlSession session =
                     MyBatisSessionFactory.getSqlSessionFactory().openSession(false)) {

            OrderDao dao = session.getMapper(OrderDao.class);

            int rows = dao.create(order);
            if (rows != 1) {
                session.rollback();
                log.error("Failed to create order, affectedRows={}", rows);
                throw new IllegalStateException("Failed to create order");
            }

            session.commit();

            eventPublisher.notifyOrderCreated(order);

            log.info("Order created successfully, id={}, orderNumber={}",
                    order.getId(), order.getOrderNumber());

            return order;

        } catch (RuntimeException e) {
            log.error("Unexpected error while creating order", e);
            throw e;
        }
    }


    @Override
    public Order update(Order order) {

        log.info("Updating order id={}", order != null ? order.getId() : null);

        if (order == null || order.getId() == null || order.getId() <= 0) {
            log.warn("Invalid Order for update");
            throw new IllegalArgumentException("Order id must be provided for update");
        }

        validateForUpdate(order);

        try (SqlSession session =
                     MyBatisSessionFactory.getSqlSessionFactory().openSession(false)) {

            OrderDao dao = session.getMapper(OrderDao.class);

            Order existingOrder = dao.findById(order.getId());
            if (existingOrder == null) {
                session.rollback();
                throw new IllegalStateException(
                        "Order with id " + order.getId() + " does not exist");
            }

            boolean wasFinished = existingOrder.isFinished();
            boolean willBeFinished = order.isFinished();

            int rows = dao.update(order);
            if (rows != 1) {
                session.rollback();
                log.warn("Order update failed, id={}", order.getId());
                throw new IllegalStateException(
                        "Order with id " + order.getId() + " does not exist");
            }

            session.commit();

            if (!wasFinished && willBeFinished) {
                eventPublisher.notifycationOnFinished(order);
            }

            log.info("Order updated successfully, id={}, finished={}",
                    order.getId(), order.isFinished());

            return order;

        } catch (RuntimeException e) {
            log.error("Unexpected error while updating order id={}",
                    order.getId(), e);
            throw e;
        }
    }


    @Override
    public Optional<Order> findById(Long id) {

        log.debug("Finding order by id={}", id);

        if (id == null || id <= 0) {
            log.warn("Invalid Order id for findById");
            throw new IllegalArgumentException("Order id must be positive");
        }

        try (SqlSession session =
                     MyBatisSessionFactory.getSqlSessionFactory().openSession()) {

            Order order = session.getMapper(OrderDao.class).findById(id);
            return Optional.ofNullable(order);
        }
    }

    @Override
    public List<Order> findAll() {

        log.debug("Fetching all orders");

        try (SqlSession session =
                     MyBatisSessionFactory.getSqlSessionFactory().openSession()) {

            return session.getMapper(OrderDao.class).findAll();
        }
    }

    @Override
    public void deleteById(Long id) {

        log.info("Deleting order id={}", id);

        if (id == null || id <= 0) {
            log.warn("Invalid Order id for delete");
            throw new IllegalArgumentException("Order id must be positive");
        }

        try (SqlSession session =
                     MyBatisSessionFactory.getSqlSessionFactory().openSession(false)) {

            OrderDao dao = session.getMapper(OrderDao.class);

            int rows = dao.deleteById(id);
            if (rows != 1) {
                session.rollback();
                log.warn("Order delete failed, id={}", id);
                throw new IllegalStateException(
                        "Order with id " + id + " does not exist");
            }

            session.commit();
            log.info("Order deleted successfully, id={}", id);

        } catch (RuntimeException e) {
            log.error("Unexpected error while deleting order id={}", id, e);
            throw e;
        }
    }

    private void validateForCreate(Order order) {

        if (order == null) {
            log.warn("Order validation failed: null order");
            throw new IllegalArgumentException("Order cannot be null");
        }

        if (order.getCourier() == null || order.getCourier().getId() == null) {
            throw new IllegalArgumentException("Order must have a courier");
        }

        if (order.getCustomer() == null || order.getCustomer().getId() == null) {
            throw new IllegalArgumentException("Order must have a customer");
        }

        if (order.getFoodSpot() == null || order.getFoodSpot().getId() == null) {
            throw new IllegalArgumentException("Order must have a food spot");
        }

        if (order.getProducts() == null || order.getProducts().isEmpty()) {
            throw new IllegalArgumentException("Order must contain products");
        }

        if (order.getTakeAddress() == null || order.getTakeAddress().isBlank()) {
            throw new IllegalArgumentException("Take address is required");
        }

        if (order.getBringAddress() == null || order.getBringAddress().isBlank()) {
            throw new IllegalArgumentException("Bring address is required");
        }

        if (order.getOrderDateTime() == null) {
            throw new IllegalArgumentException("Order date time is required");
        }
    }

    private void validateForUpdate(Order order) {

        if (order == null || order.getId() == null || order.getId() <= 0) {
            log.warn("Invalid Order for update");
            throw new IllegalArgumentException("Order id must be provided for update");
        }

        if (order.getCourier() == null || order.getCourier().getId() == null) {
            throw new IllegalArgumentException("Order must have a courier");
        }

        if (order.getCustomer() == null || order.getCustomer().getId() == null) {
            throw new IllegalArgumentException("Order must have a customer");
        }

        if (order.getFoodSpot() == null || order.getFoodSpot().getId() == null) {
            throw new IllegalArgumentException("Order must have a food spot");
        }

        if (order.getTotalPrice() == null ||
                order.getTotalPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Order total price must be positive");
        }
    }
}
