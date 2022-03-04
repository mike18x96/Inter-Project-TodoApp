-- liquibase formatted sql
-- changeset aolejniczak:3
CREATE TABLE IF NOT EXISTS user
(
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    login         varchar(50)  not null,
    password_hash varchar(100) not null,
    role          varchar(50)  not null
);
-- changeset aolejniczak:4
insert into user (id, login, password_hash, role)
values (1, 'admin', '$2a$10$IYmI740YXs71LmtCxc3G4udVBcFO/maxJe7U7qiv3v4ZObErZytu.', 'ADMIN');
insert into user (id, login, password_hash, role)
values (2, 'henio', '$2a$10$TE/2gMzatosiE/K7xYqUk.hbcTc4yl6/QVefHOEbhpCsLCjVvPhcm', 'USER');



