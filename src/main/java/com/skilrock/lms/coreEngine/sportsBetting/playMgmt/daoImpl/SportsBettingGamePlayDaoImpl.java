package com.skilrock.lms.coreEngine.sportsBetting.playMgmt.daoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.CommonMethods;
import com.skilrock.lms.common.utility.OrgCreditUpdation;
import com.skilrock.lms.coreEngine.sportsLottery.common.SLEErrors;
import com.skilrock.lms.coreEngine.sportsLottery.common.SLEException;
import com.skilrock.lms.rest.services.bean.TPTxRequestBean;
import com.skilrock.lms.web.drawGames.common.Util;

public class SportsBettingGamePlayDaoImpl {
	private static Logger logger = LoggerFactory.getLogger(SportsBettingGamePlayDaoImpl.class);

	/**
	 * 
	 * @param gamePlayBean
	 * @param userBean
	 * @param con
	 * @return
	 * @throws SLEException 
	 * @throws LMSException 
	 * @throws SQLException 
	 */

	public static synchronized long sportsBettingPurchaseTicketDaoImpl(TPTxRequestBean gamePlayBean,UserInfoBean userBean,Connection con) throws SLEException, LMSException, SQLException{
		logger.info("inside sportsBettingPurchaseTicketDaoImpl...");
			double retCommRate = 0.0;
			double agtCommRate = 0.0;
			double ticketMrp=gamePlayBean.getTxAmount();

				PreparedStatement pstmt = null;
				ResultSet rsTrns = null;
				try{
				if(gamePlayBean.getTxAmount() > 0) {
					pstmt = con.prepareStatement("select (available_credit-claimable_bal) as availbale_sale_bal from st_lms_organization_master where organization_id=?");
					pstmt.setInt(1, userBean.getUserOrgId());
					rsTrns = pstmt.executeQuery();
					if (rsTrns.next()) {
						if (!(rsTrns.getDouble("availbale_sale_bal") >= ticketMrp - ticketMrp * retCommRate * .01)) {
							logger.info(SLEErrors.RETAILER_BALANCE_INSUFFICIENT_ERROR_MESSAGE);
							throw new SLEException(SLEErrors.RETAILER_BALANCE_INSUFFICIENT_ERROR_CODE,  SLEErrors.RETAILER_BALANCE_INSUFFICIENT_ERROR_MESSAGE);
						}
					} else {
						throw new SLEException(SLEErrors.INVALID_USER_NAME_CODE, SLEErrors.INVALID_USER_NAME_MESSAGE);
					}
				}
				}finally{
					DBConnect.closeConnection(pstmt, rsTrns);
				}
				long transId = processTransactionForMasterAndGameTable(gamePlayBean, userBean, con,retCommRate, agtCommRate,  ticketMrp);
		return transId;
	}

	private static long processTransactionForMasterAndGameTable(TPTxRequestBean gamePlayBean, UserInfoBean userBean, Connection con,
		 double retCommRate,
		double agtCommRate,  double ticketMrp)
		throws SQLException, LMSException, SLEException {
		double govt_comm = 0.0;
		double vat = 0.0;
		double prize_payout_ratio = 0.0;
		boolean isValid;
		PreparedStatement insertPstmt = null;
		ResultSet insertRs = null;
		long transId = 0;
		try{
			insertPstmt = con.prepareStatement("INSERT INTO st_lms_transaction_master (user_type, service_code, interface) VALUES (?,?,?)",PreparedStatement.RETURN_GENERATED_KEYS);
			insertPstmt.setString(1, userBean.getUserType());
			insertPstmt.setString(2, gamePlayBean.getServiceCode());
			insertPstmt.setString(3, gamePlayBean.getInterfaceType());
			insertPstmt.executeUpdate();
			insertRs = insertPstmt.getGeneratedKeys();
				
			if (insertRs.next()) {
					transId = insertRs.getLong(1);
					
					double saleCommRate = 0.0;
					double goodCauseAmt=0.0;
	
					double vatAmount = CommonMethods.calculateDrawGameVatPlr(ticketMrp, saleCommRate, prize_payout_ratio, govt_comm, vat);
					double taxableSale = CommonMethods.calTaxableSale(ticketMrp, saleCommRate, prize_payout_ratio, govt_comm, vat);
	
					double retNet = CommonMethods.fmtToTwoDecimal(ticketMrp) - CommonMethods.fmtToTwoDecimal(ticketMrp * retCommRate * .01);
					double agtNet = CommonMethods.fmtToTwoDecimal(ticketMrp) - CommonMethods.fmtToTwoDecimal(ticketMrp * agtCommRate * .01);
					
					insertDataToRetailerTransactionMaster(gamePlayBean, userBean, con, transId,"SBS_SALE");
					
					
					insertPstmt = con.prepareStatement("insert into st_sbs_ret_sale(transaction_id,engine_tx_id,sports_id,retailer_org_id,ticket_nbr,mrp_amt,retailer_comm_amt,retailer_net_amt,agent_comm_amt,agent_net_amt,good_cause_amt,vat_amt,taxable_sale,player_mob_number,claim_status,transaction_date,is_cancel) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
					
					insertPstmt.setLong(1, transId);
					insertPstmt.setLong(2, gamePlayBean.getEngineTxId());
					insertPstmt.setInt(3, gamePlayBean.getGameId());
					insertPstmt.setInt(4, userBean.getUserOrgId());
					insertPstmt.setString(5, gamePlayBean.getTicketNumber());
					insertPstmt.setDouble(6, CommonMethods.fmtToTwoDecimal(ticketMrp));
					insertPstmt.setDouble(7,CommonMethods.fmtToTwoDecimal(ticketMrp * retCommRate * .01));
					insertPstmt.setDouble(8,CommonMethods.fmtToTwoDecimal(retNet));
					insertPstmt.setDouble(9,CommonMethods.fmtToTwoDecimal(ticketMrp * agtCommRate * .01));
					insertPstmt.setDouble(10,CommonMethods.fmtToTwoDecimal(agtNet));
					insertPstmt.setDouble(11,CommonMethods.fmtToTwoDecimal(goodCauseAmt));
					insertPstmt.setDouble(12,CommonMethods.fmtToTwoDecimal(vatAmount));
					insertPstmt.setDouble(13,CommonMethods.fmtToTwoDecimal(taxableSale));
					insertPstmt.setString(14, gamePlayBean.getPlrMobileNumber());
					insertPstmt.setString(15, "CLAIM_BAL");
					insertPstmt.setTimestamp(16, Util.getCurrentTimeStamp());
					insertPstmt.setString(17, "N");
 					insertPstmt.executeUpdate();
	
					//Now make payment update method only one
					isValid=OrgCreditUpdation.updateOrganizationBalWithValidate(retNet, "CLAIM_BAL", "CREDIT", userBean.getUserOrgId(), userBean.getParentOrgId(), "RETAILER", 0, con);
					if(!isValid){
						throw new SLEException(SLEErrors.RETAILER_BALANCE_INSUFFICIENT_ERROR_CODE,SLEErrors.RETAILER_BALANCE_INSUFFICIENT_ERROR_MESSAGE);
	
					}
					isValid=OrgCreditUpdation.updateOrganizationBalWithValidate(agtNet, "CLAIM_BAL", "CREDIT", userBean.getParentOrgId(),0, "AGENT", 0, con);
					if(!isValid){
						throw new SLEException(SLEErrors.AGENT_BALANCE_INSUFFICIENT_ERROR_CODE,SLEErrors.AGENT_BALANCE_INSUFFICIENT_ERROR_MESSAGE);
					}
				}else{
					throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE,SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
				}
			}finally{
			DBConnect.closeConnection(insertPstmt, insertRs);
		}
		return transId;
	}

	private static void insertDataToRetailerTransactionMaster(TPTxRequestBean gamePlayBean, UserInfoBean userBean,
			Connection con, long transId,String transactionType) throws SQLException {
		PreparedStatement insertPstmt;
		insertPstmt = con.prepareStatement("INSERT INTO st_lms_retailer_transaction_master (transaction_id,retailer_user_id,retailer_org_id,game_id,transaction_date,transaction_type) VALUES (?,?,?,?,?,?)");
		insertPstmt.setLong(1, transId);
		insertPstmt.setInt(2, userBean.getUserId()); 
		insertPstmt.setInt(3, userBean.getUserOrgId());
		//insertPstmt.setInt(4, gamePlayBean.getGameId());
		insertPstmt.setInt(4, gamePlayBean.getGameTypeId());
		insertPstmt.setTimestamp(5, Util.getCurrentTimeStamp());
		insertPstmt.setString(6, transactionType);
		insertPstmt.executeUpdate();
	}
}
