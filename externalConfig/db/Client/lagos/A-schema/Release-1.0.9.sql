--liquibase formatted sql

-- changeset Rishi_5783:1
alter table `st_rep_bo_payments` change `cash_amt` `cash_amt` decimal (20,2) DEFAULT '0.00' NOT NULL;
-- rollback alter table `st_rep_bo_payments` change `cash_amt` `cash_amt` decimal (10,2) DEFAULT '0.00' NOT NULL ;