CREATE TABLE orders (
    _order_id INTEGER PRIMARY KEY AUTO_INCREMENT,
    _supplier_id INTEGER,
    -- employee that created this order
    _employee_id INTEGER,
    _created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    _order_number CHAR(36) UNIQUE NOT NULL DEFAULT (UUID()),
    discount_type ENUM("none", "fixed", "percent") NOT NULL DEFAULT "none",
    discount_value_php DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    vat_percent DECIMAL(5,2) NOT NULL DEFAULT 12.00,
    status ENUM("pending", "acknowledged", "rejected", "cancelled", "audited") NOT NULL DEFAULT "pending",
    status_updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    remarks VARCHAR(255),
    
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

	CONSTRAINT item_orders_unique UNIQUE (_order_id, _item_id),

    FOREIGN KEY (_order_id)
        REFERENCES orders(_order_id)
        ON DELETE CASCADE,
    FOREIGN KEY (_item_id)
        REFERENCES items(_item_id)
        ON DELETE CASCADE
);
