--liquibase formatted sql

--changeset BaseSPRMS:6 endDelimiter:#

  
#
CREATE      PROCEDURE `scratchSaleAgentWiseExpand`(startDate Timestamp,endDate TimeStamp,agentOrgId int)
BEGIN
	set @repType:=(select value from st_lms_property_master where property_dev_name='SE_SALE_REP_TYPE');
	if(@repType='BOOK_WISE') then
		set @saleQry=concat("select game_nbr gameNo,game_name gameName,ifnull((nbr_of_tickets_per_book*ticket_price),0.0) unitPriceAmt,ifnull(noOfTkt,0) noOfTkt,ifnull(mrpAmt,0.0) mrpAmt,ifnull(netAmt,0.0) netAmt,ifnull(noLosTkts,0) noOfLosTkts from st_se_game_master gm left outer join (select sale.game_id,sum(ifnull(noOfTkt,0)-ifnull(noOfTktRet,0)) noOfTkt,sum(ifnull(mrpAmt,0.0)-ifnull(mrpAmtRet,0.0)) mrpAmt,sum(ifnull(netAmt,0.0)-ifnull(netAmtRet,0.0)) netAmt,sum(ifnull(noLosTkts,0)-ifnull(noLosTktsRet,0)) noLosTkts from (select game_id, sum(noLosTkts) noLosTkts, sum(noOfTkt) noOfTkt,sum(mrpAmt) mrpAmt,sum(netAmt) netAmt,0 noLosTktsRet,0 noOfTktRet,0.0 mrpAmtRet,0.0 netAmtRet from (select game_id, 0 noLosTkts, sum(nbr_of_books) noOfTkt,sum(mrp_amt) mrpAmt,sum(net_amt) netAmt,0 noOfTktRet,0.0 mrpAmtRet,0.0 netAmtRet from st_se_bo_agent_transaction where transaction_id in (select transaction_id from st_lms_bo_transaction_master where transaction_type='SALE' and transaction_date>='",startDate,"' and transaction_date<='",endDate,"') and agent_org_id=",agentOrgId," group by game_id union all select game_id,sum(nbrOfTickets) noLosTkts ,0 noOfTkt ,sum(mrp_amt) mrpAmt,sum(net_amt) netAmt,0 noOfTktRet,0.0 mrpAmtRet,0.0 netAmtRet from st_se_bo_agent_loose_book_transaction where transaction_id in (select transaction_id from st_lms_bo_transaction_master where transaction_type='LOOSE_SALE' and transaction_date>='",startDate,"' and transaction_date<='",endDate,"') and agent_org_id=",agentOrgId," group by game_id ) sale group by game_id union all select game_id,sum(noLosTkts) noLosTkts,sum(noOfTkt) noOfTkt,sum(mrpAmt) mrpAmt,sum(netAmt) netAmt,sum(noLosTktsRet) noLosTktsRet,sum(noOfTktRet) noOfTktRet,sum(mrpAmtRet) mrpAmtRet,sum(netAmtRet) netAmtRet from (select game_id,0 noLosTkts,0 noOfTkt,0.0 mrpAmt,0.0 netAmt,0 noLosTktsRet,sum(nbr_of_books) noOfTktRet,sum(mrp_amt) mrpAmtRet,sum(net_amt) netAmtRet from st_se_bo_agent_transaction where transaction_id in (select transaction_id from st_lms_bo_transaction_master where transaction_type='SALE_RET' and transaction_date>='",startDate,"' and transaction_date<='",endDate,"') and agent_org_id=",agentOrgId," group by game_id  union all select game_id,0 noLosTkts,0 noOfTkt,0.0 mrpAmt,0.0 netAmt,sum(nbrOfTickets) noLosTktsRet,0 noOfTktRet,sum(mrp_amt) mrpAmtRet,sum(net_amt) netAmtRet  from st_se_bo_agent_loose_book_transaction where transaction_id in (select transaction_id from st_lms_bo_transaction_master where transaction_type='LOOSE_SALE_RET' and transaction_date>='",startDate,"' and transaction_date<='",endDate,"') and agent_org_id=",agentOrgId," group by game_id) saleRet group by game_id ) sale group by game_id) saleTlb on saleTlb.game_id=gm.game_id");
	elseif(@repType='TICKET_WISE') then
		set @saleQry=concat("select game_nbr gameNo,game_name gameName,ticket_price unitPriceAmt,sum(soldTkt) noOfTkt,sum(soldTkt*ticket_price) mrpAmt,sum((soldTkt*ticket_price)-(soldTkt*ticket_price*transacrion_sale_comm_rate*0.01)) netAmt from st_se_game_master gm,st_se_game_inv_detail gid,(select game_id,book_nbr,sum(sold_tickets) soldTkt from st_se_game_ticket_inv_history where date>='",startDate,"' and date<='",endDate,"' and current_owner_id in(select organization_id from st_lms_organization_master where parent_id=",agentOrgId,") group by book_nbr) TktTlb where gm.game_id=TktTlb.game_id and TktTlb.book_nbr=gid.book_nbr and gid.current_owner='AGENT' group by gm.game_id");
	end if;
	
 	PREPARE stmt FROM @saleQry;
	EXECUTE stmt;
	DEALLOCATE PREPARE stmt;
    END      
#
CREATE      PROCEDURE `scratchSaleCancelDateWisePerGame`(startDate timestamp,endDate timestamp,gameNo int,agtOrgId int)
BEGIN
set @repType:=(select value from st_lms_property_master where property_dev_name='SE_SALE_REP_TYPE');
    
    if(@repType='BOOK_WISE') then
        set @saleQry=concat("select agent_org_id,ifnull(sum(mrp_amt),0.0) as mrp_amt,ifnull(sum(netAmt),0.0) netAmt, date from ((select sbt.agent_org_id,ifnull(sum(mrp_amt),0.0) as mrp_amt,ifnull(sum(net_amt),0.0) netAmt,date(btm.transaction_date) date from st_se_bo_agent_transaction sbt,st_lms_bo_transaction_master btm where  game_id=",gameNo," and sbt.transaction_id=btm.transaction_id and btm.transaction_type ='SALE' and transaction_date>='",startDate,"' and transaction_date<='" ,endDate,"' and sbt.agent_org_id=",agtOrgId ," group by date(transaction_date)) union all (select sbt.agent_org_id,ifnull(sum(mrp_amt),0.0) as mrp_amt,ifnull(sum(net_amt),0.0) netAmt,date(btm.transaction_date) date from st_se_bo_agent_loose_book_transaction sbt,st_lms_bo_transaction_master btm where  game_id=",gameNo," and sbt.transaction_id=btm.transaction_id and btm.transaction_type ='LOOSE_SALE' and transaction_date>='",startDate,"' and transaction_date<='" ,endDate,"' and sbt.agent_org_id=",agtOrgId ," group by date(transaction_date)))saleTlb group by date");
        set @cancelQry=concat("select agent_org_id,ifnull(sum(mrp_amt),0.0) as mrp_amt,ifnull(sum(cancel),0.0) cancel, date from ((select sbt.agent_org_id,ifnull(sum(mrp_amt),0.0) as mrp_amt,ifnull(sum(net_amt),0.0) cancel,date(btm.transaction_date) date from st_se_bo_agent_transaction sbt,st_lms_bo_transaction_master btm where  game_id=",gameNo," and sbt.transaction_id=btm.transaction_id and btm.transaction_type ='SALE_RET' and transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and sbt.agent_org_id=",agtOrgId," group by date(transaction_date)) union all (select sbt.agent_org_id,ifnull(sum(mrp_amt),0.0) as mrp_amt,ifnull(sum(net_amt),0.0) cancel,date(btm.transaction_date) date from st_se_bo_agent_loose_book_transaction sbt,st_lms_bo_transaction_master btm where  game_id=",gameNo," and sbt.transaction_id=btm.transaction_id and btm.transaction_type ='LOOSE_SALE_RET' and transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and sbt.agent_org_id=",agtOrgId," group by date(transaction_date))) cancelTlb group by date");
    elseif(@repType='TICKET_WISE') then
        set @saleQry=concat("select gid.current_owner_id,sum(soldTkt*ticket_price) mrpAmt,sum((soldTkt*ticket_price)-(soldTkt*ticket_price*transacrion_sale_comm_rate*0.01)) netAmt,date(transaction_date) date from st_se_game_master gm,st_se_game_inv_detail gid,(select game_id,book_nbr,sum(sold_tickets) soldTkt from st_se_game_ticket_inv_history where  game_id=",gameNo," and date>='",startDate,"' and date<='" ,endDate,"' and current_owner='RETAILER' group by book_nbr) TktTlb where gm.game_id=TktTlb.game_id and TktTlb.book_nbr=gid.book_nbr and gid.current_owner='AGENT' and gid.current_owner_id=", agtOrgId ," group by date(transaction_date)");
    end if;
     
    
if(@repType='BOOK_WISE') then
set @mainQry=concat("select saleTlb.agent_org_id,ifnull(saleTlb.date,cancelTlb.date) date,ifnull(sum(netAmt),0.0) sale,ifnull(sum(cancel),0.0) cancel from((",@saleQry,") saleTlb left join (",@cancelQry,") cancelTlb on saleTlb.date=cancelTlb.date )group by date union select saleTlb.agent_org_id,ifnull(saleTlb.date,cancelTlb.date) date,ifnull(sum(netAmt),0.0) sale,ifnull(sum(cancel),0.0) cancel from((",@saleQry,") saleTlb right join (",@cancelQry,") cancelTlb on saleTlb.date=cancelTlb.date )group by date union all select organization_id,finaldate,sum(sale_book_net) as sale, sum(ref_net_amt) as cancel from st_rep_se_agent where organization_id=",agtOrgId ," and game_id=",gameNo," and finaldate>=date('",startDate,"') and finaldate<=date('",endDate,"') group by finaldate");
else
set @mainQry=concat("select gid.current_owner_id,sum((soldTkt*ticket_price)-(soldTkt*ticket_price*transacrion_sale_comm_rate*0.01)) sale,date(transaction_date) date from st_se_game_master gm,st_se_game_inv_detail gid,(select game_id,book_nbr,sum(sold_tickets) soldTkt from st_se_game_ticket_inv_history where date>='",startDate,"' and date<='" , endDate,"' and current_owner='RETAILER' group by book_nbr) TktTlb where gm.game_id=TktTlb.game_id and TktTlb.book_nbr=gid.book_nbr and gid.current_owner='AGENT' and gid.current_owner_id=", agtOrgId ," group by date(transaction_date) union all select organization_id, sum(sale_ticket_net) as sale,finaldate from st_rep_se_agent where organization_id=",agtOrgId ,"  and game_id=",gameNo,", finaldate>=date('",startDate,"') and finaldate<=date('",endDate,"')  group by finaldate ");
end if ;
     
    PREPARE stmt FROM @mainQry;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;
    END
#
CREATE      PROCEDURE `scratchSaleReportRetailerWise`(startDate date,endDate date,agentOrgId int,RetailerOrgId int)
BEGIN
set @saleQry=concat("select sum(net_sale_amt) net_sale_amt,sum(sale_books_mrp) sale_books_mrp , sum(net_books_returned_amt) net_books_returned_amt , sum(return_books_mrp) return_books_mrp from(select ifnull(a.cc,0) 'net_sale_amt', ifnull(a.mm,0) 'sale_books_mrp', ifnull(b.dd,0) 'net_books_returned_amt', ifnull(b.mm,0) 'return_books_mrp'  from ((select sum(mrp_amt) mm, sum(net_amt) cc from st_se_agent_retailer_transaction bo, st_lms_agent_transaction_master btm where btm.transaction_id=bo.transaction_id and bo.transaction_type ='SALE' and ( btm.transaction_date>='",startDate,"' and btm.transaction_date<'",endDate,"') and btm.user_org_id =",agentOrgId," and bo.retailer_org_id=",RetailerOrgId,") a ,  (select sum(mrp_amt) mm, sum(net_amt) dd  from st_se_agent_retailer_transaction bo, st_lms_agent_transaction_master btm where btm.transaction_id=bo.transaction_id and bo.transaction_type ='SALE_RET'  and ( btm.transaction_date>='",startDate,"' and btm.transaction_date<'",endDate,"') and btm.user_org_id =",agentOrgId," and bo.retailer_org_id=",RetailerOrgId," ) b)
union all select sum(sale_book_net) 'net_sale_amt' , sum(sale_book_mrp) 'sale_books_mrp' , sum(ref_net_amt) 'net_books_returned_amt',sum(ref_sale_mrp) 'return_books_mrp' from st_rep_se_retailer where finaldate>='",startDate,"' and finaldate<'",endDate,"' and parent_id=",agentOrgId," and organization_id=",RetailerOrgId,")ret");
prepare stmt from @saleQry;
execute stmt;
deallocate prepare stmt;
    END
#

CREATE      PROCEDURE `SLEAgtReporting`(startDate TIMESTAMP,endDate TIMESTAMP)
BEGIN
    DECLARE done INT DEFAULT 0;
    DECLARE gameId INT;
    DECLARE saleResult INT;
    DECLARE cancelResult INT;
    DECLARE pwtResult INT;
    
SET @saleQry=CONCAT("insert into st_rep_sle_agent(organization_id, parent_id,finaldate,game_id,game_type_id,sale_mrp,sale_comm,sale_net,sale_good_cause,sale_vat,sale_taxable) select organization_id,parent_id,alldate,om.game_id,om.game_type_id,ifnull(saleMrp,0.00) saleMrp,ifnull(agtComm,0.00) agtComm,ifnull(saleNet,0.00) saleNet,ifnull(govtComm,0.00) govtComm,ifnull(vatAmt,0.00) vatAmt,ifnull(taxableSale,0.00) taxableSale from (select organization_id,parent_id,date(alldate) alldate,game_id,game_type_id from st_lms_organization_master,tempdate,st_sle_game_type_master where organization_type='AGENT') om 
left outer join (select drs.agent_org_id,date(drs.transaction_date) tx_date,drs.game_id,game_type_id,sum(mrp_amt) saleMrp,sum(agent_comm_amt) agtComm ,sum(bo_net_amt) saleNet,sum(govt_comm) govtComm,sum(vat_amt) vatAmt,sum(taxable_sale) taxableSale from st_sle_agt_sale drs,st_lms_agent_transaction_master rtm where drs.transaction_id=rtm.transaction_id and rtm.transaction_date>='",startDate,"' and rtm.transaction_date<='",endDate,"' and transaction_type ='SLE_SALE'  group by agent_org_id,tx_date,game_type_id ,game_id) sale on organization_id=agent_org_id and alldate=tx_date and sale.game_type_id=om.game_type_id and sale.game_id=om.game_id");
        PREPARE stmt FROM @saleQry;
        EXECUTE stmt;
        DEALLOCATE PREPARE stmt;
    DROP TEMPORARY TABLE IF EXISTS table3;
    
	
    SET @tab1 = CONCAT("CREATE TEMPORARY TABLE table3(agent_org_id int(10),tx_date date,game_id int(10),game_type_id int(10),saleRefMrp decimal(20,2),agtCommRef decimal(20,2),saleRefNet decimal(20,2),govtCommRef decimal(20,2),vatAmtRef decimal(20,2),taxableSaleRef decimal(20,2),primary key(agent_org_id,tx_date,game_type_id)) AS select drs.agent_org_id,date(rtm.transaction_date) tx_date,game_id,game_type_id,sum(mrp_amt) saleRefMrp,sum(agent_comm_amt) agtCommRef ,sum(bo_net_amt) saleRefNet,sum(govt_comm) govtCommRef,sum(vat_amt) vatAmtRef,sum(taxable_sale) taxableSaleRef from st_sle_agt_sale_refund drs,st_lms_agent_transaction_master rtm where drs.transaction_id=rtm.transaction_id and rtm.transaction_date>='",startDate,"' and rtm.transaction_date<='",endDate,"' and transaction_type in('SLE_REFUND_CANCEL','SLE_REFUND_FAILED')  group by agent_org_id,tx_date,game_type_id,game_id");
        PREPARE stmt FROM @tab1;
        EXECUTE stmt;
        DEALLOCATE PREPARE stmt;
    SELECT @tab1;
        
SET @cancelQry=CONCAT("update table3 cancel,st_rep_sle_agent dg set ref_sale_mrp=saleRefMrp,ref_comm=agtCommRef,ref_net_amt=saleRefNet,ref_good_cause=govtCommRef,ref_vat=vatAmtRef,ref_taxable=taxableSaleRef  where cancel.agent_org_id=dg.organization_id and dg.finaldate=cancel.tx_date and dg.game_type_id=cancel.game_type_id and dg.game_id=cancel.game_id");
        PREPARE stmt FROM @cancelQry;
        EXECUTE stmt;
        DEALLOCATE PREPARE stmt;
    DROP TEMPORARY TABLE IF EXISTS table4;
    
    SET @tab2=CONCAT("CREATE TEMPORARY TABLE table4 (agent_org_id INT(10), tx_date DATE, game_id INT(10), game_type_id INT(10), pwtMrp DECIMAL(20,2), retCommPwt DECIMAL(20,2), pwtNet DECIMAL(20,2), PRIMARY KEY(agent_org_id, tx_date, game_type_id)) AS SELECT sap.agent_org_id, DATE(transaction_date) tx_date, game_id, game_type_id, SUM(pwt_amt) pwtMrp, SUM(agt_claim_comm) retCommPwt, SUM(pwt_amt+agt_claim_comm) pwtNet FROM st_sle_agt_pwt sap INNER JOIN st_lms_agent_transaction_master atm ON sap.transaction_id=atm.transaction_id WHERE atm.transaction_date>='",startDate,"' AND atm.transaction_date<='",endDate,"' AND transaction_type IN ('SLE_PWT_AUTO','SLE_PWT_PLR','SLE_PWT') GROUP BY agent_org_id, tx_date, game_id, game_type_id");
        PREPARE stmt FROM @tab2;
        EXECUTE stmt;
        DEALLOCATE PREPARE stmt;
        
        SET @pwtQry=CONCAT("UPDATE table4 pwt, st_rep_sle_agent sle SET pwt_mrp=pwtMrp, ret_pwt_comm=retCommPwt, pwt_net_amt=pwtNet WHERE pwt.agent_org_id=sle.organization_id AND sle.finaldate=pwt.tx_date AND sle.game_id=pwt.game_id AND sle.game_type_id=pwt.game_type_id");
        PREPARE stmt FROM @pwtQry;
        EXECUTE stmt;
        DEALLOCATE PREPARE stmt;

	SET @dirPwtQry=CONCAT("UPDATE (SELECT agent_org_id, DATE(transaction_date) tx_date, game_id, game_type_id, SUM(pwt_amt)AS pwtMrp, SUM(agt_claim_comm) AS agtCommPwt, SUM(pwt_amt+agt_claim_comm) AS pwtNet, SUM(tax_amt) AS pwtTax FROM st_sle_agent_direct_plr_pwt pwt WHERE transaction_date>='",startDate,"' AND transaction_date<='",endDate,"' GROUP BY DATE(transaction_date), agent_org_id, game_id, game_type_id) pwt, st_rep_sle_agent sle SET direct_pwt_amt=pwtMrp, direct_pwt_comm=agtCommPwt, direct_pwt_net_amt=pwtNet, direct_pwt_tax=pwtTax WHERE pwt.agent_org_id=sle.organization_id AND sle.finaldate=pwt.tx_date AND sle.game_id=pwt.game_id AND sle.game_type_id=pwt.game_type_id");
        PREPARE stmt FROM @dirPwtQry;
        EXECUTE stmt;
        DEALLOCATE PREPARE stmt;
    END
#

CREATE      PROCEDURE `SLEBOReporting`(startDate TIMESTAMP,endDate TIMESTAMP)
BEGIN
	DECLARE done INT DEFAULT 0;
	DECLARE gameId INT;
	
	
	SET @saleQry=CONCAT("insert into st_rep_sle_bo(organization_id, parent_id,finaldate,game_id,game_type_id,sale_mrp,sale_comm,sale_net,sale_good_cause,sale_vat,sale_taxable) 
select organization_id,parent_id,alldate,om.game_id,om.game_type_id,ifnull(saleMrp,0.00) saleMrp,ifnull(agtComm,0.00) agtComm,ifnull(saleNet,0.00) saleNet,ifnull(govtComm,0.00) govtComm,ifnull(vatAmt,0.00) vatAmt,ifnull(taxableSale,0.00) taxableSale from (select organization_id,parent_id,date(alldate) alldate,game_id,game_type_id from st_lms_organization_master,tempdate,st_sle_game_type_master where organization_type='BO') om left outer join (select date(rtm.transaction_date) tx_date,game_id,game_type_id, sum(mrp_amt) saleMrp,sum(agent_comm) agtComm ,sum(net_amt) saleNet,sum(govt_comm) govtComm,sum(vat_amt) vatAmt,sum(taxable_sale) taxableSale from st_sle_bo_sale drs,st_lms_bo_transaction_master rtm where drs.transaction_id=rtm.transaction_id and rtm.transaction_date>='",startDate,"' and rtm.transaction_date<='",endDate,"' and transaction_type = 'SLE_SALE'  group by tx_date,game_id,game_type_id) sale on alldate=tx_date and sale.game_type_id=om.game_type_id and sale.game_id=om.game_id");
		PREPARE stmt FROM @saleQry;
		EXECUTE stmt;
		DEALLOCATE PREPARE stmt;
		
		SET @cancelQry=CONCAT("update (select date(rtm.transaction_date) tx_date,game_id,game_type_id,sum(mrp_amt) saleRefMrp,sum(agent_comm) agtCommRef ,sum(net_amt) saleRefNet,sum(govt_comm) govtCommRef,sum(vat_amt) vatAmtRef,sum(taxable_sale) taxableSaleRef from st_sle_bo_sale_refund drs,st_lms_bo_transaction_master rtm where drs.transaction_id=rtm.transaction_id and rtm.transaction_date>='",startDate,"' and rtm.transaction_date<='",endDate,"' and transaction_type in('SLE_REFUND_CANCEL','SLE_REFUND_FAILED')  group by tx_date,game_type_id,game_id) cancel,st_rep_sle_bo dg set ref_sale_mrp=saleRefMrp,ref_comm=agtCommRef,ref_net_amt=saleRefNet,ref_good_cause=govtCommRef,ref_vat=vatAmtRef,ref_taxable=taxableSaleRef  where dg.finaldate=cancel.tx_date and dg.game_id=cancel.game_id");
		PREPARE stmt FROM @cancelQry;
		EXECUTE stmt;
		DEALLOCATE PREPARE stmt;
		SET @pwtQry=CONCAT("UPDATE (SELECT DATE(transaction_date) tx_date, game_id, game_type_id, SUM(pwt_amt) pwtMrp, SUM(comm_amt) retCommPwt, SUM(pwt_amt+comm_amt) pwtNet FROM st_sle_bo_pwt sbp INNER JOIN st_lms_bo_transaction_master btm ON sbp.transaction_id=btm.transaction_id WHERE btm.transaction_date>='",startDate,"' AND btm.transaction_date<='",endDate,"' AND transaction_type IN('SLE_PWT_AUTO','SLE_PWT_PLR','SLE_PWT') GROUP BY tx_date, game_id, game_type_id) pwt, st_rep_sle_bo sle SET pwt_mrp=pwtMrp, ret_pwt_comm=retCommPwt, pwt_net_amt=pwtNet WHERE sle.finaldate=pwt.tx_date AND sle.game_id=pwt.game_id AND sle.game_type_id=pwt.game_type_id");
		PREPARE stmt FROM @pwtQry;
		EXECUTE stmt;
		DEALLOCATE PREPARE stmt;
SET @dirPwtQry=CONCAT("UPDATE (SELECT DATE(transaction_date) tx_date, game_id, game_type_id, SUM(pwt_amt)AS pwtMrp, 0.00 AS agtCommPwt, SUM(pwt_amt) AS pwtNet, SUM(tax_amt) AS pwtTax FROM st_sle_bo_direct_plr_pwt pwt WHERE transaction_date>='",startDate,"' AND transaction_date<='",endDate,"' GROUP BY DATE(transaction_date), game_id, game_type_id) pwt, st_rep_sle_bo sle SET direct_pwt_amt=pwtMrp, direct_pwt_comm=agtCommPwt, direct_pwt_net_amt=pwtNet, direct_pwt_tax=pwtTax WHERE sle.finaldate=pwt.tx_date AND sle.game_id=pwt.game_id AND sle.game_type_id=pwt.game_type_id");
		PREPARE stmt FROM @dirPwtQry;
		EXECUTE stmt;
		DEALLOCATE PREPARE stmt;
SET @insStatus=CONCAT("insert into archiving_status(job_name, status)values('DG_BO_REPORTING', 'DONE')");
PREPARE stmt FROM @insStatus;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
    END
#

CREATE      PROCEDURE `SLERetReporting`(startDate TIMESTAMP,endDate TIMESTAMP)
BEGIN
    DECLARE done INT DEFAULT 0;
    DECLARE gameId INT;
SET @saleQry=CONCAT("insert into st_rep_sle_retailer(organization_id, parent_id,finaldate,game_id,game_type_id,sale_mrp,sale_comm,sale_net,sale_good_cause,sale_vat,sale_taxable) 
select organization_id,parent_id,alldate,om.game_id,om.game_type_id,ifnull(saleMrp,0.00) saleMrp,ifnull(retComm,0.00) retComm,ifnull(saleNet,0.00) saleNet,ifnull(govtComm,0.00) govtComm,ifnull(vatAmt,0.00) vatAmt,ifnull(taxableSale,0.00) taxableSale from (select organization_id,parent_id, game_id, game_type_id,date(alldate) alldate from st_lms_organization_master,tempdate, st_sle_game_type_master where organization_type='RETAILER') om left outer join (select drs.game_id , game_type_id, drs.retailer_org_id,date(rtm.transaction_date) tx_date,sum(mrp_amt) saleMrp,sum(retailer_comm_amt) retComm ,sum(retailer_net_amt) saleNet,sum(good_cause_amt) govtComm,sum(vat_amt) vatAmt,sum(taxable_sale) taxableSale from st_sle_ret_sale drs,st_lms_retailer_transaction_master rtm where drs.transaction_id=rtm.transaction_id and rtm.transaction_date>='",startDate,"' and rtm.transaction_date<='",endDate,"' and transaction_type ='SLE_SALE' group by retailer_org_id,tx_date,game_type_id,game_id) sale on organization_id=retailer_org_id and alldate=tx_date and om.game_type_id=sale.game_type_id");
        PREPARE stmt FROM @saleQry;
        EXECUTE stmt;
        DEALLOCATE PREPARE stmt;
SET @a=CONCAT("select sum(saleNet-sale) result from (
select om.parent_id,ifnull(sum(reNet),0.0)saleNet from st_lms_organization_master om left outer join (
select drs.game_id,game_type_id,drs.retailer_org_id,sum(retailer_net_amt) reNet from st_sle_ret_sale drs,st_lms_retailer_transaction_master rtm where drs.transaction_id=rtm.transaction_id and rtm.transaction_date>='",startDate,"' and rtm.transaction_date<='",endDate,"' and transaction_type = 'SLE_SALE'  group by retailer_org_id , game_type_id ,game_id)sub on om.organization_id=sub.retailer_org_id group by parent_id)  first inner join (select parent_id,sum(sale_net) as sale from st_rep_sle_retailer where finaldate>=date('",startDate,"') and finaldate<=date('",endDate,"') group by parent_id) second on first.parent_id=second.parent_id");
PREPARE stmt FROM @a;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
    IF(@saleResult!=0)THEN
    CALL raise_error;
    END IF;
    DROP TEMPORARY TABLE IF EXISTS table1;
    
SET @tab1 = CONCAT("CREATE TEMPORARY TABLE table1(game_id INT(10), game_type_id INT(10),retailer_org_id int(10),tx_date date ,saleRefMrp decimal(20,2),retCommRef decimal(20,2),saleRefNet decimal(20,2),govtCommRef decimal(20,2),vatAmtRef decimal(20,2)   ,taxableSaleRef decimal(20,2),primary key(retailer_org_id,tx_date,game_type_id)) AS 
select drs.game_id , game_type_id,drs.retailer_org_id,date(rtm.transaction_date) tx_date,sum(mrp_amt) saleRefMrp,sum(retailer_comm_amt) retCommRef ,sum(retailer_net_amt) saleRefNet,sum(good_cause_amt) govtCommRef,sum(vat_amt) vatAmtRef,sum(taxable_sale) taxableSaleRef from st_sle_ret_sale_refund drs,st_lms_retailer_transaction_master rtm where drs.transaction_id=rtm.transaction_id and rtm.transaction_date>='",startDate,"' and rtm.transaction_date<='",endDate,"' and transaction_type in('SLE_REFUND_CANCEL','SLE_REFUND_FAILED')  group by retailer_org_id,tx_date,game_type_id,game_id");
    PREPARE stmt FROM @tab1;
        EXECUTE stmt;
        DEALLOCATE PREPARE stmt;
 
SET @cancelQry=CONCAT("update table1 cancel,st_rep_sle_retailer dg set ref_sale_mrp=saleRefMrp,ref_comm=retCommRef,ref_net_amt=saleRefNet,ref_good_cause=govtCommRef,ref_vat=vatAmtRef,ref_taxable=taxableSaleRef  where cancel.retailer_org_id=dg.organization_id and dg.finaldate=cancel.tx_date and dg.game_type_id=cancel.game_type_id and dg.game_id=cancel.game_id");
        PREPARE stmt FROM @cancelQry;
        EXECUTE stmt;
        DEALLOCATE PREPARE stmt;
SET @a=CONCAT("set @cancelResult=(select sum(saleRefNet-saleRef) result from (select om.parent_id,ifnull(sum(reNet),0.0)saleRefNet from st_lms_organization_master om left outer join (select drs.retailer_org_id,sum(retailer_net_amt) reNet from st_sle_ret_sale_refund drs,st_lms_retailer_transaction_master rtm where drs.transaction_id=rtm.transaction_id and rtm.transaction_date>='",startDate,"' and rtm.transaction_date<='",endDate,"' and transaction_type in('SLE_REFUND_CANCEL','SLE_REFUND_FAILED')  group by retailer_org_id)sub on om.organization_id=sub.retailer_org_id group by parent_id)first inner join (select parent_id,sum(ref_net_amt) as saleRef from st_rep_sle_retailer where finaldate>=date('",startDate,"') and finaldate<=date('",endDate,"') group by parent_id) second on first.parent_id=second.parent_id)");
PREPARE stmt FROM @a;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
    IF(@cancelResult!=0)THEN
     CALL raise_error;
    END IF;
    DROP TEMPORARY TABLE IF EXISTS table2;
    
    SET @tab2=CONCAT("CREATE TEMPORARY TABLE table2 (retailer_org_id INT(10), tx_date DATE, game_id INT(10), game_type_id INT(10), pwtMrp DECIMAL(20,2), retCommPwt DECIMAL(20,2), pwtNet DECIMAL(20,2), PRIMARY KEY(retailer_org_id,tx_date,game_type_id)) AS SELECT srp.retailer_org_id, DATE(srp.transaction_date) tx_date, srp.game_id, game_type_id, SUM(pwt_amt) pwtMrp, SUM(retailer_claim_comm) retCommPwt, SUM(pwt_amt+retailer_claim_comm) pwtNet FROM st_sle_ret_pwt srp INNER JOIN st_lms_retailer_transaction_master rtm ON srp.transaction_id=rtm.transaction_id WHERE rtm.transaction_date>='",startDate,"' AND rtm.transaction_date<='",endDate,"' AND transaction_type IN('SLE_PWT_AUTO','SLE_PWT_PLR','SLE_PWT') GROUP BY retailer_org_id, tx_date, srp.game_id, game_type_id");
        PREPARE stmt FROM @tab2;
        EXECUTE stmt;
        DEALLOCATE PREPARE stmt;
    
    SET @pwtQry=CONCAT("UPDATE table2 pwt, st_rep_sle_retailer sle SET pwt_mrp=pwtMrp, ret_pwt_comm=retCommPwt, pwt_net_amt=pwtNet WHERE pwt.retailer_org_id=sle.organization_id AND sle.finaldate=pwt.tx_date AND sle.game_id=pwt.game_id AND sle.game_type_id=pwt.game_type_id");
        PREPARE stmt FROM @pwtQry;
        EXECUTE stmt;
        DEALLOCATE PREPARE stmt;

	SET @a=CONCAT("set @pwtResult=(SELECT SUM(pwtNet-pwt) result FROM (SELECT om.parent_id, IFNULL(SUM(pwtNet),0.0) pwtNet FROM st_lms_organization_master om LEFT OUTER JOIN (SELECT srp.retailer_org_id, SUM(pwt_amt+retailer_claim_comm) pwtNet FROM st_sle_ret_pwt srp, st_lms_retailer_transaction_master rtm WHERE srp.transaction_id=rtm.transaction_id AND rtm.transaction_date>='",startDate,"' AND rtm.transaction_date<='",endDate,"' AND transaction_type IN ('SLE_PWT_AUTO','SLE_PWT_PLR','SLE_PWT') GROUP BY retailer_org_id)sub ON om.organization_id=sub.retailer_org_id GROUP BY parent_id) first INNER JOIN (SELECT parent_id, SUM(pwt_net_amt) AS pwt FROM st_rep_sle_retailer WHERE finaldate>=DATE('",startDate,"') AND finaldate<=DATE('",endDate,"') GROUP BY parent_id) second ON first.parent_id=second.parent_id)");
PREPARE stmt FROM @a;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
    IF(@pwtResult!=0)THEN
    CALL raise_error;
    END IF;
SET @insStatus=CONCAT("insert into archiving_status(job_name, status,result)values('SLE_RET_REPORTING', 'DONE',1)");
PREPARE stmt FROM @insStatus;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
SELECT "in the end";
    END      
#
CREATE      PROCEDURE `updateOrgCode`()
BEGIN 
DECLARE done INT DEFAULT 0;
DECLARE orgName varchar(100);
DECLARE orgType varchar(50);
DECLARE orgId int;
DECLARE parentId int;
DECLARE str  VARCHAR(255);
DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;
set @s2 =concat('drop temporary table if exists tmp_tab ');
prepare stmt from @s2;
execute stmt;
deallocate prepare stmt;
set @query ="create temporary table tmp_tab  select name,organization_type,organization_id,parent_id  from st_lms_organization_master  where org_code is null order by organization_id desc";
prepare stmt from @query;
execute stmt ;
set @s2 =concat('drop temporary table if exists tmp_retCount ');
prepare stmt from @s2;
execute stmt;
deallocate prepare stmt;
set @query ="create temporary table tmp_retCount  select count(*) retCount,parent_id from st_lms_organization_master  where organization_type='RETAILER' group by parent_id ";
prepare stmt from @query;
execute stmt ;
b2:begin
declare orgCur cursor for select name,organization_type,organization_id,parent_id from tmp_tab;
open orgCur ;
read_loop: LOOP
    FETCH orgCur INTO orgName,orgType,orgId,parentId ;
    IF done THEN
      LEAVE read_loop;
    END IF;
if(orgType='AGENT')then 
set @orgName =concat(" set @retName = ( select user_name from st_lms_user_master where organization_id=",orgId," and organization_type='AGENT'  and isrolehead='Y')" );
prepare stmt from @orgName;
execute stmt ; 
set @upOrgCode =concat(" update st_lms_organization_master set org_code='",@retName,"' where organization_id=",orgId,"");
prepare stmt from @upOrgCode;
execute stmt ;
end if ;
if(orgType='RETAILER')then 
set @orgCode =concat(" set @reCount = ( select retCount  from tmp_retCount  where parent_id=",parentId,")" );
prepare stmt from @orgCode;
execute stmt ;
set @orgName =concat(" set @retName = ( select user_name from st_lms_user_master where organization_id=",parentId," and organization_type='AGENT' and isrolehead='Y')" );
prepare stmt from @orgName;
execute stmt ;
SET  @str = CONCAT( @retName,@reCount);
set @upOrgCount =concat(" update tmp_retCount set retCount=retCount-1 where parent_id=",parentId,"");
prepare stmt from @upOrgCount;
execute stmt ;
set @upOrgCode =concat(" update st_lms_organization_master set org_code='",@str,"' where organization_id=",orgId,"");
prepare stmt from @upOrgCode;
execute stmt ;
end if ;
END LOOP;
CLOSE orgCur;
END b2;
END      
#
CREATE      PROCEDURE `VSAgtReporting`(startDate TIMESTAMP,endDate TIMESTAMP)
BEGIN
    DECLARE done INT DEFAULT 0;
    DECLARE gameId INT;
    DECLARE saleResult INT;
    DECLARE cancelResult INT;
    DECLARE pwtResult INT;
SET @saleQry=CONCAT("insert into st_rep_vs_agent(organization_id, parent_id,finaldate,game_id,sale_mrp,sale_comm,sale_net,sale_good_cause,sale_vat,sale_taxable) 
 select organization_id,p_id parent_id,alldate finaldate, game_id,ifnull(saleMrp,0.00) sale_mrp,ifnull(agtComm,0.00) sale_comm,ifnull(saleNet,0.00) sale_net,ifnull(govtComm,0.00) sale_good_cause,ifnull(vatAmt,0.00) sale_vat,ifnull(taxableSale,0.00) sale_taxable from (select organization_id,parent_id p_id, alldate,om.game_id game_id,ifnull(saleMrp,0.00) saleMrp,ifnull(agtComm,0.00) agtComm,ifnull(saleNet,0.00) saleNet,ifnull(govtComm,0.00) govtComm,ifnull(vatAmt,0.00) vatAmt,ifnull(taxableSale,0.00) taxableSale from (select organization_id,parent_id,date(alldate) alldate,game_id  from st_lms_organization_master,tempdate,st_vs_game_master where organization_type='AGENT') om left outer join (select b.parent_id parent ,drs.retailer_org_id,date(drs.transaction_date) tx_date,drs.game_id ,sum(mrp_amt) saleMrp,sum(retailer_comm_amt) agtComm ,sum(retailer_net_amt) saleNet,sum(good_cause_amt) govtComm,sum(vat_amt) vatAmt,sum(taxable_sale) taxableSale from  st_lms_retailer_transaction_master rtm, st_vs_ret_sale drs inner join st_lms_organization_master b on drs.retailer_org_id=b.organization_id where drs.transaction_id=rtm.transaction_id and rtm.transaction_date>='",startDate,"' and rtm.transaction_date<='",endDate,"'   and drs.status in('DONE')  and transaction_type ='VS_SALE' group by tx_date,game_id,parent ) sale on organization_id=parent and alldate=tx_date and sale.game_id=om.game_id)aa");
        PREPARE stmt FROM @saleQry;
        EXECUTE stmt;

        DEALLOCATE PREPARE stmt;

    
	
 DROP TEMPORARY TABLE IF EXISTS table4;
    
      SET @tab2=CONCAT("CREATE TEMPORARY TABLE table4 (agent_org_id INT(10), tx_date DATE, game_id INT(10), pwtMrp DECIMAL(20,2), retCommPwt DECIMAL(20,2), pwtNet DECIMAL(20,2), PRIMARY KEY(agent_org_id, tx_date, game_id)) AS select agent_org_id,tx_date, sum(pwtMrp) pwtMrp , sum(retCommPwt) retCommPwt ,sum(pwtNet) pwtNet,game_id from  (select org.organization_id agent_org_id,abc.tx_date,abc.pwtMrp,abc.retCommPwt,abc.pwtNet,abc.game_id from (SELECT sap.retailer_org_id R_id ,  DATE(sap.transaction_date) tx_date,SUM(pwt_amt) pwtMrp, SUM(agt_claim_comm) retCommPwt, SUM(pwt_amt+agt_claim_comm) pwtNet,sap.game_id FROM  st_vs_ret_pwt sap INNER JOIN  st_lms_retailer_transaction_master atm ON sap.transaction_id=atm.transaction_id WHERE atm.transaction_date>='",startDate,"'  and atm.transaction_date<='",endDate,"'  AND transaction_type IN ('VS_PWT') GROUP BY  R_id, tx_date, game_id)abc inner join (select organization_id A_id,parent_id organization_id from st_lms_organization_master where organization_type ='RETAILER')org on abc.R_id=org.A_id )qa group by tx_date,agent_org_id");         
PREPARE stmt FROM @tab2;
        EXECUTE stmt;


        DEALLOCATE PREPARE stmt;
        
        SET @pwtQry=CONCAT("UPDATE table4 pwt, st_rep_vs_agent vs SET pwt_mrp=pwtMrp, ret_pwt_comm=retCommPwt, pwt_net_amt=pwtNet WHERE pwt.agent_org_id=vs.organization_id AND vs.finaldate=pwt.tx_date AND vs.game_id=pwt.game_id");
        PREPARE stmt FROM @pwtQry;
        EXECUTE stmt;

        DEALLOCATE PREPARE stmt;
	
select 'VS_Agent_REPORTING Done!!!';
    END      
#
CREATE      PROCEDURE `VSBOReporting`(startDate TIMESTAMP,endDate TIMESTAMP)
BEGIN
	DECLARE done INT DEFAULT 0;
	DECLARE gameId INT;
	
	
	SET @saleQry=CONCAT("insert into st_rep_vs_bo(organization_id,parent_id,finaldate,game_id,sale_mrp,sale_comm,sale_net,sale_good_cause,sale_vat,sale_taxable)  select organization_id,p_id parent_id,alldate,game_id,ifnull(saleMrp,0.00) saleMrp,ifnull(boComm,0.00) boComm,ifnull(saleNet,0.00) saleNet,ifnull(govtComm,0.00) govtComm,ifnull(vatAmt,0.00) vatAmt,ifnull(taxableSale,0.00) taxableSale  from (select organization_id,parent_id p_id, alldate,om.game_id game_id,ifnull(saleMrp,0.00) saleMrp,ifnull(boComm,0.00) boComm,ifnull(saleNet,0.00) saleNet,ifnull(govtComm,0.00) govtComm,ifnull(vatAmt,0.00) vatAmt,ifnull(taxableSale,0.00) taxableSale from (select organization_id,parent_id,date(alldate) alldate,game_id  from st_lms_organization_master,tempdate,st_vs_game_master where organization_type='BO') om left outer join (select ro.parent_id bo_id, rt.* from (select b.parent_id parent ,drs.retailer_org_id,date(drs.transaction_date) tx_date,drs.game_id game_id,sum(mrp_amt) saleMrp,sum(retailer_comm_amt) boComm ,sum(retailer_net_amt) saleNet,sum(good_cause_amt) govtComm,sum(vat_amt) vatAmt,sum(taxable_sale) taxableSale from  st_lms_retailer_transaction_master rtm, st_vs_ret_sale drs inner join st_lms_organization_master b on drs.retailer_org_id=b.organization_id where drs.transaction_id=rtm.transaction_id and drs.status in('DONE')  and rtm.transaction_date>='",startDate,"' and rtm.transaction_date<='",endDate,"'   and transaction_type ='VS_SALE'  group by tx_date,game_id) rt  inner join st_lms_organization_master ro on rt.parent=ro.organization_id) qa on om.organization_id =qa.bo_id and alldate=tx_date and qa.game_id=om.game_id group by tx_date,game_id)aa");
		PREPARE stmt FROM @saleQry;
		EXECUTE stmt;
		DEALLOCATE PREPARE stmt;
		SET @cancelQry=CONCAT("update (select date(rtm.transaction_date) tx_date,drs.game_id game_id,sum(mrp_amt) saleRefMrp,sum(retailer_comm_amt) boCommRef ,sum(retailer_net_amt) saleRefNet,sum(good_cause_amt) govtCommRef,sum(vat_amt) vatAmtRef,sum(taxable_sale) taxableSaleRef from  st_vs_ret_sale_refund drs, st_lms_retailer_transaction_master rtm where drs.transaction_id=rtm.transaction_id  and rtm.transaction_date>='",startDate,"' and rtm.transaction_date<='",endDate,"'  and transaction_type in('VS_REFUND_CANCEL')  group by tx_date,game_id) cancel,st_rep_vs_bo vs set ref_sale_mrp=saleRefMrp,ref_comm=boCommRef,ref_net_amt=saleRefNet,ref_good_cause=govtCommRef,ref_vat=vatAmtRef,ref_taxable=taxableSaleRef  where vs.finaldate=cancel.tx_date and vs.game_id=cancel.game_id");
		PREPARE stmt FROM @cancelQry;
		EXECUTE stmt;
		DEALLOCATE PREPARE stmt;
		SET @pwtQry=CONCAT("UPDATE (SELECT DATE(sbp.transaction_date) tx_date, sbp.game_id game_id,  SUM(pwt_amt) pwtMrp, SUM(retailer_claim_comm) retCommPwt, SUM(pwt_amt+retailer_claim_comm) pwtNet FROM  st_vs_ret_pwt sbp INNER JOIN  st_lms_retailer_transaction_master btm ON sbp.transaction_id=btm.transaction_id WHERE btm.transaction_date>='",startDate,"' AND btm.transaction_date<='",endDate,"'  AND transaction_type IN('VS_PWT') GROUP BY tx_date, game_id) pwt, st_rep_vs_bo vs SET pwt_mrp=pwtMrp, ret_pwt_comm=retCommPwt, pwt_net_amt=pwtNet WHERE vs.finaldate=pwt.tx_date AND vs.game_id=pwt.game_id");
		PREPARE stmt FROM @pwtQry;
		EXECUTE stmt;
		DEALLOCATE PREPARE stmt;

SET @insStatus=CONCAT("insert into archiving_status(job_name, status)values('VS_BO_REPORTING', 'DONE')");
PREPARE stmt FROM @insStatus;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
select 'VS_BO_REPORTING Done!!!';
    END      
#
CREATE      PROCEDURE `VSRetReporting`(startDate TIMESTAMP,endDate TIMESTAMP)
BEGIN
    DECLARE done INT DEFAULT 0;
    DECLARE gameId INT;
DECLARE  gameCur CURSOR FOR SELECT game_id FROM st_vs_game_master;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;
    OPEN gameCur;
      read_loop: LOOP
        FETCH gameCur INTO gameId;
        IF done THEN
          LEAVE read_loop;
        END IF;
SET @saleQry=CONCAT("insert into st_rep_vs_retailer(organization_id, parent_id,finaldate,game_id,sale_mrp,sale_comm,sale_net,sale_good_cause,sale_vat,sale_taxable) 
select organization_id,parent_id,alldate,",gameId," game_id,ifnull(saleMrp,0.00) saleMrp,ifnull(retComm,0.00) retComm,ifnull(saleNet,0.00) saleNet,ifnull(govtComm,0.00) govtComm,ifnull(vatAmt,0.00) vatAmt,ifnull(taxableSale,0.00) taxableSale from (select organization_id,parent_id,date(alldate) alldate from st_lms_organization_master,tempdate where organization_type='RETAILER') om left outer join (select drs.game_id , drs.retailer_org_id,date(rtm.transaction_date) tx_date,sum(mrp_amt) saleMrp,sum(retailer_comm_amt) retComm ,sum(retailer_net_amt) saleNet,sum(good_cause_amt) govtComm,sum(vat_amt) vatAmt,sum(taxable_sale) taxableSale from st_vs_ret_sale drs,st_lms_retailer_transaction_master rtm where drs.transaction_id=rtm.transaction_id and rtm.transaction_date>='",startDate,"' and rtm.transaction_date<='",endDate,"' and drs.status in('DONE')  and transaction_type ='VS_SALE' and drs.game_id =",gameId," group by retailer_org_id,tx_date,game_id) sale on organization_id=retailer_org_id and alldate=tx_date");
        PREPARE stmt FROM @saleQry;
        EXECUTE stmt;
 select @saleQry;
       DEALLOCATE PREPARE stmt;
SET @a=CONCAT("select sum(saleNet-sale) result from (select om.parent_id,ifnull(sum(reNet),0.0)saleNet from st_lms_organization_master om left outer join (select drs.game_id,drs.retailer_org_id,sum(retailer_net_amt) reNet from st_vs_ret_sale drs,st_lms_retailer_transaction_master rtm where drs.transaction_id=rtm.transaction_id and rtm.transaction_date>='",startDate,"' and rtm.transaction_date<='",endDate,"' and transaction_type = 'VS_SALE' and drs.status in('DONE') and drs.game_id =",gameId," group by retailer_org_id , game_id)sub on om.organization_id=sub.retailer_org_id group by parent_id)  first inner join (select parent_id,sum(sale_net) as sale from st_rep_vs_retailer where finaldate>=date('",startDate,"') and finaldate<=date('",endDate,"') group by parent_id) second on first.parent_id=second.parent_id");
PREPARE stmt FROM @a;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
    IF(@saleResult!=0)THEN
    CALL raise_error;
    END IF;
    DROP TEMPORARY TABLE IF EXISTS table1;
    
SET @tab1 = CONCAT("CREATE TEMPORARY TABLE table1(retailer_org_id int(10),tx_date date ,saleRefMrp decimal(20,2),retCommRef decimal(20,2),saleRefNet decimal(20,2),govtCommRef decimal(20,2),vatAmtRef decimal(20,2)   ,taxableSaleRef decimal(20,2),primary key(retailer_org_id,tx_date)) AS 
select drs.game_id , drs.retailer_org_id,date(rtm.transaction_date) tx_date,sum(mrp_amt) saleRefMrp,sum(retailer_comm_amt) retCommRef ,sum(retailer_net_amt) saleRefNet,sum(good_cause_amt) govtCommRef,sum(vat_amt) vatAmtRef,sum(taxable_sale) taxableSaleRef from st_vs_ret_sale_refund drs,st_lms_retailer_transaction_master rtm where drs.transaction_id=rtm.transaction_id and drs.game_id =",gameId," and rtm.transaction_date>='",startDate,"' and rtm.transaction_date<='",endDate,"' and transaction_type in('VS_REFUND_CANCEL')  group by retailer_org_id,tx_date,game_id");
    PREPARE stmt FROM @tab1;
        EXECUTE stmt;
       DEALLOCATE PREPARE stmt;
 
SET @cancelQry=CONCAT("update table1 cancel,st_rep_vs_retailer dg set ref_sale_mrp=saleRefMrp,ref_comm=retCommRef,ref_net_amt=saleRefNet,ref_good_cause=govtCommRef,ref_vat=vatAmtRef,ref_taxable=taxableSaleRef  where cancel.retailer_org_id=dg.organization_id and dg.finaldate=cancel.tx_date and dg.game_id=cancel.game_id");
        PREPARE stmt FROM @cancelQry;
        EXECUTE stmt;
        DEALLOCATE PREPARE stmt;
SET @a=CONCAT("set @cancelResult=(select sum(saleRefNet-saleRef) result from (select om.parent_id,ifnull(sum(reNet),0.0)saleRefNet from st_lms_organization_master om left outer join (select drs.retailer_org_id,sum(retailer_net_amt) reNet from st_vs_ret_sale_refund drs,st_lms_retailer_transaction_master rtm where drs.transaction_id=rtm.transaction_id and rtm.transaction_date>='",startDate,"' and rtm.transaction_date<='",endDate,"' and drs.game_id =",gameId," and transaction_type in('VS_REFUND_CANCEL')  group by retailer_org_id)sub on om.organization_id=sub.retailer_org_id group by parent_id)first inner join (select parent_id,sum(ref_net_amt) as saleRef from st_rep_vs_retailer where finaldate>=date('",startDate,"') and finaldate<=date('",endDate,"') group by parent_id) second on first.parent_id=second.parent_id)");
PREPARE stmt FROM @a;
EXECUTE stmt;
select @a;
select @cancelResult;
DEALLOCATE PREPARE stmt;
    IF(@cancelResult!=0)THEN
     CALL raise_error;
    END IF;
    DROP TEMPORARY TABLE IF EXISTS table2;
    
    SET @tab2=CONCAT("CREATE TEMPORARY TABLE table2 (retailer_org_id INT(10), tx_date DATE, game_id INT(10),  pwtMrp DECIMAL(20,2), retCommPwt DECIMAL(20,2), pwtNet DECIMAL(20,2), PRIMARY KEY(retailer_org_id,tx_date)) AS SELECT srp.retailer_org_id, DATE(srp.transaction_date) tx_date, srp.game_id,  SUM(pwt_amt) pwtMrp, SUM(retailer_claim_comm) retCommPwt, SUM(pwt_amt+retailer_claim_comm) pwtNet FROM st_vs_ret_pwt srp INNER JOIN st_lms_retailer_transaction_master rtm ON srp.transaction_id=rtm.transaction_id WHERE rtm.transaction_date>='",startDate,"' AND rtm.transaction_date<='",endDate,"' and srp.game_id =",gameId,"  AND transaction_type IN('VS_PWT') GROUP BY retailer_org_id, tx_date, srp.game_id");
        PREPARE stmt FROM @tab2;
        EXECUTE stmt;
       DEALLOCATE PREPARE stmt;
    
    SET @pwtQry=CONCAT("UPDATE table2 pwt, st_rep_vs_retailer vs SET pwt_mrp=pwtMrp, ret_pwt_comm=retCommPwt, pwt_net_amt=pwtNet WHERE pwt.retailer_org_id=vs.organization_id AND vs.finaldate=pwt.tx_date AND vs.game_id=pwt.game_id ");
        PREPARE stmt FROM @pwtQry;
        EXECUTE stmt;
       DEALLOCATE PREPARE stmt;
	SET @a=CONCAT("set @pwtResult=(SELECT SUM(pwtNet-pwt) result FROM (SELECT om.parent_id, IFNULL(SUM(pwtNet),0.0) pwtNet FROM st_lms_organization_master om LEFT OUTER JOIN (SELECT srp.retailer_org_id, SUM(pwt_amt+retailer_claim_comm) pwtNet FROM st_vs_ret_pwt srp, st_lms_retailer_transaction_master rtm WHERE srp.transaction_id=rtm.transaction_id AND rtm.transaction_date>='",startDate,"' AND rtm.transaction_date<='",endDate,"' and srp.game_id =",gameId,"  AND transaction_type IN ('VS_PWT') GROUP BY retailer_org_id)sub ON om.organization_id=sub.retailer_org_id GROUP BY parent_id) first INNER JOIN (SELECT parent_id, SUM(pwt_net_amt) AS pwt FROM st_rep_vs_retailer WHERE finaldate>=DATE('",startDate,"') AND finaldate<=DATE('",endDate,"') GROUP BY parent_id) second ON first.parent_id=second.parent_id)");
PREPARE stmt FROM @a;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
 
  END LOOP;
    CLOSE gameCur;
SET @insStatus=CONCAT("insert into archiving_status(job_name, status,result)values('VS_RET_REPORTING', 'DONE',1)");
PREPARE stmt FROM @insStatus;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
SELECT "VS Retailer Reporting Done!!!";
    END #
