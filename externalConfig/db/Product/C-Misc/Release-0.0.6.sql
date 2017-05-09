--liquibase formatted sql

--changeset BaseSPRMS:2 endDelimiter:#


#

CREATE      PROCEDURE `getScratchSaleDetailWithBo`(startDate date,endDate date,agentOrgId int)
BEGIN
set @saleQry=concat("select sum(net_sale_amt) net_sale_amt,sum(sale_books_mrp) sale_books_mrp , sum(net_books_returned_amt) net_books_returned_amt , sum(return_books_mrp) return_books_mrp from(select ifnull(a.cc,0) 'net_sale_amt', ifnull(a.mm,0) 'sale_books_mrp', ifnull(b.dd,0) 'net_books_returned_amt', ifnull(b.mm,0) 'return_books_mrp' from ((select sum(mrp_amt) mm, sum(net_amt) cc from st_se_bo_agent_transaction bo, st_lms_bo_transaction_master btm where btm.transaction_id=bo.transaction_id and bo.transaction_type ='SALE' and ( btm.transaction_date>='",startDate,"' and btm.transaction_date<'",endDate,"') and agent_org_id =",agentOrgId,") a , (select sum(mrp_amt) mm, sum(net_amt) dd  from st_se_bo_agent_transaction bo, st_lms_bo_transaction_master btm where btm.transaction_id=bo.transaction_id and bo.transaction_type ='SALE_RET' and ( btm.transaction_date>='",startDate,"' and btm.transaction_date<'",endDate,"') and agent_org_id =",agentOrgId,") b) union all select sum(sale_book_net) 'net_sale_amt' , sum(sale_book_mrp) 'sale_books_mrp' , sum(ref_net_amt) 'net_books_returned_amt',sum(ref_sale_mrp) 'return_books_mrp' from st_rep_se_agent where finaldate>='",startDate,"' and finaldate<'",endDate,"' and organization_id=",agentOrgId,")agt");
prepare stmt from @saleQry;
execute stmt;
deallocate prepare stmt;
    END
#

CREATE      PROCEDURE `ics`()
BEGIN
  DECLARE done INT DEFAULT 0;
  DECLARE tid INT;
  DECLARE mainQry BLOB;
  DECLARE result VARCHAR(50);
  DECLARE qryResult VARCHAR(50);
  DECLARE errMsg VARCHAR(200);
  DECLARE isCritical VARCHAR(50);
  
  DECLARE qryCur CURSOR FOR select id,main_query,query_result,error_msg,is_critical from st_ics_query_master where query_status='ACTIVE';
  DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;
  OPEN qryCur;
  read_loop: LOOP
    FETCH qryCur INTO tid,mainQry,result,errMsg,isCritical;
    IF done THEN
      LEAVE read_loop;
    END IF;
  
     set @qry=concat("set @qryResult:=ifnull((",cast(mainQry as char(24000)),"),'NULL')");
  
     PREPARE stmt FROM @qry;
     EXECUTE stmt;
     DEALLOCATE PREPARE stmt;
     set qryResult=@qryResult;
     set @isSuccess=0;
	if qryResult!=result then 
	    set @isSuccess=1;
	end if;
   
insert into st_ics_daily_query_status(query_id, expected_result,actual_result,ics_run_date,is_success) values(tid, result,qryResult,now(),@isSuccess);
  END LOOP;
  CLOSE qryCur;
set @completeResult:=(select if(ifnull(sum(qs.is_success),0)=0,'true','false') from st_ics_query_master qm inner join st_ics_daily_query_status qs on qm.id=qs.query_id where qm.query_status='ACTIVE' and qm.is_critical='YES' and date(ics_run_date)=date(now()));
insert into st_ics_daily_status 
	(ics_datetime, is_success) values(now(),@completeResult);
END
#

CREATE      PROCEDURE `ICSCheckAmountUpdateLedgerAtAgent`(queryId INT)
BEGIN
	DECLARE result VARCHAR(20) DEFAULT NULL;
	DECLARE done INT DEFAULT 0;
	DECLARE gameId INT DEFAULT 0;
	DECLARE gameCur CURSOR FOR SELECT game_id FROM st_dg_game_master;
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;
	SET @isDG:=(SELECT IF(STATUS='ACTIVE',TRUE,FALSE) FROM st_lms_service_master WHERE service_code='DG');
	IF(@isDG) THEN
		SET result=0;
		OPEN gameCur;
		read_loop: LOOP
		FETCH gameCur INTO gameId;
		IF done THEN
		    LEAVE read_loop;
		END IF;
		SET @mainQry = CONCAT("set @result:=(SELECT IFNULL((SUM(pp)+SUM(qq)+SUM(rr)),0) AS Result FROM (SELECT a_mrp-b_mrp pp, a_retailer_comm-b_retailer_comm qq, a_net_amt-b_net_amt rr FROM (SELECT a.retailer_org_id, SUM(a.mrp_amt)a_mrp, SUM(a.retailer_comm)a_retailer_comm, SUM(a.net_amt)a_net_amt FROM st_dg_ret_sale_",gameId," a WHERE a.claim_status='DONE_CLAIM' GROUP BY a.retailer_org_id)aa INNER JOIN (SELECT b.retailer_org_id, SUM(b.mrp_amt)b_mrp, SUM(b.retailer_comm)b_retailer_comm, SUM(b.net_amt)b_net_amt FROM st_dg_agt_sale b where game_id=",gameId," GROUP BY b.retailer_org_id)bb ON aa.retailer_org_id=bb.retailer_org_id)mainTlb);");
		PREPARE stmt FROM @mainQry;
		EXECUTE stmt;
		DEALLOCATE PREPARE stmt;
		SET result:=result+@result;
		SET @mainQry = CONCAT("set @result:=(SELECT IFNULL((SUM(pp)+SUM(qq)+SUM(rr)),0) AS Result FROM (SELECT a_mrp-b_mrp pp, a_retailer_comm-b_retailer_comm qq, a_net_amt-b_net_amt rr FROM (SELECT a.retailer_org_id, SUM(a.mrp_amt)a_mrp, SUM(a.retailer_comm)a_retailer_comm, SUM(a.net_amt)a_net_amt FROM st_dg_ret_sale_refund_",gameId," a WHERE a.claim_status='DONE_CLAIM' GROUP BY a.retailer_org_id)aa INNER JOIN (SELECT b.retailer_org_id, SUM(b.mrp_amt)b_mrp, SUM(b.retailer_comm)b_retailer_comm, SUM(b.net_amt)b_net_amt FROM st_dg_agt_sale_refund b  where game_id=",gameId," GROUP BY b.retailer_org_id)bb ON aa.retailer_org_id=bb.retailer_org_id)mainTlb);");
		PREPARE stmt FROM @mainQry;
		EXECUTE stmt;
		DEALLOCATE PREPARE stmt;
		SET result:=result+@result;
		SET @mainQry = CONCAT("set @result:=(SELECT IFNULL((SUM(pp)+SUM(qq)+SUM(rr)),0) AS Result FROM (SELECT a_mrp-b_mrp pp, a_retailer_comm-b_retailer_comm qq, a_net_amt-b_net_amt rr FROM (SELECT retailer_org_id, SUM(a_mrp)a_mrp, SUM(a_retailer_comm)a_retailer_comm, SUM(a_net_amt)a_net_amt FROM (SELECT a.retailer_org_id, SUM(a.pwt_amt)a_mrp, SUM(a.retailer_claim_comm)a_retailer_comm, SUM(a.agt_claim_comm)a_net_amt FROM st_dg_ret_pwt_",gameId," a WHERE a.status='DONE_CLAIM' GROUP BY retailer_org_id UNION ALL SELECT ap.retailer_org_id, SUM(ap.pwt_amt)a_mrp, SUM(ap.retailer_claim_comm)a_retailer_comm, SUM(ap.agt_claim_comm)a_net_amt FROM st_dg_ret_direct_plr_pwt ap WHERE ap.pwt_claim_status='DONE_CLAIM' GROUP BY retailer_org_id)ff GROUP BY retailer_org_id)aa INNER JOIN (SELECT b.retailer_org_id, SUM(b.pwt_amt)b_mrp, SUM(b.comm_amt)b_retailer_comm, SUM(b.agt_claim_comm)b_net_amt FROM st_dg_agt_pwt b  where game_id=",gameId," GROUP BY b.retailer_org_id)bb ON aa.retailer_org_id=bb.retailer_org_id)mainTlb);");
		PREPARE stmt FROM @mainQry;
		EXECUTE stmt;
		DEALLOCATE PREPARE stmt;
		SET result:=result+@result;
		END LOOP;
		CLOSE gameCur;
	END IF;
	
	CALL ICSInsertSPResult(queryId, result);
    END      
#
CREATE      PROCEDURE `ICSCheckAmountUpdateLedgerAtBo`(queryId INT)
BEGIN
DECLARE result varchar(20) DEFAULT NULL;
	DECLARE done INT DEFAULT 0;
SET result=0;
SET @isDG:=(SELECT IF(STATUS='ACTIVE',TRUE,FALSE) FROM st_lms_service_master WHERE service_code='DG');
	IF(@isDG) THEN
SET result=(select IFNULL((sum(pp)+sum(qq)+sum(rr)),0) as Result from(select a_mrp-b_mrp pp,a_agt_comm-b_agt_comm qq,a_net_amt-b_net_amt rr from (select a.agent_org_id,sum(a.mrp_amt)a_mrp,sum(a.agent_comm)a_agt_comm,sum(a.net_amt)a_net_amt from st_dg_agt_sale a where a.claim_status='DONE_CLAIM' group by a.agent_org_id)aa inner join (select b.agent_org_id,sum(b.mrp_amt)b_mrp,sum(b.agent_comm)b_agt_comm,sum(b.net_amt)b_net_amt from st_dg_bo_sale b group by b.agent_org_id)bb on aa.agent_org_id=bb.agent_org_id)mainTlb
);
select result;
SET result=result+(select IFNULL((sum(pp)+sum(qq)+sum(rr)),0) as Result from(select a_mrp-b_mrp pp,a_agt_comm-b_agt_comm qq,a_net_amt-b_net_amt rr from (select a.agent_org_id,sum(a.mrp_amt)a_mrp,sum(a.agent_comm)a_agt_comm,sum(a.net_amt)a_net_amt from st_dg_agt_sale_refund a where a.claim_status='DONE_CLAIM' group by a.agent_org_id)aa inner join (select b.agent_org_id,sum(b.mrp_amt)b_mrp,sum(b.agent_comm)b_agt_comm,sum(b.net_amt)b_net_amt from st_dg_bo_sale_refund b group by b.agent_org_id)bb on aa.agent_org_id=bb.agent_org_id)mainTlb
);
SET result=result+(select IFNULL((sum(pp)+sum(qq)+sum(rr)),0) as Result from(select a_mrp-b_mrp pp,a_agt_comm-b_agt_comm qq,a_net_amt-b_net_amt rr from (select agent_org_id,sum(a_mrp)a_mrp,sum(a_agt_comm)a_agt_comm,sum(a_net_amt)a_net_amt from(select a.agent_org_id,sum(a.pwt_amt)a_mrp,sum(a.agt_claim_comm)a_agt_comm,sum(a.net_amt)a_net_amt from st_dg_agt_pwt a where a.status='DONE_CLAIM'  group by agent_org_id union all select ap.agent_org_id,sum(ap.pwt_amt)a_mrp,sum(ap.agt_claim_comm)a_agt_comm,sum(ap.net_amt)a_net_amt from st_dg_agt_direct_plr_pwt ap where ap.pwt_claim_status='DONE_CLAIM'  group by agent_org_id)ff group by agent_org_id)aa inner join (select b.agent_org_id,sum(b.pwt_amt)b_mrp,sum(b.comm_amt)b_agt_comm,sum(b.net_amt)b_net_amt from st_dg_bo_pwt b group by b.agent_org_id)bb on aa.agent_org_id=bb.agent_org_id)mainTlb);
END IF;
	select result;
	CALL ICSInsertSPResult(queryId, result);
    END      
#
CREATE      PROCEDURE `ICSCheckCBForAgent`(queryId INT)
BEGIN
	DECLARE result VARCHAR(20) DEFAULT NULL;
	DECLARE done INT DEFAULT 0;
	DECLARE gameId INT DEFAULT 0;
	DECLARE gameCur CURSOR FOR SELECT game_id FROM st_dg_game_master;
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;
	SET @mainQry = CONCAT("SET @result := (select count(1) as Result from(select agent_id,sum(result) net from(select parent_id agent_id,result from st_lms_organization_master om inner join ( ");
	OPEN gameCur;
	read_loop: LOOP
		FETCH gameCur INTO gameId;
		IF done THEN
			LEAVE read_loop;
		END IF;
		SET @mainQry = CONCAT(@mainQry, "SELECT retailer_org_id,-ifnull(SUM(ifnull(agent_net_amt,0.0)),0.0) AS result FROM st_dg_ret_sale_",gameId," WHERE claim_status='CLAIM_BAL' GROUP BY retailer_org_id UNION ALL SELECT retailer_org_id,ifnull(SUM(ifnull(pwt_amt,0.0)),0.0) AS pwt FROM st_dg_ret_pwt_",gameId," WHERE STATUS='CLAIM_BAL' GROUP BY retailer_org_id  UNION ALL Select retailer_org_id,ifnull(SUM(ifnull(agent_net_amt,0.0)),0.0) AS ref FROM st_dg_ret_sale_refund_",gameId," WHERE claim_status='CLAIM_BAL' GROUP BY retailer_org_id UNION ALL ");
	END LOOP;
	CLOSE gameCur;
SET @mainQry=TRIM(TRAILING ' UNION ALL ' FROM @mainQry);
	SET @mainQry = CONCAT(@mainQry, ")main on main.retailer_org_id=om.organization_id UNION ALL Select agent_org_id,ifnull(sum(pwt_amt),0.0) result from st_dg_agt_direct_plr_pwt where pwt_claim_status !='DONE_CLM' group by agent_org_id Union All SELECT organization_id,claimable_bal result FROM st_lms_organization_master WHERE organization_type='AGENT')allTlb group by agent_id)final where net !=0);");
select @mainQry;	
PREPARE stmt FROM @mainQry;
	EXECUTE stmt;
	DEALLOCATE PREPARE stmt;
	CALL ICSInsertSPResult(queryId, @result);
    END      
#
CREATE      PROCEDURE `ICSCheckCBForRetailer`(queryId INT)
BEGIN
    DECLARE result VARCHAR(20) DEFAULT NULL;
    DECLARE done INT DEFAULT 0;
    DECLARE gameId INT DEFAULT 0;
    DECLARE gameCur CURSOR FOR SELECT game_id FROM st_dg_game_master;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;
    SET @mainQry = CONCAT("SET @result := (select count(1) as Result from(select retailer_org_id,sum(sale) net from( ");
    OPEN gameCur;
    read_loop: LOOP
        FETCH gameCur INTO gameId;
        IF done THEN
            LEAVE read_loop;
        END IF;
        SET @mainQry = CONCAT(@mainQry, "SELECT retailer_org_id,-SUM(net_amt) AS sale FROM st_dg_ret_sale_",gameId," WHERE claim_status='CLAIM_BAL' GROUP BY retailer_org_id union all
SELECT retailer_org_id,SUM(net_amt) AS ref FROM st_dg_ret_sale_refund_",gameId," WHERE claim_status='CLAIM_BAL' GROUP BY retailer_org_id union all
SELECT retailer_org_id,SUM(pwt_amt) AS pwt FROM st_dg_ret_pwt_",gameId," WHERE STATUS='CLAIM_BAL' GROUP BY retailer_org_id Union all ");
    END LOOP;
    CLOSE gameCur;
    SET @mainQry = CONCAT(@mainQry, " SELECT organization_id,claimable_bal FROM st_lms_organization_master WHERE organization_type='RETAILER')main group by retailer_org_id)allTlb where net !=0);");
    select @mainQry;
    PREPARE stmt FROM @mainQry;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;
     
    CALL ICSInsertSPResult(queryId, @result);
    END      
#
CREATE      PROCEDURE `ICSCheckCCAForAgent`(queryId INT)
BEGIN
	DECLARE result VARCHAR(20) DEFAULT NULL;
	DECLARE done INT DEFAULT 0;
	DECLARE gameId INT DEFAULT 0;
	DECLARE gameCur CURSOR FOR SELECT game_id FROM st_dg_game_master;
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;
	SET @mainQry = CONCAT("SET @result := (select count(1) as Result from(select agent_id,sum(result) net from(select parent_id agent_id,result from st_lms_organization_master om inner join ( ");
	OPEN gameCur;
	read_loop: LOOP
		FETCH gameCur INTO gameId;
		IF done THEN
			LEAVE read_loop;
		END IF;
		SET @mainQry = CONCAT(@mainQry, "SELECT retailer_org_id,-ifnull(SUM(ifnull(agent_net_amt,0.0)),0.0) AS result FROM st_dg_ret_sale_",gameId," WHERE claim_status='DONE_CLAIM' GROUP BY retailer_org_id UNION ALL SELECT retailer_org_id,ifnull(SUM(ifnull(pwt_amt,0.0)),0.0) AS pwt FROM st_dg_ret_pwt_",gameId," WHERE STATUS='DONE_CLAIM' GROUP BY retailer_org_id  UNION ALL Select retailer_org_id,ifnull(SUM(ifnull(agent_net_amt,0.0)),0.0) AS ref FROM st_dg_ret_sale_refund_",gameId," WHERE claim_status='DONE_CLAIM' GROUP BY retailer_org_id UNION ALL ");
	END LOOP;
	CLOSE gameCur;
SET @mainQry=TRIM(TRAILING ' UNION ALL ' FROM @mainQry);
	SET @mainQry = CONCAT(@mainQry, ")main on main.retailer_org_id=om.organization_id UNION ALL Select agent_org_id,ifnull(sum(pwt_amt),0.0) result from st_dg_agt_direct_plr_pwt group by agent_org_id Union All SELECT agent_org_id,IFNULL(SUM(amount),0.0) result FROM st_lms_bo_cash_transaction  group by agent_org_id UNION ALL SELECT agent_org_id,IFNULL(SUM(amount),0.0) result FROM st_lms_bo_credit_note  group by agent_org_id UNION ALL SELECT agent_org_id,-IFNULL(SUM(amount),0.0) result FROM st_lms_bo_debit_note  group by agent_org_id UNION ALL SELECT agent_org_id,IFNULL(SUM(cheque_amt),0.0) result FROM st_lms_bo_sale_chq  group by agent_org_id UNION ALL SELECT agent_org_id,IFNULL(SUM(amount),0.0) result FROM st_lms_bo_bank_deposit_transaction  group by agent_org_id UNION ALL select agent_org_id,sum(cash_amt+credit_note+cheque_amt+bank_deposit-debit_note) from st_rep_bo_payments  group by agent_org_id union all select organization_id,sum(ref_net_amt+pwt_net_amt-sale_net+direct_pwt_net_amt) from st_rep_dg_agent  group by organization_id UNION ALL  SELECT organization_id,current_credit_amt  FROM st_lms_organization_master WHERE organization_type='AGENT')allTlb group by agent_id)final where net !=0);");
	SELECT @mainQry;
	PREPARE stmt FROM @mainQry;
	EXECUTE stmt;
	DEALLOCATE PREPARE stmt;
	CALL ICSInsertSPResult(queryId, @result);
    END      
#
CREATE      PROCEDURE `ICSCheckCCAForRetailer`(queryId INT)
BEGIN
	 DECLARE result VARCHAR(20) DEFAULT NULL;
    DECLARE done INT DEFAULT 0;
    DECLARE gameId INT DEFAULT 0;
    DECLARE gameCur CURSOR FOR SELECT game_id FROM st_dg_game_master;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;
    SET @mainQry = CONCAT("SET @result := (select count(1) as Result from(select retailer_org_id,sum(sale) net from( ");
    OPEN gameCur;
    read_loop: LOOP
        FETCH gameCur INTO gameId;
        IF done THEN
            LEAVE read_loop;
        END IF;
        SET @mainQry = CONCAT(@mainQry, "SELECT retailer_org_id,-SUM(net_amt) AS sale FROM st_dg_ret_sale_",gameId," WHERE claim_status='DONE_CLAIM' GROUP BY retailer_org_id union all
SELECT retailer_org_id,SUM(net_amt) AS ref FROM st_dg_ret_sale_refund_",gameId," WHERE claim_status='DONE_CLAIM' GROUP BY retailer_org_id union all
SELECT retailer_org_id,SUM(pwt_amt) AS pwt FROM st_dg_ret_pwt_",gameId," WHERE STATUS='DONE_CLAIM' GROUP BY retailer_org_id Union all ");
    END LOOP;
    CLOSE gameCur;
	SET @mainQry = CONCAT(@mainQry, "SELECT retailer_org_id,IFNULL(SUM(amount),0.0) result FROM st_lms_agent_cash_transaction group by retailer_org_id UNION ALL SELECT retailer_org_id,IFNULL(SUM(amount),0.0) result FROM st_lms_agent_credit_note  group by retailer_org_id UNION ALL SELECT retailer_org_id,-IFNULL(SUM(amount),0.0) result FROM st_lms_agent_debit_note group by retailer_org_id  UNION ALL SELECT retailer_org_id,IFNULL(SUM(cheque_amt),0.0) result FROM st_lms_agent_sale_chq  group by retailer_org_id UNION ALL select retailer_org_id,sum(cash_amt+credit_note+cheque_amt-debit_note) from st_rep_agent_payments  group by retailer_org_id union all select organization_id,sum(ref_net_amt+pwt_net_amt-sale_net) from st_rep_dg_retailer group by organization_id UNION ALL   SELECT organization_id,current_credit_amt FROM st_lms_organization_master WHERE organization_type='RETAILER')main group by retailer_org_id)allTlb where net !=0);");
	SELECT @mainQry;
	PREPARE stmt FROM @mainQry;
	EXECUTE stmt;
	DEALLOCATE PREPARE stmt;
	CALL ICSInsertSPResult(queryId, @result);
    END      
#
CREATE      PROCEDURE `ICSCheckTransactionIdUpdateLedgerAtAgent`(queryId INT)
BEGIN
	DECLARE result VARCHAR(20) DEFAULT NULL;
	DECLARE done INT DEFAULT 0;
	DECLARE gameId INT DEFAULT 0;
	DECLARE gameCur CURSOR FOR SELECT game_id FROM st_dg_game_master;
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;
	SET @isDG:=(SELECT IF(STATUS='ACTIVE',TRUE,FALSE) FROM st_lms_service_master WHERE service_code='DG');
	IF(@isDG) THEN
		SET result=0;
		OPEN gameCur;
		read_loop: LOOP
		FETCH gameCur INTO gameId;
		IF done THEN
		    LEAVE read_loop;
		END IF;
		SET @mainQry = CONCAT("set @result:=(SELECT aa+bb+cc+dd AS Result FROM ( SELECT(SELECT COUNT(1) FROM st_dg_ret_sale_",gameId," a LEFT JOIN st_dg_agt_sale b ON a.agent_ref_transaction_id=b.transaction_id AND a.retailer_org_id=b.retailer_org_id AND b.game_id=",gameId," WHERE a.claim_status='DONE_CLAIM' AND b.transaction_id IS NULL)aa, (SELECT COUNT(1) FROM st_dg_ret_sale_",gameId," a RIGHT JOIN st_dg_agt_sale b ON a.agent_ref_transaction_id=b.transaction_id AND a.retailer_org_id=b.retailer_org_id AND b.game_id=",gameId," WHERE a.claim_status='DONE_CLAIM' AND a.transaction_id IS NULL)bb, (SELECT COUNT(1) FROM st_dg_ret_sale_refund_",gameId," a LEFT JOIN st_dg_agt_sale_refund b ON a.agent_ref_transaction_id=b.transaction_id AND a.retailer_org_id=b.retailer_org_id AND b.game_id=",gameId," WHERE a.claim_status='DONE_CLAIM' AND b.transaction_id IS NULL)cc, (SELECT COUNT(1) FROM st_dg_ret_sale_refund_",gameId," a RIGHT JOIN st_dg_agt_sale_refund b ON a.agent_ref_transaction_id=b.transaction_id AND a.retailer_org_id=b.retailer_org_id AND b.game_id=",gameId," WHERE a.claim_status='DONE_CLAIM' AND a.transaction_id IS NULL)dd)main);");
		PREPARE stmt FROM @mainQry;
		EXECUTE stmt;
		DEALLOCATE PREPARE stmt;
		SET result:=result+@result;
	
		END LOOP;
		CLOSE gameCur;
	END IF;
	
	CALL ICSInsertSPResult(queryId, result);
    END      
#
CREATE      PROCEDURE `ICSCheckTransactionIdUpdateLedgerAtBo`(queryId INT)
BEGIN
	DECLARE result VARCHAR(20) DEFAULT NULL;
	DECLARE done INT DEFAULT 0;
	
SET @isDG:=(SELECT IF(STATUS='ACTIVE',TRUE,FALSE) FROM st_lms_service_master WHERE service_code='DG');
	IF(@isDG) THEN
SET result=(select aa+bb+cc+dd as Result from(select (select count(1) from st_dg_agt_sale a left join st_dg_bo_sale b on a.bo_ref_transaction_id=b.transaction_id and a.agent_org_id=b.agent_org_id  where a.claim_status='DONE_CLAIM' and b.transaction_id is NULL)aa,(select count(1) from st_dg_agt_sale a RIGHT join st_dg_bo_sale b on a.bo_ref_transaction_id=b.transaction_id and a.agent_org_id=b.agent_org_id  where a.claim_status='DONE_CLAIM' and a.transaction_id is NULL)bb,(select count(1) from st_dg_agt_sale_refund a left join st_dg_bo_sale_refund b on a.bo_ref_transaction_id=b.transaction_id and a.agent_org_id=b.agent_org_id  where a.claim_status='DONE_CLAIM' and b.transaction_id is NULL)cc,(select count(1) from st_dg_agt_sale_refund a RIGHT join st_dg_bo_sale_refund b on a.bo_ref_transaction_id=b.transaction_id   and a.agent_org_id=b.agent_org_id where a.claim_status='DONE_CLAIM' and a.transaction_id is NULL)dd)main);
	END IF;
	select result;
	CALL ICSInsertSPResult(queryId, result);
    END      
#
CREATE      PROCEDURE `ICSInsertSPResult`(queryId int, actualResult BIGint)
BEGIN
	DECLARE expectedResult BIGint DEFAULT 0;
	SET @expectedResult := (SELECT query_result FROM st_ics_query_master WHERE query_id=queryId);
	SET @isSuccess=CONCAT("YES");
	IF @expectedResult!=actualResult THEN 
		SET @isSuccess=CONCAT("NO");
	END IF;
	INSERT INTO st_ics_daily_query_status (query_id, expected_result, actual_result, ics_run_date, is_success) VALUES (queryId, @expectedResult, actualResult, NOW(), @isSuccess);
END      
#
CREATE      PROCEDURE `ICSOrphanValuesInAgentTransactionMaster`(queryId INT)
BEGIN
	DECLARE result VARCHAR(20) DEFAULT NULL;
	DECLARE done INT DEFAULT 0;
	
SET @isMgmt:=(SELECT IF(STATUS='ACTIVE',TRUE,FALSE) FROM st_lms_service_master WHERE service_code='MGMT');	
IF(@isMgmt) THEN
	SET result=(select qa+ag+bh+ci+dj as result  from (select (main-aa-bb-cc-dd) as qa, aa-gg ag,bb-hh bh,cc-ii ci, dd-jj dj from (select(select count(1) from st_lms_agent_transaction_master t where t.transaction_type in('CHEQUE','CHQ_BOUNCE','DR_NOTE','CASH','CR_NOTE_CASH','DR_NOTE_CASH')) main,(select count(1) from st_lms_agent_sale_chq a inner join st_lms_agent_transaction_master t on a.transaction_id=t.transaction_id where t.transaction_type in('CHEQUE','CHQ_BOUNCE'))aa,(select count(1) from st_lms_agent_debit_note a inner join st_lms_agent_transaction_master t on a.transaction_id=t.transaction_id where t.transaction_type in('DR_NOTE','DR_NOTE_CASH'))bb,(select count(1) from st_lms_agent_cash_transaction a inner join st_lms_agent_transaction_master t on a.transaction_id=t.transaction_id where t.transaction_type in('CASH'))cc,(select count(1) from st_lms_agent_credit_note a inner join st_lms_agent_transaction_master t on a.transaction_id=t.transaction_id where t.transaction_type in('CR_NOTE_CASH'))dd,(select count(1) from st_lms_agent_sale_chq)gg,(select count(1) from st_lms_agent_debit_note)hh,(select count(1) from st_lms_agent_cash_transaction)ii,(select count(1) from st_lms_agent_credit_note)jj,(select count(1) from st_lms_agent_govt_transaction)ll)alltlb)tlb);
END IF;
SET @isDG:=(SELECT IF(STATUS='ACTIVE',TRUE,FALSE) FROM st_lms_service_master WHERE service_code='DG');
	IF(@isDG) THEN
SET result=result+(select qa+ae+bf+cg+dh as result  from (select (main-aa-bb-cc-dd) as qa, aa-ee ae,bb-ff bf,cc-gg cg, dd-hh dh from (select(select count(1) from st_lms_agent_transaction_master t where t.transaction_type in('DG_SALE','DG_PWT_AUTO','DG_REFUND_CANCEL','DG_REFUND_FAILED','DG_PWT_PLR')) main,(select count(1) from st_dg_agt_sale a inner join st_lms_agent_transaction_master t on a.transaction_id=t.transaction_id where t.transaction_type in('DG_SALE'))aa,(select count(1) from st_dg_agt_sale_refund a inner join st_lms_agent_transaction_master t on a.transaction_id=t.transaction_id where t.transaction_type in('DG_REFUND_CANCEL','DG_REFUND_FAILED'))bb,(select count(1) from st_dg_agt_direct_plr_pwt a inner join st_lms_agent_transaction_master t on a.transaction_id=t.transaction_id where t.transaction_type in('DG_PWT_PLR'))cc,(select count(1) from st_dg_agt_pwt a inner join st_lms_agent_transaction_master t on a.transaction_id=t.transaction_id where t.transaction_type in('DG_PWT_AUTO'))dd,(select count(1) from st_dg_agt_sale)ee,(select count(1) from st_dg_agt_sale_refund)ff,(select count(1) from st_dg_agt_direct_plr_pwt)gg,(select count(1) from st_dg_agt_pwt)hh)alltlb)tlb);
	END IF;
	select result;
	CALL ICSInsertSPResult(queryId, result);
    END      
#
CREATE      PROCEDURE `ICSOrphanValuesInBoTransactionMaster`(queryId INT)
BEGIN
	DECLARE result VARCHAR(20) DEFAULT NULL;
	DECLARE done INT DEFAULT 0;
	
SET @isMgmt:=(SELECT IF(STATUS='ACTIVE',TRUE,FALSE) FROM st_lms_service_master WHERE service_code='MGMT');	
IF(@isMgmt) THEN
	SET result=(select qa+ag+bh+ci+dj+ek+fl as Result from (select (main-aa-bb-cc-dd-ee-ff) as qa, aa-gg ag,bb-hh bh,cc-ii ci, dd-jj dj, ee-kk ek,ff-ll fl from (select(select count(1) from st_lms_bo_transaction_master t where t.transaction_type in('CHEQUE','CHQ_BOUNCE','DR_NOTE','VAT','CASH','CR_NOTE_CASH','DR_NOTE_CASH','GOVT_COMM','BANK_DEPOSIT')) main,(select count(1) from st_lms_bo_sale_chq a inner join st_lms_bo_transaction_master t on a.transaction_id=t.transaction_id where t.transaction_type in('CHEQUE','CHQ_BOUNCE'))aa,(select count(1) from st_lms_bo_debit_note a inner join st_lms_bo_transaction_master t on a.transaction_id=t.transaction_id where t.transaction_type in('DR_NOTE','DR_NOTE_CASH'))bb,(select count(1) from st_lms_bo_cash_transaction a inner join st_lms_bo_transaction_master t on a.transaction_id=t.transaction_id where t.transaction_type in('CASH'))cc,(select count(1) from st_lms_bo_credit_note a inner join st_lms_bo_transaction_master t on a.transaction_id=t.transaction_id where t.transaction_type in('CR_NOTE_CASH'))dd,(select count(1) from st_lms_bo_bank_deposit_transaction a inner join st_lms_bo_transaction_master t on a.transaction_id=t.transaction_id where t.transaction_type in('BANK_DEPOSIT'))ee,(select count(1) from st_lms_bo_govt_transaction a inner join st_lms_bo_transaction_master t on a.transaction_id=t.transaction_id where t.transaction_type in('GOVT_COMM','VAT'))ff,(select count(1) from st_lms_bo_sale_chq)gg,(select count(1) from st_lms_bo_debit_note)hh,(select count(1) from st_lms_bo_cash_transaction)ii,(select count(1) from st_lms_bo_credit_note)jj,(select count(1) from st_lms_bo_bank_deposit_transaction)kk,(select count(1) from st_lms_bo_govt_transaction)ll)alltlb)tlb);
END IF;
SET @isDG:=(SELECT IF(STATUS='ACTIVE',TRUE,FALSE) FROM st_lms_service_master WHERE service_code='DG');
	IF(@isDG) THEN
SET result=result+(select qa+ae+bf+cg+dh as result  from (select (main-aa-bb-cc-dd) as qa, aa-ee ae,bb-ff bf,cc-gg cg, dd-hh dh from (select(select count(1) from st_lms_bo_transaction_master t where t.transaction_type in('DG_SALE','DG_PWT_AUTO','DG_REFUND_CANCEL','DG_REFUND_FAILED','DG_PWT_PLR')) main,(select count(1) from st_dg_bo_sale a inner join st_lms_bo_transaction_master t on a.transaction_id=t.transaction_id where t.transaction_type in('DG_SALE'))aa,(select count(1) from st_dg_bo_sale_refund a inner join st_lms_bo_transaction_master t on a.transaction_id=t.transaction_id where t.transaction_type in('DG_REFUND_CANCEL','DG_REFUND_FAILED'))bb,(select count(1) from st_dg_bo_direct_plr_pwt a inner join st_lms_bo_transaction_master t on a.transaction_id=t.transaction_id where t.transaction_type in('DG_PWT_PLR'))cc,(select count(1) from st_dg_bo_pwt a inner join st_lms_bo_transaction_master t on a.transaction_id=t.transaction_id where t.transaction_type in('DG_PWT_AUTO'))dd,(select count(1) from st_dg_bo_sale)ee,(select count(1) from st_dg_bo_sale_refund)ff,(select count(1) from st_dg_bo_direct_plr_pwt)gg,(select count(1) from st_dg_bo_pwt)hh)alltlb)tlb );
	END IF;
	select result;
	CALL ICSInsertSPResult(queryId, result);
    END      
#
CREATE      PROCEDURE `ICSOrphanValuesInRetailerTransactionMaster`(queryId INT)
BEGIN
	DECLARE result1 INT DEFAULT 0;
	DECLARE done INT DEFAULT 0;
	DECLARE gameId INT DEFAULT 0;
	DECLARE gameCur CURSOR FOR SELECT game_id FROM st_dg_game_master;
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;
	set @mainQry="";
	OPEN gameCur;
	read_loop: LOOP
		FETCH gameCur INTO gameId;
		IF done THEN
			LEAVE read_loop;
		END IF;
		
set @mainQry=concat("SET @result := (select aa+bb+cc+dd+ee+ff as Result from(select (SELECT COUNT(1) FROM st_lms_retailer_transaction_master rtm LEFT JOIN st_dg_ret_sale_",gameId," s1 ON rtm.transaction_id = s1.transaction_id where  rtm.game_id = ",gameId," and transaction_type in('DG_SALE','DG_SALE_OFFLINE') and s1.game_id is NULL)aa, (SELECT COUNT(1) FROM st_lms_retailer_transaction_master rtm RIGHT JOIN st_dg_ret_sale_",gameId," s1 ON rtm.transaction_id = s1.transaction_id where  s1.game_id = ",gameId," and transaction_type in('DG_SALE','DG_SALE_OFFLINE') and rtm.game_id is NULL)bb,(SELECT COUNT(1) FROM st_lms_retailer_transaction_master rtm LEFT JOIN st_dg_ret_sale_refund_",gameId," s1 ON rtm.transaction_id = s1.transaction_id where  rtm.game_id = ",gameId," and transaction_type in('DG_REFUND_CANCEL','DG_REFUND_FAILED') and s1.game_id is NULL)cc, (SELECT COUNT(1) FROM st_lms_retailer_transaction_master rtm RIGHT JOIN st_dg_ret_sale_refund_",gameId," s1 ON rtm.transaction_id = s1.transaction_id where  s1.game_id = ",gameId," and transaction_type in('DG_REFUND_CANCEL','DG_REFUND_FAILED') and rtm.game_id is NULL)dd,(SELECT COUNT(1) FROM st_lms_retailer_transaction_master rtm LEFT JOIN st_dg_ret_pwt_",gameId," s1 ON rtm.transaction_id = s1.transaction_id where  rtm.game_id = ",gameId," and transaction_type in('DG_PWT_AUTO','DG_PWT_PLR','DG_PWT') and s1.game_id is NULL)ee, (SELECT COUNT(1) FROM st_lms_retailer_transaction_master rtm RIGHT JOIN st_dg_ret_pwt_",gameId," s1 ON rtm.transaction_id = s1.transaction_id where  s1.game_id = ",gameId," and transaction_type in('DG_PWT_AUTO','DG_PWT_PLR','DG_PWT') and rtm.game_id is NULL)ff)main
)");
select @mainQry;
	PREPARE stmt FROM @mainQry;
	  EXECUTE stmt;
	  DEALLOCATE PREPARE stmt;
SET result1=result1+@result;
select result1;
	END LOOP;
	CLOSE gameCur;
select result1;
	CALL ICSInsertSPResult(queryId, result1);
    END      
#
CREATE      PROCEDURE `ICSRun`()
BEGIN
	DECLARE queryId int DEFAULT 0;
	DECLARE queryTitle varchar(300) DEFAULT NULL;
	DECLARE mainQuery blob DEFAULT null;
	declare isSP char(9) default null;
	DECLARE queryResult varchar(30) DEFAULT NULL;
	DECLARE queryDescription varchar(300) DEFAULT NULL;
	DECLARE relatedTo char(12) DEFAULT NULL;
	DECLARE tierType char(24) DEFAULT NULL;
	DECLARE errorMessage varchar(3000) DEFAULT NULL;
	DECLARE isCritical char(9) DEFAULT NULL;
	DECLARE queryStatus char(24) DEFAULT NULL;
	DECLARE done INT DEFAULT 0;
	DECLARE queryActualResult int default 0;
	DECLARE queryCur CURSOR FOR SELECT query_id, query_title, main_query, is_sp, query_result, qurey_description, related_to, tier_type, error_msg, is_critical, query_status FROM st_ics_query_master WHERE query_status='ACTIVE' AND related_to NOT IN (SELECT service_code FROM st_lms_service_master WHERE STATUS='INACTIVE');
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;
	OPEN queryCur;
	read_loop: LOOP
		FETCH queryCur INTO queryId, queryTitle, mainQuery, isSP, queryResult, queryDescription, relatedTo, tierType, errorMessage, isCritical, queryStatus;
		IF done THEN
			LEAVE read_loop;
		END IF;
		if isSP='YES' then
			SET @icsQuery = CONCAT('CALL ', CAST(mainQuery AS CHAR(24000)),'(',queryId,')');
			PREPARE pstmt FROM @icsQuery;
			EXECUTE pstmt;
			DROP PREPARE pstmt;
		else
			SET @icsQuery = CONCAT('SET @queryActualResult := IFNULL((', CAST(mainQuery AS CHAR(24000)),"), '0')");
			PREPARE pstmt FROM @icsQuery;
			EXECUTE pstmt;
			DROP PREPARE pstmt;
			CALL ICSInsertSPResult(queryId, @queryActualResult);
		end if;
	END LOOP;
	CLOSE queryCur;
	INSERT INTO st_ics_daily_status (ics_datetime, is_success) VALUES (NOW(), (SELECT IF(COUNT(*)>0,'NO','YES') AS is_success FROM st_ics_daily_query_status WHERE is_success='NO' AND DATE(ics_run_date)=DATE(NOW())));
END      
#

