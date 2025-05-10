CREATE VIEW user_metadata
AS
	SELECT
		u._user_id,
	    _created_at,
	    updated_at,
		display_name,
	    display_image,
	    gender,
		first_name,
	    last_name,
	    biography,
	    
	    GROUP_CONCAT(r.name) AS roles,
	    email,
	    verification_status,
	    verified_at
	
	FROM users u
	LEFT JOIN (
		SELECT ur._user_id, ur._role_id, name
	    FROM users_roles ur
	    
	    LEFT JOIN (
			SELECT _role_id, name
	        FROM roles
	        
	        GROUP BY _role_id, name
	    ) r ON ur._role_id = r._role_id
	    
	    GROUP BY ur._user_id, ur._role_id, name
	) r ON u._user_id = r._user_id
	LEFT JOIN (
		SELECT _user_id, email, verification_status, verified_at
	    FROM accounts
	    
	    GROUP BY _user_id, email, verification_status, verified_at
	) a ON u._user_id = a._user_id
	
	GROUP BY u._user_id, email, verification_status, verified_at;

CREATE VIEW sales
AS
  SELECT co._customer_order_id,
         co._customer_id,
         co._employee_id,
         co._customer_order_number,
         co._created_at,
         co.discount_type,
         co.discount_value,
         co.vat_percent,
         co.remarks,
         -- ──────────────── totals pulled from pre-aggregated sub-queries ────────────────
         COALESCE(oit.total_gross, 0.00)                                    AS
            total_gross,
         COALESCE(pt .total_paid, 0.00)                                     AS
            total_paid,
         -- ──────────────── discount calculations ────────────────
         CASE
           WHEN co.discount_type = 'fixed' THEN Round(co.discount_value, 2)
           WHEN co.discount_type = 'percent' THEN Round(
           oit.total_gross * co.discount_value / 100, 2)
           ELSE 0.00
         END                                                                AS
            discount_amount,
         -- net of discount
         Round(oit.total_gross - CASE
                                   WHEN co.discount_type = 'fixed' THEN
                                   co.discount_value
                                   WHEN co.discount_type = 'percent' THEN (
                                   oit.total_gross * co.discount_value / 100 )
                                   ELSE 0.00
                                 END, 2)                                    AS
            net_of_discount,
         -- VAT on the net amount
         Round(( oit.total_gross - CASE
                                     WHEN co.discount_type = 'fixed' THEN
                                     co.discount_value
                                     WHEN co.discount_type = 'percent' THEN (
                                     oit.total_gross * co.discount_value / 100 )
                                     ELSE 0.00
                                   END ) * co.vat_percent / 100, 2)         AS
            vat_amount,
         -- total due (net + VAT)
         Round(( oit.total_gross - CASE
                                     WHEN co.discount_type = 'fixed' THEN
                                     co.discount_value
                                     WHEN co.discount_type = 'percent' THEN (
                                     oit.total_gross * co.discount_value / 100 )
                                     ELSE 0.00
                                   END ) * ( 1 + co.vat_percent / 100 ), 2) AS
            total_due,
         -- remaining balance
         Round(( ( oit.total_gross - CASE
                                       WHEN co.discount_type = 'fixed' THEN
                                       co.discount_value
                                       WHEN co.discount_type = 'percent' THEN (
                                       oit.total_gross * co.discount_value / 100
                                       )
                                       ELSE 0.00
                                     END ) * ( 1 + co.vat_percent / 100 ) ) -
               COALESCE(pt.total_paid, 0.00), 2)                            AS
            balance_due
  FROM   customer_orders AS co
         -- ① pre-summed item-stock totals (avoids duplication when payments are joined)
         LEFT JOIN (SELECT _customer_order_id,
                           Sum(price_php * quantity) AS total_gross
                    FROM   customer_orders_item_stocks
                    GROUP  BY _customer_order_id) AS oit
                ON oit._customer_order_id = co._customer_order_id
         -- ② pre-summed payments (one row per order)
         LEFT JOIN (SELECT _customer_order_id,
                           Sum(amount_php) AS total_paid
                    FROM   customer_payments
                    GROUP  BY _customer_order_id) AS pt
                ON pt._customer_order_id = co._customer_order_id; 