--liquibase formatted sql

--changeset smertex:1
UPDATE users SET password = '$2a$12$4aGPvHwRecNHy.p5lc/Bxe2woV9Um2zrr5xa/R0A837o461cgPpyu'
WHERE id = '11d1b3a8-0def-4a8a-b00f-b51c43cd14e3';

UPDATE users SET password = '$2a$12$4aGPvHwRecNHy.p5lc/Bxe2woV9Um2zrr5xa/R0A837o461cgPpyu'
WHERE id = 'b9c90af7-b22b-48fd-b249-3702cb6f5ce0';