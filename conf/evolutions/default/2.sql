# Posts schema
# --- !Ups
create table "posts" (
    "id" varchar(36)not null  primary key,
    "user_id" varchar(36)not null ,
    "text" varchar(255) not null,
    "created" timestamp not null default current_timestamp
);
insert into "posts"("id", "user_id","text") values ('A75BC6C7-2B76-48F2-8EC8-5BE51F9EB4FA', '11111111-1111-1111-1111-111111111111', '今日は京都にいきます');
insert into "posts"("id", "user_id","text") values ('A75BC6C7-2B76-48F2-8EC8-5BE51F9EBdad', '22222222-2222-2222-2222-222222222222', 'お腹すいた');
insert into "posts"("id", "user_id","text") values ('A75BC6C7-2B76-48F2-8EC8-5BE51F9Ead', '33333333-3333-3333-3333-333333333333', '楽しかった');
# --- !Downs
drop table "posts"
