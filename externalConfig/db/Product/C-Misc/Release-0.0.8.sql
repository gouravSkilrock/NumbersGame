--liquibase formatted sql

--changeset BaseSPRMS:10 endDelimiter:#






#
CREATE      PROCEDURE `getStCashCheqReportRetailerId`(agentOrgId int,startDate date,endDate date)
BEGIN
	set @mainQry=concat("select distinct retailer_org_id from(
select distinct cash.retailer_org_id from st_lms_agent_cash_transaction cash, st_lms_agent_transaction_master btm where btm.transaction_id=cash.transaction_id and cash.retailer_org_id= btm.party_id and  btm.user_org_id=",agentOrgId," and (btm.transaction_date>='",startDate,"' and btm.transaction_date<'",endDate,"') union select distinct chq.retailer_org_id from st_lms_agent_sale_chq chq, st_lms_agent_transaction_master btm where btm.transaction_id=chq.transaction_id and chq.retailer_org_id= btm.party_id and btm.user_org_id=",agentOrgId,"  and (btm.transaction_date>='",startDate,"' and btm.transaction_date<'",endDate,"') union select distinct dbt.retailer_org_id from st_lms_agent_debit_note dbt, st_lms_agent_transaction_master btm where btm.transaction_id=dbt.transaction_id and dbt.retailer_org_id= btm.party_id and btm.user_org_id=",agentOrgId,"  and ( btm.transaction_date>='",startDate,"' and btm.transaction_date<'",endDate,"') union select distinct dbt.retailer_org_id from st_lms_agent_credit_note dbt, st_lms_agent_transaction_master btm where btm.transaction_id=dbt.transaction_id and dbt.retailer_org_id= btm.party_id and btm.user_org_id=",agentOrgId," and ( btm.transaction_date>='",startDate,"' and btm.transaction_date<'",endDate,"')
union all
select distinct retailer_org_id from st_rep_agent_payments where parent_id=",agentOrgId," and finaldate>='",startDate,"'  and finaldate<='",endDate,"')cash
");
PREPARE stmt FROM @mainQry;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
    END  


   #
#

CREATE      PROCEDURE `getStCashCheqReportBo3`(agentOrgId int,startDate date,endDate date,userId int,isExpand boolean)
BEGIN
	if(!isExpand) then 
	set @mainQry=concat("select sum(total_cash) 'total_cash', sum(credit) 'credit', sum(debit) 'debit', sum(cheque_coll) 'cheque_coll', sum(bounce) 'bounce',sum(bank_deposit) bank_deposit,sum(net_amount) 'net_amount' from (select aa.a 'total_cash', bb.b 'cheque_coll', cc.c 'bounce' , gg.debit_amt 'debit', dd.credit_amt 'credit',bd.d 'bank_deposit', ((aa.a+bb.b+dd.credit_amt+bd.d)-(cc.c+gg.debit_amt))'net_amount' from (( select ifnull(sum(cash.amount),0) 'a' from st_lms_bo_cash_transaction cash, st_lms_bo_transaction_master btm where cash.agent_org_id=",agentOrgId," and btm.user_id=",userId," and ( btm.transaction_date>='",startDate,"' and btm.transaction_date<'",endDate,"') and cash.transaction_id=btm.transaction_id ) aa, ( select ifnull(sum(chq.cheque_amt),0) 'b' from  st_lms_bo_sale_chq chq, st_lms_bo_transaction_master btm where chq.transaction_type IN ('CHEQUE','CLOSED') and chq.agent_org_id=",agentOrgId," and btm.user_id=",userId," and ( btm.transaction_date>='",startDate,"' and btm.transaction_date<'",endDate,"') and chq.transaction_id=btm.transaction_id ) bb, ( select ifnull(sum(chq.cheque_amt),0) 'c' from  st_lms_bo_sale_chq chq, st_lms_bo_transaction_master btm where chq.transaction_type='CHQ_BOUNCE' and chq.agent_org_id=",agentOrgId," and btm.user_id=",userId," and ( btm.transaction_date>='",startDate,"' and btm.transaction_date<'",endDate,"') and chq.transaction_id=btm.transaction_id ) cc,(select ifnull(sum(bo.amount),0) 'debit_amt'  from st_lms_bo_debit_note bo, st_lms_bo_transaction_master btm where btm.transaction_id=bo.transaction_id and (bo.transaction_type ='DR_NOTE_CASH' or bo.transaction_type ='DR_NOTE') and agent_org_id =",agentOrgId," and btm.user_id=",userId," and ( btm.transaction_date>='",startDate,"' and btm.transaction_date<'",endDate,"') )gg,(select ifnull(sum(bo.amount),0) 'credit_amt'  from st_lms_bo_credit_note bo, st_lms_bo_transaction_master btm where btm.transaction_id=bo.transaction_id and (bo.transaction_type ='CR_NOTE_CASH') and agent_org_id =",agentOrgId," and btm.user_id=",userId," and ( btm.transaction_date>='",startDate,"' and btm.transaction_date<'",endDate,"') )dd,(select ifnull(sum(bank.amount),0) 'd' from st_lms_bo_bank_deposit_transaction bank, st_lms_bo_transaction_master btm where bank.agent_org_id =",agentOrgId," and btm.user_id=",userId," and ( btm.transaction_date>='",startDate,"' and btm.transaction_date<'",endDate,"') and bank.transaction_id=btm.transaction_id ) bd)
union all 
select 	sum(cash_amt) 'total_cash', sum(cheque_amt) 'cheque_coll', sum(cheque_bounce_amt) 'bounce', sum(debit_note) 'debit', sum(credit_note) 'credit',sum(bank_deposit) 'bank_deposit',sum(((cash_amt+cheque_amt+credit_note+bank_deposit)-(cheque_bounce_amt+debit_note))) as 'net_amount' from st_rep_bo_payments where agent_org_id=",agentOrgId," and finaldate>='",startDate,"' and finaldate<'",endDate,"')cash
");
else 
set @mainQry=concat("select sum(total_cash) 'total_cash', sum(credit) 'credit', sum(debit) 'debit', sum(cheque_coll) 'cheque_coll', sum(bounce) 'bounce',sum(bank_deposit) bank_deposit,sum(net_amount) 'net_amount' from (select aa.a 'total_cash', bb.b 'cheque_coll', cc.c 'bounce' , gg.debit_amt 'debit', dd.credit_amt 'credit',bd.d 'bank_deposit', ((aa.a+bb.b+dd.credit_amt+bd.d)-(cc.c+gg.debit_amt))'net_amount' from (( select ifnull(sum(cash.amount),0) 'a' from st_lms_bo_cash_transaction cash, st_lms_bo_transaction_master btm where cash.agent_org_id=",agentOrgId," and ( btm.transaction_date>='",startDate,"' and btm.transaction_date<'",endDate,"') and cash.transaction_id=btm.transaction_id ) aa, ( select ifnull(sum(chq.cheque_amt),0) 'b' from  st_lms_bo_sale_chq chq, st_lms_bo_transaction_master btm where chq.transaction_type IN ('CHEQUE','CLOSED') and chq.agent_org_id=",agentOrgId," and ( btm.transaction_date>='",startDate,"' and btm.transaction_date<'",endDate,"') and chq.transaction_id=btm.transaction_id ) bb, ( select ifnull(sum(chq.cheque_amt),0) 'c' from  st_lms_bo_sale_chq chq, st_lms_bo_transaction_master btm where chq.transaction_type='CHQ_BOUNCE' and chq.agent_org_id=",agentOrgId," and ( btm.transaction_date>='",startDate,"' and btm.transaction_date<'",endDate,"') and chq.transaction_id=btm.transaction_id ) cc,(select ifnull(sum(bo.amount),0) 'debit_amt'  from st_lms_bo_debit_note bo, st_lms_bo_transaction_master btm where btm.transaction_id=bo.transaction_id and (bo.transaction_type ='DR_NOTE_CASH' or bo.transaction_type ='DR_NOTE') and agent_org_id =",agentOrgId," and ( btm.transaction_date>='",startDate,"' and btm.transaction_date<'",endDate,"') )gg,(select ifnull(sum(bo.amount),0) 'credit_amt'  from st_lms_bo_credit_note bo, st_lms_bo_transaction_master btm where btm.transaction_id=bo.transaction_id and (bo.transaction_type ='CR_NOTE_CASH') and agent_org_id =",agentOrgId," and ( btm.transaction_date>='",startDate,"' and btm.transaction_date<'",endDate,"') )dd,(select ifnull(sum(bank.amount),0) 'd' from st_lms_bo_bank_deposit_transaction bank, st_lms_bo_transaction_master btm where bank.agent_org_id =",agentOrgId," and ( btm.transaction_date>='",startDate,"' and btm.transaction_date<'",endDate,"') and bank.transaction_id=btm.transaction_id ) bd)
union all 
select 	sum(cash_amt) 'total_cash', sum(cheque_amt) 'cheque_coll', sum(cheque_bounce_amt) 'bounce', sum(debit_note) 'debit', sum(credit_note) 'credit',sum(bank_deposit) 'bank_deposit',sum(((cash_amt+cheque_amt+credit_note+bank_deposit)-(cheque_bounce_amt+debit_note))) as 'net_amount' from st_rep_bo_payments where agent_org_id=",agentOrgId," and finaldate>='",startDate,"' and finaldate<'",endDate,"')cash
");
end if;

	PREPARE stmt FROM @mainQry;
	EXECUTE stmt;
	DEALLOCATE PREPARE stmt;
    END

#
