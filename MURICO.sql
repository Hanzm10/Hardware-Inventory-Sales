USE murico;

CREATE TABLE customers (
  _customer_id INT NOT NULL AUTO_INCREMENT,
  _customer_created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  
  customer_first_name VARCHAR(100),
  customer_last_name VARCHAR(100),
  customer_email VARCHAR(255) UNIQUE,
  customer_shipping_address VARCHAR(255),
  customer_phone_number VARCHAR(20),
  
  PRIMARY KEY (_customer_id)
) ENGINE=InnoDB;

-- -----------------------------------------------------
-- Table: customer_orders
-- -----------------------------------------------------
CREATE TABLE customer_orders (
  _customer_order_id INT NOT NULL AUTO_INCREMENT,
  _customer_order_created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  
  _customer_id INT,  -- the customer that placed this order; can be NULL if not in the database
  _customer_order_receiver_id INT NOT NULL, -- the employee that received this order
  _customer_order_company_branch_id INT NOT NULL, -- the branch that received the order
  
  PRIMARY KEY (_customer_order_id),
  FOREIGN KEY (_customer_id) REFERENCES customers(_customer_id),
  FOREIGN KEY (_customer_order_receiver_id) REFERENCES users(user_id),
  FOREIGN KEY (_customer_order_company_branch_id) REFERENCES company_branches(_company_branch_id)
) ENGINE=InnoDB;

-- -----------------------------------------------------
-- Table: customer_delivery_orders
-- -----------------------------------------------------
CREATE TABLE customer_delivery_orders (
  _customer_delivery_order_number CHAR(36) NOT NULL UNIQUE DEFAULT (UUID()),  -- tracking number for delivery
  customer_delivery_order_expected_date TIMESTAMP NOT NULL,
  customer_delivery_order_status ENUM('pending','processing','shipped','delivered','completed','canceled') NOT NULL DEFAULT 'pending',
  customer_delivero_order_arrival_date TIMESTAMP NULL,
  
  _customer_order_id INT NOT NULL,
  PRIMARY KEY (_customer_order_id),
  FOREIGN KEY (_customer_order_id) REFERENCES customer_orders(_customer_order_id)
) ENGINE=InnoDB;

-- -----------------------------------------------------
-- Table: customer_order_items
-- -----------------------------------------------------
CREATE TABLE customer_order_items (
  _customer_order_id INT NOT NULL,
  _item_id INT NOT NULL,
  item_quantity INT NOT NULL,
  
  PRIMARY KEY (_customer_order_id, _item_id),
  FOREIGN KEY (_customer_order_id) REFERENCES customer_orders(_customer_order_id),
  FOREIGN KEY (_item_id) REFERENCES items(_item_id)
) ENGINE=InnoDB;

-- -----------------------------------------------------
-- Table: customer_order_refunds
-- -----------------------------------------------------
CREATE TABLE customer_order_refunds (
  _customer_order_refund_id INT NOT NULL AUTO_INCREMENT,
  _customer_order_refund_created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  
  refund_status ENUM('pending','approved','rejected','processed') NOT NULL DEFAULT 'pending',
  refund_reason TEXT NOT NULL,  -- Reason provided by customer or staff
  refund_amount DECIMAL(10,2) NOT NULL,  -- total amount to be refunded
  
  _customer_order_id INT NOT NULL,
  _processed_by_user_id INT NOT NULL, -- User that processed the refund.
  
  PRIMARY KEY (_customer_order_refund_id),
  FOREIGN KEY (_customer_order_id) REFERENCES customer_orders(_customer_order_id),
  FOREIGN KEY (_processed_by_user_id) REFERENCES users(user_id)
) ENGINE=InnoDB;

-- -----------------------------------------------------
-- Table: customer_order_refund_items
-- -----------------------------------------------------
CREATE TABLE customer_order_refund_items (
  _customer_order_refund_id INT NOT NULL,
  _customer_order_item_id INT NOT NULL,  -- Note: references customer_order_items; adjust FK as needed for composite keys
  item_quantity INT NOT NULL,  -- Quantity refunded
  item_refund_amount DECIMAL(10,2) NOT NULL,  -- Total refund amount for this item
  
  PRIMARY KEY (_customer_order_refund_id, _customer_order_item_id),
  FOREIGN KEY (_customer_order_refund_id) REFERENCES customer_order_refunds(_customer_order_refund_id)
  -- For _customer_order_item_id, consider creating a composite foreign key referencing customer_order_items if needed.
) ENGINE=InnoDB;
