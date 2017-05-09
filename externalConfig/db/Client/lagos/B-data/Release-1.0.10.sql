--liquibase formatted sql

--changeset Rachit_Bhandari_6099:1
update `st_dg_priviledge_rep` set `status`='INACTIVE' where `action_mapping` in ('bo_dg_rep_pwt_Menu','bo_dg_rep_pwt_Search');
-- rollback update `st_dg_priviledge_rep` set `status`='ACTIVE' where `action_mapping` in ('bo_dg_rep_pwt_Menu','bo_dg_rep_pwt_Search');

--changeset Rachit_Bhandari_6099:2
update st_lms_priviledge_rep set status = 'INACTIVE' where action_mapping in ('bo_rep_customSlotSaleMenu','bo_rep_customSlotSaleSearch');
-- rollback update st_lms_priviledge_rep set status = 'ACTIVE' where action_mapping in ('bo_rep_customSlotSaleMenu','bo_rep_customSlotSaleSearch');