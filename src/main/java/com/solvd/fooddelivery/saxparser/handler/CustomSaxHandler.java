package com.solvd.fooddelivery.saxparser.handler;

import com.solvd.fooddelivery.entity.FoodDelivery;
import com.solvd.fooddelivery.entity.ProductContainer;
import com.solvd.fooddelivery.entity.foodspot.FoodSpot;
import com.solvd.fooddelivery.entity.foodspot.Menu;
import com.solvd.fooddelivery.entity.foodspot.Product;
import com.solvd.fooddelivery.entity.human.Customer;
import com.solvd.fooddelivery.entity.human.FoodSpotOwner;
import com.solvd.fooddelivery.entity.human.courier.Courier;
import com.solvd.fooddelivery.entity.human.courier.WorkingExperience;
import com.solvd.fooddelivery.entity.order.Order;
import org.apache.commons.lang3.StringUtils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;
import java.util.UUID;
import java.util.function.BiConsumer;

public class CustomSaxHandler extends DefaultHandler {

    private FoodDelivery foodDelivery;
    private Deque<Object> stack = new ArrayDeque<>();
    private StringBuilder data = new StringBuilder();


    @Override
    public void startElement(String uri, String localName, String qName,
                             Attributes attributes) throws SAXException {

        Object currentObject = null;
        Long currentId = getId(attributes);

        switch (qName) {
            case "foodDelivery" -> currentObject = foodDelivery = new FoodDelivery();
            case "foodSpotOwner" -> currentObject = new FoodSpotOwner();
            case "foodSpot" -> currentObject = new FoodSpot();
            case "menu" -> currentObject = new Menu();
            case "product" -> currentObject = new Product();
            case "order" -> currentObject = new Order();
            case "courier" -> currentObject = new Courier();
            case "customer" -> currentObject = new Customer();
            case "workingExperience" -> currentObject = new WorkingExperience();
        }
        if (currentObject != null) {
            if (currentId != null) {
                assignId(currentObject, currentId);
            }
            stack.push(currentObject);
        }
        data.setLength(0);
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {

        data.append(ch, start, length);
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {

        String value = data.toString().trim();
        Object currentObject = stack.peek();

        boolean matched = switch (qName) {
            case "foodSpotOwner" -> {
                attachToParent(
                        FoodDelivery.class, FoodSpotOwner.class,
                        (FoodDelivery foodDelivery, FoodSpotOwner owner) ->
                                foodDelivery.getFoodSpotOwners().add(owner));
                yield true;
            }
            case "foodSpot" -> {
                attachToParent(
                        FoodSpotOwner.class, FoodSpot.class,
                        (FoodSpotOwner foodSpotOwner, FoodSpot foodSpot) ->
                                foodSpotOwner.getFoodSpots().add(foodSpot));
                yield true;
            }
            case "menu" -> {
                attachToParent(
                        FoodSpot.class, Menu.class,
                        (FoodSpot foodSpot, Menu menu) ->
                                foodSpot.getMenus().add(menu));
                yield true;
            }
            case "product" -> {
                attachToParent(
                        ProductContainer.class, Product.class,
                        (ProductContainer container, Product product) ->
                                container.getProducts().add(product)
                );
                yield true;
            }
            case "order" -> {
                attachToParent(
                        FoodSpot.class, Order.class,
                        (FoodSpot foodSpot, Order order) ->
                                foodSpot.getOrders().add(order));
                yield true;
            }
            case "courier" -> {
                attachToParent(
                        Order.class, Courier.class,
                        Order::setCourier);
                yield true;
            }
            case "customer" -> {
                attachToParent(Order.class, Customer.class,
                        Order::setCustomer);
                yield true;
            }
            case "workingExperience" -> {
                attachToParent(Courier.class, WorkingExperience.class,
                        Courier::setWorkingExperience);
                yield true;
            }
            default -> false;
        };

        if (!matched) {

            setField(currentObject, value, qName);
        }
    }

    private void setField(Object object, String value, String qName) {

        String methodName = "set" + StringUtils.capitalize(qName);
        try {
            var methods = object.getClass().getMethods();
            for (var method : methods) {
                if (method.getName().equals(methodName) && method.getParameterCount() == 1) {
                    Class<?> paramType = method.getParameterTypes()[0];
                    if (Collection.class.isAssignableFrom(paramType)){
                        return;
                    }

                    Object convertedValue = convertValue(value, paramType);
                    method.invoke(object, convertedValue);
                    break;
                }
            }
        } catch (Exception e) {
            System.err.println("Failed to set field " + value + " on " +
                    object.getClass().getSimpleName() + ": " + e.getMessage());
        }
    }

    private Object convertValue(String value, Class<?> targetType) {
        if (targetType == String.class) return value;
        if (targetType == Long.class || targetType == long.class) return Long.valueOf(value);
        if (targetType == Integer.class || targetType == int.class) return Integer.valueOf(value);
        if (targetType == Double.class || targetType == double.class) return Double.valueOf(value);
        if (targetType == Boolean.class || targetType == boolean.class) return Boolean.parseBoolean(value);
        if (targetType == BigDecimal.class) return new BigDecimal(value);
        if (targetType == LocalTime.class) return LocalTime.parse(value);
        if (targetType == LocalDateTime.class) return LocalDateTime.parse(value);
        if (targetType == UUID.class) return UUID.fromString(value);

        try {
            return targetType.getConstructor(String.class).newInstance(value);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        return null;
    }

    private <Parent, Child> void attachToParent(Class<Parent> parentType, Class<Child> childType,
                                                BiConsumer<Parent, Child> attachAction) {
        Child child = childType.cast(stack.peek());
        stack.pop();
        Parent parent = parentType.cast(stack.peek());
        attachAction.accept(parent, child);
    }

    private void assignId(Object object, Long id) {
        try {
            object.getClass().getMethod("setId", Long.class)
                    .invoke(object, id);
        } catch (Exception e) {

        }
    }

    private Long getId(Attributes attributes) {
        String value = attributes.getValue("id");
        return !StringUtils.isBlank(value) ? Long.valueOf(value) : null;
    }

    public FoodDelivery getResult() {
        return foodDelivery;
    }
}