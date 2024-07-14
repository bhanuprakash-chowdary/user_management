
CREATE DATABASE IF NOT EXISTS user_management;

USE user_management;

CREATE TABLE `user_details` (
  `user_id` int NOT NULL AUTO_INCREMENT,
  `user_name` varchar(50) DEFAULT NULL,
  `user_password` varchar(50) DEFAULT NULL,
  `user_mobile_number` varchar(10) DEFAULT NULL,
  `user_email_address` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`user_id`)
);

CREATE TABLE `journals` (
  `id` int NOT NULL AUTO_INCREMENT,
  `event_message` varchar(200) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
);
