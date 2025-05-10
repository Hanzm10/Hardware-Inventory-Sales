DELIMITER $$

CREATE TRIGGER deliver_status_change_guard
BEFORE UPDATE ON deliveries
FOR EACH ROW
BEGIN
	IF (NEW.status != OLD.status) THEN
		SET NEW.status_updated_at = CURRENT_TIMESTAMP;
	END IF;
END $$

DELIMITER ;