CREATE TABLE user
(
    id             BIGINT AUTO_INCREMENT NOT NULL,
    created_at     datetime NULL,
    name           VARCHAR(255) NULL,
    email          VARCHAR(255) NULL,
    hashed_passord VARCHAR(255) NULL,
    CONSTRAINT pk_user PRIMARY KEY (id)
);