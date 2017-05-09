--liquibase formatted sql

--changeset BaseSPRMS:7 endDelimiter:#


#
 CREATE      PROCEDURE `scratchSaleAgentWise`(startDate timestamp,endDate timestamp)
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
			set @selColOrder='organization_id' ;
		end if;
		if (@subqry1='DESC') then 
			set @selColOrder='orgCode DESC';
		end if;


   set @repType:=(select value from st_lms_property_master where property_dev_name='SE_SALE_REP_TYPE');
	if(@repType='BOOK_WISE') then
		set @saleQry=concat("select organization_id,",@selCol,",ifnull(mrpAmt,0.0) mrpAmt,ifnull(netAmt,0.0) netAmt from st_lms_organization_master left outer join (select sale.agent_org_id,sum(ifnull(mrpAmt,0.0)-ifnull(mrpAmtRet,0.0)) mrpAmt,sum(ifnull(netAmt,0.0)-ifnull(netAmtRet,0.0)) netAmt from (select agent_org_id,sum(mrp_amt) mrpAmt,sum(net_amt) netAmt,0.0 mrpAmtRet,0.0 netAmtRet from st_se_bo_agent_transaction where transaction_id in (select transaction_id from st_lms_bo_transaction_master where transaction_type='SALE' and transaction_date>='",startDate,"' and transaction_date<='",endDate, "') group by agent_org_id union all select agent_org_id,0.0,0.0,sum(mrp_amt) mrpAmtRet,sum(net_amt) netAmtRet from st_se_bo_agent_transaction where transaction_id in (select transaction_id from st_lms_bo_transaction_master where transaction_type='SALE_RET' and transaction_date>='",startDate,"' and transaction_date<='",endDate,"') group by agent_org_id union all select organization_id, sum(sale_book_mrp), sum(sale_book_net),sum(ref_sale_mrp) ,sum(ref_net_amt) from st_rep_se_agent where finaldate>='",startDate,"' and finaldate<='",endDate,"' group by organization_id) sale group by agent_org_id 
) saleTlb on organization_id=agent_org_id where organization_type='AGENT' order by ",@selColOrder,"");
	elseif(@repType='TICKET_WISE') then
		set @saleQry=concat("select organization_id,",@selCol,",ifnull(mrpAmt,0.0) mrpAmt,ifnull(netAmt,0.0) netAmt from st_lms_organization_master left outer join (select current_owner_id,sum(soldTkt*ticket_price) mrpAmt,sum((soldTkt*ticket_price)-(soldTkt*ticket_price*transacrion_sale_comm_rate*0.01)) netAmt from st_se_game_master gm,st_se_game_inv_detail gid,(select game_id,book_nbr,sum(sold_tickets) soldTkt from st_se_game_ticket_inv_history where date>='", startDate,"' and date<='",endDate,"' and current_owner='RETAILER' group by book_nbr) TktTlb where gm.game_id=TktTlb.game_id and TktTlb.book_nbr=gid.book_nbr and gid.current_owner='AGENT' group by current_owner_id) saleTlb on organization_id=current_owner_id where organization_type='AGENT' order by ",@selColOrder,"");
	end if;
	 
 	PREPARE stmt FROM @saleQry;
	EXECUTE stmt;
	DEALLOCATE PREPARE stmt;
    END    


#



