package com.skilrock.lms.web.drawGames.reportsMgmt.common;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.reportsMgmt.controllerImpl.UssdSubscriberWiseReportControllerImpl;
import com.skilrock.lms.dge.beans.UssdSubscriberDataBean;
import com.skilrock.lms.web.drawGames.common.Util;
import com.skilrock.lms.web.reportsMgmt.common.ReportUtility;

public class UssdSubscriberWiseResultAction extends ActionSupport implements
		ServletRequestAware, ServletResponseAware {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private HttpServletRequest request;
	private HttpServletResponse response;

	private Map<Integer, String> reportTypeMap;
	private String mobileType;
	private String mobileNbr;
	private String startDate;
	private int gameNo;
	private String drawName;
	private String winningStatus;
	private String endDate;
	private String reportData;
	private HashMap<Integer, String> mtnGameMap;
	private List<UssdSubscriberDataBean> mtnCustomerCenterBeans;
	private String message;

	public String getMobileType() {
		return mobileType;
	}

	public void setMobileType(String mobileType) {
		this.mobileType = mobileType;
	}
	
	public String getMobileNbr() {
		return mobileNbr;
	}

	public void setMobileNbr(String mobileNbr) {
		this.mobileNbr = mobileNbr;
	}

	public String getDrawName() {
		return drawName;
	}

	public void setDrawName(String drawName) {
		this.drawName = drawName;
	}

	public int getGameNo() {
		return gameNo;
	}

	public void setGameNo(int gameNo) {
		this.gameNo = gameNo;
	}

	public String getWinningStatus() {
		return winningStatus;
	}

	public void setWinningStatus(String winningStatus) {
		this.winningStatus = winningStatus;
	}

	public HashMap<Integer, String> getMtnGameMap() {
		return mtnGameMap;
	}

	public void setMtnGameMap(HashMap<Integer, String> mtnGameMap) {
		this.mtnGameMap = mtnGameMap;
	}


	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	@Override
	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public Map<Integer, String> getReportTypeMap() {
		return reportTypeMap;
	}

	public void setReportTypeMap(Map<Integer, String> reportTypeMap) {
		this.reportTypeMap = reportTypeMap;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public List<UssdSubscriberDataBean> getMtnCustomerCenterBeans() {
		return mtnCustomerCenterBeans;
	}

	public void setMtnCustomerCenterBeans(
			List<UssdSubscriberDataBean> mtnCustomerCenterBeans) {
		this.mtnCustomerCenterBeans = mtnCustomerCenterBeans;
	}

	public String getReportData() {
		return reportData;
	}

	public void setReportData(String reportData) {
		this.reportData = reportData;
	}

	

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String fetchMtnTxnsMenu() {
	
			HashMap<Integer, String> gameMap = null;
			try {
				mtnGameMap=new HashMap<Integer, String>();
				gameMap = ReportUtility.fetchDrawDataMenu();
				for (Map.Entry<Integer, String> entry : gameMap.entrySet()) {
					String gameName = entry.getValue();
					if(Util.getGameId("KenoFour") == entry.getKey() || Util.getGameId("KenoFive") == entry.getKey() || Util.getGameId("OneToTwelve") == entry.getKey())
						mtnGameMap.put(entry.getKey(), gameName);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			return SUCCESS;
		}
	

	public String fetchUssdSubscriberData() {
		try {
			if("NUMBER".equalsIgnoreCase(mobileType))
				mobileType=mobileNbr;
			try{
			mtnCustomerCenterBeans =new UssdSubscriberWiseReportControllerImpl().fetchUssdSubscriberData("MTN", mobileType, startDate+" 00:00:00", endDate+" 23:59:59",gameNo,drawName,winningStatus);
			} catch (LMSException e) {
				message = e.getErrorMessage();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	

	public void exportToExcel() throws IOException {
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition", "attachment; filename=Ussd_Subscriber_Report.xls");
		PrintWriter out = response.getWriter();
		if (reportData != null) {
			reportData = reportData.replaceAll("<tbody>", "").replaceAll("</tbody>", "").trim();
			reportData =reportData.replaceAll("<br>", "").replaceAll("</br>", "").trim();
			reportData =reportData.replaceAll("</div>", "</div></br>").trim();
			out.write(reportData);
		}
		out.flush();
		out.close();
	}

}
