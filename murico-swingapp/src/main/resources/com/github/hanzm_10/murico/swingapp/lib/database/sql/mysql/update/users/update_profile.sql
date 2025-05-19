UPDATE
	users AS u
	JOIN
	accounts As a
	ON
	a._user_id = u._user_id
SET
	u.first_name = ?,
	u.last_name = ?,
	u.gender = ?
WHERE
	u._user_id = ?;
