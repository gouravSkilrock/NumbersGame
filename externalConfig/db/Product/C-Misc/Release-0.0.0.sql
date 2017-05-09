--liquibase formatted sql

--changeset BaseSPRMS:3 endDelimiter:#

#

CREATE      PROCEDURE `insDelArch`(startDate TIMESTAMP,endDate TIMESTAMP,archDBName VARCHAR(50))
BEGIN
	DECLARE done INT DEFAULT 0;
	DECLARE gameId INT DEFAULT 0;
	DECLARE min_txn_id BIGINT(20);
	DECLARE max_txn_id BIGINT(20);
	DECLARE gameDGCur CURSOR FOR SELECT game_id  FROM st_dg_game_master;
	DECLARE catCSCur CURSOR FOR SELECT category_id  FROM st_cs_product_category_master;
	DECLARE  CONTINUE HANDLER FOR NOT FOUND SET done=1;
	
	SET @temp_retailer_id=CONCAT("delete from temp_arch_trans_retailer");
	PREPARE stmt FROM @temp_retailer_id;
	EXECUTE stmt;
	SET @temp_agent_id=CONCAT("delete from temp_arch_trans_agent");
	PREPARE stmt FROM @temp_agent_id;
	EXECUTE stmt;
	SET @temp_bo_id=CONCAT("delete from temp_arch_trans_bo");
	PREPARE stmt FROM @temp_bo_id;
	EXECUTE stmt;
	SET @temp_agent_receipt_id=CONCAT("delete from temp_arch_rec_agent");
	PREPARE stmt FROM @temp_agent_receipt_id;
	EXECUTE stmt;
	
	SET @temp_bo_receipt_id=CONCAT("delete from temp_arch_rec_bo");
	PREPARE stmt FROM @temp_bo_receipt_id;
	EXECUTE stmt; 
   SELECT 'tempFill';
   
    SET @isOLA:=(SELECT IF(STATUS='ACTIVE',1,0) FROM st_lms_service_master WHERE service_code='OLA');
	IF(@isOLA) THEN
	SET @olaRetDep=CONCAT("insert into ",archDBName,".st_ola_ret_deposit select * from st_ola_ret_deposit where transaction_id in ( select transaction_id from st_lms_retailer_transaction_master where transaction_date>='",startDate,"' and transaction_date<='",endDate,"'  and transaction_type='OLA_DEPOSIT') ");
	PREPARE stmt FROM @olaRetDep;
	EXECUTE stmt;
	SET @olaDelRetDep=CONCAT("delete retDep from st_ola_ret_deposit retDep	inner  join st_lms_retailer_transaction_master slrm on slrm.transaction_id=	retDep.transaction_id where transaction_date>='",startDate,"' and transaction_date<='",endDate,"'  and transaction_type='OLA_DEPOSIT' " );
	PREPARE stmt FROM @olaDelRetDep;
	EXECUTE stmt;
	SET @olaRetDepRef=CONCAT("insert into ",archDBName,".st_ola_ret_deposit_refund select * from st_ola_ret_deposit_refund where transaction_id in ( select transaction_id from st_lms_retailer_transaction_master  where transaction_date>='",startDate,"' and transaction_date<='",endDate,"'  and transaction_type='OLA_DEPOSIT_REFUND') " );
	PREPARE stmt FROM @olaRetDepRef;
	EXECUTE stmt;
	SET @olaDelRetDepRef=CONCAT("delete retDepRef from    st_ola_ret_deposit_refund retDepRef inner  join st_lms_retailer_transaction_master slrm on slrm.transaction_id=retDepRef.transaction_id where transaction_date>='",startDate,"' and transaction_date<='",endDate,"'  and transaction_type='OLA_DEPOSIT_REFUND' " );
	PREPARE stmt FROM @olaDelRetDepRef;
	EXECUTE stmt;
	SET @olaRetWith=CONCAT("insert into ",archDBName,".st_ola_ret_withdrawl select * from  st_ola_ret_withdrawl where transaction_id in  (select transaction_id from st_lms_retailer_transaction_master  where transaction_date>='",startDate,"' and transaction_date<='",endDate,"'  and transaction_type='OLA_WITHDRAWL' )" );
	PREPARE stmt FROM @olaRetWith;
	EXECUTE stmt;
	SET @olaDelRetWith=CONCAT("delete retWith  from   st_ola_ret_withdrawl retWith inner  join st_lms_retailer_transaction_master slrm on slrm.transaction_id=retWith.transaction_id where transaction_date>='",startDate,"' and transaction_date<='",endDate,"'  and transaction_type='OLA_WITHDRAWL' " );
	PREPARE stmt FROM @olaDelRetWith;
	EXECUTE stmt;	
	SET @olaRetComm=CONCAT("insert into ",archDBName,".st_ola_ret_comm select * from st_ola_ret_comm where transaction_id in  (select transaction_id from st_lms_retailer_transaction_master  where transaction_date>='",startDate,"' and transaction_date<='",endDate,"'  and transaction_type='OLA_COMMISSION' ) ");
	PREPARE stmt FROM @olaRetComm;
	EXECUTE stmt;
	SET @olaDelRetComm=CONCAT("delete retComm  from   st_ola_ret_comm retComm inner  join st_lms_retailer_transaction_master slrm on slrm.transaction_id=retComm.transaction_id where transaction_date>='",startDate,"' and transaction_date<='",endDate,"'  and transaction_type='OLA_COMMISSION' " );
	PREPARE stmt FROM @olaDelRetComm;
	EXECUTE stmt;
	SET @olaAgtDep=CONCAT(" insert into ",archDBName,".st_ola_agt_deposit select * from  st_ola_agt_deposit where transaction_id in(select transaction_id from st_lms_agent_transaction_master where transaction_date>='",startDate,"' and transaction_date<='",endDate,"'  and transaction_type='OLA_DEPOSIT' ) " );
	PREPARE stmt FROM @olaAgtDep;
	EXECUTE stmt;
	SET @olaDelAgtDep=CONCAT("delete agtDep from st_ola_agt_deposit agtDep inner  join st_lms_agent_transaction_master slam on slam.transaction_id= agtDep.transaction_id where transaction_date>='",startDate,"' and transaction_date<='",endDate,"'  and transaction_type='OLA_DEPOSIT' " );
	PREPARE stmt FROM @olaDelAgtDep;
	EXECUTE stmt;	
	SET @olaAgtDepRef=CONCAT("insert into ",archDBName,".st_ola_agt_deposit_refund select * from  st_ola_agt_deposit_refund where transaction_id in(select transaction_id from st_lms_agent_transaction_master where transaction_date>='",startDate,"' and transaction_date<='",endDate,"'  and transaction_type='OLA_DEPOSIT_REFUND' )" );
	PREPARE stmt FROM @olaAgtDepRef;
	EXECUTE stmt;
	SET @olaDelAgtDepRef=CONCAT("delete agtDepRef from st_ola_agt_deposit_refund agtDepRef inner  join st_lms_agent_transaction_master slam on slam.transaction_id= agtDepRef.transaction_id where transaction_date>='",startDate,"' and transaction_date<='",endDate,"'  and transaction_type='OLA_DEPOSIT_REFUND' " );
	PREPARE stmt FROM @olaDelAgtDepRef;
	EXECUTE stmt;
	SET @olaAgtWith=CONCAT("insert into ",archDBName,".st_ola_agt_withdrawl select * from  st_ola_agt_withdrawl where transaction_id in(select transaction_id from st_lms_agent_transaction_master where transaction_date>='",startDate,"' and transaction_date<='",endDate,"'  and transaction_type='OLA_WITHDRAWL' )" );
	PREPARE stmt FROM @olaAgtWith;
	EXECUTE stmt;
	SET @olaDelAgtWith=CONCAT("delete agtWith from st_ola_agt_withdrawl agtWith inner  join st_lms_agent_transaction_master slam on slam.transaction_id= agtWith.transaction_id where transaction_date>='",startDate,"' and transaction_date<='",endDate,"'  and transaction_type='OLA_WITHDRAWL' " );
	PREPARE stmt FROM @olaDelAgtWith;
	EXECUTE stmt;
	SET @olaAgtComm=CONCAT("insert into ",archDBName,".st_ola_agt_comm select * from  st_ola_agt_comm where transaction_id in(select transaction_id from st_lms_agent_transaction_master where transaction_date>='",startDate,"' and transaction_date<='",endDate,"'  and transaction_type='OLA_COMMISSION' ) " );
	PREPARE stmt FROM @olaAgtComm;
	EXECUTE stmt;
	SET @olaDelAgtComm=CONCAT("delete agtComm from st_ola_agt_comm agtComm inner  join st_lms_agent_transaction_master slam on slam.transaction_id= agtComm.transaction_id where transaction_date>='",startDate,"' and transaction_date<='",endDate,"'  and transaction_type='OLA_COMMISSION' " );
	PREPARE stmt FROM @olaDelAgtComm;
	EXECUTE stmt;
	SET @olaAgtDepDirPlr=CONCAT(" insert into ",archDBName,".st_ola_agt_direct_plr_deposit select * from  st_ola_agt_direct_plr_deposit where transaction_id in(select transaction_id from st_lms_agent_transaction_master where transaction_date>='",startDate,"' and transaction_date<='",endDate,"'  and transaction_type='OLA_DEPOSIT_PLR' ) " );
	PREPARE stmt FROM @olaAgtDepDirPlr;
	EXECUTE stmt;
	SET @olaDelAgtDepDirPlr=CONCAT("delete agtDepDirPlr from st_ola_agt_direct_plr_deposit agtDepDirPlr inner  join st_lms_agent_transaction_master slam on slam.transaction_id= agtDepDirPlr.transaction_id where transaction_date>='",startDate,"' and transaction_date<='",endDate,"'  and transaction_type='OLA_DEPOSIT_PLR' " );
	PREPARE stmt FROM @olaDelAgtDepDirPlr;
	EXECUTE stmt;	
	SET @olaAgtDepRefDirPlr=CONCAT("insert into ",archDBName,".st_ola_agt_direct_plr_deposit_refund select * from  st_ola_agt_direct_plr_deposit_refund where transaction_id in(select transaction_id from st_lms_agent_transaction_master where transaction_date>='",startDate,"' and transaction_date<='",endDate,"'  and transaction_type='OLA_DEPOSIT_REFUND_PLR' )" );
	PREPARE stmt FROM @olaAgtDepRefDirPlr;
	EXECUTE stmt;
	SET @olaDelAgtDepRefDirPlr=CONCAT("delete agtDepRef from st_ola_agt_deposit_refund agtDepRef inner  join st_lms_agent_transaction_master slam on slam.transaction_id= agtDepRef.transaction_id where transaction_date>='",startDate,"' and transaction_date<='",endDate,"'  and transaction_type='OLA_DEPOSIT_REFUND_PLR' " );
	PREPARE stmt FROM @olaDelAgtDepRefDirPlr;
	EXECUTE stmt;
	SET @olaAgtWithDirPlr=CONCAT("insert into ",archDBName,".st_ola_agt_direct_plr_withdrawl select * from  st_ola_agt_direct_plr_withdrawl where transaction_id in(select transaction_id from st_lms_agent_transaction_master where transaction_date>='",startDate,"' and transaction_date<='",endDate,"'  and transaction_type='OLA_WITHDRAWL_PLR' )" );
	PREPARE stmt FROM @olaAgtWithDirPlr;
	EXECUTE stmt;
	SET @olaDelAgtWithDirPlr=CONCAT("delete agtWith from st_ola_agt_direct_plr_withdrawl agtWith inner  join st_lms_agent_transaction_master slam on slam.transaction_id= agtWith.transaction_id where transaction_date>='",startDate,"' and transaction_date<='",endDate,"'  and transaction_type='OLA_WITHDRAWL_PLR' " );
	PREPARE stmt FROM @olaDelAgtWithDirPlr;
	EXECUTE stmt;
	SET @olaBoDep=CONCAT("insert into ",archDBName,".st_ola_bo_deposit select * from st_ola_bo_deposit where transaction_id in(select transaction_id from st_lms_bo_transaction_master where transaction_date>='",startDate,"' and transaction_date<='",endDate,"'  and transaction_type='OLA_DEPOSIT' )"); 
	PREPARE stmt FROM @olaBoDep;
	EXECUTE stmt;
	
	SET @olaDelBoDep=CONCAT(" delete boDep  from st_ola_bo_deposit boDep inner  join st_lms_bo_transaction_master slbm on slbm.transaction_id=boDep.transaction_id where transaction_date>='",startDate,"' and transaction_date<='",endDate,"'  and transaction_type='OLA_DEPOSIT' " );
	PREPARE stmt FROM @olaDelBoDep;
	EXECUTE stmt;
	SET @olaBoDepRef=CONCAT("insert into ",archDBName,".st_ola_bo_deposit_refund select * from  st_ola_bo_deposit_refund where transaction_id in(select transaction_id from st_lms_bo_transaction_master where transaction_date>='",startDate,"' and transaction_date<='",endDate,"'  and transaction_type='OLA_DEPOSIT_REFUND' )" );
	PREPARE stmt FROM @olaBoDepRef;
	EXECUTE stmt;
	SET @olaDelBoDepRef=CONCAT("delete  boDepRef from    st_ola_bo_deposit_refund boDepRef inner  join st_lms_bo_transaction_master slbm on slbm.transaction_id=boDepRef.transaction_id where transaction_date>='",startDate,"' and transaction_date<='",endDate,"'  and transaction_type='OLA_DEPOSIT_REFUND' " );
	PREPARE stmt FROM @olaDelBoDepRef;
	EXECUTE stmt;
	SET @olaBoWith=CONCAT("insert into ",archDBName,".st_ola_bo_withdrawl select * from  st_ola_bo_withdrawl where transaction_id in(select transaction_id from st_lms_bo_transaction_master where transaction_date>='",startDate,"' and transaction_date<='",endDate,"'  and transaction_type='OLA_WITHDRAWL' )" );
	PREPARE stmt FROM @olaBoWith;
	EXECUTE stmt;
	SET @olaDelBoWith=CONCAT("delete boWith from  st_ola_bo_withdrawl boWith inner  join st_lms_bo_transaction_master slbm on slbm.transaction_id=boWith.transaction_id where transaction_date>='",startDate,"' and transaction_date<='",endDate,"'  and transaction_type='OLA_WITHDRAWL' " );
	PREPARE stmt FROM @olaDelBoWith;
	EXECUTE stmt;
	SET @olaBoComm=CONCAT("insert into ",archDBName,".st_ola_bo_comm select * from  st_ola_bo_comm  where transaction_id in(select transaction_id  from st_lms_bo_transaction_master where transaction_date>='",startDate,"' and transaction_date<='",endDate,"'  and transaction_type='OLA_COMMISSION' )" );
	PREPARE stmt FROM @olaBoComm;
	EXECUTE stmt;
	SET @olaDelBoComm=CONCAT("delete boComm from   st_ola_bo_comm boComm inner  join st_lms_bo_transaction_master slbm on slbm.transaction_id=boComm .transaction_id where transaction_date>='",startDate,"' and transaction_date<='",endDate,"'  and transaction_type='OLA_COMMISSION' " );
	PREPARE stmt FROM @olaDelBoComm;
	EXECUTE stmt;
	SET @olaBoDepDirPlr=CONCAT("insert into ",archDBName,".st_ola_bo_direct_plr_deposit select * from st_ola_bo_direct_plr_deposit where transaction_id in(select transaction_id from st_lms_bo_transaction_master where transaction_date>='",startDate,"' and transaction_date<='",endDate,"'  and transaction_type='OLA_DEPOSIT_PLR' )"); 
	PREPARE stmt FROM @olaBoDepDirPlr;
	EXECUTE stmt;
	SET @olaDelBoDepDirPlr=CONCAT(" delete boDep  from st_ola_bo_direct_plr_deposit boDep inner  join st_lms_bo_transaction_master slbm on slbm.transaction_id=boDep.transaction_id where transaction_date>='",startDate,"' and transaction_date<='",endDate,"'  and transaction_type='OLA_DEPOSIT_PLR' " );
	PREPARE stmt FROM @olaDelBoDepDirPlr;
	EXECUTE stmt;
	SET @olaBoDepRefDirPlr=CONCAT("insert into ",archDBName,".st_ola_bo_direct_plr_deposit_refund select * from  st_ola_bo_direct_plr_deposit_refund where transaction_id in(select transaction_id from st_lms_bo_transaction_master where transaction_date>='",startDate,"' and transaction_date<='",endDate,"'  and transaction_type='OLA_DEPOSIT_REFUND_PLR' )" );
	PREPARE stmt FROM @olaBoDepRefDirPlr;
	EXECUTE stmt;
	SET @olaDelBoDepRefDirPlr=CONCAT("delete  boDepRef from    st_ola_bo_direct_plr_deposit_refund boDepRef inner  join st_lms_bo_transaction_master slbm on slbm.transaction_id=boDepRef.transaction_id where transaction_date>='",startDate,"' and transaction_date<='",endDate,"'  and transaction_type='OLA_DEPOSIT_REFUND_PLR' " );
	PREPARE stmt FROM @olaDelBoDepRefDirPlr;
	EXECUTE stmt;
	SET @olaBoWithDirPlr=CONCAT("insert into ",archDBName,".st_ola_bo_direct_plr_withdrawl select * from  st_ola_bo_direct_plr_withdrawl where transaction_id in(select transaction_id from st_lms_bo_transaction_master where transaction_date>='",startDate,"' and transaction_date<='",endDate,"'  and transaction_type='OLA_WITHDRAWL_PLR' )" );
	PREPARE stmt FROM @olaBoWithDirPlr;
	EXECUTE stmt;
	SET @olaDelBoWithDirPlr=CONCAT("delete boWith from  st_ola_bo_direct_plr_withdrawl boWith inner  join st_lms_bo_transaction_master slbm on slbm.transaction_id=boWith.transaction_id where transaction_date>='",startDate,"' and transaction_date<='",endDate,"'  and transaction_type='OLA_WITHDRAWL_PLR' " );
	PREPARE stmt FROM @olaDelBoWithDirPlr;
	EXECUTE stmt;
	SET @olaWdrwlTmp=CONCAT("insert into ",archDBName,".st_ola_withdrawl_temp select * from  st_ola_withdrawl_temp where ref_transaction_id in(select transaction_id from st_lms_bo_transaction_master where transaction_date>='",startDate,"' and transaction_date<='",endDate,"'  and transaction_type in ('OLA_WITHDRAWL_PLR', 'OLA_WITHDRAWL') UNION select transaction_id  from st_lms_agent_transaction_master where transaction_date>='",startDate,"' and transaction_date<='",endDate,"'  and transaction_type in ('OLA_WITHDRAWL_PLR', 'OLA_WITHDRAWL') UNION select transaction_id  from st_lms_retailer_transaction_master where transaction_date>='",startDate,"' and transaction_date<='",endDate,"'  and transaction_type in ('OLA_WITHDRAWL_PLR', 'OLA_WITHDRAWL'))" );
	PREPARE stmt FROM @olaWdrwlTmp;
	EXECUTE stmt;
	SET @olaDelWdrwlTmp=CONCAT("delete withTmp FROM st_ola_withdrawl_temp withTmp inner  join (select transaction_id, transaction_date from st_lms_bo_transaction_master where transaction_date>='",startDate,"' and transaction_date<='",endDate,"'  and transaction_type in ('OLA_WITHDRAWL_PLR', 'OLA_WITHDRAWL') UNION select transaction_id, transaction_date from st_lms_agent_transaction_master where transaction_date>='",startDate,"' and transaction_date<='",endDate,"'  and transaction_type in ('OLA_WITHDRAWL_PLR', 'OLA_WITHDRAWL') UNION select transaction_id, transaction_date from st_lms_retailer_transaction_master where transaction_date>='",startDate,"' and transaction_date<='",endDate,"'  and transaction_type in ('OLA_WITHDRAWL_PLR', 'OLA_WITHDRAWL')) txnTabls on txnTabls.transaction_id=withTmp.ref_transaction_id where txnTabls.transaction_date>='",startDate,"' and txnTabls.transaction_date<='",endDate,"'");
	PREPARE stmt FROM @olaDelWdrwlTmp;
	EXECUTE stmt;
	
	
END IF;	
SELECT 'ola DONE';
	SET @isDG:=(SELECT IF(STATUS='ACTIVE',1,0) FROM st_lms_service_master WHERE service_code='DG');
	IF(@isDG=1) THEN 
	  OPEN  gameDGCur;
	    read_loop: LOOP
		FETCH  gameDGCur INTO gameId;
		    IF done THEN
		      LEAVE read_loop;
		    END IF;
		SET @dg_sale=CONCAT("insert into ",archDBName,".st_dg_ret_sale_",gameId," select sale.transaction_id, sale.game_id, ticket_nbr, mrp_amt, retailer_comm, net_amt, agent_comm, agent_net_amt, sale.retailer_org_id, claim_status, good_cause_amt, agent_ref_transaction_id, vat_amt, taxable_sale,player_mob_number,ret_sd_amt,agt_vat_amt from st_dg_ret_sale_",gameId," sale inner join st_lms_retailer_transaction_master rtm on sale.transaction_id=rtm.transaction_id where sale.game_id=",gameId,"  and  transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and transaction_type in ('dg_sale','DG_SALE_OFFLINE')");    
		PREPARE stmt FROM @dg_sale;
	 	EXECUTE stmt;
		SET @dg_sale_del=CONCAT("delete sale from st_dg_ret_sale_",gameId," sale inner join st_lms_retailer_transaction_master rtm on sale.transaction_id=rtm.transaction_id where sale.game_id=",gameId,"  and  transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and transaction_type in ('dg_sale','DG_SALE_OFFLINE')");
		
		PREPARE stmt FROM @dg_sale_del;
	 	EXECUTE stmt;
		SET @dg_ref=CONCAT("insert into ",archDBName,".st_dg_ret_sale_refund_",gameId,"  select * from st_dg_ret_sale_refund_",gameId," where transaction_id in (select transaction_id  from    st_lms_retailer_transaction_master where game_id=",gameId,"  and  transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and transaction_type in ('DG_REFUND_CANCEL','DG_REFUND_FAILED'))");
		PREPARE stmt FROM @dg_ref;
 		EXECUTE stmt;
		SET @dg_ref_del=CONCAT("delete sale from st_dg_ret_sale_refund_",gameId," sale inner join st_lms_retailer_transaction_master rtm on sale.transaction_id=rtm.transaction_id where sale.game_id=",gameId,"  and  transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and transaction_type in ('DG_REFUND_CANCEL','DG_REFUND_FAILED')");
		PREPARE stmt FROM @dg_ref_del;
 		EXECUTE stmt;
       
		SET @dg_pwt=CONCAT("insert into ",archDBName,".st_dg_ret_pwt_",gameId," select * from st_dg_ret_pwt_",gameId," where transaction_id in (select transaction_id  from    st_lms_retailer_transaction_master where game_id=",gameId," and  transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and transaction_type='dg_pwt' )");
		PREPARE stmt FROM @dg_pwt;
		EXECUTE stmt;
 
		
		SET @dg_pwt_del=CONCAT("delete pwt from st_dg_ret_pwt_",gameId," pwt inner join st_lms_retailer_transaction_master rtm on pwt.transaction_id=rtm.transaction_id where rtm.game_id=",gameId," and  transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and transaction_type='dg_pwt'");
		PREPARE stmt FROM @dg_pwt_del;
		EXECUTE stmt;
		SET @dg_pwt_inv=CONCAT("insert into ",archDBName,".st_dg_pwt_inv_",gameId," (ticket_nbr, draw_id, panel_id,pwt_amt,status,retailer_transaction_id,agent_transaction_id,bo_transaction_id,is_direct_plr) select ticket_nbr, draw_id, panel_id,pwt_amt,status,retailer_transaction_id,agent_transaction_id,bo_transaction_id,is_direct_plr from st_dg_pwt_inv_",gameId," inner join st_lms_retailer_transaction_master rtm on retailer_transaction_id=transaction_id where rtm.game_id=",gameId," and  transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and transaction_type='DG_PWT' union select ticket_nbr, draw_id, panel_id,pwt_amt,status,retailer_transaction_id,agent_transaction_id,bo_transaction_id,is_direct_plr from st_dg_pwt_inv_",gameId," inner join st_lms_agent_transaction_master atm on agent_transaction_id=transaction_id where transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and transaction_type in ('DG_PWT_AUTO','DG_PWT_PLR') union select ticket_nbr, draw_id, panel_id,pwt_amt,status,retailer_transaction_id,agent_transaction_id,bo_transaction_id,is_direct_plr from st_dg_pwt_inv_",gameId," inner join st_lms_bo_transaction_master btm on bo_transaction_id=transaction_id where transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and transaction_type in ('DG_PWT_AUTO','DG_PWT_PLR')");
		PREPARE stmt FROM @dg_pwt_inv;
		EXECUTE stmt;
		SET @dg_pwt_inv_del=CONCAT("delete pwt from st_dg_pwt_inv_",gameId," pwt inner join st_lms_retailer_transaction_master rtm on retailer_transaction_id=rtm.transaction_id where rtm.game_id=",gameId," and  rtm.transaction_date>='",startDate,"' and rtm.transaction_date<='",endDate,"' and rtm.transaction_type='DG_PWT'");
	
		PREPARE stmt FROM @dg_pwt_inv_del;
		EXECUTE stmt;
		SET @dg_pwt_inv_del=CONCAT("delete pwt from st_dg_pwt_inv_",gameId," pwt inner join st_lms_agent_transaction_master atm on agent_transaction_id=atm.transaction_id where atm.transaction_date>='",startDate,"' and atm.transaction_date<='",endDate,"' and atm.transaction_type in('DG_PWT_PLR', 'DG_PWT_AUTO')");
		PREPARE stmt FROM @dg_pwt_inv_del;
		EXECUTE stmt;
		SET @dg_pwt_inv_del=CONCAT("delete pwt from st_dg_pwt_inv_",gameId," pwt inner join st_lms_bo_transaction_master btm on bo_transaction_id=btm.transaction_id where btm.transaction_date>='",startDate,"' and btm.transaction_date<='",endDate,"' and btm.transaction_type in('DG_PWT_PLR', 'DG_PWT_AUTO')");
		PREPARE stmt FROM @dg_pwt_inv_del;
		EXECUTE stmt;
		SET @st_dg_printed_tickets=CONCAT("insert into ",archDBName,".st_dg_printed_tickets_",gameId,"  select * from st_dg_printed_tickets_",gameId," where notification_time>='",startDate,"' and notification_time<='",endDate,"'");
		PREPARE stmt FROM @st_dg_printed_tickets;
 		EXECUTE stmt;
		SET @st_dg_printed_tickets_del=CONCAT("delete from st_dg_printed_tickets_",gameId," where notification_time>='",startDate,"' and notification_time<='",endDate,"'");
		PREPARE stmt FROM @st_dg_printed_tickets_del;
 		EXECUTE stmt;
	    END LOOP;
	  CLOSE gameDGCur;
		SET @st_dg_approval_req_master=CONCAT("insert into ",archDBName,".st_dg_approval_req_master select * from st_dg_approval_req_master where approval_date>='",startDate,"' and approval_date<='",endDate,"'");
		PREPARE stmt FROM @st_dg_approval_req_master;
		EXECUTE stmt;
		SET @st_dg_approval_req_master_del=CONCAT("delete from st_dg_approval_req_master where approval_date>='",startDate,"' and approval_date<='",endDate,"'");
		PREPARE stmt FROM @st_dg_approval_req_master_del;
		EXECUTE stmt;
		SET @st_dg_bo_agent_pwt_comm_variance_history=CONCAT("insert into ",archDBName,".st_dg_bo_agent_pwt_comm_variance_history select * from st_dg_bo_agent_pwt_comm_variance_history where date_changed>='",startDate,"' and date_changed<='",endDate,"'");
		PREPARE stmt FROM @st_dg_bo_agent_pwt_comm_variance_history;
		EXECUTE stmt;
		SET @st_dg_bo_agent_pwt_comm_variance_history_del=CONCAT("delete from st_dg_bo_agent_pwt_comm_variance_history where date_changed>='",startDate,"' and date_changed<='",endDate,"'");
		PREPARE stmt FROM @st_dg_bo_agent_pwt_comm_variance_history_del;
		EXECUTE stmt;
		SET @st_dg_bo_agent_sale_comm_variance_history=CONCAT("insert into ",archDBName,".st_dg_bo_agent_sale_comm_variance_history select * from st_dg_bo_agent_sale_comm_variance_history where date_changed>='",startDate,"' and date_changed<='",endDate,"'");
		PREPARE stmt FROM @st_dg_bo_agent_sale_comm_variance_history;
		EXECUTE stmt;
		SET @st_dg_bo_agent_sale_comm_variance_history_del=CONCAT("delete from st_dg_bo_agent_sale_comm_variance_history where date_changed>='",startDate,"' and date_changed<='",endDate,"'");
		PREPARE stmt FROM @st_dg_bo_agent_sale_comm_variance_history_del;
		EXECUTE stmt;
		SET @st_dg_bo_ticket_cancel=CONCAT("insert into ",archDBName,".st_dg_bo_ticket_cancel select * from st_dg_bo_ticket_cancel where ret_trans_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and transaction_type in ('DG_REFUND_CANCEL','DG_REFUND_FAILED'))");
		PREPARE stmt FROM @st_dg_bo_ticket_cancel;
		EXECUTE stmt;
		SET @st_dg_bo_ticket_cancel_del=CONCAT("delete from st_dg_bo_ticket_cancel where ret_trans_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and transaction_type in ('DG_REFUND_CANCEL','DG_REFUND_FAILED'))");
		PREPARE stmt FROM @st_dg_bo_ticket_cancel_del;
		EXECUTE stmt;
		SET @st_dg_agt_sale=CONCAT("insert into ",archDBName,".st_dg_agt_sale select * from st_dg_agt_sale where transaction_id in (select transaction_id  from    st_lms_agent_transaction_master where   transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and transaction_type='DG_SALE')");
		PREPARE stmt FROM @st_dg_agt_sale;
		EXECUTE stmt;
		SET @st_dg_agt_sale_del=CONCAT("delete from st_dg_agt_sale where transaction_id in (select transaction_id from st_lms_agent_transaction_master where transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and transaction_type='DG_SALE')");
		PREPARE stmt FROM @st_dg_agt_sale_del;
		EXECUTE stmt;
		SET @st_dg_agt_pwt=CONCAT(" insert into ",archDBName,".st_dg_agt_pwt select * from st_dg_agt_pwt where transaction_id in (select transaction_id from    st_lms_agent_transaction_master where transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and transaction_type IN('DG_PWT_AUTO','DG_PWT_PLR'))");
		PREPARE stmt FROM @st_dg_agt_pwt;
		EXECUTE stmt;
		SET @st_dg_agt_pwt_del=CONCAT("delete from st_dg_agt_pwt where transaction_id in (select transaction_id from st_lms_agent_transaction_master where transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and transaction_type IN('DG_PWT_AUTO','DG_PWT_PLR'))");
		PREPARE stmt FROM @st_dg_agt_pwt_del;
		EXECUTE stmt;
		SET @st_dg_agt_direct_plr_pwt=CONCAT("insert into ",archDBName,".st_dg_agt_direct_plr_pwt select * from st_dg_agt_direct_plr_pwt where transaction_id in (select transaction_id  from    st_lms_agent_transaction_master where  transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and  transaction_type='DG_PWT_PLR' )");
		PREPARE stmt FROM @st_dg_agt_direct_plr_pwt;
		EXECUTE stmt;
		SET @st_dg_agt_direct_plr_pwt=CONCAT("delete from st_dg_agt_direct_plr_pwt where transaction_id in (select transaction_id  from    st_lms_agent_transaction_master where  transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and  transaction_type='DG_PWT_PLR' )");
		PREPARE stmt FROM @st_dg_agt_direct_plr_pwt;
		EXECUTE stmt;
		SET @st_dg_agt_sale_refund=CONCAT(" insert into ",archDBName,".st_dg_agt_sale_refund select * from st_dg_agt_sale_refund where transaction_id in (select transaction_id  from    st_lms_agent_transaction_master where   transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and transaction_type in ('DG_REFUND_CANCEL','DG_REFUND_FAILED'))");
		PREPARE stmt FROM @st_dg_agt_sale_refund;
		EXECUTE stmt;
		SET @st_dg_agt_sale_refund=CONCAT("delete from st_dg_agt_sale_refund where transaction_id in (select transaction_id  from    st_lms_agent_transaction_master where   transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and transaction_type in ('DG_REFUND_CANCEL','DG_REFUND_FAILED'))");
		PREPARE stmt FROM @st_dg_agt_sale_refund;
		EXECUTE stmt;
		SET @st_dg_bo_sale=CONCAT(" insert into ",archDBName,".st_dg_bo_sale select * from st_dg_bo_sale where transaction_id in (select transaction_id  from    st_lms_bo_transaction_master where   transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and transaction_type='dg_sale' )");
		PREPARE stmt FROM @st_dg_bo_sale;
		EXECUTE stmt;
		SET @st_dg_bo_sale=CONCAT("delete from st_dg_bo_sale where transaction_id in (select transaction_id  from    st_lms_bo_transaction_master where   transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and transaction_type='dg_sale' )");
		PREPARE stmt FROM @st_dg_bo_sale;
		EXECUTE stmt;
		SET @st_dg_bo_pwt=CONCAT(" insert into ",archDBName,".st_dg_bo_pwt select * from st_dg_bo_pwt where transaction_id in (select transaction_id  from    st_lms_bo_transaction_master where  transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and transaction_type ='DG_PWT_AUTO')");
		PREPARE stmt FROM @st_dg_bo_pwt;
		EXECUTE stmt;
		SET @st_dg_bo_pwt=CONCAT("delete from st_dg_bo_pwt where transaction_id in (select transaction_id  from    st_lms_bo_transaction_master where  transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and transaction_type ='DG_PWT_AUTO')");
		PREPARE stmt FROM @st_dg_bo_pwt;
		EXECUTE stmt;
		SET @st_dg_bo_sale_refund=CONCAT("insert into ",archDBName,".st_dg_bo_sale_refund   select * from st_dg_bo_sale_refund where transaction_id in (select transaction_id  from    st_lms_bo_transaction_master where   transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and transaction_type in ('DG_REFUND_CANCEL','DG_REFUND_FAILED'))");
		PREPARE stmt FROM @st_dg_bo_sale_refund;
		EXECUTE stmt;
		SET @st_dg_bo_sale_refund=CONCAT("delete from st_dg_bo_sale_refund where transaction_id in (select transaction_id  from    st_lms_bo_transaction_master where   transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and transaction_type in ('DG_REFUND_CANCEL','DG_REFUND_FAILED'))");
		PREPARE stmt FROM @st_dg_bo_sale_refund;
		EXECUTE stmt;
		SET @st_dg_bo_direct_plr_pwt=CONCAT(" insert into ",archDBName,".st_dg_bo_direct_plr_pwt    select * from st_dg_bo_direct_plr_pwt where transaction_id in (select transaction_id  from    st_lms_bo_transaction_master where transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and  transaction_type='DG_PWT_PLR' )");
		PREPARE stmt FROM @st_dg_bo_direct_plr_pwt;
		EXECUTE stmt;
		SET @st_dg_bo_direct_plr_pwt=CONCAT("delete from st_dg_bo_direct_plr_pwt where transaction_id in (select transaction_id  from    st_lms_bo_transaction_master where transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and  transaction_type='DG_PWT_PLR' )");
		PREPARE stmt FROM @st_dg_bo_direct_plr_pwt;
		EXECUTE stmt;
	END IF;
SELECT 'dg done';
SET done=0;
	SET @isCS:=(SELECT IF(STATUS='ACTIVE',1,0) FROM st_lms_service_master WHERE service_code='CS');
	IF(@isCS=1) THEN 
	
	  OPEN  catCSCur;
	    read_loop: LOOP
		FETCH  catCSCur INTO gameId;
		    IF done THEN
		      LEAVE read_loop;
		    END IF;
			SET @retCSSale=CONCAT("insert into ",archDBName,".st_cs_sale_",gameId," select * from st_cs_sale_",gameId," where transaction_id in (select transaction_id  from st_lms_retailer_transaction_master where  transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and transaction_type='CS_SALE')");
			PREPARE stmt FROM @retCSSale;
			EXECUTE stmt;
			SET @delRetCSSale=CONCAT("delete from st_cs_sale_",gameId," where transaction_id in (select transaction_id  from    st_lms_retailer_transaction_master where  transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and transaction_type='CS_SALE')");
			PREPARE stmt FROM @delRetCSSale;
			EXECUTE stmt;
			SET @retCSRefund=CONCAT("insert into ",archDBName,".st_cs_refund_",gameId," select * from st_cs_refund_",gameId," where transaction_id in (select transaction_id  from st_lms_retailer_transaction_master where  transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and transaction_type in ('CS_CANCEL_SERVER','CS_CANCEL_RET'))");
			PREPARE stmt FROM @retCSRefund;
			EXECUTE stmt;
			SET @delRetCSRefund=CONCAT("delete from st_cs_refund_",gameId," where transaction_id in (select transaction_id  from    st_lms_retailer_transaction_master where  transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and transaction_type in ('CS_CANCEL_SERVER','CS_CANCEL_RET'))");
			PREPARE stmt FROM @delRetCSRefund;
			EXECUTE stmt;
		 END LOOP;
	  CLOSE catCSCur;
	
	SET @agtCSSale=CONCAT("insert into ",archDBName,".st_cs_agt_sale select * from st_cs_agt_sale where transaction_id in (select transaction_id  from st_lms_agent_transaction_master where transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and transaction_type='CS_SALE')");
	PREPARE stmt FROM @agtCSSale;
	EXECUTE stmt;
	SET @delAgtCSSale=CONCAT("delete from st_cs_agt_sale where transaction_id in (select transaction_id  from st_lms_agent_transaction_master where  transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and transaction_type='CS_SALE')");
	PREPARE stmt FROM @delAgtCSSale;
	EXECUTE stmt;
	SET @agtCSRefund=CONCAT("insert into ",archDBName,".st_cs_agt_refund select * from st_cs_agt_refund where transaction_id in (select transaction_id  from st_lms_agent_transaction_master where  transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and transaction_type in ('CS_CANCEL_SERVER','CS_CANCEL_RET'))");
	PREPARE stmt FROM @agtCSRefund;
	EXECUTE stmt;
	SET @delAgtCSRefund=CONCAT("delete from st_cs_agt_refund where transaction_id in (select transaction_id  from st_lms_agent_transaction_master where  transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and transaction_type in ('CS_CANCEL_SERVER','CS_CANCEL_RET'))");
	PREPARE stmt FROM @delAgtCSRefund;
	EXECUTE stmt;
	SET @boCSSale=CONCAT("insert into ",archDBName,".st_cs_bo_sale select * from st_cs_bo_sale where transaction_id in (select transaction_id  from st_lms_bo_transaction_master where transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and transaction_type='CS_SALE')");
	PREPARE stmt FROM @boCSSale;
	EXECUTE stmt;
	SET @delBOCSSale=CONCAT("delete from st_cs_bo_sale where transaction_id in (select transaction_id  from st_lms_bo_transaction_master where  transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and transaction_type='CS_SALE')");
	PREPARE stmt FROM @delBOCSSale;
	EXECUTE stmt;
	SET @boCSRefund=CONCAT("insert into ",archDBName,".st_cs_bo_refund select * from st_cs_bo_refund where transaction_id in (select transaction_id  from st_lms_bo_transaction_master where  transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and transaction_type in ('CS_CANCEL_SERVER','CS_CANCEL_RET'))");
	PREPARE stmt FROM @boCSRefund;
	EXECUTE stmt;
	SET @delAgtCSRefund=CONCAT("delete from st_cs_bo_refund where transaction_id in (select transaction_id  from st_lms_bo_transaction_master where  transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and transaction_type in ('CS_CANCEL_SERVER','CS_CANCEL_RET'))");
	PREPARE stmt FROM @delAgtCSRefund;
	EXECUTE stmt;
    END IF;
SELECT 'cs done';
SET @isSE:=(SELECT IF(STATUS='ACTIVE',1,0) FROM st_lms_service_master WHERE service_code='SE');
IF(@isSE=1) THEN 
	SET @st_se_retailer_pwt=CONCAT("insert into ",archDBName,".st_se_retailer_pwt select * from st_se_retailer_pwt where transaction_id in (select transaction_id  from    st_lms_retailer_transaction_master where  transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and transaction_type='pwt')");
	PREPARE stmt FROM @st_se_retailer_pwt;
	EXECUTE stmt;
	SET @st_se_retailer_pwt=CONCAT("delete from st_se_retailer_pwt where transaction_id in (select transaction_id  from    st_lms_retailer_transaction_master where  transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and transaction_type='pwt')");
	PREPARE stmt FROM @st_se_retailer_pwt;
	EXECUTE stmt;
	SET @st_se_agent_pwt=CONCAT(" insert into ",archDBName,".st_se_agent_pwt select * from st_se_agent_pwt where transaction_id in (select transaction_id  from    st_lms_agent_transaction_master where transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and transaction_type in('PWT_AUTO','PWT') )");
	PREPARE stmt FROM @st_se_agent_pwt;
	EXECUTE stmt;
	SET @st_se_agent_pwt=CONCAT("delete from st_se_agent_pwt where transaction_id in (select transaction_id  from    st_lms_agent_transaction_master where transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and transaction_type in('PWT_AUTO','PWT') )");
	PREPARE stmt FROM @st_se_agent_pwt;
	EXECUTE stmt;
	SET @st_se_agt_direct_player_pwt=CONCAT(" insert into ",archDBName,".st_se_agt_direct_player_pwt select * from st_se_agt_direct_player_pwt where transaction_id in (select transaction_id  from    st_lms_agent_transaction_master where transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and     transaction_type='PWT_PLR' )");
	PREPARE stmt FROM @st_se_agt_direct_player_pwt;
	EXECUTE stmt;
	SET @st_se_agt_direct_player_pwt=CONCAT("delete from st_se_agt_direct_player_pwt where transaction_id in (select transaction_id  from    st_lms_agent_transaction_master where transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and     transaction_type='PWT_PLR' )");
	PREPARE stmt FROM @st_se_agt_direct_player_pwt;
	EXECUTE stmt;
	SET @st_se_supplier_bo_transaction=CONCAT(" insert into ",archDBName,".st_se_supplier_bo_transaction   select * from st_se_supplier_bo_transaction where transaction_id in (select transaction_id  from    st_lms_bo_transaction_master where  transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and transaction_type ='PURCHASE')");
	PREPARE stmt FROM @st_se_supplier_bo_transaction;
	EXECUTE stmt;
	SET @st_se_supplier_bo_transaction=CONCAT("delete from st_se_supplier_bo_transaction where transaction_id in (select transaction_id  from    st_lms_bo_transaction_master where  transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and transaction_type ='PURCHASE')");
	PREPARE stmt FROM @st_se_supplier_bo_transaction;
	EXECUTE stmt;
	SET @st_se_agent_retailer_transaction=CONCAT(" insert into ",archDBName,".st_se_agent_retailer_transaction select * from st_se_agent_retailer_transaction where transaction_id in (select transaction_id  from    st_lms_agent_transaction_master where   transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and transaction_type in ('SALE_RET','sale') )");
	PREPARE stmt FROM @st_se_agent_retailer_transaction;
	EXECUTE stmt;
	SET @st_se_agent_retailer_transaction=CONCAT("delete from st_se_agent_retailer_transaction where transaction_id in (select transaction_id  from    st_lms_agent_transaction_master where   transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and transaction_type in ('SALE_RET','sale') )");
	PREPARE stmt FROM @st_se_agent_retailer_transaction;
	EXECUTE stmt;
	SET @st_se_bo_agent_transaction=CONCAT(" insert into ",archDBName,".st_se_bo_agent_transaction    select * from st_se_bo_agent_transaction where transaction_id in (select transaction_id  from    st_lms_bo_transaction_master where   transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and transaction_type in ('SALE_RET','SALE') )");
	PREPARE stmt FROM @st_se_bo_agent_transaction;
	EXECUTE stmt;
	SET @st_se_bo_agent_transaction=CONCAT("delete from st_se_bo_agent_transaction where transaction_id in (select transaction_id  from    st_lms_bo_transaction_master where   transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and transaction_type in ('SALE_RET','SALE') )");
	PREPARE stmt FROM @st_se_bo_agent_transaction;
	EXECUTE stmt;
	SET @st_se_bo_pwt=CONCAT(" insert into ",archDBName,".st_se_bo_pwt    select * from st_se_bo_pwt where transaction_id in (select transaction_id  from    st_lms_bo_transaction_master where transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and transaction_type in('PWT_AUTO','PWT'))");
	PREPARE stmt FROM @st_se_bo_pwt;
	EXECUTE stmt;
	SET @st_se_bo_pwt=CONCAT("delete from st_se_bo_pwt where transaction_id in (select transaction_id  from    st_lms_bo_transaction_master where transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and transaction_type in('PWT_AUTO','PWT'))");
	PREPARE stmt FROM @st_se_bo_pwt;
	EXECUTE stmt;
	SET @st_se_direct_player_pwt=CONCAT(" insert into ",archDBName,".st_se_direct_player_pwt     select *  from st_se_direct_player_pwt where transaction_id in (select transaction_id  from    st_lms_agent_transaction_master where transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and   transaction_type='PWT_PLR' )");
	PREPARE stmt FROM @st_se_direct_player_pwt;
	EXECUTE stmt;
	SET @st_se_direct_player_pwt=CONCAT("delete from st_se_direct_player_pwt where transaction_id in (select transaction_id  from    st_lms_agent_transaction_master where transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and   transaction_type='PWT_PLR' )");
	PREPARE stmt FROM @st_se_direct_player_pwt;
	EXECUTE stmt;
END IF;
	SET @isSLE:=(SELECT IF(STATUS='ACTIVE',1,0) FROM st_lms_service_master WHERE service_code='SLE');
	IF(@isSLE=1) THEN 
		SET @sle_sale=CONCAT("insert into ",archDBName,".st_sle_ret_sale select * from st_sle_ret_sale where transaction_date>='",startDate,"' and transaction_date<='",endDate,"';");
		PREPARE stmt FROM @sle_sale;
	 	EXECUTE stmt;
		SET @sle_sale_del=CONCAT("delete sale from st_sle_ret_sale sale where transaction_date>='",startDate,"' and transaction_date<='",endDate,"';");
		PREPARE stmt FROM @sle_sale_del;
	 	EXECUTE stmt;
		SET @sle_ref=CONCAT("insert into ",archDBName,".st_sle_ret_sale_refund select * from st_sle_ret_sale_refund where transaction_id in (select transaction_id  from    st_lms_retailer_transaction_master where  transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and transaction_type in ('SLE_REFUND_CANCEL','SLE_REFUND_FAILED'))");
		PREPARE stmt FROM @sle_ref;
 		EXECUTE stmt;
		SET @sle_ref_del=CONCAT("delete sale from st_sle_ret_sale_refund sale inner join st_lms_retailer_transaction_master rtm on sale.transaction_id=rtm.transaction_id where rtm.transaction_date>='",startDate,"' and rtm.transaction_date<='",endDate,"' and transaction_type in ('SLE_REFUND_CANCEL','SLE_REFUND_FAILED')");
		PREPARE stmt FROM @sle_ref_del;
 		EXECUTE stmt;
       
		SET @sle_pwt_ret=CONCAT("insert into ",archDBName,".st_sle_ret_pwt select * from st_sle_ret_pwt where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and transaction_type='SLE_PWT')");
		PREPARE stmt FROM @sle_pwt_ret;
		EXECUTE stmt;
		SET @sle_pwt_ret_del=CONCAT("delete pwt from st_sle_ret_pwt pwt inner join st_lms_retailer_transaction_master rtm on pwt.transaction_id=rtm.transaction_id where rtm.transaction_date>='",startDate,"' and rtm.transaction_date<='",endDate,"' and transaction_type='SLE_PWT'");
		PREPARE stmt FROM @sle_pwt_ret_del;
		EXECUTE stmt;
		SET @sle_pwt_inv=CONCAT("insert into ",archDBName,".st_sle_pwt_inv (game_id, game_type_id, draw_id, ticket_nbr, pwt_amt, retailer_transaction_id, agent_transaction_id, bo_transaction_id, status, is_direct_plr) select inv.game_id, game_type_id, draw_id, ticket_nbr, pwt_amt, retailer_transaction_id, agent_transaction_id, bo_transaction_id, status, is_direct_plr from st_sle_pwt_inv inv inner join st_lms_retailer_transaction_master rtm on retailer_transaction_id=transaction_id where transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and transaction_type='SLE_PWT' union select game_id, game_type_id, draw_id, ticket_nbr, pwt_amt, retailer_transaction_id, agent_transaction_id, bo_transaction_id, status, is_direct_plr from st_sle_pwt_inv inner join st_lms_agent_transaction_master atm on agent_transaction_id=transaction_id where transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and transaction_type in ('SLE_PWT','SLE_PWT_AUTO','SLE_PWT_PLR') union select game_id, game_type_id, draw_id, ticket_nbr, pwt_amt, retailer_transaction_id, agent_transaction_id, bo_transaction_id, status, is_direct_plr from st_sle_pwt_inv inner join st_lms_bo_transaction_master btm on bo_transaction_id=transaction_id where transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and transaction_type in ('SLE_PWT_AUTO','SLE_PWT_PLR')");
		PREPARE stmt FROM @sle_pwt_inv;
		EXECUTE stmt;
		SET @sle_pwt_inv_del=CONCAT("delete pwt from st_sle_pwt_inv pwt inner join st_lms_retailer_transaction_master rtm on retailer_transaction_id=rtm.transaction_id where rtm.transaction_date>='",startDate,"' and rtm.transaction_date<='",endDate,"' and rtm.transaction_type='SLE_PWT'");
		PREPARE stmt FROM @sle_pwt_inv_del;
		EXECUTE stmt;
		SET @sle_pwt_inv_del=CONCAT("delete pwt from st_sle_pwt_inv pwt inner join st_lms_agent_transaction_master atm on agent_transaction_id=atm.transaction_id where atm.transaction_date>='",startDate,"' and atm.transaction_date<='",endDate,"' and atm.transaction_type in('SLE_PWT','SLE_PWT_AUTO','SLE_PWT_PLR')");
		PREPARE stmt FROM @sle_pwt_inv_del;
		EXECUTE stmt;
		SET @sle_pwt_inv_del=CONCAT("delete pwt from st_sle_pwt_inv pwt inner join st_lms_bo_transaction_master btm on bo_transaction_id=btm.transaction_id where btm.transaction_date>='",startDate,"' and btm.transaction_date<='",endDate,"' and btm.transaction_type in('SLE_PWT_AUTO','SLE_PWT_PLR')");
		PREPARE stmt FROM @sle_pwt_inv_del;
		EXECUTE stmt;
		SET @st_sle_approval_req_master=CONCAT("insert into ",archDBName,".st_sle_approval_req_master select * from st_sle_approval_req_master where approval_date>='",startDate,"' and approval_date<='",endDate,"'");
		PREPARE stmt FROM @st_sle_approval_req_master;
		EXECUTE stmt;
		SET @st_sle_approval_req_master_del=CONCAT("delete from st_sle_approval_req_master where approval_date>='",startDate,"' and approval_date<='",endDate,"'");
		PREPARE stmt FROM @st_sle_approval_req_master_del;
		EXECUTE stmt;
		SET @st_sle_bo_agent_pwt_comm_variance_history=CONCAT("insert into ",archDBName,".st_sle_bo_agent_pwt_comm_variance_history select * from st_sle_bo_agent_pwt_comm_variance_history where date_changed>='",startDate,"' and date_changed<='",endDate,"'");
		PREPARE stmt FROM @st_sle_bo_agent_pwt_comm_variance_history;
		EXECUTE stmt;
		SET @st_sle_bo_agent_pwt_comm_variance_history_del=CONCAT("delete from st_sle_bo_agent_pwt_comm_variance_history where date_changed>='",startDate,"' and date_changed<='",endDate,"'");
		PREPARE stmt FROM @st_sle_bo_agent_pwt_comm_variance_history_del;
		EXECUTE stmt;
		SET @st_sle_bo_agent_sale_comm_variance_history=CONCAT("insert into ",archDBName,".st_sle_bo_agent_sale_comm_variance_history select * from st_sle_bo_agent_sale_comm_variance_history where date_changed>='",startDate,"' and date_changed<='",endDate,"'");
		PREPARE stmt FROM @st_sle_bo_agent_sale_comm_variance_history;
		EXECUTE stmt;
		SET @st_sle_bo_agent_sale_comm_variance_history_del=CONCAT("delete from st_sle_bo_agent_sale_comm_variance_history where date_changed>='",startDate,"' and date_changed<='",endDate,"'");
		PREPARE stmt FROM @st_sle_bo_agent_sale_comm_variance_history_del;
		EXECUTE stmt;
		
		
		
		
		
		
		SET @st_sle_agt_sale=CONCAT("insert into ",archDBName,".st_sle_agt_sale select * from st_sle_agt_sale where transaction_id in (select transaction_id  from    st_lms_agent_transaction_master where   transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and transaction_type='SLE_SALE')");
		PREPARE stmt FROM @st_sle_agt_sale;
		EXECUTE stmt;
		SET @st_sle_agt_sale_del=CONCAT("delete from st_sle_agt_sale where transaction_id in (select transaction_id from st_lms_agent_transaction_master where transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and transaction_type='SLE_SALE')");
		PREPARE stmt FROM @st_sle_agt_sale_del;
		EXECUTE stmt;
		SET @st_sle_agt_pwt=CONCAT("insert into ",archDBName,".st_sle_agt_pwt select * from st_sle_agt_pwt where transaction_id in (select transaction_id from st_lms_agent_transaction_master where transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and transaction_type IN('SLE_PWT','SLE_PWT_AUTO','SLE_PWT_PLR'))");
		PREPARE stmt FROM @st_sle_agt_pwt;
		EXECUTE stmt;
		SET @st_sle_agt_pwt_del=CONCAT("delete from st_sle_agt_pwt where transaction_id in (select transaction_id from st_lms_agent_transaction_master where transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and transaction_type IN('SLE_PWT','SLE_PWT_AUTO','SLE_PWT_PLR'))");
		PREPARE stmt FROM @st_sle_agt_pwt_del;
		EXECUTE stmt;
		
		
		
		
		
		
		SET @st_sle_agt_sale_refund=CONCAT(" insert into ",archDBName,".st_sle_agt_sale_refund select * from st_sle_agt_sale_refund where transaction_id in (select transaction_id  from    st_lms_agent_transaction_master where   transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and transaction_type in ('SLE_REFUND_CANCEL','SLE_REFUND_FAILED'))");
		PREPARE stmt FROM @st_sle_agt_sale_refund;
		EXECUTE stmt;
		SET @st_sle_agt_sale_refund=CONCAT("delete from st_sle_agt_sale_refund where transaction_id in (select transaction_id  from    st_lms_agent_transaction_master where   transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and transaction_type in ('SLE_REFUND_CANCEL','SLE_REFUND_FAILED'))");
		PREPARE stmt FROM @st_sle_agt_sale_refund;
		EXECUTE stmt;
		SET @st_sle_bo_sale=CONCAT(" insert into ",archDBName,".st_sle_bo_sale select * from st_sle_bo_sale where transaction_id in (select transaction_id  from    st_lms_bo_transaction_master where   transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and transaction_type='SLE_SALE' )");
		PREPARE stmt FROM @st_sle_bo_sale;
		EXECUTE stmt;
		SET @st_sle_bo_sale=CONCAT("delete from st_sle_bo_sale where transaction_id in (select transaction_id  from    st_lms_bo_transaction_master where   transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and transaction_type='SLE_SALE' )");
		PREPARE stmt FROM @st_sle_bo_sale;
		EXECUTE stmt;
		SET @st_sle_bo_pwt=CONCAT("insert into ",archDBName,".st_sle_bo_pwt select * from st_sle_bo_pwt where transaction_id in (select transaction_id from st_lms_bo_transaction_master where transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and transaction_type in ('SLE_PWT_AUTO','SLE_PWT_PLR'))");
		PREPARE stmt FROM @st_sle_bo_pwt;
		EXECUTE stmt;
		SET @st_sle_bo_pwt_del=CONCAT("delete from st_sle_bo_pwt where transaction_id in (select transaction_id from st_lms_bo_transaction_master where transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and transaction_type in ('SLE_PWT_AUTO','SLE_PWT_PLR'))");
		PREPARE stmt FROM @st_sle_bo_pwt_del;
		EXECUTE stmt;
		SET @st_sle_bo_sale_refund=CONCAT("insert into ",archDBName,".st_sle_bo_sale_refund   select * from st_sle_bo_sale_refund where transaction_id in (select transaction_id  from    st_lms_bo_transaction_master where   transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and transaction_type in ('SLE_REFUND_CANCEL','SLE_REFUND_FAILED'))");
		PREPARE stmt FROM @st_sle_bo_sale_refund;
		EXECUTE stmt;
		SET @st_sle_bo_sale_refund=CONCAT("delete from st_sle_bo_sale_refund where transaction_id in (select transaction_id  from    st_lms_bo_transaction_master where   transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and transaction_type in ('SLE_REFUND_CANCEL','SLE_REFUND_FAILED'))");
		PREPARE stmt FROM @st_sle_bo_sale_refund;
		EXECUTE stmt;
		SET @st_sle_bo_direct_plr_pwt=CONCAT("insert into ",archDBName,".st_sle_bo_direct_plr_pwt select * from st_sle_bo_direct_plr_pwt where transaction_id in (select transaction_id from st_lms_bo_transaction_master where transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and transaction_type in ('SLE_PWT_AUTO','SLE_PWT_PLR'))");
		PREPARE stmt FROM @st_sle_bo_direct_plr_pwt;
		EXECUTE stmt;
		SET @st_sle_bo_direct_plr_pwt_del=CONCAT("delete from st_sle_bo_direct_plr_pwt where transaction_id in (select transaction_id from st_lms_bo_transaction_master where transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and transaction_type in ('SLE_PWT_AUTO','SLE_PWT_PLR'))");
		PREPARE stmt FROM @st_sle_bo_direct_plr_pwt_del;
		EXECUTE stmt;
	END IF;
SELECT 'SLE DONE';
	SET @isIW:=(SELECT IF(STATUS='ACTIVE',1,0) FROM st_lms_service_master WHERE service_code='IW');
	IF(@isIW=1) THEN 
		SET @iw_sale=CONCAT("insert into ",archDBName,".st_iw_ret_sale select * from st_iw_ret_sale where transaction_date>='",startDate,"' and transaction_date<='",endDate,"';");
		PREPARE stmt FROM @iw_sale;
	 	EXECUTE stmt;
		SET @iw_sale_del=CONCAT("delete sale from st_iw_ret_sale sale where transaction_date>='",startDate,"' and transaction_date<='",endDate,"';");
		PREPARE stmt FROM @iw_sale_del;
	 	EXECUTE stmt;
		SET @iw_ref=CONCAT("insert into ",archDBName,".st_iw_ret_sale_refund select * from st_iw_ret_sale_refund where transaction_id in (select transaction_id  from    st_lms_retailer_transaction_master where  transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and transaction_type in ('IW_REFUND_CANCEL'))");
		PREPARE stmt FROM @iw_ref;
 		EXECUTE stmt;
		SET @iw_ref_del=CONCAT("delete sale from st_iw_ret_sale_refund sale inner join st_lms_retailer_transaction_master rtm on sale.transaction_id=rtm.transaction_id where rtm.transaction_date>='",startDate,"' and rtm.transaction_date<='",endDate,"' and transaction_type in ('IW_REFUND_CANCEL')");
		PREPARE stmt FROM @iw_ref_del;
 		EXECUTE stmt;
       select 'iw1';
		SET @iw_pwt_ret=CONCAT("insert into ",archDBName,".st_iw_ret_pwt select * from st_iw_ret_pwt where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and transaction_type='IW_PWT')");
		PREPARE stmt FROM @iw_pwt_ret;
		EXECUTE stmt;
select 'iwq';
		SET @iw_pwt_ret_del=CONCAT("delete pwt from st_iw_ret_pwt pwt inner join st_lms_retailer_transaction_master rtm on pwt.transaction_id=rtm.transaction_id where rtm.transaction_date>='",startDate,"' and rtm.transaction_date<='",endDate,"' and transaction_type='IW_PWT'");
		PREPARE stmt FROM @iw_pwt_ret_del;
		EXECUTE stmt;
select 'iww';
		SET @iw_pwt_inv=CONCAT("insert into ",archDBName,".st_iw_pwt_inv (game_id,   ticket_nbr, pwt_amt, retailer_transaction_id, agent_transaction_id, bo_transaction_id, status, is_direct_plr) select inv.game_id,   ticket_nbr, pwt_amt, retailer_transaction_id, agent_transaction_id, bo_transaction_id, status, is_direct_plr from st_iw_pwt_inv inv inner join st_lms_retailer_transaction_master rtm on retailer_transaction_id=transaction_id where transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and transaction_type='IW_PWT' union select game_id,   ticket_nbr, pwt_amt, retailer_transaction_id, agent_transaction_id, bo_transaction_id, status, is_direct_plr from st_iw_pwt_inv inner join st_lms_agent_transaction_master atm on agent_transaction_id=transaction_id where transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and transaction_type in ('IW_PWT') union select game_id,   ticket_nbr, pwt_amt, retailer_transaction_id, agent_transaction_id, bo_transaction_id, status, is_direct_plr from st_iw_pwt_inv inner join st_lms_bo_transaction_master btm on bo_transaction_id=transaction_id where transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and transaction_type in ('IW_PWT')");
		PREPARE stmt FROM @iw_pwt_inv;
select @iw_pwt_inv;
		EXECUTE stmt;
       select 'iw2';
		SET @iw_pwt_inv_del=CONCAT("delete pwt from st_iw_pwt_inv pwt inner join st_lms_retailer_transaction_master rtm on retailer_transaction_id=rtm.transaction_id where rtm.transaction_date>='",startDate,"' and rtm.transaction_date<='",endDate,"' and rtm.transaction_type='IW_PWT'");
		PREPARE stmt FROM @iw_pwt_inv_del;
		EXECUTE stmt;
	
			SET @st_iw_bo_direct_plr_pwt=CONCAT("insert into ",archDBName,".st_iw_bo_direct_plr_pwt select * from st_iw_bo_direct_plr_pwt where transaction_id in (select transaction_id from st_lms_bo_transaction_master where transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and transaction_type in ('IW_PWT_AUTO','IW_PWT_PLR'))");
		PREPARE stmt FROM @st_iw_bo_direct_plr_pwt;
		EXECUTE stmt;
		SET @st_iw_bo_direct_plr_pwt_del=CONCAT("delete from st_iw_bo_direct_plr_pwt where transaction_id in (select transaction_id from st_lms_bo_transaction_master where transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and transaction_type in ('IW_PWT_AUTO','IW_PWT_PLR'))");
		PREPARE stmt FROM @st_iw_bo_direct_plr_pwt_del;
		EXECUTE stmt; 
	SET @st_iw_agent_direct_plr_pwt=CONCAT("insert into ",archDBName,".st_iw_agent_direct_plr_pwt select * from st_iw_agent_direct_plr_pwt where transaction_id in (select transaction_id from st_lms_agent_transaction_master where transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and transaction_type in ('IW_PWT_AUTO','IW_PWT_PLR'))");
		PREPARE stmt FROM @st_iw_bo_direct_plr_pwt;
		EXECUTE stmt;
		SET @st_iw_agent_direct_plr_pwt_del=CONCAT("delete from st_iw_agent_direct_plr_pwt where transaction_id in (select transaction_id from st_lms_agent_transaction_master where transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and transaction_type in ('IW_PWT','IW_PWT_AUTO'))");
		PREPARE stmt FROM @st_iw_agent_direct_plr_pwt_del;
		EXECUTE stmt; 
	END IF;
SELECT 'IW DONE';
SET @isVS:=(SELECT IF(STATUS='ACTIVE',1,0) FROM st_lms_service_master WHERE service_code='VS');
	IF(@isVS=1) THEN 
		SET @vs_sale=CONCAT("insert into ",archDBName,".st_vs_ret_sale select * from st_vs_ret_sale where transaction_date>='",startDate,"' and transaction_date<='",endDate,"';");
		PREPARE stmt FROM @vs_sale;
	 	EXECUTE stmt;
		SET @vs_sale_del=CONCAT("delete sale from st_vs_ret_sale sale where transaction_date>='",startDate,"' and transaction_date<='",endDate,"';");
		PREPARE stmt FROM @vs_sale_del;
	 	EXECUTE stmt;
		SET @vs_ref=CONCAT("insert into ",archDBName,".st_vs_ret_sale_refund select * from st_vs_ret_sale_refund where transaction_date>='",startDate,"' and transaction_date<='",endDate,"' ");
		PREPARE stmt FROM @vs_ref;
 		EXECUTE stmt;
		SET @vs_ref_del=CONCAT("delete sale from st_vs_ret_sale_refund sale where transaction_date>='",startDate,"' and transaction_date<='",endDate,"'");
		PREPARE stmt FROM @vs_ref_del;
 		EXECUTE stmt;
		SET @vs_pwt_ret=CONCAT("insert into ",archDBName,".st_vs_ret_pwt select * from st_vs_ret_pwt where transaction_date>='",startDate,"' and transaction_date<='",endDate,"' ");
		PREPARE stmt FROM @vs_pwt_ret;
		EXECUTE stmt;

		SET @vs_pwt_ret_del=CONCAT("delete pwt from st_vs_ret_pwt pwt where transaction_date>='",startDate,"' and transaction_date<='",endDate,"' ");
		PREPARE stmt FROM @vs_pwt_ret_del;
		EXECUTE stmt;

		SET @vs_pwt_inv=CONCAT("insert into ",archDBName,".st_vs_pwt_inv (game_id,   ticket_nbr, pwt_amt, retailer_transaction_id, agent_transaction_id, bo_transaction_id, status, is_direct_plr) select inv.game_id,   ticket_nbr, pwt_amt, retailer_transaction_id, agent_transaction_id, bo_transaction_id, status, is_direct_plr from st_vs_pwt_inv inv inner join st_lms_retailer_transaction_master rtm on inv.retailer_transaction_id=rtm.transaction_id where rtm.transaction_date>='",startDate,"' and rtm.transaction_date<='",endDate,"' and rtm.transaction_type='VS_PWT'");
		PREPARE stmt FROM @vs_pwt_inv;

		EXECUTE stmt;

		SET @vs_pwt_inv_del=CONCAT("delete pwt from st_vs_pwt_inv pwt inner join st_lms_retailer_transaction_master rtm on retailer_transaction_id=rtm.transaction_id where rtm.transaction_date>='",startDate,"' and rtm.transaction_date<='",endDate,"' and rtm.transaction_type='VS_PWT'");
		PREPARE stmt FROM @vs_pwt_inv_del;
		EXECUTE stmt;
	
		END IF;
SELECT 'VS DONE';
SET @isMgmt:=(SELECT IF(STATUS='ACTIVE',1,0) FROM st_lms_service_master WHERE service_code='MGMT');
IF(@isMgmt=1) THEN
	SET @st_lms_tp_system_txn_mapping=CONCAT("INSERT INTO ",archDBName,".st_lms_tp_system_txn_mapping SELECT a.* FROM st_lms_tp_system_txn_mapping a INNER JOIN st_lms_tp_txn_details b ON a.lms_txn_id=b.retailer_trans_id WHERE transaction_date>='",startDate,"' AND transaction_date<='",endDate,"'");
	PREPARE stmt FROM @st_lms_tp_system_txn_mapping;
	EXECUTE stmt;
	
	
	
	SET @st_lms_tp_system_txn_mapping_sel=CONCAT("SELECT @min_txn_id:=ifnull(MIN(min_txn_id),0), @max_txn_id:=ifnull(MAX(max_txn_id),0) FROM (SELECT MIN(transaction_id) min_txn_id, MAX(transaction_id) max_txn_id FROM st_lms_bo_transaction_master WHERE transaction_date>='",startDate,"' AND transaction_date<='",endDate,"' UNION ALL SELECT MIN(transaction_id) min_txn_id, MAX(transaction_id) max_txn_id FROM st_lms_agent_transaction_master WHERE transaction_date>='",startDate,"' AND transaction_date<='",endDate,"' UNION ALL SELECT MIN(transaction_id) min_txn_id, MAX(transaction_id) max_txn_id FROM st_lms_retailer_transaction_master WHERE transaction_date>='",startDate,"' AND transaction_date<='",endDate,"')aa;");
	PREPARE stmt FROM @st_lms_tp_system_txn_mapping_sel;
	EXECUTE stmt;
	SET @st_lms_tp_system_txn_mapping_del=CONCAT("DELETE FROM st_lms_tp_system_txn_mapping WHERE lms_txn_id>=",@min_txn_id," AND lms_txn_id<=",@max_txn_id,";");
	PREPARE stmt FROM @st_lms_tp_system_txn_mapping_del;
	EXECUTE stmt;
	SET @st_lms_tp_txn_details=CONCAT("insert into ",archDBName,".st_lms_tp_txn_details select * from st_lms_tp_txn_details where transaction_date>='",startDate,"' and transaction_date<='",endDate,"'");
	PREPARE stmt FROM @st_lms_tp_txn_details;
	EXECUTE stmt;
	SET @st_lms_tp_txn_details_del=CONCAT("delete from st_lms_tp_txn_details where transaction_date>='",startDate,"' and transaction_date<='",endDate,"'");
	PREPARE stmt FROM @st_lms_tp_txn_details_del;
	EXECUTE stmt;
	SET @st_lms_agent_sale_chq=CONCAT(" insert into ",archDBName,".st_lms_agent_sale_chq select * from st_lms_agent_sale_chq where transaction_id in (select transaction_id  from st_lms_agent_transaction_master where  transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and transaction_type in ('CHEQUE','CHQ_BOUNCE'))");
	PREPARE stmt FROM @st_lms_agent_sale_chq;
	EXECUTE stmt;
	SET @st_lms_agent_sale_chq=CONCAT("delete from st_lms_agent_sale_chq where transaction_id in (select transaction_id  from st_lms_agent_transaction_master where  transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and transaction_type in ('CHEQUE','CHQ_BOUNCE'))");
	PREPARE stmt FROM @st_lms_agent_sale_chq;
	EXECUTE stmt;
	SET @st_lms_agent_debit_note=CONCAT(" insert into ",archDBName,".st_lms_agent_debit_note select * from st_lms_agent_debit_note where transaction_id in(select transaction_id  from    st_lms_agent_transaction_master where transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and transaction_type in ('DR_NOTE','DR_NOTE_CASH'))");
	PREPARE stmt FROM @st_lms_agent_debit_note;
	EXECUTE stmt;
SET @st_lms_agent_debit_note=CONCAT("delete from st_lms_agent_debit_note where transaction_id in(select transaction_id  from    st_lms_agent_transaction_master where transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and transaction_type in ('DR_NOTE','DR_NOTE_CASH'))");
	PREPARE stmt FROM @st_lms_agent_debit_note;
	EXECUTE stmt;
	SET @st_lms_agent_govt_transaction=CONCAT(" insert into ",archDBName,".st_lms_agent_govt_transaction select * from st_lms_agent_govt_transaction where transaction_id in (select transaction_id  from    st_lms_agent_transaction_master where  transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and transaction_type in ('VAT','TDS','GOVT_COMM'))");
	PREPARE stmt FROM @st_lms_agent_govt_transaction;
	EXECUTE stmt;
SET @st_lms_agent_govt_transaction=CONCAT("delete from st_lms_agent_govt_transaction where transaction_id in (select transaction_id  from    st_lms_agent_transaction_master where  transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and transaction_type in ('VAT','TDS','GOVT_COMM'))");
	PREPARE stmt FROM @st_lms_agent_govt_transaction;
	EXECUTE stmt;
	SET @st_lms_agent_cash_transaction=CONCAT(" insert into ",archDBName,".st_lms_agent_cash_transaction select * from st_lms_agent_cash_transaction where transaction_id in (select transaction_id  from    st_lms_agent_transaction_master where transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and transaction_type= ('CASH'))");
	PREPARE stmt FROM @st_lms_agent_cash_transaction;
	EXECUTE stmt;
	SET @st_lms_agent_cash_transaction=CONCAT("delete from st_lms_agent_cash_transaction where transaction_id in (select transaction_id  from    st_lms_agent_transaction_master where transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and transaction_type= ('CASH'))");
	PREPARE stmt FROM @st_lms_agent_cash_transaction;
	EXECUTE stmt;
	SET @st_lms_agent_credit_note=CONCAT(" insert into ",archDBName,".st_lms_agent_credit_note select * from st_lms_agent_credit_note where transaction_id in (select transaction_id  from    st_lms_agent_transaction_master where  transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and transaction_type='CR_NOTE_CASH' )");
	PREPARE stmt FROM @st_lms_agent_credit_note;
	EXECUTE stmt;
	SET @st_lms_agent_credit_note=CONCAT("delete from st_lms_agent_credit_note where transaction_id in (select transaction_id  from    st_lms_agent_transaction_master where  transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and transaction_type='CR_NOTE_CASH' )");
	PREPARE stmt FROM @st_lms_agent_credit_note;
	EXECUTE stmt;
	SET @st_lms_bo_sale_chq=CONCAT(" insert into ",archDBName,".st_lms_bo_sale_chq select * from st_lms_bo_sale_chq where transaction_id in (select transaction_id  from    st_lms_bo_transaction_master where  transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and transaction_type in ('CHEQUE','CHQ_BOUNCE'))");
	PREPARE stmt FROM @st_lms_bo_sale_chq;
	EXECUTE stmt;
SET @st_lms_bo_sale_chq=CONCAT("delete from st_lms_bo_sale_chq where transaction_id in (select transaction_id  from    st_lms_bo_transaction_master where  transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and transaction_type in ('CHEQUE','CHQ_BOUNCE'))");
	PREPARE stmt FROM @st_lms_bo_sale_chq;
	EXECUTE stmt;
	SET @st_lms_bo_govt_transaction=CONCAT(" insert into ",archDBName,".st_lms_bo_govt_transaction select * from st_lms_bo_govt_transaction where transaction_id in (select transaction_id  from st_lms_bo_transaction_master where  transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and transaction_type in ('VAT','TDS','GOVT_COMM'))");
	PREPARE stmt FROM @st_lms_bo_govt_transaction;
	EXECUTE stmt;
	SET @st_lms_bo_govt_transaction=CONCAT("delete from st_lms_bo_govt_transaction where transaction_id in (select transaction_id  from st_lms_bo_transaction_master where  transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and transaction_type in ('VAT','TDS','GOVT_COMM'))");
	PREPARE stmt FROM @st_lms_bo_govt_transaction;
	EXECUTE stmt;
	SET @st_lms_bo_cash_transaction=CONCAT(" insert into ",archDBName,".st_lms_bo_cash_transaction select * from st_lms_bo_cash_transaction where transaction_id in (select transaction_id  from st_lms_bo_transaction_master where transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and transaction_type= ('CASH'))");
	PREPARE stmt FROM @st_lms_bo_cash_transaction;
	EXECUTE stmt;
SET @st_lms_bo_cash_transaction=CONCAT("delete from st_lms_bo_cash_transaction where transaction_id in (select transaction_id  from st_lms_bo_transaction_master where transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and transaction_type= ('CASH'))");
	PREPARE stmt FROM @st_lms_bo_cash_transaction;
	EXECUTE stmt;
	SET @st_lms_bo_bank_deposit_transaction=CONCAT(" insert into ",archDBName,".st_lms_bo_bank_deposit_transaction select * from st_lms_bo_bank_deposit_transaction where transaction_id in (select transaction_id  from st_lms_bo_transaction_master where transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and transaction_type='BANK_DEPOSIT')");
	PREPARE stmt FROM @st_lms_bo_bank_deposit_transaction;
	EXECUTE stmt;
	SET @st_lms_bo_bank_deposit_transaction=CONCAT("delete from st_lms_bo_bank_deposit_transaction where transaction_id in (select transaction_id  from st_lms_bo_transaction_master where transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and transaction_type='BANK_DEPOSIT')");
	PREPARE stmt FROM @st_lms_bo_bank_deposit_transaction;
	EXECUTE stmt;
	SET @st_lms_agent_bank_deposit_transaction=CONCAT(" insert into ",archDBName,".st_lms_agent_bank_deposit_transaction select * from st_lms_agent_bank_deposit_transaction where transaction_id in (select transaction_id  from st_lms_agent_transaction_master where transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and transaction_type='BANK_DEPOSIT')");
	PREPARE stmt FROM @st_lms_agent_bank_deposit_transaction;
	EXECUTE stmt;
	SET @st_lms_agent_bank_deposit_transaction=CONCAT("delete from st_lms_agent_bank_deposit_transaction where transaction_id in (select transaction_id  from st_lms_agent_transaction_master where transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and transaction_type='BANK_DEPOSIT')");
	PREPARE stmt FROM @st_lms_agent_bank_deposit_transaction;
	EXECUTE stmt;
	SET @st_lms_bo_credit_note=CONCAT("insert into ",archDBName,".st_lms_bo_credit_note select * from st_lms_bo_credit_note where transaction_id in (select transaction_id  from st_lms_bo_transaction_master where  transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and transaction_type='CR_NOTE_CASH')");
	PREPARE stmt FROM @st_lms_bo_credit_note;
	EXECUTE stmt;
SET @st_lms_bo_credit_note=CONCAT("delete from st_lms_bo_credit_note where transaction_id in (select transaction_id  from st_lms_bo_transaction_master where  transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and transaction_type='CR_NOTE_CASH')");
	PREPARE stmt FROM @st_lms_bo_credit_note;
	EXECUTE stmt;
	SET @st_lms_bo_debit_note=CONCAT("insert into ",archDBName,".st_lms_bo_debit_note select * from st_lms_bo_debit_note where transaction_id in(select transaction_id  from st_lms_bo_transaction_master where transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and transaction_type in ('DR_NOTE','DR_NOTE_CASH'))");
	PREPARE stmt FROM @st_lms_bo_debit_note;
	EXECUTE stmt;
SET @st_lms_bo_debit_note=CONCAT("delete from st_lms_bo_debit_note where transaction_id in(select transaction_id  from st_lms_bo_transaction_master where transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and transaction_type in ('DR_NOTE','DR_NOTE_CASH'))");
	PREPARE stmt FROM @st_lms_bo_debit_note;
	EXECUTE stmt;
	
	SET @temp_retailer_id=CONCAT("insert into temp_arch_trans_retailer (trans_id) select transaction_id from st_lms_retailer_transaction_master where transaction_date>='",startDate,"' and transaction_date<='",endDate,"'");
	PREPARE stmt FROM @temp_retailer_id;
	EXECUTE stmt;
	SET @temp_agent_id=CONCAT("insert into temp_arch_trans_agent (trans_id) select transaction_id from st_lms_agent_transaction_master where transaction_date>='",startDate,"' and transaction_date<='",endDate,"'");
	PREPARE stmt FROM @temp_agent_id;
	EXECUTE stmt;
	SET @temp_bo_id=CONCAT("insert into temp_arch_trans_bo (trans_id) select transaction_id from st_lms_bo_transaction_master where transaction_date>='",startDate,"' and transaction_date<='",endDate,"'");
	PREPARE stmt FROM @temp_bo_id;
	EXECUTE stmt;
	SET @temp_agent_receipt_id=CONCAT("insert into temp_arch_rec_agent (rec_id) select distinct receipt_id from st_lms_agent_receipts_trn_mapping inner join temp_arch_trans_agent on transaction_id=trans_id");
	PREPARE stmt FROM @temp_agent_receipt_id;
	EXECUTE stmt;
	
	SET @temp_bo_receipt_id=CONCAT("insert into temp_arch_rec_bo (rec_id) select distinct receipt_id from st_lms_bo_receipts_trn_mapping inner join temp_arch_trans_bo on transaction_id=trans_id");
	PREPARE stmt FROM @temp_bo_receipt_id;
	EXECUTE stmt; 
	
	SET @st_lms_ret_transaction_master=CONCAT(" insert into ",archDBName,".st_lms_retailer_transaction_master select * from st_lms_retailer_transaction_master where transaction_date>='",startDate,"' and transaction_date<='",endDate,"' ");
	PREPARE stmt FROM @st_lms_ret_transaction_master;
	EXECUTE stmt;
	SET @st_lms_ret_transaction_master=CONCAT("delete from st_lms_retailer_transaction_master where transaction_date>='",startDate,"' and transaction_date<='",endDate,"' ");
	PREPARE stmt FROM @st_lms_ret_transaction_master;
	EXECUTE stmt;
	SET @st_lms_agent_receipts_trn_mapping=CONCAT("insert into ",archDBName,".st_lms_agent_receipts_trn_mapping (receipt_id,transaction_id) select receipt_id,transaction_id from st_lms_agent_receipts_trn_mapping inner join temp_arch_rec_agent on receipt_id=rec_id");
	PREPARE stmt FROM @st_lms_agent_receipts_trn_mapping;
	EXECUTE stmt;
	SET @st_lms_agent_receipts_trn_mapping=CONCAT("delete st_lms_agent_receipts_trn_mapping  from st_lms_agent_receipts_trn_mapping inner join temp_arch_rec_agent on receipt_id=rec_id");
	PREPARE stmt FROM @st_lms_agent_receipts_trn_mapping;
	EXECUTE stmt;
	SET @st_lms_agent_receipts=CONCAT("insert into ",archDBName,".st_lms_agent_receipts (receipt_id, receipt_type, agent_org_id, party_id, party_type, generated_id) select  receipt_id, receipt_type, agent_org_id, party_id, party_type, generated_id from st_lms_agent_receipts inner join temp_arch_rec_agent on receipt_id=rec_id");
	PREPARE stmt FROM @st_lms_agent_receipts;
	EXECUTE stmt;
	SET @st_lms_agent_receipts=CONCAT("delete st_lms_agent_receipts from st_lms_agent_receipts inner join temp_arch_rec_agent on receipt_id=rec_id");
	PREPARE stmt FROM @st_lms_agent_receipts;
	EXECUTE stmt;
	SET @st_lms_receipts_master1=CONCAT("insert into ",archDBName,".st_lms_receipts_master (receipt_id,user_type) select receipt_id,user_type from st_lms_receipts_master inner join temp_arch_rec_agent on receipt_id=rec_id");
	PREPARE stmt FROM @st_lms_receipts_master1;
	EXECUTE stmt;
	SET @st_lms_receipts_master1=CONCAT("delete st_lms_receipts_master from st_lms_receipts_master inner join temp_arch_rec_agent on receipt_id=rec_id");
	PREPARE stmt FROM @st_lms_receipts_master1;
	EXECUTE stmt;
	SET @st_lms_agent_transaction_master=CONCAT("insert into ",archDBName,".st_lms_agent_transaction_master select * from st_lms_agent_transaction_master where transaction_date>='",startDate,"' and transaction_date<='",endDate,"'");
	PREPARE stmt FROM @st_lms_agent_transaction_master;
	EXECUTE stmt;
	SET @st_lms_agent_transaction_master=CONCAT("delete from st_lms_agent_transaction_master where transaction_date>='",startDate,"' and transaction_date<='",endDate,"'");
	PREPARE stmt FROM @st_lms_agent_transaction_master;
	EXECUTE stmt;
	SET @st_lms_bo_receipts_trn_mapping=CONCAT("insert into ",archDBName,".st_lms_bo_receipts_trn_mapping (receipt_id,transaction_id) select receipt_id,transaction_id from st_lms_bo_receipts_trn_mapping inner join temp_arch_rec_bo on receipt_id=rec_id");
	PREPARE stmt FROM @st_lms_bo_receipts_trn_mapping;
	EXECUTE stmt;
	SET @st_lms_bo_receipts_trn_mapping=CONCAT("delete st_lms_bo_receipts_trn_mapping from st_lms_bo_receipts_trn_mapping inner join temp_arch_rec_bo on receipt_id=rec_id");
	PREPARE stmt FROM @st_lms_bo_receipts_trn_mapping;
	EXECUTE stmt;
 
	SET @st_lms_bo_receipts=CONCAT("insert into ",archDBName,".st_lms_bo_receipts (receipt_id, receipt_type, party_id, party_type, generated_id) select  receipt_id, receipt_type, party_id, party_type, generated_id from st_lms_bo_receipts inner join temp_arch_rec_bo on receipt_id=rec_id");
	PREPARE stmt FROM @st_lms_bo_receipts;
	EXECUTE stmt;
	SET @st_lms_bo_receipts=CONCAT("delete st_lms_bo_receipts from st_lms_bo_receipts inner join temp_arch_rec_bo on receipt_id=rec_id");
	PREPARE stmt FROM @st_lms_bo_receipts;
	EXECUTE stmt;
	SET @st_lms_receipts_master2=CONCAT("insert into ",archDBName,".st_lms_receipts_master (receipt_id,user_type) select receipt_id,user_type from st_lms_receipts_master inner join temp_arch_rec_bo on receipt_id=rec_id");
	PREPARE stmt FROM @st_lms_receipts_master2;
	EXECUTE stmt;
	SET @st_lms_receipts_master2=CONCAT("delete st_lms_receipts_master from st_lms_receipts_master inner join temp_arch_rec_bo on receipt_id=rec_id");
	PREPARE stmt FROM @st_lms_receipts_master2;
	EXECUTE stmt;
	SET @st_lms_bo_transaction_master=CONCAT("insert into ",archDBName,".st_lms_bo_transaction_master select * from st_lms_bo_transaction_master where   transaction_date>='",startDate,"' and transaction_date<='",endDate,"'");
	PREPARE stmt FROM @st_lms_bo_transaction_master;
	EXECUTE stmt;
	SET @st_lms_bo_transaction_master=CONCAT("delete from st_lms_bo_transaction_master where   transaction_date>='",startDate,"' and transaction_date<='",endDate,"'");
	PREPARE stmt FROM @st_lms_bo_transaction_master;
	EXECUTE stmt;
	SET @st_lms_transaction_master=CONCAT("insert into ",archDBName,".st_lms_transaction_master (transaction_id, user_type, service_code, interface) select transaction_id, user_type, service_code, interface from st_lms_transaction_master inner join (select trans_id from temp_arch_trans_bo union all select trans_id from temp_arch_trans_agent union all select trans_id from temp_arch_trans_retailer) a on transaction_id=trans_id");
	PREPARE stmt FROM @st_lms_transaction_master;
	EXECUTE stmt;
	SET @st_lms_transaction_master=CONCAT("delete st_lms_transaction_master from st_lms_transaction_master inner join (select trans_id from temp_arch_trans_bo union all select trans_id from temp_arch_trans_agent union all select trans_id from temp_arch_trans_retailer) a on transaction_id=trans_id");
	PREPARE stmt FROM @st_lms_transaction_master;
	EXECUTE stmt;
	SET @agtCurBalHistory=CONCAT("insert into st_lms_agent_current_balance_history_arch select * from st_lms_agent_current_balance_history where transaction_date>='",startDate,"' and transaction_date<='",endDate,"'");
	PREPARE stmt FROM @agtCurBalHistory;
	EXECUTE stmt;
	SET @boCurBalHistory=CONCAT("insert into st_lms_bo_current_balance_history_arch select * from st_lms_bo_current_balance_history where transaction_date>='",startDate,"' and transaction_date<='",endDate,"'");
	PREPARE stmt FROM @boCurBalHistory;
	EXECUTE stmt;
		
	SET @agtLedger=CONCAT(" insert into ",archDBName,".st_lms_agent_ledger select * from st_lms_agent_ledger where transaction_date>='",startDate,"' and transaction_date<='",endDate,"'");
	PREPARE stmt FROM @agtLedger;
	EXECUTE stmt;
	SET @agtLedger=CONCAT("delete from st_lms_agent_ledger where transaction_date>='",startDate,"' and transaction_date<='",endDate,"'");
	PREPARE stmt FROM @agtLedger;
	EXECUTE stmt;
	SET @agtCurBalHistory=CONCAT(" insert into ",archDBName,".st_lms_agent_current_balance_history select * from st_lms_agent_current_balance_history where transaction_date>='",startDate,"' and transaction_date<='",endDate,"'");
	PREPARE stmt FROM @agtCurBalHistory;
	EXECUTE stmt;
	SET @agtCurBalHistory=CONCAT("delete from st_lms_agent_current_balance_history where transaction_date>='",startDate,"' and transaction_date<='",endDate,"'");
	PREPARE stmt FROM @agtCurBalHistory;
	EXECUTE stmt;
	SET @boLedger=CONCAT(" insert into ",archDBName,".st_lms_bo_ledger select * from st_lms_bo_ledger where transaction_date>='",startDate,"' and transaction_date<='",endDate,"'");
	PREPARE stmt FROM @boLedger;
	EXECUTE stmt;
	SET @boLedger=CONCAT("delete from st_lms_bo_ledger where transaction_date>='",startDate,"' and transaction_date<='",endDate,"'");
	PREPARE stmt FROM @boLedger;
	EXECUTE stmt;
	
	SET @boCurBalHistory=CONCAT(" insert into ",archDBName,".st_lms_bo_current_balance_history select * from st_lms_bo_current_balance_history where transaction_date>='",startDate,"' and transaction_date<='",endDate,"'");
	PREPARE stmt FROM @boCurBalHistory;
	EXECUTE stmt;
	SET @boCurBalHistory=CONCAT("delete from st_lms_bo_current_balance_history where transaction_date>='",startDate,"' and transaction_date<='",endDate,"'");
	PREPARE stmt FROM @boCurBalHistory;
	EXECUTE stmt;
	SET @st_lms_organization_master_history=CONCAT("insert into ",archDBName,".st_lms_organization_master_history select * from st_lms_organization_master_history where change_time>='",startDate,"' and change_time<='",endDate,"'");
	PREPARE stmt FROM @st_lms_organization_master_history;
	EXECUTE stmt;
	SET @st_lms_organization_master_history_del=CONCAT("delete from st_lms_organization_master_history where change_time>='",startDate,"' and change_time<='",endDate,"'");
	PREPARE stmt FROM @st_lms_organization_master_history_del;
	EXECUTE stmt;
	SET @st_lms_password_history=CONCAT("insert into ",archDBName,".st_lms_password_history select * from st_lms_password_history where date_changed>='",startDate,"' and date_changed<='",endDate,"'");
	PREPARE stmt FROM @st_lms_password_history;
	EXECUTE stmt;
	SET @st_lms_password_history_del=CONCAT("delete from st_lms_password_history where date_changed>='",startDate,"' and date_changed<='",endDate,"'");
	PREPARE stmt FROM @st_lms_password_history_del;
	EXECUTE stmt;
	SET @st_lms_rg_org_daily_tx_history=CONCAT("insert into ",archDBName,".st_lms_rg_org_daily_tx_history select * from st_lms_rg_org_daily_tx_history where date>='",startDate,"' and date<='",endDate,"'");
	PREPARE stmt FROM @st_lms_rg_org_daily_tx_history;
	EXECUTE stmt;
	SET @st_lms_rg_org_daily_tx_history_del=CONCAT("delete from st_lms_rg_org_daily_tx_history where date>='",startDate,"' and date<='",endDate,"'");
	PREPARE stmt FROM @st_lms_rg_org_daily_tx_history_del;
	EXECUTE stmt;
	SET @st_lms_rg_org_weakly_tx_history=CONCAT("insert into ",archDBName,".st_lms_rg_org_weakly_tx_history select * from st_lms_rg_org_weakly_tx_history where enddate>='",startDate,"' and enddate<='",endDate,"'");
	PREPARE stmt FROM @st_lms_rg_org_weakly_tx_history;
	EXECUTE stmt;
	SET @st_lms_rg_org_weakly_tx_history_del=CONCAT("delete from st_lms_rg_org_weakly_tx_history where enddate>='",startDate,"' and enddate<='",endDate,"'");
	PREPARE stmt FROM @st_lms_rg_org_weakly_tx_history_del;
	EXECUTE stmt;
	SET @st_lms_cl_xcl_update_history=CONCAT("insert into ",archDBName,".st_lms_cl_xcl_update_history select * from st_lms_cl_xcl_update_history where date_time>='",startDate,"' and date_time<='",endDate,"'");
	PREPARE stmt FROM @st_lms_cl_xcl_update_history;
	EXECUTE stmt;
	SET @st_lms_cl_xcl_update_history_del=CONCAT("delete from st_lms_cl_xcl_update_history where date_time>='",startDate,"' and date_time<='",endDate,"'");
	PREPARE stmt FROM @st_lms_cl_xcl_update_history_del;
	EXECUTE stmt;
END IF;
SELECT 'DONE';
 END      
#
CREATE      PROCEDURE `insDelHistoryData`(sourceDB VARCHAR(50), targetDB VARCHAR(50), startDate TIMESTAMP, endDate TIMESTAMP)
BEGIN

	
	DECLARE done INT DEFAULT 0;
	DECLARE gameId INT DEFAULT 0;
	DECLARE gameCur CURSOR FOR SELECT game_id FROM st_dg_game_master WHERE game_status='OPEN';
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;
	OPEN gameCur;
	read_loop: LOOP
		FETCH gameCur INTO gameId;
		IF done THEN
			LEAVE read_loop;
		END IF;
		SET @Qry = CONCAT("INSERT INTO ",targetDB,".st_dg_printed_tickets_",gameId," (retailer_org_id, ticket_nbr, channel, notification_time, action_name) SELECT retailer_org_id, ticket_nbr, channel, notification_time, action_name FROM ",sourceDB,".st_dg_printed_tickets_",gameId," WHERE DATE(notification_time)>='",startDate,"' AND DATE(notification_time)<='",endDate,"';");
		PREPARE stmt FROM @Qry;
		EXECUTE stmt;
		DEALLOCATE PREPARE stmt;

		SET @Qry = CONCAT("DELETE FROM ",sourceDB,".st_dg_printed_tickets_",gameId," WHERE DATE(notification_time)>='",startDate,"' AND DATE(notification_time)<='",endDate,"';");
		PREPARE stmt FROM @Qry;
		EXECUTE stmt;
		DEALLOCATE PREPARE stmt;
	END LOOP;
	CLOSE gameCur;

	
	SET @Qry = CONCAT("INSERT INTO ",targetDB,".st_dg_agt_direct_plr_pwt (task_id, agent_user_id, agent_org_id, draw_id, panel_id, transaction_id, transaction_date, game_id, player_id, pwt_amt, tax_amt, net_amt, payment_type, cheque_nbr, cheque_date, drawee_bank, issuing_party_name, pwt_claim_status, agt_claim_comm) SELECT task_id, agent_user_id, agent_org_id, draw_id, panel_id, transaction_id, transaction_date, game_id, player_id, pwt_amt, tax_amt, net_amt, payment_type, cheque_nbr, cheque_date, drawee_bank, issuing_party_name, pwt_claim_status, agt_claim_comm FROM ",sourceDB,".st_dg_agt_direct_plr_pwt WHERE DATE(transaction_date)>='",startDate,"' AND DATE(transaction_date)<='",endDate,"';");
	PREPARE stmt FROM @Qry;
	EXECUTE stmt;
	DEALLOCATE PREPARE stmt;

	SET @Qry = CONCAT("DELETE FROM ",sourceDB,".st_dg_agt_direct_plr_pwt WHERE DATE(transaction_date)>='",startDate,"' AND DATE(transaction_date)<='",endDate,"';");
	PREPARE stmt FROM @Qry;
	EXECUTE stmt;
	DEALLOCATE PREPARE stmt;

	
	SET @Qry = CONCAT("INSERT INTO ",targetDB,".st_dg_approval_req_master (party_type, request_id, party_id, ticket_nbr, draw_id, panel_id, game_id, pwt_amt, tax_amt, net_amt, req_status, requester_type, requested_by_user_id, requested_by_org_id, requested_to_org_id, requested_to_type, approved_by_type, approved_by_user_id, approved_by_org_id, pay_req_for_org_type, pay_request_for_org_id, request_date, approval_date, remarks, payment_done_by_type, payment_done_by, transaction_id) SELECT party_type, request_id, party_id, ticket_nbr, draw_id, panel_id, game_id, pwt_amt, tax_amt, net_amt, req_status, requester_type, requested_by_user_id, requested_by_org_id, requested_to_org_id, requested_to_type, approved_by_type, approved_by_user_id, approved_by_org_id, pay_req_for_org_type, pay_request_for_org_id, request_date, approval_date, remarks, payment_done_by_type, payment_done_by, transaction_id FROM ",sourceDB,".st_dg_approval_req_master WHERE DATE(approval_date)>='",startDate,"' AND DATE(approval_date)<='",endDate,"';");
	PREPARE stmt FROM @Qry;
	EXECUTE stmt;
	DEALLOCATE PREPARE stmt;

	SET @Qry = CONCAT("DELETE FROM ",sourceDB,".st_dg_approval_req_master WHERE DATE(approval_date)>='",startDate,"' AND DATE(approval_date)<='",endDate,"';");
	PREPARE stmt FROM @Qry;
	EXECUTE stmt;
	DEALLOCATE PREPARE stmt;

	
	SET @Qry = CONCAT("INSERT INTO ",targetDB,".st_dg_bo_agent_pwt_comm_variance_history (agent_org_id, game_id, pwt_comm_variance, date_changed) SELECT agent_org_id, game_id, pwt_comm_variance, date_changed FROM ",sourceDB,".st_dg_bo_agent_pwt_comm_variance_history WHERE DATE(date_changed)>='",startDate,"' AND DATE(date_changed)<='",endDate,"';");
	PREPARE stmt FROM @Qry;
	EXECUTE stmt;
	DEALLOCATE PREPARE stmt;

	SET @Qry = CONCAT("DELETE FROM ",sourceDB,".st_dg_bo_agent_pwt_comm_variance_history WHERE DATE(date_changed)>='",startDate,"' AND DATE(date_changed)<='",endDate,"';");
	PREPARE stmt FROM @Qry;
	EXECUTE stmt;
	DEALLOCATE PREPARE stmt;

	
	SET @Qry = CONCAT("INSERT INTO ",targetDB,".st_dg_bo_agent_sale_comm_variance_history (agent_org_id, game_id, sale_comm_variance, date_changed) SELECT agent_org_id, game_id, sale_comm_variance, date_changed FROM ",sourceDB,".st_dg_bo_agent_sale_comm_variance_history WHERE DATE(date_changed)>='",startDate,"' AND DATE(date_changed)<='",endDate,"';");
	PREPARE stmt FROM @Qry;
	EXECUTE stmt;
	DEALLOCATE PREPARE stmt;

	
	SET @Qry = CONCAT("INSERT INTO ",targetDB,".st_dg_bo_direct_plr_pwt (task_id, bo_user_id, bo_org_id, draw_id, panel_id, transaction_id, transaction_date, game_id, player_id, pwt_amt, tax_amt, net_amt, payment_type, cheque_nbr, cheque_date, drawee_bank, issuing_party_name) SELECT task_id, bo_user_id, bo_org_id, draw_id, panel_id, transaction_id, transaction_date, game_id, player_id, pwt_amt, tax_amt, net_amt, payment_type, cheque_nbr, cheque_date, drawee_bank, issuing_party_name FROM ",sourceDB,".st_dg_bo_direct_plr_pwt WHERE DATE(transaction_date)>='",startDate,"' AND DATE(transaction_date)<='",endDate,"';");
	PREPARE stmt FROM @Qry;
	EXECUTE stmt;
	DEALLOCATE PREPARE stmt;

	SET @Qry = CONCAT("DELETE FROM ",sourceDB,".st_dg_bo_direct_plr_pwt WHERE DATE(transaction_date)>='",startDate,"' AND DATE(transaction_date)<='",endDate,"';");
	PREPARE stmt FROM @Qry;
	EXECUTE stmt;
	DEALLOCATE PREPARE stmt;

	SET @Qry = CONCAT("DELETE FROM ",sourceDB,".st_dg_bo_agent_sale_comm_variance_history WHERE DATE(date_changed)>='",startDate,"' AND DATE(date_changed)<='",endDate,"';");
	PREPARE stmt FROM @Qry;
	EXECUTE stmt;
	DEALLOCATE PREPARE stmt;

	
	
	
	
	

	
	
	
	

	
	SET @Qry = CONCAT("INSERT INTO ",targetDB,".st_lms_organization_master_history (user_id, organization_id, addr_line1, addr_line2, city, pin_code, security_deposit, credit_limit, reason, comment, organization_status, date_changed, pwt_scrap, recon_report_type) SELECT user_id, organization_id, addr_line1, addr_line2, city, pin_code, security_deposit, credit_limit, reason, comment, organization_status, date_changed, pwt_scrap, recon_report_type FROM ",sourceDB,".st_lms_organization_master_history WHERE DATE(date_changed)>='",startDate,"' AND DATE(date_changed)<='",endDate,"';");
	PREPARE stmt FROM @Qry;
	EXECUTE stmt;
	DEALLOCATE PREPARE stmt;

	SET @Qry = CONCAT("DELETE FROM ",sourceDB,".st_lms_organization_master_history WHERE DATE(date_changed)>='",startDate,"' AND DATE(date_changed)<='",endDate,"';");
	PREPARE stmt FROM @Qry;
	EXECUTE stmt;
	DEALLOCATE PREPARE stmt;

	
	SET @Qry = CONCAT("INSERT INTO ",targetDB,".st_lms_password_history (user_id, password, date_changed, type) SELECT user_id, password, date_changed, type FROM ",sourceDB,".st_lms_password_history WHERE DATE(date_changed)>='",startDate,"' AND DATE(date_changed)<='",endDate,"';");
	PREPARE stmt FROM @Qry;
	EXECUTE stmt;
	DEALLOCATE PREPARE stmt;

	SET @Qry = CONCAT("DELETE FROM ",sourceDB,".st_lms_password_history WHERE DATE(date_changed)>='",startDate,"' AND DATE(date_changed)<='",endDate,"';");
	PREPARE stmt FROM @Qry;
	EXECUTE stmt;
	DEALLOCATE PREPARE stmt;

	
	SET @Qry = CONCAT("INSERT INTO ",targetDB,".st_lms_rg_org_daily_tx_history (organization_id, user_id, date, dg_sale_amt, dg_pwt_amt, dg_reprint_limit, dg_cancel_limit, dg_invalid_pwt_limit, se_sale_amt, se_pwt_amt, se_invalid_pwt_limit, dg_offline_lateupload, dg_offline_errorfile) SELECT organization_id, user_id, date, dg_sale_amt, dg_pwt_amt, dg_reprint_limit, dg_cancel_limit, dg_invalid_pwt_limit, se_sale_amt, se_pwt_amt, se_invalid_pwt_limit, dg_offline_lateupload, dg_offline_errorfile FROM ",sourceDB,".st_lms_rg_org_daily_tx_history WHERE DATE(date)>='",startDate,"' AND DATE(date)<='",endDate,"';");
	PREPARE stmt FROM @Qry;
	EXECUTE stmt;
	DEALLOCATE PREPARE stmt;

	SET @Qry = CONCAT("DELETE FROM ",sourceDB,".st_lms_rg_org_daily_tx_history WHERE DATE(date)>='",startDate,"' AND DATE(date)<='",endDate,"';");
	PREPARE stmt FROM @Qry;
	EXECUTE stmt;
	DEALLOCATE PREPARE stmt;

	
	SET @Qry = CONCAT("INSERT INTO ",targetDB,".st_lms_rg_org_weakly_tx_history (organization_id, user_id, startdate, enddate, dg_sale_amt, dg_pwt_amt, dg_reprint_limit, dg_cancel_limit, dg_invalid_pwt_limit, se_sale_amt, se_pwt_amt, se_invalid_pwt_limit, dg_offline_lateupload, dg_offline_errorfile) SELECT organization_id, user_id, startdate, enddate, dg_sale_amt, dg_pwt_amt, dg_reprint_limit, dg_cancel_limit, dg_invalid_pwt_limit, se_sale_amt, se_pwt_amt, se_invalid_pwt_limit, dg_offline_lateupload, dg_offline_errorfile FROM ",sourceDB,".st_lms_rg_org_weakly_tx_history WHERE DATE(enddate)>='",startDate,"' AND DATE(enddate)<='",endDate,"';");
	PREPARE stmt FROM @Qry;
	EXECUTE stmt;
	DEALLOCATE PREPARE stmt;

	SET @Qry = CONCAT("DELETE FROM ",sourceDB,".st_lms_rg_org_weakly_tx_history WHERE DATE(enddate)>='",startDate,"' AND DATE(enddate)<='",endDate,"';");
	PREPARE stmt FROM @Qry;
	EXECUTE stmt;
	DEALLOCATE PREPARE stmt;

	
	SET @Qry = CONCAT("INSERT INTO ",targetDB,".st_lms_cl_xcl_update_history (organization_id, done_by_user_id, date_time, TYPE, amount, updated_value,  total_bal_before_update) SELECT organization_id, done_by_user_id, date_time, TYPE, amount, updated_value, total_bal_before_update FROM ",sourceDB,".st_lms_cl_xcl_update_history WHERE DATE(date_time)>='",startDate,"' AND DATE(date_time)<='",endDate,"';");
	PREPARE stmt FROM @Qry;
	EXECUTE stmt;
	DEALLOCATE PREPARE stmt;

	SET @Qry = CONCAT("DELETE FROM ",sourceDB,".st_lms_cl_xcl_update_history WHERE DATE(date_time)>='",startDate,"' AND DATE(date_time)<='",endDate,"';");
	PREPARE stmt FROM @Qry;
	EXECUTE stmt;
	DEALLOCATE PREPARE stmt;
    END      
#
CREATE      PROCEDURE `iwGameSaleAgentWisePerGame`(startDate timestamp,endDate timestamp,gameNo int, IN cityCode VARCHAR(10), IN stateCode VARCHAR(10))
BEGIN
		set @subqry :=(select value  from st_lms_property_master  where property_code='ORG_LIST_TYPE');
		set @selCol = 'om.name orgCode';
		if(@subqry='CODE') then 
			set @selCol='org_code orgCode' ;
		end if;
		if (@subqry='CODE_NAME') then 
			set @selCol='concat(org_code,"_",om.name)orgCode';
		end if;
		if (@subqry='NAME_CODE') then 
		  	set @selCol='concat(om.name,"_",org_code)orgCode';
		end if; 
            set @mainQry=concat("select om.organization_id,sm.name state_name,cm.city_name city_name,",@selCol,",ifnull(mrpAmt,0.0) mrpAmt,ifnull(netAmt,0.0) netAmt from st_lms_organization_master om right outer join (select parent_id,sum(mrpAmt) mrpAmt,sum(netAmt) netAmt from st_lms_organization_master,(select retailer_org_id,sum(mrpAmt) mrpAmt,sum(netAmt) netAmt from ((select sale.retailer_org_id,(ifnull(mrpAmt,0.0)-ifnull(mrpAmtRet,0.0))mrpAmt,(ifnull(netAmt,0.0)-ifnull(netAmtRet,0.0))netAmt from (select drs.retailer_org_id,sum(mrp_amt) mrpAmt,sum(agent_net_amt) netAmt from st_iw_ret_sale drs inner join st_lms_retailer_transaction_master tm on drs.transaction_id=tm.transaction_id where transaction_type in('IW_SALE') and tm.transaction_date>='",startDate,"' and tm.transaction_date<='",endDate,"' group by drs.retailer_org_id) sale left outer join (select drs.retailer_org_id,sum(mrp_amt) mrpAmtRet,sum(agent_net_amt) netAmtRet from st_iw_ret_sale_refund drs inner join st_lms_retailer_transaction_master tm on drs.transaction_id=tm.transaction_id where transaction_type in('IW_REFUND_CANCEL') and tm.transaction_date>='",startDate,"' and tm.transaction_date<='",endDate,"' group by drs.retailer_org_id) saleRet on sale.retailer_org_id=saleRet.retailer_org_id)) saleTlb group by retailer_org_id  union all select organization_id,sum(sale_mrp-ref_sale_mrp) mrpAmt,sum(sale_net-ref_net_amt) netAmt from st_rep_iw_retailer where finaldate>='",startDate,"' and finaldate<='",endDate,"' and game_id=",gameNo," group by organization_id) saleTlb where retailer_org_id=organization_id  group by parent_id having mrpAmt > 0.00 and netAmt > 0.00) saleTlb on saleTlb.parent_id=om.organization_id inner join st_lms_state_master sm on om.state_code = sm.state_code inner join st_lms_city_master cm on cm.city_name = om.city");
IF(stateCode != 'ALL' OR cityCode != 'ALL') THEN
	SET @mainQry = CONCAT(@mainQry, " WHERE ");
	IF(stateCode != 'ALL') THEN 
		SET @mainQry = CONCAT(@mainQry, " om.state_code = '",stateCode,"'");
	END IF;
	IF(cityCode != 'ALL') THEN
		SET @mainQry = CONCAT(@mainQry, " AND cm.city_name = '",cityCode,"'");
	END IF;
END IF;
PREPARE stmt FROM @mainQry;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
    END      
#
CREATE      PROCEDURE `iwPwtAgentWise`(startDate TIMESTAMP,endDate TIMESTAMP, IN stateCode VARCHAR(10), IN cityCode VARCHAR(10))
BEGIN
DECLARE done INT DEFAULT 0;
DECLARE gameId INT;
DECLARE gameCur CURSOR FOR SELECT game_id FROM st_iw_game_master;
DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;
		SET @subqry :=(SELECT VALUE  FROM st_lms_property_master  WHERE property_code='ORG_LIST_TYPE');
		SET @selCol = 'om.name orgCode';
		IF(@subqry='CODE') THEN 
			SET @selCol='org_code orgCode' ;
		END IF;
		IF (@subqry='CODE_NAME') THEN 
			SET @selCol='concat(org_code,"_",om.name)orgCode';
		END IF;
		IF (@subqry='NAME_CODE') THEN 
		  	SET @selCol='concat(om.name,"_",org_code)orgCode';
		END IF; 
 SET @mainQry=CONCAT("select ", @selCol,",organization_id,mrpAmt,netAmt, pwtTlb.name state_name, pwtTlb.city_name from st_lms_organization_master om,(SELECT parent_id,SUM(mrpAmt) mrpAmt,SUM(netAmt) netAmt, NAME, city_name FROM (SELECT main.parent_id, main.mrpAmt, main.netAmt, sm.name NAME, city.city_name FROM (SELECT parent_id,SUM(mrpAmt) mrpAmt,SUM(netAmt) netAmt FROM (");
	OPEN gameCur;
	  read_loop: LOOP
	    FETCH gameCur INTO gameId;
	    IF done THEN
	      LEAVE read_loop;
	    END IF;
SET @mainQry=CONCAT(@mainQry,"SELECT rp.retailer_org_id, SUM(rp.pwt_amt) mrpAmt, SUM(rp.pwt_amt + rp.agt_claim_comm) netAmt FROM st_iw_ret_pwt rp INNER JOIN st_lms_retailer_transaction_master rTxn ON rp.transaction_id = rTxn.transaction_id WHERE rTxn.transaction_type IN('IW_PWT') AND rTxn.transaction_date>='",startDate,"' and rTxn.transaction_date<='",endDate,"'");
SET @mainQry = CONCAT(@mainQry, " group by retailer_org_id union all "); 
	  END LOOP;
	CLOSE gameCur;
SET @mainQry=TRIM(TRAILING ' union all ' FROM @mainQry);
SET @mainQry = CONCAT(@mainQry, ")pwtTlb,(select organization_id,parent_id from  st_lms_organization_master where organization_type='RETAILER') om where retailer_org_id= organization_id group by parent_id) main INNER JOIN st_lms_organization_master om ON om.organization_id = main.parent_id INNER JOIN st_lms_state_master sm ON sm.state_code = om.state_code INNER JOIN st_lms_city_master city ON om.city = city.city_name ");
IF(stateCode != 'ALL' OR cityCode != 'ALL') THEN
	SET @mainQry = CONCAT(@mainQry, " WHERE ");
	IF(stateCode != 'ALL') THEN 
		SET @mainQry = CONCAT(@mainQry, " om.state_code = '",stateCode,"'");
	END IF;
	IF(cityCode != 'ALL') THEN
		SET @mainQry = CONCAT(@mainQry, " AND city.city_name = '",cityCode,"'");
	END IF;
END IF;
SET @mainQry = CONCAT(@mainQry, " union all SELECT dpp.agent_org_id,SUM(dpp.pwt_amt) mrpAmt,SUM(dpp.pwt_amt+dpp.agt_claim_comm) netAmt, sm.name, city.city_name FROM st_iw_agent_direct_plr_pwt  dpp INNER JOIN st_lms_organization_master om ON om.organization_id = dpp.agent_org_id INNER JOIN st_lms_city_master city ON om.city = city.city_name INNER JOIN st_lms_state_master sm ON sm.state_code = om.state_code WHERE dpp.transaction_date>='",startDate,"' and dpp.transaction_date<='",endDate,"'");
IF(stateCode != 'ALL') THEN 
	SET @mainQry = CONCAT(@mainQry, " AND om.state_code = '",stateCode,"'");
END IF;
IF(cityCode != 'ALL') THEN
	SET @mainQry = CONCAT(@mainQry, " AND city.city_name = '",cityCode,"'");
END IF;
SET @mainQry = CONCAT(@mainQry, " GROUP BY dpp.agent_org_id UNION ALL "); 
SET @mainQry = CONCAT(@mainQry, "SELECT rda.organization_id,SUM(rda.pwt_mrp+rda.direct_pwt_amt) mrpAmt,SUM(rda.pwt_net_amt+rda.direct_pwt_net_amt) netAmt, sm.name, city.city_name FROM st_rep_iw_agent rda INNER JOIN st_lms_organization_master om ON om.organization_id = rda.organization_id INNER JOIN st_lms_city_master city ON om.city = city.city_name INNER JOIN st_lms_state_master sm ON sm.state_code = om.state_code WHERE rda.finaldate>='",startDate,"' and rda.finaldate<='",endDate,"'");
IF(stateCode != 'ALL') THEN 
	SET @mainQry = CONCAT(@mainQry, " AND om.state_code = '",stateCode,"'");
END IF;
IF(cityCode != 'ALL') THEN
	SET @mainQry = CONCAT(@mainQry, " AND city.city_name = '",cityCode,"'");
END IF;
SET @mainQry = CONCAT(@mainQry, " group by organization_id having mrpAmt > 0.00 and netAmt > 0.00 ) pwtTlb group by parent_id) pwtTlb where om.organization_id=pwtTlb.parent_id;");
 PREPARE stmt FROM @mainQry;
	  EXECUTE stmt;
	  DEALLOCATE PREPARE stmt;
  END      
#
CREATE      PROCEDURE `iwPwtGameWise`(startDate TIMESTAMP, endDate TIMESTAMP,  IN stateCode VARCHAR(10), IN cityCode VARCHAR(10))
BEGIN
DECLARE done INT DEFAULT 0;
DECLARE gameId INT;
DECLARE gameCur CURSOR FOR SELECT game_id FROM st_iw_game_master;
DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;
 SET @mainQry="select gm.game_id gameId, game_disp_name gameName,mrpAmt,netAmt from st_iw_game_master gm,(select game_id,sum(mrpAmt) mrpAmt,sum(netAmt) netAmt from (";
	OPEN gameCur;
	  read_loop: LOOP
	    FETCH gameCur INTO gameId;
	    IF done THEN
	      LEAVE read_loop;
	    END IF;
SET @mainQry=CONCAT(@mainQry,"SELECT retPwt.game_id,SUM(retPwt.pwt_amt) mrpAmt,SUM(retPwt.pwt_amt + retPwt.agt_claim_comm) netAmt FROM st_iw_ret_pwt retPwt INNER JOIN st_lms_retailer_transaction_master retTxn ON retPwt.transaction_id = retTxn.transaction_id INNER JOIN st_lms_organization_master om ON om.organization_id = retPwt .retailer_org_id INNER JOIN st_lms_city_master city ON om.city = city.city_name WHERE retTxn.transaction_type IN('IW_PWT') AND retTxn.transaction_date>='",startDate,"'  AND retTxn.transaction_date<='",endDate,"'");
IF(stateCode != 'ALL') THEN 
	SET @mainQry = CONCAT(@mainQry, " AND om.state_code = '",stateCode,"'");
END IF;
IF(cityCode != 'ALL') THEN
	SET @mainQry = CONCAT(@mainQry, " AND city.city_name = '",cityCode,"'");
END IF;
SET @mainQry = CONCAT(@mainQry, "  group by game_id union all "); 
	  END LOOP;
	CLOSE gameCur;
 SET @mainQry=CONCAT(@mainQry,"SELECT plrPwt.game_id, SUM(plrPwt.pwt_amt) mrpAmt,SUM(plrPwt.pwt_amt+plrPwt.agt_claim_comm) netAmt FROM st_iw_agent_direct_plr_pwt plrPwt INNER JOIN st_lms_organization_master om ON om.organization_id = plrPwt.agent_org_id INNER JOIN st_lms_city_master city ON om.city = city.city_name WHERE transaction_date>='",startDate,"' and transaction_date<='",endDate,"'");
IF(stateCode != 'ALL') THEN 
	SET @mainQry = CONCAT(@mainQry, " AND om.state_code = '",stateCode,"'");
END IF;
IF(cityCode != 'ALL') THEN
	SET @mainQry = CONCAT(@mainQry, " AND city.city_name = '",cityCode,"'");
END IF;
SET @mainQry = CONCAT(@mainQry, "  group by game_id union all ");
	
SET @mainQry=CONCAT(@mainQry,"SELECT plrPwt.game_id,SUM(plrPwt.pwt_amt) mrpAmt,SUM(plrPwt.pwt_amt) netAmt FROM st_iw_bo_direct_plr_pwt plrPwt INNER JOIN st_lms_organization_master om ON om.organization_id = plrPwt.bo_org_id INNER JOIN st_lms_city_master city ON om.city = city.city_name WHERE plrPwt.transaction_date>='",startDate,"' and transaction_date<='",endDate,"'");
IF(stateCode != 'ALL') THEN 
	SET @mainQry = CONCAT(@mainQry, " AND om.state_code = '",stateCode,"'");
END IF;
IF(cityCode != 'ALL') THEN
	SET @mainQry = CONCAT(@mainQry, " AND city.city_name = '",cityCode,"'");
END IF;
SET @mainQry = CONCAT(@mainQry, "  group by game_id union all ");
SET @mainQry=CONCAT(@mainQry,"SELECT db.game_id,SUM(db.pwt_mrp+db.direct_pwt_amt) mrpAmt,SUM(db.pwt_net_amt+db.direct_pwt_net_amt) netAmt FROM st_rep_iw_bo db INNER JOIN st_lms_organization_master om ON om.organization_id = db.organization_id INNER JOIN st_lms_city_master city ON om.city = city.city_name WHERE db.finaldate>='",startDate,"' and db.finaldate <='",endDate,"' union all SELECT db.game_id,SUM(db.direct_pwt_amt) mrpAmt,SUM(db.direct_pwt_net_amt) netAmt FROM st_rep_iw_agent db INNER JOIN st_lms_organization_master om ON om.organization_id = db.organization_id INNER JOIN st_lms_city_master city ON om.city = city.city_name WHERE db.finaldate>='",startDate,"' and db.finaldate <='",endDate,"'");
IF(stateCode != 'ALL') THEN 
	SET @mainQry = CONCAT(@mainQry, " AND om.state_code = '",stateCode,"'");
END IF;
IF(cityCode != 'ALL') THEN
	SET @mainQry = CONCAT(@mainQry, " AND city.city_name = '",cityCode,"'");
END IF;
SET @mainQry = CONCAT(@mainQry, "  group by game_id");
SET@mainQry = CONCAT(@mainQry, ") pwtTlb group by game_id) pwtTlb where gm.game_id=pwtTlb.game_id");
 
 PREPARE stmt FROM @mainQry;
	  EXECUTE stmt;
	  DEALLOCATE PREPARE stmt;
  END      
#
CREATE      PROCEDURE `iwPwtRetailerWise`(startDate TIMESTAMP,endDate TIMESTAMP,agtOrgId INT,  IN stateCode VARCHAR(10), IN cityCode VARCHAR(10))
BEGIN
DECLARE done INT DEFAULT 0;
DECLARE gameId INT;
DECLARE gameCur CURSOR FOR SELECT game_id FROM st_iw_game_master;
DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;
		SET @subqry :=(SELECT VALUE  FROM st_lms_property_master  WHERE property_code='ORG_LIST_TYPE');
		SET @selCol = 'om.name orgCode';
		IF(@subqry='CODE') THEN 
			SET @selCol='om.org_code orgCode' ;
		END IF;
		IF (@subqry='CODE_NAME') THEN 
			SET @selCol='concat(om.org_code,"_",om.name)orgCode';
		END IF;
		IF (@subqry='NAME_CODE') THEN 
		  	SET @selCol='concat(om.name,"_",om.org_code)orgCode';
		END IF; 
 SET @mainQry=CONCAT("select om.organization_id, ",@selCol,",sum(mrpAmt) mrpAmt,sum(netAmt) netAmt, pwtTlb.name state_name, pwtTlb.city_name from st_lms_organization_master om,(");
	OPEN gameCur;
	  read_loop: LOOP
	    FETCH gameCur INTO gameId;
	    IF done THEN
	      LEAVE read_loop;
	    END IF;
SET @mainQry=CONCAT(@mainQry,"SELECT rPwt.retailer_org_id,SUM(rPwt.pwt_amt) mrpAmt,SUM(rPwt.pwt_amt+rPwt.retailer_claim_comm) netAmt, sm.name, city.city_name FROM st_iw_ret_pwt rPwt INNER JOIN st_lms_retailer_transaction_master rTxn ON rPwt.transaction_id = rTxn.transaction_id INNER JOIN st_lms_organization_master om ON rPwt.retailer_org_id = om.organization_id INNER JOIN st_lms_state_master sm ON sm.state_code = om.state_code INNER JOIN st_lms_city_master city ON om.city = city.city_name WHERE rTxn.transaction_type IN('IW_PWT') AND rTxn.transaction_date>='",startDate,"' and rTxn.transaction_date<='",endDate,"' AND om.parent_id=",agtOrgId,"");
IF(stateCode != 'ALL') THEN 
	SET @mainQry = CONCAT(@mainQry, " AND om.state_code = '",stateCode,"'");
END IF;
IF(cityCode != 'ALL') THEN
	SET @mainQry = CONCAT(@mainQry, " AND city.city_name = '",cityCode,"'");
END IF;
SET @mainQry = CONCAT(@mainQry, "  group by retailer_org_id union all "); 
	  END LOOP;
	CLOSE gameCur;
 SET @mainQry=TRIM(TRAILING ' union all ' FROM @mainQry);
 SET @mainQry=CONCAT(@mainQry," union all SELECT dr.organization_id,SUM(dr.pwt_mrp) mrpAmt,SUM(dr.pwt_net_amt) netAmt, sm.name, city.city_name FROM st_rep_iw_retailer dr INNER JOIN st_lms_organization_master om ON dr.organization_id = om.organization_id INNER JOIN st_lms_state_master sm ON sm.state_code = om.state_code INNER JOIN st_lms_city_master city ON om.city = city.city_name WHERE dr.finaldate>='",startDate,"' and dr.finaldate<='",endDate,"' and dr.parent_id=",agtOrgId,"");
 
IF(stateCode != 'ALL') THEN 
	SET @mainQry = CONCAT(@mainQry, " AND om.state_code = '",stateCode,"'");
END IF;
IF(cityCode != 'ALL') THEN
	SET @mainQry = CONCAT(@mainQry, " AND city.city_name = '",cityCode,"'");
END IF; 
 SET @mainQry = CONCAT(@mainQry, " group by organization_id  having mrpAmt > 0.00 and netAmt > 0.00) pwtTlb where om.organization_id=pwtTlb.retailer_org_id group by om.organization_id");
 
 PREPARE stmt FROM @mainQry;
	  EXECUTE stmt;
	  DEALLOCATE PREPARE stmt;
  END      
#
CREATE      PROCEDURE `iwSaleGameWise`(startDate Timestamp , endDate Timestamp,agtOrgId int, IN cityCode VARCHAR(10), IN stateCode VARCHAR(10))
BEGIN
DECLARE done INT DEFAULT 0;
DECLARE gameId INT;
DECLARE gameCur CURSOR FOR select game_id from st_iw_game_master;
DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;
set @addQry="";
set @addQryArch="";
if (agtOrgId!=0) then
set @addQry=concat("and retailer_org_id in(select organization_id from st_lms_organization_master om inner join st_lms_state_master sm on om.state_code = sm.state_code inner join st_lms_city_master cm on cm.city_name = om.city where parent_id=",agtOrgId,"");
IF(stateCode != 'ALL' OR cityCode != 'ALL') THEN
	IF(stateCode != 'ALL') THEN 
		SET @addQry = CONCAT(@addQry, " and om.state_code = '",stateCode,"'");
	END IF;
	IF(cityCode != 'ALL') THEN
		SET @addQry = CONCAT(@addQry, " AND cm.city_name = '",cityCode,"'");
	END IF;
END IF;
set @addQry = concat(@addQry, ")");
set @addQryArch=concat("and organization_id in(select organization_id from st_lms_organization_master om inner join st_lms_state_master sm on om.state_code = sm.state_code inner join st_lms_city_master cm on cm.city_name = om.city where parent_id=",agtOrgId,"");
IF(stateCode != 'ALL' OR cityCode != 'ALL') THEN
	IF(stateCode != 'ALL') THEN 
		SET @addQryArch = CONCAT(@addQryArch, " and om.state_code = '",stateCode,"'");
	END IF;
	IF(cityCode != 'ALL') THEN
		SET @addQryArch = CONCAT(@addQryArch, " AND cm.city_name = '",cityCode,"'");
	END IF;
END IF;
set @addQryArch = concat(@addQryArch, ")");
end if;
set @mainQry="select gm.game_id gameId,game_disp_name gameName,sum(mrpAmt) mrpAmt,sum(netAmt) netAmt from st_iw_game_master gm,(";
	OPEN gameCur;
	  read_loop: LOOP
	    FETCH gameCur INTO gameId;
	    IF done THEN
	      LEAVE read_loop;
	    END IF;
		set @mainQry=concat(@mainQry,"select sale.game_id,(ifnull(mrpAmt,0.0)-ifnull(mrpAmtRet,0.0))mrpAmt,(ifnull(netAmt,0.0)-ifnull(netAmtRet,0.0))netAmt from (select game_id,sum(mrp_amt) mrpAmt,sum(agent_net_amt) netAmt from st_iw_ret_sale where transaction_id in(select transaction_id from st_lms_retailer_transaction_master where transaction_type in('IW_SALE') and transaction_date>='",startDate, "' and transaction_date<='",endDate,"' ) ",@addQry," group by game_id) sale left outer join (select game_id,sum(mrp_amt) mrpAmtRet,sum(agent_net_amt) netAmtRet from st_iw_ret_sale_refund where transaction_id in(select transaction_id from st_lms_retailer_transaction_master where transaction_type in('IW_REFUND_CANCEL') and transaction_date>='",startDate,"' and transaction_date<='",endDate,"' ) ",@addQry," group by game_id)saleRet on sale.game_id=saleRet.game_id union all ");
	END LOOP;
  CLOSE gameCur;
 set @mainQry=TRIM(TRAILING 'union all ' FROM @mainQry);
 set @mainQry=concat(@mainQry," union all select game_id,sum(sale_mrp-ref_sale_mrp) mrpAmt,sum(sale_net-ref_net_amt) netAmt from st_rep_iw_retailer where finaldate>='",startDate,"' and finaldate<='",endDate,"' ",@addQryArch," group by game_id) saleTlb where gm.game_id=saleTlb.game_id group by saleTlb.game_id");
 PREPARE stmt FROM @mainQry;
	  EXECUTE stmt;
	  DEALLOCATE PREPARE stmt;
    END      
#
CREATE      PROCEDURE `iwSaleRetailerWise`(startDate Timestamp , endDate Timestamp,agtOrgId int, IN cityCode VARCHAR(10), IN stateCode VARCHAR(10))
BEGIN
DECLARE done INT DEFAULT 0;
DECLARE gameId INT;
DECLARE gameCur CURSOR FOR select game_id from st_iw_game_master;
DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;
	set @subqry :=(select value  from st_lms_property_master  where property_code='ORG_LIST_TYPE');
		set @selCol = 'om.name orgCode';
		if(@subqry='CODE') then 
			set @selCol='org_code orgCode' ;
		end if;
		if (@subqry='CODE_NAME') then 
			set @selCol='concat(org_code,"_",om.name)orgCode';
		end if;
		if (@subqry='NAME_CODE') then 
		  	set @selCol='concat(om.name,"_",org_code)orgCode';
		end if; 
 set @mainQry=concat("select om.organization_id, sm.name state_name, cm.city_name city_name,",@selCol,", ifnull(sum(mrpAmt),0.0) mrpAmt,ifnull(sum(netAmt),0.0) netAmt from st_lms_organization_master om right outer join (select organization_id,sum(mrpAmt) mrpAmt,sum(netAmt) netAmt from st_lms_organization_master,(select retailer_org_id,sum(mrpAmt) mrpAmt,sum(netAmt) netAmt from (");
	OPEN gameCur;
	  read_loop: LOOP
	    FETCH gameCur INTO gameId;
	    IF done THEN
	      LEAVE read_loop;
	    END IF;
		set @mainQry=concat(@mainQry,"(select sale.retailer_org_id,(ifnull(mrpAmt,0.0)-ifnull(mrpAmtRet,0.0))mrpAmt,(ifnull(netAmt,0.0)-ifnull(netAmtRet,0.0))netAmt from (select retailer_org_id,sum(mrp_amt) mrpAmt,sum(retailer_net_amt) netAmt from st_iw_ret_sale where transaction_id in(select transaction_id from st_lms_retailer_transaction_master where transaction_type in('IW_SALE') and transaction_date>='",startDate,"' and transaction_date<='",endDate,"' )group by retailer_org_id) sale left outer join (select retailer_org_id,sum(mrp_amt) mrpAmtRet,sum(retailer_net_amt) netAmtRet from st_iw_ret_sale_refund where transaction_id in(select transaction_id from st_lms_retailer_transaction_master where transaction_type in('IW_REFUND_CANCEL') and transaction_date>='",startDate,"' and transaction_date<='",endDate,"' )group by retailer_org_id) saleRet on sale.retailer_org_id=saleRet.retailer_org_id) union all ");
	END LOOP;
  CLOSE gameCur;
 set @mainQry=TRIM(TRAILING 'union all ' FROM @mainQry);
 set @mainQry=concat(@mainQry,") saleTlb group by retailer_org_id) saleTlb where retailer_org_id=organization_id and parent_id=",agtOrgId," group by organization_id union all select organization_id,sum(sale_mrp-ref_sale_mrp) mrpAmt,sum(sale_net-ref_net_amt) netAmt from st_rep_dg_retailer where finaldate>='",startDate,"' and finaldate<='",endDate,"' and parent_id=",agtOrgId," group by organization_id) saleTlb on saleTlb.organization_id=om.organization_id   inner join st_lms_state_master sm on om.state_code = sm.state_code inner join st_lms_city_master cm on cm.city_name = om.city ");
IF(stateCode != 'ALL' OR cityCode != 'ALL') THEN
	SET @mainQry = CONCAT(@mainQry, " WHERE ");
	IF(stateCode != 'ALL') THEN 
		SET @mainQry = CONCAT(@mainQry, " om.state_code = '",stateCode,"'");
	END IF;
	IF(cityCode != 'ALL') THEN
		SET @mainQry = CONCAT(@mainQry, " AND cm.city_name = '",cityCode,"'");
	END IF;
END IF;

set @mainQry = concat(@mainQry, " group by saleTlb.organization_id");
 PREPARE stmt FROM @mainQry;
	  EXECUTE stmt;
	  DEALLOCATE PREPARE stmt;
    END      
#
CREATE      PROCEDURE `liveConsolidatedDirectPlrPwt`(startDate timestamp,endDate timestamp,parentOrgId int)
BEGIN
if(parentOrgId!=1)
then
set @mainQry=concat("select sum(main.netAgtDirPlrPwt) netAgtDirPlrPwt from (
select ifnull(sum(pwt_amt + agt_claim_comm),0) as netAgtDirPlrPwt from st_dg_agt_direct_plr_pwt where agent_org_id = ",parentOrgId,"  and date(transaction_date) >= '",startDate,"' and date(transaction_date) <'",endDate,"'
union all select ifnull(sum(direct_pwt_net_amt),0) as netAgtDirPlrPwt from st_rep_dg_agent where organization_id = ",parentOrgId,"  and finaldate>='",startDate,"' and finaldate<'",endDate,"')main ");
else
set @mainQry=concat("select 0.0 netAgtDirPlrPwt from st_dg_agt_direct_plr_pwt");
    end if;

PREPARE stmt FROM @mainQry;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
    END      
#
CREATE      PROCEDURE `olaAgtReporting`(startDate timestamp,endDate timestamp)
BEGIN
    set @depoQry=concat("insert into st_rep_ola_agent(organization_id, parent_id,finaldate,wallet_id,deposit_mrp,deposit_comm,deposit_net) select organization_id,parent_id,alldate,om.wallet_id,ifnull(depositMrp,0.00) deposit_mrp,ifnull(depositComm,0.00) deposit_comm,ifnull(depositNet,0.00) deposit_net from (select organization_id,parent_id,date(alldate) alldate,wallet_id from st_lms_organization_master,tempdate,st_ola_wallet_master where organization_type='AGENT') om left outer join (select drs.agent_org_id,date(transaction_date) tx_date,wallet_id,sum(deposit_amt) depositMrp,sum(agent_comm) depositComm ,sum(bo_net_amt) depositNet from st_ola_agt_deposit drs,st_lms_agent_transaction_master rtm where drs.transaction_id=rtm.transaction_id and rtm.transaction_date>='",startDate,"' and rtm.transaction_date<='",endDate,"' and transaction_type in('OLA_DEPOSIT')  group by agent_org_id,tx_date,wallet_id) depo on organization_id=agent_org_id and alldate=tx_date and depo.wallet_id=om.wallet_id");
        PREPARE stmt FROM @depoQry;
        EXECUTE stmt;
select @depoQry;
        DEALLOCATE PREPARE stmt;
    DROP TEMPORARY TABLE IF EXISTS table9;
    set @tab9 = concat("CREATE TEMPORARY TABLE table9(agent_org_id int(10),tx_date date,wallet_id int(10),depositRefMrp decimal(20,2),depositCommRef decimal(20,2),depositRefNet decimal(20,2),primary key(agent_org_id,tx_date,wallet_id)) AS select drs.agent_org_id,date(transaction_date) tx_date,wallet_id,sum(deposit_amt) depositRefMrp,sum(agent_comm) depositCommRef ,sum(bo_net_amt) depositRefNet from st_ola_agt_deposit_refund drs,st_lms_agent_transaction_master rtm where drs.transaction_id=rtm.transaction_id and rtm.transaction_date>='",startDate,"' and rtm.transaction_date<='",endDate,"' and transaction_type in('OLA_DEPOSIT_REFUND')  group by agent_org_id,tx_date,wallet_id");
        PREPARE stmt FROM @tab9;
        EXECUTE stmt;
select @tab9;
        DEALLOCATE PREPARE stmt;
        set @depoCancelQry=concat("update table9 cancel,st_rep_ola_agent ola set ref_deposit_mrp=depositRefMrp,ref_deposit_comm=depositCommRef,ref_deposit_net_amt=depositRefNet where cancel.agent_org_id=ola.organization_id and ola.finaldate=cancel.tx_date and ola.wallet_id=cancel.wallet_id");
        PREPARE stmt FROM @depoCancelQry;
        EXECUTE stmt;
        DEALLOCATE PREPARE stmt;
    DROP TEMPORARY TABLE IF EXISTS table10;
    set @tab10=concat("CREATE TEMPORARY TABLE table10(agent_org_id int(10),tx_date date,wallet_id int(10),withMrp decimal(20,2),withComm decimal(20,2),withNet decimal(20,2),primary key(agent_org_id,tx_date,wallet_id)) AS select drs.agent_org_id,date(transaction_date) tx_date,wallet_id,sum(withdrawl_amt) withMrp,sum(agent_comm) withComm ,sum(bo_net_amt) withNet from st_ola_agt_withdrawl drs,st_lms_agent_transaction_master rtm where drs.transaction_id=rtm.transaction_id and rtm.transaction_date>='",startDate,"' and rtm.transaction_date<='",endDate,"' and transaction_type in('OLA_WITHDRAWL')  group by agent_org_id,tx_date,wallet_id");
        PREPARE stmt FROM @tab10;
        EXECUTE stmt;
select @tab10;
        DEALLOCATE PREPARE stmt;
        set @withQry=concat("update table10 with1,st_rep_ola_agent dg set withdrawal_mrp=withMrp,withdrawal_comm=withComm,withdrawal_net_amt=withNet  where with1.agent_org_id=dg.organization_id and dg.finaldate=with1.tx_date and dg.wallet_id=with1.wallet_id");
        PREPARE stmt FROM @withQry;
        EXECUTE stmt;
        DEALLOCATE PREPARE stmt;
    DROP TEMPORARY TABLE IF EXISTS table12;
    set @tab12=concat("CREATE TEMPORARY TABLE table12(agent_org_id int(10),tx_date date,wallet_id int(10),netGamingComm decimal(20,2),tdsComm decimal(20,2),totalNetGaming decimal(20,2),primary key(agent_org_id,tx_date,wallet_id)) As select drs.agent_org_id,date(transaction_date) tx_date,wallet_id,sum(agt_claim_comm) netGamingComm,sum(agt_claim_comm*tds_comm_rate*.01) tdsComm ,sum(agt_net_claim_comm) totalNetGaming from st_ola_agt_comm drs,st_lms_agent_transaction_master rtm where drs.transaction_id=rtm.transaction_id and rtm.transaction_date>='",startDate,"' and rtm.transaction_date<='",endDate,"' and transaction_type in('OLA_COMMISSION')group by agent_org_id,tx_date,wallet_id");
        PREPARE stmt FROM @tab12;
        EXECUTE stmt;
        DEALLOCATE PREPARE stmt;
        set @netGamingQry=concat("update table12 ng,st_rep_ola_agent ola set net_gaming_comm=netGamingComm,net_gaming_tds_comm=tdsComm,net_gaming_net_comm=totalNetGaming where ng.agent_org_id=ola.organization_id and ola.finaldate=ng.tx_date and ola.wallet_id=ng.wallet_id");
        PREPARE stmt FROM @netGamingQry;
        EXECUTE stmt;
        DEALLOCATE PREPARE stmt;
    SELECT "OLA AGENT ARCHIVING DONE !!";
    END
#

CREATE      PROCEDURE `olaAgtReportingDir`(startDate timestamp,endDate timestamp)
BEGIN
    set @depoQry=concat("insert into st_rep_ola_agt_dir(organization_id, parent_id,finaldate,wallet_id,deposit_mrp,deposit_comm,deposit_net) select organization_id,parent_id,alldate,om.wallet_id,ifnull(depositMrp,0.00) deposit_mrp,ifnull(depositComm,0.00) deposit_comm,ifnull(depositNet,0.00) deposit_net from (select organization_id,parent_id,date(alldate) alldate,wallet_id from st_lms_organization_master,tempdate,st_ola_wallet_master where organization_type='AGENT') om left outer join (select drs.agent_org_id,date(transaction_date) tx_date,wallet_id,sum(deposit_amt) depositMrp,sum(agt_claim_comm) depositComm ,sum(net_amt) depositNet from st_ola_agt_direct_plr_deposit drs,st_lms_agent_transaction_master rtm where drs.transaction_id=rtm.transaction_id and rtm.transaction_date>='",startDate,"' and rtm.transaction_date<='",endDate,"' and transaction_type in('OLA_DEPOSIT_PLR')  group by agent_org_id,tx_date,wallet_id) depo on organization_id=agent_org_id and alldate=tx_date and depo.wallet_id=om.wallet_id");
        PREPARE stmt FROM @depoQry;
        EXECUTE stmt;
        DEALLOCATE PREPARE stmt;
    DROP TEMPORARY TABLE IF EXISTS table9;
    set @tab9 = concat("CREATE TEMPORARY TABLE table9(agent_org_id int(10),tx_date date,wallet_id int(10),depositRefMrp decimal(20,2),depositCommRef decimal(20,2),depositRefNet decimal(20,2),primary key(agent_org_id,tx_date,wallet_id)) AS select drs.agent_org_id,date(transaction_date) tx_date,wallet_id,sum(deposit_amt) depositRefMrp,sum(agt_claim_comm) depositCommRef ,sum(net_amt) depositRefNet from st_ola_agt_direct_plr_deposit_refund drs,st_lms_agent_transaction_master rtm where drs.transaction_id=rtm.transaction_id and rtm.transaction_date>='",startDate,"' and rtm.transaction_date<='",endDate,"' and transaction_type in('OLA_DEPOSIT_REFUND_PLR')  group by agent_org_id,tx_date,wallet_id");
        PREPARE stmt FROM @tab9;
        EXECUTE stmt;
        DEALLOCATE PREPARE stmt;
        set @depoCancelQry=concat("update table9 cancel,st_rep_ola_agt_dir ola set ref_deposit_mrp=depositRefMrp,ref_deposit_comm=depositCommRef,ref_deposit_net_amt=depositRefNet where cancel.agent_org_id=ola.organization_id and ola.finaldate=cancel.tx_date and ola.wallet_id=cancel.wallet_id");
        PREPARE stmt FROM @depoCancelQry;
        EXECUTE stmt;
        DEALLOCATE PREPARE stmt;
    DROP TEMPORARY TABLE IF EXISTS table10;
    set @tab10=concat("CREATE TEMPORARY TABLE table10(agent_org_id int(10),tx_date date,wallet_id int(10),withMrp decimal(20,2),withComm decimal(20,2),withNet decimal(20,2),primary key(agent_org_id,tx_date,wallet_id)) AS select drs.agent_org_id,date(transaction_date) tx_date,wallet_id,sum(withdrawl_amt) withMrp,sum(agt_claim_comm) withComm ,sum(net_amt) withNet from st_ola_agt_direct_plr_withdrawl drs,st_lms_agent_transaction_master rtm where drs.transaction_id=rtm.transaction_id and rtm.transaction_date>='",startDate,"' and rtm.transaction_date<='",endDate,"' and transaction_type in('OLA_WITHDRAWL_PLR')  group by agent_org_id,tx_date,wallet_id");
        PREPARE stmt FROM @tab10;
        EXECUTE stmt;
        DEALLOCATE PREPARE stmt;
        set @withQry=concat("update table10 with1,st_rep_ola_agt_dir dg set withdrawal_mrp=withMrp,withdrawal_comm=withComm,withdrawal_net_amt=withNet  where with1.agent_org_id=dg.organization_id and dg.finaldate=with1.tx_date and dg.wallet_id=with1.wallet_id");
        PREPARE stmt FROM @withQry;
        EXECUTE stmt;
        DEALLOCATE PREPARE stmt;
    DROP TEMPORARY TABLE IF EXISTS table12;
    set @tab12=concat("CREATE TEMPORARY TABLE table12(agent_org_id int(10),tx_date date,wallet_id int(10),netGamingComm decimal(20,2),tdsComm decimal(20,2),totalNetGaming decimal(20,2),primary key(agent_org_id,tx_date,wallet_id)) As select drs.agent_org_id,date(transaction_date) tx_date,wallet_id,sum(agt_claim_comm) netGamingComm,sum(agt_claim_comm*tds_comm_rate*.01) tdsComm ,sum(agt_net_claim_comm) totalNetGaming from st_ola_agt_comm drs,st_lms_agent_transaction_master rtm where drs.transaction_id=rtm.transaction_id and rtm.transaction_date>='",startDate,"' and rtm.transaction_date<='",endDate,"' and transaction_type in('OLA_COMMISSION')group by agent_org_id,tx_date,wallet_id");
        PREPARE stmt FROM @tab12;
        EXECUTE stmt;
        DEALLOCATE PREPARE stmt;
        set @netGamingQry=concat("update table12 ng,st_rep_ola_agt_dir ola set net_gaming_comm=netGamingComm,net_gaming_tds_comm=tdsComm,net_gaming_net_comm=totalNetGaming where ng.agent_org_id=ola.organization_id and ola.finaldate=ng.tx_date and ola.wallet_id=ng.wallet_id");
        PREPARE stmt FROM @netGamingQry;
        EXECUTE stmt;
        DEALLOCATE PREPARE stmt;
	SELECT "OLA AGENT DIRECT PLAYER ARCHIVING DONE !!";
    END
#

CREATE      PROCEDURE `olaBOReporting`(startDate timestamp,endDate timestamp)
BEGIN
	 set @depoQry=concat("insert into st_rep_ola_bo(organization_id, parent_id,finaldate,wallet_id,deposit_mrp,deposit_comm,deposit_net) select organization_id,parent_id,alldate,om.wallet_id,ifnull(depositMrp,0.00) deposit_mrp,ifnull(depositComm,0.00) deposit_comm,ifnull(depositNet,0.00) deposit_net from (select organization_id,parent_id,date(alldate) alldate,wallet_id from st_lms_organization_master,tempdate,st_ola_wallet_master where organization_type='AGENT') om left outer join (select drs.agent_org_id,date(transaction_date) tx_date,wallet_id,sum(deposit_amt) depositMrp,sum(agent_comm) depositComm ,sum(net_amt) depositNet from st_ola_bo_deposit drs,st_lms_bo_transaction_master rtm where drs.transaction_id=rtm.transaction_id and rtm.transaction_date>='",startDate,"' and rtm.transaction_date<='",endDate,"' and transaction_type in('OLA_DEPOSIT')  group by agent_org_id,tx_date,wallet_id) depo on organization_id=agent_org_id and alldate=tx_date and depo.wallet_id=om.wallet_id");
        PREPARE stmt FROM @depoQry;
        EXECUTE stmt;
        DEALLOCATE PREPARE stmt;
    DROP TEMPORARY TABLE IF EXISTS table9;
    set @tab9 = concat("CREATE TEMPORARY TABLE table9(agent_org_id int(10),tx_date date,wallet_id int(10),depositRefMrp decimal(20,2),depositCommRef decimal(20,2),depositRefNet decimal(20,2),primary key(agent_org_id,tx_date,wallet_id)) AS select drs.agent_org_id,date(transaction_date) tx_date,wallet_id,sum(deposit_amt) depositRefMrp,sum(agent_comm) depositCommRef ,sum(net_amt) depositRefNet from st_ola_bo_deposit_refund drs,st_lms_bo_transaction_master rtm where drs.transaction_id=rtm.transaction_id and rtm.transaction_date>='",startDate,"' and rtm.transaction_date<='",endDate,"' and transaction_type in('OLA_DEPOSIT_REFUND')  group by agent_org_id,tx_date,wallet_id");
        PREPARE stmt FROM @tab9;
        EXECUTE stmt;
        DEALLOCATE PREPARE stmt;
        set @depoCancelQry=concat("update table9 cancel,st_rep_ola_bo ola set ref_deposit_mrp=depositRefMrp,ref_deposit_comm=depositCommRef,ref_deposit_net_amt=depositRefNet where cancel.agent_org_id=ola.organization_id and ola.finaldate=cancel.tx_date and ola.wallet_id=cancel.wallet_id");
        PREPARE stmt FROM @depoCancelQry;
        EXECUTE stmt;
        DEALLOCATE PREPARE stmt;
    DROP TEMPORARY TABLE IF EXISTS table10;
    set @tab10=concat("CREATE TEMPORARY TABLE table10(agent_org_id int(10),tx_date date,wallet_id int(10),withMrp decimal(20,2),withComm decimal(20,2),withNet decimal(20,2),primary key(agent_org_id,tx_date,wallet_id)) AS select drs.agent_org_id,date(transaction_date) tx_date,wallet_id,sum(withdrawl_amt) withMrp,sum(agent_comm) withComm ,sum(net_amt) withNet from st_ola_bo_withdrawl drs,st_lms_bo_transaction_master rtm where drs.transaction_id=rtm.transaction_id and rtm.transaction_date>='",startDate,"' and rtm.transaction_date<='",endDate,"' and transaction_type in('OLA_WITHDRAWL')  group by agent_org_id,tx_date,wallet_id");
        PREPARE stmt FROM @tab10;
        EXECUTE stmt;
        DEALLOCATE PREPARE stmt;
        set @withQry=concat("update table10 with1,st_rep_ola_bo dg set withdrawal_mrp=withMrp,withdrawal_comm=withComm,withdrawal_net_amt=withNet  where with1.agent_org_id=dg.organization_id and dg.finaldate=with1.tx_date and dg.wallet_id=with1.wallet_id");
        PREPARE stmt FROM @withQry;
        EXECUTE stmt;
        DEALLOCATE PREPARE stmt;
    DROP TEMPORARY TABLE IF EXISTS table12;
    set @tab12=concat("CREATE TEMPORARY TABLE table12(agent_org_id int(10),tx_date date,wallet_id int(10),netGamingComm decimal(20,2),tdsComm decimal(20,2),totalNetGaming decimal(20,2),primary key(agent_org_id,tx_date,wallet_id)) As select drs.agent_org_id,date(transaction_date) tx_date,wallet_id,sum(agt_claim_comm) netGamingComm,sum(agt_claim_comm*tds_comm_rate*.01) tdsComm ,sum(agt_net_claim_comm) totalNetGaming from st_ola_bo_comm drs,st_lms_bo_transaction_master rtm where drs.transaction_id=rtm.transaction_id and rtm.transaction_date>='",startDate,"' and rtm.transaction_date<='",endDate,"' and transaction_type in('OLA_COMMISSION')group by agent_org_id,tx_date,wallet_id");
        PREPARE stmt FROM @tab12;
        EXECUTE stmt;
        DEALLOCATE PREPARE stmt;
        set @netGamingQry=concat("update table12 ng,st_rep_ola_bo ola set net_gaming_comm=netGamingComm,net_gaming_tds_comm=tdsComm,net_gaming_net_comm=totalNetGaming where ng.agent_org_id=ola.organization_id and ola.finaldate=ng.tx_date and ola.wallet_id=ng.wallet_id");
        PREPARE stmt FROM @netGamingQry;
        EXECUTE stmt;
        DEALLOCATE PREPARE stmt;
	SELECT "OLA BO ARCHIVING DONE !!";
    END
#

CREATE      PROCEDURE `olaBOReportingDir`(startDate timestamp,endDate timestamp)
BEGIN
	 set @depoQry=concat("insert into st_rep_ola_bo_dir
(organization_id, parent_id,finaldate,wallet_id,deposit_mrp) 
select organization_id,parent_id,alldate,om.wallet_id,ifnull(depositMrp,0.00) deposit_mrp from (select organization_id,parent_id,date(alldate) alldate,wallet_id from st_lms_organization_master,tempdate,st_ola_wallet_master where organization_type='BO') om left outer join (select drs.bo_org_id,date(transaction_date) tx_date,wallet_id,sum(deposit_amt) depositMrp from st_ola_bo_direct_plr_deposit drs,st_lms_bo_transaction_master rtm where drs.transaction_id=rtm.transaction_id and rtm.transaction_date>='",startDate,"' and rtm.transaction_date<='",endDate,"' and transaction_type in('OLA_DEPOSIT_PLR')  group by bo_org_id,tx_date,wallet_id) depo 
on organization_id=bo_org_id and alldate=tx_date and depo.wallet_id=om.wallet_id");
        PREPARE stmt FROM @depoQry;
        EXECUTE stmt;
        DEALLOCATE PREPARE stmt;
    DROP TEMPORARY TABLE IF EXISTS table9;
    set @tab9 = concat("CREATE TEMPORARY TABLE table9(bo_org_id int(10),tx_date date,wallet_id int(10),depositRefMrp decimal(20,2),primary key(bo_org_id,tx_date,wallet_id)) AS select drs.bo_org_id,date(transaction_date) tx_date,wallet_id,sum(deposit_amt) depositRefMrp from st_ola_bo_direct_plr_deposit_refund drs,st_lms_bo_transaction_master rtm where drs.transaction_id=rtm.transaction_id and rtm.transaction_date>='",startDate,"' and rtm.transaction_date<='",endDate,"' and transaction_type in('OLA_DEPOSIT_REFUND_PLR')  group by bo_org_id,tx_date,wallet_id");
        PREPARE stmt FROM @tab9;
        EXECUTE stmt;
        DEALLOCATE PREPARE stmt;
        set @depoCancelQry=concat("update table9 cancel,st_rep_ola_bo_dir ola set ref_deposit_mrp=depositRefMrp where cancel.bo_org_id=ola.organization_id and ola.finaldate=cancel.tx_date and ola.wallet_id=cancel.wallet_id");
        PREPARE stmt FROM @depoCancelQry;
        EXECUTE stmt;
        DEALLOCATE PREPARE stmt;
    DROP TEMPORARY TABLE IF EXISTS table10;
    set @tab10=concat("CREATE TEMPORARY TABLE table10(bo_org_id int(10),tx_date date,wallet_id int(10),withMrp decimal(20,2),primary key(bo_org_id,tx_date,wallet_id)) AS select drs.bo_org_id,date(transaction_date) tx_date,wallet_id,sum(withdrawl_amt) withMrp  from st_ola_bo_direct_plr_withdrawl drs,st_lms_bo_transaction_master rtm where drs.transaction_id=rtm.transaction_id and rtm.transaction_date>='",startDate,"' and rtm.transaction_date<='",endDate,"' and transaction_type in('OLA_WITHDRAWL_PLR')  group by bo_org_id,tx_date,wallet_id");
        PREPARE stmt FROM @tab10;
        EXECUTE stmt;
        DEALLOCATE PREPARE stmt;
        set @withQry=concat("update table10 with1,st_rep_ola_bo_dir dg set withdrawal_mrp=withMrp where with1.bo_org_id=dg.organization_id and dg.finaldate=with1.tx_date and dg.wallet_id=with1.wallet_id");
        PREPARE stmt FROM @withQry;
        EXECUTE stmt;
        DEALLOCATE PREPARE stmt;
    DROP TEMPORARY TABLE IF EXISTS table12;
    set @tab12=concat("CREATE TEMPORARY TABLE table12(agent_org_id int(10),tx_date date,wallet_id int(10),netGamingComm decimal(20,2),tdsComm decimal(20,2),totalNetGaming decimal(20,2),primary key(agent_org_id,tx_date,wallet_id)) As select drs.agent_org_id,date(transaction_date) tx_date,wallet_id,sum(agt_claim_comm) netGamingComm,sum(agt_claim_comm*tds_comm_rate*.01) tdsComm ,sum(agt_net_claim_comm) totalNetGaming from st_ola_bo_comm drs,st_lms_bo_transaction_master rtm where drs.transaction_id=rtm.transaction_id and rtm.transaction_date>='",startDate,"' and rtm.transaction_date<='",endDate,"' and transaction_type in('OLA_COMMISSION')group by agent_org_id,tx_date,wallet_id");
        PREPARE stmt FROM @tab12;
        EXECUTE stmt;
        DEALLOCATE PREPARE stmt;
        set @netGamingQry=concat("update table12 ng,st_rep_ola_bo_dir ola set net_gaming_comm=netGamingComm,net_gaming_tds_comm=tdsComm,net_gaming_net_comm=totalNetGaming where ng.agent_org_id=ola.organization_id and ola.finaldate=ng.tx_date and ola.wallet_id=ng.wallet_id");
        PREPARE stmt FROM @netGamingQry;
        EXECUTE stmt;
        DEALLOCATE PREPARE stmt;
	SELECT "OLA BO DIRECT PLAYER ARCHIVING DONE !!";
    END
#

CREATE      PROCEDURE `olaRetReporting`(startDate timestamp,endDate timestamp)
BEGIN
    DECLARE done INT DEFAULT 0;
    DECLARE walletId int;
    DECLARE  walletCur CURSOR FOR select wallet_id from st_ola_wallet_master;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;
    OPEN walletCur;
      read_loop: LOOP
        FETCH walletCur INTO walletId;
        IF done THEN
          LEAVE read_loop;
        END IF;
       
        set @depositQry=concat("insert into st_rep_ola_retailer(organization_id, parent_id,finaldate,wallet_id,deposit_mrp,deposit_comm,deposit_net)select organization_id,parent_id,alldate,",walletId," wallet_id,ifnull(depositMrp,0.00) deposit_mrp,ifnull(depositComm,0.00) deposit_comm,ifnull(depositNet,0.00) deposit_net from (select organization_id,parent_id,date(alldate) alldate from st_lms_organization_master,tempdate where organization_type='RETAILER') om left outer join (select drs.retailer_org_id,date(transaction_date) tx_date,sum(deposit_amt) depositMrp,sum(retailer_comm) depositComm ,sum(net_amt) depositNet from st_ola_ret_deposit drs,st_lms_retailer_transaction_master rtm where drs.transaction_id=rtm.transaction_id and rtm.transaction_date>='",startDate,"' and rtm.transaction_date<='",endDate,"' and wallet_id=",walletId," and transaction_type in('OLA_DEPOSIT')  group by retailer_org_id,tx_date) deposit on organization_id=retailer_org_id and alldate=tx_date");
        PREPARE stmt FROM @depositQry;
        EXECUTE stmt;
        DEALLOCATE PREPARE stmt;
    DROP TEMPORARY TABLE IF EXISTS table3;
    set @tab3 = concat("CREATE TEMPORARY TABLE table3(retailer_org_id int(10),tx_date date ,depositRefMrp decimal(20,2),depositCommRef decimal(20,2),depositRefNet decimal(20,2),primary key(retailer_org_id,tx_date)) As select drs.retailer_org_id,date(transaction_date) tx_date,sum(deposit_amt) depositRefMrp,sum(retailer_comm) depositCommRef ,sum(net_amt) depositRefNet from st_ola_ret_deposit_refund drs,st_lms_retailer_transaction_master rtm where drs.transaction_id=rtm.transaction_id and rtm.transaction_date>='",startDate,"' and rtm.transaction_date<='",endDate,"' and wallet_id=",walletId," and transaction_type in('OLA_DEPOSIT_REFUND')group by retailer_org_id,tx_date");
    PREPARE stmt FROM @tab3;
        EXECUTE stmt;
        DEALLOCATE PREPARE stmt;
    set @depoCancelQry=concat("update table3 cancel,st_rep_ola_retailer ola set ref_deposit_mrp=depositRefMrp,ref_deposit_comm=depositCommRef,ref_deposit_net_amt=depositRefNet where cancel.retailer_org_id=ola.organization_id and ola.finaldate=cancel.tx_date and wallet_id=",walletId,"");
        PREPARE stmt FROM @depoCancelQry;
        EXECUTE stmt;
        DEALLOCATE PREPARE stmt;
    DROP TEMPORARY TABLE IF EXISTS table4;
    set @tab4 = concat("CREATE TEMPORARY TABLE table4(retailer_org_id int(10),tx_date date ,withMrp decimal(20,2),withComm decimal(20,2),withNet decimal(20,2),primary key(retailer_org_id,tx_date)) As select drs.retailer_org_id,date(transaction_date) tx_date,sum(withdrawl_amt) withMrp,sum(retailer_comm) withComm,sum(net_amt) withNet from st_ola_ret_withdrawl drs,st_lms_retailer_transaction_master rtm where drs.transaction_id=rtm.transaction_id and rtm.transaction_date>='",startDate,"' and rtm.transaction_date<='",endDate,"' and wallet_id=",walletId," and transaction_type in('OLA_WITHDRAWL') group by retailer_org_id,tx_date");
    PREPARE stmt FROM @tab4;
        EXECUTE stmt;
        DEALLOCATE PREPARE stmt;
    set @withQry=concat("update table4 with1,st_rep_ola_retailer ola set withdrawal_mrp=withMrp,withdrawal_comm=withComm,withdrawal_net_amt=withNet where with1.retailer_org_id=ola.organization_id and ola.finaldate=with1.tx_date and wallet_id=",walletId,"");
        PREPARE stmt FROM @withQry;
        EXECUTE stmt;
        DEALLOCATE PREPARE stmt;
DROP TEMPORARY TABLE IF EXISTS table6;
    set @tab6 = concat("CREATE TEMPORARY TABLE table6(retailer_org_id int(10),tx_date date ,netGamingComm decimal(20,2),tdsComm decimal(20,2),totalNetGaming decimal(20,2),primary key(retailer_org_id,tx_date)) As select drs.retailer_org_id,date(transaction_date) tx_date,sum(retailer_claim_comm) netGamingComm,sum(retailer_claim_comm*tds_comm_rate*.01) tdsComm ,sum(retailer_net_claim_comm) totalNetGaming from st_ola_ret_comm drs,st_lms_retailer_transaction_master rtm where drs.transaction_id=rtm.transaction_id and rtm.transaction_date>='",startDate,"' and rtm.transaction_date<='",endDate,"' and wallet_id=",walletId," and transaction_type in('OLA_COMMISSION')group by retailer_org_id,tx_date");
    PREPARE stmt FROM @tab6;
        EXECUTE stmt;
        DEALLOCATE PREPARE stmt;
    set @netGamingQry=concat("update table6 ng,st_rep_ola_retailer ola set net_gaming_comm=netGamingComm,net_gaming_tds_comm=tdsComm,net_gaming_net_comm=totalNetGaming where ng.retailer_org_id=ola.organization_id and ola.finaldate=ng.tx_date and wallet_id=",walletId,"");
        PREPARE stmt FROM @netGamingQry;
        EXECUTE stmt;
        DEALLOCATE PREPARE stmt;
    END LOOP;
    CLOSE walletCur;
select "OLA RETAILER ARCHIVING END:";
    END      
#
CREATE      PROCEDURE `openBalInsAgent`(opDate DATE)
BEGIN
	SET @isDG:=(SELECT IF(STATUS='ACTIVE',1,0) FROM st_lms_service_master WHERE service_code='DG');
	SET @isCS:=(SELECT IF(STATUS='ACTIVE',1,0) FROM st_lms_service_master WHERE service_code='CS');
	SET @isSE:=(SELECT IF(STATUS='ACTIVE',1,0) FROM st_lms_service_master WHERE service_code='SE');
	SET @isOLA:=(SELECT IF(STATUS='ACTIVE',1,0) FROM st_lms_service_master WHERE service_code='OLA');
	SET @isSLE:=(SELECT IF(STATUS='ACTIVE',1,0) FROM st_lms_service_master WHERE service_code='SLE');
	SET @isIW:=(SELECT IF(STATUS='ACTIVE',1,0) FROM st_lms_service_master WHERE service_code='IW');
	SET @isVS:=(SELECT IF(STATUS='ACTIVE',1,0) FROM st_lms_service_master WHERE service_code='VS');

	SET @query=CONCAT("INSERT INTO st_rep_org_bal_history (organization_id, organization_type, parent_id, finaldate, net_amount_transaction, cl, xcl) SELECT agent_org_id, 'AGENT', parent_id, finaldate, SUM(net),cl_amt,xcl_amt FROM ( SELECT agent_org_id, parent_id, finaldate, -(cash_amt+cheque_amt+credit_note+bank_deposit-debit_note-cheque_bounce_amt) AS net, cl_amt, xcl_amt FROM st_rep_bo_payments WHERE finaldate='",opDate,"' GROUP BY agent_org_id ");
	IF(@isDG=1) THEN
		SET @query=CONCAT(@query,"UNION ALL SELECT organization_id, parent_id, finaldate, (SUM(sale_net)-SUM(ref_net_amt)-SUM(pwt_net_amt)-SUM(direct_pwt_net_amt)) AS net, 0, 0 FROM st_rep_dg_agent WHERE finaldate='",opDate,"' GROUP BY organization_id, finaldate ");
select @query;
	END IF;
	IF(@isCS=1) THEN
		SET @query=CONCAT(@query,"UNION ALL SELECT organization_id, parent_id, finaldate, (SUM(sale_net)-SUM(ref_net_amt)) AS net, 0, 0 FROM st_rep_cs_agent WHERE finaldate='",opDate,"' GROUP BY organization_id, finaldate ");
	END IF;
	IF(@isSE=1) THEN
		SET @repType:=(SELECT VALUE FROM st_lms_property_master WHERE property_dev_name='SE_SALE_REP_TYPE');
		IF(@repType='BOOK_WISE') THEN
			SET @query=CONCAT(@query,"UNION ALL SELECT organization_id, parent_id, finaldate, (SUM(sale_book_net)-SUM(ref_net_amt)-SUM(pwt_net_amt)-SUM(direct_pwt_net_amt)), 0, 0 AS net FROM st_rep_se_agent WHERE finaldate='",opDate,"' GROUP BY organization_id, finaldate ");
		ELSE
			SET @query=CONCAT(@query,"UNION ALL SELECT organization_id, parent_id, finaldate, (SUM(sale_ticket_net)-SUM(pwt_net_amt)-SUM(direct_pwt_net_amt)), 0, 0 AS net FROM st_rep_se_agent WHERE finaldate='",opDate,"' GROUP BY organization_id, finaldate ");
		END IF ;
	END IF;
	IF(@isOLA=1) THEN
		SET @query=CONCAT(@query,"UNION ALL SELECT organization_id, parent_id, finaldate, (SUM(deposit_net)-SUM(ref_deposit_net_amt)-SUM(withdrawal_net_amt)-SUM(net_gaming)) AS net, 0, 0 FROM st_rep_ola_agent WHERE finaldate='",opDate,"' GROUP BY organization_id, finaldate ");
		SET @query=CONCAT(@query," UNION ALL SELECT organization_id, parent_id, finaldate, (SUM(deposit_net)-SUM(ref_deposit_net_amt)-SUM(withdrawal_net_amt)-SUM(net_gaming)) AS net, 0, 0 FROM st_rep_ola_agt_dir WHERE finaldate='",opDate,"' GROUP BY organization_id, finaldate ");
	END IF;
	IF(@isSLE=1) THEN
		SET @query=CONCAT(@query,"UNION ALL SELECT organization_id, parent_id, finaldate, (SUM(sale_net)-SUM(ref_net_amt)-SUM(pwt_net_amt)-SUM(direct_pwt_net_amt)) AS net, 0, 0 FROM st_rep_sle_agent WHERE finaldate='",opDate,"' GROUP BY organization_id, finaldate ");
	END IF;
IF(@isIW=1) THEN
		SET @query=CONCAT(@query,"UNION ALL SELECT organization_id, parent_id, finaldate, (SUM(sale_net)-SUM(ref_net_amt)-SUM(pwt_net_amt)-SUM(direct_pwt_net_amt)) AS net, 0, 0 FROM st_rep_iw_agent WHERE finaldate='",opDate,"' GROUP BY organization_id, finaldate ");
	END IF;
IF(@isVS=1) THEN
		SET @query=CONCAT(@query,"UNION ALL SELECT organization_id, parent_id, finaldate, (SUM(sale_net)-SUM(ref_net_amt)-SUM(pwt_net_amt)-SUM(direct_pwt_net_amt)) AS net, 0, 0 FROM st_rep_vs_agent WHERE finaldate='",opDate,"' GROUP BY organization_id, finaldate ");
	END IF;

	SET @query=CONCAT(@query,")aaa GROUP BY agent_org_id;");
	PREPARE stmt FROM @query;
	EXECUTE stmt;
	UPDATE (SELECT organization_id, (opening_bal+net_amount_transaction) AS openBal, (opening_bal_cl_inc-net_amount_transaction+cl+xcl) AS openBalCl FROM st_rep_org_bal_history WHERE finaldate=(opDate - INTERVAL 1 DAY)) openBal,
st_rep_org_bal_history hist SET hist.opening_bal=openBal.openBal, hist.opening_bal_cl_inc=openBal.openBalCl WHERE hist.organization_id=openBal.organization_id AND hist.finaldate = opDate;
    END      
#
CREATE      PROCEDURE `openBalInsRetailer`(opDate DATE)
BEGIN
	SET @isDG:=(SELECT IF(STATUS='ACTIVE',1,0) FROM st_lms_service_master WHERE service_code='DG');
	SET @isCS:=(SELECT IF(STATUS='ACTIVE',1,0) FROM st_lms_service_master WHERE service_code='CS');
	SET @isSE:=(SELECT IF(STATUS='ACTIVE',1,0) FROM st_lms_service_master WHERE service_code='SE');
	SET @isOLA:=(SELECT IF(STATUS='ACTIVE',1,0) FROM st_lms_service_master WHERE service_code='OLA');
	SET @isSLE:=(SELECT IF(STATUS='ACTIVE',1,0) FROM st_lms_service_master WHERE service_code='SLE');
	SET @isIW:=(SELECT IF(STATUS='ACTIVE',1,0) FROM st_lms_service_master WHERE service_code='IW');
	SET @isVS:=(SELECT IF(STATUS='ACTIVE',1,0) FROM st_lms_service_master WHERE service_code='VS');

	SET @query=CONCAT("INSERT INTO st_rep_org_bal_history (organization_id, organization_type, parent_id, finaldate, net_amount_transaction, cl, xcl) SELECT retailer_org_id, 'RETAILER', parent_id, finaldate, SUM(net),cl_amt,xcl_amt FROM ( SELECT retailer_org_id, parent_id, finaldate, -(cash_amt+cheque_amt+credit_note-debit_note-cheque_bounce_amt) AS net, cl_amt, xcl_amt FROM st_rep_agent_payments WHERE finaldate='",opDate,"' GROUP BY retailer_org_id ");
	IF(@isDG=1) THEN
		SET @query=CONCAT(@query,"UNION ALL SELECT organization_id, parent_id, finaldate, (SUM(sale_net)-SUM(ref_net_amt)-SUM(pwt_net_amt)) AS net, 0, 0 FROM st_rep_dg_retailer WHERE finaldate='",opDate,"' GROUP BY organization_id, finaldate ");
	END IF;
	IF(@isCS=1) THEN
		SET @query=CONCAT(@query,"UNION ALL SELECT organization_id, parent_id, finaldate, (SUM(sale_net)-SUM(ref_net_amt)) AS net, 0, 0 FROM st_rep_cs_retailer WHERE finaldate='",opDate,"' GROUP BY organization_id, finaldate ");
	END IF;
	IF(@isSE=1) THEN
		SET @repType:=(SELECT VALUE FROM st_lms_property_master WHERE property_dev_name='SE_SALE_REP_TYPE');
		IF(@repType='BOOK_WISE') THEN
			SET @query=CONCAT(@query,"UNION ALL SELECT organization_id, parent_id, finaldate, (SUM(sale_book_net)-SUM(ref_net_amt)-SUM(pwt_net_amt)) AS net, 0, 0 FROM st_rep_se_retailer WHERE finaldate='",opDate,"' GROUP BY organization_id, finaldate ");
		ELSE
			SET @query=CONCAT(@query,"UNION ALL SELECT organization_id, parent_id, finaldate, (SUM(sale_book_net)-SUM(pwt_net_amt)) AS net, 0, 0 FROM st_rep_se_retailer WHERE finaldate='",opDate,"' GROUP BY organization_id, finaldate ");
		END IF ;
	END IF;
	IF(@isOLA=1) THEN
		SET @query=CONCAT(@query,"UNION ALL SELECT organization_id, parent_id, finaldate, (SUM(deposit_net)-SUM(ref_deposit_net_amt)-SUM(withdrawal_net_amt)-SUM(net_gaming)) AS net, 0, 0 FROM st_rep_ola_retailer WHERE finaldate='",opDate,"' GROUP BY organization_id, finaldate ");
	END IF;
	IF(@isSLE=1) THEN
		SET @query=CONCAT(@query,"UNION ALL SELECT organization_id, parent_id, finaldate, (SUM(sale_net)-SUM(ref_net_amt)-SUM(pwt_net_amt)) AS net, 0, 0 FROM st_rep_sle_retailer WHERE finaldate='",opDate,"' GROUP BY organization_id, finaldate ");
	END IF;
	IF(@isIW=1) THEN
		SET @query=CONCAT(@query,"UNION ALL SELECT organization_id, parent_id, finaldate, (SUM(sale_net)-SUM(ref_net_amt)-SUM(pwt_net_amt)) AS net, 0, 0 FROM st_rep_iw_retailer WHERE finaldate='",opDate,"' GROUP BY organization_id, finaldate ");
	END IF;
	IF(@isVS=1) THEN
		SET @query=CONCAT(@query,"UNION ALL SELECT organization_id, parent_id, finaldate, (SUM(sale_net)-SUM(ref_net_amt)-SUM(pwt_net_amt)) AS net, 0, 0 FROM st_rep_vs_retailer WHERE finaldate='",opDate,"' GROUP BY organization_id, finaldate ");
	END IF;
	SET @query=CONCAT(@query,")aaa GROUP BY retailer_org_id;");
	PREPARE stmt FROM @query;
	EXECUTE stmt;
	UPDATE (SELECT organization_id, (opening_bal+net_amount_transaction) AS openBal, (opening_bal_cl_inc-net_amount_transaction+cl+xcl) AS openBalCl FROM st_rep_org_bal_history WHERE finaldate=(opDate - INTERVAL 1 DAY)) openBal,
st_rep_org_bal_history hist SET hist.opening_bal=openBal.openBal, hist.opening_bal_cl_inc=openBal.openBalCl WHERE hist.organization_id=openBal.organization_id AND hist.finaldate = opDate;
    END      
#
CREATE      PROCEDURE `openBalInsStart`(opDateStart DATE, opDateEnd DATE)
BEGIN
		WHILE opDateStart <= opDateEnd
		DO
			CALL openBalInsAgent(opDateStart);
			CALL openBalInsRetailer(opDateStart);
			SET opDateStart = opDateStart + INTERVAL 1 DAY;
		END WHILE;
    END      
#
CREATE      PROCEDURE `paymentCashChqOverAll`(startDate timestamp,endDate timestamp,agtOrgId int)
BEGIN
 
set @addOrgQry = concat("right outer join (select organization_id from st_lms_organization_master where organization_type='AGENT' and organization_id=",agtOrgId," ) om on agent_org_id=organization_id");
set @cashQry = concat("(select organization_id,cash from (select agent_org_id,sum(amount) cash from st_lms_bo_cash_transaction cash,st_lms_bo_transaction_master btm where cash.transaction_id=btm.transaction_id and transaction_date>='",startDate,"' and transaction_date<='",endDate,"' group by agent_org_id) cash ",@addOrgQry,") cash");
set @chqQry = concat("(select organization_id,chq from (select agent_org_id,sum(cheque_amt) chq from st_lms_bo_sale_chq chq,st_lms_bo_transaction_master btm where chq.transaction_id=btm.transaction_id and chq.transaction_type IN ('CHEQUE','CLOSED') and transaction_date>='",startDate,"' and transaction_date<='",endDate,"' group by agent_org_id) chq ",@addOrgQry,") chq");
set @chqRetQry = concat("(select organization_id,chq_ret from (select agent_org_id,sum(cheque_amt) chq_ret from st_lms_bo_sale_chq chq,st_lms_bo_transaction_master btm where chq.transaction_id=btm.transaction_id and chq.transaction_type IN ('CHQ_BOUNCE') and transaction_date>='",startDate,"' and transaction_date<='",endDate,"' group by agent_org_id) chq_ret ",@addOrgQry, ") chq_ret");
set @debitQry = concat("(select organization_id,debit from (select agent_org_id,sum(amount) debit from st_lms_bo_debit_note debit,st_lms_bo_transaction_master btm where debit.transaction_id=btm.transaction_id and debit.transaction_type IN ('DR_NOTE_CASH','DR_NOTE') and transaction_date>='",startDate,"' and transaction_date<='",endDate,"' group by agent_org_id) debit ",@addOrgQry,") debit");
set @creditQry = concat("(select organization_id,credit from (select agent_org_id,sum(amount) credit from st_lms_bo_credit_note credit,st_lms_bo_transaction_master btm where credit.transaction_id=btm.transaction_id and credit.transaction_type IN ('CR_NOTE_CASH','CR_NOTE') and transaction_date>='",startDate,"' and transaction_date<='",endDate,"' group by agent_org_id) credit ",@addOrgQry,") credit");
set @bankQry=concat("(select organization_id,bank from (select agent_org_id,sum(amount) bank from st_lms_bo_bank_deposit_transaction bank,st_lms_bo_transaction_master btm where bank.transaction_id=btm.transaction_id and transaction_date>='",startDate,"' and transaction_date<='",endDate,"' group by agent_org_id) bank ",@addOrgQry,") bank");
set @mainQry=concat("select main.organization_id,sum(main.cash)as cash,sum(main.chq)as chq,sum(main.chq_ret)as chq_ret,sum(main.debit)as debit,sum(main.credit)as credit,sum(main.bank)as bank from (select cash.organization_id,ifnull(cash,0.0) cash,ifnull(chq,0.0) chq,ifnull(chq_ret,0.0) chq_ret,ifnull(debit,0.0) debit,ifnull(credit,0.0) credit, ifnull(bank,0.0) bank from ",@cashQry,",",@chqQry,",",@chqRetQry,",", @debitQry,",",@creditQry,",",@bankQry," where cash.organization_id=chq.organization_id and cash.organization_id=chq_ret.organization_id and cash.organization_id=debit.organization_id and cash.organization_id=credit.organization_id and cash.organization_id=bank.organization_id and chq.organization_id=chq_ret.organization_id and chq.organization_id=debit.organization_id and chq.organization_id=credit.organization_id and chq.organization_id=bank.organization_id and chq_ret.organization_id=debit.organization_id and chq_ret.organization_id=credit.organization_id and chq_ret.organization_id=bank.organization_id  and debit.organization_id=credit.organization_id and debit.organization_id=bank.organization_id and credit.organization_id=bank.organization_id union all select agent_org_id,sum(cash_amt) as cash ,sum(cheque_amt) as chq,sum(cheque_bounce_amt)as chq_ret,sum(debit_note) as debit,sum(credit_note) as credit,sum(bank_deposit)as bank from st_rep_bo_payments where finaldate>='",startDate,"' and finaldate<='",endDate,"' and agent_org_id=",agtOrgId," group by agent_org_id)as main group by main.organization_id ");
 
PREPARE stmt FROM @mainQry;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
    END
#

CREATE      PROCEDURE `PendingCancelTktDG`(fromDate TIMESTAMP)
BEGIN
    DECLARE done INT DEFAULT 0;
    DECLARE gameId INT DEFAULT 0;
    DECLARE transactionId BIGINT DEFAULT 0;
    DECLARE gameCur CURSOR FOR SELECT game_id FROM st_dg_game_master WHERE game_status<>'SALE_CLOSE';

    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done=1;
    
    SELECT     transaction_id INTO transactionId FROM st_lms_retailer_transaction_master WHERE transaction_date>fromDate LIMIT 1 ;
    IF done = 0 THEN
        SET @mainQry = CONCAT("insert into st_dg_ret_pending_cancel (sale_ref_transaction_id,ticket_nbr,mrp_amt,ret_net_amt,agent_net_amt,game_id,cancel_attempt_time,transaction_date,retailer_org_id,reason)select transaction_id ,ticket_nbr ,mrp_amt,net_amt,agent_net_amt,game_id,now(),(select transaction_date from st_lms_retailer_transaction_master rtm where rtm.transaction_id=fff.transaction_id)transaction_date
,retailer_org_id,type from( ");
        SET @saleQry="";
        SET @ticketQry="";
        SET @serverQry="";
            OPEN gameCur;
            my_game_loop:LOOP
            FETCH gameCur INTO gameId;
            IF done THEN
                LEAVE my_game_loop;
            END IF;
            
            SET @saleQry = CONCAT(@saleQry, "SELECT rs.transaction_id ,rs.mrp_amt,rs.net_amt,rs.agent_net_amt,rs.ticket_nbr,rs.game_id,rs.retailer_org_id ,'AUTO_CANCEL_FAILED' type FROM st_dg_ret_sale_",gameId," rs left join st_dg_ret_sale_refund_",gameId," rsf on rs.transaction_id=rsf.ref_transaction_id  inner join st_lms_transaction_master tm on rs.transaction_id=tm.transaction_id where rs.transaction_id>=",transactionId," and tm.interface in('TERMINAL','WEB') and rs.mrp_amt>0  and rsf.ticket_nbr is NULL  UNION ALL ");
		SET @serverQry=CONCAT(@serverQry,"SELECT rs.transaction_id,rs.ticket_nbr,rs.mrp_amt,rs.net_amt,rs.agent_net_amt,rs.game_id,now(),(select transaction_date from st_lms_retailer_transaction_master rtm where rtm.transaction_id=rs.transaction_id)transaction_date,rs.retailer_org_id,'CANCEL_SERVER_FAILED' FROM st_dg_ret_sale_",gameId," rs left join st_dg_ret_sale_refund_",gameId," rsf on rs.transaction_id=rsf.ref_transaction_id where rs.transaction_id>=",transactionId," and rsf.ticket_nbr is NULL and rs.ticket_nbr=0 UNION ALL ");
        SET @ticketQry = CONCAT(@ticketQry, "SELECT 0 transaction_id,0 mrp_amt,0 net_amt,0 agent_net_amt,ticket_nbr,0 game_id,00 retailer_org_id,'PRINTED' type FROM st_dg_printed_tickets_",gameId," where notification_time>'",fromDate,"'  UNION ");
            END LOOP my_game_loop;
            CLOSE gameCur;
                        
        SET @ticketQry = CONCAT(@ticketQry," SELECT 0 transaction_id,0 mrp_amt,0 net_amt,0 agent_net_amt,terminal_ticket_number,0 game_id,00 retailer_org_id,'PRINTED' type   FROM st_dg_last_sold_ticket UNION  SELECT 0 transaction_id,0 mrp_amt,0 net_amt,0 agent_net_amt,web_ticket_number,0 game_id,00 retailer_org_id,'PRINTED' type   FROM st_dg_last_sold_ticket  UNION
 SELECT 0 transaction_id,0 mrp_amt,0 net_amt,0 agent_net_amt,ticket_nbr,0 game_id,00 retailer_org_id,'EXPIRED' type   FROM st_dg_ret_pending_cancel)fff group by ticket_nbr having count(*)=1 and type='AUTO_CANCEL_FAILED' and ticket_nbr <>0");
        SET @mainQry = CONCAT(@mainQry,@saleQry,@ticketQry);
		
        PREPARE stmt FROM @mainQry;
            EXECUTE stmt;
            DEALLOCATE PREPARE stmt;
             SET @serverQry=TRIM(TRAILING ' UNION ALL ' FROM @serverQry);
        SET @serverQry = CONCAT("insert into st_dg_ret_pending_cancel (sale_ref_transaction_id,ticket_nbr,mrp_amt,ret_net_amt,agent_net_amt,game_id,cancel_attempt_time,transaction_date,retailer_org_id,reason)",@serverQry);
		
		PREPARE serverStmt FROM @serverQry;
		EXECUTE serverStmt;
			DEALLOCATE PREPARE serverStmt;
	END IF;
    END      
#
CREATE      PROCEDURE `pwtPaymentDateWisePerGame`(startDate timestamp,endDate timestamp,gameNo int,agtOrgId int)
BEGIN
set @mainQry=concat("select date(transaction_date) date,parent_id,sum(pwt) as pwt from (select bb.parent_id,sum(pwt) as pwt,transaction_date from(select drs.retailer_org_id,sum(pwt_amt+agt_claim_comm) as pwt,rtm.transaction_date from st_dg_ret_pwt_",gameNo," drs,st_lms_retailer_transaction_master rtm where drs.transaction_id=rtm.transaction_id and rtm.transaction_type in('DG_PWT_AUTO','DG_PWT_PLR','DG_PWT') and
 transaction_date>='",startDate,"'and transaction_date<='", endDate,"' and drs.retailer_org_id in (select organization_id from st_lms_organization_master where organization_type='RETAILER') group by date(transaction_date),drs.retailer_org_id)aa,st_lms_organization_master bb where retailer_org_id=organization_id and parent_id=",agtOrgId," group by date(transaction_date)) pwtTlb group by date(transaction_date)
 union all select finaldate,organization_id,sum(pwt_net_amt) as pwt  from st_rep_dg_agent where game_id=",gameNo," and finaldate>='",startDate,"' and finaldate<='",endDate,"' and organization_id=",agtOrgId," group by finaldate ");
PREPARE stmt FROM @mainQry;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
    END      
#
CREATE      PROCEDURE `runArchive`(startDate TIMESTAMP, endDate TIMESTAMP,curDBName VARCHAR(50), archDBName VARCHAR(50))
exitLabel : BEGIN
 DECLARE total INT;
 DECLARE tempDate TIMESTAMP;
 call validateOpeningBalSchedulerLastRunTime(endDate, @total);
 if(@total = 0) THEN
  select "Run Opening Bal Update Scheduler";
  LEAVE exitLabel;
 end if;
CALL fillDateForRep(startDate,endDate);
SET @isMgmt:=(SELECT IF(STATUS='ACTIVE',TRUE,FALSE) FROM st_lms_service_master WHERE service_code='MGMT');
IF(@isMgmt) THEN

		SET tempDate:=(SELECT service_delivery_date FROM st_lms_service_master WHERE service_code='MGMT');
		IF(tempDate<startDate) THEN
			SET tempDate=startDate;
		END IF;

		IF(tempDate<endDate) THEN
			CALL fillDateTable(tempDate,endDate);
			CALL agentPaymentsReporting(tempDate,endDate);
			CALL boPaymentsReporting(tempDate,endDate);
		END IF;
	END IF;

SET @isDG:=(SELECT IF(STATUS='ACTIVE',TRUE,FALSE) FROM st_lms_service_master WHERE service_code='DG');
	IF(@isDG) THEN
		SET tempDate:=(SELECT service_delivery_date FROM st_lms_service_master WHERE service_code='DG');
		IF(tempDate<startDate) THEN
			SET tempDate=startDate;
		END IF;
		IF(tempDate<endDate) THEN
			CALL fillDateTable(tempDate,endDate);
			CALL drawGameAgtReporting(tempDate,endDate);
			CALL drawGameBOReporting(tempDate,endDate);
			CALL drawGameRetReporting(tempDate,endDate);
		END IF;
	END IF;
SET @isSE:=(SELECT IF(STATUS='ACTIVE',TRUE,FALSE) FROM st_lms_service_master WHERE service_code='SE');
	IF(@isSE) THEN
		SET tempDate:=(SELECT service_delivery_date FROM st_lms_service_master WHERE service_code='SE');
		IF(tempDate<startDate) THEN
			SET tempDate=startDate;
		END IF;
		IF(tempDate<endDate) THEN
			CALL fillDateTable(tempDate,endDate);
			CALL scratchGameAgtReporting(tempDate,endDate);
			CALL scratchGameBOReporting(tempDate,endDate);
			CALL scratchGameRetReporting(tempDate,endDate);
		END IF;
	END IF;
SET @isCS:=(SELECT IF(STATUS='ACTIVE',TRUE,FALSE) FROM st_lms_service_master WHERE service_code='CS');
	IF(@isCS) THEN
		SET tempDate:=(SELECT service_delivery_date FROM st_lms_service_master WHERE service_code='CS');
		IF(tempDate<startDate) THEN
			SET tempDate=startDate;
		END IF;
		IF(tempDate<endDate) THEN
			CALL fillDateTable(tempDate,endDate);
			CALL csAgtReporting(tempDate,endDate);
			CALL csBOReporting(tempDate,endDate);
			CALL csRetReporting(tempDate,endDate);
		END IF;
	END IF;
		SET @isOLA:=(SELECT IF(STATUS='ACTIVE',TRUE,FALSE) FROM st_lms_service_master WHERE service_code='OLA');
	IF(@isOLA) THEN
		SET tempDate:=(SELECT service_delivery_date FROM st_lms_service_master WHERE service_code='OLA');
		IF(tempDate<startDate) THEN
			SET tempDate=startDate;
		END IF;
		IF(tempDate<endDate) THEN
			CALL fillDateTable(tempDate,endDate);
			CALL olaAgtReporting(tempDate,endDate);
			CALL olaAgtReportingDir(tempDate,endDate);
			CALL olaBOReporting(tempDate,endDate);
			CALL olaBOReportingDir(tempDate,endDate);
			CALL olaRetReporting(tempDate,endDate);
		END IF;
	END IF;
	SET @isSLE:=(SELECT IF(STATUS='ACTIVE',TRUE,FALSE) FROM st_lms_service_master WHERE service_code='SLE');
	IF(@isSLE) THEN
		SET tempDate:=(SELECT service_delivery_date FROM st_lms_service_master WHERE service_code='SLE');
		IF(tempDate<startDate) THEN
			SET tempDate=startDate;
		END IF;
		IF(tempDate<endDate) THEN
			CALL fillDateTable(tempDate,endDate);
			CALL SLEAgtReporting(tempDate,endDate);
			CALL SLEBOReporting(tempDate,endDate);
			CALL SLERetReporting(tempDate,endDate);
		END IF;
	END IF;
SET @isIW:=(SELECT IF(STATUS='ACTIVE',TRUE,FALSE) FROM st_lms_service_master WHERE service_code='IW');
	IF(@isIW) THEN
SET tempDate:=(SELECT service_delivery_date FROM st_lms_service_master WHERE service_code='IW');
		IF(tempDate<startDate) THEN
			SET tempDate=startDate;
		END IF;
		IF(tempDate<endDate) THEN
			CALL fillDateTable(tempDate,endDate);
			CALL IWAgtReporting(tempDate,endDate);
			CALL IWBOReporting(tempDate,endDate);
			CALL IWRetReporting(tempDate,endDate);
		END IF;
	END IF;
SET @isVS:=(SELECT IF(STATUS='ACTIVE',TRUE,FALSE) FROM st_lms_service_master WHERE service_code='VS');
	IF(@isVS) THEN
SET tempDate:=(SELECT service_delivery_date FROM st_lms_service_master WHERE service_code='VS');
		IF(tempDate<startDate) THEN
			SET tempDate=startDate;
		END IF;
		IF(tempDate<endDate) THEN
			CALL fillDateTable(tempDate,endDate);
			CALL VSAgtReporting(tempDate,endDate);
			CALL VSBOReporting(tempDate,endDate);
			CALL VSRetReporting(tempDate,endDate);
		END IF;
	END IF;
CALL insDelArch(startDate,endDate,archDBName);

SELECT "LMS Archiving Done";
    END
#

CREATE      PROCEDURE `runLMSArchiving`()
BEGIN
	DECLARE startDate TIMESTAMP;
	DECLARE expiryPeriod INT;
	DECLARE endDate TIMESTAMP;
	DECLARE currentDBName VARCHAR(100);
	DECLARE archDBName VARCHAR(100);
	DECLARE dateDifference INT DEFAULT 0;	
	DECLARE tempDate TIMESTAMP;
	DECLARE numOfDate INT;
	DECLARE icsResult INT;
	DECLARE EXIT HANDLER FOR SQLEXCEPTION ROLLBACK;
        DECLARE EXIT HANDLER FOR SQLWARNING ROLLBACK;
        SET autocommit=0;
        START TRANSACTION;
SET @isICS:=(SELECT VALUE FROM st_lms_property_master WHERE property_dev_name='isICS');	
SET startDate:=(SELECT ADDDATE(last_date, 1) last_date FROM tempdate_history ORDER BY last_date DESC LIMIT 1);
SET expiryPeriod:=(SELECT VALUE FROM st_lms_property_master WHERE property_dev_name='expiry_period');
SET endDate:=(SELECT ADDDATE(NOW(), -expiryPeriod) last_date );
SET currentDBName:=(SELECT DATABASE());
SET archDBName:=(SELECT VALUE FROM st_lms_property_master WHERE property_dev_name='arch_db_name');
SET numOfDate:=(SELECT VALUE FROM st_lms_property_master WHERE property_dev_name='archiving_duration');
IF(@isICS) THEN
	
	SET icsResult:=(SELECT COUNT(*) FROM st_ics_daily_status WHERE DATE(ics_datetime)=DATE(NOW()) AND is_success='YES');
	IF(icsResult=0)THEN
	SELECT "ICS does not run successfully";
	CALL raise_error;
	END IF;
END IF;
	SET tempDate:=(SELECT (ADDDATE(startDate, numOfDate))-INTERVAL 1 SECOND tempd);
	IF(tempDate<endDate) THEN
	SELECT "Archiving Start";
	CALL runArchive(startDate,tempDate,currentDBName,archDBName);
	END IF;
	COMMIT;
    END      
#
CREATE      PROCEDURE `saleCancelPaymentDateWisePerGame`(startDate timestamp,endDate timestamp,gameNo int,agtOrgId int)
BEGIN
set @mainQry=concat("select ifnull(bbb.date,aaa.date) date,ifnull(bbb.parent_id,aaa.parent_id) parent_id,ifnull(sale,0.0) as sale,ifnull(cancel,0.0)as cancel from((select date(transaction_date) date,parent_id,sum(sale) as sale from ((select bb.parent_id,sum(sale) as sale,transaction_date from(select drs.retailer_org_id,sum(agent_net_amt) as sale,rtm.transaction_date from st_dg_ret_sale_",gameNo," drs,st_lms_retailer_transaction_master rtm where drs.transaction_id=rtm.transaction_id and rtm.transaction_type in('DG_SALE','DG_SALE_OFFLINE') and transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and drs.retailer_org_id in (select organization_id from st_lms_organization_master where organization_type='RETAILER') group by date(transaction_date),drs.retailer_org_id)aa,st_lms_organization_master bb where retailer_org_id=organization_id and parent_id=",agtOrgId," group by date(transaction_date) )) saletable group by date(transaction_date))aaa right join
(select date(transaction_date) date,parent_id,sum(cancel) as cancel from ((select bb.parent_id,sum(cancel) as cancel,transaction_date from(select drs.retailer_org_id,sum(agent_net_amt) as cancel,rtm.transaction_date from st_dg_ret_sale_refund_",gameNo," drs,st_lms_retailer_transaction_master rtm where drs.transaction_id=rtm.transaction_id and rtm.transaction_type in('DG_REFUND_CANCEL','DG_REFUND_FAILED') and transaction_date>='",startDate,"'and transaction_date<='",endDate,"' and drs.retailer_org_id in (select organization_id from st_lms_organization_master where organization_type='RETAILER') group by date(transaction_date),drs.retailer_org_id)aa,st_lms_organization_master bb where retailer_org_id=organization_id and parent_id=",agtOrgId," group by date(transaction_date))) cancelTlb group by date(transaction_date))bbb on aaa.date=bbb.date) union
select ifnull(bbb.date,aaa.date) date,ifnull(bbb.parent_id,aaa.parent_id) parent_id,ifnull(sale,0.0) as sale,ifnull(cancel,0.0) as cancel from((select date(transaction_date) date,parent_id,sum(sale) as sale from ((select bb.parent_id,sum(sale) as sale,transaction_date from(select drs.retailer_org_id,sum(agent_net_amt) as sale,rtm.transaction_date from st_dg_ret_sale_",gameNo," drs,st_lms_retailer_transaction_master rtm where drs.transaction_id=rtm.transaction_id and rtm.transaction_type in('DG_SALE','DG_SALE_OFFLINE') and transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and drs.retailer_org_id in (select organization_id from st_lms_organization_master where organization_type='RETAILER') group by date(transaction_date),drs.retailer_org_id)aa,st_lms_organization_master bb where retailer_org_id=organization_id and parent_id=",agtOrgId," group by date(transaction_date) )) saletable group by date(transaction_date))aaa left join
(select date(transaction_date) date,parent_id,sum(cancel) as cancel from ((select bb.parent_id,sum(cancel) as cancel,transaction_date from(select drs.retailer_org_id,sum(agent_net_amt) as cancel,rtm.transaction_date from st_dg_ret_sale_refund_",gameNo," drs,st_lms_retailer_transaction_master rtm where drs.transaction_id=rtm.transaction_id and rtm.transaction_type in('DG_REFUND_CANCEL','DG_REFUND_FAILED') and transaction_date>='",startDate,"'and transaction_date<='",endDate,"' and drs.retailer_org_id in (select organization_id from st_lms_organization_master where organization_type='RETAILER') group by date(transaction_date),drs.retailer_org_id)aa,st_lms_organization_master bb where retailer_org_id=organization_id and parent_id=",agtOrgId," group by date(transaction_date))) cancelTlb group by date(transaction_date))bbb
 on aaa.date=bbb.date) union all select finaldate,organization_id,sum(sale_net) as sale ,sum(ref_net_amt) as cancel  from st_rep_dg_agent where game_id=",gameNo," and finaldate>='",startDate,"' and finaldate<='",endDate,"' and organization_id=",agtOrgId," group by finaldate ");
       
PREPARE stmt FROM @mainQry;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
    END
#
CREATE      PROCEDURE `scratchBODirPlyPwtGameWise`(startDate timestamp,endDate timestamp)
BEGIN
	set @pwtQry=concat("select game_nbr gameNo, game_name gameName,sum(mrpAmt) mrpAmt from st_se_game_master gm,(select game_id,sum(pwt_amt) mrpAmt from st_se_direct_player_pwt where transaction_date>='",startDate,"' and transaction_date<='",endDate,"' group by game_id
union all select game_id,sum(direct_pwt_amt) mrpAmt from st_rep_se_agent where finaldate>='",startDate,"' and finaldate<='",endDate,"' group by game_id
)pwtTlb where gm.game_id=pwtTlb.game_id group by pwtTlb.game_id");
	prepare stmt from @pwtQry;
	execute stmt;
	deallocate prepare stmt;
    END
#
