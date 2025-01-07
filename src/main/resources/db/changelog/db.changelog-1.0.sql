--liquibase formatted sql

--changeset smertex:1
CREATE TABLE IF NOT EXISTS users(
    id              UUID            PRIMARY KEY DEFAULT gen_random_uuid(),
    email           VARCHAR(256)    UNIQUE NOT NULL,
    password        VARCHAR(256)    NOT NULL,
    role            VARCHAR(128)    NOT NULL
);

--changeset smertex:2
CREATE TABLE IF NOT EXISTS metainfo(
    id              UUID            PRIMARY KEY DEFAULT gen_random_uuid(),
    created_at      TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by      UUID            REFERENCES users(id) NOT NULL
);

--changeset smertex:3
CREATE TABLE IF NOT EXISTS task(
    id              UUID            PRIMARY KEY DEFAULT gen_random_uuid(),
    name            VARCHAR(128)    UNIQUE NOT NULL,
    status          VARCHAR(128)    NOT NULL,
    priority        VARCHAR(128)    NOT NULL,
    description     TEXT            NOT NULL,
    metainfo_id     UUID            REFERENCES metainfo(id) ON DELETE CASCADE NOT NULL UNIQUE,
    performer_id    UUID            REFERENCES users(id) NOT NULL
);

--changeset smertex:4
CREATE TABLE IF NOT EXISTS comment(
    id              UUID            PRIMARY KEY DEFAULT gen_random_uuid(),
    task_id         UUID            REFERENCES task(id) NOT NULL,
    content         TEXT            NOT NULL UNIQUE,
    created_at      TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by      UUID            REFERENCES users(id) NOT NULL
);

