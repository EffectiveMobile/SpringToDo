CREATE TABLE users (
    id               BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    email      VARCHAR(32)                                   NOT NULL,
    password   VARCHAR(1024)                                  NOT NULL,
    CONSTRAINT users_pkey PRIMARY KEY (id)
);

CREATE TABLE tasks (
    id               BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    creation_date    TIMESTAMP WITHOUT TIME ZONE,
    task_description VARCHAR(255)                            NOT NULL,
    task_header      VARCHAR(255)                            NOT NULL,
    task_priority    VARCHAR(255)                            NOT NULL,
    task_status      VARCHAR(255)                            NOT NULL,
    assignee_id      BIGINT,
    author_id        BIGINT,
    CONSTRAINT tasks_pkey PRIMARY KEY (id),
    CONSTRAINT author_id FOREIGN KEY (author_id)
        REFERENCES users (id) ON UPDATE NO ACTION ON DELETE CASCADE,
    CONSTRAINT assignee_id FOREIGN KEY (assignee_id)
        REFERENCES users (id) ON UPDATE NO ACTION ON DELETE CASCADE
);