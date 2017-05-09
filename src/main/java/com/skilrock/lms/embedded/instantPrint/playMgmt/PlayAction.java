package com.skilrock.lms.embedded.instantPrint.playMgmt;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.camlot.coreEngine.common.CommonCamlotHelper;
import com.skilrock.ipe.Bean.GameLMSBean;
import com.skilrock.ipe.Bean.TicketPurchaseLMSBean;
import com.skilrock.lms.ajax.AjaxRequestHelper;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.commercialService.common.CSPWSaleHelper;
import com.skilrock.lms.coreEngine.commercialService.common.CSUtil;
import com.skilrock.lms.coreEngine.instantPrint.common.IPEErrorMsg;
import com.skilrock.lms.coreEngine.instantPrint.common.IPEUtility;
import com.skilrock.lms.coreEngine.instantPrint.gameMgmt.common.NewGameUploadHelper;
import com.skilrock.lms.coreEngine.instantPrint.playMgmt.PlayHelper;
import com.skilrock.lms.coreEngine.instantPrint.playMgmt.RetPwtProcessHelper;
import com.skilrock.lms.embedded.drawGames.common.EmbeddedErrors;
import com.skilrock.lms.rolemgmt.beans.userBean;
import com.skilrock.lms.web.drawGames.common.UtilApplet;


public class PlayAction extends ActionSupport implements ServletRequestAware,
		ServletResponseAware {

	private static final long serialVersionUID = 1L;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private int gameId;
	private String userName;
	private String ticketNo;
	private boolean isAutoCancel;
	private int lastTransId;

	
	

	public int getLastTransId() {
		return lastTransId;
	}

	public void setLastTransId(int lastTransId) {
		this.lastTransId = lastTransId;
	}

	public boolean isAutoCancel() {
		return isAutoCancel;
	}

	public void setAutoCancel(boolean isAutoCancel) {
		this.isAutoCancel = isAutoCancel;
	}

	public String getTicketNo() {
		return ticketNo;
	}

	public void setTicketNo(String ticketNo) {
		this.ticketNo = ticketNo;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getGameId() {
		return gameId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}

	public void gameData() throws Exception {
		//NewGameUploadHelper.onStartGame();
		ServletContext sc = ServletActionContext.getServletContext();
		String isIPE = (String) sc.getAttribute("IS_IPE");
		if ("NO".equalsIgnoreCase(isIPE)) {
			response.getOutputStream().write(
					"ErrorMsg:Instant Game Not Available|".getBytes());
			return;
		}

		Map<String, HttpSession> currentUserSessionMap = (Map<String, HttpSession>) sc
				.getAttribute("LOGGED_IN_USERS");
		HttpSession session = currentUserSessionMap.get(userName);
		if (session == null) {
			response
					.getOutputStream()
					.write(
							("ErrorMsg:" + EmbeddedErrors.SESSION_EXPIRED + "ErrorCode:01|")
									.getBytes());
			return;
		}

		Map<Integer, GameLMSBean> gameMap = IPEUtility.gameMap;
		Iterator<Map.Entry<Integer, GameLMSBean>> iter = gameMap.entrySet()
				.iterator();
		StringBuilder gamestr = new StringBuilder("InstantPrintSD:");
		
		while (iter.hasNext()) {
			Map.Entry<Integer, GameLMSBean> pair = (Map.Entry<Integer, GameLMSBean>) iter
					.next();
			int gameId = pair.getKey();
			GameLMSBean gameBean = pair.getValue();
			gamestr.append(gameId + ",");
			gamestr.append(gameBean.getGameName() + ",");
			gamestr.append(gameBean.getGameNo() + ",");
			gamestr.append((gameBean.getIsSample().equalsIgnoreCase("BUY") ? 0: 1)+ ",");
			gamestr.append(gameBean.getTicketPrice() + ",");
			gamestr.append(gameBean.getTextOrImage().substring(0, 1) + ",");
			gamestr.append(gameBean.getGameLogoType().substring(0, 1) + ",");
			gamestr.append(gameBean.getPrizeLogoType().substring(0, 1) + ",");
			gamestr.append(gameBean.getGamePrintScheme() + "%#");
		}
		gamestr.delete(gamestr.lastIndexOf("%#"), gamestr.length());
		gamestr.append("|");
		System.out.println(gamestr.toString());
		response.getOutputStream().write(gamestr.toString().getBytes());
	}

	public void imageData() throws Exception {
		//NewGameUploadHelper.onStartGame();
		ServletContext sc = ServletActionContext.getServletContext();
		String isIPE = (String) sc.getAttribute("IS_IPE");
		if ("NO".equalsIgnoreCase(isIPE)) {
			response.getOutputStream().write(
					"ErrorMsg:Instant Game Not Available|".getBytes());
			return;
		}

		Map<String, HttpSession> currentUserSessionMap = (Map<String, HttpSession>) sc
		.getAttribute("LOGGED_IN_USERS");
		HttpSession session = currentUserSessionMap.get(userName);
		if (session == null) {
			response
			.getOutputStream()
			.write(
					("ErrorMsg:" + EmbeddedErrors.SESSION_EXPIRED + "ErrorCode:01|")
					.getBytes());
			return;
		}

		Map<Integer, GameLMSBean> gameMap = IPEUtility.gameMap;
		GameLMSBean gameBean = gameMap.get(gameId);
		StringBuilder gamestr = new StringBuilder("gameImageData:");

		gamestr.append(gameId + ",");
		Iterator<Map.Entry<String, byte[]>> iterImgData = gameBean
		.getImageDataMap().entrySet().iterator();
		Iterator<Map.Entry<String, Integer>> iterImgSize = gameBean
		.getImageSizeMap().entrySet().iterator();
		int sizeImgDataMap = gameBean.getImageDataMap().size();
		int sizeImgSizeMap = gameBean.getImageSizeMap().size();
		if (sizeImgDataMap == sizeImgSizeMap) {
			gamestr.append(sizeImgDataMap + ",");
			response.getOutputStream().write(gamestr.toString().getBytes());
			while (iterImgSize.hasNext()) {
				gamestr = new StringBuilder();
				Map.Entry<String, byte[]> data = (Map.Entry<String, byte[]>) iterImgData
				.next();
				Map.Entry<String, Integer> size = (Map.Entry<String, Integer>) iterImgSize
				.next();
				gamestr.append(size.getKey() + ",");
				gamestr.append(size.getValue() + ",");
				response.getOutputStream().write(gamestr.toString().getBytes());
				/*line = data.getValue();
						System.out.println("Length: "+line.length+"  data.getvalue: "+data.getValue().length);*/
				response.getOutputStream().write(data.getValue());
				/*for(int i=0;i<line.length;i++)
						gamestr.
					//gamestr.append(data.getValue());
					System.out.println("\n\n\nData for image no :" + (cntr++) );
					if(cntr==4){
					for(int i=0;i<line.length;i++)
						System.out.println("index[" + (i) + "] = " + (int)line[i]);
					}*/
			}
		}
		System.out.println("Image data fetched");
		response.getOutputStream().write("|".getBytes());
	}

	@SuppressWarnings("unchecked")
	public void purchaseTicketProcess() throws Exception {
		// NewGameUploadHelper.onStartGame();
		ServletContext sc = ServletActionContext.getServletContext();
		String isIPE = (String) sc.getAttribute("IS_IPE");
		if ("NO".equalsIgnoreCase(isIPE)) {
			response.getOutputStream().write(
					"ErrorMsg:Instant Game Not Available|".getBytes());
			return;
		}
		Map<String, HttpSession> currentUserSessionMap = (Map<String, HttpSession>) sc
		.getAttribute("LOGGED_IN_USERS");
		HttpSession session = currentUserSessionMap.get(userName);
		if (session == null) {
			response
			.getOutputStream()
			.write(
					("ErrorMsg:" + EmbeddedErrors.SESSION_EXPIRED + "ErrorCode:01|")
					.getBytes());
			return;
		}

		String refMerchantId = (String) sc.getAttribute("REF_MERCHANT_ID_IPE");
		UserInfoBean userBean = (UserInfoBean) session
		.getAttribute("USER_INFO");
		if(!(lastTransId+"").equalsIgnoreCase(IPEUtility.fetchLastIPETransId(userBean.getUserOrgId()))){
			response
			.getOutputStream()
			.write("ErrorMsg:Reprint Last Ticket|ErrorCode:03".getBytes());
			return;

		}
		TicketPurchaseLMSBean ticketBean = new TicketPurchaseLMSBean();
		ticketBean.setUserId(userBean.getUserId());
		ticketBean.setPartyId(userBean.getUserOrgId());
		ticketBean.setPartyType(userBean.getUserType());
		ticketBean.setGameId(gameId);
		ticketBean.setPurChannel("LMS_Terminal");
		ticketBean.setRefMerId(refMerchantId);
		ticketBean.setPurchaseTime(new Timestamp(new Date().getTime()));
		Map<Integer, GameLMSBean> gameMap = IPEUtility.gameMap;
		GameLMSBean gameBean = gameMap.get(gameId);

		PlayHelper helper = new PlayHelper();
		ticketBean = helper.purchaseTicketProcess(ticketBean, userBean);
		String finalPurchaseData = null;
		String saleStatus = ticketBean.getSaleStatus();
		if (!"SUCCESS".equalsIgnoreCase(saleStatus)) {
			finalPurchaseData = "ErrorMsg:" + IPEErrorMsg.buyErrMsg(saleStatus)
			+ "|";
			System.out.println("Final Purchase Data ::" + finalPurchaseData);
			response.getOutputStream().write(finalPurchaseData.getBytes());
			return;
		}
		StringBuilder topMsgsStr = new StringBuilder("");
		StringBuilder bottomMsgsStr = new StringBuilder("");
		String advtMsg = "";

		/*UtilApplet.getAdvMsgs(ticketBean.getAdvMsg(), topMsgsStr,
				bottomMsgsStr, 10);

		if (topMsgsStr.length() != 0) {
			advtMsg = "topAdvMsg:" + topMsgsStr + "|";
		}

		if (bottomMsgsStr.length() != 0) {
			advtMsg = advtMsg + "bottomAdvMsg:" + bottomMsgsStr + "|";
		}*/

		AjaxRequestHelper ajxHelper = new AjaxRequestHelper();
		ajxHelper.getAvlblCreditAmt(userBean);
		double bal = userBean.getAvailableCreditLimit()
		- userBean.getClaimableBal();
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMinimumFractionDigits(2);
		String balance = nf.format(bal).replaceAll(",", "");
		StringBuilder finalData = new StringBuilder("purchaseData:");
		String date = ticketBean.getPurchaseTime().toString();
		String gameLogoCode = gameBean.getGameLogoCode();
		//String gamePrizeCode = gameBean.getImageTypeMap().get("PRIZE").get(0);
		finalData.append(date.substring(0, date.indexOf(".")) + ","
				+ ticketBean.getTicketNo() + "," + ticketBean.getVirnNo()
				+ ","+gameLogoCode+"," + ticketBean.getImgList().replace(",", ":")
				+ ","+ticketBean.getPrizeCode()+","+ticketBean.getRefTransId() +"," +balance+ "|" + advtMsg);

		finalPurchaseData = finalData.toString();
		System.out.println("FINAL PURCHASE DATA :" + finalPurchaseData);
		response.getOutputStream().write(finalPurchaseData.getBytes());
	}

	@SuppressWarnings("unchecked")
	public void cancelTicketProcess() throws IOException {
		//NewGameUploadHelper.onStartGame();
		ServletContext sc = ServletActionContext.getServletContext();
		String isIPE = (String) sc.getAttribute("IS_IPE");
		if ("NO".equalsIgnoreCase(isIPE)) {
			response.getOutputStream().write(
					"ErrorMsg:Instant Game Not Available|".getBytes());
			return;
		}
		Map<String, HttpSession> currentUserSessionMap = (Map<String, HttpSession>) sc
				.getAttribute("LOGGED_IN_USERS");
		HttpSession session = currentUserSessionMap.get(userName);
		if (session == null) {
			response
					.getOutputStream()
					.write(
							("ErrorMsg:" + EmbeddedErrors.SESSION_EXPIRED + "ErrorCode:01|")
									.getBytes());
			return;
		}
		String refMerchantId = (String) sc.getAttribute("REF_MERCHANT_ID_IPE");
		UserInfoBean userBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		TicketPurchaseLMSBean ticketBean = new TicketPurchaseLMSBean();
		ticketBean.setTicketNo(ticketNo);
		ticketBean.setRefMerId(refMerchantId);
		ticketBean.setPurChannel("LMS_Terminal");
		PlayHelper helper = new PlayHelper();
		ticketBean = helper.cancelTicketManual(ticketBean, userBean,
				isAutoCancel() ? "PRINTER" : "MANUAL");
		String saleStatus = ticketBean.getSaleStatus();
		String finalPurchaseData = null;
		if (!"SUCCESS".equalsIgnoreCase(saleStatus)) {
			finalPurchaseData = "ErrorMsg:Ticket Can not Cancel|";
			System.out.println("Final Cancel Data ::" + finalPurchaseData);
			response.getOutputStream().write(finalPurchaseData.getBytes());
			return;
		}

		StringBuilder finalData = new StringBuilder();
		finalData.append("GameNo:" + ticketBean.getGameNo() + "|");
		finalData.append("GameName:" + ticketBean.getGameName() + "|");
		finalData.append("TicketNo:" + ticketBean.getTicketNo() + "|");
		finalData.append("TicketCost:" + ticketBean.getTotalAmt() + "|");
		finalPurchaseData = finalData.toString();
		System.out.println("FINAL PURCHASE DATA :" + finalPurchaseData);
		response.getOutputStream().write(finalPurchaseData.getBytes());
	}
	
	public void reprintLastTrans() throws IOException{
	//	NewGameUploadHelper.onStartGame();
		ServletContext sc = ServletActionContext.getServletContext();
		String isIPE = (String) sc.getAttribute("IS_IPE");
		if ("NO".equalsIgnoreCase(isIPE)) {
			response.getOutputStream().write(
					"ErrorMsg:Instant Game Not Available|".getBytes());
			return;
		}
		Map<String, HttpSession> currentUserSessionMap = (Map<String, HttpSession>) sc
		.getAttribute("LOGGED_IN_USERS");
		HttpSession session = currentUserSessionMap.get(userName);
		if (session == null) {
			response
			.getOutputStream()
			.write(
					("ErrorMsg:" + EmbeddedErrors.SESSION_EXPIRED + "ErrorCode:01|")
					.getBytes());
			return;
		}
		PlayHelper helper = new PlayHelper();
		String refMerchantId = (String) sc.getAttribute("REF_MERCHANT_ID_IPE");
		UserInfoBean userBean = (UserInfoBean) session
		.getAttribute("USER_INFO");
		TicketPurchaseLMSBean ticketBean = new TicketPurchaseLMSBean();
		ticketBean.setUserId(userBean.getUserId());
		ticketBean.setPartyId(userBean.getUserOrgId());
		ticketBean.setPartyType(userBean.getUserType());
		int id = helper.fetchGameId(lastTransId);
		ticketBean.setGameId(id);
		ticketBean.setPurChannel("LMS_Terminal");
		ticketBean.setRefMerId(refMerchantId);
		ticketBean.setRefTransId(lastTransId);
		ticketBean.setPurchaseTime(new Timestamp(new Date().getTime()));
		Map<Integer, GameLMSBean> gameMap = IPEUtility.gameMap;
		GameLMSBean gameBean = gameMap.get(id);
		ticketBean.setGameNo(gameBean.getGameNo());
		ticketBean = helper.reprintLastTicket(ticketBean, userBean);
		String reprintData = null;
		String saleStatus = ticketBean.getSaleStatus();
		if (!"SUCCESS".equalsIgnoreCase(saleStatus)) {
			reprintData = "ErrorMsg:" + IPEErrorMsg.buyErrMsg(saleStatus)
			+ "|";
			System.out.println("Reprint Data ::" + reprintData);
			response.getOutputStream().write(reprintData.getBytes());
			return;
		}
		StringBuilder topMsgsStr = new StringBuilder("");
		StringBuilder bottomMsgsStr = new StringBuilder("");
		String advtMsg = "";

		/*UtilApplet.getAdvMsgs(ticketBean.getAdvMsg(), topMsgsStr,
				bottomMsgsStr, 10);

		if (topMsgsStr.length() != 0) {
			advtMsg = "topAdvMsg:" + topMsgsStr + "|";
		}

		if (bottomMsgsStr.length() != 0) {
			advtMsg = advtMsg + "bottomAdvMsg:" + bottomMsgsStr + "|";
		}*/
		double bal = userBean.getAvailableCreditLimit()
		- userBean.getClaimableBal();
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMinimumFractionDigits(2);
		String balance = nf.format(bal).replaceAll(",", "");
		StringBuilder finalData = new StringBuilder("purchaseData:");
		String date = ticketBean.getPurchaseTime().toString();
		String gameLogoCode = gameBean.getGameLogoCode();
		//String gamePrizeCode = gameBean.getImageTypeMap().get("PRIZE").get(0);
		finalData.append(date.substring(0, date.indexOf(".")) + ","
				+ ticketBean.getTicketNo() + "," + ticketBean.getVirnNo()+ ","+id
				+ ","+gameLogoCode+"," + ticketBean.getImgList().replace(",", ":")
				+ ","+ticketBean.getPrizeCode()+","+ticketBean.getRefTransId() +"," +balance+ "|" + advtMsg);

		reprintData = finalData.toString();
		System.out.println("REPRINT DATA :" + reprintData);
		response.getOutputStream().write(reprintData.getBytes());

	}
	/*@SuppressWarnings("unchecked")
	public void claimPWTProcess() throws IOException, LMSException {
		ServletContext sc = ServletActionContext.getServletContext();
		String isIPE = (String) sc.getAttribute("IS_IPE");
		if ("NO".equalsIgnoreCase(isIPE)) {
			response.getOutputStream().write(
					"ErrorMsg:Instant Game Not Available|".getBytes());
			return;
		}
		Map<String, HttpSession> currentUserSessionMap = (Map<String, HttpSession>) sc
		.getAttribute("LOGGED_IN_USERS");
		HttpSession session = currentUserSessionMap.get(userName);
		if (session == null) {
			response
			.getOutputStream()
			.write(
					("ErrorMsg:" + EmbeddedErrors.SESSION_EXPIRED + "ErrorCode:01|")
					.getBytes());
			return;
		}
		String refMerchantId = (String) sc.getAttribute("REF_MERCHANT_ID_IPE");
		UserInfoBean userBean = (UserInfoBean) session.getAttribute("USER_INFO");
		TicketPurchaseBean ticketBean = new TicketPurchaseBean();
		ticketBean.setTicketNo(ticketNo);
		ticketBean.setRefMerId(refMerchantId);
		ticketBean.setPurChannel("LMS_Terminal");
		String highPrizeCriteria = (String) ServletActionContext.getServletContext().getAttribute("HIGH_PRIZE_CRITERIA");
		String highPrizeAmt = (String) ServletActionContext.getServletContext().getAttribute("HIGH_PRIZE_AMT");
		System.out.println("high prize amt = " + highPrizeAmt
				+ " and  highPrizeCriteria = " + highPrizeCriteria);
		RetPwtProcessHelper helper = new RetPwtProcessHelper();
		PwtBean winBean = new PwtBean();
		helper.verifyPwt(userBean, refMerchantId, highPrizeCriteria, highPrizeAmt, winBean);
	}*/
	public String  execute(){
		System.out.println("hi");
		return "dsfdsf";
	}
}
