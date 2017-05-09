--liquibase formatted sql

--changeset mayank-4252:1 endDelimiter:#
#
DROP PROCEDURE IF EXISTS `creditDebitPaymentDateWiseOverAll`;
#
CREATE PROCEDURE `creditDebitPaymentDateWiseOverAll`(startDate timestamp,endDate timestamp,agtOrgId int)
BEGIN
set @mainQry=concat("select ifnull(credit.date,debit.date) date,ifnull(debit,0.0) debit,ifnull(credit,0.0) credit,ifnull(training,0.0) training,ifnull(incentive,0.0) incentive,ifnull(others,0.0) others  from ((select agent_org_id,sum(amount) debit,date(transaction_date) date from st_lms_bo_debit_note debit,st_lms_bo_transaction_master btm where debit.transaction_id=btm.transaction_id and debit.transaction_type IN ('DR_NOTE_CASH','DR_NOTE') and transaction_date>='",startDate,"' and transaction_date<='", endDate, "' and agent_org_id= ",agtOrgId," group by date(transaction_date))  debit left join (select agent_org_id,sum(credit) credit,sum(training) training,sum(incentive) incentive,sum(others) others,date from (select agent_org_id, sum(amount) credit,0 training, 0 incentive, 0 others,date(transaction_date) date from st_lms_bo_credit_note credit,st_lms_bo_transaction_master btm where credit.transaction_id=btm.transaction_id and credit.transaction_type IN ('CR_NOTE_CASH','CR_NOTE') and transaction_date>='",startDate,"' and transaction_date<='", endDate, "'and agent_org_id= ",agtOrgId," group by date(transaction_date) union all select agent_org_id,0 credit, sum(amount) training, 0 incentive, 0 others,date(transaction_date) date from st_lms_bo_credit_note credit,st_lms_bo_transaction_master btm where credit.transaction_id=btm.transaction_id and credit.transaction_type IN ('CR_NOTE_CASH','CR_NOTE') and credit.reason IN ('CR_WEEKLY_EXP','CR_DAILY_EXP') and transaction_date>='",startDate,"' and transaction_date<='", endDate, "'and agent_org_id= ",agtOrgId," group by date(transaction_date) union all select agent_org_id,0 credit, 0 training, sum(amount) incentive,0 others,date(transaction_date) date from st_lms_bo_credit_note credit,st_lms_bo_transaction_master btm where credit.transaction_id=btm.transaction_id and credit.transaction_type IN ('CR_NOTE_CASH','CR_NOTE') and credit.reason IN ('CR_DAILY_INCENTIVE','CR_WEEKLY_INCENTIVE') and transaction_date>='",startDate,"' and transaction_date<='", endDate, "'and agent_org_id= ",agtOrgId," group by date(transaction_date) union all select agent_org_id,0 credit, 0 training, 0 incentive, sum(amount) others,date(transaction_date) date from st_lms_bo_credit_note credit,st_lms_bo_transaction_master btm where credit.transaction_id=btm.transaction_id and credit.transaction_type IN ('CR_NOTE_CASH','CR_NOTE') and credit.reason NOT IN ('CR_DAILY_INCENTIVE','CR_WEEKLY_INCENTIVE','CR_WEEKLY_EXP','CR_DAILY_EXP') and transaction_date>='",startDate,"' and transaction_date<='", endDate, "'and agent_org_id= ",agtOrgId," group by date(transaction_date) ) ab group by date) credit on debit.date=credit.date ) union select ifnull(credit.date,debit.date) date,ifnull(debit,0.0) debit,ifnull(credit,0.0) credit,ifnull(training,0.0) training,ifnull(incentive,0.0) incentive,ifnull(others,0.0) others from ((select agent_org_id,sum(amount) debit,date(transaction_date) date from st_lms_bo_debit_note debit,st_lms_bo_transaction_master btm where debit.transaction_id=btm.transaction_id and debit.transaction_type IN ('DR_NOTE_CASH','DR_NOTE') and transaction_date>='",startDate,"' and transaction_date<='", endDate, "' and agent_org_id= ",agtOrgId," group by date(transaction_date))  debit right join (select agent_org_id,sum(credit) credit,sum(training) training,sum(incentive) incentive,sum(others) others,date from (select agent_org_id, sum(amount) credit,0 training, 0 incentive, 0 others,date(transaction_date) date from st_lms_bo_credit_note credit,st_lms_bo_transaction_master btm where credit.transaction_id=btm.transaction_id and credit.transaction_type IN ('CR_NOTE_CASH','CR_NOTE') and transaction_date>='",startDate,"' and transaction_date<='", endDate, "'and agent_org_id= ",agtOrgId," group by date(transaction_date) union all select agent_org_id,0 credit, sum(amount) training, 0 incentive, 0 others,date(transaction_date) date from st_lms_bo_credit_note credit,st_lms_bo_transaction_master btm where credit.transaction_id=btm.transaction_id and credit.transaction_type IN ('CR_NOTE_CASH','CR_NOTE') and credit.reason IN ('CR_WEEKLY_EXP','CR_DAILY_EXP') and transaction_date>='",startDate,"' and transaction_date<='", endDate, "'and agent_org_id= ",agtOrgId," group by date(transaction_date) union all select agent_org_id,0 credit, 0 training, sum(amount) incentive,0 others,date(transaction_date) date from st_lms_bo_credit_note credit,st_lms_bo_transaction_master btm where credit.transaction_id=btm.transaction_id and credit.transaction_type IN ('CR_NOTE_CASH','CR_NOTE') and credit.reason IN ('CR_DAILY_INCENTIVE','CR_WEEKLY_INCENTIVE') and transaction_date>='",startDate,"' and transaction_date<='", endDate, "'and agent_org_id= ",agtOrgId," group by date(transaction_date) union all select agent_org_id,0 credit, 0 training, 0 incentive, sum(amount) others,date(transaction_date) date from st_lms_bo_credit_note credit,st_lms_bo_transaction_master btm where credit.transaction_id=btm.transaction_id and credit.transaction_type IN ('CR_NOTE_CASH','CR_NOTE') and credit.reason NOT IN ('CR_DAILY_INCENTIVE','CR_WEEKLY_INCENTIVE','CR_WEEKLY_EXP','CR_DAILY_EXP') and transaction_date>='",startDate,"' and transaction_date<='", endDate, "'and agent_org_id= ",agtOrgId," group by date(transaction_date) ) ab group by date) credit on debit.date=credit.date ) union all select finaldate,sum(debit_note) as debit,sum(credit_note) as credit, 0 training, 0 incentive, 0 others from st_rep_bo_payments where finaldate>='",startDate,"' and finaldate<='", endDate, "' and agent_org_id=",agtOrgId," group by finaldate");


	PREPARE stmt FROM @mainQry;
	EXECUTE stmt;
	DEALLOCATE PREPARE stmt;    
END
#
-- rollback CREATE PROCEDURE `creditDebitPaymentDateWiseOverAll`(startDate timestamp,endDate timestamp,agtOrgId int) BEGIN set @mainQry=concat("select ifnull(credit.date,debit.date) date,ifnull(debit,0.0) debit,ifnull(credit,0.0) credit,ifnull(training,0.0) training,ifnull(incentive,0.0) incentive,ifnull(others,0.0) others  from ((select agent_org_id,sum(amount) debit,date(transaction_date) date from st_lms_bo_debit_note debit,st_lms_bo_transaction_master btm where debit.transaction_id=btm.transaction_id and debit.transaction_type IN ('DR_NOTE_CASH','DR_NOTE') and transaction_date>='",startDate,"' and transaction_date<='", endDate, "' and agent_org_id= ",agtOrgId," group by date(transaction_date))  debit left join (select agent_org_id,sum(credit) credit,sum(training) training,sum(incentive) incentive,sum(others) others,date from (select agent_org_id, sum(amount) credit,0 training, 0 incentive, 0 others,date(transaction_date) date from st_lms_bo_credit_note credit,st_lms_bo_transaction_master btm where credit.transaction_id=btm.transaction_id and credit.transaction_type IN ('CR_NOTE_CASH','CR_NOTE') and transaction_date>='",startDate,"' and transaction_date<='", endDate, "'and agent_org_id= ",agtOrgId," group by date(transaction_date) union all select agent_org_id,0 credit, sum(amount) training, 0 incentive, 0 others,date(transaction_date) date from st_lms_bo_credit_note credit,st_lms_bo_transaction_master btm where credit.transaction_id=btm.transaction_id and credit.transaction_type IN ('CR_NOTE_CASH','CR_NOTE') and credit.reason IN ('CR_WEEKLY_EXP','CR_DAILY_EXP') and transaction_date>='",startDate,"' and transaction_date<='", endDate, "'and agent_org_id= ",agtOrgId," group by date(transaction_date) union all select agent_org_id,0 credit, 0 training, sum(amount) incentive,0 others,date(transaction_date) date from st_lms_bo_credit_note credit,st_lms_bo_transaction_master btm where credit.transaction_id=btm.transaction_id and credit.transaction_type IN ('CR_NOTE_CASH','CR_NOTE') and credit.reason IN ('CR_DAILY_INCENTIVE','CR_WEEKLY_INCENTIVE') and transaction_date>='",startDate,"' and transaction_date<='", endDate, "'and agent_org_id= ",agtOrgId," group by date(transaction_date) union all select agent_org_id,0 credit, 0 training, 0 incentive, sum(amount) others,date(transaction_date) date from st_lms_bo_credit_note credit,st_lms_bo_transaction_master btm where credit.transaction_id=btm.transaction_id and credit.transaction_type IN ('CR_NOTE_CASH','CR_NOTE') and credit.reason NOT IN ('CR_DAILY_INCENTIVE','CR_WEEKLY_INCENTIVE','CR_WEEKLY_EXP','CR_DAILY_EXP') and transaction_date>='",startDate,"' and transaction_date<='", endDate, "'and agent_org_id= ",agtOrgId," group by date(transaction_date) ) ab group by date) credit on debit.date=credit.date ) union select ifnull(credit.date,debit.date) date,ifnull(debit,0.0) debit,ifnull(credit,0.0) credit,ifnull(training,0.0) training,ifnull(incentive,0.0) incentive,ifnull(others,0.0) others from ((select agent_org_id,sum(amount) debit,date(transaction_date) date from st_lms_bo_debit_note debit,st_lms_bo_transaction_master btm where debit.transaction_id=btm.transaction_id and debit.transaction_type IN ('DR_NOTE_CASH','DR_NOTE') and transaction_date>='",startDate,"' and transaction_date<='", endDate, "' and agent_org_id= ",agtOrgId," group by date(transaction_date))  debit right join (select agent_org_id,sum(credit) credit,sum(training) training,sum(incentive) incentive,sum(others) others,date from (select agent_org_id, sum(amount) credit,0 training, 0 incentive, 0 others,date(transaction_date) date from st_lms_bo_credit_note credit,st_lms_bo_transaction_master btm where credit.transaction_id=btm.transaction_id and credit.transaction_type IN ('CR_NOTE_CASH','CR_NOTE') and transaction_date>='",startDate,"' and transaction_date<='", endDate, "'and agent_org_id= ",agtOrgId," group by date(transaction_date) union all select agent_org_id,0 credit, sum(amount) training, 0 incentive, 0 others,date(transaction_date) date from st_lms_bo_credit_note credit,st_lms_bo_transaction_master btm where credit.transaction_id=btm.transaction_id and credit.transaction_type IN ('CR_NOTE_CASH','CR_NOTE') and credit.reason IN ('CR_WEEKLY_EXP','CR_DAILY_EXP') and transaction_date>='",startDate,"' and transaction_date<='", endDate, "'and agent_org_id= ",agtOrgId," group by date(transaction_date) union all select agent_org_id,0 credit, 0 training, sum(amount) incentive,0 others,date(transaction_date) date from st_lms_bo_credit_note credit,st_lms_bo_transaction_master btm where credit.transaction_id=btm.transaction_id and credit.transaction_type IN ('CR_NOTE_CASH','CR_NOTE') and credit.reason IN ('CR_DAILY_INCENTIVE','CR_WEEKLY_INCENTIVE') and transaction_date>='",startDate,"' and transaction_date<='", endDate, "'and agent_org_id= ",agtOrgId," group by date(transaction_date) union all select agent_org_id,0 credit, 0 training, 0 incentive, sum(amount) others,date(transaction_date) date from st_lms_bo_credit_note credit,st_lms_bo_transaction_master btm where credit.transaction_id=btm.transaction_id and credit.transaction_type IN ('CR_NOTE_CASH','CR_NOTE') and credit.reason NOT IN ('CR_DAILY_INCENTIVE','CR_WEEKLY_INCENTIVE','CR_WEEKLY_EXP','CR_DAILY_EXP') and transaction_date>='",startDate,"' and transaction_date<='", endDate, "'and agent_org_id= ",agtOrgId," group by date(transaction_date) ) ab group by date) credit on debit.date=credit.date ) union all select finaldate,sum(debit_note) as debit,sum(credit_note) as credit, 0 training, 0 incentive, 0 others from st_rep_bo_payments where finaldate>='",startDate,"' and finaldate<='", endDate, "' and agent_org_id=",agtOrgId," group by finaldate");PREPARE stmt FROM @mainQry;EXECUTE stmt;DEALLOCATE PREPARE stmt; END #

--changeset mayank-5410:2 endDelimiter:#
DROP PROCEDURE IF EXISTS `drawPwtGameWise`;
#
CREATE  PROCEDURE `drawPwtGameWise`(startDate TIMESTAMP, endDate TIMESTAMP,  IN stateCode VARCHAR(10), IN cityCode VARCHAR(10))
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
 SET @mainQry=CONCAT(@mainQry,"SELECT plrPwt.game_id, SUM(plrPwt.pwt_amt) mrpAmt,SUM(plrPwt.pwt_amt+plrPwt.agt_claim_comm) netAmt FROM st_dg_agt_direct_plr_pwt plrPwt INNER JOIN st_lms_organization_master om ON om.organization_id = plrPwt.agent_org_id INNER JOIN st_lms_city_master city ON om.city = city.city_code WHERE transaction_date>='",startDate,"' and transaction_date<='",endDate,"'");
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
SET @mainQry=CONCAT(@mainQry,"SELECT db.game_id,SUM(db.pwt_mrp+db.direct_pwt_amt) mrpAmt,SUM(db.pwt_net_amt+db.direct_pwt_net_amt) netAmt FROM st_rep_dg_bo db INNER JOIN st_lms_organization_master om ON om.organization_id = db.organization_id INNER JOIN st_lms_city_master city ON om.city = city.city_name WHERE db.finaldate>='",startDate,"' and db.finaldate<='",endDate,"'");
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
  
  
  
  
  
--changeset mayank-5410:3 endDelimiter:#
#  
  DROP PROCEDURE IF EXISTS `drawPwtGameWise`;
#
CREATE  PROCEDURE `drawPwtGameWise`(startDate TIMESTAMP, endDate TIMESTAMP,  IN stateCode VARCHAR(10), IN cityCode VARCHAR(10))
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
SET @mainQry=CONCAT(@mainQry,"SELECT db.game_id,SUM(db.pwt_mrp+db.direct_pwt_amt) mrpAmt,SUM(db.pwt_net_amt+db.direct_pwt_net_amt) netAmt FROM st_rep_dg_bo db INNER JOIN st_lms_organization_master om ON om.organization_id = db.organization_id INNER JOIN st_lms_city_master city ON om.city = city.city_name WHERE db.finaldate>='",startDate,"' and db.finaldate<='",endDate,"'");
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
  
  