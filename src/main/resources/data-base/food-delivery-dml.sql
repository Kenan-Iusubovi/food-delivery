USE
food_delivery;

-- ============================
-- INSERTS
-- ============================

INSERT INTO food_spot_owners (name, surname, phone_number, email, business_license)
VALUES ('Michael', 'Owner', '555-000', 'owner@example.com', 'OWN-001');

INSERT INTO food_spots (name, address, phone, opening_time, closing_time, food_spot_owner_id)
VALUES ('Burger House', '12 Main St', '555-444', '08:00:00', '22:00:00', 1);

INSERT INTO menus (name, food_spot_id)
VALUES ('Breakfast Menu', 1);
INSERT INTO menus (name, food_spot_id)
VALUES ('Lunch Menu', 1);

INSERT INTO products (name, price, description, available)
VALUES ('Cheeseburger', 8.99, 'Classic cheeseburger', 1);

INSERT INTO products (name, price, description, available)
VALUES ('Veggie Pizza', 12.50, 'Fresh vegetable pizza', 1);

INSERT INTO products (name, price, description, available)
VALUES ('Chicken Wrap', 7.49, 'Grilled chicken wrap', 1);

INSERT INTO products (name, price, description, available)
VALUES ('Latte', 3.50, 'Hot latte coffee', 1);

INSERT INTO customers (name, surname, phone_number, email, balance, subscription)
VALUES ('John', 'Smith', '555-111', 'john@example.com', 45.20, 1);

INSERT INTO customers (name, surname, phone_number, email, balance, subscription)
VALUES ('Emily', 'Stone', '555-222', 'emily@example.com', 12.00, 0);

INSERT INTO couriers (name, surname, phone_number, email, license_number, years, days)
VALUES ('Adam', 'Cole', '555-333', 'adam@example.com', 'LIC1023', 1, 15);

INSERT INTO orders (order_number, total_price, take_address, bring_address, finished,
                    order_date_time, courier_id, customer_id, food_spot_id)
VALUES ('ORD-001', 21.49, '12 Main St', '45 River Rd', 0,
        '2025-11-28 12:00:00', 1, 1, 1);

INSERT INTO menus_has_products
VALUES (1, 1),
       (1, 4),
       (2, 2),
       (2, 3);
INSERT INTO products_has_orders
VALUES (1, 1),
       (2, 1);

-- ============================
-- UPDATES
-- ============================

UPDATE customers
SET balance = balance + 20
WHERE email = 'john@example.com';
UPDATE products
SET price = 9.50
WHERE name = 'Cheeseburger';
UPDATE couriers
SET phone_number = '555-777'
WHERE id = 1;
UPDATE menus
SET name = 'Brunch Menu'
WHERE id = 1;
UPDATE food_spots
SET closing_time = '23:00:00'
WHERE id = 1;
UPDATE orders
SET finished = 1
WHERE id = 1;
UPDATE products
SET available = 0
WHERE name = 'Latte';
UPDATE customers
SET subscription = 1
WHERE id = 2;
UPDATE couriers
SET years = years + 1
WHERE id = 1;
UPDATE food_spot_owners
SET business_license = 'NEWLIC543'
WHERE id = 1;

-- ============================
-- SAFE DELETE ORDER (NO FK ERRORS)
-- ============================

DELETE
FROM products_has_orders
WHERE order_id = 1;
DELETE
FROM menus_has_products
WHERE menu_id = 1;

DELETE
FROM orders
WHERE id = 1;

DELETE
FROM menus
WHERE id = 2;
DELETE
FROM menus
WHERE id = 1;

DELETE
FROM food_spots
WHERE id = 1;

DELETE
FROM food_spot_owners
WHERE id = 1;

DELETE
FROM products
WHERE id = 3;
DELETE
FROM products
WHERE name = 'Latte';

DELETE
FROM customers
WHERE email = 'emily@example.com';
DELETE
FROM customers
WHERE id = 1;

DELETE
FROM couriers
WHERE license_number = 'LIC1023';

-- ============================
-- SELECTS
-- ============================

SELECT o.id     AS order_id,
       o.order_number,
       c.name   AS customer_name,
       cu.name  AS courier_name,
       p.name   AS product_name,
       fs.name  AS food_spot_name,
       fso.name AS owner_name,
       m.name   AS menu_name
FROM orders o
         JOIN customers c ON o.customer_id = c.id
         JOIN couriers cu ON o.courier_id = cu.id
         JOIN products_has_orders pho ON pho.order_id = o.id
         JOIN products p ON p.id = pho.product_id
         JOIN food_spots fs ON o.food_spot_id = fs.id
         JOIN food_spot_owners fso ON fs.food_spot_owner_id = fso.id
         JOIN menus m ON m.food_spot_id = fs.id
         JOIN menus_has_products mp ON mp.menu_id = m.id AND mp.product_id = p.id;

SELECT c.name, o.order_number
FROM customers c
         JOIN orders o ON c.id = o.customer_id;

SELECT m.name AS menu, p.name AS product
FROM menus m
         LEFT JOIN menus_has_products mp ON m.id = mp.menu_id
         LEFT JOIN products p ON mp.product_id = p.id;

SELECT p.name, m.name AS menu
FROM menus m
         RIGHT JOIN menus_has_products mp ON m.id = mp.menu_id
         RIGHT JOIN products p ON mp.product_id = p.id;

SELECT *
FROM menus m
         LEFT JOIN menus_has_products mp ON m.id = mp.menu_id
UNION
SELECT *
FROM menus m
         RIGHT JOIN menus_has_products mp ON m.id = mp.menu_id;

SELECT c.name, p.name
FROM customers c
         CROSS JOIN products p;

SELECT customer_id, COUNT(*) AS orders_count
FROM orders
GROUP BY customer_id;

SELECT courier_id, SUM(total_price) AS total_revenue
FROM orders
GROUP BY courier_id;

SELECT pho.product_id, AVG(o.total_price) AS avg_total_price
FROM orders o
         JOIN products_has_orders pho ON pho.order_id = o.id
GROUP BY pho.product_id;

SELECT subscription, COUNT(*)
FROM customers
GROUP BY subscription;
SELECT available, COUNT(*)
FROM products
GROUP BY available;

SELECT YEAR (order_date_time), COUNT (*)
FROM orders
GROUP BY YEAR (order_date_time);

SELECT food_spot_id, COUNT(*)
FROM menus
GROUP BY food_spot_id;

SELECT customer_id, COUNT(*)
FROM orders
GROUP BY customer_id
HAVING COUNT(*) > 2;
SELECT courier_id, SUM(total_price)
FROM orders
GROUP BY courier_id
HAVING SUM(total_price) > 100;

SELECT pho.product_id, AVG(o.total_price) AS avg_total_price
FROM orders o
         JOIN products_has_orders pho ON pho.order_id = o.id
GROUP BY pho.product_id
HAVING AVG(o.total_price) > 20;

SELECT subscription, COUNT(*)
FROM customers
GROUP BY subscription
HAVING COUNT(*) > 1;

SELECT available, COUNT(*)
FROM products
GROUP BY available
HAVING COUNT(*) > 2;

SELECT food_spot_id, COUNT(*)
FROM menus
GROUP BY food_spot_id
HAVING COUNT(*) > 0;

SELECT YEAR (order_date_time), COUNT (*)
FROM orders
GROUP BY YEAR (order_date_time)
HAVING COUNT (*) >= 1;
