
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
(4, 'Hardware'),
(5, 'Paint & Finishes'),
(6, 'Gardening'),
(7, 'Automotive'),
(8, 'Fasteners'),
(9, 'Safety Equipment'),
(10, 'Adhesives & Sealants'),
(11, 'Power Tools'),
(12, 'Lighting'),
(13, 'Ladders & Scaffolding'),
(14, 'Cleaning Supplies');

-- Seed packagings
INSERT INTO packagings (_packaging_id, name, description) VALUES 
(1, 'Box', 'Cardboard box packaging'),
(2, 'Piece', 'Individual item'),
(3, 'Pack', 'Plastic wrapped bundle of items'),
(4, 'Set', 'Collection of related items sold together'),
(5, 'Unpackaged', 'Unpacked item pieces/units.'),
(6, 'Bottle', 'Plastic or glass bottle'),
(7, 'Tube', 'Squeezable tube for viscous contents'),
(8, 'Can', 'Sealed metal can'),
(9, 'Bag', 'Plastic or paper bag'),
(10, 'Tray', 'Flat tray container');;

-- Seed items
INSERT INTO items (_item_id, _item_uid, name, description) VALUES 
(1, UUID(), 'Hammer', '16oz steel claw hammer');
INSERT INTO items (_item_id, _item_uid, name, description) VALUES 
(2, UUID(), 'Screwdriver Set', 'Set of 6 assorted screwdrivers');
INSERT INTO items (_item_id, _item_uid, name, description) VALUES 
(3, UUID(), 'PVC Pipe', '1 inch diameter, 3 meter length');
INSERT INTO items (_item_id, _item_uid, name, description) VALUES 
(4, UUID(), 'Light Bulb', '10W LED bulb, warm white');
INSERT INTO items (_item_id, _item_uid, name, description) VALUES
(11, UUID(), 'Latex Paint', '1-gallon interior latex wall paint');
INSERT INTO items (_item_id, _item_uid, name, description) VALUES
(12, UUID(), 'Paint Roller Set', 'Roller, tray, and cover set for painting');
INSERT INTO items (_item_id, _item_uid, name, description) VALUES
(13, UUID(), 'Garden Shovel', 'Carbon steel gardening shovel');
INSERT INTO items (_item_id, _item_uid, name, description) VALUES
(14, UUID(), 'Pruning Shears', 'Stainless steel garden shears');
INSERT INTO items (_item_id, _item_uid, name, description) VALUES
(15, UUID(), 'Car Jack', 'Hydraulic jack, 2-ton capacity');
INSERT INTO items (_item_id, _item_uid, name, description) VALUES
(16, UUID(), 'Motor Oil', '1-liter 10W-40 synthetic motor oil');
INSERT INTO items (_item_id, _item_uid, name, description) VALUES
(17, UUID(), 'Screws Assortment', 'Box of assorted wood and metal screws');
INSERT INTO items (_item_id, _item_uid, name, description) VALUES
(18, UUID(), 'Safety Goggles', 'Anti-fog impact-resistant goggles');
INSERT INTO items (_item_id, _item_uid, name, description) VALUES
(19, UUID(), 'Super Glue', 'Instant-bond adhesive, 5g tube');
INSERT INTO items (_item_id, _item_uid, name, description) VALUES
(20, UUID(), 'Power Drill', 'Cordless hammer drill, 18V');
INSERT INTO items (_item_id, _item_uid, name, description) VALUES
(21, UUID(), 'LED Floodlight', '20W outdoor waterproof floodlight');
INSERT INTO items (_item_id, _item_uid, name, description) VALUES
(22, UUID(), 'Aluminum Ladder', '6-foot folding step ladder');
INSERT INTO items (_item_id, _item_uid, name, description) VALUES
(23, UUID(), 'Cleaning Rags', 'Pack of 20 microfiber cloths');
INSERT INTO items (_item_id, _item_uid, name, description) VALUES
(24, UUID(), 'Detergent', 'Liquid all-purpose cleaner, 1L');

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
(4, 4),
(11, 5),  -- Latex Paint → Paint & Finishes
(12, 5),  -- Paint Roller Set → Paint & Finishes
(13, 6),  -- Garden Shovel → Gardening
(14, 6),  -- Pruning Shears → Gardening
(15, 7),  -- Car Jack → Automotive
(16, 7),  -- Motor Oil → Automotive
(17, 8),  -- Screws Assortment → Fasteners
(18, 9),  -- Safety Goggles → Safety Equipment
(19, 10), -- Super Glue → Adhesives & Sealants
(20, 11), -- Power Drill → Power Tools
(21, 12), -- LED Floodlight → Lighting
(22, 13), -- Aluminum Ladder → Ladders & Scaffolding
(23, 14), -- Cleaning Rags → Cleaning Supplies
(24, 14); -- Detergent → Cleaning Supplies;

-- Seed item stocks
INSERT INTO item_stocks (_item_stock_id, _item_id, _packaging_id, quantity, minimum_quantity, srp_php, price_php) VALUES
(1, 1, 2, 50, 10, 250.00, 200.00),   -- Hammer / Piece
(2, 2, 4, 20, 5, 600.00, 480.00),    -- Screwdriver Set / Set
(3, 3, 3, 100, 20, 120.00, 100.00),  -- PVC Pipe / Pack
(4, 4, 2, 150, 30, 80.00, 60.00),
(11, 11, 8, 30, 10, 1400.00, 1200.00),  -- Latex Paint / Can
(12, 12, 4, 25, 5, 550.00, 450.00),    -- Paint Roller Set / Set
(13, 13, 2, 40, 10, 950.00, 800.00),   -- Garden Shovel / Piece
(14, 14, 2, 50, 15, 600.00, 500.00),   -- Pruning Shears / Piece
(15, 15, 2, 10, 5, 4000.00, 3500.00),  -- Car Jack / Piece
(16, 16, 6, 60, 15, 450.00, 380.00),   -- Motor Oil / Bottle
(17, 17, 1, 100, 30, 300.00, 250.00),  -- Screws Assortment / Box
(18, 18, 2, 75, 20, 250.00, 200.00),   -- Safety Goggles / Piece
(19, 19, 7, 150, 40, 80.00, 60.00),    -- Super Glue / Tube
(20, 20, 2, 20, 5, 4800.00, 4200.00),  -- Power Drill / Piece
(21, 21, 2, 35, 10, 1100.00, 900.00),  -- LED Floodlight / Piece
(22, 22, 2, 15, 5, 2000.00, 1800.00),  -- Aluminum Ladder / Piece
(23, 23, 3, 40, 10, 360.00, 300.00),   -- Cleaning Rags / Pack
(24, 24, 6, 80, 20, 180.00, 150.00);   -- Detergent / Bottle;    -- Light Bulb / Piece

INSERT INTO item_restocks (_item_restock_id, _item_stock_id, quantity_before, quantity_after, quantity_added)
VALUES
(1, 1, 0, 50, 50),
(2, 2, 0, 20, 20),
(3, 3, 0, 100, 100),
(4, 4, 0, 150, 50),
(11, 11, 0, 30, 30),
(12, 12, 0, 25, 25),
(13, 13, 0, 40, 40),
(14, 14, 0, 50, 50),
(15, 15, 0, 10, 10),
(16, 16, 0, 60, 60),
(17, 17, 0, 100, 100),
(18, 18, 0, 75, 75),
(19, 19, 0, 150, 150),
(20, 20, 0, 20, 20),
(21, 21, 0, 35, 35),
(22, 22, 0, 15, 15),
(23, 23, 0, 40, 40),
(24, 24, 0, 80, 80);

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
(4, 3, 4, 80.00, 55.00),
(5, 3, 11, 1400.00, 1100.00),  -- BrightLite sells Latex Paint
(6, 1, 12, 550.00, 420.00),    -- ToolPro sells Paint Roller Set
(7, 1, 13, 950.00, 700.00),    -- ToolPro sells Garden Shovel
(8, 1, 14, 600.00, 450.00),    -- ToolPro sells Pruning Shears
(9, 1, 15, 4000.00, 3000.00),  -- ToolPro sells Car Jack
(10, 2, 16, 450.00, 350.00),   -- PipeMasters sells Motor Oil
(11, 1, 17, 300.00, 220.00),   -- ToolPro sells Screws Assortment
(12, 3, 18, 250.00, 180.00),   -- BrightLite sells Safety Goggles
(13, 3, 19, 80.00, 60.00),     -- BrightLite sells Super Glue
(14, 1, 20, 4800.00, 3600.00), -- ToolPro sells Power Drill
(15, 3, 21, 1100.00, 800.00),  -- BrightLite sells LED Floodlight
(16, 1, 22, 2000.00, 1600.00), -- ToolPro sells Aluminum Ladder
(17, 2, 23, 360.00, 270.00),   -- PipeMasters sells Cleaning Rags
(18, 2, 24, 180.00, 130.00);   -- PipeMasters sells Detergent;   -- BrightLite sells Light Bulb

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
INSERT INTO customer_orders (_created_at, _customer_order_id, _customer_order_number, _employee_id, discount_type, discount_value)
VALUES (DATE_ADD('2018-01-01', INTERVAL FLOOR(RAND() * 365) DAY), 4, UUID(), 3, 'none', 0.00);
INSERT INTO customer_orders (_created_at, _customer_order_id, _customer_order_number, _employee_id, discount_type, discount_value)
VALUES (DATE_ADD('2018-01-01', INTERVAL FLOOR(RAND() * 365) DAY), 5, UUID(), 3, 'none', 0.00);
INSERT INTO customer_orders (_created_at, _customer_order_id, _customer_order_number, _employee_id, discount_type, discount_value)
VALUES (DATE_ADD('2018-01-01', INTERVAL FLOOR(RAND() * 365) DAY), 6, UUID(), 3, 'none', 0.00);
INSERT INTO customer_orders (_created_at, _customer_order_id, _customer_order_number, _employee_id, discount_type, discount_value)
VALUES (DATE_ADD('2018-01-01', INTERVAL FLOOR(RAND() * 365) DAY), 7, UUID(), 3, 'none', 0.00);
INSERT INTO customer_orders (_created_at, _customer_order_id, _customer_order_number, _employee_id, discount_type, discount_value)
VALUES (DATE_ADD('2018-01-01', INTERVAL FLOOR(RAND() * 365) DAY), 8, UUID(), 3, 'percent', 20.00);
INSERT INTO customer_orders (_created_at, _customer_order_id, _customer_order_number, _employee_id, discount_type, discount_value)
VALUES (DATE_ADD('2018-01-01', INTERVAL FLOOR(RAND() * 365) DAY), 9, UUID(), 3, 'none', 0.00);
INSERT INTO customer_orders (_created_at, _customer_order_id, _customer_order_number, _employee_id, discount_type, discount_value)
VALUES (DATE_ADD('2018-01-01', INTERVAL FLOOR(RAND() * 365) DAY), 10, UUID(), 3, 'none', 0.00);
INSERT INTO customer_orders (_created_at, _customer_order_id, _customer_order_number, _employee_id, discount_type, discount_value)
VALUES (DATE_ADD('2018-01-01', INTERVAL FLOOR(RAND() * 365) DAY), 11, UUID(), 3, 'fixed', 120.00);
INSERT INTO customer_orders (_created_at, _customer_order_id, _customer_order_number, _employee_id, discount_type, discount_value)
VALUES (DATE_ADD('2018-01-01', INTERVAL FLOOR(RAND() * 365) DAY), 12, UUID(), 3, 'none', 0.00);
INSERT INTO customer_orders (_created_at, _customer_order_id, _customer_order_number, _employee_id, discount_type, discount_value)
VALUES (DATE_ADD('2018-01-01', INTERVAL FLOOR(RAND() * 365) DAY), 13, UUID(), 3, 'percent', 10.00);
INSERT INTO customer_orders (_created_at, _customer_order_id, _customer_order_number, _employee_id, discount_type, discount_value)
VALUES (DATE_ADD('2018-01-01', INTERVAL FLOOR(RAND() * 365) DAY), 14, UUID(), 3, 'percent', 5.00);
INSERT INTO customer_orders (_created_at, _customer_order_id, _customer_order_number, _employee_id, discount_type, discount_value)
VALUES (DATE_ADD('2018-01-01', INTERVAL FLOOR(RAND() * 365) DAY), 15, UUID(), 3, 'none', 0.00);
INSERT INTO customer_orders (_created_at, _customer_order_id, _customer_order_number, _employee_id, discount_type, discount_value)
VALUES (DATE_ADD('2018-01-01', INTERVAL FLOOR(RAND() * 365) DAY), 16, UUID(), 3, 'percent', 20.00);
INSERT INTO customer_orders (_created_at, _customer_order_id, _customer_order_number, _employee_id, discount_type, discount_value)
VALUES (DATE_ADD('2018-01-01', INTERVAL FLOOR(RAND() * 365) DAY), 17, UUID(), 3, 'percent', 20.00);
INSERT INTO customer_orders (_created_at, _customer_order_id, _customer_order_number, _employee_id, discount_type, discount_value)
VALUES (DATE_ADD('2018-01-01', INTERVAL FLOOR(RAND() * 365) DAY), 18, UUID(), 3, 'fixed', 20.00);
INSERT INTO customer_orders (_created_at, _customer_order_id, _customer_order_number, _employee_id, discount_type, discount_value)
VALUES (DATE_ADD('2018-01-01', INTERVAL FLOOR(RAND() * 365) DAY), 19, UUID(), 3, 'fixed', 200.00);
INSERT INTO customer_orders (_created_at, _customer_order_id, _customer_order_number, _employee_id, discount_type, discount_value)
VALUES (DATE_ADD('2018-01-01', INTERVAL FLOOR(RAND() * 365) DAY), 20, UUID(), 3, 'none', 0.00);
INSERT INTO customer_orders (_created_at, _customer_order_id, _customer_order_number, _employee_id, discount_type, discount_value)
VALUES (DATE_ADD('2018-01-01', INTERVAL FLOOR(RAND() * 365) DAY), 21, UUID(), 3, 'none', 0.00);
INSERT INTO customer_orders (_created_at, _customer_order_id, _customer_order_number, _employee_id, discount_type, discount_value)
VALUES (DATE_ADD('2018-01-01', INTERVAL FLOOR(RAND() * 365) DAY), 22, UUID(), 3, 'none', 0.00);
INSERT INTO customer_orders (_created_at, _customer_order_id, _customer_order_number, _employee_id, discount_type, discount_value)
VALUES (DATE_ADD('2018-01-01', INTERVAL FLOOR(RAND() * 365) DAY), 23, UUID(), 3, 'none', 0.00);
INSERT INTO customer_orders (_created_at, _customer_order_id, _customer_order_number, _employee_id, discount_type, discount_value)
VALUES (DATE_ADD('2018-01-01', INTERVAL FLOOR(RAND() * 365) DAY), 24, UUID(), 3, 'none', 0.00);
INSERT INTO customer_orders (_created_at, _customer_order_id, _customer_order_number, _employee_id, discount_type, discount_value)
VALUES (DATE_ADD('2018-01-01', INTERVAL FLOOR(RAND() * 365) DAY), 25, UUID(), 3, 'none', 0.00);
INSERT INTO customer_orders (_created_at, _customer_order_id, _customer_order_number, _employee_id, discount_type, discount_value)
VALUES (DATE_ADD('2018-01-01', INTERVAL FLOOR(RAND() * 365) DAY), 26, UUID(), 3, 'none', 0.00);

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
(9, 3, 4, 60.00, 15),
(10, 4, 1, 200.0, 2),
(11, 4, 2, 480.0, 2),
(12, 5, 3, 100.00, 4),
(13, 6, 3, 100.00, 1),
(14, 7, 4, 60.00, 1),
(15, 8, 4, 60.00, 2),
(16, 9, 1, 200.0, 1),
(17, 9, 3, 100.00, 2),
(18, 10, 1, 200.00, 2),
(19, 11, 2, 480.00, 1),
(20, 13, 4, 60.00, 4),
(21, 14, 1, 200.00, 2),
(22, 14, 2, 480.00, 1),
(23, 15, 3, 100.00, 3),
(24, 16, 1, 200.00, 1),
(25, 16, 4, 60.00, 2),
(26, 17, 2, 480.00, 2),
(27, 17, 3, 100.00, 1),
(28, 18, 3, 100.00, 2),
(29, 19, 4, 60.00, 5),
(30, 20, 1, 200.00, 1),
(31, 21, 2, 480.00, 1),
(32, 21, 3, 100.00, 1),
(33, 22, 1, 200.00, 1),
(34, 23, 4, 60.00, 2),
(35, 24, 2, 480.00, 1),
(36, 24, 4, 60.00, 1),
(37, 25, 3, 100.00, 1),
(38, 26, 1, 200.00, 2);

INSERT INTO customer_payments (_customer_payment_id, _customer_order_id, payment_method, amount_php)
VALUES
(1, 1, 'cash', 150.00),
(2, 1, 'cash', 150.00),
(3, 2, 'cash', 1000.00),
(4, 3, 'cash', 200.00),
(5, 3, 'cash', 200.00),
(6, 3, 'cash', 100.00),
(7, 4, 'cash', 500.00),
(8, 5, 'cash', 400.00),
(9, 6, 'cash', 100.00),
(10, 7, 'cash', 100.00),
(11, 8, 'cash', 150.00),
(12, 9, 'cash', 200.00),
(13, 10, 'cash', 400.00),
(14, 11, 'cash', 600.00),
(15, 12, 'cash', 200.00),
(16, 13, 'cash', 300.00),
(17, 14, 'cash', 300.00),
(18, 15, 'cash', 300.00),
(19, 16, 'cash', 350.00),
(20, 17, 'cash', 300.00),
(21, 18, 'cash', 200.00),
(22, 19, 'cash', 400.00),
(23, 20, 'cash', 200.00),
(24, 21, 'cash', 250.00),
(25, 22, 'cash', 200.00),
(26, 23, 'cash', 150.00),
(27, 24, 'cash', 200.00),
(28, 25, 'cash', 100.00),
(29, 26, 'cash', 400.00);
