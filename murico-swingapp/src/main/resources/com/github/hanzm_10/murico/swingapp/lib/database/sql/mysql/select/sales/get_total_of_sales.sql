SELECT SUM(s.total_paid) AS total_revenue,
	SUM(s.total_gross) AS total_gross_sales,
	SUM(s.total_due) AS total_net_sales,
	SUM(coits.total_items_sold) AS total_items_sold
FROM sales s
LEFT JOIN (
	SELECT _customer_order_id, SUM(quantity) AS total_items_sold
    FROM customer_orders_item_stocks oits

	GROUP BY _customer_order_id
) coits ON coits._customer_order_id = s._customer_order_id LIMIT 1000;
