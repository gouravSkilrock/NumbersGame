package com.skilrock.lms.web.reportsMgmt.common;

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
import com.skilrock.lms.beans.CollectionReportBean;
import com.skilrock.lms.beans.DateBeans;
import com.skilrock.lms.common.utility.TextConfigurator;

public class WriteExcelForCollectionReport extends LocalizedTextUtil {
	private WritableCellFormat dateFormat;

	private WritableCellFormat headerDateFormat;
	private WritableCellFormat headerFormat;
	private WritableCellFormat headingLabel;
	private WritableCellFormat headingNumberFormat;
	Log logger = LogFactory.getLog(WriteExcelForCollectionReport.class);
	private WritableCellFormat numberFormat;
	private String reportType;
	private java.util.Date startDate, endDate, reportday;
	private WritableCellFormat times;
	private WritableCellFormat timesBoldUnderline;
	
	private Locale locale;

	public WriteExcelForCollectionReport(DateBeans dateBeans)
			throws WriteException {
		
		locale = Locale.getDefault();

		if (!dateBeans.getReportType().equalsIgnoreCase("")) {
			this.reportType = "Last"
					+ dateBeans.getReportType().substring(7,
							dateBeans.getReportType().length());
		} else {
			this.reportType = dateBeans.getReportType();
		}
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
			List<CollectionReportBean> reportlist, String orgname,
			String orgAdd, String currSym) throws WriteException,
			RowsExceededException, ParseException {

		sheet.addCell(new Label(2, 1, orgname, headerFormat));
		sheet.mergeCells(2, 1, 9, 1);
		sheet.addCell(new Label(10, 1, findDefaultText("label.amt.in", locale) + currSym, headerFormat));
		sheet.addCell(new Label(2, 2, orgAdd, headerFormat));
		sheet.mergeCells(2, 2, 10, 2);

		createHeaderForCollection(sheet);

		/*
		 * addCaption(sheet,3,4,"ACCOUNTS DETAILS",18); sheet.mergeCells(3, 4,
		 * 8, 4); if(reportlist.get(0).getIsDraw() &&
		 * reportlist.get(0).getIsScratch()){ addCaption(sheet,9,4,"DRAW
		 * DETAILS",13); sheet.mergeCells(9, 4, 12, 4); addCaption(sheet, 13, 4,
		 * "SCRATCH DETAILS", 11); sheet.mergeCells(13, 4, 16, 4); }
		 */

		if (reportlist.get(0).getIsDraw() && reportlist.get(0).getIsScratch() && reportlist.get(0).getIsCS()) {
			addCaption(sheet, 3, 4, findDefaultText("DRAW.DETAILS", locale), 13);
			sheet.mergeCells(3, 4, 6, 4);
			addCaption(sheet, 7, 4, findDefaultText("SCRATCH.DETAILS", locale), 11);
			sheet.mergeCells(7, 4, 10, 4);
			addCaption(sheet, 11, 4, findDefaultText("COMM.SERV.DETAIL", locale), 11);
			sheet.mergeCells(11, 4, 14, 4);
			addCaption(sheet, 15, 4, findDefaultText("ACCOUNTS.DETAILS", locale), 18);
			sheet.mergeCells(15, 4, 20, 4);
		}

		if (reportlist.get(0).getIsDraw() && !reportlist.get(0).getIsScratch() && !reportlist.get(0).getIsCS()) {
			addCaption(sheet, 3, 4, findDefaultText("DRAW.DETAILS", locale), 10);
			sheet.mergeCells(3, 4, 6, 4);
			addCaption(sheet, 7, 4, findDefaultText("ACCOUNTS.DETAILS", locale), 18);
			sheet.mergeCells(7, 4, 12, 4);
		}

		if (reportlist.get(0).getIsScratch() && !reportlist.get(0).getIsDraw() && !reportlist.get(0).getIsCS()) {
			addCaption(sheet, 3, 4, findDefaultText("SCRATCH.DETAILS", locale), 11);
			sheet.mergeCells(3, 4, 6, 4);
			addCaption(sheet, 7, 4, findDefaultText("ACCOUNTS.DETAILS", locale), 18);
			sheet.mergeCells(7, 4, 12, 4);
		}
		
		if (reportlist.get(0).getIsCS() && !reportlist.get(0).getIsScratch() && !reportlist.get(0).getIsDraw()) {
			addCaption(sheet, 3, 4, findDefaultText("COMM.SERV.DETAIL", locale), 11);
			sheet.mergeCells(3, 4, 6, 4);
			addCaption(sheet, 7, 4, findDefaultText("ACCOUNTS.DETAILS", locale), 18);
			sheet.mergeCells(7, 4, 12, 4);
		}

		addCaption(sheet, 0, 5, findDefaultText("label.sNo", locale), 5);
		addCaption(sheet, 1, 5, ((Map<String, String>) ServletActionContext
				.getServletContext().getAttribute("TIER_MAP")).get("AGENT")
				+ " "+findDefaultText("label.name", locale), 35);
		addCaption(sheet, 2, 5, findDefaultText("OPENING.BAL", locale), 14);

		if (reportlist.get(0).getIsDraw() && reportlist.get(0).getIsScratch() && reportlist.get(0).getIsCS()) {

			addCaption(sheet, 3, 5, findDefaultText("SALE.REFUND", locale), 12);
			addCaption(sheet, 4, 5, findDefaultText("SALE.REFUND", locale), 12);
			addCaption(sheet, 5, 5, TextConfigurator.getText("PWT").toUpperCase(), 12);
			addCaption(sheet, 6, 5, findDefaultText("TOTAL", locale), 12);
			addCaption(sheet, 7, 5, findDefaultText("SALE.REFUND", locale), 12);
			addCaption(sheet, 8, 5, findDefaultText("SALE.RET", locale), 12);
			addCaption(sheet, 9, 5, TextConfigurator.getText("PWT").toUpperCase(), 12);
			addCaption(sheet, 10, 5, findDefaultText("TOTAL", locale), 12);
			addCaption(sheet, 11, 5, findDefaultText("SALE.REFUND", locale), 12);
			addCaption(sheet, 12, 5, findDefaultText("SALE.RET", locale), 12);
			addCaption(sheet, 13, 5, findDefaultText("TOTAL", locale), 12);
			addCaption(sheet, 14, 5, findDefaultText("CASH", locale), 12);
			addCaption(sheet, 15, 5, findDefaultText("CHQ.RET", locale), 12);
			addCaption(sheet, 16, 5, findDefaultText("CHQ.RET", locale), 12);
			addCaption(sheet, 17, 5, findDefaultText("CREDIT", locale), 12);
			addCaption(sheet, 18, 5, findDefaultText("DEBIT", locale), 12);
			addCaption(sheet, 19, 5, findDefaultText("label.bank.deposit", locale), 12);
			addCaption(sheet, 20, 5, findDefaultText("TOTAL", locale), 12);
			addCaption(sheet, 21, 5, findDefaultText("GRAND.TOTAL", locale), 14);
		}
		if (reportlist.get(0).getIsDraw() && !reportlist.get(0).getIsScratch() && !reportlist.get(0).getIsCS()) {
			addCaption(sheet, 3, 5, findDefaultText("SALE.REFUND", locale), 12);
			addCaption(sheet, 4, 5, findDefaultText("SALE.REFUND", locale), 12);
			addCaption(sheet, 5, 5, TextConfigurator.getText("PWT").toUpperCase(), 12);
			addCaption(sheet, 6, 5, findDefaultText("TOTAL", locale), 12);
			addCaption(sheet, 7, 5, findDefaultText("CASH", locale), 12);
			addCaption(sheet, 8, 5, findDefaultText("CHQ.RET", locale), 12);
			addCaption(sheet, 9, 5, findDefaultText("CHQ.RET", locale), 12);
			addCaption(sheet, 10, 5, findDefaultText("CHQ.RET", locale), 12);
			addCaption(sheet, 11, 5, findDefaultText("DEBIT", locale), 12);
			addCaption(sheet, 12, 5, findDefaultText("label.bank.deposit", locale), 12);
			addCaption(sheet, 13, 5, findDefaultText("TOTAL", locale), 12);
			addCaption(sheet, 14, 5, findDefaultText("GRAND.TOTAL", locale), 14);
		}
		if (reportlist.get(0).getIsScratch() && !reportlist.get(0).getIsDraw() && !reportlist.get(0).getIsCS()) {
			addCaption(sheet, 3, 5, findDefaultText("SALE.REFUND", locale), 12);
			addCaption(sheet, 4, 5, findDefaultText("SALE.REFUND", locale), 12);
			addCaption(sheet, 5, 5, TextConfigurator.getText("PWT").toUpperCase(), 12);
			addCaption(sheet, 6, 5, findDefaultText("TOTAL", locale), 12);
			addCaption(sheet, 7, 5, findDefaultText("CASH", locale), 12);
			addCaption(sheet, 8, 5, findDefaultText("CHQ.RET", locale), 12);
			addCaption(sheet, 9, 5, findDefaultText("CHQ.RET", locale), 12);
			addCaption(sheet, 10, 5, findDefaultText("CREDIT", locale), 12);
			addCaption(sheet, 11, 5, findDefaultText("DEBIT", locale), 12);
			addCaption(sheet, 12, 5, findDefaultText("label.bank.deposit", locale), 12);
			addCaption(sheet, 13, 5, findDefaultText("TOTAL", locale), 12);
			addCaption(sheet, 14, 5, findDefaultText("GRAND.TOTAL", locale), 14);
		}
		if (reportlist.get(0).getIsCS() && !reportlist.get(0).getIsDraw() && !reportlist.get(0).getIsScratch()) {
			addCaption(sheet, 3, 5, findDefaultText("SALE.REFUND", locale), 12);
			addCaption(sheet, 4, 5, findDefaultText("SALE.REFUND", locale), 12);
			addCaption(sheet, 5, 5, findDefaultText("TOTAL", locale), 12);
			addCaption(sheet, 6, 5, findDefaultText("CASH", locale), 12);
			addCaption(sheet, 7, 5, findDefaultText("CHQ.RET", locale), 12);
			addCaption(sheet, 8, 5, findDefaultText("CHQ.RET", locale), 12);
			addCaption(sheet, 9, 5, findDefaultText("CREDIT", locale), 12);
			addCaption(sheet, 10, 5, findDefaultText("DEBIT", locale), 12);
			addCaption(sheet, 11, 5, findDefaultText("label.bank.deposit", locale), 12);
			addCaption(sheet, 12, 5, findDefaultText("TOTAL", locale), 12);
			addCaption(sheet, 13, 5, findDefaultText("GRAND.TOTAL", locale), 14);
		}

		int length = reportlist.size();
		CollectionReportBean reportBean = null;

		double netOpenBal = 0.0;
		double netCash = 0.0;
		double netChq = 0.0;
		double netChqRet = 0.0;
		double netCredit = 0.0;
		double netDebit = 0.0;
		double netAccTotal = 0.0;
		double netDrawSale = 0.0;
		double netDrawSaleRefund = 0.0;
		double netDrawPwt = 0.0;
		double netDrawTotal = 0.0;
		double netScratchSale = 0.0;
		double netSaleRet = 0.0;
		double netScratchPwt = 0.0;
		double netScratchTotal = 0.0;
		double netCSSale = 0.0;
		double netCSSaleRefund = 0.0;
		double netCSTotal = 0.0;
		double netGrandTotal = 0.0;
		double netBankDep = 0.0;

		for (int i = 0; i < length; i++) {

			reportBean = reportlist.get(i);

			netOpenBal += Double.parseDouble(reportBean.getOpenBal());
			netCash += Double.parseDouble(reportBean.getCash());
			netChq += Double.parseDouble(reportBean.getChq());
			netChqRet += Double.parseDouble(reportBean.getChqRet());
			netCredit += Double.parseDouble(reportBean.getCredit());
			netDebit += Double.parseDouble(reportBean.getDebit());
			netBankDep += Double.parseDouble(reportBean.getBankDeposit()); 
			netAccTotal += Double.parseDouble(reportBean.getRecTotal());

			if (reportBean.getIsDraw() && !reportBean.getIsScratch() && !reportBean.getIsCS()) {
				netDrawSale += Double.parseDouble(reportBean.getDrawSale());
				netDrawSaleRefund += Double.parseDouble(reportBean
						.getDrawSaleRefund());
				netDrawPwt += Double.parseDouble(reportBean.getDrawPwt());
				netDrawTotal += Double.parseDouble(reportBean.getDrawTotal());
				netGrandTotal += Double.parseDouble(reportBean.getGrandTotal());
			}

			if (reportBean.getIsScratch() && !reportBean.getIsDraw() && !reportBean.getIsCS()) {
				netScratchSale += Double.parseDouble(reportBean
						.getScratchSale());
				netSaleRet += Double.parseDouble(reportBean.getSaleRet());
				netScratchPwt += Double.parseDouble(reportBean.getScratchPwt());
				netScratchTotal += Double.parseDouble(reportBean
						.getScratchTotal());
				netGrandTotal += Double.parseDouble(reportBean.getGrandTotal());
			}
			
			if (reportBean.getIsCS() && !reportBean.getIsDraw() && !reportBean.getIsScratch()) {
				netCSSale += Double.parseDouble(reportBean
						.getCSSale());
				netCSSaleRefund += Double.parseDouble(reportBean.getCSSaleRefund());
				netCSTotal += Double.parseDouble(reportBean
						.getCSTotal());
				netGrandTotal += Double.parseDouble(reportBean.getGrandTotal());
			}

			if (reportBean.getIsDraw() && reportBean.getIsScratch() && reportBean.getIsCS()) {
				netDrawSale += Double.parseDouble(reportBean.getDrawSale());
				netDrawSaleRefund += Double.parseDouble(reportBean
						.getDrawSaleRefund());
				netDrawPwt += Double.parseDouble(reportBean.getDrawPwt());
				netDrawTotal += Double.parseDouble(reportBean.getDrawTotal());
				netScratchSale += Double.parseDouble(reportBean
						.getScratchSale());
				netSaleRet += Double.parseDouble(reportBean.getSaleRet());
				netScratchPwt += Double.parseDouble(reportBean.getScratchPwt());
				netScratchTotal += Double.parseDouble(reportBean
						.getScratchTotal());
				netCSSale += Double.parseDouble(reportBean
						.getCSSale());
				netCSSaleRefund += Double.parseDouble(reportBean.getCSSaleRefund());
				netCSTotal += Double.parseDouble(reportBean
						.getCSTotal());
				netGrandTotal += Double.parseDouble(reportBean.getGrandTotal());
			}

			if (reportBean.getIsDraw() && reportBean.getIsScratch() && reportBean.getIsCS()) {
				addLabel(sheet, 0, i + 6, reportBean.getSrNo());
				addLabel(sheet, 1, i + 6, reportBean.getName());
				addNumber(sheet, 2, i + 6, Double.parseDouble(reportBean
						.getOpenBal()));
				addNumber(sheet, 3, i + 6, Double.parseDouble(reportBean
						.getDrawSale()));
				addNumber(sheet, 4, i + 6, Double.parseDouble(reportBean
						.getDrawSaleRefund()));
				addNumber(sheet, 5, i + 6, Double.parseDouble(reportBean
						.getDrawPwt()));
				addNumber(sheet, 6, i + 6, Double.parseDouble(reportBean
						.getDrawTotal()));
				addNumber(sheet, 7, i + 6, Double.parseDouble(reportBean
						.getScratchSale()));
				addNumber(sheet, 8, i + 6, Double.parseDouble(reportBean
						.getSaleRet()));
				addNumber(sheet, 9, i + 6, Double.parseDouble(reportBean
						.getScratchPwt()));
				addNumber(sheet, 10, i + 6, Double.parseDouble(reportBean
						.getScratchTotal()));
				addNumber(sheet, 11, i + 6, Double.parseDouble(reportBean
						.getCSSale()));
				addNumber(sheet, 12, i + 6, Double.parseDouble(reportBean
						.getCSSaleRefund()));
				addNumber(sheet, 13, i + 6, Double.parseDouble(reportBean
						.getCSTotal()));
				addNumber(sheet, 14, i + 6, Double.parseDouble(reportBean
						.getCash()));
				addNumber(sheet, 15, i + 6, Double.parseDouble(reportBean
						.getChq()));
				addNumber(sheet, 16, i + 6, Double.parseDouble(reportBean
						.getChqRet()));
				addNumber(sheet, 17, i + 6, Double.parseDouble(reportBean
						.getCredit()));
				addNumber(sheet, 18, i + 6, Double.parseDouble(reportBean
						.getDebit()));
				addNumber(sheet, 19, i + 6, Double.parseDouble(reportBean
						.getBankDeposit()));
				addNumber(sheet, 20, i + 6, Double.parseDouble(reportBean
						.getRecTotal()));
				addNumber(sheet, 21, i + 6, Double.parseDouble(reportBean
						.getGrandTotal()));

			}
			if (reportBean.getIsDraw() && !reportBean.getIsScratch() && !reportBean.getIsCS()) {
				addLabel(sheet, 0, i + 6, reportBean.getSrNo());
				addLabel(sheet, 1, i + 6, reportBean.getName());
				addNumber(sheet, 2, i + 6, Double.parseDouble(reportBean
						.getOpenBal()));
				addNumber(sheet, 3, i + 6, Double.parseDouble(reportBean
						.getDrawSale()));
				addNumber(sheet, 4, i + 6, Double.parseDouble(reportBean
						.getDrawSaleRefund()));
				addNumber(sheet, 5, i + 6, Double.parseDouble(reportBean
						.getDrawPwt()));
				addNumber(sheet, 6, i + 6, Double.parseDouble(reportBean
						.getDrawTotal()));
				addNumber(sheet, 7, i + 6, Double.parseDouble(reportBean
						.getCash()));
				addNumber(sheet, 8, i + 6, Double.parseDouble(reportBean
						.getChq()));
				addNumber(sheet, 9, i + 6, Double.parseDouble(reportBean
						.getChqRet()));
				addNumber(sheet, 10, i + 6, Double.parseDouble(reportBean
						.getCredit()));
				addNumber(sheet, 11, i + 6, Double.parseDouble(reportBean
						.getDebit()));
				addNumber(sheet, 12, i + 6, Double.parseDouble(reportBean
						.getBankDeposit()));
				addNumber(sheet, 13, i + 6, Double.parseDouble(reportBean
						.getRecTotal()));
				addNumber(sheet, 14, i + 6, Double.parseDouble(reportBean
						.getGrandTotal()));
			}
			if (reportBean.getIsScratch() && !reportBean.getIsDraw() && !reportBean.getIsCS()) {
				addLabel(sheet, 0, i + 6, reportBean.getSrNo());
				addLabel(sheet, 1, i + 6, reportBean.getName());
				addNumber(sheet, 2, i + 6, Double.parseDouble(reportBean
						.getOpenBal()));
				addNumber(sheet, 3, i + 6, Double.parseDouble(reportBean
						.getScratchSale()));
				addNumber(sheet, 4, i + 6, Double.parseDouble(reportBean
						.getSaleRet()));
				addNumber(sheet, 5, i + 6, Double.parseDouble(reportBean
						.getScratchPwt()));
				addNumber(sheet, 6, i + 6, Double.parseDouble(reportBean
						.getScratchTotal()));
				addNumber(sheet, 7, i + 6, Double.parseDouble(reportBean
						.getCash()));
				addNumber(sheet, 8, i + 6, Double.parseDouble(reportBean
						.getChq()));
				addNumber(sheet, 9, i + 6, Double.parseDouble(reportBean
						.getChqRet()));
				addNumber(sheet, 10, i + 6, Double.parseDouble(reportBean
						.getCredit()));
				addNumber(sheet, 11, i + 6, Double.parseDouble(reportBean
						.getDebit()));
				addNumber(sheet, 12, i + 6, Double.parseDouble(reportBean
						.getBankDeposit()));
				addNumber(sheet, 13, i + 6, Double.parseDouble(reportBean
						.getRecTotal()));
				addNumber(sheet, 14, i + 6, Double.parseDouble(reportBean
						.getGrandTotal()));
			}
			if (reportBean.getIsCS() && !reportBean.getIsDraw() && !reportBean.getIsScratch()) {
				addLabel(sheet, 0, i + 6, reportBean.getSrNo());
				addLabel(sheet, 1, i + 6, reportBean.getName());
				addNumber(sheet, 2, i + 6, Double.parseDouble(reportBean
						.getOpenBal()));
				addNumber(sheet, 3, i + 6, Double.parseDouble(reportBean
						.getCSSale()));
				addNumber(sheet, 4, i + 6, Double.parseDouble(reportBean
						.getCSSaleRefund()));
				addNumber(sheet, 5, i + 6, Double.parseDouble(reportBean
						.getCSTotal()));
				addNumber(sheet, 6, i + 6, Double.parseDouble(reportBean
						.getCash()));
				addNumber(sheet, 7, i + 6, Double.parseDouble(reportBean
						.getChq()));
				addNumber(sheet, 8, i + 6, Double.parseDouble(reportBean
						.getChqRet()));
				addNumber(sheet, 9, i + 6, Double.parseDouble(reportBean
						.getCredit()));
				addNumber(sheet, 10, i + 6, Double.parseDouble(reportBean
						.getDebit()));
				addNumber(sheet, 11, i + 6, Double.parseDouble(reportBean
						.getBankDeposit()));
				addNumber(sheet, 12, i + 6, Double.parseDouble(reportBean
						.getRecTotal()));
				addNumber(sheet, 13, i + 6, Double.parseDouble(reportBean
						.getGrandTotal()));
			}
		}

		sheet.addCell(new Label(1, length + 6, findDefaultText("label.total", locale), headingLabel));
		sheet.mergeCells(1, length + 6, 1, length + 6);

		if (reportBean.getIsDraw() && reportBean.getIsScratch() && reportBean.getIsCS()) {
			sheet.addCell(new Number(2, length + 6, netOpenBal,
					headingNumberFormat));
			sheet.addCell(new Number(3, length + 6, netDrawSale,
					headingNumberFormat));
			sheet.addCell(new Number(4, length + 6, netDrawSaleRefund,
					headingNumberFormat));
			sheet.addCell(new Number(5, length + 6, netDrawPwt,
					headingNumberFormat));
			sheet.addCell(new Number(6, length + 6, netDrawTotal,
					headingNumberFormat));
			sheet.addCell(new Number(7, length + 6, netScratchSale,
					headingNumberFormat));
			sheet.addCell(new Number(8, length + 6, netSaleRet,
					headingNumberFormat));
			sheet.addCell(new Number(9, length + 6, netScratchPwt,
					headingNumberFormat));
			sheet.addCell(new Number(10, length + 6, netScratchTotal,
					headingNumberFormat));
			sheet.addCell(new Number(11, length + 6, netCSSale,
					headingNumberFormat));
			sheet.addCell(new Number(12, length + 6, netCSSaleRefund,
					headingNumberFormat));
			sheet.addCell(new Number(13, length + 6, netCSTotal,
					headingNumberFormat));
			sheet.addCell(new Number(14, length + 6, netCash,
					headingNumberFormat));
			sheet.addCell(new Number(15, length + 6, netChq,
					headingNumberFormat));
			sheet.addCell(new Number(16, length + 6, netChqRet,
					headingNumberFormat));
			sheet.addCell(new Number(17, length + 6, netCredit,
					headingNumberFormat));
			sheet.addCell(new Number(18, length + 6, netDebit,
					headingNumberFormat));
			sheet.addCell(new Number(19, length + 6, netBankDep,
					headingNumberFormat));
			sheet.addCell(new Number(20, length + 6, netAccTotal,
					headingNumberFormat));
			sheet.addCell(new Number(21, length + 6, netGrandTotal,
					headingNumberFormat));
		}
		if (reportBean.getIsDraw() && !reportBean.getIsScratch() && !reportBean.getIsCS()) {
			sheet.addCell(new Number(2, length + 6, netOpenBal,
					headingNumberFormat));
			sheet.addCell(new Number(3, length + 6, netDrawSale,
					headingNumberFormat));
			sheet.addCell(new Number(4, length + 6, netDrawSaleRefund,
					headingNumberFormat));
			sheet.addCell(new Number(5, length + 6, netDrawPwt,
					headingNumberFormat));
			sheet.addCell(new Number(6, length + 6, netDrawTotal,
					headingNumberFormat));
			sheet.addCell(new Number(7, length + 6, netCash,
					headingNumberFormat));
			sheet
					.addCell(new Number(8, length + 6, netChq,
							headingNumberFormat));
			sheet.addCell(new Number(9, length + 6, netChqRet,
					headingNumberFormat));
			sheet.addCell(new Number(10, length + 6, netCredit,
					headingNumberFormat));
			sheet.addCell(new Number(11, length + 6, netDebit,
					headingNumberFormat));
			sheet.addCell(new Number(12, length + 6, netAccTotal,
					headingNumberFormat));
			sheet.addCell(new Number(13, length + 6, netGrandTotal,
					headingNumberFormat));
		}
		if (reportBean.getIsScratch() && !reportBean.getIsDraw() && !reportBean.getIsCS()) {
			sheet.addCell(new Number(2, length + 6, netOpenBal,
					headingNumberFormat));
			sheet.addCell(new Number(3, length + 6, netScratchSale,
					headingNumberFormat));
			sheet.addCell(new Number(4, length + 6, netSaleRet,
					headingNumberFormat));
			sheet.addCell(new Number(5, length + 6, netScratchPwt,
					headingNumberFormat));
			sheet.addCell(new Number(6, length + 6, netScratchTotal,
					headingNumberFormat));
			sheet.addCell(new Number(7, length + 6, netCash,
					headingNumberFormat));
			sheet
					.addCell(new Number(8, length + 6, netChq,
							headingNumberFormat));
			sheet.addCell(new Number(9, length + 6, netChqRet,
					headingNumberFormat));
			sheet.addCell(new Number(10, length + 6, netCredit,
					headingNumberFormat));
			sheet.addCell(new Number(11, length + 6, netDebit,
					headingNumberFormat));
			sheet.addCell(new Number(12, length + 6, netBankDep,
					headingNumberFormat));
			sheet.addCell(new Number(13, length + 6, netAccTotal,
					headingNumberFormat));
			sheet.addCell(new Number(14, length + 6, netGrandTotal,
					headingNumberFormat));
		}
		if (reportBean.getIsCS() && !reportBean.getIsDraw() && !reportBean.getIsScratch()) {
			sheet.addCell(new Number(2, length + 6, netOpenBal,
					headingNumberFormat));
			sheet.addCell(new Number(3, length + 6, netCSSale,
					headingNumberFormat));
			sheet.addCell(new Number(4, length + 6, netCSSaleRefund,
					headingNumberFormat));
			sheet.addCell(new Number(5, length + 6, netCSTotal,
					headingNumberFormat));
			sheet.addCell(new Number(6, length + 6, netCash,
					headingNumberFormat));
			sheet
					.addCell(new Number(7, length + 6, netChq,
							headingNumberFormat));
			sheet.addCell(new Number(8, length + 6, netChqRet,
					headingNumberFormat));
			sheet.addCell(new Number(9, length + 6, netCredit,
					headingNumberFormat));
			sheet.addCell(new Number(10, length + 6, netDebit,
					headingNumberFormat));
			sheet.addCell(new Number(11, length + 6, netAccTotal,
					headingNumberFormat));
			sheet.addCell(new Number(12, length + 6, netGrandTotal,
					headingNumberFormat));
		}

	}

	private void createContentForAgent(WritableSheet sheet,
			List<CollectionReportBean> reportlist, String orgname, String orgAdd)
			throws WriteException, RowsExceededException, ParseException {
		sheet.addCell(new Label(2, 1, orgname, headerFormat));
		sheet.mergeCells(2, 1, 10, 1);

		sheet.addCell(new Label(2, 2, orgAdd, headerFormat));
		sheet.mergeCells(2, 2, 10, 2);

		createHeaderForCollection(sheet);

		/*
		 * addCaption(sheet,3,4,"ACCOUNTS DETAILS",18); sheet.mergeCells(3, 4,
		 * 7, 4); if(reportlist.get(0).getIsDraw() &&
		 * reportlist.get(0).getIsScratch()){ addCaption(sheet,8,4,"DRAW
		 * DETAILS",13); sheet.mergeCells(8, 4, 11, 4); addCaption(sheet, 12, 4,
		 * "SCRATCH DETAILS", 11); sheet.mergeCells(12, 4, 15, 4); }
		 */

		if (reportlist.get(0).getIsDraw() && reportlist.get(0).getIsScratch()) {
			addCaption(sheet, 3, 4, findDefaultText("DRAW.DETAILS", locale), 13);
			sheet.mergeCells(3, 4, 6, 4);
			addCaption(sheet, 7, 4, findDefaultText("SCRATCH.DETAILS", locale), 11);
			sheet.mergeCells(7, 4, 10, 4);
			addCaption(sheet, 11, 4, findDefaultText("ACCOUNTS.DETAILS", locale), 18);
			sheet.mergeCells(11, 4, 15, 4);
		}

		if (reportlist.get(0).getIsDraw() && !reportlist.get(0).getIsScratch()) {
			addCaption(sheet, 3, 4, findDefaultText("DRAW.DETAILS", locale), 10);
			sheet.mergeCells(3, 4, 6, 4);
			addCaption(sheet, 7, 4, findDefaultText("ACCOUNTS.DETAILS", locale), 18);
			sheet.mergeCells(7, 4, 11, 4);
		}

		if (reportlist.get(0).getIsScratch() && !reportlist.get(0).getIsDraw()) {
			addCaption(sheet, 3, 4, findDefaultText("SCRATCH.DETAILS", locale), 11);
			sheet.mergeCells(3, 4, 6, 4);
			addCaption(sheet, 7, 4, findDefaultText("ACCOUNTS.DETAILS", locale), 18);
			sheet.mergeCells(7, 4, 11, 4);
		}

		addCaption(sheet, 0, 5, " "+findDefaultText("label.sNo", locale), 5);
		addCaption(sheet, 1, 5, ((Map<String, String>) ServletActionContext
				.getServletContext().getAttribute("TIER_MAP")).get("RETAILER")
				+ " "+findDefaultText("label.name", locale), 35);
		addCaption(sheet, 2, 5, findDefaultText("OPENING.BAL", locale), 14);

		if (reportlist.get(0).getIsDraw() && reportlist.get(0).getIsScratch()) {
			addCaption(sheet, 3, 5, findDefaultText("SALE.REFUND", locale), 12);
			addCaption(sheet, 4, 5, findDefaultText("SALE.REFUND", locale), 12);
			addCaption(sheet, 5, 5, TextConfigurator.getText("PWT").toUpperCase(), 12);
			addCaption(sheet, 6, 5, findDefaultText("TOTAL", locale), 12);
			addCaption(sheet, 7, 5, findDefaultText("SALE.REFUND", locale), 12);
			addCaption(sheet, 8, 5, findDefaultText("SALE.RET", locale), 12);
			addCaption(sheet, 9, 5, TextConfigurator.getText("PWT").toUpperCase(), 12);
			addCaption(sheet, 10, 5, findDefaultText("TOTAL", locale), 12);
			addCaption(sheet, 11, 5, findDefaultText("CASH", locale), 12);
			addCaption(sheet, 12, 5, findDefaultText("CHQ.RET", locale), 12);
			addCaption(sheet, 13, 5, findDefaultText("CHQ.RET", locale), 12);
			addCaption(sheet, 14, 5, findDefaultText("CREDIT", locale), 12);
			addCaption(sheet, 15, 5, findDefaultText("DEBIT", locale), 12);
			addCaption(sheet, 16, 5, findDefaultText("TOTAL", locale), 12);
			addCaption(sheet, 17, 5, findDefaultText("GRAND.TOTAL", locale), 14);
		}
		if (reportlist.get(0).getIsDraw() && !reportlist.get(0).getIsScratch()) {
			addCaption(sheet, 3, 5, findDefaultText("SALE.REFUND", locale), 12);
			addCaption(sheet, 4, 5, findDefaultText("SALE.REFUND", locale), 12);
			addCaption(sheet, 5, 5, TextConfigurator.getText("PWT").toUpperCase(), 12);
			addCaption(sheet, 6, 5, findDefaultText("TOTAL", locale), 12);
			addCaption(sheet, 7, 5, findDefaultText("CASH", locale), 12);
			addCaption(sheet, 8, 5, findDefaultText("CHQ.RET", locale), 12);
			addCaption(sheet, 9, 5, findDefaultText("CHQ.RET", locale), 12);
			addCaption(sheet, 10, 5, findDefaultText("CHQ.RET", locale), 12);
			addCaption(sheet, 11, 5, findDefaultText("DEBIT", locale), 12);
			addCaption(sheet, 12, 5, findDefaultText("TOTAL", locale), 12);
			addCaption(sheet, 13, 5, findDefaultText("GRAND.TOTAL", locale), 14);
		}
		if (reportlist.get(0).getIsScratch() && !reportlist.get(0).getIsDraw()) {
			addCaption(sheet, 3, 5, findDefaultText("SALE.REFUND", locale), 12);
			addCaption(sheet, 4, 5, findDefaultText("SALE.REFUND", locale), 12);
			addCaption(sheet, 5, 5, TextConfigurator.getText("PWT").toUpperCase(), 12);
			addCaption(sheet, 6, 5, findDefaultText("TOTAL", locale), 12);
			addCaption(sheet, 7, 5, findDefaultText("CASH", locale), 12);
			addCaption(sheet, 8, 5, findDefaultText("CHQ.RET", locale), 12);
			addCaption(sheet, 9, 5, findDefaultText("CHQ.RET", locale), 12);
			addCaption(sheet, 10, 5, findDefaultText("CREDIT", locale), 12);
			addCaption(sheet, 11, 5, findDefaultText("DEBIT", locale), 12);
			addCaption(sheet, 12, 5, findDefaultText("TOTAL", locale), 12);
			addCaption(sheet, 13, 5, findDefaultText("GRAND.TOTAL", locale), 14);
		}

		int length = reportlist.size();
		CollectionReportBean reportBean = null;

		double netOpenBal = 0.0;
		double netCash = 0.0;
		double netChq = 0.0;
		double netChqRet = 0.0;
		double netCredit = 0.0;
		double netDebit = 0.0;
		double netAccTotal = 0.0;
		double netDrawSale = 0.0;
		double netDrawSaleRefund = 0.0;
		double netDrawPwt = 0.0;
		double netDrawTotal = 0.0;
		double netScratchSale = 0.0;
		double netSaleRet = 0.0;
		double netScratchPwt = 0.0;
		double netScratchTotal = 0.0;
		double netGrandTotal = 0.0;

		for (int i = 0; i < length; i++) {

			reportBean = reportlist.get(i);

			netOpenBal += Double.parseDouble(reportBean.getOpenBal());
			netCash += Double.parseDouble(reportBean.getCash());
			netChq += Double.parseDouble(reportBean.getChq());
			netChqRet += Double.parseDouble(reportBean.getChqRet());
			netCredit += Double.parseDouble(reportBean.getCredit());
			netDebit += Double.parseDouble(reportBean.getDebit());
			netAccTotal += Double.parseDouble(reportBean.getRecTotal());

			if (reportBean.getIsDraw() && !reportBean.getIsScratch()) {
				netDrawSale += Double.parseDouble(reportBean.getDrawSale());
				netDrawSaleRefund += Double.parseDouble(reportBean
						.getDrawSaleRefund());
				netDrawPwt += Double.parseDouble(reportBean.getDrawPwt());
				netDrawTotal += Double.parseDouble(reportBean.getDrawTotal());
				netGrandTotal += Double.parseDouble(reportBean.getGrandTotal());
			}

			if (reportBean.getIsScratch() && !reportBean.getIsDraw()) {
				netScratchSale += Double.parseDouble(reportBean
						.getScratchSale());
				netSaleRet += Double.parseDouble(reportBean.getSaleRet());
				netScratchPwt += Double.parseDouble(reportBean.getScratchPwt());
				netScratchTotal += Double.parseDouble(reportBean
						.getScratchTotal());
				netGrandTotal += Double.parseDouble(reportBean.getGrandTotal());
			}

			if (reportBean.getIsDraw() && reportBean.getIsScratch()) {
				netDrawSale += Double.parseDouble(reportBean.getDrawSale());
				netDrawSaleRefund += Double.parseDouble(reportBean
						.getDrawSaleRefund());
				netDrawPwt += Double.parseDouble(reportBean.getDrawPwt());
				netDrawTotal += Double.parseDouble(reportBean.getDrawTotal());
				netScratchSale += Double.parseDouble(reportBean
						.getScratchSale());
				netSaleRet += Double.parseDouble(reportBean.getSaleRet());
				netScratchPwt += Double.parseDouble(reportBean.getScratchPwt());
				netScratchTotal += Double.parseDouble(reportBean
						.getScratchTotal());
				netGrandTotal += Double.parseDouble(reportBean.getGrandTotal());
			}

			if (reportBean.getIsDraw() && reportBean.getIsScratch()) {
				addLabel(sheet, 0, i + 6, reportBean.getSrNo());
				addLabel(sheet, 1, i + 6, reportBean.getName());
				addNumber(sheet, 2, i + 6, Double.parseDouble(reportBean
						.getOpenBal()));
				addNumber(sheet, 3, i + 6, Double.parseDouble(reportBean
						.getDrawSale()));
				addNumber(sheet, 4, i + 6, Double.parseDouble(reportBean
						.getDrawSaleRefund()));
				addNumber(sheet, 5, i + 6, Double.parseDouble(reportBean
						.getDrawPwt()));
				addNumber(sheet, 6, i + 6, Double.parseDouble(reportBean
						.getDrawTotal()));
				addNumber(sheet, 7, i + 6, Double.parseDouble(reportBean
						.getScratchSale()));
				addNumber(sheet, 8, i + 6, Double.parseDouble(reportBean
						.getSaleRet()));
				addNumber(sheet, 9, i + 6, Double.parseDouble(reportBean
						.getScratchPwt()));
				addNumber(sheet, 10, i + 6, Double.parseDouble(reportBean
						.getScratchTotal()));
				addNumber(sheet, 11, i + 6, Double.parseDouble(reportBean
						.getCash()));
				addNumber(sheet, 12, i + 6, Double.parseDouble(reportBean
						.getChq()));
				addNumber(sheet, 13, i + 6, Double.parseDouble(reportBean
						.getChqRet()));
				addNumber(sheet, 14, i + 6, Double.parseDouble(reportBean
						.getCredit()));
				addNumber(sheet, 15, i + 6, Double.parseDouble(reportBean
						.getDebit()));
				addNumber(sheet, 16, i + 6, Double.parseDouble(reportBean
						.getRecTotal()));
				addNumber(sheet, 17, i + 6, Double.parseDouble(reportBean
						.getGrandTotal()));

			}
			if (reportBean.getIsDraw() && !reportBean.getIsScratch()) {
				addLabel(sheet, 0, i + 6, reportBean.getSrNo());
				addLabel(sheet, 1, i + 6, reportBean.getName());
				addNumber(sheet, 2, i + 6, Double.parseDouble(reportBean
						.getOpenBal()));
				addNumber(sheet, 3, i + 6, Double.parseDouble(reportBean
						.getDrawSale()));
				addNumber(sheet, 4, i + 6, Double.parseDouble(reportBean
						.getDrawSaleRefund()));
				addNumber(sheet, 5, i + 6, Double.parseDouble(reportBean
						.getDrawPwt()));
				addNumber(sheet, 6, i + 6, Double.parseDouble(reportBean
						.getDrawTotal()));
				addNumber(sheet, 7, i + 6, Double.parseDouble(reportBean
						.getCash()));
				addNumber(sheet, 8, i + 6, Double.parseDouble(reportBean
						.getChq()));
				addNumber(sheet, 9, i + 6, Double.parseDouble(reportBean
						.getChqRet()));
				addNumber(sheet, 10, i + 6, Double.parseDouble(reportBean
						.getCredit()));
				addNumber(sheet, 11, i + 6, Double.parseDouble(reportBean
						.getDebit()));
				addNumber(sheet, 12, i + 6, Double.parseDouble(reportBean
						.getRecTotal()));
				addNumber(sheet, 13, i + 6, Double.parseDouble(reportBean
						.getGrandTotal()));
			}
			if (reportBean.getIsScratch() && !reportBean.getIsDraw()) {
				addLabel(sheet, 0, i + 6, reportBean.getSrNo());
				addLabel(sheet, 1, i + 6, reportBean.getName());
				addNumber(sheet, 2, i + 6, Double.parseDouble(reportBean
						.getOpenBal()));
				addNumber(sheet, 3, i + 6, Double.parseDouble(reportBean
						.getScratchSale()));
				addNumber(sheet, 4, i + 6, Double.parseDouble(reportBean
						.getSaleRet()));
				addNumber(sheet, 5, i + 6, Double.parseDouble(reportBean
						.getScratchPwt()));
				addNumber(sheet, 6, i + 6, Double.parseDouble(reportBean
						.getScratchTotal()));
				addNumber(sheet, 7, i + 6, Double.parseDouble(reportBean
						.getCash()));
				addNumber(sheet, 8, i + 6, Double.parseDouble(reportBean
						.getChq()));
				addNumber(sheet, 9, i + 6, Double.parseDouble(reportBean
						.getChqRet()));
				addNumber(sheet, 10, i + 6, Double.parseDouble(reportBean
						.getCredit()));
				addNumber(sheet, 11, i + 6, Double.parseDouble(reportBean
						.getDebit()));
				addNumber(sheet, 12, i + 6, Double.parseDouble(reportBean
						.getRecTotal()));
				addNumber(sheet, 13, i + 6, Double.parseDouble(reportBean
						.getGrandTotal()));
			}
		}

		sheet.addCell(new Label(1, length + 6, findDefaultText("label.total", locale), headingLabel));
		sheet.mergeCells(1, length + 6, 1, length + 6);

		if (reportBean.getIsDraw() && reportBean.getIsScratch()) {
			sheet.addCell(new Number(2, length + 6, netOpenBal,
					headingNumberFormat));
			sheet.addCell(new Number(3, length + 6, netDrawSale,
					headingNumberFormat));
			sheet.addCell(new Number(4, length + 6, netDrawSaleRefund,
					headingNumberFormat));
			sheet.addCell(new Number(5, length + 6, netDrawPwt,
					headingNumberFormat));
			sheet.addCell(new Number(6, length + 6, netDrawTotal,
					headingNumberFormat));
			sheet.addCell(new Number(7, length + 6, netScratchSale,
					headingNumberFormat));
			sheet.addCell(new Number(8, length + 6, netSaleRet,
					headingNumberFormat));
			sheet.addCell(new Number(9, length + 6, netScratchPwt,
					headingNumberFormat));
			sheet.addCell(new Number(10, length + 6, netScratchTotal,
					headingNumberFormat));
			sheet.addCell(new Number(11, length + 6, netCash,
					headingNumberFormat));
			sheet.addCell(new Number(12, length + 6, netChq,
					headingNumberFormat));
			sheet.addCell(new Number(13, length + 6, netChqRet,
					headingNumberFormat));
			sheet.addCell(new Number(14, length + 6, netCredit,
					headingNumberFormat));
			sheet.addCell(new Number(15, length + 6, netDebit,
					headingNumberFormat));
			sheet.addCell(new Number(16, length + 6, netAccTotal,
					headingNumberFormat));
			sheet.addCell(new Number(17, length + 6, netGrandTotal,
					headingNumberFormat));
		}
		if (reportBean.getIsDraw() && !reportBean.getIsScratch()) {
			sheet.addCell(new Number(2, length + 6, netOpenBal,
					headingNumberFormat));
			sheet.addCell(new Number(3, length + 6, netDrawSale,
					headingNumberFormat));
			sheet.addCell(new Number(4, length + 6, netDrawSaleRefund,
					headingNumberFormat));
			sheet.addCell(new Number(5, length + 6, netDrawPwt,
					headingNumberFormat));
			sheet.addCell(new Number(6, length + 6, netDrawTotal,
					headingNumberFormat));
			sheet.addCell(new Number(7, length + 6, netCash,
					headingNumberFormat));
			sheet
					.addCell(new Number(8, length + 6, netChq,
							headingNumberFormat));
			sheet.addCell(new Number(9, length + 6, netChqRet,
					headingNumberFormat));
			sheet.addCell(new Number(10, length + 6, netCredit,
					headingNumberFormat));
			sheet.addCell(new Number(11, length + 6, netDebit,
					headingNumberFormat));
			sheet.addCell(new Number(12, length + 6, netAccTotal,
					headingNumberFormat));
			sheet.addCell(new Number(13, length + 6, netGrandTotal,
					headingNumberFormat));
		}
		if (reportBean.getIsScratch() && !reportBean.getIsDraw()) {
			sheet.addCell(new Number(2, length + 6, netOpenBal,
					headingNumberFormat));
			sheet.addCell(new Number(3, length + 6, netScratchSale,
					headingNumberFormat));
			sheet.addCell(new Number(4, length + 6, netSaleRet,
					headingNumberFormat));
			sheet.addCell(new Number(5, length + 6, netScratchPwt,
					headingNumberFormat));
			sheet.addCell(new Number(6, length + 6, netScratchTotal,
					headingNumberFormat));
			sheet.addCell(new Number(7, length + 6, netCash,
					headingNumberFormat));
			sheet
					.addCell(new Number(8, length + 6, netChq,
							headingNumberFormat));
			sheet.addCell(new Number(9, length + 6, netChqRet,
					headingNumberFormat));
			sheet.addCell(new Number(10, length + 6, netCredit,
					headingNumberFormat));
			sheet.addCell(new Number(11, length + 6, netDebit,
					headingNumberFormat));
			sheet.addCell(new Number(12, length + 6, netAccTotal,
					headingNumberFormat));
			sheet.addCell(new Number(13, length + 6, netGrandTotal,
					headingNumberFormat));
		}
	}

	private void createHeaderForCollection(WritableSheet sheet)
			throws WriteException, ParseException {
		DateFormat format = new SimpleDateFormat("dd-MMM-yy");

		sheet.addCell(new Label(10, 0,
				" Date  :  " + format.format(new Date()), dateFormat));
		sheet.mergeCells(10, 0, 11, 0);

		sheet.mergeCells(4, 3, 6, 3);
		sheet.mergeCells(7, 3, 9, 3);
		setHeadingForCollection(sheet);

	}

	private void setHeadingForCollection(WritableSheet sheet)
			throws RowsExceededException, WriteException {
		DateFormat format = new SimpleDateFormat("dd-MMM-yy");
		if (this.endDate == null) {

			logger
					.debug(" inside date if condition -kgkgjdkjkjkfj---- ----user Type : "
							+ this.reportType + " date : " + startDate);
			// System.out.println(" inside date if condition -kgkgjdkjkjkfj----
			// ----user Type : "+this.reportType+" date : "+startDate);
			sheet.addCell(new Label(4, 3, findDefaultText("label.colln.sum.rep.of", locale)+" "
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
			sheet.addCell(new Label(4, 3,
					findDefaultText("label.colln.sum.rep.from.date", locale) + "", headerFormat));
			sheet.addCell(new Label(7, 3, " ( " + format.format(startDate)
					+ "   -   " + format.format(endDate) + " ) ",
					headerDateFormat));
		}
	}

	public void write(List<CollectionReportBean> reportbean,
			WritableWorkbook workbk, String orgName, String orgAddress,
			String orgType, String currSym) throws IOException, WriteException,
			ParseException {
		WorkbookSettings wbSettings = new WorkbookSettings();
		wbSettings.setLocale(new Locale("en", "EN"));
		WritableWorkbook workbook = workbk;
		workbook.createSheet(findDefaultText("Report", locale), 0);
		WritableSheet excelSheet = workbook.getSheet(0);
		if (orgType.equalsIgnoreCase("BO")) {
			createContent(excelSheet, reportbean, orgName, orgAddress, currSym);
		} else {
			createContentForAgent(excelSheet, reportbean, orgName, orgAddress);
		}

		workbook.write();
		workbook.close();

	}

}
