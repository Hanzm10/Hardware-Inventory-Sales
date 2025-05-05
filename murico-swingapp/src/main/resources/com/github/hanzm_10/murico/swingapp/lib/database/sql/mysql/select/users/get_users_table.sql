<<<<<<< HEAD
SELECT 
	u._user_id, 
	u.user_display_name,
	u.user_role, 
	ac.email, 
	ac.user_password
FROM 
	users u
JOIN
	accounts ac 
	ON
	u._user_id = ac._user_id;
=======
SELECT * FROM users;
>>>>>>> branch 'main' of https://github.com/alyastanga/Hardware-Inventory-Sales.git
