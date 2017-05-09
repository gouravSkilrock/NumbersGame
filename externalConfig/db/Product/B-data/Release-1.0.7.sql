--liquibase formatted sql

-- changeset BAHADUR_SINGH_SANDHU:1
insert into `st_lms_property_master`(`property_code`,`property_dev_name`,`property_display_name`,`status`,`editable`,`value`,`value_type`,`description`)values('PLAYER_WINNING_TAX_APPLICABLE_AMOUNT','PLAYER_WINNING_TAX_APPLICABLE_AMOUNT','Player Winning Tax Applicable Amount','ACTIVE','YES','1000000','Double','Player Winning Tax Applicable Amount');

-- rollback delete from `st_lms_property_master` where property_dev_name='PLAYER_WINNING_TAX_APPLICABLE_AMOUNT';