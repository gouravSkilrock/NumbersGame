--liquibase formatted sql

-- changeset AR_4640:1
ALTER TABLE `st_lms_oranization_limits` CHANGE `levy_rate` `levy_rate` DECIMAL(10,2) UNSIGNED DEFAULT 0.00 NOT NULL; 
-- rollback  ALTER TABLE `st_lms_oranization_limits` CHANGE `levy_rate` `levy_rate` DECIMAL(10,2) UNSIGNED NOT NULL;

-- changeset AR_4640:2
ALTER TABLE `st_lms_oranization_limits` CHANGE `security_deposit_rate` `security_deposit_rate` DECIMAL(10,2) UNSIGNED DEFAULT 0.00 NOT NULL;
-- rollback ALTER TABLE `st_lms_oranization_limits` CHANGE `security_deposit_rate` `security_deposit_rate` DECIMAL(10,2) UNSIGNED NOT NULL; 

-- changeset AR_4640:3
ALTER TABLE `st_lms_oranization_limits_history` CHANGE `security_deposit_rate` `security_deposit_rate` DECIMAL(10,2) UNSIGNED DEFAULT 0.00 NOT NULL;
-- rollback ALTER TABLE `st_lms_oranization_limits_history` CHANGE `security_deposit_rate` `security_deposit_rate` DECIMAL(10,2) UNSIGNED NOT NULL; 

-- changeset AR_4640:4
ALTER TABLE `st_lms_oranization_limits_history` CHANGE `levy_rate` `levy_rate` DECIMAL(10,2) UNSIGNED DEFAULT 0.00 NOT NULL; 
-- rollback  ALTER TABLE `st_lms_oranization_limits_history` CHANGE `levy_rate` `levy_rate` DECIMAL(10,2) UNSIGNED NOT NULL;