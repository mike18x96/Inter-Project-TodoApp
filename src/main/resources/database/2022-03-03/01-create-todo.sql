-- liquibase formatted sql
-- changeset aolejniczak:5
CREATE TABLE IF NOT EXISTS todo
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        varchar(50)  not null,
    priority    varchar(100) not null,
    description varchar(100) not null,
    completed   boolean default 0,
    user_id     BIGINT,
    FOREIGN KEY (user_id) REFERENCES user(id)
);

