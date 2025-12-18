package com.solvd.fooddelivery.designpatterns.proxy.context;

public class UserContext {

    private final Long userId;
    private final boolean admin;

    public UserContext(Long userId, boolean admin) {
        this.userId = userId;
        this.admin = admin;
    }

    public Long getUserId() {
        return userId;
    }

    public boolean isAdmin() {
        return admin;
    }
}
