-- liquibase formatted sql

--changeset GMG:1  
insert into `st_lms_property_master`(`property_code`,`property_dev_name`,`property_display_name`,`status`,`editable`,`value`,`value_type`,`description`) values ('SCRATCH_GAME_NBR_DIGITS','SCRATCH_GAME_NBR_DIGITS','SCRATCH_GAME_NBR_DIGITS','ACTIVE','YES','3','Integer','tell no. of game nbr digits after which hypen will be inserted to make it valid ticket format.')

--rollback delete from  st_lms_property_master where property_code='SCRATCH_GAME_NBR_DIGITS';

