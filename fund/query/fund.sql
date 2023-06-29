CREATE TABLE fund
(
    id             BIGINT PRIMARY KEY AUTO_INCREMENT,
    member_id      BIGINT      NOT NULL,
    project_id     BIGINT      NOT NULL,
    amount         BIGINT      NOT NULL,
    status         VARCHAR(20) NOT NULL,
    payment_status VARCHAR(20) NOT NULL,
    created_at     DATETIME(6) NOT NULL,
    updated_at     DATETIME(6) NOT NULL
);