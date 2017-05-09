--liquibase formatted sql

--changeset ClientSchemaRMS:1

DROP TABLE IF EXISTS `st_cs_refund_1`;

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
) ENGINE=InnoDB ;

/*Table structure for table `st_cs_refund_2` */

DROP TABLE IF EXISTS `st_cs_refund_2`;

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
) ENGINE=InnoDB ;

/*Table structure for table `st_cs_refund_3` */

DROP TABLE IF EXISTS `st_cs_refund_3`;

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
) ENGINE=InnoDB ;

/*Table structure for table `st_cs_refund_4` */

DROP TABLE IF EXISTS `st_cs_refund_4`;

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
) ENGINE=InnoDB ;

/*Table structure for table `st_cs_refund_5` */

DROP TABLE IF EXISTS `st_cs_refund_5`;

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
) ENGINE=InnoDB ;

/*Table structure for table `st_cs_refund_6` */

DROP TABLE IF EXISTS `st_cs_refund_6`;

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
) ENGINE=InnoDB ;

/*Table structure for table `st_cs_refund_7` */

DROP TABLE IF EXISTS `st_cs_refund_7`;

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
) ENGINE=InnoDB ;

/*Table structure for table `st_cs_refund_8` */

DROP TABLE IF EXISTS `st_cs_refund_8`;

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
) ENGINE=InnoDB ;


/*Table structure for table `st_cs_sale_1` */

DROP TABLE IF EXISTS `st_cs_sale_1`;

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
) ENGINE=InnoDB ;

/*Table structure for table `st_cs_sale_2` */

DROP TABLE IF EXISTS `st_cs_sale_2`;

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
) ENGINE=InnoDB ;

/*Table structure for table `st_cs_sale_3` */

DROP TABLE IF EXISTS `st_cs_sale_3`;

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
) ENGINE=InnoDB ;

/*Table structure for table `st_cs_sale_4` */

DROP TABLE IF EXISTS `st_cs_sale_4`;

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
) ENGINE=InnoDB ;

/*Table structure for table `st_cs_sale_5` */

DROP TABLE IF EXISTS `st_cs_sale_5`;

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
) ENGINE=InnoDB ;

/*Table structure for table `st_cs_sale_6` */

DROP TABLE IF EXISTS `st_cs_sale_6`;

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
) ENGINE=InnoDB ;

/*Table structure for table `st_cs_sale_7` */

DROP TABLE IF EXISTS `st_cs_sale_7`;

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
) ENGINE=InnoDB  ;

/*Table structure for table `st_cs_sale_8` */

DROP TABLE IF EXISTS `st_cs_sale_8`;

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
) ENGINE=InnoDB ;

/*Table structure for table `st_dg_offline_files_2` */

DROP TABLE IF EXISTS `st_dg_offline_files_2`;

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
) ENGINE=InnoDB ;

/*Table structure for table `st_dg_offline_files_3` */

DROP TABLE IF EXISTS `st_dg_offline_files_3`;

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
) ENGINE=InnoDB ;

/*Table structure for table `st_dg_offline_files_6` */

DROP TABLE IF EXISTS `st_dg_offline_files_6`;

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
) ENGINE=InnoDB ;

/*Table structure for table `st_dg_printed_tickets_1` */

DROP TABLE IF EXISTS `st_dg_printed_tickets_1`;

CREATE TABLE `st_dg_printed_tickets_1` (
  `auto_id` bigint(10) NOT NULL AUTO_INCREMENT,
  `retailer_org_id` int(10) DEFAULT NULL,
  `ticket_nbr` bigint(20) unsigned DEFAULT '0',
  `channel` enum('WEB','TERMINAL') DEFAULT NULL,
  `notification_time` datetime NOT NULL,
  `action_name` char(50) NOT NULL,
  PRIMARY KEY (`auto_id`)
) ENGINE=MyISAM  ;

/*Table structure for table `st_dg_printed_tickets_10` */

DROP TABLE IF EXISTS `st_dg_printed_tickets_10`;

CREATE TABLE `st_dg_printed_tickets_10` (
  `auto_id` bigint(10) NOT NULL AUTO_INCREMENT,
  `retailer_org_id` int(10) DEFAULT NULL,
  `ticket_nbr` bigint(20) unsigned DEFAULT '0',
  `channel` enum('WEB','TERMINAL') DEFAULT NULL,
  `notification_time` datetime NOT NULL,
  `action_name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`auto_id`)
) ENGINE=InnoDB  ;

/*Table structure for table `st_dg_printed_tickets_11` */

DROP TABLE IF EXISTS `st_dg_printed_tickets_11`;

CREATE TABLE `st_dg_printed_tickets_11` (
  `auto_id` bigint(10) NOT NULL AUTO_INCREMENT,
  `retailer_org_id` int(10) DEFAULT NULL,
  `ticket_nbr` bigint(20) unsigned DEFAULT '0',
  `channel` enum('WEB','TERMINAL') DEFAULT NULL,
  `notification_time` datetime NOT NULL,
  `action_name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`auto_id`)
) ENGINE=InnoDB ;

/*Table structure for table `st_dg_printed_tickets_12` */

DROP TABLE IF EXISTS `st_dg_printed_tickets_12`;

CREATE TABLE `st_dg_printed_tickets_12` (
  `auto_id` bigint(10) NOT NULL AUTO_INCREMENT,
  `retailer_org_id` int(10) DEFAULT NULL,
  `ticket_nbr` bigint(20) unsigned DEFAULT '0',
  `channel` enum('WEB','TERMINAL') DEFAULT NULL,
  `notification_time` datetime NOT NULL,
  `action_name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`auto_id`)
) ENGINE=InnoDB ;

/*Table structure for table `st_dg_printed_tickets_15` */

DROP TABLE IF EXISTS `st_dg_printed_tickets_15`;

CREATE TABLE `st_dg_printed_tickets_15` (
  `auto_id` bigint(10) NOT NULL AUTO_INCREMENT,
  `retailer_org_id` int(10) DEFAULT NULL,
  `ticket_nbr` bigint(20) unsigned DEFAULT '0',
  `channel` enum('WEB','TERMINAL') DEFAULT NULL,
  `notification_time` datetime NOT NULL,
  `action_name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`auto_id`)
) ENGINE=InnoDB ;

/*Table structure for table `st_dg_printed_tickets_16` */

DROP TABLE IF EXISTS `st_dg_printed_tickets_16`;

CREATE TABLE `st_dg_printed_tickets_16` (
  `auto_id` bigint(10) NOT NULL AUTO_INCREMENT,
  `retailer_org_id` int(10) DEFAULT NULL,
  `ticket_nbr` bigint(20) unsigned DEFAULT '0',
  `channel` enum('WEB','TERMINAL') DEFAULT NULL,
  `notification_time` datetime NOT NULL,
  `action_name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`auto_id`)
) ENGINE=InnoDB  ;

/*Table structure for table `st_dg_printed_tickets_17` */

DROP TABLE IF EXISTS `st_dg_printed_tickets_17`;

CREATE TABLE `st_dg_printed_tickets_17` (
  `auto_id` bigint(10) NOT NULL AUTO_INCREMENT,
  `retailer_org_id` int(10) DEFAULT NULL,
  `ticket_nbr` bigint(20) unsigned DEFAULT '0',
  `channel` enum('WEB','TERMINAL') DEFAULT NULL,
  `notification_time` datetime NOT NULL,
  `action_name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`auto_id`)
) ENGINE=InnoDB  ;

/*Table structure for table `st_dg_printed_tickets_18` */

DROP TABLE IF EXISTS `st_dg_printed_tickets_18`;

CREATE TABLE `st_dg_printed_tickets_18` (
  `auto_id` bigint(10) NOT NULL AUTO_INCREMENT,
  `retailer_org_id` int(10) DEFAULT NULL,
  `ticket_nbr` bigint(20) unsigned DEFAULT '0',
  `channel` enum('WEB','TERMINAL') DEFAULT NULL,
  `notification_time` datetime NOT NULL,
  `action_name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`auto_id`)
) ENGINE=InnoDB  ;

/*Table structure for table `st_dg_printed_tickets_19` */

DROP TABLE IF EXISTS `st_dg_printed_tickets_19`;

CREATE TABLE `st_dg_printed_tickets_19` (
  `auto_id` bigint(10) NOT NULL AUTO_INCREMENT,
  `retailer_org_id` int(10) DEFAULT NULL,
  `ticket_nbr` bigint(20) unsigned DEFAULT '0',
  `channel` enum('WEB','TERMINAL') DEFAULT NULL,
  `notification_time` datetime NOT NULL,
  `action_name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`auto_id`)
) ENGINE=InnoDB  ;

/*Table structure for table `st_dg_printed_tickets_20` */

DROP TABLE IF EXISTS `st_dg_printed_tickets_20`;

CREATE TABLE `st_dg_printed_tickets_20` (
  `auto_id` bigint(10) NOT NULL AUTO_INCREMENT,
  `retailer_org_id` int(10) DEFAULT NULL,
  `ticket_nbr` bigint(20) unsigned DEFAULT '0',
  `channel` enum('WEB','TERMINAL') DEFAULT NULL,
  `notification_time` datetime NOT NULL,
  `action_name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`auto_id`)
) ENGINE=InnoDB  ;

/*Table structure for table `st_dg_printed_tickets_3` */

DROP TABLE IF EXISTS `st_dg_printed_tickets_3`;

CREATE TABLE `st_dg_printed_tickets_3` (
  `auto_id` bigint(10) NOT NULL AUTO_INCREMENT,
  `retailer_org_id` int(10) DEFAULT NULL,
  `ticket_nbr` bigint(20) unsigned DEFAULT '0',
  `channel` enum('WEB','TERMINAL') DEFAULT NULL,
  `notification_time` datetime NOT NULL,
  `action_name` char(50) NOT NULL,
  PRIMARY KEY (`auto_id`)
) ENGINE=MyISAM ;

/*Table structure for table `st_dg_printed_tickets_4` */

DROP TABLE IF EXISTS `st_dg_printed_tickets_4`;

CREATE TABLE `st_dg_printed_tickets_4` (
  `auto_id` bigint(10) NOT NULL AUTO_INCREMENT,
  `retailer_org_id` int(10) DEFAULT NULL,
  `ticket_nbr` bigint(20) unsigned DEFAULT '0',
  `channel` enum('WEB','TERMINAL') DEFAULT NULL,
  `notification_time` datetime NOT NULL,
  `action_name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`auto_id`)
) ENGINE=InnoDB ;

/*Table structure for table `st_dg_printed_tickets_5` */

DROP TABLE IF EXISTS `st_dg_printed_tickets_5`;

CREATE TABLE `st_dg_printed_tickets_5` (
  `auto_id` bigint(10) NOT NULL AUTO_INCREMENT,
  `retailer_org_id` int(10) DEFAULT NULL,
  `ticket_nbr` bigint(20) unsigned DEFAULT '0',
  `channel` enum('WEB','TERMINAL') DEFAULT NULL,
  `notification_time` datetime NOT NULL,
  `action_name` char(50) NOT NULL,
  PRIMARY KEY (`auto_id`)
) ENGINE=MyISAM  ;

/*Table structure for table `st_dg_printed_tickets_6` */

DROP TABLE IF EXISTS `st_dg_printed_tickets_6`;

CREATE TABLE `st_dg_printed_tickets_6` (
  `auto_id` bigint(10) NOT NULL AUTO_INCREMENT,
  `retailer_org_id` int(10) DEFAULT NULL,
  `ticket_nbr` bigint(20) unsigned DEFAULT '0',
  `channel` enum('WEB','TERMINAL') DEFAULT NULL,
  `notification_time` datetime NOT NULL,
  `action_name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`auto_id`)
) ENGINE=InnoDB ;

/*Table structure for table `st_dg_printed_tickets_7` */

DROP TABLE IF EXISTS `st_dg_printed_tickets_7`;

CREATE TABLE `st_dg_printed_tickets_7` (
  `auto_id` bigint(10) NOT NULL AUTO_INCREMENT,
  `retailer_org_id` int(10) DEFAULT NULL,
  `ticket_nbr` bigint(20) unsigned DEFAULT '0',
  `channel` enum('WEB','TERMINAL') DEFAULT NULL,
  `notification_time` datetime NOT NULL,
  `action_name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`auto_id`)
) ENGINE=InnoDB ;

/*Table structure for table `st_dg_printed_tickets_8` */

DROP TABLE IF EXISTS `st_dg_printed_tickets_8`;

CREATE TABLE `st_dg_printed_tickets_8` (
  `auto_id` bigint(10) NOT NULL AUTO_INCREMENT,
  `retailer_org_id` int(10) DEFAULT NULL,
  `ticket_nbr` bigint(20) unsigned DEFAULT '0',
  `channel` enum('WEB','TERMINAL') DEFAULT NULL,
  `notification_time` datetime NOT NULL,
  `action_name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`auto_id`)
) ENGINE=InnoDB ;

/*Table structure for table `st_dg_printed_tickets_9` */

DROP TABLE IF EXISTS `st_dg_printed_tickets_9`;

CREATE TABLE `st_dg_printed_tickets_9` (
  `auto_id` bigint(10) NOT NULL AUTO_INCREMENT,
  `retailer_org_id` int(10) DEFAULT NULL,
  `ticket_nbr` bigint(20) unsigned DEFAULT '0',
  `channel` enum('WEB','TERMINAL') DEFAULT NULL,
  `notification_time` datetime NOT NULL,
  `action_name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`auto_id`)
) ENGINE=InnoDB ;

/*Table structure for table `st_dg_pwt_inv_1` */

DROP TABLE IF EXISTS `st_dg_pwt_inv_1`;

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
) ENGINE=InnoDB ;

/*Table structure for table `st_dg_pwt_inv_10` */

DROP TABLE IF EXISTS `st_dg_pwt_inv_10`;

CREATE TABLE `st_dg_pwt_inv_10` (
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
) ENGINE=InnoDB ;

/*Table structure for table `st_dg_pwt_inv_11` */

DROP TABLE IF EXISTS `st_dg_pwt_inv_11`;

CREATE TABLE `st_dg_pwt_inv_11` (
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
) ENGINE=InnoDB ;

/*Table structure for table `st_dg_pwt_inv_12` */

DROP TABLE IF EXISTS `st_dg_pwt_inv_12`;

CREATE TABLE `st_dg_pwt_inv_12` (
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
) ENGINE=InnoDB ;

/*Table structure for table `st_dg_pwt_inv_15` */

DROP TABLE IF EXISTS `st_dg_pwt_inv_15`;

CREATE TABLE `st_dg_pwt_inv_15` (
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
) ENGINE=InnoDB ;

/*Table structure for table `st_dg_pwt_inv_16` */

DROP TABLE IF EXISTS `st_dg_pwt_inv_16`;

CREATE TABLE `st_dg_pwt_inv_16` (
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
) ENGINE=InnoDB ;

/*Table structure for table `st_dg_pwt_inv_17` */

DROP TABLE IF EXISTS `st_dg_pwt_inv_17`;

CREATE TABLE `st_dg_pwt_inv_17` (
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
) ENGINE=InnoDB ;

/*Table structure for table `st_dg_pwt_inv_18` */

DROP TABLE IF EXISTS `st_dg_pwt_inv_18`;

CREATE TABLE `st_dg_pwt_inv_18` (
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
) ENGINE=InnoDB ;

/*Table structure for table `st_dg_pwt_inv_19` */

DROP TABLE IF EXISTS `st_dg_pwt_inv_19`;

CREATE TABLE `st_dg_pwt_inv_19` (
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
) ENGINE=InnoDB ;

/*Table structure for table `st_dg_pwt_inv_2` */

DROP TABLE IF EXISTS `st_dg_pwt_inv_2`;

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
) ENGINE=InnoDB ;

/*Table structure for table `st_dg_pwt_inv_20` */

DROP TABLE IF EXISTS `st_dg_pwt_inv_20`;

CREATE TABLE `st_dg_pwt_inv_20` (
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
) ENGINE=InnoDB ;

/*Table structure for table `st_dg_pwt_inv_3` */

DROP TABLE IF EXISTS `st_dg_pwt_inv_3`;

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
  PRIMARY KEY (`ticket_nbr`,`draw_id`,`panel_id`)
) ENGINE=InnoDB ;

/*Table structure for table `st_dg_pwt_inv_4` */

DROP TABLE IF EXISTS `st_dg_pwt_inv_4`;

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
  PRIMARY KEY (`ticket_nbr`,`draw_id`,`panel_id`)
) ENGINE=InnoDB ;

/*Table structure for table `st_dg_pwt_inv_5` */

DROP TABLE IF EXISTS `st_dg_pwt_inv_5`;

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
) ENGINE=InnoDB ;

/*Table structure for table `st_dg_pwt_inv_6` */

DROP TABLE IF EXISTS `st_dg_pwt_inv_6`;

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
) ENGINE=InnoDB ;

/*Table structure for table `st_dg_pwt_inv_7` */

DROP TABLE IF EXISTS `st_dg_pwt_inv_7`;

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
  PRIMARY KEY (`ticket_nbr`,`draw_id`,`panel_id`)
) ENGINE=InnoDB ;

/*Table structure for table `st_dg_pwt_inv_8` */

DROP TABLE IF EXISTS `st_dg_pwt_inv_8`;

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
  `merchant_code` varchar(20) DEFAULT NULL,
  `merchant_trans_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ticket_nbr`,`draw_id`,`panel_id`)
) ENGINE=InnoDB ;

/*Table structure for table `st_dg_pwt_inv_9` */

DROP TABLE IF EXISTS `st_dg_pwt_inv_9`;

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
  PRIMARY KEY (`ticket_nbr`,`draw_id`,`panel_id`)
) ENGINE=InnoDB ;

/*Table structure for table `st_dg_ret_pwt_1` */

DROP TABLE IF EXISTS `st_dg_ret_pwt_1`;

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
) ENGINE=InnoDB ;

/*Table structure for table `st_dg_ret_pwt_10` */

DROP TABLE IF EXISTS `st_dg_ret_pwt_10`;

CREATE TABLE `st_dg_ret_pwt_10` (
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
) ENGINE=InnoDB ;

/*Table structure for table `st_dg_ret_pwt_11` */

DROP TABLE IF EXISTS `st_dg_ret_pwt_11`;

CREATE TABLE `st_dg_ret_pwt_11` (
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
) ENGINE=InnoDB ;

/*Table structure for table `st_dg_ret_pwt_12` */

DROP TABLE IF EXISTS `st_dg_ret_pwt_12`;

CREATE TABLE `st_dg_ret_pwt_12` (
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
) ENGINE=InnoDB ;

/*Table structure for table `st_dg_ret_pwt_15` */

DROP TABLE IF EXISTS `st_dg_ret_pwt_15`;

CREATE TABLE `st_dg_ret_pwt_15` (
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
) ENGINE=InnoDB ;

/*Table structure for table `st_dg_ret_pwt_16` */

DROP TABLE IF EXISTS `st_dg_ret_pwt_16`;

CREATE TABLE `st_dg_ret_pwt_16` (
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
) ENGINE=InnoDB ;

/*Table structure for table `st_dg_ret_pwt_17` */

DROP TABLE IF EXISTS `st_dg_ret_pwt_17`;

CREATE TABLE `st_dg_ret_pwt_17` (
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
) ENGINE=InnoDB ;

/*Table structure for table `st_dg_ret_pwt_18` */

DROP TABLE IF EXISTS `st_dg_ret_pwt_18`;

CREATE TABLE `st_dg_ret_pwt_18` (
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
) ENGINE=InnoDB ;

/*Table structure for table `st_dg_ret_pwt_19` */

DROP TABLE IF EXISTS `st_dg_ret_pwt_19`;

CREATE TABLE `st_dg_ret_pwt_19` (
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
) ENGINE=InnoDB ;

/*Table structure for table `st_dg_ret_pwt_2` */

DROP TABLE IF EXISTS `st_dg_ret_pwt_2`;

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
  `status` enum('CLAIM_BAL','UNCLAIM_BAL','DONE_CLM') NOT NULL
) ENGINE=InnoDB ;

/*Table structure for table `st_dg_ret_pwt_20` */

DROP TABLE IF EXISTS `st_dg_ret_pwt_20`;

CREATE TABLE `st_dg_ret_pwt_20` (
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
) ENGINE=InnoDB ;

/*Table structure for table `st_dg_ret_pwt_3` */

DROP TABLE IF EXISTS `st_dg_ret_pwt_3`;

CREATE TABLE `st_dg_ret_pwt_3` (
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
) ENGINE=InnoDB ;

/*Table structure for table `st_dg_ret_pwt_4` */

DROP TABLE IF EXISTS `st_dg_ret_pwt_4`;

CREATE TABLE `st_dg_ret_pwt_4` (
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
) ENGINE=InnoDB ;

/*Table structure for table `st_dg_ret_pwt_5` */

DROP TABLE IF EXISTS `st_dg_ret_pwt_5`;

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
) ENGINE=InnoDB ;

/*Table structure for table `st_dg_ret_pwt_6` */

DROP TABLE IF EXISTS `st_dg_ret_pwt_6`;

CREATE TABLE `st_dg_ret_pwt_6` (
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
) ENGINE=InnoDB ;

/*Table structure for table `st_dg_ret_pwt_7` */

DROP TABLE IF EXISTS `st_dg_ret_pwt_7`;

CREATE TABLE `st_dg_ret_pwt_7` (
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
) ENGINE=InnoDB ;

/*Table structure for table `st_dg_ret_pwt_8` */

DROP TABLE IF EXISTS `st_dg_ret_pwt_8`;

CREATE TABLE `st_dg_ret_pwt_8` (
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
) ENGINE=InnoDB ;

/*Table structure for table `st_dg_ret_pwt_9` */

DROP TABLE IF EXISTS `st_dg_ret_pwt_9`;

CREATE TABLE `st_dg_ret_pwt_9` (
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
) ENGINE=InnoDB ;

/*Table structure for table `st_dg_ret_sale_1` */

DROP TABLE IF EXISTS `st_dg_ret_sale_1`;

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
) ENGINE=InnoDB ;

/*Table structure for table `st_dg_ret_sale_10` */

DROP TABLE IF EXISTS `st_dg_ret_sale_10`;

CREATE TABLE `st_dg_ret_sale_10` (
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
) ENGINE=InnoDB ;

/*Table structure for table `st_dg_ret_sale_11` */

DROP TABLE IF EXISTS `st_dg_ret_sale_11`;

CREATE TABLE `st_dg_ret_sale_11` (
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
) ENGINE=InnoDB ;

/*Table structure for table `st_dg_ret_sale_12` */

DROP TABLE IF EXISTS `st_dg_ret_sale_12`;

CREATE TABLE `st_dg_ret_sale_12` (
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
) ENGINE=InnoDB ;

/*Table structure for table `st_dg_ret_sale_15` */

DROP TABLE IF EXISTS `st_dg_ret_sale_15`;

CREATE TABLE `st_dg_ret_sale_15` (
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
  PRIMARY KEY (`transaction_id`)
) ENGINE=InnoDB ;

/*Table structure for table `st_dg_ret_sale_16` */

DROP TABLE IF EXISTS `st_dg_ret_sale_16`;

CREATE TABLE `st_dg_ret_sale_16` (
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
) ENGINE=InnoDB ;

/*Table structure for table `st_dg_ret_sale_17` */

DROP TABLE IF EXISTS `st_dg_ret_sale_17`;

CREATE TABLE `st_dg_ret_sale_17` (
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
  PRIMARY KEY (`transaction_id`)
) ENGINE=InnoDB ;

/*Table structure for table `st_dg_ret_sale_18` */

DROP TABLE IF EXISTS `st_dg_ret_sale_18`;

CREATE TABLE `st_dg_ret_sale_18` (
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
) ENGINE=InnoDB ;

/*Table structure for table `st_dg_ret_sale_19` */

DROP TABLE IF EXISTS `st_dg_ret_sale_19`;

CREATE TABLE `st_dg_ret_sale_19` (
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
) ENGINE=InnoDB ;

/*Table structure for table `st_dg_ret_sale_2` */

DROP TABLE IF EXISTS `st_dg_ret_sale_2`;

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
  `good_cause_amt` decimal(10,2) NOT NULL,
  `agent_ref_transaction_id` bigint(20) unsigned DEFAULT NULL,
  `vat_amt` decimal(20,2) NOT NULL,
  `taxable_sale` decimal(20,2) NOT NULL,
  `player_mob_number` varchar(15) DEFAULT NULL,
  `ret_sd_amt` decimal(20,4) NOT NULL COMMENT 'Security deposit amount of retailer',
  `agt_vat_amt` decimal(20,4) NOT NULL
) ENGINE=InnoDB ;

/*Table structure for table `st_dg_ret_sale_20` */

DROP TABLE IF EXISTS `st_dg_ret_sale_20`;

CREATE TABLE `st_dg_ret_sale_20` (
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
) ENGINE=InnoDB ;

/*Table structure for table `st_dg_ret_sale_3` */

DROP TABLE IF EXISTS `st_dg_ret_sale_3`;

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
  `good_cause_amt` decimal(10,2) NOT NULL,
  `agent_ref_transaction_id` bigint(20) unsigned DEFAULT NULL,
  `vat_amt` decimal(20,2) NOT NULL,
  `taxable_sale` decimal(20,2) NOT NULL,
  `player_mob_number` varchar(15) DEFAULT NULL,
  `ret_sd_amt` decimal(20,4) NOT NULL COMMENT 'Security deposit amount of retailer',
  `agt_vat_amt` decimal(20,4) NOT NULL,
  PRIMARY KEY (`transaction_id`),
  KEY `ticket_nbr` (`ticket_nbr`)
) ENGINE=InnoDB ;

/*Table structure for table `st_dg_ret_sale_4` */

DROP TABLE IF EXISTS `st_dg_ret_sale_4`;

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
  `good_cause_amt` decimal(10,2) NOT NULL,
  `agent_ref_transaction_id` bigint(20) unsigned DEFAULT NULL,
  `vat_amt` decimal(20,4) NOT NULL,
  `taxable_sale` decimal(20,2) NOT NULL,
  `player_mob_number` varchar(15) DEFAULT NULL,
  `ret_sd_amt` decimal(20,4) NOT NULL COMMENT 'Security deposit amount of retailer',
  `agt_vat_amt` decimal(20,4) NOT NULL,
  PRIMARY KEY (`transaction_id`)
) ENGINE=InnoDB ;

/*Table structure for table `st_dg_ret_sale_5` */

DROP TABLE IF EXISTS `st_dg_ret_sale_5`;

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
) ENGINE=InnoDB ;

/*Table structure for table `st_dg_ret_sale_6` */

DROP TABLE IF EXISTS `st_dg_ret_sale_6`;

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
  PRIMARY KEY (`transaction_id`)
) ENGINE=InnoDB ;

/*Table structure for table `st_dg_ret_sale_7` */

DROP TABLE IF EXISTS `st_dg_ret_sale_7`;

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
  `good_cause_amt` decimal(10,2) NOT NULL,
  `agent_ref_transaction_id` bigint(20) unsigned DEFAULT NULL,
  `vat_amt` decimal(20,4) NOT NULL,
  `taxable_sale` decimal(20,2) NOT NULL,
  `player_mob_number` varchar(15) DEFAULT NULL,
  `ret_sd_amt` decimal(20,4) NOT NULL COMMENT 'Security deposit amount of retailer',
  `agt_vat_amt` decimal(20,4) NOT NULL,
  PRIMARY KEY (`transaction_id`)
) ENGINE=InnoDB ;

/*Table structure for table `st_dg_ret_sale_8` */

DROP TABLE IF EXISTS `st_dg_ret_sale_8`;

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
  `good_cause_amt` decimal(10,2) NOT NULL,
  `agent_ref_transaction_id` bigint(20) unsigned DEFAULT NULL,
  `vat_amt` decimal(20,2) NOT NULL,
  `taxable_sale` decimal(20,2) NOT NULL,
  `player_mob_number` varchar(15) DEFAULT NULL,
  `ret_sd_amt` decimal(20,4) NOT NULL COMMENT 'Security deposit amount of retailer',
  `agt_vat_amt` decimal(20,4) NOT NULL,
  PRIMARY KEY (`transaction_id`),
  KEY `ticket_nbr` (`ticket_nbr`)
) ENGINE=InnoDB ;

/*Table structure for table `st_dg_ret_sale_9` */

DROP TABLE IF EXISTS `st_dg_ret_sale_9`;

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
  `good_cause_amt` decimal(10,2) NOT NULL,
  `agent_ref_transaction_id` bigint(20) unsigned DEFAULT NULL,
  `vat_amt` decimal(20,4) NOT NULL,
  `taxable_sale` decimal(20,2) NOT NULL,
  `player_mob_number` varchar(15) DEFAULT NULL,
  `ret_sd_amt` decimal(20,4) NOT NULL COMMENT 'Security deposit amount of retailer',
  `agt_vat_amt` decimal(20,4) NOT NULL,
  PRIMARY KEY (`transaction_id`)
) ENGINE=InnoDB ;

/*Table structure for table `st_dg_ret_sale_refund_1` */

DROP TABLE IF EXISTS `st_dg_ret_sale_refund_1`;

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
) ENGINE=InnoDB ;

/*Table structure for table `st_dg_ret_sale_refund_10` */

DROP TABLE IF EXISTS `st_dg_ret_sale_refund_10`;

CREATE TABLE `st_dg_ret_sale_refund_10` (
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
  KEY `ticket_nbr` (`ticket_nbr`)
) ENGINE=InnoDB ;

/*Table structure for table `st_dg_ret_sale_refund_11` */

DROP TABLE IF EXISTS `st_dg_ret_sale_refund_11`;

CREATE TABLE `st_dg_ret_sale_refund_11` (
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
) ENGINE=InnoDB ;

/*Table structure for table `st_dg_ret_sale_refund_12` */

DROP TABLE IF EXISTS `st_dg_ret_sale_refund_12`;

CREATE TABLE `st_dg_ret_sale_refund_12` (
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
) ENGINE=InnoDB ;

/*Table structure for table `st_dg_ret_sale_refund_15` */

DROP TABLE IF EXISTS `st_dg_ret_sale_refund_15`;

CREATE TABLE `st_dg_ret_sale_refund_15` (
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
  `ret_sd_amt` decimal(20,4) DEFAULT NULL,
  `agt_vat_amt` decimal(20,4) DEFAULT NULL,
  PRIMARY KEY (`transaction_id`),
  UNIQUE KEY `ref_transaction_id` (`ref_transaction_id`),
  KEY `ticket_nbr` (`ticket_nbr`)
) ENGINE=InnoDB ;

/*Table structure for table `st_dg_ret_sale_refund_16` */

DROP TABLE IF EXISTS `st_dg_ret_sale_refund_16`;

CREATE TABLE `st_dg_ret_sale_refund_16` (
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
) ENGINE=InnoDB ;

/*Table structure for table `st_dg_ret_sale_refund_17` */

DROP TABLE IF EXISTS `st_dg_ret_sale_refund_17`;

CREATE TABLE `st_dg_ret_sale_refund_17` (
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
) ENGINE=InnoDB ;

/*Table structure for table `st_dg_ret_sale_refund_18` */

DROP TABLE IF EXISTS `st_dg_ret_sale_refund_18`;

CREATE TABLE `st_dg_ret_sale_refund_18` (
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
) ENGINE=InnoDB ;

/*Table structure for table `st_dg_ret_sale_refund_19` */

DROP TABLE IF EXISTS `st_dg_ret_sale_refund_19`;

CREATE TABLE `st_dg_ret_sale_refund_19` (
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
) ENGINE=InnoDB ;

/*Table structure for table `st_dg_ret_sale_refund_2` */

DROP TABLE IF EXISTS `st_dg_ret_sale_refund_2`;

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
  `good_cause_amt` decimal(10,2) NOT NULL,
  `agent_ref_transaction_id` bigint(20) unsigned DEFAULT NULL,
  `vat_amt` decimal(20,2) NOT NULL,
  `taxable_sale` decimal(20,2) NOT NULL,
  `cancellation_charges` decimal(10,2) DEFAULT '0.00',
  `ref_transaction_id` bigint(20) DEFAULT NULL,
  `ret_sd_amt` decimal(20,4) NOT NULL COMMENT 'Security deposit amount of retailer',
  `agt_vat_amt` decimal(20,4) NOT NULL
) ENGINE=InnoDB ;

/*Table structure for table `st_dg_ret_sale_refund_20` */

DROP TABLE IF EXISTS `st_dg_ret_sale_refund_20`;

CREATE TABLE `st_dg_ret_sale_refund_20` (
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
) ENGINE=InnoDB ;

/*Table structure for table `st_dg_ret_sale_refund_3` */

DROP TABLE IF EXISTS `st_dg_ret_sale_refund_3`;

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
  `good_cause_amt` decimal(10,2) NOT NULL,
  `agent_ref_transaction_id` bigint(20) unsigned DEFAULT NULL,
  `vat_amt` decimal(20,2) NOT NULL,
  `taxable_sale` decimal(20,2) NOT NULL,
  `cancellation_charges` decimal(10,2) DEFAULT '0.00',
  `ref_transaction_id` bigint(20) DEFAULT '0',
  `ret_sd_amt` decimal(20,4) NOT NULL COMMENT 'Security deposit amount of retailer',
  `agt_vat_amt` decimal(20,4) NOT NULL,
  PRIMARY KEY (`transaction_id`),
  KEY `ticket_nbr` (`ticket_nbr`),
  KEY `ref_transaction_id` (`ref_transaction_id`)
) ENGINE=InnoDB ;

/*Table structure for table `st_dg_ret_sale_refund_4` */

DROP TABLE IF EXISTS `st_dg_ret_sale_refund_4`;

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
  `good_cause_amt` decimal(10,2) NOT NULL,
  `agent_ref_transaction_id` bigint(20) unsigned DEFAULT NULL,
  `vat_amt` decimal(20,2) NOT NULL,
  `taxable_sale` decimal(20,2) NOT NULL,
  `cancellation_charges` decimal(10,2) DEFAULT '0.00',
  `ref_transaction_id` bigint(20) DEFAULT '0',
  `ret_sd_amt` decimal(20,4) NOT NULL COMMENT 'Security deposit amount of retailer',
  `agt_vat_amt` decimal(20,4) NOT NULL,
  PRIMARY KEY (`transaction_id`)
) ENGINE=InnoDB ;

/*Table structure for table `st_dg_ret_sale_refund_5` */

DROP TABLE IF EXISTS `st_dg_ret_sale_refund_5`;

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
  `good_cause_amt` decimal(10,2) NOT NULL,
  `agent_ref_transaction_id` bigint(20) unsigned DEFAULT NULL,
  `vat_amt` decimal(20,2) NOT NULL,
  `taxable_sale` decimal(20,2) NOT NULL,
  `cancellation_charges` decimal(10,2) DEFAULT '0.00',
  `ref_transaction_id` bigint(20) DEFAULT '0',
  `ret_sd_amt` decimal(20,4) NOT NULL COMMENT 'Security deposit amount of retailer',
  `agt_vat_amt` decimal(20,4) NOT NULL,
  PRIMARY KEY (`transaction_id`),
  KEY `ticket_nbr` (`ticket_nbr`),
  KEY `ref_transaction_id` (`ref_transaction_id`)
) ENGINE=InnoDB ;

/*Table structure for table `st_dg_ret_sale_refund_6` */

DROP TABLE IF EXISTS `st_dg_ret_sale_refund_6`;

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
) ENGINE=InnoDB ;

/*Table structure for table `st_dg_ret_sale_refund_7` */

DROP TABLE IF EXISTS `st_dg_ret_sale_refund_7`;

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
  `good_cause_amt` decimal(10,2) NOT NULL,
  `agent_ref_transaction_id` bigint(20) unsigned DEFAULT NULL,
  `vat_amt` decimal(20,2) NOT NULL,
  `taxable_sale` decimal(20,2) NOT NULL,
  `cancellation_charges` decimal(10,2) DEFAULT '0.00',
  `ref_transaction_id` bigint(20) DEFAULT '0',
  `ret_sd_amt` decimal(20,4) NOT NULL COMMENT 'Security deposit amount of retailer',
  `agt_vat_amt` decimal(20,4) NOT NULL,
  PRIMARY KEY (`transaction_id`)
) ENGINE=InnoDB ;

/*Table structure for table `st_dg_ret_sale_refund_8` */

DROP TABLE IF EXISTS `st_dg_ret_sale_refund_8`;

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
  `good_cause_amt` decimal(10,2) NOT NULL,
  `agent_ref_transaction_id` bigint(20) unsigned DEFAULT NULL,
  `vat_amt` decimal(20,2) NOT NULL,
  `taxable_sale` decimal(20,2) NOT NULL,
  `cancellation_charges` decimal(10,2) DEFAULT '0.00',
  `ref_transaction_id` bigint(20) DEFAULT '0',
  `ret_sd_amt` decimal(20,4) NOT NULL COMMENT 'Security deposit amount of retailer',
  `agt_vat_amt` decimal(20,4) NOT NULL,
  PRIMARY KEY (`transaction_id`),
  KEY `ticket_nbr` (`ticket_nbr`)
) ENGINE=InnoDB ;

/*Table structure for table `st_dg_ret_sale_refund_9` */

DROP TABLE IF EXISTS `st_dg_ret_sale_refund_9`;

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
  `good_cause_amt` decimal(10,2) NOT NULL,
  `agent_ref_transaction_id` bigint(20) unsigned DEFAULT NULL,
  `vat_amt` decimal(20,2) NOT NULL,
  `taxable_sale` decimal(20,2) NOT NULL,
  `cancellation_charges` decimal(10,2) DEFAULT '0.00',
  `ref_transaction_id` bigint(20) DEFAULT '0',
  `ret_sd_amt` decimal(20,4) NOT NULL COMMENT 'Security deposit amount of retailer',
  `agt_vat_amt` decimal(20,4) NOT NULL,
  PRIMARY KEY (`transaction_id`)
) ENGINE=InnoDB ;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
