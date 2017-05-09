--liquibase formatted sql
-- changeset Amit_7870:1
CREATE TABLE `st_lms_role_agent_mapping` (  
                             `role_id` int(10) NOT NULL,               
                             `agent_id` int(10) NOT NULL,              
                             PRIMARY KEY (`role_id`,`agent_id`)        
                           ) ENGINE=InnoDB;
                           
-- rollback DROP TABLE st_lms_role_agent_mapping;