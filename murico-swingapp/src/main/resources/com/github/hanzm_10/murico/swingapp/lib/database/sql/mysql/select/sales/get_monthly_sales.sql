SELECT
  MONTHNAME(_created_at) AS month,
  SUM(total_gross) AS total_gross
FROM sales
WHERE _created_at >= '1990-01-01'
  AND _created_at <= CURDATE()
GROUP BY MONTH(_created_at), MONTHNAME(_created_at)
ORDER BY MONTH(_created_at);
