SELECT
	SUM(srp_php) AS total_inventory_value,
    SUM(quantity) AS total_items_in_stock,
    COALESCE((
		SELECT COUNT(*)
        FROM item_stocks ist
        WHERE ist.quantity < ist.minimum_quantity
        AND ist.is_deleted = FALSE
    ), 0) AS total_items_below_critical_level,
    (
      SELECT AVG(item_total_quantity)
      FROM (
          SELECT _item_id, SUM(quantity) AS item_total_quantity
          FROM item_stocks
          WHERE is_deleted = FALSE
          GROUP BY _item_id
      ) AS per_item_totals
  ) AS avg_quantity_per_item
FROM item_stocks
WHERE is_deleted = FALSE;