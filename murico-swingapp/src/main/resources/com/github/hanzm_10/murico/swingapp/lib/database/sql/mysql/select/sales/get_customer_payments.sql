SELECT 
  _customer_order_id,
  MAX(_created_at) AS _created_at,
  SUM(amount_php) AS amount_php,
  MAX(_customer_payment_id) AS _customer_payment_id,
  MAX(payment_method) AS payment_method
FROM customer_payments
GROUP BY _customer_order_id
ORDER BY _created_at DESC;
