package com.skilrock.lms.web.commercialService.playMgmt;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.SimpleTimeZone;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.httpclient.util.DateUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.CSSaleBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.commercialService.common.CSPWSaleHelper;
import com.skilrock.lms.coreEngine.commercialService.common.CSUtil;
import com.skilrock.lms.coreEngine.drawGames.playMgmt.DrawGameRPOSHelper;
import com.skilrock.lms.web.drawGames.common.CookieMgmtForTicketNumber;
import com.skilrock.lms.web.drawGames.common.Util;

public class CSWebSaleAction extends ActionSupport implements
		ServletRequestAware, ServletResponseAware {
	static Log logger = LogFactory.getLog(CSWebSaleAction.class);

	private static final long serialVersionUID = 1L;
	private HttpServletRequest request;
	private HttpServletResponse response;

	private String state;
	private String operatorCode;
	private String voucherType;
	private double denomination;
	private String opName;
	private double amt;
	private String mobileNo;

	public String retCSSaleMenu() {
		HttpSession session = request.getSession();
		Map<String, String> stateMap = CSUtil.fetchStates();
		if (stateMap != null) {

			Map<String, String> operatorMap = CSUtil.fetchOperatorMap();
			System.out.println("operatorMap:" + operatorMap);
			session.setAttribute("STATE_OPERATORS_MAP", operatorMap);
			session.setAttribute("STATE_MAP", stateMap);
			System.out.println("stateMap:" + stateMap);
		} else {
			return ERROR;
		}
		return SUCCESS;
	}

	public void fetchCircleMap() {
		Map<String, String> circleMap = CSUtil.fetchCircleMap(operatorCode);
		try {
			PrintWriter out = response.getWriter();
			out.write(circleMap.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void fetchCategoryMap() {
		Map<String, String> categoryMap = CSUtil.fetchCategoryMap(operatorCode,
				state);
		try {
			PrintWriter out = response.getWriter();
			out.write(categoryMap.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void fetchDenominationsMap() {
		Map<String, String> denominationsMap = CSUtil.fetchDenominationsMap(
				operatorCode, state, voucherType);
		try {
			PrintWriter out = response.getWriter();
			out.write(denominationsMap.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String purchaseTicketProcess() throws Exception {
		ServletContext sc = ServletActionContext.getServletContext();
		HttpSession session = request.getSession();

		String agtId = (String) sc.getAttribute("PW_MERCHANT_ID");
		String agtPwd = (String) sc.getAttribute("PW_MERCHANT_PWD");
		String loginStatus = (String) sc
				.getAttribute("PW_MERCHANT_LOGIN_STATUS");
		String apiVer = (String) sc.getAttribute("PW_PAYWORLD_API_VERSION");

		UserInfoBean userInfoBean = (UserInfoBean) session
				.getAttribute("USER_INFO");

		
		int gameId=0;
		long lastPrintedTicket=0;
		String refMerchantId = (String) sc.getAttribute("REF_MERCHANT_ID");
		int autoCancelHoldDays=Integer.parseInt((String) sc.getAttribute("AUTO_CANCEL_CLOSER_DAYS"));
		
		String actionName=ActionContext.getContext().getName();
		DrawGameRPOSHelper drawGameRPOSHelper = new DrawGameRPOSHelper();
		//drawGameRPOSHelper.checkLastPrintedTicketStatusAndUpdate(userInfoBean,lastPrintedTicket,"WEB",refMerchantId,autoCancelHoldDays,actionName,gameId);
		try{
			long LSTktNo =  CookieMgmtForTicketNumber.getTicketNumberFromCookie(request, userInfoBean.getUserName());
			if(LSTktNo !=0){
				lastPrintedTicket = LSTktNo/Util.getDivValueForLastSoldTkt(String.valueOf(LSTktNo).length());
				gameId = Util.getGameIdFromGameNumber(Util.getGamenoFromTktnumber(String.valueOf(LSTktNo)));
			}
			drawGameRPOSHelper.insertEntryIntoPrintedTktTableForWeb(gameId, userInfoBean.getUserOrgId(), lastPrintedTicket, "WEB", Util.getCurrentTimeStamp(), actionName);
		}catch(Exception e){
			//e.printStackTrace();
		}
		
		
		
		if (!"-1".equalsIgnoreCase(voucherType)) {
			double rechargeAmt = 0.0;
			String narration = "testing";

			CSSaleBean saleBean = new CSSaleBean();
			saleBean.setMult(1);
			saleBean.setOperatorCode(operatorCode);
			saleBean.setUserName(userInfoBean.getUserName());
			saleBean.setCSRefTxId(-1);

			if ("Flexible".equalsIgnoreCase(voucherType)) {
				saleBean.setDenomination(0.0);
				saleBean.setUnitPrice(amt);
				saleBean.setMrpAmt(amt);
				rechargeAmt = amt;
			} else if ("Pin".equalsIgnoreCase(voucherType)) {
				saleBean.setDenomination(denomination);
				saleBean.setUnitPrice(denomination);
				saleBean.setMrpAmt(denomination);
				// denom = (int) denomination;
			}

			saleBean.setOperatorCode(operatorCode);

			String returnData = CSUtil.fetchCSSaleData(saleBean);

			String cookieValue = fetchCookieValue();

			String csResp = new CSPWSaleHelper().pwSaleTransaction(saleBean
					.getProdCode(), userInfoBean.getUserOrgId() + "",
					userInfoBean.getUserName(), saleBean.getOperatorCode(),
					saleBean.getCircleCode(), denomination, rechargeAmt,
					saleBean.getMult(), narration, agtId, agtPwd, loginStatus,
					apiVer, (String) ServletActionContext.getServletContext()
							.getAttribute("cs_isVoucherPrintON"), mobileNo,
					Long.parseLong(cookieValue));

			StringBuilder finalData = new StringBuilder("");

			if (!(csResp.contains("Error") || csResp.contains("ERROR"))) {
				finalData.append("mode=CS_SALE");
				finalData.append("|" + returnData);
				// 99705.35%$01131903153773348900%$2011-10-19
				// 19:08:57%$00001795170019153551%$1795170019153551%$2011-12-31|

				finalData.append("|mrpAmt=" + saleBean.getMrpAmt());
				finalData.append("|retOrg=" + userInfoBean.getOrgName());
				finalData.append("|compName="
						+ ((String) sc.getAttribute("ORG_NAME_JSP")));
				finalData.append("|currSymbol="
						+ ((String) sc.getAttribute("CURRENCY_SYMBOL")));
				finalData.append("|currSymbol="
						+ ((String) sc.getAttribute("CURRENCY_SYMBOL")));
				finalData.append("|sampleStatus="
						+ ((String) sc.getAttribute("SAMPLE")));

				String[] respDataArr = csResp.replace("|", "").replace("$", "")
						.split("%");

				String lmsTxnId = respDataArr[0];
				session.setAttribute("LMS_LAST_TXN_ID", lmsTxnId);

				StringBuilder topMsgsStr = new StringBuilder(" ");
				StringBuilder bottomMsgsStr = new StringBuilder(" ");

				int appletHeight = Util
						.getAdvMsgs(Util.getAdvMessage(userInfoBean
								.getUserOrgId(), saleBean.getCategoryId(),
								"PLAYER", "SALE", "CS"), topMsgsStr,
								bottomMsgsStr, 400);

				String bal = respDataArr[1];
				String txnNo = respDataArr[2];
				String purchaseTime = respDataArr[3];
				String serialNo = respDataArr[4];
				String pinNo = respDataArr[5];
				String expDate = respDataArr[6];

				finalData.append("|txnNo=" + txnNo);
				finalData.append("|purchaseTime=" + purchaseTime);
				finalData.append("|serialNo=" + serialNo);
				finalData.append("|pinNo=" + pinNo);
				finalData.append("|expDate=" + expDate);
				finalData.append("|topAdvMsg=" + topMsgsStr);
				finalData.append("|bottomAdvMsg=" + bottomMsgsStr);
				finalData.append("|ctr=" + appletHeight);
				System.out
						.println("FINAL CS SALE DATA:" + finalData.toString());
				session.setAttribute("FINAL_DATA", finalData.toString());
			} else if ("ErrorMsg:Reprint last voucher|ErrorCode:02"
					.equalsIgnoreCase(csResp)) {
				System.out.println("FINAL CS SALE DATA:" + csResp);
				session.setAttribute("IS_REPRINT", "true");
				return ERROR;
			} else {
				System.out.println("FINAL CS SALE DATA:" + csResp);
				session.setAttribute("FINAL_DATA_ERROR", csResp);
				session.setAttribute("IS_REPRINT", "false");
				return ERROR;
			}

		} else {
			return ERROR;
		}
		return SUCCESS;
	}

	public void fetchVoucherDenominations() throws IOException {
		Connection con = DBConnect.getConnection();
		PrintWriter out = getResponse().getWriter();
		try {
			String qry = "select denomination from st_cs_product_master where status = 'ACTIVE' and denomination != 'Flexible' and operator_code = '"
					+ opName + "'";
			System.out.println("Query to fetch denominations: " + qry);
			PreparedStatement pstmt = con.prepareStatement(qry);
			ResultSet rs = pstmt.executeQuery();
			List<String> denominationList = new ArrayList<String>();
			while (rs.next()) {
				denominationList.add(rs.getString("denomination"));
			}
			out.print(denominationList);

			/*
			 * HttpSession session = request.getSession(); UserInfoBean userInfo =
			 * (UserInfoBean) session.getAttribute("USER_INFO");
			 * out.print(CommonMethods.changeCrNoteBalRet(Double.parseDouble(listType),userInfo.getUserOrgId()));
			 */

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnect.closeCon(con);
		}
	}

	public String updateLastTxnId() {
		String newValue = (String) request.getSession().getAttribute(
				"LMS_LAST_TXN_ID");
		updateCookieValue(newValue);
		return SUCCESS;
	}

	public String reprintLastTxnId() {
		ServletContext sc = ServletActionContext.getServletContext();
		StringBuilder finalData = new StringBuilder("");

		try {
			HttpSession session = request.getSession();
			UserInfoBean userInfoBean = (UserInfoBean) session
					.getAttribute("USER_INFO");
			CSPWSaleHelper helper = new CSPWSaleHelper();
			String agtid = (String) sc.getAttribute("PW_MERCHANT_ID");
			String agtpwd = (String) sc.getAttribute("PW_MERCHANT_PWD");
			String loginstatus = (String) sc
					.getAttribute("PW_MERCHANT_LOGIN_STATUS");
			String appver = (String) sc
					.getAttribute("PW_PAYWORLD_CLIENT_VERSION");

			String csResp = helper.fetchReprintLastTrans(userInfoBean
					.getUserOrgId(), agtid, agtpwd, loginstatus, appver,
					userInfoBean);

			if (!(csResp.contains("Error") || csResp.contains("ERROR"))) {
				finalData.append("mode=CS_SALE");
				String[] respDataArr = csResp.replace("|", "").replace("$", "")
						.split("%");

				String lmsTxnId = respDataArr[2];
				session.setAttribute("LMS_LAST_TXN_ID", lmsTxnId);

				String bal = respDataArr[0];
				String txnNo = respDataArr[1];
				String purchaseTime = respDataArr[10];
				String serialNo = respDataArr[3];
				String pinNo = respDataArr[4];
				String expDate = respDataArr[11];

				CSSaleBean saleBean = new CSSaleBean();

				String returnData = CSUtil.fetchCSReprintData(saleBean, Integer
						.parseInt(lmsTxnId));
				finalData.append("|" + returnData);
				// 99705.35%$01131903153773348900%$2011-10-19
				// 19:08:57%$00001795170019153551%$1795170019153551%$2011-12-31|

				finalData.append("|mrpAmt=" + saleBean.getMrpAmt());
				finalData.append("|retOrg=" + userInfoBean.getOrgName());
				finalData.append("|compName="
						+ ((String) sc.getAttribute("ORG_NAME_JSP")));
				finalData.append("|currSymbol="
						+ ((String) sc.getAttribute("CURRENCY_SYMBOL")));
				finalData.append("|currSymbol="
						+ ((String) sc.getAttribute("CURRENCY_SYMBOL")));
				finalData.append("|sampleStatus="
						+ ((String) sc.getAttribute("SAMPLE")));
				// finalData.append("|instruction=" + instruction);

				finalData.append("|txnNo=" + txnNo);
				finalData.append("|purchaseTime=" + purchaseTime);
				finalData.append("|serialNo=" + serialNo);
				finalData.append("|pinNo=" + pinNo);
				finalData.append("|expDate=" + expDate);
				finalData.append("|ctr=" + 410);
				System.out
						.println("FINAL CS SALE DATA:" + finalData.toString());
				session.setAttribute("FINAL_DATA", finalData.toString());
			} else {
				System.out.println("FINAL CS SALE DATA:" + csResp);
				session.setAttribute("FINAL_DATA_ERROR", csResp);
				return ERROR;
			}
		} catch (Exception e) {
			logger.debug("Some Error occured in PW "
					+ new LMSException(e).getMessage());
		}
		return SUCCESS;
	}

	public String fetchCookieValue() {
		boolean found = false;
		Cookie cookie = null;
		Cookie[] cookies = request.getCookies();
		for (Cookie element : cookies) {
			cookie = element;
			if (cookie.getName().equals("LMS_LAST_TXN_ID")) {
				found = true;
				break;
			}
		}

		if (!found) {
			cookie = new Cookie("LMS_LAST_TXN_ID", "0");
			cookie.setMaxAge(48*60*60*1000);
			response.addCookie(cookie);
		} else {
			cookie.setMaxAge(48*60*60*1000);
			response.addCookie(cookie);

		}

		String value = cookie.getValue();
		System.out.println("Cookie Initial Value:" + value + "******");
		return value;
	}

	public String updateCookieValue(String newValue) {
		boolean found = false;
		Cookie cookie = null;

		if (newValue != null && !"null".equalsIgnoreCase(newValue)) {
			Cookie[] cookies = request.getCookies();
			for (Cookie element : cookies) {
				cookie = element;
				if (cookie.getName().equals("LMS_LAST_TXN_ID")) {
					found = true;
					break;
				}
			}
			if (!found) {
				cookie = new Cookie("LMS_LAST_TXN_ID", newValue);
				cookie.setMaxAge(48*60*60*1000);				response.addCookie(cookie);
			} else {
				cookie.setValue(newValue);
				cookie.setMaxAge(48*60*60*1000);				response.addCookie(cookie);

			}
		}

		String value = cookie.getValue();
		System.out.println("Cookie Updated Value:" + value + "******");
		return value;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getOperatorCode() {
		return operatorCode;
	}

	public void setOperatorCode(String operatorCode) {
		this.operatorCode = operatorCode;
	}

	public String getVoucherType() {
		return voucherType;
	}

	public void setVoucherType(String voucherType) {
		this.voucherType = voucherType;
	}

	public double getDenomination() {
		return denomination;
	}

	public void setDenomination(double denomination) {
		this.denomination = denomination;
	}

	public double getAmt() {
		return amt;
	}

	public void setAmt(double amt) {
		this.amt = amt;
	}

	public String getOpName() {
		return opName;
	}

	public void setOpName(String opName) {
		this.opName = opName;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

}
