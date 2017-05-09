package com.skilrock.lms.web.drawGames.playMgmt;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
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

import com.opensymphony.xwork2.ActionContext;
import com.skilrock.lms.ajax.AjaxRequestHelper;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.BaseAction;
import com.skilrock.lms.common.ConfigurationVariables;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;
import com.skilrock.lms.common.utility.CommonMethods;
import com.skilrock.lms.common.utility.FormatNumber;
import com.skilrock.lms.coreEngine.drawGames.common.DGErrorMsg;
import com.skilrock.lms.coreEngine.drawGames.playMgmt.DrawGameRPOSHelper;
import com.skilrock.lms.coreEngine.drawGames.playMgmt.KenoSixHelper;
import com.skilrock.lms.dge.beans.KenoPurchaseBean;
import com.skilrock.lms.rest.services.common.daoImpl.TpEBetMgmtDaoImpl;
import com.skilrock.lms.web.drawGames.common.CookieMgmtForTicketNumber;
import com.skilrock.lms.web.drawGames.common.Util;

public class KenoSixAction extends BaseAction{

	public KenoSixAction () {
		super(KenoSixAction.class.getName());
	}

	static Log logger = LogFactory.getLog(KenoSixAction.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final List numbers = Arrays.asList("", "Zero(0)", "One(1)",
			"Two(2)", "Three(3)", "Four(4)", "Five(5)", "Six(6)", "Seven(7)",
			"Eight(8)", "Nine(9)");

	private int gameId = Util.getGameId("KenoSix");
	private KenoPurchaseBean kenoPurchaseBean;
	private long LSTktNo;
	private String errMsg;
	private String json;
	
	public static void main(String[] args) throws Exception {
		new KenoAction().purchaseTicketProcess();
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

/*public static List<String> rec(int start, int elementToChoose,
			int returnCnt, String[] indexArr, String[] elements,
			StringBuffer stbuff, List<String> comboList, String qp) {

		if (returnCnt == elementToChoose) {
			return comboList;
		}
		returnCnt++;
		for (int i = start; i < elements.length; i++) {

			indexArr[returnCnt - 1] = "" + i;
			if (returnCnt == elementToChoose) {

				stbuff = new StringBuffer();
				for (String element : indexArr) {
					stbuff.append("," + elements[Integer.parseInt(element)]);
				}
				stbuff.delete(0, 1);
				comboList.add(stbuff.toString());
				if ("No".equalsIgnoreCase(qp)) {
					comboList.add("Nxt");
				} else if ("Yes".equalsIgnoreCase(qp)) {
					comboList.add("QP");
				}
			}

			rec(++start, elementToChoose, returnCnt, indexArr, elements,
					stbuff, comboList, qp);
		}
		return comboList;
	}*/
	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public KenoPurchaseBean getKenoPurchaseBean() {
		return kenoPurchaseBean;
	}

	public long getLSTktNo() {
		return LSTktNo;
	}

	public void setLSTktNo(long lSTktNo) {
		LSTktNo = lSTktNo;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}
/*public void getLines() throws IOException {
		String[] indexArr = new String[2];
		StringBuffer stbuild = null;
		List<String> comboList = new ArrayList<String>();
		String[] numbArr = new String[Integer.parseInt(pickedNum)];
		comboList = rec(0, 2, 0, indexArr, numbArr, stbuild, comboList, "Line");
		PrintWriter out = getResponse().getWriter();
		logger.debug("lines******" + comboList.size());
		out.print(comboList.size());
	}*/
	
	public void purchaseTicketProcess() {
		String pickedData[] = null;
		String[] playType = null;
		List<String> drawDateTime = new ArrayList<String>();
		StringBuilder cost = null;
		StringBuilder ticket = null;
		logger.info("className: {} --In Keno Six Purchase Ticket-- ");
		logger.debug("Inside purchaseTicketProcess");
		
		PrintWriter out = null;
		JSONObject jsonResponse = new JSONObject();
		try {
			ServletContext sc = ServletActionContext.getServletContext();
			response.setContentType("application/json");
			out = response.getWriter();
			Map<Integer, Map<Integer, String>> drawIdTableMap = (Map<Integer, Map<Integer, String>>) sc.getAttribute("drawIdTableMap");

			JSONObject requestData = (JSONObject) JSONSerializer.toJSON(json);
			JSONObject commonSaleDataReq = (JSONObject) requestData.get("commonSaleData");
			JSONArray betTypeDataReq = (JSONArray) requestData.get("betTypeData");
			String totalPurchaseAmt = (String) requestData.get("totalPurchaseAmt");
			
			String userName = (String) requestData.get("userName");
			UserInfoBean userBean = userName == null ? getUserBean() : getUserBean(userName);
			
			long lastPrintedTicket = 0;
			int lstGameId = 0;
			String actionName = ActionContext.getContext().getName();
			DrawGameRPOSHelper helper = new DrawGameRPOSHelper();
			try {
				LSTktNo = CookieMgmtForTicketNumber.getTicketNumberFromCookie(request, userBean.getUserName());
				if (LSTktNo != 0) {
					lastPrintedTicket = LSTktNo / Util.getDivValueForLastSoldTkt(String.valueOf(LSTktNo).length());
					lstGameId = Util.getGameIdFromGameNumber(Util.getGamenoFromTktnumber(String.valueOf(LSTktNo)));
				}

				helper.insertEntryIntoPrintedTktTableForWeb(lstGameId, userBean.getUserOrgId(), lastPrintedTicket, "WEB", Util.getCurrentTimeStamp(), actionName);
			} catch (Exception e) {
				 e.printStackTrace();
			}

			int noOfDraws = Integer.parseInt(commonSaleDataReq.getString("noOfDraws").trim());
			int isAdvancedPlay = "false".equals(commonSaleDataReq.getString("isAdvancePlay").trim()) ? 0 : 1;
			int panelSize = betTypeDataReq.size();
			int[] isQuickPick = new int[panelSize];
			pickedData = new String[panelSize];
			String noPicked[] = new String[panelSize];
			playType = new String[panelSize];
			int[] betAmountMultiple = new int[panelSize];
			boolean [] qpPreGenerated = new boolean[panelSize];
			for (int i = 0; i < panelSize; i++) {
				JSONObject panelData = betTypeDataReq.getJSONObject(i);
				isQuickPick[i] = panelData.getBoolean("isQp") == true ? 1 : 2;
				pickedData[i] = panelData.getString("pickedNumbers");
				noPicked[i] = panelData.getString("noPicked");
				playType[i] = panelData.getString("betName");
				betAmountMultiple[i] = panelData.getInt("betAmtMul");
				qpPreGenerated[i] = panelData.getBoolean("QPPreGenerated");
			}
			JSONArray drawDataArr = commonSaleDataReq.getJSONArray("drawData");
			int drawSize = drawDataArr.size();
			String[] drawIdArr = new String[drawSize];
			if(!commonSaleDataReq.getBoolean("isDrawManual")){
				for (int i = 0; i < drawSize; i++) {
					JSONObject drawData = drawDataArr.getJSONObject(i);
					drawIdArr[i] = String.valueOf(drawData.getInt("drawId"));
				}
			}

			String refMerchantId = (String) sc.getAttribute("REF_MERCHANT_ID");
			
			KenoPurchaseBean drawGamePurchaseBean = new KenoPurchaseBean();
			drawGamePurchaseBean.setGameId(gameId);
			drawGamePurchaseBean.setGame_no(Util.getGameNumberFromGameId(gameId));
			drawGamePurchaseBean.setGameDispName(Util.getGameDisplayName(gameId));
			
			drawGamePurchaseBean.setDrawIdTableMap(drawIdTableMap);
			drawGamePurchaseBean.setIsQuickPick(isQuickPick);
			drawGamePurchaseBean.setPlayerData(pickedData);
			drawGamePurchaseBean.setNoPicked(noPicked);
			drawGamePurchaseBean.setPlayType(playType);
			drawGamePurchaseBean.setBetAmountMultiple(betAmountMultiple);
			drawGamePurchaseBean.setGame_no(Util.getGameNumberFromGameId(gameId));
			drawGamePurchaseBean.setNoOfDraws(noOfDraws);
			drawGamePurchaseBean.setDrawDateTime(Arrays.asList(drawIdArr));
			drawGamePurchaseBean.setIsAdvancedPlay(isAdvancedPlay);
			drawGamePurchaseBean.setRefMerchantId(refMerchantId);
			drawGamePurchaseBean.setPurchaseChannel("LMS_Web");
			drawGamePurchaseBean.setBonus("N");
			drawGamePurchaseBean.setServiceId(Util.getServiceIdFormCode(request.getAttribute("code").toString()));
			drawGamePurchaseBean.setNoOfPanel(panelSize);
			drawGamePurchaseBean.setUserId(userBean.getUserId());
			drawGamePurchaseBean.setPartyId(userBean.getUserOrgId());
			drawGamePurchaseBean.setPartyType(userBean.getUserType());
			drawGamePurchaseBean.setUserMappingId(userBean.getCurrentUserMappingId());
			drawGamePurchaseBean.setTotalPurchaseAmt(Double.parseDouble(totalPurchaseAmt));
			drawGamePurchaseBean.setQPPreGenerated(qpPreGenerated) ;
			
			if (isAdvancedPlay == 1 && drawIdArr == null) {
				setErrMsg(DGErrorMsg.buyErrMsg(""));
				throw new LMSException(LMSErrors.INVALID_DATA_ERROR_CODE, LMSErrors.INVALID_DATA_ERROR_MESSAGE);
			}

			if (drawIdArr != null) {
				drawGamePurchaseBean.setDrawDateTime(Arrays.asList(drawIdArr));
			}

			boolean isValid =new KenoSixHelper().kenoValidateData(drawGamePurchaseBean);

			if (!isValid) {
				drawGamePurchaseBean.setSaleStatus("INVALID_INPUT");
				setKenoPurchaseBean(drawGamePurchaseBean);
				throw new LMSException(LMSErrors.INVALID_DATA_ERROR_CODE, LMSErrors.INVALID_DATA_ERROR_MESSAGE);
			} 
			
			if ((String)requestData.get("tokenId") != null) {
				if (!TpEBetMgmtDaoImpl.getInstance().isBetSlipActive((String)requestData.get("tokenId"))) {
					throw new LMSException(LMSErrors.BET_SLIP_EXPIRED_ERROR_CODE, LMSErrors.BET_SLIP_EXPIRED_ERROR_MESSAGE);
				}
			}
			
			kenoPurchaseBean = new KenoSixHelper().commonPurchseProcess(userBean,drawGamePurchaseBean);
			kenoPurchaseBean = getKenoPurchaseBean();
			String saleStatus = getKenoPurchaseBean().getSaleStatus();
			if (!"SUCCESS".equalsIgnoreCase(saleStatus)) {
				if ("AGT_INS_BAL".equalsIgnoreCase(saleStatus))
					throw new LMSException(LMSErrors.INSUFFICIENT_AGENT_BALANCE_ERROR_CODE, LMSErrors.INSUFFICIENT_AGENT_BALANCE_ERROR_MESSAGE);
				else if ("RET_INS_BAL".equalsIgnoreCase(saleStatus))
					throw new LMSException(LMSErrors.INSUFFICIENT_RETAILER_BALANCE_ERROR_CODE, LMSErrors.INSUFFICIENT_RETAILER_BALANCE_ERROR_MESSAGE);
				else if ("NO_DRAWS".equalsIgnoreCase(saleStatus))
					throw new LMSException(LMSErrors.TRANSACTION_FAILED_ERROR_CODE, LMSErrors.TRANSACTION_FAILED_ERROR_MESSAGE);
				else if ("FRAUD".endsWith(saleStatus))
					throw new LMSException(LMSErrors.PURCHASE_FRAUD_ERROR_CODE, LMSErrors.PURCHASE_FRAUD_ERROR_MESSAGE);
				else if ("UNAUTHORISED".endsWith(saleStatus))
					throw new LMSException(LMSErrors.UNAUTHORIZED_SALE_ERROR_CODE, LMSErrors.UNAUTHORIZED_SALE_ERROR_MESSAGE);
				
				throw new LMSException(LMSErrors.INVALID_DATA_ERROR_CODE, LMSErrors.INVALID_DATA_ERROR_MESSAGE);
			}else {	
			    	
				JSONArray betTypeArray = new JSONArray();
				JSONObject betTypeDataRes = null;
				boolean isQP = false ;
				for (int i=0; i<panelSize; i++) {
					JSONObject panelData = betTypeDataReq.getJSONObject(i);
					betTypeDataRes = new JSONObject();
					isQP = drawGamePurchaseBean.getIsQuickPick()[i] == 1 ? true : false; 
					betTypeDataRes.put("isQp", isQP);
					betTypeDataRes.put("betName", drawGamePurchaseBean.getPlayType()[i]);
					betTypeDataRes.put("pickedNumbers", kenoPurchaseBean.getPlayerData()[i]);
					betTypeDataRes.put("numberPicked", drawGamePurchaseBean.getNoPicked()[i]);
					//betTypeDataRes.put("unitPrice", drawGamePurchaseBean.getUnitPrice()[i]);
					betTypeDataRes.put("unitPrice", drawGamePurchaseBean.getUnitPrice()[i]);
					betTypeDataRes.put("noOfLines", drawGamePurchaseBean.getNoOfLines()[i]);
					betTypeDataRes.put("betAmtMul", panelData.getInt("betAmtMul"));
					double panelPrice = panelData.getInt("betAmtMul") * drawGamePurchaseBean.getUnitPrice()[i] * drawGamePurchaseBean.getNoOfLines()[i] * drawGamePurchaseBean.getNoOfDraws();
					betTypeDataRes.put("panelPrice", panelPrice);
					betTypeArray.add(betTypeDataRes);
				}

				int listSize = kenoPurchaseBean.getDrawDateTime().size();
				JSONArray drawDataArray = new JSONArray();
				JSONObject drawData = null;
				for (int i=0; i<listSize; i++) {
					String drawDataString = (String) kenoPurchaseBean.getDrawDateTime().get(i);
					drawData = new JSONObject();
					if(!commonSaleDataReq.getBoolean("isDrawManual")){
						drawData.put("drawId", drawIdArr[i]);
					}
					drawData.put("drawDate", drawDataString.split(" ")[0]);
					String drawName[] = drawDataString.split("\\*");
					if(drawName.length < 2) {
						//drawData.put("drawName", "");
						drawData.put("drawTime", drawDataString.split("&")[0].split(" ")[1]);
						drawDateTime.add(drawDataString.split(" ")[0] + " "+drawDataString.split("&")[0].split(" ")[1]);
					}
					else {
						if(!"null".equalsIgnoreCase(drawDataString.split("\\*")[1].split("&")[0]))
							drawData.put("drawName", drawDataString.split("\\*")[1].split("&")[0]);
						drawData.put("drawTime", drawDataString.split("\\*")[0].split(" ")[1]);
						drawDateTime.add(drawDataString.split(" ")[0] + " "+drawDataString.split("&")[0].split(" ")[1]);
					}
					drawDataArray.add(drawData);
				}
				JSONObject commonSaleDataRes = new JSONObject();
				commonSaleDataRes.put("ticketNumber", kenoPurchaseBean.getTicket_no());
				ticket = new StringBuilder(kenoPurchaseBean.getTicket_no()).append(kenoPurchaseBean.getReprintCount());
				commonSaleDataRes.put("gameName", kenoPurchaseBean.getGameDispName());
				commonSaleDataRes.put("purchaseDate", kenoPurchaseBean.getPurchaseTime().split(" ")[0]);
				commonSaleDataRes.put("purchaseTime", kenoPurchaseBean.getPurchaseTime().split(" ")[1]);
				commonSaleDataRes.put("purchaseAmt", kenoPurchaseBean.getTotalPurchaseAmt());
				commonSaleDataRes.put("barcodeCount", kenoPurchaseBean.getTicket_no() + kenoPurchaseBean.getReprintCount()+((ConfigurationVariables.currentTktLen == ConfigurationVariables.tktLenB && LMSFilterDispatcher.isBarCodeRequired)? kenoPurchaseBean.getBarcodeCount():""));
				cost = new StringBuilder(String.valueOf(kenoPurchaseBean.getTotalPurchaseAmt()));
				commonSaleDataRes.put("drawData", drawDataArray);


				JSONObject mainData = new JSONObject();
				mainData.put("commonSaleData", commonSaleDataRes);
				mainData.put("betTypeData", betTypeArray);
				mainData.put("advMessage", kenoPurchaseBean.getAdvMsg());
				mainData.put("orgName", userBean.getOrgName());
				mainData.put("userName", userBean.getUserName());
				new  AjaxRequestHelper().getAvlblCreditAmt(userBean);
				mainData.put("AvlblCreditAmt",FormatNumber.formatNumber(userBean.getAvailableCreditLimit() - userBean.getClaimableBal()));
				mainData.put("parentOrgName", userBean.getParentOrgName());

				jsonResponse.put("isSuccess", true);
				jsonResponse.put("errorMsg", "");
				jsonResponse.put("mainData", mainData);
				jsonResponse.put("isPromo", false);
				if (requestData.get("tokenId") != null && !((String)requestData.get("tokenId")).trim().isEmpty()) {
			    	    Util.setEbetSaleRequestStatusDone((String)requestData.get("tokenId"), userBean.getUserOrgId());
				}
				CookieMgmtForTicketNumber.checkAndUpdateTicketsDetails(request, response, userBean.getUserName(), getKenoPurchaseBean().getTicket_no() + getKenoPurchaseBean().getReprintCount());
			}
		} catch(LMSException e){
			jsonResponse.put("isSuccess", false);
			jsonResponse.put("errorCode", e.getErrorCode());
			jsonResponse.put("errorMsg", e.getErrorMessage());
		} catch(Exception e){
			e.printStackTrace();
			jsonResponse.put("isSuccess", false);
			jsonResponse.put("errorCode", LMSErrors.GENERAL_EXCEPTION_ERROR_CODE);
			jsonResponse.put("errorMsg", LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		
		}

		logger.info("className: {} Keno Six Sale Response Data : {}"+ jsonResponse);
		if("SUCCESS".equalsIgnoreCase(kenoPurchaseBean.getSaleStatus())){
			String smsData = CommonMethods.prepareSMSData(pickedData, playType, cost, ticket, drawDateTime);
			CommonMethods.sendSMS(smsData);
		}
		out.print(jsonResponse);
		out.flush();
		out.close();

		
	}
/*	
public String purchaseTicketProcess() {
		logger.debug("Inside purchaseTicketProcess");
		try {
				HttpSession session = request.getSession();
				UserInfoBean userBean = (UserInfoBean) session.getAttribute("USER_INFO");
				ServletContext sc = ServletActionContext.getServletContext();
				Map<Integer, Map<Integer, String>> drawIdTableMap = (Map<Integer, Map<Integer, String>>)sc	.getAttribute("drawIdTableMap");		
				String refMerchantId = (String) sc.getAttribute("REF_MERCHANT_ID");
				int autoCancelHoldDays=Integer.parseInt((String) sc.getAttribute("AUTO_CANCEL_CLOSER_DAYS")); 
				//String purchaseChannel = "LMS_Terminal";
		
				logger.debug("pickedNumbers ::" + pickedNumbers);
				logger.debug("noOfDraws ::" + noOfDraws);
				logger.debug("totalPurchaseAmt ::" + totalPurchaseAmt);
				logger.debug("playType ::" + playType);
				logger.debug("QP ::" + QP);
				logger.debug("betAmt ::" + betAmt);
				logger.debug("noOfLines ::" + noOfLines);
				logger.debug("noPicked ::" + noPicked);

				String[] betAmtStr = betAmt.split("Nxt");
				String[] QPStr = QP.split("Nxt");
				String[] pickedNumbersArr = pickedNumbers.split("Nxt");
				String[] noPickedArr = noPicked.replaceAll(" ", "").split("Nxt");
				String[] playTypeArr = playType.split("Nxt");
				int noOfPanel = pickedNumbersArr.length;
				int[] betAmtArr = new int[noOfPanel];
				int[] QPArr = new int[noOfPanel];
		
				for (int i = 0; i < noOfPanel; i++) {
					betAmtArr[i] = Integer.parseInt(betAmtStr[i]);
					QPArr[i] = Integer.parseInt(QPStr[i]);
				}
		
				KenoPurchaseBean drawGamePurchaseBean = new KenoPurchaseBean();
				long lastPrintedTicket=0;
				int lstGameId =0;
		
				
				if (drawIdArr != null) {
					drawGamePurchaseBean.setDrawDateTime(Arrays.asList(drawIdArr));
				}
				if(isAdvancedPlay==1 && drawIdArr==null){
					//there is some error in dala selection from front end
					setErrMsg(DGErrorMsg.buyErrMsg(""));
					return ERROR;
				}

				String actionName=ActionContext.getContext().getName();
				KenoSixHelper helper = new KenoSixHelper();
				//new DrawGameRPOSHelper().checkLastPrintedTicketStatusAndUpdate(userBean,lastPrintedTicket,"WEB",refMerchantId,autoCancelHoldDays,actionName,lstGameId);
				try{
					LSTktNo =  CookieMgmtForTicketNumber.getTicketNumberFromCookie(request, userBean.getUserName());
					if(LSTktNo !=0){
						lastPrintedTicket = LSTktNo/Util.getDivValueForLastSoldTkt(String.valueOf(LSTktNo).length());
						lstGameId = Util.getGameIdFromGameNumber(Util.getGamenoFromTktnumber(String.valueOf(LSTktNo)));
					}
					new DrawGameRPOSHelper().insertEntryIntoPrintedTktTableForWeb(lstGameId, userBean.getUserOrgId(), lastPrintedTicket, "WEB", Util.getCurrentTimeStamp(), actionName);
				}catch(Exception e){
					//e.printStackTrace();
				}
			
				drawGamePurchaseBean.setGame_no(Util.getGameNumberFromGameId(gameId));
				drawGamePurchaseBean.setGameId(gameId);
				drawGamePurchaseBean.setGameDispName(Util.getGameDisplayName(gameId));
				drawGamePurchaseBean.setBetAmountMultiple(betAmtArr);
				drawGamePurchaseBean.setIsQuickPick(QPArr);
				drawGamePurchaseBean.setPlayerData(pickedNumbersArr);
				drawGamePurchaseBean.setNoPicked(noPickedArr);
				drawGamePurchaseBean.setPartyId(userBean.getUserOrgId());
				drawGamePurchaseBean.setPartyType(userBean.getUserType());
				drawGamePurchaseBean.setUserId(userBean.getUserId());
				drawGamePurchaseBean.setNoOfDraws(noOfDraws);
				drawGamePurchaseBean.setIsAdvancedPlay(isAdvancedPlay);
				drawGamePurchaseBean.setPlrMobileNumber(plrMobileNumber);
				drawGamePurchaseBean.setRefMerchantId(refMerchantId);
				drawGamePurchaseBean.setPurchaseChannel("LMS_Web");
				drawGamePurchaseBean.setBonus("N");
				drawGamePurchaseBean.setTotalPurchaseAmt(Double.parseDouble(totalPurchaseAmt));
				drawGamePurchaseBean.setPlayType(playTypeArr);
				drawGamePurchaseBean.setDrawIdTableMap(drawIdTableMap);
				drawGamePurchaseBean.setNoOfPanel(noOfPanel);		
				String barcodeType = (String) LMSUtility.sc.getAttribute("BARCODE_TYPE");
				drawGamePurchaseBean.setBarcodeType(barcodeType);	
		
				String countryDeployed = (String) sc.getAttribute("COUNTRY_DEPLOYED");         
				if("NIGERIA".equals(countryDeployed)){      	
					SimpleDateFormat parser = new SimpleDateFormat("HH:mm:ss");
					String bettypeOffStartTime=(String) sc.getAttribute("BETTYPE_OFF_START_TIME");
					logger.info("BETTYPE_OFF_START_TIME::"+bettypeOffStartTime);
					Date start = parser.parse(bettypeOffStartTime);
					String bettypeOffEndTime=(String) sc.getAttribute("BETTYPE_OFF_END_TIME");
					logger.info("BETTYPE_OFF_END_TIME::"+bettypeOffEndTime);
	         		Date end = parser.parse(bettypeOffEndTime);
	         		String currDateString= parser.format(new Date());
	         		Date userDate = parser.parse(currDateString);
	         		logger.info(currDateString);
 			
	         		if (userDate.after(start) && userDate.before(end)) {
	         			try{     
	         					for(String betType:playTypeArr){
	         						if("Direct4".equals(betType) || "Direct5".equals(betType)){
	         							setErrMsg(DGErrorMsg.buyErrMsg("ErrorMsg:Server Busy Amount Not Deducted"));
	         							logger.info("ErrorMsg:No Direct4 and Direct5 Bet Type Till "+bettypeOffEndTime);
	         							return ERROR;
	         						}
	         					}
	         				}
	         			catch (Exception e) {
	         					e.printStackTrace();
	         					setErrMsg(DGErrorMsg.buyErrMsg("ErrorMsg:Server Busy Amount Not Deducted"));
	         					logger.info("ErrorMsg:Some Internal Error "+bettypeOffEndTime);
	         					return ERROR;
	         			}
	         		}		
				}
		 
				kenoPurchaseBean = helper.commonPurchseProcess(userBean,drawGamePurchaseBean);
				setKenoPurchaseBean(kenoPurchaseBean);
				kenoPurchaseBean = getKenoPurchaseBean();
				logger.debug(kenoPurchaseBean.getPlayerPicked() + "msg---------"+kenoPurchaseBean.getTicket_no());	
		
				String finalPurchaseData = null;
				String saleStatus = getKenoPurchaseBean().getSaleStatus();
				if (!"SUCCESS".equalsIgnoreCase(saleStatus)) {
					setErrMsg(DGErrorMsg.buyErrMsg(saleStatus));
					return ERROR;
				}		

				//String time = kenoPurchaseBean.getPurchaseTime().replace(" ", "|Time:").replace(".0", "");		
				double bal = userBean.getAvailableCreditLimit()- userBean.getClaimableBal();		
				NumberFormat nf = NumberFormat.getInstance();
				nf.setMinimumFractionDigits(2);
				String balance = nf.format(bal).replaceAll(",", "");
				int listSize = kenoPurchaseBean.getDrawDateTime().size();
				StringBuilder drawTimeBuild = new StringBuilder("");		
				for (int i = 0; i < listSize; i++) {
					drawTimeBuild.append(("|DrawDate:" + kenoPurchaseBean.getDrawDateTime().get(i)).replaceFirst(" ", "#").replace("#", "|DrawTime:").replace("&", "|DrawId:").replace(".0", ""));
				}	
			return SUCCESS;
		} catch (LMSException e) {
			e.printStackTrace();			
			setErrMsg(DGErrorMsg.buyErrMsg("Error!Try Again"));
			return ERROR;
		} catch (SQLException e) {
			e.printStackTrace();
			setErrMsg(DGErrorMsg.buyErrMsg("Error!Try Again"));
			return ERROR;
		}catch (Exception e) {
			e.printStackTrace();
			setErrMsg(DGErrorMsg.buyErrMsg("Error!Try Again"));
			return ERROR;
		}
	}
	
	public String reprintTicket() throws Exception {
		logger.debug("Inside reprintTicket");
		HttpSession session = request.getSession();
		UserInfoBean userInfoBean = (UserInfoBean) session.getAttribute("USER_INFO");
		DrawGameRPOSHelper helper = new DrawGameRPOSHelper();
		setKenoPurchaseBean((KenoPurchaseBean) helper.reprintTicket(userInfoBean));
		logger.debug("kenoSix reprint ok");
		return SUCCESS;
	}*/


	public void setKenoPurchaseBean(KenoPurchaseBean kenoPurchaseBean) {
		this.kenoPurchaseBean = kenoPurchaseBean;
	}


	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public int getGameId() {
		return gameId;
	}

}
