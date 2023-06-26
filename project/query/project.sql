CREATE TABLE project (
                        id BIGINT PRIMARY KEY AUTO_INCREMENT,
                        member_id BIGINT NOT NULL,
                        title       VARCHAR(20)  NOT NULL,
                        description VARCHAR(255) NOT NULL,
                        goal_amount BIGINT       NOT NULL,
                        status      VARCHAR(20)  NOT NULL,
                        start_date  DATE         NOT NULL,
                        end_date    DATE         NOT NULL,
                        created_at  DATETIME(6)  NOT NULL,
                        updated_at  DATETIME(6)  NOT NULL
);