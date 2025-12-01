package com.solvd.fooddelivery.repository.mappers;

import com.solvd.fooddelivery.entity.foodspot.Menu;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MenuMapper {

    public static Menu mapToMenu(ResultSet resultSet, String prefix) throws SQLException {

        Menu menu = new Menu();
        menu.setId(resultSet.getLong(prefix + "id"));
        menu.setName(resultSet.getString(prefix + "name"));

        return menu;
    }

    public static List<Menu> mapToMenuList(ResultSet resultSet) throws SQLException {

        List<Menu> menus = new ArrayList<>();
        while (resultSet.next()) {
            menus.add(mapToMenu(resultSet, ""));
        }
        return menus;
    }
}
