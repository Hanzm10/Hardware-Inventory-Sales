
-- Seed for users
INSERT INTO users (_user_id, display_name)
VALUES 
(1, 'aaron'),
(2, 'hanz'),
(3, 'peter'),
(4, 'kurt'),
(5, 'jerick');

-- Seed for accounts
INSERT INTO accounts (_account_id, _user_id, email, password_hash, password_salt, verification_status, verified_at)
VALUES
-- Seed@123
(1, 1, 'aaron@seed.com', 'PjAxGXwlhB+ODoaaJlj/eni2afjGxEMUqPMcWbLcmyk=', 'MJFZAH5CTq3pI6oIgNsAhw==', 'verified', CURRENT_TIMESTAMP),
(2, 2, 'hanz@seed.com', 'PjAxGXwlhB+ODoaaJlj/eni2afjGxEMUqPMcWbLcmyk=', 'MJFZAH5CTq3pI6oIgNsAhw==', 'verified', CURRENT_TIMESTAMP),
(3, 3, 'peter@seed.com', 'PjAxGXwlhB+ODoaaJlj/eni2afjGxEMUqPMcWbLcmyk=', 'MJFZAH5CTq3pI6oIgNsAhw==', 'verified', CURRENT_TIMESTAMP),
(4, 4, 'kurt@seed.com', 'PjAxGXwlhB+ODoaaJlj/eni2afjGxEMUqPMcWbLcmyk=', 'MJFZAH5CTq3pI6oIgNsAhw==', 'verified', CURRENT_TIMESTAMP),
(5, 5, 'jerick@seed.com', 'PjAxGXwlhB+ODoaaJlj/eni2afjGxEMUqPMcWbLcmyk=', 'MJFZAH5CTq3pI6oIgNsAhw==', 'verified', CURRENT_TIMESTAMP);

-- Roles
INSERT INTO roles (_role_id, name, description)
VALUES
(1, 'admin', 'read and write access to everything'),
(2, 'clerk', 'read and write access to customer orders'),
(3, 'purchasing officer', 'read and write access to orders, supplier items, and sales, but cannot audit orders'),
(4, 'logistics', 'read and write access to inventory');

-- Assign roles
INSERT INTO users_roles (_user_id, _role_id)
VALUES
(1, 1),
(2, 1),
(3, 2),
(3, 3),
(4, 3),
(5, 4);

INSERT INTO notification_types (_notification_type_id, code, description)
VALUES
(1, 'inventory_low', 'Inventory is running low for a product'),
(2, 'inventory_update', 'Inventory levels have changed'),
(3, 'order_placed', 'A customer placed a new order'),
(4, 'order_shipped', 'An order has been shipped'),
(5, 'order_delayed', 'An order is delayed'),
(6, 'system_error', 'A system error or issue occurred'),
(7, 'system_maintenance', 'Scheduled system maintenance'),
(8, 'promotion', 'Promotional or sales event notification');

-- Seed item categories
INSERT INTO item_categories (_item_category_id, name) VALUES 
(1, 'Electrical'),
(2, 'Plumbing'),
(3, 'Tools'),
(4, 'Hardware');

-- Seed packagings
INSERT INTO packagings (_packaging_id, name, description) VALUES 
(1, 'Box', 'Cardboard box packaging'),
(2, 'Piece', 'Individual item'),
(3, 'Pack', 'Plastic wrapped bundle of items'),
(4, 'Set', 'Collection of related items sold together'),
(5, 'Unpackaged', 'Unpacked item pieces/units.');

-- Seed items
INSERT INTO items (_item_id, _item_uid, name, description) VALUES 
(1, UUID(), 'Hammer', '16oz steel claw hammer');
INSERT INTO items (_item_id, _item_uid, name, description) VALUES 
(2, UUID(), 'Screwdriver Set', 'Set of 6 assorted screwdrivers');
INSERT INTO items (_item_id, _item_uid, name, description) VALUES 
(3, UUID(), 'PVC Pipe', '1 inch diameter, 3 meter length');
INSERT INTO items (_item_id, _item_uid, name, description) VALUES 
(4, UUID(), 'Light Bulb', '10W LED bulb, warm white');

-- Assign categories to items
INSERT INTO item_categories_items (_item_category_id, _item_id) VALUES
-- Hammer (Tools, Hardware)
(3, 1),
(4, 1),
-- Screwdriver Set (Tools)
(3, 2),
-- PVC Pipe (Plumbing, Hardware)
(2, 3),
(4, 3),
-- Light Bulb (Electrical, Hardware)
(1, 4),
(4, 4);

-- Seed item stocks
INSERT INTO item_stocks (_item_stock_id, _item_id, _packaging_id, quantity, minimum_quantity, srp_php, price_php) VALUES
(1, 1, 2, 50, 10, 250.00, 200.00),   -- Hammer / Piece
(2, 2, 4, 20, 5, 600.00, 480.00),    -- Screwdriver Set / Set
(3, 3, 3, 100, 20, 120.00, 100.00),  -- PVC Pipe / Pack
(4, 4, 2, 150, 30, 80.00, 60.00);    -- Light Bulb / Piece

INSERT INTO item_restocks (_item_restock_id, _item_stock_id, quantity_before, quantity_after, quantity_added)
VALUES
(1, 1, 0, 50, 50),
(2, 2, 0, 20, 20),
(3, 3, 0, 100, 100),
(4, 4, 0, 150, 50);

-- Seed suppliers
INSERT INTO suppliers (_supplier_id, name, email, street, city, state, postal_code, country) VALUES
(1, 'ToolPro Inc.', 'contact@toolpro.com', '123 Industrial Ave', 'Manila', 'Metro Manila', '1000', 'Philippines'),
(2, 'PipeMasters', 'sales@pipemasters.ph', '456 Pipe St', 'Quezon City', 'Metro Manila', '1100', 'Philippines'),
(3, 'BrightLite Corp.', 'info@brightlite.com', '789 Light Rd', 'Makati', 'Metro Manila', '1200', 'Philippines');

-- Seed suppliers_items
INSERT INTO suppliers_items (_supplier_item_id, _supplier_id, _item_id, srp_php, wsp_php) VALUES
(1, 1, 1, 250.00, 180.00), -- ToolPro sells Hammer
(2, 1, 2, 600.00, 450.00), -- ToolPro sells Screwdriver Set
(3, 2, 3, 120.00, 90.00),  -- PipeMasters sells PVC Pipe
(4, 3, 4, 80.00, 55.00);   -- BrightLite sells Light Bulb

-- Seed orders (1 order per supplier)
INSERT INTO orders (_order_id, _order_number, _employee_id, _supplier_id, discount_type, discount_value_php, status, remarks)
VALUES
-- jerick is employee id
(1, UUID(), 5, 1, 'none', 0.00, 'pending', 'Urgent tool replenishment');
INSERT INTO orders (_order_id, _order_number, _employee_id, _supplier_id, discount_type, discount_value_php, status, remarks)
VALUES
(2, UUID(), 5, 2, 'percent', 10.00, 'pending', 'Restocking PVC pipes');
INSERT INTO orders (_order_id, _order_number, _employee_id, _supplier_id, discount_type, discount_value_php, status, remarks)
VALUES
(3, UUID(), 5, 3, 'fixed', 200.00, 'pending', 'LED bulbs for lighting section');

-- Seed items_orders
INSERT INTO items_orders (_item_order_id, _order_id, _item_id, wsp_php, quantity) VALUES
-- Order 1: ToolPro (Hammer, Screwdriver Set)
(1, 1, 1, 180.00, 30),
(2, 1, 2, 450.00, 15),

-- Order 2: PipeMasters (PVC Pipe)
(3, 2, 3, 90.00, 50),

-- Order 3: BrightLite (Light Bulb)
(4, 3, 4, 55.00, 100);

INSERT INTO customer_orders (_customer_order_id, _customer_order_number, _employee_id, discount_type, discount_value)
VALUES (1,UUID(), 3, 'percent', 20.00);
INSERT INTO customer_orders (_customer_order_id, _customer_order_number, _employee_id, discount_type, discount_value)
VALUES (2,UUID(), 3, 'fixed', 200.00);
INSERT INTO customer_orders (_customer_order_id, _customer_order_number, _employee_id, discount_type, discount_value)
VALUES (3,UUID(), 3, 'none', 0.00);

INSERT INTO customer_orders_item_stocks (_customer_order_item_id, _customer_order_id, _item_stock_id, price_php, quantity)
VALUES
(1, 1, 1, 200.00, 3),
(2, 1, 2, 480.00, 1),
(3, 2, 2, 480.00, 4),
(4, 2, 4, 60.00, 3),
(5, 2, 3, 100.00, 6),
(6, 3, 1, 200.00, 5),
(7, 3, 2, 480.00, 3),
(8, 3, 3, 100.00, 10),
(9, 3, 4, 60.00, 15);

INSERT INTO customer_payments (_customer_payment_id, _customer_order_id, payment_method, amount_php)
VALUES
(1, 1, 'cash', 150.00),
(2, 1, 'cash', 150.00),
(3, 2, 'cash', 1000.00),
(4, 3, 'cash', 200.00),
(5, 3, 'cash', 200.00),
(6, 3, 'cash', 100.00);
