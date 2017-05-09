package com.skilrock.lms.keba.drawGames.playMgmt.action;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.ajax.AjaxRequestHelper;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.ConfigurationVariables;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;
import com.skilrock.lms.common.utility.LMSUtility;
import com.skilrock.lms.coreEngine.drawGames.common.DGErrorMsg;
import com.skilrock.lms.coreEngine.drawGames.playMgmt.DrawGameRPOSHelper;
import com.skilrock.lms.dge.beans.FortunePurchaseBean;
import com.skilrock.lms.dge.beans.RafflePurchaseBean;
import com.skilrock.lms.embedded.drawGames.common.CommonMethods;
import com.skilrock.lms.embedded.drawGames.common.EmbeddedErrors;
import com.skilrock.lms.web.drawGames.common.Util;
import com.skilrock.lms.web.drawGames.common.UtilApplet;

public class FortuneAction extends ActionSupport implements
		ServletRequestAware, ServletResponseAware {
	static Log logger = LogFactory.getLog(FortuneAction.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final List sunSign = Arrays.asList("", "Aries", "Taurus",
			"Gemini", "Cancer", "Leo", "Virgo", "Libra", "Scorpio",
			"Sagittarius", "Capricorn", "Aquarius", "Pisces");
	private String[] drawIdArr;
	private FortunePurchaseBean fortuneBean;
	//private int gameNumber = Util.getGameNumber("Fortune");
	private int gameId = Util.getGameId("Fortune");
	private int isAdvancedPlay;
	private int isQuickPick;
	private int noOfDraws;
	private String noOfPanels;
	private HttpServletRequest request;
	private HttpServletResponse response;
	HttpSession session = null;
	private String symbols;
	private int totalNoOfPanels;
	private String totalPurchaseAmt;

	private String userName;
	private long LSTktNo;
	
	
	private String plrMobileNumber;

	public String getPlrMobileNumber() {
		return plrMobileNumber;
	}

	public void setPlrMobileNumber(String plrMobileNumber) {
		this.plrMobileNumber = plrMobileNumber;
	}


	public String[] getDrawIdArr() {
		return drawIdArr;
	}

	public FortunePurchaseBean getFortuneBean() {
		return fortuneBean;
	}

	

	public int getIsAdvancedPlay() {
		return isAdvancedPlay;
	}

	public int getIsQuickPick() {
		return isQuickPick;
	}

	public int getNoOfDraws() {
		return noOfDraws;
	}

	public String getNoOfPanels() {
		return noOfPanels;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public String getSymbols() {
		return symbols;
	}

	public int getTotalNoOfPanels() {
		return totalNoOfPanels;
	}

	public String getTotalPurchaseAmt() {
		return totalPurchaseAmt;
	}

	public String getUserName() {
		return userName;
	}

	public void purchaseTicketProcess() throws Exception {
		logger.debug(" inside fortune action ---");
		ServletContext sc = ServletActionContext.getServletContext();
		String isDraw = (String) sc.getAttribute("IS_DRAW");
		response.setContentType("application/json");
		request.setAttribute("code", "MGMT");
		request.setAttribute("interfaceType", "TERMINAL");
		if (isDraw.equalsIgnoreCase("NO")) {
			response.getOutputStream().write(
					("errorMsg:" + EmbeddedErrors.DRAW_GAME_NOT_AVAILABLE)
							.getBytes());
			return;
		}
		Map currentUserSessionMap = (Map) sc.getAttribute("LOGGED_IN_USERS");
		// logger.debug(" LOGGED_IN_USERS maps is " + currentUserSessionMap);
		JSONObject jsonrep = new JSONObject();
		jsonrep.put("isSuccess",false);
		JSONObject commonSaleDataResp = new JSONObject();
		JSONObject jsonreq = 	(JSONObject) JSONSerializer.toJSON((String)request.getParameter("requestData"));
		JSONObject  commonSaleData = jsonreq.getJSONObject("commonSaleData");
		JSONArray   betTypeData = jsonreq.getJSONArray("betTypeData");
		int   qp =(((JSONObject)betTypeData.get(0)).getBoolean("isQp")) ? 1:2 ;
		int   isAdvancePlay =(commonSaleData.getBoolean("isAdvancePlay")) ? 1:0 ;
		//HttpSession session = (HttpSession) currentUserSessionMap.get(commonSaleData.get("userName"));
		HttpSession session = (HttpSession) currentUserSessionMap.get(jsonreq.get("userName"));

		// ----------------------------------
		if (stopSale()) {
			jsonrep.put("isSuccess",false);
			jsonrep.put("errorMsg","Your Sale is Stopped for Some Time");
			response.getOutputStream().write(jsonrep.toString().getBytes());
			return;
		} // for remove later added for kenya

		UserInfoBean userBean = (UserInfoBean) session.getAttribute("USER_INFO");
		String purchaseChannel = "LMS_Terminal";

		/*
		 * logger .debug("-----------------------User Org
		 * Id-------------------------------" + userBean.getUserOrgId());
		 */
		/*
		 * int[] panel_id = null; int[] betAmountMultiple =null; Integer[]
		 * isQuickPickNew = null; List<Integer> playerPicked = new ArrayList<Integer>();
		 * if (isQuickPick==1) { int index = 0; Map<Integer,Integer> qpData =
		 * new HashMap<Integer,Integer>(); for (int i = 0; i < totalNoOfPanels;
		 * i++) { index = Util.getRandomNo(1, sunSign.size()-1);
		 * qpData.put(index, qpData.get(index)==null?1:qpData.get(index)+1); }
		 * panel_id = new int[qpData.size()]; betAmountMultiple = new
		 * int[qpData.size()]; isQuickPickNew = new Integer[qpData.size()];
		 * 
		 * logger.debug(qpData+"Fortune---------"); Iterator<Integer> iter =
		 * qpData.keySet().iterator(); int i=0;int value =0; while
		 * (iter.hasNext()) { value = iter.next(); panel_id[i] = i + 1;
		 * betAmountMultiple[i] = qpData.get(value); isQuickPickNew[i] = 1;
		 * playerPicked.add(value); i++; } } else{ String[] noOfPanel =
		 * noOfPanels.split(","); String []pickedNumber = symbols.split(",");
		 * panel_id = new int[noOfPanel.length]; betAmountMultiple = new
		 * int[noOfPanel.length]; isQuickPickNew = new
		 * Integer[noOfPanel.length]; for (int i = 0; i < noOfPanel.length; i++) {
		 * playerPicked.add(sunSign.indexOf(pickedNumber[i])); panel_id[i] = i +
		 * 1; betAmountMultiple[i] = Integer.parseInt(noOfPanel[i]);
		 * isQuickPickNew[i] = isQuickPick; } } Collections.sort(playerPicked);
		 */
		int autoCancelHoldDays=Integer.parseInt((String) sc.getAttribute("AUTO_CANCEL_CLOSER_DAYS")); 
		String refMerchantId = (String) sc.getAttribute("REF_MERCHANT_ID");

		Map<Integer, Map<Integer, String>> drawIdTableMap = (Map<Integer, Map<Integer, String>>) sc.getAttribute("drawIdTableMap");
		//String str ="{'noOfPanels':1,'totalNoOfPanels':1,'symbols':'Cancer','noOfDraws':1,'totalPurchaseAmt':0.2,'noPicked':1,'isQuickPick':2,'betAmt':2,'isAdvancedPlay':0,'userName':'emm001','CRC':1039567545,'LSTktNo':0}";
	
		
		FortunePurchaseBean drawGamePurchaseBean = new FortunePurchaseBean();
		String playType =((JSONObject)betTypeData.get(0)).getString("betName");
		drawGamePurchaseBean.setPlayType(playType);
		//int qp = jsonreq.getBoolean("isQp") ? 1:2 ;
		
		String symbols = "";
		String noOfPanels = "";
		int totalCount =0;
		if(qp!=1){
			
			for(int i=0;i<betTypeData.size();i++){
				JSONObject dataBean = new JSONObject();
				dataBean=(JSONObject) betTypeData.get(i);
				if(i==0){
					symbols =symbols+dataBean.getString("pickedNumbers");
					noOfPanels=noOfPanels+dataBean.getInt("betAmtMul");
					totalCount =totalCount+dataBean.getInt("betAmtMul");
				}else{
					symbols =symbols+","+dataBean.getString("pickedNumbers");
					noOfPanels=noOfPanels+","+dataBean.getInt("betAmtMul");
					totalCount =totalCount+dataBean.getInt("betAmtMul");
				}
				
				
			}
			
		}else{
			
			totalCount =(((JSONObject)betTypeData.get(0)).getInt("betAmtMul"));
		}
		
		drawGamePurchaseBean.setIsQP(qp);
		drawGamePurchaseBean.setTotalNoOfPanels(totalCount);
		drawGamePurchaseBean.setSymbols(symbols);
		drawGamePurchaseBean.setNoOfPanels(noOfPanels);
		
		// drawGamePurchaseBean.setBetAmountMultiple(betAmountMultiple);
		drawGamePurchaseBean.setDrawIdTableMap(drawIdTableMap);
		long lastPrintedTicket=0;
		int lstGameId =0;
		long LSTktNo =commonSaleData.getLong("lastTktNo");
		//int totalNoOfPanels =jsonreq.getInt("totalSignCount");
		if(LSTktNo !=0){
			lastPrintedTicket = LSTktNo/Util.getDivValueForLastSoldTkt(String.valueOf(LSTktNo).length());
			lstGameId = Util.getGameIdFromGameNumber(Util.getGamenoFromTktnumber(String.valueOf(LSTktNo)));
		}
		if (isAdvancePlay!=0){
			JSONArray drawIdListArray =commonSaleData.getJSONArray("drawData");
			ArrayList<String> drawIdList = new ArrayList<String>();
			for(int i=0;i<drawIdListArray.size();i++){
				JSONObject drawId = new JSONObject();
				drawId=(JSONObject) drawIdListArray.get(i);
				drawIdList.add(String.valueOf(drawId.getInt("drawId")));
			}
			if (drawIdList != null) {
				drawGamePurchaseBean.setDrawDateTime(drawIdList);
			}
			
		}
		
		
		drawGamePurchaseBean.setGame_no(Util.getGameNumberFromGameId(gameId));
		drawGamePurchaseBean.setGameId(gameId);
		drawGamePurchaseBean.setGameDispName(Util.getGameDisplayName(gameId));
		// drawGamePurchaseBean.setIsQuickPick(isQuickPickNew);
		drawGamePurchaseBean.setNoOfDraws(commonSaleData.getInt("noOfDraws"));
		drawGamePurchaseBean.setPartyId(userBean.getUserOrgId());
		drawGamePurchaseBean.setPartyType(userBean.getUserType());
		drawGamePurchaseBean.setUserId(userBean.getUserId());
		drawGamePurchaseBean.setRefMerchantId(refMerchantId);
		drawGamePurchaseBean.setPurchaseChannel(purchaseChannel);
		drawGamePurchaseBean.setIsAdvancedPlay(isAdvancePlay);
		drawGamePurchaseBean.setPlrMobileNumber(null);
		String barcodeType = (String) LMSUtility.sc.getAttribute("BARCODE_TYPE");
		drawGamePurchaseBean.setBarcodeType(barcodeType);
		
		String actionName=ActionContext.getContext().getName();
		DrawGameRPOSHelper helper = new DrawGameRPOSHelper();
		helper.checkLastPrintedTicketStatusAndUpdate(userBean,lastPrintedTicket,"TERMINAL",refMerchantId,autoCancelHoldDays,actionName,lstGameId);
		// drawGamePurchaseBean.setPanel_id(panel_id);
		// drawGamePurchaseBean.setPickedNumbers(playerPicked);
		// drawGamePurchaseBean.setTotalPurchaseAmt(Double.parseDouble(totalPurchaseAmt));

		StringBuilder finalPurchaseData = new StringBuilder();
		//drawGamePurchaseBean.setLastSoldTicketNo(lastSoldTicketNo);
		
		
		/*String lastSoldTicketNo = "0";
		if(!"0".equals(LSTktNo) && LSTktNo!=null){
			lastSoldTicketNo = LSTktNo.substring(0, LSTktNo.length()-2);
		}
		
		
		DrawGameRPOSHelper helper = new DrawGameRPOSHelper();
		long lastSoldTktLMS = helper.getLastSoldTicketTerminal(userBean.getUserOrgId(),"TERMINAL");
		if(lastSoldTktLMS != Long.parseLong(lastSoldTicketNo) && lastSoldTktLMS != 0 && lastSoldTktLMS != 0 && !"0".equals(LSTktNo) && LSTktNo!=null){
			CancelTicketBean cancelTicketBean = new CancelTicketBean();
			cancelTicketBean.setDrawIdTableMap(drawIdTableMap);
			cancelTicketBean.setTicketNo(lastSoldTktLMS + "00");
			cancelTicketBean.setPartyId(userBean.getUserOrgId());
			cancelTicketBean.setPartyType(userBean.getUserType());
			cancelTicketBean.setUserId(userBean.getUserId());
			cancelTicketBean.setCancelChannel("LMS_Terminal");
			cancelTicketBean.setRefMerchantId(refMerchantId);
			cancelTicketBean.setAutoCancel(true);
			cancelTicketBean = helper.cancelTicket(cancelTicketBean,
					userBean, true);
		}*/
		
		if (totalCount < 1) {
			drawGamePurchaseBean.setSaleStatus("ERROR");
			setFortuneBean(drawGamePurchaseBean);
			// return SUCCESS;
			// response.getOutputStream().write("ERROR".getBytes());
			//finalPurchaseData.append("ErrorMsg:"+ EmbeddedErrors.PURCHSE_INVALID_DATA);
			System.out.println("FINAL PURCHASE DATA FORTUNE:"+ finalPurchaseData);
			jsonrep.put("isSuccess",false);
			jsonrep.put("errorMsg", EmbeddedErrors.PURCHSE_INVALID_DATA);
			response.getOutputStream().write(jsonrep.toString().getBytes());
			return;
		}

		FortunePurchaseBean fortuneBean = helper.fortunePurchaseTicket(
				userBean, drawGamePurchaseBean);
		
		setFortuneBean(fortuneBean);		
		fortuneBean = getFortuneBean();
		String saleStatus = getFortuneBean().getSaleStatus();
		if (!"SUCCESS".equalsIgnoreCase(saleStatus)) {
			/*finalPurchaseData.append("ErrorMsg:" + DGErrorMsg.buyErrMsg(saleStatus)
					+ "|");*/
			jsonrep.put("isSuccess",false);
			jsonrep.put("errorMsg",DGErrorMsg.buyErrMsg(saleStatus));
			System.out.println("FINAL PURCHASE DATA FORTUNE:"+ finalPurchaseData);
			response.getOutputStream().write(jsonrep.toString().getBytes());
			return;
		}
		// logger.debug(fortuneBean.getPurchaseTime());

		String time = fortuneBean.getPurchaseTime().replace(" ", "|Time:")
				.replace(".0", "");
		//StringBuilder stBuilder = new StringBuilder("");
		//int betAmountMultiple[] = fortuneBean.getBetAmountMultiple();
		JSONArray panelData = new JSONArray();
		for (int i = 0; i < fortuneBean.getPickedNumbers().size(); i++) {
			JSONObject panelObj = new JSONObject();
			panelObj.put("pickedNumbers",sunSign.get(fortuneBean.getPickedNumbers().get(i)));
			panelObj.put("betAmtMul",  fortuneBean.getBetAmountMultiple()[i] );
			panelObj.put("isQp", (((JSONObject)betTypeData.get(0)).getBoolean("isQp")) );
			panelObj.put("betName","Fortune");
			panelData.add(panelObj);
			//stBuilder.append(sunSign.get(fortuneBean.getPickedNumbers().get(i))	+ ":" + fortuneBean.getBetAmountMultiple()[i] + "|");
		}
		int listSize = fortuneBean.getDrawDateTime().size();
		//StringBuilder drawTimeBuild = new StringBuilder("");
	
		JSONArray  drawDataArray = new JSONArray();
		for (int i = 0; i < listSize; i++) {
			JSONObject drawObject = new JSONObject();
			String drawData =(String)fortuneBean.getDrawDateTime().get(i) ;
			drawObject.put("drawId",Integer.parseInt(drawData.split("\\*")[0]));
			drawObject.put("drawDate",drawData.split("\\*")[1].split(" ")[0]);
			drawObject.put("drawTime", drawData.split("\\*")[1].split(" ")[1]);
			drawObject.put("drawName", drawData.split("\\*")[2]);
			drawDataArray.add(drawObject);
			/*drawDatearray.add(fortuneBean
					.getDrawDateTime().get(i).toString().split(" ")[0]);
			drawTimearray.add(fortuneBean
					.getDrawDateTime().get(i).toString().split(" ")[1]);
			drawTimeBuild.append(("|DrawDate:" + fortuneBean
					.getDrawDateTime().get(i)).replaceFirst(" ", "#").replace(
					"#", "|DrawTime:").replace("&", "|DrawId:").replace(".0", ""));*/
		}
		//jsonrep.put("DrawDatearray", drawDatearray);
		//jsonrep.put("DrawTimearray", drawTimearray);
		AjaxRequestHelper ajxHelper = new AjaxRequestHelper();
		ajxHelper.getAvlblCreditAmt(userBean);

		double bal = userBean.getAvailableCreditLimit()- userBean.getClaimableBal();
		
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(2);
		String balance = nf.format(bal).replaceAll(",", "");
		
		/*String balance = bal + "00";
		balance = balance.substring(0, balance.indexOf(".") + 3);*/

		StringBuilder topMsgsStr = new StringBuilder("");
		StringBuilder bottomMsgsStr = new StringBuilder("");
		String advtMsg = "";

		UtilApplet.getAdvMsgs(fortuneBean.getAdvMsg(), topMsgsStr,bottomMsgsStr, 10);

		if (topMsgsStr.length() != 0) {
			advtMsg = "topAdvMsg:" + topMsgsStr + "|";
		}

		if (bottomMsgsStr.length() != 0) {
			advtMsg = advtMsg + "bottomAdvMsg:" + bottomMsgsStr + "|";
		}
		List<RafflePurchaseBean> rafflePurchaseBeanList = fortuneBean.getRafflePurchaseBeanList();
		String raffleData = CommonMethods.buildRaffleData(rafflePurchaseBeanList);

		commonSaleDataResp.put("ticketNumber",fortuneBean.getTicket_no()+ fortuneBean.getReprintCount());
		commonSaleDataResp.put("brCd", (fortuneBean.getTicket_no() + fortuneBean.getReprintCount()+((ConfigurationVariables.currentTktLen == ConfigurationVariables.tktLenB && LMSFilterDispatcher.isBarCodeRequired)? fortuneBean.getBarcodeCount():"")));
		commonSaleDataResp.put("gameName", fortuneBean.getGameDispName());
	//	jsonrep.put("Date", fortuneBean.getPurchaseTime().split(" ")[0]);
		commonSaleDataResp.put("purchaseDate", fortuneBean.getPurchaseTime().split(" ")[0]);
		commonSaleDataResp.put("purchaseTime",  fortuneBean.getPurchaseTime().split(" ")[1].replace(".0",""));
		commonSaleDataResp.put("purchaseAmt",  fortuneBean.getTotalPurchaseAmt() );
		commonSaleDataResp.put("balance",bal);
		//commonSaleDataResp.put("raffleData",raffleData );
		commonSaleDataResp.put("topAdvMsg",topMsgsStr );
		commonSaleDataResp.put("bottomAdvMsg",bottomMsgsStr );
		//commonSaleDataResp.put("totalQuantity",totalCount);
		commonSaleDataResp.put("unitPrice", Util.getUnitPrice(drawGamePurchaseBean.getGameId(), playType));
		commonSaleDataResp.put("drawData", drawDataArray); 
		jsonrep.put("commonSaleData", commonSaleDataResp);
		jsonrep.put("betTypeData", panelData);

		//finalPurchaseData.append("TicketNo:" + fortuneBean.getTicket_no() + fortuneBean.getReprintCount()+"|brCd:"+fortuneBean.getTicket_no() + fortuneBean.getReprintCount()+((ConfigurationVariables.currentTktLen == ConfigurationVariables.tktLenB && LMSFilterDispatcher.isBarCodeRequired)? fortuneBean.getBarcodeCount():"")+"|Date:" + time+"|");
		//finalPurchaseData.append("TicketCost:"+ fortuneBean.getTotalPurchaseAmt() + drawTimeBuild.toString()+ "|balance:" + balance + "|QP:" + fortuneBean.getIsQP() + "|"+ raffleData + advtMsg);
		//finalPurchaseData.append("TicketCost:"+ fortuneBean.getTotalPurchaseAmt() + drawTimeBuild.toString()+ "|balance:" + balance +"|"+ raffleData + advtMsg);
		
		
		System.out.println("FINAL PURCHASE DATA FORTUNE:" + jsonrep.toString());
		jsonrep.put("isSuccess",true);
		jsonrep.put("errorMsg","");
		//request.setAttribute("purchaseData", finalPurchaseData.toString());
		response.getOutputStream().write((jsonrep.toString()).getBytes());
	}

	public void setDrawIdArr(String[] drawIdArr) {
		this.drawIdArr = drawIdArr;
	}

	public void setFortuneBean(FortunePurchaseBean fortuneBean) {
		this.fortuneBean = fortuneBean;
	}

	

	public void setIsAdvancedPlay(int isAdvancedPlay) {
		this.isAdvancedPlay = isAdvancedPlay;
	}

	public void setIsQuickPick(int isQuickPick) {
		this.isQuickPick = isQuickPick;
	}

	public void setNoOfDraws(int noOfDraws) {
		this.noOfDraws = noOfDraws;
	}

	public void setNoOfPanels(String noOfPanels) {
		this.noOfPanels = noOfPanels;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;

	}

	public void setSymbols(String symbols) {
		this.symbols = symbols;
	}

	public void setTotalNoOfPanels(int totalNoOfPanels) {
		this.totalNoOfPanels = totalNoOfPanels;
	}

	public void setTotalPurchaseAmt(String totalPurchaseAmt) {
		this.totalPurchaseAmt = totalPurchaseAmt;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	private boolean stopSale() throws ParseException {

		Calendar cal = Calendar.getInstance();

		int weekDay = cal.get(Calendar.DAY_OF_WEEK);
		SimpleDateFormat frmt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

		String currDate = new java.sql.Date(cal.getTimeInMillis()).toString();

		String strt = currDate + " 23:05:00";
		String end = currDate + " 23:15:00";

		String satStart = currDate + " 18:00:00";
		String satEnd = currDate + " 18:30:00";

		long currTime = cal.getTimeInMillis();

		long strtTime = frmt.parse(strt).getTime();
		long endTime = frmt.parse(end).getTime();
		long satStartTime = frmt.parse(satStart).getTime();
		long satEndTime = frmt.parse(satEnd).getTime();

		if (weekDay > 1 && weekDay < 7) {
			// System.out.println("***week days******");
			if (currTime > strtTime && currTime < endTime) {
				System.out.println("stopSale() method has been called");
				System.out.println("***current date:***" + currDate + "***");
				System.out.println("***current time:***" + cal.getTime()
						+ "***");
				System.out.println("**currTime:**" + currTime + "***");
				System.out.println("**strtTime:**" + strtTime + "***");
				System.out.println("**endTime:**" + endTime + "***");
				return true;
			}
		} else if (weekDay == 7) {
			// System.out.println("***saturday******");
			if (currTime > satStartTime && currTime < satEndTime) {
				System.out.println("stopSale() method has been called");
				System.out.println("***current date:***" + currDate + "***");
				System.out.println("***current time:***" + cal.getTime()
						+ "***");
				System.out.println("**currTime:**" + currTime + "***");
				System.out.println("**satStartTime:**" + satStartTime + "***");
				System.out.println("**satEndTime:**" + satEndTime + "***");
				return true;
			}
		}

		return false;
	}

	public int getGameId() {
		return gameId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public long getLSTktNo() {
		return LSTktNo;
	}

	public void setLSTktNo(long lSTktNo) {
		LSTktNo = lSTktNo;
	}

}
