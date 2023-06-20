CREATE TABLE member (
                        id BIGINT PRIMARY KEY AUTO_INCREMENT,
                        username VARCHAR(20) NOT NULL,
                        password VARCHAR(255) NOT NULL,
                        phone VARCHAR(20) NOT NULL,
                        role VARCHAR(20) NOT NULL,
                        created_at DATETIME(6) NOT NULL,
                        updated_at DATETIME(6) NOT NULL
);