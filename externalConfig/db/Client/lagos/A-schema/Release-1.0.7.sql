--liquibase formatted sql

-- changeset AR_4729:1
ALTER TABLE `st_dg_ret_pwt_2` ADD COLUMN `govt_claim_comm` DECIMAL(20,2) DEFAULT 0.00 NOT NULL AFTER `agt_claim_comm`;
ALTER TABLE `st_dg_ret_pwt_16` ADD COLUMN `govt_claim_comm` DECIMAL(20,2) DEFAULT 0.00 NOT NULL AFTER `agt_claim_comm`;
ALTER TABLE `st_dg_ret_pwt_18` ADD COLUMN `govt_claim_comm` DECIMAL(20,2) DEFAULT 0.00 NOT NULL AFTER `agt_claim_comm`;
ALTER TABLE `st_dg_ret_pwt_9` ADD COLUMN `govt_claim_comm` DECIMAL(20,2) DEFAULT 0.00 NOT NULL AFTER `agt_claim_comm`;
ALTER TABLE `st_dg_ret_pwt_14` ADD COLUMN `govt_claim_comm` DECIMAL(20,2) DEFAULT 0.00 NOT NULL AFTER `agt_claim_comm`;

-- changeset AR_4729:2
ALTER TABLE `st_dg_ret_pwt_3` ADD COLUMN `govt_claim_comm` DECIMAL(20,2) DEFAULT 0.00 NOT NULL AFTER `agt_claim_comm`;
ALTER TABLE `st_dg_ret_pwt_8` ADD COLUMN `govt_claim_comm` DECIMAL(20,2) DEFAULT 0.00 NOT NULL AFTER `agt_claim_comm`;
ALTER TABLE `st_dg_ret_pwt_15` ADD COLUMN `govt_claim_comm` DECIMAL(20,2) DEFAULT 0.00 NOT NULL AFTER `agt_claim_comm`;

-- changeset AR_4734:3
ALTER TABLE `st_lms_rg_org_daily_tx` CHANGE `sle_sale_amt` `sle_sale_amt` DOUBLE(20,2) DEFAULT 0.00 NOT NULL; 
-- rollback ALTER TABLE `st_lms_rg_org_daily_tx` CHANGE `sle_sale_amt` `sle_sale_amt` DOUBLE(20,2) NOT NULL; 
ALTER TABLE `st_lms_rg_org_weekly_tx` CHANGE `sle_sale_amt` `sle_sale_amt` DOUBLE(20,2) DEFAULT 0.00 NOT NULL;
-- rollback ALTER TABLE `st_lms_rg_org_weekly_tx` CHANGE `sle_sale_amt` `sle_sale_amt` DOUBLE(20,2) NOT NULL;