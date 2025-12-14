package com.solvd.fooddelivery.service.impl;

import com.solvd.fooddelivery.config.MyBatisSessionFactory;
import com.solvd.fooddelivery.entity.foodspot.Product;
import com.solvd.fooddelivery.repository.mybatis.dao.ProductDao;
import com.solvd.fooddelivery.service.ProductService;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class ProductServiceImpl implements ProductService {

    private static final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Override
    public Product create(Product product) {

        log.info("Creating product");

        validateForCreate(product);

        if (!product.isAvailable()) {
            product.setAvailable(true);
        }

        try (SqlSession session =
                     MyBatisSessionFactory.getSqlSessionFactory().openSession(false)) {

            ProductDao dao = session.getMapper(ProductDao.class);

            int rows = dao.create(product);
            if (rows != 1) {
                session.rollback();
                log.error("Failed to create product, affectedRows={}", rows);
                throw new IllegalStateException("Failed to create product");
            }

            session.commit();
            log.info("Product created successfully, id={}", product.getId());
            return product;

        } catch (RuntimeException e) {
            log.error("Unexpected error while creating product", e);
            throw e;
        }
    }

    @Override
    public Product update(Product product) {

        log.info("Updating product id={}", product != null ? product.getId() : null);

        if (product == null || product.getId() == null || product.getId() <= 0) {
            log.warn("Invalid Product for update");
            throw new IllegalArgumentException("Product id must be provided for update");
        }

        validateForCreate(product);

        try (SqlSession session =
                     MyBatisSessionFactory.getSqlSessionFactory().openSession(false)) {

            ProductDao dao = session.getMapper(ProductDao.class);

            int rows = dao.update(product);
            if (rows != 1) {
                session.rollback();
                log.warn("Product update failed, id={}", product.getId());
                throw new IllegalStateException(
                        "Product with id " + product.getId() + " does not exist");
            }

            session.commit();
            log.info("Product updated successfully, id={}", product.getId());
            return product;

        } catch (RuntimeException e) {
            log.error("Unexpected error while updating product id={}",
                    product.getId(), e);
            throw e;
        }
    }

    @Override
    public Optional<Product> findById(Long id) {

        log.debug("Finding product by id={}", id);

        if (id == null || id <= 0) {
            log.warn("Invalid Product id for findById");
            throw new IllegalArgumentException("Product id must be positive");
        }

        try (SqlSession session =
                     MyBatisSessionFactory.getSqlSessionFactory().openSession()) {

            Product product = session.getMapper(ProductDao.class).findById(id);
            return Optional.ofNullable(product);
        }
    }

    @Override
    public List<Product> findAll() {

        log.debug("Fetching all products");

        try (SqlSession session =
                     MyBatisSessionFactory.getSqlSessionFactory().openSession()) {

            return session.getMapper(ProductDao.class).findAll();
        }
    }

    @Override
    public void deleteById(Long id) {

        log.info("Deleting product id={}", id);

        if (id == null || id <= 0) {
            log.warn("Invalid Product id for delete");
            throw new IllegalArgumentException("Product id must be positive");
        }

        try (SqlSession session =
                     MyBatisSessionFactory.getSqlSessionFactory().openSession(false)) {

            ProductDao dao = session.getMapper(ProductDao.class);

            int rows = dao.deleteById(id);
            if (rows != 1) {
                session.rollback();
                log.warn("Product delete failed, id={}", id);
                throw new IllegalStateException(
                        "Product with id " + id + " does not exist");
            }

            session.commit();
            log.info("Product deleted successfully, id={}", id);

        } catch (RuntimeException e) {
            log.error("Unexpected error while deleting product id={}", id, e);
            throw e;
        }
    }

    private void validateForCreate(Product product) {

        if (product == null) {
            log.warn("Product validation failed: null product");
            throw new IllegalArgumentException("Product cannot be null");
        }

        if (product.getName() == null || product.getName().isBlank()) {
            throw new IllegalArgumentException("Product name is required");
        }

        if (product.getPrice() == null || product.getPrice().signum() <= 0) {
            throw new IllegalArgumentException("Product price must be positive");
        }
    }
}
