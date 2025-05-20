CREATE VIEW inventory_breakdown AS
SELECT 
    i._item_id,
    i.name AS item_name,
    -- Concatenate distinct category names
    (
        SELECT GROUP_CONCAT(DISTINCT ic.name ORDER BY ic.name SEPARATOR ', ')
        FROM item_categories_items ict
        JOIN item_categories ic ON ic._item_category_id = ict._item_category_id
        WHERE ict._item_id = i._item_id
    ) AS category_type,
    
    -- Sum of items sold, safe from duplication
     COALESCE((
        SELECT SUM(coist.quantity)
        FROM customer_orders_item_stocks coist
        JOIN customer_orders co ON co._customer_order_id = coist._customer_order_id
        WHERE coist._item_stock_id = ist._item_stock_id
          AND co._created_at >= '2025-04-20 00:00:00'
          AND co._created_at < DATE_ADD('2025-05-20 00:00:00', INTERVAL 1 MONTH)
    ), 0) AS amount_of_items_sold,

    -- Sum of items restocked, safe from duplication
   COALESCE((
        SELECT SUM(irst.quantity_added)
        FROM item_restocks irst
        WHERE irst._item_stock_id = ist._item_stock_id
          AND irst._created_at >= '2025-04-20 00:00:00'
          AND irst._created_at < DATE_ADD('2025-05-20 00:00:00', INTERVAL 1 MONTH)
    ), 0) AS amount_of_items_restocked,
    
	ist.quantity + COALESCE((
        SELECT SUM(coist.quantity)
        FROM customer_orders_item_stocks coist
        JOIN customer_orders co ON co._customer_order_id = coist._customer_order_id
        WHERE coist._item_stock_id = ist._item_stock_id
          AND co._created_at >= '2025-04-20 00:00:00'
          AND co._created_at < DATE_ADD('2025-05-20 00:00:00', INTERVAL 1 MONTH)
    ), 0) AS initial_item_quantity,
    
    ist.quantity AS current_item_quantity,
    ist.minimum_quantity,
    p.name AS packaging_name
FROM items i
JOIN item_stocks ist ON ist._item_id = i._item_id AND ist.is_deleted = FALSE
LEFT JOIN packagings p ON p._packaging_id = ist._packaging_id