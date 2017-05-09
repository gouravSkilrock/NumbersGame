package com.skilrock.lms.web.scratchService.pwtMgmt.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.ActiveGameBean;
import com.skilrock.lms.beans.GameTicketFormatBean;
import com.skilrock.lms.beans.TicketBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.scratchService.pwtMgmt.common.PwtTicketAgentHelper;

public class PwtTicketAgentAction extends ActionSupport implements
		ServletRequestAware {

	static Log logger = LogFactory.getLog(PwtTicketAgentAction.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int game_id;

	private String[] gameNbr_Name;
	private Map<String, List<TicketBean>> gameTktMap;
	private String[] gameTktNumber;
	private int[] inpCount; // Means input field count VIRN or Ticket Num
	private StringBuilder jsString;
	private String PWTAgent;
	private HttpServletRequest request;
	private int retOrgId;
	private List<TicketBean> savedTicketList;
	private List<TicketBean> ticketList;
	private List<TicketBean> ticketList2;
	private String[] ticketNumber;

	public String comeBack() {
		HttpSession session = getRequest().getSession();
		session.removeAttribute("VERIFIED_TICKET_LIST");
		session.removeAttribute("SELECTED_GAMENBR_NAME");
		session.removeAttribute("ACTIVE_GAME_LIST");
		session.removeAttribute("SAVED_TICKET_LIST");
		session.removeAttribute("TICKET_LIST");
		return SUCCESS;
	}

	public String displayPwtTicketEntryPage() {

		HttpSession session = getRequest().getSession();
		setTicketList(new ArrayList<TicketBean>());

		PwtTicketAgentHelper pwtTicketHelper = new PwtTicketAgentHelper();
		List<ActiveGameBean> activeGameList = pwtTicketHelper.getActiveGames();

		session.setAttribute("ACTIVE_GAME_LIST", activeGameList);

		List<GameTicketFormatBean> gameFormatList = null;

		if (activeGameList != null && activeGameList.size() > 0) {
			gameFormatList = pwtTicketHelper
					.getGameTicketFormatList(activeGameList);
			session.setAttribute("AGENT_GAME_FORMAT_LIST", gameFormatList);
		}

		return SUCCESS;

	}

	/*
	 * private void copyValuesToBean(String gameNbr_Name, String[]
	 * gameTktNumber) {
	 * 
	 * HttpSession session = getRequest().getSession(); ticketList = new
	 * ArrayList<TicketBean>(); TicketBean ticketBean = null; String ticketNbr =
	 * null;
	 * 
	 * GameTicketFormatBean gameFormatBean = null; List<GameTicketFormatBean>
	 * gameFormatList = (List) session.getAttribute("AGENT_GAME_FORMAT_LIST");
	 * 
	 * List<ActiveGameBean> activeGameList=(List)
	 * session.getAttribute("ACTIVE_GAME_LIST"); PwtTicketAgentHelper
	 * pwtTicketHelper = new PwtTicketAgentHelper(); int
	 * gameId=pwtTicketHelper.getGameId(activeGameList,gameNbr_Name);
	 * 
	 * for(int i=0; i<gameFormatList.size(); i++){ gameFormatBean =
	 * gameFormatList.get(i);
	 * 
	 * if (gameId == gameFormatBean.getGameId()){ break; } }
	 * 
	 * int gameNbrDigits = gameFormatBean.getGameNbrDigits(); int packNbrDigits =
	 * gameFormatBean.getPackDigits(); int bookNbrDigits =
	 * gameFormatBean.getBookDigits();
	 * 
	 * 
	 * for(int i=0; i<gameTktNumber.length; i++){ ticketNbr = gameTktNumber[i];
	 * if(ticketNbr.indexOf("-")== -1 && ticketNbr.length() >
	 * (gameNbrDigits+packNbrDigits+bookNbrDigits)){ ticketNbr =
	 * ticketNbr.substring(0,gameNbrDigits) + "-" +
	 * ticketNbr.substring(gameNbrDigits); ticketNbr =
	 * ticketNbr.substring(0,gameNbrDigits+packNbrDigits+bookNbrDigits+1) + "-" +
	 * ticketNbr.substring(gameNbrDigits+packNbrDigits+bookNbrDigits+1); }
	 * 
	 * ticketBean=new TicketBean(); ticketBean.setTicketNumber(ticketNbr);
	 * ticketList.add(ticketBean); //System.out.println("ticket No:"+i+" :
	 * "+gameTktNumber[i]+" and Size is :"+size); }
	 * session.setAttribute("TICKET_LIST",ticketList); }
	 */

	/*
	 * public String verifyTickets() throws LMSException{ HttpSession session =
	 * getRequest().getSession(); List<ActiveGameBean> activeGameList=(List)
	 * session.getAttribute("ACTIVE_GAME_LIST"); UserInfoBean
	 * userInfo=(UserInfoBean)session.getAttribute("USER_INFO"); savedTicketList =
	 * new ArrayList<TicketBean>(); PwtTicketAgentHelper pwtTicketHelper = new
	 * PwtTicketAgentHelper();
	 * 
	 * List<String> allTktNum = new ArrayList<String>(); gameTktMap = new
	 * HashMap<String,List<TicketBean>>(); int startTktCount =0; int
	 * endTktCount =0;
	 * 
	 * for(int i=0; i<getTicketNumber().length; i++){
	 * if(getTicketNumber()[i]!=null &&
	 * !getTicketNumber()[i].trim().equals("")){
	 * allTktNum.add(getTicketNumber()[i]); } } for (int i=0;i<gameNbr_Name.length;i++){
	 * //System.out.println("Game Name--"+gameNbr_Name[i]);
	 * if(!gameNbr_Name[i].equals("-1")){ endTktCount=
	 * startTktCount+inpCount[i]; int inc =0; gameTktNumber = new
	 * String[endTktCount-startTktCount]; for(int j=startTktCount;j<endTktCount;j++){
	 * gameTktNumber[inc]=allTktNum.get(j);
	 * //System.out.println(gameNbr_Name[i]+"*-*-*"+gameTktNumber[inc]); inc++;
	 * startTktCount++; } System.out.println("Game Name****"+gameNbr_Name[i]+"
	 * Virn "+gameTktNumber.length); if(gameTktNumber.length>0){
	 * copyValuesToBean(gameNbr_Name[i],gameTktNumber); List<TicketBean>
	 * ticketList = (List) session.getAttribute("TICKET_LIST"); int game_id=0;
	 * game_id=pwtTicketHelper.getGameId(activeGameList,gameNbr_Name[i]);
	 * System.out.println("game id at the time of verification: "+game_id);
	 * 
	 * //get game nbr from game nbr name String[] gameNameNbrArr =
	 * gameNbr_Name[i].split("-"); int
	 * gameNbr=Integer.parseInt(gameNameNbrArr[0]);
	 * 
	 * ticketList2=pwtTicketHelper.getVerifiedTickets(ticketList,game_id,gameNbr);
	 * savedTicketList.addAll(pwtTicketHelper.saveTicketsData(game_id,ticketList2,userInfo.getUserOrgId(),userInfo.getUserId(),gameNbr));
	 * System.out.println("Afterrrrrrrrr callingggg save dataa");
	 * if(gameTktMap.containsKey(gameNbr_Name[i])){ List<TicketBean> oldPwtList =
	 * gameTktMap.get(gameNbr_Name[i]); oldPwtList.addAll(ticketList2);
	 * gameTktMap.put(gameNbr_Name[i], oldPwtList); }else{
	 * gameTktMap.put(gameNbr_Name[i], ticketList2); } } } }
	 * 
	 * jsString = new StringBuilder(); Iterator itTkt =
	 * gameTktMap.entrySet().iterator(); Map msgCode = new HashMap(); while
	 * (itTkt.hasNext()) { Map.Entry pairsTkt = (Map.Entry)itTkt.next();
	 * 
	 * jsString.append(pairsTkt.getKey()+":"); List<TicketBean> tktList = (List<TicketBean>)pairsTkt.getValue();
	 * Iterator itTktList = tktList.iterator(); while (itTktList.hasNext()) {
	 * TicketBean bean=(TicketBean)itTktList.next();
	 * jsString.append(bean.getTicketNumber()+"*M*"+bean.getMessageCode()+":");
	 * msgCode.put(bean.getMessageCode(),
	 * bean.getValidity()+":"+bean.getStatus()); } jsString.append("Nx*"); }
	 * Iterator itMsgCode = msgCode.entrySet().iterator(); while
	 * (itMsgCode.hasNext()) { Map.Entry pairsTkt = (Map.Entry)itMsgCode.next();
	 * jsString.append(pairsTkt.getKey()+"-"+pairsTkt.getValue()+"Msg"); }
	 * 
	 * session.setAttribute("VERIFIED_TICKET_JSSTRING",jsString);
	 * 
	 * System.out.println(jsString);
	 * 
	 * session.setAttribute("VERIFIED_TICKET_MAP",gameTktMap);
	 * 
	 * session.setAttribute("SAVED_TICKET_LIST", savedTicketList);
	 * System.out.println("Saved List is "+savedTicketList);
	 * 
	 * return SUCCESS; }
	 */

	/*
	 * public String saveTicketsData() throws LMSException{
	 * 
	 * HttpSession session = getRequest().getSession(); List<TicketBean>
	 * verifiedTicketList = (List) session.getAttribute("VERIFIED_TICKET_LIST");
	 * 
	 * UserInfoBean userInfo=(UserInfoBean)session.getAttribute("USER_INFO");
	 * 
	 * String gameNbrName=(String)session.getAttribute("SELECTED_GAMENBR_NAME");
	 * 
	 * System.out.println("Ticket List Size::" + verifiedTicketList.size());
	 * System.out.println("........................."+gameNbrName);
	 * 
	 * PwtTicketAgentHelper pwtTicketHelper = new PwtTicketAgentHelper(); int
	 * game_id = pwtTicketHelper.getGameIdFromDataBase(gameNbrName);
	 * System.out.println("Get the Game ID is : "+game_id);
	 * 
	 * //get the game nbr from game nbr name String[]
	 * gameNameNbrArr=gameNbrName.split("-"); int
	 * gameNbr=Integer.parseInt(gameNameNbrArr[0]);
	 * 
	 * 
	 * 
	 * System.out.println("Afterrrrrrrrr callingggg save dataa");
	 * 
	 * session.setAttribute("SAVED_TICKET_LIST", savedTicketList);
	 * 
	 * System.out.println("Saved List is "+savedTicketList);
	 * 
	 * return SUCCESS; }
	 */

	public int getGame_id() {
		return game_id;
	}

	public String[] getGameNbr_Name() {
		return gameNbr_Name;
	}

	public Map<String, List<TicketBean>> getGameTktMap() {
		return gameTktMap;
	}

	public String[] getGameTktNumber() {
		return gameTktNumber;
	}

	public int[] getInpCount() {
		return inpCount;
	}

	public StringBuilder getJsString() {
		return jsString;
	}

	public String getPWTAgent() {
		return PWTAgent;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public int getRetOrgName() {
		return retOrgId;
	}

	public List<TicketBean> getSavedTicketList() {
		return savedTicketList;
	}

	public List<TicketBean> getTicketList() {
		return ticketList;
	}

	public List<TicketBean> getTicketList2() {
		return ticketList2;
	}

	public String[] getTicketNumber() {
		return ticketNumber;
	}

	public void setGame_id(int game_id) {
		this.game_id = game_id;
	}

	public void setGameNbr_Name(String[] gameNbr_Name) {
		this.gameNbr_Name = gameNbr_Name;
	}

	public void setGameTktMap(Map<String, List<TicketBean>> gameTktMap) {
		this.gameTktMap = gameTktMap;
	}

	public void setGameTktNumber(String[] gameTktNumber) {
		this.gameTktNumber = gameTktNumber;
	}

	public void setInpCount(int[] inpCount) {
		this.inpCount = inpCount;
	}

	public void setJsString(StringBuilder jsString) {
		this.jsString = jsString;
	}

	public void setPWTAgent(String agent) {
		PWTAgent = agent;
	}

	public void setRetOrgName(int retOrgId) {
		this.retOrgId = retOrgId;
	}

	public void setSavedTicketList(List<TicketBean> savedTicketList) {
		this.savedTicketList = savedTicketList;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setTicketList(List<TicketBean> ticketList) {
		this.ticketList = ticketList;
	}

	public void setTicketList2(List<TicketBean> ticketList2) {
		this.ticketList2 = ticketList2;
	}

	public void setTicketNumber(String[] ticketNumber) {
		this.ticketNumber = ticketNumber;
	}

	public String verifyTickets() throws LMSException {

		HttpSession session = getRequest().getSession();
		List<ActiveGameBean> activeGameList = (List<ActiveGameBean>) session
				.getAttribute("ACTIVE_GAME_LIST");
		UserInfoBean userInfo = (UserInfoBean) session
				.getAttribute("USER_INFO");
		userInfo.setChannel("RETAIL");
		userInfo.setInterfaceType((String) request
				.getAttribute("interfaceType"));

		savedTicketList = new ArrayList<TicketBean>();
		gameTktMap = new HashMap<String, List<TicketBean>>();
		jsString = new StringBuilder("");

		PwtTicketAgentHelper pwtTicketHelper = new PwtTicketAgentHelper();
		Map map = pwtTicketHelper.agtTicketVerifyNSave(ticketNumber,
				gameNbr_Name, inpCount, userInfo, retOrgId);

		jsString = (StringBuilder) map.get("VERIFIED_TICKET_JSSTRING");
		gameTktMap = (Map<String, List<TicketBean>>) map
				.get("VERIFIED_TICKET_MAP");
		savedTicketList = (List<TicketBean>) map.get("SAVED_TICKET_LIST");
		ticketList = (List<TicketBean>) map.get("totalTktList");
		System.out.println("jsString = " + jsString + "\n gameTktMap = "
				+ gameTktMap + "\nsavedTicketList = " + savedTicketList);

		session.setAttribute("VERIFIED_TICKET_JSSTRING", jsString);
		session.setAttribute("VERIFIED_TICKET_MAP", gameTktMap);
		session.setAttribute("SAVED_TICKET_LIST", savedTicketList);
		session.setAttribute("TICKET_LIST", ticketList);

		System.out.println(jsString);
		System.out.println("Saved List is " + savedTicketList);

		return SUCCESS;
	}

}
