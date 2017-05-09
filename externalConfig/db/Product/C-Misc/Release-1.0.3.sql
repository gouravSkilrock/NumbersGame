--liquibase formatted sql

--changeset BaseSPRMS:18 endDelimiter:#

#
CREATE      PROCEDURE `getStCashCheqReportDetail`(agentOrgId int,startDate date,endDate date,retailerOrgId int)
BEGIN
       
	set @mainQry=concat("select sum(total_cash) 'total_cash', sum(credit_amt) 'credit_amt', sum(debit_amt) 'debit_amt', sum(chq_coll) 'chq_coll', sum(chq_bounce) 'chq_bounce',sum(net_amount) 'net_amount' from (select sum(main.total_cash) as 'total_cash' ,sum(main.credit_amt) as 'credit_amt', sum(main.debit_amt) as 'debit_amt', sum(main.chq_coll) as  'chq_coll',sum(main.chq_bounce)  as 'chq_bounce' ,sum(main.net_amount) as 'net_amount' from
(select aa.cash_amt 'total_cash', kk.credit_amt, gg.debit_amt, bb.chq 'chq_coll',cc.chq_bounce 'chq_bounce', ((aa.cash_amt+bb.chq+kk.credit_amt)-(cc.chq_bounce+gg.debit_amt)) 'net_amount' from ((select ifnull(sum(cash.amount),0) 'cash_amt' from st_lms_agent_cash_transaction cash, st_lms_agent_transaction_master btm where btm.user_org_id=",agentOrgId," and (btm.transaction_date>='",startDate,"' and btm.transaction_date<'",endDate,"') and cash.retailer_org_id=",retailerOrgId," and btm.transaction_type='CASH' and cash.transaction_id=btm.transaction_id ) aa, (select ifnull(sum(chq.cheque_amt),0) 'chq' from  st_lms_agent_sale_chq chq, st_lms_agent_transaction_master btm where chq.transaction_type IN ('CHEQUE','CLOSED') and btm.user_org_id=",agentOrgId," and (btm.transaction_date>='",startDate,"' and btm.transaction_date<'",endDate,"') and chq.retailer_org_id=",retailerOrgId," and chq.transaction_id=btm.transaction_id)  bb, (select ifnull(sum(chq.cheque_amt),0) 'chq_bounce' from  st_lms_agent_sale_chq chq, st_lms_agent_transaction_master btm where chq.transaction_type='CHQ_BOUNCE' and btm.user_org_id=",agentOrgId," and (btm.transaction_date>='",startDate,"' and btm.transaction_date<'",endDate,"') and chq.retailer_org_id=",retailerOrgId,"  and chq.transaction_id=btm.transaction_id ) cc, (select ifnull(sum(bo.amount),0) 'debit_amt'  from st_lms_agent_debit_note bo, st_lms_agent_transaction_master btm where btm.transaction_id=bo.transaction_id and (bo.transaction_type ='DR_NOTE_CASH' or bo.transaction_type ='DR_NOTE' ) and btm.user_org_id=",agentOrgId," and bo.retailer_org_id =",retailerOrgId," and ( btm.transaction_date>='",startDate,"' and btm.transaction_date<'",endDate,"'))gg, (select ifnull(sum(bo.amount),0) 'credit_amt'  from st_lms_agent_credit_note bo, st_lms_agent_transaction_master btm where btm.transaction_id=bo.transaction_id and (bo.transaction_type ='CR_NOTE_CASH' or bo.transaction_type ='CR_NOTE' ) and btm.user_org_id=",agentOrgId," and bo.retailer_org_id =",retailerOrgId," and ( btm.transaction_date>='",startDate,"' and btm.transaction_date<'",endDate,"'))kk))as main
union all 
select 	sum(cash_amt) 'total_cash', sum(credit_note) 'credit_amt', sum(debit_note) 'debit_amt', sum(cheque_amt) 'chq_coll', sum(cheque_bounce_amt) 'chq_bounce',sum(((cash_amt+cheque_amt+credit_note)-(cheque_bounce_amt+debit_note))) as 'net_amount'
	from st_rep_agent_payments where retailer_org_id=",retailerOrgId," and parent_id=",agentOrgId," and finaldate>='",startDate,"' and finaldate<='",endDate,"')cash
");
PREPARE stmt FROM @mainQry;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
    END
#



