--liquibase formatted sql

--changeset AG_LMS_8947:1
INSERT INTO st_se_priviledge_rep (priv_id, priv_title, priv_disp_name, action_mapping, parent_priv_id, status, priv_owner, related_to, group_name, is_start, channel, group_name_en) VALUES('552', 'Ticket Status',	'Ticket Status', 'ret_rep_TicketDetailStatus_Search', '0', 'ACTIVE', 'RETAILER', 'REPORTS', 'Ticket Status Report', 'N','WEB', 'Ticket Status Report');
-- rollback delete from st_se_priviledge_rep where priv_id='552' and action_mapping='ret_rep_TicketDetailStatus_Search' ;

--changeset AG_LMS_8947:2
INSERT INTO st_se_priviledge_rep (priv_id, priv_title, priv_disp_name, action_mapping, parent_priv_id, status, priv_owner, related_to, group_name, is_start, channel, group_name_en) VALUES('551', 'Ticket Status',	'Ticket Status', 'agt_rep_TicketDetailStatus_Search', '0', 'ACTIVE', 'AGENT', 'REPORTS', 'Scratch Sale Winning Collection Report', 'N','WEB', 'Ticket Status Report');
-- rollback delete from st_se_priviledge_rep where priv_id='551' and action_mapping='agt_rep_TicketDetailStatus_Search' ;