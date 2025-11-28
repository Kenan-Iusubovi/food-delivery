package com.solvd.fooddelivery.repository;

import com.solvd.fooddelivery.entity.foodspot.Menu;

import java.util.List;

public interface MenuRepository {

    List<Menu> findMenusByFoodSpotId(Long foodSpotId);

    boolean addProductToMenu(Long menuId, Long productId);

    boolean removeProductFromMenu(Long menuId, Long productId);

}
