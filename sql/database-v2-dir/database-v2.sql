CREATE TABLE `users` (
  `id` int PRIMARY KEY,
  `username` varchar(255),
  `password` varchar(255),
  `phone` varchar(255),
  `email` varchar(255),
  `details` text
);

CREATE TABLE `supplier` (
  `id` int PRIMARY KEY,
  `name` varchar(255),
  `address` varchar(255),
  `phone` varchar(255),
  `email` varchar(255),
  `details` text
);

CREATE TABLE `product_category` (
  `id` int PRIMARY KEY,
  `name` varchar(255),
  `description` text
);

CREATE TABLE `product` (
  `id` int PRIMARY KEY,
  `name` varchar(255),
  `supplier_id` int,
  `product_type_id` int,
  `quantity_in_stock` int,
  `unit_price` float,
  `discount_percentage` float,
  `description` text
);

CREATE TABLE `customer` (
  `id` int PRIMARY KEY,
  `name` varchar(255),
  `address` varchar(255),
  `phone` varchar(255),
  `email` varchar(255)
);

CREATE TABLE `order` (
  `id` int PRIMARY KEY,
  `customer_id` int,
  `user_id` int,
  `date_recorded` date,
  `status` int
);

CREATE TABLE `product_in_order` (
  `order_id` int,
  `product_id` int,
  `quantity` int
);

CREATE TABLE `invoice` (
  `id` int PRIMARY KEY,
  `customer_id` int,
  `user_id` int,
  `order_id` int,
  `payment_type` int,
  `total_price` double,
  `total_paid` double,
  `date_recorded` date
);

CREATE TABLE `goods_import` (
  `id` int PRIMARY KEY,
  `product_id` int,
  `quantity` int,
  `unit_price` double,
  `total_price` double,
  `date_imported` date,
  `user_id` int
);

ALTER TABLE `product` ADD FOREIGN KEY (`product_type_id`) REFERENCES `product_category` (`id`);

ALTER TABLE `product` ADD FOREIGN KEY (`supplier_id`) REFERENCES `supplier` (`id`);

ALTER TABLE `order` ADD FOREIGN KEY (`customer_id`) REFERENCES `customer` (`id`);

ALTER TABLE `order` ADD FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

ALTER TABLE `product_in_order` ADD FOREIGN KEY (`order_id`) REFERENCES `order` (`id`);

ALTER TABLE `product_in_order` ADD FOREIGN KEY (`product_id`) REFERENCES `product` (`id`);

ALTER TABLE `invoice` ADD FOREIGN KEY (`customer_id`) REFERENCES `customer` (`id`);

ALTER TABLE `invoice` ADD FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

ALTER TABLE `invoice` ADD FOREIGN KEY (`order_id`) REFERENCES `order` (`id`);

ALTER TABLE `goods_import` ADD FOREIGN KEY (`product_id`) REFERENCES `product` (`id`);

ALTER TABLE `goods_import` ADD FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);
