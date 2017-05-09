--liquibase formatted sql

--changeset B.S.Sandhu_LMS-7997:2
update st_ola_player_master set date_of_birth = '0000-00-00' where date_of_birth is null;
alter table `st_ola_player_master` change `date_of_birth` `date_of_birth` date  DEFAULT '0000-00-00' NOT NULL;
--rollback alter table `st_ola_player_master` change `date_of_birth` `date_of_birth` date   NULL ;
