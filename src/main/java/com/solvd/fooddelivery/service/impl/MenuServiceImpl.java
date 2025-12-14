package com.solvd.fooddelivery.service.impl;

import com.solvd.fooddelivery.config.MyBatisSessionFactory;
import com.solvd.fooddelivery.entity.foodspot.Menu;
import com.solvd.fooddelivery.repository.mybatis.dao.MenuDao;
import com.solvd.fooddelivery.service.MenuService;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class MenuServiceImpl implements MenuService {

    private static final Logger log = LoggerFactory.getLogger(MenuServiceImpl.class);

    @Override
    public Menu create(Menu menu) {

        log.info("Creating menu");

        validateForCreate(menu);

        try (SqlSession session =
                     MyBatisSessionFactory.getSqlSessionFactory().openSession(false)) {

            MenuDao dao = session.getMapper(MenuDao.class);

            int rows = dao.create(menu);
            if (rows != 1) {
                session.rollback();
                log.error("Failed to create menu, affectedRows={}", rows);
                throw new IllegalStateException("Failed to create menu");
            }

            session.commit();
            log.info("Menu created successfully, id={}", menu.getId());
            return menu;

        } catch (RuntimeException e) {
            log.error("Unexpected error while creating menu", e);
            throw e;
        }
    }

    @Override
    public Menu update(Menu menu) {

        log.info("Updating menu id={}", menu != null ? menu.getId() : null);

        if (menu == null || menu.getId() == null || menu.getId() <= 0) {
            log.warn("Invalid Menu for update");
            throw new IllegalArgumentException("Menu id must be provided for update");
        }

        validateCommon(menu);

        try (SqlSession session =
                     MyBatisSessionFactory.getSqlSessionFactory().openSession(false)) {

            MenuDao dao = session.getMapper(MenuDao.class);

            int rows = dao.update(menu);
            if (rows != 1) {
                session.rollback();
                log.warn("Menu update failed, id={}", menu.getId());
                throw new IllegalStateException(
                        "Menu with id " + menu.getId() + " does not exist");
            }

            session.commit();
            log.info("Menu updated successfully, id={}", menu.getId());
            return menu;

        } catch (RuntimeException e) {
            log.error("Unexpected error while updating menu id={}",
                    menu.getId(), e);
            throw e;
        }
    }

    @Override
    public Optional<Menu> findById(Long id) {

        log.debug("Finding menu by id={}", id);

        if (id == null || id <= 0) {
            log.warn("Invalid Menu id for findById");
            throw new IllegalArgumentException("Menu id must be positive");
        }

        try (SqlSession session =
                     MyBatisSessionFactory.getSqlSessionFactory().openSession()) {

            Menu menu = session.getMapper(MenuDao.class).findById(id);
            return Optional.ofNullable(menu);
        }
    }

    @Override
    public List<Menu> findAll() {

        log.debug("Fetching all menus");

        try (SqlSession session =
                     MyBatisSessionFactory.getSqlSessionFactory().openSession()) {

            return session.getMapper(MenuDao.class).findAll();
        }
    }

    @Override
    public void deleteById(Long id) {

        log.info("Deleting menu id={}", id);

        if (id == null || id <= 0) {
            log.warn("Invalid Menu id for delete");
            throw new IllegalArgumentException("Menu id must be positive");
        }

        try (SqlSession session =
                     MyBatisSessionFactory.getSqlSessionFactory().openSession(false)) {

            MenuDao dao = session.getMapper(MenuDao.class);

            int rows = dao.deleteById(id);
            if (rows != 1) {
                session.rollback();
                log.warn("Menu delete failed, id={}", id);
                throw new IllegalStateException(
                        "Menu with id " + id + " does not exist");
            }

            session.commit();
            log.info("Menu deleted successfully, id={}", id);

        } catch (RuntimeException e) {
            log.error("Unexpected error while deleting menu id={}", id, e);
            throw e;
        }
    }

    private void validateForCreate(Menu menu) {

        if (menu == null) {
            log.warn("Menu validation failed: null menu");
            throw new IllegalArgumentException("Menu cannot be null");
        }

        if (menu.getId() != null) {
            log.warn("Menu validation failed: id must be null on create");
            throw new IllegalArgumentException("New menu must not have an id");
        }

        validateCommon(menu);
    }

    private void validateCommon(Menu menu) {

        if (menu.getName() == null || menu.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Menu name is required");
        }
    }
}
