package com.skilrock.lms.web.accMgmt.common;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.AgentReceiptBean;
import com.skilrock.lms.beans.ChequeBeanClearance;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.beans.chequeAgentBean;
import com.skilrock.lms.beans.chequeForClearanceBean;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.accMgmt.common.ChequeClearanceHelper;

public class ChequeClearanceAction extends ActionSupport implements
		ServletRequestAware {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String agentOrgName;
	private String[] agtOrgName;
	List<AgentReceiptBean> agtReciptbeanlist = new ArrayList<AgentReceiptBean>();

	private Double[] chequeAmt;
	private Date[] chequeClearanceDate;
	private Date[] chequeDate;
	private String[] chequeNbr;

	private String[] chequeStatus;
	// variables for search cheques
	private String chkNbr;
	List<chequeAgentBean> chqAgtBeanList = new ArrayList<chequeAgentBean>();
	private String chqStatus;
	private Date[] clearanceDate;
	private Double[] commAmt;
	private String[] draweeBank;
	private String draweeBnk;
	private String[] isCleared;
	private String[] issuingPartyName;
	List<String> orgNameResults = new ArrayList<String>();
	private Map<Integer,String> orgNameMap= new HashMap<Integer,String>();
	List<chequeForClearanceBean> pendingChqList;

	HttpServletRequest request;
	private String []agentName;
	

	public String[] getAgentName() {
		return agentName;
	}

	public void setAgentName(String[] agentName) {
		this.agentName = agentName;
	}

	private String[] taskId;

	public String clearPendingCheques() throws LMSException {
		System.out.println("inside clear cheques");
		HttpSession session = null;
		session = getRequest().getSession();
		String rootPath = (String) session.getAttribute("ROOT_PATH");
		UserInfoBean userBean = null;
		userBean = (UserInfoBean) session.getAttribute("USER_INFO");
		String boOrgName = userBean.getOrgName();
		int userOrgID = userBean.getUserOrgId();
		ChequeClearanceHelper searchChqHelper = new ChequeClearanceHelper();

		agtReciptbeanlist = searchChqHelper.doChequeClear(chequeNbr, chequeAmt,
				chequeStatus, commAmt, agtOrgName, isCleared, draweeBank,
				chequeDate, issuingPartyName, chequeClearanceDate, userBean
						.getUserType(), rootPath, boOrgName, userOrgID,
				clearanceDate, userBean.getUserId(), taskId);
		System.out.println("before success clearing cheques ");
		session.setAttribute("agtReciptbeanlist", agtReciptbeanlist);
		return SUCCESS;

	}

	public String displaySearchPendingCheques() {

		/*ChequeClearanceHelper searchChqHelper = new ChequeClearanceHelper();
		orgNameResults = searchChqHelper.getAgtOrgList();
		setOrgNameResults(orgNameResults);
		AgentPaymentChequeHelper chequeHelper = new AgentPaymentChequeHelper();
		//orgNameList = chequeHelper.getOrganizations();
		// System.out.println(orgNameList.size() + orgNameList.get(0));
		 //setOrgNameList(orgNameList);
		setOrgNameMap(chequeHelper.getOrganizations());*/
		return SUCCESS;
	}

	public String getAgentOrgName() {
		return agentOrgName;
	}

	public String[] getAgtOrgName() {
		return agtOrgName;
	}

	public List<AgentReceiptBean> getAgtReciptbeanlist() {
		return agtReciptbeanlist;
	}

	public Double[] getChequeAmt() {
		return chequeAmt;
	}

	public Date[] getChequeClearanceDate() {
		return chequeClearanceDate;
	}

	public Date[] getChequeDate() {
		return chequeDate;
	}

	public String[] getChequeNbr() {
		return chequeNbr;
	}

	public String[] getChequeStatus() {
		return chequeStatus;
	}

	public String getChkNbr() {
		return chkNbr;
	}

	public List<chequeAgentBean> getChqAgtBeanList() {
		return chqAgtBeanList;
	}

	public String getChqStatus() {
		return chqStatus;
	}

	public Date[] getClearanceDate() {
		return clearanceDate;
	}

	public Double[] getCommAmt() {
		return commAmt;
	}

	public String[] getDraweeBank() {

		return draweeBank;
	}

	public String getDraweeBnk() {
		return draweeBnk;
	}

	public String[] getIsCleared() {
		return isCleared;
	}

	public String[] getIssuingPartyName() {
		return issuingPartyName;
	}

	public List<String> getOrgNameResults() {
		return orgNameResults;
	}

	public List<chequeForClearanceBean> getPendingChqList() {
		return pendingChqList;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public String[] getTaskId() {
		return taskId;
	}

	public String searchPendingCheques() {

		ChequeClearanceHelper searchChqHelper = new ChequeClearanceHelper();
		pendingChqList = searchChqHelper.searchPendingCheques(chkNbr,
				draweeBnk, chqStatus, agentOrgName);

		return SUCCESS;

	}

	public void setAgentOrgName(String agentOrgName) {
		this.agentOrgName = agentOrgName;
	}

	public void setAgtOrgName(String[] agtOrgName) {
		this.agtOrgName = agtOrgName;
	}

	public void setAgtReciptbeanlist(List<AgentReceiptBean> agtReciptbeanlist) {
		this.agtReciptbeanlist = agtReciptbeanlist;
	}

	public void setChequeAmt(Double[] chequeAmt) {
		this.chequeAmt = chequeAmt;
	}

	public void setChequeClearanceDate(Date[] chequeClearanceDate) {
		this.chequeClearanceDate = chequeClearanceDate;
	}

	public void setChequeDate(Date[] chequeDate) {
		this.chequeDate = chequeDate;
	}

	public void setChequeNbr(String[] chequeNbr) {
		System.out.println("dsfgdrs********" + chequeNbr + "*******");
		this.chequeNbr = chequeNbr;
	}

	public void setChequeStatus(String[] chequeStatus) {
		this.chequeStatus = chequeStatus;
	}

	public void setChkNbr(String chkNbr) {
		this.chkNbr = chkNbr;
	}

	public void setChqAgtBeanList(List<chequeAgentBean> chqAgtBeanList) {
		this.chqAgtBeanList = chqAgtBeanList;
	}

	public void setChqStatus(String chqStatus) {
		this.chqStatus = chqStatus;
	}

	public void setClearanceDate(Date[] clearanceDate) {
		this.clearanceDate = clearanceDate;
	}

	public void setCommAmt(Double[] commAmt) {
		this.commAmt = commAmt;
	}

	public void setDraweeBank(String[] draweeBank) {
		this.draweeBank = draweeBank;
	}

	public void setDraweeBnk(String draweeBnk) {
		this.draweeBnk = draweeBnk;
	}

	public void setIsCleared(String[] isCleared) {
		this.isCleared = isCleared;
	}

	public void setIssuingPartyName(String[] issuingPartyName) {
		this.issuingPartyName = issuingPartyName;
	}

	public void setOrgNameResults(List<String> orgNameResults) {
		this.orgNameResults = orgNameResults;
	}

	public void setPendingChqList(List<chequeForClearanceBean> pendingChqList) {
		this.pendingChqList = pendingChqList;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setTaskId(String[] taskId) {
		this.taskId = taskId;
	}

	public String verifyPendingCheques() {
		System.out.println("inside vrify pending cheques"
				+ clearanceDate.length);

		// System.out.println("length for cleared is" + isCleared.length + "
		// values:: " +isCleared[0]+" :: " + isCleared[1]);
		System.out.println("list size " + chequeNbr);
		System.out.println("list size ");
		Set<String> agtSet = new LinkedHashSet<String>();// agt Id Set
		Set<String> agtNameSet = new LinkedHashSet<String>();//agt name set
		String chkNbrTaskId = null;
		for (int i = 0; i < getAgtOrgName().length; i++) {
			agtSet.add(agtOrgName[i]);
			agtNameSet.add(agentName[i]);
		}

		System.out.println("before success " + agtSet.size());

		chequeAgentBean chqAgtBean;
		ChequeBeanClearance chqBean;
		List<ChequeBeanClearance> chqBeanList;

		Iterator itr = agtSet.iterator();
		Iterator itr1 = agtNameSet.iterator();
		while (itr.hasNext()) {
			System.out.println("inside while");
			chqAgtBean = new chequeAgentBean();
			chqBeanList = new ArrayList<ChequeBeanClearance>();

			String agentOrgName = (String) itr.next();
			String agentName =(String )itr1.next();
			for (int j = 0; j < agtOrgName.length; j++) {

				if (agtOrgName[j].equals(agentOrgName)) {
					chkNbrTaskId = chequeNbr[j];
					System.out.println(chkNbrTaskId.split(":")[0]
							+ "cheque of agt  : " + agentOrgName
							+ chkNbrTaskId.split(":")[1]);
					chqBean = new ChequeBeanClearance();
					chqBean.setChqNBR(chkNbrTaskId.split(":")[0]);
					chqBean.setChequeAmount(chequeAmt[j]);
					chqBean.setChequeStatus(isCleared[j]);
					chqBean.setCommAmt(commAmt[j]);
					chqBean.setDraweebank(draweeBank[j]);
					chqBean.setChequeDate(chequeDate[j]);
					chqBean.setIssuingPartyName(issuingPartyName[j]);
					System.out.println("fdgdddddddd");
					chqBean.setChequeClearanceDate(clearanceDate[j]);
					chqBean.setAgtOrgName(agentOrgName);
					chqBean.setTaskId(chkNbrTaskId.split(":")[1]);

					chqBeanList.add(chqBean);
					// System.out.println("dfgdfgdfgdfgdfg" +chequeNbr[j] +
					// "$$$$ " +chequeAmt[j] );
				}

			}
			chqAgtBean.setAgtOrgName(agentOrgName);
			chqAgtBean.setAgentName(agentName);
			chqAgtBean.setChequeDetails(chqBeanList);
			chqAgtBeanList.add(chqAgtBean);
			setChqAgtBeanList(chqAgtBeanList);

		}
		// System.out.println(" drawee bank in check list "
		// +chqAgtBeanList.get(0).getChequeDetails().get(0).getDraweebank());
		// System.out.println(" drawee bank in check list "
		// +chqAgtBeanList.get(0).getChequeDetails().get(1).getDraweebank());

		/*
		 * Map chequeMap=new TreeMap(); chequeMap.put("chequeNbr", chequeNbr);
		 * chequeMap.put("chequeAmt", chequeAmt); chequeMap.put("chequeStatus",
		 * chequeStatus); chequeMap.put("commAmt", commAmt);
		 * chequeMap.put("agtOrgName", agtOrgName); chequeMap.put("isCleared",
		 * isCleared);
		 * 
		 * request.setAttribute("chequeMap", chequeMap);
		 */

		return SUCCESS;
	}

	public Map<Integer, String> getOrgNameMap() {
		return orgNameMap;
	}

	public void setOrgNameMap(Map<Integer, String> orgNameMap) {
		this.orgNameMap = orgNameMap;
	}

}