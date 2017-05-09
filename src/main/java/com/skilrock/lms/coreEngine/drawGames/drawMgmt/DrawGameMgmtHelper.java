package com.skilrock.lms.coreEngine.drawGames.drawMgmt;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import net.sf.json.JSONObject;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.opensymphony.xwork2.util.LocalizedTextUtil;
import com.skilrock.lms.admin.SetResetUserPasswordAction;
import com.skilrock.lms.ajax.AjaxRequestHelper;
import com.skilrock.lms.beans.DailyLedgerBean;
import com.skilrock.lms.beans.PromoGameBean;
import com.skilrock.lms.beans.RankWiseWinningReportBean;
import com.skilrock.lms.beans.ReportStatusBean;
import com.skilrock.lms.beans.ServiceRequest;
import com.skilrock.lms.beans.ServiceResponse;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.CommonValidation;
import com.skilrock.lms.common.ConfigurationVariables;
import com.skilrock.lms.common.Utility;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.DBConnectReplica;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;
import com.skilrock.lms.common.utility.CommonMethods;
import com.skilrock.lms.common.utility.LMSUtility;
import com.skilrock.lms.common.utility.MailSender;
import com.skilrock.lms.common.utility.MailSenderAfterPerformDraw;
import com.skilrock.lms.common.utility.ResponsibleGaming;
import com.skilrock.lms.common.utility.orgOnLineSaleCreditUpdation;
import com.skilrock.lms.coreEngine.drawGames.common.DGPromoScheme;
import com.skilrock.lms.coreEngine.drawGames.playMgmt.DrawGameRPOSHelper;
import com.skilrock.lms.coreEngine.service.IServiceDelegate;
import com.skilrock.lms.coreEngine.service.ServiceDelegate;
import com.skilrock.lms.coreEngine.service.dge.ServiceMethodName;
import com.skilrock.lms.coreEngine.service.dge.ServiceName;
import com.skilrock.lms.coreEngine.userMgmt.common.CommonFunctionsHelper;
import com.skilrock.lms.dge.beans.AnalysisBean;
import com.skilrock.lms.dge.beans.AnalysisReportDrawBean;
import com.skilrock.lms.dge.beans.BlockTicketUserBean;
import com.skilrock.lms.dge.beans.CancelTicketBean;
import com.skilrock.lms.dge.beans.DGConsolidateDrawBean;
import com.skilrock.lms.dge.beans.DGConsolidateGameDataBean;
import com.skilrock.lms.dge.beans.DrawDataBean;
import com.skilrock.lms.dge.beans.DrawDetailsBean;
import com.skilrock.lms.dge.beans.DrawIdBean;
import com.skilrock.lms.dge.beans.DrawPanelSaleBean;
import com.skilrock.lms.dge.beans.DrawScheduleBean;
import com.skilrock.lms.dge.beans.DrawScheduleBeanResult;
import com.skilrock.lms.dge.beans.ManualWinningBean;
import com.skilrock.lms.dge.beans.MtnCustomerCenterBean;
import com.skilrock.lms.dge.beans.PanelIdBean;
import com.skilrock.lms.dge.beans.ReportBeanDrawModule;
import com.skilrock.lms.dge.beans.ReportDrawBean;
import com.skilrock.lms.dge.beans.ResultSubmitBean;
import com.skilrock.lms.dge.beans.SchedulerBean;
import com.skilrock.lms.dge.beans.TicketSearchCriteriaBean;
import com.skilrock.lms.dge.beans.TicketTracking;
import com.skilrock.lms.dge.beans.TicketWiseDataBean;
import com.skilrock.lms.dge.beans.ValidateTicketBean;
import com.skilrock.lms.dge.beans.BlockTicketUserBean.BlockTicketUserBeanBuilder;
import com.skilrock.lms.dge.gameconstants.BonusBalllottoConstants;
import com.skilrock.lms.dge.gameconstants.BonusBalltwoConstants;
import com.skilrock.lms.dge.gameconstants.FastlottoConstants;
import com.skilrock.lms.dge.gameconstants.KenoConstants;
import com.skilrock.lms.dge.gameconstants.KenoEightConstants;
import com.skilrock.lms.dge.gameconstants.KenoFourConstants;
import com.skilrock.lms.dge.gameconstants.KenoNineConstants;
import com.skilrock.lms.dge.gameconstants.KenoSevenConstants;
import com.skilrock.lms.dge.gameconstants.KenoSixConstants;
import com.skilrock.lms.dge.gameconstants.KenoTwoConstants;
import com.skilrock.lms.dge.gameconstants.LottoConstants;
import com.skilrock.lms.dge.gameconstants.PickFourConstants;
import com.skilrock.lms.dge.gameconstants.PickThreeConstants;
import com.skilrock.lms.dge.gameconstants.RainbowConstants;
import com.skilrock.lms.dge.gameconstants.SuperTwoConstants;
import com.skilrock.lms.dge.gameconstants.TanzanialottoConstants;
import com.skilrock.lms.dge.gameconstants.TenByTwentyConstants;
import com.skilrock.lms.dge.gameconstants.TwelveByTwentyFourConstants;
import com.skilrock.lms.dge.gameconstants.ZimLottoBonusConstants;
import com.skilrock.lms.dge.gameconstants.ZimLottoBonusFreeConstants;
import com.skilrock.lms.dge.gameconstants.ZimLottoBonusTwoConstants;
import com.skilrock.lms.dge.gameconstants.ZimLottoBonusTwoFreeConstants;
import com.skilrock.lms.dge.gameconstants.ZimlottoConstants;
import com.skilrock.lms.dge.gameconstants.ZimlottothreeConstants;
import com.skilrock.lms.dge.gameconstants.ZimlottotwoConstants;
import com.skilrock.lms.web.drawGames.common.DrawGameRPOS;
import com.skilrock.lms.web.drawGames.common.Util;
import com.skilrock.lms.web.reportsMgmt.common.ReportUtility;

/**
 * 
 * @author Gaurav Ujjwal
 * 
 * <pre>
 * Change History
 * Change Date     Changed By     Change Description
 * -----------     ----------     ------------------
 * (e.g.)
 * 01-JAN-2005     ABxxxxxx       CR#zzzzzz: blah blah blah... 
 * 28-MAY-2010     Arun Tanwar    CR#L0375:Implementation of winning numbers for manual entry(freezed draws).
 * 02-MAY-2010     Arun Tanwar    CR#L0375:Implementation of winning numbers for manual entry. Method getManualEntryData added.
 * 03-MAY-2010     Arun Tanwar    CR#L0375:Implementation of entering PMEP for ACTIVE draws. Method getManualDeclareData added.
 * </pre>
 */
public class DrawGameMgmtHelper extends LocalizedTextUtil{
	static Log logger = LogFactory.getLog(DrawGameMgmtHelper.class);
	private static Locale locale = Locale.getDefault();
		
	public String drawTime(String gameNo) {
		String finalList = null;
		ServiceResponse sRes = new ServiceResponse();
		ServiceRequest sReq = new ServiceRequest();
		sReq.setServiceName(ServiceName.DRAWGAME);
		sReq.setServiceMethod(ServiceMethodName.DRAW_GAME_ANALYSIS_REPORT);
		sReq.setServiceData(gameNo);
		IServiceDelegate delegate = ServiceDelegate.getInstance();
		sRes = delegate.getResponse(sReq);
		finalList = (String) sRes.getResponseData();
		System.out.println(finalList.length() + "***TIME LIST***"
				+ finalList);
		return finalList;
	}
	
	
	public SchedulerBean actionOnHold(DrawScheduleBean drawScheduleBean)
			throws Exception {
		ServiceResponse sRes = new ServiceResponse();
		ServiceRequest sReq = new ServiceRequest();
		sReq.setServiceName(ServiceName.DRAW_MGMT);
		sReq.setServiceMethod(ServiceMethodName.DRAWGAME_ACTION_ON_HOLD_DRAW);
		sReq.setServiceData(drawScheduleBean);
		IServiceDelegate delegate = ServiceDelegate.getInstance();
		sRes = delegate.getResponse(sReq);
		if (sRes.getIsSuccess()) {
			//rescheduleJob(drawScheduleBean.getGameNo());
		}
		String responseString=sRes.getResponseData().toString();
		Type elementType = new TypeToken<SchedulerBean>() {}.getType();
		SchedulerBean schedulerBean = new Gson().fromJson(responseString, elementType);
		return schedulerBean;
	}

	public boolean authorizeUser(int userId) {
		
		ResultSet rs = null;
		Statement stmt= null;
		Connection con = null;
		String fetchUser = null;

		try {
			con = DBConnect.getConnection();
			stmt = con.createStatement();
			fetchUser = "select user_id from st_lms_user_master where user_name='bomaster'";
			rs = stmt.executeQuery(fetchUser);
			if (rs.next()) 
				if (userId == rs.getInt("user_id")) 
					return true;
		}catch (SQLException e) {
		logger.error("SQL Exception  : - " + e);
		}catch (Exception e) {
			logger.error("General Exception  : - " + e);
		} finally {
			DBConnect.closeConnection(con, stmt, rs);
		}
		return false;
	}

	/////mmmmmmmmmmmmmmmmmmmmm
	
/*	@SuppressWarnings({ "finally", "unchecked", "unchecked", "unchecked", "unchecked" })
	public Map getDrawNames(String gameNo,String date)
	{
		Connection con = DBConnect.getConnection();
		Map drawNames=new HashMap();
		try {
			Statement stmt = con.createStatement();
			String fetchUser = "select draw_name from ge_draw_master_"
				+gameNo+
				"where draw_datetime like '%"
				+date+
				"%'";
			ResultSet rs = stmt.executeQuery(fetchUser);
			
			if (rs.next()) {
				drawNames.put(gameNo, rs.getString(1));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return drawNames;
		}
		

		
	}
	*/
	public ArrayList<CancelTicketBean> cancelBlkTicketAtBO(int cancelDuration ,boolean isCancel,
			String[] ticketNumArr, String[] reasons, UserInfoBean userInfoBO,
			String search_type, String gameNo, String refMerchantId,
			Map<Integer, Map<Integer, String>> drawIdTableMap,String cancellationCharges)
			throws LMSException {
		logger.debug("********cancelBlkTicketAtBO******"+ Arrays.asList(ticketNumArr) + "With the Search Type" + search_type);
		ArrayList<CancelTicketBean> canTktList = new ArrayList<CancelTicketBean>();
		int len = ticketNumArr.length;
		for (int i=0; i<len; i++) {
			logger.info("***ticketNum***" + ticketNumArr[i]);
			CancelTicketBean cancelTicketBean = new CancelTicketBean();
			cancelTicketBean.setDrawIdTableMap(drawIdTableMap);
			cancelTicketBean.setTicketNo(ticketNumArr[i]);
			cancelTicketBean.setCancelChannel("LMS_Web");
			cancelTicketBean.setRefMerchantId(refMerchantId);
			cancelTicketBean.setCancelType("LAST_SOLD_TICKET");
			cancelTicketBean.setCancelDuaraion(isCancel);
			cancelTicketBean.setCancelDuration(cancelDuration);
			cancelTicketBean.setReason(reasons[i]);
			// call cancelTicketAtBO
			cancelTicketBean = cancelTicketAtBO(cancelTicketBean, userInfoBO,search_type, gameNo,cancellationCharges);
			canTktList.add(cancelTicketBean);
		}

		return canTktList;
	}

	public SchedulerBean cancelDraw(DrawScheduleBean drawScheduleBean, UserInfoBean userInfoBean)
	throws Exception {
		ServiceResponse sRes = new ServiceResponse();
		ServiceRequest sReq = new ServiceRequest();
		sReq.setServiceName(ServiceName.DRAW_MGMT);
		sReq.setServiceMethod(ServiceMethodName.DRAWGAME_CANCEL_DRAW);
		sReq.setServiceData(drawScheduleBean);
		IServiceDelegate delegate = ServiceDelegate.getInstance();
		sRes = delegate.getResponse(sReq);
		Connection con = DBConnect.getConnection();
		String responseString=sRes.getResponseData().toString();
		Type elementType = new TypeToken<SchedulerBean>() {}.getType();
		SchedulerBean drawBean = new Gson().fromJson(responseString, elementType);
		if("cancelAll".equalsIgnoreCase(drawScheduleBean.getAction())){
			CancelTicketBean cancelBean;
			UserInfoBean userBean;
			DrawGameRPOSHelper helper = new DrawGameRPOSHelper();
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String refMerchantId = (String) LMSUtility.sc.getAttribute("REF_MERCHANT_ID");
			List<DrawScheduleBean> drawList = drawBean.getDrawScheduleList();
			for(DrawScheduleBean drwBean : drawList){
				ArrayList<String> tktList = drwBean.getTktList();
				if(!tktList.isEmpty()){
					String qry = "select sale.ticket_nbr, um.user_id, um.organization_id, um.organization_type, om.parent_id from st_dg_ret_sale_" + drawBean.getGameNo() 
					+ " sale inner join st_lms_organization_master om inner join st_lms_user_master um on sale.retailer_org_id = um.organization_id and um.organization_id = om.organization_id where sale.ticket_nbr in ("
					+ tktList.toString().replace("[", "").replace("]", "").replace(", ", ",") + ")";
					System.out.println("ticket cancel select query: "+ qry);
					pstmt = con.prepareStatement(qry);
					rs = pstmt.executeQuery();
					while(rs.next()){
						userBean = new UserInfoBean();
						cancelBean = new CancelTicketBean();
						String tktNo = rs.getString("ticket_nbr");
						userBean.setUserId(rs.getInt("user_id"));
						userBean.setUserOrgId(rs.getInt("organization_id"));
						userBean.setParentOrgId(rs.getInt("parent_id"));
						cancelBean.setTicketNo(tktNo + Util.getRpcAppenderForTickets(tktNo.length()));
						cancelBean.setGameNo(drawBean.getGameNo());
						cancelBean.setPartyId(userBean.getUserOrgId());
						cancelBean.setPartyType(rs.getString("organization_type"));
						cancelBean.setUserId(userBean.getUserId());
						cancelBean.setCancelChannel("DRAW_MGMT");
						cancelBean.setRefMerchantId(refMerchantId);
						cancelBean.setAutoCancel("CANCEL_SERVER");
						cancelBean.setAutoCancel(true);
						helper.cancelTicket(cancelBean, userBean, true,"CANCEL_SERVER");
					}
				}
			}
		}
		if (sRes.getIsSuccess()) {
		
			//rescheduleJob(drawScheduleBean.getGameNo());
			PreparedStatement ps = con.prepareStatement("insert into st_dg_draw_status_change_history (user_id, action, date, remarks) values (?,?,?,?)");
			ps.setInt(1, userInfoBean.getUserId());
			ps.setString(2, "CANCEL");
			ps.setString(3, Util.getCurrentTimeString());
			ps.setString(4, "Cancelled draws "+drawScheduleBean.getDrawIdList());
			ps.executeUpdate();
			new DrawGameRPOS().newData();
			new SetResetUserPasswordAction().logOutAllRets();
			
			String drawStatus =drawScheduleBean.getStatus();
			String filename ="";
			String subject =" IMPORTANT DRAW Cancel ALERT ";
			StringBuilder bodyText = new StringBuilder();
			bodyText.append("<b>Hello Support Team </b> <br> </br>  Draw Ids: ").append(drawScheduleBean.getDrawIdList()).append("Has Been Canceled. Draw  Status: ").append(drawStatus).append(" In Game NO: ").append(drawScheduleBean.getGameNo());
			List<String> to  =CommonFunctionsHelper.getEmailIdsForDrawMgmtNotification();
			if(to!=null&&to.size()>0){
				MailSender  sendMail = new MailSender(ConfigurationVariables.from,ConfigurationVariables.password,to,subject,bodyText.toString(), filename);
				sendMail.setDaemon(true);
				sendMail.start();
				logger.info("Mail Has Been Send");
			}
			
			
			
			
			
		}
		return drawBean;
	}
	
	public SchedulerBean freezeDraw(DrawScheduleBean drawScheduleBean, UserInfoBean userBean)
	throws Exception {
		ServiceResponse sRes = new ServiceResponse();
		ServiceRequest sReq = new ServiceRequest();
		sReq.setServiceName(ServiceName.DRAW_MGMT);
		sReq.setServiceMethod(ServiceMethodName.DRAWGAME_FREEZE_DRAW);
		sReq.setServiceData(drawScheduleBean);
		IServiceDelegate delegate = ServiceDelegate.getInstance();
		sRes = delegate.getResponse(sReq);
		if (sRes.getIsSuccess()) {
			//rescheduleJob(drawScheduleBean.getGameNo());
			Connection con = DBConnect.getConnection();
			PreparedStatement ps = con.prepareStatement("insert into st_dg_draw_status_change_history (user_id, action, date, remarks) values (?,?,?,?)");
			ps.setInt(1, userBean.getUserId());
			ps.setString(2, "FREEZE");
			ps.setString(3,Util.getCurrentTimeString());
			ps.setString(4, "Freezed draws "+drawScheduleBean.getDrawIdList());
			ps.executeUpdate();
			new DrawGameRPOS().newData();
			new SetResetUserPasswordAction().logOutAllRets();
			
			String drawStatus =drawScheduleBean.getStatus();
			String filename ="";
			String subject =" IMPORTANT DRAW FREEZE  ALERT ";
			StringBuilder bodyText = new StringBuilder();
			bodyText.append("<b>Hello Support Team </b> <br> </br>  Draw Ids: ").append(drawScheduleBean.getDrawIdList()).append("Has Been Freezed  . Draw  Status: ").append(drawStatus).append(" In Game NO: ").append(drawScheduleBean.getGameNo());
			List<String> to  =CommonFunctionsHelper.getEmailIdsForDrawMgmtNotification();
			if(to!=null&&to.size()>0){
				MailSender  sendMail = new MailSender(ConfigurationVariables.from,ConfigurationVariables.password,to,subject,bodyText.toString(), filename);
				sendMail.setDaemon(true);
				sendMail.start();
				logger.info("Mail Has Been Send");
			}
		}
		
		String responseString=sRes.getResponseData().toString();
		Type elementType = new TypeToken<SchedulerBean>() {}.getType();
		SchedulerBean schedulerBean = new Gson().fromJson(responseString, elementType);
		return schedulerBean;
	}
	
	public CancelTicketBean cancelTicketAtBO(CancelTicketBean cancelTicketBean,	UserInfoBean userInfoBO,String search_type,String gameNo,String cancellationCharges) throws LMSException {

		int gameNbr = 0;
		int gameId = 0;
		int retUserId = 0;
		int barCodeCount = -1;
		String ticketNumber = null;

		ResultSet rs = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		Statement stmt = null ;
		ResultSet rs1 = null ;
		UserInfoBean retUserBean = null;
		ValidateTicketBean tktBean=null;

		try {
			boolean isPromo = false;
			cancelTicketBean.setPromo(isPromo) ;
			con = DBConnect.getConnection();
			con.setAutoCommit(false);
			ticketNumber = Util.getTicketNumber(cancelTicketBean.getTicketNo(),3);
			gameNbr = getGamenoFromTktnumber(ticketNumber);
			gameId = Util.getGameIdFromGameNumber(gameNbr);
			if (gameId == 0) 
				throw new LMSException(LMSErrors.INVALID_TICKET_ERROR_CODE,LMSErrors.INVALID_TICKET_ERROR_MESSAGE);
				cancelTicketBean.setGameId(gameId);

			if (cancelTicketBean.getTicketNo().length() == com.skilrock.lms.common.ConfigurationVariables.barcodeCount) 
				barCodeCount = Integer.parseInt(Util.getBarCodeCountFromTicketNumber(cancelTicketBean.getTicketNo()));

			if ("RAFFLE".equalsIgnoreCase(Util.getGameType(gameId))) 
				throw new LMSException(LMSErrors.INVALID_CANCEL_TICKET_DATA_ERROR_CODE,LMSErrors.INVALID_CANCEL_TICKET_DATA_ERROR_MESSAGE);
			else if(ReportUtility.fetchGameMapWithoutPromo().containsKey(gameId))
			{
			    //No Promo ticket cancellation directly
				stmt = con.createStatement() ;
				String checkPromoTicket = "select promo_ticket_nbr from ge_sale_promo_ticket_mapping where promo_ticket_nbr = "+ticketNumber.substring(0, ticketNumber.length()-1) ;
				rs1 = stmt.executeQuery(checkPromoTicket) ;
				if(rs1.next())
					throw new LMSException(LMSErrors.INVALID_CANCEL_PROMOTIONAL_TICKET_DATA_ERROR_CODE,LMSErrors.INVALID_CANCEL_PROMOTIONAL_TICKET_DATA_ERROR_MESSAGE);
				else{
					isPromo = true ;
				}
			}
			
			retUserId = Util.getUserIdFromTicket(ticketNumber);
			
			pstmt = con.prepareStatement("select um.user_id,om.organization_id,um.organization_type,um.user_name,om.parent_id from st_lms_user_master um,st_lms_organization_master om where um.organization_id=om.organization_id and  um.user_id=?");
			pstmt.setInt(1, retUserId);
			logger.info("****select ticket cancel info query*****" + pstmt);
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				cancelTicketBean.setUserId(rs.getInt("user_id"));
				cancelTicketBean.setPartyId(rs.getInt("organization_id"));
				cancelTicketBean.setPartyType(rs.getString("organization_type"));
				
				retUserBean = new UserInfoBean();
				retUserBean.setUserId(rs.getInt("user_id"));
				retUserBean.setParentOrgId(rs.getInt("parent_id"));
				retUserBean.setUserName(rs.getString("user_name"));
				retUserBean.setUserOrgId(rs.getInt("organization_id"));
			}else{
				logger.debug("User Id Not Found In Data Base");
				throw new LMSException(LMSErrors.INVALID_TICKET_ERROR_CODE,LMSErrors.INVALID_TICKET_ERROR_MESSAGE);
				}

			logger.debug("*Inside Cancel Ticket");
			tktBean = Util.validateTkt(ticketNumber);
			if (tktBean.isValid()) {
				boolean isPromoCancelled = true;
				cancelTicketBean.setAutoCancel("CANCEL_MANUAL");
				List<String> promoTicketList = orgOnLineSaleCreditUpdation.getAssociatedPromoTicket(tktBean.getTicketNumInDB(),con);
				if (promoTicketList != null && promoTicketList.size() > 0) {
					for (int i = 0; i < promoTicketList.size(); i++) {
						int gameNmbr = getGamenoFromTktnumber(promoTicketList.get(i)+Util.getRpcAppenderForTickets(promoTicketList.get(i).length()));
						String gameType = Util.getGameType(gameNmbr);
						cancelTicketBean.setReprintCount(tktBean.getReprintCount());

						if ("RAFFLE".equalsIgnoreCase(gameType)) { // Cancel RAFFLE Ticket. 
							cancelTicketBean.setPromoTicketList(promoTicketList);
							isPromoCancelled = cancelRaffleTicket(cancelTicketBean, retUserBean,cancellationCharges, con, userInfoBO.getUserId(),search_type);
						} else {  								   // Cancel Associated Promotional Ticket. 
							cancelTicketBean.setTicketNo(promoTicketList.get(i));  
							cancelTicketBean.setBarCodeCount(-1);
							cancelTicketBean.setGameNo(gameNmbr);
							cancelTicketBean.setGameId(Util.getGameIdFromGameNumber(gameNmbr));
							isPromoCancelled = cancelStdTkt(retUserBean,cancelTicketBean, con, cancellationCharges,userInfoBO.getUserId(), search_type);
						}
					}
				}	
				if (isPromoCancelled) {   // Cancel Standard Ticket after cancelling all the Promotional tickets if Associated.
					cancelTicketBean.setPromo(isPromo);
					cancelTicketBean.setTicketNo(tktBean.getTicketNumInDB());
					cancelTicketBean.setReprintCount(tktBean.getReprintCount());
					cancelTicketBean.setGameNo(tktBean.getGameNo());
					cancelTicketBean.setBarCodeCount(barCodeCount);
					cancelTicketBean.setGameId(Util.getGameIdFromGameNumber(tktBean.getGameNo()));
					if (cancelStdTkt(retUserBean, cancelTicketBean, con, cancellationCharges, userInfoBO.getUserId(), search_type)) {
						con.commit();
					}
				}
			} else {
				logger.debug("Ticket Validation Failed...");
				throw new LMSException(LMSErrors.INVALID_TICKET_ERROR_CODE,LMSErrors.INVALID_TICKET_ERROR_MESSAGE);
			}
			cancelTicketBean.setTicketNo(ticketNumber);
		} catch (LMSException e) {
			cancelTicketBean.setValid(false);
			cancelTicketBean.setErrMsg(e.getErrorMessage());
		}catch (Exception e) {
			cancelTicketBean.setValid(false);
			e.printStackTrace();
			try {
				con.rollback();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
			throw new LMSException(LMSErrors.BO_CANCEL_TICKET_DATA_ERROR_CODE,LMSErrors.BO_CANCEL_TICKET_DATA_ERROR_MESSAGE);
		} finally {
			DBConnect.closeConnection(con, pstmt, rs);
		}
		return cancelTicketBean;
	}


	
	public SchedulerBean changeFreezeTime(DrawScheduleBean drawScheduleBean, UserInfoBean userBean)
			throws Exception {
		ServiceResponse sRes = new ServiceResponse();
		ServiceRequest sReq = new ServiceRequest();
		sReq.setServiceName(ServiceName.DRAW_MGMT);
		sReq.setServiceMethod(ServiceMethodName.DRAWGAME_CHANGE_FREEZETIME);
		sReq.setServiceData(drawScheduleBean);
		IServiceDelegate delegate = ServiceDelegate.getInstance();
		sRes = delegate.getResponse(sReq);
		logger.debug("response in dgm helper------" + sRes);
		if (sRes.getIsSuccess()) {
		
				
			//rescheduleJob(drawScheduleBean.getGameNo());
			Connection con = DBConnect.getConnection();
			PreparedStatement ps = con.prepareStatement("insert into st_dg_draw_status_change_history (user_id, action, date, remarks) values (?,?,?,?)");
			ps.setInt(1, userBean.getUserId());
			ps.setString(2, "CHANGE FREEZE TIME");
			ps.setString(3, Util.getCurrentTimeString());
			ps.setString(4, "Changed Freeze times of draws "+drawScheduleBean.getDrawIdList());
			ps.executeUpdate();
			new DrawGameRPOS().newData();
			new SetResetUserPasswordAction().logOutAllRets();
			
			
			String drawStatus =drawScheduleBean.getStatus();
			String filename ="";
			String subject =" IMPORTANT DRAW FREEZE TIME CHANGE ALERT ";
			StringBuilder bodyText = new StringBuilder();
			bodyText.append("<b>Hello Support Team </b> <br> </br> Freeze Time For  Draw Ids: ").append(drawScheduleBean.getDrawIdList()).append("Has Been Changed. Draw  Status: ").append(drawStatus).append(" In Game NO: ").append(drawScheduleBean.getGameNo());
			List<String> to  =CommonFunctionsHelper.getEmailIdsForDrawMgmtNotification();
			if(to!=null&&to.size()>0){
				MailSender  sendMail = new MailSender(ConfigurationVariables.from,ConfigurationVariables.password,to,subject,bodyText.toString(), filename);
				sendMail.setDaemon(true);
				sendMail.start();
				logger.info("Mail Has Been Send");
			}
			
			
		}
		String responseString=sRes.getResponseData().toString();
		Type elementType = new TypeToken<SchedulerBean>() {}.getType();
		SchedulerBean schedulerBean = new Gson().fromJson(responseString, elementType);
		return schedulerBean;
	}

	public Object checkNextDraw(DrawScheduleBean drawScheduleBean)
			throws Exception {
		ServiceResponse sRes = new ServiceResponse();
		ServiceRequest sReq = new ServiceRequest();
		sReq.setServiceName(ServiceName.DRAWGAME);
		sReq.setServiceMethod(ServiceMethodName.DRAWGAME_CHECK_NEXTDRAW);
		sReq.setServiceData(drawScheduleBean);
		IServiceDelegate delegate = ServiceDelegate.getInstance();
		sRes = delegate.getResponse(sReq);
		return (Object) sRes.getResponseData();
	}

	@SuppressWarnings("unchecked")
	public List<DrawPanelSaleBean> DrawMgmtReport(List<String> list) {
		List<DrawPanelSaleBean> finalList = null;
		ServiceResponse sRes = new ServiceResponse();
		ServiceRequest sReq = new ServiceRequest();
		sReq.setServiceName(ServiceName.REPORTS_MGMT);
		sReq.setServiceMethod(ServiceMethodName.DRAW_PANEL_SALE);
		sReq.setServiceData(list);
		IServiceDelegate delegate = ServiceDelegate.getInstance();
		sRes = delegate.getResponse(sReq);
		
//		if(sRes.getIsSuccess())
		{
			Type type = new TypeToken<List<DrawPanelSaleBean>>(){}.getType();
			finalList = (List<DrawPanelSaleBean>)new Gson().fromJson((JsonElement)sRes.getResponseData(), type);
		}
		
		logger.info("***DRAW_PANEL_SALE***"	+ finalList);
		return finalList;
	}

	public LinkedHashMap<String, Map<String, String>> fetchAdvMessageData(
			String searchType) throws Exception {
		LinkedHashMap<String, Map<String, String>> retMap = new LinkedHashMap<String, Map<String, String>>();
		
		Connection con = DBConnect.getConnection();
		Statement drawStmt = con.createStatement();
		String selRet = null;
		Map<String, String> retList = null;
		String type = null;

			String orgCodeQry = " slom.name orgCode  ";

		
		if ((LMSFilterDispatcher.orgFieldType).equalsIgnoreCase("CODE")) {
			orgCodeQry = " slom.org_code orgCode ";


		} else if ((LMSFilterDispatcher.orgFieldType)
				.equalsIgnoreCase("CODE_NAME")) {
			orgCodeQry = " concat(slom.org_code,'_',slom.name)  orgCode ";
		

		} else if ((LMSFilterDispatcher.orgFieldType)
				.equalsIgnoreCase("NAME_CODE")) {
			orgCodeQry = " concat(slom.name,'_',slom.org_code)  orgCode ";
		

		}	
		


		if ("AGENTWISE".equalsIgnoreCase(searchType)) {
			selRet = "select "+orgCodeQry+",upper(parent.name) as searchType,slom.organization_id from st_lms_organization_master slom,(select name,organization_id from st_lms_organization_master where parent_id=(SELECT organization_id FROM  st_lms_user_master  WHERE organization_type='BO' AND isrolehead='Y' LIMIT 1 )) parent, st_lms_user_master slum where slom.parent_id=parent.organization_id and slom.organization_id = slum.organization_id order by "+QueryManager.getAppendOrgOrder();
		} else {
			selRet = "select "+orgCodeQry+",slom.organization_id,upper(slom.city) searchType from st_lms_organization_master slom,(select name,organization_id from st_lms_organization_master where parent_id=(SELECT organization_id FROM  st_lms_user_master  WHERE organization_type='BO' AND isrolehead='Y' LIMIT 1 )) parent, st_lms_user_master slum where slom.parent_id=parent.organization_id and slom.organization_id = slum.organization_id order by "+QueryManager.getAppendOrgOrder();
		}
		

		/*	if ("AGENTWISE".equalsIgnoreCase(searchType)) {
			selRet = "SELECT UPPER(slom.name) NAME,UPPER(parent.name) AS searchType,slom.organization_id FROM st_lms_organization_master slom,(SELECT a.NAME,a.organization_id FROM st_lms_organization_master a  INNER JOIN (SELECT organization_id FROM  st_lms_user_master  WHERE organization_type='BO' AND isrolehead='Y' LIMIT 1 ) b   ON a. parent_id=b.organization_id) parent, st_lms_user_master slum WHERE slom.parent_id=parent.organization_id AND slom.organization_id = slum.organization_id   ORDER BY searchType,NAME;";
		} else {
			selRet = "SELECT UPPER(slom.name) NAME,slom.organization_id,UPPER(slom.city) searchType FROM st_lms_organization_master slom,(SELECT a.NAME,a.organization_id FROM st_lms_organization_master a  INNER JOIN (SELECT organization_id FROM  st_lms_user_master  WHERE organization_type='BO' AND isrolehead='Y' LIMIT 1 ) b   ON a. parent_id=b.organization_id) parent, st_lms_user_master slum WHERE slom.parent_id=parent.organization_id AND slom.organization_id = slum.organization_id   ORDER BY searchType,NAME;";
		}*/
		ResultSet retRs = drawStmt.executeQuery(selRet);
		while (retRs.next()) {
			type = retRs.getString("searchType");
			if (retMap.containsKey(type)) {
				retMap.get(type).put(retRs.getString("organization_id"), retRs.getString("orgCode"));
			} else {
				retList = new LinkedHashMap<String, String>();
				retList.put(retRs.getString("organization_id"), retRs.getString("orgCode"));
				retMap.put(type, retList);
			}
		}
		DBConnect.closeCon(con);
		return retMap;

	}

	/*public Map<Integer, Map<String, String>> getAdvMsgForEdit() {
		Map<Integer, Map<String, String>> advMap = new TreeMap<Integer, Map<String, String>>();
		Connection con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		try {
			pstmt = con
					.prepareStatement("select msg_id, date, msg_text, status, msg_location, msg_for, activity  from st_dg_adv_msg_master where status = 'ACTIVE' and editable='YES'");
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				
				Map<String, String> tmp = new LinkedHashMap<String, String>();
				tmp.put("Date", rs.getTimestamp("date").toString());
				tmp.put("Message Text", rs.getString("msg_text"));
				tmp.put("status", rs.getString("status"));
				tmp.put("location", rs.getString("msg_location"));
				tmp.put("Message For", rs.getString("msg_for"));
				tmp.put("Activity", rs.getString("activity"));
				advMap.put(rs.getInt("msg_id"), tmp);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return advMap;
	}*/

	/*public boolean editAdvMsgStatus(int msgId, int userId, int orgId) {
		
		Connection con = null;
		boolean status = false;
		StringBuilder query=null;
		PreparedStatement pstmt = null;
		
		try {
			con = DBConnect.getConnection();
			con.setAutoCommit(false);
			query= new StringBuilder("insert into st_dg_adv_msg_master_history select msg_id, date, creator_user_id,msg_text, msg_for , 'INACTIVE', ? ,?  from st_dg_adv_msg_master where msg_id = ?");
			
			pstmt = con.prepareStatement(query.toString());
			pstmt.setString(1, new java.sql.Timestamp(new java.util.Date().getTime()).toString());
			pstmt.setString(2, userId + "");
			pstmt.setInt(3, msgId);
			logger.debug("instAdvHist:   " + pstmt);
			pstmt.executeUpdate();

			query= new StringBuilder("update st_dg_adv_msg_master set status = 'INACTIVE' where msg_id = ?");
			pstmt = con.prepareStatement(query.toString());
			pstmt.setInt(1, msgId);
			logger.debug("updtAdvMst:   " + pstmt);
			pstmt.executeUpdate();

			query= new StringBuilder("insert into st_dg_adv_msg_org_mapping_history select amm.msg_id, aom.org_id , aom.game_id, activity, ?, ? from st_dg_adv_msg_org_mapping aom, st_dg_adv_msg_master amm where amm.msg_id = aom.msg_id and amm.msg_id = ?");
			pstmt = con.prepareStatement(query.toString());
			pstmt.setString(1, new java.sql.Timestamp(new java.util.Date().getTime()).toString());
			pstmt.setString(2, userId + "");
			pstmt.setInt(3, msgId);
			logger.debug("instOrgMappingHist:   " + pstmt);
			pstmt.executeUpdate();
			
			query= new StringBuilder("delete from st_dg_adv_msg_org_mapping where msg_id = ?");
			pstmt = con.prepareStatement(query.toString());
			pstmt.setInt(1, msgId);
			logger.debug("delOrgMapMst:   " + pstmt);
			pstmt.executeUpdate();
			
			status = true;
			con.commit();
		} catch (SQLException e) {
			logger.error("SQL Exception  :- " + e);
		} catch (Exception e) {
			logger.error("General Exception  :- " + e);
		} finally {
			DBConnect.closeConnection(con, pstmt);
		}
		return status;
	}*/

	public Map<Integer, String> fetchBoUserList(int userId, String userOrgType) {
		Connection con = DBConnect.getConnection();
		Map<Integer, String> boUserList = new TreeMap<Integer, String>();

		try {
			String query = "select CONCAT_WS('-',first_name, last_name) as username,user_id from st_lms_user_contact_details where user_id in (select user_id from st_lms_user_master where parent_user_id = ? and organization_type = ?)";
			PreparedStatement pstmt = con.prepareStatement(query);
			pstmt.setInt(1, userId);
			pstmt.setString(2, userOrgType);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				boUserList.put(rs.getInt("user_id"), rs.getString("username"));
			}

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return boUserList;
	}

	public ReportBeanDrawModule fetchDrawData(DrawDataBean drawDataBean,String raffleTktType)
			throws Exception {
		if(drawDataBean.getAgentOrgId() > 0){
		Connection con=DBConnect.getConnection();
		ArrayList<Integer> retUserIdList=new ArrayList<Integer>();
		PreparedStatement pstmt=con.prepareStatement("select user_id from st_lms_user_master where organization_id in (select organization_id from st_lms_organization_master where parent_id ="+drawDataBean.getAgentOrgId()+" and organization_type='RETAILER')");
		ResultSet rs=pstmt.executeQuery();
		while(rs.next()){
			
			retUserIdList.add(rs.getInt("user_id"));
		}
		System.out.println("retailer User Id List::"+retUserIdList);
		drawDataBean.setRetailerUserIdList(retUserIdList);
		}
		ServiceResponse sRes = new ServiceResponse();
		ServiceRequest sReq = new ServiceRequest();
		sReq.setServiceName(ServiceName.REPORTS_MGMT);
		sReq.setServiceMethod(ServiceMethodName.DRAWGAME_FETCH_DATA);
		sReq.setServiceData(drawDataBean);
		IServiceDelegate delegate = ServiceDelegate.getInstance();
		sRes = delegate.getResponse(sReq);
		
		Type type = new TypeToken<ReportBeanDrawModule>(){}.getType();
		ReportBeanDrawModule reportBeanDrawModule = (ReportBeanDrawModule) new Gson().fromJson((JsonElement)sRes.getResponseData(), type);
		
		//update the result of raffle ticket in case of reference ticket
//		ReportBeanDrawModule reportBeanDrawModule = (ReportBeanDrawModule) sRes.getResponseData();
	/*	DrawGameRPOSHelper drawHelper = new DrawGameRPOSHelper();
		String gameType = drawHelper.getGameType(drawDataBean.getGameNo());	
		if("RAFFEL".equalsIgnoreCase(gameType)){
			getDisplayTktNumber(reportBeanDrawModule);			
		}
	*/	
		
		
		String gameType = Util.getGameType(drawDataBean.getGameNo());
		  if("RAFFLE".equalsIgnoreCase(gameType)){				    
		     //to cut last four digit in case of raffle GAME			  
			  if("ORIGINAL".equalsIgnoreCase(raffleTktType)){	
			       ReportDrawBean reportGameBean=null;
				     for(int i=0;i<reportBeanDrawModule.getRepGameBean().getRepDrawBean().size();i++){
							reportGameBean = reportBeanDrawModule.getRepGameBean().getRepDrawBean().get(i); 
							String  winRes = reportGameBean.getWinningResult();
							if(winRes!=null){	
							String[] winResultArr = winRes.split(",");
							StringBuilder finalresult= new StringBuilder("");
								for(int j=0;j<winResultArr.length;j++){
									String winResWithRpCnt = winResultArr[j];
									if(winResWithRpCnt != null && !"null".equalsIgnoreCase(winResWithRpCnt)){
										int length = winResWithRpCnt.length();
											if(length == ConfigurationVariables.tktLenA || length == ConfigurationVariables.tktLenB){ 
												finalresult.append(winResWithRpCnt.substring(0, length-4));
												finalresult.append("xxxx");
												finalresult.append(",");							 
											} 
									 } 
								}
							if(finalresult!=null && !"".equals(finalresult.toString()) && !"0".equals(finalresult.toString())){
								finalresult.deleteCharAt(finalresult.length()-1);
							}
						 reportGameBean.setWinningResult(finalresult.toString());
				     } 
				}
		  }else{
			//for swap result with sale ticket number in case of reference ticket 
		      getDisplayTktNumber(reportBeanDrawModule); //in case of reference ticket
		  }
		    }
				
		return reportBeanDrawModule;

	}
	// start 
	public ArrayList<AnalysisReportDrawBean> fetch15minDrawData(
			DrawDataBean drawDataBean, String raffleTktType) throws Exception {

		ServiceResponse sRes = new ServiceResponse();
		ServiceRequest sReq = new ServiceRequest();
		sReq.setServiceName(ServiceName.REPORTS_MGMT);
		sReq
				.setServiceMethod(ServiceMethodName.DRAWGAME_FETCH_ANALYSIS_REP_DATA);
		sReq.setServiceData(drawDataBean);
		IServiceDelegate delegate = ServiceDelegate.getInstance();
		sRes = delegate.getResponse(sReq);
		
		Type type = new TypeToken<ArrayList<AnalysisReportDrawBean>>(){}.getType();
		ArrayList<AnalysisReportDrawBean> reportBeanDrawModule = new Gson().fromJson((JsonElement)sRes.getResponseData(), type);
		
//		ArrayList<AnalysisReportDrawBean> reportBeanDrawModule = (ArrayList<AnalysisReportDrawBean>) sRes
//				.getResponseData();

		return reportBeanDrawModule;

	}
	
//end added by neeraj jain
	
	public ReportBeanDrawModule fetchDrawMachineData(DrawDataBean drawDataBean,String raffleTktType)
			throws Exception {
				System.out.println("fetchDrawMachine Data");
				ServiceResponse sRes = new ServiceResponse();
				ServiceRequest sReq = new ServiceRequest();
				sReq.setServiceName(ServiceName.REPORTS_MGMT);
				sReq.setServiceMethod(ServiceMethodName.DRAWGAME_FETCH_MACHINE_DATA);
				sReq.setServiceData(drawDataBean);
				IServiceDelegate delegate = ServiceDelegate.getInstance();
				sRes = delegate.getResponse(sReq);
				// update the result of raffle ticket in case of reference ticket
				
				Type type = new TypeToken<ReportBeanDrawModule>(){}.getType();
				
				ReportBeanDrawModule reportBeanDrawModule = (ReportBeanDrawModule)new Gson().fromJson((JsonElement)sRes.getResponseData(), type);
				
//				commented for bug id 20370 
				
	//			ReportBeanDrawModule reportBeanDrawModule = (ReportBeanDrawModule) sRes
//						.getResponseData();
				/*
				 * DrawGameRPOSHelper drawHelper = new DrawGameRPOSHelper(); String
				 * gameType = drawHelper.getGameType(drawDataBean.getGameNo());
				 * if("RAFFEL".equalsIgnoreCase(gameType)){
				 * getDisplayTktNumber(reportBeanDrawModule); }
				
				String gameType = Util.getGameType(drawDataBean.getGameNo());
				if ("RAFFLE".equalsIgnoreCase(gameType)) {
					// to cut last four digit in case of raffle GAME
					raffleTktType="ORIGINAL";
					if ("ORIGINAL".equalsIgnoreCase(raffleTktType)) {
						ReportDrawBean reportGameBean = null;
						for (int i = 0; i < reportBeanDrawModule.getRepGameBean()
								.getRepDrawBean().size(); i++) {
							reportGameBean = reportBeanDrawModule.getRepGameBean()
									.getRepDrawBean().get(i);
							String winRes = reportGameBean.getWinningResult();
							if (winRes != null) {
								String[] winResultArr = winRes.split(",");
								StringBuilder finalresult = new StringBuilder("");

								for (int j = 0; j < winResultArr.length; j++) {
									String winResWithRpCnt = winResultArr[j];
									if (winResWithRpCnt != null
											&& !"null"
													.equalsIgnoreCase(winResWithRpCnt)) {
										int length = winResWithRpCnt.length();
										if (length == ConfigurationVariables.tktLenA
												|| length == ConfigurationVariables.tktLenB) {
											finalresult.append(winResWithRpCnt
													.substring(0, length - 4));
											finalresult.append("xxxx");
											finalresult.append(",");
										}
									}
								}
								if (finalresult != null
										&& !"".equals(finalresult.toString())
										&& !"0".equals(finalresult.toString())) {
									finalresult.deleteCharAt(finalresult.length() - 1);
								}
								reportGameBean.setWinningResult(finalresult.toString());
							}
						}
					} else {
						// for swap result with sale ticket number in case of reference
						// ticket
						getDisplayTktNumber(reportBeanDrawModule); // in case of
																	// reference ticket
					}
				} */


				return reportBeanDrawModule;

			}
	public ReportBeanDrawModule getDisplayTktNumber(ReportBeanDrawModule reportBeanDrawModule){
		//List<ReportDrawBean> reportGameBeanList = reportBeanDrawModule.getRepGameBean().getRepDrawBean();
		ReportDrawBean reportGameBean=null;
		
		Connection con = DBConnect.getConnection();
		Statement drawStmt =null;
		ResultSet drawRs =null;
	try{	
		for(int i=0;i<reportBeanDrawModule.getRepGameBean().getRepDrawBean().size();i++){
			reportGameBean = reportBeanDrawModule.getRepGameBean().getRepDrawBean().get(i);		
			drawStmt = con.createStatement();
			String  winresWithRpCnt = reportGameBean.getWinningResult();
			
		if(winresWithRpCnt != null && !"null".equalsIgnoreCase(winresWithRpCnt)){
			String[] winResultArr = winresWithRpCnt.split(",");
			StringBuilder finalresult= new StringBuilder("");
				for(int j=0;j<winResultArr.length;j++){
					String winResWithRpCnt = winResultArr[j];
					if(winResWithRpCnt != null && !"null".equalsIgnoreCase(winResWithRpCnt)){
						int length = winResWithRpCnt.length();
							if(length == ConfigurationVariables.tktLenA || length == ConfigurationVariables.tktLenB){
								String rpCnt = winResWithRpCnt.substring(length-2, length);
								String query ="select sale_ticket_nbr from ge_sale_promo_ticket_mapping where promo_ticket_nbr='"+winResWithRpCnt.substring(0, length-2)+"'";
								drawRs = drawStmt.executeQuery(query);
								if(drawRs.next()){
									finalresult.append(drawRs.getString(1)+rpCnt);
									finalresult.append(",");
								}
															 
							} else if(length==1){
								finalresult.append(winResWithRpCnt);
								finalresult.append(",");
							}
					 } 
				}
			if(finalresult!=null && !"".equals(finalresult.toString()) && !"0".equals(finalresult.toString())){
				finalresult.deleteCharAt(finalresult.length()-1);
			}
		 reportGameBean.setWinningResult(finalresult.toString());
			
			/*
			int length = winresWithRpCnt.length();
			if(length == ConfigurationVariables.tktLenA || length == ConfigurationVariables.tktLenB){			
			String  winresWithoutRpCnt = winresWithRpCnt.substring(0, length-2);
			
			String rpCnt = winresWithRpCnt.substring(length-2, length);
			drawRs = drawStmt.executeQuery("select sale_ticket_nbr from ge_sale_promo_ticket_mapping where promo_ticket_nbr='"+winresWithoutRpCnt+"'");
			while(drawRs.next()){
				reportGameBean.setWinningResult(drawRs.getString(1)+rpCnt);
			}
			}	
		*/}	
		}	
		DBConnect.closeCon(con);
		} catch (SQLException e) {			
			e.printStackTrace();
		}		
		return reportBeanDrawModule;
	}


	@SuppressWarnings("unchecked")
	public ArrayList<ResultSubmitBean> fetchSubDrawResult(
			DrawScheduleBean drawScheduleBean) throws Exception {
		ServiceResponse sRes = new ServiceResponse();
		ServiceRequest sReq = new ServiceRequest();
		sReq.setServiceName(ServiceName.DRAW_RESULT_MGMT);
		sReq.setServiceMethod(ServiceMethodName.FETCH_SUB_DRAW_RESULT);
		sReq.setServiceData(drawScheduleBean);
		IServiceDelegate delegate = ServiceDelegate.getInstance();
		sRes = delegate.getResponse(sReq);

		String responseString=sRes.getResponseData().toString();
		Type elementType = new TypeToken<ArrayList<ResultSubmitBean>>() {}.getType();
		ArrayList<ResultSubmitBean> resultSubmitBeanList = new Gson().fromJson(responseString, elementType);

		if("RainbowGame".equals(Util.getGameName(drawScheduleBean.getGameNo()))) {
			final String[][] colorArray = new String[][]{{"V", "Voilet"}, {"I", "Indigo"}, {"B", "Blue"}, {"G", "Green"}, {"Y", "Yellow"}, {"O", "Orange"}, {"R", "Red"}};
			for(ResultSubmitBean resultBean : resultSubmitBeanList) {
				String winResult1 = "";
				String winResult2 = "";
				String[] winOne = resultBean.getWinResult().split(",");
				String[] winTwo = resultBean.getWinResult2().split(",");
				for(int i=0; i<6; i++) {
					if(i<3) {
						winResult1 += winOne[i]+",";
						winResult2 += winTwo[i]+",";
					} else {
						for(int j=0; j<7; j++) {
							if(colorArray[j][0].equals(winOne[i]))
								winResult1 += colorArray[j][1]+",";
							if(colorArray[j][0].equals(winTwo[i]))
								winResult2 += colorArray[j][1]+",";
						}
					}
				}
				winResult1 = winResult1.substring(0, winResult1.length()-1);
				winResult2 = winResult2.substring(0, winResult2.length()-1);
				resultBean.setWinResult(winResult1);
				resultBean.setWinResult2(winResult2);
			}
		}
		ResultSubmitBean rsb = null;
		for (int i = 0; i < resultSubmitBeanList.size(); i++) {
			rsb = resultSubmitBeanList.get(i);
			rsb.setUserName(CommonFunctionsHelper.fetchNameOfUser(rsb.getUserId()));
			rsb.setUserName2(CommonFunctionsHelper.fetchNameOfUser(rsb.getUserId2()));
		}
		return resultSubmitBeanList;
	}

	public ArrayList<DrawScheduleBeanResult> getDrawSchdule(
			DrawScheduleBean drawScheduleBean) throws Exception {
		ServiceResponse sRes = new ServiceResponse();
		ServiceRequest sReq = new ServiceRequest();
		sReq.setServiceName(ServiceName.DRAW_RESULT_MGMT);
		sReq.setServiceMethod(ServiceMethodName.DRAWGAME_FETCH_DRAWSCHEDULE);
		sReq.setServiceData(drawScheduleBean);
		IServiceDelegate delegate = ServiceDelegate.getInstance();
		sRes = delegate.getResponse(sReq);
		String responseString=sRes.getResponseData().toString();
		Type elementType = new TypeToken<ArrayList<DrawScheduleBeanResult>>() {}.getType();
		ArrayList<DrawScheduleBeanResult> resultSubmitBeanList = new Gson().fromJson(responseString, elementType);
		return resultSubmitBeanList;

	}

	public Map getLastTenTicket(int gameNo, int retOrgId)
			throws LMSException {
		Connection con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		LinkedHashMap<String, ArrayList<Object>> ticketMap = new LinkedHashMap<String, ArrayList<Object>>();
		String dataLimitAppender="10";
		try {
			if("GHANA".equalsIgnoreCase(Utility.getPropertyValue("COUNTRY_DEPLOYED"))){
				dataLimitAppender="50";
			}
			//String selQry = "select ticket_nbr from st_dg_ret_sale_? where retailer_org_id=(select organization_id from st_lms_organization_master where name=?) order by transaction_id desc limit 10";
			String selQry = "select ticket_nbr from st_dg_ret_sale_? where retailer_org_id=? and ticket_nbr <> 0 order by transaction_id desc limit "+dataLimitAppender;
			pstmt = con.prepareStatement(selQry);
			pstmt.setInt(1, gameNo);
			pstmt.setInt(2, retOrgId);
			logger.debug("*****selQry****" + pstmt);
			rs = pstmt.executeQuery();
			StringBuilder tickets=new StringBuilder("(");
			if(rs.next()){
				do{
					tickets.append(rs.getString("ticket_nbr"));
					tickets.append(",");	
				}while(rs.next());
			}else{
				return ticketMap;
			}
			ticketMap=CommonMethods.fetchTicketToCancel(tickets.toString().substring(0,tickets.toString().length()-1).concat(")").concat(":").concat(String.valueOf(gameNo)));
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException();
		}
		finally{
			if(con!=null){
				DBConnect.closeCon(con);	
			}
		}
		logger.debug("****ticketMap***" + ticketMap);
		return ticketMap;
	}

	/**
	 * This method fetches draws with draw_status as 'ACTIVE'
	 * 
	 * @return
	 * @throws Exception
	 */
	public ArrayList<DrawScheduleBeanResult> getManualDeclareData(
			DrawScheduleBean drawScheduleBean) throws Exception {
		ServiceResponse sRes = new ServiceResponse();
		ServiceRequest sReq = new ServiceRequest();
		sReq.setServiceName(ServiceName.DRAWGAME);
		sReq
				.setServiceMethod(ServiceMethodName.DRAWGAME_FETCH_MANUAL_DECLARE_DATA);
		sReq.setServiceData(drawScheduleBean);
		IServiceDelegate delegate = ServiceDelegate.getInstance();
		sRes = delegate.getResponse(sReq);
		return (ArrayList<DrawScheduleBeanResult>) sRes.getResponseData();
	}

	/**
	 * This method fetches draws with draw_status as 'FREEZE' if Perform Status
	 * is "ALL" else all draws with perform_status = 'PMEP'
	 * 
	 * @return
	 * @throws Exception
	 */
	public ArrayList<DrawScheduleBeanResult> getManualEntryData(
			DrawScheduleBean drawScheduleBean) throws Exception {
		ServiceResponse sRes = new ServiceResponse();
		ServiceRequest sReq = new ServiceRequest();
		sReq.setServiceName(ServiceName.DRAW_RESULT_MGMT);
		sReq
				.setServiceMethod(ServiceMethodName.DRAWGAME_FETCH_MANUAL_ENTRY_DATA);
		sReq.setServiceData(drawScheduleBean);
		IServiceDelegate delegate = ServiceDelegate.getInstance();
		sRes = delegate.getResponse(sReq);
		
		String responseString=sRes.getResponseData().toString();
		Type elementType = new TypeToken<ArrayList<DrawScheduleBeanResult>>() {}.getType();
		ArrayList<DrawScheduleBeanResult> resultSubmitBeanList = new Gson().fromJson(responseString, elementType);
		
		return resultSubmitBeanList;
	}
	public ArrayList<DrawScheduleBeanResult> getManualMachineNumberEntryData(
			DrawScheduleBean drawScheduleBean) throws Exception {
		ServiceResponse sRes = new ServiceResponse();
		ServiceRequest sReq = new ServiceRequest();
		sReq.setServiceName(ServiceName.DRAW_RESULT_MGMT);
		sReq
				.setServiceMethod(ServiceMethodName.DRAWGAME_FETCH_MANUAL_MACHINE_ENTRY_DATA);
		sReq.setServiceData(drawScheduleBean);
		IServiceDelegate delegate = ServiceDelegate.getInstance();
		sRes = delegate.getResponse(sReq);
		Type type = new TypeToken<ArrayList<DrawScheduleBeanResult>>(){}.getType();
		ArrayList<DrawScheduleBeanResult> drawScheduleBeanResults= (ArrayList<DrawScheduleBeanResult>)new Gson().fromJson(sRes.getResponseData().toString(), type);
		return drawScheduleBeanResults;
//		return (ArrayList<DrawScheduleBeanResult>) sRes.getResponseData();
	}
	public SchedulerBean holdDraw(DrawScheduleBean drawScheduleBean)
			throws Exception {
		ServiceResponse sRes = new ServiceResponse();
		ServiceRequest sReq = new ServiceRequest();
		sReq.setServiceName(ServiceName.DRAW_MGMT);
		sReq.setServiceMethod(ServiceMethodName.DRAWGAME_HOLD_DRAW);
		sReq.setServiceData(drawScheduleBean);
		IServiceDelegate delegate = ServiceDelegate.getInstance();
		sRes = delegate.getResponse(sReq);
		if (sRes.getIsSuccess()) {
			//rescheduleJob(drawScheduleBean.getGameNo());
		}
		
		String responseString=sRes.getResponseData().toString();
		Type elementType = new TypeToken<SchedulerBean>() {}.getType();
		SchedulerBean schedulerBean = new Gson().fromJson(responseString, elementType);
		return schedulerBean;
	}

	public String initiateDrawSchdule(DrawScheduleBean drawScheduleBean)
			throws Exception {
		ServiceResponse sRes = new ServiceResponse();
		ServiceRequest sReq = new ServiceRequest();
		sReq.setServiceName(ServiceName.DRAWGAME);
		sReq.setServiceMethod(ServiceMethodName.DRAWGAME_INITIATE_DRAWSCHEDULE);
		sReq.setServiceData(drawScheduleBean);
		IServiceDelegate delegate = ServiceDelegate.getInstance();
		sRes = delegate.getResponse(sReq);
		return (String) sRes.getResponseData();

	}

	/**
	 * This method sets the perform_status of a selected ACTIVE draws to 'PMEP'
	 * so that they are not selected for scheduling by scheduler in DrawGameWeb.
	 * 
	 * @return
	 * @throws Exception
	 */
	public String performManualDeclareEntry(DrawScheduleBean drawScheduleBean)
			throws Exception {
		ServiceResponse sRes = new ServiceResponse();
		ServiceRequest sReq = new ServiceRequest();
		sReq.setServiceName(ServiceName.DRAWGAME);
		sReq
				.setServiceMethod(ServiceMethodName.DRAWGAME_PERFORM_MANUAL_DECLARE_ENTRY);
		sReq.setServiceData(drawScheduleBean);
		IServiceDelegate delegate = ServiceDelegate.getInstance();
		sRes = delegate.getResponse(sReq);
		if (sRes.getIsSuccess()) {
			//rescheduleJob(drawScheduleBean.getGameNo());
		}
		return sRes.getResponseData().toString();

	}

	/**
	 * This method invokes the game respective PerformDraw class of
	 * DrawGameEngine to perform the manual draw.
	 * 
	 * @return
	 * @throws Exception
	 */
	public String performManualWinningEntry(ManualWinningBean mwBean)
			throws Exception {
		if(validateResultData(mwBean)) {
			System.out.println("performManualWinningEntry");
			ServiceResponse sRes = new ServiceResponse();
			ServiceRequest sReq = new ServiceRequest();
			sReq.setServiceName(ServiceName.DRAW_RESULT_MGMT);
			sReq
					.setServiceMethod(ServiceMethodName.DRAWGAME_PERFORM_MANUAL_WINNING_ENTRY);
			sReq.setServiceData(mwBean);
			IServiceDelegate delegate = ServiceDelegate.getInstance();
			Connection con = DBConnect.getConnection();
			int userId = mwBean.getUserId();
			try {
				Statement stmt = con.createStatement();
				StringBuilder qryBuilder = new StringBuilder("select id from st_dg_result_sub_master where game_id = " + mwBean.getGameId()+ " and (");
				for(int i=0; i<10; i++){
					qryBuilder.append("user"+(i+1)+"_id=" + userId + " OR ");
				}
				qryBuilder.delete(qryBuilder.length() - 4, qryBuilder.length());
				qryBuilder.append(")");
				System.out.println("performManualWinningEntry result sub_master query:" + qryBuilder.toString());
				ResultSet rs = stmt.executeQuery(qryBuilder.toString());
				if (rs.next()) {
					sRes = delegate.getResponse(sReq);
					String response = sRes.getResponseData().toString();
					if (response.contains(":")) {
				if (response.split(":")[1].equalsIgnoreCase("MATCHED")) {
					System.out.println("SENDING EMAIL TO USERS.....");
					MailSenderAfterPerformDraw mailSenderAfterDrawPerform=new MailSenderAfterPerformDraw(mwBean);
					mailSenderAfterDrawPerform.setDaemon(true);
					mailSenderAfterDrawPerform.start();
					
				}
				response = response.split(":")[0];
			}
			return response;

		} else {
			return findDefaultText("msg.you.are.not.auth", locale);
		}
	} catch (Exception e) {
		e.printStackTrace();
		return findDefaultText("error.in.perform.draw", locale);
	} finally {
		DBConnect.closeCon(con);
	}
} else {
	return findDefaultText("error.inv.res.data", locale);
}
}

		// Method For Mailing Users Draw Wise
		public List<ReportDrawBean> fetchDrawDateForMail(ManualWinningBean mwBean) throws LMSException {
				System.out.println("Fetching Draw result Results ");
				ServiceResponse sRes = new ServiceResponse();
				ServiceRequest sReq = new ServiceRequest();
				sReq.setServiceName(ServiceName.DRAWGAME);
				sReq.setServiceMethod(ServiceMethodName.FETCH_DRAW_RESULT_FOR_MAILING_USERS);
				sReq.setServiceData(mwBean);
				IServiceDelegate delegate = ServiceDelegate.getInstance();
				sRes = delegate.getResponse(sReq);
				List<ReportDrawBean> mailPerformedDrawList = (ArrayList<ReportDrawBean>) sRes
				.getResponseData();
				return mailPerformedDrawList;
}
	public String performManualWinningMachineNumberEntry(ManualWinningBean mwBean)
			throws Exception {
		System.out.println("performManualWinnindghdgEntry");
		ServiceResponse sRes = new ServiceResponse();
		ServiceRequest sReq = new ServiceRequest();
		sReq.setServiceName(ServiceName.DRAW_RESULT_MGMT);
		sReq
				.setServiceMethod(ServiceMethodName.DRAWGAME_PERFORM_MANUAL_WINNING_MACHINE_NUMBER_ENTRY);
		sReq.setServiceData(mwBean);
		IServiceDelegate delegate = ServiceDelegate.getInstance();
		sRes = delegate.getResponse(sReq);
		return sRes.getResponseData().toString();
		//Connection con = DBConnect.getConnection();
		/*int userId = mwBean.getUserId();
		try {
			Statement stmt = con.createStatement();
			String fetchUser = "select id from st_dg_result_sub_master where game_no = "
					+ mwBean.getGameNumber()
					+ " and (user1_id="
					+ userId
					+ " OR user2_id=" + userId + ")";
			ResultSet rs = stmt.executeQuery(fetchUser);
			if (rs.next()) {
				sRes = delegate.getResponse(sReq);
				return sRes.getResponseData().toString();

		} else {
			return "You are not Authorized";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "Error in Performing Manual Entry";
		}
		finally{
			DBConnect.closeCon(con);
		}*/

	} 


	public SchedulerBean postponeDraw(DrawScheduleBean drawScheduleBean, UserInfoBean userBean)
			throws Exception {
		ServiceResponse sRes = new ServiceResponse();
		ServiceRequest sReq = new ServiceRequest();
		sReq.setServiceName(ServiceName.DRAW_MGMT);
		sReq.setServiceMethod(ServiceMethodName.DRAWGAME_POSTPONE_DRAW);
		sReq.setServiceData(drawScheduleBean);
		IServiceDelegate delegate = ServiceDelegate.getInstance();
		sRes = delegate.getResponse(sReq);
		if (sRes.getIsSuccess()) {
			//rescheduleJob(drawScheduleBean.getGameNo());
			Connection con = DBConnect.getConnection();
			PreparedStatement ps = con.prepareStatement("insert into st_dg_draw_status_change_history (user_id, action, date, remarks) values (?,?,?,?)");
			ps.setInt(1, userBean.getUserId());
			ps.setString(2, "CHANGE DRAW TIME");
			ps.setString(3, Util.getCurrentTimeString());
			ps.setString(4, "Changed draw time of draws "+drawScheduleBean.getDrawIdList());
			ps.executeUpdate();
			new DrawGameRPOS().newData();
			new SetResetUserPasswordAction().logOutAllRets();
			
			String drawStatus =drawScheduleBean.getStatus();
			String filename ="";
			String subject =" IMPORTANT DRAW POSTPONE ALERT ";
			StringBuilder bodyText = new StringBuilder();
			bodyText.append("<b>Hello Support Team </b> <br> </br>  Draw Ids: ").append(drawScheduleBean.getDrawIdList()).append("Has Been Postponeed  . Draw  Status: ").append(drawStatus).append(" In Game NO: ").append(drawScheduleBean.getGameNo());
			List<String> to  =CommonFunctionsHelper.getEmailIdsForDrawMgmtNotification();
			if(to!=null&&to.size()>0){
				MailSender  sendMail = new MailSender(ConfigurationVariables.from,ConfigurationVariables.password,to,subject,bodyText.toString(), filename);
				sendMail.setDaemon(true);
				sendMail.start();
				logger.info("Mail Has Been Send");
			}
			
			
		}
		String responseString=sRes.getResponseData().toString();
		Type elementType = new TypeToken<SchedulerBean>() {}.getType();
		SchedulerBean schedulerBean = new Gson().fromJson(responseString, elementType);
		return schedulerBean;
	}

	public SchedulerBean rankChkDraw(DrawScheduleBean drawScheduleBean, UserInfoBean userBean)
			throws Exception {
		ServiceResponse sRes = new ServiceResponse();
		ServiceRequest sReq = new ServiceRequest();
		sReq.setServiceName(ServiceName.DRAW_MGMT);
		sReq.setServiceMethod(ServiceMethodName.DRAWGAME_RANK_CHK);
		sReq.setServiceData(drawScheduleBean);
		IServiceDelegate delegate = ServiceDelegate.getInstance();
		sRes = delegate.getResponse(sReq);
		if (sRes.getIsSuccess()) {
			//rescheduleJob(drawScheduleBean.getGameNo());
			Connection con = DBConnect.getConnection();
			PreparedStatement ps = con.prepareStatement("insert into st_dg_draw_status_change_history (user_id, action, date, remarks) values (?,?,?,?)");
			ps.setInt(1, userBean.getUserId());
			ps.setString(2, "Claim Hold".equals(drawScheduleBean.getStatus())?"CLAIM HOLD":"CLAIM ALLOW");
			ps.setString(3, Util.getCurrentTimeString());
			ps.setString(4, "Did "+drawScheduleBean.getStatus()+" for draws "+drawScheduleBean.getDrawIdList());
			ps.executeUpdate();
			new DrawGameRPOS().newData();
			//new SetResetUserPasswordAction().logOutAllRets();
		}
		String responseString=sRes.getResponseData().toString();
		Type elementType = new TypeToken<SchedulerBean>() {}.getType();
		SchedulerBean schedulerBean = new Gson().fromJson(responseString, elementType);
		return schedulerBean;
	}

	public void rescheduleJob(int gameNo) {
		try {
			logger.debug(" calling url for scheduler---in postpone");
			URL url = new URL(Util.getDGIP("DG_WEB_IP")
					+ "/DrawGameWeb/RescheduleJob?gameNo=" + gameNo);
			URLConnection conn = url.openConnection();
			conn.setDoOutput(true);
			BufferedReader in = new BufferedReader(new InputStreamReader(conn
					.getInputStream()));
			in.close();
			/*url = new URL(Util.getDGIP("DG_SCH_IP")
					+ "/DrawGameScheduler/RescheduleJob?gameNo=" + gameNo);
			conn = url.openConnection();
			conn.setDoOutput(true);
			in = new BufferedReader(
					new InputStreamReader(conn.getInputStream()));
			in.close();*/
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String resultUserAssignEdit(int gameId, int[] user) {
		Connection con = DBConnect.getConnection();
		try {
			con.setAutoCommit(false);
			StringBuilder qryBuilder = new StringBuilder("update st_dg_result_sub_master set ");
			for(int i=0; i<user.length; i++){
				qryBuilder.append("user"+(i+1)+"_id=" + user[i] + ",");
			}
			qryBuilder.deleteCharAt(qryBuilder.length() - 1);
			qryBuilder.append(" where game_id=" + gameId);
			
			PreparedStatement pstmt = con.prepareStatement(qryBuilder.toString());
			System.out.println("resultUserAssignEdit query:" + pstmt);
			int countUpdate = pstmt.executeUpdate();
			con.commit();
			int gameNo = Util.getGameNumberFromGameId(gameId);
			if (countUpdate <= 0) {
				return resultUserAssignSave(gameId, gameNo, user);
			}
			return "SUCCESS";
		} catch (Exception ex) {
			ex.printStackTrace();
			try {
				con.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} finally {
			DBConnect.closeCon(con);
		}
		return "ERROR";
	}

	public ArrayList<ResultSubmitBean> resultUserAssignFetch() {
		Connection con = DBConnect.getConnection();
		ArrayList<ResultSubmitBean> list = new ArrayList<ResultSubmitBean>();
		try {
			con.setAutoCommit(false);
			String query = "select game_name,sdgm.game_id,sdgm.game_nbr,ifnull(user1_id,-1) as user1_id,ifnull(user2_id,-1) as user2_id ,ifnull(user3_id,-1) as user3_id, ifnull(user4_id,-1) as user4_id, ifnull(user5_id,-1) as user5_id, ifnull(user6_id,-1) as user6_id, ifnull(user7_id,-1) as user7_id, ifnull(user8_id,-1) as user8_id, ifnull(user9_id,-1) as user9_id, ifnull(user10_id,-1) as user10_id from st_dg_result_sub_master sdrs right join st_dg_game_master sdgm on sdrs.game_id=sdgm.game_id where sdgm.game_status='OPEN'";
			PreparedStatement pstmt = con.prepareStatement(query);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				ResultSubmitBean rsb = new ResultSubmitBean();
				rsb.setGameName(rs.getString("game_name"));
				rsb.setGameId(rs.getInt("game_id"));
				rsb.setGameNo(rs.getInt("game_nbr"));
				
				int[] userIdArr = new int[10]; 
				for (int i = 0; i < userIdArr.length; i++) {
					userIdArr[i] = rs.getInt("user"+(i+1)+"_id");
				}
				
				rsb.setUserIdArr(userIdArr);
				list.add(rsb);
			}

			con.commit();

		} catch (Exception ex) {
			ex.printStackTrace();
			try {
				con.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	public String resultUserAssignSave(int gameId, int gameNo, int[] user) {
		Connection con = DBConnect.getConnection();
		try {
			con.setAutoCommit(false);
			String query = "select * from st_dg_result_sub_master where game_id = ?";
			PreparedStatement pstmt = con.prepareStatement(query);
			pstmt.setInt(1, gameId);
			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				return "ALREADY_EXIST";
			}
			
			StringBuilder qryFirst = new StringBuilder("insert into st_dg_result_sub_master (game_id, game_no");
			StringBuilder qryLast = new StringBuilder(") values("+gameId+","+gameNo);
			for(int i=0; i<user.length; i++){
				qryFirst.append(", user"+(i+1)+"_id");
				qryLast.append("," + user[i]);
			}
			
			qryFirst.append(qryLast + ")");
			
			//query = "insert into st_dg_result_sub_master (game_id, game_no, user1_id, user2_id) values(?, ?, ?, ?)";
			pstmt = con.prepareStatement(qryFirst.toString());
			//pstmt.setInt(1, gameId);
			//pstmt.setInt(2, gameNo);
			//pstmt.setInt(3, user1Id);
			//pstmt.setInt(4, user2Id);

			pstmt.executeUpdate();

			con.commit();
			return "SUCCESS";
		} catch (Exception ex) {
			ex.printStackTrace();
			try {
				con.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return "ERROR";
	}

	/*public String saveAdvMessageData(String orgType, String[] gameNo,
			String[] retName, String message, int creatorUserId,
			String msgLocation, String activity, int serviceId) throws SQLException {
		
		int msgId = 0;
		String result=null;
		boolean isAllRet = false;
		//boolean isAllGame = false;
		Connection con = null;
		ResultSet rsMsgId = null;
		Statement drawStmt = null;
		StringBuilder tempRet = null;
		PreparedStatement pstmt = null;
		List<String> tempRetNameList = null;
		//List<String> tempGameList = null;
		//List<String> tempRetIdList = null;
		try{
		//int serviceId = ((HashMap<String,Integer>)LMSUtility.sc.getAttribute("SERVICES_CODE_ID_MAP")).get(serviceCode);
		if (gameNo == null || retName == null)
			throw new LMSException(LMSErrors.BO_ADD_MESSAGING_ERROR_CODE, LMSErrors.BO_ADD_MESSAGING_ERROR_MESSAGE);
			//return "error";
		
		//tempGameList = Arrays.asList(gameNo);
		//tempRetIdList = new ArrayList<String>();
		/*if (tempGameList.contains("-1")) {
			isAllGame = true;
		}*/
		/*tempRetNameList = Arrays.asList(retName);
		if (tempRetNameList.contains("-1")) {
			isAllRet = true;
		}
		tempRet = new StringBuilder("");
		if (!isAllRet)
			for (String element : retName) {
				tempRet.append("'" + element);
				tempRet.append("',");
			}
			/*tempRet.deleteCharAt(tempRet.length() - 1);
			String selRet = "select organization_id from st_lms_organization_master where name in ("
					+ tempRet + ")";
			ResultSet retRs = drawStmt.executeQuery(selRet);
			while (retRs.next()) {
				tempRetIdList.add(retRs.getString("organization_id"));
			}*/
		/*String query = "insert into st_dg_adv_msg_master(date,creator_user_id,msg_text,status,msg_for,msg_location,activity) values('"
				+ new Timestamp(new Date().getTime())
				+ "',"
				+ creatorUserId
				+ ",'"
				+ message
				+ "','ACTIVE','"
				+ orgType
				+ "','"
				+ msgLocation + "','" + activity + "')";*/

		/*con = DBConnect.getConnection();
		con.setAutoCommit(false);
		String query = "insert into st_dg_adv_msg_master(date,creator_user_id,msg_text,status,msg_for,msg_location,activity) values(?,?,?,?,?,?,?)";
		pstmt=con.prepareStatement(query);
		pstmt.setTimestamp(1, new Timestamp(new Date().getTime()));
		pstmt.setInt(2, creatorUserId);
		pstmt.setString(3, message);
		pstmt.setString(4, "ACTIVE");
		pstmt.setString(5, orgType);
		pstmt.setString(6, msgLocation);
		pstmt.setString(7, activity);
		
		logger.info("***************-" + query);
		pstmt.executeUpdate();
		rsMsgId = pstmt.getGeneratedKeys();
		//drawStmt.execute(query);
		if (rsMsgId.next()) 
			msgId = rsMsgId.getInt(1);
		
		drawStmt = con.createStatement();
		if (isAllRet) 
			for (String element : gameNo) {
				query = "insert into st_dg_adv_msg_org_mapping(msg_id,org_id,service_id,game_id) values("
						+ msgId + ",-1,"+serviceId+"," + element + ")";
				drawStmt.addBatch(query);

			}
			//drawStmt.executeBatch();
		 else 
			for (String element : gameNo) {
				for (int j = 0; j < retName.length; j++) {
					query = "insert into st_dg_adv_msg_org_mapping(msg_id,org_id,service_id,game_id) values("
							+ msgId
							+ ","
							+ retName[j].split("~")[0] + ","+serviceId
							+ ","	
							+ element + ")";
					drawStmt.addBatch(query);
				}
			}
			//drawStmt.executeBatch();
		
		drawStmt.executeBatch();
		con.commit();
		//DBConnect.closeCon(con);
		result="success";
		//		ADD ADVERTISEMENT MESSAGES IN STATIC MAP IN CONTEXT
		Util.advMsgDataMap = new DrawGameRPOSHelper().getAdvMsgDataMap();
		}catch (LMSException e) {
			logger.error(e.getErrorMessage());
			logger.error("Exception in Adv Mssg " + e);
			result="error";
		}catch (SQLException e) {
			logger.error("Exception in Adv Mssg " + e);
			result="error";
		}catch (Exception e) {
			logger.error("Exception in Adv Mssg " + e);
			result="error";
		}finally{
			DBConnect.closePstmt(pstmt);
			DBConnect.closeConnection(con, drawStmt, rsMsgId);
		}
		return result;
	}
	
	public String saveAdvMessageDataForRetailer(String orgType, String[] agtName,
			String[] retName, String message, int creatorUserId,
			String msgLocation, String activity1) throws SQLException {
		
		int msgId = 0;
		boolean isAllRet = false;
		
		String result = null;
		String status = null;
		String tempRetStr = "";
		String tempAgtStr = "";
		String tempOrgIdStr = "";
		StringBuilder tempOrgStr = null;

		ResultSet rs = null;
		Connection con = null;
		Statement drawStmt = null;
		PreparedStatement pstmt = null;

		List<Integer> orgIdList = null;
		List<String> phoneNoList = null;
		List<Integer> userIdList = null;
		List<String> tempAgtNameList = null;
		List<String> tempRetNameList = null;
		try{

			if (retName == null && agtName == null) 
			throw new LMSException(LMSErrors.BO_ADD_MESSAGING_ERROR_CODE, LMSErrors.BO_ADD_MESSAGING_ERROR_MESSAGE);

			if(agtName != null)
			tempAgtNameList = Arrays.asList(agtName);

			if(tempAgtNameList != null)
			tempAgtStr = tempAgtNameList.toString().replace(", ", "','").replace("[", "'").replace("]", "'");
		
			if(retName != null){
				String temp[] = new String[retName.length];
					for(int i=0; i<retName.length; i++) 
							temp[i] = retName[i].split("~")[0];
					tempRetNameList = Arrays.asList(temp);
			}
			if(tempRetNameList != null)
			tempRetStr = tempRetNameList.toString().replace(", ", "','").replace("[", "'").replace("]", "'");

			if (tempRetNameList != null && tempRetNameList.contains("-1")) 
			isAllRet = true;

			if (!isAllRet) {
				tempOrgStr = new StringBuilder("");
				if(tempAgtStr.length() == 0 && tempRetStr.length() != 0)
					tempOrgStr.append(tempRetStr);
				else if(tempAgtStr.length() != 0 && tempRetStr.length() == 0)
					tempOrgStr.append(tempAgtStr);
				else 
				tempOrgStr.append(tempAgtStr + "," + tempRetStr);
			
			/*String orgIdQry = "select organization_id from st_lms_organization_master where name in (" + tempOrgStr.toString() + ")";
			pstmt = con.prepareStatement(orgIdQry);
			System.out.println("orgIdQry:" + pstmt);
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				orgIdList.add(rs.getInt("organization_id"));
			}*/
			
			//tempOrgIdStr = orgIdList.toString().replace("[", "(").replace("]", ")").replace(" ", "");
			/*tempOrgIdStr = " and b.organization_id in (" + tempRetStr + ")";
		}
		
		//orgIdList = new ArrayList<Integer>();
		
		String qry = "select a.user_id, b.organization_id, a.phone_nbr from st_lms_user_contact_details a, st_lms_user_master b, st_lms_role_master c where b.isrolehead='Y' and a.user_id=b.user_id and c.is_master = 'Y' and b.role_id=c.role_id" + tempOrgIdStr;
		con = DBConnect.getConnection();
		con.setAutoCommit(false);
		pstmt = con.prepareStatement(qry);
		logger.info("phn no query:" + pstmt);
		rs = pstmt.executeQuery();
		
		orgIdList = new ArrayList<Integer>();
		userIdList = new ArrayList<Integer>();
		phoneNoList = new ArrayList<String>();
		while (rs.next()) {
			userIdList.add(rs.getInt("user_id"));
			orgIdList.add(rs.getInt("organization_id"));
			phoneNoList.add(rs.getString("phone_nbr"));
		}
		/*String query = "insert into st_dg_adv_msg_master(date,creator_user_id,msg_text,status,msg_for,msg_location,activity) values('"
				+ new Timestamp(new Date().getTime())
				+ "',"
				+ creatorUserId
				+ ",'"
				+ message
				+ "','ACTIVE','"
				+ orgType
				+ "','"
				+ msgLocation + "','" + activity1 + "')";
		*/
		
		/*String query = "insert into st_dg_adv_msg_master(date,creator_user_id,msg_text,status,msg_for,msg_location,activity) values(?,?,?,?,?,?,?)";
		pstmt=con.prepareStatement(query);
		pstmt.setTimestamp(1, new Timestamp(new Date().getTime()));
		pstmt.setInt(2, creatorUserId);
		pstmt.setString(3, message);
		pstmt.setString(4, "ACTIVE");
		pstmt.setString(5, orgType);
		pstmt.setString(6, msgLocation);
		pstmt.setString(7, activity1);
		logger.info("***************-" + query);
		pstmt.executeUpdate();
		rs = pstmt.getGeneratedKeys();

		if (rs.next()) 
			msgId = rs.getInt(1);
		//--
		//-- insert SMS details
		
		drawStmt=con.createStatement();
		for (int i = 0; i < orgIdList.size(); i++) {
			status = "Sent";
			Timestamp currTime = new Timestamp(new Date().getTime());
			if("Instant".equalsIgnoreCase(activity1)){
				try {
					Util.sendMsgToUsers(phoneNoList.get(i), message);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if("Draw Perform".equalsIgnoreCase(activity1)){
				
			} else if ("Scheduled".equalsIgnoreCase(activity1)){
				
			}
			query = "insert into st_dg_adv_sms_details (user_id,org_id,phn_no,msg_id,status,time) values("+userIdList.get(i)+","+orgIdList.get(i)+",'"+phoneNoList.get(i)+"',"+msgId+",'"+status+"','"+currTime+"')";
			drawStmt.addBatch(query);
		}
		
		drawStmt.executeBatch();
		con.commit();
		result="success";

		}catch (LMSException e) {
			logger.error(e.getErrorMessage());
			logger.error("Exception in Adv Mssg " + e);
			result="error";
		}catch (SQLException e) {
			logger.error("Exception in Adv Mssg " + e);
			result="error";
		}catch (Exception e) {
			logger.error("Exception in Adv Mssg " + e);
			result="error";
		}finally{
			DBConnect.closeConnection(con, pstmt, drawStmt, rs);
		}

		return result;

	}*/


	public String saveDrawResult(ManualWinningBean mwBean) throws Exception {
		ServiceResponse sRes = new ServiceResponse();
		ServiceRequest sReq = new ServiceRequest();
		sReq.setServiceName(ServiceName.DRAW_RESULT_MGMT);
		sReq.setServiceMethod(ServiceMethodName.SAVE_SUB_DRAW_RESULT);
		sReq.setServiceData(mwBean);
		IServiceDelegate delegate = ServiceDelegate.getInstance();
		sRes = delegate.getResponse(sReq);
		return sRes.getResponseData().toString();

	}

	
	public List<TicketTracking> ticketWinStatus(String ticketNum) throws Exception {
		
		int gameNbr;	
		String orgCodeQry =null;
		String barCodeCount="-1";
		String ticketNumber=null;
		String saleTypeQuery =null;
		String fetchPwtTime = null;
		String fetchPartyName = null;

		ResultSet rs=null;
		Statement stmt =null;
		Connection con = null;
		PreparedStatement pstmt =null;

		ServiceResponse sRes = null;
		ServiceRequest sReq = null;
	
		TicketTracking tktTrack=null;
		ValidateTicketBean tktBean=null;
		List<TicketTracking> trackingList=null;	

		try {

			tktTrack = new TicketTracking();
			trackingList=new LinkedList<TicketTracking>();	
			if( !CommonValidation.isNumericWithoutDot(ticketNum,false) || "".equals(ticketNum) || "0".equals(ticketNum)) // 0 , blank for track ticket transaction ticket
				throw new LMSException(LMSErrors.INVALID_TICKET_ERROR_CODE,LMSErrors.INVALID_TICKET_ERROR_MESSAGE);			

			ticketNumber=Util.getTicketNumber(ticketNum, 3); // ADDED FOR EITHER CASE
			if("".equals(ticketNumber))
				throw new LMSException(LMSErrors.INVALID_TICKET_ERROR_CODE,LMSErrors.INVALID_TICKET_ERROR_MESSAGE);
			
			// Get The BarCode From Ticket Number If Required  
			if (ticketNum.trim().length() == com.skilrock.lms.common.ConfigurationVariables.barcodeCount) 
				barCodeCount = Util.getBarCodeCountFromTicketNumber(ticketNum);
			
			tktBean = new ValidateTicketBean();
			tktBean=Util.validateTkt(ticketNumber); 
			if(!(tktBean.isValid()))
				throw new LMSException(LMSErrors.INVALID_TICKET_ERROR_CODE,LMSErrors.INVALID_TICKET_ERROR_MESSAGE);
			
		con = DBConnect.getConnection();
		gameNbr=Util.getGamenoFromTktnumber(ticketNumber);
		sRes = new ServiceResponse();
		sReq = new ServiceRequest();
		sReq.setServiceName(ServiceName.REPORTS_MGMT);
		
		if(Util.getGameType(Util.getGameIdFromGameNumber(gameNbr)).equals("RAFFLE")){
			sReq.setServiceMethod(ServiceMethodName.RAFFLE_TRACK_TICKET);
			trackingList.add((TicketTracking)trckRaffleTicket(ticketNumber,con));
		}else{
		sReq.setServiceMethod(ServiceMethodName.DRAWGAME_TRACK_TICKET);
		sReq.setServiceData(ticketNumber.concat("Nxt").concat(barCodeCount));
		IServiceDelegate delegate = ServiceDelegate.getInstance();
		sRes = delegate.getResponse(sReq);
		if (!sRes.getIsSuccess()) 
			throw new LMSException(LMSErrors.INVALID_TICKET_ERROR_CODE,LMSErrors.INVALID_TICKET_ERROR_MESSAGE);
		
		if (sRes.getIsSuccess()) {
			
			String responseString=sRes.getResponseData().toString();
			Type elementType = new TypeToken<TicketTracking>() {}.getType();
			tktTrack = new Gson().fromJson(responseString, elementType);
			//tktTrack = (TicketTracking) sRes.getResponseData();
			
			// Rounding Done Based On the Requirement. Initially we used to do it in DGE.
			List<DrawIdBean> drawWinList = null;
			if(Boolean.parseBoolean((String)Utility.getPropertyValue("DO_MATH_ROUNDING_FOR_PWT_AMT"))){
			drawWinList=tktTrack.getDrawWinList();
			Iterator<DrawIdBean> drawWiseiterator = drawWinList.iterator();
			while(drawWiseiterator.hasNext()){
				DrawIdBean drawIdBean = drawWiseiterator.next();
				//Draw Wise Rounding
				drawIdBean.setWinningAmt(Math.round(Double.parseDouble(drawIdBean.getWinningAmt()))+"");
				Iterator<PanelIdBean> panelWiseiterator = drawIdBean.getPanelWinList().iterator();
					while(panelWiseiterator.hasNext()){
						PanelIdBean panelIdBean = panelWiseiterator.next();
						//Panel Wise Rounding
						panelIdBean.setWinningAmt(Math.round(panelIdBean.getWinningAmt()));
						panelIdBean.setPlayType(panelIdBean.getBetDispName());
					}
				}		
			}
			
			HashMap<Integer, String> partyIdMap = tktTrack.getPartyId();
			HashMap<Integer, String> pwtTimeMap = new HashMap<Integer, String>();
			
			int gameId = tktTrack.getGameId();
			String[] arr = new String[] { "st_lms_bo_transaction_master","st_lms_retailer_transaction_master","st_lms_agent_transaction_master" };

			orgCodeQry =QueryManager.getOrgCodeQuery();
			stmt = con.createStatement();
			fetchPartyName = "select organization_id,"+orgCodeQry+" from st_lms_organization_master where organization_id in ("+ partyIdMap.values().toString().replace("[", "").replace("]", "") + ")";
				rs = stmt.executeQuery(fetchPartyName);
				while (rs.next()) {
					partyIdMap.put(rs.getInt("organization_id"), rs.getString("orgCode"));
				}

				fetchPwtTime = " slm,(select transaction_id,draw.draw_id from st_lms_transaction_master sltm,(select draw_id, if(retailer_transaction_id is null ,if(agent_transaction_id is null ,bo_transaction_id,agent_transaction_id ),retailer_transaction_id ) as trans_id from st_dg_pwt_inv_"
						+ gameId
						+ " where ticket_nbr='"
						+ Util.getTktWithoutRpcNBarCodeCount(ticketNumber, ticketNumber.length())
						+ "') draw where sltm.transaction_id=draw.trans_id ) a where slm.transaction_id=a.transaction_id";

			
				for (String element : arr) {
					String qry = "select transaction_date,draw_id from "+ element + fetchPwtTime;
					logger.info(qry);
					rs = stmt.executeQuery(qry);
					while (rs.next()) {
						pwtTimeMap.put(rs.getInt("draw_id"), rs.getString("transaction_date").split("\\.")[0]);
					}
				}
				logger.info(pwtTimeMap + "***Track Tick" + partyIdMap);
				tktTrack.setPartyId(partyIdMap);
				tktTrack.setRetailerName(partyIdMap.get(tktTrack.getRetailerId()));
				tktTrack.setPwtTimeMap(pwtTimeMap);

				saleTypeQuery = "select rt.transaction_type from st_lms_retailer_transaction_master rt, st_dg_ret_sale_? rs where rt.transaction_id = rs.transaction_id and rs.ticket_nbr = ?";
				pstmt = con.prepareStatement(saleTypeQuery);
				pstmt.setInt(1, tktTrack.getGameId());
				pstmt.setString(2,  Util.getTktWithoutRpcNBarCodeCount(ticketNumber, ticketNumber.length()));

				rs = pstmt.executeQuery();
				String trxnType = null;
				if (rs.next()) {
					trxnType = rs.getString("transaction_type");
				}
				if (trxnType != null) {
					if ("DG_SALE".equalsIgnoreCase(trxnType)) {
						tktTrack.setSaleMode("ONLINE");
					} else if ("DG_SALE_OFFLINE".equalsIgnoreCase(trxnType)) {
						tktTrack.setSaleMode("OFFLINE");
					}
				}
				trackingList.add(tktTrack);
		}

		logger.info("ticket tracking::"+trackingList);
				List<PromoGameBean> raffleList = 	DGPromoScheme.getAvailablePromoGamesNew(Util.getGameName(tktTrack.getGameId()), tktTrack.getSaleAmt(), null);
					if(raffleList != null && raffleList.size()>0){
						PromoGameBean promoBean = null;
					//	DrawIdBean drawBean = new DrawIdBean();
						String raffleTicketNum = null;
						for(int i = 0 ; i < raffleList.size();i++){
							promoBean = raffleList.get(i);
							if("RAFFLE".equalsIgnoreCase(promoBean.getPromoGametype())){
							if("ORIGINAL".equalsIgnoreCase(promoBean.getPromoTicketType())){
								PreparedStatement pstmt1 = con.prepareStatement("Select promo_ticket_nbr from ge_sale_promo_ticket_mapping where sale_ticket_nbr = '"+ticketNumber.substring(0, ticketNumber.length() - 2)+"'");
								ResultSet rs1 = pstmt1.executeQuery();								
								if(rs1.next()){
									raffleTicketNum = rs1.getString("promo_ticket_nbr");
								}
								trackingList.add((TicketTracking)trckRaffleTicket(raffleTicketNum+"00",con));
							}
						}
					}
				}
			}
					
				//is there any promo ticket available
				//check it from st_lms_promo_mapping_table here you will get the ticket 14151415141514
		}catch(LMSException e){
			logger.error(e.getErrorMessage());
			tktTrack.setStatus(e.getErrorMessage());
			trackingList.add(tktTrack);
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			DBConnect.closeConnection(con, pstmt,stmt, rs);
		}
		return trackingList;
	}

	
	public TicketTracking trckRaffleTicket(String ticketNum,Connection con) throws Exception{
		ServiceResponse sRes = new ServiceResponse();
		ServiceRequest sReq = new ServiceRequest();
		sReq.setServiceName(ServiceName.REPORTS_MGMT);
		sReq.setServiceMethod(ServiceMethodName.RAFFLE_TRACK_TICKET);
		sReq.setServiceData(ticketNum);
		int gameNbr;
		TicketTracking tktTrack1=new TicketTracking();
		
		

		IServiceDelegate delegate = ServiceDelegate.getInstance();
		//TicketTracking tktTrack = null;
		
			PromoGameBean promoBean = null;
			DrawIdBean drawBean = new DrawIdBean();
			String raffleTicketNum = ticketNum.substring(0,ticketNum.length()-2);
					
						sRes = delegate.getResponse(sReq);
						if(sRes.getIsSuccess()){
							String responseString=sRes.getResponseData().toString();
							Type elementType = new TypeToken<TicketTracking>() {}.getType();
							tktTrack1 = new Gson().fromJson(responseString, elementType);
							//tktTrack1 = (TicketTracking) sRes.getResponseData();	
						drawBean=tktTrack1.getDrawWinList().get(0);
						if (drawBean.getWinResult() != null && !"NULL".equalsIgnoreCase(drawBean.getWinResult()) && !"0".equalsIgnoreCase(drawBean.getWinResult()))
						{
						String[] drawRsltArr = drawBean.getWinResult().split(",");
						StringBuilder tmpRslt = new StringBuilder("");
						for(int k=0; k<drawRsltArr.length; k++){
							drawRsltArr[k] = drawRsltArr[k].substring(0, drawRsltArr[k].length() - 4) + "XXXX,";
							tmpRslt.append(drawRsltArr[k]);
						}
						if(tmpRslt.length() > 0){
							tmpRslt.deleteCharAt(tmpRslt.length() - 1);
						}
						drawBean.setWinResult(tmpRslt.toString());
						}
						HashMap<Integer, String> partyIdMap = tktTrack1.getPartyId();
						HashMap<Integer, String> pwtTimeMap = new HashMap<Integer, String>();
						
						int gameNo = tktTrack1.getGameNo();
						tktTrack1.setTktNumber(raffleTicketNum +"00");
						String[] arr = new String[] { "st_lms_bo_transaction_master",
								"st_lms_retailer_transaction_master",
								"st_lms_agent_transaction_master" };
						
							Statement stmt = con.createStatement();
							String fetchPartyName = "select organization_id,name from st_lms_organization_master where organization_id in ("
									+ partyIdMap.values().toString().replace("[", "")
											.replace("]", "") + ")";
							ResultSet partyRS = stmt.executeQuery(fetchPartyName);
							while (partyRS.next()) {
								partyIdMap.put(partyRS.getInt("organization_id"), partyRS
										.getString("name"));
							}

							String fetchPwtTime = " slm,(select transaction_id,draw.draw_id from st_lms_transaction_master sltm,(select draw_id, if(retailer_transaction_id is null ,if(agent_transaction_id is null ,bo_transaction_id,agent_transaction_id ),retailer_transaction_id ) as trans_id from st_dg_pwt_inv_"
									+ gameNo
									+ " where ticket_nbr='"
									+ raffleTicketNum
									+ "') draw where sltm.transaction_id=draw.trans_id ) a where slm.transaction_id=a.transaction_id";

							ResultSet pwtRS = null;
							for (String element : arr) {
								String qry = "select transaction_date,draw_id from "
										+ element + fetchPwtTime;
								System.out.println(qry);
								pwtRS = stmt.executeQuery(qry);
								while (pwtRS.next()) {
									pwtTimeMap.put(pwtRS.getInt("draw_id"), pwtRS
											.getString("transaction_date").split("\\.")[0]);
								}
							}

							System.out.println(pwtTimeMap + "***Track Tick" + partyIdMap);
							tktTrack1.setPartyId(partyIdMap);
							tktTrack1.setRetailerName(partyIdMap.get(tktTrack1
									.getRetailerId()));
							tktTrack1.setPwtTimeMap(pwtTimeMap);

							String saleTypeQuery = "select rt.transaction_type from st_lms_retailer_transaction_master rt, st_dg_ret_sale_? rs where rt.transaction_id = rs.transaction_id and rs.ticket_nbr = ?";
							PreparedStatement pstmt = con.prepareStatement(saleTypeQuery);
							pstmt.setInt(1, tktTrack1.getGameNo());
							pstmt.setString(2, raffleTicketNum);

							ResultSet rs = pstmt.executeQuery();
							String trxnType = null;
							if (rs.next()) {
								trxnType = rs.getString("transaction_type");
							}
							if (trxnType != null) {
								if ("DG_SALE".equalsIgnoreCase(trxnType)) {
									tktTrack1.setSaleMode("ONLINE");
								} else if ("DG_SALE_OFFLINE".equalsIgnoreCase(trxnType)) {
									tktTrack1.setSaleMode("OFFLINE");
								}
							}
						
			
						}
					return tktTrack1;
				}
			
		
	
	
	
	public Map weeklyReport(ReportStatusBean reportStatusBean) {
		Map utilMap = new HashMap();
		List<DailyLedgerBean> weeklyReport = new ArrayList<DailyLedgerBean>();
		Connection con = null;

		if("NO".equals(Utility.getPropertyValue("IS_DATA_FROM_REPLICA")) || "MAIN_DB".equals(reportStatusBean.getReportingFrom()))
			con = DBConnect.getConnection();
		else
			con = DBConnectReplica.getConnection();

		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(new Date().getTime());
		// cal.add(Calendar.DAY_OF_YEAR, 1);
		System.out.println("**********" + cal.getTime());
		java.sql.Date fromDate = null;
		java.sql.Date toDate = null;
		try {
			Statement stmt = con.createStatement();
			String fetchUser = "select count(organization_id) as retNum from st_lms_organization_master where organization_type='RETAILER'";
			ResultSet rs = stmt.executeQuery(fetchUser);
			if (rs.next()) {
				utilMap.put("RETAILER", rs.getInt("retNum"));
			}
			// PreparedStatement pstmt = con.prepareStatement("select
			// ii.dg_sale_amt 'dg_sale', kk.dg_refund 'dg_sale_refund',
			// jj.dg_pwt_amt 'dg_pwt' from (( select ifnull(sum(net_amt),0)
			// 'dg_sale_amt' from st_dg_bo_sale bo, st_lms_bo_transaction_master
			// btm where btm.transaction_id=bo.transaction_id and
			// btm.transaction_type ='DG_SALE' and (
			// date(btm.transaction_date)>=? and
			// date(btm.transaction_date)<?))ii, (select ifnull(sum(net_amt),0)
			// as 'dg_refund' from st_dg_bo_sale_refund bo,
			// st_lms_bo_transaction_master btm where
			// btm.transaction_id=bo.transaction_id and btm.transaction_type =
			// 'DG_REFUND_CANCEL' and ( date(btm.transaction_date)>=? and
			// date(btm.transaction_date)<?))kk,(select ifnull(sum(pwt_amt),0)
			// 'dg_pwt_amt' from st_dg_bo_pwt bo, st_lms_bo_transaction_master
			// btm where btm.transaction_id=bo.transaction_id and
			// (btm.transaction_type ='DG_PWT' or btm.transaction_type
			// ='DG_PWT_AUTO') and ( date(btm.transaction_date)>=? and
			// date(btm.transaction_date)<?))jj)");
			PreparedStatement pstmt = con
					.prepareStatement("select ii.dg_sale_amt 'dg_sale', kk.dg_refund 'dg_sale_refund', jj.dg_pwt_amt 'dg_pwt', mm.dp_pwt_bo_amt 'dg_dp_bo_pwt',nn.dp_pwt_agt_amt 'dg_dp_agt_pwt' from (( select ifnull(sum(net_amt),0) 'dg_sale_amt'  from st_dg_bo_sale bo, st_lms_bo_transaction_master btm where btm.transaction_id=bo.transaction_id and btm.transaction_type ='DG_SALE' and ( date(btm.transaction_date)>=? and date(btm.transaction_date)<?))ii, (select ifnull(sum(net_amt),0) as 'dg_refund' from st_dg_bo_sale_refund bo, st_lms_bo_transaction_master btm where btm.transaction_id=bo.transaction_id and btm.transaction_type in('DG_REFUND_CANCEL','DG_REFUND_FAILED') and ( date(btm.transaction_date)>=? and date(btm.transaction_date)<?))kk,(select ifnull(sum(pwt_amt),0) 'dg_pwt_amt'  from st_dg_bo_pwt bo, st_lms_bo_transaction_master btm where btm.transaction_id=bo.transaction_id and (btm.transaction_type ='DG_PWT' or btm.transaction_type ='DG_PWT_AUTO') and ( date(btm.transaction_date)>=? and date(btm.transaction_date)<?))jj, (select ifnull(sum(pwt_amt),0) 'dp_pwt_bo_amt' from st_dg_bo_direct_plr_pwt bo, st_lms_bo_transaction_master btm where btm.transaction_id=bo.transaction_id and (btm.transaction_type ='DG_PWT_PLR') and ( date(btm.transaction_date)>=? and date(btm.transaction_date)<?))mm, (select ifnull(sum(pwt_amt),0) 'dp_pwt_agt_amt' from st_dg_agt_direct_plr_pwt bo, st_lms_agent_transaction_master btm where btm.transaction_id=bo.transaction_id and (btm.transaction_type ='DG_PWT_PLR') and ( date(btm.transaction_date)>=? and date(btm.transaction_date)<?))nn)");
			for (int i = 0; i < 7; i++) {
				cal.add(Calendar.DAY_OF_YEAR, -1);
				System.out.println("**********" + cal.getTime());
				fromDate = new java.sql.Date(cal.getTimeInMillis());
				toDate = new java.sql.Date(cal.getTimeInMillis() + 24 * 60 * 60
						* 1000);
				pstmt.setDate(1, fromDate);
				pstmt.setDate(2, toDate);
				pstmt.setDate(3, fromDate);
				pstmt.setDate(4, toDate);
				pstmt.setDate(5, fromDate);
				pstmt.setDate(6, toDate);
				pstmt.setDate(7, fromDate);
				pstmt.setDate(8, toDate);
				pstmt.setDate(9, fromDate);
				pstmt.setDate(10, toDate);
				System.out.println("weeklyReport*******" + pstmt);
				rs = pstmt.executeQuery();
				if (rs.next()) {
					DailyLedgerBean dlbean = new DailyLedgerBean();
					dlbean.setDate(fromDate.toString());
					dlbean.setNetPwt(rs.getDouble("dg_pwt")
							+ rs.getDouble("dg_dp_bo_pwt") + "");
					dlbean.setNetsale(rs.getString("dg_sale"));
					dlbean.setNetSaleRefund(rs.getString("dg_sale_refund"));
					dlbean.setNetsale(rs.getDouble("dg_sale")
							- rs.getDouble("dg_sale_refund") + "");
					weeklyReport.add(dlbean);
				}
			}
			utilMap.put("REPORT", weeklyReport);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return utilMap;
	}
	
	public Map<String, LinkedHashMap<String, DrawDetailsBean>> fetchWinningResultDateWise(String fromDate, String toDate) throws Exception{
		ServiceResponse sRes = new ServiceResponse();
		ServiceRequest sReq = new ServiceRequest();
		sReq.setServiceName(ServiceName.REPORTS_MGMT);
		sReq.setServiceMethod(ServiceMethodName.FETCH_WINNING_RESULT_DATE_WISE);
		boolean isMachineEnabled = LMSFilterDispatcher.isMachineEnabled;
		if(toDate != null){
			sReq.setServiceData(isMachineEnabled + "|" + fromDate + "|" + toDate);
		} else {
			sReq.setServiceData(isMachineEnabled + "|" + fromDate);
		}
		IServiceDelegate delegate = ServiceDelegate.getInstance();
		sRes = delegate.getResponse(sReq);
		
		Type type = new TypeToken<Map<String, LinkedHashMap<String, DrawDetailsBean>>>(){}.getType();
		
		return (Map<String, LinkedHashMap<String, DrawDetailsBean>>)new Gson().fromJson((JsonElement)sRes.getResponseData(), type);
		
//		return (Map)sRes.getResponseData();
	}
	
	public boolean cancelRaffleTicket(CancelTicketBean cancelTicketBean,
			UserInfoBean retUserBean, String cancellationCharges,
			Connection con, int boUserId, String search_type) throws Exception {
		int barCodeCount =-1;
		ServiceResponse sRes = new ServiceResponse();
		ServiceRequest sReq = new ServiceRequest();
		IServiceDelegate delegate = ServiceDelegate.getInstance();
		List<String> promoTicketList = cancelTicketBean.getPromoTicketList();
		if (cancelTicketBean.getTicketNo().length() == com.skilrock.lms.common.ConfigurationVariables.barcodeCount) 
			barCodeCount = Integer.parseInt(Util.getBarCodeCountFromTicketNumber(cancelTicketBean.getTicketNo()));
		cancelTicketBean.setBarCodeCount(barCodeCount);
		int rafleGmeNo = getGamenoFromTktnumber(promoTicketList.get(0) + "0");
		// here cancel raffle ticket
		long refTransid = orgOnLineSaleCreditUpdation.drawRaffleTicketCancel(
				retUserBean, con, cancellationCharges, promoTicketList.get(0),
				rafleGmeNo);
		cancelTicketBean.setRefTransId(refTransid + "");
		cancelTicketBean.setTicketNo(promoTicketList.get(0) + "0");
		cancelTicketBean.setGameId(rafleGmeNo);
		if (refTransid > 0) {
			sRes = new ServiceResponse();
			sReq = new ServiceRequest();
			sReq.setServiceName(ServiceName.RAFFLE);
			sReq.setServiceMethod(ServiceMethodName.RAFFLE_CANCEL_TICKET);
			sReq.setServiceData(cancelTicketBean);
			sRes = delegate.getResponse(sReq);
			if (sRes.getIsSuccess()) {
				if (cancelTicketBean.isValid()) {
					logger.info("---draw game ticket successfully cancelled at BO end");
					String insQry = "insert into st_dg_bo_ticket_cancel(ret_trans_id, user_id, retailer_org_id,  game_no, ticket_no, cancel_type, ticket_amt) values (?,?,?,?,?,?,?)";
					PreparedStatement pstmt = con.prepareStatement(insQry);
					pstmt.setInt(1, Integer.parseInt(cancelTicketBean
							.getRefTransId()));
					pstmt.setInt(2, boUserId);
					pstmt.setInt(3, retUserBean.getUserId());
					pstmt.setInt(4, rafleGmeNo);
					pstmt.setString(5, cancelTicketBean.getTicketNo());

					pstmt.setString(6,
							search_type.equals("Enter_Ticket") ? "BY_NUMBER"
									: "BY_TRANSACTION");
					pstmt.setDouble(7, cancelTicketBean.getRefundAmount());
					int noOfRowsInserted = pstmt.executeUpdate();
					if (noOfRowsInserted <= 0) {
						throw new LMSException(
								findDefaultText("error.insert.not.done.tkt.cancel.at.bo.tkt.cancel.table", locale));

					}
					cancelTicketBean.setErrMsg(findDefaultText("msg.tkt.cancel.success", locale));
					
				} else {
					System.out
							.println("---draw game ticket not cancelled at BO end");

					cancelTicketBean
							.setErrMsg(findDefaultText("error.tkt.cannt.cancel.invalid.tkt", locale));
				}
				
				return true;
			} else {
				cancelTicketBean
				.setErrMsg(findDefaultText("error.tkt.cannt.cancel", locale));
				cancelTicketBean.setValid(false);
				return false;
			}
		} else {
			return false;
		}
	}

	public boolean cancelStdTkt(UserInfoBean retUserBean,
			CancelTicketBean cancelTicketBean, Connection con,
			String cancellationCharges, int boUserId, String search_type) throws Exception {

		ServiceResponse sRes = new ServiceResponse();
		ServiceRequest sReq = new ServiceRequest();
		sReq.setServiceName(ServiceName.PLAYGAME);
		sReq.setServiceMethod(ServiceMethodName.DRAWGAME_CANCEL_TICKET);
		sReq.setServiceData(cancelTicketBean);
		IServiceDelegate delegate = ServiceDelegate.getInstance();

		boolean isFraud = ResponsibleGaming.respGaming(retUserBean, "DG_CANCEL","1", con);
		if (!isFraud) {
			
			double tickRefundAmt = orgOnLineSaleCreditUpdation.drawTicketCancel(
					retUserBean, cancelTicketBean, con, cancellationCharges);
			logger.debug(tickRefundAmt + "*tickRefundAmt");
			
			if(cancelTicketBean.isPromo() == true && tickRefundAmt == 0)
			{
				cancelTicketBean.setValid(false);
				cancelTicketBean.setErrMsg(findDefaultText("err.promo.tkt.no.cancel", locale));
				return false;
			}
			
			if (tickRefundAmt >= 0) {

				cancelTicketBean.setTicketNo(cancelTicketBean.getTicketNo()
						+ cancelTicketBean.getReprintCount());
				sRes = delegate.getResponse(sReq);
				String responseString = sRes.getResponseData().toString();
				Type elementType = new TypeToken<CancelTicketBean>() {}.getType();
				if (sRes.getIsSuccess()) {
					logger.debug("*Inside Cancel Ticket Success");
					cancelTicketBean.setRefundAmount(tickRefundAmt);
					CancelTicketBean cancelTicketBean1 = new Gson().fromJson(responseString, elementType);				
					cancelTicketBean.setCancelTime(cancelTicketBean1.getCancelTime());					
					
					if (cancelTicketBean.isValid()) {

						logger.debug("---draw game ticket successfully cancelled at BO end");
						String insQry = "insert into st_dg_bo_ticket_cancel(ret_trans_id, user_id, retailer_org_id,  game_no, ticket_no, cancel_type, ticket_amt, reason) values (?,?,?,?,?,?,?,?)";
						PreparedStatement pstmt = con.prepareStatement(insQry);
						pstmt.setLong(1, Long.parseLong(cancelTicketBean
								.getRefTransId()));
						pstmt.setInt(2, boUserId);
						pstmt.setInt(3, retUserBean.getUserId());
						pstmt.setInt(4, cancelTicketBean.getGameId());
						pstmt.setString(5, cancelTicketBean.getTicketNo());

						pstmt.setString(6,
								search_type.equals("Enter_Ticket") ? "BY_NUMBER"
										: "BY_TRANSACTION");
						pstmt.setDouble(7, cancelTicketBean.getRefundAmount());
						pstmt.setString(8, cancelTicketBean.getReason());
						int noOfRowsInserted = pstmt.executeUpdate();
						if (noOfRowsInserted <= 0) {
							throw new LMSException(
									findDefaultText("error.insert.not.done.tkt.cancel.at.bo.tkt.cancel.table", locale));

						}
						cancelTicketBean.setErrMsg(findDefaultText("msg.tkt.cancel.success", locale));
						
					} else {
						logger.debug("---draw game ticket not cancelled at BO end");

						cancelTicketBean
								.setErrMsg(findDefaultText("error.tkt.cannt.cancel.invalid.tkt", locale));
					}
					
					
					
					return true;
				} else {
					cancelTicketBean.setErrMsg(((CancelTicketBean)new Gson().fromJson(responseString, elementType)).getErrMsg());
					return false;
				}

			} else {
				cancelTicketBean.setValid(false);
				cancelTicketBean.setErrMsg(findDefaultText("error.tkt.already.cancel.or.claim", locale));
				return false;
			}
		}else {
			cancelTicketBean.setValid(false);
			cancelTicketBean.setError(true);
			cancelTicketBean.setErrMsg(findDefaultText("error.cancel.tkt.limit.exceed", locale));
			return false;
			
		}
		
		
	}
	
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
	
	public boolean validateTktForRelativeRetailer(String tktNo, int agtUserId){
		if(tktNo != null){
			Connection con = DBConnect.getConnection();
			try{
				int retUserId = 0;
				if(tktNo.length() == ConfigurationVariables.tktLenA){
					retUserId = Integer.parseInt(tktNo.substring(0, ConfigurationVariables.retIdLenA));
				} else if(tktNo.length() == ConfigurationVariables.tktLenB){
					retUserId = Integer.parseInt(tktNo.substring(0, ConfigurationVariables.retIdLenB));
				} else {
					return false;
				}
				PreparedStatement pstmt = con.prepareStatement("SELECT user_id FROM st_lms_user_master WHERE organization_type = 'RETAILER' AND user_id = ? AND parent_user_id = ?");
				pstmt.setInt(1, retUserId);
				pstmt.setInt(2, agtUserId);
				logger.debug("**query***"+pstmt+"**");
				ResultSet rs = pstmt.executeQuery();
				
				if(rs.next()){
					return true;
				}
			} catch(Exception ex){
				ex.printStackTrace();
				logger.debug(ex);
			} finally{
				try {
					if(con != null && !con.isClosed()){
						DBConnect.closeCon(con);
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return false;
	}
	
	public static boolean validateResultData(ManualWinningBean mwBean){
		String gameName = Util.getGameName(mwBean.getGameId());
		String fmlyType = (mwBean.getGameId() == 6 || mwBean.getGameId() == 7 || "MiniRoulette".equalsIgnoreCase(gameName) || "FullRoulette".equalsIgnoreCase(gameName)) ? "Fortune" : Util.getGameType(mwBean.getGameId());
	
		if(fmlyType != null){
				if("Fortune".equalsIgnoreCase(fmlyType)){
					return fortuneFmlyValidateData(mwBean);
				} else if("Lotto".equalsIgnoreCase(fmlyType)){
					return lottoFmlyValidateData(mwBean);
				} else if("Keno".equalsIgnoreCase(fmlyType)){
					return kenoFmlyValidateData(mwBean);
				} else if("ForTuneTwo".equalsIgnoreCase(fmlyType)){
					return fortuneTwoFmlyValidateData(mwBean);
				}else if("FourDigit".equalsIgnoreCase(fmlyType)){
					return fourDigitValidateData(mwBean);
				} else if("Rainbow".equalsIgnoreCase(fmlyType)){
					return rainbowValidateData(mwBean);
				}
			
		} else {
			return false;
		}
		return true;
	}
	
	public static boolean fortuneFmlyValidateData(ManualWinningBean mwBean){
		String gameName = Util.getGameName(mwBean.getGameId());
		if(gameName != null){
			int startRange = 0;
			int endRange = 0;
			boolean isDuplicate = false;
			
			if("Fortune".equalsIgnoreCase(gameName)){
				startRange = 1;
				endRange = 12;
				isDuplicate = true;
			} else if("Card12".equalsIgnoreCase(gameName)){
				startRange = 1;
				endRange = 12;
				isDuplicate = true;
			} else if("Card16".equalsIgnoreCase(gameName)){
				startRange = 1;
				endRange = 16;
				isDuplicate = true;
			} else if("Zerotonine".equalsIgnoreCase(gameName)){
				startRange = 1;
				endRange = 10;
				isDuplicate = true;
			} else if("OneToTwelve".equalsIgnoreCase(gameName)){
				startRange = 1;
				endRange = 12;
				isDuplicate = true;
			}else if("MiniRoulette".equalsIgnoreCase(gameName)){
				startRange = 0;
				endRange = 13;
				isDuplicate = true;
			}else if("FullRoulette".equalsIgnoreCase(gameName)){
				startRange = 0;
				endRange = 37;
				isDuplicate = true;
			} else {
				System.out.println("Fortune Family Result Data Validation: invalid game name");
				return false;
			}
			
			int[] cardType = mwBean.getCardType();
			StringBuilder data = new StringBuilder("");
			for (int i = 0; i < cardType.length; i++) {
				data.append(cardType[i] + ",");
			}
			if(data.length() > 0){
				data.deleteCharAt(data.length() - 1);
			}
			
			System.out.println("Result Data Validation: data:" + data.toString());
			if(!Util.validateNumber(startRange, endRange, data.toString(), isDuplicate)){
				System.out.println("Fortune Family Result Data Validation: invalid result data");
				return false;
			}
		} else{
			System.out.println("Fortune Family Result Data Validation: game name is null");
			return false;
		}
		return true;
	}
	
	public static boolean lottoFmlyValidateData(ManualWinningBean mwBean){
		String gameName = Util.getGameName(mwBean.getGameId());
		if(gameName != null){
			int startRange = 0;
			int endRange = 0;
			boolean isDuplicate = false;
			
			if("Lotto".equalsIgnoreCase(gameName)){
				startRange = LottoConstants.START_RANGE;
				endRange = LottoConstants.END_RANGE;
				isDuplicate = LottoConstants.IS_DUPLICATE;
			} else if("Zimlotto".equalsIgnoreCase(gameName)){
				startRange = ZimlottoConstants.START_RANGE;
				endRange = ZimlottoConstants.END_RANGE;
				isDuplicate = ZimlottoConstants.IS_DUPLICATE;
			} else if("ZimlottoTwo".equalsIgnoreCase(gameName)){
				startRange = ZimlottotwoConstants.START_RANGE;
				endRange = ZimlottotwoConstants.END_RANGE;
				isDuplicate = ZimlottotwoConstants.IS_DUPLICATE;
			} else if("ZimLottoBonus".equalsIgnoreCase(gameName)){
				startRange = ZimLottoBonusConstants.START_RANGE;
				endRange = ZimLottoBonusConstants.END_RANGE;
				isDuplicate = ZimLottoBonusConstants.IS_DUPLICATE;
			} else if("ZimLottoBonusFree".equalsIgnoreCase(gameName)){
				startRange = ZimLottoBonusFreeConstants.START_RANGE;
				endRange = ZimLottoBonusFreeConstants.END_RANGE;
				isDuplicate = ZimLottoBonusFreeConstants.IS_DUPLICATE;
			}else if("ZimLottoBonusTwo".equalsIgnoreCase(gameName)){
				startRange = ZimLottoBonusTwoConstants.START_RANGE;
				endRange = ZimLottoBonusTwoConstants.END_RANGE;
				isDuplicate = ZimLottoBonusTwoConstants.IS_DUPLICATE;
			} else if("ZimLottoBonusTwoFree".equalsIgnoreCase(gameName)){
				startRange = ZimLottoBonusTwoFreeConstants.START_RANGE;
				endRange = ZimLottoBonusTwoFreeConstants.END_RANGE;
				isDuplicate = ZimLottoBonusTwoFreeConstants.IS_DUPLICATE;
			}else if("ZimlottoThree".equalsIgnoreCase(gameName)){
				startRange = ZimlottothreeConstants.START_RANGE;
				endRange = ZimlottothreeConstants.END_RANGE;
				isDuplicate = ZimlottothreeConstants.IS_DUPLICATE;
			} else if("Fastlotto".equalsIgnoreCase(gameName)){
				startRange = FastlottoConstants.START_RANGE;
				endRange = FastlottoConstants.END_RANGE;
				isDuplicate = FastlottoConstants.IS_DUPLICATE;
			} else if("bonusballlotto".equalsIgnoreCase(gameName)){
				startRange = BonusBalllottoConstants.START_RANGE;
				endRange = BonusBalllottoConstants.END_RANGE;
				isDuplicate = BonusBalllottoConstants.IS_DUPLICATE;
			}  else if("bonusballtwo".equalsIgnoreCase(gameName)){
				startRange = BonusBalltwoConstants.START_RANGE;
				endRange = BonusBalltwoConstants.END_RANGE;
				isDuplicate = BonusBalltwoConstants.IS_DUPLICATE;
			}else if("tanzanialotto".equalsIgnoreCase(gameName)){
				startRange = TanzanialottoConstants.START_RANGE;
				endRange = TanzanialottoConstants.END_RANGE;
				isDuplicate = TanzanialottoConstants.IS_DUPLICATE;
			} else {
				System.out.println("Lotto Result Data Validation: invalid game name");
				return false;
			}
			
			if(!validateRangeNdDuplicacy(mwBean, startRange, endRange, isDuplicate)){
				return false;
			}
		} else{
			System.out.println("Lotto Result Data Validation: game name is null");
			return false;
		}
		return true;
	
	}
	
	public static boolean kenoFmlyValidateData(ManualWinningBean mwBean){
		String gameName = Util.getGameName(mwBean.getGameId());
		if(gameName != null){
			int startRange = 0;
			int endRange = 0;
			boolean isDuplicate = false;
			
			if("KenoSix".equalsIgnoreCase(gameName)){
				startRange = KenoSixConstants.START_RANGE;
				endRange = KenoSixConstants.END_RANGE;
				isDuplicate = KenoSixConstants.IS_DUPLICATE;
			}else if("Keno".equalsIgnoreCase(gameName) || "KenoFive".equalsIgnoreCase(gameName)){
				startRange = KenoConstants.START_RANGE;
				endRange = KenoConstants.END_RANGE;
				isDuplicate = KenoConstants.IS_DUPLICATE;
			} else if("KenoTwo".equalsIgnoreCase(gameName)){
				startRange = KenoTwoConstants.START_RANGE;
				endRange = KenoTwoConstants.END_RANGE;
				isDuplicate = KenoTwoConstants.IS_DUPLICATE;
			}else if("KenoFour".equalsIgnoreCase(gameName)){
				startRange = KenoFourConstants.START_RANGE;
				endRange = KenoFourConstants.END_RANGE;
				isDuplicate = KenoFourConstants.IS_DUPLICATE;
			}else if("KenoSeven".equalsIgnoreCase(gameName)){
				startRange = KenoSevenConstants.START_RANGE;
				endRange = KenoSevenConstants.END_RANGE;
				isDuplicate = KenoSevenConstants.IS_DUPLICATE;
			}else if("KenoEight".equalsIgnoreCase(gameName)){
				startRange = KenoEightConstants.START_RANGE;
				endRange = KenoEightConstants.END_RANGE;
				isDuplicate = KenoEightConstants.IS_DUPLICATE;
			}else if("superTwo".equalsIgnoreCase(gameName)){
				startRange = SuperTwoConstants.START_RANGE;
				endRange = SuperTwoConstants.END_RANGE;
				isDuplicate = SuperTwoConstants.IS_DUPLICATE;
			} else if("TwelveByTwentyFour".equalsIgnoreCase(gameName)){
				startRange = TwelveByTwentyFourConstants.START_RANGE;
				endRange = TwelveByTwentyFourConstants.END_RANGE;
				isDuplicate = TwelveByTwentyFourConstants.IS_DUPLICATE;
			} else if("TenByTwenty".equalsIgnoreCase(gameName)){
				startRange = TenByTwentyConstants.START_RANGE;
				endRange = TenByTwentyConstants.END_RANGE;
				isDuplicate = TenByTwentyConstants.IS_DUPLICATE;
			}  else if("KenoNine".equalsIgnoreCase(gameName)){
				startRange = KenoNineConstants.START_RANGE;
				endRange = KenoNineConstants.END_RANGE;
				isDuplicate = KenoNineConstants.IS_DUPLICATE;
			}else {
				System.out.println("Keno Result Data Validation: invalid game name");
				return false;
			}
			
			if(!validateRangeNdDuplicacy(mwBean, startRange, endRange, isDuplicate)){
				return false;
			}
		} else{
			System.out.println("Keno Result Data Validation: game name is null");
			return false;
		}
		return true;
	}

	public static boolean rainbowValidateData(ManualWinningBean mwBean) {
		String gameName = Util.getGameName(mwBean.getGameId());
		if(gameName != null) {
			int startRange = 0;
			int endRange = 0;
			boolean isDuplicate = false;
			
			if("RainbowGame".equalsIgnoreCase(gameName)){
				startRange = RainbowConstants.START_RANGE;
				endRange = RainbowConstants.END_RANGE;
				isDuplicate = RainbowConstants.IS_DUPLICATE;
			}else if("PickFour".equalsIgnoreCase(gameName)){
				startRange = PickFourConstants.START_RANGE;
				endRange = PickFourConstants.END_RANGE;
				isDuplicate = PickFourConstants.IS_DUPLICATE;
			} else if("PickThree".equalsIgnoreCase(gameName)){
				startRange = PickThreeConstants.START_RANGE;
				endRange = PickThreeConstants.END_RANGE;
				isDuplicate = PickThreeConstants.IS_DUPLICATE;
			} else {
				logger.info("Rainbow Data Validation - Invalid Game Name");
				return false;
			}

			if(!validateRangeNdDuplicacy(mwBean, startRange, endRange, isDuplicate)){
				return false;
			}
		} else {
			logger.info("Rainbow Data Validation - Game Name is NULL");
			return false;
		}

		return true;
	}

	public static boolean fortuneTwoFmlyValidateData(ManualWinningBean mwBean){
		String gameName = Util.getGameName(mwBean.getGameId());
		if(gameName != null){
			int startRange = 0;
			int endRange = 0;
			boolean isDuplicate = false;
			
			if("FortuneTwo".equalsIgnoreCase(gameName)){
				startRange = 1;
				endRange = 12;
				isDuplicate = true;
			} else if("FortuneThree".equalsIgnoreCase(gameName)){
				startRange = 1;
				endRange = 12;
				isDuplicate = true;
			}
				else {
			
				System.out.println("FortuneTwo Family Result Data Validation: invalid game name");
				return false;
			}
			
			Integer[] cardType = mwBean.getWinningNumbers();
			StringBuilder data = new StringBuilder("");
			for (int i = 0; i < cardType.length; i++) {
				data.append(cardType[i] + ",");
			}
			if(data.length() > 0){
				data.deleteCharAt(data.length() - 1);
			}
			
			System.out.println("Result Data Validation: data:" + data.toString());
			if(!Util.validateNumber(startRange, endRange, data.toString(), isDuplicate)){
				System.out.println("FortuneTwo Family Result Data Validation: invalid result data");
				return false;
			}
		} else{
			System.out.println("FortuneTwo Family Result Data Validation: game name is null");
			return false;
		}
		return true;
	}
	
	public static boolean fourDigitValidateData(ManualWinningBean mwBean){
		String gameName = Util.getGameName(mwBean.getGameId());
		if(gameName != null){
			int startRange = 0;
			int endRange = 0;
			boolean isDuplicate = false;
			
			if("FourDigit".equalsIgnoreCase(gameName)){
				startRange = 0000;
				endRange = 9999;
				isDuplicate = true;
			} 
			else {			
				System.out.println("FourDigit Family Result Data Validation: invalid game name");
				return false;
			}
			
			Integer[] cardType = mwBean.getWinningNumbers();
			StringBuilder data = new StringBuilder("");
			for (int i = 0; i < cardType.length; i++) {
				data.append(cardType[i] + ",");
			}
			if(data.length() > 0){
				data.deleteCharAt(data.length() - 1);
			}
			
			System.out.println("Result Data Validation: data:" + data.toString());
			if(!Util.validateNumber(startRange, endRange, data.toString(), isDuplicate)){
				System.out.println("FourDigit Family Result Data Validation: invalid result data");
				return false;
			}
		} else{
			System.out.println("FourDigit Family Result Data Validation: game name is null");
			return false;
		}
		return true;
	}
	
	private static boolean validateRangeNdDuplicacy(ManualWinningBean mwBean, int startRange, int endRange, boolean isDuplicate) {
		Integer[] winNumbers = mwBean.getWinningNumbers();
		int winNumSize = mwBean.getWinNumSize();
		if(winNumbers.length == winNumSize || validateKenoOrKenoEight(mwBean, winNumbers)){
			StringBuilder data = null;
			
			for(int i=0; i<winNumbers.length/winNumSize; i++){
				data = new StringBuilder("");
				for(int j=i*winNumSize; j<i*winNumSize+winNumSize; j++){
					data.append(winNumbers[j] + ",");
				}
				if(data.length() > 0){
					data.deleteCharAt(data.length()-1);
				}
				
				System.out.println("Result Data Validation: data:" + data.toString());
				if(!Util.validateNumber(startRange, endRange, data.toString(), isDuplicate)){
					System.out.println("Result Data Validation: invalid result data");
					return false;
				}
			}
		} else {
			System.out.println("Result Data Validation: incomplete result data");
			return false;
		}
		return true;
	}


	static boolean validateKenoOrKenoEight(ManualWinningBean mwBean, Integer[] winNumbers) {
		boolean isValid=false;
		if( "Keno".equalsIgnoreCase(Util.getGameName(mwBean.getGameId())) || "KenoEight".equalsIgnoreCase(Util.getGameName(mwBean.getGameId()))){
			isValid = (winNumbers.length == 5 || winNumbers.length == 6); 
			mwBean.setWinNumSize(mwBean.getWinNumSize());
			
		}
		return isValid;
	}
	
	/*public Map<Integer,String> fetchServices(){
		Map<Integer, String> serviceMap = new HashMap<Integer, String>();
		Connection con = DBConnect.getConnection();
		try{
			PreparedStatement pstmt = con.prepareStatement("select service_id, service_display_name from st_lms_service_master where status='ACTIVE' and service_display_name <> 'HOME' AND service_display_name <> 'SCRATCH'");
			System.out.println("Service Query:" + pstmt + "***");
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				int servId = rs.getInt("service_id");
				String servDispName = rs.getString("service_display_name");
				serviceMap.put(servId, servDispName);
			}
		} catch(SQLException se){
			se.printStackTrace();
		} finally{
			DBConnect.closeCon(con);
		}
		return serviceMap;
	}*/
	
	public Map<String,String> fetchServices(){
		Map<String, String> serviceMap = new HashMap<String, String>();
		Connection con = DBConnect.getConnection();
		try{
			PreparedStatement pstmt = con.prepareStatement("select service_id, service_code, service_display_name from st_lms_service_master where status='ACTIVE' and service_display_name <> 'HOME' AND service_display_name <> 'SCRATCH'");
			System.out.println("Service Query:" + pstmt + "***");
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				serviceMap.put(rs.getString("service_code"), rs.getString("service_display_name"));
			}
		} catch(SQLException se){
			se.printStackTrace();
		} finally{
			DBConnect.closeCon(con);
		}
		return serviceMap;
	}
	
	public Map<Integer,String> fetchCategories(){
		Map<Integer, String> categoryMap = new HashMap<Integer, String>();
		Connection con = DBConnect.getConnection();
		try{
			PreparedStatement pstmt = con.prepareStatement("select category_id, category_code from  st_cs_product_category_master where status='ACTIVE'");
			System.out.println("Category Query:" + pstmt + "***");
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				categoryMap.put(rs.getInt("category_id"), rs.getString("category_code"));
			}
		} catch(SQLException se){
			se.printStackTrace();
		} finally{
			DBConnect.closeCon(con);
		}
		return categoryMap;
	}
	

	
	public String fetchCancelDrawData(DrawScheduleBean drawScheduleBean){
		ServiceResponse sRes = new ServiceResponse();
		ServiceRequest sReq = new ServiceRequest();
		sReq.setServiceName(ServiceName.DRAW_MGMT);
		sReq.setServiceMethod(ServiceMethodName.DRAWGAME_FETCH_CANCEL_DRAW_DATA);
		sReq.setServiceData(drawScheduleBean);
		IServiceDelegate delegate = ServiceDelegate.getInstance();
		try{
		sRes = delegate.getResponse(sReq);
		}catch (Exception e) {
			logger.error(e);
		}
		
		String responseString=sRes.getResponseData().toString();
		Type elementType = new TypeToken<TreeMap<String, String>>() {}.getType();
		TreeMap<String, String> drawMap = new Gson().fromJson(responseString, elementType);
		StringBuilder respBuilder = new StringBuilder();
		for(String key: drawMap.keySet()){
			respBuilder.append("<td align=\"center\" width=\"25%\">" + drawMap.get(key) + "</td>,");
		}
		respBuilder.deleteCharAt(respBuilder.length()-1);
		return respBuilder.toString();
	}
/**
 * 	
 * @param drawDataBean
 * @param raffleTktType
 * @return
 */
	public DGConsolidateGameDataBean fetchDrawGameDataForLMS(DrawDataBean drawDataBean,String raffleTktType, String stateCode, String cityCode){
		DGConsolidateGameDataBean consolidateBean = new DGConsolidateGameDataBean();
		ArrayList<Integer> retUserIdList=new ArrayList<Integer>();
		Connection con=null;
//		PreparedStatement pstmt =null;
		Statement stmt = null;
		ResultSet  rs =null;
		//StringBuilder query = new StringBuilder("SELECT user_id FROM st_lms_user_master WHERE organization_id IN (SELECT organization_id FROM st_lms_organization_master om INNER JOIN st_lms_state_master sm ON om.state_code = sm.state_code INNER JOIN st_lms_city_master cm ON om.city = cm.city_name ");
		StringBuilder query = new StringBuilder("select user_id from st_lms_user_master a inner join st_lms_organization_master b on a.organization_id=b.organization_id where a.organization_type='RETAILER'");
//		String stateQuery = null;
//		String cityQuery = null;
//		String retQuery = null;
		try {
				con =DBConnect.getConnection();
			if(drawDataBean.getAgentOrgId() > 0) {
				query.append(" and ");
				query.append(" parent_id = "+drawDataBean.getAgentOrgId()+";");
			}
				stmt = con.createStatement();
				rs = stmt.executeQuery(query.toString());
				while (rs.next()) {
					retUserIdList.add(rs.getInt("user_id"));
				}
				logger.info("Retailers For Agent: " + drawDataBean.getAgentOrgId() + ":- " + retUserIdList);
			
			/* if(!"ALL".equals(stateCode) || (drawDataBean.getAgentOrgId() > 0)) {
				con =DBConnect.getConnection();
				query.append(" WHERE ");
				if(!"ALL".equals(stateCode)) {
					query.append("sm.state_code = '"+stateCode+"'");
				}
				if(!"ALL".equals(cityCode)) {
					if(!"ALL".equals(stateCode)) {
						query.append("AND");
					}
					query.append(" cm.city_name = '"+cityCode+"'");
				}
				if (drawDataBean.getAgentOrgId() > 0) {
					   if(!"ALL".equals(cityCode)||!"ALL".equals(stateCode))
					      query.append("AND");
						query.append(" parent_id = "+drawDataBean.getAgentOrgId()+" AND organization_type='RETAILER'");
				}
				query.append(")");

				stmt = con.createStatement();
				rs = stmt.executeQuery(query.toString());
				while (rs.next()) {
					retUserIdList.add(rs.getInt("user_id"));
				}
				logger.info("Retailers For Agent: " + drawDataBean.getAgentOrgId() + ":- " + retUserIdList);
			}
			
			/*if(drawDataBean.getAgentOrgId() > 0){

				con =DBConnect.getConnection();
				pstmt=con.prepareStatement("select user_id from st_lms_user_master where organization_id in (select organization_id from st_lms_organization_master where parent_id =? and organization_type=?)");
				pstmt.setInt(1,drawDataBean.getAgentOrgId());
				pstmt.setString(2,"RETAILER");
				rs=pstmt.executeQuery();
				while(rs.next()){
					
					retUserIdList.add(rs.getInt("user_id"));
				}
				logger.info("Retailers For Agent: "+drawDataBean.getAgentOrgId()+":- "+retUserIdList);
			
				}	*/
		drawDataBean.setRetailerUserIdList(retUserIdList);
		// get Data For LMS ;
		
		ServiceRequest serReq = new ServiceRequest();
		ServiceResponse serResp = new ServiceResponse();
		serReq.setServiceName(ServiceName.REPORTS_MGMT);
		serReq.setServiceMethod(ServiceMethodName.FETCH_DRAW_GAME_CONSOLIDATE_DATA);
		serReq.setServiceData(drawDataBean);
		IServiceDelegate  delegate =ServiceDelegate.getInstance(); 
		serResp=delegate.getResponse(serReq);
		if(serResp.getIsSuccess()){
//			consolidateBean=(DGConsolidateGameDataBean)serResp.getResponseData();
			Type type = new TypeToken<DGConsolidateGameDataBean>(){}.getType();
			consolidateBean = (DGConsolidateGameDataBean)new Gson().fromJson((JsonElement)serResp.getResponseData(), type);
			logger.info("Got Draw Game Consolidate Data "+consolidateBean);
			
			
			
			 /* String gameType = Util.getGameType(drawDataBean.getGameNo());
			  * commented for bug  20391 
			  * 
			  * if("RAFFLE".equalsIgnoreCase(gameType)){				    
			     //to cut last four digit in case of raffle GAME			  
				  if("ORIGINAL".equalsIgnoreCase(raffleTktType)){	
					  DGConsolidateDrawBean reportDrawBean=null;
					     for(int i=0;i<consolidateBean.getDrawDataBeanList().size();i++){
					    	 	reportDrawBean = consolidateBean.getDrawDataBeanList().get(i); 
								String  winRes = reportDrawBean.getWinningResult();
								if(winRes!=null){	
								String[] winResultArr = winRes.split(",");
								StringBuilder finalresult= new StringBuilder("");
									for(int j=0;j<winResultArr.length;j++){
										String winResWithRpCnt = winResultArr[j];
										if(winResWithRpCnt != null && !"null".equalsIgnoreCase(winResWithRpCnt)){
											int length = winResWithRpCnt.length();
												if(length == ConfigurationVariables.tktLenA || length == ConfigurationVariables.tktLenB){ 
													finalresult.append(winResWithRpCnt.substring(0, length-4));
													finalresult.append("xxxx");
													finalresult.append(",");							 
												} 
										 } 
									}
								if(finalresult!=null && !"".equals(finalresult.toString()) && !"0".equals(finalresult.toString())){
									finalresult.deleteCharAt(finalresult.length()-1);
								}
								reportDrawBean.setWinningResult(finalresult.toString());
					     } 
					}
			  }else{
				//for swap result with sale ticket number in case of reference ticket 
			      getDisplayTktNumber(consolidateBean); //in case of reference ticket
			  }
			    }*/
			
		}
		}catch(Exception e){
			e.printStackTrace();
			
		}finally{
			
			try{
				if(con!=null){
					con.close();
				}
//				if(stmt!=null){
//					stmt.close();
//				}
				if(rs!=null){
					rs.close();
				}
			}catch(Exception e){
				
				e.printStackTrace();
			}
			
		}
		
		
		return consolidateBean ;
	}	
/**
 *  This Method APPEND "XXXX" To Winning Ticket Numbers For RAFFLE Game Result
 * @overload ReportBeanDrawModule getDisplayTktNumber(ReportBeanDrawModule reportBeanDrawModule) 
 * @param consolidateBean
 * @return
 */
	public DGConsolidateGameDataBean getDisplayTktNumber(DGConsolidateGameDataBean consolidateBean){
		//List<ReportDrawBean> reportGameBeanList = reportBeanDrawModule.getRepGameBean().getRepDrawBean();
		  DGConsolidateDrawBean reportDrawBean=null;
		
		Connection con = DBConnect.getConnection();
		Statement drawStmt =null;
		ResultSet drawRs =null;
	try{	
		for(int i=0;i<consolidateBean.getDrawDataBeanList().size();i++){
			reportDrawBean = consolidateBean.getDrawDataBeanList().get(i);		
			drawStmt = con.createStatement();
			String  winresWithRpCnt = reportDrawBean.getWinningResult();
			
		if(winresWithRpCnt != null && !"null".equalsIgnoreCase(winresWithRpCnt)){
			String[] winResultArr = winresWithRpCnt.split(",");
			StringBuilder finalresult= new StringBuilder("");
				for(int j=0;j<winResultArr.length;j++){
					String winResWithRpCnt = winResultArr[j];
					if(winResWithRpCnt != null && !"null".equalsIgnoreCase(winResWithRpCnt)){
						int length = winResWithRpCnt.length();
							if(length == ConfigurationVariables.tktLenA || length == ConfigurationVariables.tktLenB){
								String rpCnt = winResWithRpCnt.substring(length-2, length);
								String query ="select sale_ticket_nbr from ge_sale_promo_ticket_mapping where promo_ticket_nbr='"+winResWithRpCnt.substring(0, length-2)+"'";
								drawRs = drawStmt.executeQuery(query);
								if(drawRs.next()){
									finalresult.append(drawRs.getString(1)+rpCnt);
									finalresult.append(",");
								}
															 
							} else if(length==1){
								finalresult.append(winResWithRpCnt);
								finalresult.append(",");
							}
					 } 
				}
			if(finalresult!=null && !"".equals(finalresult.toString()) && !"0".equals(finalresult.toString())){
				finalresult.deleteCharAt(finalresult.length()-1);
			}
			reportDrawBean.setWinningResult(finalresult.toString());
			
			/*
			int length = winresWithRpCnt.length();
			if(length == ConfigurationVariables.tktLenA || length == ConfigurationVariables.tktLenB){			
			String  winresWithoutRpCnt = winresWithRpCnt.substring(0, length-2);
			
			String rpCnt = winresWithRpCnt.substring(length-2, length);
			drawRs = drawStmt.executeQuery("select sale_ticket_nbr from ge_sale_promo_ticket_mapping where promo_ticket_nbr='"+winresWithoutRpCnt+"'");
			while(drawRs.next()){
				reportGameBean.setWinningResult(drawRs.getString(1)+rpCnt);
			}
			}	
		*/}	
		
		}	
		DBConnect.closeCon(con);
		} catch (SQLException e) {			
			e.printStackTrace();
		}		
		return consolidateBean;
	}
	
	public static boolean updateFreeTicket(UserInfoBean userInfoBean,int gameId,int promoGameId,int promoTicekts){
		PreparedStatement pstmt = null;
		Connection con = null;
		boolean isUdpated =false;
		try{
			con =DBConnect.getConnection();
			con.setAutoCommit(false);
			String query = " update st_dg_promo_scheme set no_of_free_tickets=? where sale_game_id=? and scheme_id=?";
			pstmt =con.prepareStatement(query);
			pstmt.setInt(1, promoTicekts);
			pstmt.setInt(2, gameId);
			pstmt.setInt(3, promoGameId);
			logger.info("query for free ticket update"+pstmt);
			int i=	pstmt.executeUpdate();
			logger.info(" record updated"+i);
			DBConnect.closePstmt(pstmt);
			String insertQry =" insert into st_dg_promo_scheme_history(scheme_id,sale_game_id,no_of_free_tickets,doneBy_user_id,updation_time) select scheme_id,sale_game_id,no_of_free_tickets,"+userInfoBean.getUserId()+" userId, now() dateTime from st_dg_promo_scheme where sale_game_id=? and scheme_id=? ";
			pstmt =con.prepareStatement(insertQry);
			pstmt.setInt(1, gameId);
			pstmt.setInt(2, promoGameId);
			logger.info("query for insert ticket history"+pstmt);
			pstmt.executeUpdate();
			if(i==1){
				con.commit();
				isUdpated=true;
			Util.promoGameBeanMap =DrawGameRPOSHelper.getPromoGameBeanMap(con);
			}
			
					
		}catch(Exception e){
			logger.error("Excetion e",e);
		}finally{
			DBConnect.closePstmt(pstmt);
			DBConnect.closeCon(con);
			
		}
		return isUdpated;
		
	}

	public TicketWiseDataBean getConsolidatedTicketsForDraw(int gameId,int agentOrgId,String drawName,String dateTimeOfDraw,Timestamp uIdChangeDate,AnalysisBean anaBean,String status){
		
		int userId  = 0;
		ServiceRequest serReq = null;
		ServiceResponse serResp = null;
		List<AnalysisReportDrawBean> anaList = null;
		TicketWiseDataBean ticketBean = null;
		try {
			logger.info("inside getConsolidatedTicketsForDraw()");
			userId  = agentOrgId != -1 ? Util.fetchUserIdFormOrgId(agentOrgId): agentOrgId;
			Map<String, String> orgInfoMap = AjaxRequestHelper.getUserIdOgrCodeMap((userId == -1) , agentOrgId);
			List<String> partyIdList = (agentOrgId != -1) ? new ArrayList<String>(orgInfoMap.keySet()) : null;
			anaBean.setGameId(gameId);
			anaBean.setPartyId(userId+"");
			anaBean.setDrawName(drawName);
			anaBean.setDrawTime(dateTimeOfDraw);
			anaBean.setPartyIdList(partyIdList);
			anaBean.setDrawStatus(status);
			serReq = new ServiceRequest();
			serResp = new ServiceResponse();
			serReq.setServiceName(ServiceName.REPORTS_MGMT);
			serReq.setServiceMethod(ServiceMethodName.FETCH_DRAW_GAME_CONSOLIDATE_DATA_EXPAND);
			serReq.setServiceData(anaBean);
			IServiceDelegate  delegate =ServiceDelegate.getInstance(); 
			serResp=delegate.getResponse(serReq);

			String responseString = serResp.getResponseData().toString();
			Type elementType = new TypeToken<TicketWiseDataBean>() {}.getType();
			ticketBean = new Gson().fromJson(responseString, elementType);
			if (serResp.getIsSuccess()) {
				logger.info("response of getConsolidatedTicketsForDraw() came "+serResp.getIsSuccess()); 
				anaList =  ticketBean.getAnaBeanList();
				if(anaList != null && anaList.size() > 0){
				Iterator<AnalysisReportDrawBean> iterator = anaList.iterator();
					while (iterator.hasNext()) {
						boolean isOld = false;
						AnalysisReportDrawBean tempBean = iterator.next();
						String drawStatus = tempBean.getTicketStatus();
						String ticketNumber = tempBean.getTicketNumber();
						
						Timestamp purchaseTime = new Timestamp(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(tempBean.getTktPurchaseTime()).getTime());;

						if(uIdChangeDate.after(purchaseTime)){
							isOld =  true;
						}
						tempBean.setTicketNumber(("UNCLAIMED".equals(drawStatus)) ? 
																			((!isOld) ? 
																					("XXXX".concat(ticketNumber.substring(4, 18)))
																					: ticketNumber.substring(0, 14).concat("XXXX"))
																			: ticketNumber);
						tempBean.setPartyName(orgInfoMap.get(tempBean.getPartyId()));
					}
				}
			}else{
				if(ticketBean.getErrorCode() == LMSErrors.DRAW_DOES_NOT_EXISTS_ERROR_CODE){
					ticketBean.setErrorMessage(LMSErrors.DRAW_DOES_NOT_EXISTS_ERROR_MESSAGE);
				}else if(ticketBean.getErrorCode() == LMSErrors.DATA_ARCHIEVED_ERROR_CODE){
					ticketBean.setErrorMessage(LMSErrors.DATA_ARCHIEVED_ERROR_MESSAGE+ticketBean.getArchData());
				}else{
					ticketBean.setErrorMessage(LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
				}
				logger.info("response of getConsolidatedTicketsForDraw() came "+serResp.getIsSuccess());
			}
		} catch (Exception e) {
			e.printStackTrace();
			if(ticketBean == null){
				ticketBean = new TicketWiseDataBean();
				ticketBean.setErrorCode(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE);
				ticketBean.setErrorMessage(LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
			}
		}
		return ticketBean;
	}
	
	
	public List<BlockTicketUserBean> blockBunchTicket(int doneByUserId, String[] ticketNumberArray ,Timestamp updatedTime, String purpose , String[] status, String countryName){
		ServiceRequest serReq = null;
		ServiceResponse serResp = null;
		ArrayList<BlockTicketUserBean> blockTicketList = null;
		boolean isValid = true;
		try {
			logger.info("inside blockBunchTicket()");
			blockTicketList = new ArrayList<BlockTicketUserBean>();
			for(int i = 0;i < ticketNumberArray.length ; i++){
				String responseMessage = "";
				int barCodeCount = -1;
				String ticketNumber = ticketNumberArray[i];
				
				if(ticketNumber.length() == ConfigurationVariables.barcodeCount){
					barCodeCount = Integer.parseInt(Util.getBarCodeCountFromTicketNumber(ticketNumber));
					ticketNumber = Util.getTicketNumberForBarCode(ticketNumber);
				}
				int gameId = Util.getGameIdFromGameNumber(Util.getGamenoFromTktnumber(ticketNumber));
				if(gameId == 0){
					isValid = false;
					responseMessage = "Invalid Ticket";
				}
				int retUserId = Util.getUserIdFromTicket(ticketNumber);
				if(retUserId == 0){
					isValid = false;
					responseMessage = "Invalid Ticket";
				}
				if(!ticketNumber.isEmpty()){
					BlockTicketUserBean blockTicketUserBean = new BlockTicketUserBeanBuilder(ticketNumber).gameId(gameId).doneByUserId(doneByUserId).countryName(countryName)
					.barCodeCount(barCodeCount).status(status[i]).updatedTime(updatedTime).purpose(purpose).retUserId(retUserId).responseMessage(responseMessage).build();
					blockTicketList.add(blockTicketUserBean);
				}
			}
			if(isValid){
			blockTicketList.trimToSize();
			serReq = new ServiceRequest();
			serResp = new ServiceResponse();			
			serReq.setServiceName(ServiceName.DRAW_MGMT);
			serReq.setServiceMethod(ServiceMethodName.BLOCK_BUNCH_TICKET_IN_DGE);
			serReq.setServiceData(blockTicketList);
			IServiceDelegate  delegate =ServiceDelegate.getInstance(); 
			serResp=delegate.getResponse(serReq);
			if(serResp.getIsSuccess()){
				String responseString = serResp.getResponseData().toString();
				Type elementType = new TypeToken<List<BlockTicketUserBean>>() {}.getType();
				return new Gson().fromJson(responseString, elementType);	
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return blockTicketList;
	}	
	
	public List<BlockTicketUserBean> getTicketsToBlockUnblock(String criteria ,String ticketNo , int agentOrgId , int retOrgId ,int gameId , String status){
		ServiceRequest serReq = null;
		ServiceResponse serResp = null;
		ArrayList<Integer> userIdList = null;
		TicketSearchCriteriaBean bean =  null;
		Statement st=null;
		ResultSet rs=null;
		Map<Integer,String> map=null;
		Connection con=null;
		try {
			con=DBConnect.getConnection();
			logger.info("inside getTicketsToBlockUnblock()");
			bean =  new TicketSearchCriteriaBean();
			if ("2".equals(criteria)) {
				if (agentOrgId == -1 && retOrgId == -1) {
					userIdList = new ArrayList<Integer>();
				} else if (retOrgId == -1) {
					userIdList = AjaxRequestHelper.getUserIdList(agentOrgId,false);
				} else {
					userIdList = AjaxRequestHelper.getUserIdList(retOrgId, true);
				}
			}
			bean.setGameId(gameId);
			bean.setStatus(status);
			bean.setCriteria(criteria);
			bean.setUserIdList(userIdList);
			bean.setTicketNumber(ticketNo);
			serReq = new ServiceRequest();
			serResp = new ServiceResponse();
			serReq.setServiceName(ServiceName.DRAW_MGMT);
			serReq.setServiceMethod(ServiceMethodName.FETCH_TICKETS_TO_BLOCK_UNBLOCK_IN_DGE);
			serReq.setServiceData(bean);
			IServiceDelegate  delegate =ServiceDelegate.getInstance(); 
			serResp=delegate.getResponse(serReq);
			if(serResp.getIsSuccess()){
				String responseString = serResp.getResponseData().toString();
				Type elementType = new TypeToken<List<BlockTicketUserBean>>() {}.getType();
				List<BlockTicketUserBean>list = new Gson().fromJson(responseString, elementType);
				
			 st=con.createStatement();	
		     rs=st.executeQuery("select user_id,user_name from st_lms_user_master where  organization_type ='BO'");
			 map=new HashMap<Integer,String>();
				while(rs.next()){
				   map.put(rs.getInt("user_id"),rs.getString("user_name"));
				}
				for(BlockTicketUserBean temp:list){
					temp.setUserName(map.get(temp.getDoneByUserId()));
				}
				return list;
			}
			
			
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public ArrayList<BlockTicketUserBean> unblockBunchTicket(int doneByUserId, String[] ticketNumberArray ,Timestamp updatedTime, String[] purpose,String countryName){
		ServiceRequest serReq = null;
		ServiceResponse serResp = null;
		ArrayList<BlockTicketUserBean> blockTicketList = null;
		try {
			logger.info("inside unblockSpecificTicket()");
			blockTicketList = new ArrayList<BlockTicketUserBean>();
			ArrayList<String> purposeList =  new ArrayList<String>(Arrays.asList(purpose));
			List<String> a = new ArrayList<String>();
			a.add("-1");
			purposeList.removeAll(a);
			purposeList.trimToSize();
			for(int i = 0;i < ticketNumberArray.length ; i++){
				int barCodeCount = -1;
				String ticketNumber = ticketNumberArray[i];
				
				if(ticketNumber.length() == ConfigurationVariables.barcodeCount){
					barCodeCount = Integer.parseInt(Util.getBarCodeCountFromTicketNumber(ticketNumber));
					ticketNumber = Util.getTicketNumberForBarCode(ticketNumber);
				}
				int gameId = Util.getGameIdFromGameNumber(Util.getGamenoFromTktnumber(ticketNumber));
				if(gameId == 0){
					throw new LMSException(LMSErrors.INVALID_TICKET_ERROR_CODE , LMSErrors.INVALID_TICKET_ERROR_MESSAGE);
				}
				int retUserId = Util.getUserIdFromTicket(ticketNumber);
				BlockTicketUserBean blockTicketUserBean = new BlockTicketUserBeanBuilder(ticketNumber).gameId(gameId).doneByUserId(doneByUserId).countryName(countryName)
				.barCodeCount(barCodeCount).purpose(purposeList.get(i)).updatedTime(updatedTime).retUserId(retUserId).build();
				blockTicketList.add(blockTicketUserBean);
			}
			blockTicketList.trimToSize();
			serReq = new ServiceRequest();
			serResp = new ServiceResponse();			
			serReq.setServiceName(ServiceName.DRAW_MGMT);
			serReq.setServiceMethod(ServiceMethodName.BLOCK_UNBLOCK_TICKET_IN_DGE);
			serReq.setServiceData(blockTicketList);
			IServiceDelegate  delegate =ServiceDelegate.getInstance(); 
			serResp=delegate.getResponse(serReq);
			if(serResp.getIsSuccess()){
				String responseString = serResp.getResponseData().toString();
				Type elementType = new TypeToken<List<BlockTicketUserBean>>() {}.getType();
				return new Gson().fromJson(responseString, elementType);	
			}
		}catch (LMSException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public DGConsolidateGameDataBean fetchPromoDrawGameDataForLMS(DrawDataBean drawDataBean, String raffleTktType) {
		DGConsolidateGameDataBean consolidateBean = new DGConsolidateGameDataBean();
		ArrayList<Integer> retUserIdList = new ArrayList<Integer>();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			if (drawDataBean.getAgentOrgId() > 0) {
				con = DBConnect.getConnection();
				pstmt = con.prepareStatement("select user_id from st_lms_user_master where organization_id in (select organization_id from st_lms_organization_master where parent_id =? and organization_type=?)");
				pstmt.setInt(1, drawDataBean.getAgentOrgId());
				pstmt.setString(2, "RETAILER");
				rs = pstmt.executeQuery();
				while (rs.next()) {
					retUserIdList.add(rs.getInt("user_id"));
				}
				logger.info("Retailers For Agent: " + drawDataBean.getAgentOrgId() + ":- " + retUserIdList);
			}
			drawDataBean.setRetailerUserIdList(retUserIdList);
			// get Data For LMS ;

			ServiceRequest serReq = new ServiceRequest();
			ServiceResponse serResp = new ServiceResponse();
			serReq.setServiceName(ServiceName.REPORTS_MGMT);
			serReq.setServiceMethod(ServiceMethodName.FETCH_PROMO_DRAW_GAME_CONSOLIDATE_DATA);
			serReq.setServiceData(drawDataBean);
			IServiceDelegate delegate = ServiceDelegate.getInstance();
			serResp = delegate.getResponse(serReq);
			if (serResp.getIsSuccess()) {
				// consolidateBean=(DGConsolidateGameDataBean)serResp.getResponseData();
				Type type = new TypeToken<DGConsolidateGameDataBean>() {}.getType();
				consolidateBean = (DGConsolidateGameDataBean) new Gson().fromJson((JsonElement) serResp.getResponseData(), type);
				logger.info("Got Draw Game Consolidate Data " + consolidateBean);

				String gameType = Util.getGameType(drawDataBean.getGameNo());
				if ("RAFFLE".equalsIgnoreCase(gameType)) {
					// to cut last four digit in case of raffle GAME
					if ("ORIGINAL".equalsIgnoreCase(raffleTktType)) {
						DGConsolidateDrawBean reportDrawBean = null;
						for (int i = 0; i < consolidateBean.getDrawDataBeanList().size(); i++) {
							reportDrawBean = consolidateBean.getDrawDataBeanList().get(i);
							String winRes = reportDrawBean.getWinningResult();
							if (winRes != null) {
								String[] winResultArr = winRes.split(",");
								StringBuilder finalresult = new StringBuilder("");
								for (int j = 0; j < winResultArr.length; j++) {
									String winResWithRpCnt = winResultArr[j];
									if (winResWithRpCnt != null && !"null".equalsIgnoreCase(winResWithRpCnt)) {
										int length = winResWithRpCnt.length();
										if (length == ConfigurationVariables.tktLenA || length == ConfigurationVariables.tktLenB) {
											finalresult.append(winResWithRpCnt.substring(0, length - 4));
											finalresult.append("xxxx");
											finalresult.append(",");
										}
									}
								}
								if (finalresult != null && !"".equals(finalresult.toString()) && !"0".equals(finalresult.toString())) {
									finalresult.deleteCharAt(finalresult.length() - 1);
								}
								reportDrawBean.setWinningResult(finalresult.toString());
							}
						}
					} else {
						// for swap result with sale ticket number in case of reference ticket
						getDisplayTktNumber(consolidateBean); // in case of reference ticket
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (con != null) {
					con.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e) {

				e.printStackTrace();
			}
		}
		return consolidateBean;
	}
	
	public Map<Integer, RankWiseWinningReportBean> fetchRankWiseWinningData(DrawDataBean drawDataBean){
		Map<Integer,RankWiseWinningReportBean> dataMap = null;
		Set<Integer> removableKeys = new HashSet<Integer>();
		
		try{
				Connection con = DBConnect.getConnection();
				ServiceRequest serReq = new ServiceRequest();
				ServiceResponse serResp = new ServiceResponse();
				serReq.setServiceName(ServiceName.REPORTS_MGMT);
				serReq.setServiceMethod(ServiceMethodName.FETCH_RANK_WISE_WINNING_DATA);
				serReq.setServiceData(drawDataBean);
				IServiceDelegate delegate = ServiceDelegate.getInstance();
				serResp = delegate.getResponse(serReq);
				
				if (serResp.getIsSuccess()) {
					Type type = new TypeToken<Map<Integer, RankWiseWinningReportBean>>() {}.getType();
					dataMap =  new Gson().fromJson((JsonElement) serResp.getResponseData(), type);
					logger.info("Got Rank wise Consolidate Data " + dataMap);
					
					for(Map.Entry<Integer, RankWiseWinningReportBean> itr : dataMap.entrySet()) {
							int sum = 0;
							for(Map.Entry<Integer, Integer> itrInner : itr.getValue().getWinningTktMap().entrySet()){
								sum += itrInner.getValue();
							}
							itr.getValue().setSum(sum);
							
							if(sum > 0){
						
								String names[] = fetchName(itr.getValue().getRetailerOrgId(),con).split("#");							
								itr.getValue().setRetailerName(names[0].toUpperCase());
								itr.getValue().setAgentName(fetchAgentName(Integer.parseInt(names[1]),con).toUpperCase());
							}else{
								removableKeys.add(itr.getKey());
							}
											
					}
					
					dataMap.keySet().removeAll(removableKeys);
				}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return dataMap;
	}
	
	public String fetchName(int id,Connection con) throws SQLException{
		String orgName = null;
		String qry = "select a.name, parent_id from st_lms_organization_master  a inner join st_lms_user_master b on a.organization_id = b.organization_id where b.user_id = "+id;
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(qry);
		if(rs.next()){
			orgName =  rs.getString("name")+"#"+rs.getString("parent_id");
		}
		return orgName;
	}
	
	public String fetchAgentName(int id,Connection con) throws SQLException{
		String orgName = null;
		String qry = "select name from st_lms_organization_master where organization_id = "+id;
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(qry);
		if(rs.next()){
			orgName =  rs.getString("name");
		}
		return orgName;
	}
	
	public List<MtnCustomerCenterBean> fetchMerchantWiseTxns(String merchantName, String mobileNbr, String startDate, String endDate) throws Exception {
		List<MtnCustomerCenterBean> mtnCustomerCenterBeans= null;
		JsonObject reqObj = new JsonObject();
		ServiceResponse sRes = new ServiceResponse();
		ServiceRequest sReq = new ServiceRequest();
		sReq.setServiceName(ServiceName.REPORTS_MGMT);
		sReq.setServiceMethod(ServiceMethodName.FETCH_MERCHANT_WISE_TRANSACTION_DATA);

		reqObj.addProperty("merchantName", merchantName);
		reqObj.addProperty("mobileNbr", mobileNbr);
		reqObj.addProperty("startDate", startDate);
		reqObj.addProperty("endDate", endDate);

		sReq.setServiceData(reqObj);
		IServiceDelegate delegate = ServiceDelegate.getInstance();
		sRes = delegate.getResponse(sReq);

		if(sRes.getIsSuccess()) {
			Type type = new TypeToken<List<MtnCustomerCenterBean>>(){}.getType();
			mtnCustomerCenterBeans= new Gson().fromJson((JsonElement) sRes.getResponseData(), type);
		}
		return mtnCustomerCenterBeans;
	}
	
	
	 public StringBuilder getAutumaticResultSubmissionResponse(DrawDataBean drawDataBean){
	  
		 StringBuilder submissionResp=new StringBuilder();
		 HttpURLConnection connection = null;
		 try{
		 URL url = new URL(Utility.serverDGWURL+"/com/skilrock/drawManager/drawResultSubmit.action?gameName="+drawDataBean.getGameName()+"&drawId="+drawDataBean.getDrawId()+"");
		 connection = (HttpURLConnection)url.openConnection();
		 connection.setRequestMethod("POST");  
		 connection.setUseCaches(false);
	     connection.setDoOutput(true);
	      //Send request
	        DataOutputStream wr = new DataOutputStream (connection.getOutputStream());
	        wr.close();
	     
	        InputStream is = connection.getInputStream();
	        BufferedReader rd = new BufferedReader(new InputStreamReader(is));
	        String line;
	        while((line = rd.readLine()) != null) {
	        	submissionResp.append(line);
	        	submissionResp.append('\r');
	        }
	        rd.close();
		 }
		 catch(Exception e){
			 JSONObject jsonResponse = new JSONObject();
			 jsonResponse.put("isSuccess",false);
			 jsonResponse.put("responseMessage","Internal system error !!!");
			 submissionResp.append(jsonResponse);
		 }
		 finally
		 {
			 if(connection != null) {
			      connection.disconnect(); 
			    }
		 }
		 return submissionResp;  
	 }
}

   