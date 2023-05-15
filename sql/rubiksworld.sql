DROP DATABASE IF EXISTS rubiksworld;

CREATE DATABASE IF NOT EXISTS rubiksworld;

USE rubiksworld;

CREATE TABLE `Categories`
(
    `name` VARCHAR(30) PRIMARY KEY
);

CREATE TABLE `Models`
(
    `name`                VARCHAR(100)  NOT NULL,
    `maker`               VARCHAR(100)  NOT NULL,
    `category_name`       VARCHAR(30)   NOT NULL,
    `price`               DECIMAL(4, 2) NOT NULL,
    `discount_percentage` DECIMAL(4, 2),
    `image_url`           VARCHAR(512)  NOT NULL,
    `is_speed_cube`       BOOLEAN       NOT NULL,
    `is_stickerless`      BOOLEAN       NOT NULL,
    `is_magnetic`         BOOLEAN       NOT NULL,
    PRIMARY KEY (`name`, `maker`)
);

CREATE TABLE `CustomizableParts`
(
    `model_name`  VARCHAR(100) NOT NULL,
    `model_maker` VARCHAR(100) NOT NULL,
    `part`        VARCHAR(100) NOT NULL,
    PRIMARY KEY (`model_name`, `model_maker`, `part`)
);

CREATE TABLE `Customizations`
(
    `model_name`  VARCHAR(100),
    `model_maker` VARCHAR(100),
    `part`        VARCHAR(100),
    `change`      VARCHAR(100),
    `price`       DECIMAL(4, 2) NOT NULL,
    `is_default`  BOOLEAN       NOT NULL,
    PRIMARY KEY (`model_name`, `model_maker`, `part`, `change`)
);

CREATE TABLE `Applications`
(
    `model_name`           VARCHAR(100),
    `model_maker`          VARCHAR(100),
    `customization_part`   VARCHAR(100),
    `customization_change` VARCHAR(100),
    `model_version_id`     INTEGER,
    PRIMARY KEY (`model_name`, `model_maker`, `customization_part`, `customization_change`, `model_version_id`)
);

CREATE TABLE `ModelVersions`
(
    `id`          INTEGER PRIMARY KEY AUTO_INCREMENT,
    `model_name`  VARCHAR(100) NOT NULL,
    `model_maker` VARCHAR(100) NOT NULL
);

CREATE TABLE `Users`
(
    `nickname`         VARCHAR(255) PRIMARY KEY,
    `name`             VARCHAR(255) NOT NULL,
    `surname`          VARCHAR(255) NOT NULL,
    `city`             VARCHAR(255),
    `zip`              VARCHAR(10),
    `email`            VARCHAR(512),
    `address`          VARCHAR(255),
    `phone_number`     VARCHAR(20),
    `shipping_price`   DECIMAL(4, 2) NOT NULL,
    `minimum_subtotal` DECIMAL(4, 2) NOT NULL
);

CREATE TABLE `CartPresences`
(
    `user_nickname`    VARCHAR(255),
    `model_version_id` INTEGER,
    PRIMARY KEY (`user_nickname`, `model_version_id`)
);

CREATE TABLE `WishlistPresences`
(
    `user_nickname` VARCHAR(255),
    `model_name`    VARCHAR(100),
    `model_maker`   VARCHAR(100),
    PRIMARY KEY (`user_nickname`, `model_name`, `model_maker`)
);

CREATE TABLE `Solves`
(
    `user_nickname`     VARCHAR(255),
    `solve_time`        INTEGER,
    `registration_date` DATE,
    `model_name`        VARCHAR(100),
    `model_maker`       VARCHAR(100),
    PRIMARY KEY (`user_nickname`, `solve_time`, `registration_date`)
);

CREATE TABLE `Friendships`
(
    `sender_nickname`   VARCHAR(255),
    `receiver_nickname` VARCHAR(255),
    PRIMARY KEY (`sender_nickname`, `receiver_nickname`)
);

CREATE TABLE `Orders`
(
    `id`            INTEGER AUTO_INCREMENT,
    `order_date`    DATE,
    `order_time`    TIME    NOT NULL,
    `shipping_date` DATE    NOT NULL,
    `total`         INTEGER NOT NULL,
    `user_nickname` VARCHAR(255) NOT NULL,
    PRIMARY KEY (`id`, `order_date`)
);

CREATE TABLE `Discounts`
(
    `order_id`    INTEGER,
    `order_date`  DATE,
    `coupon_code` VARCHAR(20) NOT NULL,
    PRIMARY KEY (`order_id`, `order_date`)
);

CREATE TABLE `Coupons`
(
    `code`  VARCHAR(20) PRIMARY KEY,
    `value` INTEGER NOT NULL,
    `type`  INTEGER NOT NULL
);

ALTER TABLE `Models`
    ADD FOREIGN KEY (`category_name`) REFERENCES `Categories` (`name`);

ALTER TABLE `CustomizableParts`
    ADD FOREIGN KEY (`model_name`, `model_maker`) REFERENCES `Models` (`name`, `maker`);

ALTER TABLE `Customizations`
    ADD FOREIGN KEY (`model_name`, `model_maker`, `part`) REFERENCES `CustomizableParts` (`model_name`, `model_maker`, `part`);

ALTER TABLE `Applications`
    ADD FOREIGN KEY (`model_name`, `model_maker`, `customization_part`, `customization_change`) REFERENCES `Customizations` (`model_name`, `model_maker`, `part`, `change`);

ALTER TABLE `Applications`
    ADD FOREIGN KEY (`model_version_id`) REFERENCES `ModelVersions` (`id`);

ALTER TABLE `ModelVersions`
    ADD FOREIGN KEY (`model_name`, `model_maker`) REFERENCES `Applications` (`model_name`, `model_maker`);

ALTER TABLE `CartPresences`
    ADD FOREIGN KEY (`user_nickname`) REFERENCES `Users` (`nickname`);

ALTER TABLE `CartPresences`
    ADD FOREIGN KEY (`model_version_id`) REFERENCES `ModelVersions` (`id`);

ALTER TABLE `WishlistPresences`
    ADD FOREIGN KEY (`user_nickname`) REFERENCES `Users` (`nickname`);

ALTER TABLE `WishlistPresences`
    ADD FOREIGN KEY (`model_name`, `model_maker`) REFERENCES `Models` (`name`, `maker`);

ALTER TABLE `Solves`
    ADD FOREIGN KEY (`user_nickname`) REFERENCES `Users` (`nickname`);

ALTER TABLE `Solves`
    ADD FOREIGN KEY (`model_name`, `model_maker`) REFERENCES `Models` (`name`, `maker`);

ALTER TABLE `Friendships`
    ADD FOREIGN KEY (`sender_nickname`) REFERENCES `Users` (`nickname`);

ALTER TABLE `Friendships`
    ADD FOREIGN KEY (`receiver_nickname`) REFERENCES `Users` (`nickname`);

ALTER TABLE `Orders`
    ADD FOREIGN KEY (`user_nickname`) REFERENCES `Users` (`nickname`);

ALTER TABLE `Discounts`
    ADD FOREIGN KEY (`order_id`, `order_date`) REFERENCES `Orders` (`id`, `order_date`);

ALTER TABLE `Discounts`
    ADD FOREIGN KEY (`coupon_code`) REFERENCES `Coupons` (`code`);

# Populate

INSERT INTO Categories
VALUES ('3x3'), ('4x4');

INSERT INTO Models
    (name, maker, category_name, price, discount_percentage, image_url, is_speed_cube, is_stickerless, is_magnetic)
VALUES
    ('WeiLong WR M V9', 'MoYu', '3x3', 17.99, NULL, 'https://cdn.shopify.com/s/files/1/0978/8602/files/MoYu-WeiLong-WR-M-V9-3x3-Magnetic-Standard-Frosted-Stickerless-Bright_540x.jpg?v=1682478273', 1, 1, 1),
    ('RS3 M', 'MoYu', '3x3', 9.99, NULL, 'https://cdn.shopify.com/s/files/1/0978/8602/products/MoYu-RS3-M-2020-3x3-Magnetic-Black-2_540x.jpg?v=1681849962', 1, 0, 1),
    ('GTS3 M', 'MoYu', '3x3', 24, NULL, 'https://cdn.shopify.com/s/files/1/0978/8602/products/MoYu-WeiLong-GTS2-M-3x3-Magnetic-Black_540x.jpg?v=1681847670', 1, 1, 1),
    ('WeiLong GTS3 M', 'MoYu', '3x3', 19.99, NULL, 'https://cdn.shopify.com/s/files/1/0978/8602/products/MoYu-WeiLong-GTS3-M-3x3-Magnetic-Stickerless-Bright_540x.jpg?v=1681848204', 1, 1, 0),
    ('356', 'GAN', '3x3', 26, NULL, 'https://cdn.shopify.com/s/files/1/0978/8602/products/GAN-356-3x3-Magnetic-Stickerless-Bright_540x.jpg?v=1681849509', 1, 1, 1),
    ('11 Pro', 'GAN', '3x3', 41.89, 0.1, 'https://cdn.shopify.com/s/files/1/0978/8602/products/GAN-11-Pro-3x3-Magnetic_540x.jpg?v=1681850528', 1, 1, 1),
    ('ZhiLong Mini', 'YJ', '3x3', 7.41, NULL, 'https://cdn.shopify.com/s/files/1/0978/8602/products/YJ-ZhiLong-Mini-50mm-3x3-Magnetic-Stickerless-Bright_540x.jpg?v=1681850645', 1, 1, 1),
    ('Valk 3', 'QiYi', '3x3', 23.99, 0.15, 'https://cdn.shopify.com/s/files/1/0978/8602/products/QiYi-Valk-3-3x3-Magnetic-Stickerless-Bright-4_540x.jpg?v=1681848780', 1, 1, 1),
    ('Sail W', 'QiYi', '3x3', 3.68, NULL, 'https://cdn.shopify.com/s/files/1/0978/8602/products/QiYi-Sail-W-3x3-White-2_540x.jpg?v=1681848838', 0, 0, 0),
    ('Big Sail', 'QiYi', '3x3', 4.99, NULL, 'https://cdn.shopify.com/s/files/1/0978/8602/products/QiYi-Big-Sail-60mm-3x3-Black_540x.jpg?v=1681847606', 1, 0, 0),
    ('Meilong', 'MoFang JiaoShi', '3x3', 6.99, 0.2, 'https://cdn.shopify.com/s/files/1/0978/8602/products/MoFang-JiaoShi-MeiLong-3x3-Magnetic-Stickerless-Bright_540x.jpg?v=1681849893', 1, 0, 1),
    ('Metallic', 'Cyclone Boys', '3x3', 16.49, NULL, 'https://cdn.shopify.com/s/files/1/0978/8602/products/Cyclone-Boys-Metallic-3x3-Magnetic-Stickerless-Bright_540x.jpg?v=1681852512', 0, 0, 1);

INSERT INTO CustomizableParts
    (model_name, model_maker, part)
VALUES
    ('WeiLong WR M V9', 'MoYu', 'Version'),
    ('RS3 M', 'MoYu', 'Version'),
    ('RS3 M', 'MoYu', 'Lubrication'),
    ('GTS3 M', 'MoYu', 'Lubrication'),
    ('356', 'GAN', 'Lubrication'),
    ('11 Pro', 'GAN', 'Internal Color'),
    ('Sail W', 'QiYi', 'Plastic Color'),
    ('Meilong', 'MoFang JiaoShi', 'Plastic Color');

INSERT INTO Customizations
    (model_name, model_maker, part, `change`, price, is_default)
VALUES
    ('WeiLong WR M V9', 'MoYu', 'Version', 'Standard', 0, TRUE),
    ('WeiLong WR M V9', 'MoYu', 'Version', 'Magnetic Levitation', 6, FALSE),
    ('WeiLong WR M V9', 'MoYu', 'Version', 'Ball Core', 12, FALSE),
    ('RS3 M', 'MoYu', 'Version', 'Standard', 0, TRUE),
    ('RS3 M', 'MoYu', 'Version', 'Magnetic Levitation', 5, FALSE),
    ('RS3 M', 'MoYu', 'Version', 'Ball Core', 9, FALSE),
    ('RS3 M', 'MoYu', 'Lubrication', 'No', 0, TRUE),
    ('RS3 M', 'MoYu', 'Lubrication', 'Yes', 5, FALSE),
    ('GTS3 M', 'MoYu', 'Lubrication', 'No', 0, TRUE),
    ('GTS3 M', 'MoYu', 'Lubrication', 'Yes', 5, FALSE),
    ('356', 'GAN', 'Lubrication', 'No', 0, TRUE),
    ('356', 'GAN', 'Lubrication', 'Yes', 5, FALSE),
    ('11 Pro', 'GAN', 'Internal Color', 'Black', 0, TRUE),
    ('11 Pro', 'GAN', 'Internal Color', 'None', 0, FALSE),
    ('Sail W', 'QiYi', 'Plastic Color', 'Black', 0, FALSE),
    ('Sail W', 'QiYi', 'Plastic Color', 'White', 0, TRUE),
    ('Meilong', 'MoFang JiaoShi', 'Plastic Color', 'Stickerless', 0, TRUE),
    ('Meilong', 'MoFang JiaoShi', 'Plastic Color', 'Black', 0,  FALSE);