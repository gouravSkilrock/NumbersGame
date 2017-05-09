package com.skilrock.lms.common.utility;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.beans.OrgPwtLimitBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.Utility;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.drawGames.common.CommonFunctionsHelper;
import com.skilrock.lms.dge.beans.CancelTicketBean;
import com.skilrock.lms.dge.beans.GameMasterLMSBean;
import com.skilrock.lms.web.drawGames.common.Util;

public class orgOnLineSaleCreditUpdation {
	static Log logger = LogFactory.getLog(orgOnLineSaleCreditUpdation.class);

	public static int drawRaffleSaleTicketUpdate(long refrenceId,
			String ticket_nbr, int game_nbr, UserInfoBean userInfoBean) {
		logger.debug("inside ticket update yogesh **********************:: "
				+ ticket_nbr);
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			con = DBConnect.getConnection();
			con.setAutoCommit(false);

			// update retaler draw sale table for ticket number
			pstmt = con
					.prepareStatement("update st_dg_ret_sale_? set game_id=(select game_id from st_dg_game_master where game_nbr=?),ticket_nbr=? where transaction_id=?");
			pstmt.setInt(1, game_nbr);
			pstmt.setInt(2, game_nbr);
			pstmt.setString(3, ticket_nbr);
			pstmt.setLong(4, refrenceId);
			int isUpdate = pstmt.executeUpdate();
			if (isUpdate == 1) {
				con.commit();
				return 1;
			} else {
				// cancelTicketTxnFailed();
				// drawTicketSaleRefund(userInfoBean,
				// game_nbr,"FAILED",refrenceId);
				return 0;
			}

		} catch (Exception se) {
			drawTicketSaleRefund(userInfoBean, game_nbr, "FAILED", refrenceId);
			se.printStackTrace();
			return 0;
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

		}

	}

	public static long drawRaffleTicketCancel(UserInfoBean userInfoBean,
			Connection con, String cancellationCharges, String tktnumber,
			int gameNbr) {

		logger.debug("inside cancellation  yogesh **********************:: "
				+ tktnumber);
		double cancellationChargePer = Double.parseDouble(cancellationCharges);

		// whether already refunded
		// if if pwt has claimed for a single panel
		//

		PreparedStatement pstmt = null;

		ResultSet rsTrns = null;

		try {

			// int gameNbr = cancelTicketBean.getGameNo();

			// check for pwt status
			pstmt = con
					.prepareStatement("select ticket_nbr from st_dg_pwt_inv_? where ticket_nbr = ?");
			pstmt.setInt(1, gameNbr);
			pstmt.setString(2, tktnumber);
			ResultSet rsPwt = pstmt.executeQuery();
			if (rsPwt.next()) {
				return -1;
			}

			// check for already cancelled in refund table status
			pstmt = con
					.prepareStatement("select ticket_nbr from st_dg_ret_sale_refund_? where ticket_nbr=?");
			pstmt.setInt(1, gameNbr);
			pstmt.setString(2, tktnumber);
			rsPwt = pstmt.executeQuery();
			if (rsPwt.next()) {
				return -1;
			}

			// get the comm from draw game master table
			pstmt = con
					.prepareStatement("select * from st_dg_ret_sale_? where ticket_nbr=? and game_id=(select game_id from st_dg_game_master where game_nbr=?)");
			pstmt.setInt(1, gameNbr);
			pstmt.setString(2, tktnumber);
			pstmt.setInt(3, gameNbr);
			ResultSet ticketDetails = pstmt.executeQuery();
			double ticketMrp = 0.0;
			double retComm = 0.0;
			double agtComm = 0.0;
			double govt_comm = 0.0;
			double vatAmt = 0.0;
			double taxableSale = 0.0;
			double retNet = 0.0;
			double agtNet = 0.0;
			double retSdAmt = 0.0;
			double agtVatAmt = 0.0;
			//int refTranId = 0;
			long refTranId=0;
			int gameId = 0;
			if (ticketDetails.next()) {
				ticketMrp = ticketDetails.getDouble("mrp_amt");
				retComm = ticketDetails.getDouble("retailer_comm");
				retNet = ticketDetails.getDouble("net_amt");
				agtComm = ticketDetails.getDouble("agent_comm");
				agtNet = ticketDetails.getDouble("agent_net_amt");
				govt_comm = ticketDetails.getDouble("good_cause_amt");
				vatAmt = ticketDetails.getDouble("vat_amt");
				taxableSale = ticketDetails.getDouble("taxable_sale");
				retSdAmt = ticketDetails.getDouble("ret_sd_amt");
				agtVatAmt = ticketDetails.getDouble("agt_vat_amt");
				gameId = ticketDetails.getInt("game_id");
				refTranId = ticketDetails.getLong("transaction_id");
			} else {
				return -1;
			}
			double cancellationCharge = ticketMrp * .01 * cancellationChargePer;
			logger.debug(ticketMrp + "*******Inside Cancellation*********"
					+ cancellationChargePer + "********" + cancellationCharge);
			// insert in main transaction table
			pstmt = con.prepareStatement(QueryManager
					.insertInLMSTransactionMaster());
			pstmt.setString(1, "RETAILER");
			pstmt.executeUpdate();
			rsTrns = pstmt.getGeneratedKeys();
			if (rsTrns.next()) {
				long transId = rsTrns.getInt(1);
				// cancelTicketBean.setRefTransId(transId + "");
				logger.debug("trans id " + transId);
				// insert into retailer transaction master
				pstmt = con
						.prepareStatement("INSERT INTO st_lms_retailer_transaction_master (transaction_id,retailer_user_id,retailer_org_id,game_id,transaction_date,transaction_type) VALUES (?,?,?,?,?,?)");
				pstmt.setLong(1, transId);
				pstmt.setInt(2, userInfoBean.getUserId()); // these are
				// retailers parent
				// id
				pstmt.setInt(3, userInfoBean.getUserOrgId());
				pstmt.setInt(4, gameId);
				pstmt.setTimestamp(5, new java.sql.Timestamp(new Date()
						.getTime()));
				pstmt.setString(6, "DG_REFUND_CANCEL");
				pstmt.executeUpdate();

				// insert in ret draw sale table
				pstmt = con
						.prepareStatement("insert into st_dg_ret_sale_refund_?(transaction_id,mrp_amt,retailer_comm,net_amt,agent_comm,agent_net_amt,retailer_org_id,claim_status,good_cause_amt,vat_amt,taxable_sale,game_id,cancellation_charges,ticket_nbr,ref_transaction_id,ret_sd_amt,agt_vat_amt) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
				pstmt.setInt(1, gameNbr);
				pstmt.setLong(2, transId);
				pstmt.setDouble(3, ticketMrp);
				pstmt.setDouble(4, CommonMethods.fmtToTwoDecimal(retComm));
				pstmt.setDouble(5, CommonMethods.fmtToTwoDecimal(retNet)
						- cancellationCharge);
				pstmt.setDouble(6, CommonMethods.fmtToTwoDecimal(agtComm));
				pstmt.setDouble(7, CommonMethods.fmtToTwoDecimal(agtNet)
						- cancellationCharge);
				pstmt.setInt(8, userInfoBean.getUserOrgId());
				pstmt.setString(9, "CLAIM_BAL");
				pstmt.setDouble(10, CommonMethods.fmtToTwoDecimal(govt_comm));
				pstmt.setDouble(11, CommonMethods.fmtToTwoDecimal(vatAmt));
				pstmt.setDouble(12, CommonMethods.fmtToTwoDecimal(taxableSale));
				pstmt.setInt(13, gameId);
				pstmt.setDouble(14, cancellationCharge);
				pstmt.setString(15, tktnumber);
				pstmt.setLong(16, refTranId);
				pstmt.setDouble(17, CommonMethods.fmtToTwoDecimal(retSdAmt));
				pstmt.setDouble(18, CommonMethods.fmtToTwoDecimal(agtVatAmt));
				pstmt.executeUpdate();

				// update st_lms_organization_master for claimable balance for
				// retailer
				/*
				 * pstmt = con .prepareStatement("update
				 * st_lms_organization_master set claimable_bal=claimable_bal-?
				 * where organization_id=?"); pstmt.setDouble(1,
				 * CommonMethods.fmtToTwoDecimal(retNet) - cancellationCharge);
				 * pstmt.setInt(2, userInfoBean.getUserOrgId());
				 * pstmt.executeUpdate(); logger.debug("pstmt ret " + pstmt);
				 */

				
				//Now make payment updte method only one
				OrgCreditUpdation.updateOrganizationBalWithValidate(CommonMethods
						.fmtToTwoDecimal(retNet), "CLAIM_BAL", "DEBIT", userInfoBean
						.getUserOrgId(), userInfoBean
						.getParentOrgId(), "RETAILER", 0, con);
				
				OrgCreditUpdation.updateOrganizationBalWithValidate(CommonMethods
						.fmtToTwoDecimal(agtNet), "CLAIM_BAL", "DEBIT", userInfoBean
						.getParentOrgId(),0, "AGENT", 0, con);
				
				/*CommonFunctionsHelper commHelper = new CommonFunctionsHelper();
				commHelper.updateOrgBalance("CLAIM_BAL", CommonMethods
						.fmtToTwoDecimal(retNet), userInfoBean.getUserOrgId(),
						"DEBIT", con);

				// update st_lms_organization_master for claimable balance for
				// agent
				commHelper.updateOrgBalance("CLAIM_BAL", CommonMethods
						.fmtToTwoDecimal(agtNet),
						userInfoBean.getParentOrgId(), "DEBIT", con);*/

				/*
				 * // update st_lms_organization_master for claimable balance
				 * for agent pstmt = con .prepareStatement("update
				 * st_lms_organization_master set claimable_bal=claimable_bal-?
				 * where organization_id=?"); pstmt.setDouble(1,
				 * CommonMethods.fmtToTwoDecimal(agtNet) - cancellationCharge);
				 * pstmt.setInt(2, userInfoBean.getParentOrgId());
				 * pstmt.executeUpdate(); logger.debug("pstmt agt " + pstmt);
				 */
				// con.commit();
				// return ticketMrp - cancellationCharge;
				return transId;
			} else {
				return -1;
			}

		} catch (Exception se) {
			se.printStackTrace();
			return -1;
		}

	}

	/**
	 * This method is used to update organization credit for draw game sale
	 * ticket
	 * 
	 * @param userInfoBean
	 *            logged in user information
	 * @param gameNbr
	 *            for which ticket is sold
	 * @param ticketMrp
	 *            mrp amount of ticket
	 * @return integer value (0) if retailer has insufficient balane (-1) if
	 *         agent(retailer's parent) has insufficient balane (-2) if some
	 *         error has occured (> 0) means valid reference id successful
	 *         transaction
	 * 
	 */

	public static  long drawTcketSaleBalDeduction(
			UserInfoBean userInfoBean, int gameId, double ticketMrp,String plrMobileNbr,Connection con) {
		//logger.debug("inside balance deduction  Ticket Mrp :: ");
		
		PreparedStatement pstmt = null;

		ResultSet rsTrns = null;
		boolean isValid=false;
		try {
			
			/*// get the comm from draw game master table
			pstmt = con
					.prepareStatement("select * from st_dg_game_master where game_nbr="
							+ gameNbr);
			ResultSet gameDetails = pstmt.executeQuery();
			double retCommRate = 0.0;
			double agtCommRate = 0.0;
			double govt_comm = 0.0;
			double vat = 0.0;
			double prize_payout_ratio = 0.0;
			int gameId = 0;
			if (gameDetails.next()) {
				retCommRate = gameDetails.getDouble("retailer_sale_comm_rate");
				agtCommRate = gameDetails.getDouble("agent_sale_comm_rate");
				govt_comm = gameDetails.getDouble("govt_comm");
				vat = gameDetails.getDouble("vat_amt");
				prize_payout_ratio = gameDetails
						.getDouble("prize_payout_ratio");
				gameId = gameDetails.getInt("game_id");
			} else {
				return -2;
			}*/
			
			double retCommRate = 0.0;
			double agtCommRate = 0.0;
			double levyRate = 0.0;
			double agtLevyRate = 0.0;
			double secDepRate = 0.0;
			double govt_comm = 0.0;
			double vat = 0.0;
			double prize_payout_ratio = 0.0;
			double retNet = 0.0;
			double agtNet = 0.0;
			double retSdAmt = 0.0;
			double vatAmount = 0.0;
			double agtVatAmt = 0.0;
			double goodCauseAmt=0.0;
			double retComm = 0.0;
			double agtComm = 0.0;
			
			//get from context			
			GameMasterLMSBean gameMasterLMSBean =Util.getGameMasterLMSBean(gameId);
			if(gameMasterLMSBean!=null){
				//retCommRate=gameMasterLMSBean.getRetSaleCommRate();
				//agtCommRate =gameMasterLMSBean.getAgentSaleCommRate();
				govt_comm = gameMasterLMSBean.getGovtComm();
				vat = gameMasterLMSBean.getVatAmount();
				prize_payout_ratio = gameMasterLMSBean.getPrizePayoutRatio();
				//gameId = gameMasterLMSBean.getGameId();	//not need to get again				
			}else{
				return -2;
			}
			

			/*retCommRate = CommonFunctionsHelper.fetchDGCommOfOrganization(
					gameId, userInfoBean.getUserOrgId(), "SALE", userInfoBean
							.getUserType(), con);

			agtCommRate = CommonFunctionsHelper
					.fetchDGCommOfOrganization(gameId, userInfoBean
							.getParentOrgId(), "SALE", "AGENT", con);*/
			
			
			// get sale comm variance from util
			retCommRate = Util.getSaleCommVariance(userInfoBean.getUserOrgId(),gameId);
			agtCommRate = Util.getSaleCommVariance(userInfoBean.getParentOrgId(),gameId);

			if(ticketMrp>0) {
				// check with ACA
				// if online sale amt > ACA then return ERROR
				pstmt = con
						.prepareStatement("select (available_credit-claimable_bal) as availbale_sale_bal, organization_status from st_lms_organization_master where organization_id=?");
				pstmt.setInt(1, userInfoBean.getUserOrgId());
				rsTrns = pstmt.executeQuery();
				//check for >= ?? needs to be confirmed
				if (rsTrns.next()) {
					if (!(rsTrns.getDouble("availbale_sale_bal") >= ticketMrp
							- ticketMrp * retCommRate * .01)) {
						return 0;
					}
					if("INACTIVE".equals(rsTrns.getString("organization_status"))) {
						return -5;
					}
				} else {
					return -2;
				}

				// check for agents ACA and claimable balance
				if(!"GHANA".equalsIgnoreCase(Utility.getPropertyValue("COUNTRY_DEPLOYED"))){
					pstmt = con
							.prepareStatement("select (available_credit-claimable_bal) as availbale_sale_bal, organization_status from st_lms_organization_master where organization_id=?");
					pstmt.setInt(1, userInfoBean.getParentOrgId());
					rsTrns = pstmt.executeQuery();
					//check for >= ?? needs to be confirmed
					if (rsTrns.next()) {
						if (!(rsTrns.getDouble("availbale_sale_bal") >= ticketMrp
								- ticketMrp * agtCommRate * .01)) {
							return -1;
						}
						if("INACTIVE".equals(rsTrns.getString("organization_status"))) {
							return -2;
						}
					} else {
						return -2;
					}
				}
			}

			// insert in main transaction table
			pstmt = con.prepareStatement(QueryManager
					.insertInLMSTransactionMaster());
			pstmt.setString(1, "RETAILER");
			pstmt.executeUpdate();
			rsTrns = pstmt.getGeneratedKeys();
			if (rsTrns.next()) {
				long transId = rsTrns.getLong(1);
				//logger.debug("trans id " + transId);
				// insert into retailer transaction master
				pstmt = con
						.prepareStatement("INSERT INTO st_lms_retailer_transaction_master (transaction_id,retailer_user_id,retailer_org_id,game_id,transaction_date,transaction_type) VALUES (?,?,?,?,?,?)");
				pstmt.setLong(1, transId);
				pstmt.setInt(2, userInfoBean.getUserId()); // these are
				// retailers parent
				// id
				pstmt.setInt(3, userInfoBean.getUserOrgId());
				pstmt.setInt(4, gameId);
				pstmt.setTimestamp(5, Util.getCurrentTimeStamp());
				pstmt.setString(6, "DG_SALE");
				pstmt.executeUpdate();

                //remove unused variable 	saleCommRate			
				double saleCommRate = 0.0;// This is agent or retailer sale
				// Comm Rate currently set to 0.
				// calculate vat
				double taxableSale = CommonMethods.calTaxableSale(ticketMrp,
						saleCommRate, prize_payout_ratio, govt_comm, vat);
				//logger.debug("taxableSale   ======== " + taxableSale);


				if("BENIN".equalsIgnoreCase(Utility.getPropertyValue("COUNTRY_DEPLOYED"))){
					OrgPwtLimitBean orgLimit = new CommonFunctionsHelper().fetchPwtLimitsOfOrgnization(userInfoBean.getUserOrgId(),con);
					OrgPwtLimitBean parentOrgLimit = new CommonFunctionsHelper().fetchPwtLimitsOfOrgnization(userInfoBean.getParentOrgId(),con);
				
					if(orgLimit != null){
						levyRate = orgLimit.getLevyRate();
						secDepRate = orgLimit.getSecurityDepositRate();
					}
					if(parentOrgLimit != null){
						agtLevyRate = parentOrgLimit.getLevyRate();
					}
					goodCauseAmt=CommonMethods.fmtToTwoDecimal(ticketMrp-(ticketMrp*100)/(100+govt_comm));
					retComm = CommonMethods.fmtToTwoDecimal((ticketMrp-goodCauseAmt)* retCommRate * .01);
					agtComm = retComm;
					vatAmount = CommonMethods.fmtToTwoDecimal(retComm*levyRate*.01);
					retSdAmt = CommonMethods.fmtToTwoDecimal(retComm*secDepRate*.01);
					agtVatAmt = vatAmount;
					retNet = CommonMethods.fmtToTwoDecimal(ticketMrp)- (retComm-vatAmount-retSdAmt);
					agtNet = retNet;
				} else{
					retComm = CommonMethods.fmtToTwoDecimal(ticketMrp * retCommRate* .01);
					agtComm = CommonMethods.fmtToTwoDecimal(ticketMrp * agtCommRate* .01);
					goodCauseAmt=CommonMethods.fmtToTwoDecimal(ticketMrp* govt_comm * .01);
					retNet = CommonMethods.fmtToTwoDecimal(ticketMrp) - retComm;
					agtNet = CommonMethods.fmtToTwoDecimal(ticketMrp) - agtComm;
					vatAmount = CommonMethods.calculateDrawGameVatPlr(ticketMrp, saleCommRate, prize_payout_ratio, govt_comm, vat);
					retSdAmt = 0.0;
					agtVatAmt = CommonMethods.calculateDrawGameVatRet(ticketMrp, saleCommRate, prize_payout_ratio, govt_comm, vat);
				}
				/*
				 * if(isModified) { //update in ret draw sale table pstmt =
				 * con.prepareStatement("update st_dg_ret_sale_? set
				 * transaction_id=transaction_id-1,mrp_amt="+modifiedTotalPurAmt+",retailer_comm="+CommonMethods.fmtToTwoDecimal(ticketMrp*retCommRate*.01)+",net_amt="+CommonMethods.fmtToTwoDecimal(retNet)+",agent_comm="+CommonMethods.fmtToTwoDecimal(ticketMrp*agtCommRate*.01)+",agent_net_amt="+agtNet+",retailer_org_id="+userInfoBean.getUserOrgId()+",claim_status="+"CLAIM_BAL"+",good_cause_amt="+CommonMethods.fmtToTwoDecimal(ticketMrp*govt_comm*.01)+",vat_amt="+CommonMethods.fmtToTwoDecimal(vatAmount)+",taxable_sale="+CommonMethods.fmtToTwoDecimal(taxableSale)+",game_id="+gameId);
				 * pstmt.setInt(1,gameNbr); pstmt.executeUpdate(); }
				 * 
				 * 
				 * 
				 * else
				 */

				// insert in ret draw sale table
				//Needs to confirm from sir
				pstmt = con
						.prepareStatement("insert into st_dg_ret_sale_?(transaction_id,mrp_amt,retailer_comm,net_amt,agent_comm,agent_net_amt,retailer_org_id,claim_status,good_cause_amt,vat_amt,taxable_sale,game_id,player_mob_number,ret_sd_amt,agt_vat_amt) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
				pstmt.setInt(1, gameId);
				pstmt.setLong(2, transId);
				pstmt.setDouble(3, CommonMethods.fmtToTwoDecimal(ticketMrp));
				pstmt.setDouble(4,retComm);
				pstmt.setDouble(5, CommonMethods.fmtToTwoDecimal(retNet));
				pstmt.setDouble(6, agtComm);
				pstmt.setDouble(7, CommonMethods.fmtToTwoDecimal(agtNet));
				pstmt.setInt(8, userInfoBean.getUserOrgId());
				pstmt.setString(9, "CLAIM_BAL");
				pstmt.setDouble(10, goodCauseAmt);
				pstmt.setDouble(11, CommonMethods.fmtToTwoDecimal(vatAmount));
				pstmt.setDouble(12, CommonMethods.fmtToTwoDecimal(taxableSale));
				pstmt.setInt(13, gameId);
				pstmt.setString(14, plrMobileNbr);
				pstmt.setDouble(15, CommonMethods.fmtToTwoDecimal(retSdAmt));
				pstmt.setDouble(16, CommonMethods.fmtToTwoDecimal(agtVatAmt));

				pstmt.executeUpdate();

				// update st_lms_organization_master for claimable balance for
				// retailer
				/*
				 * pstmt = con .prepareStatement("update
				 * st_lms_organization_master set claimable_bal=claimable_bal+?
				 * where organization_id=?"); pstmt.setDouble(1, retNet);
				 * pstmt.setInt(2, userInfoBean.getUserOrgId());
				 * pstmt.executeUpdate(); logger.debug("RET Claimable Balance");
				 */

				
				//Now make payment updte method only one
				if(ticketMrp>0) {
					isValid=OrgCreditUpdation.updateOrganizationBalWithValidate(retNet, "CLAIM_BAL", "CREDIT", userInfoBean
							.getUserOrgId(), userInfoBean
							.getParentOrgId(), "RETAILER", 0, con);
					if(!isValid){
						return -2;
					}
					isValid=OrgCreditUpdation.updateOrganizationBalWithValidate(agtNet, "CLAIM_BAL", "CREDIT", userInfoBean
							.getParentOrgId(),0, "AGENT", 0, con);
					if(!isValid){
						return -2;
					}
				}
				/*CommonFunctionsHelper commHelper = new CommonFunctionsHelper();
				commHelper.updateOrgBalance("CLAIM_BAL", retNet, userInfoBean
						.getUserOrgId(), "CREDIT", con);

				// update st_lms_organization_master for claimable balance for
				// agent
				commHelper.updateOrgBalance("CLAIM_BAL", agtNet, userInfoBean
						.getParentOrgId(), "CREDIT", con);*/
				/*
				 * pstmt = con .prepareStatement("update
				 * st_lms_organization_master set claimable_bal=claimable_bal+?
				 * where organization_id=?"); pstmt.setDouble(1, agtNet);
				 * pstmt.setInt(2, userInfoBean.getParentOrgId());
				 * pstmt.executeUpdate();
				 */

			
				return transId;

			} else {
				return -2;
			}

		} catch (Exception se) {
			se.printStackTrace();
			return -2;
		} 

	}

	/**
	 * **********Added on 30 july,2010
	 * 
	 * @param userInfoBean
	 *            :Contain user Information
	 * @param gameNbr :
	 *            Game no
	 * @param modifiedTotalPurAmt
	 *            :New Total Purchase Amt
	 * @param oldTotalPurAmt :
	 *            old Total Purchase Amt
	 * @param transId
	 * @return
	 */
	public static synchronized long drawTcketSaleBalDedUpdate(
			UserInfoBean userInfoBean, int gameId, double modifiedTotalPurAmt,
			double oldTotalPurAmt, long transId,Connection con) {
		logger
				.debug("inside balance updation  **********************new mrp :: "
						+ modifiedTotalPurAmt);
		
		PreparedStatement pstmt = null;

		ResultSet rsTrns = null;

		try {
			
			/*// get the comm from draw game master table
			pstmt = con
					.prepareStatement("select * from st_dg_game_master where game_nbr="
							+ gameNbr);
			ResultSet gameDetails = pstmt.executeQuery();
			double retCommRate = 0.0;
			double agtCommRate = 0.0;
			double govt_comm = 0.0;
			double vat = 0.0;
			double prize_payout_ratio = 0.0;
			int gameId = 0;
			if (gameDetails.next()) {
				retCommRate = gameDetails.getDouble("retailer_sale_comm_rate");
				agtCommRate = gameDetails.getDouble("agent_sale_comm_rate");
				govt_comm = gameDetails.getDouble("govt_comm");
				vat = gameDetails.getDouble("vat_amt");
				prize_payout_ratio = gameDetails
						.getDouble("prize_payout_ratio");
				gameId = gameDetails.getInt("game_id");
			} else {
				return -2;
			}*/
			
			double retCommRate = 0.0;
			double agtCommRate = 0.0;
			double levyRate = 0.0;
			double agtLevyRate = 0.0;
			double secDepRate = 0.0;
			double oldVatAmt = 0.0;
			double retOldSdAmt = 0.0;
			double agtOldVatAmt = 0.0;
			double govt_comm = 0.0;
			double vat = 0.0;
			double prize_payout_ratio = 0.0;
			double retNet = 0.0;
			double agtNet = 0.0;
			double retSdAmt = 0.0;
			double vatAmount = 0.0;
			double agtVatAmt = 0.0;
			double retNetClm = 0.0;
			double agtNetClm = 0.0;
			double goodCauseAmt=0.0;
			double oldGoodCauseAmt=0.0;
			double retCommAmt = 0.0;
			double agtCommAmt = 0.0;
			double retOldCommAmt=0.0;
			double agtOldCommAmt=0.0;
			
			//get from context			
			GameMasterLMSBean gameMasterLMSBean =Util.getGameMasterLMSBean(gameId);
			if(gameMasterLMSBean!=null){
				retCommRate=gameMasterLMSBean.getRetSaleCommRate();
				agtCommRate =gameMasterLMSBean.getAgentSaleCommRate();
				govt_comm = gameMasterLMSBean.getGovtComm();
				vat = gameMasterLMSBean.getVatAmount();
				prize_payout_ratio = gameMasterLMSBean.getPrizePayoutRatio();
					
			}else{
				return -2;
			}

			/*retCommRate = CommonFunctionsHelper.fetchDGCommOfOrganization(
					gameId, userInfoBean.getUserOrgId(), "SALE", userInfoBean
							.getUserType(), con);

			agtCommRate = CommonFunctionsHelper
					.fetchDGCommOfOrganization(gameId, userInfoBean
							.getParentOrgId(), "SALE", "AGENT", con);*/
			
			// get sale comm variance from util
			retCommRate = Util.getSaleCommVariance(userInfoBean.getUserOrgId(),gameId);
			agtCommRate = Util.getSaleCommVariance(userInfoBean.getParentOrgId(),gameId);
			/*
			 * double taxableSale = (((modifiedTotalPurAmt * (100 - (0 +
			 * prize_payout_ratio + govt_comm))) / 100) * 100) / (100 + vat);
			 */
			double taxableSale = CommonMethods.calTaxableSale(
					modifiedTotalPurAmt, 0, prize_payout_ratio, govt_comm, vat);

			logger.debug("taxableSale   ======== " + taxableSale);

			// double retNetClm = -(oldTotalPurAmt - oldTotalPurAmt *
			// retCommRate
			// * .01)
			// + modifiedTotalPurAmt
			// - modifiedTotalPurAmt
			// * retCommRate
			// * .01;
			// double retNet = modifiedTotalPurAmt - modifiedTotalPurAmt
			// * retCommRate * .01;
			// double agtNet = modifiedTotalPurAmt - modifiedTotalPurAmt
			// * agtCommRate * .01;
			// double agtNetClm = -(oldTotalPurAmt - oldTotalPurAmt *
			// agtCommRate
			// * .01)
			// + modifiedTotalPurAmt
			// - modifiedTotalPurAmt
			// * agtCommRate
			// * .01;

			// Added by Vaibhav
			if("BENIN".equalsIgnoreCase(Utility.getPropertyValue("COUNTRY_DEPLOYED"))){
				OrgPwtLimitBean orgLimit = new CommonFunctionsHelper().fetchPwtLimitsOfOrgnization(userInfoBean.getUserOrgId(),con);
				OrgPwtLimitBean parentOrgLimit = new CommonFunctionsHelper().fetchPwtLimitsOfOrgnization(userInfoBean.getParentOrgId(),con);
	
				
				if(orgLimit != null){
					levyRate = orgLimit.getLevyRate();
					secDepRate = orgLimit.getSecurityDepositRate();
				}
				if(parentOrgLimit != null){
					agtLevyRate = parentOrgLimit.getLevyRate();
				}
				goodCauseAmt=CommonMethods.fmtToTwoDecimal(modifiedTotalPurAmt-(modifiedTotalPurAmt*100)/(100+govt_comm));
				retCommAmt = CommonMethods.fmtToTwoDecimal((modifiedTotalPurAmt-goodCauseAmt)* retCommRate * .01);
				agtCommAmt = retCommAmt;
					
				oldGoodCauseAmt=CommonMethods.fmtToTwoDecimal(oldTotalPurAmt-(oldTotalPurAmt*100)/(100+govt_comm));
				retOldCommAmt = CommonMethods.fmtToTwoDecimal((oldTotalPurAmt-goodCauseAmt)* retCommRate * .01);
				agtOldCommAmt = retOldCommAmt;
				
				vatAmount = CommonMethods.fmtToTwoDecimal(retCommAmt*levyRate*.01);
				retSdAmt = CommonMethods.fmtToTwoDecimal(retCommAmt*secDepRate*.01);
				agtVatAmt = vatAmount;
				
				oldVatAmt = CommonMethods.fmtToTwoDecimal(retOldCommAmt*levyRate*.01);
				retOldSdAmt = CommonMethods.fmtToTwoDecimal(retOldCommAmt*secDepRate*.01);
				agtOldVatAmt = oldVatAmt;
				
				retNet = CommonMethods.fmtToTwoDecimal(modifiedTotalPurAmt)- (retCommAmt-vatAmount-retSdAmt);
				agtNet = retNet;
				
				retNetClm = retNet- (CommonMethods.fmtToTwoDecimal(oldTotalPurAmt) - (retOldCommAmt-oldVatAmt-retOldSdAmt));
				agtNetClm =retNetClm;
				
			} else{
				goodCauseAmt=CommonMethods.fmtToTwoDecimal(modifiedTotalPurAmt* govt_comm * .01);
				retCommAmt = CommonMethods.fmtToTwoDecimal(modifiedTotalPurAmt * retCommRate * .01);
				agtCommAmt = CommonMethods.fmtToTwoDecimal(modifiedTotalPurAmt * agtCommRate * .01);
					
				retOldCommAmt = CommonMethods.fmtToTwoDecimal(oldTotalPurAmt * retCommRate * .01);
				agtOldCommAmt = CommonMethods.fmtToTwoDecimal(oldTotalPurAmt * agtCommRate * .01);
				retNet = CommonMethods.fmtToTwoDecimal(modifiedTotalPurAmt)-  retCommAmt;
				agtNet = CommonMethods.fmtToTwoDecimal(modifiedTotalPurAmt)- agtCommAmt;
				vatAmount = CommonMethods.calculateDrawGameVatPlr(modifiedTotalPurAmt, 0, prize_payout_ratio, govt_comm, vat);
				retSdAmt = 0.0;
				agtVatAmt = CommonMethods.calculateDrawGameVatRet(modifiedTotalPurAmt, 0, prize_payout_ratio, govt_comm, vat);
				retNetClm = retNet- (CommonMethods.fmtToTwoDecimal(oldTotalPurAmt) - retOldCommAmt);
				agtNetClm = agtNet- (CommonMethods.fmtToTwoDecimal(oldTotalPurAmt) - agtOldCommAmt);
			}

			// update in ret draw sale table
			StringBuilder saleQuery = new StringBuilder();
			saleQuery = saleQuery.append("update st_dg_ret_sale_? set mrp_amt=").append(modifiedTotalPurAmt).
					append(",retailer_comm=").append(retCommAmt).append(",net_amt=").append(CommonMethods.fmtToTwoDecimal(retNet))
					.append(",agent_comm=").append(agtCommAmt).append(",agent_net_amt=").append(agtNet).
					append(",ret_sd_amt=").append(retSdAmt).append(",agt_vat_amt=").append(CommonMethods.fmtToTwoDecimal(agtVatAmt)).
					append(",good_cause_amt=").append(goodCauseAmt).append(",vat_amt=").append(CommonMethods.fmtToTwoDecimal(vatAmount)).
					append(",taxable_sale=").append(CommonMethods.fmtToTwoDecimal(taxableSale)).append(" where transaction_id=").
					append(transId).append(" and retailer_org_id=").append(userInfoBean.getUserOrgId());
			
			pstmt = con.prepareStatement(saleQuery.toString());
			pstmt.setInt(1, gameId);
			pstmt.executeUpdate();

			// update st_lms_organization_master for claimable balance for
			// retailer
			/*
			 * pstmt = con .prepareStatement("update st_lms_organization_master
			 * set claimable_bal=claimable_bal+? where organization_id=?");
			 * pstmt.setDouble(1, retNetClm); pstmt.setInt(2,
			 * userInfoBean.getUserOrgId()); pstmt.executeUpdate();
			 */

			
			//Now make payment updte method only one
			OrgCreditUpdation.updateOrganizationBalWithValidate(retNetClm, "CLAIM_BAL", "CREDIT", userInfoBean
					.getUserOrgId(), userInfoBean
					.getParentOrgId(), "RETAILER", 0, con);
			
			OrgCreditUpdation.updateOrganizationBalWithValidate(agtNetClm, "CLAIM_BAL", "CREDIT", userInfoBean
					.getParentOrgId(),0, "AGENT", 0, con);
			
			
			/*CommonFunctionsHelper commHelper = new CommonFunctionsHelper();
			commHelper.updateOrgBalance("CLAIM_BAL", retNetClm, userInfoBean
					.getUserOrgId(), "CREDIT", con);

			// update st_lms_organization_master for claimable balance for agent
			commHelper.updateOrgBalance("CLAIM_BAL", agtNetClm, userInfoBean
					.getParentOrgId(), "CREDIT", con);*/

			/*
			 * pstmt = con .prepareStatement("update st_lms_organization_master
			 * set claimable_bal=claimable_bal+? where organization_id=?");
			 * pstmt.setDouble(1, agtNetClm); pstmt.setInt(2,
			 * userInfoBean.getParentOrgId()); pstmt.executeUpdate();
			 */
		
			return transId;

		} catch (Exception se) {
			se.printStackTrace();
			return -2;
		}

	}

	/**
	 * This method is used to cancel the ticket
	 * 
	 * @param userInfoBean
	 * @param gameNbr
	 * @param cancelTicketBean.getTicketNo()
	 * @param con
	 * @return 1 if successfully cancelled
	 */

	public static double drawTicketCancel(UserInfoBean userInfoBean,
			CancelTicketBean cancelTicketBean, Connection con,
			String cancellationCharges) {

		logger.debug("inside cancellation Cancel Transaction:: "+ cancelTicketBean.getTicketNo());
		double cancellationChargePer = Double.parseDouble(cancellationCharges);

		// whether already refunded
		// if if pwt has claimed for a single panel
		//

		PreparedStatement pstmt = null;
		// 
		ResultSet rsTrns = null;

		try {

			int gameId = cancelTicketBean.getGameId();

			// check for pwt status
			pstmt = con
					.prepareStatement("select ticket_nbr from st_dg_pwt_inv_? where ticket_nbr = ?");
			pstmt.setInt(1, gameId);
			pstmt.setString(2, cancelTicketBean.getTicketNo());
			ResultSet rsPwt = pstmt.executeQuery();
			if (rsPwt.next()) {
				return -1;
			}

			// check for already cancelled in refund table status
			pstmt = con
					.prepareStatement("select ticket_nbr from st_dg_ret_sale_refund_? where ticket_nbr=?");
			pstmt.setInt(1, gameId);
			pstmt.setString(2, cancelTicketBean.getTicketNo());
			rsPwt = pstmt.executeQuery();
			if (rsPwt.next()) {
				return -1;
			}

			// get the comm from draw game master table
			pstmt = con
					.prepareStatement("select * from st_dg_ret_sale_? where ticket_nbr=? and game_id=?");
			pstmt.setInt(1, gameId);
			pstmt.setString(2, cancelTicketBean.getTicketNo());
			pstmt.setInt(3, gameId);
			ResultSet ticketDetails = pstmt.executeQuery();
			double ticketMrp = 0.0;
			double retComm = 0.0;
			double agtComm = 0.0;
			double govt_comm = 0.0;
			double vatAmt = 0.0;
			double taxableSale = 0.0;
			double retNet = 0.0;
			double agtNet = 0.0;
			double retSdAmt = 0.0;
			double agtVatAmt = 0.0;
			long refTranId;
			long saleTransId=0;
			boolean isValid=false;
			if (ticketDetails.next()) {
				saleTransId=ticketDetails.getLong("transaction_id");
				ticketMrp = ticketDetails.getDouble("mrp_amt");
				retComm = ticketDetails.getDouble("retailer_comm");
				retNet = ticketDetails.getDouble("net_amt");
				agtComm = ticketDetails.getDouble("agent_comm");
				agtNet = ticketDetails.getDouble("agent_net_amt");
				govt_comm = ticketDetails.getDouble("good_cause_amt");
				vatAmt = ticketDetails.getDouble("vat_amt");
				taxableSale = ticketDetails.getDouble("taxable_sale");
				gameId = ticketDetails.getInt("game_id");
				refTranId = ticketDetails.getLong("transaction_id");
				retSdAmt = ticketDetails.getDouble("ret_sd_amt");
				agtVatAmt = ticketDetails.getDouble("agt_vat_amt");
			} else {
				return -1;
			}            
			double cancellationCharge = ticketMrp * .01 * cancellationChargePer;
			logger.debug(ticketMrp + "*******Inside Cancellation*********"
					+ cancellationChargePer + "********" + cancellationCharge);
			
			//subtract cancel amount from sale in responsible Gaming
			ResponsibleGaming
			.respGaming(userInfoBean, "DG_CANCEL_AMOUNT",String.valueOf(ticketMrp), con);
			
			
			if(cancelTicketBean.isHoldAutoCancel()){
				Calendar calendar=Calendar.getInstance();
				int dayOfYear = calendar.get(Calendar.DAY_OF_YEAR); 
				
				if(cancelTicketBean.getAutoCancelHoldDays()+cancelTicketBean.getDayOfTicket() <= dayOfYear){
					// insert in ret draw sale table
					
					//need to change this query after cancel review by sumit
					Statement stmt=con.createStatement();
					ResultSet dateRs=stmt.executeQuery("select transaction_date from st_lms_retailer_transaction_master where transaction_id="+saleTransId);
					Timestamp transactionDate=null;
					if(dateRs.next()){
						 transactionDate=dateRs.getTimestamp("transaction_date");
					}else{
						return -1;
					}
					
					pstmt = con
							.prepareStatement("insert into st_dg_ret_pending_cancel(sale_ref_transaction_id,ticket_nbr,mrp_amt,ret_net_amt,agent_net_amt,game_id,cancel_attempt_time,transaction_date,retailer_org_id,reason)values(?,?,?,?,?,?,?,?,?,?)");
					pstmt.setLong(1, saleTransId);
					pstmt.setString(2, cancelTicketBean.getTicketNo());
					pstmt.setDouble(3, ticketMrp);
					pstmt.setDouble(4, retNet);
					pstmt.setDouble(5, agtNet);
					pstmt.setInt(6, gameId);
					pstmt.setTimestamp(7, Util.getCurrentTimeStamp());
					pstmt.setTimestamp(8, transactionDate);
					pstmt.setInt(9, userInfoBean.getUserOrgId());
					pstmt.setString(10, "CANCEL_EXPIRED");
					logger.info("Auto Cancel Expire Insert Qry:"+pstmt);
					pstmt.executeUpdate();
					return -3;
				}
				
			}
			
			
				
			// insert in main transaction table
			pstmt = con.prepareStatement(QueryManager
					.insertInLMSTransactionMaster());
			pstmt.setString(1, "RETAILER");
			pstmt.executeUpdate();
			rsTrns = pstmt.getGeneratedKeys();
			if (rsTrns.next()) {
				long transId = rsTrns.getLong(1);
				cancelTicketBean.setRefTransId(transId + "");
				logger.debug("trans id " + transId);
				// insert into retailer transaction master
				pstmt = con
						.prepareStatement("INSERT INTO st_lms_retailer_transaction_master (transaction_id,retailer_user_id,retailer_org_id,game_id,transaction_date,transaction_type) VALUES (?,?,?,?,?,?)");
				pstmt.setLong(1, transId);
				pstmt.setInt(2, userInfoBean.getUserId()); // these are
				// retailers parent
				// id
				pstmt.setInt(3, userInfoBean.getUserOrgId());
				pstmt.setInt(4, gameId);
				pstmt.setTimestamp(5, new java.sql.Timestamp(new Date()
						.getTime()));
				pstmt.setString(6, "DG_REFUND_CANCEL");
				pstmt.executeUpdate();

				// insert in ret draw sale table
				pstmt = con
						.prepareStatement("insert into st_dg_ret_sale_refund_?(transaction_id,mrp_amt,retailer_comm,net_amt,agent_comm,agent_net_amt,retailer_org_id,claim_status,good_cause_amt,vat_amt,taxable_sale,game_id,cancellation_charges,ticket_nbr,ref_transaction_id,ret_sd_amt,agt_vat_amt) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
				pstmt.setInt(1, gameId);
				pstmt.setLong(2, transId);
				pstmt.setDouble(3, ticketMrp);
				pstmt.setDouble(4, CommonMethods.fmtToTwoDecimal(retComm));
				pstmt.setDouble(5, CommonMethods.fmtToTwoDecimal(retNet)
						- cancellationCharge);
				pstmt.setDouble(6, CommonMethods.fmtToTwoDecimal(agtComm));
				pstmt.setDouble(7, CommonMethods.fmtToTwoDecimal(agtNet)
						- cancellationCharge);
				pstmt.setInt(8, userInfoBean.getUserOrgId());
				pstmt.setString(9, "CLAIM_BAL");
				pstmt.setDouble(10, CommonMethods.fmtToTwoDecimal(govt_comm));
				pstmt.setDouble(11, CommonMethods.fmtToTwoDecimal(vatAmt));
				pstmt.setDouble(12, CommonMethods.fmtToTwoDecimal(taxableSale));
				pstmt.setInt(13, gameId);
				pstmt.setDouble(14, cancellationCharge);
				pstmt.setString(15, cancelTicketBean.getTicketNo());
				pstmt.setLong(16, refTranId);
				pstmt.setDouble(17, CommonMethods.fmtToTwoDecimal(retSdAmt));
				pstmt.setDouble(18, CommonMethods.fmtToTwoDecimal(agtVatAmt));
				pstmt.executeUpdate();

				// update st_lms_organization_master for claimable balance for
				// retailer
				/*
				 * pstmt = con .prepareStatement("update
				 * st_lms_organization_master set claimable_bal=claimable_bal-?
				 * where organization_id=?"); pstmt.setDouble(1,
				 * CommonMethods.fmtToTwoDecimal(retNet) - cancellationCharge);
				 * pstmt.setInt(2, userInfoBean.getUserOrgId());
				 * pstmt.executeUpdate(); logger.debug("pstmt ret " + pstmt);
				 */

				
				//Now make payment updte method only one
				isValid=OrgCreditUpdation.updateOrganizationBalWithValidate(CommonMethods
						.fmtToTwoDecimal(retNet), "CLAIM_BAL", "DEBIT", userInfoBean.getUserOrgId(), userInfoBean.getParentOrgId(), "RETAILER", 0, con);
				if(!isValid)
					return -1;
				isValid=OrgCreditUpdation.updateOrganizationBalWithValidate(CommonMethods
						.fmtToTwoDecimal(agtNet), "CLAIM_BAL", "DEBIT", userInfoBean.getParentOrgId(),0, "AGENT", 0, con);
				if(!isValid)
					return -1;
				
				/*CommonFunctionsHelper commHelper = new CommonFunctionsHelper();
				commHelper.updateOrgBalance("CLAIM_BAL", CommonMethods
						.fmtToTwoDecimal(retNet), userInfoBean.getUserOrgId(),
						"DEBIT", con);

				// update st_lms_organization_master for claimable balance for
				// agent
				commHelper.updateOrgBalance("CLAIM_BAL", CommonMethods
						.fmtToTwoDecimal(agtNet),
						userInfoBean.getParentOrgId(), "DEBIT", con);*/

				/*
				 * // update st_lms_organization_master for claimable balance
				 * for agent pstmt = con .prepareStatement("update
				 * st_lms_organization_master set claimable_bal=claimable_bal-?
				 * where organization_id=?"); pstmt.setDouble(1,
				 * CommonMethods.fmtToTwoDecimal(agtNet) - cancellationCharge);
				 * pstmt.setInt(2, userInfoBean.getParentOrgId());
				 * pstmt.executeUpdate(); logger.debug("pstmt agt " + pstmt);
				 */
				// con.commit();
				return ticketMrp - cancellationCharge;

			} else {
				return -1;
			}

		} catch (Exception se) {
			se.printStackTrace();
			return -1;
		}

	}

	public static int drawTicketNPromoMappigUpdate(int promoId,
			int parentGameNo, String parentTicketNo, String promoTktNo) {

		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			con = DBConnect.getConnection();
			con.setAutoCommit(false);

			// update retaler draw sale table for ticket number
			pstmt = con
					.prepareStatement("insert into ge_sale_promo_ticket_mapping (promo_id, sale_ticket_nbr, promo_ticket_nbr)values(?, ?, ?)");
			// pstmt.setInt(1, parentGameNo);
			pstmt.setInt(1, promoId);
			pstmt.setString(2, parentTicketNo);
			pstmt.setString(3, promoTktNo);
			int isUpdate = pstmt.executeUpdate();
			if (isUpdate == 1) {
				con.commit();
				return 1;
			} else {
				// cancelTicketTxnFailed();
				// drawTicketSaleRefund(userInfoBean,
				// game_nbr,"FAILED",refrenceId);
				return 0;
			}

		} catch (Exception se) {
			se.printStackTrace();
			return 0;
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

		}

	}

	// added by amit

	/**
	 * This method is used to refund in case of failed transaction draw ticket
	 * 
	 * @param userInfoBean
	 * @param gameNbr
	 * @param ticket_nbr
	 * @param type
	 *            (CANCEL or FAILED)
	 * @return 1 if successfully refunded or cancelled
	 * 
	 */

	// comments after reveiwing // drawTicketSaleRefund method is to updated for
	// the scenerio where ticket no is not generated
	// Use reference id for tracking the failed ticket sale
	// suggestion dont user underscore for naming variables
	public static int drawTicketSaleRefund(UserInfoBean userInfoBean,
			int gameId, String type, long refrenceId) {

		Connection con = null;
		PreparedStatement pstmt = null;

		ResultSet rsTrns = null;

		try {
			con = DBConnect.getConnection();
			con.setAutoCommit(false);

			// get the comm from draw game master table
			pstmt = con
					.prepareStatement("select * from st_dg_ret_sale_? where game_id=?  and transaction_id=?");
			pstmt.setInt(1, gameId);
			pstmt.setInt(2, gameId);
			pstmt.setLong(3, refrenceId);
			ResultSet ticketDetails = pstmt.executeQuery();
			double ticketMrp = 0.0;
			double retComm = 0.0;
			double agtComm = 0.0;
			double govt_comm = 0.0;
			double vatAmt = 0.0;
			double taxableSale = 0.0;
			double retNet = 0.0;
			double agtNet = 0.0;
			double retSdAmt = 0.0;
			double agtVatAmt = 0.0;
			String ticketNbr;

			//int gameId = 0;
			if (ticketDetails.next()) {
				ticketMrp = ticketDetails.getDouble("mrp_amt");
				retComm = ticketDetails.getDouble("retailer_comm");
				retNet = ticketDetails.getDouble("net_amt");
				agtComm = ticketDetails.getDouble("agent_comm");
				agtNet = ticketDetails.getDouble("agent_net_amt");
				govt_comm = ticketDetails.getDouble("good_cause_amt");
				vatAmt = ticketDetails.getDouble("vat_amt");
				taxableSale = ticketDetails.getDouble("taxable_sale");
				gameId = ticketDetails.getInt("game_id");
				ticketNbr = ticketDetails.getString("ticket_nbr");
				retSdAmt = ticketDetails.getDouble("ret_sd_amt");
				agtVatAmt = ticketDetails.getDouble("agt_vat_amt");
			} else {
				return 0;
			}

			// insert in main transaction table
			pstmt = con.prepareStatement(QueryManager
					.insertInLMSTransactionMaster());
			pstmt.setString(1, "RETAILER");
			pstmt.executeUpdate();
			rsTrns = pstmt.getGeneratedKeys();
			if (rsTrns.next()) {
				long transId = rsTrns.getLong(1);
				logger.debug("trans id " + transId);
				// insert into retailer transaction master
				pstmt = con
						.prepareStatement("INSERT INTO st_lms_retailer_transaction_master (transaction_id,retailer_user_id,retailer_org_id,game_id,transaction_date,transaction_type) VALUES (?,?,?,?,?,?)");
				pstmt.setLong(1, transId);
				pstmt.setInt(2, userInfoBean.getUserId()); // these are
				// retailers parent
				// id
				pstmt.setInt(3, userInfoBean.getUserOrgId());
				pstmt.setInt(4, gameId);
				pstmt.setTimestamp(5, new java.sql.Timestamp(new Date()
						.getTime()));
				if ("FAILED".equalsIgnoreCase(type)) {
					pstmt.setString(6, "DG_REFUND_FAILED");
				} else {
					pstmt.setString(6, "DG_REFUND_CANCEL");
				}
				pstmt.executeUpdate();

				// insert in ret draw sale table
				pstmt = con
						.prepareStatement("insert into st_dg_ret_sale_refund_?(transaction_id,mrp_amt,retailer_comm,net_amt,agent_comm,agent_net_amt,retailer_org_id,claim_status,good_cause_amt,vat_amt,taxable_sale,game_id,ticket_nbr,ref_transaction_id,ret_sd_amt,agt_vat_amt) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
				pstmt.setInt(1, gameId);
				pstmt.setLong(2, transId);
				pstmt.setDouble(3, ticketMrp);
				pstmt.setDouble(4, CommonMethods.fmtToTwoDecimal(retComm));
				pstmt.setDouble(5, CommonMethods.fmtToTwoDecimal(retNet));
				pstmt.setDouble(6, CommonMethods.fmtToTwoDecimal(agtComm));
				pstmt.setDouble(7, CommonMethods.fmtToTwoDecimal(agtNet));
				pstmt.setInt(8, userInfoBean.getUserOrgId());
				pstmt.setString(9, "CLAIM_BAL");
				pstmt.setDouble(10, CommonMethods.fmtToTwoDecimal(govt_comm));
				pstmt.setDouble(11, CommonMethods.fmtToTwoDecimal(vatAmt));
				pstmt.setDouble(12, CommonMethods.fmtToTwoDecimal(taxableSale));
				pstmt.setInt(13, gameId);
				pstmt.setString(14, ticketNbr);
				pstmt.setLong(15, refrenceId);
				pstmt.setDouble(16, CommonMethods.fmtToTwoDecimal(retSdAmt));
				pstmt.setDouble(17, CommonMethods.fmtToTwoDecimal(agtVatAmt));
				pstmt.executeUpdate();

				// update st_lms_organization_master for claimable balance for
				// retailer
				/*
				 * pstmt = con .prepareStatement("update
				 * st_lms_organization_master set claimable_bal=claimable_bal-?
				 * where organization_id=?"); pstmt.setDouble(1,
				 * CommonMethods.fmtToTwoDecimal(retNet)); pstmt.setInt(2,
				 * userInfoBean.getUserOrgId()); pstmt.executeUpdate();
				 * logger.debug("pstmt ret " + pstmt);
				 */
				
				//Now make payment updte method only one
				boolean isValid=false;
				isValid=OrgCreditUpdation.updateOrganizationBalWithValidate(CommonMethods
						.fmtToTwoDecimal(retNet), "CLAIM_BAL", "DEBIT", userInfoBean.getUserOrgId(), userInfoBean.getParentOrgId(), "RETAILER", 0, con);
				if(!isValid)
					return 0;
				isValid=OrgCreditUpdation.updateOrganizationBalWithValidate(CommonMethods
						.fmtToTwoDecimal(agtNet), "CLAIM_BAL", "DEBIT", userInfoBean.getParentOrgId(),0, "AGENT", 0, con);
			
				if(!isValid)
					return 0;
				/*CommonFunctionsHelper commHelper = new CommonFunctionsHelper();
				commHelper.updateOrgBalance("CLAIM_BAL", CommonMethods
						.fmtToTwoDecimal(retNet), userInfoBean.getUserOrgId(),
						"DEBIT", con);

				// update st_lms_organization_master for claimable balance for
				// agent
				commHelper.updateOrgBalance("CLAIM_BAL", CommonMethods
						.fmtToTwoDecimal(agtNet),
						userInfoBean.getParentOrgId(), "DEBIT", con);
*/
				/*
				 * pstmt = con .prepareStatement("update
				 * st_lms_organization_master set claimable_bal=claimable_bal-?
				 * where organization_id=?"); pstmt.setDouble(1, agtNet);
				 * pstmt.setInt(2, userInfoBean.getParentOrgId());
				 * pstmt.executeUpdate(); logger.debug("pstmt agt " + pstmt);
				 */

				con.commit();
				return 1;

			} else {
				return 0;
			}

		} catch (Exception se) {
			se.printStackTrace();
			return 0;
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

	}

	/*
	 * public static boolean isOrgBalanceAvaillable(UserInfoBean
	 * userInfoBean,int gameNbr,double ticketMrp) throws LMSException{
	 * 
	 * Connection con=null; PreparedStatement pstmt=null; ResultSet rsTrns=null;
	 * 
	 * try{ con = DBConnect.getConnection();
	 * 
	 * //get the comm from draw game master table
	 * 
	 * pstmt = con.prepareStatement("select * from st_draw_game_master where
	 * game_nbr="+gameNbr); ResultSet gameDetails = pstmt.executeQuery(); double
	 * retCommRate=0.0; double agtCommRate=0.0; while(gameDetails.next()){
	 * retCommRate = gameDetails.getDouble("ret_sale_comm_rate"); agtCommRate =
	 * gameDetails.getDouble("agt_sale_comm_rate"); }
	 * 
	 * 
	 * //check retailer available pstmt = con.prepareStatement("select
	 * (available_credit-claimable_bal) as availbale_sale_bal from
	 * st_lms_organization_master where organization_id=?");
	 * pstmt.setInt(1,userInfoBean.getUserOrgId()); rsTrns =
	 * pstmt.executeQuery(); if(rsTrns.next()){
	 * if(!(rsTrns.getDouble("availbale_sale_bal") >
	 * ticketMrp-(ticketMrp*retCommRate*.01))) return false;
	 * 
	 * }else throw new LMSException("trasaction not done");
	 * 
	 * //check agents available pstmt = con.prepareStatement("select
	 * (available_credit-claimable_bal) as availbale_sale_bal from
	 * st_lms_organization_master where organization_id=?");
	 * pstmt.setInt(1,userInfoBean.getParentOrgId()); rsTrns =
	 * pstmt.executeQuery(); if(rsTrns.next()){
	 * if(!(rsTrns.getDouble("availbale_sale_bal") >
	 * ticketMrp-(ticketMrp*agtCommRate*.01))) return false; }else throw new
	 * LMSException("trasaction not done");
	 * 
	 * return true;
	 * 
	 * }catch(SQLException se){ se.printStackTrace(); throw new LMSException(); } }
	 */

	/**
	 * This method is used to update ticket number if ticket is generated by
	 * draw engine successfully
	 * 
	 * @param refrenceId
	 * @param ticket_nbr
	 * @return integer value (0) if some error has occured or (1) if updated
	 *         successfully
	 * @throws LMSException
	 */

	public static int drawTicketSaleTicketUpdate(long refrenceId,
			String ticket_nbr, int gameId, UserInfoBean userInfoBean,String interFaceType,Connection con) {
		//logger.debug("inside ticket update Getting DGE:"+ ticket_nbr);
		
		PreparedStatement pstmt = null;
		String query=null;
		try {
			
			interFaceType="LMS_Terminal".equalsIgnoreCase(interFaceType) ? "TERMINAL" : "WEB";
			if(!"RAFFLE".equalsIgnoreCase(Util.getGameType( gameId)) && !"Zimlottothree".equalsIgnoreCase(Util.getGameName(gameId)) && !"Zimlottobonusfree".equalsIgnoreCase(Util.getGameName(gameId))&&!"Zimlottobonustwofree".equalsIgnoreCase(Util.getGameName(gameId))){
				if("TERMINAL".equals(interFaceType))
				 query = "update st_dg_last_sold_ticket set terminal_ticket_number = " + ticket_nbr + ",terminal_ticket_status='SOLD' where ret_org_id = " + userInfoBean.getUserOrgId();
				else
					 query = "update st_dg_last_sold_ticket set web_ticket_number = " + ticket_nbr + ",web_ticket_status='SOLD' where ret_org_id = " + userInfoBean.getUserOrgId();
					pstmt = con.prepareStatement(query);
				pstmt.executeUpdate();
			}
			//Here call the last sale an heart beat update in retoffiline master
			// update retaler draw sale table for ticket number			
			Util.setHeartBeatAndSaleTime(userInfoBean.getUserOrgId(),"SALE",con);
			pstmt = con
					.prepareStatement("update st_dg_ret_sale_? set ticket_nbr=? where transaction_id=?");
			pstmt.setInt(1, gameId);
			//pstmt.setInt(2, game_nbr);
			pstmt.setString(2, ticket_nbr);
			pstmt.setLong(3, refrenceId);
			int isUpdate = pstmt.executeUpdate();
			if (isUpdate == 1) {
				return 1;
			} else {
				// cancelTicketTxnFailed();
				// drawTicketSaleRefund(userInfoBean,
				// game_nbr,"FAILED",refrenceId);
				return 0;
			}

		} catch (Exception se) {
			// drawTicketSaleRefund(userInfoBean, game_nbr, "FAILED",
			// refrenceId);
			se.printStackTrace();
			return 0;
		} 

	}
	
	public static int drawPromoTicketSaleTicketUpdate(long refrenceId, String ticket_nbr, int gameId, UserInfoBean userInfoBean, String interFaceType, Connection con, Boolean isPromo) {
		//logger.debug("inside ticket update Getting DGE:"+ ticket_nbr);
		
		PreparedStatement pstmt = null;
		String query=null;
		try {
			
			interFaceType="LMS_Terminal".equalsIgnoreCase(interFaceType) ? "TERMINAL" : "WEB";
			if (!isPromo) {
				if("TERMINAL".equals(interFaceType))
				 query = "update st_dg_last_sold_ticket set terminal_ticket_number = " + ticket_nbr + ",terminal_ticket_status='SOLD' where ret_org_id = " + userInfoBean.getUserOrgId();
				else
					 query = "update st_dg_last_sold_ticket set web_ticket_number = " + ticket_nbr + ",web_ticket_status='SOLD' where ret_org_id = " + userInfoBean.getUserOrgId();
					pstmt = con.prepareStatement(query);
				pstmt.executeUpdate();
			}
			//Here call the last sale an heart beat update in retoffiline master
			// update retaler draw sale table for ticket number			
			Util.setHeartBeatAndSaleTime(userInfoBean.getUserOrgId(),"SALE",con);
			pstmt = con
					.prepareStatement("update st_dg_ret_sale_? set ticket_nbr=? where transaction_id=?");
			pstmt.setInt(1, gameId);
			//pstmt.setInt(2, game_nbr);
			pstmt.setString(2, ticket_nbr);
			pstmt.setLong(3, refrenceId);
			int isUpdate = pstmt.executeUpdate();
			if (isUpdate == 1) {
				return 1;
			} else {
				// cancelTicketTxnFailed();
				// drawTicketSaleRefund(userInfoBean,
				// game_nbr,"FAILED",refrenceId);
				return 0;
			}

		} catch (Exception se) {
			// drawTicketSaleRefund(userInfoBean, game_nbr, "FAILED",
			// refrenceId);
			se.printStackTrace();
			return 0;
		} 

	}

	public static double drawTicketTxCancel(CancelTicketBean cancelTicketBean,
			UserInfoBean userInfoBean, Connection con) {
		PreparedStatement pstmt = null;
		// 
		ResultSet rsTrns = null;
		long  saleRefTransId = Long.parseLong(cancelTicketBean.getRefTransId());
		try {

			int gameNbr = cancelTicketBean.getGameNo();

			// check for pwt status
			/*
			 * pstmt = con.prepareStatement("select ticket_nbr from
			 * st_dg_pwt_inv_? where ticket_nbr = ?"); pstmt.setInt(1,gameNbr);
			 * pstmt.setString(2,cancelTicketBean.getTicketNo()); ResultSet
			 * rsPwt = pstmt.executeQuery(); if(rsPwt.next()) return -1;
			 * 
			 * //check for already cancelled in refund table status pstmt =
			 * con.prepareStatement("select ticket_nbr from
			 * st_dg_ret_sale_refund_? where ticket_nbr=?");
			 * pstmt.setInt(1,gameNbr);
			 * pstmt.setString(2,cancelTicketBean.getTicketNo()); rsPwt =
			 * pstmt.executeQuery(); if(rsPwt.next()) return -1;
			 */// commented by amit because not applicable in this
			// case
			// get the comm from draw game master table
			pstmt = con
					.prepareStatement("select * from st_dg_ret_sale_? where transaction_id=? and game_id=?");
			pstmt.setInt(1, cancelTicketBean.getGameId());
			pstmt.setLong(2, saleRefTransId);
			pstmt.setInt(3, cancelTicketBean.getGameId());
			ResultSet ticketDetails = pstmt.executeQuery();
			double ticketMrp = 0.0;
			double retComm = 0.0;
			double agtComm = 0.0;
			double govt_comm = 0.0;
			double vatAmt = 0.0;
			double taxableSale = 0.0;
			double retNet = 0.0;
			double agtNet = 0.0;
			double retSdAmt = 0.0;
			double agtVatAmt = 0.0;
			int gameId = 0;
			if (ticketDetails.next()) {
				ticketMrp = ticketDetails.getDouble("mrp_amt");
				retComm = ticketDetails.getDouble("retailer_comm");
				retNet = ticketDetails.getDouble("net_amt");
				agtComm = ticketDetails.getDouble("agent_comm");
				agtNet = ticketDetails.getDouble("agent_net_amt");
				govt_comm = ticketDetails.getDouble("good_cause_amt");
				vatAmt = ticketDetails.getDouble("vat_amt");
				taxableSale = ticketDetails.getDouble("taxable_sale");
				gameId = ticketDetails.getInt("game_id");
				retSdAmt = ticketDetails.getDouble("ret_sd_amt");
				agtVatAmt = ticketDetails.getDouble("agt_vat_amt");
			} else {
				return -1;
			}
			// double cancellationCharge = ticketMrp*.01*cancellationChargePer;
			// logger.debug(ticketMrp+"*******Inside
			// Cancellation*********"+cancellationChargePer+"********"+cancellationCharge);
			// insert in main transaction table
			pstmt = con.prepareStatement(QueryManager
					.insertInLMSTransactionMaster());
			pstmt.setString(1, "RETAILER");
			pstmt.executeUpdate();
			rsTrns = pstmt.getGeneratedKeys();
			if (rsTrns.next()) {
				long transId = rsTrns.getLong(1);
				cancelTicketBean.setRefTransId(transId + "");
				logger.debug("trans id " + transId);
				// insert into retailer transaction master
				pstmt = con
						.prepareStatement("INSERT INTO st_lms_retailer_transaction_master (transaction_id,retailer_user_id,retailer_org_id,game_id,transaction_date,transaction_type) VALUES (?,?,?,?,?,?)");
				pstmt.setLong(1, transId);
				pstmt.setInt(2, userInfoBean.getUserId()); // these are
				// retailers parent
				// id
				pstmt.setInt(3, userInfoBean.getUserOrgId());
				pstmt.setInt(4, gameId);
				pstmt.setTimestamp(5, new java.sql.Timestamp(new Date()
						.getTime()));
				pstmt.setString(6, "DG_REFUND_FAILED");
				pstmt.executeUpdate();

				// insert in ret draw sale table
				pstmt = con
						.prepareStatement("insert into st_dg_ret_sale_refund_?(transaction_id,mrp_amt,retailer_comm,net_amt,agent_comm,agent_net_amt,retailer_org_id,claim_status,good_cause_amt,vat_amt,taxable_sale,game_id,cancellation_charges,ticket_nbr,ref_transaction_id,ret_sd_amt,agt_vat_amt) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
				pstmt.setInt(1, cancelTicketBean.getGameId());
				pstmt.setLong(2, transId);
				pstmt.setDouble(3, ticketMrp);
				pstmt.setDouble(4, CommonMethods.fmtToTwoDecimal(retComm));
				pstmt.setDouble(5, CommonMethods.fmtToTwoDecimal(retNet));
				pstmt.setDouble(6, CommonMethods.fmtToTwoDecimal(agtComm));
				pstmt.setDouble(7, CommonMethods.fmtToTwoDecimal(agtNet));
				pstmt.setInt(8, userInfoBean.getUserOrgId());
				pstmt.setString(9, "CLAIM_BAL");
				pstmt.setDouble(10, CommonMethods.fmtToTwoDecimal(govt_comm));
				pstmt.setDouble(11, CommonMethods.fmtToTwoDecimal(vatAmt));
				pstmt.setDouble(12, CommonMethods.fmtToTwoDecimal(taxableSale));
				pstmt.setInt(13, gameId);
				pstmt.setDouble(14, 0.0);// updated by amit because in this
				// case no cancellation charges
				// apply
				pstmt.setString(15, cancelTicketBean.getTicketNo());
				pstmt.setLong(16, saleRefTransId);
				pstmt.setDouble(17, CommonMethods.fmtToTwoDecimal(retSdAmt));
				pstmt.setDouble(18, CommonMethods.fmtToTwoDecimal(agtVatAmt));
				pstmt.executeUpdate();

				// update st_lms_organization_master for claimable balance for
				// retailer

				/*
				 * pstmt = con .prepareStatement("update
				 * st_lms_organization_master set claimable_bal=claimable_bal-?
				 * where organization_id=?"); pstmt.setDouble(1,
				 * CommonMethods.fmtToTwoDecimal(retNet)); pstmt.setInt(2,
				 * userInfoBean.getUserOrgId()); pstmt.executeUpdate();
				 * logger.debug("pstmt ret " + pstmt);
				 */

				boolean isValid=false;
				//Now make payment updte method only one
				isValid=OrgCreditUpdation.updateOrganizationBalWithValidate(CommonMethods
						.fmtToTwoDecimal(retNet), "CLAIM_BAL", "DEBIT", userInfoBean.getUserOrgId(), userInfoBean.getParentOrgId(), "RETAILER", 0, con);
				if(!isValid){
					return -1;
				}
				isValid=OrgCreditUpdation.updateOrganizationBalWithValidate(CommonMethods
						.fmtToTwoDecimal(agtNet), "CLAIM_BAL", "DEBIT", userInfoBean.getParentOrgId(),0, "AGENT", 0, con);
				if(!isValid){
					return -1;
				}
				
			/*	CommonFunctionsHelper commHelper = new CommonFunctionsHelper();
				commHelper.updateOrgBalance("CLAIM_BAL", CommonMethods
						.fmtToTwoDecimal(retNet), userInfoBean.getUserOrgId(),
						"DEBIT", con);

				// update st_lms_organization_master for claimable balance for
				// agent
				commHelper.updateOrgBalance("CLAIM_BAL", CommonMethods
						.fmtToTwoDecimal(agtNet),
						userInfoBean.getParentOrgId(), "DEBIT", con);*/

				/*
				 * // update st_lms_organization_master for claimable balance
				 * for agent pstmt = con .prepareStatement("update
				 * st_lms_organization_master set claimable_bal=claimable_bal-?
				 * where organization_id=?"); pstmt.setDouble(1,
				 * CommonMethods.fmtToTwoDecimal(agtNet)); pstmt.setInt(2,
				 * userInfoBean.getParentOrgId()); pstmt.executeUpdate();
				 * logger.debug("pstmt agt " + pstmt);
				 */
				// con.commit();
				return ticketMrp;

			} else {
				return -1;
			}

		} catch (Exception se) {
			se.printStackTrace();
			return -1;
		}

	}

	
	public static List<String> getAssociatedPromoTicket(String parentTktNo) {

		Connection con = null;
		PreparedStatement pstmt = null;

		List<String> promoTicketList = new ArrayList<String>();

		try {
			con = DBConnect.getConnection();
			con.setAutoCommit(false);

			// update retaler draw sale table for ticket number
			pstmt = con
					.prepareStatement("select promo_ticket_nbr from ge_sale_promo_ticket_mapping where sale_ticket_nbr=?");
			// pstmt.setInt(1, prentGameNo);
			pstmt.setString(1, parentTktNo);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				promoTicketList.add(rs.getString(1));
			}
		} catch (Exception se) {
			se.printStackTrace();
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

		}
		return promoTicketList;

	}
	public static List<String> getAssociatedPromoTicket(String parentTktNo,Connection con) {

		ResultSet rs=null; 
		PreparedStatement pstmt = null;
		List<String> promoTicketList = null;
		try {
			pstmt = con.prepareStatement("select promo_ticket_nbr from ge_sale_promo_ticket_mapping where sale_ticket_nbr=?");
			pstmt.setString(1, parentTktNo);
			rs = pstmt.executeQuery();
			promoTicketList = new ArrayList<String>();
			while (rs.next()) {
				promoTicketList.add(rs.getString(1));
			}
		} catch (Exception se) {
			se.printStackTrace();
		} finally{
			DBConnect.closePstmt(pstmt);
			DBConnect.closeRs(rs);
		}
		return promoTicketList;

	}
	public static void main(String[] args) throws LMSException {
		orgOnLineSaleCreditUpdation s = new orgOnLineSaleCreditUpdation();
		logger.debug("start");
		UserInfoBean u = new UserInfoBean();
		u.setUserId(536);
		u.setUserOrgId(164);
		u.setParentOrgId(162);
		// int t = s.drawTcketSaleBalDeduction(u,102,500);
		// s.drawTicketSaleTicketUpdate(t, 1414, 102);
		// s.drawTicketSaleRefund(u, 102, 1414, "");
		// logger.debug(s.drawTicketSale(u,102,"105-001001-002",400));
		logger.debug("success");
	}

}