------------------------
-- USERS & ACCOUNTS --
------------------------

CREATE TABLE users (
    _user_id INTEGER PRIMARY KEY AUTO_INCREMENT,
    _created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    display_name VARCHAR(50) NOT NULL,
    display_image text,
    gender ENUM("male", "female", "non_binary"),
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    biography VARCHAR(255),

    UNIQUE (display_name)
);

CREATE TABLE accounts (
    _account_id INTEGER PRIMARY KEY AUTO_INCREMENT,
    _user_id INTEGER,
    _created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    email VARCHAR(255) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    password_salt VARCHAR(255) NOT NULL,
    verification_status ENUM("unverified", "verified") NOT NULL DEFAULT "unverified",

    UNIQUE (_user_id, email),
    FOREIGN KEY (_user_id)
        REFERENCES users(_user_id)
        ON DELETE CASCADE
);

CREATE TABLE accounts_pending_verifications (
    _account_pending_verification_id INTEGER PRIMARY KEY AUTO_INCREMENT,
    _account_id INTEGER,
    _created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    UNIQUE (_account_id),
    FOREIGN KEY (_account_id)
        REFERENCES accounts(_account_id)
        ON DELETE CASCADE
);

CREATE TABLE accounts_verifications (
    _account_id INTEGER PRIMARY KEY,
    _verified_by INTEGER,
    _created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (_account_id)
        REFERENCES accounts(_account_id)
        ON DELETE CASCADE,
    FOREIGN KEY (_verified_by)
        REFERENCES users(_user_id)
        ON DELETE CASCADE
);

CREATE TABLE roles (
    _role_id INTEGER PRIMARY KEY AUTO_INCREMENT,
    _created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    name VARCHAR(50) NOT NULL,
    description VARCHAR(255),

    UNIQUE (name)
);

CREATE TABLE users_roles (
    _user_id INTEGER,
    _role_id INTEGER,

    PRIMARY KEY (_user_id, _role_id),
    FOREIGN KEY (_user_id)
        REFERENCES users(_user_id)
        ON DELETE CASCADE,
    FOREIGN KEY (_role_id)
        REFERENCES roles(_role_id)
        ON DELETE CASCADE
);

-------------------------------
-- AUTHENTICATION & SESSIONS --
-------------------------------

CREATE TABLE sessions (
    _session_id INTEGER PRIMARY KEY AUTO_INCREMENT,
    _user_id INTEGER,
    _session_token CHAR(36) NOT NULL DEFAULT (UUID()),
    _created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP NOT NULL DEFAULT (DATE_ADD(CURRENT_TIMESTAMP, INTERVAL 7 DAY)),
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    ip_address VARCHAR(45),
    user_agent VARCHAR(255),
    status ENUM("active", "inactive", "revoked") NOT NULL DEFAULT "active",
    status_updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    UNIQUE (_session_token),
    FOREIGN KEY (_user_id)
        REFERENCES users(_user_id)
        ON DELETE CASCADE
);

-------------------
-- NOTIFICATIONS --
-------------------

CREATE TABLE notification_types (
    _notification_type_id INTEGER PRIMARY KEY AUTO_INCREMENT,
    code VARCHAR(50) NOT NULL,
    description VARCHAR(255),

    UNIQUE (code)
);

CREATE TABLE notifications (
    _notification_id INTEGER PRIMARY KEY AUTO_INCREMENT,
    _user_id INTEGER,
    _notification_type_id INTEGER,
    _created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    read_at TIMESTAMP NULL DEFAULT NULL,
    priority ENUM("low", "normal", "high", "urgent", "critical") NOT NULL DEFAULT "low",
    message TEXT NOT NULL,

    FOREIGN KEY (_user_id)
        REFERENCES users(_user_id)
        ON DELETE CASCADE,
    FOREIGN KEY (_notification_type_id)
        REFERENCES notification_types(_notification_type_id)
        ON DELETE SET NULL
);

-----------------------
-- ITEMS & SUPPLIERS --
-----------------------

CREATE TABLE item_categories (
    _item_category_id INTEGER PRIMARY KEY AUTO_INCREMENT,
    _created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    name VARCHAR(100) NOT NULL,

    UNIQUE (name)
);

CREATE TABLE packagings (
    _packaging_id INTEGER PRIMARY KEY AUTO_INCREMENT,
    _created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(255),

    UNIQUE (name)
);

CREATE TABLE items (
    _item_id INTEGER PRIMARY KEY AUTO_INCREMENT,
    _item_uid CHAR(36) NOT NULL DEFAULT (UUID()),
    _created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(255),

    UNIQUE (_item_uid, name)
);

CREATE TABLE item_categories_items (
    _item_category_id INTEGER,
    _item_id INTEGER,
    PRIMARY KEY (_item_category_id, _item_id),
    FOREIGN KEY (_item_category_id)
        REFERENCES item_categories(_item_category_id)
        ON DELETE CASCADE,
    FOREIGN KEY (_item_id)
        REFERENCES items(_item_id)
        ON DELETE CASCADE
);

CREATE TABLE item_stocks (
    _item_stock_id INTEGER PRIMARY KEY AUTO_INCREMENT,
    _item_id INTEGER,
    _packaging_id INTEGER,
    _created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    quantity INTEGER,
    minimum_quantity INTEGER NOT NULL DEFAULT 0,
    srp_php DECIMAL(10, 2) NOT NULL CHECK (srp_php >= 0),
    price_php DECIMAL(10, 2) NOT NULL CHECK(price_php >= 0),
    UNIQUE (_item_id, _packaging_id),
    FOREIGN KEY (_item_id)
        REFERENCES items(_item_id)
        ON DELETE CASCADE,
    FOREIGN KEY (_packaging_id)
        REFERENCES packagings(_packaging_id)
        ON DELETE CASCADE
);

CREATE TABLE item_restocks (
    _item_restock_id INTEGER PRIMARY KEY AUTO_INCREMENT,
    _item_stock_id INTEGER,
    _created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    quantity_before INTEGER NOT NULL CHECK (quantity_before >= 0),
    quantity_after INTEGER NOT NULL CHECK (quantity_after > 0),
    quantity_added INTEGER NOT NULL CHECK (quantity_added > 0),
    UNIQUE (_item_stock_id),
    FOREIGN KEY (_item_stock_id)
        REFERENCES item_stocks(_item_stock_id)
        ON DELETE CASCADE
);

CREATE TABLE suppliers (
    _supplier_id INTEGER PRIMARY KEY AUTO_INCREMENT,
    _created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL,
    -- address
    street VARCHAR(255),
    city VARCHAR(100),
    state VARCHAR(100),
    postal_code VARCHAR(20),
    country VARCHAR(100),

    UNIQUE (name, email)
);

CREATE TABLE suppliers_items (
    _supplier_item_id INTEGER PRIMARY KEY AUTO_INCREMENT,
    _supplier_id INTEGER,
    _item_id INTEGER,
    _created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    srp_php DECIMAL(10,2) NOT NULL CHECK (srp_php >= 0),
    wsp_php DECIMAL(10,2) NOT NULL CHECK (wsp_php >= 0),

    UNIQUE (_supplier_id, _item_id),
    FOREIGN KEY (_supplier_id)
        REFERENCES suppliers(_supplier_id)
        ON DELETE CASCADE,
    FOREIGN KEY (_item_id)
        REFERENCES items(_item_id)
        ON DELETE CASCADE
);

-- this tells whether a user is part of our suppliers, thus they'll receive supplier-related information
CREATE TABLE suppliers_users (
    _supplier_user_id INTEGER PRIMARY KEY AUTO_INCREMENT,
    _supplier_id INTEGER,
    _user_id INTEGER,

    UNIQUE (_supplier_id, _user_id),
    FOREIGN KEY (_supplier_id)
        REFERENCES suppliers(_supplier_id)
        ON DELETE CASCADE,
    FOREIGN KEY (_user_id)
        REFERENCES users(_user_id)
        ON DELETE CASCADE
);

---------------
-- CUSTOMERS --
---------------

CREATE TABLE customers (
    _customer_id INTEGER PRIMARY KEY AUTO_INCREMENT,
    _created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    email VARCHAR(255),

    UNIQUE (email)
);

------------
-- ORDERS --
------------

CREATE TABLE orders (
    _order_id INTEGER PRIMARY KEY AUTO_INCREMENT,
    -- the employee that created this order
    _employee_id INTEGER,
    _supplier_id INTEGER,
    _created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    _order_number CHAR(36) NOT NULL DEFAULT (UUID()),
    discount_type ENUM("none", "fixed", "percentage") NOT NULL DEFAULT "none",
    discount_value_php DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    vat_percent DECIMAL(5,2) NOT NULL DEFAULT 12.00,
    status ENUM("pending", "acknowledged", "rejected", "cancelled") NOT NULL DEFAULT "pending",
    status_updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    remarks VARCHAR(255),

    UNIQUE (_order_number),
    FOREIGN KEY (_supplier_id)
        REFERENCES suppliers(_supplier_id)
        ON DELETE CASCADE,
    FOREIGN KEY (_employee_id)
        REFERENCES users(_user_id)
        ON DELETE CASCADE
);

CREATE TABLE order_payments (
    _order_payment_id INTEGER PRIMARY KEY AUTO_INCREMENT,
    _order_id INTEGER,
    _created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    reference_number VARCHAR(36) NULL,
    payment_method ENUM("cash", "gcash", "bank_transfer", "credit_card") NOT NULL,
    amount_php DECIMAL(10,2) NOT NULL CHECK(amount_php > 0),

    FOREIGN KEY (_order_id)
        REFERENCES orders(_order_id)
        ON DELETE SET NULL
);

CREATE TABLE items_orders (
    _item_order_id INTEGER PRIMARY KEY AUTO_INCREMENT,
    _order_id INTEGER,
    _item_id INTEGER,
    wsp_php DECIMAL(10,2) NOT NULL CHECK (wsp_php >= 0),
    quantity INTEGER NOT NULL CHECK (quantity > 0),

    UNIQUE (_order_id, _item_id),
    FOREIGN KEY (_order_id)
        REFERENCES orders(_order_id)
        ON DELETE CASCADE,
    FOREIGN KEY (_item_id)
        REFERENCES items(_item_id)
        ON DELETE CASCADE
);

-------------------------------
-- SALES AND CUSTOMER ORDERS --
-------------------------------

CREATE TABLE customer_orders (
    _customer_order_id INTEGER PRIMARY KEY AUTO_INCREMENT,
    _customer_id INTEGER NULL,
    -- employee that handled this transaction
    _employee_id INTEGER NULL,
    _customer_order_number CHAR(36) NOT NULL DEFAULT (UUID()),
    _created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    discount_type ENUM("none", "fixed", "percentage") NOT NULL DEFAULT "none",
    discount_value DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    vat_percent DECIMAL(5,2) NOT NULL DEFAULT 12.00,
    remarks VARCHAR(255),

    -- set null to preserve sales records
    FOREIGN KEY (_customer_id)
        REFERENCES customers(_customer_id)
        ON DELETE SET NULL,
    FOREIGN KEY (_employee_id)
        REFERENCES users(_user_id)
        ON DELETE SET NULL
);

CREATE TABLE customer_orders_item_stocks (
    _customer_order_item_id INTEGER PRIMARY KEY AUTO_INCREMENT,
    _customer_order_id INTEGER,
    _item_stock_id INTEGER,

    price_php DECIMAL(10,2) NOT NULL CHECK(price_php >= 0),
    quantity INTEGER NOT NULL CHECK(quantity > 0),

    UNIQUE (_customer_order_id, _item_stock_id),
    FOREIGN KEY (_customer_order_id)
        REFERENCES customer_orders(_customer_order_id)
        ON DELETE SET NULL,
    FOREIGN KEY (_item_stock_id)
        REFERENCES item_stocks(_item_stock_id)
        ON DELETE SET NULL
);

CREATE TABLE customer_payments (
    _customer_payment_id INTEGER PRIMARY KEY AUTO_INCREMENT,
    _customer_order_id INTEGER,
    _created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    payment_method ENUM("cash", "gcash", "credit_card", "bank_transfer", "voucher") NOT NULL,
    amount_php DECIMAL(10,2) NOT NULL CHECK(amount_php > 0),

    FOREIGN KEY(_customer_order_id)
        REFERENCES customer_orders(_customer_order_id)
);

CREATE TABLE sales (
    _sale_id INTEGER PRIMARY KEY AUTO_INCREMENT,
    _customer_order_id INTEGER,
    _created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    total_price_php DECIMAL(10, 2) NOT NULL CHECK(total_price_php >= 0),
    total_amount_paid_php DECIMAL(10, 2) NOT NULL CHECK(total_amount_paid_php >= 0),
    change_php DECIMAL(10,2) GENERATED ALWAYS AS (total_amount_paid_php - total_price_php),

    UNIQUE (_customer_order_id),
    FOREIGN KEY (_customer_order_id)
        REFERENCES customer_orders(_customer_order_id)
);

----------------
-- DELIVERIES --
----------------

CREATE TABLE deliveries (
    _delivery_id INTEGER PRIMARY KEY AUTO_INCREMENT,
    _created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    _delivery_number CHAR(36) NOT NULL DEFAULT (UUID()),
    expected_arrival_date TIMESTAMP,
    actual_arrival_date TIMESTAMP,
    status ENUM("pending", "in_transit", "delivered", "cancelled") NOT NULL DEFAULT "pending",
    status_updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fee_php DECIMAL(10,2) NOT NULL CHECK(fee_php >= 0),
    -- address
    street VARCHAR(255),
    city VARCHAR(100),
    state VARCHAR(100),
    postal_code VARCHAR(20),
    country VARCHAR(100),

    UNIQUE (_delivery_number)
);

CREATE TABLE orders_deliveries (
    _order_delivery_id INTEGER PRIMARY KEY AUTO_INCREMENT,
    _order_id INTEGER,
    _delivery_id INTEGER,

    UNIQUE (_order_id, _delivery_id),
    FOREIGN KEY (_order_id)
        REFERENCES orders(_order_id)
        ON DELETE CASCADE,
    FOREIGN KEY (_delivery_id)
        REFERENCES deliveries(_delivery_id)
        ON DELETE CASCADE
);

-- if a customer_order doesn't have this, then it's assumed the customer walked in
CREATE TABLE customer_orders_deliveries (
    _customer_order_delivery_id INTEGER PRIMARY KEY AUTO_INCREMENT,
    _customer_order_id INTEGER,
    _delivery_id INTEGER,

    UNIQUE (_customer_order_id, _delivery_id),
    FOREIGN KEY (_customer_order_id)
        REFERENCES customer_orders(_customer_order_id)
        ON DELETE CASCADE,
    FOREIGN KEY (_delivery_id)
        REFERENCES deliveries(_delivery_id)
        ON DELETE CASCADE
);

--------------
-- TRIGGERS --
--------------

-- for item quanities book-keeping and such, let's handle those operations on
-- the backend to avoid deadlocks and since it's pretty complicated.

DELIMITER $$

CREATE TRIGGER update_verification_status
AFTER INSERT ON accounts_verifications
FOR EACH ROW
BEGIN
    UPDATE accounts
    SET verification_status = "verified"
    WHERE _account_id = NEW._account_id;

    DELETE FROM accounts_pending_verifications
    WHERE _account_id = NEW._account_id;
END $$

CREATE TRIGGER update_session_status
BEFORE UPDATE ON sessions
FOR EACH ROW
BEGIN
    IF (NEW.status != OLD.status) THEN
        SET NEW.status_updated_at = CURRENT_TIMESTAMP;
    END IF;
END $$

CREATE TRIGGER update_order_status
BEFORE UPDATE ON orders
FOR EACH ROW
BEGIN
    IF (NEW.status != OLD.status) THEN
        SET NEW.status_updated_at = CURRENT_TIMESTAMP;
    END IF;
END $$

CREATE TRIGGER update_delivery_status
BEFORE UPDATE ON deliveries
FOR EACH ROW
BEGIN
    IF (NEW.status != OLD.status) THEN
        SET NEW.status_updated_at = CURRENT_TIMESTAMP;
    END IF;
END $$

DELIMITER ;

