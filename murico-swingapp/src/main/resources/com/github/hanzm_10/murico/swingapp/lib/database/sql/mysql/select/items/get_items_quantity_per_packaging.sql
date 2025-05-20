SELECT
	i._item_id,
	i.name AS item_name,
	p._packaging_id,
    p.name AS packaging_name,
    ist.quantity AS packaging_quantity
FROM items i
LEFT JOIN item_stocks ist ON ist._item_id = i._item_id
LEFT JOIN packagings p ON p._packaging_id = ist._packaging_id
WHERE ist.is_deleted = FALSE;