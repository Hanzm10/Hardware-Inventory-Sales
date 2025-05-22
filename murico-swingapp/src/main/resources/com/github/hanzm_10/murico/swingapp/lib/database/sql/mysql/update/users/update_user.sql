UPDATE users
SET display_name = :display_name,
    gender = :gender,
    first_name = :first_name,
    last_name = :last_name,
    biography = :biography
WHERE _user_id = :user_id;