package com.skilrock.lms.web.drawGames.drawMgmt;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.PromoGameBean;
import com.skilrock.lms.beans.RankWiseWinningReportBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.CommonValidation;
import com.skilrock.lms.common.Utility;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.CommonMethods;
import com.skilrock.lms.common.utility.LMSUtility;
import com.skilrock.lms.coreEngine.drawGames.common.DGErrorMsg;
import com.skilrock.lms.coreEngine.drawGames.drawMgmt.DrawGameMgmtHelper;
import com.skilrock.lms.coreEngine.drawGames.playMgmt.DrawGameRPOSHelper;
import com.skilrock.lms.coreEngine.ola.common.OLAUtility;
import com.skilrock.lms.dge.beans.AnalysisBean;
import com.skilrock.lms.dge.beans.AnalysisReportDrawBean;
import com.skilrock.lms.dge.beans.BlockTicketUserBean;
import com.skilrock.lms.dge.beans.CancelTicketBean;
import com.skilrock.lms.dge.beans.DGConsolidateDrawBean;
import com.skilrock.lms.dge.beans.DGConsolidateGameDataBean;
import com.skilrock.lms.dge.beans.DrawDataBean;
import com.skilrock.lms.dge.beans.DrawScheduleBean;
import com.skilrock.lms.dge.beans.DrawScheduleBeanResult;
import com.skilrock.lms.dge.beans.FortunePurchaseBean;
import com.skilrock.lms.dge.beans.KenoPurchaseBean;
import com.skilrock.lms.dge.beans.LottoPurchaseBean;
import com.skilrock.lms.dge.beans.ManualWinningBean;
import com.skilrock.lms.dge.beans.ResultSubmitBean;
import com.skilrock.lms.dge.beans.SchedulerBean;
import com.skilrock.lms.dge.beans.TicketWiseDataBean;
import com.skilrock.lms.dge.gameconstants.FortuneThreeConstants;
import com.skilrock.lms.dge.gameconstants.FortuneTwoConstants;
import com.skilrock.lms.web.drawGames.common.DrawGameRPOS;
import com.skilrock.lms.web.drawGames.common.Util;
import com.skilrock.lms.web.reportsMgmt.common.ReportUtility;

import net.sf.json.JSONObject;


/**
 * 
 * @author Gaurav Ujjwal
 * 
 *         <pre>
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
public class DrawGameMgmt extends ActionSupport implements ServletRequestAware,ServletResponseAware {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static void main(String[] args) throws Exception {
		// weeklyReport();
	}

	private String agentOrgName;
	private int agentOrgId;
	private String cancelMoneyStatus;
	private int cardType[];
	private String changeType;
	private String draw_day;
	private String[] draw_freeze_time;
	private String[] draw_time;
	private int drawId;
	private int[] drawIds;
	private ArrayList<DrawScheduleBeanResult> drawScheduleBeanResultList;
	private String drawType[];
	private String end_Date;
	private FortunePurchaseBean fortuneBean;
	private int freezeTime;
	private String fromDate;
	private String fromHour;
	private String fromMin;
	private String fromSec;
	private String gameName;
	private String date;
	private String drawIdString;
	private String action;
	private String gameNo;
	private String gameType;
	private String invoker;
	private KenoPurchaseBean kenoPurchaseBean;
	Log logger = LogFactory.getLog(DrawGameMgmt.class);
	private LottoPurchaseBean lottoPurchaseBean;
	private String message;
	String performStatus[];
	private int postponeTime;
	private String[] retName;
	private int retId;
	private int retOrgId;
	private int transNo;
	private String[] reasons ;
	private String reason;
	
	private String archDate;
	private String depDate;
	private  int gameId ;
	private Map<Integer,List<PromoGameBean>> promoGameBeanMap;
	private int promoGameId;
	private int  saleGameId;
	private int promoTicekts;
	private HttpServletResponse response;
	private List<AnalysisReportDrawBean> analist;
	private List<BlockTicketUserBean> blockTicketUserBeanList;
	private HashMap<Integer, String> drawGameInfo;
	private String valueToSend;
	
	private String stateCode;
	private String CityCode;
	private Map<String, String> stateMap;
	private String priv;
	private Map<Integer,RankWiseWinningReportBean> rankWinningData= null;
	
	@Override
	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}

	public HttpServletResponse getResponse() {
		return response;
	}
	
	public Map<String, String> getStateMap() {
		return stateMap;
	}

	public void setStateMap(Map<String, String> stateMap) {
		this.stateMap = stateMap;
	}

	public String getStateCode() {
		return stateCode;
	}

	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}

	public String getCityCode() {
		return CityCode;
	}

	public void setCityCode(String cityCode) {
		CityCode = cityCode;
	}

	public String getDepDate() {
		return depDate;
	}

	public void setDepDate(String depDate) {
		this.depDate = depDate;
	}

	public String getArchDate() {
		return archDate;
	}

	public void setArchDate(String archDate) {
		this.archDate = archDate;
	}

	public int getRetId() {
		return retId;
	}

	public void setRetId(int retId) {
		this.retId = retId;
	}

	public int getTransNo() {
		return transNo;
	}

	public void setTransNo(int transNo) {
		this.transNo = transNo;
	}

	public String getPriv() {
		return priv;
	}

	public void setPriv(String priv) {
		this.priv = priv;
	}

	private String retOrgName;
	private String search_type;
	private HttpServletRequest servletRequest;
	private HttpServletResponse servletResponse;
	private String start_date;
	private String sale_date;
	private String status;
	private String subHold;
	private String ticketNo;
	private String ticketNum;
	private String[] ticketNumArr;
	private String toDate;
	private String toHour;
	private String toMin;
	private String toSec;
	private String type;
	private String dateOfDraw;
	private String[] ticketStatusArr;

	private int user1;
	private int user2;
	
	private String winNumber;
	private String macNumber;

	private Integer winNumbers[];
	private Integer macNumbers[];

	private int winNumSize;

	private int msgId;
	private String drawName;
		
	private int[] user;
	private String userStr;
	
	private ArrayList<DGConsolidateDrawBean> drawDataBeanList;
	private HashMap<Integer, String> commSerList;
	private HashMap<Integer, String> drawSerList;
	private Map<Integer, String> olaSerList;

	public String actionData() {
		HttpSession session = servletRequest.getSession();
		for (int s : drawIds) {
			logger.debug("--------" + s);
		}

		logger.debug("changeType: " + changeType);
		DrawScheduleBean drawScheduleBean = (DrawScheduleBean) session
				.getAttribute("DRAW_SCH_LIST");

		return SUCCESS;
	}

	public String actionOnHold() throws Exception {
		HttpSession session = servletRequest.getSession();
		List<Integer> drawIdList = new ArrayList<Integer>();
		for (int element : drawIds) {
			drawIdList.add(element);
		}
		logger.debug(drawIdList + "--Before Action On HOld--" + subHold);

	//	gameNo = gameNo.substring(0, gameNo.lastIndexOf("-"));
		DrawScheduleBean drawScheduleBean = new DrawScheduleBean();
		drawScheduleBean.setDrawIdList(drawIdList);
		drawScheduleBean.setGameNo(Integer.parseInt(gameNo));
		drawScheduleBean.setStatus(subHold);
		DrawGameMgmtHelper helper = new DrawGameMgmtHelper();
		SchedulerBean schedulerBean = (SchedulerBean) helper
				.actionOnHold(drawScheduleBean);
		logger.debug("SchedulelIst Size: "
				+ schedulerBean.getDrawScheduleList().size());

		// schedulerBean.setActionType("CANCELLED");
		session.setAttribute("RESULT_LIST", schedulerBean);
		logger.debug("msg--" + schedulerBean.getResponseMessage());
		logger.debug("msg--" + schedulerBean.getGameNo());
		logger.debug("After--" + new Date());

		return SUCCESS;
	}

	@SuppressWarnings("unchecked")
	public String cancelBlkTicketAtBO() throws Exception {
		logger.debug("Inside cancelBlkTicketAtBO");
		
		ServletContext sc = null;
		HttpSession session = null;
		String refMerchantId = null;
		UserInfoBean userInfoBean = null;
		DrawGameMgmtHelper helper = null;
		ArrayList<CancelTicketBean> canTktList = null;
		Map<Integer, Map<Integer, String>> drawIdTableMap = null;
		
		try{
		validateInputForCancel(null, null,ticketNumArr,servletRequest,true);
		sc = ServletActionContext.getServletContext();
		session = servletRequest.getSession();
		userInfoBean = (UserInfoBean) session.getAttribute("USER_INFO");
		refMerchantId = (String) sc.getAttribute("REF_MERCHANT_ID");
		drawIdTableMap = (Map<Integer, Map<Integer, String>>) sc.getAttribute("drawIdTableMap");
		logger.debug(" drawIdTableMap in cancel ticket " + drawIdTableMap);
		helper = new DrawGameMgmtHelper();
		canTktList = helper.cancelBlkTicketAtBO(Integer.parseInt((String)sc.getAttribute("CANCEL_DURATION")),"true".equalsIgnoreCase((String)sc.getAttribute("IS_CANCEL_DURATION")),ticketNumArr, getReasons(), userInfoBean,search_type, gameNo, refMerchantId,
						drawIdTableMap,(String)sc.getAttribute("DRAW_GAME_CANCELLATION_CHARGES"));
		}catch (LMSException e) {
			return ERROR;
		}catch (Exception e) {
			return ERROR;
		}
		servletRequest.setAttribute("canTktList", canTktList);
		return SUCCESS;

	}

	public String cancelDraw() throws Exception {
		logger.debug("-----IN CANCEL DRAW-----");

		HttpSession session = servletRequest.getSession();
		UserInfoBean userInfoBean = (UserInfoBean) session
		.getAttribute("USER_INFO");
		List<Integer> drawIdList = new ArrayList<Integer>();
		for (int element : drawIds) {
			drawIdList.add(element);
		}
	//	gameNo = gameNo.substring(0, gameNo.lastIndexOf("-"));
		DrawScheduleBean drawScheduleBean = new DrawScheduleBean();
		drawScheduleBean.setDrawIdList(drawIdList);
		drawScheduleBean.setGameNo(Integer.parseInt(gameNo));
		drawScheduleBean.setAction(action);
		logger.debug("selected status in cancel action ::" + status);

		drawScheduleBean.setStatus(status);
		DrawGameMgmtHelper helper = new DrawGameMgmtHelper();
		SchedulerBean schedulerBean = (SchedulerBean) helper
				.cancelDraw(drawScheduleBean, userInfoBean);
		logger.debug(schedulerBean.getDrawScheduleList().size());

		// schedulerBean.setActionType("CANCELLED");
		session.setAttribute("RESULT_LIST", schedulerBean);
		logger.debug("msg--" + schedulerBean.getResponseMessage());
		logger.debug("msg--" + schedulerBean.getGameNo());
		logger.debug("msg--" + schedulerBean.getFreezeOrPerform().toString());
		logger.debug("After--" + new Date());

		return SUCCESS;
	}

	public String freezeDraw() throws Exception {
		logger.debug("-----IN FREEZE DRAW-----");
		// logger.debug("LOGGED_IN_USERS map is: " + currentUserSessionMap);
		// logger.debug("user name is: " + userName);

		HttpSession session = servletRequest.getSession();
		UserInfoBean userInfoBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		List<Integer> drawIdList = new ArrayList<Integer>();
		for (int element : drawIds) {
			drawIdList.add(element);
		}
	//	gameNo = gameNo.substring(0, gameNo.lastIndexOf("-"));
		DrawScheduleBean drawScheduleBean = new DrawScheduleBean();
		drawScheduleBean.setDrawIdList(drawIdList);
		drawScheduleBean.setGameNo(Integer.parseInt(gameNo));
		logger.debug("selected status in freeze action ::" + status);

		drawScheduleBean.setStatus(status);
		DrawGameMgmtHelper helper = new DrawGameMgmtHelper();
		SchedulerBean schedulerBean = (SchedulerBean) helper
				.freezeDraw(drawScheduleBean, userInfoBean);
		logger.debug(schedulerBean.getDrawScheduleList().size());

		// schedulerBean.setActionType("CANCELLED");
		ServletContext sc = LMSUtility.sc;
		Map<String, HttpSession> currentUserSessionMap = (Map<String, HttpSession>) sc
				.getAttribute("LOGGED_IN_USERS");
		session =currentUserSessionMap.get(userInfoBean.getUserName().toLowerCase());
		session.setAttribute("RESULT_LIST", schedulerBean);
		logger.debug("msg--" + schedulerBean.getResponseMessage());
		logger.debug("msg--" + schedulerBean.getGameNo());
		logger.debug("msg--" + schedulerBean.getFreezeOrPerform().toString());
		logger.debug("After--" + new Date());

		return SUCCESS;
	}
	
	@SuppressWarnings("unchecked")
	public String cancelTicketAtBO() throws Exception {
		logger.debug("Inside cancelTicketAtBO");
		CancelTicketBean cancelTicketBean =null;
		ServletContext sc = null;
		HttpSession session =null;
		UserInfoBean userInfoBean = null;
		String refMerchantId = null;
		String cancelChannel = null;
		String reason = null;
		String cancellationCharges =null;
		DrawGameMgmtHelper helper = null;
		Map<Integer, Map<Integer, String>> drawIdTableMap =null;

		try{
		cancelTicketBean = new CancelTicketBean();
		reason = servletRequest.getParameter("reason");
		validateInputForCancel(ticketNo, reason,null,null,false);
		
		sc = ServletActionContext.getServletContext();
		session = servletRequest.getSession();
		userInfoBean = (UserInfoBean) session.getAttribute("USER_INFO");

		cancelChannel = "LMS_Web";
		refMerchantId = (String) sc.getAttribute("REF_MERCHANT_ID");
		cancellationCharges = (String)sc.getAttribute("DRAW_GAME_CANCELLATION_CHARGES");
		drawIdTableMap = (Map<Integer, Map<Integer, String>>) sc.getAttribute("drawIdTableMap");

		logger.debug(" drawIdTableMap in cancel ticket " + drawIdTableMap);
		cancelTicketBean = new CancelTicketBean();
		cancelTicketBean.setReason(reason);
		cancelTicketBean.setTicketNo(ticketNo);
		cancelTicketBean.setCancelChannel(cancelChannel);
		cancelTicketBean.setRefMerchantId(refMerchantId);
		cancelTicketBean.setDrawIdTableMap(drawIdTableMap);
		cancelTicketBean.setCancelDuration(Integer.parseInt((String)sc.getAttribute("CANCEL_DURATION")));
		cancelTicketBean.setCancelDuaraion("true".equalsIgnoreCase((String)sc.getAttribute("IS_CANCEL_DURATION")));
		
		helper = new DrawGameMgmtHelper();
		cancelTicketBean = helper.cancelTicketAtBO(cancelTicketBean, userInfoBean,search_type, gameNo,cancellationCharges);
		servletRequest.setAttribute("CANCEL_BEAN", cancelTicketBean);
		
		}catch (LMSException e) {
			cancelTicketBean.setErrMsg(e.getErrorMessage());
			return ERROR;
		}catch (Exception e) {
			cancelTicketBean.setErrMsg(LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
			return ERROR;
		}
		return SUCCESS;
	}

	
	private void validateInputForCancel(String ticketNumber,String reason,String[] ticketNumArr,HttpServletRequest servletRequest,boolean isBulkCancel) throws LMSException{
		if(!isBulkCancel){
		if (ticketNo == null || !CommonValidation.isNumericWithoutDot(ticketNo,false)) 
		throw new LMSException(LMSErrors.INVALID_TICKET_ERROR_CODE,LMSErrors.INVALID_TICKET_ERROR_MESSAGE);
		
		if("-1".equals(reason) || "".equals(reason))
			throw new LMSException(LMSErrors.INVALID_REASON_FOR_CANCEL_TICKET_DATA_ERROR_CODE,LMSErrors.INVALID_REASON_FOR_CANCEL_TICKET_DATA_ERROR_MESSAGE);
		}
		else{
			if (ticketNumArr.length > 0) {
				this.reasons = new String[ticketNumArr.length];
				for(int i=0; i<ticketNumArr.length; i++)
				{
					this.reasons[i] = servletRequest.getParameter("reason_"+ticketNumArr[i]);
					if(this.reasons[i]==null || this.reasons[i].equals("-1") || !CommonValidation.isNumericWithoutDot(ticketNumArr[i],false))
						throw new LMSException(LMSErrors.INVALID_REASON_FOR_CANCEL_TICKET_DATA_ERROR_CODE,LMSErrors.INVALID_REASON_FOR_CANCEL_TICKET_DATA_ERROR_MESSAGE);
				}
			}else{
				
			}
		}
		
	}
	
	public String changeFreezeTime() throws Exception {
		logger.debug("Inside changeFreezeTime");
		HttpSession session = servletRequest.getSession();
		UserInfoBean userInfoBean = (UserInfoBean) session
		.getAttribute("USER_INFO");
		List<Integer> drawIdList = new ArrayList<Integer>();
		for (int element : drawIds) {
			drawIdList.add(element);
		}
	//	gameNo = gameNo.substring(0, gameNo.lastIndexOf("-"));
		logger.debug("draw ids----" + drawIdList);

		DrawScheduleBean drawScheduleBean = new DrawScheduleBean();
		drawScheduleBean.setDrawIdList(drawIdList);
		drawScheduleBean.setStatus(status);
		drawScheduleBean.setGameNo(Integer.parseInt(gameNo));
		drawScheduleBean.setFreezeTime(freezeTime);
		logger.debug("Before--" + new Date());

		DrawGameMgmtHelper helper = new DrawGameMgmtHelper();
		SchedulerBean schedulerBean = (SchedulerBean) helper
				.changeFreezeTime(drawScheduleBean, userInfoBean);
		session.setAttribute("RESULT_LIST", schedulerBean);

		for (int i = 0; i < schedulerBean.getDrawScheduleList().size(); i++) {
			drawScheduleBean = schedulerBean.getDrawScheduleList().get(i);
			logger.debug("---------result--"
					+ drawScheduleBean.getUpdatedFreezeTime());

		}
		return SUCCESS;
	}

	public String checkNextDraw() throws Exception {
		HttpSession session = servletRequest.getSession();
		logger.debug("Iside checkNestDraw");
		logger.debug("Before--" + new Date());

		DrawScheduleBean drawScheduleBean = new DrawScheduleBean();
		// drawScheduleBean.setDrawId(7);
		drawScheduleBean.setGameNo(102);
		DrawGameMgmtHelper helper = new DrawGameMgmtHelper();
		Object message = helper.checkNextDraw(drawScheduleBean);
		logger.debug("msg---------" + message);
		logger.debug("After--" + new Date());

		return SUCCESS;
	}

	public String fetchAdvMsgData() throws Exception {
		HttpSession session = servletRequest.getSession();
		DrawGameMgmtHelper helper = new DrawGameMgmtHelper();
		session
				.setAttribute("RET_MAP", helper
						.fetchAdvMessageData(search_type));
		logger.debug("----selection Map--" + session.getAttribute("RET_MAP"));

		return SUCCESS;

	}

	/*public String getAdvMsgDataForEdit() throws Exception {
		HttpSession session = servletRequest.getSession();
		DrawGameMgmtHelper helper = new DrawGameMgmtHelper();
		session.setAttribute("ADV_MSG_MAP", helper.getAdvMsgForEdit());
		logger.debug("----Adv Msg Map--" + session.getAttribute("ADV_MSG_MAP"));
		return SUCCESS;
	}*/

	/*public String editAdvMsgData() throws Exception {
		HttpSession session = servletRequest.getSession();
		UserInfoBean userBean = (UserInfoBean) session.getAttribute("USER_INFO");
		DrawGameMgmtHelper helper = new DrawGameMgmtHelper();
		boolean status = helper.editAdvMsgStatus(msgId, userBean.getUserId(),userBean.getUserOrgId());
		if (!status) {
			return "applicationError";
		}
		Util.advMsgDataMap = new DrawGameRPOSHelper().getAdvMsgDataMap();
		return SUCCESS;
	}*/

	public String fetchDrawData() throws Exception {
		HttpSession session = servletRequest.getSession();
		logger.debug(gameNo + "Before--" + new Date());
		DrawDataBean drawDataBean = new DrawDataBean();
		drawDataBean.setGameNo(Integer.parseInt(gameNo));
		drawDataBean.setAgentOrgId(agentOrgId);
		System.out.println(agentOrgId);
		drawDataBean.setFromDate(start_date + " 00:00:00");
		// The to date has been Hard Code to Avoid Use of calendar and appended
		// with 23:59:59 to achieve the data
		// corresponding to the midnight of given date
		drawDataBean.setToDate(end_Date + " 23:59:59");
		drawDataBean.setDrawName(drawName);
		DrawGameMgmtHelper helper = new DrawGameMgmtHelper();
		// helper.fetchDrawData(drawDataBean);
		logger.debug("startDate is" + start_date);
		logger.debug("endDate is" + end_Date);
		ServletContext sc = ServletActionContext.getServletContext();
		String raffleTktType = (String) sc.getAttribute("raffle_ticket_type");
		session.setAttribute("DRAW_DATA_LIST", helper.fetchDrawData(
				drawDataBean, raffleTktType));
		return SUCCESS;
	}
	// start 
	public String fetch15minDrawData() throws Exception {
		HttpSession session = servletRequest.getSession();
		logger.debug(gameNo + "Before--" + new Date());
		DrawDataBean drawDataBean = new DrawDataBean();
		drawDataBean.setGameNo(Integer.parseInt(gameNo));
		if (dateOfDraw != null) {
			String lastDate = CommonMethods.getLastArchDate();
			System.out.println("last archieve date"+lastDate);
			if(dateOfDraw.compareTo(lastDate)<=0){
				message="For Details Please Choose start date after "+lastDate;
				return SUCCESS;
			}
		}
		drawDataBean.setFromDate(dateOfDraw + " 00:00:00");
		
		drawDataBean.setToDate(dateOfDraw + " 23:59:59");
		drawDataBean.setDrawName(drawName);

		DrawGameMgmtHelper helper = new DrawGameMgmtHelper();
		logger.debug("startDate is" + dateOfDraw);
	 //	logger.debug("endDate is" + end_Date);
		ServletContext sc = ServletActionContext.getServletContext();
		String raffleTktType = (String) sc.getAttribute("raffle_ticket_type");
		//commented to display all merchant data 31DEC2013
		//String refMerchantId = (String) sc.getAttribute("REF_MERCHANT_ID");
		String refMerchantId = "ALL";
		
		drawDataBean.setMerchantId(refMerchantId);
		session.setAttribute("DRAW_DATA_LIST", helper.fetch15minDrawData(
				drawDataBean, raffleTktType));
		return SUCCESS;
	}
	
	// end added by neeraj jain
	
	public String fetchDrawDataMenu() throws Exception {
		HttpSession session = servletRequest.getSession();
		ServletContext sc = session.getServletContext();
		
		session.setAttribute("presentDate", new java.sql.Date(new Date()
				.getTime()).toString());
		session.setAttribute("DRAWGAME_LIST", ReportUtility.fetchDrawDataMenu());
		session.setAttribute("OLA_DATA_LIST", OLAUtility.getOlaWalletDataMap());
		//SimpleDateFormat formatOld = new SimpleDateFormat("dd-MM-yyyy");
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date strtDate = format.parse(CommonMethods.getLastArchDate());
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(strtDate);
		cal1.add(Calendar.DATE, 1); 
		setArchDate(format.format(cal1.getTime()));
		setDepDate(CommonMethods.convertDateInGlobalFormat((String)sc.getAttribute("DEPLOYMENT_DATE"), "yyyy-mm-dd", (String)sc.getAttribute("date_format")));
		stateMap = CommonMethods.fetchStateList();
		return SUCCESS;
	}
	
	public String rankWiseReportDrawMenu() throws Exception {
		HttpSession session = servletRequest.getSession();
		ServletContext sc = session.getServletContext();
		session.setAttribute("presentDate", new java.sql.Date(new Date().getTime()).toString());
		Map<Integer, String> gameMap = new HashMap<Integer,String>();
		gameMap.put(Util.getGameId("TwelveByTwentyFour"), ReportUtility.fetchDrawDataMenu().get(Util.getGameId("TwelveByTwentyFour")));
		session.setAttribute("DRAWGAME_LIST", gameMap);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date strtDate = format.parse(CommonMethods.getLastArchDate());
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(strtDate);
		cal1.add(Calendar.DATE, 1); 
		setArchDate(format.format(cal1.getTime()));
		setDepDate(CommonMethods.convertDateInGlobalFormat((String)sc.getAttribute("DEPLOYMENT_DATE"), "yyyy-mm-dd", (String)sc.getAttribute("date_format")));
		stateMap = CommonMethods.fetchStateList();
		return SUCCESS;
	}

	public String fetchActiveDrawDataMenu() throws Exception {
		commSerList=null;
		drawSerList=null;
		olaSerList=null;
		try {
			setCommSerList(ReportUtility.fetchActiveCategoriesCommSerData());
			setDrawSerList(ReportUtility.fetchActiveGameDrawDataMenu());
			setOlaSerList(OLAUtility.getOlaWalletDataMap());
		} catch (Exception e) {
			logger.error(" EXCEPTION : " +e );
		}
		return SUCCESS;
	}
	
	/*public String fetchActiveDrawDataMenu() throws Exception {
		HttpSession session = servletRequest.getSession();
		
		session.setAttribute("presentDate", new java.sql.Date(new Date()
				.getTime()).toString());
		session.setAttribute("DRAWGAME_LIST", ReportUtility.fetchActiveGameDrawDataMenu());
		session.setAttribute("OLA_DATA_LIST", OLAUtility.getOlaWalletDataMap());
		return SUCCESS;
	}*/
	
	public String fetchSubDrawResult() throws Exception {
		
		StringBuilder sd=null;
		HttpSession session = null;
		UserInfoBean userBean = null;
		Timestamp toTimeStamp = null;
		Timestamp fromTimeStamp = null;
		DrawGameMgmtHelper helper =  null;
		SimpleDateFormat dateFormat = null;
		DrawScheduleBean drawScheduleBean =null;
		try {
			session = servletRequest.getSession();
			userBean = (UserInfoBean) session.getAttribute("USER_INFO");
			dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			
		if (fromDate != null && fromDate.length() == 0) 
				fromDate = null;
		if (toDate != null && toDate.length() == 0) 
				toDate = null;

		setGameName(Util.getGameName(Integer.parseInt(gameNo)));
		if (fromDate != null) {
				sd =new StringBuilder(fromDate).append(" ").append(fromHour).append(":").append(fromMin).append(":").append(fromSec);
				fromTimeStamp=new Timestamp(dateFormat.parse(sd.toString()).getTime());
			}
			if (toDate != null) {
				sd=new StringBuilder(toDate).append(" ").append(toHour).append(":").append(toMin).append(":").append(toSec);
				toTimeStamp=new Timestamp(dateFormat.parse(sd.toString()).getTime());
			}
		}catch (ParseException e) {
			logger.error("Parsing Exception : " + e);
		}
		catch (Exception e) {
			logger.error("General Exception : " + e);
		}
		drawScheduleBean = new DrawScheduleBean();
		drawScheduleBean.setDraw_id(drawId);
		drawScheduleBean.setEndDate(toTimeStamp);
		drawScheduleBean.setStartDate(fromTimeStamp);
		drawScheduleBean.setGameNo(Integer.parseInt(gameNo));
		drawScheduleBean.setUserId(userBean.getUserId());
		helper = new DrawGameMgmtHelper();
		if (!helper.authorizeUser(userBean.getUserId())) 
			return "unauthorize";

		session.setAttribute("DRAW_SCH_LIST", helper.fetchSubDrawResult(drawScheduleBean));
		return SUCCESS;
	}

	public String getAgentOrgName() {
		return agentOrgName;
	}

	public String getCancelMoneyStatus() {
		return cancelMoneyStatus;
	}

	public int[] getCardType() {
		return cardType;
	}

	public String getChangeType() {
		return changeType;
	}

	public String getDraw_day() {
		return draw_day;
	}

	public String[] getDraw_freeze_time() {
		return draw_freeze_time;
	}

	public String[] getDraw_time() {
		return draw_time;
	}

	public int getDrawId() {
		return drawId;
	}

	public int[] getDrawIds() {
		return drawIds;
	}

	public String getDrawSchdule() throws Exception {
		
		logger.debug("Before--" + new Date());
		HttpSession session = servletRequest.getSession();
		Calendar fromDrawCal = Calendar.getInstance();
		Calendar toDrawCal = Calendar.getInstance();
		Calendar startTimeForDraw = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		Date frdate = null;
		Date todateObj = null;
		Timestamp fromTimeStamp = null;
		Timestamp toTimeStamp = null;
		List<Integer> drawIdList = new ArrayList<Integer>();
		gameName = Util.getGameName(gameId);
		logger.debug("status === " + status + "--draw ids--" + drawIds);
		if (fromDate != null) {
			if (fromDate.length() == 0) {
				fromDate = null;
			} else {
				logger.debug("len-----" + fromDate.length());
			}
		}
		if (toDate != null) {
			if (toDate.length() == 0) {
				toDate = null;
			}
		}
		if (drawId == 0) {
			drawIdList.add(0);
		} else {
			drawIdList.add(drawId);
		}
		logger.debug("from date---" + fromDate + "--toDate---" + toDate
				+ "----draw id---" + drawId + "----g no--" + gameNo);

		
		try {
			if (fromDate != null) {
				frdate = format.parse(fromDate);
				fromDrawCal.setTime(frdate);
				fromDrawCal.set(Calendar.HOUR, Integer.parseInt(fromHour));
				fromDrawCal.set(Calendar.MINUTE, Integer.parseInt(fromMin));
				fromDrawCal.set(Calendar.SECOND, Integer.parseInt(fromSec));
				fromTimeStamp = new Timestamp(fromDrawCal.getTime().getTime());
			}
			if (toDate != null) {
				todateObj = format.parse(toDate);
				toDrawCal.setTime(todateObj);
				toDrawCal.set(Calendar.HOUR, Integer.parseInt(toHour));
				toDrawCal.set(Calendar.MINUTE, Integer.parseInt(toMin));
				toDrawCal.set(Calendar.SECOND, Integer.parseInt(toSec));
				toTimeStamp = new Timestamp(toDrawCal.getTime().getTime());
			}
			if(todateObj.compareTo(frdate)<0){
				System.out.println("From Date Should be less than To Date");
				addActionMessage(getText("label.invalid.date.todate.shouldbe.after.fromdate"));
				session.setAttribute("DRAW_SCH_LIST", null);
				if ("CLAIM HOLD".equalsIgnoreCase(status.trim())) {
					return "performed";
				} else if ("FREEZE".equalsIgnoreCase(status.trim())) {
					return "freeze";
				} else if ("ACTIVE".equalsIgnoreCase(status.trim())
						|| "INACTIVE".equalsIgnoreCase(status.trim())) {
					return "active";
				} else if ("HOLD".equalsIgnoreCase(status.trim())) {
					return "hold";
				} else {
					return INPUT;
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		ServletContext sc = ServletActionContext.getServletContext();
		int DrawScheduleResultDelay =Integer.parseInt( (String) sc.getAttribute("Draw_Schedule_Result_Delay"));
		startTimeForDraw.add(Calendar.HOUR_OF_DAY,DrawScheduleResultDelay);
		DrawScheduleBean drawScheduleBean = new DrawScheduleBean();
		drawScheduleBean.setDraw_day(draw_day);
		drawScheduleBean.setDrawIdList(drawIdList);
		drawScheduleBean.setGameNo(gameId);
		drawScheduleBean.setStartDate(fromTimeStamp);
		drawScheduleBean.setEndDate(toTimeStamp);
		drawScheduleBean.setStatus(status);
		drawScheduleBean.setStartTimeForDraws(new Timestamp(startTimeForDraw.getTime().getTime()));
		DrawGameMgmtHelper helper = new DrawGameMgmtHelper();
		session.setAttribute("DRAW_SCH_LIST", helper
				.getDrawSchdule(drawScheduleBean));
		logger.debug("After--" + new Date());

		if ("CLAIM ALLOW".equalsIgnoreCase(status.trim())) {
			return "allow";
		}else if ("CLAIM HOLD".equalsIgnoreCase(status.trim())) {
			return "performed";
		} else if ("FREEZE".equalsIgnoreCase(status.trim())) {
			return "freeze";
		} else if ("ACTIVE".equalsIgnoreCase(status.trim())
				|| "INACTIVE".equalsIgnoreCase(status.trim())) {
			return "active";
		} else if ("HOLD".equalsIgnoreCase(status.trim())) {
			return "hold";
		} else {
			return INPUT;
		}

	}

	public ArrayList<DrawScheduleBeanResult> getDrawScheduleBeanResultList() {
		return drawScheduleBeanResultList;
	}

	public String[] getDrawType() {
		return drawType;
	}

	public String getEnd_Date() {
		return end_Date;
	}

	public FortunePurchaseBean getFortuneBean() {
		return fortuneBean;
	}

	public int getFreezeTime() {
		return freezeTime;
	}

	public String getFromDate() {
		return fromDate;
	}

	public String getFromHour() {
		return fromHour;
	}

	public String getFromMin() {
		return fromMin;
	}

	public String getFromSec() {
		return fromSec;
	}

	public String getGameData() {
		logger.debug("Inside getGameData");
		if(!("GHANA".equalsIgnoreCase(Utility.getPropertyValue("COUNTRY_DEPLOYED")))) {
			String actionName = ActionContext.getContext().getName();
			Map<Integer, String> gameMap = ReportUtility.fetchActiveGameDrawDataMenu();
			if("bo_dg_machineNumberEntry".equals(actionName)) {
				int raffleGameId = Util.getGameId("RaffleGame1");
				gameMap.remove(raffleGameId);
	
				int kenoSevenGameId = Util.getGameId("KenoSeven");
				gameMap.remove(kenoSevenGameId);
				
				int kenoNineGameId = Util.getGameId("KenoNine");
				gameMap.remove(kenoNineGameId);
			}
		}
		HttpSession session = servletRequest.getSession();
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		Date date = Calendar.getInstance().getTime();
		String today = format.format(date);
		session.setAttribute("CURR_TIME", today);
		session.setAttribute("DRAWGAME_LIST", ReportUtility.fetchActiveGameDrawDataMenu());
		session.setAttribute("INVOKER", invoker);
		return SUCCESS;
	}

	public String getGameDataForTrackTx(){
		HttpSession session = getServletRequest().getSession();
		session.setAttribute("DRAWGAME_LIST", ReportUtility.fetchActiveGameDrawDataMenu());
		return SUCCESS;
	}
	
	
	public String getGameDataDec() {
		invoker = "ManualDeclare";
		return getGameData();
	}

	public String getGameDataWin() {
		invoker = "ManualEntry";
		return getGameData();
	}

	public String getGameName() {
		return gameName;
	}

	public String getGameNo() {
		return gameNo;
	}

	public int getGameId() {
		return gameId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public String getGameType() {
		return gameType;
	}

	public KenoPurchaseBean getKenoPurchaseBean() {
		return kenoPurchaseBean;
	}

	public String getLastTenTicket() throws NumberFormatException, LMSException {
		DrawGameMgmtHelper helper = new DrawGameMgmtHelper();
		Map ticketMap = helper.getLastTenTicket(Integer.parseInt(gameNo),
				retOrgId);
		HttpSession session = servletRequest.getSession();
		session.setAttribute("ticketMap", ticketMap);
		return SUCCESS;
	}

	public LottoPurchaseBean getLottoPurchaseBean() {
		return lottoPurchaseBean;
	}

	/**
	 * This method fetches draws with draw_status as 'ACTIVE'.
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getManualDeclareData() throws Exception {
		logger.debug("INside getManualDeclareData");
		
		logger.debug("Before--" + new Date());
		HttpSession session = servletRequest.getSession();
		Calendar fromDrawCal = Calendar.getInstance();
		Calendar toDrawCal = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		Date frdate = null;
		Date todateObj = null;
		Timestamp fromTimeStamp = null;
		Timestamp toTimeStamp = null;
		if (fromDate != null) {
			if (fromDate.length() == 0) {
				fromDate = null;
			} else {
				logger.debug("len-----" + fromDate.length());

			}
		}
		if (toDate != null) {
			if (toDate.length() == 0) {
				toDate = null;
			}
		}
		
		gameName = Util.getGameName(Integer.parseInt(gameNo));
		try {
			if (fromDate != null) {
				frdate = format.parse(fromDate);
				fromDrawCal.setTime(frdate);
				fromDrawCal.set(Calendar.HOUR, Integer.parseInt(fromHour));
				fromDrawCal.set(Calendar.MINUTE, Integer.parseInt(fromMin));
				fromDrawCal.set(Calendar.SECOND, Integer.parseInt(fromSec));
				fromTimeStamp = new Timestamp(fromDrawCal.getTime().getTime());
			}
			if (toDate != null) {
				todateObj = format.parse(toDate);
				toDrawCal.setTime(todateObj);
				toDrawCal.set(Calendar.HOUR, Integer.parseInt(toHour));
				toDrawCal.set(Calendar.MINUTE, Integer.parseInt(toMin));
				toDrawCal.set(Calendar.SECOND, Integer.parseInt(toSec));
				toTimeStamp = new Timestamp(toDrawCal.getTime().getTime());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		DrawScheduleBean drawScheduleBean = new DrawScheduleBean();
		drawScheduleBean.setDraw_id(drawId);
		drawScheduleBean.setGameNo(Integer.parseInt(gameNo));
		drawScheduleBean.setStartDate(fromTimeStamp);
		drawScheduleBean.setEndDate(toTimeStamp);

		DrawGameMgmtHelper helper = new DrawGameMgmtHelper();
		session.setAttribute("DRAW_SCH_LIST", helper
				.getManualDeclareData(drawScheduleBean));
		return SUCCESS;
	}

	/**
	 * This method fetches draws with draw_status as 'FREEZE' if Perform Status
	 * is "ALL" else all draws with perform_status = 'PMEP'
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getManualEntryData() throws Exception {
		logger.debug("INside getManualEntryData");
		logger.debug("action call------------------");
		logger.debug("Before--" + new Date());

		HttpSession session = servletRequest.getSession();
		UserInfoBean userBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		Calendar fromDrawCal = Calendar.getInstance();
		Calendar toDrawCal = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		Date frdate = null;
		Date todateObj = null;
		Timestamp fromTimeStamp = null;
		Timestamp toTimeStamp = null;
		if (fromDate != null) {
			if (fromDate.length() == 0) {
				fromDate = null;
			} else {
				logger.debug("len-----" + fromDate.length());

			}
		}
		if (toDate != null) {
			if (toDate.length() == 0) {
				toDate = null;
			}
		}
		if (fromHour != null && fromMin != null && fromSec != null
				&& toHour != null && toMin != null && toSec != null) {

			if (fromHour.length() == 0) {
				fromHour = "00";
			}
			if (fromMin.length() == 0) {
				fromMin = "00";
			}
			if (fromSec.length() == 0) {
				fromSec = "00";
			}
			if (toHour.length() == 0) {
				toHour = "23";
			}
			if (toMin.length() == 0) {
				toMin = "59";
			}
			if (toSec.length() == 0) {
				toSec = "59";
			}
		}
		
		gameName = Util.getGameName(gameId);
		try {
			if (fromDate != null) {
				frdate = format.parse(fromDate);
				fromDrawCal.setTime(frdate);
				fromDrawCal.set(Calendar.HOUR, Integer.parseInt(fromHour));
				fromDrawCal.set(Calendar.MINUTE, Integer.parseInt(fromMin));
				fromDrawCal.set(Calendar.SECOND, Integer.parseInt(fromSec));
				fromTimeStamp = new Timestamp(fromDrawCal.getTime().getTime());
			}
			if (toDate != null) {
				todateObj = format.parse(toDate);
				toDrawCal.setTime(todateObj);
				toDrawCal.set(Calendar.HOUR, Integer.parseInt(toHour));
				toDrawCal.set(Calendar.MINUTE, Integer.parseInt(toMin));
				toDrawCal.set(Calendar.SECOND, Integer.parseInt(toSec));
				toTimeStamp = new Timestamp(toDrawCal.getTime().getTime());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		DrawScheduleBean drawScheduleBean = new DrawScheduleBean();
		drawScheduleBean.setDraw_id(drawId);
		//drawScheduleBean.setGameNo(Integer.parseInt(gameNo));
		drawScheduleBean.setGameId(gameId);
		drawScheduleBean.setStartDate(fromTimeStamp);
		drawScheduleBean.setEndDate(toTimeStamp);
		drawScheduleBean.setStatus(performStatus[0].equals("ALL") ? "FREEZE"
				: null);
		System.out.println("heej" + performStatus[0]);
		drawScheduleBean.setUserId(userBean.getUserId());
		drawScheduleBean.setPerformStatus(performStatus);
		DrawGameMgmtHelper helper = new DrawGameMgmtHelper();
		session.setAttribute("DRAW_SCH_LIST", helper
				.getManualEntryData(drawScheduleBean));
		return SUCCESS;
	}

	public String getManualMachineNumberEntryData() throws Exception {
		System.out.println("getManualMachineNumberEntryData()");
		logger.debug("INside getManualEntryData");
		logger.debug("action call------------------");
		logger.debug("Before--" + new Date());

		HttpSession session = servletRequest.getSession();
		UserInfoBean userBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		Calendar fromDrawCal = Calendar.getInstance();
		Calendar toDrawCal = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		Date frdate = null;
		Date todateObj = null;
		Timestamp fromTimeStamp = null;
		Timestamp toTimeStamp = null;
		if (fromDate != null) {
			if (fromDate.length() == 0) {
				fromDate = null;
			} else {
				logger.debug("len-----" + fromDate.length());

			}
		}
		if (toDate != null) {
			if (toDate.length() == 0) {
				toDate = null;
			}
		}

		
		gameName = Util.getGameName(Integer.parseInt(gameNo));
		try {
			if (fromDate != null) {
				frdate = format.parse(fromDate);
				fromDrawCal.setTime(frdate);
				fromDrawCal.set(Calendar.HOUR, Integer.parseInt(fromHour));
				fromDrawCal.set(Calendar.MINUTE, Integer.parseInt(fromMin));
				fromDrawCal.set(Calendar.SECOND, Integer.parseInt(fromSec));
				fromTimeStamp = new Timestamp(fromDrawCal.getTime().getTime());
			}
			if (toDate != null) {
				todateObj = format.parse(toDate);
				toDrawCal.setTime(todateObj);
				toDrawCal.set(Calendar.HOUR, Integer.parseInt(toHour));
				toDrawCal.set(Calendar.MINUTE, Integer.parseInt(toMin));
				toDrawCal.set(Calendar.SECOND, Integer.parseInt(toSec));
				toTimeStamp = new Timestamp(toDrawCal.getTime().getTime());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		DrawScheduleBean drawScheduleBean = new DrawScheduleBean();
		drawScheduleBean.setDraw_id(drawId);
		drawScheduleBean.setGameNo(Integer.parseInt(gameNo));
		drawScheduleBean.setStartDate(fromTimeStamp);
		drawScheduleBean.setEndDate(toTimeStamp);
		drawScheduleBean.setStatus(performStatus[0].equals("ALL") ? "FREEZE"
				: null);
		System.out.println("hello" + (performStatus[0]));
		drawScheduleBean.setUserId(userBean.getUserId());
		drawScheduleBean.setPerformStatus(performStatus);
		DrawGameMgmtHelper helper = new DrawGameMgmtHelper();
		session.setAttribute("DRAW_MACHINE_NUMBER_SCH_LIST", helper
				.getManualMachineNumberEntryData(drawScheduleBean));
		// session.setAttribute("DRAW_MACHINE_NUMBER_SCH_LIST", helper
		// .getManualMachineNumberEntryData(drawScheduleBean));
		return SUCCESS;
	}

	public String getMessage() {
		return message;
	}

	public String[] getPerformStatus() {
		return performStatus;
	}

	public int getPostponeTime() {
		return postponeTime;
	}

	public String[] getRetName() {
		return retName;
	}

	public String getRetOrgName() {
		return retOrgName;
	}

	public String getSearch_type() {
		return search_type;
	}

	public String getStart_date() {
		return start_date;
	}

	public String getStatus() {
		return status;
	}

	public String getSubHold() {
		return subHold;
	}

	public String getTicketNo() {
		return ticketNo;
	}

	public String getTicketNum() {
		return ticketNum;
	}

	public String[] getTicketNumArr() {
		return ticketNumArr;
	}

	public String getToDate() {
		return toDate;
	}

	public String getToHour() {
		return toHour;
	}

	public String getToMin() {
		return toMin;
	}

	public String getToSec() {
		return toSec;
	}

	public String getType() {
		return type;
	}

	public int getUser1() {
		return user1;
	}

	public int getUser2Id() {
		return user2;
	}

	public String getWinNumber() {
		return winNumber;
	}

	public Integer[] getWinNumbers() {
		return winNumbers;
	}

	public int getWinNumSize() {
		return winNumSize;
	}

	public Integer[] getMacNumbers() {
		return macNumbers;
	}

	public void setMacNumbers(Integer[] macNumbers) {
		this.macNumbers = macNumbers;
	}
	
	public String getMacNumber() {
		return macNumber;
	}

	public void setMacNumber(String macNumber) {
		this.macNumber = macNumber;
	}

	public String holdDraw() throws Exception {
		logger.debug("Inside holdDraw");
		HttpSession session = servletRequest.getSession();
		List<Integer> drawIdList = new ArrayList<Integer>();
		for (int element : drawIds) {
			drawIdList.add(element);
		}
	//	gameNo = gameNo.substring(0, gameNo.lastIndexOf("-"));
		DrawScheduleBean drawScheduleBean = new DrawScheduleBean();
		drawScheduleBean.setDrawIdList(drawIdList);
		drawScheduleBean.setGameNo(Integer.parseInt(gameNo));
		drawScheduleBean.setStatus(status);
		DrawGameMgmtHelper helper = new DrawGameMgmtHelper();
		SchedulerBean schedulerBean = (SchedulerBean) helper
				.holdDraw(drawScheduleBean);
		logger.debug("Inside holdDraw");

		// schedulerBean.setActionType("CANCELLED");
		session.setAttribute("RESULT_LIST", schedulerBean);
		logger.debug("msg--" + schedulerBean.getResponseMessage());
		logger.debug("msg--" + schedulerBean.getGameNo());
		logger.debug("msg--" + schedulerBean.getFreezeOrPerform().toString());
		logger.debug("After--" + new Date());

		return SUCCESS;
	}

	public String initiateDrawSchedule() throws Exception {
		HttpSession session = servletRequest.getSession();
		logger.debug("Before--" + new Date());
		DrawScheduleBean drawScheduleBean = new DrawScheduleBean();
		drawScheduleBean.setGameNo(101);
		DrawGameMgmtHelper helper = new DrawGameMgmtHelper();
		helper.initiateDrawSchdule(drawScheduleBean);
		logger.debug("After--" + new Date());
		return SUCCESS;

	}

	public String manualWiningEntry() {
		HttpSession session = servletRequest.getSession();
		ArrayList<DrawScheduleBeanResult> list = (ArrayList<DrawScheduleBeanResult>) session
				.getAttribute("DRAW_SCH_LIST");
		drawScheduleBeanResultList = new ArrayList<DrawScheduleBeanResult>();
		DrawScheduleBeanResult checkedDrawBean = null;
		int i = -1;
		for (int drawId : drawIds) {
			checkedDrawBean = new DrawScheduleBeanResult();
			checkedDrawBean.setDrawId(drawId);
			if ((i = list.indexOf(checkedDrawBean)) > -1) {
				drawScheduleBeanResultList.add(list.get(i));
			}
		}
		setGameNo(Util.getGameNumberFromGameId(gameId)+"");
		setBonusBallEnable(Util.getLmsGameMap().get(gameId).getBonusBallEnable());
		logger.debug("drawScheduleBeanResultList ==== "
				+ drawScheduleBeanResultList);
		return SUCCESS;
	}

	public String manualWiningMachineNumberEntry() {
		HttpSession session = servletRequest.getSession();
		ArrayList<DrawScheduleBeanResult> list = (ArrayList<DrawScheduleBeanResult>) session
				.getAttribute("DRAW_MACHINE_NUMBER_SCH_LIST");
		drawScheduleBeanResultList = new ArrayList<DrawScheduleBeanResult>();
		DrawScheduleBeanResult checkedDrawBean = null;
		int i = -1;
		for (int drawId : drawIds) {
			checkedDrawBean = new DrawScheduleBeanResult();
			checkedDrawBean.setDrawId(drawId);
			if ((i = list.indexOf(checkedDrawBean)) > -1) {
				drawScheduleBeanResultList.add(list.get(i));
			}
		}
		logger.debug("drawScheduleBeanResultList ==== "
				+ drawScheduleBeanResultList);
		return SUCCESS;
	}

	/**
	 * This method sets the perform_status of a selected ACTIVE draws to 'PMEP'
	 * so that they are not selected for scheduling by scheduler in DrawGameWeb.
	 * 
	 * @return
	 * @throws Exception
	 */
	public String performManualDeclareEntry() throws Exception {
		HttpSession session = servletRequest.getSession();
		List<Integer> drawIdList = new ArrayList<Integer>();
		for (int element : drawIds) {
			drawIdList.add(element);
		}
		DrawScheduleBean drawScheduleBean = new DrawScheduleBean();
		drawScheduleBean.setDrawIdList(drawIdList);
		drawScheduleBean.setGameNo(Integer.parseInt(gameNo));
		String[] performStatus = { "PMEP" };
		drawScheduleBean.setPerformStatus(performStatus);
		DrawGameMgmtHelper helper = new DrawGameMgmtHelper();

		session.setAttribute("MESSAGE", helper
				.performManualDeclareEntry(drawScheduleBean));
		return SUCCESS;
	}

	/**
	 * This method invokes the game respective PerformDraw class of
	 * DrawGameEngine to perform the manual draw.
	 * 
	 * @return
	 * @throws Exception
	 */
	public String performManualWinningEntry() throws Exception {
		logger.debug("Start Result Submission Process");
		HttpSession session = servletRequest.getSession();
		UserInfoBean userBean = (UserInfoBean) session.getAttribute("USER_INFO");
		ManualWinningBean mwBean = new ManualWinningBean();
		mwBean.setGameId(gameId);
		mwBean.setDrawIds(drawIds);
		mwBean.setDrawType(drawType);
		System.out.println("Draw Type" + mwBean.getDrawType());
		
		// For Verification Engine
		
		/*ArrayList<DrawScheduleBeanResult> list = (ArrayList<DrawScheduleBeanResult>) session
		.getAttribute("DRAW_SCH_LIST");
		DrawScheduleBeanResult checkedDrawBean = null, drawBean = null;
		DrawGameRPOSHelper dgHelper = new DrawGameRPOSHelper();
		int i = -1;
		for (int drawId : drawIds) {
			checkedDrawBean = new DrawScheduleBeanResult();
			drawBean = new DrawScheduleBeanResult();
			checkedDrawBean.setDrawId(drawId);
			if ((i = list.indexOf(checkedDrawBean)) > -1) {
				drawBean = list.get(i);
				dgHelper.getUnsoldTkts(Integer.parseInt(gameNo), getStartDate(drawBean.getFreezeDateTime()), drawBean.getFreezeDateTime());
			}
		}*/
		
		mwBean.setCardType(cardType);
		System.out.println("Card Type" + cardType);
		if (winNumbers != null) {
			List<Integer> tmpList=new ArrayList<Integer>();
			for(Integer winNum:winNumbers){
				if(winNum!=null){
					tmpList.add(winNum);
				}
			}
			winNumbers=tmpList.toArray(new Integer[0]);
			winNumSize = winNumbers.length;
		}
		mwBean.setWinningNumbers(winNumbers);
		System.out.println("winning numberas" + mwBean.getWinningNumbers());
		//winNumSize = winNumbers.length;
		mwBean.setWinNumSize(winNumSize);
		mwBean.setUserId(userBean.getUserId());
		mwBean.setMachineNumbers(macNumbers);
		DrawGameMgmtHelper helper = new DrawGameMgmtHelper();
		session.setAttribute("MESSAGE", helper
				.performManualWinningEntry(mwBean));
		DrawGameRPOS.onStartNewData();
		//logger.debug("Returning Success");
		return SUCCESS;
	}

	public String performManualWinningMachineNumberEntry() throws Exception {
		logger.debug("123456");
		HttpSession session = servletRequest.getSession();
		UserInfoBean userBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		ManualWinningBean mwBean = new ManualWinningBean();
		mwBean.setGameNumber(gameNo);
		mwBean.setDrawIds(drawIds);
		mwBean.setDrawType(drawType);
		System.out.println("sfd" + mwBean.getDrawType());

		System.out.println("wdraw type" + drawType);
		mwBean.setCardType(cardType);
		System.out.println("sd" + cardType);
		mwBean.setWinningNumbers(winNumbers);
		System.out.println("winning numberas" + mwBean.getWinningNumbers());
		mwBean.setWinNumSize(winNumSize);
		mwBean.setUserId(userBean.getUserId());
		DrawGameMgmtHelper helper = new DrawGameMgmtHelper();
		session.setAttribute("MESSAGE", helper
				.performManualWinningMachineNumberEntry(mwBean));
		DrawGameRPOS.onStartNewData();
		return SUCCESS;
	}

	public String postponeDraw() throws Exception {
		logger.debug("Inside postponeDraw");
		logger.debug("Before--" + new Date());

		HttpSession session = servletRequest.getSession();
		UserInfoBean userInfoBean = (UserInfoBean) session
		.getAttribute("USER_INFO");
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		Date todateObj = null;
		Timestamp toTimeStamp = null;
		Calendar toDrawCal = Calendar.getInstance();
		DrawScheduleBean drawScheduleBean = new DrawScheduleBean();
		List<Integer> drawIdList = new ArrayList<Integer>();
		for (int element : drawIds) {
			drawIdList.add(element);
		}
		//gameNo = gameNo.substring(0, gameNo.lastIndexOf("-"));

		if (toDate != null) {
			if (toDate.length() == 0) {
				toDate = null;
			}
		}
		try {

			if (toDate != null) {
				todateObj = format.parse(toDate);
				toDrawCal.setTime(todateObj);
				toDrawCal.set(Calendar.HOUR, Integer.parseInt(toHour));
				toDrawCal.set(Calendar.MINUTE, Integer.parseInt(toMin));
				toDrawCal.set(Calendar.SECOND, Integer.parseInt(toSec));
				toTimeStamp = new Timestamp(toDrawCal.getTime().getTime());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		drawScheduleBean.setGameNo(Integer.parseInt(gameNo));
		drawScheduleBean.setDrawIdList(drawIdList);
		drawScheduleBean.setPostponeMin(postponeTime);
		drawScheduleBean.setPostponeDate(toTimeStamp);
		logger.debug("selected status in postpone action ::" + status);

		drawScheduleBean.setStatus(status);
		DrawGameMgmtHelper helper = new DrawGameMgmtHelper();
		SchedulerBean schedulerBean = (SchedulerBean) helper
				.postponeDraw(drawScheduleBean,userInfoBean);
		session.setAttribute("RESULT_LIST", schedulerBean);
		logger.debug("After--" + new Date());

		return SUCCESS;
	}

	public String rankChkDraw() throws Exception {
		HttpSession session = servletRequest.getSession();
		UserInfoBean userInfoBean = (UserInfoBean) session
		.getAttribute("USER_INFO");
		List<Integer> drawIdList = new ArrayList<Integer>();
		for (int element : drawIds) {
			drawIdList.add(element);
		}
		logger.debug(drawIdList + "--Before Action On HOld--" + subHold);

	//	gameNo = gameNo.substring(0, gameNo.lastIndexOf("-"));
		DrawScheduleBean drawScheduleBean = new DrawScheduleBean();
		drawScheduleBean.setDrawIdList(drawIdList);
		drawScheduleBean.setGameNo(Integer.parseInt(gameNo));
		drawScheduleBean.setStatus(subHold);
		DrawGameMgmtHelper helper = new DrawGameMgmtHelper();
		SchedulerBean schedulerBean = (SchedulerBean) helper
				.rankChkDraw(drawScheduleBean,userInfoBean);
		logger.debug("schedulerBean.getDrawScheduleList().size()"
				+ schedulerBean.getDrawScheduleList().size());

		// schedulerBean.setActionType("CANCELLED");
		session.setAttribute("RESULT_LIST", schedulerBean);
		logger.debug("msg--" + schedulerBean.getResponseMessage());
		logger.debug("msg--" + schedulerBean.getGameNo());
		logger.debug("After--" + new Date());

		return SUCCESS;
	}

	public String reprintTicket() {
		logger.debug("-------BO Ticket Reprint-------");
		HttpSession session = servletRequest.getSession();
		logger.debug("Inside reprintTicket");
		// UserInfoBean userInfoBean = (UserInfoBean) session
		// .getAttribute("USER_INFO");
		logger.debug("Before--" + new Date());
		logger.debug("Before--" + new Date());
		String errMsg = "Last Transaction Not Sale";
		DrawGameRPOSHelper helper = new DrawGameRPOSHelper();
		Object gameBean = null;
		UserInfoBean userInfoBean = new UserInfoBean();

		userInfoBean.setOrgName(Util.getOrgName(Util.fetchOrgIdOfUser(Integer
				.parseInt(ticketNum.substring(0, 4)))));
		userInfoBean.setUserOrgId(Util.fetchOrgIdOfUser(Integer
				.parseInt(ticketNum.substring(0, 4))));
		userInfoBean.setUserId(Integer.parseInt(ticketNum.substring(0, 3)));
		session.setAttribute("boReprintRetOrgName", userInfoBean.getOrgName());
		gameBean = helper.reprintTicket(userInfoBean, ticketNum);
		logger.debug("******gameBean::" + gameBean);

		if (gameBean instanceof FortunePurchaseBean) {
			logger.debug(" FortunePurchaseBean reprint ");
			FortunePurchaseBean fortuneBean = (FortunePurchaseBean) gameBean;
			setFortuneBean(fortuneBean);
			session.setAttribute("REPRINT_TYPE", Util.getGameName(
					fortuneBean.getGame_no()).toUpperCase()
					+ "_REPRINT");
			return SUCCESS;
		} else if (gameBean instanceof LottoPurchaseBean) {

			LottoPurchaseBean lottoPurchaseBean = (LottoPurchaseBean) gameBean;
			setLottoPurchaseBean(lottoPurchaseBean);
			session.setAttribute("REPRINT_TYPE", Util.getGameName(
					lottoPurchaseBean.getGame_no()).toUpperCase()
					+ "_REPRINT");
			return SUCCESS;
		} else if (gameBean instanceof KenoPurchaseBean) {
			logger.debug(" kenoPurchaseBean reprint ");
			KenoPurchaseBean kenoPurchaseBean = (KenoPurchaseBean) gameBean;
			setKenoPurchaseBean(kenoPurchaseBean);
			session.setAttribute("REPRINT_TYPE", Util.getGameName(
					kenoPurchaseBean.getGame_no()).toUpperCase()
					+ "_REPRINT");
			return SUCCESS;
		} else if (gameBean instanceof String
				&& "RG_RPERINT".equals(gameBean.toString())) {
			errMsg = DGErrorMsg.buyErrMsg(gameBean.toString());
			session.setAttribute("REPRINT_TYPE_ERROR", errMsg);
			session.setAttribute("boReprintErrMsg", errMsg);
			return ERROR;
		} else if (gameBean instanceof String) {
			errMsg = (String) gameBean;
			session.setAttribute("REPRINT_TYPE_ERROR", errMsg);
			session.setAttribute("boReprintErrMsg", errMsg);
			return ERROR;
		}
		session.setAttribute("boReprintErrMsg", errMsg);

		return ERROR;
	}

	public String resultUserAssignEdit() {
		HttpSession session = servletRequest.getSession();
		UserInfoBean userBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		DrawGameMgmtHelper helper = new DrawGameMgmtHelper();
		if (!helper.authorizeUser(userBean.getUserId())) {
			return "unauthorize";
		}
		
		if(userStr != null){
			String[] userIdArr = userStr.split(",");
			user = new int[userIdArr.length];
			for (int i = 0; i < userIdArr.length; i++) {
				user[i] = Integer.parseInt(userIdArr[i]);
			}
		}
		
		int temp = 0;
		for (int i = 0; i < user.length; i++) {
			if(user[i] == -1){
				for (int j = i+1; j < user.length; j++) {
					if(user[j] != -1){
						temp = user[j];
						user[j] = user[i];
						user[i] = temp;
					}
				}
			}
		}
		
		session.setAttribute("BO_USER_ASSIGN_MESSAGE", helper.resultUserAssignEdit(gameId, user));
		return SUCCESS;
	}

	public String resultUserAssignFetch() {
		HttpSession session = servletRequest.getSession();
		UserInfoBean userBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		DrawGameMgmtHelper helper = new DrawGameMgmtHelper();
		List<ResultSubmitBean> userList = helper.resultUserAssignFetch();
		if (!helper.authorizeUser(userBean.getUserId())) {
			return "unauthorize";
		}
		resultUserAssignMenu();
		session.setAttribute("USER_LIST", userList);
		return SUCCESS;
	}

	public String resultUserAssignMenu() {
		HttpSession session = servletRequest.getSession();
		UserInfoBean userBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		DrawGameMgmtHelper helper = new DrawGameMgmtHelper();
		//List<ResultSubmitBean> userList = helper.resultUserAssignFetch();
		if (!helper.authorizeUser(userBean.getUserId())) {
			return "unauthorize";
		}
		int resultSubmitUsersCount = Integer.parseInt((String)ServletActionContext.getServletContext().getAttribute("RESULT_SUBMIT_USERS_COUNT"));
		Map<Integer, String> boUsersMap = helper.fetchBoUserList(userBean.getUserId(), userBean.getUserType());
		session.setAttribute("BO_USER_LIST", boUsersMap);
		int resultSubmitUsersDB = boUsersMap.size();
		
		ArrayList<Integer> usersCount = new ArrayList<Integer>();
		int min = resultSubmitUsersCount;
		if(resultSubmitUsersCount > resultSubmitUsersDB){
			min = resultSubmitUsersDB;
		}
		
		for (int i = 0; i < min; i++) {
			usersCount.add(99);
		}
		
		session.setAttribute("BO_RESULT_ASSIGN_USERS_COUNT", usersCount);
		getGameData();
		return SUCCESS;
	}

	public String resultUserAssignSave() {
		HttpSession session = servletRequest.getSession();
		int gameId = Integer.parseInt(this.gameNo.split("-")[0]);
		int gameNo = Util.getGameNumberFromGameId(gameId);
		
		int temp = 0;
		for (int i = 0; i < user.length; i++) {
			if(user[i] == -1){
				for (int j = i+1; j < user.length; j++) {
					if(user[j] != -1){
						temp = user[j];
						user[j] = user[i];
						user[i] = temp;
					}
				}
			}
		}
		
		session.setAttribute("BO_USER_ASSIGN_MESSAGE", new DrawGameMgmtHelper().resultUserAssignSave(gameId, gameNo, user));
		return SUCCESS;
	}

	public String saveDrawResult() throws Exception {
		logger.debug(drawId + "******" + gameNo + "**********" + winNumber);
		int cardTypeVal = 0;
		String[] macNum = null;
		if (winNumbers != null) {
			List<Integer> tmpList=new ArrayList<Integer>();
			for(Integer winNum:winNumbers){
				if(winNum!=null){
					tmpList.add(winNum);
				}
			}
			winNumbers=tmpList.toArray(new Integer[0]);
		}
		if (winNumber.indexOf(",") != -1) {
			String[] winNum = winNumber.trim().split(",");
			winNumbers = new Integer[winNum.length];
			if ("FortuneTwo".equalsIgnoreCase(Util.getGameName(Integer
					.parseInt(gameNo.trim())))) {
				for (int i = 0; i < winNum.length; i++) {
					winNumbers[i] = Arrays.asList(
							FortuneTwoConstants.fortuneData).indexOf(
							winNum[i].trim());
				}
			} else if ("FortuneThree".equalsIgnoreCase(Util.getGameName(Integer
					.parseInt(gameNo.trim())))) {
				for (int i = 0; i < winNum.length; i++) {
					winNumbers[i] = Arrays.asList(
							FortuneThreeConstants.fortuneData).indexOf(
							winNum[i].trim());
				}
			} else if("RainbowGame".equalsIgnoreCase(Util.getGameName(Integer.parseInt(gameNo.trim())))) {
				final String[][] colorArray = new String[][]{{"13", "Voilet"}, {"14", "Indigo"}, {"15", "Blue"}, {"16", "Green"}, {"17", "Yellow"}, {"18", "Orange"}, {"19", "Red"}};

				for (int i=0; i<3; i++) {
					winNumbers[i] = Integer.parseInt(winNum[i].trim());
				}
				for (int i=3; i<winNum.length; i++) {
					for(int j=0; j<7; j++) {
						if((winNum[i].trim()).equals(colorArray[j][1])) {
							winNumbers[i] = Integer.parseInt(colorArray[j][0]);
							break;
						}
					}
				}
			} else {
				if("KenoFive".equalsIgnoreCase(Util.getGameMap().get(Integer.parseInt(gameNo)).getGameNameDev()) || "KenoFour".equalsIgnoreCase(Util.getGameMap().get(Integer.parseInt(gameNo)).getGameNameDev())){
					macNum = macNumber.trim().split(",");
					macNumbers = new Integer[macNum.length];
				}
				for (int i = 0; i < winNum.length; i++) {
					winNumbers[i] = Integer.parseInt(winNum[i].trim());
					if("KenoFive".equalsIgnoreCase(Util.getGameMap().get(Integer.parseInt(gameNo)).getGameNameDev()) ||  "KenoFour".equalsIgnoreCase(Util.getGameMap().get(Integer.parseInt(gameNo)).getGameNameDev()))
						macNumbers[i] = Integer.parseInt(macNum[i].trim());
				}
			}
			winNumSize = winNum.length;
		} else {
			try {
				cardTypeVal = Integer.parseInt(winNumber);
			} catch (Exception e) {
			}
		}
		if (winNumbers != null) {
			winNumSize = winNumbers.length;
		}
		HttpSession session = servletRequest.getSession();
		UserInfoBean userBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		System.out
				.println("****userid**" + userBean.getUserId() + "**********");
		ManualWinningBean mwBean = new ManualWinningBean();
		mwBean.setGameNumber(Util.getGameNumberFromGameId(Integer.parseInt(gameNo.toString()))+"");
		mwBean.setGameId(Integer.parseInt(gameNo.toString()));
		mwBean.setDrawIds(new int[] { drawId });
		mwBean.setDrawType(new String[] { "PMEM" });
		mwBean.setCardType(new int[] { cardTypeVal });
		mwBean.setWinningNumbers(winNumbers);
		mwBean.setMachineNumbers(macNumbers);
		mwBean.setWinNumSize(winNumSize);
		mwBean.setUserId(userBean.getUserId());
		DrawGameMgmtHelper helper = new DrawGameMgmtHelper();
		session.setAttribute("MESSAGE", helper.saveDrawResult(mwBean));
		DrawGameRPOS.onStartNewData();
		return SUCCESS;
	}

	public String getStartDate(String s) {
		SimpleDateFormat df=new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
	    Calendar cal=Calendar.getInstance();
	    Date d1;
	    String str = null;
	    try {
			d1 = df.parse(s);
		
	    cal.setTime(d1);
	    cal.add(Calendar.DAY_OF_MONTH, -5);
		str = new Timestamp(cal.getTimeInMillis()).toString();
	    } catch (ParseException e) {
			e.printStackTrace();
		}
		return (str.substring(0, str.length()-2));
	  }
	
	public void setAgentOrgName(String agentOrgName) {
		this.agentOrgName = agentOrgName;
	}

	public void setCancelMoneyStatus(String cancelMoneyStatus) {
		this.cancelMoneyStatus = cancelMoneyStatus;
	}

	public void setCardType(int[] cardType) {
		this.cardType = cardType;
	}

	public void setChangeType(String changeType) {
		this.changeType = changeType;
	}

	public void setDraw_day(String draw_day) {
		this.draw_day = draw_day;
	}

	public void setDraw_freeze_time(String[] draw_freeze_time) {
		this.draw_freeze_time = draw_freeze_time;
	}

	public void setDraw_time(String[] draw_time) {
		this.draw_time = draw_time;
	}

	public void setDrawId(int drawId) {
		this.drawId = drawId;
	}

	public void setDrawIds(int[] drawIds) {
		this.drawIds = drawIds;
	}

	public void setDrawScheduleBeanResultList(
			ArrayList<DrawScheduleBeanResult> drawScheduleBeanResultList) {
		this.drawScheduleBeanResultList = drawScheduleBeanResultList;
	}

	public void setDrawType(String[] drawType) {
		this.drawType = drawType;
	}

	public void setEnd_Date(String end_Date) {
		this.end_Date = end_Date;
	}

	public void setFortuneBean(FortunePurchaseBean fortuneBean) {
		this.fortuneBean = fortuneBean;
	}

	public void setFreezeTime(int freezeTime) {
		this.freezeTime = freezeTime;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public void setFromHour(String fromHour) {
		this.fromHour = fromHour;
	}

	public void setFromMin(String fromMin) {
		this.fromMin = fromMin;
	}

	public void setFromSec(String fromSec) {
		this.fromSec = fromSec;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public void setGameNo(String gameNo) {
		this.gameNo = gameNo;
	}

	public void setGameType(String gameType) {
		this.gameType = gameType;
	}

	public void setKenoPurchaseBean(KenoPurchaseBean kenoPurchaseBean) {
		this.kenoPurchaseBean = kenoPurchaseBean;
	}

	public void setLottoPurchaseBean(LottoPurchaseBean lottoPurchaseBean) {
		this.lottoPurchaseBean = lottoPurchaseBean;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setPerformStatus(String[] performStatus) {
		this.performStatus = performStatus;
	}

	public void setPostponeTime(int postponeTime) {
		this.postponeTime = postponeTime;
	}

	public void setRetName(String[] retName) {
		this.retName = retName;
	}

	public void setRetOrgName(String retOrgName) {
		this.retOrgName = retOrgName;
	}

	public void setSearch_type(String search_type) {
		this.search_type = search_type;
	}

	public void setServletRequest(HttpServletRequest servletRequest) {
		this.servletRequest = servletRequest;

	}

	public void setStart_date(String start_date) {
		this.start_date = start_date;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setSubHold(String subHold) {
		this.subHold = subHold;
	}

	public String setTempBean() {
		System.out.println("inside initiate");
		HttpSession session = servletRequest.getSession();
		if (gameType.equalsIgnoreCase("fortune")) {
			setFortuneBean((FortunePurchaseBean) session
					.getAttribute("TEMP_BEAN"));
		} else if (gameType.equalsIgnoreCase("lotto")) {
			setLottoPurchaseBean((LottoPurchaseBean) session
					.getAttribute("TEMP_BEAN"));
		} else if (gameType.equalsIgnoreCase("keno")) {
			setKenoPurchaseBean((KenoPurchaseBean) session
					.getAttribute("TEMP_BEAN"));
		}
		return SUCCESS;
	}

	public void setTicketNo(String ticketNo) {
		this.ticketNo = ticketNo;
	}

	public void setTicketNum(String ticketNum) {
		this.ticketNum = ticketNum;
	}

	public void setTicketNumArr(String[] ticketNumArr) {
		this.ticketNumArr = ticketNumArr;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public void setToHour(String toHour) {
		this.toHour = toHour;
	}

	public void setToMin(String toMin) {
		this.toMin = toMin;
	}

	public void setToSec(String toSec) {
		this.toSec = toSec;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setUser1(int user1) {
		this.user1 = user1;
	}

	public void setUser2(int user2) {
		this.user2 = user2;
	}

	public void setWinNumber(String winNumber) {
		this.winNumber = winNumber;
	}

	public void setWinNumbers(Integer[] winNumbers) {
		this.winNumbers = winNumbers;
	}

	public void setWinNumSize(int winNumSize) {
		this.winNumSize = winNumSize;
	}

	public String ticketCancellationMenu() throws LMSException {
		try {
			HttpSession session = servletRequest.getSession();
			UserInfoBean userBean = (UserInfoBean) session
					.getAttribute("USER_INFO");
			//List<String> agentOrgList = new AjaxRequestHelper().getAgents(
				//	"agent", userBean);
			// session.setAttribute("AgentOrgList", agentOrgList);
			session.setAttribute("DRAWGAME_LIST",ReportUtility.fetchGameMapWithoutPromo());
			session.setAttribute("RetOrgList", new ArrayList<String>());
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException("Error in ticketCancellationMenu");
		}
		return SUCCESS;
	}

	public String ticketWinStatus() throws Exception {
		HttpSession session = servletRequest.getSession();
		DrawGameMgmtHelper helper = new DrawGameMgmtHelper();
		session.setAttribute("TKT_WIN_STATUS", helper
				.ticketWinStatus(ticketNum));
		return SUCCESS;
	}

	public String transTicketWinStatus() throws Exception {
		
		Connection con = null;
		ServletContext sc = null;
		HttpSession session = null;
		SimpleDateFormat sdf = null;
		Timestamp txDateDate = null;
		Timestamp uIdChangeDate = null; 
		
		int userId=0;
		int gameId = 0;
		int gameNumber = 0;
		int dayOfYear = 0;
		long transTickNum = 0;

		String tickNum = "";
		String[] arr = null;
		Calendar cal = null;
		String query =  null;
		try {
			session = servletRequest.getSession();
			session.setAttribute("TKT_WIN_STATUS" , null);
			sc = ServletActionContext.getServletContext();
			gameId = Integer.parseInt(gameNo);
			gameNumber = Util.getGameNumberFromGameId(gameId);

			sdf = new SimpleDateFormat("yyyy-MM-dd");
			txDateDate = new java.sql.Timestamp(sdf.parse(sale_date).getTime());
			uIdChangeDate = new java.sql.Timestamp(sdf.parse((String) sc.getAttribute("USER_MAPPING_ID_DEPLOYMENT_DATE")).getTime());
			arr = sale_date.split("-");
			cal = Calendar.getInstance();
			cal.set(Integer.parseInt(arr[0]), Integer.parseInt(arr[1]) - 1,	Integer.parseInt(arr[2]));
			dayOfYear = cal.get(Calendar.DAY_OF_YEAR);

			con = DBConnect.getConnection();
			userId = ReportUtility.getUserIdFromOrgId(retOrgId , con);
			if (uIdChangeDate.after(txDateDate)) { // OLD TICKET
				transTickNum = ((long) (((userId * 10 + gameNumber) * 1000) + dayOfYear) * 10000 + transNo);
				query = "select ticket_nbr from st_dg_ret_sale_" + gameId
						+ " where ticket_nbr like '" + transTickNum + "%'";
			} else { // NEW TICKET
				userId = Util.getMappingIdFromTxId(txDateDate, userId , con);
				transTickNum = (long)(((3 * 10 + gameNumber) * 1000) + dayOfYear)*100000000 + (userId * 1000 + transNo);
				query = "select ticket_nbr from st_dg_ret_sale_" + gameId
						+ " where ticket_nbr like '%" + transTickNum + "%'";
			}

			tickNum = Util.getTickNumberFromTransTickNo(transTickNum, gameId,query,con);
			DrawGameMgmtHelper helper = new DrawGameMgmtHelper();
			session.setAttribute("TKT_WIN_STATUS", helper.ticketWinStatus(tickNum));
		}catch (LMSException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			DBConnect.closeCon(con);
		}

		return SUCCESS;
	}
	
	public String agtTrackTicket() throws Exception {
		HttpSession session = servletRequest.getSession();
		DrawGameMgmtHelper helper = new DrawGameMgmtHelper();
		session.setAttribute("TKT_WIN_STATUS", helper
				.ticketWinStatus(ticketNum));
		return SUCCESS;
	}
	
	public void fetchCancelDrawData() throws IOException{
		PrintWriter out = response.getWriter();
		List<Integer> drawIdList = new ArrayList<Integer>();
		drawIdList.add(Integer.parseInt(drawIdString));
	//	gameNo = gameNo.substring(0, gameNo.lastIndexOf("-"));
		DrawScheduleBean drawScheduleBean = new DrawScheduleBean();
		drawScheduleBean.setDrawIdList(drawIdList);
		drawScheduleBean.setGameNo(Integer.parseInt(gameNo));
		DrawGameMgmtHelper helper = new DrawGameMgmtHelper();
		String res = helper.fetchCancelDrawData(drawScheduleBean);
		out.write(res);
		out.flush();
	}
/**
 * This Method Show Draw Wise+Agent Wise  DATA OF LMS SALE IN DGE. 
 * @return
 * @throws Exception
 */
	public String fetchDrawDataForLMS() throws Exception {

		logger.info("startDate is" + dateOfDraw + " endDate is" + end_Date
				+ " agentOrgId " + agentOrgId + " gameNo" + gameNo + "Before--"
				+ new Date());
		try {
		
			int gameId = Integer.parseInt(gameNo);
			if (gameId > 0 && dateOfDraw != null) {
				DrawDataBean drawDataBean = new DrawDataBean();
				drawDataBean.setAgentOrgId(agentOrgId);
				// The to date has been Hard Code to Avoid Use of calendar and
				// appended
				// with 23:59:59 to achieve the data
				// corresponding to the midnight of given date
				drawDataBean.setFromDate(dateOfDraw + " 00:00:00");
				drawDataBean.setToDate(end_Date + " 23:59:59");
				drawDataBean.setGameNo(gameId);
				drawDataBean.setDrawName(drawName);
				
				ServletContext sc = ServletActionContext.getServletContext();
				String raffleTktType = (String) sc.getAttribute("raffle_ticket_type");
				String refMerchantId = (String) sc.getAttribute("REF_MERCHANT_ID");
				drawDataBean.setMerchantId(refMerchantId);
				DrawGameMgmtHelper helper = new DrawGameMgmtHelper();
				DGConsolidateGameDataBean gameDataBean = null;
				gameDataBean = helper.fetchDrawGameDataForLMS(drawDataBean,
						raffleTktType, stateCode, CityCode);
				if (gameDataBean != null) {
					drawDataBeanList = (ArrayList<DGConsolidateDrawBean>) gameDataBean
							.getDrawDataBeanList();
					logger.info("Got Draw Game Data : "+drawDataBeanList.size());
					setGameId(gameId);
					return SUCCESS;
				}
			} else {
				logger.info("Incorrect Inputs ");
				addActionMessage("Please Enter Correct Values");
				return ERROR;
			}
		}catch(Exception e){
			e.printStackTrace();
			addActionMessage("Some Error In Draw Data ");
			
		}
		return ERROR;
		
	}
	public String  fetchPromoGameDetails(){
		ObjectOutputStream oos =null;
		ObjectInputStream ois =null;
		try{
			ByteArrayOutputStream bos =new ByteArrayOutputStream();
			oos =new ObjectOutputStream(bos);
			oos.writeObject(Util.promoGameBeanMap);
			oos.flush();    
			ByteArrayInputStream bin =  new ByteArrayInputStream(bos.toByteArray()); 
			ois = new ObjectInputStream(bin);  
			setPromoGameBeanMap((Map<Integer, List<PromoGameBean>>) ois.readObject());
		}catch(Exception e){
			addActionMessage("Some Internal Error IN Displaying Game Info");
			logger.error("Errro",e);
		}
	return SUCCESS;
		
		
	}
	
	public void  updateFreeTicket(){
		
		boolean isUpdated=false;
		try{
			PrintWriter out = servletResponse.getWriter();
			servletResponse.setContentType("text/html");
			logger.debug(promoGameId+saleGameId+promoTicekts);
			
			
			if(promoGameId<=0 ||saleGameId<=0||promoTicekts<0){
				out.print("Error | Please Provide Valid Input");
				return ;
			}
			if(promoTicekts>15){
				out.print("Error |Please Provide Valid Value For Promo Tickets");
				return ;
			}
			HttpSession session = servletRequest.getSession();
			UserInfoBean uib = (UserInfoBean) session.getAttribute("USER_INFO");
			 isUpdated =DrawGameMgmtHelper.updateFreeTicket(uib,saleGameId, promoGameId, promoTicekts);
			 if(isUpdated){
				 out.print("Success|Promo Ticket Value Updated Successfully"+"|"+promoTicekts);
				 return ;
			 }else{
				 
				 out.print("Error|Error In Promo Ticket Value Updatation");
				 return ;
			 }
			 
		}catch (Exception e) {
			addActionError("Error In Promo Ticket Udpation !!");
			logger.error("Exception in update free ticket",e);
		}
		return ;
	
		
	}
	
	public String drawWiseWinTicketDetails() {
		SimpleDateFormat sdf = null;
		Timestamp uIdChangeDate = null;
		TicketWiseDataBean ticketBean = null;
		AnalysisBean anaBean=null;
		try{
			anaBean = new AnalysisBean();
			ServletContext sc = ServletActionContext.getServletContext();
			String refMerchantId = (String) sc.getAttribute("REF_MERCHANT_ID");
			setDrawName(drawName);
			sdf = new SimpleDateFormat("yyyy-MM-dd");
			anaBean.setMerchantId(refMerchantId);
			uIdChangeDate = new java.sql.Timestamp(sdf.parse(Utility.getPropertyValue("USER_MAPPING_ID_DEPLOYMENT_DATE")).getTime());
			ticketBean = new DrawGameMgmtHelper().getConsolidatedTicketsForDraw(gameId,agentOrgId,drawName,start_date,uIdChangeDate,anaBean,status);
			setAnalist(ticketBean.getAnaBeanList());
			setMessage(ticketBean.getErrorMessage());
			setStart_date(start_date);	
		}catch (Exception e) {
			e.printStackTrace();
			if(ticketBean == null){
				ticketBean = new TicketWiseDataBean();
				ticketBean.setErrorCode(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE);
				ticketBean.setErrorMessage(LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
			}
		}
		return SUCCESS;
	}
	
	public void drawWiseWinTicketDetailsExport() throws IOException {
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition","attachment; filename=TicketWiseDetails.xls");
		PrintWriter out = response.getWriter();
		if (valueToSend != null) {
			valueToSend = valueToSend.replaceAll("<tbody>", "").replaceAll("</tbody>", "").trim();
			out.write(valueToSend);
		}
	}
	
	public String fetchGameInfo(){
		setDrawGameInfo(ReportUtility.fetchActiveGameDrawDataMenu());
		return SUCCESS;
	}
	
	public String blockBunchTicket() throws ParseException{
		try{
			int userId =  ((UserInfoBean) servletRequest.getSession().getAttribute("USER_INFO")).getUserId();
			ServletContext sc=LMSUtility.sc;
			String countryName=(String)sc.getAttribute("COUNTRY_DEPLOYED");
			Timestamp requesInitiateTime = new Timestamp(System.currentTimeMillis());
			List<BlockTicketUserBean> blockTicketUserBeanList = new DrawGameMgmtHelper().blockBunchTicket
														(userId, ticketNumArr , requesInitiateTime, "" ,ticketStatusArr,countryName);
			if(blockTicketUserBeanList == null){
				setMessage(" Contact Customer Support for this ticket !!!! ");
			}else {
				setMessage(null);
			}
			setBlockTicketUserBeanList(blockTicketUserBeanList);
		}catch (Exception e) {
			e.printStackTrace();
			setMessage(" Contact Customer Support for this ticket !!!! ");
		}
		return SUCCESS;
	}
	
	private String criteria;
	
	public String getTicketsToUnblock() throws ParseException{
		
		try{
			List<BlockTicketUserBean> blockTicketUserBeanList = new DrawGameMgmtHelper().getTicketsToBlockUnblock(criteria , ticketNo , agentOrgId , retOrgId , Integer.parseInt(gameNo) , reason);
			if(blockTicketUserBeanList == null){
				setMessage(" Contact Customer Support for this ticket !!!! ");
			}else {
				setBlockTicketUserBeanList(blockTicketUserBeanList);
			}
			setReasons(null);
		}catch (Exception e) {
			e.printStackTrace();
			setMessage(" Contact Customer Support for this ticket !!!! ");
		}
		return SUCCESS;
	}
	
	public String unblockSpecificTicket() throws ParseException{
		
		try{
			int userId =  ((UserInfoBean) servletRequest.getSession().getAttribute("USER_INFO")).getUserId();
			ServletContext sc=LMSUtility.sc;
			String countryName=(String)sc.getAttribute("COUNTRY_DEPLOYED");
			Timestamp requesInitiateTime = new Timestamp(System.currentTimeMillis());
			ArrayList<BlockTicketUserBean> blockTicketUserBeanList = new DrawGameMgmtHelper().unblockBunchTicket
														(userId, ticketNumArr , requesInitiateTime, reasons,countryName);
			if(blockTicketUserBeanList == null){
				setMessage(" Contact Customer Support for this ticket !!!! ");
			}else {
				setBlockTicketUserBeanList(blockTicketUserBeanList);
			}
		}catch (Exception e) {
			e.printStackTrace();
			setMessage(" Contact Customer Support for this ticket !!!! ");
		}
		return SUCCESS;
	}
	
	public void validate (){
	}
	public int getMsgId() {
		return msgId;
	}

	public void setMsgId(int msgId) {
		this.msgId = msgId;
	}

	public String getDrawName() {
		return drawName;
	}

	public void setDrawName(String drawName) {
		this.drawName = drawName;
	}

	public int[] getUser() {
		return user;
	}

	public void setUser(int[] user) {
		this.user = user;
	}

	public String getUserStr() {
		return userStr;
	}

	public void setUserStr(String userStr) {
		this.userStr = userStr;
	}

	public int getAgentOrgId() {
		return agentOrgId;
	}

	public void setAgentOrgId(int agentOrgId) {
		this.agentOrgId = agentOrgId;
	}

	public int getRetOrgId() {
		return retOrgId;
	}

	public void setRetOrgId(int retOrgId) {
		this.retOrgId = retOrgId;
	}

	public String getSale_date() {
		return sale_date;
	}

	public void setSale_date(String saleDate) {
		sale_date = saleDate;
	}

	public String getDrawIdString() {
		return drawIdString;
	}

	public void setDrawIdString(String drawIdString) {
		this.drawIdString = drawIdString;
	}

	public HttpServletResponse getServletResponse() {
		return servletResponse;
	}

	public HttpServletRequest getServletRequest() {
		return servletRequest;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public ArrayList<DGConsolidateDrawBean> getDrawDataBeanList() {
		return drawDataBeanList;
	}

	public void setDrawDataBeanList(
			ArrayList<DGConsolidateDrawBean> drawDataBeanList) {
		this.drawDataBeanList = drawDataBeanList;
	}

	public String getDateOfDraw() {
		return dateOfDraw;
	}

	public void setDateOfDraw(String dateOfDraw) {
		this.dateOfDraw = dateOfDraw;
	}

	public String[] getReasons() {
		return reasons;
	}

	public void setReasons(String[] reasons) {
		this.reasons = reasons;
	}

	public HashMap<Integer, String> getCommSerList() {
		return commSerList;
	}

	public void setCommSerList(HashMap<Integer, String> commSerList) {
		this.commSerList = commSerList;
	}

	public HashMap<Integer, String> getDrawSerList() {
		return drawSerList;
	}

	public void setDrawSerList(HashMap<Integer, String> drawSerList) {
		this.drawSerList = drawSerList;
	}

	public Map<Integer, String> getOlaSerList() {
		return olaSerList;
	}

	public void setOlaSerList(Map<Integer, String> olaSerList) {
		this.olaSerList = olaSerList;
	}

	public Map<Integer, List<PromoGameBean>> getPromoGameBeanMap() {
		return promoGameBeanMap;
	}

	public void setPromoGameBeanMap(
			Map<Integer, List<PromoGameBean>> promoGameBeanMap) {
		this.promoGameBeanMap = promoGameBeanMap;
	}

	public int getPromoGameId() {
		return promoGameId;
	}

	public int getSaleGameId() {
		return saleGameId;
	}

	public int getPromoTicekts() {
		return promoTicekts;
	}

	public void setPromoGameId(int promoGameId) {
		this.promoGameId = promoGameId;
	}

	public void setSaleGameId(int saleGameId) {
		this.saleGameId = saleGameId;
	}

	public void setPromoTicekts(int promoTicekts) {
		this.promoTicekts = promoTicekts;
	}

	public List<AnalysisReportDrawBean> getAnalist() {
		return analist;
	}

	public void setAnalist(List<AnalysisReportDrawBean> analist) {
		this.analist = analist;
	}

	public String[] getTicketStatusArr() {
		return ticketStatusArr;
	}

	public void setTicketStatusArr(String[] ticketStatusArr) {
		this.ticketStatusArr = ticketStatusArr;
	}

	public List<BlockTicketUserBean> getBlockTicketUserBeanList() {
		return blockTicketUserBeanList;
	}

	public void setBlockTicketUserBeanList(
			List<BlockTicketUserBean> blockTicketUserBeanList) {
		this.blockTicketUserBeanList = blockTicketUserBeanList;
	}

	public HashMap<Integer, String> getDrawGameInfo() {
		return drawGameInfo;
	}

	public void setDrawGameInfo(HashMap<Integer, String> drawGameInfo) {
		this.drawGameInfo = drawGameInfo;
	}

	public String getCriteria() {
		return criteria;
	}

	public void setCriteria(String criteria) {
		this.criteria = criteria;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getValueToSend() {
		return valueToSend;
	}

	public void setValueToSend(String valueToSend) {
		this.valueToSend = valueToSend;
	}
	
	
	public String fetchPromoDrawDataMenu() throws Exception {
		HttpSession session = servletRequest.getSession();
		ServletContext sc = session.getServletContext();
		
		session.setAttribute("presentDate", new java.sql.Date(new Date()
				.getTime()).toString());
		session.setAttribute("DRAWGAME_LIST", ReportUtility.fetchDrawDataMenu());
		//SimpleDateFormat formatOld = new SimpleDateFormat("dd-MM-yyyy");
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date strtDate = format.parse(CommonMethods.getLastArchDate());
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(strtDate);
		cal1.add(Calendar.DATE, 1); 
		setArchDate(format.format(cal1.getTime()));
		setDepDate(CommonMethods.convertDateInGlobalFormat((String)sc.getAttribute("DEPLOYMENT_DATE"), "yyyy-mm-dd", (String)sc.getAttribute("date_format")));
		return SUCCESS;
	}
	
	public String fetchPromoDrawDataForLMS() throws Exception {
		logger.info("startDate is" + dateOfDraw + " endDate is" + end_Date
				+ " agentOrgId " + agentOrgId + " gameNo" + gameNo + "Before--"
				+ new Date());
		try {
			String gameName = Util.getGameName(Integer.parseInt(gameNo));
			if(!"TwelveByTwentyFour".equals(gameName)) {
				message="Report is Not Available for This Game.";
				return SUCCESS;
			}

			int gameId = Integer.parseInt(gameNo);
			if (gameId > 0 && dateOfDraw != null) {
				DrawDataBean drawDataBean = new DrawDataBean();
//				drawDataBean.setAgentOrgId(agentOrgId);
				drawDataBean.setAgentOrgId(-1);
				// The to date has been Hard Code to Avoid Use of calendar and
				// appended
				// with 23:59:59 to achieve the data
				// corresponding to the midnight of given date
				drawDataBean.setFromDate(dateOfDraw + " 00:00:00");
				drawDataBean.setToDate(end_Date + " 23:59:59");
				drawDataBean.setGameNo(gameId);
				drawDataBean.setDrawName(drawName);

				ServletContext sc = ServletActionContext.getServletContext();
				String raffleTktType = (String) sc.getAttribute("raffle_ticket_type");
				String refMerchantId = (String) sc.getAttribute("REF_MERCHANT_ID");
				drawDataBean.setMerchantId(refMerchantId);
				DrawGameMgmtHelper helper = new DrawGameMgmtHelper();
				DGConsolidateGameDataBean gameDataBean = null;
				gameDataBean = helper.fetchPromoDrawGameDataForLMS(drawDataBean, raffleTktType);
				if (gameDataBean != null) {
					drawDataBeanList = (ArrayList<DGConsolidateDrawBean>) gameDataBean.getDrawDataBeanList();
					logger.info("Got Draw Game Data : " + drawDataBeanList.size());
					setGameId(gameId);
					return SUCCESS;
				}
			} else {
				logger.info("Incorrect Inputs ");
				addActionMessage("Please Enter Correct Values");
				return ERROR;
			}
		} catch (Exception e) {
			e.printStackTrace();
			addActionMessage("Some Error In Draw Data ");
		}
		return ERROR;
	}
	
	/* Fetch Rank Wise Winning Data for 12/24 game */
	
	public String fatchRankWiseWinningData() throws Exception {

		logger.info("startDate is" + dateOfDraw + " 00:00:00" + " endDate is" + dateOfDraw + " 23:59:59" + " gameNo" + gameNo + "Before--"+ new Date() + " drawName: "+drawName);
		try {
			
			int gameId = Integer.parseInt(gameNo.contains(",") ? gameNo.split(",")[0] : gameNo);
			String dName = drawName.contains(",") ? drawName.split(",")[0] : drawName;
			if (gameId > 0 && dateOfDraw != null && !dName.equals("ALL")) {
				DrawDataBean drawDataBean = new DrawDataBean();
				// The to date has been Hard Code to Avoid Use of calendar and
				// appended
				// with 23:59:59 to achieve the data
				// corresponding to the midnight of given date
				drawDataBean.setFromDate(dateOfDraw + " 00:00:00");
				drawDataBean.setToDate(dateOfDraw + " 23:59:59");
				drawDataBean.setGameNo(gameId);
				drawDataBean.setDrawName(dName);
				
				ServletContext sc = ServletActionContext.getServletContext();
				String refMerchantId = (String) sc.getAttribute("REF_MERCHANT_ID");
				drawDataBean.setMerchantId(refMerchantId);
				DrawGameMgmtHelper helper = new DrawGameMgmtHelper();
				Map<Integer, RankWiseWinningReportBean> dataMap = null;
				dataMap = helper.fetchRankWiseWinningData(drawDataBean);
				if (dataMap != null) {
					rankWinningData = dataMap;
					return SUCCESS;
				}
			} else {
				logger.info("Incorrect Inputs ");
				addActionMessage("Please Enter Correct Values");
				return ERROR;
			}
		}catch(Exception e){
			e.printStackTrace();
			addActionMessage("Some Error In Draw Data ");
			
		}
		return ERROR;
		
	}

	public void setRankWinningData(Map<Integer,RankWiseWinningReportBean> rankWinningData) {
		this.rankWinningData = rankWinningData;
	}

	public Map<Integer,RankWiseWinningReportBean> getRankWinningData() {
		return rankWinningData;
	}
	
	private String reportData;
	
	public String getReportData() {
		return reportData;
	}

	public void setReportData(String reportData) {
		this.reportData = reportData;
	}

	public void exportAsExcel() throws IOException {
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition", "attachment; filename=Rank Wise Winning Report.xls");
		PrintWriter out = response.getWriter();
		if (reportData != null) {
			reportData = reportData.replaceAll("<tbody>", "").replaceAll("</tbody>", "").trim();
			out.write(reportData);
		}
		out.flush();
		out.close();
	}
	
	private String DrawDateTime;
	private String freezeDateTime;
	
	
	
	public String getDrawDateTime() {
		return DrawDateTime;
	}

	public void setDrawDateTime(String drawDateTime) {
		DrawDateTime = drawDateTime;
	}

	public String getFreezeDateTime() {
		return freezeDateTime;
	}

	public void setFreezeDateTime(String freezeDateTime) {
		this.freezeDateTime = freezeDateTime;
	}

	public void getResponseFromDGW() throws IOException{
		logger.info("Draw DateTime is :" + DrawDateTime + " Freeze DateTime is :" + freezeDateTime+ " drawId is :" + drawId + " gameId is :" + gameId);
		StringBuilder resp=null;
		PrintWriter out=null;
		DrawDataBean drawDataBean=null;
		DrawGameMgmtHelper helper=null;
		try{
		    drawDataBean=new DrawDataBean();
			drawDataBean.setDrawTime(DrawDateTime);
			drawDataBean.setFromDate(freezeDateTime);
			drawDataBean.setDrawId(drawId);
			drawDataBean.setGameName(gameName);
			helper = new DrawGameMgmtHelper();
			resp=helper.getAutumaticResultSubmissionResponse(drawDataBean);
			out = response.getWriter();
		
		}
		catch(Exception e){
			JSONObject jsonResponse = new JSONObject();
			 jsonResponse.put("isSuccess",false);
			 jsonResponse.put("responseMessage","Internal system error !!!");
			 resp.append(jsonResponse);
			
		}
		out.write(resp.toString());
	}
	
	private String bonusBallEnable;

	public String getBonusBallEnable() {
		return bonusBallEnable;
	}
	public void setBonusBallEnable(String bonusBallEnable) {
		this.bonusBallEnable = bonusBallEnable;
	}
}