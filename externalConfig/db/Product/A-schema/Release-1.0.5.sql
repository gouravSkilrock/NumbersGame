-- liquibase formatted sql

-- changeset GK:1
alter table `st_lms_oranization_limits` change `security_deposit_rate` `security_deposit_rate` decimal (10,2)UNSIGNED NULL;
alter table `st_lms_oranization_limits_history` change `security_deposit_rate` `security_deposit_rate` decimal (10,2)UNSIGNED NULL;
-- rollback alter table `st_lms_oranization_limits` change `security_deposit_rate` `security_deposit_rate` decimal (10,2)UNSIGNED NOT NULL;alter table `st_lms_oranization_limits_history` change `security_deposit_rate` `security_deposit_rate` decimal (10,2)UNSIGNED NOT NULL;
