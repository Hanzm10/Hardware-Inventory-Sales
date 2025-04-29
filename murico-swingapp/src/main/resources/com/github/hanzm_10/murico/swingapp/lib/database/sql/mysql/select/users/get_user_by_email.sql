SELECT
  _user_id, _user_created_at, user_display_name, user_display_image, user_gender
FROM
  users
  JOIN user_credentials USING (_user_id)
WHERE
  user_email = ?;
