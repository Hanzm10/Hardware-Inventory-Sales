SELECT 
	u._user_id, 
	u.user_display_name,
	u.user_role, 
	uc.user_email, 
	uc.user_password
FROM 
	users u
JOIN
	user_credentials uc 
	ON
	u._user_id = uc._user_id;