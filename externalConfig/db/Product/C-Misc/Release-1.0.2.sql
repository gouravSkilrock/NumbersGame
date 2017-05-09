--liquibase formatted sql

--changeset BaseSPRMS:17 endDelimiter:#

#
CREATE      PROCEDURE `scratchPwtGameWiseExpand`(startDate TIMESTAMP,endDate TIMESTAMP)
BEGIN
    SET @pwtQry=CONCAT("SELECT aa.game_id gameNo,game_name gameName , SUM(noOfTkt) noOfTkt,SUM(priceAmt)priceAmt,SUM(netAmt) netAmt FROM(
SELECT game_id,pwt_amt priceAmt, COUNT(*) noOfTkt,SUM(pwt_amt) netAmt  FROM st_se_bo_pwt bpwt, st_lms_bo_transaction_master btm  WHERE btm.transaction_id=bpwt.transaction_id AND ( btm.transaction_date>='",startDate,"' AND btm.transaction_date<'",endDate,"') GROUP BY pwt_amt, game_id UNION ALL SELECT game_id,pwt_amt priceAmt,COUNT(pwt_amt) noOfTkt,SUM(pwt_amt) netAmt FROM st_se_direct_player_pwt WHERE transaction_date>='",startDate,"' AND transaction_date<='",endDate,"' GROUP BY pwt_amt,game_id)aa INNER JOIN st_se_game_master bb ON aa.game_id=bb.game_id GROUP BY aa.game_id,priceAmt");
    PREPARE stmt FROM @pwtQry;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;
    END  
#
CREATE      PROCEDURE `getStCashCheqReportBo2`(agentOrgId int,startDate date,endDate date)
BEGIN
	set @mainQry=concat("select sum(total_cash) 'total_cash', sum(credit) 'credit', sum(debit) 'debit', sum(cheque_coll) 'cheque_coll', sum(bounce) 'bounce',sum(bank_amt) bank_amt,sum(net_amount) 'net_amount' from (select aa.a 'total_cash', bb.b 'cheque_coll', cc.c 'bounce' , gg.debit_amt 'debit', dd.credit_amt 'credit',hh.bank_amt  bank_amt, ((aa.a+bb.b+dd.credit_amt+hh.bank_amt)-(cc.c+gg.debit_amt))'net_amount' from (( select ifnull(sum(cash.amount),0) 'a' from st_lms_bo_cash_transaction cash, st_lms_bo_transaction_master btm where cash.agent_org_id=",agentOrgId," and ( btm.transaction_date>='",startDate,"' and btm.transaction_date<'",endDate,"') and cash.transaction_id=btm.transaction_id ) aa, ( select ifnull(sum(chq.cheque_amt),0) 'b' from  st_lms_bo_sale_chq chq, st_lms_bo_transaction_master btm where chq.transaction_type IN ('CHEQUE','CLOSED') and chq.agent_org_id=",agentOrgId," and ( btm.transaction_date>='",startDate,"' and btm.transaction_date<'",endDate,"') and chq.transaction_id=btm.transaction_id ) bb, ( select ifnull(sum(chq.cheque_amt),0) 'c' from  st_lms_bo_sale_chq chq, st_lms_bo_transaction_master btm where chq.transaction_type='CHQ_BOUNCE' and chq.agent_org_id=",agentOrgId," and ( btm.transaction_date>='",startDate,"' and btm.transaction_date<'",endDate,"') and chq.transaction_id=btm.transaction_id ) cc,(select ifnull(sum(bo.amount),0) 'debit_amt'  from st_lms_bo_debit_note bo, st_lms_bo_transaction_master btm where btm.transaction_id=bo.transaction_id and (bo.transaction_type ='DR_NOTE_CASH' or bo.transaction_type ='DR_NOTE') and agent_org_id =",agentOrgId," and ( btm.transaction_date>='",startDate,"' and btm.transaction_date<'",endDate,"') )gg,(select ifnull(sum(bo.amount),0) 'credit_amt'  from st_lms_bo_credit_note bo, st_lms_bo_transaction_master btm where btm.transaction_id=bo.transaction_id and (bo.transaction_type ='CR_NOTE_CASH') and agent_org_id =",agentOrgId," and ( btm.transaction_date>='",startDate,"' and btm.transaction_date<'",endDate,"') )dd,(select ifnull(sum(bo.amount),0) 'bank_amt'  from st_lms_bo_bank_deposit_transaction bo, st_lms_bo_transaction_master btm where btm.transaction_id=bo.transaction_id and btm.transaction_type='BANK_DEPOSIT' and agent_org_id =",agentOrgId," and ( btm.transaction_date>='",startDate,"' and btm.transaction_date<'",endDate,"'))hh )
union all 
select 	sum(cash_amt) 'total_cash', sum(cheque_amt) 'cheque_coll', sum(cheque_bounce_amt) 'bounce', sum(debit_note) 'debit', sum(credit_note) 'credit' , sum(bank_deposit) bank_deposit,sum(((cash_amt+cheque_amt+credit_note+bank_deposit)-(cheque_bounce_amt+debit_note))) as 'net_amount'
from st_rep_bo_payments where agent_org_id=",agentOrgId," and finaldate>='",startDate,"' and finaldate<='",endDate,"')cash
");
PREPARE stmt FROM @mainQry;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
    END 
#



