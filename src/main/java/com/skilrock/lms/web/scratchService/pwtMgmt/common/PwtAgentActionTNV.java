/*
 * � copyright 2007, SkilRock Technologies, A division of Sugal & Damani Lottery Agency Pvt. Ltd.
 * All Rights Reserved
 * The contents of this file are the property of Sugal & Damani Lottery Agency Pvt. Ltd.
 * and are subject to a License agreement with Sugal & Damani Lottery Agency Pvt. Ltd.; you may
 * not use this file except in compliance with that License.  You may obtain a
 * copy of that license from:
 * Legal Department
 * Sugal & Damani Lottery Agency Pvt. Ltd.
 * 6/35,WEA, Karol Bagh,
 * New Delhi
 * India - 110005
 * This software is distributed under the License and is provided on an �AS IS�
 * basis, without warranty of any kind, either express or implied, unless
 * otherwise provided in the License.  See the License for governing rights and
 * limitations under the License.
 */

package com.skilrock.lms.web.scratchService.pwtMgmt.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
import com.skilrock.lms.beans.OrgBean;
import com.skilrock.lms.beans.PwtBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.coreEngine.scratchService.pwtMgmt.common.PwtAgentHelperTNV;

// import
// com.skilrock.lms.coreEngine.scratchService.pwtMgmt.common.PwtAgentHelper;

/**
 * This class provides methods for handling the PWT at Agent end
 * 
 * @author Skilrock Technologies
 * 
 */
public class PwtAgentActionTNV extends ActionSupport implements
		ServletRequestAware {
	public static final String APPLICATION_ERROR = "applicationError";

	static Log logger = LogFactory.getLog(PwtAgentActionTNV.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private double amount;
	private String[] gameNbr_Name;

	private String[] gameVirnCode;
	private Map<String, List<PwtBean>> gameVirnMap;
	private int[] inpCount; // Means input field count VIRN or Ticket Num
	private boolean isSuccess;
	private StringBuilder jsString;
	private List<PwtBean> pwtList;

	private HttpServletRequest request;
	private String retOrgName;

	private String[] ticketNumber;

	private String[] virnCode;

	/**
	 * This method displays the PWT page for agent
	 * 
	 * @return String
	 * @throws Exception
	 */
	public String displayPwtAgentEntryPage() throws Exception {

		HttpSession session = getRequest().getSession();
		setPwtList(new ArrayList<PwtBean>());

		UserInfoBean userInfoBean = (UserInfoBean) session
				.getAttribute("USER_INFO");

		int agtOrgId = userInfoBean.getUserOrgId();

		PwtAgentHelperTNV pwtAgentHelper = new PwtAgentHelperTNV();

		List activeGameList = pwtAgentHelper.getActiveGames(agtOrgId);
		List retOrgList = pwtAgentHelper.getRetailers(agtOrgId);
		session.setAttribute("ACTIVE_GAME_LIST", activeGameList);
		session.setAttribute("RET_ORG_LIST", retOrgList);

		List<PwtBean> pwtList = getPwtList();
		pwtList.add(new PwtBean());
		session.setAttribute("PWT_LIST", pwtList);
		return SUCCESS;

	}

	public double getAmount() {
		return amount;
	}

	public String[] getGameNbr_Name() {
		return gameNbr_Name;
	}

	public String[] getGameVirnCode() {
		return gameVirnCode;
	}

	public Map<String, List<PwtBean>> getGameVirnMap() {
		return gameVirnMap;
	}

	public int[] getInpCount() {
		return inpCount;
	}

	public boolean getIsSuccess() {
		return isSuccess;
	}

	public List getPwtList() {
		return pwtList;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public String getRetOrgName() {
		return retOrgName;
	}

	public String[] getTicketNumber() {
		return ticketNumber;
	}

	public String[] getVirnCode() {
		return virnCode;
	}

	public int[] getVirnCount() {
		return inpCount;
	}

	/**
	 * This method is used for saving the PWT data
	 * 
	 * @return String
	 * @throws Exception
	 */
	/*
	 * public String savePwtTicketsData() throws Exception {
	 * 
	 * List<String> allVirnCode = new ArrayList<String>(); gameVirnMap = new
	 * HashMap<String,List<PwtBean>>(); int startVirnCount =0; int
	 * endVirnCount =0;
	 * 
	 * String highPrizeCriteria=
	 * (String)ServletActionContext.getServletContext().getAttribute("HIGH_PRIZE_CRITERIA");
	 * String highPrizeAmt=
	 * (String)ServletActionContext.getServletContext().getAttribute("HIGH_PRIZE_AMT");
	 * logger.debug("high prize amt is " + highPrizeAmt); double
	 * HighPrizeAmount=0.0; if(!highPrizeAmt.equals(null)) {
	 * HighPrizeAmount=Double.parseDouble(highPrizeAmt); }
	 * 
	 * 
	 * HttpSession session = getRequest().getSession();
	 * 
	 * 
	 * List<ActiveGameBean> activeGameList = (List)
	 * session.getAttribute("ACTIVE_GAME_LIST"); List<OrgBean> retOrgList =
	 * (List) session.getAttribute("RET_ORG_LIST");
	 * 
	 * PwtAgentHelper pwtAgentHelper = new PwtAgentHelper();
	 * 
	 * UserInfoBean userInfoBean = (UserInfoBean)
	 * session.getAttribute("USER_INFO"); int agentUserId =
	 * userInfoBean.getUserId(); int agtOrgId = userInfoBean.getUserOrgId();
	 * 
	 * String loggedInUserOrgName = userInfoBean.getOrgName(); String rootPath =
	 * (String) session.getAttribute("ROOT_PATH"); logger.debug("About to call
	 * save dataa"); for(int i=0; i<getVirnCode().length; i++){
	 * if(getVirnCode()[i]!=null && !getVirnCode()[i].trim().equals("")){
	 * allVirnCode.add(getVirnCode()[i]); } } for (int i=0;i<gameNbr_Name.length;i++){
	 * logger.debug("Game Name--"+gameNbr_Name[i]);
	 * if(!gameNbr_Name[i].equals("-1")){ endVirnCount=
	 * startVirnCount+inpCount[i]; int inc =0; gameVirnCode = new
	 * String[endVirnCount-startVirnCount]; List<PwtBean> pwtList= new
	 * ArrayList<PwtBean>(); for(int j=startVirnCount;j<endVirnCount;j++){
	 * PwtBean pwtBean = null; pwtBean= new PwtBean();
	 * pwtBean.setVirnCode(allVirnCode.get(j)); pwtList.add(pwtBean);
	 * gameVirnCode[inc]=allVirnCode.get(j);
	 * logger.debug(gameNbr_Name[i]+"*-*-*"+gameVirnCode[inc]); inc++;
	 * startVirnCount++; } logger.debug("Game Name****"+gameNbr_Name[i]+" Virn
	 * "+gameVirnCode.length); if(gameVirnCode.length>0){
	 * //pwtAgentHelper.savePwtTicketsData(agentUserId, agtOrgId, //
	 * gameNbr_Name[i], getRetOrgName(), retOrgList, activeGameList, //
	 * gameVirnCode,
	 * pwtList,rootPath,loggedInUserOrgName,HighPrizeAmount,highPrizeCriteria);
	 * if(gameVirnMap.containsKey(gameNbr_Name[i])){ List<PwtBean> oldPwtList =
	 * gameVirnMap.get(gameNbr_Name[i]); oldPwtList.addAll(pwtList);
	 * gameVirnMap.put(gameNbr_Name[i], oldPwtList); }else{
	 * gameVirnMap.put(gameNbr_Name[i], pwtList); } amount = amount+
	 * pwtAgentHelper.getPwtAmount(pwtList); } } }
	 * 
	 * logger.debug("Afterrrrrrrrr callingggg save dataa");
	 * //logger.debug("Result "+gameVirnMap); if(amount > 0){ setSuccess(true);
	 * setAmount(amount); } else { logger.debug("----Zero amount-------");
	 * setSuccess(false); }
	 * 
	 * 
	 * 
	 * jsString = new StringBuilder(); Iterator itTkt =
	 * gameVirnMap.entrySet().iterator(); Map msgCode = new HashMap(); while
	 * (itTkt.hasNext()) { Map.Entry pairsTkt = (Map.Entry)itTkt.next();
	 * jsString.append(pairsTkt.getKey()+":"); List<PwtBean> virnList = (List<PwtBean>)pairsTkt.getValue();
	 * Iterator itTktList = virnList.iterator(); while (itTktList.hasNext()) {
	 * PwtBean bean=(PwtBean)itTktList.next();
	 * jsString.append(bean.getVirnCode()+"*M*"+bean.getMessageCode()+"*M*"+bean.getPwtAmount()+":");
	 * msgCode.put(bean.getMessageCode(),
	 * bean.getMessage()+":"+bean.getVerificationStatus()); }
	 * jsString.append("Nx*"); } Iterator itMsgCode =
	 * msgCode.entrySet().iterator(); while (itMsgCode.hasNext()) { Map.Entry
	 * pairsTkt = (Map.Entry)itMsgCode.next();
	 * jsString.append(pairsTkt.getKey()+"-"+pairsTkt.getValue()+"Msg"); }
	 * 
	 * session.setAttribute("VERIFIED_VIRN_JSSTRING",jsString);
	 * 
	 * logger.debug(jsString);
	 * 
	 * 
	 * 
	 * 
	 * session.setAttribute("savePwtTicketsData",this); return SUCCESS; }
	 */

	public String savePwtTicketsDataNew() throws Exception {

		List<String> allVirnCode = new ArrayList<String>();
		List<String> allTicketList = new ArrayList<String>();
		gameVirnMap = new HashMap<String, List<PwtBean>>();
		String highPrizeCriteria = (String) ServletActionContext
				.getServletContext().getAttribute("HIGH_PRIZE_CRITERIA");
		String highPrizeAmt = (String) ServletActionContext.getServletContext()
				.getAttribute("HIGH_PRIZE_AMT");
		logger.debug("high prize amt is " + highPrizeAmt);
		double HighPrizeAmount = 0.0;
		if (!highPrizeAmt.equals(null)) {
			HighPrizeAmount = Double.parseDouble(highPrizeAmt);
		}

		HttpSession session = getRequest().getSession();

		List<ActiveGameBean> activeGameList = (List) session
				.getAttribute("ACTIVE_GAME_LIST");
		List<OrgBean> retOrgList = (List) session.getAttribute("RET_ORG_LIST");

		PwtAgentHelperTNV pwtAgentHelper = new PwtAgentHelperTNV();

		UserInfoBean userInfoBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		//int agentUserId = userInfoBean.getUserId();
		//int agtOrgId = userInfoBean.getUserOrgId();

		String loggedInUserOrgName = userInfoBean.getOrgName();
		String rootPath = (String) session.getAttribute("ROOT_PATH");
		logger.debug("About to call save dataa");
		for (int i = 0; i < getVirnCode().length; i++) {
			if (getVirnCode()[i] != null && !getVirnCode()[i].trim().equals("")) {
				allVirnCode.add(getVirnCode()[i]);
			}
		}

		for (int i = 0; i < getTicketNumber().length; i++) {
			if (getTicketNumber()[i] != null
					&& !getTicketNumber()[i].trim().equals("")) {
				allTicketList.add(getTicketNumber()[i]);
			}
		}		
		
		//here check the size of both the list (If not same then throw the error)

		// get the organizatuion id from jsp
		int retOrgId = -1;
		int retUserId = -1;
		OrgBean retOrgBean = null;
		if (retOrgList != null) {
			for (int i = 0; i < retOrgList.size(); i++) {
				retOrgBean = retOrgList.get(i);
				if (retOrgName.equals(retOrgBean.getOrgId() + "")) {
					retOrgId = retOrgBean.getOrgId();
					retUserId = retOrgBean.getUserId();
					break;

				}
			}
		}
		gameVirnMap = pwtAgentHelper.savePwtTicketsDataNew(gameNbr_Name,
				allVirnCode, activeGameList, retUserId, Integer
						.parseInt(retOrgName), HighPrizeAmount,
				highPrizeCriteria, inpCount, userInfoBean.getUserId(), userInfoBean.getUserOrgId(), rootPath,
				loggedInUserOrgName, allTicketList);

		jsString = new StringBuilder();
		Iterator itTkt = gameVirnMap.entrySet().iterator();
		Map msgCode = new HashMap();
		while (itTkt.hasNext()) {
			Map.Entry pairsTkt = (Map.Entry) itTkt.next();
			jsString.append(pairsTkt.getKey() + ":");
			List<PwtBean> virnList = (List<PwtBean>) pairsTkt.getValue();
			Iterator itTktList = virnList.iterator();
			while (itTktList.hasNext()) {
				PwtBean bean = (PwtBean) itTktList.next();
				if (bean.getIsValid()) {
					amount = amount + Double.parseDouble(bean.getPwtAmount());
				}
				jsString.append(bean.getVirnCode() + "*M*"
						+ bean.getMessageCode() + "*M*" + bean.getPwtAmount()
						+ "*M*" + bean.getTicketNumber() + "*M*"
						+ bean.getTicketMessage() + "*M*"
						+ bean.getTicketVerificationStatus() + ":");
				msgCode.put(bean.getMessageCode(), bean.getMessage() + ":"
						+ bean.getVerificationStatus());
			}
			jsString.append("Nx*");
		}
		Iterator itMsgCode = msgCode.entrySet().iterator();
		while (itMsgCode.hasNext()) {
			Map.Entry pairsTkt = (Map.Entry) itMsgCode.next();
			jsString.append(pairsTkt.getKey() + "-" + pairsTkt.getValue()
					+ "Msg");
		}

		session.setAttribute("VERIFIED_VIRN_JSSTRING", jsString);

		logger.debug(jsString);
		if (amount > 0) {
			setSuccess(true);
			setAmount(amount);
		} else {
			logger.debug("----Zero amount-------");
			setSuccess(false);
		}
		System.out
				.println("Afterrrrrrrrr callingggg save dataa total amount is  "
						+ amount);
		session.setAttribute("savePwtTicketsData", this);
		return SUCCESS;

	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public void setGameNbr_Name(String[] gameNbr_Name) {
		this.gameNbr_Name = gameNbr_Name;
	}

	public void setGameVirnCode(String[] gameVirnCode) {
		this.gameVirnCode = gameVirnCode;
	}

	public void setGameVirnMap(Map<String, List<PwtBean>> gameVirnMap) {
		this.gameVirnMap = gameVirnMap;
	}

	public void setInpCount(int[] inpCount) {
		this.inpCount = inpCount;
	}

	public void setPwtList(List pwtList) {
		this.pwtList = pwtList;
	}

	public void setRetOrgName(String retOrgName) {
		this.retOrgName = retOrgName;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public void setTicketNumber(String[] ticketNumber) {
		this.ticketNumber = ticketNumber;
	}

	public void setVirnCode(String[] virnCode) {
		this.virnCode = virnCode;
	}

	public void setVirnCount(int[] virnCount) {
		this.inpCount = virnCount;
	}

}
