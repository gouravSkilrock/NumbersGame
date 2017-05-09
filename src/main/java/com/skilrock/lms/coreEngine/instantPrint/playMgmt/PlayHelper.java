package com.skilrock.lms.coreEngine.instantPrint.playMgmt;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.skilrock.ipe.Bean.TicketPurchaseLMSBean;
import com.skilrock.ipe.instantprint.IInstantPrint;
import com.skilrock.ipe.instantprint.InstantPrint;
import com.skilrock.ipe.instantprint.TicketPurchaseBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.coreEngine.instantPrint.beans.GameInfoBean;
import com.skilrock.lms.coreEngine.instantPrint.common.CommonMethods;
import com.skilrock.lms.coreEngine.instantPrint.common.IPEUtility;


public class PlayHelper {

	public TicketPurchaseLMSBean purchaseTicketProcess(
			TicketPurchaseLMSBean ticketLMSBean, UserInfoBean userBean) {
		Connection con = null;
		PreparedStatement pstmt = null;
		InstantPrint service = new InstantPrint();
		IInstantPrint portType = service.getInstantPrintServicePort();
		int gameNo = IPEUtility.getGameNo(ticketLMSBean.getGameId());
		ticketLMSBean.setGameNo(gameNo);
		ticketLMSBean.setGameName(IPEUtility.getGameName(gameNo));
		double totPurAmt = IPEUtility.getGameTktPrice(gameNo);
		ticketLMSBean.setTotalAmt(totPurAmt);
		TicketPurchaseBean ticketBean= new TicketPurchaseBean();
		ticketBean.setUserId(userBean.getUserId());
		ticketBean.setPartyId(userBean.getUserOrgId());
		ticketBean.setPartyType(userBean.getUserType());
		ticketBean.setGameId(ticketLMSBean.getGameId());
		ticketBean.setGameNo(ticketLMSBean.getGameNo());
		ticketBean.setPurChannel(ticketLMSBean.getPurChannel());
		ticketBean.setRefMerId(ticketLMSBean.getRefMerId());
		ticketBean.setPurchaseTime(ticketLMSBean.getPurchaseTime().getTime());
		ticketBean.setTotalAmt(ticketLMSBean.getTotalAmt());
		ticketBean.setGameName(ticketLMSBean.getGameName());
		ticketBean=portType.savePurchaseTicket(ticketBean);
		ticketLMSBean.setSaleStatus("FAILED");
		

		boolean isFraud = false; // Code Here for Responsible Gaming
		if (!isFraud) {
			String status = ticketSaleBalDeduction(userBean, gameNo, ticketLMSBean);
			try {
				ticketLMSBean.setSaleStatus(status);
				if ("SUCCESS".equals(status)) {

					if (ticketBean.isIsSale()) {
						ticketLMSBean.setTicketNo(ticketBean.getTicketNo());
						ticketLMSBean.setVirnNo(ticketBean.getVirnNo());
						ticketLMSBean.setImgList(ticketBean.getImgList());
						ticketLMSBean.setPrizeCode(ticketBean.getPrizeCode());
						ticketLMSBean.setSale(ticketBean.isIsSale());
						
						/*ticketBean.setAdvMsg(getAdvMessage(userBean.getUserOrgId(),
								ticketBean.getGameNo(), "PLAYER", "SALE"));*/
						
						try {
							con = DBConnect.getConnection();
							con.setAutoCommit(false);
							pstmt = con
									.prepareStatement("update st_ipe_ret_sale_? set ticket_nbr=? where transaction_id=?");
							pstmt.setInt(1, gameNo);
							pstmt.setString(2, ticketLMSBean.getTicketNo());
							pstmt.setInt(3, ticketLMSBean.getRefTransId());
							int isUpdate = pstmt.executeUpdate();
							if (isUpdate == 1) {
								IPEUtility.updateLastIPETransId(userBean.getUserOrgId(),ticketLMSBean.getRefTransId());
								con.commit();
							} /*else {
								// Sale Refund Cause By LMS Fail.
								cancelTicketProcess(ticketBean, userBean,
										"LMS_SERVER", true);
							}*/
						} catch (SQLException e) {
							e.printStackTrace();
							// Sale Refund Cause By LMS Fail.
							/*cancelTicketProcess(ticketBean, userBean,
									"LMS_SERVER", true);*/
							return ticketLMSBean;
						} finally {
							DBConnect.closeCon(con);
						}
					} else {
						// Sale Refund Cause By IPE Fail.
						/*cancelTicketProcess(ticketBean, userBean, "IPE_SERVER",
								false);*/
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				// Sale Refund Cause By LMS Fail.
				// cancelTicketProcess(ticketBean, userBean, "LMS_SERVER", false);
				return ticketLMSBean;
			}
		} else {
			ticketLMSBean.setSaleStatus("FRAUD");
		}

		return ticketLMSBean;
	}

	public TicketPurchaseLMSBean cancelTicketManual(TicketPurchaseLMSBean ticketLMSBean,
			UserInfoBean userBean, String cancelCause) {
		Connection con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		String status = "FAILED";
		String ticketNo = ticketLMSBean.getTicketNo();
		try {
			int gameNo = Integer.parseInt(ticketNo.substring(0, 3));
			ticketLMSBean.setGameNo(gameNo);
			pstmt = con
					.prepareStatement("select game_id,transaction_id,mrp_amt from st_ipe_ret_sale_? where ticket_nbr=?");
			pstmt.setInt(1, gameNo);
			pstmt.setString(2, ticketNo);
			ResultSet ticketDetails = pstmt.executeQuery();
			if (ticketDetails.next()) {
				ticketLMSBean.setGameId(ticketDetails.getInt("game_id"));
				ticketLMSBean
						.setRefTransId(ticketDetails.getInt("transaction_id"));
				ticketLMSBean.setTotalAmt(ticketDetails.getDouble("mrp_amt"));
			} else {
				ticketLMSBean.setSaleStatus(status);
				return ticketLMSBean;
			}
			status = cancelTicketProcess(ticketLMSBean, userBean, cancelCause,
					true);
			ticketLMSBean.setSaleStatus(status);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnect.closeCon(con);
		}
		return ticketLMSBean;
	}

	public String cancelTicketProcess(TicketPurchaseLMSBean ticketLMSBean,
			UserInfoBean userBean, String cancelCause, boolean isUpdIPE) {
		String status = ticketRefundBalUpdation(ticketLMSBean, userBean,
				cancelCause);
		if ("SUCCESS".equals(status) && isUpdIPE) {
			TicketPurchaseBean ticketBean = new TicketPurchaseBean();
			ticketBean.setTicketNo(ticketLMSBean.getTicketNo());
			ticketBean.setRefMerId(ticketLMSBean.getRefMerId());
			ticketBean.setPurChannel(ticketLMSBean.getPurChannel());
			ticketBean.setGameNo(ticketLMSBean.getGameNo());
			ticketBean.setGameId(ticketLMSBean.getGameId());
			ticketBean.setRefTransId(ticketLMSBean.getRefTransId());
			ticketBean.setTotalAmt(ticketLMSBean.getTotalAmt());
			InstantPrint service = new InstantPrint();
			IInstantPrint portType = service.getInstantPrintServicePort();
			ticketBean =portType.cancelTicketProcess(ticketBean);
			if (!ticketBean.isIsSale()) {
				status = "FAILED";
			}
		}

		return status;
	}

	public static synchronized String ticketRefundBalUpdation(
			TicketPurchaseLMSBean ticketLMSBean, UserInfoBean userBean,
			String cancelCause) {
		Connection con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		int gameNo = ticketLMSBean.getGameNo();
		int gameId = ticketLMSBean.getGameId();
		double agtNetAmt = 0.0;
		double retNetAmt = 0.0;
		try {
			con.setAutoCommit(false);
			pstmt = con
					.prepareStatement("select ticket_nbr,mrp_amt,net_amt,agent_net_amt from st_ipe_ret_sale_? where transaction_id=? and game_id=?");
			pstmt.setInt(1, gameNo);
			pstmt.setInt(2, ticketLMSBean.getRefTransId());
			pstmt.setInt(3, gameId);
			ResultSet ticketDetails = pstmt.executeQuery();
			if (ticketDetails.next()) {
				retNetAmt = ticketDetails.getDouble("net_amt");
				agtNetAmt = ticketDetails.getDouble("agent_net_amt");
			} else {
				return "FAILED";
			}

			pstmt = con.prepareStatement(QueryManager
					.insertInLMSTransactionMaster());
			pstmt.setString(1, "RETAILER");
			pstmt.executeUpdate();
			ResultSet rsTrns = pstmt.getGeneratedKeys();
			if (rsTrns.next()) {
				int transId = rsTrns.getInt(1);
				ticketLMSBean.setPurchaseTime(new java.sql.Timestamp(new Date()
						.getTime()));
				pstmt = con
						.prepareStatement("INSERT INTO st_lms_retailer_transaction_master (transaction_id,retailer_user_id,retailer_org_id,game_id,transaction_date,transaction_type) VALUES (?,?,?,?,?,?)");
				pstmt.setInt(1, transId);
				pstmt.setInt(2, userBean.getUserId());
				pstmt.setInt(3, userBean.getUserOrgId());
				pstmt.setInt(4, gameId);
				pstmt.setTimestamp(5, ticketLMSBean.getPurchaseTime());
				pstmt.setString(6, "IPE_REFUND");
				pstmt.executeUpdate();

				pstmt = con
						.prepareStatement("insert into st_ipe_ret_sale_refund_?(transaction_id,retailer_org_id,game_id,ticket_nbr,mrp_amt,net_amt,ret_comm_rate,agt_comm_rate,agent_net_amt,good_cause_rate,agent_ref_transaction_id,vat_amt,taxable_sale,cancellation_charges,sale_ref_trans_id,claim_status,cancel_cause) select ?,retailer_org_id,game_id,ticket_nbr,mrp_amt,net_amt,ret_comm_rate,agt_comm_rate,agent_net_amt,good_cause_rate,agent_ref_transaction_id,vat_amt,taxable_sale,?,transaction_id,'CLAIM_BAL',? from st_ipe_ret_sale_? where transaction_id=? and game_id=?");
				pstmt.setInt(1, gameNo);
				pstmt.setInt(2, transId);
				pstmt.setDouble(3, 0.0);
				pstmt.setString(4, cancelCause);
				pstmt.setInt(5, gameNo);
				pstmt.setInt(6, ticketLMSBean.getRefTransId());
				pstmt.setInt(7, gameId);
				pstmt.executeUpdate();

				CommonMethods.updateOrgBalance("CLAIM_BAL", retNetAmt, userBean
						.getUserOrgId(), "DEBIT", con);
				CommonMethods.updateOrgBalance("CLAIM_BAL", agtNetAmt, userBean
						.getParentOrgId(), "DEBIT", con);

				ticketLMSBean.setRefTransId(transId); // Cancel Trans Id
			}
			con.commit();
		} catch (Exception e) {
			e.printStackTrace();
			return "FAILED";
		} finally {
			DBConnect.closeCon(con);
		}
		return "SUCCESS";
	}

	public static synchronized String ticketSaleBalDeduction(
			UserInfoBean userInfoBean, int gameNo,TicketPurchaseLMSBean ticketBean) {
		Connection con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rsTrns = null;
		int orgId = userInfoBean.getUserOrgId();
		try {
			con = DBConnect.getConnection();
			con.setAutoCommit(false);
			double ticketMrp = ticketBean.getTotalAmt();
			double retNetAmt = 0.0;
			double agtNetAmt = 0.0;
			double retCommRate = 0.0;
			double agtCommRate = 0.0;
			double govt_comm = 0.0;
			double vat = 0.0;
			double prize_payout_ratio = 0.0;
			int gameId = IPEUtility.getGameId(gameNo);
			GameInfoBean gameInfoBean = IPEUtility.fetchOrgGameComm(orgId, gameId);
			if (gameInfoBean != null) {
				retCommRate = gameInfoBean.getRetSaleComm();
				agtCommRate = gameInfoBean.getAgentSaleComm();
				govt_comm = gameInfoBean.getGovtComm();
				vat = gameInfoBean.getVatComm();
				prize_payout_ratio = gameInfoBean.getPpr();
			} else {
				return "FAILED";
			}

			retNetAmt = ticketMrp - (ticketMrp * retCommRate * 0.01);
			agtNetAmt = ticketMrp - (ticketMrp * agtCommRate * 0.01);

			// check for retailer ACA and claimable balance
			pstmt = con
					.prepareStatement("select (available_credit-claimable_bal) as availbale_sale_bal from st_lms_organization_master where organization_id=?");
			pstmt.setInt(1, userInfoBean.getUserOrgId());
			rsTrns = pstmt.executeQuery();
			if (rsTrns.next()) {
				if (rsTrns.getDouble("availbale_sale_bal") < retNetAmt) {
					return "RET_INS_BAL";
				}
			} else {
				return "FAILED";
			}

			// check for agents ACA and claimable balance
			pstmt = con
					.prepareStatement("select (available_credit-claimable_bal) as availbale_sale_bal from st_lms_organization_master where organization_id=?");
			pstmt.setInt(1, userInfoBean.getParentOrgId());
			rsTrns = pstmt.executeQuery();
			if (rsTrns.next()) {
				if (rsTrns.getDouble("availbale_sale_bal") < agtNetAmt) {
					return "AGT_INS_BAL";
				}
			} else {
				return "FAILED";
			}

			// insert in main transaction table
			pstmt = con.prepareStatement(QueryManager
					.insertInLMSTransactionMaster());
			pstmt.setString(1, "RETAILER");
			pstmt.executeUpdate();
			rsTrns = pstmt.getGeneratedKeys();
			if (rsTrns.next()) {
				int transId = rsTrns.getInt(1);
				ticketBean.setRefTransId(transId);
				// insert into retailer transaction master
				pstmt = con
						.prepareStatement("INSERT INTO st_lms_retailer_transaction_master (transaction_id,retailer_user_id,retailer_org_id,game_id,transaction_date,transaction_type) VALUES (?,?,?,?,?,?)");
				pstmt.setInt(1, transId);
				pstmt.setInt(2, userInfoBean.getUserId()); // these are
				// retailers parent
				// id
				pstmt.setInt(3, userInfoBean.getUserOrgId());
				pstmt.setInt(4, gameId);
				pstmt.setTimestamp(5, new java.sql.Timestamp(new Date()
						.getTime()));
				pstmt.setString(6, "IPE_SALE");
				pstmt.executeUpdate();

				double saleCommRate = 0.0;// This is agent or retailer sale
				// Comm Rate currently set to 0.
				// calculate vat
				double vatAmount = CommonMethods.calculateVatPlr(ticketMrp, 0,
						prize_payout_ratio, govt_comm, vat);
				double taxableSale = CommonMethods.calTaxableSale(ticketMrp,
						saleCommRate, prize_payout_ratio, govt_comm, vat);
				System.out.println("taxableSale   ======== " + taxableSale);

				double retNet = CommonMethods.fmtToTwoDecimal(retNetAmt);
				double agtNet = CommonMethods.fmtToTwoDecimal(agtNetAmt);

				// insert in ret draw sale table
				pstmt = con
						.prepareStatement("insert into st_ipe_ret_sale_?(transaction_id,retailer_org_id,game_id,mrp_amt,net_amt,ret_comm_rate,agt_comm_rate,agent_net_amt,claim_status,good_cause_rate,vat_amt,taxable_sale) values (?,?,?,?,?,?,?,?,'CLAIM_BAL',?,?,?)");
				pstmt.setInt(1, gameNo);
				pstmt.setInt(2, transId);
				pstmt.setInt(3, orgId);
				pstmt.setInt(4, gameId);
				pstmt.setDouble(5, ticketMrp);
				pstmt.setDouble(6, retNet);
				pstmt.setDouble(7, retCommRate);
				pstmt.setDouble(8, agtCommRate);
				pstmt.setDouble(9, agtNet);
				pstmt.setDouble(10, govt_comm);
				pstmt.setDouble(11, CommonMethods.fmtToTwoDecimal(vatAmount));
				pstmt.setDouble(12, CommonMethods.fmtToTwoDecimal(taxableSale));
				pstmt.executeUpdate();

				CommonMethods.updateOrgBalance("CLAIM_BAL", retNet,
						userInfoBean.getUserOrgId(), "CREDIT", con);

				// update st_lms_organization_master for claimable balance for
				// agent
				CommonMethods.updateOrgBalance("CLAIM_BAL", agtNet,
						userInfoBean.getParentOrgId(), "CREDIT", con);

				con.commit();

			} else {
				return "FAILED";
			}

		} catch (Exception se) {
			se.printStackTrace();
			return "FAILED";
		} finally {
			DBConnect.closeCon(con);
		}
		return "SUCCESS";
	}
	
	public Map<String, List<String>> getAdvMessage(int orgId, int gameId,
			String forOrgType, String activity) throws SQLException {
		Map<String, List<String>> msgMap = new HashMap<String, List<String>>();

		Connection con = DBConnect.getConnection();
		Statement drawStmt = con.createStatement();
		String msgLocation = null;
		String query = "select msg_text,org_id,game_id,activity,msg_location  from st_ipe_adv_msg_org_mapping mop,st_ipe_adv_msg_master mm where (org_id="
				+ orgId
				+ " or org_id=-1) and mm.msg_id = mop.msg_id and mm.status='ACTIVE'  and msg_for='"
				+ forOrgType + "'";
		StringBuilder whereClause = new StringBuilder("");
		if (gameId != 0) {
			whereClause.append(" and mop.game_id=" + gameId);
		}
		if (activity != null) {
			whereClause.append(" and (mm.activity='" + activity
					+ "' or mm.activity='ALL')");
		}
		System.out.println(query + whereClause);
		ResultSet retRs = drawStmt.executeQuery(query + whereClause);
		while (retRs.next()) {
			msgLocation = retRs.getString("msg_location");
			if (msgMap.containsKey(msgLocation)) {
				msgMap.get(msgLocation).add(retRs.getString("msg_text"));
			} else {
				List<String> tempList = new ArrayList<String>();
				tempList.add(retRs.getString("msg_text"));
				msgMap.put(retRs.getString("msg_location"), tempList);
			}
		}
		DBConnect.closeCon(con);
		return msgMap;
	}
	
	public TicketPurchaseLMSBean reprintLastTicket(TicketPurchaseLMSBean ticketLMSBean, UserInfoBean userBean){
		Connection con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		InstantPrint service = new InstantPrint();
		IInstantPrint portType = service.getInstantPrintServicePort();
		TicketPurchaseBean ticketBean= new TicketPurchaseBean();
		ticketBean.setUserId(userBean.getUserId());
		ticketBean.setPartyId(userBean.getUserOrgId());
		ticketBean.setPartyType(userBean.getUserType());
		ticketBean.setGameId(ticketLMSBean.getGameId());
		ticketBean.setPurChannel(ticketLMSBean.getPurChannel());
		ticketBean.setRefMerId(ticketLMSBean.getRefMerId());
		ticketBean.setRefTransId(ticketLMSBean.getRefTransId());
		ticketBean.setPurchaseTime(ticketLMSBean.getPurchaseTime().getTime());
		ticketBean.setGameNo(ticketLMSBean.getGameNo());
		
		try{
			String reprintQry = "select ticket_nbr,transaction_id from st_ipe_ret_sale_"+ ticketLMSBean.getGameNo() +" where transaction_id  = (select IPELastSaleTransId from st_lms_last_sale_transaction where organization_id = ?)";
			pstmt = con.prepareStatement(reprintQry);
			pstmt.setInt(1, userBean.getUserOrgId());
			rs = pstmt.executeQuery();
			if(rs.next()){
				ticketBean.setTicketNo(rs.getString("ticket_nbr"));
				ticketBean.setRefTransId(rs.getInt("transaction_id"));
				ticketBean=portType.reprintTicket(ticketBean);
				
			}
			if (ticketBean.isIsSale()) {
				ticketLMSBean.setTicketNo(ticketBean.getTicketNo());
				ticketLMSBean.setVirnNo(ticketBean.getVirnNo());
				ticketLMSBean.setImgList(ticketBean.getImgList());
				ticketLMSBean.setPrizeCode(ticketBean.getPrizeCode());
				//ticketLMSBean.setTicketNo(rs.getString("ticket_nbr"));
				ticketLMSBean.setRefTransId(rs.getInt("transaction_id"));
				ticketLMSBean.setSaleStatus(ticketBean.getSaleStatus());
						
			}
			pstmt.close();
			rs.close();
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		finally{
			DBConnect.closeCon(con);
		}
		return ticketLMSBean;
	}
	
	public int fetchGameId(int lastTransId){
		Connection con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int i = 0;
		try{
			String qry = "select game_id from st_lms_retailer_transaction_master where transaction_id  =  ?";
			pstmt = con.prepareStatement(qry);
			pstmt.setInt(1, lastTransId);
			rs = pstmt.executeQuery();
			if(rs.next()){
				i = rs.getInt("game_id");
			}
			return i;
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		finally{
			DBConnect.closeCon(con);
		}
		return 0;
	}
	
}
