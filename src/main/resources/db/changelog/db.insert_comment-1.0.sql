--liquibase formatted sql

--changeset smertex:1
INSERT INTO comment(id, task_id, content, created_by)
VALUES ('5e0dc76a-f6d1-44b9-9f6a-259915ceef7c', 'a9099b32-e5b2-41aa-9ab6-d4d461549c70', 'This is test content 1', 'b9c90af7-b22b-48fd-b249-3702cb6f5ce0');

--changeset smertex:2
INSERT INTO comment(id, task_id, content, created_by)
VALUES ('0ccd1547-d72b-424a-9c60-6d38318eb2f5', '5f0288e7-301b-416b-af58-dd433667a607', 'This is test content 2', '11d1b3a8-0def-4a8a-b00f-b51c43cd14e3');

--changeset smertex:3
INSERT INTO comment(id, task_id, content, created_by)
VALUES ('db15c0f3-8255-4a3f-8b81-7fd129028ab3', '5f0288e7-301b-416b-af58-dd433667a607', 'This is test content 3', '11d1b3a8-0def-4a8a-b00f-b51c43cd14e3');
