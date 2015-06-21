# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table contract (
  id                        bigint not null,
  user_id                   bigint,
  job_id                    bigint,
  completed                 boolean,
  worker_rating             integer,
  worker_comment            varchar(255),
  owner_rating              integer,
  owner_comment             varchar(255),
  constraint pk_contract primary key (id))
;

create table job (
  id                        bigint not null,
  name                      varchar(255),
  owner_id                  bigint,
  positions                 integer,
  payout                    integer,
  description               varchar(255),
  location                  varchar(255),
  latitude                  varchar(255),
  longitude                 varchar(255),
  constraint pk_job primary key (id))
;

create table user (
  id                        bigint not null,
  first_name                varchar(255),
  last_name                 varchar(255),
  email                     varchar(255),
  encrypted_password        varchar(255),
  constraint uq_user_email unique (email),
  constraint pk_user primary key (id))
;

create sequence contract_seq;

create sequence job_seq;

create sequence user_seq;

alter table contract add constraint fk_contract_user_1 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_contract_user_1 on contract (user_id);
alter table contract add constraint fk_contract_job_2 foreign key (job_id) references job (id) on delete restrict on update restrict;
create index ix_contract_job_2 on contract (job_id);
alter table job add constraint fk_job_owner_3 foreign key (owner_id) references user (id) on delete restrict on update restrict;
create index ix_job_owner_3 on job (owner_id);



# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists contract;

drop table if exists job;

drop table if exists user;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists contract_seq;

drop sequence if exists job_seq;

drop sequence if exists user_seq;

