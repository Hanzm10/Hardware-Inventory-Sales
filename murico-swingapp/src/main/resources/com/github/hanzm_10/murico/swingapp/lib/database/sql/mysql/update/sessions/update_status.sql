UPDATE sessions
SET status = ?
WHERE _session_token = ?;  -- replace with the actual session ID