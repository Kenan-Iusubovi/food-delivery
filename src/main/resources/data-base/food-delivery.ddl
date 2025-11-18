-- MySQL Script FIXED (unique foreign key names)

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

CREATE SCHEMA IF NOT EXISTS `food_delivery` DEFAULT CHARACTER SET utf8 ;
USE `food_delivery` ;

-- menus
CREATE TABLE IF NOT EXISTS `menus` (
                                       `id` BIGINT NOT NULL AUTO_INCREMENT,
                                       `name` VARCHAR(45) NOT NULL,
                                       PRIMARY KEY (`id`),
                                       UNIQUE INDEX `id_UNIQUE` (`id`)
) ENGINE=InnoDB;

-- products
CREATE TABLE IF NOT EXISTS `products` (
                                          `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
                                          `name` VARCHAR(45) NOT NULL,
                                          `price` DECIMAL NOT NULL,
                                          `description` VARCHAR(45) NOT NULL,
                                          `available` TINYINT NOT NULL,
                                          PRIMARY KEY (`id`)
) ENGINE=InnoDB;

-- couriers
CREATE TABLE IF NOT EXISTS `couriers` (
                                          `id` BIGINT NOT NULL AUTO_INCREMENT,
                                          `name` VARCHAR(45) NOT NULL,
                                          `surname` VARCHAR(45) NOT NULL,
                                          `phone_number` VARCHAR(45) NULL,
                                          `email` VARCHAR(45) NOT NULL,
                                          `license_number` VARCHAR(45) NOT NULL,
                                          `years` INT NULL,
                                          `months` INT NULL,
                                          `days` INT NULL,
                                          PRIMARY KEY (`id`),
                                          UNIQUE INDEX `id_UNIQUE` (`id`),
                                          UNIQUE INDEX `phone_number_UNIQUE` (`phone_number`),
                                          UNIQUE INDEX `email_UNIQUE` (`email`),
                                          UNIQUE INDEX `license_number_UNIQUE` (`license_number`)
) ENGINE=InnoDB;

-- customers
CREATE TABLE IF NOT EXISTS `customers` (
                                           `id` BIGINT NOT NULL AUTO_INCREMENT,
                                           `name` VARCHAR(45) NOT NULL,
                                           `surname` VARCHAR(45) NOT NULL,
                                           `phone_number` VARCHAR(45) NULL,
                                           `email` VARCHAR(45) NOT NULL,
                                           `balance` DECIMAL NOT NULL,
                                           `subscription` TINYINT NULL,
                                           PRIMARY KEY (`id`),
                                           UNIQUE INDEX `id_UNIQUE` (`id`),
                                           UNIQUE INDEX `phone_number_UNIQUE` (`phone_number`),
                                           UNIQUE INDEX `email_UNIQUE` (`email`)
) ENGINE=InnoDB;

-- orders
CREATE TABLE IF NOT EXISTS `orders` (
                                        `id` BIGINT NOT NULL AUTO_INCREMENT,
                                        `order_number` VARCHAR(45) NOT NULL,
                                        `total_price` DECIMAL NOT NULL,
                                        `take_address` VARCHAR(45) NOT NULL,
                                        `bring_address` VARCHAR(45) NOT NULL,
                                        `finished` TINYINT NULL,
                                        `order_date_time` TIMESTAMP NOT NULL,
                                        `product_id` BIGINT UNSIGNED NOT NULL,
                                        `courier_id` BIGINT NOT NULL,
                                        `customer_id` BIGINT NOT NULL,
                                        PRIMARY KEY (`id`, `courier_id`, `customer_id`),
                                        UNIQUE INDEX `id_UNIQUE` (`id`),
                                        UNIQUE INDEX `order_number_UNIQUE` (`order_number`),
                                        INDEX `idx_orders_product` (`product_id`),
                                        INDEX `idx_orders_courier` (`courier_id`),
                                        INDEX `idx_orders_customer` (`customer_id`),
                                        CONSTRAINT `fk_orders_product`
                                            FOREIGN KEY (`product_id`) REFERENCES `products` (`id`)
                                                ON DELETE NO ACTION ON UPDATE NO ACTION,
                                        CONSTRAINT `fk_orders_courier`
                                            FOREIGN KEY (`courier_id`) REFERENCES `couriers` (`id`)
                                                ON DELETE NO ACTION ON UPDATE NO ACTION,
                                        CONSTRAINT `fk_orders_customer`
                                            FOREIGN KEY (`customer_id`) REFERENCES `customers` (`id`)
                                                ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB;

-- food_spots
CREATE TABLE IF NOT EXISTS `food_spots` (
                                            `id` BIGINT NOT NULL,
                                            `name` VARCHAR(45) NOT NULL,
                                            `address` VARCHAR(45) NOT NULL,
                                            `phone_number` VARCHAR(45) NULL,
                                            `opening_time` TIME NOT NULL,
                                            `closing_time` TIME NOT NULL,
                                            `menu_id` BIGINT NOT NULL,
                                            `order_id` BIGINT NOT NULL,
                                            PRIMARY KEY (`id`, `menu_id`, `order_id`),
                                            UNIQUE INDEX `id_UNIQUE` (`id`),
                                            UNIQUE INDEX `address_UNIQUE` (`address`),
                                            UNIQUE INDEX `name_UNIQUE` (`name`),
                                            UNIQUE INDEX `phone_number_UNIQUE` (`phone_number`),
                                            INDEX `idx_food_spots_menu` (`menu_id`),
                                            INDEX `idx_food_spots_order` (`order_id`),
                                            CONSTRAINT `fk_food_spots_menu`
                                                FOREIGN KEY (`menu_id`) REFERENCES `menus` (`id`)
                                                    ON DELETE NO ACTION ON UPDATE NO ACTION,
                                            CONSTRAINT `fk_food_spots_order`
                                                FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`)
                                                    ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB;

-- food_spot_owners
CREATE TABLE IF NOT EXISTS `food_spot_owners` (
                                                  `id` BIGINT NOT NULL AUTO_INCREMENT,
                                                  `name` VARCHAR(45) NOT NULL,
                                                  `surname` VARCHAR(45) NOT NULL,
                                                  `phone_number` VARCHAR(45) NULL,
                                                  `email` VARCHAR(45) NOT NULL,
                                                  `business_license` VARCHAR(45) NOT NULL,
                                                  `food_spot_id` BIGINT NOT NULL,
                                                  PRIMARY KEY (`id`, `food_spot_id`),
                                                  UNIQUE INDEX `email_UNIQUE` (`email`),
                                                  UNIQUE INDEX `phone_number_UNIQUE` (`phone_number`),
                                                  UNIQUE INDEX `business_license_UNIQUE` (`business_license`),
                                                  UNIQUE INDEX `id_UNIQUE` (`id`),
                                                  INDEX `idx_owner_food_spot` (`food_spot_id`),
                                                  CONSTRAINT `fk_food_spot_owners_food_spot`
                                                      FOREIGN KEY (`food_spot_id`) REFERENCES `food_spots` (`id`)
                                                          ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB;

-- menus_has_products
CREATE TABLE IF NOT EXISTS `menus_has_products` (
                                                    `menu_id` BIGINT NOT NULL,
                                                    `product_id` BIGINT UNSIGNED NOT NULL,
                                                    PRIMARY KEY (`menu_id`, `product_id`),
                                                    INDEX `idx_mhp_product` (`product_id`),
                                                    INDEX `idx_mhp_menu` (`menu_id`),
                                                    CONSTRAINT `fk_mhp_menu`
                                                        FOREIGN KEY (`menu_id`) REFERENCES `menus` (`id`)
                                                            ON DELETE NO ACTION ON UPDATE NO ACTION,
                                                    CONSTRAINT `fk_mhp_product`
                                                        FOREIGN KEY (`product_id`) REFERENCES `products` (`id`)
                                                            ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB;

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
