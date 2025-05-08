CREATE TABLE users (
    _user_id INTEGER PRIMARY KEY AUTO_INCREMENT,
    _created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    display_name VARCHAR(50) NOT NULL,
    display_image text,
    gender ENUM("male", "female", "non_binary"),
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    biography VARCHAR(255),

    UNIQUE (display_name)
);

CREATE TABLE accounts (
    _account_id INTEGER PRIMARY KEY AUTO_INCREMENT,
    _user_id INTEGER,
    _created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    email VARCHAR(255) NOT NULL,
    password_hash VARCHAR(255) CHARACTER SET BINARY NOT NULL,
    password_salt VARCHAR(255) NOT NULL,
    verification_status ENUM("unverified", "verified") NOT NULL DEFAULT "unverified",

    UNIQUE (_user_id, email),
    FOREIGN KEY (_user_id)
        REFERENCES users(_user_id)
        ON DELETE CASCADE
);

CREATE TABLE accounts_pending_verifications (
    _account_pending_verification_id INTEGER PRIMARY KEY AUTO_INCREMENT,
    _account_id INTEGER,
    _created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    UNIQUE (_account_id),
    FOREIGN KEY (_account_id)
        REFERENCES accounts(_account_id)
        ON DELETE CASCADE
);

CREATE TABLE accounts_verifications (
    _account_id INTEGER PRIMARY KEY,
    _verified_by INTEGER,
    _created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (_account_id)
        REFERENCES accounts(_account_id)
        ON DELETE CASCADE,
    FOREIGN KEY (_verified_by)
        REFERENCES users(_user_id)
        ON DELETE CASCADE
);

CREATE TABLE roles (
    _role_id INTEGER PRIMARY KEY AUTO_INCREMENT,
    _created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    name VARCHAR(50) NOT NULL,
    description VARCHAR(255),

    UNIQUE (name)
);

CREATE TABLE users_roles (
    _user_id INTEGER,
    _role_id INTEGER,

    PRIMARY KEY (_user_id, _role_id),
    FOREIGN KEY (_user_id)
        REFERENCES users(_user_id)
        ON DELETE CASCADE,
    FOREIGN KEY (_role_id)
        REFERENCES roles(_role_id)
        ON DELETE CASCADE
);

CREATE TABLE sessions (
    _session_id INTEGER PRIMARY KEY AUTO_INCREMENT,
    _user_id INTEGER,
    _session_token CHAR(36) NOT NULL DEFAULT (UUID()),
    _created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP NOT NULL DEFAULT (DATE_ADD(CURRENT_TIMESTAMP, INTERVAL 7 DAY)),
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    ip_address VARCHAR(45),
    user_agent VARCHAR(255),
    status ENUM("active", "inactive", "revoked") NOT NULL DEFAULT "active",
    status_updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    UNIQUE (_session_token),
    FOREIGN KEY (_user_id)
        REFERENCES users(_user_id)
        ON DELETE CASCADE
);