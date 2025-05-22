SELECT Extract(year FROM co._created_at) AS year,
       ic.NAME                           AS category_name,
       Sum(coits.quantity)               AS total_quantity
FROM   customer_orders_item_stocks coits
       LEFT JOIN customer_orders co
              ON co._customer_order_id = coits._customer_order_id
       LEFT JOIN item_stocks its
              ON its._item_stock_id = coits._item_stock_id
       LEFT JOIN item_categories_items ici
              ON ici._item_id = its._item_id
       LEFT JOIN item_categories ic
              ON ic._item_category_id = ici._item_category_id
GROUP  BY year,
          ic.NAME
ORDER  BY year,
          total_quantity DESC; 