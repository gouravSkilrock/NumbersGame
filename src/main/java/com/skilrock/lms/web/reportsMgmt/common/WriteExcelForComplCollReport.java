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

import com.skilrock.lms.beans.CashChqReportBean;
import com.skilrock.lms.beans.CompleteCollectionBean;
import com.skilrock.lms.beans.DateBeans;
import com.skilrock.lms.common.MyTextProvider;
import com.skilrock.lms.common.utility.TextConfigurator;

public class WriteExcelForComplCollReport extends MyTextProvider {
	private WritableCellFormat dateFormat;
	private int mainReportLength = 3;
	private WritableCellFormat headerDateFormat;
	private WritableCellFormat headerFormat;
	private WritableCellFormat headingLabel;
	private WritableCellFormat headingNumberFormat;
	Log logger = LogFactory.getLog(WriteExcelForComplCollReport.class);
	private WritableCellFormat numberFormat;
	private String reportType;
	private java.util.Date startDate, endDate, reportday;
	private WritableCellFormat times;
	private WritableCellFormat timesBoldUnderline;
	
	private Locale locale;

	public WriteExcelForComplCollReport(DateBeans dateBeans)
			throws WriteException {
		
		locale = Locale.getDefault();

		this.reportType = dateBeans.getReportType();
		this.startDate = dateBeans.getStartDate();
		this.endDate = dateBeans.getEndDate();
		this.reportday = dateBeans.getReportday();

		numberFormat = new WritableCellFormat(NumberFormats.FORMAT3);
		numberFormat.setFont(new WritableFont(WritableFont.TIMES, 12));
		numberFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
		numberFormat.setWrap(false);

		times = new WritableCellFormat(new WritableFont(WritableFont.TIMES, 12));
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
		headingLabel.setAlignment(Alignment.LEFT);
		headingLabel.setBackground(Colour.GRAY_25);

		headingNumberFormat = new WritableCellFormat(NumberFormats.FORMAT3);
		headingNumberFormat.setFont(new WritableFont(WritableFont.TIMES, 12));
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
	private void addCaption(WritableSheet sheet, int column, int row, String s)
			throws RowsExceededException, WriteException {
		Label headingLabel;
		headingLabel = new Label(column, row, s, timesBoldUnderline);
		if (s.equalsIgnoreCase("Agent Name")) {
			sheet.setColumnView(column, 30);
		} else {
			sheet.setColumnView(column, 20);
		}
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
			Map<String, CompleteCollectionBean> reportMap, String orgName,
			String orgAdd, boolean isSE, boolean isDG, boolean isCS,
			boolean isOLA, boolean isSLE, boolean isIW) throws WriteException,
			RowsExceededException, ParseException {
		sheet.addCell(new Label(2, 1, orgName, headerFormat));
		sheet.mergeCells(2, 1, 10, 1);

		sheet.addCell(new Label(2, 2, orgAdd, headerFormat));
		sheet.mergeCells(2, 2, 10, 2);
		createHeaderForComplCollReport(sheet);

		double totalDrawSale = 0.0;
		double totalDrawPwt = 0.0;
		double totalScratchSale = 0.0;
		double totalScratchPwt = 0.0;
		double totalOLADeposit = 0.0;
		double totalOLAWithdraw = 0.0;
		double totalOLANetGaming = 0.0;
		double totalCSSale = 0.0;
		double totalCashInHand = 0.0;

		double totalSleSale = 0.0;
		double totalSlePwt = 0.0;
		
		double totalIwSale = 0.0;
		double totalIwPwt = 0.0;

		// add caption
		addCaption(sheet, 1, 5, getText("label.party.name"));
		sheet.mergeCells(1, 5, 3, 7);
		int length = reportMap.size();
		int colIndex = 4;
		if (isDG) {
			addCaption(sheet, colIndex, 6, getText("label.draw.game"));
			sheet.mergeCells(colIndex, 6, colIndex+1, 6);
			addCaption(sheet, colIndex, 7, getText("label.sale.amt"));
			addCaption(sheet, colIndex+1, 7, getText("label.pwt.amt"));
			colIndex += 2;
		}
		if (isSE) {
			addCaption(sheet, colIndex, 6, getText("label.scratch.game"));
			sheet.mergeCells(colIndex, 6, colIndex+1, 6);
			addCaption(sheet, colIndex, 7, getText("label.sale.amt"));
			addCaption(sheet, colIndex+1, 7, getText("label.pwt.amt"));
			colIndex += 2;
		}
		if (isCS) {
			addCaption(sheet, colIndex, 6, getText("label.com.srv"));
			sheet.mergeCells(colIndex, 6, colIndex, 6);
			addCaption(sheet, colIndex, 7, getText("label.sale.amt"));
			colIndex += 1;
		}
		if (isOLA) {
			addCaption(sheet, colIndex, 6, getText("label.offline.aff"));
			sheet.mergeCells(colIndex, 6, colIndex+2, 6);
			addCaption(sheet, colIndex, 7, getText("label.deposit"));
			addCaption(sheet, colIndex+1, 7, getText("label.wdl.amt"));
			addCaption(sheet, colIndex+2, 7, getText("label.net.gaming.amt"));
			colIndex += 3;
		}
		if(isSLE){ 
			addCaption(sheet, colIndex, 6, getText("label.sport.lot"));
			sheet.mergeCells(colIndex, 6, colIndex+1, 6);
			addCaption(sheet, colIndex, 7, getText("label.sale.amt"));
			addCaption(sheet, colIndex+1, 7, getText("label.pwt.amt"));
			colIndex += 2;
		}
		if(isIW){ 
			addCaption(sheet, colIndex, 6, getText("label.instant.win"));
			sheet.mergeCells(colIndex, 6, colIndex+1, 6);
			addCaption(sheet, colIndex, 7, getText("label.sale.amt"));
			addCaption(sheet, colIndex+1, 7, getText("label.pwt.amt"));
			colIndex += 2;
		}
		
		addCaption(sheet, 4, 5, getText("label.srv.name"));
		sheet.mergeCells(4, 5, colIndex-1, 5);
		
		addCaption(sheet, colIndex, 5, getText("label.net.amt"));
		sheet.mergeCells(colIndex, 5, colIndex, 7);

		int i = 0;
		Iterator<Map.Entry<String, CompleteCollectionBean>> it = reportMap
				.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, CompleteCollectionBean> pair = it.next();
			CompleteCollectionBean bean = new CompleteCollectionBean();
			bean = pair.getValue();
			addLabel(sheet, 1, i + 8, bean.getOrgName());
			sheet.mergeCells(1, i + 8, 3, i + 8);
			
			int dataStartIndex = 4;
			if (isDG) {
				addNumber(sheet, dataStartIndex, i + 8, bean.getDrawSale() - bean.getDrawCancel());
				totalDrawSale += bean.getDrawSale() - bean.getDrawCancel();
				addNumber(sheet, dataStartIndex+1, i + 8, bean.getDrawPwt() + bean.getDrawDirPlyPwt());
				totalDrawPwt += bean.getDrawPwt() + bean.getDrawDirPlyPwt();
				
				dataStartIndex += 2;
			}
			if (isSE) {
				addNumber(sheet, dataStartIndex, i + 8, bean.getScratchSale());
				totalScratchSale += bean.getScratchSale();
				addNumber(sheet, dataStartIndex+1, i + 8, bean.getScratchPwt() + bean.getScratchDirPlyPwt());
				totalScratchSale += bean.getScratchPwt() + bean.getScratchDirPlyPwt();
				
				dataStartIndex += 2;
			}
			if (isCS) {
				addNumber(sheet, dataStartIndex, i + 8, bean.getCSSale() - bean.getCSCancel());
				totalCSSale += bean.getCSSale() - bean.getCSCancel();
				
				dataStartIndex += 1;
			}
			if (isOLA) {
				addNumber(sheet, dataStartIndex, i + 8, bean.getOlaDepositAmt() - bean.getOlaDepositCancelAmt());
				totalOLADeposit += bean.getOlaDepositAmt() - bean.getOlaDepositCancelAmt();
				addNumber(sheet, dataStartIndex+1, i + 8, bean.getOlaWithdrawalAmt());
				totalOLAWithdraw += bean.getOlaWithdrawalAmt();
				addNumber(sheet, dataStartIndex+2, i + 8, bean.getOlaNetGaming());
				totalOLANetGaming += bean.getOlaNetGaming();
				
				dataStartIndex += 3;
			}
			if (isSLE) { 
				addNumber(sheet, dataStartIndex, i + 8, bean.getSleSale() - bean.getSleCancel()); 
				totalSleSale += bean.getSleSale() - bean.getSleCancel(); 
				addNumber(sheet, dataStartIndex+1, i + 8, bean.getSlePwt() + bean.getSleDirPlyPwt()); 
				totalSlePwt += bean.getSlePwt() + bean.getSleDirPlyPwt();
				
				dataStartIndex += 2;
			}
			if (isIW) { 
				addNumber(sheet, dataStartIndex, i + 8, bean.getIwSale() - bean.getIwCancel()); 
				totalIwSale += bean.getIwSale() - bean.getIwCancel(); 
				addNumber(sheet, dataStartIndex+1, i + 8, bean.getIwPwt() + bean.getIwDirPlyPwt()); 
				totalIwPwt += bean.getIwPwt() + bean.getIwDirPlyPwt();
				
				dataStartIndex += 2;
			}
			
			bean.setTotalCashInHand(bean.getDrawSale()
					- bean.getDrawCancel() - bean.getDrawPwt()
					- bean.getDrawDirPlyPwt() 
					+ bean.getScratchSale() - bean.getScratchPwt()
					- bean.getScratchDirPlyPwt()
					+ bean.getCSSale() - bean.getCSCancel()
					+ bean.getOlaDepositAmt() - bean.getOlaDepositCancelAmt()
					- bean.getOlaWithdrawalAmt() - bean.getOlaNetGaming()
					+ bean.getSleSale()
					- bean.getSleCancel()
					- bean.getSlePwt() - bean.getSleDirPlyPwt()
					+ bean.getIwSale()
					- bean.getIwCancel()
					- bean.getIwPwt() - bean.getIwDirPlyPwt());
			
			addNumber(sheet, dataStartIndex, i + 8, bean.getTotalCashInHand());
			totalCashInHand += bean.getTotalCashInHand();
			sheet.mergeCells(dataStartIndex, i + 8, dataStartIndex, i + 8);

			i++;
		}
		addCaption(sheet, 1, length + 8, getText("label.total"));
		sheet.mergeCells(1, length + 8, 3, length + 8);
		
		colIndex = 4;
		if (isDG) {
			sheet.addCell(new Number(colIndex, length + 8, totalDrawSale,
					headingNumberFormat));
			sheet.addCell(new Number(colIndex+1, length + 8, totalDrawPwt,
					headingNumberFormat));
			colIndex += 2;
		}
		if (isSE) {
			sheet.addCell(new Number(colIndex, length + 8, totalScratchSale,
					headingNumberFormat));
			sheet.addCell(new Number(colIndex+1, length + 8, totalScratchPwt,
					headingNumberFormat));
			colIndex += 2;
		}
		if (isCS) {
			sheet.addCell(new Number(colIndex, length + 8, totalCSSale,
					headingNumberFormat));
			colIndex += 1;
		}
		if (isOLA) {
			sheet.addCell(new Number(colIndex, length + 8, totalOLADeposit,
					headingNumberFormat));
			sheet.addCell(new Number(colIndex+1, length + 8, totalOLAWithdraw,
					headingNumberFormat));
			sheet.addCell(new Number(colIndex+2, length + 8, totalOLANetGaming,
					headingNumberFormat));
			colIndex += 3;
		}
		if (isSLE) {
			sheet.addCell(new Number(colIndex, length + 8, totalSleSale,
					headingNumberFormat));
			sheet.addCell(new Number(colIndex+1, length + 8, totalSlePwt,
					headingNumberFormat));
			colIndex += 2;
		}
		if (isIW) {
			sheet.addCell(new Number(colIndex, length + 8, totalIwSale,
					headingNumberFormat));
			sheet.addCell(new Number(colIndex+1, length + 8, totalIwPwt,
					headingNumberFormat));
			colIndex += 2;
		}
		
		sheet.addCell(new Number(colIndex, length + 8, totalCashInHand,
				headingNumberFormat));
		
		mainReportLength = length + 6 + 2;
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
	private void createContentOfAgentSheet(WritableSheet sheet,
			List<CashChqReportBean> reportlist, CashChqReportBean agentBoDetail)
			throws WriteException, RowsExceededException, ParseException {

		createHeaderForComplCollReport(sheet);
		// add caption

		addCaption(sheet, 0, 4, getText("label.cash.chq.rep.with.bo"));
		sheet.mergeCells(0, 4, 3, 4);

		addCaption(sheet, 0, 5, getText("label.cash.depo"));
		addCaption(sheet, 1, 5, getText("label.chq.depo"));
		addCaption(sheet, 2, 5, getText("label.chq.bounce"));
		addCaption(sheet, 3, 5, getText("Credit_Note"));
		addCaption(sheet, 4, 5, getText("Debit_Note"));
		addCaption(sheet, 5, 5, getText("label.net.depo"));

		if (agentBoDetail != null) {
			addNumber(sheet, 0, 6, Double.parseDouble(agentBoDetail
					.getTotalCash()));
			addNumber(sheet, 1, 6, Double.parseDouble(agentBoDetail
					.getTotalChq()));
			addNumber(sheet, 2, 6, Double.parseDouble(agentBoDetail
					.getCheqBounce()));
			addNumber(sheet, 3, 6, Double
					.parseDouble(agentBoDetail.getCredit()));
			addNumber(sheet, 4, 6, Double.parseDouble(agentBoDetail.getDebit()));
			addNumber(sheet, 5, 6, Double
					.parseDouble(agentBoDetail.getNetAmt()));
		} else {
			addNumber(sheet, 0, 6, 0.0);
			addNumber(sheet, 1, 6, 0.0);
			addNumber(sheet, 2, 6, 0.0);
			addNumber(sheet, 3, 6, 0.0);
			addNumber(sheet, 4, 6, 0.0);
			addNumber(sheet, 5, 6, 0.0);
		}

		addCaption(sheet, 0, 8, getText("label.cash.chq") + " "
				+ TextConfigurator.getText("Collection") + " "
				+ getText("label.from") + " "
				+ getText("Retailer"));
		sheet.mergeCells(0, 8, 4, 8);

		addCaption(sheet, 0, 9, getText("label.party.name"));
		addCaption(sheet, 1, 9, getText("label.cash.colln"));
		addCaption(sheet, 2, 9, getText("label.chq.colln"));
		addCaption(sheet, 3, 9, getText("label.chq.bounce"));
		addCaption(sheet, 4, 9, getText("Debit_Note"));
		addCaption(sheet, 5, 9, getText("label.net.coll"));

		double cashColl = 0.0;
		double chqColl = 0.0;
		double chqBounce = 0.0;
		double netColl = 0.0;
		double netDebit = 0.0;

		int length = reportlist.size();
		CashChqReportBean bean = null;
		// sheet.insertRow(1);
		for (int i = 0; i < length; i++) {
			bean = reportlist.get(i);

			cashColl += Double.parseDouble(bean.getTotalCash());
			chqColl += Double.parseDouble(bean.getTotalChq());
			chqBounce += Double.parseDouble(bean.getCheqBounce());
			netColl += Double.parseDouble(bean.getNetAmt());
			netDebit += Double.parseDouble(bean.getDebit());

			addLabel(sheet, 0, i + 10, bean.getName());
			addNumber(sheet, 1, i + 10, Double.parseDouble(bean.getTotalCash()));
			addNumber(sheet, 2, i + 10, Double.parseDouble(bean.getTotalChq()));
			addNumber(sheet, 3, i + 10, Double
					.parseDouble(bean.getCheqBounce()));
			addNumber(sheet, 4, i + 10, Double.parseDouble(bean.getDebit()));
			addNumber(sheet, 5, i + 10, Double.parseDouble(bean.getNetAmt()));

		}

		sheet.addCell(new Label(0, length + 10, getText("label.total"), headingLabel));
		sheet
				.addCell(new Number(1, length + 10, cashColl,
						headingNumberFormat));
		sheet.addCell(new Number(2, length + 10, chqColl, headingNumberFormat));
		sheet
				.addCell(new Number(3, length + 10, chqBounce,
						headingNumberFormat));
		sheet
				.addCell(new Number(4, length + 10, netDebit,
						headingNumberFormat));
		sheet.addCell(new Number(5, length + 10, netColl, headingNumberFormat));

	}

	private void createDetailContentForAgentWise(WritableSheet sheet,
			CompleteCollectionBean agtTotalBean,
			Map<String, CompleteCollectionBean> detailDataMap, String orgName,
			String orgAdd, Double drawDirPlrPwt, Double scratchDirPlrPwt,
			Double sleDirPlrPwt,
			Double iwDirPlrPwt, boolean isSE, boolean isDG, boolean isCS,
			boolean isOLA, boolean isSLE, boolean isIW) throws WriteException,
			RowsExceededException, ParseException {
		createHeaderForComplCollReport(sheet);

		sheet.addCell(new Label(2, 1, orgName, headerFormat));
		sheet.mergeCells(2, 1, 10, 1);

		sheet.addCell(new Label(2, 2, orgAdd, headerFormat));
		sheet.mergeCells(2, 2, 10, 2);
		createHeaderForComplCollReport(sheet);

		double totalDrawSale = 0.0;
		double totalDrawPwt = 0.0;
		double totalScratchSale = 0.0;
		double totalScratchPwt = 0.0;
		double totalCSSale = 0.0;
		double totalCashInHand = 0.0;
		double totalOLADeposit = 0.0;
		double totalOLAWithdraw = 0.0;
		double totalOLANetGaming = 0.0;

		double totalSleSale = 0.0;
		double totalSlePwt = 0.0;
		
		double totalIwSale = 0.0;
		double totalIwPwt = 0.0;

		// add caption
		addCaption(sheet, 1, 5, getText("label.party.name"));
		sheet.mergeCells(1, 5, 3, 7);
		int colIndex = 4;
		if (isDG) {
			addCaption(sheet, colIndex, 6, getText("label.draw.game"));
			sheet.mergeCells(colIndex, 6, colIndex+1, 6);
			addCaption(sheet, colIndex, 7, getText("label.sale.amt"));
			addCaption(sheet, colIndex+1, 7, getText("label.pwt.amt"));
			colIndex += 2;
		}
		if (isSE) {
			addCaption(sheet, colIndex, 6, getText("label.scratch.game"));
			sheet.mergeCells(colIndex, 6, colIndex+1, 6);
			addCaption(sheet, colIndex, 7, getText("label.sale.amt"));
			addCaption(sheet, colIndex+1, 7, getText("label.pwt.amt"));
			colIndex += 2;
		}
		if (isCS) {
			addCaption(sheet, colIndex, 6, getText("label.com.srv"));
			sheet.mergeCells(colIndex, 6, colIndex, 6);
			addCaption(sheet, colIndex, 7, getText("label.sale.amt"));
			colIndex += 1;
		}
		if (isOLA) {
			addCaption(sheet, colIndex, 6, getText("label.offline.aff"));
			sheet.mergeCells(colIndex, 6, colIndex+2, 6);
			addCaption(sheet, colIndex, 7, getText("label.deposit"));
			addCaption(sheet, colIndex+1, 7, getText("label.wdl.amt"));
			addCaption(sheet, colIndex+2, 7, getText("label.net.gaming.amt"));
			colIndex += 3;
		}
		if(isSLE){ 
			addCaption(sheet, colIndex, 6, getText("label.sport.lot"));
			sheet.mergeCells(colIndex, 6, colIndex+1, 6);
			addCaption(sheet, colIndex, 7, getText("label.sale.amt"));
			addCaption(sheet, colIndex+1, 7, getText("label.pwt.amt"));
			colIndex += 2;
		}
		if(isIW){ 
			addCaption(sheet, colIndex, 6, getText("label.instant.win"));
			sheet.mergeCells(colIndex, 6, colIndex+1, 6);
			addCaption(sheet, colIndex, 7, getText("label.sale.amt"));
			addCaption(sheet, colIndex+1, 7, getText("label.pwt.amt"));
			colIndex += 2;
		}
		
		addCaption(sheet, 4, 5, getText("label.srv.name"));
		sheet.mergeCells(4, 5, colIndex-1, 5);
		
		addCaption(sheet, colIndex, 5, getText("label.net.amt"));
		sheet.mergeCells(colIndex, 5, colIndex, 7);
		
		if (isDG) {
			sheet.mergeCells(4, 5, 5, 5);
			addCaption(sheet, 4, 6, getText("label.draw.game"));
		}
		if (isSE) {
			sheet.mergeCells(4, 5, 5, 5);
			addCaption(sheet, 4, 6, getText("label.scratch.game"));
		}
		if (isCS) {
			sheet.mergeCells(4, 5, 5, 5);
			addCaption(sheet, 4, 6, getText("label.com.srv"));
		}
		if (isOLA) {
			sheet.mergeCells(4, 5, 6, 5);
			addCaption(sheet, 4, 6, getText("label.offline.aff"));
		}
		if (isSLE) {
			sheet.mergeCells(4, 5, 5, 5);
			addCaption(sheet, 4, 6, getText("label.sport.lot"));
		}
		if (isIW) {
			sheet.mergeCells(4, 5, 5, 5);
			addCaption(sheet, 4, 6, getText("label.instant.win"));
		}

		int length = detailDataMap.size();
		int i = 0;
		Iterator<Map.Entry<String, CompleteCollectionBean>> it = detailDataMap
				.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, CompleteCollectionBean> pair = it.next();
			CompleteCollectionBean bean = new CompleteCollectionBean();
			bean = pair.getValue();
			addLabel(sheet, 1, i + 8, bean.getOrgName());
			sheet.mergeCells(1, i + 8, 3, i + 8);
			int dataStartIndex = 4;
			if (isDG) {
				addNumber(sheet, dataStartIndex, i + 8, bean.getDrawSale()
						- bean.getDrawCancel());
				totalDrawSale += bean.getDrawSale() - bean.getDrawCancel();
				addNumber(sheet, dataStartIndex+1, i + 8, bean.getDrawPwt()
						+ bean.getDrawDirPlyPwt());
				totalDrawPwt += bean.getDrawPwt() + bean.getDrawDirPlyPwt();
				dataStartIndex += 2;
			}
			if (isSE) {
				addNumber(sheet, dataStartIndex, i + 8, bean.getScratchSale());
				totalScratchSale += bean.getScratchSale();
				addNumber(sheet, dataStartIndex+1, i + 8, bean.getScratchPwt()
						+ bean.getScratchDirPlyPwt());
				dataStartIndex += 2;
			}
			if (isCS) {
				addNumber(sheet, dataStartIndex, i + 8, bean.getCSSale()
						- bean.getCSCancel());
				totalCSSale += bean.getCSSale() - bean.getCSCancel();
				dataStartIndex += 1; 
			}
			if (isOLA) {
				addNumber(sheet, dataStartIndex, i + 8, bean.getOlaDepositAmt()
						- bean.getOlaDepositCancelAmt());
				totalOLADeposit += bean.getOlaDepositAmt()
						- bean.getOlaDepositCancelAmt();
				addNumber(sheet, dataStartIndex+1, i + 8, bean.getOlaWithdrawalAmt());
				totalOLAWithdraw += bean.getOlaWithdrawalAmt();
				addNumber(sheet, dataStartIndex+2, i + 8, bean.getOlaNetGaming());
				totalOLANetGaming += bean.getOlaNetGaming();
				dataStartIndex += 3; 
			}
			if (isSLE) {
				addNumber(sheet, dataStartIndex, i + 8, bean.getSleSale()
						- bean.getSleCancel());
				totalSleSale += bean.getSleSale() - bean.getSleCancel();
				addNumber(sheet, dataStartIndex+1, i + 8, bean.getSlePwt()
						+ bean.getSleDirPlyPwt());
				totalSlePwt += bean.getSlePwt() + bean.getSleDirPlyPwt();
				dataStartIndex += 2; 
			}
			if (isIW) {
				addNumber(sheet, dataStartIndex, i + 8, bean.getIwSale() - bean.getIwCancel());
				totalIwSale += bean.getIwSale() - bean.getIwCancel();
				addNumber(sheet, dataStartIndex+1, i + 8, bean.getIwPwt() + bean.getIwDirPlyPwt());
				totalIwPwt += bean.getIwPwt() + bean.getIwDirPlyPwt();
				dataStartIndex += 2; 
			}
			bean.setTotalCashInHand(bean.getDrawSale()
					- bean.getDrawCancel() - bean.getDrawPwt()
					- bean.getDrawDirPlyPwt() 
					+ bean.getScratchSale() - bean.getScratchPwt()
					- bean.getScratchDirPlyPwt()
					+ bean.getCSSale() - bean.getCSCancel()
					+ bean.getOlaDepositAmt() - bean.getOlaDepositCancelAmt()
					- bean.getOlaWithdrawalAmt() - bean.getOlaNetGaming()
					+ bean.getSleSale()
					- bean.getSleCancel()
					- bean.getSlePwt() - bean.getSleDirPlyPwt()
					+ bean.getIwSale()
					- bean.getIwCancel()
					- bean.getIwPwt() - bean.getIwDirPlyPwt());
			
			addNumber(sheet, dataStartIndex, i + 8, bean.getTotalCashInHand());
			totalCashInHand += bean.getTotalCashInHand();
			sheet.mergeCells(dataStartIndex, i + 8, dataStartIndex, i + 8);
			i++;
		}
		colIndex = 4;
		addCaption(sheet, 1, length + 8, getText("label.total"));
		sheet.mergeCells(1, length + 8, 3, length + 8);
		addCaption(sheet, 1, length + 9, getText("label.agt.total"));
		sheet.mergeCells(1, length + 9, 3, length + 9);
		if (isDG) {
			sheet.addCell(new Number(colIndex, length + 8, totalDrawSale, headingNumberFormat));
			sheet.addCell(new Number(colIndex+1, length + 8, totalDrawPwt, headingNumberFormat));
			
			sheet.addCell(new Number(colIndex, length + 9, agtTotalBean.getDrawSale()
					- agtTotalBean.getDrawCancel(), headingNumberFormat));
			sheet.addCell(new Number(colIndex+1, length + 9, agtTotalBean.getDrawPwt()
					+ agtTotalBean.getDrawDirPlyPwt(), headingNumberFormat));
			colIndex += 2;
		}
		if (isSE) {
			sheet.addCell(new Number(colIndex, length + 8, totalScratchSale, headingNumberFormat));
			sheet.addCell(new Number(colIndex+1, length + 8, totalScratchPwt, headingNumberFormat));
			
			sheet.addCell(new Number(colIndex, length + 9, agtTotalBean.getScratchSale(), headingNumberFormat));
			sheet.addCell(new Number(colIndex+1, length + 9, agtTotalBean.getScratchPwt()
					+ agtTotalBean.getScratchDirPlyPwt(), headingNumberFormat));
			colIndex += 2;
		}
		if (isCS) {
			sheet.addCell(new Number(colIndex, length + 8, totalCSSale, headingNumberFormat));
			
			sheet.addCell(new Number(colIndex, length + 9, agtTotalBean.getCSSale()
					- agtTotalBean.getCSCancel(), headingNumberFormat));
			colIndex += 1;
		}
		if (isOLA) {
			sheet.addCell(new Number(colIndex, length + 8, totalOLADeposit, headingNumberFormat));
			sheet.addCell(new Number(colIndex+1, length + 8, totalOLAWithdraw, headingNumberFormat));
			sheet.addCell(new Number(colIndex+2, length + 8, totalOLANetGaming, headingNumberFormat));
			
			sheet.addCell(new Number(colIndex, length + 9, agtTotalBean.getOlaDepositAmt()
					- agtTotalBean.getOlaDepositCancelAmt(),headingNumberFormat));
			sheet.addCell(new Number(colIndex+1, length + 9, agtTotalBean.getOlaWithdrawalAmt(), headingNumberFormat));
			sheet.addCell(new Number(colIndex+2, length + 9, agtTotalBean.getOlaNetGaming(), headingNumberFormat));
			colIndex += 3;
		}
		if (isSLE) {
			sheet.addCell(new Number(colIndex, length + 8, totalSleSale, headingNumberFormat));
			sheet.addCell(new Number(colIndex+1, length + 8, totalSlePwt, headingNumberFormat));
			
			sheet.addCell(new Number(colIndex, length + 9, agtTotalBean.getSleSale()
					- agtTotalBean.getSleCancel(), headingNumberFormat));
			sheet.addCell(new Number(colIndex+1, length + 9, agtTotalBean.getSlePwt()
					+ agtTotalBean.getSleDirPlyPwt(), headingNumberFormat));
			colIndex += 2;
		}
		if (isIW) {
			sheet.addCell(new Number(colIndex, length + 8, totalIwSale, headingNumberFormat));
			sheet.addCell(new Number(colIndex+1, length + 8, totalIwPwt, headingNumberFormat));
			
			sheet.addCell(new Number(colIndex, length + 9, agtTotalBean.getIwSale() - agtTotalBean.getIwCancel(), headingNumberFormat));
			sheet.addCell(new Number(colIndex+1, length + 9, agtTotalBean.getIwPwt() + agtTotalBean.getIwDirPlyPwt(), headingNumberFormat));
			colIndex += 2;
		}
		sheet.addCell(new Number(colIndex, length + 8, totalCashInHand, headingNumberFormat));
		sheet.addCell(new Number(colIndex, length + 9, agtTotalBean.getDrawSale()
				- agtTotalBean.getDrawCancel() - agtTotalBean.getDrawPwt()
				- agtTotalBean.getDrawDirPlyPwt() 
				+ agtTotalBean.getScratchSale() - agtTotalBean.getScratchPwt()
				- agtTotalBean.getScratchDirPlyPwt()
				+ agtTotalBean.getCSSale() - agtTotalBean.getCSCancel()
				+ agtTotalBean.getOlaDepositAmt() - agtTotalBean.getOlaDepositCancelAmt()
				- agtTotalBean.getOlaWithdrawalAmt() - agtTotalBean.getOlaNetGaming()
				+ agtTotalBean.getSleSale() - agtTotalBean.getSleCancel()
				- agtTotalBean.getSlePwt() - agtTotalBean.getSleDirPlyPwt()
				+ agtTotalBean.getIwSale() - agtTotalBean.getIwCancel()
				- agtTotalBean.getIwPwt() - agtTotalBean.getIwDirPlyPwt(),
				headingNumberFormat));
		
		colIndex = 4;
		
		sheet.addCell(new Label(colIndex, length + 11, 
				getText("label.rep.for.pwt.by.plr"), headerFormat));
		
		addCaption(sheet, colIndex, length + 12, getText("label.org.name"));
		sheet.mergeCells(colIndex, length + 12, colIndex+1, length + 12);
		addLabel(sheet, colIndex, length + 13, orgName);
		sheet.mergeCells(colIndex, length + 13, colIndex+1, length + 12);
		colIndex += 2;
		if (isDG) {
			addCaption(sheet, colIndex, length + 12, getText("label.draw.game"));
			sheet.mergeCells(colIndex, length + 12, colIndex+1, length + 12);
			addNumber(sheet, colIndex, length + 13, drawDirPlrPwt);
			sheet.mergeCells(colIndex, length + 13, colIndex+1, length + 13);
			colIndex += 2;
		}
		if (isSE) {
			addCaption(sheet, colIndex, length + 12, getText("label.scratch.game"));
			sheet.mergeCells(colIndex, length + 12, colIndex+1, length + 12);
			addNumber(sheet, colIndex, length + 13, scratchDirPlrPwt);
			sheet.mergeCells(colIndex, length + 13, colIndex+1, length + 13);
			colIndex += 2;
		}
		if (isSLE) {
			addCaption(sheet,colIndex, length + 12, getText("label.sport.lot"));
			sheet.mergeCells(colIndex, length + 12, colIndex+1, length + 12);
			addNumber(sheet, colIndex, length + 13, sleDirPlrPwt);
			sheet.mergeCells(colIndex, length + 13, colIndex+1, length + 13);
			colIndex += 2;
		}
		if (isIW) {
			addCaption(sheet,colIndex, length + 12, getText("label.instant.win"));
			sheet.mergeCells(colIndex, length + 12, colIndex+1, length + 12);
			addNumber(sheet, colIndex, length + 13, sleDirPlrPwt);
			sheet.mergeCells(colIndex, length + 13, colIndex+1, length + 13);
			colIndex += 2;
		}
		//Merging columns for direct player heading! 
		sheet.mergeCells(4, length + 11, colIndex-1, length + 11);
	}

	private int createDetailContentForDayWise(WritableSheet sheet,
			Map<String, CompleteCollectionBean> dataMap, String orgName,
			String orgAdd, boolean isSE, boolean isDG, boolean isCS,
			boolean isOLA, boolean isSLE, boolean isIW, int length) throws WriteException,
			RowsExceededException, ParseException {
		// createHeaderForComplCollReport(sheet);

		sheet.addCell(new Label(2, length + 1, getText("label.sale")
				+ " "
				+ TextConfigurator.getText("PWT")
				+ " "
				+ TextConfigurator.getText("Report")
				+ " "
				+ getText("label.day.wise")
				+ " "
				+ getText("label.for") + " " + orgName,
				headerFormat));
		sheet.mergeCells(2, length + 1, 6, length + 1);

		double totalDrawSale = 0.0;
		double totalDrawPwt = 0.0;
		double totalScratchSale = 0.0;
		double totalScratchPwt = 0.0;
		double totalCSSale = 0.0;
		double totalCashInHand = 0.0;
		double totalOLADeposit = 0.0;
		double totalOLAWithdraw = 0.0;
		double totalOLANetGaming = 0.0;

		double totalSleSale = 0.0;
		double totalSlePwt = 0.0;
		
		double totalIwSale = 0.0;
		double totalIwPwt = 0.0;

		// add caption
		addCaption(sheet, 1, length + 5, getText("label.party.name"));
		sheet.mergeCells(1, length + 5, 3, length + 7);
		addCaption(sheet, 4, length + 5, getText("label.srv.name"));
		int len = dataMap.size();
		if (isSE && isDG && isCS) {
			sheet.mergeCells(4, length + 5, 7, length + 5);
			addCaption(sheet, 4, length + 6, getText("label.draw.game"));
			sheet.mergeCells(4, length + 6, 5, length + 6);
			addCaption(sheet, 6, length + 6, getText(
					"label.scratch.game"));
			sheet.mergeCells(6, length + 6, 7, length + 6);
			addCaption(sheet, 7, length + 6, getText("label.com.srv"));
			sheet.mergeCells(7, length + 6, 7, length + 6);

			addCaption(sheet, 8, length + 5, getText("label.net.amt"));
			sheet.mergeCells(8, length + 5, 9, length + 7);

			addCaption(sheet, 4, length + 7, getText("label.sale.amt"));
			addCaption(sheet, 5, length + 7, getText("label.pwt.amt"));
			addCaption(sheet, 6, length + 7, getText("label.sale.amt"));
			addCaption(sheet, 7, length + 7, getText("label.pwt.amt"));
			addCaption(sheet, 8, length + 7, getText("label.sale.amt"));

			if (len == 0) {
				addLabel(sheet, 1, length + 6, getText("msg.no.record"));
				sheet.mergeCells(1, length + 6, 8, length + 6);
			}
			int i = 0;
			Iterator<Map.Entry<String, CompleteCollectionBean>> it = dataMap
					.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<String, CompleteCollectionBean> pair = it.next();
				CompleteCollectionBean bean = new CompleteCollectionBean();
				bean = pair.getValue();
				addLabel(sheet, 1, i + length + 8, bean.getOrgName());
				sheet.mergeCells(1, i + length + 8, 3, i + length + 8);
				addNumber(sheet, 4, i + length + 8, bean.getDrawSale()
						- bean.getDrawCancel());
				totalDrawSale += bean.getDrawSale() - bean.getDrawCancel();
				addNumber(sheet, 5, i + length + 8, bean.getDrawPwt()
						+ bean.getDrawDirPlyPwt());
				totalDrawPwt += bean.getDrawPwt() + bean.getDrawDirPlyPwt();
				addNumber(sheet, 6, i + length + 8, bean.getScratchSale());
				totalScratchSale += bean.getScratchSale();
				addNumber(sheet, 7, i + length + 8, bean.getScratchPwt()
						+ bean.getScratchDirPlyPwt());
				totalScratchPwt += bean.getScratchPwt()
						+ bean.getScratchDirPlyPwt();
				addNumber(sheet, 8, i + length + 8, bean.getCSSale()
						- bean.getCSCancel());
				totalCSSale += bean.getCSSale() - bean.getCSCancel();
				bean.setTotalCashInHand(bean.getDrawSale()
						- bean.getDrawCancel() - bean.getDrawPwt()
						- bean.getDrawDirPlyPwt() + bean.getScratchSale()
						- bean.getScratchPwt() - bean.getScratchDirPlyPwt()
						+ bean.getCSSale() - bean.getCSCancel());
				addNumber(sheet, 8, i + length + 8, bean.getTotalCashInHand());
				totalCashInHand += bean.getTotalCashInHand();
				sheet.mergeCells(8, i + length + 8, 9, i + length + 8);
				i++;
			}
			addCaption(sheet, 1, len + length + 8, getText(
					"label.total"));
			sheet.mergeCells(1, len + length + 8, 3, len + length + 8);
			sheet.addCell(new Number(4, len + length + 8, totalDrawSale,
					headingNumberFormat));
			sheet.addCell(new Number(5, len + length + 8, totalDrawPwt,
					headingNumberFormat));
			sheet.addCell(new Number(6, len + length + 8, totalScratchSale,
					headingNumberFormat));
			sheet.addCell(new Number(7, len + length + 8, totalScratchPwt,
					headingNumberFormat));
			sheet.addCell(new Number(8, len + length + 8, totalCSSale,
					headingNumberFormat));
			sheet.addCell(new Number(9, len + length + 8, totalCashInHand,
					headingNumberFormat));
			sheet.mergeCells(9, len + length + 10, 9, len + length + 8);
		}  else if (isDG && isSE && isSLE) {
			sheet.mergeCells(4, length + 5, 7, length + 5);
			addCaption(sheet, 4, length + 6, getText("label.draw.game"));
			sheet.mergeCells(4, length + 6, 5, length + 6);
			addCaption(sheet, 6, length + 6, getText("label.scratch.game"));
			sheet.mergeCells(6, length + 6, 7, length + 6);
			addCaption(sheet, 8, length + 6, getText("label.sport.lot"));
			sheet.mergeCells(8, length + 6, 9, length + 6);

			addCaption(sheet, 10, length + 5, getText("label.net.amt"));
			sheet.mergeCells(10, length + 5, 11, length + 7);

			addCaption(sheet, 4, length + 7, getText("label.sale.amt"));
			addCaption(sheet, 5, length + 7, getText("label.pwt.amt"));
			addCaption(sheet, 6, length + 7, getText("label.sale.amt"));
			addCaption(sheet, 7, length + 7, getText("label.pwt.amt"));
			addCaption(sheet, 8, length + 7, getText("label.sale.amt"));
			addCaption(sheet, 9, length + 7, getText("label.pwt.amt"));

			if (len == 0) {
				addLabel(sheet, 1, length + 6, getText("msg.no.record"));
				sheet.mergeCells(1, length + 6, 10, length + 6);
			}
			int i = 0;
			Iterator<Map.Entry<String, CompleteCollectionBean>> it = dataMap
					.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<String, CompleteCollectionBean> pair = it.next();
				CompleteCollectionBean bean = new CompleteCollectionBean();
				bean = pair.getValue();
				addLabel(sheet, 1, i + length + 8, bean.getOrgName());
				sheet.mergeCells(1, i + length + 8, 3, i + length + 8);
				addNumber(sheet, 4, i + length + 8, bean.getDrawSale()
						- bean.getDrawCancel());
				totalDrawSale += bean.getDrawSale() - bean.getDrawCancel();
				addNumber(sheet, 5, i + length + 8, bean.getDrawPwt()
						+ bean.getDrawDirPlyPwt());
				totalDrawPwt += bean.getDrawPwt() + bean.getDrawDirPlyPwt();
				addNumber(sheet, 6, i + length + 8, bean.getScratchSale());
				totalScratchSale += bean.getScratchSale();
				addNumber(sheet, 7, i + length + 8, bean.getScratchPwt()
						+ bean.getScratchDirPlyPwt());
				totalScratchPwt += bean.getScratchPwt()
						+ bean.getScratchDirPlyPwt();
				addNumber(sheet, 8, i + length + 8, bean.getSleSale());
				totalSleSale += bean.getSleSale();
				addNumber(sheet, 9, i + length + 8, bean.getSlePwt()
						+ bean.getSleDirPlyPwt());
				totalSlePwt += bean.getSlePwt() + bean.getSleDirPlyPwt();
				bean.setTotalCashInHand(bean.getDrawSale()
						- bean.getDrawCancel() - bean.getDrawPwt()
						- bean.getDrawDirPlyPwt() + bean.getScratchSale()
						- bean.getScratchPwt() - bean.getScratchDirPlyPwt()
						+ bean.getSleSale() - bean.getSleCancel()
						- bean.getSlePwt() - bean.getSleDirPlyPwt());
				addNumber(sheet, 10, i + length + 8, bean.getTotalCashInHand());
				totalCashInHand += bean.getTotalCashInHand();
				sheet.mergeCells(10, i + length + 8, 11, i + length + 8);
				i++;
			}
			addCaption(sheet, 1, len + length + 8, getText(
					"label.total"));
			sheet.mergeCells(1, len + length + 8, 3, len + length + 8);
			sheet.addCell(new Number(4, len + length + 8, totalDrawSale,
					headingNumberFormat));
			sheet.addCell(new Number(5, len + length + 8, totalDrawPwt,
					headingNumberFormat));
			sheet.addCell(new Number(6, len + length + 8, totalScratchSale,
					headingNumberFormat));
			sheet.addCell(new Number(7, len + length + 8, totalScratchPwt,
					headingNumberFormat));
			sheet.addCell(new Number(8, len + length + 8, totalSleSale,
					headingNumberFormat));
			sheet.addCell(new Number(9, len + length + 8, totalSlePwt,
					headingNumberFormat));
			sheet.addCell(new Number(10, len + length + 8, totalCashInHand,
					headingNumberFormat));
			sheet.mergeCells(10, len + length + 10, 11, len + length + 8);
		} else if (isDG && isSE && isIW) {
			sheet.mergeCells(4, length + 5, 7, length + 5);
			addCaption(sheet, 4, length + 6, getText("label.draw.game"));
			sheet.mergeCells(4, length + 6, 5, length + 6);
			addCaption(sheet, 6, length + 6, getText("label.scratch.game"));
			sheet.mergeCells(6, length + 6, 7, length + 6);
			addCaption(sheet, 8, length + 6, getText("label.instant.win"));
			sheet.mergeCells(8, length + 6, 9, length + 6);

			addCaption(sheet, 10, length + 5, getText("label.net.amt"));
			sheet.mergeCells(10, length + 5, 11, length + 7);

			addCaption(sheet, 4, length + 7, getText("label.sale.amt"));
			addCaption(sheet, 5, length + 7, getText("label.pwt.amt"));
			addCaption(sheet, 6, length + 7, getText("label.sale.amt"));
			addCaption(sheet, 7, length + 7, getText("label.pwt.amt"));
			addCaption(sheet, 8, length + 7, getText("label.sale.amt"));
			addCaption(sheet, 9, length + 7, getText("label.pwt.amt"));

			if (len == 0) {
				addLabel(sheet, 1, length + 6, getText("msg.no.record"));
				sheet.mergeCells(1, length + 6, 10, length + 6);
			}
			int i = 0;
			Iterator<Map.Entry<String, CompleteCollectionBean>> it = dataMap
					.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<String, CompleteCollectionBean> pair = it.next();
				CompleteCollectionBean bean = new CompleteCollectionBean();
				bean = pair.getValue();
				addLabel(sheet, 1, i + length + 8, bean.getOrgName());
				sheet.mergeCells(1, i + length + 8, 3, i + length + 8);
				addNumber(sheet, 4, i + length + 8, bean.getDrawSale() - bean.getDrawCancel());
				totalDrawSale += bean.getDrawSale() - bean.getDrawCancel();
				addNumber(sheet, 5, i + length + 8, bean.getDrawPwt() + bean.getDrawDirPlyPwt());
				totalDrawPwt += bean.getDrawPwt() + bean.getDrawDirPlyPwt();

				addNumber(sheet, 6, i + length + 8, bean.getScratchSale());
				totalScratchSale += bean.getScratchSale();
				addNumber(sheet, 7, i + length + 8, bean.getScratchPwt() + bean.getScratchDirPlyPwt());
				totalScratchPwt += bean.getScratchPwt() + bean.getScratchDirPlyPwt();

				addNumber(sheet, 8, i + length + 8, bean.getIwSale());
				totalSleSale += bean.getIwSale();
				addNumber(sheet, 9, i + length + 8, bean.getIwPwt() + bean.getIwDirPlyPwt());
				totalSlePwt += bean.getIwPwt() + bean.getIwDirPlyPwt();

				bean.setTotalCashInHand(bean.getDrawSale()
						- bean.getDrawCancel() - bean.getDrawPwt()
						- bean.getDrawDirPlyPwt() + bean.getScratchSale()
						- bean.getScratchPwt() - bean.getScratchDirPlyPwt()
						+ bean.getIwSale() - bean.getIwCancel()
						- bean.getIwPwt() - bean.getIwDirPlyPwt());
				addNumber(sheet, 10, i + length + 8, bean.getTotalCashInHand());
				totalCashInHand += bean.getTotalCashInHand();
				sheet.mergeCells(10, i + length + 8, 11, i + length + 8);
				i++;
			}
			addCaption(sheet, 1, len + length + 8, getText("label.total"));
			sheet.mergeCells(1, len + length + 8, 3, len + length + 8);
			sheet.addCell(new Number(4, len + length + 8, totalDrawSale,
					headingNumberFormat));
			sheet.addCell(new Number(5, len + length + 8, totalDrawPwt,
					headingNumberFormat));
			sheet.addCell(new Number(6, len + length + 8, totalScratchSale,
					headingNumberFormat));
			sheet.addCell(new Number(7, len + length + 8, totalScratchPwt,
					headingNumberFormat));
			sheet.addCell(new Number(8, len + length + 8, totalIwSale,
					headingNumberFormat));
			sheet.addCell(new Number(9, len + length + 8, totalIwPwt,
					headingNumberFormat));
			sheet.addCell(new Number(10, len + length + 8, totalCashInHand,
					headingNumberFormat));
			sheet.mergeCells(10, len + length + 10, 11, len + length + 8);
		} else if (isDG && isSLE) {
			sheet.mergeCells(4, length + 5, 7, length + 5);
			addCaption(sheet, 4, length + 6, getText("label.draw.game"));
			sheet.mergeCells(4, length + 6, 5, length + 6);
			addCaption(sheet, 6, length + 6, getText("label.sport.lot"));
			sheet.mergeCells(6, length + 6, 7, length + 6);

			addCaption(sheet, 8, length + 5, getText("label.net.amt"));
			sheet.mergeCells(8, length + 5, 9, length + 7);

			addCaption(sheet, 4, length + 7, getText("label.sale.amt"));
			addCaption(sheet, 5, length + 7, getText("label.pwt.amt"));
			addCaption(sheet, 6, length + 7, getText("label.sale.amt"));
			addCaption(sheet, 7, length + 7, getText("label.pwt.amt"));

			if (len == 0) {
				addLabel(sheet, 1, length + 6, getText("msg.no.record"));
				sheet.mergeCells(1, length + 6, 7, length + 6);
			}
			int i = 0;
			Iterator<Map.Entry<String, CompleteCollectionBean>> it = dataMap
					.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<String, CompleteCollectionBean> pair = it.next();
				CompleteCollectionBean bean = new CompleteCollectionBean();
				bean = pair.getValue();
				addLabel(sheet, 1, i + length + 8, bean.getOrgName());
				sheet.mergeCells(1, i + length + 8, 3, i + length + 8);
				addNumber(sheet, 4, i + length + 8, bean.getDrawSale()
						- bean.getDrawCancel());
				totalDrawSale += bean.getDrawSale() - bean.getDrawCancel();
				addNumber(sheet, 5, i + length + 8, bean.getDrawPwt()
						+ bean.getDrawDirPlyPwt());
				totalDrawPwt += bean.getDrawPwt() + bean.getDrawDirPlyPwt();
				addNumber(sheet, 6, i + length + 8, bean.getSleSale());
				totalSleSale += bean.getSleSale();
				addNumber(sheet, 7, i + length + 8, bean.getSlePwt()
						+ bean.getSleDirPlyPwt());
				totalSlePwt += bean.getSlePwt() + bean.getSleDirPlyPwt();
				bean.setTotalCashInHand(bean.getDrawSale()
						- bean.getDrawCancel() - bean.getDrawPwt()
						- bean.getDrawDirPlyPwt() + bean.getSleSale()
						- bean.getSleCancel() - bean.getSlePwt()
						- bean.getSleDirPlyPwt());
				addNumber(sheet, 8, i + length + 8, bean.getTotalCashInHand());
				totalCashInHand += bean.getTotalCashInHand();
				sheet.mergeCells(8, i + length + 8, 9, i + length + 8);
				i++;
			}
			addCaption(sheet, 1, len + length + 8, getText(
					"label.total"));
			sheet.mergeCells(1, len + length + 8, 3, len + length + 8);
			sheet.addCell(new Number(4, len + length + 8, totalDrawSale,
					headingNumberFormat));
			sheet.addCell(new Number(5, len + length + 8, totalDrawPwt,
					headingNumberFormat));
			sheet.addCell(new Number(6, len + length + 8, totalSleSale,
					headingNumberFormat));
			sheet.addCell(new Number(7, len + length + 8, totalSlePwt,
					headingNumberFormat));
			sheet.addCell(new Number(8, len + length + 8, totalCashInHand,
					headingNumberFormat));
			sheet.mergeCells(8, len + length + 10, 9, len + length + 8);
		} else if (isDG && isIW) {
			sheet.mergeCells(4, length + 5, 7, length + 5);
			addCaption(sheet, 4, length + 6, getText("label.draw.game"));
			sheet.mergeCells(4, length + 6, 5, length + 6);
			addCaption(sheet, 6, length + 6, getText("label.instant.win"));
			sheet.mergeCells(6, length + 6, 7, length + 6);

			addCaption(sheet, 8, length + 5, getText("label.net.amt"));
			sheet.mergeCells(8, length + 5, 9, length + 7);

			addCaption(sheet, 4, length + 7, getText("label.sale.amt"));
			addCaption(sheet, 5, length + 7, getText("label.pwt.amt"));
			addCaption(sheet, 6, length + 7, getText("label.sale.amt"));
			addCaption(sheet, 7, length + 7, getText("label.pwt.amt"));

			if (len == 0) {
				addLabel(sheet, 1, length + 6, getText("msg.no.record"));
				sheet.mergeCells(1, length + 6, 7, length + 6);
			}
			int i = 0;
			Iterator<Map.Entry<String, CompleteCollectionBean>> it = dataMap
					.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<String, CompleteCollectionBean> pair = it.next();
				CompleteCollectionBean bean = new CompleteCollectionBean();
				bean = pair.getValue();
				addLabel(sheet, 1, i + length + 8, bean.getOrgName());
				sheet.mergeCells(1, i + length + 8, 3, i + length + 8);
				addNumber(sheet, 4, i + length + 8, bean.getDrawSale() - bean.getDrawCancel());
				totalDrawSale += bean.getDrawSale() - bean.getDrawCancel();
				addNumber(sheet, 5, i + length + 8, bean.getDrawPwt() + bean.getDrawDirPlyPwt());
				totalDrawPwt += bean.getDrawPwt() + bean.getDrawDirPlyPwt();
				addNumber(sheet, 6, i + length + 8, bean.getIwSale());
				totalIwSale += bean.getIwSale();
				addNumber(sheet, 7, i + length + 8, bean.getIwPwt() + bean.getIwDirPlyPwt());
				totalIwPwt += bean.getIwPwt() + bean.getIwDirPlyPwt();
				bean.setTotalCashInHand(bean.getDrawSale() - bean.getDrawCancel() - bean.getDrawPwt() - bean.getDrawDirPlyPwt() + bean.getIwSale() - bean.getIwCancel() - bean.getIwPwt() - bean.getIwDirPlyPwt());
				addNumber(sheet, 8, i + length + 8, bean.getTotalCashInHand());
				totalCashInHand += bean.getTotalCashInHand();
				sheet.mergeCells(8, i + length + 8, 9, i + length + 8);
				i++;
			}
			addCaption(sheet, 1, len + length + 8, getText("label.total"));
			sheet.mergeCells(1, len + length + 8, 3, len + length + 8);
			sheet.addCell(new Number(4, len + length + 8, totalDrawSale, headingNumberFormat));
			sheet.addCell(new Number(5, len + length + 8, totalDrawPwt, headingNumberFormat));
			sheet.addCell(new Number(6, len + length + 8, totalIwSale, headingNumberFormat));
			sheet.addCell(new Number(7, len + length + 8, totalIwPwt, headingNumberFormat));
			sheet.addCell(new Number(8, len + length + 8, totalCashInHand, headingNumberFormat));
			sheet.mergeCells(8, len + length + 10, 9, len + length + 8);
		} else {
			if (isDG) {
				sheet.mergeCells(4, length + 5, 5, length + 5);
				addCaption(sheet, 4, length + 6, getText(
						"label.draw.game"));
			}
			if (isSE) {
				sheet.mergeCells(4, length + 5, 5, length + 5);
				addCaption(sheet, 4, length + 6, getText(
						"label.scratch.game"));
			}
			if (isOLA) {
				sheet.mergeCells(4, length + 5, 6, length + 5);
				addCaption(sheet, 4, length + 6, getText(
						"label.offline.aff"));
			}
			/*
			 * if (isSLE) { sheet.mergeCells(4, length+5, 5, length+5);
			 * addCaption(sheet, 4, length+6, getText("label.sport.lot",
			 * locale)); }
			 */

			int i = 0;
			Iterator<Map.Entry<String, CompleteCollectionBean>> it = dataMap
					.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<String, CompleteCollectionBean> pair = it.next();
				CompleteCollectionBean bean = new CompleteCollectionBean();
				bean = pair.getValue();
				addLabel(sheet, 1, i + length + 8, bean.getOrgName());
				sheet.mergeCells(1, i + length + 8, 3, i + length + 8);
				if (isDG) {
					sheet.mergeCells(4, length + 6, 5, length + 6);
					addCaption(sheet, 6, length + 5, getText(
							"label.net.amt"));
					sheet.mergeCells(6, length + 5, 7, length + 7);

					addCaption(sheet, 4, length + 7, getText(
							"label.sale.amt"));
					addCaption(sheet, 5, length + 7, getText(
							"label.pwt.amt"));
					addNumber(sheet, 4, i + length + 8, bean.getDrawSale()
							- bean.getDrawCancel());
					totalDrawSale += bean.getDrawSale() - bean.getDrawCancel();
					addNumber(sheet, 5, i + length + 8, bean.getDrawPwt()
							+ bean.getDrawDirPlyPwt());
					totalDrawPwt += bean.getDrawPwt() + bean.getDrawDirPlyPwt();
					bean.setTotalCashInHand(bean.getDrawSale()
							- bean.getDrawCancel() - bean.getDrawPwt()
							- bean.getDrawDirPlyPwt());
					addNumber(sheet, 6, i + length + 8, bean
							.getTotalCashInHand());
					sheet.mergeCells(6, i + length + 8, 7, i + length + 8);
					addCaption(sheet, 1, len + length + 8, getText(
							"label.total"));
					sheet.mergeCells(1, len + length + 8, 3, len + length + 8);
				}
				if (isSE) {
					sheet.mergeCells(4, length + 6, 5, length + 6);
					addCaption(sheet, 6, length + 5, getText(
							"label.net.amt"));
					sheet.mergeCells(6, length + 5, 7, length + 7);

					addCaption(sheet, 4, length + 7, getText(
							"label.sale.amt"));
					addCaption(sheet, 5, length + 7, getText(
							"label.pwt.amt"));
					addNumber(sheet, 4, i + length + 8, bean.getScratchSale());
					totalScratchSale += bean.getScratchSale();
					addNumber(sheet, 5, i + length + 8, bean.getScratchPwt()
							+ bean.getScratchDirPlyPwt());
					bean
							.setTotalCashInHand(bean.getScratchSale()
									- bean.getScratchPwt()
									- bean.getScratchDirPlyPwt());
					addNumber(sheet, 6, i + length + 8, bean
							.getTotalCashInHand());
					sheet.mergeCells(6, i + length + 8, 7, i + length + 8);
					addCaption(sheet, 1, len + length + 8, getText(
							"label.total"));
					sheet.mergeCells(1, len + length + 8, 3, len + length + 8);
				}
				if (isOLA) {
					sheet.mergeCells(4, length + 6, 5, length + 6);
					addCaption(sheet, 7, length + 5, getText(
							"label.net.amt"));
					sheet.mergeCells(7, length + 5, 8, length + 7);

					addCaption(sheet, 4, length + 7, getText(
							"label.deposit"));
					addCaption(sheet, 5, length + 7, getText(
							"label.wdl.amt"));
					addCaption(sheet, 6, length + 7, getText(
							"label.net.gaming.amt"));
					addNumber(sheet, 4, i + length + 8, bean.getOlaDepositAmt()
							- bean.getOlaDepositCancelAmt());
					totalOLADeposit += bean.getOlaDepositAmt()
							- bean.getOlaDepositCancelAmt();
					addNumber(sheet, 5, i + length + 8, bean
							.getOlaWithdrawalAmt());
					totalOLAWithdraw += bean.getOlaWithdrawalAmt();
					addNumber(sheet, 6, i + length + 8, bean.getOlaNetGaming());
					totalOLANetGaming += bean.getOlaNetGaming();
					bean.setTotalCashInHand(bean.getOlaDepositAmt()
							- bean.getOlaDepositCancelAmt()
							- bean.getOlaWithdrawalAmt()
							- bean.getOlaNetGaming());
					addNumber(sheet, 7, i + length + 8, bean
							.getTotalCashInHand());
					sheet.mergeCells(7, i + length + 8, 8, i + length + 8);
					addCaption(sheet, 1, len + length + 8, getText(
							"label.total"));
					sheet.mergeCells(1, len + length + 8, 3, len + length + 8);

				}
				/*
				 * if (isSLE) { sheet.mergeCells(4, length+6, 5, length+6);
				 * addCaption(sheet, 6, length+5,
				 * getText("label.net.amt"));
				 * sheet.mergeCells(6, length+5, 7, length+7);
				 * 
				 * addCaption(sheet, 4, length+7,
				 * getText("label.sale.amt")); addCaption(sheet,
				 * 5, length+7, getText("label.pwt.amt"));
				 * addNumber(sheet, 4, i + length+8, bean.getSleSale() -
				 * bean.getSleCancel()); totalSleSale += bean.getSleSale() -
				 * bean.getSleCancel(); addNumber(sheet, 5, i + length+8,
				 * bean.getSlePwt() + bean.getSleDirPlyPwt()); totalSlePwt +=
				 * bean.getSlePwt() + bean.getSleDirPlyPwt();
				 * bean.setTotalCashInHand(bean.getSleSale() -
				 * bean.getSleCancel() - bean.getSlePwt() -
				 * bean.getSleDirPlyPwt()); addNumber(sheet, 6, i +length+ 8,
				 * bean.getTotalCashInHand()); sheet.mergeCells(6, i + length+8,
				 * 7, i + length+8); addCaption(sheet, 1, len+length + 8,
				 * getText("label.total")); sheet.mergeCells(1,
				 * len+length + 8, 3, len+length + 8); }
				 */

				if (isDG) {
					sheet.addCell(new Number(4, len + length + 8,
							totalDrawSale, headingNumberFormat));
					sheet.addCell(new Number(5, len + length + 8, totalDrawPwt,
							headingNumberFormat));
					sheet.addCell(new Number(6, len + length + 8,
							totalCashInHand, headingNumberFormat));
					sheet.mergeCells(6, len + length + 8, 7, len + length + 8);
				}
				if (isSE) {
					sheet.addCell(new Number(4, len + length + 8,
							totalScratchSale, headingNumberFormat));
					sheet.addCell(new Number(5, len + length + 8,
							totalScratchPwt, headingNumberFormat));
					sheet.addCell(new Number(6, len + length + 8,
							totalCashInHand, headingNumberFormat));
					sheet.mergeCells(6, len + length + 8, 7, len + length + 8);
				}
				if (isOLA) {
					sheet.addCell(new Number(4, len + length + 8,
							totalOLADeposit, headingNumberFormat));
					sheet.addCell(new Number(5, len + length + 8,
							totalOLAWithdraw, headingNumberFormat));
					sheet.addCell(new Number(6, len + length + 8,
							totalOLANetGaming, headingNumberFormat));
					sheet.addCell(new Number(7, len + length + 8,
							totalCashInHand, headingNumberFormat));
					sheet.mergeCells(7, len + length + 8, 8, len + length + 8);
				}
				/*
				 * if (isSLE) { sheet.addCell(new Number(4, len+length + 8,
				 * totalSleSale, headingNumberFormat)); sheet.addCell(new
				 * Number(5, len+length + 8, totalSlePwt, headingNumberFormat));
				 * sheet.addCell(new Number(6, len+length + 8, totalCashInHand,
				 * headingNumberFormat)); sheet.mergeCells(6, len+length + 8, 7,
				 * len+length + 8); }
				 */

				i++;
			}
		}
		length = len + 10;
		return length;

	}

	private void createHeaderForComplCollReport(WritableSheet sheet)
			throws WriteException, ParseException {
		DateFormat format = new SimpleDateFormat("dd-MMM-yy");

		sheet.addCell(new Label(10, 0, getText("label.date")
				+ "  :  " + format.format(new Date()), dateFormat));
		sheet.mergeCells(10, 0, 11, 0);

		sheet.mergeCells(4, 3, 8, 3);
		sheet.mergeCells(9, 3, 11, 3);
		setHeadingForComplCollReport(sheet);

	}

	// -------------------------- Agent sale & Pwt detail method

	private void setHeadingForComplCollReport(WritableSheet sheet)
			throws RowsExceededException, WriteException {
		DateFormat format = new SimpleDateFormat("dd-MMM-yy");
		if (this.endDate == null) {

			logger.debug(" inside date if condition -kgkgjdkjkjkfj---- ----user Type : "
							+ this.reportType + " date : " + startDate);
			sheet.addCell(new Label(4, 3, getText("label.from.date")
					+ " " + getText("label.of") + reportType,
					headerFormat));
			if (reportday == null) {
				sheet.addCell(new Label(9, 3, " ( "
						+ new SimpleDateFormat("MMM-yyyy").format(startDate)
						+ " ) ", headerDateFormat));
			} else {
				sheet.addCell(new Label(9, 3, " ( "
						+ new SimpleDateFormat("dd-MMM-yyyy").format(reportday)
						+ " ) ", headerDateFormat));
			}
		} else {
			logger.debug(" inside date else condition ----- ----user Type : "
					+ reportType + " date : " + startDate + " end date : "
					+ endDate);
			sheet.addCell(new Label(4, 3, getText(
					"label.sale.pwt.colln.rep")
					+ " " + getText("label.from.date") + "",
					headerFormat));
			sheet.addCell(new Label(9, 3, " ( " + format.format(startDate)
					+ "   -   " + format.format(endDate) + " ) ",
					headerDateFormat));
		}
	}

	public void write(Map<String, CompleteCollectionBean> reportbean,
			WritableWorkbook workbk, String orgName, String orgAdd,
			boolean isSE, boolean isDG, boolean isCS, boolean isOLA,
			boolean isSLE, boolean isIW) throws IOException, WriteException, ParseException {
		WorkbookSettings wbSettings = new WorkbookSettings();
		wbSettings.setLocale(new Locale("en", "EN"));
		// WritableWorkbook workbook = Workbook.createWorkbook(file,
		// wbSettings);
		WritableWorkbook workbook = workbk;
		workbook.createSheet(getText("Report"), 0);
		WritableSheet excelSheet = workbook.getSheet(0);

		createContent(excelSheet, reportbean, orgName, orgAdd, isSE, isDG,
				isCS, isOLA, isSLE, isIW);

		workbook.write();
		workbook.close();
	}

	/**
	 * It create excel sheet for agent that contain retailer wise detail of sale
	 * & pwt transactions.
	 */
	public void writeAgent(ArrayList<CashChqReportBean> data,
			CashChqReportBean agentBoDetail, WritableWorkbook workbk)
			throws WriteException, IOException {
		WorkbookSettings wbSettings = new WorkbookSettings();
		wbSettings.setLocale(new Locale("en", "EN"));
		// WritableWorkbook workbook = Workbook.createWorkbook(file,
		// wbSettings);
		WritableWorkbook workbook = workbk;
		workbook.createSheet(getText("Report"), 0);
		WritableSheet excelSheet = workbook.getSheet(0);
		try {
			createContentOfAgentSheet(excelSheet, data, agentBoDetail);
		} catch (RowsExceededException e) {
			e.printStackTrace();
		} catch (WriteException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		workbook.write();
		workbook.close();

	}

	public void writeFullDetailDateWise(
			Map<String, CompleteCollectionBean> reportMap,
			Map<Integer, Map<String, CompleteCollectionBean>> detailMapList,
			WritableWorkbook workbk, String orgName, String orgAdd,
			Map<Integer, String> orgAddMap, Map<Integer, Double> drawDPPMap,
			Map<Integer, Double> scratchDPPMap, Map<Integer, Double> sleDPPMap, 
			Map<Integer, Double> iwDPPMap,
			boolean isSE, boolean isDG, boolean isCS, boolean isOLA,
			boolean isSLE, boolean isIW) throws IOException, WriteException, ParseException {
		WorkbookSettings wbSettings = new WorkbookSettings();
		wbSettings.setLocale(new Locale("en", "EN"));
		WritableWorkbook workbook = workbk;
		workbook.createSheet(getText("Report"), 0);
		WritableSheet excelSheet = workbook.getSheet(0);
		createContent(excelSheet, reportMap, orgName, orgAdd, isSE, isDG, isCS,
				isOLA, isSLE, isIW);
		Iterator<Map.Entry<Integer, Map<String, CompleteCollectionBean>>> it = detailMapList
				.entrySet().iterator();
		int i = 1;
		while (it.hasNext()) {
			Map.Entry<Integer, Map<String, CompleteCollectionBean>> pair = it
					.next();
			workbook.createSheet(
					reportMap.get(pair.getKey() + "").getOrgName(), i);
			excelSheet = workbook.getSheet(i);
			createDetailContentForAgentWise(excelSheet, reportMap.get(pair
					.getKey()
					+ ""), pair.getValue(), reportMap.get(pair.getKey() + "")
					.getOrgName(), orgAddMap.get(pair.getKey()), drawDPPMap
					.get(pair.getKey()), scratchDPPMap.get(pair.getKey()),
					sleDPPMap.get(pair.getKey()), iwDPPMap.get(pair.getKey()), isSE, isDG, isCS, isOLA,
					isSLE, isIW);
			i++;
		}
		workbook.write();
		workbook.close();
	}

	public void WriteFullDetailRetailerWise(
			Map<String, CompleteCollectionBean> reportMap,
			WritableWorkbook workbk, String orgName, String orgAdd,
			Double drawDPPMap, Double scratchDPPMap, Double sleDPPMap,
			boolean isSE, boolean isDG, boolean isCS, boolean isOLA,
			boolean isSLE, boolean isIW) throws IOException, WriteException, ParseException {
		WorkbookSettings wbSettings = new WorkbookSettings();
		wbSettings.setLocale(new Locale("en", "EN"));
		WritableWorkbook workbook = workbk;
		workbook.createSheet(getText("Report"), 0);
		WritableSheet excelSheet = workbook.getSheet(0);
		createContent(excelSheet, reportMap, orgName, orgAdd, isSE, isDG, isCS,
				isOLA, isSLE, isIW);
		workbook.write();
		workbook.close();
	}

	public void writeFullDetailDayWise(
			Map<String, CompleteCollectionBean> mainSummaryData,
			Map<String, Map<String, CompleteCollectionBean>> detailDataMap,
			WritableWorkbook workbk, String orgName, String orgAdd,
			boolean isSE, boolean isDG, boolean isCS, boolean isOLA,
			boolean isSLE, boolean isIW) throws IOException, WriteException, ParseException {
		WorkbookSettings wbSettings = new WorkbookSettings();
		wbSettings.setLocale(new Locale("en", "EN"));
		WritableWorkbook workbook = workbk;
		workbook.createSheet(getText("Report"), 0);
		WritableSheet excelSheet = workbook.getSheet(0);
		createContent(excelSheet, mainSummaryData, orgName, orgAdd, isSE, isDG,
				isCS, isOLA, isSLE, isIW);

		int i = 1;
		int length = mainReportLength;
		Iterator<Map.Entry<String, Map<String, CompleteCollectionBean>>> it = detailDataMap
				.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, Map<String, CompleteCollectionBean>> pair = it
					.next();
			System.out.println("dataMap size  is  " + detailDataMap.size());
			length += createDetailContentForDayWise(excelSheet,
					pair.getValue(), pair.getKey(), "", isSE, isDG, isCS,
					isOLA, isSLE, isIW, length);
			i++;
		}
		/*
		 * this code is for creating separate sheets for each agent in case of
		 * day wise filter Iterator<Map.Entry<String, List<CashChqReportBean>>>
		 * it = dataMap.entrySet().iterator(); int i=1; while(it.hasNext()){
		 * Map.Entry<String, List<CashChqReportBean>> pair = it.next();
		 * workbook.createSheet(mainSummaryData.get(i).getName(), i); excelSheet
		 * = workbook.getSheet(i); createDetailContentForDayWise(excelSheet,
		 * pair.getValue()); i++; }
		 */
		workbook.write();
		workbook.close();
	}

}
