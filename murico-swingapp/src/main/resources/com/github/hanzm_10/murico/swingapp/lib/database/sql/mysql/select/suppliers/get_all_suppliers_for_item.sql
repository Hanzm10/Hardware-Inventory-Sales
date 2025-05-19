SELECT s.name AS supplier_name, si.wsp_php
FROM suppliers_items si
JOIN suppliers s ON si._supplier_id = s._supplier_id
WHERE si._item_id = ?;