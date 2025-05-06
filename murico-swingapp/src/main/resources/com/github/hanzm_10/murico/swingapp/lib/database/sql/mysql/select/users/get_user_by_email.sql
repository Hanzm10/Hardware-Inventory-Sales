SELECT * FROM users
WHERE _user_id = (
	SELECT _user_id
	FROM accounts
	WHERE email = ?
);