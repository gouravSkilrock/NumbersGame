package com.skilrock.lms.api.lmsWrapper;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.admin.common.SetResetUserPasswordHelper;
import com.skilrock.lms.api.common.LmsWrapperOrgRegShiftBeanComp;
import com.skilrock.lms.api.lmsWrapper.beans.AgentInfoBean;
import com.skilrock.lms.api.lmsWrapper.beans.LmsWrapperDrawIdBean;
import com.skilrock.lms.api.lmsWrapper.beans.LmsWrapperInventoryAssignDataBean;
import com.skilrock.lms.api.lmsWrapper.beans.LmsWrapperInventoryDetailBean;
import com.skilrock.lms.api.lmsWrapper.beans.LmsWrapperKenoPurchaseBean;
import com.skilrock.lms.api.lmsWrapper.beans.LmsWrapperMainPWTDrawBean;
import com.skilrock.lms.api.lmsWrapper.beans.LmsWrapperOrgRegShiftBean;
import com.skilrock.lms.api.lmsWrapper.beans.LmsWrapperOrgRegistrationBean;
import com.skilrock.lms.api.lmsWrapper.beans.LmsWrapperOrganizationRegShiftDataBean;
import com.skilrock.lms.api.lmsWrapper.beans.LmsWrapperPWTDrawBean;
import com.skilrock.lms.api.lmsWrapper.beans.LmsWrapperPanelIdBean;
import com.skilrock.lms.api.lmsWrapper.beans.LmsWrapperPwtApiBean;
import com.skilrock.lms.api.lmsWrapper.beans.LmsWrapperRetailerInfoBean;
import com.skilrock.lms.api.lmsWrapper.beans.LmsWrapperRetailerListBean;
import com.skilrock.lms.api.lmsWrapper.beans.LmsWrapperUserRegistrationBean;
import com.skilrock.lms.api.lmsWrapper.beans.LmsWrapperVerifyBeforeShiftBean;
import com.skilrock.lms.api.lmsWrapper.beans.LmsWrapperVerifyBeforeShiftReq;
import com.skilrock.lms.api.lmsWrapper.beans.LmsWrapperVerifyOrgBean;
import com.skilrock.lms.api.lmsWrapper.beans.OrgInvDetails;
import com.skilrock.lms.api.lmsWrapper.common.InventoryHelper;
import com.skilrock.lms.api.lmsWrapper.common.WrapperUtility;
import com.skilrock.lms.beans.AgentRegistrationBean;
import com.skilrock.lms.beans.AvailableServiceBean;
import com.skilrock.lms.beans.HistoryBean;
import com.skilrock.lms.beans.LoginBean;
import com.skilrock.lms.beans.PlayerBean;
import com.skilrock.lms.beans.RetailerRegistrationBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.Utility;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.CommonMethods;
import com.skilrock.lms.common.utility.LMSUtility;
import com.skilrock.lms.coreEngine.drawGames.pwtMgmt.BOPwtProcessHelper;
import com.skilrock.lms.coreEngine.inventoryMgmt.ConsNNonConsInvHelper;
import com.skilrock.lms.coreEngine.loginMgmt.common.UserAuthenticationHelper;
import com.skilrock.lms.coreEngine.userMgmt.common.CountryOrgHelper;
import com.skilrock.lms.coreEngine.userMgmt.common.OrgNUserRegHelper;
import com.skilrock.lms.coreEngine.userMgmt.common.OrganizationHelper;
import com.skilrock.lms.dge.beans.DrawIdBean;
import com.skilrock.lms.dge.beans.KenoPurchaseBean;
import com.skilrock.lms.dge.beans.MainPWTDrawBean;
import com.skilrock.lms.dge.beans.PWTDrawBean;
import com.skilrock.lms.dge.beans.PanelIdBean;
import com.skilrock.lms.web.drawGames.common.Util;

public class LmsWrapperServiceApiHelper {
	static Log logger = LogFactory.getLog(LmsWrapperServiceApiHelper.class);
	
	public static void main(String[] args) throws IOException{
		Connection con=DBConnect.getConnection();
		LmsWrapperPWTDrawBean pwtbean =new LmsWrapperPWTDrawBean(); 
		LmsWrapperServiceApiHelper help=new LmsWrapperServiceApiHelper();
		
		String[] serNoAll=new String[]{"06277PT10102221","06277PT10102230","06277PT10102236","4354356464656"};
		String[] serNo=new String[4];
		serNo[0]="06277PT10102221";
		serNo[1]="06277PT10102230";
		serNo[2]="06277PT10102236";
		boolean isafg;
		if(pwtbean.isValid() && pwtbean.isHighPrize()){
			System.out.println("sfsdf");
		}
		List<String> dulSerNoList=help.checkDuplicateTerminal(serNo,"1");
		dulSerNoList.removeAll(Arrays.asList(serNo));
	      System.out.println(dulSerNoList);
	}
	
    public static void onStartWrapperData(){
    	Connection con=DBConnect.getConnection();
    	
    	String wrapperAuthenticateData="select system_ip,system_username,system_password from st_lms_wrapper_authentication_master";
    	try{
    		Map<String,String> AuthenticateDataMap=new HashMap<String, String>();
    	Statement stmt=con.createStatement();
    	ResultSet rs=stmt.executeQuery(wrapperAuthenticateData);
    	while(rs.next()){
    		AuthenticateDataMap.put(rs.getString("system_ip"),rs.getString("system_username")+":"+rs.getString("system_password"));
    	}
    	WrapperUtility.wrapperAuthenticateDataMap=AuthenticateDataMap;
    	System.out.println("AuthenticateDataMap::"+AuthenticateDataMap);
    	}catch (Exception e) {
			// TODO: handle exception
		}
    	finally {
    		try{
    			con.close();
    		}
    		catch (Exception e) {
				// TODO: handle exception
			}
    	}
    }
	
	
	public List<AgentInfoBean> getAgentList(String listType) {
		
		List<AgentInfoBean> agentInfoList=new ArrayList<AgentInfoBean>();
		Connection con=DBConnect.getConnection();
		String appender = null;
		try {
			if("ALL".equals(listType)) {
				appender = " and om.organization_status !='TERMINATE'";
			} else {
				appender = "";
			}
			String getAgentQry="select name,user_id,om.organization_id,(available_credit-claimable_bal) balance from st_lms_organization_master om,st_lms_user_master um where om.organization_type='AGENT' and um.organization_id=om.organization_id"+appender+" group by om.organization_id";
			System.out.println("Get agent List Qry::"+getAgentQry);
			Statement stmt=con.createStatement();
			ResultSet rs=stmt.executeQuery(getAgentQry);
			while(rs.next()){
				AgentInfoBean agentInfoBean=new AgentInfoBean();
				agentInfoBean.setAgentName(rs.getString("name"));
				agentInfoBean.setAgentOrgId(rs.getInt("organization_id"));
				agentInfoBean.setBalance(rs.getString("balance"));
				agentInfoBean.setUserId(rs.getInt("user_id"));
				agentInfoList.add(agentInfoBean);
			}
		} catch (SQLException e) {
			
			e.printStackTrace();
		}finally{
			try{
				con.close();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		return agentInfoList;
	}
	
	public String getWrapperUserData(){
		String userData="";
         Connection con =DBConnect.getConnection();
 		String fetchuserData="select organization_id,user_id,organization_type from st_lms_user_master where user_type='wrapper'";
          try {
			Statement stmt=con.createStatement();
			ResultSet rs=stmt.executeQuery(fetchuserData);
			if(rs.next()){
				userData=rs.getInt("user_id")+"-"+rs.getInt("organization_id")+"-"+rs.getString("organization_type");
			}
		} catch (SQLException e) {
			
			e.printStackTrace();
		}finally {
			try{
				con.close();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
          
		return userData;
	}
	public UserInfoBean getUserData(){
		String userName="";
		String password="";
		Connection con =DBConnect.getConnection();
 		String fetchuserData="select user_name,password from st_lms_user_master where user_type='wrapper'";
          try {
			Statement stmt=con.createStatement();
			ResultSet rs=stmt.executeQuery(fetchuserData);
			if(rs.next()){
				userName=rs.getString("user_name");
				password=(String)LMSUtility.sc.getAttribute("WRAPPER_USER_PASSWORD");
			}
		} catch (SQLException e) {
			
			e.printStackTrace();
		}finally{
			try{
				con.close();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		UserInfoBean userBean= new UserInfoBean();

		
		LoginBean loginBean = new UserAuthenticationHelper()
				.loginAuthentication(userName, password, "WEB", "2000",null,false);
		
		if("success".equals(loginBean.getStatus()) || "FirstTime".equals(loginBean.getStatus())){
			userBean = loginBean.getUserInfo();
	       /* Connection connection = DBConnect.getConnection();
	        try {
				PreparedStatement pstmt = connection.prepareStatement("select merchant_ip,user_name,password from st_lms_merchant_auth_master where  user_name = ?");
				pstmt.setString(1, userName);
				ResultSet rs = pstmt.executeQuery();
				String merIp = null;
				String tpPass = null;			
				if(rs.next()){
					merIp = rs.getString("merchant_ip");
					tpPass = rs.getString("password");
				}
				if(userBean.isTPUser()){
					userBean.setTpIp(merIp);
					userBean.setTpTxnPassword(tpPass);
				}
			} catch (SQLException e) {
				
				e.printStackTrace();
			}	*/
		}else{
			userBean.setStatus("FAILED");
			System.out.println(" password does not match");
			return userBean;
		}
			
		return userBean;
	}
	
	
	
	public UserInfoBean getUserDataFromUserId(int userId){
		UserInfoBean userInfo= null;
		Connection con=DBConnect.getConnection();
		String getUserDataQry="select user_id,om.organization_id,role_id,parent_user_id,om.organization_type,registration_date,user_name,name,available_credit,credit_limit,claimable_bal,unclaimable_bal,current_credit_amt,organization_status,status,parent_id,pwt_scrap,tp_organization,isrolehead from st_lms_user_master um,st_lms_organization_master om where um.organization_id=om.organization_id and um.user_id="+userId;
		try{
		Statement stmt=con.createStatement();
		ResultSet rs=stmt.executeQuery(getUserDataQry);
		if(rs.next()){
			userInfo = new UserInfoBean();
			
			userInfo.setRoleId(rs.getInt("role_id"));
			userInfo.setUserId(rs.getInt("user_id"));
			userInfo.setUserName(rs.getString("user_name"));
			userInfo.setUserOrgId(rs.getInt("organization_id"));
			userInfo.setUserType(rs.getString("om.organization_type"));
			userInfo.setOrgName(rs.getString("name"));
			userInfo.setAvailableCreditLimit(rs.getDouble("available_credit"));
			userInfo.setClaimableBal(rs.getDouble("claimable_bal"));
			userInfo.setUnclaimableBal(rs.getDouble("unclaimable_bal"));
			userInfo.setCurrentCreditAmt(rs.getDouble("current_credit_amt"));
			userInfo.setStatus(rs.getString("status"));
			userInfo.setOrgStatus(rs.getString("organization_status"));
			userInfo.setPwtSacrap(rs.getString("pwt_scrap"));
			userInfo.setParentOrgId(rs.getInt("parent_id"));
			userInfo.setIsRoleHeadUser(rs.getString("isrolehead"));
			
			
		}
		}catch (Exception e) {
			e.printStackTrace();
			
		}finally{
			try{
				con.close();
			}catch(SQLException e){
				e.printStackTrace();
			}
		}
		
		
		return userInfo;
	}
	
	
	
	public LmsWrapperPwtApiBean verifyDirectPlrTicketNo(UserInfoBean userInfoBean,String ticketNo) throws LMSException{
		 LmsWrapperPwtApiBean pwtApiBean=new LmsWrapperPwtApiBean();
		 boolean isSuccess = true;
		 
		 String pwtAmtForMasterApproval = (String) LMSUtility.sc.getAttribute("PWT_APPROVAL_LIMIT");
			String highPrizeScheme = (String) LMSUtility.sc.getAttribute("DRAW_GAME_HIGH_PRIZE_SCHEME");
			String highPrizeAmt = (String) LMSUtility.sc.getAttribute("HIGH_PRIZE_AMT");
			String refMerchantId = (String) LMSUtility.sc.getAttribute("REF_MERCHANT_ID");
			MainPWTDrawBean  mainPwtBean = new MainPWTDrawBean();
			mainPwtBean.setTicketNo(ticketNo.trim());
			logger.debug("*****ticketNbr***" + ticketNo);
			BOPwtProcessHelper pwtHelper = new BOPwtProcessHelper();
			MainPWTDrawBean pwtWinningBean = pwtHelper.newMethod(mainPwtBean, userInfoBean, pwtAmtForMasterApproval, highPrizeScheme, refMerchantId,highPrizeAmt);
			
			if("UN_AUTH".equals(pwtWinningBean.getStatus())) {
				pwtApiBean.setStatus("-2");
				return pwtApiBean;
			}
			
			if("error".equalsIgnoreCase(pwtWinningBean.getWinningBeanList().get(0).getStatus()) || "ERROR_INVALID".equals(pwtWinningBean.getStatus())){
				pwtApiBean.setStatus("ERROR_INVALID");
				return pwtApiBean;
			}
					
			boolean isMasAppReq = true;
			//pwtWinningBean.setGameDispName(Util.getGameDisplayName(pwtWinningBean
			//		.getGameNo()));
			//session.setAttribute("PWT_RES", pwtWinningBean);
			
			for(PWTDrawBean pwtDrawBean : pwtWinningBean.getWinningBeanList()) {
				if(pwtDrawBean.getDrawWinList() != null) {
					for(DrawIdBean drawIdBean : pwtDrawBean.getDrawWinList()) {
						if(drawIdBean.getRankId() == 4 && "TwelveByTwentyFour".equals(Util.getGameName(pwtWinningBean.getGameId()))) {
							pwtWinningBean.setStatus("-1");
							isSuccess = false;
						}
					}
				}
			}
			
			if (pwtWinningBean.isValid() && pwtWinningBean.isHighPrize()) {
				pwtApiBean.setTotalTicketPwtAmount(pwtWinningBean.getTotlticketAmount());
				//session.setAttribute("PWT_AMT", pwtWinningBean.getTotlticketAmount());
				
				if ("MAS_APP_REQ".equalsIgnoreCase(pwtWinningBean.getPwtStatus())) {
					isMasAppReq = false;
				}
				
				pwtApiBean.setMessage("registration");
			
			} else if (pwtWinningBean.isValid() && pwtWinningBean.isWinTkt() && isSuccess) {
				 pwtApiBean= registerAnyPlayer(pwtWinningBean,userInfoBean,pwtApiBean);// register the player as
				
				 String status=pwtApiBean.getStatus();// anonymous
				if (status.equals("SUCCESS")) {
					pwtApiBean.setMessage("paySuccess");
					
				} else {
					pwtApiBean.setMessage("error");
					pwtApiBean.setSuccess(false);
				}
			} else {
				pwtApiBean.setMessage("SUCCESS");
				pwtApiBean.setSuccess(true);
				
			}
			ArrayList<LmsWrapperPWTDrawBean> lmsPwtBean=new ArrayList<LmsWrapperPWTDrawBean>();
			
			if(pwtWinningBean.getWinningBeanList()!=null){
			for(int i=0;i<pwtWinningBean.getWinningBeanList().size();i++){
				List<LmsWrapperDrawIdBean> lmsDrawBean=new ArrayList<LmsWrapperDrawIdBean>();
				PWTDrawBean pwtDrawBean=pwtWinningBean.getWinningBeanList().get(i);
				
				if(pwtDrawBean.getDrawWinList() != null){
				for(int j=0;j<pwtDrawBean.getDrawWinList().size();j++){
					DrawIdBean drawBean=pwtDrawBean.getDrawWinList().get(j);
				ArrayList<LmsWrapperPanelIdBean> panelWinList =new ArrayList<LmsWrapperPanelIdBean>();
					
				if(drawBean.getPanelWinList() !=null){
				for(int k=0;k<drawBean.getPanelWinList().size();k++){
						PanelIdBean panelBean=drawBean.getPanelWinList().get(k);
						LmsWrapperPanelIdBean lmsWrapperPanelIdBean =new LmsWrapperPanelIdBean();
						lmsWrapperPanelIdBean.setAppReq(panelBean.isAppReq());
						lmsWrapperPanelIdBean.setBetAmtMultiple(panelBean.getBetAmtMultiple());
						lmsWrapperPanelIdBean.setHighLevel(panelBean.isHighLevel());
						lmsWrapperPanelIdBean.setLineId(panelBean.getLineId());
						lmsWrapperPanelIdBean.setMessage(panelBean.getMessage());
						lmsWrapperPanelIdBean.setMessageCode(panelBean.getMessageCode());
						lmsWrapperPanelIdBean.setPanelId(panelBean.getPanelId());
						lmsWrapperPanelIdBean.setPartyId(panelBean.getPartyId());
						lmsWrapperPanelIdBean.setPartyName(panelBean.getPartyName());
						lmsWrapperPanelIdBean.setPickedData(panelBean.getPickedData());
						lmsWrapperPanelIdBean.setPlayType(panelBean.getPlayType());
						lmsWrapperPanelIdBean.setStatus(panelBean.getStatus());
						lmsWrapperPanelIdBean.setValid(panelBean.isValid());
						lmsWrapperPanelIdBean.setVerificationStatus(panelBean.getVerificationStatus());
						lmsWrapperPanelIdBean.setWinningAmt(panelBean.getWinningAmt());
						
						panelWinList.add(lmsWrapperPanelIdBean);
					}
				}
					LmsWrapperDrawIdBean lmsWrapperDrawIdBean =new LmsWrapperDrawIdBean();
					lmsWrapperDrawIdBean.setAppReq(drawBean.isAppReq());
					lmsWrapperDrawIdBean.setClaimedTime(drawBean.getClaimedTime());
					lmsWrapperDrawIdBean.setDrawDateTime(drawBean.getDrawDateTime());
					lmsWrapperDrawIdBean.setDrawId(drawBean.getDrawId());
					lmsWrapperDrawIdBean.setHighLevel(drawBean.isHighLevel());
					lmsWrapperDrawIdBean.setMessage(drawBean.getMessage());
					lmsWrapperDrawIdBean.setMessageCode(drawBean.getMessageCode());
					lmsWrapperDrawIdBean.setPanelWinList(panelWinList);
					lmsWrapperDrawIdBean.setStatus(drawBean.getStatus());
					lmsWrapperDrawIdBean.setTableName(drawBean.getTableName());
					lmsWrapperDrawIdBean.setValid(drawBean.isValid());
					lmsWrapperDrawIdBean.setVerificationStatus(drawBean.getVerificationStatus());
					lmsWrapperDrawIdBean.setWinningAmt(drawBean.getWinningAmt());
					lmsWrapperDrawIdBean.setWinResult(drawBean.getWinResult());
					
					lmsDrawBean.add(lmsWrapperDrawIdBean);
				}
			}
				LmsWrapperPWTDrawBean lmsWrapperPwtDrawBean=new LmsWrapperPWTDrawBean();
				//lmsWrapperPwtDrawBean.setAdvMsg( pwtDrawBean.getAdvMsg());
				lmsWrapperPwtDrawBean.setDrawWinList(lmsDrawBean);
				lmsWrapperPwtDrawBean.setGameDispName(pwtDrawBean.getGameDispName());
				lmsWrapperPwtDrawBean.setGameId(pwtDrawBean.getGameId());
				lmsWrapperPwtDrawBean.setGameNo(pwtDrawBean.getGameNo());
				lmsWrapperPwtDrawBean.setHighPrize(pwtDrawBean.isHighPrize());
				lmsWrapperPwtDrawBean.setPartyId(pwtDrawBean.getPartyId());
				lmsWrapperPwtDrawBean.setPartyType(pwtDrawBean.getPartyType());
				lmsWrapperPwtDrawBean.setPwtStatus(pwtDrawBean.getPwtStatus());
				lmsWrapperPwtDrawBean.setPwtTicketType(pwtDrawBean.getPwtTicketType());
				lmsWrapperPwtDrawBean.setRaffelAssociated(pwtDrawBean.isRaffelAssociated());
			//	lmsWrapperPwtDrawBean.setRaffleDrawIdBeanList(raffleDrawIdBeanList)
				lmsWrapperPwtDrawBean.setRefMerchantId(pwtDrawBean.getRefMerchantId());
				lmsWrapperPwtDrawBean.setReprint(pwtDrawBean.isReprint());
				lmsWrapperPwtDrawBean.setReprintCount(pwtDrawBean.getReprintCount());
				lmsWrapperPwtDrawBean.setResAwaited(pwtDrawBean.isResAwaited());
				lmsWrapperPwtDrawBean.setStatus(pwtDrawBean.getStatus());
				lmsWrapperPwtDrawBean.setTicketNo(pwtDrawBean.getTicketNo());
				lmsWrapperPwtDrawBean.setTotalAmount(pwtDrawBean.getTotalAmount());
				lmsWrapperPwtDrawBean.setUserId(pwtDrawBean.getUserId());
				lmsWrapperPwtDrawBean.setValid(pwtDrawBean.isValid());
				lmsWrapperPwtDrawBean.setWinTkt(pwtDrawBean.isWinTkt());
				
				
				lmsPwtBean.add(lmsWrapperPwtDrawBean);
			}
			
	}			
			LmsWrapperMainPWTDrawBean lmsWrapperPwtWinningBean=new LmsWrapperMainPWTDrawBean();
			//lmsWrapperPwtWinningBean.setAdvMsg(pwtWinningBean.getAdvMsg());
			lmsWrapperPwtWinningBean.setHighPrize(pwtWinningBean.isHighPrize());
			lmsWrapperPwtWinningBean.setIsmPesaEnable(pwtWinningBean.isIsmPesaEnable());
			lmsWrapperPwtWinningBean.setMainTktGameNo(pwtWinningBean.getMainTktGameNo());
			lmsWrapperPwtWinningBean.setMobileNumber(pwtWinningBean.getMobileNumber());
			//lmsWrapperPwtWinningBean.setPurchaseBean((LmsWrapperKenoPurchaseBean) pwtWinningBean.getPurchaseBean());
			if(pwtWinningBean.getPurchaseBean() != null){
				KenoPurchaseBean kenoBean=(KenoPurchaseBean) pwtWinningBean.getPurchaseBean();
				LmsWrapperKenoPurchaseBean wrapperKenoBean=new LmsWrapperKenoPurchaseBean();
				wrapperKenoBean.setAdvMsg(kenoBean.getAdvMsg());
				wrapperKenoBean.setBetAmountMultiple(kenoBean.getBetAmountMultiple());
				wrapperKenoBean.setBonus(kenoBean.getBonus());
				wrapperKenoBean.setDrawDateTime(kenoBean.getDrawDateTime());
				wrapperKenoBean.setDrawIdTableMap(kenoBean.getDrawIdTableMap());
				wrapperKenoBean.setDrawNameList(kenoBean.getDrawNameList());
				wrapperKenoBean.setGame_no(kenoBean.getGame_no());
				wrapperKenoBean.setGameDispName(kenoBean.getGameDispName());
				wrapperKenoBean.setIsAdvancedPlay(kenoBean.getIsAdvancedPlay());
				wrapperKenoBean.setIsQuickPick(kenoBean.getIsQuickPick());
				wrapperKenoBean.setLastSoldTicketNo(kenoBean.getLastSoldTicketNo());
				wrapperKenoBean.setNoOfDraws(kenoBean.getNoOfDraws());
				wrapperKenoBean.setNoOfLines(kenoBean.getNoOfLines());
				wrapperKenoBean.setNoOfPanel(kenoBean.getNoOfPanel());
				wrapperKenoBean.setNoPicked(kenoBean.getNoPicked());
				wrapperKenoBean.setPartyId(kenoBean.getPartyId());
				wrapperKenoBean.setPlayerData(kenoBean.getPlayerData());
				wrapperKenoBean.setPlayerPicked(kenoBean.getPlayerPicked());
				wrapperKenoBean.setPlayType(kenoBean.getPlayType());
				wrapperKenoBean.setPurchaseChannel(kenoBean.getPurchaseChannel());
				wrapperKenoBean.setPurchaseTime(kenoBean.getPurchaseTime());
				wrapperKenoBean.setRaffelAssociated(kenoBean.isRaffelAssociated());
				wrapperKenoBean.setRaffleDrawIdTableMap(kenoBean.getRaffleDrawIdTableMap());
				wrapperKenoBean.setRaffleNo(kenoBean.getRaffleNo());
				wrapperKenoBean.setRefMerchantId(kenoBean.getRefMerchantId());
				wrapperKenoBean.setReprintCount(kenoBean.getReprintCount());
				wrapperKenoBean.setSaleStatus(kenoBean.getSaleStatus());
				wrapperKenoBean.setTicket_no(kenoBean.getTicket_no());
				wrapperKenoBean.setTotalPurchaseAmt(kenoBean.getTotalPurchaseAmt());
				wrapperKenoBean.setUnitPrice(kenoBean.getUnitPrice());
				wrapperKenoBean.setUserId(kenoBean.getUserId());
				wrapperKenoBean.setBarcodeCount(kenoBean.getBarcodeCount());
				wrapperKenoBean.setRetailerName(Util.getOrgNameFromTktNo((kenoBean.getTicket_no() + kenoBean.getReprintCount()),"AGENT"));
				wrapperKenoBean.setExpiryPeriod(Util.getGameMap().get(kenoBean.getGameId()).getTicketExpiryPeriod() );
				wrapperKenoBean.setCurrSymbol((String) LMSUtility.sc.getAttribute("CURRENCY_SYMBOL"));
				wrapperKenoBean.setOrgAddress((String) LMSUtility.sc.getAttribute("ORG_ADDRESS"));
				wrapperKenoBean.setOrgName(userInfoBean.getOrgName());
				String ticketExpiryEnabled = (String) LMSUtility.sc.getAttribute("TICKET_EXPIRY_ENABLED");
				if ("YES".equalsIgnoreCase(ticketExpiryEnabled)) {
					wrapperKenoBean.setTicketExpiryEnabled(true);
				}else{
					wrapperKenoBean.setTicketExpiryEnabled(false);
				}
				lmsWrapperPwtWinningBean.setPurchaseBean(wrapperKenoBean);	
			}
			
			lmsWrapperPwtWinningBean.setPwtStatus(pwtWinningBean.getPwtStatus());
			lmsWrapperPwtWinningBean.setPwtTicketType(pwtWinningBean.getPwtTicketType());
			lmsWrapperPwtWinningBean.setRefNumber(pwtWinningBean.getRefNumber());
			lmsWrapperPwtWinningBean.setReprint(pwtWinningBean.isReprint());
			lmsWrapperPwtWinningBean.setStatus(pwtWinningBean.getStatus());
			lmsWrapperPwtWinningBean.setTicketNo(pwtWinningBean.getTicketNo());
			lmsWrapperPwtWinningBean.setTotlticketAmount(pwtWinningBean.getTotlticketAmount());
			lmsWrapperPwtWinningBean.setTransactionIdList(pwtWinningBean.getTransactionIdList());
			lmsWrapperPwtWinningBean.setValid(pwtWinningBean.isValid());
			lmsWrapperPwtWinningBean.setWinningBeanList(lmsPwtBean);
			lmsWrapperPwtWinningBean.setWinTkt(pwtWinningBean.isWinTkt());
			lmsWrapperPwtWinningBean.setOrgName(userInfoBean.getOrgName()+":"+Util.getOrgNameFromTktNo((pwtWinningBean.getTicketNo()),"AGENT"));
			
			pwtApiBean.setMainPwtDrawBean(lmsWrapperPwtWinningBean);
		 return pwtApiBean;
	}
	public LmsWrapperPwtApiBean registerAnyPlayer(MainPWTDrawBean pwtDrawBean,UserInfoBean userInfoBean,LmsWrapperPwtApiBean pwtApiBean) throws LMSException{

		PlayerBean plrBean = null;
		boolean isAnonymous = true;
		
		Map pwtAppMap = new TreeMap();
		String rootPath ="";
		
		if (userInfoBean == null) {
			throw new LMSException("userInfoBean = " + userInfoBean);
		}
		// player registration and approval process
		BOPwtProcessHelper helper = new BOPwtProcessHelper();
		logger.debug("root path is " + rootPath);
		pwtAppMap = helper.plrRegistrationAndApproval(userInfoBean,
				pwtDrawBean, null, 0, plrBean, rootPath,
				isAnonymous);
		pwtDrawBean=(MainPWTDrawBean) pwtAppMap.get("PWT_RES_BEAN");
		
		pwtApiBean.setIsAnonymous(pwtAppMap.get("isAnonymous").toString());
		pwtApiBean.setGameName(pwtAppMap.get("GAME_NAME").toString());
		
		pwtApiBean.setNetAmountPaid (Double.parseDouble(pwtAppMap.get("NET_AMOUNT_PAID").toString()) );
		pwtApiBean.setRecId(pwtAppMap.get("recId").toString());
		pwtApiBean.setStatus("SUCCESS");
	//	session.setAttribute("plrPwtAppDetMap", pwtAppMap);
		//session.setAttribute("PWT_RES", pwtAppMap.get("PWT_RES_BEAN"));

		return pwtApiBean;
	
	}
	
	/*public LinkedList<LmsWrapperOrgRegShiftBean> getOrgRegShiftData(String agentName){
		LinkedList<LmsWrapperOrgRegShiftBean> orgRegShiftBeanList=new LinkedList<LmsWrapperOrgRegShiftBean>();
		try{
		orgRegShiftBeanList=getOrgDataFromAgentName(agentName);
		}
		catch (SQLException se) {
			
		}
		return orgRegShiftBeanList;
	}*/
	
	public LmsWrapperOrganizationRegShiftDataBean getOrgRegShiftData(String agentName) {
		Connection con=DBConnect.getConnection();
		System.out.println("AGENT NAME::"+agentName);
		LmsWrapperOrganizationRegShiftDataBean orgRegistrationShiftDataBean=new LmsWrapperOrganizationRegShiftDataBean();
		if(WrapperUtility.isTerminateAgent(agentName)){
			orgRegistrationShiftDataBean.setErrorCode("100");
			 orgRegistrationShiftDataBean.setSuccess(true);
			return orgRegistrationShiftDataBean;
		}
		
		LinkedList<LmsWrapperOrgRegShiftBean> orgRegShiftBeanList=new LinkedList<LmsWrapperOrgRegShiftBean>();
		
		String organisationData="select om.organization_id,om.parent_id,name,organization_type,addr_line1,addr_line2,city,state_code,country_code,pin_code,credit_limit,security_deposit,organization_status,vat_registration_nbr,pwt_scrap,recon_report_type,verification_limit,pay_limit,scrap_limit,approval_limit from st_lms_organization_master om,st_lms_oranization_limits ol where (om.name='"+ agentName +"' or om.parent_id in (select organization_id from st_lms_organization_master where name='"+agentName+"')) " 
			+"and om.organization_id=ol.organization_id";
  
		 try{
			Statement stmt=con.createStatement();
			System.out.println("Get Organization Data::"+organisationData);
			ResultSet rs=stmt.executeQuery(organisationData);
			while(rs.next()){
				LmsWrapperOrgRegShiftBean orgRegShiftBean=new LmsWrapperOrgRegShiftBean();
				LmsWrapperOrgRegistrationBean orgRegistrationBean=new LmsWrapperOrgRegistrationBean();
				LmsWrapperUserRegistrationBean userRegistrationBean=new LmsWrapperUserRegistrationBean();
				ArrayList<LmsWrapperInventoryDetailBean> invDeatilBeanList=new ArrayList<LmsWrapperInventoryDetailBean>();
				orgRegistrationBean.setOrgName(rs.getString("name"));
				
				userRegistrationBean =getuserDataFromAgentName(orgRegistrationBean.getOrgName(),con);
				orgRegShiftBean.setUserRegistrationBean(userRegistrationBean);
				
				orgRegistrationBean.setOrgType(rs.getString("organization_type"));
				orgRegistrationBean.setAgentName(agentName);
				if("AGENT".equalsIgnoreCase(rs.getString("organization_type"))){
					orgRegistrationBean.setAgtOrgId(rs.getInt("organization_id"));
				    
				}else{
					
					orgRegistrationBean.setAgtOrgId(rs.getInt("parent_id"));
					orgRegistrationBean.setOrgId(rs.getInt("organization_id"));
				}
				
				invDeatilBeanList=getInventoryDataFromOrgId(rs.getString("organization_id"),orgRegistrationBean.getOrgType(), con);
				orgRegShiftBean.setInvDeatilBeanList(invDeatilBeanList);
				
				orgRegistrationBean.setAddrLine1(rs.getString("addr_line1"));
				orgRegistrationBean.setAddrLine2(rs.getString("addr_line2"));
				orgRegistrationBean.setCity(rs.getString("city"));
				
				Statement stmtCountry=con.createStatement();
				String getCountryAndState="select ss.name as state_name,cc.name as country_name from st_lms_country_master cc,st_lms_state_master ss where cc.country_code=ss.country_code and cc.country_code='"+rs.getString("country_code")+"' and ss.state_code='"+rs.getString("state_code")+"'";
				System.out.println("Get getCountryAndState Data::"+getCountryAndState);
				ResultSet rsCont=stmtCountry.executeQuery(getCountryAndState);
				
				if(rsCont.next()){
				orgRegistrationBean.setState(rsCont.getString("state_name"));
				orgRegistrationBean.setCountry(rsCont.getString("country_name"));
				}
				
				orgRegistrationBean.setPinCode(rs.getString("pin_code"));
				orgRegistrationBean.setCreditLimit(0.0);
				orgRegistrationBean.setSecurity(0.0);
				orgRegistrationBean.setStatusorg(rs.getString("organization_status"));
				orgRegistrationBean.setVatRegNo(rs.getString("vat_registration_nbr"));
				orgRegistrationBean.setReconReportType(rs.getString("recon_report_type"));
				orgRegistrationBean.setVerLimit(rs.getString("verification_limit"));
				orgRegistrationBean.setAppLimit(rs.getString("approval_limit"));
				orgRegistrationBean.setAutoScrap(rs.getString("pwt_scrap"));
		        orgRegShiftBean.setOrgType(rs.getString("organization_type"));
		        orgRegShiftBean.setOrgRegistrationBean(orgRegistrationBean);
		        orgRegShiftBean.setOrgName(rs.getString("name"));
				orgRegShiftBeanList.add(orgRegShiftBean);
			}
			orgRegistrationShiftDataBean.setOrgRegShiftBean(orgRegShiftBeanList);
			orgRegistrationShiftDataBean.setAgentName(agentName);
		 }
		 catch (Exception e) {
			 e.printStackTrace();
			 orgRegistrationShiftDataBean.setSuccess(false);
			 orgRegistrationShiftDataBean.setErrorCode("500");
			 return orgRegistrationShiftDataBean;
		}
		 finally{
			 try{
				 con.close();
			 }catch (Exception e) {
				e.printStackTrace();
			}
		 }
		 orgRegistrationShiftDataBean.setErrorCode("100");
		 orgRegistrationShiftDataBean.setSuccess(true);
		return orgRegistrationShiftDataBean;
	}
	
	public LmsWrapperOrganizationRegShiftDataBean getOrgRegShiftData(String agentName , String agentUserId) {
		Connection con=DBConnect.getConnection();
		System.out.println("AGENT NAME::"+agentName);
		LmsWrapperOrganizationRegShiftDataBean orgRegistrationShiftDataBean=new LmsWrapperOrganizationRegShiftDataBean();
		if(WrapperUtility.isTerminateAgent(agentName)){
			orgRegistrationShiftDataBean.setErrorCode("100");
			 orgRegistrationShiftDataBean.setSuccess(true);
			return orgRegistrationShiftDataBean;
		}
		
		LinkedList<LmsWrapperOrgRegShiftBean> orgRegShiftBeanList=new LinkedList<LmsWrapperOrgRegShiftBean>();
		
		/*String organisationData="select om.organization_id,om.parent_id,name,organization_type,addr_line1,addr_line2,city,state_code,country_code,pin_code,credit_limit,security_deposit,organization_status,vat_registration_nbr,pwt_scrap,recon_report_type,verification_limit,pay_limit,scrap_limit,approval_limit from st_lms_organization_master om,st_lms_oranization_limits ol where (om.name='"+ agentName +"' or om.parent_id in (select organization_id from st_lms_organization_master where name='"+agentName+"')) " 
			+"and om.organization_id=ol.organization_id";*/
		

		String organisationData = "select om.organization_id,om.parent_id,om.org_code,name,organization_type,addr_line1,addr_line2,city,state_code,country_code,pin_code,credit_limit,security_deposit,organization_status,vat_registration_nbr,pwt_scrap,recon_report_type,verification_limit,pay_limit,scrap_limit,approval_limit from st_lms_organization_master om,st_lms_oranization_limits ol , (select a.organization_id from (select organization_id, user_id , organization_type, parent_user_id, user_name from st_lms_user_master where parent_user_id in (select user_id from st_lms_user_master where organization_id in (select organization_id  from st_lms_user_master where user_id  =  '"+agentUserId+"')) and organization_type = 'RETAILER') a inner join st_lms_ret_offline_master b on a.user_id =  b.user_id and serial_number <> '-1' union all select organization_id from st_lms_user_master where user_id  = '"+agentUserId+"') a where om.organization_id=ol.organization_id and ol.organization_id = a.organization_id order by organization_id"; 

  
		 try{
			Statement stmt=con.createStatement();
			System.out.println("Get Organization Data::"+organisationData);
			ResultSet rs=stmt.executeQuery(organisationData);
			while(rs.next()){
				LmsWrapperOrgRegShiftBean orgRegShiftBean=new LmsWrapperOrgRegShiftBean();
				LmsWrapperOrgRegistrationBean orgRegistrationBean=new LmsWrapperOrgRegistrationBean();
				LmsWrapperUserRegistrationBean userRegistrationBean=new LmsWrapperUserRegistrationBean();
				ArrayList<LmsWrapperInventoryDetailBean> invDeatilBeanList=new ArrayList<LmsWrapperInventoryDetailBean>();
				orgRegistrationBean.setOrgName(rs.getString("name"));
				
				userRegistrationBean =getuserDataFromAgentName(orgRegistrationBean.getOrgName(),con);
				orgRegShiftBean.setUserRegistrationBean(userRegistrationBean);
				
				orgRegistrationBean.setOrgType(rs.getString("organization_type"));
				orgRegistrationBean.setAgentName(agentName);
				if("AGENT".equalsIgnoreCase(rs.getString("organization_type"))){
					orgRegistrationBean.setAgtOrgId(rs.getInt("organization_id"));
				    
				}else{
					
					orgRegistrationBean.setAgtOrgId(rs.getInt("parent_id"));
					orgRegistrationBean.setOrgId(rs.getInt("organization_id"));
				}
				
				invDeatilBeanList=getInventoryDataFromOrgId(rs.getString("organization_id"),orgRegistrationBean.getOrgType(), con);
				orgRegShiftBean.setInvDeatilBeanList(invDeatilBeanList);
				
				orgRegistrationBean.setAddrLine1(rs.getString("addr_line1"));
				orgRegistrationBean.setAddrLine2(rs.getString("addr_line2"));
				orgRegistrationBean.setCity(rs.getString("city"));
				
				Statement stmtCountry=con.createStatement();
				String getCountryAndState="select ss.name as state_name,cc.name as country_name from st_lms_country_master cc,st_lms_state_master ss where cc.country_code=ss.country_code and cc.country_code='"+rs.getString("country_code")+"' and ss.state_code='"+rs.getString("state_code")+"'";
				System.out.println("Get getCountryAndState Data::"+getCountryAndState);
				ResultSet rsCont=stmtCountry.executeQuery(getCountryAndState);
				
				if(rsCont.next()){
				orgRegistrationBean.setState(rsCont.getString("state_name"));
				orgRegistrationBean.setCountry(rsCont.getString("country_name"));
				}
				
				orgRegistrationBean.setPinCode(rs.getString("pin_code"));
				orgRegistrationBean.setCreditLimit(0.0);
				orgRegistrationBean.setSecurity(0.0);
				orgRegistrationBean.setStatusorg(rs.getString("organization_status"));
				orgRegistrationBean.setVatRegNo(rs.getString("vat_registration_nbr"));
				orgRegistrationBean.setReconReportType(rs.getString("recon_report_type"));
				orgRegistrationBean.setVerLimit(rs.getString("verification_limit"));
				orgRegistrationBean.setAppLimit(rs.getString("approval_limit"));
				orgRegistrationBean.setAutoScrap(rs.getString("pwt_scrap"));
		        orgRegShiftBean.setOrgType(rs.getString("organization_type"));
		        orgRegShiftBean.setOrgRegistrationBean(orgRegistrationBean);
		        orgRegShiftBean.setOrgName(rs.getString("name"));
		        orgRegistrationBean.setOrgCode(rs.getString("org_code"));
				orgRegShiftBeanList.add(orgRegShiftBean);
			}
			Collections.sort(orgRegShiftBeanList, new LmsWrapperOrgRegShiftBeanComp());
			orgRegistrationShiftDataBean.setOrgRegShiftBean(orgRegShiftBeanList);
			orgRegistrationShiftDataBean.setAgentName(agentName);
		 }
		 catch (Exception e) {
			 e.printStackTrace();
			 orgRegistrationShiftDataBean.setSuccess(false);
			 orgRegistrationShiftDataBean.setErrorCode("500");
			 return orgRegistrationShiftDataBean;
		}
		 finally{
			 try{
				 con.close();
			 }catch (Exception e) {
				e.printStackTrace();
			}
		 }
		 orgRegistrationShiftDataBean.setErrorCode("100");
		 orgRegistrationShiftDataBean.setSuccess(true);
		return orgRegistrationShiftDataBean;
	}
	
	public LmsWrapperUserRegistrationBean getuserDataFromAgentName(String orgName,Connection con){
		LmsWrapperUserRegistrationBean userRegistrationBean=new LmsWrapperUserRegistrationBean();
		
		String userData="select um.user_id,user_name,password,auto_password,secret_ques,secret_ans,first_name,last_name,id_type,id_nbr,email_id,phone_nbr,mobile_nbr from st_lms_user_master um,st_lms_user_contact_details ucd where um.organization_id=(select organization_id from st_lms_organization_master where name='"+orgName+"') and um.user_id=ucd.user_id and isrolehead='Y'";
  
		 try {
			Statement stmt=con.createStatement();
			System.out.println("User Data::"+userData);
			ResultSet rs=stmt.executeQuery(userData);
			while(rs.next()){
				userRegistrationBean.setUserName(rs.getString("user_name"));
				userRegistrationBean.setSecQues(rs.getString("secret_ques"));
                userRegistrationBean.setSecAns(rs.getString("secret_ans"));
                userRegistrationBean.setFirstName(rs.getString("first_name"));
                userRegistrationBean.setLastName(rs.getString("last_name"));
				userRegistrationBean.setIdNo(rs.getString("id_nbr")+rs.getInt("user_id"));
				userRegistrationBean.setOriIdNo(rs.getString("id_nbr"));
				userRegistrationBean.setIdType(rs.getString("id_type"));
				userRegistrationBean.setEmail(rs.getString("email_id"));
				userRegistrationBean.setPhone(rs.getLong("phone_nbr"));
				userRegistrationBean.setPassword(rs.getString("password"));
				userRegistrationBean.setAutoPassword(rs.getInt("auto_password"));
				if(rs.getString("mobile_nbr") != null){
					userRegistrationBean.setMobileNum(rs.getString("mobile_nbr"));
				}else{
					userRegistrationBean.setMobileNum("0");
				}
				
			}
			
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		
		
		return userRegistrationBean;
	}
	
	public ArrayList<LmsWrapperInventoryDetailBean> getInventoryDataFromOrgId(String orgId,String orgType,Connection con){
		
		ArrayList<LmsWrapperInventoryDetailBean> invDataBeanList=new ArrayList<LmsWrapperInventoryDetailBean>();
		
		
		String invData="select inv_model_id,serial_no,cost_to_bo,is_new,brand_id,inv_id  from (select inv_model_id,serial_no,cost_to_bo,is_new from st_lms_inv_status where current_owner_id= "+orgId+" union all select inv_model_id,serial_no,cost_to_bo,is_new from st_lms_inv_status where current_owner_id in (select organization_id from"
		               + " st_lms_organization_master where parent_id= "+orgId+" ) ) aaa inner join st_lms_inv_model_master mm on aaa.inv_model_id=mm.model_id order by inv_model_id asc";
  
		 try {
			 HashMap<String,ArrayList<String>> invDetMap=new HashMap<String, ArrayList<String>>();
			Statement stmt=con.createStatement();
			System.out.println("Get inventory Data From Organization Id::"+invData);
			ResultSet rs=stmt.executeQuery(invData);
			while(rs.next()){
				
				ArrayList<String> serNoList=new ArrayList<String>();
				
				if(invDetMap.get(rs.getString("inv_model_id")+"-"+rs.getString("cost_to_bo")+"-"+rs.getString("is_new")+"-"+rs.getString("brand_id")+"-"+rs.getString("inv_id")) != null){
					invDetMap.get(rs.getString("inv_model_id")+"-"+rs.getString("cost_to_bo")+"-"+rs.getString("is_new")+"-"+rs.getString("brand_id")+"-"+rs.getString("inv_id")).add(rs.getString("serial_no"));
				}else{
					
					serNoList.add(rs.getString("serial_no"));
				    invDetMap.put(rs.getString("inv_model_id")+"-"+rs.getString("cost_to_bo")+"-"+rs.getString("is_new")+"-"+rs.getString("brand_id")+"-"+rs.getString("inv_id"), serNoList);
				   System.out.println("Inventor Detail Map for OrgId"+orgId+":::"+invDetMap);
				}
			}
			
			//set datav to model,brand and inventory map
               InventoryHelper.setInventoryDataToMap();
			
			
			Set<String> invItr=invDetMap.keySet();
			for(String modelinv: invItr){
				LmsWrapperInventoryDetailBean invDataBean=new LmsWrapperInventoryDetailBean();
				invDataBean.setModelId(modelinv.split("-")[0]);
				invDataBean.setNonConsModelName(InventoryHelper.modelIdMap.get(modelinv.split("-")[0]));
				invDataBean.setCost(Double.parseDouble(modelinv.split("-")[1]));
				invDataBean.setIsNew(modelinv.split("-")[2]);
				invDataBean.setBrandsId(modelinv.split("-")[3]);
				invDataBean.setNonConsBrandName(InventoryHelper.brandIdMap.get(modelinv.split("-")[3]));
				invDataBean.setInvId(modelinv.split("-")[4]);
				invDataBean.setNonConsInvName(InventoryHelper.inventoryIdMap.get(modelinv.split("-")[4]));
				invDataBean.setOrgId(Integer.parseInt(orgId));
				List<String> serNoList=new ArrayList<String>();
				serNoList=invDetMap.get(modelinv);
				//String[] serNo= serNoList.toArray(new String[serNoList.size()]);
				String[] serNo= new String[1];
				serNo[0]=serNoList.toString().replace("[", "").replace("]", "");
				invDataBean.setSerNo(serNo);
				
				invDataBeanList.add(invDataBean);
			}
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		
		
		return invDataBeanList;
	}
	
	public String agentRetailerRegistrationAction(LmsWrapperOrgRegShiftBean orgRegShiftBean){
		String status="FAILED";
		
		if(orgRegShiftBean !=null){
							
				if("AGENT".equalsIgnoreCase(orgRegShiftBean.getOrgType())){
					status=agentOrgUserReg(orgRegShiftBean);
					if("FAILED".equalsIgnoreCase(status)){
						return status;
					}
				}else{
					status=createRetailerOrgAtBO(orgRegShiftBean);
					
					if("FAILED".equalsIgnoreCase(status)){
						return status;
					}
				}
			}
		status="SUCCESS";
		return status;
		
	}
	
	public String agentOrgUserReg(LmsWrapperOrgRegShiftBean orgRegShiftBean){
		String status="FAILED";
		UserInfoBean userInfoBean = getUserData();
		if("FAILED".equalsIgnoreCase(userInfoBean.getStatus())){
			return status;
		}
		
		LmsWrapperOrgRegistrationBean orgRegistrationBean=orgRegShiftBean.getOrgRegistrationBean();
		LmsWrapperUserRegistrationBean userRegistrationBean=orgRegShiftBean.getUserRegistrationBean();
		List<LmsWrapperInventoryDetailBean> invDetBeanList=orgRegShiftBean.getInvDeatilBeanList();
		try{
		
			
			
		//set data to agentRegistrationBean
        AgentRegistrationBean agentRegistrationBean=new AgentRegistrationBean();
        agentRegistrationBean.setAddrLine1(orgRegistrationBean.getAddrLine1());
        agentRegistrationBean.setAddrLine2(orgRegistrationBean.getAddrLine2());
        agentRegistrationBean.setAutoScrap(orgRegistrationBean.getAutoScrap());
        agentRegistrationBean.setCity(orgRegistrationBean.getCity());
        agentRegistrationBean.setCountry(orgRegistrationBean.getCountry());
        agentRegistrationBean.setState(orgRegistrationBean.getState());
        agentRegistrationBean.setCreditLimit(orgRegistrationBean.getCreditLimit());
        agentRegistrationBean.setEmail(userRegistrationBean.getEmail());
        Set<Integer> emailPrivIdList=getEmailPrivListId(orgRegistrationBean.getOrgType());
        String[] emailPrivId=new String[emailPrivIdList.size()];
        int count=0;
        for(Integer emPId : emailPrivIdList){
            emailPrivId[count]=emPId.toString();
            count++;
        }
          
        agentRegistrationBean.setEmailPrivId(emailPrivId);
        agentRegistrationBean.setFirstName(userRegistrationBean.getFirstName());
        agentRegistrationBean.setIdNo(userRegistrationBean.getIdNo());
        agentRegistrationBean.setIdType(userRegistrationBean.getIdType());
        agentRegistrationBean.setLastName(userRegistrationBean.getLastName());
        agentRegistrationBean.setOrgName(orgRegistrationBean.getOrgName());
        agentRegistrationBean.setOrgType(orgRegistrationBean.getOrgType());
        agentRegistrationBean.setPhone(userRegistrationBean.getPhone());
        agentRegistrationBean.setMobile(Long.parseLong(userRegistrationBean.getMobileNum()));
        agentRegistrationBean.setSecAns(userRegistrationBean.getSecAns());
        agentRegistrationBean.setSecQues(userRegistrationBean.getSecQues());
        agentRegistrationBean.setUserName(userRegistrationBean.getUserName());
        agentRegistrationBean.setPin(userRegistrationBean.getPin());
        agentRegistrationBean.setSecurity(orgRegistrationBean.getSecurity());
        agentRegistrationBean.setVerLimit((String)LMSUtility.sc.getAttribute("agtVerLimit"));
        agentRegistrationBean.setPayLimit((String)LMSUtility.sc.getAttribute("agtPayLimit"));
        agentRegistrationBean.setVatRegNo(orgRegistrationBean.getVatRegNo());
        agentRegistrationBean.setStatusorg(orgRegistrationBean.getStatusorg());
        agentRegistrationBean.setReconReportType(orgRegistrationBean.getReconReportType());
        agentRegistrationBean.setAppLimit((String)LMSUtility.sc.getAttribute("agtAppLimit"));
        agentRegistrationBean.setScrapLimit((String)LMSUtility.sc.getAttribute("agtScrapLimit"));
        agentRegistrationBean.setStatus("ACTIVE");
        agentRegistrationBean.setMaxPerDayPayLimit(Double.parseDouble(((String)LMSUtility.sc.getAttribute("MAX_PER_DAY_PAY_LIMIT_FOR_AGENT"))));
        agentRegistrationBean.setSelfClaim(Utility.getPropertyValue("SELF_CLAIM_AGT"));
        agentRegistrationBean.setOtherClaim(Utility.getPropertyValue("OTHER_CLAIM_AGT"));
        agentRegistrationBean.setMinClaimPerTicket(Double.parseDouble(Utility.getPropertyValue("MIN_CLAIM_PER_TICKET_AGT")));
        agentRegistrationBean.setMaxClaimPerTicket(Double.parseDouble(Utility.getPropertyValue("MAX_CLAIM_PER_TICKET_AGT")));
        agentRegistrationBean.setBlockAmt(Double.parseDouble(Utility.getPropertyValue("BLOCK_AMT")));
        agentRegistrationBean.setBlockDays(Integer.parseInt(Utility.getPropertyValue("BLOCK_DAYS")));
        agentRegistrationBean.setBlockAction(Utility.getPropertyValue("BLOCK_ACTION"));
        agentRegistrationBean.setRegFrom("WRAPPER");
        
        List serviceListBean=fetchService(orgRegistrationBean.getOrgType());
        int[] serviceList=null;
        String[] statusTable=null;
        if(serviceListBean != null){
        	serviceList =new int[serviceListBean.size()];
			statusTable=new String[serviceListBean.size()];
			for(int i=0;i< serviceListBean.size();i++){
				AvailableServiceBean  serviceBean=(AvailableServiceBean) serviceListBean.get(i);
				
				serviceList[i]=serviceBean.getMappingId();
				statusTable[i]="ACTIVE-"+serviceBean.getPrivRepTable();
			}
		}
        
        
        agentRegistrationBean.setId(serviceList);
        agentRegistrationBean.setStatusTable(statusTable);
		OrgNUserRegHelper helper = new OrgNUserRegHelper();
		Map<String, String> errorMap = helper.createNewAgentOrgNUser(
				userInfoBean, agentRegistrationBean,"BO");
		
		System.out.println("*****errorMap" + errorMap);
		
		
		
		
		if (errorMap.containsKey("returnTypeError")) {
			if(errorMap.containsValue("User Name already exists !! !!")){
				agentRegistrationBean.setUserName(agentRegistrationBean.getUserName()+"_temp");
				logger.error(" USER Name Already Exists!!");
			} if (errorMap.containsValue("Id Number is Already exits !!")) {
				agentRegistrationBean.setIdNo(agentRegistrationBean.getIdNo()+"_1");
				logger.error("Id Number Already Exists!!");
			} if (errorMap.containsValue("Organization Name Already exits !!")) {
				logger.error("Organization  Already Exists!!");
				return status;
				//agentRegistrationBean.setOrgName(agentRegistrationBean.getOrgName()+"_1");
				
			}
			
			errorMap = helper.createNewAgentOrgNUser(
					userInfoBean, agentRegistrationBean,"BO");
			if (errorMap.containsKey("returnTypeError")) {
				return status;
			}
			
		}
		
				status=WrapperUtility.setPassword(agentRegistrationBean.getUserName(), userRegistrationBean.getPassword(),userRegistrationBean.getAutoPassword());
		if("FAILED".equalsIgnoreCase(status)){
			return status;
		}
		
		//inventoryUpload for at BO
		if(invDetBeanList !=null && invDetBeanList.size()>0){
		
			InventoryHelper.setInventoryDataToMap();
			
		for(int j=0;j<invDetBeanList.size();j++){
				LmsWrapperInventoryDetailBean invDetBean=invDetBeanList.get(j);
				if(invDetBean.getOrgId() == orgRegistrationBean.getAgtOrgId()){
					
					
					
					// upload inventory at BO
					
					ConsNNonConsInvHelper helperNonCons = new ConsNNonConsInvHelper();
					
					String nonConsModelId=InventoryHelper.modelNameMap.get(invDetBean.getNonConsModelName());
					String nonConsBrandId=InventoryHelper.brandNameMap.get(invDetBean.getNonConsBrandName());
					String nonConsInvId=InventoryHelper.inventoryNameMap.get(invDetBean.getNonConsInvName());
					
					//check duplicate terminal id
					List<String> dupSerialNoList=checkDuplicateTerminal(invDetBean.getSerNo(),nonConsModelId);
					
					//change status from REMOVED TO BO
					if(dupSerialNoList.size()>0){
					status=WrapperUtility.changeStatusFromRemovedToBo(dupSerialNoList,nonConsModelId);
					if("FAILED".equalsIgnoreCase(status)){
						return status;
					}
					}
					
					String[] serialNo=invDetBean.getSerNo();
					List<String> serialNoList=new LinkedList<String>(Arrays.asList(serialNo[0].split(",")));
					
				
					if(dupSerialNoList.size()>0){
						for(int k=0;k<dupSerialNoList.size();k++){
							serialNoList.remove(dupSerialNoList.get(k));
							//dupSerialNoList.remove(serialNoList.get(j));
						}
						
					}
					if(serialNoList.size()>0 && serialNoList !=null){
						String[] serialNoUpload=serialNoList.toArray(new String[serialNoList.size()]);
					ArrayList arr = helperNonCons.nonConsInvUpload(nonConsModelId+"-"+nonConsBrandId, invDetBean.getCost(), invDetBean.getIsNew(),
							null, serialNoUpload, userInfoBean.getUserId(), userInfoBean
									.getUserOrgId(), userInfoBean.getUserType());
					
					}

					// assign inventory to Agent
					
					int[] non_cons_inv_id=new int[1];
					non_cons_inv_id[0]=Integer.parseInt(nonConsInvId);
					
					int[] non_cons_model_id=new int[1];
					non_cons_model_id[0]=Integer.parseInt(nonConsModelId);
			
					int[] non_cons_brand_id=new int[1];
					non_cons_brand_id[0]=Integer.parseInt(nonConsBrandId);
					
					int[] consInvId =new int[1];
					consInvId[0]=-1;
					
					int[] consModelId=new int[1];
					consModelId[0]=-1;
					
					int[] consQty=new int[1];
					
					int agentOrgId=WrapperUtility.getAgentIdFromAgentName(orgRegistrationBean.getAgentName());
					
					int DNID = helperNonCons.assignConsNNonConsInv(
							userInfoBean.getUserOrgId(), userInfoBean.getUserId(), orgRegistrationBean.getOrgType(),
							agentOrgId, -1, non_cons_inv_id, non_cons_model_id,
							non_cons_brand_id, invDetBean.getSerNo(), consInvId, consModelId, null,
							userInfoBean.getUserType());
					
					
				}
			}
		}
		
		
		/*if (errorMap.containsKey("returnTypeError")) {
			//session.setAttribute("ORGANIZATION_NAME", this.getOrgName());
			//session.setAttribute("USER_NAME", this.getUserName());
			//return SUCCESS;
		} else {
			//this.errorMap = errorMap;
			logger.error("Organization and USER Name Already Exists!!");
			logger.debug("Organization and USER Name Already Exists!!");
			//return INPUT;
		}*/
		}
		catch (Exception e) {
			e.printStackTrace();
			return status;
		}
		status="SUCCESS";
		return status;
	}
	
	public Set<Integer> getEmailPrivListId(String privType) throws IOException {
		// get the email mailing privilege list from database
		
		OrganizationHelper orgReg = new OrganizationHelper();
		Map<Integer, String> reportTypeTitleMap = orgReg
				.getMailingReportTitle(privType);
		logger.debug("Inside getEmailPrivListId");
		logger.debug("email privilege list in orgnization creation == "
				+ reportTypeTitleMap);
		Set<Integer> emailPrivIdList=reportTypeTitleMap.keySet();
      return emailPrivIdList;
	}
	
	public List fetchService(String userType) {
		
		logger.info("********User Services Fatch**********");
		CountryOrgHelper country = new CountryOrgHelper();
		List serviceListBean=country.getAvlSerInterface(userType);
		
		
		return serviceListBean;
	}
	
	public String createRetailerOrgAtBO(LmsWrapperOrgRegShiftBean orgRegShiftBean){
		String status="FAILED";
		String terminalId=null;
		String modelName=null;
		 String sim[]=null;
		 String simModelName[]=null;
		UserInfoBean agtInfoBean = null;
		LmsWrapperOrgRegistrationBean orgRegistrationBean=orgRegShiftBean.getOrgRegistrationBean();
		LmsWrapperUserRegistrationBean userRegistrationBean=orgRegShiftBean.getUserRegistrationBean();
		List<LmsWrapperInventoryDetailBean> invDetBeanList=orgRegShiftBean.getInvDeatilBeanList();
		try{
		//set data to retailerRegistrationBean
		RetailerRegistrationBean retailerRegistrationBean=new RetailerRegistrationBean();
		retailerRegistrationBean.setAddrLine1(orgRegistrationBean.getAddrLine1());
		retailerRegistrationBean.setAddrLine2(orgRegistrationBean.getAddrLine2());
		retailerRegistrationBean.setAgtOrgId(orgRegistrationBean.getAgtOrgId());
		retailerRegistrationBean.setAppLimit((String)LMSUtility.sc.getAttribute("agtAppLimit"));
		retailerRegistrationBean.setAutoScrap(orgRegistrationBean.getAutoScrap());
		retailerRegistrationBean.setCity(orgRegistrationBean.getCity());
		retailerRegistrationBean.setCountry(orgRegistrationBean.getCountry());
		retailerRegistrationBean.setCreditLimit(orgRegistrationBean.getCreditLimit());
		retailerRegistrationBean.setEmail(userRegistrationBean.getEmail());
		retailerRegistrationBean.setIsRetailerOnline("NO");
		Set<Integer> emailPrivIdList=getEmailPrivListId(orgRegistrationBean.getOrgType());
        String[] emailPrivId=new String[emailPrivIdList.size()];
        int count=0;
        for(Integer emPId : emailPrivIdList){
            emailPrivId[count]=emPId.toString();
            count++;
        }
		retailerRegistrationBean.setEmailPrivId(emailPrivId);
		retailerRegistrationBean.setFirstName(userRegistrationBean.getFirstName());
		
		retailerRegistrationBean.setIdNo(userRegistrationBean.getIdNo());
		retailerRegistrationBean.setIdType(userRegistrationBean.getIdType());
		retailerRegistrationBean.setIsOffLine("NO");
		retailerRegistrationBean.setLastName(userRegistrationBean.getLastName());
		
		retailerRegistrationBean.setOlaDepositLimit((String)LMSUtility.sc.getAttribute("retDepositLimit"));
		retailerRegistrationBean.setOlaWithdrawalLimit((String)LMSUtility.sc.getAttribute("olaWithdrawalLimit"));
		retailerRegistrationBean.setOrgName(orgRegistrationBean.getOrgName());
		retailerRegistrationBean.setOrgType(orgRegistrationBean.getOrgType());
		retailerRegistrationBean.setPayLimit((String)LMSUtility.sc.getAttribute("agtPayLimit"));
		retailerRegistrationBean.setPhone(userRegistrationBean.getPhone());
		retailerRegistrationBean.setMobile(Long.parseLong(userRegistrationBean.getMobileNum()));
		retailerRegistrationBean.setPin(userRegistrationBean.getPin());
		retailerRegistrationBean.setPntId(userRegistrationBean.getPntId());
		retailerRegistrationBean.setReconReportType(orgRegistrationBean.getReconReportType());
		retailerRegistrationBean.setScrapLimit((String)LMSUtility.sc.getAttribute("agtScrapLimit"));
		retailerRegistrationBean.setSecAns(userRegistrationBean.getSecAns());
		retailerRegistrationBean.setSecQues(userRegistrationBean.getSecQues());
		retailerRegistrationBean.setSecurity(orgRegistrationBean.getSecurity());
		retailerRegistrationBean.setState(orgRegistrationBean.getState());
		retailerRegistrationBean.setStatus("ACTIVE");
		retailerRegistrationBean.setStatusorg(orgRegistrationBean.getStatusorg());
		retailerRegistrationBean.setMaxPerDayPayLimit(Double.parseDouble(((String)LMSUtility.sc.getAttribute("MAX_PER_DAY_PAY_LIMIT_FOR_RET"))));
		retailerRegistrationBean.setSelfClaim(Utility.getPropertyValue("SELF_CLAIM_RET"));
		retailerRegistrationBean.setOtherClaim(Utility.getPropertyValue("OTHER_CLAIM_RET"));
		retailerRegistrationBean.setMinClaimPerTicket(Double.parseDouble(Utility.getPropertyValue("MIN_CLAIM_PER_TICKET_RET")));
		retailerRegistrationBean.setMaxClaimPerTicket(Double.parseDouble(Utility.getPropertyValue("MAX_CLAIM_PER_TICKET_RET")));
		retailerRegistrationBean.setBlockAmt(Double.parseDouble(Utility.getPropertyValue("BLOCK_AMT")));
		retailerRegistrationBean.setBlockDays(Integer.parseInt(Utility.getPropertyValue("BLOCK_DAYS")));
		retailerRegistrationBean.setBlockAction(Utility.getPropertyValue("BLOCK_ACTION"));
		 List serviceListBean=fetchService(orgRegistrationBean.getOrgType());
	        int[] serviceList=null;
	        String[] statusTable=null;
	        if(serviceListBean != null){
	        	serviceList =new int[serviceListBean.size()];
				statusTable=new String[serviceListBean.size()];
				for(int i=0;i< serviceListBean.size();i++){
					AvailableServiceBean  serviceBean=(AvailableServiceBean) serviceListBean.get(i);
					
					serviceList[i]=serviceBean.getMappingId();
					statusTable[i]="ACTIVE-"+serviceBean.getPrivRepTable();
				}
			}
	        
	        
		retailerRegistrationBean.setId(serviceList);
		retailerRegistrationBean.setStatusTable(statusTable);
		
		
		retailerRegistrationBean.setUserName(userRegistrationBean.getUserName());
		retailerRegistrationBean.setVatRegNo(orgRegistrationBean.getVatRegNo());
		retailerRegistrationBean.setVerLimit((String)LMSUtility.sc.getAttribute("agtVerLimit"));
	
		if("YES".equalsIgnoreCase(Utility.getPropertyValue("sim_binding"))){
			sim =orgRegistrationBean.getSim();
			simModelName=orgRegistrationBean.getSimModelName();
			if(isValidSim(sim,simModelName)){
				retailerRegistrationBean.setSim(sim);
				retailerRegistrationBean.setSimModelName(simModelName);
			}else{
		
				logger.debug("Invalid Sim Input");
				return status;
			}
			
		
		}
		if(invDetBeanList !=null){
			for(int j=0;j<invDetBeanList.size();j++){
				LmsWrapperInventoryDetailBean invDetBean=invDetBeanList.get(j);
				if(invDetBean.getOrgId() == orgRegistrationBean.getOrgId()){
					terminalId=invDetBean.getSerNo()[0];
					modelName=invDetBean.getNonConsModelName();
				}
			}
		}
		retailerRegistrationBean.setTerminalId(terminalId);
		retailerRegistrationBean.setModelName(modelName);
		
		Map<String, String> errorMap = null;
		OrgNUserRegHelper helper = new OrgNUserRegHelper();
		logger.info("---------------Check BAL");
		System.out.println("---------------Check BAL");
		// get agent Org id from agent name
		int agentOrgId=WrapperUtility.getAgentIdFromAgentName(orgRegistrationBean.getAgentName());
		String errMsg = CommonMethods.chkCreditLimitAgt(agentOrgId, orgRegistrationBean.getCreditLimit());

		if (!"TRUE".equals(errMsg)) {
			errorMap  = new HashMap<String, String>();
			errorMap.put("orgError", errMsg);
			//this.errorMap = errorMap;
			//return INPUT;
		}

		// create agent info bean
		agtInfoBean = helper.createAgtBean(agentOrgId);
		System.out
				.println( "*******verLimit***" + orgRegistrationBean.getVerLimit());
		errorMap = helper.createNewRetailerOrgNUser(agtInfoBean, retailerRegistrationBean,"BO");
		System.out.println("*****errorMap" + errorMap);
		
		
		
		
		if (errorMap.containsKey("returnTypeError")) {
			if(errorMap.containsValue("User Name already exists !! !!")){
				retailerRegistrationBean.setUserName(retailerRegistrationBean.getUserName()+"_temp");
				logger.error(" USER Name Already Exists!!");
			} if (errorMap.containsValue("Id Number is Already exits !!")) {
				retailerRegistrationBean.setIdNo(retailerRegistrationBean.getIdNo()+"_1");
				logger.error("Id Number Already Exists!!");
			} if (errorMap.containsValue("Organization Name Already exits !!")) {
				retailerRegistrationBean.setOrgName(retailerRegistrationBean.getOrgName()+"_1");
				logger.error("Organization  Already Exists!!");
			}
			
			errorMap = helper.createNewRetailerOrgNUser(agtInfoBean, retailerRegistrationBean,"BO");

			if (errorMap.containsKey("returnTypeError")) {
				return status;
			}
			
		}
		
		// update password of retailer
					status=WrapperUtility.setPassword(retailerRegistrationBean.getUserName(), userRegistrationBean.getPassword(),userRegistrationBean.getAutoPassword());
					if("FAILED".equalsIgnoreCase(status)){
						return status;
					}
	/*	System.out.println("*****errorMap" + errorMap);
		
		if(errorMap.containsValue("User Name already exists !! !!")){
			retailerRegistrationBean.setUserName(retailerRegistrationBean.getUserName()+"_1");
			errorMap = helper.createNewRetailerOrgNUser(agtInfoBean, retailerRegistrationBean,"BO");
			// update password of retailer
			status=WrapperUtility.setPassword(retailerRegistrationBean.getUserName(), userRegistrationBean.getPassword(),userRegistrationBean.getAutoPassword());
			if("FAILED".equalsIgnoreCase(status)){
				return status;
			}
		}else{
			// update password of retailer
			status=WrapperUtility.setPassword(retailerRegistrationBean.getUserName(), userRegistrationBean.getPassword(),userRegistrationBean.getAutoPassword());
			if("FAILED".equalsIgnoreCase(status)){
				return status;
			}
		}
		 */
		
		if (!errorMap.containsKey("returnTypeError")) {
			if (terminalId != null) {
				helper.assignInventoryToRetailer(agtInfoBean,retailerRegistrationBean);
			}

		//	session.setAttribute("ORGANIZATION_NAME", this.getOrgName());
			//session.setAttribute("USER_NAME", this.getUserName().toLowerCase());
			//session.setAttribute("RETAILER_PASSWORD", errorMap
					//.get("NewPassword"));
			//return SUCCESS;
		} else {
		//	this.errorMap = errorMap;
			/*logger.error("Organization and USER Name Already Exists!!");
			System.out.println("Organization and USER Name Already Exists!!");*/
			//return INPUT;
		}
		}	
     catch (Exception e) {
         return status;
  }
     status="SUCCESS";
     return status;
	}


	
	public LmsWrapperRetailerListBean getRetailerListBean(String agentUserId) {
		LmsWrapperRetailerListBean retailerListBean=new LmsWrapperRetailerListBean();
		List<LmsWrapperRetailerInfoBean> retailerInfoList=new ArrayList<LmsWrapperRetailerInfoBean>();
		Connection con=DBConnect.getConnection();
		try {
			//String getRetailerQry="select name,user_id,om.organization_id,(available_credit-claimable_bal) balance,organization_status from st_lms_organization_master om,st_lms_user_master um where om.organization_type='RETAILER' and um.organization_id=om.organization_id and um.parent_user_id = "+agentUserId+" group by om.organization_id";
			String getRetailerQry =  "select name,a.user_id,a.organization_id, balance,organization_status  from (select name,user_id,om.organization_id,(available_credit-claimable_bal) balance,organization_status from st_lms_organization_master om,st_lms_user_master um where om.organization_type='RETAILER' and um.organization_id=om.organization_id and um.parent_user_id in (select user_id from st_lms_user_master where organization_id in (select organization_id  from st_lms_user_master where user_id  =  "+agentUserId+"))  group by om.organization_id) a inner join st_lms_ret_offline_master b on a.user_id =  b.user_id and serial_number <> '-1'";
			Statement stmt=con.createStatement();
			System.out.println("Get Retailer Qry From Agent UserId::"+getRetailerQry);
			ResultSet rs=stmt.executeQuery(getRetailerQry);
			while(rs.next()){
				LmsWrapperRetailerInfoBean retailerInfoBean=new LmsWrapperRetailerInfoBean();
				retailerInfoBean.setRetailerName(rs.getString("name"));
				retailerInfoBean.setRetailerOrgId(rs.getInt("organization_id"));
				retailerInfoBean.setBalance(rs.getDouble("balance"));
				retailerInfoBean.setUserId(rs.getInt("user_id"));
				retailerInfoBean.setStatus(rs.getString("organization_status"));
				retailerInfoList.add(retailerInfoBean);
			}
			retailerListBean.setRetailerInfoBean(retailerInfoList);
		} catch (SQLException e) {
			e.printStackTrace();
			retailerListBean.setSuccess(false);
			retailerListBean.setErrorCode("500");
			return retailerListBean;
		}finally{
			try{
				con.close();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		retailerListBean.setSuccess(true);
		retailerListBean.setErrorCode("100");
		return retailerListBean;
	}
	
	
	
	public String  removeStatusOfAgentRetailer(String agentOrgId, String requestIp){
		
		
		String status="FAILED";
		Connection con=DBConnect.getConnection();
		UserInfoBean userInfo =getUserData();
		String dateAppender = new SimpleDateFormat("ddMMyyyy").format(Util.getCurrentTimeStamp());
		if("FAILED".equalsIgnoreCase(userInfo.getStatus())){
			return status;
		}
		try {
		
		
		// return terminal from retailer to agent
		status=agtNonConsInvReturn(agentOrgId,con);
		if("FAILED".equalsIgnoreCase(status)){
			return status;
		}
		
		SetResetUserPasswordHelper.logOutAllRetsOfAgent(Integer.parseInt(agentOrgId));
		
		
		//list of terminals at agent
		List<String> serialNoList=WrapperUtility.getSerialNoFromAgentOrgId(agentOrgId);
		
		
		//return terminal from agent to BO
		status=consNonConsReturnInvSave(agentOrgId,con,userInfo);
		
		if("FAILED".equalsIgnoreCase(status)){
			return status;
		}
		//remove terminal from BO
		
		String removeTerminalQry="update st_lms_inv_status set current_owner_type='REMOVED'  where serial_no in ('"+serialNoList.toString().substring(1, serialNoList.toString().length()-1).replaceAll(",","','").replaceAll(" ","")+"')";
	
		System.out.println("REMOVE TERMINAL FROM BO::"+removeTerminalQry);
			Statement stmt=con.createStatement();
			stmt.executeUpdate(removeTerminalQry);

			//	Update Organization and User Status to BLOCK
			int orgId = 0;
			int userId = 0;
			String orgStatus = null;
			String userStatus = null;

			HistoryBean historyBean = new HistoryBean();
			historyBean.setDoneByUserId(userInfo.getUserId());
			historyBean.setComments("BLOCKED FOR AGENT SHIFTING");
			historyBean.setRequestIp(requestIp);
			historyBean.setUpdatedValue("BLOCK");
			historyBean.setDateAppender(dateAppender);
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT slom.organization_id, organization_status, user_id, status FROM st_lms_organization_master slom INNER JOIN st_lms_user_master slum ON slom.organization_id=slum.organization_id WHERE isrolehead = 'Y' and  (slom.organization_id="+agentOrgId+" OR parent_id="+agentOrgId+");");
			while(rs.next()) {
				orgId = rs.getInt("organization_id");
				userId = rs.getInt("user_id");
				orgStatus = rs.getString("organization_status");
				userStatus = rs.getString("status");

				if("ACTIVE".equals(orgStatus) || "INACTIVE".equals(orgStatus)) {
					historyBean.setOrganizationId(orgId);
					historyBean.setChangeType("ORGANIZATION_STATUS");
					historyBean.setChangeValue(orgStatus);
					CommonMethods.insertUpdateOrganizationHistoryForAGTShift(historyBean, con);
				}

				if("ACTIVE".equals(userStatus) || "INACTIVE".equals(userStatus)) {
					historyBean.setOrganizationId(userId);
					historyBean.setChangeType("USER_STATUS");
					historyBean.setChangeValue(userStatus);
					CommonMethods.insertUpdateUserHistoryForAGTShift(historyBean, con);
				}
			}

			/*
			String terminateOrgStatus="update st_lms_organization_master set organization_status='TERMINATE' where (organization_id= "+agentOrgId+" or parent_id= "+agentOrgId+" )";
	        stmt.executeUpdate(terminateOrgStatus);
	        System.out.println("Update Organization status of agent and retailer::"+terminateOrgStatus);
	      
	        PreparedStatement st = con.prepareStatement("update st_lms_user_master user,(select organization_id,parent_id from st_lms_organization_master where parent_id=?) org set status=?,termination_date=? where (user.organization_id=org.organization_id or org.parent_id=user.organization_id)");
	        st.setInt(1, Integer.parseInt(agentOrgId));
	        st.setString(2, "TERMINATE");
			st.setTimestamp(3, new Timestamp(new Date().getTime()));
			st.executeUpdate();
			
			System.out
					.println("REMOVE TERMINAL FROM AGENT SUCCESSFULLY");
			*/
		} catch (SQLException e) {
			e.printStackTrace();
			return status;
		} catch (LMSException e) {
			e.printStackTrace();
			return status;
		}
		finally{
			try{
				con.close();
			}catch (Exception e) {
				// TODO: handle exception
			}
		}
		status="SUCCESS";
		return status;
	 }
	
	public String agtNonConsInvReturn(String agentOrgId,Connection con){
		String status="FAILED";
		String userType="";
		int agtUserId=-1;
		//terminal-EFT930G-123456
		String fetchAgentQry="select user_id,organization_type from st_lms_user_master where organization_id='"+agentOrgId+"' and isrolehead='Y'";
		String fetchRetailerTerminalQry="select inv_model_id,serial_no,current_owner_id,current_owner_type from st_lms_inv_status where current_owner_type='RETAILER' and current_owner_id in(select organization_id from st_lms_organization_master where parent_id= "+agentOrgId+" )";
		try{
			
		
		Statement stmt=con.createStatement();
		ResultSet rs=stmt.executeQuery(fetchAgentQry);
		if(rs.next()){
			agtUserId=rs.getInt("user_id");
			userType=rs.getString("organization_type");
		}
		
		rs=stmt.executeQuery(fetchRetailerTerminalQry);
		
			while(rs.next()){
				int retOrgId=rs.getInt("current_owner_id");
				String retModelId=rs.getInt("inv_model_id")+"-"+rs.getString("serial_no");
				ConsNNonConsInvHelper helper = new ConsNNonConsInvHelper();
				status = helper.agtNonConsInvReturnSave(retOrgId, rs.getString("serial_no"),Integer.parseInt(agentOrgId),retModelId, userType,agtUserId);
			}
		
		}
		catch (Exception e) {
			return status;
			// TODO: handle exception
		}
		status="SUCCESS";
		return status;
	}
	
	
	public String consNonConsReturnInvSave(String agentOrgId,Connection con,UserInfoBean userInfo)  {
            String status="FAILED";
            ResultSet rs=null;
		String fetchAgentTerminalQry="select inv_model_id,serial_no,brand_id,inv_id,current_owner_type  from (select inv_model_id,serial_no,current_owner_type from st_lms_inv_status where current_owner_id= "+agentOrgId+" ) aaa inner join st_lms_inv_model_master mm on aaa.inv_model_id=mm.model_id order by inv_model_id asc";
			System.out.println("FETCH AGENT TERMINAL SHIFT TO BO::"+fetchAgentTerminalQry);
			HashMap<String,ArrayList<String>> invDetMap=new HashMap<String, ArrayList<String>>();
			String ownerType="";
			int count=0;
			try{
		
			Statement stmt=con.createStatement();
			 rs=stmt.executeQuery(fetchAgentTerminalQry);
			
				while(rs.next()){
					ownerType=rs.getString("current_owner_type");
					 
						ArrayList<String> serNoList=new ArrayList<String>();
						if(invDetMap.get(rs.getString("inv_model_id")+"-"+rs.getString("brand_id")+"-"+rs.getString("inv_id")) != null){
							invDetMap.get(rs.getString("inv_model_id")+"-"+rs.getString("brand_id")+"-"+rs.getString("inv_id")).add(rs.getString("serial_no"));
						}else{
							
							serNoList.add(rs.getString("serial_no"));
						    invDetMap.put(rs.getString("inv_model_id")+"-"+rs.getString("brand_id")+"-"+rs.getString("inv_id"), serNoList);
						   System.out.println("Inventor Detail Map for OrgId"+agentOrgId+":::"+invDetMap);
						
					}
				}
			
					Set<String> invItr=invDetMap.keySet();
					
					int[] non_cons_inv_id=new int[invItr.size()];
					int[] non_cons_model_id=new int[invItr.size()];
					int[] non_cons_brand_id=new int[invItr.size()];
					int[] consModelId=new int[1];
					int[] consInvId =new int[1];
					String[] serNo =new String[invItr.size()];
					
					for(String modelinv: invItr){
						
						non_cons_model_id[count]=Integer.parseInt(modelinv.split("-")[0]);
						non_cons_brand_id[count]=Integer.parseInt(modelinv.split("-")[1]);
						non_cons_inv_id[count]=Integer.parseInt(modelinv.split("-")[2]);
												
						List<String> serNoList=new ArrayList<String>();
						serNoList=invDetMap.get(modelinv);
						serNo[count] = serNoList.toString().substring(1, serNoList.toString().length()-1).replaceAll(" ", "") ;
						
						
						consInvId[0]=-1;
						
		
						consModelId[0]=-1;
						count ++;
						
					}
					ConsNNonConsInvHelper helper = new ConsNNonConsInvHelper();
					int DNID = helper.returnConsNNonConsInv(
							userInfo.getUserOrgId(), userInfo.getUserId(), ownerType,
							Integer.parseInt(agentOrgId), -1, non_cons_inv_id, non_cons_model_id,
							non_cons_brand_id, serNo, consInvId, consModelId, null,
							userInfo.getUserType());
					
			}
			catch (Exception e) {
				return status;
			}
			status="SUCCESS";
			return status;
				}

	
	
	public List<String> checkDuplicateTerminal(String[] serialNo, String modelId){
		
		List<String> SerList=Arrays.asList(serialNo);
		List<String> duplicateSerialNoList=new ArrayList<String>();
		System.out.println(SerList);
		System.out.println("CHECK TERMINAL::"+SerList+"::modelId::"+modelId);
		Connection con =DBConnect.getConnection();
		try {
			String chkDulicateInvQry="select inv_model_id,serial_no from st_lms_inv_status where serial_no in ('"+SerList.toString().substring(1, SerList.toString().length()-1).replaceAll(",","','").replaceAll(" ","")+"') and current_owner_type in ('REMOVED') and inv_model_id='"+modelId+"';";
			Statement stmt=con.createStatement();
			System.out.println("Check Duplicate Terminal::"+chkDulicateInvQry);
		ResultSet rs=stmt.executeQuery(chkDulicateInvQry);
		
			while(rs.next()){
				duplicateSerialNoList.add(rs.getString("serial_no"));
			}
		
		
		} catch (SQLException e) {
		
			e.printStackTrace();
		}finally{
			try{
				con.close();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	System.out.println("DUPLICAT SERIAL NO::"+duplicateSerialNoList);
	return duplicateSerialNoList;
	}
	
	
	public String checkRetailerLoginPlace(String userName, String terminalId) throws LMSException
	{
		String Location="false";
		Connection con = null;
		con = DBConnect.getConnection();
		try {
			String query="select current_owner_type from st_lms_inv_status where serial_no like'%"+terminalId+"' and current_owner_type != 'REMOVED'";
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			if(rs.next())
			{
				System.out.println(rs.getString(1));
				Location="true";
			}
			else
			{
				Location="false";
			}
		}
		catch(NullPointerException ex)
		{
			System.out.println("NullPointerException:...");
			ex.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			try {
				if (con != null && !con.isClosed()) {
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
				throw new LMSException(e);
			}
		}
		System.out.println(Location);
		return Location;
	}
	
	public String removeConsNNonConsInv(LmsWrapperInventoryAssignDataBean invAssignDataBean){
		String status="FAILED";
		InventoryHelper.setInventoryDataToMap();
		
		 String[] nonConsInvId=invAssignDataBean.getNonConsInvId();
		 String[] nonConsModelId=invAssignDataBean.getNonConsModelId();
		 String[] nonConsBrandId=invAssignDataBean.getNonConsBrandId();
		 String[] consInvId=invAssignDataBean.getConsInvId();
	     String[] consModelId=invAssignDataBean.getConsModelId();
	   
		int[] nonConsumableInvId=new int[nonConsInvId.length];
		for(int i=0;i<nonConsInvId.length;i++){
			if(nonConsInvId[i] != null){
			nonConsumableInvId[i]=Integer.parseInt(InventoryHelper.inventoryNameMap.get(nonConsInvId[i]));
			}
		}
		
		int[] nonConsumableModelId=new int[nonConsModelId.length];
		for(int i=0;i<nonConsModelId.length;i++){
			if(nonConsModelId[i] != null){
			nonConsumableModelId[i]=Integer.parseInt(InventoryHelper.modelNameMap.get(nonConsModelId[i]));
			}
		}
		
		int[] nonConsumableBrandId=new int[nonConsBrandId.length];
		for(int i=0;i<nonConsBrandId.length;i++){
			if(nonConsBrandId[i] != null){
			nonConsumableBrandId[i]=Integer.parseInt(InventoryHelper.brandNameMap.get(nonConsBrandId[i]));
			}
		}
		
		/*int[] consumableInvId=new int[consInvId.length];
		for(int i=0;i<consInvId.length;i++){
			consumableInvId[i]=Integer.parseInt(InventoryHelper.consInventoryNameMap.get(consInvId[i]));
		}
		
		int[] consumableModelId=new int[consModelId.length];
		for(int i=0;i<consModelId.length;i++){
			consumableModelId[i]=Integer.parseInt(InventoryHelper.consModelIdMap.get(consModelId[i]));
		}
		*/
		UserInfoBean userInfo = getUserData();
		if("FAILED".equalsIgnoreCase(userInfo.getStatus())){
			return status;
		}
		
		ConsNNonConsInvHelper invHelper = new ConsNNonConsInvHelper();
		Connection conn = DBConnect.getConnection();
		try {
			
			// return non consumable inventory
			String nonConsInvRes = removeNonConsInv(userInfo.getUserOrgId(),
					nonConsumableInvId, nonConsumableModelId, nonConsumableBrandId, invAssignDataBean.getSerNo(), conn);
			// assign consumable inventory
			/*String consInvRes = verifyAssignConsInv(userOrgId, consInvId,
					consModelId, consQty, conn);*/

			
			
		} catch (SQLException e) {
			e.printStackTrace();
			
		} catch (LMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
				
			}
		}
      status="SUCCESS";
	return status;
	}
	
	public String removeNonConsInv(int userOrgId, int[] nonConsInvId,
			int[] nonConsModelId, int[] nonConsBrandId, String[] serNo,
			Connection conn) throws SQLException,LMSException{
		String status="FAILED";
		logger.debug("removeNonConsInv called");
		List<String> validSerNoList = new ArrayList<String>();
		
		String assignSerNoQuery = null;
		Statement assignSerNoPstmt = conn.createStatement();
		ResultSet rs = null;

		String[] serNoArr = null;
		List<String> dbEntries = new ArrayList<String>();

		if (nonConsModelId != null) {
			for (int i = 0, len = nonConsModelId.length; i < len; i = i + 1) {
				if (nonConsInvId[i] > 0 && nonConsModelId[i] > 0
						&& nonConsBrandId[i] > 0 && !"".equals(serNo[i].trim())) {

					assignSerNoQuery = "select serial_no from st_lms_inv_status  where inv_model_id = "
							+ nonConsModelId[i]
							+ " and serial_no in ( "
							+ "'"
							+ serNo[i].replace(",", "','").toUpperCase()
							+ "'"
							+ " ) and  current_owner_type !='REMOVED' and current_owner_id = " + userOrgId;
					logger.debug("fetch nonCons list1 Pstmt = "
							+ assignSerNoQuery);
					rs = assignSerNoPstmt.executeQuery(assignSerNoQuery);
					dbEntries.clear();
					while (rs.next()) {
						dbEntries.add(rs.getString("serial_no"));
					}

					serNoArr = serNo[i].split(",");
					for (int k = 0, klen = serNoArr.length; k < klen; k = k + 1) {
						if (!"".equals(serNoArr[k].trim())) {
							if (dbEntries.contains(serNoArr[k].trim()
									.toUpperCase())) {
								validSerNoList.add(serNoArr[k].trim());
							}
						}
					}

				} else {
					// values are not in valid format
					logger.debug("inside else ==== " + nonConsInvId[i] + " == "
							+ nonConsModelId[i] + " === " + nonConsBrandId[i]
							+ "==" + serNo[i].trim());
				}
				
				status=WrapperUtility.changeStatusFromBoToRemoved(validSerNoList, nonConsModelId[i], conn);
				
			}
		}

		logger.debug("valid list = " + validSerNoList);
				
		return status;
	}
	
	public  boolean isValidSim(String sim[],String simModelName[]){
		try{
			Set<String> modelSet = new HashSet<String>();
			boolean oneSelected =false;
			if(simModelName!=null){
				
				for(String simModel :simModelName){
					
					if(simModel!=null&&!"-1".equalsIgnoreCase(simModel.trim())){
						if(!modelSet.add(simModel)){
							logger.info("Cannot Assign Multiple Sim of Same Model !!");
							return false;
						}else{
							
							oneSelected=true;
						}
					}
					
				}
				if(!oneSelected){
					
					logger.info("Please Select Atleast One Sim");
					return false;
				}
				
			}else{
				
				return false;
			}
			oneSelected=false;
			if(sim!=null){
				
				for(String simSrNo :sim){
					
					if(simSrNo!=null&&!"".equalsIgnoreCase(simSrNo.trim())){
						oneSelected=true;
					}
					
				}
						
				if(!oneSelected){
					
					logger.info("Please provide Atleast One Sim Serial Number ");
					return false;
				}
			}else{
				
				return false;
			}
			
			return true;
			
		}catch(Exception e){
			
			logger.error("Exception invalid Sim Input",e);
			
		}
		
		return false;
	}	
	
	public List<LmsWrapperVerifyBeforeShiftBean> verifyBeforeShift(LmsWrapperVerifyBeforeShiftReq lmsWrapperVerifyBeforeShiftReq) throws
				LMSException{

		Connection con = null;
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		Set<String> duplicateInv = null;
		Set<String> outerDuplicateInv = null;
		List<LmsWrapperVerifyBeforeShiftBean> orgList = null;;

		try {
			StringBuilder query = new StringBuilder("select sum(INV_ID) INV_ID , sum(USER_NAME) USER_NAME, sum(ORGANIZATION_NAME) ORGANIZATION_NAME , sum(ORGANIZATION_CODE) ORGANIZATION_CODE, sum(ID_NO_ID_TYPE) ID_NO_ID_TYPE from  (")
				.append("select count(*) 'INV_ID' , 0 'USER_NAME' , 0 'ORGANIZATION_NAME', 0 'ORGANIZATION_CODE',0 'ID_NO_ID_TYPE'  from st_lms_inv_status where serial_no = ? and inv_model_id = ?   and current_owner_type <> 'REMOVED' union all ") 
				.append("select  0 'INV_ID',count(*) 'USER_NAME' ,0 'ORGANIZATION_NAME',0 'ORGANIZATION_CODE',0 'ID_NO_ID_TYPE' from st_lms_user_master where user_name = ? union all ") 
				.append("select  0 'INV_ID', 0 'USER_NAME' ,count(*) 'ORGANIZATION_NAME'  ,0 'ORGANIZATION_CODE',0 'ID_NO_ID_TYPE' from st_lms_organization_master where name = ? union all ")
				.append("select  0 'INV_ID', 0 'USER_NAME' , 0 ORGANIZATION_NAME,count(*) 'ORGANIZATION_CODE',0 'ID_NO_ID_TYPE' from st_lms_organization_master where org_code = ? union all ")
				.append("select  0 'INV_ID', 0 'USER_NAME' , 0 ORGANIZATION_NAME,0 'ORGANIZATION_CODE' , count(*) 'ID_NO_ID_TYPE' from st_lms_user_contact_details where id_type = ? and id_nbr = ?	) a");
			
			con = DBConnect.getConnection();
			pstmt = con.prepareStatement(query.toString());
			
			Iterator<LmsWrapperVerifyOrgBean> beanIterator = lmsWrapperVerifyBeforeShiftReq.getOrgListToValidate().iterator();
			orgList = new ArrayList<LmsWrapperVerifyBeforeShiftBean>();
			outerDuplicateInv =  new HashSet<String>();
			while (beanIterator.hasNext()) {
				
				LmsWrapperVerifyOrgBean lmsWrapperVerifyOrgBean = beanIterator.next();
				
				List<OrgInvDetails> orgInvList = lmsWrapperVerifyOrgBean.getOrgInvList();
				
				pstmt.setString(3, lmsWrapperVerifyOrgBean.getUserName());
				pstmt.setString(4, lmsWrapperVerifyOrgBean.getOrgName());
				pstmt.setString(5, lmsWrapperVerifyOrgBean.getOrgCode());
				pstmt.setString(6, lmsWrapperVerifyOrgBean.getIdType());
				pstmt.setString(7, lmsWrapperVerifyOrgBean.getIdNumber());
				
				Iterator<OrgInvDetails> it = orgInvList.iterator();
				duplicateInv = new HashSet<String>();
				while(it.hasNext()){
					OrgInvDetails tempBean = it.next();
					pstmt.setString(2, tempBean.getInvModelId());
					
					String[] serialNos = tempBean.getSerialNo().split(",");
					 for(int i = 0 ; i < serialNos.length ; i++){
						 String serialId = serialNos[i].trim();
						 pstmt.setString(1, serialId);
						 rs = pstmt.executeQuery();
						 rs.next();
						 if(rs.getBoolean("INV_ID") && outerDuplicateInv.add(serialId)){
							 duplicateInv.add(serialId);
						 }
					 }
				}
				
				LmsWrapperVerifyBeforeShiftBean orgResBean =  new LmsWrapperVerifyBeforeShiftBean();
				orgResBean.setOrgName(lmsWrapperVerifyOrgBean.getOrgName());

				HashMap <String, String> errorMap = new HashMap<String, String>();
				errorMap.put("INV_ID", (duplicateInv.size()>0)?duplicateInv.toString().replace("[", "").replace("]", ""):"OK");
				errorMap.put("USER_NAME", (rs.getBoolean("USER_NAME"))?"DUPLICATE":"OK");
				errorMap.put("ORGANIZATION_NAME", (rs.getBoolean("ORGANIZATION_NAME"))?"DUPLICATE":"OK");
				errorMap.put("ORGANIZATION_CODE", (rs.getBoolean("ORGANIZATION_CODE"))?"DUPLICATE":"OK");
				errorMap.put("ID_NO_ID_TYPE", (rs.getBoolean("ID_NO_ID_TYPE"))?"DUPLICATE":"OK");
				orgResBean.setErrorMap(errorMap);
				orgList.add(orgResBean);
				pstmt.clearParameters();
			}
		}catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE , LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE , LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}finally{
			DBConnect.closeConnection(con, pstmt, rs);
		}
		return orgList;
	}
}
