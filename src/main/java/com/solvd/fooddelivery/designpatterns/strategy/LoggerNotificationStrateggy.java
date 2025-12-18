package com.solvd.fooddelivery.designpatterns.strategy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerNotificationStrateggy implements NotificationStrategy{

    private static final Logger log = LoggerFactory.getLogger(LoggerNotificationStrateggy.class);


    @Override
    public void notify(String message) {

        log.info(message);
    }
}
