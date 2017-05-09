package com.skilrock.lms.web.drawGames.reportsMgmt.common;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

import com.skilrock.lms.beans.SalePwtReportsBean;
import com.skilrock.lms.common.MyTextProvider;
import com.skilrock.lms.common.utility.CommonMethods;
import com.skilrock.lms.common.utility.TextConfigurator;


public class WriteExcelForDrawSaleReport extends MyTextProvider {
	private WritableCellFormat dateFormat;

	private WritableCellFormat headerDateFormat;
	private WritableCellFormat headerFormat;
	private WritableCellFormat headingLabel;
	private WritableCellFormat headingNumberFormat;
	Log logger = LogFactory.getLog(WriteExcelForDrawSaleReport.class);
	private WritableCellFormat numberFormat;
	private WritableCellFormat integerFormat;
	private String reportType;
	private java.util.Date startDate, endDate, reportday;
	private WritableCellFormat times;
	private WritableCellFormat timesBoldUnderline;
	public WriteExcelForDrawSaleReport(Timestamp tStart, Timestamp tEnd, String reportType)throws WriteException{
		this.reportType = reportType;
		this.startDate = new Date(tStart.getTime());
		this.endDate = new Date(tEnd.getTime());
		this.reportday = new Date(tStart.getTime());
		
		numberFormat = new WritableCellFormat(NumberFormats.FORMAT3);
		numberFormat.setFont(new WritableFont(WritableFont.TIMES, 10));
		numberFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
		numberFormat.setWrap(false);

		integerFormat = new WritableCellFormat(NumberFormats.INTEGER);
		integerFormat.setFont(new WritableFont(WritableFont.TIMES, 10));
		integerFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
		integerFormat.setWrap(false);
		
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

	private void addLabel(WritableSheet sheet, int column, int row, Object s, int width)
			throws WriteException, RowsExceededException {
		Label headingLabel;
		headingLabel = new Label(column, row, s!=null?s.toString():"", times);
		sheet.setColumnView(column, width);
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
	
	private void addInteger(WritableSheet sheet, int column, int row, Double amt)
			throws WriteException, RowsExceededException {
		Number headingNumberFormat;
		headingNumberFormat = new Number(column, row, amt, integerFormat);
		sheet.addCell(headingNumberFormat);
}

	private void createContent(WritableSheet sheet,
			List<SalePwtReportsBean> reportlist, String orgname, String orgAdd,
			String currSym, String filter, String city, String state) throws WriteException,
			RowsExceededException, ParseException {

		sheet.addCell(new Label(2, 1, orgname, headerFormat));
		sheet.mergeCells(2, 1, 9, 1);
		sheet.addCell(new Label(10, 1, getText("label.amt.in")+" "  + currSym, headerFormat));
		sheet.setColumnView(10, 15);
		sheet.addCell(new Label(2, 2, orgAdd, headerFormat));
		sheet.mergeCells(2, 2, 10, 2);

		createHeaderForDrawSaleReport(sheet, city, state);
		addCaption(sheet, 4, 5, "S.No.", 2);
		if ("Game".equalsIgnoreCase(filter.split(" ")[0])) {
			addCaption(sheet, 5, 5, filter.split(" ")[0] + " "+getText("label.name"), 10);
		} else {
			addCaption(sheet, 5, 5, ((Map) ServletActionContext
					.getServletContext().getAttribute("TIER_MAP")).get(filter
					.split(" ")[0].toUpperCase())
					+ " "+getText("label.name"), 10);
		}
		sheet.mergeCells(5, 4, 5, 4);
		boolean status = !("Game".equalsIgnoreCase(filter.split(" ")[0])) ; 
		if (status) {
			addCaption(sheet, 6, 5, getText("label.state"), 10);
			sheet.mergeCells(6, 5, 6, 5);
			addCaption(sheet, 7, 5, getText("label.city"), 10);
			sheet.mergeCells(7, 5, 7, 5);
		}
		
		addCaption(sheet, status ? 8 : 6, 5, getText("label.mrp.amt"), 10);
		sheet.mergeCells(status ? 8 : 6, 5, status ? 8 : 6, 4);
		if("Agent Wise".equalsIgnoreCase(filter)){
			addCaption(sheet, status ? 9 : 7, 5, getText("label.net.amt"), 10);
			sheet.mergeCells(status ? 9 : 7, 5, status ? 9 : 7, 4);
		}
		int length = reportlist.size();
		logger.debug("size of bean list" + length);
		int i = 0;
		double totalMrp = 0.0;
		double totalNet = 0.0;
		
		while (i < length) {
			SalePwtReportsBean bean = reportlist.get(i);

				addLabel(sheet, 4, i + 6, (i + 1),6);
				addLabel(sheet, 5, i + 6, bean.getGameName(),15);
				if(status){
				addLabel(sheet, 6, i + 6, bean.getStateCode(),15);
				addLabel(sheet, 7, i + 6, bean.getCityCode(),15);
				}
				addNumber(sheet, status ? 8 : 6, i + 6, bean.getSaleMrpAmt());
				if("Agent Wise".equalsIgnoreCase(filter)){
					addNumber(sheet, status ? 9 : 7, i + 6, bean.getSaleNetAmt());
				}
				totalMrp += bean.getSaleMrpAmt();
				totalNet += bean.getSaleNetAmt();
				i++;
			}
		addCaption(sheet, status ? 7 : 5, length + 6, getText("label.total"), 13);
		sheet.setColumnView(8, 15);
		sheet.addCell(new Number(status ? 8 : 6, length + 6, totalMrp, headingNumberFormat));
		if("Agent Wise".equalsIgnoreCase(filter)){
			sheet.setColumnView(9, 16);
			sheet.addCell(new Number(9, length + 6, totalNet, headingNumberFormat));
		}
	}
	
	private void createContentGameWise(WritableSheet sheet,
			List<SalePwtReportsBean> reportlistExp,  String orgname, String orgAdd,
			String currSym, String filter) throws WriteException,
			RowsExceededException, ParseException {
		sheet.addCell(new Label(2, 1, orgname, headerFormat));
		sheet.mergeCells(2, 1, 9, 1);
		sheet.addCell(new Label(10, 1,getText("label.amt.in")+" "  + currSym, headerFormat));
		sheet.setColumnView(10, 15);
		sheet.addCell(new Label(2, 2, orgAdd, headerFormat));
		sheet.mergeCells(2, 2, 10, 2);

		createHeaderForDrawSaleReport(sheet, "ALL", "ALL");
		addCaption(sheet, 3, 4, getText("label.s.no"), 2);
		addCaption(sheet, 4, 4, getText("label.game.name"), 20);
		addCaption(sheet, 5, 4, getText("label.price.amt"), 15);
		sheet.mergeCells(5, 4, 5, 4);
		addCaption(sheet, 6, 4, getText("label.no.of.tkts"), 15);
		sheet.mergeCells(6, 4, 6, 4);
		/*addCaption(sheet, 7, 4, getText("label.state"), 15);
		sheet.mergeCells(7, 4, 7, 4);
		addCaption(sheet, 8, 4, getText("label.city"), 15);
		sheet.mergeCells(8, 4, 8, 4);*/
		addCaption(sheet, 7, 4, getText("label.mrp.amt"), 15);
		sheet.mergeCells(7, 4, 7, 4);
		
		int length = reportlistExp.size();
		logger.debug("size of bean list expended" + length);
		int i = 0;
		double totalMrp = 0.0;
		
		while (i < length) {
			SalePwtReportsBean bean = reportlistExp.get(i);

				addLabel(sheet, 3, i + 5, (i + 1),15);
				addLabel(sheet, 4, i + 5, bean.getGameName(),15);
				addNumber(sheet, 5, i + 5, bean.getUnitPriceAmt());
				addInteger(sheet, 6, i + 5, bean.getNoOfTkt()+0.0);
				/*addLabel(sheet, 7, i + 5, bean.getStateCode(),15);
				addLabel(sheet, 8, i + 5, bean.getCityCode(),15);*/
				addNumber(sheet, 7, i + 5, bean.getSaleMrpAmt());
				totalMrp += bean.getSaleMrpAmt();
				i++;
			}
		addCaption(sheet, 8, length + 5, getText("label.total"), 13);
		sheet.setColumnView(9, 15);
		sheet.addCell(new Number(9, length + 5, totalMrp, headingNumberFormat));
	}
	
	private void createContentAgentWise(WritableSheet sheet,
			List<SalePwtReportsBean> reportlistExp,  String orgname, String orgAdd,
			String currSym, String filter) throws WriteException,
			RowsExceededException, ParseException {
		sheet.addCell(new Label(2, 1, orgname, headerFormat));
		sheet.mergeCells(2, 1, 9, 1);
		sheet.addCell(new Label(10, 1, getText("label.amt.in")+" " + currSym, headerFormat));
		sheet.setColumnView(10, 15);
		sheet.addCell(new Label(2, 2, orgAdd, headerFormat));
		sheet.mergeCells(2, 2, 10, 2);

		createHeaderForDrawSaleReport(sheet,"ALL", "ALL");
		
		addCaption(sheet, 3, 4, getText("label.s.no"), 2);
		addCaption(sheet, 4, 4, getText("label.game.name"), 15);
		sheet.mergeCells(4, 4, 4, 4);
		addCaption(sheet, 5, 4, getText("label.price.amt"), 15);
		sheet.mergeCells(5, 4, 5, 4);
		addCaption(sheet, 6, 4, getText("label.no.of.tkts"), 15);
		sheet.mergeCells(6, 4, 6, 4);
		addCaption(sheet, 7, 4, getText("label.state"), 15);
		sheet.mergeCells(7, 4, 7, 4);
		addCaption(sheet, 8, 4, getText("label.city"), 15);
		sheet.mergeCells(8, 4, 8, 4);
		addCaption(sheet, 9, 4, getText("label.mrp.amt"), 15);
		sheet.mergeCells(9, 4, 9, 4);
		addCaption(sheet, 10, 4, getText("label.net.amt"), 15);
		sheet.mergeCells(10, 4, 10, 4);
		
		int length = reportlistExp.size();
		logger.debug("size of bean list expended" + length);
		int i = 0;
		double totalMrp = 0.0;
		double totalNet = 0.0;
		
		while (i < length) {
			SalePwtReportsBean bean = reportlistExp.get(i);

				addLabel(sheet, 3, i + 5, (i + 1),15);
				addLabel(sheet, 4, i + 5, bean.getGameName(),15);
				addNumber(sheet, 5, i + 5, bean.getUnitPriceAmt());
				addInteger(sheet, 6, i + 5, bean.getNoOfTkt()+0.0);
				addLabel(sheet, 7, i + 5, bean.getStateCode(),15);
				addLabel(sheet, 8, i + 5, bean.getCityCode(),15);
				addNumber(sheet, 9, i + 5, bean.getSaleMrpAmt());
				addNumber(sheet, 10, i + 5, bean.getSaleNetAmt());
				totalMrp += bean.getSaleMrpAmt();
				totalNet += bean.getSaleNetAmt();
				i++;
			}
		addCaption(sheet, 8, length + 5, getText("label.total"), 13);
		sheet.setColumnView(9, 15);
		sheet.addCell(new Number(9, length + 5, totalMrp, headingNumberFormat));
		sheet.addCell(new Number(10, length + 5, totalNet, headingNumberFormat));
	}

	private void createHeaderForDrawSaleReport(WritableSheet sheet, String city, String state)
			throws WriteException, ParseException {
		DateFormat format = new SimpleDateFormat("dd-MMM-yy");

		sheet.addCell(new Label(10, 0,
				" "+getText("label.date" )+"  :  " + format.format(new Date()), dateFormat));
		sheet.mergeCells(10, 0, 11, 0);

		sheet.mergeCells(4, 3, 6, 3);
		sheet.mergeCells(7, 3, 9, 3);
		setHeadingForDrawSaleReport(sheet, city, state);

	}

	private void setHeadingForDrawSaleReport(WritableSheet sheet, String city, String state)
			throws RowsExceededException, WriteException {
		DateFormat format = new SimpleDateFormat("dd-MMM-yy");
		if (this.endDate == null) {

			logger
					.debug(" inside date if condition -kgkgjdkjkjkfj---- ----user Type : "
							+ this.reportType + " date : " + startDate);
			// System.out.println(" inside date if condition -kgkgjdkjkjkfj----
			// ----user Type : "+this.reportType+" date : "+startDate);
			sheet.addCell(new Label(4, 3, " "+getText("label.draw.game.sale")+" "+ TextConfigurator.getText("Report") +" "+getText("label.of")+" "
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
			sheet.addCell(new Label(4, 3, getText("label.draw.game.sale")+" "+ TextConfigurator.getText("Report") +" "+getText("label.from.date")+" "
					+ "", headerFormat));
			sheet.addCell(new Label(7, 3, " ( " + format.format(startDate)
					+ "   -   " + format.format(endDate) + " ) ",
					headerDateFormat));
			
			if(!("ALL".equalsIgnoreCase(state) || state == null) ){
				sheet.addCell(new Label(5, 4, ("ALL".equalsIgnoreCase(city) || city == null) ? (getText("label.state")+" - "+ CommonMethods.fetchStateList().get(state)) : (getText("label.state")+" - "+ CommonMethods.fetchStateList().get(state)+ " | City - " + city), headerFormat)) ;
				sheet.mergeCells(5, 4, 8, 4);
			}
		}
	}

	public void writeGameWise(List<SalePwtReportsBean> reportBeanList,List<SalePwtReportsBean> reportBeanListExp,
			WritableWorkbook workbk, String orgName, String orgAddress,
			String orgType, String currSym, String stateCode, String cityCode) throws IOException,
			WriteException, ParseException {
		WorkbookSettings wbSettings = new WorkbookSettings();
		wbSettings.setLocale(new Locale("en", "EN"));
		WritableWorkbook workbook = workbk;
		workbook.createSheet("Report", 0);
		workbook.createSheet("Detail", 1);
		WritableSheet MainSheet = workbook.getSheet(0);
		WritableSheet detailSheet = workbook.getSheet(1);
		createContent(MainSheet, reportBeanList, orgName, orgAddress, currSym, "Game Wise", cityCode, stateCode);
		createContentGameWise(detailSheet, reportBeanListExp, orgName, orgAddress, currSym, "Game Wise");
		workbook.write();
		workbook.close();

	}
	
	
	public void writeAgentWise(List<SalePwtReportsBean> reportBeanList, Map<Integer,List<SalePwtReportsBean>> reportBeanListMapExp,
			WritableWorkbook workbk, Map<Integer, List<String>> orgMap, String orgName, String orgAddress,
			String orgType, String currSym, String city, String state) throws IOException,
			WriteException, ParseException {
		WorkbookSettings wbSettings = new WorkbookSettings();
		wbSettings.setLocale(new Locale("en", "EN"));
		WritableWorkbook workbook = workbk;
		workbook.createSheet("Report", 0);
		WritableSheet excelSheet = workbook.getSheet(0);
		createContent(excelSheet, reportBeanList, orgName, orgAddress,
					currSym, "Agent Wise", city, state);

		Iterator<Map.Entry<Integer, List<SalePwtReportsBean>>> it = reportBeanListMapExp
				.entrySet().iterator();
		int i = 1;
		while (it.hasNext()) {
			Map.Entry<Integer, List<SalePwtReportsBean>> pair = it.next();
			List<String> addMap = orgMap.get(pair.getKey());
			workbook.createSheet(addMap.get(0),i);
			excelSheet = workbook.getSheet(i);
			createContentAgentWise(excelSheet, pair.getValue(), addMap.get(0), addMap.get(1),currSym,"Agent Wise");
			i++;
		}
		workbook.write();
		workbook.close();
	}

}
