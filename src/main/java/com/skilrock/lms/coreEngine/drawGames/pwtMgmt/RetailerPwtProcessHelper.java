package com.skilrock.lms.coreEngine.drawGames.pwtMgmt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.beans.OrgPwtLimitBean;
import com.skilrock.lms.beans.PlayerBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.Utility;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.GenerateRecieptNo;
import com.skilrock.lms.coreEngine.drawGames.common.CommonFunctionsHelper;
import com.skilrock.lms.dge.beans.DrawIdBean;
import com.skilrock.lms.dge.beans.GameMasterLMSBean;
import com.skilrock.lms.dge.beans.PWTDrawBean;
import com.skilrock.lms.dge.beans.PanelIdBean;
import com.skilrock.lms.dge.beans.RaffleDrawIdBean;
import com.skilrock.lms.web.drawGames.common.Util;

public class RetailerPwtProcessHelper {
	static Log logger = LogFactory.getLog(RetailerPwtProcessHelper.class);
	private PreparedStatement pstmt;
	private ResultSet result;

	private String createQueryForApp(boolean inPayLimit) {
		StringBuilder insertAppQuery = new StringBuilder(
				"insert into  st_dg_approval_req_master (party_type , "
						+ "request_id , party_id , ticket_nbr , draw_id ,panel_id, game_id , pwt_amt , tax_amt , net_amt , "
						+ "req_status , requester_type , requested_by_user_id , requested_by_org_id , requested_to_org_id ,"
						+ " requested_to_type , request_date ,  remarks ");
		if (inPayLimit) {
			insertAppQuery
					.append(", approved_by_type, approved_by_user_id , approved_by_org_id , "
							+ "pay_req_for_org_type, pay_request_for_org_id, approval_date  ");
		}
		insertAppQuery
				.append(" ) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,? ");

		if (inPayLimit) {
			insertAppQuery.append(",?,?,?,?,?,? ");
		}
		insertAppQuery.append(")");
		return insertAppQuery.toString();
	}

	public Map doPlrRegistrationAndApproval(UserInfoBean userInfoBean,
			PWTDrawBean pwtDrawBean, String playerType, int playerId,
			PlayerBean plrBean, Connection connection, String highPrizeScheme)
			throws LMSException {
		Map pwtAppMap = new TreeMap();

		try {
			// register player
			if (plrBean != null && "PLAYER".equalsIgnoreCase(playerType.trim())) {
				playerId = new PlayerVerifyHelperForApp().registerPlayer(
						plrBean, connection);
			}

			logger.debug("Player id is " + playerId
					+ " for that approval required");

			// receipt generation done by yogesh
			String recIdForApp = GenerateRecieptNo
					.generateRequestIdDraw("DGREQUEST");
			if (recIdForApp == null || "".equals(recIdForApp)) {
				throw new LMSException("Request Id is not generated properly");
			}

			RetPWTProcessHelper retHelper = new RetPWTProcessHelper();
			if (highPrizeScheme.equalsIgnoreCase("DRAW_WISE")) {
				for (DrawIdBean drawIdBean : pwtDrawBean.getDrawWinList()) {
					if (drawIdBean.isAppReq() || drawIdBean.isHighLevel()) {
						pwtAppMap = insIntoPwtAppReqMasByRet(userInfoBean,
								pwtDrawBean.getGameId(), pwtDrawBean
										.getGameNo(),
								pwtDrawBean.getTicketNo(), null, playerType,
								playerId, recIdForApp, connection, drawIdBean
										.getWinningAmt(), drawIdBean
										.getDrawId(), drawIdBean.isHighLevel());
					}
				}

			} else if (highPrizeScheme.equalsIgnoreCase("PANEL_WISE")) {
				for (DrawIdBean drawIdBean : pwtDrawBean.getDrawWinList()) {
					if (drawIdBean.getPanelWinList() != null) {
						for (PanelIdBean panelIdBean : drawIdBean
								.getPanelWinList()) {
							if (panelIdBean.isAppReq()
									|| panelIdBean.isHighLevel()) {
								pwtAppMap = insIntoPwtAppReqMasByRet(
										userInfoBean, pwtDrawBean.getGameId(),
										pwtDrawBean.getGameNo(), pwtDrawBean
												.getTicketNo(), panelIdBean
												.getPanelId(), playerType,
										playerId, recIdForApp, connection,
										panelIdBean.getWinningAmt()+"", drawIdBean
												.getDrawId(), panelIdBean
												.isHighLevel());
							}
						}
					}
				}

			}

			return pwtAppMap;

		} catch (Exception e) {
			logger.error("Exception: " + e);
			e.printStackTrace();
			throw new LMSException(e);
		}
	}

	private Map<String, Object> getOrganizationForApp(int orgId, double pwtAmt,
			Connection connection) throws SQLException, LMSException {

		Map<String, Object> map = new TreeMap<String, Object>();
		String getParentOrgId = "select parent_id, (select organization_type from st_lms_organization_master bb "
				+ " where  organization_id = aa.parent_id) 'organization_type' from (select parent_id,organization_type "
				+ " from st_lms_organization_master where organization_id = ?)aa";
		pstmt = connection.prepareStatement(getParentOrgId);
		pstmt.setInt(1, orgId);
		result = pstmt.executeQuery();
		logger.debug("getOrganizationForApp = " + pstmt);
		if (result.next()) {
			int parentOrgId = result.getInt("parent_id");
			String organizationType = result.getString("organization_type");
			// int parentUserId = result.getInt("user_id");
			// added by yogesh because this function is written in common
			// functions of pwt
			CommonFunctionsHelper commonFunction = new CommonFunctionsHelper();
			OrgPwtLimitBean limitBean = commonFunction
					.fetchPwtLimitsOfOrgnization(parentOrgId, connection);

			if (!"BO".equalsIgnoreCase(organizationType)
					&& pwtAmt > limitBean.getApprovalLimit()) {
				logger
						.debug("getOrganizationForApp is called again because PWT Amt is "
								+ " greator then parent ('"
								+ organizationType
								+ "') approval limit ");
				map = getOrganizationForApp(parentOrgId, pwtAmt, connection);
			} else {
				map.put("parentOrgId", parentOrgId);
				map.put("organizationType", organizationType);
				// map.put("parentUserId", parentUserId);
				map.put("limitBean", limitBean);
			}
			return map;

		} else {
			logger.debug("No Organization found");
			throw new LMSException("No Organization found");
		}

	}

	/*
	 * commented by amit because this method is already available in Util class
	 * private int getGameIdFromgameNbr(int gameNbr, Connection con) throws
	 * LMSException {
	 * 
	 * try { PreparedStatement pstmt = con .prepareStatement("select game_id
	 * from st_dg_game_master where game_nbr=?"); pstmt.setInt(1, gameNbr);
	 * ResultSet rs = pstmt.executeQuery(); if (rs.next()) return
	 * rs.getInt("game_id"); else throw new LMSException(); } catch
	 * (SQLException e) { e.printStackTrace(); throw new LMSException(); } }
	 */

	private Map insIntoPwtAppReqMasByRet(UserInfoBean userInfoBean, int gameId,
			int gameNbr, String ticketNbr, Object panelId, String playerType,
			int playerId, String recIdForApp, Connection connection,
			String winningAmt, int drawId, boolean isHighLevel)
			throws LMSException, SQLException {

		Map map = new TreeMap();

		// get the current organization pay limit
		CommonFunctionsHelper commonFunction = new CommonFunctionsHelper();
		OrgPwtLimitBean limitBean = commonFunction.fetchPwtLimitsOfOrgnization(
				userInfoBean.getUserOrgId(), connection);
		boolean isAppReq = Double.parseDouble(winningAmt) > limitBean
				.getApprovalLimit();
		boolean inPayLimit = Double.parseDouble(winningAmt) <= limitBean
				.getPayLimit();

		int reqToOrgId = userInfoBean.getUserOrgId();
		String reqToOrgType = userInfoBean.getUserType();

		if (isAppReq || isHighLevel && !inPayLimit) {
			// get the organization details whose Approval is required
			Map<String, Object> orgForAppDetail = getOrganizationForApp(
					userInfoBean.getUserOrgId(),
					Double.parseDouble(winningAmt), connection);
			reqToOrgId = (Integer) orgForAppDetail.get("parentOrgId");
			reqToOrgType = (String) orgForAppDetail.get("organizationType");
		}
		boolean isAutoApp = !(isAppReq || isHighLevel && !inPayLimit);
		logger.debug("Approval requested to orgId = " + reqToOrgId
				+ "  and user type = " + reqToOrgType);

		String status = isAutoApp ? "PND_PAY" : "REQUESTED";

		// insert into Approval table
		String insertAppQuery = createQueryForApp(isAutoApp);
		logger.debug("insertAppQuery = " + insertAppQuery);
		pstmt = connection.prepareStatement(insertAppQuery);
		pstmt.setString(1, playerType.toUpperCase());
		pstmt.setString(2, recIdForApp);

		boolean isPlr = "player".equalsIgnoreCase(playerType.trim());
		if (isPlr) {
			pstmt.setInt(3, playerId);
		} else {
			pstmt.setObject(3, null);
		}

		pstmt.setString(4, ticketNbr);
		pstmt.setInt(5, drawId);
		pstmt.setObject(6, panelId);
		pstmt.setInt(7, gameId);
		pstmt.setDouble(8, Double.parseDouble(winningAmt));
		pstmt.setDouble(9, 0.0);
		pstmt.setDouble(10, Double.parseDouble(winningAmt));
		pstmt.setString(11, status);
		pstmt.setString(12, userInfoBean.getUserType());
		pstmt.setInt(13, userInfoBean.getUserId());
		pstmt.setInt(14, userInfoBean.getUserOrgId());
		pstmt.setInt(15, reqToOrgId);
		pstmt.setString(16, reqToOrgType);
		pstmt.setTimestamp(17, new java.sql.Timestamp(new java.util.Date()
				.getTime()));
		pstmt.setString(18, "request For PWT Approval");
		if (isAutoApp) {
			pstmt.setString(18, "Auto Approved. ");
			pstmt.setString(19, userInfoBean.getUserType());
			pstmt.setInt(20, userInfoBean.getUserId());
			pstmt.setInt(21, userInfoBean.getUserOrgId());
			pstmt.setString(22, userInfoBean.getUserType());
			pstmt.setInt(23, userInfoBean.getUserOrgId());
			pstmt.setTimestamp(24, new java.sql.Timestamp(new java.util.Date()
					.getTime()));
		}
		logger.debug("insertAppQuery pppppp = " + pstmt);
		pstmt.executeUpdate();

		logger.debug("insertion into pwt temp table = " + pstmt);
		ResultSet rs = pstmt.getGeneratedKeys();
		Integer reqId = null;
		if (rs.next()) {
			reqId = rs.getInt(1);

			// insert ticket details into st_dg_pwt_inv_? table
			String insIntoDGPwtInvQuery = "insert into st_dg_pwt_inv_?(ticket_nbr, draw_id,panel_id, pwt_amt, status,is_direct_plr) values (?, ?, ?, ?, ?,?)";
			PreparedStatement insIntoDGPwtInvPstmt = connection
					.prepareStatement(insIntoDGPwtInvQuery);
			insIntoDGPwtInvPstmt.setInt(1, gameId);
			insIntoDGPwtInvPstmt.setString(2, ticketNbr);
			insIntoDGPwtInvPstmt.setInt(3, drawId);
			insIntoDGPwtInvPstmt.setObject(4, panelId);
			insIntoDGPwtInvPstmt.setDouble(5, Double.parseDouble(winningAmt));
			insIntoDGPwtInvPstmt.setString(6, status);
			insIntoDGPwtInvPstmt.setString(7, "Y");
			insIntoDGPwtInvPstmt.executeUpdate();

			map.put("reqId", reqId);
			map.put("isAutoApp", isAutoApp);
			map.put("reqToOrgType", reqToOrgType);
			return map;
		} else {
			throw new LMSException(
					"NO Data Inserted in st_dg_approval_req_master table");
		}

		// return reqId;

	}

	public String plrRegistrationAndApproval(String firstName, String lastName,
			String idNumber, String idType, Connection con,
			PWTDrawBean pwtDrawBean, UserInfoBean userInfoBean,
			String highPrizeScheme) throws LMSException {
		PlayerBean plrBean = null;

		// check if player is registered or not
		PlayerVerifyHelperForApp verifyHelper = new PlayerVerifyHelperForApp();
		int playerId = verifyHelper.verifyPlayer(firstName, lastName, idNumber,
				idType);

		if (playerId == 0) {// means player is not registered its need to
			// register
			plrBean = new PlayerBean();
			Statement stmt;
			String countryName = "";
			String stateName = "";
			String city = "";
			try {
				stmt = con.createStatement();
				ResultSet rs = stmt
						.executeQuery("select  a.name country,b.name state,c.city from st_lms_country_master a,st_lms_state_master b ,st_lms_organization_master c where c.organization_id="
								+ userInfoBean.getUserOrgId()
								+ " and c.country_code=a.country_code and c.state_code=b.state_code");
				if (rs.next()) {
					countryName = rs.getString("country");
					stateName = rs.getString("state");
					city = rs.getString("city");
				} else {
					throw new LMSException();
				}

			} catch (SQLException e1) {
				logger.error("Exception: " + e1);
				e1.printStackTrace();
				throw new LMSException();
			}
			plrBean.setFirstName(firstName);
			plrBean.setLastName(lastName);
			plrBean.setIdType(idType);
			plrBean.setIdNumber(idNumber);
			plrBean.setEmailId("NA");
			plrBean.setPhone("NA");
			plrBean.setPlrAddr1("NA");
			plrBean.setPlrAddr2("NA");
			plrBean.setPlrState(stateName);
			plrBean.setPlrCity(city);
			plrBean.setPlrCountry(countryName);
			plrBean.setPlrPin(0);
			plrBean.setBankName(null);
			plrBean.setBankBranch(null);
			plrBean.setLocationCity(null);
			plrBean.setBankAccNbr(null);
			logger.debug("Inside player registration 11111 & plrBean is "
					+ plrBean);
		} else {
			logger
					.debug("Player is already registered with plr id "
							+ playerId);
		}

		if (userInfoBean == null) {
			throw new LMSException("userInfoBean = " + userInfoBean);
		}

		String playerType = "PLAYER";
		doPlrRegistrationAndApproval(userInfoBean, pwtDrawBean, playerType,
				playerId, plrBean, con, highPrizeScheme);
		/*
		 * if(plrBean==null) { plrBean =
		 * (PlayerBean)session.getAttribute("playerBean");
		 * session.setAttribute("playerBean", null); }
		 * 
		 * pwtAppMap.put("plrBean", plrBean);
		 * 
		 * session.setAttribute("plrPwtAppDetMap", pwtAppMap);
		 */

		return "success";
	}

	private void setPanelBeanStatus(String status, DrawIdBean drawIdWinningBean) {
		if (drawIdWinningBean.getPanelWinList() != null) {
			for (PanelIdBean panelIdBean : drawIdWinningBean.getPanelWinList()) {
				panelIdBean.setStatus(status);
			}
		}
	}

	public PWTDrawBean verifyDrawPwt(String ticketNbr, int gameId,
			Connection connection, UserInfoBean userInfoBean,
			String highPrizeCriteria, String highPrizeAmt,
			PWTDrawBean pwtDrawBean, String highPrizeScheme, boolean isPayment,String pwtTicketType,List<Long> transIdList)
			throws LMSException {

		try {
			// get the gameId from the database using gameNbr
			double totalPWTAmtDraw=0.0;
			pwtDrawBean.setGameId(gameId);
			// get the retailer PWT Limits
			CommonFunctionsHelper commonFunction = new CommonFunctionsHelper();
			OrgPwtLimitBean orgPwtLimit = commonFunction
					.fetchPwtLimitsOfOrgnization(userInfoBean.getUserOrgId(),
							connection);
			RetPWTProcessHelper retHelper = new RetPWTProcessHelper();
			verifyPwtAmount(ticketNbr, gameId, connection, userInfoBean,
					highPrizeCriteria, highPrizeAmt, pwtDrawBean);

			// String scheme="panelWise";
			GameMasterLMSBean gameMasterLMSBean =Util.getGameMasterLMSBean(gameId);
			double govtCommPwt = gameMasterLMSBean.getGovtCommPwt();
			if(pwtDrawBean.getTotalAmount()>=Double.parseDouble(Utility.getPropertyValue("PLAYER_WINNING_TAX_APPLICABLE_AMOUNT")))
				pwtDrawBean.setGovtTaxAmount(govtCommPwt*pwtDrawBean.getTotalAmount()*0.01);
			
			if (highPrizeScheme.equalsIgnoreCase("DRAW_WISE")) {
				logger.debug("-------------DRAW_WISE in ret pwt");
				if (pwtDrawBean.getDrawWinList() != null) {
					for (DrawIdBean drawIdWinningBean : pwtDrawBean
							.getDrawWinList()) {
						
						if(drawIdWinningBean.getRankId() == 4 && "TwelveByTwentyFour".equals(Util.getGameName(pwtDrawBean.getGameId()))) {
							isPayment = false;
						}

						String pwtAmount = drawIdWinningBean.getWinningAmt();
						String prizeStatus = drawIdWinningBean.getStatus();
						int drawId = drawIdWinningBean.getDrawId();

						if (prizeStatus.equalsIgnoreCase("NORMAL_PAY")) {
							if (orgPwtLimit == null) { // send mail to
								// backoffice
								throw new LMSException(
										"PWT Limits Are Not defined Properly!!");

							} else { // test PWT amount with limit
								// process

								logger.debug("pwt amount " + pwtAmount);
								double pwtAmt = Double.parseDouble(pwtAmount);

								if (pwtAmt <= orgPwtLimit
										.getVerificationLimit()) { // test
									// retailer
									// verification
									// limit
									logger
											.debug("inside verification limit == ");
									boolean isHighPrizeFlag = highPrizeCriteria
											.equals("amt")
											&& pwtAmt > Double
													.parseDouble(highPrizeAmt);
									// check for HIGH Prize Or is Approval
									// Required

									if (pwtAmt > orgPwtLimit.getApprovalLimit()
											|| isHighPrizeFlag) {
										drawIdWinningBean
												.setHighLevel(isHighPrizeFlag);
										drawIdWinningBean
												.setAppReq(pwtAmt > orgPwtLimit
														.getApprovalLimit());
										// drawIdWinningBean.setRetPayLimit(pwtAmt<=orgPwtLimit.getPayLimit());
										drawIdWinningBean
												.setStatus("HIGH_PRIZE");
										// drawIdBeanList.add(drawIdWinningBean);

									} else if (pwtAmt <= orgPwtLimit
											.getPayLimit()) {// if PWT amount
										// is in
										// retailer
										// payment limit
										// insert into receipt create
										boolean isAutoScrap = "YES"
												.equalsIgnoreCase(orgPwtLimit
														.getIsPwtAutoScrap())
												&& pwtAmt <= orgPwtLimit
														.getScrapLimit() ? true
												: false;
										if (isPayment) {
											//last false parameter for deducting govt comm on pwt
										long transactionId =	retHelper
													.retPwtPayment(
															userInfoBean
																	.getUserId(),
															userInfoBean
																	.getUserOrgId(),
															userInfoBean
																	.getParentOrgId(),
															 gameId,
															isAutoScrap,
															pwtAmt, drawId, 0,
															ticketNbr,
															connection, false,false); 
											transIdList.add(transactionId);
											// panel
											// id is
											// zero
											// in
											// case
											// of
											// draw_wise
											// and
											// Ticket_wise
										}
										drawIdWinningBean
												.setStatus("NORMAL_PAY");
										// drawIdBeanList.add(drawIdWinningBean);
										// return pwtBean;
									} else {
										drawIdWinningBean
												.setStatus("OUT_PAY_LIMIT");
										// drawIdBeanList.add(drawIdWinningBean);
									}
								} else { // Out of Range of Retailer
									// Verification Limit.
									logger
											.debug("Out of Range of Retailer Verification Limit.");
									drawIdWinningBean
											.setStatus("OUT_VERIFY_LIMIT");
									// drawIdBeanList.add(drawIdWinningBean);
								}

							}
						} else if (prizeStatus == "CLAIMED") {
							drawIdWinningBean.setStatus("CLAIMED");
						}

						String drawBeanStatus = drawIdWinningBean.getStatus();
						if (!("NORMAL_PAY".equalsIgnoreCase(drawBeanStatus) || "HIGH_PRIZE"
								.equalsIgnoreCase(drawBeanStatus))) {
							setPanelBeanStatus(drawBeanStatus,
									drawIdWinningBean);
						}
						logger.debug("222222222222222222222222draw :"
								+ drawIdWinningBean.getDrawDateTime()
								+ ": status=" + drawIdWinningBean.getStatus());
					}
				}
			} else if (highPrizeScheme.equalsIgnoreCase("PANEL_WISE")) {
				logger.debug("-------------PANEL_WISE in ret pwt");

				for (DrawIdBean drawIdWinningBean : pwtDrawBean
						.getDrawWinList()) {
					int drawId = drawIdWinningBean.getDrawId();
					if (drawIdWinningBean.getPanelWinList() != null) {
						PanelIdBean panelIdBean = null;
						for (Object panelIdBeanObj : drawIdWinningBean
								.getPanelWinList()) {

							panelIdBean = (PanelIdBean) panelIdBeanObj;
							if (panelIdBean.getStatus().equalsIgnoreCase(
									"NORMAL_PAY")) {
								Double pwtAmt = panelIdBean.getWinningAmt();
								boolean isAutoScrap = "YES"
										.equalsIgnoreCase(orgPwtLimit
												.getIsPwtAutoScrap())
										&& pwtAmt <= orgPwtLimit
												.getScrapLimit() ? true : false;
								if (isPayment) {
									//last false parameter for deducting govt comm on pwt
									long transactionId = retHelper.retPwtPayment(userInfoBean
											.getUserId(), userInfoBean
											.getUserOrgId(), userInfoBean
											.getParentOrgId(),  gameId,
											isAutoScrap, pwtAmt, drawId,
											panelIdBean.getPanelId(),
											ticketNbr, connection, false,false);
									transIdList.add(transactionId);
									
								}
								panelIdBean.setStatus("NORMAL_PAY");
							}

						}
					}
				}

			} else if (highPrizeScheme.equalsIgnoreCase("TICKET_WISE")) {
				logger.debug("-------------TICKET_WISE in ret pwt");
				double pwtAmt = 0.0;
				int drawId = 0;
				pwtAmt = pwtDrawBean.getTotalAmount();
				//totalPWTAmtDraw=pwtAmt;
				if (pwtAmt > 0 && !pwtTicketType.equalsIgnoreCase("RAFFLE")) {
					totalPWTAmtDraw=pwtAmt;
					if (orgPwtLimit == null) { // send mail to
						// back office
						throw new LMSException(
								"PWT Limits Are Not defined Properly!!");

					} else { // test PWT amount with limit process

						if (pwtAmt <= orgPwtLimit.getVerificationLimit()) { // test
							// retailer
							// verification
							// limit
							logger.debug("inside verification limit == ");
							boolean isHighPrizeFlag = highPrizeCriteria
									.equals("amt")
									&& pwtAmt > Double
											.parseDouble(highPrizeAmt);
							// check for HIGH Prize Or is Approval Required

							if (pwtAmt > orgPwtLimit.getApprovalLimit()
									|| isHighPrizeFlag) {
								/*
								 * drawIdWinningBean
								 * .setHighLevel(isHighPrizeFlag);
								 * drawIdWinningBean .setAppReq(pwtAmt >
								 * orgPwtLimit .getApprovalLimit());
								 */
								// drawIdWinningBean.setRetPayLimit(pwtAmt<=orgPwtLimit.getPayLimit());
								pwtDrawBean.setPwtStatus("HIGH_PRIZE");
								// drawIdBeanList.add(drawIdWinningBean);

							} else if (pwtAmt <= orgPwtLimit.getPayLimit()) { // if
								// PWT
								// amount
								// is
								// in
								// retailer
								// payment
								// limit
								// insert into receipt create
								boolean isAutoScrap = "YES"
										.equalsIgnoreCase(orgPwtLimit
												.getIsPwtAutoScrap())
										&& pwtAmt <= orgPwtLimit
												.getScrapLimit() ? true : false;
								for (DrawIdBean drawIdWinningBean : pwtDrawBean
										.getDrawWinList()) {
									drawId = drawIdWinningBean.getDrawId();
									PanelIdBean panelIdBean = null;
									if (drawIdWinningBean.getPanelWinList() != null) {
										for (Object panelIdBeanObj : drawIdWinningBean
												.getPanelWinList()) {

											panelIdBean = (PanelIdBean) panelIdBeanObj;
											if (panelIdBean.getStatus()
													.equalsIgnoreCase(
															"NORMAL_PAY")) {
												if (isPayment) {
													
													boolean isGovtTaxDeduct=false;
													if(pwtDrawBean.getGovtTaxAmount()>0)
														isGovtTaxDeduct=true;
													//last false parameter for deducting govt comm on pwt
													long transactionId = retHelper
															.retPwtPayment(
																	userInfoBean
																			.getUserId(),
																	userInfoBean
																			.getUserOrgId(),
																	userInfoBean
																			.getParentOrgId(),
																	
																	gameId,
																	isAutoScrap,panelIdBean.getWinningAmt(),
																	drawId,
																	panelIdBean
																			.getPanelId(),
																	ticketNbr,
																	connection,
																	false,isGovtTaxDeduct);
													transIdList.add(transactionId);
												}	
											}
										}
									}
								}

								pwtDrawBean.setPwtStatus("NORMAL_PAY");
								// drawIdBeanList.add(drawIdWinningBean);
								// return pwtBean;
							} else {
								pwtDrawBean.setPwtStatus("OUT_PAY_LIMIT");
								// drawIdBeanList.add(drawIdWinningBean);
							}
						} else { // Out of Range of Retailer
							// Verification Limit.
							logger
									.debug("Out of Range of Retailer Verification Limit.");
							pwtDrawBean.setPwtStatus("OUT_VERIFY_LIMIT");
							// drawIdBeanList.add(drawIdWinningBean);
						}

					}

				}
				logger.debug("@@@@@@@@@@@@@@@@@@@@@Ticket status is : "
						+ pwtDrawBean.getPwtStatus());

			}
           
			//here do the pyment process for the raffle games
			double totalPWTAmtRaffle=0.0;
		if(pwtDrawBean.getRaffleDrawIdBeanList()!=null){	
			for (Object raffleDrawidBeanObj : pwtDrawBean.getRaffleDrawIdBeanList()){	
			       RaffleDrawIdBean raffleDrawidBean= (RaffleDrawIdBean) raffleDrawidBeanObj;
			      if(raffleDrawidBean.isValid()){
			       int raffleNo = raffleDrawidBean.getRaffleGameno();
			       String raffleTicketNbrinDb=raffleDrawidBean.getRaffleTicketNumberInDB();
					double pwtAmt = 0.0;
					int drawId = 0;
					pwtAmt = Double.parseDouble(raffleDrawidBean.getWinningAmt());
					totalPWTAmtRaffle=totalPWTAmtRaffle+pwtAmt;
					if (pwtAmt > 0) {

						if (orgPwtLimit == null) { // send mail to
							// back office
							throw new LMSException(
									"PWT Limits Are Not defined Properly!!");

						} else { // test PWT amount with limit process

							if (pwtAmt <= orgPwtLimit.getVerificationLimit()) { // test
								// retailer
								// verification
								// limit
								logger.debug("inside verification limit == ");
								boolean isHighPrizeFlag = (highPrizeCriteria
										.equals("amt") && (pwtAmt > Double
										.parseDouble(highPrizeAmt)));
								// check for HIGH Prize Or is Approval Required

								if (pwtAmt > orgPwtLimit.getApprovalLimit()
										|| isHighPrizeFlag) {
									/*
									 * drawIdWinningBean
									 * .setHighLevel(isHighPrizeFlag);
									 * drawIdWinningBean .setAppReq(pwtAmt >
									 * orgPwtLimit .getApprovalLimit());
									 */
									// drawIdWinningBean.setRetPayLimit(pwtAmt<=orgPwtLimit.getPayLimit());
									raffleDrawidBean.setPwtStatus("HIGH_PRIZE");
									// drawIdBeanList.add(drawIdWinningBean);

								} else if (pwtAmt <= orgPwtLimit.getPayLimit()) { // if
									// PWT
									// amount
									// is
									// in
									// retailer
									// payment
									// limit
									// insert into receipt create
									boolean isAutoScrap = ("YES"
											.equalsIgnoreCase(orgPwtLimit
													.getIsPwtAutoScrap()) && pwtAmt <= orgPwtLimit
											.getScrapLimit()) ? true : false;
									
										drawId = raffleDrawidBean.getDrawId();									
									if (raffleDrawidBean.getStatus()
											.equalsIgnoreCase(
													"NORMAL_PAY")) {
										if (isPayment) {
											//last false parameter for deducting govt comm on pwt
											long transactionId= retHelper.retPwtPayment(userInfoBean.getUserId(),userInfoBean.getUserOrgId(),userInfoBean.getParentOrgId(),
													raffleNo,isAutoScrap,pwtAmt,drawId,0,raffleTicketNbrinDb,
															connection,false,false);
											 transIdList.add(transactionId);
										}
									}

									raffleDrawidBean.setPwtStatus("NORMAL_PAY");									
								} else {
									raffleDrawidBean.setPwtStatus("OUT_PAY_LIMIT");									
								}
							} else { // Out of Range of Retailer
								// Verification Limit.								
								raffleDrawidBean.setPwtStatus("OUT_VERIFY_LIMIT");
								// drawIdBeanList.add(drawIdWinningBean);
							}

						}
                         
					}else if(raffleDrawidBean.getStatus().equalsIgnoreCase("NON_WIN")){
						raffleDrawidBean.setPwtStatus("NON_WIN");
					}
			}	
					logger.debug("@@@@@@@@@@@@@@@@@@@@@Ticket status is : "
							+ pwtDrawBean.getPwtStatus());			       
			}
		}	
			
		double totalDARWandraffle = totalPWTAmtDraw + totalPWTAmtRaffle;	
		pwtDrawBean.setTotalAmount(totalDARWandraffle);
	//	pwtDrawBean.setTotalAmount(totalPWTAmtRaffle);
			/*//here see the limits after adding both for draw and raffle
			
			// if(pwtTicketType.equalsIgnoreCase("DRAW"))	{
				double totalPWTAmtDraw = pwtDrawBean.getTotalAmount();			
				double totalDARWandraffle = totalPWTAmtDraw + totalPWTAmtRaffle;				
			if(totalDARWandraffle > 0){	
				if (totalDARWandraffle <= orgPwtLimit.getVerificationLimit()) {					
					logger.debug("inside verification limit == ");
					boolean isHighPrizeFlag = (highPrizeCriteria
							.equals("amt") && (totalDARWandraffle > Double
							.parseDouble(highPrizeAmt)));
					// check for HIGH Prize Or is Approval Required

					if (totalDARWandraffle > orgPwtLimit.getApprovalLimit()
							|| isHighPrizeFlag) {						
						pwtDrawBean.setPwtStatus("HIGH_PRIZE");
					

					} else if (totalDARWandraffle <= orgPwtLimit.getPayLimit()) { 						
						boolean isAutoScrap = ("YES"
								.equalsIgnoreCase(orgPwtLimit
										.getIsPwtAutoScrap()) && pwtAmt <= orgPwtLimit
								.getScrapLimit()) ? true : false;						
							drawId = raffleDrawidBean.getDrawId();									
					
							if (raffleDrawidBean.getStatus()
								.equalsIgnoreCase(
										"NORMAL_PAY")) {
							if (isPayment) {
								retHelper.retPwtPayment(userInfoBean.getUserId(),userInfoBean.getUserOrgId(),userInfoBean.getParentOrgId(),
										raffleNo,raffleNo,isAutoScrap,pwtAmt,drawId,0,raffleTicketNbrinDb,
												connection);
							}
						}
                        
						pwtDrawBean.setPwtStatus("NORMAL_PAY");		
													
					} else {
						pwtDrawBean.setPwtStatus("OUT_PAY_LIMIT");									
					}
				} else { 							
					pwtDrawBean.setPwtStatus("OUT_VERIFY_LIMIT");
					
				}
			 }	
				
			// }
			pwtDrawBean.setTotalAmount(totalDARWandraffle);*/
			return pwtDrawBean;
		} catch (SQLException e) {
			logger.error("Exception: " + e);
			e.printStackTrace();
			throw new LMSException(e);
		}
		/*
		 * if (prizeStatus.equalsIgnoreCase("UNCLM_PWT") || prizeStatus
		 * .equalsIgnoreCase("UNCLM_CANCELLED")) {
		 * 
		 * if (orgPwtLimit == null) { // send mail to // backoffice throw new
		 * LMSException( "PWT Limits Are Not defined Properly!!"); } else { //
		 * test PWT amount with limit // process
		 * 
		 * logger.debug("pwt amount " + pwtAmount); double pwtAmt = Double
		 * .parseDouble(pwtAmount);
		 * 
		 * if (pwtAmt <= orgPwtLimit .getVerificationLimit()) { // test //
		 * retailer // verification // limit System.out .println("inside
		 * verification limit == "); boolean isHighPrizeFlag =
		 * (highPrizeCriteria .equals("amt") && (pwtAmt > Double
		 * .parseDouble(highPrizeAmt))); // check for HIGH Prize Or is Approval //
		 * Required
		 * 
		 * if (pwtAmt > orgPwtLimit .getApprovalLimit() || isHighPrizeFlag) {
		 * drawIdWinningBean .setHighLevel(isHighPrizeFlag); drawIdWinningBean
		 * .setAppReq(pwtAmt > orgPwtLimit .getApprovalLimit()); //
		 * drawIdWinningBean.setRetPayLimit(pwtAmt<=orgPwtLimit.getPayLimit());
		 * drawIdWinningBean .setStatus("HIGH_PRIZE"); //
		 * drawIdBeanList.add(drawIdWinningBean); } else if (pwtAmt <=
		 * orgPwtLimit .getPayLimit()) {// if PWT // amount is // in // retailer //
		 * payment // limit // insert into receipt create boolean isAutoScrap =
		 * ("YES" .equalsIgnoreCase(orgPwtLimit .getIsPwtAutoScrap()) && pwtAmt <=
		 * orgPwtLimit .getScrapLimit()) ? true : false; retHelper
		 * .retPwtPayment( userInfoBean .getUserId(), userInfoBean
		 * .getUserOrgId(), userInfoBean .getParentOrgId(), gameNbr, gameId,
		 * isAutoScrap, pwtAmt, drawId, null, ticketNbr, connection);
		 * drawIdWinningBean .setStatus("NORMAL_PAY"); //
		 * drawIdBeanList.add(drawIdWinningBean); // return pwtBean; } else {
		 * drawIdWinningBean .setStatus("OUT_PAY_LIMIT"); //
		 * drawIdBeanList.add(drawIdWinningBean); } } else { // Out of Range of
		 * Retailer // Verification Limit. System.out .println("Out of Range of
		 * Retailer Verification Limit."); drawIdWinningBean
		 * .setStatus("OUT_VERIFY_LIMIT"); //
		 * drawIdBeanList.add(drawIdWinningBean); } } }
		 * 
		 * else if (prizeStatus.equalsIgnoreCase("CLAIMED")) {
		 * drawIdWinningBean.setStatus("CLAIMED"); } else if
		 * (prizeStatus.equalsIgnoreCase("NON WIN")) {
		 * drawIdWinningBean.setStatus("NON WIN"); } } }
		 * 
		 * else if (highPrizeScheme.equalsIgnoreCase("PANEL_WISE")) { } } } }
		 * catch (SQLException e) { e.printStackTrace(); throw new
		 * LMSException(e); } // return drawPwtBeanList; return pwtDrawBean;
		 */
	}

	// added by amit on 03/08/10
	public void verifyPwtAmount(String ticketNbr, int gameId,
			Connection connection, UserInfoBean userInfoBean,
			String highPrizeCriteria, String highPrizeAmt,
			PWTDrawBean pwtDrawBean) throws LMSException {

		
		double totalTktPwtAmt = 0.0;
		double totlraffleAmout = 0.0;
		// get the retailer PWT Limits
		CommonFunctionsHelper commonFunction = null;
		OrgPwtLimitBean orgPwtLimit;
		try {
			
			pwtDrawBean.setGameId(gameId);
			commonFunction = new CommonFunctionsHelper();
			orgPwtLimit = commonFunction.fetchPwtLimitsOfOrgnization(
					userInfoBean.getUserOrgId(), connection);

			// if scheme is panel wise winning
			if (pwtDrawBean.getDrawWinList() != null) {
				for (DrawIdBean drawIdWinningBean : pwtDrawBean
						.getDrawWinList()) {
					int drawId = drawIdWinningBean.getDrawId();
					boolean isDrawOutVerify = false;
					boolean isDrawHighPrize = false;
					boolean isDrawOutPayLimit = false;
					boolean isDrawClaimed = false;
					boolean isDrawCancelledPermanet = false;
					boolean isDrawPndPay = false;
					boolean isDrawNormalPay = false;
					boolean isDrawPndMas = false;
					boolean isDrawRequested = false;
					boolean isClmPending = false;
					if (drawIdWinningBean.getWinningAmt() != null && !("".equalsIgnoreCase(drawIdWinningBean.getWinningAmt()))) {
						totalTktPwtAmt = totalTktPwtAmt
								+ Double.parseDouble(drawIdWinningBean
										.getWinningAmt());
					}

					if (drawIdWinningBean.getPanelWinList() != null) {
						PanelIdBean panelIdBean = null;
						for (Object panelIdBeanObj : drawIdWinningBean
								.getPanelWinList()) {

							panelIdBean = (PanelIdBean) panelIdBeanObj;
							String pwtAmount = panelIdBean.getWinningAmt() + "";
							String prizeStatus = panelIdBean.getStatus();
							logger.debug("Status For The Panel " + prizeStatus);
							if (prizeStatus.equalsIgnoreCase("UNCLM_PWT") || prizeStatus.equalsIgnoreCase("NORMAL_PAY") 
									|| prizeStatus
											.equalsIgnoreCase("UNCLM_CANCELLED")) {

								if (orgPwtLimit == null) { // send mail to
									// backoffice
									throw new LMSException(
											"PWT Limits Are Not defined Properly!!");

								} else { // test PWT amount with limit process

									logger.debug("pwt amount " + pwtAmount);
									double pwtAmt = Double
											.parseDouble(pwtAmount);

									if (pwtAmt <= orgPwtLimit
											.getVerificationLimit()) { // test
										// organization
										// verification
										// limit
										logger
												.debug("inside verification limit == ");
										boolean isHighPrizeFlag = highPrizeCriteria
												.equals("amt")
												&& pwtAmt > Double
														.parseDouble(highPrizeAmt);
										// check for HIGH Prize Or is Approval
										// Required
										if (pwtAmt < orgPwtLimit.getMinClaimPerTicket() || pwtAmt > orgPwtLimit.getMaxClaimPerTicket()) {
													panelIdBean.setStatus("PWT_LIMIT_EXCEED");
													isDrawOutVerify = true;

										} else if (pwtAmt > orgPwtLimit
												.getApprovalLimit()
												|| isHighPrizeFlag) {
											panelIdBean
													.setHighLevel(isHighPrizeFlag);
											panelIdBean
													.setAppReq(pwtAmt > orgPwtLimit
															.getApprovalLimit());

											panelIdBean.setStatus("HIGH_PRIZE");
											isDrawHighPrize = true;

										} else if (pwtAmt <= orgPwtLimit
												.getPayLimit()) {// if PWT
											// amount
											// is in org
											// payment limit
											panelIdBean.setStatus("NORMAL_PAY");
											isDrawNormalPay = true;

										} else {
											panelIdBean
													.setStatus("OUT_PAY_LIMIT");
											isDrawOutPayLimit = true;
										}
									} else { // Out of Range of Retailer
										// Verification Limit.
										logger
												.debug("Out of Range of Retailer Verification Limit.");
										panelIdBean
												.setStatus("OUT_VERIFY_LIMIT");
										isDrawOutVerify = true;

									}

								}

							} else if (prizeStatus.equalsIgnoreCase("CLAIMED")) {
								panelIdBean.setStatus("CLAIMED");
								isDrawClaimed = true;
								PreparedStatement pstmt;

								pstmt = connection
										.prepareStatement("select status from st_dg_pwt_inv_? where ticket_nbr=? and draw_id=? and panel_id=?");

								pstmt.setInt(1, gameId);
								pstmt.setString(2, ticketNbr);
								pstmt.setInt(3, drawId);
								pstmt.setInt(4, panelIdBean.getPanelId());
								logger.debug(pstmt);
								ResultSet resultSet = pstmt.executeQuery();
								if (resultSet.next()) {
									prizeStatus = resultSet.getString("status");
									if (prizeStatus.equalsIgnoreCase("MISSING")) {
										panelIdBean.setStatus("MISSING");
									} else if (prizeStatus
											.equalsIgnoreCase("CLAIM_PLR_RET_UNCLM")) {
										panelIdBean.setStatus("CLAIMED");
									} else if (prizeStatus
											.equalsIgnoreCase("CLAIM_PLR_RET_CLM")) {
										panelIdBean.setStatus("CLAIMED");
									} else if (prizeStatus
											.equalsIgnoreCase("CLAIM_PLR")) {
										panelIdBean.setStatus("CLAIMED");
									} else if (prizeStatus
											.equalsIgnoreCase("CLAIM_RET_TEMP")) {
										panelIdBean.setStatus("CLAIMED");
									} else if (prizeStatus
											.equalsIgnoreCase("CLAIM_PLR_RET_TEMP")) {
										panelIdBean.setStatus("CLAIMED");
									} else if (prizeStatus
											.equalsIgnoreCase("CLAIM_AGT_TEMP")) {
										panelIdBean.setStatus("CLAIMED");
									} else if (prizeStatus
											.equalsIgnoreCase("CLAIM_RET_AGT_TEMP")) {
										panelIdBean.setStatus("CLAIMED");
									} else if (prizeStatus
											.equalsIgnoreCase("CLAIM_AGT")) {
										panelIdBean.setStatus("CLAIMED");
									} else if (prizeStatus
											.equalsIgnoreCase("CLAIM_RET")) {
										panelIdBean.setStatus("CLAIMED");
									} else if (prizeStatus
											.equalsIgnoreCase("CLAIM_PLR_TEMP")) {
										panelIdBean.setStatus("CLAIMED");
									} else if (prizeStatus
											.equalsIgnoreCase("CLAIM_PLR_RET")) {
										panelIdBean.setStatus("CLAIMED");
									} else if (prizeStatus
											.equalsIgnoreCase("REQUESTED")) {
										panelIdBean.setStatus("REQUESTED");
									} else if (prizeStatus
											.equalsIgnoreCase("PND_MAS")) {
										panelIdBean.setStatus("PND_MAS");
									} else if (prizeStatus
											.equalsIgnoreCase("PND_PAY")) {
										panelIdBean.setStatus("PND_PAY");
									} else if (prizeStatus
											.equalsIgnoreCase("CLAIM_AGT_AUTO")) {
										panelIdBean.setStatus("CLAIMED");
									} else if (prizeStatus
											.equalsIgnoreCase("CLAIM_PLR_BO")) {
										panelIdBean.setStatus("CLAIMED");
									} else if (prizeStatus
											.equalsIgnoreCase("CLAIM_PLR_AGT_UNCLM_DIR")) {
										panelIdBean.setStatus("CLAIMED");
									} else if (prizeStatus
											.equalsIgnoreCase("CLAIM_PLR_AGT_CLM_DIR")) {
										panelIdBean.setStatus("CLAIMED");
									} else if (prizeStatus
											.equalsIgnoreCase("CLAIM_PLR_AGT_TEMP")) {
										panelIdBean.setStatus("CLAIMED");
									} else if (prizeStatus
											.equalsIgnoreCase("CLAIM_RET_CLM")) {
										panelIdBean.setStatus("CLAIMED");
									} else if (prizeStatus
											.equalsIgnoreCase("CLAIM_RET_CLM_AUTO")) {
										panelIdBean.setStatus("CLAIMED");
									} else if (prizeStatus
											.equalsIgnoreCase("CLAIM_RET_UNCLM")) {
										panelIdBean.setStatus("CLAIMED");
									} else if (prizeStatus
											.equalsIgnoreCase("CANCELLED_PERMANENT")) {
										panelIdBean
												.setStatus("CANCELLED_PERMANENT");
									} else if (prizeStatus
											.equalsIgnoreCase("CLAIM_PLR_RET_CLM_DIR")) {
										panelIdBean.setStatus("CLAIMED");
									} else if (prizeStatus
											.equalsIgnoreCase("CLAIM_PLR_RET_UNCLM_DIR")) {
										panelIdBean.setStatus("CLAIMED");
									} else if (prizeStatus
											.equalsIgnoreCase("CLAIM_AGT_CLM_AUTO")) {
										panelIdBean.setStatus("CLAIMED");
									} else {
										panelIdBean.setStatus("UNDIFINED");
									}
								}
							} else if (prizeStatus.equalsIgnoreCase("NON WIN")) {
								panelIdBean.setStatus("NON WIN");
							} else if (prizeStatus.equals("CLAIM_PENDING")) {
								panelIdBean.setStatus("CLAIM_PENDING");
								isClmPending = true;
							}
						}
						if (isClmPending) {
							drawIdWinningBean.setStatus("CLAIM_PENDING");
							setPanelBeanStatus(drawIdWinningBean.getStatus(),
									drawIdWinningBean);
						} else if (isDrawOutVerify) {
							drawIdWinningBean.setStatus("OUT_VERIFY_LIMIT");
							setPanelBeanStatus(drawIdWinningBean.getStatus(),
									drawIdWinningBean);
						} else if (isDrawOutPayLimit) {
							drawIdWinningBean.setStatus("OUT_PAY_LIMIT");
							setPanelBeanStatus(drawIdWinningBean.getStatus(),
									drawIdWinningBean);
						} else if (isDrawHighPrize) {
							drawIdWinningBean.setStatus("HIGH_PRIZE");
							setPanelBeanStatus(drawIdWinningBean.getStatus(),
									drawIdWinningBean);
						} else if (isDrawRequested) {
							drawIdWinningBean.setStatus("REQUESTED");
							setPanelBeanStatus(drawIdWinningBean.getStatus(),
									drawIdWinningBean);
						} else if (isDrawPndPay) {
							drawIdWinningBean.setStatus("PND_PAY");
							setPanelBeanStatus(drawIdWinningBean.getStatus(),
									drawIdWinningBean);
						} else if (isDrawPndMas) {
							drawIdWinningBean.setStatus("PND_MAS");
							setPanelBeanStatus(drawIdWinningBean.getStatus(),
									drawIdWinningBean);
						} else if (isDrawCancelledPermanet) {
							drawIdWinningBean.setStatus("CANCELLED_PERMANENT");
							setPanelBeanStatus(drawIdWinningBean.getStatus(),
									drawIdWinningBean);
						} else if (isDrawClaimed) {
							drawIdWinningBean.setStatus("CLAIMED");
						} else if (isDrawNormalPay) {
							if (drawIdWinningBean.getStatus().equals(
									"DRAW_EXPIRED")) {
								drawIdWinningBean.setStatus("DRAW_EXPIRED");
								setPanelBeanStatus(drawIdWinningBean
										.getStatus(), drawIdWinningBean);
							} else
								drawIdWinningBean.setStatus("NORMAL_PAY");
						} else {
							drawIdWinningBean.setStatus("NON WIN");
						}
						logger.debug("***Draw Date Time  :"
								+ drawIdWinningBean.getDrawDateTime()
								+ ": status=" + drawIdWinningBean.getStatus());
					} else if (drawIdWinningBean.getStatus().equals(
							"VERIFICATION_PENDING")) {
						drawIdWinningBean.setStatus("VERIFICATION_PENDING");
					} else if (drawIdWinningBean.getStatus().equals(
							"RES_AWAITED")) {
						drawIdWinningBean.setStatus("RES_AWAITED");
					}
				}
			}
			logger.debug("Draw ticket pwt amount is " + totalTktPwtAmt);
			if (pwtDrawBean.getRaffleDrawIdBeanList() != null) {
				for (Object raffleDrawidBeanObj : pwtDrawBean
						.getRaffleDrawIdBeanList()) {

					RaffleDrawIdBean raffleDrawidBean = (RaffleDrawIdBean) raffleDrawidBeanObj;

					int drawId = raffleDrawidBean.getDrawId();
					int rafflegameNo = raffleDrawidBean.getRaffleGameno();
					String raffleTicketNumber = raffleDrawidBean
							.getRaffleTicketNumberInDB();
					String pwtAmount = raffleDrawidBean.getWinningAmt();
					String prizeStatus = raffleDrawidBean.getStatus();
					// totlraffleAmout=totlraffleAmout+Double.parseDouble(pwtAmount);
					if (prizeStatus.equalsIgnoreCase("UNCLM_PWT")
							|| prizeStatus.equalsIgnoreCase("UNCLM_CANCELLED")) {
						totlraffleAmout = totlraffleAmout
								+ Double.parseDouble(pwtAmount);
						if (orgPwtLimit == null) { // send mail to
							// backoffice
							throw new LMSException(
									"PWT Limits Are Not defined Properly!!");

						} else { // test PWT amount with limit process

							logger.debug("pwt amount " + pwtAmount);
							double pwtAmt = Double.parseDouble(pwtAmount);

							if (pwtAmt <= orgPwtLimit.getVerificationLimit()) { // test
								// organization
								// verification
								// limit
								logger.debug("inside verification limit == ");
								boolean isHighPrizeFlag = (highPrizeCriteria
										.equals("amt") && (pwtAmt > Double
										.parseDouble(highPrizeAmt)));
								// check for HIGH Prize Or is Approval
								// Required

								if (pwtAmt > orgPwtLimit.getApprovalLimit()
										|| isHighPrizeFlag) {
									raffleDrawidBean
											.setHighLevel(isHighPrizeFlag);
									raffleDrawidBean
											.setAppReq(pwtAmt > orgPwtLimit
													.getApprovalLimit());

									raffleDrawidBean.setStatus("HIGH_PRIZE");

								} else if (pwtAmt <= orgPwtLimit.getPayLimit()) {// if
									// PWT
									// amount
									// is in org
									// payment limit

									raffleDrawidBean.setStatus("NORMAL_PAY");
								} else {
									raffleDrawidBean.setStatus("OUT_PAY_LIMIT");
								}
							} else { // Out of Range of Retailer
								// Verification Limit.
								logger
										.debug("Out of Range of Retailer Verification Limit.");
								raffleDrawidBean.setStatus("OUT_VERIFY_LIMIT");
							}

						}

					} else if (prizeStatus.equalsIgnoreCase("CLAIMED")) {
						raffleDrawidBean.setStatus("CLAIMED");
						PreparedStatement pstmt;
						pstmt = connection
								.prepareStatement("select status from st_dg_pwt_inv_? where ticket_nbr=? and draw_id=? ");

						pstmt.setInt(1, rafflegameNo);
						pstmt.setString(2, raffleTicketNumber);
						pstmt.setInt(3, drawId);
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
								raffleDrawidBean
										.setStatus("CANCELLED_PERMANENT");
							} else {
								raffleDrawidBean.setStatus("UNDIFINED");
							}
						} else if (prizeStatus.equalsIgnoreCase("NON WIN")) {
							raffleDrawidBean.setStatus("NON WIN");
						}

					} else if (prizeStatus.equalsIgnoreCase("NON_WIN")) {
						raffleDrawidBean.setStatus("NON_WIN");
					}

				}
			}
			pwtDrawBean.setTotalAmount(totalTktPwtAmt + totlraffleAmout);
			logger.debug("Total Ticket Winning :-"
					+ (totalTktPwtAmt + totlraffleAmout));

		} catch (SQLException e) {
			logger.error("Exception: " + e);
			e.printStackTrace();
		}
	}

}
