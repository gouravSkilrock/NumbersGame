package com.skilrock.lms.web.scratchService.reportsMgmt.common;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.ejb.Local;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.opensymphony.xwork2.util.LocalizedTextUtil;
import com.skilrock.lms.beans.DateBeans;
import com.skilrock.lms.beans.SaleReportBean;
import com.skilrock.lms.beans.ScratchTicketStatusBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.utility.TextConfigurator;

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

public class WriteExcelForTicketStatusReport extends LocalizedTextUtil{
	static Log logger = LogFactory.getLog(WriteExcelForTicketStatusReport.class);

	private WritableCellFormat dateFormat;
	private WritableCellFormat headerDateFormat;
	private WritableCellFormat headerFormat;
	private WritableCellFormat headingLabel;
	private WritableCellFormat headingNumberFormat;
	private WritableCellFormat numberFormat;
    private WritableCellFormat times;
	private WritableCellFormat timesBoldUnderline;
    private Locale locale=Locale.getDefault(); 
	public WriteExcelForTicketStatusReport() throws WriteException {

	/*	this.reportType = dateBeans.getReportType();
		this.startDate = dateBeans.getStartDate();
		this.endDate = dateBeans.getEndDate();
		this.reportday = dateBeans.getReportday();
*/
		numberFormat = new WritableCellFormat(NumberFormats.FORMAT4);
		numberFormat.setFont(new WritableFont(WritableFont.TIMES, 12));
		numberFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
		numberFormat.setWrap(true);

		times = new WritableCellFormat(new WritableFont(WritableFont.TIMES, 12));
		times.setWrap(true);
		times.setBorder(Border.ALL, BorderLineStyle.THIN);

		timesBoldUnderline = new WritableCellFormat(new WritableFont(
				WritableFont.TIMES, 12, WritableFont.BOLD, false,
				UnderlineStyle.NO_UNDERLINE));
		timesBoldUnderline.setWrap(true);
		timesBoldUnderline.setAlignment(Alignment.CENTRE);
		timesBoldUnderline.setBorder(Border.ALL, BorderLineStyle.MEDIUM);
		timesBoldUnderline.setBackground(Colour.GREY_25_PERCENT);

		dateFormat = new WritableCellFormat(DateFormats.FORMAT2);
		dateFormat.setFont(new WritableFont(WritableFont.TIMES, 12,
				WritableFont.BOLD));
		dateFormat.setWrap(true);
		dateFormat.setAlignment(Alignment.RIGHT);

		headerFormat = new WritableCellFormat(new WritableFont(
				WritableFont.TIMES, 15));
		headerFormat.setWrap(false);
		headerFormat.setAlignment(Alignment.CENTRE);

		headerDateFormat = new WritableCellFormat(DateFormats.FORMAT4);
		headerDateFormat.setFont(new WritableFont(WritableFont.TIMES, 12,
				WritableFont.BOLD));
		headerDateFormat.setWrap(true);
		headerDateFormat.setAlignment(Alignment.CENTRE);

		headingLabel = new WritableCellFormat(new WritableFont(
				WritableFont.TIMES, 12));
		headingLabel.setWrap(true);
		headingLabel.setBorder(Border.ALL, BorderLineStyle.MEDIUM);
		// headingLabel.setAlignment(Alignment.LEFT);
		headingLabel.setBackground(Colour.GRAY_25);

		headingNumberFormat = new WritableCellFormat(NumberFormats.FORMAT4);
		headingNumberFormat.setFont(new WritableFont(WritableFont.TIMES, 12));
		headingNumberFormat.setWrap(true);
		headingNumberFormat.setBorder(Border.ALL, BorderLineStyle.MEDIUM);
		headingNumberFormat.setBackground(Colour.GRAY_25);
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

	private void createHeaderForAgent(WritableSheet sheet)
			throws WriteException, ParseException {
		DateFormat format = new SimpleDateFormat("dd-MMM-yy");

		sheet.addCell(new Label(3, 0, findDefaultText("label.date",locale)+"  :  " + format.format(new Date()),
				dateFormat));
		sheet.mergeCells(5, 0, 6, 0);

		//setHeadingForSaleReportAgentWise(sheet);
		sheet.mergeCells(3, 1, 4, 1);
		sheet.mergeCells(3, 2, 4, 2);

		sheet.mergeCells(2, 2, 4, 2);

		sheet.mergeCells(1, 4, 3, 4);
		sheet.mergeCells(4, 4, 6, 4);

	}

	/*private void createHeaderForAgentGameWise(WritableSheet sheet)
			throws WriteException, ParseException {
		DateFormat format = new SimpleDateFormat("dd-MMM-yy");

		sheet.addCell(new Label(4, 0, findDefaultText("label.date",locale)+"  :  " + format.format(new Date()),
				dateFormat));
		sheet.mergeCells(4, 0, 5, 0);
		setHeadingForSaleReport(sheet);
		sheet.mergeCells(0, 1, 5, 1);
		sheet.mergeCells(0, 2, 5, 2);

	}*/

/*	private void createHeaderForGame(WritableSheet sheet)
			throws WriteException, ParseException {
		DateFormat format = new SimpleDateFormat("dd-MMM-yy");

		sheet.addCell(new Label(4, 0, findDefaultText("label.date",locale)+"   :  " + format.format(new Date()),
				dateFormat));
		sheet.mergeCells(4, 0, 5, 0);

		setHeadingForSaleReport(sheet);
		sheet.mergeCells(2, 2, 4, 2);
		// sheet.mergeCells(0, 1, 5, 1);
		// sheet.mergeCells(0, 2, 5, 2);

	}
*/
	private void createHeaderForRetailer(WritableSheet sheet)
			throws WriteException, ParseException {
		DateFormat format = new SimpleDateFormat("dd-MMM-yy");

		sheet.addCell(new Label(2, 0, findDefaultText("label.date",locale)+"   :  " + format.format(new Date()),
				dateFormat));
		sheet.mergeCells(2, 0, 3, 0);

		//setHeadingForSaleReport(sheet);
		sheet.mergeCells(0, 1, 3, 1);
		sheet.mergeCells(0, 2, 3, 2);

	}

	/*private void setHeadingForSaleReport(WritableSheet sheet)
			throws RowsExceededException, WriteException {

		DateFormat format = new SimpleDateFormat("dd-MMM-yy");
		if (this.endDate == null) {
			sheet.addCell(new Label(3, 1, reportType +" "+findDefaultText("label.sale",locale)+" "+TextConfigurator.getText("Report"),
					headerFormat));
			if (reportday == null) {
				sheet.addCell(new Label(2, 2, " ( "
						+ new SimpleDateFormat("MMM-yyyy").format(startDate)
						+ " ) ", headerDateFormat));
			} else {
				sheet.addCell(new Label(2, 2, " ( "
						+ new SimpleDateFormat("dd-MMM-yyyy").format(reportday)
						+ " ) ", headerDateFormat));
			}
		} else {
			sheet.addCell(new Label(3, 1, reportType+" "+findDefaultText("label.sale",locale)+" "+TextConfigurator.getText("Report"),
					headerFormat));
			sheet.addCell(new Label(2, 2, " ( " + format.format(startDate)
					+ "   -   " + format.format(endDate) + " ) ",
					headerDateFormat));
		}
	}*/

	/*private void setHeadingForAgent(WritableSheet sheet)
			throws RowsExceededException, WriteException {

		DateFormat format = new SimpleDateFormat("dd-MMM-yy");
		if (this.endDate == null) {
			sheet.addCell(new Label(3, 1, reportType+" "+findDefaultText("label.sale",locale)+" "+TextConfigurator.getText("Report"),
					headerFormat));
			if (reportday == null) {
				sheet.addCell(new Label(3, 2, " ( "
						+ new SimpleDateFormat("MMM-yyyy").format(startDate)
						+ " ) ", headerDateFormat));
			} else {
				sheet.addCell(new Label(3, 2, " ( "
						+ new SimpleDateFormat("dd-MMM-yyyy").format(reportday)
						+ " ) ", headerDateFormat));
			}
		} else {
			sheet.addCell(new Label(3, 1, reportType+" "+findDefaultText("label.sale",locale)+" "+TextConfigurator.getText("Report"),
					headerFormat));
			sheet.addCell(new Label(3, 2, " ( " + format.format(startDate)
					+ "   -   " + format.format(endDate) + " ) ",
					headerDateFormat));
		}
	}*/

	// Agents excel detail

	public void writeAgentExcel(List<ScratchTicketStatusBean> reportlist,
			WritableWorkbook workbk	,UserInfoBean userBean) throws IOException, WriteException,
			ParseException {
		workbk.createSheet("ticketStatusReport", 0);
		WritableSheet excelSheet = workbk.getSheet(0);

		addCaption(excelSheet, 1, 6,"Ticket Status Report");
	//	addCaption(excelSheet, 4, 4, findDefaultText("label.deducted.sale.details", locale));
		// creating header
		createHeaderForAgent(excelSheet);
		addCaption(excelSheet,3, 3, "Today's Scratch Ticket Sale Status For  "+userBean.getOrgName());

		// create Caption
		addCaption(excelSheet, 0, 5, findDefaultText("label.cashier.name", locale));
		addCaption(excelSheet, 1, 5, findDefaultText("label.game.name", locale));
		addCaption(excelSheet, 2, 5, findDefaultText("label.book.no", locale));
		addCaption(excelSheet, 3, 5, "Total Tickets");
		addCaption(excelSheet, 4, 5,"Tickets Sold Today");
		addCaption(excelSheet, 5, 5,  "Tickets Remaining ");
		// insert the list of rows
		int length = reportlist.size();
		ScratchTicketStatusBean bean = null;
		for (int i = 0; i < length; i++) {
			bean = reportlist.get(i);
			//System.out.println("get String : $" + bean.getName() + "$");
			addLabel(excelSheet, 0, i + 6, bean.getRetailerOrgName());
			addLabel(excelSheet, 1, i + 6, bean.getGameName());
			addLabel(excelSheet, 2, i + 6, bean.getBookNumber());
			addNumber(excelSheet, 3, i + 6, bean.getTotalTickets());
			addNumber(excelSheet, 4, i + 6, bean.getTicketsSold());
			addNumber(excelSheet, 5, i + 6, bean.getTicketsRemaining());

    		

		}
		/*excelSheet.addCell(new Label(0, length + 6, findDefaultText("label.total",locale), headingLabel));

		excelSheet.addCell(new Number(1, length + 6, mrpsaleAmt,
				headingNumberFormat));
		excelSheet.addCell(new Number(2, length + 6, mrpreturnAmt,
				headingNumberFormat));
		excelSheet.addCell(new Number(3, length + 6, mrpnetSale,
				headingNumberFormat));

		excelSheet.addCell(new Number(4, length + 6, saleAmt,
				headingNumberFormat));
		excelSheet.addCell(new Number(5, length + 6, returnAmt,
				headingNumberFormat));
		excelSheet.addCell(new Number(6, length + 6, netSale,
				headingNumberFormat));*/

		workbk.write();
		workbk.close();

	}

	
	public void writeRetailerExcel(List<ScratchTicketStatusBean> reportlist,
			WritableWorkbook workbk,			UserInfoBean userBean) throws IOException, WriteException,
			ParseException {
		workbk.createSheet("ticketStatusReport", 0);
		WritableSheet excelSheet = workbk.getSheet(0);

		
	//	addCaption(excelSheet, 4, 4, findDefaultText("label.deducted.sale.details", locale));
		// creating header
		createHeaderForAgent(excelSheet);
		addCaption(excelSheet, 3, 3, "Today's Scratch Ticket Sale Status For  "+userBean.getOrgName());
		// create Caption
		addCaption(excelSheet, 0, 5, findDefaultText("label.game.name", locale));
		addCaption(excelSheet, 1, 5, findDefaultText("label.book.no", locale));
		addCaption(excelSheet, 2, 5, "Total Tickets");
		addCaption(excelSheet, 3, 5,"Tickets Sold Today");
		addCaption(excelSheet, 4, 5,  "Tickets Remaining ");
		// insert the list of rows
		int length = reportlist.size();
		ScratchTicketStatusBean bean = null;
		for (int i = 0; i < length; i++) {
			bean = reportlist.get(i);
			//System.out.println("get String : $" + bean.getName() + "$");
			addLabel(excelSheet, 0, i + 6, bean.getGameName());
			addLabel(excelSheet, 1, i + 6, bean.getBookNumber());
			addNumber(excelSheet, 2, i + 6, bean.getTotalTickets());
			addNumber(excelSheet, 3, i + 6, bean.getTicketsSold());
			addNumber(excelSheet, 4, i + 6, bean.getTicketsRemaining());

    		

		}
		/*excelSheet.addCell(new Label(0, length + 6, findDefaultText("label.total",locale), headingLabel));

		excelSheet.addCell(new Number(1, length + 6, mrpsaleAmt,
				headingNumberFormat));
		excelSheet.addCell(new Number(2, length + 6, mrpreturnAmt,
				headingNumberFormat));
		excelSheet.addCell(new Number(3, length + 6, mrpnetSale,
				headingNumberFormat));

		excelSheet.addCell(new Number(4, length + 6, saleAmt,
				headingNumberFormat));
		excelSheet.addCell(new Number(5, length + 6, returnAmt,
				headingNumberFormat));
		excelSheet.addCell(new Number(6, length + 6, netSale,
				headingNumberFormat));*/

		workbk.write();
		workbk.close();

	}


	


}
