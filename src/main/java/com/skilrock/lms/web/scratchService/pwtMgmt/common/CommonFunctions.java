package com.skilrock.lms.web.scratchService.pwtMgmt.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.PlayerBean;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.scratchService.pwtMgmt.common.PlayerVerifyHelperForApp;

public class CommonFunctions extends ActionSupport implements
		ServletRequestAware, ServletResponseAware {
	static Log logger = LogFactory.getLog(CommonFunctions.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String firstName;
	private String idNumber;
	private String idType;
	private String lastName;
	private String orgType;

	private HttpServletRequest request;

	public String fetchPlayerDetails() throws LMSException {

		PlayerVerifyHelperForApp searchPlayerHelper = new PlayerVerifyHelperForApp();
		Map<String, Object> playerBeanMap = searchPlayerHelper.searchPlayer(
				firstName, lastName, idNumber, idType);
		PlayerBean plrBean = (PlayerBean) playerBeanMap.get("plrBean");
		HttpSession session = request.getSession();
		session.setAttribute("playerBean", plrBean);
		String plrAlreadyReg = "NO";
		if (plrBean != null) {
			plrAlreadyReg = "YES";
		}
		List<String> countryList = (ArrayList<String>) playerBeanMap
				.get("countryList");
		if (countryList == null) {
			countryList = new ArrayList<String>();
		}
		session.setAttribute("countryList", countryList);
		session.setAttribute("plrAlreadyReg", plrAlreadyReg);
		return SUCCESS;
	}

	/*
	 * public void updateClmableBalOfOrg() { PrintWriter out = null; try { out =
	 * response.getWriter();
	 * 
	 * HttpSession session = request.getSession(); UserInfoBean userInfoBean =
	 * (UserInfoBean) session.getAttribute("USER_INFO"); String rootPath =
	 * (String) session.getAttribute("ROOT_PATH");
	 * if("YES".equalsIgnoreCase(LMSFilterDispatcher.claimByClick)){ String
	 * receiptId =null; CommonFunctionsHelper helper = new
	 * CommonFunctionsHelper(); receiptId =
	 * helper.updateClmableBalOfOrg(userInfoBean.getUserOrgId(),
	 * userInfoBean.getUserOrgId(), userInfoBean.getUserType()); logger.debug("
	 * Claimed PWT's generated rec id = "+receiptId+" for Organization =
	 * "+userInfoBean.getUserOrgId()); }
	 * 
	 * AjaxRequestHelper ajxHelper = new AjaxRequestHelper(); String amtStr =
	 * ajxHelper.getAvlblCreditAmt(userInfoBean); out.print(amtStr);
	 * 
	 * }catch(Exception e){ e.printStackTrace(); if(out!=null) out.print(""); } }
	 * 
	 * 
	 * public String fetchUnClmablePwtDetail() throws LMSException { HttpSession
	 * session = request.getSession(); UserInfoBean userInfoBean =
	 * (UserInfoBean) session.getAttribute("USER_INFO");
	 * logger.debug("fetchUnClmablePwtDetail = "); CommonFunctionsHelper helper =
	 * new CommonFunctionsHelper(); Map map =
	 * helper.fetchUnCLMPwtDetailsOfOrg(userInfoBean.getUserOrgId(),
	 * userInfoBean.getUserType(), "UNCLAIM_BAL");
	 * 
	 * session.setAttribute("totalUnclmBal", map.get("totalUnclmBal"));
	 * session.setAttribute("unclmPwtDet", map.get("unclmPwtDet"));
	 * session.setAttribute("newDRGameWiseTotAmt",
	 * map.get("newDRGameWiseTotAmt"));
	 * session.setAttribute("DRClmPwtDetailMap", map.get("DRClmPwtDetailMap"));
	 * 
	 * return SUCCESS; }
	 */

	public String getFirstName() {
		return firstName;
	}

	public String getIdNumber() {
		return idNumber;
	}

	public String getIdType() {
		return idType;
	}

	public String getLastName() {
		return lastName;
	}

	public String getOrgType() {
		return orgType;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}

	public void setIdType(String idType) {
		this.idType = idType;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setServletResponse(HttpServletResponse res) {

	}

}