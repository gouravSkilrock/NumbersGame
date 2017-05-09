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

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.ChequeBean;
import com.skilrock.lms.beans.PaymentBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryHelper;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;

/**
 * This class is used to process payment from Agent Agent
 * 
 * @author Skilrock Technologies
 * 
 */
public class AgentPayment extends ActionSupport implements ServletRequestAware{

	private static final long serialVersionUID = 1271130427666936592L;
	
	
	private HttpServletRequest request;
	
	

	/**
	 * This method is used to take and process cheque entry. *
	 * 
	 * @return
	 * @throws Exception
	 */


	/*public String getBankName() {
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

	public String getContextDateFormat() {
		return contextDateFormat;
	}

	public String getIssuePartyname() {
		return issuePartyname;
	}*/

	/*public String getOrgName() {
		return orgName;
	}

	public String getOrgType() {
		return orgType;
	}

	public String getPaymentType() {
		return paymentType;
	}*/

	public HttpServletRequest getRequest() {
		return request;
	}
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	/*public int getSNo() {
		return sNo;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public String getUserName() {
		return userName;
	}

	/**
	 * This method is used t remove the payment from the cart.
	 * 
	 * @return String
	 * @throws LMSException
	 */
	/*public String RemovePayEntity() throws LMSException {

		HttpSession session = null;
		List<PaymentBean> paymetList = null;
		session = getRequest().getSession();
		System.out.println("Session :" + session);
		PaymentBean paymentBean = null;
		List cash = null;
		// If Payment list is not null means there might already payment be
		// added in the cart.
		if (session.getAttribute("PAYMENT_LIST") != null) {
			paymetList = (List) session.getAttribute("PAYMENT_LIST");

			for (int i = 0; i < paymetList.size(); i++) {

				paymentBean = (PaymentBean) paymetList.get(i);
				System.out.println("removing processing... "
						+ paymentBean.getSNo());
				System.out.println("removing entity Sno is... " + sNo);
				System.out.println("removing entity ampunt is... "
						+ totalAmount);
				System.out.println("removing entity from bean ampunt is... "
						+ paymentBean.getAmount());
				System.out.println("removing entity pay type is... "
						+ paymentType.trim());
				System.out.println("removing entity pay type from bean is... "
						+ paymentBean.getPayType().trim());
				// Here variable sNo ,which was generated on jsp came through
				// parameter and that entry is searched from the payment list
				// and removed.
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
					// check if payment type is Cheque.
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
		// /just for display purpose
		// test(session);

		return SUCCESS;

	}*/

	/*public void setBankName(String bankName) {
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

	public void setContextDateFormat(String contextDateFormat) {
		this.contextDateFormat = contextDateFormat;
	}

	public void setIssuePartyname(String issuePartyname) {
		this.issuePartyname = issuePartyname;
	}*/

	/*public void setOrgName(String orgName) {
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
	 * This method is used to process payment from Agent. In this method Agent
	 * list is fetched and set in a list with is used on the cash and cheque
	 * submit page.
	 * 
	 * @return SUCCESS
	 * @throws LMSException
	 */
	public String start() throws LMSException {

		HttpSession session = null;
		String isCREnable = "INACTIVE";
		session = getRequest().getSession();
		ServletContext sc = ServletActionContext.getServletContext();
		UserInfoBean userBean = (UserInfoBean) session.getAttribute("USER_INFO");
		//contextDateFormat = (String) sc.getAttribute("date_format");
		//System.out.println("date format from the Contex" + contextDateFormat);
		//session.setAttribute("DISPLAY", "Yes");
		//session.setAttribute("orgNameList", null);
		//session.setAttribute("userNameList", null);
		//session.setAttribute("orgNameIdMap", null);
		//System.out.println("session" + session);

		//session.setAttribute("CHEQUE_END_DATE_START", null);
		//session.setAttribute("CHEQUE_START_DATE_START", null);
		/*session.setAttribute("ORG_TYPE", null);
		session.setAttribute("ORG_NAME", null);*/
		//session.setAttribute("COUNT", null);
		//session.setAttribute("PAYMENT_LIST", null);
		//session.setAttribute("CHEQUE_LIST", null);
		//session.setAttribute("CASH", null);
		//System.out.println("session" + session);
		QueryHelper qp = new QueryHelper();
		//List searchResults = qp.searchOrganization();
		//List searchResultsUser = qp.searchUser();
		//Map userNameIdGroup = new HashMap();
		//userNameIdGroup = qp.getUserIdGroup();

		//Map orgNameIdGroup = new HashMap();
		//orgNameIdGroup = qp.getOrgIdGroup();
		//Date currDate = new Date();
		////DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		//Long stLong = currDate.getTime() - 60L * 60L * 24 * 1000 * 30 * 6;
		//Date startDate = new Date(stLong);
		//String strCurrDate = dateFormat.format(currDate);
		//String startDateString = dateFormat.format(startDate);
		isCREnable = (String)sc.getAttribute("IS_CASH_REGISTER");
		if(isCREnable.equalsIgnoreCase("ACTIVE"))
		{
			isCREnable = qp.checkDrawerAvailablity(userBean.getUserId());
		}
		//System.out.println(strCurrDate + "dateeeeeeeee");
		System.out.println(isCREnable + "cash register");
		session.setAttribute("isCashRegister",isCREnable);
		//session.setAttribute("CHEQUE_END_DATE_START", strCurrDate);
		//session.setAttribute("DATE_FORMAT", contextDateFormat);
		//session.setAttribute("CHEQUE_START_DATE_START", startDateString);
	/*	if (searchResultsUser != null && searchResultsUser.size() > 0) {
			///session.setAttribute("orgNameList", searchResults);
			session.setAttribute("userNameList", searchResultsUser);
			session.setAttribute("orgNameIdMap", orgNameIdGroup);
		}*/

		return SUCCESS;
	}
}

	

	/*private void test(HttpSession session) {
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
		String query = QueryManager.getST5AgentChequeQuery();
		// String query="select count(*) 'count' from st_lms_agent_sale_chq
		// where drawee_bank='"+bank+"' and chq_nbr="+chqnbr+"";
		statement = conn.prepareStatement(query);
		statement.setString(1, bank);
		statement.setString(2, chqnbr);
		rs = statement.executeQuery();
		rs.next();
		int count = rs.getInt("count");
		System.out.println("getFetchSize" + count);
		if (count > 0) {
			return true;
		} else {
			return false;
		}

	}
}
*/
