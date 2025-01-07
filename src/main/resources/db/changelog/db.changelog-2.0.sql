--liquibase formatted sql

--changeset smertex:1
INSERT INTO users(id, email, password, role)
VALUES ('11d1b3a8-0def-4a8a-b00f-b51c43cd14e3', 'smertexx@gmail.com', 'qwertyui12345678', 'ADMIN');

--changeset smertex:2
INSERT INTO users(id, email, password, role)
VALUES ('b9c90af7-b22b-48fd-b249-3702cb6f5ce0', 'evgenii@gmail.com', 'qwertyui12345678', 'USER');