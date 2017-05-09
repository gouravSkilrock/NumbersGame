--liquibase formatted sql

-- changeset BAHADUR_SINGH_SANDHU:1
 ALTER TABLE st_se_ticket_by_ticket_sale_txn ADD game_id int(10) unsigned ;
-- rollback alter table `st_se_ticket_by_ticket_sale_txn` drop game_id;
 
 
 
 
 -- changeset BAHADUR_SINGH_SANDHU:2
ALTER TABLE st_se_ticket_by_ticket_sale_txn ADD ticket_price decimal(12,2) ;
-- rollback alter table `st_se_ticket_by_ticket_sale_txn` drop ticket_price ;