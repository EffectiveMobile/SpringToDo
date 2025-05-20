create table tasks
(
    id          serial
        primary key,
    title       varchar(100) not null
        unique,
    description varchar(255),
    status      varchar(20)  not null
);

INSERT INTO tasks (title, description, status) VALUES
                                                   ('Task 1', 'Desc 1', 'PENDING'),
                                                   ('Task 2', 'Desc 2', 'PENDING'),
                                                   ('Task 3', 'Desc 3', 'COMPLETED');