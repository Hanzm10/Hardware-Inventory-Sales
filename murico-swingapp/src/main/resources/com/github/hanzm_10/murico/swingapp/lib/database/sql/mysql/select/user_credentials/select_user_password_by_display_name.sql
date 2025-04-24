SELECT user_password FROM user_credentials WHERE (
    SELECT _user_id
    FROM users
    WHERE user_display_name = ?
) = _user_id