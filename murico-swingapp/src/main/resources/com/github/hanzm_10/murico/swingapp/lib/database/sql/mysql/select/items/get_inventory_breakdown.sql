SELECT 
    i._item_id,
    i.name AS item_name,
    (
        SELECT GROUP_CONCAT(DISTINCT ic.name ORDER BY ic.name SEPARATOR ', ')
        FROM item_categories_items ict
        JOIN item_categories ic ON ic._item_category_id = ict._item_category_id
        WHERE ict._item_id = i._item_id
    ) AS category_type,
    
    COALESCE((
        SELECT SUM(coist.quantity)
        FROM customer_orders_item_stocks coist
        JOIN customer_orders co ON co._customer_order_id = coist._customer_order_id
        WHERE coist._item_stock_id = ist._item_stock_id
          AND co._created_at BETWEEN :start_date
          AND :end_date
        GROUP BY coist._item_stock_id
    ), 0) AS amount_of_items_sold,

    COALESCE((
        SELECT SUM(irst.quantity_added)
        FROM item_restocks irst
        WHERE irst._item_stock_id = ist._item_stock_id
          AND irst._created_at BETWEEN :start_date
          AND :end_date
    ), 0) AS amount_of_items_restocked,
    
    ist.quantity + COALESCE((
        SELECT SUM(coist.quantity)
        FROM customer_orders_item_stocks coist
        JOIN customer_orders co ON co._customer_order_id = coist._customer_order_id
        WHERE coist._item_stock_id = ist._item_stock_id
          AND co._created_at BETWEEN :start_date
          AND :end_date
        GROUP BY coist._item_stock_id
    ), 0) AS initial_item_quantity,
    
    COALESCE((
        SELECT SUM(irst.quantity_added)
        FROM item_restocks irst
        WHERE irst._item_stock_id = ist._item_stock_id
          AND irst._created_at BETWEEN :start_date
          AND :end_date
    ), 0)
    - COALESCE((
        SELECT SUM(coist.quantity)
        FROM customer_orders_item_stocks coist
        JOIN customer_orders co ON co._customer_order_id = coist._customer_order_id
        WHERE coist._item_stock_id = ist._item_stock_id
          AND co._created_at BETWEEN :start_date
          AND :end_date
        GROUP BY coist._item_stock_id
    ), 0) AS current_item_quantity,
    
    ist.minimum_quantity,
    p.name AS packaging_name
FROM items i
JOIN item_stocks ist ON ist._item_id = i._item_id AND ist.is_deleted = FALSE
LEFT JOIN packagings p ON p._packaging_id = ist._packaging_id
WHERE i.is_deleted = FALSE;
