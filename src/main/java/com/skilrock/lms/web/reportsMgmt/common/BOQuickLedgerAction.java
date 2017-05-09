package com.skilrock.lms.web.reportsMgmt.common;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.DailyLedgerBean;
import com.skilrock.lms.beans.DateBeans;
import com.skilrock.lms.beans.OrgBean;
import com.skilrock.lms.beans.ReportStatusBean;
import com.skilrock.lms.beans.ServicesBean;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.FormatNumber;
import com.skilrock.lms.common.utility.LMSUtility;
import com.skilrock.lms.coreEngine.reportsMgmt.common.RetDailyLedgerHelper;
import com.skilrock.lms.coreEngine.userMgmt.common.CommonFunctionsHelper;
import com.skilrock.lms.embedded.drawGames.common.EmbeddedErrors;

public class BOQuickLedgerAction extends ActionSupport implements
		ServletRequestAware, ServletResponseAware {
	private static Log logger = LogFactory.getLog(BOQuickLedgerAction.class);
	private static final long serialVersionUID = 1L;
	private int agentOrgId;
	private String end_Date;
	private String reportType;
	private String ledgerType;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private int retOrgId;
	private String start_date;
	private String fromHour;
	private String fromMin;
	private String fromSec;
	private String toHour;
	private String toMin;
	private String toSec;

	@Override
	public String execute() throws Exception {
		logger.info("-------BO Quick Ledger----------");
		/*
		 * HttpSession session = request.getSession(); List<OrgBean> agentList =
		 * new CommonFunctionsHelper().getAgentOrgList(); Map<Integer, String>
		 * agentMap = new HashMap<Integer, String>(); OrgBean orgBean = null;
		 * for (int i = 0; i < agentList.size(); i++) { orgBean =
		 * agentList.get(i); agentMap.put(orgBean.getOrgId(),
		 * orgBean.getOrgName()); }
		 * 
		 * session.setAttribute("agentMap", agentMap);
		 */
		return SUCCESS;
	}

	public String fetchQuickLedger() throws LMSException {
		logger.info("---fetchQuickLedger--");
		ServletContext sc = ServletActionContext.getServletContext();
		HttpSession session = request.getSession();
		String dateFormat = (String) sc.getAttribute("date_format");
		String deploy_Date = (String) sc.getAttribute("DEPLOYMENT_DATE");
		Calendar fromDrawCal = Calendar.getInstance();
		Calendar toDrawCal = Calendar.getInstance();
		boolean isScratch = false;
		boolean isDraw = false;
		if (((String) sc.getAttribute("IS_SCRATCH")).equalsIgnoreCase("yes")) {
			isScratch = true;
		}
		if (((String) sc.getAttribute("IS_DRAW")).equalsIgnoreCase("yes")) {
			isDraw = true;
		}
		boolean isCS = "YES".equalsIgnoreCase((String) LMSUtility.sc
				.getAttribute("IS_CS"));
		String format = "yyyy/mm/dd";
		Date frdate = null;
		Date todateObj = null;
		DateBeans dateBeans = null;
		Timestamp startDate = null;
		Timestamp endDate = null;
		Date deployDate = null;
		try {
			// startDate = new Timestamp((new SimpleDateFormat(format)).parse(
			// start_date).getTime());
			// endDate = new Timestamp((new SimpleDateFormat(format)).parse(
			// start_date).getTime()
			// + 24 * 60 * 60 * 1000 - 1000);
			deployDate = new Date((new SimpleDateFormat(dateFormat)).parse(
					deploy_Date).getTime());
			if (fromHour.length() == 1) {
				fromHour = "0" + fromHour;
			}
			if (fromMin.length() == 1) {
				fromMin = "0" + fromMin;
			}
			if (fromSec.length() == 1) {
				fromSec = "0" + fromSec;
			}
			if (toHour.length() == 1) {
				toHour = "0" + toHour;
			}
			if (toMin.length() == 1) {
				toMin = "0" + toMin;
			}
			if (toSec.length() == 1) {
				toSec = "0" + toSec;
			}

			try {
				if (start_date != null) {
					String[] arr = start_date.split("-");
					fromDrawCal.set(Integer.parseInt(arr[0]), Integer
							.parseInt(arr[1]) - 1, Integer.parseInt(arr[2]));
					fromDrawCal.set(Calendar.HOUR_OF_DAY, Integer
							.parseInt(fromHour));
					fromDrawCal.set(Calendar.MINUTE, Integer.parseInt(fromMin));
					fromDrawCal.set(Calendar.SECOND, Integer.parseInt(fromSec));
					startDate = new Timestamp(fromDrawCal.getTime().getTime());
				}
				if (start_date != null) {
					String[] arr = start_date.split("-");
					toDrawCal.set(Integer.parseInt(arr[0]), Integer
							.parseInt(arr[1]) - 1, Integer.parseInt(arr[2]));
					toDrawCal.set(Calendar.HOUR_OF_DAY, Integer
							.parseInt(toHour));
					toDrawCal.set(Calendar.MINUTE, Integer.parseInt(toMin));
					toDrawCal.set(Calendar.SECOND, Integer.parseInt(toSec));
					endDate = new Timestamp(toDrawCal.getTime().getTime());
				}
			} catch (Exception e) {
				throw new LMSException("Date Format Error");

			}
			dateBeans = new DateBeans();
			dateBeans.setStartTime(startDate);
			dateBeans.setEndTime(endDate);
			dateBeans.setReportType(ledgerType);

			System.out.println(reportType);

			if ("transaction report".equalsIgnoreCase(reportType)) {

				String status = transactionReportAction(dateBeans, retOrgId);
				if ("success".equalsIgnoreCase(status)) {
					return "transactionReport";
				}
			}
			DailyLedgerBean CRBTemp = new DailyLedgerBean();
			DailyLedgerBean CRB = new DailyLedgerBean();
			DateBeans dateBean = new DateBeans();
			RetDailyLedgerHelper helper = new RetDailyLedgerHelper();
			String retOrgName = helper.getRetName(retOrgId);
			dateBean.setStartTime(new Timestamp(deployDate.getTime()));
			dateBean.setEndTime(startDate);
			CRBTemp = helper.getRetLegderDetail(dateBean, isScratch, isDraw,
					isCS, retOrgId);
			dateBean.setStartTime(startDate);
			dateBean.setEndTime(endDate);
			CRB = helper.getRetLegderDetail(dateBean, isScratch, isDraw, isCS,
					retOrgId);
			CRB.setOpenBal(CRBTemp.getClrBal());
			CRB.setClrBal(FormatNumber.formatNumber(Double.parseDouble(CRB
					.getOpenBal())
					+ Double.parseDouble(CRB.getClrBal())));

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
			session.setAttribute("ledgerType", ledgerType);
			session.setAttribute("retName", retOrgName);
			session.setAttribute("todayDate", new java.sql.Date(
					(new java.util.Date()).getTime()).toString());
			session.setAttribute("reportDate", start_date);

			session.setAttribute("fromTime", fromHour + ":" + fromMin + ":"
					+ fromSec);
			session.setAttribute("toTime", toHour + ":" + toMin + ":" + toSec);
			session.setAttribute("openBal", CRB.getOpenBal());
			session.setAttribute("purchase", CRB.getPurchase());
			session.setAttribute("netsale", CRB.getNetsale());
			session.setAttribute("netPwt", CRB.getNetPwt());
			session.setAttribute("netPayment", CRB.getNetPayment());
			session.setAttribute("clrBal", CRB.getClrBal());
			session.setAttribute("cashCol", CRB.getCashCol());
			session.setAttribute("scratchSale", CRB.getScratchSale());
			session.setAttribute("profit", CRB.getProfit());
			session.setAttribute("netSaleCS", CRB.getNetSaleCS());
			session.setAttribute("netSaleSLE", CRB.getSleSale());
			session.setAttribute("netPwtSLE", CRB.getSlePwt());
			session.setAttribute("netSaleIW", CRB.getIwSale());
			session.setAttribute("netPwtIW", CRB.getIwPwt());
			session.setAttribute("netSaleVS", CRB.getVsSale());
			session.setAttribute("netPwtVS", CRB.getVsPwt());

		} catch (ParseException e) {
			throw new LMSException("Date Format Error");
		}
		return SUCCESS;
	}

	public int getAgentOrgId() {
		return agentOrgId;
	}

	public String getEnd_Date() {
		return end_Date;
	}

	public String getReportType() {
		return reportType;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public String transactionReportAction(DateBeans dateBeans, int retOrgId) {
		HttpSession session = request.getSession();
		Map<String, List<Map<String, Double>>> dataMap = new HashMap<String, List<Map<String, Double>>>();
		RetDailyLedgerHelper helper = new RetDailyLedgerHelper();
		// dataList = helper.retDailyLedgerGameWise(dateBeans, retOrgId);
		try {

			ReportStatusBean reportStatusBean = ReportUtility.getReportStatus(ActionContext.getContext().getName());
			if ("FAILURE".equals(reportStatusBean.getReportStatus())) {
				response.getOutputStream().write(("ErrorMsg:No Reporting Till " + reportStatusBean.getEndTime() + "|ErrorCode:" + EmbeddedErrors.REPORTING_TIME_ERROR_CODE).getBytes());
				return null;
			}
			dataMap = RetDailyLedgerHelper
					.retDailyLedgerGameWiseTerminal(dateBeans, retOrgId,reportStatusBean);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		System.out.println("Response For transaction Report::" + dataMap);
		String retOrgName = helper.getRetName(retOrgId);
		Map<String, String> gameDeatilMap = new HashMap<String, String>();
		int i = 0;
		if (dataMap.size() != 0) {
			String gameName = "";
			Double totalMrpSale = 0.0;
			Double totalMrpPwt = 0.0;
			Double totalNetSale = 0.0;
			Double totalNetPwt = 0.0;

			if (ServicesBean.isDG()) {
				List<Map<String, Double>> dgList = dataMap.get("DG");
				int dgLen = dgList.size();
				while (i < dgLen) {

					Set<String> keySet = dgList.get(i).keySet();
					Iterator<String> itr = keySet.iterator();

					while (itr.hasNext()) {
						String gName = itr.next();
						if (gName.indexOf("name") != -1) {
							gameName = gName.substring(4, gName.length());
						}
					}
					String SaleString = "";
					if (dgList.get(i).get("mrpSaleSlot") != null) {
						SaleString = FormatNumber.formatNumber((dgList.get(i)
								.get("mrpSale") - dgList.get(i).get(
								"mrpSaleSlot")))
								+ ":"
								+ FormatNumber.formatNumber(dgList.get(i).get(
										"mrpPwt"))
								+ ":"
								+ FormatNumber.formatNumber(dgList.get(i).get(
										"mrpSaleSlot"));
					} else {
						SaleString = FormatNumber.formatNumber(dgList.get(i)
								.get("mrpSale"))
								+ ":"
								+ FormatNumber.formatNumber(dgList.get(i).get(
										"mrpPwt"));
					}
					gameDeatilMap.put(gameName, SaleString);

					totalMrpSale += dgList.get(i).get("mrpSale");
					totalNetSale += dgList.get(i).get("netSale");
					totalMrpPwt += dgList.get(i).get("mrpPwt");
					totalNetPwt += dgList.get(i).get("netPwt");
					i++;
				}

			}

			int iSle = 0;
			if (ServicesBean.isSLE()) {
				List<Map<String, Double>> sleList = dataMap.get("SLE");
				int sleLen = sleList.size();
				while (iSle < sleLen) {

					Set<String> keySet = sleList.get(iSle).keySet();
					Iterator<String> itr = keySet.iterator();
					while (itr.hasNext()) {
						String gName = itr.next();
						if (gName.indexOf("name") != -1) {
							gameName = gName.substring(4, gName.length());
						}
					}

					String SaleString = FormatNumber.formatNumber(sleList.get(
							iSle).get("mrpSale"))
							+ ":"
							+ FormatNumber.formatNumber(sleList.get(iSle).get(
									"mrpPwt"));

					gameDeatilMap.put(gameName, SaleString);

					totalMrpSale += sleList.get(iSle).get("mrpSale");
					totalNetSale += sleList.get(iSle).get("netSale");
					totalMrpPwt += sleList.get(iSle).get("mrpPwt");
					totalNetPwt += sleList.get(iSle).get("netPwt");

					iSle++;
				}
			}
			int iIw = 0;
			if (ServicesBean.isIW()) {
				List<Map<String, Double>> iwList = dataMap.get("IW");
				int iwLen = iwList.size();
				while (iIw < iwLen) {

					Set<String> keySet = iwList.get(iIw).keySet();
					Iterator<String> itr = keySet.iterator();
					while (itr.hasNext()) {
						String gName = itr.next();
						if (gName.indexOf("name") != -1) {
							gameName = gName.substring(4, gName.length());
						}
					}

					String SaleString = FormatNumber.formatNumber(iwList.get(
							iIw).get("mrpSale"))
							+ ":"
							+ FormatNumber.formatNumber(iwList.get(iIw).get(
									"mrpPwt"));

					gameDeatilMap.put(gameName, SaleString);

					totalMrpSale += iwList.get(iIw).get("mrpSale");
					totalNetSale += iwList.get(iIw).get("netSale");
					totalMrpPwt += iwList.get(iIw).get("mrpPwt");
					totalNetPwt += iwList.get(iIw).get("netPwt");

					iIw++;
				}
			}
			
			int iVs = 0;
			if (ServicesBean.isVS()) {
				List<Map<String, Double>> vsList = dataMap.get("VS");
				int vsLen = vsList.size();
				while (iVs < vsLen) {

					Set<String> keySet = vsList.get(iVs).keySet();
					Iterator<String> itr = keySet.iterator();
					while (itr.hasNext()) {
						String gName = itr.next();
						if (gName.indexOf("name") != -1) {
							gameName = gName.substring(4, gName.length());
						}
					}

					String SaleString = FormatNumber.formatNumber(vsList.get(iVs).get("mrpSale")) + ":" + FormatNumber.formatNumber(vsList.get(iVs).get("mrpPwt"));

					gameDeatilMap.put(gameName, SaleString);

					totalMrpSale += vsList.get(iVs).get("mrpSale");
					totalNetSale += vsList.get(iVs).get("netSale");
					totalMrpPwt += vsList.get(iVs).get("mrpPwt");
					totalNetPwt += vsList.get(iVs).get("netPwt");

					iVs++;
				}
			}

			// Added code for adding Scratch winning data in transaction report
			// in terminal...
			if (ServicesBean.isSE()) {
				List<Map<String, Double>> seList = dataMap.get("SE");
				int seLen = seList.size();
				session.setAttribute("ScratchPWT", FormatNumber
						.formatNumber(seList.get(seList.size() - 1).get(
								"scratchMrpPwt")));

				totalMrpPwt += seList.get(seLen - 1).get("scratchMrpPwt");
				totalNetPwt += seList.get(seLen - 1).get("scratchNetPwt");
			}
			// Added code for adding Scratch data ends here...

			System.out.println("gameDeatilMap::" + gameDeatilMap);
			session.setAttribute("gameDetailMap", gameDeatilMap);
			session.setAttribute("reportDate", start_date);
			session.setAttribute("fromTime", fromHour + ":" + fromMin + ":"
					+ fromSec);
			session.setAttribute("toTime", toHour + ":" + toMin + ":" + toSec);
			session.setAttribute("retName", retOrgName);
			session.setAttribute("totalSale", FormatNumber
					.formatNumber(totalMrpSale));
			session.setAttribute("totalPwt", FormatNumber
					.formatNumber(totalMrpPwt));
			session.setAttribute("cashInHand", FormatNumber
					.formatNumber(totalMrpSale - totalMrpPwt));
			session.setAttribute("PTPA", FormatNumber.formatNumber(totalNetSale
					- totalNetPwt));
			session.setAttribute("Profit", FormatNumber
					.formatNumber(totalMrpSale - totalMrpPwt
							- (totalNetSale - totalNetPwt)));

		}

		return "success";

	}

	public void getRetailerOrgList() throws IOException {
		PrintWriter out = getResponse().getWriter();
		List<OrgBean> retList = new CommonFunctionsHelper()
				.getRetailerOrgList(agentOrgId + "");
		logger.info("---Retailer List--" + retList);
		StringBuilder retStr = new StringBuilder("");
		OrgBean orgBean = null;
		for (int i = 0; i < retList.size(); i++) {
			orgBean = retList.get(i);
			retStr
					.append(orgBean.getOrgId() + ":" + orgBean.getOrgName()
							+ ",");
		}
		logger.info("---Retailer List--" + retStr);
		out.print(retStr);
	}

	public int getRetOrgId() {
		return retOrgId;
	}

	public String getStart_date() {
		return start_date;
	}

	public void setAgentOrgId(int agentOrgId) {
		this.agentOrgId = agentOrgId;
	}

	public void setEnd_Date(String end_Date) {
		this.end_Date = end_Date;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	public void setRetOrgId(int retOrgId) {
		this.retOrgId = retOrgId;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}

	public void setStart_date(String start_date) {
		this.start_date = start_date;
	}

	public String getFromHour() {
		return fromHour;
	}

	public void setFromHour(String fromHour) {
		this.fromHour = fromHour;
	}

	public String getFromMin() {
		return fromMin;
	}

	public void setFromMin(String fromMin) {
		this.fromMin = fromMin;
	}

	public String getFromSec() {
		return fromSec;
	}

	public void setFromSec(String fromSec) {
		this.fromSec = fromSec;
	}

	public String getToHour() {
		return toHour;
	}

	public void setToHour(String toHour) {
		this.toHour = toHour;
	}

	public String getToMin() {
		return toMin;
	}

	public void setToMin(String toMin) {
		this.toMin = toMin;
	}

	public String getToSec() {
		return toSec;
	}

	public void setToSec(String toSec) {
		this.toSec = toSec;
	}

	public String getLedgerType() {
		return ledgerType;
	}

	public void setLedgerType(String ledgerType) {
		this.ledgerType = ledgerType;
	}

}
