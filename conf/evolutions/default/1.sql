# --- !Ups
create table "user" (
    "id" varchar(36)not null  primary key,
    "name" varchar(255) not null,
    "created_at" datetime not null default current_timestamp

);
insert into "user"("id","name") values ('11111111-1111-1111-1111-111111111111', 'alice');
insert into "user"("id","name") values ('22222222-2222-2222-2222-222222222222','bob');
insert into "user"("id","name") values ('33333333-3333-3333-3333-333333333333', 'charlie');


# --- !Downs
drop table "user"
