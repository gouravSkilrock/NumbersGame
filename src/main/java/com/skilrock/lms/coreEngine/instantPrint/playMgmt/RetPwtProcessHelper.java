package com.skilrock.lms.coreEngine.instantPrint.playMgmt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import com.skilrock.ipe.Bean.PwtLMSBean;
import com.skilrock.ipe.instantprint.IInstantPrint;
import com.skilrock.ipe.instantprint.InstantPrint;
import com.skilrock.ipe.instantprint.PwtBean;
import com.skilrock.lms.beans.OrgPwtLimitBean;
import com.skilrock.lms.beans.TicketBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.instantPrint.beans.GameInfoBean;
import com.skilrock.lms.coreEngine.instantPrint.common.CommonMethods;
import com.skilrock.lms.coreEngine.instantPrint.common.IPEUtility;
import com.skilrock.lms.coreEngine.instantPrint.service.IServiceDelegate;
import com.skilrock.lms.coreEngine.instantPrint.service.ServiceDelegate;
import com.skilrock.lms.coreEngine.scratchService.pwtMgmt.common.CommonFunctionsHelper;

public class RetPwtProcessHelper {
/*public PwtBean payPwtTicket(UserInfoBean userInfoBean,
			String refMerchantId, String highPrizeCriteria,
			String highPrizeAmt, PwtBean winBean) throws LMSException {
		String ticketNo = winBean.getTicketNo();
		int gameNo = Util.fetchGameNoFrmTicket(ticketNo, 3);
		int gameId = Util.getGameId(gameNo);

		if (gameNo == 0) {
			winBean.setStatus("INVALID_TICKET");
			winBean.setReturnType("success");
			return winBean;
		}

		winBean.setGameId(gameId);
		winBean.setGameNo(gameNo);

		IServiceDelegate delegate = ServiceDelegate.getInstance();
		ServiceResponse sRes = new ServiceResponse();
		ServiceRequest sReq = new ServiceRequest();
		sReq.setServiceName(ServiceName.INSTANTPRINT);
		//sReq.setServiceMethod(ServiceMethodName.PAY_PWT_TICKET);
		sReq.setServiceData(winBean);
		sRes = delegate.getResponse(sReq);
		winBean = (PwtBean) sRes.getResponseData();

		
		if (sRes.getIsSuccess()) {
			String tktStatus = winBean.getStatus();
			if ("SUCCESS".equals(tktStatus)) {

				verifyPwt(winBean, userInfoBean, highPrizeCriteria,
						highPrizeAmt);

			} else {
				if ("TICKET_EXPIRED".equals(tktStatus)) {
					winBean.setStatus("TICKET_EXPIRED");
					winBean.setReturnType("success");
				} else {
					// Invalid Ticket
					winBean.setStatus("INVALID_TICKET");
					winBean.setReturnType("success");
				}
			}
		}
		return winBean;
	}

	public void verifyPwt(PwtBean winBean, UserInfoBean userInfoBean,
			String highPrizeCriteria, String highPrizeAmt) throws LMSException {
		Connection con = DBConnect.getConnection();
		String ticketNo = winBean.getTicketNo();
		int gameNo = winBean.getGameNo();
		int gameId = winBean.getGameId();

		OrgPwtLimitBean orgPwtLimit = null;
		try {
			orgPwtLimit = (new CommonMethods()).fetchPwtLimitsOfOrgnization(
					userInfoBean.getUserOrgId(), con);

			double pwtAmt = winBean.getPrizeAmt();

			String claimStatus = winBean.getClaimStatus();

			if ("UNCLM_PRIZE".equals(claimStatus)) {
				winBean.setClaimStatus("UNCLAIMED");
				if (orgPwtLimit == null) {
					throw new LMSException(
							"PWT Limits Are Not defined Properly!!");
				} else {
					if (pwtAmt <= orgPwtLimit.getVerificationLimit()) {
						boolean isHighPrize = highPrizeCriteria.equals("amt")
								&& pwtAmt > Double.parseDouble(highPrizeAmt);

						if (isHighPrize) {
							winBean.setHighPrize(isHighPrize);
							winBean.setRegRequired(true);
							winBean.setStatus("HIGH_PRIZE");
							if (pwtAmt < orgPwtLimit.getApprovalLimit()) {
								// Go for Approval

							}
							winBean.setReturnType("registration");
						} else if (pwtAmt <= orgPwtLimit.getPayLimit()) {
							winBean.setStatus("NORMAL_PAY");
							boolean isAutoScrap = "YES"
									.equalsIgnoreCase(orgPwtLimit
											.getIsPwtAutoScrap())
									&& pwtAmt <= orgPwtLimit.getScrapLimit();

							// Done Payment By Retailer Here
							retPwtPayment(userInfoBean.getUserId(),
									userInfoBean.getUserOrgId(), userInfoBean
											.getParentOrgId(), gameNo, gameId,
									isAutoScrap, pwtAmt, ticketNo, con);
							winBean.setReturnType("success");
						} else {
							winBean.setStatus("OUT_PAY_LIMIT");
							winBean.setReturnType("success");
						}
					} else {
						winBean.setStatus("OUT_VERIFY_LIMIT");
						winBean.setReturnType("success");
					}
				}
			} else if ("CLM_PRIZE".equalsIgnoreCase(claimStatus)) {
				winBean.setClaimStatus("CLAIMED");
				PreparedStatement pstmt = con
						.prepareStatement("select claim_status from st_ipe_pwt_inv_? where ticket_nbr=?");
				pstmt.setInt(1, gameNo);
				pstmt.setString(2, ticketNo);
				ResultSet rs = pstmt.executeQuery();
				if (rs.next()) {
					winBean.setStatus(PwtClaimMsg.pwtMsg(rs
							.getString("claim_status")));
					winBean.setReturnType("success");
				} else {
					winBean.setReturnType("error");
				}
			} else if ("NO_PRIZE".equalsIgnoreCase(claimStatus)) {
				winBean.setClaimStatus("CLAIMED");
				winBean.setStatus("No Prize!! Try Again.");
				winBean.setReturnType("success");
			} else {
				winBean.setClaimStatus("ERROR");
				winBean.setReturnType("error");
			}

		} catch (SQLException e) {
			winBean.setReturnType("error");
			e.printStackTrace();
		} finally {
			DBConnect.closeCon(con);
		}
	}
*/
	
	public void updateClaimStatus(PwtLMSBean winLMSBean,Connection con)
	{
		InstantPrint service = new InstantPrint();
		IInstantPrint portType = service.getInstantPrintServicePort();
		PwtBean winBean = new PwtBean();
		winBean.setGameNo(winLMSBean.getGameNo());
		winBean.setGameId(winLMSBean.getGameId());
		winBean.setTicketNo(winLMSBean.getTicketNo());
		winBean.setVirnNo(winLMSBean.getVirnNo());
		winBean =portType.updateClaimStatus(winBean);
		winLMSBean.setSuccess(winBean.isSuccess());
		if (!winLMSBean.isSuccess()) {
			
		}
	}
	public int retPwtPayment(PwtLMSBean winLMSBean, UserInfoBean userInfoBean, boolean isAutoScrap, Connection con) throws LMSException {
		try {
			Double fmtPwtAmt = CommonMethods.fmtToTwoDecimal(winLMSBean.getPrizeAmt());
			// insert data into main transaction master
			GameInfoBean gameInfoBean = IPEUtility
					.fetchOrgGameComm(userInfoBean.getUserOrgId(),winLMSBean.getGameId());
			double agtComm = gameInfoBean.getAgentPwtComm();
			double retComm = gameInfoBean.getRetPwtComm();

			String transMasQuery = QueryManager.insertInLMSTransactionMaster();
			PreparedStatement pstmt = con.prepareStatement(transMasQuery);
			pstmt.setString(1, "RETAILER");
			pstmt.executeUpdate();
			ResultSet rs = pstmt.getGeneratedKeys();

			if (rs.next()) {
				int transId = rs.getInt(1);

				// insert into retailer transaction master
				String retTransMasQry = "insert into  st_lms_retailer_transaction_master ( transaction_id , retailer_user_id , retailer_org_id ,game_id, transaction_date , transaction_type ) values (?,?,?,?,?,?)";

				pstmt = con.prepareStatement(retTransMasQry);
				pstmt.setInt(1, transId);
				pstmt.setInt(2,userInfoBean.getUserId());
				pstmt.setInt(3, userInfoBean.getUserOrgId());
				pstmt.setInt(4, winLMSBean.getGameId());
				pstmt.setTimestamp(5, new Timestamp(new Date().getTime()));
				pstmt.setString(6, "IPE_PWT");
				pstmt.executeUpdate();
        
				String retPwtEntry = "insert into st_ipe_ret_pwt_? (transaction_id, retailer_user_id,retailer_org_id,game_id,ticket_nbr,pwt_amt,retailer_claim_comm,agt_claim_comm,status) values(?,?,?,?,?,?,?,?,?)";
				pstmt = con.prepareStatement(retPwtEntry);
				pstmt.setInt(1,winLMSBean.getGameNo());
				pstmt.setInt(2, transId);
				pstmt.setInt(3, userInfoBean.getUserId());
				pstmt.setInt(4, userInfoBean.getUserOrgId());
				pstmt.setInt(5, winLMSBean.getGameId());
				pstmt.setString(6,winLMSBean.getTicketNo());
				pstmt.setDouble(7, fmtPwtAmt);
				pstmt.setDouble(8, retComm);
				pstmt.setDouble(9, agtComm);
				pstmt.setString(10, isAutoScrap ? "CLAIM_BAL" : "UNCLAIM_BAL");
				pstmt.executeUpdate();

				String pwtInvQry = "insert into st_ipe_pwt_inv_?(ticket_nbr, game_id, pwt_amt,claim_status,retailer_transaction_id,is_direct_plr)values(?,?,?,?,?,'N')";
				PreparedStatement pwtInvPstmt = con.prepareStatement(pwtInvQry);
				pwtInvPstmt.setInt(1, winLMSBean.getGameNo());
				pwtInvPstmt.setString(2, winLMSBean.getTicketNo());
				pwtInvPstmt.setInt(3, winLMSBean.getGameId());
				pwtInvPstmt.setDouble(4, fmtPwtAmt);
				pwtInvPstmt.setString(5, isAutoScrap ? "CLAIM_PLR_RET_CLM"
						: "CLAIM_PLR_RET_UNCLM");
				pwtInvPstmt.setInt(6, transId);
				pwtInvPstmt.executeUpdate();

				// update retailer UNCLAIM_BAL payment
				if (isAutoScrap) {
					// now retailer claimable balance DEBITED
					CommonMethods.updateOrgBalance("CLAIM_BAL", fmtPwtAmt
							+ fmtPwtAmt * .01 * retComm, userInfoBean.getUserOrgId(), "DEBIT",
							con);
					// agent claimable balance DEBITED
					CommonMethods.updateOrgBalance("CLAIM_BAL", fmtPwtAmt
							+ fmtPwtAmt * .01 * agtComm,userInfoBean.getParentOrgId(), "DEBIT",
							con);
				} else {
					CommonMethods.updateOrgBalance("UNCLAIM_BAL", fmtPwtAmt,
							userInfoBean.getUserOrgId(), "CREDIT", con);
				}

				// receipt entries are required to be inserted into receipt
				// table
				return transId;

			} else {
				throw new LMSException(
						"no data insert into main transaction master");
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException(e);
		}

	}
	
	public PwtLMSBean verifypwt(PwtLMSBean winLMSBean, UserInfoBean userInfoBean,
			String highPrizeCriteria, String highPrizeAmt) throws LMSException {
		Connection con = DBConnect.getConnection();

		try {
			con.setAutoCommit(false);			
			String ticketNo = winLMSBean.getTicketNo();		
			PwtBean winBean = new PwtBean();	
			winBean.setStatus(winLMSBean.getStatus());
			winBean.setReturnType(winLMSBean.getReturnType());
			winBean.setGameId(winLMSBean.getGameId());
			winBean.setGameNo(winLMSBean.getGameNo());
			winBean.setTicketNo(winLMSBean.getTicketNo());
			winBean.setVirnNo(winLMSBean.getVirnNo());
			winBean.setMobileNumber(winLMSBean.getMobileNumber());
			InstantPrint service = new InstantPrint();
			IInstantPrint portType = service.getInstantPrintServicePort();
			winBean=portType.payPwtTicket(winBean);
			winLMSBean.setStatus(winBean.getStatus());
			winLMSBean.setSuccess(winBean.isSuccess());
			winLMSBean.setPrizeAmt(winBean.getPrizeAmt());
			winLMSBean.setIsSold(winBean.getIsSold());
			winLMSBean.setClaimStatus(winBean.getClaimStatus());
			winLMSBean.setTktvalidity(winBean.getTktvalidity());
		
			if (!winLMSBean.isSuccess()) {
				return winLMSBean;
			}
			
			if (winLMSBean.isSuccess()) {
				
		OrgPwtLimitBean orgPwtLimit = null;

			orgPwtLimit = (new CommonMethods()).fetchPwtLimitsOfOrgnization(
					userInfoBean.getUserOrgId(), con);

			double pwtAmt = winLMSBean.getPrizeAmt();

			String claimStatus = winLMSBean.getClaimStatus();
		
			if ("UNCLM_PRIZE".equals(claimStatus)) {
				winLMSBean.setClaimStatus("UNCLAIMED");
			
				PreparedStatement pstmt = con
				.prepareStatement("select claim_status from st_ipe_pwt_inv_? where ticket_nbr=?");
		pstmt.setInt(1, winLMSBean.getGameNo());
		pstmt.setString(2, ticketNo);
		ResultSet rs = pstmt.executeQuery();
		if (rs.next()) {
			winLMSBean.setTktvalidity("Invalid Ticket");
			winLMSBean.setReturnType("error");
			winLMSBean.setStatus("Ticket is already claimed");
			return winLMSBean;
		} 
				if (orgPwtLimit == null) {
					throw new LMSException(
							"PWT Limits Are Not defined Properly!!");
				} else {
					if (pwtAmt <= orgPwtLimit.getVerificationLimit()) {
						boolean isHighPrize = highPrizeCriteria.equals("amt")
								&& pwtAmt > Double.parseDouble(highPrizeAmt);

						if (isHighPrize) {
							winLMSBean.setHighPrize(isHighPrize);
							winLMSBean.setRegRequired(true);
							winLMSBean.setStatus("HIGH_PRIZE");
							if (pwtAmt < orgPwtLimit.getApprovalLimit()) {
								// Go for Approval

							}
							winLMSBean.setReturnType("registration");
							return winLMSBean;
						} else if (pwtAmt <= orgPwtLimit.getPayLimit()) {
							winLMSBean.setStatus("NORMAL_PAY");
							boolean isAutoScrap = "YES"
									.equalsIgnoreCase(orgPwtLimit
											.getIsPwtAutoScrap())
									&& pwtAmt <= orgPwtLimit.getScrapLimit();

							RetPwtProcessHelper helper = new RetPwtProcessHelper();					
							helper.retPwtPayment(winLMSBean, userInfoBean,isAutoScrap ,con);
							
							//Done update claim status
                             helper.updateClaimStatus(winLMSBean, con);
							
                             winLMSBean.setReturnType("success");
						} else {
							winLMSBean.setStatus("OUT_PAY_LIMIT");
							winLMSBean.setReturnType("success");
							return winLMSBean;
						}
					} else {
						winLMSBean.setStatus("OUT_VERIFY_LIMIT");
						winLMSBean.setReturnType("success");
						return winLMSBean;
					}
				}
			} else if ("CLM_PRIZE".equalsIgnoreCase(claimStatus)) {
				winLMSBean.setClaimStatus("CLAIMED");
				PreparedStatement pstmt = con
						.prepareStatement("select claim_status from st_ipe_pwt_inv_? where ticket_nbr=?");
				pstmt.setInt(1, winLMSBean.getGameNo());
				pstmt.setString(2, ticketNo);
				ResultSet rs = pstmt.executeQuery();
				if (rs.next()) {
					winLMSBean.setStatus(PwtClaimMsg.pwtMsg(rs
							.getString("claim_status")));
					
					winLMSBean.setReturnType("success");
				} else {
					winLMSBean.setTktvalidity("Invalid Ticket");
					winLMSBean.setStatus("Ticket is already claimed ");
					winLMSBean.setReturnType("error");
				}
				return winLMSBean;
			} else if ("NO_PRIZE".equalsIgnoreCase(claimStatus)) {
				winLMSBean.setClaimStatus("CLAIMED");
				winLMSBean.setStatus("No Prize!! Try Again.");
				winLMSBean.setReturnType("success");
				return winLMSBean;
			} else {
				winLMSBean.setClaimStatus("ERROR");
				winLMSBean.setTktvalidity("Invalid Ticket");
				winLMSBean.setStatus("Ticket is already claimed");
				winLMSBean.setReturnType("error");
				return winLMSBean;
			}

			}
				con.commit();		
			} catch (SQLException e) {
				winLMSBean.setReturnType("error");
			e.printStackTrace();
		} finally {
			DBConnect.closeCon(con);
			
		}
		return winLMSBean;
	}
	
	
}