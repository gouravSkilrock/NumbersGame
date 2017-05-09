--liquibase formatted sql

-- changeset rachit_5433:1
update st_dg_priviledge_rep set status = 'ACTIVE' where action_id = 196 and action_mapping = 'fetchTPTktDetails'

-- rollback update st_dg_priviledge_rep set status = 'INACTIVE' where action_id = 196 and action_mapping = 'fetchTPTktDetails'
-- changeset gaurav:1
UPDATE `st_dg_priviledge_rep` SET `status` = 'ACTIVE' WHERE `action_mapping` = 'lotteryMenu_Instant'; 
-- rollback UPDATE `st_dg_priviledge_rep` SET `status` = 'INACTIVE' WHERE `action_mapping` = 'lotteryMenu_Instant'; 



