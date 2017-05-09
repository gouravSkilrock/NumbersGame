package com.skilrock.lms.web.reportsMgmt.common;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
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
import com.skilrock.lms.common.utility.TextConfigurator;

public class WriteExcelForConsLiveGameReport extends LocalizedTextUtil{
	private WritableCellFormat dateFormat;

	private WritableCellFormat headerDateFormat;
	private WritableCellFormat headerFormat;
	private WritableCellFormat headingLabel;
	private WritableCellFormat headingNumberFormat;
	Log logger = LogFactory.getLog(WriteExcelForConsLiveGameReport.class);
	private WritableCellFormat numberFormat;
	private String reportType;
	private java.util.Date startDate, endDate, reportday;
	private WritableCellFormat times;
	private WritableCellFormat timesBoldUnderline;
	private Locale locale=Locale.getDefault();
	public WriteExcelForConsLiveGameReport(DateBeans dateBeans)
			throws WriteException {

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
		headingLabel = new Label(column, row, s!=null?s.toString():"", times);
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

	private void createContent(WritableSheet sheet, Map<String, String> repMap,
			String orgname, String orgAdd, String currSym)
			throws WriteException, RowsExceededException, ParseException {

		sheet.addCell(new Label(5, 1, orgname, headerFormat));
		sheet.mergeCells(5, 1, 15, 1);
		sheet.addCell(new Label(16, 1, findDefaultText("label.amt.in", locale)+" "+ currSym, headerFormat));
		sheet.addCell(new Label(5, 2, orgAdd, headerFormat));
		sheet.mergeCells(5, 2, 16, 2);

		createHeaderForConsLiveGameReport(sheet);
		addCaption(sheet, 1, 4, ((Map) ServletActionContext
				.getServletContext().getAttribute("TIER_MAP")).get("RETAILER")
						+ " "+findDefaultText("label.name", locale), 13);
		sheet.mergeCells(1, 4, 3, 5);
		sheet.mergeCells(1, 4, 3, 5);
		addCaption(sheet, 4, 4, findDefaultText("label.draw.game", locale), 10);
		sheet.mergeCells(4, 4, 6, 4);
		addCaption(sheet, 7, 4, findDefaultText("label.scratch.game", locale), 8);
		sheet.mergeCells(7, 4, 8, 4);
		addCaption(sheet, 9, 4, findDefaultText("label.ola.report", locale), 8);
		sheet.mergeCells(9, 4, 10, 4);
		addCaption(sheet, 11, 4, findDefaultText("label.cs.sale", locale), 18);
		sheet.mergeCells(11, 4, 11, 5);
		addCaption(sheet,12,4,findDefaultText("label.sport.lot", locale), 10);
		sheet.mergeCells(12, 4, 14, 4);
		addCaption(sheet, 15, 4, findDefaultText("label.cash.submitted", locale), 18);
		sheet.mergeCells(15, 4, 15, 5);
		addCaption(sheet, 16, 4, findDefaultText("label.net.amt", locale), 18);
		sheet.mergeCells(16, 4, 16, 5);
		addCaption(sheet, 4, 5, findDefaultText("label.sale.amt", locale), 13);
		addCaption(sheet, 5, 5, findDefaultText("label.cancel.amt", locale), 13);
		addCaption(sheet, 6, 5, TextConfigurator.getText("PWT")+" "+findDefaultText("label.amount", locale), 15);
		addCaption(sheet, 7, 5, findDefaultText("label.sale.amt", locale), 13);
		addCaption(sheet, 8, 5, TextConfigurator.getText("PWT")+" "+findDefaultText("label.amount", locale), 15);
		addCaption(sheet, 9, 5, findDefaultText("label.depo.amt", locale), 15);
		addCaption(sheet, 10, 5, findDefaultText("label.wdl.amt", locale), 17);
		addCaption(sheet, 12, 5, findDefaultText("label.sale.amt", locale), 13);
		addCaption(sheet, 13, 5, findDefaultText("label.cancel.amt", locale), 13);
		addCaption(sheet, 14, 5, TextConfigurator.getText("PWT")+" "+findDefaultText("label.amount", locale), 15);
	
		int length = repMap.size();

//		System.out.println("ReportMap    " + repMap);
		int i = 0;
		Iterator iter = repMap.entrySet().iterator();
		while (iter.hasNext()) {

			Map.Entry pair = (Map.Entry) iter.next();
			if (!((String) pair.getKey()).equalsIgnoreCase("Total")
					&& !((String) pair.getKey()).equalsIgnoreCase("dirPlrPwt")
					&& !((String) pair.getKey()).equalsIgnoreCase("agtName")) {
				System.out.println((String) pair.getValue());
				addLabel(sheet, 1, i + 6, pair.getKey());
				sheet.mergeCells(1, i + 6, 3, i + 6);
				String[] data = ((String) pair.getValue()).split(",");
				addNumber(sheet, 4, i + 6, Double.parseDouble(data[0]));
				addNumber(sheet, 5, i + 6, Double.parseDouble(data[1]));
				addNumber(sheet, 6, i + 6, Double.parseDouble(data[2]));
				addNumber(sheet, 7, i + 6, Double.parseDouble(data[3]));
				addNumber(sheet, 8, i + 6, Double.parseDouble(data[4]));
				addNumber(sheet, 9, i + 6, Double.parseDouble(data[5]));
				addNumber(sheet, 10, i + 6, Double.parseDouble(data[6]));
				addNumber(sheet, 11, i + 6, Double.parseDouble(data[7]));
				addNumber(sheet, 12, i + 6, Double.parseDouble(data[8]));
				addNumber(sheet, 13, i + 6, Double.parseDouble(data[9]));
				addNumber(sheet, 14, i + 6, Double.parseDouble(data[10]));
				addNumber(sheet, 15, i + 6, Double.parseDouble(data[11]));
				addNumber(sheet, 16, i + 6, Double.parseDouble(data[12]));
				i++;
			}
		}
		String[] bottomLine = repMap.get("Total").split(",");
		addCaption(sheet, 1, length + 3, findDefaultText("label.total", locale), 12);
		sheet.mergeCells(1, length + 3, 3, length + 3);
		sheet.addCell(new Number(4, length + 3, Double
				.parseDouble(bottomLine[0]), headingNumberFormat));
		sheet.addCell(new Number(5, length + 3, Double
				.parseDouble(bottomLine[1]), headingNumberFormat));
		sheet.addCell(new Number(6, length + 3, Double
				.parseDouble(bottomLine[2]), headingNumberFormat));
		sheet.addCell(new Number(7, length + 3, Double
				.parseDouble(bottomLine[3]), headingNumberFormat));
		sheet.addCell(new Number(8, length + 3, Double
				.parseDouble(bottomLine[4]), headingNumberFormat));
		sheet.addCell(new Number(9, length + 3, Double
				.parseDouble(bottomLine[5]), headingNumberFormat));
		sheet.addCell(new Number(10, length + 3, Double
				.parseDouble(bottomLine[6]), headingNumberFormat));
		sheet.addCell(new Number(11, length + 3, Double
				.parseDouble(bottomLine[7]), headingNumberFormat));
		sheet.addCell(new Number(12, length + 3, Double
				.parseDouble(bottomLine[8]), headingNumberFormat));
		sheet.addCell(new Number(13, length + 3, Double
				.parseDouble(bottomLine[9]), headingNumberFormat));
		sheet.addCell(new Number(14, length + 3, Double
				.parseDouble(bottomLine[10]), headingNumberFormat));
		sheet.addCell(new Number(15, length + 3, Double
				.parseDouble(bottomLine[11]), headingNumberFormat));
		sheet.addCell(new Number(16, length + 3, Double
				.parseDouble(bottomLine[12]), headingNumberFormat));

		addCaption(sheet, 1, length + 7, ((Map) ServletActionContext
					.getServletContext().getAttribute("TIER_MAP")).get("AGENT")
					+ " "+findDefaultText("label.name", locale) , 15);
		sheet.mergeCells(1, length + 7, 3, length + 7);
		addCaption(sheet, 4, length + 7, findDefaultText("label.net", locale)+" "+ TextConfigurator.getText("PWT") +" "+findDefaultText("label.amount", locale), 15);
		sheet.mergeCells(4, length + 7, 6, length + 7);
		addLabel(sheet, 1, length + 8, repMap.get("agtName"));
		sheet.mergeCells(1, length + 8, 3, length + 8);
		addNumber(sheet, 4, length + 8, Double.parseDouble(repMap
				.get("dirPlrPwt")));
		sheet.mergeCells(4, length + 8, 6, length + 8);

	}

	private void createNextContent(WritableSheet sheet,
			List<ArrayList<String>> repMap, String orgname, String orgAdd,
			String currSym, ArrayList<String> gameList) throws WriteException,
			RowsExceededException, ParseException {

		sheet.addCell(new Label(2, 1, orgname, headerFormat));
		sheet.mergeCells(2, 1, 9, 1);
		sheet.addCell(new Label(10, 1, findDefaultText("label.amt.in", locale)+" " + currSym, headerFormat));
		sheet.addCell(new Label(2, 2, orgAdd, headerFormat));
		sheet.mergeCells(2, 2, 10, 2);

		createHeaderForConsLiveGameReport(sheet);
		addCaption(sheet, 1, 4, ((Map) ServletActionContext.getServletContext()
				.getAttribute("TIER_MAP")).get("RETAILER")
				+ " "+findDefaultText("label.name", locale), 13);
		sheet.mergeCells(1, 4, 3, 5);

		int col1Count = 4;
		int row1Count = 4;
		int col2Count = 5;
		int row2Count = 4;
		Iterator<String> gameListIterator = gameList.iterator();
		while (gameListIterator.hasNext()) {
			String gameName = gameListIterator.next();
			addCaption(sheet, col1Count, row1Count, gameName, 10);
			sheet.mergeCells(col1Count, row1Count, col2Count, row2Count);
			col1Count += 2;
			col2Count += 2;
		}
		row2Count = 5;
		addCaption(sheet, col1Count, row1Count, findDefaultText("label.total.sale.amt", locale), 18);
		sheet.mergeCells(col1Count, row1Count, col1Count, row2Count);
		col1Count += 1;
		col2Count += 2;
		addCaption(sheet, col1Count, row1Count, findDefaultText("label.total.pwt.amt", locale), 18);
		sheet.mergeCells(col1Count, row1Count, col1Count, row2Count);
		col1Count += 1;
		col2Count += 2;
		addCaption(sheet, col1Count, row1Count, findDefaultText("label.net.amt", locale), 18);
		sheet.mergeCells(col1Count, row1Count, col1Count, row2Count);

		col1Count += 1;
		col2Count += 2;
		col1Count = 4;
		row1Count = 5;

		Iterator<String> salePwtIterator = gameList.iterator();
		while (salePwtIterator.hasNext()) {
			salePwtIterator.next();
			addCaption(sheet, col1Count, row1Count, findDefaultText("label.sale.amt", locale), 18);
			col1Count += 1;
			addCaption(sheet, col1Count, row1Count, findDefaultText("label.pwt.amt", locale), 18);
			col1Count += 1;
		}
		col1Count = 1;
		col2Count = 3;
		row1Count = 6;

		Iterator<ArrayList<String>> salePwtListIterator = repMap.iterator();
		while (salePwtListIterator.hasNext()) {
			ArrayList<String> temp = salePwtListIterator.next();
			int salePwtColCount = 3;
			int count = 0;
			while (count != temp.size()) {
				if (count == 0) {
					sheet
							.mergeCells(col1Count, row1Count, col2Count,
									row1Count);
					addCaption(sheet, col1Count, row1Count, temp.get(count)
							.toString(), 18);
				} else {
					addCaption(sheet, salePwtColCount, row1Count, temp.get(
							count).toString(), 18);
				}
				count += 1;
				salePwtColCount += 1;

			}
			row1Count += 1;
		}
	}

	private void createHeaderForConsLiveGameReport(WritableSheet sheet)
			throws WriteException, ParseException {
		DateFormat format = new SimpleDateFormat("dd-MMM-yy");

		sheet.addCell(new Label(10, 0,
				" "+findDefaultText("label.date", locale )+"  :  " + format.format(new Date()), dateFormat));
		sheet.mergeCells(10, 0, 11, 0);

		sheet.mergeCells(4, 3, 7, 3);
		sheet.mergeCells(8, 3, 10, 3);
		setHeadingForConsLiveGameReport(sheet);

	}

	private void setHeadingForConsLiveGameReport(WritableSheet sheet)
			throws RowsExceededException, WriteException {
		DateFormat format = new SimpleDateFormat("dd-MMM-yy");
		if (this.endDate == null) {

			logger
					.debug(" inside date if condition -kgkgjdkjkjkfj---- ----user Type : "
							+ this.reportType + " date : " + startDate);
			// System.out.println(" inside date if condition -kgkgjdkjkjkfj----
			// ----user Type : "+this.reportType+" date : "+startDate);
			sheet.addCell(new Label(4, 3, " "+findDefaultText("label.draw.game.sale.and.pwt.report.of", locale)+" "
					+ reportType, headerFormat));
			if (reportday == null) {
				sheet.addCell(new Label(8, 3, " ( "
						+ new SimpleDateFormat("MMM-yyyy").format(startDate)
						+ " ) ", headerDateFormat));
			} else {
				sheet.addCell(new Label(8, 3, " ( "
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
			sheet.addCell(new Label(4, 3, findDefaultText("label.draw.game.sale.and", locale)+" " + TextConfigurator.getText("PWT") +" "+findDefaultText("label.from.date", locale)
					+ "", headerFormat));
			sheet.addCell(new Label(8, 3, " ( " + format.format(startDate)
					+ "   -   " + format.format(endDate) + " ) ",
					headerDateFormat));
		}
	}

	public void write(Map<String, String> repMap, WritableWorkbook workbk,
			String orgName, String orgAddress, String orgType, String currSym)
			throws IOException, WriteException, ParseException {
		WorkbookSettings wbSettings = new WorkbookSettings();
		wbSettings.setLocale(new Locale("en", "EN"));
		WritableWorkbook workbook = workbk;
		workbook.createSheet("Report", 0);
		WritableSheet excelSheet = workbook.getSheet(0);
		if (orgType.equalsIgnoreCase("BO")) {
			createContent(excelSheet, repMap, orgName, orgAddress, currSym);
		} else {
			// todo implement the export excel in case of agent DG full Report
		}

		workbook.write();
		workbook.close();

	}

	public void writeExcel(List<ArrayList<String>> repMap,
			WritableWorkbook workbk, String orgName, String orgAddress,
			String orgType, String currSym, ArrayList<String> gameList)
			throws IOException, WriteException, ParseException {
		WorkbookSettings wbSettings = new WorkbookSettings();
		wbSettings.setLocale(new Locale("en", "EN"));
		WritableWorkbook workbook = workbk;
		workbook.createSheet("Report", 0);
		WritableSheet excelSheet = workbook.getSheet(0);
		if (orgType.equalsIgnoreCase("BO")) {
			createNextContent(excelSheet, repMap, orgName, orgAddress, currSym,
					gameList);
		} else {
			// todo implement the export excel in case of agent DG full Report
		}

		workbook.write();
		workbook.close();

	}

}
