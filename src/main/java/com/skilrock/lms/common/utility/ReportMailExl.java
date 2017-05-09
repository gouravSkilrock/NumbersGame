package com.skilrock.lms.common.utility;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.record.MergeCellsRecord;

import com.skilrock.lms.beans.CSSaleReportBean;
import com.skilrock.lms.beans.CashChqReportBean;
import com.skilrock.lms.beans.CompleteCollectionBean;
import com.skilrock.lms.beans.DateBeans;
import com.skilrock.lms.beans.PwtReportBean;
import com.skilrock.lms.beans.SalePwtReportsBean;
import com.skilrock.lms.beans.SaleReportBean;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;

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

public class ReportMailExl {

	static Log logger = LogFactory.getLog(ReportMailExl.class);

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

	public ReportMailExl(String fileName, DateBeans dateBeans)
			throws IOException, WriteException {
		WorkbookSettings wbSettings = new WorkbookSettings();
		wbSettings.setLocale(new Locale("en", "EN"));
		this.reportType = dateBeans.getReportType();
		this.startDate = dateBeans.getFirstdate();
		this.endDate = dateBeans.getLastdate();
		this.reportday = dateBeans.getReportday();

		System.out.println("file object ----------- " + new File(fileName));
		workbook = Workbook.createWorkbook(new File(fileName));
		System.out.println("work book object ------- " + workbook);
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
		System.out.println("passed String : " + s);
		label = new Label(column, row, s.toString(), times);
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

	private void createHeaderForAgent(WritableSheet sheet)
			throws WriteException, ParseException {
		DateFormat format = new SimpleDateFormat("dd-MMM-yy");

		sheet.addCell(new Label(2, 0, " Date  :  " + format.format(new Date()),
				dateFormat));
		sheet.mergeCells(2, 0, 3, 0);

		setHeadingForSaleReport(sheet);
		sheet.mergeCells(0, 1, 3, 1);
		sheet.mergeCells(0, 2, 3, 2);

	}

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

	private void createHeaderForGame(WritableSheet sheet)
			throws WriteException, ParseException {
		DateFormat format = new SimpleDateFormat("dd-MMM-yy");

		sheet.addCell(new Label(4, 0, " Date  :  " + format.format(new Date()),
				dateFormat));
		sheet.mergeCells(4, 0, 5, 0);

		setHeadingForSaleReport(sheet);

		sheet.mergeCells(0, 1, 5, 1);
		sheet.mergeCells(0, 2, 5, 2);

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

	/**
	 * this method is used to creating the excel file;
	 * 
	 * @param gamesalelist
	 * @param agtsalelist
	 * @param pwtlist
	 * @param cashlist
	 * @return
	 * @throws IOException
	 * @throws WriteException
	 */
	public boolean CreateXLSFile(
			Map<String, CompleteCollectionBean> reportMapAgentWise,
			List<SalePwtReportsBean> drawSaleReportList,
			List<SalePwtReportsBean> drawPwtReportList,
			List<SalePwtReportsBean> scratchSaleReportList,
			List<SalePwtReportsBean> scratchPwtReportList,
			List<CSSaleReportBean> csSaleReportList,
			List<CashChqReportBean> cashlist, double boDirPlrPwtForDraw,
			double boDirPlrPwtForScratch) throws IOException, WriteException,
			ParseException {

		boolean isDraw = LMSFilterDispatcher.getIsDraw()
				.equalsIgnoreCase("yes") ? true : false;
		boolean isScratch = LMSFilterDispatcher.getIsScratch()
				.equalsIgnoreCase("yes") ? true : false;
		boolean isCS = LMSFilterDispatcher.getIsCS().equalsIgnoreCase("yes") ? true
				: false;
		boolean isOLA = LMSFilterDispatcher.getIsOLA().equalsIgnoreCase("yes") ? true
				: false;

		writeAgent(reportMapAgentWise, isDraw, isScratch, isCS, isOLA);
		writeGame(drawSaleReportList, drawPwtReportList, scratchSaleReportList,
				scratchPwtReportList, csSaleReportList, isDraw, isScratch,
				isCS, isOLA, boDirPlrPwtForDraw, boDirPlrPwtForScratch);
		writeCashChqReportSheet(cashlist);
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
					WritableFont.TIMES, 12, WritableFont.BOLD, false,
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
			System.out
					.println(" inside date if condition -kgkgjdkjkjkfj---- ----user Type : "
							+ this.reportType + " date : " + startDate);
			sheet.addCell(new Label(0, 1, " Cash and Cheque Report Detail",
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
			System.out
					.println(" inside date else condition ----- ----user Type : "
							+ reportType
							+ " date : "
							+ startDate
							+ " end date : " + endDate);
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
			System.out
					.println(" inside date if condition -kgkgjdkjkjkfj---- ----user Type : "
							+ this.reportType + " date : " + startDate);
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
			System.out
					.println(" inside date else condition ----- ----user Type : "
							+ reportType
							+ " date : "
							+ startDate
							+ " end date : " + endDate);
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
			System.out
					.println(" inside date if condition -kgkgjdkjkjkfj---- ----user Type : "
							+ this.reportType + " date : " + startDate);
			sheet.addCell(new Label(0, 1, " Sale Report Detail", headerFormat));
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
			System.out
					.println(" inside date else condition ----- ----user Type : "
							+ reportType
							+ " date : "
							+ startDate
							+ " end date : " + endDate);

			sheet.addCell(new Label(0, 1, reportType + " Sale Report",
					headerFormat));
			sheet.addCell(new Label(0, 2, " ( " + format.format(startDate)
					+ "   -   " + format.format(endDate) + " ) ",
					headerDateFormat));
		}
	}

	public void writeAgent(
			Map<String, CompleteCollectionBean> reportMapAgentWise,
			boolean isDraw, boolean isScratch, boolean isCS, boolean isOLA)
			throws IOException, WriteException, ParseException {

		WritableSheet excelSheet = workbook.getSheet(1);
		// creating header
		createHeaderForAgent(excelSheet);
		addCaption(excelSheet, 3, 4, "PARTY NAME");
		excelSheet.mergeCells(3, 4, 3, 5);

		int col1Count = 4;
		int row1Count = 4;
		int col2Count = 5;
		int row2Count = 4;

		if (isDraw) {
			addCaption(excelSheet, col1Count, row1Count, "DRAW");
			excelSheet.mergeCells(col1Count, row1Count, col2Count, row2Count);
			col1Count += 2;
			col2Count += 2;
		}
		if (isScratch) {
			addCaption(excelSheet, col1Count, row1Count, "SCRATCH");
			excelSheet.mergeCells(col1Count, row1Count, col2Count, row2Count);
			col1Count += 2;
			col2Count += 2;
		}
		if (isCS) {
			addCaption(excelSheet, col1Count, row1Count, "CS");
			excelSheet.mergeCells(col1Count, row1Count, col2Count, row2Count);
			col1Count += 2;
			col2Count += 2;
		}
		if (isOLA) {

			addCaption(excelSheet, col1Count, row1Count, "OLA");
			excelSheet.mergeCells(col1Count, row1Count, col2Count, row2Count);
			col1Count += 2;
			col2Count += 2;
		}

		col1Count += 1;
		col2Count += 2;
		col1Count = 4;
		row1Count = 5;
		if (isDraw) {
			addCaption(excelSheet, col1Count, row1Count, "Sale");
			col1Count += 1;
			addCaption(excelSheet, col1Count, row1Count, "Pwt");
			col1Count += 1;
		}
		if (isScratch) {
			addCaption(excelSheet, col1Count, row1Count, "Sale");
			col1Count += 1;
			addCaption(excelSheet, col1Count, row1Count, "Pwt");
			col1Count += 1;
		}
		if (isCS) {
			addCaption(excelSheet, col1Count, row1Count, "Sale");
			col1Count += 1;
			addCaption(excelSheet, col1Count, row1Count, "Return");
			col1Count += 1;
		}
		if (isOLA) {
			addCaption(excelSheet, col1Count, row1Count, "Deposit");
			col1Count += 1;
			addCaption(excelSheet, col1Count, row1Count, "Withdraw");
			col1Count += 1;
			addCaption(excelSheet, col1Count, row1Count, "Net Gamming");
			col1Count += 1;
		}

		col2Count = 3;
		row1Count = 6;

		Set<Map.Entry<String, CompleteCollectionBean>> set = reportMapAgentWise
				.entrySet();
		Iterator<Entry<String, CompleteCollectionBean>> it = set.iterator();
		while (it.hasNext()) {
			col1Count = 3;
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
						.getDrawPwt()
						+ bean.getDrawDirPlyPwt());
			}
			if (isScratch) {
				col1Count += 1;
				addDoubleNumber(excelSheet, col1Count, row1Count, bean
						.getScratchSale());
				col1Count += 1;
				addDoubleNumber(excelSheet, col1Count, row1Count, bean
						.getScratchPwt()
						+ bean.getScratchDirPlyPwt());
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

	private void writeCashChqReportSheet(List<CashChqReportBean> reportlist)
			throws IOException, WriteException, ParseException {

		WritableSheet sheet = workbook.getSheet(2);

		createHeaderForCashChq(sheet);

		// add caption
		addCaption(sheet, 0, 4, "Party Name");
		addCaption(sheet, 1, 4, "Cash Collection");
		addCaption(sheet, 2, 4, "Cheque Collection");
		addCaption(sheet, 3, 4, "Cheque Bounce");
		addCaption(sheet, 4, 4, "Credit Ammount");
		addCaption(sheet, 5, 4, "Debit Ammount");
		addCaption(sheet, 6, 4, "Bank Deposit");
		addCaption(sheet, 7, 4, "Net Collection");

		double cashColl = 0.0;
		double chqColl = 0.0;
		double chqBounce = 0.0;
		double creditCol = 0.0;
		double debitCol = 0.0;
		double netColl = 0.0;
		double bankDep = 0.0;
		int length = 0;
		if (reportlist != null) {
			length = reportlist.size();
			CashChqReportBean bean = null;
			for (int i = 0; i < length; i++) {
				bean = reportlist.get(i);

				cashColl += Double.parseDouble(bean.getTotalCash());
				chqColl += Double.parseDouble(bean.getTotalChq());
				chqBounce += Double.parseDouble(bean.getCheqBounce());
				netColl += Double.parseDouble(bean.getNetAmt());
				creditCol += Double.parseDouble(bean.getCredit());
				debitCol += Double.parseDouble(bean.getDebit());
				bankDep+=Double.parseDouble(bean.getBankDeposit());

				addCaption(sheet, 0, i + 5, bean.getName());
				addDoubleNumber(sheet, 1, i + 5, Double.parseDouble(bean
						.getTotalCash()));
				addDoubleNumber(sheet, 2, i + 5, Double.parseDouble(bean
						.getTotalChq()));
				addDoubleNumber(sheet, 3, i + 5, Double.parseDouble(bean
						.getCheqBounce()));
				addDoubleNumber(sheet, 4, i + 5, Double.parseDouble(bean
						.getCredit()));
				addDoubleNumber(sheet, 5, i + 5, Double.parseDouble(bean
						.getDebit()));

				addDoubleNumber(sheet, 6, i + 5, Double.parseDouble(bean
						.getBankDeposit()));
				addDoubleNumber(sheet, 7, i + 5, Double.parseDouble(bean
						.getNetAmt()));
			}
		}
		sheet.addCell(new Label(0, length + 5, "Total", headingLabel));
		sheet.addCell(new Number(1, length + 5, cashColl, headingNumberFormat));
		sheet.addCell(new Number(2, length + 5, chqColl, headingNumberFormat));
		sheet
				.addCell(new Number(3, length + 5, chqBounce,
						headingNumberFormat));
		sheet
				.addCell(new Number(4, length + 5, creditCol,
						headingNumberFormat));
		sheet.addCell(new Number(5, length + 5, debitCol, headingNumberFormat));
		sheet.addCell(new Number(6, length + 5, bankDep, headingNumberFormat));
		sheet.addCell(new Number(7, length + 5, netColl, headingNumberFormat));

	}

	public void writeGame(List<SalePwtReportsBean> drawSaleReportList,
			List<SalePwtReportsBean> drawPwtReportList,
			List<SalePwtReportsBean> scratchSaleReportList,
			List<SalePwtReportsBean> scratchPwtReportList,
			List<CSSaleReportBean> csSaleReportList, boolean isDraw,
			boolean isScratch, boolean isCS, boolean isOLA,
			double boDirPlrPwtForDraw, double boDirPlrPwtForScratch)
			throws IOException, WriteException, ParseException {

		WritableSheet excelSheet = workbook.getSheet(0);
		// create Header
		createHeaderForGame(excelSheet);
		// create Caption

		int row1Count = 8;
		int colForService = 4;
		if (isDraw) {
			 
			if (!drawSaleReportList.isEmpty()) {
				addCaption(excelSheet, colForService, 6, "DRAW");
				addCaption(excelSheet, 4, row1Count, "SNo.");
				addCaption(excelSheet, 5, row1Count, "GAME NAME");
				addCaption(excelSheet, 6, row1Count, "SALE AMOUNT");
				addCaption(excelSheet, 7, row1Count, "WINNING AMOUNT");
				int serialCount = 1;
				row1Count += 1;
				addCaption(excelSheet, 4, row1Count,
						"Direct Player Pwt At Back Office For Draw");
				excelSheet.mergeCells(4, row1Count, 6, row1Count);
				addDoubleNumber(excelSheet, 7, row1Count, boDirPlrPwtForDraw);

				row1Count += 1;

				Iterator<SalePwtReportsBean> saleItr = drawSaleReportList
						.iterator();
				while (saleItr.hasNext()) {
					double matchedPwt = 0.0;
					int col1Count = 4;

					SalePwtReportsBean saleBean = saleItr.next();

					addCaption(excelSheet, col1Count, row1Count,
							String.valueOf(serialCount));
					col1Count += 1;
					addCaption(excelSheet, col1Count, row1Count, saleBean
							.getGameName());
					col1Count += 1;
					addDoubleNumber(excelSheet, col1Count, row1Count, saleBean
							.getSaleMrpAmt());
					col1Count += 1;
					Iterator<SalePwtReportsBean> pwtItr = drawPwtReportList
							.iterator();
					while (pwtItr.hasNext()) {
						SalePwtReportsBean pwtBean = pwtItr.next();
						if (saleBean.getGameName().equalsIgnoreCase(
								pwtBean.getGameName())) {
							matchedPwt = pwtBean.getPwtMrpAmt();
							break;
						}
					}
					addDoubleNumber(excelSheet, col1Count, row1Count,
							matchedPwt);
					row1Count += 1;
					serialCount += 1;
				}
			}

		}

		if (isScratch) {
			double matchedPwt = 0.0;
			if (!scratchSaleReportList.isEmpty()) {
				int serialCount = 1;
				row1Count += 1;
				addCaption(excelSheet, colForService, row1Count, "SCRATCH");
				row1Count += 2;
				addCaption(excelSheet, 4, row1Count, "SNo.");
				addCaption(excelSheet, 5, row1Count, "GAME NAME");
				addCaption(excelSheet, 6, row1Count, "SALE AMOUNT");
				addCaption(excelSheet, 7, row1Count, "WINNING AMOUNT");
				row1Count += 1;

				addCaption(excelSheet, 4, row1Count,
						"Direct Player Pwt At Back Office For Scratch");
				excelSheet.mergeCells(4, row1Count, 6, row1Count);
				addDoubleNumber(excelSheet, 7, row1Count, boDirPlrPwtForScratch);

				row1Count += 1;
				Iterator<SalePwtReportsBean> saleItr = scratchSaleReportList
						.iterator();
				while (saleItr.hasNext()) {
					int col1Count = 4;

					SalePwtReportsBean saleBean = saleItr.next();

					addCaption(excelSheet, col1Count, row1Count,
							String.valueOf(serialCount));
					col1Count += 1;
					addCaption(excelSheet, col1Count, row1Count, saleBean
							.getGameName());
					col1Count += 1;
					addDoubleNumber(excelSheet, col1Count, row1Count, saleBean
							.getSaleMrpAmt());
					col1Count += 1;
					Iterator<SalePwtReportsBean> pwtItr = scratchPwtReportList
							.iterator();
					while (pwtItr.hasNext()) {
						SalePwtReportsBean pwtBean = pwtItr.next();
						if (saleBean.getGameName().equalsIgnoreCase(
								pwtBean.getGameName())) {
							matchedPwt = pwtBean.getPwtMrpAmt();
							break;
						}
					}
					addDoubleNumber(excelSheet, col1Count, row1Count,
							matchedPwt);
					row1Count += 1;
					serialCount += 1;
				}
			}

		}
		if (isCS) {
			if (!csSaleReportList.isEmpty()) {
				int serialCount = 1;
				row1Count += 1;
				addCaption(excelSheet, colForService, row1Count, "COMM SER");
				row1Count += 2;
				addCaption(excelSheet, 4, row1Count, "SNo.");
				addCaption(excelSheet, 5, row1Count, "PRODUCT NAME");
				addCaption(excelSheet, 6, row1Count, "SALE AMOUNT");
				row1Count += 1;
				Iterator<CSSaleReportBean> saleItr = csSaleReportList
						.iterator();
				while (saleItr.hasNext()) {
					int col1Count = 4;

					CSSaleReportBean saleBean = saleItr.next();

					addCaption(excelSheet, col1Count, row1Count,
							String.valueOf(serialCount));
					col1Count += 1;
					addCaption(excelSheet, col1Count, row1Count, saleBean
							.getCategoryCode());
					col1Count += 1;
					addDoubleNumber(excelSheet, col1Count, row1Count, saleBean
							.getMrpAmt());
					serialCount += 1;
					row1Count += 1;
				}

			}

		}

		if (isOLA) {
			// add Code For OLA
		}
	}
}
