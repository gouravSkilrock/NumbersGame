package com.skilrock.lms.embedded.loginMgmt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.ajax.AjaxRequestHelper;
import com.skilrock.lms.beans.LoginBean;
import com.skilrock.lms.beans.ServicesBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.ConfigurationVariables;
import com.skilrock.lms.common.Utility;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.CommonMethods;
import com.skilrock.lms.common.utility.LatLongFromCellId;
import com.skilrock.lms.coreEngine.drawGames.playMgmt.DrawGameOfflineHelper;
import com.skilrock.lms.coreEngine.drawGames.playMgmt.DrawGameRPOSHelper;
import com.skilrock.lms.coreEngine.loginMgmt.common.UserAuthenticationHelper;
import com.skilrock.lms.coreEngine.messageMgmt.MessageUtility;
import com.skilrock.lms.dge.beans.BetDetailsBean;
import com.skilrock.lms.dge.beans.GameMasterLMSBean;
import com.skilrock.lms.embedded.drawGames.common.EmbeddedErrors;
import com.skilrock.lms.web.drawGames.common.Util;

public class AuthenticationHelper {
	private static Log logger = LogFactory.getLog(AuthenticationHelper.class);
public LoginBean authentication(String invCode,String invModel, String terminalId,String deviceType,String profile,double version,String simType,double CAPP ,double CCONF,double CDLL,String uname,String password,
		String loginAttempts,String refMerchantId,long LSTktNo,String actionName, Map frzTimeMap,Map<Integer, Map<Integer, String>> drawIdTableMap,Map<Integer, List<List>>  drawTimeMap, int msgId, int CID, int LAC, StringBuilder finalData,String sessionId) throws LMSException{
	Connection con =null;
	LoginBean loginBean =new LoginBean();
	PreparedStatement retPstmt=null;
	PreparedStatement retSimPstmt =null;
	ResultSet retRs=null;
	ResultSet retSimRs=null;
	try{
		
		con=DBConnect.getConnection();
		UserAuthenticationHelper loginAuth = new UserAuthenticationHelper();
		//check terminal Id for lms Wrapper
		if(Utility.getPropertyValue("IS_WRAPPER_ENABLED").equalsIgnoreCase("YES")){
			
			if(!loginAuth.checkTerminalId(terminalId,con)){
				logger.info("terminal Id not exist");
				loginBean.setErrorCode(EmbeddedErrors.ERROR_200); 
				return loginBean ;
			}
		}
	loginBean = loginAuth.loginAuthentication(uname, password,"TERMINAL", loginAttempts,con,sessionId , true);
	String returntype = loginBean.getStatus();
	logger.info("The user login is " + returntype);
	UserInfoBean userInfo = loginBean.getUserInfo();
	logger.info("USER_NAME-----> " + uname);
	//System.out.println("PASSWORD-----> " + password);
	String firstLogin = "";	
	if (returntype.equals("success") || returntype.equals("FirstTime")) {
		
		//String refMerchantId = (String) sc.getAttribute("REF_MERCHANT_ID");
		// int autoCancelHoldDays=Integer.parseInt((String) sc.getAttribute("AUTO_CANCEL_CLOSER_DAYS"));
		long lastPrintedTicket=0;
		int gameId1 = 0;
		if(LSTktNo !=0){
			lastPrintedTicket = LSTktNo/Util.getDivValueForLastSoldTkt(String.valueOf(LSTktNo).length());
			gameId1 = Util.getGameIdFromGameNumber(Util.getGamenoFromTktnumber(String.valueOf(LSTktNo)));
		}

		
		DrawGameRPOSHelper drawGameRPOSHelper = new DrawGameRPOSHelper();

		String ticketNo = String.valueOf(LSTktNo);
		con.setAutoCommit(false);
		int tktNo = 0;
		if(ticketNo.length() > 1)
		{
			tktNo = Util.getUserIdFromTicket(ticketNo);
			if(userInfo.getUserId() == tktNo)
				drawGameRPOSHelper.checkLastPrintedTicketStatusAndUpdate(userInfo,lastPrintedTicket,"TERMINAL",refMerchantId,Integer.parseInt(Utility.getPropertyValue("AUTO_CANCEL_CLOSER_DAYS")),actionName, gameId1,con);
		}
		
		
		//get all details from st_lms_ret_offline_master
		String dbTerminalId="";
		String downloadAvailable="NO";
		double expectedVersion=0.0;
		String expVersion=null;
		boolean isOfflineUser = false;
		String offLineStatus="INACTIVE";
		int binding_length=0;
		boolean isLoginBinding = Utility.getPropertyValue("LOGIN_BINDING").equalsIgnoreCase("YES");
		boolean isSimBinding =  Utility.getPropertyValue("sim_binding").equalsIgnoreCase("YES");
		
		if(isLoginBinding){
			retPstmt=con.prepareStatement(" select iom.serial_number,iom.is_download_available,iom.expected_version,iom.is_offline,iom.offline_status, imm.check_binding_length from st_lms_ret_offline_master iom inner join st_lms_inv_model_master imm on iom.device_type = imm.model_name where iom.user_id = ? and iom.device_type = ?");
			retPstmt.setInt(1, userInfo.getUserId());
			retPstmt.setString(2, deviceType);
			if(isSimBinding){
				String invCol =null;
				int invModelId=0;
				retSimPstmt = con.prepareStatement("select inv_column_name,model_id from st_lms_inv_model_master where model_name=?" ); 
				retSimPstmt.setString(1,invModel);
				retSimRs=retSimPstmt.executeQuery();
				if(retSimRs.next()){
					invCol =retSimRs.getString("inv_column_name");
					invModelId =retSimRs.getInt("model_id");
				}else{
					loginAuth = null;
					logger.info("You cannot login using this MODEL ");
					loginBean.setErrorCode(EmbeddedErrors.ERROR_204);
					return loginBean ;	
					
				}
				DBConnect.closeConnection(retSimPstmt, retSimRs);
				retSimPstmt = con.prepareStatement("select inv_code invCode from st_lms_ret_offline_master iom inner join st_lms_inv_mapping invMap  on ( iom."+invCol+"=invMap. serial_no )  where iom.user_id = ? and inv_model_id=? ");
				retSimPstmt.setInt(1, userInfo.getUserId());
				retSimPstmt.setInt(2,invModelId);
				retSimRs=retSimPstmt.executeQuery();
			}
		}else{
			retPstmt=con.prepareStatement("select iom.serial_number,iom.is_download_available,iom.expected_version,iom.is_offline,iom.offline_status from st_lms_ret_offline_master iom where iom.user_id = ?");
			retPstmt.setInt(1, userInfo.getUserId());
		}
		retRs=retPstmt.executeQuery();
		if(retRs.next()){
			dbTerminalId=retRs.getString("serial_number");					
			downloadAvailable=retRs.getString("is_download_available");
			expectedVersion=retRs.getDouble("expected_version");
			expVersion=retRs.getString("expected_version");
			isOfflineUser="YES".equals(retRs.getString("is_offline"));
			if(isOfflineUser)
				offLineStatus=retRs.getString("offline_status");
			if(isLoginBinding){
				binding_length = Integer.parseInt(retRs.getString("check_binding_length"));				
			}
			if(isSimBinding){
				if(retSimRs.next()){
					boolean isValidSim = retSimRs.getString("invCode").equalsIgnoreCase(invCode)?true:false ;  
					
					if(!isValidSim){
						loginAuth = null;
						logger.info("Invalid IMSI/Inventory Code Provided");
						loginBean.setErrorCode(EmbeddedErrors.ERROR_204);
						return loginBean ;	
					}
					
				}else{
					loginAuth = null;
					logger.info("You cannot login using this SIM ");
					loginBean.setErrorCode(EmbeddedErrors.ERROR_204);
					return loginBean ;	
					
				}
				DBConnect.closeConnection(retSimPstmt, retSimRs);
				
			}
			
			
		}else {
			loginAuth = null;
			logger.info("You cannot login using terminal");
			loginBean.setErrorCode(EmbeddedErrors.ERROR_203);
			return loginBean ;		
		}
		
		if(isLoginBinding){
			boolean isValidTerminal =loginAuth.validateTerminalId(dbTerminalId,terminalId,deviceType, binding_length);
			if (!isValidTerminal) {
				loginBean.setErrorCode(EmbeddedErrors.ERROR_201);
				return loginBean ;
			}
		}

		/*if ( !isValidTerminal) {
			
			/*if(loginAuth.checkTerminalId(terminalId) || !"YES".equalsIgnoreCase((String) ServletActionContext
				.getServletContext().getAttribute("IS_WRAPPER_ENABLED"))){*/
			//response.getOutputStream().write(("ErrorMsg:" + EmbeddedErrors.LOGIN_INVALID_TERMINAL_ID).getBytes());
			/*loginBean.setErrorCode(EmbeddedErrors.ERROR_201);
			return loginBean ;*/
				/*	}else{
				response
				.getOutputStream()
				.write(
						("ErrorMsg:" + (String) ServletActionContext.getServletContext()
								.getAttribute("WRAPPER_IP")+"|" + "ErrorCode:04|")
								.getBytes());
				
				System.out.println(""+"ErrorMsg:" + (String) ServletActionContext.getServletContext()
								.getAttribute("WRAPPER_IP")+"|" + "ErrorCode:04|");
				
				return;
			}*/
		//}
		/*
		 * if(version >= 4.0 &&
		 * DrawGameOfflineHelper.checkOfflineUser(userInfo.getUserId())){
		 * response.getOutputStream().write("ErrorMsg:Offline Game is Not
		 * Available.|".getBytes()); session.invalidate(); return; }
		 */
		if (userInfo.getUserType().equals("RETAILER")) {
			boolean isDraw = ServicesBean.isDG();
			boolean isScratch = ServicesBean.isSE();
			boolean isCs = ServicesBean.isCS();
			
			boolean isIW = ServicesBean.isIW();
			boolean isOLA = ServicesBean.isOLA();
			boolean isSLE = ServicesBean.isSLE();

			// order of services: first char for home, second for scratch,
			// third for draw
			String servSb = null; // for Home
			// service by
			// default 1

			if (isDraw && isScratch) {
				servSb = "111";
			} else if (isDraw && !isScratch) {
				servSb = "101";
			} else if (!isDraw && isScratch) {
				servSb = "110";
			} else {
				servSb = "100";
			}

			if (isCs) {
				servSb = servSb + "1";
			} else {
				servSb = servSb + "0";
			}

			if (isIW) {
				servSb = servSb + "1";
			} else {
				servSb = servSb + "0";
			}

			if (isOLA) {
				servSb = servSb + "1";
			} else {
				servSb = servSb + "0";
			}
			
			if (isSLE) {
				if(Utility.getPropertyValue("COUNTRY_DEPLOYED").equalsIgnoreCase("GHANA")) {
					if(version >= 10.26)
						servSb = servSb + "1";
				}
				else 
					servSb = servSb + "1";
			} else {
				if(Utility.getPropertyValue("COUNTRY_DEPLOYED").equalsIgnoreCase("GHANA")) {
					if(version >= 10.26)
						servSb = servSb + "0";
				} else
					servSb = servSb + "0";
			}
			
			if (returntype.equals("FirstTime")) {
				logger.info("Inside FirstTime Login");
				firstLogin = "SFT|";
			}
	

			AjaxRequestHelper ajxHelper = new AjaxRequestHelper();
			ajxHelper.getAvlblCreditAmt(userInfo,con);
			
			NumberFormat nFormat = NumberFormat.getInstance();
			nFormat.setMinimumFractionDigits(2);

			String bal = nFormat.format(userInfo.getAvailableCreditLimit());
			String claimbal = nFormat.format(userInfo.getClaimableBal());
			String unClaimbal = nFormat.format(userInfo.getUnclaimableBal());

			bal = bal.replace(",", "");
			claimbal = claimbal.replace(",", "");
			unClaimbal = unClaimbal.replace(",", "");

			String balance = nFormat.format((userInfo.getAvailableCreditLimit() - userInfo.getClaimableBal())).replace(",", "");

			logger.info("-------ENTERING INTO DEVICE VERSION CHECKING PART----------");

		
			//String deviceVersion = null;
		//	String isMandatory = null;
	
		/*	try {*/
				//	con.setAutoCommit(false);
				// -- SAVE CURRENT VERSION IN DATABASE
			//	Statement stmt = con.createStatement();
				if (profile == null) {
					profile = "NA";
				}
				UserAuthenticationHelper.updateDownloadDetails(userInfo.getUserId(), version,expectedVersion,downloadAvailable,con);
				if("KENYA".equalsIgnoreCase(Utility.getPropertyValue("COUNTRY_DEPLOYED"))){
					String insertVersionQuery = "update st_lms_ret_offline_master set current_version = ?, profile = ?,CAPP = ?,CDLL = ?,CCONF = ?,last_connected_through=?,last_HBT_time=?,device_type=?  where user_id = ?";
					PreparedStatement pstmt = con.prepareStatement(insertVersionQuery);
					pstmt.setString(1, version + "");
					pstmt.setString(2, profile);
					pstmt.setDouble(3, CAPP);
					pstmt.setDouble(4, CDLL);
					pstmt.setDouble(5, CCONF);
					pstmt.setString(6, simType);
					pstmt.setTimestamp(7, new Timestamp(new Date().getTime()));
					pstmt.setString(8,deviceType);
					pstmt.setInt(9, userInfo.getUserId());
					pstmt.executeUpdate();
					
					pstmt = con.prepareStatement("insert into st_lms_ret_wise_sim_history(sim_id, ret_organization_id) select id,? from st_lms_con_device_master where sim_name=?");
					pstmt.setInt(1, userInfo.getUserOrgId());
					pstmt.setString(2, simType);
					pstmt.executeUpdate();
				} else {
					String updateVersionQuery = "update st_lms_ret_offline_master set current_version = ?, profile = ?,last_connected_through=?,last_HBT_time=?,device_type=?,login_status=?,last_login_time=?  where user_id = ?";
					PreparedStatement pstmt = con.prepareStatement(updateVersionQuery);
					pstmt.setString(1, version + "");
					pstmt.setString(2, profile);
					pstmt.setString(3, simType);
					pstmt.setTimestamp(4, Util.getCurrentTimeStamp());
					pstmt.setString(5,deviceType);
					pstmt.setString(6,"LOGIN");
					pstmt.setTimestamp(7, Util.getCurrentTimeStamp());
					pstmt.setInt(8, userInfo.getUserId());
					logger.info("updateVersionQuery"+pstmt);
					int update=pstmt.executeUpdate();
					logger.info("updated Version"+update);
					pstmt = con.prepareStatement("insert into st_lms_ret_wise_sim_history(datetime,sim_id, ret_organization_id) select ?,id,? from st_lms_con_device_master where sim_name=?");
					pstmt.setTimestamp(1, Util.getCurrentTimeStamp());						
					pstmt.setInt(2, userInfo.getUserOrgId());
					pstmt.setString(3, simType);
					logger.info("insert ret sim wise history query"+pstmt);
					update=pstmt.executeUpdate();
					logger.info("insert sim"+update);
				}
				con.commit();
			/*} catch (SQLException ex) {
				logger.error("sql exception ", ex);
				
			}*/
			
			//DBConnect.closeCon(con);	

				/*String deviceDetails = "select device_version, is_mandatory, file_size from st_lms_htpos_download_details where id=(select max(id) from st_lms_htpos_download_details where device_type='"
						+ deviceType + "')";

				ResultSet rs = stmt.executeQuery(deviceDetails);

				while (rs.next()) {
					deviceVersion = rs.getString("device_version");
					isMandatory = rs.getString("is_mandatory");
					fileSize = rs.getString("file_size");
				}
				
			
			System.out.println("------DEVICE VERSION:" + deviceVersion
					+ "----IS MANDATORY:" + isMandatory + "-------");*/

			
			String orgTypeOnTicket =Utility.getPropertyValue("ORG_TYPE_ON_TICKET");
		
			if (orgTypeOnTicket != null) {
				if (orgTypeOnTicket.equalsIgnoreCase("AGENT")) {
					orgTypeOnTicket = userInfo.getParentOrgName().toUpperCase();
				} else if (orgTypeOnTicket.equalsIgnoreCase("RETAILER")) {
					orgTypeOnTicket = userInfo.getOrgName().toUpperCase();
				}
			}

			/*String agtOrgN = userInfo.getParentOrgName().toUpperCase();
			String retOrgN=  userInfo.getOrgName().toUpperCase(); */
			String retOrgN = null;
			String agtOrgN = userInfo.getParentOrgCode();
			if("GHANA".equalsIgnoreCase(Utility.getPropertyValue("COUNTRY_DEPLOYED")) || "BENIN".equalsIgnoreCase(Utility.getPropertyValue("COUNTRY_DEPLOYED")))
				retOrgN =  userInfo.getUserOrgCode();
			else
				retOrgN=  userInfo.getOrgName().toUpperCase();
		
			String currentTime = Util.getCurrentTimeString();

			//HashMap actionServiceMap = loginBean.getActionServiceMap();
			
			String embeddedActionList = null;
			//isOfflineUser = DrawGameOfflineHelper.checkOfflineUser(userInfo.getUserId(),con);
			
			if(version < 8.5){
				ArrayList<String> userActionList = loginBean
				.getUserActionList();
				if (isOfflineUser) {
					embeddedActionList = EmbeddedPrivMapping.getPriviledge(userActionList, "OFFLINE",con);
				} else {
					embeddedActionList = EmbeddedPrivMapping.getPriviledge(userActionList, "ONLINE",con);
				}
			}
			else{
				if(Utility.getPropertyValue("COUNTRY_DEPLOYED").equalsIgnoreCase("GHANA") && version < 10.26) {
					embeddedActionList = "21100214002130021340213302131021320223001130011120111541114011130114003100032000|";
				} else {
					embeddedActionList = EmbeddedPrivMapping.getPriviledgeNew(userInfo.getUserId(),con);	
				}
			}
			/*
			 * System.out.println(" embeddedActionList is--------------" +
			 * embeddedActionList);
			 */
			finalData.append(firstLogin + "success" + "," + bal + ","+ claimbal + "," + unClaimbal + "|"+ embeddedActionList);
			boolean isDownloadAvailable ="YES".equalsIgnoreCase(downloadAvailable) 
											&&  version !=expectedVersion;
				
				//UserAuthenticationHelper.chkDownloadAvailable(userInfo.getUserId(),con);
			if (version >= 6.1 && version < 8.5) {
				if("NA".equalsIgnoreCase(profile)){
					profile = "INGENICO";
				}
				finalData.append(UserAuthenticationHelper.terminalInfo(
						deviceType, profile, isCs, isDownloadAvailable, version, userInfo.getUserId(),con));
			} else if(version >= 8.5){
				if("NA".equalsIgnoreCase(profile)){
					profile = "INGENICO";
				}
				finalData.append(UserAuthenticationHelper.newTerminalInfo(
						deviceType, profile, isCs, isDownloadAvailable, version,expVersion, userInfo.getUserId(),con));
			}else {
			/*	finalData.append("version:" + deviceVersion
						+ "|is mandatory:" + isMandatory + "|fSize:" + fileSize + "|");*/
				finalData.append(UserAuthenticationHelper.terminalInfoForLessVersion(deviceType, profile, isDownloadAvailable, version, 
																			userInfo.getUserId(),con));
			}
			
			String sample =Utility.getPropertyValue("SAMPLE").equalsIgnoreCase("YES") ? "Y" : "N";
			String isLastTicketCancel =Utility.getPropertyValue("CANCEL_TYPE").equalsIgnoreCase("LAST_SOLD_TICKET") ? "Y" : "N";
			finalData.append("OrgN:" + orgTypeOnTicket + "|AgtOrgN:"+agtOrgN+"|RetOrgN:"+retOrgN+"|SAMPLE:" + sample
					+ "|CompanyN:" + Utility.getPropertyValue("ORG_NAME_JSP") + "|CurrencyS:"
					+ Utility.getPropertyValue("CURRENCY_SYMBOL") + "|CurrentT:" + currentTime + "|DF:"
					+ Utility.getPropertyValue("date_format").toLowerCase() + "|Services:" + servSb.toString() + "|isLTktCan:"+isLastTicketCancel+"|rTO:"+ Utility.getPropertyValue("RESPONSE_TIME_OUT") +"|"+"isLTktCanSLE:"+ Utility.getPropertyValue("SLE_LAST_TICKET_CANCEL").substring(0, 1) +"|" );
			if(isCs){
				finalData.append("cs_provider:"+ Utility.getPropertyValue("CS_PROVIDER") + "|");
			}
	
			/*session.setAttribute("PRIV_MAP", actionServiceMap);
			session.setAttribute("ACTION_LIST", userActionList);
			System.out.println(" user infor after setting in session is "
					+ userInfo);
			
			

			loggedInUser(uname, session);
*/
			if (isDraw) {

				/*String saleStartTime = (String) sc
						.getAttribute("SALE_START_TIME");
				String saleEndTime = (String) sc
						.getAttribute("SALE_END_TIME");*/

				finalData.append("isOffline:"+ (isOfflineUser ? "Y|balance:"+ balance+ "|SALE_ST:"+ Utility.getPropertyValue("SALE_START_TIME")+ "|SALE_ET:"+ Utility.getPropertyValue("SALE_END_TIME")+ "|status:"
									+ offLineStatus /*DrawGameOfflineHelper.fetchOfflineUserStatus(userInfo.getUserOrgId(),con)*/+ "|userId:" + userInfo.getUserId() + "|": "N|"));
				String gameInfo ="";
				if(version <  8.5){
				
				 gameInfo = "GameInfo:"+ DrawGameRPOSHelper.embdDgData(isOfflineUser,drawIdTableMap,
												userInfo.getUserId(), userInfo.getUserOrgId(), version,con) + "|";
					
			
				if (isOfflineUser) {
					String offStatus = DrawGameOfflineHelper.updateLoginStatus(userInfo.getUserId(),con);
					if (offStatus.equals("LOGIN")
							|| offStatus.equals("LOGOUT")) {
						if (offStatus.equals("LOGOUT")) {
							finalData.append(gameInfo);
						}
						logger.info("**Inside the offline*****");
					} else {
						/*response
								.getOutputStream()
								.write(
										("Errormsg:" + EmbeddedErrors.LOGIN_ALREADY_LOGGED_IN_PLEASE_LOGOUT_AND_UPLOAD_FILE_PROPERLY)
												.getBytes());*/
						loginBean.setErrorCode(EmbeddedErrors.ERROR_202);
						return loginBean ;
						
						
					}

				} else {
					System.out.println("**Inside the online**");
					StringBuilder noOfDraw = new StringBuilder("");
					Map<Integer, GameMasterLMSBean> map = Util.getGameMap();

					StringBuilder drawTimeStr = new StringBuilder(
							"|DrawDT:");
					StringBuilder frzTimeStr = new StringBuilder(
							"|DrawFTG:");

					/*TreeMap frzTimeMap = (TreeMap) sc.getContext(
							"/DrawGameWeb").getAttribute("FREEZETIME_DATA");*/
					
				

					if (version < 5.1) {
						/*Map<Integer, Map<Integer, String>> drawIdTableMap = (TreeMap<Integer, Map<Integer, String>>) sc
								.getAttribute("drawIdTableMap");*/
						/*		TreeMap<Integer, List<List>> drawTimeMap = (TreeMap<Integer, List<List>>) sc
								.getAttribute("GAME_DATA");*/
						Iterator iter = drawIdTableMap.entrySet()
								.iterator();
						while (iter.hasNext()) {
							Map.Entry pair = (Map.Entry) iter.next();
							Map<Integer, String> noofDrawMap = (Map<Integer, String>) pair
									.getValue();

							Integer gameId = (Integer) pair.getKey();
							GameMasterLMSBean bean = map.get(gameId);
							noOfDraw.append(pair.getKey() + ":");
							noOfDraw.append(noofDrawMap.size() + ":");

							//noOfDraw.append(Util.convertCollToStr(
							//		new TreeMap(bean.getPriceMap())
							//				.values()).replace(", ", ":"));
							Map<String, BetDetailsBean> bdbMap = bean.getPriceMap();
							Iterator<String> iterator = bdbMap.keySet().iterator();
							while(iterator.hasNext())
							{
								noOfDraw.append(bdbMap.get(iterator.next()).getUnitPrice()).append(":");
							}
							noOfDraw.deleteCharAt(noOfDraw.length() - 1);

							noOfDraw.append(":"
									+ bean.getTicketExpiryPeriod() + ",");

							frzTimeStr.append(pair.getKey());
							frzTimeStr.append(",");
							frzTimeStr.append(Long
									.parseLong((String) frzTimeMap.get(pair
											.getKey())));
							frzTimeStr.append(":");

							drawTimeStr.append(pair.getKey());
							drawTimeStr.append(",");
							List drawTimeList = drawTimeMap.get(
									pair.getKey()).get(0);
							for (int i = 0; i < drawTimeList.size(); i++) {
								Long drawTime = (Long) drawTimeList.get(i);
								Timestamp time = new Timestamp(drawTime);
								drawTimeStr.append(time.toString().split(
										"\\.")[0]);
								drawTimeStr.append(",");
							}
							drawTimeStr
									.deleteCharAt(drawTimeStr.length() - 1);
							drawTimeStr.append("#");
						}
						frzTimeStr.deleteCharAt(frzTimeStr.length() - 1);
						drawTimeStr.deleteCharAt(drawTimeStr.length() - 1);
						if (noOfDraw.length() > 0) {
							noOfDraw.deleteCharAt(noOfDraw.length() - 1);
						}
						String onlineData = "GameData:" + noOfDraw
								+ drawTimeStr + frzTimeStr + "|";

						finalData.append(onlineData);
					} else if (version >= 5.1) {
						finalData.append(gameInfo);
					}

				}
				
			}	
			// (new DrawGameRPOS()).newData();
			}
			if (isScratch) {
				
				if(Utility.getPropertyValue("SCRATCH_PWT_PRINT").equalsIgnoreCase("YES"))
					finalData.append("ScratchPWTPrt:Y|");
				else
					finalData.append("ScratchPWTPrt:N|");
				
				if(Utility.getPropertyValue("SCRATCH_PWT_WIN_PRINT").equalsIgnoreCase("YES"))
					finalData.append("ScratchWinRec:Y|");
				else
					finalData.append("ScratchWinRec:N|");
				
				
				
				
				
				// append Scratch specific data to finalData if any
			}
			
			boolean permCombiDisp =Utility.getPropertyValue("PERM_COMB_DISP").equalsIgnoreCase("YES");// "YES".equalsIgnoreCase((String)sc.getAttribute("PERM_COMB_DISP"));
			
			if(permCombiDisp){
				finalData.append("isCombP:Y|");
			} else {
				finalData.append("isCombP:N|");
			}
			boolean isMpesa = Utility.getPropertyValue("IS_MPESA_ENABLE").equalsIgnoreCase("YES");//"YES".equalsIgnoreCase((String)sc.getAttribute("IS_MPESA_ENABLE"));
			if(isMpesa){
				finalData.append("MPESA:Y|");
			}else{
				finalData.append("MPESA:N|");
			}
			
			if(version>=8.55){
				finalData.append("sTime:"+Utility.getPropertyValue("REPORTING_OFF_START_TIME")+"|eTime:"+Utility.getPropertyValue("REPORTING_OFF_END_TIME")+"|isPno:"+Utility.getPropertyValue("IS_PLAYER_MOBILE_REQ")+"|"); 
			}
			if(isSLE){
				finalData.append("isPnoSLE:").append(Utility.getPropertyValue("SLE_MOBILENO_REQUIRED_ON_SALE")).append("|");
			}
			finalData.append("hbInterval:").append(Utility.getPropertyValue("hbInterval")).append("|");
			finalData.append("uIDFill:").append(Utility.getPropertyValue("uIDFill")).append("|");
			finalData.append("InpType:").append(Utility.getPropertyValue("InpType")).append("|");
			finalData.append("retAuth:").append(Utility.getPropertyValue("retAuth")).append("|");
			finalData.append("userId:").append(userInfo.getUserId()).append("|");
			if(Utility.getPropertyValue("Supervisor_PWD")!=null && Utility.getPropertyValue("Supervisor_PWD").trim().length()>1){
				
				finalData.append("supervisorPwd:").append(Utility.getPropertyValue("Supervisor_PWD")).append("|");
			}

			/*
			if(Utility.getPropertyValue("FlashMsg")!=null && Utility.getPropertyValue("FlashMsg").trim().length()>1){
				finalData.append("flsMsg:").append(Utility.getPropertyValue("FlashMsg").replace("|","")).append("|");
			*/
			//String flashMessages = MessageUtility.fetchUserWiseFlashMessagesEmbedded(msgId, userInfo.getUserId(), userInfo.getUserType(), con);
			String flashMessages = MessageUtility.fetchUserWiseFlashMessagesEmbedded(msgId, userInfo.getUserId(), con);
			finalData.append("flsMsg:").append(flashMessages).append("|");

			finalData.append(Utility.getPropertyValue("RAFFLE_GAME_DATA")).append("|");

			if(version>=9.85) {
				if(CommonMethods.checkPriviledgeActiveForUser(userInfo.getUserId(), "Setting_Message", "TERMINAL", "HOME", con)) {
					String deleteMessageStatus = Utility.getPropertyValue("MESSAGE_INBOX_DELETE_STATUS");
					if(deleteMessageStatus == null || "NO".equals(deleteMessageStatus)) {
						deleteMessageStatus = "N";
						String messageInfo = MessageUtility.getNewMessageStatusEmbedded(msgId, userInfo.getUserId(), userInfo.getUserType(), con);
						finalData.append("msgInfo:").append(messageInfo).append(",").append(deleteMessageStatus);
					} else {
						deleteMessageStatus = "Y";
						finalData.append("msgInfo:0,N,N,Y");
					}
				}
			}

			if("GHANA".equalsIgnoreCase(Utility.getPropertyValue("COUNTRY_DEPLOYED"))) {
				finalData.append("|minBAM:").append(Utility.getPropertyValue("MIN_BET_AMT_MULTIPLE"));
				finalData.append("|isMacAllowed:").append(Utility.getPropertyValue("IS_MACHINE_ALLOWED"));
			}

			if((Utility.getPropertyValue("COUNTRY_DEPLOYED").equalsIgnoreCase("GHANA") && version >= 10.26) || "NIGERIA".equalsIgnoreCase(Utility.getPropertyValue("COUNTRY_DEPLOYED")) || "ZIMBABWE".equalsIgnoreCase(Utility.getPropertyValue("COUNTRY_DEPLOYED")) || ("PHILIP".equalsIgnoreCase(Utility.getPropertyValue("COUNTRY_DEPLOYED"))))
				finalData.append("|sessId:").append(sessionId).append("|").append("|merCode:").append(ConfigurationVariables.LMS_MERCHANT_CODE).append("|");

			String betMulValue = Utility.getPropertyValue("MIN_BET_AMT_MUL_REQUIRED");
			if(betMulValue != null)
				finalData.append("betAmtMulDC:"+betMulValue+"|");
			//UserAuthenticationHelper.updateLoginStatus(userInfo.getUserId(), "LOGIN",con);
			UserAuthenticationHelper.updateUserSession(userInfo.getUserName() , sessionId , con);
			
			
			con.commit();
			logger.info("*********Embedded Login Data*******"+ finalData.toString());
			loginBean.setErrorCode(EmbeddedErrors.ERROR_100);
			
			if("YES".equalsIgnoreCase(Utility.getPropertyValue("REAL_TIME_LOCATION_UPDATE"))){
				//Update latitude and longitude in st_lms_ret_offline_master...
				LatLongFromCellId latLongThread = new LatLongFromCellId(userInfo.getUserId(), CID, LAC);
				latLongThread.setDaemon(true);
				latLongThread.start();
			}
			
			return loginBean ;
		
		} else {
			loginAuth = null;
			logger.info("You cannot login using terminal");
			loginBean.setErrorCode(EmbeddedErrors.ERROR_203);
			return loginBean ;
		
		}

	}	
		
		
	}catch (Exception e) {
		logger.error(" Exception",e);
		throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
	}finally {
		
		try{
			DBConnect.closeCon(con);
			
		}catch (Exception e) {
			logger.error("Exception ",e);
		}
	}
	
	
	loginBean.setErrorCode(EmbeddedErrors.ERROR_000);
	return loginBean ;
	
	
	
}
	

}
