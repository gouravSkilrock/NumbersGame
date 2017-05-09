package com.skilrock.lms.web.scratchService.pwtMgmt.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.ActiveGameBean;
import com.skilrock.lms.beans.GameTicketFormatBean;
import com.skilrock.lms.beans.OrgBean;
import com.skilrock.lms.beans.PwtBean;
import com.skilrock.lms.beans.TicketBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.scratchService.pwtMgmt.common.PwtBOHelper;
import com.skilrock.lms.coreEngine.scratchService.pwtMgmt.common.PwtTicketHelper;
import com.skilrock.lms.coreEngine.scratchService.pwtMgmt.common.PwtVerfiyHelper;

public class PwtVerifyAction extends ActionSupport implements
		ServletRequestAware {
	static Log logger = LogFactory.getLog(PwtVerifyAction.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String agtOrgName;
	private double amount;
	private String closeReceipt;
	private List<TicketBean> duplicateticketList;
	int end = 0;
	private String fromDate;
	private int game_id;
	private String[] gameNbr_Name = null;
	private String[] gamePrzAmtLength = null;
	private boolean isSuccess;
	private String[] noOfTick = null;

	private double[] prizeAmtArr = null;
	private String PWTFlag;
	private List<PwtBean> pwtList;
	private String receiptNum;
	private HttpServletRequest request;
	private List<TicketBean> savedTicketList;
	private String searchResultsAvailable;
	int start = 0;
	private String status;

	private String ticketFile;
	private List<TicketBean> ticketList;
	private List<TicketBean> ticketList2;
	List<String> ticketListString = new ArrayList<String>();

	private String[] ticketNumber;

	List tmpRcptSearchRes = new ArrayList();
	private Map tmpReceiptMap;
	private String toDate;

	private String[] virnCode;

	// private String ticketDetails;

	public String comeBack() {
		HttpSession session = getRequest().getSession();
		session.removeAttribute("VERIFIED_TICKET_LIST");
		session.removeAttribute("SELECTED_GAMENBR_NAME");
		session.removeAttribute("ACTIVE_GAME_LIST");
		session.removeAttribute("SAVED_TICKET_LIST");
		session.removeAttribute("TICKET_LIST");
		return SUCCESS;
	}

	@Override
	public String execute() {

		HttpSession session = request.getSession();
		session.setAttribute("PWT_CURRENT_DATE", new java.sql.Date(
				new java.util.Date().getTime()).toString());
		return SUCCESS;
	}

	public String generateReceipt() throws LMSException {
		HttpSession session = getRequest().getSession();
		PwtVerfiyHelper verifyHelp = new PwtVerfiyHelper();
		OrgBean agentOrgBean = null;
		List verifiedData = new ArrayList();

		verifiedData = verifyHelp.generateReceipt(receiptNum);
		Map gameVirnCode = (Map) verifiedData.get(1);

		agentOrgBean = verifyHelp.agentOrgName(receiptNum);

		setAgtOrgName(agentOrgBean.getOrgName());

		List<PwtBean> pwtCompleteReceiptList = null;
		String highPrizeCriteria = (String) ServletActionContext
				.getServletContext().getAttribute("HIGH_PRIZE_CRITERIA");
		String highPrizeAmt = (String) ServletActionContext.getServletContext()
				.getAttribute("HIGH_PRIZE_AMT");
		double HighPrizeAmount = 0.0;
		if (!highPrizeAmt.equals(null)) {
			HighPrizeAmount = Double.parseDouble(highPrizeAmt);
		}

		PwtBOHelper pwtBOHelper = new PwtBOHelper();
		UserInfoBean userInfoBean = (UserInfoBean) session
				.getAttribute("USER_INFO");

		List activeGameList = pwtBOHelper.getActiveGames();

		String boOrgName = userInfoBean.getOrgName();
		int userOrgID = userInfoBean.getUserOrgId();
		int userId = userInfoBean.getUserId();
		String channel = "RETAIL";
		String interfaceType = (String) request.getAttribute("interfaceType");

		String rootPath = (String) session.getAttribute("ROOT_PATH");
		if (!gameVirnCode.isEmpty()) {
			pwtCompleteReceiptList = verifyHelp.saveBOPwtTicketsDataTmpReceipt(
					boOrgName, userId, userOrgID, agentOrgBean, activeGameList,
					rootPath, HighPrizeAmount, highPrizeCriteria, verifiedData,
					receiptNum, closeReceipt, channel, interfaceType);
		}
		// System.out.println("---pwtCompleteReceiptList--generateReceipt--"+pwtCompleteReceiptList);
		System.out.println("amount is at action for all the pwts  " + amount);
		amount = pwtBOHelper.getPwtAmount(pwtCompleteReceiptList);
		if (amount > 0) {
			setSuccess(true);
		} else {
			setSuccess(false);
			// double amount = pwtBOHelper.getPwtAmount(pwtCompleteReceiptList);
		}

		/*
		 * if(amount > 0){ setSuccess(true); setAmount(amount);
		 * System.out.println("----Success and amount-------is" +amount);
		 * if(closeReceipt!=null&&closeReceipt.equals("Yes")){
		 * verifyHelp.closeReceipt(receiptNum,userId,userOrgID,verifiedData,activeGameList); }
		 * System.out.println("----Close Receipt-------"); } else {
		 * System.out.println("---close Receipt---" +closeReceipt);
		 * if(closeReceipt!=null&&closeReceipt.equals("Yes")){
		 * verifyHelp.closeReceipt(receiptNum,userId,userOrgID,verifiedData,activeGameList); }
		 * System.out.println("----Zero amount-------"); setSuccess(false); }
		 */
		session.setAttribute("GEN_RCT_SUC", isSuccess());
		session.setAttribute("AMOUNT", getAmount());
		return SUCCESS;
	}

	public String getAgtOrgName() {
		return agtOrgName;
	}

	/*
	 * public void saveTmpTicketsData(HttpSession session) {
	 * 
	 * System.out.println("In If-------saveTmpTicketsData--"+session); List<TicketBean>
	 * verifiedTicketList = (List<TicketBean>)
	 * session.getAttribute("VERIFIED_TICKET_LIST"); //List<ActiveGameBean>
	 * activeGameBeanList=(List) session.getAttribute("ACTIVE_GAME_LIST");
	 * System.out.println("In If-------45--"); String
	 * gameNbrName=(String)session.getAttribute("SELECTED_GAMENBR_NAME");
	 * 
	 * System.out.println("Ticket List Size::" + verifiedTicketList.size());
	 * //System.out.println(activeGameBeanList);
	 * System.out.println("........................."+gameNbrName);
	 * 
	 * PwtTicketHelper pwtTicketHelper = new PwtTicketHelper(); int game_id =
	 * pwtTicketHelper.getGameIdFromDataBase(gameNbrName);
	 * 
	 * System.out.println("Get the Game ID is : "+game_id); PwtVerfiyHelper
	 * pwtVeriHelp = new PwtVerfiyHelper();
	 * 
	 * setSavedTicketList(pwtVeriHelp.saveTmpTicketsData(game_id,verifiedTicketList,receiptNum,((UserInfoBean)session.getAttribute("USER_INFO")).getUserId()));
	 * 
	 * verifiedTicketList.addAll(duplicateticketList);
	 * session.setAttribute("VERIFIED_TICKET_LIST",verifiedTicketList);
	 * System.out.println("Afterrrrrrrrr callingggg save dataa");
	 * 
	 * session.setAttribute("SAVED_TICKET_LIST", savedTicketList);
	 * 
	 * System.out.println("Saved List is "+savedTicketList); }
	 */

	public double getAmount() {
		return amount;
	}

	public String getCloseReceipt() {
		return closeReceipt;
	}

	public List<TicketBean> getDuplicateticketList() {
		return duplicateticketList;
	}

	public int getEnd() {
		return end;
	}

	public String getFromDate() {
		return fromDate;
	}

	public int getGame_id() {
		return game_id;
	}

	public String[] getGameNbr_Name() {
		return gameNbr_Name;
	}

	public String[] getGamePrzAmtLength() {
		return gamePrzAmtLength;
	}

	public String[] getNoOfTick() {
		return noOfTick;
	}

	public double[] getPrizeAmtArr() {
		return prizeAmtArr;
	}

	public String getPWTFlag() {
		return PWTFlag;
	}

	public List<PwtBean> getPwtList() {
		return pwtList;
	}

	public String getReceiptNum() {
		return receiptNum;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public List<TicketBean> getSavedTicketList() {
		return savedTicketList;
	}

	public String getSearchResultsAvailable() {
		return searchResultsAvailable;
	}

	public int getStart() {
		return start;
	}

	public String getStatus() {
		return status;
	}

	public String getTicketFile() {
		return ticketFile;
	}

	public List<TicketBean> getTicketList() {
		return ticketList;
	}

	public List<TicketBean> getTicketList2() {
		return ticketList2;
	}

	public List<String> getTicketListString() {
		return ticketListString;
	}

	public String[] getTicketNumber() {
		return ticketNumber;
	}

	public List getTmpRcptSearchRes() {
		return tmpRcptSearchRes;
	}

	public Map getTmpReceiptMap() {
		return tmpReceiptMap;
	}

	public String getToDate() {
		return toDate;
	}

	public String[] getVirnCode() {
		return virnCode;
	}

	public boolean isSuccess() {
		return isSuccess;
	}

	public String pwtRcptGenSearchResult() {
		HttpSession session = getRequest().getSession();

		session.setAttribute("SEARCH_RESULTS1", null);
		session.setAttribute("SEARCH_RESULTS", null);

		PwtVerfiyHelper pwtVeriHelp = new PwtVerfiyHelper();

		tmpRcptSearchRes = pwtVeriHelp.pwtRcptGenSearchRes(agtOrgName,
				receiptNum, fromDate, toDate, status);
		setTmpRcptSearchRes(tmpRcptSearchRes);

		if (tmpRcptSearchRes != null && tmpRcptSearchRes.size() > 0) {

			session.setAttribute("SEARCH_RESULTS1", tmpRcptSearchRes);
			session.setAttribute("startValueOrderSearch", new Integer(0));
			setEnd(5);
			setStart(0);
			setSearchResultsAvailable("Yes");
		} else {
			setSearchResultsAvailable("No");
		}

		searchAjax();
		return SUCCESS;
	}

	public String saveTmpVirnData() throws Exception {

		System.out.println("--------saveTmpVirnData----------");
		HttpSession session = getRequest().getSession();

		List<PwtBean> pwtList = new ArrayList<PwtBean>();
		PwtBean pwtBean = null;
		List<String> virnStringList = new ArrayList<String>();
		List<PwtBean> duplicateVirnList = new ArrayList<PwtBean>();
		/*
		 * for(int i=0; i<getVirnCode().length; i++){ if(getVirnCode()[i]!=null &&
		 * !getVirnCode()[i].trim().equals("")){
		 * if(virnStringList.contains(getVirnCode()[i].trim())) { pwtBean= new
		 * PwtBean(); pwtBean.setVirnCode(getVirnCode()[i].trim());
		 * pwtBean.setValid(false); pwtBean.setVerificationStatus("InValid
		 * VIRN"); pwtBean.setMessage("Duplicate Virn Entry in File");
		 * pwtBean.setMessageCode("112013"); duplicateVirnList.add(pwtBean);
		 * 
		 * }else virnStringList.add(getVirnCode()[i].trim()); }
		 * //pwtBean.setPwtAmount(getPwtAmount()[i]); }
		 * 
		 * //code added here by yogesh to read virn from file also try{
		 * InputStreamReader fileStreamReader = new InputStreamReader(new
		 * FileInputStream(ticketFile)); BufferedReader br = new
		 * BufferedReader(fileStreamReader); String strVirnLine=null; int
		 * fileVirnLimit=0; while((strVirnLine =br.readLine())!=null) {
		 * if("".equals(strVirnLine.trim())) continue; if(fileVirnLimit>5000) {
		 * addActionError("Data In File Exceeds limit "); return ERROR; }
		 * if(virnStringList.contains(strVirnLine.trim())) { pwtBean= new
		 * PwtBean(); pwtBean.setVirnCode(strVirnLine.trim());
		 * pwtBean.setValid(false); pwtBean.setVerificationStatus("InValid
		 * VIRN"); pwtBean.setMessage("Duplicate Virn Entry in File");
		 * pwtBean.setMessageCode("112013"); duplicateVirnList.add(pwtBean);
		 * }else virnStringList.add(strVirnLine.trim()); fileVirnLimit++; }
		 * }catch(FileNotFoundException fe) { //fe.printStackTrace(); //throw
		 * new LMSException("file not found exception",fe); }
		 */
		PwtBOHelper pwtBOHelper = new PwtBOHelper();
		Map map = pwtBOHelper.getVirnList(virnCode, ticketFile);
		if (map != null && map.containsKey("error")) {
			addActionError("Data In File Exceeds limit ");
			return ERROR;
		}
		virnStringList = (ArrayList<String>) map.get("virnStringList");
		duplicateVirnList = (ArrayList<PwtBean>) map.get("duplicateVirnList");
		// now make PWT bean from whole virnStringList
		for (int i = 0; i < virnStringList.size(); i++) {
			pwtBean = new PwtBean();
			pwtBean.setVerificationStatus("InValid");
			pwtBean.setVirnCode(virnStringList.get(i));
			pwtList.add(pwtBean);

		}

		setVirnCode(virnStringList.toArray(new String[0]));

		System.out.println("Pwt List Size::" + pwtList.size());
		System.out.println("VIRN Code Size::" + getVirnCode().length);

		String highPrizeCriteria = (String) ServletActionContext
				.getServletContext().getAttribute("HIGH_PRIZE_CRITERIA");
		String highPrizeAmt = (String) ServletActionContext.getServletContext()
				.getAttribute("HIGH_PRIZE_AMT");
		System.out.println("high prize amt is " + highPrizeAmt);
		double HighPrizeAmount = 0.0;
		if (!highPrizeAmt.equals(null)) {
			HighPrizeAmount = Double.parseDouble(highPrizeAmt);
		}
		List<ActiveGameBean> activeGameList = (List) session
				.getAttribute("ACTIVE_GAME_LIST");
		List<OrgBean> agtOrgList = (List) session.getAttribute("AGT_ORG_LIST");
		// int gameId =
		// pwtAgentHelper.getGameId(retOrgList,activeGameList,getGameNbr_Name());

		UserInfoBean userInfoBean = (UserInfoBean) session
				.getAttribute("USER_INFO");

		String boOrgName = userInfoBean.getOrgName();
		int userOrgID = userInfoBean.getUserOrgId();

		String rootPath = (String) session.getAttribute("ROOT_PATH");
		// System.out.println("About to call save
		// dataa"+getGameNbr_Name()[0]+"---"+Integer.parseInt(receiptNum));

		pwtBOHelper.saveTmpPwtVirnData(getGameNbr_Name()[0], receiptNum,
				userInfoBean.getUserId(), boOrgName, userOrgID,
				getAgtOrgName(), agtOrgList, activeGameList, getVirnCode(),
				pwtList, rootPath, HighPrizeAmount, highPrizeCriteria);

		System.out.println("Afterrrrrrrrr callingggg save dataa");
		pwtList.addAll(duplicateVirnList);
		setPwtList(pwtList);
		session.setAttribute("saveTmpVirnData", this);
		return SUCCESS;

	}

	public String searchAjax() {
		int endValue = getEnd();
		int startValue = getStart();
		// PrintWriter out = getResponse().getWriter();
		HttpSession session = getRequest().getSession();
		List ajaxList = (List) session.getAttribute("SEARCH_RESULTS1");
		List ajaxSearchList = new ArrayList();
		// System.out.println("end "+getEnd());
		if (ajaxList != null) {
			if (getEnd() < ajaxList.size()) {
				endValue = getEnd();
			} else {
				endValue = ajaxList.size();
			}
			/*
			 * if (getEnd() != null) { end = getEnd(); } else { end = "first"; }
			 * System.out.println("List Size " + ajaxList.size()); startValue =
			 * (Integer) session .getAttribute("startValueOrderSearch"); if
			 * (end.equals("first")) { System.out.println("i m in first");
			 * startValue = 0; endValue = startValue + 5; if (endValue >
			 * ajaxList.size()) { endValue = ajaxList.size(); } } else if
			 * (end.equals("Previous")) { System.out.println("i m in Previous");
			 * startValue = startValue - 5; if (startValue < 5) { startValue =
			 * 0; } endValue = startValue + 5; if (endValue > ajaxList.size()) {
			 * endValue = ajaxList.size(); } } else if (end.equals("Next")) {
			 * System.out.println("i m in Next"); startValue = startValue + 5;
			 * endValue = startValue + 5; if (endValue > ajaxList.size()) {
			 * endValue = ajaxList.size(); } if (startValue > ajaxList.size()) {
			 * startValue = ajaxList.size() - (ajaxList.size() % 5); } } else if
			 * (end.equals("last")) { endValue = ajaxList.size(); startValue =
			 * endValue - (endValue % 5); } if (startValue == endValue) {
			 * startValue = endValue - 5; }
			 */
			System.out.println("End value" + endValue);
			System.out.println("Start Value" + startValue);
			for (int i = startValue; i < endValue; i++) {
				ajaxSearchList.add(ajaxList.get(i));
			}
			session.setAttribute("SEARCH_RESULTS", ajaxSearchList);
			session.setAttribute("startValueOrderSearch", startValue);
			setSearchResultsAvailable("Yes");
		}
		return SUCCESS;
	}

	public void setAgtOrgName(String agtOrgName) {
		this.agtOrgName = agtOrgName;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public void setCloseReceipt(String closeReceipt) {
		this.closeReceipt = closeReceipt;
	}

	public void setDuplicateticketList(List<TicketBean> duplicateticketList) {
		this.duplicateticketList = duplicateticketList;
	}

	public void setEnd(int end) {
		this.end = end;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public void setGame_id(int game_id) {
		this.game_id = game_id;
	}

	public void setGameNbr_Name(String[] gameNbr_Name) {
		this.gameNbr_Name = gameNbr_Name;
	}

	public void setGamePrzAmtLength(String[] gamePrzAmtLength) {
		this.gamePrzAmtLength = gamePrzAmtLength;
	}

	public void setNoOfTick(String[] noOfTick) {
		this.noOfTick = noOfTick;
	}

	public void setPrizeAmtArr(double[] prizeAmtArr) {
		this.prizeAmtArr = prizeAmtArr;
	}

	public void setPWTFlag(String flag) {
		PWTFlag = flag;
	}

	public void setPwtList(List<PwtBean> pwtList) {
		this.pwtList = pwtList;
	}

	public void setReceiptNum(String receiptNum) {
		this.receiptNum = receiptNum;
	}

	public void setSavedTicketList(List<TicketBean> savedTicketList) {
		this.savedTicketList = savedTicketList;
	}

	public void setSearchResultsAvailable(String searchResultsAvailable) {
		this.searchResultsAvailable = searchResultsAvailable;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public void setTicketFile(String ticketFile) {
		this.ticketFile = ticketFile;
	}

	public void setTicketList(List<TicketBean> ticketList) {
		this.ticketList = ticketList;
	}

	public void setTicketList2(List<TicketBean> ticketList2) {
		this.ticketList2 = ticketList2;
	}

	public void setTicketListString(List<String> ticketListString) {
		this.ticketListString = ticketListString;
	}

	public void setTicketNumber(String[] ticketNumber) {
		this.ticketNumber = ticketNumber;
	}

	public void setTmpRcptSearchRes(List tmpRcptSearchRes) {
		this.tmpRcptSearchRes = tmpRcptSearchRes;
	}

	public void setTmpReceiptMap(Map tmpReceiptMap) {
		this.tmpReceiptMap = tmpReceiptMap;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public void setVirnCode(String[] virnCode) {
		this.virnCode = virnCode;
	}

	public String tmpPwtRcptSearchResult() {
		HttpSession session = getRequest().getSession();

		session.setAttribute("SEARCH_RESULTS1", null);
		session.setAttribute("SEARCH_RESULTS", null);

		PwtVerfiyHelper pwtVeriHelp = new PwtVerfiyHelper();

		tmpRcptSearchRes = pwtVeriHelp.tmpRcptSearchRes(agtOrgName, receiptNum,
				fromDate, toDate);
		setTmpRcptSearchRes(tmpRcptSearchRes);

		if (tmpRcptSearchRes != null && tmpRcptSearchRes.size() > 0) {

			session.setAttribute("SEARCH_RESULTS1", tmpRcptSearchRes);
			session.setAttribute("startValueOrderSearch", new Integer(0));
			setSearchResultsAvailable("Yes");
			setEnd(5);
			setStart(0);
		} else {
			setSearchResultsAvailable("No");
		}

		searchAjax();
		return SUCCESS;
	}

	public String tmpPwtReceipt() throws Exception {
		HttpSession session = getRequest().getSession();
		UserInfoBean userInfo = (UserInfoBean) session
				.getAttribute("USER_INFO");

		PwtBOHelper pwtBOHelper = new PwtBOHelper();
		List activeGameList = pwtBOHelper.getActiveGames();
		List agtOrgList = pwtBOHelper.getAgents(userInfo);

		session.setAttribute("ACTIVE_GAME_LIST", activeGameList);
		session.setAttribute("AGT_ORG_LIST", agtOrgList);

		return SUCCESS;
	}

	public String tmpPwtReceiptSubmit() {
		HttpSession session = getRequest().getSession();
		UserInfoBean userBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		String rootPath = (String) session.getAttribute("ROOT_PATH");
		PwtVerfiyHelper pwtVeriHelp = new PwtVerfiyHelper();
		Map gameNumMap = new HashMap();
		int counter = 0;
		int newCounter = 0;
		for (int i = 0; i < getGameNbr_Name().length; i++) {
			Map prizeAmtMap = new HashMap();
			// System.out.println(getGamePrzAmtLength()[i]);
			newCounter = Integer.parseInt(getGamePrzAmtLength()[i]) + counter;
			for (int j = counter; j < newCounter; j++) {
				// System.out.println(noOfTick[counter]+"-not-"+counter+"-c--nc--"+newCounter+"--"+prizeAmtArr[counter]);
				if (!noOfTick[counter].equals(" ")) {
					prizeAmtMap.put(prizeAmtArr[counter], noOfTick[counter]);
				}
				counter++;
			}
			gameNumMap.put(gameNbr_Name[i].substring(0, gameNbr_Name[i]
					.indexOf("-")), prizeAmtMap);

		}
		System.out
				.println(agtOrgName + "Temp Pwt Receipt gameMap" + gameNumMap);
		setReceiptNum(pwtVeriHelp.getTmpReceiptId(gameNumMap, agtOrgName,
				userBean.getOrgName(), userBean.getUserOrgId(), rootPath));
		setTmpReceiptMap(gameNumMap);
		session.setAttribute("tmpPwtReceiptSubmit", this);
		return SUCCESS;
	}

	public String tmpRcptDetail() {
		HttpSession session = getRequest().getSession();
		PwtVerfiyHelper verifyHelp = new PwtVerfiyHelper();
		session.setAttribute("TMP_RCPT_DETAIL", verifyHelp
				.tmpRcptDetail(receiptNum));
		System.out.println("-tmpRcptDetail-----");
		return SUCCESS;

	}

	public String tmpTicketVerify() throws Exception {
		HttpSession session = getRequest().getSession();
		setTicketList(new ArrayList<TicketBean>());

		PwtTicketHelper pwtTicketHelper = new PwtTicketHelper();
		List<ActiveGameBean> activeGameList = pwtTicketHelper.getActiveGames();

		List<GameTicketFormatBean> gameFormatList = null;

		if (activeGameList != null && activeGameList.size() > 0) {
			gameFormatList = pwtTicketHelper
					.getGameTicketFormatList(activeGameList);

			session.setAttribute("GAME_FORMAT_LIST", gameFormatList);
		}

		session.setAttribute("ACTIVE_GAME_LIST", activeGameList);

		return SUCCESS;
	}

	public String tmpVirnVerify() throws Exception {

		HttpSession session = getRequest().getSession();
		UserInfoBean userInfo = (UserInfoBean) session
				.getAttribute("USER_INFO");

		setPwtList(new ArrayList<PwtBean>());
		PwtBOHelper pwtBOHelper = new PwtBOHelper();
		List activeGameList = pwtBOHelper.getActiveGames();
		List agtOrgList = pwtBOHelper.getAgents(userInfo);
		session.setAttribute("ACTIVE_GAME_LIST", activeGameList);
		session.setAttribute("AGT_ORG_LIST", agtOrgList);
		List<PwtBean> pwtList = getPwtList();
		pwtList.add(new PwtBean());
		session.setAttribute("PWT_LIST", pwtList);
		System.out.println("Exittttted---------------");
		return SUCCESS;

	}

	public String verifyTmpTickets() throws LMSException {

		HttpSession session = getRequest().getSession();
		PwtTicketHelper pwtTicketHelper = new PwtTicketHelper();

		Map map = pwtTicketHelper.getTicketList(ticketNumber, ticketFile);
		if (map != null && map.containsKey("error")) {
			addActionError("Data In File Exceeds limit ");
			return ERROR;
		}

		ticketListString = (List<String>) map.get("ticketListString");
		duplicateticketList = (List<TicketBean>) map.get("duplicateticketList");

		// get game nbr from game nbr name
		String[] gameNameNbeArr = getGameNbr_Name()[0].split("-");
		int gameNbr = Integer.parseInt(gameNameNbeArr[0]);
		List<TicketBean> VerifyTicketList = pwtTicketHelper
				.getGameWiseVerifiedTickets(ticketListString, gameNbr);

		session.setAttribute("TMP_RCPT_NUM", receiptNum);
		session.setAttribute("TMP_AGT_NAME", agtOrgName);
		VerifyTicketList.addAll(duplicateticketList);
		if (VerifyTicketList != null) {
			setTicketList2(VerifyTicketList);
			session.setAttribute("VERIFIED_TICKET_LIST", ticketList2);
			session.setAttribute("TICKET_LIST", ticketList);
			// setPWTFlag(pwtTicketHelper.getPwtFlag());
			PwtVerfiyHelper pwtVeriHelp = new PwtVerfiyHelper();
			UserInfoBean userInfo = (UserInfoBean) session
					.getAttribute("USER_INFO");
			userInfo.setChannel("RETAIL");
			userInfo.setInterfaceType((String) request
					.getAttribute("interfaceType"));
			savedTicketList = pwtVeriHelp.saveTmpTicketsData(VerifyTicketList,
					receiptNum, userInfo.getUserId(), userInfo.getUserOrgId(),
					agtOrgName, userInfo.getChannel(), userInfo
							.getInterfaceType());
			session.setAttribute("SAVED_TICKET_LIST", savedTicketList);
			return SUCCESS;

		} else {
			System.out.println("FFFFFFFFFF");
			addActionError("Ticket Number is not in correct format");
			session.setAttribute("TICKET_LIST", ticketList);
			return ERROR;

		}

	}
}
