--liquibase formatted sql

--changeset BaseSPRMS:14 endDelimiter:#


#
CREATE      PROCEDURE `scratchSaleGameWise`(startDate timestamp,endDate timestamp)
BEGIN
	
   set @repType:=(select value from st_lms_property_master where property_dev_name='SE_SALE_REP_TYPE');
	if(@repType='BOOK_WISE') then
		set @saleQry=concat("select game_nbr gameNo,game_name gameName,ifnull(mrpAmt,0.0) mrpAmt,ifnull(netAmt,0.0) netAmt from st_se_game_master gm left outer join (select game_id,sum(ifnull(mrpAmt,0.0)-ifnull(mrpAmtRet,0.0)) mrpAmt,sum(ifnull(netAmt,0.0)-ifnull(netAmtRet,0.0)) netAmt from (select game_id,sum(mrp_amt) mrpAmt,sum(net_amt) netAmt,0.0 mrpAmtRet,0.0 netAmtRet from st_se_bo_agent_transaction where transaction_id in (select transaction_id from st_lms_bo_transaction_master where transaction_type='SALE' and transaction_date>='",startDate,"' and transaction_date<='",endDate, "') group by game_id union all select game_id,0.0,0.0,sum(mrp_amt) mrpAmtRet,sum(net_amt) netAmtRet from st_se_bo_agent_transaction where transaction_id in (select transaction_id from st_lms_bo_transaction_master where transaction_type='SALE_RET' and transaction_date>='",startDate,"' and transaction_date<='",endDate,"') group by game_id union all 
select game_id,sum(sale_book_mrp),sum(sale_book_net),sum(ref_sale_mrp),sum(ref_net_amt) from st_rep_se_agent where finaldate>='",startDate,"' and finaldate<='",endDate,"' group by game_id) sale group by game_id) saleTlb on saleTlb.game_id=gm.game_id");
	elseif(@repType='TICKET_WISE') then
		set @saleQry=concat("select game_nbr gameNo,game_name gameName,sum(soldTkt*ticket_price) mrpAmt,sum((soldTkt*ticket_price)-(soldTkt*ticket_price*transacrion_sale_comm_rate*0.01)) netAmt from st_se_game_master gm,st_se_game_inv_detail gid,(select game_id,book_nbr,sum(sold_tickets) soldTkt from st_se_game_ticket_inv_history where date>='", startDate,"' and date<='",endDate,"' and current_owner='RETAILER' group by book_nbr) TktTlb where gm.game_id=TktTlb.game_id and TktTlb.book_nbr=gid.book_nbr and gid.current_owner='AGENT' group by gm.game_id");
	end if;
	
 	PREPARE stmt FROM @saleQry;
	EXECUTE stmt;
	DEALLOCATE PREPARE stmt;
    END  

#


