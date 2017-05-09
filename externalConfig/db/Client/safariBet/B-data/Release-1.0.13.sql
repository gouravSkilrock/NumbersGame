--liquibase formatted sql

-- changeset AmitLMS-9706:1
 update st_se_priviledge_rep set group_name = 'Sale PWT collection report' where action_id = '589' and priv_id = '550';
 
-- rollback update st_se_priviledge_rep set group_name = 'SAFARI BET-Customer Specific Report' where action_id = '589' and priv_id = '550'