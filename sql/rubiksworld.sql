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
    `shipping_price`   DECIMAL(4, 2) NOT NULL DEFAULT 2.80,
    `minimum_subtotal` DECIMAL(4, 2) NOT NULL DEFAULT 15
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
    ADD FOREIGN KEY (`model_name`, `model_maker`) REFERENCES `Models` (`name`, `maker`);

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
VALUES ('2x2'), ('3x3'), ('4x4'), ('5x5'), ('Exotic'), ('Lubricant'), ('Accessory');

INSERT INTO Models
    (name, maker, category_name, price, discount_percentage, image_url, is_speed_cube, is_stickerless, is_magnetic)
VALUES
    ('WeiLong WR M V9', 'MoYu', '3x3', 17.99, NULL, 'https://cdn.shopify.com/s/files/1/0978/8602/files/MoYu-WeiLong-WR-M-V9-3x3-Magnetic-Standard-Frosted-Stickerless-Bright_540x.jpg?v=1682478273', 1, 1, 1),
    ('RS3 M', 'MoYu', '3x3', 9.99, NULL, 'https://cdn.shopify.com/s/files/1/0978/8602/products/MoYu-RS3-M-2020-3x3-Magnetic-Black-2_540x.jpg?v=1681849962', 1, 0, 1),
    ('GTS3 M', 'MoYu', '3x3', 24, NULL, 'https://cdn.shopify.com/s/files/1/0978/8602/products/MoYu-WeiLong-GTS2-M-3x3-Magnetic-Black_540x.jpg?v=1681847670', 1, 0, 1),
    ('WeiLong GTS3 M', 'MoYu', '3x3', 19.99, NULL, 'https://cdn.shopify.com/s/files/1/0978/8602/products/MoYu-WeiLong-GTS3-M-3x3-Magnetic-Stickerless-Bright_540x.jpg?v=1681848204', 1, 1, 0),
    ('356', 'GAN', '3x3', 26, NULL, 'https://cdn.shopify.com/s/files/1/0978/8602/products/GAN-356-3x3-Magnetic-Stickerless-Bright_540x.jpg?v=1681849509', 1, 1, 1),
    ('11 Pro', 'GAN', '3x3', 41.89, 0.1, 'https://cdn.shopify.com/s/files/1/0978/8602/products/GAN-11-Pro-3x3-Magnetic_540x.jpg?v=1681850528', 1, 1, 1),
    ('ZhiLong Mini', 'YJ', '3x3', 7.41, NULL, 'https://cdn.shopify.com/s/files/1/0978/8602/products/YJ-ZhiLong-Mini-50mm-3x3-Magnetic-Stickerless-Bright_540x.jpg?v=1681850645', 1, 1, 1),
    ('Valk 3', 'QiYi', '3x3', 23.99, 0.15, 'https://cdn.shopify.com/s/files/1/0978/8602/products/QiYi-Valk-3-3x3-Magnetic-Stickerless-Bright-4_540x.jpg?v=1681848780', 1, 1, 1),
    ('Sail W', 'QiYi', '3x3', 3.68, NULL, 'https://cdn.shopify.com/s/files/1/0978/8602/products/QiYi-Sail-W-3x3-White-2_540x.jpg?v=1681848838', 0, 0, 0),
    ('Big Sail', 'QiYi', '3x3', 4.99, NULL, 'https://cdn.shopify.com/s/files/1/0978/8602/products/QiYi-Big-Sail-60mm-3x3-Black_540x.jpg?v=1681847606', 1, 0, 0),
    ('Meilong', 'MoFang JiaoShi', '3x3', 6.99, 0.2, 'https://cdn.shopify.com/s/files/1/0978/8602/products/MoFang-JiaoShi-MeiLong-3x3-Magnetic-Stickerless-Bright_540x.jpg?v=1681849893', 1, 0, 1),
    ('Metallic', 'Cyclone Boys', '3x3', 16.49, NULL, 'https://cdn.shopify.com/s/files/1/0978/8602/products/Cyclone-Boys-Metallic-3x3-Magnetic-Stickerless-Bright_540x.jpg?v=1681852512', 0, 0, 1),
    ('Little Magic', 'YuXin', '3x3', 4.64, NULL, 'https://cdn.shopify.com/s/files/1/0978/8602/products/YuXin-Little-Magic-3x3-Stickerless-Bright_540x.jpg?v=1681847815', 0, 1, 0),
    ('YuPo V2', 'YJ', '2x2', 6.51, NULL, 'https://cdn.shopify.com/s/files/1/0978/8602/products/YJ-YuPo-V2-2x2-Magnetic-Stickerless-Bright_540x.jpg?v=1681848530', 0, 1, 0),
    ('Pyraminx', 'QiYi', 'Exotic', 7.45, 0.1, 'https://cdn.shopify.com/s/files/1/0978/8602/products/QiYi-MS-Pyraminx-Magnetic-Stickerless-Bright_540x.jpg?v=1681849569', 0, 1, 1),
    ('Galaxy V2 Megaminx', 'X-Man', 'Exotic', 18.50, NULL, 'https://cdn.shopify.com/s/files/1/0978/8602/products/X-Man-Galaxy-V2-Megaminx-Sculpted-Stickerless-Bright_540x.jpg?v=1681847935', 0, 1, 0),
    ('MeiLong Skewb', 'MoFang JiaoShi', 'Exotic', 5.40, NULL, 'https://cdn.shopify.com/s/files/1/0978/8602/products/MoFang-JiaoShi-MeiLong-Skewb-Stickerless-Bright_540x.jpg?v=1681849974', 0, 1, 0),
    ('Gear Cube', 'QiYi', 'Exotic', 6.50, NULL, 'https://cdn.shopify.com/s/files/1/0978/8602/products/QiYi-3x3-Gear-Cube-Tiled-Black_a8b77767-829b-4719-b6b5-06c808d8f812_540x.jpg?v=1681880198', 0, 0, 0),
    ('MeiLong 4x4', 'MoFang JiaoShi', '4x4', 10.20, NULL, 'https://cdn.shopify.com/s/files/1/0978/8602/products/MoFang-JiaoShi-MeiLong-4x4-Magnetic-Black-2_540x.jpg?v=1681849918', 0, 0, 1),
    ('MGC 5x5', 'YJ', '5x5', 20.50, 0.15, 'https://cdn.shopify.com/s/files/1/0978/8602/products/YJ-MGC-5x5-Magnetic-Black-2_540x.jpg?v=1681849430', 0, 0, 1),
    ('Lubricant', 'Martian', 'Lubricant', 5.60, NULL, 'https://cdn.shopify.com/s/files/1/0978/8602/products/Martian-Lubricant_540x.jpg?v=1681847624', 0, 0, 0),
    ('Halo Smart Timer', 'GAN', 'Accessory', 39.99, NULL, 'https://cdn.shopify.com/s/files/1/0978/8602/products/GAN-Halo-Smart-Timer-Bluetooth-Blue_540x.jpg?v=1681856081', 0, 0, 0),
    ('G5 Pro Timer', 'SpeedStacks', 'Accessory', 30.80, 0.05, 'https://cdn.shopify.com/s/files/1/0978/8602/products/SpeedStacks-G5-Pro-Timer-Factory_540x.jpg?v=1681881393', 0, 0, 0),
    ('Display Stand', 'GAN', 'Accessory', 30.80, 0.05, 'https://cdn.shopify.com/s/files/1/0978/8602/products/GAN-Cube-Display-Stand-4_c7a2bc1b-9232-4f87-bee4-1b99df74f817_540x.jpg?v=1681851661', 0, 0, 0);

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
    ('Meilong', 'MoFang JiaoShi', 'Plastic Color'),
    ('Pyraminx', 'QiYi', 'Lubrication'),
    ('Lubricant', 'Martian', 'Size');

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
    ('Meilong', 'MoFang JiaoShi', 'Plastic Color', 'Black', 0,  FALSE),
    ('Pyraminx', 'QiYi', 'Lubrication', 'No', 0, TRUE),
    ('Pyraminx', 'QiYi', 'Lubrication', 'Yes', 5, FALSE),
    ('Lubricant', 'Martian', 'Size', '5ml', 0, TRUE),
    ('Lubricant', 'Martian', 'Size', '10ml', 4, FALSE),
    ('Lubricant', 'Martian', 'Size', '15ml', 7, FALSE);

INSERT INTO Users
    (nickname, name, surname)
VALUES
    ('luca_rossi', 'Luca', 'Rossi'),
    ('giulia_russo', 'Giulia', 'Russo'),
    ('ferrari50', 'Lorenzo', 'Ferrari'),
    ('chiarina02', 'Chiara', 'Bianchi'),
    ('fraroma', 'Francesco', 'Romano'),
    ('galletta_', 'Alessia', 'Gallo'),
    ('marchino05', 'Marco', 'Conti'),
    ('marisofi', 'Sofia', 'Marino'),
    ('darkmatt', 'Matteo', 'Greco'),
    ('alilombi', 'Alice', 'Lombardi');