SELECT
  YEAR(_created_at) AS year,
  SUM(total_gross) AS total_gross
FROM sales
WHERE _created_at >= '1990-01-01'
  AND _created_at <= CURDATE()
GROUP BY YEAR(_created_at)
ORDER BY YEAR(_created_at);
