package com.skilrock.lms.api.common;

import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.skilrock.lms.beans.PlayerBean;
import com.skilrock.lms.beans.ServiceRequest;
import com.skilrock.lms.beans.ServiceResponse;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.ConfigurationVariables;
import com.skilrock.lms.common.Utility;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.CommonMethods;
import com.skilrock.lms.common.utility.GenerateRecieptNo;
import com.skilrock.lms.coreEngine.drawGames.playMgmt.DrawGameRPOSHelper;
import com.skilrock.lms.coreEngine.drawGames.pwtMgmt.PlayerVerifyHelperForApp;
import com.skilrock.lms.coreEngine.service.IServiceDelegate;
import com.skilrock.lms.coreEngine.service.ServiceDelegate;
import com.skilrock.lms.coreEngine.service.dge.ServiceMethodName;
import com.skilrock.lms.coreEngine.service.dge.ServiceName;
import com.skilrock.lms.dge.beans.DrawIdBean;
import com.skilrock.lms.dge.beans.MainPWTDrawBean;
import com.skilrock.lms.dge.beans.PWTDrawBean;
import com.skilrock.lms.dge.beans.PanelIdBean;
import com.skilrock.lms.web.drawGames.common.Util;

public class TPBoPwtProcessHelper {
	Log logger = LogFactory.getLog(TPBoPwtProcessHelper.class);
	public int getGamenoFromTktnumber(String tktNum) {

		int retIdLen = 0;
		int gameNo = 0;
		int tktLen = 0;
		String tktBuf = null;

		if (tktNum != null
				&& (tktNum.length() == ConfigurationVariables.tktLenA || tktNum
						.length() == ConfigurationVariables.tktLenB)) {
			if (tktNum.length() == ConfigurationVariables.tktLenA) {
				tktLen = ConfigurationVariables.tktLenA;
				retIdLen = ConfigurationVariables.retIdLenA;
				tktBuf = tktNum.substring(retIdLen, retIdLen
						+ ConfigurationVariables.gameNoLenA);
			} else if (tktNum.length() == ConfigurationVariables.tktLenB) {
				tktLen = ConfigurationVariables.tktLenB;
				retIdLen = ConfigurationVariables.retIdLenB;
				tktBuf = tktNum.substring(retIdLen, retIdLen
						+ ConfigurationVariables.gameNoLenB);
			}
			gameNo = Integer.parseInt(tktBuf);
		}
		return gameNo;

	}
	
	public MainPWTDrawBean verifyPwt(MainPWTDrawBean mainPwtBean,
			UserInfoBean userInfoBean, String pwtAmtForMasterApproval, String refMerchantId) {
		ArrayList<PWTDrawBean> winningBeanList = new ArrayList<PWTDrawBean>();
		String ticketNumber =Util.getTicketNumber( mainPwtBean.getTicketNo(), mainPwtBean.getInpType()); 
		if(ticketNumber.equals("ERROR") || "".equals(ticketNumber)){
			mainPwtBean.setStatus("ERROR_INVALID");
			return mainPwtBean;
		}
		double govtTaxAmount = 0.00;
		double totalticketAmt=0.0;
		int barCodeCount=-1;
		// Get The BarCode If Reqired
		if (mainPwtBean.getInpType() == 1 || mainPwtBean.getTicketNo()
						.length() == com.skilrock.lms.common.ConfigurationVariables.barcodeCount) {
			barCodeCount = Integer
					.parseInt(Util.getBarCodeCountFromTicketNumber(mainPwtBean
							.getTicketNo()));
		}
		// get game number from ticket number
		int gameNo = getGamenoFromTktnumber(ticketNumber);
		if (gameNo <= 0) {
			mainPwtBean.setStatus("ERROR_INVALID");
			return mainPwtBean;
		}
		mainPwtBean.setMainTktGameNo(gameNo);
		mainPwtBean.setGameId(Util.getGameIdFromGameNumber(gameNo));
		// get game type from ticket type
		String gameType = Util.getGameType(gameNo);
		if (gameType == null) {
			mainPwtBean.setStatus("ERROR_INVALID");
			return mainPwtBean;
		}	
			mainPwtBean.setPwtTicketType("DRAW");
			PWTDrawBean drawScheduleBean = new PWTDrawBean();
			drawScheduleBean.setByPassDates(true);
			drawScheduleBean.setBarCodeCount(barCodeCount);
			drawScheduleBean.setTicketNo(ticketNumber);
			drawScheduleBean.setPartyId(userInfoBean.getUserOrgId());
			drawScheduleBean.setUserId(userInfoBean.getUserId());
			drawScheduleBean.setPartyType(userInfoBean.getUserType());
			drawScheduleBean.setRefMerchantId(refMerchantId);
			drawScheduleBean = verifyTicket(drawScheduleBean,
					userInfoBean, pwtAmtForMasterApproval);
			winningBeanList.add(drawScheduleBean);	
			mainPwtBean.setStatus(drawScheduleBean.getStatus());
			mainPwtBean.setWinningBeanList(winningBeanList);
		// here set  total ticket amount	
			for (int i = 0; i < winningBeanList.size(); i++) {
				PWTDrawBean pwtBean = winningBeanList.get(i);
				govtTaxAmount = govtTaxAmount + pwtBean.getGovtTaxAmount();
				totalticketAmt = totalticketAmt + pwtBean.getTotalAmount();
			}
			mainPwtBean.setTotlticketAmount(totalticketAmt);
			mainPwtBean.setGovtTaxAmount(govtTaxAmount);
		return mainPwtBean;
	}
	
	public PWTDrawBean verifyTicket(PWTDrawBean winningBean,
			UserInfoBean userInfoBean, String pwtAmtForMasterApproval) {
		Connection connection = DBConnect.getConnection();

		ServiceResponse sRes = new ServiceResponse();
		ServiceRequest sReq = new ServiceRequest();
		sReq.setServiceName(ServiceName.PWT_MGMT);
		sReq.setServiceMethod(ServiceMethodName.DRAWGAME_PRZE_WINNING_TICKET);
		sReq.setServiceData(winningBean);

		IServiceDelegate delegate = ServiceDelegate.getInstance();
		boolean isMasAppReq = false;
		try {
			connection.setAutoCommit(false);
			sRes = delegate.getResponse(sReq);
			Type type = new TypeToken<PWTDrawBean>(){}.getType();
			winningBean = (PWTDrawBean)new Gson().fromJson((JsonElement)sRes.getResponseData(), type);
//			winningBean = (PWTDrawBean) sRes.getResponseData();

			//logger.debug(winningBean.isValid() + "************Test***********"); Check
			if (sRes.getIsSuccess()) {
				if(Boolean.parseBoolean((String)com.skilrock.lms.common.Utility.getPropertyValue("DO_MATH_ROUNDING_FOR_PWT_AMT")))
					CommonMethods.doRoundingForPwtAmt(winningBean);
				// if scheme is panel wise winning
				//	String highPrizeAmt = (String) LMSUtility.sc.getAttribute("HIGH_PRIZE_AMT");
				//logger.debug("HIGH_PRIZE_AMT" + highPrizeAmt); Check
				boolean isUnClaimed=false;
				boolean isDrawExpired = false;
				double totPwtAmt = 0.0;
 				for (DrawIdBean drawIdWinningBean : winningBean
						.getDrawWinList()) {
					
				//	int drawId = drawIdWinningBean.getDrawId();
					if (drawIdWinningBean.getPanelWinList() != null) {
						if(drawIdWinningBean.getStatus().equalsIgnoreCase("UNCLM_PWT")){
							isUnClaimed=true;
						}else if(drawIdWinningBean.getStatus().equals("DRAW_EXPIRED")){
							isDrawExpired= true;
						}						
						totPwtAmt= totPwtAmt + Double.parseDouble(drawIdWinningBean.getWinningAmt());						
						
					}
				}
				if(isUnClaimed){
					double applicableAmount = Double.parseDouble(Utility.getPropertyValue("PLAYER_WINNING_TAX_APPLICABLE_AMOUNT"));
					double govtTaxAmount = 0.00;
					if(totPwtAmt >= applicableAmount) {
						double govtCommPwt = Util.getGameMasterLMSBean(winningBean.getGameId()).getGovtCommPwt();
						govtTaxAmount = totPwtAmt*govtCommPwt*0.01;
					}

					isMasAppReq = totPwtAmt > Double.parseDouble(pwtAmtForMasterApproval);
					if (isMasAppReq) {			
						winningBean.setStatus("MAS_APP_REQ");
						winningBean.setTotalAmount(totPwtAmt - govtTaxAmount);
					}else if(totPwtAmt>0){
						winningBean.setGameDispName(Util.getGameDisplayName(winningBean.getGameNo()));
						winningBean.setGameId(Util.getGameIdFromGameNumber(winningBean.getGameNo()));
						winningBean.setGovtTaxAmount(govtTaxAmount);
						winningBean.setTotalAmount(totPwtAmt);
						winningBean.setStatus("SUCCESS");
					}else{
						winningBean.setGameDispName(Util.getGameDisplayName(winningBean
								.getGameNo()));
						winningBean.setGameId(Util.getGameIdFromGameNumber(winningBean.getGameNo()));
						winningBean.setStatus("NonWin");
					}
					
				}else{
					if(winningBean.isResAwaited()){
						winningBean.setStatus("RES_AWAITED");
					}else if (isDrawExpired){
						winningBean.setStatus("DRAW_EXPIRED");
					}else{
						winningBean.setStatus("CLAIMED");
					}
				}
				

			} else {
				//logger.debug("inside invalid ticket "); Check
				winningBean.setStatus("ERROR");
				//return error from here  Check 

			}
			DBConnect.closeCon(connection);
		} catch (Exception e) {
			//logger.error("Exception: " + e); CHECK
			e.printStackTrace();
			winningBean.setStatus("ERROR");
		}
		return winningBean;
	}
public int playerRegistration(PlayerBean plrInfoBean,boolean isAnonymous,Connection con){
	int playerId=0;
	try{
		if (!isAnonymous && plrInfoBean != null) {
			playerId = new PlayerVerifyHelperForApp().verifyPlayer(plrInfoBean.getFirstName(),plrInfoBean.getLastName(),plrInfoBean.getIdNumber(),plrInfoBean.getIdType());
			if(playerId==0){
				// new Player Info
				playerId = new PlayerVerifyHelperForApp().registerPlayer(
						plrInfoBean, con);
			}
			
		} else if (isAnonymous) {
			playerId = 1; // hard coded for Anonymous player
		}
	}catch(Exception e){
		e.printStackTrace();
		
	}
	
	return playerId;
}	
public MainPWTDrawBean pwtPayment(MainPWTDrawBean  mainPwtDrawBean,Connection con,UserInfoBean userInfoBean,boolean isAnonymous,int playerId,String refTransId,int tpSystemId) throws LMSException {
	try{
		Map pwtAppMap = new TreeMap();
		//int pwtGameNo = 0;
		
		String playerType=null;
		String recIdForApp = GenerateRecieptNo.generateRequestIdDraw("DGREQUEST");
		double netPwtAmt = 0.0;
		boolean ispay = false;
		boolean isResAwaited = false;
		PreparedStatement pstmt =null;
		List<Long> transIdList = new ArrayList<Long>();
		//logger.debug("inside panel wise");
		List<PWTDrawBean> pwtDrawBeanList = mainPwtDrawBean.getWinningBeanList();

		/*	Tax Deduction	*/
		double taxPercentage = 0.00;
		if(mainPwtDrawBean.getGovtTaxAmount() > 0) {
			taxPercentage = Util.getGameMasterLMSBean(mainPwtDrawBean.getGameId()).getGovtCommPwt();
		}

		for (PWTDrawBean pwtDrawBean : pwtDrawBeanList) {
			if (pwtDrawBean.isResAwaited())
				isResAwaited = true;
			// pwtGameNo = pwtDrawBean.getGameNo();

			if (pwtDrawBean.getDrawWinList() != null
					&& pwtDrawBean.getDrawWinList().size() > 0) {
				for (DrawIdBean drawIdBean : pwtDrawBean
						.getDrawWinList()) {
					if (drawIdBean.getPanelWinList() != null) {

						for (PanelIdBean panelIdBean : drawIdBean
								.getPanelWinList()) {

							if (panelIdBean.isValid()) {
								int reqToOrgId = 0;
								String reqToOrgType = null;
								String remarks = null;
								String reqStatus = null;
								String approvedByType = null;
								int approvedByUserId = 0;
								int approvedByOrgId = 0;
								logger.debug("panelIdBean.isValid()===="+ panelIdBean.isValid());
							/*	if (panelIdBean.isAppReq()) {
									// means approval from BO master is
									// required
									// get Back office organization and
									// user id

									reqToOrgId = userInfoBean
											.getParentOrgId();
									reqToOrgType = "BO";
									remarks = "requested to BO master  For Approval";
									reqStatus = "PND_MAS";
									System.out
											.println("inside if panelIdBean.isAppReq()==="
													+ panelIdBean
															.isAppReq()
													+ remarks
													+ reqStatus);

								} else*/ 
								
							if (!isAnonymous) {
									// go for pending payments
									playerType = "player"; 
									reqToOrgId = userInfoBean
											.getUserOrgId();
									reqToOrgType = userInfoBean
											.getUserType();
									remarks = "Auto Approved By BO";
									reqStatus = "PAID";
									approvedByType = "BO";
									approvedByUserId = userInfoBean
											.getUserId();
									approvedByOrgId = reqToOrgId;
									panelIdBean.setStatus("HIGH_PRIZE");
									System.out
											.println("inside else panelIdBean.isAppReq()==="
													+ panelIdBean
															.isAppReq()
													+ remarks
													+ reqStatus);
									
								} else {
									playerType = "anonymous";
									reqToOrgId = userInfoBean
											.getUserOrgId();
									reqToOrgType = userInfoBean
											.getUserType();
									remarks = "Paid as Anonymous Player";
									reqStatus = "PAID";
									approvedByType = "BO";
									approvedByUserId = userInfoBean
											.getUserId();
									approvedByOrgId = reqToOrgId;
									panelIdBean.setStatus("NORMAL_PAY");

								}

								logger.debug("Approval requested to orgId = "+ reqToOrgId+ "  and user type = "+ reqToOrgType);

								// generate TEMP receipt for Approval
								// GenerateRecieptNo.get

								logger.debug("^^^^^^^^^***** panel wise "+ panelIdBean.getWinningAmt());

								// insert into Approval table
								/*if (playerType == null) {
									playerType = "anonymous";
								}*/
								// code added for the case if player is
								// anonymous
								// and no high prize
								String insertAppQuery = "insert into  st_dg_approval_req_master (party_type ,request_id,party_id,ticket_nbr,draw_id,panel_id,game_id,pwt_amt,tax_amt,net_amt,req_status,requester_type,requested_by_user_id,requested_by_org_id,requested_to_org_id,requested_to_type,approved_by_type, approved_by_user_id , approved_by_org_id,pay_req_for_org_type,pay_request_for_org_id,approval_date,request_date, remarks) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
								logger.debug("insertAppQuery = "
										+ insertAppQuery
										+ "@@@@@@@@@@@"
										+ playerType.toUpperCase());
								pstmt = con
										.prepareStatement(insertAppQuery);
								pstmt.setString(1, playerType
										.toUpperCase());
								pstmt.setString(2, recIdForApp);

								/*boolean isPlr = "player"
										.equalsIgnoreCase(playerType
												.trim());
								if (isPlr) {
									pstmt.setInt(3, playerId);
								} else {
									pstmt.setObject(3, null);
								}*/
								
								pstmt.setInt(3, playerId);
								
								// ticket is entered with reprint count
								// in Approval req master on 04/04/2011
								pstmt.setObject(4,pwtDrawBean.getTicketNo()+ pwtDrawBean.getReprintCount());
								pstmt.setInt(5, drawIdBean.getDrawId());
								pstmt.setInt(6, panelIdBean
										.getPanelId());
								pstmt
										.setInt(7, pwtDrawBean
												.getGameId());
								pstmt.setDouble(8, CommonMethods
										.fmtToTwoDecimal(panelIdBean
												.getWinningAmt()));
								pstmt.setDouble(9, 0.0);
								pstmt.setDouble(10, panelIdBean
										.getWinningAmt());
								pstmt.setString(11, reqStatus);
								pstmt.setString(12, userInfoBean
										.getUserType());
								pstmt.setInt(13, userInfoBean
										.getUserId());
								pstmt.setInt(14, userInfoBean
										.getUserOrgId());
								pstmt.setInt(15, reqToOrgId);
								pstmt.setString(16, reqToOrgType);
							/*	if (drawIdBean.isAppReq()) {
									pstmt.setObject(17, null);
									pstmt.setObject(18, null);
									pstmt.setObject(19, null);
									pstmt.setObject(20, null);
									pstmt.setObject(21, null);
									pstmt.setObject(22, null);
								} else {*/
									pstmt.setString(17, approvedByType);
									pstmt.setInt(18, approvedByUserId);
									pstmt.setInt(19, approvedByOrgId);
									pstmt.setString(20, approvedByType);
									pstmt.setInt(21, approvedByOrgId);
								Calendar cal = Calendar.getInstance();
								Timestamp currentDate = null;
								currentDate = new Timestamp(cal.getTime().getTime());	
									
								pstmt.setTimestamp(22,currentDate);
								//}
								pstmt.setTimestamp(23,currentDate);
								pstmt.setString(24, remarks);
								logger.debug("insertAppQuery pppppp = "
										+ pstmt);
								pstmt.executeUpdate();

								System.out
										.println("insertion into pwt temp request  table = "
												+ pstmt);
								ResultSet rs = pstmt.getGeneratedKeys();
								int reqId = 0;
								if (rs.next()) {
									reqId = rs.getInt(1);
								} else {
									throw new LMSException(
											"NO Data Inserted in st_pwt_approval_request_master table");
								}

								// insert in draw pwt inv table
								if (reqStatus!=null) {
									reqStatus = "CLAIM_PLR_BO";
								}
								System.out.println("@@@@new status is"+ reqStatus);
								String insIntoDGPwtInvQuery = "insert into st_dg_pwt_inv_?(ticket_nbr, draw_id,panel_id, pwt_amt, status,is_direct_plr) values (?, ?, ?, ?, ?,?)";
								PreparedStatement insIntoDGPwtInvPstmt = con
										.prepareStatement(insIntoDGPwtInvQuery);
								insIntoDGPwtInvPstmt.setInt(1,
										pwtDrawBean.getGameId());
								insIntoDGPwtInvPstmt.setString(2,
										pwtDrawBean.getTicketNo());
								insIntoDGPwtInvPstmt.setInt(3,
										drawIdBean.getDrawId());
								insIntoDGPwtInvPstmt.setInt(4,
										panelIdBean.getPanelId());
								insIntoDGPwtInvPstmt
										.setDouble(
												5,
												CommonMethods
														.fmtToTwoDecimal(panelIdBean
																.getWinningAmt()));
								insIntoDGPwtInvPstmt.setString(6,
										reqStatus);
								insIntoDGPwtInvPstmt.setString(7, "Y");
								insIntoDGPwtInvPstmt.executeUpdate();
								netPwtAmt = netPwtAmt
										+ CommonMethods
												.fmtToTwoDecimal(panelIdBean
														.getWinningAmt());

								if ( reqStatus!=null) {
									logger.info("is  payin as anonymous::::"+isAnonymous+"reqStatus "+reqStatus);
									// tax=0.0,chequeNbr=null,draweeBank=null,issuingParty=null,chqDate=null,paymentType=CASH
									// hard coded for anonymous player
									double tax = panelIdBean.getWinningAmt()*taxPercentage*0.01;
									long transId = boDirectPlrPwtPayment(
											pwtDrawBean.getTicketNo(),
											drawIdBean.getDrawId(),
											playerId,
											CommonMethods.fmtToTwoDecimal(panelIdBean.getWinningAmt()),
											tax,
											//CommonMethods.fmtToTwoDecimal(panelIdBean.getGovtCommPwt()),
											reqId,
											null,
											null,
											null,
											null,
											"CASH",
											userInfoBean.getUserOrgId(),
											userInfoBean.getUserId(),
											pwtDrawBean.getGameNo(),
											pwtDrawBean.getGameId(),
											con, panelIdBean
													.getPanelId(),
											"PANEL_WISE");
									if (transId > 0) {
										String updateAppTable = "update  st_dg_approval_req_master  set  payment_done_by_type =?, payment_done_by =? ,transaction_id=? where  task_id = ?";
										PreparedStatement pstmt1 = con
												.prepareStatement(updateAppTable);
										pstmt1.setString(1, "BO");
										pstmt1.setInt(2, userInfoBean
												.getUserOrgId());
										pstmt1.setLong(3, transId);
										pstmt1.setInt(4, reqId);
										logger
												.debug("update  st_dg_approval_req_master Query::::"
														+ pstmt);
										pstmt1.executeUpdate();
										transIdList.add(transId);
										// store ref Trans Id
										TpUtilityHelper.storeTpSystemTxnId(tpSystemId,transId+"", refTransId, con);
										ispay=true;
									}else{
										logger
										.debug("Error In Transaction At LMS End");
										throw new LMSException("Error At LMS End");
										
									}
								}

							}

						}

					}
				}
				// }

				if (ispay) {
					// Draw Game Updation of Ticket
					ServiceResponse sRes = new ServiceResponse();
					ServiceRequest sReq = new ServiceRequest();
					sReq.setServiceName(ServiceName.PWT_MGMT);
					// sReq.setServiceMethod(ServiceMethodName.DRAWGAME_PWT_UPDATE);
					IServiceDelegate delegate = ServiceDelegate.getInstance();
					if(!isAnonymous){
						pwtDrawBean.setStatus("REGISTRATION");
					}else{
						pwtDrawBean.setStatus("NORMAL_PAY");
					}
					
					sReq
							.setServiceMethod(ServiceMethodName.DRAWGAME_PWT_UPDATE);
					sReq.setServiceData(pwtDrawBean);
					sRes = delegate.getResponse(sReq);
					if (sRes.getIsSuccess()) {
						// connection.commit();

						// generete temp receipt here
						/*
						 * GraphReportHelper graphReportHelper = new
						 * GraphReportHelper();graphReportHelper.
						 * createTextReportTempPlayerReceipt(
						 * recIdForApp, "BO", rootPath, "DRAW_GAME");
						 */
						// connection.close();
						pwtDrawBean.setStatus("SUCCESS");
						
						if (isResAwaited && netPwtAmt > 0) {
							DrawGameRPOSHelper helper = new DrawGameRPOSHelper();
							int len = mainPwtDrawBean.getTicketNo().length();
							Object gameBean = helper.reprintTicket(userInfoBean, true,
									mainPwtDrawBean.getTicketNo().substring(0, len - 2),
									false,null,null);
							mainPwtDrawBean.setPurchaseBean(gameBean);
							mainPwtDrawBean.setReprint(true);
						}
						
										
						con.commit();
						// set Trnas IDs 
						mainPwtDrawBean.setTransactionIdList(transIdList);
						mainPwtDrawBean.setRefNumber(recIdForApp);// set Request Id
						mainPwtDrawBean.setPwtStatus("SUCCESS");
						return mainPwtDrawBean;
						
						
					} else {
						System.out
								.println("***************************^^^^^^^^^^inside error while updating draw game ");
						con.close();
						mainPwtDrawBean.setPwtStatus("ERROR");
						pwtDrawBean.setStatus("ERROR");
						throw new LMSException(
								"Pwt not updated in DGE...");
					}
				}
			}
		
			
			
			
			/*pwtAppMap.put("recId", recIdForApp);
				// pwtAppMap.put("PWT_RES_BEAN", pwtDrawBean);// added by amit
			pwtAppMap.put("PWT_RES_BEAN", mainPwtDrawBean);
			pwtAppMap.put("GAME_NAME", Util.getGameName(pwtGameNo)
						.toUpperCase()
						+ "_PWT");
			pwtAppMap.put("isAnonymous", isAnonymous);
			pwtAppMap.put("NET_AMOUNT_PAID", netPwtAmt);*/
				// pwtAppMap.put("reqId", reqId);
				// pwtAppMap.putAll(pwtDetails);///////tttttttttttttttttt
				// pwtAppMap.put("remarks", remarks);

				// now generate temporary receipt for player
				// GraphReportHelper graphHelpwr=new GraphReportHelper();
				// graphHelpwr.createTextReportTempPlayerReceipt(reqId,"BO",
				// rootPath);
			
			
		
		}
	
				
	}catch(Exception e){
		e.printStackTrace();
		mainPwtDrawBean.setPwtStatus("ERROR");
		throw new LMSException("Error In Pwt Payment");
		
	} 
	return mainPwtDrawBean;	

}


public long boDirectPlrPwtPayment(String ticketNbr, int drawId,
		int playerId, double pwtAmt, double tax, int taskId,
		String chequeNbr, String draweeBank, String issuingParty,
		java.sql.Date chqDate, String paymentType, int userOrgId,
		int userId, int gameNbr, int gameId, Connection connection,
		Object panelId, String schemeType) throws LMSException {
	try {

		// insert data into main transaction master
		logger.debug("insert data into transaction master ");
		String transMasQuery = QueryManager.insertInLMSTransactionMaster();
		PreparedStatement pstmt = connection
				.prepareStatement(transMasQuery);
		pstmt.setString(1, "BO");
		pstmt.executeUpdate();
		ResultSet rs = pstmt.getGeneratedKeys();

		if (rs.next()) {
			long transId = rs.getLong(1);
			// double pwtAmt = Double.parseDouble(bean.getPwtAmount());
			// DGDirectPlrPwtBean dirPlrBean = (DGDirectPlrPwtBean) bean;

			// insert in st_bo_transaction master
			pstmt = connection.prepareStatement(QueryManager
					.insertInBOTransactionMaster());
			pstmt.setLong(1, transId);
			pstmt.setInt(2, userId);
			pstmt.setInt(3, userOrgId);	
			pstmt.setString(4, "PLAYER");
			pstmt.setInt(5, playerId);
			pstmt.setTimestamp(6, new java.sql.Timestamp(
					new java.util.Date().getTime()));
			pstmt.setString(7, "DG_PWT_PLR");
			pstmt.executeUpdate();
			logger.debug("insert into BO transaction master = " + pstmt);

			String directPlrPayment = "insert into st_dg_bo_direct_plr_pwt (bo_user_id, "
					+ "bo_org_id, draw_id, transaction_id, transaction_date, game_id, player_id,"
					+ " pwt_amt, tax_amt, net_amt, payment_type, cheque_nbr, cheque_date, drawee_bank,"
					+ " issuing_party_name, task_id,panel_id ) values (?, ?, ?, ?, ?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)";
			pstmt = connection.prepareStatement(directPlrPayment);
			pstmt.setInt(1, userId);
			pstmt.setInt(2, userOrgId);
			pstmt.setInt(3, drawId);
			pstmt.setLong(4, transId);
			pstmt.setTimestamp(5, new java.sql.Timestamp(
					new java.util.Date().getTime()));
			pstmt.setInt(6, gameId);
			pstmt.setInt(7, playerId);
			pstmt.setDouble(8, pwtAmt);
			pstmt.setDouble(9, tax);
			pstmt.setDouble(10, pwtAmt - tax);
			pstmt.setString(11, paymentType);

			if ("cash".equalsIgnoreCase(paymentType)
					|| "TPT".equalsIgnoreCase(paymentType)) {
				pstmt.setObject(12, null);
				pstmt.setObject(13, null);
				pstmt.setObject(14, null);
				pstmt.setObject(15, null);
			} else if ("cheque".equalsIgnoreCase(paymentType)) {
				pstmt.setString(12, chequeNbr);
				pstmt.setDate(13, chqDate);
				pstmt.setString(14, draweeBank);
				pstmt.setString(15, issuingParty);
			}
			// pstmt.setString(12, chequeNbr);
			// pstmt.setDate(13, chqDate);
			// pstmt.setString(14, draweeBank);
			// pstmt.setString(15, issuingParty);
			pstmt.setInt(16, taskId);
			pstmt.setObject(17, panelId);
			pstmt.executeUpdate();
			logger.debug("insert into st_dg_bo_direct_plr_pwt = " + pstmt);

			// update ticket details into st_dg_pwt_inv_? table
			String insIntoDGPwtInvQuery = null;
			if ("DRAW_WISE".equalsIgnoreCase(schemeType.trim())) {
				insIntoDGPwtInvQuery = "update st_dg_pwt_inv_? set status = ? ,  bo_transaction_id = ? "
						+ " where ticket_nbr = ? and draw_id = ?";
			} else {
				insIntoDGPwtInvQuery = "update st_dg_pwt_inv_? set status = ? ,  bo_transaction_id = ? "
						+ " where ticket_nbr = ? and draw_id = ? and panel_id="
						+ panelId;
			}
			PreparedStatement insIntoDGPwtInvPstmt = connection
					.prepareStatement(insIntoDGPwtInvQuery);
			insIntoDGPwtInvPstmt.setInt(1, gameNbr);
			insIntoDGPwtInvPstmt.setString(2, "CLAIM_PLR_BO");
			insIntoDGPwtInvPstmt.setLong(3, transId);
			insIntoDGPwtInvPstmt.setString(4, ticketNbr);
			insIntoDGPwtInvPstmt.setInt(5, drawId);
			logger.debug("insIntoDGPwtInvPstmt = " + insIntoDGPwtInvPstmt);
			insIntoDGPwtInvPstmt.executeUpdate();

			// receipt entries are required to be inserted into receipt
			// table
			return transId;

		} else {
			throw new LMSException(
					"no data insert into main transaction master");
		}

	} catch (SQLException e) {
		logger.error("Exception: " + e);
		e.printStackTrace();
		throw new LMSException(e);
	}

}
	
}
