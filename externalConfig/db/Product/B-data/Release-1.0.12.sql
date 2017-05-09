--liquibase formatted sql
--changeset BaseDataRMS:1
delete from st_lms_role_agent_mapping ;
insert into st_lms_role_agent_mapping SELECT DISTINCT a.role_id, b.organization_id FROM st_lms_user_master a INNER JOIN st_lms_user_master b ON a.user_id=b.parent_user_id WHERE a.organization_type='BO' AND a.user_id IN(SELECT c.parent_user_id FROM st_lms_user_master c WHERE c.organization_type='AGENT') AND b.organization_type='agent' UNION SELECT DISTINCT a.role_id, b.organization_id FROM st_lms_user_master a, st_lms_user_master b  WHERE a.organization_type='BO' AND  b.organization_type='AGENT' AND a.role_id=1;
-- rollback delete from st_lms_role_agent_mapping ;