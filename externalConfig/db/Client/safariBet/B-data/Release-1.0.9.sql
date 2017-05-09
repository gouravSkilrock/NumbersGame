--liquibase formatted sql

-- changeset Gp:1
update st_se_priviledge_rep set is_start='Y' where action_mapping = 'pwt_ret_verifyAndSaveTicketNVirn';

-- rollback update st_se_priviledge_rep set is_start='N' where action_mapping = 'pwt_ret_verifyAndSaveTicketNVirn';

-- changeset Gp:2
update st_se_priviledge_rep a inner join st_lms_user_priv_mapping b ON a.priv_id=b.priv_id and b.role_id=2 and b.service_mapping_id='9'  set a.status ='ACTIVE',b.status ='ACTIVE' where action_mapping='agentDirectPlrPwtHome';

-- rollback update st_se_priviledge_rep a inner join st_lms_user_priv_mapping b ON a.priv_id=b.priv_id and b.role_id=2 and b.service_mapping_id='9'  set a.status ='INACTIVE',b.status ='INACTIVE' where action_mapping='agentDirectPlrPwtHome';

-- changeset Gp:3
update st_se_priviledge_rep set status='ACTIVE' where action_mapping='searchforPendingPwtToPay';

-- rollback update st_se_priviledge_rep set status='INACTIVE' where action_mapping='searchforPendingPwtToPay';

-- changeset Gp:4
update st_se_priviledge_rep a INNER JOIN st_lms_role_priv_mapping b ON a.priv_id=b.priv_id set a.status='INACTIVE',b.status='INACTIVE' where b.role_id='3' and b.service_mapping_id='15' and a.priv_disp_name='Sold Ticket Entry';

-- rollback update st_se_priviledge_rep a INNER JOIN st_lms_role_priv_mapping b ON a.priv_id=b.priv_id set a.status='ACTIVE',b.status='ACTIVE' where b.role_id='3' and b.service_mapping_id='15' and a.priv_disp_name='Sold Ticket Entry';;

-- changeset Gp:5
update st_se_priviledge_rep a INNER JOIN st_lms_role_priv_mapping b ON a.priv_id=b.priv_id set a.status='ACTIVE',b.status='ACTIVE' where b.role_id='1' and b.service_mapping_id='7' and a.priv_disp_name='Shift Inventory';

-- rollback update st_se_priviledge_rep a INNER JOIN st_lms_role_priv_mapping b ON a.priv_id=b.priv_id set a.status='INACTIVE',b.status='INACTIVE' where b.role_id='1' and b.service_mapping_id='7' and a.priv_disp_name='Shift Inventory';

-- changeset Gp:7
update st_se_priviledge_rep set group_name='Scratch Sale Winning Collection Report',group_name_en='Scratch Sale Winning Collection Report' where action_mapping in ('agt_rep_TicketByTicketSaleTxn_Menu','agt_rep_TicketByTicketSaleTxn_Search');

-- rollback update st_se_priviledge_rep set group_name='Scratch Sale Winning Collection Report ticket by ticket',group_name_en='Scratch Sale Winning Collection Report ticket by ticket' where action_mapping in ('agt_rep_TicketByTicketSaleTxn_Menu','agt_rep_TicketByTicketSaleTxn_Search');

-- changeset Gp:8
update st_se_priviledge_rep a Inner join st_lms_role_priv_mapping b ON a.priv_id=b.priv_id set a.status = 'ACTIVE',b.status = 'ACTIVE' where a.priv_disp_name='Sell Ticket' and b.role_id='3' and service_mapping_id = '15' and a.priv_owner='RETAILER';

-- rollback update st_se_priviledge_rep a Inner join st_lms_role_priv_mapping b ON a.priv_id=b.priv_id set a.status = 'INACTIVE',b.status = 'INACTIVE' where a.priv_disp_name='Sell Ticket' and b.role_id='3' and service_mapping_id = '15' and a.priv_owner='RETAILER';;

-- changeset Gp:9
update st_se_priviledge_rep set group_name='Winning Claim',group_name_en='Winning Claim' where action_mapping='pwt_ret_verifyAndSaveTicketNVirn';

-- rollback update st_se_priviledge_rep set group_name='Draw Game Pwt Report',group_name_en='Winning Claim' where action_mapping='pwt_ret_verifyAndSaveTicketNVirn';

-- changeset amit:1
update `st_lms_tier_master` set `tier_name`='Cashier' where `tier_code`='RETAILER';

-- rollback update `st_lms_tier_master` set `tier_name`='Retailer' where `tier_code`='RETAILER';

-- changeset amit:2
update `st_lms_tier_master` set `tier_name`='Shop' where `tier_code`='AGENT';

-- rollback update `st_lms_tier_master` set `tier_name`='Regional Office' where `tier_code`='AGENT';

-- changeset NeerajLMS7973:1
INSERT  INTO `st_se_priviledge_rep`  (`priv_id`,`priv_title`,`priv_disp_name`,`action_mapping`,`parent_priv_id`,`status`,`priv_owner`,`related_to`,`group_name`,`is_start`,`channel`,`priv_code`,`hidden`,`group_name_fr`,`group_name_en`) VALUES (552,'Ticket Status','Ticket Status','ret_rep_TicketStatus_Menu',0,'ACTIVE','RETAILER','REPORTS','Ticket Status Report','Y','WEB',NULL,NULL,NULL,'Ticket Status Report');
INSERT  INTO `st_se_priviledge_rep`(`priv_id`,`priv_title`,`priv_disp_name`,`action_mapping`,`parent_priv_id`,`status`,`priv_owner`,`related_to`,`group_name`,`is_start`,`channel`,`priv_code`,`hidden`,`group_name_fr`,`group_name_en`) VALUES (552,'Ticket Status','Ticket Status','ret_rep_TicketStatus_Search',0,'ACTIVE','RETAILER','REPORTS','Ticket Status Report','N','WEB',NULL,NULL,NULL,'Ticket Status Report');
-- rollback delete st_se_priviledge_rep  where action_mapping='ret_rep_TicketStatus_Menu' or action_mapping='ret_rep_TicketStatus_Search';
-- changeset NeerajLMS7974:1
INSERT  INTO `st_se_priviledge_rep`  (`priv_id`,`priv_title`,`priv_disp_name`,`action_mapping`,`parent_priv_id`,`status`,`priv_owner`,`related_to`,`group_name`,`is_start`,`channel`,`priv_code`,`hidden`,`group_name_fr`,`group_name_en`) VALUES (551,'Ticket Status','Ticket Status','agt_rep_TicketStatus_Menu',0,'ACTIVE','AGENT','REPORTS','Ticket Status Report','Y','WEB',NULL,NULL,NULL,'Ticket Status Report');
INSERT  INTO `st_se_priviledge_rep`(`priv_id`,`priv_title`,`priv_disp_name`,`action_mapping`,`parent_priv_id`,`status`,`priv_owner`,`related_to`,`group_name`,`is_start`,`channel`,`priv_code`,`hidden`,`group_name_fr`,`group_name_en`) VALUES (551,'Ticket Status','Ticket Status','agt_rep_TicketStatus_Search',0,'ACTIVE','AGENT','REPORTS','Ticket Status Report','N','WEB',NULL,NULL,NULL,'Ticket Status Report');
-- rollback delete st_se_priviledge_rep  where action_mapping='agt_rep_TicketStatus_Menu' or action_mapping='agt_rep_TicketStatus_Search';
-- changeset NeerajLMS7974:2
INSERT  INTO `st_se_priviledge_rep`(`priv_id`,`priv_title`,`priv_disp_name`,`action_mapping`,`parent_priv_id`,`status`,`priv_owner`,`related_to`,`group_name`,`is_start`,`channel`,`priv_code`,`hidden`,`group_name_fr`,`group_name_en`) VALUES (552,'Ticket Status','Ticket Status','ret_rep_tikcetStatus_ExpExcel',0,'ACTIVE','RETAILER','REPORTS','Ticket Status Report','N','WEB',NULL,NULL,NULL,'Ticket Status Report');
INSERT  INTO `st_se_priviledge_rep`(`priv_id`,`priv_title`,`priv_disp_name`,`action_mapping`,`parent_priv_id`,`status`,`priv_owner`,`related_to`,`group_name`,`is_start`,`channel`,`priv_code`,`hidden`,`group_name_fr`,`group_name_en`) VALUES (551,'Ticket Status','Ticket Status','agt_rep_tikcetStatus_ExpExcel',0,'ACTIVE','AGENT','REPORTS','Ticket Status Report','N','WEB',NULL,NULL,NULL,'Ticket Status Report');
-- rollback delete st_se_priviledge_rep  where action_mapping='ret_rep_tikcetStatus_ExpExcel' or action_mapping='agt_rep_tikcetStatus_ExpExcel';



-- changeset Gp:10
 
insert into `st_se_priviledge_rep` (`priv_id`, `priv_title`, `priv_disp_name`, `action_mapping`, `parent_priv_id`, `status`, `priv_owner`, `related_to`, `group_name`, `is_start`, `channel`, `priv_code`, `hidden`, `group_name_fr`, `group_name_en`) values('551','Ticket By Ticket Sale','Ticket By Ticket Sale','ret_rep_TicketByTicketSaleTxn_Menu','0','ACTIVE','RETAILER','REPORTS','Scratch Sale Winning Collection Report','Y','WEB',NULL,NULL,NULL,'Scratch Sale Winning Collection Report');

-- rollback delete from st_se_priviledge_rep where priv_id = '551' and action_mapping = 'ret_rep_TicketByTicketSaleTxn_Menu'

-- changeset Gp:11
insert into `st_se_priviledge_rep` (`priv_id`, `priv_title`, `priv_disp_name`, `action_mapping`, `parent_priv_id`, `status`, `priv_owner`, `related_to`, `group_name`, `is_start`, `channel`, `priv_code`, `hidden`, `group_name_fr`, `group_name_en`) values('551','Ticket By Ticket Sale','Ticket By Ticket Sale','ret_rep_TicketByTicketSaleTxn_Search','0','ACTIVE','RETAILER','REPORTS','Scratch Sale Winning Collection Report','N','WEB',NULL,NULL,NULL,'Scratch Sale Winning Collection Report');

-- rollback delete from st_se_priviledge_rep where priv_id = '551' and action_mapping = 'ret_rep_TicketByTicketSaleTxn_Search'


-- changeset amit_LMS-8084:3
insert into `st_se_priviledge_rep` (`priv_id`, `priv_title`, `priv_disp_name`, `action_mapping`, `parent_priv_id`, `status`, `priv_owner`, `related_to`, `group_name`, `is_start`, `channel`, `priv_code`, `hidden`, `group_name_fr`, `group_name_en`) values('120','IM_DispatchOrder','Dispatch Order','bo_im_dispatchOrder_Navigate','0','ACTIVE','BO','INV_MGT','Dispatch Order','N','WEB',NULL,NULL,NULL,'Dispatch Order');
-- rollback delete from st_se_priviledge_rep where priv_id = '120' and action_mapping = 'bo_im_dispatchOrder_Navigate';


