
-- Seed for users
INSERT INTO users (_user_id, display_name, display_image)
VALUES 
(1, 'aaron', 'profile_picture/aaron.png'),
(2, 'hanz', 'profile_picture/hanz.png'),
(3, 'peter', 'profile_picture/peter.png'),
(4, 'kurt', 'profile_picture/kurt.png'),
(5, 'jerick', 'profile_picture/jerick.png');


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
INSERT INTO item_categories (_item_category_id, name) VALUES 
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
(5, 'Unpackaged', 'Unpacked item pieces/units.');
INSERT INTO packagings (_packaging_id, name, description) VALUES 
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
(5, UUID(), 'Latex Paint', '1-gallon interior latex wall paint');
INSERT INTO items (_item_id, _item_uid, name, description) VALUES
(6, UUID(), 'Paint Roller Set', 'Roller, tray, and cover set for painting');
INSERT INTO items (_item_id, _item_uid, name, description) VALUES
(7, UUID(), 'Garden Shovel', 'Carbon steel gardening shovel');
INSERT INTO items (_item_id, _item_uid, name, description) VALUES
(8, UUID(), 'Pruning Shears', 'Stainless steel garden shears');
INSERT INTO items (_item_id, _item_uid, name, description) VALUES
(9, UUID(), 'Car Jack', 'Hydraulic jack, 2-ton capacity');
INSERT INTO items (_item_id, _item_uid, name, description) VALUES
(10, UUID(), 'Motor Oil', '1-liter 10W-40 synthetic motor oil');
INSERT INTO items (_item_id, _item_uid, name, description) VALUES
(11, UUID(), 'Screws Assortment', 'Box of assorted wood and metal screws');
INSERT INTO items (_item_id, _item_uid, name, description) VALUES
(12, UUID(), 'Safety Goggles', 'Anti-fog impact-resistant goggles');
INSERT INTO items (_item_id, _item_uid, name, description) VALUES
(13, UUID(), 'Super Glue', 'Instant-bond adhesive, 5g tube');
INSERT INTO items (_item_id, _item_uid, name, description) VALUES
(14, UUID(), 'Power Drill', 'Cordless hammer drill, 18V');
INSERT INTO items (_item_id, _item_uid, name, description) VALUES
(15, UUID(), 'LED Floodlight', '20W outdoor waterproof floodlight');
INSERT INTO items (_item_id, _item_uid, name, description) VALUES
(16, UUID(), 'Aluminum Ladder', '6-foot folding step ladder');
INSERT INTO items (_item_id, _item_uid, name, description) VALUES
(17, UUID(), 'Cleaning Rags', 'Pack of 20 microfiber cloths');
INSERT INTO items (_item_id, _item_uid, name, description) VALUES
(18, UUID(), 'Detergent', 'Liquid all-purpose cleaner, 1L');

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
INSERT INTO item_categories_items (_item_category_id, _item_id) VALUES
(5, 5),  -- Latex Paint → Paint & Finishes
(5, 6),  -- Paint Roller Set → Paint & Finishes
(6, 7),  -- Garden Shovel → Gardening
(6, 8),  -- Pruning Shears → Gardening
(7, 9),  -- Car Jack → Automotive
(7, 10),  -- Motor Oil → Automotive
(8, 11),  -- Screws Assortment → Fasteners
(9, 12),  -- Safety Goggles → Safety Equipment
(10, 13), -- Super Glue → Adhesives & Sealants
(11, 14), -- Power Drill → Power Tools
(12, 15), -- LED Floodlight → Lighting
(13, 16), -- Aluminum Ladder → Ladders & Scaffolding
(14, 17), -- Cleaning Rags → Cleaning Supplies
(14, 18); -- Detergent → Cleaning Supplies;

-- Seed item stocks
INSERT INTO item_stocks (_item_stock_id, _item_id, _packaging_id, quantity, minimum_quantity, srp_php, price_php) VALUES
(1, 1, 2, 30, 10, 250.00, 200.00),   -- Hammer / Piece
(2, 2, 4, 4, 5, 600.00, 480.00),    -- Screwdriver Set / Set
(3, 3, 3, 74, 20, 120.00, 100.00),  -- PVC Pipe / Pack
(4, 4, 2, 115, 30, 80.00, 60.00);
INSERT INTO item_stocks (_item_stock_id, _item_id, _packaging_id, quantity, minimum_quantity, srp_php, price_php) VALUES
(5, 5, 8, 28, 10, 1400.00, 1200.00),  -- Latex Paint / Can
(6, 6, 4, 22, 5, 550.00, 450.00),    -- Paint Roller Set / Set
(7, 7, 2, 30, 10, 950.00, 800.00),   -- Garden Shovel / Piece
(8, 8, 2, 47, 15, 600.00, 500.00),   -- Pruning Shears / Piece
(9, 9, 2, 10, 5, 4000.00, 3500.00),  -- Car Jack / Piece
(10, 10, 6, 57, 15, 450.00, 380.00),   -- Motor Oil / Bottle
(11, 11, 1, 98, 30, 300.00, 250.00),  -- Screws Assortment / Box
(12, 12, 2, 74, 20, 250.00, 200.00),   -- Safety Goggles / Piece
(13, 13, 7, 147, 40, 80.00, 60.00),    -- Super Glue / Tube
(14, 14, 2, 19, 5, 4800.00, 4200.00),  -- Power Drill / Piece
(15, 15, 2, 25, 10, 1100.00, 900.00),  -- LED Floodlight / Piece
(16, 16, 2, 14, 5, 2000.00, 1800.00),  -- Aluminum Ladder / Piece
(17, 17, 3, 13, 10, 360.00, 300.00),   -- Cleaning Rags / Pack
(18, 18, 6, 68, 20, 180.00, 150.00);   -- Detergent / Bottle;    -- Light Bulb / Piece
INSERT INTO item_stocks (_item_stock_id, _item_id, _packaging_id, quantity, minimum_quantity, srp_php, price_php) VALUES
(19, 5, 1, 20, 5, 2500.00, 2320.45),  -- Latex Paint / Box
(20, 6, 1, 15, 3, 2450.00, 2325.75);    -- Paint Roller Set / Box

INSERT INTO item_restocks (_item_restock_id, _item_stock_id, quantity_before, quantity_after, quantity_added, _created_at)
VALUES
(1, 1, 0, 50, 50, '2018-01-01 00:00:00'),
(2, 2, 0, 20, 20, '2018-01-01 00:00:00'),
(3, 3, 0, 100, 100, '2018-01-01 00:00:00'),
(4, 4, 0, 150, 150, '2018-01-01 00:00:00');
INSERT INTO item_restocks (_item_restock_id, _item_stock_id, quantity_before, quantity_after, quantity_added, _created_at)
VALUES
(5, 5, 0, 30, 30, '2018-01-01 00:00:00'),
(6, 6, 0, 25, 25, '2018-01-01 00:00:00'),
(7, 7, 0, 40, 40, '2018-01-01 00:00:00'),
(8, 8, 0, 50, 50, '2018-01-01 00:00:00'),
(9, 9, 0, 10, 10, '2018-01-01 00:00:00'),
(10, 10, 0, 60, 60, '2018-01-01 00:00:00'),
(11, 11, 0, 100, 100, '2018-01-01 00:00:00'),
(12, 12, 0, 75, 75, '2018-01-01 00:00:00'),
(13, 13, 0, 150, 150, '2018-01-01 00:00:00'),
(14, 14, 0, 20, 20, '2018-01-01 00:00:00'),
(15, 15, 0, 35, 35, '2018-01-01 00:00:00'),
(16, 16, 0, 15, 15, '2018-01-01 00:00:00'),
(17, 17, 0, 40, 40, '2018-01-01 00:00:00'),
(18, 18, 0, 80, 80, '2018-01-01 00:00:00');
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
(4, 3, 4, 80.00, 55.00);
INSERT INTO suppliers_items (_supplier_item_id, _supplier_id, _item_id, srp_php, wsp_php) VALUES
(5, 3, 5, 1400.00, 1100.00),  -- BrightLite sells Latex Paint
(6, 1, 6, 550.00, 420.00),    -- ToolPro sells Paint Roller Set
(7, 1, 7, 950.00, 700.00),    -- ToolPro sells Garden Shovel
(8, 1, 8, 600.00, 450.00),    -- ToolPro sells Pruning Shears
(9, 1, 9, 4000.00, 3000.00),  -- ToolPro sells Car Jack
(10, 2, 10, 450.00, 350.00),   -- PipeMasters sells Motor Oil
(11, 1, 11, 300.00, 220.00),   -- ToolPro sells Screws Assortment
(12, 3, 12, 250.00, 180.00),   -- BrightLite sells Safety Goggles
(13, 3, 13, 80.00, 60.00),     -- BrightLite sells Super Glue
(14, 1, 14, 4800.00, 3600.00), -- ToolPro sells Power Drill
(15, 3, 15, 1100.00, 800.00),  -- BrightLite sells LED Floodlight
(16, 1, 16, 2000.00, 1600.00), -- ToolPro sells Aluminum Ladder
(17, 2, 17, 360.00, 270.00),   -- PipeMasters sells Cleaning Rags
(18, 2, 18, 180.00, 130.00);   -- PipeMasters sells Detergent;   -- BrightLite sells Light Bulb

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

INSERT INTO customer_orders (_created_at, _customer_order_id, _customer_order_number, _employee_id, discount_type, discount_value)
VALUES ('2018-01-01 00:00:00', 1, UUID(), 3, 'percent', 20.00);
INSERT INTO customer_orders (_created_at, _customer_order_id, _customer_order_number, _employee_id, discount_type, discount_value)
VALUES ('2018-02-01 00:00:00', 2, UUID(), 3, 'fixed', 200.00);
INSERT INTO customer_orders (_created_at, _customer_order_id, _customer_order_number, _employee_id, discount_type, discount_value)
VALUES ('2024-02-01 00:00:00', 3, UUID(), 3, 'none', 0.00);
INSERT INTO customer_orders (_created_at, _customer_order_id, _customer_order_number, _employee_id, discount_type, discount_value)
VALUES ('2024-03-02 00:00:00', 4, UUID(), 3, 'none', 0.00);
INSERT INTO customer_orders (_created_at, _customer_order_id, _customer_order_number, _employee_id, discount_type, discount_value)
VALUES ('2024-01-04 00:00:00', 5, UUID(), 3, 'none', 0.00);
INSERT INTO customer_orders (_created_at, _customer_order_id, _customer_order_number, _employee_id, discount_type, discount_value)
VALUES ('2025-01-06 00:00:00', 6, UUID(), 3, 'none', 0.00);
INSERT INTO customer_orders (_created_at, _customer_order_id, _customer_order_number, _employee_id, discount_type, discount_value)
VALUES ('2025-06-11 00:00:00', 7, UUID(), 3, 'none', 0.00);
INSERT INTO customer_orders (_created_at, _customer_order_id, _customer_order_number, _employee_id, discount_type, discount_value)
VALUES ('2021-05-01 00:00:00', 8, UUID(), 3, 'percent', 20.00);
INSERT INTO customer_orders (_created_at, _customer_order_id, _customer_order_number, _employee_id, discount_type, discount_value)
VALUES ('2021-04-01 00:00:00', 9, UUID(), 3, 'none', 0.00);
INSERT INTO customer_orders (_created_at, _customer_order_id, _customer_order_number, _employee_id, discount_type, discount_value)
VALUES ('2020-07-01 00:00:00', 10, UUID(), 3, 'none', 0.00);
INSERT INTO customer_orders (_created_at, _customer_order_id, _customer_order_number, _employee_id, discount_type, discount_value)
VALUES ('2020-10-12 00:00:00', 11, UUID(), 3, 'fixed', 120.00);
INSERT INTO customer_orders (_created_at, _customer_order_id, _customer_order_number, _employee_id, discount_type, discount_value)
VALUES ('2020-02-04 00:00:00', 12, UUID(), 3, 'none', 0.00);
INSERT INTO customer_orders (_created_at, _customer_order_id, _customer_order_number, _employee_id, discount_type, discount_value)
VALUES ('2019-11-13 00:00:00', 13, UUID(), 3, 'percent', 10.00);
INSERT INTO customer_orders (_created_at, _customer_order_id, _customer_order_number, _employee_id, discount_type, discount_value)
VALUES ('2019-12-25 00:00:00', 14, UUID(), 3, 'percent', 5.00);
INSERT INTO customer_orders (_created_at, _customer_order_id, _customer_order_number, _employee_id, discount_type, discount_value)
VALUES ('2018-12-23 00:00:00', 15, UUID(), 3, 'none', 0.00);
INSERT INTO customer_orders (_created_at, _customer_order_id, _customer_order_number, _employee_id, discount_type, discount_value)
VALUES ('2018-08-24 00:00:00', 16, UUID(), 3, 'percent', 20.00);
INSERT INTO customer_orders (_created_at, _customer_order_id, _customer_order_number, _employee_id, discount_type, discount_value)
VALUES ('2022-07-02 00:00:00', 17, UUID(), 3, 'percent', 20.00);
INSERT INTO customer_orders (_created_at, _customer_order_id, _customer_order_number, _employee_id, discount_type, discount_value)
VALUES ('2022-08-05 00:00:00', 18, UUID(), 3, 'fixed', 20.00);
INSERT INTO customer_orders (_created_at, _customer_order_id, _customer_order_number, _employee_id, discount_type, discount_value)
VALUES ('2022-01-01 00:00:00', 19, UUID(), 3, 'fixed', 200.00);
INSERT INTO customer_orders (_created_at, _customer_order_id, _customer_order_number, _employee_id, discount_type, discount_value)
VALUES ('2022-02-04 00:00:00', 20, UUID(), 3, 'none', 0.00);
INSERT INTO customer_orders (_created_at, _customer_order_id, _customer_order_number, _employee_id, discount_type, discount_value)
VALUES ('2021-06-04 00:00:00', 21, UUID(), 3, 'none', 0.00);
INSERT INTO customer_orders (_created_at, _customer_order_id, _customer_order_number, _employee_id, discount_type, discount_value)
VALUES ('2020-07-17 00:00:00', 22, UUID(), 3, 'none', 0.00);
INSERT INTO customer_orders (_created_at, _customer_order_id, _customer_order_number, _employee_id, discount_type, discount_value)
VALUES ('2019-09-14 00:00:00', 23, UUID(), 3, 'none', 0.00);
INSERT INTO customer_orders (_created_at, _customer_order_id, _customer_order_number, _employee_id, discount_type, discount_value)
VALUES ('2020-08-12 00:00:00', 24, UUID(), 3, 'none', 0.00);
INSERT INTO customer_orders (_created_at, _customer_order_id, _customer_order_number, _employee_id, discount_type, discount_value)
VALUES ('2023-04-23 00:00:00', 25, UUID(), 3, 'none', 0.00);
INSERT INTO customer_orders (_created_at, _customer_order_id, _customer_order_number, _employee_id, discount_type, discount_value)
VALUES ('2020-03-23 00:00:00', 26, UUID(), 3, 'none', 0.00);
INSERT INTO customer_orders (_created_at, _customer_order_id, _customer_order_number, _employee_id, discount_type, discount_value)
VALUES ('2025-04-05 00:00:00', 27, UUID(), 3, 'none', 0.00);
INSERT INTO customer_orders (_created_at, _customer_order_id, _customer_order_number, _employee_id, discount_type, discount_value)
VALUES ('2021-10-23 00:00:00', 28, UUID(), 3, 'none', 0.00);
INSERT INTO customer_orders (_created_at, _customer_order_id, _customer_order_number, _employee_id, discount_type, discount_value)
VALUES ('2019-12-04 00:00:00', 29, UUID(), 3, 'none', 0.00);
INSERT INTO customer_orders (_created_at, _customer_order_id, _customer_order_number, _employee_id, discount_type, discount_value)
VALUES ('2020-11-21 00:00:00', 30, UUID(), 3, 'none', 0.00);
INSERT INTO customer_orders (_created_at, _customer_order_id, _customer_order_number, _employee_id, discount_type, discount_value)
VALUES ('2019-05-11 00:00:00', 31, UUID(), 3, 'none', 0.00);


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
INSERT INTO customer_orders_item_stocks (_customer_order_item_id, _customer_order_id, _item_stock_id, price_php, quantity)
VALUES
(10, 4, 1, 200.0, 2),
(11, 4, 2, 480.0, 2),
(12, 5, 3, 100.00, 4),
(13, 6, 3, 100.00, 1),
(14, 7, 4, 60.00, 1),
(15, 8, 4, 60.00, 2),
(16, 9, 1, 200.0, 1),
(17, 10, 5, 1200.00, 2),
(18, 10, 1, 200.00, 2),
(19, 11, 2, 480.00, 1),
(20, 13, 4, 60.00, 4),
(21, 14, 1, 200.00, 2),
(22, 14, 2, 480.00, 1),
(23, 15, 6, 450.00, 3),
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
(38, 26, 1, 200.00, 2),
(39, 27, 7, 800.00, 10),
(40, 27, 8, 500.00, 3),
(41, 27, 9, 3500.00, 2),
(42, 28, 9, 3500.00, 4),
(43, 28, 10, 380.00, 3),
(44, 28, 11, 250.00, 2),
(45, 28, 12, 200.00, 1),
(46, 28, 13, 60.00, 3),
(47, 29, 14, 4200.00, 1),
(48, 30, 15, 900.0, 10),
(49, 30, 16, 1800.00, 1),
(50, 30, 17, 300.00, 10),
(51, 31, 18, 150.00, 12),
(52, 31, 17, 300.00, 17);

INSERT INTO customer_payments (_customer_payment_id, _customer_order_id, payment_method, amount_php, _created_at)
VALUES
(1, 1, 'cash', 150.00, '2018-01-01 10:00:00'),
(2, 1, 'cash', 150.00, '2018-02-01 10:00:00'),
(3, 2, 'cash', 1000.00, '2018-03-04 10:00:00'),
(4, 3, 'cash', 200.00, '2018-04-05 10:00:00'),
(5, 3, 'cash', 200.00, '2018-05-06 10:00:00'),
(6, 3, 'cash', 100.00, '2018-10-10 10:00:00');
INSERT INTO customer_payments (_customer_payment_id, _customer_order_id, payment_method, amount_php, _created_at)
VALUES
(7, 4, 'cash', 500.00, '2019-01-01 10:00:00'),
(8, 5, 'cash', 400.00, '2019-01-01 10:00:00'),
(9, 6, 'cash', 100.00, '2019-01-21 10:00:00'),
(10, 7, 'cash', 100.00, '2019-02-01 10:00:00'),
(11, 8, 'cash', 150.00, '2019-04-01 09:00:00'),
(12, 9, 'cash', 200.00, '2019-05-01 09:00:00'),
(13, 10, 'cash', 400.00, '2019-06-01 09:00:00'),
(14, 11, 'cash', 600.00, '2019-06-05 09:00:00'),
(15, 12, 'cash', 200.00, '2019-06-10 09:00:00'),
(16, 13, 'cash', 300.00, '2019-06-10 10:00:00'),
(17, 14, 'cash', 300.00, '2019-06-10 11:00:00'),
(18, 15, 'cash', 300.00, '2019-08-15 13:00:00'),
(19, 16, 'cash', 350.00, '2019-08-15 13:00:00'),
(20, 17, 'cash', 300.00, '2019-09-03 08:00:00'),
(21, 18, 'cash', 200.00, '2019-09-04 15:00:00'),
(22, 19, 'cash', 400.00, '2019-09-04 04:00:00'),
(23, 20, 'cash', 200.00, '2020-01-01 12:00:00'),
(24, 21, 'cash', 250.00, '2022-01-01 00:00:00'),
(25, 22, 'cash', 200.00, '2022-01-01 00:00:00'),
(26, 23, 'cash', 150.00, '2022-02-11 09:00:00'),
(27, 24, 'cash', 200.00, '2022-02-14 10:00:00'),
(28, 25, 'cash', 100.00, '2022-02-15 14:00:00'),
(29, 26, 'cash', 400.00, '2022-02-15 14:00:00');
INSERT INTO customer_payments (_customer_payment_id, _customer_order_id, payment_method, amount_php, _created_at)
VALUES
(30, 27, 'cash', 8000.00, '2023-02-15 14:00:00'),  -- 800*10 + 500*3 + 3500*2 = 8000 + 1500 + 7000 = 16,500 → partial payment
(31, 27, 'cash', 5000.00, '2023-02-15 14:00:00'),  -- additional payment → total = 13,000
(32, 28, 'cash', 12000.00, '2023-02-15 14:00:00'), -- 3500*4 + 380*3 + 250*2 + 200 + 60*3 = 14000 + 1140 + 500 + 200 + 180 = 16,020 → partial
(33, 28, 'cash', 4000.00, '2024-02-15 14:00:00'),  -- more to cover balance
(34, 29, 'cash', 4200.00, '2024-02-15 14:00:00'),  -- 4200*1 = exact
(35, 30, 'cash', 12000.00, '2025-02-15 14:00:00'), -- 900*10 + 1800 + 300*10 = 9000 + 1800 + 3000 = 13,800 → partial
(36, 30, 'cash', 3800.00, '2025-02-15 14:00:00'),  -- cover remaining
(37, 31, 'cash', 1800.00, '2025-02-15 14:00:00');  -- 150*12 = 1800

-- Seed additional suppliers
INSERT INTO suppliers (_supplier_id, name, email, street, city, state, postal_code, country) VALUES
(4, 'BuildRight Solutions', 'sales@buildright.ph', '88 Construction Hub', 'Pasig', 'Metro Manila', '1600', 'Philippines'),
(5, 'EcoGarden Supplies', 'contact@ecogarden.com', '1 Green Thumb Lane', 'Antipolo', 'Rizal', '1870', 'Philippines'),
(6, 'PowerMax Tools', 'orders@powermax.com', '25 Industrial Park', 'Caloocan', 'Metro Manila', '1400', 'Philippines');

-- Seed additional suppliers_items (linking new and existing items to new and existing suppliers)
-- Assuming supplier_item_id continues from 19
INSERT INTO suppliers_items (_supplier_item_id, _supplier_id, _item_id, srp_php, wsp_php) VALUES
-- BuildRight Solutions (Supplier 4)
(19, 4, 1, 255.00, 185.00),  -- Hammer (also by ToolPro)
(20, 4, 3, 125.00, 95.00),   -- PVC Pipe (also by PipeMasters)
(21, 4, 11, 310.00, 225.00), -- Screws Assortment (also by ToolPro)
(22, 4, 16, 2050.00, 1650.00),-- Aluminum Ladder (also by ToolPro)

-- EcoGarden Supplies (Supplier 5)
(23, 5, 7, 930.00, 720.00),  -- Garden Shovel (competitive with ToolPro)
(24, 5, 8, 590.00, 460.00),  -- Pruning Shears (competitive with ToolPro)
(25, 5, 17, 350.00, 260.00), -- Cleaning Rags (competitive with PipeMasters)

-- PowerMax Tools (Supplier 6)
(26, 6, 14, 4700.00, 3500.00),-- Power Drill (competitive with ToolPro)
(27, 6, 2, 580.00, 440.00),  -- Screwdriver Set (competitive with ToolPro)
(28, 6, 9, 3900.00, 2950.00),  -- Car Jack (competitive with ToolPro)

-- Additional items for existing suppliers for more order variety
(29, 1, 5, 1450.00, 1150.00), -- ToolPro now also sells Latex Paint
(30, 2, 12, 260.00, 190.00), -- PipeMasters now also sells Safety Goggles
(31, 3, 10, 460.00, 360.00); -- BrightLite now also sells Motor Oil

-- Continue Seed for orders (start _order_id from 4)
-- Peter (employee_id 3: clerk, purchasing officer)
-- Kurt (employee_id 4: purchasing officer)
-- Jerick (employee_id 5: logistics - can also place orders)

INSERT INTO orders (_order_id, _order_number, _employee_id, _supplier_id, discount_type, discount_value_php, status, remarks, _created_at)
VALUES
-- Order from BuildRight Solutions (Supplier 4) by Kurt
(4, UUID(), 4, 4, 'fixed', 150.00, 'audited', 'Stock replenishment for common hardware.', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 10 DAY)),
-- Order from EcoGarden Supplies (Supplier 5) by Peter
(5, UUID(), 3, 5, 'percent', 5.00, 'acknowledged', 'Gardening tools for spring season.', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 8 DAY)),
-- Order from PowerMax Tools (Supplier 6) by Jerick
(6, UUID(), 5, 6, 'none', 0.00, 'pending', 'Urgent need for power drills.', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 2 DAY)),
-- Another order from ToolPro (Supplier 1), different items, by Kurt
(7, UUID(), 4, 1, 'percent', 7.50, 'audited', 'Paints and automotive restock.', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 15 DAY)),
-- Another order from PipeMasters (Supplier 2), different items, by Peter
(8, UUID(), 3, 2, 'fixed', 100.00, 'acknowledged', 'Safety Goggles and cleaning supplies bulk.', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 5 DAY)),
-- Another order from BrightLite (Supplier 3), different items, by Jerick
(9, UUID(), 5, 3, 'none', 0.00, 'pending', 'Motor oil and additional lighting.', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 1 DAY));

-- Continue Seed for items_orders (start _item_order_id from 5)

-- Order 4: BuildRight Solutions (Supplier 4) by Kurt
INSERT INTO items_orders (_item_order_id, _order_id, _item_id, wsp_php, quantity) VALUES
(5, 4, 1, 185.00, 20),  -- Hammer from BuildRight
(6, 4, 11, 225.00, 10), -- Screws Assortment from BuildRight
(7, 4, 16, 1650.00, 5);  -- Aluminum Ladder from BuildRight

-- Order 5: EcoGarden Supplies (Supplier 5) by Peter
INSERT INTO items_orders (_item_order_id, _order_id, _item_id, wsp_php, quantity) VALUES
(8, 5, 7, 720.00, 15),   -- Garden Shovel from EcoGarden
(9, 5, 8, 460.00, 25),   -- Pruning Shears from EcoGarden
(10, 5, 17, 260.00, 30); -- Cleaning Rags from EcoGarden

-- Order 6: PowerMax Tools (Supplier 6) by Jerick
INSERT INTO items_orders (_item_order_id, _order_id, _item_id, wsp_php, quantity) VALUES
(11, 6, 14, 3500.00, 8), -- Power Drill from PowerMax
(12, 6, 9, 2950.00, 3);  -- Car Jack from PowerMax

-- Order 7: ToolPro (Supplier 1) by Kurt - new items for ToolPro
INSERT INTO items_orders (_item_order_id, _order_id, _item_id, wsp_php, quantity) VALUES
(13, 7, 5, 1150.00, 10), -- Latex Paint from ToolPro (newly added to their catalog)
(14, 7, 9, 3000.00, 2),  -- Car Jack from ToolPro
(15, 7, 7, 700.00, 12);  -- Garden Shovel from ToolPro

-- Order 8: PipeMasters (Supplier 2) by Peter - new items for PipeMasters
INSERT INTO items_orders (_item_order_id, _order_id, _item_id, wsp_php, quantity) VALUES
(16, 8, 12, 190.00, 50), -- Safety Goggles from PipeMasters (newly added)
(17, 8, 18, 130.00, 40), -- Detergent from PipeMasters
(18, 8, 10, 350.00, 20); -- Motor Oil from PipeMasters

-- Order 9: BrightLite (Supplier 3) by Jerick - new items for BrightLite
INSERT INTO items_orders (_item_order_id, _order_id, _item_id, wsp_php, quantity) VALUES
(19, 9, 10, 360.00, 15), -- Motor Oil from BrightLite (newly added)
(20, 9, 15, 800.00, 25), -- LED Floodlight from BrightLite
(21, 9, 13, 60.00, 100); -- Super Glue from BrightLite

-- Additional item_restocks spanning 2019-2023, ~10 per year with varying months

INSERT INTO item_restocks (_item_restock_id, _item_stock_id, quantity_before, quantity_after, quantity_added, _created_at) VALUES
-- 2019
(19, 1, 50, 80, 30, '2019-02-15 10:23:00'),
(20, 2, 20, 40, 20, '2019-03-20 15:45:00'),
(21, 3, 100, 130, 30, '2019-05-10 09:10:00'),
(22, 4, 150, 170, 20, '2019-07-22 11:30:00'),
(23, 5, 30, 55, 25, '2019-09-01 14:00:00'),
(24, 6, 25, 45, 20, '2019-10-12 16:50:00'),
(25, 7, 40, 60, 20, '2019-11-05 08:25:00'),
(26, 8, 50, 70, 20, '2019-12-19 17:00:00'),
(27, 9, 10, 25, 15, '2019-04-07 12:10:00'),
(28, 10, 60, 90, 30, '2019-06-15 10:40:00'),

-- 2020
(29, 11, 100, 130, 30, '2020-01-25 09:30:00'),
(30, 12, 75, 95, 20, '2020-02-14 13:15:00'),
(31, 13, 150, 180, 30, '2020-03-28 14:50:00'),
(32, 14, 20, 35, 15, '2020-04-20 08:00:00'),
(33, 15, 35, 50, 15, '2020-05-30 15:20:00'),
(34, 16, 15, 30, 15, '2020-06-25 12:45:00'),
(35, 17, 40, 60, 20, '2020-07-15 10:00:00'),
(36, 18, 80, 100, 20, '2020-08-18 09:30:00'),
(37, 5, 55, 85, 30, '2020-09-05 16:45:00'),
(38, 6, 45, 65, 20, '2020-10-21 11:15:00'),

-- 2021
(39, 7, 60, 85, 25, '2021-01-12 13:05:00'),
(40, 8, 70, 95, 25, '2021-02-17 10:25:00'),
(41, 9, 25, 40, 15, '2021-03-23 14:40:00'),
(42, 10, 90, 120, 30, '2021-04-30 09:55:00'),
(43, 11, 130, 155, 25, '2021-05-18 16:30:00'),
(44, 12, 95, 120, 25, '2021-06-27 11:20:00'),
(45, 13, 180, 210, 30, '2021-07-29 14:15:00'),
(46, 14, 35, 50, 15, '2021-08-22 12:00:00'),
(47, 15, 50, 70, 20, '2021-09-30 09:40:00'),
(48, 16, 30, 50, 20, '2021-10-14 10:10:00'),

-- 2022
(49, 17, 60, 80, 20, '2022-01-10 15:45:00'),
(50, 18, 100, 130, 30, '2022-02-19 10:05:00'),
(51, 5, 85, 110, 25, '2022-03-28 09:35:00'),
(52, 6, 65, 90, 25, '2022-04-30 13:20:00'),
(53, 7, 85, 115, 30, '2022-05-21 08:55:00'),
(54, 8, 95, 120, 25, '2022-06-30 17:15:00'),
(55, 9, 40, 60, 20, '2022-07-15 14:30:00'),
(56, 10, 120, 150, 30, '2022-08-23 16:00:00'),
(57, 11, 155, 185, 30, '2022-09-07 10:10:00'),
(58, 12, 120, 140, 20, '2022-10-12 11:50:00'),

-- 2023
(59, 13, 210, 240, 30, '2023-01-11 12:00:00'),
(60, 14, 50, 65, 15, '2023-02-15 15:30:00'),
(61, 15, 70, 90, 20, '2023-03-20 09:45:00'),
(62, 16, 50, 75, 25, '2023-04-28 13:10:00'),
(63, 17, 80, 105, 25, '2023-05-14 10:30:00'),
(64, 18, 130, 160, 30, '2023-06-18 11:40:00'),
(65, 5, 110, 140, 30, '2023-07-22 14:20:00'),
(66, 6, 90, 115, 25, '2023-08-25 16:45:00'),
(67, 7, 115, 140, 25, '2023-09-10 12:15:00'),
(68, 8, 120, 145, 25, '2023-10-05 09:05:00'),

(69, 13, 210, 240, 30, '2023-03-11 12:00:00'),
(70, 14, 50, 65, 15, '2023-04-16 15:30:00'),
(71, 15, 70, 90, 20, '2023-06-10 09:45:00'),
(72, 16, 50, 75, 25, '2023-10-08 13:10:00'),
(73, 17, 80, 105, 25, '2023-12-14 10:30:00'),
(74, 18, 130, 160, 30, '2023-05-28 11:40:00'),
(75, 5, 110, 140, 30, '2023-05-23 14:20:00'),
(76, 6, 90, 115, 25, '2023-11-18 16:45:00'),
(77, 7, 115, 140, 25, '2023-11-18 12:15:00'),
(78, 8, 120, 145, 25, '2023-04-15 09:05:00'),

-- 2024
(79, 7, 60, 85, 25, '2024-05-12 13:05:00'),
(80, 8, 70, 95, 25, '2024-08-17 10:25:00'),
(81, 9, 25, 40, 15, '2024-08-23 14:40:00'),
(82, 10, 90, 120, 30, '2024-04-30 09:55:00'),
(83, 11, 130, 155, 25, '2024-05-18 16:30:00'),
(84, 12, 95, 120, 25, '2024-02-27 11:20:00'),
(85, 13, 180, 210, 30, '2024-10-29 14:15:00'),
(86, 14, 35, 50, 15, '2024-09-22 12:00:00'),
(87, 15, 50, 70, 20, '2024-02-30 09:40:00'),
(88, 16, 30, 50, 20, '2024-06-01 10:10:00');

-- Additional customer_orders spanning 2019-2023, ~10 per year with varying months

INSERT INTO customer_orders (_created_at, _customer_order_id, _customer_order_number, _employee_id, discount_type, discount_value) VALUES
-- 2019
('2019-04-18 13:45:00', 32, UUID(), 3, 'none', 0.00),
('2019-05-22 16:50:00', 33, UUID(), 3, 'percent', 10.00),
('2019-06-10 11:20:00', 34, UUID(), 3, 'fixed', 50.00),
('2019-07-14 12:30:00', 35, UUID(), 3, 'none', 0.00),
('2019-08-05 15:00:00', 36, UUID(), 3, 'percent', 5.00),
('2019-09-17 09:10:00', 37, UUID(), 3, 'none', 0.00),
('2019-10-23 14:25:00', 38, UUID(), 3, 'fixed', 150.00),

-- 2020
('2020-01-30 10:00:00', 39, UUID(), 3, 'none', 0.00),
('2020-02-28 13:40:00', 40, UUID(), 3, 'percent', 20.00),
('2020-03-19 09:30:00', 41, UUID(), 3, 'none', 0.00),
('2020-04-14 15:25:00', 42, UUID(), 3, 'fixed', 75.00),
('2020-05-22 10:45:00', 43, UUID(), 3, 'percent', 10.00),
('2020-06-30 16:15:00', 44, UUID(), 3, 'none', 0.00),
('2020-07-13 11:50:00', 45, UUID(), 3, 'percent', 25.00),
('2020-08-08 14:10:00', 46, UUID(), 3, 'fixed', 125.00),
('2020-09-15 09:20:00', 47, UUID(), 3, 'none', 0.00),
('2020-10-29 13:30:00', 48, UUID(), 3, 'percent', 30.00),

-- 2021
('2021-01-07 12:00:00', 49, UUID(), 3, 'none', 0.00),
('2021-02-18 10:35:00', 50, UUID(), 3, 'percent', 15.00),
('2021-03-23 16:40:00', 51, UUID(), 3, 'fixed', 80.00),
('2021-04-28 11:25:00', 52, UUID(), 3, 'none', 0.00),
('2021-05-15 14:50:00', 53, UUID(), 3, 'percent', 5.00),
('2021-06-19 09:55:00', 54, UUID(), 3, 'none', 0.00),
('2021-07-24 13:15:00', 55, UUID(), 3, 'fixed', 100.00),
('2021-08-30 16:20:00', 56, UUID(), 3, 'percent', 20.00),
('2021-09-10 10:10:00', 57, UUID(), 3, 'none', 0.00),
('2021-10-26 15:40:00', 58, UUID(), 3, 'fixed', 60.00),

-- 2022
('2022-01-12 09:25:00', 59, UUID(), 3, 'none', 0.00),
('2022-02-21 14:05:00', 60, UUID(), 3, 'percent', 10.00),
('2022-03-17 11:50:00', 61, UUID(), 3, 'fixed', 90.00),
('2022-04-29 16:30:00', 62, UUID(), 3, 'none', 0.00),
('2022-05-23 13:10:00', 63, UUID(), 3, 'percent', 15.00),
('2022-06-28 10:00:00', 64, UUID(), 3, 'none', 0.00),
('2022-07-19 12:40:00', 65, UUID(), 3, 'fixed', 110.00),
('2022-08-24 09:35:00', 66, UUID(), 3, 'percent', 20.00),
('2022-09-13 14:20:00', 67, UUID(), 3, 'none', 0.00),
('2022-10-30 16:45:00', 68, UUID(), 3, 'fixed', 70.00),

-- 2023
('2023-01-08 10:10:00', 69, UUID(), 3, 'none', 0.00),
('2023-02-22 15:00:00', 70, UUID(), 3, 'percent', 25.00),
('2023-03-26 09:45:00', 71, UUID(), 3, 'none', 0.00),
('2023-04-30 14:15:00', 72, UUID(), 3, 'fixed', 80.00),
('2023-05-20 11:05:00', 73, UUID(), 3, 'percent', 10.00),
('2023-06-25 16:50:00', 74, UUID(), 3, 'none', 0.00),
('2023-07-30 12:30:00', 75, UUID(), 3, 'percent', 30.00),
('2023-08-19 09:40:00', 76, UUID(), 3, 'fixed', 100.00),
('2023-09-24 13:00:00', 77, UUID(), 3, 'none', 0.00),
('2023-10-29 15:55:00', 78, UUID(), 3, 'percent', 20.00),

('2024-01-08 10:10:00', 79, UUID(), 3, 'none', 0.00),
('2024-02-22 15:00:00', 80, UUID(), 3, 'percent', 25.00),
('2024-03-26 09:45:00', 81, UUID(), 3, 'none', 0.00),
('2024-04-30 14:15:00', 82, UUID(), 3, 'fixed', 80.00),
('2024-05-20 11:05:00', 83, UUID(), 3, 'percent', 10.00),
('2024-06-25 16:50:00', 84, UUID(), 3, 'none', 0.00),
('2024-07-30 12:30:00', 85, UUID(), 3, 'percent', 30.00),
('2024-08-19 09:40:00', 86, UUID(), 3, 'fixed', 100.00),
('2024-09-24 13:00:00', 87, UUID(), 3, 'none', 0.00),
('2024-10-29 15:55:00', 88, UUID(), 3, 'percent', 20.00),

('2025-01-08 10:10:00', 89, UUID(), 3, 'none', 0.00),
('2025-02-22 15:00:00', 90, UUID(), 3, 'percent', 25.00),
('2025-03-26 09:45:00', 91, UUID(), 3, 'none', 0.00),
('2025-04-30 14:15:00', 92, UUID(), 3, 'fixed', 80.00),
('2025-05-20 11:05:00', 93, UUID(), 3, 'percent', 10.00);

INSERT INTO customer_orders_item_stocks (
  _customer_order_item_id, _customer_order_id, _item_stock_id, quantity, price_php
) VALUES
-- 2019
(53, 32, 1, 2, 100.00),
(54, 32, 2, 1, 150.00),
(55, 33, 3, 3, 80.00),
(56, 34, 4, 1, 120.00),
(57, 35, 5, 5, 60.00),
(58, 36, 6, 2, 45.00),
(59, 37, 7, 1, 90.00),
(60, 38, 8, 4, 110.00),

-- 2020
(61, 39, 9, 2, 50.00),
(62, 40, 10, 1, 100.00),
(63, 41, 11, 3, 200.00),
(64, 42, 12, 1, 150.00),
(65, 43, 13, 2, 90.00),
(66, 44, 14, 2, 85.00),
(67, 45, 15, 1, 70.00),
(68, 46, 16, 3, 65.00),
(69, 47, 17, 2, 95.00),
(70, 48, 18, 1, 130.00),

-- 2021
(71, 49, 5, 2, 60.00),
(72, 50, 6, 1, 45.00),
(73, 51, 7, 3, 90.00),
(74, 52, 8, 2, 110.00),
(75, 53, 9, 4, 50.00),
(76, 54, 10, 1, 100.00),
(77, 55, 11, 2, 200.00),
(78, 56, 12, 3, 150.00),
(79, 57, 13, 1, 90.00),
(80, 58, 5, 2, 60.00),

-- 2022
(81, 59, 6, 1, 45.00),
(82, 60, 7, 3, 90.00),
(83, 61, 8, 2, 110.00),
(84, 62, 9, 4, 50.00),
(85, 63, 10, 1, 100.00),
(86, 64, 11, 2, 200.00),
(87, 65, 12, 3, 150.00),
(88, 66, 13, 1, 90.00),
(89, 67, 13, 1, 90.00),
(90, 68, 5, 2, 60.00),

-- 2023
(91, 69, 9, 2, 50.00),
(92, 70, 10, 1, 100.00),
(93, 71, 11, 3, 200.00),
(94, 72, 12, 1, 150.00),
(95, 73, 13, 2, 90.00),
(96, 74, 14, 2, 85.00),
(97, 75, 15, 1, 70.00),
(98, 76, 16, 3, 65.00),
(99, 77, 17, 2, 95.00),
(90, 78, 18, 1, 130.00),

-- 2024
(91, 79, 9, 2, 50.00),
(92, 80, 10, 1, 100.00),
(93, 81, 11, 3, 200.00),
(94, 82, 12, 1, 150.00),
(95, 83, 13, 2, 90.00),
(96, 84, 14, 2, 85.00),
(97, 85, 15, 1, 70.00),
(98, 86, 16, 3, 65.00),
(99, 87, 17, 2, 95.00),
(100, 88, 18, 1, 130.00),

--2025
(101, 89, 9, 2, 50.00),
(102, 90, 10, 1, 100.00),
(103, 91, 11, 3, 200.00),
(104, 92, 12, 1, 150.00);

UPDATE item_stocks
SET quantity = COALESCE((
      SELECT SUM(quantity_added)
      FROM item_restocks r
      WHERE r._item_stock_id = item_stocks._item_stock_id
    ), 0)
  - COALESCE((
      SELECT SUM(quantity)
      FROM customer_orders_item_stocks c
      WHERE c._item_stock_id = item_stocks._item_stock_id
    ), 0);