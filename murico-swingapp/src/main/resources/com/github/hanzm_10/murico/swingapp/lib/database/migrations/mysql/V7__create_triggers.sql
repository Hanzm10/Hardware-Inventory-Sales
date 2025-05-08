DELIMITER $$

CREATE TRIGGER update_verification_status
AFTER INSERT ON accounts_verifications
FOR EACH ROW
BEGIN
    UPDATE accounts
    SET verification_status = "verified"
    WHERE _account_id = NEW._account_id;

    DELETE FROM accounts_pending_verifications
    WHERE _account_id = NEW._account_id;
END $$

CREATE TRIGGER update_session_status
BEFORE UPDATE ON sessions
FOR EACH ROW
BEGIN
    IF (NEW.status != OLD.status) THEN
        SET NEW.status_updated_at = CURRENT_TIMESTAMP;
    END IF;
END $$

CREATE TRIGGER update_order_status
BEFORE UPDATE ON orders
FOR EACH ROW
BEGIN
    IF (NEW.status != OLD.status) THEN
        SET NEW.status_updated_at = CURRENT_TIMESTAMP;
    END IF;
END $$

CREATE TRIGGER update_delivery_status
BEFORE UPDATE ON deliveries
FOR EACH ROW
BEGIN
    IF (NEW.status != OLD.status) THEN
        SET NEW.status_updated_at = CURRENT_TIMESTAMP;
    END IF;
END $$

DELIMITER ;
