package com.solvd.fooddelivery.handler;

import com.solvd.fooddelivery.entity.FoodDelivery;
import com.solvd.fooddelivery.entity.foodspot.FoodSpot;
import com.solvd.fooddelivery.entity.foodspot.Menu;
import com.solvd.fooddelivery.entity.foodspot.Product;
import com.solvd.fooddelivery.entity.human.*;
import com.solvd.fooddelivery.entity.human.courier.Courier;
import com.solvd.fooddelivery.entity.human.courier.WorkingExperience;
import com.solvd.fooddelivery.entity.order.Order;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.function.BiConsumer;

public class CustomSaxHandler extends DefaultHandler {

    private FoodDelivery foodDelivery;
    private Deque<Object> stack = new ArrayDeque<>();
    private StringBuilder data = new StringBuilder();


    @Override
    public void startElement(String uri, String localName, String qName,
                             Attributes attributes) throws SAXException {
        switch (qName) {
            case "foodDelivery" ->{
                foodDelivery = new FoodDelivery();
                stack.push(foodDelivery);
            }
            case "foodSpotOwner" -> {
                FoodSpotOwner owner = new FoodSpotOwner();
                Long id = Long.valueOf(attributes.getValue("id"));
                if (id != null) {
                    owner.setId(id);
                }
                stack.push(owner);
            }
            case "foodSpot" -> {
                FoodSpot foodSpot = new FoodSpot();
                Long id = Long.valueOf(attributes.getValue("id"));
                if (id != null) {
                    foodSpot.setId(id);
                }
                stack.push(foodSpot);
            }
            case "menu" -> {
                Menu menu = new Menu();
                Long id = Long.valueOf(attributes.getValue("id"));
                if (id != null) {
                    menu.setId(id);
                }
                stack.push(menu);
            }
            case "product" -> {
                Product product = new Product();
                Long id = Long.valueOf(attributes.getValue("id"));
                if (id != null) {
                    product.setId(id);
                }
                stack.push(product);
            }
            case "order" -> {
                Order order = new Order();
                Long id = Long.valueOf(attributes.getValue("id"));
                if (id != null) {
                    order.setId(id);
                }
                stack.push(order);
            }
            case "courier" -> {
                Courier courier = new Courier();
                Long id = Long.valueOf(attributes.getValue("id"));
                if (id != null) {
                    courier.setId(id);
                }
                stack.push(courier);
            }
            case "customer" -> {
                Customer customer = new Customer();
                Long id = Long.valueOf(attributes.getValue("id"));
                if (id != null) {
                    customer.setId(id);
                }
                stack.push(customer);
            }
            case "workingExperience" -> stack.push(new WorkingExperience());
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
        Object current = stack.peek();

        switch (qName) {
            case "foodSpotOwner" -> attachToParent(
                    FoodDelivery.class, FoodSpotOwner.class,
                    (FoodDelivery foodDelivery, FoodSpotOwner foodSpotOwner) ->
                            foodDelivery.getFoodSpotOwners().add(foodSpotOwner)

            );
            case "name" -> {
                if (current instanceof Human human){
                    human.setName(value);
                }
                if (current instanceof FoodSpot foodSpot) {
                    foodSpot.setName(value);
                }
                if (current instanceof Menu menu) {
                    menu.setName(value);
                }
                if (current instanceof Product product) {
                    product.setName(value);
                }
            }
            case "surname" -> {
                if (current instanceof Human human) {
                    human.setSurname(value);
                }
            }
            case "phoneNumber" -> {
                if (current instanceof Human human) {
                    human.setPhoneNumber(value);
                }
                if (current instanceof FoodSpot foodSpot) {
                    foodSpot.setPhoneNumber(value);
                }
            }
            case "email" -> {
                if (current instanceof Human human) {
                    human.setEmail(value);
                }
            }
            case "businesLicense" -> {
                if (current instanceof FoodSpotOwner owner) {
                    owner.setBusinessLicense(value);
                }
            }
            case "foodSpot" -> attachToParent(
                    FoodSpotOwner.class, FoodSpot.class,
                    (FoodSpotOwner foodSpotOwner, FoodSpot foodSpot) ->
                            foodSpotOwner.getFoodSpots().add(foodSpot)
            );

            case "address" -> {
                if (current instanceof FoodSpot foodSpot) {
                    foodSpot.setAddress(value);
                }
            }
            case "product" -> {
                if (current instanceof Menu) {
                    attachToParent(
                            Menu.class, Product.class,
                            (Menu menu, Product product) ->
                                    menu.getProducts().add(product)
                    );
                }
                if (current instanceof Order) {
                    attachToParent(
                            Order.class, Product.class,
                            (Order order, Product product) ->
                                    order.getProducts().add(product)
                    );
                }
            }
            case "price" -> {
                if (current instanceof Product product) {
                    product.setPrice(BigDecimal.valueOf(Double.parseDouble(value)));
                }
            }
            case "description" -> {
                if (current instanceof Product product) {
                    product.setDescription(value);
                }
            }
            case "available" -> {
                if (current instanceof Product product) {
                    product.setAvailable(Boolean.parseBoolean(value));
                }
            }
            case "openingTime" -> {
                if (current instanceof FoodSpot foodSpot) {
                    foodSpot.setOpeningTime(LocalTime.parse(value));
                }
            }
            case "closingTime" -> {
                if (current instanceof FoodSpot foodSpot) {
                    foodSpot.setClosingTime(LocalTime.parse(value));
                }
            }
            case "order" -> attachToParent(
                    FoodSpot.class, Order.class,
                    (FoodSpot foodSpot, Order order) ->
                        foodSpot.getOrders().add(order)
                    );
            case "orderNumber" -> {
                if (current instanceof Order order) {
                    order.setOrderNumber(UUID.fromString(value));
                }
            }
            case "courier" -> attachToParent(
                    Order.class, Courier.class,
                    (Order order, Courier courier) ->
                        order.setCourier(courier)
            );
            case "customer" -> attachToParent(
                    Order.class, Customer.class,
                    Order::setCustomer
            );
            case "licenseNumber" -> {
                if (current instanceof Courier courier) {
                    courier.setLicenseNumber(value);
                }
            }
            case "workingExperience" -> attachToParent(
                    Courier.class, WorkingExperience.class,
                    (Courier::setWorkingExperience)
            );
            case "years" -> {
                if (current instanceof WorkingExperience workingExperience) {
                    workingExperience.setYears(Integer.parseInt(value));
                }
            }
            case "months" -> {
                if (current instanceof WorkingExperience workingExperience) {
                    workingExperience.setMonths(Integer.parseInt(value));
                }
            }
            case "days" -> {
                if (current instanceof WorkingExperience workingExperience) {
                    workingExperience.setDays(Integer.parseInt(value));
                }
            }
            case "balance" -> {
                if (current instanceof Customer customer) {
                    customer.setBalance(BigDecimal.valueOf(Double.parseDouble(value)));
                }
            }
            case "subscription" -> {
                if (current instanceof Customer customer) {
                    customer.setSubscription(Boolean.parseBoolean(value));
                }
            }


            case "totalPrice" -> {
                if (current instanceof Order order) {
                    order.setTotalPrice(BigDecimal.valueOf(Double.parseDouble(value)));
                }
            }
            case "takeAddress" -> {
                if (current instanceof Order order){
                    order.setTakeAddress(value);
                }
            }
            case "bringAddress" -> {
                if (current instanceof Order order){
                    order.setBringAddress(value);
                }
            }
            case "finished" -> {
                if (current instanceof Order order){
                    order.setFinished(Boolean.parseBoolean(value));
                }
            }
            case "orderDateTime" -> {
                if (current instanceof Order order){
                    order.setOrderDateTime(LocalDateTime.parse(value));
                }
            }
        }
    }

    private <Parent, Child> void attachToParent(Class<Parent> parentType, Class<Child> childType,
                                                BiConsumer<Parent, Child> attachAction) {
        Child child = childType.cast(stack.pop());
        Parent parent = parentType.cast(stack.peek());
        attachAction.accept(parent, child);
    }

    public FoodDelivery getResult() {
        return foodDelivery;
    }
}
