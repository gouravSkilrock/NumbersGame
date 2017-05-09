package com.skilrock.lms.embedded.reportsMgmt.common;

import java.net.URLDecoder;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.DateBeans;
import com.skilrock.lms.beans.PwtReportBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.GetDate;
import com.skilrock.lms.coreEngine.drawGames.reportMgmt.DGPwtReportRetHelper;
import com.skilrock.lms.coreEngine.userMgmt.common.CommonFunctionsHelper;
import com.skilrock.lms.embedded.drawGames.common.EmbeddedErrors;

public class PWTReportForRetAction extends ActionSupport implements
		ServletResponseAware, ServletRequestAware {

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

	public void pwtReportForRet() throws LMSException, Exception {
		DateBeans dateBean = new DateBeans();
		List<PwtReportBean> list = null;
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

		System.out.println("Type:" + Type);

		if ("Date Wise".equalsIgnoreCase(URLDecoder.decode(Type, "UTF-8"))) {
			dateBean = GetDate.getDate(start_date, end_date);
		} else {
			dateBean = GetDate.getDate(URLDecoder.decode(Type, "UTF-8"));
		}
		DGPwtReportRetHelper dgHelper = new DGPwtReportRetHelper(userInfoBean,
				dateBean);
		list = dgHelper.getDGPwtDetail();
		String finalData = "";
		Calendar cd = Calendar.getInstance();
		cd.setTime(new java.util.Date());
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
		Iterator<PwtReportBean> it = list.iterator();
		PwtReportBean pwtReportBean = new PwtReportBean();
		Double pwtClmTotMrp = 0.0;
		Double pwtClmTotSale = 0.0;
		Double pwtClmTotProf = 0.0;
		/*
		 * Double pwtUnclmTotMrp = 0.0; Double pwtUnclmTotSale = 0.0; Double
		 * pwtUnclmTotProf = 0.0;
		 */
		// |GameName:Fortune,ClaimedMRP:123.00,ClaimedNet:123.00,ClaimedProfit:123.00,UnClaimedMRP:123.00,UnClaimedNet:123.00,UnClaimedProfit:123.00|
		while (it.hasNext()) {
			pwtReportBean = it.next();
			// finalData += "|Game Type:" + pwtReportBean.getGameName() +
			// ",ClaimedMRP:"+ pwtReportBean.getClmMrp() + ",ClaimedNet:" +
			// pwtReportBean.getClmNet() + ",ClaimedProfit:" +
			// pwtReportBean.getClmProfit() + ",UnclaimedMRP:" +
			// pwtReportBean.getUnclmMrp() + ",UnclaimedNet:" +
			// pwtReportBean.getUnclmNet() + ",UnclaimedProfit:" +
			// pwtReportBean.getUnclmProfit();
			finalData += "|Game Type:" + pwtReportBean.getGameName()
					+ ",ClaimedMRP:" + pwtReportBean.getClmMrp()
					+ ",ClaimedNet:" + pwtReportBean.getClmNet()
					+ ",ClaimedProfit:" + pwtReportBean.getClmProfit();
			pwtClmTotMrp += Double.parseDouble(pwtReportBean.getClmMrp());
			pwtClmTotSale += Double.parseDouble(pwtReportBean.getClmNet());
			pwtClmTotProf += Double.parseDouble(pwtReportBean.getClmProfit());
			/*
			 * pwtUnclmTotMrp +=
			 * Double.parseDouble(pwtReportBean.getUnclmMrp()); pwtUnclmTotSale +=
			 * Double.parseDouble(pwtReportBean.getUnclmNet()); pwtUnclmTotProf +=
			 * Double.parseDouble(pwtReportBean.getUnclmProfit());
			 */
		}
		// finalData += "|Total:ClaimedMRP:" + pwtClmTotMrp + ",ClaimedNet:" +
		// pwtClmTotSale + ",ClaimedProfit:" + pwtClmTotProf + ",UnclaimedMRP:"
		// + pwtUnclmTotMrp + ",UnclaimedNet:" + pwtUnclmTotSale +
		// ",UnclaimedProfit:" + pwtUnclmTotProf+"|";
		finalData += "|Total:ClaimedMRP:" + pwtClmTotMrp + ",ClaimedNet:"
				+ pwtClmTotSale + ",ClaimedProfit:" + pwtClmTotProf + "|";
		response.getOutputStream().write(finalData.toString().getBytes());
	}

	public void setEnd_date(String end_date) {
		System.out.println("end date called" + end_date);
		this.end_date = end_date;

	}

	public void setServletRequest(HttpServletRequest req) {
		request = req;

	}

	public void setServletResponse(HttpServletResponse res) {
		response = res;

	}

	public void setStart_date(String start_date) {
		System.out.println("first date called" + start_date);
		this.start_date = start_date;
	}

	public void setType(String type) {
		Type = type;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}
