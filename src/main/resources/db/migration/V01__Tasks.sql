create table tasks
(
    id          serial
        primary key,
    title       varchar(100) not null
        unique,
    description varchar(255),
    status      varchar(20)  not null
);
