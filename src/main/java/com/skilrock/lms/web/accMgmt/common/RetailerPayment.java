/***
 *  * � copyright 2007, SkilRock Technologies, A division of Sugal & Damani Lottery Agency Pvt. Ltd.
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
 * 
 */
package com.skilrock.lms.web.accMgmt.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.ChequeBean;
import com.skilrock.lms.beans.PaymentBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryHelper;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.CommonMethods;

/**
 * This class is used to process payment from Retailer
 * 
 * @author Skilrock Technologies
 * 
 */
public class RetailerPayment extends ActionSupport implements
		ServletRequestAware {

	private static final long serialVersionUID = 1271130427666936592L;
	private String bankName = null;
	private double chequeAmount;
	private String chequeDate;
	private String chequeNumber;
	private String issuePartyname;
	private String orgName = null;
	private String orgType;
	private String paymentType;
	private HttpServletRequest request;
	private int sNo;
	private double totalAmount;
	private String userName;

	

	public String addMore() throws Exception {
		HttpSession session = null;

		session = getRequest().getSession();

		return SUCCESS;
	}

	/**
	 * This method is used to process cash from Agent
	 * 
	 * @return SUCCESS
	 * @throws LMSException
	 */
	public String Cash() throws Exception {
		HttpSession session = null;

		session = getRequest().getSession();

		return SUCCESS;
	}

	/**
	 * cashprocess() is not used from 04-07-2009 This class is used to take and
	 * process cash entry.
	 * 
	 * @return
	 * @throws Exception
	 */
	public String CashProcess() throws Exception {

		HttpSession session = null;
		List<PaymentBean> paymetList = null;
		double totalCash = 0.00;
		session = getRequest().getSession();
		System.out.println("Session :" + session);
		PaymentBean paymentBean = new PaymentBean();
		paymentBean.setPayType("Cash");
		paymentBean.setDescription("Cash Amount added");
		paymentBean.setAmount(totalAmount);

		if (session.getAttribute("ORG_TYPE") == null) {
			session.setAttribute("ORG_TYPE", orgType);

		}
		if (session.getAttribute("ORG_NAME") == null) {
			session.setAttribute("ORG_NAME", orgName);

		}
		if (session.getAttribute("COUNT") == null) {
			session.setAttribute("COUNT", new Integer(1));
			paymentBean.setSNo(new Integer(1));
		} else {

			int count = ((Integer) session.getAttribute("COUNT")).intValue();
			count = count + 1;
			session.setAttribute("COUNT", new Integer(count));
			paymentBean.setSNo(new Integer(count));
		}

		if (session.getAttribute("PAYMENT_LIST") != null) {
			paymetList = (List) session.getAttribute("PAYMENT_LIST");

			paymetList.add(paymentBean);
		} else {
			paymetList = new ArrayList<PaymentBean>();
			paymetList.add(paymentBean);

		}
		if (session.getAttribute("CASH") != null) {
			totalCash = (Double) session.getAttribute("CASH");
			totalAmount = totalAmount + totalCash;
			session.setAttribute("CASH", totalAmount);

		} else {
			session.setAttribute("CASH", totalAmount);
		}

		session.setAttribute("PAYMENT_LIST", paymetList);

		return SUCCESS;
	}

	/**
	 * This method is used to process cheque from Agent
	 * 
	 * @return SUCCESS
	 * @throws Exception
	 */
	public String Cheque() throws Exception {
		HttpSession session = null;
		session = getRequest().getSession();
		System.out.println("===companyList === "
				+ session.getAttribute("companyList"));

		return SUCCESS;
	}

	/**
	 * This class is used to take and process cheque entry.
	 * 
	 * @return
	 * @throws Exception
	 */
	public String ChequeProcess() throws Exception {

		HttpSession session = null;
		session = getRequest().getSession();
		UserInfoBean userInfo = (UserInfoBean) session
				.getAttribute("USER_INFO");
		List<PaymentBean> paymetList = null;
		List<ChequeBean> chequeList = null;
		ChequeBean chequeBean = null;
		if (validateCheque(bankName, chequeNumber)) {

			addActionError("Cheque Number: " + chequeNumber + " from bank "
					+ bankName + " has already been submitted ");
			// conn.rollback();
			return ERROR;
		}
		if (session.getAttribute("CHEQUE_LIST") != null) {
			chequeList = (List<ChequeBean>) session.getAttribute("CHEQUE_LIST");
			boolean duplicateFlag = validateChequeForDuplicateEntry(chequeList,
					chequeNumber, bankName);
			if (duplicateFlag) {
				addActionError("Cheque Number: " + chequeNumber + " from bank "
						+ bankName + " has  been already Added In To The Cart ");
				return ERROR;
			}
			chequeBean = new ChequeBean();
			chequeBean.setOrgName(orgName);
			chequeBean.setChequeNumber(chequeNumber);
			chequeBean.setChequeAmount(chequeAmount);
			chequeBean.setChequeDate(chequeDate);
			chequeBean.setBankName(bankName);
			chequeBean.setIssuePartyname(issuePartyname);
			chequeList.add(chequeBean);
		} else {
			chequeList = new ArrayList<ChequeBean>();
			chequeBean = new ChequeBean();
			chequeBean.setOrgName(orgName);
			chequeBean.setChequeNumber(chequeNumber);
			chequeBean.setChequeAmount(chequeAmount);
			chequeBean.setChequeDate(chequeDate);
			chequeBean.setBankName(bankName);
			chequeBean.setIssuePartyname(issuePartyname);
			chequeList.add(chequeBean);

		}
		double totalAmount = 0.0;
		for (int i = 0; i < chequeList.size(); i++) {
			totalAmount = totalAmount + chequeList.get(i).getChequeAmount();
		}
		String errMsg = CommonMethods.chkCreditLimitAgt(
				userInfo.getUserOrgId(), totalAmount);
		if (!"TRUE".equals(errMsg)) {
			addActionError(errMsg);
			return ERROR;
		}

		session.setAttribute("CHEQUE_LIST", chequeList);

		session = getRequest().getSession();
		System.out.println("Session :" + session);
		PaymentBean paymentBean = new PaymentBean();
		paymentBean.setPayType("Cheque");
		paymentBean.setDescription("Cheque No: " + chequeNumber
				+ " from bank : " + bankName);
		paymentBean.setAmount(chequeAmount);
		paymentBean.setChequeNo(chequeNumber);
		paymentBean.setBankName(bankName);
		if (session.getAttribute("ORG_TYPE") == null) {
			session.setAttribute("ORG_TYPE", orgType);

		}
		if (session.getAttribute("ORG_NAME") == null) {
			session.setAttribute("ORG_NAME", orgName);

		}
		if (session.getAttribute("COUNT") == null) {
			session.setAttribute("COUNT", new Integer(1));
			paymentBean.setSNo(new Integer(1));
		} else {

			int count = ((Integer) session.getAttribute("COUNT")).intValue();
			count = count + 1;
			session.setAttribute("COUNT", new Integer(count));
			paymentBean.setSNo(new Integer(count));
		}
		if (session.getAttribute("PAYMENT_LIST") != null) {
			paymetList = (List) session.getAttribute("PAYMENT_LIST");

			paymetList.add(paymentBean);
		} else {
			paymetList = new ArrayList<PaymentBean>();
			paymetList.add(paymentBean);

		}
		session.setAttribute("PAYMENT_LIST", paymetList);

		return SUCCESS;
	}

	public String getBankName() {
		return bankName;
	}

	public double getChequeAmount() {
		return chequeAmount;
	}

	public String getChequeDate() {
		return chequeDate;
	}

	public String getChequeNumber() {
		return chequeNumber;
	}

	public String getIssuePartyname() {
		return issuePartyname;
	}

	public String getOrgName() {
		return orgName;
	}

	public String getOrgType() {
		return orgType;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public int getSNo() {
		return sNo;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public String getUserName() {
		return userName;
	}

	/**
	 * This method is used to remove payment entry from the cart.
	 * 
	 * @return
	 * @throws Exception
	 */
	public String RemovePayEntity() throws Exception {

		HttpSession session = null;
		List<PaymentBean> paymetList = null;
		session = getRequest().getSession();
		System.out.println("Session :" + session);
		PaymentBean paymentBean = null;
		List cash = null;
		if (session.getAttribute("PAYMENT_LIST") != null) {
			paymetList = (List) session.getAttribute("PAYMENT_LIST");

			for (int i = 0; i < paymetList.size(); i++) {

				paymentBean = (PaymentBean) paymetList.get(i);
				System.out.println("removing processing... "
						+ paymentBean.getSNo()
						+ "\n removing entity Sno is... " + sNo
						+ "\nremoving entity ampunt is... " + totalAmount
						+ "\nremoving entity from bean ampunt is... "
						+ paymentBean.getAmount()
						+ "\nremoving entity pay type is... "
						+ paymentType.trim()
						+ "\nremoving entity pay type from bean is... "
						+ paymentBean.getPayType().trim());

				if (paymentBean.getSNo() == sNo
						&& paymentBean.getAmount() == totalAmount
						&& paymentType.trim().equals(
								paymentBean.getPayType().trim())) {
					System.out.println("--------------------removing  entry ");

					if (paymentBean.getPayType().trim().equals("Cash")) {
						if (session.getAttribute("CASH") != null) {
							System.out.println("Total Cash Before Removeing"
									+ ((Double) session.getAttribute("CASH"))
											.doubleValue());
							System.out.println("Total Cash to Remove"
									+ paymentBean.getAmount());
							double totalCash = ((Double) session
									.getAttribute("CASH")).doubleValue()
									- paymentBean.getAmount();
							session.setAttribute("CASH", totalCash);
							System.out.println("Total Cash after Remove "
									+ totalCash);
						}
					}
					if (paymentBean.getPayType().trim().equals("Cheque")) {
						System.out
								.println("Cheque type iterarion start... to remove");
						if (session.getAttribute("CHEQUE_LIST") != null) {
							List<ChequeBean> chqList = (List) session
									.getAttribute("CHEQUE_LIST");
							for (int j = 0; j < chqList.size(); j++) {
								System.out
										.println("Cheque type iterarion ... to remove and size is "
												+ chqList.size());
								ChequeBean chqBean = (ChequeBean) chqList
										.get(j);
								System.out
										.println("removing processing...bank name from bean "
												+ chqBean.getBankName());
								System.out
										.println("removing entity bank name... "
												+ bankName);
								System.out
										.println("removing entity amount is... "
												+ chqBean.getChequeAmount());
								System.out
										.println("removing entity from bean amount is... "
												+ chequeAmount);
								System.out
										.println("removing entity chq Number from bean is... "
												+ chqBean.getChequeNumber());
								System.out
										.println("removing entity chq number... "
												+ chequeNumber);
								if (chqBean.getChequeNumber().equals(
										chequeNumber)
										&& chqBean.getChequeAmount() == chequeAmount
										&& chqBean.getBankName().trim().equals(
												bankName)) {
									chqList.remove(j);
									System.out
											.println("Cheque is Removed from List");
									session.setAttribute("PAYMENT_LIST",
											chqList);
								}
							}

						}
					}
					paymetList.remove(i);
					break;
				}
			}
			session.setAttribute("PAYMENT_LIST", paymetList);
		}

		test(session);

		return SUCCESS;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public void setChequeAmount(double chequeAmount) {
		this.chequeAmount = chequeAmount;
	}

	public void setChequeDate(String chequeDate) {
		this.chequeDate = chequeDate;
	}

	public void setChequeNumber(String chequeNumber) {
		this.chequeNumber = chequeNumber;
	}

	public void setIssuePartyname(String issuePartyname) {
		this.issuePartyname = issuePartyname;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setSNo(int no) {
		sNo = no;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * This method is used to process cash,cheque receive for payment from
	 * Retailer
	 * 
	 * @return
	 * @throws Exception
	 */

	public String start() throws Exception {
		try {
			HttpSession session = null;
			System.out.println(session);
			session = getRequest().getSession();
			System.out.println("session1" + session);
			session.setAttribute("companyList", null);
			session.setAttribute("ORG_TYPE", null);
			session.setAttribute("ORG_NAME", null);
			session.setAttribute("COUNT", null);
			session.setAttribute("PAYMENT_LIST", null);
			session.setAttribute("CHEQUE_LIST", null);
			session.setAttribute("CASH", null);
			session.setAttribute("userNameList", null);
			session.setAttribute("orgNameIdMap", null);
			UserInfoBean userInfo = null;
			session.setAttribute("CHEQUE_END_DATE_START", null);
			session.setAttribute("CHEQUE_START_DATE_START", null);
			Date currDate = new Date();
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Long stLong = currDate.getTime() - 60L * 60L * 24 * 1000 * 30 * 6;
			Date startDate = new Date(stLong);
			String strCurrDate = dateFormat.format(currDate);
			String startDateString = dateFormat.format(startDate);
			System.out.println(strCurrDate + "dateeeeeeeee");

			userInfo = (UserInfoBean) session.getAttribute("USER_INFO");
			int agentId = userInfo.getUserOrgId();
			session.setAttribute("companyList", null);
			QueryHelper qp = new QueryHelper();
			//  List searchResults = qp.searchOrganizationForRetailer(agentId);
			List searchResultsUser = qp.searchUser();

			Map userNameIdGroup = new HashMap();
			userNameIdGroup = qp.getUserIdGroup();
			Map orgNameIdGroup = new HashMap();
			orgNameIdGroup = qp.getOrgIdGroup();

			session.setAttribute("CHEQUE_END_DATE_START", strCurrDate);
			session.setAttribute("CHEQUE_START_DATE_START", startDateString);
			if (searchResultsUser != null && searchResultsUser.size() > 0) {
				// session.setAttribute("companyList", searchResults);
				session.setAttribute("userNameList", searchResultsUser);
				session.setAttribute("orgNameIdMap", orgNameIdGroup);
				/*System.out.println("?????????"
						+ (List) session.getAttribute("companyList"));*/
				System.out.println(">>>>>" + searchResultsUser);
				return SUCCESS;
			} else {
				session.setAttribute("companyList", new ArrayList());
				System.out.println("no companyList === in else");
				return SUCCESS;
			}
		} catch (Exception e) {
			throw new LMSException();

		}
	}

	private void test(HttpSession session) {
		List<ChequeBean> paymentList = (List<ChequeBean>) session
				.getAttribute("CHEQUE_LIST");
		if (paymentList != null) {
			for (int i = 0; i < paymentList.size(); i++) {
				System.out.println("Size of Cheque List" + paymentList.size());
				System.out.println(((ChequeBean) paymentList.get(i))
						.getChequeNumber());
				System.out.println(((ChequeBean) paymentList.get(i))
						.getChequeAmount());
				System.out.println(((ChequeBean) paymentList.get(i))
						.getBankName());

			}
		}
		if (session.getAttribute("CASH") != null) {
			double cash = ((Double) session.getAttribute("CASH")).doubleValue();

			System.out.println("Total remaining cash after removing" + cash);
		}
	}

	private boolean validateCheque(String bank, String chqnbr) throws Exception {
		StringBuffer st = new StringBuffer();
		st.append(bank).append(chqnbr);
		String bnkChq = st.toString();
		 
		Connection conn = null;
		conn = DBConnect.getConnection();
		PreparedStatement statement = null;
		ResultSet rs = null;
		String query = QueryManager.getST5RetailerChequeQuery();
		// String query="select count(*) 'count' from st_lms_agent_sale_chq
		// where drawee_bank='"+bank+"' and chq_nbr="+chqnbr+"";
		statement = conn.prepareStatement(query);
		statement.setString(1, bank);
		statement.setString(2, chqnbr);
		rs = statement.executeQuery();
		int count = 0;
		if (rs.next()) {
			count = rs.getInt("count");
			System.out.println("getFetchSize" + count);
		}
		try {
		} finally {
			if (conn != null) {
				conn.close();
			}
		}

		if (count > 0) {
			return true;
		} else {
			return false;
		}
	}

	private boolean validateChequeForDuplicateEntry(
			List<ChequeBean> chequeList, String chequeNumber, String bankName) {
		for (ChequeBean chequeBean : chequeList) {
			String beanChequeNumber = chequeBean.getChequeNumber();
			String beanBankName = chequeBean.getBankName();
			if (bankName.trim().equalsIgnoreCase(beanBankName.trim())
					&& chequeNumber.trim().equalsIgnoreCase(
							beanChequeNumber.trim())) {
				return true;
			}
		}
		return false;
	}

}
