package com.solvd.fooddelivery;

import com.solvd.fooddelivery.entity.foodspot.FoodSpot;
import com.solvd.fooddelivery.entity.foodspot.Menu;
import com.solvd.fooddelivery.entity.foodspot.Product;
import com.solvd.fooddelivery.entity.human.Customer;
import com.solvd.fooddelivery.entity.human.FoodSpotOwner;
import com.solvd.fooddelivery.entity.human.courier.Courier;
import com.solvd.fooddelivery.entity.human.courier.WorkingExperience;
import com.solvd.fooddelivery.entity.order.Order;
import com.solvd.fooddelivery.service.*;
import com.solvd.fooddelivery.service.impl.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class Main {

    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {

        FoodSpotOwnerService ownerService = new FoodSpotOwnerServiceImpl();
        FoodSpotService foodSpotService = new FoodSpotServiceImpl();
        MenuService menuService = new MenuServiceImpl();
        ProductService productService = new ProductServiceImpl();
        CourierService courierService = new CourierServiceImpl();
        CustomerService customerService = new CustomerServiceImpl();
        OrderService orderService = new OrderServiceImpl();
        FoodSpotMenuService foodSpotMenuService = new FoodSpotMenuServiceImpl();

        Long ownerId = null;
        Long foodSpot1Id = null;
        Long foodSpot2Id = null;
        Long menu1Id = null;
        Long menu2Id = null;
        Long product1Id = null;
        Long product2Id = null;
        Long courierId = null;
        Long customerId = null;
        Long orderId = null;

        try {

            FoodSpotOwner owner = new FoodSpotOwner();
            owner.setName("Alex");
            owner.setSurname("Johnson");
            owner.setPhoneNumber("+995501111111");
            owner.setEmail("alex.owner.01@mail.com");
            owner.setBusinessLicense("BL-FOOD-2024-001");
            ownerService.create(owner);
            ownerId = owner.getId();

            FoodSpot fs1 = new FoodSpot();
            fs1.setName("Burger Point");
            fs1.setAddress("Tbilisi Rustaveli 10");
            fs1.setPhoneNumber("+995522222222");
            fs1.setOpeningTime(LocalTime.of(9, 0));
            fs1.setClosingTime(LocalTime.of(22, 0));
            fs1.setOwner(owner);
            foodSpotService.create(fs1);
            foodSpot1Id = fs1.getId();

            FoodSpot fs2 = new FoodSpot();
            fs2.setName("Pizza Hub");
            fs2.setAddress("Tbilisi Saburtalo 5");
            fs2.setPhoneNumber("+995533333333");
            fs2.setOpeningTime(LocalTime.of(10, 0));
            fs2.setClosingTime(LocalTime.of(23, 0));
            fs2.setOwner(owner);
            foodSpotService.create(fs2);
            foodSpot2Id = fs2.getId();

            Menu menu1 = new Menu();
            menu1.setName("Lunch Menu");
            menuService.create(menu1);
            menu1Id = menu1.getId();

            Menu menu2 = new Menu();
            menu2.setName("Dinner Menu");
            menuService.create(menu2);
            menu2Id = menu2.getId();

            foodSpotMenuService.addMenuToFoodSpot(foodSpot1Id, menu1Id);
            foodSpotMenuService.addMenuToFoodSpot(foodSpot1Id, menu2Id);
            foodSpotMenuService.addMenuToFoodSpot(foodSpot2Id, menu2Id);

            Product p1 = new Product();
            p1.setName("Cheeseburger");
            p1.setPrice(new BigDecimal("14.50"));
            productService.create(p1);
            product1Id = p1.getId();

            Product p2 = new Product();
            p2.setName("Pepperoni Pizza");
            p2.setPrice(new BigDecimal("22.00"));
            productService.create(p2);
            product2Id = p2.getId();

            Courier courier = new Courier();
            courier.setName("Michael");
            courier.setSurname("Brown");
            courier.setPhoneNumber("+995544444444");
            courier.setEmail("courier.mike@mail.com");
            courier.setLicenseNumber("LIC-998877");
            courier.setWorkingExperience(new WorkingExperience(3, 6, 10));
            courierService.create(courier);
            courierId = courier.getId();

            Customer customer = new Customer();
            customer.setName("Emma");
            customer.setSurname("Wilson");
            customer.setPhoneNumber("+995555555555");
            customer.setEmail("emma.customer@mail.com");
            customer.setBalance(new BigDecimal("120.00"));
            customer.setSubscription(true);
            customerService.create(customer);
            customerId = customer.getId();

            Order order = new Order();
            order.setCourier(courier);
            order.setCustomer(customer);

            FoodSpot orderFoodSpot = new FoodSpot();
            orderFoodSpot.setId(foodSpot1Id);
            order.setFoodSpot(orderFoodSpot);

            order.setProducts(List.of(p1, p2));
            order.setTakeAddress("Burger Point");
            order.setBringAddress("Customer Home");
            order.setOrderDateTime(LocalDateTime.now());
            order.setTotalPrice(p1.getPrice().add(p2.getPrice()));
            orderService.create(order);
            orderId = order.getId();

            order.setFinished(true);
            orderService.update(order);

            ownerService.findAll();
            foodSpotService.findAll();
            menuService.findAll();
            productService.findAll();
            courierService.findAll();
            customerService.findAll();
            orderService.findAll();

        } catch (Exception e) {
            log.error("Execution failed", e);
        } finally {

            try { if (orderId != null) orderService.deleteById(orderId); } catch (Exception ignored) {}
            try { if (product1Id != null) productService.deleteById(product1Id); } catch (Exception ignored) {}
            try { if (product2Id != null) productService.deleteById(product2Id); } catch (Exception ignored) {}
            try { if (foodSpot1Id != null) foodSpotMenuService.removeAllMenusFromFoodSpot(foodSpot1Id); } catch (Exception ignored) {}
            try { if (foodSpot2Id != null) foodSpotMenuService.removeAllMenusFromFoodSpot(foodSpot2Id); } catch (Exception ignored) {}
            try { if (menu1Id != null) menuService.deleteById(menu1Id); } catch (Exception ignored) {}
            try { if (menu2Id != null) menuService.deleteById(menu2Id); } catch (Exception ignored) {}
            try { if (foodSpot1Id != null) foodSpotService.deleteById(foodSpot1Id); } catch (Exception ignored) {}
            try { if (foodSpot2Id != null) foodSpotService.deleteById(foodSpot2Id); } catch (Exception ignored) {}
            try { if (courierId != null) courierService.deleteById(courierId); } catch (Exception ignored) {}
            try { if (customerId != null) customerService.deleteById(customerId); } catch (Exception ignored) {}
            try { if (ownerId != null) ownerService.deleteById(ownerId); } catch (Exception ignored) {}
        }
    }
}
