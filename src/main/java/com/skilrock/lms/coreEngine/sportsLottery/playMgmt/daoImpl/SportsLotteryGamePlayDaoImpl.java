package com.skilrock.lms.coreEngine.sportsLottery.playMgmt.daoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.Utility;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.CommonMethods;
import com.skilrock.lms.common.utility.OrgCreditUpdation;
import com.skilrock.lms.common.utility.ResponsibleGaming;
import com.skilrock.lms.coreEngine.sportsLottery.common.SLEErrors;
import com.skilrock.lms.coreEngine.sportsLottery.common.SLEException;
import com.skilrock.lms.dge.beans.GameMasterLMSBean;
import com.skilrock.lms.rest.services.bean.TPTxRequestBean;
import com.skilrock.lms.web.drawGames.common.Util;

public class SportsLotteryGamePlayDaoImpl {
	private static Logger logger = LoggerFactory.getLogger(SportsLotteryGamePlayDaoImpl.class);

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
/*	public static synchronized long sportsLotteryPurchaseTicketDaoImpl(SportsLotteryGamePlayBean gamePlayBean,UserInfoBean userBean,Connection con) throws SLEException{
		 PreparedStatement insertPstmt=null;
		 ResultSet insertRs=null;
		 long transId=0;
		 NumberFormat nf = NumberFormat.getInstance();
			nf.setMinimumFractionDigits(2);
			
		try{
			// insert in main transaction table
			int gameTypeId=gamePlayBean.getGameTypeId();
			insertPstmt = con.prepareStatement("INSERT INTO st_lms_transaction_master (user_type, service_code, interface) VALUES (?,?,?)",PreparedStatement.RETURN_GENERATED_KEYS);
			insertPstmt.setString(1, userBean.getUserType());
			insertPstmt.setString(2, gamePlayBean.getServiceCode());
			insertPstmt.setString(3, gamePlayBean.getInterfaceType());
			insertPstmt.executeUpdate();
			        
			insertRs = insertPstmt.getGeneratedKeys();
					if (insertRs.next()) {
						transId = insertRs.getLong(1);
						
						// insert into retailer transaction master
						insertPstmt = con
									.prepareStatement("INSERT INTO st_lms_retailer_transaction_master (transaction_id,retailer_user_id,retailer_org_id,game_id,transaction_date,transaction_type) VALUES (?,?,?,?,?,?)");
						insertPstmt.setLong(1, transId);
						insertPstmt.setInt(2, userBean.getUserId()); 
						insertPstmt.setInt(3, userBean.getUserOrgId());
						insertPstmt.setInt(4, gamePlayBean.getGameId());
						insertPstmt.setTimestamp(5, Util.getCurrentTimeStamp());
						insertPstmt.setString(6, "SE_SALE");
						insertPstmt.executeUpdate();
						
						double ticketMrp=gamePlayBean.getTotalPurchaseAmt();
						double retCommAmt=ticketMrp*SportsLotteryUtils.gameTypeInfoMap.get(gameTypeId).getRetailetSaleComm()*.01;
						double retNetAmt=ticketMrp-retCommAmt;
						
						double agtCommAmt=ticketMrp*SportsLotteryUtils.gameTypeInfoMap.get(gameTypeId).getAgentSaleComm()*.01;
						double agentNetAmt=ticketMrp-agtCommAmt;
						double goodCauseAmt=0.0;
						double vatAmt=0.0;
						double taxableSale=0.0;
						boolean isValid=false;
						insertPstmt = con
								.prepareStatement("insert into st_sle_ret_sale_?(transaction_id,game_id,game_type_id,retailer_org_id,ticket_nbr,mrp_amt,retailer_comm_amt,retailer_net_amt,agent_comm_amt,agent_net_amt,good_cause_amt,vat_amt,taxable_sale,player_mob_number,claim_status,transaction_date,is_cancel) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
						
						insertPstmt.setInt(1, gamePlayBean.getGameId());
						insertPstmt.setLong(2, transId);
						insertPstmt.setInt(3, gamePlayBean.getGameId());
						insertPstmt.setInt(4,gamePlayBean.getGameTypeId());
						insertPstmt.setInt(5, userBean.getUserOrgId());
						insertPstmt.setLong(6, 0);
						insertPstmt.setDouble(7,CommonMethods.fmtToTwoDecimal(ticketMrp));
						insertPstmt.setDouble(8,CommonMethods.fmtToTwoDecimal(retCommAmt));
						insertPstmt.setDouble(9,CommonMethods.fmtToTwoDecimal(retNetAmt));
						insertPstmt.setDouble(10,CommonMethods.fmtToTwoDecimal(agtCommAmt));
						insertPstmt.setDouble(11,CommonMethods.fmtToTwoDecimal(agentNetAmt));
						insertPstmt.setDouble(12,CommonMethods.fmtToTwoDecimal(goodCauseAmt));
						insertPstmt.setDouble(13,CommonMethods.fmtToTwoDecimal(vatAmt));
						insertPstmt.setDouble(14,CommonMethods.fmtToTwoDecimal(taxableSale));
						insertPstmt.setString(15, gamePlayBean.getPlrMobNo());
						insertPstmt.setString(16, "CLAIM_BAL");
						insertPstmt.setTimestamp(17, Util.getCurrentTimeStamp());
						insertPstmt.setString(18, "N");
						insertPstmt.executeUpdate();

						//Now make payment updte method only one
						isValid=OrgCreditUpdation.updateOrganizationBalWithValidate(retNetAmt, "CLAIM_BAL", "CREDIT", userBean
								.getUserOrgId(), userBean
								.getParentOrgId(), "RETAILER", 0, con);
						if(!isValid){
							throw new SLEException(SLEErrors.RETAILER_BALANCE_INSUFFICIENT_ERROR_CODE,SLEErrors.RETAILER_BALANCE_INSUFFICIENT_ERROR_MESSAGE);

						}
						isValid=OrgCreditUpdation.updateOrganizationBalWithValidate(agentNetAmt, "CLAIM_BAL", "CREDIT", userBean
								.getParentOrgId(),0, "AGENT", 0, con);
						if(!isValid){
							throw new SLEException(SLEErrors.AGENT_BALANCE_INSUFFICIENT_ERROR_CODE,SLEErrors.AGENT_BALANCE_INSUFFICIENT_ERROR_MESSAGE);

						}
						
						
					}else{
						throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE,SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
					}
					
		}catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE,SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);

		}
		
		return transId;
		
	}*/
	
	public static synchronized long sportsLotteryPurchaseTicketDaoImpl(TPTxRequestBean gamePlayBean,UserInfoBean userBean,Connection con) throws SLEException, LMSException, SQLException{
		logger.info("inside sportsLotteryPurchaseTicketDaoImpl...");
		
		PreparedStatement pstmt = null;
		PreparedStatement insertPstmt=null;
		ResultSet insertRs=null;
		ResultSet rsTrns = null;
		long transId=0;
		
		try{
			double vat = 0.0;
			double govt_comm = 0.0;
			double retCommRate = 0.0;
			double agtCommRate = 0.0;
			double prize_payout_ratio = 0.0;
			int gameTypeId=gamePlayBean.getGameTypeId();
			double ticketMrp=gamePlayBean.getTxAmount();
			boolean isValid=false;

			GameMasterLMSBean gameMasterLMSBean =Util.getSLEGameMasterLMSBean(gameTypeId);
				if(gameMasterLMSBean != null){
					govt_comm = gameMasterLMSBean.getGovtComm();
					vat = gameMasterLMSBean.getVatAmount();
					prize_payout_ratio = gameMasterLMSBean.getPrizePayoutRatio();
				}else{
					throw new SLEException(SLEErrors.INVALID_USER_NAME_CODE, SLEErrors.INVALID_USER_NAME_MESSAGE);
				}
				
				// get sale comm variance from util
				retCommRate = Util.getSLESaleCommVariance(userBean.getUserOrgId(),gameTypeId);
				agtCommRate = Util.getSLESaleCommVariance(userBean.getParentOrgId(),gameTypeId);

				if(gamePlayBean.getTxAmount() > 0) {
					// check with ACA
					// if online sale amt > ACA then return ERROR
					pstmt = con.prepareStatement("select (available_credit-claimable_bal) as availbale_sale_bal from st_lms_organization_master where organization_id=?");
					pstmt.setInt(1, userBean.getUserOrgId());
					rsTrns = pstmt.executeQuery();
					//check for >= ?? needs to be confirmed
					if (rsTrns.next()) {
						if (!(rsTrns.getDouble("availbale_sale_bal") >= ticketMrp - ticketMrp * retCommRate * .01)) {
							logger.info(SLEErrors.RETAILER_BALANCE_INSUFFICIENT_ERROR_MESSAGE);
							throw new SLEException(SLEErrors.RETAILER_BALANCE_INSUFFICIENT_ERROR_CODE,  SLEErrors.RETAILER_BALANCE_INSUFFICIENT_ERROR_MESSAGE);
						}
					} else {
						throw new SLEException(SLEErrors.INVALID_USER_NAME_CODE, SLEErrors.INVALID_USER_NAME_MESSAGE);
					}

					// check for agents ACA and claimable balance
					if(!"GHANA".equalsIgnoreCase(Utility.getPropertyValue("COUNTRY_DEPLOYED"))){
						pstmt = con.prepareStatement("select (available_credit-claimable_bal) as availbale_sale_bal from st_lms_organization_master where organization_id=?");
						pstmt.setInt(1, userBean.getParentOrgId());
						rsTrns = pstmt.executeQuery();
						//check for >= ?? needs to be confirmed
						if (rsTrns.next()) {
							if (!(rsTrns.getDouble("availbale_sale_bal") >= ticketMrp - ticketMrp * agtCommRate * .01)) {
								logger.info(SLEErrors.AGENT_BALANCE_INSUFFICIENT_ERROR_MESSAGE);
								throw new SLEException(SLEErrors.AGENT_BALANCE_INSUFFICIENT_ERROR_CODE, SLEErrors.AGENT_BALANCE_INSUFFICIENT_ERROR_MESSAGE);
							}
						} else {
							throw new SLEException(SLEErrors.INVALID_USER_NAME_CODE, SLEErrors.INVALID_USER_NAME_MESSAGE);
						}
					}
				}
			
				// insert in main transaction table
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

						// calculate vat
						double vatAmount = CommonMethods.calculateDrawGameVatPlr(ticketMrp, saleCommRate, prize_payout_ratio, govt_comm, vat);
						double taxableSale = CommonMethods.calTaxableSale(ticketMrp, saleCommRate, prize_payout_ratio, govt_comm, vat);

						double retNet = CommonMethods.fmtToTwoDecimal(ticketMrp) - CommonMethods.fmtToTwoDecimal(ticketMrp * retCommRate * .01);
						double agtNet = CommonMethods.fmtToTwoDecimal(ticketMrp) - CommonMethods.fmtToTwoDecimal(ticketMrp * agtCommRate * .01);
						
						// insert into retailer transaction master
						insertPstmt = con.prepareStatement("INSERT INTO st_lms_retailer_transaction_master (transaction_id,retailer_user_id,retailer_org_id,game_id,transaction_date,transaction_type) VALUES (?,?,?,?,?,?)");
						insertPstmt.setLong(1, transId);
						insertPstmt.setInt(2, userBean.getUserId()); 
						insertPstmt.setInt(3, userBean.getUserOrgId());
						//insertPstmt.setInt(4, gamePlayBean.getGameId());
						insertPstmt.setInt(4, gamePlayBean.getGameTypeId());
						insertPstmt.setTimestamp(5, Util.getCurrentTimeStamp());
						insertPstmt.setString(6, "SLE_SALE");
						insertPstmt.executeUpdate();
						
						
						//double retCommAmt=ticketMrp*SportsLotteryUtils.gameTypeInfoMap.get(gameTypeId).getRetailetSaleComm()*.01;
						//double retNetAmt=ticketMrp-retNet;
						
						//double agtCommAmt=ticketMrp*SportsLotteryUtils.gameTypeInfoMap.get(gameTypeId).getAgentSaleComm()*.01;
						//double agentNetAmt=ticketMrp-agtNet;
						
						insertPstmt = con.prepareStatement("insert into st_sle_ret_sale(transaction_id,engine_tx_id,game_id,game_type_id,retailer_org_id,ticket_nbr,mrp_amt,retailer_comm_amt,retailer_net_amt,agent_comm_amt,agent_net_amt,good_cause_amt,vat_amt,taxable_sale,player_mob_number,claim_status,transaction_date,is_cancel) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
						
						insertPstmt.setLong(1, transId);
						insertPstmt.setLong(2, gamePlayBean.getEngineTxId());
						insertPstmt.setInt(3, gamePlayBean.getGameId());
						insertPstmt.setInt(4,gamePlayBean.getGameTypeId());
						insertPstmt.setInt(5, userBean.getUserOrgId());
						insertPstmt.setString(6, gamePlayBean.getTicketNumber());
						insertPstmt.setDouble(7, CommonMethods.fmtToTwoDecimal(ticketMrp));
						insertPstmt.setDouble(8,CommonMethods.fmtToTwoDecimal(ticketMrp * retCommRate * .01));
						insertPstmt.setDouble(9,CommonMethods.fmtToTwoDecimal(retNet));
						insertPstmt.setDouble(10,CommonMethods.fmtToTwoDecimal(ticketMrp * agtCommRate * .01));
						insertPstmt.setDouble(11,CommonMethods.fmtToTwoDecimal(agtNet));
						insertPstmt.setDouble(12,CommonMethods.fmtToTwoDecimal(goodCauseAmt));
						insertPstmt.setDouble(13,CommonMethods.fmtToTwoDecimal(vatAmount));
						insertPstmt.setDouble(14,CommonMethods.fmtToTwoDecimal(taxableSale));
						insertPstmt.setString(15, gamePlayBean.getPlrMobileNumber());
						insertPstmt.setString(16, "CLAIM_BAL");
						insertPstmt.setTimestamp(17, Util.getCurrentTimeStamp());
						insertPstmt.setString(18, "N");
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
					DBConnect.closeConnection(insertPstmt, rsTrns);
					DBConnect.closeConnection(pstmt, insertRs);
				}
		return transId;
	}
	
	public static synchronized long refundPurchaseTicket(TPTxRequestBean gamePlayBean,UserInfoBean userInfoBean, Connection con) throws SLEException {
		
		logger.debug("inside cancellation Cancel Transaction:: "+ gamePlayBean.getTicketNumber());
		long transId = 0;
		
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		try {
			String ticketNumber = gamePlayBean.getTicketNumber();
			String ticketIdDB = ticketNumber.substring(0,ticketNumber.length()-1);
			int gameId = gamePlayBean.getGameId();
			int gameTypeId = 0;
			
			// check for pwt status
			pstmt = con.prepareStatement("select ticket_nbr from st_sle_pwt_inv where ticket_nbr = ?");
			pstmt.setString(1, ticketIdDB);
			ResultSet rsPwt = pstmt.executeQuery();
			if (rsPwt.next()) {
				logger.info("ticket present in st_sle_pwt_inv");
				throw new SLEException(SLEErrors.CANCEL_INVALID_ERROR_CODE, SLEErrors.CANCEL_INVALID_ERROR_MESSAGE);
			}
			
			// check for already cancelled in refund table status
			pstmt = con.prepareStatement("select slSale.ticket_nbr from st_sle_ret_sale_refund slRef INNER JOIN st_sle_ret_sale slSale on slSale.transaction_id=slRef.sale_ref_transaction_id where slSale.engine_tx_id= ?");
			pstmt.setLong(1, gamePlayBean.getEngineSaleTxId());
			rsPwt = pstmt.executeQuery();
			if (rsPwt.next()) {
				logger.info("ticket present in st_sle_ret_sale_refund");
				throw new SLEException(SLEErrors.CANCEL_INVALID_ERROR_CODE, SLEErrors.CANCEL_INVALID_ERROR_MESSAGE);
			}
			
			// get the commission from sale table on which this ticket was sold
			pstmt = con.prepareStatement("select * from st_sle_ret_sale where game_id=? and game_type_id = ? and  engine_tx_id=?");
			
			pstmt.setInt(1, gameId);
			pstmt.setInt(2, gamePlayBean.getGameTypeId());
			pstmt.setLong(3, gamePlayBean.getEngineSaleTxId());
			
			ResultSet ticketDetails = pstmt.executeQuery();
			double ticketMrp = 0.0;
			double retComm = 0.0;
			double agtComm = 0.0;
			double goodCause = 0.0;
			double vatAmt = 0.0;
			double taxableSale = 0.0;
			double retNet = 0.0;
			double agtNet = 0.0;
			long refTranId;
			boolean isValid=false;
			if (ticketDetails.next()) {
				ticketMrp = ticketDetails.getDouble("mrp_amt");
				retComm = ticketDetails.getDouble("retailer_comm_amt");
				retNet = ticketDetails.getDouble("retailer_net_amt");
				agtComm = ticketDetails.getDouble("agent_comm_amt");
				agtNet = ticketDetails.getDouble("agent_net_amt");
				goodCause = ticketDetails.getDouble("good_cause_amt");
				vatAmt = ticketDetails.getDouble("vat_amt");
				taxableSale = ticketDetails.getDouble("taxable_sale");
				gameId = ticketDetails.getInt("game_id");
				gameTypeId = ticketDetails.getInt("game_type_id");
				refTranId = ticketDetails.getLong("transaction_id");
			} else {
				logger.info("ticket not present in st_sle_ret_sale");
				throw new SLEException(SLEErrors.CANCEL_INVALID_ERROR_CODE, SLEErrors.CANCEL_INVALID_ERROR_MESSAGE);
			}            
		
			Timestamp txDataTime = new java.sql.Timestamp(new Date().getTime());
			double sleTktCancelCharges = (Double.parseDouble(Utility.getPropertyValue("SLE_TKT_CANCELLATION_CHARGES")));
			double cancellationCharge = (ticketMrp * .01 * sleTktCancelCharges);
			logger.debug("{}*******Inside Cancellation*********{}*********{}",ticketMrp ,sleTktCancelCharges , cancellationCharge);

			//subtract cancel amount from sale in responsible Gaming
			//ResponsibleGaming.respGaming(userInfoBean, "SLE_CANCEL_AMOUNT",String.valueOf(ticketMrp), con);
			
			// insert in main transaction table
			pstmt = con.prepareStatement("INSERT INTO st_lms_transaction_master (user_type, service_code, interface) VALUES (?,?,?)",PreparedStatement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, userInfoBean.getUserType());
			pstmt.setString(2, gamePlayBean.getServiceCode());
			pstmt.setString(3, gamePlayBean.getInterfaceType());
			pstmt.executeUpdate();
			rs = pstmt.getGeneratedKeys();
			
			if (rs.next()) {
				transId = rs.getLong(1);
				logger.debug("trans id " + transId);
				// insert into retailer transaction master
				pstmt = con.prepareStatement("INSERT INTO st_lms_retailer_transaction_master (transaction_id,retailer_user_id,retailer_org_id,game_id,transaction_date,transaction_type) VALUES (?,?,?,?,?,?)");
				pstmt.setLong(1, transId);
				pstmt.setInt(2, userInfoBean.getUserId());
				pstmt.setInt(3, userInfoBean.getUserOrgId());
				pstmt.setInt(4, gameTypeId);
				pstmt.setTimestamp(5, txDataTime);
				pstmt.setString(6, "SLE_REFUND_CANCEL");
				pstmt.executeUpdate();
				pstmt.clearParameters();
				
				// Mark Ticket as cancel in sale table
				pstmt = con.prepareStatement("update st_sle_ret_sale set is_cancel = 'Y' where  engine_tx_id= ? ");
				pstmt.setLong(1, gamePlayBean.getEngineSaleTxId());
				pstmt.executeUpdate();
				pstmt.clearParameters();
				
				// insert in retailer draw sale table
				//pstmt = con.prepareStatement("insert into st_sle_ret_sale_refund(transaction_id,mrp_amt,retailer_comm,net_amt,agent_comm,agent_net_amt,retailer_org_id,claim_status,good_cause_amt,vat_amt,taxable_sale,game_id,cancellation_charges,ticket_nbr,ref_transaction_id) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
				
				pstmt = con.prepareStatement("insert into st_sle_ret_sale_refund(transaction_id ,engine_tx_id,game_id , game_type_id ,retailer_org_id,ticket_nbr , mrp_amt , retailer_comm_amt, retailer_net_amt,agent_comm_amt ,agent_net_amt ,good_cause_amt ,vat_amt ,taxable_sale ,cancellation_charges ,claim_status ,sale_ref_transaction_id,transaction_date) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			
				pstmt.setLong(1, transId);
				pstmt.setLong(2, gamePlayBean.getEngineTxId());
				pstmt.setInt(3, gameId);
				pstmt.setDouble(4, gameTypeId);
				pstmt.setInt(5, userInfoBean.getUserOrgId());
				pstmt.setString(6, ticketIdDB);
				pstmt.setDouble(7, CommonMethods.fmtToTwoDecimal(ticketMrp));
				pstmt.setDouble(8, CommonMethods.fmtToTwoDecimal(retComm));
				pstmt.setDouble(9, CommonMethods.fmtToTwoDecimal(retNet) - cancellationCharge);
				pstmt.setDouble(10, CommonMethods.fmtToTwoDecimal(agtComm));
				pstmt.setDouble(11, CommonMethods.fmtToTwoDecimal(agtNet) - cancellationCharge);
				pstmt.setDouble(12, CommonMethods.fmtToTwoDecimal(goodCause));
				pstmt.setDouble(13, CommonMethods.fmtToTwoDecimal(vatAmt));
				pstmt.setDouble(14, CommonMethods.fmtToTwoDecimal(taxableSale));
				pstmt.setDouble(15, cancellationCharge);
				pstmt.setString(16, "CLAIM_BAL");
				pstmt.setLong(17, refTranId);
				pstmt.setTimestamp(18, txDataTime);
				pstmt.executeUpdate();

				//Now make payment updte method only one
				isValid=OrgCreditUpdation.updateOrganizationBalWithValidate(CommonMethods.fmtToTwoDecimal(retNet), "CLAIM_BAL", "DEBIT", userInfoBean.getUserOrgId(), userInfoBean.getParentOrgId(), "RETAILER", 0, con);
				if(!isValid){
					throw new SLEException(SLEErrors.CANCEL_INVALID_ERROR_CODE, SLEErrors.CANCEL_INVALID_ERROR_MESSAGE);
				}
				
				isValid=OrgCreditUpdation.updateOrganizationBalWithValidate(CommonMethods.fmtToTwoDecimal(agtNet), "CLAIM_BAL", "DEBIT", userInfoBean.getParentOrgId(),0, "AGENT", 0, con);
				if(!isValid){
					throw new SLEException(SLEErrors.CANCEL_INVALID_ERROR_CODE, SLEErrors.CANCEL_INVALID_ERROR_MESSAGE);
				}
			} else {
				throw new SLEException(SLEErrors.CANCEL_INVALID_ERROR_CODE, SLEErrors.CANCEL_INVALID_ERROR_MESSAGE);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE, SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (SLEException se) {
			throw se;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE, SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeConnection(pstmt, rs);
		}

		return transId;
	}
	
	public static synchronized int updateRgForTicketReprint(UserInfoBean userInfoBean , TPTxRequestBean tpTransactionBean , Connection con) throws SLEException{

		//long lmsTxId = 0;
		int errorCode = 0;
		//ResultSet rs = null;
		//Statement stmt = null;
		try {
			// CHECK IF LAST TX SALE ???
			/*String query = "select transaction_id from st_sle_ret_sale where ticket_nbr ="+tpTransactionBean.getTicketNumber()+" and game_id ="+tpTransactionBean.getGameId()+" and game_type_id  = "+ tpTransactionBean.getGameTypeId();
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);
			if(rs.next()){
				lmsTxId = rs.getLong("transaction_id");
			}else{
				throw new SLEException(); // REPRINT ERROR INVALID TICKET
			}
			
			// CHECK IF IT HAS NOT BEEN CANCELLED
			query = "select transaction_id from st_sle_ret_sale_refund where sale_ref_transaction_id = "+lmsTxId ;
			rs = stmt.executeQuery(query);
			if(rs.next()){
				lmsTxId = rs.getLong("transaction_id");
			}else{
				throw new SLEException(); // CANCELLED TICKET
			}*/
			

			
			
			con.setAutoCommit(false);
			boolean isFraud = ResponsibleGaming.respGaming(userInfoBean, "SLE_REPRINT", "1", con); // REPRINT TIKCET LIMIT
			if(isFraud){
				errorCode = LMSErrors.RG_LIMIT_EXCEPTION_ERROR_CODE;
			}else{
				con.commit();
			}
		} catch (Exception e) {
			errorCode = SLEErrors.GENERAL_EXCEPTION_ERROR_CODE;
		}
		return errorCode;
	}
	
	public static synchronized int updateRGForInvalidPWT(UserInfoBean userInfoBean , TPTxRequestBean tpTransactionBean , Connection con) throws SLEException{
		int errorCode = 0;
		try {
			con.setAutoCommit(false);
			boolean isFraud = ResponsibleGaming.respGaming(userInfoBean, "SLE_INVALID_PWT", "1", con); // INVALID TIKCET LIMIT
			if(isFraud){
				errorCode = LMSErrors.RG_LIMIT_EXCEPTION_ERROR_CODE;
			}else{
				con.commit();
			}
		} catch (Exception e) {
			errorCode = SLEErrors.GENERAL_EXCEPTION_ERROR_CODE;
		}
		return errorCode;
	}
	public static synchronized void updatePurchaseTicket(long ticketNumber,long transId,int gameId,int gameTypeId,Connection con) throws SLEException{
		 PreparedStatement updatePstmt=null;
		 try{
			 updatePstmt=con.prepareStatement("update st_sle_ret_sale_? set ticket_nbr=? where transaction_id=? and game_id=? and game_type_id=?");
			 updatePstmt.setInt(1, gameId);
			 updatePstmt.setLong(2, ticketNumber);
			 updatePstmt.setLong(3, transId);
			 updatePstmt.setInt(4, gameId);
			 updatePstmt.setInt(5, gameTypeId);
			 updatePstmt.executeUpdate();
			 
		 }catch (SQLException e) {
				throw new SLEException(SLEErrors.SQL_EXCEPTION_ERROR_CODE,SLEErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		}catch (Exception e) {
			throw new SLEException(SLEErrors.GENERAL_EXCEPTION_ERROR_CODE,SLEErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		
	}
}
