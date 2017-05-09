--liquibase formatted sql

--changeset BaseSPRMS:16 endDelimiter:#

#
CREATE PROCEDURE `scratchSaleGameWiseExpand`(startDate Timestamp , endDate Timestamp)
BEGIN
	set @repType:=(select value from st_lms_property_master where property_dev_name='SE_SALE_REP_TYPE');
	if(@repType='BOOK_WISE') then
		set @saleQry=concat("select game_nbr gameNo,game_name gameName,ifnull((nbr_of_tickets_per_book*ticket_price),0.0) unitPriceAmt,ifnull(noOfTkt,0) noOfTkt,ifnull(mrpAmt,0.0) mrpAmt,ifnull(netAmt,0.0) netAmt,ifnull(noLosTkts,0) noOfLosTkts from st_se_game_master gm left outer join (select sale.game_id,sum(ifnull(noOfTkt,0)-ifnull(noOfTktRet,0)) noOfTkt,sum(ifnull(mrpAmt,0.0)-ifnull(mrpAmtRet,0.0)) mrpAmt,sum(ifnull(netAmt,0.0)-ifnull(netAmtRet,0.0)) netAmt,sum(ifnull(noLosTkts,0)-ifnull(noLosTktsRet,0)) noLosTkts from (select game_id, sum(noLosTkts) noLosTkts, sum(noOfTkt) noOfTkt,sum(mrpAmt) mrpAmt,sum(netAmt) netAmt,0 noLosTktsRet,0 noOfTktRet,0.0 mrpAmtRet,0.0 netAmtRet from (select game_id, 0 noLosTkts, sum(nbr_of_books) noOfTkt,sum(mrp_amt) mrpAmt,sum(net_amt) netAmt,0 noOfTktRet,0.0 mrpAmtRet,0.0 netAmtRet from st_se_bo_agent_transaction where transaction_id in (select transaction_id from st_lms_bo_transaction_master where transaction_type='SALE' and transaction_date>='",startDate,"' and transaction_date<='",endDate,"') group by game_id union all select game_id,sum(nbrOfTickets) noLosTkts ,0 noOfTkt ,sum(mrp_amt) mrpAmt,sum(net_amt) netAmt,0 noOfTktRet,0.0 mrpAmtRet,0.0 netAmtRet from st_se_bo_agent_loose_book_transaction where transaction_id in (select transaction_id from st_lms_bo_transaction_master where transaction_type='LOOSE_SALE' and transaction_date>='",startDate, "' and transaction_date<='",endDate,"') group by game_id ) sale group by game_id union all select game_id,sum(noLosTkts) noLosTkts,sum(noOfTkt) noOfTkt,sum(mrpAmt) mrpAmt,sum(netAmt) netAmt,sum(noLosTktsRet) noLosTktsRet,sum(noOfTktRet) noOfTktRet,sum(mrpAmtRet) mrpAmtRet,sum(netAmtRet) netAmtRet from (select game_id,0 noLosTkts,0 noOfTkt,0.0 mrpAmt,0.0 netAmt,0 noLosTktsRet,sum(nbr_of_books) noOfTktRet,sum(mrp_amt) mrpAmtRet,sum(net_amt) netAmtRet from st_se_bo_agent_transaction where transaction_id in (select transaction_id from st_lms_bo_transaction_master where transaction_type='SALE_RET' and transaction_date>='",startDate,"' and transaction_date<='",endDate,"') group by game_id  union all select game_id,0 noLosTkts,0 noOfTkt,0.0 mrpAmt,0.0 netAmt,sum(nbrOfTickets) noLosTktsRet,0 noOfTktRet,sum(mrp_amt) mrpAmtRet,sum(net_amt) netAmtRet  from st_se_bo_agent_loose_book_transaction where transaction_id in (select transaction_id from st_lms_bo_transaction_master where transaction_type='LOOSE_SALE_RET' and transaction_date>='",startDate,"' and transaction_date<='",endDate,"') group by game_id) saleRet group by game_id ) sale group by game_id) saleTlb on saleTlb.game_id=gm.game_id ");
	elseif(@repType='TICKET_WISE') then
		set @saleQry=concat("select game_nbr gameNo,game_name gameName,ticket_price unitPriceAmt,sum(soldTkt) noOfTkt,sum(soldTkt*ticket_price) mrpAmt,sum((soldTkt*ticket_price)-(soldTkt*ticket_price*transacrion_sale_comm_rate*0.01)) netAmt from st_se_game_master gm,st_se_game_inv_detail gid,(select game_id,book_nbr,sum(sold_tickets) soldTkt from st_se_game_ticket_inv_history where date>='",startDate,"' and date<='",endDate,"' and current_owner='RETAILER' group by book_nbr) TktTlb where gm.game_id=TktTlb.game_id and TktTlb.book_nbr=gid.book_nbr and gid.current_owner='AGENT' group by gm.game_id");
	end if;
	
 	PREPARE stmt FROM @saleQry;
	EXECUTE stmt;
	DEALLOCATE PREPARE stmt;
    END 
#

CREATE      PROCEDURE `getScratchSaleRetailerId`(startDate date,endDate date,agentOrgId int)
BEGIN
set @getRetailerId=concat("select distinct ret.retailer_org_id from(select distinct bo.retailer_org_id from st_se_agent_retailer_transaction bo,st_lms_agent_transaction_master btm where btm.transaction_id=bo.transaction_id  and ( btm.transaction_date>='",startDate,"' and btm.transaction_date<'",endDate,"') and btm.user_org_id=",agentOrgId,"
union all select distinct organization_id retailer_org_id from st_rep_se_retailer  where finaldate>='",startDate,"' and finaldate <'",endDate,"' and parent_id=",agentOrgId," and (sale_book_mrp+ref_sale_mrp)!=0) ret");
  
prepare stmt from @getRetailerId;
execute stmt;
deallocate prepare stmt;
    END
#


CREATE  PROCEDURE `scratchPwtGameWise`(startDate timestamp,endDate timestamp)
BEGIN
	set @pwtQry=concat("select gameNo,gameName,sum(mrpAmt) mrpAmt,sum(netAmt) netAmt from (select game_nbr gameNo, game_name gameName,mrpAmt,netAmt from st_se_game_master gm,(select game_id,sum(pwt_amt) mrpAmt,sum(net_amt) netAmt from st_se_bo_pwt where transaction_id in(select transaction_id from st_lms_bo_transaction_master where transaction_date>='",startDate,"' and transaction_date<'",endDate,"') group by game_id) pwtTlb where gm.game_id=pwtTlb.game_id  union all 
select game_nbr gameNo, game_name gameName,mrpAmt,netAmt from st_se_game_master gm,(select game_id,sum(pwt_amt) mrpAmt,sum(pwt_amt) netAmt from st_se_direct_player_pwt where transaction_date>='",startDate,"' and transaction_date<'",endDate,"' group by game_id union all select game_id,sum(pwt_mrp) mrpAmt,sum(pwt_net_amt) netAmt from st_rep_se_bo where finaldate>='",startDate,"' and finaldate<'",endDate,"' group by game_id)pwtTlb where gm.game_id=pwtTlb.game_id) tlb group by gameNo");

	prepare stmt from @pwtQry;
	execute stmt;
	deallocate prepare stmt;
    END      

#


