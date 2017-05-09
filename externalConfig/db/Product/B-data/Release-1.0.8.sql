--liquibase formatted sql

-- changeset BAHADUR_SINGH_SANDHU_LMS_1616:1
insert  into `st_se_priviledge_rep`(`priv_id`,`priv_title`,`priv_disp_name`,`action_mapping`,`parent_priv_id`,`status`,`priv_owner`,`related_to`,`group_name`,`is_start`,`channel`,`priv_code`,`hidden`,`group_name_fr`,`group_name_en`) values (549,'Ticket By Ticket Sale','Ticket By Ticket Sale','agt_rep_TicketByTicketSaleTxn_Menu',0,'ACTIVE','AGENT','REPORTS','Ticket By Ticket Sale','Y','WEB',NULL,NULL,NULL,'Sale PWT Collection Report Ticket By Ticket');
-- rollback delete from st_se_priviledge_rep where priv_id=549 AND action_mapping = 'agt_rep_TicketByTicketSaleTxn_Menu';

-- changeset BAHADUR_SINGH_SANDHU_LMS_1616:2
insert  into `st_se_priviledge_rep`(`priv_id`,`priv_title`,`priv_disp_name`,`action_mapping`,`parent_priv_id`,`status`,`priv_owner`,`related_to`,`group_name`,`is_start`,`channel`,`priv_code`,`hidden`,`group_name_fr`,`group_name_en`) values (549,'Ticket By Ticket Sale','Ticket By Ticket Sale','agt_rep_TicketByTicketSaleTxn_Search',0,'ACTIVE','AGENT','REPORTS','Ticket By Ticket Sale','N','WEB',NULL,NULL,NULL,'Sale PWT Collection Report Ticket By Ticket');
-- rollback delete from st_se_priviledge_rep where priv_id=549 AND action_mapping = 'agt_rep_TicketByTicketSaleTxn_Search'; 

-- changeset Mukesh_Sharma:1
insert  into `st_se_priviledge_rep`(`priv_id`,`priv_title`,`priv_disp_name`,`action_mapping`,`parent_priv_id`,`status`,`priv_owner`,`related_to`,`group_name`,`is_start`,`channel`,`priv_code`,`hidden`,`group_name_fr`,`group_name_en`)values (550,'bo_rep_customer_specific_Menu','bo_rep_customer_specific_Menu','bo_rep_customer_specific_Menu',0,'ACTIVE','BO','REPORTS','SAFARI BET-Customer Specific Report','Y','WEB',NULL,NULL,NULL,'SAFARI BET-Customer Specific Report');
-- rollback delete from `st_se_priviledge_rep` where priv_id=550 AND priv_title='bo_rep_customer_specific_Menu';
