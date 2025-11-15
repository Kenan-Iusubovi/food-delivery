package com.solvd.fooddelivery.parsers.jaxbframework.adapter;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class LocalTimeAdapter extends XmlAdapter<String, LocalTime> {

    private final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_TIME;

    @Override
    public LocalTime unmarshal(String s) throws Exception {
        return LocalTime.parse(s, formatter);
    }

    @Override
    public String marshal(LocalTime localTime) throws Exception {
        return localTime.format(formatter);
    }
}
