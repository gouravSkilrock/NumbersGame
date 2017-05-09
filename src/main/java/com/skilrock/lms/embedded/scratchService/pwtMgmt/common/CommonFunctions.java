package com.skilrock.lms.embedded.scratchService.pwtMgmt.common;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.ajax.AjaxRequestHelper;
import com.skilrock.lms.beans.PlayerBean;
import com.skilrock.lms.beans.ServicesBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.FormatNumber;
import com.skilrock.lms.coreEngine.drawGames.playMgmt.DrawGameRPOSHelper;
import com.skilrock.lms.coreEngine.scratchService.pwtMgmt.common.PlayerVerifyHelperForApp;
import com.skilrock.lms.coreEngine.userMgmt.common.CommonFunctionsHelper;
import com.skilrock.lms.embedded.drawGames.common.EmbeddedErrors;
import com.skilrock.lms.sportsLottery.common.NotifySLE;
import com.skilrock.lms.sportsLottery.common.SLE;
import com.skilrock.lms.sportsLottery.userMgmt.javaBeans.UserDataBean;
import com.skilrock.lms.web.drawGames.common.Util;

public class CommonFunctions extends ActionSupport implements
		ServletRequestAware, ServletResponseAware {

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
	private HttpServletResponse response;

	private String userName;
	private long LSTktNo;
	private long slLastTxnId;
	
	public long getSlLastTxnId() {
		return slLastTxnId;
	}

	public void setSlLastTxnId(long slLastTxnId) {
		this.slLastTxnId = slLastTxnId;
	}

	public void back() {

		try {

			ServletContext sc = ServletActionContext.getServletContext();

			Map currentUserSessionMap = (Map) sc
					.getAttribute("LOGGED_IN_USERS");
			/*
			 * System.out.println(" LOGGED_IN_USERS maps is " +
			 * currentUserSessionMap);
			 */

			System.out.println(" user name is " + userName);

			HttpSession session = (HttpSession) currentUserSessionMap
					.get(userName);

			// HttpSession session = request.getSession();
			UserInfoBean userInfoBean = (UserInfoBean) session
					.getAttribute("USER_INFO");

			int userOrgId = userInfoBean.getUserOrgId();
			String availableCredit = null;// userBean.getAvailableCreditLimit();'
			Double unClmBal = null;
			String clmBal = null;

			Connection con = DBConnect.getConnection();

			String receiptId = null;
			CommonFunctionsHelper helper = new CommonFunctionsHelper();
			receiptId = helper.updateClmableBalOfOrg(userInfoBean.getUserId(),
					userInfoBean.getUserOrgId(), userInfoBean.getUserType(),
					userInfoBean.getParentOrgId());
			System.out.println(" Claimed PWT's generated rec id = " + receiptId
					+ " for Organization = " + userInfoBean.getUserOrgId());
			AjaxRequestHelper ajxHelper = new AjaxRequestHelper();
			availableCredit = ajxHelper.getAvlblCreditAmt(userInfoBean);
			clmBal = "0";

			try {
				Statement stmt = con.createStatement();

				String getACtDetails = "select unclaimable_bal from st_lms_organization_master where organization_type!='BO' and organization_id = "
						+ userOrgId;
				ResultSet result = stmt.executeQuery(getACtDetails);

				if (result.next()) {
					unClmBal = result.getDouble("unclaimable_bal");
				}

			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					if (con != null) {
						con.close();
					}
				} catch (SQLException se) {
					se.printStackTrace();
				}
			}

			try {
				response.getOutputStream()
						.write(
								(FormatNumber.formatNumber(availableCredit)
										+ ","
										+ FormatNumber.formatNumber(clmBal)
										+ "," + FormatNumber
										.formatNumber(unClmBal)).getBytes());
				System.out.println(" send data is "
						+ FormatNumber.formatNumber(availableCredit) + ","
						+ FormatNumber.formatNumber(clmBal) + ","
						+ FormatNumber.formatNumber(unClmBal));
			} catch (IOException e) {
				e.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();

		}

	}

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

	public String getAvlblCreditAmt(UserInfoBean bean, HttpSession session) {
		UserInfoBean userBean = bean;
		String avlCredit = "N";
		int userOrgId = userBean.getUserOrgId();
		Double availableCredit = null;// userBean.getAvailableCreditLimit();'
		Double unClmBal = null;
		Double clmBal = null;

		Connection con = DBConnect.getConnection();
		try {
			Statement stmt = con.createStatement();

			String getACtDetails = "select available_credit, claimable_bal, unclaimable_bal from st_organization_master where organization_type!='BO' and organization_id = "
					+ userOrgId;
			ResultSet result = stmt.executeQuery(getACtDetails);

			if (result.next()) {
				availableCredit = result.getDouble("available_credit");
				unClmBal = result.getDouble("unclaimable_bal");
				clmBal = result.getDouble("claimable_bal");
				avlCredit = FormatNumber.formatNumber(availableCredit) + "="
						+ FormatNumber.formatNumber(clmBal) + "="
						+ FormatNumber.formatNumber(unClmBal) + "="
						+ FormatNumber.formatNumber(availableCredit - clmBal);
			}

			userBean.setAvailableCreditLimit(availableCredit);
			userBean.setClaimableBal(clmBal);
			userBean.setUnclaimableBal(unClmBal);
			session.setAttribute("USER_INFO", userBean);

			System.out.println(avlCredit + "****---------********");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
		return avlCredit;
	}

	public String getFirstName() {
		return firstName;
	}
	
	public long getLSTktNo() {
		return LSTktNo;
	}

	public void setLSTktNo(long lSTktNo) {
		LSTktNo = lSTktNo;
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

	public String getUserName() {
		return userName;
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
		this.response = res;

	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void updateClmableBalOfOrg() {

		try {

			ServletContext sc = ServletActionContext.getServletContext();

			Map currentUserSessionMap = (Map) sc
					.getAttribute("LOGGED_IN_USERS");
			if (currentUserSessionMap == null) {
				response
						.getOutputStream()
						.write(
								("ErrorMsg:" + EmbeddedErrors.SESSION_EXPIRED + "ErrorCode:01|")
										.getBytes());
				return;
			}
			/*
			 * System.out.println(" LOGGED_IN_USERS maps is " +
			 * currentUserSessionMap);
			 */

			System.out.println(" user name is " + userName);

			HttpSession session = (HttpSession) currentUserSessionMap
					.get(userName);
			if (!CommonFunctionsHelper.isSessionValid(session)) {
				response
						.getOutputStream()
						.write(
								("ErrorMsg:" + EmbeddedErrors.SESSION_EXPIRED + "ErrorCode:01|")
										.getBytes());
				return;
			}
			// HttpSession session = request.getSession();
			UserInfoBean userInfoBean = (UserInfoBean) session
					.getAttribute("USER_INFO");

			//to cancel last sold ticket if unprinted at terminal end
			Map<Integer, Map<Integer, String>> drawIdTableMap = (Map<Integer, Map<Integer, String>>) sc
			.getAttribute("drawIdTableMap");
			String refMerchantId = (String) sc.getAttribute("REF_MERCHANT_ID");
			long lastPrintedTicket=0;
			int gameId = 0;
			if(LSTktNo !=0){
				lastPrintedTicket = LSTktNo/Util.getDivValueForLastSoldTkt(String.valueOf(LSTktNo).length());
				gameId = Util.getGameIdFromGameNumber(Util.getGamenoFromTktnumber(String.valueOf(LSTktNo)));
			}
			DrawGameRPOSHelper dgHelper = new DrawGameRPOSHelper();
			String actionName=ActionContext.getContext().getName();
			int autoCancelHoldDays=Integer.parseInt((String) sc.getAttribute("AUTO_CANCEL_CLOSER_DAYS"));
			dgHelper.checkLastPrintedTicketStatusAndUpdate(userInfoBean,lastPrintedTicket,"TERMINAL",refMerchantId,autoCancelHoldDays,actionName,gameId);
			
			if(ServicesBean.isSLE()) {
				try {
					UserDataBean dataBean = new UserDataBean();
					dataBean.setUserName(userInfoBean.getUserName());
					dataBean.setSessionId(userInfoBean.getUserSession());
					dataBean.setSlLastTxnId(getSlLastTxnId());

					NotifySLE notifySLE = new NotifySLE(SLE.Activity.LAST_TICKET_INFO, dataBean);
					notifySLE.asyncCall(notifySLE);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
			
			int userOrgId = userInfoBean.getUserOrgId();
			Double availableCredit = null;// userBean.getAvailableCreditLimit();'
			Double unClmBal = null;
			Double clmBal = null;
			// -----------------------
			Double extendedCreditLimit = null;
			Double currentCreditAmount = null;

			Connection con = DBConnect.getConnection();
			try {
				Statement stmt = con.createStatement();

				String getACtDetails = "select available_credit, claimable_bal, unclaimable_bal, extended_credit_limit, current_credit_amt from st_lms_organization_master where organization_type!='BO' and organization_id = "
						+ userOrgId;
				ResultSet result = stmt.executeQuery(getACtDetails);

				if (result.next()) {
					availableCredit = result.getDouble("available_credit");
					unClmBal = result.getDouble("unclaimable_bal");
					clmBal = result.getDouble("claimable_bal");
					extendedCreditLimit = result
							.getDouble("extended_credit_limit");
					currentCreditAmount = result
							.getDouble("current_credit_amt");
				}
				userInfoBean.setAvailableCreditLimit(availableCredit);
				userInfoBean.setClaimableBal(clmBal);
				userInfoBean.setUnclaimableBal(unClmBal);
				userInfoBean.setExtendedCreditLimit(extendedCreditLimit);
				userInfoBean.setCurrentCreditAmt(currentCreditAmount);

			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					if (con != null) {
						con.close();
					}
				} catch (SQLException se) {
					se.printStackTrace();
				}
			}

			try {
				// ------first data is actual balance..(AC-CB)-----
				/*response.getOutputStream().write(
						(FormatNumber.formatNumber(availableCredit - clmBal)
								+ ","
								+ FormatNumber.formatNumber(clmBal)
								+ ","
								+ FormatNumber.formatNumber(unClmBal)
								+ ","
								+ FormatNumber
										.formatNumber(extendedCreditLimit)
								+ "," + FormatNumber
								.formatNumber(currentCreditAmount)).getBytes());

				System.out.println(" send data is "
						+ FormatNumber.formatNumber(availableCredit) + ","
						+ FormatNumber.formatNumber(clmBal) + ","
						+ FormatNumber.formatNumber(unClmBal) + ","
						+ FormatNumber.formatNumber(extendedCreditLimit) + ","
						+ FormatNumber.formatNumber(currentCreditAmount));*/
				response.getOutputStream().write(
						("Balance:"+FormatNumber.formatNumber(availableCredit)
								+ ","
								+ FormatNumber.formatNumber(clmBal)
								+ ","
								+ FormatNumber.formatNumber(unClmBal)
								+ "|").getBytes());

				System.out.println(" send data is "
						+ "Balance:"+FormatNumber.formatNumber(availableCredit)
						+ ","
						+ FormatNumber.formatNumber(clmBal)
						+ ","
						+ FormatNumber.formatNumber(unClmBal)
						+ "|"
						);
				
			} catch (IOException e) {
				e.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();

		}

	}

}