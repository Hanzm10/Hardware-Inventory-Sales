CREATE TABLE `roles` (
  `_role_id` integer PRIMARY KEY NOT NULL AUTO_INCREMENT,
  `_role_created_at` timestamp NOT NULL DEFAULT (now()),
  `role_name` varchar(50) UNIQUE NOT NULL,
  `role_description` text
);

CREATE TABLE `users` (
  `_user_id` integer PRIMARY KEY NOT NULL AUTO_INCREMENT,
  `_user_created_at` timestamp NOT NULL DEFAULT (now()),
  `user_display_name` varchar(255) UNIQUE,
  `user_display_image` text COMMENT 'A link text to user''s display image',
  `user_gender` ENUM ('male', 'female', 'unknown') NOT NULL DEFAULT 'unknown'
);

CREATE TABLE `user_roles` (
  `_user_id` integer NOT NULL,
  `_role_id` integer NOT NULL,
  PRIMARY KEY (`_user_id`, `_role_id`)
);

CREATE TABLE `user_credentials` (
  `_user_id` integer PRIMARY KEY NOT NULL,
  `user_email` varchar(255) UNIQUE NOT NULL,
  `user_password` varchar(255) NOT NULL,
  `user_first_name` varchar(50),
  `user_last_name` varchar(50),
  `user_phone_number` varchar(20)
);

CREATE TABLE `sessions` (
  `_session_id` integer NOT NULL AUTO_INCREMENT,
  `_user_id` integer NOT NULL,
  `_session_uid` char(36) UNIQUE NOT NULL DEFAULT (uuid()),
  `_session_created_at` timestamp NOT NULL DEFAULT (now()),
  `_session_expires_at` timestamp NOT NULL DEFAULT (now() + interval 7 day),
  `session_ip_address` varchar(45),
  `session_user_agent` text,
  `session_status` ENUM ('active', 'inactive', 'revoked') NOT NULL DEFAULT 'active',
  PRIMARY KEY (`_session_id`, `_user_id`)
);

CREATE TABLE `item_categories` (
  `_item_category_id` integer PRIMARY KEY NOT NULL AUTO_INCREMENT,
  `item_category_name` varchar(255) UNIQUE NOT NULL,
  `item_category_description` text
);

CREATE TABLE `items` (
  `_item_id` integer NOT NULL AUTO_INCREMENT,
  `_item_created_at` timestamp NOT NULL DEFAULT (now()),
  `item_name` varchar(255) NOT NULL COMMENT 'name of the item (i.e. 50mm nails, bold nails, etc.)',
  `item_pack_type` varchar(255) NOT NULL COMMENT 'type of packaging (i.e. 1kg for nails, 2x2x8 per piece for wood, etc.)',
  `item_minimum_quantity` int NOT NULL,
  `item_srp` decimal(10,2) NOT NULL,
  `item_wsp` decimal(10,2) NOT NULL,
  `_item_category_id` integer NOT NULL,
  PRIMARY KEY (`_item_id`, `_item_category_id`)
);

CREATE TABLE `suppliers` (
  `_supplier_id` integer PRIMARY KEY NOT NULL AUTO_INCREMENT,
  `_supplier_created_at` timestamp NOT NULL DEFAULT (now()),
  `supplier_name` varchar(255) NOT NULL,
  `supplier_address` varchar(255),
  `supplier_email` varchar(255) NOT NULL,
  `supplier_phone_number` varchar(20)
);

CREATE TABLE `notifications` (
  `_notification_id` integer PRIMARY KEY NOT NULL AUTO_INCREMENT,
  `_notification_created_at` timestamp NOT NULL DEFAULT (now()),
  `notification_type` ENUM ('low_stock', 'restock', 'order', 'transfer', 'system', 'other') NOT NULL DEFAULT 'system',
  `notification_priority` ENUM ('none', 'normal', 'urgent', 'critical') NOT NULL DEFAULT 'normal' COMMENT 'whether the information of the notification requires action.',
  `notification_read_status` ENUM ('read', 'unread') NOT NULL DEFAULT 'unread',
  `notification_message` text NOT NULL,
  `notification_resolution_status` ENUM ('pending', 'resolved') NOT NULL DEFAULT 'pending' COMMENT 'whether the notification has been resolved or is still pending. (i.e. a low stock alert)',
  `_user_id` integer NOT NULL
);

CREATE TABLE `item_restocks` (
  `_item_restock_id` integer PRIMARY KEY NOT NULL AUTO_INCREMENT,
  `_item_restock_created_at` timestamp NOT NULL DEFAULT (now()),
  `item_restock_quantity` int NOT NULL,
  `item_restock_verification_status` ENUM ('pending', 'approved', 'denied', 'requires_change') NOT NULL DEFAULT 'pending',
  `_item_id` integer NOT NULL,
  `_restocked_by_user_id` integer NOT NULL COMMENT 'the user that created the restock information',
  `_acknowledged_by_user_id` integer NOT NULL COMMENT 'the user that acknowledged this restock'
);

CREATE TABLE `purchase_orders` (
  `_purchase_order_id` integer PRIMARY KEY NOT NULL AUTO_INCREMENT,
  `_purchase_order_created_at` timestamp NOT NULL DEFAULT (now()),
  `_purchase_order_number` char(36) UNIQUE NOT NULL DEFAULT (uuid()) COMMENT 'used for tracking the purchase order',
  `purchase_order_quantity` int NOT NULL,
  `purchase_order_unit_price` decimal(10,2) NOT NULL COMMENT 'cost per item quantity',
  `purchase_order_pure_total_cost` decimal(10,2) NOT NULL COMMENT 'quantity * unit price',
  `purchase_order_total_cost` decimal(10,2) NOT NULL COMMENT 'the real total cost of the purchase order which takes vat, and maybe discounts, into account.',
  `purchage_order_vat` decimal(10,2) NOT NULL COMMENT 'the vat added to this purchase order in Philippine pesos',
  `purchase_order_status` ENUM ('pending_approval', 'approved', 'rejected', 'processing', 'shipped', 'delivered', 'canceled') DEFAULT 'pending_approval',
  `purchase_order_expected_delivery_date` timestamp NOT NULL,
  `purchase_order_actual_delivery_date` timestamp,
  `_supplier_id` integer NOT NULL,
  `_item_id` integer NOT NULL
);

CREATE TABLE `customer_orders` (
  `_customer_order_id` integer PRIMARY KEY NOT NULL AUTO_INCREMENT,
  `_customer_order_created_at` timestamp NOT NULL DEFAULT (now()),
  `customer_order_payment` decimal(10,2) NOT NULL COMMENT 'the payment the customer gave in Philippine pesos',
  `customer_order_pure_total_cost` decimal(10,2) NOT NULL COMMENT 'the total amount the customer has to pay for',
  `customer_order_total_cost` decimal(10,2) NOT NULL COMMENT 'the real total amount the customer has to pay for. This includes the vat, discounts (in the future), etc.',
  `customer_order_vat` decimal(10,2) NOT NULL COMMENT 'the vat added to the customer order''s total',
  `_customer_id` integer COMMENT 'the customer that placed this order, could be null if customer does not exist in database',
  `_customer_order_receiver_id` integer NOT NULL COMMENT 'the employee that received this order'
);

CREATE TABLE `customer_delivery_orders` (
  `_customer_delivery_order_number` char(36) UNIQUE DEFAULT (uuid()) COMMENT 'used for tracking customer orders if it is to be delivered.',
  `customer_delivery_order_expected_date` timestamp NOT NULL,
  `customer_delivery_order_status` ENUM ('pending', 'processing', 'shipped', 'delivered', 'completed', 'canceled') NOT NULL DEFAULT 'pending',
  `customer_delivery_order_arrival_date` timestamp,
  `_customer_order_id` integer PRIMARY KEY NOT NULL
);

CREATE TABLE `customer_order_items` (
  `item_quantity` int NOT NULL,
  `_customer_order_id` integer NOT NULL,
  `_item_id` integer NOT NULL,
  PRIMARY KEY (`_customer_order_id`, `_item_id`)
);

CREATE TABLE `customer_order_refunds` (
  `_customer_order_refund_id` integer PRIMARY KEY NOT NULL AUTO_INCREMENT,
  `_customer_order_refund_created_at` timestamp NOT NULL DEFAULT (now()),
  `refund_status` ENUM ('pending', 'approved', 'rejected', 'processed') NOT NULL DEFAULT 'pending',
  `refund_reason` text NOT NULL COMMENT 'Reason provided by customer or staff',
  `refund_total_amount` decimal(10,2) NOT NULL COMMENT 'total amount to be refunded. This is an accumulation of the refunded amount per item that will reference this refunds table.',
  `_processed_by_user_id` integer NOT NULL COMMENT 'User that processed the refund.'
);

CREATE TABLE `customer_order_refund_items` (
  `item_quantity` int NOT NULL COMMENT 'Quantity refunded',
  `item_refund_total_amount` decimal(10,2) NOT NULL COMMENT 'Total refund amount for this item',
  `_customer_order_refund_id` integer NOT NULL,
  `_customer_order_item_id` integer NOT NULL,
  PRIMARY KEY (`_customer_order_refund_id`, `_customer_order_item_id`)
);

CREATE TABLE `customers` (
  `_customer_id` integer PRIMARY KEY NOT NULL AUTO_INCREMENT,
  `_customer_created_at` timestamp NOT NULL DEFAULT (now()),
  `customer_first_name` varchar(100),
  `customer_last_name` varchar(100),
  `customer_email` varchar(255) UNIQUE,
  `customer_shipping_address` varchar(255),
  `customer_phone_number` varchar(20)
);

CREATE TABLE `audit_logs` (
  `_audit_log_id` integer PRIMARY KEY NOT NULL AUTO_INCREMENT,
  `_audit_created_at` timestamp NOT NULL DEFAULT (now()),
  `audit_table_name` varchar(100) NOT NULL,
  `audit_action` ENUM ('INSERT', 'UPDATE', 'DELETE') NOT NULL,
  `audit_description` text,
  `_user_id` integer
);

CREATE INDEX `user_gender_index` ON `users` (`_user_id`, `user_gender`);

CREATE INDEX `session_status_index` ON `sessions` (`session_status`);

CREATE INDEX `suppliers_index_2` ON `suppliers` (`_supplier_id`);

CREATE INDEX `notification_type_index` ON `notifications` (`notification_type`);

CREATE INDEX `notification_priority_index` ON `notifications` (`notification_priority`);

CREATE INDEX `notification_created_at_index` ON `notifications` (`_notification_created_at`);

CREATE INDEX `notification_read_status_index` ON `notifications` (`notification_read_status`);

CREATE INDEX `notification_type_per_user_index` ON `notifications` (`_user_id`, `notification_type`);

CREATE INDEX `notification_created_at_per_user_index` ON `notifications` (`_user_id`, `_notification_created_at`);

CREATE INDEX `notification_read_status_per_user_index` ON `notifications` (`_user_id`, `notification_read_status`);

CREATE INDEX `notification_priority_per_user_index` ON `notifications` (`_user_id`, `notification_priority`);

CREATE INDEX `purchase_orders_index_11` ON `purchase_orders` (`_purchase_order_number`);

CREATE INDEX `purchase_orders_index_12` ON `purchase_orders` (`purchase_order_status`);

CREATE INDEX `audit_table_name_per_action_index` ON `audit_logs` (`audit_table_name`, `audit_action`);

CREATE INDEX `audit_action_index` ON `audit_logs` (`audit_table_name`);

ALTER TABLE `user_roles` ADD FOREIGN KEY (`_user_id`) REFERENCES `users` (`_user_id`);

ALTER TABLE `user_roles` ADD FOREIGN KEY (`_role_id`) REFERENCES `roles` (`_role_id`);

ALTER TABLE `user_credentials` ADD FOREIGN KEY (`_user_id`) REFERENCES `users` (`_user_id`);

ALTER TABLE `sessions` ADD FOREIGN KEY (`_user_id`) REFERENCES `users` (`_user_id`);

ALTER TABLE `items` ADD FOREIGN KEY (`_item_category_id`) REFERENCES `item_categories` (`_item_category_id`);

ALTER TABLE `notifications` ADD FOREIGN KEY (`_user_id`) REFERENCES `users` (`_user_id`);

ALTER TABLE `item_restocks` ADD FOREIGN KEY (`_item_id`) REFERENCES `items` (`_item_id`);

ALTER TABLE `item_restocks` ADD FOREIGN KEY (`_restocked_by_user_id`) REFERENCES `users` (`_user_id`);

ALTER TABLE `item_restocks` ADD FOREIGN KEY (`_acknowledged_by_user_id`) REFERENCES `users` (`_user_id`);

ALTER TABLE `purchase_orders` ADD FOREIGN KEY (`_supplier_id`) REFERENCES `suppliers` (`_supplier_id`);

ALTER TABLE `purchase_orders` ADD FOREIGN KEY (`_item_id`) REFERENCES `items` (`_item_id`);

ALTER TABLE `customer_orders` ADD FOREIGN KEY (`_customer_id`) REFERENCES `customers` (`_customer_id`);

ALTER TABLE `customer_orders` ADD FOREIGN KEY (`_customer_order_receiver_id`) REFERENCES `users` (`_user_id`);

ALTER TABLE `customer_delivery_orders` ADD FOREIGN KEY (`_customer_order_id`) REFERENCES `customer_orders` (`_customer_id`);

ALTER TABLE `customer_order_items` ADD FOREIGN KEY (`_customer_order_id`) REFERENCES `customer_orders` (`_customer_order_id`);

ALTER TABLE `customer_order_items` ADD FOREIGN KEY (`_item_id`) REFERENCES `items` (`_item_id`);

ALTER TABLE `customer_order_refunds` ADD FOREIGN KEY (`_processed_by_user_id`) REFERENCES `users` (`_user_id`);

ALTER TABLE `customer_order_refund_items` ADD FOREIGN KEY (`_customer_order_refund_id`) REFERENCES `customer_order_refunds` (`_customer_order_refund_id`);

ALTER TABLE `customer_order_refund_items` ADD FOREIGN KEY (`_customer_order_item_id`) REFERENCES `customer_order_items` (`_customer_order_id`);

ALTER TABLE `audit_logs` ADD FOREIGN KEY (`_user_id`) REFERENCES `users` (`_user_id`);
