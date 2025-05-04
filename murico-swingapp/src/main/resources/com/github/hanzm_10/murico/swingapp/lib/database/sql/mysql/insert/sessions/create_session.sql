INSERT INTO sessions (
  _user_id,
  session_ip_address,
  session_user_agent,
) VALUES (
    $1, $2, $3
);