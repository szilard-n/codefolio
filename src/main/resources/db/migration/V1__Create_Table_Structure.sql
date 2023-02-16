create table t_user
(
    id       uuid primary key,
    username varchar unique not null,
    email    varchar unique not null,
    password varchar
);

create table t_project
(
    id          uuid primary key,
    category    varchar     not null,
    title       varchar     not null,
    description varchar,
    created_by  uuid        not null,
    created_at  timestamptz not null,
    updated_at  timestamptz not null,

    constraint fk_created_by
        foreign key (created_by)
            references t_user (id)
);

create table t_task
(
    id          uuid primary key,
    description varchar not null,
    index       integer not null,
    project_id  uuid,
    created_by  uuid    not null,

    constraint fk_project_id
        foreign key (project_id)
            references t_project (id),

    constraint fk_created_by
        foreign key (created_by)
            references t_user (id)
);

create table t_user_project
(
    user_id    uuid,
    project_id uuid,

    constraint fk_user_id
        foreign key (user_id)
            references t_user (id),

    constraint fk_project_id
        foreign key (project_id)
            references t_project (id),

    constraint pk_user_project
        primary key (user_id, project_id)
);

create table t_user_task
(
    id        uuid primary key,
    user_id   uuid,
    task_id   uuid,
    completed boolean,

    constraint fk_user_id
        foreign key (user_id)
            references t_user (id),

    constraint fk_task_id
        foreign key (task_id)
            references t_task (id)
)

