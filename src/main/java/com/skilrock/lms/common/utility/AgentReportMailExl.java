package com.skilrock.lms.common.utility;

import java.io.File;
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
import java.util.Set;
import java.util.Map.Entry;

import jxl.Workbook;
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

import com.skilrock.lms.beans.CSSaleReportBean;
import com.skilrock.lms.beans.CashChqReportBean;
import com.skilrock.lms.beans.CompleteCollectionBean;
import com.skilrock.lms.beans.DateBeans;
import com.skilrock.lms.beans.PwtReportBean;
import com.skilrock.lms.beans.SalePwtReportsBean;
import com.skilrock.lms.beans.SaleReportBean;

public class AgentReportMailExl {
	static Log logger = LogFactory.getLog(AgentReportMailExl.class);

	private WritableCellFormat dateFormat;
	private WritableCellFormat headerDateFormat;
	private WritableCellFormat headerFormat;
	private WritableCellFormat headingLabel;
	private WritableCellFormat headingNumberFormat;
	private WritableCellFormat numberFormat;
	private String reportType;
	private java.util.Date startDate, endDate, reportday;
	private WritableCellFormat times;
	private WritableCellFormat timesBoldUnderline;
	private WritableWorkbook workbook;

	public AgentReportMailExl(String fileName, DateBeans dateBeans)
			throws IOException, WriteException {
		WorkbookSettings wbSettings = new WorkbookSettings();
		wbSettings.setLocale(new Locale("en", "EN"));
		this.reportType = dateBeans.getReportType();
		this.startDate = dateBeans.getFirstdate();
		this.endDate = dateBeans.getLastdate();
		this.reportday = dateBeans.getReportday();

		// System.out.println("file object ----------- "+new File(fileName));
		workbook = Workbook.createWorkbook(new File(fileName));
		// System.out.println("work book object ------- "+workbook);
		workbook.createSheet("Game Sale_Report", 0);
		workbook.createSheet("Agent Sale_Report", 1);
		workbook.createSheet("Cash_Cheque_Report", 2);
		setFormatting();

	}

	private void addCaption(WritableSheet sheet, int column, int row, String s)
			throws RowsExceededException, WriteException {
		Label label;
		label = new Label(column, row, s, timesBoldUnderline);
		sheet.setColumnView(column, 20);
		sheet.addCell(label);
	}

	private void addDoubleNumber(WritableSheet sheet, int column, int row,
			java.lang.Number integer) throws WriteException,
			RowsExceededException {
		Number number;
		number = new Number(column, row, integer.doubleValue(), numberFormat);
		sheet.addCell(number);
	}

	private void addLabel(WritableSheet sheet, int column, int row, Object s)
			throws WriteException, RowsExceededException {
		Label label;
		// System.out.println("passed String : "+s);
		if (s == null) {
			label = new Label(column, row, "", times);
		} else {
			label = new Label(column, row, s.toString(), times);
		}
		sheet.addCell(label);
	}

	private void addNumber(WritableSheet sheet, int column, int row,
			java.lang.Number integer) throws WriteException,
			RowsExceededException {
		Number number;
		number = new Number(column, row, integer.doubleValue(), times);
		sheet.addCell(number);
	}

	private void addPWTCaption(WritableSheet sheet, int column, int row,
			String s) throws RowsExceededException, WriteException {
		Label label;
		label = new Label(column, row, s, timesBoldUnderline);
		sheet.setColumnView(column, 40);
		sheet.addCell(label);
	}

	/**
	 * It creates Contents of excels sheet. It iterate the list & put the data
	 * into rows.
	 * 
	 * @param sheet
	 * @param reportlist
	 * @param agentBoDetail
	 * @throws WriteException
	 * @throws RowsExceededException
	 * @throws ParseException
	 */
	private void createCashChqAgentSheet(List<CashChqReportBean> reportlist,
			CashChqReportBean agentBoDetail) throws WriteException,
			RowsExceededException, ParseException {
		WritableSheet sheet = workbook.getSheet(2);
		createHeaderForCashChq(sheet);
		// add caption

		addCaption(sheet, 0, 4, "Payment Details With BO");
		sheet.mergeCells(0, 4, 3, 4);

		addCaption(sheet, 0, 5, "Cash Deposit");
		addCaption(sheet, 1, 5, "Cheque Deposit");
		addCaption(sheet, 2, 5, "Cheque Bounce");
		addCaption(sheet, 3, 5, "Credit Note");
		addCaption(sheet, 4, 5, "Debit Note");
		addCaption(sheet, 5, 5, "Net Deposit");

		if (agentBoDetail != null) {
			addDoubleNumber(sheet, 0, 6, Double.parseDouble(agentBoDetail
					.getTotalCash()));
			addDoubleNumber(sheet, 1, 6, Double.parseDouble(agentBoDetail
					.getTotalChq()));
			addDoubleNumber(sheet, 2, 6, Double.parseDouble(agentBoDetail
					.getCheqBounce()));
			addDoubleNumber(sheet, 3, 6, Double.parseDouble(agentBoDetail
					.getCredit()));
			addDoubleNumber(sheet, 4, 6, Double.parseDouble(agentBoDetail
					.getDebit()));
			addDoubleNumber(sheet, 5, 6, Double.parseDouble(agentBoDetail
					.getNetAmt()));
		} else {
			addDoubleNumber(sheet, 0, 6, 0.0);
			addDoubleNumber(sheet, 1, 6, 0.0);
			addDoubleNumber(sheet, 2, 6, 0.0);
			addDoubleNumber(sheet, 3, 6, 0.0);
			addDoubleNumber(sheet, 4, 6, 0.0);
			addDoubleNumber(sheet, 5, 6, 0.0);
		}

		addCaption(sheet, 0, 8, "Payment Details With Retailers");
		sheet.mergeCells(0, 8, 4, 8);

		addCaption(sheet, 0, 9, "Party Name");
		addCaption(sheet, 1, 9, "Cash Collection");
		addCaption(sheet, 2, 9, "Cheque Collection");
		addCaption(sheet, 3, 9, "Cheque Bounce");
		addCaption(sheet, 4, 9, "Credit Note");
		addCaption(sheet, 5, 9, "Debit Note");
		addCaption(sheet, 6, 9, "Net Collection");

		double cashColl = 0.0;
		double chqColl = 0.0;
		double chqBounce = 0.0;
		double crNote = 0.0;
		double dbNote = 0.0;
		// double bnkDep = 0.0;
		double netColl = 0.0;

		int length = reportlist.size();
		CashChqReportBean bean = null;
		// sheet.insertRow(1);
		for (int i = 0; i < length; i++) {
			bean = reportlist.get(i);

			cashColl += Double.parseDouble(bean.getTotalCash());
			chqColl += Double.parseDouble(bean.getTotalChq());
			chqBounce += Double.parseDouble(bean.getCheqBounce());
			crNote += Double.parseDouble(bean.getCredit());
			dbNote += Double.parseDouble(bean.getDebit());
			netColl += Double.parseDouble(bean.getNetAmt());

			addLabel(sheet, 0, i + 10, bean.getName());
			addDoubleNumber(sheet, 1, i + 10, Double.parseDouble(bean
					.getTotalCash()));
			addDoubleNumber(sheet, 2, i + 10, Double.parseDouble(bean
					.getTotalChq()));
			addDoubleNumber(sheet, 3, i + 10, Double.parseDouble(bean
					.getCheqBounce()));
			addDoubleNumber(sheet, 4, i + 10, Double.parseDouble(bean
					.getCredit()));
			addDoubleNumber(sheet, 5, i + 10, Double.parseDouble(bean
					.getDebit()));
			addDoubleNumber(sheet, 6, i + 10, Double.parseDouble(bean
					.getNetAmt()));
		}

		sheet.addCell(new Label(0, length + 10, "Total", headingLabel));
		sheet
				.addCell(new Number(1, length + 10, cashColl,
						headingNumberFormat));
		sheet.addCell(new Number(2, length + 10, chqColl, headingNumberFormat));
		sheet
				.addCell(new Number(3, length + 10, chqBounce,
						headingNumberFormat));
		sheet.addCell(new Number(4, length + 10, crNote, headingNumberFormat));
		sheet.addCell(new Number(5, length + 10, dbNote, headingNumberFormat));
		sheet.addCell(new Number(6, length + 10, netColl, headingNumberFormat));

	}

	private void createHeaderForAgentGameWise(WritableSheet sheet)
			throws WriteException, ParseException {
		DateFormat format = new SimpleDateFormat("dd-MMM-yy");

		sheet.addCell(new Label(4, 0, " Date  :  " + format.format(new Date()),
				dateFormat));
		sheet.mergeCells(4, 0, 5, 0);
		setHeadingForSaleReport(sheet);
		sheet.mergeCells(0, 1, 5, 1);
		sheet.mergeCells(0, 2, 5, 2);

	}

	/**
	 * It create excel sheet for agent that contain retailer wise detail of cash
	 * & cheque transactions.
	 */

	private void createHeaderForCashChq(WritableSheet sheet)
			throws WriteException, ParseException {
		DateFormat format = new SimpleDateFormat("dd-MMM-yy");
		sheet.addCell(new Label(3, 0, " Date  :  " + format.format(new Date()),
				dateFormat));
		sheet.mergeCells(3, 0, 4, 0);
		setHeadingForCashChq(sheet);
		sheet.mergeCells(0, 1, 4, 1);
		sheet.mergeCells(0, 2, 4, 2);

	}

	private void createHeaderForPWT(WritableSheet sheet) throws WriteException,
			ParseException {
		DateFormat format = new SimpleDateFormat("dd-MMM-yy");
		sheet.addCell(new Label(1, 0, " Date  :  " + format.format(new Date()),
				dateFormat));
		setHeadingForPWT(sheet);
		sheet.mergeCells(0, 1, 1, 1);
		sheet.mergeCells(0, 2, 1, 2);
	}

	private void createHeaderForRetailer(WritableSheet sheet)
			throws WriteException, ParseException {
		DateFormat format = new SimpleDateFormat("dd-MMM-yy");

		sheet.addCell(new Label(2, 0, " Date  :  " + format.format(new Date()),
				dateFormat));
		sheet.mergeCells(2, 0, 3, 0);

		setHeadingForSaleReport(sheet);
		sheet.mergeCells(0, 1, 3, 1);
		sheet.mergeCells(0, 2, 3, 2);

	}

	/**
	 * this method is used to creating the excel file;
	 * 
	 */
	public boolean CreateXLSFile(
			Map<String, CompleteCollectionBean> dgScSalePwtRetWiseMap,
			List<CSSaleReportBean> csSaleRetWiseList,
			List<SalePwtReportsBean> drawSaleGameWiseListForAgent,
			List<SalePwtReportsBean> drawPwtGameWiseListForAgent,
			List<SalePwtReportsBean> scratchSaleGameWiseForAgentList,
			List<SalePwtReportsBean> scratchPwtGameWiseForAgentList,
			List<CSSaleReportBean> csSaleProductWiseForAgentList,
			SaleReportBean purchWithBO,
			List<SaleReportBean> purchWithBOGameWise,
			List<CashChqReportBean> cashChqWithRetlist,
			CashChqReportBean cashChqWithBoBean, boolean isDraw,
			boolean isScratch, boolean isCS, boolean isOLA) throws IOException,
			WriteException, ParseException {

		writeAgentExcelGameWise(drawSaleGameWiseListForAgent,
				drawPwtGameWiseListForAgent, scratchSaleGameWiseForAgentList,
				scratchPwtGameWiseForAgentList, csSaleProductWiseForAgentList,
				purchWithBOGameWise, isDraw, isScratch, isCS, isOLA);

		writeAgentExcelRetailerWise(dgScSalePwtRetWiseMap, csSaleRetWiseList,
				purchWithBO, isDraw, isScratch, isCS, isOLA);

		createCashChqAgentSheet(cashChqWithRetlist, cashChqWithBoBean);

		workbook.write();
		workbook.close();
		return true;
	}

	private void setFormatting() {
		try {
			numberFormat = new WritableCellFormat(NumberFormats.FORMAT3);
			numberFormat.setFont(new WritableFont(WritableFont.TIMES, 12));
			numberFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
			numberFormat.setWrap(false);

			times = new WritableCellFormat(new WritableFont(WritableFont.TIMES,
					12));
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
			dateFormat.setFont(new WritableFont(WritableFont.TIMES, 12,
					WritableFont.BOLD));
			dateFormat.setWrap(false);
			dateFormat.setAlignment(Alignment.RIGHT);

			headerFormat = new WritableCellFormat(new WritableFont(
					WritableFont.TIMES, 15));
			headerFormat.setWrap(false);
			headerFormat.setAlignment(Alignment.CENTRE);

			headerDateFormat = new WritableCellFormat(DateFormats.FORMAT4);
			headerDateFormat.setFont(new WritableFont(WritableFont.TIMES, 12,
					WritableFont.BOLD));
			headerDateFormat.setWrap(false);
			headerDateFormat.setAlignment(Alignment.CENTRE);

			headingLabel = new WritableCellFormat(new WritableFont(
					WritableFont.TIMES, 12));
			headingLabel.setWrap(false);
			headingLabel.setBorder(Border.ALL, BorderLineStyle.MEDIUM);
			// headingLabel.setAlignment(Alignment.LEFT);
			headingLabel.setBackground(Colour.GRAY_25);

			headingNumberFormat = new WritableCellFormat(NumberFormats.FORMAT3);
			headingNumberFormat
					.setFont(new WritableFont(WritableFont.TIMES, 12));
			headingNumberFormat.setWrap(false);
			headingNumberFormat.setBorder(Border.ALL, BorderLineStyle.MEDIUM);
			headingNumberFormat.setBackground(Colour.GRAY_25);
		} catch (WriteException e) {
			e.printStackTrace();
		}
	}

	private void setHeadingForCashChq(WritableSheet sheet)
			throws RowsExceededException, WriteException {
		DateFormat format = new SimpleDateFormat("dd-MMM-yy");
		if (this.endDate == null) {
			sheet.addCell(new Label(0, 1, reportType
					+ " Cash and Cheque Report", headerFormat));
			if (reportday == null) {
				sheet.addCell(new Label(0, 2, " ( "
						+ new SimpleDateFormat("MMM-yyyy").format(startDate)
						+ " ) ", headerDateFormat));
			} else {
				sheet.addCell(new Label(0, 2, " ( "
						+ new SimpleDateFormat("dd-MMM-yyyy").format(reportday)
						+ " ) ", headerDateFormat));
			}
		} else {
			sheet.addCell(new Label(0, 1, reportType
					+ " Cash and Cheque Report", headerFormat));
			sheet.addCell(new Label(0, 2, " ( " + format.format(startDate)
					+ "   -   " + format.format(endDate) + " ) ",
					headerDateFormat));
		}
	}

	private void setHeadingForPWT(WritableSheet sheet)
			throws RowsExceededException, WriteException {
		DateFormat format = new SimpleDateFormat("dd-MMM-yy");
		if (this.endDate == null) {
			sheet.addCell(new Label(0, 1, " PWT Report Detail", headerFormat));
			if (reportday == null) {
				sheet.addCell(new Label(0, 2, " ( "
						+ new SimpleDateFormat("MMM-yyyy").format(startDate)
						+ " ) ", headerDateFormat));
			} else {
				sheet.addCell(new Label(0, 2, " ( "
						+ new SimpleDateFormat("dd-MMM-yyyy").format(reportday)
						+ " ) ", headerDateFormat));
			}
		} else {
			sheet.addCell(new Label(0, 1, reportType + " PWT Report",
					headerFormat));
			sheet.addCell(new Label(0, 2, " ( " + format.format(startDate)
					+ "   -   " + format.format(endDate) + " ) ",
					headerDateFormat));
		}
	}

	private void setHeadingForSaleReport(WritableSheet sheet)
			throws RowsExceededException, WriteException {
		DateFormat format = new SimpleDateFormat("dd-MMM-yy");
		if (this.endDate == null) {
			sheet.addCell(new Label(0, 1, reportType + " Sale Report",
					headerFormat));
			if (reportday == null) {
				sheet.addCell(new Label(0, 2, " ( "
						+ new SimpleDateFormat("MMM-yyyy").format(startDate)
						+ " ) ", headerDateFormat));
			} else {
				sheet.addCell(new Label(0, 2, " ( "
						+ new SimpleDateFormat("dd-MMM-yyyy").format(reportday)
						+ " ) ", headerDateFormat));
			}
		} else {
			sheet.addCell(new Label(0, 1, reportType + " Sale Report",
					headerFormat));
			sheet.addCell(new Label(0, 2, " ( " + format.format(startDate)
					+ "   -   " + format.format(endDate) + " ) ",
					headerDateFormat));
		}
	}

	public void writeAgentExcelGameWise(
			List<SalePwtReportsBean> drawSaleGameWiseListForAgent,
			List<SalePwtReportsBean> drawPwtGameWiseListForAgent,
			List<SalePwtReportsBean> scratchSaleGameWiseForAgentList,
			List<SalePwtReportsBean> scratchPwtGameWiseForAgentList,
			List<CSSaleReportBean> csSaleProductWiseForAgentList,
			List<SaleReportBean> purchWithBO, boolean isDraw,
			boolean isScratch, boolean isCS, boolean isOLA) throws IOException,
			WriteException, ParseException {

		WritableSheet excelSheet = workbook.getSheet(0); // create Header
		createHeaderForAgentGameWise(excelSheet); // create Caption

		int col1count = 0;
		int colForService = 2;
		int row1count = 6;
		if (isScratch) {
			addCaption(excelSheet, colForService, 6, "SCRATCH");
			row1count += 2;
			addCaption(excelSheet, col1count, row1count,
					"Scratch Game Sale/Winning Details with Retailer");

			row1count += 2;

			addCaption(excelSheet, col1count, row1count, "Game Name");
			col1count += 1;
			addCaption(excelSheet, col1count, row1count, "Book Price Amount ");
			col1count += 1;
			addCaption(excelSheet, col1count, row1count, "Number Of Books");
			col1count += 1;
			addCaption(excelSheet, col1count, row1count,
					"Number Of Loose Tickets");
			col1count += 1;
			addCaption(excelSheet, col1count, row1count, "Sale Mrp Amount");
			col1count += 1;
			addCaption(excelSheet, col1count, row1count, "Sale Net Amount");
			col1count += 1;
			addCaption(excelSheet, col1count, row1count, "Pwt Mrp Amount");
			col1count += 1;
			addCaption(excelSheet, col1count, row1count, "Pwt Net Amount");
			row1count += 1;

			Iterator<SalePwtReportsBean> scratchSaleIterator = scratchSaleGameWiseForAgentList
					.iterator();

			SalePwtReportsBean saleForScratch = null;
			SalePwtReportsBean pwtForScratch = null;
			while (scratchSaleIterator.hasNext()) {
				col1count = 0;
				double scratchMrpPwt = 0.0;
				double scratchNetPwt = 0.0;
				saleForScratch = scratchSaleIterator.next();
				String gameName = saleForScratch.getGameName();
				addCaption(excelSheet, col1count, row1count, gameName);
				col1count += 1;
				addNumber(excelSheet, col1count, row1count, saleForScratch
						.getUnitPriceAmt());
				col1count += 1;
				addNumber(excelSheet, col1count, row1count, saleForScratch
						.getNoOfTkt());
				col1count += 1;
				addNumber(excelSheet, col1count, row1count, saleForScratch
						.getNoOfLosTkt());
				col1count += 1;
				addNumber(excelSheet, col1count, row1count, saleForScratch
						.getSaleMrpAmt());
				col1count += 1;
				addNumber(excelSheet, col1count, row1count, saleForScratch
						.getSaleNetAmt());
				col1count += 1;

				Iterator<SalePwtReportsBean> scratchPwtIterator = scratchPwtGameWiseForAgentList
						.iterator();
				while (scratchPwtIterator.hasNext()) {
					pwtForScratch = scratchPwtIterator.next();
					if (pwtForScratch.getGameName().equalsIgnoreCase(gameName)) {
						scratchMrpPwt += pwtForScratch.getPwtMrpAmt();
						scratchNetPwt += pwtForScratch.getPwtNetAmt();
					}
				}
				addNumber(excelSheet, col1count, row1count, scratchMrpPwt);
				col1count += 1;
				addNumber(excelSheet, col1count, row1count, scratchNetPwt);
				row1count += 1;
			}
		}
		int serialNo = 0;
		SalePwtReportsBean saleForDraw = null;
		SalePwtReportsBean pwtForDraw = null;
		if (isDraw) {
			col1count = 0;
			row1count += 2;
			addCaption(excelSheet, colForService, row1count, "DRAW");
			row1count += 2;
			addCaption(excelSheet, col1count, row1count,
					"Draw Game Sale/Winning Details with Retailer");
			row1count += 2;
			addCaption(excelSheet, col1count, row1count, "SNo.");
			col1count += 1;
			addCaption(excelSheet, col1count, row1count, "Game Name");
			col1count += 1;
			addCaption(excelSheet, col1count, row1count, "Sale Amount");
			col1count += 1;
			addCaption(excelSheet, col1count, row1count, "Winning Amount");
			row1count += 1;
			if (drawPwtGameWiseListForAgent.isEmpty()
					&& drawSaleGameWiseListForAgent.isEmpty()) {
				row1count += 1;
				col1count = 0;
				addCaption(excelSheet, col1count, row1count, " ");
				col1count += 1;
				addCaption(excelSheet, col1count, row1count, " ");
				col1count += 1;
				addNumber(excelSheet, col1count, row1count, 0.0);
				col1count += 1;
				addNumber(excelSheet, col1count, row1count, 0.0);

			} else if (!drawSaleGameWiseListForAgent.isEmpty()
					&& drawPwtGameWiseListForAgent.isEmpty()) {
				col1count = 0;

				Iterator<SalePwtReportsBean> drawSaleIterator = drawSaleGameWiseListForAgent
						.iterator();

				while (drawSaleIterator.hasNext()) {
					serialNo += 1;
					col1count = 0;
					saleForDraw = drawSaleIterator.next();
					String gameName = saleForDraw.getGameName();

					addCaption(excelSheet, col1count, row1count, String
							.valueOf(serialNo));
					col1count += 1;
					addCaption(excelSheet, col1count, row1count, gameName);
					col1count += 1;
					addNumber(excelSheet, col1count, row1count, saleForDraw
							.getSaleMrpAmt());
					col1count += 1;
					addNumber(excelSheet, col1count, row1count, 0.0);
					row1count += 1;

				}
			} else if (!drawPwtGameWiseListForAgent.isEmpty()
					&& drawSaleGameWiseListForAgent.isEmpty()) {
				col1count = 0;
				serialNo += 1;
				Iterator<SalePwtReportsBean> drawPwtIterator = drawPwtGameWiseListForAgent
						.iterator();

				while (drawPwtIterator.hasNext()) {
					col1count = 0;
					pwtForDraw = drawPwtIterator.next();

					addCaption(excelSheet, col1count, row1count, String
							.valueOf(serialNo));
					col1count += 1;
					addCaption(excelSheet, col1count, row1count, pwtForDraw
							.getGameName());
					col1count += 1;
					addNumber(excelSheet, col1count, row1count, 0.0);
					col1count += 1;
					addNumber(excelSheet, col1count, row1count, pwtForDraw
							.getPwtMrpAmt());
					row1count += 1;

				}
			} else {
				Iterator<SalePwtReportsBean> drawSaleIterator = drawSaleGameWiseListForAgent
						.iterator();
				List<String> gameList = new ArrayList<String>();
				Iterator<SalePwtReportsBean> drawSaleForList = drawSaleGameWiseListForAgent
						.iterator();
				while (drawSaleForList.hasNext()) {
					gameList.add(drawSaleForList.next().getGameName());
				}

				List<String> pwtGameList = new ArrayList<String>();
				Iterator<SalePwtReportsBean> drawPwtForList = drawPwtGameWiseListForAgent
						.iterator();
				while (drawPwtForList.hasNext()) {
					pwtGameList.add(drawPwtForList.next().getGameName());
				}

				List<String> sapratePwtList = new ArrayList<String>();
				List<String> saprateSaleList = new ArrayList<String>();

				for (int i = 0; i < gameList.size(); i++) {
					if (!pwtGameList.contains(gameList.get(i))) {
						saprateSaleList.add(gameList.get(i));
					}
				}
				for (int i = 0; i < pwtGameList.size(); i++) {
					if (!gameList.contains(pwtGameList.get(i))) {
						sapratePwtList.add(pwtGameList.get(i));
					}
				}

				while (drawSaleIterator.hasNext()) {
					col1count = 0;
					saleForDraw = drawSaleIterator.next();
					String gameName = saleForDraw.getGameName();
					Iterator<SalePwtReportsBean> drawPwtIterator = drawPwtGameWiseListForAgent
							.iterator();
					while (drawPwtIterator.hasNext()) {
						pwtForDraw = drawPwtIterator.next();
						String pwtGameName = pwtForDraw.getGameName();
						if (pwtGameName.equalsIgnoreCase(gameName)) {
							serialNo+=1;
							addCaption(excelSheet, col1count, row1count, String
									.valueOf(serialNo));
							col1count += 1;
							addCaption(excelSheet, col1count, row1count, gameName);
							col1count += 1;
							addNumber(excelSheet, col1count, row1count, saleForDraw
									.getSaleMrpAmt());
							col1count += 1;

							addNumber(excelSheet, col1count, row1count,
									pwtForDraw.getPwtMrpAmt());
							row1count += 1;
							break;
						}
						
					}
					
				}
				if (!saprateSaleList.isEmpty()) {
					Iterator<String> drawSaleIteratorForUnmatched = saprateSaleList
							.iterator();
					while (drawSaleIteratorForUnmatched.hasNext()) {
						String gameName = drawSaleIteratorForUnmatched.next();
						Iterator<SalePwtReportsBean> drawSaleIteratorForUnmatchedValues = drawSaleGameWiseListForAgent
								.iterator();
						while (drawSaleIteratorForUnmatchedValues.hasNext()) {
							SalePwtReportsBean temp = drawSaleIteratorForUnmatchedValues
									.next();
							if (temp.getGameName().equalsIgnoreCase(gameName)) {
								serialNo += 1;
								col1count = 0;
								
								addCaption(excelSheet, col1count, row1count,
										String.valueOf(serialNo));
								col1count += 1;
								addCaption(excelSheet, col1count, row1count,
										temp.getGameName());
								col1count += 1;
								addNumber(excelSheet, col1count, row1count,
										temp.getSaleMrpAmt());
								col1count += 1;
								addNumber(excelSheet, col1count, row1count, 0.0);
								row1count += 1;

							}

						}
					}
				}
				if (!sapratePwtList.isEmpty()) {
					Iterator<String> drawPwtIteratorForUnmatched = sapratePwtList
							.iterator();
					while (drawPwtIteratorForUnmatched.hasNext()) {
						String pwtGameName = drawPwtIteratorForUnmatched.next();
						Iterator<SalePwtReportsBean> drawPwtIteratorForUnmatchedValues = drawPwtGameWiseListForAgent
								.iterator();
						while (drawPwtIteratorForUnmatchedValues.hasNext()) {
							SalePwtReportsBean temp = drawPwtIteratorForUnmatchedValues
									.next();
							if (temp.getGameName()
									.equalsIgnoreCase(pwtGameName)) {
								serialNo += 1;
								col1count = 0;
								row1count += 1;
								addCaption(excelSheet, col1count, row1count,
										String.valueOf(serialNo));
								col1count += 1;
								addCaption(excelSheet, col1count, row1count,
										temp.getGameName());
								col1count += 1;
								addNumber(excelSheet, col1count, row1count, 0.0);
								col1count += 1;
								addNumber(excelSheet, col1count, row1count,
										temp.getPwtMrpAmt());
							}
						}
					}
				}
			}

		}

		serialNo = 0;

		if (isCS) {
			serialNo += 1;
			col1count = 0;
			row1count += 2;
			addCaption(excelSheet, colForService, row1count, "COMMERCIAL SERVICES");
			row1count += 2;
			addCaption(excelSheet, col1count, row1count,
					"Commercial Service Details with Retailer");
			row1count += 2;
			addCaption(excelSheet, col1count, row1count, "SNo.");
			col1count += 1;
			addCaption(excelSheet, col1count, row1count, "Category");
			col1count += 1;
			addCaption(excelSheet, col1count, row1count, "Sale");
			col1count += 1;
			addCaption(excelSheet, col1count, row1count, "Buy Cost");
			row1count += 1;
			if (!csSaleProductWiseForAgentList.isEmpty()) {

				Iterator<CSSaleReportBean> csIterator = csSaleProductWiseForAgentList
						.iterator();
				while (csIterator.hasNext()) {
					col1count = 0;
					CSSaleReportBean csSaleebean = csIterator.next();
					addCaption(excelSheet, col1count, row1count, String
							.valueOf(serialNo));
					col1count += 1;
					addCaption(excelSheet, col1count, row1count, csSaleebean
							.getCategoryCode());
					col1count += 1;
					addCaption(excelSheet, col1count, row1count, String
							.valueOf(csSaleebean.getMrpAmt()));
					col1count += 1;
					addCaption(excelSheet, col1count, row1count, String
							.valueOf(csSaleebean.getBuyCost()));

				}
			} else {
				col1count = 0;
				addCaption(excelSheet, col1count, row1count, "");
				col1count += 1;
				addCaption(excelSheet, col1count, row1count, "");
				col1count += 1;
				addCaption(excelSheet, col1count, row1count, "0.0");
				col1count += 1;
				addCaption(excelSheet, col1count, row1count, "0.0");
			}
		}
		if (isOLA) {

			// if(!csSaleProductWiseForAgentList.isEmpty()){}
		}

	}

	public void writeAgentExcelRetailerWise(
			Map<String, CompleteCollectionBean> dgScSalePwtRetWiseMap,
			List<CSSaleReportBean> csSaleRetWiseList,
			SaleReportBean purchWithBO, boolean isDraw, boolean isScratch,
			boolean isCS, boolean isOLA) throws IOException, WriteException,
			ParseException {
		WritableSheet excelSheet = workbook.getSheet(1);
		// creating header
		createHeaderForRetailer(excelSheet);
		// create Caption
		addCaption(excelSheet, 0, 4, "Purchased Details From BO");
		excelSheet.mergeCells(0, 4, 2, 4);

		addCaption(excelSheet, 0, 5, "Purchased Amount");
		addCaption(excelSheet, 1, 5, "Return Amount");
		addCaption(excelSheet, 2, 5, "Net Purchased");
		if (purchWithBO != null) {
			addDoubleNumber(excelSheet, 0, 6, Double.parseDouble(purchWithBO
					.getSaleAmt()));
			addDoubleNumber(excelSheet, 1, 6, Double.parseDouble(purchWithBO
					.getReturnAmt()));
			addDoubleNumber(excelSheet, 2, 6, (Double.parseDouble(purchWithBO
					.getSaleAmt()) - Double.parseDouble(purchWithBO
					.getReturnAmt())));
		} else {
			addDoubleNumber(excelSheet, 0, 6, 0.0);
			addDoubleNumber(excelSheet, 1, 6, 0.0);
			addDoubleNumber(excelSheet, 2, 6, 0.0);
		}
		int col1Count = 0;
		int row1Count = 8;
		int col2Count = 0;
		int row2Count = 8;
		addCaption(excelSheet, col1Count, row1Count,
				"Sale Details with Retailer");
		excelSheet.mergeCells(col1Count, row1Count, col1Count + 3, row1Count);
		row1Count += 2;
		addCaption(excelSheet, col1Count, row1Count, "Party Name");
		row2Count += 2;
		excelSheet.mergeCells(col2Count, row1Count, col2Count, row2Count + 1);
		col1Count += 1;
		if (isDraw) {
			col2Count += 2;
			addCaption(excelSheet, col1Count, row1Count, "DRAW");
			excelSheet.mergeCells(col1Count, row1Count, col2Count, row2Count);
			addCaption(excelSheet, col1Count, row1Count + 1, "Sale");
			col1Count += 1;
			addCaption(excelSheet, col1Count, row1Count + 1, "Pwt");
			col1Count += 1;
			col2Count += 2;
		}
		if (isScratch) {
			addCaption(excelSheet, col1Count, row1Count, "SCRATCH");
			excelSheet.mergeCells(col1Count, row1Count, col2Count, row2Count);
			addCaption(excelSheet, col1Count, row1Count + 1, "Sale");
			col1Count += 1;
			addCaption(excelSheet, col1Count, row1Count + 1, "Pwt");
			col1Count += 1;
			col2Count += 2;
		}
		if (isCS) {
			addCaption(excelSheet, col1Count, row1Count, "CS");
			excelSheet.mergeCells(col1Count, row1Count, col2Count, row2Count);
			addCaption(excelSheet, col1Count, row1Count + 1, "Sale");
			col1Count += 1;
			addCaption(excelSheet, col1Count, row1Count + 1, "Pwt");
			col1Count += 1;
			col2Count += 2;
		}
		if (isOLA) {
			addCaption(excelSheet, col1Count, row1Count, "OLA");
			excelSheet.mergeCells(col1Count, row1Count, col2Count + 1,
					row2Count);
			addCaption(excelSheet, col1Count, row1Count + 1, "Deposit");
			col1Count += 1;
			addCaption(excelSheet, col1Count, row1Count + 1, "Withdraw");
			col1Count += 1;
			addCaption(excelSheet, col1Count, row1Count + 1, "Net Gamming");
		}

		col2Count = 3;
		row1Count += 2;

		Set<Map.Entry<String, CompleteCollectionBean>> set = dgScSalePwtRetWiseMap
				.entrySet();
		Iterator<Entry<String, CompleteCollectionBean>> it = set.iterator();
		while (it.hasNext()) {
			col1Count = 0;
			Map.Entry<String, CompleteCollectionBean> entry = (Map.Entry<String, CompleteCollectionBean>) it
					.next();
			CompleteCollectionBean bean = entry.getValue();
			addCaption(excelSheet, col1Count, row1Count, bean.getOrgName());
			col1Count += 1;
			if (isDraw) {
				addDoubleNumber(excelSheet, col1Count, row1Count, bean
						.getDrawSale()
						- bean.getDrawCancel());
				col1Count += 1;
				addDoubleNumber(excelSheet, col1Count, row1Count, bean
						.getDrawPwt());
			}
			if (isScratch) {
				col1Count += 1;
				addDoubleNumber(excelSheet, col1Count, row1Count, bean
						.getScratchSale());
				col1Count += 1;
				addDoubleNumber(excelSheet, col1Count, row1Count, bean
						.getScratchPwt());
			}
			if (isCS) {
				col1Count += 1;
				addDoubleNumber(excelSheet, col1Count, row1Count, bean
						.getCSSale());
				col1Count += 1;
				addDoubleNumber(excelSheet, col1Count, row1Count, bean
						.getCSCancel());
			}
			if (isOLA) {
				col1Count += 1;
				addDoubleNumber(excelSheet, col1Count, row1Count, bean
						.getOlaDepositAmt()
						- bean.getOlaDepositCancelAmt());
				col1Count += 1;
				addDoubleNumber(excelSheet, col1Count, row1Count, bean
						.getOlaWithdrawalAmt());
				col1Count += 1;
				addDoubleNumber(excelSheet, col1Count, row1Count, bean
						.getOlaNetGaming());
			}
			row1Count += 1;
		}

	}

	/*
	 * private void writePwtReportSheet(List<PwtReportBean> reportlist) throws
	 * IOException, WriteException, ParseException { WritableSheet sheet =
	 * workbook.getSheet(3); createHeaderForPWT(sheet);
	 * 
	 * // create Caption addPWTCaption(sheet, 0, 4, "Party Name");
	 * addPWTCaption(sheet, 1, 4, "Total PWT Amount");
	 * 
	 * double totalPwtAmt = 0.0; int length = reportlist.size(); PwtReportBean
	 * bean = null; for (int i = 0; i < length; i++) { bean = reportlist.get(i);
	 * if (bean != null) { addLabel(sheet, 0, i + 5, bean.getPartyName());
	 * addDoubleNumber(sheet, 1, i + 5, Double.parseDouble(bean .getPwtAmt()));
	 * totalPwtAmt += Double.parseDouble(bean.getPwtAmt()); } }
	 * sheet.addCell(new Label(0, length + 5, "Total", headingLabel));
	 * sheet.addCell(new Number(1, length + 5, totalPwtAmt,
	 * headingNumberFormat)); }
	 */

}
