SELECT EXISTS (
    SELECT 1
    FROM sessions
    WHERE _session_uid = ?  
);