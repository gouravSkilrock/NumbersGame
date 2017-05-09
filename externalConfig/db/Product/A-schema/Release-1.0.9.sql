--liquibase formatted sql

-- changeset Gp:1
alter table `st_lms_user_master` change `tp_user_id` `tp_user_id` varchar (20) NULL ;

-- rollback alter table `st_lms_user_master` change `tp_user_id` `tp_user_id` varchar (20) NOT NULL ;