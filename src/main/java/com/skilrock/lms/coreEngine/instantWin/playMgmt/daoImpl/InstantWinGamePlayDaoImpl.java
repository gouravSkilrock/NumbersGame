package com.skilrock.lms.coreEngine.instantWin.playMgmt.daoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
import com.skilrock.lms.coreEngine.instantWin.common.IWErrors;
import com.skilrock.lms.coreEngine.instantWin.common.IWException;
import com.skilrock.lms.rest.services.bean.TPTxRequestBean;
import com.skilrock.lms.web.drawGames.common.Util;

public class InstantWinGamePlayDaoImpl {
	private static Logger logger = LoggerFactory.getLogger(InstantWinGamePlayDaoImpl.class);

	public static synchronized long instantWinPurchaseTicketDaoImpl(TPTxRequestBean gamePlayBean, UserInfoBean userBean, Connection con) throws IWException, LMSException, SQLException {
		logger.info("inside instantWinPurchaseTicketDaoImpl...");
		Statement stmt = null;
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		PreparedStatement insertPstmt = null;
		ResultSet insertRs = null;
		ResultSet rsTrns = null;
		long transId = 0;
		try {
			double vat = 0.0;
			double govt_comm = 0.0;
			double retCommRate = 0.0;
			double agtCommRate = 0.0;
			double prize_payout_ratio = 0.0;
			double ticketMrp = gamePlayBean.getTxAmount();
			boolean isValid = false;
			
			stmt = con.createStatement();
			
			rs = stmt.executeQuery("select SQL_CACHE vat_amt, gov_comm_rate, prize_payout_ratio from st_iw_game_master where game_id = " + gamePlayBean.getGameId());
			if (rs.next()) {
				govt_comm = rs.getDouble("gov_comm_rate");
				vat = rs.getDouble("vat_amt");
				prize_payout_ratio = rs.getDouble("prize_payout_ratio");
			} else {
				throw new IWException(IWErrors.GAME_NOT_AVAILABLE_ERROR_CODE, IWErrors.GAME_NOT_AVAILABLE_ERROR_MESSAGE);
			}

//			GameMasterLMSBean gameMasterLMSBean = Util.getIWGameMasterLMSBean(gamePlayBean.getGameId());
//			if (gameMasterLMSBean != null) {
//				govt_comm = gameMasterLMSBean.getGovtComm();
//				vat = gameMasterLMSBean.getVatAmount();
//				prize_payout_ratio = gameMasterLMSBean.getPrizePayoutRatio();
//			} else {
//				throw new IWException(IWErrors.INVALID_USER_NAME_CODE, IWErrors.INVALID_USER_NAME_MESSAGE);
//			}

			retCommRate = Util.getIWSaleCommVariance(userBean.getUserOrgId(), gamePlayBean.getGameId());
			agtCommRate = Util.getIWSaleCommVariance(userBean.getParentOrgId(), gamePlayBean.getGameId());

			if (gamePlayBean.getTxAmount() > 0) {
				// check with ACA
				// if online sale amt > ACA then return ERROR
				pstmt = con.prepareStatement("select (available_credit-claimable_bal) as availbale_sale_bal, organization_status from st_lms_organization_master where organization_id=?");
				pstmt.setInt(1, userBean.getUserOrgId());
				rsTrns = pstmt.executeQuery();
				if (rsTrns.next()) {
					if (!"ACTIVE".equals(rsTrns.getString("organization_status"))) {
						logger.info(IWErrors.INACTIVE_RETAILER_ERROR_MESSAGE);
						throw new IWException(IWErrors.INACTIVE_RETAILER_ERROR_CODE, IWErrors.INACTIVE_RETAILER_ERROR_MESSAGE);
					}
					if (!(rsTrns.getDouble("availbale_sale_bal") >= ticketMrp - ticketMrp * retCommRate * .01)) {
						logger.info(IWErrors.RETAILER_BALANCE_INSUFFICIENT_ERROR_MESSAGE);
						throw new IWException(IWErrors.RETAILER_BALANCE_INSUFFICIENT_ERROR_CODE, IWErrors.RETAILER_BALANCE_INSUFFICIENT_ERROR_MESSAGE);
					}
				} else {
					throw new IWException(IWErrors.INVALID_USER_NAME_CODE, IWErrors.INVALID_USER_NAME_MESSAGE);
				}

				// check for agents ACA and claimable balance
				if (!"GHANA".equalsIgnoreCase(Utility.getPropertyValue("COUNTRY_DEPLOYED"))) {
					pstmt = con
							.prepareStatement("select (available_credit-claimable_bal) as availbale_sale_bal, organization_status from st_lms_organization_master where organization_id=?");
					pstmt.setInt(1, userBean.getParentOrgId());
					rsTrns = pstmt.executeQuery();
					// check for >= ?? needs to be confirmed
					if (rsTrns.next()) {
						if (!"ACTIVE".equals(rsTrns.getString("organization_status"))) {
							logger.info(IWErrors.INACTIVE_AGENT_ERROR_MESSAGE);
							throw new IWException(IWErrors.INACTIVE_AGENT_ERROR_CODE, IWErrors.INACTIVE_AGENT_ERROR_MESSAGE);
						}

						if (!(rsTrns.getDouble("availbale_sale_bal") >= ticketMrp - ticketMrp * agtCommRate * .01)) {
							logger.info(IWErrors.AGENT_BALANCE_INSUFFICIENT_ERROR_MESSAGE);
							throw new IWException(IWErrors.AGENT_BALANCE_INSUFFICIENT_ERROR_CODE, IWErrors.AGENT_BALANCE_INSUFFICIENT_ERROR_MESSAGE);
						}
					} else {
						throw new IWException(IWErrors.INVALID_USER_NAME_CODE, IWErrors.INVALID_USER_NAME_MESSAGE);
					}
				}
			}

			insertPstmt = con.prepareStatement("INSERT INTO st_lms_transaction_master (user_type, service_code, interface) VALUES (?,?,?)", PreparedStatement.RETURN_GENERATED_KEYS);
			insertPstmt.setString(1, userBean.getUserType());
			insertPstmt.setString(2, gamePlayBean.getServiceCode());
			insertPstmt.setString(3, gamePlayBean.getInterfaceType());
			insertPstmt.executeUpdate();
			insertRs = insertPstmt.getGeneratedKeys();

			if (insertRs.next()) {
				transId = insertRs.getLong(1);

				double saleCommRate = 0.0;
				double goodCauseAmt = 0.0;

				// calculate vat
				double vatAmount = CommonMethods.calculateDrawGameVatPlr(ticketMrp, saleCommRate, prize_payout_ratio, govt_comm, vat);
				double taxableSale = CommonMethods.calTaxableSale(ticketMrp, saleCommRate, prize_payout_ratio, govt_comm, vat);

				double retNet = CommonMethods.fmtToTwoDecimal(ticketMrp) - CommonMethods.fmtToTwoDecimal(ticketMrp * retCommRate * .01);
				double agtNet = CommonMethods.fmtToTwoDecimal(ticketMrp) - CommonMethods.fmtToTwoDecimal(ticketMrp * agtCommRate * .01);
				
				goodCauseAmt = CommonMethods.fmtToTwoDecimal(ticketMrp * govt_comm * .01);

				// insert into retailer transaction master
				insertPstmt = con.prepareStatement("INSERT INTO st_lms_retailer_transaction_master (transaction_id,retailer_user_id,retailer_org_id,game_id,transaction_date,transaction_type) VALUES (?,?,?,?,?,?)");
				insertPstmt.setLong(1, transId);
				insertPstmt.setInt(2, userBean.getUserId());
				insertPstmt.setInt(3, userBean.getUserOrgId());
				insertPstmt.setInt(4, gamePlayBean.getGameId());
//				insertPstmt.setInt(4, gamePlayBean.getGameTypeId());
				insertPstmt.setTimestamp(5, Util.getCurrentTimeStamp());
				insertPstmt.setString(6, "IW_SALE");
				insertPstmt.executeUpdate();

				insertPstmt = con.prepareStatement("insert into st_iw_ret_sale(transaction_id, engine_tx_id, game_id, retailer_org_id, ticket_nbr, mrp_amt, retailer_comm_amt, retailer_net_amt, agent_comm_amt, agent_net_amt, good_cause_amt, vat_amt, taxable_sale, player_mob_number, claim_status, transaction_date, is_cancel) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

				insertPstmt.setLong(1, transId);
				insertPstmt.setString(2, gamePlayBean.getIwEngineTxId());
				insertPstmt.setInt(3, gamePlayBean.getGameId());
				insertPstmt.setInt(4, userBean.getUserOrgId());
				insertPstmt.setString(5, gamePlayBean.getTicketNumber());
				insertPstmt.setDouble(6, CommonMethods.fmtToTwoDecimal(ticketMrp));
				insertPstmt.setDouble(7, CommonMethods.fmtToTwoDecimal(ticketMrp * retCommRate * .01));
				insertPstmt.setDouble(8, CommonMethods.fmtToTwoDecimal(retNet));
				insertPstmt.setDouble(9, CommonMethods.fmtToTwoDecimal(ticketMrp * agtCommRate * .01));
				insertPstmt.setDouble(10, CommonMethods.fmtToTwoDecimal(agtNet));
				insertPstmt.setDouble(11, CommonMethods.fmtToTwoDecimal(goodCauseAmt));
				insertPstmt.setDouble(12, CommonMethods.fmtToTwoDecimal(vatAmount));
				insertPstmt.setDouble(13, CommonMethods.fmtToTwoDecimal(taxableSale));
				insertPstmt.setString(14, gamePlayBean.getPlrMobileNumber());
				insertPstmt.setString(15, "DONE_CLAIM");
				insertPstmt.setTimestamp(16, Util.getCurrentTimeStamp());
				insertPstmt.setString(17, "N");
				insertPstmt.executeUpdate();

				// Now make payment update method only one
				isValid = OrgCreditUpdation.updateOrganizationBalWithValidate(retNet, "TRANSACTION", "IW_SALE", userBean.getUserOrgId(), userBean.getParentOrgId(), "RETAILER", 0, con);
				if (!isValid) {
					throw new IWException(IWErrors.RETAILER_BALANCE_INSUFFICIENT_ERROR_CODE, IWErrors.RETAILER_BALANCE_INSUFFICIENT_ERROR_MESSAGE);
				}
				isValid = OrgCreditUpdation.updateOrganizationBalWithValidate(agtNet, "TRANSACTION", "IW_SALE", userBean.getParentOrgId(), 0, "AGENT", 0, con);
				if (!isValid) {
					throw new IWException(IWErrors.AGENT_BALANCE_INSUFFICIENT_ERROR_CODE, IWErrors.AGENT_BALANCE_INSUFFICIENT_ERROR_MESSAGE);
				}
			} else {
				throw new IWException(IWErrors.GENERAL_EXCEPTION_ERROR_CODE, IWErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
			}
		} finally {
			DBConnect.closeConnection(insertPstmt, rsTrns);
			DBConnect.closeConnection(pstmt, insertRs);
		}
		return transId;
	}

	public static synchronized long refundPurchaseTicket(TPTxRequestBean gamePlayBean, UserInfoBean userInfoBean, Connection con) throws IWException {
		logger.debug("inside cancellation Cancel Transaction:: " + gamePlayBean.getTicketNumber());
		long transId = 0;

		ResultSet rs = null;
		PreparedStatement pstmt = null;
		try {
//			String ticketNumber = gamePlayBean.getTicketNumber();
//			String ticketIdDB = ticketNumber.substring(0, ticketNumber.length() - 1);

			String ticketIdDB = gamePlayBean.getTicketNumber();

			int gameId = gamePlayBean.getGameId();

			// check for pwt status
			pstmt = con.prepareStatement("select ticket_nbr from st_iw_pwt_inv where ticket_nbr = ?");
			pstmt.setString(1, ticketIdDB);
			ResultSet rsPwt = pstmt.executeQuery();
			if (rsPwt.next()) {
				logger.info("ticket present in st_iw_pwt_inv");
				throw new IWException(IWErrors.CANCEL_INVALID_ERROR_CODE, IWErrors.CANCEL_INVALID_ERROR_MESSAGE);
			}

			// check for already cancelled in refund table status
			pstmt = con.prepareStatement("select ticket_nbr from st_iw_ret_sale_refund where ticket_nbr=?");
			pstmt.setString(1, ticketIdDB);
			rsPwt = pstmt.executeQuery();
			if (rsPwt.next()) {
				logger.info("ticket present in st_iw_ret_sale_refund");
				throw new IWException(IWErrors.CANCEL_INVALID_ERROR_CODE, IWErrors.CANCEL_INVALID_ERROR_MESSAGE);
			}

			// get the commission from sale table on which this ticket was sold
			pstmt = con.prepareStatement("select * from st_iw_ret_sale where ticket_nbr=? and game_id=?");
			pstmt.setString(1, ticketIdDB);
			pstmt.setInt(2, gameId);

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
			boolean isValid = false;
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
				refTranId = ticketDetails.getLong("transaction_id");
			} else {
				logger.info("ticket not present in st_iw_ret_sale");
				throw new IWException(IWErrors.CANCEL_INVALID_ERROR_CODE, IWErrors.CANCEL_INVALID_ERROR_MESSAGE);
			}

			Timestamp txDataTime = new java.sql.Timestamp(new Date().getTime());
			double iwTktCancelCharges = (Double.parseDouble(Utility.getPropertyValue("IW_TKT_CANCELLATION_CHARGES")));
			double cancellationCharge = (ticketMrp * .01 * iwTktCancelCharges);
			logger.debug("{}*******Inside Cancellation*********{}*********{}", ticketMrp, iwTktCancelCharges, cancellationCharge);

			// subtract cancel amount from sale in responsible Gaming
			// ResponsibleGaming.respGaming(userInfoBean, "IW_CANCEL_AMOUNT",String.valueOf(ticketMrp), con);

			// insert in main transaction table
			pstmt = con.prepareStatement("INSERT INTO st_lms_transaction_master (user_type, service_code, interface) VALUES (?,?,?)", PreparedStatement.RETURN_GENERATED_KEYS);
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
				pstmt.setInt(4, gameId);
				pstmt.setTimestamp(5, txDataTime);
				pstmt.setString(6, "IW_REFUND_CANCEL");
				pstmt.executeUpdate();
				pstmt.clearParameters();

				// Mark Ticket as cancel in sale table
				pstmt = con.prepareStatement("update st_iw_ret_sale set is_cancel = 'Y' where ticket_nbr = ?");
				pstmt.setString(1, ticketIdDB);
				pstmt.executeUpdate();
				pstmt.clearParameters();

				// insert in retailer draw sale table
				// pstmt = con.prepareStatement("insert into st_iw_ret_sale_refund(transaction_id,mrp_amt,retailer_comm,net_amt,agent_comm,agent_net_amt,retailer_org_id,claim_status,good_cause_amt,vat_amt,taxable_sale,game_id,cancellation_charges,ticket_nbr,ref_transaction_id) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

				pstmt = con.prepareStatement("insert into st_iw_ret_sale_refund(transaction_id, engine_tx_id, game_id, retailer_org_id, ticket_nbr, mrp_amt, retailer_comm_amt, retailer_net_amt, agent_comm_amt, agent_net_amt, good_cause_amt, vat_amt, taxable_sale, cancellation_charges, claim_status, sale_ref_transaction_id, transaction_date) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

				pstmt.setLong(1, transId);
				pstmt.setString(2, gamePlayBean.getIwEngineTxId());
				pstmt.setInt(3, gameId);
				pstmt.setInt(4, userInfoBean.getUserOrgId());
				pstmt.setString(5, ticketIdDB);
				pstmt.setDouble(6, CommonMethods.fmtToTwoDecimal(ticketMrp));
				pstmt.setDouble(7, CommonMethods.fmtToTwoDecimal(retComm));
				pstmt.setDouble(8, CommonMethods.fmtToTwoDecimal(retNet) - cancellationCharge);
				pstmt.setDouble(9, CommonMethods.fmtToTwoDecimal(agtComm));
				pstmt.setDouble(10, CommonMethods.fmtToTwoDecimal(agtNet) - cancellationCharge);
				pstmt.setDouble(11, CommonMethods.fmtToTwoDecimal(goodCause));
				pstmt.setDouble(12, CommonMethods.fmtToTwoDecimal(vatAmt));
				pstmt.setDouble(13, CommonMethods.fmtToTwoDecimal(taxableSale));
				pstmt.setDouble(14, cancellationCharge);
				pstmt.setString(15, "DONE_CLAIM");
				pstmt.setLong(16, refTranId);
				pstmt.setTimestamp(17, txDataTime);
				pstmt.executeUpdate();

				isValid = OrgCreditUpdation.updateOrganizationBalWithValidate(CommonMethods.fmtToTwoDecimal(retNet), "TRANSACTION", "IW_CANCEL", userInfoBean.getUserOrgId(), userInfoBean.getParentOrgId(), "RETAILER", 0, con);
				if (!isValid) {
					throw new IWException(IWErrors.CANCEL_INVALID_ERROR_CODE, IWErrors.CANCEL_INVALID_ERROR_MESSAGE);
				}

				isValid = OrgCreditUpdation.updateOrganizationBalWithValidate(CommonMethods.fmtToTwoDecimal(agtNet), "TRANSACTION", "IW_CANCEL", userInfoBean.getParentOrgId(), 0, "AGENT", 0, con);
				if (!isValid) {
					throw new IWException(IWErrors.CANCEL_INVALID_ERROR_CODE, IWErrors.CANCEL_INVALID_ERROR_MESSAGE);
				}
			} else {
				throw new IWException(IWErrors.CANCEL_INVALID_ERROR_CODE, IWErrors.CANCEL_INVALID_ERROR_MESSAGE);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new IWException(IWErrors.SQL_EXCEPTION_ERROR_CODE, IWErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (IWException se) {
			throw se;
		} catch (Exception e) {
			e.printStackTrace();
			throw new IWException(IWErrors.GENERAL_EXCEPTION_ERROR_CODE, IWErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeConnection(pstmt, rs);
		}
		return transId;
	}

	public static synchronized int updateRgForTicketReprint(UserInfoBean userInfoBean, TPTxRequestBean tpTransactionBean, Connection con) throws IWException {
		// long lmsTxId = 0;
		int errorCode = 0;
		// ResultSet rs = null;
		// Statement stmt = null;
		try {
			// CHECK IF LAST TX SALE ???
			/*
			 * String query =
			 * "select transaction_id from st_iw_ret_sale where ticket_nbr ="
			 * +tpTransactionBean
			 * .getTicketNumber()+" and game_id ="+tpTransactionBean
			 * .getGameId()+" and game_type_id  = "+
			 * tpTransactionBean.getGameTypeId(); stmt = con.createStatement();
			 * rs = stmt.executeQuery(query); if(rs.next()){ lmsTxId =
			 * rs.getLong("transaction_id"); }else{ throw new IWException(); //
			 * REPRINT ERROR INVALID TICKET }
			 * 
			 * // CHECK IF IT HAS NOT BEEN CANCELLED query =
			 * "select transaction_id from st_iw_ret_sale_refund where sale_ref_transaction_id = "
			 * +lmsTxId ; rs = stmt.executeQuery(query); if(rs.next()){ lmsTxId
			 * = rs.getLong("transaction_id"); }else{ throw new IWException();
			 * // CANCELLED TICKET }
			 */

			con.setAutoCommit(false);
			boolean isFraud = ResponsibleGaming.respGaming(userInfoBean, "IW_REPRINT", "1", con); // REPRINT TIKCET LIMIT
			if (isFraud) {
				errorCode = LMSErrors.RG_LIMIT_EXCEPTION_ERROR_CODE;
			} else {
				con.commit();
			}
		} catch (Exception e) {
			errorCode = IWErrors.GENERAL_EXCEPTION_ERROR_CODE;
		}
		return errorCode;
	}

	public static synchronized int updateRGForInvalidPWT(UserInfoBean userInfoBean, TPTxRequestBean tpTransactionBean, Connection con) throws IWException {
		int errorCode = 0;
		try {
			con.setAutoCommit(false);
			boolean isFraud = ResponsibleGaming.respGaming(userInfoBean, "IW_INVALID_PWT", "1", con); // INVALID TIKCET LIMIT
			if (isFraud) {
				errorCode = LMSErrors.RG_LIMIT_EXCEPTION_ERROR_CODE;
			} else {
				con.commit();
			}
		} catch (Exception e) {
			errorCode = IWErrors.GENERAL_EXCEPTION_ERROR_CODE;
		}
		return errorCode;
	}

	public static synchronized void updatePurchaseTicket(long ticketNumber, long transId, int gameId, int gameTypeId, Connection con) throws IWException {
		PreparedStatement updatePstmt = null;
		try {
			updatePstmt = con.prepareStatement("update st_iw_ret_sale_? set ticket_nbr=? where transaction_id=? and game_id=? and game_type_id=?");
			updatePstmt.setInt(1, gameId);
			updatePstmt.setLong(2, ticketNumber);
			updatePstmt.setLong(3, transId);
			updatePstmt.setInt(4, gameId);
			updatePstmt.setInt(5, gameTypeId);
			updatePstmt.executeUpdate();
		} catch (SQLException e) {
			throw new IWException(IWErrors.SQL_EXCEPTION_ERROR_CODE, IWErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			throw new IWException(IWErrors.GENERAL_EXCEPTION_ERROR_CODE, IWErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
	}

}
