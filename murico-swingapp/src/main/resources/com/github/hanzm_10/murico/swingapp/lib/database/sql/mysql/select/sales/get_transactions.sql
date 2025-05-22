SELECT transaction_date,
       transaction_type,
       ref_id,
       order_number,
       amount,
       party,
       employee_handler,
       status
FROM   (
       -- Customer Sales (using sales table total)
       SELECT co._created_at            AS transaction_date,
              'Outbound orders'                    AS transaction_type,
              co._customer_order_id     AS ref_id,
              co._customer_order_number AS order_number,-- Show order number
              s.total_due               AS amount,-- Get total from SALES table
              CASE
                WHEN c._customer_id IS NULL THEN 'Walk-in'
                ELSE COALESCE(Trim(c.first_name
                                   || ' '
                                   || c.last_name), c.email, 'Cust#'
                                                             || c._customer_id)
              END                       AS party,
              u.display_name            AS employee_handler,
              'Completed'               AS status
       -- Assume sales are completed? Or add status to sales/customer_orders?
       FROM   customer_orders co
              JOIN users u
                ON co._employee_id = u._user_id
              LEFT JOIN customers c
                     ON co._customer_id = c._customer_id
              LEFT JOIN sales s
                     ON co._customer_order_id = s._customer_order_id
       -- Join Sales table
       UNION ALL
       -- Supplier Purchases (Calculate total from items_orders) - Show when 'delivered'?
       SELECT o._created_at                       AS transaction_date,
              -- Or status_updated_at when delivered?
              'Inbound Orders'                          AS transaction_type,
              o._order_id                         AS ref_id,
              o._order_number                     AS order_number,-- Show PO number
              (SELECT Sum(io.quantity * io.wsp_php)
               FROM   items_orders io
               WHERE  io._order_id = o._order_id) AS amount,-- Calculate total
              s.NAME                              AS party,
              e.display_name                      AS employee_handler,
              -- employee_handler who created PO
              o.status                            AS status
       -- Status from supplier orders
       FROM   orders o
              JOIN suppliers s
                ON o._supplier_id = s._supplier_id
              JOIN users e
                ON o._employee_id = e._user_id
       WHERE  o.status = 'delivered' -- Filter example: Only show delivered POs?
       UNION ALL
       -- Customer Payments
       SELECT cp._created_at          AS transaction_date,
              'Inbound payments'        AS transaction_type,
              cp._customer_payment_id AS ref_id,
              NULL                    AS order_number,
              cp.amount_php           AS amount,
              CASE
                WHEN c._customer_id IS NULL THEN 'Walk-in'
                ELSE COALESCE(Trim(c.first_name
                                   || ' '
                                   || c.last_name), c.email, 'Cust#'
                                                             || c._customer_id)
              END                     AS party,
              NULL                    AS employee_handler,
              -- Payment might not directly link to employee_handler?
              'Completed'             AS status
       FROM   customer_payments cp
              JOIN customer_orders co
                ON cp._customer_order_id = co._customer_order_id
              LEFT JOIN customers c
                     ON co._customer_id = c._customer_id
        UNION ALL
        -- Supplier Payments (Order Payments)
        SELECT op._created_at       AS transaction_date,
               'Outbound payments'     AS transaction_type,
               op._order_payment_id AS ref_id,
               NULL                 AS order_number,
               op.amount_php        AS amount,-- Showing as negative (expense)
               s.NAME               AS party,
               NULL                 AS employee_handler,
               'Completed'          AS status
        FROM   order_payments op
               JOIN orders o
                 ON op._order_id = o._order_id
               JOIN suppliers s
                 ON o._supplier_id = s._supplier_id
       -- UNION ALL for Refunds could be added here if schema supports easy joining
       -- SELECT cr._created_at, 'Refund', cr._refund_id, ..., -cr.refund_amount, ... FROM customer_refunds cr ...
       ) AS combined_transactions
ORDER  BY transaction_date DESC; 