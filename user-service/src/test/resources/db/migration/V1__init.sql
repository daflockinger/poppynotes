create table user (id bigint not null auto_increment, auth_email varchar(255) not null, crypt_key varchar(255), name varchar(255), pin_code varchar(255), recovery_email varchar(255) not null, status varchar(255) not null, unlock_code varchar(255), primary key (id)) ENGINE = MEMORY;
create table user_roles (user_id bigint not null, roles varchar(255)) ENGINE = MEMORY;
create index IDX33eks3n2e24dw9mcbtm1lb47o on user (auth_email) using HASH;
alter table user add constraint UK33eks3n2e24dw9mcbtm1lb47o unique (auth_email);
alter table user_roles add constraint FK55itppkw3i07do3h7qoclqd4k foreign key (user_id) references user (id);