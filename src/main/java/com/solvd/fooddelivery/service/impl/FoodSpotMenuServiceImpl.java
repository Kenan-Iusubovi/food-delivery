package com.solvd.fooddelivery.service.impl;

import com.solvd.fooddelivery.config.MyBatisSessionFactory;
import com.solvd.fooddelivery.repository.mybatis.dao.FoodSpotMenuDao;
import com.solvd.fooddelivery.service.FoodSpotMenuService;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FoodSpotMenuServiceImpl implements FoodSpotMenuService {

    private static final Logger log = LoggerFactory.getLogger(FoodSpotMenuServiceImpl.class);

    @Override
    public void addMenuToFoodSpot(Long foodSpotId, Long menuId) {

        validateId(foodSpotId, "FoodSpot");
        validateId(menuId, "Menu");

        try (SqlSession session =
                     MyBatisSessionFactory.getSqlSessionFactory().openSession(false)) {

            FoodSpotMenuDao dao = session.getMapper(FoodSpotMenuDao.class);

            int rows = dao.link(foodSpotId, menuId);
            if (rows != 1) {
                session.rollback();
                throw new IllegalStateException(
                        "Failed to link Menu " + menuId + " to FoodSpot " + foodSpotId);
            }

            session.commit();
            log.info("Linked Menu {} to FoodSpot {}", menuId, foodSpotId);
        }
    }

    @Override
    public void removeMenuFromFoodSpot(Long foodSpotId, Long menuId) {

        validateId(foodSpotId, "FoodSpot");
        validateId(menuId, "Menu");

        try (SqlSession session =
                     MyBatisSessionFactory.getSqlSessionFactory().openSession(false)) {

            FoodSpotMenuDao dao = session.getMapper(FoodSpotMenuDao.class);

            dao.unlink(foodSpotId, menuId);
            session.commit();
            log.info("Unlinked Menu {} from FoodSpot {}", menuId, foodSpotId);
        }
    }

    @Override
    public void removeAllMenusFromFoodSpot(Long foodSpotId) {

        validateId(foodSpotId, "FoodSpot");

        try (SqlSession session =
                     MyBatisSessionFactory.getSqlSessionFactory().openSession(false)) {

            FoodSpotMenuDao dao = session.getMapper(FoodSpotMenuDao.class);

            dao.unlinkByFoodSpotId(foodSpotId);
            session.commit();
            log.info("Removed all menus from FoodSpot {}", foodSpotId);
        }
    }

    @Override
    public void removeMenuFromAllFoodSpots(Long menuId) {

        validateId(menuId, "Menu");

        try (SqlSession session =
                     MyBatisSessionFactory.getSqlSessionFactory().openSession(false)) {

            FoodSpotMenuDao dao = session.getMapper(FoodSpotMenuDao.class);

            dao.unlinkByMenuId(menuId);
            session.commit();
            log.info("Removed Menu {} from all FoodSpots", menuId);
        }
    }

    private void validateId(Long id, String name) {

        if (id == null || id <= 0) {
            throw new IllegalArgumentException(name + " id must be positive");
        }
    }
}
