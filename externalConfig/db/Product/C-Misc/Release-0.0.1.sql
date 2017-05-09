--liquibase formatted sql

--changeset BaseSPRMS:4 endDelimiter:#
#
CREATE      PROCEDURE `scratchBODirPlyPwtGameWiseExpand`(startDate timestamp,endDate timestamp)
BEGIN
	set @pwtQry=concat("select game_nbr gameNo, game_name gameName,priceAmt,noOfTkt,mrpAmt from st_se_game_master gm,(select game_id,pwt_amt priceAmt,count(pwt_amt) noOfTkt,sum(pwt_amt) mrpAmt from st_se_direct_player_pwt where transaction_date>='",startDate,"' and transaction_date<='",endDate,"' group by pwt_amt,game_id) pwtTlb where gm.game_id=pwtTlb.game_id order by game_nbr,priceAmt");
	prepare stmt from @pwtQry;
	execute stmt;
	deallocate prepare stmt;
    END      
#
CREATE      PROCEDURE `scratchCollectionAgentWisePerGame`(startDate timestamp,endDate timestamp,gameNo int)
BEGIN
    
         
         
 
    set @repType:=(select value from st_lms_property_master where property_dev_name='SE_SALE_REP_TYPE');
    
    if(@repType='BOOK_WISE') then
        set @saleQry=concat("select organization_id,ifnull(sale,0.0) sale from st_lms_organization_master left outer join 
(select agent_org_id,sum(mrpAmt) mrpAmt,sum(sale) sale from( 
select agent_org_id,sum(mrp_amt) mrpAmt,sum(net_amt) sale from st_se_bo_agent_transaction at inner join st_lms_bo_transaction_master tm on at.transaction_id=tm.transaction_id where  tm.transaction_type='SALE' and  transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and game_id=",gameNo," group by agent_org_id union all select agent_org_id,sum(mrp_amt) mrpAmt,sum(net_amt) sale from st_se_bo_agent_loose_book_transaction right outer join st_lms_organization_master on organization_id=agent_org_id where transaction_id in (select transaction_id from st_lms_bo_transaction_master where transaction_type='LOOSE_SALE' and   transaction_date>='",startDate,"' and transaction_date<='",endDate,"') and game_id=",gameNo," group by agent_org_id) sale group by agent_org_id) sale on organization_id=agent_org_id where organization_type='AGENT'");
        set @cancelQry=concat("select organization_id,ifnull(cancel,0.0) cancel from st_lms_organization_master left outer join (
select agent_org_id,sum(mrpAmtRet) mrpAmtRet,sum(cancel) cancel from
(select agent_org_id,sum(mrp_amt) mrpAmtRet,sum(net_amt) cancel from st_se_bo_agent_transaction at inner join st_lms_bo_transaction_master tm on at.transaction_id=tm.transaction_id where  tm.transaction_type='SALE_RET' and  transaction_date>='",startDate,"' and  transaction_date<='",endDate,"' and game_id=",gameNo,"  group by agent_org_id union all select agent_org_id,sum(mrp_amt) mrpAmtRet,sum(net_amt) cancel from st_se_bo_agent_loose_book_transaction right outer join st_lms_organization_master on organization_id=agent_org_id where transaction_id in (select transaction_id from st_lms_bo_transaction_master where transaction_type='LOOSE_SALE_RET' and   transaction_date>='",startDate,"' and transaction_date<='",endDate,"' ) and game_id=",gameNo," group by agent_org_id) saleRet group by agent_org_id) cancel on organization_id=agent_org_id where organization_type='AGENT'");
    elseif(@repType='TICKET_WISE') then
        set @saleQry=concat("select organization_id,ifnull(netAmt,0.0) sale from st_lms_organization_master left outer join (select current_owner_id,sum(soldTkt*ticket_price) mrpAmt,sum((soldTkt*ticket_price)-(soldTkt*ticket_price*transacrion_sale_comm_rate*0.01)) netAmt from st_se_game_master gm,st_se_game_inv_detail gid,(select game_id,book_nbr,sum(sold_tickets) soldTkt from st_se_game_ticket_inv_history where date>='",startDate,"' and date<='",endDate,"' and current_owner='RETAILER' and game_id=",gameNo," group by book_nbr) TktTlb where gm.game_id=TktTlb.game_id and TktTlb.book_nbr=gid.book_nbr and gid.current_owner='AGENT' group by current_owner_id) saleTlb on organization_id=current_owner_id where organization_type='AGENT'");
        set @cancelQry=concat("select organization_id,0.00 cancel from st_lms_organization_master where organization_type='AGENT'");
    end if;
     set @mainQry=concat("select organization_id,sum(sale) sale,sum(cancel) cancel,sum(pwt) pwt,sum(pwtDir) pwtdir from (select a.organization_id,sale,cancel,pwt,pwtDir from (select sale.organization_id,sale,cancel,pwt from (",@saleQry,") sale inner join (",@cancelQry,") cancel inner join (select organization_id,ifnull(pwt,0.0) pwt from st_lms_organization_master left outer join (select parent_id,ifnull(sum(pwt),0.0) pwt from st_lms_organization_master inner join (select rp.retailer_org_id,sum(pwt_amt+(pwt_amt*agt_claim_comm*0.01)) pwt from st_se_retailer_pwt rp inner join st_lms_retailer_transaction_master tm on rp.transaction_id=tm.transaction_id where transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and transaction_type='PWT' and rp.game_id=",gameNo,"  group by rp.retailer_org_id union all select retailer_org_id,sum(pwt_amt+(pwt_amt*claim_comm*0.01)) pwt from st_se_agent_pwt ap inner join st_lms_agent_transaction_master tm on ap.transaction_id=tm.transaction_id where ap.status!='DONE_UNCLM' and transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and transaction_type='PWT' and ap.game_id=",gameNo,"  group by retailer_org_id) pwt on organization_id=retailer_org_id where organization_type='RETAILER' group by parent_id) pwt on organization_id=pwt.parent_id  where organization_type='AGENT') pwt on sale.organization_id=cancel.organization_id and sale.organization_id=pwt.organization_id and cancel.organization_id=pwt.organization_id) a inner join (select organization_id,ifnull(pwtDir,0.0) pwtDir from st_lms_organization_master left outer join (select agent_org_id,sum(pwtDir) pwtDir from (select agent_org_id,sum(pwt_amt+(pwt_amt*claim_comm*0.01)) pwtDir from st_se_agt_direct_player_pwt where transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and game_id=",gameNo,"  group by agent_org_id union all select agent_org_id,sum(pwt_amt+comm_amt) pwt from st_se_bo_pwt bp inner join st_lms_bo_transaction_master tm on bp.transaction_id=tm.transaction_id where transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and transaction_type='PWT' and bp.game_id=",gameNo,"  group by agent_org_id) pwt group by agent_org_id) pwtDir on organization_id=agent_org_id  where organization_type='AGENT') b on a.organization_id=b.organization_id");
 
    
if(@repType='BOOK_WISE') then
set @mainQry=concat(@mainQry," union all select organization_id,sum(sale_book_net) as sale, sum(ref_net_amt) as cancel,sum(pwt_net_amt),sum(direct_pwt_net_amt)as pwtDir from st_rep_se_agent where game_id=",gameNo," and finaldate>=date('",startDate,"') and finaldate<=date('",endDate,"') group by organization_id ) as main group by main.organization_id ");
else
set @mainQry=concat(@mainQry," union all select organization_id, sum(sale_ticket_net) as sale, 0.0 as cancel, sum(pwt_net_amt) as pwt,sum(direct_pwt_net_amt)as pwtDir from st_rep_se_agent where game_id=",gameNo," and finaldate>=date('",startDate,"') and finaldate<=date('",endDate,"') group by organization_id) as main group by main.organization_id ");
end if ;
  
    PREPARE stmt FROM @mainQry;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;
 
    END      
#
CREATE      PROCEDURE `scratchCollectionDayWisePerGame`(startDate timestamp,endDate timestamp,gameNo int,agtOrgId int,viewBy varchar(20))
BEGIN
	
        
         
	if(viewBy='Agent' and agtOrgId!=0) then
		set @sePwtCol = " pwt_amt+(pwt_amt*claim_comm*0.01) ";
		set @addScratchPwtQry = concat(" and rp.retailer_org_id in (select organization_id from st_lms_organization_master where parent_id=",agtOrgId,")");
		set @addDirPlrQry = concat(" and agent_org_id=",agtOrgId);
		set @addSaleQry=concat("'RETAILER' and gid.current_owner_id in(select organization_id from st_lms_organization_master where parent_id=", agtOrgId ,") group by date");
	else
		set @sePwtCol = "pwt_amt+(pwt_amt*agt_claim_comm*0.01)";
		set @addScratchPwtQry = "";
		set @addDirPlrQry = "";
		set @addSaleQry="'AGENT'  group by date";
	end if;
	
	set @repType:=(select value from st_lms_property_master where property_dev_name='SE_SALE_REP_TYPE');
	
	if(@repType='BOOK_WISE') then
		if(viewBy='Agent' and agtOrgId!=0) then
			set @saleQry=concat("select date(alldate) tx_date,ifnull(sale,0.0) sale from tempdate left outer join (select transaction_date,sum(mrpAmt) mrpAmt,sum(sale) sale from(select  transaction_date,sum(mrp_amt) mrpAmt,sum(net_amt) sale from st_se_agent_retailer_transaction sale,(select transaction_id,date(transaction_date) transaction_date from st_lms_agent_transaction_master where transaction_type='SALE' and transaction_date>='",startDate,"' and transaction_date<='",endDate,"') tranTlb where sale.transaction_id=tranTlb.transaction_id and agent_org_id=",agtOrgId," and game_id=",gameNo," group by transaction_date
union all
select transaction_date,sum(mrp_amt) mrpAmt,sum(net_amt) sale from st_se_agent_ret_loose_book_transaction sale,(select transaction_id,date(transaction_date) transaction_date from st_lms_agent_transaction_master where transaction_type='LOOSE_SALE' and transaction_date>='",startDate,"' and transaction_date<='",endDate,"') tranTlb where sale.transaction_id=tranTlb.transaction_id and agent_org_id=",agtOrgId," and game_id=",gameNo," group by transaction_date) sale group by transaction_date) sale on date(alldate)=transaction_date");
set @cancelQry=concat("select date(alldate) tx_date,ifnull(cancel,0.0) cancel from tempdate left outer join (select transaction_date,sum(mrpAmt) mrpAmt,sum(cancel) cancel from (select transaction_date,sum(mrp_amt) mrpAmt,sum(net_amt) cancel from st_se_agent_retailer_transaction sale,(select transaction_id,date(transaction_date) transaction_date from st_lms_agent_transaction_master where transaction_type='SALE_RET' and transaction_date>='2012-05-22' and transaction_date<='2012-05-23') tranTlb where sale.transaction_id=tranTlb.transaction_id and agent_org_id=2 and game_id=1 group by transaction_date union all select transaction_date,sum(mrp_amt) mrpAmt,sum(net_amt) cancel from st_se_agent_ret_loose_book_transaction sale,(select transaction_id,date(transaction_date) transaction_date from st_lms_agent_transaction_master where transaction_type='LOOSE_SALE_RET' and transaction_date>='",startDate,"' and transaction_date<='",endDate,"') tranTlb where sale.transaction_id=tranTlb.transaction_id and agent_org_id=",agtOrgId," and game_id=",gameNo," group by transaction_date)cancel group by transaction_date) cancel on date(alldate)=transaction_date");
		else
set @saleQry=concat("select date(alldate) tx_date,ifnull(sale,0.0) sale from tempdate left outer join (
select transaction_date,sum(mrpAmt) mrpAmt,sum(sale) sale from(select transaction_date,sum(mrp_amt) mrpAmt,sum(net_amt) sale from st_se_bo_agent_transaction sale,(select transaction_id,date(transaction_date) transaction_date from st_lms_bo_transaction_master where transaction_type='SALE' and transaction_date>='",startDate,"' and transaction_date<='",endDate,"') tranTlb where sale.transaction_id=tranTlb.transaction_id and game_id=",gameNo," group by transaction_date union all select transaction_date,sum(mrp_amt) mrpAmt,sum(net_amt) sale from st_se_bo_agent_loose_book_transaction sale,(select transaction_id,date(transaction_date) transaction_date from st_lms_bo_transaction_master where transaction_type='LOOSE_SALE' and transaction_date>='",startDate,"' and transaction_date<='",endDate,"') tranTlb where sale.transaction_id=tranTlb.transaction_id and game_id=",gameNo," group by transaction_date)sale group by transaction_date) sale on date(alldate)=transaction_date");
set @cancelQry=concat("select date(alldate) tx_date,ifnull(cancel,0.0) cancel from tempdate left outer join (
select transaction_date,sum(mrpAmt) mrpAmt,sum(cancel) cancel from (
select transaction_date,sum(mrp_amt) mrpAmt,sum(net_amt) cancel from st_se_bo_agent_transaction sale,(select transaction_id,date(transaction_date) transaction_date from st_lms_bo_transaction_master where transaction_type='SALE_RET' and transaction_date>='",startDate,"' and transaction_date<='",endDate,"') tranTlb where sale.transaction_id=tranTlb.transaction_id and game_id=",gameNo," group by transaction_date union all select transaction_date,sum(mrp_amt) mrpAmt,sum(net_amt) cancel from st_se_bo_agent_loose_book_transaction sale,(select transaction_id,date(transaction_date) transaction_date from st_lms_bo_transaction_master where transaction_type='LOOSE_SALE_RET' and transaction_date>='",startDate,"' and transaction_date<='",endDate,"') tranTlb where sale.transaction_id=tranTlb.transaction_id and game_id=",gameNo," group by transaction_date) cancle group by transaction_date) cancel on date(alldate)=transaction_date");
		end if;
	elseif(@repType='TICKET_WISE') then
		set @saleQry=concat("select date(alldate) tx_date,ifnull(sale,0.0) sale from tempdate left outer join (select date, sum((soldTkt*ticket_price) - (soldTkt*ticket_price*transacrion_sale_comm_rate*0.01)) sale from st_se_game_master gm inner join st_se_game_inv_detail gid inner join (select date(date) date,game_id,book_nbr,sum(sold_tickets) soldTkt from st_se_game_ticket_inv_history where date>='",startDate,"' and date<='",endDate,"' and current_owner='RETAILER' group by book_nbr,date(date)) TktTlb on gm.game_id=TktTlb.game_id and TktTlb.book_nbr=gid.book_nbr where gm.game_id=",gameNo," and gid.current_owner=",@addSaleQry,") sale on date(alldate)=date");
		set @cancelQry="select date(alldate) tx_date,0.0 cancel from tempdate";
	end if;
	set @pwtQry=concat("select date(alldate) tx_date,ifnull(pwt,0.0) pwt from tempdate left outer join (select date(rtm.transaction_date) tx_date,sum(",@sePwtCol,") pwt from st_se_retailer_pwt rp,st_lms_retailer_transaction_master rtm where rp.transaction_id=rtm.transaction_id and rtm.transaction_date>='",startDate,"' and rtm.transaction_date<='",endDate,"' and rtm.transaction_type='PWT' and rp.game_id=",gameNo," ",@addScratchPwtQry," group by tx_date) pwt on date(alldate)=tx_date");
	set @dirPwtQry=concat("select tx_date,sum(net_amt) pwtDir from (select date(transaction_date) tx_date ,(pwt_amt+(pwt_amt*claim_comm*0.01)) net_amt from st_se_agt_direct_player_pwt where transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and game_id=",gameNo," ",@addDirPlrQry,") aa group by tx_date");
	
	set @mainQry=concat("select main.alldate alldate ,sum(main.sale) sale,sum(main.cancel) cancel,sum(main.pwt) pwt,sum(main.pwtDir) pwtdir from(select a.tx_date as alldate,sale,cancel,pwt,ifnull(pwtDir,0.0) pwtDir from (select sale.tx_date,sale,cancel,pwt from (",@saleQry,") sale inner join (",@cancelQry,") cancel inner join (",@pwtQry,") pwt on sale.tx_date=cancel.tx_date and sale.tx_date=pwt.tx_date and cancel.tx_date=pwt.tx_date) a left outer join (",@dirPwtQry,")pwtDir on a.tx_date=pwtDir.tx_date");
if(@repType='BOOK_WISE') then
		if(viewBy='Agent' and agtOrgId!=0) then
               set @mainQry=concat(@mainQry," union all select finaldate as alldate,sum(sale_book_mrp)as sale,sum(ref_net_amt)as cancel,sum(pwt_net_amt)as pwt,sum(direct_pwt_net_amt)as pwtDir from st_rep_se_agent where game_id=",gameNo," and finaldate>=date('",startDate,"') and finaldate<=date('",endDate,"') group by finaldate)as main group by main.alldate");
               else
               set @mainQry=concat(@mainQry," union all select finaldate as alldate,sum(sale_book_net)as sale,sum(ref_net_amt)as cancel, sum(pwt_net_amt)as pwt ,sum(direct_pwt_net_amt)as pwtDir  from st_rep_se_bo where game_id=",gameNo," and    finaldate>=date('",startDate,"') and finaldate<=date('",endDate,"') group by finaldate)as main group by main.alldate");
               end if;
else
    if(viewBy='Agent' and agtOrgId!=0) then
    set @mainQry=concat(@mainQry," union all select finaldate as alldate,sum(sale_ticket_net) as sale,0.0 as cancel,sum(pwt_net_amt)as pwt,sum(direct_pwt_net_amt)as pwtDir from st_rep_se_agent where  game_id=",gameNo," and finaldate>=date('",startDate,"') and finaldate<=date('",endDate,"') group by finaldate)as main group by main.alldate");
               else
               set @mainQry=concat(@mainQry," union all select finaldate as alldate,sum(sale_ticket_net) as sale,0.0 as cancel,sum(pwt_net_amt)as pwt,sum(direct_pwt_net_amt)as pwtDir from st_rep_se_bo where game_id=",gameNo," and  finaldate>=date('",startDate,"') and finaldate<=date('",endDate,"') group by finaldate )as main group by main.alldate");
               end if;
 end if;
 PREPARE stmt FROM @mainQry;
 EXECUTE stmt;
 DEALLOCATE PREPARE stmt;
 
    END
#

CREATE      PROCEDURE `scratchCollectionRetailerWisePerGame`(startDate TIMESTAMP,endDate TIMESTAMP,gameNo INT,parentOrgId INT)
BEGIN
	
         
         
	SET @repType:=(SELECT VALUE FROM st_lms_property_master WHERE property_dev_name='SE_SALE_REP_TYPE');
	
	IF(@repType='BOOK_WISE') THEN
		SET @saleQry=CONCAT("select organization_id,ifnull(sale,0.0) sale from st_lms_organization_master om left outer join (select retailer_org_id,parent_id,sum(sale) sale from (
select retailer_org_id,parent_id,sum(net_amt) sale from st_se_agent_retailer_transaction at inner join st_lms_agent_transaction_master tm inner join st_lms_organization_master om on at.transaction_id=tm.transaction_id and retailer_org_id=organization_id where  tm.transaction_type='SALE' and transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and game_id=",gameNo," and parent_id=",parentOrgId," group by retailer_org_id union all select retailer_org_id,parent_id,sum(net_amt) sale from st_se_agent_ret_loose_book_transaction at inner join st_lms_agent_transaction_master tm inner join st_lms_organization_master om on at.transaction_id=tm.transaction_id and retailer_org_id=organization_id where   tm.transaction_type='LOOSE_SALE' and transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and game_id=",gameNo," and parent_id=",parentOrgId," group by retailer_org_id)sale group by retailer_org_id ) sale on retailer_org_id=organization_id where organization_type='RETAILER' and om.parent_id=",parentOrgId,"");
		SET @cancelQry=CONCAT("select organization_id,ifnull(cancel,0.0) cancel from st_lms_organization_master left outer join (select retailer_org_id,sum(mrpAmtRet) mrpAmtRet,sum(cancel) cancel from (select retailer_org_id,sum(mrp_amt) mrpAmtRet,sum(net_amt) cancel from st_se_agent_retailer_transaction at inner join st_lms_agent_transaction_master tm on at.transaction_id=tm.transaction_id where  tm.transaction_type='SALE_RET' and transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and game_id=",gameNo," group by retailer_org_id
union all select retailer_org_id,sum(mrp_amt) mrpAmtRet,sum(net_amt) cancel from st_se_agent_ret_loose_book_transaction at inner join st_lms_agent_transaction_master tm on at.transaction_id=tm.transaction_id where  tm.transaction_type='LOOSE_SALE_RET' and transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and game_id=",gameNo," group by retailer_org_id)cancel group by retailer_org_id ) cancel on organization_id=retailer_org_id where organization_type='RETAILER' and parent_id=",parentOrgId,"");
	ELSEIF(@repType='TICKET_WISE') THEN
		SET @saleQry=CONCAT("select organization_id,ifnull(sale,0.0) sale from st_lms_organization_master left outer join (select current_owner_id , sum(soldTkt*ticket_price)  mrpAmt,sum((soldTkt*ticket_price) - (soldTkt*ticket_price*transacrion_sale_comm_rate*0.01)) sale from st_se_game_master gm,st_se_game_inv_detail gid,(select game_id,book_nbr,sum(sold_tickets) soldTkt from st_se_game_ticket_inv_history where date>='",startDate,"' and date<='",endDate,"' and current_owner='RETAILER' and game_id=",gameNo," group by book_nbr) TktTlb where gm.game_id=TktTlb.game_id and TktTlb.book_nbr=gid.book_nbr and gid.current_owner='RETAILER' group by current_owner_id) saleTlb on organization_id=current_owner_id where organization_type='RETAILER' and parent_id=",parentOrgId,"");
		SET @cancelQry=CONCAT("select organization_id,0.00 cancel from st_lms_organization_master where organization_type='RETAILER' and parent_id=",parentOrgId,"");
	END IF;
 	SET @mainQry=CONCAT("select main.organization_id,sum(main.sale) as sale ,sum(main.cancel) as cancel,sum(main.pwt) as pwt from(select sale.organization_id,sale,cancel,pwt from (",@saleQry,") sale inner join (",@cancelQry,") cancel inner join (select organization_id,ifnull(pwt,0.0) pwt from st_lms_organization_master left outer join (select rp.retailer_org_id ,sum(pwt_amt+(pwt_amt*claim_comm*0.01)) pwt from st_se_retailer_pwt rp inner join st_lms_retailer_transaction_master tm on rp.transaction_id=tm.transaction_id where transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and transaction_type='PWT' and rp.game_id=",gameNo," group by rp.retailer_org_id union all select retailer_org_id,sum(pwt_amt+(pwt_amt*claim_comm*0.01)) pwt from st_se_agent_pwt ap inner join st_lms_agent_transaction_master tm on ap.transaction_id=tm.transaction_id where transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and transaction_type='PWT' and ap.game_id=",gameNo," group by retailer_org_id) pwt on organization_id=retailer_org_id where organization_type='RETAILER' and parent_id=",parentOrgId,")  pwt on sale.organization_id=cancel.organization_id and sale.organization_id=pwt.organization_id and cancel.organization_id=pwt.organization_id");
  	
IF(@repType='BOOK_WISE') THEN
SET @mainQry=CONCAT(@mainQry," union all select organization_id,sum(sale_book_net) as sale,sum(ref_net_amt) as cancel,sum(pwt_net_amt)as pwt from st_rep_se_retailer where game_id=",gameNo," and parent_id=",parentOrgId," and finaldate>=date('",startDate,"') and finaldate<=date('",endDate,"') group by organization_id)as main group by main.organization_id ");
ELSE 
SET @mainQry=CONCAT(@mainQry," union all select organization_id,sum(sale_ticket_mrp) as sale,0.0 as cancel,sum(pwt_net_amt)as pwt from st_rep_se_retailer where game_id=",gameNo," and parent_id=",parentOrgId," and finaldate>=date('",startDate,"') and finaldate<=date('",endDate,"') group by organization_id)as main group by main.organization_id");
END IF;
 	PREPARE stmt FROM @mainQry;
	EXECUTE stmt;
	DEALLOCATE PREPARE stmt;
 
    END      
#
CREATE      PROCEDURE `scratchDirPwtPlayerDateWisePerGame`(startDate timestamp,endDate timestamp,gameNo int, agtOrgId int)
BEGIN
set @mainQry=concat("select date(transaction_date) date,agent_org_id,sum(pwt_amt+(pwt_amt*claim_comm*0.01)) pwtDir from st_se_agt_direct_player_pwt where transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and game_id=",gameNo," and agent_org_id=",agtOrgId," group by date(transaction_date)
union all
select finaldate,organization_id, sum(direct_pwt_net_amt)as pwtDir from st_rep_se_agent where organization_id=",agtOrgId," and game_id=",gameNo," and finaldate>=date('",startDate,"') and finaldate<=date('",endDate,"') group by finaldate
");
PREPARE stmt FROM @mainQry;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
    END      
#
CREATE      PROCEDURE `scratchGameAgtReporting`(startDate timestamp,endDate timestamp)
BEGIN
	set @saleQry=concat("insert into st_rep_se_agent(organization_id, parent_id,finaldate,game_id,sale_book_mrp,sale_comm,sale_book_net,sale_vat,sale_taxable) select organization_id,om.parent_id,alldate,om.game_id,ifnull(bookWiseMrpSale,0.00) bookWiseMrpSale,ifnull(retComm,0.00) retComm,ifnull(bookWiseNetSale,0.00) bookWiseNetSale,ifnull(vatAmt,0.00) vatAmt,ifnull(taxableSale,0.00) taxableSale from (select organization_id,parent_id,date(alldate) alldate,game_id from st_lms_organization_master,tempdate,st_se_game_master where organization_type='AGENT') om left outer join (select agent_org_id,parent_id,date(transaction_date) tx_date,game_id,sum(net_amt) bookWiseNetSale,sum(mrp_amt) bookWiseMrpSale,sum(comm_amt) retComm,sum(vat_amt) vatAmt,sum(taxable_sale) taxableSale from st_se_bo_agent_transaction at inner join st_lms_bo_transaction_master tm inner join st_lms_organization_master om on at.transaction_id=tm.transaction_id and agent_org_id=organization_id where  tm.transaction_type='SALE' and transaction_date>='",startDate,"' and transaction_date<='",endDate,"' group by agent_org_id,date(transaction_date),game_id) sale on organization_id=agent_org_id and alldate=tx_date and om.game_id=sale.game_id");
	
	PREPARE stmt FROM @saleQry;
	EXECUTE stmt;
	DEALLOCATE PREPARE stmt;
	
	set @cancelQry=concat("update(select agent_org_id,date(transaction_date) tx_date,game_id,sum(mrp_amt) refMrp,sum(net_amt) refNet,sum(comm_amt) retComm,sum(vat_amt) vatAmt,sum(taxable_sale) taxableSale from st_se_bo_agent_transaction at inner join st_lms_bo_transaction_master tm on at.transaction_id=tm.transaction_id where  tm.transaction_type='SALE_RET' and transaction_date>='",startDate,"' and transaction_date<='",endDate,"' group by agent_org_id,date(transaction_date),game_id) cancel,st_rep_se_agent se set ref_sale_mrp=refMrp,ref_comm=retComm,ref_net_amt=refNet,ref_vat=vatAmt,ref_taxable=taxableSale where cancel.agent_org_id=se.organization_id and se.finaldate=cancel.tx_date and cancel.game_id=se.game_id");
	
	PREPARE stmt FROM @cancelQry;
	EXECUTE stmt;
	DEALLOCATE PREPARE stmt;
	set @pwtQry=concat("update (select agent_org_id,tx_date,sum(retCommPwt) retCommPwt,sum(pwtNet) pwtNet,game_id,sum(pwtMrp) pwtMrp from (select drs.agent_org_id,date(transaction_date) tx_date,sum(comm_amt) retCommPwt ,(sum(pwt_amt+comm_amt)) pwtNet,drs.game_id,sum(pwt_amt) pwtMrp from st_se_bo_pwt drs,st_lms_bo_transaction_master rtm where drs.transaction_id=rtm.transaction_id and transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and transaction_type in('PWT') group by agent_org_id,tx_date,drs.game_id
union all select drs.agent_org_id,date(transaction_date) tx_date,sum(comm_amt) retCommPwt ,sum(pwt_amt+comm_amt) pwtNet,drs.game_id,sum(pwt_amt) pwtMrp from st_se_agent_pwt drs,st_lms_agent_transaction_master rtm where drs.transaction_id=rtm.transaction_id and drs.status!='DONE_UNCLM' and transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and transaction_type in('PWT') group by agent_org_id,tx_date,drs.game_id union all select bb.parent_id agent_org_id,date(transaction_date) tx_date,sum(agt_claim_comm) retCommPwt ,sum(pwt_amt+(pwt_amt*agt_claim_comm*0.01)) pwtNet,drs.game_id,sum(pwt_amt) pwtMrp from st_se_retailer_pwt drs,st_lms_retailer_transaction_master rtm,st_lms_organization_master bb  where drs.transaction_id=rtm.transaction_id and transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and transaction_type in('PWT') and rtm.retailer_org_id=bb.organization_id and bb.organization_type='RETAILER' group by bb.parent_id,tx_date,drs.game_id) c group by c.tx_date,c.game_id,c.agent_org_id) pwt,st_rep_se_agent se set pwt_mrp=pwtMrp,pwt_comm=retCommPwt,pwt_net_amt=pwtNet  where pwt.agent_org_id=se.organization_id and se.finaldate=pwt.tx_date and pwt.game_id=se.game_id");
	
	PREPARE stmt FROM @pwtQry;
	EXECUTE stmt;
	DEALLOCATE PREPARE stmt;
	set @pwtDirQry=concat("update (select agent_org_id,date(transaction_date) tx_date,sum(pwt_amt) pwtMrp,game_id,sum(pwt_amt+(pwt_amt*claim_comm*0.01)) pwtNet,sum((pwt_amt*claim_comm*0.01))retCommPwt,sum(tax_amt) taxAmt from st_se_agt_direct_player_pwt where transaction_date>='",startDate,"' and transaction_date<='",endDate,"' group by agent_org_id,tx_date,game_id) pwt,st_rep_se_agent se set direct_pwt_amt=pwtMrp,direct_pwt_comm=retCommPwt,direct_pwt_net_amt=pwtNet,direct_pwt_tax=taxAmt  where pwt.agent_org_id=se.organization_id and se.finaldate=pwt.tx_date and pwt.game_id=se.game_id");
	
	PREPARE stmt FROM @pwtDirQry;
	EXECUTE stmt;
	DEALLOCATE PREPARE stmt;
	
	select 'SE Agt Rep Done';
set @insStatus=concat("insert into archiving_status(job_name, status)values('SE_AGENT_REPORTING', 'DONE')");
prepare stmt from @insStatus;
execute stmt;
deallocate prepare stmt;
    END      
#
