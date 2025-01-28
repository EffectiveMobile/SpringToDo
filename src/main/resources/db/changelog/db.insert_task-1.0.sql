--liquibase formatted sql

--changeset smertex:1
INSERT INTO metainfo(id, created_by)
VALUES ('e4609e63-68ad-4687-bf41-7cadda54b38d', '11d1b3a8-0def-4a8a-b00f-b51c43cd14e3');
INSERT INTO task(id, name, status, priority, metainfo_id, description, performer_id)
VALUES ('955d1c4b-8f30-4f8b-87f9-7b7107fcd206', 'Test1', 'WAITING', 'LOWEST', 'e4609e63-68ad-4687-bf41-7cadda54b38d', 'This is test description 1', 'b9c90af7-b22b-48fd-b249-3702cb6f5ce0');

--changeset smertex:2
INSERT INTO metainfo(id, created_by)
VALUES ('95570aa2-d4a3-4111-8704-9ba217faac0e', 'b9c90af7-b22b-48fd-b249-3702cb6f5ce0');
INSERT INTO task(id, name, status, priority, metainfo_id, description, performer_id)
VALUES ('a9099b32-e5b2-41aa-9ab6-d4d461549c70', 'Test2', 'WAITING', 'HIGHEST', '95570aa2-d4a3-4111-8704-9ba217faac0e', 'This is test description 2', 'b9c90af7-b22b-48fd-b249-3702cb6f5ce0');

--changeset smertex:3
INSERT INTO metainfo(id, created_by)
VALUES ('c0e93a55-311d-4b40-80f3-0441ff3edd80', '11d1b3a8-0def-4a8a-b00f-b51c43cd14e3');
INSERT INTO task(id, name, status, priority, metainfo_id, description, performer_id)
VALUES ('5f0288e7-301b-416b-af58-dd433667a607', 'Test3', 'IN_PROGRESS', 'HIGHEST', 'c0e93a55-311d-4b40-80f3-0441ff3edd80', 'This is test description 3', '11d1b3a8-0def-4a8a-b00f-b51c43cd14e3');