DROP
DATABASE IF EXISTS food_delivery;
CREATE
DATABASE food_delivery CHARACTER SET utf8;
USE
food_delivery;

CREATE TABLE food_spot_owners
(
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    name             VARCHAR(45)        NOT NULL,
    surname          VARCHAR(45)        NOT NULL,
    phone_number     VARCHAR(45) UNIQUE,
    email            VARCHAR(45) UNIQUE NOT NULL,
    business_license VARCHAR(45) UNIQUE NOT NULL
);

CREATE TABLE food_spots
(
    id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
    name               VARCHAR(45) UNIQUE  NOT NULL,
    address            VARCHAR(100) UNIQUE NOT NULL,
    phone_number       VARCHAR(45) UNIQUE,
    opening_time       TIME                NOT NULL,
    closing_time       TIME                NOT NULL,
    food_spot_owner_id BIGINT              NOT NULL,
    CONSTRAINT fk_food_spot_owner
        FOREIGN KEY (food_spot_owner_id)
            REFERENCES food_spot_owners (id)
            ON DELETE CASCADE
);

CREATE TABLE menus
(
    id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(45) NOT NULL
);

CREATE TABLE food_spots_has_menus
(
    food_spot_id BIGINT NOT NULL,
    menu_id      BIGINT NOT NULL,
    PRIMARY KEY (food_spot_id, menu_id),
    CONSTRAINT fk_fsm_food_spot
        FOREIGN KEY (food_spot_id)
            REFERENCES food_spots (id)
            ON DELETE CASCADE,
    CONSTRAINT fk_fsm_menu
        FOREIGN KEY (menu_id)
            REFERENCES menus (id)
            ON DELETE CASCADE
);

CREATE TABLE products
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(45)    NOT NULL,
    price       DECIMAL(10, 2) NOT NULL,
    description VARCHAR(255),
    available   TINYINT        NOT NULL
);

CREATE TABLE menus_has_products
(
    menu_id    BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    PRIMARY KEY (menu_id, product_id),
    CONSTRAINT fk_mhp_menu
        FOREIGN KEY (menu_id)
            REFERENCES menus (id)
            ON DELETE CASCADE,
    CONSTRAINT fk_mhp_product
        FOREIGN KEY (product_id)
            REFERENCES products (id)
            ON DELETE CASCADE
);


CREATE TABLE couriers
(
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    name           VARCHAR(45)        NOT NULL,
    surname        VARCHAR(45)        NOT NULL,
    phone_number   VARCHAR(45) UNIQUE,
    email          VARCHAR(45) UNIQUE NOT NULL,
    license_number VARCHAR(45) UNIQUE NOT NULL,
    years          INT                NOT NULL,
    months         INT                NOT NULL,
    days           INT                NOT NULL
);



CREATE TABLE customers
(
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    name         VARCHAR(45)        NOT NULL,
    surname      VARCHAR(45)        NOT NULL,
    phone_number VARCHAR(45) UNIQUE,
    email        VARCHAR(45) UNIQUE NOT NULL,
    balance      DECIMAL(10, 2)     NOT NULL,
    subscription TINYINT            NOT NULL,
    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE orders
(
    id                    BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_number          VARCHAR(45) UNIQUE NOT NULL,
    total_price           DECIMAL(10, 2)     NOT NULL,
    take_address          VARCHAR(100)       NOT NULL,
    bring_address         VARCHAR(100)       NOT NULL,
    finished              TINYINT            NOT NULL,
    order_date_time       TIMESTAMP          NOT NULL,
    courier_id            BIGINT             NOT NULL,
    customer_id           BIGINT             NOT NULL,
    food_spot_id          BIGINT             NOT NULL,
    delivery_instructions VARCHAR(255),
    CONSTRAINT fk_orders_courier
        FOREIGN KEY (courier_id)
            REFERENCES couriers (id),
    CONSTRAINT fk_orders_customer
        FOREIGN KEY (customer_id)
            REFERENCES customers (id),
    CONSTRAINT fk_orders_food_spot
        FOREIGN KEY (food_spot_id)
            REFERENCES food_spots (id)
);

CREATE TABLE products_has_orders
(
    product_id BIGINT NOT NULL,
    order_id   BIGINT NOT NULL,
    PRIMARY KEY (product_id, order_id),
    CONSTRAINT fk_pho_product
        FOREIGN KEY (product_id)
            REFERENCES products (id)
            ON DELETE CASCADE,
    CONSTRAINT fk_pho_order
        FOREIGN KEY (order_id)
            REFERENCES orders (id)
            ON DELETE CASCADE
);
