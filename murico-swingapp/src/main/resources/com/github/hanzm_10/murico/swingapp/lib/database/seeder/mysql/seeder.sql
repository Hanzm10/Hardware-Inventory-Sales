-- Seed for users
INSERT INTO users (display_name)
VALUES 
('aaron'),
('hanz'),
('peter'),
('kurt'),
('jerick');

-- Seed for accounts
INSERT INTO accounts (_user_id, email, password_hash, password_salt, verification_status, verified_at)
VALUES
-- Seed@123
(1, 'aaron@seed.com', 'PjAxGXwlhB+ODoaaJlj/eni2afjGxEMUqPMcWbLcmyk=', 'MJFZAH5CTq3pI6oIgNsAhw==', 'verified', CURRENT_TIMESTAMP),
(2, 'hanz@seed.com', 'PjAxGXwlhB+ODoaaJlj/eni2afjGxEMUqPMcWbLcmyk=', 'MJFZAH5CTq3pI6oIgNsAhw==', 'verified', CURRENT_TIMESTAMP),
(3, 'peter@seed.com', 'PjAxGXwlhB+ODoaaJlj/eni2afjGxEMUqPMcWbLcmyk=', 'MJFZAH5CTq3pI6oIgNsAhw==', 'verified', CURRENT_TIMESTAMP),
(4, 'kurt@seed.com', 'PjAxGXwlhB+ODoaaJlj/eni2afjGxEMUqPMcWbLcmyk=', 'MJFZAH5CTq3pI6oIgNsAhw==', 'verified', CURRENT_TIMESTAMP),
(5, 'jerick@seed.com', 'PjAxGXwlhB+ODoaaJlj/eni2afjGxEMUqPMcWbLcmyk=', 'MJFZAH5CTq3pI6oIgNsAhw==', 'verified', CURRENT_TIMESTAMP);

-- Roles
INSERT INTO roles (name, description)
VALUES
('admin', 'read and write access to everything'),
('clerk', 'read and write access to customer orders'),
('purchasing officer', 'read and write access to orders, supplier items, and sales, but cannot audit orders'),
('logistics', 'read and write access to inventory');

-- Assign roles
INSERT INTO users_roles (_user_id, _role_id)
VALUES
(1, 1),
(2, 1),
(3, 2),
(3, 3),
(4, 3),
(5, 4);

INSERT INTO notification_types (code, description)
VALUES
('inventory_low', 'Inventory is running low for a product'),
('inventory_update', 'Inventory levels have changed'),
('order_placed', 'A customer placed a new order'),
('order_shipped', 'An order has been shipped'),
('order_delayed', 'An order is delayed'),
('system_error', 'A system error or issue occurred'),
('system_maintenance', 'Scheduled system maintenance'),
('promotion', 'Promotional or sales event notification');

-- Seed item categories
INSERT INTO item_categories (name) VALUES 
('Electrical'),
('Plumbing'),
('Tools'),
('Hardware');

-- Seed packagings
INSERT INTO packagings (name, description) VALUES 
('Box', 'Cardboard box packaging'),
('Piece', 'Individual item'),
('Pack', 'Plastic wrapped bundle of items'),
('Set', 'Collection of related items sold together'),
('Unpackaged', 'Unpacked item pieces/units.');

-- Seed items
INSERT INTO items (name, description) VALUES 
('Hammer', '16oz steel claw hammer'),
('Screwdriver Set', 'Set of 6 assorted screwdrivers'),
('PVC Pipe', '1 inch diameter, 3 meter length'),
('Light Bulb', '10W LED bulb, warm white');

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
INSERT INTO item_stocks (_item_id, _packaging_id, quantity, minimum_quantity, srp_php, price_php) VALUES
(1, 2, 50, 10, 250.00, 200.00),   -- Hammer / Piece
(2, 4, 20, 5, 600.00, 480.00),    -- Screwdriver Set / Set
(3, 3, 100, 20, 120.00, 100.00),  -- PVC Pipe / Pack
(4, 2, 150, 30, 80.00, 60.00);    -- Light Bulb / Piece

INSERT INTO item_restocks (_item_stock_id, quantity_before, quantity_after, quantity_added)
VALUES
(1, 0, 50, 50),
(2, 0, 20, 20),
(3, 0, 100, 100),
(4, 0, 150, 50);

-- Seed suppliers
INSERT INTO suppliers (name, email, street, city, state, postal_code, country) VALUES
('ToolPro Inc.', 'contact@toolpro.com', '123 Industrial Ave', 'Manila', 'Metro Manila', '1000', 'Philippines'),
('PipeMasters', 'sales@pipemasters.ph', '456 Pipe St', 'Quezon City', 'Metro Manila', '1100', 'Philippines'),
('BrightLite Corp.', 'info@brightlite.com', '789 Light Rd', 'Makati', 'Metro Manila', '1200', 'Philippines');

-- Seed suppliers_items
INSERT INTO suppliers_items (_supplier_id, _item_id, srp_php, wsp_php) VALUES
(1, 1, 250.00, 180.00), -- ToolPro sells Hammer
(1, 2, 600.00, 450.00), -- ToolPro sells Screwdriver Set
(2, 3, 120.00, 90.00),  -- PipeMasters sells PVC Pipe
(3, 4, 80.00, 55.00);   -- BrightLite sells Light Bulb

-- Seed orders (1 order per supplier)
INSERT INTO orders (_employee_id, _supplier_id, discount_type, discount_value_php, status, remarks)
VALUES
-- jerick is employee id
(5, 1, 'none', 0.00, 'pending', 'Urgent tool replenishment'),
(5, 2, 'percent', 10.00, 'pending', 'Restocking PVC pipes'),
(5, 3, 'fixed', 200.00, 'pending', 'LED bulbs for lighting section');

-- Get order IDs if needed manually
-- SELECT _order_id, _supplier_id FROM orders;

-- Seed items_orders
INSERT INTO items_orders (_order_id, _item_id, wsp_php, quantity) VALUES
-- Order 1: ToolPro (Hammer, Screwdriver Set)
(1, 1, 180.00, 30),
(1, 2, 450.00, 15),

-- Order 2: PipeMasters (PVC Pipe)
(2, 3, 90.00, 50),

-- Order 3: BrightLite (Light Bulb)
(3, 4, 55.00, 100);

INSERT INTO customer_orders (_employee_id, discount_type, discount_value)
VALUES (3, 'percent', 20.00), (3, 'fixed', 200.00), (3, 'none', 0.00);

INSERT INTO customer_orders_item_stocks (_customer_order_id, _item_stock_id, price_php, quantity)
VALUES
(1, 1, 200.00, 3),
(1, 2, 480.00, 1),
(2, 2, 480.00, 4),
(2, 4, 60.00, 3),
(2, 3, 100.00, 6),
(3, 1, 200.00, 5),
(3, 2, 480.00, 3),
(3, 3, 100.00, 10),
(3, 4, 60.00, 15);

INSERT INTO customer_payments (_customer_order_id, payment_method, amount_php)
VALUES
(1, 'cash', 150.00),
(1, 'cash', 150.00),
(2, 'cash', 1000.00),
(3, 'cash', 200.00),
(3, 'cash', 200.00),
(3, 'cash', 100.00);
