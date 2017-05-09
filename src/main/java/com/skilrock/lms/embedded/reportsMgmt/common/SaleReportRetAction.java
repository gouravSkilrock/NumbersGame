package com.skilrock.lms.embedded.reportsMgmt.common;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.HashMap;
import java.util.Iterator;
import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletContext;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.beans.DateBeans;
import com.skilrock.lms.beans.SaleReportBean;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.GetDate;
import com.skilrock.lms.common.utility.FormatNumber;
import com.skilrock.lms.coreEngine.drawGames.reportMgmt.DGSaleReportRetHelper;
import com.skilrock.lms.coreEngine.userMgmt.common.CommonFunctionsHelper;
import com.skilrock.lms.embedded.drawGames.common.EmbeddedErrors;

public class SaleReportRetAction extends ActionSupport implements
		ServletRequestAware, ServletResponseAware {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String end_date;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private String start_date;
	private String Type;
	private String userName;

	@Override
	public String execute() {
		request.getSession().setAttribute("stDate",
				new java.sql.Date(new java.util.Date().getTime()));
		return SUCCESS;
	}

	public String getEnd_date() {
		return end_date;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public String getStart_date() {
		return start_date;
	}

	public String getType() {
		return Type;
	}

	public String getUserName() {
		return userName;
	}

	public void saleReportForRet() throws LMSException, IOException {
		DateBeans dateBeans = null;
		ServletContext sc = ServletActionContext.getServletContext();
		HashMap<String, HttpSession> myMap = new HashMap<String, HttpSession>();
		myMap = (HashMap<String, HttpSession>) sc
				.getAttribute("LOGGED_IN_USERS");
		if (myMap == null) {
			response
					.getOutputStream()
					.write(
							("ErrorMsg:" + EmbeddedErrors.SESSION_EXPIRED + "ErrorCode:01|")
									.getBytes());
			return;
		}
		HttpSession mySession = myMap.get(userName);
		if (!CommonFunctionsHelper.isSessionValid(mySession)) {
			response
					.getOutputStream()
					.write(
							("ErrorMsg:" + EmbeddedErrors.SESSION_EXPIRED + "ErrorCode:01|")
									.getBytes());
			return;
		}
		UserInfoBean userInfoBean = (UserInfoBean) mySession
				.getAttribute("USER_INFO");

		if ("Date Wise".equalsIgnoreCase(URLDecoder.decode(Type, "UTF-8"))) {
			dateBeans = GetDate.getDate(start_date, end_date);
		} else {
			dateBeans = GetDate.getDate(URLDecoder.decode(Type, "UTF-8"));
		}
		DGSaleReportRetHelper helper = new DGSaleReportRetHelper(userInfoBean,
				dateBeans);
		List<SaleReportBean> salelist = helper.getDGSaleDetailGameWise();
		Iterator<SaleReportBean> it = salelist.iterator();
		Calendar cd = Calendar.getInstance();
		cd.setTime(new java.util.Date());
		String finalData = "";
		String hour = cd.get(Calendar.HOUR_OF_DAY) + "";
		String min = cd.get(Calendar.MINUTE) + "";
		String sec = cd.get(Calendar.SECOND) + "";
		if (hour.length() <= 1) {
			hour = "0" + hour;
		}
		if (min.length() <= 1) {
			min = "0" + min;
		}
		if (sec.length() <= 1) {
			sec = "0" + sec;
		}
		finalData = "Date:"
				+ new java.sql.Date((new java.util.Date()).getTime())
						.toString() + "|Time:" + hour + ":" + min + ":" + sec;
		Double totalMRPSale = 0.0;
		Double totalNetSale = 0.0;
		Double totalProfit = 0.0;
		while (it.hasNext()) {
			SaleReportBean saleReportBean = it.next();
			finalData += "|Game Type:"
					+ saleReportBean.getGamename()
					+ ",MRPSale:"
					+ saleReportBean.getNetMrpAmt()
					+ ",NetSale:"
					+ saleReportBean.getNetAmt()
					+ ",Profit:"
					+ FormatNumber.formatNumber(Double
							.parseDouble(saleReportBean.getNetMrpAmt())
							- Double.parseDouble(saleReportBean.getNetAmt()));
			totalMRPSale += Double.parseDouble(saleReportBean.getNetMrpAmt());
			totalNetSale += Double.parseDouble(saleReportBean.getNetAmt());
			totalProfit += Double.parseDouble(saleReportBean.getNetMrpAmt())
					- Double.parseDouble(saleReportBean.getNetAmt());
		}
		finalData += "|Total:MRPSale:" + totalMRPSale + ",NetSale:"
				+ totalNetSale + ",Profit:"
				+ FormatNumber.formatNumber(totalProfit) + "|";
		response.getOutputStream().write(finalData.getBytes());
		return;
	}

	public void setEnd_date(String end_date) {
		/*
		 * if(end_date == null){ this.end_date=new java.sql.Date(new
		 * java.util.Date().getTime()).toString(); }
		 */
		this.end_date = end_date;
	}

	public void setServletRequest(HttpServletRequest req) {
		request = req;
	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;

	}

	public void setStart_date(String start_date) {
		/*
		 * if(start_date == null){ this.start_date=new java.sql.Date(new
		 * java.util.Date().getTime()).toString(); }
		 */
		this.start_date = start_date;
	}

	public void setType(String Type) {
		this.Type = Type;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}
