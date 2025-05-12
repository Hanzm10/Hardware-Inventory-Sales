SELECT 
	a._user_id AS USER_ID,
    u.display_name AS USERNAME,
	a.email AS EMAIL_ADDRESS,
	r.name AS ROLE,
    a.verification_status AS STATUS
FROM
	accounts AS a
INNER JOIN
	users_roles AS ur
	ON
	a._user_id = ur._user_id
INNER JOIN
	users AS u
    ON
    a._user_id = u._user_id
INNER JOIN
	roles AS r
	ON 
	ur._role_id = r._role_id;
	