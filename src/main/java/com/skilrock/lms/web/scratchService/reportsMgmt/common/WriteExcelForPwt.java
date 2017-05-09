package com.skilrock.lms.web.scratchService.reportsMgmt.common;

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
import com.skilrock.lms.beans.GameWisePWTBean;
import com.skilrock.lms.beans.PwtReportBean;
import com.skilrock.lms.common.utility.TextConfigurator;

public class WriteExcelForPwt  extends LocalizedTextUtil {
	static Log logger = LogFactory.getLog(WriteExcelForPwt.class);

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
	private Locale locale=Locale.getDefault(); 
	public WriteExcelForPwt(DateBeans dateBeans) throws WriteException {

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

	private void addCaption(WritableSheet sheet, int column, int row, String s)
			throws RowsExceededException, WriteException {
		Label headingLabel;
		headingLabel = new Label(column, row, s, timesBoldUnderline);
		sheet.setColumnView(column, 20);
		sheet.addCell(headingLabel);
	}

	private void addLabel(WritableSheet sheet, int column, int row, Object s)
			throws WriteException, RowsExceededException {
		Label headingLabel;
		headingLabel = new Label(column, row, s.toString(), times);
		sheet.addCell(headingLabel);
	}

	private void addNumber(WritableSheet sheet, int column, int row, Double amt)
			throws WriteException, RowsExceededException {
		Number headingNumberFormat;

		headingNumberFormat = new Number(column, row, amt, numberFormat);
		sheet.addCell(headingNumberFormat);
	}

	private void createContentAgentWise(WritableSheet sheet,
			List<PwtReportBean> reportlist) throws WriteException,
			RowsExceededException, ParseException {

		createHeader(sheet, "AgentWise");

		// create Caption
		addCaption(sheet, 0, 4, findDefaultText("label.party.name", locale));
		addCaption(sheet, 1, 4, findDefaultText("label.total", locale)+" " + TextConfigurator.getText("PWT") + " "+findDefaultText("label.amount", locale));

		double totalPwtAmt = 0.0;
		int length = reportlist.size();
		PwtReportBean bean = null;
		for (int i = 0; i < length; i++) {
			bean = reportlist.get(i);
			addLabel(sheet, 0, i + 5, bean.getPartyName());
			addNumber(sheet, 1, i + 5, Double.parseDouble(bean.getPwtAmt().replaceAll(",", "")));
			totalPwtAmt += Double.parseDouble(bean.getPwtAmt().replaceAll(",", ""));
		}

		// sheet.addCell(new Label( 0, length+5, "Total",headingLabel));
		// sheet.addCell(new Number(1, length+5,
		// totalPwtAmt,headingNumberFormat));
	}

	private void createContentGameWise(WritableSheet sheet,
			List<GameWisePWTBean> reportlist) throws WriteException,
			RowsExceededException, ParseException {

		createHeader(sheet, "GameWise");

		// create Caption
		addCaption(sheet, 0, 4, findDefaultText("label.game.no", locale));
		addCaption(sheet, 1, 4, findDefaultText("label.game.name", locale));
		addCaption(sheet, 2, 4, ((Map<String, String>) ServletActionContext
				.getServletContext().getAttribute("TIER_MAP")).get("AGENT")
				+ "s Claimed");
		addCaption(sheet, 3, 4, findDefaultText("label.plrs.claimed", locale));
		addCaption(sheet, 4, 4, findDefaultText("label.total", locale)+" "  + TextConfigurator.getText("PWT") + " "+findDefaultText("label.claimed",locale));
		addCaption(sheet, 5, 4, findDefaultText("label.total", locale)+" "   + TextConfigurator.getText("PWT") + " "+findDefaultText("label.amount", locale));

		int length = reportlist.size();
		GameWisePWTBean bean = null;
		for (int i = 0; i < length; i++) {
			bean = reportlist.get(i);
			addLabel(sheet, 0, i + 5, bean.getGameNbr());
			addLabel(sheet, 1, i + 5, bean.getGameName());
			addNumber(sheet, 2, i + 5, Double.parseDouble(bean.getAgentPWT().replaceAll(",", "")));
			addNumber(sheet, 3, i + 5, Double.parseDouble(bean.getPlayerPWT().replaceAll(",", "")));
			addNumber(sheet, 4, i + 5, Double.parseDouble(bean.getSumPwtAtBo().replaceAll(",", "")));
			addNumber(sheet, 5, i + 5, Double.parseDouble(bean.getTotalPWT()
					.replaceAll(",", "")));
			// totalPwtAmt+=Double.parseDouble(bean.getPwtAmt().replaceAll(",", ""));
		}

		// sheet.addCell(new Label( 0, length+5, "Total",headingLabel));
		// sheet.addCell(new Number(1, length+5,
		// totalPwtAmt,headingNumberFormat));

	}

	private void createHeader(WritableSheet sheet, String reportsType)
			throws WriteException, ParseException {
		DateFormat format = new SimpleDateFormat("dd-MMM-yy");

		sheet.addCell(new Label(4, 0, " "+findDefaultText("label.date", locale)+"  :  " + format.format(new Date()),
				dateFormat));
		sheet.mergeCells(4, 0, 5, 0);
		setHeadingForPWT(sheet, reportsType);
		sheet.mergeCells(0, 1, 4, 1);
		sheet.mergeCells(0, 2, 4, 2);

	}

	private void setHeadingForPWT(WritableSheet sheet, String reportsTypename)
			throws RowsExceededException, WriteException {
		DateFormat format = new SimpleDateFormat("dd-MMM-yy");
		if (this.endDate == null) {
			System.out
					.println(" inside date if condition -kgkgjdkjkjkfj---- ----user Type : "
							+ this.reportType + " date : " + startDate);
			sheet.addCell(new Label(0, 1, reportType + " " + TextConfigurator.getText("PWT") + " " + TextConfigurator.getText("Report") + " "
					+ reportsTypename, headerFormat));
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
			sheet.addCell(new Label(0, 1, reportType + " " + TextConfigurator.getText("PWT") + " " + TextConfigurator.getText("Report") + " ",
					headerFormat));
			sheet.addCell(new Label(0, 2, " ( " + format.format(startDate)
					+ "   -   " + format.format(endDate) + " ) ",
					headerDateFormat));
		}
	}

	public void writeAgentWise(List<PwtReportBean> reportbean,
			WritableWorkbook workbk) throws IOException, WriteException,
			ParseException {
		WorkbookSettings wbSettings = new WorkbookSettings();
		wbSettings.setLocale(new Locale("en", "EN"));
		// WritableWorkbook workbook = Workbook.createWorkbook(file,
		// wbSettings);
		WritableWorkbook workbook = workbk;
		workbook.createSheet("Report", 0);
		WritableSheet excelSheet = workbook.getSheet(0);
		createContentAgentWise(excelSheet, reportbean);
		workbook.write();
		workbook.close();
	}

	public void writeGameWise(List<GameWisePWTBean> reportbean,
			WritableWorkbook workbk) throws IOException, WriteException,
			ParseException {
		WorkbookSettings wbSettings = new WorkbookSettings();
		wbSettings.setLocale(new Locale("en", "EN"));
		// WritableWorkbook workbook = Workbook.createWorkbook(file,
		// wbSettings);
		WritableWorkbook workbook = workbk;
		workbook.createSheet("Report", 0);
		WritableSheet excelSheet = workbook.getSheet(0);
		createContentGameWise(excelSheet, reportbean);
		workbook.write();
		workbook.close();
	}

}
