--liquibase formatted sql

-- changeset AR_4728:3
INSERT INTO `st_lms_property_master` (`property_code`, `property_dev_name`, `property_display_name`, `status`, `editable`, `value`, `value_type`, `description`) VALUES('SLE_LAST_TICKET_CANCEL','SLE_LAST_TICKET_CANCEL','SLE_LAST_TICKET_CANCEL','ACTIVE','YES','YES','String','SLE_LAST_TICKET_CANCEL');

-- changeset nikhil:1
INSERT INTO `st_lms_property_master` (`property_code`, `property_dev_name`, `property_display_name`, `status`, `editable`, `value`, `value_type`, `description`) VALUES('new bettype_off_start_time','NEW_BETTYPE_OFF_START_TIME','new bettype Off Start Time','ACTIVE','YES','19:30:00','timestamp','bettype Off Start Time for 4/90,3/90,2/90');
INSERT INTO `st_lms_property_master` (`property_code`, `property_dev_name`, `property_display_name`, `status`, `editable`, `value`, `value_type`, `description`) VALUES('new_bettype_off_end_time','NEW_BETTYPE_OFF_END_TIME','new bettype Off End Time','ACTIVE','YES','19:40:00','timestamp','bettype Off End Time for 4/90,3/90,2/90');

-- rollback delete from  st_lms_property_master where property_dev_name IN ('NEW_BETTYPE_OFF_START_TIME','NEW_BETTYPE_OFF_END_TIME');