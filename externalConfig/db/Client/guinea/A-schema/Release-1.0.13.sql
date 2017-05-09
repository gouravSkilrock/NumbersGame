--liquibase formatted sql

--changeset kannu_9621:1
alter table `st_lms_user_master` add column `tp_user_id` varchar(20) DEFAULT NULL;