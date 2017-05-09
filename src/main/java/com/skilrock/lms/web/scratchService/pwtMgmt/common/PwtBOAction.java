/*
 * © copyright 2007, SkilRock Technologies, A division of Sugal & Damani Lottery Agency Pvt. Ltd.
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
 * This software is distributed under the License and is provided on an “AS IS”
 * basis, without warranty of any kind, either express or implied, unless
 * otherwise provided in the License.  See the License for governing rights and
 * limitations under the License.
 */

package com.skilrock.lms.web.scratchService.pwtMgmt.common;

import java.util.ArrayList;
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
import com.skilrock.lms.coreEngine.scratchService.pwtMgmt.common.PwtBOHelper;

/**
 * This class provides methods for handling PWT at BO's end
 * 
 * @author Skilrock Technologies
 * 
 */
public class PwtBOAction extends ActionSupport implements ServletRequestAware {

	public static final String APPLICATION_ERROR = "applicationError";
	static Log logger = LogFactory.getLog(PwtBOAction.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String agtOrgName;
	private double amount;
	// private String[] pwtAmount;
	private String gameNbr_Name;

	private boolean isSuccess;
	private List<PwtBean> pwtList;
	private HttpServletRequest request;

	private String[] virnCode;

	private String virnFile;

	/**
	 * This method displays the PWT for BO
	 * 
	 * @return String
	 * @throws Exception
	 */
	public String displayPwtBOEntryPage() throws Exception {
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

	public String getAgtOrgName() {
		return agtOrgName;
	}

	public double getAmount() {
		return amount;
	}

	/*
	 * public String[] getPwtAmount() { return pwtAmount; }
	 * 
	 * public void setPwtAmount(String[] pwtAmount) { this.pwtAmount =
	 * pwtAmount; }
	 */
	public String getGameNbr_Name() {
		return gameNbr_Name;
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

	public String[] getVirnCode() {
		return virnCode;
	}

	public String getVirnFile() {
		return virnFile;
	}

	/**
	 * This method is used for saving the PWT data
	 * 
	 * @return String
	 * @throws Exception
	 */
	public String saveBOPwtTicketsData() throws Exception {

		HttpSession session = getRequest().getSession();
		List<PwtBean> pwtList = new ArrayList<PwtBean>();
		PwtBean pwtBean = null;
		List<String> virnStringList = new ArrayList<String>();
		List<PwtBean> duplicateVirnList = new ArrayList<PwtBean>();
		final String ENC_SCHEME_TYPE = "1W_ENC_OF_ALL";
		final String PWT_VERIFICATION_TYPE = "";

		/*
		 * for(int i=0; i<getVirnCode().length; i++){ if(getVirnCode()[i]!=null &&
		 * !getVirnCode()[i].trim().equals("")){
		 * if(virnStringList.contains(getVirnCode()[i].trim())) { pwtBean= new
		 * PwtBean(); pwtBean.setVirnCode(getVirnCode()[i].trim());
		 * pwtBean.setValid(false); pwtBean.setVerificationStatus("InValid
		 * VIRN"); pwtBean.setMessage("Duplicate Virn Entry in File");
		 * pwtBean.setMessageCode("112013"); duplicateVirnList.add(pwtBean);
		 * 
		 * }else virnStringList.add(getVirnCode()[i].trim()); } } //code added
		 * here by yogesh to read virn from file also try{ InputStreamReader
		 * fileStreamReader = new InputStreamReader(new
		 * FileInputStream(virnFile)); BufferedReader br = new
		 * BufferedReader(fileStreamReader); String strVirnLine=null; int
		 * fileVirnLimit=0; while((strVirnLine =br.readLine())!=null) {
		 * if("".equals(strVirnLine.trim())) continue; if(fileVirnLimit>5000) {
		 * addActionError("Data In File Exceeds limit "); return ERROR; }
		 * if(virnStringList.contains(strVirnLine.trim())) { pwtBean= new
		 * PwtBean(); pwtBean.setVirnCode(strVirnLine.trim());
		 * pwtBean.setValid(false); pwtBean.setVerificationStatus("Duplicate
		 * Virn Entry in File"); pwtBean.setMessage("InValid VIRN");
		 * pwtBean.setMessageCode("112013"); duplicateVirnList.add(pwtBean);
		 * }else virnStringList.add(strVirnLine.trim()); fileVirnLimit++; }
		 * }catch(FileNotFoundException fe) { //fe.printStackTrace(); //throw
		 * new LMSException("file not found exception",fe); }
		 */

		// now make PWT bean from whole virnStringList
		PwtBOHelper pwtBOHelper = new PwtBOHelper();
		Map map = pwtBOHelper.getVirnList(virnCode, virnFile);
		if (map != null && map.containsKey("error")) {
			addActionError("Data In File Exceeds limit ");
			return ERROR;
		} else if (map != null && map.isEmpty()) {
			addActionError("NO Virn is enterd ");
			return ERROR;
		}

		virnStringList = (ArrayList<String>) map.get("virnStringList");
		duplicateVirnList = (ArrayList<PwtBean>) map.get("duplicateVirnList");

		for (int i = 0; i < virnStringList.size(); i++) {
			pwtBean = new PwtBean();
			pwtBean.setMessage("Invalid Virn");
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
		System.out.println("About to call save dataa");

		pwtBOHelper.saveBOPwtTicketsData(getGameNbr_Name(), boOrgName,
				userOrgID, getAgtOrgName(), agtOrgList, activeGameList,
				getVirnCode(), pwtList, rootPath, HighPrizeAmount,
				highPrizeCriteria, userInfoBean.getUserId(), ENC_SCHEME_TYPE,
				PWT_VERIFICATION_TYPE);

		System.out.println("Afterrrrrrrrr callingggg save dataa");

		double amount = pwtBOHelper.getPwtAmount(pwtList);
		if (amount > 0) {
			setSuccess(true);
			setAmount(amount);
		} else {
			System.out.println("----Zero amount-------");
			setSuccess(false);
		}
		pwtList.addAll(duplicateVirnList);
		setPwtList(pwtList);
		session.setAttribute("saveBOPwtTicketsData", this);
		return SUCCESS;

	}

	public void setAgtOrgName(String agtOrgName) {
		this.agtOrgName = agtOrgName;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public void setGameNbr_Name(String gameNbr_Name) {
		this.gameNbr_Name = gameNbr_Name;
	}

	public void setPwtList(List pwtList) {
		this.pwtList = pwtList;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public void setVirnCode(String[] virnCode) {
		System.out.println("[[[[[[" + virnCode);
		this.virnCode = virnCode;
	}

	public void setVirnFile(String virnFile) {
		this.virnFile = virnFile;
	}

}
