package com.skilrock.lms.web.drawGames.reportsMgmt.common;


import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.Alignment;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.common.utility.CommonMethods;
import com.skilrock.lms.coreEngine.drawGames.reportMgmt.CombinitionAnalysisDataDrawWiseHelper;
import com.skilrock.lms.dge.beans.AnalysisBean;
import com.skilrock.lms.dge.beans.CombiAnalysisBean;
import com.skilrock.lms.web.drawGames.common.Util;
import com.skilrock.lms.web.reportsMgmt.common.ReportUtility;

public class CombinitionAnalysisDataDrawWise extends ActionSupport implements
		ServletRequestAware, ServletResponseAware {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private String depDate;
	private String dateOfDraw;
	private String gameNo;
	private String message;
	private String drawName;
	private String valueToSend;
	List<CombiAnalysisBean> topCombiBean;
	List<CombiAnalysisBean> topNumbBean;
	List<CombiAnalysisBean> topNumbDetailList;
	private String gameDevName;
	
	public String getGameDevName() {
		return gameDevName;
	}

	public void setGameDevName(String gameDevName) {
		this.gameDevName = gameDevName;
	}

	public String fetchSalesAnalysisDataDrawWise() {
		String refMerchantId = null;
		AnalysisBean anaBean = null;
		HttpSession session = null;
		Map<String, List<CombiAnalysisBean>> combiMap = null;

		try {
			gameDevName = Util.getGameName(Integer.parseInt(gameNo));
			if ("OneToTwelve".equals(gameDevName) || "Zerotonine".equals(gameDevName) || "RaffleGame1".equals(gameDevName)) {
				message = "Report is Not Available for This Game.";
				return SUCCESS;
			}
			session = getRequest().getSession();
			refMerchantId = (String) session.getServletContext().getAttribute("REF_MERCHANT_ID");
			anaBean = new AnalysisBean();

			if (dateOfDraw != null) {
				String lastDate = CommonMethods.getLastArchDate();
				System.out.println("last archieve date" + lastDate);
				if (dateOfDraw.compareTo(lastDate) <= 0) {
					message = "For Details Please Choose start date after " + lastDate;
					return SUCCESS;
				}
			}
			anaBean.setGameNo(gameNo);
			anaBean.setDrawName(drawName);
			anaBean.setStartDate(dateOfDraw);
			anaBean.setMerchantId(refMerchantId);

			combiMap = new CombinitionAnalysisDataDrawWiseHelper().getReport(anaBean);
			if (combiMap != null) {
				setDrawName(drawName);
				request.getSession().setAttribute("COMB", combiMap.get("COMB"));
				request.getSession().setAttribute("dateOfDraw", dateOfDraw);
				request.getSession().setAttribute("drawName", drawName);
				setTopCombiBean(combiMap.get("COMB"));
				setTopNumbBean(combiMap.get("NUMB"));
				setTopNumbDetailList(combiMap.get("DIST"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	public String fetchGameListMenu() {
		HttpSession session = getRequest().getSession();
		ServletContext sc = session.getServletContext();
		session.setAttribute("presentDate", new java.sql.Date(new Date().getTime()).toString());
		setDepDate(CommonMethods.convertDateInGlobalFormat((String)sc.getAttribute("DEPLOYMENT_DATE"), "yyyy-mm-dd", (String)sc.getAttribute("date_format")));
		try {
			session.setAttribute("DRAWGAME_LIST", ReportUtility.fetchDrawDataMenu());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}

	public void exportAsExcel() throws IOException, WriteException {
		if ("TwelveByTwentyFour".equals(gameDevName)) {
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-disposition", "attachment;filename=CombinationAnalysisReport.xls");
			List<CombiAnalysisBean> combiAnalysisList = (List<CombiAnalysisBean>) request.getSession().getAttribute("COMB");
			WorkbookSettings wbSettings = new WorkbookSettings();
			wbSettings.setLocale(new Locale("en", "EN"));

			WritableWorkbook workbook = Workbook.createWorkbook(response.getOutputStream(), wbSettings);
			workbook.createSheet("Report", 0);
			WritableSheet excelSheet = workbook.getSheet(0);

			// Lets create a times font
			WritableFont times10pt = new WritableFont(WritableFont.TIMES, 10);
			WritableCellFormat times = null;
			WritableCellFormat timesBoldline = null;
			// Define the cell format
			times = new WritableCellFormat(times10pt);
			times.setVerticalAlignment(VerticalAlignment.CENTRE);
			// Lets automatically wrap the cells
//			times.setWrap(true);

			WritableCellFormat cellFormat = new WritableCellFormat(times10pt);
//			cellFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
			cellFormat.setAlignment(Alignment.RIGHT);

			// create create a bold font with unterlines
			WritableFont times10ptBoldUnderline = new WritableFont(WritableFont.TIMES, 10, WritableFont.BOLD, false);
			timesBoldline = new WritableCellFormat(times10ptBoldUnderline);
//			timesBoldline.setVerticalAlignment(VerticalAlignment.CENTRE);
			cellFormat.setAlignment(Alignment.CENTRE);
			// Lets automatically wrap the cells
//			timesBoldline.setWrap(true);

			addCaption(excelSheet, 0, 0, "Report Name:Combination Analysis Report", timesBoldline);
			addCaption(excelSheet, 0, 1, getText("label.start.date") + ":" + request.getSession().getAttribute("dateOfDraw"), timesBoldline);
			addCaption(excelSheet, 0, 2, getText("label.draw.name") + ":" + request.getSession().getAttribute("drawName"), timesBoldline);
			excelSheet.mergeCells(0, 3, 13, 3);
			addCaption(excelSheet, 0, 3, getText("label.combi.wise.analys.of") + ":" + request.getSession().getAttribute("drawName"), timesBoldline);
			excelSheet.mergeCells(0, 4, 11, 4);
			addCaption(excelSheet, 0, 4, getText("label.top.d12.combi"), timesBoldline);
			addCaption(excelSheet, 12, 4, getText("label.sale.amt"), timesBoldline);
			addCaption(excelSheet, 13, 4, getText("label.combi.count"), timesBoldline);

			int i = 5;
			int j = 0;
			for (CombiAnalysisBean analysisBean : combiAnalysisList) {
				String tempArray[] = analysisBean.getCombinition().split(",");
				j = 0;
				for (String value : tempArray)
					excelSheet.addCell(new Label(j++, i, value, times));

				excelSheet.addCell(new Label(j++, i, String.valueOf(analysisBean.getTotalSale()), times));
				excelSheet.addCell(new Label(j++, i, String.valueOf(analysisBean.getCombiCount()), times));
				i++;
			}
			workbook.write();
			workbook.close();
		} else {
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition", "attachment; filename=CombinationAnalysisReport.xls");
			PrintWriter out = response.getWriter();
			if (valueToSend != null) {
				valueToSend = valueToSend.replaceAll("<tbody>", "").replaceAll("</tbody>", "").trim();
				out.write(valueToSend);
			}
		}
	}
	
	private void addCaption(WritableSheet sheet, int column, int row, String s, WritableCellFormat timesBoldline) throws RowsExceededException, WriteException {
		Label label;
		label = new Label(column, row, s, timesBoldline);
		sheet.addCell(label);
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

	public String getDepDate() {
		return depDate;
	}

	public void setDepDate(String depDate) {
		this.depDate = depDate;
	}

	public String getDateOfDraw() {
		return dateOfDraw;
	}

	public void setDateOfDraw(String dateOfDraw) {
		this.dateOfDraw = dateOfDraw;
	}

	public String getGameNo() {
		return gameNo;
	}

	public void setGameNo(String gameNo) {
		this.gameNo = gameNo;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getDrawName() {
		return drawName;
	}

	public void setDrawName(String drawName) {
		this.drawName = drawName;
	}
	
	public String getValueToSend() {
		return valueToSend;
	}

	public void setValueToSend(String valueToSend) {
		this.valueToSend = valueToSend;
	}

	public List<CombiAnalysisBean> getTopCombiBean() {
		return topCombiBean;
	}

	public void setTopCombiBean(List<CombiAnalysisBean> topCombiBean) {
		this.topCombiBean = topCombiBean;
	}

	public List<CombiAnalysisBean> getTopNumbBean() {
		return topNumbBean;
	}

	public void setTopNumbBean(List<CombiAnalysisBean> topNumbBean) {
		this.topNumbBean = topNumbBean;
	}

	public List<CombiAnalysisBean> getTopNumbDetailList() {
		return topNumbDetailList;
	}

	public void setTopNumbDetailList(List<CombiAnalysisBean> topNumbDetailList) {
		this.topNumbDetailList = topNumbDetailList;
	}

	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	@Override
	public void setServletResponse(HttpServletResponse response) {
		this.response = response;

	}
}
