--liquibase formatted sql

--changeset BaseSPRMS:5 endDelimiter:#

#
CREATE      PROCEDURE `scratchGameBOReporting`(startDate timestamp,endDate timestamp)
BEGIN
	set @saleQry=concat("insert into st_rep_se_bo(organization_id, parent_id,finaldate,game_id,sale_book_mrp,sale_comm,sale_book_net,sale_vat,sale_taxable) select organization_id, om.parent_id, alldate, om.game_id, ifnull(bookWiseMrpSale,0.00) bookWiseMrpSale, ifnull(retComm,0.00) retComm,ifnull(bookWiseNetSale,0.00) bookWiseNetSale,ifnull(vatAmt,0.00) vatAmt,ifnull(taxableSale,0.00) taxableSale from (select organization_id,parent_id,date(alldate) alldate,game_id from st_lms_organization_master,tempdate,st_se_game_master where organization_type='BO') om left outer join (select date(transaction_date) tx_date,game_id,sum(net_amt) bookWiseNetSale,sum(mrp_amt) bookWiseMrpSale,sum(comm_amt) retComm,sum(vat_amt) vatAmt,sum(taxable_sale) taxableSale from st_se_bo_agent_transaction at inner join st_lms_bo_transaction_master tm inner join st_lms_organization_master om on at.transaction_id=tm.transaction_id and agent_org_id=organization_id where  tm.transaction_type='SALE' and transaction_date>='",startDate,"' and transaction_date<='",endDate,"' group by date(transaction_date),game_id) sale on alldate=tx_date and om.game_id=sale.game_id");
	
	PREPARE stmt FROM @saleQry;
	EXECUTE stmt;
	DEALLOCATE PREPARE stmt;
	
	set @cancelQry=concat("update(select date(transaction_date) tx_date,game_id,sum(mrp_amt) refMrp,sum(net_amt) refNet,sum(comm_amt) retComm,sum(vat_amt) vatAmt,sum(taxable_sale) taxableSale from st_se_bo_agent_transaction at inner join st_lms_bo_transaction_master tm on at.transaction_id=tm.transaction_id where  tm.transaction_type='SALE_RET' and transaction_date>='",startDate,"' and transaction_date<='",endDate,"' group by date(transaction_date),game_id) cancel,st_rep_se_bo se set ref_sale_mrp=refMrp,ref_comm=retComm,ref_net_amt=refNet,ref_vat=vatAmt,ref_taxable=taxableSale where se.finaldate=cancel.tx_date and cancel.game_id=se.game_id");
	
	PREPARE stmt FROM @cancelQry;
	EXECUTE stmt;
	DEALLOCATE PREPARE stmt;
	set @pwtQry=concat("update (select date(transaction_date) tx_date,drs.game_id,sum(pwt_amt) pwtMrp,sum(comm_amt) retCommPwt ,sum(pwt_amt+comm_amt) pwtNet from st_se_bo_pwt drs,st_lms_bo_transaction_master rtm where drs.transaction_id=rtm.transaction_id and transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and transaction_type in('PWT_AUTO','PWT_PLR','PWT')  group by tx_date,drs.game_id) pwt,st_rep_se_bo se set pwt_mrp=pwtMrp,pwt_comm=retCommPwt,pwt_net_amt=pwtNet  where se.finaldate=pwt.tx_date and pwt.game_id=se.game_id");
	
	PREPARE stmt FROM @pwtQry;
	EXECUTE stmt;
	DEALLOCATE PREPARE stmt;
	set @pwtDirQry=concat("update (select date(transaction_date) tx_date,sum(pwt_amt) pwtMrp,game_id,sum(net_amt) pwtNet,sum(0.00) retCommPwt, sum(tax_amt) taxAmt from st_se_direct_player_pwt where transaction_date>='",startDate,"' and transaction_date<='",endDate,"' group by tx_date,game_id) pwt,st_rep_se_bo se set direct_pwt_amt = pwtMrp , direct_pwt_comm = retCommPwt , direct_pwt_net_amt=pwtNet, direct_pwt_tax=taxAmt  where  se.finaldate=pwt.tx_date and pwt.game_id=se.game_id");
 
	PREPARE stmt FROM @pwtDirQry;
	EXECUTE stmt;
	DEALLOCATE PREPARE stmt;
	select 'SE BO Rep Done';
set @insStatus=concat("insert into archiving_status(job_name, status)values('SE_BO_REPORTING', 'DONE')");
prepare stmt from @insStatus;
execute stmt;
deallocate prepare stmt;
    END      
#
CREATE      PROCEDURE `scratchGameRetReporting`(startDate timestamp,endDate timestamp)
BEGIN
	set @saleQry=concat("insert into st_rep_se_retailer(organization_id, parent_id,finaldate,game_id,sale_book_mrp,sale_comm,sale_book_net,sale_vat,sale_taxable) select organization_id,om.parent_id,alldate,om.game_id,ifnull(bookWiseMrpSale,0.00) bookWiseMrpSale,ifnull(retComm,0.00) retComm,ifnull(bookWiseNetSale,0.00) bookWiseNetSale,ifnull(vatAmt,0.00) vatAmt,ifnull(taxableSale,0.00) taxableSale from (select organization_id,parent_id,date(alldate) alldate,game_id from st_lms_organization_master,tempdate,st_se_game_master where organization_type='RETAILER') om left outer join (select retailer_org_id,parent_id,date(transaction_date) tx_date,game_id,sum(net_amt) bookWiseNetSale,sum(mrp_amt) bookWiseMrpSale,sum(comm_amt) retComm,sum(vat_amt) vatAmt,sum(taxable_sale) taxableSale from st_se_agent_retailer_transaction at inner join st_lms_agent_transaction_master tm inner join st_lms_organization_master om on at.transaction_id=tm.transaction_id and retailer_org_id=organization_id where  tm.transaction_type='SALE' and transaction_date>='",startDate,"' and transaction_date<='",endDate,"' group by retailer_org_id,date(transaction_date),game_id) sale on organization_id=retailer_org_id and alldate=tx_date and om.game_id=sale.game_id");
	PREPARE stmt FROM @saleQry;
	EXECUTE stmt;
	DEALLOCATE PREPARE stmt;
	
	set @cancelQry=concat("update(select retailer_org_id,date(transaction_date) tx_date,game_id,sum(mrp_amt) refMrp,sum(net_amt) refNet,sum(comm_amt) retComm,sum(vat_amt) vatAmt,sum(taxable_sale) taxableSale from st_se_agent_retailer_transaction at inner join st_lms_agent_transaction_master tm on at.transaction_id=tm.transaction_id where  tm.transaction_type='SALE_RET' and transaction_date>='",startDate,"' and transaction_date<='",endDate,"' group by retailer_org_id,date(transaction_date),game_id) cancel,st_rep_se_retailer se set ref_sale_mrp=refMrp,ref_comm=retComm,ref_net_amt=refNet,ref_vat=vatAmt,ref_taxable=taxableSale where cancel.retailer_org_id=se.organization_id and se.finaldate=cancel.tx_date and cancel.game_id=se.game_id");
	PREPARE stmt FROM @cancelQry;
	EXECUTE stmt;
	DEALLOCATE PREPARE stmt;
	set @pwtQry=concat("update (select retailer_org_id,tx_date,sum(retCommPwt) retCommPwt,sum(pwtNet) pwtNet,game_id,sum(pwtMrp) pwtMrp from (select drs.retailer_org_id,date(transaction_date) tx_date,sum(comm_amt) retCommPwt ,sum(pwt_amt+comm_amt) pwtNet,drs.game_id,sum(pwt_amt) pwtMrp from st_se_agent_pwt drs inner join st_lms_agent_transaction_master rtm on drs.transaction_id=rtm.transaction_id and drs.status!='DONE_UNCLM' and transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and transaction_type in('PWT') group by retailer_org_id,tx_date,drs.game_id union all select drs.retailer_org_id,date(transaction_date) tx_date,sum(claim_comm) retCommPwt ,sum(pwt_amt+claim_comm) pwtNet,drs.game_id,sum(pwt_amt) pwtMrp from st_se_retailer_pwt drs inner join st_lms_retailer_transaction_master rtm on drs.transaction_id=rtm.transaction_id and transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and transaction_type in('PWT_AUTO','PWT_PLR','PWT')  group by retailer_org_id,tx_date,drs.game_id)c group by c.tx_date,c.game_id,c.retailer_org_id) pwt,st_rep_se_retailer se set pwt_mrp=pwtMrp,ret_pwt_comm=retCommPwt,pwt_net_amt=pwtNet  where pwt.retailer_org_id=se.organization_id and se.finaldate=pwt.tx_date and pwt.game_id=se.game_id");
	PREPARE stmt FROM @pwtQry;
	EXECUTE stmt;
	DEALLOCATE PREPARE stmt;
	select 'SE Ret Rep Done';
set @insStatus=concat("insert into archiving_status(job_name, status)values('SE_RET_REPORTING', 'DONE')");
prepare stmt from @insStatus;
execute stmt;
deallocate prepare stmt;
    END      
#
CREATE      PROCEDURE `scratchLiveGameReportRetailerWise`(parentOrgId int,startDate timestamp,endDate timestamp)
BEGIN
IF(parentOrgId=1) then
set @startQuery=concat(" ");
set @pwtQuery=concat(" ");
set @unionQuery=concat(" ");
else
set @startQuery=concat(" and gmti.current_owner_id in(select organization_id from st_lms_organization_master where parent_id=",parentOrgId,")");
set @pwtQuery=concat("and retailer_org_id in (select organization_id from st_lms_organization_master where parent_id=",parentOrgId,")");
set @unionQuery=concat(" parent_id=",parentOrgId," and ");
end if;
set @mainQry=concat("select main.organization_id,sum(main.sale)as sale,sum(main.pwt)as pwt from(select sale.organization_id,sale,pwt from(select organization_id,ifnull( sale,0.0) sale from st_lms_organization_master om left outer join (select current_owner_id,sum(sale) sale from (select gmti.game_id,gmti.current_owner_id,gmti.date, (sum(gmti.sold_tickets)*ticket_price) sale from st_se_game_ticket_inv_history gmti ,st_se_game_master gm where gmti.current_owner='RETAILER' ",@startQuery," and gmti.date>='",startDate,"' and gmti.date<='",endDate,"' and gmti.game_id=gm.game_id group by gmti.current_owner_id,gmti.game_id) aa group by current_owner_id)sale  on om.organization_id=sale.current_owner_id where om.organization_type='RETAILER')sale inner join (select organization_id,ifnull( pwt,0.0)  pwt from st_lms_organization_master om left outer join (select retailer_org_id,sum(pwt_amt) pwt from st_se_retailer_pwt where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and transaction_type='PWT' ",@pwtQuery,") group by retailer_org_id)pwt on om.organization_id=pwt.retailer_org_id where om.organization_type='RETAILER')pwt on sale.organization_id=pwt.organization_id union all select organization_id,sum(sale_ticket_mrp) sale,sum(pwt_net_amt) pwt  from  st_rep_se_retailer  where ",@unionQuery," finaldate>=date('",startDate,"') and finaldate<=date('",endDate,"') group by organization_id)as main group by main.organization_id");

            
PREPARE stmt FROM @mainQry;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
    END      
#
CREATE      PROCEDURE `scratchPaymentAgentWisePerGame`(startDate timestamp,endDate timestamp,gameNo int,agtOrgId int)
BEGIN
set @repType:=(select value from st_lms_property_master where property_dev_name='SE_SALE_REP_TYPE');
if(@repType='BOOK_WISE') then
set @saleQry=concat("select organization_id,ifnull(sale,0.0) sale from st_lms_organization_master left outer join (select agent_org_id,sum(mrpAmt),sum(sale) sale from (select agent_org_id,sum(mrp_amt) mrpAmt,sum(net_amt) sale from st_se_bo_agent_transaction at inner join st_lms_bo_transaction_master tm on at.transaction_id=tm.transaction_id where  tm.transaction_type='SALE' and transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and agent_org_id=",agtOrgId," and game_id=",gameNo," group by agent_org_id
union all select agent_org_id,sum(mrp_amt) mrpAmt,sum(net_amt) sale from st_se_bo_agent_loose_book_transaction at inner join st_lms_bo_transaction_master tm on at.transaction_id=tm.transaction_id where  tm.transaction_type='LOOSE_SALE' and transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and agent_org_id=",agtOrgId," and game_id=",gameNo," group by agent_org_id)sale1 group by agent_org_id
) sale on organization_id=agent_org_id where organization_type= 'AGENT' and organization_id = ",agtOrgId," ");
set @cancelQry=concat("select organization_id,ifnull(cancel,0.0) cancel from st_lms_organization_master left outer join (select agent_org_id,sum(mrpAmtRet),sum(cancel) cancel from (select agent_org_id,sum(mrp_amt) mrpAmtRet,sum(net_amt) cancel from st_se_bo_agent_transaction at inner join st_lms_bo_transaction_master tm on at.transaction_id=tm.transaction_id where  tm.transaction_type='SALE_RET' and transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and agent_org_id=",agtOrgId," and game_id=",gameNo," group by agent_org_id
union all select agent_org_id,sum(mrp_amt) mrpAmtRet,sum(net_amt) cancel from st_se_bo_agent_loose_book_transaction at inner join st_lms_bo_transaction_master tm on at.transaction_id=tm.transaction_id where  tm.transaction_type='LOOSE_SALE_RET' and transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and agent_org_id=",agtOrgId," and game_id=",gameNo," group by agent_org_id)cancel1 group by agent_org_id
) cancel on organization_id=agent_org_id where organization_type= 'AGENT' and organization_id = ",agtOrgId," ");
elseif(@repType='TICKET_WISE') then
set @saleQry=concat("select organization_id,ifnull(netAmt,0.0) sale from st_lms_organization_master inner join (select current_owner_id,sum(soldTkt*ticket_price) mrpAmt,sum((soldTkt*ticket_price)-(soldTkt*ticket_price*transacrion_sale_comm_rate*0.01)) netAmt from st_se_game_master gm,st_se_game_inv_detail gid,(select game_id,book_nbr,sum(sold_tickets) soldTkt from st_se_game_ticket_inv_history where date>='",startDate,"' and date<='",endDate,"' and current_owner='RETAILER' and game_id=",gameNo," group by book_nbr) TktTlb where gm.game_id=TktTlb.game_id and TktTlb.book_nbr=gid.book_nbr and gid.current_owner= 'AGENT' and organization_id = ",agtOrgId,"  and current_owner_id=",agtOrgId," group by current_owner_id) saleTlb on organization_id=current_owner_id where organization_type= 'AGENT' and organization_id = ",agtOrgId," ");
set @cancelQry=concat("select organization_id,0.00 cancel from st_lms_organization_master where organization_type= 'AGENT' and organization_id = ",agtOrgId," ");
end if;
  set @mainQry=concat("select organization_id,sum(sale) sale,sum(cancel) cancel,sum(pwt) pwt,sum(pwtDir) pwtdir from (select a.organization_id,sale,cancel,pwt,pwtDir from (select sale.organization_id,sale,cancel,pwt from (",@saleQry,") sale inner join (",@cancelQry,") cancel inner join (select organization_id,ifnull(pwt,0.0) pwt from st_lms_organization_master left outer  join (select parent_id,ifnull(sum(pwt),0.0) pwt from st_lms_organization_master left outer join (select rp.retailer_org_id,sum(pwt_amt+(pwt_amt*agt_claim_comm*0.01)) pwt from st_se_retailer_pwt rp inner join st_lms_retailer_transaction_master tm on rp.transaction_id=tm.transaction_id where rp.retailer_org_id in (select organization_id from st_lms_organization_master where parent_id=",agtOrgId,") and transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and transaction_type='PWT' and rp.game_id=",gameNo,"  group by rp.retailer_org_id union all select retailer_org_id,sum(pwt_amt+(pwt_amt*claim_comm*0.01)) pwt from st_se_agent_pwt ap inner join st_lms_agent_transaction_master tm on ap.transaction_id=tm.transaction_id where ap.status!='DONE_UNCLM' and agent_org_id=",agtOrgId," and transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and transaction_type='PWT' and ap.game_id=",gameNo,"  group by retailer_org_id) pwt on organization_id=retailer_org_id where organization_type='RETAILER' group by parent_id) pwt on organization_id=pwt.parent_id  where organization_type= 'AGENT' and organization_id = ",agtOrgId," ) pwt on sale.organization_id=cancel.organization_id and sale.organization_id=pwt.organization_id and cancel.organization_id=pwt.organization_id) a inner join (select organization_id,ifnull(pwtDir,0.0) pwtDir from st_lms_organization_master left outer join (select agent_org_id,sum(pwtDir) pwtDir from (select agent_org_id,sum(pwt_amt+(pwt_amt*claim_comm*0.01)) pwtDir from st_se_agt_direct_player_pwt where agent_org_id=",agtOrgId," and transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and game_id=",gameNo,"  group by agent_org_id union all select agent_org_id,sum(pwt_amt+comm_amt) pwt from st_se_bo_pwt bp inner join st_lms_bo_transaction_master tm on bp.transaction_id=tm.transaction_id where transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and transaction_type='PWT' and bp.game_id=",gameNo," and bp.agent_org_id=",agtOrgId," group by agent_org_id) pwt group by agent_org_id) pwtDir on organization_id=agent_org_id  where organization_type= 'AGENT' and organization_id = ",agtOrgId," ) b on a.organization_id=b.organization_id");
 
if(@repType='BOOK_WISE') then
set @mainQry=concat(@mainQry," union all select organization_id,sum(sale_book_net) as sale, sum(ref_net_amt) as cancel,sum(pwt_net_amt),sum(direct_pwt_net_amt)as pwtDir from st_rep_se_agent where organization_id=",agtOrgId," and game_id=",gameNo," and finaldate>=date('",startDate,"') and finaldate<=date('",endDate,"') group by organization_id ) as main group by main.organization_id ");
else
set @mainQry=concat(@mainQry," union all select organization_id, sum(sale_ticket_net) as sale, 0.0 as cancel, sum(pwt_net_amt) as pwt,sum(direct_pwt_net_amt)as pwtDir from st_rep_se_agent where organization_id=",agtOrgId," and game_id=",gameNo," and finaldate>=date('",startDate,"') and finaldate<=date('",endDate,"') group by organization_id) as main group by main.organization_id ");
end if ;
   
PREPARE stmt FROM @mainQry;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
 
    END      
#
CREATE      PROCEDURE `scratchPwtAgentWise`(startDate timestamp,endDate timestamp)
BEGIN

set @subqry :=(select value  from st_lms_property_master  where property_code='ORG_LIST_TYPE');
		set @selCol = 'name orgCode';
		if(@subqry='CODE') then 
			set @selCol='org_code orgCode' ;
		end if;
		if (@subqry='CODE_NAME') then 
			set @selCol='concat(org_code,"_",name)orgCode';
		end if;
		if (@subqry='NAME_CODE') then 
		  	set @selCol='concat(name,"_",org_code)orgCode';
		end if; 
		set @subqry1 :=(select value  from st_lms_property_master  where property_code='ORG_LIST_ORDER');
		set @selColOrder = 'orgCode ASC';
		if(@subqry1='ORG_ID') then 
			set @selColOrder='pq.organization_id' ;
		end if;
		if (@subqry1='DESC') then 
			set @selColOrder='orgCode DESC';
		end if;


	set @pwtQry=concat("select pq.organization_id,",@selCol,",mrpAmt,netAmt from 
(select organization_id,sum(mrpAmt) mrpAmt,sum(netAmt) netAmt from (
select agent_org_id organization_id,ifnull(sum(pwt_amt),0) mrpAmt,ifnull(sum(net_amt),0) netAmt from st_se_bo_pwt bpwt, st_lms_organization_master  where bpwt.transaction_id in (select btm.transaction_id from st_lms_bo_transaction_master btm where ( transaction_type='PWT' or transaction_type='PWT_AUTO') and  ( btm.transaction_date>='",startDate,"' and btm.transaction_date<'",endDate,"')) and organization_id = agent_org_id group by agent_org_id union all select organization_id,sum(pwt_mrp) mrpAmt,sum(pwt_net_amt) netAmt from st_rep_se_agent where finaldate>='",startDate,"' and finaldate<'",endDate,"' group by organization_id) pwtTlb group by organization_id)pq left outer join (select organization_id,org_code,name from st_lms_organization_master where organization_type='AGENT') om on pq.organization_id=om.organization_id  order by ",@selColOrder,"");
	prepare stmt from @pwtQry;
	execute stmt;
	deallocate prepare stmt;
    END
#
CREATE      PROCEDURE `scratchPwtAgentWiseExpand`(startDate timestamp,endDate timestamp,agentOrgId int)
BEGIN
set @pwtQry=concat("select game_id gameNo,agent_org_id,(select game_name from st_se_game_master where game_id=aa.game_id ) 'gameName', pwt_amt priceAmt, no_of_tkt noOfTkt, (pwt_amt*no_of_tkt) 'netAmt' from (select pwt_amt , count(*) 'no_of_tkt', game_id, agent_org_id from st_se_bo_pwt bpwt, st_lms_bo_transaction_master btm  where btm.transaction_id=bpwt.transaction_id and agent_org_id = ",agentOrgId," and ( btm.transaction_date>='",startDate,"' and btm.transaction_date<'",endDate,"') group by pwt_amt, game_id )aa order by gameName asc, pwt_amt asc ");
	prepare stmt from @pwtQry;
	execute stmt;
	deallocate prepare stmt;
    END      
#
CREATE      PROCEDURE `scratchPwtDateWisePerGame`(startDate timestamp,endDate timestamp,gameNo int, agtOrgId int)
BEGIN
set @mainQry=concat("select date(transaction_date) date,parent_id,sum(pwt) as pwt from (select bb.parent_id,sum(pwt) as pwt,transaction_date from(select srp.retailer_org_id,sum(pwt_amt+(pwt_amt*agt_claim_comm*0.01)) pwt,transaction_date from  st_se_retailer_pwt srp inner join st_lms_retailer_transaction_master rtm on srp.transaction_id=rtm.transaction_id and srp.game_id=",gameNo," and transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and transaction_type='PWT' and srp.retailer_org_id in (select organization_id from st_lms_organization_master where organization_type='RETAILER')group by date(transaction_date),retailer_org_id)aa,st_lms_organization_master bb where retailer_org_id=organization_id and parent_id=",agtOrgId," group by date(transaction_date) 
union all 
select bb.parent_id,sum(pwt) as pwt,transaction_date from(select srp.retailer_org_id,sum(pwt_amt+(pwt_amt*claim_comm*0.01)) pwt,transaction_date from st_se_agent_pwt srp  inner join st_lms_agent_transaction_master rtm on srp.transaction_id=rtm.transaction_id and srp.game_id=",gameNo," and transaction_date>='",startDate,"' and transaction_date<= '",endDate,"'  and transaction_type='PWT' and srp.retailer_org_id in (select organization_id from st_lms_organization_master where organization_type='RETAILER')group by date(transaction_date),retailer_org_id)aa,st_lms_organization_master bb where retailer_org_id=organization_id and parent_id=",agtOrgId," group by date(transaction_date)
union all
select agent_org_id,sum(pwt_amt+comm_amt) Pwt,transaction_date  from st_se_bo_pwt sbp inner join st_lms_bo_transaction_master btm on sbp.transaction_id=btm.transaction_id and sbp.game_id=",gameNo," and transaction_date>='",startDate,"' and transaction_date<= '",endDate,"' and transaction_type='PWT' and agent_org_id in (select organization_id from st_lms_organization_master where organization_type='AGENT' and organization_id=",agtOrgId,") group by date(transaction_date))pwtTlb group by date(transaction_date)
union all
select finaldate,organization_id, sum(pwt_net_amt) pwt from st_rep_se_agent where organization_id=",agtOrgId," and game_id=",gameNo," and finaldate>=date('",startDate,"') and finaldate<=date('",endDate,"') group by finaldate
");
PREPARE stmt FROM @mainQry;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
    END
#


