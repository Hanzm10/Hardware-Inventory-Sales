CREATE TABLE item_categories (
    _item_category_id INTEGER PRIMARY KEY AUTO_INCREMENT,
    _created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    name VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE packagings (
    _packaging_id INTEGER PRIMARY KEY AUTO_INCREMENT,
    _created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    name VARCHAR(100) UNIQUE NOT NULL,
    description VARCHAR(255)
);

CREATE TABLE items (
    _item_id INTEGER PRIMARY KEY AUTO_INCREMENT,
    _item_uid CHAR(36) UNIQUE NOT NULL DEFAULT (UUID()),
    _created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    name VARCHAR(255) UNIQUE NOT NULL,
    description VARCHAR(255)
);

CREATE TABLE item_categories_items (
    _item_category_id INTEGER,
    _item_id INTEGER,
    CONSTRAINT item_categories_pk PRIMARY KEY (_item_category_id, _item_id),
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
    
    CONSTRAINT item_packaging_unique UNIQUE (_item_id, _packaging_id),
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
    FOREIGN KEY (_item_stock_id)
        REFERENCES item_stocks(_item_stock_id)
        ON DELETE CASCADE
);

CREATE TABLE suppliers (
    _supplier_id INTEGER PRIMARY KEY AUTO_INCREMENT,
    _created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    name VARCHAR(100) UNIQUE NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    -- address
    street VARCHAR(255),
    city VARCHAR(100),
    state VARCHAR(100),
    postal_code VARCHAR(20),
    country VARCHAR(100)
);

CREATE TABLE suppliers_items (
    _supplier_item_id INTEGER PRIMARY KEY AUTO_INCREMENT,
    _supplier_id INTEGER,
    _item_id INTEGER,
    _created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    srp_php DECIMAL(10,2) NOT NULL CHECK (srp_php >= 0),
    wsp_php DECIMAL(10,2) NOT NULL CHECK (wsp_php >= 0),

	CONSTRAINT supplier_item_unique UNIQUE (_supplier_id, _item_id),
    FOREIGN KEY (_supplier_id)
        REFERENCES suppliers(_supplier_id)
        ON DELETE CASCADE,
    FOREIGN KEY (_item_id)
        REFERENCES items(_item_id)
        ON DELETE CASCADE
);