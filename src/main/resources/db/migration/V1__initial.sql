create schema if not exists exm;

create table exm.user
(
    id         bigserial primary key,
    username   varchar(32) not null unique,
    name       varchar(50),
    email      varchar(64) unique,
    created_at timestamp   not null default now()
);
