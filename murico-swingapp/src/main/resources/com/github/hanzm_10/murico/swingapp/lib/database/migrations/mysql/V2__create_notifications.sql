CREATE TABLE notification_types (
    _notification_type_id INTEGER PRIMARY KEY AUTO_INCREMENT,
    code VARCHAR(50) NOT NULL,
    description VARCHAR(255),

    UNIQUE (code)
);

CREATE TABLE notifications (
    _notification_id INTEGER PRIMARY KEY AUTO_INCREMENT,
    _user_id INTEGER,
    _notification_type_id INTEGER,
    _created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    read_at TIMESTAMP NULL DEFAULT NULL,
    priority ENUM("low", "normal", "high", "urgent", "critical") NOT NULL DEFAULT "low",
    message TEXT NOT NULL,

    FOREIGN KEY (_user_id)
        REFERENCES users(_user_id)
        ON DELETE CASCADE,
    FOREIGN KEY (_notification_type_id)
        REFERENCES notification_types(_notification_type_id)
        ON DELETE SET NULL
);