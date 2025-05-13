SELECT 
    EXTRACT(YEAR FROM co._created_at) AS year,
    ic.name AS category_name,
    SUM(coits.quantity) AS total_quantity
FROM murico.customer_orders_item_stocks coits
LEFT JOIN murico.customer_orders co ON co._customer_order_id = coits._customer_order_id
LEFT JOIN murico.item_stocks its ON its._item_stock_id = coits._item_stock_id
LEFT JOIN murico.item_categories_items ici ON ici._item_id = its._item_id
LEFT JOIN murico.item_categories ic ON ic._item_category_id = ici._item_category_id
GROUP BY year, ic.name
ORDER BY year, total_quantity DESC;