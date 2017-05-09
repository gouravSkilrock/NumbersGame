package com.skilrock.lms.web.reportsMgmt.common;

import java.io.IOException;
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

import com.skilrock.lms.beans.CashChqPmntBean;
import com.skilrock.lms.beans.CashChqReportBean;
import com.skilrock.lms.beans.CashierDrawerDataForPWTBean;
import com.skilrock.lms.beans.DateBeans;
import com.skilrock.lms.common.MyTextProvider;

public class WriteExcelForCashChq extends MyTextProvider {
	private WritableCellFormat dateFormat;

	private WritableCellFormat headerDateFormat;
	private WritableCellFormat headerFormat;
	private WritableCellFormat headingLabel;
	private WritableCellFormat headingNumberFormat;
	Log logger = LogFactory.getLog(WriteExcelForCashChq.class);
	private int mainReportLength = 0;
	private WritableCellFormat numberFormat;
	private String reportType;
	private java.util.Date startDate, endDate, reportday;
	private WritableCellFormat times;
	private WritableCellFormat timesBoldUnderline;

	public WriteExcelForCashChq(DateBeans dateBeans) throws WriteException {

		this.reportType = dateBeans.getReportType();
		this.startDate = dateBeans.getStartDate();
		this.endDate = dateBeans.getEndDate();
		this.reportday = dateBeans.getReportday();

		initWriteExcel();
	}

	private void initWriteExcel() throws WriteException {
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

	private void addCaption1(WritableSheet sheet, int column, int row, String s)
			throws RowsExceededException, WriteException {
		Label headingLabel;
		headingLabel = new Label(column, row, s, times);
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
			List<CashChqReportBean> reportlist, String orgName, String orgAdd, String stateName, String cityName, String reportType)
			throws WriteException, RowsExceededException, ParseException {
		sheet.addCell(new Label(2, 1, orgName, headerFormat));
		sheet.mergeCells(2, 1, 10, 1);

		sheet.addCell(new Label(2, 2, orgAdd, headerFormat));
		sheet.mergeCells(2, 2, 10, 2);
		createHeaderForCashChq(sheet, stateName, cityName);
		
		boolean reportTypeStatus = reportType.equalsIgnoreCase("Agentwise") ;
		// add caption
		addCaption(sheet, 1, 8, getText("label.party.name"));
		if(reportTypeStatus){
			addCaption(sheet, 2, 8, getText("label.state"));
			addCaption(sheet, 3, 8, getText("label.city"));
		}
		addCaption(sheet, reportTypeStatus ? 4 : 2, 8, getText("label.cash.colln"));
		addCaption(sheet, reportTypeStatus ? 5 : 3, 8, getText("label.chq.colln"));
		addCaption(sheet, reportTypeStatus ? 6 : 4, 8, getText("label.chq.bounce"));
		addCaption(sheet, reportTypeStatus ? 7 : 5, 8, getText("Credit_Note"));
		addCaption(sheet, reportTypeStatus ? 8 : 6, 8, getText("Debit_Note"));
		addCaption(sheet, reportTypeStatus ? 9 : 7, 8, getText("label.bank.deposit"));
		if(reportType.equalsIgnoreCase("Userwise")){
			addCaption(sheet, reportTypeStatus ? 10 : 8, 8, getText("Claimed_Amount"));
			addCaption(sheet, reportTypeStatus ? 11 : 9, 8, getText("label.net.coll"));
		}else
			addCaption(sheet, reportTypeStatus ? 10 : 8, 8, getText("label.net.coll"));

		double cashColl = 0.0;
		double chqColl = 0.0;
		double chqBounce = 0.0;
		double netColl = 0.0;
		double netCredit = 0.0;
		double netDebit = 0.0;
		double netBankDep = 0.0;
		double netClaimedAmt = 0.0;
		double netAmt = 0.0;

		int length = reportlist.size();
		CashChqReportBean bean = null;
		// sheet.insertRow(1);
		for (int i = 0; i < length; i++) {
			bean = reportlist.get(i);
			if (bean.getName() != "-1") {

				cashColl += Double.parseDouble(bean.getTotalCash());
				chqColl += Double.parseDouble(bean.getTotalChq());
				chqBounce += Double.parseDouble(bean.getCheqBounce());
				netCredit += Double.parseDouble(bean.getCredit());
				netDebit += Double.parseDouble(bean.getDebit());
				netBankDep += Double.parseDouble(bean.getBankDeposit());
				if(reportType.equalsIgnoreCase("Userwise"))
						netClaimedAmt+= Double.parseDouble(bean.getClaimAmt());
				
				addLabel(sheet, 1, i + 9, bean.getName());
				if(reportTypeStatus){
					addLabel(sheet, 2, i + 9, bean.getState());
					addLabel(sheet, 3, i + 9, bean.getCity());
				}
				addNumber(sheet, reportTypeStatus ? 4 : 2, i + 9, Double.parseDouble(bean
						.getTotalCash()));
				addNumber(sheet, reportTypeStatus ? 5 : 3, i + 9, Double.parseDouble(bean
						.getTotalChq()));
				addNumber(sheet, reportTypeStatus ? 6 : 4, i + 9, Double.parseDouble(bean
						.getCheqBounce()));
				addNumber(sheet, reportTypeStatus ? 7 : 5, i + 9, Double.parseDouble(bean.getCredit()));
				addNumber(sheet, reportTypeStatus ? 8 : 6, i + 9, Double.parseDouble(bean.getDebit()));
				addNumber(sheet, reportTypeStatus ? 9 : 7, i + 9, Double.parseDouble(bean
						.getBankDeposit()));
				if(reportType.equalsIgnoreCase("Userwise")){
					addNumber(sheet, reportTypeStatus ? 10 : 8, i + 9, Double.parseDouble(bean.getClaimAmt()));
					netAmt = Double.parseDouble(bean.getTotalCash())
					+ Double.parseDouble(bean.getTotalChq())
					- Double.parseDouble(bean.getCheqBounce())
					+ Double.parseDouble(bean.getCredit())
					- Double.parseDouble(bean.getDebit())
					+ Double.parseDouble(bean.getBankDeposit())
					- Double.parseDouble(bean.getClaimAmt());
					addNumber(sheet, reportTypeStatus ? 11 : 9, i + 9, netAmt);
				}else{
					netAmt = Double.parseDouble(bean.getTotalCash())
					+ Double.parseDouble(bean.getTotalChq())
					- Double.parseDouble(bean.getCheqBounce())
					+ Double.parseDouble(bean.getCredit())
					- Double.parseDouble(bean.getDebit())
					+ Double.parseDouble(bean.getBankDeposit());
					addNumber(sheet, reportTypeStatus ? 10 : 8, i + 9, netAmt);
				}
				netColl += netAmt;

			}

		}

		sheet.addCell(new Label(reportTypeStatus? 3 : 1, length + 8, getText("label.total"),
				headingLabel));
		sheet.addCell(new Number(reportTypeStatus ? 4 : 2, length + 8, cashColl, headingNumberFormat));
		sheet.addCell(new Number(reportTypeStatus ? 5 : 3, length + 8, chqColl, headingNumberFormat));
		sheet.addCell(new Number(reportTypeStatus ? 6 : 4, length + 8, chqBounce,
						headingNumberFormat));
		sheet.addCell(new Number(reportTypeStatus ? 7 : 5, length + 8, netCredit,
						headingNumberFormat));
		sheet.addCell(new Number(reportTypeStatus ? 8 : 6, length + 8, netDebit, headingNumberFormat));
		sheet.addCell(new Number(reportTypeStatus ? 9 : 7, length + 8, netBankDep,
						headingNumberFormat));
		if(reportType.equalsIgnoreCase("Userwise")){
			sheet.addCell(new Number(reportTypeStatus ? 10 : 8, length + 8, netClaimedAmt,
					headingNumberFormat));
			sheet.addCell(new Number(reportTypeStatus ? 11 : 9, length + 8, netColl, headingNumberFormat));
		}else
			sheet.addCell(new Number(reportTypeStatus ? 10 : 8, length + 8, netColl, headingNumberFormat));

		mainReportLength = reportType.equalsIgnoreCase("Userwise")?(length + 9 + 2):(length + 8 + 2);

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

		createHeaderForCashChq(sheet, "", "");
		// add caption

		addCaption(sheet, 2, 4, getText("label.cash.chq.rep.with.bo"));
		sheet.mergeCells(2, 4, 4, 4);

		addCaption(sheet, 1, 5, getText("label.cash.depo"));
		addCaption(sheet, 2, 5, getText("label.chq.depo"));
		addCaption(sheet, 3, 5, getText("label.chq.bounce"));
		addCaption(sheet, 4, 5, getText("Credit_Note"));
		addCaption(sheet, 5, 5, getText("Debit_Note"));
		addCaption(sheet, 6, 5, getText("label.bank.deposit"));
		addCaption(sheet, 7, 5, getText("label.net.depo"));

		if (agentBoDetail != null) {
			addNumber(sheet, 1, 6, Double.parseDouble(agentBoDetail
					.getTotalCash()));
			addNumber(sheet, 2, 6, Double.parseDouble(agentBoDetail
					.getTotalChq()));
			addNumber(sheet, 3, 6, Double.parseDouble(agentBoDetail
					.getCheqBounce()));
			addNumber(sheet, 4, 6, Double
					.parseDouble(agentBoDetail.getCredit()));
			addNumber(sheet, 5, 6, Double.parseDouble(agentBoDetail.getDebit()));
			addNumber(sheet, 6, 6, Double.parseDouble(agentBoDetail
					.getBankDeposit()));
			addNumber(sheet, 7, 6, Double
					.parseDouble(agentBoDetail.getNetAmt()));
		} else {
			addNumber(sheet, 1, 6, 0.0);
			addNumber(sheet, 2, 6, 0.0);
			addNumber(sheet, 3, 6, 0.0);
			addNumber(sheet, 4, 6, 0.0);
			addNumber(sheet, 5, 6, 0.0);
			addNumber(sheet, 6, 6, 0.0);
			addNumber(sheet, 7, 6, 0.0);
		}

		addCaption(sheet, 2, 8, getText("label.cash.chq.rep.from.ret"));
		sheet.mergeCells(2, 8, 5, 8);

		addCaption(sheet, 1, 9, getText("label.party.name"));
		addCaption(sheet, 2, 9, getText("label.cash.colln"));
		addCaption(sheet, 3, 9, getText("label.chq.colln"));
		addCaption(sheet, 4, 9, getText("label.chq.bounce"));
		addCaption(sheet, 5, 9, getText("Credit_Note"));
		addCaption(sheet, 6, 9, getText("Debit_Note"));
		addCaption(sheet, 7, 9, getText("label.net.coll"));

		double cashColl = 0.0;
		double chqColl = 0.0;
		double chqBounce = 0.0;
		double netColl = 0.0;
		double netCredit = 0.0;
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
			netCredit += Double.parseDouble(bean.getCredit());
			netDebit += Double.parseDouble(bean.getDebit());

			addLabel(sheet, 1, i + 10, bean.getName());
			addNumber(sheet, 2, i + 10, Double.parseDouble(bean.getTotalCash()));
			addNumber(sheet, 3, i + 10, Double.parseDouble(bean.getTotalChq()));
			addNumber(sheet, 4, i + 10, Double
					.parseDouble(bean.getCheqBounce()));
			addNumber(sheet, 5, i + 10, Double.parseDouble(bean.getCredit()));
			addNumber(sheet, 6, i + 10, Double.parseDouble(bean.getDebit()));
			addNumber(sheet, 7, i + 10, Double.parseDouble(bean.getNetAmt()));

		}

		sheet.addCell(new Label(1, length + 10, getText("label.total"),
				headingLabel));
		sheet
				.addCell(new Number(2, length + 10, cashColl,
						headingNumberFormat));
		sheet.addCell(new Number(3, length + 10, chqColl, headingNumberFormat));
		sheet
				.addCell(new Number(4, length + 10, chqBounce,
						headingNumberFormat));
		sheet
				.addCell(new Number(5, length + 10, netCredit,
						headingNumberFormat));
		sheet
				.addCell(new Number(6, length + 10, netDebit,
						headingNumberFormat));
		sheet.addCell(new Number(7, length + 10, netColl, headingNumberFormat));

	}

	private int createDetailContentForAgentWise(WritableSheet sheet,
			List<CashChqPmntBean> detailDataList, String orgAdd, int i, String reportType)
			throws WriteException, RowsExceededException, ParseException {

		// sheet.addCell(new Label(2, 1, orgName, headerFormat));
		// sheet.mergeCells(2, 1, 10, 1);

		// sheet.addCell(new Label(2, 2, orgAdd, headerFormat));
		// sheet.mergeCells(2, 2, 10, 2);

		int length = detailDataList.size();
		int j = 0;
		while (j < length) {
			addLabel(sheet, 1, i + j + 6, detailDataList.get(j).getOrgName());
			if(reportType.equalsIgnoreCase("Agentwise")){
				addLabel(sheet, 2, i + j + 6, detailDataList.get(j).getStateName());
				addLabel(sheet, 3, i + j + 6, detailDataList.get(j).getCityName());
			}
			addLabel(sheet, reportType.equalsIgnoreCase("Agentwise") ? 4 : 2, i + j + 6, detailDataList.get(j).getDate());
			addLabel(sheet, reportType.equalsIgnoreCase("Agentwise") ? 5 : 3, i + j + 6, detailDataList.get(j).getVoucherNo());
			addLabel(sheet, reportType.equalsIgnoreCase("Agentwise") ? 6 : 4, i + j + 6, detailDataList.get(j)
					.getPaymentType());
			addNumber(sheet, reportType.equalsIgnoreCase("Agentwise") ? 7 : 5, i + j + 6, detailDataList.get(j)
					.getPaymentAmount());
			j++;

		}
		return i + j;
	}

	private int createDetailContentForDayWise(WritableSheet sheet,
			List<CashChqPmntBean> detailDataList, String orgName,
			String orgAdd, int length) throws WriteException,
			RowsExceededException, ParseException {
		// createHeaderForCashChq(sheet);

		sheet.addCell(new Label(2, length + 1,
				getText("label.cash.chq.rep.voucher.wise.for") + " " + orgName,
				headerFormat));
		sheet.mergeCells(2, length + 1, 4, length + 1);

		sheet.addCell(new Label(2, length + 2, orgAdd, headerFormat));
		sheet.mergeCells(2, length + 2, 10, length + 2);

		addCaption(sheet, 1, length + 3, getText("label.agt.name"));
		addCaption(sheet, 2, length + 3, getText("label.voucher.nbr"));
		addCaption(sheet, 3, length + 3, getText("label.type"));
		addCaption(sheet, 4, length + 3, getText("label.amount"));
		int i = 0;
		int len = detailDataList.size();
		if (len == 0) {
			addLabel(sheet, 1, length + 4, getText("msg.no.record"));
			sheet.mergeCells(1, length + 4, 4, length + 4);
		}
		while (i < len) {
			addLabel(sheet, 1, i + length + 4, detailDataList.get(i).getDate());
			addLabel(sheet, 2, i + length + 4, detailDataList.get(i)
					.getVoucherNo());
			addLabel(sheet, 3, i + length + 4, detailDataList.get(i)
					.getPaymentType());
			addNumber(sheet, 4, i + length + 4, detailDataList.get(i)
					.getPaymentAmount());
			i++;
		}
		length = len + 6;
		return length;
	}

	private void createHeaderForCashChq(WritableSheet sheet, String stateName, String cityName)
			throws WriteException, ParseException {
		DateFormat format = new SimpleDateFormat("dd-MMM-yy");

		sheet.addCell(new Label(10, 0,
				" Date  :  " + format.format(new Date()), dateFormat));
		sheet.mergeCells(10, 0, 11, 0);

		sheet.mergeCells(4, 3, 7, 3);
		sheet.mergeCells(8, 3, 10, 3);
		setHeadingForCashChq(sheet, stateName, cityName);

	}

	// -------------------------- Agent Cash & Cheque detail method

	private void setHeadingForCashChq(WritableSheet sheet, String stateName, String cityName)
			throws RowsExceededException, WriteException {
		DateFormat format = new SimpleDateFormat("dd-MMM-yy");
		if (this.endDate == null) {

			logger
					.debug(" inside date if condition -kgkgjdkjkjkfj---- ----user Type : "
							+ this.reportType + " date : " + startDate);
			// System.out.println(" inside date if condition -kgkgjdkjkjkfj----
			// ----user Type : "+this.reportType+" date : "+startDate);
			sheet.addCell(new Label(4, 3, getText("label.cash.chq.rep.of")
					+ " " + reportType, headerFormat));
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
			sheet.addCell(new Label(4, 3,
					getText("label.cash.chq.rep.from.date"), headerFormat));
			sheet.addCell(new Label(8, 3, " ( " + format.format(startDate)
					+ "   -   " + format.format(endDate) + " ) ",
					headerDateFormat));

			if(stateName != null && stateName.length()>0) {
				sheet.addCell(new Label(4, 4, "State", headerFormat));
				sheet.addCell(new Label(5, 4, stateName, headerFormat));
			}

			if(cityName != null && cityName.length()>0) {
				sheet.addCell(new Label(4, 5, "City", headerFormat));
				sheet.addCell(new Label(5, 5, cityName, headerFormat));
			}
		}
	}

	public void write(List<CashChqReportBean> reportbean,
			WritableWorkbook workbk, String orgName, String orgAdd)
			throws IOException, WriteException, ParseException {
		WorkbookSettings wbSettings = new WorkbookSettings();
		wbSettings.setLocale(new Locale("en", "EN"));
		// WritableWorkbook workbook = Workbook.createWorkbook(file,
		// wbSettings);
		WritableWorkbook workbook = workbk;
		workbook.createSheet(getText("Report"), 0);
		WritableSheet excelSheet = workbook.getSheet(0);

		createContent(excelSheet, reportbean, orgName, orgAdd, "", "","");

		workbook.write();
		workbook.close();
	}

	/**
	 * It create excel sheet for agent that contain retailer wise detail of cash
	 * & cheque transactions.
	 */
	public void writeAgent(List<CashChqReportBean> data,
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
			List<CashChqReportBean> mainSummaryData,
			Map<Integer, List<CashChqPmntBean>> dataMap,
			WritableWorkbook workbk, String orgName, String orgAdd,
			String stateName, String cityName, String reportType)
			throws IOException, WriteException, ParseException {
		WorkbookSettings wbSettings = new WorkbookSettings();
		Locale locale = Locale.getDefault();
		wbSettings.setLocale(locale);
		WritableWorkbook workbook = workbk;
		workbook.createSheet(getText("Report"), 0);
		WritableSheet sheet = workbook.getSheet(0);
		createContent(sheet, mainSummaryData, orgName, orgAdd, stateName, cityName, reportType);
		Iterator<Map.Entry<Integer, List<CashChqPmntBean>>> it = dataMap
				.entrySet().iterator();
		// LocalizedTextUtil.findDefaultText("label.detail.rep",
		// Locale.getDefault());
		workbook.createSheet(getText("label.detail.rep"), 1);
		sheet = workbook.getSheet(1);
		int j = 2;
		createHeaderForCashChq(sheet, stateName, cityName);
		addCaption(sheet, 1, 5, getText("label.party.name"));
		if(reportType.equalsIgnoreCase("Agentwise")){
			addCaption(sheet, 2, 5, getText("label.state"));
			addCaption(sheet, 3, 5, getText("label.city"));
		}
		addCaption(sheet, reportType.equalsIgnoreCase("Agentwise") ? j+2 : 2, 5, getText("label.date"));
		addCaption(sheet, reportType.equalsIgnoreCase("Agentwise") ? j+3 : 3, 5, getText("label.voucher.nbr"));
		addCaption(sheet, reportType.equalsIgnoreCase("Agentwise") ? j+4 : 4, 5, getText("label.type"));
		addCaption(sheet, reportType.equalsIgnoreCase("Agentwise") ? j+5 : 5, 5, getText("label.amount"));
		int i = 0;
		j=2;
		while (it.hasNext()) {
			Map.Entry<Integer, List<CashChqPmntBean>> pair = it.next();
			// / workbook.createSheet(nameIdMap.get(pair.getKey()), i);
			// int k=workbook.getSheets().length-1;
			// WritableSheet sheet = workbook.getSheet(k);
			if (i >= 60000) {

				i = 0;
				sheet = workbook.createSheet(getText("label.detail.rep")
						+ (workbook.getSheets().length - 1), 1);
				createHeaderForCashChq(sheet, stateName, cityName);
				addCaption(sheet, 1, 5, getText("label.party.name"));
				if(reportType.equalsIgnoreCase("Agentwise")){
					addCaption(sheet, 2, 5, getText("label.state"));
					addCaption(sheet, 3, 5, getText("label.city"));
				}
				addCaption(sheet, reportType.equalsIgnoreCase("Agentwise") ? j+2 : 2, 5, getText("label.date"));
				addCaption(sheet, reportType.equalsIgnoreCase("Agentwise") ? j+3 : 3, 5, getText("label.voucher.nbr"));
				addCaption(sheet, reportType.equalsIgnoreCase("Agentwise") ? j+4 : 4, 5, getText("label.type"));
				addCaption(sheet, reportType.equalsIgnoreCase("Agentwise") ? j+5 : 5, 5, getText("label.amount"));
				// k++;

			}
			i = createDetailContentForAgentWise(sheet, pair.getValue(), "", i, reportType);

			// i++;
		}
		workbook.write();
		workbook.close();
	}

	public void writeFullDetailDayWise(List<CashChqReportBean> mainSummaryData,
			Map<String, List<CashChqPmntBean>> dataMap,
			WritableWorkbook workbk, String orgName, String orgAdd,
			String stateName, String cityName)
			throws IOException, WriteException, ParseException {
		WorkbookSettings wbSettings = new WorkbookSettings();
		wbSettings.setLocale(new Locale("en", "EN"));
		WritableWorkbook workbook = workbk;
		workbook.createSheet(getText("Report"), 0);
		WritableSheet excelSheet = workbook.getSheet(0);
		createContent(excelSheet, mainSummaryData, orgName, orgAdd, stateName, cityName,"");
		Iterator<Map.Entry<String, List<CashChqPmntBean>>> it = dataMap
				.entrySet().iterator();
		int i = 1;
		int length = mainReportLength;
		while (it.hasNext()) {
			Map.Entry<String, List<CashChqPmntBean>> pair = it.next();
			System.out.println("dataMap size  is  " + dataMap.size());
			length += createDetailContentForDayWise(excelSheet,
					pair.getValue(), pair.getKey(), "", length);
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

	public void writeFullDetailForPwtTicketsCashierWise(
			List<CashierDrawerDataForPWTBean> detailsForPwtTicketsCashierWise,
			WritableWorkbook workbk, String orgName, String orgAdd)
			throws IOException, WriteException, ParseException {
		WorkbookSettings wbSettings = new WorkbookSettings();
		wbSettings.setLocale(new Locale("en", "EN"));
		WritableWorkbook workbook = workbk;
		workbook.createSheet(getText("label.tktwisecsrwisedetail"), 0);
		WritableSheet excelSheet = workbook.getSheet(0);
		createContentForPwtTicketsCashierWise(excelSheet,
				detailsForPwtTicketsCashierWise, orgName, orgAdd);
		workbook.write();
		workbook.close();
	}

	private void addNumber(WritableSheet sheet, int column, int row,
			java.lang.Number integer) throws WriteException,
			RowsExceededException {
		Number number;
		number = new Number(column, row, integer.doubleValue(), times);
		sheet.addCell(number);
	}

	private void addDoubleNumber(WritableSheet sheet, int column, int row,
			java.lang.Number integer) throws WriteException,
			RowsExceededException {
		Number number;
		number = new Number(column, row, integer.doubleValue(), numberFormat);
		sheet.addCell(number);
	}

	private void createHeaderForAgent(WritableSheet sheet)
			throws WriteException, ParseException {
		DateFormat format = new SimpleDateFormat("dd-MMM-yy");

		sheet.addCell(new Label(2, 0, getText("label.date") + " :  "
				+ format.format(new Date()), dateFormat));
		sheet.mergeCells(2, 0, 3, 0);

		setHeadingForSaleReport(sheet);
		sheet.mergeCells(0, 1, 3, 1);
		sheet.mergeCells(0, 2, 3, 2);

	}

	private void setHeadingForSaleReport(WritableSheet sheet)
			throws RowsExceededException, WriteException {
		DateFormat format = new SimpleDateFormat("dd-MMM-yy");
		if (this.endDate == null) {
			System.out
					.println(" inside date if condition -kgkgjdkjkjkfj---- ----user Type : "
							+ this.reportType + " date : " + startDate);
			sheet.addCell(new Label(0, 1, getText("label.sale.rep.detail"),
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

			sheet.addCell(new Label(0, 1, reportType + " " + getText("Report"),
					headerDateFormat));
			sheet.addCell(new Label(0, 2, " ( " + format.format(startDate)
					+ "   -   " + format.format(endDate) + " ) ",
					headerDateFormat));
		}
	}

	private void createContentForPwtTicketsCashierWise(
			WritableSheet excelSheet,
			List<CashierDrawerDataForPWTBean> detailsForPwtTicketsCashierWise,
			String orgName, String orgAdd) throws RowsExceededException,
			WriteException, ParseException {

		createHeaderForAgent(excelSheet);
		addCaption(excelSheet, 3, 4, getText("label.sNo"));

		int col1Count = 4;
		int row1Count = 4;
		int col2Count = 5;

		addCaption(excelSheet, col1Count, row1Count,
				getText("label.cashier.name") + " ");
		col1Count += 1;
		addCaption(excelSheet, col1Count, row1Count, getText("label.tckt.no")
				+ " ");
		col1Count += 1;
		addCaption(excelSheet, col1Count, row1Count, getText("label.game.name")
				+ " ");
		col1Count += 1;
		addCaption(excelSheet, col1Count, row1Count,
				getText("label.claimed.time") + " ");
		col1Count += 1;
		addCaption(excelSheet, col1Count, row1Count, getText("label.amount")
				+ " ");
		col1Count += 1;

		col1Count += 1;
		col2Count += 1;
		col1Count = 4;
		row1Count = 5;
		col2Count = 3;
		row1Count = 5;

		Iterator<CashierDrawerDataForPWTBean> it = detailsForPwtTicketsCashierWise
				.iterator();
		int serialNo = 1;
		while (it.hasNext()) {
			col1Count = 3;
			CashierDrawerDataForPWTBean bean = it.next();
			addCaption(excelSheet, col1Count, row1Count, String
					.valueOf(serialNo));
			col1Count += 1;
			addCaption1(excelSheet, col1Count, row1Count, bean.getCashierName());
			col1Count += 1;
			addCaption1(excelSheet, col1Count, row1Count, bean
					.getTicketNumber());
			col1Count += 1;
			addCaption1(excelSheet, col1Count, row1Count, bean.getGameName());
			col1Count += 1;
			addCaption1(excelSheet, col1Count, row1Count, bean.getClaimedTime()
					.toString().replace(".0", ""));
			col1Count += 1;
			addDoubleNumber(excelSheet, col1Count, row1Count, bean
					.getClaimedAmount());
			row1Count += 1;
			serialNo++;
		}

	}

}
