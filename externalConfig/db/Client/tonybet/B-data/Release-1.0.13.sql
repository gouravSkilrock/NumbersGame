--liquibase formatted sql

--changeset Rachit-9836:1
update st_dg_priviledge_rep set status = 'ACTIVE' where priv_title = 'BO_DG_THIRTPARTY_TRACK_TICKET' and priv_id = 109;
--rollback update st_dg_priviledge_rep set status = 'INACTIVE' where priv_title = 'BO_DG_THIRTPARTY_TRACK_TICKET' and priv_id = 109; 

--changeset Rachit-9836:2
update st_dg_priviledge_rep set status = 'INACTIVE' where priv_id in (19,21,22,40,27,34,35,36,38,28);
--rollback update st_dg_priviledge_rep set status = 'ACTIVE' where priv_id in (19,21,22,40,27,34,35,36,38,28);