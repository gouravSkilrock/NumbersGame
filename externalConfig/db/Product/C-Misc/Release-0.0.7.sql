--liquibase formatted sql

--changeset BaseSPRMS:1 endDelimiter:#



#

 CREATE  PROCEDURE `agentPaymentsReporting`(startDate TIMESTAMP,endDate TIMESTAMP)
BEGIN
	DECLARE done INT DEFAULT 0;
	DECLARE gameId INT;
	
	SET @cashQry=CONCAT("insert into st_rep_agent_payments(retailer_org_id, parent_id,finaldate,cash_amt) select organization_id ,parent_id ,alldate , ifnull(cash,0.00) cash from (select organization_id,parent_id,alldate from st_lms_organization_master,datestore where organization_type='RETAILER') om left outer join (select retailer_org_id,date(transaction_date) tdate,sum(amount) as cash from st_lms_agent_cash_transaction cash inner join  st_lms_agent_transaction_master tm on cash.transaction_id=tm.transaction_id  where transaction_type=('CASH') and transaction_date>='",startDate,"' and transaction_date<='",endDate,"' group by date(transaction_date) ,retailer_org_id) cash on organization_id=retailer_org_id and tdate=alldate");
		PREPARE stmt FROM @cashQry;
		EXECUTE stmt;
		DEALLOCATE PREPARE stmt;
		SET @creditQry=CONCAT("update (select retailer_org_id,date(transaction_date) tdate,sum(amount) as credit from st_lms_agent_credit_note cr inner join  st_lms_agent_transaction_master tm on cr.transaction_id=tm.transaction_id  where tm.transaction_type=('CR_NOTE_CASH') and cr.transaction_type in ('CR_NOTE_CASH','CR_NOTE') and transaction_date>='",startDate,"' and transaction_date<='",endDate,"' group by date(transaction_date),retailer_org_id) credit,st_rep_agent_payments pay set credit_note=credit where credit.retailer_org_id=pay.retailer_org_id and pay.finaldate=credit.tdate");
		PREPARE stmt FROM @creditQry;
		EXECUTE stmt;
		DEALLOCATE PREPARE stmt;
		SET @debitQry=CONCAT("update (select retailer_org_id,date(transaction_date) tdate,sum(amount) as debit from st_lms_agent_debit_note dr inner join  st_lms_agent_transaction_master tm on dr.transaction_id=tm.transaction_id  where tm.transaction_type IN ( 'DR_NOTE_CASH', 'DR_NOTE' ) and dr.transaction_type in ('DR_NOTE_CASH','DR_NOTE') and transaction_date>='",startDate,"' and transaction_date<='",endDate,"' group by date(transaction_date) ,retailer_org_id) debit,st_rep_agent_payments pay set debit_note=debit where debit.retailer_org_id=pay.retailer_org_id and pay.finaldate=debit.tdate");
		PREPARE stmt FROM @debitQry;
		EXECUTE stmt;
		DEALLOCATE PREPARE stmt;
SET @chqQry=CONCAT("update (select retailer_org_id,date(transaction_date) tdate,sum(cheque_amt) as cheque_amt from st_lms_agent_sale_chq chq inner join  st_lms_agent_transaction_master tm on chq.transaction_id=tm.transaction_id  where tm.transaction_type in ('CHEQUE', 'CLOSED') and chq.transaction_type IN ('CHEQUE', 'CLOSED') and transaction_date>='",startDate,"' and transaction_date<='",endDate,"' group by date(transaction_date) ,retailer_org_id) chq,st_rep_agent_payments pay set pay.cheque_amt=chq.cheque_amt where chq.retailer_org_id=pay.retailer_org_id and pay.finaldate=chq.tdate");
		PREPARE stmt FROM @chqQry;
		EXECUTE stmt;
		DEALLOCATE PREPARE stmt;
SET @chqBonuceQry=CONCAT("update (select retailer_org_id,date(transaction_date) tdate,sum(cheque_amt) as cheque_amt from st_lms_agent_sale_chq chq inner join  st_lms_agent_transaction_master tm on chq.transaction_id=tm.transaction_id where tm.transaction_type=( 'CHQ_BOUNCE') and chq.transaction_type=('CHQ_BOUNCE') and transaction_date>='",startDate,"' and transaction_date<='",endDate,"' group by date(transaction_date),retailer_org_id) chq,st_rep_agent_payments pay set pay.cheque_bounce_amt=chq.cheque_amt where chq.retailer_org_id=pay.retailer_org_id and pay.finaldate=chq.tdate");
		PREPARE stmt FROM @chqBonuceQry;
		EXECUTE stmt;
		DEALLOCATE PREPARE stmt;

SET @clXclQry=CONCAT("UPDATE (SELECT temp.orgId AS orgId, SUM(temp.clAmount) AS clAmount, SUM(temp.xclAmount) AS xclAmount, parentId, orgType, tDate FROM(SELECT slcx.organization_id AS orgId, slom.parent_id AS parentId, organization_type AS orgType, DATE(date_time)AS tDate, IF(TYPE='CL',amount,0) clAmount, IF(TYPE='XCL',amount,0) xclAmount FROM st_lms_cl_xcl_update_history slcx INNER JOIN st_lms_organization_master slom ON slcx.organization_id=slom.organization_id AND organization_type='RETAILER' AND DATE(date_time)>='",startDate,"' AND DATE(date_time)<='",endDate,"') temp  GROUP BY DATE(tDate), orgId ORDER BY orgId) clXclTbl, st_rep_agent_payments pay SET cl_amt=clAmount, xcl_amt=xclAmount WHERE pay.retailer_org_id=clXclTbl.orgId AND pay.parent_id=clXclTbl.parentId AND pay.finaldate=clXclTbl.tDate");
		PREPARE stmt FROM @clXclQry;
		EXECUTE stmt;
		DEALLOCATE PREPARE stmt;
		SET @bankQry=CONCAT("UPDATE (SELECT retailer_org_id,date(transaction_date) tdate,SUM(amount) AS amount from st_lms_agent_bank_deposit_transaction bank inner join st_lms_agent_transaction_master tm on bank.transaction_id=tm.transaction_id where tm.transaction_type=('BANK_DEPOSIT') and transaction_date>='",startDate,"' and transaction_date<='",endDate,"' group by date(transaction_date),retailer_org_id) bank,st_rep_agent_payments pay set pay.bank_deposit=amount where bank.retailer_org_id=pay.retailer_org_id and pay.finaldate=bank.tdate");
		PREPARE stmt FROM @bankQry;
		EXECUTE stmt;
		DEALLOCATE PREPARE stmt;
		
		SET @insStatus=CONCAT("insert into archiving_status(job_name, status)values('AGENT_PAYMENT_REPORTING', 'DONE')");
		PREPARE stmt FROM @insStatus;
		EXECUTE stmt;
		DEALLOCATE PREPARE stmt;

		

    END 
#

CREATE PROCEDURE `boPaymentsReporting`(startDate TIMESTAMP,endDate TIMESTAMP)
BEGIN
	DECLARE done INT DEFAULT 0;
	DECLARE gameId INT;
	
	SET @cashQry=CONCAT("insert into st_rep_bo_payments(agent_org_id, parent_id,finaldate,cash_amt) select organization_id ,parent_id ,alldate , ifnull(cash,0.00) cash from (select organization_id,parent_id,alldate from st_lms_organization_master,datestore where organization_type='AGENT') om left outer join (select agent_org_id,date(transaction_date) tdate,sum(amount) as cash from st_lms_bo_cash_transaction cash inner join  st_lms_bo_transaction_master tm on cash.transaction_id=tm.transaction_id  where transaction_type=('CASH') and transaction_date>='",startDate,"' and transaction_date<='",endDate,"' group by date(transaction_date) ,agent_org_id) cash on organization_id=agent_org_id and tdate=alldate");
		PREPARE stmt FROM @cashQry;
		EXECUTE stmt;
		DEALLOCATE PREPARE stmt;
		SET @creditQry=CONCAT("update (select agent_org_id,date(transaction_date) tdate,sum(amount) as credit from st_lms_bo_credit_note cr inner join  st_lms_bo_transaction_master tm on cr.transaction_id=tm.transaction_id  where tm.transaction_type=('CR_NOTE_CASH') and cr.transaction_type in ('CR_NOTE_CASH','CR_NOTE') and transaction_date>='",startDate,"' and transaction_date<='",endDate,"' group by date(transaction_date),agent_org_id) credit,st_rep_bo_payments pay set credit_note=credit where credit.agent_org_id=pay.agent_org_id and pay.finaldate=credit.tdate");
		PREPARE stmt FROM @creditQry;
		EXECUTE stmt;
		DEALLOCATE PREPARE stmt;
		SET @debitQry=CONCAT("update (select agent_org_id,date(transaction_date) tdate,sum(amount) as debit from st_lms_bo_debit_note dr inner join  st_lms_bo_transaction_master tm on dr.transaction_id=tm.transaction_id  where tm.transaction_type IN ('DR_NOTE','DR_NOTE_CASH') and dr.transaction_type in ('DR_NOTE_CASH','DR_NOTE') and transaction_date>='",startDate,"' and transaction_date<='",endDate,"' group by date(transaction_date) ,agent_org_id) debit,st_rep_bo_payments pay set debit_note=debit where debit.agent_org_id=pay.agent_org_id and pay.finaldate=debit.tdate");
		PREPARE stmt FROM @debitQry;
		EXECUTE stmt;
		DEALLOCATE PREPARE stmt;
		SET @chqQry=CONCAT("update (select agent_org_id,date(transaction_date) tdate,sum(cheque_amt) as cheque_amt from st_lms_bo_sale_chq chq inner join  st_lms_bo_transaction_master tm on chq.transaction_id=tm.transaction_id  where tm.transaction_type IN ('CHEQUE','CLOSED') and chq.transaction_type IN ('CHEQUE', 'CLOSED') and transaction_date>='",startDate,"' and transaction_date<='",endDate,"' group by date(transaction_date) ,agent_org_id) chq,st_rep_bo_payments pay set pay.cheque_amt=chq.cheque_amt where chq.agent_org_id=pay.agent_org_id and pay.finaldate=chq.tdate");
		PREPARE stmt FROM @chqQry;
		EXECUTE stmt;
		DEALLOCATE PREPARE stmt;
		
		SET @chqBonuceQry=CONCAT("update (select agent_org_id,date(transaction_date) tdate,sum(cheque_amt) as cheque_amt from st_lms_bo_sale_chq chq inner join  st_lms_bo_transaction_master tm on chq.transaction_id=tm.transaction_id where tm.transaction_type=( 'CHQ_BOUNCE') and chq.transaction_type=('CHQ_BOUNCE') and transaction_date>='",startDate,"' and transaction_date<='",endDate,"' group by date(transaction_date),agent_org_id) chq,st_rep_bo_payments pay set pay.cheque_bounce_amt=chq.cheque_amt where chq.agent_org_id=pay.agent_org_id and pay.finaldate=chq.tdate");
		PREPARE stmt FROM @chqBonuceQry;
		EXECUTE stmt;
		DEALLOCATE PREPARE stmt;
		SET @bankQry=CONCAT("update (select agent_org_id,date(transaction_date) tdate,sum(amount) as amount from st_lms_bo_bank_deposit_transaction bank inner join st_lms_bo_transaction_master tm on bank.transaction_id=tm.transaction_id where tm.transaction_type=( 'BANK_DEPOSIT') and transaction_date>='",startDate,"' and transaction_date<='",endDate,"' group by date(transaction_date),agent_org_id) bank,st_rep_bo_payments pay set pay.bank_deposit=amount where bank.agent_org_id=pay.agent_org_id and pay.finaldate=bank.tdate");
		PREPARE stmt FROM @bankQry;
		EXECUTE stmt;
		DEALLOCATE PREPARE stmt;

		SET @clXclQry=CONCAT("UPDATE (SELECT temp.orgId AS orgId, SUM(temp.clAmount) AS clAmount, SUM(temp.xclAmount) AS xclAmount, parentId, orgType, tDate FROM(SELECT slcx.organization_id AS orgId, slom.parent_id AS parentId, organization_type AS orgType, DATE(date_time)AS tDate, IF(TYPE='CL',amount,0) clAmount, IF(TYPE='XCL',amount,0) xclAmount FROM st_lms_cl_xcl_update_history slcx INNER JOIN st_lms_organization_master slom ON slcx.organization_id=slom.organization_id AND organization_type='AGENT' AND DATE(date_time)>='",startDate,"' AND DATE(date_time)<='",endDate,"') temp  GROUP BY DATE(tDate), orgId ORDER BY orgId) clXclTbl, st_rep_bo_payments pay SET cl_amt=clAmount, xcl_amt=xclAmount WHERE pay.agent_org_id=clXclTbl.orgId AND pay.parent_id=clXclTbl.parentId AND pay.finaldate=clXclTbl.tDate");
		PREPARE stmt FROM @clXclQry;
		EXECUTE stmt;
		DEALLOCATE PREPARE stmt;
		
		SET @insStatus=CONCAT("insert into archiving_status(job_name, status)values('BO_PAYMENT_REPORTING', 'DONE')");
		PREPARE stmt FROM @insStatus;
		EXECUTE stmt;
		DEALLOCATE PREPARE stmt;


    END 
#
CREATE  PROCEDURE `cashChqPaymentDateWiseOverAll`(startDate timestamp,endDate timestamp,agtOrgId int)
BEGIN
set @mainQry=concat("select ifnull(chq.date,cash.date) date,ifnull(cash,0.0) cash,ifnull(chq,0.0) chq from ((select agent_org_id,sum(amount) cash,date(transaction_date) date from st_lms_bo_cash_transaction cash,st_lms_bo_transaction_master btm where cash.transaction_id=btm.transaction_id and transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and agent_org_id= ",agtOrgId, " group by date(transaction_date)) cash 
left join (select agent_org_id,sum(cheque_amt) chq,date(transaction_date) date from st_lms_bo_sale_chq chq,st_lms_bo_transaction_master btm where chq.transaction_id=btm.transaction_id and chq.transaction_type IN ('CHEQUE','CLOSED') and transaction_date>='",startDate,"' and transaction_date<='",endDate,"'and agent_org_id= ",agtOrgId," group by date(transaction_date)) chq on cash.date=chq.date ) union 
select ifnull(chq.date,cash.date) date,ifnull(cash,0.0) cash,ifnull(chq,0.0) chq from ((select agent_org_id,sum(amount) cash,date(transaction_date) date from st_lms_bo_cash_transaction cash,st_lms_bo_transaction_master btm where cash.transaction_id=btm.transaction_id and transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and agent_org_id= ",agtOrgId, " group by date(transaction_date)) cash 
right join (select agent_org_id,sum(cheque_amt) chq,date(transaction_date) date from st_lms_bo_sale_chq chq,st_lms_bo_transaction_master btm where chq.transaction_id=btm.transaction_id and chq.transaction_type IN ('CHEQUE','CLOSED') and transaction_date>='",startDate,"' and transaction_date<='",endDate,"'and agent_org_id= ",agtOrgId," group by date(transaction_date)) chq on cash.date=chq.date ) union all select finaldate,sum(cash_amt) as cash ,sum(cheque_amt) as chq from st_rep_bo_payments where finaldate>='",startDate,"' and finaldate<='",endDate,"' and agent_org_id=",agtOrgId," group by finaldate");
 
PREPARE stmt FROM @mainQry;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
    END 
#
CREATE PROCEDURE `cashLiveGameReportRetailerWise`(startDate timestamp,endDate timestamp,parentOrgId int)
BEGIN
if(parentOrgId!=1)
then
set @mainQry=concat("select main.retailer_org_id retailer_org_id,sum(main.cash) cash from (select retailer_org_id,sum(amount) cash from st_lms_agent_cash_transaction where transaction_id in(select transaction_id from st_lms_agent_transaction_master where transaction_type='CASH' and transaction_date>='",startDate,"' and transaction_date<='",endDate,"') and agent_org_id=",parentOrgId," group by retailer_org_id union all select retailer_org_id,sum(cash_amt) cash from st_rep_agent_payments where parent_id=",parentOrgId," and finaldate>='",startDate,"' and finaldate<'",endDate,"' group by retailer_org_id)main group by main.retailer_org_id");
else
set @mainQry=concat("select main.retailer_org_id retailer_org_id,sum(main.cash) cash from (select retailer_org_id,sum(amount) cash from st_lms_agent_cash_transaction where transaction_id in(select transaction_id from st_lms_agent_transaction_master where transaction_type='CASH' and transaction_date>='",startDate,"' and transaction_date<='",endDate,"') group by retailer_org_id union all select retailer_org_id,sum(cash_amt) cash from st_rep_agent_payments where finaldate>='",startDate,"' and finaldate<'",endDate,"' group by retailer_org_id)main group by main.retailer_org_id");
end if ;

            
PREPARE stmt FROM @mainQry;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
    END
#
CREATE PROCEDURE `chqRetBankPaymentDateWiseOverAll`(startDate timestamp,endDate timestamp,agtOrgId int)
BEGIN
set @mainQry=concat("select ifnull(chq_ret.date,bank.date) date,ifnull(chq_ret,0.0) chq_ret,ifnull(bank,0.0) bank from ((select agent_org_id,sum(cheque_amt) chq_ret,date(transaction_date) date from st_lms_bo_sale_chq chq,st_lms_bo_transaction_master btm where chq.transaction_id=btm.transaction_id and chq.transaction_type IN ('CHQ_BOUNCE') and transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and agent_org_id= ",agtOrgId," group by date(transaction_date)) chq_ret
left join (select agent_org_id,sum(amount) bank,date(transaction_date) date from st_lms_bo_bank_deposit_transaction bank,st_lms_bo_transaction_master btm where bank.transaction_id=btm.transaction_id and transaction_date>='", startDate,"' and transaction_date<='",endDate,"'and agent_org_id= ",agtOrgId, " group by date(transaction_date))  bank on chq_ret.date=bank.date ) union
select ifnull(chq_ret.date,bank.date) date,ifnull(chq_ret,0.0) chq_ret,ifnull(bank,0.0) bank from ((select agent_org_id,sum(cheque_amt) chq_ret,date(transaction_date) date from st_lms_bo_sale_chq chq,st_lms_bo_transaction_master btm where chq.transaction_id=btm.transaction_id and chq.transaction_type IN ('CHQ_BOUNCE') and transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and agent_org_id= ",agtOrgId," group by date(transaction_date)) chq_ret
right join (select agent_org_id,sum(amount) bank,date(transaction_date) date from st_lms_bo_bank_deposit_transaction bank,st_lms_bo_transaction_master btm where bank.transaction_id=btm.transaction_id and transaction_date>='", startDate,"' and transaction_date<='",endDate,"'and agent_org_id= ",agtOrgId, " group by date(transaction_date))  bank on chq_ret.date=bank.date ) 
union all select finaldate,sum(cheque_bounce_amt)as chq_ret,sum(bank_deposit)as bank from st_rep_bo_payments where finaldate>='",startDate,"' and finaldate<='",endDate,"' and agent_org_id=",agtOrgId," group by finaldate");
 
	PREPARE stmt FROM @mainQry;
	EXECUTE stmt;
	DEALLOCATE PREPARE stmt;   
    END
#
CREATE      PROCEDURE `collectionCashChqOverAll`(startDate timestamp,endDate timestamp)
BEGIN
          
         
         
	set @addOrgQry = concat("right outer join (select organization_id from st_lms_organization_master where organization_type='AGENT') om on agent_org_id=organization_id");
	set @cashQry = concat("(select organization_id,cash from (select agent_org_id,sum(amount) cash from st_lms_bo_cash_transaction cash,st_lms_bo_transaction_master btm where cash.transaction_id=btm.transaction_id and transaction_date>='",startDate,"' and transaction_date<='",endDate,"' group by agent_org_id) cash ",@addOrgQry,") cash");
	set @chqQry = concat("(select organization_id,chq from (select agent_org_id,sum(cheque_amt) chq from st_lms_bo_sale_chq chq,st_lms_bo_transaction_master btm where chq.transaction_id=btm.transaction_id and chq.transaction_type IN ('CHEQUE','CLOSED') and transaction_date>='",startDate,"' and transaction_date<='",endDate,"' group by agent_org_id) chq ",@addOrgQry,") chq");
	set @chqRetQry = concat("(select organization_id,chq_ret from (select agent_org_id,sum(cheque_amt) chq_ret from st_lms_bo_sale_chq chq,st_lms_bo_transaction_master btm where chq.transaction_id=btm.transaction_id and chq.transaction_type IN ('CHQ_BOUNCE') and transaction_date>='",startDate,"' and transaction_date<='",endDate,"' group by agent_org_id) chq_ret ",@addOrgQry, ") chq_ret");
	set @debitQry = concat("(select organization_id,debit from (select agent_org_id,sum(amount) debit from st_lms_bo_debit_note debit,st_lms_bo_transaction_master btm where debit.transaction_id=btm.transaction_id and debit.transaction_type IN ('DR_NOTE_CASH','DR_NOTE') and transaction_date>='",startDate,"' and transaction_date<='",endDate,"' group by agent_org_id) debit ",@addOrgQry,") debit");
	set @creditQry = concat("(select organization_id,credit from (select agent_org_id,sum(amount) credit from st_lms_bo_credit_note credit,st_lms_bo_transaction_master btm where credit.transaction_id=btm.transaction_id and credit.transaction_type IN ('CR_NOTE_CASH','CR_NOTE') and transaction_date>='",startDate,"' and transaction_date<='",endDate,"' group by agent_org_id) credit ",@addOrgQry,") credit");
	set @bankQry=concat("(select organization_id,bank from (select agent_org_id,sum(amount) bank from st_lms_bo_bank_deposit_transaction bank,st_lms_bo_transaction_master btm where bank.transaction_id=btm.transaction_id and transaction_date>='",startDate,"' and transaction_date<='",endDate,"' group by agent_org_id) bank ",@addOrgQry,") bank");
	set @mainQry=concat("select main.organization_id,sum(main.cash)as cash,sum(main.chq)as chq,sum(main.chq_ret)as chq_ret,sum(main.debit)as debit,sum(main.credit)as credit,sum(main.bank)as bank from (select cash.organization_id,ifnull(cash,0.0) cash,ifnull(chq,0.0) chq,ifnull(chq_ret,0.0) chq_ret,ifnull(debit,0.0) debit,ifnull(credit,0.0) credit, ifnull(bank,0.0) bank from ",@cashQry,",",@chqQry,",",@chqRetQry,",", @debitQry,",",@creditQry,",",@bankQry," where cash.organization_id=chq.organization_id and cash.organization_id=chq_ret.organization_id and cash.organization_id=debit.organization_id and cash.organization_id=credit.organization_id and cash.organization_id=bank.organization_id and chq.organization_id=chq_ret.organization_id and chq.organization_id=debit.organization_id and chq.organization_id=credit.organization_id and chq.organization_id=bank.organization_id and chq_ret.organization_id=debit.organization_id and chq_ret.organization_id=credit.organization_id and chq_ret.organization_id=bank.organization_id  and debit.organization_id=credit.organization_id and debit.organization_id=bank.organization_id and credit.organization_id=bank.organization_id union all select agent_org_id,sum(cash_amt) as cash ,sum(cheque_amt) as chq,sum(cheque_bounce_amt)as chq_ret,sum(debit_note) as debit,sum(credit_note) as credit,sum(bank_deposit)as bank from st_rep_bo_payments where finaldate>='",startDate,"' and finaldate<='",endDate,"' group by agent_org_id)as main group by main.organization_id ");
	
	PREPARE stmt FROM @mainQry;
	EXECUTE stmt;
	DEALLOCATE PREPARE stmt;
    END 
#
CREATE      PROCEDURE `createArchDB`(curDBName VARCHAR(50),dbName VARCHAR(50))
BEGIN
	DECLARE done INT DEFAULT 0;
	DECLARE tlbName VARCHAR(100);
	DECLARE  tblCur CURSOR FOR SELECT table_name FROM information_schema.TABLES WHERE TABLE_SCHEMA=curDBName;
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;
SET @dropDBQry=CONCAT("DROP DATABASE IF EXISTS ",dbName,"");
PREPARE stmt FROM @dropDBQry;
		EXECUTE stmt;
		DEALLOCATE PREPARE stmt;
SET @crDBQry=CONCAT("CREATE DATABASE ",dbName,"");
PREPARE stmt FROM @crDBQry;
		EXECUTE stmt;
		DEALLOCATE PREPARE stmt;
	OPEN tblCur;
	  read_loop: LOOP
	    FETCH tblCur INTO tlbName;
	    IF done THEN
	      LEAVE read_loop;
	    END IF;
		SET @crTlbQry=CONCAT("CREATE TABLE ",dbName,".",tlbName," SELECT * FROM ",tlbName," where 1=2");
PREPARE stmt FROM @crTlbQry;
		EXECUTE stmt;
		DEALLOCATE PREPARE stmt;
SET @alterTlbQry=CONCAT("alter table ",dbName,".",tlbName," Engine=InnoDB row_format=compressed");
PREPARE stmt FROM @alterTlbQry;
		EXECUTE stmt;
		DEALLOCATE PREPARE stmt;
	END LOOP;
	CLOSE tblCur;
    END  
#
CREATE      PROCEDURE `creditDebitPaymentDateWiseOverAll`(startDate timestamp,endDate timestamp,agtOrgId int)
BEGIN
set @mainQry=concat("select ifnull(credit.date,debit.date) date,ifnull(debit,0.0) debit,ifnull(credit,0.0) credit,ifnull(training,0.0) training,ifnull(incentive,0.0) incentive,ifnull(others,0.0) others  from ((select agent_org_id,sum(amount) debit,date(transaction_date) date from st_lms_bo_debit_note debit,st_lms_bo_transaction_master btm where debit.transaction_id=btm.transaction_id and debit.transaction_type IN ('DR_NOTE_CASH','DR_NOTE') and transaction_date>='",startDate,"' and transaction_date<='", endDate, "' and agent_org_id= ",agtOrgId," group by date(transaction_date))  debit left join (select agent_org_id,sum(credit) credit,sum(training) training,sum(incentive) incentive,sum(others) others,date from (select agent_org_id, sum(amount) credit,0 training, 0 incentive, 0 others,date(transaction_date) date from st_lms_bo_credit_note credit,st_lms_bo_transaction_master btm where credit.transaction_id=btm.transaction_id and credit.transaction_type IN ('CR_NOTE_CASH','CR_NOTE') and transaction_date>='",startDate,"' and transaction_date<='", endDate, "'and agent_org_id= ",agtOrgId," group by date(transaction_date) union all select agent_org_id,0 credit, sum(amount) training, 0 incentive, 0 others,date(transaction_date) date from st_lms_bo_credit_note credit,st_lms_bo_transaction_master btm where credit.transaction_id=btm.transaction_id and credit.transaction_type IN ('CR_NOTE_CASH','CR_NOTE') and credit.reason IN ('CR_WEEKLY_EXP','CR_DAILY_EXP') and transaction_date>='",startDate,"' and transaction_date<='", endDate, "'and agent_org_id= ",agtOrgId," group by date(transaction_date) union all select agent_org_id,0 credit, 0 training, sum(amount) incentive,0 others,date(transaction_date) date from st_lms_bo_credit_note credit,st_lms_bo_transaction_master btm where credit.transaction_id=btm.transaction_id and credit.transaction_type IN ('CR_NOTE_CASH','CR_NOTE') and credit.reason IN ('CR_DAILY_INCENTIVE','CR_WEEKLY_INCENTIVE') and transaction_date>='",startDate,"' and transaction_date<='", endDate, "'and agent_org_id= ",agtOrgId," group by date(transaction_date) union all select agent_org_id,0 credit, 0 training, 0 incentive, sum(amount) others,date(transaction_date) date from st_lms_bo_credit_note credit,st_lms_bo_transaction_master btm where credit.transaction_id=btm.transaction_id and credit.transaction_type IN ('CR_NOTE_CASH','CR_NOTE') and credit.reason NOT IN ('CR_DAILY_INCENTIVE','CR_WEEKLY_INCENTIVE','CR_WEEKLY_EXP','CR_DAILY_EXP') and transaction_date>='",startDate,"' and transaction_date<='", endDate, "'and agent_org_id= ",agtOrgId," group by date(transaction_date) ) ab group by date) credit on debit.date=credit.date ) union select ifnull(credit.date,debit.date) date,ifnull(debit,0.0) debit,ifnull(credit,0.0) credit,ifnull(training,0.0) training,ifnull(incentive,0.0) incentive,ifnull(others,0.0) others from ((select agent_org_id,sum(amount) debit,date(transaction_date) date from st_lms_bo_debit_note debit,st_lms_bo_transaction_master btm where debit.transaction_id=btm.transaction_id and debit.transaction_type IN ('DR_NOTE_CASH','DR_NOTE') and transaction_date>='",startDate,"' and transaction_date<='", endDate, "' and agent_org_id= ",agtOrgId," group by date(transaction_date))  debit right join (select agent_org_id,sum(credit) credit,sum(training) training,sum(incentive) incentive,sum(others) others,date from (select agent_org_id, sum(amount) credit,0 training, 0 incentive, 0 others,date(transaction_date) date from st_lms_bo_credit_note credit,st_lms_bo_transaction_master btm where credit.transaction_id=btm.transaction_id and credit.transaction_type IN ('CR_NOTE_CASH','CR_NOTE') and transaction_date>='",startDate,"' and transaction_date<='", endDate, "'and agent_org_id= ",agtOrgId," group by date(transaction_date) union all select agent_org_id,0 credit, sum(amount) training, 0 incentive, 0 others,date(transaction_date) date from st_lms_bo_credit_note credit,st_lms_bo_transaction_master btm where credit.transaction_id=btm.transaction_id and credit.transaction_type IN ('CR_NOTE_CASH','CR_NOTE') and credit.reason IN ('CR_WEEKLY_EXP','CR_DAILY_EXP') and transaction_date>='",startDate,"' and transaction_date<='", endDate, "'and agent_org_id= ",agtOrgId," group by date(transaction_date) union all select agent_org_id,0 credit, 0 training, sum(amount) incentive,0 others,date(transaction_date) date from st_lms_bo_credit_note credit,st_lms_bo_transaction_master btm where credit.transaction_id=btm.transaction_id and credit.transaction_type IN ('CR_NOTE_CASH','CR_NOTE') and credit.reason IN ('CR_DAILY_INCENTIVE','CR_WEEKLY_INCENTIVE') and transaction_date>='",startDate,"' and transaction_date<='", endDate, "'and agent_org_id= ",agtOrgId," group by date(transaction_date) union all select agent_org_id,0 credit, 0 training, 0 incentive, sum(amount) others,date(transaction_date) date from st_lms_bo_credit_note credit,st_lms_bo_transaction_master btm where credit.transaction_id=btm.transaction_id and credit.transaction_type IN ('CR_NOTE_CASH','CR_NOTE') and credit.reason NOT IN ('CR_DAILY_INCENTIVE','CR_WEEKLY_INCENTIVE','CR_WEEKLY_EXP','CR_DAILY_EXP') and transaction_date>='",startDate,"' and transaction_date<='", endDate, "'and agent_org_id= ",agtOrgId," group by date(transaction_date) ) ab group by date) credit on debit.date=credit.date ) union all select finaldate,sum(debit_note) as debit,sum(credit_note) as credit, 0 training, 0 incentive, 0 others from st_rep_bo_payments where finaldate>='",startDate,"' and finaldate<='", endDate, "' and agent_org_id=",agtOrgId," group by finaldate");


	PREPARE stmt FROM @mainQry;
	EXECUTE stmt;
	DEALLOCATE PREPARE stmt;    
END     
#
CREATE      PROCEDURE `csAgtReporting`(startDate timestamp,endDate timestamp)
BEGIN
	set @saleQry=concat("insert into st_rep_cs_agent(organization_id, parent_id,finaldate,category_id,sale_mrp,sale_comm,sale_net,sale_good_cause,sale_vat,sale_jv_comm,sale_jv_net_amt) select organization_id,parent_id,alldate,om.category_id,ifnull(saleMrp,0.00) saleMrp,ifnull(agtComm,0.00) agtComm,ifnull(saleNet,0.00) saleNet,ifnull(govtComm,0.00) govtComm,ifnull(vatAmt,0.00) vatAmt,ifnull(saleJVComm,0.00) saleJVComm,ifnull(saleJVNetAmt,0.00) saleJVNetAmt from (select organization_id,parent_id,date(alldate) alldate,category_id from st_lms_organization_master,tempdate,st_cs_product_category_master where organization_type='AGENT') om left outer join (select drs.agent_org_id,date(transaction_date) tx_date,category_id,sum(mrp_amt) saleMrp,sum(drs.agent_comm) agtComm ,sum(bo_net_amt) saleNet,sum(drs.govt_comm) govtComm,sum(vat_amt) vatAmt,sum(drs.jv_comm) saleJVComm,sum(jv_comm_amt) saleJVNetAmt from st_cs_agt_sale drs inner join st_lms_agent_transaction_master rtm inner join st_cs_product_master pm on drs.transaction_id=rtm.transaction_id and  drs.product_id=pm.product_id where rtm.transaction_date>='",startDate,"' and rtm.transaction_date<='",endDate,"' and transaction_type in('CS_SALE')  group by agent_org_id,tx_date,category_id) sale on organization_id=agent_org_id and alldate=tx_date and sale.category_id=om.category_id");
		PREPARE stmt FROM @saleQry;
		EXECUTE stmt;
		DEALLOCATE PREPARE stmt;
		set @cancelQry=concat("update (select drs.agent_org_id,date(transaction_date) tx_date,category_id,sum(mrp_amt) saleRefMrp,sum(drs.agent_comm) agtCommRef ,sum(bo_net_amt) saleRefNet,sum(drs.govt_comm) govtCommRef,sum(drs.vat_amt) vatAmtRef,sum(drs.jv_comm) refJVComm,sum(jv_comm_amt) refJVNetAmt from st_cs_agt_refund drs inner join st_lms_agent_transaction_master rtm inner join st_cs_product_master cm on drs.transaction_id=rtm.transaction_id and drs.product_id=cm.product_id where rtm.transaction_date>='",startDate,"' and rtm.transaction_date<='",endDate,"' and transaction_type='CS_CANCEL_SERVER'  group by agent_org_id,tx_date,category_id) cancel,st_rep_cs_agent cs set ref_sale_mrp=saleRefMrp,ref_comm=agtCommRef,ref_net_amt=saleRefNet,ref_good_cause=govtCommRef,ref_vat=vatAmtRef,ref_jv_comm=refJVComm, ref_jv_net_amt=refJVNetAmt  where cancel.agent_org_id=cs.organization_id and cs.finaldate=cancel.tx_date and cs.category_id=cancel.category_id");
		PREPARE stmt FROM @cancelQry;
		EXECUTE stmt;
		DEALLOCATE PREPARE stmt;
set @insStatus=concat("insert into archiving_status(job_name, status)values('CS_AGENT_REPORTING', 'DONE')");
prepare stmt from @insStatus;
execute stmt;
deallocate prepare stmt;
    END     
#
CREATE      PROCEDURE `csBOReporting`(startDate timestamp,endDate timestamp)
BEGIN
	set @saleQry=concat("insert into st_rep_cs_bo(organization_id, parent_id,finaldate,category_id,sale_mrp,sale_comm,sale_net,sale_good_cause,sale_vat,sale_jv_comm,sale_jv_net_amt) select organization_id,parent_id,alldate,om.category_id,ifnull(saleMrp,0.00) saleMrp,ifnull(agtComm,0.00) agtComm,ifnull(saleNet,0.00) saleNet,ifnull(govtComm,0.00) govtComm,ifnull(vatAmt,0.00) vatAmt,ifnull(jvComm,0.00) jvComm,ifnull(jvNetAmt,0.00) jvNetAmt from (select organization_id,parent_id,date(alldate) alldate,category_id from st_lms_organization_master,tempdate,st_cs_product_category_master where organization_type='BO') om left outer join (select date(transaction_date) tx_date,category_id,sum(mrp_amt) saleMrp,sum(drs.agent_comm) agtComm ,sum(net_amt) saleNet,sum(drs.govt_comm) govtComm,sum(vat) vatAmt,sum(drs.jv_comm) jvComm,sum(jv_comm_amt) jvNetAmt from st_cs_bo_sale drs inner join st_lms_bo_transaction_master rtm inner join st_cs_product_master pm on drs.transaction_id=rtm.transaction_id and drs.product_id=pm.product_id where rtm.transaction_date>='",startDate,"' and rtm.transaction_date<='",endDate,"' and transaction_type='CS_SALE' group by tx_date,category_id) sale on alldate=tx_date and sale.category_id=om.category_id");
		PREPARE stmt FROM @saleQry;
		EXECUTE stmt;
		DEALLOCATE PREPARE stmt;
		set @cancelQry=concat("update (select date(transaction_date) tx_date,category_id,sum(mrp_amt) saleRefMrp,sum(drs.agent_comm) agtCommRef ,sum(net_amt) saleRefNet,sum(drs.govt_comm) govtCommRef,sum(vat) vatAmtRef,sum(drs.jv_comm) jvComm,sum(jv_comm_amt) jvNetAmt from st_cs_bo_refund drs inner join st_lms_bo_transaction_master rtm inner join st_cs_product_master pm on drs.transaction_id=rtm.transaction_id and drs.product_id=pm.product_id where rtm.transaction_date>='",startDate,"' and rtm.transaction_date<='",endDate,"' and transaction_type='CS_CANCEL_SERVER'  group by tx_date,category_id) cancel,st_rep_cs_bo dg set ref_sale_mrp=saleRefMrp,ref_comm=agtCommRef,ref_net_amt=saleRefNet,ref_good_cause=govtCommRef,ref_vat=vatAmtRef,ref_jv_comm=jvComm,ref_jv_net_amt=jvNetAmt  where dg.finaldate=cancel.tx_date and dg.category_id=cancel.category_id");
		PREPARE stmt FROM @cancelQry;
		EXECUTE stmt;
		DEALLOCATE PREPARE stmt;  
set @insStatus=concat("insert into archiving_status(job_name, status)values('CS_BO_REPORTING', 'DONE')");
prepare stmt from @insStatus;
execute stmt;
deallocate prepare stmt;
  END
#
CREATE      PROCEDURE `csCollectionAgentWisePerCategory`(startDate Timestamp, endDate Timestamp, catId int)
BEGIN
	 
         
         
      set @mainQry=concat("select parent_id,sum(main.sale) as sale ,sum(main.cancel)as cancel from (select sale.parent_id,sum(sale)sale,sum(cancel)cancel from (select organization_id,parent_id,ifnull(sale,0.0) sale from st_lms_organization_master om left outer join (select cs.retailer_org_id,sum(agent_net_amt) as sale from st_cs_sale_",catId," cs inner join st_lms_retailer_transaction_master rtm on cs.transaction_id=rtm.transaction_id where rtm.transaction_type='CS_SALE' and rtm.transaction_date>='",startDate,"' and rtm.transaction_date<='",endDate,"' group by cs.retailer_org_id) sale on om.organization_id=retailer_org_id where om.organization_type='RETAILER') sale inner join (select organization_id,parent_id,ifnull(cancel,0.0) cancel from st_lms_organization_master om left outer join (select cs.retailer_org_id,sum(agent_net_amt) as cancel from st_cs_refund_",catId," cs inner join st_lms_retailer_transaction_master rtm on cs.transaction_id=rtm.transaction_id where transaction_type in('CS_CANCEL_SERVER','CS_CANCEL_RET') and transaction_date>='",startDate,"' and transaction_date<='",endDate,"' group by cs.retailer_org_id) cancel on om.organization_id=retailer_org_id where om.organization_type='RETAILER') cancel on  sale.organization_id=cancel.organization_id group by parent_id union all select organization_id,sum(sale_net) as sale ,sum(ref_net_amt) as cancel from st_rep_cs_agent where category_id = ",catId," and finaldate>=date('",startDate,"') and finaldate<=date('",endDate,"') group by organization_id) as main group by main.parent_id");
      
PREPARE stmt FROM @mainQry;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
    END    
#
CREATE      PROCEDURE `csCollectionAgentWisePerGame`(startDate timestamp,endDate timestamp,catId int)
BEGIN
 set @mainQry = concat("select parent_id,sum(sale) sale,sum(cancel) cancel from(select organization_id parent_id,ifnull(sale,0.0) sale,ifnull(cancel,0.0) cancel from (select sale.parent_id,sale,cancel from (select bb.parent_id,sum(sale) as sale from (select drs.retailer_org_id,sum(agent_net_amt) as sale from st_cs_sale_",catId," drs where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_type in('CS_SALE') and transaction_date>='",startDate,"' and transaction_date<='",endDate,"') group by drs.retailer_org_id) aa right outer join (select organization_id,parent_id from st_lms_organization_master where organization_type='RETAILER')bb on retailer_org_id=organization_id group by parent_id) sale,(select bb.parent_id,sum(cancel) as cancel from (select drs.retailer_org_id,sum(agent_net_amt) as cancel from st_cs_refund_",catId," drs where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_type in('CS_CANCEL_SERVER','CS_CANCEL_RET') and transaction_date>='",startDate,"' and transaction_date<='",endDate,"') group by drs.retailer_org_id) aa right outer join (select organization_id,parent_id from st_lms_organization_master where organization_type='RETAILER') bb on retailer_org_id=organization_id group by parent_id) cancel where sale.parent_id=cancel.parent_id  union all select organization_id,sum(sale_net) as sale ,sum(ref_net_amt) as cancel from st_rep_cs_agent where category_id = ",catId," and finaldate>=date('",startDate,"') and finaldate<=date('",endDate,"') group by organization_id) gameTlb right outer join (select organization_id from st_lms_organization_master where organization_type='AGENT') agtTlb on gameTlb.parent_id=agtTlb.organization_id)main group by main.parent_id");

 PREPARE stmt FROM @mainQry;
	  EXECUTE stmt;
	  DEALLOCATE PREPARE stmt;
    END     
#
CREATE      PROCEDURE `csCollectionDateWisePerCategory`(startDate Timestamp, endDate Timestamp, catId int, agtOrgId int)
BEGIN
set @mainQry=concat("select ifnull(saleTlb1.date,cancelTlb1.date) date,saleTlb1.parent_id,ifnull(sale,0.0) as sale,ifnull(cancel,0.0) as cancel from
(select date(transaction_date) date,parent_id,ifnull(sum(sale),0.0) as sale from ((select bb.parent_id,sum(sale) as sale,transaction_date from(select drs.retailer_org_id,sum(agent_net_amt) as sale,rtm.transaction_date from st_cs_sale_",catId," drs,st_lms_retailer_transaction_master rtm where drs.transaction_id=rtm.transaction_id and rtm.transaction_type in('CS_SALE') and transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and drs.retailer_org_id in (select organization_id from st_lms_organization_master where organization_type='RETAILER') group by date(transaction_date),drs.retailer_org_id)aa,st_lms_organization_master bb where retailer_org_id=organization_id and parent_id=",agtOrgId," group by date(transaction_date))) saleTlb group by date(transaction_date))saleTlb1
left join
(select date(transaction_date) date,parent_id,ifnull(sum(cancel),0.0) as cancel from ((select bb.parent_id,sum(cancel) as cancel,transaction_date from(select drs.retailer_org_id,sum(agent_net_amt) as cancel,rtm.transaction_date from st_cs_refund_",catId," drs,st_lms_retailer_transaction_master rtm where drs.transaction_id=rtm.transaction_id and rtm.transaction_type in('CS_CANCEL_SERVER','CS_CANCEL_RET') and transaction_date>='",startDate,"'and transaction_date<='",endDate,"' and drs.retailer_org_id in (select organization_id from st_lms_organization_master where organization_type='RETAILER') group by date(transaction_date),drs.retailer_org_id)aa,st_lms_organization_master bb where retailer_org_id=organization_id and parent_id=",agtOrgId," group by date(transaction_date))) cancelTlb group by date(transaction_date))cancelTlb1 on saleTlb1.date=cancelTlb1.date
union 
select ifnull(saleTlb1.date,cancelTlb1.date) date,saleTlb1.parent_id,ifnull(sale,0.0) as sale,ifnull(cancel,0.0) as cancel from
(select date(transaction_date) date,parent_id,sum(sale) as sale from ((select bb.parent_id,ifnull(sum(sale),0.0) as sale,transaction_date from(select drs.retailer_org_id,sum(agent_net_amt) as sale,rtm.transaction_date from st_cs_sale_",catId," drs,st_lms_retailer_transaction_master rtm where drs.transaction_id=rtm.transaction_id and rtm.transaction_type in('CS_SALE') and transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and drs.retailer_org_id in (select organization_id from st_lms_organization_master where organization_type='RETAILER') group by date(transaction_date),drs.retailer_org_id)aa,st_lms_organization_master bb where retailer_org_id=organization_id and parent_id=",agtOrgId," group by date(transaction_date))) saleTlb group by date(transaction_date))saleTlb1
right join
(select date(transaction_date) date,parent_id,sum(cancel) as cancel from ((select bb.parent_id,ifnull(sum(cancel),0.0) as cancel,transaction_date from(select drs.retailer_org_id,sum(agent_net_amt) as cancel,rtm.transaction_date from st_cs_refund_",catId," drs,st_lms_retailer_transaction_master rtm where drs.transaction_id=rtm.transaction_id and rtm.transaction_type in('CS_CANCEL_SERVER','CS_CANCEL_RET') and transaction_date>='",startDate,"'and transaction_date<='",endDate,"' and drs.retailer_org_id in (select organization_id from st_lms_organization_master where organization_type='RETAILER') group by date(transaction_date),drs.retailer_org_id)aa,st_lms_organization_master bb where retailer_org_id=organization_id and parent_id=",agtOrgId," group by date(transaction_date))) cancelTlb group by date(transaction_date))cancelTlb1
on saleTlb1.date=cancelTlb1.date
union all select finaldate,organization_id,sum(sale_net) as sale ,sum(ref_net_amt) as cancel from st_rep_cs_agent where category_id = ",catId," and organization_id=",agtOrgId," and finaldate>=date('",startDate,"') and finaldate<=date('",endDate,"') group by finaldate
");
 PREPARE stmt FROM @mainQry;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
    END 
#
CREATE      PROCEDURE `csCollectionDayWisePerCategory`(startDate timestamp,endDate timestamp,catId int,agtOrgId int,viewBy varchar(20))
BEGIN
	
         
         
         
	if(viewBy='Agent' and agtOrgId!=0) then
		set @csSaleCol = "net_amt";
		set @addCSQry = concat(" and cs.retailer_org_id in (select organization_id from st_lms_organization_master where parent_id=", agtOrgId , ")");
	else
		set @csSaleCol = "agent_net_amt";
		set @addCSQry = "";
	end if;
	
 
 set @mainQry=concat("select main.alldate,sum(main.sale)as sale,sum(main.cancel)as cancel from (select sale.alldate,ifnull(sale,0.0) sale,ifnull(cancel,0.0) cancel from (select alldate,sale from tempdate left outer join (select date(transaction_date) tx_date,sum(",@csSaleCol,") sale from st_cs_sale_",catId," cs,st_lms_retailer_transaction_master rtm where cs.transaction_id=rtm.transaction_id and rtm.transaction_date>='",startDate,"' and rtm.transaction_date<='",endDate,"' and transaction_type ='CS_SALE' ",@addCSQry," group by tx_date) sale on alldate=sale.tx_date) sale inner join (select alldate,cancel from tempdate left outer join (select date(transaction_date) tx_date,sum(",@csSaleCol,") cancel from st_cs_refund_",catId," cs,st_lms_retailer_transaction_master rtm where cs.transaction_id=rtm.transaction_id and rtm.transaction_date>='",startDate,"' and rtm.transaction_date<='",endDate,"' and transaction_type in('CS_CANCEL_SERVER','CS_CANCEL_RET') ",@addCSQry," group by tx_date) cancel on alldate=cancel.tx_date) cancel on sale.alldate=cancel.alldate ");
if(viewBy='Agent' and agtOrgId!=0) then
		set @mainQry=concat(@mainQry," union all select csr.finaldate as alldate,sum(csr.sale_net) as sale ,sum(csr.ref_net_amt) cancel  from st_rep_cs_retailer as csr, st_rep_cs_agent as csa where csr.finaldate=csa.finaldate and csr.parent_id=csa.parent_id and csr.parent_id=",agtOrgId," and csr.category_id=",catId," and csr.finaldate>=date('",startDate,"') and csr.finaldate<=date('",endDate,"') group by csr.finaldate)as main group by main.alldate ");
	else
		set @mainQry=concat(@mainQry," union all select finaldate as alldate,sum(sale_net) as sale,sum(ref_net_amt) as cancel from st_rep_cs_agent where category_id=",catId," and finaldate>=date('",startDate,"') and finaldate<=date('",endDate,"') group by finaldate)as main group by main.alldate  ");
	end if;
	
 
 PREPARE stmt FROM @mainQry;
 EXECUTE stmt;
 DEALLOCATE PREPARE stmt;
    END 
#

CREATE      PROCEDURE `csCollectionRetailerWisePerCategory`(startDate TIMESTAMP, endDate TIMESTAMP, catId INT, parentOrgId INT)
BEGIN
	
        
        
 
         IF(parentOrgId!=0) THEN
	  SET @addQry=CONCAT("and om.parent_id=",parentOrgId,"");
	  SET @addQry2=CONCAT(" and parent_id=",parentOrgId,"");
	ELSE
	SET @addQry="";
	SET @addQry2="";
	END IF;
         SET @mainQry=CONCAT("select main.organization_id,sum(main.sale)as sale,sum(main.cancel)as cancel from (select sale.organization_id,sale,cancel from (select organization_id,ifnull(sale,0.0) sale from st_lms_organization_master om left outer join (select cs.retailer_org_id,sum(net_amt) as sale from st_cs_sale_",catId," cs inner join st_lms_retailer_transaction_master rtm on cs.transaction_id=rtm.transaction_id where rtm.transaction_type ='CS_SALE' and rtm.transaction_date>='",startDate,"' and rtm.transaction_date<='",endDate,"' group by cs.retailer_org_id) sale on om.organization_id=retailer_org_id where om.organization_type='RETAILER' ",@addQry,") sale inner join (select organization_id,ifnull(cancel,0.0) cancel from st_lms_organization_master om left outer join (select cs.retailer_org_id,sum(net_amt) as cancel from st_cs_refund_",catId," cs inner join st_lms_retailer_transaction_master rtm on cs.transaction_id=rtm.transaction_id where transaction_type in('CS_CANCEL_SERVER','CS_CANCEL_RET') and transaction_date>='",startDate,"' and transaction_date<='",endDate,"' group by cs.retailer_org_id) cancel on om.organization_id=retailer_org_id where om.organization_type='RETAILER'  ",@addQry,") cancel on sale.organization_id=cancel.organization_id union all select organization_id,sum(sale_net) as sale ,sum(ref_net_amt) cancel  from  st_rep_cs_retailer where  category_id=",catId,@addQry2," and finaldate>=date('",startDate,"') and finaldate<=date('",endDate,"') group by organization_id)as main group by main.organization_id ");

    
 
PREPARE stmt FROM @mainQry;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
    END     
#
CREATE      PROCEDURE `csPaymentAgentWisePerGame`(startDate timestamp,endDate timestamp,catId int,agtOrgId int)
BEGIN
       set @mainQry=concat("select parent_id,sum(main.sale) as sale ,sum(main.cancel)as cancel from (select sale.parent_id,sum(sale)sale,sum(cancel)cancel from (select organization_id,parent_id,ifnull(sale,0.0) sale from st_lms_organization_master om left outer join (select cs.retailer_org_id,sum(agent_net_amt) as sale from st_cs_sale_",catId," cs inner join st_lms_retailer_transaction_master rtm on cs.transaction_id=rtm.transaction_id where rtm.transaction_type='CS_SALE' and rtm.transaction_date>='",startDate,"' and rtm.transaction_date<='",endDate,"' group by cs.retailer_org_id) sale on om.organization_id=retailer_org_id where om.organization_type='RETAILER' and om.parent_id=",agtOrgId,") sale inner join (select organization_id,parent_id,ifnull(cancel,0.0) cancel from st_lms_organization_master om left outer join (select cs.retailer_org_id,sum(agent_net_amt) as cancel from st_cs_refund_",catId," cs inner join st_lms_retailer_transaction_master rtm on cs.transaction_id=rtm.transaction_id where transaction_type in('CS_CANCEL_SERVER','CS_CANCEL_RET') and transaction_date>='",startDate,"' and transaction_date<='",endDate,"' group by cs.retailer_org_id) cancel on om.organization_id=retailer_org_id where om.organization_type='RETAILER' and om.parent_id=",agtOrgId,") cancel on  sale.organization_id=cancel.organization_id group by parent_id union all select organization_id,sum(sale_net) as sale ,sum(ref_net_amt) as cancel from st_rep_cs_agent where organization_id=",agtOrgId," and category_id = ",catId," and finaldate>=date('",startDate,"') and finaldate<=date('",endDate,"') group by organization_id) as main group by main.parent_id");
      
PREPARE stmt FROM @mainQry;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
    END    
#
CREATE      PROCEDURE `csRetReporting`(startDate timestamp,endDate timestamp)
BEGIN
	DECLARE done INT DEFAULT 0;
	DECLARE productId int;
	DECLARE  gameCur CURSOR FOR select category_id from st_cs_product_category_master;
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;
	OPEN gameCur;
	  read_loop: LOOP
	    FETCH gameCur INTO productId;
	    IF done THEN
	      LEAVE read_loop;
	    END IF;
	    
		set @saleQry=concat("insert into st_rep_cs_retailer(organization_id, parent_id,finaldate,category_id,sale_mrp,sale_comm,sale_net,sale_good_cause,sale_vat,sale_jv_comm,sale_jv_net_amt) select organization_id,parent_id,alldate,",productId," category_id,ifnull(saleMrp,0.00) saleMrp,ifnull(retComm,0.00) retComm,ifnull(saleNet,0.00) saleNet,ifnull(govtComm,0.00) govtComm,ifnull(vatComm,0.00) vatComm,ifnull(jvComm,0.00) jvComm,ifnull(jvNetAmt,0.00) jvNetAmt from (select organization_id,parent_id,date(alldate) alldate from st_lms_organization_master,tempdate where organization_type='RETAILER') om left outer join (select drs.retailer_org_id,date(transaction_date) tx_date,sum(mrp_amt) saleMrp,sum(retailer_comm) retComm ,sum(net_amt) saleNet,sum(govt_comm) govtComm,sum(vat) vatComm,sum(jv_comm) jvComm,sum(jv_comm_amt) jvNetAmt from st_cs_sale_",productId," drs inner join st_lms_retailer_transaction_master rtm on drs.transaction_id=rtm.transaction_id where rtm.transaction_date>='",startDate,"' and rtm.transaction_date<='",endDate,"' and transaction_type='CS_SALE' group by retailer_org_id,tx_date) sale on organization_id=retailer_org_id and alldate=tx_date");
		PREPARE stmt FROM @saleQry;
		EXECUTE stmt;
		DEALLOCATE PREPARE stmt;
		set @cancelQry=concat("update ( select drs.retailer_org_id,date(transaction_date) tx_date,sum(mrp_amt) saleRefMrp,sum(retailer_comm) retCommRef ,sum(net_amt) saleRefNet,sum(govt_comm) govtCommRef,sum(vat) vatAmtRef,sum(jv_comm) jvComm,sum(jv_comm_cost) jvNetAmt from st_cs_refund_",productId," drs inner join st_lms_retailer_transaction_master rtm on drs.transaction_id=rtm.transaction_id where rtm.transaction_date>='",startDate,"' and rtm.transaction_date<='",endDate,"' and transaction_type='CS_CANCEL_SERVER'  group by retailer_org_id,tx_date ) cancel,st_rep_cs_retailer cs set ref_sale_mrp=saleRefMrp,ref_comm=retCommRef,ref_net_amt=saleRefNet,ref_good_cause=govtCommRef,ref_vat=vatAmtRef,ref_jv_comm=jvComm,ref_jv_net_amt=jvNetAmt  where cancel.retailer_org_id=cs.organization_id and cs.finaldate=cancel.tx_date and cs.category_id=",productId,"
");
		PREPARE stmt FROM @cancelQry;
		EXECUTE stmt;
		DEALLOCATE PREPARE stmt;
	END LOOP;
	CLOSE gameCur;
    END      
#
CREATE      PROCEDURE `csSaleAgentWise`(startDate Timestamp, endDate Timestamp)
BEGIN
DECLARE done INT DEFAULT 0;
DECLARE catId INT;
DECLARE catCur CURSOR FOR select category_id from st_cs_product_category_master;
DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;

set @subqry :=(select value  from st_lms_property_master  where property_code='ORG_LIST_TYPE');
		set @selCol = 'agm.name orgCode';
		if(@subqry='CODE') then 
			set @selCol='agm.org_code orgCode' ;
		end if;
		if (@subqry='CODE_NAME') then 
			set @selCol='concat(agm.org_code,"_",agm.name)orgCode';
		end if;
		if (@subqry='NAME_CODE') then 
		  	set @selCol='concat(agm.name,"_",agm.org_code)orgCode';
		end if; 
		set @subqry1 :=(select value  from st_lms_property_master  where property_code='ORG_LIST_ORDER');
		set @selColOrder = 'orgCode ASC';
		if(@subqry1='ORG_ID') then 
			set @selColOrder='agm.organization_id' ;
		end if;
		if (@subqry1='DESC') then 
			set @selColOrder='orgCode DESC';
		end if;

set @mainQry= concat("select agm.organization_id,",@selCol,",sum(mrpAmt)mrp,sum(buyCostAmt)buyCost,sum(netAmt)netAmt from st_lms_organization_master agm ,(");
	OPEN catCur;
	  read_loop: LOOP
	    FETCH catCur INTO catId;
	    IF done THEN
	      LEAVE read_loop;
	    END IF;
		set @mainQry=concat(@mainQry,"select parent_id organization_id,sum(mrpAmt) mrpAmt,sum(buyCostAmt) buyCostAmt,sum(netAmt) netAmt from st_lms_organization_master ,(select sale.retailer_org_id,(ifnull(mrpAmt,0.0)-ifnull(mrpAmtRet,0.0))mrpAmt,(ifnull(buyCost,0.0)-ifnull(buyCostRet,0.0))buyCostAmt,(ifnull(netAmt,0.0)-ifnull(netAmtRet,0.0))netAmt from (select retailer_org_id,sum(mrp_amt) mrpAmt,sum(mrp_amt - (mrp_amt*jv_comm/100)) buyCost, sum(agent_net_amt) netAmt from st_cs_sale_", catId ," where transaction_id in(select transaction_id from st_lms_retailer_transaction_master where transaction_type ='CS_SALE' and transaction_date>='",startDate,"' and transaction_date<='",endDate,"') group by retailer_org_id) sale left outer join (select retailer_org_id,sum(mrp_amt) mrpAmtRet,sum(mrp_amt - (mrp_amt*jv_comm/100)) buyCostRet, sum(agent_net_amt)netAmtRet from st_cs_refund_",catId," where transaction_id in(select transaction_id from st_lms_retailer_transaction_master where transaction_type in('CS_CANCEL_SERVER','CS_CANCEL_RET') and transaction_date>='",startDate,"' and transaction_date<='",endDate,"' ) group by retailer_org_id)saleRet on sale.retailer_org_id=saleRet.retailer_org_id)cs where cs.retailer_org_id=organization_id group by parent_id union all ");
		END LOOP;
		CLOSE catCur;
 set @mainQry=TRIM(TRAILING 'union all ' FROM @mainQry);
 set @mainQry=concat(@mainQry," union all select organization_id,ifnull(sum(sale_mrp-ref_sale_mrp),0) mrpAmt,ifnull(sum(sale_jv_net_amt-ref_jv_net_amt),0) buyCost, ifnull(sum(sale_net-ref_net_amt),0)netAmt  from st_rep_cs_agent where finaldate>='",startDate,"' and finaldate<='",endDate,"' group by organization_id");
 set @mainQry = concat(@mainQry,") saleTlb where saleTlb.organization_id = agm.organization_id and agm.organization_type='AGENT' group by agm.organization_id order by  ",@selColOrder,"");
	
 PREPARE stmt FROM @mainQry;
	  EXECUTE stmt;
	  DEALLOCATE PREPARE stmt;
    END     
#
CREATE      PROCEDURE `csSaleCategoryWise`(startDate Timestamp , endDate Timestamp)
BEGIN
DECLARE done INT DEFAULT 0;
DECLARE catId INT;
DECLARE catCur CURSOR FOR select category_id from st_cs_product_category_master;
DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;
set @mainQry = "select opcm.category_id,opcm.category_code,mrp,buyCost from st_cs_product_category_master opcm, (select category_id,sum(mrp) mrp,sum(buyCost) buyCost from(";
 set @mainQry= concat(@mainQry,"select pcm.category_id,sum(mrpAmt) mrp,sum(buyCostAmt) buyCost from st_cs_product_category_master pcm ,st_cs_product_master pm,(");
	OPEN catCur;
	  read_loop: LOOP
	    FETCH catCur INTO catId;
	    IF done THEN
	      LEAVE read_loop;
	    END IF;
		set @mainQry=concat(@mainQry,"select sale.product_id,(ifnull(mrpAmt,0.0)-ifnull(mrpAmtRet,0.0))mrpAmt,(ifnull(buyCost,0.0)-ifnull(buyCostRet,0.0))buyCostAmt from (select product_id,sum(mrp_amt) mrpAmt,sum(mrp_amt - (mrp_amt*jv_comm/100)) buyCost from st_cs_sale_",catId," where transaction_id in(select transaction_id from st_lms_retailer_transaction_master where transaction_type ='CS_SALE' and transaction_date>='"
				,startDate,"' and transaction_date<='",endDate,"' ) group by product_id) sale left outer join (select product_id,sum(mrp_amt) mrpAmtRet,sum(mrp_amt - (mrp_amt*jv_comm/100)) buyCostRet from st_cs_refund_",catId," where transaction_id in(select transaction_id from st_lms_retailer_transaction_master where transaction_type in('CS_CANCEL_SERVER','CS_CANCEL_RET') and transaction_date>='"
				,startDate,"' and transaction_date<='",endDate,"' ) group by product_id)saleRet on sale.product_id=saleRet.product_id union all ");
	END LOOP;
  CLOSE catCur;
 set @mainQry=TRIM(TRAILING 'union all ' FROM @mainQry);
set @mainQry=concat(@mainQry,") saleTlb where pm.product_id=saleTlb.product_id and pcm.category_id = pm.category_id group by category_id");
  set @mainQry=concat(@mainQry," union all select category_id,ifnull(sum(sale_mrp-ref_sale_mrp),0) mrp,ifnull(sum(sale_jv_net_amt-ref_jv_net_amt),0) buyCost from st_rep_cs_retailer where finaldate>='",startDate,"' and finaldate<='",endDate,"'  group by category_id) aa group by aa.category_id)totCat where totCat.category_id = opcm.category_id");
  
 PREPARE stmt FROM @mainQry;
	  EXECUTE stmt;
	  DEALLOCATE PREPARE stmt;
    END 
#
CREATE      PROCEDURE `csSaleProductwise`(startDate Timestamp, endDate Timestamp, catId int)
BEGIN
set @mainQry="select saleTlb.product_id,pcm.category_id,product_code,pm.description,operator_name,denomination,mrpAmt,buyCostAmt from st_cs_product_category_master pcm ,st_cs_product_master pm, st_cs_operator_master com,(";
	set @mainQry=concat(@mainQry,"select sale.product_id,(ifnull(mrpAmt,0.0)-ifnull(mrpAmtRet,0.0))mrpAmt,(ifnull(buyCost,0.0)-ifnull(buyCostRet,0.0))buyCostAmt from (select product_id,sum(mrp_amt) mrpAmt,sum(mrp_amt - (mrp_amt*jv_comm/100)) buyCost from st_cs_sale_",catId," where transaction_id in(select transaction_id from st_lms_retailer_transaction_master where transaction_type ='CS_SALE' and transaction_date>='",startDate,"' and transaction_date<='",endDate,"') group by product_id) sale left outer join (select product_id,sum(mrp_amt) mrpAmtRet,sum(mrp_amt - (mrp_amt*jv_comm/100)) buyCostRet from st_cs_refund_",catId," where transaction_id in(select transaction_id from st_lms_retailer_transaction_master where transaction_type in('CS_CANCEL_SERVER','CS_CANCEL_RET') and transaction_date>='"	,startDate,"' and transaction_date<='",endDate,"') group by product_id)saleRet on sale.product_id=saleRet.product_id");
set @mainQry=concat(@mainQry,") saleTlb where pm.product_id=saleTlb.product_id and pcm.category_id = pm.category_id and pm.operator_code = com.operator_code and pcm.category_id =",catId);
 
   
 PREPARE stmt FROM @mainQry;
	  EXECUTE stmt;
	  DEALLOCATE PREPARE stmt;
    END 
#
CREATE      PROCEDURE `csSaleProductWiseAgentwise`(startDate Timestamp, endDate Timestamp, agtOrgId int)
BEGIN
DECLARE done INT DEFAULT 0;
DECLARE catId INT;
DECLARE catCur CURSOR FOR select category_id from st_cs_product_category_master;
DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;
set @mainQry= "select saleTlb.product_id,pm.product_id,product_code,pm.description,operator_name,denomination,sum(mrpAmt)mrp,sum(buyCostAmt)buyCost,sum(netAmt)net from st_cs_product_master pm, st_cs_operator_master com,(";
	OPEN catCur;
	  read_loop: LOOP
	    FETCH catCur INTO catId;
	    IF done THEN
	      LEAVE read_loop;
	    END IF;
		set @mainQry=concat(@mainQry,"select sale.product_id,(ifnull(mrpAmt,0.0)-ifnull(mrpAmtRet,0.0))mrpAmt,(ifnull(buyCost,0.0)-ifnull(buyCostRet,0.0))buyCostAmt,(ifnull(netAmt,0.0)-ifnull(netAmtRet,0.0))netAmt from (select product_id,sum(mrp_amt) mrpAmt,sum(mrp_amt - (mrp_amt*jv_comm/100)) buyCost, sum(agent_net_amt)netAmt from st_cs_sale_",catId," cs, st_lms_organization_master rtm where transaction_id in(select transaction_id from st_lms_retailer_transaction_master where transaction_type ='CS_SALE' and transaction_date>='",startDate,"' and transaction_date<='",endDate,"') and cs.retailer_org_id = rtm.organization_id and rtm.parent_id = ",agtOrgId," and rtm.organization_type = 'RETAILER'  group by product_id) sale left outer join (select product_id,sum(mrp_amt) mrpAmtRet,sum(mrp_amt - (mrp_amt*jv_comm)/100) buyCostRet, sum(agent_net_amt)netAmtRet from st_cs_refund_",catId," csret, st_lms_organization_master rtm where transaction_id in(select transaction_id from st_lms_retailer_transaction_master where transaction_type in('CS_CANCEL_SERVER','CS_CANCEL_RET') and transaction_date>='",startDate,"' and transaction_date<='",endDate,"' )and csret.retailer_org_id = rtm.organization_id and rtm.parent_id = ",agtOrgId," and rtm.organization_type = 'RETAILER' group by product_id)saleRet on sale.product_id=saleRet.product_id union all ");
		END LOOP;
		CLOSE catCur;
 set @mainQry=TRIM(TRAILING 'union all ' FROM @mainQry);
 set @mainQry = concat(@mainQry,") saleTlb where pm.product_id=saleTlb.product_id and pm.operator_code=com.operator_code group by saleTlb.product_id");
 
	
 PREPARE stmt FROM @mainQry;
	  EXECUTE stmt;
	  DEALLOCATE PREPARE stmt;
    END
#
CREATE      PROCEDURE `csSaleProductWiseRetailerWise`(startDate Timestamp, endDate Timestamp, retOrgId int)
BEGIN
DECLARE done INT DEFAULT 0;
DECLARE catId INT;
DECLARE catCur CURSOR FOR select category_id from st_cs_product_category_master;
DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;
set @mainQry= "select saleTlb.product_id,pm.product_id,product_code,operator_name,pm.description,denomination,sum(mrpAmt)mrp,sum(buyCostAmt)buyCost,sum(netAmt)net from st_cs_product_master pm,st_cs_operator_master com,(";
	OPEN catCur;
	  read_loop: LOOP
	    FETCH catCur INTO catId;
	    IF done THEN
	      LEAVE read_loop;
	    END IF;
		set @mainQry=concat(@mainQry,"select sale.product_id,(ifnull(mrpAmt,0.0)-ifnull(mrpAmtRet,0.0))mrpAmt,(ifnull(buyCost,0.0)-ifnull(buyCostRet,0.0))buyCostAmt,(ifnull(netAmt,0.0)-ifnull(netAmtRet,0.0))netAmt from (select product_id,sum(mrp_amt) mrpAmt,sum(mrp_amt - (mrp_amt*jv_comm/100)) buyCost, sum(agent_net_amt)netAmt from st_cs_sale_",catId," cs where transaction_id in(select transaction_id from st_lms_retailer_transaction_master where transaction_type ='CS_SALE' and transaction_date>='", startDate,"' and transaction_date<='",endDate,"' ) and cs.retailer_org_id = ",retOrgId,"  group by product_id) sale left outer join (select product_id,sum(mrp_amt) mrpAmtRet,sum(mrp_amt - (mrp_amt*jv_comm)/100) buyCostRet, sum(agent_net_amt)netAmtRet from st_cs_refund_",catId," csret where transaction_id in(select transaction_id from st_lms_retailer_transaction_master where transaction_type in('CS_CANCEL_SERVER','CS_CANCEL_RET') and transaction_date>='",startDate,"' and transaction_date<='",endDate,"' )and csret.retailer_org_id = ",retOrgId," group by product_id)saleRet on sale.product_id=saleRet.product_id union all ");
		END LOOP;
		CLOSE catCur;
 set @mainQry=TRIM(TRAILING 'union all ' FROM @mainQry);
 set @mainQry = concat(@mainQry,") saleTlb where pm.product_id=saleTlb.product_id and pm.operator_code = com.operator_code group by saleTlb.product_id");
 
 
	
 PREPARE stmt FROM @mainQry;
	  EXECUTE stmt;
	  DEALLOCATE PREPARE stmt;
    END      
#
CREATE      PROCEDURE `csSaleRetailerWise`(startDate TIMESTAMP, endDate TIMESTAMP, agtOrgId INT)
BEGIN
DECLARE done INT DEFAULT 0;
DECLARE catId INT;
DECLARE catCur CURSOR FOR select category_id from st_cs_product_category_master;
DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;

IF(agtOrgId=-2) then
set @agtOrgCondition=concat(" ");
ELSE
set @agtOrgCondition=concat("  and parent_id='",agtOrgId,"'");
end if ;

set @mainQry= "select rtm.organization_id,rtm.name,sum(mrpAmt)mrp,sum(buyCost)buyCost,sum(netAmt)net from st_lms_organization_master rtm,(";
	OPEN catCur;
	  read_loop: LOOP
	    FETCH catCur INTO catId;
	    IF done THEN
	      LEAVE read_loop;
	    END IF;
		set @mainQry=concat(@mainQry,"select sale.retailer_org_id,(ifnull(mrpAmt,0.0)-ifnull(mrpAmtRet,0.0))mrpAmt,(ifnull(buyCost,0.0)-ifnull(buyCostRet,0.0))buyCost,(ifnull(netAmt,0.0)-ifnull(netAmtRet,0.0))netAmt  from (select retailer_org_id,sum(mrp_amt) mrpAmt,sum(mrp_amt - (mrp_amt*jv_comm/100)) buyCost, sum(net_Amt) netAmt from st_cs_sale_",catId," where transaction_id in(select transaction_id from st_lms_retailer_transaction_master where transaction_type ='CS_SALE' and transaction_date>='", startDate,"' and transaction_date<='",endDate,"' ) group by retailer_org_id) sale left outer join (select retailer_org_id,sum(mrp_amt) mrpAmtRet,sum(mrp_amt - (mrp_amt*jv_comm/100)) buyCostRet, sum(net_amt)netAmtRet from st_cs_refund_",catId," where transaction_id in(select transaction_id from st_lms_retailer_transaction_master where transaction_type in('CS_CANCEL_SERVER','CS_CANCEL_RET') and transaction_date>='",startDate,"' and transaction_date<='",endDate,"' ) group by retailer_org_id)saleRet on sale.retailer_org_id=saleRet.retailer_org_id union all ");
		END LOOP;
		CLOSE catCur;
 set @mainQry=TRIM(TRAILING 'union all ' FROM @mainQry);
 set @mainQry=concat(@mainQry," union all select organization_id retailer_org_id,ifnull(sum(sale_mrp-ref_sale_mrp),0) mrpAmt,ifnull(sum(sale_jv_net_amt-ref_jv_net_amt),0) buyCost, ifnull(sum(sale_net-ref_net_amt),0)netAmt  from st_rep_cs_retailer where finaldate>='",startDate,"' and finaldate<='",endDate,"'",@agtOrgCondition," group by organization_id");
 set @mainQry = concat(@mainQry,") saleTlb where rtm.organization_type='RETAILER' and saleTlb.retailer_org_id = rtm.organization_id ",@agtOrgCondition," group by saleTlb.retailer_org_id");
	

 PREPARE stmt FROM @mainQry;
	  EXECUTE stmt;
	  DEALLOCATE PREPARE stmt;
    END      
#
CREATE      PROCEDURE `DirPlayerpwtPaymentDateWisePerGame`(startDate timestamp,endDate timestamp,agtOrgId int)
BEGIN
set @mainQry=concat("select date(transaction_date) date,agent_org_id,sum(pwt_amt+agt_claim_comm) pwtDir from st_dg_agt_direct_plr_pwt where transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and agent_org_id=",agtOrgId," group by date(transaction_date)
union all select finaldate,organization_id,sum(direct_pwt_net_amt) as pwtDir  from st_rep_dg_agent where  finaldate>='",startDate,"' and finaldate<='",endDate,"' and organization_id=",agtOrgId," group by finaldate ");
PREPARE stmt FROM @mainQry;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
    END      
#
CREATE      PROCEDURE `drawAgentDirPlyPwtGameWise`(startDate timestamp,endDate timestamp,agtOrgId int)
BEGIN
select gm.game_id gameId, game_name gameName,sum(mrpAmt) mrpAmt from st_dg_game_master gm,(select game_id,sum(pwt_amt) mrpAmt from st_dg_agt_direct_plr_pwt where transaction_date>=startDate and transaction_date<=endDate and agent_org_id=agtOrgId group by game_id union all select game_id,sum(direct_pwt_amt) mrpAmt from st_rep_dg_agent where finaldate>=startDate and finaldate<=endDate and organization_id=agtOrgId group by game_id)pwtTlb where gm.game_id=pwtTlb.game_id group by pwtTlb.game_id;
    END      
#
CREATE      PROCEDURE `drawAgentDirPlyPwtGameWiseExpand`(startDate timestamp,endDate timestamp,agtOrgId int)
BEGIN
select gm.game_id gameId, game_name gameName,priceAmt,noOfTkt,mrpAmt from st_dg_game_master gm,(select game_id,pwt_amt priceAmt,count(pwt_amt) noOfTkt,sum(pwt_amt) mrpAmt from st_dg_agt_direct_plr_pwt where transaction_date>=startDate and transaction_date<=endDate and agent_org_id=agtOrgId group by pwt_amt,game_id) pwtTlb where gm.game_id=pwtTlb.game_id order by game_nbr,priceAmt;  
  END
#

CREATE      PROCEDURE `drawBODirPlyPwtGameWise`(startDate timestamp,endDate timestamp)
BEGIN
select gm.game_id gameId, game_name gameName,sum(mrpAmt) mrpAmt from st_dg_game_master gm,(select game_id,sum(pwt_amt) mrpAmt from st_dg_bo_direct_plr_pwt where transaction_date>=startDate and transaction_date<=endDate group by game_id union all select game_id,sum(direct_pwt_amt) mrpAmt from st_rep_dg_bo where finaldate>=startDate and finaldate<=endDate  group by game_id)pwtTlb where gm.game_id=pwtTlb.game_id group by pwtTlb.game_id;
END
#

CREATE      PROCEDURE `drawBODirPlyPwtGameWiseExpand`(startDate timestamp,endDate timestamp)
BEGIN
select gm.game_id gameId, game_name gameName,priceAmt,noOfTkt,mrpAmt from st_dg_game_master gm,(select game_id,pwt_amt priceAmt,count(pwt_amt) noOfTkt,sum(pwt_amt) mrpAmt from st_dg_bo_direct_plr_pwt where transaction_date>=startDate and transaction_date<=endDate group by pwt_amt,game_id) pwtTlb where gm.game_id=pwtTlb.game_id order by game_nbr,priceAmt;
    END
#
CREATE      PROCEDURE `drawCollectionAgentWisePerGame`(startDate timestamp,endDate timestamp,gameNo int)
BEGIN
        
         
         
      set @mainQry=concat("select parent_id,sum(main.sale) as sale ,sum(main.cancel)as cancel,sum(main.pwt)as pwt,sum(main.pwtDir)as pwtDir from (select parent_id,sale,cancel,pwt,ifnull(pwtDir,0.0) pwtDir from (select sale.parent_id,sum(sale) sale,sum(cancel) cancel,sum(pwt) pwt from (select organization_id,parent_id,ifnull(sale,0.0) sale from st_lms_organization_master om left outer join (select drs.retailer_org_id,sum(agent_net_amt) as sale from st_dg_ret_sale_",gameNo," drs inner join st_lms_retailer_transaction_master rtm on drs.transaction_id=rtm.transaction_id where rtm.transaction_type in('DG_SALE','DG_SALE_OFFLINE') and rtm.transaction_date>='",startDate,"' and rtm.transaction_date<='",endDate,"' group by drs.retailer_org_id) sale on om.organization_id=retailer_org_id where om.organization_type='RETAILER') sale inner join (select organization_id,parent_id,ifnull(cancel,0.0) cancel from st_lms_organization_master om left outer join (select drs.retailer_org_id,sum(agent_net_amt) as cancel from st_dg_ret_sale_refund_",gameNo," drs inner join st_lms_retailer_transaction_master rtm on drs.transaction_id=rtm.transaction_id where transaction_type in('DG_REFUND_CANCEL','DG_REFUND_FAILED') and transaction_date>='",startDate,"' and transaction_date<='",endDate,"' group by drs.retailer_org_id) cancel on om.organization_id=retailer_org_id where om.organization_type='RETAILER') cancel inner join (select organization_id,parent_id,ifnull(pwt,0.0) pwt from st_lms_organization_master om left outer join (select drs.retailer_org_id,sum(pwt_amt+agt_claim_comm) as pwt from st_dg_ret_pwt_",gameNo," drs inner join st_lms_retailer_transaction_master rtm on drs.transaction_id=rtm.transaction_id where transaction_type in('DG_PWT_AUTO','DG_PWT_PLR','DG_PWT') and transaction_date>='",startDate,"' and transaction_date<='",endDate,"' group by drs.retailer_org_id) pwt on om.organization_id=retailer_org_id where om.organization_type='RETAILER') pwt on  sale.organization_id=cancel.organization_id and sale.organization_id=pwt.organization_id and pwt.organization_id=cancel.organization_id group by parent_id) aa left outer join (select organization_id,sum(pwt_amt+agt_claim_comm) pwtDir from st_lms_organization_master left outer join st_dg_agt_direct_plr_pwt on organization_id=agent_org_id where transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and organization_type='AGENT' and game_id=",gameNo," group by agent_org_id) bb on parent_id=organization_id union all select organization_id,sum(sale_net) as sale ,sum(ref_net_amt) as cancel , sum(pwt_net_amt) as pwt,sum(direct_pwt_net_amt) as pwtDir from st_rep_dg_agent where game_id=",gameNo," and finaldate>=date('",startDate,"') and finaldate<=date('",endDate,"') group by organization_id) as main group by main.parent_id");
       
PREPARE stmt FROM @mainQry;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
    END
#
CREATE      PROCEDURE `drawCollectionDayWisePerGame`(startDate timestamp,endDate timestamp,gameNo int,agtOrgId int,viewBy varchar(20))
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
	
 
 set @mainQry=concat("select main.alldate,sum(main.sale)as sale,sum(main.cancel)as cancel,sum(main.pwt)as pwt,sum(main.pwtDir)as pwtDir from (select date(alldate) alldate,sale,cancel,pwt,ifnull(pwtDir,0.0) pwtDir from (select sale.alldate,ifnull(sale,0.0) sale,ifnull(cancel,0.0) cancel,ifnull(pwt,0.0) pwt from (select alldate,sale from tempdate left outer join (select date(transaction_date) tx_date,sum(",@dgSaleCol,") sale from st_dg_ret_sale_",gameNo," drs,st_lms_retailer_transaction_master rtm where drs.transaction_id=rtm.transaction_id and rtm.transaction_date>='",startDate,"' and rtm.transaction_date<='",endDate,"' and transaction_type in('DG_SALE','DG_SALE_OFFLINE') ",@addDrawQry," group by tx_date) sale on alldate=sale.tx_date) sale inner join (select alldate,cancel from tempdate left outer join (select date(transaction_date) tx_date,sum(",@dgSaleCol,") cancel from st_dg_ret_sale_refund_",gameNo," drs,st_lms_retailer_transaction_master rtm where drs.transaction_id=rtm.transaction_id and rtm.transaction_date>='",startDate,"' and rtm.transaction_date<='",endDate,"' and transaction_type in('DG_REFUND_CANCEL','DG_REFUND_FAILED') ",@addDrawQry," group by tx_date) cancel on alldate=cancel.tx_date) cancel inner join (select alldate,pwt from tempdate left outer join (select date(transaction_date) tx_date,sum(",@dgPwtCol,") pwt from st_dg_ret_pwt_",gameNo," drs,st_lms_retailer_transaction_master rtm where drs.transaction_id=rtm.transaction_id and rtm.transaction_date>='",startDate,"' and rtm.transaction_date<='",endDate,"' and transaction_type in('DG_PWT_AUTO','DG_PWT_PLR','DG_PWT') ",@addDrawQry," group by tx_date) pwt on alldate=pwt.tx_date) pwt on sale.alldate=cancel.alldate and sale.alldate=pwt.alldate and cancel.alldate=pwt.alldate) a left outer join (select date(transaction_date) tx_date ,sum(pwt_amt+agt_claim_comm) pwtDir from st_dg_agt_direct_plr_pwt where transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and game_id=",gameNo," ",@addDirPlrQry," group by tx_date) b on alldate=tx_date  ");
if(viewBy='Agent' and agtOrgId!=0) then
		set @mainQry=concat(@mainQry," union all select dgr.finaldate as alldate,sum(dgr.sale_net) as sale ,sum(dgr.ref_net_amt) cancel, sum(dgr.pwt_net_amt) pwt,sum(ifnull(dga.direct_pwt_net_amt,0.00))as pwtDir  from st_rep_dg_retailer as dgr left outer join  st_rep_dg_agent as dga on dgr.finaldate=dga.finaldate and dgr.parent_id=dga.parent_id where dgr.parent_id=",agtOrgId," and dgr.game_id=",gameNo," and dgr.finaldate>=date('",startDate,"') and dgr.finaldate<=date('",endDate,"') group by dgr.finaldate)as main group by main.alldate ");
	else
		set @mainQry=concat(@mainQry," union all select finaldate as alldate,sum(sale_net) as sale,sum(ref_net_amt) as cancel, sum(pwt_net_amt) as pwt, sum(direct_pwt_net_amt) as pwtDir from st_rep_dg_agent where game_id=",gameNo," and finaldate>=date('",startDate,"') and finaldate<=date('",endDate,"') group by finaldate)as main group by main.alldate  ");
	end if;
	
 
 PREPARE stmt FROM @mainQry;
 EXECUTE stmt;
 DEALLOCATE PREPARE stmt;
    END
#
CREATE      PROCEDURE `drawCollectionRetailerWisePerGame`(startDate timestamp,endDate timestamp,gameNo int,parentOrgId int)
BEGIN
       
        
        
 
         if(parentOrgId!=0) then
	  set @addQry=concat("and om.parent_id=",parentOrgId,"");	
	else
	set @addQry="";
	end if;
         set @mainQry=concat("select main.organization_id,sum(main.sale)as sale,sum(main.cancel)as cancel,sum(main.pwt)as pwt from (select sale.organization_id,sale,cancel,pwt from (select organization_id,ifnull(sale,0.0) sale from st_lms_organization_master om left outer join (select drs.retailer_org_id,sum(net_amt) as sale from st_dg_ret_sale_",gameNo," drs inner join st_lms_retailer_transaction_master rtm on drs.transaction_id=rtm.transaction_id where rtm.transaction_type in('DG_SALE','DG_SALE_OFFLINE') and rtm.transaction_date>='",startDate,"' and rtm.transaction_date<='",endDate,"' group by drs.retailer_org_id) sale on om.organization_id=retailer_org_id where om.organization_type='RETAILER' ",@addQry,") sale inner join (select organization_id,ifnull(cancel,0.0) cancel from st_lms_organization_master om left outer join (select drs.retailer_org_id,sum(net_amt) as cancel from st_dg_ret_sale_refund_",gameNo," drs inner join st_lms_retailer_transaction_master rtm on drs.transaction_id=rtm.transaction_id where transaction_type in('DG_REFUND_CANCEL','DG_REFUND_FAILED') and transaction_date>='",startDate,"' and transaction_date<='",endDate,"' group by drs.retailer_org_id) cancel on om.organization_id=retailer_org_id where om.organization_type='RETAILER'  ",@addQry,") cancel inner join (select organization_id,ifnull(pwt,0.0) pwt from st_lms_organization_master om left outer join (select drs.retailer_org_id,sum(pwt_amt+retailer_claim_comm) as pwt from st_dg_ret_pwt_",gameNo," drs inner join st_lms_retailer_transaction_master rtm on drs.transaction_id=rtm.transaction_id where transaction_type in('DG_PWT_AUTO','DG_PWT_PLR','DG_PWT') and transaction_date>='",startDate,"' and transaction_date<='",endDate,"' group by drs.retailer_org_id) pwt on om.organization_id=retailer_org_id where om.organization_type='RETAILER'  ",@addQry,") pwt on  sale.organization_id=cancel.organization_id and sale.organization_id=pwt.organization_id and pwt.organization_id=cancel.organization_id union all select dgr.organization_id as organization_id,sum(dgr.sale_net) as sale ,sum(dgr.ref_net_amt) cancel, sum(dgr.pwt_net_amt) pwt  from  st_rep_dg_retailer as dgr  where  dgr.game_id=",gameNo," and dgr.parent_id=",parentOrgId," and dgr.finaldate>=date('",startDate,"') and dgr.finaldate<=date('",endDate,"') group by dgr.organization_id)as main group by main.organization_id ");
       
      
 
            
PREPARE stmt FROM @mainQry;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
    END      
#
CREATE      PROCEDURE `drawGameAgtReporting`(startDate timestamp,endDate timestamp)
BEGIN
    DECLARE done INT DEFAULT 0;
    DECLARE gameId int;
    declare saleResult int;
    declare cancelResult int;
    declare pwtResult int;
    set @saleQry=concat("insert into st_rep_dg_agent(organization_id, parent_id,finaldate,game_id,sale_mrp,sale_comm,sale_net,sale_good_cause,sale_vat,sale_taxable) select organization_id,parent_id,alldate,om.game_id,ifnull(saleMrp,0.00) saleMrp,ifnull(agtComm,0.00) agtComm,ifnull(saleNet,0.00) saleNet,ifnull(govtComm,0.00) govtComm,ifnull(vatAmt,0.00) vatAmt,ifnull(taxableSale,0.00) taxableSale from (select organization_id,parent_id,date(alldate) alldate,game_id from st_lms_organization_master,tempdate,st_dg_game_master where organization_type='AGENT') om left outer join (select drs.agent_org_id,date(transaction_date) tx_date,game_id,sum(mrp_amt) saleMrp,sum(agent_comm) agtComm ,sum(bo_net_amt) saleNet,sum(govt_comm) govtComm,sum(vat_amt) vatAmt,sum(taxable_sale) taxableSale from st_dg_agt_sale drs,st_lms_agent_transaction_master rtm where drs.transaction_id=rtm.transaction_id and rtm.transaction_date>='",startDate,"' and rtm.transaction_date<='",endDate,"' and transaction_type in('DG_SALE','DG_SALE_OFFLINE')  group by agent_org_id,tx_date,game_id) sale on organization_id=agent_org_id and alldate=tx_date and sale.game_id=om.game_id");
        PREPARE stmt FROM @saleQry;
        EXECUTE stmt;
        DEALLOCATE PREPARE stmt;
    DROP TEMPORARY TABLE IF EXISTS table3;
    set @tab1 = concat("CREATE TEMPORARY TABLE table3(agent_org_id int(10),tx_date date,game_id int(10),saleRefMrp decimal(20,2),agtCommRef decimal(20,2),saleRefNet decimal(20,2),govtCommRef decimal(20,2),vatAmtRef decimal(20,2),taxableSaleRef decimal(20,2),primary key(agent_org_id,tx_date,game_id)) AS select drs.agent_org_id,date(transaction_date) tx_date,game_id,sum(mrp_amt) saleRefMrp,sum(agent_comm) agtCommRef ,sum(bo_net_amt) saleRefNet,sum(govt_comm) govtCommRef,sum(vat_amt) vatAmtRef,sum(taxable_sale) taxableSaleRef from st_dg_agt_sale_refund drs,st_lms_agent_transaction_master rtm where drs.transaction_id=rtm.transaction_id and rtm.transaction_date>='",startDate,"' and rtm.transaction_date<='",endDate,"' and transaction_type in('DG_REFUND_CANCEL','DG_REFUND_FAILED')  group by agent_org_id,tx_date,game_id");
select @tab1;
        PREPARE stmt FROM @tab1;
        EXECUTE stmt;
        DEALLOCATE PREPARE stmt;
    select @tab1;
        set @cancelQry=concat("update table3 cancel,st_rep_dg_agent dg set ref_sale_mrp=saleRefMrp,ref_comm=agtCommRef,ref_net_amt=saleRefNet,ref_good_cause=govtCommRef,ref_vat=vatAmtRef,ref_taxable=taxableSaleRef  where cancel.agent_org_id=dg.organization_id and dg.finaldate=cancel.tx_date and dg.game_id=cancel.game_id");
        PREPARE stmt FROM @cancelQry;
        EXECUTE stmt;
        DEALLOCATE PREPARE stmt;
    DROP TEMPORARY TABLE IF EXISTS table4;
    set @tab2=concat("CREATE TEMPORARY TABLE table4(agent_org_id int(10),tx_date date,game_id int(10),pwtMrp decimal(20,2),retCommPwt decimal(20,2),pwtNet decimal(20,2),primary key(agent_org_id,tx_date,game_id)) AS select drs.agent_org_id,date(transaction_date) tx_date,game_id,sum(pwt_amt) pwtMrp,sum(agt_claim_comm) retCommPwt ,sum(pwt_amt+agt_claim_comm) pwtNet from st_dg_agt_pwt drs,st_lms_agent_transaction_master rtm where drs.transaction_id=rtm.transaction_id and rtm.transaction_date>='",startDate,"' and rtm.transaction_date<='",endDate,"' and transaction_type in('DG_PWT_AUTO','DG_PWT_PLR','DG_PWT')  group by agent_org_id,tx_date,game_id");
        PREPARE stmt FROM @tab2;
        EXECUTE stmt;
        DEALLOCATE PREPARE stmt;
        set @pwtQry=concat("update table4 pwt,st_rep_dg_agent dg set pwt_mrp=pwtMrp,ret_pwt_comm=retCommPwt,pwt_net_amt=pwtNet  where pwt.agent_org_id=dg.organization_id and dg.finaldate=pwt.tx_date and dg.game_id=pwt.game_id");
        PREPARE stmt FROM @pwtQry;
        EXECUTE stmt;
        DEALLOCATE PREPARE stmt;
set @dirPwtQry=concat("update (select agent_org_id,date(transaction_date) tx_date,game_id,sum(pwt_amt)as pwtMrp,sum(agt_claim_comm) as agtCommPwt ,sum(pwt_amt+agt_claim_comm) as pwtNet,sum(tax_amt) as pwtTax  from st_dg_agt_direct_plr_pwt pwt where transaction_date>='",startDate,"' and transaction_date<='",endDate,"' group by date(transaction_date),agent_org_id,game_id) pwt,st_rep_dg_agent dg set direct_pwt_amt=pwtMrp,direct_pwt_comm=agtCommPwt,direct_pwt_net_amt=pwtNet ,direct_pwt_tax=pwtTax where pwt.agent_org_id=dg.organization_id and dg.finaldate=pwt.tx_date and dg.game_id=pwt.game_id");
        PREPARE stmt FROM @dirPwtQry;
        EXECUTE stmt;
        DEALLOCATE PREPARE stmt;
    END      
#
CREATE      PROCEDURE `drawGameBOReporting`(startDate timestamp,endDate timestamp)
BEGIN
	DECLARE done INT DEFAULT 0;
	DECLARE gameId int;
	
	set @saleQry=concat("insert into st_rep_dg_bo(organization_id, parent_id,finaldate,game_id,sale_mrp,sale_comm,sale_net,sale_good_cause,sale_vat,sale_taxable) select organization_id,parent_id,alldate,om.game_id,ifnull(saleMrp,0.00) saleMrp,ifnull(agtComm,0.00) agtComm,ifnull(saleNet,0.00) saleNet,ifnull(govtComm,0.00) govtComm,ifnull(vatAmt,0.00) vatAmt,ifnull(taxableSale,0.00) taxableSale from (select organization_id,parent_id,date(alldate) alldate,game_id from st_lms_organization_master,tempdate,st_dg_game_master where organization_type='BO') om left outer join (select date(transaction_date) tx_date,game_id,sum(mrp_amt) saleMrp,sum(agent_comm) agtComm ,sum(net_amt) saleNet,sum(govt_comm) govtComm,sum(vat_amt) vatAmt,sum(taxable_sale) taxableSale from st_dg_bo_sale drs,st_lms_bo_transaction_master rtm where drs.transaction_id=rtm.transaction_id and rtm.transaction_date>='",startDate,"' and rtm.transaction_date<='",endDate,"' and transaction_type in('DG_SALE','DG_SALE_OFFLINE')  group by tx_date,game_id) sale on alldate=tx_date and sale.game_id=om.game_id");
		PREPARE stmt FROM @saleQry;
		EXECUTE stmt;
		DEALLOCATE PREPARE stmt;
		set @cancelQry=concat("update (select date(transaction_date) tx_date,game_id,sum(mrp_amt) saleRefMrp,sum(agent_comm) agtCommRef ,sum(net_amt) saleRefNet,sum(govt_comm) govtCommRef,sum(vat_amt) vatAmtRef,sum(taxable_sale) taxableSaleRef from st_dg_bo_sale_refund drs,st_lms_bo_transaction_master rtm where drs.transaction_id=rtm.transaction_id and rtm.transaction_date>='",startDate,"' and rtm.transaction_date<='",endDate,"' and transaction_type in('DG_REFUND_CANCEL','DG_REFUND_FAILED')  group by tx_date,game_id) cancel,st_rep_dg_bo dg set ref_sale_mrp=saleRefMrp,ref_comm=agtCommRef,ref_net_amt=saleRefNet,ref_good_cause=govtCommRef,ref_vat=vatAmtRef,ref_taxable=taxableSaleRef  where dg.finaldate=cancel.tx_date and dg.game_id=cancel.game_id");
		PREPARE stmt FROM @cancelQry;
		EXECUTE stmt;
		DEALLOCATE PREPARE stmt;
		set @pwtQry=concat("update (select date(transaction_date) tx_date,game_id,sum(pwt_amt) pwtMrp,sum(comm_amt) retCommPwt ,sum(pwt_amt+comm_amt) pwtNet from st_dg_bo_pwt drs,st_lms_bo_transaction_master rtm where drs.transaction_id=rtm.transaction_id and rtm.transaction_date>='",startDate,"' and rtm.transaction_date<='",endDate,"' and transaction_type in('DG_PWT_AUTO','DG_PWT_PLR','DG_PWT')  group by tx_date,game_id) pwt,st_rep_dg_bo dg set pwt_mrp=pwtMrp,ret_pwt_comm=retCommPwt,pwt_net_amt=pwtNet  where dg.finaldate=pwt.tx_date and dg.game_id=pwt.game_id");
		PREPARE stmt FROM @pwtQry;
		EXECUTE stmt;
		DEALLOCATE PREPARE stmt;
set @dirPwtQry=concat("update (select date(transaction_date) tx_date,game_id,sum(pwt_amt)as pwtMrp,0.00 as agtCommPwt ,sum(pwt_amt+0.00) as pwtNet,sum(tax_amt) as pwtTax  from st_dg_bo_direct_plr_pwt pwt where transaction_date>='",startDate,"' and transaction_date<='",endDate,"' group by date(transaction_date),game_id) pwt,st_rep_dg_bo dg set direct_pwt_amt=pwtMrp,direct_pwt_comm=agtCommPwt,direct_pwt_net_amt=pwtNet ,direct_pwt_tax=pwtTax where dg.finaldate=pwt.tx_date and dg.game_id=pwt.game_id");
		PREPARE stmt FROM @dirPwtQry;
		EXECUTE stmt;
		DEALLOCATE PREPARE stmt;
set @insStatus=concat("insert into archiving_status(job_name, status)values('DG_BO_REPORTING', 'DONE')");
prepare stmt from @insStatus;
execute stmt;
deallocate prepare stmt;
    END
#

CREATE      PROCEDURE `drawGameRetReporting`(startDate TIMESTAMP,endDate TIMESTAMP)
BEGIN
    DECLARE done INT DEFAULT 0;
    DECLARE gameId INT;
    DECLARE  gameCur CURSOR FOR SELECT game_id FROM st_dg_game_master;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;
    OPEN gameCur;
      read_loop: LOOP
        FETCH gameCur INTO gameId;
        IF done THEN
          LEAVE read_loop;
        END IF;
       
        SET @saleQry=CONCAT("insert into st_rep_dg_retailer(organization_id, parent_id,finaldate,game_id,sale_mrp,sale_comm,sale_net,sale_good_cause,sale_vat,sale_taxable,sale_sd,agt_vat_amt) select organization_id,parent_id,alldate,",gameId," game_id,ifnull(saleMrp,0.00) saleMrp,ifnull(retComm,0.00) retComm,ifnull(saleNet,0.00) saleNet,ifnull(govtComm,0.00) govtComm,ifnull(vatAmt,0.00) vatAmt,ifnull(taxableSale,0.00) taxableSale,ifnull(sale_sd,0.00) sale_sd,ifnull(agt_vat_amt,0.00) agt_vat_amt from (select organization_id,parent_id,date(alldate) alldate from st_lms_organization_master,tempdate where organization_type='RETAILER') om left outer join (select drs.retailer_org_id,date(transaction_date) tx_date,sum(mrp_amt) saleMrp,sum(retailer_comm) retComm ,sum(net_amt) saleNet,sum(good_cause_amt) govtComm,sum(vat_amt) vatAmt,sum(taxable_sale) taxableSale,sum(ret_sd_amt) sale_sd,sum(agt_vat_amt) agt_vat_amt  from st_dg_ret_sale_",gameId," drs,st_lms_retailer_transaction_master rtm where drs.transaction_id=rtm.transaction_id and rtm.transaction_date>='",startDate,"' and rtm.transaction_date<='",endDate,"' and transaction_type in('DG_SALE','DG_SALE_OFFLINE') group by retailer_org_id,tx_date) sale on organization_id=retailer_org_id and alldate=tx_date");
        PREPARE stmt FROM @saleQry;
        EXECUTE stmt;
        DEALLOCATE PREPARE stmt;
SET @a=CONCAT("set @saleResult=(select sum(saleNet-sale) result from (select om.parent_id,ifnull(sum(reNet),0.0)saleNet from st_lms_organization_master om left outer join (select drs.retailer_org_id,sum(net_amt) reNet from st_dg_ret_sale_",gameId," drs,st_lms_retailer_transaction_master rtm where drs.transaction_id=rtm.transaction_id and rtm.transaction_date>='",startDate,"' and rtm.transaction_date<='",endDate,"' and transaction_type in('DG_SALE','DG_SALE_OFFLINE')  group by retailer_org_id)sub on om.organization_id=sub.retailer_org_id group by parent_id)first inner join (select parent_id,sum(sale_net) as sale from st_rep_dg_retailer where game_id=",gameId," and finaldate>=date('",startDate,"') and finaldate<=date('",endDate,"') group by parent_id) second on first.parent_id=second.parent_id)");
PREPARE stmt FROM @a;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
    IF(@saleResult!=0)THEN
    CALL raise_error;
    END IF;
    DROP TEMPORARY TABLE IF EXISTS table1;
    SET @tab1 = CONCAT("CREATE TEMPORARY TABLE table1(retailer_org_id int(10),tx_date date ,saleRefMrp decimal(20,2),retCommRef decimal(20,2),saleRefNet decimal(20,2),govtCommRef decimal(20,2),vatAmtRef decimal(20,2),taxableSaleRef decimal(20,2),saleSdRef decimal(20,2),agtVatAmtRef decimal(20,2),primary key(retailer_org_id,tx_date)) AS select drs.retailer_org_id,date(transaction_date) tx_date,sum(mrp_amt) saleRefMrp,sum(retailer_comm) retCommRef ,sum(net_amt) saleRefNet,sum(good_cause_amt) govtCommRef,sum(vat_amt) vatAmtRef,sum(taxable_sale) taxableSaleRef,sum(ret_sd_amt) saleSdRef,sum(agt_vat_amt)agtVatAmtRef from st_dg_ret_sale_refund_",gameId," drs,st_lms_retailer_transaction_master rtm where drs.transaction_id=rtm.transaction_id and rtm.transaction_date>='",startDate,"' and rtm.transaction_date<='",endDate,"' and transaction_type in('DG_REFUND_CANCEL','DG_REFUND_FAILED') group by retailer_org_id,tx_date");
    PREPARE stmt FROM @tab1;
        EXECUTE stmt;
        DEALLOCATE PREPARE stmt;
    SET @cancelQry=CONCAT("update table1 cancel,st_rep_dg_retailer dg set ref_sale_mrp=saleRefMrp,ref_comm=retCommRef,ref_net_amt=saleRefNet,ref_good_cause=govtCommRef,ref_vat=vatAmtRef,ref_taxable=taxableSaleRef,ref_sd=saleSdRef,ref_agt_vat_amt=agtVatAmtRef where cancel.retailer_org_id=dg.organization_id and dg.finaldate=cancel.tx_date and game_id=",gameId,"");
        PREPARE stmt FROM @cancelQry;
        EXECUTE stmt;
        DEALLOCATE PREPARE stmt;
SET @a=CONCAT("set @cancelResult=(select sum(saleRefNet-saleRef) result from (select om.parent_id,ifnull(sum(reNet),0.0)saleRefNet from st_lms_organization_master om left outer join (select drs.retailer_org_id,sum(net_amt) reNet from st_dg_ret_sale_refund_",gameId," drs,st_lms_retailer_transaction_master rtm where drs.transaction_id=rtm.transaction_id and rtm.transaction_date>='",startDate,"' and rtm.transaction_date<='",endDate,"' and transaction_type in('DG_REFUND_CANCEL','DG_REFUND_FAILED')  group by retailer_org_id)sub on om.organization_id=sub.retailer_org_id group by parent_id)first inner join (select parent_id,sum(ref_net_amt) as saleRef from st_rep_dg_retailer where game_id=",gameId," and finaldate>=date('",startDate,"') and finaldate<=date('",endDate,"') group by parent_id) second on first.parent_id=second.parent_id)");
PREPARE stmt FROM @a;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
    IF(@cancelResult!=0)THEN
     CALL raise_error;
    END IF;
    DROP TEMPORARY TABLE IF EXISTS table2;
    SET @tab2=CONCAT("CREATE TEMPORARY TABLE table2(retailer_org_id int(10),tx_date date ,pwtMrp decimal(20,2)   ,retCommPwt decimal(20,2),pwtNet decimal(20,2),primary key(retailer_org_id,tx_date)) AS select drs.retailer_org_id,date(transaction_date) tx_date,sum(pwt_amt) pwtMrp,sum(retailer_claim_comm) retCommPwt ,sum(pwt_amt+retailer_claim_comm) pwtNet from st_dg_ret_pwt_",gameId," drs,st_lms_retailer_transaction_master rtm where drs.transaction_id=rtm.transaction_id and rtm.transaction_date>='",startDate,"' and rtm.transaction_date<='",endDate,"' and transaction_type in('DG_PWT_AUTO','DG_PWT_PLR','DG_PWT')  group by retailer_org_id,tx_date");
        PREPARE stmt FROM @tab2;
        EXECUTE stmt;
        DEALLOCATE PREPARE stmt;
    SET @pwtQry=CONCAT("update table2 pwt,st_rep_dg_retailer dg set pwt_mrp=pwtMrp,ret_pwt_comm=retCommPwt,pwt_net_amt=pwtNet  where pwt.retailer_org_id=dg.organization_id and dg.finaldate=pwt.tx_date and game_id=",gameId,"");
        PREPARE stmt FROM @pwtQry;
        EXECUTE stmt;
        DEALLOCATE PREPARE stmt;
SET @a=CONCAT("set @pwtResult=(select sum(pwtNet-pwt) result from (select om.parent_id,ifnull(sum(pwtNet),0.0) pwtNet from st_lms_organization_master om left outer join (select drs.retailer_org_id,sum(pwt_amt+retailer_claim_comm) pwtNet from st_dg_ret_pwt_",gameId," drs,st_lms_retailer_transaction_master rtm where drs.transaction_id=rtm.transaction_id and rtm.transaction_date>='",startDate,"' and rtm.transaction_date<='",endDate,"' and transaction_type in('DG_PWT_AUTO','DG_PWT_PLR','DG_PWT')  group by retailer_org_id)sub on om.organization_id=sub.retailer_org_id group by parent_id)first inner join (select parent_id,sum(pwt_net_amt) as pwt from st_rep_dg_retailer where game_id=",gameId," and finaldate>=date('",startDate,"') and finaldate<=date('",endDate,"') group by parent_id) second on first.parent_id=second.parent_id)");
PREPARE stmt FROM @a;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
    IF(@pwtResult!=0)THEN
    CALL raise_error;
    END IF;
    END LOOP;
    CLOSE gameCur;
SET @insStatus=CONCAT("insert into archiving_status(job_name, status,result)values('DG_RET_REPORTING', 'DONE',1)");
PREPARE stmt FROM @insStatus;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
SELECT "in the end";
    END      
#
CREATE      PROCEDURE `drawGameSaleAgentWiseExpend`(startDate Timestamp , endDate Timestamp ,agentOrgId int, IN stateCode VARCHAR(10), IN cityCode VARCHAR(10))
BEGIN
DECLARE done INT DEFAULT 0;
DECLARE gameId INT;
DECLARE gameCur CURSOR FOR select game_id from st_dg_game_master;
DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;

set @locationQry = '';

IF(stateCode != 'ALL' OR cityCode != 'ALL') THEN
	IF(stateCode != 'ALL') THEN 
		SET @locationQry = CONCAT(@locationQry, " and om.state_code = '",stateCode,"'");
	END IF;
	IF(cityCode != 'ALL') THEN
		SET @locationQry = CONCAT(@locationQry, " AND cm.city_name = '",cityCode,"'");
	END IF;
END IF;

 set @mainQry="select saleTlb.state_name state_name, saleTlb.city_name city_name,  gm.game_id gameId,game_name gameName,unitPriceAmt,noOfTkt,mrpAmt,netAmt from st_dg_game_master gm,(";
	OPEN gameCur;
	  read_loop: LOOP
	    FETCH gameCur INTO gameId;
	    IF done THEN
	      LEAVE read_loop;
	    END IF;
		set @mainQry=concat(@mainQry,"(select state_name, city_name, game_id,unitPriceAmt,noOfTkt,mrpAmt,netAmt from (select  sale.state_name, sale.city_name,sale.game_id,unitPriceAmt,(ifnull(noOfTkt,0)-ifnull(noOfTktRet,0)) noOfTkt,(ifnull(mrpAmt,0.0)-ifnull(mrpAmtRet,0.0))mrpAmt,(ifnull(netAmt,0.0)-ifnull(netAmtRet,0.0))netAmt from (select  sm.name state_name, cm.city_name city_name,game_id,mrp_amt unitPriceAmt,count(mrp_amt) noOfTkt,sum(mrp_amt) mrpAmt,sum(agent_net_amt) netAmt from st_dg_ret_sale_",gameId," inner join st_lms_organization_master om on om.organization_id = retailer_org_id inner join st_lms_state_master sm on om.state_code = sm.state_code inner join st_lms_city_master cm on om.city = cm.city_name where transaction_id in(select transaction_id from st_lms_retailer_transaction_master where transaction_type in('DG_SALE','DG_SALE_OFFLINE') and transaction_date>='",startDate,"' and transaction_date<='",endDate,"' ) and retailer_org_id in (select organization_id from st_lms_organization_master where parent_id =",agentOrgId," ) ",@locationQry," group by mrp_amt  having (unitPriceAmt > 0.00 and mrpAmt > 0.00 and netAmt > 0.00 ) order by mrp_amt) sale left outer join (select sm.name state_name, cm.city_name city_name, game_id,mrp_amt unitPriceAmtRet,count(mrp_amt) noOfTktRet,sum(mrp_amt) mrpAmtRet,sum(agent_net_amt) netAmtRet from st_dg_ret_sale_refund_",gameId," inner join st_lms_organization_master om on om.organization_id = retailer_org_id inner join st_lms_state_master sm on om.state_code = sm.state_code inner join st_lms_city_master cm on om.city = cm.city_name where transaction_id in(select transaction_id from st_lms_retailer_transaction_master where transaction_type in('DG_REFUND_CANCEL','DG_REFUND_FAILED') and transaction_date>='",startDate,"' and transaction_date<='",endDate,"' ) and retailer_org_id in (select organization_id from st_lms_organization_master where parent_id =",agentOrgId," ) ",@locationQry," group by mrp_amt   having (unitPriceAmtRet > 0.00 and mrpAmtRet > 0.00 and netAmtRet > 0.00 ) order by mrp_amt) saleRet on unitPriceAmt=unitPriceAmtRet) sale where noOfTkt!=0) union all");
	END LOOP;
  CLOSE gameCur;
 set @mainQry=TRIM(TRAILING 'union all' FROM @mainQry);
 set @mainQry=concat(@mainQry,") saleTlb where gm.game_id=saleTlb.game_id");

 PREPARE stmt FROM @mainQry;
	  EXECUTE stmt;
	  DEALLOCATE PREPARE stmt;
    END      
#
CREATE      PROCEDURE `drawGameSaleAgentWisePerGame`(startDate timestamp,endDate timestamp,gameNo int, IN cityCode VARCHAR(10), IN stateCode VARCHAR(10))
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
            set @mainQry=concat("select om.organization_id,sm.name state_name,cm.city_name city_name, ",@selCol,",ifnull(mrpAmt,0.0) mrpAmt,ifnull(netAmt,0.0) netAmt from st_lms_organization_master om right outer join (select parent_id,sum(mrpAmt) mrpAmt,sum(netAmt) netAmt from st_lms_organization_master,(select retailer_org_id,sum(mrpAmt) mrpAmt,sum(netAmt) netAmt from ((select sale.retailer_org_id,(ifnull(mrpAmt,0.0)-ifnull(mrpAmtRet,0.0))mrpAmt,(ifnull(netAmt,0.0)-ifnull(netAmtRet,0.0))netAmt from (select drs.retailer_org_id,sum(mrp_amt) mrpAmt,sum(agent_net_amt) netAmt from st_dg_ret_sale_",gameNo," drs inner join st_lms_retailer_transaction_master tm on drs.transaction_id=tm.transaction_id where transaction_type in('DG_SALE','DG_SALE_OFFLINE') and transaction_date>='",startDate,"' and transaction_date<='",endDate,"' group by drs.retailer_org_id) sale left outer join (select drs.retailer_org_id,sum(mrp_amt) mrpAmtRet,sum(agent_net_amt) netAmtRet from st_dg_ret_sale_refund_",gameNo," drs inner join st_lms_retailer_transaction_master tm on drs.transaction_id=tm.transaction_id where transaction_type in('DG_REFUND_CANCEL','DG_REFUND_FAILED') and transaction_date>='",startDate,"' and transaction_date<='",endDate,"' group by drs.retailer_org_id) saleRet on sale.retailer_org_id=saleRet.retailer_org_id)) saleTlb group by retailer_org_id  union all select organization_id,sum(sale_mrp-ref_sale_mrp) mrpAmt,sum(sale_net-ref_net_amt) netAmt from st_rep_dg_retailer where finaldate>='",startDate,"' and finaldate<='",endDate,"' and game_id=",gameNo," group by organization_id) saleTlb where retailer_org_id=organization_id  group by parent_id having mrpAmt > 0.00 and netAmt > 0.00) saleTlb on saleTlb.parent_id=om.organization_id inner join st_lms_state_master sm on om.state_code = sm.state_code inner join st_lms_city_master cm on cm.city_name = om.city");
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
CREATE      PROCEDURE `drawLiveGameReportRetailerWise`(gameNo int,startDate timestamp,endDate timestamp,parentOrgId int)
BEGIN
if (parentOrgId=1) then 
set @startQuery=concat("( select * from st_lms_organization_master where organization_type='RETAILER' )");
set @endQuery=concat(" ");
set @lastQuery=concat(" ");
set @unionQuery=concat(" ");
else
set @startQuery=concat(" st_lms_organization_master ");
set @endQuery=concat(" and drs.retailer_org_id in (select organization_id from st_lms_organization_master where parent_id=",parentOrgId,") ");
set @lastQuery=concat("and om.parent_id=",parentOrgId,"");
set @unionQuery=concat(" and  dgr.parent_id=",parentOrgId," ");
end if;
set @mainQry=concat("select main.organization_id organization_id,sum(main.sale)as sale,sum(main.cancel)as cancel,sum(main.pwt)as pwt from (
select sale.organization_id,sale,cancel,pwt from(select organization_id,ifnull(sale,0.0) sale from ",@startQuery," om left outer join(select drs.retailer_org_id,sum(mrp_amt) as sale from st_dg_ret_sale_",gameNo," drs inner join st_lms_retailer_transaction_master rtm on drs.transaction_id=rtm.transaction_id  where rtm.transaction_type in('DG_SALE','DG_SALE_OFFLINE') and rtm.transaction_date>='",startDate,"' and rtm.transaction_date<='",endDate,"' ",@endQuery," group by drs.retailer_org_id)sale on om.organization_id=retailer_org_id where om.organization_type='RETAILER' ",@lastQuery,")sale
 inner join (select organization_id,ifnull(cancel,0.0) cancel from ",@startQuery," om left outer join (select drs.retailer_org_id,sum(mrp_amt) as cancel from st_dg_ret_sale_refund_",gameNo," drs inner join st_lms_retailer_transaction_master rtm on drs.transaction_id=rtm.transaction_id where rtm.transaction_type in('DG_REFUND_CANCEL','DG_REFUND_FAILED') and rtm.transaction_date>='",startDate,"' and rtm.transaction_date<='",endDate,"' ",@endQuery," group by drs.retailer_org_id)cancel on om.organization_id=retailer_org_id where om.organization_type='RETAILER' ",@lastQuery,")cancel inner join (select organization_id,ifnull(pwt,0.0) pwt from ",@startQuery," om left outer join(select drs.retailer_org_id,sum(pwt_amt) as pwt from st_dg_ret_pwt_",gameNo," drs inner join st_lms_retailer_transaction_master rtm on drs.transaction_id=rtm.transaction_id  where rtm.transaction_type in('DG_PWT_AUTO','DG_PWT_PLR','DG_PWT') and rtm.transaction_date>='",startDate,"' and rtm.transaction_date<='",endDate,"' ",@endQuery," group by drs.retailer_org_id)pwt on om.organization_id=retailer_org_id where om.organization_type='RETAILER' ",@lastQuery,")pwt on sale.organization_id=cancel.organization_id and sale.organization_id=pwt.organization_id and pwt.organization_id=cancel.organization_id 
union all select dgr.organization_id as organization_id,sum(dgr.sale_mrp) as sale ,sum(dgr.ref_sale_mrp) cancel, sum(dgr.pwt_mrp) pwt  from  st_rep_dg_retailer as dgr  where  dgr.game_id=",gameNo," ",@unionQuery," and dgr.finaldate>=date('",startDate,"') and dgr.finaldate<=date('",endDate,"') group by dgr.organization_id)as main group by main.organization_id");
            

PREPARE stmt FROM @mainQry;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
    END      
#
CREATE      PROCEDURE `drawPaymentAgentWisePerGame`(startDate timestamp,endDate timestamp,gameNo int,agtOrgId int)
BEGIN
  set @mainQry=concat("select parent_id,sum(main.sale) as sale ,sum(main.cancel)as cancel,sum(main.pwt)as pwt,sum(main.pwtDir)as pwtDir from (select parent_id,sale,cancel,pwt,ifnull(pwtDir,0.0) pwtDir from (select sale.parent_id,sum(sale) sale,sum(cancel) cancel,sum(pwt) pwt from (select organization_id,parent_id,ifnull(sale,0.0) sale from st_lms_organization_master om left outer join (select drs.retailer_org_id,sum(agent_net_amt) as sale from st_dg_ret_sale_",gameNo," drs inner join st_lms_retailer_transaction_master rtm on drs.transaction_id=rtm.transaction_id where rtm.transaction_type in('DG_SALE','DG_SALE_OFFLINE') and rtm.transaction_date>='",startDate,"' and rtm.transaction_date<='",endDate,"' group by drs.retailer_org_id) sale on om.organization_id=retailer_org_id where om.organization_type='RETAILER' and om.parent_id=",agtOrgId,") sale inner join (select organization_id,parent_id,ifnull(cancel,0.0) cancel from st_lms_organization_master om left outer join (select drs.retailer_org_id,sum(agent_net_amt) as cancel from st_dg_ret_sale_refund_",gameNo," drs inner join st_lms_retailer_transaction_master rtm on drs.transaction_id=rtm.transaction_id where transaction_type in('DG_REFUND_CANCEL','DG_REFUND_FAILED') and transaction_date>='",startDate,"' and transaction_date<='",endDate,"' group by drs.retailer_org_id) cancel on om.organization_id=retailer_org_id where om.organization_type='RETAILER' and om.parent_id=",agtOrgId,") cancel inner join (select organization_id,parent_id,ifnull(pwt,0.0) pwt from st_lms_organization_master om left outer join (select drs.retailer_org_id,sum(pwt_amt+agt_claim_comm) as pwt from st_dg_ret_pwt_",gameNo," drs inner join st_lms_retailer_transaction_master rtm on drs.transaction_id=rtm.transaction_id where transaction_type in('DG_PWT_AUTO','DG_PWT_PLR','DG_PWT') and transaction_date>='",startDate,"' and transaction_date<='",endDate,"' group by drs.retailer_org_id) pwt on om.organization_id=retailer_org_id where om.organization_type='RETAILER' and om.parent_id=",agtOrgId,") pwt on  sale.organization_id=cancel.organization_id and sale.organization_id=pwt.organization_id and pwt.organization_id=cancel.organization_id group by parent_id) aa left outer join (select organization_id,sum(pwt_amt+agt_claim_comm) pwtDir from st_lms_organization_master left outer join st_dg_agt_direct_plr_pwt on organization_id=agent_org_id where transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and organization_type='AGENT' and organization_id=",agtOrgId," and game_id=",gameNo," group by agent_org_id) bb on parent_id=organization_id union all select organization_id,sum(sale_net) as sale ,sum(ref_net_amt) as cancel , sum(pwt_net_amt) as pwt,sum(direct_pwt_net_amt) as pwtDir from st_rep_dg_agent where game_id=",gameNo," and finaldate>=date('",startDate,"') and finaldate<=date('",endDate,"') and organization_id=",agtOrgId," group by organization_id) as main group by main.parent_id");
       
PREPARE stmt FROM @mainQry;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
    END      
#
CREATE      PROCEDURE `drawPwtAgentWise`(startDate TIMESTAMP,endDate TIMESTAMP, IN stateCode VARCHAR(10), IN cityCode VARCHAR(10))
BEGIN
DECLARE done INT DEFAULT 0;
DECLARE gameId INT;
DECLARE gameCur CURSOR FOR SELECT game_id FROM st_dg_game_master;
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
 SET @mainQry=CONCAT("select ",@selCol,",organization_id,mrpAmt,netAmt, pwtTlb.name state_name, pwtTlb.city_name from st_lms_organization_master om,(SELECT parent_id,SUM(mrpAmt) mrpAmt,SUM(netAmt) netAmt, NAME, city_name FROM (SELECT main.parent_id, main.mrpAmt, main.netAmt, sm.name NAME, city.city_name FROM (SELECT parent_id,SUM(mrpAmt) mrpAmt,SUM(netAmt) netAmt FROM (");
	OPEN gameCur;
	  read_loop: LOOP
	    FETCH gameCur INTO gameId;
	    IF done THEN
	      LEAVE read_loop;
	    END IF;
SET @mainQry=CONCAT(@mainQry,"SELECT rp.retailer_org_id, SUM(rp.pwt_amt) mrpAmt, SUM(rp.pwt_amt + rp.agt_claim_comm-rp.govt_claim_comm) netAmt FROM st_dg_ret_pwt_",gameId," rp INNER JOIN st_lms_retailer_transaction_master rTxn ON rp.transaction_id = rTxn.transaction_id WHERE rTxn.transaction_type IN('DG_PWT_AUTO','DG_PWT_PLR','DG_PWT') AND rTxn.transaction_date>='",startDate,"' and rTxn.transaction_date<='",endDate,"'");
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

SET @mainQry = CONCAT(@mainQry, " union all SELECT dpp.agent_org_id,SUM(dpp.pwt_amt) mrpAmt,SUM(dpp.pwt_amt+dpp.agt_claim_comm-dpp.tax_amt) netAmt, sm.name, city.city_name FROM st_dg_agt_direct_plr_pwt  dpp INNER JOIN st_lms_organization_master om ON om.organization_id = dpp.agent_org_id INNER JOIN st_lms_city_master city ON om.city = city.city_name INNER JOIN st_lms_state_master sm ON sm.state_code = om.state_code WHERE dpp.transaction_date>='",startDate,"' and dpp.transaction_date<='",endDate,"'");
IF(stateCode != 'ALL') THEN 
	SET @mainQry = CONCAT(@mainQry, " AND om.state_code = '",stateCode,"'");
END IF;
IF(cityCode != 'ALL') THEN
	SET @mainQry = CONCAT(@mainQry, " AND city.city_name = '",cityCode,"'");
END IF;
SET @mainQry = CONCAT(@mainQry, " GROUP BY dpp.agent_org_id UNION ALL "); 
SET @mainQry = CONCAT(@mainQry, "SELECT rda.organization_id,SUM(rda.pwt_mrp+rda.direct_pwt_amt) mrpAmt,SUM(rda.pwt_net_amt+rda.direct_pwt_net_amt) netAmt, sm.name, city.city_name FROM st_rep_dg_agent rda INNER JOIN st_lms_organization_master om ON om.organization_id = rda.organization_id INNER JOIN st_lms_city_master city ON om.city = city.city_name INNER JOIN st_lms_state_master sm ON sm.state_code = om.state_code WHERE rda.finaldate>='",startDate,"' and rda.finaldate<='",endDate,"'");
IF(stateCode != 'ALL') THEN 
	SET @mainQry = CONCAT(@mainQry, " AND om.state_code = '",stateCode,"'");
END IF;
IF(cityCode != 'ALL') THEN
	SET @mainQry = CONCAT(@mainQry, " AND city.city_name = '",cityCode,"'");
END IF;
SET @mainQry = CONCAT(@mainQry, " group by organization_id) pwtTlb group by parent_id) pwtTlb where om.organization_id=pwtTlb.parent_id;");
 PREPARE stmt FROM @mainQry;
	  EXECUTE stmt;
	  DEALLOCATE PREPARE stmt;
  END      
#
CREATE      PROCEDURE `drawPwtAgentWiseExpand`(startDate timestamp,endDate timestamp,agentOrgId int)
BEGIN
DECLARE done INT DEFAULT 0;
DECLARE gameId INT;
DECLARE gameCur CURSOR FOR select game_id from st_dg_game_master;
DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;
 set @mainQry="select gm.game_id gameId, game_name gameName,priceAmt,noOfTkt,mrpAmt,netAmt from st_dg_game_master gm,(";
	OPEN gameCur;
	  read_loop: LOOP
	    FETCH gameCur INTO gameId;
	    IF done THEN
	      LEAVE read_loop;
	    END IF;
set @mainQry=concat(@mainQry,"select game_id,priceAmt,sum(noOfTkt) noOfTkt,sum(mrpAmt) mrpAmt,sum(netAmt) netAmt from (select game_id,pwt_amt priceAmt,count(pwt_amt) noOfTkt,sum(pwt_amt) mrpAmt,sum(pwt_amt+agt_claim_comm) netAmt from st_dg_ret_pwt_",gameId," where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_type in('DG_PWT_AUTO','DG_PWT_PLR','DG_PWT') and transaction_date>='",startDate,"' and transaction_date<='",endDate,"') and retailer_org_id in (select organization_id from st_lms_organization_master where parent_id=",agentOrgId,") group by pwt_amt union all select game_id,pwt_amt priceAmt,count(pwt_amt) noOfTkt,sum(pwt_amt) mrpAmt,sum(pwt_amt+agt_claim_comm) netAmt from st_dg_agt_direct_plr_pwt where transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and game_id=",gameId," and agent_org_id=",agentOrgId, " group by pwt_amt) pwtTlb group by priceAmt union all "); 
	  END LOOP;
	CLOSE gameCur;
set @mainQry=TRIM(TRAILING ' union all ' FROM @mainQry);
 set @mainQry=concat(@mainQry,") pwtTlb where gm.game_id=pwtTlb.game_id");
 
 PREPARE stmt FROM @mainQry;
	  EXECUTE stmt;
	  DEALLOCATE PREPARE stmt;
  END      
#
CREATE      PROCEDURE `drawPwtGameWise`(startDate TIMESTAMP, endDate TIMESTAMP,  IN stateCode VARCHAR(10), IN cityCode VARCHAR(10))
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

CREATE      PROCEDURE `drawPwtGameWiseExpand`(startDate timestamp,endDate timestamp)
BEGIN
DECLARE done INT DEFAULT 0;
DECLARE gameId INT;
DECLARE gameCur CURSOR FOR select game_id from st_dg_game_master;
DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;
 set @mainQry="select gm.game_id gameId, game_name gameName,priceAmt,noOfTkt,mrpAmt,netAmt from st_dg_game_master gm,(";
	OPEN gameCur;
	  read_loop: LOOP
	    FETCH gameCur INTO gameId;
	    IF done THEN
	      LEAVE read_loop;
	    END IF;
set @mainQry=concat(@mainQry,"select game_id,priceAmt,sum(noOfTkt) noOfTkt,sum(mrpAmt) mrpAmt,sum(netAmt) netAmt from (select game_id,pwt_amt priceAmt,count(pwt_amt) noOfTkt,sum(pwt_amt) mrpAmt,sum(pwt_amt+agt_claim_comm) netAmt from st_dg_ret_pwt_",gameId," where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_type in('DG_PWT_AUTO','DG_PWT_PLR','DG_PWT') and transaction_date>='",startDate,"' and transaction_date<='",endDate,"') group by pwt_amt union all select game_id,pwt_amt priceAmt,count(pwt_amt) noOfTkt,sum(pwt_amt) mrpAmt,sum(pwt_amt+agt_claim_comm) netAmt from st_dg_agt_direct_plr_pwt where transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and game_id=",gameId," group by pwt_amt union all select game_id,pwt_amt priceAmt,count(pwt_amt) noOfTkt,sum(pwt_amt) mrpAmt,sum(pwt_amt) netAmt from st_dg_bo_direct_plr_pwt where transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and game_id=",gameId," group by pwt_amt) pwtTlb group by priceAmt union all "); 
	  END LOOP;
	CLOSE gameCur;
 set @mainQry=TRIM(TRAILING ' union all ' FROM @mainQry);
 set @mainQry=concat(@mainQry,") pwtTlb where gm.game_id=pwtTlb.game_id");
 PREPARE stmt FROM @mainQry;
	  EXECUTE stmt;
	  DEALLOCATE PREPARE stmt;
  END      
#
CREATE      PROCEDURE `drawPwtGameWiseExpandForAgent`(startDate timestamp,endDate timestamp,agtOrgId int)
BEGIN
DECLARE done INT DEFAULT 0;
DECLARE gameId INT;
DECLARE gameCur CURSOR FOR select game_id from st_dg_game_master;
DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;
 set @mainQry="select gm.game_id gameId, game_name gameName,priceAmt,noOfTkt,mrpAmt,netAmt from st_dg_game_master gm,(";
	OPEN gameCur;
	  read_loop: LOOP
	    FETCH gameCur INTO gameId;
	    IF done THEN
	      LEAVE read_loop;
	    END IF;
set @mainQry=concat(@mainQry,"select game_id,priceAmt,sum(noOfTkt) noOfTkt,sum(mrpAmt) mrpAmt,sum(netAmt) netAmt from (select game_id,pwt_amt priceAmt,count(pwt_amt) noOfTkt,sum(pwt_amt) mrpAmt,sum(pwt_amt+retailer_claim_comm) netAmt from st_dg_ret_pwt_",gameId," where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_type in('DG_PWT_AUTO','DG_PWT_PLR','DG_PWT') and transaction_date>='",startDate,"' and transaction_date<='",endDate,"') and retailer_org_id in (select organization_id from st_lms_organization_master where parent_id=",agtOrgId,") group by pwt_amt union all select game_id,pwt_amt priceAmt,count(pwt_amt) noOfTkt,sum(pwt_amt) mrpAmt,sum(pwt_amt+agt_claim_comm) netAmt from st_dg_agt_direct_plr_pwt where transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and game_id=",gameId," and agent_org_id=",agtOrgId," group by pwt_amt) pwtTlb group by priceAmt union all "); 
	  END LOOP;
	CLOSE gameCur;
 set @mainQry=TRIM(TRAILING ' union all ' FROM @mainQry);
 set @mainQry=concat(@mainQry,") pwtTlb where gm.game_id=pwtTlb.game_id");
 PREPARE stmt FROM @mainQry;
	  EXECUTE stmt;
	  DEALLOCATE PREPARE stmt;
  END      
#
CREATE      PROCEDURE `drawPwtGameWiseForAgent`(startDate timestamp,endDate timestamp,agtOrgId int)
BEGIN
DECLARE done INT DEFAULT 0;
DECLARE gameId INT;
DECLARE gameCur CURSOR FOR select game_id from st_dg_game_master;
DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;
 set @mainQry="select gm.game_id gameId, game_name gameName,mrpAmt,netAmt from st_dg_game_master gm,(select game_id,sum(mrpAmt) mrpAmt,sum(netAmt) netAmt from(";
	OPEN gameCur;
	  read_loop: LOOP
	    FETCH gameCur INTO gameId;
	    IF done THEN
	      LEAVE read_loop;
	    END IF;
set @mainQry=concat(@mainQry,"select game_id,sum(pwt_amt) mrpAmt,sum(pwt_amt+retailer_claim_comm) netAmt from st_dg_ret_pwt_",gameId," where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_type in('DG_PWT_AUTO','DG_PWT_PLR','DG_PWT') and transaction_date>='",startDate,"' and transaction_date<='",endDate,"') and retailer_org_id in (select organization_id from st_lms_organization_master where parent_id=",agtOrgId,") group by game_id union all "); 
	  END LOOP;
	CLOSE gameCur;
 set @mainQry=concat(@mainQry,"select game_id,sum(pwt_amt) mrpAmt,sum(pwt_amt+agt_claim_comm) netAmt from st_dg_agt_direct_plr_pwt where transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and agent_org_id=",agtOrgId," group by game_id union all select game_id,sum(pwt_mrp+direct_pwt_amt) mrpAmt,sum(pwt_net_amt+direct_pwt_net_amt) netAmt from st_rep_dg_agent where finaldate>='",startDate,"' and finaldate<='",endDate,"' and organization_id=",agtOrgId," group by game_id) pwtTlb group by game_id) pwtTlb where gm.game_id=pwtTlb.game_id");
 PREPARE stmt FROM @mainQry;
	  EXECUTE stmt;
	  DEALLOCATE PREPARE stmt;
  END      
#
CREATE      PROCEDURE `drawPwtRetailerWise`(startDate TIMESTAMP,endDate TIMESTAMP,agtOrgId INT,  IN stateCode VARCHAR(10), IN cityCode VARCHAR(10))
BEGIN
DECLARE done INT DEFAULT 0;
DECLARE gameId INT;
DECLARE gameCur CURSOR FOR SELECT game_id FROM st_dg_game_master;
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
SET @mainQry=CONCAT(@mainQry,"SELECT rPwt.retailer_org_id,SUM(rPwt.pwt_amt) mrpAmt,SUM(rPwt.pwt_amt+rPwt.retailer_claim_comm) netAmt, sm.name, city.city_name FROM st_dg_ret_pwt_",gameId," rPwt INNER JOIN st_lms_retailer_transaction_master rTxn ON rPwt.transaction_id = rTxn.transaction_id INNER JOIN st_lms_organization_master om ON rPwt.retailer_org_id = om.organization_id INNER JOIN st_lms_state_master sm ON sm.state_code = om.state_code INNER JOIN st_lms_city_master city ON om.city = city.city_name WHERE rTxn.transaction_type IN('DG_PWT_AUTO','DG_PWT_PLR','DG_PWT') AND rTxn.transaction_date>='",startDate,"' and rTxn.transaction_date<='",endDate,"' AND om.parent_id=",agtOrgId,"");
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
 SET @mainQry=CONCAT(@mainQry," union all SELECT dr.organization_id,SUM(dr.pwt_mrp) mrpAmt,SUM(dr.pwt_net_amt) netAmt, sm.name, city.city_name FROM st_rep_dg_retailer dr INNER JOIN st_lms_organization_master om ON dr.organization_id = om.organization_id INNER JOIN st_lms_state_master sm ON sm.state_code = om.state_code INNER JOIN st_lms_city_master city ON om.city = city.city_name WHERE dr.finaldate>='",startDate,"' and dr.finaldate<='",endDate,"' and dr.parent_id=",agtOrgId,"");
 
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

CREATE      PROCEDURE `drawPwtRetailerWiseExpand`(startDate TIMESTAMP,endDate TIMESTAMP,retOrgId INT, IN stateCode VARCHAR(10), IN cityCode VARCHAR(10))
BEGIN
DECLARE done INT DEFAULT 0;
DECLARE gameNo INT;
DECLARE gameCur CURSOR FOR SELECT game_id FROM st_dg_game_master;
DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;
 SET @mainQry="select gm.game_id gameId, game_name gameName,priceAmt,noOfTkt,mrpAmt,netAmt, name state_name, city_name from st_dg_game_master gm,(";
	OPEN gameCur;
	  read_loop: LOOP
	    FETCH gameCur INTO gameNo;
	    IF done THEN
	      LEAVE read_loop;
	    END IF;
SET @mainQry=CONCAT(@mainQry,"select game_id,priceAmt,sum(noOfTkt) noOfTkt,sum(mrpAmt) mrpAmt,sum(netAmt) netAmt, name, city_name from (select drp.game_id,drp.pwt_amt priceAmt,count(drp.pwt_amt) noOfTkt,sum(drp.pwt_amt) mrpAmt,sum(drp.pwt_amt+drp.retailer_claim_comm) netAmt, sm.name, cm.city_name from st_dg_ret_pwt_",gameNo,"  drp inner join st_lms_retailer_transaction_master retTxn on drp.transaction_id = retTxn.transaction_id INNER JOIN st_lms_organization_master om ON drp.retailer_org_id = om.organization_id inner join st_lms_state_master sm on sm.state_code = om.state_code INNER JOIN st_lms_city_master cm ON om.city = cm.city_name where retTxn.transaction_type in ('DG_PWT_AUTO','DG_PWT_PLR','DG_PWT') and retTxn.transaction_date>='",startDate,"' and retTxn.transaction_date<='",endDate,"' and drp.retailer_org_id=",retOrgId,"");
IF(stateCode != 'ALL') THEN 
	SET @mainQry = CONCAT(@mainQry, " AND om.state_code = '",stateCode,"'");
END IF;
IF(cityCode != 'ALL') THEN
	SET @mainQry = CONCAT(@mainQry, " AND cm.city_name = '",cityCode,"'");
END IF;
SET @mainQry=CONCAT(@mainQry," group by pwt_amt) pwtTlb group by priceAmt union all "); 
	  END LOOP;
	CLOSE gameCur;
 SET @mainQry=TRIM(TRAILING ' union all ' FROM @mainQry);
 SET @mainQry=CONCAT(@mainQry,") pwtTlb where gm.game_id=pwtTlb.game_id");

 PREPARE stmt FROM @mainQry;
	  EXECUTE stmt;
	  DEALLOCATE PREPARE stmt;
  END      
#
CREATE      PROCEDURE `drawSaleGameWise`(startDate Timestamp , endDate Timestamp,agtOrgId int, IN cityCode VARCHAR(10), IN stateCode VARCHAR(10))
BEGIN
DECLARE done INT DEFAULT 0;
DECLARE gameId INT;
DECLARE gameCur CURSOR FOR select game_id from st_dg_game_master;
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
set @mainQry="select gm.game_id gameId,game_name gameName,sum(mrpAmt) mrpAmt,sum(netAmt) netAmt from st_dg_game_master gm,(";
	OPEN gameCur;
	  read_loop: LOOP
	    FETCH gameCur INTO gameId;
	    IF done THEN
	      LEAVE read_loop;
	    END IF;
		set @mainQry=concat(@mainQry,"select sale.game_id,(ifnull(mrpAmt,0.0)-ifnull(mrpAmtRet,0.0))mrpAmt,(ifnull(netAmt,0.0)-ifnull(netAmtRet,0.0))netAmt from (select game_id,sum(mrp_amt) mrpAmt,sum(agent_net_amt) netAmt from st_dg_ret_sale_",gameId," where transaction_id in(select transaction_id from st_lms_retailer_transaction_master where transaction_type in('DG_SALE','DG_SALE_OFFLINE') and transaction_date>='",startDate, "' and transaction_date<='",endDate,"' ) ",@addQry," group by game_id) sale left outer join (select game_id,sum(mrp_amt) mrpAmtRet,sum(agent_net_amt) netAmtRet from st_dg_ret_sale_refund_",gameId," where transaction_id in(select transaction_id from st_lms_retailer_transaction_master where transaction_type in('DG_REFUND_CANCEL','DG_REFUND_FAILED') and transaction_date>='",startDate,"' and transaction_date<='",endDate,"' ) ",@addQry," group by game_id)saleRet on sale.game_id=saleRet.game_id union all ");
	END LOOP;
  CLOSE gameCur;
 set @mainQry=TRIM(TRAILING 'union all ' FROM @mainQry);
 set @mainQry=concat(@mainQry," union all select game_id,sum(sale_mrp-ref_sale_mrp) mrpAmt,sum(sale_net-ref_net_amt) netAmt from st_rep_dg_retailer where finaldate>='",startDate,"' and finaldate<='",endDate,"' ",@addQryArch," group by game_id) saleTlb where gm.game_id=saleTlb.game_id group by saleTlb.game_id");
 PREPARE stmt FROM @mainQry;
	  EXECUTE stmt;
	  DEALLOCATE PREPARE stmt;
    END
#

CREATE      PROCEDURE `drawSaleGameWiseExpand`(startDate Timestamp , endDate Timestamp,agtOrgId int)
BEGIN
DECLARE done INT DEFAULT 0;
DECLARE gameId INT;
DECLARE gameCur CURSOR FOR select game_id from st_dg_game_master;
DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;
set @addQry="";
if (agtOrgId!=0) then
set @addQry=concat("and retailer_org_id in(select organization_id from st_lms_organization_master where parent_id=",agtOrgId,")");
end if;
 set @mainQry="select gm.game_id gameId,game_name gameName,unitPriceAmt,noOfTkt,mrpAmt,netAmt from st_dg_game_master gm,(";
	OPEN gameCur;
	  read_loop: LOOP
	    FETCH gameCur INTO gameId;
	    IF done THEN
	      LEAVE read_loop;
	    END IF;
		set @mainQry=concat(@mainQry,"(select game_id,unitPriceAmt,noOfTkt,mrpAmt,netAmt from (select sale.game_id,unitPriceAmt,(ifnull(noOfTkt,0)-ifnull(noOfTktRet,0)) noOfTkt,(ifnull(mrpAmt,0.0)-ifnull(mrpAmtRet,0.0)) mrpAmt,(ifnull(netAmt,0.0)-ifnull(netAmtRet,0.0)) netAmt from (select game_id,mrp_amt unitPriceAmt,count(mrp_amt) noOfTkt,sum(mrp_amt) mrpAmt,sum(agent_net_amt) netAmt from st_dg_ret_sale_",gameId," where transaction_id in(select transaction_id from st_lms_retailer_transaction_master where transaction_type in('DG_SALE','DG_SALE_OFFLINE') and transaction_date>='",startDate,"' and transaction_date<='",endDate,"' ) ",@addQry," group by mrp_amt order by mrp_amt) sale left outer join (select game_id,mrp_amt unitPriceAmtRet,count(mrp_amt) noOfTktRet,sum(mrp_amt) mrpAmtRet,sum(agent_net_amt) netAmtRet from st_dg_ret_sale_refund_",gameId," where transaction_id in(select transaction_id from st_lms_retailer_transaction_master where transaction_type in('DG_REFUND_CANCEL','DG_REFUND_FAILED') and transaction_date>='",startDate,"' and transaction_date<='",endDate,"' ) ",@addQry," group by mrp_amt order by mrp_amt) saleRet on sale.game_id=saleRet.game_id and unitPriceAmt=unitPriceAmtRet) sale where noOfTkt!=0) union all ");
	END LOOP;
  CLOSE gameCur;
 set @mainQry=TRIM(TRAILING 'union all ' FROM @mainQry);
 set @mainQry=concat(@mainQry,") saleTlb where gm.game_id=saleTlb.game_id");
 PREPARE stmt FROM @mainQry;
	  EXECUTE stmt;
	  DEALLOCATE PREPARE stmt;
    END      
#
CREATE      PROCEDURE `drawSaleRetailerWise`(startDate Timestamp , endDate Timestamp,agtOrgId int, IN cityCode VARCHAR(10), IN stateCode VARCHAR(10))
BEGIN
DECLARE done INT DEFAULT 0;
DECLARE gameId INT;
DECLARE gameCur CURSOR FOR select game_id from st_dg_game_master;
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
		set @mainQry=concat(@mainQry,"(select sale.retailer_org_id,(ifnull(mrpAmt,0.0)-ifnull(mrpAmtRet,0.0))mrpAmt,(ifnull(netAmt,0.0)-ifnull(netAmtRet,0.0))netAmt from (select retailer_org_id,sum(mrp_amt) mrpAmt,sum(net_amt) netAmt from st_dg_ret_sale_",gameId," where transaction_id in(select transaction_id from st_lms_retailer_transaction_master where transaction_type in('DG_SALE','DG_SALE_OFFLINE') and transaction_date>='",startDate,"' and transaction_date<='",endDate,"' )group by retailer_org_id) sale left outer join (select retailer_org_id,sum(mrp_amt) mrpAmtRet,sum(net_amt) netAmtRet from st_dg_ret_sale_refund_",gameId," where transaction_id in(select transaction_id from st_lms_retailer_transaction_master where transaction_type in('DG_REFUND_CANCEL','DG_REFUND_FAILED') and transaction_date>='",startDate,"' and transaction_date<='",endDate,"' )group by retailer_org_id) saleRet on sale.retailer_org_id=saleRet.retailer_org_id) union all ");
	END LOOP;
  CLOSE gameCur;
 set @mainQry=TRIM(TRAILING 'union all ' FROM @mainQry);
 set @mainQry=concat(@mainQry,") saleTlb group by retailer_org_id) saleTlb where retailer_org_id=organization_id and parent_id=",agtOrgId," group by organization_id union all select organization_id,sum(sale_mrp-ref_sale_mrp) mrpAmt,sum(sale_net-ref_net_amt) netAmt from st_rep_dg_retailer where finaldate>='",startDate,"' and finaldate<='",endDate,"' and parent_id=",agtOrgId," group by organization_id) saleTlb on saleTlb.organization_id=om.organization_id  inner join st_lms_state_master sm on om.state_code = sm.state_code inner join st_lms_city_master cm on cm.city_name = om.city ");
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
CREATE      PROCEDURE `drawSaleRetailerWiseExpand`(startDate Timestamp , endDate Timestamp,agentOrgId int, IN stateCode VARCHAR(10), IN cityCode VARCHAR(10))
BEGIN
DECLARE done INT DEFAULT 0;
DECLARE gameId INT;
DECLARE gameCur CURSOR FOR select game_id from st_dg_game_master;
DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;

set @locationQry = '';

IF(stateCode != 'ALL' OR cityCode != 'ALL') THEN
	IF(stateCode != 'ALL') THEN 
		SET @locationQry = CONCAT(@locationQry, " and om.state_code = '",stateCode,"'");
	END IF;
	IF(cityCode != 'ALL') THEN
		SET @locationQry = CONCAT(@locationQry, " AND cm.city_name = '",cityCode,"'");
	END IF;
END IF;

 set @mainQry="select saleTlb.state_name state_name, saleTlb.city_name city_name, gm.game_id gameId,game_name gameName,unitPriceAmt,noOfTkt,mrpAmt,netAmt from st_dg_game_master gm,(";
	OPEN gameCur;
	  read_loop: LOOP
	    FETCH gameCur INTO gameId;
	    IF done THEN
	      LEAVE read_loop;
	    END IF;
		set @mainQry=concat(@mainQry,"(select state_name, city_name, game_id,unitPriceAmt,noOfTkt,mrpAmt,netAmt from (select sale.state_name, sale.city_name, sale.game_id,unitPriceAmt,(ifnull(noOfTkt,0)-ifnull(noOfTktRet,0)) noOfTkt,(ifnull(mrpAmt,0.0)-ifnull(mrpAmtRet,0.0))mrpAmt,(ifnull(netAmt,0.0)-ifnull(netAmtRet,0.0))netAmt from (select sm.name state_name, cm.city_name city_name,game_id,mrp_amt unitPriceAmt,count(mrp_amt) noOfTkt,sum(mrp_amt) mrpAmt,sum(net_amt) netAmt from st_dg_ret_sale_",gameId," inner join st_lms_organization_master om on om.organization_id = retailer_org_id inner join st_lms_state_master sm on om.state_code = sm.state_code inner join st_lms_city_master cm on om.city = cm.city_name where transaction_id in(select transaction_id from st_lms_retailer_transaction_master where transaction_type in('DG_SALE','DG_SALE_OFFLINE') and transaction_date>='",startDate,"' and transaction_date<='",endDate,"' ) and retailer_org_id in (select organization_id from st_lms_organization_master where organization_id =",agentOrgId," ) ",@locationQry," group by mrp_amt order by mrp_amt) sale left outer join (select sm.name state_name, cm.city_name city_name, game_id,mrp_amt unitPriceAmtRet,count(mrp_amt) noOfTktRet,sum(mrp_amt) mrpAmtRet,sum(net_amt) netAmtRet from st_dg_ret_sale_refund_",gameId," inner join st_lms_organization_master om on om.organization_id = retailer_org_id inner join st_lms_state_master sm on om.state_code = sm.state_code inner join st_lms_city_master cm on om.city = cm.city_name where transaction_id in(select transaction_id from st_lms_retailer_transaction_master where transaction_type in('DG_REFUND_CANCEL','DG_REFUND_FAILED') and transaction_date>='",startDate,"' and transaction_date<='",endDate,"' ) and retailer_org_id in (select organization_id from st_lms_organization_master where organization_id =",agentOrgId," ) ",@locationQry," group by mrp_amt order by mrp_amt) saleRet on unitPriceAmt=unitPriceAmtRet) sale where noOfTkt!=0 ) union all ");
	END LOOP;
  CLOSE gameCur;
 set @mainQry=TRIM(TRAILING 'union all ' FROM @mainQry);
 set @mainQry=concat(@mainQry,") saleTlb where gm.game_id=saleTlb.game_id");


 PREPARE stmt FROM @mainQry;
	  EXECUTE stmt;
	  DEALLOCATE PREPARE stmt;
    END
#
CREATE      PROCEDURE `fetchDrawDirectPlyPwtofAgent`(startDate timestamp,endDate timestamp,agtOrgId int)
BEGIN
       
        
        
      set @mainQry=concat("select agent_org_id,sum(main.pwtDir)as pwtDir from (select organization_id as agent_org_id,sum(direct_pwt_amt+direct_pwt_comm)as pwtDir from st_rep_dg_agent where organization_id=",agtOrgId," and finaldate>='",startDate,"' and finaldate<='",endDate,"' group by organization_id union all select agent_org_id,sum(pwt_amt+agt_claim_comm) pwtDir from st_dg_agt_direct_plr_pwt where transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and agent_org_id=",agtOrgId," group by agent_org_id)as main group by main.agent_org_id ");
         
PREPARE stmt FROM @mainQry;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
    END
#
CREATE      PROCEDURE `fetchScratchDirectPlyPwtofAgent`(startDate timestamp,endDate timestamp,agtOrgId int)
BEGIN 
       
        
        
     
 set @mainQry=concat("select main.agent_org_id,sum(main.pwtDir)as pwtDir from (select organization_id as agent_org_id,sum(direct_pwt_net_amt)as pwtDir from st_rep_se_agent where organization_id=",agtOrgId," and finaldate>='",startDate,"' and finaldate<='",endDate,"' group by organization_id union all  select agent_org_id,sum(pwt_amt+(pwt_amt*claim_comm*0.01)) pwtDir from st_se_agt_direct_player_pwt where transaction_date>='",startDate,"' and transaction_date<='",endDate,"' and agent_org_id=",agtOrgId," group by agent_org_id)as main group by main.agent_org_id ");
PREPARE stmt FROM @mainQry;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
    END      
#
CREATE      PROCEDURE `fillDateForRep`(startDate timestamp,endDate timestamp)
BEGIN
	DECLARE totalDays  INT;
	DECLARE x  INT;
	declare tempDate date;
	SET totalDays = (datediff(endDate,startDate)+1);
	
	SET @insQry =  'insert into datestore(alldate) values ';
	set @dateVal='';
	set tempDate=date(startDate);
	set @dt=concat("delete from datestore");
	prepare stmt from @dt;
	execute stmt;
	deallocate prepare stmt;
	set x=1;
	WHILE x  <= totalDays DO
		set @dateVal=CONCAT(@dateVal,"('",tempDate,"'),");
		set tempDate=DATE_ADD(tempDate, INTERVAL 1 DAY);
		set  x = x + 1;
	END WHILE;
	set @dateVal=TRIM(TRAILING ',' FROM @dateVal);
	set @insQry=CONCAT(@insQry,@dateVal);
       select @insQry;
	  PREPARE stmt FROM @insQry;
	  EXECUTE stmt;
	  DEALLOCATE PREPARE stmt;
	  set @insth=concat("insert into tempdate_history(last_date,processing_time)  select max(alldate),now() from datestore");
	PREPARE stmt FROM @insth;
	  EXECUTE stmt;
	  DEALLOCATE PREPARE stmt;
    END      
#
CREATE      PROCEDURE `fillDateTable`(startDate timestamp,endDate timestamp)
BEGIN
	DECLARE totalDays  INT;
	DECLARE x  INT;
	declare tempDate date;
	SET totalDays = (datediff(endDate,startDate)+1);
	
	SET @insQry =  'insert into tempdate(alldate) values ';
	set @dateVal='';
	set tempDate=date(startDate);
	set @dt=concat("delete from tempdate");
	prepare stmt from @dt;
	execute stmt;
	deallocate prepare stmt;
	
	set x=1;
	WHILE x  <= totalDays DO
		set @dateVal=CONCAT(@dateVal,"('",tempDate,"'),");
		set tempDate=DATE_ADD(tempDate, INTERVAL 1 DAY);
		set  x = x + 1;
	END WHILE;
	set @dateVal=TRIM(TRAILING ',' FROM @dateVal);
	set @insQry=CONCAT(@insQry,@dateVal);
       
	  PREPARE stmt FROM @insQry;
	  EXECUTE stmt;
	  DEALLOCATE PREPARE stmt;
    END
#
CREATE      PROCEDURE `getAddressFromOrganizationMaster`(orgId int)
BEGIN
	set @getAdd=concat("select addr_line1, addr_line2, city from st_lms_organization_master where organization_id =",orgId,"");
	Prepare stmt from @getAdd;
	execute stmt;
	deallocate prepare stmt;
    END
#

CREATE      PROCEDURE `getAgentScratchPwtDetails`(agentOrgId INT,startDate DATE,endDate DATE)
BEGIN
	SET @pwtQry=CONCAT("select sum(total_pwt_amt) total_pwt_amt from( select ifnull(sum(bpwt.pwt_amt),0) total_pwt_amt from st_se_bo_pwt bpwt, st_lms_bo_transaction_master btm where bpwt.agent_org_id=",agentOrgId," and btm.transaction_id=bpwt.transaction_id and ( btm.transaction_date>='",startDate,"' and btm.transaction_date<'",endDate,"')union all select sum(pwt_mrp) total_pwt_amt from st_rep_se_agent where finaldate>='",startDate,"' and finaldate<'",endDate,"' and organization_id=",agentOrgId,")pwt");
	PREPARE stmt FROM @pwtQry;
	EXECUTE stmt;
	DEALLOCATE PREPARE stmt;
    END      
#
CREATE      PROCEDURE `getAgtDirPlrPwt`(startDate TimeStamp,endDate TimeStamp,gameNumber int, IN cityCode VARCHAR(10), IN stateCode VARCHAR(10))
BEGIN
set @subqry :=(select value  from st_lms_property_master  where property_code='ORG_LIST_TYPE');
		set @selCol = 'som.name agtname';
		if(@subqry='CODE') then 
			set @selCol='som.org_code agtname' ;
		end if;
		if (@subqry='CODE_NAME') then 
			set @selCol='concat(som.org_code,"_",som.name)agtname';
		end if;
		if (@subqry='NAME_CODE') then 
		  	set @selCol='concat(som.name,"_",som.org_code)agtname';
		end if; 
		set @subqry1 :=(select value  from st_lms_property_master  where property_code='ORG_LIST_ORDER');
		set @selColOrder = 'agtname ASC';
		if(@subqry1='ORG_ID') then 
			set @selColOrder='organization_id' ;
		end if;
		if (@subqry1='DESC') then 
			set @selColOrder='agtname DESC';
		end if;
		
set @agtDirPlrPwtQuery = concat("select ",@selCol,", sum(totDirPlrPwt) totDirPlrPwt from(select agent_org_id as agt_id, sum(net_amt + agt_claim_comm)as totDirPlrPwt from st_dg_agt_direct_plr_pwt where transaction_id in (select transaction_id from st_lms_agent_transaction_master where date(transaction_date)>='",startDate,"' and date(transaction_date)<'",endDate,"' and transaction_type = 'DG_PWT_PLR') and game_id =",gameNumber," group by agent_org_id union all select organization_id,sum(direct_pwt_net_amt) as totDirPlrPwt from st_rep_dg_agent where finaldate>='",startDate,"' and finaldate<'",endDate,"' and game_id=",gameNumber," group by organization_id)agt, st_lms_organization_master som, st_lms_state_master sm, st_lms_city_master cm where som.state_code = sm.state_code and cm.city_name = som.city and som.organization_type= 'AGENT' and som.organization_id = agt.agt_id");
IF(stateCode != 'ALL' OR cityCode != 'ALL') THEN
	IF(stateCode != 'ALL') THEN 
		SET @agtDirPlrPwtQuery = CONCAT(@agtDirPlrPwtQuery, " and som.state_code = '",stateCode,"'");
	END IF;
	IF(cityCode != 'ALL') THEN
		SET @agtDirPlrPwtQuery = CONCAT(@agtDirPlrPwtQuery, " AND cm.city_name = '",cityCode,"'");
	END IF;
END IF;
set @agtDirPlrPwtQuery = concat(@agtDirPlrPwtQuery, " group by agt.agt_id order by ",@selColOrder,"");
prepare stmt from @agtDirPlrPwtQuery;
execute stmt;
deallocate prepare stmt;
    END      
#
CREATE      PROCEDURE `getAgtDirPlrPwtIW`(startDate TimeStamp,endDate TimeStamp,gameNumber int, IN cityCode VARCHAR(10), IN stateCode VARCHAR(10))
BEGIN
set @subqry :=(select value  from st_lms_property_master  where property_code='ORG_LIST_TYPE');
		set @selCol = 'som.name agtname';
		if(@subqry='CODE') then 
			set @selCol='som.org_code agtname' ;
		end if;
		if (@subqry='CODE_NAME') then 
			set @selCol='concat(som.org_code,"_",som.name)agtname';
		end if;
		if (@subqry='NAME_CODE') then 
		  	set @selCol='concat(som.name,"_",som.org_code)agtname';
		end if; 
		set @subqry1 :=(select value  from st_lms_property_master  where property_code='ORG_LIST_ORDER');
		set @selColOrder = 'agtname ASC';
		if(@subqry1='ORG_ID') then 
			set @selColOrder='organization_id' ;
		end if;
		if (@subqry1='DESC') then 
			set @selColOrder='agtname DESC';
		end if;
		
set @agtDirPlrPwtQuery = concat("select ",@selCol,", sum(totDirPlrPwt) totDirPlrPwt from(select agent_org_id as agt_id, sum(net_amt + agt_claim_comm)as totDirPlrPwt from st_iw_agent_direct_plr_pwt where transaction_id in (select transaction_id from st_lms_agent_transaction_master where date(transaction_date)>='",startDate,"' and date(transaction_date)<'",endDate,"' and transaction_type = 'IW_PWT_AUTO') and game_id =",gameNumber," group by agent_org_id union all select organization_id,sum(direct_pwt_net_amt) as totDirPlrPwt from st_rep_iw_agent where finaldate>='",startDate,"' and finaldate<'",endDate,"' and game_id=",gameNumber," group by organization_id)agt, st_lms_organization_master som, st_lms_state_master sm, st_lms_city_master cm where som.state_code = sm.state_code and cm.city_name = som.city and som.organization_type= 'AGENT' and som.organization_id = agt.agt_id");
IF(stateCode != 'ALL' OR cityCode != 'ALL') THEN
	IF(stateCode != 'ALL') THEN 
		SET @agtDirPlrPwtQuery = CONCAT(@agtDirPlrPwtQuery, " and som.state_code = '",stateCode,"'");
	END IF;
	IF(cityCode != 'ALL') THEN
		SET @agtDirPlrPwtQuery = CONCAT(@agtDirPlrPwtQuery, " AND cm.city_name = '",cityCode,"'");
	END IF;
END IF;
set @agtDirPlrPwtQuery = concat(@agtDirPlrPwtQuery, " group by agt.agt_id order by ",@selColOrder,"");
prepare stmt from @agtDirPlrPwtQuery;
execute stmt;
deallocate prepare stmt;
    END      
#
CREATE      PROCEDURE `getCashChqDetailAgentWise`(startDate date,endDate timestamp,userId int,isExpand boolean)
BEGIN
if(!isExpand && userId!=-1) then
set @cond=concat(" btm.user_id=",userId," and ( btm.transaction_date>='",startDate,"' and btm.transaction_date<='",endDate,"') and trxtable.transaction_id=btm.transaction_id");
else
set @cond=concat(" ( btm.transaction_date>='",startDate,"' and btm.transaction_date<='",endDate,"') and trxtable.transaction_id=btm.transaction_id");
end if;
set @mainQry=concat("select amount,transaction_id,type,generated_id,name from (","select amount,trx.transaction_id,type,generated_id,transaction_date,trx.party_id from (select ifnull(trxtable.amount,0) 'amount',btm.transaction_id,'CASH' type,btm.transaction_date,btm.party_id from st_lms_bo_cash_transaction trxtable, st_lms_bo_transaction_master btm where ",@cond," union select ifnull(trxtable.amount,0) 'amount',btm.transaction_id,'BANK DEPOSIT' type,btm.transaction_date,btm.party_id from st_lms_bo_bank_deposit_transaction trxtable, st_lms_bo_transaction_master btm where ",@cond," union select ifnull(trxtable.cheque_amt,0) 'amount',btm.transaction_id,'CHEQUE' type,btm.transaction_date,btm.party_id from  st_lms_bo_sale_chq trxtable, st_lms_bo_transaction_master btm where trxtable.transaction_type IN ('CHEQUE','CLOSED') and ",@cond," union select ifnull(trxtable.cheque_amt,0) 'amount',btm.transaction_id,'CHQ_BOUNCE' type,btm.transaction_date,btm.party_id from  st_lms_bo_sale_chq trxtable, st_lms_bo_transaction_master btm where trxtable.transaction_type='CHQ_BOUNCE' and ",@cond," union select ifnull(trxtable.amount,0) 'amount',btm.transaction_id,'DEBIT NOTE' type,btm.transaction_date,btm.party_id  from st_lms_bo_debit_note trxtable, st_lms_bo_transaction_master btm where btm.transaction_id=trxtable.transaction_id and (trxtable.transaction_type ='DR_NOTE_CASH' or trxtable.transaction_type ='DR_NOTE') and ",@cond," union select ifnull(trxtable.amount,0) 'amount',btm.transaction_id,'CREDIT NOTE' type,btm.transaction_date,btm.party_id  from st_lms_bo_credit_note trxtable, st_lms_bo_transaction_master btm where btm.transaction_id=trxtable.transaction_id and (trxtable.transaction_type ='CR_NOTE_CASH') and ",@cond,") trx,st_lms_bo_receipts_trn_mapping brm,st_lms_bo_receipts borec where brm.receipt_id= borec.receipt_id "," and trx.transaction_id=brm.transaction_id ) innertab ,st_lms_organization_master slom where innertab.party_id = slom.organization_id order by name,type");

PREPARE stmt FROM @mainQry;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
END      
#
CREATE      PROCEDURE `getCashChqDetailDateWise`(orgId int,startDate Date,endDate date,userId int)
BEGIN
if(userId!=-1)then
	set @cond=concat("trxtable.agent_org_id =",orgId," and btm.user_id=",userId," and ( btm.transaction_date>='",startDate,"' and btm.transaction_date<'",endDate,"') and trxtable.transaction_id=btm.transaction_id");
else
	set @cond=concat("trxtable.agent_org_id =",orgId," and ( btm.transaction_date>='",startDate,"' and btm.transaction_date<'",endDate,"') and trxtable.transaction_id=btm.transaction_id");
end if;
	set @mainQry=concat("select amount,trx.transaction_id,type,generated_id,transaction_date from (select ifnull(trxtable.amount,0) 'amount',btm.transaction_id,'CASH' type,btm.transaction_date from st_lms_bo_cash_transaction trxtable, st_lms_bo_transaction_master btm where ",@cond," union select ifnull(trxtable.amount,0) 'amount',btm.transaction_id,'BANK DEPOSIT' type,btm.transaction_date from st_lms_bo_bank_deposit_transaction trxtable, st_lms_bo_transaction_master btm where ",@cond," union select ifnull(trxtable.cheque_amt,0) 'amount',btm.transaction_id,'CHEQUE' type,btm.transaction_date from  st_lms_bo_sale_chq trxtable, st_lms_bo_transaction_master btm where trxtable.transaction_type IN ('CHEQUE','CLOSED') and ",@cond," union select ifnull(trxtable.cheque_amt,0) 'amount',btm.transaction_id,'CHQ_BOUNCE' type,btm.transaction_date from  st_lms_bo_sale_chq trxtable, st_lms_bo_transaction_master btm where trxtable.transaction_type='CHQ_BOUNCE' and ",@cond," union select ifnull(trxtable.amount,0) 'amount',btm.transaction_id,'DEBIT NOTE' type,btm.transaction_date  from st_lms_bo_debit_note trxtable, st_lms_bo_transaction_master btm where btm.transaction_id=trxtable.transaction_id and (trxtable.transaction_type ='DR_NOTE_CASH' or trxtable.transaction_type ='DR_NOTE') and ",@cond," union select ifnull(trxtable.amount,0) 'amount',btm.transaction_id,'CREDIT NOTE' type,btm.transaction_date  from st_lms_bo_credit_note trxtable, st_lms_bo_transaction_master btm where btm.transaction_id=trxtable.transaction_id and (trxtable.transaction_type ='CR_NOTE_CASH') and ",@cond,") trx,st_lms_bo_receipts_trn_mapping brm,st_lms_bo_receipts borec where brm.receipt_id= borec.receipt_id and borec.party_id=",orgId," and trx.transaction_id=brm.transaction_id order by transaction_date asc");
 
	PREPARE stmt FROM @mainQry;
	EXECUTE stmt;
	DEALLOCATE PREPARE stmt;
    END
#

CREATE      PROCEDURE `getCashChqDetailDayWise`(startDate date,endDate date,userId int,isExpand boolean)
BEGIN
if(!isExpand) then
set @cond=concat("(btm.transaction_date>='",startDate,"' and btm.transaction_date<'",endDate,"') and btm.user_id=",userId,"  and trxtable.transaction_id=btm.transaction_id group by trx_date");
else
set @cond=concat("(btm.transaction_date>='",startDate,"' and btm.transaction_date<'",endDate,"') and trxtable.transaction_id=btm.transaction_id group by trx_date");
end if;
set @mainQry=concat("select sum(amount) amount,type,transaction_date from (select amount,type,trx_date transaction_date from (select ifnull(sum(trxtable.amount),0) 'amount',btm.transaction_id,'CASH' type,SUBSTRING_INDEX(btm.transaction_date,' ',1) trx_date from st_lms_bo_cash_transaction trxtable, st_lms_bo_transaction_master btm where ",@cond," union select ifnull(sum(trxtable.amount),0) 'amount',btm.transaction_id,'BANK_DEPOSIT' type,SUBSTRING_INDEX(btm.transaction_date,' ',1) trx_date from st_lms_bo_bank_deposit_transaction trxtable, st_lms_bo_transaction_master btm where ",@cond," union select ifnull(sum(trxtable.cheque_amt),0) 'amount',btm.transaction_id,'CHEQUE' type,SUBSTRING_INDEX(btm.transaction_date,' ',1) trx_date from  st_lms_bo_sale_chq trxtable, st_lms_bo_transaction_master btm where trxtable.transaction_type IN ('CHEQUE','CLOSED')  and ",@cond
," union select ifnull(sum(trxtable.cheque_amt),0) 'amount',btm.transaction_id,'CHQ_BOUNCE' type,SUBSTRING_INDEX(btm.transaction_date,' ',1) trx_date from  st_lms_bo_sale_chq trxtable, st_lms_bo_transaction_master btm where trxtable.transaction_type='CHQ_BOUNCE' and ",@cond," union select ifnull(sum(trxtable.amount),0) 'amount',btm.transaction_id,'DEBIT_NOTE' type,SUBSTRING_INDEX(btm.transaction_date,' ',1) trx_date  from st_lms_bo_debit_note trxtable, st_lms_bo_transaction_master btm where (trxtable.transaction_type ='DR_NOTE_CASH' or trxtable.transaction_type ='DR_NOTE') and ",@cond," union select ifnull(sum(trxtable.amount),0) 'amount',btm.transaction_id,'CREDIT_NOTE' type,SUBSTRING_INDEX(btm.transaction_date,' ',1) trx_date  from st_lms_bo_credit_note trxtable, st_lms_bo_transaction_master btm where (trxtable.transaction_type ='CR_NOTE_CASH') and ",@cond,") trx
union all 
select sum(cash_amt),'CASH',finaldate from st_rep_bo_payments where finaldate>='",startDate,"' and finaldate<'",endDate,"' group by finaldate
union all
select sum(credit_note),'CREDIT_NOTE',finaldate from st_rep_bo_payments where finaldate>='",startDate,"' and finaldate<'",endDate,"' group by finaldate
union all
select sum(debit_note),'DEBIT_NOTE',finaldate from st_rep_bo_payments where finaldate>='",startDate,"' and finaldate<'",endDate,"' group by finaldate
union all
select sum(cheque_amt),'CHEQUE',finaldate from st_rep_bo_payments where finaldate>='",startDate,"' and finaldate<'",endDate,"' group by finaldate
union all
select sum(cheque_bounce_amt),'CHQ_BOUNCE',finaldate from st_rep_bo_payments where finaldate>='",startDate,"' and finaldate<'",endDate,"' group by finaldate
union all
select sum(bank_deposit),'BANK_DEPOSIT',finaldate from st_rep_bo_payments where finaldate>='",startDate,"' and finaldate<'",endDate,"' group by finaldate) cash group by  type,transaction_date
");

PREPARE stmt FROM @mainQry;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
   END      
#
CREATE      PROCEDURE `getPwtDetails`(startDate TimeStamp,endDate TimeStamp,gameNumber int, IN cityCode VARCHAR(10), IN stateCode VARCHAR(10))
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
			set @selCol='organization_id' ;
		end if;
		if (@subqry1='DESC') then 
			set @selCol='orgCode DESC';
		end if;
set @PwtQuery = concat("select ",concat('som2.', @selCol),", sum(totPwtAgt) totPwtAgt  from(select som.parent_id as agt_id, sum(totPwt) as totPwtAgt from(select retailer_org_id as ret_id, sum(pwt_amt + agt_claim_comm)as totPwt from st_dg_ret_pwt_",gameNumber," where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where date(transaction_date)>='",startDate,"' and date(transaction_date)<'",endDate,"' and transaction_type = 'DG_PWT') group by retailer_org_id)ret, st_lms_organization_master som where som.organization_type= 'RETAILER' and som.organization_id = ret.ret_id group by som.parent_id union all select organization_id,sum(pwt_net_amt) as totPwtAgt from st_rep_dg_agent where finaldate>='",startDate,"' and finaldate<'",endDate,"' and game_id=",gameNumber," group by organization_id )agt, st_lms_organization_master som2 , st_lms_state_master sm, st_lms_city_master cm where som2.state_code = sm.state_code and cm.city_name=som2.city and som2.organization_type='AGENT' and agt.agt_id = som2.organization_id");
IF(stateCode != 'ALL' OR cityCode != 'ALL') THEN
	IF(stateCode != 'ALL') THEN 
		SET @PwtQuery = CONCAT(@PwtQuery, " and som2.state_code = '",stateCode,"'");
	END IF;
	IF(cityCode != 'ALL') THEN
		SET @PwtQuery = CONCAT(@PwtQuery, " AND cm.city_name = '",cityCode,"'");
	END IF;
END IF;
set @PwtQuery = concat(@PwtQuery, " group by agt.agt_id order by ",@selColOrder,"");
Prepare stmt from @PwtQuery;
execute stmt;
deallocate prepare stmt;
    END      
#
CREATE      PROCEDURE `getPwtDetailsIW`(startDate TimeStamp,endDate TimeStamp,gameNumber int, IN cityCode VARCHAR(10), IN stateCode VARCHAR(10))
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
			set @selCol='organization_id' ;
		end if;
		if (@subqry1='DESC') then 
			set @selCol='orgCode DESC';
		end if;
set @PwtQuery = concat("select ",concat('som2.', @selCol),", sum(totPwtAgt) totPwtAgt  from(select som.parent_id as agt_id, sum(totPwt) as totPwtAgt from(select retailer_org_id as ret_id, sum(pwt_amt + agt_claim_comm)as totPwt from st_iw_ret_pwt where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where date(transaction_date)>='",startDate,"' and date(transaction_date)<'",endDate,"' and transaction_type = 'IW_PWT') group by retailer_org_id)ret, st_lms_organization_master som where som.organization_type= 'RETAILER' and som.organization_id = ret.ret_id group by som.parent_id union all select organization_id,sum(pwt_net_amt) as totPwtAgt from st_rep_iw_agent where finaldate>='",startDate,"' and finaldate<'",endDate,"' and game_id=",gameNumber," group by organization_id )agt, st_lms_organization_master som2 , st_lms_state_master sm, st_lms_city_master cm where som2.state_code = sm.state_code and cm.city_name=som2.city and som2.organization_type='AGENT' and agt.agt_id = som2.organization_id");
IF(stateCode != 'ALL' OR cityCode != 'ALL') THEN
	IF(stateCode != 'ALL') THEN 
		SET @PwtQuery = CONCAT(@PwtQuery, " and som2.state_code = '",stateCode,"'");
	END IF;
	IF(cityCode != 'ALL') THEN
		SET @PwtQuery = CONCAT(@PwtQuery, " AND cm.city_name = '",cityCode,"'");
	END IF;
END IF;
set @PwtQuery = concat(@PwtQuery, " group by agt.agt_id order by ",@selColOrder,"");
Prepare stmt from @PwtQuery;
execute stmt;
deallocate prepare stmt;
    END      
#
CREATE      PROCEDURE `getSaleData`(startDate TimeStamp,endDate TimeStamp,gameNumber int, IN cityCode VARCHAR(10), IN stateCode VARCHAR(10))
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
			set @selCol='organization_id' ;
		end if;
		if (@subqry1='DESC') then 
			set @selCol='orgCode DESC';
		end if;
		
set @SaleQueryLeft = concat("select ifnull(sale.orgCode, saleRet.orgCode) as agtName,sale.agt_id, ifnull(sale.totSaleAgt,0) as netSale, ifnull(saleRet.totSaleRetAgt,0) as netSaleRef from (select ",@selCol,",agt_id, totSaleAgt  from(select som.parent_id as agt_id, sum(totSale) as totSaleAgt from(select retailer_org_id as ret_id, sum(agent_net_amt)as totSale from st_dg_ret_sale_",gameNumber," where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_type in ('DG_SALE', 'DG_SALE_OFFLINE') and date(transaction_date)>='",startDate,"' and date(transaction_date)<'",endDate,"') group by retailer_org_id)ret, st_lms_organization_master som where som.organization_type= 'RETAILER' and som.organization_id = ret.ret_id group by som.parent_id)agt, st_lms_organization_master som2 where som2.organization_type='AGENT' and agt.agt_id = som2.organization_id order by  ",@selColOrder,")sale left outer join (select ",@selCol,", totSaleRetAgt  from(select som.parent_id as agt_id, sum(totSaleRet) as totSaleRetAgt from(select retailer_org_id as ret_id, sum(agent_net_amt)as totSaleRet from st_dg_ret_sale_refund_",gameNumber," where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where date(transaction_date)>='",startDate,"' and date(transaction_date)<'",endDate,"' and transaction_type in ('DG_REFUND_CANCEL', 'DG_REFUND_FAILED')) group by retailer_org_id)ret, st_lms_organization_master som where som.organization_type= 'RETAILER' and som.organization_id = ret.ret_id group by som.parent_id)agt, st_lms_organization_master som2 where som2.organization_type='AGENT' and agt.agt_id = som2.organization_id order by ",@selColOrder,")saleRet on sale.orgCode = saleRet.orgCode inner join st_lms_organization_master om1 on om1.organization_id = sale.agt_id inner join st_lms_state_master sm on sm.state_code = om1.state_code inner join st_lms_city_master cm on om1.city = cm.city_name");
IF(stateCode != 'ALL' OR cityCode != 'ALL') THEN
	SET @SaleQueryLeft = CONCAT(@SaleQueryLeft, " WHERE ");
	IF(stateCode != 'ALL') THEN 
		SET @SaleQueryLeft = CONCAT(@SaleQueryLeft, " om1.state_code = '",stateCode,"'");
	END IF;
	IF(cityCode != 'ALL') THEN
		SET @SaleQueryLeft = CONCAT(@SaleQueryLeft, " AND cm.city_name = '",cityCode,"'");
	END IF;
END IF;
set @SaleQueryRight = concat("select ifnull(sale.orgCode, saleRet.orgCode) as agtName,sale.agt_id, ifnull(sale.totSaleAgt,0) as netSale, ifnull(saleRet.totSaleRetAgt,0) as netSaleRef from (select ",@selCol,",agt_id, totSaleAgt  from(select som.parent_id as agt_id, sum(totSale) as totSaleAgt from(select retailer_org_id as ret_id, sum(agent_net_amt)as totSale from st_dg_ret_sale_",gameNumber," where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_type in ('DG_SALE', 'DG_SALE_OFFLINE') and date(transaction_date)>='",startDate,"' and date(transaction_date)<'",endDate,"') group by retailer_org_id)ret, st_lms_organization_master som where som.organization_type= 'RETAILER' and som.organization_id = ret.ret_id group by som.parent_id)agt, st_lms_organization_master som2 where som2.organization_type='AGENT' and agt.agt_id = som2.organization_id order by  ",@selColOrder,")sale right outer join (select ",@selCol,", totSaleRetAgt  from(select som.parent_id as agt_id, sum(totSaleRet) as totSaleRetAgt from(select retailer_org_id as ret_id, sum(agent_net_amt)as totSaleRet from st_dg_ret_sale_refund_",gameNumber," where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where date(transaction_date)>='",startDate,"' and date(transaction_date)<'",endDate,"' and transaction_type in ('DG_REFUND_CANCEL', 'DG_REFUND_FAILED')) group by retailer_org_id)ret, st_lms_organization_master som where som.organization_type= 'RETAILER' and som.organization_id = ret.ret_id group by som.parent_id)agt, st_lms_organization_master som2 where som2.organization_type='AGENT' and agt.agt_id = som2.organization_id order by  ",@selColOrder,")saleRet on sale.orgCode = saleRet.orgCode inner join st_lms_organization_master om1 on om1.organization_id = sale.agt_id inner join st_lms_state_master sm on sm.state_code = om1.state_code inner join st_lms_city_master cm on om1.city = cm.city_name");
IF(stateCode != 'ALL' OR cityCode != 'ALL') THEN
	SET @SaleQueryRight = CONCAT(@SaleQueryRight, " WHERE ");
	IF(stateCode != 'ALL') THEN 
		SET @SaleQueryRight = CONCAT(@SaleQueryRight, " om1.state_code = '",stateCode,"'");
	END IF;
	IF(cityCode != 'ALL') THEN
		SET @SaleQueryRight = CONCAT(@SaleQueryRight, " AND cm.city_name = '",cityCode,"'");
	END IF;
END IF;
set @SaleQueryUnion = concat("select agtName,netSalefinal from (select typ.agtName, sum(typ.netSale - typ.netSaleRef) as netSalefinal from (",@SaleQueryLeft," union ", @SaleQueryRight," union all select ",concat('om.',@selCol),",om.parent_id,ifnull(sum(sale_net),0) ,ifnull(sum(ref_net_amt),0) from st_rep_dg_retailer rep inner join st_lms_organization_master om  on om.organization_id=rep.parent_id inner join st_lms_state_master sm on sm.state_code = om.state_code inner join st_lms_city_master cm on om.city = cm.city_name where finaldate>='",startDate,"' and finaldate<'",endDate,"' and game_id=",gameNumber,"");
IF(stateCode != 'ALL' OR cityCode != 'ALL') THEN
	IF(stateCode != 'ALL') THEN 
		SET @SaleQueryUnion = CONCAT(@SaleQueryUnion, " and om.state_code = '",stateCode,"'");
	END IF;
	IF(cityCode != 'ALL') THEN
		SET @SaleQueryUnion = CONCAT(@SaleQueryUnion, " AND cm.city_name = '",cityCode,"'");
	END IF;
END IF;
set @SaleQueryUnion = concat(@SaleQueryUnion, " group by rep.parent_id )typ group by typ.agtName)typ");
Prepare stmt from @SaleQueryUnion;
execute stmt;
deallocate prepare stmt;
    END      
#
CREATE      PROCEDURE `getSaleDataIW`(startDate TimeStamp,endDate TimeStamp,gameNumber int, IN cityCode VARCHAR(10), IN stateCode VARCHAR(10))
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
			set @selCol='organization_id' ;
		end if;
		if (@subqry1='DESC') then 
			set @selCol='orgCode DESC';
		end if;
		
set @SaleQueryLeft = concat("select ifnull(sale.orgCode, saleRet.orgCode) as agtName,sale.agt_id, ifnull(sale.totSaleAgt,0) as netSale, ifnull(saleRet.totSaleRetAgt,0) as netSaleRef from (select ",@selCol,",agt_id, totSaleAgt  from(select som.parent_id as agt_id, sum(totSale) as totSaleAgt from(select retailer_org_id as ret_id, sum(agent_net_amt)as totSale from st_iw_ret_sale where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_type in ('IW_SALE') and date(transaction_date)>='",startDate,"' and date(transaction_date)<'",endDate,"') group by retailer_org_id)ret, st_lms_organization_master som where som.organization_type= 'RETAILER' and som.organization_id = ret.ret_id group by som.parent_id)agt, st_lms_organization_master som2 where som2.organization_type='AGENT' and agt.agt_id = som2.organization_id order by  ",@selColOrder,")sale left outer join (select ",@selCol,", totSaleRetAgt  from(select som.parent_id as agt_id, sum(totSaleRet) as totSaleRetAgt from(select retailer_org_id as ret_id, sum(agent_net_amt)as totSaleRet from st_iw_ret_sale_refund where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where date(transaction_date)>='",startDate,"' and date(transaction_date)<'",endDate,"' and transaction_type in ('IW_REFUND_CANCEL')) group by retailer_org_id)ret, st_lms_organization_master som where som.organization_type= 'RETAILER' and som.organization_id = ret.ret_id group by som.parent_id)agt, st_lms_organization_master som2 where som2.organization_type='AGENT' and agt.agt_id = som2.organization_id order by ",@selColOrder,")saleRet on sale.orgCode = saleRet.orgCode inner join st_lms_organization_master om1 on om1.organization_id = sale.agt_id inner join st_lms_state_master sm on sm.state_code = om1.state_code inner join st_lms_city_master cm on om1.city = cm.city_name");
IF(stateCode != 'ALL' OR cityCode != 'ALL') THEN
	SET @SaleQueryLeft = CONCAT(@SaleQueryLeft, " WHERE ");
	IF(stateCode != 'ALL') THEN 
		SET @SaleQueryLeft = CONCAT(@SaleQueryLeft, " om1.state_code = '",stateCode,"'");
	END IF;
	IF(cityCode != 'ALL') THEN
		SET @SaleQueryLeft = CONCAT(@SaleQueryLeft, " AND cm.city_name = '",cityCode,"'");
	END IF;
END IF;
set @SaleQueryRight = concat("select ifnull(sale.orgCode, saleRet.orgCode) as agtName,sale.agt_id, ifnull(sale.totSaleAgt,0) as netSale, ifnull(saleRet.totSaleRetAgt,0) as netSaleRef from (select ",@selCol,",agt_id, totSaleAgt  from(select som.parent_id as agt_id, sum(totSale) as totSaleAgt from(select retailer_org_id as ret_id, sum(agent_net_amt)as totSale from st_iw_ret_sale where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_type in ('IW_SALE') and date(transaction_date)>='",startDate,"' and date(transaction_date)<'",endDate,"') group by retailer_org_id)ret, st_lms_organization_master som where som.organization_type= 'RETAILER' and som.organization_id = ret.ret_id group by som.parent_id)agt, st_lms_organization_master som2 where som2.organization_type='AGENT' and agt.agt_id = som2.organization_id order by  ",@selColOrder,")sale right outer join (select ",@selCol,", totSaleRetAgt  from(select som.parent_id as agt_id, sum(totSaleRet) as totSaleRetAgt from(select retailer_org_id as ret_id, sum(agent_net_amt)as totSaleRet from st_iw_ret_sale_refund where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where date(transaction_date)>='",startDate,"' and date(transaction_date)<'",endDate,"' and transaction_type in ('IW_REFUND_CANCEL')) group by retailer_org_id)ret, st_lms_organization_master som where som.organization_type= 'RETAILER' and som.organization_id = ret.ret_id group by som.parent_id)agt, st_lms_organization_master som2 where som2.organization_type='AGENT' and agt.agt_id = som2.organization_id order by  ",@selColOrder,")saleRet on sale.orgCode = saleRet.orgCode inner join st_lms_organization_master om1 on om1.organization_id = sale.agt_id inner join st_lms_state_master sm on sm.state_code = om1.state_code inner join st_lms_city_master cm on om1.city = cm.city_name");
IF(stateCode != 'ALL' OR cityCode != 'ALL') THEN
	SET @SaleQueryRight = CONCAT(@SaleQueryRight, " WHERE ");
	IF(stateCode != 'ALL') THEN 
		SET @SaleQueryRight = CONCAT(@SaleQueryRight, " om1.state_code = '",stateCode,"'");
	END IF;
	IF(cityCode != 'ALL') THEN
		SET @SaleQueryRight = CONCAT(@SaleQueryRight, " AND cm.city_name = '",cityCode,"'");
	END IF;
END IF;
set @SaleQueryUnion = concat("select agtName,netSalefinal from (select typ.agtName, sum(typ.netSale - typ.netSaleRef) as netSalefinal from (",@SaleQueryLeft," union ", @SaleQueryRight," union all select ",concat('om.',@selCol),",om.parent_id,ifnull(sum(sale_net),0) ,ifnull(sum(ref_net_amt),0) from st_rep_iw_retailer rep inner join st_lms_organization_master om  on om.organization_id=rep.parent_id inner join st_lms_state_master sm on sm.state_code = om.state_code inner join st_lms_city_master cm on om.city = cm.city_name where finaldate>='",startDate,"' and finaldate<'",endDate,"' and game_id=",gameNumber,"");
IF(stateCode != 'ALL' OR cityCode != 'ALL') THEN
	IF(stateCode != 'ALL') THEN 
		SET @SaleQueryUnion = CONCAT(@SaleQueryUnion, " and om.state_code = '",stateCode,"'");
	END IF;
	IF(cityCode != 'ALL') THEN
		SET @SaleQueryUnion = CONCAT(@SaleQueryUnion, " AND cm.city_name = '",cityCode,"'");
	END IF;
END IF;
set @SaleQueryUnion = concat(@SaleQueryUnion, " group by rep.parent_id )typ group by typ.agtName)typ");
Prepare stmt from @SaleQueryUnion;
execute stmt;
deallocate prepare stmt;
    END
#
CREATE      PROCEDURE `getScratchSaleAgentGameId`(agentOrgId int,startDate date,endDate date)
BEGIN
set @scratchQry=concat("select distinct bo.game_id from st_se_agent_retailer_transaction bo,st_lms_agent_transaction_master btm where btm.transaction_id=bo.transaction_id and btm.user_org_id=",agentOrgId," and ( btm.transaction_date>='",startDate,"' and btm.transaction_date<'",endDate,"') union select distinct bo.game_id from st_se_bo_agent_transaction bo,st_lms_bo_transaction_master btm 	where btm.transaction_id=bo.transaction_id  and bo.agent_org_id=",agentOrgId,"  and ( btm.transaction_date>='",startDate,"'  and btm.transaction_date<'",endDate,"') order by game_id desc");
	prepare stmt from @scratchQry;
	execute stmt;
	deallocate prepare stmt;
    END      
#
CREATE      PROCEDURE `getScratchSaleDetailGameWise`(agentOrgId int,startDate date,endDate date,gameId int)
BEGIN
	set @saleQry=concat("select ifnull(e.ee,0) 'remaining_books',ifnull(a.aa,0) 'books_purchase_from_bo', ifnull(b.bb,0) 'books_returned_to_bo' ,ifnull(c.cc,0) 'books_sale_to_retailer', ifnull(d.dd,0) 'books_returned_by_retailer',ifnull(f.aa1,0) 'tickets_purchase_from_bo', ifnull(g.bb1,0) 'tickets_returned_to_bo' ,ifnull(h.cc1,0) 'tickets_sale_to_retailer', ifnull(i.dd1,0) 'tickets_returned_by_retailer' from ( (select sum(nbr_of_books) aa from st_se_bo_agent_transaction bo, st_lms_bo_transaction_master btm where btm.transaction_id=bo.transaction_id and bo.transaction_type ='SALE' and bo.agent_org_id=",agentOrgId," and ( btm.transaction_date>='",startDate,"' and btm.transaction_date<'",endDate,"') and game_id =",gameId," ) a , ( select sum(nbr_of_books) bb from st_se_bo_agent_transaction bo, st_lms_bo_transaction_master btm where btm.transaction_id=bo.transaction_id and bo.transaction_type ='SALE_RET' and bo.agent_org_id=",agentOrgId," and ( btm.transaction_date>='",startDate,"' and btm.transaction_date<'",endDate,"') and game_id =",gameId,"  )b , (select sum(nbr_of_books) cc from st_se_agent_retailer_transaction bo, st_lms_agent_transaction_master btm where btm.transaction_id=bo.transaction_id and bo.transaction_type ='SALE' and btm.user_org_id=",agentOrgId," and ( btm.transaction_date>='",startDate,"' and btm.transaction_date<'",endDate,"') and game_id =",gameId,"  ) c, (select sum(nbr_of_books) dd from st_se_agent_retailer_transaction bo, st_lms_agent_transaction_master btm where btm.transaction_id=bo.transaction_id and bo.transaction_type ='SALE_RET' and btm.user_org_id=",agentOrgId,"  and ( btm.transaction_date>='",startDate,"' and btm.transaction_date<'",endDate,"') and game_id =
",gameId ," )d,(select sum(nbrOfTickets) aa1 from st_se_bo_agent_loose_book_transaction bo, st_lms_bo_transaction_master btm where btm.transaction_id=bo.transaction_id and bo.transaction_type ='LOOSE_SALE' and bo.agent_org_id=",agentOrgId," and ( btm.transaction_date>='",startDate,"' and btm.transaction_date<'",endDate,"') and game_id =",gameId," ) f , ( select sum(nbrOfTickets) bb1 from st_se_bo_agent_loose_book_transaction bo, st_lms_bo_transaction_master btm where btm.transaction_id=bo.transaction_id and bo.transaction_type ='LOOSE_SALE_RET' and bo.agent_org_id=",agentOrgId," and ( btm.transaction_date>='",startDate,"' and btm.transaction_date<'",endDate,"') and game_id =",gameId," )g ,(select sum(nbr_of_tickets) cc1 from st_se_agent_ret_loose_book_transaction bo, st_lms_agent_transaction_master btm where btm.transaction_id=bo.transaction_id and bo.transaction_type ='LOOSE_SALE' and btm.user_org_id=",agentOrgId,"  and ( btm.transaction_date>='",startDate,"' and btm.transaction_date<'",endDate,"') and game_id =",gameId,"  ) h, (select sum(nbr_of_tickets) dd1 from st_se_agent_ret_loose_book_transaction bo, st_lms_agent_transaction_master btm where btm.transaction_id=bo.transaction_id and bo.transaction_type ='LOOSE_SALE_RET' and btm.user_org_id=",agentOrgId," and ( btm.transaction_date>='",startDate,"' and btm.transaction_date<'",endDate,"') and game_id =",gameId,"  )i,(select count(book_nbr) ee from st_se_game_inv_status where current_owner='AGENT' and current_owner_id=",agentOrgId," and game_id=",gameId,") e)" );
prepare stmt from @saleQry;
execute stmt;
deallocate prepare stmt;
    END
#
