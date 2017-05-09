--liquibase formatted sql

-- changeset RACHIT_LMS_7296:1
insert into `st_se_priviledge_rep` (`priv_id`, `priv_title`, `priv_disp_name`, `action_mapping`, `parent_priv_id`, `status`, `priv_owner`, `related_to`, `group_name`, `is_start`, `channel`, `priv_code`, `hidden`, `group_name_fr`, `group_name_en`) values('1118','RET_PWT_VERIFICATION','RET:Pwt Verification','ticketAndVirnNumberVerify','0','ACTIVE','RETAILER','PWT','Winning Claim','N','WEB',NULL,NULL,'Winning Claim','Winning Claim');
-- rollback delete from st_se_priviledge_rep where priv_id=1118 AND action_mapping = 'ticketAndVirnNumberVerify';