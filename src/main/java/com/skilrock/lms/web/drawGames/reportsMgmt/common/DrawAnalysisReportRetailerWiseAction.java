package com.skilrock.lms.web.drawGames.reportsMgmt.common;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.ReportStatusBean;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.CommonMethods;
import com.skilrock.lms.coreEngine.drawGames.reportMgmt.DrawAnalysisReportRetailerWiseHelper;
import com.skilrock.lms.coreEngine.drawGames.reportMgmt.DrawSaleReportHelper;
import com.skilrock.lms.dge.beans.AnalysisBean;
import com.skilrock.lms.web.drawGames.reportsMgmt.beans.RegionWiseDataBean;
import com.skilrock.lms.web.reportsMgmt.common.ReportUtility;

public class DrawAnalysisReportRetailerWiseAction extends ActionSupport
		implements ServletRequestAware, ServletResponseAware {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	HttpServletRequest request;
	HttpServletResponse response;

	private String gameNo;
	private int gameId;
	private String drawName;
	private String drawTime;
	private String dateOfDraw;
	private String message;
	private String startDate;
	private String endDate;
	private Map<String, RegionWiseDataBean> regionDataMap;
	private String repType;
	private String stateCode;

	public String fetchDrawAnalysisDataRetailerWise() {
		HttpSession session = getRequest().getSession();
		String refMerchantId = (String) session.getServletContext()
				.getAttribute("REF_MERCHANT_ID");
		AnalysisBean anaBean = new AnalysisBean();
		if (dateOfDraw != null) {
			String lastDate = CommonMethods.getLastArchDate();
			System.out.println("last archieve date" + lastDate);
			if (dateOfDraw.compareTo(lastDate) <= 0) {
				message = "For Details Please Choose start date after "
						+ lastDate;
				return SUCCESS;
			}
		}
		anaBean.setGameNo(gameNo);
		anaBean.setDrawName(drawName);
		anaBean.setDrawTime(drawTime);
		anaBean.setStartDate(dateOfDraw);
		anaBean.setMerchantId(refMerchantId);
		DrawAnalysisReportRetailerWiseHelper helper = new DrawAnalysisReportRetailerWiseHelper();
		try {
			List<AnalysisBean> anaBeanList = helper.fetchData(anaBean);
			session
					.setAttribute("DRAW_ANALYSIS_LIST_RETAILERWISE",
							anaBeanList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}

	public String fetchRegionWiseDrawData() {

		HttpSession session = getRequest().getSession();
		String refMerchantId = (String) session.getServletContext()
				.getAttribute("REF_MERCHANT_ID");
		AnalysisBean anaBean = new AnalysisBean();
		if (gameId > 0) {
			anaBean.setGameId(gameId);
			// / anaBean.setDrawTime(drawTime);
			anaBean.setStartDate(startDate + " 00:00:00");
			anaBean.setEndDate(endDate + " 23:59:59");
			anaBean.setMerchantId(refMerchantId);
			// repType="DT";
			// DrawAnalysisReportRetailerWiseHelper helper=new
			// DrawAnalysisReportRetailerWiseHelper();
			String actionName = ActionContext.getContext().getName();
			ReportStatusBean reportStatusBean = ReportUtility
					.getReportStatus(actionName);

			if ("FAILURE".equals(reportStatusBean.getReportStatus())) {
				addActionError(getText("msg.error.msg.no.rep.till")
						+ reportStatusBean.getEndTime());
				return SUCCESS;
			}
			try {
				if (repType.equalsIgnoreCase("DW")) {
					anaBean.setDrawName(drawName);
					regionDataMap = DrawAnalysisReportRetailerWiseHelper
							.fetchRegionWiseDrawData(anaBean, stateCode,
									reportStatusBean);
				} else if (repType.equalsIgnoreCase("DT")) {
					regionDataMap = DrawSaleReportHelper
							.fetchRegionWiseSaleCashData(anaBean, stateCode,
									reportStatusBean);

				}

				/*
				 * Iterator itr = regionDataMap.entrySet().iterator();
				 * while(itr.hasNext()){ Map.Entry<String,RegionWiseDataBean>
				 * entry = (Map.Entry<String,RegionWiseDataBean>)itr.next();
				 * System
				 * .out.println(entry.getKey()+" :"+entry.getValue().getSaleAmt
				 * ());
				 * 
				 * }
				 */
			} catch (LMSException e) {
				addActionError(e.getErrorMessage());
			} catch (Exception e) {

				e.printStackTrace();
			}

		} else {

			addActionError(getText("msg.internal.error.try.again"));

		}

		return SUCCESS;
	}

	public String fetchAreaWiseDrawData() {
		HttpSession session = getRequest().getSession();
		String refMerchantId = (String) session.getServletContext()
				.getAttribute("REF_MERCHANT_ID");
		AnalysisBean anaBean = new AnalysisBean();
		if (gameId > 0) {
			anaBean.setGameId(gameId);
			// / anaBean.setDrawTime(drawTime);
			anaBean.setStartDate(startDate + " 00:00:00");
			anaBean.setEndDate(endDate + " 23:59:59");
			anaBean.setMerchantId(refMerchantId);
			String actionName = ActionContext.getContext().getName();
			ReportStatusBean reportStatusBean = ReportUtility
					.getReportStatus(actionName);

			if ("FAILURE".equals(reportStatusBean.getReportStatus())) {
				addActionError(getText("msg.error.msg.no.rep.till")
						+ reportStatusBean.getEndTime());
				return SUCCESS;
			}
			// repType="DT";
			// DrawAnalysisReportRetailerWiseHelper helper=new
			// DrawAnalysisReportRetailerWiseHelper();
			try {
				if (repType.equalsIgnoreCase("DW")) {
					anaBean.setDrawName(drawName);
					regionDataMap = DrawAnalysisReportRetailerWiseHelper
							.fetchAreaWiseDrawData(anaBean, stateCode,
									reportStatusBean);
				} else if (repType.equalsIgnoreCase("DT")) {
					regionDataMap = DrawSaleReportHelper
							.fetchRegionWiseSaleCashData(anaBean, stateCode,
									reportStatusBean);

				}

				/*
				 * Iterator itr = regionDataMap.entrySet().iterator();
				 * while(itr.hasNext()){ Map.Entry<String,RegionWiseDataBean>
				 * entry = (Map.Entry<String,RegionWiseDataBean>)itr.next();
				 * System
				 * .out.println(entry.getKey()+" :"+entry.getValue().getSaleAmt
				 * ());
				 * 
				 * }
				 */
			} catch (LMSException e) {
				addActionError(e.getErrorMessage());
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {

			addActionError(getText("msg.internal.error.try.again"));
		}

		return SUCCESS;
	}

	@Override
	public String execute() throws Exception {
		HttpSession session = request.getSession();

		session.setAttribute("presentDate", new java.sql.Date(new Date()
				.getTime()).toString());
		session
				.setAttribute("DRAWGAME_LIST", ReportUtility
						.fetchDrawDataMenu());
		return SUCCESS;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	public String getGameNo() {
		return gameNo;
	}

	public void setGameNo(String gameNo) {
		this.gameNo = gameNo;
	}

	public String getDrawName() {
		return drawName;
	}

	public void setDrawName(String drawName) {
		this.drawName = drawName;
	}

	public String getDrawTime() {
		return drawTime;
	}

	public void setDrawTime(String drawTime) {
		this.drawTime = drawTime;
	}

	public String getDateOfDraw() {
		return dateOfDraw;
	}

	public void setDateOfDraw(String dateOfDraw) {
		this.dateOfDraw = dateOfDraw;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
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

	public Map<String, RegionWiseDataBean> getRegionDataMap() {
		return regionDataMap;
	}

	public void setRegionDataMap(Map<String, RegionWiseDataBean> regionDataMap) {
		this.regionDataMap = regionDataMap;
	}

	public String getRepType() {
		return repType;
	}

	public void setRepType(String repType) {
		this.repType = repType;
	}

	public int getGameId() {
		return gameId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public String getStateCode() {
		return stateCode;
	}

	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}
}
