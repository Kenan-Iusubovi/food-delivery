package com.solvd.fooddelivery.service;

public interface FoodSpotMenuService {

    void addMenuToFoodSpot(Long foodSpotId, Long menuId);

    void removeMenuFromFoodSpot(Long foodSpotId, Long menuId);

    void removeAllMenusFromFoodSpot(Long foodSpotId);

    void removeMenuFromAllFoodSpots(Long menuId);
}
