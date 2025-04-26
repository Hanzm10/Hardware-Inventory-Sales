-- ===========================================
-- Tables Creation
-- ===========================================

CREATE TABLE `roles` (
  `_role_id` INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  `_role_created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `role_name` VARCHAR(50) UNIQUE NOT NULL,
  `role_description` TEXT
);

CREATE TABLE `users` (
  `_user_id` INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  `_user_created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `user_display_name` VARCHAR(255) UNIQUE NOT NULL,
  `user_display_image` TEXT COMMENT 'A link text to user''s display image',
  `user_gender` ENUM('male', 'female', 'unknown') NOT NULL DEFAULT 'unknown'
);

CREATE TABLE `user_roles` (
  `_user_id` INT NOT NULL,
  `_role_id` INT NOT NULL,
  PRIMARY KEY (`_user_id`, `_role_id`),
  CONSTRAINT `fk_user_roles_user_id` FOREIGN KEY (`_user_id`) REFERENCES `users` (`_user_id`) ON DELETE CASCADE,
  CONSTRAINT `fk_user_roles_role_id` FOREIGN KEY (`_role_id`) REFERENCES `roles` (`_role_id`) ON DELETE CASCADE
);

CREATE TABLE `user_credentials` (
  `_user_id` INT PRIMARY KEY NOT NULL,
  `user_email` VARCHAR(255) UNIQUE NOT NULL,
  `user_password` VARCHAR(255) NOT NULL,
  `user_first_name` VARCHAR(50),
  `user_last_name` VARCHAR(50),
  `user_phone_number` VARCHAR(20),
  CONSTRAINT `fk_user_credentials_user_id` FOREIGN KEY (`_user_id`) REFERENCES `users` (`_user_id`) ON DELETE CASCADE
);

CREATE TABLE `sessions` (
  `_session_id` INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  `_session_uid` CHAR(36) UNIQUE NOT NULL DEFAULT (UUID()),
  `_session_created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `_session_last_accessed_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `_session_expires_at` TIMESTAMP NOT NULL DEFAULT (CURRENT_TIMESTAMP + INTERVAL 7 DAY),
  `session_ip_address` VARCHAR(45),
  `session_user_agent` TEXT,
  `session_status` ENUM('active', 'inactive', 'revoked') NOT NULL DEFAULT 'active',
  `_user_id` INT NOT NULL,
  CONSTRAINT `fk_sessions_user_id` FOREIGN KEY (`_user_id`) REFERENCES `users` (`_user_id`) ON DELETE CASCADE
);

CREATE TABLE `item_categories` (
  `_item_category_id` INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  `item_category_name` VARCHAR(255) UNIQUE NOT NULL,
  `item_category_description` TEXT
);

CREATE TABLE `item_pack_types` (
  `_item_pack_type_id` INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  `item_pack_type_name` VARCHAR(255) UNIQUE NOT NULL COMMENT 'type of packaging (i.e. 1kg for nails, 2x2x8 per piece for wood, etc.)',
  `item_pack_type_description` TEXT
);

CREATE TABLE `items` (
  `_item_id` INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  `_item_created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `item_name` VARCHAR(255) NOT NULL COMMENT 'name of the item (i.e. 50mm nails, bold nails, etc.)',
  `item_minimum_quantity` INT NOT NULL,
  `item_suggested_retail_price_php` DECIMAL(10,2) NOT NULL,
  `item_wholesale_price_php` DECIMAL(10,2) NOT NULL,
  `_item_category_id` INT NOT NULL,
  `_item_pack_type_id` INT NOT NULL,
  CONSTRAINT `fk_items_item_category_id` FOREIGN KEY (`_item_category_id`) REFERENCES `item_categories` (`_item_category_id`) ON DELETE CASCADE
  CONSTRAINT `fk_items_item_pack_type_id` FOREIGN KEY (`_item_pack_type_id`) REFERENCES `item_pack_types` (`_item_pack_type_id`) ON DELETE CASCADE
);

CREATE TABLE `suppliers` (
  `_supplier_id` INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  `_supplier_created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `supplier_name` VARCHAR(255) UNIQUE NOT NULL,
  `supplier_address` VARCHAR(255),
  `supplier_email` VARCHAR(255) UNIQUE NOT NULL,
  `supplier_phone_number` VARCHAR(20)
);

CREATE TABLE `notifications` (
  `_notification_id` INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  `_notification_created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `notification_type` ENUM('low_stock', 'restock', 'order', 'transfer', 'system', 'other') NOT NULL DEFAULT 'system',
  `notification_priority` ENUM('none', 'normal', 'urgent', 'critical') NOT NULL DEFAULT 'normal',
  `notification_read_status` ENUM('read', 'unread') NOT NULL DEFAULT 'unread',
  `notification_message` TEXT NOT NULL,
  `notification_resolution_status` ENUM('pending', 'resolved') NOT NULL DEFAULT 'pending',
  `_user_id` INT NOT NULL,
  CONSTRAINT `fk_notifications_user_id` FOREIGN KEY (`_user_id`) REFERENCES `users` (`_user_id`) ON DELETE CASCADE
);

CREATE TABLE `item_restocks` (
  `_item_restock_id` INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  `_item_restock_created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `item_restock_quantity` INT NOT NULL,
  `item_restock_verification_status` ENUM('pending', 'approved', 'denied', 'requires_change') NOT NULL DEFAULT 'pending',
  `_item_id` INT NOT NULL,
  `_restocked_by_user_id` INT NOT NULL COMMENT 'The user that created the restock information',
  `_acknowledged_by_user_id` INT NOT NULL COMMENT 'The user that acknowledged this restock',
  CONSTRAINT `fk_item_restocks_item_id` FOREIGN KEY (`_item_id`) REFERENCES `items` (`_item_id`) ON DELETE CASCADE,
  CONSTRAINT `fk_item_restocks_restocked_by_user_id` FOREIGN KEY (`_restocked_by_user_id`) REFERENCES `users` (`_user_id`) ON DELETE CASCADE,
  CONSTRAINT `fk_item_restocks_acknowledged_by_user_id` FOREIGN KEY (`_acknowledged_by_user_id`) REFERENCES `users` (`_user_id`) ON DELETE CASCADE
);

CREATE TABLE `purchase_orders` (
  `_purchase_order_id` INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  `_purchase_order_created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `_purchase_order_number` CHAR(36) UNIQUE NOT NULL DEFAULT (UUID()),
  `purchase_order_quantity` INT NOT NULL,
  `purchase_order_unit_price` DECIMAL(10,2) NOT NULL,
  `purchase_order_pure_total_cost` DECIMAL(10,2) NOT NULL,
  `purchase_order_total_cost` DECIMAL(10,2) NOT NULL,
  `purchase_order_vat` DECIMAL(10,2) NOT NULL,
  `purchase_order_status` ENUM('pending_approval', 'approved', 'rejected', 'processing', 'shipped', 'delivered', 'canceled') DEFAULT 'pending_approval',
  `purchase_order_expected_delivery_date` TIMESTAMP NOT NULL,
  `purchase_order_actual_delivery_date` TIMESTAMP,
  `_supplier_id` INT NOT NULL,
  `_item_id` INT NOT NULL,
  CONSTRAINT `fk_purchase_orders_supplier_id` FOREIGN KEY (`_supplier_id`) REFERENCES `suppliers` (`_supplier_id`) ON DELETE CASCADE,
  CONSTRAINT `fk_purchase_orders_item_id` FOREIGN KEY (`_item_id`) REFERENCES `items` (`_item_id`) ON DELETE CASCADE
);

CREATE TABLE `customers` (
  `_customer_id` INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  `_customer_created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `customer_first_name` VARCHAR(100),
  `customer_last_name` VARCHAR(100),
  `customer_email` VARCHAR(255) UNIQUE,
  `customer_shipping_address` VARCHAR(255),
  `customer_phone_number` VARCHAR(20)
);

CREATE TABLE `customer_orders` (
  `_customer_order_id` INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  `_customer_order_created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `customer_order_payment` DECIMAL(10,2) NOT NULL,
  `customer_order_pure_total_cost` DECIMAL(10,2) NOT NULL,
  `customer_order_total_cost` DECIMAL(10,2) NOT NULL,
  `customer_order_vat` DECIMAL(10,2) NOT NULL,
  `_customer_id` INT,
  `_customer_order_receiver_id` INT NOT NULL,
  CONSTRAINT `fk_customer_orders_customer_id` FOREIGN KEY (`_customer_id`) REFERENCES `customers` (`_customer_id`) ON DELETE SET NULL,
  CONSTRAINT `fk_customer_orders_receiver_id` FOREIGN KEY (`_customer_order_receiver_id`) REFERENCES `users` (`_user_id`) ON DELETE CASCADE
);

CREATE TABLE `customer_delivery_orders` (
  `_customer_delivery_order_number` CHAR(36) UNIQUE PRIMARY KEY NOT NULL DEFAULT (UUID()),
  `customer_delivery_order_expected_date` TIMESTAMP NOT NULL,
  `customer_delivery_order_status` ENUM('pending', 'processing', 'shipped', 'delivered', 'completed', 'canceled') NOT NULL DEFAULT 'pending',
  `customer_delivery_order_arrival_date` TIMESTAMP,
  `_created_by_user_id` INT NOT NULL,
  `_customer_order_id` INT NOT NULL,
  CONSTRAINT `fk_customer_delivery_orders_created_by_user_id` FOREIGN KEY (`_created_by_user_id`) REFERENCES `users` (`_user_id`) ON DELETE CASCADE,
  CONSTRAINT `fk_customer_delivery_orders_customer_order_id` FOREIGN KEY (`_customer_order_id`) REFERENCES `customer_orders` (`_customer_order_id`) ON DELETE CASCADE
);

CREATE TABLE `customer_order_items` (
  `item_quantity` INT NOT NULL,
  `_customer_order_id` INT NOT NULL,
  `_item_id` INT NOT NULL,
  PRIMARY KEY (`_customer_order_id`, `_item_id`),
  CONSTRAINT `fk_customer_order_items_order_id` FOREIGN KEY (`_customer_order_id`) REFERENCES `customer_orders` (`_customer_order_id`) ON DELETE CASCADE,
  CONSTRAINT `fk_customer_order_items_item_id` FOREIGN KEY (`_item_id`) REFERENCES `items` (`_item_id`) ON DELETE CASCADE
);

CREATE TABLE `customer_order_refunds` (
  `_customer_order_refund_id` INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  `_customer_order_refund_created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `refund_status` ENUM('pending', 'approved', 'rejected', 'processed') NOT NULL DEFAULT 'pending',
  `refund_reason` TEXT NOT NULL,
  `_processed_by_user_id` INT NOT NULL,
  `_customer_order_id` INT NOT NULL,
  CONSTRAINT `fk_customer_order_refunds_processed_by_user_id` FOREIGN KEY (`_processed_by_user_id`) REFERENCES `users` (`_user_id`) ON DELETE CASCADE,
  CONSTRAINT `fk_customer_order_refunds_customer_order_id` FOREIGN KEY (`_customer_order_id`) REFERENCES `customer_orders` (`_customer_order_id`) ON DELETE CASCADE
);

CREATE TABLE `audit_logs` (
  `_audit_log_id` INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  `_audit_created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `audit_table_name` VARCHAR(100) NOT NULL,
  `audit_action` ENUM('INSERT', 'UPDATE', 'DELETE') NOT NULL,
  `audit_description` TEXT,
  `audit_previous_data` JSON COMMENT 'Data before operation',
  `audit_after_data` JSON COMMENT 'Data after operation',
  `_user_id` INT,
  CONSTRAINT `fk_audit_logs_user_id` FOREIGN KEY (`_user_audit_logs`) REFERENCES `users` (`_user_id`) ON DELETE SET NULL
);

-- ===========================================
-- Indexes Creation
-- ===========================================

CREATE INDEX `user_gender_index` ON `users` (`_user_id`, `user_gender`);
CREATE INDEX `session_status_index` ON `sessions` (`session_status`);
CREATE INDEX `notification_type_index` ON `notifications` (`notification_type`);
CREATE INDEX `notification_priority_index` ON `notifications` (`notification_priority`);
CREATE INDEX `notification_created_at_index` ON `notifications` (`_notification_created_at`);
CREATE INDEX `notification_read_status_index` ON `notifications` (`notification_read_status`);
CREATE INDEX `notification_type_per_user_index` ON `notifications` (`_user_id`, `notification_type`);
CREATE INDEX `notification_created_at_per_user_index` ON `notifications` (`_user_id`, `_notification_created_at`);
CREATE INDEX `notification_read_status_per_user_index` ON `notifications` (`_user_id`, `notification_read_status`);
CREATE INDEX `notification_priority_per_user_index` ON `notifications` (`_user_id`, `notification_priority`);
CREATE INDEX `purchase_orders_index_number` ON `purchase_orders` (`_purchase_order_number`);
CREATE INDEX `purchase_orders_index_status` ON `purchase_orders` (`purchase_order_status`);
CREATE INDEX `audit_table_name_per_action_index` ON `audit_logs` (`audit_table_name`, `audit_action`);