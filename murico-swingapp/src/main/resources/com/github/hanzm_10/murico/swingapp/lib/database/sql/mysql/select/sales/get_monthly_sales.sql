SELECT Monthname(_created_at) AS month,
       Sum(total_gross)       AS total_gross
FROM   sales
WHERE  _created_at >= '1990-01-01'
       AND _created_at <= Curdate()
GROUP  BY Month(_created_at),
          Monthname(_created_at)
ORDER  BY Month(_created_at); 