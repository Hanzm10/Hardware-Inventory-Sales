DELIMITER $$

CREATE TRIGGER update_verification_status
BEFORE UPDATE ON accounts
FOR EACH ROW
BEGIN
	IF NEW.verification_status != OLD.verification_status THEN
		CASE NEW.verification_status
			WHEN 'verified' THEN SET NEW.verified_at = CURRENT_TIMESTMAP;
			ELSE
				BEGIN
					SET NEW.verified_at = NULL;
				END;
		END CASE;
	END IF;
END $$

CREATE TRIGGER update_session_status
BEFORE UPDATE ON sessions
FOR EACH ROW
BEGIN
    IF (NEW.status != OLD.status) THEN
        SET NEW.status_updated_at = CURRENT_TIMESTAMP;
    END IF;
END $$
DELIMITER ;
