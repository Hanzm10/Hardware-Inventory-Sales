SELECT
    i.name AS item_name,
	i._item_id,
	ic.name AS category_type_name,
	p.name AS packaging_type_name,
	s.name AS supplier_name,
	ist.quantity AS stock_quantity,
	ist.minimum_quantity,
	ist.price_php AS unit_price_php,
	ist._item_stock_id
FROM item_stocks ist
JOIN items i ON ist._item_id = i._item_id
LEFT JOIN packagings p ON ist._packaging_id = p._packaging_id
LEFT JOIN item_categories_items ici ON i._item_id = ici._item_id
LEFT JOIN item_categories ic ON ici._item_category_id = ic._item_category_id
LEFT JOIN suppliers_items si ON i._item_id = si._item_id
LEFT JOIN suppliers s ON si._supplier_id = s._supplier_id
WHERE i.is_deleted = FALSE AND ist.is_deleted = FALSE
ORDER BY i.name