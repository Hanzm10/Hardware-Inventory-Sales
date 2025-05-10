CREATE TABLE deliveries (
    _delivery_id INTEGER PRIMARY KEY AUTO_INCREMENT,
    _created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    _delivery_number CHAR(36) NOT NULL DEFAULT (UUID()),
    expected_arrival_date TIMESTAMP NULL,
    actual_arrival_date TIMESTAMP NULL,
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