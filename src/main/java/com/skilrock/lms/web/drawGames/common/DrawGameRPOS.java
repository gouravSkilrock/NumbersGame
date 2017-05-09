package com.skilrock.lms.web.drawGames.common;

import java.awt.Toolkit;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.BaseAction;
import com.skilrock.lms.common.ConfigurationVariables;
import com.skilrock.lms.common.Utility;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;
import com.skilrock.lms.common.utility.LMSUtility;
import com.skilrock.lms.coreEngine.drawGames.playMgmt.DrawGameOfflineHelper;
import com.skilrock.lms.coreEngine.drawGames.playMgmt.DrawGameRPOSHelper;
import com.skilrock.lms.coreEngine.drawGames.playMgmt.ServerStartUpData;
import com.skilrock.lms.dge.beans.CancelTicketBean;
import com.skilrock.lms.dge.beans.DrawDetailsBean;
import com.skilrock.lms.dge.beans.DrawIdBean;
import com.skilrock.lms.dge.beans.FortunePurchaseBean;
import com.skilrock.lms.dge.beans.KenoPurchaseBean;
import com.skilrock.lms.dge.beans.LottoPurchaseBean;
import com.skilrock.lms.dge.beans.MainPWTDrawBean;
import com.skilrock.lms.dge.beans.PWTDrawBean;
import com.skilrock.lms.dge.beans.WebReprint;
import com.skilrock.lms.rest.common.TransactionManager;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

public class DrawGameRPOS extends BaseAction {

	private static final String INTERFACE_TYPE_WEB = "WEB";
	private static final String EXCEPTION_OCCURRED = "Exception Occurred";
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DrawGameRPOS(DrawGameRPOSHelper drawGameRPOSHelper, WebReprintFactory webReprintFactory){
		super(DrawGameRPOS.class.getName());
		this.drawGameRPOSHelper = drawGameRPOSHelper;
		this.webReprintFactory = webReprintFactory;
	}
	
	public DrawGameRPOS() {
		super(DrawGameRPOS.class.getName());
		drawGameRPOSHelper = new DrawGameRPOSHelper();
		webReprintFactory = new WebReprintFactory();
	}

	public static void onStartNewData() {
		new DrawGameRPOS().newData();
	}
	
	private DrawGameRPOSHelper drawGameRPOSHelper;
	private WebReprintFactory webReprintFactory;
	JSONObject jsonResponse;
	private Object gameBean;
	private UserInfoBean userInfoBean;
	private WebReprint webReprint;
	private String gameBeanType;
	private String actionName;
	private int gameId;
	private long lastPrintedTicket;
	private long lastTicketNumber;
	private boolean isReprintFirstTime;
	private WebReprintContext webReprintContext;
	private JSONObject requestData;
	private PrintWriter printWriter;
	
	private boolean autoCancel;
	TreeMap drawGameData;
	private Map<String, Map<Integer, DrawDetailsBean>> drawGameNewData;
	private String endRange;
	private String errMsg;
	private String firstName;
	private FortunePurchaseBean fortuneBean;
	TreeMap freezeTimeMap;
	private int gameNo;
	private String idNumber;
	private String idType;
	private String isRG;
	private String jreVersion;
	private KenoPurchaseBean kenoPurchaseBean;
	private KenoPurchaseBean kenoTwoPurchaseBean;
	private KenoPurchaseBean twelveByTwentyFourPurchaseBean;
	private String lastName;
	Log logger = LogFactory.getLog(DrawGameRPOS.class);
	private LottoPurchaseBean lottoPurchaseBean;
	private String pickNum;
	private HttpServletResponse response;
	String serverTime;
	private HttpServletRequest servletRequest;
	HttpSession session = null;
	private String startRange;
	private String ticketNo;
	private String pickNumStr;
	Map unitPrice;
	private String purchaseData;

	private String winNum;
	private String cancelType;
	private String json;
	private long LSTktNo;
	private String userName;

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	/*public void cancelTicket() throws Exception {
		logger.info("Inside cancelTicket");
		PrintWriter out = getResponse().getWriter();
		ServletContext sc = ServletActionContext.getServletContext();
		session = servletRequest.getSession();
		UserInfoBean userInfoBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		String refMerchantId = (String) sc.getAttribute("REF_MERCHANT_ID");

		if(!isAutoCancel()){
			cancelType="manual";
		} else if(cancelType != null && "dataError".equals(cancelType)){
			// DO NOTHING
		}else{
			cancelType="";
		}
		
		String cancelChannel = "LMS_Web";
		CancelTicketBean cancelTicketBean = new CancelTicketBean();
		Map<Integer, Map<Integer, String>> drawIdTableMap = (Map<Integer, Map<Integer, String>>) sc.getAttribute("drawIdTableMap");
		logger.debug(" ticketNo = " + ticketNo
				+ " drawIdTableMap in cancel ticket " + drawIdTableMap);
		DrawGameRPOSHelper helper = new DrawGameRPOSHelper();
		cancelTicketBean.setDrawIdTableMap(drawIdTableMap);
		
		cancelTicketBean.setCancelDuaraion("true".equalsIgnoreCase((String)sc.getAttribute("IS_CANCEL_DURATION")));
		cancelTicketBean.setCancelDuration(Integer.parseInt((String)sc.getAttribute("CANCEL_DURATION")));
		
		if ("LAST_SOLD_TICKET".equalsIgnoreCase((String) sc
				.getAttribute("CANCEL_TYPE"))) {
			ticketNo = helper.getLastSoldTicketNO(userInfoBean,"WEB");
			if(ticketNo!= null){
				ticketNo = ticketNo + Util.getRpcAppenderForTickets(ticketNo.length());
			}
			cancelTicketBean.setCancelType("LAST_SOLD_TICKET");
		}
		if (ticketNo == null) {
			out.print("Ticket Cannot Be Cancelled ");
		} else {
			
			cancelTicketBean.setTicketNo(ticketNo);
			cancelTicketBean.setPartyId(userInfoBean.getUserOrgId());
			cancelTicketBean.setUserId(userInfoBean.getUserId());
			cancelTicketBean.setPartyType(userInfoBean.getUserType());
			cancelTicketBean.setCancelChannel(cancelChannel);
			cancelTicketBean.setRefMerchantId(refMerchantId);
			cancelTicketBean.setAutoCancel(isAutoCancel());

			cancelTicketBean = helper.cancelTicket(cancelTicketBean,
					userInfoBean, isAutoCancel(),cancelType);

			if (cancelTicketBean.isValid()) {
				if ("LAST_SOLD_TICKET".equalsIgnoreCase(cancelTicketBean
						.getCancelType())) {
					String tktNum = cancelTicketBean.getTicketNo();

					cancelTicketBean.setTicketNo(tktNum.substring(0, tktNum
							.length() - 1)
							+ cancelTicketBean.getReprintCount());
				}
				
				int appletHeight = 150;
				StringBuilder topMsgsStrCancel = new StringBuilder(" ");
				StringBuilder bottomMsgsStrCancel = new StringBuilder(" ");
				
				if(cancelTicketBean.getRefundAmount() > 0.0){
					appletHeight = UtilApplet.getAdvMsgs(cancelTicketBean.getAdvMsg(), topMsgsStrCancel, bottomMsgsStrCancel, appletHeight);	
				}
				
				out.print("cancelTime=" + cancelTicketBean.getCancelTime()
						+ "|orgName=" + sc.getAttribute("ORG_NAME_JSP")
						+ "|currSymbol=" + sc.getAttribute("CURRENCY_SYMBOL")
						+ "|gameName=" + Util.getGameName(Util.getGameIdFromGameNumber(cancelTicketBean.getGameNo()))
						+ "|gameDispName=" + Util.getGameDisplayName(Util.getGameIdFromGameNumber(cancelTicketBean.getGameNo()))
						+ "|topAdvMsgCancel=" + topMsgsStrCancel
						+ "|bottomAdvMsgCancel=" + bottomMsgsStrCancel
						+ "|tktNo=" + cancelTicketBean.getTicketNo()
						+ "|refundAmt=" + cancelTicketBean.getRefundAmount()
						+ "|ctr=" + appletHeight);
			} else if (cancelTicketBean.isError()) { // RG Limit fail
				logger.debug("********" + cancelTicketBean.getErrMsg());
				out.print(cancelTicketBean.getErrMsg());
			} else {
				out.print("Ticket Cannot Be Cancelled or Invalid Ticket");
			}
		}
	}*/

	@SuppressWarnings("unchecked")
	public void cancelTicket() {
		logger.info("-- Inside Cancel Ticket JSON --");

		PrintWriter out = null;
		JSONObject responseObject = new JSONObject();
		String parentOrgName = null;
		try {
			JSONObject requestData = (JSONObject) JSONSerializer.toJSON(json);
			String ticketNumber=requestData.get("ticketNumber").toString();
			
			if(ticketNumber!="null")
                ticketNo = (String) requestData.get("ticketNumber");
			autoCancel = (Boolean) requestData.get("autoCancel");
			logger.info("Ticket Number - "+ticketNo+" | Auto Cancel - "+autoCancel);

			out = getResponse().getWriter();

			//session = servletRequest.getSession();
			UserInfoBean userInfoBean = null;
			String userName = (String) requestData.get("userName");
			if(userName == null){
				session = servletRequest.getSession();
				userInfoBean = (UserInfoBean) session.getAttribute("USER_INFO");
			} else{
				userInfoBean = getUserBean(userName);
			}
			parentOrgName = userInfoBean.getParentOrgName();
			ServletContext sc = ServletActionContext.getServletContext();
			String refMerchantId = (String) sc.getAttribute("REF_MERCHANT_ID");

			TransactionManager.setResponseData("true");
			
			if(!autoCancel) {
				cancelType = "manual";
			} else if(cancelType != null && "dataError".equals(cancelType)) {
			} else {
				cancelType = "";
			}

			String cancelChannel = "LMS_Web";
			CancelTicketBean cancelTicketBean = new CancelTicketBean();
			Map<Integer, Map<Integer, String>> drawIdTableMap = (Map<Integer, Map<Integer, String>>) sc.getAttribute("drawIdTableMap");
			DrawGameRPOSHelper helper = new DrawGameRPOSHelper();
			cancelTicketBean.setDrawIdTableMap(drawIdTableMap);
			cancelTicketBean.setCancelDuaraion("true".equalsIgnoreCase((String)sc.getAttribute("IS_CANCEL_DURATION")));
			cancelTicketBean.setCancelDuration(Integer.parseInt((String)sc.getAttribute("CANCEL_DURATION")));
			if ("LAST_SOLD_TICKET".equalsIgnoreCase((String) sc.getAttribute("CANCEL_TYPE"))) {
				ticketNo = helper.getLastSoldTicketNO(userInfoBean,INTERFACE_TYPE_WEB);
				if(ticketNo!= null)
					ticketNo = ticketNo + Util.getRpcAppenderForTickets(ticketNo.length());

				cancelTicketBean.setCancelType("LAST_SOLD_TICKET");
			}
			if (ticketNo == null) {
				throw new LMSException(LMSErrors.INVALID_CANCEL_TICKET_DATA_ERROR_CODE, LMSErrors.INVALID_CANCEL_TICKET_DATA_ERROR_MESSAGE);
			} else {
				cancelTicketBean.setTicketNo(ticketNo);
				cancelTicketBean.setPartyId(userInfoBean.getUserOrgId());
				cancelTicketBean.setUserId(userInfoBean.getUserId());
				cancelTicketBean.setPartyType(userInfoBean.getUserType());
				cancelTicketBean.setCancelChannel(cancelChannel);
				cancelTicketBean.setRefMerchantId(refMerchantId);
				cancelTicketBean.setAutoCancel(isAutoCancel());

				cancelTicketBean = helper.cancelTicket(cancelTicketBean, userInfoBean, isAutoCancel(),cancelType);

				if (cancelTicketBean.isValid()) {
					if ("LAST_SOLD_TICKET".equalsIgnoreCase(cancelTicketBean.getCancelType())) {
						String tktNum = cancelTicketBean.getTicketNo();
						cancelTicketBean.setTicketNo(tktNum.substring(0, tktNum.length() - 1) + cancelTicketBean.getReprintCount());
					}

					JSONObject mainData = new JSONObject();
					mainData.put("ticketNumber", cancelTicketBean.getTicketNo());
					mainData.put("orgName", sc.getAttribute("ORG_NAME_JSP"));
					mainData.put("gameName", Util.getGameName(Util.getGameIdFromGameNumber(cancelTicketBean.getGameNo())));
					mainData.put("gameDispName", Util.getGameDisplayName(Util.getGameIdFromGameNumber(cancelTicketBean.getGameNo())));
					mainData.put("refundAmount", cancelTicketBean.getRefundAmount());
					mainData.put("cancelTime", cancelTicketBean.getCancelTime());
					mainData.put("advMessage", cancelTicketBean.getAdvMsg());
					mainData.put("currencySymbol", sc.getAttribute("CURRENCY_SYMBOL"));
					mainData.put("parentOrgName", parentOrgName);
					responseObject.put("isSuccess", true);
					responseObject.put("errorMsg", "");
					responseObject.put("mainData", mainData);
				} else if (cancelTicketBean.isError()) {
					if(cancelTicketBean.getErrMsg() != null && cancelTicketBean.getErrMsg().contains("|")){
						throw new LMSException(cancelTicketBean.getErrMsg().split("\\|")[0]);
					} else 
						throw new LMSException(cancelTicketBean.getErrMsg());
				} else {
					throw new LMSException(LMSErrors.INVALID_CANCEL_TICKET_DATA_ERROR_CODE, LMSErrors.INVALID_CANCEL_TICKET_DATA_ERROR_MESSAGE);
				}
			}
		} catch(LMSException e) {
			e.printStackTrace();
			responseObject.put("isSuccess", false);
			if(e.getMessage() !=null){
				responseObject.put("errorCode", LMSErrors.GENERAL_EXCEPTION_ERROR_CODE);
				responseObject.put("errorMsg", e.getMessage());
			} else{		
				responseObject.put("errorCode", e.getErrorCode());
				responseObject.put("errorMsg", e.getErrorMessage());
			}
		} catch(Exception e) {
			e.printStackTrace();

			responseObject.put("isSuccess", false);
			responseObject.put("errorCode", LMSErrors.GENERAL_EXCEPTION_ERROR_CODE);
			responseObject.put("errorMsg", LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}

		logger.info("CancelTicket Response Data - " + responseObject);
		out.print(responseObject);
		out.flush();
		out.close();
	}

	@SuppressWarnings("unchecked")
	public String fetchDrawGameData() throws Exception {
			return SUCCESS;
	}

	public void fetchDrawGameUpdatedData() throws Exception {
		ServletContext sc = ServletActionContext.getServletContext();
		PrintWriter out = getResponse().getWriter();
		Date date = new Date();
		StringBuilder rData = new StringBuilder(date.toString() + "="
				+ date.getTime() + "ServerDate");
		Iterator iter = ((TreeMap) sc.getAttribute("GAME_DATA")).entrySet()
				.iterator();
		while (iter.hasNext()) {
			Map.Entry pair = (Map.Entry) iter.next();
			List<List> drawList = (List<List>) pair.getValue();
			String nxtDrwTime = drawList.get(0).toString().replace("[", "")
					.replace("]", "");
			List<String> winList = (List<String>) drawList.get(1);
			Iterator winItr = winList.iterator();
			String winData = "";
			while (winItr.hasNext()) {
				winData = winData + winItr.next() + " Nxt ";
			}
			
			//sachin
			Map<Integer, Map<Integer, DrawDetailsBean>> drawDetailsMap = (Map<Integer, Map<Integer, DrawDetailsBean>>) ServletActionContext
			.getServletContext().getAttribute("drawDetailsMap");
			StringBuilder drawIdData = new StringBuilder(); 
			Map<Integer, DrawDetailsBean> innerMap = drawDetailsMap.get( pair.getKey());
			if (innerMap != null && innerMap.size() > 0) {
				Iterator<Map.Entry<Integer, DrawDetailsBean>> innerIter = innerMap
					.entrySet().iterator();
				DrawDetailsBean drawBean = null;
				while (innerIter.hasNext()) {
					Map.Entry<Integer, DrawDetailsBean> innerEntryMap = innerIter
					.next();
					drawBean = innerEntryMap.getValue();
					drawIdData.append(drawBean.getDrawId()+","+drawBean.getDrawName()+","+drawBean.getDrawDateTime().getTime()+":");
				}
		}
			
			
			
			// vaibhav
			String gameStatus = drawList.get(2).toString().replace("[", "")
					.replace("]", "");
			rData.append(Util.getGameName((Integer) pair.getKey())
					.toLowerCase()
					+ "UPD"
					+ nxtDrwTime
					+ "UPD"
					+ winData
					+ "UPD"
					+ drawIdData
					+ "UPD"
					+ gameStatus
					+ "Game");
			// rData.append(pair.getKey() + "UPD" + nxtDrwTime + "UPD" + winData
			// + "Game");
		}
		logger.debug("Ajax Call fetchDrawGameUpdatedData" + rData);
		out.print(rData);
	}

	public void fetchServerTime() throws IOException {
		String combinedData = null;
		PrintWriter out = getResponse().getWriter();
		Date date = new Date();
		combinedData = date.toString() + "=" + date.getTime();
		logger.debug("Combined Data: " + combinedData);
		out.print(combinedData);
	}

	public TreeMap getDrawGameData() {
		return drawGameData;
	}

	public Map<String, Map<Integer, DrawDetailsBean>> getDrawGameNewData() {
		return drawGameNewData;
	}

	public String getEndRange() {
		return endRange;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public String getFirstName() {
		return firstName;
	}

	public FortunePurchaseBean getFortuneBean() {
		return fortuneBean;
	}

	public TreeMap getFreezeTimeMap() {
		return freezeTimeMap;
	}

	public int getGameNo() {
		return gameNo;
	}

	// public vo

	public String getIdNumber() {
		return idNumber;
	}

	public String getIdType() {
		return idType;
	}

	public String getIsRG() {
		return isRG;
	}

	public String getJreVersion() {
		return jreVersion;
	}

	public KenoPurchaseBean getKenoPurchaseBean() {
		return kenoPurchaseBean;
	}

	public String getLastName() {
		return lastName;
	}

	public LottoPurchaseBean getLottoPurchaseBean() {
		return lottoPurchaseBean;
	}

	public String getPickNum() {
		return pickNum;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public String getServerTime() {
		return serverTime;
	}

	public HttpServletRequest getServletRequest() {
		return servletRequest;
	}

	public String getStartRange() {
		return startRange;
	}

	public String getTicketNo() {
		return ticketNo;
	}

	public Map getUnitPrice() {
		return unitPrice;
	}

	public String getWinNum() {
		return winNum;
	}

	public boolean isAutoCancel() {
		return autoCancel;
	}

	public void newData() {
		//ServletContext sc = ServletActionContext.getServletContext();
		ServletContext sc = LMSUtility.sc;
		//DrawGameRPOSHelper helper = new DrawGameRPOSHelper();
		/*List updatedDataList = */ServerStartUpData.getUpdatedData(sc);
		/*sc.setAttribute("GAME_DATA", (TreeMap<Integer, List<List>>) updatedDataList.get(1));
		sc.setAttribute("drawIdTableMap", (Map<Integer, Map<Integer, String>>) updatedDataList.get(0));
		sc.setAttribute("jackPotMap", (Map<Integer, Double>) updatedDataList.get(3));
		sc.setAttribute("drawIdDateMap",(Map<String, Map<Long, String>>) updatedDataList.get(4));
		sc.setAttribute("drawDetailsMap",(Map<String, Map<Integer, DrawDetailsBean>>) updatedDataList.get(5));
		logger.debug("*jackPotMap**rpos*"+ (Map<Integer, Double>) updatedDataList.get(3));

		Util.drawIdTableMap = (Map<Integer, Map<Integer, String>>) updatedDataList.get(0);
		Util.gameData = (TreeMap<Integer, List<List>>) updatedDataList.get(1);
		Util.jackPotMap = (Map<Integer, Double>) updatedDataList.get(3);
		Util.drawIdDateMap = (Map<String, Map<Long, String>>) updatedDataList.get(4);
		Util.drawDetailsMap = (Map<Integer, Map<Integer, DrawDetailsBean>>) updatedDataList.get(5);*/

		if ("Yes".equalsIgnoreCase((String) sc.getAttribute("RET_OFFLINE"))) {
			logger.debug("<-------------Step1------------->");
			if (gameNo != 0) {
				logger.debug("<-------------Step2------------->");
				DrawGameOfflineHelper.checkUserAFUStatus(gameNo);
			}
		}

	}

	public String payPwtTicket(){
		PrintWriter out = null;
		JSONObject jsonResponse = new JSONObject();
		String parentOrgName = null;
		try{	
			response.setContentType("application/json");
			out = response.getWriter();
			UserInfoBean userInfoBean = null;
			
			String highPrizeCriteria = (String) LMSUtility.sc.getAttribute("HIGH_PRIZE_CRITERIA");
			String highPrizeAmt = (String) LMSUtility.sc.getAttribute("HIGH_PRIZE_AMT");
			String highPrizeScheme = (String) LMSUtility.sc.getAttribute("DRAW_GAME_HIGH_PRIZE_SCHEME");
	
			JSONObject requestData = (JSONObject) JSONSerializer.toJSON(json);
			String userName = (String) requestData.get("userName");
			if(userName == null){
				session = servletRequest.getSession();
				userInfoBean = (UserInfoBean) session.getAttribute("USER_INFO");
			} else{
				session = getSession(userName);
				userInfoBean = getUserBean(userName);
			}
			parentOrgName = userInfoBean.getParentOrgName();
			
			//MainPWTDrawBean drawScheduleBean = (MainPWTDrawBean) requestData.get("mainData");
			//MainPWTDrawBean drawScheduleBean = new Gson().fromJson(data.get("mainData").toString(), new TypeToken<MainPWTDrawBean>() {}.getType());
			MainPWTDrawBean drawScheduleBean = (MainPWTDrawBean)session.getAttribute("WinningBean");
			
			DrawGameRPOSHelper helper = new DrawGameRPOSHelper();
	
			int gameId=0;
			long lastPrintedTicket=0;
			ServletContext sc = LMSUtility.sc;
			String refMerchantId = (String) sc.getAttribute("REF_MERCHANT_ID");
			int autoCancelHoldDays=Integer.parseInt((String) sc.getAttribute("AUTO_CANCEL_CLOSER_DAYS"));
			
			String actionName=ActionContext.getContext().getName();
			DrawGameRPOSHelper drawGameRPOSHelper = new DrawGameRPOSHelper();
			//drawGameRPOSHelper.checkLastPrintedTicketStatusAndUpdate(userInfoBean,lastPrintedTicket,"WEB",refMerchantId,autoCancelHoldDays,actionName,gameId);
			try{
				long LSTktNo =  CookieMgmtForTicketNumber.getTicketNumberFromCookie(servletRequest, userInfoBean.getUserName());
				if(LSTktNo !=0){
					lastPrintedTicket = LSTktNo/Util.getDivValueForLastSoldTkt(String.valueOf(LSTktNo).length());
					gameId = Util.getGameIdFromGameNumber(Util.getGamenoFromTktnumber(String.valueOf(LSTktNo)));
				}
				drawGameRPOSHelper.insertEntryIntoPrintedTktTableForWeb(gameId, userInfoBean.getUserOrgId(), lastPrintedTicket, INTERFACE_TYPE_WEB, Util.getCurrentTimeStamp(), actionName);
			}catch(Exception e){
				//e.printStackTrace();
			}
			MainPWTDrawBean pwtWinningBean = helper.payPwtTicket(drawScheduleBean,
					userInfoBean,highPrizeCriteria,highPrizeAmt,highPrizeScheme);
	
			if ("PWT_LIMIT_EXCEED".equalsIgnoreCase(drawScheduleBean.getStatus())) {
				pwtWinningBean.setStatus("PWT_LIMIT_EXCEED");
				throw new LMSException(LMSErrors.RG_LIMIT_EXCEPTION_ERROR_CODE, LMSErrors.RG_LIMIT_EXCEPTION_ERROR_MESSAGE);
			} else if ("INVALID_PWT_LIMIT_EXCEED".equalsIgnoreCase(drawScheduleBean.getStatus())) {
				pwtWinningBean.setStatus("INVALID_PWT_LIMIT_EXCEED");
				throw new LMSException(LMSErrors.RG_LIMIT_EXCEPTION_ERROR_CODE, LMSErrors.RG_LIMIT_EXCEPTION_ERROR_MESSAGE);
			}
			else if (pwtWinningBean.getStatus().equalsIgnoreCase("ERROR")) {
				throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE, LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
			} else {
				/*jsonResponse.put("isSuccess", true);
				jsonResponse.put("mainData", pwtWinningBean);
				jsonResponse.put("userName", userInfoBean.getUserName());
				jsonResponse.put("orgName", userInfoBean.getOrgName());
				jsonResponse.put("errorMsg", "");*/
				jsonResponse = parseWinningData(pwtWinningBean, userInfoBean, true,parentOrgName);
			}
			
		}catch (LMSException e) {
			jsonResponse.put("isSuccess", false);
			jsonResponse.put("errorCode",e.getErrorCode());
			jsonResponse.put("errorMsg", e.getErrorMessage());	
		}catch (Exception e) {
			e.printStackTrace();
		 	jsonResponse.put("isSuccess", false);
			jsonResponse.put("errorCode", LMSErrors.GENERAL_EXCEPTION_ERROR_CODE);
			jsonResponse.put("errorMsg", LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
	
	}
	logger.info("Response Data : {}"+ jsonResponse);
	out.print(jsonResponse);
	out.flush();
	out.close();


	return SUCCESS;
	}

	public String prizeWinningTicket(){
		
		PrintWriter out = null;
		JSONObject jsonResponse = new JSONObject();
		String parentOrgName = null;
		try{
			response.setContentType("application/json");
			out = response.getWriter();
			logger.info("Inside prizeWinningTicket");
			UserInfoBean userInfoBean = null;
			ServletContext sc = ServletActionContext.getServletContext();
			String refMerchantId = (String) sc.getAttribute("REF_MERCHANT_ID");
			
			JSONObject requestData = (JSONObject) JSONSerializer.toJSON(json);
			String ticketNumber = (String) requestData.get("ticketNumber");
			
			String userName = (String) requestData.get("userName");
			if(userName == null){
				session = servletRequest.getSession();
				userInfoBean = (UserInfoBean) session.getAttribute("USER_INFO");
			} else{
				session = getSession(userName);
				userInfoBean = getUserBean(userName);
			}
			parentOrgName = userInfoBean.getParentOrgName();
		
			int gameId=0;
			long lastPrintedTicket=0;
			
			String actionName=ActionContext.getContext().getName();
			DrawGameRPOSHelper drawGameRPOSHelper = new DrawGameRPOSHelper();
			//drawGameRPOSHelper.checkLastPrintedTicketStatusAndUpdate(userInfoBean,lastPrintedTicket,"WEB",refMerchantId,autoCancelHoldDays,actionName,gameId);
			try{
				long LSTktNo =  CookieMgmtForTicketNumber.getTicketNumberFromCookie(servletRequest, userInfoBean.getUserName());
				if(LSTktNo !=0){
					lastPrintedTicket = LSTktNo/Util.getDivValueForLastSoldTkt(String.valueOf(LSTktNo).length());
					gameId = Util.getGameIdFromGameNumber(Util.getGamenoFromTktnumber(String.valueOf(LSTktNo)));
				}
				drawGameRPOSHelper.insertEntryIntoPrintedTktTableForWeb(gameId, userInfoBean.getUserOrgId(), lastPrintedTicket, INTERFACE_TYPE_WEB, Util.getCurrentTimeStamp(), actionName);
			}catch(Exception e){
				//e.printStackTrace();
			}
			
			MainPWTDrawBean mainPwtBean = new MainPWTDrawBean();
			mainPwtBean.setInpType(Integer.parseInt((String) sc.getAttribute("InpType")));
			mainPwtBean.setTicketNo(ticketNumber);
	
			DrawGameRPOSHelper helper = new DrawGameRPOSHelper();
			logger.debug("Before--" + new Date());
			MainPWTDrawBean mainWinningBean = helper.prizeWinningTicket(
					mainPwtBean, userInfoBean, refMerchantId);
	
			if (mainWinningBean.getTotlticketAmount() > 0.0) {
				logger.debug("Creating beep sound with amount:::"
						+ mainWinningBean.getTotlticketAmount());
				Toolkit.getDefaultToolkit().beep();
			}
		
			if ("ERROR".equalsIgnoreCase(mainWinningBean.getStatus())) {
				throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE, LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
			} else if ("FRAUD".equalsIgnoreCase(mainWinningBean.getStatus())) {
				throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE, LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
			} else if ("ERROR_INVALID".equalsIgnoreCase(mainWinningBean.getStatus())) {
				throw new LMSException(LMSErrors.INVALID_TICKET_ERROR_CODE, LMSErrors.INVALID_TICKET_ERROR_MESSAGE);
			} else if ("CANCELLED".equalsIgnoreCase(mainWinningBean.getStatus())) {
				throw new LMSException(LMSErrors.TICKET_CANCELLED_ERROR_CODE, LMSErrors.TICKET_CANCELLED_ERROR_MESSAGE);
			} else if ("ALREADY_CANCELLED".equalsIgnoreCase(mainWinningBean.getStatus())) {
				throw new LMSException(LMSErrors.TICKET_ALREADY_CANCELLED_ERROR_CODE, LMSErrors.TICKET_ALREADY_CANCELLED_ERROR_MESSAGE);
			}else if("TICKET_EXPIRED".equalsIgnoreCase(mainWinningBean.getStatus())){
				throw new LMSException(LMSErrors.TICKET_EXPIRED_ERROR_CODE, LMSErrors.TICKET_EXPIRED_ERROR_MESSAGE);
			} else if (!mainWinningBean.isValid()) {
				throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE, LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
			} if ("UN_AUTH".equalsIgnoreCase(mainWinningBean.getStatus())) {
				throw new LMSException(LMSErrors.UNAUTHORIZED_PWT_CLAIM_ERROR_CODE, LMSErrors.UNAUTHORIZED_PWT_CLAIM_ERROR_MESSAGE);
			}else if("HIGH_PRIZE".equalsIgnoreCase(mainWinningBean.getStatus())){
				throw new LMSException(LMSErrors.HIGH_PRIZE_PWT_ERROR_CODE, LMSErrors.HIGH_PRIZE_PWT_ERROR_MESSAGE);
			}else if("OUT_VERIFY_LIMIT".equalsIgnoreCase(mainWinningBean.getStatus())){
				throw new LMSException(LMSErrors.OUT_VERIFY_LIMIT_ERROR_CODE, LMSErrors.OUT_VERIFY_LIMIT_ERROR_MESSAGE);
			}else if("OUT_PAY_LIMIT".equalsIgnoreCase(mainWinningBean.getStatus())){
				throw new LMSException(LMSErrors.OUT_PAY_LIMIT_ERROR_CODE, LMSErrors.OUT_PAY_LIMIT_ERROR_MESSAGE);
			}else{
				session.setAttribute("WinningBean", mainWinningBean);
				/*jsonResponse.put("isSuccess", true);
				jsonResponse.put("mainData", mainWinningBean);
				jsonResponse.put("errorMsg", "");*/
				jsonResponse = parseWinningData(mainWinningBean, userInfoBean, false, parentOrgName);
			}
		
		}catch (LMSException e) {
			jsonResponse.put("isSuccess", false);
			jsonResponse.put("errorCode",e.getErrorCode());
			jsonResponse.put("errorMsg", e.getErrorMessage());	
		}catch (Exception e) {
			e.printStackTrace();
		 	jsonResponse.put("isSuccess", false);
			jsonResponse.put("errorCode", LMSErrors.GENERAL_EXCEPTION_ERROR_CODE);
			jsonResponse.put("errorMsg", LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		
		}
		logger.info("Response Data : {}"+ jsonResponse);
		out.print(jsonResponse);
		out.flush();
		out.close();


		return SUCCESS;
	}

	private JSONObject parseWinningData(MainPWTDrawBean winningBean, UserInfoBean userBean, boolean isPay, String parentOrgName) throws LMSException {
		SimpleDateFormat inputFormat = null;
		SimpleDateFormat dateFormat = null;
		SimpleDateFormat timeFormat = null;

		JSONObject responseObject = new JSONObject();
		JSONArray drawArray = new JSONArray();
		JSONObject drawObject = null;
		try {
			responseObject = new JSONObject();
			drawArray = new JSONArray();
			drawObject = null;

			inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			//dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			timeFormat = new SimpleDateFormat("HH:mm:ss");

			double totalWinAmt = 0.00;
			String reprintCountPwt = null;
			for(PWTDrawBean pwtBean : winningBean.getWinningBeanList()) {
				reprintCountPwt = pwtBean.getReprintCount();
				for(DrawIdBean drawBean : pwtBean.getDrawWinList()) {
					String winAmt = drawBean.getWinningAmt();
					if(winAmt != null && winAmt.length()>0)
						totalWinAmt += Double.parseDouble(drawBean.getWinningAmt());

					Date inputDate = inputFormat.parse(drawBean.getDrawDateTime().split("\\*")[0]);
					drawObject = new JSONObject();
					if(drawBean.getDrawname()!= null && !"null".equalsIgnoreCase(drawBean.getDrawname())){
						drawObject.put("drawName", drawBean.getDrawname());
					}
					drawObject.put("drawDate", dateFormat.format(inputDate));
					drawObject.put("drawTime", timeFormat.format(inputDate));					
					drawObject.put("winStatus", "RES_AWAITED".equalsIgnoreCase(drawBean.getStatus())?"Result Awaited!!":"NORMAL_PAY".equalsIgnoreCase(drawBean.getStatus())?"Win!!":drawBean.getStatus());
					drawObject.put("winningAmt", drawBean.getWinningAmt());
					drawArray.add(drawObject);
				}
			}

			

			responseObject.put("isSuccess", true);
			responseObject.put("advMsg", winningBean.getAdvMsg());
			responseObject.put("gameName", Util.getGameDisplayName(winningBean.getGameId()));
			responseObject.put("gameDevName", Util.getGameMap().get(winningBean.getGameId()).getGameNameDev());
			responseObject.put("ticketNumber", winningBean.getTicketNo());
			responseObject.put("barcodeCount", winningBean.getTicketNo()+((ConfigurationVariables.currentTktLen == ConfigurationVariables.tktLenB && LMSFilterDispatcher.isBarCodeRequired)? winningBean.getBarcodeCount():""));
			responseObject.put("totalWinAmt", totalWinAmt);
			responseObject.put("totalPay", totalWinAmt);
			responseObject.put("orgName", Utility.getPropertyValue("ORG_NAME_JSP"));
			responseObject.put("retailerName", userBean.getUserName());
			responseObject.put("reprintCountPwt", reprintCountPwt);
			responseObject.put("currencySymbol", Utility.getPropertyValue("CURRENCY_SYMBOL"));
			responseObject.put("drawData", drawArray);

			if (isPay) {
				/*
				 * Object purchaseBean = winningBean.getPurchaseBean();
				 * if(purchaseBean instanceof KenoPurchaseBean) { Date
				 * purchaseDate =
				 * inputFormat.parse(kenoPurchaseBean.getPurchaseTime());
				 * 
				 * KenoPurchaseBean kenoBean = (KenoPurchaseBean) purchaseBean;
				 * reprintObject.put("orgName",
				 * Utility.getPropertyValue("ORG_NAME_JSP"));
				 * reprintObject.put("advMsg", kenoBean.getAdvMsg());
				 * reprintObject.put("retailerName", userBean.getUserName());
				 * reprintObject.put("ticketNumber", kenoBean.getTicket_no() +
				 * kenoBean.getReprintCount()); reprintObject.put("brCd",
				 * kenoBean.getTicket_no() + kenoBean.getReprintCount() +
				 * kenoBean.getBarcodeCount()); reprintObject.put("gameName",
				 * Util.getGameDisplayName(winningBean.getGameId()));
				 * reprintObject.put("purchaseDate",
				 * dateFormat.format(purchaseDate));
				 * reprintObject.put("purchaseTime",
				 * timeFormat.format(purchaseDate));
				 * reprintObject.put("purchaseAmt",
				 * kenoBean.getTotalPurchaseAmt()); reprintObject.put("isQp",
				 * kenoBean.getIsQuickPick()[0]); reprintObject.put("betName",
				 * kenoBean.getBetDispName()); reprintObject.put("", );
				 * reprintObject.put("", ); reprintObject.put("", );
				 * reprintObject.put("", );
				 * 
				 * drawArray = new JSONArray(); for(String drawNameDateTime :
				 * kenoPurchaseBean.getDrawDateTime()) { drawObject = new
				 * JSONObject();
				 * 
				 * Date inputDate =
				 * inputFormat.parse(drawNameDateTime.split("\\*")[0]); String
				 * drawName = (drawNameDateTime.split("\\*")[1]).split("&")[0];
				 * drawObject.put("drawName", drawName);
				 * drawObject.put("drawDate", dateFormat.format(inputDate));
				 * drawObject.put("drawTime", timeFormat.format(inputDate));
				 * drawArray.add(drawObject); } reprintObject.put("drawData",
				 * drawArray); }
				 */

				responseObject.put("isReprint", winningBean.isReprint());
				JSONArray betTypeArray = new JSONArray();
				JSONObject betTypeDataRes = null;
				boolean isQP = false;
				Object objBean = winningBean.getPurchaseBean();

				if (objBean instanceof KenoPurchaseBean) {
					KenoPurchaseBean bean = (KenoPurchaseBean) winningBean
							.getPurchaseBean();

					for (int i = 0; i < bean.getPlayerData().length; i++) {
						betTypeDataRes = new JSONObject();
						isQP = bean.getIsQuickPick()[i] == 1 ? true : false;
						betTypeDataRes.put("isQp", isQP);
						betTypeDataRes.put("betName", bean.getPlayType()[i]);
						betTypeDataRes.put("pickedNumbers", bean.getPlayerData()[i]);
						betTypeDataRes.put("numberPicked",bean.getPlayerData()[i].split(",").length);
						betTypeDataRes.put("unitPrice", bean.getUnitPrice()[i]);
						betTypeDataRes.put("noOfLines", bean.getNoOfLines()[i]);
						betTypeDataRes.put("betAmtMul", bean.getBetAmountMultiple()[i]);
						double panelPrice = bean.getUnitPrice()[i] * bean.getNoOfDraws();
						betTypeDataRes.put("panelPrice", panelPrice);
						betTypeArray.add(betTypeDataRes);
					}
					bean.setTicket_no(bean.getTicket_no()+bean.getReprintCount());
					responseObject.put("reprintData", bean);
				}
				if (objBean instanceof FortunePurchaseBean) {
					FortunePurchaseBean bean = (FortunePurchaseBean) winningBean.getPurchaseBean();

					isQP = false;
					for (int i = 0; i < bean.getBetAmountMultiple().length; i++) {
						betTypeDataRes = new JSONObject();
						isQP = bean.getIsQuickPick()[i] == 1 ? true : false;
						betTypeDataRes.put("isQp", isQP);
						betTypeDataRes.put("betName", "oneToTwelve");
						betTypeDataRes.put("pickedNumbers", bean.getPickedNumbers().get(i).toString());
						betTypeDataRes.put("unitPrice", bean.getUnitPrice());
						betTypeDataRes.put("noOfLines", "1");
						betTypeDataRes.put("betAmtMul", bean.getBetAmountMultiple()[i]);
						double panelPrice = bean.getBetAmountMultiple()[i] * bean.getUnitPrice() * 1 * bean.getNoOfDraws();
						betTypeDataRes.put("panelPrice", panelPrice);
						betTypeArray.add(betTypeDataRes);
					}
					bean.setTicket_no(bean.getTicket_no()+bean.getReprintCount());
					responseObject.put("reprintData", bean);
				}else if (objBean instanceof LottoPurchaseBean) {
					LottoPurchaseBean bean = (LottoPurchaseBean) winningBean.getPurchaseBean();
					isQP = false;
					for (int i = 0; i < bean.getBetAmountMultiple().length; i++) {
						betTypeDataRes = new JSONObject();
						isQP = bean.getIsQuickPick()[i] == 1 ? true : false;
						betTypeDataRes.put("isQp", isQP);
						betTypeDataRes.put("betName", "oneToTwelve");
						betTypeDataRes.put("pickedNumbers", bean.getPlayerPicked());
						betTypeDataRes.put("unitPrice", bean.getUnitPrice());
						betTypeDataRes.put("noOfLines", "1");
						betTypeDataRes.put("betAmtMul", bean.getBetAmountMultiple()[i]);
						double panelPrice = bean.getBetAmountMultiple()[i] * bean.getUnitPrice() * 1 * bean.getNoOfDraws();
						betTypeDataRes.put("panelPrice", panelPrice);
						betTypeArray.add(betTypeDataRes);
					}
					bean.setTicket_no(bean.getTicket_no()+bean.getReprintCount());
					responseObject.put("reprintData", bean);
				}
				responseObject.put("betTypeData", betTypeArray);
				responseObject.put("parentOrgName", parentOrgName);
			}
		} catch (Exception ex) {
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE, LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}

		return responseObject;
	}

	public void registerPlayer() throws IOException {
		session = servletRequest.getSession();
		UserInfoBean userInfoBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		DrawGameRPOSHelper helper = new DrawGameRPOSHelper();
		PWTDrawBean pwtBean = helper.registerPlayer(firstName, lastName,
				idNumber, idType,
				(PWTDrawBean) session.getAttribute("PWT_RES"), userInfoBean);
		PrintWriter out = getResponse().getWriter();
		if (pwtBean.getStatus().equals("SUCCESS")) {
			out.print("Registration Successful");
		} else {
			out.print("Error");
		}
	}

	/*public String reprintTicket() throws Exception {

		session = servletRequest.getSession();
		logger.info("Inside reprintTicket");
		UserInfoBean userInfoBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		logger.debug("Before--" + new Date());
		errMsg = "Last Transaction Not Sale";
		DrawGameRPOSHelper helper = new DrawGameRPOSHelper();
		Object gameBean = null;
		
		
		int gameId=0;
		long lastPrintedTicket=0;
		ServletContext  sc = LMSUtility.sc;
		String refMerchantId = (String) sc.getAttribute("REF_MERCHANT_ID");
		int autoCancelHoldDays=Integer.parseInt((String) sc.getAttribute("AUTO_CANCEL_CLOSER_DAYS"));
		
		String actionName=ActionContext.getContext().getName();
		DrawGameRPOSHelper drawGameRPOSHelper = new DrawGameRPOSHelper();
		//drawGameRPOSHelper.checkLastPrintedTicketStatusAndUpdate(userInfoBean,lastPrintedTicket,"WEB",refMerchantId,autoCancelHoldDays,actionName,gameId);
		try{
			long LSTktNo =  CookieMgmtForTicketNumber.getTicketNumberFromCookie(servletRequest, userInfoBean.getUserName());
			if(LSTktNo !=0){
				lastPrintedTicket = LSTktNo/Util.getDivValueForLastSoldTkt(String.valueOf(LSTktNo).length());
				gameId = Util.getGameIdFromGameNumber(Util.getGamenoFromTktnumber(String.valueOf(LSTktNo)));
			}
			drawGameRPOSHelper.insertEntryIntoPrintedTktTableForWeb(gameId, userInfoBean.getUserOrgId(), lastPrintedTicket, "WEB", Util.getCurrentTimeStamp(), actionName);
		}catch(Exception e){
			//e.printStackTrace();
		}
		
		
		if (ticketNo == null) {
			gameBean = helper.reprintTicket(userInfoBean,false,null,true,null,"WEB");
		} else {
			gameBean = helper.reprintTicket(userInfoBean,false,ticketNo,false,null,"WEB");
		}
		logger.debug("******gameBean::" + gameBean);
		isRG = "false";
		if (gameBean instanceof FortunePurchaseBean) {
			logger.debug(" FortunePurchaseBean reprint ");
			FortunePurchaseBean fortuneBean = (FortunePurchaseBean) gameBean;
			setFortuneBean(fortuneBean);
			return Util.getGameName(fortuneBean.getGame_no()).toUpperCase()
					+ "_REPRINT";
		} else if (gameBean instanceof LottoPurchaseBean) {

			LottoPurchaseBean lottoPurchaseBean = (LottoPurchaseBean) gameBean;
			setLottoPurchaseBean(lottoPurchaseBean);
			
			//added for numbers wrong print in ticket
			StringBuilder sb=new StringBuilder();
			if(lottoPurchaseBean.getPlayerPicked().size()>0){
				for(int i=0;i<lottoPurchaseBean.getPlayerPicked().size();i++){
					sb.append(lottoPurchaseBean.getPlayerPicked().get(i)+ ";");
				}
			}
			setPickNumStr(sb.toString());
			
			setPurchaseData(StringifyGameResponseForApplet.stringifyGameResponseForApplet(userInfoBean , lottoPurchaseBean));
			return Util.getGameName(lottoPurchaseBean.getGameId())
					.toUpperCase()
					+ "_REPRINT";
		} else if (gameBean instanceof KenoPurchaseBean) {
			logger.debug(" kenoPurchaseBean reprint ");
			KenoPurchaseBean kenoPurchaseBean = (KenoPurchaseBean) gameBean;
			setKenoTwoPurchaseBean(kenoPurchaseBean);
			setKenoPurchaseBean(kenoPurchaseBean);
			setTwelveByTwentyFourPurchaseBean(kenoPurchaseBean);
			return Util.getGameName(kenoPurchaseBean.getGameId())
					.toUpperCase()
					+ "_REPRINT";
		} else if (gameBean instanceof String
				&& "RG_RPERINT".equals(gameBean.toString())) {
			isRG = "true";
			errMsg = DGErrorMsg.buyErrMsg(gameBean.toString());
		} else if (gameBean instanceof String) {
			if (gameBean.toString().indexOf("ErrorCode") > -1) {
				errMsg = gameBean.toString().split("\\|ErrorCode")[0];
		    } else {
			errMsg = (String) gameBean;
			}
		}

		return ERROR;
	}*/

	public void reprintTicket() {
		try {
			logger.info("Data Received For Reprint :" +json);
			printWriter = response.getWriter();
			getRequiredInformationForTicketReprint();
			drawGameRPOSHelper.insertEntryIntoPrintedTktTableForWeb(gameId, userInfoBean.getUserOrgId(), lastPrintedTicket, INTERFACE_TYPE_WEB, Util.getCurrentTimeStamp(), actionName);
			isReprintFirstTime = (ticketNo == null) ? true : false;
			gameBean = drawGameRPOSHelper.reprintTicket(userInfoBean, false, ticketNo, isReprintFirstTime, null, INTERFACE_TYPE_WEB);
			if(gameBean != null){
				gameBeanType = gameBean.getClass().getSimpleName();
			}else{
				gameBeanType = "default";
			}
			webReprintContext = webReprintFactory.fetchReprintGameTypeInstance(gameBeanType);
			prepareWebReprintBean();
			jsonResponse = webReprintContext.reprintTicket(webReprint);
		} catch(LMSException e) {
			logger.error(EXCEPTION_OCCURRED, e);
			jsonResponse = prepareFailureResponse(e.getErrorCode(), e.getErrorMessage());
		} catch(Exception e) {
			logger.error(EXCEPTION_OCCURRED, e);
			jsonResponse = prepareFailureResponse(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		logger.info("ReprintTicket Response Data - " + jsonResponse);
		printWriter.print(jsonResponse);
		printWriter.flush();
		printWriter.close();
	}

	private void getRequiredInformationForTicketReprint() throws LMSException, Exception {
		jsonResponse = new JSONObject();
		if (json != null){
			requestData = (JSONObject) JSONSerializer.toJSON(json);
			userName = (String) requestData.get("userName");
		}
		if(userName == null){
			getUserInformationFromSessionIfUserNameIsNull();
		} else{
			getUserInformationFromSessionIfUserNameNotNull();
		}
		actionName = ActionContext.getContext().getName();
		lastTicketNumber =  CookieMgmtForTicketNumber.getTicketNumberFromCookie(servletRequest, userInfoBean.getUserName());
		if(lastTicketNumber != 0) {
			lastPrintedTicket = lastTicketNumber/Util.getDivValueForLastSoldTkt(String.valueOf(lastTicketNumber).length());
			gameId = Util.getGameIdFromGameNumber(Util.getGamenoFromTktnumber(String.valueOf(lastTicketNumber)));
		}
	}

	private void getUserInformationFromSessionIfUserNameIsNull() {
		session = servletRequest.getSession();
		userInfoBean = (UserInfoBean) session.getAttribute("USER_INFO");
	}
	
	private void getUserInformationFromSessionIfUserNameNotNull() throws LMSException {
		userName = (String)requestData.get("userName");
		session = getSession(userName);
		userInfoBean = getUserBean(userName);
	}

	private void prepareWebReprintBean() {
		webReprint = new WebReprint();
		webReprint.setGameBean(gameBean);
		webReprint.setUserInfoBean(userInfoBean);
	}
	
	private JSONObject prepareFailureResponse(int errorCode, String errorMessage){
		JSONObject jsonResponse = new JSONObject();
		jsonResponse.put("isSuccess", false);
		jsonResponse.put("errorCode", errorCode);
		jsonResponse.put("errorMsg", errorMessage);
		return jsonResponse;
	}
	
	public void setAutoCancel(boolean autoCancel) {
		this.autoCancel = autoCancel;
	}

	public void setDrawGameData(TreeMap drawGameData) {
		this.drawGameData = drawGameData;
	}

	public void setDrawGameNewData(Map<String, Map<Integer, DrawDetailsBean>> drawGameNewData) {
		this.drawGameNewData = drawGameNewData;
	}

	public void setEndRange(String endRange) {
		this.endRange = endRange;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setFortuneBean(FortunePurchaseBean fortuneBean) {
		this.fortuneBean = fortuneBean;
	}

	public void setFreezeTimeMap(TreeMap freezeTimeMap) {
		this.freezeTimeMap = freezeTimeMap;
	}

	public void setGameNo(int gameNo) {
		this.gameNo = gameNo;
	}

	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}

	public void setIdType(String idType) {
		this.idType = idType;
	}

	public void setIsRG(String isRG) {
		this.isRG = isRG;
	}

	/**
	 * This method sets the applet jre version into session
	 * 
	 * @throws Exception
	 */
	public void setJreVersion() throws Exception {
		session = servletRequest.getSession();
		session.setAttribute("jre_version", jreVersion);
	}

	public void setJreVersion(String jreVersion) {
		this.jreVersion = jreVersion;
	}

	public void setKenoPurchaseBean(KenoPurchaseBean kenoPurchaseBean) {
		this.kenoPurchaseBean = kenoPurchaseBean;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setLottoPurchaseBean(LottoPurchaseBean lottoPurchaseBean) {
		this.lottoPurchaseBean = lottoPurchaseBean;
	}

	public void setPickNum(String pickNum) {
		this.pickNum = pickNum;
	}

	public void setServerTime(String serverTime) {
		this.serverTime = serverTime;
	}

	public void setServletRequest(HttpServletRequest servletRequest) {
		this.servletRequest = servletRequest;

	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;

	}

	public void setStartRange(String startRange) {
		this.startRange = startRange;
	}

	public void setTicketNo(String ticketNo) {
		this.ticketNo = ticketNo;
	}

	public void setUnitPrice(Map unitPrice) {
		this.unitPrice = unitPrice;
	}

	public void setWinNum(String winNum) {
		this.winNum = winNum;
	}

	public KenoPurchaseBean getKenoTwoPurchaseBean() {
		return kenoTwoPurchaseBean;
	}

	public void setKenoTwoPurchaseBean(KenoPurchaseBean kenoTwoPurchaseBean) {
		this.kenoTwoPurchaseBean = kenoTwoPurchaseBean;
	}

	public KenoPurchaseBean getTwelveByTwentyFourPurchaseBean() {
		return twelveByTwentyFourPurchaseBean;
	}

	public void setTwelveByTwentyFourPurchaseBean(
			KenoPurchaseBean twelveByTwentyFourPurchaseBean) {
		this.twelveByTwentyFourPurchaseBean = twelveByTwentyFourPurchaseBean;
	}

	public void setPickNumStr(String pickNumStr) {
		this.pickNumStr = pickNumStr;
	}

	public String getPickNumStr() {
		return pickNumStr;
	}

	public String getPurchaseData() {
		return purchaseData;
	}

	public void setPurchaseData(String purchaseData) {
		this.purchaseData = purchaseData;
	}

	public String getCancelType() {
		return cancelType;
	}

	public void setCancelType(String cancelType) {
		this.cancelType = cancelType;
	}

	public long getLSTktNo() {
		return LSTktNo;
	}

	public void setLSTktNo(long lSTktNo) {
		LSTktNo = lSTktNo;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	

	
}