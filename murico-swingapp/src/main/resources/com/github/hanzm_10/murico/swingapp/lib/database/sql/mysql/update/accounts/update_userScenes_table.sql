UPDATE roles r
JOIN users_roles ur
  ON ur._role_id = r._role_id
SET r.name = ?
WHERE u._user_id = ?;