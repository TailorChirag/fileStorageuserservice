CREATE TABLE token
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    created_at datetime NULL,
    value      VARCHAR(255) NULL,
    user_id    BIGINT NULL,
    expiry_at  datetime NULL,
    deleted    BIT(1) NOT NULL,
    CONSTRAINT pk_token PRIMARY KEY (id)
);

CREATE TABLE user
(
    id             BIGINT AUTO_INCREMENT NOT NULL,
    created_at     datetime NULL,
    name           VARCHAR(255) NULL,
    email          VARCHAR(255) NULL,
    hashed_passord VARCHAR(255) NULL,
    CONSTRAINT pk_user PRIMARY KEY (id)
);

ALTER TABLE token
    ADD CONSTRAINT FK_TOKEN_ON_USER FOREIGN KEY (user_id) REFERENCES user (id);