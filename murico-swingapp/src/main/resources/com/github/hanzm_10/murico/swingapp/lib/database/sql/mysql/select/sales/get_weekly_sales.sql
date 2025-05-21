SELECT
  DAYNAME(_created_at) AS weekday,
  SUM(total_gross) AS total_gross
FROM sales
WHERE _created_at >= '1990-01-01'
  AND _created_at <= CURDATE()
GROUP BY DAYOFWEEK(_created_at), DAYNAME(_created_at)
ORDER BY DAYOFWEEK(_created_at);
