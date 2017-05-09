--liquibase formatted sql

--changeset Rachit-9381:1
CREATE TABLE `archiving_status` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `job_name` varchar(100) DEFAULT NULL,
  `datetime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `status` enum('NOT_DONE','DONE') DEFAULT 'NOT_DONE',
  `result` int(5) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC;
--rollback drop table `archiving_status`;


CREATE TABLE `datestore` (
  `alldate` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `datestore`;


CREATE TABLE `ge_merchant_master` (
  `merchant_id` int(5) NOT NULL AUTO_INCREMENT,
  `merchant_code` varchar(20) DEFAULT NULL,
  `merchant_name` varchar(50) DEFAULT NULL,
  `merchant_domain_name` varchar(70) DEFAULT NULL,
  `registration_date` datetime DEFAULT NULL,
  `user_name` varchar(50) DEFAULT NULL,
  `password` varchar(50) DEFAULT NULL,
  `vendor_user_name` varchar(50) DEFAULT NULL,
  `vendor_password` varchar(50) DEFAULT NULL,
  `status` enum('ACTIVE','INACTIVE') DEFAULT NULL,
  PRIMARY KEY (`merchant_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
--rollback drop table `ge_merchant_master`;


CREATE TABLE `ge_sale_promo_ticket_mapping` (
  `promo_id` int(11) DEFAULT NULL,
  `sale_ticket_nbr` bigint(20) unsigned DEFAULT NULL,
  `promo_ticket_nbr` bigint(20) unsigned DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `ge_sale_promo_ticket_mapping`;


CREATE TABLE `lmc` (
  `lmc_id` int(20) DEFAULT NULL,
  `region` varchar(20) DEFAULT NULL,
  `city` varchar(20) DEFAULT NULL,
  `zone` varchar(20) DEFAULT NULL,
  `area_code` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
--rollback drop table `lmc`;

CREATE TABLE `lmc_missing` (
  `lmc_id` int(20) DEFAULT NULL,
  `region` varchar(20) DEFAULT NULL,
  `city` varchar(20) DEFAULT NULL,
  `zone` varchar(20) DEFAULT NULL,
  `area_code` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
--rollback drop table `lmc_missing`;

CREATE TABLE `retailers` (
  `retailer_id` int(20) DEFAULT NULL,
  `region` varchar(20) DEFAULT NULL,
  `city` varchar(20) DEFAULT NULL,
  `zone` varchar(20) DEFAULT NULL,
  `area_code` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
--rollback drop table `retailers`;

CREATE TABLE `retailers_missing` (
  `retailer_id` int(20) DEFAULT NULL,
  `region` varchar(20) DEFAULT NULL,
  `city` varchar(20) DEFAULT NULL,
  `zone` varchar(20) DEFAULT NULL,
  `area_code` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
--rollback drop table `retailers_missing`;

CREATE TABLE `st_admin_menu_master` (
  `menu_id` smallint(10) unsigned NOT NULL AUTO_INCREMENT,
  `action_id` smallint(10) unsigned NOT NULL,
  `menu_name` varchar(50) DEFAULT NULL,
  `menu_disp_name` varchar(50) DEFAULT NULL,
  `parent_menu_id` smallint(10) unsigned NOT NULL,
  `item_order` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`menu_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC;
--rollback drop table `st_admin_menu_master`;

CREATE TABLE `st_admin_priviledge_rep` (
  `action_id` smallint(10) unsigned NOT NULL AUTO_INCREMENT,
  `priv_id` int(10) unsigned NOT NULL,
  `priv_title` varchar(50) CHARACTER SET utf8 NOT NULL DEFAULT '',
  `priv_disp_name` varchar(50) DEFAULT NULL,
  `action_mapping` varchar(50) CHARACTER SET utf8 DEFAULT NULL,
  `parent_priv_id` int(10) unsigned NOT NULL,
  `status` enum('ACTIVE','INACTIVE') DEFAULT NULL,
  `priv_owner` enum('ADMIN') DEFAULT NULL,
  `related_to` enum('ADMIN_MGT') DEFAULT NULL,
  `group_name` varchar(50) DEFAULT NULL,
  `is_start` enum('Y','N') DEFAULT NULL,
  PRIMARY KEY (`action_id`),
  KEY `priv_title` (`priv_title`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
--rollback drop table `st_admin_priviledge_rep`;

CREATE TABLE `st_admin_user_master` (
  `user_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `registration_date` datetime DEFAULT NULL,
  `last_login_date` datetime DEFAULT NULL,
  `auto_password` tinyint(1) DEFAULT NULL,
  `user_name` varchar(30) CHARACTER SET utf8 NOT NULL,
  `password` varchar(30) CHARACTER SET utf8 NOT NULL,
  `status` enum('ACTIVE','INACTIVE','TERMINATE') CHARACTER SET utf8 NOT NULL,
  `secret_ques` varchar(50) CHARACTER SET utf8 NOT NULL,
  `secret_ans` varchar(50) CHARACTER SET utf8 DEFAULT NULL,
  `login_attempt` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
--rollback drop table `st_admin_user_master`;

CREATE TABLE `st_cs_agent_retailer_sale_comm_variance` (
  `agent_org_id` int(10) unsigned NOT NULL,
  `retailer_org_id` int(10) unsigned NOT NULL,
  `product_id` int(10) unsigned NOT NULL,
  `sale_comm_variance` decimal(5,2) DEFAULT NULL,
  `default_sale_comm_rate` decimal(5,2) DEFAULT NULL,
  PRIMARY KEY (`agent_org_id`,`retailer_org_id`,`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_cs_agent_retailer_sale_comm_variance`;

CREATE TABLE `st_cs_agent_retailer_sale_comm_variance_history` (
  `agent_org_id` int(10) unsigned NOT NULL,
  `retailer_org_id` int(10) unsigned NOT NULL,
  `product_id` int(10) unsigned NOT NULL,
  `sale_comm_variance` decimal(5,2) NOT NULL,
  `date_changed` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_cs_agent_retailer_sale_comm_variance_history`;

CREATE TABLE `st_cs_agt_refund` (
  `transaction_id` bigint(20) unsigned NOT NULL,
  `bo_ref_transaction_id` bigint(20) DEFAULT NULL,
  `mrp_amt` decimal(20,2) NOT NULL,
  `net_amt` decimal(20,2) DEFAULT NULL,
  `cancellation_charges` decimal(20,2) DEFAULT NULL,
  `bo_net_amt` decimal(20,4) DEFAULT NULL,
  `retailer_comm` decimal(10,2) NOT NULL,
  `agent_comm` decimal(10,2) NOT NULL,
  `jv_comm_amt` decimal(10,4) DEFAULT NULL,
  `jv_comm` decimal(10,2) NOT NULL,
  `vat` decimal(10,2) NOT NULL,
  `vat_amt` decimal(10,2) DEFAULT NULL,
  `govt_comm` decimal(10,2) NOT NULL,
  `govt_comm_amt` decimal(10,2) DEFAULT NULL,
  `product_id` int(10) NOT NULL,
  `claim_status` enum('DONE_CLAIM','CLAIM_BAL') NOT NULL,
  `retailer_org_id` int(10) NOT NULL,
  `agent_org_id` int(10) NOT NULL,
  PRIMARY KEY (`transaction_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_cs_agt_refund`;

CREATE TABLE `st_cs_agt_sale` (
  `transaction_id` bigint(20) unsigned NOT NULL,
  `bo_ref_transaction_id` bigint(20) DEFAULT NULL,
  `agent_org_id` int(10) NOT NULL,
  `mrp_amt` decimal(20,2) NOT NULL,
  `net_amt` decimal(20,2) DEFAULT NULL,
  `bo_net_amt` decimal(20,4) DEFAULT NULL,
  `retailer_comm` decimal(10,2) NOT NULL,
  `agent_comm` decimal(10,2) NOT NULL,
  `claim_status` enum('DONE_CLAIM','CLAIM_BAL') NOT NULL,
  `jv_comm` decimal(10,2) NOT NULL,
  `jv_comm_amt` decimal(10,4) DEFAULT NULL,
  `vat` decimal(10,2) NOT NULL,
  `vat_amt` decimal(10,2) DEFAULT NULL,
  `govt_comm` decimal(10,2) NOT NULL,
  `govt_comm_amt` decimal(10,2) DEFAULT NULL,
  `product_id` int(10) NOT NULL,
  `retailer_org_id` int(10) NOT NULL,
  PRIMARY KEY (`transaction_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_cs_agt_sale`;

CREATE TABLE `st_cs_bo_agent_sale_comm_variance` (
  `agent_org_id` int(10) unsigned NOT NULL,
  `product_id` int(10) unsigned NOT NULL,
  `sale_comm_variance` decimal(5,2) DEFAULT NULL,
  `default_sale_comm_rate` decimal(5,2) DEFAULT NULL,
  PRIMARY KEY (`agent_org_id`,`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_cs_bo_agent_sale_comm_variance`;

CREATE TABLE `st_cs_bo_agent_sale_comm_variance_history` (
  `agent_org_id` int(11) unsigned NOT NULL,
  `product_id` int(10) unsigned NOT NULL,
  `sale_comm_variance` decimal(5,2) NOT NULL,
  `date_changed` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_cs_bo_agent_sale_comm_variance_history`;

CREATE TABLE `st_cs_bo_refund` (
  `transaction_id` bigint(20) unsigned NOT NULL,
  `agent_org_id` int(10) NOT NULL,
  `mrp_amt` decimal(20,2) NOT NULL,
  `net_amt` decimal(20,4) DEFAULT NULL,
  `agent_comm` decimal(10,2) NOT NULL,
  `product_id` int(10) NOT NULL,
  `cancellation_charges` decimal(10,2) NOT NULL,
  `jv_comm` decimal(10,2) NOT NULL,
  `jv_comm_amt` decimal(10,4) DEFAULT NULL,
  `vat` decimal(10,2) NOT NULL,
  `vat_amt` decimal(10,2) DEFAULT NULL,
  `govt_comm` decimal(10,2) NOT NULL,
  `govt_comm_amt` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`transaction_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_cs_bo_refund`;

CREATE TABLE `st_cs_bo_sale` (
  `transaction_id` bigint(20) unsigned NOT NULL,
  `mrp_amt` decimal(20,2) NOT NULL,
  `net_amt` decimal(20,4) DEFAULT NULL,
  `jv_comm` decimal(10,2) NOT NULL,
  `jv_comm_amt` decimal(10,4) DEFAULT NULL,
  `vat` decimal(10,2) NOT NULL,
  `vat_amt` decimal(10,2) DEFAULT NULL,
  `govt_comm` decimal(10,2) NOT NULL,
  `govt_comm_amt` decimal(10,2) DEFAULT NULL,
  `product_id` int(10) NOT NULL,
  `agent_org_id` int(10) NOT NULL,
  `agent_comm` decimal(10,2) NOT NULL,
  PRIMARY KEY (`transaction_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_cs_bo_sale`;

CREATE TABLE `st_cs_circle_master` (
  `circle_code` varchar(10) NOT NULL,
  `circle_name` varchar(30) DEFAULT NULL,
  `status` enum('ACTIVE','INACTIVE') DEFAULT NULL,
  PRIMARY KEY (`circle_code`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC;
--rollback drop table `st_cs_circle_master`;

CREATE TABLE `st_cs_menu_master` (
  `menu_id` smallint(10) unsigned NOT NULL AUTO_INCREMENT,
  `action_id` smallint(10) unsigned NOT NULL,
  `menu_name` varchar(50) DEFAULT NULL,
  `menu_disp_name` varchar(50) DEFAULT NULL,
  `parent_menu_id` smallint(10) unsigned NOT NULL,
  `item_order` tinyint(4) DEFAULT NULL,
  `menu_disp_name_fr` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `menu_disp_name_en` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`menu_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
--rollback drop table `st_cs_menu_master`;


CREATE TABLE `st_cs_operator_master` (
  `operator_code` varchar(10) NOT NULL,
  `operator_name` varchar(30) DEFAULT NULL,
  `status` enum('ACTIVE','INACTIVE') DEFAULT NULL,
  UNIQUE KEY `operator_code` (`operator_code`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC;
--rollback drop table `st_cs_operator_master`;

CREATE TABLE `st_cs_priviledge_rep` (
  `action_id` smallint(10) unsigned NOT NULL AUTO_INCREMENT,
  `priv_id` int(10) unsigned NOT NULL,
  `priv_title` varchar(50) CHARACTER SET utf8 NOT NULL DEFAULT '',
  `priv_disp_name` varchar(50) DEFAULT NULL,
  `action_mapping` varchar(50) CHARACTER SET utf8 DEFAULT NULL,
  `parent_priv_id` int(10) unsigned NOT NULL,
  `status` enum('ACTIVE','INACTIVE') DEFAULT NULL,
  `priv_owner` enum('BO','AGENT','RETAILER') DEFAULT NULL,
  `related_to` enum('ACT_MGT','REPORTS','BO_USER_MGT','USER_MGT','AGENT_USER_MGT','ROLE_MGT','RET_USER_MGT','INV_MGT','PROD_MGT','TERMINAL_MGT','MISC') DEFAULT NULL,
  `group_name` varchar(50) DEFAULT NULL,
  `is_start` enum('Y','N') DEFAULT NULL,
  `channel` enum('WEB','TERMINAL','MOBILE') NOT NULL,
  `priv_code` int(10) DEFAULT NULL,
  `hidden` enum('Y','N') DEFAULT NULL,
  `group_name_fr` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `group_name_en` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`action_id`),
  KEY `priv_title` (`priv_title`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
--rollback drop table `st_cs_priviledge_rep`;


CREATE TABLE `st_cs_product_category_master` (
  `category_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `category_code` varchar(50) NOT NULL,
  `description` varchar(100) DEFAULT NULL,
  `status` enum('Active','Inactive') DEFAULT NULL,
  PRIMARY KEY (`category_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC;
--rollback drop table `st_cs_product_category_master`;

CREATE TABLE `st_cs_product_details` (
  `product_id` int(10) NOT NULL,
  `talktime` decimal(10,2) DEFAULT NULL,
  `validity` varchar(20) DEFAULT NULL,
  `admin_fee` decimal(10,2) DEFAULT NULL,
  `service_tax` decimal(10,2) DEFAULT NULL,
  `recharge_instructions` varchar(50) NOT NULL,
  PRIMARY KEY (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC;
--rollback drop table `st_cs_product_details`;

CREATE TABLE `st_cs_product_master` (
  `product_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `product_code` varchar(20) NOT NULL,
  `category_id` int(10) unsigned NOT NULL,
  `description` varchar(50) DEFAULT NULL,
  `operator_code` varchar(50) NOT NULL,
  `circle_code` varchar(30) NOT NULL DEFAULT '*',
  `denomination` varchar(20) NOT NULL,
  `country_code` enum('KEN','TAN') NOT NULL,
  `supplier_name` varchar(50) DEFAULT NULL,
  `unit_price` decimal(10,2) unsigned NOT NULL,
  `retailer_comm` decimal(10,2) unsigned NOT NULL,
  `agent_comm` decimal(10,2) unsigned NOT NULL,
  `jv_comm` decimal(10,2) unsigned NOT NULL,
  `good_cause_comm` decimal(10,2) unsigned NOT NULL,
  `vat_comm` decimal(10,2) DEFAULT NULL,
  `status` enum('ACTIVE','INACTIVE') DEFAULT NULL,
  PRIMARY KEY (`product_id`),
  UNIQUE KEY `circle_index` (`product_code`,`denomination`,`operator_code`,`circle_code`),
  KEY `category_id` (`category_id`),
  CONSTRAINT `st_cs_product_master_ibfk_1` FOREIGN KEY (`category_id`) REFERENCES `st_cs_product_category_master` (`category_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC;
--rollback drop table `st_cs_product_master`;

CREATE TABLE `st_cs_product_name_master` (
  `product_code` varchar(20) DEFAULT NULL,
  `product_name` varchar(30) DEFAULT NULL,
  `status` enum('ACTIVE','INACTIVE') DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC;
--rollback drop table `st_cs_product_name_master`;

CREATE TABLE `st_cs_refund_1` (
  `transaction_id` bigint(20) unsigned NOT NULL,
  `product_id` int(10) NOT NULL,
  `retailer_org_id` int(10) NOT NULL,
  `cs_ref_transaction_id` varchar(20) NOT NULL,
  `rms_ref_transaction_id` bigint(20) NOT NULL,
  `claim_status` enum('DONE_CLAIM','CLAIM_BAL') NOT NULL,
  `cancel_reason` enum('CANCEL_SERVER','CANCEL_RET') NOT NULL,
  `mrp_amt` decimal(20,2) NOT NULL,
  `net_amt` decimal(20,4) DEFAULT NULL,
  `agent_net_amt` decimal(20,4) DEFAULT NULL,
  `retailer_comm` decimal(10,2) NOT NULL,
  `agent_comm` decimal(10,2) NOT NULL,
  `jv_comm` decimal(10,2) NOT NULL,
  `jv_comm_cost` decimal(10,4) DEFAULT NULL,
  `vat` decimal(10,2) NOT NULL,
  `vat_amt` decimal(10,4) DEFAULT NULL,
  `govt_comm` decimal(10,2) NOT NULL,
  `govt_comm_amt` decimal(10,4) DEFAULT NULL,
  `agent_ref_transaction_id` bigint(20) NOT NULL,
  `cancellation_charges` decimal(10,2) NOT NULL,
  PRIMARY KEY (`transaction_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_cs_refund_1`;

CREATE TABLE `st_cs_refund_2` (
  `transaction_id` bigint(20) unsigned NOT NULL,
  `product_id` int(10) NOT NULL,
  `retailer_org_id` int(10) NOT NULL,
  `cs_ref_transaction_id` varchar(20) NOT NULL,
  `rms_ref_transaction_id` bigint(20) NOT NULL,
  `claim_status` enum('DONE_CLAIM','CLAIM_BAL') NOT NULL,
  `cancel_reason` enum('CANCEL_SERVER','CANCEL_RET') NOT NULL,
  `mrp_amt` decimal(20,2) NOT NULL,
  `net_amt` decimal(20,4) DEFAULT NULL,
  `agent_net_amt` decimal(20,4) DEFAULT NULL,
  `retailer_comm` decimal(10,2) NOT NULL,
  `agent_comm` decimal(10,2) NOT NULL,
  `jv_comm` decimal(10,2) NOT NULL,
  `jv_comm_cost` decimal(10,4) DEFAULT NULL,
  `vat` decimal(10,2) NOT NULL,
  `vat_amt` decimal(10,4) DEFAULT NULL,
  `govt_comm` decimal(10,2) NOT NULL,
  `govt_comm_amt` decimal(10,4) DEFAULT NULL,
  `agent_ref_transaction_id` bigint(20) NOT NULL,
  `cancellation_charges` decimal(10,2) NOT NULL,
  PRIMARY KEY (`transaction_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_cs_refund_2`;

CREATE TABLE `st_cs_refund_3` (
  `transaction_id` bigint(20) unsigned NOT NULL,
  `product_id` int(10) NOT NULL,
  `retailer_org_id` int(10) NOT NULL,
  `cs_ref_transaction_id` varchar(20) NOT NULL,
  `rms_ref_transaction_id` bigint(20) NOT NULL,
  `claim_status` enum('DONE_CLAIM','CLAIM_BAL') NOT NULL,
  `cancel_reason` enum('CANCEL_SERVER','CANCEL_RET') NOT NULL,
  `mrp_amt` decimal(20,2) NOT NULL,
  `net_amt` decimal(20,4) DEFAULT NULL,
  `agent_net_amt` decimal(20,4) DEFAULT NULL,
  `retailer_comm` decimal(10,2) NOT NULL,
  `agent_comm` decimal(10,2) NOT NULL,
  `jv_comm` decimal(10,2) NOT NULL,
  `jv_comm_cost` decimal(10,4) DEFAULT NULL,
  `vat` decimal(10,2) NOT NULL,
  `vat_amt` decimal(10,4) DEFAULT NULL,
  `govt_comm` decimal(10,2) NOT NULL,
  `govt_comm_amt` decimal(10,4) DEFAULT NULL,
  `agent_ref_transaction_id` bigint(20) NOT NULL,
  `cancellation_charges` decimal(10,2) NOT NULL,
  PRIMARY KEY (`transaction_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_cs_refund_3`;

CREATE TABLE `st_cs_refund_4` (
  `transaction_id` bigint(20) unsigned NOT NULL,
  `product_id` int(10) NOT NULL,
  `retailer_org_id` int(10) NOT NULL,
  `cs_ref_transaction_id` varchar(20) NOT NULL,
  `rms_ref_transaction_id` bigint(20) NOT NULL,
  `claim_status` enum('DONE_CLAIM','CLAIM_BAL') NOT NULL,
  `cancel_reason` enum('CANCEL_SERVER','CANCEL_RET') NOT NULL,
  `mrp_amt` decimal(20,2) NOT NULL,
  `net_amt` decimal(20,4) DEFAULT NULL,
  `agent_net_amt` decimal(20,4) DEFAULT NULL,
  `retailer_comm` decimal(10,2) NOT NULL,
  `agent_comm` decimal(10,2) NOT NULL,
  `jv_comm` decimal(10,2) NOT NULL,
  `jv_comm_cost` decimal(10,4) DEFAULT NULL,
  `vat` decimal(10,2) NOT NULL,
  `vat_amt` decimal(10,4) DEFAULT NULL,
  `govt_comm` decimal(10,2) NOT NULL,
  `govt_comm_amt` decimal(10,4) DEFAULT NULL,
  `agent_ref_transaction_id` bigint(20) NOT NULL,
  `cancellation_charges` decimal(10,2) NOT NULL,
  PRIMARY KEY (`transaction_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_cs_refund_4`;

CREATE TABLE `st_cs_refund_5` (
  `transaction_id` bigint(20) unsigned NOT NULL,
  `product_id` int(10) NOT NULL,
  `retailer_org_id` int(10) NOT NULL,
  `cs_ref_transaction_id` varchar(20) NOT NULL,
  `rms_ref_transaction_id` bigint(20) NOT NULL,
  `claim_status` enum('DONE_CLAIM','CLAIM_BAL') NOT NULL,
  `cancel_reason` enum('CANCEL_SERVER','CANCEL_RET') NOT NULL,
  `mrp_amt` decimal(20,2) NOT NULL,
  `net_amt` decimal(20,4) DEFAULT NULL,
  `agent_net_amt` decimal(20,4) DEFAULT NULL,
  `retailer_comm` decimal(10,2) NOT NULL,
  `agent_comm` decimal(10,2) NOT NULL,
  `jv_comm` decimal(10,2) NOT NULL,
  `jv_comm_cost` decimal(10,4) DEFAULT NULL,
  `vat` decimal(10,2) NOT NULL,
  `vat_amt` decimal(10,4) DEFAULT NULL,
  `govt_comm` decimal(10,2) NOT NULL,
  `govt_comm_amt` decimal(10,4) DEFAULT NULL,
  `agent_ref_transaction_id` bigint(20) NOT NULL,
  `cancellation_charges` decimal(10,2) NOT NULL,
  PRIMARY KEY (`transaction_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_cs_refund_5`;

CREATE TABLE `st_cs_refund_6` (
  `transaction_id` bigint(20) unsigned NOT NULL,
  `product_id` int(10) NOT NULL,
  `retailer_org_id` int(10) NOT NULL,
  `cs_ref_transaction_id` varchar(20) NOT NULL,
  `rms_ref_transaction_id` bigint(20) NOT NULL,
  `claim_status` enum('DONE_CLAIM','CLAIM_BAL') NOT NULL,
  `cancel_reason` enum('CANCEL_SERVER','CANCEL_RET') NOT NULL,
  `mrp_amt` decimal(20,2) NOT NULL,
  `net_amt` decimal(20,4) DEFAULT NULL,
  `agent_net_amt` decimal(20,4) DEFAULT NULL,
  `retailer_comm` decimal(10,2) NOT NULL,
  `agent_comm` decimal(10,2) NOT NULL,
  `jv_comm` decimal(10,2) NOT NULL,
  `jv_comm_cost` decimal(10,4) DEFAULT NULL,
  `vat` decimal(10,2) NOT NULL,
  `vat_amt` decimal(10,4) DEFAULT NULL,
  `govt_comm` decimal(10,2) NOT NULL,
  `govt_comm_amt` decimal(10,4) DEFAULT NULL,
  `agent_ref_transaction_id` bigint(20) NOT NULL,
  `cancellation_charges` decimal(10,2) NOT NULL,
  PRIMARY KEY (`transaction_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_cs_refund_6`;

CREATE TABLE `st_cs_refund_7` (
  `transaction_id` bigint(20) unsigned NOT NULL,
  `product_id` int(10) NOT NULL,
  `retailer_org_id` int(10) NOT NULL,
  `cs_ref_transaction_id` varchar(20) NOT NULL,
  `rms_ref_transaction_id` bigint(20) NOT NULL,
  `claim_status` enum('DONE_CLAIM','CLAIM_BAL') NOT NULL,
  `cancel_reason` enum('CANCEL_SERVER','CANCEL_RET') NOT NULL,
  `mrp_amt` decimal(20,2) NOT NULL,
  `net_amt` decimal(20,4) DEFAULT NULL,
  `agent_net_amt` decimal(20,4) DEFAULT NULL,
  `retailer_comm` decimal(10,2) NOT NULL,
  `agent_comm` decimal(10,2) NOT NULL,
  `jv_comm` decimal(10,2) NOT NULL,
  `jv_comm_cost` decimal(10,4) DEFAULT NULL,
  `vat` decimal(10,2) NOT NULL,
  `vat_amt` decimal(10,4) DEFAULT NULL,
  `govt_comm` decimal(10,2) NOT NULL,
  `govt_comm_amt` decimal(10,4) DEFAULT NULL,
  `agent_ref_transaction_id` bigint(20) NOT NULL,
  `cancellation_charges` decimal(10,2) NOT NULL,
  PRIMARY KEY (`transaction_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_cs_refund_7`;

CREATE TABLE `st_cs_refund_8` (
  `transaction_id` bigint(20) unsigned NOT NULL,
  `product_id` int(10) NOT NULL,
  `retailer_org_id` int(10) NOT NULL,
  `cs_ref_transaction_id` varchar(20) NOT NULL,
  `rms_ref_transaction_id` bigint(20) NOT NULL,
  `claim_status` enum('DONE_CLAIM','CLAIM_BAL') NOT NULL,
  `cancel_reason` enum('CANCEL_SERVER','CANCEL_RET') NOT NULL,
  `mrp_amt` decimal(20,2) NOT NULL,
  `net_amt` decimal(20,4) DEFAULT NULL,
  `agent_net_amt` decimal(20,4) DEFAULT NULL,
  `retailer_comm` decimal(10,2) NOT NULL,
  `agent_comm` decimal(10,2) NOT NULL,
  `jv_comm` decimal(10,2) NOT NULL,
  `jv_comm_cost` decimal(10,4) DEFAULT NULL,
  `vat` decimal(10,2) NOT NULL,
  `vat_amt` decimal(10,4) DEFAULT NULL,
  `govt_comm` decimal(10,2) NOT NULL,
  `govt_comm_amt` decimal(10,4) DEFAULT NULL,
  `agent_ref_transaction_id` bigint(20) NOT NULL,
  `cancellation_charges` decimal(10,2) NOT NULL,
  PRIMARY KEY (`transaction_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_cs_refund_8`;

CREATE TABLE `st_cs_ret_activity_history` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `date` datetime NOT NULL,
  `live_retailers` int(11) DEFAULT NULL,
  `noSale_retailers` int(11) DEFAULT NULL,
  `inactive_retailers` int(11) DEFAULT NULL,
  `terminated_retailers` int(11) DEFAULT NULL,
  `total_sales` double DEFAULT NULL,
  `avg_sale_per_ret` double DEFAULT NULL,
  PRIMARY KEY (`id`,`date`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
--rollback drop table `st_cs_ret_activity_history`;

CREATE TABLE `st_cs_sale_1` (
  `transaction_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `product_id` int(10) NOT NULL,
  `retailer_org_id` int(10) NOT NULL,
  `cs_ref_transaction_id` varchar(20) NOT NULL,
  `mrp_amt` decimal(20,2) NOT NULL,
  `net_amt` decimal(20,4) DEFAULT NULL,
  `agent_net_amt` decimal(20,4) DEFAULT NULL,
  `retailer_comm` decimal(10,2) NOT NULL,
  `agent_comm` decimal(10,2) NOT NULL,
  `jv_comm` decimal(10,2) NOT NULL,
  `jv_comm_amt` decimal(10,4) DEFAULT NULL,
  `vat` decimal(10,2) NOT NULL,
  `vat_amt` decimal(10,4) DEFAULT NULL,
  `govt_comm` decimal(10,2) NOT NULL,
  `govt_comm_amt` decimal(10,4) DEFAULT NULL,
  `multiple` int(10) NOT NULL,
  `purchase_channel` enum('MOBILE','WEB','TERMINAL') NOT NULL,
  `claim_status` enum('DONE_CLAIM','CLAIM_BAL') NOT NULL,
  `agent_ref_transaction_id` bigint(20) NOT NULL,
  PRIMARY KEY (`transaction_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_cs_sale_1`;

CREATE TABLE `st_cs_sale_2` (
  `transaction_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `product_id` int(10) NOT NULL,
  `retailer_org_id` int(10) NOT NULL,
  `cs_ref_transaction_id` varchar(20) NOT NULL,
  `mrp_amt` decimal(20,2) NOT NULL,
  `net_amt` decimal(20,4) DEFAULT NULL,
  `agent_net_amt` decimal(20,4) DEFAULT NULL,
  `retailer_comm` decimal(10,2) NOT NULL,
  `agent_comm` decimal(10,2) NOT NULL,
  `jv_comm` decimal(10,2) NOT NULL,
  `jv_comm_amt` decimal(10,4) DEFAULT NULL,
  `vat` decimal(10,2) NOT NULL,
  `vat_amt` decimal(10,4) DEFAULT NULL,
  `govt_comm` decimal(10,2) NOT NULL,
  `govt_comm_amt` decimal(10,4) DEFAULT NULL,
  `multiple` int(10) NOT NULL,
  `purchase_channel` enum('MOBILE','WEB','TERMINAL') NOT NULL,
  `claim_status` enum('DONE_CLAIM','CLAIM_BAL') NOT NULL,
  `agent_ref_transaction_id` bigint(20) NOT NULL,
  PRIMARY KEY (`transaction_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_cs_sale_2`;

CREATE TABLE `st_cs_sale_3` (
  `transaction_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `product_id` int(10) NOT NULL,
  `retailer_org_id` int(10) NOT NULL,
  `cs_ref_transaction_id` varchar(20) NOT NULL,
  `mrp_amt` decimal(20,2) NOT NULL,
  `net_amt` decimal(20,4) DEFAULT NULL,
  `agent_net_amt` decimal(20,4) DEFAULT NULL,
  `retailer_comm` decimal(10,2) NOT NULL,
  `agent_comm` decimal(10,2) NOT NULL,
  `jv_comm` decimal(10,2) NOT NULL,
  `jv_comm_amt` decimal(10,4) DEFAULT NULL,
  `vat` decimal(10,2) NOT NULL,
  `vat_amt` decimal(10,4) DEFAULT NULL,
  `govt_comm` decimal(10,2) NOT NULL,
  `govt_comm_amt` decimal(10,4) DEFAULT NULL,
  `multiple` int(10) NOT NULL,
  `purchase_channel` enum('MOBILE','WEB','TERMINAL') NOT NULL,
  `claim_status` enum('DONE_CLAIM','CLAIM_BAL') NOT NULL,
  `agent_ref_transaction_id` bigint(20) NOT NULL,
  PRIMARY KEY (`transaction_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_cs_sale_3`;

CREATE TABLE `st_cs_sale_4` (
  `transaction_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `product_id` int(10) NOT NULL,
  `retailer_org_id` int(10) NOT NULL,
  `cs_ref_transaction_id` varchar(20) NOT NULL,
  `mrp_amt` decimal(20,2) NOT NULL,
  `net_amt` decimal(20,4) DEFAULT NULL,
  `agent_net_amt` decimal(20,4) DEFAULT NULL,
  `retailer_comm` decimal(10,2) NOT NULL,
  `agent_comm` decimal(10,2) NOT NULL,
  `jv_comm` decimal(10,2) NOT NULL,
  `jv_comm_amt` decimal(10,4) DEFAULT NULL,
  `vat` decimal(10,2) NOT NULL,
  `vat_amt` decimal(10,4) DEFAULT NULL,
  `govt_comm` decimal(10,2) NOT NULL,
  `govt_comm_amt` decimal(10,4) DEFAULT NULL,
  `multiple` int(10) NOT NULL,
  `purchase_channel` enum('MOBILE','WEB','TERMINAL') NOT NULL,
  `claim_status` enum('DONE_CLAIM','CLAIM_BAL') NOT NULL,
  `agent_ref_transaction_id` bigint(20) NOT NULL,
  PRIMARY KEY (`transaction_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_cs_sale_4`;

CREATE TABLE `st_cs_sale_5` (
  `transaction_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `product_id` int(10) NOT NULL,
  `retailer_org_id` int(10) NOT NULL,
  `cs_ref_transaction_id` varchar(20) NOT NULL,
  `mrp_amt` decimal(20,2) NOT NULL,
  `net_amt` decimal(20,4) DEFAULT NULL,
  `agent_net_amt` decimal(20,4) DEFAULT NULL,
  `retailer_comm` decimal(10,2) NOT NULL,
  `agent_comm` decimal(10,2) NOT NULL,
  `jv_comm` decimal(10,2) NOT NULL,
  `jv_comm_amt` decimal(10,4) DEFAULT NULL,
  `vat` decimal(10,2) NOT NULL,
  `vat_amt` decimal(10,4) DEFAULT NULL,
  `govt_comm` decimal(10,2) NOT NULL,
  `govt_comm_amt` decimal(10,4) DEFAULT NULL,
  `multiple` int(10) NOT NULL,
  `purchase_channel` enum('MOBILE','WEB','TERMINAL') NOT NULL,
  `claim_status` enum('DONE_CLAIM','CLAIM_BAL') NOT NULL,
  `agent_ref_transaction_id` bigint(20) NOT NULL,
  PRIMARY KEY (`transaction_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_cs_sale_5`;

CREATE TABLE `st_cs_sale_6` (
  `transaction_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `product_id` int(10) NOT NULL,
  `retailer_org_id` int(10) NOT NULL,
  `cs_ref_transaction_id` varchar(20) NOT NULL,
  `mrp_amt` decimal(20,2) NOT NULL,
  `net_amt` decimal(20,4) DEFAULT NULL,
  `agent_net_amt` decimal(20,4) DEFAULT NULL,
  `retailer_comm` decimal(10,2) NOT NULL,
  `agent_comm` decimal(10,2) NOT NULL,
  `jv_comm` decimal(10,2) NOT NULL,
  `jv_comm_amt` decimal(10,4) DEFAULT NULL,
  `vat` decimal(10,2) NOT NULL,
  `vat_amt` decimal(10,4) DEFAULT NULL,
  `govt_comm` decimal(10,2) NOT NULL,
  `govt_comm_amt` decimal(10,4) DEFAULT NULL,
  `multiple` int(10) NOT NULL,
  `purchase_channel` enum('MOBILE','WEB','TERMINAL') NOT NULL,
  `claim_status` enum('DONE_CLAIM','CLAIM_BAL') NOT NULL,
  `agent_ref_transaction_id` bigint(20) NOT NULL,
  PRIMARY KEY (`transaction_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_cs_sale_6`;

CREATE TABLE `st_cs_sale_7` (
  `transaction_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `product_id` int(10) NOT NULL,
  `retailer_org_id` int(10) NOT NULL,
  `cs_ref_transaction_id` varchar(20) NOT NULL,
  `mrp_amt` decimal(20,2) NOT NULL,
  `net_amt` decimal(20,4) DEFAULT NULL,
  `agent_net_amt` decimal(20,4) DEFAULT NULL,
  `retailer_comm` decimal(10,2) NOT NULL,
  `agent_comm` decimal(10,2) NOT NULL,
  `jv_comm` decimal(10,2) NOT NULL,
  `jv_comm_amt` decimal(10,4) DEFAULT NULL,
  `vat` decimal(10,2) NOT NULL,
  `vat_amt` decimal(10,4) DEFAULT NULL,
  `govt_comm` decimal(10,2) NOT NULL,
  `govt_comm_amt` decimal(10,4) DEFAULT NULL,
  `multiple` int(10) NOT NULL,
  `purchase_channel` enum('MOBILE','WEB','TERMINAL') NOT NULL,
  `claim_status` enum('DONE_CLAIM','CLAIM_BAL') NOT NULL,
  `agent_ref_transaction_id` bigint(20) NOT NULL,
  PRIMARY KEY (`transaction_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_cs_sale_7`;

CREATE TABLE `st_cs_sale_8` (
  `transaction_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `product_id` int(10) NOT NULL,
  `retailer_org_id` int(10) NOT NULL,
  `cs_ref_transaction_id` varchar(20) NOT NULL,
  `mrp_amt` decimal(20,2) NOT NULL,
  `net_amt` decimal(20,4) DEFAULT NULL,
  `agent_net_amt` decimal(20,4) DEFAULT NULL,
  `retailer_comm` decimal(10,2) NOT NULL,
  `agent_comm` decimal(10,2) NOT NULL,
  `jv_comm` decimal(10,2) NOT NULL,
  `jv_comm_amt` decimal(10,4) DEFAULT NULL,
  `vat` decimal(10,2) NOT NULL,
  `vat_amt` decimal(10,4) DEFAULT NULL,
  `govt_comm` decimal(10,2) NOT NULL,
  `govt_comm_amt` decimal(10,4) DEFAULT NULL,
  `multiple` int(10) NOT NULL,
  `purchase_channel` enum('MOBILE','WEB','TERMINAL') NOT NULL,
  `claim_status` enum('DONE_CLAIM','CLAIM_BAL') NOT NULL,
  `agent_ref_transaction_id` bigint(20) NOT NULL,
  PRIMARY KEY (`transaction_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_cs_sale_8`;

CREATE TABLE `st_dg_adv_msg_master` (
  `msg_id` int(11) NOT NULL AUTO_INCREMENT,
  `date` datetime DEFAULT NULL,
  `creator_user_id` int(11) DEFAULT NULL,
  `msg_text` varchar(2000) DEFAULT NULL,
  `status` enum('ACTIVE','INACTIVE','TERMINATE') DEFAULT NULL,
  `editable` enum('YES','NO') DEFAULT 'YES',
  `msg_for` varchar(20) DEFAULT NULL,
  `msg_location` varchar(100) DEFAULT NULL,
  `activity` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`msg_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
--rollback drop table `st_dg_adv_msg_master`;

CREATE TABLE `st_dg_adv_msg_master_history` (
  `msg_id` int(11) DEFAULT NULL,
  `date` datetime DEFAULT NULL,
  `creator_user_id` int(11) DEFAULT NULL,
  `msg_text` varchar(2000) DEFAULT NULL,
  `msg_for` varchar(20) DEFAULT NULL,
  `status` enum('ACTIVE','INACTIVE','TERMINATE') DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `update_user_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_dg_adv_msg_master_history`;

CREATE TABLE `st_dg_adv_msg_org_mapping` (
  `msg_id` int(11) DEFAULT NULL,
  `org_id` int(11) DEFAULT NULL,
  `service_id` int(11) NOT NULL DEFAULT '0',
  `game_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_dg_adv_msg_org_mapping`;

CREATE TABLE `st_dg_adv_msg_org_mapping_history` (
  `msg_id` int(11) DEFAULT NULL,
  `org_id` int(11) DEFAULT NULL,
  `game_id` int(11) DEFAULT NULL,
  `activity` varchar(30) DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `update_user_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_dg_adv_msg_org_mapping_history`;

CREATE TABLE `st_dg_adv_sms_details` (
  `user_id` int(11) NOT NULL,
  `org_id` int(11) NOT NULL,
  `phn_no` varchar(20) NOT NULL,
  `msg_id` int(11) NOT NULL,
  `status` enum('Sent','Scheduled','Delivered','Failed') NOT NULL DEFAULT 'Sent',
  `time` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
--rollback drop table `st_dg_adv_sms_details`;

CREATE TABLE `st_dg_agent_retailer_pwt_comm_variance_history` (
  `agent_org_id` int(10) unsigned NOT NULL,
  `retailer_org_id` int(10) unsigned NOT NULL,
  `game_id` int(10) unsigned NOT NULL,
  `pwt_comm_variance` decimal(5,2) NOT NULL,
  `date_changed` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_dg_agent_retailer_pwt_comm_variance_history`;

CREATE TABLE `st_dg_agent_retailer_sale_comm_variance_history` (
  `agent_org_id` int(10) unsigned NOT NULL,
  `retialer_org_id` int(10) unsigned NOT NULL,
  `game_id` int(10) unsigned NOT NULL,
  `sale_comm_variance` decimal(5,2) NOT NULL,
  `date_changed` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_dg_agent_retailer_sale_comm_variance_history`;

CREATE TABLE `st_dg_agent_retailer_sale_pwt_comm_variance` (
  `agent_org_id` int(10) unsigned NOT NULL,
  `retailer_org_id` int(10) unsigned NOT NULL,
  `game_id` int(10) unsigned NOT NULL,
  `sale_comm_variance` decimal(5,2) DEFAULT NULL,
  `default_sale_comm_rate` decimal(5,2) DEFAULT NULL,
  `pwt_comm_variance` decimal(5,2) DEFAULT NULL,
  `default_pwt_comm_rate` decimal(5,2) DEFAULT NULL,
  PRIMARY KEY (`agent_org_id`,`retailer_org_id`,`game_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_dg_agent_retailer_sale_pwt_comm_variance`;

CREATE TABLE `st_dg_agt_direct_plr_pwt` (
  `task_id` int(10) unsigned NOT NULL,
  `agent_user_id` int(10) unsigned DEFAULT NULL,
  `agent_org_id` int(10) unsigned DEFAULT NULL,
  `draw_id` int(10) unsigned NOT NULL,
  `panel_id` int(10) unsigned DEFAULT NULL,
  `transaction_id` bigint(10) unsigned NOT NULL,
  `transaction_date` datetime NOT NULL,
  `game_id` int(10) unsigned NOT NULL,
  `player_id` int(10) unsigned NOT NULL,
  `pwt_amt` decimal(20,2) NOT NULL,
  `tax_amt` decimal(20,2) NOT NULL,
  `net_amt` decimal(20,2) NOT NULL,
  `payment_type` enum('CASH','CHEQUE','TPT') DEFAULT NULL,
  `cheque_nbr` varchar(10) CHARACTER SET utf8 DEFAULT NULL,
  `cheque_date` date DEFAULT NULL,
  `drawee_bank` varchar(50) CHARACTER SET utf8 DEFAULT NULL,
  `issuing_party_name` varchar(50) CHARACTER SET utf8 DEFAULT NULL,
  `pwt_claim_status` enum('CLAIM_RET_UNCLM','UNCLM_PWT','UNCLM_CANCELLED','CLAIM_PLR_AGT_UNCLM_DIR','CLAIM_PLR_AGT_CLM','CLAIM_BAL','UNCLAIM_BAL','DONE_CLM') NOT NULL,
  `agt_claim_comm` decimal(20,2) NOT NULL,
  PRIMARY KEY (`transaction_id`),
  KEY `transaction_id` (`transaction_id`),
  KEY `player_id` (`player_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_dg_agt_direct_plr_pwt`;

CREATE TABLE `st_dg_agt_pwt` (
  `agent_user_id` int(10) unsigned NOT NULL,
  `agent_org_id` int(10) unsigned NOT NULL,
  `retailer_org_id` int(10) unsigned NOT NULL,
  `draw_id` int(10) unsigned NOT NULL,
  `panel_id` int(10) unsigned DEFAULT NULL,
  `game_id` int(10) unsigned NOT NULL,
  `transaction_id` bigint(20) unsigned NOT NULL,
  `pwt_amt` decimal(20,2) NOT NULL,
  `comm_amt` decimal(20,2) DEFAULT NULL,
  `net_amt` decimal(20,2) DEFAULT NULL,
  `agt_claim_comm` decimal(20,2) NOT NULL,
  `status` enum('DONE_CLM','CLAIM_BAL','DONE_UNCLM','UNCLAIM_BAL') NOT NULL,
  `govt_claim_comm` decimal(20,2) NOT NULL DEFAULT '0.00',
  PRIMARY KEY (`transaction_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_dg_agt_pwt`;

CREATE TABLE `st_dg_agt_sale` (
  `transaction_id` bigint(20) unsigned NOT NULL,
  `agent_org_id` int(10) unsigned NOT NULL,
  `retailer_org_id` int(10) unsigned NOT NULL,
  `game_id` int(10) unsigned NOT NULL,
  `mrp_amt` decimal(20,2) NOT NULL,
  `retailer_comm` decimal(20,2) NOT NULL,
  `net_amt` decimal(20,2) NOT NULL,
  `agent_comm` decimal(20,2) NOT NULL,
  `bo_ref_transaction_id` bigint(20) unsigned DEFAULT NULL,
  `claim_status` enum('DONE_CLAIM','CLAIM_BAL') NOT NULL,
  `bo_net_amt` decimal(20,2) DEFAULT NULL,
  `vat_amt` decimal(20,4) NOT NULL,
  `taxable_sale` decimal(20,4) NOT NULL,
  `govt_comm` decimal(20,4) NOT NULL,
  PRIMARY KEY (`transaction_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_dg_agt_sale`;

/*Table structure for table `st_dg_agt_sale_refund` */

CREATE TABLE `st_dg_agt_sale_refund` (
  `transaction_id` bigint(20) unsigned NOT NULL,
  `agent_org_id` int(10) unsigned NOT NULL,
  `retailer_org_id` int(10) unsigned NOT NULL,
  `game_id` int(10) unsigned NOT NULL,
  `mrp_amt` decimal(20,2) NOT NULL,
  `retailer_comm` decimal(20,2) NOT NULL,
  `net_amt` decimal(20,2) NOT NULL,
  `agent_comm` decimal(20,2) NOT NULL,
  `bo_net_amt` decimal(20,2) NOT NULL,
  `claim_status` enum('DONE_CLAIM','CLAIM_BAL') NOT NULL,
  `bo_ref_transaction_id` bigint(20) unsigned DEFAULT NULL,
  `vat_amt` decimal(20,4) NOT NULL,
  `taxable_sale` decimal(20,4) NOT NULL,
  `govt_comm` decimal(20,4) NOT NULL,
  `cancellation_charges` decimal(10,2) NOT NULL DEFAULT '0.00',
  PRIMARY KEY (`transaction_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_dg_agt_sale_refund`;

/*Table structure for table `st_dg_approval_req_master` */

CREATE TABLE `st_dg_approval_req_master` (
  `task_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `party_type` enum('BO','AGENT','RETAILER','PLAYER','ANONYMOUS') NOT NULL,
  `request_id` varchar(20) CHARACTER SET utf8 NOT NULL DEFAULT '',
  `party_id` int(10) unsigned DEFAULT NULL,
  `ticket_nbr` bigint(20) unsigned NOT NULL,
  `draw_id` int(10) unsigned NOT NULL,
  `panel_id` int(10) unsigned DEFAULT NULL,
  `game_id` int(10) unsigned NOT NULL,
  `pwt_amt` decimal(20,2) NOT NULL,
  `tax_amt` decimal(20,2) NOT NULL DEFAULT '0.00',
  `net_amt` decimal(20,2) NOT NULL,
  `req_status` enum('REQUESTED','PND_MAS','PND_PAY','PAID','NA','CANCEL') NOT NULL,
  `requester_type` enum('BO','AGENT','RETAILER') NOT NULL,
  `requested_by_user_id` int(10) unsigned NOT NULL,
  `requested_by_org_id` int(10) unsigned NOT NULL,
  `requested_to_org_id` int(10) unsigned NOT NULL,
  `requested_to_type` enum('BO','AGENT','RETAILER') NOT NULL,
  `approved_by_type` enum('BO','AGENT','RETAILER') DEFAULT NULL,
  `approved_by_user_id` int(10) unsigned DEFAULT NULL,
  `approved_by_org_id` int(10) unsigned DEFAULT NULL,
  `pay_req_for_org_type` enum('BO','AGENT','RETAILER') DEFAULT NULL,
  `pay_request_for_org_id` int(10) unsigned DEFAULT NULL,
  `request_date` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `approval_date` datetime DEFAULT NULL,
  `remarks` varchar(100) CHARACTER SET utf8 NOT NULL DEFAULT '',
  `payment_done_by_type` enum('BO','AGENT','RETAILER') DEFAULT NULL,
  `payment_done_by` int(10) unsigned DEFAULT NULL,
  `transaction_id` bigint(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`task_id`),
  UNIQUE KEY `ticket_nbr` (`ticket_nbr`,`draw_id`,`panel_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
--rollback drop table `st_dg_approval_req_master`;


CREATE TABLE `st_dg_approval_req_master_new` (
  `task_id` int(10) unsigned NOT NULL DEFAULT '0',
  `party_type` enum('BO','AGENT','RETAILER','PLAYER','ANONYMOUS') NOT NULL,
  `request_id` varchar(20) CHARACTER SET utf8 NOT NULL DEFAULT '',
  `party_id` int(10) unsigned DEFAULT NULL,
  `ticket_nbr` bigint(20) unsigned NOT NULL,
  `draw_id` int(10) unsigned NOT NULL,
  `panel_id` int(10) unsigned DEFAULT NULL,
  `game_id` int(10) unsigned NOT NULL,
  `pwt_amt` decimal(20,2) NOT NULL,
  `tax_amt` decimal(20,2) NOT NULL DEFAULT '0.00',
  `net_amt` decimal(20,2) NOT NULL,
  `req_status` enum('REQUESTED','PND_MAS','PND_PAY','PAID','NA','CANCEL') NOT NULL,
  `requester_type` enum('BO','AGENT','RETAILER') NOT NULL,
  `requested_by_user_id` int(10) unsigned NOT NULL,
  `requested_by_org_id` int(10) unsigned NOT NULL,
  `requested_to_org_id` int(10) unsigned NOT NULL,
  `requested_to_type` enum('BO','AGENT','RETAILER') NOT NULL,
  `approved_by_type` enum('BO','AGENT','RETAILER') DEFAULT NULL,
  `approved_by_user_id` int(10) unsigned DEFAULT NULL,
  `approved_by_org_id` int(10) unsigned DEFAULT NULL,
  `pay_req_for_org_type` enum('BO','AGENT','RETAILER') DEFAULT NULL,
  `pay_request_for_org_id` int(10) unsigned DEFAULT NULL,
  `request_date` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `approval_date` datetime DEFAULT NULL,
  `remarks` varchar(100) CHARACTER SET utf8 NOT NULL DEFAULT '',
  `payment_done_by_type` enum('BO','AGENT','RETAILER') DEFAULT NULL,
  `payment_done_by` int(10) unsigned DEFAULT NULL,
  `transaction_id` bigint(10) unsigned DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_dg_approval_req_master_new`;

CREATE TABLE `st_dg_bo_agent_pwt_comm_variance_history` (
  `agent_org_id` int(11) unsigned NOT NULL,
  `game_id` int(11) unsigned NOT NULL,
  `pwt_comm_variance` decimal(5,2) NOT NULL,
  `date_changed` datetime NOT NULL DEFAULT '0000-00-00 00:00:00'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_dg_bo_agent_pwt_comm_variance_history`;

CREATE TABLE `st_dg_bo_agent_sale_comm_variance_history` (
  `agent_org_id` int(11) unsigned NOT NULL,
  `game_id` int(11) unsigned NOT NULL,
  `sale_comm_variance` decimal(5,2) NOT NULL,
  `date_changed` datetime NOT NULL DEFAULT '0000-00-00 00:00:00'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_dg_bo_agent_sale_comm_variance_history`;

CREATE TABLE `st_dg_bo_agent_sale_pwt_comm_variance` (
  `agent_org_id` int(10) unsigned NOT NULL,
  `game_id` int(10) unsigned NOT NULL,
  `sale_comm_variance` decimal(5,2) DEFAULT NULL,
  `default_sale_comm_rate` decimal(5,2) DEFAULT NULL,
  `pwt_comm_variance` decimal(5,2) DEFAULT NULL,
  `default_pwt_comm_rate` decimal(5,2) DEFAULT NULL,
  PRIMARY KEY (`agent_org_id`,`game_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_dg_bo_agent_sale_pwt_comm_variance`;

CREATE TABLE `st_dg_bo_direct_plr_pwt` (
  `task_id` int(10) unsigned NOT NULL,
  `bo_user_id` int(10) unsigned DEFAULT NULL,
  `bo_org_id` int(10) unsigned DEFAULT NULL,
  `draw_id` int(10) unsigned NOT NULL,
  `panel_id` int(10) unsigned DEFAULT NULL,
  `transaction_id` bigint(10) unsigned NOT NULL,
  `transaction_date` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `game_id` int(10) unsigned NOT NULL,
  `player_id` int(10) unsigned NOT NULL,
  `pwt_amt` decimal(20,2) NOT NULL,
  `tax_amt` decimal(20,2) NOT NULL,
  `net_amt` decimal(20,2) NOT NULL,
  `payment_type` enum('CASH','CHEQUE','TPT') DEFAULT NULL,
  `cheque_nbr` varchar(10) CHARACTER SET utf8 DEFAULT NULL,
  `cheque_date` date DEFAULT NULL,
  `drawee_bank` varchar(50) CHARACTER SET utf8 DEFAULT NULL,
  `issuing_party_name` varchar(50) CHARACTER SET utf8 DEFAULT NULL,
  KEY `transaction_id` (`transaction_id`),
  KEY `player_id` (`player_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_dg_bo_direct_plr_pwt`;

CREATE TABLE `st_dg_bo_pwt` (
  `bo_user_id` int(10) unsigned NOT NULL,
  `bo_org_id` int(10) unsigned NOT NULL,
  `agent_org_id` int(10) unsigned NOT NULL,
  `draw_id` int(10) unsigned NOT NULL,
  `panel_id` int(10) unsigned DEFAULT NULL,
  `game_id` int(10) unsigned NOT NULL,
  `transaction_id` bigint(20) unsigned NOT NULL,
  `pwt_amt` decimal(20,2) NOT NULL,
  `comm_amt` decimal(20,2) DEFAULT NULL,
  `net_amt` decimal(20,2) DEFAULT NULL,
  `govt_claim_comm` decimal(20,4) NOT NULL DEFAULT '0.0000',
  PRIMARY KEY (`transaction_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_dg_bo_pwt`;

CREATE TABLE `st_dg_bo_sale` (
  `transaction_id` bigint(20) unsigned NOT NULL,
  `agent_org_id` int(10) unsigned NOT NULL,
  `game_id` int(10) unsigned NOT NULL,
  `mrp_amt` decimal(20,2) NOT NULL,
  `agent_comm` decimal(20,2) NOT NULL,
  `net_amt` decimal(20,2) NOT NULL,
  `vat_amt` decimal(20,4) NOT NULL,
  `taxable_sale` decimal(20,4) NOT NULL,
  `govt_comm` decimal(20,4) NOT NULL,
  PRIMARY KEY (`transaction_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_dg_bo_sale`;

CREATE TABLE `st_dg_bo_sale_refund` (
  `transaction_id` bigint(20) unsigned NOT NULL,
  `agent_org_id` int(10) unsigned NOT NULL,
  `game_id` int(10) unsigned NOT NULL,
  `mrp_amt` decimal(20,2) unsigned NOT NULL,
  `agent_comm` decimal(20,2) NOT NULL,
  `net_amt` decimal(20,2) unsigned NOT NULL,
  `vat_amt` decimal(20,4) NOT NULL,
  `taxable_sale` decimal(20,4) unsigned NOT NULL,
  `govt_comm` decimal(20,4) unsigned NOT NULL,
  `cancellation_charges` decimal(10,2) NOT NULL DEFAULT '0.00',
  PRIMARY KEY (`transaction_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_dg_bo_ticket_cancel`;

CREATE TABLE `st_dg_bo_ticket_cancel` (
  `ret_trans_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `retailer_org_id` int(11) NOT NULL,
  `game_no` int(11) NOT NULL,
  `ticket_no` varchar(20) NOT NULL,
  `cancel_type` enum('BY_NUMBER','BY_TRANSACTION') DEFAULT NULL,
  `ticket_amt` double(10,2) NOT NULL,
  `reason` enum('WRONG_NUMBER_PICKED','WRONG_AMOUNT_PLAYED','TICKET_NOT_PRINTED','OTHER') DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_dg_bo_ticket_cancel`;

CREATE TABLE `st_dg_draw_status_change_history` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `action` enum('FREEZE','CHANGE DRAW TIME','CHANGE FREEZE TIME','CANCEL','CLAIM ALLOW','CLAIM HOLD') NOT NULL,
  `date` datetime NOT NULL,
  `remarks` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2251 DEFAULT CHARSET=latin1 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC;
--rollback drop table `st_dg_draw_status_change_history`;

CREATE TABLE `st_dg_game_govt_comm_history` (
  `game_id` int(10) unsigned NOT NULL,
  `govt_comm_rate` decimal(5,2) NOT NULL,
  `govt_comm_type` enum('SALE','PWT') DEFAULT NULL,
  `date_changed` datetime NOT NULL DEFAULT '0000-00-00 00:00:00'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_dg_game_govt_comm_history`;

CREATE TABLE `st_dg_game_master` (
  `game_id` int(10) unsigned NOT NULL,
  `game_nbr` smallint(5) unsigned NOT NULL,
  `game_name` varchar(50) NOT NULL,
  `game_name_dev` varchar(50) DEFAULT NULL,
  `agent_sale_comm_rate` decimal(10,2) unsigned NOT NULL,
  `agent_pwt_comm_rate` decimal(10,2) unsigned NOT NULL,
  `retailer_sale_comm_rate` decimal(10,2) NOT NULL,
  `retailer_pwt_comm_rate` decimal(10,2) NOT NULL,
  `vat_amt` decimal(5,2) unsigned NOT NULL,
  `govt_comm` decimal(5,2) NOT NULL,
  `govt_comm_pwt` decimal(5,2) DEFAULT NULL,
  `high_prize_amt` decimal(20,2) NOT NULL,
  `prize_payout_ratio` decimal(5,2) unsigned NOT NULL,
  `game_status` enum('OPEN','SALE_HOLD','SALE_CLOSE') DEFAULT NULL,
  `offline_freeze_time` int(10) unsigned NOT NULL,
  `is_offline` enum('Y','N') NOT NULL,
  `raffle_ticket_type` enum('ORIGINAL','REFERENCE') DEFAULT NULL,
  `closing_time` datetime DEFAULT NULL,
  `display_order` tinyint(4) DEFAULT NULL,
  `is_sale_allowed_through_terminal` enum('Y','N') DEFAULT NULL,
  `bonus_ball_enable` enum('Y','N') DEFAULT NULL,
  PRIMARY KEY (`game_id`),
  KEY `game_nbr` (`game_nbr`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_dg_game_master`;

CREATE TABLE `st_dg_invalid_ticket_history` (
  `task_id` int(10) unsigned NOT NULL,
  `ticket_nbr` bigint(20) unsigned NOT NULL,
  `date` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `user_id` int(10) unsigned DEFAULT NULL,
  `user_org_id` int(10) unsigned DEFAULT NULL,
  `remarks` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`task_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_dg_invalid_ticket_history`;

CREATE TABLE `st_dg_last_sold_ticket` (
  `ret_org_id` int(11) NOT NULL,
  `terminal_ticket_number` bigint(20) NOT NULL,
  `terminal_ticket_status` enum('CANCELLED','SOLD','PRINTED') DEFAULT NULL,
  `web_ticket_number` bigint(20) NOT NULL,
  `web_ticket_status` enum('CANCELLED','SOLD','PRINTED') DEFAULT NULL,
  PRIMARY KEY (`ret_org_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC;
--rollback drop table `st_dg_last_sold_ticket`;

CREATE TABLE `st_dg_menu_master` (
  `menu_id` smallint(5) unsigned NOT NULL AUTO_INCREMENT,
  `action_id` smallint(5) unsigned DEFAULT NULL,
  `menu_name` varchar(50) DEFAULT NULL,
  `menu_disp_name` varchar(50) DEFAULT NULL,
  `parent_menu_id` smallint(5) unsigned DEFAULT NULL,
  `item_order` tinyint(3) unsigned DEFAULT NULL,
  `menu_disp_name_fr` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `menu_disp_name_en` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`menu_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
--rollback drop table `st_dg_menu_master`;


CREATE TABLE `st_dg_offline_files_2` (
  `file_id` int(11) NOT NULL AUTO_INCREMENT,
  `game_no` tinyint(3) unsigned DEFAULT NULL,
  `game_id` tinyint(3) unsigned DEFAULT NULL,
  `retailer_user_id` int(11) DEFAULT NULL,
  `retailer_org_id` int(11) DEFAULT NULL,
  `upload_time` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `filename` varchar(50) DEFAULT NULL,
  `file` longblob,
  `reference_no` varchar(20) DEFAULT NULL,
  `status` enum('UPLOADED','SUCCESS','ERROR','APPROVED','DECLINED','LATE_UPLOAD','ERROR_RESOLVED','EMPTY') DEFAULT NULL,
  `updateby` int(11) DEFAULT NULL,
  `updatetime` datetime DEFAULT NULL,
  `uploaded_by` int(11) DEFAULT NULL,
  PRIMARY KEY (`file_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_dg_offline_files_2`;

CREATE TABLE `st_dg_offline_files_3` (
  `file_id` int(11) NOT NULL AUTO_INCREMENT,
  `game_no` tinyint(3) unsigned DEFAULT NULL,
  `game_id` tinyint(3) unsigned DEFAULT NULL,
  `retailer_user_id` int(11) DEFAULT NULL,
  `retailer_org_id` int(11) DEFAULT NULL,
  `upload_time` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `filename` varchar(50) DEFAULT NULL,
  `file` longblob,
  `reference_no` varchar(20) DEFAULT NULL,
  `status` enum('UPLOADED','SUCCESS','ERROR','APPROVED','DECLINED','LATE_UPLOAD','ERROR_RESOLVED','EMPTY') DEFAULT NULL,
  `updateby` int(11) DEFAULT NULL,
  `updatetime` datetime DEFAULT NULL,
  `uploaded_by` int(11) DEFAULT NULL,
  PRIMARY KEY (`file_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_dg_offline_files_3`;

CREATE TABLE `st_dg_offline_files_6` (
  `file_id` int(11) NOT NULL AUTO_INCREMENT,
  `game_no` tinyint(3) unsigned DEFAULT NULL,
  `game_id` tinyint(3) unsigned DEFAULT NULL,
  `retailer_user_id` int(11) DEFAULT NULL,
  `retailer_org_id` int(11) DEFAULT NULL,
  `upload_time` datetime NOT NULL,
  `filename` varchar(50) DEFAULT NULL,
  `file` longblob,
  `reference_no` varchar(20) DEFAULT NULL,
  `status` enum('UPLOADED','SUCCESS','ERROR','APPROVED','DECLINED','LATE_UPLOAD','ERROR_RESOLVED','EMPTY') DEFAULT NULL,
  `updateby` int(11) DEFAULT NULL,
  `updatetime` datetime DEFAULT NULL,
  `uploaded_by` int(11) DEFAULT NULL,
  PRIMARY KEY (`file_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_dg_offline_files_6`;

CREATE TABLE `st_dg_printed_tickets_1` (
  `auto_id` bigint(10) NOT NULL AUTO_INCREMENT,
  `retailer_org_id` int(10) DEFAULT NULL,
  `ticket_nbr` bigint(20) unsigned DEFAULT '0',
  `channel` enum('WEB','TERMINAL') DEFAULT NULL,
  `notification_time` datetime NOT NULL,
  `action_name` char(50) NOT NULL,
  PRIMARY KEY (`auto_id`)
) ENGINE=MyISAM AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
--rollback drop table `st_dg_printed_tickets_1`;

CREATE TABLE `st_dg_printed_tickets_9` (
  `auto_id` bigint(10) NOT NULL AUTO_INCREMENT,
  `retailer_org_id` int(10) DEFAULT NULL,
  `ticket_nbr` bigint(20) unsigned DEFAULT '0',
  `channel` enum('WEB','TERMINAL') DEFAULT NULL,
  `notification_time` datetime NOT NULL,
  `action_name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`auto_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_dg_printed_tickets_9`;

CREATE TABLE `st_dg_printed_tickets_4` (
  `auto_id` bigint(10) NOT NULL AUTO_INCREMENT,
  `retailer_org_id` int(10) DEFAULT NULL,
  `ticket_nbr` bigint(20) unsigned DEFAULT '0',
  `channel` enum('WEB','TERMINAL') DEFAULT NULL,
  `notification_time` datetime NOT NULL,
  `action_name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`auto_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
--rollback drop table `st_dg_printed_tickets_4`;

CREATE TABLE `st_dg_printed_tickets_7` (
  `auto_id` bigint(10) NOT NULL AUTO_INCREMENT,
  `retailer_org_id` int(10) DEFAULT NULL,
  `ticket_nbr` bigint(20) unsigned DEFAULT '0',
  `channel` enum('WEB','TERMINAL') DEFAULT NULL,
  `notification_time` datetime NOT NULL,
  `action_name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`auto_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
--rollback drop table `st_dg_printed_tickets_7`;

CREATE TABLE `st_dg_printed_tickets_6` (
  `auto_id` bigint(10) NOT NULL AUTO_INCREMENT,
  `retailer_org_id` int(10) DEFAULT NULL,
  `ticket_nbr` bigint(20) unsigned DEFAULT '0',
  `channel` enum('WEB','TERMINAL') DEFAULT NULL,
  `notification_time` datetime NOT NULL,
  `action_name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`auto_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
--rollback drop table `st_dg_printed_tickets_6`;

CREATE TABLE `st_dg_printed_tickets_3` (
  `auto_id` bigint(10) NOT NULL AUTO_INCREMENT,
  `retailer_org_id` int(10) DEFAULT NULL,
  `ticket_nbr` bigint(20) unsigned DEFAULT '0',
  `channel` enum('WEB','TERMINAL') DEFAULT NULL,
  `notification_time` datetime NOT NULL,
  `action_name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`auto_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
--rollback drop table `st_dg_printed_tickets_3`;

CREATE TABLE `st_dg_printed_tickets_8` (
  `auto_id` bigint(10) NOT NULL AUTO_INCREMENT,
  `retailer_org_id` int(10) DEFAULT NULL,
  `ticket_nbr` bigint(20) unsigned DEFAULT '0',
  `channel` enum('WEB','TERMINAL') DEFAULT NULL,
  `notification_time` datetime NOT NULL,
  `action_name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`auto_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
--rollback drop table `st_dg_printed_tickets_8`;


CREATE TABLE `st_dg_printed_tickets_2` (
  `auto_id` bigint(10) NOT NULL AUTO_INCREMENT,
  `retailer_org_id` int(10) DEFAULT NULL,
  `ticket_nbr` bigint(20) unsigned DEFAULT '0',
  `channel` enum('WEB','TERMINAL') DEFAULT NULL,
  `notification_time` datetime NOT NULL,
  `action_name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`auto_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
--rollback drop table `st_dg_printed_tickets_2`;


CREATE TABLE `st_dg_printed_tickets_5` (
  `auto_id` bigint(10) NOT NULL AUTO_INCREMENT,
  `retailer_org_id` int(10) DEFAULT NULL,
  `ticket_nbr` bigint(20) unsigned DEFAULT '0',
  `channel` enum('WEB','TERMINAL') DEFAULT NULL,
  `notification_time` datetime NOT NULL,
  `action_name` char(50) NOT NULL,
  PRIMARY KEY (`auto_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
--rollback drop table `st_dg_printed_tickets_5`;


CREATE TABLE `st_dg_priviledge_rep` (
  `action_id` smallint(10) unsigned NOT NULL AUTO_INCREMENT,
  `priv_id` int(10) unsigned NOT NULL,
  `priv_title` varchar(50) CHARACTER SET utf8 NOT NULL DEFAULT '',
  `priv_disp_name` varchar(50) DEFAULT NULL,
  `action_mapping` varchar(50) CHARACTER SET utf8 NOT NULL,
  `parent_priv_id` int(10) unsigned DEFAULT NULL,
  `status` enum('ACTIVE','INACTIVE') DEFAULT NULL,
  `priv_owner` enum('BO','AGENT','RETAILER') DEFAULT NULL,
  `related_to` enum('GAME_MGT','PWT','PLAY_MGT','REPORTS','DRAW_MGT','MISC') DEFAULT NULL,
  `group_name` varchar(50) DEFAULT NULL,
  `is_start` enum('Y','N') DEFAULT NULL,
  `channel` enum('WEB','TERMINAL','MOBILE') NOT NULL,
  `priv_code` int(10) DEFAULT NULL,
  `hidden` enum('Y','N') DEFAULT NULL,
  `group_name_fr` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `group_name_en` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `is_audit_trail_display` enum('Y','N') DEFAULT 'N',
  PRIMARY KEY (`action_id`),
  KEY `priv_title` (`priv_title`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
--rollback drop table `st_dg_priviledge_rep`;


CREATE TABLE `st_dg_promo_scheme` (
  `scheme_id` int(20) NOT NULL AUTO_INCREMENT,
  `sale_game_id` double DEFAULT NULL,
  `promo_game_id` double DEFAULT NULL,
  `sale_ticket_amt` decimal(12,0) DEFAULT NULL,
  `valid_for_draw` varchar(21) DEFAULT NULL,
  `promo_game_type` varchar(51) DEFAULT NULL,
  `status` varchar(24) DEFAULT NULL,
  `promo_ticket_type` varchar(27) DEFAULT NULL,
  `no_of_free_tickets` tinyint(4) DEFAULT NULL,
  `no_of_draws` tinyint(4) DEFAULT NULL,
  `start_time` varchar(30) DEFAULT NULL,
  `end_time` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`scheme_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
--rollback drop table `st_dg_promo_scheme`;

CREATE TABLE `st_dg_promo_scheme_history` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `scheme_id` int(10) unsigned DEFAULT NULL,
  `sale_game_id` int(10) unsigned DEFAULT NULL,
  `no_of_free_tickets` tinyint(4) DEFAULT NULL,
  `doneBy_user_id` int(10) unsigned DEFAULT NULL,
  `updation_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
--rollback drop table `st_dg_promo_scheme_history`;

CREATE TABLE `st_dg_pwt_inv_1` (
  `ticket_nbr` bigint(20) unsigned NOT NULL,
  `draw_id` int(10) unsigned NOT NULL,
  `panel_id` int(10) unsigned NOT NULL,
  `pwt_amt` decimal(20,2) NOT NULL,
  `status` enum('CLAIM_PLR_RET_CLM','CLAIM_PLR_RET_UNCLM','CLAIM_PLR_RET_CLM_DIR','CLAIM_PLR_RET_UNCLM_DIR','CLAIM_RET_CLM_AUTO','PND_PAY','CLAIM_PLR_BO','UNCM_PWT','UNCM_PWT_CANCELLED','CLAIM_RET_CLM','CLAIM_RET_UNCLM','REQUESTED','CLAIM_AGT_AUTO','CLAIM_PLR_AGT_CLM_DIR','CLAIM_AGT_TEMP','CLAIM_PLR_AGT_UNCLM_DIR','CLAIM_AGT','CLAIM_RET_AGT_TEMP','CLAIM_PLR_AGT_TEMP','CLAIM_AGT_CLM_AUTO','CANCELLED_PERMANENT','PND_MAS') NOT NULL,
  `retailer_transaction_id` bigint(20) unsigned DEFAULT NULL,
  `agent_transaction_id` bigint(20) unsigned DEFAULT NULL,
  `bo_transaction_id` bigint(20) unsigned DEFAULT NULL,
  `is_direct_plr` enum('Y','N') DEFAULT NULL,
  `merchant_code` varchar(20) DEFAULT NULL,
  `merchant_trans_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ticket_nbr`,`draw_id`,`panel_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_dg_pwt_inv_1`;

CREATE TABLE `st_dg_pwt_inv_9` (
  `ticket_nbr` bigint(20) unsigned NOT NULL,
  `draw_id` int(10) unsigned NOT NULL,
  `panel_id` int(10) unsigned NOT NULL,
  `pwt_amt` decimal(20,2) NOT NULL,
  `status` enum('CLAIM_PLR_RET_CLM','CLAIM_PLR_RET_UNCLM','CLAIM_PLR_RET_CLM_DIR','CLAIM_PLR_RET_UNCLM_DIR','CLAIM_RET_CLM_AUTO','PND_PAY','CLAIM_PLR_BO','UNCM_PWT','UNCM_PWT_CANCELLED','CLAIM_RET_CLM','CLAIM_RET_UNCLM','REQUESTED','CLAIM_AGT_AUTO','CLAIM_PLR_AGT_CLM_DIR','CLAIM_AGT_TEMP','CLAIM_PLR_AGT_UNCLM_DIR','CLAIM_AGT','CLAIM_RET_AGT_TEMP','CLAIM_PLR_AGT_TEMP','CLAIM_AGT_CLM_AUTO','CANCELLED_PERMANENT','PND_MAS') NOT NULL,
  `retailer_transaction_id` bigint(20) unsigned DEFAULT NULL,
  `agent_transaction_id` bigint(20) unsigned DEFAULT NULL,
  `bo_transaction_id` bigint(20) unsigned DEFAULT NULL,
  `is_direct_plr` enum('Y','N') DEFAULT NULL,
  `merchant_code` varchar(20) DEFAULT NULL,
  `merchant_trans_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ticket_nbr`,`draw_id`,`panel_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_dg_pwt_inv_9`;

CREATE TABLE `st_dg_pwt_inv_4` (
  `ticket_nbr` bigint(20) unsigned NOT NULL,
  `draw_id` int(10) unsigned NOT NULL,
  `panel_id` int(10) unsigned NOT NULL,
  `pwt_amt` decimal(20,2) NOT NULL,
  `status` enum('CLAIM_PLR_RET_CLM','CLAIM_PLR_RET_UNCLM','CLAIM_PLR_RET_CLM_DIR','CLAIM_PLR_RET_UNCLM_DIR','CLAIM_RET_CLM_AUTO','PND_PAY','CLAIM_PLR_BO','UNCM_PWT','UNCM_PWT_CANCELLED','CLAIM_RET_CLM','CLAIM_RET_UNCLM','REQUESTED','CLAIM_AGT_AUTO','CLAIM_PLR_AGT_CLM_DIR','CLAIM_AGT_TEMP','CLAIM_PLR_AGT_UNCLM_DIR','CLAIM_AGT','CLAIM_RET_AGT_TEMP','CLAIM_PLR_AGT_TEMP','CLAIM_AGT_CLM_AUTO','CANCELLED_PERMANENT','PND_MAS') NOT NULL,
  `retailer_transaction_id` bigint(20) unsigned DEFAULT NULL,
  `agent_transaction_id` bigint(20) unsigned DEFAULT NULL,
  `bo_transaction_id` bigint(20) unsigned DEFAULT NULL,
  `is_direct_plr` enum('Y','N') DEFAULT NULL,
  `merchant_code` varchar(20) DEFAULT NULL,
  `merchant_trans_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ticket_nbr`,`draw_id`,`panel_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_dg_pwt_inv_4`;

CREATE TABLE `st_dg_pwt_inv_7` (
  `ticket_nbr` bigint(20) unsigned NOT NULL,
  `draw_id` int(10) unsigned NOT NULL,
  `panel_id` int(10) unsigned NOT NULL,
  `pwt_amt` decimal(20,2) NOT NULL,
  `status` enum('CLAIM_PLR_RET_CLM','CLAIM_PLR_RET_UNCLM','CLAIM_PLR_RET_CLM_DIR','CLAIM_PLR_RET_UNCLM_DIR','CLAIM_RET_CLM_AUTO','PND_PAY','CLAIM_PLR_BO','UNCM_PWT','UNCM_PWT_CANCELLED','CLAIM_RET_CLM','CLAIM_RET_UNCLM','REQUESTED','CLAIM_AGT_AUTO','CLAIM_PLR_AGT_CLM_DIR','CLAIM_AGT_TEMP','CLAIM_PLR_AGT_UNCLM_DIR','CLAIM_AGT','CLAIM_RET_AGT_TEMP','CLAIM_PLR_AGT_TEMP','CLAIM_AGT_CLM_AUTO','CANCELLED_PERMANENT','PND_MAS') NOT NULL,
  `retailer_transaction_id` bigint(20) unsigned DEFAULT NULL,
  `agent_transaction_id` bigint(20) unsigned DEFAULT NULL,
  `bo_transaction_id` bigint(20) unsigned DEFAULT NULL,
  `is_direct_plr` enum('Y','N') DEFAULT NULL,
  `merchant_code` varchar(20) DEFAULT NULL,
  `merchant_trans_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ticket_nbr`,`draw_id`,`panel_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_dg_pwt_inv_7`;

CREATE TABLE `st_dg_pwt_inv_6` (
  `ticket_nbr` bigint(20) unsigned NOT NULL,
  `draw_id` int(10) unsigned NOT NULL,
  `panel_id` int(10) unsigned NOT NULL,
  `pwt_amt` decimal(20,2) NOT NULL,
  `status` enum('CLAIM_PLR_RET_CLM','CLAIM_PLR_RET_UNCLM','CLAIM_PLR_RET_CLM_DIR','CLAIM_PLR_RET_UNCLM_DIR','CLAIM_RET_CLM_AUTO','PND_PAY','CLAIM_PLR_BO','UNCM_PWT','UNCM_PWT_CANCELLED','CLAIM_RET_CLM','CLAIM_RET_UNCLM','REQUESTED','CLAIM_AGT_AUTO','CLAIM_PLR_AGT_CLM_DIR','CLAIM_AGT_TEMP','CLAIM_PLR_AGT_UNCLM_DIR','CLAIM_AGT','CLAIM_RET_AGT_TEMP','CLAIM_PLR_AGT_TEMP','CLAIM_AGT_CLM_AUTO','CANCELLED_PERMANENT','PND_MAS') NOT NULL,
  `retailer_transaction_id` bigint(20) unsigned DEFAULT NULL,
  `agent_transaction_id` bigint(20) unsigned DEFAULT NULL,
  `bo_transaction_id` bigint(20) unsigned DEFAULT NULL,
  `is_direct_plr` enum('Y','N') DEFAULT NULL,
  PRIMARY KEY (`ticket_nbr`,`draw_id`,`panel_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_dg_pwt_inv_6`;

CREATE TABLE `st_dg_pwt_inv_3` (
  `ticket_nbr` bigint(20) unsigned NOT NULL,
  `draw_id` int(10) unsigned NOT NULL,
  `panel_id` int(10) unsigned NOT NULL,
  `pwt_amt` decimal(20,2) NOT NULL,
  `status` enum('CLAIM_PLR_RET_CLM','CLAIM_PLR_RET_UNCLM','CLAIM_PLR_RET_CLM_DIR','CLAIM_PLR_RET_UNCLM_DIR','CLAIM_RET_CLM_AUTO','PND_PAY','CLAIM_PLR_BO','UNCM_PWT','UNCM_PWT_CANCELLED','CLAIM_RET_CLM','CLAIM_RET_UNCLM','REQUESTED','CLAIM_AGT_AUTO','CLAIM_PLR_AGT_CLM_DIR','CLAIM_AGT_TEMP','CLAIM_PLR_AGT_UNCLM_DIR','CLAIM_AGT','CLAIM_RET_AGT_TEMP','CLAIM_PLR_AGT_TEMP','CLAIM_AGT_CLM_AUTO','CANCELLED_PERMANENT','PND_MAS') NOT NULL,
  `retailer_transaction_id` bigint(20) unsigned DEFAULT NULL,
  `agent_transaction_id` bigint(20) unsigned DEFAULT NULL,
  `bo_transaction_id` bigint(20) unsigned DEFAULT NULL,
  `is_direct_plr` enum('Y','N') DEFAULT NULL,
  `merchant_code` varchar(20) DEFAULT NULL,
  `merchant_trans_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ticket_nbr`,`draw_id`,`panel_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_dg_pwt_inv_3`;

CREATE TABLE `st_dg_pwt_inv_8` (
  `ticket_nbr` bigint(20) unsigned NOT NULL,
  `draw_id` int(10) unsigned NOT NULL,
  `panel_id` int(10) unsigned NOT NULL,
  `pwt_amt` decimal(20,2) NOT NULL,
  `status` enum('CLAIM_PLR_RET_CLM','CLAIM_PLR_RET_UNCLM','CLAIM_PLR_RET_CLM_DIR','CLAIM_PLR_RET_UNCLM_DIR','CLAIM_RET_CLM_AUTO','PND_PAY','CLAIM_PLR_BO','UNCM_PWT','UNCM_PWT_CANCELLED','CLAIM_RET_CLM','CLAIM_RET_UNCLM','REQUESTED','CLAIM_AGT_AUTO','CLAIM_PLR_AGT_CLM_DIR','CLAIM_AGT_TEMP','CLAIM_PLR_AGT_UNCLM_DIR','CLAIM_AGT','CLAIM_RET_AGT_TEMP','CLAIM_PLR_AGT_TEMP','CLAIM_AGT_CLM_AUTO','CANCELLED_PERMANENT','PND_MAS') NOT NULL,
  `retailer_transaction_id` bigint(20) unsigned DEFAULT NULL,
  `agent_transaction_id` bigint(20) unsigned DEFAULT NULL,
  `bo_transaction_id` bigint(20) unsigned DEFAULT NULL,
  `is_direct_plr` enum('Y','N') DEFAULT NULL,
  PRIMARY KEY (`ticket_nbr`,`draw_id`,`panel_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_dg_pwt_inv_8`;

CREATE TABLE `st_dg_pwt_inv_2` (
  `ticket_nbr` bigint(20) unsigned NOT NULL,
  `draw_id` int(10) unsigned NOT NULL,
  `panel_id` int(10) unsigned NOT NULL,
  `pwt_amt` decimal(20,2) NOT NULL,
  `status` enum('CLAIM_PLR_RET_CLM','CLAIM_PLR_RET_UNCLM','CLAIM_PLR_RET_CLM_DIR','CLAIM_PLR_RET_UNCLM_DIR','CLAIM_RET_CLM_AUTO','PND_PAY','CLAIM_PLR_BO','UNCM_PWT','UNCM_PWT_CANCELLED','CLAIM_RET_CLM','CLAIM_RET_UNCLM','REQUESTED','CLAIM_AGT_AUTO','CLAIM_PLR_AGT_CLM_DIR','CLAIM_AGT_TEMP','CLAIM_PLR_AGT_UNCLM_DIR','CLAIM_AGT','CLAIM_RET_AGT_TEMP','CLAIM_PLR_AGT_TEMP','CLAIM_AGT_CLM_AUTO','CANCELLED_PERMANENT','PND_MAS') NOT NULL,
  `retailer_transaction_id` bigint(20) unsigned DEFAULT NULL,
  `agent_transaction_id` bigint(20) unsigned DEFAULT NULL,
  `bo_transaction_id` bigint(20) unsigned DEFAULT NULL,
  `is_direct_plr` enum('Y','N') DEFAULT NULL,
  PRIMARY KEY (`ticket_nbr`,`draw_id`,`panel_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_dg_pwt_inv_2`;

CREATE TABLE `st_dg_pwt_inv_5` (
  `ticket_nbr` bigint(20) unsigned NOT NULL,
  `draw_id` int(10) unsigned NOT NULL,
  `panel_id` int(10) unsigned NOT NULL,
  `pwt_amt` decimal(20,2) NOT NULL,
  `status` enum('CLAIM_PLR_RET_CLM','CLAIM_PLR_RET_UNCLM','CLAIM_PLR_RET_CLM_DIR','CLAIM_PLR_RET_UNCLM_DIR','CLAIM_RET_CLM_AUTO','PND_PAY','CLAIM_PLR_BO','UNCM_PWT','UNCM_PWT_CANCELLED','CLAIM_RET_CLM','CLAIM_RET_UNCLM','REQUESTED','CLAIM_AGT_AUTO','CLAIM_PLR_AGT_CLM_DIR','CLAIM_AGT_TEMP','CLAIM_PLR_AGT_UNCLM_DIR','CLAIM_AGT','CLAIM_RET_AGT_TEMP','CLAIM_PLR_AGT_TEMP','CLAIM_AGT_CLM_AUTO','CANCELLED_PERMANENT','PND_MAS') NOT NULL,
  `retailer_transaction_id` bigint(20) unsigned DEFAULT NULL,
  `agent_transaction_id` bigint(20) unsigned DEFAULT NULL,
  `bo_transaction_id` bigint(20) unsigned DEFAULT NULL,
  `is_direct_plr` enum('Y','N') DEFAULT NULL,
  `merchant_code` varchar(20) DEFAULT NULL,
  `merchant_trans_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ticket_nbr`,`draw_id`,`panel_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_dg_pwt_inv_5`;

CREATE TABLE `st_dg_result_sub_master` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `game_id` int(11) DEFAULT NULL,
  `game_no` int(11) DEFAULT NULL,
  `user1_id` int(11) DEFAULT NULL,
  `user2_id` int(11) DEFAULT NULL,
  `user3_id` int(11) DEFAULT NULL,
  `user4_id` int(11) DEFAULT NULL,
  `user5_id` int(11) DEFAULT NULL,
  `user6_id` int(11) DEFAULT NULL,
  `user7_id` int(11) DEFAULT NULL,
  `user8_id` int(11) DEFAULT NULL,
  `user9_id` int(11) DEFAULT NULL,
  `user10_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
--rollback drop table `st_dg_result_sub_master`;

CREATE TABLE `st_dg_ret_direct_plr_pwt` (
  `task_id` int(10) unsigned NOT NULL,
  `retailer_user_id` int(10) unsigned NOT NULL,
  `retailer_org_id` int(10) unsigned NOT NULL,
  `draw_id` int(10) unsigned NOT NULL,
  `panel_id` int(10) unsigned DEFAULT NULL,
  `transaction_id` bigint(10) unsigned NOT NULL,
  `transaction_date` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `game_id` int(10) unsigned NOT NULL,
  `player_id` int(10) unsigned NOT NULL,
  `pwt_amt` decimal(20,2) NOT NULL,
  `tax_amt` decimal(20,2) NOT NULL,
  `net_amt` decimal(20,2) NOT NULL,
  `payment_type` enum('CASH','CHEQUE','TPT') DEFAULT NULL,
  `cheque_nbr` varchar(10) CHARACTER SET utf8 DEFAULT NULL,
  `cheque_date` date DEFAULT NULL,
  `drawee_bank` varchar(50) CHARACTER SET utf8 DEFAULT NULL,
  `issuing_party_name` varchar(50) CHARACTER SET utf8 DEFAULT NULL,
  `pwt_claim_status` enum('CLAIM_RET_UNCLM','UNCLM_PWT','UNCLM_CANCELLED','CLAIM_PLR_AGT_UNCLM_DIR','CLAIM_PLR_AGT_CLM_DIR','CLAIM_AGT_TEMP','CLAIM_PLR_AGT_TEMP','CLAIM_RET_AGT_TEMP','CLAIM_AGT','CLAIM_AGT_AUTO','CLAIM_PLR_BO','CLAIM_PLR_RET_UNCLM','CLAIM_PLR_RET_CLM','CLAIM_PLR_RET_UNCLM_DIR','CLAIM_PLR_RET_CLM_DIR','CLAIM_RET_TEMP','CLAIM_RET_CLM','CLAIM_RET_CLM_AUTO','CLAIM_PLR_RET_TEMP','REQUESTED','PND_MAS','PND_PAY','CANCELLED_PERMANENT','MISSING') NOT NULL,
  `retailer_claim_comm` decimal(20,2) NOT NULL,
  `agt_claim_comm` decimal(20,2) NOT NULL,
  KEY `transaction_id` (`transaction_id`),
  KEY `player_id` (`player_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_dg_ret_direct_plr_pwt`;

CREATE TABLE `st_dg_ret_pending_cancel` (
  `sale_ref_transaction_id` bigint(20) unsigned NOT NULL,
  `ticket_nbr` bigint(20) unsigned DEFAULT NULL,
  `mrp_amt` decimal(20,2) DEFAULT NULL,
  `ret_net_amt` decimal(20,2) DEFAULT NULL,
  `agent_net_amt` decimal(20,2) DEFAULT NULL,
  `game_id` int(11) DEFAULT NULL,
  `cancel_attempt_time` datetime DEFAULT NULL,
  `transaction_date` datetime DEFAULT NULL,
  `retailer_org_id` int(11) DEFAULT NULL,
  `settlement_ref_trans_id` bigint(20) unsigned DEFAULT NULL,
  `reason` enum('CANCEL_EXPIRED','AUTO_CANCEL_FAILED','CANCEL_SERVER_FAILED','OTHERS','DG_ERROR') DEFAULT NULL,
  PRIMARY KEY (`sale_ref_transaction_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_dg_ret_pending_cancel`;

CREATE TABLE `st_dg_ret_pending_sale` (
  `ticket_nbr` bigint(20) unsigned NOT NULL,
  `game_id` int(11) NOT NULL,
  `cancel_time` datetime DEFAULT NULL,
  `mrp_amt` decimal(20,2) NOT NULL,
  `retailer_comm` decimal(20,2) NOT NULL,
  `net_amt` decimal(20,2) NOT NULL,
  `agent_comm` decimal(20,2) NOT NULL,
  `agent_net_amt` decimal(20,2) NOT NULL,
  `retailer_org_id` int(10) unsigned NOT NULL,
  `good_cause_amt` decimal(10,4) NOT NULL,
  `vat_amt` decimal(20,4) NOT NULL,
  `taxable_sale` decimal(20,4) NOT NULL,
  `ref_transaction_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ticket_nbr`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_dg_ret_pending_sale`;

CREATE TABLE `st_dg_ret_pwt_1` (
  `retailer_user_id` int(10) unsigned NOT NULL,
  `retailer_org_id` int(10) unsigned NOT NULL,
  `draw_id` int(10) unsigned NOT NULL,
  `panel_id` int(10) unsigned NOT NULL,
  `game_id` int(10) unsigned NOT NULL,
  `transaction_id` bigint(20) unsigned NOT NULL,
  `pwt_amt` decimal(20,2) NOT NULL,
  `retailer_claim_comm` decimal(20,2) NOT NULL DEFAULT '0.00',
  `agt_claim_comm` decimal(20,2) NOT NULL,
  `govt_claim_comm` decimal(20,2) NOT NULL DEFAULT '0.00',
  `status` enum('CLAIM_BAL','UNCLAIM_BAL','DONE_CLM') NOT NULL,
  PRIMARY KEY (`transaction_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_dg_ret_pwt_1`;

CREATE TABLE `st_dg_ret_pwt_9` (
  `retailer_user_id` int(10) unsigned NOT NULL,
  `retailer_org_id` int(10) unsigned NOT NULL,
  `draw_id` int(10) unsigned NOT NULL,
  `panel_id` int(10) unsigned NOT NULL,
  `game_id` int(10) unsigned NOT NULL,
  `transaction_id` bigint(20) unsigned NOT NULL,
  `pwt_amt` decimal(20,2) NOT NULL,
  `retailer_claim_comm` decimal(20,2) NOT NULL DEFAULT '0.00',
  `agt_claim_comm` decimal(20,2) NOT NULL,
  `govt_claim_comm` decimal(20,2) NOT NULL DEFAULT '0.00',
  `status` enum('CLAIM_BAL','UNCLAIM_BAL','DONE_CLM') NOT NULL,
  PRIMARY KEY (`transaction_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_dg_ret_pwt_9`;

CREATE TABLE `st_dg_ret_pwt_4` (
  `retailer_user_id` int(10) unsigned NOT NULL,
  `retailer_org_id` int(10) unsigned NOT NULL,
  `draw_id` int(10) unsigned NOT NULL,
  `panel_id` int(10) unsigned NOT NULL,
  `game_id` int(10) unsigned NOT NULL,
  `transaction_id` bigint(20) unsigned NOT NULL,
  `pwt_amt` decimal(20,2) NOT NULL,
  `retailer_claim_comm` decimal(20,2) NOT NULL DEFAULT '0.00',
  `agt_claim_comm` decimal(20,2) NOT NULL,
  `govt_claim_comm` decimal(20,2) NOT NULL DEFAULT '0.00',
  `status` enum('CLAIM_BAL','UNCLAIM_BAL','DONE_CLM') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_dg_ret_pwt_4`;

CREATE TABLE `st_dg_ret_pwt_7` (
  `retailer_user_id` int(10) unsigned NOT NULL,
  `retailer_org_id` int(10) unsigned NOT NULL,
  `draw_id` int(10) unsigned NOT NULL,
  `panel_id` int(10) unsigned NOT NULL,
  `game_id` int(10) unsigned NOT NULL,
  `transaction_id` bigint(20) unsigned NOT NULL,
  `pwt_amt` decimal(20,2) NOT NULL,
  `retailer_claim_comm` decimal(20,2) NOT NULL DEFAULT '0.00',
  `agt_claim_comm` decimal(20,2) NOT NULL,
  `govt_claim_comm` decimal(20,2) NOT NULL DEFAULT '0.00',
  `status` enum('CLAIM_BAL','UNCLAIM_BAL','DONE_CLM') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_dg_ret_pwt_7`;

CREATE TABLE `st_dg_ret_pwt_6` (
  `retailer_user_id` int(10) unsigned NOT NULL,
  `retailer_org_id` int(10) unsigned NOT NULL,
  `draw_id` int(10) unsigned NOT NULL,
  `panel_id` int(10) unsigned DEFAULT NULL,
  `game_id` int(10) unsigned NOT NULL,
  `transaction_id` bigint(20) unsigned NOT NULL,
  `pwt_amt` decimal(20,2) NOT NULL,
  `retailer_claim_comm` decimal(20,2) NOT NULL DEFAULT '0.00',
  `agt_claim_comm` decimal(20,2) NOT NULL,
  `govt_claim_comm` decimal(20,2) NOT NULL DEFAULT '0.00',
  `status` enum('CLAIM_BAL','UNCLAIM_BAL','DONE_CLM') NOT NULL,
  PRIMARY KEY (`transaction_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_dg_ret_pwt_6`;

CREATE TABLE `st_dg_ret_pwt_3` (
  `retailer_user_id` int(10) unsigned NOT NULL,
  `retailer_org_id` int(10) unsigned NOT NULL,
  `draw_id` int(10) unsigned NOT NULL,
  `panel_id` int(10) unsigned NOT NULL,
  `game_id` int(10) unsigned NOT NULL,
  `transaction_id` bigint(20) unsigned NOT NULL,
  `pwt_amt` decimal(20,2) NOT NULL,
  `retailer_claim_comm` decimal(20,2) NOT NULL DEFAULT '0.00',
  `agt_claim_comm` decimal(20,2) NOT NULL,
  `govt_claim_comm` decimal(20,2) NOT NULL DEFAULT '0.00',
  `status` enum('CLAIM_BAL','UNCLAIM_BAL','DONE_CLM') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_dg_ret_pwt_3`;

CREATE TABLE `st_dg_ret_pwt_8` (
  `retailer_user_id` int(10) unsigned NOT NULL,
  `retailer_org_id` int(10) unsigned NOT NULL,
  `draw_id` int(10) unsigned NOT NULL,
  `panel_id` int(10) unsigned NOT NULL,
  `game_id` int(10) unsigned NOT NULL,
  `transaction_id` bigint(20) unsigned NOT NULL,
  `pwt_amt` decimal(20,2) NOT NULL,
  `retailer_claim_comm` decimal(20,2) NOT NULL DEFAULT '0.00',
  `agt_claim_comm` decimal(20,2) NOT NULL,
  `govt_claim_comm` decimal(20,2) NOT NULL DEFAULT '0.00',
  `status` enum('CLAIM_BAL','UNCLAIM_BAL','DONE_CLM') NOT NULL,
  PRIMARY KEY (`transaction_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_dg_ret_pwt_8`;

CREATE TABLE `st_dg_ret_pwt_2` (
  `retailer_user_id` int(10) unsigned NOT NULL,
  `retailer_org_id` int(10) unsigned NOT NULL,
  `draw_id` int(10) unsigned NOT NULL,
  `panel_id` int(10) unsigned NOT NULL,
  `game_id` int(10) unsigned NOT NULL,
  `transaction_id` bigint(20) unsigned NOT NULL,
  `pwt_amt` decimal(20,2) NOT NULL,
  `retailer_claim_comm` decimal(20,2) NOT NULL DEFAULT '0.00',
  `agt_claim_comm` decimal(20,2) NOT NULL,
  `govt_claim_comm` decimal(20,2) NOT NULL DEFAULT '0.00',
  `status` enum('CLAIM_BAL','UNCLAIM_BAL','DONE_CLM') NOT NULL,
  PRIMARY KEY (`transaction_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_dg_ret_pwt_2`;

CREATE TABLE `st_dg_ret_pwt_5` (
  `retailer_user_id` int(10) unsigned NOT NULL,
  `retailer_org_id` int(10) unsigned NOT NULL,
  `draw_id` int(10) unsigned NOT NULL,
  `panel_id` int(10) unsigned DEFAULT NULL,
  `game_id` int(10) unsigned NOT NULL,
  `transaction_id` bigint(20) unsigned NOT NULL,
  `pwt_amt` decimal(20,2) NOT NULL,
  `retailer_claim_comm` decimal(20,2) NOT NULL DEFAULT '0.00',
  `agt_claim_comm` decimal(20,2) NOT NULL,
  `govt_claim_comm` decimal(20,2) NOT NULL DEFAULT '0.00',
  `status` enum('CLAIM_BAL','UNCLAIM_BAL','DONE_CLM') NOT NULL,
  PRIMARY KEY (`transaction_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_dg_ret_pwt_5`;

CREATE TABLE `st_dg_ret_sale_1` (
  `transaction_id` bigint(20) unsigned NOT NULL,
  `game_id` int(10) unsigned NOT NULL DEFAULT '0',
  `ticket_nbr` bigint(20) unsigned NOT NULL DEFAULT '0',
  `mrp_amt` decimal(20,2) unsigned NOT NULL,
  `retailer_comm` decimal(20,2) NOT NULL,
  `net_amt` decimal(20,2) NOT NULL,
  `agent_comm` decimal(20,2) NOT NULL,
  `agent_net_amt` decimal(20,2) NOT NULL,
  `retailer_org_id` int(10) NOT NULL,
  `claim_status` enum('DONE_CLAIM','CLAIM_BAL') NOT NULL,
  `good_cause_amt` decimal(10,2) NOT NULL,
  `agent_ref_transaction_id` bigint(20) unsigned DEFAULT NULL,
  `vat_amt` decimal(20,4) NOT NULL,
  `taxable_sale` decimal(20,2) NOT NULL,
  `player_mob_number` varchar(15) DEFAULT NULL,
  `ret_sd_amt` decimal(20,4) NOT NULL COMMENT 'Security deposit amount of retailer',
  `agt_vat_amt` decimal(20,4) NOT NULL,
  PRIMARY KEY (`transaction_id`),
  KEY `ticket_nbr` (`ticket_nbr`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_dg_ret_sale_1`;

CREATE TABLE `st_dg_ret_sale_9` (
  `transaction_id` bigint(20) unsigned NOT NULL,
  `game_id` int(10) unsigned NOT NULL DEFAULT '0',
  `ticket_nbr` bigint(20) unsigned NOT NULL DEFAULT '0',
  `mrp_amt` decimal(20,2) unsigned NOT NULL,
  `retailer_comm` decimal(20,2) NOT NULL,
  `net_amt` decimal(20,2) NOT NULL,
  `agent_comm` decimal(20,2) NOT NULL,
  `agent_net_amt` decimal(20,2) NOT NULL,
  `retailer_org_id` int(10) NOT NULL,
  `claim_status` enum('DONE_CLAIM','CLAIM_BAL') NOT NULL,
  `good_cause_amt` decimal(10,4) NOT NULL,
  `agent_ref_transaction_id` bigint(20) unsigned DEFAULT NULL,
  `vat_amt` decimal(20,4) NOT NULL,
  `taxable_sale` decimal(20,4) NOT NULL,
  `player_mob_number` varchar(15) DEFAULT NULL,
  `ret_sd_amt` decimal(20,4) NOT NULL COMMENT 'Security deposit amount of retailer',
  `agt_vat_amt` decimal(20,4) NOT NULL,
  PRIMARY KEY (`transaction_id`),
  KEY `ticket_nbr` (`ticket_nbr`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_dg_ret_sale_9`;

CREATE TABLE `st_dg_ret_sale_4` (
  `transaction_id` bigint(20) unsigned NOT NULL,
  `game_id` int(10) unsigned NOT NULL DEFAULT '0',
  `ticket_nbr` bigint(20) unsigned NOT NULL DEFAULT '0',
  `mrp_amt` decimal(20,2) unsigned NOT NULL,
  `retailer_comm` decimal(20,2) NOT NULL,
  `net_amt` decimal(20,2) NOT NULL,
  `agent_comm` decimal(20,2) NOT NULL,
  `agent_net_amt` decimal(20,2) NOT NULL,
  `retailer_org_id` int(10) NOT NULL,
  `claim_status` enum('DONE_CLAIM','CLAIM_BAL') NOT NULL,
  `good_cause_amt` decimal(10,4) NOT NULL,
  `agent_ref_transaction_id` bigint(20) unsigned DEFAULT NULL,
  `vat_amt` decimal(20,4) NOT NULL,
  `taxable_sale` decimal(20,4) NOT NULL,
  `player_mob_number` varchar(15) DEFAULT NULL,
  `ret_sd_amt` decimal(20,4) NOT NULL,
  `agt_vat_amt` decimal(20,4) NOT NULL,
  PRIMARY KEY (`transaction_id`),
  KEY `ticket_nbr` (`ticket_nbr`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_dg_ret_sale_4`;

CREATE TABLE `st_dg_ret_sale_7` (
  `transaction_id` bigint(20) unsigned NOT NULL,
  `game_id` int(10) unsigned NOT NULL DEFAULT '0',
  `ticket_nbr` bigint(20) unsigned NOT NULL DEFAULT '0',
  `mrp_amt` decimal(20,2) unsigned NOT NULL,
  `retailer_comm` decimal(20,2) NOT NULL,
  `net_amt` decimal(20,2) NOT NULL,
  `agent_comm` decimal(20,2) NOT NULL,
  `agent_net_amt` decimal(20,2) NOT NULL,
  `retailer_org_id` int(10) NOT NULL,
  `claim_status` enum('DONE_CLAIM','CLAIM_BAL') NOT NULL,
  `good_cause_amt` decimal(10,4) NOT NULL,
  `agent_ref_transaction_id` bigint(20) unsigned DEFAULT NULL,
  `vat_amt` decimal(20,4) NOT NULL,
  `taxable_sale` decimal(20,4) NOT NULL,
  `player_mob_number` varchar(15) DEFAULT NULL,
  `ret_sd_amt` decimal(20,4) DEFAULT NULL,
  `agt_vat_amt` decimal(20,4) DEFAULT NULL,
  PRIMARY KEY (`transaction_id`),
  KEY `ticket_nbr` (`ticket_nbr`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_dg_ret_sale_7`;

CREATE TABLE `st_dg_ret_sale_6` (
  `transaction_id` bigint(20) unsigned NOT NULL,
  `game_id` int(10) unsigned NOT NULL DEFAULT '0',
  `ticket_nbr` bigint(20) unsigned NOT NULL DEFAULT '0',
  `mrp_amt` decimal(20,2) unsigned NOT NULL,
  `retailer_comm` decimal(20,2) NOT NULL,
  `net_amt` decimal(20,2) NOT NULL,
  `agent_comm` decimal(20,2) NOT NULL,
  `agent_net_amt` decimal(20,2) NOT NULL,
  `retailer_org_id` int(10) NOT NULL,
  `claim_status` enum('DONE_CLAIM','CLAIM_BAL') NOT NULL,
  `good_cause_amt` decimal(10,2) NOT NULL,
  `agent_ref_transaction_id` bigint(20) unsigned DEFAULT NULL,
  `vat_amt` decimal(20,4) NOT NULL,
  `taxable_sale` decimal(20,2) NOT NULL,
  `player_mob_number` varchar(15) DEFAULT NULL,
  `ret_sd_amt` decimal(20,4) NOT NULL COMMENT 'Security deposit amount of retailer',
  `agt_vat_amt` decimal(20,4) NOT NULL,
  PRIMARY KEY (`transaction_id`),
  KEY `ticket_nbr` (`ticket_nbr`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_dg_ret_sale_6`;

CREATE TABLE `st_dg_ret_sale_3` (
  `transaction_id` bigint(20) unsigned NOT NULL,
  `game_id` int(10) unsigned NOT NULL DEFAULT '0',
  `ticket_nbr` bigint(20) unsigned NOT NULL DEFAULT '0',
  `mrp_amt` decimal(20,2) unsigned NOT NULL,
  `retailer_comm` decimal(20,2) NOT NULL,
  `net_amt` decimal(20,2) NOT NULL,
  `agent_comm` decimal(20,2) NOT NULL,
  `agent_net_amt` decimal(20,2) NOT NULL,
  `retailer_org_id` int(10) NOT NULL,
  `claim_status` enum('DONE_CLAIM','CLAIM_BAL') NOT NULL,
  `good_cause_amt` decimal(10,4) NOT NULL,
  `agent_ref_transaction_id` bigint(20) unsigned DEFAULT NULL,
  `vat_amt` decimal(20,4) NOT NULL,
  `taxable_sale` decimal(20,4) NOT NULL,
  `player_mob_number` varchar(15) DEFAULT NULL,
  `ret_sd_amt` decimal(20,4) NOT NULL COMMENT 'Security deposit amount of retailer',
  `agt_vat_amt` decimal(20,4) NOT NULL,
  PRIMARY KEY (`transaction_id`),
  KEY `ticket_nbr` (`ticket_nbr`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_dg_ret_sale_3`;

CREATE TABLE `st_dg_ret_sale_8` (
  `transaction_id` bigint(20) unsigned NOT NULL,
  `game_id` int(10) unsigned NOT NULL DEFAULT '0',
  `ticket_nbr` bigint(20) unsigned NOT NULL DEFAULT '0',
  `mrp_amt` decimal(20,2) unsigned NOT NULL,
  `retailer_comm` decimal(20,2) NOT NULL,
  `net_amt` decimal(20,2) NOT NULL,
  `agent_comm` decimal(20,2) NOT NULL,
  `agent_net_amt` decimal(20,2) NOT NULL,
  `retailer_org_id` int(10) NOT NULL,
  `claim_status` enum('DONE_CLAIM','CLAIM_BAL') NOT NULL,
  `good_cause_amt` decimal(10,4) NOT NULL,
  `agent_ref_transaction_id` bigint(20) unsigned DEFAULT NULL,
  `vat_amt` decimal(20,4) NOT NULL,
  `taxable_sale` decimal(20,4) NOT NULL,
  `player_mob_number` varchar(15) DEFAULT NULL,
  `ret_sd_amt` decimal(20,4) NOT NULL COMMENT 'Security deposit amount of retailer',
  `agt_vat_amt` decimal(20,4) NOT NULL,
  PRIMARY KEY (`transaction_id`),
  KEY `ticket_nbr` (`ticket_nbr`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_dg_ret_sale_8`;

CREATE TABLE `st_dg_ret_sale_2` (
  `transaction_id` bigint(20) unsigned NOT NULL,
  `game_id` int(10) unsigned NOT NULL DEFAULT '0',
  `ticket_nbr` bigint(20) unsigned NOT NULL DEFAULT '0',
  `mrp_amt` decimal(20,2) unsigned NOT NULL,
  `retailer_comm` decimal(20,2) NOT NULL,
  `net_amt` decimal(20,2) NOT NULL,
  `agent_comm` decimal(20,2) NOT NULL,
  `agent_net_amt` decimal(20,2) NOT NULL,
  `retailer_org_id` int(10) NOT NULL,
  `claim_status` enum('DONE_CLAIM','CLAIM_BAL') NOT NULL,
  `good_cause_amt` decimal(10,4) NOT NULL,
  `agent_ref_transaction_id` bigint(20) unsigned DEFAULT NULL,
  `vat_amt` decimal(20,4) NOT NULL,
  `taxable_sale` decimal(20,4) NOT NULL,
  `player_mob_number` varchar(15) DEFAULT NULL,
  `ret_sd_amt` decimal(20,4) NOT NULL COMMENT 'Security deposit amount of retailer',
  `agt_vat_amt` decimal(20,4) NOT NULL,
  PRIMARY KEY (`transaction_id`),
  KEY `ticket_nbr` (`ticket_nbr`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_dg_ret_sale_2`;

CREATE TABLE `st_dg_ret_sale_5` (
  `transaction_id` bigint(20) unsigned NOT NULL,
  `game_id` int(10) unsigned NOT NULL DEFAULT '0',
  `ticket_nbr` bigint(20) unsigned NOT NULL DEFAULT '0',
  `mrp_amt` decimal(20,2) unsigned NOT NULL,
  `retailer_comm` decimal(20,2) NOT NULL,
  `net_amt` decimal(20,2) NOT NULL,
  `agent_comm` decimal(20,2) NOT NULL,
  `agent_net_amt` decimal(20,2) NOT NULL,
  `retailer_org_id` int(10) NOT NULL,
  `claim_status` enum('DONE_CLAIM','CLAIM_BAL') NOT NULL,
  `good_cause_amt` decimal(10,2) NOT NULL,
  `agent_ref_transaction_id` bigint(20) unsigned DEFAULT NULL,
  `vat_amt` decimal(20,2) NOT NULL,
  `taxable_sale` decimal(20,2) NOT NULL,
  `player_mob_number` varchar(15) DEFAULT NULL,
  `ret_sd_amt` decimal(20,4) NOT NULL COMMENT 'Security deposit amount of retailer',
  `agt_vat_amt` decimal(20,4) NOT NULL,
  PRIMARY KEY (`transaction_id`),
  KEY `ticket_nbr` (`ticket_nbr`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_dg_ret_sale_5`;

CREATE TABLE `st_dg_ret_sale_refund_1` (
  `transaction_id` bigint(20) unsigned NOT NULL,
  `game_id` int(10) unsigned NOT NULL DEFAULT '0',
  `ticket_nbr` bigint(20) unsigned NOT NULL DEFAULT '0',
  `mrp_amt` decimal(20,2) NOT NULL,
  `retailer_comm` decimal(20,2) NOT NULL,
  `net_amt` decimal(20,2) NOT NULL,
  `agent_comm` decimal(20,2) NOT NULL,
  `agent_net_amt` decimal(20,2) NOT NULL,
  `retailer_org_id` int(10) unsigned NOT NULL,
  `claim_status` enum('DONE_CLAIM','CLAIM_BAL') NOT NULL,
  `good_cause_amt` decimal(10,2) NOT NULL,
  `agent_ref_transaction_id` bigint(20) unsigned DEFAULT NULL,
  `vat_amt` decimal(20,2) NOT NULL,
  `taxable_sale` decimal(20,2) NOT NULL,
  `cancellation_charges` decimal(10,2) DEFAULT '0.00',
  `ref_transaction_id` bigint(20) DEFAULT '0',
  `ret_sd_amt` decimal(20,4) NOT NULL COMMENT 'Security deposit amount of retailer',
  `agt_vat_amt` decimal(20,4) NOT NULL,
  PRIMARY KEY (`transaction_id`),
  UNIQUE KEY `ref_transaction_id` (`ref_transaction_id`),
  KEY `ticket_nbr` (`ticket_nbr`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_dg_ret_sale_refund_1`;

CREATE TABLE `st_dg_ret_sale_refund_9` (
  `transaction_id` bigint(20) unsigned NOT NULL,
  `game_id` int(10) unsigned NOT NULL DEFAULT '0',
  `ticket_nbr` bigint(20) unsigned NOT NULL DEFAULT '0',
  `mrp_amt` decimal(20,2) NOT NULL,
  `retailer_comm` decimal(20,2) NOT NULL,
  `net_amt` decimal(20,2) NOT NULL,
  `agent_comm` decimal(20,2) NOT NULL,
  `agent_net_amt` decimal(20,2) NOT NULL,
  `retailer_org_id` int(10) unsigned NOT NULL,
  `claim_status` enum('DONE_CLAIM','CLAIM_BAL') NOT NULL,
  `good_cause_amt` decimal(10,4) NOT NULL,
  `agent_ref_transaction_id` bigint(20) unsigned DEFAULT NULL,
  `vat_amt` decimal(20,4) NOT NULL,
  `taxable_sale` decimal(20,4) NOT NULL,
  `cancellation_charges` decimal(10,2) DEFAULT '0.00',
  `ref_transaction_id` bigint(20) DEFAULT '0',
  `ret_sd_amt` decimal(20,4) NOT NULL COMMENT 'Security deposit amount of retailer',
  `agt_vat_amt` decimal(20,4) NOT NULL,
  PRIMARY KEY (`transaction_id`),
  KEY `ticket_nbr` (`ticket_nbr`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_dg_ret_sale_refund_9`;

CREATE TABLE `st_dg_ret_sale_refund_4` (
  `transaction_id` bigint(20) unsigned NOT NULL,
  `game_id` int(10) unsigned NOT NULL DEFAULT '0',
  `ticket_nbr` bigint(20) unsigned NOT NULL DEFAULT '0',
  `mrp_amt` decimal(20,2) NOT NULL,
  `retailer_comm` decimal(20,2) NOT NULL,
  `net_amt` decimal(20,2) NOT NULL,
  `agent_comm` decimal(20,2) NOT NULL,
  `agent_net_amt` decimal(20,2) NOT NULL,
  `retailer_org_id` int(10) unsigned NOT NULL,
  `claim_status` enum('DONE_CLAIM','CLAIM_BAL') NOT NULL,
  `good_cause_amt` decimal(10,4) NOT NULL,
  `agent_ref_transaction_id` bigint(20) unsigned DEFAULT NULL,
  `vat_amt` decimal(20,4) NOT NULL,
  `taxable_sale` decimal(20,4) NOT NULL,
  `cancellation_charges` decimal(10,2) DEFAULT '0.00',
  `ref_transaction_id` bigint(20) DEFAULT '0',
  `ret_sd_amt` decimal(20,4) NOT NULL,
  `agt_vat_amt` decimal(20,4) NOT NULL,
  PRIMARY KEY (`transaction_id`),
  UNIQUE KEY `ref_transaction_id` (`ref_transaction_id`),
  KEY `ticket_nbr` (`ticket_nbr`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_dg_ret_sale_refund_4`;

CREATE TABLE `st_dg_ret_sale_refund_7` (
  `transaction_id` bigint(20) unsigned NOT NULL,
  `game_id` int(10) unsigned NOT NULL DEFAULT '0',
  `ticket_nbr` bigint(20) unsigned NOT NULL DEFAULT '0',
  `mrp_amt` decimal(20,2) NOT NULL,
  `retailer_comm` decimal(20,2) NOT NULL,
  `net_amt` decimal(20,2) NOT NULL,
  `agent_comm` decimal(20,2) NOT NULL,
  `agent_net_amt` decimal(20,2) NOT NULL,
  `retailer_org_id` int(10) unsigned NOT NULL,
  `claim_status` enum('DONE_CLAIM','CLAIM_BAL') NOT NULL,
  `good_cause_amt` decimal(10,4) NOT NULL,
  `agent_ref_transaction_id` bigint(20) unsigned DEFAULT NULL,
  `vat_amt` decimal(20,4) NOT NULL,
  `taxable_sale` decimal(20,4) NOT NULL,
  `cancellation_charges` decimal(10,2) DEFAULT '0.00',
  `ref_transaction_id` bigint(20) DEFAULT '0',
  `ret_sd_amt` decimal(20,4) NOT NULL COMMENT 'Security deposit amount of retailer',
  `agt_vat_amt` decimal(20,4) NOT NULL,
  PRIMARY KEY (`transaction_id`),
  UNIQUE KEY `ref_transaction_id` (`ref_transaction_id`),
  KEY `ticket_nbr` (`ticket_nbr`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_dg_ret_sale_refund_7`;

CREATE TABLE `st_dg_ret_sale_refund_6` (
  `transaction_id` bigint(20) unsigned NOT NULL,
  `game_id` int(10) unsigned NOT NULL DEFAULT '0',
  `ticket_nbr` bigint(20) unsigned NOT NULL DEFAULT '0',
  `mrp_amt` decimal(20,2) NOT NULL,
  `retailer_comm` decimal(20,2) NOT NULL,
  `net_amt` decimal(20,2) NOT NULL,
  `agent_comm` decimal(20,2) NOT NULL,
  `agent_net_amt` decimal(20,2) NOT NULL,
  `retailer_org_id` int(10) unsigned NOT NULL,
  `claim_status` enum('DONE_CLAIM','CLAIM_BAL') NOT NULL,
  `good_cause_amt` decimal(10,2) NOT NULL,
  `agent_ref_transaction_id` bigint(20) unsigned DEFAULT NULL,
  `vat_amt` decimal(20,2) NOT NULL,
  `taxable_sale` decimal(20,2) NOT NULL,
  `cancellation_charges` decimal(10,2) DEFAULT '0.00',
  `ref_transaction_id` bigint(20) DEFAULT '0',
  `ret_sd_amt` decimal(20,4) NOT NULL COMMENT 'Security deposit amount of retailer',
  `agt_vat_amt` decimal(20,4) NOT NULL,
  PRIMARY KEY (`transaction_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_dg_ret_sale_refund_6`;

CREATE TABLE `st_dg_ret_sale_refund_3` (
  `transaction_id` bigint(20) unsigned NOT NULL,
  `game_id` int(10) unsigned NOT NULL DEFAULT '0',
  `ticket_nbr` bigint(20) unsigned NOT NULL DEFAULT '0',
  `mrp_amt` decimal(20,2) NOT NULL,
  `retailer_comm` decimal(20,2) NOT NULL,
  `net_amt` decimal(20,2) NOT NULL,
  `agent_comm` decimal(20,2) NOT NULL,
  `agent_net_amt` decimal(20,2) NOT NULL,
  `retailer_org_id` int(10) unsigned NOT NULL,
  `claim_status` enum('DONE_CLAIM','CLAIM_BAL') NOT NULL,
  `good_cause_amt` decimal(10,4) NOT NULL,
  `agent_ref_transaction_id` bigint(20) unsigned DEFAULT NULL,
  `vat_amt` decimal(20,4) NOT NULL,
  `taxable_sale` decimal(20,4) NOT NULL,
  `cancellation_charges` decimal(10,2) DEFAULT '0.00',
  `ref_transaction_id` bigint(20) DEFAULT '0',
  `ret_sd_amt` decimal(20,4) NOT NULL COMMENT 'Security deposit amount of retailer',
  `agt_vat_amt` decimal(20,4) NOT NULL,
  PRIMARY KEY (`transaction_id`),
  UNIQUE KEY `ref_transaction_id` (`ref_transaction_id`),
  KEY `ticket_nbr` (`ticket_nbr`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_dg_ret_sale_refund_3`;

CREATE TABLE `st_dg_ret_sale_refund_8` (
  `transaction_id` bigint(20) unsigned NOT NULL,
  `game_id` int(10) unsigned NOT NULL DEFAULT '0',
  `ticket_nbr` bigint(20) unsigned NOT NULL DEFAULT '0',
  `mrp_amt` decimal(20,2) NOT NULL,
  `retailer_comm` decimal(20,2) NOT NULL,
  `net_amt` decimal(20,2) NOT NULL,
  `agent_comm` decimal(20,2) NOT NULL,
  `agent_net_amt` decimal(20,2) NOT NULL,
  `retailer_org_id` int(10) unsigned NOT NULL,
  `claim_status` enum('DONE_CLAIM','CLAIM_BAL') NOT NULL,
  `good_cause_amt` decimal(10,4) NOT NULL,
  `agent_ref_transaction_id` bigint(20) unsigned DEFAULT NULL,
  `vat_amt` decimal(20,4) NOT NULL,
  `taxable_sale` decimal(20,4) NOT NULL,
  `cancellation_charges` decimal(10,2) DEFAULT '0.00',
  `ref_transaction_id` bigint(20) DEFAULT '0',
  `ret_sd_amt` decimal(20,4) NOT NULL COMMENT 'Security deposit amount of retailer',
  `agt_vat_amt` decimal(20,4) NOT NULL,
  PRIMARY KEY (`transaction_id`),
  UNIQUE KEY `ref_transaction_id` (`ref_transaction_id`),
  KEY `ticket_nbr` (`ticket_nbr`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_dg_ret_sale_refund_8`;

CREATE TABLE `st_dg_ret_sale_refund_2` (
  `transaction_id` bigint(20) unsigned NOT NULL,
  `game_id` int(10) unsigned NOT NULL DEFAULT '0',
  `ticket_nbr` bigint(20) unsigned NOT NULL DEFAULT '0',
  `mrp_amt` decimal(20,2) NOT NULL,
  `retailer_comm` decimal(20,2) NOT NULL,
  `net_amt` decimal(20,2) NOT NULL,
  `agent_comm` decimal(20,2) NOT NULL,
  `agent_net_amt` decimal(20,2) NOT NULL,
  `retailer_org_id` int(10) unsigned NOT NULL,
  `claim_status` enum('DONE_CLAIM','CLAIM_BAL') NOT NULL,
  `good_cause_amt` decimal(10,4) NOT NULL,
  `agent_ref_transaction_id` bigint(20) unsigned DEFAULT NULL,
  `vat_amt` decimal(20,4) NOT NULL,
  `taxable_sale` decimal(20,4) NOT NULL,
  `cancellation_charges` decimal(10,2) DEFAULT '0.00',
  `ref_transaction_id` bigint(20) DEFAULT '0',
  `ret_sd_amt` decimal(20,4) NOT NULL COMMENT 'Security deposit amount of retailer',
  `agt_vat_amt` decimal(20,4) NOT NULL,
  PRIMARY KEY (`transaction_id`),
  UNIQUE KEY `ref_transaction_id` (`ref_transaction_id`),
  KEY `ticket_nbr` (`ticket_nbr`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_dg_ret_sale_refund_2`;

CREATE TABLE `st_dg_ret_sale_refund_5` (
  `transaction_id` bigint(20) unsigned NOT NULL,
  `game_id` int(10) unsigned NOT NULL DEFAULT '0',
  `ticket_nbr` bigint(20) unsigned NOT NULL DEFAULT '0',
  `mrp_amt` decimal(20,2) NOT NULL,
  `retailer_comm` decimal(20,2) NOT NULL,
  `net_amt` decimal(20,2) NOT NULL,
  `agent_comm` decimal(20,2) NOT NULL,
  `agent_net_amt` decimal(20,2) NOT NULL,
  `retailer_org_id` int(10) unsigned NOT NULL,
  `claim_status` enum('DONE_CLAIM','CLAIM_BAL') NOT NULL,
  `good_cause_amt` decimal(10,4) NOT NULL,
  `agent_ref_transaction_id` bigint(20) unsigned DEFAULT NULL,
  `vat_amt` decimal(20,4) NOT NULL,
  `taxable_sale` decimal(20,4) NOT NULL,
  `cancellation_charges` decimal(10,2) DEFAULT '0.00',
  `ref_transaction_id` bigint(20) DEFAULT '0',
  `ret_sd_amt` decimal(20,4) NOT NULL COMMENT 'Security deposit amount of retailer',
  `agt_vat_amt` decimal(20,4) NOT NULL,
  PRIMARY KEY (`transaction_id`),
  KEY `ticket_nbr` (`ticket_nbr`),
  KEY `ref_transaction_id` (`ref_transaction_id`),
  KEY `ticket_nbr_2` (`ticket_nbr`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_dg_ret_sale_refund_5`;

CREATE TABLE `st_ics_daily_query_status` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `query_id` int(10) DEFAULT NULL,
  `expected_result` varchar(100) DEFAULT NULL,
  `actual_result` varchar(100) DEFAULT NULL,
  `ics_run_date` datetime DEFAULT NULL,
  `query_execution_time` int(11) DEFAULT NULL COMMENT 'Query Execution Time in Milli Seconds',
  `is_success` enum('YES','NO') DEFAULT NULL,
  `run_by` enum('AUTO','MANNUAL') DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
--rollback drop table `st_ics_daily_query_status`;

CREATE TABLE `st_ics_daily_status` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `ics_datetime` datetime DEFAULT NULL,
  `ics_execution_time` int(11) DEFAULT NULL COMMENT 'Total ICS Execution Time in Milli Seconds',
  `is_success` enum('YES','NO') NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
--rollback drop table `st_ics_daily_status`;

CREATE TABLE `st_ics_property_master` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `property_dev_name` varchar(50) DEFAULT NULL,
  `property_display_name` varchar(50) DEFAULT NULL,
  `status` enum('ACTIVE','INACTIVE') DEFAULT NULL,
  `editable` enum('YES','NO') DEFAULT NULL,
  `value` varchar(100) DEFAULT NULL,
  `value_type` varchar(50) DEFAULT NULL,
  `description` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `property_dev_name` (`property_dev_name`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
--rollback drop table `st_ics_property_master`;

CREATE TABLE `st_ics_query_master` (
  `query_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `query_title` varchar(300) DEFAULT NULL,
  `main_query` blob,
  `is_sp` enum('YES','NO') DEFAULT NULL,
  `is_date_req` enum('YES','NO') DEFAULT NULL,
  `last_successful_date` datetime DEFAULT NULL,
  `query_result` varchar(30) DEFAULT NULL,
  `qurey_description` varchar(300) DEFAULT NULL,
  `related_to` enum('CS','DG','MGMT','OLA','SE') DEFAULT NULL,
  `tier_type` enum('BO','AGENT','RETAILER','ALL') DEFAULT NULL,
  `error_msg` varchar(3000) DEFAULT NULL,
  `is_critical` enum('YES','NO') DEFAULT NULL,
  `query_status` enum('ACTIVE','INACTIVE') DEFAULT NULL,
  `last_updated_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updated_by` int(11) DEFAULT NULL,
  PRIMARY KEY (`query_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
--rollback drop table `st_ics_query_master`;

CREATE TABLE `st_ics_query_master_history` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `query_id` int(10) unsigned NOT NULL,
  `query_title` varchar(300) DEFAULT NULL,
  `last_successful_date` datetime DEFAULT NULL,
  `qurey_description` varchar(300) DEFAULT NULL,
  `error_msg` varchar(3000) DEFAULT NULL,
  `is_critical` enum('YES','NO') DEFAULT NULL,
  `query_status` enum('ACTIVE','INACTIVE') DEFAULT NULL,
  `updated_date` datetime DEFAULT NULL,
  `updated_by` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
--rollback drop table `st_ics_query_master_history`;

CREATE TABLE `st_iw_agent_direct_plr_pwt` (
  `agent_org_id` int(10) unsigned DEFAULT NULL,
  `agent_user_id` int(10) unsigned DEFAULT NULL,
  `game_id` int(10) unsigned NOT NULL,
  `pwt_inv_id` int(10) unsigned NOT NULL,
  `task_id` int(10) unsigned DEFAULT NULL,
  `transaction_id` bigint(10) unsigned NOT NULL,
  `transaction_date` datetime NOT NULL,
  `player_id` int(10) unsigned NOT NULL,
  `pwt_amt` decimal(20,2) NOT NULL,
  `tax_amt` decimal(20,2) NOT NULL,
  `net_amt` decimal(20,2) NOT NULL,
  `payment_type` enum('CASH','CHEQUE','TPT') DEFAULT NULL,
  `cheque_nbr` varchar(10) DEFAULT NULL,
  `cheque_date` date DEFAULT NULL,
  `drawee_bank` varchar(50) DEFAULT NULL,
  `issuing_party_name` varchar(50) DEFAULT NULL,
  `pwt_claim_status` enum('CLAIM_RET_UNCLM','UNCLM_PWT','UNCLM_CANCELLED','CLAIM_PLR_AGT_UNCLM_DIR','CLAIM_PLR_AGT_CLM','CLAIM_BAL','UNCLAIM_BAL','DONE_CLM') NOT NULL,
  `agt_claim_comm` decimal(20,2) NOT NULL,
  KEY `player_id` (`player_id`),
  KEY `transaction_id` (`transaction_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
--rollback drop table `st_iw_agent_direct_plr_pwt`;

CREATE TABLE `st_iw_agent_retailer_pwt_comm_variance_history` (
  `agent_org_id` int(10) unsigned NOT NULL,
  `retailer_org_id` int(10) unsigned NOT NULL,
  `game_id` int(10) unsigned NOT NULL,
  `pwt_comm_variance` decimal(5,2) NOT NULL,
  `date_changed` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
--rollback drop table `st_iw_agent_retailer_pwt_comm_variance_history`;

CREATE TABLE `st_iw_agent_retailer_sale_comm_variance_history` (
  `agent_org_id` int(10) unsigned NOT NULL,
  `retialer_org_id` int(10) unsigned NOT NULL,
  `game_id` int(10) unsigned NOT NULL,
  `sale_comm_variance` decimal(5,2) NOT NULL,
  `date_changed` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
--rollback drop table `st_iw_agent_retailer_sale_comm_variance_history`;

CREATE TABLE `st_iw_agent_retailer_sale_pwt_comm_variance` (
  `agent_org_id` int(10) unsigned NOT NULL,
  `retailer_org_id` int(10) unsigned NOT NULL,
  `game_id` int(10) unsigned NOT NULL,
  `sale_comm_variance` decimal(5,2) DEFAULT NULL,
  `default_sale_comm_rate` decimal(5,2) DEFAULT NULL,
  `pwt_comm_variance` decimal(5,2) DEFAULT NULL,
  `default_pwt_comm_rate` decimal(5,2) DEFAULT NULL,
  PRIMARY KEY (`agent_org_id`,`retailer_org_id`,`game_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
--rollback drop table `st_iw_agent_retailer_sale_pwt_comm_variance`;

CREATE TABLE `st_iw_agt_pwt` (
  `agent_user_id` int(10) unsigned NOT NULL,
  `agent_org_id` int(10) unsigned NOT NULL,
  `retailer_org_id` int(10) unsigned NOT NULL,
  `game_id` int(10) unsigned NOT NULL,
  `transaction_id` bigint(20) unsigned NOT NULL,
  `pwt_amt` decimal(20,2) NOT NULL,
  `comm_amt` decimal(20,2) DEFAULT NULL,
  `net_amt` decimal(20,2) DEFAULT NULL,
  `agt_claim_comm` decimal(20,2) NOT NULL,
  `status` enum('DONE_CLM','CLAIM_BAL','DONE_UNCLM','UNCLAIM_BAL') NOT NULL,
  PRIMARY KEY (`transaction_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
--rollback drop table `st_iw_agt_pwt`;

CREATE TABLE `st_iw_agt_sale` (
  `transaction_id` bigint(20) unsigned NOT NULL,
  `agent_org_id` int(10) unsigned NOT NULL,
  `retailer_org_id` int(10) unsigned NOT NULL,
  `game_id` int(10) unsigned NOT NULL,
  `mrp_amt` decimal(20,2) NOT NULL,
  `retailer_comm_amt` decimal(20,2) NOT NULL,
  `net_amt` decimal(20,2) NOT NULL,
  `agent_comm_amt` decimal(20,2) NOT NULL,
  `bo_ref_transaction_id` bigint(20) unsigned DEFAULT NULL,
  `claim_status` enum('DONE_CLAIM','CLAIM_BAL') NOT NULL,
  `bo_net_amt` decimal(20,2) DEFAULT NULL,
  `vat_amt` decimal(20,4) NOT NULL,
  `taxable_sale` decimal(20,4) NOT NULL,
  `govt_comm` decimal(20,4) NOT NULL,
  `transaction_date` datetime DEFAULT NULL,
  PRIMARY KEY (`transaction_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
--rollback drop table `st_iw_agt_sale`;

CREATE TABLE `st_iw_agt_sale_refund` (
  `transaction_id` bigint(20) unsigned NOT NULL,
  `agent_org_id` int(10) unsigned NOT NULL,
  `retailer_org_id` int(10) unsigned NOT NULL,
  `game_id` int(10) unsigned NOT NULL,
  `mrp_amt` decimal(20,2) NOT NULL,
  `retailer_comm_amt` decimal(20,2) NOT NULL,
  `net_amt` decimal(20,2) NOT NULL,
  `agent_comm_amt` decimal(20,2) NOT NULL,
  `bo_net_amt` decimal(20,2) NOT NULL,
  `claim_status` enum('DONE_CLAIM','CLAIM_BAL') NOT NULL,
  `bo_ref_transaction_id` bigint(20) unsigned DEFAULT NULL,
  `vat_amt` decimal(20,4) NOT NULL,
  `taxable_sale` decimal(20,4) NOT NULL,
  `govt_comm` decimal(20,4) NOT NULL,
  `cancellation_charges` decimal(10,2) NOT NULL DEFAULT '0.00',
  `transaction_date` datetime DEFAULT NULL,
  PRIMARY KEY (`transaction_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
--rollback drop table `st_iw_agt_sale_refund`;

CREATE TABLE `st_iw_approval_req_master` (
  `task_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `request_id` varchar(20) NOT NULL,
  `party_id` int(10) unsigned DEFAULT NULL,
  `party_type` enum('BO','AGENT','RETAILER','PLAYER','ANONYMOUS') NOT NULL,
  `ticket_nbr` varchar(50) NOT NULL,
  `game_id` int(10) unsigned NOT NULL,
  `pwt_amt` decimal(20,2) NOT NULL,
  `tax_amt` decimal(20,2) NOT NULL DEFAULT '0.00',
  `net_amt` decimal(20,2) NOT NULL,
  `request_status` enum('REQUESTED','PND_MAS','PND_PAY','PAID','NA','CANCEL') NOT NULL,
  `requested_by_user_id` int(10) unsigned NOT NULL,
  `requester_by_type` enum('BO','AGENT','RETAILER') NOT NULL,
  `requested_to_org_id` int(10) unsigned DEFAULT NULL,
  `requested_to_type` enum('BO','AGENT','RETAILER') DEFAULT NULL,
  `request_date` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `approved_by_user_id` int(10) unsigned DEFAULT NULL,
  `approved_by_type` enum('BO','AGENT','RETAILER') DEFAULT NULL,
  `approval_date` datetime DEFAULT NULL,
  `pay_req_for_user_id` int(10) unsigned DEFAULT NULL,
  `pay_req_for_type` enum('BO','AGENT','RETAILER') DEFAULT NULL,
  `remarks` varchar(100) NOT NULL,
  `payment_done_by_user_id` int(10) unsigned DEFAULT NULL,
  `payment_done_by_type` enum('BO','AGENT','RETAILER') DEFAULT NULL,
  `transaction_id` bigint(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`task_id`),
  UNIQUE KEY `ticket_nbr` (`ticket_nbr`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
--rollback drop table `st_iw_approval_req_master`;

CREATE TABLE `st_iw_bo_agent_pwt_comm_variance_history` (
  `agent_org_id` int(11) unsigned NOT NULL,
  `game_id` int(11) unsigned NOT NULL,
  `pwt_comm_variance` decimal(5,2) NOT NULL,
  `date_changed` datetime NOT NULL DEFAULT '0000-00-00 00:00:00'
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
--rollback drop table `st_iw_bo_agent_pwt_comm_variance_history`;

CREATE TABLE `st_iw_bo_agent_sale_comm_variance_history` (
  `agent_org_id` int(11) unsigned NOT NULL,
  `game_id` int(11) unsigned NOT NULL,
  `sale_comm_variance` decimal(5,2) NOT NULL,
  `date_changed` datetime NOT NULL DEFAULT '0000-00-00 00:00:00'
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
--rollback drop table `st_iw_bo_agent_sale_comm_variance_history`;

CREATE TABLE `st_iw_bo_agent_sale_pwt_comm_variance` (
  `agent_org_id` int(10) unsigned NOT NULL,
  `game_id` int(10) unsigned NOT NULL,
  `sale_comm_variance` decimal(5,2) DEFAULT NULL,
  `default_sale_comm_rate` decimal(5,2) DEFAULT NULL,
  `pwt_comm_variance` decimal(5,2) DEFAULT NULL,
  `default_pwt_comm_rate` decimal(5,2) DEFAULT NULL,
  PRIMARY KEY (`agent_org_id`,`game_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
--rollback drop table `st_iw_bo_agent_sale_pwt_comm_variance`;

CREATE TABLE `st_iw_bo_direct_plr_pwt` (
  `bo_org_id` int(10) unsigned DEFAULT NULL,
  `bo_user_id` int(10) unsigned DEFAULT NULL,
  `game_id` int(10) unsigned NOT NULL,
  `pwt_inv_id` int(10) unsigned NOT NULL,
  `task_id` int(10) unsigned DEFAULT NULL,
  `transaction_id` bigint(10) unsigned NOT NULL,
  `transaction_date` datetime NOT NULL,
  `player_id` int(10) unsigned NOT NULL,
  `pwt_amt` decimal(20,2) NOT NULL,
  `tax_amt` decimal(20,2) NOT NULL,
  `net_amt` decimal(20,2) NOT NULL,
  `payment_type` enum('CASH','CHEQUE','TPT') DEFAULT NULL,
  `cheque_nbr` varchar(10) DEFAULT NULL,
  `cheque_date` date DEFAULT NULL,
  `drawee_bank` varchar(50) DEFAULT NULL,
  `issuing_party_name` varchar(50) DEFAULT NULL,
  KEY `player_id` (`player_id`),
  KEY `transaction_id` (`transaction_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
--rollback drop table `st_iw_bo_direct_plr_pwt`;

CREATE TABLE `st_iw_bo_pwt` (
  `bo_user_id` int(10) unsigned NOT NULL,
  `bo_org_id` int(10) unsigned NOT NULL,
  `agent_org_id` int(10) unsigned NOT NULL,
  `game_id` int(10) unsigned NOT NULL,
  `transaction_id` bigint(20) unsigned NOT NULL,
  `pwt_amt` decimal(20,2) NOT NULL,
  `comm_amt` decimal(20,2) DEFAULT NULL,
  `net_amt` decimal(20,2) DEFAULT NULL,
  PRIMARY KEY (`transaction_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
--rollback drop table `st_iw_bo_pwt`;

CREATE TABLE `st_iw_bo_sale` (
  `transaction_id` bigint(20) unsigned NOT NULL,
  `agent_org_id` int(10) unsigned NOT NULL,
  `game_id` int(10) unsigned NOT NULL,
  `mrp_amt` decimal(20,2) NOT NULL,
  `agent_comm` decimal(20,2) NOT NULL,
  `net_amt` decimal(20,2) NOT NULL,
  `vat_amt` decimal(20,4) NOT NULL,
  `taxable_sale` decimal(20,4) NOT NULL,
  `govt_comm` decimal(20,4) NOT NULL,
  `transaction_date` datetime DEFAULT NULL,
  PRIMARY KEY (`transaction_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
--rollback drop table `st_iw_bo_sale`;

CREATE TABLE `st_iw_game_govt_comm_history` (
  `game_id` int(10) unsigned NOT NULL,
  `govt_comm_rate` decimal(5,2) NOT NULL,
  `date_changed` datetime NOT NULL DEFAULT '0000-00-00 00:00:00'
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
--rollback drop table `st_iw_game_govt_comm_history`;

CREATE TABLE `st_iw_game_master` (
  `game_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `game_no` int(11) unsigned NOT NULL,
  `game_dev_name` varchar(50) DEFAULT NULL,
  `game_disp_name` varchar(100) DEFAULT NULL,
  `vat_amt` decimal(10,2) DEFAULT NULL,
  `gov_comm_rate` decimal(10,2) DEFAULT NULL,
  `retailer_sale_comm_rate` decimal(10,2) DEFAULT NULL,
  `retailer_pwt_comm_rate` decimal(10,2) DEFAULT NULL,
  `agent_sale_comm_rate` decimal(10,2) DEFAULT NULL,
  `agent_pwt_comm_rate` decimal(10,2) DEFAULT NULL,
  `closing_time` datetime DEFAULT NULL,
  `display_order` int(11) DEFAULT NULL,
  `game_status` enum('SALE_OPEN','SALE_HOLD','SALE_CLOSE') DEFAULT NULL,
  `prize_payout_ratio` decimal(5,2) unsigned NOT NULL,
  PRIMARY KEY (`game_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
--rollback drop table `st_iw_game_master`;

CREATE TABLE `st_iw_menu_master` (
  `menu_id` smallint(5) unsigned NOT NULL AUTO_INCREMENT,
  `action_id` smallint(5) unsigned DEFAULT NULL,
  `menu_name` varchar(50) DEFAULT NULL,
  `menu_disp_name` varchar(50) DEFAULT NULL,
  `parent_menu_id` smallint(5) unsigned DEFAULT NULL,
  `item_order` tinyint(3) unsigned DEFAULT NULL,
  `menu_disp_name_fr` varchar(50) DEFAULT NULL,
  `menu_disp_name_en` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`menu_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
--rollback drop table `st_iw_menu_master`;


CREATE TABLE `st_iw_priviledge_rep` (
  `action_id` smallint(10) unsigned NOT NULL AUTO_INCREMENT,
  `priv_id` int(10) unsigned NOT NULL,
  `priv_title` varchar(50) NOT NULL,
  `priv_disp_name` varchar(50) DEFAULT NULL,
  `action_mapping` varchar(50) NOT NULL,
  `parent_priv_id` int(10) unsigned DEFAULT NULL,
  `status` enum('ACTIVE','INACTIVE') DEFAULT NULL,
  `priv_owner` enum('BO','AGENT','RETAILER') DEFAULT NULL,
  `related_to` enum('IW_GAME_MGT','IW_PWT','IW_PLAY_MGT','IW_REPORTS','IW_DRAW_MGT','IW_MISC') DEFAULT NULL,
  `group_name` varchar(50) DEFAULT NULL,
  `is_start` enum('Y','N') DEFAULT NULL,
  `channel` enum('WEB','TERMINAL','MOBILE') NOT NULL,
  `priv_code` int(10) DEFAULT NULL,
  `hidden` enum('Y','N') DEFAULT NULL,
  `group_name_fr` varchar(50) DEFAULT NULL,
  `group_name_en` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`action_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
--rollback drop table `st_iw_priviledge_rep`;


CREATE TABLE `st_iw_pwt_inv` (
  `pwt_inv_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `game_id` int(11) DEFAULT NULL,
  `ticket_nbr` varchar(50) DEFAULT NULL,
  `pwt_amt` decimal(20,2) DEFAULT NULL,
  `retailer_transaction_id` bigint(20) DEFAULT NULL,
  `agent_transaction_id` bigint(20) DEFAULT NULL,
  `bo_transaction_id` bigint(20) DEFAULT NULL,
  `engine_transaction_id` varchar(50) DEFAULT NULL,
  `claim_at` enum('BO','AGENT','RETAILER') DEFAULT NULL,
  `status` enum('CLAIM_AT_BO','CLAIM_AT_AGENT','CLAIM_AT_RETAILER','CLAIM_RET_CLM_AUTO','CLAIM_AGT_CLM_AUTO') DEFAULT NULL,
  `is_direct_plr` enum('YES','NO') DEFAULT NULL,
  PRIMARY KEY (`pwt_inv_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
--rollback drop table `st_iw_pwt_inv`;

CREATE TABLE `st_iw_ret_activity_history` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `date` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `live_retailers` int(11) DEFAULT NULL,
  `noSale_retailers` int(11) DEFAULT NULL,
  `inactive_retailers` int(11) DEFAULT NULL,
  `terminated_retailers` int(11) DEFAULT NULL,
  `total_sales` double DEFAULT NULL,
  `total_pwt` double DEFAULT NULL,
  `total_tkt_count` int(11) DEFAULT NULL,
  `total_pwt_count` int(11) DEFAULT NULL,
  `avg_sale_per_ret` double DEFAULT NULL,
  PRIMARY KEY (`id`,`date`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
--rollback drop table `st_iw_ret_activity_history`;

CREATE TABLE `st_iw_ret_pwt` (
  `transaction_id` bigint(20) unsigned NOT NULL,
  `pwt_inv_id` int(11) DEFAULT NULL,
  `game_id` int(11) DEFAULT NULL,
  `retailer_org_id` int(11) DEFAULT NULL,
  `retailer_user_id` int(11) DEFAULT NULL,
  `pwt_amt` decimal(20,2) DEFAULT NULL,
  `retailer_claim_comm` decimal(20,2) DEFAULT NULL,
  `retailer_net_amt` decimal(20,2) DEFAULT NULL,
  `agt_claim_comm` decimal(20,2) DEFAULT NULL,
  `agent_net_amt` decimal(20,2) DEFAULT NULL,
  `transaction_date` datetime DEFAULT NULL,
  `pwt_claim_status` enum('CLAIM_BAL','DONE_CLM') DEFAULT NULL,
  `agent_ref_reansaction_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`transaction_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
--rollback drop table `st_iw_ret_pwt`;

CREATE TABLE `st_iw_ret_sale` (
  `transaction_id` bigint(10) unsigned NOT NULL,
  `engine_tx_id` varchar(50) NOT NULL,
  `game_id` int(10) unsigned NOT NULL DEFAULT '0',
  `retailer_org_id` int(11) DEFAULT NULL,
  `ticket_nbr` varchar(50) NOT NULL DEFAULT '0',
  `mrp_amt` decimal(20,2) unsigned NOT NULL,
  `retailer_comm_amt` decimal(20,2) NOT NULL,
  `retailer_net_amt` decimal(20,2) NOT NULL,
  `agent_comm_amt` decimal(20,2) NOT NULL,
  `agent_net_amt` decimal(20,2) NOT NULL,
  `good_cause_amt` decimal(20,2) NOT NULL,
  `vat_amt` decimal(20,4) NOT NULL,
  `taxable_sale` decimal(20,4) NOT NULL,
  `player_mob_number` varchar(15) DEFAULT NULL,
  `claim_status` enum('CLAIM_BAL','DONE_CLAIM') DEFAULT NULL,
  `agent_ref_transaction_id` bigint(20) DEFAULT NULL,
  `transaction_date` datetime DEFAULT NULL,
  `is_cancel` enum('Y','N') DEFAULT NULL,
  PRIMARY KEY (`transaction_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
--rollback drop table `st_iw_ret_sale`;

CREATE TABLE `st_iw_ret_sale_refund` (
  `transaction_id` bigint(20) unsigned NOT NULL,
  `engine_tx_id` varchar(50) NOT NULL,
  `game_id` int(10) unsigned NOT NULL DEFAULT '0',
  `retailer_org_id` int(11) DEFAULT NULL,
  `ticket_nbr` varchar(50) NOT NULL DEFAULT '0',
  `mrp_amt` decimal(20,2) unsigned NOT NULL,
  `retailer_comm_amt` decimal(20,2) NOT NULL,
  `retailer_net_amt` decimal(20,2) NOT NULL,
  `agent_comm_amt` decimal(20,2) NOT NULL,
  `agent_net_amt` decimal(20,2) NOT NULL,
  `good_cause_amt` decimal(20,2) NOT NULL,
  `vat_amt` decimal(20,4) NOT NULL,
  `taxable_sale` decimal(20,4) NOT NULL,
  `cancellation_charges` decimal(10,2) NOT NULL,
  `claim_status` enum('CLAIM_BAL','DONE_CLAIM') DEFAULT NULL,
  `agent_ref_transaction_id` bigint(20) DEFAULT NULL,
  `sale_ref_transaction_id` bigint(20) DEFAULT NULL,
  `transaction_date` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
--rollback drop table `st_iw_ret_sale_refund`;

CREATE TABLE `st_iw_retailer_daily_incentive_data` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `start_date` datetime DEFAULT NULL,
  `end_date` datetime DEFAULT NULL,
  `user_name` varchar(20) NOT NULL,
  `sale_amt` decimal(20,2) DEFAULT NULL,
  `winning_amt` decimal(20,2) DEFAULT NULL,
  `incentive_per` decimal(5,2) DEFAULT NULL,
  `incentive_amt` decimal(20,2) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_iw_retailer_daily_incentive_data`;

CREATE TABLE `st_iw_retailer_weekly_incentive_data` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `start_date` datetime DEFAULT NULL,
  `end_date` datetime DEFAULT NULL,
  `user_name` varchar(20) NOT NULL,
  `sale_amt` decimal(20,2) DEFAULT NULL,
  `winning_amt` decimal(20,2) DEFAULT NULL,
  `incentive_per` decimal(5,2) DEFAULT NULL,
  `incentive_amt` decimal(20,2) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_iw_retailer_weekly_incentive_data`;

CREATE TABLE `st_lms_agent_bank_deposit_transaction` (
  `transaction_id` bigint(20) unsigned NOT NULL DEFAULT '0',
  `retailer_org_id` int(11) DEFAULT NULL,
  `amount` decimal(23,2) DEFAULT NULL,
  `bank_name` varchar(60) DEFAULT NULL,
  `bank_branch_name` varchar(75) DEFAULT NULL,
  `bank_receipt_no` varchar(60) DEFAULT NULL,
  `bank_deposit_date` date DEFAULT NULL,
  PRIMARY KEY (`transaction_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_lms_agent_bank_deposit_transaction`;

CREATE TABLE `st_lms_agent_bank_details` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `agent_org_id` int(11) NOT NULL,
  `bank_id` int(10) NOT NULL,
  `branch_id` int(10) DEFAULT NULL,
  `account_nbr` varchar(20) NOT NULL,
  `doneBy_user_id` int(10) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `account_nbr` (`account_nbr`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_lms_agent_bank_details`;

CREATE TABLE `st_lms_agent_bank_details_history` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `agent_org_id` int(11) NOT NULL,
  `bank_id` int(10) NOT NULL,
  `branch_id` int(10) DEFAULT NULL,
  `account_nbr` varchar(20) NOT NULL,
  `doneBy_user_id` int(10) DEFAULT NULL,
  `date_changes` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_lms_agent_bank_details_history`;

CREATE TABLE `st_lms_agent_bank_master` (
  `bank_id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `bank_name` varchar(50) NOT NULL,
  `bank_type` enum('E-ZWICH','other') NOT NULL DEFAULT 'other',
  PRIMARY KEY (`bank_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
--rollback drop table `st_lms_agent_bank_master`;

CREATE TABLE `st_lms_agent_branch_master` (
  `branch_id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `branch_name` varchar(30) NOT NULL,
  `bank_id` int(10) unsigned NOT NULL,
  `branch_sort_code` int(10) NOT NULL,
  PRIMARY KEY (`branch_id`),
  UNIQUE KEY `branch_name` (`branch_name`),
  KEY `FK_st_lms_agent_branch_master` (`bank_id`),
  CONSTRAINT `FK_st_lms_agent_branch_master` FOREIGN KEY (`bank_id`) REFERENCES `st_lms_agent_bank_master` (`bank_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
--rollback drop table `st_lms_agent_branch_master`;

CREATE TABLE `st_lms_agent_cash_transaction` (
  `transaction_id` bigint(10) unsigned NOT NULL,
  `agent_user_id` int(10) unsigned NOT NULL,
  `retailer_org_id` int(10) unsigned NOT NULL,
  `amount` decimal(20,2) unsigned NOT NULL,
  `agent_org_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`transaction_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
--rollback drop table `st_lms_agent_cash_transaction`;

CREATE TABLE `st_lms_agent_credit_note` (
  `transaction_id` bigint(20) unsigned NOT NULL,
  `retailer_org_id` int(10) unsigned NOT NULL,
  `amount` decimal(20,2) unsigned DEFAULT NULL,
  `transaction_type` enum('CR_NOTE','CR_NOTE_CASH') NOT NULL,
  `remarks` varchar(100) DEFAULT NULL,
  `agent_user_id` int(10) unsigned NOT NULL,
  `agent_org_id` int(10) unsigned NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
--rollback drop table `st_lms_agent_credit_note`;

CREATE TABLE `st_lms_agent_current_balance` (
  `account_type` varchar(25) NOT NULL,
  `current_balance` decimal(20,2) NOT NULL,
  `agent_org_id` int(10) unsigned NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
--rollback drop table `st_lms_agent_current_balance`;

CREATE TABLE `st_lms_agent_current_balance_history` (
  `account_type` varchar(25) CHARACTER SET utf8 NOT NULL,
  `current_balance` decimal(20,2) NOT NULL,
  `agent_org_id` int(10) unsigned NOT NULL,
  `transaction_date` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_lms_agent_current_balance_history`;

CREATE TABLE `st_lms_agent_current_balance_history_arch` (
  `account_type` varchar(25) CHARACTER SET utf8 NOT NULL,
  `current_balance` decimal(20,2) NOT NULL,
  `agent_org_id` int(10) unsigned NOT NULL,
  `transaction_date` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_lms_agent_current_balance_history_arch`;

CREATE TABLE `st_lms_agent_current_balance_history_bak` (
  `account_type` varchar(25) NOT NULL,
  `current_balance` decimal(20,2) NOT NULL,
  `agent_org_id` int(10) unsigned NOT NULL,
  `transaction_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
--rollback drop table `st_lms_agent_current_balance_history_bak`;

CREATE TABLE `st_lms_agent_daily_training_exp` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `date` datetime DEFAULT NULL,
  `agent_org_id` int(10) DEFAULT NULL,
  `service_id` int(11) DEFAULT NULL,
  `mrp_sale` decimal(20,2) DEFAULT NULL,
  `training_exp_per` decimal(5,2) DEFAULT NULL,
  `training_exp_amt` decimal(20,2) DEFAULT NULL,
  `time_slotted_mrp_sale` decimal(20,2) NOT NULL DEFAULT '0.00',
  `extra_training_exp_per` decimal(10,2) NOT NULL DEFAULT '0.00',
  `extra_training_exp_amt` decimal(20,2) NOT NULL DEFAULT '0.00',
  `credit_note_number` varchar(20) DEFAULT NULL,
  `done_by_user_id` int(10) DEFAULT '0',
  `status` enum('PENDING','DONE') DEFAULT NULL,
  `updated_date` datetime DEFAULT NULL,
  `updated_mode` enum('AUTO','MANUAL') DEFAULT NULL,
  `game_id` tinyint(4) DEFAULT NULL,
  `incentive_mrp` decimal(20,2) NOT NULL DEFAULT '0.00',
  `incentive_per` decimal(5,2) DEFAULT NULL,
  `incentive_amt` decimal(20,2) DEFAULT NULL,
  `incentive_credit_note_number` varchar(20) DEFAULT NULL,
  UNIQUE KEY `id` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_lms_agent_daily_training_exp`;

CREATE TABLE `st_lms_agent_daily_trng_exp_var_mapping` (
  `agent_org_id` int(10) DEFAULT NULL,
  `service_id` int(11) DEFAULT NULL,
  `training_exp_var` decimal(5,2) DEFAULT NULL,
  `extra_training_exp_var` decimal(10,2) NOT NULL DEFAULT '0.00',
  `game_id` tinyint(4) DEFAULT NULL,
  `incentive_exp_var` decimal(5,2) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_lms_agent_daily_trng_exp_var_mapping`;

CREATE TABLE `st_lms_agent_daily_trng_exp_var_mapping_history` (
  `agent_org_id` int(10) DEFAULT NULL,
  `service_id` int(11) DEFAULT NULL,
  `training_exp_var` decimal(5,2) DEFAULT NULL,
  `extra_training_exp_var` decimal(10,2) NOT NULL DEFAULT '0.00',
  `updated_by_user_id` int(10) DEFAULT NULL,
  `updated_date` datetime DEFAULT NULL,
  `incentive_exp_var` decimal(5,2) DEFAULT NULL,
  `game_id` tinyint(4) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_lms_agent_daily_trng_exp_var_mapping_history`;

CREATE TABLE `st_lms_agent_debit_note` (
  `transaction_id` bigint(20) unsigned NOT NULL,
  `retailer_org_id` int(10) unsigned NOT NULL,
  `amount` decimal(20,2) unsigned DEFAULT NULL,
  `transaction_type` enum('DR_NOTE','DR_NOTE_CASH') NOT NULL,
  `remarks` varchar(2000) DEFAULT NULL,
  `agent_user_id` int(10) unsigned NOT NULL,
  `agent_org_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`transaction_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
--rollback drop table `st_lms_agent_debit_note`;

CREATE TABLE `st_lms_agent_default_daily_training_exp` (
  `service_id` int(11) NOT NULL DEFAULT '0',
  `game_id` int(11) NOT NULL,
  `training_exp_default` decimal(10,2) DEFAULT NULL,
  `training_exp_extra` decimal(10,2) NOT NULL DEFAULT '0.00',
  `incentive_exp_default` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`service_id`,`game_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_lms_agent_default_daily_training_exp`;

CREATE TABLE `st_lms_agent_default_weekly_training_exp` (
  `service_id` int(11) NOT NULL DEFAULT '0',
  `game_id` int(11) NOT NULL,
  `training_exp_default` decimal(10,2) DEFAULT NULL,
  `training_exp_extra` decimal(10,2) NOT NULL DEFAULT '0.00',
  `incentive_exp_default` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`service_id`,`game_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_lms_agent_default_weekly_training_exp`;

CREATE TABLE `st_lms_agent_extended_limit_history` (
  `task_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `agent_org_id` int(10) unsigned NOT NULL,
  `agent_user_id` int(10) unsigned NOT NULL,
  `retailer_org_id` int(10) unsigned NOT NULL,
  `extended_credit_limit` decimal(20,2) unsigned NOT NULL,
  `date_changed` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `extends_credit_limit_upto` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`task_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;
--rollback drop table `st_lms_agent_extended_limit_history`;

CREATE TABLE `st_lms_agent_govt_transaction` (
  `transaction_id` bigint(10) unsigned NOT NULL,
  `amount` decimal(20,2) unsigned NOT NULL,
  `month` date DEFAULT NULL,
  `transaction_type` enum('VAT','TDS','GOVT_COMM') NOT NULL,
  `service_code` varchar(5) DEFAULT NULL,
  PRIMARY KEY (`transaction_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
--rollback drop table `st_lms_agent_govt_transaction`;

CREATE TABLE `st_lms_agent_ledger` (
  `agent_org_id` int(10) unsigned NOT NULL,
  `transaction_type` enum('PURCH','PURCH_RET','BO_PWT','BO_PWT_AUTO','BO_CASH','BO_DG_PWT_AUTO','SALE','SALE_RET','PWT_PLR','PWT','PWT_AUTO','DG_SALE','DG_REFUND_CANCEL','DG_REFUND_FAILED','DG_PWT_AUTO','BO_CHEQUE','BO_CH_BOUN','BO_DR_NOTE','BO_CR_NOTE_CASH','BO_DR_NOTE_CASH','CASH','CHEQUE','CHQ_BOUNCE','DR_NOTE','CR_NOTE_CASH','DR_NOTE_CASH','CR_NOTE','DG_PWT_PLR') DEFAULT NULL,
  `transaction_date` datetime DEFAULT NULL,
  `account_type` varchar(25) DEFAULT NULL,
  `amount` decimal(20,2) DEFAULT NULL,
  `balance` decimal(20,2) DEFAULT NULL,
  `transaction_with` varchar(50) NOT NULL,
  `transaction_id` bigint(20) unsigned NOT NULL,
  `receipt_id` varchar(50) NOT NULL,
  `task_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  UNIQUE KEY `task_id` (`task_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC;
--rollback drop table `st_lms_agent_ledger`;

CREATE TABLE `st_lms_agent_post_deposit_commission_details` (
  `task_id` tinyint(3) unsigned NOT NULL AUTO_INCREMENT,
  `deposit_default_comm_rate` decimal(5,2) DEFAULT NULL,
  `tax_default_comm_rate` decimal(5,2) DEFAULT NULL,
  `charges_1` decimal(5,2) DEFAULT NULL,
  `charges_2` decimal(5,2) DEFAULT NULL,
  `status` enum('ACTIVE','INACTIVE') DEFAULT NULL,
  PRIMARY KEY (`task_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_lms_agent_post_deposit_commission_details`;

CREATE TABLE `st_lms_agent_post_deposit_commission_details_history` (
  `task_id` tinyint(3) unsigned NOT NULL AUTO_INCREMENT,
  `deposit_default_comm_rate` decimal(5,2) DEFAULT NULL,
  `tax_comm_rate` decimal(5,2) DEFAULT NULL,
  `charges_1` decimal(5,2) DEFAULT NULL,
  `charges_2` decimal(5,2) DEFAULT NULL,
  `updated_by_user_id` varchar(10) DEFAULT NULL,
  `updated_date` datetime DEFAULT NULL,
  `status` enum('ACTIVE','INACTIVE') DEFAULT NULL,
  PRIMARY KEY (`task_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_lms_agent_post_deposit_commission_details_history`;

CREATE TABLE `st_lms_agent_post_deposit_commission_variance` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `agt_org_id` int(11) DEFAULT NULL,
  `deposit_comm_var` decimal(5,2) DEFAULT NULL,
  `tax_var` decimal(5,2) DEFAULT NULL,
  `charges_1_var` decimal(5,2) DEFAULT NULL,
  `charges_2_var` decimal(5,2) DEFAULT NULL,
  `status` enum('ACTIVE','INACTIVE') DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_lms_agent_post_deposit_commission_variance`;

CREATE TABLE `st_lms_agent_post_deposit_commission_variance_history` (
  `task_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `agt_org_id` int(11) DEFAULT NULL,
  `change_comm_rate` decimal(5,2) DEFAULT NULL,
  `commission_type` enum('DEPOSIT','TAX','CHARGES_1','CHARGES_2') DEFAULT NULL,
  `updated_by_user_id` int(11) DEFAULT NULL,
  `updated_date` datetime DEFAULT NULL,
  PRIMARY KEY (`task_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_lms_agent_post_deposit_commission_variance_history`;

CREATE TABLE `st_lms_agent_post_deposit_daily_commission` (
  `auto_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `agt_org_id` int(11) DEFAULT NULL,
  `date_time` date DEFAULT NULL,
  `deposit_amount` decimal(10,2) DEFAULT NULL,
  `deposit_comm_rate` decimal(5,2) DEFAULT NULL,
  `deposit_comm_amount` decimal(10,2) DEFAULT NULL,
  `tax_comm_rate` decimal(5,2) DEFAULT NULL,
  `tax_amount` decimal(10,2) DEFAULT NULL,
  `charges_1` decimal(5,2) DEFAULT NULL,
  `charges_2` decimal(5,2) DEFAULT NULL,
  `net_amount_to_pay` decimal(10,2) DEFAULT NULL,
  `status` enum('PENDING','PROCESSED','DENIED','APPROVED','PAID') DEFAULT NULL,
  PRIMARY KEY (`auto_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_lms_agent_post_deposit_daily_commission`;

CREATE TABLE `st_lms_agent_post_deposit_datewise_commission` (
  `auto_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `agt_org_id` int(11) DEFAULT NULL,
  `start_date` date DEFAULT NULL,
  `end_date` date DEFAULT NULL,
  `deposit_amount` decimal(10,2) DEFAULT NULL,
  `deposit_comm_amount` decimal(10,2) DEFAULT NULL,
  `tax_amount` decimal(10,2) DEFAULT NULL,
  `charges_1` decimal(10,2) DEFAULT NULL,
  `charges_2` decimal(5,2) DEFAULT NULL,
  `net_amount_to_pay` decimal(10,2) DEFAULT NULL,
  `paid_date` datetime DEFAULT NULL,
  `paid_mode` varchar(20) DEFAULT NULL,
  `paid_by_user_id` int(11) DEFAULT NULL,
  `status` enum('PENDING','DENIED','APPROVED','PAID') DEFAULT NULL,
  PRIMARY KEY (`auto_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_lms_agent_post_deposit_datewise_commission`;

CREATE TABLE `st_lms_agent_receipts` (
  `receipt_id` int(10) unsigned NOT NULL,
  `receipt_type` enum('INVOICE','DLCHALLAN','CR_NOTE','DSRCHALLAN','RECEIPT','DR_NOTE','CR_NOTE_CASH','DR_NOTE_CASH','GOVT_RCPT','DG_RECEIPT','DG_INVOICE','OLA_INVOICE','OLA_RECEIPT','CS_INVOICE','CS_RECEIPT','SLE_RECEIPT','SLE_INVOICE') NOT NULL,
  `agent_org_id` int(10) unsigned NOT NULL,
  `party_id` int(10) unsigned DEFAULT NULL,
  `party_type` enum('BO','RETAILER','PLAYER','GOVT') NOT NULL,
  `generated_id` varchar(20) NOT NULL,
  `voucher_date` datetime DEFAULT NULL,
  PRIMARY KEY (`receipt_id`),
  UNIQUE KEY `generated_id` (`generated_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;
--rollback drop table `st_lms_agent_receipts`;

CREATE TABLE `st_lms_agent_receipts_trn_mapping` (
  `receipt_id` int(10) unsigned NOT NULL,
  `transaction_id` bigint(10) unsigned NOT NULL,
  KEY `transaction_id` (`transaction_id`),
  KEY `id` (`receipt_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
--rollback drop table `st_lms_agent_receipts_trn_mapping`;

CREATE TABLE `st_lms_agent_recon_bookwise` (
  `task_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `organization_id` int(10) unsigned NOT NULL,
  `game_id` int(5) unsigned NOT NULL,
  `recon_date` datetime DEFAULT NULL,
  `type` enum('NET_SALE','N.A.','PWT','Cash','ChqBounce Charges','Cheque Bounce','Credit Note','Debit Note','Cheque') DEFAULT NULL,
  `amt_parent` decimal(20,2) unsigned DEFAULT NULL,
  `amt_child` decimal(20,2) unsigned DEFAULT NULL,
  `recon_balance` decimal(20,2) DEFAULT NULL,
  `mrp_amt` decimal(20,2) unsigned DEFAULT NULL,
  PRIMARY KEY (`task_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
--rollback drop table `st_lms_agent_recon_bookwise`;

CREATE TABLE `st_lms_agent_recon_ticketwise` (
  `task_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `organization_id` int(10) unsigned NOT NULL,
  `game_id` int(5) unsigned NOT NULL,
  `recon_date` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `type` enum('NET_SALE','N.A.','PWT','Cash','ChqBounce Charges','Cheque Bounce','Credit Note','Debit Note','Cheque') DEFAULT NULL,
  `amt_parent` decimal(20,2) unsigned DEFAULT NULL,
  `amt_child` decimal(20,2) unsigned DEFAULT NULL,
  `recon_balance` decimal(20,2) DEFAULT NULL,
  `mrp_amt` decimal(20,2) unsigned DEFAULT NULL,
  PRIMARY KEY (`task_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
--rollback drop table `st_lms_agent_recon_ticketwise`;

CREATE TABLE `st_lms_agent_sale_chq` (
  `transaction_id` bigint(20) unsigned NOT NULL,
  `agent_user_id` int(10) unsigned NOT NULL,
  `retailer_org_id` int(10) unsigned NOT NULL,
  `cheque_nbr` varchar(10) NOT NULL DEFAULT '',
  `cheque_date` date NOT NULL,
  `issuing_party_name` varchar(30) NOT NULL,
  `drawee_bank` varchar(30) NOT NULL,
  `cheque_amt` decimal(20,2) unsigned NOT NULL,
  `transaction_type` enum('CHEQUE','CHQ_BOUNCE','CLOSED') NOT NULL,
  `agent_org_id` int(10) unsigned NOT NULL,
  KEY `transaction_id` (`transaction_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
--rollback drop table `st_lms_agent_sale_chq`;

CREATE TABLE `st_lms_agent_tasks` (
  `task_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `amount` decimal(20,2) NOT NULL,
  `month` date DEFAULT NULL,
  `transaction_type` enum('VAT','TDS','GOVT_COMM') NOT NULL,
  `status` enum('APPROVED','UNAPPROVED','DONE') NOT NULL,
  `approve_date` datetime DEFAULT NULL,
  `agent_org_id` int(10) unsigned NOT NULL,
  `service_code` varchar(5) DEFAULT NULL,
  PRIMARY KEY (`task_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
--rollback drop table `st_lms_agent_tasks`;

CREATE TABLE `st_lms_agent_transaction_master` (
  `transaction_id` bigint(10) unsigned NOT NULL,
  `user_id` int(10) unsigned NOT NULL,
  `user_org_id` int(10) unsigned NOT NULL,
  `party_type` enum('BO','AGENT','RETAILER','PLAYER','GOVT') DEFAULT NULL,
  `party_id` int(10) unsigned DEFAULT NULL,
  `transaction_type` enum('DG_SALE','CHEQUE','CHQ_BOUNCE','DR_NOTE','DG_PWT_AUTO','DG_REFUND_CANCEL','PURCHASE','DG_REFUND_FAILED','VAT','SALE','CASH','TDS','PWT_PLR','CR_NOTE_CASH','DR_NOTE_CASH','GOVT_COMM','SALE_RET','PWT','UNCLM_PWT','PWT_AUTO','DG_PWT_PLR','DG_PWT','CS_SALE','CS_CANCEL_SERVER','CS_CANCEL_RET','LOOSE_SALE','LOOSE_SALE_RET','OLA_DEPOSIT','OLA_DEPOSIT_PLR','OLA_WITHDRAWL','OLA_WITHDRAWL_PLR','OLA_DEPOSIT_REFUND','OLA_DEPOSIT_REFUND_PLR','OLA_WITHDRAWL_REFUND','OLA_WITHDRAWL_REFUND_PLR','OLA_COMMISSION','SLE_SALE','SLE_REFUND_CANCEL','SLE_PWT','SLE_PWT_AUTO','BANK_DEPOSIT','IW_SALE','IW_REFUND_CANCEL','IW_PWT','IW_PWT_AUTO') DEFAULT NULL,
  `transaction_date` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`transaction_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
--rollback drop table `st_lms_agent_transaction_master`;

CREATE TABLE `st_lms_agent_weekly_training_exp` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `date` datetime DEFAULT NULL,
  `agent_org_id` int(10) DEFAULT NULL,
  `service_id` int(11) DEFAULT NULL,
  `mrp_sale` decimal(20,2) DEFAULT NULL,
  `training_exp_per` decimal(5,2) DEFAULT NULL,
  `training_exp_amt` decimal(20,2) DEFAULT NULL,
  `time_slotted_mrp_sale` decimal(20,2) NOT NULL DEFAULT '0.00',
  `extra_training_exp_per` decimal(10,2) NOT NULL DEFAULT '0.00',
  `extra_training_exp_amt` decimal(20,2) NOT NULL DEFAULT '0.00',
  `credit_note_number` varchar(20) DEFAULT NULL,
  `done_by_user_id` int(10) DEFAULT NULL,
  `status` enum('PENDING','DONE') DEFAULT NULL,
  `updated_date` datetime DEFAULT NULL,
  `updated_mode` enum('AUTO','MANUAL') DEFAULT NULL,
  `game_id` tinyint(4) DEFAULT NULL,
  `incentive_mrp` decimal(20,2) NOT NULL DEFAULT '0.00',
  `incentive_per` decimal(5,2) DEFAULT NULL,
  `incentive_amt` decimal(20,2) DEFAULT NULL,
  `incentive_credit_note_number` varchar(20) DEFAULT NULL,
  UNIQUE KEY `id` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_lms_agent_weekly_training_exp`;

CREATE TABLE `st_lms_agent_weekly_trng_exp_var_mapping` (
  `agent_org_id` int(10) DEFAULT NULL,
  `service_id` int(11) DEFAULT NULL,
  `training_exp_var` decimal(5,2) DEFAULT NULL,
  `extra_training_exp_var` decimal(10,2) NOT NULL DEFAULT '0.00',
  `game_id` tinyint(4) DEFAULT NULL,
  `incentive_exp_var` decimal(5,2) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_lms_agent_weekly_trng_exp_var_mapping`;

CREATE TABLE `st_lms_agent_weekly_trng_exp_var_mapping_history` (
  `agent_org_id` int(10) DEFAULT NULL,
  `service_id` int(11) DEFAULT NULL,
  `training_exp_var` decimal(5,2) DEFAULT NULL,
  `extra_training_exp_var` decimal(10,2) NOT NULL DEFAULT '0.00',
  `updated_by_user_id` int(10) DEFAULT NULL,
  `updated_date` datetime DEFAULT NULL,
  `game_id` tinyint(4) DEFAULT NULL,
  `incentive_exp_var` decimal(5,2) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_lms_agent_weekly_trng_exp_var_mapping_history`;

CREATE TABLE `st_lms_area_master` (
  `area_name` varchar(50) NOT NULL,
  `area_code` varchar(10) NOT NULL,
  `city_code` varchar(10) DEFAULT NULL,
  `state_code` varchar(10) DEFAULT NULL,
  `country_code` varchar(10) DEFAULT NULL,
  `status` enum('ACTIVE','INACTIVE') DEFAULT NULL,
  PRIMARY KEY (`area_code`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_lms_area_master`;

CREATE TABLE `st_lms_audit_user_access_history` (
  `id` bigint(18) unsigned NOT NULL AUTO_INCREMENT,
  `audit_id` bigint(18) unsigned NOT NULL,
  `user_name` varchar(20) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `access_ip` varchar(20) NOT NULL,
  `request_time` datetime NOT NULL,
  `action_name` varchar(50) DEFAULT NULL,
  `priv_id` int(11) DEFAULT NULL,
  `is_audit_trail_display` enum('Y','N') DEFAULT NULL,
  `service_type` enum('HOME','DG','SLE','MGMT') DEFAULT NULL,
  `interface` enum('WEB','TERMINAL','MOBILE') DEFAULT NULL,
  `response_time` datetime NOT NULL,
  `entry_time` datetime NOT NULL COMMENT 'Entry Time while inserting entry in database through scheduler',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_lms_audit_user_access_history`;

CREATE TABLE `st_lms_bank_deposit_bank_details` (
  `bank_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `bank_dev_name` varchar(100) DEFAULT NULL,
  `bank_disp_name` varchar(200) DEFAULT NULL,
  `account_number` varchar(100) DEFAULT NULL,
  `description` varchar(500) DEFAULT NULL,
  `status` enum('ACTIVE','INACTIVE') DEFAULT NULL,
  `item_order` int(10) DEFAULT NULL,
  PRIMARY KEY (`bank_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_lms_bank_deposit_bank_details`;

CREATE TABLE `st_lms_bank_deposit_details` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `organization_id` int(11) NOT NULL,
  `bank_id` int(11) NOT NULL,
  `branch_name` varchar(50) NOT NULL,
  `receipt_no` varchar(50) NOT NULL,
  `amount` double NOT NULL,
  `bank_deposit_date` datetime NOT NULL,
  `request_date` datetime NOT NULL,
  `process_date` datetime DEFAULT NULL,
  `process_by_user_id` int(11) DEFAULT NULL,
  `status` enum('PENDING','APPROVED','CANCELLED') NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
--rollback drop table `st_lms_bank_deposit_details`;

CREATE TABLE `st_lms_bank_master` (
  `bank_id` int(10) NOT NULL AUTO_INCREMENT,
  `bank_display_name` varchar(30) NOT NULL,
  `bank_full_name` varchar(100) DEFAULT NULL,
  `bank_address1` varchar(100) NOT NULL,
  `bank_address2` varchar(100) DEFAULT NULL,
  `country` varchar(50) NOT NULL,
  `state` varchar(50) NOT NULL,
  `city` varchar(50) NOT NULL,
  `role_id` int(10) DEFAULT NULL,
  `creation_date` datetime DEFAULT NULL,
  `created_by_user_id` int(10) unsigned DEFAULT NULL,
  `bank_code` varchar(30) DEFAULT NULL,
  `status` enum('ACTIVE','INACTIVE') DEFAULT NULL,
  PRIMARY KEY (`bank_id`),
  UNIQUE KEY `bank_display_name` (`bank_display_name`),
  UNIQUE KEY `role_id` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
--rollback drop table `st_lms_bank_master`;

CREATE TABLE `st_lms_bo_bank_deposit_transaction` (
  `transaction_id` bigint(20) unsigned NOT NULL,
  `agent_org_id` int(11) DEFAULT NULL,
  `amount` decimal(23,2) DEFAULT NULL,
  `bank_name` varchar(60) DEFAULT NULL,
  `bank_branch_name` varchar(75) DEFAULT NULL,
  `bank_receipt_no` varchar(60) DEFAULT NULL,
  `bank_deposit_date` date DEFAULT NULL,
  PRIMARY KEY (`transaction_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_lms_bo_bank_deposit_transaction`;

CREATE TABLE `st_lms_bo_cash_denomination_details` (
  `transaction_id` bigint(10) NOT NULL,
  `cashier_id` int(10) DEFAULT NULL,
  `receive_denomination` varchar(20) DEFAULT NULL,
  `return_denomination` varchar(20) DEFAULT NULL,
  `nbrOfNotes` int(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC;
--rollback drop table `st_lms_bo_cash_denomination_details`;

CREATE TABLE `st_lms_bo_cash_drawer_master` (
  `drawer_id` int(10) NOT NULL AUTO_INCREMENT,
  `drawer_name` varchar(30) NOT NULL,
  `remarks` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`drawer_id`,`drawer_name`),
  UNIQUE KEY `drawer_id` (`drawer_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC;
--rollback drop table `st_lms_bo_cash_drawer_master`;

CREATE TABLE `st_lms_bo_cash_drawer_status` (
  `cashier_id` int(10) DEFAULT NULL,
  `drawer_id` int(10) DEFAULT NULL,
  `denomination` varchar(20) DEFAULT NULL,
  `nbrOfNotes` int(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC;
--rollback drop table `st_lms_bo_cash_drawer_status`;

CREATE TABLE `st_lms_bo_cash_drawer_status_history` (
  `task_id` int(20) NOT NULL AUTO_INCREMENT,
  `cashier_id` int(10) DEFAULT NULL,
  `drawer_id` int(10) DEFAULT NULL,
  `denomination` varchar(20) DEFAULT NULL,
  `nbrOfNotes` int(10) DEFAULT NULL,
  `date` date DEFAULT NULL,
  `update_mode` enum('CLEAR','CHANGE') DEFAULT NULL,
  `doneBy` int(11) DEFAULT NULL COMMENT 'it is an user_id of cashier who updated the record.',
  UNIQUE KEY `task_id` (`task_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC;
--rollback drop table `st_lms_bo_cash_drawer_status_history`;

CREATE TABLE `st_lms_bo_cash_transaction` (
  `transaction_id` bigint(10) unsigned NOT NULL,
  `agent_org_id` int(10) unsigned NOT NULL,
  `amount` decimal(20,2) unsigned NOT NULL,
  PRIMARY KEY (`transaction_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
--rollback drop table `st_lms_bo_cash_transaction`;

CREATE TABLE `st_lms_bo_cheque_temp_receipt` (
  `task_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `temp_receipt_id` varchar(20) NOT NULL,
  `transaction_id` bigint(20) unsigned DEFAULT NULL,
  `cheque_nbr` varchar(10) NOT NULL DEFAULT '',
  `agent_org_id` int(10) NOT NULL,
  `cheque_date` date NOT NULL,
  `cheque_receiving_date` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `cheque_clearance_date` date DEFAULT NULL,
  `issuing_party_name` varchar(30) NOT NULL,
  `drawee_bank` varchar(30) NOT NULL,
  `cheque_amt` decimal(20,2) unsigned NOT NULL,
  `cheque_status` enum('CLEARED','CANCEL','PENDING') NOT NULL,
  PRIMARY KEY (`task_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;
--rollback drop table `st_lms_bo_cheque_temp_receipt`;

CREATE TABLE `st_lms_bo_credit_note` (
  `transaction_id` bigint(10) unsigned NOT NULL,
  `agent_org_id` int(10) unsigned NOT NULL,
  `amount` decimal(20,2) DEFAULT NULL,
  `transaction_type` enum('CR_NOTE_CASH','CR_NOTE') DEFAULT NULL,
  `remarks` varchar(2000) DEFAULT NULL,
  `reason` enum('OTHERS','AGAINST_LOOSE_BOOKS_RETURN','AGAINST_FAULTY_RECHARGE_VOUCHERS','CR_WEEKLY_EXP','CR_DAILY_EXP') NOT NULL DEFAULT 'OTHERS',
  `ref_id` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`transaction_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
--rollback drop table `st_lms_bo_credit_note`;

CREATE TABLE `st_lms_bo_current_balance` (
  `account_type` varchar(25) NOT NULL DEFAULT '',
  `current_balance` decimal(20,2) NOT NULL,
  `agent_org_id` int(10) unsigned DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
--rollback drop table `st_lms_bo_current_balance`;


CREATE TABLE `st_lms_bo_current_balance_history` (
  `account_type` varchar(25) CHARACTER SET utf8 NOT NULL DEFAULT '',
  `current_balance` decimal(20,2) NOT NULL,
  `agent_org_id` int(10) unsigned DEFAULT NULL,
  `transaction_date` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_lms_bo_current_balance_history`;

CREATE TABLE `st_lms_bo_current_balance_history_arch` (
  `account_type` varchar(25) CHARACTER SET utf8 NOT NULL DEFAULT '',
  `current_balance` decimal(20,2) NOT NULL,
  `agent_org_id` int(10) unsigned DEFAULT NULL,
  `transaction_date` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_lms_bo_current_balance_history_arch`;


CREATE TABLE `st_lms_bo_debit_note` (
  `transaction_id` bigint(10) unsigned NOT NULL,
  `agent_org_id` int(10) unsigned NOT NULL,
  `amount` decimal(20,2) unsigned DEFAULT NULL,
  `transaction_type` enum('DR_NOTE','DR_NOTE_CASH') DEFAULT NULL,
  `remarks` varchar(2000) DEFAULT NULL,
  `reason` enum('OTHERS','AGAINST_LOOSE_BOOKS_RETURN','AGAINST_FAULTY_RECHARGE_VOUCHERS','DR_WRONG_RECEIPT_ON_CASH','DR_WRONG_RECEIPT_ON_BD','DR_DAILY_TE_RETURN','DR_WEEKLY_TE_RETURN') NOT NULL DEFAULT 'OTHERS',
  `ref_id` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`transaction_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
--rollback drop table `st_lms_bo_debit_note`;

CREATE TABLE `st_lms_bo_extended_limit_history` (
  `task_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `bo_user_id` int(10) unsigned NOT NULL,
  `agent_org_id` int(10) unsigned NOT NULL,
  `extended_credit_limit` decimal(20,2) unsigned NOT NULL,
  `date_changed` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `extends_credit_limit_upto` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`task_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6491 DEFAULT CHARSET=utf8;
--rollback drop table `st_lms_bo_extended_limit_history`;

CREATE TABLE `st_lms_bo_govt_transaction` (
  `transaction_id` bigint(10) unsigned NOT NULL,
  `amount` decimal(20,2) NOT NULL,
  `game_id` int(10) unsigned DEFAULT NULL,
  `month` date DEFAULT NULL,
  `transaction_type` enum('VAT','TDS','GOVT_COMM','GOVT_COMM_PWT') NOT NULL,
  `start_date` datetime DEFAULT NULL,
  `end_date` datetime DEFAULT NULL,
  `service_code` varchar(5) DEFAULT NULL,
  PRIMARY KEY (`transaction_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
--rollback drop table `st_lms_bo_govt_transaction`;

CREATE TABLE `st_lms_bo_ledger` (
  `transaction_type` enum('PURCH','PURCH_RET','BO_PWT','BO_PWT_AUTO','BO_CASH','BO_DG_PWT_AUTO','SALE','SALE_RET','PWT_PLR','PWT','PWT_AUTO','DG_SALE','DG_REFUND_CANCEL','DG_REFUND_FAILED','DG_PWT_AUTO','BO_CHEQUE','BO_CH_BOUN','BO_DR_NOTE','BO_CR_NOTE_CASH','BO_DR_NOTE_CASH','CASH','CHEQUE','CHQ_BOUNCE','DR_NOTE','CR_NOTE_CASH','DR_NOTE_CASH','CR_NOTE','TDS','GOVT_COMM','VAT','UNCLM_PWT','DG_PWT','DG_PWT_PLR','BANK_DEPOSIT','CS_SALE','CS_CANCEL_SERVER','CS_CANCEL_RET') NOT NULL,
  `transaction_id` bigint(10) unsigned NOT NULL,
  `transaction_date` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `account_type` varchar(25) NOT NULL DEFAULT '',
  `amount` decimal(20,2) NOT NULL,
  `balance` decimal(20,2) NOT NULL,
  `transaction_with` varchar(50) DEFAULT NULL,
  `receipt_id` varchar(50) DEFAULT NULL,
  `task_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  UNIQUE KEY `task_id` (`task_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
--rollback drop table `st_lms_bo_ledger`;


CREATE TABLE `st_lms_bo_receipts` (
  `receipt_id` int(10) unsigned NOT NULL,
  `receipt_type` enum('INVOICE','DLCHALLAN','CR_NOTE','DSRCHALLAN','RECEIPT','DR_NOTE','CR_NOTE_CASH','DR_NOTE_CASH','GOVT_RCPT','DG_RECEIPT','DG_INVOICE','OLA_INVOICE','OLA_RECEIPT','CS_INVOICE','CS_RECEIPT','SLE_RECEIPT','SLE_INVOICE') DEFAULT NULL,
  `party_id` int(10) unsigned DEFAULT NULL,
  `party_type` enum('AGENT','SUPPLIER','GOVT','PLAYER','RETAILER','OLA_DISTRIBUTOR','BO') NOT NULL,
  `generated_id` varchar(20) NOT NULL,
  `voucher_date` datetime DEFAULT NULL,
  PRIMARY KEY (`receipt_id`),
  UNIQUE KEY `generated_id` (`generated_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
--rollback drop table `st_lms_bo_receipts`;

CREATE TABLE `st_lms_bo_receipts_trn_mapping` (
  `receipt_id` int(10) unsigned NOT NULL,
  `transaction_id` bigint(10) unsigned NOT NULL,
  KEY `id` (`receipt_id`),
  KEY `transaction_id` (`transaction_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
--rollback drop table `st_lms_bo_receipts_trn_mapping`;

CREATE TABLE `st_lms_bo_sale_chq` (
  `transaction_id` bigint(10) unsigned NOT NULL,
  `agent_org_id` int(10) unsigned NOT NULL,
  `cheque_nbr` varchar(10) NOT NULL,
  `cheque_date` date NOT NULL,
  `issuing_party_name` varchar(30) NOT NULL,
  `drawee_bank` varchar(30) NOT NULL,
  `cheque_amt` decimal(20,2) unsigned NOT NULL,
  `transaction_type` enum('CHEQUE','CHQ_BOUNCE','CLOSED') NOT NULL,
  PRIMARY KEY (`transaction_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
--rollback drop table `st_lms_bo_sale_chq`;

CREATE TABLE `st_lms_bo_tasks` (
  `task_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `amount` decimal(20,2) NOT NULL,
  `game_id` int(10) unsigned DEFAULT NULL,
  `month` date DEFAULT NULL,
  `transaction_type` enum('VAT','TDS','GOVT_COMM','GOVT_COMM_PWT') NOT NULL,
  `status` enum('APPROVED','UNAPPROVED','DONE') NOT NULL,
  `approve_date` datetime DEFAULT NULL,
  `start_date` datetime DEFAULT NULL,
  `end_date` datetime DEFAULT NULL,
  `service_code` varchar(5) DEFAULT NULL,
  PRIMARY KEY (`task_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;
--rollback drop table `st_lms_bo_tasks`;

CREATE TABLE `st_lms_bo_transaction_master` (
  `transaction_id` bigint(10) unsigned NOT NULL,
  `user_id` int(10) unsigned NOT NULL,
  `user_org_id` int(10) unsigned NOT NULL,
  `party_type` enum('AGENT','SUPPLIER','GOVT','PLAYER','RETAILER','OLA_DISTRIBUTOR') NOT NULL,
  `party_id` int(10) unsigned DEFAULT NULL,
  `transaction_date` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `transaction_type` enum('DG_SALE','CHEQUE','CHQ_BOUNCE','DR_NOTE','DG_PWT_AUTO','DG_REFUND_CANCEL','PURCHASE','DG_REFUND_FAILED','VAT','SALE','CASH','TDS','PWT_PLR','CR_NOTE_CASH','DR_NOTE_CASH','GOVT_COMM','GOVT_COMM_PWT','SALE_RET','PWT','UNCLM_PWT','PWT_AUTO','DG_PWT_PLR','DG_PWT','BANK_DEPOSIT','CS_SALE','CS_CANCEL_SERVER','CS_CANCEL_RET','LOOSE_SALE','LOOSE_SALE_RET','OLA_COMMISSION','OLA_DEPOSIT','OLA_DEPOSIT_PLR','OLA_DEPOSIT_REFUND','OLA_DEPOSIT_REFUND_PLR','OLA_WITHDRAWL','OLA_WITHDRAWL_PLR','OLA_WITHDRAWL_REFUND_PLR','OLA_WITHDRAWL_REFUND','OLA_CASHCARD_SALE','SLE_SALE','SLE_REFUND_CANCEL','SLE_REFUND_FAILED','SLE_PWT_AUTO','SLE_PWT_PLR','IW_SALE','IW_REFUND_CANCEL','IW_REFUND_FAILED','IW_PWT_AUTO','IW_PWT_PLR') DEFAULT NULL,
  PRIMARY KEY (`transaction_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
--rollback drop table `st_lms_bo_transaction_master`;

CREATE TABLE `st_lms_branch_master` (
  `branch_id` int(10) NOT NULL AUTO_INCREMENT,
  `branch_display_name` varchar(30) NOT NULL,
  `branch_full_name` varchar(100) DEFAULT NULL,
  `branch_address1` varchar(100) DEFAULT NULL,
  `branch_address2` varchar(100) DEFAULT NULL,
  `country` varchar(50) NOT NULL,
  `state` varchar(50) NOT NULL,
  `city` varchar(50) NOT NULL,
  `creation_date` datetime DEFAULT NULL,
  `created_by_user_id` int(10) unsigned DEFAULT NULL,
  `branch_code` varchar(30) DEFAULT NULL,
  `bank_id` int(10) DEFAULT NULL,
  `status` enum('ACTIVE','INACTIVE') DEFAULT NULL,
  `bypass_dates_for_pwt` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`branch_id`),
  UNIQUE KEY `branch_display_name` (`branch_display_name`),
  KEY `FK_st_lms_branch_master` (`bank_id`),
  CONSTRAINT `FK_st_lms_branch_master` FOREIGN KEY (`bank_id`) REFERENCES `st_lms_bank_master` (`bank_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_lms_branch_master`;
CREATE TABLE `st_lms_transaction_master` (
  `transaction_id` bigint(10) unsigned NOT NULL AUTO_INCREMENT,
  `user_type` enum('BO','AGENT','RETAILER') NOT NULL,
  `service_code` varchar(5) DEFAULT NULL,
  `interface` enum('WEB','TERMINAL','API') NOT NULL,
  PRIMARY KEY (`transaction_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
--rollback drop table `st_lms_transaction_master`;

CREATE TABLE `st_lms_branch_transaction_mapping` (
  `lms_transaction_id` bigint(10) unsigned DEFAULT NULL,
  `bank_id` int(10) DEFAULT NULL,
  `branch_id` int(10) DEFAULT NULL,
  `trn_type` enum('CASH','DR_NOTE_CASH','DG_PWT_PLR') DEFAULT NULL,
  `comment` varchar(100) DEFAULT NULL,
  `user_id` int(10) unsigned DEFAULT NULL,
  KEY `FK_st_lms_branch_transaction_mapping` (`lms_transaction_id`),
  CONSTRAINT `FK_st_lms_branch_transaction_mapping` FOREIGN KEY (`lms_transaction_id`) REFERENCES `st_lms_transaction_master` (`transaction_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_lms_branch_transaction_mapping`;

CREATE TABLE `st_lms_city_master` (
  `city_name` varchar(50) DEFAULT NULL,
  `city_code` varchar(10) DEFAULT NULL,
  `state_code` varchar(10) DEFAULT NULL,
  `country_code` varchar(10) DEFAULT NULL,
  `city_phone_code` varchar(6) DEFAULT NULL,
  `status` enum('ACTIVE','INACTIVE') DEFAULT NULL,
  UNIQUE KEY `city_code` (`city_code`),
  UNIQUE KEY `city_name` (`city_name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_lms_city_master`;

CREATE TABLE `st_lms_cl_xcl_update_history` (
  `auto_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `organization_id` int(11) unsigned NOT NULL,
  `done_by_user_id` int(11) DEFAULT NULL,
  `date_time` datetime NOT NULL,
  `type` enum('CL','XCL') NOT NULL,
  `amount` decimal(20,2) NOT NULL,
  `updated_value` decimal(20,2) NOT NULL,
  `total_bal_before_update` decimal(20,2) NOT NULL,
  PRIMARY KEY (`auto_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC;
--rollback drop table `st_lms_cl_xcl_update_history`;

CREATE TABLE `st_lms_con_device_master` (
  `id` int(11) NOT NULL,
  `sim_name` char(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `sim_name` (`sim_name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
--rollback drop table `st_lms_con_device_master`;

CREATE TABLE `st_lms_cons_inv_detail` (
  `task_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(10) unsigned NOT NULL,
  `user_org_id` int(10) unsigned NOT NULL,
  `current_owner_id` int(10) unsigned NOT NULL,
  `inv_model_id` smallint(10) unsigned NOT NULL,
  `quantity` int(10) unsigned NOT NULL,
  `date` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `ref_transaction_id` bigint(10) unsigned DEFAULT NULL,
  `amount` decimal(20,2) unsigned DEFAULT NULL,
  PRIMARY KEY (`task_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
--rollback drop table `st_lms_cons_inv_detail`;

CREATE TABLE `st_lms_cons_inv_specification` (
  `inv_model_id` smallint(10) unsigned NOT NULL AUTO_INCREMENT,
  `inv_id` int(10) unsigned NOT NULL,
  `spec_type` varchar(20) NOT NULL DEFAULT '',
  `spec_value` int(10) unsigned NOT NULL,
  `spec_unit` varchar(20) NOT NULL DEFAULT '',
  `cost_per_unit` decimal(10,2) unsigned NOT NULL,
  `spec_desc` varchar(100) NOT NULL DEFAULT '',
  PRIMARY KEY (`inv_model_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
--rollback drop table `st_lms_cons_inv_specification`;

CREATE TABLE `st_lms_cons_inv_status` (
  `party_type` enum('BO','AGENT','RETAILER') NOT NULL,
  `party_org_id` int(10) unsigned NOT NULL,
  `inv_model_id` smallint(10) unsigned NOT NULL,
  `cumm_qty_count` bigint(20) unsigned NOT NULL,
  PRIMARY KEY (`party_org_id`,`inv_model_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_lms_cons_inv_status`;

CREATE TABLE `st_lms_country_master` (
  `country_code` varchar(10) NOT NULL,
  `name` varchar(30) NOT NULL,
  `status` enum('ACTIVE','INACTIVE') NOT NULL,
  `country_phone_code` varchar(6) DEFAULT NULL,
  PRIMARY KEY (`country_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;
--rollback drop table `st_lms_country_master`;

CREATE TABLE `st_lms_daily_connectivity_history` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `date` date DEFAULT NULL,
  `sim_id` int(11) DEFAULT NULL COMMENT 'fk that relates with st_lms_con_device_master.',
  `ret_count` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC;
--rollback drop table `st_lms_daily_connectivity_history`;

CREATE TABLE `st_lms_denomination_master` (
  `country_code` varchar(10) DEFAULT NULL,
  `denomination_type` varchar(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_lms_denomination_master`;

CREATE TABLE `st_lms_division_master` (
  `division_name` varchar(50) NOT NULL,
  `division_code` varchar(50) NOT NULL,
  `area_code` varchar(10) NOT NULL,
  `city_code` varchar(10) DEFAULT NULL,
  `state_code` varchar(10) DEFAULT NULL,
  `country_code` varchar(10) DEFAULT NULL,
  `status` enum('ACTIVE','INACTIVE') DEFAULT NULL,
  PRIMARY KEY (`division_code`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_lms_division_master`;

CREATE TABLE `st_lms_draw_schedule_master` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `draw_time` varchar(20) DEFAULT NULL,
  `screen_time` varchar(20) DEFAULT NULL,
  `game_id` int(11) DEFAULT NULL,
  `screen_type` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_lms_draw_schedule_master`;

CREATE TABLE `st_lms_ebet_retailer_mappping` (
  `Id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(10) unsigned NOT NULL,
  `organization_id` int(10) unsigned NOT NULL,
  `model_id` int(10) NOT NULL,
  `device_id` varchar(30) NOT NULL,
  `device_code` varchar(10) DEFAULT NULL,
  `assign_datetime` datetime DEFAULT NULL,
  `last_sale_time` datetime DEFAULT NULL,
  PRIMARY KEY (`Id`),
  UNIQUE KEY `device_per_retailer` (`organization_id`,`model_id`,`device_id`),
  UNIQUE KEY `CK_ret_dev_Unique` (`organization_id`,`device_code`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
--rollback drop table `st_lms_ebet_retailer_mappping`;

CREATE TABLE `st_lms_ebet_retailer_mappping_history` (
  `Id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(10) unsigned NOT NULL,
  `organization_id` int(10) unsigned NOT NULL,
  `model_id` int(10) NOT NULL,
  `device_id` varchar(30) NOT NULL,
  `device_code` varchar(10) DEFAULT NULL,
  `changed_time` datetime DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_lms_ebet_retailer_mappping_history`;

CREATE TABLE `st_lms_ebet_sale_request` (
  `Id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `token_id` varchar(10) NOT NULL,
  `user_id` int(10) NOT NULL,
  `organization_id` int(10) NOT NULL,
  `device_map_id` int(10) NOT NULL,
  `request_data` longblob,
  `sale_type` enum('DGE','SLE','IW') DEFAULT NULL,
  `ebet_request_datetime` datetime DEFAULT NULL,
  `ebet_expiry_datetime` datetime DEFAULT NULL,
  `user_mobile` int(16) DEFAULT NULL,
  `processed_datetime` datetime DEFAULT NULL,
  `status` enum('Initiated','DONE','Cancelled') DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
--rollback drop table `st_lms_ebet_sale_request`;

CREATE TABLE `st_lms_holiday_master` (
  `holiday_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `holiday_date` date DEFAULT NULL,
  `holiday_day` enum('SUNDAY','MONDAY','TUESDAY','WEDNESDAY','THURSDAY','FRIDAY','SATURDAY') DEFAULT NULL,
  `reason` varchar(100) DEFAULT NULL,
  `status` enum('ACTIVE','INACTIVE') DEFAULT NULL,
  PRIMARY KEY (`holiday_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_lms_holiday_master`;

CREATE TABLE `st_lms_htpos_device_master` (
  `device_id` int(11) NOT NULL,
  `device_type` varchar(50) NOT NULL,
  PRIMARY KEY (`device_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_lms_htpos_device_master`;

CREATE TABLE `st_lms_htpos_download` (
  `auto_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `device_id` int(11) NOT NULL,
  `item_id` int(11) NOT NULL,
  `item_name` varchar(50) NOT NULL,
  `version` double(10,2) NOT NULL,
  `isMandatory` enum('YES','NO') NOT NULL DEFAULT 'NO',
  `fileSize` varchar(50) NOT NULL,
  `fileSize_adf` varchar(50) DEFAULT '-1',
  `updated_date` date DEFAULT NULL,
  `profile` enum('INGENICO','SKILROCK','GHANA') DEFAULT NULL,
  `status` enum('ACTIVE','INACTIVE') NOT NULL DEFAULT 'ACTIVE',
  `updatedBy` int(11) NOT NULL,
  `manual_download_status` enum('ACTIVE','INACTIVE') NOT NULL DEFAULT 'INACTIVE',
  PRIMARY KEY (`auto_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
--rollback drop table `st_lms_htpos_download`;

CREATE TABLE `st_lms_htpos_download_details` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `device_type` varchar(50) DEFAULT NULL,
  `version_date` date DEFAULT NULL,
  `device_version` varchar(50) DEFAULT NULL,
  `is_mandatory` enum('NO','YES') DEFAULT NULL,
  `file_size` varchar(50) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
--rollback drop table `st_lms_htpos_download_details`;

CREATE TABLE `st_lms_htpos_version_master` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `device_id` int(11) NOT NULL,
  `version` varchar(50) NOT NULL,
  `status` enum('ACTIVE','INACTIVE') NOT NULL DEFAULT 'ACTIVE',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
--rollback drop table `st_lms_htpos_version_master`;

CREATE TABLE `st_lms_inv_brand_master` (
  `brand_id` smallint(10) unsigned NOT NULL AUTO_INCREMENT,
  `brand_name` varchar(40) NOT NULL,
  `brand_desc` varchar(100) NOT NULL,
  `inv_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`brand_id`),
  UNIQUE KEY `brand_name` (`brand_name`,`inv_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
--rollback drop table `st_lms_inv_brand_master`;

CREATE TABLE `st_lms_inv_detail` (
  `task_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(10) unsigned NOT NULL,
  `user_org_type` enum('BO','AGENT','RETAILER') NOT NULL,
  `user_org_id` int(10) unsigned NOT NULL,
  `inv_model_id` smallint(10) unsigned NOT NULL,
  `serial_no` varchar(50) NOT NULL DEFAULT '',
  `date` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `cost_to_bo` decimal(20,2) DEFAULT '0.00',
  `is_new` enum('Y','N') NOT NULL,
  `current_owner_type` enum('BO','AGENT','RETAILER') NOT NULL,
  `current_owner_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`task_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
--rollback drop table `st_lms_inv_detail`;

CREATE TABLE `st_lms_inv_dl_detail` (
  `dl_id` int(11) NOT NULL AUTO_INCREMENT,
  `dl_owner_type` varchar(20) DEFAULT 'NULL',
  `generated_dl_id` varchar(25) NOT NULL,
  PRIMARY KEY (`dl_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
--rollback drop table `st_lms_inv_dl_detail`;

CREATE TABLE `st_lms_inv_dl_task_mapping` (
  `dl_id` int(11) DEFAULT NULL,
  `task_id` int(11) DEFAULT NULL,
  `inventory_type` enum('CONS','NON_CONS') DEFAULT 'NON_CONS'
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
--rollback drop table `st_lms_inv_dl_task_mapping`;

CREATE TABLE `st_lms_inv_mapping` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `inv_model_id` smallint(10) unsigned NOT NULL,
  `serial_no` varchar(50) NOT NULL DEFAULT '',
  `inv_code` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `inv_model_id` (`inv_model_id`,`inv_code`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
--rollback drop table `st_lms_inv_mapping`;

CREATE TABLE `st_lms_inv_master` (
  `inv_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `inv_name` varchar(40) NOT NULL,
  `inv_type` enum('NON_CONS','CONS') NOT NULL,
  `inv_img` longblob,
  `inv_desc` varchar(100) NOT NULL DEFAULT '',
  `is_usr_cntrl` enum('Y','N') NOT NULL DEFAULT 'Y' COMMENT 'Set N if Inv is admin Cntrl else N',
  PRIMARY KEY (`inv_id`),
  UNIQUE KEY `inv_name` (`inv_name`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
--rollback drop table `st_lms_inv_master`;

CREATE TABLE `st_lms_inv_model_master` (
  `model_id` smallint(10) unsigned NOT NULL AUTO_INCREMENT,
  `brand_id` smallint(10) unsigned NOT NULL,
  `model_name` varchar(50) NOT NULL DEFAULT '',
  `inv_id` int(10) unsigned NOT NULL,
  `model_desc` varchar(100) NOT NULL,
  `is_req_on_reg` enum('YES','NO') NOT NULL DEFAULT 'NO',
  `check_binding_length` tinyint(2) DEFAULT '0',
  `inv_column_name` varchar(50) DEFAULT NULL COMMENT 'inv col name in st_lms_ret_offline',
  `is_inv_code_req` enum('Y','N') NOT NULL DEFAULT 'N',
  PRIMARY KEY (`model_id`),
  UNIQUE KEY `brand_id` (`brand_id`,`model_name`,`inv_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
--rollback drop table `st_lms_inv_model_master`;

CREATE TABLE `st_lms_inv_status` (
  `user_id` int(10) unsigned NOT NULL,
  `user_org_type` enum('BO','AGENT','RETAILER') NOT NULL,
  `user_org_id` int(10) unsigned NOT NULL,
  `inv_model_id` smallint(10) unsigned NOT NULL,
  `serial_no` varchar(50) NOT NULL DEFAULT '',
  `cost_to_bo` decimal(20,2) DEFAULT '0.00',
  `is_new` enum('Y','N') NOT NULL,
  `current_owner_type` enum('BO','AGENT','RETAILER','REMOVED') NOT NULL,
  `current_owner_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`inv_model_id`,`serial_no`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_lms_inv_status`;

CREATE TABLE `st_lms_key_details` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `ticket_name` varchar(20) DEFAULT NULL,
  `ticket_sum` varchar(100) DEFAULT NULL,
  `ticket_id` varchar(100) DEFAULT NULL,
  `ticket_id1` varchar(1000) DEFAULT NULL,
  `ticket_start_date` date DEFAULT NULL,
  `ticket_valid_count` int(11) DEFAULT NULL,
  UNIQUE KEY `id` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
--rollback drop table `st_lms_key_details`;

CREATE TABLE `st_lms_last_sale_transaction` (
  `organization_id` int(10) unsigned NOT NULL,
  `CSLastSaleTransId` bigint(20) unsigned NOT NULL DEFAULT '0',
  `DGLastSaleTransId` bigint(20) unsigned NOT NULL DEFAULT '0',
  `IPELastSaleTransId` bigint(20) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`organization_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC;
--rollback drop table `st_lms_last_sale_transaction`;

CREATE TABLE `st_lms_location_wise_history` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `date` date DEFAULT NULL,
  `city_code` varchar(10) DEFAULT NULL COMMENT 'fk',
  `dg_sale_RC` int(11) DEFAULT NULL,
  `dg_pwt_RC` int(11) DEFAULT NULL,
  `se_sale_RC` int(11) DEFAULT NULL,
  `se_pwt_RC` int(11) DEFAULT NULL,
  `cs_sale_RC` int(11) DEFAULT NULL,
  `ola_deposit_RC` int(11) DEFAULT NULL,
  `ola_wd_RC` int(11) DEFAULT NULL,
  `dg_RC` int(11) DEFAULT NULL,
  `se_RC` int(11) DEFAULT NULL,
  `cs_RC` int(11) DEFAULT NULL,
  `ola_RC` int(11) DEFAULT NULL,
  `total_RC` int(11) DEFAULT NULL,
  `login_RC` int(11) DEFAULT NULL,
  `heartbeat_RC` int(11) DEFAULT NULL,
  `dg_total_sale` double(20,2) DEFAULT NULL,
  `dg_total_pwt` double(20,2) DEFAULT NULL,
  `se_total_sale` double(20,2) DEFAULT NULL,
  `se_total_pwt` double(20,2) DEFAULT NULL,
  `ola_total_deposit` double(20,2) DEFAULT NULL,
  `ola_total_wd` double(20,2) DEFAULT NULL,
  `cs_total_sale` double(20,2) DEFAULT NULL,
  `sl_sale_RC` int(11) DEFAULT NULL,
  `sl_pwt_RC` int(11) DEFAULT NULL,
  `sl_RC` int(11) DEFAULT NULL,
  `sl_total_sale` double(20,2) DEFAULT NULL,
  `sl_total_pwt` double(20,2) DEFAULT NULL,
  `iw_sale_RC` int(11) DEFAULT NULL,
  `iw_pwt_RC` int(11) DEFAULT NULL,
  `iw_RC` int(11) DEFAULT NULL,
  `vs_sale_RC` int(11) DEFAULT NULL,
  `vs_pwt_RC` int(11) DEFAULT NULL,
  `vs_RC` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC;
--rollback drop table `st_lms_location_wise_history`;

CREATE TABLE `st_lms_manual_correction` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `transaction_id_or_ref_id` varchar(1000) DEFAULT NULL,
  `transaction_amount` decimal(10,0) DEFAULT NULL,
  `deleted_from_table` varchar(1000) DEFAULT NULL,
  `deleted_by` varchar(50) DEFAULT NULL,
  `remarks` varchar(2000) DEFAULT NULL,
  `date` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
--rollback drop table `st_lms_manual_correction`;

CREATE TABLE `st_lms_menu_master` (
  `menu_id` smallint(10) unsigned NOT NULL AUTO_INCREMENT,
  `action_id` smallint(10) unsigned NOT NULL,
  `menu_name` varchar(50) DEFAULT NULL,
  `menu_disp_name` varchar(50) DEFAULT NULL,
  `parent_menu_id` smallint(10) unsigned NOT NULL,
  `item_order` tinyint(4) DEFAULT NULL,
  `menu_disp_name_fr` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `menu_disp_name_en` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`menu_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
--rollback drop table `st_lms_menu_master`;


CREATE TABLE `st_lms_merchant_auth_master` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `merchant_name` varchar(25) DEFAULT NULL,
  `ref_agt_id` int(11) NOT NULL,
  `merchant_ip` varchar(20) NOT NULL,
  `user_name` varchar(30) DEFAULT NULL,
  `password` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
--rollback drop table `st_lms_merchant_auth_master`;

CREATE TABLE `st_lms_new_ret_activity_history` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `date` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `dg_sale_RC` int(11) DEFAULT NULL,
  `dg_pwt_RC` int(11) DEFAULT NULL,
  `se_sale_RC` int(11) DEFAULT NULL,
  `se_pwt_RC` int(11) DEFAULT NULL,
  `cs_sale_RC` int(11) DEFAULT NULL,
  `ola_deposit_RC` int(11) DEFAULT NULL,
  `ola_wd_RC` int(11) DEFAULT NULL,
  `dg_RC` int(11) DEFAULT NULL,
  `se_RC` int(11) DEFAULT NULL,
  `cs_RC` int(11) DEFAULT NULL,
  `ola_RC` int(11) DEFAULT NULL,
  `total_RC` int(11) DEFAULT NULL,
  `heartBeat_RC` int(11) DEFAULT NULL,
  `login_RC` int(11) DEFAULT NULL,
  `inactive_retailers` int(11) DEFAULT NULL,
  `terminated_retailers` int(11) DEFAULT NULL,
  `dg_total_sales` double(20,2) DEFAULT NULL,
  `dg_total_pwt` double(20,2) DEFAULT NULL,
  `dg_tkt_count` int(11) DEFAULT NULL,
  `dg_pwt_count` int(11) DEFAULT NULL,
  `dg_avg_sale_per_ret` double(20,2) DEFAULT NULL,
  `se_total_sales` double(20,2) DEFAULT NULL,
  `se_total_pwt` double(20,2) DEFAULT NULL,
  `ola_total_deposit` double(20,2) DEFAULT NULL,
  `ola_total_wd` double(20,2) DEFAULT NULL,
  `cs_total_sale` double(20,2) DEFAULT NULL,
  `sl_sale_RC` int(11) DEFAULT NULL,
  `sl_pwt_RC` int(11) DEFAULT NULL,
  `sl_RC` int(11) DEFAULT NULL,
  `sl_total_sales` double(20,2) DEFAULT NULL,
  `sl_total_pwt` double(20,2) DEFAULT NULL,
  `sl_tkt_count` int(11) DEFAULT NULL,
  `sl_pwt_count` int(11) DEFAULT NULL,
  `sl_avg_sale_per_ret` double(20,2) DEFAULT NULL,
  `iw_sale_RC` int(11) DEFAULT NULL,
  `iw_pwt_RC` int(11) DEFAULT NULL,
  `iw_RC` int(11) DEFAULT NULL,
  `iw_total_sales` double(20,2) DEFAULT NULL,
  `iw_total_pwt` double(20,2) DEFAULT NULL,
  `iw_tkt_count` int(11) DEFAULT NULL,
  `iw_pwt_count` int(11) DEFAULT NULL,
  `iw_avg_sale_per_ret` double(20,2) DEFAULT NULL,
  `vs_sale_RC` int(11) DEFAULT NULL,
  `vs_pwt_RC` int(11) DEFAULT NULL,
  `vs_RC` int(11) DEFAULT NULL,
  `vs_total_sales` double(20,2) DEFAULT NULL,
  `vs_total_pwt` double(20,2) DEFAULT NULL,
  `vs_tkt_count` int(11) DEFAULT NULL,
  `vs_pwt_count` int(11) DEFAULT NULL,
  `vs_avg_sale_per_ret` double(20,2) DEFAULT NULL,
  PRIMARY KEY (`id`,`date`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
--rollback drop table `st_lms_new_ret_activity_history`;

CREATE TABLE `st_lms_oranization_limits` (
  `organization_id` int(10) unsigned NOT NULL,
  `verification_limit` decimal(15,2) NOT NULL,
  `approval_limit` decimal(15,2) NOT NULL,
  `pay_limit` decimal(15,2) NOT NULL,
  `scrap_limit` decimal(15,2) NOT NULL,
  `ola_deposit_limit` decimal(15,2) DEFAULT NULL,
  `ola_withdrawal_limit` decimal(15,2) DEFAULT NULL,
  `max_daily_claim_amt` decimal(15,2) NOT NULL,
  `levy_rate` decimal(10,2) unsigned NOT NULL DEFAULT '0.00',
  `security_deposit_rate` decimal(10,2) unsigned NOT NULL DEFAULT '0.00',
  `self_claim` enum('YES','NO') DEFAULT NULL,
  `other_claim` enum('YES','NO') DEFAULT NULL,
  `min_claim_per_ticket` decimal(15,2) DEFAULT NULL,
  `max_claim_per_ticket` decimal(15,2) DEFAULT NULL,
  `block_amt` decimal(15,2) NOT NULL,
  `block_days` int(10) NOT NULL,
  `block_action` enum('LOGIN_BLOCK','SALE_BLOCK','NO_ACTION') CHARACTER SET utf8 NOT NULL,
  PRIMARY KEY (`organization_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_lms_oranization_limits`;

CREATE TABLE `st_lms_oranization_limits_history` (
  `task_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `date_changed` datetime NOT NULL,
  `change_by_user_id` int(11) DEFAULT NULL,
  `organization_id` int(10) unsigned NOT NULL,
  `verification_limit` decimal(15,2) NOT NULL,
  `approval_limit` decimal(15,2) NOT NULL,
  `pay_limit` decimal(15,2) NOT NULL,
  `scrap_limit` decimal(15,2) NOT NULL,
  `ola_deposit_limit` decimal(15,2) DEFAULT NULL,
  `ola_withdrawal_limit` decimal(15,2) DEFAULT NULL,
  `max_daily_claim_amt` decimal(15,2) NOT NULL,
  `levy_rate` decimal(10,2) unsigned NOT NULL DEFAULT '0.00',
  `security_deposit_rate` decimal(10,2) unsigned NOT NULL DEFAULT '0.00',
  `self_claim` enum('YES','NO') DEFAULT NULL,
  `other_claim` enum('YES','NO') DEFAULT NULL,
  `min_claim_per_ticket` decimal(15,2) DEFAULT NULL,
  `max_claim_per_ticket` decimal(15,2) DEFAULT NULL,
  `block_amt` decimal(15,2) NOT NULL,
  `block_days` int(10) NOT NULL,
  `block_action` enum('LOGIN_BLOCK','SALE_BLOCK','NO_ACTION') CHARACTER SET utf8 NOT NULL,
  PRIMARY KEY (`task_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_lms_oranization_limits_history`;

CREATE TABLE `st_lms_organization_master` (
  `organization_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `organization_type` enum('BO','AGENT','RETAILER') NOT NULL,
  `name` varchar(50) NOT NULL,
  `org_code` varchar(50) DEFAULT NULL,
  `parent_id` int(10) unsigned NOT NULL,
  `addr_line1` varchar(100) NOT NULL,
  `addr_line2` varchar(100) DEFAULT NULL,
  `division_code` varchar(10) DEFAULT NULL,
  `area_code` varchar(10) DEFAULT NULL,
  `city` varchar(30) NOT NULL,
  `state_code` varchar(10) NOT NULL,
  `country_code` varchar(10) NOT NULL,
  `pin_code` bigint(15) unsigned DEFAULT NULL,
  `available_credit` decimal(20,2) DEFAULT '0.00',
  `claimable_bal` decimal(20,2) NOT NULL DEFAULT '0.00',
  `unclaimable_bal` decimal(20,2) NOT NULL DEFAULT '0.00',
  `credit_limit` decimal(20,2) NOT NULL,
  `security_deposit` decimal(20,2) DEFAULT NULL,
  `organization_status` enum('ACTIVE','INACTIVE','BLOCK','TERMINATE') NOT NULL,
  `extended_credit_limit` decimal(20,2) DEFAULT '0.00',
  `current_credit_amt` decimal(20,2) NOT NULL,
  `extends_credit_limit_upto` datetime DEFAULT NULL,
  `vat_registration_nbr` varchar(20) DEFAULT NULL,
  `pwt_scrap` enum('YES','NO') DEFAULT NULL,
  `recon_report_type` enum('Ticket Wise Report','Book Wise Report') DEFAULT NULL,
  `tp_organization` enum('NO','YES') DEFAULT 'NO',
  PRIMARY KEY (`organization_id`),
  UNIQUE KEY `name` (`name`),
  UNIQUE KEY `org_code` (`org_code`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC;
--rollback drop table `st_lms_organization_master`;

CREATE TABLE `st_lms_organization_master_history` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `organization_id` int(11) DEFAULT NULL,
  `change_type` enum('ORGANIZATION_STATUS','ADDRESS_1','ADDRESS_2','DIVISION_CODE','AREA_CODE','CITY','PIN_CODE','PWT_SCRAP','SECURITY_DEPOSIT') DEFAULT NULL,
  `change_value` varchar(150) DEFAULT NULL,
  `change_time` datetime DEFAULT NULL,
  `done_by_user_id` int(11) DEFAULT NULL,
  `comments` varchar(500) DEFAULT NULL,
  `request_ip` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
--rollback drop table `st_lms_organization_master_history`;

CREATE TABLE `st_lms_organization_master_history_2014` (
  `user_id` int(10) unsigned NOT NULL,
  `organization_id` int(10) unsigned NOT NULL,
  `addr_line1` varchar(100) NOT NULL DEFAULT '',
  `addr_line2` varchar(100) DEFAULT NULL,
  `division_code` varchar(10) DEFAULT NULL,
  `area_code` varchar(10) DEFAULT NULL,
  `city` varchar(30) DEFAULT '',
  `pin_code` bigint(15) unsigned DEFAULT NULL,
  `security_deposit` decimal(20,2) NOT NULL,
  `credit_limit` decimal(20,2) NOT NULL,
  `reason` enum('INACTIVE_AUTO_ACT','INACTIVE_AUTO_FRAUD','INACTIVE_MANUAL_BO','INACTIVE_MANUAL_AGENT','ACTIVE_AUTO_ACT','ACTIVE_AUTO_FRAUD','ACTIVE_MANUAL_BO','ACTIVE_MANUAL_AGENT','CREDIT_CHANGED_BO','CREDIT_CHANGED_AGENT','TERMINATE_MANUAL_BO') NOT NULL,
  `comment` varchar(1000) DEFAULT NULL,
  `organization_status` enum('ACTIVE','INACTIVE','TERMINATE') NOT NULL,
  `date_changed` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `pwt_scrap` enum('YES','NO') DEFAULT NULL,
  `recon_report_type` enum('Ticket Wise Report','Book Wise Report') DEFAULT NULL,
  UNIQUE KEY `organization_id` (`organization_id`,`organization_status`,`date_changed`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;
--rollback drop table `st_lms_organization_master_history_2014`;

CREATE TABLE `st_lms_organization_security_levy_master` (
  `organization_id` int(10) NOT NULL,
  `initial_security_deposit` decimal(10,2) DEFAULT '0.00',
  `expected_security_deposit` decimal(10,2) NOT NULL DEFAULT '0.00',
  `collected_security_deposit` decimal(10,2) DEFAULT '0.00',
  `levy_cat_type` enum('CAT-1','CAT-2') NOT NULL DEFAULT 'CAT-1',
  PRIMARY KEY (`organization_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
--rollback drop table `st_lms_organization_security_levy_master`;

CREATE TABLE `st_lms_password_history` (
  `task_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(10) unsigned NOT NULL,
  `password` varchar(30) NOT NULL,
  `date_changed` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `type` tinyint(4) NOT NULL,
  PRIMARY KEY (`task_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC;
--rollback drop table `st_lms_password_history`;

CREATE TABLE `st_lms_player_master` (
  `player_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `first_name` varchar(30) NOT NULL,
  `last_name` varchar(30) DEFAULT NULL,
  `email_id` varchar(30) DEFAULT NULL,
  `phone_nbr` varchar(15) DEFAULT NULL,
  `addr_line1` varchar(30) NOT NULL,
  `addr_line2` varchar(30) DEFAULT NULL,
  `city` varchar(30) NOT NULL,
  `state_code` varchar(10) NOT NULL,
  `country_code` varchar(10) NOT NULL,
  `pin_code` bigint(10) unsigned NOT NULL,
  `photo_id_type` varchar(25) NOT NULL,
  `photo_id_nbr` varchar(15) NOT NULL,
  `bank_name` varchar(40) DEFAULT NULL,
  `bank_branch` varchar(20) DEFAULT NULL,
  `location_city` varchar(30) DEFAULT NULL,
  `bank_acc_nbr` varchar(25) DEFAULT NULL,
  PRIMARY KEY (`player_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;
--rollback drop table `st_lms_player_master`;

CREATE TABLE `st_lms_pos_version_history` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `date` date DEFAULT NULL,
  `device_type` varchar(20) DEFAULT NULL COMMENT 'foreign key that relates with st_lms_htts_version_master.',
  `current_version` varchar(10) DEFAULT NULL COMMENT 'fk that relates with st_lms_htts_device_master.',
  `ret_count` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC;
--rollback drop table `st_lms_pos_version_history`;

CREATE TABLE `st_lms_priviledge_rep` (
  `action_id` smallint(10) unsigned NOT NULL AUTO_INCREMENT,
  `priv_id` int(10) unsigned NOT NULL,
  `priv_title` varchar(50) CHARACTER SET utf8 NOT NULL DEFAULT '',
  `priv_disp_name` varchar(50) DEFAULT NULL,
  `action_mapping` varchar(50) CHARACTER SET utf8 DEFAULT NULL,
  `parent_priv_id` int(10) unsigned NOT NULL,
  `status` enum('ACTIVE','INACTIVE') DEFAULT NULL,
  `priv_owner` enum('BO','AGENT','RETAILER') DEFAULT NULL,
  `related_to` enum('ACT_MGT','REPORTS','BO_USER_MGT','USER_MGT','AGENT_USER_MGT','ROLE_MGT','RET_USER_MGT','INV_MGT','PROD_MGT','TERMINAL_MGT','MISC') DEFAULT NULL,
  `group_name` varchar(50) DEFAULT NULL,
  `is_start` enum('Y','N') DEFAULT NULL,
  `channel` enum('WEB','TERMINAL','MOBILE') NOT NULL,
  `priv_code` int(10) DEFAULT NULL,
  `hidden` enum('Y','N') DEFAULT NULL,
  `group_name_fr` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `group_name_en` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `is_audit_trail_display` enum('Y','N') DEFAULT 'N',
  PRIMARY KEY (`action_id`),
  KEY `priv_title` (`priv_title`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
--rollback drop table `st_lms_priviledge_rep`;


CREATE TABLE `st_lms_property_master` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `property_code` varchar(50) DEFAULT NULL,
  `property_dev_name` varchar(50) DEFAULT NULL,
  `property_display_name` varchar(50) DEFAULT NULL,
  `status` enum('ACTIVE','INACTIVE') DEFAULT NULL,
  `editable` enum('YES','NO') DEFAULT NULL,
  `value` varchar(400) DEFAULT NULL,
  `value_type` varchar(50) DEFAULT NULL,
  `description` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `property_code` (`property_code`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
--rollback drop table `st_lms_property_master`;

CREATE TABLE `st_lms_receipts_master` (
  `receipt_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `user_type` enum('BO','AGENT','RETAILER') NOT NULL,
  PRIMARY KEY (`receipt_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
--rollback drop table `st_lms_receipts_master`;

CREATE TABLE `st_lms_report_email_priv_master` (
  `user_id` int(10) unsigned NOT NULL,
  `email_pid` int(10) unsigned NOT NULL,
  `status` enum('ACTIVE','INACTIVE') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
--rollback drop table `st_lms_report_email_priv_master`;

CREATE TABLE `st_lms_report_email_priviledge_rep` (
  `email_pid` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `priv_title` varchar(50) NOT NULL,
  `status` enum('ACTIVE','INACTIVE') NOT NULL,
  `priv_owner` enum('BO','AGENT','RETAILER') DEFAULT NULL,
  PRIMARY KEY (`email_pid`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC;
--rollback drop table `st_lms_report_email_priviledge_rep`;

CREATE TABLE `st_lms_report_email_user_master` (
  `user_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `ref_user_id` int(10) unsigned DEFAULT NULL,
  `organization_id` int(10) unsigned NOT NULL,
  `organization_type` enum('BO','AGENT','RETAILER') NOT NULL,
  `first_name` varchar(50) NOT NULL,
  `last_name` varchar(50) NOT NULL,
  `email_id` varchar(50) NOT NULL,
  `mob_no` varchar(15) NOT NULL DEFAULT '',
  `registration_date` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `status` enum('ACTIVE','INACTIVE','BLOCK','TERMINATE') NOT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC;
--rollback drop table `st_lms_report_email_user_master`;

CREATE TABLE `st_lms_reports_status` (
  `report_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `report_group_name` varchar(200) NOT NULL COMMENT 'this is priv group name from priv_rep table',
  `report_disp_name` varchar(200) NOT NULL,
  `reporting_from` enum('MAIN_DB','REPLICA_DB') NOT NULL DEFAULT 'MAIN_DB',
  `report_off_start_time` time NOT NULL COMMENT 'HH:mm:ss',
  `report_off_end_time` time NOT NULL COMMENT 'HH:mm:ss',
  `serviceName` enum('MGMT','DG','SE','CS','OLA') NOT NULL,
  `interfaceType` enum('WEB','TERMINAL','API') NOT NULL DEFAULT 'WEB',
  PRIMARY KEY (`report_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
--rollback drop table `st_lms_reports_status`;

CREATE TABLE `st_lms_ret_activityHistory_agentwise` (
  `task_id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `agent_id` int(10) unsigned DEFAULT NULL,
  `date` date DEFAULT NULL,
  `status` enum('ACTIVE','INACTIVE','BLOCK','TERMINATE') DEFAULT NULL,
  `active_Ret` int(11) DEFAULT NULL,
  `newLogin_Ret` int(11) DEFAULT NULL,
  `assigned_total` int(11) DEFAULT NULL,
  `notAssigned_total` int(11) DEFAULT NULL,
  PRIMARY KEY (`task_id`),
  UNIQUE KEY `agent_id` (`agent_id`,`date`),
  UNIQUE KEY `agent_id_2` (`agent_id`,`date`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
--rollback drop table `st_lms_ret_activityHistory_agentwise`;

CREATE TABLE `st_lms_ret_activity_history` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `date` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `live_retailers` int(11) DEFAULT NULL,
  `noSale_retailers` int(11) DEFAULT NULL,
  `inactive_retailers` int(11) DEFAULT NULL,
  `terminated_retailers` int(11) DEFAULT NULL,
  `total_sales` double DEFAULT NULL,
  `total_pwt` double DEFAULT NULL,
  `total_tkt_count` int(11) DEFAULT NULL,
  `total_pwt_count` int(11) DEFAULT NULL,
  `avg_sale_per_ret` double DEFAULT NULL,
  PRIMARY KEY (`id`,`date`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
--rollback drop table `st_lms_ret_activity_history`;

CREATE TABLE `st_lms_ret_offline_master` (
  `user_offline_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(10) unsigned DEFAULT NULL,
  `organization_id` int(10) unsigned DEFAULT NULL,
  `offline_status` enum('ACTIVE','INACTIVE') DEFAULT NULL,
  `login_status` enum('LOGIN','LOGOUT') DEFAULT NULL,
  `last_AFU_time` datetime DEFAULT NULL,
  `is_offline` enum('NO','YES') NOT NULL DEFAULT 'NO',
  `serial_number` varchar(100) DEFAULT NULL,
  `sim1` varchar(50) DEFAULT '-1',
  `sim2` varchar(50) DEFAULT '-1',
  `sim3` varchar(50) DEFAULT '-1',
  `current_version` varchar(10) DEFAULT '0',
  `device_type` varchar(20) DEFAULT NULL,
  `profile` varchar(15) DEFAULT NULL,
  `expected_version` varchar(10) DEFAULT NULL,
  `is_download_available` enum('YES','NO') NOT NULL DEFAULT 'NO',
  `downloaded_on` datetime DEFAULT NULL,
  `last_HBT_time` datetime DEFAULT NULL,
  `dg_last_sale_time` datetime DEFAULT NULL,
  `dg_last_pwt_time` datetime DEFAULT NULL,
  `se_last_sale_time` datetime DEFAULT NULL,
  `se_last_pwt_time` datetime DEFAULT NULL,
  `ola_last_deposit_time` datetime DEFAULT NULL,
  `ola_last_withdrawal_time` datetime DEFAULT NULL,
  `cs_last_sale_time` datetime DEFAULT NULL,
  `sle_last_sale_time` datetime DEFAULT NULL,
  `sle_last_pwt_time` datetime DEFAULT NULL,
  `iw_last_pwt_time` datetime DEFAULT NULL,
  `iw_last_sale_time` datetime DEFAULT NULL,
  `last_connected_through` varchar(20) DEFAULT NULL,
  `last_login_time` datetime DEFAULT NULL,
  `lat` varchar(20) DEFAULT NULL,
  `lon` varchar(20) DEFAULT NULL,
  `city_code` varchar(10) DEFAULT NULL,
  `vs_last_sale_time` datetime DEFAULT NULL,
  `vs_last_pwt_time` datetime DEFAULT NULL,
  `vs_shop_entity_id` varchar(20) DEFAULT NULL,
  `vs_printer_id` varchar(50) DEFAULT NULL,
  `vs_printer_entity_id` varchar(20) DEFAULT NULL,
  `vs_retailer_entiry_id` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`user_offline_id`),
  UNIQUE KEY `organization_id` (`organization_id`),
  UNIQUE KEY `user_id` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
--rollback drop table `st_lms_ret_offline_master`;

CREATE TABLE `st_lms_ret_recon_ticketwise` (
  `task_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `agent_org_id` int(10) unsigned NOT NULL,
  `retailer_org_id` int(10) unsigned NOT NULL,
  `game_id` int(5) unsigned NOT NULL,
  `recon_date` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `type` enum('NET_SALE','N.A.','PWT','Cash','ChqBounce Charges','Cheque Bounce','Credit Note','Debit Note','Cheque') DEFAULT NULL,
  `amt_parent` decimal(20,2) DEFAULT NULL,
  `amt_child` decimal(20,2) DEFAULT NULL,
  `recon_balance` decimal(20,2) DEFAULT NULL,
  `mrp_amt` decimal(20,2) unsigned DEFAULT NULL,
  PRIMARY KEY (`task_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
--rollback drop table `st_lms_ret_recon_ticketwise`;

CREATE TABLE `st_lms_ret_saleHistory_agentwise` (
  `task_id` int(11) unsigned NOT NULL DEFAULT '0',
  `game_id` int(10) unsigned NOT NULL DEFAULT '0',
  `totalSale` decimal(20,2) unsigned DEFAULT NULL,
  PRIMARY KEY (`task_id`,`game_id`),
  CONSTRAINT `st_lms_ret_saleHistory_agentwise_ibfk_1` FOREIGN KEY (`task_id`) REFERENCES `st_lms_ret_activityHistory_agentwise` (`task_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_lms_ret_saleHistory_agentwise`;

CREATE TABLE `st_lms_ret_sold_claim_criteria` (
  `organization_id` int(10) unsigned NOT NULL,
  `claim_at_self_ret` enum('YES','NO') NOT NULL,
  `claim_at_self_agt` enum('YES','NO') NOT NULL,
  `claim_at_other_ret_same_agt` enum('YES','NO') NOT NULL,
  `claim_at_other_ret` enum('YES','NO') NOT NULL,
  `claim_at_other_agt` enum('YES','NO') NOT NULL,
  `claim_at_bo` enum('YES','NO') NOT NULL,
  PRIMARY KEY (`organization_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_lms_ret_sold_claim_criteria`;

CREATE TABLE `st_lms_ret_sold_claim_criteria_history` (
  `task_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `organization_id` int(10) unsigned NOT NULL,
  `claim_at_self_ret` enum('YES','NO') NOT NULL,
  `claim_at_self_agt` enum('YES','NO') NOT NULL,
  `claim_at_other_ret_same_agt` enum('YES','NO') NOT NULL,
  `claim_at_other_ret` enum('YES','NO') NOT NULL,
  `claim_at_other_agt` enum('YES','NO') NOT NULL,
  `claim_at_bo` enum('YES','NO') NOT NULL,
  `update_time` datetime DEFAULT NULL,
  `done_by_user_id` int(11) DEFAULT NULL,
  `request_ip` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`task_id`,`organization_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_lms_ret_sold_claim_criteria_history`;

CREATE TABLE `st_lms_ret_wise_sim_history` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `datetime` datetime NOT NULL,
  `sim_id` int(11) DEFAULT NULL,
  `ret_organization_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC;
--rollback drop table `st_lms_ret_wise_sim_history`;

CREATE TABLE `st_lms_retailer_post_deposit_commission_details` (
  `task_id` tinyint(3) unsigned NOT NULL AUTO_INCREMENT,
  `deposit_default_comm_rate` decimal(5,2) DEFAULT NULL,
  `tax_default_comm_rate` decimal(5,2) DEFAULT NULL,
  `charges_1` decimal(5,2) DEFAULT NULL,
  `charges_2` decimal(5,2) DEFAULT NULL,
  `status` enum('ACTIVE','INACTIVE') DEFAULT NULL,
  PRIMARY KEY (`task_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_lms_retailer_post_deposit_commission_details`;

CREATE TABLE `st_lms_retailer_post_deposit_commission_details_history` (
  `task_id` tinyint(3) unsigned NOT NULL AUTO_INCREMENT,
  `deposit_default_comm_rate` decimal(5,2) DEFAULT NULL,
  `tax_comm_rate` decimal(5,2) DEFAULT NULL,
  `charges_1` decimal(5,2) DEFAULT NULL,
  `charges_2` decimal(5,2) DEFAULT NULL,
  `updated_by_user_id` varchar(10) DEFAULT NULL,
  `updated_date` datetime DEFAULT NULL,
  `status` enum('ACTIVE','INACTIVE') DEFAULT NULL,
  PRIMARY KEY (`task_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_lms_retailer_post_deposit_commission_details_history`;

CREATE TABLE `st_lms_retailer_post_deposit_commission_variance` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `ret_org_id` int(11) DEFAULT NULL,
  `deposit_comm_var` decimal(5,2) DEFAULT NULL,
  `tax_var` decimal(5,2) DEFAULT NULL,
  `charges_1_var` decimal(5,2) DEFAULT NULL,
  `charges_2_var` decimal(5,2) DEFAULT NULL,
  `status` enum('ACTIVE','INACTIVE') DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_lms_retailer_post_deposit_commission_variance`;

CREATE TABLE `st_lms_retailer_post_deposit_commission_variance_history` (
  `task_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `ret_org_id` int(11) DEFAULT NULL,
  `change_comm_rate` decimal(5,2) DEFAULT NULL,
  `commission_type` enum('DEPOSIT','TAX','CHARGES_1','CHARGES_2') DEFAULT NULL,
  `updated_by_user_id` int(11) DEFAULT NULL,
  `updated_date` datetime DEFAULT NULL,
  PRIMARY KEY (`task_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_lms_retailer_post_deposit_commission_variance_history`;

CREATE TABLE `st_lms_retailer_post_deposit_daily_commission` (
  `auto_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `ret_org_id` int(11) DEFAULT NULL,
  `date_time` date DEFAULT NULL,
  `deposit_amount` decimal(10,2) DEFAULT NULL,
  `deposit_comm_rate` decimal(5,2) DEFAULT NULL,
  `deposit_comm_amount` decimal(10,2) DEFAULT NULL,
  `tax_comm_rate` decimal(5,2) DEFAULT NULL,
  `tax_amount` decimal(10,2) DEFAULT NULL,
  `charges_1` decimal(5,2) DEFAULT NULL,
  `charges_2` decimal(5,2) DEFAULT NULL,
  `net_amount_to_pay` decimal(10,2) DEFAULT NULL,
  `status` enum('PENDING','DENIED','APPROVED','PAID') DEFAULT NULL,
  PRIMARY KEY (`auto_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_lms_retailer_post_deposit_daily_commission`;

CREATE TABLE `st_lms_retailer_post_deposit_datewise_commission` (
  `auto_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `ret_org_id` int(11) DEFAULT NULL,
  `start_date` date DEFAULT NULL,
  `end_date` date DEFAULT NULL,
  `deposit_amount` decimal(10,2) DEFAULT NULL,
  `deposit_comm_rate` decimal(5,2) DEFAULT NULL,
  `deposit_comm_amount` decimal(10,2) DEFAULT NULL,
  `tax_comm_rate` decimal(5,2) DEFAULT NULL,
  `tax_amount` decimal(10,2) DEFAULT NULL,
  `charges_1` decimal(5,2) DEFAULT NULL,
  `charges_2` decimal(5,2) DEFAULT NULL,
  `net_amount_to_pay` decimal(10,2) DEFAULT NULL,
  `paid_date` datetime DEFAULT NULL,
  `paid_mode` varchar(20) DEFAULT NULL,
  `paid_by_user_id` int(11) DEFAULT NULL,
  `status` enum('PENDING','DENIED','APPROVED','PAID') DEFAULT NULL,
  PRIMARY KEY (`auto_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_lms_retailer_post_deposit_datewise_commission`;

CREATE TABLE `st_lms_retailer_transaction_master` (
  `transaction_id` bigint(10) unsigned NOT NULL,
  `retailer_user_id` int(10) unsigned NOT NULL,
  `retailer_org_id` int(10) unsigned NOT NULL,
  `game_id` int(10) unsigned NOT NULL,
  `transaction_date` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `transaction_type` enum('DG_SALE','CHEQUE','CHQ_BOUNCE','DR_NOTE','DG_PWT_AUTO','DG_REFUND_CANCEL','PURCHASE','DG_REFUND_FAILED','VAT','SALE','CASH','TDS','PWT_PLR','CR_NOTE_CASH','DR_NOTE_CASH','GOVT_COMM','SALE_RET','PWT','UNCLM_PWT','PWT_AUTO','DG_PWT_PLR','DG_PWT','DG_SALE_OFFLINE','CS_SALE','CS_CANCEL_SERVER','CS_CANCEL_RET','OLA_DEPOSIT','OLA_WITHDRAWL','OLA_DEPOSIT_REFUND','OLA_WITHDRAWL_REFUND','OLA_COMMISSION','SLE_SALE','SLE_REFUND_CANCEL','SLE_PWT','IW_SALE','IW_REFUND_CANCEL','IW_PWT','VS_SALE','VS_SALE_REFUND','VS_PWT') DEFAULT NULL,
  PRIMARY KEY (`transaction_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
--rollback drop table `st_lms_retailer_transaction_master`;

CREATE TABLE `st_lms_rg_criteria_limit` (
  `crit_id` int(20) unsigned NOT NULL AUTO_INCREMENT,
  `criteria` varchar(50) CHARACTER SET utf8 DEFAULT NULL,
  `criteria_type` enum('DAILY','WEEKLY') DEFAULT NULL,
  `criteria_limit` double(20,2) DEFAULT NULL,
  `crit_status` enum('ACTIVE','INACTIVE') DEFAULT NULL,
  `organization_type` enum('BO','AGENT','RETAILER') DEFAULT NULL,
  `crit_action` enum('PWT_HOLD','SALE_HOLD','REPRINT_HOLD','CANCEL_HOLD','INACTIVE_USER','NO_ACTION') DEFAULT NULL,
  `criteria_desc` varchar(500) CHARACTER SET utf8 DEFAULT NULL,
  `service_code` varchar(5) DEFAULT NULL,
  PRIMARY KEY (`crit_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
--rollback drop table `st_lms_rg_criteria_limit`;

CREATE TABLE `st_lms_rg_criteria_limit_history` (
  `task_id` int(20) unsigned NOT NULL AUTO_INCREMENT,
  `date` datetime DEFAULT NULL,
  `crit_id` int(20) DEFAULT NULL,
  `criteria` varchar(50) CHARACTER SET utf8 DEFAULT NULL,
  `criteria_type` enum('DAILY','WEEKLY') DEFAULT NULL,
  `criteria_limit` double(20,2) DEFAULT NULL,
  `crit_status` enum('ACTIVE','INACTIVE') DEFAULT NULL,
  `organization_type` enum('BO','AGENT','RETAILER') DEFAULT NULL,
  `crit_action` enum('PWT_HOLD','SALE_HOLD','REPRINT_HOLD','CANCEL_HOLD','INACTIVE_USER','NO_ACTION') DEFAULT NULL,
  `criteria_desc` varchar(500) CHARACTER SET utf8 DEFAULT NULL,
  `service_code` varchar(5) DEFAULT NULL,
  PRIMARY KEY (`task_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
--rollback drop table `st_lms_rg_criteria_limit_history`;

CREATE TABLE `st_lms_rg_org_daily_tx` (
  `organization_id` int(20) unsigned NOT NULL,
  `user_id` int(20) unsigned DEFAULT NULL,
  `dg_sale_amt` double(20,2) NOT NULL DEFAULT '0.00',
  `dg_pwt_amt` double(20,2) NOT NULL DEFAULT '0.00',
  `dg_reprint_limit` int(10) unsigned NOT NULL DEFAULT '0',
  `dg_cancel_limit` int(10) unsigned NOT NULL DEFAULT '0',
  `dg_invalid_pwt_limit` int(10) unsigned NOT NULL DEFAULT '0',
  `se_sale_amt` double(20,2) NOT NULL DEFAULT '0.00',
  `se_pwt_amt` double(20,2) NOT NULL DEFAULT '0.00',
  `se_invalid_pwt_limit` int(10) unsigned NOT NULL DEFAULT '0',
  `dg_offline_lateupload` int(10) unsigned NOT NULL DEFAULT '0',
  `dg_offline_errorfile` int(10) unsigned NOT NULL DEFAULT '0',
  `sle_sale_amt` double(20,2) NOT NULL DEFAULT '0.00',
  `sle_pwt_amt` double(20,2) NOT NULL DEFAULT '0.00',
  `sle_reprint_limit` int(10) unsigned NOT NULL DEFAULT '0',
  `sle_cancel_limit` int(10) unsigned NOT NULL DEFAULT '0',
  `sle_invalid_pwt_limit` int(10) unsigned NOT NULL DEFAULT '0',
  `iw_sale_amt` double(20,2) NOT NULL DEFAULT '0.00',
  `iw_pwt_amt` double(20,2) NOT NULL DEFAULT '0.00',
  `iw_invalid_pwt_limit` int(10) unsigned NOT NULL DEFAULT '0',
  `iw_reprint_limit` int(10) unsigned NOT NULL DEFAULT '0',
  `vs_sale_amt` double(20,2) NOT NULL DEFAULT '0.00',
  `vs_pwt_amt` double(20,2) NOT NULL DEFAULT '0.00',
  `vs_invalid_pwt_limit` int(10) unsigned NOT NULL DEFAULT '0',
  `vs_cancel_limit` int(10) unsigned NOT NULL DEFAULT '0',
  `vs_reprint_limit` int(10) unsigned NOT NULL DEFAULT '0',
  `cs_reprint_limit` int(10) DEFAULT NULL,
  PRIMARY KEY (`organization_id`),
  UNIQUE KEY `user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_lms_rg_org_daily_tx`;

CREATE TABLE `st_lms_rg_org_daily_tx_bak` (
  `organization_id` int(20) unsigned NOT NULL,
  `user_id` int(20) unsigned DEFAULT NULL,
  `dg_sale_amt` double(20,2) NOT NULL DEFAULT '0.00',
  `dg_pwt_amt` double(20,2) NOT NULL DEFAULT '0.00',
  `dg_reprint_limit` int(10) unsigned NOT NULL DEFAULT '0',
  `dg_cancel_limit` int(10) unsigned NOT NULL DEFAULT '0',
  `dg_invalid_pwt_limit` int(10) unsigned NOT NULL DEFAULT '0',
  `se_sale_amt` double(20,2) NOT NULL DEFAULT '0.00',
  `se_pwt_amt` double(20,2) NOT NULL DEFAULT '0.00',
  `se_invalid_pwt_limit` int(10) unsigned NOT NULL DEFAULT '0',
  `cs_reprint_limit` int(10) unsigned NOT NULL DEFAULT '0',
  `dg_offline_lateupload` int(10) unsigned NOT NULL DEFAULT '0',
  `dg_offline_errorfile` int(10) unsigned NOT NULL DEFAULT '0',
  `sle_sale_amt` double(20,2) NOT NULL,
  `sle_pwt_amt` double(20,2) NOT NULL,
  `sle_reprint_limit` int(10) unsigned NOT NULL,
  `sle_cancel_limit` int(10) unsigned NOT NULL,
  `sle_invalid_pwt_limit` int(10) unsigned NOT NULL,
  UNIQUE KEY `user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_lms_rg_org_daily_tx_bak`;

CREATE TABLE `st_lms_rg_org_daily_tx_history` (
  `organization_id` int(20) unsigned NOT NULL,
  `user_id` int(20) unsigned DEFAULT NULL,
  `date` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `dg_sale_amt` double(20,2) NOT NULL DEFAULT '0.00',
  `dg_pwt_amt` double(20,2) NOT NULL DEFAULT '0.00',
  `dg_reprint_limit` int(10) unsigned NOT NULL DEFAULT '0',
  `dg_cancel_limit` int(10) unsigned NOT NULL DEFAULT '0',
  `dg_invalid_pwt_limit` int(10) unsigned NOT NULL DEFAULT '0',
  `se_sale_amt` double(20,2) NOT NULL DEFAULT '0.00',
  `se_pwt_amt` double(20,2) NOT NULL DEFAULT '0.00',
  `se_invalid_pwt_limit` int(10) unsigned NOT NULL DEFAULT '0',
  `dg_offline_lateupload` int(10) unsigned NOT NULL DEFAULT '0',
  `dg_offline_errorfile` int(10) unsigned NOT NULL DEFAULT '0',
  `sle_sale_amt` double(20,2) NOT NULL DEFAULT '0.00',
  `sle_pwt_amt` double(20,2) NOT NULL DEFAULT '0.00',
  `sle_reprint_limit` int(10) unsigned NOT NULL DEFAULT '0',
  `sle_cancel_limit` int(10) unsigned NOT NULL DEFAULT '0',
  `sle_invalid_pwt_limit` int(10) unsigned NOT NULL DEFAULT '0',
  `iw_sale_amt` double(20,2) NOT NULL DEFAULT '0.00',
  `iw_pwt_amt` double(20,2) NOT NULL DEFAULT '0.00',
  `iw_reprint_limit` int(10) unsigned NOT NULL DEFAULT '0',
  `iw_invalid_pwt_limit` int(10) unsigned NOT NULL DEFAULT '0',
  `vs_sale_amt` double(20,2) NOT NULL DEFAULT '0.00',
  `vs_pwt_amt` double(20,2) NOT NULL DEFAULT '0.00',
  `vs_reprint_limit` int(10) unsigned NOT NULL DEFAULT '0',
  `vs_cancel_limit` int(10) unsigned NOT NULL DEFAULT '0',
  `vs_invalid_pwt_limit` int(10) unsigned NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_lms_rg_org_daily_tx_history`;

CREATE TABLE `st_lms_rg_org_weakly_tx_history` (
  `organization_id` int(20) unsigned NOT NULL,
  `user_id` int(20) unsigned DEFAULT NULL,
  `startdate` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `enddate` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `dg_sale_amt` double(20,2) NOT NULL DEFAULT '0.00',
  `dg_pwt_amt` double(20,2) NOT NULL DEFAULT '0.00',
  `dg_reprint_limit` int(10) unsigned NOT NULL DEFAULT '0',
  `dg_cancel_limit` int(10) unsigned NOT NULL DEFAULT '0',
  `dg_invalid_pwt_limit` int(10) unsigned NOT NULL DEFAULT '0',
  `se_sale_amt` double(20,2) NOT NULL DEFAULT '0.00',
  `se_pwt_amt` double(20,2) NOT NULL DEFAULT '0.00',
  `se_invalid_pwt_limit` int(10) unsigned NOT NULL DEFAULT '0',
  `dg_offline_lateupload` int(10) unsigned NOT NULL DEFAULT '0',
  `dg_offline_errorfile` int(10) unsigned NOT NULL DEFAULT '0',
  `sle_sale_amt` double(20,2) NOT NULL DEFAULT '0.00',
  `sle_pwt_amt` double(20,2) NOT NULL DEFAULT '0.00',
  `sle_reprint_limit` int(10) unsigned NOT NULL DEFAULT '0',
  `sle_cancel_limit` int(10) unsigned NOT NULL DEFAULT '0',
  `sle_invalid_pwt_limit` int(10) unsigned NOT NULL DEFAULT '0',
  `iw_sale_amt` double(20,2) NOT NULL DEFAULT '0.00',
  `iw_pwt_amt` double(20,2) NOT NULL DEFAULT '0.00',
  `iw_reprint_limit` int(10) unsigned NOT NULL DEFAULT '0',
  `iw_invalid_pwt_limit` int(10) unsigned NOT NULL DEFAULT '0',
  `vs_sale_amt` double(20,2) NOT NULL DEFAULT '0.00',
  `vs_pwt_amt` double(20,2) NOT NULL DEFAULT '0.00',
  `vs_reprint_limit` int(10) unsigned NOT NULL DEFAULT '0',
  `vs_cancel_limit` int(10) unsigned NOT NULL DEFAULT '0',
  `vs_invalid_pwt_limit` int(10) unsigned NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_lms_rg_org_weakly_tx_history`;

CREATE TABLE `st_lms_rg_org_weekly_tx` (
  `organization_id` int(20) unsigned NOT NULL,
  `user_id` int(20) unsigned DEFAULT NULL,
  `startdate` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `dg_sale_amt` double(20,2) NOT NULL DEFAULT '0.00',
  `dg_pwt_amt` double(20,2) NOT NULL DEFAULT '0.00',
  `dg_reprint_limit` int(10) unsigned NOT NULL DEFAULT '0',
  `dg_cancel_limit` int(10) unsigned NOT NULL DEFAULT '0',
  `dg_invalid_pwt_limit` int(10) unsigned NOT NULL DEFAULT '0',
  `se_sale_amt` double(20,2) NOT NULL DEFAULT '0.00',
  `se_pwt_amt` double(20,2) NOT NULL DEFAULT '0.00',
  `se_invalid_pwt_limit` int(10) unsigned NOT NULL DEFAULT '0',
  `dg_offline_lateupload` int(10) unsigned NOT NULL DEFAULT '0',
  `dg_offline_errorfile` int(10) unsigned NOT NULL DEFAULT '0',
  `sle_sale_amt` double(20,2) NOT NULL DEFAULT '0.00',
  `sle_pwt_amt` double(20,2) NOT NULL DEFAULT '0.00',
  `sle_reprint_limit` int(10) unsigned NOT NULL DEFAULT '0',
  `sle_cancel_limit` int(10) unsigned NOT NULL DEFAULT '0',
  `sle_invalid_pwt_limit` int(10) unsigned NOT NULL DEFAULT '0',
  `iw_sale_amt` double(20,2) NOT NULL DEFAULT '0.00',
  `iw_pwt_amt` double(20,2) NOT NULL DEFAULT '0.00',
  `iw_invalid_pwt_limit` int(10) unsigned NOT NULL DEFAULT '0',
  `iw_reprint_limit` int(10) unsigned NOT NULL DEFAULT '0',
  `vs_sale_amt` double(20,2) NOT NULL DEFAULT '0.00',
  `vs_pwt_amt` double(20,2) NOT NULL DEFAULT '0.00',
  `vs_invalid_pwt_limit` int(10) unsigned NOT NULL DEFAULT '0',
  `vs_cancel_limit` int(10) unsigned NOT NULL DEFAULT '0',
  `vs_reprint_limit` int(10) unsigned NOT NULL DEFAULT '0',
  `cs_reprint_limit` int(10) DEFAULT NULL,
  PRIMARY KEY (`organization_id`),
  UNIQUE KEY `user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_lms_rg_org_weekly_tx`;

CREATE TABLE `st_lms_role_agent_mapping` (
  `role_id` int(10) NOT NULL,
  `agent_id` int(10) NOT NULL,
  PRIMARY KEY (`role_id`,`agent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_lms_role_agent_mapping`;

CREATE TABLE `st_lms_role_master` (
  `role_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `role_name` varchar(30) NOT NULL,
  `role_description` varchar(30) DEFAULT NULL,
  `tier_id` tinyint(4) DEFAULT NULL,
  `owner_org_id` int(10) DEFAULT NULL,
  `is_master` enum('Y','N') DEFAULT NULL,
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC;
--rollback drop table `st_lms_role_master`;

CREATE TABLE `st_lms_role_priv_mapping` (
  `role_id` int(10) NOT NULL,
  `priv_id` int(10) NOT NULL,
  `service_mapping_id` tinyint(4) NOT NULL,
  `status` enum('ACTIVE','INACTIVE') NOT NULL,
  PRIMARY KEY (`role_id`,`priv_id`,`service_mapping_id`,`status`),
  KEY `FKAE76D2232F1555B0` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_lms_role_priv_mapping`;

CREATE TABLE `st_lms_scheduler_history` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `scheId` tinyint(4) unsigned NOT NULL,
  `status` enum('ACTIVE','INACTIVE') DEFAULT NULL,
  `last_start_time` datetime DEFAULT NULL,
  `last_end_time` datetime DEFAULT NULL,
  `last_status` enum('DONE','RUNNING','ERROR') DEFAULT NULL,
  `status_msg` varchar(100) DEFAULT NULL,
  `record_insertion_time` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`id`),
  UNIQUE KEY `record_insertion_time` (`scheId`,`record_insertion_time`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
--rollback drop table `st_lms_scheduler_history`;

CREATE TABLE `st_lms_scheduler_master` (
  `id` tinyint(4) unsigned NOT NULL AUTO_INCREMENT,
  `dev_name` varchar(50) NOT NULL,
  `display_name` varchar(50) DEFAULT NULL,
  `jobGroup` varchar(50) DEFAULT NULL,
  `status` enum('ACTIVE','INACTIVE') DEFAULT NULL,
  `scheduled_Time` varchar(20) DEFAULT NULL,
  `last_start_time` datetime DEFAULT NULL,
  `last_end_time` datetime DEFAULT NULL,
  `last_status` enum('DONE','RUNNING','ERROR') DEFAULT NULL,
  `last_success_time` datetime DEFAULT NULL,
  `estimated_time` int(6) DEFAULT NULL COMMENT 'Scheuler Estimated Duration In Seconds',
  `status_msg` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `dev_name` (`dev_name`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
--rollback drop table `st_lms_scheduler_master`;

CREATE TABLE `st_lms_server_info_master` (
  `server_id` int(11) NOT NULL AUTO_INCREMENT,
  `server_code` char(5) DEFAULT NULL,
  `host_address` varchar(16) DEFAULT NULL,
  `local_address` varchar(50) DEFAULT NULL,
  `protocol` varchar(5) DEFAULT NULL,
  `project_name` varchar(20) DEFAULT NULL,
  `port` varchar(6) DEFAULT NULL,
  `status` enum('ACTIVE','INACTIVE') DEFAULT NULL,
  PRIMARY KEY (`server_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
--rollback drop table `st_lms_server_info_master`;

CREATE TABLE `st_lms_service_delivery_master` (
  `service_delivery_master_id` tinyint(4) unsigned NOT NULL AUTO_INCREMENT,
  `service_id` tinyint(4) unsigned NOT NULL,
  `channel` enum('RETAIL','PLAYER') DEFAULT NULL,
  `interface` enum('WEB','TERMINAL') DEFAULT NULL,
  `tier_id` tinyint(4) unsigned NOT NULL,
  `status` enum('ACTIVE','INACTIVE') DEFAULT NULL,
  `priv_rep_table` varchar(50) DEFAULT NULL,
  `menu_master_table` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`service_delivery_master_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
--rollback drop table `st_lms_service_delivery_master`;

CREATE TABLE `st_lms_service_master` (
  `service_id` tinyint(3) unsigned NOT NULL AUTO_INCREMENT,
  `service_name` varchar(30) DEFAULT NULL,
  `service_display_name` varchar(30) DEFAULT NULL,
  `service_code` varchar(5) DEFAULT NULL,
  `status` enum('ACTIVE','INACTIVE') DEFAULT NULL,
  `ref_merchant_id` varchar(20) DEFAULT NULL,
  `service_deligate_url` varchar(50) DEFAULT NULL,
  `service_delivery_date` date NOT NULL,
  PRIMARY KEY (`service_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
--rollback drop table `st_lms_service_master`;

CREATE TABLE `st_lms_service_role_mapping` (
  `id` tinyint(4) unsigned NOT NULL,
  `role_id` int(10) unsigned NOT NULL,
  `organization_id` int(10) unsigned NOT NULL,
  `status` enum('ACTIVE','INACTIVE') NOT NULL,
  PRIMARY KEY (`id`,`role_id`,`organization_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_lms_service_role_mapping`;

CREATE TABLE `st_lms_state_master` (
  `state_code` varchar(10) NOT NULL,
  `country_code` varchar(10) NOT NULL,
  `name` varchar(30) NOT NULL,
  `no_agt_registered` int(5) NOT NULL,
  `no_ret_registered` int(5) NOT NULL,
  `status` enum('ACTIVE','INACTIVE') NOT NULL,
  PRIMARY KEY (`state_code`),
  KEY `country_code` (`country_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;
--rollback drop table `st_lms_state_master`;

CREATE TABLE `st_lms_terminal_mapping` (
  `terminal_mapping_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `lms_related_to` enum('DRAW_GAMES','SCRATCH_OFF_GAMES','ORDER_MGMT','REPORTS','INSTANT_PRINT') DEFAULT NULL,
  `lms_group_name` varchar(50) DEFAULT NULL,
  `lms_priv_name` varchar(50) DEFAULT NULL,
  `terminal_id` tinyint(10) DEFAULT NULL,
  `version_id` double(10,2) DEFAULT NULL,
  `terminal_related_to` enum('DRAW_GAMES','Scratch_Off_Games','Reports','Inst.Print_Games') DEFAULT NULL,
  `terminal_group` varchar(50) DEFAULT NULL,
  `terminal_priv` varchar(50) DEFAULT NULL,
  `user_type` enum('ONLINE','OFFLINE') NOT NULL,
  PRIMARY KEY (`terminal_mapping_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
--rollback drop table `st_lms_terminal_mapping`;

CREATE TABLE `st_lms_test_details` (
  `id` bigint(10) unsigned NOT NULL AUTO_INCREMENT,
  `ip` varchar(20) DEFAULT NULL,
  `port` char(10) DEFAULT NULL,
  `project_name` varchar(50) DEFAULT NULL,
  `date` date DEFAULT NULL,
  `time` time DEFAULT NULL,
  `cid` bigint(10) DEFAULT NULL,
  `lac` bigint(10) DEFAULT NULL,
  `response_time` varchar(50) DEFAULT NULL,
  `sigal_level` int(5) DEFAULT NULL,
  `request_counter` int(5) DEFAULT NULL,
  `response_counter` int(5) DEFAULT NULL,
  `sim_1` varchar(100) DEFAULT NULL,
  `sim_2` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
--rollback drop table `st_lms_test_details`;

CREATE TABLE `st_lms_tier_master` (
  `tier_id` tinyint(4) unsigned NOT NULL AUTO_INCREMENT,
  `tier_name` varchar(20) DEFAULT NULL,
  `tier_code` varchar(10) DEFAULT NULL,
  `tier_status` enum('ACTIVE','INACTIVE') DEFAULT NULL,
  `parent_tier_id` tinyint(4) unsigned NOT NULL,
  PRIMARY KEY (`tier_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
--rollback drop table `st_lms_tier_master`;

CREATE TABLE `st_lms_tp_system_txn_mapping` (
  `tp_system_id` int(10) NOT NULL,
  `lms_txn_id` bigint(11) NOT NULL,
  `tp_ref_txn_id` varchar(80) NOT NULL,
  KEY `tp_system_id` (`tp_system_id`,`tp_ref_txn_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=COMPACT;
--rollback drop table `st_lms_tp_system_txn_mapping`;

CREATE TABLE `st_lms_tp_txn_details` (
  `agent_trans_id` bigint(20) DEFAULT NULL,
  `retailer_trans_id` bigint(20) DEFAULT NULL,
  `tp_trans_id` varchar(80) DEFAULT NULL,
  `transaction_date` datetime DEFAULT NULL,
  `bank_name` varchar(100) DEFAULT NULL,
  `branch_name` varchar(100) DEFAULT NULL,
  `cashier_name` varchar(100) DEFAULT NULL,
  `region_name` varchar(100) DEFAULT NULL,
  UNIQUE KEY `tp_trans_id` (`tp_trans_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
--rollback drop table `st_lms_tp_txn_details`;

CREATE TABLE `st_lms_tp_txn_mapping` (
  `retailer_org_id` int(10) NOT NULL,
  `lms_txn_id` bigint(11) NOT NULL,
  `tp_ref_txn_id` bigint(11) NOT NULL,
  `mobile_no` varchar(20) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
--rollback drop table `st_lms_tp_txn_mapping`;

CREATE TABLE `st_lms_track_ticket_data` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `req_by_user_id` int(11) DEFAULT NULL,
  `ticket_number` bigint(20) unsigned DEFAULT NULL,
  `ticket_format` enum('OLD','NEW') DEFAULT NULL,
  `remarks` varchar(200) DEFAULT NULL,
  `entry_time` datetime DEFAULT NULL,
  `request_ip` varchar(30) DEFAULT NULL,
  `status` enum('VALID','INVALID') DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
--rollback drop table `st_lms_track_ticket_data`;

CREATE TABLE `st_lms_track_ticket_user_details` (
  `req_by_user_id` int(10) unsigned NOT NULL,
  `auth_attempt` int(11) DEFAULT NULL,
  `unauth_attempt` int(11) DEFAULT NULL,
  PRIMARY KEY (`req_by_user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_lms_track_ticket_user_details`;

CREATE TABLE `st_lms_track_ticket_user_details_history` (
  `req_by_user_id` int(10) DEFAULT NULL,
  `auth_attempt` int(11) DEFAULT NULL,
  `unauth_attempt` int(11) DEFAULT NULL,
  `history_date` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_lms_track_ticket_user_details_history`;

CREATE TABLE `st_lms_user_branch_mapping` (
  `user_id` int(10) unsigned NOT NULL DEFAULT '0',
  `bank_id` int(10) DEFAULT NULL,
  `branch_id` int(10) DEFAULT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_lms_user_branch_mapping`;

CREATE TABLE `st_lms_user_branch_mapping_history` (
  `current_user_id` int(10) DEFAULT NULL,
  `bank_id` int(10) DEFAULT NULL,
  `branch_id` int(10) DEFAULT NULL,
  `current_branch_id` int(10) DEFAULT NULL,
  `date_changes` datetime DEFAULT NULL,
  `done_by_user_id` int(10) DEFAULT NULL,
  `comment` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_lms_user_branch_mapping_history`;

CREATE TABLE `st_lms_user_contact_details` (
  `user_id` int(10) unsigned NOT NULL,
  `first_name` varchar(30) NOT NULL,
  `last_name` varchar(30) NOT NULL,
  `id_type` enum('Passport','Driving Licence','Voter Id','Bank Statement','NRC','Pan Card','NID','Others','IFU Code') DEFAULT NULL,
  `id_nbr` varchar(30) DEFAULT NULL,
  `email_id` varchar(50) NOT NULL DEFAULT '',
  `phone_nbr` varchar(15) DEFAULT NULL,
  `mobile_nbr` varchar(15) DEFAULT '0',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC;
--rollback drop table `st_lms_user_contact_details`;

CREATE TABLE `st_lms_user_inbox_message_mapping` (
  `inbox_message_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `message_id` int(11) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  `send_date` datetime DEFAULT NULL COMMENT 'in seconds',
  `read_date` datetime DEFAULT NULL,
  `delete_date` datetime DEFAULT NULL,
  `remove_date` datetime DEFAULT NULL,
  `status` enum('READ','UNREAD','DELETE','REMOVE') DEFAULT NULL,
  PRIMARY KEY (`inbox_message_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
--rollback drop table `st_lms_user_inbox_message_mapping`;

CREATE TABLE `st_lms_user_ip_group` (
  `group_id` int(11) DEFAULT NULL,
  `group_name` varchar(20) DEFAULT NULL,
  `allowed_ip` varchar(100) DEFAULT NULL,
  `status` enum('ACTIVE','INACTIVE') DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC;
--rollback drop table `st_lms_user_ip_group`;
CREATE TABLE `st_lms_user_master` (
  `user_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `organization_id` int(10) unsigned NOT NULL,
  `role_id` int(10) unsigned NOT NULL,
  `parent_user_id` int(10) unsigned NOT NULL,
  `organization_type` enum('BO','AGENT','RETAILER') NOT NULL,
  `registration_date` datetime DEFAULT NULL,
  `last_login_date` datetime DEFAULT NULL,
  `auto_password` tinyint(1) DEFAULT NULL,
  `user_name` char(30) NOT NULL,
  `password` varchar(30) NOT NULL,
  `status` enum('ACTIVE','INACTIVE','BLOCK','TERMINATE') NOT NULL,
  `secret_ques` varchar(50) NOT NULL,
  `secret_ans` varchar(50) DEFAULT NULL,
  `isrolehead` enum('Y','N') DEFAULT NULL,
  `login_attempts` int(10) unsigned NOT NULL DEFAULT '0',
  `user_type` enum('WRAPPER') DEFAULT NULL,
  `termination_date` datetime DEFAULT NULL,
  `user_session` varchar(50) DEFAULT NULL,
  `register_by_id` int(10) DEFAULT NULL,
  `tp_user_id` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `user_name` (`user_name`),
  KEY `organization_id` (`organization_id`),
  KEY `role_id` (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC;
--rollback drop table `st_lms_user_master`;

CREATE TABLE `st_lms_user_ip_group_mapping` (
  `group_id` int(11) DEFAULT NULL,
  `user_id` int(10) unsigned DEFAULT NULL,
  `status` enum('ACTIVE','INACTIVE') DEFAULT NULL,
  KEY `FK_st_lms_user_ip_group_mapping_usrid` (`user_id`),
  CONSTRAINT `FK_st_lms_user_ip_group_mapping_usrid` FOREIGN KEY (`user_id`) REFERENCES `st_lms_user_master` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC;
--rollback drop table `st_lms_user_ip_group_mapping`;

CREATE TABLE `st_lms_user_ip_time_mapping` (
  `user_id` int(10) unsigned NOT NULL,
  `allowed_ip` varchar(100) DEFAULT NULL,
  `monday_start_time` time DEFAULT NULL,
  `monday_end_time` time DEFAULT NULL,
  `tuesday_start_time` time DEFAULT NULL,
  `tuesday_end_time` time DEFAULT NULL,
  `wednesday_start_time` time DEFAULT NULL,
  `wednesday_end_time` time DEFAULT NULL,
  `thursday_start_time` time DEFAULT NULL,
  `thursday_end_time` time DEFAULT NULL,
  `friday_start_time` time DEFAULT NULL,
  `friday_end_time` time DEFAULT NULL,
  `saturday_start_time` time DEFAULT NULL,
  `saturday_end_time` time DEFAULT NULL,
  `sunday_start_time` time DEFAULT NULL,
  `sunday_end_time` time DEFAULT NULL,
  `status` enum('ACTIVE','INACTIVE') DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `st_lms_user_ip_time_mapping_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `st_lms_user_master` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_lms_user_ip_time_mapping`;


CREATE TABLE `st_lms_user_master_history` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `change_type` enum('PASSWORD','EMAIL_ID','MOBILE_NUMBER','USER_STATUS','PHONE_NUMBER') DEFAULT NULL,
  `change_value` varchar(150) DEFAULT NULL,
  `change_time` datetime DEFAULT NULL,
  `done_by_user_id` int(11) DEFAULT NULL,
  `comments` varchar(500) DEFAULT NULL,
  `request_ip` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
--rollback drop table `st_lms_user_master_history`;

CREATE TABLE `st_lms_user_message_detail` (
  `message_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `message_type_id` int(11) DEFAULT NULL,
  `message_subject` varchar(100) DEFAULT NULL,
  `message_body` varchar(300) DEFAULT NULL,
  `message_date` datetime DEFAULT NULL,
  `expiry_period` datetime DEFAULT NULL,
  `is_popup` enum('YES','NO') DEFAULT NULL,
  `is_mandatory` enum('YES','NO') DEFAULT NULL,
  `creator_user_id` int(11) DEFAULT NULL,
  `status` enum('ACTIVE','INACTIVE') DEFAULT NULL,
  PRIMARY KEY (`message_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
--rollback drop table `st_lms_user_message_detail`;

CREATE TABLE `st_lms_user_message_detail_history` (
  `message_id` int(10) DEFAULT NULL,
  `message_subject` varchar(100) DEFAULT NULL,
  `message_body` varchar(300) DEFAULT NULL,
  `message_date` datetime DEFAULT NULL,
  `expiry_period` datetime DEFAULT NULL,
  `message_type` enum('INBOX','FLASH') DEFAULT NULL,
  `is_popup` enum('YES','NO') DEFAULT NULL,
  `is_mandatory` enum('YES','NO') DEFAULT NULL,
  `creator_user_id` int(11) DEFAULT NULL,
  `user_type` enum('RETAILER','AGENT','BO') DEFAULT NULL,
  `interface_type` enum('WEB','TERMINAL','FFT') DEFAULT NULL,
  `status` enum('ACTIVE','INACTIVE') DEFAULT NULL,
  `update_on` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_lms_user_message_detail_history`;

CREATE TABLE `st_lms_user_message_type_master` (
  `message_type_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `type_dev_name` enum('INBOX','FLASH','REGISTRATION') DEFAULT NULL,
  `type_disp_name` varchar(20) DEFAULT NULL,
  `is_inbox_message` enum('YES','NO') DEFAULT NULL,
  `user_type` enum('BO','AGENT','RETAILER') DEFAULT NULL,
  `interface_type` enum('WEB','TERMINAL','FFT') DEFAULT NULL,
  `no_of_messages` int(11) DEFAULT NULL,
  `status` enum('ACTIVE','INACTIVE') DEFAULT NULL,
  PRIMARY KEY (`message_type_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
--rollback drop table `st_lms_user_message_type_master`;

CREATE TABLE `st_lms_user_priv_history` (
  `user_id` int(11) DEFAULT NULL,
  `priv_id` int(11) DEFAULT NULL,
  `service_mapping_id` int(11) DEFAULT NULL,
  `status` enum('ACTIVE','INACTIVE','NA') DEFAULT NULL,
  `change_date` datetime DEFAULT NULL,
  `change_by` int(11) DEFAULT NULL,
  `request_ip` varchar(30) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_lms_user_priv_history`;

CREATE TABLE `st_lms_user_priv_mapping` (
  `user_id` int(10) unsigned NOT NULL,
  `role_id` int(10) unsigned NOT NULL,
  `priv_id` int(10) unsigned NOT NULL,
  `service_mapping_id` tinyint(4) unsigned NOT NULL,
  `status` enum('ACTIVE','INACTIVE','NA') NOT NULL,
  `change_date` datetime DEFAULT NULL,
  `change_by` int(11) DEFAULT NULL,
  `request_ip` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`user_id`,`role_id`,`priv_id`,`service_mapping_id`,`status`),
  KEY `FK_user_priv_master` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_lms_user_priv_mapping`;

CREATE TABLE `st_lms_user_random_id_generation_status` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `server_id` int(11) DEFAULT NULL,
  `gen_date` datetime DEFAULT NULL,
  `code_expiry_days` int(11) DEFAULT NULL,
  `last_exp_date` datetime DEFAULT NULL,
  `status` enum('PENDING','SUCCESS') DEFAULT 'PENDING',
  `reason` varchar(50) DEFAULT NULL,
  `start_range` int(11) NOT NULL,
  `end_range` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
--rollback drop table `st_lms_user_random_id_generation_status`;

CREATE TABLE `st_lms_user_random_id_mapping` (
  `user_id` int(10) unsigned NOT NULL,
  `user_mapping_id` int(11) NOT NULL,
  `mapping_id_gen_date` datetime NOT NULL,
  `code_expiry` datetime NOT NULL,
  `adv_user_mapping_id` int(11) NOT NULL,
  `adv_mapping_id_gen_date` datetime NOT NULL,
  `adv_code_expiry` datetime NOT NULL,
  PRIMARY KEY (`user_mapping_id`,`mapping_id_gen_date`),
  UNIQUE KEY `user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_lms_user_random_id_mapping`;

CREATE TABLE `st_lms_user_random_id_mapping_history` (
  `user_id` int(10) unsigned NOT NULL,
  `user_mapping_id` int(11) NOT NULL,
  `mapping_id_gen_date` datetime NOT NULL,
  `code_expiry` datetime NOT NULL,
  `activity` enum('MANUAL_GEN','AUTO_GEN','RET_SHIFT','RET_REG') NOT NULL,
  `done_by` int(11) NOT NULL,
  UNIQUE KEY `user_id` (`user_id`,`mapping_id_gen_date`),
  CONSTRAINT `st_lms_user_random_id_mapping_history_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `st_lms_user_random_id_mapping` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_lms_user_random_id_mapping_history`;

CREATE TABLE `st_lms_wrapper_authentication_master` (
  `id` tinyint(3) unsigned NOT NULL AUTO_INCREMENT,
  `system_ip` char(100) DEFAULT NULL,
  `system_username` char(30) DEFAULT NULL,
  `system_password` char(30) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
--rollback drop table `st_lms_wrapper_authentication_master`;

CREATE TABLE `st_ola_affiliate_plr_mapping` (
  `player_id` int(10) NOT NULL,
  `ref_user_id` int(10) NOT NULL,
  `ref_user_org_type` enum('BO','AGENT','RETAILER') DEFAULT NULL,
  `ref_user_org_id` int(11) NOT NULL,
  `wallet_id` tinyint(3) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC;
--rollback drop table `st_ola_affiliate_plr_mapping`;

CREATE TABLE `st_ola_affiliate_plr_mapping_history` (
  `plr_id` int(10) DEFAULT NULL,
  `ref_user_id` int(10) DEFAULT NULL,
  `date_changed` datetime NOT NULL,
  `user_system` enum('OLA','PT') NOT NULL,
  `done_by_user_id` int(10) DEFAULT NULL,
  `remarks` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_ola_affiliate_plr_mapping_history`;

CREATE TABLE `st_ola_agent_retailer_comm_variance` (
  `agent_org_id` int(10) unsigned NOT NULL,
  `retailer_org_id` int(10) NOT NULL,
  `wallet_id` int(10) unsigned NOT NULL,
  `deposit_comm_variance` decimal(5,2) DEFAULT NULL,
  `default_deposit_comm_rate` decimal(5,2) DEFAULT NULL,
  `withdrawl_comm_variance` decimal(5,2) DEFAULT NULL,
  `default_withdrawl_comm_rate` decimal(5,2) DEFAULT NULL,
  `net_gaming_comm_variance` decimal(5,2) DEFAULT NULL,
  `default_net_gaming_comm_rate` decimal(5,2) DEFAULT NULL,
  PRIMARY KEY (`agent_org_id`,`retailer_org_id`,`wallet_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_ola_agent_retailer_comm_variance`;

CREATE TABLE `st_ola_agent_retailer_deposit_comm_variance_history` (
  `agent_org_id` int(10) unsigned NOT NULL,
  `retialer_org_id` int(10) unsigned NOT NULL,
  `wallet_id` int(10) unsigned NOT NULL,
  `deposit_comm_variance` decimal(5,2) NOT NULL,
  `date_changed` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_ola_agent_retailer_deposit_comm_variance_history`;

CREATE TABLE `st_ola_agent_retailer_plr_comm_variance_history` (
  `agent_org_id` int(10) unsigned NOT NULL,
  `retialer_org_id` int(10) unsigned NOT NULL,
  `wallet_id` int(10) unsigned NOT NULL,
  `net_gaming_comm_variance` decimal(5,2) NOT NULL,
  `date_changed` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_ola_agent_retailer_plr_comm_variance_history`;

CREATE TABLE `st_ola_agent_retailer_withdrawl_comm_variance_history` (
  `agent_org_id` int(10) unsigned NOT NULL,
  `retailer_org_id` int(10) unsigned NOT NULL,
  `wallet_id` int(10) unsigned NOT NULL,
  `withdrawl_comm_variance` decimal(5,2) NOT NULL,
  `date_changed` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_ola_agent_retailer_withdrawl_comm_variance_history`;

CREATE TABLE `st_ola_agt_comm` (
  `transaction_id` bigint(20) unsigned NOT NULL,
  `wallet_id` int(10) unsigned NOT NULL,
  `agent_user_id` int(10) unsigned NOT NULL,
  `agent_org_id` int(10) unsigned NOT NULL,
  `retailer_org_id` int(11) DEFAULT NULL,
  `agt_claim_comm` decimal(20,2) NOT NULL DEFAULT '0.00',
  `tds_comm_rate` decimal(10,2) DEFAULT NULL,
  `agt_net_claim_comm` double(20,2) DEFAULT NULL,
  `ret_mrp_comm` decimal(20,2) NOT NULL,
  `ret_claim_comm` decimal(20,2) NOT NULL,
  `status` enum('CLAIM_BAL','UNCLAIM_BAL','DONE_CLM') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_ola_agt_comm`;

CREATE TABLE `st_ola_agt_deposit` (
  `transaction_id` bigint(20) unsigned NOT NULL,
  `wallet_id` int(10) unsigned NOT NULL,
  `agent_org_id` int(10) unsigned NOT NULL,
  `retailer_org_id` int(10) unsigned NOT NULL,
  `deposit_amt` decimal(20,2) DEFAULT NULL,
  `agent_comm` decimal(20,2) DEFAULT NULL,
  `net_amt` decimal(20,2) DEFAULT NULL,
  `bo_net_amt` decimal(20,2) DEFAULT '0.00',
  `retailer_comm` decimal(20,2) DEFAULT NULL,
  `claim_status` enum('DONE_CLAIM','CLAIM_BAL') DEFAULT NULL,
  `bo_ref_transaction_id` bigint(20) unsigned DEFAULT NULL,
  PRIMARY KEY (`transaction_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_ola_agt_deposit`;

CREATE TABLE `st_ola_agt_deposit_refund` (
  `transaction_id` bigint(20) unsigned NOT NULL,
  `bo_ref_transaction_id` bigint(20) unsigned NOT NULL,
  `deposit_amt` decimal(20,2) DEFAULT NULL,
  `net_amt` decimal(20,2) DEFAULT NULL,
  `bo_net_amt` decimal(20,2) DEFAULT '0.00',
  `retailer_comm` decimal(20,2) DEFAULT NULL,
  `agent_comm` decimal(20,2) DEFAULT NULL,
  `wallet_id` int(10) DEFAULT NULL,
  `status` enum('DONE_CLAIM','CLAIM_BAL') DEFAULT NULL,
  `retailer_org_id` int(10) unsigned NOT NULL,
  `agent_org_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`transaction_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_ola_agt_deposit_refund`;

CREATE TABLE `st_ola_agt_direct_plr_deposit` (
  `transaction_id` bigint(20) unsigned NOT NULL,
  `agent_user_id` int(10) unsigned DEFAULT NULL,
  `agent_org_id` int(10) unsigned DEFAULT NULL,
  `wallet_id` int(10) unsigned DEFAULT NULL,
  `plr_id` int(11) DEFAULT NULL,
  `deposit_amt` decimal(20,2) DEFAULT NULL,
  `net_amt` decimal(20,2) DEFAULT NULL,
  `deposit_claim_status` enum('CLAIM_BAL','DONE_CLAIM') NOT NULL,
  `agt_claim_comm` decimal(20,2) NOT NULL,
  `bo_ref_transaction_id` bigint(20) unsigned DEFAULT NULL,
  `ims_ref_transaction_id` bigint(20) unsigned DEFAULT NULL,
  `status` enum('PENDING','DONE','FAILED') DEFAULT NULL,
  `settlement_time` datetime DEFAULT NULL,
  `settlement_status` enum('PENDING','AUTO_SETTLED','SETTLED_BY_BO') DEFAULT NULL,
  PRIMARY KEY (`transaction_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_ola_agt_direct_plr_deposit`;

CREATE TABLE `st_ola_agt_direct_plr_deposit_refund` (
  `transaction_id` bigint(11) unsigned NOT NULL,
  `agent_user_id` int(10) unsigned DEFAULT NULL,
  `agent_org_id` int(10) unsigned DEFAULT NULL,
  `wallet_id` int(10) unsigned DEFAULT NULL,
  `plr_id` int(11) DEFAULT NULL,
  `deposit_amt` decimal(20,2) DEFAULT NULL,
  `net_amt` decimal(20,2) DEFAULT NULL,
  `deposit_claim_status` enum('CLAIM_BAL','DONE_CLAIM') NOT NULL,
  `cancel_reason` enum('CANCEL_SERVER','CANCEL_MANUAL') DEFAULT NULL,
  `agt_claim_comm` decimal(20,2) NOT NULL,
  `ref_transaction_id` bigint(20) unsigned DEFAULT NULL,
  `bo_ref_transaction_id` bigint(20) unsigned DEFAULT NULL,
  PRIMARY KEY (`transaction_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_ola_agt_direct_plr_deposit_refund`;

CREATE TABLE `st_ola_agt_direct_plr_withdrawl` (
  `transaction_id` bigint(20) unsigned NOT NULL,
  `agent_user_id` int(10) unsigned DEFAULT NULL,
  `agent_org_id` int(10) unsigned DEFAULT NULL,
  `wallet_id` int(10) unsigned DEFAULT NULL,
  `plr_id` int(11) DEFAULT NULL,
  `withdrawl_amt` decimal(20,2) DEFAULT NULL,
  `net_amt` decimal(20,2) DEFAULT NULL,
  `withdrawl_claim_status` enum('CLAIM_BAL','DONE_CLAIM') NOT NULL,
  `agt_claim_comm` decimal(20,2) NOT NULL,
  `bo_ref_transaction_id` bigint(20) unsigned DEFAULT NULL,
  `ims_ref_transaction_id` bigint(20) unsigned DEFAULT NULL,
  PRIMARY KEY (`transaction_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_ola_agt_direct_plr_withdrawl`;

CREATE TABLE `st_ola_agt_direct_plr_withdrawl_refund` (
  `task_id` int(11) NOT NULL AUTO_INCREMENT,
  `wallet_id` int(10) NOT NULL,
  `plr_id` int(11) DEFAULT NULL,
  `plr_alias` varchar(50) DEFAULT NULL,
  `agt_user_id` int(10) NOT NULL,
  `agt_org_id` int(10) NOT NULL,
  `transaction_id` bigint(20) unsigned DEFAULT NULL,
  `withdrawl_amt` decimal(20,2) NOT NULL,
  `agent_net_amt` decimal(20,2) DEFAULT NULL,
  `agent_comm` decimal(3,2) NOT NULL,
  `claim_status` enum('DONE_CLAIM','CLAIM_BAL') DEFAULT NULL,
  `ref_transaction_id` bigint(20) unsigned NOT NULL,
  `bo_ref_transaction_id` bigint(20) unsigned NOT NULL,
  PRIMARY KEY (`task_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_ola_agt_direct_plr_withdrawl_refund`;

CREATE TABLE `st_ola_agt_direct_plr_withdrawl_temp` (
  `transaction_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `wallet_id` int(10) NOT NULL,
  `agt_org_id` int(10) NOT NULL,
  `agt_user_id` int(10) NOT NULL,
  `plr_alias` varchar(100) DEFAULT NULL,
  `ims_ref_transaction_id` bigint(20) NOT NULL,
  `withdrawl_amt` decimal(20,2) NOT NULL,
  `agent_net_amt` decimal(20,2) DEFAULT NULL,
  `agent_comm` decimal(3,2) NOT NULL,
  `channel` enum('WEB','TERMINAL') DEFAULT NULL,
  `status` enum('DONE','PENDING','DENIED') DEFAULT NULL,
  `agt_ref_transaction_id` bigint(20) unsigned NOT NULL,
  PRIMARY KEY (`transaction_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
--rollback drop table `st_ola_agt_direct_plr_withdrawl_temp`;

CREATE TABLE `st_ola_agt_ret_commission` (
  `agt_org_id` int(10) DEFAULT NULL,
  `ret_org_id` int(10) DEFAULT NULL,
  `wallet_id` int(10) NOT NULL,
  `plr_net_gaming` decimal(20,2) DEFAULT NULL,
  `commission_calculated` decimal(20,2) DEFAULT NULL,
  `agent_commission` decimal(20,2) DEFAULT NULL,
  `agt_comm_rate` decimal(20,2) DEFAULT NULL,
  `transaction_id` bigint(20) DEFAULT NULL,
  `start_date` date DEFAULT NULL,
  `end_date` date DEFAULT NULL,
  `claim_status` enum('CLAIM_BAL','DONE_CLAIM') DEFAULT NULL,
  `update_mode` enum('AUTO','MANUAL') DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_ola_agt_ret_commission`;

CREATE TABLE `st_ola_agt_withdrawl` (
  `transaction_id` bigint(20) unsigned NOT NULL,
  `bo_ref_transaction_id` bigint(20) unsigned DEFAULT NULL,
  `agent_org_id` int(10) unsigned NOT NULL,
  `withdrawl_amt` decimal(20,2) DEFAULT NULL,
  `net_amt` decimal(20,2) DEFAULT NULL,
  `bo_net_amt` decimal(20,2) DEFAULT '0.00',
  `retailer_comm` decimal(20,2) DEFAULT NULL,
  `agent_comm` decimal(20,2) DEFAULT NULL,
  `claim_status` enum('DONE_CLAIM','CLAIM_BAL') DEFAULT NULL,
  `wallet_id` int(10) unsigned NOT NULL,
  `retailer_org_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`transaction_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_ola_agt_withdrawl`;

CREATE TABLE `st_ola_agt_withdrawl_refund` (
  `transaction_id` bigint(20) unsigned NOT NULL,
  `bo_ref_transaction_id` bigint(20) unsigned NOT NULL,
  `withdrawl_amt` decimal(20,2) DEFAULT NULL,
  `net_amt` decimal(20,2) DEFAULT NULL,
  `bo_net_amt` decimal(20,2) DEFAULT '0.00',
  `retailer_comm` decimal(3,2) DEFAULT NULL,
  `agent_comm` decimal(3,2) DEFAULT NULL,
  `wallet_id` int(10) DEFAULT NULL,
  `status` enum('DONE_CLAIM','CLAIM_BAL') DEFAULT NULL,
  `retailer_org_id` int(10) unsigned NOT NULL,
  `agent_org_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`transaction_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_ola_agt_withdrawl_refund`;

CREATE TABLE `st_ola_bo_agent_comm_variance` (
  `agent_org_id` int(10) unsigned NOT NULL,
  `wallet_id` int(10) unsigned NOT NULL,
  `deposit_comm_variance` decimal(5,2) DEFAULT NULL,
  `default_deposit_comm_rate` decimal(5,2) DEFAULT NULL,
  `withdrawl_comm_variance` decimal(5,2) DEFAULT NULL,
  `default_withdrawl_comm_rate` decimal(5,2) DEFAULT NULL,
  `net_gaming_comm_variance` decimal(5,2) DEFAULT NULL,
  `default_net_gaming_comm_rate` decimal(5,2) DEFAULT NULL,
  PRIMARY KEY (`agent_org_id`,`wallet_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_ola_bo_agent_comm_variance`;

CREATE TABLE `st_ola_bo_agent_deposit_comm_variance_history` (
  `agent_org_id` int(11) unsigned NOT NULL,
  `wallet_id` int(11) unsigned NOT NULL,
  `deposit_comm_variance` decimal(5,2) NOT NULL,
  `date_changed` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_ola_bo_agent_deposit_comm_variance_history`;

CREATE TABLE `st_ola_bo_agent_plr_comm_variance_history` (
  `agent_org_id` int(11) unsigned NOT NULL,
  `wallet_id` int(11) unsigned NOT NULL,
  `net_gaming_comm_variance` decimal(5,2) NOT NULL,
  `date_changed` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_ola_bo_agent_plr_comm_variance_history`;

CREATE TABLE `st_ola_bo_agent_withdrawl_comm_variance_history` (
  `agent_org_id` int(11) unsigned NOT NULL,
  `wallet_id` int(11) unsigned NOT NULL,
  `withdrawl_comm_variance` decimal(5,2) NOT NULL,
  `date_changed` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_ola_bo_agent_withdrawl_comm_variance_history`;

CREATE TABLE `st_ola_bo_agt_commission` (
  `agt_org_id` int(10) DEFAULT NULL,
  `net_gaming` decimal(20,2) DEFAULT NULL,
  `commission_calculated` decimal(20,2) DEFAULT NULL,
  `comm_rate` decimal(3,2) DEFAULT NULL,
  `transaction_id` bigint(20) DEFAULT NULL,
  `start_date` date DEFAULT NULL,
  `end_date` date DEFAULT NULL,
  `wallet_id` int(10) NOT NULL,
  `update_mode` enum('AUTO','MANUAL') DEFAULT NULL,
  `status` enum('DONE_CLAIM','CLAIM_BAL') DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_ola_bo_agt_commission`;

CREATE TABLE `st_ola_bo_comm` (
  `transaction_id` bigint(20) unsigned NOT NULL,
  `wallet_id` int(10) unsigned NOT NULL,
  `bo_user_id` int(10) unsigned NOT NULL,
  `bo_org_id` int(10) unsigned NOT NULL,
  `agent_org_id` int(11) DEFAULT NULL,
  `agt_claim_comm` decimal(20,2) NOT NULL DEFAULT '0.00',
  `tds_comm_rate` decimal(10,2) DEFAULT NULL,
  `agt_net_claim_comm` double(20,2) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_ola_bo_comm`;

CREATE TABLE `st_ola_bo_deposit` (
  `transaction_id` bigint(20) unsigned NOT NULL,
  `wallet_id` int(10) unsigned DEFAULT NULL,
  `agent_org_id` int(10) unsigned NOT NULL,
  `deposit_amt` decimal(20,2) unsigned DEFAULT NULL,
  `agent_comm` decimal(20,2) DEFAULT NULL,
  `net_amt` decimal(20,2) unsigned DEFAULT NULL,
  `good_cause_comm` decimal(20,2) unsigned DEFAULT NULL,
  `vat_comm` decimal(20,2) DEFAULT NULL,
  PRIMARY KEY (`transaction_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_ola_bo_deposit`;

CREATE TABLE `st_ola_bo_deposit_refund` (
  `transaction_id` bigint(20) unsigned NOT NULL,
  `agent_org_id` int(10) DEFAULT NULL,
  `deposit_amt` decimal(20,2) DEFAULT NULL,
  `net_amt` decimal(20,2) DEFAULT NULL,
  `agent_comm` decimal(20,2) DEFAULT NULL,
  `wallet_id` int(10) DEFAULT NULL,
  `vat_comm` decimal(20,2) DEFAULT NULL,
  `good_cause_comm` decimal(20,2) DEFAULT NULL,
  PRIMARY KEY (`transaction_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_ola_bo_deposit_refund`;

CREATE TABLE `st_ola_bo_direct_plr_deposit` (
  `transaction_id` bigint(20) unsigned NOT NULL,
  `bo_user_id` int(10) unsigned DEFAULT NULL,
  `bo_org_id` int(10) unsigned DEFAULT NULL,
  `wallet_id` int(10) unsigned DEFAULT NULL,
  `plr_id` int(11) DEFAULT NULL,
  `deposit_amt` decimal(20,2) DEFAULT NULL,
  `ims_ref_transaction_id` bigint(20) unsigned DEFAULT NULL,
  `status` enum('PENDING','DONE','FAILED') DEFAULT NULL,
  `settlement_time` datetime DEFAULT NULL,
  `settlement_status` enum('PENDING','AUTO_SETTLED','SETTLED_BY_BO') DEFAULT NULL,
  PRIMARY KEY (`transaction_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_ola_bo_direct_plr_deposit`;

CREATE TABLE `st_ola_bo_direct_plr_deposit_refund` (
  `transaction_id` bigint(11) NOT NULL AUTO_INCREMENT,
  `bo_user_id` int(10) unsigned DEFAULT NULL,
  `bo_org_id` int(10) unsigned DEFAULT NULL,
  `wallet_id` int(10) unsigned DEFAULT NULL,
  `plr_id` int(11) DEFAULT NULL,
  `deposit_amt` decimal(20,2) DEFAULT NULL,
  `cancel_reason` enum('CANCEL_SERVER','CANCEL_MANUAL') DEFAULT NULL,
  `ref_transaction_id` bigint(20) unsigned DEFAULT NULL,
  PRIMARY KEY (`transaction_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;
--rollback drop table `st_ola_bo_direct_plr_deposit_refund`;

CREATE TABLE `st_ola_bo_direct_plr_withdrawl` (
  `transaction_id` bigint(20) unsigned NOT NULL,
  `wallet_id` int(10) NOT NULL,
  `plr_id` int(11) DEFAULT NULL,
  `bo_user_id` int(10) NOT NULL,
  `bo_org_id` int(10) NOT NULL,
  `ims_ref_transaction_id` bigint(20) NOT NULL,
  `withdrawl_amt` decimal(20,2) NOT NULL,
  PRIMARY KEY (`transaction_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_ola_bo_direct_plr_withdrawl`;

CREATE TABLE `st_ola_bo_direct_plr_withdrawl_refund` (
  `task_id` int(11) NOT NULL AUTO_INCREMENT,
  `wallet_id` int(10) NOT NULL,
  `plr_id` int(11) DEFAULT NULL,
  `plr_alias` varchar(50) DEFAULT NULL,
  `bo_user_id` int(10) NOT NULL,
  `bo_org_id` int(10) NOT NULL,
  `transaction_id` bigint(20) unsigned DEFAULT NULL,
  `ims_ref_transaction_id` bigint(20) NOT NULL,
  `withdrawl_amt` decimal(20,2) NOT NULL,
  `cancel_reason` enum('CANCEL_SERVER','CANCEL_MANUAL') DEFAULT NULL,
  `ref_transaction_id` bigint(20) unsigned DEFAULT NULL,
  PRIMARY KEY (`task_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_ola_bo_direct_plr_withdrawl_refund`;

CREATE TABLE `st_ola_bo_direct_plr_withdrawl_temp` (
  `transaction_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `wallet_id` int(10) NOT NULL,
  `user_id` int(10) NOT NULL,
  `plr_alias` varchar(100) DEFAULT NULL,
  `ims_ref_transaction_id` bigint(20) NOT NULL,
  `withdrawl_amt` decimal(20,2) NOT NULL,
  `channel` enum('WEB','TERMINAL') DEFAULT NULL,
  `status` enum('DONE','PENDING','DENIED') DEFAULT NULL,
  `bo_ref_transaction_id` bigint(20) unsigned NOT NULL,
  PRIMARY KEY (`transaction_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
--rollback drop table `st_ola_bo_direct_plr_withdrawl_temp`;

CREATE TABLE `st_ola_bo_distributor_transaction` (
  `transaction_id` bigint(10) unsigned NOT NULL,
  `wallet_id` int(10) NOT NULL,
  `totalamount` decimal(20,2) NOT NULL DEFAULT '0.00',
  `sale_comm_rate` decimal(4,2) NOT NULL DEFAULT '0.00',
  `amount` decimal(20,2) NOT NULL,
  `distributor` varchar(30) NOT NULL,
  `transaction_type` enum('OTHER','OLA_CASHCARD_SALE') NOT NULL,
  `start_date` datetime DEFAULT NULL,
  `end_date` datetime DEFAULT NULL,
  `service_code` varchar(5) DEFAULT NULL,
  PRIMARY KEY (`transaction_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_ola_bo_distributor_transaction`;

CREATE TABLE `st_ola_bo_withdrawl` (
  `transaction_id` bigint(20) unsigned NOT NULL,
  `withdrawl_amt` decimal(20,2) unsigned DEFAULT NULL,
  `net_amt` decimal(20,2) unsigned DEFAULT NULL,
  `good_cause_comm` decimal(20,2) unsigned DEFAULT NULL,
  `wallet_id` int(10) unsigned DEFAULT NULL,
  `agent_org_id` int(10) unsigned NOT NULL,
  `agent_comm` decimal(20,2) DEFAULT NULL,
  `vat_comm` decimal(20,2) DEFAULT NULL,
  PRIMARY KEY (`transaction_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_ola_bo_withdrawl`;

CREATE TABLE `st_ola_bo_withdrawl_refund` (
  `transaction_id` bigint(20) unsigned NOT NULL,
  `agent_org_id` int(10) DEFAULT NULL,
  `withdrawl_amt` decimal(20,2) DEFAULT NULL,
  `net_amt` decimal(20,2) DEFAULT NULL,
  `agent_comm` decimal(3,2) DEFAULT NULL,
  `wallet_id` int(10) DEFAULT NULL,
  `vat_comm` decimal(3,2) DEFAULT NULL,
  `good_cause_comm` decimal(3,2) DEFAULT NULL,
  PRIMARY KEY (`transaction_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_ola_bo_withdrawl_refund`;

CREATE TABLE `st_ola_commission_master` (
  `wallet_id` int(11) NOT NULL,
  `debit_card_comm_rate` double(10,2) DEFAULT NULL,
  `credit_card_comm_rate` double(10,2) DEFAULT NULL,
  `net_banking_comm_rate` double(10,2) DEFAULT NULL,
  `cash_card_comm_rate` double(10,2) DEFAULT NULL,
  `ola_bind_comm_rate` double(10,2) DEFAULT NULL,
  `tech_process_comm_rate` double(10,2) DEFAULT NULL,
  `ola_non_bind_comm_rate` double(10,2) DEFAULT NULL,
  `tier_id` tinyint(4) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_ola_commission_master`;

CREATE TABLE `st_ola_daily_retailer_commission_1` (
  `task_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `ret_org_id` int(10) DEFAULT NULL,
  `plr_id` varchar(20) DEFAULT NULL,
  `plr_net_gaming` decimal(20,2) DEFAULT NULL,
  `date` date DEFAULT NULL,
  `status` enum('PENDING','DONE') NOT NULL DEFAULT 'PENDING',
  `commission_TP` decimal(20,2) DEFAULT NULL,
  PRIMARY KEY (`task_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC;
--rollback drop table `st_ola_daily_retailer_commission_1`;

CREATE TABLE `st_ola_daily_retailer_commission_2` (
  `task_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `ret_org_id` int(10) DEFAULT NULL,
  `plr_id` varchar(20) DEFAULT NULL,
  `plr_net_gaming` decimal(20,2) DEFAULT NULL,
  `date` date DEFAULT NULL,
  `status` enum('PENDING','DONE') NOT NULL DEFAULT 'PENDING',
  `commission_TP` decimal(20,2) DEFAULT NULL,
  PRIMARY KEY (`task_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC;
--rollback drop table `st_ola_daily_retailer_commission_2`;

CREATE TABLE `st_ola_menu_master` (
  `menu_id` smallint(10) unsigned NOT NULL AUTO_INCREMENT,
  `action_id` smallint(10) unsigned NOT NULL,
  `menu_name` varchar(50) NOT NULL,
  `menu_disp_name` varchar(50) DEFAULT NULL,
  `parent_menu_id` smallint(10) unsigned NOT NULL,
  `item_order` tinyint(4) unsigned DEFAULT NULL,
  `menu_disp_name_fr` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `menu_disp_name_en` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`menu_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
--rollback drop table `st_ola_menu_master`;


CREATE TABLE `st_ola_org_affiliate_mapping` (
  `organization_id` int(10) unsigned NOT NULL,
  `ref_user_id` varchar(20) NOT NULL,
  PRIMARY KEY (`organization_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_ola_org_affiliate_mapping`;

CREATE TABLE `st_ola_player_master` (
  `lms_plr_id` bigint(10) unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `wallet_id` int(10) unsigned NOT NULL DEFAULT '0',
  `account_id` varchar(30) DEFAULT NULL,
  `fname` varchar(50) DEFAULT NULL,
  `lname` varchar(50) DEFAULT NULL,
  `gender` enum('M','F') DEFAULT NULL,
  `date_of_birth` date NOT NULL DEFAULT '0000-00-00',
  `password` varchar(50) DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  `phone` varchar(20) NOT NULL,
  `address` varchar(50) DEFAULT NULL,
  `city` varchar(30) DEFAULT NULL,
  `state` varchar(30) DEFAULT NULL,
  `country` varchar(30) DEFAULT NULL,
  `status` enum('ACTIVE','INACTIVE') DEFAULT NULL,
  `registration_date` datetime DEFAULT NULL,
  `registration_type` enum('DIRECT','OLA') DEFAULT NULL,
  PRIMARY KEY (`username`,`wallet_id`),
  UNIQUE KEY `lms_plr_id` (`lms_plr_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
--rollback drop table `st_ola_player_master`;

CREATE TABLE `st_ola_priviledge_rep` (
  `action_id` smallint(10) unsigned NOT NULL AUTO_INCREMENT,
  `priv_id` int(10) unsigned NOT NULL,
  `priv_title` varchar(50) CHARACTER SET utf8 NOT NULL DEFAULT '',
  `priv_disp_name` varchar(50) DEFAULT NULL,
  `action_mapping` varchar(50) CHARACTER SET utf8 DEFAULT NULL,
  `parent_priv_id` int(10) unsigned NOT NULL,
  `status` enum('ACTIVE','INACTIVE') DEFAULT NULL,
  `priv_owner` enum('BO','AGENT','RETAILER') DEFAULT NULL,
  `related_to` enum('ola_Mgmt','REPORTS','ACT_MGT') DEFAULT NULL,
  `group_name` varchar(50) DEFAULT NULL,
  `is_start` enum('Y','N') DEFAULT NULL,
  `channel` enum('WEB','TERMINAL','MOBILE') NOT NULL,
  `priv_code` int(10) DEFAULT NULL,
  `hidden` enum('Y','N') DEFAULT NULL,
  `group_name_fr` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `group_name_en` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`action_id`),
  KEY `priv_title` (`priv_title`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
--rollback drop table `st_ola_priviledge_rep`;


CREATE TABLE `st_ola_ret_comm` (
  `transaction_id` bigint(20) unsigned NOT NULL,
  `wallet_id` int(10) unsigned NOT NULL,
  `retailer_user_id` int(10) unsigned NOT NULL,
  `retailer_org_id` int(10) unsigned NOT NULL,
  `retailer_claim_comm` decimal(20,2) NOT NULL DEFAULT '0.00',
  `tds_comm_rate` decimal(10,2) DEFAULT NULL,
  `retailer_net_claim_comm` double(20,2) DEFAULT NULL,
  `agt_claim_comm` decimal(20,2) NOT NULL,
  `agt_net_claim_comm` decimal(10,2) DEFAULT NULL,
  `status` enum('CLAIM_BAL','UNCLAIM_BAL','DONE_CLM') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_ola_ret_comm`;

CREATE TABLE `st_ola_ret_deposit` (
  `transaction_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `wallet_id` int(10) NOT NULL,
  `party_id` int(10) DEFAULT NULL,
  `retailer_org_id` int(10) NOT NULL,
  `deposit_amt` decimal(20,2) NOT NULL,
  `retailer_comm` decimal(20,2) NOT NULL,
  `net_amt` decimal(20,2) NOT NULL,
  `agent_comm` decimal(20,2) NOT NULL,
  `agent_net_amt` decimal(20,2) DEFAULT NULL,
  `agent_ref_transaction_id` bigint(20) unsigned NOT NULL,
  `claim_status` enum('DONE_CLAIM','CLAIM_BAL') DEFAULT NULL,
  `deposit_channel` enum('WEB','TERMINAL') DEFAULT NULL,
  `ims_ref_transaction_id` bigint(20) NOT NULL,
  `status` enum('PENDING','DONE','FAILED') DEFAULT NULL,
  `settlement_time` datetime DEFAULT NULL,
  `settlement_status` enum('PENDING','AUTO_SETTLED','SETTLED_BY_BO') DEFAULT NULL,
  PRIMARY KEY (`transaction_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC;
--rollback drop table `st_ola_ret_deposit`;

CREATE TABLE `st_ola_ret_deposit_refund` (
  `transaction_id` bigint(20) unsigned NOT NULL,
  `wallet_id` int(20) DEFAULT NULL,
  `retailer_org_id` int(10) unsigned NOT NULL,
  `ims_ref_transaction_id` bigint(20) unsigned NOT NULL,
  `ola_ref_transaction_id` bigint(20) unsigned NOT NULL,
  `claim_status` enum('DONE_CLAIM','CLAIM_BAL') DEFAULT NULL,
  `cancel_reason` enum('CANCEL_SERVER','CANCEL_MANUAL') DEFAULT NULL,
  `deposit_amt` decimal(20,2) DEFAULT NULL,
  `net_amt` decimal(20,2) DEFAULT NULL,
  `agent_net_amt` decimal(20,2) DEFAULT NULL,
  `retailer_comm` decimal(20,2) unsigned NOT NULL,
  `agent_comm` decimal(20,2) unsigned NOT NULL,
  `agent_ref_transaction_id` bigint(20) unsigned NOT NULL,
  `party_id` int(10) DEFAULT NULL,
  PRIMARY KEY (`transaction_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC;
--rollback drop table `st_ola_ret_deposit_refund`;

CREATE TABLE `st_ola_ret_withdrawl` (
  `transaction_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `wallet_id` int(10) NOT NULL,
  `retailer_org_id` int(10) NOT NULL,
  `ims_ref_transaction_id` bigint(20) NOT NULL,
  `withdrawl_amt` decimal(20,2) NOT NULL,
  `net_amt` decimal(20,2) NOT NULL,
  `agent_net_amt` decimal(20,2) DEFAULT NULL,
  `retailer_comm` decimal(20,2) NOT NULL,
  `agent_comm` decimal(20,2) NOT NULL,
  `deposit_channel` enum('WEB','TERMINAL') DEFAULT NULL,
  `claim_status` enum('DONE_CLAIM','CLAIM_BAL') DEFAULT NULL,
  `agent_ref_transaction_id` bigint(20) unsigned NOT NULL,
  `party_id` int(10) DEFAULT NULL,
  PRIMARY KEY (`transaction_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
--rollback drop table `st_ola_ret_withdrawl`;

CREATE TABLE `st_ola_ret_withdrawl_refund` (
  `transaction_id` bigint(20) unsigned NOT NULL,
  `wallet_id` int(20) DEFAULT NULL,
  `retailer_org_id` int(10) unsigned NOT NULL,
  `ims_ref_transaction_id` bigint(20) unsigned NOT NULL,
  `ola_ref_transaction_id` bigint(20) unsigned NOT NULL,
  `claim_status` enum('DONE_CLAIM','CLAIM_BAL') DEFAULT NULL,
  `cancel_reason` enum('CANCEL_SERVER') DEFAULT NULL,
  `withdrawl_amt` decimal(20,2) DEFAULT NULL,
  `net_amt` decimal(20,2) DEFAULT NULL,
  `agent_net_amt` decimal(20,2) DEFAULT NULL,
  `retailer_comm` decimal(3,2) unsigned NOT NULL,
  `agent_comm` decimal(3,2) unsigned NOT NULL,
  `agent_ref_transaction_id` bigint(20) unsigned NOT NULL,
  `party_id` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`transaction_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_ola_ret_withdrawl_refund`;

CREATE TABLE `st_ola_ret_withdrawl_temp` (
  `transaction_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `wallet_id` int(10) NOT NULL,
  `retailer_org_id` int(10) NOT NULL,
  `ims_ref_transaction_id` bigint(20) NOT NULL,
  `withdrawl_amt` decimal(20,2) NOT NULL,
  `net_amt` decimal(20,2) NOT NULL,
  `agent_net_amt` decimal(20,2) DEFAULT NULL,
  `retailer_comm` decimal(3,2) NOT NULL,
  `agent_comm` decimal(3,2) NOT NULL,
  `deposit_channel` enum('WEB','TERMINAL') DEFAULT NULL,
  `status` enum('DONE','PENDING','DENIED') DEFAULT NULL,
  `retailer_ref_transaction_id` bigint(20) unsigned NOT NULL,
  `party_id` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`transaction_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
--rollback drop table `st_ola_ret_withdrawl_temp`;

CREATE TABLE `st_ola_transaction_authentication_history` (
  `transaction_id` int(20) NOT NULL,
  `authentication_code` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`transaction_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_ola_transaction_authentication_history`;

/*Table structure for table `st_ola_wallet_authentication_type` */

CREATE TABLE `st_ola_wallet_authentication_type` (
  `wallet_id` int(10) DEFAULT NULL,
  `transaction_type` enum('DEPOSIT','WITHDRAWL') DEFAULT NULL,
  `authentication_type` enum('NO','SMS_CODE','FINGER_PRINT','CARD_SWIPE') DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_ola_wallet_authentication_type`;
/*Table structure for table `st_ola_wallet_master` */

CREATE TABLE `st_ola_wallet_master` (
  `wallet_id` int(10) NOT NULL AUTO_INCREMENT,
  `wallet_name` enum('PLAYTECH_CASINO','Rummy','KhelPlayRummy','GroupRummy','PLAYER_LOTTERY','TabletGaming','ALA_WALLET') NOT NULL,
  `wallet_category` enum('BANK','GAMING','CASHCARD') DEFAULT NULL,
  `wallet_display_name` varchar(100) NOT NULL,
  `ret_deposit_comm` decimal(10,2) DEFAULT NULL,
  `agt_deposit_comm` decimal(10,2) DEFAULT NULL,
  `ret_withdrawl_comm` decimal(10,2) DEFAULT NULL,
  `agt_withdrawl_comm` decimal(10,2) DEFAULT NULL,
  `tds_comm_rate` double(10,2) DEFAULT NULL,
  `ret_net_gaming_comm` decimal(10,2) DEFAULT NULL,
  `agt_net_gaming_comm` decimal(10,2) DEFAULT NULL,
  `wallet_status` enum('ACTIVE','INACTIVE') DEFAULT NULL,
  `verification_type` enum('MOBILE_NUM','USER_NAME') DEFAULT NULL,
  `min_deposit` decimal(10,2) DEFAULT NULL,
  `registration_type` enum('FULL','ANONYMOUS','MOBILE','ALA') NOT NULL,
  PRIMARY KEY (`wallet_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
--rollback drop table `st_ola_wallet_master`;
/*Table structure for table `st_ola_wallet_integration_master` */

CREATE TABLE `st_ola_wallet_integration_master` (
  `wallet_id` int(10) NOT NULL,
  `int_ip` varchar(50) DEFAULT NULL,
  `int_walletCode` varchar(100) NOT NULL,
  `int_userName` varchar(50) DEFAULT NULL,
  `int_password` varchar(100) DEFAULT NULL,
  KEY `wallet_id` (`wallet_id`),
  CONSTRAINT `st_ola_wallet_integration_master_ibfk_1` FOREIGN KEY (`wallet_id`) REFERENCES `st_ola_wallet_master` (`wallet_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_ola_wallet_integration_master`;

/*Table structure for table `st_ola_withdrawl_temp` */

CREATE TABLE `st_ola_withdrawl_temp` (
  `task_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `wallet_id` int(10) NOT NULL,
  `party_id` int(11) DEFAULT NULL,
  `org_id` int(10) NOT NULL,
  `ims_ref_transaction_id` bigint(20) NOT NULL,
  `withdrawl_amt` decimal(20,2) NOT NULL,
  `withdrawl_channel` enum('WEB','TERMINAL') DEFAULT NULL,
  `status` enum('INITIATED','DENIED','FAILED','PROCESSED','DONE') DEFAULT NULL,
  `ref_transaction_id` bigint(20) unsigned NOT NULL,
  `settlement_time` datetime DEFAULT NULL,
  `settlement_status` enum('PENDING','AUTO_SETTLED','SETTLED_BY_BO') DEFAULT NULL,
  PRIMARY KEY (`task_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
--rollback drop table `st_ola_withdrawl_temp`;
/*Table structure for table `st_ping_test` */

CREATE TABLE `st_ping_test` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `test` varchar(20) NOT NULL,
  `pinged on` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
--rollback drop table `st_ping_test`;
/*Table structure for table `st_rep_agent_payments` */

CREATE TABLE `st_rep_agent_payments` (
  `retailer_org_id` int(10) unsigned NOT NULL,
  `parent_id` int(10) unsigned NOT NULL,
  `finaldate` date NOT NULL DEFAULT '0000-00-00',
  `cash_amt` decimal(10,2) NOT NULL DEFAULT '0.00',
  `credit_note` decimal(10,2) NOT NULL DEFAULT '0.00',
  `debit_note` decimal(10,2) NOT NULL DEFAULT '0.00',
  `cheque_amt` decimal(10,2) NOT NULL DEFAULT '0.00',
  `cheque_bounce_amt` decimal(10,2) NOT NULL DEFAULT '0.00',
  `cl_amt` decimal(10,2) NOT NULL DEFAULT '0.00',
  `xcl_amt` decimal(10,2) NOT NULL DEFAULT '0.00',
  `bank_deposit` decimal(10,2) DEFAULT '0.00',
  PRIMARY KEY (`finaldate`,`retailer_org_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_rep_agent_payments`;
/*Table structure for table `st_rep_bo_payments` */

CREATE TABLE `st_rep_bo_payments` (
  `agent_org_id` int(10) unsigned NOT NULL,
  `parent_id` int(10) unsigned NOT NULL,
  `finaldate` date NOT NULL DEFAULT '0000-00-00',
  `cash_amt` decimal(10,2) NOT NULL DEFAULT '0.00',
  `credit_note` decimal(10,2) NOT NULL DEFAULT '0.00',
  `debit_note` decimal(10,2) NOT NULL DEFAULT '0.00',
  `cheque_amt` decimal(10,2) NOT NULL DEFAULT '0.00',
  `cheque_bounce_amt` decimal(10,2) NOT NULL DEFAULT '0.00',
  `bank_deposit` decimal(10,2) NOT NULL DEFAULT '0.00',
  `cl_amt` decimal(10,2) NOT NULL DEFAULT '0.00',
  `xcl_amt` decimal(10,2) NOT NULL DEFAULT '0.00',
  PRIMARY KEY (`finaldate`,`agent_org_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_rep_bo_payments`;
/*Table structure for table `st_rep_cs_agent` */

CREATE TABLE `st_rep_cs_agent` (
  `organization_id` int(10) unsigned NOT NULL,
  `parent_id` int(10) unsigned NOT NULL,
  `finaldate` date NOT NULL DEFAULT '0000-00-00',
  `category_id` int(10) NOT NULL,
  `sale_mrp` decimal(10,2) NOT NULL DEFAULT '0.00',
  `sale_comm` decimal(10,2) NOT NULL DEFAULT '0.00',
  `sale_jv_comm` decimal(10,2) NOT NULL DEFAULT '0.00',
  `sale_jv_net_amt` decimal(10,2) NOT NULL DEFAULT '0.00',
  `sale_net` decimal(10,2) NOT NULL DEFAULT '0.00',
  `sale_good_cause` decimal(10,4) NOT NULL DEFAULT '0.0000',
  `sale_vat` decimal(10,4) NOT NULL DEFAULT '0.0000',
  `ref_sale_mrp` decimal(10,2) NOT NULL DEFAULT '0.00',
  `ref_comm` decimal(10,2) NOT NULL DEFAULT '0.00',
  `ref_jv_comm` decimal(10,2) NOT NULL DEFAULT '0.00',
  `ref_jv_net_amt` decimal(10,2) NOT NULL DEFAULT '0.00',
  `ref_net_amt` decimal(10,2) NOT NULL DEFAULT '0.00',
  `ref_good_cause` decimal(10,4) NOT NULL DEFAULT '0.0000',
  `ref_vat` decimal(10,4) NOT NULL DEFAULT '0.0000',
  PRIMARY KEY (`finaldate`,`organization_id`,`category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_rep_cs_agent`;
/*Table structure for table `st_rep_cs_bo` */

CREATE TABLE `st_rep_cs_bo` (
  `organization_id` int(10) unsigned NOT NULL,
  `parent_id` int(10) unsigned NOT NULL,
  `finaldate` date NOT NULL,
  `category_id` int(10) NOT NULL,
  `sale_mrp` decimal(10,2) NOT NULL DEFAULT '0.00',
  `sale_comm` decimal(10,2) NOT NULL DEFAULT '0.00',
  `sale_jv_comm` decimal(10,2) NOT NULL DEFAULT '0.00',
  `sale_jv_net_amt` decimal(10,2) NOT NULL DEFAULT '0.00',
  `sale_net` decimal(10,2) NOT NULL DEFAULT '0.00',
  `sale_good_cause` decimal(10,4) NOT NULL DEFAULT '0.0000',
  `sale_vat` decimal(10,4) NOT NULL DEFAULT '0.0000',
  `ref_sale_mrp` decimal(10,2) NOT NULL DEFAULT '0.00',
  `ref_comm` decimal(10,2) NOT NULL DEFAULT '0.00',
  `ref_jv_comm` decimal(10,2) NOT NULL DEFAULT '0.00',
  `ref_jv_net_amt` decimal(10,2) NOT NULL DEFAULT '0.00',
  `ref_net_amt` decimal(10,2) NOT NULL DEFAULT '0.00',
  `ref_good_cause` decimal(10,4) NOT NULL DEFAULT '0.0000',
  `ref_vat` decimal(10,4) NOT NULL DEFAULT '0.0000',
  PRIMARY KEY (`finaldate`,`organization_id`,`category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_rep_cs_bo`;
/*Table structure for table `st_rep_cs_retailer` */

CREATE TABLE `st_rep_cs_retailer` (
  `organization_id` int(10) unsigned NOT NULL,
  `parent_id` int(10) unsigned NOT NULL,
  `finaldate` date NOT NULL,
  `category_id` int(10) NOT NULL,
  `sale_mrp` decimal(10,2) NOT NULL DEFAULT '0.00',
  `sale_comm` decimal(10,2) NOT NULL DEFAULT '0.00',
  `sale_jv_comm` decimal(10,2) NOT NULL DEFAULT '0.00',
  `sale_jv_net_amt` decimal(10,2) NOT NULL DEFAULT '0.00',
  `sale_net` decimal(10,2) NOT NULL DEFAULT '0.00',
  `sale_good_cause` decimal(10,4) NOT NULL DEFAULT '0.0000',
  `sale_vat` decimal(10,4) NOT NULL DEFAULT '0.0000',
  `ref_sale_mrp` decimal(10,2) NOT NULL DEFAULT '0.00',
  `ref_comm` decimal(10,2) NOT NULL DEFAULT '0.00',
  `ref_jv_comm` decimal(10,2) NOT NULL DEFAULT '0.00',
  `ref_jv_net_amt` decimal(10,2) NOT NULL DEFAULT '0.00',
  `ref_net_amt` decimal(10,2) NOT NULL DEFAULT '0.00',
  `ref_good_cause` decimal(10,4) NOT NULL DEFAULT '0.0000',
  `ref_vat` decimal(10,4) NOT NULL DEFAULT '0.0000',
  PRIMARY KEY (`finaldate`,`organization_id`,`category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_rep_cs_retailer`;
/*Table structure for table `st_rep_dg_agent` */

CREATE TABLE `st_rep_dg_agent` (
  `organization_id` int(10) unsigned NOT NULL,
  `parent_id` int(10) unsigned NOT NULL,
  `finaldate` date NOT NULL DEFAULT '0000-00-00',
  `game_id` int(10) NOT NULL,
  `sale_mrp` decimal(10,2) NOT NULL DEFAULT '0.00',
  `sale_comm` decimal(10,2) NOT NULL DEFAULT '0.00',
  `sale_net` decimal(10,2) NOT NULL DEFAULT '0.00',
  `sale_good_cause` decimal(10,4) NOT NULL DEFAULT '0.0000',
  `sale_vat` decimal(10,4) NOT NULL DEFAULT '0.0000',
  `sale_taxable` decimal(10,2) NOT NULL DEFAULT '0.00',
  `ref_sale_mrp` decimal(10,2) NOT NULL DEFAULT '0.00',
  `ref_comm` decimal(10,2) NOT NULL DEFAULT '0.00',
  `ref_net_amt` decimal(10,2) NOT NULL DEFAULT '0.00',
  `ref_good_cause` decimal(10,4) NOT NULL DEFAULT '0.0000',
  `ref_vat` decimal(10,4) NOT NULL DEFAULT '0.0000',
  `ref_taxable` decimal(10,2) NOT NULL DEFAULT '0.00',
  `pwt_mrp` decimal(10,2) NOT NULL DEFAULT '0.00',
  `ret_pwt_comm` decimal(10,2) NOT NULL DEFAULT '0.00',
  `pwt_net_amt` decimal(10,2) NOT NULL DEFAULT '0.00',
  `direct_pwt_amt` decimal(10,2) NOT NULL DEFAULT '0.00',
  `direct_pwt_comm` decimal(10,2) NOT NULL DEFAULT '0.00',
  `direct_pwt_net_amt` decimal(10,2) NOT NULL DEFAULT '0.00',
  `direct_pwt_tax` decimal(10,2) NOT NULL DEFAULT '0.00',
  `govt_claim_comm` decimal(10,2) NOT NULL DEFAULT '0.00',
  PRIMARY KEY (`finaldate`,`organization_id`,`game_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_rep_dg_agent`;
/*Table structure for table `st_rep_dg_bo` */

CREATE TABLE `st_rep_dg_bo` (
  `organization_id` int(10) unsigned NOT NULL,
  `parent_id` int(10) unsigned NOT NULL,
  `finaldate` date NOT NULL DEFAULT '0000-00-00',
  `game_id` int(10) NOT NULL,
  `sale_mrp` decimal(10,2) NOT NULL DEFAULT '0.00',
  `sale_comm` decimal(10,2) NOT NULL DEFAULT '0.00',
  `sale_net` decimal(10,2) NOT NULL DEFAULT '0.00',
  `sale_good_cause` decimal(10,4) NOT NULL DEFAULT '0.0000',
  `sale_vat` decimal(10,4) NOT NULL DEFAULT '0.0000',
  `sale_taxable` decimal(10,2) NOT NULL DEFAULT '0.00',
  `ref_sale_mrp` decimal(10,2) NOT NULL DEFAULT '0.00',
  `ref_comm` decimal(10,2) NOT NULL DEFAULT '0.00',
  `ref_net_amt` decimal(10,2) NOT NULL DEFAULT '0.00',
  `ref_good_cause` decimal(10,4) NOT NULL DEFAULT '0.0000',
  `ref_vat` decimal(10,4) NOT NULL DEFAULT '0.0000',
  `ref_taxable` decimal(10,2) NOT NULL DEFAULT '0.00',
  `pwt_mrp` decimal(10,2) NOT NULL DEFAULT '0.00',
  `ret_pwt_comm` decimal(10,2) NOT NULL DEFAULT '0.00',
  `pwt_net_amt` decimal(10,2) NOT NULL DEFAULT '0.00',
  `direct_pwt_amt` decimal(10,2) NOT NULL DEFAULT '0.00',
  `direct_pwt_comm` decimal(10,2) NOT NULL DEFAULT '0.00',
  `direct_pwt_net_amt` decimal(10,2) NOT NULL DEFAULT '0.00',
  `direct_pwt_tax` decimal(10,2) NOT NULL DEFAULT '0.00',
  `govt_claim_comm` decimal(10,2) NOT NULL DEFAULT '0.00',
  PRIMARY KEY (`finaldate`,`organization_id`,`game_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_rep_dg_bo`;
/*Table structure for table `st_rep_dg_retailer` */

CREATE TABLE `st_rep_dg_retailer` (
  `organization_id` int(10) unsigned NOT NULL,
  `parent_id` int(10) unsigned NOT NULL,
  `finaldate` date NOT NULL,
  `game_id` int(10) NOT NULL,
  `sale_mrp` decimal(10,2) NOT NULL DEFAULT '0.00',
  `sale_comm` decimal(10,2) NOT NULL DEFAULT '0.00',
  `sale_net` decimal(10,2) NOT NULL DEFAULT '0.00',
  `sale_good_cause` decimal(10,4) NOT NULL DEFAULT '0.0000',
  `sale_vat` decimal(10,4) NOT NULL DEFAULT '0.0000',
  `sale_taxable` decimal(10,2) NOT NULL DEFAULT '0.00',
  `ref_sale_mrp` decimal(10,2) NOT NULL DEFAULT '0.00',
  `ref_comm` decimal(10,2) NOT NULL DEFAULT '0.00',
  `ref_net_amt` decimal(10,2) NOT NULL DEFAULT '0.00',
  `ref_good_cause` decimal(10,4) NOT NULL DEFAULT '0.0000',
  `ref_vat` decimal(10,4) NOT NULL DEFAULT '0.0000',
  `ref_taxable` decimal(10,2) NOT NULL DEFAULT '0.00',
  `pwt_mrp` decimal(10,2) NOT NULL DEFAULT '0.00',
  `ret_pwt_comm` decimal(10,2) NOT NULL DEFAULT '0.00',
  `pwt_net_amt` decimal(10,2) NOT NULL DEFAULT '0.00',
  `govt_claim_comm` decimal(10,2) NOT NULL DEFAULT '0.00',
  `sale_sd` decimal(10,2) NOT NULL DEFAULT '0.00',
  `ref_sd` decimal(10,2) NOT NULL DEFAULT '0.00',
  `agt_vat_amt` decimal(10,2) NOT NULL DEFAULT '0.00',
  `ref_agt_vat_amt` decimal(10,2) NOT NULL DEFAULT '0.00',
  PRIMARY KEY (`finaldate`,`organization_id`,`game_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_rep_dg_retailer`;
/*Table structure for table `st_rep_iw_agent` */

CREATE TABLE `st_rep_iw_agent` (
  `organization_id` int(10) unsigned NOT NULL,
  `parent_id` int(10) unsigned NOT NULL,
  `finaldate` date NOT NULL DEFAULT '0000-00-00',
  `game_id` int(10) NOT NULL,
  `sale_mrp` decimal(10,2) NOT NULL DEFAULT '0.00',
  `sale_comm` decimal(10,2) NOT NULL DEFAULT '0.00',
  `sale_net` decimal(10,2) NOT NULL DEFAULT '0.00',
  `sale_good_cause` decimal(10,4) NOT NULL DEFAULT '0.0000',
  `sale_vat` decimal(10,4) NOT NULL DEFAULT '0.0000',
  `sale_taxable` decimal(10,2) NOT NULL DEFAULT '0.00',
  `ref_sale_mrp` decimal(10,2) NOT NULL DEFAULT '0.00',
  `ref_comm` decimal(10,2) NOT NULL DEFAULT '0.00',
  `ref_net_amt` decimal(10,2) NOT NULL DEFAULT '0.00',
  `ref_good_cause` decimal(10,4) NOT NULL DEFAULT '0.0000',
  `ref_vat` decimal(10,4) NOT NULL DEFAULT '0.0000',
  `ref_taxable` decimal(10,2) NOT NULL DEFAULT '0.00',
  `pwt_mrp` decimal(10,2) NOT NULL DEFAULT '0.00',
  `ret_pwt_comm` decimal(10,2) NOT NULL DEFAULT '0.00',
  `pwt_net_amt` decimal(10,2) NOT NULL DEFAULT '0.00',
  `direct_pwt_amt` decimal(10,2) NOT NULL DEFAULT '0.00',
  `direct_pwt_comm` decimal(10,2) NOT NULL DEFAULT '0.00',
  `direct_pwt_net_amt` decimal(10,2) NOT NULL DEFAULT '0.00',
  `direct_pwt_tax` decimal(10,2) NOT NULL DEFAULT '0.00',
  PRIMARY KEY (`organization_id`,`finaldate`,`game_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
--rollback drop table `st_rep_iw_agent`;
/*Table structure for table `st_rep_iw_bo` */

CREATE TABLE `st_rep_iw_bo` (
  `organization_id` int(10) unsigned NOT NULL,
  `parent_id` int(10) unsigned NOT NULL,
  `finaldate` date NOT NULL DEFAULT '0000-00-00',
  `game_id` int(10) NOT NULL,
  `sale_mrp` decimal(10,2) NOT NULL DEFAULT '0.00',
  `sale_comm` decimal(10,2) NOT NULL DEFAULT '0.00',
  `sale_net` decimal(10,2) NOT NULL DEFAULT '0.00',
  `sale_good_cause` decimal(10,4) NOT NULL DEFAULT '0.0000',
  `sale_vat` decimal(10,4) NOT NULL DEFAULT '0.0000',
  `sale_taxable` decimal(10,2) NOT NULL DEFAULT '0.00',
  `ref_sale_mrp` decimal(10,2) NOT NULL DEFAULT '0.00',
  `ref_comm` decimal(10,2) NOT NULL DEFAULT '0.00',
  `ref_net_amt` decimal(10,2) NOT NULL DEFAULT '0.00',
  `ref_good_cause` decimal(10,4) NOT NULL DEFAULT '0.0000',
  `ref_vat` decimal(10,4) NOT NULL DEFAULT '0.0000',
  `ref_taxable` decimal(10,2) NOT NULL DEFAULT '0.00',
  `pwt_mrp` decimal(10,2) NOT NULL DEFAULT '0.00',
  `ret_pwt_comm` decimal(10,2) NOT NULL DEFAULT '0.00',
  `pwt_net_amt` decimal(10,2) NOT NULL DEFAULT '0.00',
  `direct_pwt_amt` decimal(10,2) NOT NULL DEFAULT '0.00',
  `direct_pwt_comm` decimal(10,2) NOT NULL DEFAULT '0.00',
  `direct_pwt_net_amt` decimal(10,2) NOT NULL DEFAULT '0.00',
  `direct_pwt_tax` decimal(10,2) NOT NULL DEFAULT '0.00',
  PRIMARY KEY (`organization_id`,`finaldate`,`game_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
--rollback drop table `st_rep_iw_bo`;
/*Table structure for table `st_rep_iw_retailer` */

CREATE TABLE `st_rep_iw_retailer` (
  `organization_id` int(10) unsigned NOT NULL,
  `parent_id` int(10) unsigned NOT NULL,
  `finaldate` date NOT NULL,
  `game_id` int(10) NOT NULL,
  `sale_mrp` decimal(10,2) NOT NULL DEFAULT '0.00',
  `sale_comm` decimal(10,2) NOT NULL DEFAULT '0.00',
  `sale_net` decimal(10,2) NOT NULL DEFAULT '0.00',
  `sale_good_cause` decimal(10,4) NOT NULL DEFAULT '0.0000',
  `sale_vat` decimal(10,4) NOT NULL DEFAULT '0.0000',
  `sale_taxable` decimal(10,2) NOT NULL DEFAULT '0.00',
  `ref_sale_mrp` decimal(10,2) NOT NULL DEFAULT '0.00',
  `ref_comm` decimal(10,2) NOT NULL DEFAULT '0.00',
  `ref_net_amt` decimal(10,2) NOT NULL DEFAULT '0.00',
  `ref_good_cause` decimal(10,4) NOT NULL DEFAULT '0.0000',
  `ref_vat` decimal(10,4) NOT NULL DEFAULT '0.0000',
  `ref_taxable` decimal(10,2) NOT NULL DEFAULT '0.00',
  `pwt_mrp` decimal(10,2) NOT NULL DEFAULT '0.00',
  `ret_pwt_comm` decimal(10,2) NOT NULL DEFAULT '0.00',
  `pwt_net_amt` decimal(10,2) NOT NULL DEFAULT '0.00',
  PRIMARY KEY (`organization_id`,`finaldate`,`game_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
--rollback drop table `st_rep_iw_retailer`;
/*Table structure for table `st_rep_ola_agent` */

CREATE TABLE `st_rep_ola_agent` (
  `organization_id` int(10) unsigned NOT NULL,
  `parent_id` int(10) unsigned NOT NULL,
  `finaldate` date NOT NULL DEFAULT '0000-00-00',
  `wallet_id` int(10) NOT NULL,
  `deposit_mrp` decimal(10,2) NOT NULL DEFAULT '0.00',
  `deposit_comm` decimal(10,2) NOT NULL DEFAULT '0.00',
  `deposit_net` decimal(10,2) NOT NULL DEFAULT '0.00',
  `ref_deposit_mrp` decimal(10,2) NOT NULL DEFAULT '0.00',
  `ref_deposit_comm` decimal(10,2) NOT NULL DEFAULT '0.00',
  `ref_deposit_net_amt` decimal(10,2) NOT NULL DEFAULT '0.00',
  `withdrawal_mrp` decimal(10,2) NOT NULL DEFAULT '0.00',
  `withdrawal_comm` decimal(10,2) NOT NULL DEFAULT '0.00',
  `withdrawal_net_amt` decimal(10,2) NOT NULL DEFAULT '0.00',
  `ref_withdrawal_mrp` decimal(10,2) NOT NULL DEFAULT '0.00',
  `ref_withdrawal_comm` decimal(10,2) NOT NULL DEFAULT '0.00',
  `ref_withdrawal_net_amt` decimal(10,2) NOT NULL DEFAULT '0.00',
  `net_gaming` decimal(10,2) NOT NULL DEFAULT '0.00',
  `net_gaming_comm` decimal(10,2) NOT NULL DEFAULT '0.00',
  `net_gaming_tds_comm` decimal(10,2) NOT NULL DEFAULT '0.00',
  `net_gaming_net_comm` decimal(10,2) NOT NULL DEFAULT '0.00',
  PRIMARY KEY (`finaldate`,`organization_id`,`wallet_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_rep_ola_agent`;
/*Table structure for table `st_rep_ola_agt_dir` */

CREATE TABLE `st_rep_ola_agt_dir` (
  `organization_id` int(10) unsigned NOT NULL,
  `parent_id` int(10) unsigned NOT NULL,
  `finaldate` date NOT NULL DEFAULT '0000-00-00',
  `wallet_id` int(10) NOT NULL,
  `deposit_mrp` decimal(10,2) NOT NULL DEFAULT '0.00',
  `deposit_comm` decimal(10,2) NOT NULL DEFAULT '0.00',
  `deposit_net` decimal(10,2) NOT NULL DEFAULT '0.00',
  `ref_deposit_mrp` decimal(10,2) NOT NULL DEFAULT '0.00',
  `ref_deposit_comm` decimal(10,2) NOT NULL DEFAULT '0.00',
  `ref_deposit_net_amt` decimal(10,2) NOT NULL DEFAULT '0.00',
  `withdrawal_mrp` decimal(10,2) NOT NULL DEFAULT '0.00',
  `withdrawal_comm` decimal(10,2) NOT NULL DEFAULT '0.00',
  `withdrawal_net_amt` decimal(10,2) NOT NULL DEFAULT '0.00',
  `net_gaming` decimal(10,2) NOT NULL DEFAULT '0.00',
  `net_gaming_comm` decimal(10,2) NOT NULL DEFAULT '0.00',
  `net_gaming_tds_comm` decimal(10,2) NOT NULL DEFAULT '0.00',
  `net_gaming_net_comm` decimal(10,2) NOT NULL DEFAULT '0.00',
  PRIMARY KEY (`finaldate`,`organization_id`,`wallet_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_rep_ola_agt_dir`;
/*Table structure for table `st_rep_ola_bo` */

CREATE TABLE `st_rep_ola_bo` (
  `organization_id` int(10) unsigned NOT NULL,
  `parent_id` int(10) unsigned NOT NULL,
  `finaldate` date NOT NULL DEFAULT '0000-00-00',
  `wallet_id` int(10) NOT NULL,
  `deposit_mrp` decimal(10,2) NOT NULL DEFAULT '0.00',
  `deposit_comm` decimal(10,2) NOT NULL DEFAULT '0.00',
  `deposit_net` decimal(10,2) NOT NULL DEFAULT '0.00',
  `ref_deposit_mrp` decimal(10,2) NOT NULL DEFAULT '0.00',
  `ref_deposit_comm` decimal(10,2) NOT NULL DEFAULT '0.00',
  `ref_deposit_net_amt` decimal(10,2) NOT NULL DEFAULT '0.00',
  `withdrawal_mrp` decimal(10,2) NOT NULL DEFAULT '0.00',
  `withdrawal_comm` decimal(10,2) NOT NULL DEFAULT '0.00',
  `withdrawal_net_amt` decimal(10,2) NOT NULL DEFAULT '0.00',
  `ref_withdrawal_mrp` decimal(10,2) NOT NULL DEFAULT '0.00',
  `ref_withdrawal_comm` decimal(10,2) NOT NULL DEFAULT '0.00',
  `ref_withdrawal_net_amt` decimal(10,2) NOT NULL DEFAULT '0.00',
  `net_gaming` decimal(10,2) NOT NULL DEFAULT '0.00',
  `net_gaming_comm` decimal(10,2) NOT NULL DEFAULT '0.00',
  `net_gaming_tds_comm` decimal(10,2) NOT NULL DEFAULT '0.00',
  `net_gaming_net_comm` decimal(10,2) NOT NULL DEFAULT '0.00',
  PRIMARY KEY (`finaldate`,`organization_id`,`wallet_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_rep_ola_bo`;
/*Table structure for table `st_rep_ola_bo_dir` */

CREATE TABLE `st_rep_ola_bo_dir` (
  `organization_id` int(10) unsigned NOT NULL,
  `parent_id` int(10) unsigned NOT NULL,
  `finaldate` date NOT NULL DEFAULT '0000-00-00',
  `wallet_id` int(10) NOT NULL,
  `deposit_mrp` decimal(10,2) NOT NULL DEFAULT '0.00',
  `ref_deposit_mrp` decimal(10,2) NOT NULL DEFAULT '0.00',
  `withdrawal_mrp` decimal(10,2) NOT NULL DEFAULT '0.00',
  `net_gaming` decimal(10,2) NOT NULL DEFAULT '0.00',
  `net_gaming_comm` decimal(10,2) NOT NULL DEFAULT '0.00',
  `net_gaming_tds_comm` decimal(10,2) NOT NULL DEFAULT '0.00',
  `net_gaming_net_comm` decimal(10,2) NOT NULL DEFAULT '0.00',
  PRIMARY KEY (`finaldate`,`organization_id`,`wallet_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_rep_ola_bo_dir`;
/*Table structure for table `st_rep_ola_retailer` */

CREATE TABLE `st_rep_ola_retailer` (
  `organization_id` int(10) unsigned NOT NULL,
  `parent_id` int(10) unsigned NOT NULL,
  `finaldate` date NOT NULL,
  `wallet_id` int(10) NOT NULL,
  `deposit_mrp` decimal(10,2) NOT NULL DEFAULT '0.00',
  `deposit_comm` decimal(10,2) NOT NULL DEFAULT '0.00',
  `deposit_net` decimal(10,2) NOT NULL DEFAULT '0.00',
  `ref_deposit_mrp` decimal(10,2) NOT NULL DEFAULT '0.00',
  `ref_deposit_comm` decimal(10,2) NOT NULL DEFAULT '0.00',
  `ref_deposit_net_amt` decimal(10,2) NOT NULL DEFAULT '0.00',
  `withdrawal_mrp` decimal(10,2) NOT NULL DEFAULT '0.00',
  `withdrawal_comm` decimal(10,2) NOT NULL DEFAULT '0.00',
  `withdrawal_net_amt` decimal(10,2) NOT NULL DEFAULT '0.00',
  `ref_withdrawal_mrp` decimal(10,2) NOT NULL DEFAULT '0.00',
  `ref_withdrawal_comm` decimal(10,2) NOT NULL DEFAULT '0.00',
  `ref_withdrawal_net_amt` decimal(10,2) NOT NULL DEFAULT '0.00',
  `net_gaming` decimal(10,2) NOT NULL DEFAULT '0.00',
  `net_gaming_comm` decimal(10,2) NOT NULL DEFAULT '0.00',
  `net_gaming_tds_comm` decimal(10,2) NOT NULL DEFAULT '0.00',
  `net_gaming_net_comm` decimal(10,2) NOT NULL DEFAULT '0.00',
  PRIMARY KEY (`finaldate`,`organization_id`,`wallet_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_rep_ola_retailer`;
/*Table structure for table `st_rep_org_bal_history` */

CREATE TABLE `st_rep_org_bal_history` (
  `organization_id` int(10) NOT NULL DEFAULT '0',
  `organization_type` enum('AGENT','BO','RETAILER') DEFAULT NULL,
  `parent_id` int(10) DEFAULT NULL,
  `finaldate` date NOT NULL DEFAULT '0000-00-00',
  `opening_bal` decimal(20,4) DEFAULT '0.0000',
  `opening_bal_cl_inc` decimal(20,4) DEFAULT '0.0000',
  `net_amount_transaction` decimal(20,4) DEFAULT '0.0000',
  `cl` decimal(20,4) DEFAULT '0.0000',
  `xcl` decimal(20,4) DEFAULT '0.0000',
  PRIMARY KEY (`finaldate`,`organization_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_rep_org_bal_history`;
/*Table structure for table `st_rep_se_agent` */

CREATE TABLE `st_rep_se_agent` (
  `organization_id` int(10) unsigned NOT NULL,
  `parent_id` int(10) NOT NULL,
  `finaldate` date NOT NULL,
  `game_id` int(10) NOT NULL,
  `sale_book_mrp` decimal(10,2) NOT NULL DEFAULT '0.00',
  `sale_ticket_mrp` decimal(10,2) NOT NULL DEFAULT '0.00',
  `sale_comm` decimal(10,2) NOT NULL DEFAULT '0.00',
  `sale_book_net` decimal(10,2) NOT NULL DEFAULT '0.00',
  `sale_ticket_net` decimal(10,2) NOT NULL DEFAULT '0.00',
  `sale_vat` decimal(10,4) NOT NULL DEFAULT '0.0000',
  `sale_taxable` decimal(10,4) NOT NULL DEFAULT '0.0000',
  `ref_sale_mrp` decimal(10,2) NOT NULL DEFAULT '0.00',
  `ref_comm` decimal(10,2) NOT NULL DEFAULT '0.00',
  `ref_net_amt` decimal(10,2) NOT NULL DEFAULT '0.00',
  `ref_vat` decimal(10,4) NOT NULL DEFAULT '0.0000',
  `ref_taxable` decimal(10,4) NOT NULL DEFAULT '0.0000',
  `pwt_mrp` decimal(10,2) NOT NULL DEFAULT '0.00',
  `pwt_comm` decimal(10,2) NOT NULL DEFAULT '0.00',
  `pwt_net_amt` decimal(10,2) NOT NULL DEFAULT '0.00',
  `direct_pwt_amt` decimal(10,2) NOT NULL DEFAULT '0.00',
  `direct_pwt_comm` decimal(10,2) NOT NULL DEFAULT '0.00',
  `direct_pwt_net_amt` decimal(10,2) NOT NULL DEFAULT '0.00',
  `direct_pwt_tax` decimal(10,2) NOT NULL DEFAULT '0.00',
  PRIMARY KEY (`finaldate`,`organization_id`,`game_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_rep_se_agent`;
/*Table structure for table `st_rep_se_bo` */

CREATE TABLE `st_rep_se_bo` (
  `organization_id` int(10) unsigned NOT NULL,
  `parent_id` int(10) NOT NULL,
  `finaldate` date NOT NULL,
  `game_id` int(10) NOT NULL,
  `sale_book_mrp` decimal(10,2) NOT NULL DEFAULT '0.00',
  `sale_ticket_mrp` decimal(10,2) NOT NULL DEFAULT '0.00',
  `sale_comm` decimal(10,2) NOT NULL DEFAULT '0.00',
  `sale_book_net` decimal(10,2) NOT NULL DEFAULT '0.00',
  `sale_ticket_net` decimal(10,2) NOT NULL DEFAULT '0.00',
  `sale_vat` decimal(10,4) NOT NULL DEFAULT '0.0000',
  `sale_taxable` decimal(10,4) NOT NULL DEFAULT '0.0000',
  `ref_sale_mrp` decimal(10,2) NOT NULL DEFAULT '0.00',
  `ref_comm` decimal(10,2) NOT NULL DEFAULT '0.00',
  `ref_net_amt` decimal(10,2) NOT NULL DEFAULT '0.00',
  `ref_vat` decimal(10,4) NOT NULL DEFAULT '0.0000',
  `ref_taxable` decimal(10,4) NOT NULL DEFAULT '0.0000',
  `pwt_mrp` decimal(10,2) NOT NULL DEFAULT '0.00',
  `pwt_comm` decimal(10,2) NOT NULL DEFAULT '0.00',
  `pwt_net_amt` decimal(10,2) NOT NULL DEFAULT '0.00',
  `direct_pwt_amt` decimal(10,2) NOT NULL DEFAULT '0.00',
  `direct_pwt_comm` decimal(10,2) NOT NULL DEFAULT '0.00',
  `direct_pwt_net_amt` decimal(10,2) NOT NULL DEFAULT '0.00',
  `direct_pwt_tax` decimal(10,2) NOT NULL DEFAULT '0.00',
  PRIMARY KEY (`finaldate`,`organization_id`,`game_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_rep_se_bo`;
/*Table structure for table `st_rep_se_retailer` */

CREATE TABLE `st_rep_se_retailer` (
  `organization_id` int(10) unsigned NOT NULL,
  `parent_id` int(10) NOT NULL,
  `finaldate` date NOT NULL,
  `game_id` int(10) NOT NULL,
  `sale_book_mrp` decimal(10,2) NOT NULL DEFAULT '0.00',
  `sale_ticket_mrp` decimal(10,2) NOT NULL DEFAULT '0.00',
  `sale_comm` decimal(10,2) NOT NULL DEFAULT '0.00',
  `sale_book_net` decimal(10,2) NOT NULL DEFAULT '0.00',
  `sale_ticket_net` decimal(10,2) NOT NULL DEFAULT '0.00',
  `sale_vat` decimal(10,4) NOT NULL DEFAULT '0.0000',
  `sale_taxable` decimal(10,4) NOT NULL DEFAULT '0.0000',
  `ref_sale_mrp` decimal(10,2) NOT NULL DEFAULT '0.00',
  `ref_comm` decimal(10,2) NOT NULL DEFAULT '0.00',
  `ref_net_amt` decimal(10,2) NOT NULL DEFAULT '0.00',
  `ref_vat` decimal(10,4) NOT NULL DEFAULT '0.0000',
  `ref_taxable` decimal(10,4) NOT NULL DEFAULT '0.0000',
  `pwt_mrp` decimal(10,2) NOT NULL DEFAULT '0.00',
  `ret_pwt_comm` decimal(10,2) NOT NULL DEFAULT '0.00',
  `pwt_net_amt` decimal(10,2) NOT NULL DEFAULT '0.00',
  PRIMARY KEY (`finaldate`,`organization_id`,`game_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_rep_se_retailer`;
/*Table structure for table `st_rep_sle_agent` */

CREATE TABLE `st_rep_sle_agent` (
  `organization_id` int(10) unsigned NOT NULL,
  `parent_id` int(10) unsigned NOT NULL,
  `finaldate` date NOT NULL DEFAULT '0000-00-00',
  `game_id` int(10) NOT NULL,
  `game_type_id` int(10) NOT NULL,
  `sale_mrp` decimal(10,2) NOT NULL DEFAULT '0.00',
  `sale_comm` decimal(10,2) NOT NULL DEFAULT '0.00',
  `sale_net` decimal(10,2) NOT NULL DEFAULT '0.00',
  `sale_good_cause` decimal(10,4) NOT NULL DEFAULT '0.0000',
  `sale_vat` decimal(10,4) NOT NULL DEFAULT '0.0000',
  `sale_taxable` decimal(10,2) NOT NULL DEFAULT '0.00',
  `ref_sale_mrp` decimal(10,2) NOT NULL DEFAULT '0.00',
  `ref_comm` decimal(10,2) NOT NULL DEFAULT '0.00',
  `ref_net_amt` decimal(10,2) NOT NULL DEFAULT '0.00',
  `ref_good_cause` decimal(10,4) NOT NULL DEFAULT '0.0000',
  `ref_vat` decimal(10,4) NOT NULL DEFAULT '0.0000',
  `ref_taxable` decimal(10,2) NOT NULL DEFAULT '0.00',
  `pwt_mrp` decimal(10,2) NOT NULL DEFAULT '0.00',
  `ret_pwt_comm` decimal(10,2) NOT NULL DEFAULT '0.00',
  `govt_pwt_comm` decimal(10,2) NOT NULL DEFAULT '0.00',
  `pwt_net_amt` decimal(10,2) NOT NULL DEFAULT '0.00',
  `direct_pwt_amt` decimal(10,2) NOT NULL DEFAULT '0.00',
  `direct_pwt_comm` decimal(10,2) NOT NULL DEFAULT '0.00',
  `direct_pwt_net_amt` decimal(10,2) NOT NULL DEFAULT '0.00',
  `direct_pwt_tax` decimal(10,2) NOT NULL DEFAULT '0.00',
  PRIMARY KEY (`finaldate`,`organization_id`,`game_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_rep_sle_agent`;
/*Table structure for table `st_rep_sle_bo` */

CREATE TABLE `st_rep_sle_bo` (
  `organization_id` int(10) unsigned NOT NULL,
  `parent_id` int(10) unsigned NOT NULL,
  `finaldate` date NOT NULL DEFAULT '0000-00-00',
  `game_id` int(10) NOT NULL,
  `game_type_id` int(10) NOT NULL,
  `sale_mrp` decimal(10,2) NOT NULL DEFAULT '0.00',
  `sale_comm` decimal(10,2) NOT NULL DEFAULT '0.00',
  `sale_net` decimal(10,2) NOT NULL DEFAULT '0.00',
  `sale_good_cause` decimal(10,4) NOT NULL DEFAULT '0.0000',
  `sale_vat` decimal(10,4) NOT NULL DEFAULT '0.0000',
  `sale_taxable` decimal(10,2) NOT NULL DEFAULT '0.00',
  `ref_sale_mrp` decimal(10,2) NOT NULL DEFAULT '0.00',
  `ref_comm` decimal(10,2) NOT NULL DEFAULT '0.00',
  `ref_net_amt` decimal(10,2) NOT NULL DEFAULT '0.00',
  `ref_good_cause` decimal(10,4) NOT NULL DEFAULT '0.0000',
  `ref_vat` decimal(10,4) NOT NULL DEFAULT '0.0000',
  `ref_taxable` decimal(10,2) NOT NULL DEFAULT '0.00',
  `pwt_mrp` decimal(10,2) NOT NULL DEFAULT '0.00',
  `ret_pwt_comm` decimal(10,2) NOT NULL DEFAULT '0.00',
  `govt_pwt_comm` decimal(10,2) NOT NULL DEFAULT '0.00',
  `pwt_net_amt` decimal(10,2) NOT NULL DEFAULT '0.00',
  `direct_pwt_amt` decimal(10,2) NOT NULL DEFAULT '0.00',
  `direct_pwt_comm` decimal(10,2) NOT NULL DEFAULT '0.00',
  `direct_pwt_net_amt` decimal(10,2) NOT NULL DEFAULT '0.00',
  `direct_pwt_tax` decimal(10,2) NOT NULL DEFAULT '0.00',
  PRIMARY KEY (`finaldate`,`organization_id`,`game_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_rep_sle_bo`;
/*Table structure for table `st_rep_sle_retailer` */

CREATE TABLE `st_rep_sle_retailer` (
  `organization_id` int(10) unsigned NOT NULL,
  `parent_id` int(10) unsigned NOT NULL,
  `finaldate` date NOT NULL,
  `game_id` int(10) NOT NULL,
  `game_type_id` int(10) NOT NULL,
  `sale_mrp` decimal(10,2) NOT NULL DEFAULT '0.00',
  `sale_comm` decimal(10,2) NOT NULL DEFAULT '0.00',
  `sale_net` decimal(10,2) NOT NULL DEFAULT '0.00',
  `sale_good_cause` decimal(10,4) NOT NULL DEFAULT '0.0000',
  `sale_vat` decimal(10,4) NOT NULL DEFAULT '0.0000',
  `sale_taxable` decimal(10,2) NOT NULL DEFAULT '0.00',
  `ref_sale_mrp` decimal(10,2) NOT NULL DEFAULT '0.00',
  `ref_comm` decimal(10,2) NOT NULL DEFAULT '0.00',
  `ref_net_amt` decimal(10,2) NOT NULL DEFAULT '0.00',
  `ref_good_cause` decimal(10,4) NOT NULL DEFAULT '0.0000',
  `ref_vat` decimal(10,4) NOT NULL DEFAULT '0.0000',
  `ref_taxable` decimal(10,2) NOT NULL DEFAULT '0.00',
  `pwt_mrp` decimal(10,2) NOT NULL DEFAULT '0.00',
  `ret_pwt_comm` decimal(10,2) NOT NULL DEFAULT '0.00',
  `govt_pwt_comm` decimal(10,2) NOT NULL DEFAULT '0.00',
  `pwt_net_amt` decimal(10,2) NOT NULL DEFAULT '0.00',
  PRIMARY KEY (`finaldate`,`organization_id`,`game_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_rep_sle_retailer`;
/*Table structure for table `st_rep_vs_agent` */

CREATE TABLE `st_rep_vs_agent` (
  `organization_id` int(10) unsigned NOT NULL,
  `parent_id` int(10) unsigned NOT NULL,
  `finaldate` date NOT NULL DEFAULT '0000-00-00',
  `game_id` int(10) NOT NULL,
  `sale_mrp` decimal(10,2) NOT NULL DEFAULT '0.00',
  `sale_comm` decimal(10,2) NOT NULL DEFAULT '0.00',
  `sale_net` decimal(10,2) NOT NULL DEFAULT '0.00',
  `sale_good_cause` decimal(10,4) NOT NULL DEFAULT '0.0000',
  `sale_vat` decimal(10,4) NOT NULL DEFAULT '0.0000',
  `sale_taxable` decimal(10,2) NOT NULL DEFAULT '0.00',
  `ref_sale_mrp` decimal(10,2) NOT NULL DEFAULT '0.00',
  `ref_comm` decimal(10,2) NOT NULL DEFAULT '0.00',
  `ref_net_amt` decimal(10,2) NOT NULL DEFAULT '0.00',
  `ref_good_cause` decimal(10,4) NOT NULL DEFAULT '0.0000',
  `ref_vat` decimal(10,4) NOT NULL DEFAULT '0.0000',
  `ref_taxable` decimal(10,2) NOT NULL DEFAULT '0.00',
  `pwt_mrp` decimal(10,2) NOT NULL DEFAULT '0.00',
  `ret_pwt_comm` decimal(10,2) NOT NULL DEFAULT '0.00',
  `pwt_net_amt` decimal(10,2) NOT NULL DEFAULT '0.00',
  `direct_pwt_amt` decimal(10,2) NOT NULL DEFAULT '0.00',
  `direct_pwt_comm` decimal(10,2) NOT NULL DEFAULT '0.00',
  `direct_pwt_net_amt` decimal(10,2) NOT NULL DEFAULT '0.00',
  `direct_pwt_tax` decimal(10,2) NOT NULL DEFAULT '0.00',
  PRIMARY KEY (`organization_id`,`finaldate`,`game_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
--rollback drop table `st_rep_vs_agent`;
/*Table structure for table `st_rep_vs_bo` */

CREATE TABLE `st_rep_vs_bo` (
  `organization_id` int(10) unsigned NOT NULL,
  `parent_id` int(10) unsigned NOT NULL,
  `finaldate` date NOT NULL DEFAULT '0000-00-00',
  `game_id` int(10) NOT NULL,
  `sale_mrp` decimal(10,2) NOT NULL DEFAULT '0.00',
  `sale_comm` decimal(10,2) NOT NULL DEFAULT '0.00',
  `sale_net` decimal(10,2) NOT NULL DEFAULT '0.00',
  `sale_good_cause` decimal(10,4) NOT NULL DEFAULT '0.0000',
  `sale_vat` decimal(10,4) NOT NULL DEFAULT '0.0000',
  `sale_taxable` decimal(10,2) NOT NULL DEFAULT '0.00',
  `ref_sale_mrp` decimal(10,2) NOT NULL DEFAULT '0.00',
  `ref_comm` decimal(10,2) NOT NULL DEFAULT '0.00',
  `ref_net_amt` decimal(10,2) NOT NULL DEFAULT '0.00',
  `ref_good_cause` decimal(10,4) NOT NULL DEFAULT '0.0000',
  `ref_vat` decimal(10,4) NOT NULL DEFAULT '0.0000',
  `ref_taxable` decimal(10,2) NOT NULL DEFAULT '0.00',
  `pwt_mrp` decimal(10,2) NOT NULL DEFAULT '0.00',
  `ret_pwt_comm` decimal(10,2) NOT NULL DEFAULT '0.00',
  `pwt_net_amt` decimal(10,2) NOT NULL DEFAULT '0.00',
  `direct_pwt_amt` decimal(10,2) NOT NULL DEFAULT '0.00',
  `direct_pwt_comm` decimal(10,2) NOT NULL DEFAULT '0.00',
  `direct_pwt_net_amt` decimal(10,2) NOT NULL DEFAULT '0.00',
  `direct_pwt_tax` decimal(10,2) NOT NULL DEFAULT '0.00',
  PRIMARY KEY (`organization_id`,`finaldate`,`game_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
--rollback drop table `st_rep_vs_bo`;
/*Table structure for table `st_rep_vs_retailer` */

CREATE TABLE `st_rep_vs_retailer` (
  `organization_id` int(10) unsigned NOT NULL,
  `parent_id` int(10) unsigned NOT NULL,
  `finaldate` date NOT NULL,
  `game_id` int(10) NOT NULL,
  `sale_mrp` decimal(10,2) NOT NULL DEFAULT '0.00',
  `sale_comm` decimal(10,2) NOT NULL DEFAULT '0.00',
  `sale_net` decimal(10,2) NOT NULL DEFAULT '0.00',
  `sale_good_cause` decimal(10,4) NOT NULL DEFAULT '0.0000',
  `sale_vat` decimal(10,4) NOT NULL DEFAULT '0.0000',
  `sale_taxable` decimal(10,2) NOT NULL DEFAULT '0.00',
  `ref_sale_mrp` decimal(10,2) NOT NULL DEFAULT '0.00',
  `ref_comm` decimal(10,2) NOT NULL DEFAULT '0.00',
  `ref_net_amt` decimal(10,2) NOT NULL DEFAULT '0.00',
  `ref_good_cause` decimal(10,4) NOT NULL DEFAULT '0.0000',
  `ref_vat` decimal(10,4) NOT NULL DEFAULT '0.0000',
  `ref_taxable` decimal(10,2) NOT NULL DEFAULT '0.00',
  `pwt_mrp` decimal(10,2) NOT NULL DEFAULT '0.00',
  `ret_pwt_comm` decimal(10,2) NOT NULL DEFAULT '0.00',
  `pwt_net_amt` decimal(10,2) NOT NULL DEFAULT '0.00',
  PRIMARY KEY (`organization_id`,`finaldate`,`game_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
--rollback drop table `st_rep_vs_retailer`;
/*Table structure for table `st_se_agent_invoice_delchallan_mapping` */

CREATE TABLE `st_se_agent_invoice_delchallan_mapping` (
  `id` int(10) NOT NULL,
  `generated_invoice_id` varchar(20) NOT NULL,
  `generated_del_challan_id` varchar(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
--rollback drop table `st_se_agent_invoice_delchallan_mapping`;
/*Table structure for table `st_se_agent_order` */

CREATE TABLE `st_se_agent_order` (
  `order_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `agent_user_id` int(10) unsigned NOT NULL,
  `retailer_user_id` int(10) unsigned NOT NULL,
  `retailer_org_id` int(10) unsigned NOT NULL,
  `order_date` date NOT NULL,
  `order_status` enum('AUTO_PROCESSED','PROCESSED','CLOSED','REQUESTED','DENIED','SEMI_PROCESSED','ASSIGNED','APPROVED','AUTO_APPROVED','SEMI_ASSIGNED') NOT NULL,
  `self_initiated` enum('Y','N') NOT NULL,
  `invoice_id` int(10) unsigned DEFAULT NULL,
  `agent_org_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`order_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC;
--rollback drop table `st_se_agent_order`;
/*Table structure for table `st_se_agent_order_invoices` */

CREATE TABLE `st_se_agent_order_invoices` (
  `order_invoice_dc_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `order_id` int(10) unsigned NOT NULL,
  `invoice_id` int(10) unsigned zerofill DEFAULT NULL,
  `retailer_org_id` int(10) unsigned NOT NULL,
  `game_id` int(10) unsigned NOT NULL,
  `order_status` enum('AUTO_PROCESSED','PROCESSED','CLOSED','REQUESTED','DENIED','SEMI_PROCESSED','ASSIGNED','SEMI_ASSIGNED') NOT NULL,
  `nbr_of_books_dlvrd` int(10) DEFAULT NULL,
  `dc_id` bigint(10) NOT NULL,
  `agent_user_id` int(10) unsigned NOT NULL,
  `agent_org_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`order_invoice_dc_id`,`game_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;
--rollback drop table `st_se_agent_order_invoices`;
/*Table structure for table `st_se_agent_ordered_games` */

CREATE TABLE `st_se_agent_ordered_games` (
  `order_id` int(10) unsigned NOT NULL,
  `game_id` int(10) unsigned NOT NULL,
  `nbr_of_books_req` int(10) unsigned DEFAULT NULL,
  `nbr_of_books_appr` int(10) unsigned DEFAULT NULL,
  `nbr_of_books_dlvrd` int(10) unsigned DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
--rollback drop table `st_se_agent_ordered_games`;
/*Table structure for table `st_se_agent_pwt` */

CREATE TABLE `st_se_agent_pwt` (
  `virn_code` char(24) NOT NULL DEFAULT '',
  `transaction_id` bigint(10) unsigned NOT NULL,
  `game_id` int(10) unsigned NOT NULL,
  `agent_user_id` int(10) unsigned NOT NULL,
  `retailer_user_id` int(10) unsigned NOT NULL,
  `retailer_org_id` int(10) unsigned DEFAULT NULL,
  `pwt_amt` decimal(20,2) NOT NULL,
  `comm_amt` decimal(20,2) NOT NULL,
  `net_amt` decimal(20,2) NOT NULL,
  `agent_org_id` int(10) unsigned NOT NULL,
  `status` enum('DONE_CLM','CLAIM_BAL','DONE_UNCLM','UNCLAIM_BAL') NOT NULL,
  `claim_comm` decimal(20,2) NOT NULL,
  `ticket_nbr` varchar(20) NOT NULL,
  PRIMARY KEY (`virn_code`,`game_id`,`ticket_nbr`),
  KEY `transaction_id` (`transaction_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
--rollback drop table `st_se_agent_pwt`;
/*Table structure for table `st_se_agent_ret_loose_book_transaction` */

CREATE TABLE `st_se_agent_ret_loose_book_transaction` (
  `transaction_id` bigint(10) unsigned NOT NULL,
  `game_id` int(10) unsigned NOT NULL,
  `agent_user_id` int(10) unsigned NOT NULL,
  `retailer_org_id` int(10) unsigned NOT NULL,
  `mrp_amt` decimal(20,2) DEFAULT NULL,
  `retComm` decimal(20,2) DEFAULT NULL,
  `comm_amt` decimal(20,2) DEFAULT NULL,
  `net_amt` decimal(20,2) NOT NULL,
  `transaction_type` enum('LOOSE_SALE','LOOSE_SALE_RET') NOT NULL,
  `nbr_of_tickets` int(11) NOT NULL,
  `vat_amt` decimal(20,2) DEFAULT NULL,
  `taxable_sale` decimal(20,2) DEFAULT NULL,
  `agent_org_id` int(10) unsigned NOT NULL,
  KEY `transaction_id` (`transaction_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;
--rollback drop table `st_se_agent_ret_loose_book_transaction`;
/*Table structure for table `st_se_agent_retailer_pwt_comm_variance_history` */

CREATE TABLE `st_se_agent_retailer_pwt_comm_variance_history` (
  `agent_org_id` int(11) unsigned NOT NULL,
  `retailer_org_id` int(11) unsigned NOT NULL,
  `game_id` int(11) unsigned NOT NULL,
  `pwt_comm_variance` decimal(5,2) NOT NULL,
  `date_changed` datetime NOT NULL DEFAULT '0000-00-00 00:00:00'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
--rollback drop table `st_se_agent_retailer_pwt_comm_variance_history`;
/*Table structure for table `st_se_agent_retailer_sale_comm_variance_history` */

CREATE TABLE `st_se_agent_retailer_sale_comm_variance_history` (
  `agent_org_id` int(11) unsigned NOT NULL,
  `retailer_org_id` int(11) unsigned NOT NULL,
  `game_id` int(11) unsigned NOT NULL,
  `sale_comm_variance` decimal(5,2) NOT NULL,
  `date_changed` datetime NOT NULL DEFAULT '0000-00-00 00:00:00'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
--rollback drop table `st_se_agent_retailer_sale_comm_variance_history`;
/*Table structure for table `st_se_agent_retailer_sale_pwt_comm_variance` */

CREATE TABLE `st_se_agent_retailer_sale_pwt_comm_variance` (
  `agent_org_id` int(11) unsigned NOT NULL,
  `retailer_org_id` int(11) unsigned NOT NULL,
  `game_id` int(11) unsigned NOT NULL,
  `sale_comm_variance` decimal(5,2) DEFAULT NULL,
  `default_sale_comm_rate` decimal(5,2) DEFAULT NULL,
  `pwt_comm_variance` decimal(5,2) DEFAULT NULL,
  `default_pwt_comm_rate` decimal(5,2) DEFAULT NULL,
  PRIMARY KEY (`agent_org_id`,`retailer_org_id`,`game_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
--rollback drop table `st_se_agent_retailer_sale_pwt_comm_variance`;
/*Table structure for table `st_se_agent_retailer_transaction` */

CREATE TABLE `st_se_agent_retailer_transaction` (
  `transaction_id` bigint(10) unsigned NOT NULL,
  `game_id` int(10) unsigned NOT NULL,
  `agent_user_id` int(10) unsigned NOT NULL,
  `retailer_org_id` int(10) unsigned NOT NULL,
  `mrp_amt` decimal(20,2) DEFAULT NULL,
  `comm_amt` decimal(20,2) DEFAULT NULL,
  `net_amt` decimal(20,2) NOT NULL,
  `transaction_type` enum('SALE','SALE_RET') NOT NULL,
  `nbr_of_books` int(11) NOT NULL,
  `vat_amt` decimal(20,2) DEFAULT NULL,
  `taxable_sale` decimal(20,2) DEFAULT NULL,
  `agent_org_id` int(10) unsigned NOT NULL,
  KEY `transaction_id` (`transaction_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;
--rollback drop table `st_se_agent_retailer_transaction`;
/*Table structure for table `st_se_agt_direct_player_pwt` */

CREATE TABLE `st_se_agt_direct_player_pwt` (
  `task_id` int(10) NOT NULL,
  `agent_user_id` int(10) unsigned NOT NULL,
  `agent_org_id` int(10) unsigned NOT NULL,
  `ticket_nbr` char(14) NOT NULL DEFAULT '',
  `virn_code` char(24) NOT NULL DEFAULT '',
  `transaction_id` bigint(10) unsigned NOT NULL,
  `transaction_date` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `game_id` int(10) unsigned NOT NULL,
  `player_id` int(10) unsigned NOT NULL,
  `pwt_amt` decimal(20,2) NOT NULL,
  `tax_amt` decimal(20,2) NOT NULL,
  `net_amt` decimal(20,2) NOT NULL,
  `payment_type` enum('CASH','CHEQUE','TPT') NOT NULL,
  `cheque_nbr` varchar(10) CHARACTER SET utf8 DEFAULT NULL,
  `cheque_date` date DEFAULT NULL,
  `drawee_bank` varchar(50) CHARACTER SET utf8 DEFAULT NULL,
  `issuing_party_name` varchar(50) CHARACTER SET utf8 DEFAULT NULL,
  `pwt_claim_status` enum('CLAIM_RET_UNCLM','UNCLM_PWT','UNCLM_CANCELLED','CLAIM_PLR_AGT_UNCLM_DIR','CLAIM_PLR_AGT_CLM','CLAIM_BAL','UNCLAIM_BAL','DONE_CLM') NOT NULL,
  `claim_comm` decimal(20,2) NOT NULL,
  PRIMARY KEY (`virn_code`,`game_id`),
  KEY `transaction_id` (`transaction_id`),
  KEY `player_id` (`player_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_se_agt_direct_player_pwt`;
/*Table structure for table `st_se_bo_agent_loose_book_transaction` */

CREATE TABLE `st_se_bo_agent_loose_book_transaction` (
  `transaction_id` bigint(10) unsigned NOT NULL,
  `game_id` int(10) unsigned NOT NULL,
  `nbrOfTickets` int(10) DEFAULT NULL,
  `agent_org_id` int(10) unsigned NOT NULL,
  `mrp_amt` decimal(20,2) unsigned DEFAULT NULL,
  `agtSaleCommRate` decimal(20,2) DEFAULT NULL,
  `comm_amt` decimal(20,2) DEFAULT NULL,
  `net_amt` decimal(20,2) NOT NULL,
  `transaction_type` enum('LOOSE_SALE','LOOSE_SALE_RET') NOT NULL,
  `vat_amt` decimal(20,2) DEFAULT '0.00',
  `taxable_sale` decimal(20,2) DEFAULT '0.00',
  `gov_comm_amt` decimal(10,2) DEFAULT '0.00',
  KEY `transaction_id` (`transaction_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
--rollback drop table `st_se_bo_agent_loose_book_transaction`;
/*Table structure for table `st_se_bo_agent_pwt_comm_variance_history` */

CREATE TABLE `st_se_bo_agent_pwt_comm_variance_history` (
  `agent_org_id` int(11) unsigned NOT NULL,
  `game_id` int(11) unsigned NOT NULL,
  `pwt_comm_variance` decimal(5,2) NOT NULL,
  `date_changed` datetime NOT NULL DEFAULT '0000-00-00 00:00:00'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
--rollback drop table `st_se_bo_agent_pwt_comm_variance_history`;
/*Table structure for table `st_se_bo_agent_sale_comm_variance_history` */

CREATE TABLE `st_se_bo_agent_sale_comm_variance_history` (
  `agent_org_id` int(11) unsigned NOT NULL,
  `game_id` int(11) unsigned NOT NULL,
  `sale_comm_variance` decimal(5,2) NOT NULL,
  `date_changed` datetime NOT NULL DEFAULT '0000-00-00 00:00:00'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
--rollback drop table `st_se_bo_agent_sale_comm_variance_history`;
/*Table structure for table `st_se_bo_agent_sale_pwt_comm_variance` */

CREATE TABLE `st_se_bo_agent_sale_pwt_comm_variance` (
  `agent_org_id` int(11) unsigned NOT NULL,
  `game_id` int(11) unsigned NOT NULL,
  `sale_comm_variance` decimal(5,2) DEFAULT NULL,
  `default_sale_comm_rate` decimal(5,2) DEFAULT NULL,
  `pwt_comm_variance` decimal(5,2) DEFAULT NULL,
  `default_pwt_comm_rate` decimal(5,2) DEFAULT NULL,
  PRIMARY KEY (`agent_org_id`,`game_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
--rollback drop table `st_se_bo_agent_sale_pwt_comm_variance`;
/*Table structure for table `st_se_bo_agent_transaction` */

CREATE TABLE `st_se_bo_agent_transaction` (
  `transaction_id` bigint(10) unsigned NOT NULL,
  `nbr_of_books` int(15) unsigned NOT NULL,
  `game_id` int(10) unsigned NOT NULL,
  `agent_org_id` int(10) unsigned NOT NULL,
  `mrp_amt` decimal(20,2) unsigned DEFAULT NULL,
  `comm_amt` decimal(20,2) DEFAULT NULL,
  `net_amt` decimal(20,2) NOT NULL,
  `transaction_type` enum('SALE','SALE_RET') NOT NULL,
  `vat_amt` decimal(20,2) DEFAULT '0.00',
  `taxable_sale` decimal(20,2) DEFAULT '0.00',
  `gov_comm_amt` decimal(10,2) DEFAULT '0.00',
  KEY `transaction_id` (`transaction_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
--rollback drop table `st_se_bo_agent_transaction`;
/*Table structure for table `st_se_bo_invoice_delchallan_mapping` */

CREATE TABLE `st_se_bo_invoice_delchallan_mapping` (
  `id` int(10) unsigned NOT NULL,
  `generated_invoice_id` varchar(20) NOT NULL,
  `generated_del_challan_id` varchar(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
--rollback drop table `st_se_bo_invoice_delchallan_mapping`;
/*Table structure for table `st_se_bo_order` */

CREATE TABLE `st_se_bo_order` (
  `order_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `agent_org_id` int(10) unsigned NOT NULL,
  `agent_user_id` int(10) unsigned NOT NULL,
  `order_date` date NOT NULL,
  `order_status` enum('AUTO_PROCESSED','PROCESSED','CLOSED','REQUESTED','DENIED','ASSIGNED','SEMI_PROCESSED','APPROVED','SEMI_ASSIGNED') NOT NULL,
  `self_initiated` enum('Y','N') NOT NULL,
  `invoice_id` int(10) DEFAULT NULL,
  PRIMARY KEY (`order_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;
--rollback drop table `st_se_bo_order`;
/*Table structure for table `st_se_bo_order_invoices` */

CREATE TABLE `st_se_bo_order_invoices` (
  `order_invoice_dc_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `order_id` int(10) unsigned NOT NULL,
  `invoice_id` int(10) unsigned zerofill DEFAULT NULL,
  `agent_org_id` int(10) unsigned NOT NULL,
  `game_id` int(10) unsigned NOT NULL,
  `order_status` enum('AUTO_PROCESSED','PROCESSED','CLOSED','REQUESTED','DENIED','SEMI_PROCESSED','ASSIGNED','SEMI_ASSIGNED') NOT NULL,
  `nbr_of_books_dlvrd` int(15) DEFAULT NULL,
  `dc_id` bigint(10) NOT NULL,
  PRIMARY KEY (`order_invoice_dc_id`,`game_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;
--rollback drop table `st_se_bo_order_invoices`;
/*Table structure for table `st_se_bo_ordered_games` */

CREATE TABLE `st_se_bo_ordered_games` (
  `order_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `game_id` int(10) unsigned NOT NULL,
  `nbr_of_books_req` int(10) unsigned DEFAULT NULL,
  `nbr_of_books_appr` int(10) unsigned DEFAULT NULL,
  `nbr_of_books_dlvrd` int(10) unsigned DEFAULT NULL,
  KEY `NewIndex_1` (`order_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;
--rollback drop table `st_se_bo_ordered_games`;
/*Table structure for table `st_se_bo_pwt` */

CREATE TABLE `st_se_bo_pwt` (
  `virn_code` char(24) NOT NULL DEFAULT '',
  `transaction_id` bigint(10) unsigned NOT NULL,
  `game_id` int(10) unsigned NOT NULL,
  `agent_user_id` int(10) unsigned NOT NULL,
  `agent_org_id` int(10) unsigned NOT NULL,
  `pwt_amt` decimal(20,2) NOT NULL,
  `comm_amt` decimal(20,2) NOT NULL,
  `net_amt` decimal(20,2) NOT NULL,
  PRIMARY KEY (`virn_code`,`game_id`),
  KEY `transaction_id` (`transaction_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
--rollback drop table `st_se_bo_pwt`;
/*Table structure for table `st_se_book_activation` */

CREATE TABLE `st_se_book_activation` (
  `game_id` int(10) unsigned NOT NULL,
  `pack_nbr` varchar(15) NOT NULL DEFAULT '',
  `book_nbr` varchar(15) NOT NULL,
  `book_status` enum('a') NOT NULL,
  `transaction_id` bigint(20) unsigned NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
--rollback drop table `st_se_book_activation`;
/*Table structure for table `st_se_direct_player_pwt` */

CREATE TABLE `st_se_direct_player_pwt` (
  `task_id` int(10) unsigned NOT NULL,
  `ticket_nbr` char(14) NOT NULL DEFAULT '',
  `virn_code` char(24) NOT NULL DEFAULT '',
  `transaction_id` bigint(10) unsigned NOT NULL,
  `game_id` int(10) unsigned NOT NULL,
  `player_id` int(10) unsigned NOT NULL,
  `pwt_amt` decimal(20,2) NOT NULL,
  `tax_amt` decimal(20,2) NOT NULL,
  `net_amt` decimal(20,2) NOT NULL,
  `transaction_date` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `payment_type` enum('CASH','CHEQUE','TPT') NOT NULL,
  `cheque_nbr` varchar(10) DEFAULT NULL,
  `cheque_date` date DEFAULT NULL,
  `drawee_bank` varchar(50) DEFAULT NULL,
  `issuing_party_name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`virn_code`,`game_id`),
  KEY `transaction_id` (`transaction_id`),
  KEY `player_id` (`player_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
--rollback drop table `st_se_direct_player_pwt`;
/*Table structure for table `st_se_direct_player_pwt_temp_receipt` */

CREATE TABLE `st_se_direct_player_pwt_temp_receipt` (
  `pwt_receipt_id` bigint(10) unsigned NOT NULL AUTO_INCREMENT,
  `player_id` int(10) unsigned NOT NULL,
  `transaction_date` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `virn_code` char(24) NOT NULL DEFAULT '',
  `game_id` int(10) unsigned NOT NULL,
  `pwt_amt` decimal(20,2) DEFAULT NULL,
  `status` enum('OPEN','CLOSE') NOT NULL,
  `tax_amt` decimal(20,2) DEFAULT NULL,
  `net_amt` decimal(20,2) DEFAULT NULL,
  `ticket_nbr` char(14) NOT NULL,
  PRIMARY KEY (`pwt_receipt_id`,`game_id`),
  UNIQUE KEY `transaction_id` (`pwt_receipt_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
--rollback drop table `st_se_direct_player_pwt_temp_receipt`;
/*Table structure for table `st_se_game_govt_comm_history` */

CREATE TABLE `st_se_game_govt_comm_history` (
  `game_id` int(10) unsigned NOT NULL,
  `govt_comm_rate` decimal(5,2) NOT NULL,
  `date_changed` datetime NOT NULL DEFAULT '0000-00-00 00:00:00'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
--rollback drop table `st_se_game_govt_comm_history`;
/*Table structure for table `st_se_game_inv_detail` */

CREATE TABLE `st_se_game_inv_detail` (
  `transaction_id` bigint(10) DEFAULT NULL,
  `game_id` int(10) unsigned NOT NULL,
  `pack_nbr` varchar(15) NOT NULL DEFAULT '',
  `book_nbr` varchar(15) NOT NULL DEFAULT '',
  `current_owner` enum('BO','AGENT','RETAILER') NOT NULL,
  `current_owner_id` int(10) unsigned NOT NULL,
  `transaction_date` datetime DEFAULT NULL,
  `transaction_at` varchar(10) DEFAULT NULL,
  `transacrion_sale_comm_rate` decimal(5,2) DEFAULT NULL,
  `transaction_gov_comm_rate` decimal(5,2) DEFAULT NULL,
  `transaction_purchase_comm_rate` decimal(5,2) DEFAULT NULL,
  `changed_by_user_id` int(10) DEFAULT NULL,
  `book_status` enum('ACTIVE','INACTIVE','MISSING','CLAIMED','ASSIGNED','IN_TRANSIT','RECEIVED_BY_RET') DEFAULT NULL,
  `warehouse_id` int(10) DEFAULT NULL,
  `agent_invoice_id` varchar(50) DEFAULT NULL,
  `ret_invoice_id` varchar(50) DEFAULT NULL,
  `order_invoice_dc_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC;
--rollback drop table `st_se_game_inv_detail`;
/*Table structure for table `st_se_game_inv_status` */

CREATE TABLE `st_se_game_inv_status` (
  `game_id` int(10) unsigned NOT NULL,
  `pack_nbr` varchar(15) NOT NULL,
  `book_nbr` varchar(15) NOT NULL DEFAULT '',
  `current_owner` enum('BO','AGENT','RETAILER') NOT NULL,
  `current_owner_id` int(10) unsigned NOT NULL,
  `book_status` enum('ACTIVE','INACTIVE','MISSING','CLAIMED','ASSIGNED','IN_TRANSIT','RECEIVED_BY_RET') DEFAULT NULL,
  `warehouse_id` int(10) DEFAULT NULL,
  `book_receive_reg_date_ret` datetime DEFAULT NULL,
  `book_activation_date_ret` datetime DEFAULT NULL,
  `total_low_win_tier_tickets` int(10) DEFAULT NULL,
  `first_ticket_claim_date` datetime DEFAULT NULL,
  `total_low_win_tier_tickets_claimed` int(10) NOT NULL DEFAULT '0',
  `cur_rem_tickets` int(10) DEFAULT NULL,
  `active_tickets_upto` varchar(10) DEFAULT NULL,
  `agent_invoice_id` varchar(20) DEFAULT NULL,
  `ret_invoice_id` varchar(20) DEFAULT NULL,
  `order_id` int(11) DEFAULT NULL,
  `agent_dl_id` varchar(20) DEFAULT NULL,
  `ret_dl_id` varchar(20) DEFAULT NULL,
  `bo_dl_id` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`game_id`,`pack_nbr`,`book_nbr`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC;
--rollback drop table `st_se_game_inv_status`;
/*Table structure for table `st_se_game_master` */

CREATE TABLE `st_se_game_master` (
  `game_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `game_nbr` int(10) unsigned NOT NULL,
  `game_name` varchar(50) NOT NULL,
  `ticket_price` decimal(12,2) NOT NULL,
  `start_date` datetime DEFAULT NULL,
  `sale_end_date` datetime DEFAULT NULL,
  `pwt_end_date` datetime DEFAULT NULL,
  `nbr_of_tickets` int(20) unsigned DEFAULT NULL,
  `nbr_of_books` int(10) unsigned DEFAULT NULL,
  `nbr_of_tickets_per_book` int(10) unsigned NOT NULL,
  `nbr_of_books_per_pack` int(10) unsigned NOT NULL,
  `agent_sale_comm_rate` decimal(5,2) NOT NULL,
  `agent_pwt_comm_rate` decimal(5,2) NOT NULL,
  `retailer_sale_comm_rate` decimal(5,2) NOT NULL,
  `retailer_pwt_comm_rate` decimal(5,2) NOT NULL,
  `govt_comm_rate` decimal(5,2) DEFAULT NULL,
  `game_status` enum('OPEN','SALE_CLOSE','CLOSE','NEW','SALE_HOLD','TERMINATE') NOT NULL,
  `govt_comm_type` enum('FIXED_PER','MIN_PROFIT','MAP_FP') DEFAULT NULL,
  `fixed_amt` decimal(20,2) DEFAULT NULL,
  `add_inv_status` enum('T','F') DEFAULT NULL,
  `prize_payout_ratio` decimal(5,2) DEFAULT '0.00',
  `vat_amt` decimal(5,2) DEFAULT '0.00',
  `vat_paid` decimal(20,2) DEFAULT '0.00',
  `vat_balance` decimal(20,0) DEFAULT '0',
  `tickets_in_scheme` bigint(20) unsigned NOT NULL,
  `invoice_scheme_id` int(10) unsigned NOT NULL,
  `game_description` varchar(500) DEFAULT NULL,
  `game_image_path` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`game_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;
--rollback drop table `st_se_game_master`;
/*Table structure for table `st_se_game_ticket_inv_history` */

CREATE TABLE `st_se_game_ticket_inv_history` (
  `task_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `game_id` int(10) unsigned NOT NULL,
  `book_nbr` varchar(15) CHARACTER SET utf8 NOT NULL DEFAULT '',
  `current_owner` enum('BO','AGENT','RETAILER') NOT NULL,
  `current_owner_id` int(10) unsigned NOT NULL,
  `date` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `done_by_oid` int(10) unsigned DEFAULT NULL,
  `done_by_uid` int(10) unsigned DEFAULT NULL,
  `cur_rem_tickets` int(10) DEFAULT NULL,
  `active_tickets_upto` varchar(10) DEFAULT NULL,
  `sold_tickets` int(10) unsigned DEFAULT NULL,
  `status` enum('ACTIVE','INACTIVE','ACTIVE_CLOSE','CLAIMED_CLOSE') DEFAULT NULL,
  PRIMARY KEY (`task_id`,`game_id`,`book_nbr`,`current_owner`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
--rollback drop table `st_se_game_ticket_inv_history`;
/*Table structure for table `st_se_game_ticket_nbr_format` */

CREATE TABLE `st_se_game_ticket_nbr_format` (
  `game_id` int(10) unsigned NOT NULL,
  `pack_nbr_digits` tinyint(3) unsigned NOT NULL,
  `book_nbr_digits` tinyint(3) unsigned NOT NULL,
  `ticket_nbr_digits` tinyint(3) unsigned NOT NULL,
  `game_nbr_digits` tinyint(3) unsigned NOT NULL,
  `game_rank_digits` tinyint(3) unsigned NOT NULL,
  `game_virn_digits` tinyint(3) unsigned NOT NULL,
  PRIMARY KEY (`game_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
--rollback drop table `st_se_game_ticket_nbr_format`;
/*Table structure for table `st_se_invoicing_methods` */

CREATE TABLE `st_se_invoicing_methods` (
  `invoice_method_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `scheme_type` enum('ON_BOOK_ACTIVATION_RET','AFTER_DAYS_ACTIVATION','ON_WIN_CLAIM_RET','BO_AGENT_DISPATCH','AGENT_RET_DISPATCH','ON_SALES_RET','ON_BOOK_RECEIVE_REG_RET') DEFAULT NULL,
  `scheme_value_type` enum('FIXED','PERCENTAGE','BOOLEAN') DEFAULT NULL,
  `scheme_value` char(10) DEFAULT NULL,
  `scheme_description` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`invoice_method_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
--rollback drop table `st_se_invoicing_methods`;
/*Table structure for table `st_se_menu_master` */

CREATE TABLE `st_se_menu_master` (
  `menu_id` smallint(10) unsigned NOT NULL AUTO_INCREMENT,
  `action_id` smallint(10) unsigned NOT NULL,
  `menu_name` varchar(50) NOT NULL,
  `menu_disp_name` varchar(50) DEFAULT NULL,
  `parent_menu_id` smallint(10) unsigned NOT NULL,
  `item_order` tinyint(4) unsigned DEFAULT NULL,
  `menu_disp_name_fr` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `menu_disp_name_en` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`menu_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
--rollback drop table `st_se_menu_master`;

/*Table structure for table `st_se_miss_book_history` */

CREATE TABLE `st_se_miss_book_history` (
  `game_id` int(10) unsigned NOT NULL,
  `pack_nbr` varchar(15) NOT NULL,
  `book_nbr` varchar(15) NOT NULL DEFAULT '',
  `status` enum('ACTIVE','INACTIVE','MISSING','CLAIMED') DEFAULT NULL,
  `user_id` int(10) unsigned NOT NULL,
  `user_org_id` int(10) unsigned NOT NULL,
  `date` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `remarks` varchar(100) NOT NULL DEFAULT ''
) ENGINE=InnoDB DEFAULT CHARSET=utf8 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC;
--rollback drop table `st_se_miss_book_history`;
/*Table structure for table `st_se_miss_pwt_inv_history` */

CREATE TABLE `st_se_miss_pwt_inv_history` (
  `virn_code` char(24) NOT NULL DEFAULT '',
  `game_id` int(10) unsigned NOT NULL,
  `status` enum('ACTIVE','INACTIVE','MISSING','CLAIMED') DEFAULT NULL,
  `user_id` int(10) unsigned NOT NULL,
  `user_org_id` int(10) unsigned NOT NULL,
  `date` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `remarks` varchar(100) NOT NULL DEFAULT ''
) ENGINE=InnoDB DEFAULT CHARSET=utf8 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC;
--rollback drop table `st_se_miss_pwt_inv_history`;
/*Table structure for table `st_se_org_game_invoice_method_history` */

CREATE TABLE `st_se_org_game_invoice_method_history` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `retailer_id` int(10) unsigned NOT NULL,
  `game_id` int(10) unsigned NOT NULL,
  `invoice_method_id` int(10) unsigned NOT NULL,
  `invoice_method_value` char(10) NOT NULL,
  `date_changed` datetime NOT NULL,
  `changed_by_user_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
--rollback drop table `st_se_org_game_invoice_method_history`;
/*Table structure for table `st_se_org_game_invoice_methods` */

CREATE TABLE `st_se_org_game_invoice_methods` (
  `organization_id` int(10) unsigned NOT NULL,
  `game_id` int(10) unsigned NOT NULL,
  `invoice_method_id` int(10) unsigned NOT NULL,
  `invoice_method_value` char(20) DEFAULT NULL,
  PRIMARY KEY (`organization_id`,`game_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
--rollback drop table `st_se_org_game_invoice_methods`;
/*Table structure for table `st_se_priviledge_rep` */

CREATE TABLE `st_se_priviledge_rep` (
  `action_id` smallint(10) unsigned NOT NULL AUTO_INCREMENT,
  `priv_id` int(10) unsigned NOT NULL,
  `priv_title` varchar(50) CHARACTER SET utf8 NOT NULL DEFAULT '',
  `priv_disp_name` varchar(50) DEFAULT NULL,
  `action_mapping` varchar(50) CHARACTER SET utf8 DEFAULT NULL,
  `parent_priv_id` int(10) unsigned NOT NULL,
  `status` enum('ACTIVE','INACTIVE') DEFAULT NULL,
  `priv_owner` enum('BO','AGENT','RETAILER') DEFAULT NULL,
  `related_to` enum('REPORTS','GAME_MGT','INV_MGT','ORDER_MGT','PWT','MISC') DEFAULT NULL,
  `group_name` varchar(50) DEFAULT NULL,
  `is_start` enum('Y','N') DEFAULT NULL,
  `channel` enum('WEB','TERMINAL','MOBILE') NOT NULL,
  `priv_code` int(10) DEFAULT NULL,
  `hidden` enum('Y','N') DEFAULT NULL,
  `group_name_fr` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `group_name_en` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`action_id`),
  KEY `priv_title` (`priv_title`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
--rollback drop table `st_se_priviledge_rep`;


/*Table structure for table `st_se_pwt_approval_request_master` */

CREATE TABLE `st_se_pwt_approval_request_master` (
  `task_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `party_type` enum('BO','AGENT','RETAILER','PLAYER','ANONYMOUS') NOT NULL,
  `request_id` varchar(20) NOT NULL DEFAULT '',
  `party_id` int(10) unsigned DEFAULT NULL,
  `ticket_nbr` char(14) NOT NULL DEFAULT '',
  `virn_code` char(24) NOT NULL DEFAULT '',
  `game_id` int(10) unsigned NOT NULL,
  `pwt_amt` decimal(20,2) NOT NULL,
  `tax_amt` decimal(20,2) NOT NULL DEFAULT '0.00',
  `net_amt` decimal(20,2) NOT NULL,
  `req_status` enum('REQUESTED','PND_MAS','PND_PAY','PAID','NA','CANCEL') NOT NULL,
  `requester_type` enum('BO','AGENT','RETAILER') NOT NULL,
  `requested_by_user_id` int(10) unsigned NOT NULL,
  `requested_by_org_id` int(10) unsigned NOT NULL,
  `requested_to_org_id` int(10) unsigned NOT NULL,
  `requested_to_type` enum('BO','AGENT','RETAILER') NOT NULL,
  `approved_by_type` enum('BO','AGENT','RETAILER') DEFAULT NULL,
  `approved_by_user_id` int(10) unsigned DEFAULT NULL,
  `approved_by_org_id` int(10) unsigned DEFAULT NULL,
  `pay_req_for_org_type` enum('BO','AGENT','RETAILER') DEFAULT NULL,
  `pay_request_for_org_id` int(10) unsigned DEFAULT NULL,
  `request_date` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `approval_date` datetime DEFAULT NULL,
  `remarks` varchar(50) NOT NULL,
  `payment_done_by_type` enum('BO','AGENT','RETAILER') DEFAULT NULL,
  `payment_done_by` int(10) unsigned DEFAULT NULL,
  `transaction_id` bigint(20) unsigned DEFAULT NULL,
  PRIMARY KEY (`task_id`),
  UNIQUE KEY `transaction_id` (`request_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;
--rollback drop table `st_se_pwt_approval_request_master`;
/*Table structure for table `st_se_rank_master` */

CREATE TABLE `st_se_rank_master` (
  `game_id` int(10) unsigned NOT NULL,
  `rank_nbr` int(10) unsigned NOT NULL,
  `prize_amt` decimal(20,2) NOT NULL,
  `prize_level` enum('HIGH','LOW') NOT NULL,
  `total_no_of_prize` int(10) unsigned NOT NULL DEFAULT '0',
  `no_of_prize_claim` int(10) unsigned NOT NULL DEFAULT '0',
  `no_of_prize_cancel` int(10) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`game_id`,`rank_nbr`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
--rollback drop table `st_se_rank_master`;
/*Table structure for table `st_se_retailer_direct_player_pwt` */

CREATE TABLE `st_se_retailer_direct_player_pwt` (
  `task_id` int(10) unsigned NOT NULL,
  `retailer_user_id` int(10) unsigned NOT NULL,
  `retailer_org_id` int(10) unsigned NOT NULL,
  `ticket_nbr` char(14) NOT NULL,
  `virn_code` char(24) NOT NULL,
  `transaction_id` bigint(20) unsigned NOT NULL,
  `transaction_date` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `game_id` int(10) unsigned NOT NULL,
  `player_id` int(10) unsigned NOT NULL,
  `pwt_amt` decimal(20,2) NOT NULL,
  `tax_amt` decimal(20,2) NOT NULL,
  `net_amt` decimal(20,2) NOT NULL,
  `payment_type` enum('CASH','CHEQUE','TPT') NOT NULL,
  `cheque_nbr` varchar(10) CHARACTER SET utf8 DEFAULT NULL,
  `cheque_date` date DEFAULT NULL,
  `drawee_bank` varchar(50) CHARACTER SET utf8 DEFAULT NULL,
  `issuing_party_name` varchar(30) DEFAULT NULL,
  `pwt_claim_status` enum('DONE_CLM','CLAIM_BAL','DONE_UNCLM','UNCLAIM_BAL') NOT NULL,
  `claim_comm` decimal(20,2) NOT NULL,
  `agt_claim_comm` decimal(20,2) NOT NULL,
  PRIMARY KEY (`virn_code`,`game_id`),
  KEY `transaction_id` (`transaction_id`),
  KEY `player_id` (`player_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_se_retailer_direct_player_pwt`;
/*Table structure for table `st_se_retailer_pwt` */

CREATE TABLE `st_se_retailer_pwt` (
  `retailer_user_id` int(10) unsigned NOT NULL,
  `retailer_org_id` int(10) unsigned NOT NULL,
  `virn_code` char(24) NOT NULL,
  `ticket_nbr` char(14) NOT NULL,
  `game_id` int(10) unsigned NOT NULL,
  `transaction_id` bigint(20) unsigned NOT NULL,
  `pwt_amt` decimal(20,2) NOT NULL,
  `claim_comm` decimal(20,2) NOT NULL DEFAULT '0.00',
  `agt_claim_comm` decimal(20,2) NOT NULL,
  `status` enum('DONE_CLM','DONE_UNCLM','UNCLAIM_BAL','CLAIM_BAL') NOT NULL,
  PRIMARY KEY (`virn_code`,`game_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
--rollback drop table `st_se_retailer_pwt`;
/*Table structure for table `st_se_supplier_bo_transaction` */

CREATE TABLE `st_se_supplier_bo_transaction` (
  `transaction_id` bigint(20) unsigned NOT NULL,
  `game_id` int(10) unsigned NOT NULL,
  `supplier_id` int(10) unsigned NOT NULL,
  `nbr_of_books` int(10) unsigned NOT NULL,
  `mrp_amt` decimal(20,2) unsigned NOT NULL,
  KEY `transaction_id` (`transaction_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
--rollback drop table `st_se_supplier_bo_transaction`;
/*Table structure for table `st_se_supplier_master` */

CREATE TABLE `st_se_supplier_master` (
  `supplier_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(30) NOT NULL,
  `addr_line1` varchar(30) NOT NULL,
  `addr_line2` varchar(30) DEFAULT NULL,
  `city` varchar(15) NOT NULL,
  `state_code` varchar(15) NOT NULL,
  `country_code` varchar(15) NOT NULL,
  `pin_code` bigint(15) unsigned NOT NULL,
  PRIMARY KEY (`supplier_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;
--rollback drop table `st_se_supplier_master`;
/*Table structure for table `st_se_ticket_by_ticket_sale_txn` */

CREATE TABLE `st_se_ticket_by_ticket_sale_txn` (
  `txn_id` varchar(50) NOT NULL,
  `ticket_nbr` varchar(50) NOT NULL,
  `ret_org_id` int(11) NOT NULL,
  `ret_user_id` int(11) DEFAULT NULL,
  `sale_time` datetime NOT NULL,
  `tp_txn_id` varchar(20) DEFAULT NULL,
  `ticket_status` enum('SOLD','UNSOLD') NOT NULL,
  `game_id` int(10) unsigned DEFAULT NULL,
  `ticket_price` decimal(12,2) DEFAULT NULL,
  PRIMARY KEY (`txn_id`),
  UNIQUE KEY `tp_txn_id` (`tp_txn_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_se_ticket_by_ticket_sale_txn`;
/*Table structure for table `st_se_tmp_pwt_inv` */

CREATE TABLE `st_se_tmp_pwt_inv` (
  `virn_code` char(24) NOT NULL,
  `game_id` int(10) unsigned NOT NULL,
  `receipt_id` varchar(20) NOT NULL,
  `user_id` int(10) unsigned NOT NULL,
  `status` enum('OPEN','CLOSE') DEFAULT NULL,
  `date_entered` datetime DEFAULT NULL,
  `final_receipt_num` varchar(40) DEFAULT NULL,
  PRIMARY KEY (`virn_code`,`game_id`),
  KEY `FK_st_tmp_pwt_inv` (`receipt_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_se_tmp_pwt_inv`;
/*Table structure for table `st_se_tmp_pwt_receipt` */

CREATE TABLE `st_se_tmp_pwt_receipt` (
  `receipt_id` varchar(20) NOT NULL,
  `agent_org_id` int(10) unsigned NOT NULL,
  `status` enum('OPEN','CLOSE') DEFAULT NULL,
  `date_entered` datetime DEFAULT NULL,
  PRIMARY KEY (`receipt_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_se_tmp_pwt_receipt`;
/*Table structure for table `st_se_tmp_pwt_receipt_detail` */

CREATE TABLE `st_se_tmp_pwt_receipt_detail` (
  `receipt_id` varchar(20) NOT NULL,
  `game_id` int(10) unsigned NOT NULL,
  `no_of_tickets` int(10) unsigned NOT NULL,
  `prize_amt` decimal(10,2) unsigned DEFAULT NULL,
  KEY `FK_st_tmp_pwt_receipt_detail` (`receipt_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
--rollback drop table `st_se_tmp_pwt_receipt_detail`;
/*Table structure for table `st_se_tmp_pwt_receipt_mapping` */

CREATE TABLE `st_se_tmp_pwt_receipt_mapping` (
  `tmp_receipt_id` varchar(20) NOT NULL,
  `final_receipt_id` varchar(40) NOT NULL,
  KEY `FK_st_tmp_pwt_receipt_mapping` (`tmp_receipt_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_se_tmp_pwt_receipt_mapping`;
/*Table structure for table `st_se_tmp_pwt_tickets_inv` */

CREATE TABLE `st_se_tmp_pwt_tickets_inv` (
  `ticket_nbr` char(14) NOT NULL,
  `book_nbr` varchar(15) DEFAULT NULL,
  `game_id` int(10) unsigned NOT NULL,
  `receipt_id` varchar(20) NOT NULL,
  `user_id` int(10) unsigned NOT NULL,
  `status` enum('OPEN','CLOSE') DEFAULT NULL,
  `date_entered` datetime DEFAULT NULL,
  `final_receipt_num` varchar(40) DEFAULT NULL,
  PRIMARY KEY (`ticket_nbr`,`game_id`),
  KEY `FK_st_tmp_pwt_tickets_inv` (`receipt_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
--rollback drop table `st_se_tmp_pwt_tickets_inv`;
/*Table structure for table `st_se_warehouse_master` */

CREATE TABLE `st_se_warehouse_master` (
  `warehouse_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `warehouse_name` varchar(50) DEFAULT NULL,
  `warehouse_created_date` datetime DEFAULT NULL,
  `warehouse_capacity` int(11) DEFAULT NULL,
  `city_id` varchar(10) DEFAULT NULL,
  `state_id` varchar(10) DEFAULT NULL,
  `address1` varchar(50) DEFAULT NULL,
  `address2` varchar(50) DEFAULT NULL,
  `warehouse_owner_id` int(10) DEFAULT NULL,
  `warehouse_type` enum('FIXED','MOBILE') DEFAULT NULL,
  PRIMARY KEY (`warehouse_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
--rollback drop table `st_se_warehouse_master`;
/*Table structure for table `st_sle_agent_direct_plr_pwt` */

CREATE TABLE `st_sle_agent_direct_plr_pwt` (
  `agent_org_id` int(10) unsigned DEFAULT NULL,
  `agent_user_id` int(10) unsigned DEFAULT NULL,
  `game_id` int(10) unsigned NOT NULL,
  `game_type_id` int(10) DEFAULT NULL,
  `draw_id` int(10) unsigned NOT NULL,
  `pwt_inv_id` int(10) unsigned NOT NULL,
  `task_id` int(10) unsigned DEFAULT NULL,
  `transaction_id` bigint(10) unsigned NOT NULL,
  `transaction_date` datetime NOT NULL,
  `player_id` int(10) unsigned NOT NULL,
  `pwt_amt` decimal(20,2) NOT NULL,
  `tax_amt` decimal(20,2) NOT NULL,
  `net_amt` decimal(20,2) NOT NULL,
  `payment_type` enum('CASH','CHEQUE','TPT') DEFAULT NULL,
  `cheque_nbr` varchar(10) CHARACTER SET utf8 DEFAULT NULL,
  `cheque_date` date DEFAULT NULL,
  `drawee_bank` varchar(50) CHARACTER SET utf8 DEFAULT NULL,
  `issuing_party_name` varchar(50) CHARACTER SET utf8 DEFAULT NULL,
  `pwt_claim_status` enum('CLAIM_RET_UNCLM','UNCLM_PWT','UNCLM_CANCELLED','CLAIM_PLR_AGT_UNCLM_DIR','CLAIM_PLR_AGT_CLM','CLAIM_BAL','UNCLAIM_BAL','DONE_CLM') NOT NULL,
  `agt_claim_comm` decimal(20,2) NOT NULL,
  KEY `transaction_id` (`transaction_id`),
  KEY `player_id` (`player_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_sle_agent_direct_plr_pwt`;
/*Table structure for table `st_sle_agent_retailer_pwt_comm_variance_history` */

CREATE TABLE `st_sle_agent_retailer_pwt_comm_variance_history` (
  `agent_org_id` int(10) unsigned NOT NULL,
  `retailer_org_id` int(10) unsigned NOT NULL,
  `game_id` int(10) unsigned NOT NULL,
  `game_type_id` int(10) unsigned NOT NULL,
  `pwt_comm_variance` decimal(5,2) NOT NULL,
  `date_changed` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_sle_agent_retailer_pwt_comm_variance_history`;
/*Table structure for table `st_sle_agent_retailer_sale_comm_variance_history` */

CREATE TABLE `st_sle_agent_retailer_sale_comm_variance_history` (
  `agent_org_id` int(10) unsigned NOT NULL,
  `retialer_org_id` int(10) unsigned NOT NULL,
  `game_id` int(10) unsigned NOT NULL,
  `game_type_id` int(10) unsigned NOT NULL,
  `sale_comm_variance` decimal(5,2) NOT NULL,
  `date_changed` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_sle_agent_retailer_sale_comm_variance_history`;
/*Table structure for table `st_sle_agent_retailer_sale_pwt_comm_variance` */

CREATE TABLE `st_sle_agent_retailer_sale_pwt_comm_variance` (
  `agent_org_id` int(10) unsigned NOT NULL,
  `retailer_org_id` int(10) unsigned NOT NULL,
  `game_id` int(10) unsigned NOT NULL,
  `game_type_id` int(10) unsigned NOT NULL,
  `sale_comm_variance` decimal(5,2) DEFAULT NULL,
  `default_sale_comm_rate` decimal(5,2) DEFAULT NULL,
  `pwt_comm_variance` decimal(5,2) DEFAULT NULL,
  `default_pwt_comm_rate` decimal(5,2) DEFAULT NULL,
  PRIMARY KEY (`agent_org_id`,`retailer_org_id`,`game_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_sle_agent_retailer_sale_pwt_comm_variance`;
/*Table structure for table `st_sle_agt_pwt` */

CREATE TABLE `st_sle_agt_pwt` (
  `agent_user_id` int(10) unsigned NOT NULL,
  `agent_org_id` int(10) unsigned NOT NULL,
  `retailer_org_id` int(10) unsigned NOT NULL,
  `game_id` int(10) unsigned NOT NULL,
  `game_type_id` int(10) unsigned NOT NULL,
  `draw_id` int(10) unsigned NOT NULL,
  `transaction_id` bigint(20) unsigned NOT NULL,
  `pwt_amt` decimal(20,2) NOT NULL,
  `comm_amt` decimal(20,2) DEFAULT NULL,
  `govt_claim_comm` decimal(20,2) NOT NULL DEFAULT '0.00',
  `net_amt` decimal(20,2) DEFAULT NULL,
  `agt_claim_comm` decimal(20,2) NOT NULL,
  `status` enum('DONE_CLM','CLAIM_BAL','DONE_UNCLM','UNCLAIM_BAL') NOT NULL,
  PRIMARY KEY (`transaction_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_sle_agt_pwt`;
/*Table structure for table `st_sle_agt_sale` */

CREATE TABLE `st_sle_agt_sale` (
  `transaction_id` bigint(20) unsigned NOT NULL,
  `agent_org_id` int(10) unsigned NOT NULL,
  `retailer_org_id` int(10) unsigned NOT NULL,
  `game_id` int(10) unsigned NOT NULL,
  `game_type_id` int(10) unsigned NOT NULL,
  `mrp_amt` decimal(20,2) NOT NULL,
  `retailer_comm_amt` decimal(20,2) NOT NULL,
  `net_amt` decimal(20,2) NOT NULL,
  `agent_comm_amt` decimal(20,2) NOT NULL,
  `bo_ref_transaction_id` bigint(20) unsigned DEFAULT NULL,
  `claim_status` enum('DONE_CLAIM','CLAIM_BAL') NOT NULL,
  `bo_net_amt` decimal(20,2) DEFAULT NULL,
  `vat_amt` decimal(20,4) NOT NULL,
  `taxable_sale` decimal(20,4) NOT NULL,
  `govt_comm` decimal(20,4) NOT NULL,
  `transaction_date` datetime DEFAULT NULL,
  PRIMARY KEY (`transaction_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_sle_agt_sale`;
/*Table structure for table `st_sle_agt_sale_refund` */

CREATE TABLE `st_sle_agt_sale_refund` (
  `transaction_id` bigint(20) unsigned NOT NULL,
  `agent_org_id` int(10) unsigned NOT NULL,
  `retailer_org_id` int(10) unsigned NOT NULL,
  `game_id` int(10) unsigned NOT NULL,
  `game_type_id` int(10) unsigned DEFAULT NULL,
  `mrp_amt` decimal(20,2) NOT NULL,
  `retailer_comm_amt` decimal(20,2) NOT NULL,
  `net_amt` decimal(20,2) NOT NULL,
  `agent_comm_amt` decimal(20,2) NOT NULL,
  `bo_net_amt` decimal(20,2) NOT NULL,
  `claim_status` enum('DONE_CLAIM','CLAIM_BAL') NOT NULL,
  `bo_ref_transaction_id` bigint(20) unsigned DEFAULT NULL,
  `vat_amt` decimal(20,4) NOT NULL,
  `taxable_sale` decimal(20,4) NOT NULL,
  `govt_comm` decimal(20,4) NOT NULL,
  `cancellation_charges` decimal(10,2) NOT NULL DEFAULT '0.00',
  `transaction_date` datetime DEFAULT NULL,
  PRIMARY KEY (`transaction_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_sle_agt_sale_refund`;
/*Table structure for table `st_sle_approval_req_master` */

CREATE TABLE `st_sle_approval_req_master` (
  `task_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `request_id` varchar(20) CHARACTER SET utf8 NOT NULL DEFAULT '',
  `party_id` int(10) unsigned DEFAULT NULL,
  `party_type` enum('BO','AGENT','RETAILER','PLAYER','ANONYMOUS') NOT NULL,
  `ticket_nbr` bigint(20) unsigned NOT NULL,
  `game_id` int(10) unsigned NOT NULL,
  `game_type_id` int(10) DEFAULT NULL,
  `draw_id` int(10) unsigned NOT NULL,
  `panel_id` int(10) unsigned DEFAULT NULL,
  `pwt_amt` decimal(20,2) NOT NULL,
  `tax_amt` decimal(20,2) NOT NULL DEFAULT '0.00',
  `net_amt` decimal(20,2) NOT NULL,
  `request_status` enum('REQUESTED','PND_MAS','PND_PAY','PAID','NA','CANCEL') NOT NULL,
  `requested_by_user_id` int(10) unsigned NOT NULL,
  `requester_by_type` enum('BO','AGENT','RETAILER') NOT NULL,
  `requested_to_org_id` int(10) unsigned DEFAULT NULL,
  `requested_to_type` enum('BO','AGENT','RETAILER') DEFAULT NULL,
  `request_date` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `approved_by_user_id` int(10) unsigned DEFAULT NULL,
  `approved_by_type` enum('BO','AGENT','RETAILER') DEFAULT NULL,
  `approval_date` datetime DEFAULT NULL,
  `pay_req_for_user_id` int(10) unsigned DEFAULT NULL,
  `pay_req_for_type` enum('BO','AGENT','RETAILER') DEFAULT NULL,
  `remarks` varchar(100) CHARACTER SET utf8 NOT NULL DEFAULT '',
  `payment_done_by_user_id` int(10) unsigned DEFAULT NULL,
  `payment_done_by_type` enum('BO','AGENT','RETAILER') DEFAULT NULL,
  `transaction_id` bigint(10) unsigned DEFAULT NULL,
  `engine_trans_id` bigint(10) DEFAULT NULL,
  PRIMARY KEY (`task_id`),
  UNIQUE KEY `ticket_nbr` (`ticket_nbr`,`draw_id`,`panel_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
--rollback drop table `st_sle_approval_req_master`;
/*Table structure for table `st_sle_bo_agent_pwt_comm_variance_history` */

CREATE TABLE `st_sle_bo_agent_pwt_comm_variance_history` (
  `agent_org_id` int(11) unsigned NOT NULL,
  `game_id` int(11) unsigned NOT NULL,
  `game_type_id` int(10) unsigned NOT NULL,
  `pwt_comm_variance` decimal(5,2) NOT NULL,
  `date_changed` datetime NOT NULL DEFAULT '0000-00-00 00:00:00'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_sle_bo_agent_pwt_comm_variance_history`;
/*Table structure for table `st_sle_bo_agent_sale_comm_variance_history` */

CREATE TABLE `st_sle_bo_agent_sale_comm_variance_history` (
  `agent_org_id` int(11) unsigned NOT NULL,
  `game_id` int(11) unsigned NOT NULL,
  `game_type_id` int(10) unsigned NOT NULL,
  `sale_comm_variance` decimal(5,2) NOT NULL,
  `date_changed` datetime NOT NULL DEFAULT '0000-00-00 00:00:00'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_sle_bo_agent_sale_comm_variance_history`;
/*Table structure for table `st_sle_bo_agent_sale_pwt_comm_variance` */

CREATE TABLE `st_sle_bo_agent_sale_pwt_comm_variance` (
  `agent_org_id` int(10) unsigned NOT NULL,
  `game_id` int(10) unsigned NOT NULL,
  `game_type_id` int(10) unsigned NOT NULL,
  `sale_comm_variance` decimal(5,2) DEFAULT NULL,
  `default_sale_comm_rate` decimal(5,2) DEFAULT NULL,
  `pwt_comm_variance` decimal(5,2) DEFAULT NULL,
  `default_pwt_comm_rate` decimal(5,2) DEFAULT NULL,
  PRIMARY KEY (`agent_org_id`,`game_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_sle_bo_agent_sale_pwt_comm_variance`;
/*Table structure for table `st_sle_bo_direct_plr_pwt` */

CREATE TABLE `st_sle_bo_direct_plr_pwt` (
  `bo_org_id` int(10) unsigned DEFAULT NULL,
  `bo_user_id` int(10) unsigned DEFAULT NULL,
  `game_id` int(10) unsigned NOT NULL,
  `game_type_id` int(10) DEFAULT NULL,
  `draw_id` int(10) unsigned NOT NULL,
  `pwt_inv_id` int(10) unsigned NOT NULL,
  `task_id` int(10) unsigned DEFAULT NULL,
  `transaction_id` bigint(10) unsigned NOT NULL,
  `transaction_date` datetime NOT NULL,
  `player_id` int(10) unsigned NOT NULL,
  `pwt_amt` decimal(20,2) NOT NULL,
  `tax_amt` decimal(20,2) NOT NULL,
  `net_amt` decimal(20,2) NOT NULL,
  `payment_type` enum('CASH','CHEQUE','TPT') DEFAULT NULL,
  `cheque_nbr` varchar(10) CHARACTER SET utf8 DEFAULT NULL,
  `cheque_date` date DEFAULT NULL,
  `drawee_bank` varchar(50) CHARACTER SET utf8 DEFAULT NULL,
  `issuing_party_name` varchar(50) CHARACTER SET utf8 DEFAULT NULL,
  KEY `transaction_id` (`transaction_id`),
  KEY `player_id` (`player_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_sle_bo_direct_plr_pwt`;
/*Table structure for table `st_sle_bo_pwt` */

CREATE TABLE `st_sle_bo_pwt` (
  `bo_user_id` int(10) unsigned NOT NULL,
  `bo_org_id` int(10) unsigned NOT NULL,
  `agent_org_id` int(10) unsigned NOT NULL,
  `game_id` int(10) unsigned NOT NULL,
  `game_type_id` int(10) unsigned NOT NULL,
  `draw_id` int(10) unsigned NOT NULL,
  `transaction_id` bigint(20) unsigned NOT NULL,
  `pwt_amt` decimal(20,2) NOT NULL,
  `comm_amt` decimal(20,2) DEFAULT NULL,
  `govt_claim_comm` decimal(20,2) NOT NULL DEFAULT '0.00',
  `net_amt` decimal(20,2) DEFAULT NULL,
  PRIMARY KEY (`transaction_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_sle_bo_pwt`;
/*Table structure for table `st_sle_bo_sale` */

CREATE TABLE `st_sle_bo_sale` (
  `transaction_id` bigint(20) unsigned NOT NULL,
  `agent_org_id` int(10) unsigned NOT NULL,
  `game_id` int(10) unsigned NOT NULL,
  `game_type_id` int(10) unsigned NOT NULL,
  `mrp_amt` decimal(20,2) NOT NULL,
  `agent_comm` decimal(20,2) NOT NULL,
  `net_amt` decimal(20,2) NOT NULL,
  `vat_amt` decimal(20,4) NOT NULL,
  `taxable_sale` decimal(20,4) NOT NULL,
  `govt_comm` decimal(20,4) NOT NULL,
  `transaction_date` datetime DEFAULT NULL,
  PRIMARY KEY (`transaction_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_sle_bo_sale`;
/*Table structure for table `st_sle_bo_sale_refund` */

CREATE TABLE `st_sle_bo_sale_refund` (
  `transaction_id` bigint(20) unsigned NOT NULL,
  `agent_org_id` int(10) unsigned NOT NULL,
  `game_id` int(10) unsigned NOT NULL,
  `game_type_id` int(10) unsigned NOT NULL,
  `mrp_amt` decimal(20,2) unsigned NOT NULL,
  `agent_comm` decimal(20,2) NOT NULL,
  `net_amt` decimal(20,2) unsigned NOT NULL,
  `vat_amt` decimal(20,4) unsigned NOT NULL,
  `taxable_sale` decimal(20,4) unsigned NOT NULL,
  `govt_comm` decimal(20,4) unsigned NOT NULL,
  `cancellation_charges` decimal(10,2) NOT NULL DEFAULT '0.00',
  `transaction_date` datetime DEFAULT NULL,
  PRIMARY KEY (`transaction_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_sle_bo_sale_refund`;
/*Table structure for table `st_sle_game_govt_comm_history` */

CREATE TABLE `st_sle_game_govt_comm_history` (
  `game_id` int(10) unsigned NOT NULL,
  `game_type_id` int(10) unsigned NOT NULL,
  `govt_comm_rate` decimal(5,2) NOT NULL,
  `date_changed` datetime NOT NULL DEFAULT '0000-00-00 00:00:00'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_sle_game_govt_comm_history`;
/*Table structure for table `st_sle_game_master` */

CREATE TABLE `st_sle_game_master` (
  `game_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `game_no` int(11) DEFAULT NULL,
  `game_dev_name` varchar(50) DEFAULT NULL,
  `game_disp_name` varchar(100) DEFAULT NULL,
  `closing_time` datetime DEFAULT NULL,
  `display_order` int(11) DEFAULT NULL,
  `game_status` enum('SALE_OPEN','SALE_HOLD','SALE_CLOSE') DEFAULT NULL,
  PRIMARY KEY (`game_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
--rollback drop table `st_sle_game_master`;
/*Table structure for table `st_sle_game_type_master` */

CREATE TABLE `st_sle_game_type_master` (
  `game_type_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `game_id` int(11) DEFAULT NULL,
  `type_dev_name` varchar(50) DEFAULT NULL,
  `type_disp_name` varchar(100) DEFAULT NULL,
  `vat_amt` decimal(10,2) DEFAULT NULL,
  `gov_comm_rate` decimal(10,2) DEFAULT NULL,
  `retailer_sale_comm_rate` decimal(10,2) DEFAULT NULL,
  `retailer_pwt_comm_rate` decimal(10,2) DEFAULT NULL,
  `agent_sale_comm_rate` decimal(10,2) DEFAULT NULL,
  `agent_pwt_comm_rate` decimal(10,2) DEFAULT NULL,
  `govt_pwt_comm_rate` decimal(10,2) NOT NULL DEFAULT '0.00',
  `closing_time` datetime DEFAULT NULL,
  `display_order` int(11) DEFAULT NULL,
  `type_status` enum('SALE_OPEN','SALE_HOLD','SALE_CLOSE') DEFAULT NULL,
  `prize_payout_ratio` decimal(5,2) unsigned NOT NULL,
  PRIMARY KEY (`game_type_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
--rollback drop table `st_sle_game_type_master`;
/*Table structure for table `st_sle_menu_master` */

CREATE TABLE `st_sle_menu_master` (
  `menu_id` smallint(5) unsigned NOT NULL AUTO_INCREMENT,
  `action_id` smallint(5) unsigned DEFAULT NULL,
  `menu_name` varchar(50) DEFAULT NULL,
  `menu_disp_name` varchar(50) DEFAULT NULL,
  `parent_menu_id` smallint(5) unsigned DEFAULT NULL,
  `item_order` tinyint(3) unsigned DEFAULT NULL,
  `menu_disp_name_fr` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `menu_disp_name_en` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`menu_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
--rollback drop table `st_sle_menu_master`;
/*Table structure for table `st_sle_priviledge_rep` */

CREATE TABLE `st_sle_priviledge_rep` (
  `action_id` smallint(10) unsigned NOT NULL AUTO_INCREMENT,
  `priv_id` int(10) unsigned NOT NULL,
  `priv_title` varchar(50) CHARACTER SET utf8 NOT NULL DEFAULT '',
  `priv_disp_name` varchar(50) DEFAULT NULL,
  `action_mapping` varchar(50) CHARACTER SET utf8 NOT NULL,
  `parent_priv_id` int(10) unsigned DEFAULT NULL,
  `status` enum('ACTIVE','INACTIVE') DEFAULT NULL,
  `priv_owner` enum('BO','AGENT','RETAILER') DEFAULT NULL,
  `related_to` enum('GAME_MGT','PWT','PLAY_MGT','REPORTS','DRAW_MGT','MISC','SLE_MGT') DEFAULT NULL,
  `group_name` varchar(50) DEFAULT NULL,
  `is_start` enum('Y','N') DEFAULT NULL,
  `channel` enum('WEB','TERMINAL','MOBILE') NOT NULL,
  `priv_code` int(10) DEFAULT NULL,
  `hidden` enum('Y','N') DEFAULT NULL,
  `group_name_fr` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `group_name_en` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`action_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
--rollback drop table `st_sle_priviledge_rep`;

/*Table structure for table `st_sle_pwt_inv` */

CREATE TABLE `st_sle_pwt_inv` (
  `pwt_inv_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `game_id` int(11) DEFAULT NULL,
  `game_type_id` int(11) DEFAULT NULL,
  `draw_id` int(11) DEFAULT NULL,
  `ticket_nbr` bigint(20) DEFAULT NULL,
  `pwt_amt` decimal(20,2) DEFAULT NULL,
  `retailer_transaction_id` bigint(20) DEFAULT NULL,
  `agent_transaction_id` bigint(20) DEFAULT NULL,
  `bo_transaction_id` bigint(20) DEFAULT NULL,
  `engine_transaction_id` varchar(50) DEFAULT NULL,
  `claim_at` enum('BO','AGENT','RETAILER') DEFAULT NULL,
  `status` enum('CLAIM_AT_BO','CLAIM_AT_AGENT','CLAIM_AT_RETAILER','CLAIM_RET_CLM_AUTO','CLAIM_AGT_CLM_AUTO') DEFAULT NULL,
  `is_direct_plr` enum('YES','NO') DEFAULT NULL,
  PRIMARY KEY (`pwt_inv_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
--rollback drop table `st_sle_pwt_inv`;
/*Table structure for table `st_sle_ret_activity_history` */

CREATE TABLE `st_sle_ret_activity_history` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `date` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `live_retailers` int(11) DEFAULT NULL,
  `noSale_retailers` int(11) DEFAULT NULL,
  `inactive_retailers` int(11) DEFAULT NULL,
  `terminated_retailers` int(11) DEFAULT NULL,
  `total_sales` double DEFAULT NULL,
  `total_pwt` double DEFAULT NULL,
  `total_tkt_count` int(11) DEFAULT NULL,
  `total_pwt_count` int(11) DEFAULT NULL,
  `avg_sale_per_ret` double DEFAULT NULL,
  PRIMARY KEY (`id`,`date`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;
--rollback drop table `st_sle_ret_activity_history`;
/*Table structure for table `st_sle_ret_pwt` */

CREATE TABLE `st_sle_ret_pwt` (
  `transaction_id` bigint(20) unsigned NOT NULL,
  `pwt_inv_id` int(11) DEFAULT NULL,
  `game_id` int(11) DEFAULT NULL,
  `game_type_id` int(11) DEFAULT NULL,
  `draw_id` int(11) DEFAULT NULL,
  `retailer_org_id` int(11) DEFAULT NULL,
  `retailer_user_id` int(11) DEFAULT NULL,
  `pwt_amt` decimal(20,2) DEFAULT NULL,
  `retailer_claim_comm` decimal(20,2) DEFAULT NULL,
  `retailer_net_amt` decimal(20,2) DEFAULT NULL,
  `govt_claim_comm` decimal(20,2) NOT NULL DEFAULT '0.00',
  `agt_claim_comm` decimal(20,2) DEFAULT NULL,
  `agent_net_amt` decimal(20,2) DEFAULT NULL,
  `transaction_date` datetime DEFAULT NULL,
  `pwt_claim_status` enum('CLAIM_BAL','DONE_CLM') DEFAULT NULL,
  `agent_ref_reansaction_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`transaction_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_sle_ret_pwt`;
/*Table structure for table `st_sle_ret_sale` */

CREATE TABLE `st_sle_ret_sale` (
  `transaction_id` bigint(20) unsigned NOT NULL,
  `engine_tx_id` bigint(20) unsigned NOT NULL,
  `game_id` int(10) unsigned NOT NULL DEFAULT '0',
  `game_type_id` int(11) DEFAULT NULL,
  `retailer_org_id` int(11) DEFAULT NULL,
  `ticket_nbr` bigint(20) unsigned NOT NULL DEFAULT '0',
  `mrp_amt` decimal(20,2) unsigned NOT NULL,
  `retailer_comm_amt` decimal(20,2) NOT NULL,
  `retailer_net_amt` decimal(20,2) NOT NULL,
  `agent_comm_amt` decimal(20,2) NOT NULL,
  `agent_net_amt` decimal(20,2) NOT NULL,
  `good_cause_amt` decimal(20,2) NOT NULL,
  `vat_amt` decimal(20,4) NOT NULL,
  `taxable_sale` decimal(20,4) NOT NULL,
  `player_mob_number` varchar(15) DEFAULT NULL,
  `claim_status` enum('CLAIM_BAL','DONE_CLAIM') DEFAULT NULL,
  `agent_ref_transaction_id` bigint(20) DEFAULT NULL,
  `transaction_date` datetime DEFAULT NULL,
  `is_cancel` enum('Y','N') DEFAULT NULL,
  PRIMARY KEY (`transaction_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_sle_ret_sale`;
/*Table structure for table `st_sle_ret_sale_refund` */

CREATE TABLE `st_sle_ret_sale_refund` (
  `transaction_id` bigint(20) unsigned NOT NULL,
  `engine_tx_id` bigint(20) unsigned NOT NULL,
  `game_id` int(10) unsigned NOT NULL DEFAULT '0',
  `game_type_id` int(11) DEFAULT NULL,
  `retailer_org_id` int(11) DEFAULT NULL,
  `ticket_nbr` bigint(20) unsigned NOT NULL DEFAULT '0',
  `mrp_amt` decimal(20,2) unsigned NOT NULL,
  `retailer_comm_amt` decimal(20,2) NOT NULL,
  `retailer_net_amt` decimal(20,2) NOT NULL,
  `agent_comm_amt` decimal(20,2) NOT NULL,
  `agent_net_amt` decimal(20,2) NOT NULL,
  `good_cause_amt` decimal(20,2) NOT NULL,
  `vat_amt` decimal(20,4) NOT NULL,
  `taxable_sale` decimal(20,4) NOT NULL,
  `cancellation_charges` decimal(10,2) NOT NULL,
  `claim_status` enum('CLAIM_BAL','DONE_CLAIM') DEFAULT NULL,
  `agent_ref_transaction_id` bigint(20) DEFAULT NULL,
  `sale_ref_transaction_id` bigint(20) DEFAULT NULL,
  `transaction_date` datetime DEFAULT NULL,
  PRIMARY KEY (`transaction_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_sle_ret_sale_refund`;
/*Table structure for table `st_temp_update_ledger` */

CREATE TABLE `st_temp_update_ledger` (
  `auto_inc` bigint(20) NOT NULL AUTO_INCREMENT,
  `trans_id` int(11) DEFAULT NULL,
  `ret_comm` double DEFAULT NULL,
  `agt_comm` double DEFAULT NULL,
  `govt_comm` double DEFAULT NULL,
  PRIMARY KEY (`auto_inc`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--rollback drop table `st_temp_update_ledger`;
/*Table structure for table `st_vs_agent_direct_plr_pwt` */

CREATE TABLE `st_vs_agent_direct_plr_pwt` (
  `agent_org_id` int(10) unsigned DEFAULT NULL,
  `agent_user_id` int(10) unsigned DEFAULT NULL,
  `game_id` int(10) unsigned NOT NULL,
  `pwt_inv_id` int(10) unsigned NOT NULL,
  `task_id` int(10) unsigned DEFAULT NULL,
  `transaction_id` bigint(10) unsigned NOT NULL,
  `transaction_date` datetime NOT NULL,
  `player_id` int(10) unsigned NOT NULL,
  `pwt_amt` decimal(20,2) NOT NULL,
  `tax_amt` decimal(20,2) NOT NULL,
  `net_amt` decimal(20,2) NOT NULL,
  `payment_type` enum('CASH','CHEQUE','TPT') DEFAULT NULL,
  `cheque_nbr` varchar(10) DEFAULT NULL,
  `cheque_date` date DEFAULT NULL,
  `drawee_bank` varchar(50) DEFAULT NULL,
  `issuing_party_name` varchar(50) DEFAULT NULL,
  `pwt_claim_status` enum('CLAIM_RET_UNCLM','UNCLM_PWT','UNCLM_CANCELLED','CLAIM_PLR_AGT_UNCLM_DIR','CLAIM_PLR_AGT_CLM','CLAIM_BAL','UNCLAIM_BAL','DONE_CLM') NOT NULL,
  `agt_claim_comm` decimal(20,2) NOT NULL,
  KEY `player_id` (`player_id`),
  KEY `transaction_id` (`transaction_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
--rollback drop table `st_vs_agent_direct_plr_pwt`;
/*Table structure for table `st_vs_agent_retailer_pwt_comm_variance_history` */

CREATE TABLE `st_vs_agent_retailer_pwt_comm_variance_history` (
  `agent_org_id` int(10) unsigned NOT NULL,
  `retailer_org_id` int(10) unsigned NOT NULL,
  `game_id` int(10) unsigned NOT NULL,
  `pwt_comm_variance` decimal(5,2) NOT NULL,
  `date_changed` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
--rollback drop table `st_vs_agent_retailer_pwt_comm_variance_history`;
/*Table structure for table `st_vs_agent_retailer_sale_comm_variance_history` */

CREATE TABLE `st_vs_agent_retailer_sale_comm_variance_history` (
  `agent_org_id` int(10) unsigned NOT NULL,
  `retialer_org_id` int(10) unsigned NOT NULL,
  `game_id` int(10) unsigned NOT NULL,
  `sale_comm_variance` decimal(5,2) NOT NULL,
  `date_changed` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
--rollback drop table `st_vs_agent_retailer_sale_comm_variance_history`;
/*Table structure for table `st_vs_agent_retailer_sale_pwt_comm_variance` */

CREATE TABLE `st_vs_agent_retailer_sale_pwt_comm_variance` (
  `agent_org_id` int(10) unsigned NOT NULL,
  `retailer_org_id` int(10) unsigned NOT NULL,
  `game_id` int(10) unsigned NOT NULL,
  `sale_comm_variance` decimal(5,2) DEFAULT NULL,
  `default_sale_comm_rate` decimal(5,2) DEFAULT NULL,
  `pwt_comm_variance` decimal(5,2) DEFAULT NULL,
  `default_pwt_comm_rate` decimal(5,2) DEFAULT NULL,
  PRIMARY KEY (`agent_org_id`,`retailer_org_id`,`game_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
--rollback drop table `st_vs_agent_retailer_sale_pwt_comm_variance`;
/*Table structure for table `st_vs_approval_req_master` */

CREATE TABLE `st_vs_approval_req_master` (
  `task_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `request_id` varchar(20) NOT NULL,
  `party_id` int(10) unsigned DEFAULT NULL,
  `party_type` enum('BO','AGENT','RETAILER','PLAYER','ANONYMOUS') NOT NULL,
  `ticket_nbr` varchar(50) NOT NULL,
  `game_id` int(10) unsigned NOT NULL,
  `pwt_amt` decimal(20,2) NOT NULL,
  `tax_amt` decimal(20,2) NOT NULL DEFAULT '0.00',
  `net_amt` decimal(20,2) NOT NULL,
  `request_status` enum('REQUESTED','PND_MAS','PND_PAY','PAID','NA','CANCEL') NOT NULL,
  `requested_by_user_id` int(10) unsigned NOT NULL,
  `requester_by_type` enum('BO','AGENT','RETAILER') NOT NULL,
  `requested_to_org_id` int(10) unsigned DEFAULT NULL,
  `requested_to_type` enum('BO','AGENT','RETAILER') DEFAULT NULL,
  `request_date` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `approved_by_user_id` int(10) unsigned DEFAULT NULL,
  `approved_by_type` enum('BO','AGENT','RETAILER') DEFAULT NULL,
  `approval_date` datetime DEFAULT NULL,
  `pay_req_for_user_id` int(10) unsigned DEFAULT NULL,
  `pay_req_for_type` enum('BO','AGENT','RETAILER') DEFAULT NULL,
  `remarks` varchar(100) NOT NULL,
  `payment_done_by_user_id` int(10) unsigned DEFAULT NULL,
  `payment_done_by_type` enum('BO','AGENT','RETAILER') DEFAULT NULL,
  `transaction_id` bigint(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`task_id`),
  UNIQUE KEY `ticket_nbr` (`ticket_nbr`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
--rollback drop table `st_vs_approval_req_master`;
/*Table structure for table `st_vs_bo_agent_pwt_comm_variance_history` */

CREATE TABLE `st_vs_bo_agent_pwt_comm_variance_history` (
  `agent_org_id` int(11) unsigned NOT NULL,
  `game_id` int(11) unsigned NOT NULL,
  `pwt_comm_variance` decimal(5,2) NOT NULL,
  `date_changed` datetime NOT NULL DEFAULT '0000-00-00 00:00:00'
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
--rollback drop table `st_vs_bo_agent_pwt_comm_variance_history`;
/*Table structure for table `st_vs_bo_agent_sale_comm_variance_history` */

CREATE TABLE `st_vs_bo_agent_sale_comm_variance_history` (
  `agent_org_id` int(11) unsigned NOT NULL,
  `game_id` int(11) unsigned NOT NULL,
  `sale_comm_variance` decimal(5,2) NOT NULL,
  `date_changed` datetime NOT NULL DEFAULT '0000-00-00 00:00:00'
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
--rollback drop table `st_vs_bo_agent_sale_comm_variance_history`;
/*Table structure for table `st_vs_bo_agent_sale_pwt_comm_variance` */

CREATE TABLE `st_vs_bo_agent_sale_pwt_comm_variance` (
  `agent_org_id` int(10) unsigned NOT NULL,
  `game_id` int(10) unsigned NOT NULL,
  `sale_comm_variance` decimal(5,2) DEFAULT NULL,
  `default_sale_comm_rate` decimal(5,2) DEFAULT NULL,
  `pwt_comm_variance` decimal(5,2) DEFAULT NULL,
  `default_pwt_comm_rate` decimal(5,2) DEFAULT NULL,
  PRIMARY KEY (`agent_org_id`,`game_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
--rollback drop table `st_vs_bo_agent_sale_pwt_comm_variance`;
/*Table structure for table `st_vs_bo_direct_plr_pwt` */

CREATE TABLE `st_vs_bo_direct_plr_pwt` (
  `bo_org_id` int(10) unsigned DEFAULT NULL,
  `bo_user_id` int(10) unsigned DEFAULT NULL,
  `game_id` int(10) unsigned NOT NULL,
  `pwt_inv_id` int(10) unsigned NOT NULL,
  `task_id` int(10) unsigned DEFAULT NULL,
  `transaction_id` bigint(10) unsigned NOT NULL,
  `transaction_date` datetime NOT NULL,
  `player_id` int(10) unsigned NOT NULL,
  `pwt_amt` decimal(20,2) NOT NULL,
  `tax_amt` decimal(20,2) NOT NULL,
  `net_amt` decimal(20,2) NOT NULL,
  `payment_type` enum('CASH','CHEQUE','TPT') DEFAULT NULL,
  `cheque_nbr` varchar(10) DEFAULT NULL,
  `cheque_date` date DEFAULT NULL,
  `drawee_bank` varchar(50) DEFAULT NULL,
  `issuing_party_name` varchar(50) DEFAULT NULL,
  KEY `player_id` (`player_id`),
  KEY `transaction_id` (`transaction_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
--rollback drop table `st_vs_bo_direct_plr_pwt`;
/*Table structure for table `st_vs_game_govt_comm_history` */

CREATE TABLE `st_vs_game_govt_comm_history` (
  `game_id` int(10) unsigned NOT NULL,
  `govt_comm_rate` decimal(5,2) NOT NULL,
  `date_changed` datetime NOT NULL DEFAULT '0000-00-00 00:00:00'
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
--rollback drop table `st_vs_game_govt_comm_history`;
/*Table structure for table `st_vs_game_master` */

CREATE TABLE `st_vs_game_master` (
  `game_id` int(10) unsigned NOT NULL,
  `game_no` int(11) unsigned NOT NULL,
  `game_dev_name` varchar(50) DEFAULT NULL,
  `game_disp_name` varchar(100) DEFAULT NULL,
  `vat_amt` decimal(10,2) NOT NULL,
  `gov_comm_rate` decimal(10,2) NOT NULL,
  `govt_comm_pwt_rate` decimal(5,2) NOT NULL,
  `retailer_sale_comm_rate` decimal(10,2) NOT NULL,
  `retailer_pwt_comm_rate` decimal(10,2) NOT NULL,
  `agent_sale_comm_rate` decimal(10,2) NOT NULL,
  `agent_pwt_comm_rate` decimal(10,2) NOT NULL,
  `closing_time` datetime DEFAULT NULL,
  `display_order` int(11) DEFAULT NULL,
  `game_status` enum('SALE_OPEN','SALE_HOLD','SALE_CLOSE') DEFAULT NULL,
  `prize_payout_ratio` decimal(5,2) unsigned NOT NULL,
  PRIMARY KEY (`game_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
--rollback drop table `st_vs_game_master`;
/*Table structure for table `st_vs_game_ticket_info` */

CREATE TABLE `st_vs_game_ticket_info` (
  `lms_trans_id` bigint(10) unsigned NOT NULL,
  `event_id` bigint(10) unsigned NOT NULL,
  `event_type` varchar(50) NOT NULL,
  `event_start_date` varchar(50) DEFAULT NULL,
  `event_end_date` varchar(50) DEFAULT NULL,
  ` varchar(50) DEFAULT NULL,       
t_selection_info` blob
) ENGINE=MyISAM DEFAULT CHARSET=latin1 ROW_FORMAT=DYNAMIC;
--rollback drop table `st_vs_game_ticket_info`;
/*Table structure for table `st_vs_menu_master` */

CREATE TABLE `st_vs_menu_master` (
  `menu_id` smallint(5) unsigned NOT NULL AUTO_INCREMENT,
  `action_id` smallint(5) unsigned DEFAULT NULL,
  `menu_name` varchar(50) DEFAULT NULL,
  `menu_disp_name` varchar(50) DEFAULT NULL,
  `parent_menu_id` smallint(5) unsigned DEFAULT NULL,
  `item_order` tinyint(3) unsigned DEFAULT NULL,
  `menu_disp_name_fr` varchar(50) DEFAULT NULL,
  `menu_disp_name_en` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
--rollback drop table `st_vs_menu_master`;
/*Table structure for table `st_vs_priviledge_rep` */

CREATE TABLE `st_vs_priviledge_rep` (
  `action_id` smallint(10) unsigned NOT NULL AUTO_INCREMENT,
  `priv_id` int(10) unsigned NOT NULL,
  `priv_title` varchar(50) NOT NULL,
  `priv_disp_name` varchar(50) DEFAULT NULL,
  `action_mapping` varchar(50) NOT NULL,
  `parent_priv_id` int(10) unsigned DEFAULT NULL,
  `status` enum('ACTIVE','INACTIVE') DEFAULT NULL,
  `priv_owner` enum('BO','AGENT','RETAILER') DEFAULT NULL,
  `related_to` enum('VB') DEFAULT NULL,
  `group_name` varchar(50) DEFAULT NULL,
  `is_start` enum('Y','N') DEFAULT NULL,
  `channel` enum('WEB','TERMINAL','MOBILE') NOT NULL,
  `priv_code` int(10) DEFAULT NULL,
  `hidden` enum('Y','N') DEFAULT NULL,
  `group_name_fr` varchar(50) DEFAULT NULL,
  `group_name_en` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`action_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
--rollback drop table `st_vs_priviledge_rep`;
/*Table structure for table `st_vs_pwt_inv` */

CREATE TABLE `st_vs_pwt_inv` (
  `pwt_inv_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `game_id` int(11) DEFAULT NULL,
  `ticket_nbr` varchar(50) DEFAULT NULL,
  `pwt_amt` decimal(20,2) DEFAULT NULL,
  `retailer_transaction_id` bigint(20) DEFAULT NULL,
  `agent_transaction_id` bigint(20) DEFAULT NULL,
  `bo_transaction_id` bigint(20) DEFAULT NULL,
  `engine_transaction_id` varchar(50) DEFAULT NULL,
  `claim_at` enum('BO','AGENT','RETAILER') DEFAULT NULL,
  `status` enum('CLAIM_AT_BO','CLAIM_AT_AGENT','CLAIM_AT_RETAILER','CLAIM_RET_CLM_AUTO','CLAIM_AGT_CLM_AUTO') DEFAULT NULL,
  `is_direct_plr` enum('YES','NO') DEFAULT NULL,
  PRIMARY KEY (`pwt_inv_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
--rollback drop table `st_vs_pwt_inv`;
/*Table structure for table `st_vs_ret_activity_history` */

CREATE TABLE `st_vs_ret_activity_history` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `date` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `live_retailers` int(11) DEFAULT NULL,
  `noSale_retailers` int(11) DEFAULT NULL,
  `inactive_retailers` int(11) DEFAULT NULL,
  `terminated_retailers` int(11) DEFAULT NULL,
  `total_sales` double DEFAULT NULL,
  `total_pwt` double DEFAULT NULL,
  `total_tkt_count` int(11) DEFAULT NULL,
  `total_pwt_count` int(11) DEFAULT NULL,
  `avg_sale_per_ret` double DEFAULT NULL,
  PRIMARY KEY (`id`,`date`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
--rollback drop table `st_vs_ret_activity_history`;
/*Table structure for table `st_vs_ret_pwt` */

CREATE TABLE `st_vs_ret_pwt` (
  `transaction_id` bigint(20) unsigned NOT NULL,
  `pwt_inv_id` int(11) DEFAULT NULL,
  `game_id` int(11) DEFAULT NULL,
  `ticket_nbr` varchar(50) DEFAULT NULL,
  `engine_transaction_id` varchar(50) DEFAULT NULL,
  `retailer_org_id` int(11) DEFAULT NULL,
  `retailer_user_id` int(11) DEFAULT NULL,
  `pwt_amt` decimal(20,2) DEFAULT NULL,
  `retailer_claim_comm` decimal(20,2) DEFAULT NULL,
  `retailer_net_amt` decimal(20,2) DEFAULT NULL,
  `agt_claim_comm` decimal(20,2) DEFAULT NULL,
  `agent_net_amt` decimal(20,2) DEFAULT NULL,
  `govt_claim_comm` decimal(20,2) DEFAULT NULL,
  `transaction_date` datetime DEFAULT NULL,
  `pwt_claim_status` enum('CLAIM_BAL','DONE_CLM') DEFAULT NULL,
  PRIMARY KEY (`transaction_id`),
  UNIQUE KEY `ticket_nbr` (`ticket_nbr`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
--rollback drop table `st_vs_ret_pwt`;
/*Table structure for table `st_vs_ret_sale` */

CREATE TABLE `st_vs_ret_sale` (
  `transaction_id` bigint(10) unsigned NOT NULL,
  `engine_tx_id` varchar(50) NOT NULL,
  `game_id` int(10) unsigned NOT NULL DEFAULT '0',
  `retailer_org_id` int(11) DEFAULT NULL,
  `ticket_nbr` varchar(50) NOT NULL DEFAULT '0',
  `mrp_amt` decimal(20,2) unsigned NOT NULL,
  `retailer_comm_amt` decimal(20,2) NOT NULL,
  `retailer_net_amt` decimal(20,2) NOT NULL,
  `agent_comm_amt` decimal(20,2) NOT NULL,
  `agent_net_amt` decimal(20,2) NOT NULL,
  `good_cause_amt` decimal(20,2) NOT NULL,
  `vat_amt` decimal(20,4) NOT NULL,
  `taxable_sale` decimal(20,4) NOT NULL,
  `player_mob_number` varchar(15) DEFAULT NULL,
  `claim_status` enum('CLAIM_BAL','DONE_CLAIM') DEFAULT NULL,
  `transaction_date` datetime DEFAULT NULL,
  `is_cancel` enum('Y','N') DEFAULT NULL,
  `status` enum('PENDING','DONE','FAILED') DEFAULT NULL,
  PRIMARY KEY (`transaction_id`),
  UNIQUE KEY `engine_tx_id` (`engine_tx_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
--rollback drop table `st_vs_ret_sale`;
/*Table structure for table `st_vs_ret_sale_refund` */

CREATE TABLE `st_vs_ret_sale_refund` (
  `transaction_id` bigint(20) unsigned NOT NULL,
  `engine_tx_id` varchar(50) NOT NULL,
  `game_id` int(10) unsigned NOT NULL DEFAULT '0',
  `retailer_org_id` int(11) DEFAULT NULL,
  `ticket_nbr` varchar(50) NOT NULL DEFAULT '0',
  `mrp_amt` decimal(20,2) unsigned NOT NULL,
  `retailer_comm_amt` decimal(20,2) NOT NULL,
  `retailer_net_amt` decimal(20,2) NOT NULL,
  `agent_comm_amt` decimal(20,2) NOT NULL,
  `agent_net_amt` decimal(20,2) NOT NULL,
  `good_cause_amt` decimal(20,2) NOT NULL,
  `vat_amt` decimal(20,4) NOT NULL,
  `taxable_sale` decimal(20,4) NOT NULL,
  `cancellation_charges` decimal(10,2) NOT NULL,
  `claim_status` enum('CLAIM_BAL','DONE_CLAIM') DEFAULT NULL,
  `sale_ref_transaction_id` bigint(20) DEFAULT NULL,
  `transaction_date` datetime DEFAULT NULL,
  `status` enum('PENDING','DONE','FAILED') DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
--rollback drop table `st_vs_ret_sale_refund`;
/*Table structure for table `temp_arch_rec_agent` */

CREATE TABLE `temp_arch_rec_agent` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `rec_id` bigint(20) unsigned NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1 ROW_FORMAT=DYNAMIC;
--rollback drop table `temp_arch_rec_agent`;
/*Table structure for table `temp_arch_rec_bo` */

CREATE TABLE `temp_arch_rec_bo` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `rec_id` bigint(20) unsigned NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1 ROW_FORMAT=DYNAMIC;
--rollback drop table `temp_arch_rec_bo`;
/*Table structure for table `temp_arch_trans_agent` */

CREATE TABLE `temp_arch_trans_agent` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `trans_id` bigint(20) unsigned NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1 ROW_FORMAT=DYNAMIC;
--rollback drop table `temp_arch_trans_agent`;
/*Table structure for table `temp_arch_trans_bo` */

CREATE TABLE `temp_arch_trans_bo` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `trans_id` bigint(20) unsigned NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1 ROW_FORMAT=DYNAMIC;
--rollback drop table `temp_arch_trans_bo`;
/*Table structure for table `temp_arch_trans_retailer` */

CREATE TABLE `temp_arch_trans_retailer` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `trans_id` bigint(20) unsigned NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1 ROW_FORMAT=DYNAMIC;
--rollback drop table `temp_arch_trans_retailer`;
/*Table structure for table `tempdate` */

CREATE TABLE `tempdate` (
  `alldate` datetime NOT NULL DEFAULT '0000-00-00 00:00:00'
) ENGINE=InnoDB DEFAULT CHARSET=latin1 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC;
--rollback drop table `tempdate`;
/*Table structure for table `tempdate_history` */

CREATE TABLE `tempdate_history` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `last_date` date DEFAULT NULL,
  `processing_time` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=latin1 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC;
--rollback drop table `tempdate_history`;