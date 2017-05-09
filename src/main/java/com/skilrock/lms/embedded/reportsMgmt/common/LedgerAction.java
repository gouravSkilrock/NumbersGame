package com.skilrock.lms.embedded.reportsMgmt.common;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.Timestamp;
import java.text.NumberFormat;
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
import com.skilrock.lms.ajax.AjaxRequestHelper;
import com.skilrock.lms.beans.CollectionReportBean;
import com.skilrock.lms.beans.DailyLedgerBean;
import com.skilrock.lms.beans.DateBeans;
import com.skilrock.lms.beans.ReportStatusBean;
import com.skilrock.lms.beans.ServicesBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.Utility;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.FormatNumber;
import com.skilrock.lms.common.utility.GetDate;
import com.skilrock.lms.coreEngine.drawGames.playMgmt.DrawGameRPOSHelper;
import com.skilrock.lms.coreEngine.reportsMgmt.common.RetDailyLedgerHelper;
import com.skilrock.lms.coreEngine.userMgmt.common.CommonFunctionsHelper;
import com.skilrock.lms.embedded.drawGames.common.EmbeddedErrors;
import com.skilrock.lms.web.drawGames.common.Util;
import com.skilrock.lms.web.reportsMgmt.common.ReportUtility;

/**
 * This class is for generation of Agent and Back Office ledger.
 * 
 * @author Skilrock Technologies
 * 
 */
public class LedgerAction extends ActionSupport implements ServletRequestAware,
		ServletResponseAware {
	private static Log logger = LogFactory.getLog(LedgerAction.class);
	private static final long serialVersionUID = 1L;

	SimpleDateFormat dateformat = null;
	private Date frDate;
	private String fromDate;
	private String OrgName;
	String query = null;
	/*
	 * These were the duplicate variable which have been moved to global
	 * variables 07-03-
	 */
	private HttpServletRequest request;
	private HttpServletResponse response;

	HttpSession session = null;

	// LedgerHelper ledgerHelper=new LedgerHelper();

	private Date tDate;
	private String toDate;

	private String type;
	UserInfoBean userBean = null;

	private String userName;
	private long LSTktNo;
	private String isLiveBal;

	public String getIsLiveBal() {
		return isLiveBal;
	}

	public void setIsLiveBal(String isLiveBal) {
		this.isLiveBal = isLiveBal;
	}

	private void generateReport(DailyLedgerBean CRB, boolean isClXcl)
			throws LMSException {

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

		String finalData = "Date:"
				+ new java.sql.Date((new java.util.Date()).getTime())
						.toString() + "|Time:" + hour + ":" + min + ":" + sec;
		// String finalData = "Date:" +
		// EmbeddedUtils.getDisplayDateInFormat("ret_rep_ledger_Generate") +
		// "|Time:" +
		// EmbeddedUtils.getDisplayTimeInFormat("ret_rep_ledger_Generate");
		NumberFormat nFormat = NumberFormat.getInstance();
		nFormat.setMinimumFractionDigits(2);
		String balance = nFormat.format(Double.parseDouble(CRB.getOpenBal()))
				.replace(",", "");
		String NSales = nFormat.format(
				Double.parseDouble(CRB.getNetsale())
						+ Double.parseDouble(CRB.getSleSale())+ Double.parseDouble(CRB.getIwSale())+ Double.parseDouble(CRB.getVsSale()))
				.replace(",", "");
		String NPWT = nFormat.format(
				Double.parseDouble(CRB.getNetPwt())
						+ Double.parseDouble(CRB.getSlePwt())+Double.parseDouble(CRB.getIwPwt())+Double.parseDouble(CRB.getVsPwt())).replace(",", "");
		String NPurchase = nFormat
				.format(Double.parseDouble(CRB.getPurchase())).replace(",", "");
		String NPayment = nFormat.format(
				Double.parseDouble(CRB.getNetPayment())).replace(",", "");
		String ClosingBal = nFormat.format(Double.parseDouble(CRB.getClrBal()))
				.replace(",", "");
		String CashColl = nFormat.format(Double.parseDouble(CRB.getCashCol()))
				.replace(",", "");
		String Profit = nFormat.format(Double.parseDouble(CRB.getProfit()))
				.replace(",", "");
		String Balance = CRB.getBalance();
		String clXclAmount = "";

		if (isClXcl) {
			clXclAmount = "|TransferCredit:"
					+ nFormat.format(Double.parseDouble(CRB.getClXclAmount()))
							.replace(",", "");
		}

		if (ServicesBean.isSE() && "NO".equals(Utility.getPropertyValue("SE_LAST_SOLD_TKT_ENTRY"))) {
			finalData += "|OB:" + balance + clXclAmount + "|NSales:" + NSales
					+ ",NPWT:" + NPWT + ",NPurchase:" + NPurchase
					+ ",NPayment:" + NPayment + ",ClosingBal:" + ClosingBal;
		} else {
			finalData += "|OB:" + balance + clXclAmount + "|NSales:" + NSales
					+ ",NPWT:" + NPWT + ",NPayment:" + NPayment
					+ ",ClosingBal:" + ClosingBal;
		}

		finalData += "|CashColl:" + CashColl + "|Profit:" + Profit
				+ "|Balance:" + Balance + "|retOrg:" + OrgName + "|";
		logger.debug("Opening Balance:-" + CRB.getOpenBal() + " ClXclAmount:-"
				+ clXclAmount + " NetSale:-" + CRB.getNetsale() + " NetPwt:-"
				+ CRB.getNetPwt() + " Payment:-" + CRB.getNetPayment()
				+ " CashCol:-" + CRB.getCashCol() + " profit:-"
				+ CRB.getProfit() + "|Balance:" + CRB.getBalance());
		try {
			response.getOutputStream().write(finalData.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void generateReportGameWise(
			Map<String, List<Map<String, Double>>> dataMap, String date,
			UserInfoBean userBean) throws LMSException {
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

		String finalData = "Date:"
				+ new java.sql.Date((new java.util.Date()).getTime())
						.toString() + "|Time:" + hour + ":" + min + ":" + sec;
		// String finalData = "Date:" +
		// EmbeddedUtils.getDisplayDateInFormat("gameWiseReport") + "|Time:" +
		// EmbeddedUtils.getDisplayTimeInFormat("gameWiseReport");

		finalData += "|ReportDate:" + date;
		int i = 0;
		if (dataMap.size() != 0) {
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
					finalData += "|Game Type:";
					String gameName = null;
					while (itr.hasNext()) {
						String gName = itr.next();
						if (gName.indexOf("name") != -1) {
							gameName = gName.substring(4, gName.length());
							finalData += "GameN:" + gameName + ",";

						}
					}

					Double mrpSaleSlot = dgList.get(i).get("mrpSaleSlot");
					Double netSaleSlot = dgList.get(i).get("netSaleSlot");
					if (mrpSaleSlot != null && netSaleSlot != null) {
						finalData += "GameS:"
								+ FormatNumber.formatNumber(dgList.get(i).get(
										"mrpSale")
										- mrpSaleSlot) + ",";
						finalData += "GameSS:" + gameName + "(Slot Sales)~"
								+ FormatNumber.formatNumber(mrpSaleSlot) + ",";
					} else {
						finalData += "GameS:"
								+ FormatNumber.formatNumber(dgList.get(i).get(
										"mrpSale")) + ",";
					}

					finalData += "GamePWT:"
							+ FormatNumber.formatNumber(dgList.get(i).get(
									"mrpPwt"));
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
					finalData += "|Game Type:";
					String gameName = null;
					while (itr.hasNext()) {
						String gName = itr.next();
						if (gName.indexOf("name") != -1) {
							gameName = gName.substring(4, gName.length());
							finalData += "GameN:" + gameName + ",";

						}
					}

					finalData += "GameS:"
							+ FormatNumber.formatNumber(sleList.get(iSle).get(
									"mrpSale")) + ",";

					finalData += "GamePWT:"
							+ FormatNumber.formatNumber(sleList.get(iSle).get(
									"mrpPwt"));
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
					finalData += "|Game Type:";
					String gameName = null;
					while (itr.hasNext()) {
						String gName = itr.next();
						if (gName.indexOf("name") != -1) {
							gameName = gName.substring(4, gName.length());
							finalData += "GameN:" + gameName + ",";

						}
					}

					finalData += "GameS:"
							+ FormatNumber.formatNumber(iwList.get(iIw).get(
									"mrpSale")) + ",";

					finalData += "GamePWT:"
							+ FormatNumber.formatNumber(iwList.get(iIw).get(
									"mrpPwt"));
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
				int iwLen = vsList.size();
				while (iVs < iwLen) {
					Set<String> keySet = vsList.get(iVs).keySet();
					Iterator<String> itr = keySet.iterator();
					finalData += "|Game Type:";
					String gameName = null;
					while (itr.hasNext()) {
						String gName = itr.next();
						if (gName.indexOf("name") != -1) {
							gameName = gName.substring(4, gName.length());
							finalData += "GameN:" + gameName + ",";
						}
					}

					finalData += "GameS:" + FormatNumber.formatNumber(vsList.get(iVs).get("mrpSale")) + ",";

					finalData += "GamePWT:" + FormatNumber.formatNumber(vsList.get(iIw).get("mrpPwt"));
					totalMrpSale += vsList.get(iIw).get("mrpSale");
					totalNetSale += vsList.get(iIw).get("netSale");
					totalMrpPwt += vsList.get(iIw).get("mrpPwt");
					totalNetPwt += vsList.get(iIw).get("netPwt");
					iVs++;
				}
			}

			// Added code for adding Scratch winning data in transaction report
			// in terminal...
			if (ServicesBean.isSE()) {
				List<Map<String, Double>> seList = dataMap.get("SE");
				int seLen = seList.size();
				finalData += "|ScratchPWT:"
						+ FormatNumber.formatNumber(seList.get(seLen - 1).get(
								"scratchMrpPwt"));

				totalMrpPwt += seList.get(seLen - 1).get("scratchMrpPwt");
				totalNetPwt += seList.get(seLen - 1).get("scratchNetPwt");
			}
			// Added code for adding Scratch data ends here...

			AjaxRequestHelper ajxHelper = new AjaxRequestHelper();
			ajxHelper.getAvlblCreditAmt(userBean);
			double bal = userBean.getAvailableCreditLimit()
					- userBean.getClaimableBal();
			String balance = bal + "00";
			balance = balance.substring(0, balance.indexOf(".") + 3);
			finalData += "|TotalSale:"
					+ FormatNumber.formatNumber(totalMrpSale);
			finalData += "|TotalPWT:" + FormatNumber.formatNumber(totalMrpPwt);
			finalData += "|CashInHand:"
					+ FormatNumber.formatNumber(totalMrpSale - totalMrpPwt);
			finalData += "|PTPA:"
					+ FormatNumber.formatNumber(totalNetSale - totalNetPwt);
			finalData += "|Profit:"
					+ FormatNumber.formatNumber(totalMrpSale - totalMrpPwt
							- (totalNetSale - totalNetPwt));
			finalData += "|Balance:" + Util.getBalInString(bal);
			finalData += "|RetOrg:" + userBean.getOrgName() + "|";
			System.out.println("FINAL GAME WISE REPORT DATA: " + finalData);
		}
		try {
			response.getOutputStream().write(finalData.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void summaryTransactionReport() throws Exception {
		ServletContext sc = ServletActionContext.getServletContext();

		SimpleDateFormat sDF = new SimpleDateFormat(
				(String) sc.getAttribute("date_format"));
		Date deplDate = sDF.parse((String) sc.getAttribute("DEPLOYMENT_DATE"));

		Map currentUserSessionMap = (Map) sc.getAttribute("LOGGED_IN_USERS");
		if (currentUserSessionMap == null) {
			response.getOutputStream()
					.write(("ErrorMsg:" + EmbeddedErrors.SESSION_EXPIRED + "ErrorCode:01|")
							.getBytes());
			return;
		}
		// System.out.println(" LOGGED_IN_USERS maps is " +
		// currentUserSessionMap);

		System.out.println(" user name is " + userName);
		session = (HttpSession) currentUserSessionMap.get(userName);
		if (!CommonFunctionsHelper.isSessionValid(session)) {
			response.getOutputStream()
					.write(("ErrorMsg:" + EmbeddedErrors.SESSION_EXPIRED + "ErrorCode:01|")
							.getBytes());
			return;
		}

		// to cancel last sold ticket if unprinted at terminal end
		userBean = (UserInfoBean) session.getAttribute("USER_INFO");
		Map<Integer, Map<Integer, String>> drawIdTableMap = (Map<Integer, Map<Integer, String>>) sc
				.getAttribute("drawIdTableMap");
		String refMerchantId = (String) sc.getAttribute("REF_MERCHANT_ID");
		int autoCancelHoldDays = Integer.parseInt((String) sc
				.getAttribute("AUTO_CANCEL_CLOSER_DAYS"));
		long lastPrintedTicket = 0;
		int gameId = 0;
		if (LSTktNo != 0) {
			lastPrintedTicket = LSTktNo
					/ Util.getDivValueForLastSoldTkt(String.valueOf(LSTktNo)
							.length());
			gameId = Util.getGameIdFromGameNumber(Util
					.getGamenoFromTktnumber(String.valueOf(LSTktNo)));
		}

		String actionName = ActionContext.getContext().getName();
		DrawGameRPOSHelper drawGameRPOSHelper = new DrawGameRPOSHelper();
		drawGameRPOSHelper.checkLastPrintedTicketStatusAndUpdate(userBean,
				lastPrintedTicket, "TERMINAL", refMerchantId,
				autoCancelHoldDays, actionName, gameId);

		// continue for report generation
		DateBeans dateBeans = null;

		if ("Current Day".equalsIgnoreCase(URLDecoder.decode(type, "UTF-8"))) {
			dateBeans = GetDate.getDate(URLDecoder.decode(type, "UTF-8"));
		} else if ("Last Day"
				.equalsIgnoreCase(URLDecoder.decode(type, "UTF-8"))) {
			dateBeans = new DateBeans();
			dateBeans.setFirstdate(new java.sql.Date(new Date().getTime() - 24
					* 60 * 60 * 1000));
			dateBeans.setLastdate(new java.sql.Date(new Date().getTime()));
		} else if ("Day Before Yesterday".equalsIgnoreCase(URLDecoder.decode(
				type, "UTF-8"))) {
			dateBeans = new DateBeans();
			dateBeans.setFirstdate(new java.sql.Date(new Date().getTime() - 2
					* 24 * 60 * 60 * 1000));
			dateBeans.setLastdate(new java.sql.Date(new Date().getTime() - 24
					* 60 * 60 * 1000));
		} else if ("Date Wise".equalsIgnoreCase(URLDecoder
				.decode(type, "UTF-8"))) {
			dateBeans = new DateBeans();
			dateBeans.setFirstdate(new java.sql.Date(sDF.parse(fromDate)
					.getTime()));
			dateBeans.setLastdate(new java.sql.Date(sDF.parse(toDate).getTime()
					+ 24 * 60 * 60 * 1000));
		}
		userBean = (UserInfoBean) session.getAttribute("USER_INFO");
		frDate = dateBeans.getFirstdate();
		tDate = dateBeans.getLastdate();
		int orgId = userBean.getUserOrgId();
		String retOrgName = userBean.getOrgName();
		RetDailyLedgerHelper helper = new RetDailyLedgerHelper();
		CollectionReportBean bean = helper
				.getSummaryTxnReport(dateBeans, orgId);
		if ("Date Wise".equalsIgnoreCase(URLDecoder.decode(type, "UTF-8"))) {
			dateBeans
					.setLastdate(new java.sql.Date(sDF.parse(toDate).getTime()));
		}
		generateSummerizedTxnReport(bean, dateBeans.getFirstdate().toString()
				.split(" ")[0],
				dateBeans.getLastdate().toString().split(" ")[0], retOrgName);

		// for sports lottery data needs to change later

		/*
		 * GameDataReportControllerImpl controllerImpl = new
		 * GameDataReportControllerImpl(); Map<String,
		 * List<RetGameDataReportBean>> gameDataReportMap = null;
		 * SimpleDateFormat simpleDateFormat = null; Date date = null; try {
		 * simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd"); if
		 * ("Current Day".equalsIgnoreCase(URLDecoder.decode(type, "UTF-8"))) {
		 * date = new Date(); } else if
		 * ("Last Day".equalsIgnoreCase(URLDecoder.decode(type, "UTF-8"))) {
		 * date = new Date(new Date().getTime() - 24*60*60*1000); } else
		 * if("Day Before Yesterday".equalsIgnoreCase(URLDecoder.decode(type,
		 * "UTF-8"))) { date = new Date(new Date().getTime() - 48*60*60*1000);
		 * //date = simpleDateFormat.parse(type); }
		 * 
		 * gameDataReportMap =
		 * controllerImpl.gameDataReportRetailerWise(userBean.getUserOrgId(),
		 * date, date); NumberFormat numberFormat = NumberFormat.getInstance();
		 * numberFormat.setMinimumFractionDigits(2);
		 * 
		 * double totalSaleAmt = 0; double totalPwtAmt = 0; for(String gameName
		 * : gameDataReportMap.keySet()) { List<RetGameDataReportBean>
		 * gameDataReportList = gameDataReportMap.get(gameName);
		 * for(RetGameDataReportBean gameDataReportBean : gameDataReportList) {
		 * double saleAmt = gameDataReportBean.getSaleAmount(); double pwtAmt =
		 * gameDataReportBean.getPwtAmount(); totalSaleAmt += saleAmt;
		 * totalPwtAmt += pwtAmt;
		 * 
		 * } } StringBuilder slData=new StringBuilder();
		 * slData.append("SLSale:");
		 * slData.append(numberFormat.format(totalSaleAmt).replaceAll(",", ""));
		 * slData.append("|SLPayout:");
		 * slData.append(numberFormat.format(totalPwtAmt).replaceAll(",", ""));
		 * slData.append("|SLCol:");
		 * slData.append(numberFormat.format(Double.valueOf
		 * (bean.getGrandTotal())+totalSaleAmt-totalPwtAmt).replaceAll(",",
		 * "")); response.getOutputStream().write(slData.toString().getBytes());
		 * 
		 * 
		 * 
		 * 
		 * } catch (SLEException e) { try {
		 * response.getOutputStream().write(("ErrorMsg:"
		 * +e.getErrorMessage()).getBytes()); } catch (IOException e1) {
		 * e1.printStackTrace(); } return; }
		 */
	}

	private void generateSummerizedTxnReport(CollectionReportBean bean,
			String fdate, String tdate, String retName) {
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

		String finalData = "Date:"
				+ new java.sql.Date((new java.util.Date()).getTime())
						.toString() + "|Time:" + hour + ":" + min + ":" + sec;
		finalData += "|RDate:" + fdate + "|FDate:" + fdate + "|TDate:" + tdate;
		if (ReportUtility.isDG) {
			finalData += "|DSale:" + FormatNumber.formatToTwoDecimal(Double.parseDouble(bean.getDrawSale())) + "|DNPayout:"
					+ bean.getDrawPwt();
		}
		if (ReportUtility.isSE) {
			finalData += "|SSale:" + bean.getScratchSale() + "|SNPayout:"
					+ bean.getScratchPwt();
		}
		if (ReportUtility.isCS) {
			finalData += "|CSSale:" + bean.getCSSale();
		}
		if (ReportUtility.isOLA) {
			finalData += "|OLADeposit:" + bean.getOlaDeposit()
					+ "|OLAWithdrawal:" + bean.getOlaWithdraw();
		}
		if (ReportUtility.isSLE) {
			finalData += "|SLSale:" + bean.getSleSale() + "|SLNPayout:"
					+ bean.getSlePwt();
		}
		if (ReportUtility.isIW) {
			finalData += "|IWSale:" + bean.getIwSale() + "|IWNPayout:"
					+ bean.getIwPwt();
		}
		double pft = Double.parseDouble(bean.getGrandTotal())
				- Double.parseDouble(bean.getOpenBal());
		finalData += "|CCol:" + bean.getGrandTotal() + "|Pft:"
				+ FormatNumber.formatNumber(pft) + "|PTPA:" + bean.getOpenBal()
				+ "|ROrg:" + retName + "|";

		try {
			System.out.println("FINAL SUMMARY TXN REPORT DATA:" + finalData);
			response.getOutputStream().write(finalData.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void detailTransactionReport() throws UnsupportedEncodingException,
			LMSException {
		ServletContext sc = ServletActionContext.getServletContext();

		Map currentUserSessionMap = (Map) sc.getAttribute("LOGGED_IN_USERS");
		if (currentUserSessionMap == null) {
			try {
				response.getOutputStream()
						.write(("ErrorMsg:" + EmbeddedErrors.SESSION_EXPIRED + "ErrorCode:01|")
								.getBytes());
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}
		// System.out.println(" LOGGED_IN_USERS maps is " +
		// currentUserSessionMap);

		System.out.println(" user name is " + userName);
		session = (HttpSession) currentUserSessionMap.get(userName);
		if (!CommonFunctionsHelper.isSessionValid(session)) {
			try {
				response.getOutputStream()
						.write(("ErrorMsg:" + EmbeddedErrors.SESSION_EXPIRED + "ErrorCode:01|")
								.getBytes());
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}
		DateBeans dateBeans = null;

		if ("Current Day".equalsIgnoreCase(URLDecoder.decode(type, "UTF-8"))) {
			dateBeans = GetDate.getDate(URLDecoder.decode(type, "UTF-8"));
		} else if ("Last Day"
				.equalsIgnoreCase(URLDecoder.decode(type, "UTF-8"))) {
			dateBeans = new DateBeans();
			dateBeans.setFirstdate(new java.sql.Date(new Date().getTime() - 24
					* 60 * 60 * 1000));
			dateBeans.setLastdate(new java.sql.Date(new Date().getTime()));
		} else if ("Day Before Yesterday".equalsIgnoreCase(URLDecoder.decode(
				type, "UTF-8"))) {
			dateBeans = new DateBeans();
			dateBeans.setFirstdate(new java.sql.Date(new Date().getTime() - 2
					* 24 * 60 * 60 * 1000));
			dateBeans.setLastdate(new java.sql.Date(new Date().getTime() - 24
					* 60 * 60 * 1000));
		}
		userBean = (UserInfoBean) session.getAttribute("USER_INFO");
		frDate = dateBeans.getFirstdate();
		tDate = dateBeans.getLastdate();
		String retOrgName = userBean.getOrgName();
		OrgName = retOrgName;
		int retOrgId = userBean.getUserOrgId();

		RetDailyLedgerHelper helper2 = new RetDailyLedgerHelper();
		Map<String, List<String>> dgMap = null;
		Map<String, List<String>> seMap = null;
		if (ReportUtility.isDG) {
			dgMap = helper2.getDGDetailReport(dateBeans, retOrgId);
		}
		if (ReportUtility.isSE) {
			seMap = helper2.getSEDetailReport(dateBeans, retOrgId);
		}
		generateDetailedTransactionReport(seMap, dgMap, dateBeans
				.getFirstdate().toString().split(" ")[0], retOrgName);

	}

	private void generateDetailedTransactionReport(
			Map<String, List<String>> seList, Map<String, List<String>> dgList,
			String date, String retOrgName) throws LMSException {
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
		String finalData = "Date:"
				+ new java.sql.Date((new java.util.Date()).getTime())
						.toString() + "|Time:" + hour + ":" + min + ":" + sec;
		finalData += "|ReportDate:" + date;
		/*
		 * int i=0,len = dataList.size(); if(len!=0){ Double totalMrpSale = 0.0;
		 * Double totalMrpPwt = 0.0; Double totalNetSale = 0.0; Double
		 * totalNetPwt = 0.0; while(i<len){
		 * 
		 * Set<String> keySet = dataList.get(i).keySet(); Iterator<String> itr =
		 * keySet.iterator(); finalData += "|Game Type:"; while(itr.hasNext()){
		 * String gName = itr.next(); if(gName.indexOf("name") != -1){ finalData
		 * += "GameN:"+ gName.substring(4, gName.length())+","; } } finalData +=
		 * "GameS:"+
		 * FormatNumber.formatNumber(dataList.get(i).get("mrpSale"))+",";
		 * finalData += "GamePWT:"+
		 * FormatNumber.formatNumber(dataList.get(i).get("mrpPwt"));
		 * totalMrpSale += dataList.get(i).get("mrpSale"); totalNetSale +=
		 * dataList.get(i).get("netSale"); totalMrpPwt +=
		 * dataList.get(i).get("mrpPwt"); totalNetPwt +=
		 * dataList.get(i).get("netPwt"); i++; }
		 * 
		 * finalData += "|TotalSale:"+ FormatNumber.formatNumber(totalMrpSale);
		 * finalData += "|TotalPWT:" + FormatNumber.formatNumber(totalMrpPwt);
		 * finalData += "|CashInHand:" + FormatNumber.formatNumber(totalMrpSale
		 * - totalMrpPwt); finalData += "|PTPA:" +
		 * FormatNumber.formatNumber(totalNetSale - totalNetPwt); finalData +=
		 * "|Profit:" + FormatNumber.formatNumber((totalMrpSale - totalMrpPwt) -
		 * (totalNetSale - totalNetPwt)); finalData += "|RetOrg:" + retOrgName
		 * +"|"; } try { response.getOutputStream().write(finalData.getBytes());
		 * } catch (Exception e) { e.printStackTrace(); }
		 */
	}

	public void detailTxnReport() {

	}

	public String getFromDate() {
		return fromDate;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	/*
	 * Date:2009-12-18| Time:22:33:44| ReportDate:2010-12-12| Game:game
	 * name,sale,PWT| Game Type:GameN:5/90,GameS:1234.00,GamePWT:567.00| Game
	 * Type:GameN:5/90,GameS:1234.00,GamePWT:567.00| Game
	 * Type:GameN:5/90,GameS:1234.00,GamePWT:567.00| TotalSale:234554.00|
	 * TotalPWT:567.00| CashInHand:678.00| PTPA:678.00| Profit:678.00|
	 * RetOrg:gjhbdjbfjhsbfjhs|
	 */

	/*
	 * private void generateReport(String query, Timestamp fromTimeStamp,
	 * Timestamp dt, String typeName) {
	 * 
	 * DBConnect dbConnect = null; Connection connection = null;
	 * 
	 * double balance = 0.0; double amount = 0.0;
	 * 
	 * System.out.println("query " + query); System.out.println("fromTimeStamp "
	 * + fromTimeStamp); System.out.println("dt " + dt);
	 * System.out.println("typeName " + typeName); ServletContext sc =
	 * ServletActionContext.getServletContext(); String isDraw = (String)
	 * sc.getAttribute("IS_SCRATCH"); if (isDraw.equalsIgnoreCase("NO")) { try {
	 * response.getOutputStream().write( "Scratch Game Not
	 * Avaialbe".getBytes()); } catch (IOException e) { System.out.println("
	 * exception in ledger action"); e.printStackTrace(); } return; } try {
	 * 
	 * connection = DBConnect.getConnection(); PreparedStatement statement =
	 * connection.prepareStatement(query);
	 * 
	 * statement.setString(1, typeName); statement.setTimestamp(2,
	 * fromTimeStamp); statement.setTimestamp(3, dt);
	 * 
	 * System.out.println(" " + statement); ResultSet resultSet =
	 * statement.executeQuery();
	 * 
	 * String transactionType = null; // String transactionId = null; // String
	 * accountType = null; // Timestamp trDate = null; // String transactionWith
	 * = null;
	 * 
	 * StringBuilder xmlString = new StringBuilder( "<?xml version=\"1.0\"
	 * encoding=\"UTF-8\"?><ledger><date>" +
	 * fromTimeStamp.toString().substring(0, 10) + "</date>");
	 * 
	 * System.out.println(" before result set"); int i = 0; String amtType =
	 * null; while (resultSet.next()) { System.out.println("
	 * <<<----inside---->>> result
	 * set"); if (i == 0) { balance = resultSet.getDouble("balance"); } i++;
	 * amount = resultSet.getDouble("amount"); if (amount > 0) { amtType = "C";
	 * } else { amtType = "D"; amount = amount * (-1); } transactionType =
	 * resultSet.getString("transaction_type"); xmlString.append("<details
	 * id=\"" + i + "\">" + transactionType + " " + amount + " " + amtType +
	 * "</details>"); // transactionId = resultSet.getString("transaction_id");
	 * // accountType = resultSet.getString("account_type"); // trDate =
	 * resultSet.getTimestamp("transaction_date"); // transactionWith =
	 * resultSet.getString("transaction_with"); // System.out.println(balance +
	 * " " + amount + " " // + transactionType + " " + transactionId + " " // +
	 * accountType + " " + trDate + " " + transactionWith); }
	 * xmlString.append("<balance>" + balance + "</balance></ledger>");
	 * System.out.println("openingBalance " + balance);
	 * 
	 * System.out.println("PPPPPPPPPP" + statement.toString());
	 * 
	 * response.getOutputStream().write(xmlString.toString().getBytes()); }
	 * catch (Exception e) { e.printStackTrace(); } finally { try {
	 * 
	 * System.out.println(" closing connection "); if (connection != null) {
	 * connection.close(); } } catch (SQLException e) { // TODO Auto-generated
	 * catch block e.printStackTrace(); } } }
	 */

	public HttpServletResponse getResponse() {
		return response;
	}

	public String getToDate() {
		return toDate;
	}

	public String getType() {
		return type;
	}

	public String getUserName() {
		return userName;
	}

	/**
	 * This method is for entering the data into agent ledger.
	 * 
	 * @return String
	 * @throws Exception
	 */
	public void retLedger() throws Exception {

		String actionName = ActionContext.getContext().getName();
		ReportStatusBean reportStatusBean = ReportUtility
				.getReportStatus(actionName);

		if ("FAILURE".equals(reportStatusBean.getReportStatus())) {
			response.getOutputStream().write(
					("ErrorMsg:No Reporting Till " + reportStatusBean
							.getEndTime()+"|ErrorCode:"+EmbeddedErrors.REPORTING_TIME_ERROR_CODE).getBytes());
			return;
		}

		ServletContext sc = ServletActionContext.getServletContext();
		Map currentUserSessionMap = (Map) sc.getAttribute("LOGGED_IN_USERS");
		session = (HttpSession) currentUserSessionMap.get(userName);

		/*
		 * String reportingOffStartTime = (String)
		 * sc.getAttribute("REPORTING_OFF_START_TIME"); String
		 * reportingOffEndTime = (String)
		 * sc.getAttribute("REPORTING_OFF_END_TIME");
		 * logger.debug("REPORTING_OFF_START_TIME - "+reportingOffStartTime);
		 * logger.debug("REPORTING_OFF_END_TIME - "+reportingOffEndTime);
		 * 
		 * Calendar calendar = Calendar.getInstance(); dateformat = new
		 * SimpleDateFormat("HH:mm:ss"); String time =
		 * dateformat.format(calendar.getTime());
		 * logger.debug("Current Time - "+time);
		 * if(time.compareTo(reportingOffStartTime)>0 &&
		 * time.compareTo(reportingOffEndTime)<0) { try {
		 * response.getOutputStream
		 * ().write(("ErrorMsg:No Reporting Till "+reportingOffEndTime
		 * ).getBytes()); } catch (Exception e) { e.printStackTrace(); } return;
		 * }
		 */

		DateBeans dateBeans = null;
		if ("Current Day".equalsIgnoreCase(URLDecoder.decode(type, "UTF-8"))) {
			dateBeans = GetDate.getDate(URLDecoder.decode(type, "UTF-8"));
		} else if ("Last Day"
				.equalsIgnoreCase(URLDecoder.decode(type, "UTF-8"))) {
			dateBeans = new DateBeans();
			dateBeans.setFirstdate(new java.sql.Date(new Date().getTime() - 24
					* 60 * 60 * 1000));
			dateBeans.setLastdate(new java.sql.Date(new Date().getTime()));
		}

		userBean = (UserInfoBean) session.getAttribute("USER_INFO");

		if (ServicesBean.isDG()) {
			String refMerchantId = (String) sc.getAttribute("REF_MERCHANT_ID");
			int autoCancelHoldDays = Integer.parseInt((String) sc
					.getAttribute("AUTO_CANCEL_CLOSER_DAYS"));
			DrawGameRPOSHelper dgHelper = new DrawGameRPOSHelper();
			long lastPrintedTicket = 0;
			int gameId = 0;
			if (LSTktNo != 0) {
				lastPrintedTicket = LSTktNo
						/ Util.getDivValueForLastSoldTkt(String
								.valueOf(LSTktNo).length());
				gameId = Util.getGameIdFromGameNumber(Util
						.getGamenoFromTktnumber(String.valueOf(LSTktNo)));
			}
			dgHelper.checkLastPrintedTicketStatusAndUpdate(userBean,
					lastPrintedTicket, "TERMINAL", refMerchantId,
					autoCancelHoldDays, actionName, gameId);
		}

		dateformat = new SimpleDateFormat("yyyy-MM-dd");
		frDate = dateformat.parse(dateBeans.getFirstdate().toString());
		tDate = dateformat.parse(dateBeans.getLastdate().toString());

		dateBeans = new DateBeans();
		int orgId = userBean.getUserOrgId();
		RetDailyLedgerHelper helper = new RetDailyLedgerHelper();
		SimpleDateFormat sDF = new SimpleDateFormat(
				(String) sc.getAttribute("date_format"));
		Date deplDate = sDF.parse((String) sc.getAttribute("DEPLOYMENT_DATE"));
		dateBeans.setStartTime(new java.sql.Timestamp(deplDate.getTime()));
		dateBeans.setEndTime(new java.sql.Timestamp(frDate.getTime()));
		DailyLedgerBean CRB = new DailyLedgerBean();
		DailyLedgerBean CRBTemp = new DailyLedgerBean();
		if ("Y".equals(isLiveBal)) {
			Timestamp deployDate = dateBeans.getStartTime();
			dateBeans.setStartTime(new java.sql.Timestamp(frDate.getTime()));
			dateBeans.setEndTime(new java.sql.Timestamp(tDate.getTime()));
			CRB = helper.getRetLegderDetailClXclInc(deployDate, dateBeans,
					userBean, reportStatusBean);

			/*
			 * CRBTemp = helper.getRetLegderDetailClXclInc(dateBeans, userBean,
			 * reportStatusBean); dateBeans.setStartTime(new
			 * java.sql.Timestamp(frDate.getTime())); dateBeans.setEndTime(new
			 * java.sql.Timestamp(tDate.getTime())); CRB =
			 * helper.getRetLegderDetailClXclInc(dateBeans, userBean,
			 * reportStatusBean); CRB.setOpenBal(CRBTemp.getClrBal());
			 * CRB.setClrBal
			 * (FormatNumber.formatNumber(Double.parseDouble(CRB.getOpenBal()) +
			 * Double.parseDouble(CRB.getClrBal())));
			 * CRB.setClXclAmount(FormatNumber
			 * .formatNumber(Double.parseDouble(CRBTemp.getClXclAmount()) +
			 * Double.parseDouble(CRB.getClXclAmount())));
			 */
		} else if (isLiveBal == null || "N".equals(isLiveBal)) {
            Timestamp deployDate = dateBeans.getStartTime();
			dateBeans.setStartTime(new java.sql.Timestamp(frDate.getTime()));
			dateBeans.setEndTime(new java.sql.Timestamp(tDate.getTime()));

            CRB = helper.getRetLegderDetail(deployDate, dateBeans, userBean, reportStatusBean);
		}

		OrgName = userBean.getOrgName();
		generateReport(CRB, "Y".equals(isLiveBal));
	}

	public void retReportGameWise() throws Exception {
		ServletContext sc = ServletActionContext.getServletContext();

		Map currentUserSessionMap = (Map) sc.getAttribute("LOGGED_IN_USERS");
		
		ReportStatusBean reportStatusBean = ReportUtility.getReportStatus(ActionContext.getContext().getName());
		if ("FAILURE".equals(reportStatusBean.getReportStatus())) {
			response.getOutputStream().write(("ErrorMsg:No Reporting Till " + reportStatusBean.getEndTime() + "|ErrorCode:" + EmbeddedErrors.REPORTING_TIME_ERROR_CODE).getBytes());
			return;
		}
		
		/*
		 * if (currentUserSession Map == null) { try { response
		 * .getOutputStream() .write( ("ErrorMsg:" +
		 * EmbeddedErrors.SESSION_EXPIRED + "ErrorCode:01|") .getBytes()); }
		 * catch (IOException e) { e.printStackTrace(); } return; }
		 */
		// System.out.println(" LOGGED_IN_USERS maps is " +
		// currentUserSessionMap);

		// System.out.println(" user name is " + userName);
		session = (HttpSession) currentUserSessionMap.get(userName);
		/*
		 * if (!CommonFunctionsHelper.isSessionValid(session)) { try { response
		 * .getOutputStream() .write( ("ErrorMsg:" +
		 * EmbeddedErrors.SESSION_EXPIRED + "ErrorCode:01|") .getBytes()); }
		 * catch (IOException e) { e.printStackTrace(); } return; }
		 */

		// to cancel last sold ticket if unprinted at terminal end
		userBean = (UserInfoBean) session.getAttribute("USER_INFO");
		Map<Integer, Map<Integer, String>> drawIdTableMap = (Map<Integer, Map<Integer, String>>) sc
				.getAttribute("drawIdTableMap");
		String refMerchantId = (String) sc.getAttribute("REF_MERCHANT_ID");
		int autoCancelHoldDays = Integer.parseInt((String) sc
				.getAttribute("AUTO_CANCEL_CLOSER_DAYS"));
		long lastPrintedTicket = 0;
		int gameId = 0;
		if (LSTktNo != 0) {
			lastPrintedTicket = LSTktNo
					/ Util.getDivValueForLastSoldTkt(String.valueOf(LSTktNo)
							.length());
			gameId = Util.getGameIdFromGameNumber(Util
					.getGamenoFromTktnumber(String.valueOf(LSTktNo)));
		}
		// long lastSoldTktLMS =
		// dgHelper.getLastSoldTicketTerminal(userBean.getUserOrgId(),"TERMINAL");
		DrawGameRPOSHelper dgHelper = new DrawGameRPOSHelper();
		String actionName = ActionContext.getContext().getName();
		dgHelper.checkLastPrintedTicketStatusAndUpdate(userBean,
				lastPrintedTicket, "TERMINAL", refMerchantId,
				autoCancelHoldDays, actionName, gameId);

//		Calendar calendar = Calendar.getInstance();
//		String reportingOffStartTime = (String) sc
//				.getAttribute("REPORTING_OFF_START_TIME");
//		System.out
//				.println("REPORTING_OFF_START_TIME::" + reportingOffStartTime);
//		String reportingOffEndTime = (String) sc
//				.getAttribute("REPORTING_OFF_END_TIME");
//		System.out.println("REPORTING_OFF_END_TIME::" + reportingOffEndTime);
//
//		String time = (calendar.get(Calendar.HOUR_OF_DAY) + ":"
//				+ calendar.get(Calendar.MINUTE) + ":" + calendar
//				.get(Calendar.SECOND));
//		System.out.println("current time::" + time);
//		if (time.compareTo(reportingOffStartTime) > 0) {
//			if (time.compareTo(reportingOffEndTime) < 0) {
//				try {
//					response.getOutputStream()
//							.write(("ErrorMsg:No Reporting Till " + reportingOffEndTime+"|ErrorCode:"+EmbeddedErrors.REPORTING_TIME_ERROR_CODE)
//									.getBytes());
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//				return;
//			}
//		}

		// continue for report generation
		DateBeans dateBeans = null;
		Map<String, List<Map<String, Double>>> dataMap = new HashMap<String, List<Map<String, Double>>>();

		if ("Current Day".equalsIgnoreCase(URLDecoder.decode(type, "UTF-8"))) {
			dateBeans = GetDate.getDate(URLDecoder.decode(type, "UTF-8"));
		} else if ("Last Day"
				.equalsIgnoreCase(URLDecoder.decode(type, "UTF-8"))) {
			dateBeans = new DateBeans();
			dateBeans.setFirstdate(new java.sql.Date(new Date().getTime() - 24
					* 60 * 60 * 1000));
			dateBeans.setLastdate(new java.sql.Date(new Date().getTime()));
		} else if ("Day Before Yesterday".equalsIgnoreCase(URLDecoder.decode(
				type, "UTF-8"))) {
			dateBeans = new DateBeans();
			dateBeans.setFirstdate(new java.sql.Date(new Date().getTime() - 2
					* 24 * 60 * 60 * 1000));
			dateBeans.setLastdate(new java.sql.Date(new Date().getTime() - 24
					* 60 * 60 * 1000));
		}
		String dateFormat = "yyyy-MM-dd";
		frDate = new Date((new SimpleDateFormat(dateFormat)).parse(
				dateBeans.getFirstdate().toString()).getTime());
		tDate = new Date((new SimpleDateFormat(dateFormat)).parse(
				dateBeans.getLastdate().toString()).getTime());
		dateBeans.setStartTime(new Timestamp(frDate.getTime()));
		dateBeans.setEndTime(new Timestamp(tDate.getTime()));
		String retOrgName = userBean.getOrgName();
		OrgName = retOrgName;
		int retOrgId = userBean.getUserOrgId();
		//RetDailyLedgerHelper helper = new RetDailyLedgerHelper();
		dataMap = RetDailyLedgerHelper.retDailyLedgerGameWiseTerminal(dateBeans, retOrgId, reportStatusBean);
		generateReportGameWise(dataMap, dateBeans.getFirstdate().toString()
				.split(" ")[0], userBean);

	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;

	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public long getLSTktNo() {
		return LSTktNo;
	}

	public void setLSTktNo(long lSTktNo) {
		LSTktNo = lSTktNo;
	}

}