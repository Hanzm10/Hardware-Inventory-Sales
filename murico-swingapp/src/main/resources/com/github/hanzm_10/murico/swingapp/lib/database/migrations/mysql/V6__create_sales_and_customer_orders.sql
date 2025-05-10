CREATE TABLE customer_orders (
    _customer_order_id INTEGER PRIMARY KEY AUTO_INCREMENT,
    _customer_id INTEGER NULL,
    -- employee that handled this transaction
    _employee_id INTEGER NULL,
    _customer_order_number CHAR(36) NOT NULL DEFAULT (UUID()), 
    _created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    discount_type ENUM("none", "fixed", "percent") NOT NULL DEFAULT "none",
    discount_value DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    vat_percent DECIMAL(5,2) NOT NULL DEFAULT 12.00,
    remarks VARCHAR(255),

	UNIQUE (_customer_order_number),
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
