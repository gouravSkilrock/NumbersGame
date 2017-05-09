/*
 * � copyright 2007, SkilRock Technologies, A division of Sugal & Damani Lottery Agency Pvt. Ltd.
 * All Rights Reserved
 * The contents of this file are the property of Sugal & Damani Lottery Agency Pvt. Ltd.
 * and are subject to a License agreement with Sugal & Damani Lottery Agency Pvt. Ltd.; you may
 * not use this file except in compliance with that License.  You may obtain a
 * copy of that license from:
 * Legal Department
 * Sugal & Damani Lottery Agency Pvt. Ltd.
 * 6/35,WEA, Karol Bagh,
 * New Delhi
 * India - 110005
 * This software is distributed under the License and is provided on an �AS IS�
 * basis, without warranty of any kind, either express or implied, unless
 * otherwise provided in the License.  See the License for governing rights and
 * limitations under the License.
 */

package com.skilrock.lms.common.db;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MySqlQueries {
	public static final String FETCH_EXISTED_INV_LIST_QUERY = "SELECT serial_no, current_owner_org_id, model_id  FROM st_lms_inv_status WHERE model_id=? and current_owner_user_id=? and current_owner_org_id=? ";

	public static final String GET_Agent_LATEST_DR_NOTE_NBR = "SELECT * from st_lms_agent_receipts where (receipt_type=? or receipt_type=?) and agent_org_id=? ORDER BY generated_id DESC LIMIT 1";
	public static final String GET_AGENT_LATEST_RECEIPT_NBR = "SELECT * from st_lms_agent_receipts where receipt_type = ? and agent_org_id=?  ORDER BY generated_id DESC LIMIT 1";
	public static final String GET_AGT_REC_BOOKWISE_DATE = "select recon_date from st_lms_agent_recon_bookwise where organization_id=? order by recon_date desc limit 1";
	public static final String GET_AGT_REC_TKTWISE_DATE = "select recon_date from st_lms_agent_recon_ticketwise where organization_id=? order by recon_date desc limit 1";
	public static final String ST2_AGENT_CS_RFND = "select sart.retailer_org_id as retailer_org_id,satm.transaction_date,sart.net_amt,sog.name from st_lms_agent_transaction_master satm , st_cs_agt_refund sart , st_lms_organization_master sog where satm.transaction_id  = sart.transaction_id and sart.retailer_org_id = sog.organization_id and satm.transaction_id = ?";
	public static final String ST2_AGENT_OLA_RFND = "select sart.retailer_org_id as retailer_org_id,satm.transaction_date,sart.net_amt,sog.name from st_lms_agent_transaction_master satm , st_ola_agt_deposit_refund sart , st_lms_organization_master sog where satm.transaction_id  = sart.transaction_id and sart.retailer_org_id = sog.organization_id and satm.transaction_id = ?";
	public static final String GET_BO_LATEST_DR_NOTE_NBR = "SELECT * from st_lms_bo_receipts where receipt_type=? or receipt_type=? ORDER BY generated_id DESC LIMIT 1";
	public static final String GET_BO_LATEST_RECEIPT_NBR = "SELECT * from st_lms_bo_receipts where receipt_type=?  ORDER BY generated_id DESC LIMIT 1";
	public static final String GET_RET_LATEST_RECEIPT_NBR = "SELECT * from st_ret_receipts where receipt_type=? and ret_org_id=?  ORDER BY generated_id DESC LIMIT 1";
	public static final String ST2_AGENT_CS_SALE = "select sadg.retailer_org_id as retailer_org_id,satm.transaction_date,sadg.net_amt,sog.name from st_lms_agent_transaction_master satm , st_cs_agt_sale sadg , st_lms_organization_master sog where satm.transaction_id  = sadg.transaction_id and sadg.retailer_org_id = sog.organization_id and satm.transaction_id = ?";
	public static final String ST2_AGENT_OLA_DEPOSIT = "select sadg.retailer_org_id as retailer_org_id,satm.transaction_date,sadg.net_amt,sog.name from st_lms_agent_transaction_master satm , st_ola_agt_deposit sadg , st_lms_organization_master sog where satm.transaction_id  = sadg.transaction_id and sadg.retailer_org_id = sog.organization_id and satm.transaction_id = ?";	
	public static final String GET_RET_REC_TKT_WISE_DATE = "select recon_date from st_lms_ret_recon_ticketwise where agent_org_id=? and retailer_org_id=? order by recon_date desc limit 1";
	public static final String INSERT_AGENT_RECEIPT = "insert into st_lms_agent_receipts(receipt_id,receipt_type,agent_org_id,party_id,party_type,generated_id,voucher_date) values(?,?,?,?,?,?,?)";
	public static final String INSERT_AGENT_RECEIPTS_TRN_MAPPING = "INSERT INTO st_lms_agent_receipts_trn_mapping (receipt_id,transaction_id) VALUES (?,?)";
	public static final String INSERT_AGENT_RECON_BOOKWISE = "SELECT * from st_lms_agent_receipts where (receipt_type=? or receipt_type=?) and agent_org_id=? ORDER BY generated_id DESC LIMIT 1";
	public static final String INSERT_AGENT_TRANSACTION_MASTER = "INSERT INTO st_lms_agent_transaction_master (transaction_id,user_id,user_org_id,party_type,party_id,transaction_type,transaction_date) VALUES (?,?,?,?,?,?,?)";
	public static final String INSERT_BO_RECEIPT = "insert into st_lms_bo_receipts(receipt_id,receipt_type,party_id,party_type,generated_id,voucher_date) values(?,?,?,?,?,?)";
	public static final String INSERT_BO_RECEIPTS_TRN_MAPPING = "INSERT INTO st_lms_bo_receipts_trn_mapping (receipt_id,transaction_id) VALUES (?,?)";
	public static final String INSERT_BO_TRANSACTION_MASTER = "INSERT INTO st_lms_bo_transaction_master (transaction_id,user_id,user_org_id,party_type,party_id,transaction_date,transaction_type) VALUES (?,?,?,?,?,?,?)";
	public static final String INSERT_PWT_TICKETS_DETAILS_GAME_NBR = "insert into st_se_pwt_tickets_inv_?(ticket_nbr,game_id,book_nbr,status,verify_by_user,verify_by_org) values(?,?,?,?,?,?)";
	public static final String INSERT_RECEIPT_MASTER = "insert into st_lms_receipts_master(user_type) values(?)";

	public static final String INSERT_AGENT_DAILY_TRNG_EXP = "insert into st_lms_agent_daily_training_exp(date,agent_org_id, service_id,game_id,mrp_sale,training_exp_per,training_exp_amt,time_slotted_mrp_sale,extra_training_exp_per,extra_training_exp_amt,status) values (?,?,?,?,?,?,?,?,?,?,?)";

	public static final String INSERT_AGENT_WEEKLY_TRNG_EXP = "insert into st_lms_agent_weekly_training_exp (date,agent_org_id, service_id,game_id,mrp_sale,training_exp_per,training_exp_amt,time_slotted_mrp_sale,extra_training_exp_per,extra_training_exp_amt,status) values (?,?,?,?,?,?,?,?,?,?,?)";

	public static final String INSERT_RET_RECEIPT = "insert into st_ret_receipts(receipt_id,receipt_type,ret_org_id,party_id,party_type,generated_id) values(?,?,?,?,?,?)";
	public static final String INSERT_RET_RECEIPTS_TRN_MAPPING = "INSERT INTO st_ret_receipts_trn_mapping (receipt_id,transaction_id) VALUES (?,?)";
	public static final String INSERT_RETAILER_TRANSACTION_MASTER = "INSERT INTO st_lms_retailer_transaction_master (transaction_id,retailer_user_id,retailer_org_id,transaction_date,transaction_type) VALUES (?,?,?,?,?)";
	static Log logger = LogFactory.getLog(MySqlQueries.class);

	// queries to ticket and PWTs
	public static final String PWT_TICKETS_DETAILS_GAME_NBR = "select * from st_se_pwt_tickets_inv_? where ticket_nbr = ?";
	
	public static final String PWT_INV_TICKETS_DETAILS_GAME_NBR = "select * from st_se_pwt_inv_? where id1 = ? and ticket_status in ('ACTIVE', 'SOLD')";

	// public static final String RET_DAILY_LEDGER = "select sale.netSale,
	// saleRet.netSaleRefund, pwt.netPwt, cash.cashAmt, chq.chqAmt,
	// chqboun.chqBounce, dbnote.dbnAmt from (select
	// ifnull(sum(slact.amount),0)as cashAmt from st_lms_agent_cash_transaction
	// slact, st_lms_agent_transaction_master atm where slact.retailer_org_id=?
	// and date(atm.transaction_date)>=? and date(atm.transaction_date)<? and
	// slact.transaction_id = atm.transaction_id)as cash, (select
	// ifnull(sum(cheque_amt),0)as chqAmt from st_lms_agent_sale_chq slasc,
	// st_lms_agent_transaction_master atm where slasc.retailer_org_id = ? and
	// (slasc.transaction_type='CHEQUE' or slasc.transaction_type='CLOSED') and
	// date(atm.transaction_date)>=? and date(atm.transaction_date)<? and
	// slasc.transaction_id = atm.transaction_id)as chq, (select
	// ifnull(sum(cheque_amt),0)as chqBounce from st_lms_agent_sale_chq slasc,
	// st_lms_agent_transaction_master atm where slasc.retailer_org_id = ? and
	// slasc.transaction_type='CHQ_BOUNCE' and date(atm.transaction_date)>=? and
	// date(atm.transaction_date)<? and slasc.transaction_id =
	// atm.transaction_id)as chqboun, (select ifnull(sum(amount),0)as dbnAmt
	// from st_lms_agent_debit_note sladn, st_lms_agent_transaction_master atm
	// where sladn.retailer_org_id = ? and date(atm.transaction_date)>=? and
	// date(atm.transaction_date)<? and sladn.transaction_id =
	// atm.transaction_id) as dbnote, (select ifnull(sum(tot.net_amt),0) as
	// netSale from(select transaction_id, net_amt from st_dg_ret_sale_2 where
	// retailer_org_id=? union select transaction_id, net_amt from
	// st_dg_ret_sale_3 where retailer_org_id=?)as tot,
	// st_lms_retailer_transaction_master as rtm where tot.transaction_id =
	// rtm.transaction_id and date(rtm.transaction_date)>=? and
	// date(rtm.transaction_date)<?)as sale, (select ifnull(sum(tot.net_amt),0)
	// as netSaleRefund from(select transaction_id, net_amt from
	// st_dg_ret_sale_refund_2 where retailer_org_id=? union select
	// transaction_id, net_amt from st_dg_ret_sale_refund_3 where
	// retailer_org_id=?)as tot, st_lms_retailer_transaction_master as rtm where
	// tot.transaction_id = rtm.transaction_id and date(rtm.transaction_date)>=?
	// and date(rtm.transaction_date)<?)as saleRet, (select
	// ifnull(sum(tot.net_amt),0) as netPwt from(select
	// transaction_id,(pwt_amt+retailer_claim_comm)as net_amt from
	// st_dg_ret_pwt_2 where retailer_org_id=? union select transaction_id,
	// (pwt_amt+retailer_claim_comm)as net_amt from st_dg_ret_pwt_3 where
	// retailer_org_id=?)as tot, st_lms_retailer_transaction_master as rtm where
	// tot.transaction_id = rtm.transaction_id and date(rtm.transaction_date)>=?
	// and date(rtm.transaction_date)<?) as pwt";
	public static final String RET_DAILY_LEDGER = "select sale.netSale, sale.mrpSale, saleRet.netSaleRefund, saleRet.mrpSaleRef, pwt.netPwt, pwt.mrpPwt, cash.cashAmt, chq.chqAmt, chqboun.chqBounce, dbnote.dbnAmt from (select ifnull(sum(slact.amount),0)as cashAmt from st_lms_agent_cash_transaction slact, st_lms_agent_transaction_master atm where slact.retailer_org_id = ? and date(atm.transaction_date)>=? and date(atm.transaction_date)<? and slact.transaction_id = atm.transaction_id)as cash, (select ifnull(sum(cheque_amt),0)as chqAmt from st_lms_agent_sale_chq slasc, st_lms_agent_transaction_master atm where slasc.retailer_org_id = ? and (slasc.transaction_type='CHEQUE' or slasc.transaction_type='CLOSED') and date(atm.transaction_date)>=? and date(atm.transaction_date)<? and slasc.transaction_id = atm.transaction_id)as chq, (select ifnull(sum(cheque_amt),0)as chqBounce from st_lms_agent_sale_chq slasc, st_lms_agent_transaction_master atm where slasc.retailer_org_id = ? and slasc.transaction_type='CHQ_BOUNCE' and date(atm.transaction_date)>=? and date(atm.transaction_date)<? and slasc.transaction_id = atm.transaction_id)as chqboun, (select ifnull(sum(amount),0)as dbnAmt from st_lms_agent_debit_note sladn, st_lms_agent_transaction_master atm where sladn.retailer_org_id = ? and date(atm.transaction_date)>=? and date(atm.transaction_date)<? and sladn.transaction_id = atm.transaction_id) as dbnote, (select ifnull(sum(tot.mrp_amt),0) as mrpSale, ifnull(sum(tot.net_amt),0) as netSale from(select transaction_id, mrp_amt, net_amt from st_dg_ret_sale_2 where retailer_org_id = ? union select transaction_id, mrp_amt, net_amt from st_dg_ret_sale_3 where retailer_org_id = ?)as tot, st_lms_retailer_transaction_master as rtm where tot.transaction_id = rtm.transaction_id and date(rtm.transaction_date)>=? and date(rtm.transaction_date)<?)as sale, (select ifnull(sum(tot.mrpRef),0)as mrpSaleRef, ifnull(sum(tot.net_amt),0) as netSaleRefund from(select transaction_id, mrp_amt as mrpRef, net_amt from st_dg_ret_sale_refund_2 where retailer_org_id = ? union select transaction_id, mrp_amt as mrpRef, net_amt from st_dg_ret_sale_refund_3 where retailer_org_id = ?)as tot, st_lms_retailer_transaction_master as rtm where tot.transaction_id = rtm.transaction_id and date(rtm.transaction_date)>=? and date(rtm.transaction_date)<?)as saleRet, (select ifnull(sum(tot.mrpPwt),0)as mrpPwt, ifnull(sum(tot.net_amt),0) as netPwt from(select transaction_id, pwt_amt as mrpPwt,(pwt_amt+retailer_claim_comm)as  net_amt from st_dg_ret_pwt_2 where retailer_org_id = ? union select transaction_id, pwt_amt as mrpPwt, (pwt_amt+retailer_claim_comm)as net_amt from st_dg_ret_pwt_3 where retailer_org_id = ?)as tot, st_lms_retailer_transaction_master as rtm where tot.transaction_id = rtm.transaction_id and date(rtm.transaction_date)>=? and date(rtm.transaction_date)<?) as pwt";

	public static final String ST_AGENT_INVOICE_DETAILS = "select game_id, pack_nbr, transaction_at, total_books, transaction_date, party_id as retailer_org_id, current_owner_id,(select game_name from st_se_game_master where game_id=aa.game_id) 'game_name', (select nbr_of_books_per_pack from st_se_game_master where game_id=aa.game_id) 'books_per_pack' from ( select gid.game_id, gid.pack_nbr, gid.book_nbr 'total_books', transaction_at, btm.transaction_date, gid.current_owner_id, btm.party_id  from st_se_game_inv_detail gid, st_lms_agent_transaction_master btm where gid.transaction_id=btm.transaction_id and transaction_at=(select organization_type from st_lms_organization_master where organization_id=?) and gid.current_owner=? and gid.transaction_id in(select rtm.transaction_id 'tid' from st_lms_agent_receipts_trn_mapping rtm where rtm.receipt_id=?) )aa order by game_id, pack_nbr, total_books ";
	public static final String ST_AGENT_SALE_REPORT_GAME_WISE = "select ifnull(e.ee,0) 'remaining_books',ifnull(a.aa,0) 'books_purchase_from_bo', ifnull(b.bb,0) 'books_returned_to_bo' ,ifnull(c.cc,0) 'books_sale_to_retailer', ifnull(d.dd,0) 'books_returned_by_retailer',ifnull(f.aa1,0) 'tickets_purchase_from_bo', ifnull(g.bb1,0) 'tickets_returned_to_bo' ,ifnull(h.cc1,0) 'tickets_sale_to_retailer', ifnull(i.dd1,0) 'tickets_returned_by_retailer' from ( (select sum(nbr_of_books) aa from st_se_bo_agent_transaction bo, st_lms_bo_transaction_master btm where btm.transaction_id=bo.transaction_id and bo.transaction_type ='SALE' and bo.agent_org_id=? and ( btm.transaction_date>=? and btm.transaction_date<?) and game_id =? ) a , ( select sum(nbr_of_books) bb from st_se_bo_agent_transaction bo, st_lms_bo_transaction_master btm where btm.transaction_id=bo.transaction_id and bo.transaction_type ='SALE_RET' and bo.agent_org_id=? and ( btm.transaction_date>=? and btm.transaction_date<?) and game_id =?  )b , (select sum(nbr_of_books) cc from st_se_agent_retailer_transaction bo, st_lms_agent_transaction_master btm where btm.transaction_id=bo.transaction_id and bo.transaction_type ='SALE' and btm.user_org_id=?  and ( btm.transaction_date>=? and btm.transaction_date<?) and game_id =?  ) c, (select sum(nbr_of_books) dd from st_se_agent_retailer_transaction bo, st_lms_agent_transaction_master btm where btm.transaction_id=bo.transaction_id and bo.transaction_type ='SALE_RET' and btm.user_org_id=?  and ( btm.transaction_date>=? and btm.transaction_date<?) and game_id =?  )d,(select sum(nbrOfTickets) aa1 from st_se_bo_agent_loose_book_transaction bo, st_lms_bo_transaction_master btm where btm.transaction_id=bo.transaction_id and bo.transaction_type ='LOOSE_SALE' and bo.agent_org_id=? and ( btm.transaction_date>=? and btm.transaction_date<?) and game_id =? ) f , ( select sum(nbrOfTickets) bb1 from st_se_bo_agent_loose_book_transaction bo, st_lms_bo_transaction_master btm where btm.transaction_id=bo.transaction_id and bo.transaction_type ='LOOSE_SALE_RET' and bo.agent_org_id=? and ( btm.transaction_date>=? and btm.transaction_date<?) and game_id =?  )g ,(select sum(nbr_of_tickets) cc1 from st_se_agent_ret_loose_book_transaction bo, st_lms_agent_transaction_master btm where btm.transaction_id=bo.transaction_id and bo.transaction_type ='LOOSE_SALE' and btm.user_org_id=?  and ( btm.transaction_date>=? and btm.transaction_date<?) and game_id =?  ) h, (select sum(nbr_of_tickets) dd1 from st_se_agent_ret_loose_book_transaction bo, st_lms_agent_transaction_master btm where btm.transaction_id=bo.transaction_id and bo.transaction_type ='LOOSE_SALE_RET' and btm.user_org_id=?  and ( btm.transaction_date>=? and btm.transaction_date<?) and game_id =?  )i,(select count(book_nbr) ee from st_se_game_inv_status where current_owner='AGENT' and current_owner_id=? and game_id=?) e)";
	// sale reports queries on agent side changed after table altered
	public static final String ST_AGENT_SALE_REPORT_GET_GAME_ID = "select distinct bo.game_id from st_se_agent_retailer_transaction bo,st_lms_agent_transaction_master btm where btm.transaction_id=bo.transaction_id and btm.user_org_id=? and ( btm.transaction_date>=? and btm.transaction_date<?) union select distinct bo.game_id from st_se_bo_agent_transaction bo,st_lms_bo_transaction_master btm where btm.transaction_id=bo.transaction_id and bo.agent_org_id=? and ( btm.transaction_date>=? and btm.transaction_date<?) union select distinct bo.game_id from st_se_bo_agent_loose_book_transaction bo,st_lms_bo_transaction_master btm where btm.transaction_id=bo.transaction_id and bo.agent_org_id=? and ( btm.transaction_date>=? and btm.transaction_date<?)union select distinct bo.game_id from st_se_agent_ret_loose_book_transaction bo,st_lms_agent_transaction_master btm where btm.transaction_id=bo.transaction_id and btm.user_org_id=? and ( btm.transaction_date>=? and btm.transaction_date<?)";

	public static final String ST_AGENT_SALE_RETURN_DETAILS = "select transaction_id, transacrion_sale_comm_rate, game_id, pack_nbr, transaction_at, total_books, transaction_date, retailer_org_id, current_owner_id,(select game_name from st_se_game_master where game_id=aa.game_id) 'game_name', (select nbr_of_books_per_pack from st_se_game_master where game_id=aa.game_id) 'books_per_pack' from ( select gid.game_id, gid.pack_nbr, gid.book_nbr 'total_books', gid.transaction_id,  (bat.comm_amt*100/bat.mrp_amt) 'transacrion_sale_comm_rate', transaction_at, btm.transaction_date, gid.current_owner_id, btm.party_id 'retailer_org_id'  from st_se_game_inv_detail gid, st_lms_agent_transaction_master btm, st_se_agent_retailer_transaction bat where gid.transaction_id=btm.transaction_id and bat.transaction_id=gid.transaction_id and transaction_at=(select organization_type from st_lms_organization_master where organization_id=?) and gid.current_owner=? and gid.transaction_id=? )aa order by game_id, pack_nbr, total_books";

	public static final String ST_BO_AGENT_PWT_DETAILS = "select agent_org_id,(select game_name from st_se_game_master where game_id=aa.game_id ) 'game_name', pwt_amt, no_of_tkt, (pwt_amt*no_of_tkt) 'amount' from (select pwt_amt , count(*) 'no_of_tkt', game_id, agent_org_id from st_se_bo_pwt bpwt, st_lms_bo_transaction_master btm  where btm.transaction_id=bpwt.transaction_id and agent_org_id =? and ( btm.transaction_date>=? and btm.transaction_date<?) group by pwt_amt, game_id )aa order by game_name asc, pwt_amt asc ";
	public static final String ST_BO_GAME_WISE_PWT_AGENT_DETAILS = "select ifnull(sum(pwt_amt),0) 'agent_pwt',bpwt.game_id from st_se_bo_pwt bpwt, st_se_game_master gm,  st_lms_bo_transaction_master btm  where btm.transaction_id=bpwt.transaction_id and  ( btm.transaction_date>=? and btm.transaction_date<?) and gm.game_id=bpwt.game_id and gm.game_status like ? group by game_id order by game_id ";

	public static final String ST_BO_GAME_WISE_PWT_GAME_DETAILS = "select game_id, game_nbr, game_name from st_se_game_master where game_status like ?";
	public static final String ST_BO_GAME_WISE_PWT_PLAYER_DETAILS = "select ifnull(sum(pwt_amt),0) 'player_pwt', gm.game_id from st_se_direct_player_pwt dpp, st_se_game_master gm where (  transaction_date>=? and transaction_date<?) and gm.game_id=dpp.game_id and gm.game_status like ? group by dpp.game_id order by dpp.game_id";

	// public static final String ST_BO_GAME_WISE_PWT_RET_DETAILS="select
	// sum(pwt_amt) 'ret_pwt', bpwt.game_id from st_se_agent_pwt bpwt,
	// st_se_game_master gm, st_lms_agent_transaction_master btm where
	// btm.transaction_id=bpwt.transaction_id and ( btm.transaction_date>=? and
	// btm.transaction_date<?) and gm.game_id=bpwt.game_id and gm.game_status
	// like ? group by bpwt.game_id order by gm.game_id";
	public static final String ST_BO_GAME_WISE_PWT_RET_DETAILS = "select ifnull(sum(pwt_amt), 0) 'ret_pwt', bpwt.game_id from st_se_agent_pwt bpwt, st_se_game_master gm, st_lms_agent_transaction_master btm  where btm.transaction_id=bpwt.transaction_id  and ( btm.transaction_date>=? and btm.transaction_date<?) and gm.game_id=bpwt.game_id and gm.game_status like ? group by bpwt.game_id order by gm.game_id";
	public static final String ST_BO_GAME_WISE_TOTAL_PWT_DETAILS = "select ifnull(sum(pwt_amt),0) 'TOTAL_PWT', spi.game_id from st_se_pwt_inv_? spi where spi.game_id = ? group by game_id ";
	public static final String ST_BO_INVOICE_CUSTOMER_DETAILS = "select st_lms_organization_master.name, st_lms_organization_master.vat_registration_nbr 'vat_ref_no' ,addr_line1,  addr_line2,city,st_lms_state_master.name 'state',st_lms_country_master.name 'country' from st_lms_organization_master,st_lms_state_master,st_lms_country_master where st_lms_organization_master.organization_id=? and st_lms_organization_master.country_code=st_lms_country_master.country_code and  st_lms_organization_master.state_code=st_lms_state_master.state_code";
	// delivery challan queries
	public static final String ST_BO_INVOICE_DETAILS = "select game_id, pack_nbr, total_books, transaction_date, transaction_at, party_id, current_owner_id,(select game_name from st_se_game_master where game_id=aa.game_id) 'game_name', (select nbr_of_books_per_pack from st_se_game_master where game_id=aa.game_id) 'books_per_pack' from (  select gid.game_id, gid.pack_nbr, gid.book_nbr 'total_books',btm.transaction_date, transaction_at, gid.current_owner_id, btm.party_id  from st_se_game_inv_detail gid, st_lms_bo_transaction_master btm where gid.transaction_id=btm.transaction_id  and transaction_at=(select organization_type from st_lms_organization_master where organization_id=?) and gid.current_owner=? and gid.transaction_id in(select rtm.transaction_id 'tid' from st_lms_bo_receipts_trn_mapping rtm where rtm.receipt_id=?)  )aa order by game_id, pack_nbr, total_books  ";
	// pwt details queries
	public static final String ST_BO_PLAYER_PWT_DETAILS = "select (select concat(first_name, concat(' ',last_name)) from st_lms_player_master where player_id=aa.player_id) 'player_name', (select game_name from st_se_game_master where game_id=aa.game_id ) 'game_name', pwt_amt, no_of_tkt, (pwt_amt*no_of_tkt) 'amount' from (select pwt_amt , player_id, count(*) 'no_of_tkt', game_id from st_se_direct_player_pwt where ( transaction_date>=? and transaction_date<?) group by pwt_amt, game_id, player_id )aa order by game_name asc, player_name asc, pwt_amt asc";

	public static final String ST_BO_SALE_RETURN_DETAILS = "select transaction_id, transacrion_sale_comm_rate, game_id, pack_nbr, total_books, transaction_date, transaction_at, party_id, current_owner_id,(select game_name from st_se_game_master where game_id=aa.game_id) 'game_name', (select nbr_of_books_per_pack from st_se_game_master where game_id=aa.game_id) 'books_per_pack' from ( select gid.game_id, gid.pack_nbr, gid.transaction_id, (bat.comm_amt*100/bat.mrp_amt) 'transacrion_sale_comm_rate', gid.book_nbr 'total_books', btm.transaction_date, transaction_at, gid.current_owner_id, btm.party_id from st_se_game_inv_detail gid, st_lms_bo_transaction_master btm, st_se_bo_agent_transaction bat where gid.transaction_id=btm.transaction_id and bat.transaction_id=gid.transaction_id and transaction_at=(select organization_type from st_lms_organization_master where organization_id=?) and gid.current_owner=? and gid.transaction_id =? )aa order by game_id, transacrion_sale_comm_rate, pack_nbr, total_books";
	// public static final String ST_CASH_CHEQ_REPORT_BO1="select distinct
	// cash.agent_org_id from st_lms_bo_cash_transaction cash,
	// st_lms_bo_transaction_master btm where
	// btm.transaction_id=cash.transaction_id and cash.agent_org_id=
	// btm.party_id and ( btm.transaction_date>=? and btm.transaction_date<?)
	// union select distinct chq.agent_org_id from st_lms_bo_sale_chq chq,
	// st_lms_bo_transaction_master btm where
	// btm.transaction_id=chq.transaction_id and chq.agent_org_id= btm.party_id
	// and ( btm.transaction_date>=? and btm.transaction_date<?) union distinct
	// select dbt.agent_org_id from st_lms_bo_debit_note dbt,
	// st_lms_bo_transaction_master btm where
	// btm.transaction_id=dbt.transaction_id and dbt.agent_org_id= btm.party_id
	// and ( btm.transaction_date>=? and btm.transaction_date<?)";
	public static final String ST_CASH_CHEQ_REPORT_BO1 = "select distinct cash.agent_org_id from st_lms_bo_cash_transaction cash, st_lms_bo_transaction_master btm where btm.transaction_id=cash.transaction_id and cash.agent_org_id= btm.party_id and ( btm.transaction_date>=? and btm.transaction_date<?) union select distinct chq.agent_org_id from st_lms_bo_sale_chq chq, st_lms_bo_transaction_master btm where btm.transaction_id=chq.transaction_id and chq.agent_org_id= btm.party_id and ( btm.transaction_date>=? and btm.transaction_date<?) union distinct select dbt.agent_org_id from st_lms_bo_debit_note dbt, st_lms_bo_transaction_master btm where btm.transaction_id=dbt.transaction_id and dbt.agent_org_id= btm.party_id and ( btm.transaction_date>=? and btm.transaction_date<?)union distinct select dbt.agent_org_id from st_lms_bo_credit_note dbt, st_lms_bo_transaction_master btm where btm.transaction_id=dbt.transaction_id and dbt.agent_org_id= btm.party_id and ( btm.transaction_date>=? and btm.transaction_date<?)";
	// public static final String ST_CASH_CHEQ_REPORT_BO2="select aa.a
	// 'total_cash', bb.b 'cheque_coll', cc.c 'bounce' , gg.debit_amt 'debit',
	// ((aa.a+bb.b)-(cc.c+gg.debit_amt))'net_amount' from (( select
	// ifnull(sum(cash.amount),0) 'a' from st_lms_bo_cash_transaction cash,
	// st_lms_bo_transaction_master btm where cash.agent_org_id=? and (
	// btm.transaction_date>=? and btm.transaction_date<?) and
	// cash.transaction_id=btm.transaction_id ) aa, ( select
	// ifnull(sum(chq.cheque_amt),0) 'b' from st_lms_bo_sale_chq chq,
	// st_lms_bo_transaction_master btm where chq.transaction_type IN
	// ('CHEQUE','CLOSED') and chq.agent_org_id=? and ( btm.transaction_date>=?
	// and btm.transaction_date<?) and chq.transaction_id=btm.transaction_id )
	// bb, ( select ifnull(sum(chq.cheque_amt),0) 'c' from st_lms_bo_sale_chq
	// chq, st_lms_bo_transaction_master btm where
	// chq.transaction_type='CHQ_BOUNCE' and chq.agent_org_id=? and (
	// btm.transaction_date>=? and btm.transaction_date<?) and
	// chq.transaction_id=btm.transaction_id ) cc,(select
	// ifnull(sum(bo.amount),0) 'debit_amt' from st_lms_bo_debit_note bo,
	// st_lms_bo_transaction_master btm where
	// btm.transaction_id=bo.transaction_id and bo.transaction_type
	// ='DR_NOTE_CASH' and agent_org_id =? and ( btm.transaction_date>=? and
	// btm.transaction_date<?) )gg )";
	// public static final String ST_CASH_CHEQ_REPORT_BO2="select aa.a
	// 'total_cash', bb.b 'cheque_coll', cc.c 'bounce' , gg.debit_amt 'debit',
	// ((aa.a+bb.b)-(cc.c+gg.debit_amt))'net_amount' from (( select
	// ifnull(sum(cash.amount),0) 'a' from st_lms_bo_cash_transaction cash,
	// st_lms_bo_transaction_master btm where cash.agent_org_id=? and (
	// btm.transaction_date>=? and btm.transaction_date<?) and
	// cash.transaction_id=btm.transaction_id ) aa, ( select
	// ifnull(sum(chq.cheque_amt),0) 'b' from st_lms_bo_sale_chq chq,
	// st_lms_bo_transaction_master btm where chq.transaction_type IN
	// ('CHEQUE','CLOSED') and chq.agent_org_id=? and ( btm.transaction_date>=?
	// and btm.transaction_date<?) and chq.transaction_id=btm.transaction_id )
	// bb, ( select ifnull(sum(chq.cheque_amt),0) 'c' from st_lms_bo_sale_chq
	// chq, st_lms_bo_transaction_master btm where
	// chq.transaction_type='CHQ_BOUNCE' and chq.agent_org_id=? and (
	// btm.transaction_date>=? and btm.transaction_date<?) and
	// chq.transaction_id=btm.transaction_id ) cc,(select
	// ifnull(sum(bo.amount),0) 'debit_amt' from st_lms_bo_debit_note bo,
	// st_lms_bo_transaction_master btm where
	// btm.transaction_id=bo.transaction_id and (bo.transaction_type
	// ='DR_NOTE_CASH' or bo.transaction_type ='DR_NOTE') and agent_org_id =?
	// and ( btm.transaction_date>=? and btm.transaction_date<?) )gg )";
	public static final String ST_CASH_CHEQ_REPORT_BO3 = "select aa.a 'total_cash', bb.b 'cheque_coll', cc.c 'bounce' , gg.debit_amt 'debit', dd.credit_amt 'credit',bd.d 'bank_deposit', ((aa.a+bb.b+dd.credit_amt+bd.d)-(cc.c+gg.debit_amt))'net_amount' from (( select ifnull(sum(cash.amount),0) 'a' from st_lms_bo_cash_transaction cash, st_lms_bo_transaction_master btm where cash.agent_org_id=? and btm.user_id=? and ( btm.transaction_date>=? and btm.transaction_date<?) and cash.transaction_id=btm.transaction_id ) aa, ( select ifnull(sum(chq.cheque_amt),0) 'b' from  st_lms_bo_sale_chq chq, st_lms_bo_transaction_master btm where chq.transaction_type IN ('CHEQUE','CLOSED') and chq.agent_org_id=? and btm.user_id=? and ( btm.transaction_date>=? and btm.transaction_date<?) and chq.transaction_id=btm.transaction_id ) bb, ( select ifnull(sum(chq.cheque_amt),0) 'c' from  st_lms_bo_sale_chq chq, st_lms_bo_transaction_master btm where chq.transaction_type='CHQ_BOUNCE' and chq.agent_org_id=? and btm.user_id=? and ( btm.transaction_date>=? and btm.transaction_date<?) and chq.transaction_id=btm.transaction_id ) cc,(select ifnull(sum(bo.amount),0) 'debit_amt'  from st_lms_bo_debit_note bo, st_lms_bo_transaction_master btm where btm.transaction_id=bo.transaction_id and (bo.transaction_type ='DR_NOTE_CASH' or bo.transaction_type ='DR_NOTE') and agent_org_id =? and btm.user_id=? and ( btm.transaction_date>=? and btm.transaction_date<?) )gg,(select ifnull(sum(bo.amount),0) 'credit_amt'  from st_lms_bo_credit_note bo, st_lms_bo_transaction_master btm where btm.transaction_id=bo.transaction_id and (bo.transaction_type ='CR_NOTE_CASH') and agent_org_id =? and btm.user_id=? and ( btm.transaction_date>=? and btm.transaction_date<?) )dd,(select ifnull(sum(bank.amount),0) 'd' from st_lms_bo_bank_deposit_transaction bank, st_lms_bo_transaction_master btm where bank.agent_org_id =? and btm.user_id=? and ( btm.transaction_date>=? and btm.transaction_date<?) and bank.transaction_id=btm.transaction_id ) bd)";

	public static final String ST_CASH_CHEQ_REPORT_BO2 = "select aa.a 'total_cash', bb.b 'cheque_coll', cc.c 'bounce' , gg.debit_amt 'debit', dd.credit_amt 'credit', ((aa.a+bb.b+dd.credit_amt)-(cc.c+gg.debit_amt))'net_amount' from (( select ifnull(sum(cash.amount),0) 'a' from st_lms_bo_cash_transaction cash, st_lms_bo_transaction_master btm where cash.agent_org_id=? and ( btm.transaction_date>=? and btm.transaction_date<?) and cash.transaction_id=btm.transaction_id ) aa, ( select ifnull(sum(chq.cheque_amt),0) 'b' from  st_lms_bo_sale_chq chq, st_lms_bo_transaction_master btm where chq.transaction_type IN ('CHEQUE','CLOSED') and chq.agent_org_id=? and ( btm.transaction_date>=? and btm.transaction_date<?) and chq.transaction_id=btm.transaction_id ) bb, ( select ifnull(sum(chq.cheque_amt),0) 'c' from  st_lms_bo_sale_chq chq, st_lms_bo_transaction_master btm where chq.transaction_type='CHQ_BOUNCE' and chq.agent_org_id=? and ( btm.transaction_date>=? and btm.transaction_date<?) and chq.transaction_id=btm.transaction_id ) cc,(select ifnull(sum(bo.amount),0) 'debit_amt'  from st_lms_bo_debit_note bo, st_lms_bo_transaction_master btm where btm.transaction_id=bo.transaction_id and (bo.transaction_type ='DR_NOTE_CASH' or bo.transaction_type ='DR_NOTE') and agent_org_id =? and ( btm.transaction_date>=? and btm.transaction_date<?) )gg,(select ifnull(sum(bo.amount),0) 'credit_amt'  from st_lms_bo_credit_note bo, st_lms_bo_transaction_master btm where btm.transaction_id=bo.transaction_id and (bo.transaction_type ='CR_NOTE_CASH') and agent_org_id =? and ( btm.transaction_date>=? and btm.transaction_date<?) )dd)";
	// public static final String ST_CASH_CHEQ_REPORT_DETAIL="select aa.cash_amt
	// 'total_cash', bb.chq 'chq_coll', gg.debit_amt, cc.chq_bounce
	// 'chq_bounce', ((aa.cash_amt+bb.chq)-(cc.chq_bounce+gg.debit_amt))
	// 'net_amount' from ((select ifnull(sum(cash.amount),0) 'cash_amt' from
	// st_lms_agent_cash_transaction cash, st_lms_agent_transaction_master btm
	// where btm.user_org_id=? and (btm.transaction_date>=? and
	// btm.transaction_date<?) and cash.retailer_org_id=? and
	// btm.transaction_type='CASH' and cash.transaction_id=btm.transaction_id )
	// aa, (select ifnull(sum(chq.cheque_amt),0) 'chq' from
	// st_lms_agent_sale_chq chq, st_lms_agent_transaction_master btm where
	// chq.transaction_type IN ('CHEQUE','CLOSED') and btm.user_org_id=? and
	// (btm.transaction_date>=? and btm.transaction_date<?) and
	// chq.retailer_org_id=? and chq.transaction_id=btm.transaction_id) bb,
	// (select ifnull(sum(chq.cheque_amt),0) 'chq_bounce' from
	// st_lms_agent_sale_chq chq, st_lms_agent_transaction_master btm where
	// chq.transaction_type='CHQ_BOUNCE' and btm.user_org_id=? and
	// (btm.transaction_date>=? and btm.transaction_date<?) and
	// chq.retailer_org_id=? and chq.transaction_id=btm.transaction_id ) cc,
	// (select ifnull(sum(bo.amount),0) 'debit_amt' from st_lms_agent_debit_note
	// bo, st_lms_agent_transaction_master btm where
	// btm.transaction_id=bo.transaction_id and bo.transaction_type
	// ='DR_NOTE_CASH' and btm.user_org_id=? and bo.retailer_org_id =? and (
	// btm.transaction_date>=? and btm.transaction_date<?))gg) ";
	// public static final String ST_CASH_CHEQ_REPORT_DETAIL = "select
	// aa.cash_amt 'total_cash', bb.chq 'chq_coll', gg.debit_amt, cc.chq_bounce
	// 'chq_bounce', ((aa.cash_amt+bb.chq)-(cc.chq_bounce+gg.debit_amt))
	// 'net_amount' from ((select ifnull(sum(cash.amount),0) 'cash_amt' from
	// st_lms_agent_cash_transaction cash, st_lms_agent_transaction_master btm
	// where btm.user_org_id=? and (btm.transaction_date>=? and
	// btm.transaction_date<?) and cash.retailer_org_id=? and
	// btm.transaction_type='CASH' and cash.transaction_id=btm.transaction_id )
	// aa, (select ifnull(sum(chq.cheque_amt),0) 'chq' from
	// st_lms_agent_sale_chq chq, st_lms_agent_transaction_master btm where
	// chq.transaction_type IN ('CHEQUE','CLOSED') and btm.user_org_id=? and
	// (btm.transaction_date>=? and btm.transaction_date<?) and
	// chq.retailer_org_id=? and chq.transaction_id=btm.transaction_id) bb,
	// (select ifnull(sum(chq.cheque_amt),0) 'chq_bounce' from
	// st_lms_agent_sale_chq chq, st_lms_agent_transaction_master btm where
	// chq.transaction_type='CHQ_BOUNCE' and btm.user_org_id=? and
	// (btm.transaction_date>=? and btm.transaction_date<?) and
	// chq.retailer_org_id=? and chq.transaction_id=btm.transaction_id ) cc,
	// (select ifnull(sum(bo.amount),0) 'debit_amt' from st_lms_agent_debit_note
	// bo, st_lms_agent_transaction_master btm where
	// btm.transaction_id=bo.transaction_id and (bo.transaction_type
	// ='DR_NOTE_CASH' or bo.transaction_type ='DR_NOTE' ) and btm.user_org_id=?
	// and bo.retailer_org_id =? and ( btm.transaction_date>=? and
	// btm.transaction_date<?))gg) ";
	public static final String ST_CASH_CHEQ_REPORT_DETAIL = "select aa.cash_amt 'total_cash', bb.chq 'chq_coll', gg.debit_amt, kk.credit_amt,cc.chq_bounce 'chq_bounce', ((aa.cash_amt+bb.chq+kk.credit_amt)-(cc.chq_bounce+gg.debit_amt)) 'net_amount' from ((select ifnull(sum(cash.amount),0) 'cash_amt' from st_lms_agent_cash_transaction cash, st_lms_agent_transaction_master btm where btm.user_org_id=? and (btm.transaction_date>=? and btm.transaction_date<?) and cash.retailer_org_id=? and btm.transaction_type='CASH' and cash.transaction_id=btm.transaction_id ) aa, (select ifnull(sum(chq.cheque_amt),0) 'chq' from  st_lms_agent_sale_chq chq, st_lms_agent_transaction_master btm where chq.transaction_type IN ('CHEQUE','CLOSED') and btm.user_org_id=? and (btm.transaction_date>=? and btm.transaction_date<?) and chq.retailer_org_id=?  and chq.transaction_id=btm.transaction_id)  bb, (select ifnull(sum(chq.cheque_amt),0) 'chq_bounce' from  st_lms_agent_sale_chq chq, st_lms_agent_transaction_master btm where chq.transaction_type='CHQ_BOUNCE' and btm.user_org_id=? and (btm.transaction_date>=? and btm.transaction_date<?) and chq.retailer_org_id=?  and chq.transaction_id=btm.transaction_id ) cc, (select ifnull(sum(bo.amount),0) 'debit_amt'  from st_lms_agent_debit_note bo, st_lms_agent_transaction_master btm where btm.transaction_id=bo.transaction_id and (bo.transaction_type ='DR_NOTE_CASH' or bo.transaction_type ='DR_NOTE' ) and btm.user_org_id=? and bo.retailer_org_id =? and ( btm.transaction_date>=? and btm.transaction_date<?))gg, (select ifnull(sum(bo.amount),0) 'credit_amt'  from st_lms_agent_credit_note bo, st_lms_agent_transaction_master btm where btm.transaction_id=bo.transaction_id and (bo.transaction_type ='CR_NOTE_CASH' or bo.transaction_type ='CR_NOTE' ) and btm.user_org_id=? and bo.retailer_org_id =? and ( btm.transaction_date>=? and btm.transaction_date<?))kk)";
	// cash cheque report on agent side
	public static final String ST_CASH_CHEQ_REPORT_RETAILER_ID = "select distinct cash.retailer_org_id from st_lms_agent_cash_transaction cash, st_lms_agent_transaction_master btm where btm.transaction_id=cash.transaction_id and cash.retailer_org_id= btm.party_id and  btm.user_org_id=? and (btm.transaction_date>=? and btm.transaction_date<?) union select distinct chq.retailer_org_id from st_lms_agent_sale_chq chq, st_lms_agent_transaction_master btm where btm.transaction_id=chq.transaction_id and chq.retailer_org_id= btm.party_id and btm.user_org_id=?  and (btm.transaction_date>=? and btm.transaction_date<?) union select distinct dbt.retailer_org_id from st_lms_agent_debit_note dbt, st_lms_agent_transaction_master btm where btm.transaction_id=dbt.transaction_id and dbt.retailer_org_id= btm.party_id and btm.user_org_id=?  and ( btm.transaction_date>=? and btm.transaction_date<?) union select distinct dbt.retailer_org_id from st_lms_agent_credit_note dbt, st_lms_agent_transaction_master btm where btm.transaction_id=dbt.transaction_id and dbt.retailer_org_id= btm.party_id and btm.user_org_id=?  and ( btm.transaction_date>=? and btm.transaction_date<?)";

	// public static final String ST_COLLECTION_DETAILS_FOR_AGENT= "select
	// aaa.cash, aaa.cheque, bbb.pwt, ccc.sale, ccc.sale_ret, aaa.cheque_ret,
	// aaa.debit from (( select aa.cash_amt 'cash', bb.chq 'cheque',
	// gg.debit_amt 'debit', cc.chq_bounce 'cheque_ret' from ((select
	// ifnull(sum(cash.amount),0) 'cash_amt' from st_lms_agent_cash_transaction
	// cash, st_lms_agent_transaction_master btm where cash.agent_user_id=? and
	// (date(btm.transaction_date)>=? and date(btm.transaction_date)<?) and
	// cash.retailer_org_id=? and btm.transaction_type='CASH' and
	// cash.transaction_id=btm.transaction_id ) aa, (select
	// ifnull(sum(chq.cheque_amt),0) 'chq' from st_lms_agent_sale_chq chq,
	// st_lms_agent_transaction_master btm where chq.agent_user_id=? and
	// (date(btm.transaction_date)>=? and date(btm.transaction_date)<?) and
	// chq.retailer_org_id=? and chq.transaction_type IN ('CHEQUE','CLOSED') and
	// chq.transaction_id=btm.transaction_id) bb, (select
	// ifnull(sum(chq.cheque_amt),0) 'chq_bounce' from st_lms_agent_sale_chq
	// chq, st_lms_agent_transaction_master btm where chq.agent_user_id=? and
	// (date(btm.transaction_date)>=? and date(btm.transaction_date)<?) and
	// chq.retailer_org_id=? and chq.transaction_type='CHQ_BOUNCE' and
	// chq.transaction_id=btm.transaction_id ) cc, (select
	// ifnull(sum(bo.amount),0) 'debit_amt' from st_lms_agent_debit_note bo,
	// st_lms_agent_transaction_master btm where bo.agent_user_id=? and (
	// date(btm.transaction_date)>=? and date(btm.transaction_date)<?) and
	// bo.retailer_org_id =? and btm.transaction_id=bo.transaction_id and
	// bo.transaction_type ='DR_NOTE_CASH')gg ) ) aaa, ( select
	// ifnull(sum(apwt.pwt_amt),0) 'pwt' from st_se_agent_pwt apwt,
	// st_lms_agent_transaction_master atm where apwt.agent_user_id=? and (
	// date(atm.transaction_date)>=? and date(atm.transaction_date)<?)and
	// apwt.retailer_org_id=? and atm.transaction_id=apwt.transaction_id ) bbb,
	// ( select ifnull(a.cc,0) 'sale', ifnull(b.dd,0) 'sale_ret' from ((select
	// sum(net_amt) cc from st_se_agent_retailer_transaction bo,
	// st_lms_agent_transaction_master btm where bo.agent_user_id = ? and(
	// date(btm.transaction_date) >= ? and date(btm.transaction_date) < ?) and
	// bo.retailer_org_id = ? and btm.transaction_id=bo.transaction_id and
	// bo.transaction_type ='SALE' ) a , (select sum(net_amt) dd from
	// st_se_agent_retailer_transaction bo, st_lms_agent_transaction_master btm
	// where bo.agent_user_id = ? and( date(btm.transaction_date) >= ? and
	// date(btm.transaction_date) < ?) and bo.retailer_org_id = ? and
	// btm.transaction_id=bo.transaction_id and bo.transaction_type ='SALE_RET'
	// ) b) )ccc)";
	// public static final String ST_COLLECTION_DETAILS_FOR_AGENT = "select aa.a
	// 'cash', bb.b 'cheque', cc.c 'cheque_ret', gg.debit_amt 'debit' from ((
	// select ifnull(sum(cash.amount),0) 'a' from st_lms_agent_cash_transaction
	// cash, st_lms_agent_transaction_master btm where cash.agent_org_id=? and
	// cash.retailer_org_id = ? and ( date(btm.transaction_date)>=? and
	// date(btm.transaction_date)<?) and cash.transaction_id=btm.transaction_id
	// ) aa, ( select ifnull(sum(chq.cheque_amt),0) 'b' from
	// st_lms_agent_sale_chq chq, st_lms_agent_transaction_master btm where
	// chq.transaction_type IN ('CHEQUE','CLOSED') and chq.agent_org_id=? and
	// chq.retailer_org_id = ? and ( date(btm.transaction_date)>=? and
	// date(btm.transaction_date)<?) and chq.transaction_id=btm.transaction_id )
	// bb, ( select ifnull(sum(chq.cheque_amt),0) 'c' from st_lms_agent_sale_chq
	// chq, st_lms_agent_transaction_master btm where
	// chq.transaction_type='CHQ_BOUNCE' and chq.agent_org_id=? and
	// chq.retailer_org_id =? and ( date(btm.transaction_date)>=? and
	// date(btm.transaction_date)<?) and chq.transaction_id=btm.transaction_id )
	// cc, ( select ifnull(sum(bo.amount),0) 'debit_amt' from
	// st_lms_agent_debit_note bo, st_lms_agent_transaction_master btm where
	// btm.transaction_id=bo.transaction_id and (bo.transaction_type
	// ='DR_NOTE_CASH' or bo.transaction_type ='DR_NOTE') and agent_org_id =?
	// and retailer_org_id = ? and ( date(btm.transaction_date)>=? and
	// date(btm.transaction_date)<?) )gg)";
	public static final String ST_COLLECTION_DETAILS_FOR_AGENT = "select aa.a 'cash', bb.b 'cheque', cc.c 'cheque_ret', gg.debit_amt 'debit', kk.credit_amt 'credit'  from  (( select ifnull(sum(cash.amount),0) 'a' from st_lms_agent_cash_transaction cash, st_lms_agent_transaction_master btm where cash.agent_org_id=? and cash.retailer_org_id = ? and ( date(btm.transaction_date)>=? and date(btm.transaction_date)<?) and cash.transaction_id=btm.transaction_id ) aa, ( select ifnull(sum(chq.cheque_amt),0) 'b' from  st_lms_agent_sale_chq chq, st_lms_agent_transaction_master btm where chq.transaction_type IN ('CHEQUE','CLOSED') and chq.agent_org_id=? and chq.retailer_org_id = ? and ( date(btm.transaction_date)>=? and date(btm.transaction_date)<?) and chq.transaction_id=btm.transaction_id  ) bb, ( select ifnull(sum(chq.cheque_amt),0) 'c' from  st_lms_agent_sale_chq chq, st_lms_agent_transaction_master btm where chq.transaction_type='CHQ_BOUNCE' and chq.agent_org_id=? and chq.retailer_org_id =? and ( date(btm.transaction_date)>=? and date(btm.transaction_date)<?) and chq.transaction_id=btm.transaction_id  ) cc,  ( select ifnull(sum(bo.amount),0) 'debit_amt'  from st_lms_agent_debit_note bo, st_lms_agent_transaction_master btm where btm.transaction_id=bo.transaction_id and (bo.transaction_type ='DR_NOTE_CASH' or bo.transaction_type ='DR_NOTE') and agent_org_id =? and retailer_org_id = ? and ( date(btm.transaction_date)>=? and date(btm.transaction_date)<?) )gg, ( select ifnull(sum(bo.amount),0) 'credit_amt'  from st_lms_agent_credit_note bo, st_lms_agent_transaction_master btm where btm.transaction_id=bo.transaction_id and (bo.transaction_type ='CR_NOTE_CASH' or bo.transaction_type ='CR_NOTE') and agent_org_id =? and retailer_org_id = ? and ( date(btm.transaction_date)>=? and date(btm.transaction_date)<?) )kk)";
	public static final String ST_COLLECTION_DETAILS_FOR_AGENT_DG = "select ii.dg_sale_amt 'dg_sale', kk.dg_refund 'dg_sale_refund', jj.dg_pwt_amt 'dg_pwt' from (( select ifnull(sum(net_amt),0) 'dg_sale_amt'  from st_dg_agt_sale bo, st_lms_agent_transaction_master btm where btm.transaction_id=bo.transaction_id and btm.transaction_type ='DG_SALE' and agent_org_id =? and retailer_org_id = ? and ( date(btm.transaction_date)>=? and date(btm.transaction_date)<?))ii, (select ifnull(sum(net_amt),0) as 'dg_refund' from st_dg_agt_sale_refund bo, st_lms_agent_transaction_master btm where btm.transaction_id=bo.transaction_id and btm.transaction_type in('DG_REFUND_CANCEL','DG_REFUND_FAILED') and agent_org_id=? and retailer_org_id = ? and( date(btm.transaction_date)>=? and date(btm.transaction_date)<?))kk,(select ifnull(sum(net_amt),0) 'dg_pwt_amt'  from st_dg_agt_pwt bo, st_lms_agent_transaction_master btm where btm.transaction_id=bo.transaction_id and (btm.transaction_type ='DG_PWT' or btm.transaction_type ='DG_PWT_AUTO')and agent_org_id =? and retailer_org_id= ? and( date(btm.transaction_date)>=? and date(btm.transaction_date)<?))jj)";
	public static final String ST_COLLECTION_DETAILS_FOR_AGENT_SE = "select ff.pwt_amt 'se_pwt', dd.sale_amt 'se_sale', ee.sale_return_amt 'se_sale_ret' from ((select sum(sale_amt) sale_amt from (select ifnull(sum(net_amt),0) 'sale_amt'  from st_se_agent_retailer_transaction bo, st_lms_agent_transaction_master btm where btm.transaction_id=bo.transaction_id and bo.transaction_type ='SALE' and agent_org_id =? and retailer_org_id=? and ( date(btm.transaction_date)>=? and date(btm.transaction_date)<?) union all select ifnull(sum(net_amt),0) 'sale_amt'  from st_se_agent_ret_loose_book_transaction bo, st_lms_agent_transaction_master btm where btm.transaction_id=bo.transaction_id and bo.transaction_type ='LOOSE_SALE' and agent_org_id =? and retailer_org_id=? and (date(btm.transaction_date)>=? and date(btm.transaction_date)<?))dd)dd,(select sum(sale_return_amt) sale_return_amt from (select ifnull(sum(net_amt),0) 'sale_return_amt'  from st_se_agent_retailer_transaction bo, st_lms_agent_transaction_master btm where btm.transaction_id=bo.transaction_id and bo.transaction_type ='SALE_RET' and agent_org_id =? and retailer_org_id =?  and( date(btm.transaction_date)>=? and date(btm.transaction_date)<?) union all select ifnull(sum(net_amt),0) 'sale_return_amt'  from st_se_agent_ret_loose_book_transaction bo, st_lms_agent_transaction_master btm where btm.transaction_id=bo.transaction_id and bo.transaction_type ='LOOSE_SALE_RET' and agent_org_id =? and retailer_org_id=? and (date(btm.transaction_date)>=? and date(btm.transaction_date)<?))ee)ee,( select ifnull(sum(bpwt.pwt_amt),0) 'pwt_amt' from st_se_agent_pwt bpwt, st_lms_agent_transaction_master btm where bpwt.agent_org_id=? and retailer_org_id = ? and btm.transaction_id=bpwt.transaction_id and ( date(btm.transaction_date)>=? and date( btm.transaction_date)<?))ff)";
	public static final String ST_COLLECTION_DETAILS_FOR_AGENT_OLA = "select ff.netGaming 'ola_commission_calculated', dd.depositAmt 'ola_deposit', ee.withdrawalAmt 'ola_withdrawal' from (( select ifnull(sum(net_amt),0) 'depositAmt' from st_ola_agt_deposit bo, st_lms_agent_transaction_master btm where btm.transaction_id=bo.transaction_id  and agent_org_id =?  and retailer_org_id=? and ( date(btm.transaction_date)>=? and date(btm.transaction_date)<?))dd , (select ifnull(sum(net_amt),0) 'withdrawalAmt' from st_ola_agt_withdrawl bo,  st_lms_agent_transaction_master btm where btm.transaction_id=bo.transaction_id  and agent_org_id =? and retailer_org_id =? and ( date(btm.transaction_date)>=? and date(btm.transaction_date)<?))ee , (select ifnull(sum(bpwt.commission_calculated),0) 'netGaming' from st_ola_agt_ret_commisiion bpwt, st_lms_agent_transaction_master btm where bpwt.agt_org_id=? and ret_org_id = ? and btm.transaction_id=bpwt.transaction_id and ( date(btm.transaction_date)>=? and date(btm.transaction_date)<?))ff)";
	// public static final String ST_COLLECTION_DETAILS_FOR_BO="select aa.a
	// 'cash', bb.b 'cheque', ff.pwt_amt 'pwt', dd.sale_amt 'sale',
	// ee.sale_return_amt 'sale_ret', cc.c 'cheque_ret', gg.debit_amt 'debit'
	// from (( select ifnull(sum(cash.amount),0) 'a' from
	// st_lms_bo_cash_transaction cash, st_lms_bo_transaction_master btm where
	// cash.agent_org_id=? and ( btm.transaction_date>=? and
	// btm.transaction_date<?) and cash.transaction_id=btm.transaction_id ) aa,
	// ( select ifnull(sum(chq.cheque_amt),0) 'b' from st_lms_bo_sale_chq chq,
	// st_lms_bo_transaction_master btm where chq.transaction_type IN
	// ('CHEQUE','CLOSED') and chq.agent_org_id=? and ( btm.transaction_date>=?
	// and btm.transaction_date<?) and chq.transaction_id=btm.transaction_id )
	// bb, ( select ifnull(sum(chq.cheque_amt),0) 'c' from st_lms_bo_sale_chq
	// chq, st_lms_bo_transaction_master btm where
	// chq.transaction_type='CHQ_BOUNCE' and chq.agent_org_id=? and (
	// btm.transaction_date>=? and btm.transaction_date<?) and
	// chq.transaction_id=btm.transaction_id ) cc, ( select
	// ifnull(sum(net_amt),0) 'sale_amt' from st_se_bo_agent_transaction bo,
	// st_lms_bo_transaction_master btm where
	// btm.transaction_id=bo.transaction_id and bo.transaction_type ='SALE' and
	// agent_org_id =? and ( btm.transaction_date>=? and btm.transaction_date<?)
	// )dd, ( select ifnull(sum(net_amt),0) 'sale_return_amt' from
	// st_se_bo_agent_transaction bo, st_lms_bo_transaction_master btm where
	// btm.transaction_id=bo.transaction_id and bo.transaction_type ='SALE_RET'
	// and agent_org_id =? and ( btm.transaction_date>=? and
	// btm.transaction_date<?) )ee, ( select ifnull(sum(bpwt.pwt_amt),0)
	// 'pwt_amt' from st_se_bo_pwt bpwt, st_lms_bo_transaction_master btm where
	// bpwt.agent_org_id=? and btm.transaction_id=bpwt.transaction_id and (
	// btm.transaction_date>=? and btm.transaction_date<?) )ff,( select
	// ifnull(sum(bo.amount),0) 'debit_amt' from st_lms_bo_debit_note bo,
	// st_lms_bo_transaction_master btm where
	// btm.transaction_id=bo.transaction_id and bo.transaction_type
	// ='DR_NOTE_CASH' and agent_org_id =? and ( btm.transaction_date>=? and
	// btm.transaction_date<?) )gg )";
	// public static final String ST_COLLECTION_DETAILS_FOR_BO="select aa.a
	// 'cash', bb.b 'cheque', ff.pwt_amt 'pwt', dd.sale_amt 'sale',
	// ee.sale_return_amt 'sale_ret', cc.c 'cheque_ret', gg.debit_amt 'debit',
	// hh.credit_amt 'credit' from (( select ifnull(sum(cash.amount),0) 'a' from
	// st_lms_bo_cash_transaction cash, st_lms_bo_transaction_master btm where
	// cash.agent_org_id=? and ( btm.transaction_date>=? and
	// btm.transaction_date<?) and cash.transaction_id=btm.transaction_id ) aa,
	// ( select ifnull(sum(chq.cheque_amt),0) 'b' from st_lms_bo_sale_chq chq,
	// st_lms_bo_transaction_master btm where chq.transaction_type IN
	// ('CHEQUE','CLOSED') and chq.agent_org_id=? and ( btm.transaction_date>=?
	// and btm.transaction_date<?) and chq.transaction_id=btm.transaction_id )
	// bb, ( select ifnull(sum(chq.cheque_amt),0) 'c' from st_lms_bo_sale_chq
	// chq, st_lms_bo_transaction_master btm where
	// chq.transaction_type='CHQ_BOUNCE' and chq.agent_org_id=? and (
	// btm.transaction_date>=? and btm.transaction_date<?) and
	// chq.transaction_id=btm.transaction_id ) cc, ( select
	// ifnull(sum(net_amt),0) 'sale_amt' from st_se_bo_agent_transaction bo,
	// st_lms_bo_transaction_master btm where
	// btm.transaction_id=bo.transaction_id and bo.transaction_type ='SALE' and
	// agent_org_id =? and ( btm.transaction_date>=? and btm.transaction_date<?)
	// )dd, ( select ifnull(sum(net_amt),0) 'sale_return_amt' from
	// st_se_bo_agent_transaction bo, st_lms_bo_transaction_master btm where
	// btm.transaction_id=bo.transaction_id and bo.transaction_type ='SALE_RET'
	// and agent_org_id =? and ( btm.transaction_date>=? and
	// btm.transaction_date<?) )ee, ( select ifnull(sum(bpwt.pwt_amt),0)
	// 'pwt_amt' from st_se_bo_pwt bpwt, st_lms_bo_transaction_master btm where
	// bpwt.agent_org_id=? and btm.transaction_id=bpwt.transaction_id and (
	// btm.transaction_date>=? and btm.transaction_date<?) )ff,( select
	// ifnull(sum(bo.amount),0) 'debit_amt' from st_lms_bo_debit_note bo,
	// st_lms_bo_transaction_master btm where
	// btm.transaction_id=bo.transaction_id and (bo.transaction_type
	// ='DR_NOTE_CASH' or bo.transaction_type ='DR_NOTE') and agent_org_id =?
	// and ( btm.transaction_date>=? and btm.transaction_date<?) )gg,(select
	// ifnull(sum(bo.amount),0) 'credit_amt' from st_lms_bo_credit_note bo,
	// st_lms_bo_transaction_master btm where
	// btm.transaction_id=bo.transaction_id and (bo.transaction_type
	// ='CR_NOTE_CASH') and agent_org_id =? and ( btm.transaction_date>=? and
	// btm.transaction_date<?) )hh )";
	public static final String ST_COLLECTION_DETAILS_FOR_BO = "select aa.a 'cash', bb.b 'cheque', cc.c 'cheque_ret', gg.debit_amt 'debit', hh.credit_amt 'credit',  bd.bank 'bank_deposit'  from  (( select ifnull(sum(cash.amount),0) 'a' from st_lms_bo_cash_transaction cash, st_lms_bo_transaction_master btm where cash.agent_org_id=? and ( date(btm.transaction_date)>=? and date(btm.transaction_date)<?) and cash.transaction_id=btm.transaction_id ) aa, ( select ifnull(sum(chq.cheque_amt),0) 'b' from  st_lms_bo_sale_chq chq, st_lms_bo_transaction_master btm where chq.transaction_type IN ('CHEQUE','CLOSED') and chq.agent_org_id=? and ( date(btm.transaction_date)>=? and date(btm.transaction_date)<?) and chq.transaction_id=btm.transaction_id  ) bb, ( select ifnull(sum(chq.cheque_amt),0) 'c' from  st_lms_bo_sale_chq chq, st_lms_bo_transaction_master btm where chq.transaction_type='CHQ_BOUNCE' and chq.agent_org_id=? and ( date(btm.transaction_date)>=? and date(btm.transaction_date)<?) and chq.transaction_id=btm.transaction_id  ) cc,  ( select ifnull(sum(bo.amount),0) 'debit_amt'  from st_lms_bo_debit_note bo, st_lms_bo_transaction_master btm where btm.transaction_id=bo.transaction_id and (bo.transaction_type ='DR_NOTE_CASH' or bo.transaction_type ='DR_NOTE') and agent_org_id =? and ( date(btm.transaction_date)>=? and date(btm.transaction_date)<?) )gg,(select ifnull(sum(bo.amount),0) 'credit_amt'  from st_lms_bo_credit_note bo, st_lms_bo_transaction_master btm where btm.transaction_id=bo.transaction_id and (bo.transaction_type ='CR_NOTE_CASH') and agent_org_id =? and ( date(btm.transaction_date)>=? and date(btm.transaction_date)<?) )hh, (select ifnull(sum(cash.amount),0) 'bank' from st_lms_bo_bank_deposit_transaction cash, st_lms_bo_transaction_master btm where cash.agent_org_id=? and ( date(btm.transaction_date)>=? and date(btm.transaction_date)<?) and cash.transaction_id=btm.transaction_id )bd )";
	public static final String ST_COLLECTION_DETAILS_FOR_BO_DG = "select ii.dg_sale_amt 'dg_sale', kk.dg_refund 'dg_sale_refund', jj.dg_pwt_amt 'dg_pwt' from (( select ifnull(sum(net_amt),0) 'dg_sale_amt'  from st_dg_bo_sale bo, st_lms_bo_transaction_master btm where btm.transaction_id=bo.transaction_id and btm.transaction_type ='DG_SALE' and agent_org_id =? and ( date(btm.transaction_date)>=? and date(btm.transaction_date)<?))ii, (select ifnull(sum(net_amt),0) as 'dg_refund' from st_dg_bo_sale_refund bo, st_lms_bo_transaction_master btm where btm.transaction_id=bo.transaction_id and btm.transaction_type in ('DG_REFUND_CANCEL','DG_REFUND_FAILED') and agent_org_id=? and ( date(btm.transaction_date)>=? and date(btm.transaction_date)<?))kk,(select ifnull(sum(net_amt),0) 'dg_pwt_amt'  from st_dg_bo_pwt bo, st_lms_bo_transaction_master btm where btm.transaction_id=bo.transaction_id and (btm.transaction_type ='DG_PWT' or btm.transaction_type ='DG_PWT_AUTO')and agent_org_id =? and ( date(btm.transaction_date)>=? and date(btm.transaction_date)<?))jj)";
	public static final String ST_COLLECTION_DETAILS_FOR_BO_OLA = "select ii.deposit_amt 'ola_deposit', kk.withdrawl_amt 'withdrawl_amt', jj.net_gaming_amt 'net_gaming_amt' from (( select ifnull(sum(net_amt),0) 'deposit_amt'  from st_ola_bo_deposit bo, st_lms_bo_transaction_master btm where btm.transaction_id=bo.transaction_id and btm.transaction_type ='OLA_DEPOSIT' and agent_org_id =? and ( date(btm.transaction_date)>=? and date(btm.transaction_date)<?))ii, (select ifnull(sum(net_amt),0) as 'withdrawl_amt' from st_ola_bo_withdrawl bo, st_lms_bo_transaction_master btm where btm.transaction_id=bo.transaction_id and btm.transaction_type ='OLA_WITHDRAWL' and agent_org_id=? and ( date(btm.transaction_date)>=? and date(btm.transaction_date)<?))kk,(select ifnull(sum(commission_calculated),0) 'net_gaming_amt'  from st_ola_bo_agt_commisiion bo, st_lms_bo_transaction_master btm where btm.transaction_id=bo.transaction_id and btm.transaction_type ='OLA_COMMISSION' and agt_org_id =? and ( date(btm.transaction_date)>=? and date(btm.transaction_date)<?))jj)";
	public static final String ST_COLLECTION_DETAILS_FOR_BO_SE = "select ff.pwt_amt 'se_pwt', dd.sale_amt 'se_sale', ee.sale_return_amt 'se_sale_ret' from (( select ifnull(sum(net_amt),0) 'sale_amt'  from st_se_bo_agent_transaction bo, st_lms_bo_transaction_master btm where btm.transaction_id=bo.transaction_id and bo.transaction_type ='SALE' and agent_org_id =? and ( date(btm.transaction_date)>=? and date(btm.transaction_date)<?)  )dd, ( select ifnull(sum(net_amt),0) 'sale_return_amt'  from st_se_bo_agent_transaction bo, st_lms_bo_transaction_master btm where btm.transaction_id=bo.transaction_id and bo.transaction_type ='SALE_RET' and agent_org_id =? and ( date(btm.transaction_date)>=? and date(btm.transaction_date)<?) )ee, ( select ifnull(sum(bpwt.pwt_amt),0) 'pwt_amt' from st_se_bo_pwt bpwt, st_lms_bo_transaction_master btm where bpwt.agent_org_id=? and btm.transaction_id=bpwt.transaction_id and ( date(btm.transaction_date)>=? and date( btm.transaction_date)<?)  )ff)";
	public static final String ST_COLLECTION_DETAILS_FOR_BO_CS = "select ii.cs_sale_amt 'cs_sale', kk.cs_refund 'cs_sale_refund' from (( select ifnull(sum(net_amt),0) 'cs_sale_amt'  from st_cs_bo_sale bo, st_lms_bo_transaction_master btm where btm.transaction_id=bo.transaction_id and btm.transaction_type ='CS_SALE' and agent_org_id =? and ( date(btm.transaction_date)>=? and date(btm.transaction_date)<?))ii, (select ifnull(sum(net_amt),0) as 'cs_refund' from st_cs_bo_refund bo, st_lms_bo_transaction_master btm where btm.transaction_id=bo.transaction_id and btm.transaction_type in ('CS_CANCEL_SERVER','CS_CANCEL_RET') and agent_org_id=? and ( date(btm.transaction_date)>=? and date(btm.transaction_date)<?))kk)";
	public static final String ST_DG_DIR_PLR_PWT_REPORT_AGENT_WISE_BO = "";
	// Direct Player PWT Reports
	public static final String ST_DG_DIR_PLR_PWT_REPORT_GAME_WISE_AGT = "(select ifnull(lef.game_name, rig.game_name) name, ifnull(tot_pwt_clm,0)tpc, ifnull(tot_net_clm,0)tnc, ifnull(tot_pwt_unclm,0)tpu, ifnull(tot_net_unclm,0)tnu from(select game_name, tot_pwt_clm, tot_net_clm from(select draw_id, game_id, transaction_date, ifnull(sum(pwt_amt),0)as tot_pwt_clm, ifnull(sum(net_amt),0) as tot_net_clm  from st_dg_agt_direct_plr_pwt where (date(transaction_date)>=? and date(transaction_date)<?) and pwt_claim_status='DONE_CLM' and agent_org_id = ? group by game_id)tot, (select game_name, game_id from st_dg_game_master)myn where tot.game_id = myn.game_id) as lef left outer join (select game_name, tot_pwt_unclm, tot_net_unclm from(select draw_id, game_id, transaction_date, ifnull(sum(pwt_amt),0)as tot_pwt_unclm, ifnull(sum(net_amt),0) as tot_net_unclm  from st_dg_agt_direct_plr_pwt where (date(transaction_date)>=? and date(transaction_date)<?) and pwt_claim_status='CLAIM_BAL' and agent_org_id = ? group by game_id)tot, (select game_name, game_id from st_dg_game_master)myn where tot.game_id = myn.game_id)as rig on lef.game_name=rig.game_name) union (select ifnull(lef.game_name, rig.game_name) name, ifnull(tot_pwt_clm,0)tpc, ifnull(tot_net_clm,0)tnc, ifnull(tot_pwt_unclm,0)tpu, ifnull(tot_net_unclm,0)tnu from(select game_name, tot_pwt_clm, tot_net_clm from(select draw_id, game_id, transaction_date, ifnull(sum(pwt_amt),0)as tot_pwt_clm, ifnull(sum(net_amt),0) as tot_net_clm  from st_dg_agt_direct_plr_pwt where (date(transaction_date)>=? and date(transaction_date)<?) and pwt_claim_status='DONE_CLM' and agent_org_id = ? group by game_id)tot, (select game_name, game_id from st_dg_game_master)myn where tot.game_id = myn.game_id) as lef right outer join (select game_name, tot_pwt_unclm, tot_net_unclm from(select draw_id, game_id, transaction_date, ifnull(sum(pwt_amt),0)as tot_pwt_unclm, ifnull(sum(net_amt),0) as tot_net_unclm  from st_dg_agt_direct_plr_pwt where (date(transaction_date)>=? and date(transaction_date)<?) and pwt_claim_status='CLAIM_BAL' and agent_org_id = ? group by game_id)tot, (select game_name, game_id from st_dg_game_master)myn where tot.game_id = myn.game_id)as rig on lef.game_name=rig.game_name)";

	public static final String ST_DG_DIR_PLR_PWT_REPORT_GAME_WISE_AGT_NEW = "select game_name, tot.mrpPwt as MrpPwt, tot.netPwt as NetPwt from(select game_id, transaction_date, ifnull(sum(pwt_amt),0)as mrpPwt, ifnull(sum(pwt_amt + agt_claim_comm),0) as netPwt  from st_dg_agt_direct_plr_pwt where (date(transaction_date)>=? and date(transaction_date)<?) and pwt_claim_status in ('DONE_CLM','CLAIM_BAL')  and agent_org_id = ? group by game_id)tot, (select game_name, game_id from st_dg_game_master)myn where tot.game_id = myn.game_id";

	public static final String ST_DG_DIR_PLR_PWT_REPORT_GAME_WISE_BO = "select game_name, total_pwt from (select game_id, ifnull(sum(pwt_amt),0) as total_pwt from st_dg_bo_direct_plr_pwt where (date(transaction_date)>=? and date(transaction_date)<?) group by game_id) as myn, (select game_name, game_id from st_dg_game_master)tot where myn.game_id = tot.game_id";
	// Added by Neeraj
	public static final String ST_DG_JACKPOT_REPORT_GAME_WISE_BO = "select  gdm.draw_id,gdm.draw_datetime,gdm.total_sale_value,gm.prize_fund,gm.RSR_for_this_draw,gm.total_available_RSR,gm.RSR_utilized_in_this_draw,gm.jackpot_for_this_draw,gm.total_available_jackpot,gm.fixed_prizes_fund,gm.remaining_prize_fund,gm.carried_over_RSR,gm.carried_over_jackpot from ge_miscellaneous_? gm,ge_draw_master_?  gdm  where  gm.draw_id=gdm.draw_id and gdm.draw_status='CLAIM ALLOW' and (gdm.draw_datetime>=? and gdm.draw_datetime<?) ";

	public static final String ST_DG_PWT_REPORT_AGENT_WISE_BO = "select top.name as name, myn.MrpClm as MrpClm, myn.MrpUnclm as MrpUnclm from (select ifnull(clm.agent_org_id, unclm.agent_org_id) as agent_id, ifnull(clm.MrpClm,0) as MrpClm, ifnull(unclm.MrpUnclm,0) as MrpUnclm from (select agent_org_id, ifnull(sum(pwt_amt),0) as MrpClm, status from st_dg_agt_pwt sdap,st_lms_agent_transaction_master slatm where  (date(transaction_date)>=? and date(transaction_date)<?) and sdap.transaction_id=slatm.transaction_id and status = 'DONE_CLM' group by agent_org_id)clm left outer join (select agent_org_id, ifnull(sum(pwt_amt),0) as MrpUnclm, status from st_dg_agt_pwt sdap,st_lms_agent_transaction_master slatm where  (date(transaction_date>=?)and date(transaction_date)<?) and sdap.transaction_id=slatm.transaction_id and (status = 'UNCLAIM_BAL' or status = 'CLAIM_BAL') group by agent_org_id)unclm on clm.agent_org_id = unclm.agent_org_id)as myn,(select name, organization_id from st_lms_organization_master where organization_type='AGENT')top where myn.agent_id = top.organization_id";
	public static final String ST_DG_PWT_REPORT_GAME_WISE_AGT = "select top.game_name as gamename,  myn.agtMrpClaimed as agtMrpClaimed, myn.agtNetClaimed as agtNetClaimed, myn.agtMrpUnclm as agtMrpUnclm, myn.agtNetUnclm as agtNetUnclm from ((select ifnull(unclaimed.gid, claimed.gid) as gid, ifnull(claimed.agtMrpClaimed, 0) as agtMrpClaimed, ifnull(claimed.agtNetClaimed,0) as agtNetClaimed, ifnull(unclaimed.agtMrpUnclm,0) as agtMrpUnclm, ifnull(unclaimed.agtNetUnclm,0) as agtNetUnclm from (select ret1.game_id as gid, ifnull(sum(agtMrp),0) as agtMrpClaimed, ifnull(sum(agtNet), 0) as agtNetClaimed from (select transaction_id from st_lms_agent_transaction_master where (date(transaction_date) >=? and date(transaction_date) < ?) and transaction_type = 'DG_PWT_AUTO' and user_org_id = ?)unclm, (select transaction_id, game_id, pwt_amt as agtMrp, pwt_amt+agt_claim_comm as agtNet from st_dg_agt_pwt where status = 'DONE_CLM')ret1 where unclm.transaction_id = ret1.transaction_id group by ret1.game_id )as claimed left outer join (select ret1.game_id as gid, ifnull(sum(agtMrp),0) as agtMrpUnclm, ifnull(sum(agtNet), 0) as agtNetUnclm from (select transaction_id from st_lms_agent_transaction_master where (date(transaction_date)>=? and date(transaction_date)<?) and transaction_type = 'DG_PWT_AUTO' and user_org_id = ?)unclm,(select transaction_id, game_id, pwt_amt as agtMrp, pwt_amt+agt_claim_comm as agtNet from st_dg_agt_pwt where status = 'CLAIM_BAL' or 'UNCLAIM_BAL')ret1 where unclm.transaction_id = ret1.transaction_id group by ret1.game_id)as unclaimed on claimed.gid = unclaimed.gid)union (select ifnull(unclaimed.gid, claimed.gid) as gid, ifnull(claimed.agtMrpClaimed, 0) as agtMrpClaimed, ifnull(claimed.agtNetClaimed,0) as agtNetClaimed, ifnull(unclaimed.agtMrpUnclm,0) as agtMrpUnclm, ifnull(unclaimed.agtNetUnclm,0) as agtNetUnclm from(select ret1.game_id as gid, ifnull(sum(agtMrp),0) as agtMrpClaimed, ifnull(sum(agtNet), 0) as agtNetClaimed from (select transaction_id from st_lms_agent_transaction_master where (date(transaction_date)>=? and date(transaction_date)<?) and transaction_type = 'DG_PWT_AUTO' and user_org_id = ?)unclm,(select transaction_id, game_id, pwt_amt as agtMrp, pwt_amt+agt_claim_comm as agtNet from st_dg_agt_pwt where status = 'DONE_CLM')ret1 where unclm.transaction_id = ret1.transaction_id group by ret1.game_id)as claimed right outer join (select ret1.game_id as gid, ifnull(sum(agtMrp),0) as agtMrpUnclm, ifnull(sum(agtNet), 0) as agtNetUnclm from (select transaction_id from st_lms_agent_transaction_master where (date(transaction_date)>=? and date(transaction_date) < ?) and transaction_type = 'DG_PWT_AUTO' and user_org_id = ?)unclm, (select transaction_id, game_id, pwt_amt as agtMrp, pwt_amt+agt_claim_comm as agtNet from st_dg_agt_pwt where status = 'CLAIM_BAL' or 'UNCLAIM_BAL')ret1 where unclm.transaction_id = ret1.transaction_id group by ret1.game_id)as unclaimed on claimed.gid = unclaimed.gid))as myn,(select game_name, game_id from st_dg_game_master)as top where myn.gid = top.game_id";
	public static final String ST_DG_PWT_REPORT_GAME_WISE_AGT_NEW = "select mub.game_name as gamename,  tot.agtMrpPwt as MrpPwt, tot.agtNetPwt as NetPwt from (select ret1.game_id as gid, ifnull(sum(agtMrp),0) as agtMrpPwt, ifnull(sum(agtNet), 0) as agtNetPwt from (select transaction_id from st_lms_agent_transaction_master where (date(transaction_date) >=? and date(transaction_date) < ?) and transaction_type = 'DG_PWT_AUTO' and user_org_id = ?)clm, (select transaction_id, game_id, pwt_amt as agtMrp, pwt_amt+agt_claim_comm as agtNet from st_dg_agt_pwt where status = 'DONE_CLM' or status= 'CLAIM_BAL')ret1 where clm.transaction_id = ret1.transaction_id group by ret1.game_id )as tot, (select game_id, game_name from st_dg_game_master)mub where tot.gid = mub.game_id";

	public static final String ST_DG_PWT_REPORT_GAME_WISE_BO = "select top.game_name as gamename,  myn.agtMrpClaimed as agtMrpClaimed, myn.agtMrpUnclm as agtMrpUnclm from ((select ifnull(unclaimed.gid, claimed.gid) as gid, ifnull(claimed.agtMrpClaimed, 0) as agtMrpClaimed, ifnull(unclaimed.agtMrpUnclm,0) as agtMrpUnclm from (select ret1.game_id as gid, ifnull(sum(agtMrp),0) as agtMrpClaimed from (select transaction_id from st_lms_agent_transaction_master where (date(transaction_date) >=? and date(transaction_date) <?) and transaction_type = 'DG_PWT_AUTO')unclm, (select transaction_id, game_id, pwt_amt as agtMrp from st_dg_agt_pwt where status = 'DONE_CLM')ret1 where unclm.transaction_id = ret1.transaction_id group by ret1.game_id )as claimed left outer join (select ret1.game_id as gid, ifnull(sum(agtMrp),0) as agtMrpUnclm from (select transaction_id from st_lms_agent_transaction_master where (date(transaction_date)>=? and date(transaction_date)<?) and transaction_type = 'DG_PWT_AUTO')unclm,(select transaction_id, game_id, pwt_amt as agtMrp from st_dg_agt_pwt where status = 'UNCLAIM_BAL' or 'CLAIM_BAL')ret1 where unclm.transaction_id = ret1.transaction_id group by ret1.game_id)as unclaimed on claimed.gid = unclaimed.gid)union (select ifnull(unclaimed.gid, claimed.gid) as gid, ifnull(claimed.agtMrpClaimed, 0) as agtMrpClaimed, ifnull(unclaimed.agtMrpUnclm,0) as agtMrpUnclm from(select ret1.game_id as gid, ifnull(sum(agtMrp),0) as agtMrpClaimed from (select transaction_id from st_lms_agent_transaction_master where (date(transaction_date)>=? and date(transaction_date)<?) and transaction_type = 'DG_PWT_AUTO')unclm,(select transaction_id, game_id, pwt_amt as agtMrp from st_dg_agt_pwt where status = 'DONE_CLM')ret1 where unclm.transaction_id = ret1.transaction_id group by ret1.game_id)as claimed right outer join (select ret1.game_id as gid, ifnull(sum(agtMrp),0) as agtMrpUnclm from (select transaction_id from st_lms_agent_transaction_master where (date(transaction_date)>=? and date(transaction_date) < ?) and transaction_type = 'DG_PWT_AUTO')unclm, (select transaction_id, game_id, pwt_amt as agtMrp from st_dg_agt_pwt where status = 'UNCLAIM_BAL' or 'CLAIM_BAL')ret1 where unclm.transaction_id = ret1.transaction_id group by ret1.game_id)as unclaimed on claimed.gid = unclaimed.gid))as myn,(select game_name, game_id from st_dg_game_master)as top where myn.gid = top.game_id";
	public static final String ST_DG_PWT_REPORT_GAME_WISE_RET = "select top.game_name as gamename,  myn.agtMrpClaimed as agtMrpClaimed, myn.agtNetClaimed as agtNetClaimed, myn.agtMrpUnclm as agtMrpUnclm, myn.agtNetUnclm as agtNetUnclm from ((select ifnull(unclaimed.gid, claimed.gid) as gid, ifnull(claimed.agtMrpClaimed, 0) as agtMrpClaimed, ifnull(claimed.agtNetClaimed,0) as agtNetClaimed, ifnull(unclaimed.agtMrpUnclm,0) as agtMrpUnclm, ifnull(unclaimed.agtNetUnclm,0) as agtNetUnclm from (select unclm.game_id as gid, ifnull(sum(agtMrp),0) as agtMrpClaimed, ifnull(sum(agtNet), 0) as agtNetClaimed from (select transaction_id, game_id from st_lms_retailer_transaction_master where (date(transaction_date) >= ? and date(transaction_date) < ?) and transaction_type = 'DG_PWT' and retailer_user_id = ?)unclm,(select transaction_id, (pwt_amt - retailer_claim_comm) as agtMrp, (pwt_amt - retailer_claim_comm - agt_claim_comm)as agtNet, game_id from st_dg_ret_pwt_1 where status = 'DONE_CLM' union select transaction_id, (pwt_amt - retailer_claim_comm) as agtMrp, (pwt_amt - retailer_claim_comm - agt_claim_comm)as agtNet, game_id from st_dg_ret_pwt_2 where status = 'DONE_CLM' union select transaction_id, (pwt_amt - retailer_claim_comm) as agtMrp, (pwt_amt - retailer_claim_comm - agt_claim_comm)as agtNet, game_id from st_dg_ret_pwt_3 where status = 'DONE_CLM' union select transaction_id, (pwt_amt - retailer_claim_comm) as agtMrp, (pwt_amt - retailer_claim_comm - agt_claim_comm)as agtNet, game_id from st_dg_ret_pwt_4 where status = 'DONE_CLM' union select transaction_id, (pwt_amt - retailer_claim_comm) as agtMrp, (pwt_amt - retailer_claim_comm - agt_claim_comm)as agtNet, game_id from st_dg_ret_pwt_5 where status = 'DONE_CLM' union select transaction_id, (pwt_amt - retailer_claim_comm) as agtMrp, (pwt_amt - retailer_claim_comm - agt_claim_comm)as agtNet, game_id from st_dg_ret_pwt_6 where status = 'DONE_CLM' union select transaction_id, (pwt_amt - retailer_claim_comm) as agtMrp, (pwt_amt - retailer_claim_comm - agt_claim_comm)as agtNet, game_id from st_dg_ret_pwt_7 where status = 'DONE_CLM' union select transaction_id, (pwt_amt - retailer_claim_comm) as agtMrp, (pwt_amt - retailer_claim_comm - agt_claim_comm)as agtNet, game_id from st_dg_ret_pwt_8 where status = 'DONE_CLM')ret1 where unclm.transaction_id = ret1.transaction_id group by unclm.game_id)as claimed left outer join(select unclm.game_id as gid, ifnull(sum(agtMrp),0) as agtMrpUnclm, ifnull(sum(agtNet), 0) as agtNetUnclm from (select transaction_id, game_id from st_lms_retailer_transaction_master where (date(transaction_date)>=? and date(transaction_date) < ?) and transaction_type = 'DG_PWT' and retailer_user_id = ?)unclm,(select transaction_id, (pwt_amt - retailer_claim_comm) as agtMrp, (pwt_amt - retailer_claim_comm - agt_claim_comm)as agtNet, game_id from st_dg_ret_pwt_1 where status = 'CLAIM_BAL' or status = 'UNCLAIM_BAL' union select transaction_id, (pwt_amt - retailer_claim_comm) as agtMrp, (pwt_amt - retailer_claim_comm - agt_claim_comm)as agtNet, game_id from st_dg_ret_pwt_2 where status = 'CLAIM_BAL' or status = 'UNCLAIM_BAL' union select transaction_id, (pwt_amt - retailer_claim_comm) as agtMrp, (pwt_amt - retailer_claim_comm - agt_claim_comm)as agtNet, game_id from st_dg_ret_pwt_3 where status = 'CLAIM_BAL' or status = 'UNCLAIM_BAL' union select transaction_id, (pwt_amt - retailer_claim_comm) as agtMrp, (pwt_amt - retailer_claim_comm - agt_claim_comm)as agtNet, game_id from st_dg_ret_pwt_4 where status =  'CLAIM_BAL' or status = 'UNCLAIM_BAL' union select transaction_id, (pwt_amt - retailer_claim_comm) as agtMrp, (pwt_amt - retailer_claim_comm - agt_claim_comm)as agtNet, game_id from st_dg_ret_pwt_5 where status = 'CLAIM_BAL' or status = 'UNCLAIM_BAL' union select transaction_id, (pwt_amt - retailer_claim_comm) as agtMrp, (pwt_amt - retailer_claim_comm - agt_claim_comm)as agtNet, game_id from st_dg_ret_pwt_6 where status = 'CLAIM_BAL' or status = 'UNCLAIM_BAL' union select transaction_id, (pwt_amt - retailer_claim_comm) as agtMrp, (pwt_amt - retailer_claim_comm - agt_claim_comm)as agtNet, game_id from st_dg_ret_pwt_7 where status = 'CLAIM_BAL' or status = 'UNCLAIM_BAL' union select transaction_id, (pwt_amt - retailer_claim_comm) as agtMrp, (pwt_amt - retailer_claim_comm - agt_claim_comm)as agtNet, game_id from st_dg_ret_pwt_8 where status = 'CLAIM_BAL' or status = 'UNCLAIM_BAL')ret1 where unclm.transaction_id = ret1.transaction_id group by unclm.game_id )as unclaimed on claimed.gid = unclaimed.gid)union (select ifnull(unclaimed.gid, claimed.gid) as gid, ifnull(claimed.agtMrpClaimed, 0) as agtMrpClaimed, ifnull(claimed.agtNetClaimed,0) as agtNetClaimed, ifnull(unclaimed.agtMrpUnclm,0) as agtMrpUnclm, ifnull(unclaimed.agtNetUnclm,0) as agtNetUnclm from (select unclm.game_id as gid, ifnull(sum(agtMrp),0) as agtMrpClaimed, ifnull(sum(agtNet), 0) as agtNetClaimed from (select transaction_id, game_id from st_lms_retailer_transaction_master where (date(transaction_date)>= ? and date(transaction_date)< ?)and transaction_type = 'DG_PWT' and retailer_user_id = ?)unclm,(select transaction_id, (pwt_amt - retailer_claim_comm) as agtMrp, (pwt_amt - retailer_claim_comm - agt_claim_comm)as agtNet, game_id from st_dg_ret_pwt_1 where status = 'DONE_CLM' union select transaction_id, (pwt_amt - retailer_claim_comm) as agtMrp, (pwt_amt - retailer_claim_comm - agt_claim_comm)as agtNet, game_id from st_dg_ret_pwt_2 where status = 'DONE_CLM' union select transaction_id, (pwt_amt - retailer_claim_comm) as agtMrp, (pwt_amt - retailer_claim_comm - agt_claim_comm)as agtNet, game_id from st_dg_ret_pwt_3 where status = 'DONE_CLM' union select transaction_id, (pwt_amt - retailer_claim_comm) as agtMrp, (pwt_amt - retailer_claim_comm - agt_claim_comm)as agtNet, game_id from st_dg_ret_pwt_4 where status = 'DONE_CLM' union select transaction_id, (pwt_amt - retailer_claim_comm) as agtMrp, (pwt_amt - retailer_claim_comm - agt_claim_comm)as agtNet, game_id from st_dg_ret_pwt_5 where status = 'DONE_CLM' union select transaction_id, (pwt_amt - retailer_claim_comm) as agtMrp, (pwt_amt - retailer_claim_comm - agt_claim_comm)as agtNet, game_id from st_dg_ret_pwt_6 where status = 'DONE_CLM' union select transaction_id, (pwt_amt - retailer_claim_comm) as agtMrp, (pwt_amt - retailer_claim_comm - agt_claim_comm)as agtNet, game_id from st_dg_ret_pwt_7 where status = 'DONE_CLM' union select transaction_id, (pwt_amt - retailer_claim_comm) as agtMrp, (pwt_amt - retailer_claim_comm - agt_claim_comm)as agtNet, game_id from st_dg_ret_pwt_8 where status = 'DONE_CLM')ret1 where unclm.transaction_id = ret1.transaction_id group by unclm.game_id )as claimed right outer join(select unclm.game_id as gid, ifnull(sum(agtMrp),0) as agtMrpUnclm, ifnull(sum(agtNet), 0) as agtNetUnclm from (select transaction_id, game_id from st_lms_retailer_transaction_master where (date(transaction_date) >=? and date(transaction_date)<?) and transaction_type = 'DG_PWT' and retailer_user_id = ?)unclm,(select transaction_id, (pwt_amt - retailer_claim_comm) as agtMrp, (pwt_amt - retailer_claim_comm - agt_claim_comm)as agtNet, game_id from st_dg_ret_pwt_1 where status = 'CLAIM_BAL' or status = 'UNCLAIM_BAL' union select transaction_id, (pwt_amt - retailer_claim_comm) as agtMrp, (pwt_amt - retailer_claim_comm - agt_claim_comm)as agtNet, game_id from st_dg_ret_pwt_2 where status = 'CLAIM_BAL' or status = 'UNCLAIM_BAL' union select transaction_id, (pwt_amt - retailer_claim_comm) as agtMrp, (pwt_amt - retailer_claim_comm - agt_claim_comm)as agtNet, game_id from st_dg_ret_pwt_3 where status = 'CLAIM_BAL' or status = 'UNCLAIM_BAL' union select transaction_id, (pwt_amt - retailer_claim_comm) as agtMrp, (pwt_amt - retailer_claim_comm - agt_claim_comm)as agtNet, game_id from st_dg_ret_pwt_4 where status =  'CLAIM_BAL' or status = 'UNCLAIM_BAL' union select transaction_id, (pwt_amt - retailer_claim_comm) as agtMrp, (pwt_amt - retailer_claim_comm - agt_claim_comm)as agtNet, game_id from st_dg_ret_pwt_5 where status = 'CLAIM_BAL' or status = 'UNCLAIM_BAL' union select transaction_id, (pwt_amt - retailer_claim_comm) as agtMrp, (pwt_amt - retailer_claim_comm - agt_claim_comm)as agtNet, game_id from st_dg_ret_pwt_6 where status = 'CLAIM_BAL' or status = 'UNCLAIM_BAL' union select transaction_id, (pwt_amt - retailer_claim_comm) as agtMrp, (pwt_amt - retailer_claim_comm - agt_claim_comm)as agtNet, game_id from st_dg_ret_pwt_7 where status = 'CLAIM_BAL' or status = 'UNCLAIM_BAL' union select transaction_id, (pwt_amt - retailer_claim_comm) as agtMrp, (pwt_amt - retailer_claim_comm - agt_claim_comm)as agtNet, game_id from st_dg_ret_pwt_8 where status = 'CLAIM_BAL' or status = 'UNCLAIM_BAL')ret1 where unclm.transaction_id = ret1.transaction_id group by unclm.game_id)as unclaimed on claimed.gid = unclaimed.gid))as myn,(select game_name, game_id from st_dg_game_master)as top where myn.gid = top.game_id";
	public static final String ST_DG_PWT_REPORT_RETAILER_WISE_AGT = "select top.name,  myn.pwtMrpClm as pwtMrpClm, myn.pwtNetClm as pwtNetClm, myn.pwtMrpUnclm as pwtMrpUnclm, myn.pwtNetUnclm as pwtNetUnclm from (select lef.retailer_org_id, ifnull(lef.pwtMrpClm,0) as pwtMrpClm, ifnull(lef.pwtNetClm,0) as pwtNetClm, ifnull(righ.pwtMrpUnclm,0) as pwtMrpUnclm, ifnull(righ.pwtNetUnclm,0) as pwtNetUnclm  from (select dgpclm.retailer_org_id, ifnull(sum(pwtMrpClm),0) as pwtMrpClm, ifnull(sum(pwtNetClm),0) as pwtNetClm from (select transaction_id, user_org_id from st_lms_agent_transaction_master where (date(transaction_date)>=? and date(transaction_date)<?) and (transaction_type='DG_PWT' or transaction_type='DG_PWT_AUTO'))as main, (select transaction_id, agent_org_id, retailer_org_id, pwt_amt as pwtMrpClm, pwt_amt+agt_claim_comm as pwtNetClm from st_dg_agt_pwt where status='DONE_CLM') as dgpclm where main.transaction_id = dgpclm.transaction_id  and main.user_org_id = dgpclm.agent_org_id and dgpclm.agent_org_id = ? group by dgpclm.retailer_org_id) as lef left outer join (select dgpUnclm.retailer_org_id, ifnull(sum(pwtMrpUnclm),0) as pwtMrpUnclm, ifnull(sum(pwtNetUnclm),0) as pwtNetUnclm from (select transaction_id, user_org_id from st_lms_agent_transaction_master where (date(transaction_date)>=? and date(transaction_date)<?) and (transaction_type='DG_PWT' or transaction_type='DG_PWT_AUTO'))as main, (select transaction_id, agent_org_id, retailer_org_id, pwt_amt as pwtMrpUnclm, pwt_amt+agt_claim_comm as pwtNetUnclm from st_dg_agt_pwt where status='CLAIM_BAL' or 'UNCLAIM_BAL') as dgpUnclm where main.transaction_id = dgpUnclm.transaction_id  and main.user_org_id = dgpUnclm.agent_org_id and dgpUnclm.agent_org_id = ? group by dgpUnclm.retailer_org_id)as righ on lef.retailer_org_id = righ.retailer_org_id)as myn,(select organization_id, name from st_lms_organization_master where organization_type='RETAILER')as top where top.organization_id = myn.retailer_org_id";

	public static final String ST_DG_PWT_REPORT_RETAILER_WISE_AGT_NEW = "select top.name,  myn.MrpPwt as PwtMrp, myn.NetPwt as pwtNet from (select dgpclm.retailer_org_id, ifnull(sum(dgpclm.pwtMrp),0) as MrpPwt, ifnull(sum(dgpclm.pwtNet),0) as NetPwt from (select transaction_id, user_org_id from st_lms_agent_transaction_master where (date(transaction_date)>=? and date(transaction_date)<?) and (transaction_type='DG_PWT' or transaction_type='DG_PWT_AUTO'))as main, (select transaction_id, agent_org_id, retailer_org_id, pwt_amt as pwtMrp, pwt_amt+agt_claim_comm as pwtNet from st_dg_agt_pwt where status='DONE_CLM' or status= 'CLAIM_BAL') as dgpclm where main.transaction_id = dgpclm.transaction_id  and main.user_org_id = dgpclm.agent_org_id and dgpclm.agent_org_id = ? group by dgpclm.retailer_org_id)as myn,(select organization_id, name from st_lms_organization_master where organization_type='RETAILER')as top where top.organization_id = myn.retailer_org_id";

	// cash cheque report on agent side
	// public static final String ST_CASH_CHEQ_REPORT_RETAILER_ID="select
	// distinct cash.retailer_org_id from st_lms_agent_cash_transaction cash,
	// st_lms_agent_transaction_master btm where
	// btm.transaction_id=cash.transaction_id and cash.retailer_org_id=
	// btm.retailer_org_id and cash.agent_id=? and (btm.transaction_date>=? and
	// btm.transaction_date<?) union select distinct chq.retailer_org_id from
	// st_lms_agent_sale_chq chq, st_lms_agent_transaction_master btm where
	// btm.transaction_id=chq.transaction_id and chq.retailer_org_id=
	// btm.retailer_org_id and chq.agent_id=? and (btm.transaction_date>=? and
	// btm.transaction_date<?) union select distinct dbt.retailer_org_id from
	// st_lms_agent_debit_note dbt, st_lms_agent_transaction_master btm where
	// btm.transaction_id=dbt.transaction_id and dbt.retailer_org_id=
	// btm.retailer_org_id and dbt.agent_id=? and ( btm.transaction_date>=? and
	// btm.transaction_date<?)";
	// public static final String ST_CASH_CHEQ_REPORT_DETAIL="select aa.cash_amt
	// 'total_cash', bb.chq 'chq_coll', gg.debit_amt, cc.chq_bounce
	// 'chq_bounce', ((aa.cash_amt+bb.chq)-(cc.chq_bounce+gg.debit_amt))
	// 'net_amount' from ((select ifnull(sum(cash.amount),0) 'cash_amt' from
	// st_lms_agent_cash_transaction cash, st_lms_agent_transaction_master btm
	// where cash.agent_id=? and (btm.transaction_date>=? and
	// btm.transaction_date<?) and cash.retailer_org_id=? and
	// btm.transaction_type='CASH' and cash.transaction_id=btm.transaction_id )
	// aa, (select ifnull(sum(chq.cheque_amt),0) 'chq' from
	// st_lms_agent_sale_chq chq, st_lms_agent_transaction_master btm where
	// chq.transaction_type IN ('CHEQUE','CLOSED') and chq.agent_id=? and
	// (btm.transaction_date>=? and btm.transaction_date<?) and
	// chq.retailer_org_id=? and chq.transaction_id=btm.transaction_id) bb,
	// (select ifnull(sum(chq.cheque_amt),0) 'chq_bounce' from
	// st_lms_agent_sale_chq chq, st_lms_agent_transaction_master btm where
	// chq.transaction_type='CHQ_BOUNCE' and chq.agent_id=? and
	// (btm.transaction_date>=? and btm.transaction_date<?) and
	// chq.retailer_org_id=? and chq.transaction_id=btm.transaction_id ) cc,
	// (select ifnull(sum(bo.amount),0) 'debit_amt' from st_lms_agent_debit_note
	// bo, st_lms_agent_transaction_master btm where
	// btm.transaction_id=bo.transaction_id and bo.transaction_type
	// ='DR_NOTE_CASH' and bo.agent_id=? and bo.retailer_org_id =? and (
	// btm.transaction_date>=? and btm.transaction_date<?))gg) ";

	public static final String ST_DG_PWT_REPORT_RETAILER_WISE_BO = "select top.name,  myn.pwtMrpClm as pwtMrpClm, myn.pwtMrpUnclm as pwtMrpUnclm from (select lef.retailer_org_id, ifnull(lef.pwtMrpClm,0) as pwtMrpClm, ifnull(righ.pwtMrpUnclm,0) as pwtMrpUnclm from (select dgpclm.retailer_org_id, ifnull(sum(pwtMrpClm),0) as pwtMrpClm from (select transaction_id, user_org_id from st_lms_agent_transaction_master where (date(transaction_date)>=? and date(transaction_date)<?) and (transaction_type='DG_PWT' or transaction_type='DG_PWT_AUTO'))as main, (select transaction_id, agent_org_id, retailer_org_id, pwt_amt as pwtMrpClm from st_dg_agt_pwt where status='DONE_CLM') as dgpclm where main.transaction_id = dgpclm.transaction_id  and main.user_org_id = dgpclm.agent_org_id and dgpclm.agent_org_id = ? group by dgpclm.retailer_org_id) as lef left outer join (select dgpUnclm.retailer_org_id, ifnull(sum(pwtMrpUnclm),0) as pwtMrpUnclm from (select transaction_id, user_org_id from st_lms_agent_transaction_master where (date(transaction_date)>=? and date(transaction_date)<?) and (transaction_type='DG_PWT' or transaction_type='DG_PWT_AUTO'))as main, (select transaction_id, agent_org_id, retailer_org_id, pwt_amt as pwtMrpUnclm from st_dg_agt_pwt where status='CLAIM_BAL' or status = 'UNCLAIM_BAL') as dgpUnclm where main.transaction_id = dgpUnclm.transaction_id  and main.user_org_id = dgpUnclm.agent_org_id and dgpUnclm.agent_org_id = ? group by dgpUnclm.retailer_org_id)as righ on lef.retailer_org_id = righ.retailer_org_id)as myn,(select organization_id, name from st_lms_organization_master where organization_type='RETAILER')as top where top.organization_id = myn.retailer_org_id";
	// Draw game reports
	public static final String ST_DG_SALE_REPORT = "select sale.name, sale.game_name, ifnull(sale.sum_sale_mrp,0) 'sum_sale_mrp', ifnull(refund.sum_refund_mrp,0) 'sum_refund_mrp', ifnull(sale.sum_sale_net,0) 'sum_sale_net', ifnull(refund.sum_refund_net,0) 'sum_refund_net', sale.pkey from (select party_id, ifnull(sum(mrp_amt),0)  'sum_sale_mrp', ifnull(sum(net_amt),0)  'sum_sale_net', name, game_name, concat(party_id,'-', gm.game_id) 'pkey'  from st_lms_bo_transaction_master btm, st_dg_bo_sale bo, st_lms_organization_master om, st_dg_game_master gm where btm.transaction_id=bo.transaction_id and btm.transaction_type ='DG_SALE' and  om.organization_id=btm.party_id and gm.game_id = bo.game_id and ( btm.transaction_date>=? and btm.transaction_date<?) group by party_id, gm.game_id) sale left join (select party_id, ifnull(sum(mrp_amt),0)  'sum_refund_mrp', ifnull(sum(net_amt),0)  'sum_refund_net', name, game_name, concat(party_id,'-', gm.game_id) 'pkey'  from st_lms_bo_transaction_master btm, st_dg_bo_sale_refund bo, st_lms_organization_master om, st_dg_game_master gm where btm.transaction_id=bo.transaction_id and (btm.transaction_type ='DG_REFUND_FAILED' or btm.transaction_type = 'DG_REFUND_CANCEL' ) and  om.organization_id=btm.party_id and gm.game_id = bo.game_id and ( btm.transaction_date>=? and btm.transaction_date<?) group by party_id, gm.game_id) refund on  refund.pkey = sale.pkey union select  refund.name, refund.game_name, ifnull(sale.sum_sale_mrp,0) 'sum_sale_mrp', ifnull(refund.sum_refund_mrp,0) 'sum_refund_mrp', ifnull(sale.sum_sale_net,0) 'sum_sale_net', ifnull(refund.sum_refund_net,0) 'sum_refund_net', refund.pkey from (select party_id, ifnull(sum(mrp_amt),0)  'sum_sale_mrp', ifnull(sum(net_amt),0)  'sum_sale_net', name, game_name, concat(party_id,'-', gm.game_id) 'pkey'  from st_lms_bo_transaction_master btm, st_dg_bo_sale bo, st_lms_organization_master om, st_dg_game_master gm where btm.transaction_id=bo.transaction_id and btm.transaction_type ='DG_SALE' and  om.organization_id=btm.party_id and gm.game_id = bo.game_id and ( btm.transaction_date>=? and btm.transaction_date<?) group by party_id, gm.game_id) sale right join (select party_id, ifnull(sum(mrp_amt),0)  'sum_refund_mrp', ifnull(sum(net_amt),0)  'sum_refund_net', name, game_name, concat(party_id,'-', gm.game_id) 'pkey'  from st_lms_bo_transaction_master btm, st_dg_bo_sale_refund bo, st_lms_organization_master om, st_dg_game_master gm where btm.transaction_id=bo.transaction_id and (btm.transaction_type ='DG_REFUND_FAILED' or btm.transaction_type ='DG_REFUND_CANCEL' ) and  om.organization_id=btm.party_id and gm.game_id = bo.game_id and ( btm.transaction_date>=? and btm.transaction_date<?) group by party_id, gm.game_id) refund on  refund.pkey = sale.pkey";

	// reports queries on agent side sale report
	// public static final String ST_AGENT_SALE_REPORT_GET_GAME_ID="select
	// distinct bo.game_id from st_se_agent_retailer_transaction
	// bo,st_lms_agent_transaction_master btm where
	// btm.transaction_id=bo.transaction_id and btm.agent_id=? and (
	// btm.transaction_date>=? and btm.transaction_date<?) union select distinct
	// bo.game_id from st_se_bo_agent_transaction
	// bo,st_lms_bo_transaction_master btm where
	// btm.transaction_id=bo.transaction_id and bo.agent_org_id=? and (
	// btm.transaction_date>=? and btm.transaction_date<?) order by game_id
	// desc";
	// public static final String ST_SALE_REPORT_GET_RETAILER_ORG_ID="select
	// distinct bo.retailer_org_id from st_se_agent_retailer_transaction
	// bo,st_lms_agent_transaction_master btm where
	// btm.transaction_id=bo.transaction_id and ( btm.transaction_date>=? and
	// btm.transaction_date<?) and bo.agent_id=?";
	// public static final String ST_AGENT_SALE_REPORT_GAME_WISE="select
	// ifnull(e.ee,0) 'remaining_books',ifnull(a.aa,0) 'books_purchase_from_bo',
	// ifnull(b.bb,0) 'books_returned_to_bo' ,ifnull(c.cc,0)
	// 'books_sale_to_retailer', ifnull(d.dd,0) 'books_returned_by_retailer'
	// from ( ( select sum(nbr_of_books) aa from st_se_bo_agent_transaction bo,
	// st_lms_bo_transaction_master btm where
	// btm.transaction_id=bo.transaction_id and bo.transaction_type ='SALE' and
	// bo.agent_org_id=? and ( btm.transaction_date>=? and
	// btm.transaction_date<?) and game_id =? ) a , ( select sum(nbr_of_books)
	// bb from st_se_bo_agent_transaction bo, st_lms_bo_transaction_master btm
	// where btm.transaction_id=bo.transaction_id and bo.transaction_type
	// ='SALE_RET' and bo.agent_org_id=? and ( btm.transaction_date>=? and
	// btm.transaction_date<?) and game_id =? ) b , (select sum(nbr_of_books) cc
	// from st_se_agent_retailer_transaction bo, st_lms_agent_transaction_master
	// btm where btm.transaction_id=bo.transaction_id and bo.transaction_type
	// ='SALE' and bo.agent_id=? and ( btm.transaction_date>=? and
	// btm.transaction_date<?) and game_id =? ) c, (select sum(nbr_of_books) dd
	// from st_se_agent_retailer_transaction bo, st_lms_agent_transaction_master
	// btm where btm.transaction_id=bo.transaction_id and bo.transaction_type
	// ='SALE_RET' and bo.agent_id=? and ( btm.transaction_date>=? and
	// btm.transaction_date<?) and game_id =? )d, (select count(book_nbr) ee
	// from st_se_game_inv_status where current_owner='AGENT' and
	// current_owner_id=? and game_id=?) e)";
	// public static final String ST_SALE_REPORT_RETAILER_WISE="select
	// ifnull(a.aa,0) 'books_sold', ifnull(a.cc,0) 'net_sale_amt',
	// ifnull(b.dd,0) 'net_books_returned_amt', ifnull(b.bb,0) 'books_returned'
	// from ((select sum(nbr_of_books) aa, sum(net_amt) cc from
	// st_se_agent_retailer_transaction bo, st_lms_agent_transaction_master btm
	// where btm.transaction_id=bo.transaction_id and bo.transaction_type
	// ='SALE' and ( btm.transaction_date>=? and btm.transaction_date<?) and
	// bo.agent_id =? and bo.retailer_org_id=? ) a , (select sum(nbr_of_books)
	// bb, sum(net_amt) dd from st_se_agent_retailer_transaction bo,
	// st_lms_agent_transaction_master btm where
	// btm.transaction_id=bo.transaction_id and bo.transaction_type ='SALE_RET'
	// and ( btm.transaction_date>=? and btm.transaction_date<?) and bo.agent_id
	// =? and bo.retailer_org_id=? ) b)";

	public static final String ST_DG_SALE_REPORT_AGENT_WISE = "select top.user_name as userName, sum(myn.totalSale) as totalSale, sum(myn.totalRefund) as totalRefund from(select sale.user_org_id as ret_id, ifnull(sale.saleMrp, 0)as totalSale, ifnull(refund.refundAmt, 0)as totalRefund  from(select main.transaction_id, main.user_org_id , ifnull(sum(agent_net_amt),0) as saleMrp from(select transaction_id, user_org_id from st_lms_agent_transaction_master where (date(transaction_date)>= ? and date(transaction_date)<?) and transaction_type = 'DG_SALE')main,(select agent_ref_transaction_id, agent_net_amt from st_dg_ret_sale_1 union select agent_ref_transaction_id, agent_net_amt from st_dg_ret_sale_2 union select agent_ref_transaction_id, agent_net_amt from st_dg_ret_sale_3 union select agent_ref_transaction_id, agent_net_amt from st_dg_ret_sale_4 union select agent_ref_transaction_id, agent_net_amt from st_dg_ret_sale_5 union select agent_ref_transaction_id, agent_net_amt from st_dg_ret_sale_6 union select agent_ref_transaction_id, agent_net_amt from st_dg_ret_sale_7 union select agent_ref_transaction_id, agent_net_amt from st_dg_ret_sale_8 )ret1 where main.transaction_id = ret1.agent_ref_transaction_id group by ret1.agent_ref_transaction_id)as sale left outer join (select main.transaction_id, main.user_org_id , ifnull(sum(agent_net_amt),0) as refundAmt from(select transaction_id, user_org_id from st_lms_agent_transaction_master where (date(transaction_date)>= ? and date(transaction_date)<?) and transaction_type in('DG_REFUND_CANCEL','DG_REFUND_FAILED'))main,(select agent_ref_transaction_id, agent_net_amt,retailer_org_id from st_dg_ret_sale_refund_1 union select agent_ref_transaction_id, agent_net_amt, retailer_org_id from st_dg_ret_sale_refund_2 union select agent_ref_transaction_id, agent_net_amt, retailer_org_id from st_dg_ret_sale_refund_3 union select agent_ref_transaction_id, agent_net_amt, retailer_org_id from st_dg_ret_sale_refund_4 union select agent_ref_transaction_id, agent_net_amt, retailer_org_id from st_dg_ret_sale_refund_5 union select agent_ref_transaction_id, agent_net_amt,retailer_org_id from st_dg_ret_sale_refund_6 union select agent_ref_transaction_id, agent_net_amt,retailer_org_id from st_dg_ret_sale_refund_7 union select agent_ref_transaction_id, agent_net_amt,retailer_org_id from st_dg_ret_sale_refund_8)ret1 where main.transaction_id = ret1.agent_ref_transaction_id group by ret1.agent_ref_transaction_id)as refund on sale.user_org_id = refund.user_org_id)as myn,(select user_id, user_name from st_lms_user_master where organization_type='AGENT')as top where myn.ret_id = top.user_id group by UserName";
	public static final String ST_DG_SALE_REPORT_AGENT_WISE_BO = "select naming.name as agent_name, fine.SaleMrp as SaleMrp, fine.RefundMrp as RefundMrp, (fine.SaleMrp - fine.RefundMrp)as NetMrp, fine.SaleNet as SaleNet, fine.RefundNet as RefundNet, (fine.SaleNet - fine.RefundNet)as NetNet from (select sale.party_id, ifnull(sale.saleMrp,0) as SaleMrp, ifnull(refund.refundMrp,0)as RefundMrp, ifnull(sale.saleNet, 0) as SaleNet, ifnull(refund.refundNet, 0) as RefundNet from (select party_id , ifnull(sum(mrp_amt),0) as saleMrp, ifNull(sum(net_amt),0) as saleNet from (select transaction_id, party_id from st_lms_bo_transaction_master where (date(transaction_date)>=? and date(transaction_date)<?) and transaction_type = 'DG_SALE')main, (select transaction_id, mrp_amt, net_amt from st_dg_bo_sale)ret1 where main.transaction_id = ret1.transaction_id group by party_id)as sale left outer join (select party_id , ifnull(sum(mrp_amt),0) as refundMrp, ifNull(sum(net_amt),0) as refundNet from (select transaction_id, party_id from st_lms_bo_transaction_master where (date(transaction_date)>=? and date(transaction_date)<? ) and transaction_type in('DG_REFUND_CANCEL','DG_REFUND_FAILED'))main, (select transaction_id, mrp_amt, net_amt from st_dg_bo_sale_refund)ret1 where main.transaction_id = ret1.transaction_id group by party_id)as refund on sale.party_id = refund.party_id)as fine,(select organization_id, name from st_lms_organization_master)as naming where fine.party_id = naming.organization_id";
	public static final String ST_DG_SALE_REPORT_GAME_WISE = "select  sale.game_name, ifnull(sale.sum_sale_mrp,0) 'sum_sale_mrp', ifnull(refund.sum_refund_mrp,0) 'sum_refund_mrp', ifnull(sale.sum_sale_net,0) 'sum_sale_net', ifnull(refund.sum_refund_net,0) 'sum_refund_net' from (select ifnull(sum(mrp_amt),0)  'sum_sale_mrp', ifnull(sum(net_amt),0)  'sum_sale_net', game_name, gm.game_id 'pkey'  from st_lms_bo_transaction_master btm, st_dg_bo_sale bo, st_dg_game_master gm where btm.transaction_id=bo.transaction_id and btm.transaction_type ='DG_SALE' and gm.game_id = bo.game_id and ( btm.transaction_date>=? and btm.transaction_date<?) group by gm.game_id) sale left join (select ifnull(sum(mrp_amt),0)  'sum_refund_mrp', ifnull(sum(net_amt),0)  'sum_refund_net', game_name, gm.game_id 'pkey'  from st_lms_bo_transaction_master btm, st_dg_bo_sale_refund bo, st_dg_game_master gm where btm.transaction_id=bo.transaction_id and (btm.transaction_type ='DG_REFUND_FAILED' or btm.transaction_type ='DG_REFUND_CANCEL' ) and gm.game_id = bo.game_id and ( btm.transaction_date>=? and btm.transaction_date<?) group by gm.game_id) refund on  refund.pkey = sale.pkey union select refund.game_name, ifnull(sale.sum_sale_mrp,0) 'sum_sale_mrp', ifnull(refund.sum_refund_mrp,0) 'sum_refund_mrp', ifnull(sale.sum_sale_net,0) 'sum_sale_net', ifnull(refund.sum_refund_net,0) 'sum_refund_net' from (select ifnull(sum(mrp_amt),0)  'sum_sale_mrp', ifnull(sum(net_amt),0)  'sum_sale_net', game_name, gm.game_id 'pkey'  from st_lms_bo_transaction_master btm, st_dg_bo_sale bo, st_dg_game_master gm where btm.transaction_id=bo.transaction_id and btm.transaction_type ='DG_SALE' and gm.game_id = bo.game_id and ( btm.transaction_date>=? and btm.transaction_date<?) group by gm.game_id) sale right join (select ifnull(sum(mrp_amt),0)  'sum_refund_mrp', ifnull(sum(net_amt),0)  'sum_refund_net', game_name, gm.game_id 'pkey'  from st_lms_bo_transaction_master btm, st_dg_bo_sale_refund bo,  st_dg_game_master gm where btm.transaction_id=bo.transaction_id and (btm.transaction_type ='DG_REFUND_FAILED' or btm.transaction_type ='DG_REFUND_CANCEL' ) and gm.game_id = bo.game_id and ( btm.transaction_date>=? and btm.transaction_date<?) group by gm.game_id) refund on  refund.pkey = sale.pkey";
	public static final String ST_DG_SALE_REPORT_GAME_WISE_AGT = "select naming.game_name as game_name, fine.SaleMrp as SaleMrp, fine.RefundMrp as RefundMrp, (fine.SaleMrp - fine.RefundMrp)as NetMrp, fine.SaleNet as SaleNet, fine.RefundNet as RefundNet, (fine.SaleNet - fine.RefundNet)as NetNet from (select sale.game_id, ifnull(sale.saleMrp,0) as SaleMrp, ifnull(refund.refundMrp,0)as RefundMrp, ifnull(sale.saleNet, 0) as SaleNet, ifnull(refund.refundNet, 0) as RefundNet from (select game_id , ifnull(sum(mrp_amt),0) as saleMrp, ifNull(sum(net_amt),0) as saleNet from (select transaction_id, user_org_id from st_lms_agent_transaction_master where (date(transaction_date)>=? and date(transaction_date) < ?) and transaction_type = 'DG_SALE' and user_org_id = ?)main,(select transaction_id, game_id, mrp_amt, mrp_amt-agent_comm as net_amt from st_dg_agt_sale)ret1 where main.transaction_id = ret1.transaction_id group by game_id)as sale left outer join (select game_id , ifnull(sum(mrp_amt),0) as refundMrp, ifNull(sum(net_amt),0) as refundNet from (select transaction_id, user_org_id from st_lms_agent_transaction_master where (date(transaction_date) >=? and date(transaction_date)<?)  and transaction_type in ('DG_REFUND_CANCEL','DG_REFUND_FAILED') and user_org_id = ?)main,(select transaction_id, game_id, mrp_amt, mrp_amt-agent_comm as net_amt from st_dg_agt_sale_refund)ret1 where main.transaction_id = ret1.transaction_id group by game_id)as refund on sale.game_id = refund.game_id)as fine,(select game_id, game_name from st_dg_game_master)as naming where fine.game_id = naming.game_id order by fine.game_id asc";

	public static final String ST_DG_SALE_REPORT_GAME_WISE_BO = "select naming.game_name as game_name, fine.SaleMrp as SaleMrp, fine.RefundMrp as RefundMrp, (fine.SaleMrp - fine.RefundMrp)as NetMrp, fine.SaleNet as SaleNet, fine.RefundNet as RefundNet, (fine.SaleNet - fine.RefundNet)as NetNet from (select sale.game_id, ifnull(sale.saleMrp,0) as SaleMrp, ifnull(refund.refundMrp,0)as RefundMrp, ifnull(sale.saleNet, 0) as SaleNet, ifnull(refund.refundNet, 0) as RefundNet from (select game_id , ifnull(sum(mrp_amt),0) as saleMrp, ifNull(sum(net_amt),0) as saleNet from (select transaction_id from st_lms_bo_transaction_master where (date(transaction_date) >= ? and date(transaction_date) < ?) and transaction_type = 'DG_SALE')main,(select transaction_id, mrp_amt, mrp_amt-agent_comm as net_amt, game_id from st_dg_bo_sale)ret1 where main.transaction_id = ret1.transaction_id group by game_id)as sale left outer join (select game_id , ifnull(sum(mrp_amt),0) as refundMrp, ifNull(sum(net_amt),0) as refundNet from (select transaction_id from st_lms_bo_transaction_master where (date(transaction_date) >= ? and date(transaction_date) < ?) and transaction_type in('DG_REFUND_CANCEL','DG_REFUND_FAILED'))main,(select transaction_id, mrp_amt, mrp_amt-agent_comm as net_amt, game_id from st_dg_bo_sale_refund)ret1 where main.transaction_id = ret1.transaction_id group by game_id)as refund on sale.game_id = refund.game_id)as fine,(select game_id, game_name from st_dg_game_master)as naming where fine.game_id = naming.game_id order by fine.game_id asc";
	public static final String ST_DG_SALE_REPORT_GAME_WISE_RET = "select naming.game_name as game_name, fine.SaleMrp as SaleMrp, fine.RefundMrp as RefundMrp, (fine.SaleMrp - fine.RefundMrp)as NetMrp, fine.SaleNet as SaleNet, fine.RefundNet as RefundNet, (fine.SaleNet - fine.RefundNet)as NetNet from (select sale.game_id, ifnull(sale.saleMrp,0) as SaleMrp, ifnull(refund.refundMrp,0)as RefundMrp, ifnull(sale.saleNet, 0) as SaleNet, ifnull(refund.refundNet, 0) as RefundNet from (select game_id , ifnull(sum(mrp_amt),0) as saleMrp, ifNull(sum(net_amt),0) as saleNet from (select transaction_id, retailer_org_id from st_lms_retailer_transaction_master where (date(transaction_date) >= ? and date(transaction_date) < ?) and transaction_type = 'DG_SALE'and retailer_user_id = ?)main,(?))ret1 where main.transaction_id = ret1.transaction_id group by game_id)as sale left outer join (select game_id , ifnull(sum(mrp_amt),0) as refundMrp, ifNull(sum(net_amt),0) as refundNet from (select transaction_id, retailer_org_id from st_lms_retailer_transaction_master where (date(transaction_date) >= ? and date(transaction_date) < ?) and transaction_type in('DG_REFUND_CANCEL','DG_REFUND_FAILED') and retailer_user_id = ?)main,(?))ret1 where main.transaction_id = ret1.transaction_id group by game_id)as refund on sale.game_id = refund.game_id)as fine,(select game_id, game_name from st_dg_game_master)as naming where fine.game_id = naming.game_id order by fine.game_id asc";
	public static final String ST_DG_SALE_REPORT_RETAILER_WISE_AGT = "select naming.name as ret_name, fine.SaleMrp as SaleMrp, fine.RefundMrp as RefundMrp, (fine.SaleMrp - fine.RefundMrp)as NetMrp, fine.SaleNet as SaleNet, fine.RefundNet as RefundNet, (fine.SaleNet - fine.RefundNet)as NetNet from (select sale.retailer_org_id as retailer_org_id, ifnull(sale.saleMrp,0) as SaleMrp, ifnull(refund.refundMrp,0)as RefundMrp, ifnull(sale.saleNet, 0) as SaleNet, ifnull(refund.refundNet, 0) as RefundNet from (select ret1.retailer_org_id as retailer_org_id , ifnull(sum(mrp_amt),0) as saleMrp, ifNull(sum(agent_net_amt),0) as saleNet from (select transaction_id, user_org_id from st_lms_agent_transaction_master where (date(transaction_date)>=? and date(transaction_date)<?) and transaction_type = 'DG_SALE')main, (select transaction_id, mrp_amt, mrp_amt-agent_comm as agent_net_amt, retailer_org_id from st_dg_agt_sale)ret1 where main.transaction_id = ret1.transaction_id group by retailer_org_id )as sale left outer join (select ret1.retailer_org_id , ifnull(sum(mrp_amt),0) as refundMrp, ifNull(sum(agent_net_amt),0) as refundNet from (select transaction_id, user_org_id from st_lms_agent_transaction_master where (date(transaction_date)>= ? and date(transaction_date)< ?) and transaction_type in('DG_REFUND_CANCEL','DG_REFUND_FAILED'))main, (select transaction_id, mrp_amt, mrp_amt-agent_comm as agent_net_amt, retailer_org_id from st_dg_agt_sale_refund)ret1 where main.transaction_id = ret1.transaction_id group by retailer_org_id )as refund on sale.retailer_org_id = refund.retailer_org_id)as fine, (select organization_id, name from st_lms_organization_master where parent_id = ?)as naming where fine.retailer_org_id = naming.organization_id";
	public static final String ST_DG_SALE_REPORT_RETAILER_WISE_BO = "select naming.name as ret_name, fine.SaleMrp as SaleMrp, fine.RefundMrp as RefundMrp, (fine.SaleMrp - fine.RefundMrp)as NetMrp, fine.SaleNet as SaleNet, fine.RefundNet as RefundNet, (fine.SaleNet - fine.RefundNet)as NetNet from (select sale.retailer_org_id as retailer_org_id, ifnull(sale.saleMrp,0) as SaleMrp, ifnull(refund.refundMrp,0)as RefundMrp, ifnull(sale.saleNet, 0) as SaleNet, ifnull(refund.refundNet, 0) as RefundNet from (select ret1.retailer_org_id as retailer_org_id , ifnull(sum(mrp_amt),0) as saleMrp, ifNull(sum(agent_net_amt),0) as saleNet from (select transaction_id, user_org_id from st_lms_agent_transaction_master where (date(transaction_date)>=? and date(transaction_date)<?) and transaction_type = 'DG_SALE')main, (select transaction_id, mrp_amt, mrp_amt-agent_comm as agent_net_amt, retailer_org_id from st_dg_agt_sale)ret1 where main.transaction_id = ret1.transaction_id group by retailer_org_id )as sale left outer join (select ret1.retailer_org_id , ifnull(sum(mrp_amt),0) as refundMrp, ifNull(sum(agent_net_amt),0) as refundNet from (select transaction_id, user_org_id from st_lms_agent_transaction_master where (date(transaction_date)>= ? and date(transaction_date)< ?) and transaction_type in ('DG_REFUND_CANCEL','DG_REFUND_FAILED'))main, (select transaction_id, mrp_amt, mrp_amt-agent_comm as agent_net_amt, retailer_org_id from st_dg_agt_sale_refund)ret1 where main.transaction_id = ret1.transaction_id group by retailer_org_id )as refund on sale.retailer_org_id = refund.retailer_org_id)as fine, (select organization_id, name from st_lms_organization_master where parent_id = ?)as naming where fine.retailer_org_id = naming.organization_id";
	public static final String ST_FETCH_CONS_COUNTS_FORORG = "select party_org_id 'current_owner_id',name, party_type 'current_owner_type', sum(cumm_qty_count) 'inv_count', cost_per_unit 'cost', bb.inv_id, cc.inv_name from st_lms_cons_inv_status aa, st_lms_cons_inv_specification bb, st_lms_inv_master cc, st_lms_organization_master ee where aa.inv_model_id =  bb.inv_model_id and aa.party_org_id = ee.organization_id and bb.inv_id = cc.inv_id  ";
	public static final String ST_FETCH_CONS_DETAIL_FORORG = "select  name, party_type 'owner', inv_name, 'brand_name', concat(spec_value, '-', spec_unit, '-', spec_type) 'model_name', 'serial_no', 'inv_code', cost_per_unit 'cost', 'is_new', cumm_qty_count 'count'  from st_lms_cons_inv_status aa, st_lms_cons_inv_specification bb,  st_lms_inv_master dd, st_lms_organization_master ee where aa.inv_model_id = bb.inv_model_id and bb.inv_id = dd.inv_id and ee.organization_id = aa.party_org_id  ";

	// By Arun's Queries for Consumable and Non-Consumable Inventory
	public static final String ST_FETCH_NON_CONS_COUNTS_FORORG = "select current_owner_id, name, current_owner_type, count(serial_no) 'inv_count', cost_to_bo 'cost', inv_model_id, cc.inv_name, bb.inv_id, brand_id from st_lms_inv_status aa, st_lms_inv_model_master bb, st_lms_inv_master cc,st_lms_organization_master ee where aa.inv_model_id =  bb.model_id and aa.current_owner_id = ee.organization_id and bb.inv_id = cc.inv_id and  current_owner_type<>'REMOVED' ";
	public static final String ST_FETCH_NON_CONS_DETAIL_FORORG = "select  name, current_owner_type 'owner', inv_name, brand_name, model_name, aa.serial_no,inv_code, cost_to_bo 'cost', is_new, '1' as 'count'  from st_lms_inv_status aa, st_lms_inv_model_master bb, st_lms_inv_brand_master cc , st_lms_inv_master dd, st_lms_organization_master ee,st_lms_inv_mapping ff  where aa.inv_model_id = bb.model_id and bb.brand_id = cc.brand_id and cc.inv_id = dd.inv_id and bb.inv_id = dd.inv_id and ee.organization_id = aa.current_owner_id  and ff.serial_no=aa.serial_no and ff.inv_model_id=aa.inv_model_id ";
	public static final String ST_GAME_NAME = "select game_name, ticket_price, nbr_of_tickets_per_book, (ticket_price*nbr_of_tickets_per_book) 'book_price' from st_se_game_master  where game_id=?";
	public static final String ST_GET_ORG_NAME = "select name from st_lms_organization_master where organization_id=?";
	public static final String ST_INVENTORY_GAME_REPORT = "select a.bo_count,b.agt_count,c.ret_count  from ((select count(book_nbr) bo_count from  st_se_game_inv_status where  current_owner = 'BO' and game_id  = ?) a , (select count(book_nbr) agt_count from  st_se_game_inv_status where  current_owner = 'AGENT' and game_id  = ?) b , (select count(book_nbr) ret_count from  st_se_game_inv_status where  current_owner = 'RETAILER' and game_id  = ?) c)";

	public static final String ST_INVENTORY_GAME_REPORT_FOR_AGENT = "select a.agt_count,b.ret_count from((select count(book_nbr) agt_count from  st_se_game_inv_status where current_owner = 'AGENT' and current_owner_id = ? and game_id  = ?) a,(select count(book_nbr) ret_count from  st_se_game_inv_status where  current_owner = 'RETAILER' and current_owner_id in (select organization_id from st_lms_organization_master where parent_id = ?) and game_id  = ? ) b)";
	public static final String ST_INVENTORY_GAME_REPORT_FOR_AGENT_RET_ONLINE = "select a.agt_count,b.ret_count,c.active_count from((select count(book_nbr) agt_count from  st_se_game_inv_status where current_owner = 'AGENT' and current_owner_id = ? and game_id  = ?) a, (select count(book_nbr) ret_count from  st_se_game_inv_status where  current_owner = 'RETAILER' and current_owner_id in (select organization_id from st_lms_organization_master where parent_id = ?) and game_id  = ? ) b, (select count(book_nbr) active_count from  st_se_game_inv_status where  current_owner = 'RETAILER' and book_status = 'ACTIVE' and  current_owner_id in (select organization_id from st_lms_organization_master where parent_id = ?) and game_id  = ?) c)";
	public static final String ST_INVENTORY_GAME_REPORT_FOR_RETAILER = "select a.ret_count,b.active_count from ( (select count(book_nbr) ret_count from  st_se_game_inv_status where  current_owner = 'RETAILER' and current_owner_id = ? and game_id  = ? ) a,(select count(book_nbr) active_count from  st_se_game_inv_status where  current_owner = 'RETAILER' and book_status = 'ACTIVE' and  current_owner_id = ? and game_id  = ?) b)";

	public static final String ST_INVENTORY_GAME_REPORT_RET_ONLINE = "select a.bo_count,b.agt_count,c.ret_count, d.active_count from ((select count(book_nbr) bo_count from  st_se_game_inv_status where  current_owner = 'BO' and game_id  = ?) a , (select count(book_nbr) agt_count from  st_se_game_inv_status where  current_owner = 'AGENT' and game_id  = ?) b , (select count(book_nbr) ret_count from  st_se_game_inv_status where  current_owner = 'RETAILER' and game_id  = ?) c, (select count(book_nbr) active_count from  st_se_game_inv_status where  current_owner = 'RETAILER' and book_status = 'ACTIVE' and game_id  = ?) d)";
	public static final String ST_INVENTORY_GAME_SEARCH = "select distinct(game_id), game_nbr, game_name, start_date, sale_end_date, pwt_end_date, game_status from st_se_game_master";
	public static final String ST_INVENTORY_GAME_SEARCH_LINK = "select a.sold_by_bo,b.returned_to_bo,c.sold_by_agents,d.returned_to_agents from ((select ifnull(sum(b.nbr_of_books),0) sold_by_bo from st_lms_bo_transaction_master a,st_se_bo_agent_transaction b where a.transaction_id = b.transaction_id and a.transaction_type = 'SALE'and b.game_id = ?) a,(select ifnull(sum(b.nbr_of_books),0) returned_to_bo from st_lms_bo_transaction_master a,st_se_bo_agent_transaction b where a.transaction_id = b.transaction_id and a.transaction_type = 'SALE_RET' and b.game_id = ?) b,(select ifnull(sum(b.nbr_of_books),0) sold_by_agents from st_lms_agent_transaction_master a,st_se_agent_retailer_transaction b where a.transaction_id = b.transaction_id  and a.transaction_type = 'SALE' and b.game_id = ?) c,(select ifnull(sum(b.nbr_of_books),0) returned_to_agents from st_lms_agent_transaction_master a,st_se_agent_retailer_transaction b where a.transaction_id = b.transaction_id  and a.transaction_type = 'SALE_RET' and b.game_id = ?) d)";
	// By Arun's Queries
	public static final String ST_NO_OF_PRIZE_REM = "select total_no_of_prize, no_of_prize_claim, no_of_prize_cancel, (total_no_of_prize-(no_of_prize_claim + no_of_prize_cancel)) 'No of Prizes Remaining' , prize_amt 'pwt_amt'  from st_se_rank_master where game_id =?";
	public static final String ST_PLAYER_PWT_REPORT_BO = "select ifnull(sum(pwt_amt),0) total_pwt_amt from st_se_direct_player_pwt where  transaction_date>=? and transaction_date<?";
	public static final String ST_PWT_PLR_REPORT_AGENT = "select 'Player' as name, ifnull(sum(pwt_amt),0) total_pwt_amt from st_se_agt_direct_player_pwt aa, st_lms_agent_transaction_master atm  where aa.transaction_id = atm.transaction_id and  agent_org_id = ? and ( atm.transaction_date>=? and atm.transaction_date<?) group by agent_org_id";
	public static final String ST_PWT_REPORT_AGENT = "select bb.name, ifnull(sum(apwt.pwt_amt),0) total_pwt_amt   from st_se_agent_pwt apwt, st_lms_agent_transaction_master atm, st_lms_organization_master bb where atm.transaction_id=apwt.transaction_id and bb.organization_id = apwt.retailer_org_id and atm.user_org_id=? and ( atm.transaction_date>=? and atm.transaction_date<?) group by retailer_org_id order by name";
	// public static final String ST_PWT_REPORT_AGENT1="select distinct
	// apwt.retailer_org_id from st_se_agent_pwt apwt,
	// st_lms_agent_transaction_master atm where
	// apwt.retailer_org_id=atm.retailer_org_id and
	// atm.transaction_id=apwt.transaction_id and atm.user_id=? and
	// (atm.transaction_date>=? and atm.transaction_date<?)";
	public static final String ST_PWT_REPORT_AGENT1 = "select distinct apwt.retailer_org_id from st_se_agent_pwt apwt, st_lms_agent_transaction_master atm where apwt.retailer_org_id=atm.party_id  and atm.transaction_id=apwt.transaction_id  and atm.user_org_id=? and (atm.transaction_date>=? and atm.transaction_date<?)";
	public static final String ST_PWT_REPORT_BO = "select ifnull(sum(bpwt.pwt_amt),0) total_pwt_amt from st_se_bo_pwt bpwt, st_lms_bo_transaction_master btm where bpwt.agent_org_id=? and btm.transaction_id=bpwt.transaction_id and ( btm.transaction_date>=? and btm.transaction_date<?)";
	public static final String ST_PWT_REPORT_BO1 = "select distinct bo.agent_org_id from st_se_bo_pwt bo, st_lms_bo_transaction_master btm where bo.agent_org_id=btm.party_id  and bo.transaction_id=btm.transaction_id  and ( btm.transaction_date>=? and btm.transaction_date<?)";
	// public static final String ST_RECEIPT_SEARCH="select aa.id,
	// aa.generated_id, aa.receipt_type,aa.organization_type,aa.name,om.name
	// 'owner_name' from st_lms_organization_master om, ( select borec.id,
	// brm.generated_id,
	// borec.receipt_type,umas.organization_type,umas.name,umas.parent_id from
	// st_lms_bo_receipts borec,st_lms_organization_master umas,
	// st_bo_receipt_gen_mapping brm where brm.id= borec.id and
	// borec.agent_org_id=umas.organization_id)aa where
	// aa.parent_id=om.organization_id ";
	// public static final String ST_RECEIPT_SEARCH="select aa.receipt_id,
	// aa.generated_id, aa.receipt_type,aa.organization_type,aa.name,om.name
	// 'owner_name' from st_lms_organization_master om,( select
	// borec.receipt_id, borec.generated_id,
	// borec.receipt_type,umas.organization_type,umas.name,umas.parent_id from
	// st_lms_bo_receipts borec, st_lms_organization_master
	// umas,st_lms_bo_receipts_trn_mapping brm where brm.receipt_id=
	// borec.receipt_id and borec.party_id=umas.organization_id group by
	// generated_id )aa where aa.parent_id=om.organization_id ";
	public static final String ST_RECEIPT_SEARCH_DLNOTE = "select aa.dl_id,  aa.date, aa.generated_dl_id,aa.organization_type,aa.name,om.name 'owner_name' from st_lms_organization_master om,( select bodl.dl_id, bodl.generated_dl_id,ind.date,umas.organization_type,umas.name,umas.parent_id  from  st_lms_inv_dl_detail bodl, st_lms_organization_master umas,st_lms_inv_dl_task_mapping dtm, st_lms_inv_detail ind where dtm.dl_id= bodl.dl_id and ind.current_owner_id=umas.organization_id  and ind.current_owner_type=umas.organization_type and ind.task_id = dtm.task_id  and ind.current_owner_type='AGENT'";
	public static final String ST_RECEIPT_SEARCH = "select aa.receipt_id,  aa.transaction_date, aa.generated_id, aa.receipt_type,aa.organization_type,aa.name,om.name 'owner_name' from st_lms_organization_master om,( select borec.receipt_id, borec.generated_id,  btm.transaction_date, borec.receipt_type,umas.organization_type,umas.name,umas.parent_id  from  st_lms_bo_receipts borec, st_lms_organization_master umas,st_lms_bo_receipts_trn_mapping brm, st_lms_bo_transaction_master btm where brm.receipt_id= borec.receipt_id and borec.party_id=umas.organization_id and btm.transaction_id = brm.transaction_id  and borec.party_type='AGENT'";
	public static final String ST_RECEIPT_SEARCH_AGENT_ORGID = "select name,organization_id from st_lms_organization_master where organization_type='AGENT' order by name";
	public static final String ST_RECEIPT_SEARCH_DLNOTE_RETAILER = "select aa.dl_id,  aa.date, aa.generated_dl_id,aa.organization_type,aa.name,om.name 'owner_name' from st_lms_organization_master om,( select bodl.dl_id, bodl.generated_dl_id,ind.date,umas.organization_type,umas.name,umas.parent_id  from  st_lms_inv_dl_detail bodl, st_lms_organization_master umas,st_lms_inv_dl_task_mapping dtm, st_lms_inv_detail ind where dtm.dl_id= bodl.dl_id and ind.current_owner_id=umas.organization_id  and ind.current_owner_type=umas.organization_type and ind.task_id = dtm.task_id  and ind.current_owner_type='RETAILER'";
	// public static final String ST_RECEIPT_SEARCH_RET="select aa.id,
	// aa.generated_id
	// ,aa.receipt_type,aa.organization_type,aa.name,aa.parent_id,om.name
	// 'owner_name' from st_lms_organization_master om,(select borec.id,
	// brm.generated_id,
	// borec.receipt_type,umas.organization_type,umas.name,umas.parent_id from
	// st_lms_agent_receipts borec,st_lms_organization_master umas,
	// st_lms_agent_receipts_gen_mapping brm where brm.id= borec.id and
	// borec.retailer_org_id=umas.organization_id)aa where
	// aa.parent_id=om.organization_id ";
	// public static final String ST_RECEIPT_SEARCH_RET="select aa.receipt_id,
	// aa.generated_id
	// ,aa.receipt_type,aa.organization_type,aa.name,aa.parent_id,om.name
	// 'owner_name' from st_lms_organization_master om,(select borec.receipt_id,
	// borec.generated_id,
	// borec.receipt_type,umas.organization_type,umas.name,umas.parent_id from
	// st_lms_agent_receipts borec,st_lms_organization_master
	// umas,st_lms_agent_receipts_trn_mapping brm where brm.receipt_id=
	// borec.receipt_id and borec.party_id=umas.organization_id group by
	// generated_id )aa where aa.parent_id=om.organization_id ";
	public static final String ST_RECEIPT_SEARCH_RET = "select aa.receipt_id,  aa.transaction_date, aa.generated_id ,aa.receipt_type,aa.organization_type,aa.name,aa.parent_id,om.name 'owner_name' from st_lms_organization_master om,(select borec.receipt_id, borec.generated_id, btm.transaction_date, borec.receipt_type,umas.organization_type,umas.name,umas.parent_id  from st_lms_agent_receipts borec,st_lms_organization_master umas,st_lms_agent_receipts_trn_mapping brm, st_lms_agent_transaction_master btm where  brm.receipt_id= borec.receipt_id and borec.party_id=umas.organization_id and btm.transaction_id = brm.transaction_id and borec.party_type='RETAILER'";
	public static final String ST_REPORT_MAIL_SCHEDULAR_GET_AGENT_DETAIL = "select ifnull(a.cc,0) 'net_sale_amt', ifnull(b.dd,0) 'net_books_returned_amt', aa.cash_amt 'total_cash', bb.chq 'chq_coll', cc.chq_bounce 'chq_bounce',dd.retailer_pwt_amt, ee.active_retailer  from (( select sum(net_amt) cc from st_se_agent_retailer_transaction bo, st_lms_agent_transaction_master btm where btm.transaction_id=bo.transaction_id and bo.transaction_type ='SALE' and (btm.transaction_date>=? and btm.transaction_date<?) and bo.agent_user_id =? ) a ,  ( select sum(net_amt) dd  from st_se_agent_retailer_transaction bo, st_lms_agent_transaction_master btm where btm.transaction_id=bo.transaction_id and bo.transaction_type ='SALE_RET'  and ( btm.transaction_date>=? and btm.transaction_date<?) and bo.agent_user_id =? ) b,( select ifnull(sum(cash.amount),0) 'cash_amt' from st_lms_agent_cash_transaction cash, st_lms_agent_transaction_master btm where  (  btm.transaction_date>=? and btm.transaction_date<?) and cash.agent_user_id=? and btm.transaction_type='CASH' and cash.transaction_id=btm.transaction_id ) aa, (select ifnull(sum(chq.cheque_amt),0) 'chq' from  st_lms_agent_sale_chq chq, st_lms_agent_transaction_master btm where chq.transaction_type IN ('CHEQUE','CLOSED')  and (  btm.transaction_date>=? and btm.transaction_date<?) and chq.agent_user_id=? and chq.transaction_id=btm.transaction_id )  bb, (select ifnull(sum(chq.cheque_amt),0) 'chq_bounce' from  st_lms_agent_sale_chq chq, st_lms_agent_transaction_master btm where chq.transaction_type='CHQ_BOUNCE'  and (  btm.transaction_date>=? and btm.transaction_date<?) and chq.agent_user_id=? and chq.transaction_id=btm.transaction_id ) cc, (select ifnull(sum(pwt_amt),0) 'retailer_pwt_amt' from st_se_direct_player_pwt where  transaction_date>=? and transaction_date<? ) dd,(select count(organization_id) 'active_retailer' from st_lms_organization_master where organization_type='RETAILER' and organization_status='ACTIVE'  and  parent_id=?)ee)  ";
	public static final String ST_REPORT_MAIL_SCHEDULER_GET_AGENT_DETAIL = "select so.organization_id,so.name,ucd.email_id from st_lms_organization_master so,st_lms_user_master su,st_lms_user_contact_details ucd where so.organization_id = su.organization_id and su.user_id=ucd.user_id and so.organization_type = 'AGENT' and so.organization_status='ACTIVE' order by so.organization_id";
	// report mail scheduler
	public static final String ST_REPORT_MAIL_SCHEDULER_GET_AGENT_EMAILID = "select uc.email_id,um.user_id,om.organization_id from st_lms_user_contact_details uc, st_lms_user_master um, st_lms_role_master rm, st_lms_organization_master om where rm.role_id = um.role_id and um.user_id=uc.user_id and rm.role_name IN ('AGT_MAS') and um.isrolehead='Y' and om.organization_id=um.organization_id and om.organization_status='ACTIVE'";
	// public static final String ST_REPORT_MAIL_SCHEDULER_GET_BO_DETAIL="select
	// aaa.books_sold_amt, aaa.books_returned_amt, bbb.total_plyer_pwt_amt,
	// ddd.total_agent_pwt_amt, ccc.total_cash, ccc.total_chq,
	// ccc.total_chq_bounce, eee.active_agent, fff.active_retailer from (
	// (select ifnull(a.aa,0) 'books_sold_amt', ifnull(b.bb,0)
	// 'books_returned_amt' from ((select sum(net_amt) aa from
	// st_se_bo_agent_transaction bo, st_lms_bo_transaction_master btm where
	// btm.transaction_id=bo.transaction_id and bo.transaction_type ='SALE' and
	// (btm.transaction_date>=? and btm.transaction_date<?)) a , (select
	// sum(net_amt) bb from st_se_bo_agent_transaction bo,
	// st_lms_bo_transaction_master btm where
	// btm.transaction_id=bo.transaction_id and bo.transaction_type ='SALE_RET'
	// and (btm.transaction_date>=? and btm.transaction_date<?)) b)) aaa,
	// (select ifnull(sum(pwt_amt),0) 'total_plyer_pwt_amt' from
	// st_se_direct_player_pwt where transaction_date>=? and transaction_date<?)
	// bbb, (select ifnull(aa.cash_amt,0) 'total_cash', bb.chq 'total_chq',
	// cc.chq_bounce 'total_chq_bounce' from ((select sum(cash.amount) cash_amt
	// from st_lms_bo_cash_transaction cash, st_lms_bo_transaction_master btm
	// where (btm.transaction_date>=? and btm.transaction_date<?) and
	// cash.transaction_id=btm.transaction_id) aa, (select
	// ifnull(sum(chq.cheque_amt),0) chq from st_lms_bo_sale_chq chq,
	// st_lms_bo_transaction_master btm where chq.transaction_type IN
	// ('CHEQUE','CLOSED') and (btm.transaction_date>=? and
	// btm.transaction_date<?) and chq.transaction_id=btm.transaction_id) bb,
	// (select ifnull(sum(chq.cheque_amt),0) chq_bounce from st_lms_bo_sale_chq
	// chq, st_lms_bo_transaction_master btm where
	// chq.transaction_type='CHQ_BOUNCE' and (btm.transaction_date>=? and
	// btm.transaction_date<?) and chq.transaction_id=btm.transaction_id) cc))
	// ccc, (select ifnull(sum(bpwt.pwt_amt),0) total_agent_pwt_amt from
	// st_se_bo_pwt bpwt , st_lms_bo_transaction_master btm where
	// btm.transaction_id=bpwt.transaction_id and (btm.transaction_date>=? and
	// btm.transaction_date<?)) ddd, (select count(organization_id)
	// 'active_agent' from st_lms_organization_master where
	// organization_type='AGENT' and organization_status='ACTIVE') eee, (select
	// count(organization_id) 'active_retailer' from st_lms_organization_master
	// where organization_type='RETAILER' and organization_status='ACTIVE') fff
	// )";
	public static final String ST_REPORT_MAIL_SCHEDULER_GET_BO_DETAIL = "";
	public static final String ST_REPORT_MAIL_SCHEDULER_GET_BO_EMAILID = "select uc.email_id from st_lms_user_contact_details uc, st_lms_user_master um, st_lms_role_master rm where rm.role_id = um.role_id and um.user_id=uc.user_id and rm.role_name IN ('BO_MAS', 'BO_ADM') and um.isrolehead='Y'";
	// public static final String ST_SALE_REPORT_AGENT_WISE="select
	// ifnull(a.aa,0) 'books_sold', ifnull(a.cc,0) 'net_sale_amt',
	// ifnull(b.dd,0) 'net_books_returned_amt', ifnull(b.bb,0) 'books_returned'
	// from ((select sum(nbr_of_books) aa, sum(net_amt) cc from
	// st_se_bo_agent_transaction bo, st_lms_bo_transaction_master btm where
	// btm.transaction_id=bo.transaction_id and bo.transaction_type ='SALE' and
	// ( btm.transaction_date>=? and btm.transaction_date<?) and agent_org_id =?
	// ) a , ( select sum(nbr_of_books) bb, sum(net_amt) dd from
	// st_se_bo_agent_transaction bo, st_lms_bo_transaction_master btm where
	// btm.transaction_id=bo.transaction_id and bo.transaction_type ='SALE_RET'
	// and ( btm.transaction_date>=? and btm.transaction_date<?) and
	// agent_org_id =?) b)";
	// mrp details added
	public static final String ST_SALE_REPORT_AGENT_WISE = "select ifnull(a.aa,0) 'books_sold', ifnull(a.cc,0) 'net_sale_amt', ifnull(a.mm,0) 'sale_books_mrp', ifnull(b.dd,0) 'net_books_returned_amt', ifnull(b.bb,0) 'books_returned', ifnull(b.mm,0) 'return_books_mrp' from ((select sum(aa) aa,  sum(mm) mm, sum(cc) cc from(select sum(nbr_of_books) aa,  sum(mrp_amt) mm, sum(net_amt) cc from st_se_bo_agent_transaction bo, st_lms_bo_transaction_master btm where btm.transaction_id=bo.transaction_id and bo.transaction_type ='SALE' and ( btm.transaction_date>=? and btm.transaction_date<?) and agent_org_id =? union all select 0 aa,  sum(mrp_amt) mm, sum(net_amt) cc from st_se_bo_agent_loose_book_transaction bo, st_lms_bo_transaction_master btm where btm.transaction_id=bo.transaction_id and bo.transaction_type ='LOOSE_SALE' and ( btm.transaction_date>=? and btm.transaction_date<?) and agent_org_id =?) sale) a ,(select sum(bb) bb, sum(mm) mm, sum(dd) dd  from( select sum(nbr_of_books) bb, sum(mrp_amt) mm, sum(net_amt) dd  from st_se_bo_agent_transaction bo, st_lms_bo_transaction_master btm where btm.transaction_id=bo.transaction_id and bo.transaction_type ='SALE_RET' and ( btm.transaction_date>=? and btm.transaction_date<?) and agent_org_id =? union all select 0 bb, sum(mrp_amt) mm, sum(net_amt) dd  from st_se_bo_agent_loose_book_transaction bo, st_lms_bo_transaction_master btm where btm.transaction_id=bo.transaction_id and bo.transaction_type ='LOOSE_SALE_RET' and ( btm.transaction_date>=? and btm.transaction_date<?) and agent_org_id =?)saleRet ) b)";
	public static final String ST_SALE_REPORT_AGENTS_RET_LIST = "select distinct bo.game_id,bo.retailer_org_id from st_se_agent_retailer_transaction bo,st_lms_agent_transaction_master btm where btm.transaction_id=bo.transaction_id and bo.agent_user_id=? and btm.transaction_date>=?";
	public static final String ST_SALE_REPORT_AGENTS_SALE_DETAIL = "select ifnull(a.aa,0) 'books_sold', ifnull(b.bb,0) 'books_returned'  from ((select sum(nbr_of_books) aa from st_se_agent_retailer_transaction ag, st_lms_agent_transaction_master atm where atm.transaction_id=ag.transaction_id and ag.transaction_type ='SALE' and atm.transaction_date>=? and game_id = ? and ag.retailer_org_id=? and ag.agent_user_id =?) a , (select sum(nbr_of_books) bb from st_se_agent_retailer_transaction ag, st_lms_agent_transaction_master atm where atm.transaction_id=ag.transaction_id and ag.transaction_type ='SALE_RET' and atm.transaction_date>=? and game_id =? and ag.retailer_org_id=? and ag.agent_user_id =?) b)";
	public static final String ST_SALE_REPORT_GAME_WISE = "select ifnull(a.aa,0) 'books_sold',ifnull(c.ee,0) 'remaining_books' , ifnull(a.cc,0) 'net_sale_amt', ifnull(b.dd,0) 'net_books_returned_amt', ifnull(b.bb,0) 'books_returned'  from ((select sum(nbr_of_books) aa, sum(net_amt) cc from st_se_bo_agent_transaction bo, st_lms_bo_transaction_master btm where btm.transaction_id=bo.transaction_id and bo.transaction_type ='SALE' and ( btm.transaction_date>=? and btm.transaction_date<?) and game_id =? ) a , ( select sum(nbr_of_books) bb, sum(net_amt) dd  from st_se_bo_agent_transaction bo, st_lms_bo_transaction_master btm where btm.transaction_id=bo.transaction_id and bo.transaction_type ='SALE_RET' and ( btm.transaction_date>=? and btm.transaction_date<?) and game_id =? ) b, (select count(book_nbr) ee from st_se_game_inv_status where current_owner='BO' and game_id=?)c)";
	public static final String ST_SALE_REPORT_GET_AGENT_ID = "select distinct bo.agent_org_id from st_se_bo_agent_transaction bo,st_lms_bo_transaction_master btm where btm.transaction_id=bo.transaction_id  and ( btm.transaction_date>=? and btm.transaction_date<?)";

	public static final String ST_SALE_REPORT_GET_GAME_ID = "select distinct bo.game_id from st_se_bo_agent_transaction bo,st_lms_bo_transaction_master btm where btm.transaction_id=bo.transaction_id  and ( btm.transaction_date>=? and btm.transaction_date<?) order by bo.game_id desc";

	// public static final String ST1_RETAILER_ORG = "select
	// so.organization_id,so.name,su.user_id from st_lms_organization_master
	// so,st_lms_user_master su,st_lms_role_master sr where so.organization_id =
	// su.organization_id and su.role_id = sr.role_id and so.organization_type =
	// 'RETAILER' and (so.organization_status='ACTIVE' OR
	// so.organization_status='INACTIVE') and sr.role_name ='RET_MAS' and
	// so.parent_id = ?";

	public static final String ST_SALE_REPORT_GET_GAMEID = "select distinct bo.game_id,bo.agent_org_id from st_se_bo_agent_transaction bo,st_lms_bo_transaction_master btm where btm.transaction_id=bo.transaction_id  and btm.transaction_date>=?";
	public static final String ST_SALE_REPORT_GET_RETAILER_ORG_ID = "select retailer_org_id from (select distinct bo.retailer_org_id from st_se_agent_retailer_transaction bo,st_lms_agent_transaction_master btm where btm.transaction_id=bo.transaction_id  and ( btm.transaction_date>=? and btm.transaction_date<?) and btm.user_org_id=? union select distinct bo.retailer_org_id from st_se_agent_ret_loose_book_transaction bo,st_lms_agent_transaction_master btm where btm.transaction_id=bo.transaction_id  and ( btm.transaction_date>=? and btm.transaction_date<?) and btm.user_org_id=?) retOrgId";
	// reports queries
	public static final String ST_SALE_REPORT_GET_SALE_DETAIL = "select ifnull(a.aa,0) 'books_sold', ifnull(b.bb,0) 'books_returned'  from ((select sum(nbr_of_books) aa from st_se_bo_agent_transaction bo, st_lms_bo_transaction_master btm where btm.transaction_id=bo.transaction_id and bo.transaction_type ='SALE' and btm.transaction_date>=? and game_id =? and agent_org_id =?) a , (select sum(nbr_of_books) bb from st_se_bo_agent_transaction bo, st_lms_bo_transaction_master btm where btm.transaction_id=bo.transaction_id and bo.transaction_type ='SALE_RET' and btm.transaction_date>=? and game_id =? and agent_org_id =?) b)";
	// public static final String ST_SALE_REPORT_RETAILER_WISE="select
	// ifnull(a.aa,0) 'books_sold', ifnull(a.cc,0) 'net_sale_amt',
	// ifnull(b.dd,0) 'net_books_returned_amt', ifnull(b.bb,0) 'books_returned'
	// from ((select sum(nbr_of_books) aa, sum(net_amt) cc from
	// st_se_agent_retailer_transaction bo, st_lms_agent_transaction_master btm
	// where btm.transaction_id=bo.transaction_id and bo.transaction_type
	// ='SALE' and ( btm.transaction_date>=? and btm.transaction_date<?) and
	// btm.user_org_id =? and bo.retailer_org_id=? ) a , (select
	// sum(nbr_of_books) bb, sum(net_amt) dd from
	// st_se_agent_retailer_transaction bo, st_lms_agent_transaction_master btm
	// where btm.transaction_id=bo.transaction_id and bo.transaction_type
	// ='SALE_RET' and ( btm.transaction_date>=? and btm.transaction_date<?) and
	// btm.user_org_id =? and bo.retailer_org_id=? ) b)";
	// mrp details added
	public static final String ST_SALE_REPORT_RETAILER_WISE = "select ifnull(a.aa,0) 'books_sold', ifnull(a.cc,0) 'net_sale_amt', ifnull(a.mm,0) 'sale_books_mrp', ifnull(b.dd,0) 'net_books_returned_amt', ifnull(b.bb,0) 'books_returned', ifnull(b.mm,0) 'return_books_mrp' from ((select sum(aa) aa,  sum(mm) mm, sum(cc) cc from(select sum(nbr_of_books) aa,  sum(mrp_amt) mm, sum(net_amt) cc from st_se_agent_retailer_transaction bo, st_lms_agent_transaction_master btm  where btm.transaction_id=bo.transaction_id and bo.transaction_type ='SALE' and ( btm.transaction_date>=? and btm.transaction_date<?) and agent_org_id =? and bo.retailer_org_id=? union all select 0 aa,  sum(mrp_amt) mm, sum(net_amt) cc from st_se_agent_ret_loose_book_transaction bo, st_lms_agent_transaction_master btm where btm.transaction_id=bo.transaction_id and bo.transaction_type ='LOOSE_SALE' and ( btm.transaction_date>=? and btm.transaction_date<?) and agent_org_id =? and bo.retailer_org_id=?) sale) a ,(select sum(bb) bb, sum(mm) mm, sum(dd) dd  from( select sum(nbr_of_books) bb, sum(mrp_amt) mm, sum(net_amt) dd  from st_se_agent_retailer_transaction bo, st_lms_agent_transaction_master btm  where btm.transaction_id=bo.transaction_id and bo.transaction_type ='SALE_RET' and ( btm.transaction_date>=? and btm.transaction_date<?) and agent_org_id =? and bo.retailer_org_id=? union all select 0 bb, sum(mrp_amt) mm, sum(net_amt) dd  from st_se_agent_ret_loose_book_transaction bo, st_lms_agent_transaction_master btm  where btm.transaction_id=bo.transaction_id and bo.transaction_type ='LOOSE_SALE_RET' and ( btm.transaction_date>=? and btm.transaction_date<?) and agent_org_id =? and bo.retailer_org_id=?)saleRet ) b)";
	public static final String ST_STATUS_OF_VIRN_CODE = "";

	public static final String ST1_ACTIVE_AGT_GAMES = "select a.game_id,a.game_name,a.game_nbr,a.agent_pwt_comm_rate,a.retailer_pwt_comm_rate ,b.pwt_comm_variance from st_se_game_master a  left join st_se_bo_agent_sale_pwt_comm_variance b on a.game_id=b.game_id where (a.game_status = 'OPEN' or a.game_status = 'SALE_HOLD' or a.game_status = 'SALE_CLOSE') and b.agent_org_id=?";

	public static final String ST1_ACTIVE_GAMES = "select game_id,game_name,game_nbr,agent_pwt_comm_rate,retailer_pwt_comm_rate from st_se_game_master where game_status = 'OPEN' or game_status = 'SALE_HOLD' or game_status = 'SALE_CLOSE' ";

	public static final String ST1_AGENT_APP_BOOKS = "select  sum(a.nbr_of_books_appr) 'nbr_of_books_appr' from st_se_agent_ordered_games a,st_se_agent_order c where c.order_status='APPROVED' and  a.order_id=c.order_id and a.game_id = ?";

	public static final String ST1_AGENT_APP_ORDER_GAMES = "select saog.order_id,saog.game_id,saog.nbr_of_books_appr,saog.nbr_of_books_dlvrd,sgm.game_name,sgm.game_nbr,sgm.nbr_of_books_per_pack,sgm.ticket_price,sgm.nbr_of_tickets_per_book,sgm.retailer_sale_comm_rate,sgm.prize_payout_ratio,sgm.vat_amt,sgm.govt_comm_rate,sgm.govt_comm_type,sgm.fixed_amt,sgm.tickets_in_scheme,sgm.vat_balance,sgtn.pack_nbr_digits,sgtn.book_nbr_digits,sgtn.game_nbr_digits from st_se_agent_ordered_games saog,st_se_game_master sgm,st_se_game_ticket_nbr_format sgtn where saog.game_id = sgm.game_id and saog.game_id=sgtn.game_id and saog.order_id = ";

	public static final String ST1_AGENT_APPROVED_ORDERS = "select sbo.order_id,sbo.agent_org_id,sbo.retailer_user_id,sbo.retailer_org_id,som.name,sbo.order_date,sbo.order_status from st_se_agent_order sbo,st_se_agent_ordered_games sog,st_lms_organization_master som ,st_se_game_master gm where sbo.retailer_org_id = som.organization_id and sbo.order_status in('APPROVED','SEMI_PROCESSED','ASSIGNED','SEMI_ASSIGNED')and sog.game_id=gm.game_id and sbo.agent_org_id = ";

	public static final String ST1_AGENT_BO_ACC_DETAIL = "select credit_limit,current_credit_amt,available_credit,extended_credit_limit,claimable_bal from st_lms_organization_master  where organization_type='AGENT' and name=";

	public static final String ST1_AGENT_BOOK_INV_VERIFY = "select book_nbr,current_owner,current_owner_id from st_se_game_inv_status where game_id = ";

	public static final String ST1_AGENT_BOOK_VERIFY = "select current_owner,current_owner_id from st_se_game_inv_status where game_id = ? and book_nbr = ?";

	public static final String ST1_AGENT_MASTER = "insert into st_lms_agent_transaction_master(user_id,user_org_id,transaction_type,transaction_date) values (?,?,?,?)";

	// public static final String ST1_AGENT_ORG ="select
	// so.organization_id,so.name,su.user_id from st_lms_organization_master
	// so,st_lms_user_master su,st_lms_role_master sr where so.organization_id =
	// su.organization_id and su.role_id = sr.role_id and so.organization_type =
	// 'AGENT' and sr.role_name ='AGT_MAS' and so.organization_status='ACTIVE'";
	public static final String ST1_AGENT_ORG = "select so.organization_id,so.name,so.organization_type,su.user_id,so.available_credit from st_lms_organization_master so,st_lms_user_master su,st_lms_role_master sr, st_lms_tier_master tm where so.organization_type = 'AGENT' and so.organization_status='ACTIVE' and so.organization_id = su.organization_id and su.isrolehead = 'Y' and   su.role_id = sr.role_id  and tm.tier_id = su.role_id and sr.is_master = 'Y'   and so.parent_id = ?  order by name";
	// public static final String ST1_AGENT_ORG_ACTIVE = "select
	// so.organization_id,so.name,so.organization_type,su.user_id,so.available_credit
	// from st_lms_organization_master so,st_lms_user_master
	// su,st_lms_role_master sr where so.organization_id = su.organization_id
	// and su.role_id = sr.role_id and so.organization_type = 'AGENT' and
	// so.organization_status='ACTIVE' and sr.role_name ='AGT_MAS' and
	// so.parent_id = ?";
	public static final String ST1_AGENT_ORG_WITHOUT_SORT = "select so.organization_id,so.name,so.organization_type,su.user_id,so.available_credit from st_lms_organization_master so,st_lms_user_master su,st_lms_role_master sr, st_lms_tier_master tm where so.organization_type = 'AGENT' and so.organization_status='ACTIVE' and so.organization_id = su.organization_id and su.isrolehead = 'Y' and   su.role_id = sr.role_id  and tm.tier_id = su.role_id and sr.is_master = 'Y'   and so.parent_id = ? ";

	public static final String ST1_AGENT_ORG_ACTIVE = "select so.organization_id as orgId,so.name,so.organization_type,su.user_id,so.available_credit,so.claimable_bal from st_lms_organization_master so,st_lms_user_master su,st_lms_role_master sr, st_lms_tier_master tm where so.organization_type = 'AGENT' and so.organization_status='ACTIVE' and so.organization_id = su.organization_id and su.isrolehead = 'Y' and   su.role_id = sr.role_id  and tm.tier_id = su.role_id and sr.is_master = 'Y'   and so.parent_id = ?  order by name";
	// public static final String ST1_AGENT_ORG_PWT = "select
	// so.organization_id,so.name,su.user_id from st_lms_organization_master
	// so,st_lms_user_master su,st_lms_role_master sr where so.organization_id =
	// su.organization_id and su.role_id = sr.role_id and so.organization_type =
	// 'AGENT' and sr.role_name ='AGT_MAS' and (so.organization_status='ACTIVE'
	// or so.organization_status='INACTIVE')";
	public static final String ST1_AGENT_ORG_PWT = "select so.organization_id,so.name,so.organization_type,su.user_id,so.available_credit from st_lms_organization_master so,st_lms_user_master su,st_lms_role_master sr, st_lms_tier_master tm where so.organization_type = 'AGENT' and (so.organization_status='ACTIVE' or so.organization_status='INACTIVE') and so.organization_id = su.organization_id and su.isrolehead = 'Y' and   su.role_id = sr.role_id  and tm.tier_id = su.role_id and sr.is_master = 'Y'   and so.parent_id = ?  order by name";
	public static final String ST1_AGENT_PACK_INV_VERIFY = "select current_owner,current_owner_id from st_se_game_inv_status where game_id = ? and pack_nbr = ? ";
	public static final String ST1_AGENT_RET_ACC_DETAIL = "select credit_limit,current_credit_amt,available_credit,extended_credit_limit,claimable_bal from st_lms_organization_master  where organization_type='RETAILER' and name=";

	public static final String ST1_AGENT_RETAILER = "insert into st_se_agent_retailer_transaction(transaction_id,game_id,agent_user_id,retailer_org_id,mrp_amt,comm_amt,net_amt,transaction_type,nbr_of_books,vat_amt,taxable_sale,agent_org_id) values (?,?,?,?,?,?,?,?,?,?,?,?)";
	public static final String ST1_AGENT_RETAILER_FOR_LOOSE_SALE = "insert into st_se_agent_ret_loose_book_transaction(transaction_id,game_id,agent_user_id,retailer_org_id,mrp_amt,retComm,comm_amt,net_amt,transaction_type,nbr_of_tickets,vat_amt,taxable_sale,agent_org_id) values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
	public static final String ST1_AGENT_TOTAL_BOOKS = "select count(book_nbr) 'total' from st_se_game_inv_status where  current_owner='AGENT' and current_owner_id = ? and game_id = ?";

	// ADDED BY YOGESH TO MAKE RETAILER ONLINE
	// public static final String ST1_AGT_UPDATE_GAME_INV_STATUS = "update
	// st_se_game_inv_status set current_owner = ?, current_owner_id = ?,
	// book_status=? where game_id = ?";
	public static final String ST1_AGT_UPDATE_GAME_INV_STATUS = "update st_se_game_inv_status set current_owner = ?, current_owner_id = ?, book_status=?, cur_rem_tickets = ?, active_tickets_upto = ?, ret_dl_id=? where game_id = ?";
	
	public static final String ST1_AGENT_UPDATE_GAME_INV_INVOICE_STATUS = "update st_se_game_inv_status set ret_invoice_id = ? where game_id = ? and pack_nbr = ? ";

	public static final String ST1_BO_AGENT = "insert into st_se_bo_agent_transaction(transaction_id,nbr_of_books,game_id,agent_org_id,mrp_amt,comm_amt,net_amt,transaction_type,vat_amt,taxable_sale,gov_comm_amt) values (?,?,?,?,?,?,?,?,?,?,?)";
	public static final String ST1_BO_AGENT_LOOSE_SALE = "insert into st_se_bo_agent_loose_book_transaction(transaction_id, game_id,nbrOfTickets,agent_org_id, mrp_amt, agtSaleCommRate, comm_amt, net_amt, transaction_type, vat_amt, taxable_sale, gov_comm_amt)values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)";
	public static final String ST1_BO_AGENT_TRANSACTION = "insert into st_lms_bo_transaction_master(party_type,party_id,transaction_date,transaction_type) values (?,?,?,?)";

	public static final String ST1_BO_APP_BOOKS = "select  sum(a.nbr_of_books_appr) 'nbr_of_books_appr' from st_se_bo_ordered_games a,st_se_bo_order c where c.order_status='APPROVED' and  a.order_id=c.order_id and a.game_id = ?";

	public static final String ST1_BO_APP_ORDER_GAMES = "select sbog.order_id,sbog.game_id,sbog.nbr_of_books_appr,sbog.nbr_of_books_dlvrd,sgm.game_name,sgm.game_nbr,sgm.nbr_of_books_per_pack,sgm.ticket_price,sgm.nbr_of_tickets_per_book,sgm.agent_sale_comm_rate,sgm.prize_payout_ratio,sgm.vat_amt,sgm.govt_comm_rate,sgm.vat_balance,sgm.govt_comm_type,sgm.fixed_amt,sgm.tickets_in_scheme,sgtn.pack_nbr_digits,sgtn.book_nbr_digits,sgtn.game_nbr_digits from st_se_bo_ordered_games sbog,st_se_game_master sgm,st_se_game_ticket_nbr_format sgtn where sbog.game_id = sgm.game_id and sbog.game_id = sgtn.game_id and sbog.order_id = ";

	// -------------------------changed by arun---------------------------------
	// public static final String ST1_BO_APPROVED_ORDERS = "select
	// sbo.order_id,sbo.agent_org_id,som.name,sbo.order_date,sbo.order_status
	// from st_se_bo_order sbo,st_se_bo_ordered_games
	// sog,st_lms_organization_master som ,st_se_game_master gm where
	// sbo.agent_org_id = som.organization_id and sbo.order_status
	// in('APPROVED','SEMI_PROCESSED')and sog.game_id=gm.game_id";
	public static final String ST1_BO_APPROVED_ORDERS = "select sbo.order_id,sbo.agent_org_id,som.name,sbo.order_date,sbo.order_status from st_se_bo_order sbo,st_se_bo_ordered_games sog,st_lms_organization_master som ,st_se_game_master gm where sbo.agent_org_id = som.organization_id and sbo.order_status in('APPROVED','SEMI_PROCESSED','SEMI_ASSIGNED')and sog.game_id=gm.game_id and sbo.order_id=sog.order_id ";

	public static final String ST1_BO_BOOK_INV_VERIFY = "select book_nbr,current_owner from st_se_game_inv_status where game_id = ";

	public static final String ST1_BO_BOOK_VERIFY = "select current_owner,current_owner_id from st_se_game_inv_status where game_id = ? and book_nbr = ?";

	public static final String ST1_BO_MASTER = "insert into st_lms_bo_transaction_master(transaction_id,party_type,party_id,transaction_date,transaction_type,user_id,user_org_id) values (?,?,?,?,?,?,?)";

	public static final String ST1_BO_PACK_INV_VERIFY = "select current_owner,current_owner_id from st_se_game_inv_status where game_id = ? and pack_nbr = ? ";

	public static final String ST1_BO_TOTAL_BOOKS = "select count(book_nbr) 'total' from st_se_game_inv_status where  current_owner='BO' and game_id = ?";

	// public static final String ST1_BO_UPDATE_GAME_INV_STATUS = "update
	// st_se_game_inv_status set current_owner = ?, current_owner_id = ?,
	// book_status=? where game_id = ?";
	public static final String ST1_BO_UPDATE_GAME_INV_STATUS = "update st_se_game_inv_status set current_owner = ?, current_owner_id = ?, book_status=?, cur_rem_tickets = ?, active_tickets_upto = ?, agent_dl_id=? where game_id = ?";

	public static final String ST1_BOOKS_FOR_PACK = "select book_nbr from st_se_game_inv_status where game_id = ? and pack_nbr = ?";

	public static final String ST1_DISTINCT_PRIZES = "select distinct pwt_amt from st_pwt_inv where game_id = ? order by pwt_amt";

	public static final String ST1_GAME_DETAILS = "select game_id,game_name,game_nbr,ticket_price,start_date,sale_end_date,pwt_end_date,nbr_of_tickets_per_book,nbr_of_books_per_pack,agent_sale_comm_rate,nbr_of_tickets,nbr_of_books from st_se_game_master where game_id = ?";

	public static final String ST1_GAME_FORMAT_INFO = "select game_id,pack_nbr_digits,book_nbr_digits,game_nbr_digits from st_se_game_ticket_nbr_format where game_id in ( ";

	public static final String ST1_GAME_ID_FROM_NAME_NBR = "select a.game_id,b.pack_nbr_digits,b.book_nbr_digits,b.game_nbr_digits from st_se_game_master a,st_se_game_ticket_nbr_format b where a.game_id=b.game_id and a.game_name=? and a.game_nbr=?";

	public static final String ST1_GAME_ID_RET_FROM_NAME_NBR = "select game_id from st_se_game_master where game_name=? and game_nbr=?";
	public static final String ST1_GAME_MASTER_VAT_INSERT = "update st_se_game_master set vat_balance=? where game_id=?";
	public static final String ST1_GAME_PRIZES_LEFT = " select pwt_amt,count(pwt_amt) 'No of Prizes Remaining',game_id,status from st_pwt_inv group by pwt_amt,game_id,status having status = 'UNCLM_PWT' and game_id = ? ";
	public static final String ST1_GAME_WITH_AGENT = "select count(*) 'COUNT' from st_se_game_inv_status where current_owner='AGENT' and current_owner_id=(select organization_id from st_lms_organization_master where name=?) and game_id=?";

	public static final String ST1_GAME_WITH_RETAILER = "select count(*) 'COUNT' from st_se_game_inv_status where current_owner='RETAILER' and current_owner_id in (select organization_id from st_lms_organization_master  where parent_id=?) and game_id=?";

	public static final String ST1_GAME_WITH_RETAILER_BY_AGENT = "select count(*) 'COUNT' from st_se_game_inv_status where current_owner='RETAILER' and current_owner_id in (select organization_id from st_lms_organization_master  where parent_id=(select organization_id  from st_lms_organization_master where name=?)) and game_id=?";

	public static final String ST1_GET_ORG_ADDRESS = "select som.addr_line1,som.addr_line2,som.city,ssm.name 'state',scm.name 'country' from st_lms_organization_master som,st_lms_country_master scm,st_lms_state_master ssm where som.organization_id = ? and som.country_code = scm.country_code and som.state_code = ssm.state_code and som.country_code = ssm.country_code";

	public static final String ST1_INSERT_AGENT_ORDER = "insert into st_se_agent_order(agent_user_id,retailer_user_id,retailer_org_id,order_date,order_status,self_initiated,agent_org_id) values(?,?,?,?,?,?,?)";

	public static final String ST1_INSERT_AGENT_ORDER_INVOICES = "insert into st_se_agent_order_invoices(order_id,invoice_id,retailer_org_id,game_id,order_status,nbr_of_books_dlvrd,agent_user_id,agent_org_id,dc_id) values(?,?,?,?,?,?,?,?,?)";

	public static final String ST1_INSERT_AGENT_ORDERED_GAMES = "insert into st_se_agent_ordered_games(order_id,game_id,nbr_of_books_req,nbr_of_books_appr) values(?,?,?,?)";

	public static final String ST1_INSERT_AGENT_ORDERED_GAMES_BY_RET = "insert into st_se_agent_ordered_games(order_id,game_id,nbr_of_books_req) values(?,?,?)";

	public static final String ST1_INSERT_AGENT_RECEIPTS = "insert into st_lms_agent_receipts(receipt_type,agent_org_id,retailer_org_id) values(?,?,?)";

	public static final String ST1_INSERT_AGENT_RECEIPTS_MAPPING = "insert into st_lms_agent_receipts_trn_mapping(id,transaction_id) values(?,?)";

	public static final String ST1_INSERT_BO_ORDER = "insert into st_se_bo_order(agent_user_id,agent_org_id,order_date,order_status,self_initiated) values(?,?,?,?,?)";

	public static final String ST1_INSERT_BO_ORDERED_GAMES = "insert into st_se_bo_ordered_games(order_id,game_id,nbr_of_books_req) values(?,?,?)";

	public static final String ST1_INSERT_BO_ORDERED_GAMES_AUTO = "insert into st_se_bo_ordered_games(order_id,game_id,nbr_of_books_req,nbr_of_books_appr) values(?,?,?,?)";

	public static final String ST1_INSERT_BO_RECEIPTS = "insert into st_lms_bo_receipts(receipt_type,agent_org_id) values(?,?)";

	public static final String ST1_INSERT_BO_RECEIPTS_MAPPING = "insert into st_lms_bo_receipts_trn_mapping(id,transaction_id) values(?,?)";

	public static final String ST1_INSERT_GAME_INV_DETAIL = "INSERT INTO st_se_game_inv_detail (transaction_id, game_id, pack_nbr, book_nbr, current_owner, current_owner_id, transaction_date, transaction_at, book_status, order_invoice_dc_id, warehouse_id) VALUES (?,?,?,?,?,?,?,?,?,?,?);";
	
	public static final String ST1_INSERT_GAME_INV_WAREHOUSE_DETAIL = "INSERT INTO st_se_game_inv_detail (transaction_id, game_id, pack_nbr, book_nbr, current_owner, current_owner_id, transaction_date, transaction_at, book_status, order_invoice_dc_id, warehouse_id) VALUES (?,?,?,?,?,?,?,?,?,?,?);";

	public static final String ST1_INSERT_GAME_INV_AGENT_INVOICE_DETAIL = "insert into st_se_game_inv_detail(transaction_id, game_id, pack_nbr, book_nbr, current_owner, current_owner_id,transaction_date,transaction_at,transacrion_sale_comm_rate,transaction_gov_comm_rate,transaction_purchase_comm_rate, changed_by_user_id, book_status, warehouse_id, agent_invoice_id) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
	
	public static final String ST1_INSERT_GAME_INV_RETAILER_INVOICE_DETAIL = "insert into st_se_game_inv_detail(transaction_id, game_id, pack_nbr, book_nbr, current_owner, current_owner_id,transaction_date,transaction_at,transacrion_sale_comm_rate,transaction_gov_comm_rate,transaction_purchase_comm_rate, changed_by_user_id, book_status, warehouse_id, agent_invoice_id, ret_invoice_id) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
	
	public static final String ST1_INSERT_ORDER_INVOICES = "insert into st_se_bo_order_invoices(order_id,invoice_id,agent_org_id,game_id,order_status,nbr_of_books_dlvrd,dc_id) values(?,?,?,?,?,?,?)";

	public static final String ST1_INSERT_RET_ORDERED_GAMES = "insert into st_se_agent_ordered_games(order_id,game_id,nbr_of_books_req) values(?,?,?)";

	public static final String ST1_INSERT_ST_BO_MASTER_FOR_CASH = "INSERT INTO st_lms_bo_transaction_master (party_type,party_id,transaction_date,transaction_type) VALUES (?,?,?,?)";

	public static final String ST1_ORG_CREDIT = "select organization_type,current_credit_amt,credit_limit,available_credit from st_lms_organization_master where organization_id = ?";

	public static final String ST1_ORG_CREDIT_UPDATE = "update st_lms_organization_master set current_credit_amt = ?, organization_status = ?, available_credit=? where organization_id = ?";

	public static final String ST1_ORG_FOR_USER = "select organization_id from st_lms_user_master where user_id = ?";

	public static final String ST1_PACK_FOR_BOOK = "select pack_nbr from st_se_game_inv_status where game_id = ? and book_nbr = ?";
	
	public static final String ST1_WAREHOUSE_FOR_BOOK = "select warehouse_id from st_se_game_inv_status where game_id = ? and book_nbr = ?";

	public static final String ST1_PWT_AGENT_DETAIL = "insert into st_se_agent_pwt(virn_code,transaction_id,game_id,agent_user_id,retailer_user_id,retailer_org_id,pwt_amt,comm_amt,net_amt,agent_org_id) values (?,?,?,?,?,?,?,?,?,?)";

	public static final String ST1_PWT_AGENT_MASTER = "insert into st_lms_agent_transaction_master(user_id,user_org_id,transaction_type,transaction_date) values (?,?,?,?)";

	public static final String ST1_PWT_BO_CHECK = " select virn_code, pwt_amt, prize_level, status,id1 from ";

	public static final String ST1_PWT_BO_DETAIL = "insert into st_se_bo_pwt(virn_code,transaction_id,game_id,agent_user_id,agent_org_id,pwt_amt,comm_amt,net_amt) values (?,?,?,?,?,?,?,?)";

	public static final String ST1_PWT_BO_UPDATE = " update st_se_pwt_inv_? set status = 'CLAIM_AGT' where game_id = ? and virn_code = ? and id1=? ";

	// new & update
	public static final String ST1_PWT_CHECK = " select virn_code,pwt_amt,prize_level,status from st_se_pwt_inv_? where game_id=? and virn_code=? ";

	public static final String ST1_PWT_RET_CHECK = " select virn_code,pwt_amt,prize_level,status from st_pwt_inv where game_id=? and virn_code=?";

	// public static final String ST4_INSERT_GAME_INV_DETAIL= "insert into
	// st_se_game_inv_detail (transaction_id,game_id,pack_nbr,
	// book_nbr,current_owner,current_owner_id,transaction_date,transaction_at)
	// values(?,?,?,?,?,?,?,?)";

	public static final String ST1_PWT_UPDATE = " update st_se_pwt_inv_? set status = 'CLAIM_RET' where game_id = ? and virn_code = ? ";

	public static final String ST1_RET_ACC_DETAIL = "select credit_limit,current_credit_amt,available_credit,extended_credit_limit from st_lms_organization_master  where organization_type='RETAILER' and organization_id=";

	// ------- fetch organization details -----------------
	// public static final String ST1_RETAILER_ORG = "select
	// so.organization_id,so.name,su.user_id from st_lms_organization_master
	// so,st_lms_user_master su,st_lms_role_master sr where so.organization_id =
	// su.organization_id and su.role_id = sr.role_id and so.organization_type =
	// 'RETAILER' and (so.organization_status='ACTIVE' OR
	// so.organization_status='INACTIVE') and sr.role_name ='RET_MAS' and
	// su.isrolehead='Y' and so.parent_id = ?";
	// public static final String ST1_RETAILER_ORG_ACTIVE = "select
	// so.organization_id,so.name,su.user_id,so.available_credit from
	// st_lms_organization_master so,st_lms_user_master su,st_lms_role_master sr
	// where so.organization_id = su.organization_id and su.role_id = sr.role_id
	// and so.organization_type = 'RETAILER' and so.organization_status='ACTIVE'
	// and sr.role_name ='RET_MAS' and so.parent_id = ?";
	public static final String ST1_RETAILER_ORG = "select so.organization_id,so.name,so.organization_type,su.user_id,so.available_credit from st_lms_organization_master so,st_lms_user_master su,st_lms_role_master sr, st_lms_tier_master tm where so.organization_type = 'RETAILER' and (so.organization_status='ACTIVE' or so.organization_status='INACTIVE') and so.organization_id = su.organization_id and su.isrolehead = 'Y' and   su.role_id = sr.role_id  and tm.tier_id = su.role_id and sr.is_master = 'Y'   and so.parent_id = ?  order by name";

	// public static final String ST1_RETAILER_ORG_ACTIVE = "select
	// so.organization_id,so.name,su.user_id,so.available_credit from
	// st_lms_organization_master so,st_lms_user_master su,st_lms_role_master sr
	// where so.organization_id = su.organization_id and su.role_id = sr.role_id
	// and so.organization_type = 'RETAILER' and so.organization_status='ACTIVE'
	// and su.isrolehead='Y' and sr.role_name ='RET_MAS' and so.parent_id = ?";
	public static final String ST1_RETAILER_ORG_ACTIVE = "select so.organization_id,so.name,so.organization_type,su.user_id,so.available_credit,so.claimable_bal from st_lms_organization_master so,st_lms_user_master su,st_lms_role_master sr, st_lms_tier_master tm where so.organization_type = 'RETAILER' and so.organization_status='ACTIVE' and so.organization_id = su.organization_id and su.isrolehead = 'Y' and   su.role_id = sr.role_id  and tm.tier_id = su.role_id and sr.is_master = 'Y'   and so.parent_id = ?  order by name";

	public static final String ST1_SEARCH_AGENT_GAME = "select  distinct game_id from st_se_game_inv_status where current_owner = 'AGENT' and current_owner_id = ";

	public static final String ST1_SEARCH_BO_GAME = "select a.game_id,a.game_name,a.game_nbr,a.ticket_price,a.start_date,a.sale_end_date,a.pwt_end_date,a.agent_sale_comm_rate,a.retailer_sale_comm_rate,a.nbr_of_tickets_per_book,b.sale_comm_variance from st_se_game_master a left join st_se_bo_agent_sale_pwt_comm_variance b on a.game_id=b.game_id";

	// 1. Aman's Queries
	public static final String ST1_SEARCH_GAME = "select game_id,game_name,game_nbr,ticket_price,start_date,sale_end_date,pwt_end_date,agent_sale_comm_rate,retailer_sale_comm_rate,nbr_of_tickets_per_book from st_se_game_master";
	public static final String ST1_SEARCH_RET_GAME = "select game_id,game_name,game_nbr,ticket_price,start_date,sale_end_date,pwt_end_date,nbr_of_tickets_per_book,retailer_sale_comm_rate from st_se_game_master";
	public static final String ST1_SEARCH_RETAILER_GAME = "select  distinct game_id from st_se_game_inv_status where current_owner = 'AGENT' and current_owner_id = ";

	public static final String ST1_SEARCH_USER = "select user_id,user_name,status,registration_date from st_lms_user_master";

	public static final String ST1_SELECT_ORG_TYPE_FOR_BO = "select organization_id,organization_type,name from st_lms_organization_master where organization_type='AGENT'";

	public static final String ST1_UPDATE_AGENT_ORDER = "update st_se_agent_order set order_status = ? where order_id = ?";

	public static final String ST1_UPDATE_AGENT_ORDERED_GAMES = "update st_se_agent_ordered_games set nbr_of_books_dlvrd = ? where order_id = ? and game_id = ? ";

	public static final String ST1_UPDATE_BO_ORDER = "update st_se_bo_order set order_status = ? where order_id = ?";

	public static final String ST1_UPDATE_BO_ORDERED_GAMES = "update st_se_bo_ordered_games set nbr_of_books_dlvrd = ? where order_id = ? and game_id = ? ";

	public static final String ST1_UPDATE_GAME_INV_STATUS = "update st_se_game_inv_status set current_owner = ?, current_owner_id = ? where game_id = ?";
	
	public static final String  ST1_BO_UPDATE_GAME_INV_INVOICE_STATUS = "update st_se_game_inv_status set agent_invoice_id = ? where game_id = ? and pack_nbr = ? ";

	public static final String ST2_AGENT_CASH = "select sact.retailer_org_id,satm.transaction_date,sact.amount,sog.name from st_lms_agent_transaction_master satm , st_lms_agent_cash_transaction sact , st_lms_organization_master sog where satm.transaction_id  = sact.transaction_id and sact.retailer_org_id = sog.organization_id and satm.transaction_id = ?";
	public static final String ST2_AGENT_OLA_COMMISSION = "select sact.retailer_org_id retailer_org_id,satm.transaction_date,sact.ret_claim_comm amount,sog.name from st_lms_agent_transaction_master satm , st_ola_agt_comm sact , st_lms_organization_master sog where satm.transaction_id  = sact.transaction_id and sact.retailer_org_id = sog.organization_id and satm.transaction_id = ?";
	public static final String ST2_AGENT_CHQ = "select sasc.retailer_org_id,satm.transaction_date,sasc.cheque_amt,sog.name from st_lms_agent_transaction_master satm , st_lms_agent_sale_chq sasc , st_lms_organization_master sog where satm.transaction_id  = sasc.transaction_id and sasc.retailer_org_id = sog.organization_id and satm.transaction_id = ?";

	public static final String ST2_AGENT_CHQ_BOUNCE = "select sasc.retailer_org_id,satm.transaction_date,sasc.cheque_amt,sog.name from st_lms_agent_transaction_master satm , st_lms_agent_sale_chq sasc , st_lms_organization_master sog where satm.transaction_id  = sasc.transaction_id and sasc.retailer_org_id = sog.organization_id and satm.transaction_id = ?";

	public static final String ST2_AGENT_CURRENT_BAL = " select account_type, current_balance from st_lms_agent_current_balance where agent_org_id = ?";

	public static final String ST2_AGENT_CURRENT_BAL_AGENT = " select current_balance from st_lms_agent_current_balance  where account_type = ";

	// AGENT LEDGER
	// SELECT QUERIES
	public static final String ST2_AGENT_DATE = "select date from st_lms_agent_ledger where agent_org_id ";

	public static final String ST2_AGENT_DG_PWT_PLR = "select sadpp.pwt_amt, sadpp.tax_amt, sadpp.net_amt,satm.transaction_date,satm.transaction_type , spm.first_name from st_dg_agt_direct_plr_pwt sadpp, st_lms_agent_transaction_master satm ,st_lms_player_master spm where satm.transaction_id  = sadpp.transaction_id and spm.player_id = sadpp.player_id and satm.transaction_id = ?";

	public static final String ST2_AGENT_DRW_GM_PWT_PAY = "select sdgpw.retailer_org_id as retailer_org_id,satm.transaction_date,sdgpw.pwt_amt as pwt_amount,sdgpw.comm_amt as comm_amount,sog.name from st_lms_agent_transaction_master satm , st_dg_agt_pwt sdgpw , st_lms_organization_master sog where satm.transaction_id  = sdgpw.transaction_id and sdgpw.retailer_org_id = sog.organization_id and satm.transaction_id = ? group by sdgpw.transaction_id";
	public static final String ST2_AGENT_OLA_GM_WITHDRAWAL_PAY = "select sdgpw.retailer_org_id as retailer_org_id,satm.transaction_date,sdgpw.withdrawl_amt as pwt_amount,(sdgpw.net_amt-sdgpw.withdrawl_amt) as comm_amount,sog.name from st_lms_agent_transaction_master satm , st_ola_agt_withdrawl sdgpw , st_lms_organization_master sog where satm.transaction_id  = sdgpw.transaction_id and sdgpw.retailer_org_id = sog.organization_id and satm.transaction_id = ? group by sdgpw.transaction_id";
	public static final String ST2_AGENT_DRW_GM_RFND = "select sart.retailer_org_id as retailer_org_id,satm.transaction_date,sart.net_amt,sog.name from st_lms_agent_transaction_master satm , st_dg_agt_sale_refund sart , st_lms_organization_master sog where satm.transaction_id  = sart.transaction_id and sart.retailer_org_id = sog.organization_id and satm.transaction_id = ?";

	public static final String ST2_AGENT_DRW_GM_SALE = "select sadg.retailer_org_id as retailer_org_id,satm.transaction_date,sadg.net_amt,sog.name from st_lms_agent_transaction_master satm , st_dg_agt_sale sadg , st_lms_organization_master sog where satm.transaction_id  = sadg.transaction_id and sadg.retailer_org_id = sog.organization_id and satm.transaction_id = ?";

	public static final String ST2_AGENT_PWT_PAY = "select spw.retailer_org_id,satm.transaction_date,sum(spw.pwt_amt) as pwt_amount,sum(spw.comm_amt) as comm_amount,sog.name from st_lms_agent_transaction_master satm , st_se_agent_pwt spw , st_lms_organization_master sog where satm.transaction_id  = spw.transaction_id and spw.retailer_org_id = sog.organization_id and satm.transaction_id = ? group by spw.transaction_id";

	public static final String ST2_AGENT_PWT_PLR = "select sadpp.pwt_amt, sadpp.tax_amt, sadpp.net_amt,satm.transaction_date,satm.transaction_type , spm.first_name from st_se_agt_direct_player_pwt sadpp, st_lms_agent_transaction_master satm ,st_lms_player_master spm where satm.transaction_id  = sadpp.transaction_id and spm.player_id = sadpp.player_id and satm.transaction_id = ?";

	public static final String ST2_AGENT_SALE = "select sart.retailer_org_id,satm.transaction_date,sart.net_amt,sog.name from st_lms_agent_transaction_master satm , st_se_agent_retailer_transaction sart , st_lms_organization_master sog where satm.transaction_id  = sart.transaction_id and sart.retailer_org_id = sog.organization_id and satm.transaction_id = ?";

	public static final String ST2_AGENT_SALE_RET = "select sart.retailer_org_id,satm.transaction_date,sart.net_amt,sog.name from st_lms_agent_transaction_master satm , st_se_agent_retailer_transaction sart , st_lms_organization_master sog where satm.transaction_id  = sart.transaction_id and sart.retailer_org_id = sog.organization_id and satm.transaction_id = ?";

	public static final String ST2_AGENT_TRANSACTION = "select transaction_id ,transaction_type from st_lms_agent_transaction_master where transaction_date > ";

	public static final String ST2_BO_CASH = "select sbct.agent_org_id,sbtm.transaction_date,sbct.amount, sog.name from st_lms_bo_cash_transaction sbct , st_lms_bo_transaction_master sbtm ,st_lms_organization_master sog where sbtm.transaction_id  = sbct.transaction_id and sbct.agent_org_id = sog.organization_id and sbtm.transaction_id = ? ";

	public static final String ST2_BO_OLA_COMMISSION = "select sbct.agent_org_id agent_org_id,sbtm.transaction_date,sbct.agt_net_claim_comm amount, sog.name from st_ola_bo_comm sbct , st_lms_bo_transaction_master sbtm ,st_lms_organization_master sog where sbtm.transaction_id  = sbct.transaction_id and sbct.agent_org_id = sog.organization_id and sbtm.transaction_id = ?";

	public static final String ST2_BO_BANK_DEPOSIT = "select sbct.agent_org_id,sbtm.transaction_date,sbct.amount, sog.name from st_lms_bo_bank_deposit_transaction sbct , st_lms_bo_transaction_master sbtm ,st_lms_organization_master sog where sbtm.transaction_id  = sbct.transaction_id and sbct.agent_org_id = sog.organization_id and sbtm.transaction_id = ?";

	public static final String ST2_BO_CHQ = "select sbsc.agent_org_id,sbtm.transaction_date,sbsc.cheque_amt , sog.name from st_lms_bo_sale_chq sbsc , st_lms_bo_transaction_master sbtm ,st_lms_organization_master sog where sbtm.transaction_id  = sbsc.transaction_id and sbsc.agent_org_id = sog.organization_id and sbtm.transaction_id =  ?";
	public static final String ST2_BO_CHQ_BOUNCE = "select sbsc.agent_org_id,sbtm.transaction_date,sbsc.cheque_amt , sog.name from st_lms_bo_sale_chq sbsc , st_lms_bo_transaction_master sbtm ,st_lms_organization_master sog where sbtm.transaction_id  = sbsc.transaction_id and sbsc.agent_org_id = sog.organization_id and sbtm.transaction_id = ? ";
	public static final String ST2_BO_CURRENT_BAL = " select account_type, current_balance from st_lms_bo_current_balance ";
	public static final String ST2_BO_CURRENT_BAL_AGENT = " select current_balance from st_lms_bo_current_balance  where account_type = 'AGENT_ACC' and agent_org_id = ?";
	// Gaurav's
	// Queries----------------------------------------------------------------------
	// BO LEDGER QUERIES
	// SELECT QUERIES
	public static final String ST2_BO_DATE = "select transaction_date from st_lms_bo_ledger order by transaction_date desc";
	public static final String ST2_BO_DG_PWT_PLR = "select sdpp.pwt_amt, sdpp.tax_amt, sdpp.net_amt,sbtm.transaction_date,sbtm.transaction_type , spm.first_name from st_dg_bo_direct_plr_pwt sdpp, st_lms_bo_transaction_master sbtm ,st_lms_player_master spm where sbtm.transaction_id  = sdpp.transaction_id and spm.player_id = sdpp.player_id and sbtm.transaction_id = ?";
	public static final String ST2_BO_DRAW_GM_PWT_PAY = "select sdgbp.agent_org_id as agent_org_id,sbtm.transaction_date,sum(sdgbp.pwt_amt) as pwt_amount,sum(sdgbp.comm_amt) as comm_amount, sog.name  from st_dg_bo_pwt sdgbp , st_lms_bo_transaction_master sbtm ,st_lms_organization_master sog where sbtm.transaction_id  = sdgbp.transaction_id and sdgbp.agent_org_id = sog.organization_id and sbtm.transaction_id = ?  group by sdgbp.transaction_id";
	public static final String ST2_BO_DRW_GAME_SALE = "select sbdgm.agent_org_id as agent_org_id ,sbtm.transaction_date,sbdgm.net_amt , sog.name from st_dg_bo_sale sbdgm , st_lms_bo_transaction_master sbtm ,st_lms_organization_master sog where sbdgm.transaction_id  = sbtm.transaction_id and sbdgm.agent_org_id = sog.organization_id and sbdgm.transaction_id = ?";
	public static final String ST2_BO_COMM_SERV_SALE = "select sbcsm.agent_org_id as agent_org_id ,sbtm.transaction_date,sbcsm.net_amt , sog.name from st_cs_bo_sale sbcsm , st_lms_bo_transaction_master sbtm ,st_lms_organization_master sog where sbcsm.transaction_id  = sbtm.transaction_id and sbcsm.agent_org_id = sog.organization_id and sbcsm.transaction_id = ?";
	public static final String ST2_BO_OLA_DEPOSIT = "select sbolam.agent_org_id as agent_org_id ,sbtm.transaction_date,sbolam.net_amt ,sog.name from st_ola_bo_deposit sbolam,st_lms_bo_transaction_master sbtm ,st_lms_organization_master sog where sbolam.transaction_id = sbtm.transaction_id and sbolam.agent_org_id = sog.organization_id and sbolam.transaction_id = ?";
	public static final String ST2_BO_DRW_GM_RFND = "select sbdgm.agent_org_id as agent_org_id,sbtm.transaction_date,sbdgm.net_amt , sog.name from st_dg_bo_sale_refund sbdgm , st_lms_bo_transaction_master sbtm ,st_lms_organization_master sog where sbdgm.transaction_id  = sbtm.transaction_id and sbdgm.agent_org_id = sog.organization_id and sbdgm.transaction_id = ?";
	public static final String ST2_BO_COMM_SERV_RFND = "select sbcsm.agent_org_id as agent_org_id,sbtm.transaction_date,sbcsm.net_amt , sog.name from st_cs_bo_refund sbcsm , st_lms_bo_transaction_master sbtm ,st_lms_organization_master sog where sbcsm.transaction_id  = sbtm.transaction_id and sbcsm.agent_org_id = sog.organization_id and sbcsm.transaction_id = ?";
	public static final String ST2_BO_OLA_RFND = "select sbolam.agent_org_id as agent_org_id,sbtm.transaction_date,sbolam.net_amt , sog.name from st_ola_bo_deposit_refund sbolam , st_lms_bo_transaction_master sbtm ,st_lms_organization_master sog where sbolam.transaction_id  = sbtm.transaction_id and sbolam.agent_org_id = sog.organization_id and sbolam.transaction_id = ?";
	public static final String ST2_BO_GOVT_COMM = "select sbtm.transaction_date,sbgt.amount  from st_lms_bo_govt_transaction sbgt , st_lms_bo_transaction_master sbtm where sbtm.transaction_id  = sbgt.transaction_id  and sbtm.transaction_id= ? ";

	public static final String ST2_BO_PWT_PAY = "select sbp.agent_org_id,sbtm.transaction_date,sum(sbp.pwt_amt) as pwt_amount,sum(sbp.comm_amt) as comm_amount, sog.name  from st_se_bo_pwt sbp , st_lms_bo_transaction_master sbtm ,st_lms_organization_master sog where sbtm.transaction_id  = sbp.transaction_id and sbp.agent_org_id = sog.organization_id and sbtm.transaction_id = ?  group by sbp.transaction_id";
	public static final String ST2_BO_OLA_WITHDRAWL = "select solabp.agent_org_id as agent_org_id,sbtm.transaction_date,sum(solabp.withdrawl_amt) as pwt_amount,sum((net_amt-solabp.withdrawl_amt)) as comm_amount, sog.name from st_ola_bo_withdrawl solabp , st_lms_bo_transaction_master sbtm ,st_lms_organization_master sog where sbtm.transaction_id  = solabp.transaction_id and solabp.agent_org_id = sog.organization_id and sbtm.transaction_id = ? group by solabp.transaction_id";

	public static final String ST2_BO_PWT_PLR = "select sdpp.pwt_amt, sdpp.tax_amt, sdpp.net_amt,sbtm.transaction_date,sbtm.transaction_type , spm.first_name from st_se_direct_player_pwt sdpp, st_lms_bo_transaction_master sbtm ,st_lms_player_master spm where sbtm.transaction_id  = sdpp.transaction_id and spm.player_id = sdpp.player_id and sbtm.transaction_id = ? ";

	public static final String ST2_BO_SALE = "select sbat.agent_org_id,sbtm.transaction_date,sbat.net_amt , sog.name from st_se_bo_agent_transaction sbat , st_lms_bo_transaction_master sbtm ,st_lms_organization_master sog where sbtm.transaction_id  = sbat.transaction_id and sbat.agent_org_id = sog.organization_id and sbtm.transaction_id = ?";

	public static final String ST2_BO_SALE_RET = "select sbat.agent_org_id,sbtm.transaction_date,sbat.net_amt , sog.name from st_se_bo_agent_transaction sbat , st_lms_bo_transaction_master sbtm ,st_lms_organization_master sog where sbtm.transaction_id  = sbat.transaction_id and sbat.agent_org_id = sog.organization_id and sbtm.transaction_id = ?";

	public static final String ST2_BO_TDS = "select sbtm.transaction_date,sbgt.amount  from st_lms_bo_govt_transaction sbgt , st_lms_bo_transaction_master sbtm where sbtm.transaction_id  = sbgt.transaction_id  and sbtm.transaction_id= ? ";

	public static final String ST2_BO_TRANSACTION = "select transaction_id ,transaction_type,transaction_date from st_lms_bo_transaction_master where transaction_date > ? and transaction_date <= ?  order by transaction_date";

	public static final String ST2_BO_UNCLM_PWT = "select sbtm.transaction_date,sbgt.amount  from st_lms_bo_govt_transaction sbgt , st_lms_bo_transaction_master sbtm where sbtm.transaction_id  = sbgt.transaction_id  and sbtm.transaction_id= ? ";

	public static final String ST2_INSERT_AGENT_CURRENT_BAL = "insert into st_lms_bo_current_balance (account_type,current_balance,agent_org_id) values (?,?,?)";

	// INSERT QUERIES
	public static final String ST2_INSERT_AGENT_LEDGER = "insert into st_lms_agent_ledger (agent_org_id,transaction_type, transaction_id,transaction_date,amount,account_type,balance,transaction_with,receipt_id) values (?,?,?,?,?,?,?,?,?)";

	// INSERT QUERIES
	public static final String ST2_INSERT_BO_LEDGER = "insert into st_lms_bo_ledger (transaction_type, transaction_id,transaction_date,amount,account_type,balance,transaction_with,receipt_id) values (?,?,?,?,?,?,?,?)";

	// UPDATE QUERIES
	public static final String ST2_UPDATE_AGENT_CURRENT_BAL = "update st_lms_agent_current_balance set current_balance = ? where account_type = ? and agent_org_id = ?";

	public static final String ST2_UPDATE_CURRENT_BAL = "update st_lms_bo_current_balance set current_balance = ? where account_type = ? ";

	// UPDATE QUERIES
	public static final String ST2_UPDATE_CURRENT_BAL_AGENT = "update st_lms_bo_current_balance set current_balance = ? where account_type = ? and agent_org_id = ? ";

	public static final String ST3_AGENT_ORG_SEARCH = "select a.organization_id,a.organization_type,a.name,a.pwt_scrap,d.name 'parent_name',a.organization_status,a.addr_line1,a.city,b.name 'state',c.name 'country' from st_lms_organization_master a,st_lms_organization_master d,st_lms_state_master b,st_lms_country_master c where a.state_code=b.state_code and a.parent_id=d.organization_id and a.country_code=c.country_code and a.organization_type='AGENT' ";

	public static final String ST3_CLOSE_EXPIRE_GAMES = "update st_se_game_master set game_status='CLOSE'";

	public static final String ST3_CREATE_TASK = "insert into st_lms_bo_tasks (amount, month, transaction_type, status, approve_date, start_date, end_date,service_code)";

	public static final String ST3_CREATE_TASK_AGT = "insert into st_lms_agent_tasks (amount,month,transaction_type,status,approve_date,agent_org_id,service_code)";

	public static final String ST3_CREATE_TASK_UNCLM = "insert into st_lms_bo_tasks(amount,game_id,transaction_type,status,approve_date)";

	public static final String ST3_EXT_PWT_END_DATE = "update st_se_game_master  set pwt_end_date=";

	public static final String ST3_EXT_SALE_END_DATE = "update st_se_game_master set game_status='OPEN', sale_end_date =";

	public static final String ST3_EXT_SALE_PWT_DATE = "update st_se_game_master set game_status='OPEN', pwt_end_date =";

	public static final String ST3_EXTEND_PWT_END_DATE = "update st_se_game_master set game_status='open', pwt_end_date=";

	public static final String ST3_GAME_PRIZES_LEFT = " select pwt_amt,count(pwt_amt) 'No of Prizes Remaining',game_id,status from st_pwt_inv group by pwt_amt,game_id,status having status = 'SUB_GOV' and game_id = ? ";

  //public static final String ST3_GET_CONTACT_DETAILS = "select * from st_lms_user_contact_details";
	public static final String ST3_GET_CONTACT_DETAILS = "select user_id,first_name,last_name,id_type,id_nbr,email_id,ifnull(phone_nbr,0) phone_nbr,ifnull(mobile_nbr,0) mobile_nbr from st_lms_user_contact_details";
	
	public static final String ST3_GET_COUNTRY = "select * from st_lms_country_master where status='active'";

	public static final String ST3_GET_COUNTRY_CODE = "select country_code from st_lms_country_master ";

	public static final String ST3_GET_COUNTRY_STATE_CODE = "select cont.country_code,stat.state_code,stat.no_agt_registered,stat.no_ret_registered from st_lms_country_master cont,st_lms_state_master stat";
	
	public static final String ST3_GET_STATE_CITY_CODE = "select cm.city_code city_code, sm.state_code state_code from st_lms_city_master cm, st_lms_state_master sm";

	public static final String ST3_GET_COUNTRY_STATE_DETAILS = "select cont.name as country,cont.country_code,stat.name as state from st_lms_country_master cont,st_lms_state_master stat where stat.country_code=cont.country_code and";

	public static final String ST3_GET_FORGOT_PASS = "select user_id,user_name,secret_ques,secret_ans from st_lms_user_master";
	public static final String ST3_GET_GAME_DATAILS = "select game_id,game_nbr, game_name, sale_end_date  from st_se_game_master where game_status=";
	public static final String ST3_GET_IWGAME_DATAILS = "select game_id,game_nbr, game_name, sale_end_date  from st_iw_game_master where game_status=";
	public static final String ST3_GET_GAME_DATES = "select game_id,start_date,sale_end_date,pwt_end_date from st_se_game_master where game_name=";
	public static final String ST3_GET_IWGAME_DATES = "select game_id,start_date,sale_end_date,pwt_end_date from st_iw_game_master where game_name=";
	public static final String ST3_GET_GAME_DETAILS = "select game_nbr,start_date,sale_end_date,pwt_end_date,nbr_of_tickets,nbr_of_books,nbr_of_tickets_per_book,nbr_of_books_per_pack,agent_sale_comm_rate,agent_pwt_comm_rate,retailer_sale_comm_rate,retailer_pwt_comm_rate from st_se_game_master";
	public static final String ST3_GET_GAME_DETAILS1 = "select * from st_se_game_master";
	public static final String ST3_GET_IWGAME_DETAILS1 = "select * from st_iw_game_master";
	public static final String ST3_GET_GAME_ID = "select game_id from st_se_game_master where game_name=";

	public static final String ST3_GET_GAME_RANKS = "select * from st_se_rank_master where game_id=";

	public static final String ST3_GET_LATEST_MONTH_TDS_DG = "select month  from st_lms_bo_tasks where transaction_type='TDS' and service_code='DG' order by month desc";

	public static final String ST3_GET_LATEST_MONTH_TDS_SE = "select month  from st_lms_bo_tasks where transaction_type='TDS' and service_code='SE' order by month desc";

	public static final String ST3_GET_LATEST_MONTH_VAT_AGT_DG = "select month  from st_lms_agent_tasks where transaction_type='VAT' and agent_org_id=? and service_code='DG' order by month desc";

	public static final String ST3_GET_LATEST_MONTH_VAT_AGT_SE = "select month  from st_lms_agent_tasks where transaction_type='VAT' and agent_org_id=? and service_code='SE' order by month desc";

	public static final String ST3_GET_LATEST_MONTH_VAT_DG = "select month  from st_lms_bo_tasks where transaction_type='VAT' and service_code='DG' order by month desc";
	public static final String ST3_GET_LATEST_MONTH_VAT_SE = "select month  from st_lms_bo_tasks where transaction_type='VAT' and service_code='SE' order by month desc";
	public static final String ST3_GET_LATEST_MONTH_VAT_IW = "select month  from st_lms_bo_tasks where transaction_type='VAT' and service_code='IW' order by month desc";
	public static final String ST3_GET_ORG_DETAILS = "select organization_id,organization_type from st_lms_organization_master";
	public static final String ST3_GET_ORG_ID = "select organization_id from st_lms_organization_master";
	public static final String ST3_GET_ORG_NAME = "select name,current_credit_amt,available_credit from st_lms_organization_master";
	public static final String ST3_GET_ORGANIZATION_DETAILS = "select * from st_lms_organization_master";
	public static final String ST3_GET_ORGANIZATION_DETAILS_WITH_SEC_DPST_DETAILS = "SELECT * FROM st_lms_organization_master om INNER JOIN st_lms_organization_security_levy_master slm ON om.organization_id=slm.organization_id";

	public static final String ST3_GET_ORGANIZATION_ID = "select organization_id,user_name from st_lms_user_master";
	public static final String ST3_GET_PASSWORD = "select password, user_id from st_lms_user_master where user_name =?";
	public static final String ST3_GET_PASSWORD_HISTORY = "select password from st_lms_password_history where user_id = ? and type = 0 order by date_changed desc limit 3 ";

	public static final String ST3_GET_PASSWORD_TYPE = "select auto_password from st_lms_user_master ";

	public static final String ST3_GET_PWT_MRP = "select SUM(pwt_amt) from st_pwt_inv";

	public static final String ST3_GET_REMAINING_BOOKS_BO = "select COUNT(book_nbr)  from st_se_game_inv_status";

	public static final String ST3_GET_ROLE_ID = "select role_id from st_lms_role_master rm where rm.is_master='Y' and rm.tier_id = (select tier_id from st_lms_tier_master where tier_code=";

	public static final String ST3_GET_ROLE_NAME = "select role_name from st_lms_role_master ";

	public static final String ST3_GET_SALE_CLOSE = "select game_id from st_se_game_master where  game_status='OPEN' and CURRENT_DATE > sale_end_date ";

	public static final String ST3_GET_SALE_HOLD_GAME = "select * from st_se_game_master where game_status='SALE_HOLD' ";

	// public static final String Update_User_Master = "update
	// st_lms_user_master set ";

	public static final String ST3_GET_STATE_CODE = "select state_code from st_lms_state_master ";

	public static final String ST3_GET_UNAPPROVED_TDS_DG = "select * from st_lms_bo_tasks where transaction_type='TDS' and status='UNAPPROVED' and service_code='DG'";

	public static final String ST3_GET_UNAPPROVED_TDS_SE = "select * from st_lms_bo_tasks where transaction_type='TDS' and status='UNAPPROVED' and service_code='SE'";

	public static final String ST3_GET_UNAPPROVED_VAT_AGT_DG = "select * from st_lms_agent_tasks where transaction_type='VAT' and status='UNAPPROVED' and service_code='DG' and agent_org_id=";

	public static final String ST3_GET_UNAPPROVED_VAT_AGT_SE = "select * from st_lms_agent_tasks where transaction_type='VAT' and status='UNAPPROVED' and service_code='SE' and agent_org_id=";

	public static final String ST3_GET_UNAPPROVED_VAT_DG = "select * from st_lms_bo_tasks where transaction_type='VAT' and status='UNAPPROVED' AND service_code='DG'";

	public static final String ST3_GET_UNAPPROVED_VAT_SE = "select * from st_lms_bo_tasks where transaction_type='VAT' and status='UNAPPROVED' AND service_code='SE'";
	
	public static final String ST3_GET_UNAPPROVED_VAT_IW = "select * from st_lms_bo_tasks where transaction_type='VAT' and status='UNAPPROVED' AND service_code='IW'";

	public static final String ST3_GET_USER = "select user_name from st_lms_user_master ";

	public static final String ST3_GET_USER_DETAILS = "select user_id,organization_id,role_id,user_name,password,status,organization_type,organization_id from st_lms_user_master ";

	public static final String ST3_GET_USER_ID = "select user_id from st_lms_user_master";

	public static final String ST3_GET_USER_ORG_DETAILS = "select org.name,org.organization_id,org.organization_type,org.addr_line1,org.addr_line2,org.city,org.country_code,org.state_code,org.pin_code,org.credit_limit,org.organization_status,user.user_name,user.status from st_lms_organization_master org,st_lms_user_master user where user.organization_id=org.organization_id and ";

	public static final String ST3_GOVT_COMM_APPROVED_DG = "select * from st_lms_bo_tasks task,st_dg_game_master game where task.transaction_type='GOVT_COMM' and task.service_code='DG' and task.status='APPROVED'and task.game_id=game.game_id";

	public static final String ST3_GOVT_COMM_APPROVED_SE = "select * from st_lms_bo_tasks task,st_se_game_master game where task.transaction_type='GOVT_COMM' and  task.service_code='SE' and task.status='APPROVED'and task.game_id=game.game_id";

	public static final String ST3_GOVT_COMM_RATE = "select govt_comm_rate,govt_comm_type,fixed_amt from st_se_game_master";

	public static final String ST3_HOLD_SALE = "update st_se_game_master set game_status='SALE_HOLD' ";

	//public static final String ST3_INSERT_AGENT_DETAIL = "INSERT into st_lms_user_master(organization_id,role_id,organization_type,auto_password,user_name,password,status,secret_ques,secret_ans,registration_date,isrolehead,parent_user_id, register_by_id) values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
	
	public static final String ST3_INSERT_AGENT_DETAIL = "INSERT into st_lms_user_master(organization_id,role_id,organization_type,auto_password,user_name,password,status,secret_ques,secret_ans,registration_date,isrolehead,parent_user_id, register_by_id, tp_user_id) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	
	// public static final String ST3_INSERT_CONTACTS_DETAILS="INSERT into
	// st_lms_user_contact_details(user_id,first_name,last_name,email_id,phone_nbr)
	// VALUES(?,?,?,?,?)";
	public static final String ST3_INSERT_CONTACTS_DETAILS = "INSERT into st_lms_user_contact_details(user_id,first_name,last_name,email_id,phone_nbr, id_type, id_nbr,mobile_nbr) VALUES(?,?,?,?,?,?,?,?)";

	public static final String ST3_INSERT_GAME_DATES = "insert into st_se_game_master(start_date,sale_end_date,pwt_end_date)";

	public static final String ST3_INSERT_GOVT_TDS_TRANSACTION = "insert into st_lms_bo_govt_transaction(transaction_id,service_code,amount, month, transaction_type, start_date, end_date) ";

	public static final String ST3_INSERT_GOVT_TDS_TRANSACTION_AGT = "insert into st_lms_agent_govt_transaction(transaction_id,amount,month,transaction_type,service_code) ";

	public static final String ST3_INSERT_GOVT_TRANSACTION = "insert into st_lms_bo_govt_transaction(transaction_id,amount,game_id,transaction_type) ";

	// --------------changed by arun--------------------
	// public static final String ST3_INSERT_LOGIN_DATE ="update
	// st_lms_user_master set last_login_date= CURRENT_DATE ";
	public static final String ST3_INSERT_LOGIN_DATE = "update st_lms_user_master set  last_login_date=?";

	public static final String ST3_INSERT_NEW_GAME_DATES = "update st_se_game_master set start_date=";

	public static final String ST3_INSERT_NEW_IWGAME_DATES = "update st_iw_game_master set start_date=";

	//public static final String ST3_INSERT_ORG_HISTORY = "INSERT into st_lms_organization_master_history(user_id, organization_id,addr_line1,addr_line2,city,pin_code,credit_limit,security_deposit,reason,comment,organization_status,date_changed, pwt_scrap, recon_report_type )";

	public static final String ST3_INSERT_ORG_HISTORY = "INSERT into st_lms_organization_master_history(user_id, organization_id,addr_line1,addr_line2,division_code,area_code,city,pin_code,credit_limit,security_deposit,reason,comment,organization_status,date_changed, pwt_scrap, recon_report_type )";

	public static final String ST3_INSERT_ORG_MASTER = "INSERT into st_lms_organization_master(organization_type,name,addr_line1,addr_line2,city,pin_code,state_code,country_code,current_credit_amt,credit_limit,security_deposit,organization_status,available_credit)";

	public static final String ST3_INSERT_PASSWORD_DETAILS = "INSERT into st_lms_password_history (user_id, password, date_changed, type)VALUES(?, ?, CURRENT_TIMESTAMP, '1')";

	public static final String INSERT_USER_TIME_LIMIT_MAPPING = "INSERT INTO st_lms_user_ip_time_mapping (user_id, allowed_ip, monday_start_time, monday_end_time, tuesday_start_time, tuesday_end_time, wednesday_start_time, wednesday_end_time, thursday_start_time, thursday_end_time, friday_start_time, friday_end_time, saturday_start_time, saturday_end_time, sunday_start_time, sunday_end_time, STATUS)VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";

	public static final String ST3_INSERT_PASSWORD_HISTORY = "INSERT into st_lms_password_history(user_id, password, date_changed, type) VALUES (?, ?, ?, ?)";

	// updated by arun when winning prize remaining process changing
	public static final String ST3_INSERT_RANK_DETAILS = "insert into st_se_rank_master (game_id, rank_nbr, prize_amt, prize_level, total_no_of_prize, no_of_prize_claim, no_of_prize_cancel)value(?, ?, ?, ?, ?, ?, ?)";

	public static final String ST3_INSERT_IWRANK_DETAILS = "insert into st_iw_rank_master (game_id, rank_nbr, prize_amt, prize_level, total_no_of_prize, no_of_prize_claim, no_of_prize_cancel)value(?, ?, ?, ?, ?, ?, ?)";

	public static final String ST3_INSERT_RETAILER_ORG_ = "INSERT into st_lms_organization_master(organization_type,name,org_code,parent_id," +
			"addr_line1,addr_line2,city,division_code,area_code,state_code,country_code,pin_code,available_credit,credit_limit,security_deposit,organization_status,current_credit_amt" +
			",vat_registration_nbr, pwt_scrap, recon_report_type) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	public static final String ST3_INSERT_SUPPLIER_DETAILS = "insert into st_se_supplier_master (name,addr_line1,addr_line2,city,state_code,country_code,pin_code)";

	public static final String ST3_INSERT_TICKET_FORMAT = "insert into st_se_game_ticket_nbr_format(game_id,pack_nbr_digits,book_nbr_digits,ticket_nbr_digits,game_nbr_digits,game_rank_digits,game_virn_digits) values";

	public static final String ST3_INSERT_IWTICKET_FORMAT = "insert into st_iw_game_ticket_nbr_format(game_id,pack_nbr_digits,book_nbr_digits,ticket_nbr_digits,game_nbr_digits,game_rank_digits,game_virn_digits) values";

	public static final String ST3_INSERT_TRANSACTION_MASTER = "insert into st_lms_bo_transaction_master(party_type,transaction_date,transaction_type) ";

	public static final String ST3_INSERT_TRANSACTION_MASTER_AGT = "insert into st_lms_agent_transaction_master(user_id ,transaction_type,transaction_date) ";

	public static final String ST3_LAST_MONTH_TDS = "select month from st_lms_bo_tasks where transaction_type='TDS'";

	public static final String ST3_LAST_MONTH_TRANSACTION_DG = "select transaction_date,tax_amt from st_dg_bo_direct_plr_pwt";

	public static final String ST3_LAST_MONTH_TRANSACTION_SE = "select transaction_date,tax_amt from st_se_direct_player_pwt";

	public static final String ST3_MRP_AMT = "select SUM(mrp_amt) from st_se_bo_agent_transaction";

	// Yogesh's Queries--------------------------------------------------
	public static final String ST3_SEARCH_GAME = "select game_id,game_name,game_nbr,ticket_price,start_date,sale_end_date,pwt_end_date from st_se_game_master";

	public static final String ST3_SEARCH_USER = "select user_id,user_name,status,registration_date from st_lms_user_master";

	public static final String ST3_SELECT_CLOSED_GAMES = "select * from st_se_game_master where game_status='CLOSE'";

	public static final String ST3_SELECT_COUNTRY = "select name from st_lms_country_master ";

	public static final String ST3_SELECT_EXPIRE_GAMES = "select game_id from st_se_game_master where (game_status='SALE_CLOSE' or game_status='SALE_HOLD' or game_status='OPEN') and CURRENT_DATE > pwt_end_date";

	public static final String ST3_SELECT_STATE = "select name from st_lms_state_master ";

	public static final String ST3_SELECT_SUPPLIER_DETAILS = "select name from st_se_supplier_master ";

	public static final String ST3_SUBMIT_TDS_APPROVED = "Update st_lms_bo_tasks set status='DONE'";

	public static final String ST3_SUBMIT_UNCLM_PWT_APPROVED = "Update st_lms_bo_tasks set status='DONE'";

	public static final String ST3_SUBMIT_UNCLM_PWT_APPROVED_AGT = "Update st_lms_agent_tasks set status='DONE'";

	public static final String ST3_TDS_APPROVED_DG = "select * from st_lms_bo_tasks where transaction_type='TDS' and status='APPROVED' and service_code='DG'";

	public static final String ST3_TDS_APPROVED_SE = "select * from st_lms_bo_tasks where transaction_type='TDS' and status='APPROVED' and service_code='SE'";

	public static final String ST3_UNCLM_PWT_APPROVED = "select * from st_lms_bo_tasks task,st_se_game_master game where task.transaction_type='UNCLM_PWT' and task.status='APPROVED' and task.game_id=game.game_id";

	public static final String ST3_UPDATE_GAME_STATUS = "update st_se_game_master set game_status='SALE_CLOSE',";

	public static final String ST3_UPDATE_PASS_HISTORY = "INSERT into st_lms_password_history(user_id,password,date_changed,type)VALUES(?,?,CURRENT_DATE,?)";

	public static final String ST3_UPDATE_PWT_INV = "update st_pwt_inv set status='SUB_GOV'";

	public static final String ST3_UPDATE_QUERY_MANAGER = "update st_se_game_master set game_status='TERMINATE'";

	public static final String ST3_UPDATE_SALE_CLOSE_STATUS = "update st_se_game_master set game_status='SALE_CLOSE'";

	public static final String ST3_UPDATE_SALE_PWT_STATUS = "update st_se_game_master set game_status='OPEN',";

	public static final String ST3_UPDATE_SALE_STATUS = "update st_se_game_master set game_status='OPEN',";

	public static final String ST3_UPDATE_TASK_STATUS = "Update st_lms_bo_tasks set status='APPROVED', approve_date = ? ";

	public static final String ST3_UPDATE_TASK_STATUS_AGT = "Update st_lms_agent_tasks set status='APPROVED', approve_date = ? ";

	public static final String ST3_UPDATE_USER_MASTER = "update st_lms_user_master set password= ? , auto_password=? where user_name = ?";

	public static final String ST3_UPDATE_USER_MASTER1 = "update st_lms_user_master set  password = ? ,auto_password = 0 where user_id = ?";

	public static final String ST3_UPDET_ORG_DETAILS = "update st_lms_organization_master set ";

	public static final String ST3_UPDET_USER_DETAILS = "update st_lms_user_contact_details set";

	public static final String ST3_UPDET_USER_PASS = "update st_lms_user_master set ";

	public static final String ST3_UPDET_USER_STATUS = "update st_lms_user_master set";

	public static final String ST3_VAT_APPROVED_AGT_DG = "select * from st_lms_agent_tasks where transaction_type='VAT' and status='APPROVED' and service_code='DG' and agent_org_id=";

	public static final String ST3_VAT_APPROVED_AGT_SE = "select * from st_lms_agent_tasks where transaction_type='VAT' and status='APPROVED' and service_code='SE' and agent_org_id=";
	
	public static final String ST3_VAT_APPROVED_AGT_IW = "select * from st_lms_agent_tasks where transaction_type='VAT' and status='APPROVED' and service_code='IW' and agent_org_id=";

	public static final String ST3_VAT_APPROVED_DG = "select * from st_lms_bo_tasks where transaction_type='VAT' and status='APPROVED' and service_code='DG'";

	public static final String ST3_VAT_APPROVED_SE = "select * from st_lms_bo_tasks where transaction_type='VAT' and status='APPROVED' and service_code='SE'";
	
	public static final String ST3_VAT_APPROVED_IW = "select * from st_lms_bo_tasks where transaction_type='VAT' and status='APPROVED' and service_code='IW'";

	// 4. Vishal's Queries-------------------------
	public static final String ST4_GET_ACTIVE_GAME_DETAILS = "select * from st_se_game_master where game_status in ('OPEN','SALE_HOLD','SALE_CLOSE')";

	public static final String ST4_GET_BOOK_NBR_OF_PACK_NBR = "select book_nbr from st_se_game_inv_status where pack_nbr = ? ";

	public static final String ST4_GET_BOOK_OF_TICKETS_OF_PWT = "select * from st_se_pwt_tickets_inv_? where game_id = ? and book_nbr = ?";

	public static final String ST4_GET_BOOK_OF_TICKETS_OF_PWT_FOR_PACK = "select * from st_se_pwt_tickets_inv_? where game_id = ? and book_nbr in(select book_nbr from st_se_game_inv_status where game_id = ? and pack_nbr = ?)";

	public static final String ST4_GET_BOOK_OF_TICKETS_OF_PWT_FOR_PACK_TEMP = "select * from st_se_tmp_pwt_tickets_inv where game_id = ? and book_nbr in(select book_nbr from st_se_game_inv_status where game_id = ? and pack_nbr = ?)";

	public static final String ST4_GET_BOOK_OF_TICKETS_OF_PWT_TEMP = "select * from st_se_tmp_pwt_tickets_inv where game_id = ? and book_nbr = ?";

	public static final String ST4_GET_BOOK_STATUS_DETAILS_AND_BOOK_NBRS_USING_PACK_NBR = "select book_nbr,current_owner_id,book_status from st_se_game_inv_status where game_id= ? and pack_nbr= ? ";

	public static final String ST4_GET_BOOK_VALIDITY_DETAILS = "select count(book_nbr) 'total' from st_se_game_inv_status where game_id = ? and book_nbr = ?";

	public static final String ST4_GET_CURRENT_OWNER_DETAILS_AND_BOOK_NBRS_USING_PACK_NBR = "select book_nbr,current_owner_id,book_status from st_se_game_inv_status where game_id= ? and pack_nbr= ? ";

	public static final String ST4_GET_CURRENT_OWNER_DETAILS_USING_GAME_BOOK_NBR = "select current_owner_id,current_owner,book_status from st_se_game_inv_status where game_id= ? and book_nbr= ? ";

	public static final String ST4_GET_CURRENT_OWNER_DETAILS_USING_GAME_ID = "select current_owner_id,nbr_of_tickets_per_book from st_se_game_inv_status where game_id = ?";

	public static final String ST4_GET_CURRENT_OWNER_TYPE_USING_ORGANIZATION_ID = "select organization_type from st_lms_organization_master where organization_id= ? ";

	public static final String ST4_GET_GAME_DETAILS_USING_GAME_ID = "select *  from st_se_game_master where game_id = ?";

	public static final String ST4_GET_GAME_DETAILS_USING_GAME_NAME = "select * from st_se_game_master where game_name = ?";

	public static final String ST4_GET_GAME_TICKET_DETAILS_USING_GAME_ID = "select a.nbr_of_tickets_per_book, b.ticket_nbr_digits, b.pack_nbr_digits, b.book_nbr_digits, b.game_nbr_digits, game_virn_digits, game_rank_digits  from st_se_game_master a,st_se_game_ticket_nbr_format b where a.game_id=? and a.game_id=b.game_id";

	public static final String ST4_GET_ORGANIZATION_ID_USING_ORGANIZATION_NAME = "select organization_id from st_lms_organization_master where name = ?";

	public static final String ST4_GET_PACK_NBR_OF_BOOK_NBR = "select pack_nbr from st_se_game_inv_status where book_nbr = ? ";

	public static final String ST4_GET_PACK_VALIDITY_DETAILS = "select count(pack_nbr) 'total' from st_se_game_inv_status where game_id = ? and pack_nbr = ?";

	public static final String ST4_GET_PWT_TICKETS_DETAILS = "select * from st_pwt_tickets_inv where ticket_nbr = ?";

	public static final String ST4_GET_USER_ID_USING_USER_NAME = "select user_id from st_lms_user_master where user_name = ?";

	public static final String ST4_INSERT_AGENT_RECEIPTS = "insert into st_lms_agent_receipts (receipt_type, agent_org_id, party_id) values(?,?,?)";

	public static final String ST4_INSERT_AGENT_RECEIPTS_TRN_MAPPING = "insert into st_lms_agent_receipts_trn_mapping (id,transaction_id) values(?,?)";

	public static final String ST4_INSERT_AGENT_RETAILER_TRANSACTION = "insert into st_se_agent_retailer_transaction (transaction_id, game_id, agent_user_id, retailer_org_id, mrp_amt, comm_amt, net_amt, transaction_type, nbr_of_books,vat_amt,taxable_sale,agent_org_id) values(?,?,?,?,?,?,?,?,?,?,?,?)";

	public static final String ST4_INSERT_AGENT_TRANSACTION_MASTER = "insert into st_lms_agent_transaction_master (user_id, user_org_id, transaction_type, transaction_date) values(?,?,?,?)";

	public static final String ST4_INSERT_BO_AGENT_TRANSACTION = "insert into st_se_bo_agent_transaction (transaction_id,nbr_of_books,game_id,agent_org_id, mrp_amt,comm_amt,net_amt,transaction_type,vat_amt,taxable_sale,gov_comm_amt) values(?,?,?,?,?,?,?,?,?,?,?)";

	public static final String ST4_INSERT_BO_RECEIPTS = "insert into st_lms_bo_receipts (receipt_type, agent_org_id) values(?,?)";

	public static final String ST4_INSERT_BO_RECEIPTS_TRN_MAPPING = "insert into st_lms_bo_receipts_trn_mapping (id,transaction_id) values(?,?)";

	public static final String ST4_INSERT_BO_TRANSACTION_MASTER = "insert into st_lms_bo_transaction_master (party_type, party_id, transaction_date,transaction_type) values(?,?,?,?)";

	public static final String ST4_INSERT_GAME_INV_DETAIL = "insert into st_se_game_inv_detail (transaction_id,game_id,pack_nbr, book_nbr,current_owner,current_owner_id,transaction_date,transaction_at,transacrion_sale_comm_rate,transaction_gov_comm_rate,transaction_purchase_comm_rate) select ?,?,?,?,?,?,?,?,transacrion_sale_comm_rate,transaction_gov_comm_rate,transaction_purchase_comm_rate from st_se_game_inv_detail where book_nbr=? and transaction_at=? order by transaction_date desc limit 1";

	public static final String ST4_INSERT_GAME_INV_DETAIL_FOR_AGENT = "insert into st_se_game_inv_detail (transaction_id,game_id,pack_nbr, book_nbr,current_owner,current_owner_id,transaction_date,transaction_at,transacrion_sale_comm_rate) values(?,?,?,?,?,?,?,?,?)";

	public static final String ST4_INSERT_PWT_INV_DETAILS = "insert into st_pwt_inv (virn_code,game_id,pwt_amt,prize_level,status) values(?,?,?,?,?)";

	// new---------
	public static final String ST4_INSERT_TICKETS_NO = "insert into st_pwt_tickets_inv (ticket_nbr, game_id, book_nbr, status) values(?,?,?,?)";

	public static final String ST4_PWT_PLAYER_DETAIL = "update st_se_pwt_inv_? set status = 'CLAIM_PLR' where game_id = ? and virn_code = ? ";

	public static final String ST4_SEARCH_AGENT_ORGANIZATION = "select * from st_lms_organization_master where organization_type = 'AGENT'";

	public static final String ST4_SEARCH_GAME = "select game_id,game_name,game_nbr,ticket_price,start_date,sale_end_date,pwt_end_date from st_se_game_master";

	public static final String ST4_SEARCH_ORGANIZATION = "select * from st_lms_organization_master";

	public static final String ST4_SEARCH_RETAILER_ORGANIZATION = "select * from st_lms_organization_master where organization_type = 'RETAILER' and parent_id = ?";

	public static final String ST4_SEARCH_USER = "select user_id,user_name,status,registration_date from st_lms_user_master";

	public static final String ST4_UPDATE_GAME_INV_STATUS_FOR_BOOK = "update st_se_game_inv_status set current_owner='BO',book_status=?, current_owner_id=?,agent_invoice_id=? where game_id= ? and book_nbr= ? ";

	public static final String ST4_UPDATE_GAME_INV_STATUS_FOR_BOOK_FOR_AGENT = "update st_se_game_inv_status set current_owner='AGENT',book_status=?,current_owner_id = ?,ret_invoice_id=? where game_id= ? and book_nbr= ? ";

	public static final String ST4_UPDATE_GAME_INV_STATUS_FOR_PACK = "update st_se_game_inv_status set current_owner='BO',current_owner_id=? where game_id= ? and pack_nbr= ? ";

	public static final String ST4_UPDATE_GAME_INV_STATUS_FOR_PACK_FOR_AGENT = "update st_se_game_inv_status set current_owner='AGENT',current_owner_id = ? where game_id= ? and pack_nbr= ? ";

	public static final String ST4_UPDATE_PWT_TICKET_INV_STATUS_TO_AGT = "update st_se_pwt_tickets_inv_? set status ='CLAIM_AGT',verify_by_user=?,verify_by_org=? where game_id= ? and ticket_nbr= ? ";

	public static final String ST4_UPDATE_PWT_TICKET_INV_STATUS_TO_PLR = "update st_se_pwt_tickets_inv_? set status ='CLAIM_PLR',verify_by_user=?,verify_by_org=? where game_id= ? and ticket_nbr= ? ";

	public static final String ST4_UPDATE_PWT_TICKET_INV_STATUS_TO_RETURN = "update st_se_pwt_tickets_inv_? set status ='RETURN',verify_by_user=?,verify_by_org=? where game_id= ? and ticket_nbr= ? ";

	public static final String ST5_AGENT_CASH_TRANSACTION = "INSERT INTO st_lms_agent_cash_transaction (transaction_id,agent_user_id,retailer_org_id,amount,agent_org_id) VALUES (?,?,?,?,?)";

	public static final String ST5_AGENT_CHQ = "select count(*) 'count' from st_lms_bo_sale_chq where drawee_bank=? and cheque_nbr=?";

	public static final String ST5_AGENT_DETAILS = "select c.name,c.addr_line1,c.city,d.name 'country_name',e.name 'state_name',c.pin_code,c.credit_limit,c.current_credit_amt,c.extended_credit_limit,c.available_credit from st_lms_organization_master c,st_lms_country_master d, st_lms_state_master e where  c.country_code=d.country_code and c.state_code=e.state_code and c.name='";

	public static final String ST5_AGENT_RECEIPTS = "INSERT INTO st_lms_agent_receipts (receipt_type,agent_org_id,retailer_org_id) VALUES (?,?,?)";

	public static final String ST5_AGENT_RECEIPTS_GEN_MAPPING = "INSERT INTO st_lms_agent_receipts_gen_mapping (id,generated_id,receipt_type,agt_org_id) VALUES (?,?,?,?)";

	public static final String ST5_AGENT_RECEIPTS_TRN_MAPPING = "INSERT INTO st_lms_agent_receipts_trn_mapping (id,transaction_id) VALUES (?,?)";

	public static final String ST5_AGENT_REQUEST_LIST = "select a.order_id,a.order_date,c.name from st_se_agent_order a,st_lms_organization_master c where a.order_status='REQUESTED' and a.retailer_org_id=c.organization_id and  a.agent_org_id=? ";

	public static final String ST5_AGENT_SALE_CHEQUE = "update st_lms_agent_sale_chq set transaction_type=? where cheque_nbr=? and drawee_bank=?";

	public static final String ST5_AGENT_SALE_CHEQUE1 = "INSERT INTO st_lms_agent_sale_chq(transaction_id,agent_user_id,retailer_org_id,cheque_nbr,cheque_date,issuing_party_name,drawee_bank,cheque_amt,transaction_type,agent_org_id) VALUES (?,?,?,?,?,?,?,?,?,?)";

	public static final String ST5_BO_CASH_TRANSACTION = "INSERT INTO st_lms_bo_cash_transaction (transaction_id,agent_org_id,amount) VALUES (?,?,?)";

	// added by amit for bank deposit

	public static final String ST5_BO_BANK_DEPOSIT_TRANSACTION = "INSERT INTO st_lms_bo_bank_deposit_transaction (transaction_id,agent_org_id,amount,bank_name, bank_branch_name, bank_receipt_no, bank_deposit_date) VALUES (?,?,?,?,?,?,?)";

	public static final String ST5_AGENT_BANK_DEPOSIT_TRANSACTION = "INSERT INTO st_lms_agent_bank_deposit_transaction (transaction_id,retailer_org_id,amount,bank_name, bank_branch_name, bank_receipt_no, bank_deposit_date) VALUES (?,?,?,?,?,?,?)";

	public static final String ST5_BO_ORDERED_GAME = "select game_id,nbr_of_books_req,nbr_of_books_appr from st_se_bo_ordered_games";

	public static final String ST5_BO_RECEIPTS = "INSERT INTO st_lms_bo_receipts (receipt_type,agent_org_id) VALUES (?,?)";
	public static final String ST5_BO_RECEIPTS_GEN_MAPPING = "INSERT INTO st_bo_receipt_gen_mapping (id,generated_id,receipt_type) VALUES (?,?,?)";

	public static final String ST5_BO_RECEIPTS_TRN_MAPPING = "INSERT INTO st_lms_bo_receipts_trn_mapping (receipt_id,transaction_id) VALUES (?,?)";

	// //QUERIES REGARDING THE ORDER REQUEST FOR ST
	public static final String ST5_BO_REQUEST_LIST = "select a.order_id,a.order_date,c.name from st_se_bo_order a,st_lms_organization_master c where a.order_status='REQUESTED' and a.agent_org_id=c.organization_id ";

	public static final String ST5_BO_SALE_CHEQUE = "update st_lms_bo_sale_chq set transaction_type=? where cheque_nbr=? and drawee_bank=?";

	// Hanuman's Queries----------------------------------------------
	// ///some new query are added
	public static final String ST5_BO_UPDATE_BO_ORDER = "update st_se_bo_order set order_status=? where order_id=?";

	public static final String ST5_BO_UPDATE_BO_ORDER_INVOICES = "update st_se_bo_order_invoices set order_status=? where order_id=?";

	public static final String ST5_COUNTRY_CODE = "select country_code from st_lms_country_master";

	public static final String ST5_DIRECT_PLAYER_PWT_ENTRY = "insert into st_se_direct_player_pwt(task_id,ticket_nbr,virn_code,transaction_id,game_id,player_id,pwt_amt,tax_amt,net_amt,transaction_date,payment_type,cheque_nbr,cheque_date,drawee_bank,issuing_party_name)VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	public static final String ST5_GAME_INV_STATUS = "select sum(book_nbr) from st_se_game_inv_status where current_owner='BO'";

	public static final String ST5_GAME_MASTER_ENTRY = "INSERT into st_se_game_master(game_nbr,game_name,ticket_price,nbr_of_tickets_per_book,nbr_of_books_per_pack,agent_sale_comm_rate,agent_pwt_comm_rate,retailer_sale_comm_rate,retailer_pwt_comm_rate,govt_comm_rate,game_status,govt_comm_type,fixed_amt,add_inv_status,prize_payout_ratio,vat_amt,tickets_in_scheme, invoice_scheme_id)VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	public static final String ST5_IWGAME_MASTER_ENTRY = "INSERT into st_iw_game_master(game_nbr,game_name,ticket_price,nbr_of_tickets_per_book,nbr_of_books_per_pack,agent_sale_comm_rate,agent_pwt_comm_rate,retailer_sale_comm_rate,retailer_pwt_comm_rate,govt_comm_rate,game_status,govt_comm_type,fixed_amt,add_inv_status,prize_payout_ratio,vat_amt,tickets_in_scheme)VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	public static final String ST5_GET_PASSWORD_TYPE = "select auto_password from st_lms_user_master ";

	public static final String ST5_GET_ROLE_NAME = "select role_name from st_lms_role_master ";

	public static final String ST5_GET_USER_DETAILS = "select user_id,organization_id,role_id,user_name,password,status,organization_type from st_lms_user_master ";

	public static final String ST5_INSERT_AGENT_MASTER_FOR_CASH = "INSERT INTO st_lms_agent_transaction_master (user_id,user_org_id,transaction_type,transaction_date) VALUES (?,?,?,?)";

	public static final String ST5_INSERT_LOGIN_DATE = "update st_lms_user_master set  last_login_date= CURRENT_DATE ";

	public static final String ST5_INSERT_ST_BO_MASTER_FOR_CASH = "INSERT INTO st_lms_bo_transaction_master (party_type,party_id,transaction_date,transaction_type) VALUES (?,?,?,?)";

	public static final String ST5_INSERT_st_lms_bo_sale_chq = "INSERT INTO st_lms_bo_sale_chq(transaction_id,agent_org_id,cheque_nbr,cheque_date,issuing_party_name,drawee_bank,cheque_amt,transaction_type) VALUES (?,?,?,?,?,?,?,?)";

	public static final String ST5_NO_OF_BOOKS_APPR = "select no_of_books_appr from st_se_bo_ordered_games a, st_se_bo_order b ";

	public static final String ST5_ORDER_REQUEST1 = "select b.game_id 'game_id',b.game_nbr 'game_number',b.game_name 'game_name',b.ticket_price 'ticket_price',b.nbr_of_tickets_per_book 'tickets_per_book', a.nbr_of_books_req 'nbr_of_books_req',COUNT(d.book_nbr) 'total' from st_se_bo_ordered_games a,st_se_game_master b,st_se_bo_order c,st_se_game_inv_status d where a.game_id=b.game_id and a.game_id=d.game_id and c.order_status='REQUESTED' and d.current_owner='BO' and a.order_id=c.order_id and a.order_id=";

	// QUERY FOR RequestApproveAction class
	public static final String ST5_ORDER_REQUEST2 = "select  SUM(a.nbr_of_books_appr) 'no_of_books_appr' from st_se_bo_ordered_games a,st_se_bo_order c where c.order_status='APPROVED' and  a.order_id=c.order_id and a.game_id=";

	/*
	 * queries added after first Review
	 */

	public static final String ST5_ORDER_REQUEST3 = "UPDATE st_se_bo_ordered_games SET nbr_of_books_appr = ? WHERE order_id =? and game_id=?";

	public static final String ST5_ORDER_REQUEST4 = "update st_se_bo_order set order_status='APPROVED' WHERE order_id=?";
	public static final String ST5_ORDER_REQUEST5 = "update st_se_bo_order set order_status='DENIED' WHERE order_id=?";
	// /This query for Org Search
	public static final String ST5_ORG_SEARCH = "select a.organization_id, a.extends_credit_limit_upto, a.organization_type,a.name,a.parent_id,d.name 'parent_name',a.organization_status,a.addr_line1,a.city,a.available_credit,a.credit_limit,a.security_deposit,a.extended_credit_limit,a.pwt_scrap,a.claimable_bal,a.unclaimable_bal,b.name 'state',c.name 'country' from st_lms_organization_master a,st_lms_organization_master d,st_lms_state_master b,st_lms_country_master c where a.state_code=b.state_code and  a.country_code=c.country_code and a.parent_id = d.organization_id and a.organization_type in('AGENT','RETAILER') ";
	public static final String ST5_PLAYER_DETAILS = "select a.player_id,a.first_name,a.last_name,a.email_id,a.phone_nbr,a.addr_line1,a.addr_line2,a.city,d.name 'country',e.name 'state',a.pin_code, a.bank_acc_nbr, a.location_city, a.bank_branch, a.bank_name from st_lms_player_master a,st_lms_country_master d, st_lms_state_master e where  a.country_code=d.country_code and a.state_code=e.state_code and a.first_name=? and a.last_name=? and a.photo_id_type=? and a.photo_id_nbr=?";

	public static final String ST5_PLAYER_ENTRY = "insert into st_lms_player_master(first_name,last_name,email_id,phone_nbr,addr_line1,addr_line2,city,state_code,country_code,pin_code,photo_id_type,photo_id_nbr)VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
	public static final String ST5_PLR_PWT_COMM_RATE = "select govt_pwt_comm_rate from st_se_game_master where game_id=";
	public static final String ST5_PWT_PLAYER_TEP_TRANC = "insert into st_se_direct_player_pwt_temp_receipt (player_id,transaction_date,virn_code,game_id,pwt_amt,status,ticket_nbr) values(?,?,?,?,?,?,?)";
	public static final String ST5_PWT_PLAYER_TEP_TRANC_DETAIL = "select a.pwt_receipt_id,a.player_id ,a.game_id,a.transaction_date,a.virn_code,a.pwt_amt,a.tax_amt,a.net_amt,a.status,a.ticket_nbr,b.first_name,b.last_name,c.game_name,c.game_nbr from st_se_direct_player_pwt_temp_receipt a ,st_lms_player_master b,st_se_game_master c where a.game_id=c.game_id and a.player_id=b.player_id ";
	public static final String ST5_RET_ORDER_REQUEST5 = "update st_se_agent_order set order_status='DENIED' WHERE order_id=?";

	public static final String ST5_RET_ORG_SEARCH = "select a.organization_id,a.organization_type,a.name,a.organization_status,a.addr_line1,a.city,b.name 'state',c.name 'country' from st_lms_organization_master a,st_lms_state_master b,st_lms_country_master c where a.state_code=b.state_code and  a.country_code=c.country_code and a.organization_type in('RETAILER') ";

	// ----------by arun--------
	public static final String ST5_RET_ORG_SEARCH_FOR_AGENT = "select a.organization_id, a.extends_credit_limit_upto ,a.organization_type,a.name,d.name 'parent_name',a.organization_status,a.addr_line1,a.city,a.available_credit,a.credit_limit,a.security_deposit,a.extended_credit_limit,a.pwt_scrap,a.claimable_bal,b.name 'state',c.name 'country' from st_lms_organization_master a,st_lms_organization_master d,st_lms_state_master b,st_lms_country_master c where a.state_code=b.state_code and  a.country_code=c.country_code and a.parent_id=d.organization_id and a.organization_type='RETAILER' and a.parent_id=? ";
	public static final String ST5_RETAILER_CHQ = "select count(*) 'count' from st_lms_agent_sale_chq where drawee_bank=? and cheque_nbr=?";

	public static final String ST5_RETAILER_DETAILS = "select c.name,c.addr_line1,c.city,d.name 'country_name',e.name 'state_name',c.pin_code,c.credit_limit,c.current_credit_amt,c.available_credit,c.extended_credit_limit from st_lms_organization_master c,st_lms_country_master d, st_lms_state_master e where  c.country_code=d.country_code and c.state_code=e.state_code and c.name='";

	public static final String ST5_RETAILER_ORDER_REQUEST1 = "select b.game_id 'game_id',b.game_nbr 'game_number',b.game_name 'game_name',b.ticket_price 'ticket_price',b.nbr_of_tickets_per_book 'tickets_per_book', a.nbr_of_books_req 'nbr_of_books_req',COUNT(d.book_nbr) 'total' from st_se_agent_ordered_games a,st_se_game_master b,st_se_agent_order c,st_se_game_inv_status d where a.game_id=b.game_id and a.game_id=d.game_id and c.order_status='REQUESTED' and d.current_owner='AGENT' and a.order_id=c.order_id and a.order_id=";

	public static final String ST5_RETAILER_ORDER_REQUEST2 = "select  SUM(a.nbr_of_books_appr) 'no_of_books_appr' from st_se_agent_ordered_games a,st_se_agent_order c where c.order_status='APPROVED' and  a.order_id=c.order_id and a.game_id=";

	public static final String ST5_RETAILER_ORDER_REQUEST3 = "UPDATE st_se_agent_ordered_games SET nbr_of_books_appr = ? WHERE order_id =? and game_id=?";
	public static final String ST5_RETAILER_ORDER_REQUEST4 = "update st_se_agent_order set order_status='APPROVED' WHERE order_id=?";

	public static final String ST5_SEARCH_CHEQUE = "select a.transaction_id,a.cheque_nbr,a.cheque_date,a.issuing_party_name,a.drawee_bank,a.cheque_amt,b.name from st_lms_bo_sale_chq a, st_lms_organization_master b where a.agent_org_id=b.organization_id and a.transaction_type='CHEQUE' ";

	public static final String ST5_SEARCH_CHEQUE_RETAILER = "select a.transaction_id,a.cheque_nbr,a.cheque_date,a.issuing_party_name,a.drawee_bank,a.cheque_amt,b.name from st_lms_agent_sale_chq a, st_lms_organization_master b where a.retailer_org_id=b.organization_id and a.transaction_type='CHEQUE'";
	public static final String ST5_SEARCH_GAME = "select * from st_se_game_master";
	public static final String ST5_SEARCH_ROLE = "select role_id from st_lms_role_master";
	public static final String ST5_SEARCH_SUPPLIER = "select supplier_id,name,addr_line1,addr_line2,city,state_code,country_code,pin_code from st_se_supplier_master";
	// public static final String ST5_SEARCH_USER = "select c.name,d.name
	// 'parent',a.user_id,b.role_name,a.user_name,a.status,a.registration_date
	// from st_lms_user_master a,st_lms_role_master b,st_lms_organization_master
	// c,st_lms_organization_master d where b.role_name in ('AGT_MAS','RET_MAS')
	// and a.role_id=b.role_id and c.organization_id=a.organization_id and
	// d.organization_id=c.parent_id";
	public static final String ST5_SEARCH_USER = "select c.name,d.name 'parent',a.user_id,b.role_name,a.user_name,a.status,a.registration_date from st_lms_user_master a,st_lms_role_master b,st_lms_organization_master c,st_lms_organization_master d where b.tier_id in (select tier_id from st_lms_tier_master where (tier_code = 'AGENT' or tier_code = 'RETAILER')) and a.role_id=b.role_id and c.organization_id=a.organization_id and d.organization_id=c.parent_id";
	public static final String ST5_SEARCH_USER_DETAIL = "select a.user_id,a.user_name,a.status,a.registration_date,a.organization_type,c.role_name,b.name from st_lms_user_master a,st_lms_organization_master b,st_lms_role_master c where a.organization_id=b.organization_id and a.role_id=c.role_id and a.user_id=";

	// retailers query

	public static final String ST5_SEARCH_USER_FOR_CASH = "select user_name,user_id from st_lms_user_master where organization_id=(select organization_id from st_lms_organization_master where name ='SATYAM' and organization_type='AGT_MAS') and role_id=(select role_id from st_lms_role_master where role_name='AGT_MAS')";
	public static final String ST5_SELECT_ORG_TYPE_FOR_AGENT = "select organization_id,organization_type,name from st_lms_organization_master where organization_type='RETAILER'";
	//public static final String ST5_SELECT_ORG_TYPE_FOR_BO = "select organization_id,organization_type,name from st_lms_organization_master where organization_type='AGENT' and organization_status!='TERMINATE'";
	public static final String ST5_SELECT_ORG_TYPE_FOR_BO = "select organization_id,organization_type,name from st_lms_organization_master where organization_type='AGENT'";
	public static final String ST5_SELECT_TRANSACTIONId = "select transaction_id from st_lms_bo_transaction_master";
	public static final String ST5_STATE_CODE = "select state_code from st_lms_state_master";
	public static final String ST5_SUPPLIER_BO_TRANS = "INSERT into st_se_supplier_bo_transaction(transaction_id,game_id,supplier_id,nbr_of_books,mrp_amt)VALUES(?,?,?,?,?)";
	public static final String ST5_TOTAL_DISPATCH = "select nbr_of_books_dlvrd from st_se_bo_ordered_games where order_id=";

	// insert entry into st transaction master and bo transaction master tables

	// public static final String INSERT_LMS_TRANSACTION_MASTER="INSERT INTO
	// st_lms_transaction_master (user_type) VALUES (?)";

	public static final String ST5_UPDATE_ST_BO_TEMP = "update st_se_direct_player_pwt_temp_receipt set status=? , tax_amt=?, net_amt=? where pwt_receipt_id=?";

	public static final String ST55_SELECT_COUNTRY = "select name from st_lms_country_master ";

	// //player
	public static final String ST55_SELECT_STATE = "select name from st_lms_state_master ";

	// queries for receipt related table

	public static final String ST6_AGENT_DEBIT_NOTE = "select sbdn.retailer_org_id,sbtm.transaction_date,sbdn.amount , sog.name from st_lms_agent_debit_note sbdn , st_lms_agent_transaction_master sbtm ,st_lms_organization_master sog where sbtm.transaction_id  = sbdn.transaction_id and sbdn.retailer_org_id = sog.organization_id and sbtm.transaction_id = ? ";

	// Added for agent credit note @ amit

	public static final String ST6_AGENT_CREDIT_NOTE = "select sbdn.retailer_org_id,sbtm.transaction_date,sbdn.amount , sog.name from st_lms_agent_credit_note sbdn , st_lms_agent_transaction_master sbtm ,st_lms_organization_master sog where sbtm.transaction_id  = sbdn.transaction_id and sbdn.retailer_org_id = sog.organization_id and sbtm.transaction_id = ? ";

	public static final String ST6_AGENT_VAT_PAY = "select satm.transaction_date,sagt.amount  from st_lms_agent_govt_transaction sagt, st_lms_agent_transaction_master satm where satm.transaction_id  = sagt.transaction_id  and satm.transaction_id= ? ";

	public static final String ST6_AGT_CURR_BAL = " select current_balance from st_lms_agent_current_balance  where account_type = ? and agent_org_id = ?";

	public static final String ST6_AGT_TRANSACTION = " select transaction_id ,transaction_type from st_lms_agent_transaction_master where transaction_date > ? and transaction_date < ? and user_id in (select user_id from st_lms_user_master where organization_id = ? )  order by transaction_date";

	public static final String ST6_BO_AGT_TRANSACTIONS = "select transaction_id ,transaction_type,transaction_date from st_lms_bo_transaction_master where transaction_date > ? and transaction_date < ? and party_id = ? and party_type = 'AGENT'  order by transaction_date";

	public static final String ST6_BO_CREDIT_NOTE = "select sbdn.agent_org_id,sbtm.transaction_date,sbdn.amount , sog.name from st_lms_bo_credit_note sbdn , st_lms_bo_transaction_master sbtm ,st_lms_organization_master sog where sbtm.transaction_id  = sbdn.transaction_id and sbdn.agent_org_id = sog.organization_id and sbtm.transaction_id = ? ";

	public static final String ST6_BO_DEBIT_NOTE = "select sbdn.agent_org_id,sbtm.transaction_date,sbdn.amount , sog.name from st_lms_bo_debit_note sbdn , st_lms_bo_transaction_master sbtm ,st_lms_organization_master sog where sbtm.transaction_id  = sbdn.transaction_id and sbdn.agent_org_id = sog.organization_id and sbtm.transaction_id = ? ";

	public static final String ST6_BO_VAT_PAY = "select sbtm.transaction_date,sbgt.amount  from st_lms_bo_govt_transaction sbgt , st_lms_bo_transaction_master sbtm where sbtm.transaction_id  = sbgt.transaction_id  and sbtm.transaction_id= ? ";

	public static final String ST6_GET_ADD = "select addr_line1,addr_line2,city,st_lms_state_master.name,st_lms_country_master.name from st_lms_organization_master,st_lms_state_master,st_lms_country_master where st_lms_organization_master.organization_id= ? and st_lms_organization_master.country_code=st_lms_country_master.country_code and  st_lms_organization_master.state_code=st_lms_state_master.state_code ";

	public static final String ST6_GET_ADD_AGT_RETWISE = "select transaction_type,receipt_id as transaction_id,account_type,balance,amount,transaction_date,transaction_with from st_lms_bo_ledger where account_type = ? and transaction_date >= ? and transaction_date <= ?";

	public static final String ST6_GET_ADD_BO_AGTWISE = "select transaction_type,receipt_id as transaction_id,account_type,balance,amount,transaction_date,transaction_with from st_lms_bo_ledger where account_type = ? and transaction_date >= ? and transaction_date <= ?";

	public static final String ST6_INSERT_AGT_CURR_BAL = " insert into st_lms_agent_current_balance (account_type,current_balance,agent_org_id) values ( ?,?,?)";

	public static final String GET_Agent_LATEST_CR_NOTE_NBR = "SELECT * from st_lms_agent_receipts where (receipt_type=? or receipt_type=?) and agent_org_id=? ORDER BY generated_id DESC LIMIT 1";

	// public static final String ST6_RETWISE_LEDGER_AGT="select
	// transaction_type,receipt_id as
	// transaction_id,account_type,balance,amount,transaction_date,transaction_with
	// from st_lms_agent_ledger where account_type = (select organization_id
	// from st_lms_organization_master where name = ?) and transaction_date >= ?
	// and transaction_date <= ? ";

	public static final String ST6_TRANSACTION_DATE = "select transaction_date from st_lms_agent_ledger where agent_org_id = ? order by transaction_date desc";

	// Ledger Display Queries Starts

	public static final String ST6_ACC_LEDGER_AGT = "select transaction_type,receipt_id as transaction_id,account_type,balance,amount,transaction_date,transaction_with from st_lms_agent_ledger where account_type = ? and agent_org_id= ? and transaction_date >= ? and transaction_date <= ?";

	public static final String ST6_ACC_LEDGER_BO = "select transaction_type,receipt_id as transaction_id,account_type,balance,amount,transaction_date,transaction_with from st_lms_bo_ledger where account_type = ? and transaction_date >= ? and transaction_date <= ?";

	public static final String ST6_RETWISE_LEDGER_AGT = "select transaction_type,receipt_id as transaction_id,account_type,balance,amount,transaction_date,transaction_with from st_lms_agent_ledger where account_type = (select organization_id from st_lms_organization_master where name = ?) and transaction_date >= ? and transaction_date < ?";

	public static final String ST6_RETWISE_LEDGER_AGT_RCPT = "select transaction_type,receipt_id as transaction_id,account_type,balance,sum(amount) amount,transaction_date,transaction_with from st_lms_agent_ledger where account_type = (select organization_id from st_lms_organization_master where name = ?) and transaction_date >= ? and transaction_date < ?  group by receipt_id order by transaction_date,receipt_id";

	public static final String ST6_JOURNAL_LEDGER_AGT = "select transaction_type,receipt_id as transaction_id,account_type,balance,amount,transaction_date,transaction_with from st_lms_agent_ledger where  transaction_date >= ? and transaction_date <= ? and agent_org_id= ?";

	public static final String ST6_JOURNAL_LEDGER_BO = "select transaction_type,receipt_id as transaction_id,account_type,balance,amount,transaction_date,transaction_with from st_lms_bo_ledger where  transaction_date >= ?  and transaction_date <= ?";

	public static final String ST6_AGTWISE_LEDGER_BO = "select transaction_type,receipt_id as transaction_id,account_type,balance,amount,transaction_date,transaction_with from st_lms_bo_ledger where account_type = (select organization_id from st_lms_organization_master where name = ? ) and transaction_date >= ? and transaction_date <= ?";

	public static final String ST6_AGTWISE_LEDGER_BO_RCPT = "select transaction_type,receipt_id as transaction_id,account_type,balance,sum(amount) amount,transaction_date,transaction_with from st_lms_bo_ledger where account_type = (select organization_id from st_lms_organization_master where name = ? ) and transaction_date >= ? and transaction_date <= ? group by receipt_id order by transaction_date,receipt_id";

	public static final String GET_AGENT_OLA_DATA_RETAILER_WISE = "select DEPOSIT.deposit-DEPREF.refund deposit,WITHDRAW.withdraw-WITHREF.refund withdraw ,NETPLR.plr_net_gaming plr_net_gaming from (select rettx.retailer_org_id retalier_org,ifnull(sum(withdrawl_amt),0.0) refund from st_ola_ret_withdrawl_refund ola  inner join (select transaction_id,retailer_org_id from st_lms_retailer_transaction_master where date(transaction_date)>=? and date(transaction_date)<?   and transaction_type='OLA_WITHDRAWL_REFUND' and retailer_org_id=?  ) rettx on ola.transaction_id=rettx.transaction_id) WITHREF, (select rettx.retailer_org_id retalier_org,ifnull(sum(withdrawl_amt),0.0) withdraw from st_ola_ret_withdrawl ola  inner join (select transaction_id,retailer_org_id from st_lms_retailer_transaction_master where date(transaction_date)>=? and date(transaction_date)<?     and transaction_type='OLA_WITHDRAWL' and retailer_org_id=?  ) rettx on ola.transaction_id=rettx.transaction_id)WITHDRAW,(select retailer_org_id retalier_org,ifnull(sum(deposit_amt),0.0) refund from st_ola_ret_deposit_refund ola  inner join (select transaction_id from st_lms_retailer_transaction_master where date(transaction_date)>=? and date(transaction_date)<?  and transaction_type='OLA_DEPOSIT_REFUND' and retailer_org_id=?  ) rettx on ola.transaction_id=rettx.transaction_id) DEPREF,(select retailer_org_id retalier_org,ifnull(sum(deposit_amt),0.0) deposit from st_ola_ret_deposit ola  inner join (select transaction_id from st_lms_retailer_transaction_master where date(transaction_date)>=? and date(transaction_date)<? and transaction_type='OLA_DEPOSIT' and retailer_org_id=?  ) rettx on ola.transaction_id=rettx.transaction_id)DEPOSIT,(select arc.plr_net_gaming plr_net_gaming from st_ola_agt_ret_commission arc inner join (select transaction_id ,user_org_id from st_lms_agent_transaction_master where transaction_type ='OLA_COMMISSION' and date(transaction_date)>=? and date(transaction_date)<? and user_org_id=? ) atm on atm.transaction_id=arc.transaction_id and arc.ret_org_id=? group by arc.ret_org_id union select distinct(null) plr_net_gaming from st_lms_organization_master) NETPLR limit 1";
	
	public static final String GET_AGENT_CS_DATA_RETAILER_WISE="select FINAL.retailer_org_id org_id, FINAL.sale sale,FINAL.refund refund ,ifnull(FINAL.sale-FINAL.refund,0) total from (select SALE.retailer_org_id,ifnull(sale,0) sale,ifnull(refund,0)refund  from (select a.retailer_org_id,ifnull(sum(net_amt),0) sale from st_cs_agt_sale  a inner join (select * from st_lms_agent_transaction_master where user_org_id=?  and date(transaction_date) >=? and date(transaction_date) <?  and transaction_type='CS_SALE') b on a.transaction_id=b.transaction_id and a.retailer_org_id=? group by a.retailer_org_id)SALE left join (select a.retailer_org_id,ifnull(sum(net_amt),0) refund from st_cs_agt_refund  a inner join (select * from st_lms_agent_transaction_master where user_org_id=?  and date(transaction_date) >=? and date(transaction_date) <? and transaction_type  in('CS_CANCEL_SERVER','CS_CANCEL_RET')) b on a.transaction_id=b.transaction_id and a.retailer_org_id=?  group by a.retailer_org_id) REFUND on SALE.retailer_org_id=REFUND.retailer_org_id) FINAL";
	// Ledger Display Queries Ends
	
	public static final String GENERATE_LMS_TRANSACTION = "INSERT INTO st_lms_transaction_master (user_type, service_code, interface) VALUES (?, ?, ?)";
}
