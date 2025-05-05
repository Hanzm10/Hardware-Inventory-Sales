SELECT password_hash, password_salt
FROM accounts
WHERE _user_id = (
	SELECT _user_id
	FROM users
	WHERE display_name = $1
);