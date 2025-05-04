SELECT * FROM roles
WHERE _role_id = (
	SELECT _role_id
	FROM users_roles
	WHERE _user_id = $1
);