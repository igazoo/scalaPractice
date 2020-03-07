
# --- !Ups
create table "comments" (
    "id" varchar(36)not null  primary key,
    "parent_post_id" varchar(36)not null ,
    "text" varchar(255) not null,
    "created" timestamp not null default current_timestamp
);
insert into "comments"("id", "parent_post_id","text") values ('A75BC6C7-2B76-48F2-8EC8-5BE51F484FA','A75BC6C7-2B76-48F2-8EC8-5BE51F9EB4FA',  'こちらこそ、こんにちは');

# --- !Downs
drop table "comments"
