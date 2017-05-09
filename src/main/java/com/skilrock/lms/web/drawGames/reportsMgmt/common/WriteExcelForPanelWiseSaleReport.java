package com.skilrock.lms.web.drawGames.reportsMgmt.common;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

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

import com.opensymphony.xwork2.util.LocalizedTextUtil;
import com.skilrock.lms.beans.DateBeans;
import com.skilrock.lms.common.utility.TextConfigurator;
import com.skilrock.lms.dge.beans.DrawPanelSaleBean;
import com.skilrock.lms.web.drawGames.common.Util;

public class WriteExcelForPanelWiseSaleReport extends LocalizedTextUtil {
	private WritableCellFormat dateFormat;

	private WritableCellFormat dateFormat2;
	private WritableCellFormat headerDateFormat;
	private WritableCellFormat headerFormat;
	private WritableCellFormat headingLabel;
	private WritableCellFormat headingNumberFormat;
	private WritableCellFormat idNumberFormat;
	Log logger = LogFactory.getLog(WriteExcelForPanelWiseSaleReport.class);
	private WritableCellFormat numberFormat;
	private String reportType;
	private java.util.Date startDate, endDate, reportday;
	private WritableCellFormat timeFormat;
	private WritableCellFormat times;
	private WritableCellFormat timesBoldUnderline;
	private Locale locale=Locale.getDefault(); 
	public WriteExcelForPanelWiseSaleReport(DateBeans dateBeans)
			throws WriteException {

		this.reportType = dateBeans.getReportType();
		this.startDate = dateBeans.getStartDate();
		this.endDate = dateBeans.getEndDate();
		this.reportday = dateBeans.getReportday();

		numberFormat = new WritableCellFormat(NumberFormats.FORMAT3);
		numberFormat.setFont(new WritableFont(WritableFont.TIMES, 10));
		numberFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
		numberFormat.setWrap(false);

		idNumberFormat = new WritableCellFormat(NumberFormats.INTEGER);
		idNumberFormat.setWrap(false);
		idNumberFormat.setBorder(Border.ALL, BorderLineStyle.THIN);

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

		dateFormat2 = new WritableCellFormat(DateFormats.FORMAT2);
		timeFormat = new WritableCellFormat(DateFormats.FORMAT11);

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

	private void addId(WritableSheet sheet, int column, int row, Integer id)
			throws WriteException, RowsExceededException {
		Number headingNumberFormat;
		headingNumberFormat = new Number(column, row, id, idNumberFormat);
		sheet.addCell(headingNumberFormat);
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
			List<DrawPanelSaleBean> repList, String orgname, String orgAdd,
			String currSym, int gameId) throws WriteException,
			RowsExceededException, ParseException {
		String gameNameDev = Util.getGameName(gameId);

		sheet.addCell(new Label(2, 1, orgname, headerFormat));
		sheet.mergeCells(2, 1, 9, 1);
		sheet.addCell(new Label(10, 1, findDefaultText("label.amt.in", locale)+" " + currSym, headerFormat));
		sheet.addCell(new Label(2, 2, orgAdd, headerFormat));
		sheet.mergeCells(2, 2, 10, 2);

		createHeaderForPanelWiseSaleReport(sheet);
		addCaption(sheet, 1, 4, findDefaultText("label.draw.id", locale), 10);
		addCaption(sheet, 2, 4, findDefaultText("label.draw.datetime", locale), 13);
		sheet.mergeCells(2, 4, 4, 4);
		if ("Zerotonine".equalsIgnoreCase(gameNameDev)) {
			addCaption(sheet, 5, 4, findDefaultText("label.zero", locale), 8);
			addCaption(sheet, 6, 4, findDefaultText("label.one", locale), 8);
			addCaption(sheet, 7, 4, findDefaultText("label.two", locale), 8);
			addCaption(sheet, 8, 4, findDefaultText("label.three", locale), 8);
			addCaption(sheet, 9, 4, findDefaultText("label.four", locale), 8);
			addCaption(sheet, 10, 4, findDefaultText("label.five", locale), 8);
			addCaption(sheet, 11, 4, findDefaultText("label.six", locale), 8);
			addCaption(sheet, 12, 4, findDefaultText("label.seven", locale), 8);
			addCaption(sheet, 13, 4, findDefaultText("label.eight", locale), 8);
			addCaption(sheet, 14, 4, findDefaultText("label.nine", locale), 8);
		}
		if("Keno".equalsIgnoreCase(gameNameDev)) {
			addCaption(sheet, 5, 4, findDefaultText("label.direct1", locale), 8);
			addCaption(sheet, 6, 4, findDefaultText("label.direct2", locale), 8);
			addCaption(sheet, 7, 4, findDefaultText("label.direct3", locale), 8);
			addCaption(sheet, 8, 4, findDefaultText("label.direct4", locale), 8);
			addCaption(sheet, 9, 4, findDefaultText("label.direct5", locale), 8);
			addCaption(sheet, 10, 4, findDefaultText("label.perm2", locale), 8);
			addCaption(sheet, 11, 4, findDefaultText("label.perm3", locale), 8);
			addCaption(sheet, 12, 4, findDefaultText("label.banker", locale), 8);
			addCaption(sheet, 13, 4, findDefaultText("label.banker1.against.all", locale), 8);
			sheet.mergeCells(13, 4, 14, 4);
		}
		/*if("KenoFour".equalsIgnoreCase(gameNameDev)) {
			addCaption(sheet, 5, 4, "Direct1", 8);
			addCaption(sheet, 6, 4, "Direct2", 8);
			addCaption(sheet, 7, 4, "Direct3", 8);
			addCaption(sheet, 8, 4, "Direct4", 8);
			addCaption(sheet, 9, 4, "Direct5", 8);
			addCaption(sheet, 10, 4, "Perm2", 8);
			addCaption(sheet, 11, 4, "Perm3", 8);
			addCaption(sheet, 12, 4, "Banker", 8);
			addCaption(sheet, 13, 4, "Banker 1 Against All", 8);
			sheet.mergeCells(13, 4, 14, 4);
		}*/
		if("KenoFive".equalsIgnoreCase(gameNameDev) || "KenoFour".equalsIgnoreCase(gameNameDev)) {
			addCaption(sheet, 5, 4, findDefaultText("label.direct1", locale), 8);
			addCaption(sheet, 6, 4, findDefaultText("label.direct2", locale), 8);
			addCaption(sheet, 7, 4, findDefaultText("label.direct3", locale), 8);
			addCaption(sheet, 8, 4, findDefaultText("label.direct4", locale), 8);
			addCaption(sheet, 9, 4, findDefaultText("label.direct5", locale), 8);
			addCaption(sheet, 10, 4,findDefaultText("label.perm2", locale), 8);
			addCaption(sheet, 11, 4,findDefaultText("label.perm3", locale), 8);
			addCaption(sheet, 12, 4,findDefaultText("label.banker", locale), 8);
			addCaption(sheet, 13, 4,findDefaultText("label.banker1.against.all", locale), 8);
			addCaption(sheet, 14, 4,findDefaultText("label.dc.direct2", locale), 8);
			addCaption(sheet, 15, 4,findDefaultText("label.dc.direct3", locale), 8);
			addCaption(sheet, 16, 4,findDefaultText("label.dc.perm2", locale), 8);
			addCaption(sheet, 17, 4,findDefaultText("label.dc.perm2", locale), 8);
			//sheet.mergeCells(13, 4, 14, 4);
		}
		if("Fortune".equalsIgnoreCase(gameNameDev)){
			addCaption(sheet, 5, 4, findDefaultText("label.aries", locale), 8);
			addCaption(sheet, 6, 4, findDefaultText("label.taurus", locale), 8);
			addCaption(sheet, 7, 4, findDefaultText("label.gemini", locale), 8);
			addCaption(sheet, 8, 4, findDefaultText("label.cancer", locale), 8);
			addCaption(sheet, 9, 4, findDefaultText("label.leo", locale), 8);
			addCaption(sheet, 10, 4,findDefaultText("label.virgo", locale), 8);
			addCaption(sheet, 11, 4,findDefaultText("label.libra", locale), 8);
			addCaption(sheet, 12, 4,findDefaultText("label.scorpio", locale), 8);
			addCaption(sheet, 13, 4,findDefaultText("label.sagittarius", locale), 8);
			addCaption(sheet, 14, 4,findDefaultText("label.capricorn", locale), 8);
			addCaption(sheet, 15, 4,findDefaultText("label.aquarius", locale), 8);
			addCaption(sheet, 16, 4,findDefaultText("label.pisces", locale), 8);
		}
		if("SuperTwo".equalsIgnoreCase(gameNameDev)) {
			addCaption(sheet, 5, 4, findDefaultText("label.dir2.position", locale), 13);
			addCaption(sheet, 6, 4, findDefaultText("label.dir2.regular", locale), 13);
			addCaption(sheet, 7, 4, findDefaultText("label.banker.front", locale), 13);
			addCaption(sheet, 8, 4, findDefaultText("label.banker.rear", locale), 13);
			sheet.mergeCells(13, 4, 14, 4);
		}
		if("FortuneTwo".equalsIgnoreCase(gameNameDev)) {
			addCaption(sheet, 5, 4, findDefaultText("label.direct1", locale), 13);
			addCaption(sheet, 6, 4, findDefaultText("label.direct2", locale), 13);
			addCaption(sheet, 7, 4, findDefaultText("label.direct3", locale), 13);
			addCaption(sheet, 8, 4, findDefaultText("label.direct4", locale), 13);
			addCaption(sheet, 9, 4, findDefaultText("label.group2", locale), 13);
			addCaption(sheet, 10, 4,findDefaultText("label.group3", locale), 13);
			sheet.mergeCells(13, 4, 14, 4);
		}
		
		if("KenoTwo".equalsIgnoreCase(gameNameDev)) {
			addCaption(sheet, 5, 4, findDefaultText("label.pick1", locale), 13);
			addCaption(sheet, 6, 4, findDefaultText("label.pick2", locale), 13);
			addCaption(sheet, 7, 4, findDefaultText("label.pick3", locale), 13);
			sheet.mergeCells(13, 4, 14, 4);
		}
		
		if("Zimlottotwo".equalsIgnoreCase(gameNameDev) || "ZimLottoBonus".equalsIgnoreCase(gameNameDev) || "ZimLottoBonusFree".equalsIgnoreCase(gameNameDev)) {
			addCaption(sheet, 5, 4, findDefaultText("label.direct6", locale), 13);
			addCaption(sheet, 6, 4, findDefaultText("label.perm6", locale), 13);
			sheet.mergeCells(13, 4, 14, 4);
		}
		int length = repList.size();

		int i = 0;
		Map<Integer, Double> totalMap = new TreeMap<Integer, Double>();
		totalMap.put(0, 0.0);
		totalMap.put(1, 0.0);
		totalMap.put(2, 0.0);
		totalMap.put(3, 0.0);
		totalMap.put(4, 0.0);
		totalMap.put(5, 0.0);
		totalMap.put(6, 0.0);
		totalMap.put(7, 0.0);
		totalMap.put(8, 0.0);
		totalMap.put(9, 0.0);
		totalMap.put(10, 0.0);
		totalMap.put(11, 0.0);
		totalMap.put(12, 0.0);

		DrawPanelSaleBean bean = null;
		while (i < length) {
			bean = repList.get(i);
			Map<String, Double> map = bean.getPanelSaleMap();
			addId(sheet, 1, i + 5, bean.getDrawId());
			addLabel(sheet, 2, i + 5, bean.getDrawDateTime());
			sheet.mergeCells(2, i + 5, 4, i + 5);
			if ("Zerotonine".equalsIgnoreCase(gameNameDev)) {
				addNumber(sheet, 5, i + 5, map.get("Zero(0)"));
				totalMap.put(0, totalMap.get(0) + map.get("Zero(0)"));
				addNumber(sheet, 6, i + 5, map.get("One(1)"));
				totalMap.put(1, totalMap.get(1) + map.get("One(1)"));
				addNumber(sheet, 7, i + 5, map.get("Two(2)"));
				totalMap.put(2, totalMap.get(2) + map.get("Two(2)"));
				addNumber(sheet, 8, i + 5, map.get("Three(3)"));
				totalMap.put(3, totalMap.get(3) + map.get("Three(3)"));
				addNumber(sheet, 9, i + 5, map.get("Four(4)"));
				totalMap.put(4, totalMap.get(4) + map.get("Four(4)"));
				addNumber(sheet, 10, i + 5, map.get("Five(5)"));
				totalMap.put(5, totalMap.get(5) + map.get("Five(5)"));
				addNumber(sheet, 11, i + 5, map.get("Six(6)"));
				totalMap.put(6, totalMap.get(6) + map.get("Six(6)"));
				addNumber(sheet, 12, i + 5, map.get("Seven(7)"));
				totalMap.put(7, totalMap.get(7) + map.get("Seven(7)"));
				addNumber(sheet, 13, i + 5, map.get("Eight(8)"));
				totalMap.put(8, totalMap.get(8) + map.get("Eight(8)"));
				addNumber(sheet, 14, i + 5, map.get("Nine(9)"));
				totalMap.put(9, totalMap.get(9) + map.get("Nine(9)"));
			}
			if("Keno".equalsIgnoreCase(gameNameDev)){
				addNumber(sheet, 5, i + 5, map.get("Direct1"));
				totalMap.put(0, totalMap.get(0) + map.get("Direct1"));
				addNumber(sheet, 6, i + 5, map.get("Direct2"));
				totalMap.put(1, totalMap.get(1) + map.get("Direct2"));
				addNumber(sheet, 7, i + 5, map.get("Direct3"));
				totalMap.put(2, totalMap.get(2) + map.get("Direct3"));
				addNumber(sheet, 8, i + 5, map.get("Direct4"));
				totalMap.put(3, totalMap.get(3) + map.get("Direct4"));
				addNumber(sheet, 9, i + 5, map.get("Direct5"));
				totalMap.put(4, totalMap.get(4) + map.get("Direct5"));
				addNumber(sheet, 10, i + 5, map.get("Perm2"));
				totalMap.put(5, totalMap.get(5) + map.get("Perm2"));
				addNumber(sheet, 11, i + 5, map.get("Perm3"));
				totalMap.put(6, totalMap.get(6) + map.get("Perm3"));
				addNumber(sheet, 12, i + 5, map.get("Banker"));
				totalMap.put(7, totalMap.get(7) + map.get("Banker"));
				addNumber(sheet, 13, i + 5, map.get("Banker1AgainstAll"));
				totalMap.put(8, totalMap.get(8) + map.get("Banker1AgainstAll"));
				sheet.mergeCells(13, i + 5, 14, i + 5);
			}
			/*if("KenoFour".equalsIgnoreCase(gameNameDev)){
				addNumber(sheet, 5, i + 5, map.get("Direct1"));
				totalMap.put(0, totalMap.get(0) + map.get("Direct1"));
				addNumber(sheet, 6, i + 5, map.get("Direct2"));
				totalMap.put(1, totalMap.get(1) + map.get("Direct2"));
				addNumber(sheet, 7, i + 5, map.get("Direct3"));
				totalMap.put(2, totalMap.get(2) + map.get("Direct3"));
				addNumber(sheet, 8, i + 5, map.get("Direct4"));
				totalMap.put(3, totalMap.get(3) + map.get("Direct4"));
				addNumber(sheet, 9, i + 5, map.get("Direct5"));
				totalMap.put(4, totalMap.get(4) + map.get("Direct5"));
				addNumber(sheet, 10, i + 5, map.get("Perm2"));
				totalMap.put(5, totalMap.get(5) + map.get("Perm2"));
				addNumber(sheet, 11, i + 5, map.get("Perm3"));
				totalMap.put(6, totalMap.get(6) + map.get("Perm3"));
				addNumber(sheet, 12, i + 5, map.get("Banker"));
				totalMap.put(7, totalMap.get(7) + map.get("Banker"));
				addNumber(sheet, 13, i + 5, map.get("Banker1AgainstAll"));
				totalMap.put(8, totalMap.get(8) + map.get("Banker1AgainstAll"));
				sheet.mergeCells(13, i + 5, 14, i + 5);
			}*/
			if("KenoFive".equalsIgnoreCase(gameNameDev) || "KenoFour".equalsIgnoreCase(gameNameDev)){
				addNumber(sheet, 5, i + 5, map.get("Direct1"));
				totalMap.put(0, totalMap.get(0) + map.get("Direct1"));
				addNumber(sheet, 6, i + 5, map.get("Direct2"));
				totalMap.put(1, totalMap.get(1) + map.get("Direct2"));
				addNumber(sheet, 7, i + 5, map.get("Direct3"));
				totalMap.put(2, totalMap.get(2) + map.get("Direct3"));
				addNumber(sheet, 8, i + 5, map.get("Direct4"));
				totalMap.put(3, totalMap.get(3) + map.get("Direct4"));
				addNumber(sheet, 9, i + 5, map.get("Direct5"));
				totalMap.put(4, totalMap.get(4) + map.get("Direct5"));
				addNumber(sheet, 10, i + 5, map.get("Perm2"));
				totalMap.put(5, totalMap.get(5) + map.get("Perm2"));
				addNumber(sheet, 11, i + 5, map.get("Perm3"));
				totalMap.put(6, totalMap.get(6) + map.get("Perm3"));
				addNumber(sheet, 12, i + 5, map.get("Banker"));
				totalMap.put(7, totalMap.get(7) + map.get("Banker"));
				addNumber(sheet, 13, i + 5, map.get("Banker1AgainstAll"));
				totalMap.put(8, totalMap.get(8) + map.get("Banker1AgainstAll"));
				addNumber(sheet, 14, i + 5, map.get("DC-Direct2"));
				totalMap.put(9, totalMap.get(9) + map.get("DC-Direct2"));
				addNumber(sheet, 15, i + 5, map.get("DC-Direct3"));
				totalMap.put(10, totalMap.get(10) + map.get("DC-Direct3"));
				addNumber(sheet, 16, i + 5, map.get("DC-Perm2"));
				totalMap.put(11, totalMap.get(11) + map.get("DC-Perm2"));
				addNumber(sheet, 17, i + 5, map.get("DC-Perm3"));
				totalMap.put(12, totalMap.get(12) + map.get("DC-Perm3"));
				//sheet.mergeCells(13, i + 5, 14, i + 5);
			}
			if("Fortune".equalsIgnoreCase(gameNameDev)){
				addNumber(sheet, 5, i + 5, map.get("Aries"));
				totalMap.put(0, totalMap.get(0) + map.get("Aries"));
				addNumber(sheet, 6, i + 5, map.get("Taurus"));
				totalMap.put(1, totalMap.get(1) + map.get("Taurus"));
				addNumber(sheet, 7, i + 5, map.get("Gemini"));
				totalMap.put(2, totalMap.get(2) + map.get("Gemini"));
				addNumber(sheet, 8, i + 5, map.get("Cancer"));
				totalMap.put(3, totalMap.get(3) + map.get("Cancer"));
				addNumber(sheet, 9, i + 5, map.get("Leo"));
				totalMap.put(4, totalMap.get(4) + map.get("Leo"));
				addNumber(sheet, 10, i + 5, map.get("Virgo"));
				totalMap.put(5, totalMap.get(5) + map.get("Virgo"));
				addNumber(sheet, 11, i + 5, map.get("Libra"));
				totalMap.put(6, totalMap.get(6) + map.get("Libra"));
				addNumber(sheet, 12, i + 5, map.get("Scorpio"));
				totalMap.put(7, totalMap.get(7) + map.get("Scorpio"));
				addNumber(sheet, 13, i + 5, map.get("Sagittarius"));
				totalMap.put(8, totalMap.get(8) + map.get("Sagittarius"));
				addNumber(sheet, 14, i + 5, map.get("Capricorn"));
				totalMap.put(9, totalMap.get(9) + map.get("Capricorn"));
				addNumber(sheet, 15, i + 5, map.get("Aquarius"));
				totalMap.put(10, totalMap.get(10) + map.get("Aquarius"));
				addNumber(sheet, 16, i + 5, map.get("Pisces"));
				totalMap.put(11, totalMap.get(11) + map.get("Pisces"));
			}
			if("SuperTwo".equalsIgnoreCase(gameNameDev)){
				addNumber(sheet, 5, i + 5, map.get("Dir-2 Position"));
				totalMap.put(0, totalMap.get(0) + map.get("Dir-2 Position"));
				addNumber(sheet, 6, i + 5, map.get("Dir-2 Regular"));
				totalMap.put(1, totalMap.get(1) + map.get("Dir-2 Regular"));
				addNumber(sheet, 7, i + 5, map.get("Banker-Front"));
				totalMap.put(2, totalMap.get(2) + map.get("Banker-Front"));
				addNumber(sheet, 8, i + 5, map.get("Banker-Rear"));
				totalMap.put(3, totalMap.get(3) + map.get("Banker-Rear"));
				sheet.mergeCells(13, i + 5, 14, i + 5);
			}
			if("FortuneTwo".equalsIgnoreCase(gameNameDev)){
				addNumber(sheet, 5, i + 5, map.get("Direct1"));
				totalMap.put(0, totalMap.get(0) + map.get("Direct1"));
				addNumber(sheet, 6, i + 5, map.get("Direct2"));
				totalMap.put(1, totalMap.get(1) + map.get("Direct2"));
				addNumber(sheet, 7, i + 5, map.get("Direct3"));
				totalMap.put(2, totalMap.get(2) + map.get("Direct3"));
				addNumber(sheet, 8, i + 5, map.get("Direct4"));
				totalMap.put(3, totalMap.get(3) + map.get("Direct4"));
				addNumber(sheet, 9, i + 5, map.get("Banker2"));
				totalMap.put(4, totalMap.get(4) + map.get("Banker2"));
				addNumber(sheet, 10, i + 5, map.get("Banker3"));
				totalMap.put(5, totalMap.get(5) + map.get("Banker3"));
				sheet.mergeCells(13, i + 5, 14, i + 5);
			}
			if("KenoTwo".equalsIgnoreCase(gameNameDev)){
				addNumber(sheet, 5, i + 5, map.get("Perm1"));
				totalMap.put(0, totalMap.get(0) + map.get("Perm1"));
				addNumber(sheet, 6, i + 5, map.get("Perm2"));
				totalMap.put(1, totalMap.get(1) + map.get("Perm2"));
				addNumber(sheet, 7, i + 5, map.get("Perm3"));
				totalMap.put(2, totalMap.get(2) + map.get("Perm3"));
				sheet.mergeCells(13, i + 5, 14, i + 5);
			}
			if("Zimlottotwo".equalsIgnoreCase(gameNameDev)  || "ZimLottoBonus".equalsIgnoreCase(gameNameDev)  || "ZimLottoBonusFree".equalsIgnoreCase(gameNameDev)){
				addNumber(sheet, 5, i + 5, map.get("Direct6"));
				totalMap.put(0, totalMap.get(0) + map.get("Direct6"));
				addNumber(sheet, 6, i + 5, map.get("Perm6"));
				totalMap.put(1, totalMap.get(1) + map.get("Perm6"));
				sheet.mergeCells(13, i + 5, 14, i + 5);
			}
			i++;
		}
		addCaption(sheet, 4, length + 5, findDefaultText("label.total", locale), 10);
		int j = 0;
		Iterator<Map.Entry<Integer, Double>> it = totalMap.entrySet()
				.iterator();
		if ("Zerotonine".equalsIgnoreCase(gameNameDev)) {
			while (j < 10) {
				Map.Entry<Integer, Double> pair = it.next();
				sheet.addCell(new Number(j + 5, length + 5, pair.getValue(),
						headingNumberFormat));
				j++;
			}
		}
		if("Keno".equalsIgnoreCase(gameNameDev)) {
			while (j < 9) {
				Map.Entry<Integer, Double> pair = it.next();
				sheet.addCell(new Number(j + 5, length + 5, pair.getValue(),
						headingNumberFormat));
				j++;
			}
			sheet.mergeCells(j + 4, length + 5, j + 5, length + 5);
		}
		/*if("KenoFour".equalsIgnoreCase(gameNameDev)) {
			while (j < 9) {
				Map.Entry<Integer, Double> pair = it.next();
				sheet.addCell(new Number(j + 5, length + 5, pair.getValue(),
						headingNumberFormat));
				j++;
			}
			sheet.mergeCells(j + 4, length + 5, j + 5, length + 5);
		}*/
		if("KenoFive".equalsIgnoreCase(gameNameDev) || "KenoFour".equalsIgnoreCase(gameNameDev)) {
			while (j < 13) {
				Map.Entry<Integer, Double> pair = it.next();
				sheet.addCell(new Number(j + 5, length + 5, pair.getValue(),
						headingNumberFormat));
				j++;
			}
		}
		if("Fortune".equalsIgnoreCase(gameNameDev)){
			while (j < 12) {
				Map.Entry<Integer, Double> pair = it.next();
				sheet.addCell(new Number(j + 5, length + 5, pair.getValue(),
						headingNumberFormat));
				j++;
			}
		}
		if("SuperTwo".equalsIgnoreCase(gameNameDev)) {
			while (j < 4) {
				Map.Entry<Integer, Double> pair = it.next();
				sheet.addCell(new Number(j + 5, length + 5, pair.getValue(),
						headingNumberFormat));
				j++;
			}
			//sheet.mergeCells(j + 4, length + 5, j + 5, length + 5);
		}
		if("FortuneTwo".equalsIgnoreCase(gameNameDev)) {
			while (j < 6) {
				Map.Entry<Integer, Double> pair = it.next();
				sheet.addCell(new Number(j + 5, length + 5, pair.getValue(),
						headingNumberFormat));
				j++;
			}
		}
		if("KenoTwo".equalsIgnoreCase(gameNameDev)) {
			while (j < 3) {
				Map.Entry<Integer, Double> pair = it.next();
				sheet.addCell(new Number(j + 5, length + 5, pair.getValue(),
						headingNumberFormat));
				j++;
			}
		}
		if("Zimlottotwo".equalsIgnoreCase(gameNameDev)  || "ZimLottoBonus".equalsIgnoreCase(gameNameDev)  || "ZimLottoBonusFree".equalsIgnoreCase(gameNameDev)) {
			while (j < 2) {
				Map.Entry<Integer, Double> pair = it.next();
				sheet.addCell(new Number(j + 5, length + 5, pair.getValue(),
						headingNumberFormat));
				j++;
			}
		}
	}

	private void createHeaderForPanelWiseSaleReport(WritableSheet sheet)
			throws WriteException, ParseException {
		DateFormat format = new SimpleDateFormat("dd-MMM-yy");

		sheet.addCell(new Label(10, 0,
				" Date  :  " + format.format(new Date()), dateFormat));
		sheet.mergeCells(10, 0, 11, 0);

		sheet.mergeCells(4, 3, 7, 3);
		sheet.mergeCells(8, 3, 10, 3);
		setHeadingForPanelWiseSaleReport(sheet);

	}

	private void setHeadingForPanelWiseSaleReport(WritableSheet sheet)
			throws RowsExceededException, WriteException {
		DateFormat format = new SimpleDateFormat("dd-MMM-yy");
		if (this.endDate == null) {

			logger
					.debug(" inside date if condition -kgkgjdkjkjkfj---- ----user Type : "
							+ this.reportType + " date : " + startDate);
			// System.out.println(" inside date if condition -kgkgjdkjkjkfj----
			// ----user Type : "+this.reportType+" date : "+startDate);
			sheet.addCell(new Label(4, 3, " "+findDefaultText("label.draw.panel.wise.sale", locale)+" " + TextConfigurator.getText("Report") + " "+findDefaultText("label.of", locale)+" "
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
			sheet.addCell(new Label(4, 3, " "+findDefaultText("label.draw.panel.wise.sale", locale)+" " + TextConfigurator.getText("Report") +" "+findDefaultText("label.from.date", locale)+" "
					+ "", headerFormat));
			sheet.addCell(new Label(8, 3, " ( " + format.format(startDate)
					+ "   -   " + format.format(endDate) + " ) ",
					headerDateFormat));
		}
	}

	public void write(List<DrawPanelSaleBean> repList, WritableWorkbook workbk,
			String orgName, String orgAddress, String orgType, String currSym,
			int gameId) throws IOException, WriteException, ParseException {
		WorkbookSettings wbSettings = new WorkbookSettings();
		wbSettings.setLocale(new Locale("en", "EN"));
		WritableWorkbook workbook = workbk;
		workbook.createSheet("Report", 0);
		WritableSheet excelSheet = workbook.getSheet(0);
		if (orgType.equalsIgnoreCase("BO")) {
			createContent(excelSheet, repList, orgName, orgAddress, currSym,
					gameId);
		} else {
			// todo implement the export excel in case of agent DG full Report
		}

		workbook.write();
		workbook.close();

	}

	/*
	 * private void addDate(WritableSheet sheet, int column, int row, Date date)
	 * throws WriteException, RowsExceededException { DateTime DT; DT = new
	 * DateTime(column, row, date, dateFormat2, true); sheet.addCell(DT); }
	 * 
	 * private void addTime(WritableSheet sheet, int column, int row, Date date)
	 * throws WriteException, RowsExceededException { DateTime DT; DT = new
	 * DateTime(column, row, date, timeFormat, true); sheet.addCell(DT); }
	 */

}
