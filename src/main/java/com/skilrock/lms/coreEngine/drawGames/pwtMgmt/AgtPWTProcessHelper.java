package com.skilrock.lms.coreEngine.drawGames.pwtMgmt;

import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.skilrock.lms.beans.OrgPwtLimitBean;
import com.skilrock.lms.beans.PlayerBean;
import com.skilrock.lms.beans.ServiceRequest;
import com.skilrock.lms.beans.ServiceResponse;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.Utility;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;
import com.skilrock.lms.common.utility.CommonMethods;
import com.skilrock.lms.common.utility.GenerateRecieptNo;
import com.skilrock.lms.common.utility.OrgCreditUpdation;
import com.skilrock.lms.common.utility.ResponsibleGaming;
import com.skilrock.lms.coreEngine.drawGames.playMgmt.DrawGameRPOSHelper;
import com.skilrock.lms.coreEngine.service.IServiceDelegate;
import com.skilrock.lms.coreEngine.service.ServiceDelegate;
import com.skilrock.lms.coreEngine.service.dge.ServiceMethodName;
import com.skilrock.lms.coreEngine.service.dge.ServiceName;
import com.skilrock.lms.coreEngine.userMgmt.common.CommonFunctionsHelper;
import com.skilrock.lms.dge.beans.DrawIdBean;
import com.skilrock.lms.dge.beans.GameMasterLMSBean;
import com.skilrock.lms.dge.beans.MainPWTDrawBean;
import com.skilrock.lms.dge.beans.PWTDrawBean;
import com.skilrock.lms.dge.beans.PanelIdBean;
import com.skilrock.lms.dge.beans.RaffleDrawIdBean;
import com.skilrock.lms.web.bankMgmt.Helpers.BankUtil;
import com.skilrock.lms.web.drawGames.common.Util;

public class AgtPWTProcessHelper {
	static Log logger = LogFactory.getLog(AgtPWTProcessHelper.class);
	private PreparedStatement pstmt;

	public long agentPwtPayment(String ticketNum, int drawId, int playerId,
			double pwtAmt, double tax, int taskId, String paymentType,
			int userOrgId, int userId, int gameNbr, int gameId,
			Connection connection, Object panelId, String schemeType,
			boolean isAutoScrap) throws LMSException {
		try {

			// insert data into lms transaction master
			logger.debug("insert data into transaction master ");
			String transMasQuery = QueryManager.insertInLMSTransactionMaster();
			PreparedStatement pstmt = connection
					.prepareStatement(transMasQuery);
			pstmt.setString(1, "AGENT");
			pstmt.executeUpdate();
			ResultSet rs = pstmt.getGeneratedKeys();

			if (rs.next()) {
				long transId = rs.getLong(1);

				// insert in st_agent_transaction master
				pstmt = connection.prepareStatement(QueryManager
						.insertInAgentTransactionMaster());
				pstmt.setLong(1, transId);
				pstmt.setInt(2, userId);
				pstmt.setInt(3, userOrgId);
				pstmt.setString(4, "PLAYER");
				pstmt.setInt(5, playerId);
				pstmt.setString(6, "DG_PWT_PLR");
				pstmt.setTimestamp(7, new java.sql.Timestamp(
						new java.util.Date().getTime()));

				logger.debug("insert into Agent transaction master = " + pstmt);
				pstmt.executeUpdate();
				com.skilrock.lms.coreEngine.drawGames.common.CommonFunctionsHelper commHelper = new com.skilrock.lms.coreEngine.drawGames.common.CommonFunctionsHelper();

				// fetch agent comm rate
				double agtComm = com.skilrock.lms.coreEngine.drawGames.common.CommonFunctionsHelper
						.fetchDGCommOfOrganization(gameId, userOrgId, "PWT",
								"AGENT", connection);

				String directPlrPayment = "insert into st_dg_agt_direct_plr_pwt (agent_user_id, "
						+ "agent_org_id, draw_id, transaction_id, transaction_date, game_id, player_id,"
						+ " pwt_amt, tax_amt, net_amt, payment_type, cheque_nbr, cheque_date, drawee_bank,"
						+ " issuing_party_name, task_id,panel_id,agt_claim_comm,pwt_claim_status) values (?, ?, ?, ?, ?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?)";
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
				pstmt.setObject(12, null);
				pstmt.setObject(13, null);
				pstmt.setObject(14, null);
				pstmt.setObject(15, null);
				pstmt.setInt(16, taskId);
				pstmt.setObject(17, panelId);
				pstmt.setDouble(18, (pwtAmt * .01 * agtComm));
				pstmt.setString(19, isAutoScrap ? "CLAIM_BAL" : "UNCLAIM_BAL");
				logger.debug("insert query st_dg_agt_direct_plr_pwt ::::"
						+ pstmt);
				pstmt.executeUpdate();
				// update ticket details into st_dg_pwt_inv_? table

				if (isAutoScrap) {
					// agent claimable balance DEBITED
					//Now make payment updte method only one
						
					OrgCreditUpdation.updateOrganizationBalWithValidate(pwtAmt + (pwtAmt
							* .01 * agtComm)+tax, "CLAIM_BAL", "DEBIT", userOrgId,0, "AGENT", 0, connection);
					
					/*commHelper.updateOrgBalance("CLAIM_BAL", pwtAmt + pwtAmt
							* .01 * agtComm, userOrgId, "DEBIT", connection);*/
				} else {
					OrgCreditUpdation.updateOrganizationBalWithValidate(pwtAmt+tax, "UNCLAIM_BAL", "CREDIT", userOrgId,0, "AGENT", 0, connection);
					/*commHelper.updateOrgBalance("UNCLAIM_BAL", pwtAmt,
							userOrgId, "CREDIT", connection);*/
				}
				return transId;

			} else {
				throw new LMSException(
						"no data insert into main transaction master");
			}

		} catch (SQLException e) {
			logger.debug("Exception : " + e);
			e.printStackTrace();
			throw new LMSException(e);
		}

	}

	@SuppressWarnings("unchecked")
	public Map plrRegistrationAndApproval(UserInfoBean userInfoBean,
			MainPWTDrawBean mainPwtDrawBean, String playerType, int playerId,
			PlayerBean plrBean, String rootPath, String highPrizeScheme,
			boolean isAnonymous) throws LMSException {
		BOPwtProcessHelper boHelper = new BOPwtProcessHelper();
		//int pwtGameNo = 0;
		Map pwtAppMap = null;
		ServiceResponse sRes = null;
		ServiceRequest sReq = null;
		IServiceDelegate delegate = null;
		Connection connection = null;
		try {
			// For RG LIMIT FOR AGENT
			if(getRgStatus(userInfoBean, mainPwtDrawBean.getTotlticketAmount())){
				return pwtAppMap;
				}
			connection = DBConnect.getConnection();
			connection.setAutoCommit(false);
			// register player
			if (!isAnonymous && plrBean != null) {
				playerId = new PlayerVerifyHelperForApp().registerPlayer(
						plrBean, connection);
			} else if (isAnonymous) {
				playerId = 1; // hard coded for Anonymous player
			}

			logger.debug("Player id is " + playerId
					+ " for that approval required");

			if (playerId <= 0) {
				throw new LMSException(
						"error while player registration process player id is"
								+ playerId);
			}
			String recIdForApp = GenerateRecieptNo
					.generateRequestIdDraw("DGREQUEST");

			double netPwtAmt = 0.0;
			boolean isNormlpay = false;
			boolean isResAwaited = false;
			logger.debug("inside panel wise");
			List<PWTDrawBean> pwtDrawBeanList = mainPwtDrawBean
					.getWinningBeanList();
			for (PWTDrawBean pwtDrawBean : pwtDrawBeanList) {
				if (pwtDrawBean.isResAwaited())
					isResAwaited = true;
				if ("DRAW".equalsIgnoreCase(pwtDrawBean.getPwtTicketType())) {
					//pwtGameNo = pwtDrawBean.getGameNo();
					if (pwtDrawBean.getDrawWinList() != null
							&& pwtDrawBean.getDrawWinList().size() > 0) {
						for (DrawIdBean drawIdBean : pwtDrawBean
								.getDrawWinList()) {

							if (drawIdBean.getPanelWinList() != null) {

								for (PanelIdBean panelIdBean : drawIdBean
										.getPanelWinList()) {
									if (panelIdBean.getWinningAmt() == 0.0) {
										panelIdBean.setStatus("NON WIN");
									}
									if (panelIdBean.isValid()) {
										isNormlpay = true;
										// fetch limits of agent
										CommonFunctionsHelper commonFunction = new CommonFunctionsHelper();
										OrgPwtLimitBean orgPwtLimit = commonFunction
												.fetchPwtLimitsOfOrgnization(
														userInfoBean
																.getUserOrgId(),
														connection);
										double pwtAmt = panelIdBean
												.getWinningAmt();
										int reqToOrgId = 0;
										String reqToOrgType = null;
										String remarks = null;
										String reqStatus = null;
										String approvedByType = null;
										int approvedByUserId = 0;
										int approvedByOrgId = 0;
										logger
												.debug("panelIdBean.isValid()===="
														+ panelIdBean.isValid());
										if (panelIdBean.isAppReq()) { // means
											// approval from
											// BO master is
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

										} else {
											reqToOrgId = userInfoBean
													.getUserOrgId();
											reqToOrgType = userInfoBean
													.getUserType();
											reqStatus = "PAID";
											approvedByType = "AGENT";
											approvedByUserId = userInfoBean
													.getUserId();
											approvedByOrgId = reqToOrgId;

											if (!isAnonymous) {
												// Pay as Registered Player
												remarks = "Paid as Registered Player";
											} else {
												// Pay as Anonymous Player
												remarks = "Paid as Anonymous Player";
											}
										}

										logger
												.debug("Approval requested to orgId = "
														+ reqToOrgId
														+ "  and user type = "
														+ reqToOrgType);

										System.out
												.println("^^^^^^^^^***** panel wise  pwtAmt"
														+ pwtAmt);

										// insert into Approval table
										if (playerType == null) {
											playerType = "anonymous";
										}
										double govtCommPwt =0;
										GameMasterLMSBean gameMasterLMSBean =Util.getGameMasterLMSBean(pwtDrawBean.getGameId());
										if(panelIdBean.getWinningAmt()>=Double.parseDouble(Utility.getPropertyValue("PLAYER_WINNING_TAX_APPLICABLE_AMOUNT")))
											govtCommPwt = gameMasterLMSBean.getGovtCommPwt();
										double tax = govtCommPwt*0.01*panelIdBean.getWinningAmt();
										
										String insertAppQuery = "insert into  st_dg_approval_req_master (party_type ,request_id,party_id,ticket_nbr,draw_id,panel_id,game_id,pwt_amt,tax_amt,net_amt,req_status,requester_type,requested_by_user_id,requested_by_org_id,requested_to_org_id,requested_to_type,approved_by_type, approved_by_user_id , approved_by_org_id,pay_req_for_org_type,pay_request_for_org_id,approval_date,request_date, remarks) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
										logger.debug("@@@@@@@@@@@playerType"
												+ playerType.toUpperCase());
										pstmt = connection
												.prepareStatement(insertAppQuery);
										pstmt.setString(1, playerType
												.toUpperCase());
										pstmt.setString(2, recIdForApp);

										boolean isPlr = "player"
												.equalsIgnoreCase(playerType
														.trim());
										if (isPlr) {
											pstmt.setInt(3, playerId);
										} else {
											pstmt.setObject(3, null);
										}
										// ticket is entered with reprint count
										// in Approval req master on 04/04/2011
										pstmt
												.setObject(
														4,
														pwtDrawBean
																.getTicketNo()
																+ pwtDrawBean
																		.getReprintCount());
										pstmt.setInt(5, drawIdBean.getDrawId());
										pstmt.setInt(6, panelIdBean
												.getPanelId());
										pstmt
												.setInt(7, pwtDrawBean
														.getGameId());
										pstmt.setDouble(8, panelIdBean
												.getWinningAmt());
										pstmt.setDouble(9,tax);
										pstmt.setDouble(10, CommonMethods
												.fmtToTwoDecimal(panelIdBean
														.getWinningAmt()-tax));
										pstmt.setString(11, reqStatus);
										pstmt.setString(12, userInfoBean
												.getUserType());
										pstmt.setInt(13, userInfoBean
												.getUserId());
										pstmt.setInt(14, userInfoBean
												.getUserOrgId());
										pstmt.setInt(15, reqToOrgId);
										pstmt.setString(16, reqToOrgType);
										if (drawIdBean.isAppReq()) {
											pstmt.setObject(17, null);
											pstmt.setObject(18, null);
											pstmt.setObject(19, null);
											pstmt.setObject(20, null);
											pstmt.setObject(21, null);
											pstmt.setObject(22, null);
										} else {
											pstmt.setString(17, approvedByType);
											pstmt.setInt(18, approvedByUserId);
											pstmt.setInt(19, approvedByOrgId);
											pstmt.setString(20, approvedByType);
											pstmt.setInt(21, approvedByOrgId);
											pstmt
													.setTimestamp(
															22,
															new java.sql.Timestamp(
																	new java.util.Date()
																			.getTime()));
										}
										pstmt.setTimestamp(23,
												new java.sql.Timestamp(
														new java.util.Date()
																.getTime()));
										pstmt.setString(24, remarks);
										logger.debug("insertAppQuery pppppp = "
												+ pstmt);
										pstmt.executeUpdate();
										ResultSet rs = pstmt.getGeneratedKeys();
										int reqId = 0;
										if (rs.next()) {
											reqId = rs.getInt(1);
										} else {
											throw new LMSException(
													"NO Data Inserted in st_pwt_approval_request_master table");
										}

										// insert in draw pwt inv table
										reqStatus = "CLAIM_AGT";
										logger.debug("@@@@@@@@@new status is"
												+ reqStatus);
										String insIntoDGPwtInvQuery = "insert into st_dg_pwt_inv_?(ticket_nbr, draw_id,panel_id, pwt_amt, status,is_direct_plr) values (?, ?, ?, ?, ?,?)";
										PreparedStatement insIntoDGPwtInvPstmt = connection
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
																.fmtToTwoDecimal(pwtAmt));
										insIntoDGPwtInvPstmt.setString(6,
												reqStatus);
										insIntoDGPwtInvPstmt.setString(7, "Y");
										insIntoDGPwtInvPstmt.executeUpdate();
										netPwtAmt = netPwtAmt + pwtAmt-tax;

										boolean isAutoScrap = "YES"
												.equalsIgnoreCase(orgPwtLimit
														.getIsPwtAutoScrap())
												&& pwtAmt <= orgPwtLimit
														.getScrapLimit() ? true
												: false;
										long transId = agentPwtPayment(
												pwtDrawBean.getTicketNo(),
												drawIdBean.getDrawId(),
												playerId,
												CommonMethods
														.fmtToTwoDecimal(pwtAmt),
												tax, reqId, "CASH",
												userInfoBean.getUserOrgId(),
												userInfoBean.getUserId(),
												pwtDrawBean.getGameNo(),
												pwtDrawBean.getGameId(),
												connection, panelIdBean
														.getPanelId(),
												"PANEL_WISE", isAutoScrap);
										panelIdBean.setStatus("NORMAL_PAY");
										if (transId > 0) {
											String updateAppTable = "update  st_dg_approval_req_master  set  payment_done_by_type =?, payment_done_by =? ,transaction_id=? where  task_id = ?";
											PreparedStatement pstmt = connection
													.prepareStatement(updateAppTable);
											pstmt.setString(1, "AGENT");
											pstmt.setInt(2, userInfoBean
													.getUserOrgId());
											pstmt.setLong(3, transId);
											pstmt.setInt(4, reqId);
											logger
													.debug("update  st_dg_approval_req_master Query::::"
															+ pstmt);
											pstmt.executeUpdate();
											String updateDGPwtInvQuery = "update st_dg_pwt_inv_? set status = ? ,  agent_transaction_id = ? "
													+ " where ticket_nbr = ? and draw_id = ? and panel_id= ? ";

											PreparedStatement updatDGPwtInvPstmt = connection
													.prepareStatement(updateDGPwtInvQuery);
											updatDGPwtInvPstmt.setInt(1,
													pwtDrawBean.getGameId());
											updatDGPwtInvPstmt.setString(2,
													"CLAIM_AGT");
											updatDGPwtInvPstmt.setLong(3,
													transId);
											updatDGPwtInvPstmt.setString(4,
													pwtDrawBean.getTicketNo());
											updatDGPwtInvPstmt.setInt(5,
													drawIdBean.getDrawId());
											updatDGPwtInvPstmt.setInt(6,
													panelIdBean.getPanelId());
											logger
													.debug("updatDGPwtInvPstmt = "
															+ updatDGPwtInvPstmt);
											updatDGPwtInvPstmt.executeUpdate();

										}

									}
								}

							}
						}
						// }

						// Draw Game Updation of Ticket
						if (isNormlpay) {
							pwtDrawBean.setStatus("NORMAL_PAY");
							sReq = new ServiceRequest();
							sRes = new ServiceResponse();
							sReq.setServiceName(ServiceName.PWT_MGMT);
							sReq
									.setServiceMethod(ServiceMethodName.DRAWGAME_PWT_UPDATE);
							delegate = ServiceDelegate.getInstance();
							sReq.setServiceData(pwtDrawBean);
							sRes = delegate.getResponse(sReq);
							if (sRes.getIsSuccess()) {
								connection.commit();
								pwtDrawBean.setStatus("SUCCESS");
							} else {
								logger
										.debug("************^^^^^^^^inside error while updating draw game ");
								connection.close();
								pwtDrawBean.setStatus("ERROR");
								throw new LMSException("Pwt not updated in DGE...");
							}
						}
					}
				} else if ("RAFFLE".equalsIgnoreCase(pwtDrawBean.getPwtTicketType())) {
					/*String rafflwPwtAmtNisResAwaited = boHelper.doRafflePWTPaymet(pwtDrawBean, userInfoBean, isAnonymous, playerType, recIdForApp, playerId, connection);
					double rafflwPwtAmt = Double.parseDouble((rafflwPwtAmtNisResAwaited.split(":")[0]));
					netPwtAmt = netPwtAmt + rafflwPwtAmt;
					if (!isResAwaited)
						isResAwaited = Boolean.parseBoolean(rafflwPwtAmtNisResAwaited.split(":")[1]);
					connection.commit();*/
					List<RaffleDrawIdBean> raffleDrawIdBeanList = pwtDrawBean.getRaffleDrawIdBeanList();
					for (RaffleDrawIdBean raffleDrawIdBean : raffleDrawIdBeanList) {
						if (raffleDrawIdBean.isResAwaited()) {
							isResAwaited = true;
						}

						if (raffleDrawIdBean.isValid()) {
							isNormlpay = true;
							CommonFunctionsHelper commonFunction = new CommonFunctionsHelper();
							OrgPwtLimitBean orgPwtLimit = commonFunction.fetchPwtLimitsOfOrgnization(userInfoBean.getUserOrgId(), connection);
							double pwtAmt = Double.parseDouble(raffleDrawIdBean.getWinningAmt());
							int reqToOrgId = 0;
							String reqToOrgType = null;
							String remarks = null;
							String reqStatus = null;
							String approvedByType = null;
							int approvedByUserId = 0;
							int approvedByOrgId = 0;
							if (raffleDrawIdBean.isAppReq()) {
								reqToOrgId = userInfoBean.getParentOrgId();
								reqToOrgType = "BO";
								remarks = "requested to BO master  For Approval";
								reqStatus = "PND_MAS";
							} else {
								reqToOrgId = userInfoBean.getUserOrgId();
								reqToOrgType = userInfoBean.getUserType();
								reqStatus = "PAID";
								approvedByType = "AGENT";
								approvedByUserId = userInfoBean.getUserId();
								approvedByOrgId = reqToOrgId;
								if (!isAnonymous) {
									remarks = "Paid as Registered Player";
								} else {
									remarks = "Paid as Anonymous Player";
								}
							}
							if (playerType == null) {
								playerType = "anonymous";
							}
							String insertAppQuery = "insert into  st_dg_approval_req_master (party_type ,request_id,party_id,ticket_nbr,draw_id,panel_id,game_id,pwt_amt,tax_amt,net_amt,req_status,requester_type,requested_by_user_id,requested_by_org_id,requested_to_org_id,requested_to_type,approved_by_type, approved_by_user_id , approved_by_org_id,pay_req_for_org_type,pay_request_for_org_id,approval_date,request_date, remarks) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
							pstmt = connection.prepareStatement(insertAppQuery);
							pstmt.setString(1, playerType.toUpperCase());
							pstmt.setString(2, recIdForApp);
							boolean isPlr = "player".equalsIgnoreCase(playerType.trim());
							if (isPlr) {
								pstmt.setInt(3, playerId);
							} else {
								pstmt.setObject(3, null);
							}
							pstmt.setObject(4, raffleDrawIdBean.getRaffleTicketNumberInDB() + pwtDrawBean.getReprintCount());
							pstmt.setInt(5, raffleDrawIdBean.getDrawId());
							pstmt.setInt(6, 0);
							pstmt.setInt(7, pwtDrawBean.getGameId());
							pstmt.setDouble(8, Double.parseDouble(raffleDrawIdBean.getWinningAmt()));
							pstmt.setDouble(9, 0.0);
							pstmt.setDouble(10, CommonMethods.fmtToTwoDecimal(Double.parseDouble(raffleDrawIdBean.getWinningAmt())));
							pstmt.setString(11, reqStatus);
							pstmt.setString(12, userInfoBean.getUserType());
							pstmt.setInt(13, userInfoBean.getUserId());
							pstmt.setInt(14, userInfoBean.getUserOrgId());
							pstmt.setInt(15, reqToOrgId);
							pstmt.setString(16, reqToOrgType);
							if (raffleDrawIdBean.isAppReq()) {
								pstmt.setObject(17, null);
								pstmt.setObject(18, null);
								pstmt.setObject(19, null);
								pstmt.setObject(20, null);
								pstmt.setObject(21, null);
								pstmt.setObject(22, null);
							} else {
								pstmt.setString(17, approvedByType);
								pstmt.setInt(18, approvedByUserId);
								pstmt.setInt(19, approvedByOrgId);
								pstmt.setString(20, approvedByType);
								pstmt.setInt(21, approvedByOrgId);
								pstmt.setTimestamp(22, new java.sql.Timestamp(new java.util.Date().getTime()));
							}
							pstmt.setTimestamp(23, new java.sql.Timestamp(new java.util.Date().getTime()));
							pstmt.setString(24, remarks);
							pstmt.executeUpdate();
							ResultSet rs = pstmt.getGeneratedKeys();
							int reqId = 0;
							if (rs.next()) {
								reqId = rs.getInt(1);
							} else {
								throw new LMSException("NO Data Inserted in st_pwt_approval_request_master table");
							}

							reqStatus = "CLAIM_AGT";
							String insIntoDGPwtInvQuery = "insert into st_dg_pwt_inv_?(ticket_nbr, draw_id,panel_id, pwt_amt, status,is_direct_plr) values (?, ?, ?, ?, ?,?)";
							PreparedStatement insIntoDGPwtInvPstmt = connection.prepareStatement(insIntoDGPwtInvQuery);
							insIntoDGPwtInvPstmt.setInt(1, pwtDrawBean.getGameId());
							insIntoDGPwtInvPstmt.setString(2, raffleDrawIdBean.getRaffleTicketNumberInDB());
							insIntoDGPwtInvPstmt.setInt(3, raffleDrawIdBean.getDrawId());
							insIntoDGPwtInvPstmt.setInt(4, 0);
							insIntoDGPwtInvPstmt.setDouble(5, CommonMethods.fmtToTwoDecimal(pwtAmt));
							insIntoDGPwtInvPstmt.setString(6, reqStatus);
							insIntoDGPwtInvPstmt.setString(7, "Y");
							insIntoDGPwtInvPstmt.executeUpdate();
							netPwtAmt = netPwtAmt + pwtAmt;
							boolean isAutoScrap = "YES".equalsIgnoreCase(orgPwtLimit.getIsPwtAutoScrap()) && pwtAmt <= orgPwtLimit.getScrapLimit() ? true : false;
							long transId = agentPwtPayment(pwtDrawBean.getTicketNo(), raffleDrawIdBean.getDrawId(), playerId, CommonMethods.fmtToTwoDecimal(pwtAmt), 0.0, reqId, "CASH", userInfoBean.getUserOrgId(), userInfoBean.getUserId(), pwtDrawBean.getGameNo(), pwtDrawBean.getGameId(), connection, 0, "PANEL_WISE", isAutoScrap);
							raffleDrawIdBean.setStatus("NORMAL_PAY");
							if (transId > 0) {
								String updateAppTable = "update  st_dg_approval_req_master  set  payment_done_by_type =?, payment_done_by =? ,transaction_id=? where  task_id = ?";
								PreparedStatement pstmt = connection.prepareStatement(updateAppTable);
								pstmt.setString(1, "AGENT");
								pstmt.setInt(2, userInfoBean.getUserOrgId());
								pstmt.setLong(3, transId);
								pstmt.setInt(4, reqId);
								pstmt.executeUpdate();
								String updateDGPwtInvQuery = "update st_dg_pwt_inv_? set status = ? ,  agent_transaction_id = ? " + " where ticket_nbr = ? and draw_id = ? and panel_id= ? ";
								PreparedStatement updatDGPwtInvPstmt = connection.prepareStatement(updateDGPwtInvQuery);
								updatDGPwtInvPstmt.setInt(1, pwtDrawBean.getGameId());
								updatDGPwtInvPstmt.setString(2, "CLAIM_AGT");
								updatDGPwtInvPstmt.setLong(3, transId);
								updatDGPwtInvPstmt.setString(4, raffleDrawIdBean.getRaffleTicketNumberInDB());
								updatDGPwtInvPstmt.setInt(5, raffleDrawIdBean.getDrawId());
								updatDGPwtInvPstmt.setInt(6, 0);
								updatDGPwtInvPstmt.executeUpdate();
							}
						}
						if (isNormlpay) {
							pwtDrawBean.setStatus("NORMAL_PAY");
							sReq = new ServiceRequest();
							sRes = new ServiceResponse();
							sReq.setServiceName(ServiceName.PWT_MGMT);
							sReq.setServiceMethod(ServiceMethodName.RAFFLE_PWT_UPDATE);
							delegate = ServiceDelegate.getInstance();
							sReq.setServiceData(pwtDrawBean);
							sRes = delegate.getResponse(sReq);
							if (sRes.getIsSuccess()) {
								connection.commit();
								pwtDrawBean.setStatus("SUCCESS");
							} else {
								connection.close();
								pwtDrawBean.setStatus("ERROR");
								throw new LMSException("Pwt not updated in DGE...");
							}
						}
					}
				}
			}

			// call reprint process if ticket has to be reprint

			if (isResAwaited && netPwtAmt > 0) {
				DrawGameRPOSHelper helper = new DrawGameRPOSHelper();
				String ticketNumber=mainPwtDrawBean.getTicketNo();
				int len = ticketNumber.length();
				Object gameBean = helper.reprintTicket(userInfoBean, true,
						/*len==ConfigurationVariables.barcodeCount?ticketNumber.substring(0, len - 4):ticketNumber.substring(0, len - 2)*/
							Util.getTktWithoutRpcNBarCodeCount(ticketNumber, len),
						false, null, "WEB");
				mainPwtDrawBean.setPurchaseBean(gameBean);
				mainPwtDrawBean.setReprint(true);
				mainPwtDrawBean.setStatus("SUCCESS");
			}
			pwtAppMap = new TreeMap();
			if ("DRAW".equalsIgnoreCase(mainPwtDrawBean.getPwtTicketType())) {

				pwtAppMap.put("PWT_RES_BEAN", mainPwtDrawBean);
				pwtAppMap.put("GAME_NAME", Util.getGameName(mainPwtDrawBean.getGameId())
						.toUpperCase()
						+ "_PWT");
				pwtAppMap.put("isAnonymous", isAnonymous);
				pwtAppMap.put("NET_AMOUNT_PAID", netPwtAmt);
			} else if ("RAFFLE".equalsIgnoreCase(mainPwtDrawBean
					.getPwtTicketType())) {
				System.out.println();
				pwtAppMap.put("PWT_RES_BEAN", mainPwtDrawBean);
				pwtAppMap.put("GAME_NAME", Util.getGameName(
						Util.getGamenoFromTktnumber(mainPwtDrawBean
								.getTicketNo())).toUpperCase()
						+ "_PWT");
				pwtAppMap.put("isAnonymous", isAnonymous);
				pwtAppMap.put("NET_AMOUNT_PAID", netPwtAmt);
			}
			return pwtAppMap;

		} catch (Exception e) {
			logger.debug("Exception : " + e);
			e.printStackTrace();
			throw new LMSException(e);
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					logger.debug("Exception : " + e);
					e.printStackTrace();
					throw new LMSException(e);
				}
			}
		}
	}
	
	public Map<String, PWTDrawBean> retPwtSubmit(
			Map<String, PWTDrawBean> pwtMap, UserInfoBean userInfo,
			int partyOrgId) throws LMSException {
		ServletContext sc = ServletActionContext.getServletContext();
		String refMerchantId = (String) sc.getAttribute("REF_MERCHANT_ID");

		PWTDrawBean pwtBean = null;
		Connection connection = DBConnect.getConnection();
		PreparedStatement psmt = null;
		ResultSet getUsrId = null;
		int retUserId = 0;
		String fecthUsrQry = "select user_id from st_lms_user_master where organization_id=? and isrolehead='Y'";
		List<PWTDrawBean> winningList = new ArrayList<PWTDrawBean>();
		ServiceRequest sReq = new ServiceRequest();
		ServiceResponse sRes = new ServiceResponse();
		sReq.setServiceName(ServiceName.PWT_MGMT);
		sReq.setServiceMethod(ServiceMethodName.DRAWGAME_PWT_UPDATE);
		IServiceDelegate delegate = ServiceDelegate.getInstance();

		RetPWTProcessHelper retHelper = new RetPWTProcessHelper();
		try {
			connection.setAutoCommit(false);
			CommonFunctionsHelper commonFunction = new CommonFunctionsHelper();
			OrgPwtLimitBean orgPwtLimit = commonFunction
					.fetchPwtLimitsOfOrgnization(userInfo.getUserOrgId(),
							connection);
			psmt = connection.prepareStatement(fecthUsrQry);
			psmt.setInt(1, partyOrgId);
			logger.debug("fecthUsrQry:::>>>>>" + psmt);
			getUsrId = psmt.executeQuery();
			if (getUsrId.next()) {
				retUserId = getUsrId.getInt(1);
			}// else case to be implemented

			Iterator itr = pwtMap.entrySet().iterator();
			while (itr.hasNext()) {
				Map.Entry pair = (Map.Entry) itr.next();
				pwtBean = (PWTDrawBean) pair.getValue();
				if (pwtBean.getStatus().equalsIgnoreCase("SUCCESS")) {
					if (pwtBean.getPwtStatus().equalsIgnoreCase("NORMAL_PAY")) {
						pwtBean.setGameId(Util.getGameId(pwtBean.getGameDispName()));
						winningList.add(pwtBean);
						logger.debug("Winning Bean added to list");
					}
				}
			}

			for (int i = 0; i < winningList.size(); i++) {
				pwtBean = (PWTDrawBean) winningList.get(i);
				pwtBean.setPartyId(partyOrgId);
				pwtBean.setUserId(retUserId);
				pwtBean.setPartyType("RETAILER");
				pwtBean.setRefMerchantId(refMerchantId);
				boolean isAutoScrap = "YES".equalsIgnoreCase(orgPwtLimit
						.getIsPwtAutoScrap())
						&& pwtBean.getTotalAmount() <= orgPwtLimit
								.getScrapLimit() ? true : false;
				for (DrawIdBean drawIdWinningBean : pwtBean.getDrawWinList()) {
					int drawId = drawIdWinningBean.getDrawId();
					PanelIdBean panelIdBean = null;
					if (drawIdWinningBean.getPanelWinList() != null) {
						for (Object panelIdBeanObj : drawIdWinningBean
								.getPanelWinList()) {

							panelIdBean = (PanelIdBean) panelIdBeanObj;
							if (panelIdBean.getStatus().equalsIgnoreCase(
									"NORMAL_PAY")) {
								logger
										.debug("updating REtailer balance'''''''''''''''''");
								retHelper.retPwtPayment(userInfo.getUserId(),
										partyOrgId, userInfo.getUserOrgId(),
										pwtBean.getGameId(), isAutoScrap,
										panelIdBean.getWinningAmt(), drawId,
										panelIdBean.getPanelId(), pwtBean
												.getTicketNo(), connection,
										false, false);
							}
						}
					}
				}
				sReq.setServiceData(pwtBean);
				sRes = delegate.getResponse(sReq);
				if (sRes.getIsSuccess()) {

					pwtBean.setPwtStatus("SUBMITTED");

				} else {

					pwtBean.setPwtStatus("ERROR");
				}

			}

			/*
			 * CommonFunctionsHelper helper = new CommonFunctionsHelper();
			 * helper.updateClmableBalOfOrg(retUserId, partyOrgId, "RETAILER",
			 * userInfo.getUserOrgId(), connection);
			 */connection.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return pwtMap;
	}

	public Map<String, PWTDrawBean> retTicketVerifyNSave(String[] ticketNums,
			UserInfoBean userInfo, int partyOrgId) throws LMSException {
		Map<String, PWTDrawBean> pwtTicketMap = new LinkedHashMap<String, PWTDrawBean>();
		PWTDrawBean pwtBean = null;
		Connection connection = DBConnect.getConnection();
		List<PWTDrawBean> winTicketList = new ArrayList<PWTDrawBean>();
		String highPrizeCriteria = (String) ServletActionContext
				.getServletContext().getAttribute("HIGH_PRIZE_CRITERIA");
		String highPrizeAmt = (String) ServletActionContext.getServletContext()
				.getAttribute("HIGH_PRIZE_AMT");

		ServiceRequest sReq = new ServiceRequest();
		ServiceResponse sRes = new ServiceResponse();
		sReq.setServiceName(ServiceName.PWT_MGMT);
		sReq.setServiceMethod(ServiceMethodName.DRAWGAME_PRZE_WINNING_TICKET);
		IServiceDelegate delegate = ServiceDelegate.getInstance();

		try {
			CommonFunctionsHelper commonFunction = new CommonFunctionsHelper();
			OrgPwtLimitBean orgPwtLimit = commonFunction
					.fetchPwtLimitsOfOrgnization(partyOrgId, connection);

			for (int i = 0; i < ticketNums.length; i++) {
				boolean isActive = false;
				if (ticketNums[i] != null && !ticketNums[i].trim().equals("")) {
					pwtBean = new PWTDrawBean();
					pwtBean.setTicketNo(ticketNums[i]);
					sReq.setServiceData(pwtBean);
					sRes = delegate.getResponse(sReq);
					
					Type type = new TypeToken<PWTDrawBean>(){}.getType();
					pwtBean = (PWTDrawBean)new Gson().fromJson((JsonElement)sRes.getResponseData(), type);
//					pwtBean = (PWTDrawBean) sRes.getResponseData();
					
					if (sRes.getIsSuccess()) {
						if(Boolean.parseBoolean((String)com.skilrock.lms.common.Utility.getPropertyValue("DO_MATH_ROUNDING_FOR_PWT_AMT")))
							CommonMethods.doRoundingForPwtAmt(pwtBean);

						for (DrawIdBean drawBean : pwtBean.getDrawWinList()) {
							logger.debug("draw status::::after dge:::::"
									+ drawBean.getStatus());
							if ("RES_AWAITED".equalsIgnoreCase(drawBean
									.getStatus())) {
								isActive = true;
								break;
							}
						}
						if (isActive) {
							pwtBean.setStatus("RES_AWAITED");
						} else {
							pwtBean.setStatus("SUCCESS");
						}
					} else {
						pwtBean.setStatus("ERROR");
					}
					winTicketList.add(pwtBean);

				}
			}
			double retTotAmt = 0.0;
			RetailerPwtProcessHelper retHelper = new RetailerPwtProcessHelper();
			for (int i = 0; i < winTicketList.size(); i++) {
				double ticketWinAmt = 0.0;
				pwtBean = winTicketList.get(i);
				if (pwtBean.getStatus().equalsIgnoreCase("SUCCESS")) {
					retHelper.verifyPwtAmount(pwtBean.getTicketNo(), pwtBean
							.getGameNo(), connection, userInfo,
							highPrizeCriteria, highPrizeAmt, pwtBean);

					ticketWinAmt = pwtBean.getTotalAmount();
					if (ticketWinAmt > 0) {
						if (orgPwtLimit == null) {
							throw new LMSException(
									"PWT Limits Are Not defined Properly!!");

						} else { // test PWT amount with limit process

							if (ticketWinAmt <= orgPwtLimit
									.getVerificationLimit()) {
								System.out
										.println("inside verification limit == ");
								boolean isHighPrizeFlag = highPrizeCriteria
										.equals("amt")
										&& ticketWinAmt > Double
												.parseDouble(highPrizeAmt);
								// check for HIGH Prize Or is Approval Required

								if (ticketWinAmt > orgPwtLimit
										.getApprovalLimit()
										|| isHighPrizeFlag) {

									pwtBean.setPwtStatus("HIGH_PRIZE");

								} else if (ticketWinAmt <= orgPwtLimit
										.getPayLimit()) {
									pwtBean.setPwtStatus("NORMAL_PAY");

								} else {
									pwtBean.setPwtStatus("OUT_PAY_LIMIT");

								}
							} else {
								logger
										.debug("Out of Range of Retailer Verification Limit.");
								pwtBean.setPwtStatus("OUT_VERIFY_LIMIT");
							}
						}
					} else {
						pwtBean.setPwtStatus("NON_WIN_OR_CLAIMED");
					}
					logger.debug("@@@@@@@@@@@@@@Ticket status is : "
							+ pwtBean.getPwtStatus());

					if (pwtBean.getPwtStatus().equalsIgnoreCase("NORMAL_PAY")) {
						retTotAmt = retTotAmt + pwtBean.getTotalAmount();
					}
				} else if (pwtBean.getStatus().equalsIgnoreCase("RES_AWAITED")) {
					pwtBean.setPwtStatus("Ticket Still Active");
				} else if (pwtBean.getStatus().equalsIgnoreCase("ERROR")) {
					pwtBean.setPwtStatus("Invalid Ticket");

				}
				pwtTicketMap.put(pwtBean.getTicketNo()
						+ (pwtBean.getReprintCount() == null ? "" : pwtBean
								.getReprintCount()), pwtBean);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return pwtTicketMap;
	}

	public PWTDrawBean verifyAndSaveTicketNoDirPlr(PWTDrawBean winningBean,
			UserInfoBean userInfoBean, String pwtAmtForMasterApproval,
			String highPrizeScheme, String highPrizeAmt,
			String highPrizeCriteria, Connection connection) {

		ResultSet rs = null;
		PreparedStatement ps = null;

		int retOrgId = 0;
		int agtOrgId = 0;

		ServiceRequest sReq = null;
		ServiceResponse sRes = null;
		IServiceDelegate delegate = null;

		RetailerPwtProcessHelper retHelper = null;

		try {
			/*if ("NO".equalsIgnoreCase((String) ServletActionContext
					.getServletContext().getAttribute("PWT_CLAIM_EVERYWHERE"))) {
				ps = connection
						.prepareStatement("select organization_id from st_lms_user_master where user_id="
								+ Util.getUserIdFromTicket(winningBean
										.getTicketNo()));
				rs = ps.executeQuery();
				if (rs.next()) {
					retOrgId = rs.getInt(1);
				}
				ps = connection
						.prepareStatement("select parent_id from st_lms_organization_master where organization_id = "
								+ retOrgId);
				rs = ps.executeQuery();
				if (rs.next()) {
					agtOrgId = rs.getInt(1);
				}
				logger.debug("Retailer orgid:" + retOrgId
						+ "...ret'sAgtOrgId = " + agtOrgId + "&agtid= "
						+ userInfoBean.getUserOrgId());
				if (userInfoBean.getUserOrgId() != agtOrgId) {
					winningBean.setStatus("UN_AUTH");
					return winningBean;
				}
			}*/
				 // Added By Mandeep for PWT CENTERS
			/*if(!Util.canClaimAnywhere(winningBean.getTicketNo(), true, userInfoBean.getUserOrgId())){
				winningBean.setStatus("UN_AUTH");
				return winningBean;
			}*/
			
			
			sRes = new ServiceResponse();
			sReq = new ServiceRequest();
			sReq.setServiceName(ServiceName.PWT_MGMT);
			sReq.setServiceMethod(ServiceMethodName.DRAWGAME_PRZE_WINNING_TICKET);
			sReq.setServiceData(winningBean);
			delegate = ServiceDelegate.getInstance();
			sRes = delegate.getResponse(sReq);
			Type type = new TypeToken<PWTDrawBean>(){}.getType();
			winningBean = (PWTDrawBean)new Gson().fromJson((JsonElement)sRes.getResponseData(), type);
//			winningBean = (PWTDrawBean) sRes.getResponseData();
			logger.debug("If Winning Is Valid " + winningBean.isValid());
			if (sRes.getIsSuccess()) {

				if(Boolean.parseBoolean((String)com.skilrock.lms.common.Utility.getPropertyValue("DO_MATH_ROUNDING_FOR_PWT_AMT")))
					CommonMethods.doRoundingForPwtAmt(winningBean);
				logger.debug("HIGH_PRIZE_AMT" + highPrizeAmt);
				retHelper = new RetailerPwtProcessHelper();
				retHelper.verifyPwtAmount(winningBean.getTicketNo(),
						winningBean.getGameId(), connection, userInfoBean,
						highPrizeCriteria, highPrizeAmt, winningBean);
				String pwtStatus = null;
				boolean isOutVerify = false;
				boolean isOutPay = false;
				boolean isHighPrize = false;
				boolean isPay = false;
				boolean isClmPend = false;
				for (DrawIdBean drawIdWinningBean : winningBean
						.getDrawWinList()) {

					pwtStatus = drawIdWinningBean.getStatus();
					if ("OUT_VERIFY_LIMIT".equals(pwtStatus)) {
						isOutVerify = true;
						break;
					} else if ("OUT_PAY_LIMIT".equals(pwtStatus)) {
						isOutPay = true;
						break;
					} else if ("HIGH_PRIZE".equals(pwtStatus)) {
						winningBean.setHighPrize(true);
						isHighPrize = true;
						break;
					} else if ("NORMAL_PAY".equals(pwtStatus)) {
						isPay = true;
						break;
					} else if ("CLAIM_PENDING".equals(pwtStatus)) {
						isClmPend = true;
					}
				}
				if (isOutVerify || isOutPay) {
					winningBean.setPwtStatus("OUT_LIMITS");
				} else if (isHighPrize || isPay) {
					winningBean.setPwtStatus("PAY_PWT");
					winningBean.setWinTkt(true);
				} else if (isClmPend) {
					winningBean.setPwtStatus("CLAIM_PENDING");
					winningBean.setWinTkt(false);
				} else {
					winningBean.setWinTkt(false);
				}
				winningBean.setGameDispName(Util.getGameDisplayName(winningBean
						.getGameId()));
				//winningBean.setGameId(Util.getGameIdFromGameNumber(winningBean.getGameNo()));
				winningBean.setStatus("SUCCESS");
				logger.debug("winningBean.isWinTkt()::::::"
						+ winningBean.isWinTkt());

			} else {
				logger.debug("inside invalid ticket ");
			}
		} catch (Exception e) {
			e.printStackTrace();
			winningBean.setStatus("ERROR");
		}
		return winningBean;
	}
	
	public MainPWTDrawBean newMethod(MainPWTDrawBean  mainPwtBean,
			UserInfoBean userInfoBean, String pwtAmtForMasterApproval,
			String highPrizeScheme, String refMerchantId, String highPrizeAmt,
			String highPrizeCriteria) throws LMSException {

		List<PWTDrawBean> winningBeanList = new ArrayList<PWTDrawBean>();
		String ticketNumber =null;
		Connection connection = null;
		int gameNo = 0;
		int barCodeCount=-1;
		boolean byPassDates=false;
		BOPwtProcessHelper boHelper = null;
		
		try {
			ticketNumber = Util.getTicketNumber(mainPwtBean.getTicketNo(), 3);
			if (mainPwtBean.getInpType() == 1
					|| mainPwtBean.getTicketNo()
							.length() == com.skilrock.lms.common.ConfigurationVariables.barcodeCount) {
				barCodeCount = Integer
						.parseInt(Util.getBarCodeCountFromTicketNumber(mainPwtBean
								.getTicketNo()));
			}
			connection = DBConnect.getConnection();
			gameNo = Util.getGamenoFromTktnumber(ticketNumber);
			if (gameNo <= 0) {
				mainPwtBean.setStatus("ERROR_INVALID");
				return mainPwtBean;
			}
			int gameId =Util.getGameIdFromGameNumber(gameNo);
			String gameType = Util.getGameType(gameId);
			if (gameType == null) {
				mainPwtBean.setStatus("ERROR_INVALID");
				return mainPwtBean;
			}

			if(!Util.canClaimAgent(ticketNumber, userInfoBean.getUserOrgId(), connection)) {
				mainPwtBean.setStatus("UN_AUTH");
				return mainPwtBean;
			}

			mainPwtBean.setMainTktGameNo(gameNo);
			mainPwtBean.setGameId(gameId);
			if (gameType.equalsIgnoreCase("RAFFLE")) {
				// call raffle verify process
				mainPwtBean.setPwtTicketType("RAFFLE");
				PWTDrawBean promoWinBean = new PWTDrawBean();
				promoWinBean.setTicketNo(mainPwtBean.getTicketNo());
				promoWinBean.setPartyId(userInfoBean.getUserOrgId());
				promoWinBean.setPartyType(userInfoBean.getUserType());
				promoWinBean.setUserId(userInfoBean.getUserId());
				promoWinBean.setRefMerchantId(refMerchantId);
				promoWinBean.setPwtTicketType("ORIGINAL");
				// here identify the type of request for raffle or for
				// draw(standard)
				ServiceResponse sRes = new ServiceResponse();
				ServiceRequest sReq = new ServiceRequest();
				IServiceDelegate delegate = ServiceDelegate.getInstance();
				sReq.setServiceName(ServiceName.DRAWGAME);
				sReq
						.setServiceMethod(ServiceMethodName.RAFFLE_PRZE_WINNING_TICKET);
				sReq.setServiceData(promoWinBean);
				sRes = delegate.getResponse(sReq);
				promoWinBean = (PWTDrawBean) sRes.getResponseData();
				List raffleDrawIdBeanList = promoWinBean
						.getRaffleDrawIdBeanList();
				if (raffleDrawIdBeanList.size() < 1) {
					promoWinBean.setStatus("ERROR");
				}
				if (sRes.getIsRaffleSuccess()) {
					verifyRafflePwtBean(promoWinBean, pwtAmtForMasterApproval,
							connection);
					promoWinBean.setStatus("SUCCESS");
				} else {
					promoWinBean.setValid(false);
					promoWinBean.setStatus("ERROR");
				}

				mainPwtBean.setWinningBeanList(winningBeanList);
			} else {
				mainPwtBean.setPwtTicketType("DRAW");
				PWTDrawBean drawScheduleBean = new PWTDrawBean();
				if(LMSFilterDispatcher.isByPassDatesRequired)
					byPassDates=BankUtil.isBypassPWTDates(userInfoBean.getUserId(), connection);
					drawScheduleBean.setByPassDates(byPassDates);
				drawScheduleBean.setTicketNo(ticketNumber);
				drawScheduleBean.setBarCodeCount(barCodeCount);
				drawScheduleBean.setPartyId(userInfoBean.getUserOrgId());
				drawScheduleBean.setUserId(userInfoBean.getUserId());
				drawScheduleBean.setPartyType(userInfoBean.getUserType());
				drawScheduleBean.setRefMerchantId(refMerchantId);

				drawScheduleBean = verifyAndSaveTicketNoDirPlr(
						drawScheduleBean, userInfoBean,
						pwtAmtForMasterApproval, highPrizeScheme, highPrizeAmt,
						highPrizeCriteria, connection);
				winningBeanList.add(drawScheduleBean);
				if ("UN_AUTH".equalsIgnoreCase(drawScheduleBean.getStatus())) {
					mainPwtBean.setStatus("UN_AUTH");
					return mainPwtBean;
				}

				if ("SUCCESS".equalsIgnoreCase(drawScheduleBean.getStatus())) {
					boHelper = new BOPwtProcessHelper();
					String raffleTktType = boHelper
							.getRaffleTktTypeFromgameNbr(gameNo, connection);
					if ("REFERENCE".equalsIgnoreCase(raffleTktType)) {
						PWTDrawBean pwtPomoBean = null;
						pwtPomoBean = boHelper.verifyRaffleTkt(mainPwtBean
								.getTicketNo(), gameNo, userInfoBean,
								refMerchantId, pwtAmtForMasterApproval,
								connection);
						if (pwtPomoBean != null) {
							winningBeanList.add(pwtPomoBean);
						}
					}
				}
				mainPwtBean.setWinningBeanList(winningBeanList);
			}

			// here verify the total ticket amount
			mainPwtBean = getTotalPWTTkeAmt(mainPwtBean, connection,pwtAmtForMasterApproval, userInfoBean);
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			ex.printStackTrace();
		} finally {
			DBConnect.closeCon(connection);
		}
		return mainPwtBean;
	}
	
	public MainPWTDrawBean  getTotalPWTTkeAmt(MainPWTDrawBean  mainPwtBean,Connection connection,String pwtAmtForMasterApproval,UserInfoBean userInfoBean) throws LMSException{
		
		String highPrizeAmt = (String) ServletActionContext
		.getServletContext().getAttribute("HIGH_PRIZE_AMT");
		double totalticketAmt=0.0;	
		List<PWTDrawBean> winningBeanList=mainPwtBean.getWinningBeanList();
		
		for(int i=0;i<winningBeanList.size();i++){			
			PWTDrawBean pwtBean=winningBeanList.get(i);
			totalticketAmt=totalticketAmt+pwtBean.getTotalAmount();			
		}
		/*for(int i=0;i<winningBeanList.size();i++){			
			PWTDrawBean pwtBean=winningBeanList.get(i);
			if(pwtBean.getDrawWinList()!=null)
				for(int j=0;j<pwtBean.getDrawWinList().size();j++){		
					DrawIdBean drawBean=pwtBean.getDrawWinList().get(j);
					List<PanelIdBean> panelWinList=drawBean.getPanelWinList();
					if(panelWinList!=null && drawBean.getStatus().equals("NORMAL_PAY"))
			totalticketAmt=totalticketAmt+Double.parseDouble(drawBean.getWinningAmt());			
		}
		}*/
				
		
		CommonFunctionsHelper commonFunction = new CommonFunctionsHelper();
		OrgPwtLimitBean orgPwtLimit=null;
		
			try {
				orgPwtLimit = commonFunction.fetchPwtLimitsOfOrgnization(
						userInfoBean.getUserOrgId(), connection);
				if (orgPwtLimit == null) { // send mail to
					// backoffice
					throw new LMSException("PWT Limits Are Not defined Properly!!");

				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		
		
		if(totalticketAmt > 0){
			if (totalticketAmt <= orgPwtLimit
					.getVerificationLimit()) { // test
				// organization
				// verification
				// limit
				logger
						.debug("inside verification limit == ");
				/*boolean isHighPrizeFlag = highPrizeCriteria
						.equals("amt")
						&& pwtAmt > Double
								.parseDouble(highPrizeAmt);*/
				
				boolean isHighPrizeFlag = totalticketAmt > Double
						.parseDouble(highPrizeAmt);
				
				
				// check for HIGH Prize Or is Approval
				// Required
				if(totalticketAmt < orgPwtLimit.getMinClaimPerTicket()||totalticketAmt>orgPwtLimit.getMaxClaimPerTicket() ){
					mainPwtBean.setStatus("OUT_PAY_LIMIT");			
				}
				else if (totalticketAmt > orgPwtLimit.getApprovalLimit()
						|| isHighPrizeFlag) {
					mainPwtBean.setHighPrize(true);
					mainPwtBean.setStatus("HIGH_PRIZE");			

				} else if (totalticketAmt <= orgPwtLimit
						.getPayLimit()) {// if PWT amount is in org payment limit
					mainPwtBean.setStatus("NORMAL_PAY");
					mainPwtBean.setWinTkt(true);
				} else {
					mainPwtBean.setStatus("OUT_PAY_LIMIT");					
				}
			} else { // Out of Range of Retailer Verification Limit.				
				mainPwtBean.setStatus("OUT_VERIFY_LIMIT");
			}
		}
		mainPwtBean.setTotlticketAmount(totalticketAmt);
		return mainPwtBean;
	}
	
	

	
	private void verifyRafflePwtBean(PWTDrawBean winBean, String pwtAmtForMasterApproval, Connection connection){
		try{
			boolean isMasAppReq = false;
			// if scheme is panel wise winning
			String highPrizeAmt = (String) ServletActionContext.getServletContext()
					.getAttribute("HIGH_PRIZE_AMT");
			logger.debug("HIGH_PRIZE_AMT" + highPrizeAmt);
			
		
			double totlraffleAmout = 0.0;
			if (winBean.getRaffleDrawIdBeanList() != null) {
				for (Object raffleDrawidBeanObj : winBean.getRaffleDrawIdBeanList()) {
					RaffleDrawIdBean raffleDrawidBean = (RaffleDrawIdBean) raffleDrawidBeanObj;
	
					int drawId = raffleDrawidBean.getDrawId();
					int rafflegameNo = raffleDrawidBean.getRaffleGameno();
					String raffleTicketNumber = raffleDrawidBean
							.getRaffleTicketNumberInDB();
					String pwtAmount = raffleDrawidBean.getWinningAmt();
					String prizeStatus = raffleDrawidBean.getStatus();
	
					if (prizeStatus.equalsIgnoreCase("UNCLM_PWT")
							|| prizeStatus.equalsIgnoreCase("UNCLM_CANCELLED")) {
						totlraffleAmout = totlraffleAmout
								+ Double.parseDouble(pwtAmount);
						isMasAppReq = Double.parseDouble(pwtAmount) > Double
								.parseDouble(pwtAmtForMasterApproval);
						if (isMasAppReq) {
							raffleDrawidBean.setAppReq(isMasAppReq);
							raffleDrawidBean.setPwtStatus("MAS_APP_REQ");
							System.out.println("MAS_APP_REQ***********");
						}
						raffleDrawidBean.setStatus("NORMAL_PAY");
	
						if (Double.parseDouble(pwtAmount) > Double.parseDouble(highPrizeAmt)) {
							raffleDrawidBean.setHighPrize(true);
							logger.debug("isHighPrize" + raffleDrawidBean.isHighPrize());
						} else {
							raffleDrawidBean.setWinTkt(true);
						}
					} else if (prizeStatus.equalsIgnoreCase("CLAIMED")) {
						raffleDrawidBean.setStatus("CLAIMED");
						PreparedStatement pstmt = connection
								.prepareStatement("select status from st_dg_pwt_inv_? where ticket_nbr=? and draw_id=? and panel_id=?");
						pstmt.setInt(1, rafflegameNo);
						pstmt.setString(2, raffleTicketNumber);
						pstmt.setInt(3, drawId);
						pstmt.setInt(4, 0);
						logger.debug(pstmt);
						ResultSet resultSet = pstmt.executeQuery();
						if (resultSet.next()) {
							prizeStatus = resultSet.getString("status");
							if (prizeStatus.equalsIgnoreCase("MISSING")) {
								raffleDrawidBean.setStatus("MISSING");
							} else if (prizeStatus
									.equalsIgnoreCase("CLAIM_PLR_RET_UNCLM")) {
								raffleDrawidBean.setStatus("CLAIMED");
							} else if (prizeStatus
									.equalsIgnoreCase("CLAIM_PLR_RET_CLM")) {
								raffleDrawidBean.setStatus("CLAIMED");
							} else if (prizeStatus
									.equalsIgnoreCase("CLAIM_PLR")) {
								raffleDrawidBean.setStatus("CLAIMED");
							} else if (prizeStatus
									.equalsIgnoreCase("CLAIM_RET_TEMP")) {
								raffleDrawidBean.setStatus("CLAIMED");
							} else if (prizeStatus
									.equalsIgnoreCase("CLAIM_PLR_RET_TEMP")) {
								raffleDrawidBean.setStatus("CLAIMED");
							} else if (prizeStatus
									.equalsIgnoreCase("CLAIM_AGT_TEMP")) {
								raffleDrawidBean.setStatus("CLAIMED");
							} else if (prizeStatus
									.equalsIgnoreCase("CLAIM_RET_AGT_TEMP")) {
								raffleDrawidBean.setStatus("CLAIMED");
							} else if (prizeStatus
									.equalsIgnoreCase("CLAIM_AGT")) {
								raffleDrawidBean.setStatus("CLAIMED");
							} else if (prizeStatus
									.equalsIgnoreCase("CLAIM_RET")) {
								raffleDrawidBean.setStatus("CLAIMED");
							} else if (prizeStatus
									.equalsIgnoreCase("CLAIM_PLR_TEMP")) {
								raffleDrawidBean.setStatus("CLAIMED");
							} else if (prizeStatus
									.equalsIgnoreCase("CLAIM_PLR_RET")) {
								raffleDrawidBean.setStatus("CLAIMED");
							} else if (prizeStatus
									.equalsIgnoreCase("REQUESTED")) {
								raffleDrawidBean.setStatus("REQUESTED");
							} else if (prizeStatus.equalsIgnoreCase("PND_MAS")) {
								raffleDrawidBean.setStatus("PND_MAS");
							} else if (prizeStatus.equalsIgnoreCase("PND_PAY")) {
								raffleDrawidBean.setStatus("PND_PAY");
							} else if (prizeStatus
									.equalsIgnoreCase("CLAIM_AGT_AUTO")) {
								raffleDrawidBean.setStatus("CLAIMED");
							} else if (prizeStatus
									.equalsIgnoreCase("CLAIM_PLR_BO")) {
								raffleDrawidBean.setStatus("CLAIMED");
							} else if (prizeStatus
									.equalsIgnoreCase("CLAIM_PLR_AGT_UNCLM_DIR")) {
								raffleDrawidBean.setStatus("CLAIMED");
							} else if (prizeStatus
									.equalsIgnoreCase("CLAIM_PLR_AGT_CLM_DIR")) {
								raffleDrawidBean.setStatus("CLAIMED");
							} else if (prizeStatus
									.equalsIgnoreCase("CLAIM_PLR_AGT_TEMP")) {
								raffleDrawidBean.setStatus("CLAIMED");
							} else if (prizeStatus
									.equalsIgnoreCase("CLAIM_RET_CLM")) {
								raffleDrawidBean.setStatus("CLAIMED");
							} else if (prizeStatus
									.equalsIgnoreCase("CLAIM_RET_CLM_AUTO")) {
								raffleDrawidBean.setStatus("CLAIMED");
							} else if (prizeStatus
									.equalsIgnoreCase("CLAIM_RET_UNCLM")) {
								raffleDrawidBean.setStatus("CLAIMED");
							} else if (prizeStatus
									.equalsIgnoreCase("CANCELLED_PERMANENT")) {
								raffleDrawidBean.setStatus("CANCELLED_PERMANENT");
							} else if (prizeStatus
									.equalsIgnoreCase("CLAIM_PLR_RET_CLM_DIR")) {
								raffleDrawidBean.setStatus("CLAIMED");
							} else if (prizeStatus
									.equalsIgnoreCase("CLAIM_PLR_RET_UNCLM_DIR")) {
								raffleDrawidBean.setStatus("CLAIMED");
							} else if (prizeStatus
									.equalsIgnoreCase("CLAIM_AGT_CLM_AUTO")) {
								raffleDrawidBean.setStatus("CLAIMED");
							} else {
								raffleDrawidBean.setStatus("UNDIFINED");
							}
						}
	
					} else if (prizeStatus.equalsIgnoreCase("NON WIN")) {
						raffleDrawidBean.setStatus("NON WIN");
						
					}
	
				}
			}
	
			//winBean.setGameDispName(Util.getGameDisplayName(winBean.getGameNo()));
			//winBean.setGameId(Util.getGameId(winBean.getGameNo()));
			//winBean.setTotalAmount(totPwtAmt);
			//winBean.setStatus("SUCCESS");
			winBean.setTotalAmount(totlraffleAmout);
			winBean.setStatus("SUCCESS");
		}catch(SQLException e){
			 e.printStackTrace();	
		}
	}

	
	public boolean getRgStatus(UserInfoBean userInfoBean,double totalTktWinAmt){
		boolean isFraud = ResponsibleGaming.respGaming(userInfoBean,
				"AGENT_DG_PWT", "" + totalTktWinAmt);
		return isFraud;
	}
	
	
}

/*
 * public static void main(String[] args) { try { Connection connection = new
 * DBConnect().getConnection(); connection.setAutoCommit(false);
 * AgtPWTProcessHelper helper = new AgtPWTProcessHelper(); Map dgVirnPwtMap =
 * helper.fetchDGPwtDetailsOfRetOrg(171, "RETAILER", "CLAIM_BAL", connection);
 * logger.debug("dgVirnPwtMap = "+dgVirnPwtMap); if(dgVirnPwtMap!=null &&
 * !dgVirnPwtMap.isEmpty()) { logger.debug("CLAIMABLE for RETAILER"); String
 * dgReceiptId = helper.updateClmableBalOfRetOrg(542, 171, 541, 170,
 * dgVirnPwtMap, connection); logger.debug("dgReceiptId = "+dgReceiptId); }
 * connection.commit(); }catch (Exception e) { e.printStackTrace(); } }
 */
