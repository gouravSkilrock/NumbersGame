--liquibase formatted sql

--changeset mukeshsharma-5565:1 endDelimiter:#

#  
  DROP PROCEDURE IF EXISTS `drawPwtGameWise`;
#

CREATE PROCEDURE `drawPwtGameWise`(startDate TIMESTAMP, endDate TIMESTAMP,  IN stateCode VARCHAR(10), IN cityCode VARCHAR(10))
BEGIN
DECLARE done INT DEFAULT 0;
DECLARE gameId INT;
DECLARE gameCur CURSOR FOR SELECT game_id FROM st_dg_game_master;
DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;
 SET @mainQry="select gm.game_id gameId, game_name gameName,mrpAmt,netAmt from st_dg_game_master gm,(select game_id,sum(mrpAmt) mrpAmt,sum(netAmt) netAmt from (";
	OPEN gameCur;
	  read_loop: LOOP
	    FETCH gameCur INTO gameId;
	    IF done THEN
	      LEAVE read_loop;
	    END IF;
SET @mainQry=CONCAT(@mainQry,"SELECT retPwt.game_id,SUM(retPwt.pwt_amt) mrpAmt,SUM(retPwt.pwt_amt + retPwt.agt_claim_comm) netAmt FROM st_dg_ret_pwt_",gameId,"  retPwt INNER JOIN st_lms_retailer_transaction_master retTxn ON retPwt.transaction_id = retTxn.transaction_id INNER JOIN st_lms_organization_master om ON om.organization_id = retPwt .retailer_org_id INNER JOIN st_lms_city_master city ON om.city = city.city_name WHERE retTxn.transaction_type IN('DG_PWT_AUTO','DG_PWT_PLR','DG_PWT') AND retTxn.transaction_date>='",startDate,"'  AND retTxn.transaction_date<='",endDate,"'");
IF(stateCode != 'ALL') THEN 
	SET @mainQry = CONCAT(@mainQry, " AND om.state_code = '",stateCode,"'");
END IF;
IF(cityCode != 'ALL') THEN
	SET @mainQry = CONCAT(@mainQry, " AND city.city_name = '",cityCode,"'");
END IF;
SET @mainQry = CONCAT(@mainQry, "  group by game_id union all "); 
	  END LOOP;
	CLOSE gameCur;
 SET @mainQry=CONCAT(@mainQry,"SELECT plrPwt.game_id, SUM(plrPwt.pwt_amt) mrpAmt,SUM(plrPwt.pwt_amt+plrPwt.agt_claim_comm) netAmt FROM st_dg_agt_direct_plr_pwt plrPwt INNER JOIN st_lms_organization_master om ON om.organization_id = plrPwt.agent_org_id INNER JOIN st_lms_city_master city ON om.city = city.city_name WHERE transaction_date>='",startDate,"' and transaction_date<='",endDate,"'");
IF(stateCode != 'ALL') THEN 
	SET @mainQry = CONCAT(@mainQry, " AND om.state_code = '",stateCode,"'");
END IF;
IF(cityCode != 'ALL') THEN
	SET @mainQry = CONCAT(@mainQry, " AND city.city_name = '",cityCode,"'");
END IF;
SET @mainQry = CONCAT(@mainQry, "  group by game_id union all ");
	
SET @mainQry=CONCAT(@mainQry,"SELECT plrPwt.game_id,SUM(plrPwt.pwt_amt) mrpAmt,SUM(plrPwt.pwt_amt) netAmt FROM st_dg_bo_direct_plr_pwt plrPwt INNER JOIN st_lms_organization_master om ON om.organization_id = plrPwt.bo_org_id INNER JOIN st_lms_city_master city ON om.city = city.city_name WHERE plrPwt.transaction_date>='",startDate,"' and transaction_date<='",endDate,"'");
IF(stateCode != 'ALL') THEN 
	SET @mainQry = CONCAT(@mainQry, " AND om.state_code = '",stateCode,"'");
END IF;
IF(cityCode != 'ALL') THEN
	SET @mainQry = CONCAT(@mainQry, " AND city.city_name = '",cityCode,"'");
END IF;
SET @mainQry = CONCAT(@mainQry, "  group by game_id union all ");
SET @mainQry=CONCAT(@mainQry,"SELECT db.game_id,SUM(db.pwt_mrp+db.direct_pwt_amt) mrpAmt,SUM(db.pwt_net_amt+db.direct_pwt_net_amt) netAmt FROM st_rep_dg_bo db INNER JOIN st_lms_organization_master om ON om.organization_id = db.organization_id INNER JOIN st_lms_city_master city ON om.city = city.city_name WHERE  db.finaldate>='",startDate,"' and db.finaldate <='",endDate,"' union all SELECT db.game_id,SUM(db.direct_pwt_amt) mrpAmt,SUM(db.direct_pwt_net_amt) netAmt FROM st_rep_dg_agent db INNER JOIN st_lms_organization_master om ON om.organization_id = db.organization_id INNER JOIN st_lms_city_master city ON om.city = city.city_name WHERE  db.finaldate>='",startDate,"' and db.finaldate <='",endDate,"'");
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
 
-- rollback CREATE PROCEDURE `drawPwtGameWise`(startDate TIMESTAMP, endDate TIMESTAMP, IN stateCode VARCHAR(10), IN cityCode VARCHAR(10)) BEGIN DECLARE done INT DEFAULT 0;DECLARE gameId INT;DECLARE gameCur CURSOR FOR SELECT game_id FROM st_dg_game_master;DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;SET @mainQry="select gm.game_id gameId, game_name gameName,mrpAmt,netAmt from st_dg_game_master gm,(select game_id,sum(mrpAmt) mrpAmt,sum(netAmt) netAmt from (";OPEN gameCur;read_loop: LOOP FETCH gameCur INTO gameId;IF done THEN LEAVE read_loop;END IF;SET @mainQry=CONCAT(@mainQry,"SELECT retPwt.game_id,SUM(retPwt.pwt_amt) mrpAmt,SUM(retPwt.pwt_amt + retPwt.agt_claim_comm) netAmt FROM st_dg_ret_pwt_",gameId,"  retPwt INNER JOIN st_lms_retailer_transaction_master retTxn ON retPwt.transaction_id = retTxn.transaction_id INNER JOIN st_lms_organization_master om ON om.organization_id = retPwt .retailer_org_id INNER JOIN st_lms_city_master city ON om.city = city.city_name WHERE retTxn.transaction_type IN('DG_PWT_AUTO','DG_PWT_PLR','DG_PWT') AND retTxn.transaction_date>='",startDate,"'  AND retTxn.transaction_date<='",endDate,"'");IF(stateCode != 'ALL') THEN SET @mainQry = CONCAT(@mainQry, " AND om.state_code = '",stateCode,"'");END IF;IF(cityCode != 'ALL') THEN SET @mainQry = CONCAT(@mainQry, " AND city.city_name = '",cityCode,"'");END IF;SET @mainQry = CONCAT(@mainQry, "  group by game_id union all ");END LOOP;CLOSE gameCur; SET @mainQry=CONCAT(@mainQry,"SELECT plrPwt.game_id, SUM(plrPwt.pwt_amt) mrpAmt,SUM(plrPwt.pwt_amt+plrPwt.agt_claim_comm) netAmt FROM st_dg_agt_direct_plr_pwt plrPwt INNER JOIN st_lms_organization_master om ON om.organization_id = plrPwt.agent_org_id INNER JOIN st_lms_city_master city ON om.city = city.city_name WHERE transaction_date>='",startDate,"' and transaction_date<='",endDate,"'");IF(stateCode != 'ALL') THEN SET @mainQry = CONCAT(@mainQry, " AND om.state_code = '",stateCode,"'");END IF;IF(cityCode != 'ALL') THEN SET @mainQry = CONCAT(@mainQry, " AND city.city_name = '",cityCode,"'");END IF;SET @mainQry = CONCAT(@mainQry, "  group by game_id union all ");SET @mainQry=CONCAT(@mainQry,"SELECT plrPwt.game_id,SUM(plrPwt.pwt_amt) mrpAmt,SUM(plrPwt.pwt_amt) netAmt FROM st_dg_bo_direct_plr_pwt plrPwt INNER JOIN st_lms_organization_master om ON om.organization_id = plrPwt.bo_org_id INNER JOIN st_lms_city_master city ON om.city = city.city_name WHERE plrPwt.transaction_date>='",startDate,"' and transaction_date<='",endDate,"'");IF(stateCode != 'ALL') THEN 	SET @mainQry = CONCAT(@mainQry, " AND om.state_code = '",stateCode,"'");END IF;IF(cityCode != 'ALL') THEN 	SET @mainQry = CONCAT(@mainQry, " AND city.city_name = '",cityCode,"'");END IF;SET @mainQry = CONCAT(@mainQry, "  group by game_id union all ");SET @mainQry=CONCAT(@mainQry,"SELECT db.game_id,SUM(db.pwt_mrp+db.direct_pwt_amt) mrpAmt,SUM(db.pwt_net_amt+db.direct_pwt_net_amt) netAmt FROM st_rep_dg_bo db INNER JOIN st_lms_organization_master om ON om.organization_id = db.organization_id INNER JOIN st_lms_city_master city ON om.city = city.city_name WHERE  db.finaldate>='",startDate,"' and db.finaldate <='",endDate,"' union all SELECT db.game_id,SUM(db.direct_pwt_amt) mrpAmt,SUM(db.direct_pwt_net_amt) netAmt FROM st_rep_dg_agent db INNER JOIN st_lms_organization_master om ON om.organization_id = db.organization_id INNER JOIN st_lms_city_master city ON om.city = city.city_name WHERE  db.finaldate>='",startDate,"' and db.finaldate <='",endDate,"'");IF(stateCode != 'ALL') THEN 	SET @mainQry = CONCAT(@mainQry, " AND om.state_code = '",stateCode,"'");END IF;IF(cityCode != 'ALL') THEN 	SET @mainQry = CONCAT(@mainQry, " AND city.city_name = '",cityCode,"'");END IF;SET @mainQry = CONCAT(@mainQry, "  group by game_id");SET@mainQry = CONCAT(@mainQry, ") pwtTlb group by game_id) pwtTlb where gm.game_id=pwtTlb.game_id");  PREPARE stmt FROM @mainQry;EXECUTE stmt;DEALLOCATE PREPARE stmt;END #

 
-- changeset Ishu-5663:1 endDelimiter:#

DROP PROCEDURE IF EXISTS `drawCollectionDayWisePerGame`#

CREATE DEFINER=`root`@`localhost` PROCEDURE `drawCollectionDayWisePerGame`(startDate TIMESTAMP,endDate TIMESTAMP,gameNo INT,agtOrgId INT,viewBy VARCHAR(20))
BEGIN
	
    if(viewBy='Agent' and agtOrgId!=0) then
		set @dgSaleCol = "net_amt";
		set @dgPwtCol = "pwt_amt+retailer_claim_comm";
		set @addDrawQry = concat(" and drs.retailer_org_id in (select organization_id from st_lms_organization_master where parent_id=", agtOrgId , ")");
		set @addDirPlrQry = concat(" and agent_org_id=",agtOrgId);
	else
		set @dgSaleCol = "agent_net_amt";
		set @dgPwtCol = "pwt_amt+agt_claim_comm";
		set @addDrawQry = "";
		set @addDirPlrQry = "";
	end if;
	
 
 set @mainQry=concat("select main.alldate,sum(main.sale)as sale,sum(main.cancel)as cancel,sum(main.pwt)as pwt,sum(main.pwtDir)as pwtDir from (select date(alldate) alldate,sale,cancel,pwt,ifnull(pwtDir,0.0) pwtDir from (select sale.alldate,ifnull(sale,0.0) sale,ifnull(cancel,0.0) cancel,ifnull(pwt,0.0) pwt from (select alldate,sale from tempdate left outer join (select date(transaction_date) tx_date,sum(",@dgSaleCol,") sale from st_dg_ret_sale_",gameNo," drs,st_lms_retailer_transaction_master rtm where rtm.retailer_org_id in (select organization_id from st_lms_user_master where parent_user_id not in (select user_id from st_lms_user_master  where (termination_date < '",startDate,"' or registration_date> '",endDate,"') and isrolehead='Y' and organization_type='AGENT') and organization_type='RETAILER' ) and drs.transaction_id=rtm.transaction_id and rtm.transaction_date>='",startDate,"' and rtm.transaction_date<='",endDate,"' and transaction_type in('DG_SALE','DG_SALE_OFFLINE') ",@addDrawQry," group by tx_date) sale on alldate=sale.tx_date) sale inner join (select alldate,cancel from tempdate left outer join (select date(transaction_date) tx_date,sum(",@dgSaleCol,") cancel from st_dg_ret_sale_refund_",gameNo," drs,st_lms_retailer_transaction_master rtm where rtm.retailer_org_id in (select organization_id from st_lms_user_master where parent_user_id not in (select user_id from st_lms_user_master  where (termination_date < '",startDate,"' or registration_date> '",endDate,"') and isrolehead='Y' and organization_type='AGENT') and organization_type='RETAILER' ) and drs.transaction_id=rtm.transaction_id and rtm.transaction_date>='",startDate,"' and rtm.transaction_date<='",endDate,"' and transaction_type in('DG_REFUND_CANCEL','DG_REFUND_FAILED') ",@addDrawQry," group by tx_date) cancel on alldate=cancel.tx_date) cancel inner join (select alldate,pwt from tempdate left outer join (select date(transaction_date) tx_date,sum(",@dgPwtCol,") pwt from st_dg_ret_pwt_",gameNo," drs,st_lms_retailer_transaction_master rtm where rtm.retailer_org_id in (select organization_id from st_lms_user_master where parent_user_id not in (select user_id from st_lms_user_master  where (termination_date < '",startDate,"' or registration_date> '",endDate,"') and isrolehead='Y' and organization_type='AGENT') and organization_type='RETAILER' ) and  drs.transaction_id=rtm.transaction_id and rtm.transaction_date>='",startDate,"' and rtm.transaction_date<='",endDate,"' and transaction_type in('DG_PWT_AUTO','DG_PWT_PLR','DG_PWT') ",@addDrawQry," group by tx_date) pwt on alldate=pwt.tx_date) pwt on sale.alldate=cancel.alldate and sale.alldate=pwt.alldate and cancel.alldate=pwt.alldate) a left outer join (select date(transaction_date) tx_date ,sum(pwt_amt+agt_claim_comm) pwtDir from st_dg_agt_direct_plr_pwt where 
agent_org_id in (select organization_id from st_lms_user_master  where (termination_date > '",startDate,"' or registration_date< '",endDate,"') and isrolehead='Y' and organization_type='AGENT' ) and transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and game_id=",gameNo," ",@addDirPlrQry," group by tx_date) b on alldate=tx_date  ");
if(viewBy='Agent' and agtOrgId!=0) then
		set @mainQry=concat(@mainQry," union all select alldate,sum( sale) sale, sum(cancel) cancel,sum(pwt) pwt, sum(directPlrPwt) directPlrPwt from (select finaldate as alldate,sum(sale_net) as sale ,sum(ref_net_amt) cancel, sum(pwt_net_amt) pwt,0.00 directPlrPwt  from st_rep_dg_retailer where parent_id=",agtOrgId," and game_id=",gameNo," and finaldate>=date('",startDate,"') and finaldate<=date('",endDate,"') group by finaldate union all select finaldate as alldate,0.00 as sale ,0.00 cancel,0.00 pwt,sum(ifnull(direct_pwt_net_amt,0.00))as directPlrPwt  from st_rep_dg_agent where organization_id in (select organization_id from st_lms_user_master  where (termination_date > '",startDate,"' or registration_date< '",endDate,"') and isrolehead='Y' and organization_type='AGENT' and organization_id=",agtOrgId,") and organization_id=",agtOrgId," and game_id=",gameNo," and finaldate>=date('",startDate,"') and finaldate<=date('",endDate,"') group by finaldate) al group by alldate)as main group by main.alldate");
	else
		set @mainQry=concat(@mainQry," union all select finaldate as alldate,sum(sale_net) as sale,sum(ref_net_amt) as cancel, sum(pwt_net_amt) as pwt, sum(direct_pwt_net_amt) as pwtDir from st_rep_dg_agent where organization_id in (select organization_id from st_lms_user_master  where (termination_date > '",startDate,"' or registration_date< '",endDate,"') and isrolehead='Y' and organization_type='AGENT' and organization_id=",agtOrgId,") and game_id=",gameNo," and finaldate>=date('",startDate,"') and finaldate<=date('",endDate,"') and organization_id=",agtOrgId," group by finaldate)as main group by main.alldate ");
	end if;

 PREPARE stmt FROM @mainQry;
 EXECUTE stmt;
 DEALLOCATE PREPARE stmt;
    END
#

-- rollback CREATE PROCEDURE `drawCollectionDayWisePerGame`(startDate TIMESTAMP,endDate TIMESTAMP,gameNo INT,agtOrgId INT,viewBy VARCHAR(20)) BEGIN if(viewBy='Agent' and agtOrgId!=0) then set @dgSaleCol = "net_amt"; set @dgPwtCol = "pwt_amt+retailer_claim_comm"; set @addDrawQry = concat(" and drs.retailer_org_id in (select organization_id from st_lms_organization_master where parent_id=", agtOrgId , ")"); set @addDirPlrQry = concat(" and agent_org_id=",agtOrgId); else set @dgSaleCol = "agent_net_amt"; set @dgPwtCol = "pwt_amt+agt_claim_comm"; set @addDrawQry = ""; set @addDirPlrQry = ""; end if; set @mainQry=concat("select main.alldate,sum(main.sale)as sale,sum(main.cancel)as cancel,sum(main.pwt)as pwt,sum(main.pwtDir)as pwtDir from (select date(alldate) alldate,sale,cancel,pwt,ifnull(pwtDir,0.0) pwtDir from (select sale.alldate,ifnull(sale,0.0) sale,ifnull(cancel,0.0) cancel,ifnull(pwt,0.0) pwt from (select alldate,sale from tempdate left outer join (select date(transaction_date) tx_date,sum(",@dgSaleCol,") sale from st_dg_ret_sale_",gameNo," drs,st_lms_retailer_transaction_master rtm where rtm.retailer_org_id in (select organization_id from st_lms_user_master where parent_user_id not in (select user_id from st_lms_user_master  where (termination_date < '",startDate,"' or registration_date> '",endDate,"') and isrolehead='Y' and organization_type='AGENT') and organization_type='RETAILER' ) and drs.transaction_id=rtm.transaction_id and rtm.transaction_date>='",startDate,"' and rtm.transaction_date<='",endDate,"' and transaction_type in('DG_SALE','DG_SALE_OFFLINE') ",@addDrawQry," group by tx_date) sale on alldate=sale.tx_date) sale inner join (select alldate,cancel from tempdate left outer join (select date(transaction_date) tx_date,sum(",@dgSaleCol,") cancel from st_dg_ret_sale_refund_",gameNo," drs,st_lms_retailer_transaction_master rtm where rtm.retailer_org_id in (select organization_id from st_lms_user_master where parent_user_id not in (select user_id from st_lms_user_master  where (termination_date < '",startDate,"' or registration_date> '",endDate,"') and isrolehead='Y' and organization_type='AGENT') and organization_type='RETAILER' ) and drs.transaction_id=rtm.transaction_id and rtm.transaction_date>='",startDate,"' and rtm.transaction_date<='",endDate,"' and transaction_type in('DG_REFUND_CANCEL','DG_REFUND_FAILED') ",@addDrawQry," group by tx_date) cancel on alldate=cancel.tx_date) cancel inner join (select alldate,pwt from tempdate left outer join (select date(transaction_date) tx_date,sum(",@dgPwtCol,") pwt from st_dg_ret_pwt_",gameNo," drs,st_lms_retailer_transaction_master rtm where rtm.retailer_org_id in (select organization_id from st_lms_user_master where parent_user_id not in (select user_id from st_lms_user_master  where (termination_date < '",startDate,"' or registration_date> '",endDate,"') and isrolehead='Y' and organization_type='AGENT') and organization_type='RETAILER' ) and  drs.transaction_id=rtm.transaction_id and rtm.transaction_date>='",startDate,"' and rtm.transaction_date<='",endDate,"' and transaction_type in('DG_PWT_AUTO','DG_PWT_PLR','DG_PWT') ",@addDrawQry," group by tx_date) pwt on alldate=pwt.tx_date) pwt on sale.alldate=cancel.alldate and sale.alldate=pwt.alldate and cancel.alldate=pwt.alldate) a left outer join (select date(transaction_date) tx_date ,sum(pwt_amt+agt_claim_comm) pwtDir from st_dg_agt_direct_plr_pwt where agent_org_id in (select organization_id from st_lms_user_master  where (termination_date > '",startDate,"' or registration_date< '",endDate,"') and isrolehead='Y' and organization_type='AGENT' ) and transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and game_id=",gameNo," ",@addDirPlrQry," group by tx_date) b on alldate=tx_date  "); if(viewBy='Agent' and agtOrgId!=0) then set @mainQry=concat(@mainQry," union all select dgr.finaldate as alldate,sum(dgr.sale_net) as sale ,sum(dgr.ref_net_amt) cancel, sum(dgr.pwt_net_amt) pwt,sum(ifnull(dga.direct_pwt_net_amt,0.00))as pwtDir  from st_rep_dg_retailer as dgr left outer join  st_rep_dg_agent as dga on dgr.finaldate=dga.finaldate and dgr.parent_id=dga.parent_id where dga.organization_id in (select organization_id from st_lms_user_master where termination_date> '",startDate,"' or registration_date <'",endDate,"' and isrolehead='Y'  and organization_type='AGENT') and dgr.parent_id=",agtOrgId," and dgr.game_id=",gameNo," and dgr.finaldate>=date('",startDate,"') and dgr.finaldate<=date('",endDate,"') group by dgr.finaldate)as main group by main.alldate "); else set @mainQry=concat(@mainQry," union all select finaldate as alldate,sum(sale_net) as sale,sum(ref_net_amt) as cancel, sum(pwt_net_amt) as pwt, sum(direct_pwt_net_amt) as pwtDir from st_rep_dg_agent where organization_id in (select organization_id from st_lms_user_master  where (termination_date > '",startDate,"' or registration_date< '",endDate,"') and isrolehead='Y' and organization_type='AGENT' ) and game_id=",gameNo," and finaldate>=date('",startDate,"') and finaldate<=date('",endDate,"') group by finaldate)as main group by main.alldate  "); end if; PREPARE stmt FROM @mainQry; EXECUTE stmt; DEALLOCATE PREPARE stmt; END #

-- changeset mayank-4675:1 endDelimiter:#

#
DROP PROCEDURE IF EXISTS `iwSaleRetailerWise`;
#
CREATE PROCEDURE `iwSaleRetailerWise`(startDate Timestamp , endDate Timestamp,agtOrgId int, IN cityCode VARCHAR(10), IN stateCode VARCHAR(10))
BEGIN
DECLARE done INT DEFAULT 0;
DECLARE gameId INT;
DECLARE gameCur CURSOR FOR select game_id from st_iw_game_master;
DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;
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
 set @mainQry=concat("select om.organization_id, sm.name state_name, cm.city_name city_name,",concat("om.",@selCol),", ifnull(sum(mrpAmt),0.0) mrpAmt,ifnull(sum(netAmt),0.0) netAmt from st_lms_organization_master om right outer join (select organization_id,sum(mrpAmt) mrpAmt,sum(netAmt) netAmt from st_lms_organization_master,(select retailer_org_id,sum(mrpAmt) mrpAmt,sum(netAmt) netAmt from (");
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
 set @mainQry=concat(@mainQry,") saleTlb group by retailer_org_id) saleTlb where retailer_org_id=organization_id and parent_id=",agtOrgId," group by organization_id union all select organization_id,sum(sale_mrp-ref_sale_mrp) mrpAmt,sum(sale_net-ref_net_amt) netAmt from st_rep_iw_retailer where finaldate>='",startDate,"' and finaldate<='",endDate,"' and parent_id=",agtOrgId," group by organization_id) saleTlb on saleTlb.organization_id=om.organization_id   inner join st_lms_state_master sm on om.state_code = sm.state_code inner join st_lms_city_master cm on cm.city_name = om.city ");
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
    
    
-- changeset Ishu-6023:2 endDelimiter:#

DROP PROCEDURE IF EXISTS `drawPwtGameWise`#

CREATE PROCEDURE `drawPwtGameWise`(startDate TIMESTAMP, endDate TIMESTAMP,  IN stateCode VARCHAR(10), IN cityCode VARCHAR(10))
BEGIN
DECLARE done INT DEFAULT 0;
DECLARE gameId INT;
DECLARE gameCur CURSOR FOR SELECT game_id FROM st_dg_game_master;
DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;
 SET @mainQry="select gm.game_id gameId, game_name gameName,mrpAmt,netAmt from st_dg_game_master gm,(select game_id,sum(mrpAmt) mrpAmt,sum(netAmt) netAmt from (";
	OPEN gameCur;
	  read_loop: LOOP
	    FETCH gameCur INTO gameId;
	    IF done THEN
	      LEAVE read_loop;
	    END IF;
SET @mainQry=CONCAT(@mainQry,"SELECT retPwt.game_id,SUM(retPwt.pwt_amt) mrpAmt,SUM(retPwt.pwt_amt + retPwt.agt_claim_comm) netAmt FROM st_dg_ret_pwt_",gameId,"  retPwt INNER JOIN st_lms_retailer_transaction_master retTxn ON retPwt.transaction_id = retTxn.transaction_id INNER JOIN st_lms_organization_master om ON om.organization_id = retPwt .retailer_org_id INNER JOIN st_lms_city_master city ON om.city = city.city_name WHERE retTxn.transaction_type IN('DG_PWT_AUTO','DG_PWT_PLR','DG_PWT') AND retTxn.transaction_date>='",startDate,"'  AND retTxn.transaction_date<='",endDate,"'");
IF(stateCode != 'ALL') THEN 
	SET @mainQry = CONCAT(@mainQry, " AND om.state_code = '",stateCode,"'");
END IF;
IF(cityCode != 'ALL') THEN
	SET @mainQry = CONCAT(@mainQry, " AND city.city_name = '",cityCode,"'");
END IF;
SET @mainQry = CONCAT(@mainQry, "  group by game_id union all "); 
	  END LOOP;
	CLOSE gameCur;
 SET @mainQry=CONCAT(@mainQry,"SELECT plrPwt.game_id, SUM(plrPwt.pwt_amt) mrpAmt,SUM(plrPwt.pwt_amt+plrPwt.agt_claim_comm) netAmt FROM st_dg_agt_direct_plr_pwt plrPwt INNER JOIN st_lms_organization_master om ON om.organization_id = plrPwt.agent_org_id INNER JOIN st_lms_city_master city ON om.city = city.city_name WHERE transaction_date>='",startDate,"' and transaction_date<='",endDate,"'");
IF(stateCode != 'ALL') THEN 
	SET @mainQry = CONCAT(@mainQry, " AND om.state_code = '",stateCode,"'");
END IF;
IF(cityCode != 'ALL') THEN
	SET @mainQry = CONCAT(@mainQry, " AND city.city_name = '",cityCode,"'");
END IF;
SET @mainQry = CONCAT(@mainQry, "  group by game_id union all ");
	
SET @mainQry=CONCAT(@mainQry,"SELECT plrPwt.game_id,SUM(plrPwt.pwt_amt) mrpAmt,SUM(plrPwt.pwt_amt) netAmt FROM st_dg_bo_direct_plr_pwt plrPwt INNER JOIN st_lms_organization_master om ON om.organization_id = plrPwt.bo_org_id INNER JOIN st_lms_city_master city ON om.city = city.city_name WHERE plrPwt.transaction_date>='",startDate,"' and transaction_date<='",endDate,"'");
IF(stateCode != 'ALL') THEN 
	SET @mainQry = CONCAT(@mainQry, " AND om.state_code = '",stateCode,"'");
END IF;
IF(cityCode != 'ALL') THEN
	SET @mainQry = CONCAT(@mainQry, " AND city.city_name = '",cityCode,"'");
END IF;
SET @mainQry = CONCAT(@mainQry, "  group by game_id union all ");
SET @mainQry=CONCAT(@mainQry,"SELECT db.game_id,SUM(db.pwt_mrp+db.direct_pwt_amt) mrpAmt,SUM(db.pwt_net_amt+db.direct_pwt_net_amt) netAmt FROM st_rep_dg_bo db INNER JOIN st_lms_organization_master om ON om.organization_id = db.organization_id INNER JOIN st_lms_city_master city ON om.city = city.city_name WHERE  db.finaldate>='",startDate,"' and db.finaldate <='",endDate,"'");
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

-- rollback DROP PROCEDURE IF EXISTS `drawPwtGameWise`# CREATE PROCEDURE `drawPwtGameWise`(startDate TIMESTAMP, endDate TIMESTAMP,  IN stateCode VARCHAR(10), IN cityCode VARCHAR(10)) BEGIN DECLARE done INT DEFAULT 0; DECLARE gameId INT; DECLARE gameCur CURSOR FOR SELECT game_id FROM st_dg_game_master; DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1; SET @mainQry="select gm.game_id gameId, game_name gameName,mrpAmt,netAmt from st_dg_game_master gm,(select game_id,sum(mrpAmt) mrpAmt,sum(netAmt) netAmt from ("; OPEN gameCur; read_loop: LOOP FETCH gameCur INTO gameId; IF done THEN LEAVE read_loop; END IF; SET @mainQry=CONCAT(@mainQry,"SELECT retPwt.game_id,SUM(retPwt.pwt_amt) mrpAmt,SUM(retPwt.pwt_amt + retPwt.agt_claim_comm) netAmt FROM st_dg_ret_pwt_",gameId,"  retPwt INNER JOIN st_lms_retailer_transaction_master retTxn ON retPwt.transaction_id = retTxn.transaction_id INNER JOIN st_lms_organization_master om ON om.organization_id = retPwt .retailer_org_id INNER JOIN st_lms_city_master city ON om.city = city.city_name WHERE retTxn.transaction_type IN('DG_PWT_AUTO','DG_PWT_PLR','DG_PWT') AND retTxn.transaction_date>='",startDate,"'  AND retTxn.transaction_date<='",endDate,"'"); IF(stateCode != 'ALL') THEN SET @mainQry = CONCAT(@mainQry, " AND om.state_code = '",stateCode,"'"); END IF; IF(cityCode != 'ALL') THEN SET @mainQry = CONCAT(@mainQry, " AND city.city_name = '",cityCode,"'"); END IF; SET @mainQry = CONCAT(@mainQry, "  group by game_id union all "); END LOOP; CLOSE gameCur; SET @mainQry=CONCAT(@mainQry,"SELECT plrPwt.game_id, SUM(plrPwt.pwt_amt) mrpAmt,SUM(plrPwt.pwt_amt+plrPwt.agt_claim_comm) netAmt FROM st_dg_agt_direct_plr_pwt plrPwt INNER JOIN st_lms_organization_master om ON om.organization_id = plrPwt.agent_org_id INNER JOIN st_lms_city_master city ON om.city = city.city_name WHERE transaction_date>='",startDate,"' and transaction_date<='",endDate,"'"); IF(stateCode != 'ALL') THEN SET @mainQry = CONCAT(@mainQry, " AND om.state_code = '",stateCode,"'"); END IF; IF(cityCode != 'ALL') THEN SET @mainQry = CONCAT(@mainQry, " AND city.city_name = '",cityCode,"'"); END IF; SET @mainQry = CONCAT(@mainQry, "  group by game_id union all "); SET @mainQry=CONCAT(@mainQry,"SELECT plrPwt.game_id,SUM(plrPwt.pwt_amt) mrpAmt,SUM(plrPwt.pwt_amt) netAmt FROM st_dg_bo_direct_plr_pwt plrPwt INNER JOIN st_lms_organization_master om ON om.organization_id = plrPwt.bo_org_id INNER JOIN st_lms_city_master city ON om.city = city.city_name WHERE plrPwt.transaction_date>='",startDate,"' and transaction_date<='",endDate,"'"); IF(stateCode != 'ALL') THEN SET @mainQry = CONCAT(@mainQry, " AND om.state_code = '",stateCode,"'"); END IF; IF(cityCode != 'ALL') THEN SET @mainQry = CONCAT(@mainQry, " AND city.city_name = '",cityCode,"'"); END IF; SET @mainQry = CONCAT(@mainQry, "  group by game_id union all "); SET @mainQry=CONCAT(@mainQry,"SELECT db.game_id,SUM(db.pwt_mrp+db.direct_pwt_amt) mrpAmt,SUM(db.pwt_net_amt+db.direct_pwt_net_amt) netAmt FROM st_rep_dg_bo db INNER JOIN st_lms_organization_master om ON om.organization_id = db.organization_id INNER JOIN st_lms_city_master city ON om.city = city.city_name WHERE  db.finaldate>='",startDate,"' and db.finaldate <='",endDate,"' union all SELECT db.game_id,SUM(db.direct_pwt_amt) mrpAmt,SUM(db.direct_pwt_net_amt) netAmt FROM st_rep_dg_agent db INNER JOIN st_lms_organization_master om ON om.organization_id = db.organization_id INNER JOIN st_lms_city_master city ON om.city = city.city_name WHERE  db.finaldate>='",startDate,"' and db.finaldate <='",endDate,"'"); IF(stateCode != 'ALL') THEN SET @mainQry = CONCAT(@mainQry, " AND om.state_code = '",stateCode,"'"); END IF; IF(cityCode != 'ALL') THEN SET @mainQry = CONCAT(@mainQry, " AND city.city_name = '",cityCode,"'"); END IF; SET @mainQry = CONCAT(@mainQry, "  group by game_id"); SET@mainQry = CONCAT(@mainQry, ") pwtTlb group by game_id) pwtTlb where gm.game_id=pwtTlb.game_id"); PREPARE stmt FROM @mainQry; EXECUTE stmt; DEALLOCATE PREPARE stmt; END #
