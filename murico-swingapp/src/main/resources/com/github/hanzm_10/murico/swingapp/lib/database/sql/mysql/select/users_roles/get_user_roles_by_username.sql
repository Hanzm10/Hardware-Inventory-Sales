SELECT 
	r.name
FROM 
	roles AS r
JOIN 
	users_roles AS ur
	ON
	ur._role_id = r._role_id
JOIN
	users AS u
	ON
	u._user_id = ur._user_id
WHERE
	u.display_name = ?;