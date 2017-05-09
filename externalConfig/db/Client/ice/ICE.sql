insert into st_dg_game_master 
	(   game_id, 
	    game_nbr, game_name, game_name_dev, 
		agent_sale_comm_rate, 
		agent_pwt_comm_rate, 
		retailer_sale_comm_rate, 
		retailer_pwt_comm_rate, 
		vat_amt, 
		govt_comm, 
		govt_comm_pwt, 
		high_prize_amt, 
		prize_payout_ratio, 
		game_status, 
		offline_freeze_time, 
		is_offline, 
		raffle_ticket_type, 
		closing_time, 
		display_order, 
		is_sale_allowed_through_terminal, 
		bonus_ball_enable
	)
	values
	( 21, 
	  7, 
	  "Bingo", 
	  "BingoSeventyFive", 
	  0.00, 
	  0.00, 
	  0.00, 
	  0.00, 
	  0.00, 
	  0.00, 
	  0.00, 
	  10000.00, 
	  50.0, 
	  "OPEN", 
	  30, 
	  "Y", 
	  'REFERENCE', 
	  'NULL', 
	  10, 
	  'Y', 
	  'N'
	);
	
	
	
	CREATE TABLE `st_dg_pwt_inv_21` (                                                                                                                                                                                                                                                                                                                                                                                                                   
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
	   PRIMARY KEY (`ticket_nbr`,`draw_id`,`panel_id`);                                                                                                                                                                                                                                                                                                                                                                                                  
	)
	
	
	 CREATE TABLE `st_dg_ret_pwt_21` (                                 
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
	);
				 
				 
	CREATE TABLE `st_dg_ret_sale_21` (                                                    
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
    );


    CREATE TABLE `st_dg_ret_sale_refund_21` (                                             
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
    );

    CREATE TABLE `st_dg_printed_tickets_21` (                 
		`auto_id` bigint(10) NOT NULL AUTO_INCREMENT,           
		`retailer_org_id` int(10) DEFAULT NULL,                 
		`ticket_nbr` bigint(20) unsigned DEFAULT '0',           
		`channel` enum('WEB','TERMINAL') DEFAULT NULL,          
		`notification_time` datetime NOT NULL,                  
		`action_name` varchar(50) DEFAULT NULL,                 
		PRIMARY KEY (`auto_id`)                                 
	);		



  insert into st_dg_priviledge_rep 
	(
		action_id,
		priv_id, 
		priv_title, 
		priv_disp_name, 
		action_mapping, 
		parent_priv_id, 
		status, 
		priv_owner, 
		related_to, 
		group_name, 
		is_start, 
		channel, 
		priv_code, 
		hidden, 
		group_name_fr, 
		group_name_en, 
		is_audit_trail_display
	)
	values
	(
	  233, 
      0, 
     'RET_PLAY_BingoSeventyFive', 
     'Sale BingoSeventyFive', 
	 'bingoSeventyFiveBuy', 
 	 0, 
	 'ACTIVE', 
	 'RETAILER', 
	 'PLAY_MGT', 
	 'Sale:  BingoSeventyFiveBuy', 
	 'Y', 
	 'WEB', 
	 '(NULL)', 
	 'N', 
	 '', 
	 'Sale:  BingoSeventyFiveBuy', 
	 'N'
	);

CREATE TABLE `st_sbs_ret_sale` (
  `transaction_id` bigint(10) unsigned NOT NULL,
  `engine_tx_id` varchar(50) NOT NULL,
  `sports_id` int(10) unsigned NOT NULL DEFAULT '0',
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
) ENGINE=InnoDB; 


alter table `st_lms_retailer_transaction_master` change `transaction_type` `transaction_type` enum ('DG_SALE','CHEQUE','CHQ_BOUNCE','DR_NOTE','DG_PWT_AUTO','DG_REFUND_CANCEL','PURCHASE','DG_REFUND_FAILED','VAT',
'SALE','CASH','TDS','PWT_PLR','CR_NOTE_CASH','DR_NOTE_CASH','GOVT_COMM','SALE_RET','PWT','UNCLM_PWT','PWT_AUTO',
'DG_PWT_PLR','DG_PWT','DG_SALE_OFFLINE','CS_SALE','CS_CANCEL_SERVER','CS_CANCEL_RET','OLA_DEPOSIT','OLA_WITHDRAWL',
'OLA_DEPOSIT_REFUND','OLA_WITHDRAWL_REFUND','OLA_COMMISSION','SLE_SALE','SLE_REFUND_CANCEL','SLE_PWT','IW_SALE','IW_REFUND_CANCEL',
'IW_PWT','SBS_SALE') CHARACTER SET utf8  COLLATE utf8_general_ci   NULL; 

insert into `ge_merchant_master` `merchant_code`, `merchant_name`, `merchant_domain_name`, `registration_date`, `user_name`, `password`, `vendor_user_name`, `vendor_password`, `status`) values('SBS','Sports Betting Engine','localhost','2016-09-30 09:32:51',NULL,NULL,'E49B4EF3-1511-4B8B-86D2-CE78DA0F74D6','p@55w0rd','INACTIVE');




    						  
