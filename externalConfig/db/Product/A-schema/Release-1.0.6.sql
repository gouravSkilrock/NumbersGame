--liquibase formatted sql

-- changeset ISHU:1
 alter table `st_lms_agent_transaction_master` change `transaction_type` `transaction_type` enum ('DG_SALE','CHEQUE','CHQ_BOUNCE','DR_NOTE','DG_PWT_AUTO','DG_REFUND_CANCEL','PURCHASE','DG_REFUND_FAILED',
'VAT','SALE','CASH','TDS','PWT_PLR','CR_NOTE_CASH','DR_NOTE_CASH','GOVT_COMM','SALE_RET','PWT','UNCLM_PWT',
'PWT_AUTO','DG_PWT_PLR','DG_PWT','CS_SALE','CS_CANCEL_SERVER','CS_CANCEL_RET','LOOSE_SALE','LOOSE_SALE_RET',
'OLA_DEPOSIT','OLA_DEPOSIT_PLR','OLA_WITHDRAWL','OLA_WITHDRAWL_PLR','OLA_DEPOSIT_REFUND','OLA_DEPOSIT_REFUND_PLR',
'OLA_WITHDRAWL_REFUND','OLA_WITHDRAWL_REFUND_PLR','OLA_COMMISSION','SLE_SALE','SLE_REFUND_CANCEL','SLE_PWT','SLE_PWT_AUTO',
'BANK_DEPOSIT','IW_SALE','IW_REFUND_CANCEL','IW_PWT','IW_PWT_AUTO') NULL;

-- rollback alter table `st_lms_agent_transaction_master` change `transaction_type` `transaction_type` enum ('DG_SALE') NULL;

-- changeset GMG:1
create table `st_se_ticket_by_ticket_sale_txn` (  `txn_id` varchar (50) NOT NULL , `ticket_nbr` varchar (50) NOT NULL , `ret_org_id` int (11) NOT NULL , `ret_user_id` int (11) , `sale_time` datetime NOT NULL , `tp_txn_id` varchar (20) , `ticket_status` enum ('SOLD','UNSOLD') NOT NULL , PRIMARY KEY (`txn_id`));

-- rollback DROP TABLE st_se_ticket_by_ticket_sale_txn;


--changeset GMG:2
ALTER TABLE st_se_ticket_by_ticket_sale_txn ADD UNIQUE (tp_txn_id);

-- rollback ALTER TABLE st_se_ticket_by_ticket_sale_txn DROP INDEX tp_txn_id;