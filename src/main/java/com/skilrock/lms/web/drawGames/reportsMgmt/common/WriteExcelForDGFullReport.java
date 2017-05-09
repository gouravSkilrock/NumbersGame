package com.skilrock.lms.web.drawGames.reportsMgmt.common;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
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
import com.skilrock.lms.web.reportsMgmt.common.ReportUtility;

public class WriteExcelForDGFullReport extends LocalizedTextUtil {
	private WritableCellFormat dateFormat;

	private WritableCellFormat headerDateFormat;
	private WritableCellFormat headerFormat;
	private WritableCellFormat headingLabel;
	private WritableCellFormat headingNumberFormat;
	Log logger = LogFactory.getLog(WriteExcelForDGFullReport.class);
	private WritableCellFormat numberFormat;
	private String reportType;
	private java.util.Date startDate, endDate, reportday;
	private WritableCellFormat times;
	private WritableCellFormat timesBoldUnderline;
	private Locale locale=Locale.getDefault();
	public WriteExcelForDGFullReport(DateBeans dateBeans) throws WriteException {

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

	private void createContent(WritableSheet sheet, Map<String, String> repMap,
			String orgname, String orgAdd, Map<Integer, String> gameNumMap, Map<Integer, String> IWGameMap)
			throws WriteException, RowsExceededException, ParseException {
		
		boolean isIW = ReportUtility.isIW;

		sheet.addCell(new Label(2, 1, orgname, headerFormat));
		sheet.mergeCells(2, 1, 10, 1);

		sheet.addCell(new Label(2, 2, orgAdd, headerFormat));
		sheet.mergeCells(2, 2, 10, 2);

		createHeaderForDGFullReport(sheet);
		addCaption(sheet, 1, 4, ((Map) ServletActionContext.getServletContext()
				.getAttribute("TIER_MAP")).get("AGENT")
				+ " "+findDefaultText("label.name", locale), 13);
		sheet.mergeCells(1, 4, 3, 6);
		
		
		addCaption(sheet, 4, 4, findDefaultText("label.location", locale), 11);
		sheet.mergeCells(4, 4, 6, 6);
		
		addCaption(sheet, 7, 4, findDefaultText("label.sales", locale), 18);
		sheet.mergeCells(7, 4, 6 + (isIW ? (gameNumMap.size()+IWGameMap.size()) : gameNumMap.size()), 4);
		
		addCaption(sheet, 7, 5, "DG", 11);
		sheet.mergeCells(7, 5, 12, 5);
		
		if(isIW){
		addCaption(sheet, 6+((gameNumMap.size()+IWGameMap.size())), 5, "IW", 11);
		sheet.mergeCells(6+((gameNumMap.size()+IWGameMap.size())), 5, 6+((gameNumMap.size()+IWGameMap.size())), 5);
		}
		
		
		
		
		
		
		addCaption(sheet, 7 + ((!isIW) ? gameNumMap.size() : (gameNumMap.size()+IWGameMap.size())), 4, findDefaultText("label.prize.payout", locale), 18);
		sheet
				.mergeCells(7 + ((!isIW) ? gameNumMap.size() : (gameNumMap.size()+IWGameMap.size())), 4,
						6 + ((!isIW) ? gameNumMap.size() : (gameNumMap.size()+IWGameMap.size())) * 2, 4);
		
		addCaption(sheet, 7+ ((!isIW) ? gameNumMap.size() : (gameNumMap.size()+IWGameMap.size())), 5, "DG", 11);
		sheet.mergeCells(7+ ((!isIW) ? gameNumMap.size() : (gameNumMap.size()+IWGameMap.size())), 5, 12+ ((!isIW) ? gameNumMap.size() : (gameNumMap.size()+IWGameMap.size())), 5);
		
		if(isIW){
		addCaption(sheet, 6+((gameNumMap.size()+IWGameMap.size())*2), 5, "IW", 11);
		sheet.mergeCells(6+((gameNumMap.size()+IWGameMap.size())*2), 5, 6+((gameNumMap.size()+IWGameMap.size())*2), 5);
		}
		
		addCaption(sheet, 7 + (((!isIW) ? gameNumMap.size() : (gameNumMap.size()+IWGameMap.size())) * 2), 4, findDefaultText("label.net.portion", locale), 18);
		sheet.mergeCells(7 + (((!isIW) ? gameNumMap.size() : (gameNumMap.size()+IWGameMap.size())) * 2), 4,
				6 + ((!isIW) ? gameNumMap.size() : (gameNumMap.size()+IWGameMap.size())) * 3, 4);
		
		addCaption(sheet, 7+ (((!isIW) ? gameNumMap.size() : (gameNumMap.size()+IWGameMap.size()))*2), 5, "DG", 11);
		sheet.mergeCells(7+ ((!isIW) ? gameNumMap.size() : (gameNumMap.size()+IWGameMap.size()))*2, 5, 12+ ((!isIW) ? gameNumMap.size() : (gameNumMap.size()+IWGameMap.size()))*2, 5);
		
		if(isIW){
		addCaption(sheet, 6+((gameNumMap.size()+IWGameMap.size())*3), 5, "IW", 11);
		sheet.mergeCells(6+((gameNumMap.size()+IWGameMap.size())*3), 5, 6+((gameNumMap.size()+IWGameMap.size())*3), 5);
		}
		
		
		int clmn = 7;
		Iterator<Map.Entry<Integer, String>> it = gameNumMap.entrySet()
				.iterator();
		Iterator<Map.Entry<Integer, String>> iwIterator = null;
		
		while (it.hasNext()) {
			Map.Entry<Integer, String> pair = it.next();
			addCaption(sheet, clmn++, 6, pair.getValue(), 10);
		}
		if(isIW){
		iwIterator = IWGameMap.entrySet().iterator();
		while (iwIterator.hasNext()) {
			Map.Entry<Integer, String> pair = iwIterator.next();
			addCaption(sheet, clmn++, 6, pair.getValue(), 10);
		}}
		it = gameNumMap.entrySet().iterator();
		
		while (it.hasNext()) {
			Map.Entry<Integer, String> pair = it.next();
			addCaption(sheet, clmn++, 6, pair.getValue(), 10);
		}
		if(isIW){
		iwIterator = IWGameMap.entrySet().iterator();
		while (iwIterator.hasNext()) {
			Map.Entry<Integer, String> pair = iwIterator.next();
			addCaption(sheet, clmn++, 6, pair.getValue(), 10);
		}}
		it = gameNumMap.entrySet().iterator();
		
		while (it.hasNext()) {
			Map.Entry<Integer, String> pair = it.next();
			addCaption(sheet, clmn++, 6, pair.getValue(), 10);
		}
		
		if(isIW){
		iwIterator = IWGameMap.entrySet().iterator();
		while (iwIterator.hasNext()) {
			Map.Entry<Integer, String> pair = iwIterator.next();
			addCaption(sheet, clmn++, 6, pair.getValue(), 10);
		}
		}
		int length = repMap.size();

		HashMap saleList = null;
		HashMap pwtList = null;
		HashMap directPlrList = null;
		HashMap iwSales = null ;
		HashMap iwPwt = null ;
		HashMap iwDirPlrPwt = null ;
		List dataList = new ArrayList();
		List nameList = new ArrayList();
		double totalFooter[] = new double[(gameNumMap.size() + (!isIW ? 0 : IWGameMap.size()) ) * 3];
		int c = 0;
		while (c < (gameNumMap.size() + (!isIW ? 0 : IWGameMap.size()) ) * 3) {
			totalFooter[c] = 0.0;
			c++;
		}
		System.out.println("ReportMap    " + repMap);
		Iterator iter = repMap.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry pair = (Map.Entry) iter.next();
			dataList.add(pair.getValue());
			nameList.add(pair.getKey());
		}
		
		
		
		Collections.sort(nameList, new Comparator<String>() {

			@Override
			public int compare(String s1, String s2) {

				return (s1.toUpperCase()).compareTo(s2.toUpperCase());
			}
		});
		//java.util.Collections.sort(nameList);
		for (int i = 0; i < length; i++) {
			addLabel(sheet, 1, i + 6, nameList.get(i));
			sheet.mergeCells(1, i + 6, 3, i + 6);
			List tempList = (ArrayList) dataList.get(i);
			addLabel(sheet, 4, i + 6, isIW ? tempList.get(6) : tempList.get(3));
			sheet.mergeCells(4, i + 6, 6, i + 6);

			saleList = (HashMap) tempList.get(0);
			pwtList = (HashMap) tempList.get(1);
			directPlrList = (HashMap) tempList.get(2);
			if(ReportUtility.isIW){
			iwSales = (HashMap) tempList.get(3);
			iwPwt = (HashMap) tempList.get(4);
			iwDirPlrPwt = (HashMap) tempList.get(5);
			}
			clmn = 7;
			int j = 0;
			Iterator<Map.Entry<Integer, String>> it2 = gameNumMap.entrySet()
					.iterator();
			
			while (it2.hasNext()) {
				Map.Entry<Integer, String> pair = it2.next();
				addNumber(sheet, clmn++, i + 7, Double
						.parseDouble((null == saleList.get(pair.getKey()) ? 0
								: saleList.get(pair.getKey())).toString()));
				totalFooter[j++] += Double.parseDouble(null == saleList
						.get(pair.getKey()) ? "0" : saleList.get(pair.getKey())
						.toString());
			}
			Iterator<Map.Entry<Integer, String>> iwitIterator2 = null ; 
			if(isIW){
			iwitIterator2 = IWGameMap.entrySet()
			.iterator();
			while (iwitIterator2.hasNext()) {
				Map.Entry<Integer, String> pair = iwitIterator2.next();
				addNumber(sheet, clmn++, i + 7, Double
						.parseDouble((null == iwSales.get(pair.getKey()) ? 0
								: iwSales.get(pair.getKey())).toString()));
				totalFooter[j++] += Double.parseDouble(null == iwSales
						.get(pair.getKey()) ? "0" : iwSales.get(pair.getKey())
						.toString());
			}
			}
			it2 = gameNumMap.entrySet().iterator();
			
			while (it2.hasNext()) {
				Map.Entry<Integer, String> pair = it2.next();
				addNumber(
						sheet,
						clmn++,
						i + 7,
						Double
								.parseDouble((pwtList.get(pair.getKey()) == null ? 0
										: pwtList.get(pair.getKey()))
										.toString())
								+ Double.parseDouble((directPlrList.get(pair
										.getKey()) == null ? 0 : directPlrList
										.get(pair.getKey())).toString()));
				totalFooter[j++] += Double.parseDouble((pwtList.get(pair
						.getKey()) == null ? 0 : pwtList.get(pair.getKey()))
						.toString())
						+ Double
								.parseDouble((directPlrList.get(pair.getKey()) == null ? 0
										: directPlrList.get(pair.getKey()))
										.toString());
			}
			if(isIW){
			iwitIterator2 = IWGameMap.entrySet().iterator();
			while (iwitIterator2.hasNext()) {
				Map.Entry<Integer, String> pair = iwitIterator2.next();
				addNumber(
						sheet,
						clmn++,
						i + 7,
						Double
								.parseDouble((iwPwt.get(pair.getKey()) == null ? 0
										: iwPwt.get(pair.getKey()))
										.toString())+ Double.parseDouble((iwDirPlrPwt.get(pair
												.getKey()) == null ? 0 : iwDirPlrPwt
														.get(pair.getKey())).toString()));
								
				totalFooter[j++] += Double.parseDouble((iwPwt.get(pair
						.getKey()) == null ? 0 : iwPwt.get(pair.getKey()))
						.toString())+ Double
						.parseDouble((iwDirPlrPwt.get(pair.getKey()) == null ? 0
								: iwDirPlrPwt.get(pair.getKey()))
								.toString());
			}
			}
			it2 = gameNumMap.entrySet().iterator();
			
			while (it2.hasNext()) {
				Map.Entry<Integer, String> pair = it2.next();
				addNumber(
						sheet,
						clmn++,
						i + 7,
						Double
								.parseDouble((saleList.get(pair.getKey()) == null ? 0
										: saleList.get(pair.getKey()))
										.toString())
								- Double.parseDouble((pwtList
										.get(pair.getKey()) == null ? 0
										: pwtList.get(pair.getKey()))
										.toString())
								- Double.parseDouble((directPlrList.get(pair
										.getKey()) == null ? 0 : directPlrList
										.get(pair.getKey())).toString()));
				totalFooter[j++] += Double.parseDouble((saleList.get(pair
						.getKey()) == null ? 0 : saleList.get(pair.getKey()))
						.toString())
						- Double
								.parseDouble((pwtList.get(pair.getKey()) == null ? 0
										: pwtList.get(pair.getKey()))
										.toString())
						- Double
								.parseDouble((directPlrList.get(pair.getKey()) == null ? 0
										: directPlrList.get(pair.getKey()))
										.toString());
			}
			if(isIW){
			iwitIterator2 = IWGameMap.entrySet().iterator();
			while (iwitIterator2.hasNext()) {
				Map.Entry<Integer, String> pair = iwitIterator2.next();
				addNumber(
						sheet,
						clmn++,
						i + 7,
						Double
								.parseDouble((iwSales.get(pair.getKey()) == null ? 0
										: iwSales.get(pair.getKey()))
										.toString())
								- Double.parseDouble((iwPwt
										.get(pair.getKey()) == null ? 0
										: iwPwt.get(pair.getKey()))
										.toString())- Double.parseDouble((iwDirPlrPwt.get(pair
												.getKey()) == null ? 0 : iwDirPlrPwt
														.get(pair.getKey())).toString()));
				totalFooter[j++] += Double.parseDouble((iwSales.get(pair
						.getKey()) == null ? 0 : iwSales.get(pair.getKey()))
						.toString())
						- Double
								.parseDouble((iwPwt.get(pair.getKey()) == null ? 0
										: iwPwt.get(pair.getKey()))
										.toString())- Double
										.parseDouble((iwDirPlrPwt.get(pair.getKey()) == null ? 0
												: iwDirPlrPwt.get(pair.getKey()))
												.toString());
			}
			}
		}
		sheet.addCell(new Label(6, length + 6, findDefaultText("label.total", locale), headingLabel));
		sheet.mergeCells(1, length + 6, 1, length + 6);
		clmn = 7;
		for (double d : totalFooter) {
			sheet
					.addCell(new Number(clmn++, length + 6, d,
							headingNumberFormat));
		}
	}

	/*
	 * private void createContentForAgent(WritableSheet sheet, List<CollectionReportBean>
	 * reportlist, String orgname, String orgAdd) throws WriteException,
	 * RowsExceededException, ParseException { sheet.addCell(new Label(2, 1,
	 * orgname, headerFormat)); sheet.mergeCells(2, 1, 10, 1);
	 * 
	 * sheet.addCell(new Label(2, 2, orgAdd, headerFormat)); sheet.mergeCells(2,
	 * 2, 10, 2);
	 * 
	 * createHeaderForDGFullReport(sheet);
	 * 
	 * 
	 * addCaption(sheet,3,4,"ACCOUNTS DETAILS",18); sheet.mergeCells(3, 4, 7,
	 * 4); if(reportlist.get(0).getIsDraw() &&
	 * reportlist.get(0).getIsScratch()){ addCaption(sheet,8,4,"DRAW
	 * DETAILS",13); sheet.mergeCells(8, 4, 11, 4); addCaption(sheet, 12, 4,
	 * "SCRATCH DETAILS", 11); sheet.mergeCells(12, 4, 15, 4); }
	 * 
	 * 
	 * if (reportlist.get(0).getIsDraw() && reportlist.get(0).getIsScratch()) {
	 * addCaption(sheet, 3, 4, "DRAW DETAILS", 13); sheet.mergeCells(3, 4, 6,
	 * 4); addCaption(sheet, 7, 4, "SCRATCH DETAILS", 11); sheet.mergeCells(7,
	 * 4, 10, 4); addCaption(sheet, 11, 4, "ACCOUNTS DETAILS", 18);
	 * sheet.mergeCells(11, 4, 15, 4); }
	 * 
	 * if (reportlist.get(0).getIsDraw() && !(reportlist.get(0).getIsScratch())) {
	 * addCaption(sheet, 3, 4, "DRAW DETAILS", 10); sheet.mergeCells(3, 4, 6,
	 * 4); addCaption(sheet, 7, 4, "ACCOUNTS DETAILS", 18); sheet.mergeCells(7,
	 * 4, 11, 4); }
	 * 
	 * if (reportlist.get(0).getIsScratch() && !(reportlist.get(0).getIsDraw())) {
	 * addCaption(sheet, 3, 4, "SCRATCH DETAILS", 11); sheet.mergeCells(3, 4, 6,
	 * 4); addCaption(sheet, 7, 4, "ACCOUNTS DETAILS", 18); sheet.mergeCells(7,
	 * 4, 11, 4); }
	 * 
	 * addCaption(sheet, 0, 5, "SNo", 5); addCaption(sheet, 1, 5, "Retailer
	 * Name", 35); addCaption(sheet, 2, 5, "OPENING BAL", 14);
	 * 
	 * if (reportlist.get(0).getIsDraw() && reportlist.get(0).getIsScratch()) {
	 * addCaption(sheet, 3, 5, "SALE", 12); addCaption(sheet, 4, 5, "SALE
	 * REFUND", 12); addCaption(sheet, 5, 5, "PWT", 12); addCaption(sheet, 6, 5,
	 * "TOTAL", 12); addCaption(sheet, 7, 5, "SALE", 12); addCaption(sheet, 8,
	 * 5, "SALE RET", 12); addCaption(sheet, 9, 5, "PWT", 12); addCaption(sheet,
	 * 10, 5, "TOTAL", 12); addCaption(sheet, 11, 5, "CASH", 12);
	 * addCaption(sheet, 12, 5, "CHQ", 12); addCaption(sheet, 13, 5, "CHQ RET",
	 * 12); addCaption(sheet, 14, 5, "DEBIT", 12); addCaption(sheet, 15, 5,
	 * "TOTAL", 12); addCaption(sheet, 16, 5, "GRAND TOTAL", 14); } if
	 * (reportlist.get(0).getIsDraw() && !(reportlist.get(0).getIsScratch())) {
	 * addCaption(sheet, 3, 5, "SALE", 12); addCaption(sheet, 4, 5, "SALE
	 * REFUND", 12); addCaption(sheet, 5, 5, "PWT", 12); addCaption(sheet, 6, 5,
	 * "TOTAL", 12); addCaption(sheet, 7, 5, "CASH", 12); addCaption(sheet, 8,
	 * 5, "CHQ", 12); addCaption(sheet, 9, 5, "CHQ RET", 12); addCaption(sheet,
	 * 10, 5, "DEBIT", 12); addCaption(sheet, 11, 5, "TOTAL", 12);
	 * addCaption(sheet, 12, 5, "GRAND TOTAL", 14); } if
	 * (reportlist.get(0).getIsScratch() && !(reportlist.get(0).getIsDraw())) {
	 * addCaption(sheet, 3, 5, "SALE", 12); addCaption(sheet, 4, 5, "SALE
	 * REFUND", 12); addCaption(sheet, 5, 5, "PWT", 12); addCaption(sheet, 6, 5,
	 * "TOTAL", 12); addCaption(sheet, 7, 5, "CASH", 12); addCaption(sheet, 8,
	 * 5, "CHQ", 12); addCaption(sheet, 9, 5, "CHQ RET", 12); addCaption(sheet,
	 * 10, 5, "DEBIT", 12); addCaption(sheet, 11, 5, "TOTAL", 12);
	 * addCaption(sheet, 12, 5, "GRAND TOTAL", 14); }
	 * 
	 * int length = reportlist.size(); CollectionReportBean reportBean = null;
	 * 
	 * double netOpenBal = 0.0; double netCash = 0.0; double netChq = 0.0;
	 * double netChqRet = 0.0; double netDebit = 0.0; double netAccTotal = 0.0;
	 * double netDrawSale = 0.0; double netDrawSaleRefund = 0.0; double
	 * netDrawPwt = 0.0; double netDrawTotal = 0.0; double netScratchSale = 0.0;
	 * double netSaleRet = 0.0; double netScratchPwt = 0.0; double
	 * netScratchTotal = 0.0; double netGrandTotal = 0.0;
	 * 
	 * for (int i = 0; i < length; i++) {
	 * 
	 * reportBean = reportlist.get(i);
	 * 
	 * netOpenBal += Double.parseDouble(reportBean.getOpenBal()); netCash +=
	 * Double.parseDouble(reportBean.getCash()); netChq +=
	 * Double.parseDouble(reportBean.getChq()); netChqRet +=
	 * Double.parseDouble(reportBean.getChqRet()); netDebit +=
	 * Double.parseDouble(reportBean.getDebit()); netAccTotal +=
	 * Double.parseDouble(reportBean.getRecTotal());
	 * 
	 * if (reportBean.getIsDraw() && !reportBean.getIsScratch()) { netDrawSale +=
	 * Double.parseDouble(reportBean.getDrawSale()); netDrawSaleRefund +=
	 * Double.parseDouble(reportBean .getDrawSaleRefund()); netDrawPwt +=
	 * Double.parseDouble(reportBean.getDrawPwt()); netDrawTotal +=
	 * Double.parseDouble(reportBean.getDrawTotal()); netGrandTotal +=
	 * Double.parseDouble(reportBean.getGrandTotal()); }
	 * 
	 * if (reportBean.getIsScratch() && !reportBean.getIsDraw()) {
	 * netScratchSale += Double.parseDouble(reportBean .getScratchSale());
	 * netSaleRet += Double.parseDouble(reportBean.getSaleRet()); netScratchPwt +=
	 * Double.parseDouble(reportBean.getScratchPwt()); netScratchTotal +=
	 * Double.parseDouble(reportBean .getScratchTotal()); netGrandTotal +=
	 * Double.parseDouble(reportBean.getGrandTotal()); }
	 * 
	 * if (reportBean.getIsDraw() && reportBean.getIsScratch()) { netDrawSale +=
	 * Double.parseDouble(reportBean.getDrawSale()); netDrawSaleRefund +=
	 * Double.parseDouble(reportBean .getDrawSaleRefund()); netDrawPwt +=
	 * Double.parseDouble(reportBean.getDrawPwt()); netDrawTotal +=
	 * Double.parseDouble(reportBean.getDrawTotal()); netScratchSale +=
	 * Double.parseDouble(reportBean .getScratchSale()); netSaleRet +=
	 * Double.parseDouble(reportBean.getSaleRet()); netScratchPwt +=
	 * Double.parseDouble(reportBean.getScratchPwt()); netScratchTotal +=
	 * Double.parseDouble(reportBean .getScratchTotal()); netGrandTotal +=
	 * Double.parseDouble(reportBean.getGrandTotal()); }
	 * 
	 * if (reportBean.getIsDraw() && reportBean.getIsScratch()) {
	 * addLabel(sheet, 0, i + 6, reportBean.getSrNo()); addLabel(sheet, 1, i +
	 * 6, reportBean.getName()); addNumber(sheet, 2, i + 6,
	 * Double.parseDouble(reportBean .getOpenBal())); addNumber(sheet, 3, i + 6,
	 * Double.parseDouble(reportBean .getDrawSale())); addNumber(sheet, 4, i +
	 * 6, Double.parseDouble(reportBean .getDrawSaleRefund())); addNumber(sheet,
	 * 5, i + 6, Double.parseDouble(reportBean .getDrawPwt())); addNumber(sheet,
	 * 6, i + 6, Double.parseDouble(reportBean .getDrawTotal()));
	 * addNumber(sheet, 7, i + 6, Double.parseDouble(reportBean
	 * .getScratchSale())); addNumber(sheet, 8, i + 6,
	 * Double.parseDouble(reportBean .getSaleRet())); addNumber(sheet, 9, i + 6,
	 * Double.parseDouble(reportBean .getScratchPwt())); addNumber(sheet, 10, i +
	 * 6, Double.parseDouble(reportBean .getScratchTotal())); addNumber(sheet,
	 * 11, i + 6, Double.parseDouble(reportBean .getCash())); addNumber(sheet,
	 * 12, i + 6, Double.parseDouble(reportBean .getChq())); addNumber(sheet,
	 * 13, i + 6, Double.parseDouble(reportBean .getChqRet())); addNumber(sheet,
	 * 14, i + 6, Double.parseDouble(reportBean .getDebit())); addNumber(sheet,
	 * 15, i + 6, Double.parseDouble(reportBean .getRecTotal()));
	 * addNumber(sheet, 16, i + 6, Double.parseDouble(reportBean
	 * .getGrandTotal())); } if (reportBean.getIsDraw() &&
	 * !(reportBean.getIsScratch())) { addLabel(sheet, 0, i + 6,
	 * reportBean.getSrNo()); addLabel(sheet, 1, i + 6, reportBean.getName());
	 * addNumber(sheet, 2, i + 6, Double.parseDouble(reportBean .getOpenBal()));
	 * addNumber(sheet, 3, i + 6, Double.parseDouble(reportBean
	 * .getDrawSale())); addNumber(sheet, 4, i + 6,
	 * Double.parseDouble(reportBean .getDrawSaleRefund())); addNumber(sheet, 5,
	 * i + 6, Double.parseDouble(reportBean .getDrawPwt())); addNumber(sheet, 6,
	 * i + 6, Double.parseDouble(reportBean .getDrawTotal())); addNumber(sheet,
	 * 7, i + 6, Double.parseDouble(reportBean .getCash())); addNumber(sheet, 8,
	 * i + 6, Double.parseDouble(reportBean .getChq())); addNumber(sheet, 9, i +
	 * 6, Double.parseDouble(reportBean .getChqRet())); addNumber(sheet, 10, i +
	 * 6, Double.parseDouble(reportBean .getDebit())); addNumber(sheet, 11, i +
	 * 6, Double.parseDouble(reportBean .getRecTotal())); addNumber(sheet, 12, i +
	 * 6, Double.parseDouble(reportBean .getGrandTotal())); } if
	 * (reportBean.getIsScratch() && !(reportBean.getIsDraw())) {
	 * addLabel(sheet, 0, i + 6, reportBean.getSrNo()); addLabel(sheet, 1, i +
	 * 6, reportBean.getName()); addNumber(sheet, 2, i + 6,
	 * Double.parseDouble(reportBean .getOpenBal())); addNumber(sheet, 3, i + 6,
	 * Double.parseDouble(reportBean .getScratchSale())); addNumber(sheet, 4, i +
	 * 6, Double.parseDouble(reportBean .getSaleRet())); addNumber(sheet, 5, i +
	 * 6, Double.parseDouble(reportBean .getScratchPwt())); addNumber(sheet, 6,
	 * i + 6, Double.parseDouble(reportBean .getScratchTotal()));
	 * addNumber(sheet, 7, i + 6, Double.parseDouble(reportBean .getCash()));
	 * addNumber(sheet, 8, i + 6, Double.parseDouble(reportBean .getChq()));
	 * addNumber(sheet, 9, i + 6, Double.parseDouble(reportBean .getChqRet()));
	 * addNumber(sheet, 10, i + 6, Double.parseDouble(reportBean .getDebit()));
	 * addNumber(sheet, 11, i + 6, Double.parseDouble(reportBean
	 * .getRecTotal())); addNumber(sheet, 12, i + 6,
	 * Double.parseDouble(reportBean .getGrandTotal())); } }
	 * 
	 * sheet.addCell(new Label(1, length + 6, "Total", headingLabel));
	 * sheet.mergeCells(1, length + 6, 1, length + 6);
	 * 
	 * if (reportBean.getIsDraw() && reportBean.getIsScratch()) {
	 * sheet.addCell(new Number(2, length + 6, netOpenBal,
	 * headingNumberFormat)); sheet.addCell(new Number(3, length + 6,
	 * netDrawSale, headingNumberFormat)); sheet.addCell(new Number(4, length +
	 * 6, netDrawSaleRefund, headingNumberFormat)); sheet.addCell(new Number(5,
	 * length + 6, netDrawPwt, headingNumberFormat)); sheet.addCell(new
	 * Number(6, length + 6, netDrawTotal, headingNumberFormat));
	 * sheet.addCell(new Number(7, length + 6, netScratchSale,
	 * headingNumberFormat)); sheet.addCell(new Number(8, length + 6,
	 * netSaleRet, headingNumberFormat)); sheet.addCell(new Number(9, length +
	 * 6, netScratchPwt, headingNumberFormat)); sheet.addCell(new Number(10,
	 * length + 6, netScratchTotal, headingNumberFormat)); sheet.addCell(new
	 * Number(11, length + 6, netCash, headingNumberFormat)); sheet.addCell(new
	 * Number(12, length + 6, netChq, headingNumberFormat)); sheet.addCell(new
	 * Number(13, length + 6, netChqRet, headingNumberFormat));
	 * sheet.addCell(new Number(14, length + 6, netDebit, headingNumberFormat));
	 * sheet.addCell(new Number(15, length + 6, netAccTotal,
	 * headingNumberFormat)); sheet.addCell(new Number(16, length + 6,
	 * netGrandTotal, headingNumberFormat)); } if (reportBean.getIsDraw() &&
	 * !(reportBean.getIsScratch())) { sheet.addCell(new Number(2, length + 6,
	 * netOpenBal, headingNumberFormat)); sheet.addCell(new Number(3, length +
	 * 6, netDrawSale, headingNumberFormat)); sheet.addCell(new Number(4, length +
	 * 6, netDrawSaleRefund, headingNumberFormat)); sheet.addCell(new Number(5,
	 * length + 6, netDrawPwt, headingNumberFormat)); sheet.addCell(new
	 * Number(6, length + 6, netDrawTotal, headingNumberFormat));
	 * sheet.addCell(new Number(7, length + 6, netCash, headingNumberFormat));
	 * sheet .addCell(new Number(8, length + 6, netChq, headingNumberFormat));
	 * sheet.addCell(new Number(9, length + 6, netChqRet, headingNumberFormat));
	 * sheet.addCell(new Number(10, length + 6, netDebit, headingNumberFormat));
	 * sheet.addCell(new Number(11, length + 6, netAccTotal,
	 * headingNumberFormat)); sheet.addCell(new Number(12, length + 6,
	 * netGrandTotal, headingNumberFormat)); } if (reportBean.getIsScratch() &&
	 * !(reportBean.getIsDraw())) { sheet.addCell(new Number(2, length + 6,
	 * netOpenBal, headingNumberFormat)); sheet.addCell(new Number(3, length +
	 * 6, netScratchSale, headingNumberFormat)); sheet.addCell(new Number(4,
	 * length + 6, netSaleRet, headingNumberFormat)); sheet.addCell(new
	 * Number(5, length + 6, netScratchPwt, headingNumberFormat));
	 * sheet.addCell(new Number(6, length + 6, netScratchTotal,
	 * headingNumberFormat)); sheet.addCell(new Number(7, length + 6, netCash,
	 * headingNumberFormat)); sheet .addCell(new Number(8, length + 6, netChq,
	 * headingNumberFormat)); sheet.addCell(new Number(9, length + 6, netChqRet,
	 * headingNumberFormat)); sheet.addCell(new Number(10, length + 6, netDebit,
	 * headingNumberFormat)); sheet.addCell(new Number(11, length + 6,
	 * netAccTotal, headingNumberFormat)); sheet.addCell(new Number(12, length +
	 * 6, netGrandTotal, headingNumberFormat)); } }
	 */

	private void createHeaderForDGFullReport(WritableSheet sheet)
			throws WriteException, ParseException {
		DateFormat format = new SimpleDateFormat("dd-MMM-yy");

		sheet.addCell(new Label(10, 0,
				" "+findDefaultText("label.date", locale )+"  :  " + format.format(new Date()), dateFormat));
		sheet.mergeCells(10, 0, 11, 0);

		sheet.mergeCells(4, 3, 7, 3);
		sheet.mergeCells(8, 3, 10, 3);
		setHeadingForDGFullReport(sheet);

	}

	private void setHeadingForDGFullReport(WritableSheet sheet)
			throws RowsExceededException, WriteException {
		DateFormat format = new SimpleDateFormat("dd-MMM-yy");
		if (this.endDate == null) {

			logger
					.debug(" inside date if condition -kgkgjdkjkjkfj---- ----user Type : "
							+ this.reportType + " date : " + startDate);
			// System.out.println(" inside date if condition -kgkgjdkjkjkfj----
			// ----user Type : "+this.reportType+" date : "+startDate);
			sheet.addCell(new Label(4, 3, " "+findDefaultText("label.draw.game.sale.and", locale)+" "+ TextConfigurator.getText("PWT") +" "+findDefaultText("label.report.of", locale)+" "
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
			sheet.addCell(new Label(4, 3, findDefaultText("label.draw.game.sale.and", locale)+" "+ TextConfigurator.getText("PWT") +" "+findDefaultText("label.from.date", locale)
					+ "", headerFormat));
			sheet.addCell(new Label(8, 3, " ( " + format.format(startDate)
					+ "   -   " + format.format(endDate) + " ) ",
					headerDateFormat));
		}
	}

	public void write(Map<String, String> repMap, WritableWorkbook workbk,
			String orgName, String orgAddress, Map<Integer, String> gameNumMap, Map<Integer, String> IWGameMap,
			String orgType) throws IOException, WriteException, ParseException {
		WorkbookSettings wbSettings = new WorkbookSettings();
		wbSettings.setLocale(new Locale("en", "EN"));
		WritableWorkbook workbook = workbk;
		workbook.createSheet("Report", 0);
		WritableSheet excelSheet = workbook.getSheet(0);
		if (orgType.equalsIgnoreCase("BO")) {
			createContent(excelSheet, repMap, orgName, orgAddress, gameNumMap, IWGameMap);
		} else {
			// todo implement the export excel in case of agent DG full Report
		}

		workbook.write();
		workbook.close();

	}

}
