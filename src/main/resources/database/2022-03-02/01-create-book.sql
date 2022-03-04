--liquibase formatted sql
--changeset aolejniczak:1
CREATE TABLE IF NOT EXISTS book
(
    id    BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(400) NOT NULL

);
