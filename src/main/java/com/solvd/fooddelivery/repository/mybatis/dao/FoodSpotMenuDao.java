package com.solvd.fooddelivery.repository.mybatis.dao;

import org.apache.ibatis.annotations.Param;

public interface FoodSpotMenuDao {

    int link(@Param("foodSpotId") Long foodSpotId,
             @Param("menuId") Long menuId);

    int unlink(@Param("foodSpotId") Long foodSpotId,
               @Param("menuId") Long menuId);

    int unlinkByFoodSpotId(@Param("foodSpotId") Long foodSpotId);

    int unlinkByMenuId(@Param("menuId") Long menuId);
}
