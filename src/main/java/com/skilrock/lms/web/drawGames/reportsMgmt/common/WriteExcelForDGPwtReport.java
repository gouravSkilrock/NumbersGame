package com.skilrock.lms.web.drawGames.reportsMgmt.common;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import jxl.WorkbookSettings;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.write.DateFormats;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.NumberFormats;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.util.LocalizedTextUtil;
import com.skilrock.lms.beans.DateBeans;
import com.skilrock.lms.beans.DirPlrPwtRepBean;
import com.skilrock.lms.beans.PwtReportBean;
import com.skilrock.lms.common.utility.TextConfigurator;

public class WriteExcelForDGPwtReport extends LocalizedTextUtil{
	private WritableCellFormat dateFormat;

	private WritableCellFormat headerDateFormat;
	private WritableCellFormat headerFormat;
	private WritableCellFormat headingLabel;
	private WritableCellFormat headingNumberFormat;
	Log logger = LogFactory.getLog(WriteExcelForDGPwtReport.class);
	private WritableCellFormat numberFormat;
	private String reportType;
	private java.util.Date startDate, endDate, reportday;
	private WritableCellFormat times;
	private WritableCellFormat timesBoldUnderline;
	private Locale locale=Locale.getDefault(); 
	public WriteExcelForDGPwtReport(DateBeans dateBeans) throws WriteException {

		this.reportType = dateBeans.getReportType();
		this.startDate = dateBeans.getStartDate();
		this.endDate = dateBeans.getEndDate();
		this.reportday = dateBeans.getReportday();

		numberFormat = new WritableCellFormat(NumberFormats.FORMAT3);
		numberFormat.setFont(new WritableFont(WritableFont.TIMES, 10));
		numberFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
		numberFormat.setWrap(false);

		times = new WritableCellFormat(new WritableFont(WritableFont.TIMES, 10));
		times.setWrap(false);
		times.setBorder(Border.ALL, BorderLineStyle.THIN);

		timesBoldUnderline = new WritableCellFormat(new WritableFont(
				WritableFont.TIMES, 10, WritableFont.BOLD, false,
				UnderlineStyle.NO_UNDERLINE));
		timesBoldUnderline.setWrap(false);
		timesBoldUnderline.setAlignment(Alignment.CENTRE);
		timesBoldUnderline.setBorder(Border.ALL, BorderLineStyle.MEDIUM);
		timesBoldUnderline.setBackground(Colour.GREY_25_PERCENT);

		dateFormat = new WritableCellFormat(DateFormats.FORMAT2);
		dateFormat.setFont(new WritableFont(WritableFont.TIMES, 10,
				WritableFont.BOLD));
		dateFormat.setWrap(false);
		dateFormat.setAlignment(Alignment.RIGHT);

		headerFormat = new WritableCellFormat(new WritableFont(
				WritableFont.TIMES, 10));
		headerFormat.setWrap(false);
		headerFormat.setAlignment(Alignment.CENTRE);

		headerDateFormat = new WritableCellFormat(DateFormats.FORMAT4);
		headerDateFormat.setFont(new WritableFont(WritableFont.TIMES, 10,
				WritableFont.BOLD));
		headerDateFormat.setWrap(false);
		headerDateFormat.setAlignment(Alignment.LEFT);

		headingLabel = new WritableCellFormat(new WritableFont(
				WritableFont.TIMES, 10));
		headingLabel.setWrap(false);
		headingLabel.setBorder(Border.ALL, BorderLineStyle.MEDIUM);
		headingLabel.setAlignment(Alignment.LEFT);
		headingLabel.setBackground(Colour.GRAY_25);

		headingNumberFormat = new WritableCellFormat(NumberFormats.FORMAT3);
		headingNumberFormat.setFont(new WritableFont(WritableFont.TIMES, 10));
		headingNumberFormat.setWrap(false);
		headingNumberFormat.setBorder(Border.ALL, BorderLineStyle.MEDIUM);
		headingNumberFormat.setBackground(Colour.GRAY_25);

	}

	/**
	 * It create the table header of excel sheet.
	 * 
	 * @param sheet
	 * @param column
	 * @param row
	 * @param s
	 * @throws RowsExceededException
	 * @throws WriteException
	 */
	private void addCaption(WritableSheet sheet, int column, int row, String s,
			int width) throws RowsExceededException, WriteException {
		Label headingLabel;
		headingLabel = new Label(column, row, s, timesBoldUnderline);
		sheet.setColumnView(column, width);
		sheet.addCell(headingLabel);
	}

	private void addLabel(WritableSheet sheet, int column, int row, Object s)
			throws WriteException, RowsExceededException {
		Label headingLabel;
		headingLabel = new Label(column, row, s.toString(), times);
		sheet.addCell(headingLabel);
	}

	/**
	 * It insert the Number (float,double,long & int) inside the excel sheet.
	 * 
	 * @param sheet
	 * @param column
	 * @param row
	 * @param amt
	 * @throws WriteException
	 * @throws RowsExceededException
	 */
	private void addNumber(WritableSheet sheet, int column, int row, Double amt)
			throws WriteException, RowsExceededException {
		Number headingNumberFormat;
		headingNumberFormat = new Number(column, row, amt, numberFormat);
		sheet.addCell(headingNumberFormat);
	}

	private void createContent(WritableSheet sheet,
			List<PwtReportBean> reportlist, List<DirPlrPwtRepBean> dirPlrList,
			String orgname, String orgAdd, String currSym, String filter)
			throws WriteException, RowsExceededException, ParseException {

		sheet.addCell(new Label(2, 1, orgname, headerFormat));
		sheet.mergeCells(2, 1, 9, 1);
		sheet.addCell(new Label(10, 1, findDefaultText("label.amt.in", locale)+" " + currSym, headerFormat));
		sheet.addCell(new Label(2, 2, orgAdd, headerFormat));
		sheet.mergeCells(2, 2, 10, 2);

		createHeaderForDGPwtReport(sheet);
		if ("Game".equalsIgnoreCase(filter.split(" ")[0])) {
			addCaption(sheet, 2, 4, filter.split(" ")[0] + " "+findDefaultText("label.name", locale), 13);
		} else {
			addCaption(sheet, 2, 4, ((Map) ServletActionContext
					.getServletContext().getAttribute("TIER_MAP")).get(filter
					.split(" ")[0].toUpperCase())
					+ " "+findDefaultText("label.name", locale), 13);
		}
		sheet.mergeCells(2, 4, 3, 4);
		addCaption(sheet, 4, 4, findDefaultText("label.total",locale)+" "+TextConfigurator.getText("PWT"), 13);
		sheet.mergeCells(4, 4, 6, 4);

		int length = reportlist.size();
		logger.debug("size of bean list" + length);
		int i = 0;
		PwtReportBean totalBean = null;
		boolean bottomRepFlag = false;
		while (i < length) {
			PwtReportBean bean = reportlist.get(i);
			if (bean.getGameName() != null && bean.getGameName() != "Total"
					|| bean.getAgentName() != null
					&& bean.getAgentName() != "Total"
					|| bean.getRetName() != null
					&& bean.getRetName() != "Total") {

				if (bean.getGameName() != null) {
					bottomRepFlag = true;
					addLabel(sheet, 2, i + 5, bean.getGameName());
				} else if (bean.getAgentName() != null) {
					addLabel(sheet, 2, i + 5, bean.getAgentName());
				} else if (bean.getRetName() != null) {
					addLabel(sheet, 2, i + 5, bean.getRetName());
				}
				sheet.mergeCells(2, i + 5, 3, i + 5);
				addNumber(sheet, 4, i + 5, Double.parseDouble(bean.getClmMrp()));
				sheet.mergeCells(4, i + 5, 6, i + 5);
			} else {
				totalBean = bean;
			}
			i++;
		}

		addCaption(sheet, 2, length + 4, findDefaultText("label.total",locale), 13);
		sheet.mergeCells(2, length + 4, 3, length + 4);
		sheet.addCell(new Number(4, length + 4, Double.parseDouble(totalBean
				.getClmMrp()), headingNumberFormat));
		sheet.mergeCells(4, length + 4, 6, length + 4);

		if (bottomRepFlag) {
			sheet.addCell(new Label(2, length + 4 + 4,
					TextConfigurator.getText("Direct_Player")+" "+ TextConfigurator.getText("PWT") +" "+findDefaultText("label.by.back.office", locale), headerFormat));
			sheet.mergeCells(2, length + 4 + 4, 7, length + 4 + 4);
			addCaption(sheet, 2, length + 5 + 4,
					filter.split(" ")[0] + " "+findDefaultText("label.name", locale), 13);
			sheet.mergeCells(2, length + 5 + 4, 3, length + 5 + 4);
			addCaption(sheet, 4, length + 5 + 4, findDefaultText("label.total",locale)+" "+TextConfigurator.getText("PWT"), 13);
			sheet.mergeCells(4, length + 5 + 4, 6, length + 5 + 4);
			i = 0;
			int dirPwtLength = dirPlrList.size();
			DirPlrPwtRepBean dirTotalBean = null;
			while (i < dirPwtLength) {
				DirPlrPwtRepBean bean = dirPlrList.get(i);
				if (bean.getGameName() != null && bean.getGameName() != "Total") {
					addLabel(sheet, 2, i + 5 + length + 5, bean.getGameName());
					sheet.mergeCells(2, i + 5 + length + 5, 3, i + 5 + length
							+ 5);
					addNumber(sheet, 4, i + 5 + length + 5, bean.getPwtAmtClm());
					sheet.mergeCells(4, i + 5 + length + 5, 6, i + 5 + length
							+ 5);
				} else {
					dirTotalBean = bean;
				}
				i++;
			}
			addCaption(sheet, 2, dirPwtLength + length + 4 + 5, "Total", 13);
			sheet.mergeCells(2, dirPwtLength + length + 4 + 5, 3, length + 4);
			sheet.addCell(new Number(4, dirPwtLength + length + 4 + 5,
					dirTotalBean.getPwtAmtClm(), headingNumberFormat));
			sheet.mergeCells(4, dirPwtLength + length + 4 + 5, 6, dirPwtLength
					+ length + 4 + 5);
		}
	}

	private void createContentForAgent(WritableSheet sheet,
			List<PwtReportBean> reportlist, List<DirPlrPwtRepBean> dirPlrList,
			String orgname, String orgAdd, String currSym, String filter)
			throws WriteException, RowsExceededException, ParseException {
		sheet.addCell(new Label(2, 1, orgname, headerFormat));
		sheet.mergeCells(2, 1, 9, 1);
		sheet.addCell(new Label(10, 1, findDefaultText("label.amt.in", locale)+" " + currSym, headerFormat));
		sheet.addCell(new Label(2, 2, orgAdd, headerFormat));
		sheet.mergeCells(2, 2, 10, 2);

		createHeaderForDGPwtReport(sheet);
		if ("Game".equalsIgnoreCase(filter.split(" ")[0])) {
			addCaption(sheet, 2, 4, filter.split(" ")[0] + " "+findDefaultText("label.name", locale), 13);
		} else if ("Retailer".equalsIgnoreCase(filter.split(" ")[0])) {
			addCaption(sheet, 2, 4, ((Map) ServletActionContext
					.getServletContext().getAttribute("TIER_MAP")).get(filter
					.split(" ")[0].toUpperCase())
					+ " "+findDefaultText("label.name", locale), 13);
		}
		sheet.mergeCells(2, 4, 3, 4);
		addCaption(sheet, 4, 4, findDefaultText("label.mrp.amt", locale), 13);
		sheet.mergeCells(4, 4, 6, 4);
		addCaption(sheet, 7, 4, findDefaultText("label.net.amt", locale), 13);
		sheet.mergeCells(7, 4, 9, 4);
		addCaption(sheet, 10, 4, TextConfigurator.getText("PWT")+" "+findDefaultText("label.amount", locale), 13);
		
		sheet.mergeCells(10, 4, 12, 4);

		int length = reportlist.size();
		logger.debug("size of bean list" + length);
		int i = 0;
		PwtReportBean totalBean = null;
		boolean bottomRepFlag = false;
		if (length == 1) {
			bottomRepFlag = true;
		}
		while (i < length) {
			PwtReportBean bean = reportlist.get(i);
			if (bean.getGameName() != null && bean.getGameName() != "Total"
					|| bean.getRetName() != null
					&& bean.getRetName() != "Total") {

				if (bean.getGameName() != null) {
					bottomRepFlag = true;
					addLabel(sheet, 2, i + 5, bean.getGameName());
				} else if (bean.getRetName() != null) {
					addLabel(sheet, 2, i + 5, bean.getRetName());
				}
				sheet.mergeCells(2, i + 5, 3, i + 5);
				addNumber(sheet, 4, i + 5, Double.parseDouble(bean.getClmMrp()));
				sheet.mergeCells(4, i + 5, 6, i + 5);
				addNumber(sheet, 7, i + 5, Double.parseDouble(bean.getClmNet()));
				sheet.mergeCells(7, i + 5, 9, i + 5);
				addNumber(sheet, 10, i + 5, Double.parseDouble(bean
						.getClmProfit()));
				sheet.mergeCells(10, i + 5, 12, i + 5);
			} else {
				totalBean = bean;
			}
			i++;
		}

		addCaption(sheet, 2, length + 4, findDefaultText("label.total",locale), 13);
		sheet.mergeCells(2, length + 4, 3, length + 4);
		sheet.addCell(new Number(4, length + 4, Double.parseDouble(totalBean
				.getClmMrp()), headingNumberFormat));
		sheet.mergeCells(4, length + 4, 6, length + 4);
		sheet.addCell(new Number(7, length + 4, Double.parseDouble(totalBean
				.getClmNet()), headingNumberFormat));
		sheet.mergeCells(7, length + 4, 9, length + 4);
		sheet.addCell(new Number(10, length + 4, Double.parseDouble(totalBean
				.getClmProfit()), headingNumberFormat));
		sheet.mergeCells(10, length + 4, 12, length + 4);

		if (bottomRepFlag) {
			sheet.addCell(new Label(2, length + 4 + 4, TextConfigurator.getText("Direct_Player")+" "+TextConfigurator.getText("PWT")+" By "
					+ orgname, headerFormat));
			sheet.mergeCells(2, length + 4 + 4, 7, length + 4 + 4);
			addCaption(sheet, 2, length + 5 + 4,
					filter.split(" ")[0] + " "+findDefaultText("label.name", locale), 13);
			sheet.mergeCells(2, length + 5 + 4, 3, length + 5 + 4);
			addCaption(sheet, 4, length + 5 + 4, findDefaultText("label.mrp.amt", locale), 13);
			sheet.mergeCells(4, length + 5 + 4, 6, length + 5 + 4);
			addCaption(sheet, 7, length + 5 + 4, findDefaultText("label.net.amt", locale), 13);
			sheet.mergeCells(7, length + 5 + 4, 9, length + 5 + 4);
			addCaption(sheet, 10, length + 5 + 4, TextConfigurator.getText("PWT")+" "+findDefaultText("label.amount", locale), 13);
			sheet.mergeCells(10, length + 5 + 4, 12, length + 5 + 4);
			i = 0;
			int dirPwtLength = dirPlrList.size();
			DirPlrPwtRepBean dirTotalBean = null;
			while (i < dirPwtLength) {
				DirPlrPwtRepBean bean = dirPlrList.get(i);
				if (bean.getGameName() != null && bean.getGameName() != "Total") {
					addLabel(sheet, 2, i + 5 + length + 5, bean.getGameName());
					sheet.mergeCells(2, i + 5 + length + 5, 3, i + 5 + length
							+ 5);
					addNumber(sheet, 4, i + 5 + length + 5, bean.getPwtAmtClm());
					sheet.mergeCells(4, i + 5 + length + 5, 6, i + 5 + length
							+ 5);
					addNumber(sheet, 7, i + 5 + length + 5, bean.getNetAmtClm());
					sheet.mergeCells(7, i + 5 + length + 5, 9, i + 5 + length
							+ 5);
					addNumber(sheet, 10, i + 5 + length + 5, bean
							.getProfitClm());
					sheet.mergeCells(10, i + 5 + length + 5, 12, i + 5 + length
							+ 5);
				} else {
					dirTotalBean = bean;
				}
				i++;
			}
			addCaption(sheet, 2, dirPwtLength + length + 4 + 5,findDefaultText("label.total",locale), 13);
			sheet.mergeCells(2, dirPwtLength + length + 4 + 5, 3, length + 4);
			sheet.addCell(new Number(4, dirPwtLength + length + 4 + 5,
					dirTotalBean.getPwtAmtClm(), headingNumberFormat));
			sheet.mergeCells(4, dirPwtLength + length + 4 + 5, 6, dirPwtLength
					+ length + 4 + 5);
			sheet.addCell(new Number(7, dirPwtLength + length + 4 + 5,
					dirTotalBean.getNetAmtClm(), headingNumberFormat));
			sheet.mergeCells(7, dirPwtLength + length + 4 + 5, 9, dirPwtLength
					+ length + 4 + 5);
			sheet.addCell(new Number(10, dirPwtLength + length + 4 + 5,
					dirTotalBean.getProfitClm(), headingNumberFormat));
			sheet.mergeCells(10, dirPwtLength + length + 4 + 5, 12,
					dirPwtLength + length + 4 + 5);
		}

	}

	private void createHeaderForDGPwtReport(WritableSheet sheet)
			throws WriteException, ParseException {
		DateFormat format = new SimpleDateFormat("dd-MMM-yy");

		sheet.addCell(new Label(10, 0,
				" "+findDefaultText("label.date", locale )+"  :  " + format.format(new Date()), dateFormat));
		sheet.mergeCells(10, 0, 11, 0);

		sheet.mergeCells(4, 3, 6, 3);
		sheet.mergeCells(7, 3, 9, 3);
		setHeadingForDGPwtReport(sheet);

	}

	private void setHeadingForDGPwtReport(WritableSheet sheet)
			throws RowsExceededException, WriteException {
		DateFormat format = new SimpleDateFormat("dd-MMM-yy");
		if (this.endDate == null) {

			logger
					.debug(" inside date if condition -kgkgjdkjkjkfj---- ----user Type : "
							+ this.reportType + " date : " + startDate);
			// System.out.println(" inside date if condition -kgkgjdkjkjkfj----
			// ----user Type : "+this.reportType+" date : "+startDate);
			sheet.addCell(new Label(4, 3, " "+findDefaultText("label.draw.game", locale)+" "+ TextConfigurator.getText("PWT") +" "+TextConfigurator.getText("Report")+" "+findDefaultText("label.of", locale)+" "
					+ reportType, headerFormat));
			if (reportday == null) {
				sheet.addCell(new Label(7, 3, " ( "
						+ new SimpleDateFormat("MMM-yyyy").format(startDate)
						+ " ) ", headerDateFormat));
			} else {
				sheet.addCell(new Label(7, 3, " ( "
						+ new SimpleDateFormat("dd-MMM-yyyy").format(reportday)
						+ " ) ", headerDateFormat));
			}
		} else {
			logger.debug(" inside date else condition ----- ----user Type : "
					+ reportType + " date : " + startDate + " end date : "
					+ endDate);
			// System.out.println(" inside date else condition ----- ----user
			// Type : "+reportType+" date : "+startDate +" end date :
			// "+endDate);
			sheet.addCell(new Label(4, 3, findDefaultText("label.draw.game", locale)+" "+ TextConfigurator.getText("PWT") +" "+TextConfigurator.getText("Report")+" "+findDefaultText("label.from.date", locale)+" "
					+ "", headerFormat));
			sheet.addCell(new Label(7, 3, " ( " + format.format(startDate)
					+ "   -   " + format.format(endDate) + " ) ",
					headerDateFormat));
		}
	}

	public void write(List<PwtReportBean> reportBeanList,
			List<DirPlrPwtRepBean> dirPlrBeanList, WritableWorkbook workbk,
			String orgName, String orgAddress, String orgType, String currSym,
			String filter) throws IOException, WriteException, ParseException {
		WorkbookSettings wbSettings = new WorkbookSettings();
		wbSettings.setLocale(new Locale("en", "EN"));
		WritableWorkbook workbook = workbk;
		workbook.createSheet("Report", 0);
		WritableSheet excelSheet = workbook.getSheet(0);
		if (orgType.equalsIgnoreCase("BO")) {
			createContent(excelSheet, reportBeanList, dirPlrBeanList, orgName,
					orgAddress, currSym, filter);
		} else {
			createContentForAgent(excelSheet, reportBeanList, dirPlrBeanList,
					orgName, orgAddress, currSym, filter);
		}

		workbook.write();
		workbook.close();

	}

}
