SELECT
    u._user_id,
    u.display_name,
    u.first_name,
    u.last_name,
    GROUP_CONCAT(r.name) AS roles

FROM users u

LEFT JOIN (
    SELECT ur._user_id, r.name
    FROM users_roles ur
    LEFT JOIN roles r ON ur._role_id = r._role_id
) r ON u._user_id = r._user_id

GROUP BY u._user_id, u.display_name, u.first_name, u.last_name;
