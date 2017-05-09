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

public class WriteExcelForSaleReport extends LocalizedTextUtil{
	static Log logger = LogFactory.getLog(WriteExcelForSaleReport.class);

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
	public WriteExcelForSaleReport(DateBeans dateBeans) throws WriteException {

		this.reportType = dateBeans.getReportType();
		this.startDate = dateBeans.getStartDate();
		this.endDate = dateBeans.getEndDate();
		this.reportday = dateBeans.getReportday();

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

		sheet.addCell(new Label(5, 0, findDefaultText("label.date",locale)+"  :  " + format.format(new Date()),
				dateFormat));
		sheet.mergeCells(5, 0, 6, 0);

		setHeadingForSaleReportAgentWise(sheet);
		sheet.mergeCells(3, 1, 4, 1);
		sheet.mergeCells(3, 2, 4, 2);

		sheet.mergeCells(2, 2, 4, 2);

		sheet.mergeCells(1, 4, 3, 4);
		sheet.mergeCells(4, 4, 6, 4);

	}

	private void createHeaderForAgentGameWise(WritableSheet sheet)
			throws WriteException, ParseException {
		DateFormat format = new SimpleDateFormat("dd-MMM-yy");

		sheet.addCell(new Label(4, 0, findDefaultText("label.date",locale)+"  :  " + format.format(new Date()),
				dateFormat));
		sheet.mergeCells(4, 0, 5, 0);
		setHeadingForSaleReport(sheet);
		sheet.mergeCells(0, 1, 5, 1);
		sheet.mergeCells(0, 2, 5, 2);

	}

	private void createHeaderForGame(WritableSheet sheet)
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

	private void createHeaderForRetailer(WritableSheet sheet)
			throws WriteException, ParseException {
		DateFormat format = new SimpleDateFormat("dd-MMM-yy");

		sheet.addCell(new Label(2, 0, findDefaultText("label.date",locale)+"   :  " + format.format(new Date()),
				dateFormat));
		sheet.mergeCells(2, 0, 3, 0);

		setHeadingForSaleReport(sheet);
		sheet.mergeCells(0, 1, 3, 1);
		sheet.mergeCells(0, 2, 3, 2);

	}

	private void setHeadingForSaleReport(WritableSheet sheet)
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
	}

	private void setHeadingForSaleReportAgentWise(WritableSheet sheet)
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
	}

	// Agents excel detail

	public void writeAgent(List<SaleReportBean> reportlist,
			WritableWorkbook workbk) throws IOException, WriteException,
			ParseException {
		workbk.createSheet("report", 0);
		WritableSheet excelSheet = workbk.getSheet(0);

		addCaption(excelSheet, 1, 4,findDefaultText("label.mrp.sale.details",locale));
		addCaption(excelSheet, 4, 4, findDefaultText("label.deducted.sale.details", locale));
		// creating header
		createHeaderForAgent(excelSheet);

		// create Caption
		addCaption(excelSheet, 0, 5, findDefaultText("label.party.name", locale));
		addCaption(excelSheet, 1, 5, findDefaultText("label.sale.amt", locale));
		addCaption(excelSheet, 2, 5, findDefaultText("label.return.amt", locale));
		addCaption(excelSheet, 3, 5, findDefaultText("label.net.sale", locale));
		addCaption(excelSheet, 4, 5, findDefaultText("label.sale.amt", locale));
		addCaption(excelSheet, 5, 5, findDefaultText("label.return.amt", locale));
		addCaption(excelSheet, 6, 5, findDefaultText("label.net.sale", locale));

		double saleAmt = 0.0;
		double returnAmt = 0.0;
		double netSale = 0.0;
		double mrpsaleAmt = 0.0;
		double mrpreturnAmt = 0.0;
		double mrpnetSale = 0.0;
		// insert the list of rows
		int length = reportlist.size();
		SaleReportBean bean = null;
		for (int i = 0; i < length; i++) {
			bean = reportlist.get(i);
			System.out.println("get String : $" + bean.getName() + "$");
			addLabel(excelSheet, 0, i + 6, bean.getName());

			mrpsaleAmt += Double.parseDouble(bean.getSaleMrp());
			mrpreturnAmt += Double.parseDouble(bean.getSaleReturnMrp());
			mrpnetSale += Double.parseDouble(bean.getSaleMrp())
					- Double.parseDouble(bean.getSaleReturnMrp());

			saleAmt += Double.parseDouble(bean.getSaleAmt());
			returnAmt += Double.parseDouble(bean.getReturnAmt());
			netSale += Double.parseDouble(bean.getSaleAmt())
					- Double.parseDouble(bean.getReturnAmt());

			addDoubleNumber(excelSheet, 1, i + 6, Double.parseDouble(bean
					.getSaleMrp()));
			addDoubleNumber(excelSheet, 2, i + 6, Double.parseDouble(bean
					.getSaleReturnMrp()));
			addDoubleNumber(excelSheet, 3, i + 6, (Double.parseDouble(bean
					.getSaleMrp()) - Double
					.parseDouble(bean.getSaleReturnMrp())));

			addDoubleNumber(excelSheet, 4, i + 6, Double.parseDouble(bean
					.getSaleAmt()));
			addDoubleNumber(excelSheet, 5, i + 6, Double.parseDouble(bean
					.getReturnAmt()));
			addDoubleNumber(excelSheet, 6, i + 6, (Double.parseDouble(bean
					.getSaleAmt()) - Double.parseDouble(bean.getReturnAmt())));

		}
		excelSheet.addCell(new Label(0, length + 6, findDefaultText("label.total",locale), headingLabel));

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
				headingNumberFormat));

		workbk.write();
		workbk.close();

	}

	public void writeAgentExcelGameWise(List<SaleReportBean> reportlist,
			WritableWorkbook workbk) throws IOException, WriteException,
			ParseException {
		workbk.createSheet("report", 0);
		WritableSheet excelSheet = workbk.getSheet(0);
		// create Header
		createHeaderForAgentGameWise(excelSheet);
		// create Caption
		addCaption(excelSheet, 0, 4, findDefaultText("label.game.name",locale));
		addCaption(excelSheet, 1, 4, findDefaultText("label.cost.tkt",locale));
		addCaption(excelSheet, 2, 4, findDefaultText("label.tickets.book",locale));
		addCaption(excelSheet, 3, 4, findDefaultText("label.existing.books",locale));
		addCaption(excelSheet, 4, 4, findDefaultText("label.books.purchased.from.bo",locale));
		addCaption(excelSheet, 5, 4, findDefaultText("label.books.returned.to.bo",locale));
		addCaption(excelSheet, 6, 4, findDefaultText("label.books.sold.to.retailer",locale));
		addCaption(excelSheet, 7, 4, findDefaultText("label.books.returned.by.retailer",locale));
		addCaption(excelSheet, 8, 4, findDefaultText("label.remaining.books",locale));
		addCaption(excelSheet, 9, 4, findDefaultText("label.tkts.purchased.from.bo",locale));
		addCaption(excelSheet, 10, 4, findDefaultText("label.tkts.returned.to.bo",locale));
		addCaption(excelSheet, 11, 4, findDefaultText("label.tkts.sold.to.retailer",locale));
		addCaption(excelSheet, 12, 4, findDefaultText("label.tkts.returned.by.retailer",locale));

		// insert the list of rows
		long booksSold = 0;
		long booksRet = 0;
		long booksSaleToRet = 0;
		long booksRetByRetailer = 0;
		long booksRemaining = 0;
		long bookExist = 0;
		long ticketsSold = 0;
		long ticketsRet = 0;
		long ticketsSaleToRet = 0;
		long ticketsRetByRetailer = 0;

		int length = reportlist.size();

		SaleReportBean bean = null;
		for (int i = 0; i < length; i++) {
			bean = reportlist.get(i);
			System.out.println("get String : $" + bean.getGamename() + "$");
			addLabel(excelSheet, 0, i + 5, bean.getGamename());
			addDoubleNumber(excelSheet, 1, i + 5, bean.getTicketCost());
			addNumber(excelSheet, 2, i + 5, bean.getTicketsPerBook());
			addNumber(excelSheet, 3, i + 5, bean.getExistingBooks());
			System.out.println("book cost ===================== "
					+ bean.getBookCost());
			addNumber(excelSheet, 4, i + 5, bean.getSale());
			addNumber(excelSheet, 5, i + 5, bean.getSalereturn());
			addNumber(excelSheet, 6, i + 5, bean.getSaleToRetailer());
			addNumber(excelSheet, 7, i + 5, bean.getSaleReturnByRetailer());
			addNumber(excelSheet, 8, i + 5, bean.getAgentsRemBooks());
			
			
			addNumber(excelSheet, 9, i + 5, bean.getLooseSale());
			addNumber(excelSheet, 10, i + 5, bean.getLooseSaleReturn());
			addNumber(excelSheet, 11, i + 5, bean.getLooseSaleToRetailer());
			addNumber(excelSheet, 12, i + 5, bean.getLooseSaleReturnByRetailer());
			
			
			

			bookExist += bean.getExistingBooks();
			booksSold += bean.getSale();
			booksRet += bean.getSalereturn();
			booksSaleToRet += bean.getSaleToRetailer();
			booksRetByRetailer += bean.getSaleReturnByRetailer();
			booksRemaining += bean.getAgentsRemBooks();
			
			ticketsSold += bean.getLooseSale();
			ticketsRet += bean.getLooseSaleReturn();
			ticketsSaleToRet += bean.getLooseSaleToRetailer();
			ticketsRetByRetailer += bean.getLooseSaleReturnByRetailer();
			

		}
		excelSheet.addCell(new Label(0, length + 5, findDefaultText("label.total", locale), headingLabel));
		excelSheet.addCell(new Label(1, length + 5, "", headingLabel));
		excelSheet.addCell(new Label(2, length + 5, "", headingLabel));
		excelSheet.addCell(new Number(3, length + 5, bookExist, headingLabel));
		excelSheet.addCell(new Number(4, length + 5, booksSold, headingLabel));
		excelSheet.addCell(new Number(5, length + 5, booksRet, headingLabel));
		excelSheet.addCell(new Number(6, length + 5, booksSaleToRet,
				headingLabel));
		excelSheet.addCell(new Number(7, length + 5, booksRetByRetailer,
				headingLabel));
		excelSheet.addCell(new Number(8, length + 5, booksRemaining,
				headingLabel));
		excelSheet.addCell(new Number(9, length+ 5, ticketsSold ,headingLabel));
		excelSheet.addCell(new Number(10, length+ 5, ticketsRet ,headingLabel));
		excelSheet.addCell(new Number(11, length+ 5, ticketsSaleToRet ,headingLabel));
		excelSheet.addCell(new Number(12, length+ 5, ticketsRetByRetailer ,headingLabel));
		
		

		workbk.write();
		workbk.close();
	}

	public void writeAgentExcelRetailerWise(List<SaleReportBean> reportlist,
			SaleReportBean purchDetail, WritableWorkbook workbk)
			throws IOException, WriteException, ParseException {

		workbk.createSheet("report", 0);
		WritableSheet excelSheet = workbk.getSheet(0);
		// creating header
		createHeaderForRetailer(excelSheet);
		// create Caption
		addCaption(excelSheet, 1, 4, findDefaultText("label.purchased.details.from.bo", locale));
		excelSheet.mergeCells(1, 4, 6, 4);

		addCaption(excelSheet, 1, 5, findDefaultText("label.mrp.sale.details",locale));
		excelSheet.mergeCells(1, 5, 3, 5);

		addCaption(excelSheet, 4, 5, findDefaultText("label.deducted.sale.details", locale));
		excelSheet.mergeCells(4, 5, 6, 5);

		addCaption(excelSheet, 1, 6, findDefaultText("label.purchased.amt", locale));
		addCaption(excelSheet, 2, 6, findDefaultText("label.return.amt", locale));
		addCaption(excelSheet, 3, 6, findDefaultText("label.net.purchased", locale));
		addCaption(excelSheet, 4, 6, findDefaultText("label.purchased.amt", locale));
		addCaption(excelSheet, 5, 6, findDefaultText("label.return.amt", locale));
		addCaption(excelSheet, 6, 6, findDefaultText("label.net.purchased", locale));
		if (purchDetail != null) {
			addDoubleNumber(excelSheet, 1, 7, Double.parseDouble(purchDetail
					.getSaleMrp()));
			addDoubleNumber(excelSheet, 2, 7, Double.parseDouble(purchDetail
					.getSaleReturnMrp()));
			addDoubleNumber(excelSheet, 3, 7, (Double.parseDouble(purchDetail
					.getSaleMrp()) - Double.parseDouble(purchDetail
					.getSaleReturnMrp())));
			addDoubleNumber(excelSheet, 4, 7, Double.parseDouble(purchDetail
					.getSaleAmt()));
			addDoubleNumber(excelSheet, 5, 7, Double.parseDouble(purchDetail
					.getReturnAmt()));
			addDoubleNumber(excelSheet, 6, 7, (Double.parseDouble(purchDetail
					.getSaleAmt()) - Double.parseDouble(purchDetail
					.getReturnAmt())));
		} else {
			addDoubleNumber(excelSheet, 1, 7, 0.0);
			addDoubleNumber(excelSheet, 2, 7, 0.0);
			addDoubleNumber(excelSheet, 3, 7, 0.0);
			addDoubleNumber(excelSheet, 4, 7, 0.0);
			addDoubleNumber(excelSheet, 5, 7, 0.0);
			addDoubleNumber(excelSheet, 6, 7, 0.0);
		}

		addCaption(excelSheet, 1, 9, findDefaultText("label.sale.details.with.retailer",locale));
		excelSheet.mergeCells(1, 9, 7, 9);

		addCaption(excelSheet, 1, 10, "");

		addCaption(excelSheet, 2, 10, findDefaultText("label.mrp.sale.details",locale));
		excelSheet.mergeCells(2, 10, 4, 10);

		addCaption(excelSheet, 5, 10, findDefaultText("label.deducted.sale.details", locale));
		excelSheet.mergeCells(5, 10, 7, 10);

		addCaption(excelSheet, 1, 11, findDefaultText("label.party.name", locale));
		addCaption(excelSheet, 2, 11, findDefaultText("label.sale.amt", locale));
		addCaption(excelSheet, 3, 11, findDefaultText("label.return.amt", locale));
		addCaption(excelSheet, 4, 11, findDefaultText("label.net.sale", locale));
		addCaption(excelSheet, 5, 11, findDefaultText("label.sale.amt", locale));
		addCaption(excelSheet, 6, 11, findDefaultText("label.return.amt", locale));
		addCaption(excelSheet, 7, 11, findDefaultText("label.net.sale", locale));

		double saleMrp = 0.0;
		double saleReturnMrp = 0.0;
		double netMrp = 0.0;
		double saleAmt = 0.0;
		double returnAmt = 0.0;
		double netSale = 0.0;
		// insert the list of rows
		int length = reportlist.size();
		SaleReportBean bean = null;
		for (int i = 0; i < length; i++) {
			bean = reportlist.get(i);
			System.out.println("get String : $" + bean.getName() + "$");
			addLabel(excelSheet, 1, i + 12, bean.getName());

			saleMrp += Double.parseDouble(bean.getSaleMrp());
			saleReturnMrp += Double.parseDouble(bean.getSaleReturnMrp());
			netMrp += Double.parseDouble(bean.getSaleMrp())
					- Double.parseDouble(bean.getSaleReturnMrp());
			saleAmt += Double.parseDouble(bean.getSaleAmt());
			returnAmt += Double.parseDouble(bean.getReturnAmt());
			netSale += Double.parseDouble(bean.getSaleAmt())
					- Double.parseDouble(bean.getReturnAmt());

			addDoubleNumber(excelSheet, 2, i + 12, Double.parseDouble(bean
					.getSaleMrp()));
			addDoubleNumber(excelSheet, 3, i + 12, Double.parseDouble(bean
					.getSaleReturnMrp()));
			addDoubleNumber(excelSheet, 4, i + 12, (Double.parseDouble(bean
					.getSaleMrp()) - Double
					.parseDouble(bean.getSaleReturnMrp())));
			addDoubleNumber(excelSheet, 5, i + 12, Double.parseDouble(bean
					.getSaleAmt()));
			addDoubleNumber(excelSheet, 6, i + 12, Double.parseDouble(bean
					.getReturnAmt()));
			addDoubleNumber(excelSheet, 7, i + 12, (Double.parseDouble(bean
					.getSaleAmt()) - Double.parseDouble(bean.getReturnAmt())));

		}
		excelSheet.addCell(new Label(1, length + 12, findDefaultText("label.total",locale), headingLabel));
		excelSheet.addCell(new Number(2, length + 12, saleMrp,
				headingNumberFormat));
		excelSheet.addCell(new Number(3, length + 12, saleReturnMrp,
				headingNumberFormat));
		excelSheet.addCell(new Number(4, length + 12, netMrp,
				headingNumberFormat));
		excelSheet.addCell(new Number(5, length + 12, saleAmt,
				headingNumberFormat));
		excelSheet.addCell(new Number(6, length + 12, returnAmt,
				headingNumberFormat));
		excelSheet.addCell(new Number(7, length + 12, netSale,
				headingNumberFormat));

		workbk.write();
		workbk.close();

	}

	public void writeGame(List<SaleReportBean> reportlist,
			WritableWorkbook workbk) throws IOException, WriteException,
			ParseException {
		workbk.createSheet("report", 0);
		WritableSheet excelSheet = workbk.getSheet(0);
		// create Header
		createHeaderForGame(excelSheet);
		// create Caption
		addCaption(excelSheet, 0, 4, findDefaultText("label.game.name",locale));
		addCaption(excelSheet, 1, 4, findDefaultText("label.cost.tkt",locale));
		addCaption(excelSheet, 2, 4, findDefaultText("label.tickets.book",locale));
		addCaption(excelSheet, 3, 4, findDefaultText("label.cost.book", locale));
		addCaption(excelSheet, 4, 4, findDefaultText("label.books.sold", locale));
		addCaption(excelSheet, 5, 4, findDefaultText("label.books.return", locale));

		// insert the list of rows
		long booksSold = 0;
		long booksRet = 0;

		int length = reportlist.size();
		SaleReportBean bean = null;
		for (int i = 0; i < length; i++) {
			bean = reportlist.get(i);
			System.out.println("get String : $" + bean.getGamename() + "$");
			addLabel(excelSheet, 0, i + 5, bean.getGamename());
			addDoubleNumber(excelSheet, 1, i + 5, bean.getTicketCost());
			addNumber(excelSheet, 2, i + 5, bean.getTicketsPerBook());
			addDoubleNumber(excelSheet, 3, i + 5, Double.parseDouble(bean
					.getBookCost()));
			System.out.println("book cost ===================== "
					+ bean.getBookCost());
			addNumber(excelSheet, 4, i + 5, bean.getSale());
			addNumber(excelSheet, 5, i + 5, bean.getSalereturn());

			booksSold += bean.getSale();
			booksRet += bean.getSalereturn();

		}
		excelSheet.addCell(new Label(0, length + 5, findDefaultText("label.total",locale), headingLabel));
		excelSheet.addCell(new Label(1, length + 5, "", headingLabel));
		excelSheet.addCell(new Label(2, length + 5, "", headingLabel));
		excelSheet.addCell(new Label(3, length + 5, "", headingLabel));
		excelSheet.addCell(new Number(4, length + 5, booksSold, headingLabel));
		excelSheet.addCell(new Number(5, length + 5, booksRet, headingLabel));

		workbk.write();
		workbk.close();
	}

	/*
	 * public void write(List<SaleReportBean> reportbean, WritableWorkbook
	 * workbk) throws IOException, WriteException { WorkbookSettings wbSettings =
	 * new WorkbookSettings(); wbSettings.setLocale(new Locale("en", "EN"));
	 * //WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);
	 * WritableWorkbook workbook =workbk; workbook.createSheet("Report", 0);
	 * WritableSheet excelSheet = workbook.getSheet(0); createLabel(excelSheet);
	 * createContent(excelSheet,reportbean); workbook.write(); workbook.close(); }
	 * 
	 * 
	 * 
	 * private void createLabel(WritableSheet sheet) throws WriteException {
	 * WritableFont times10pt = new WritableFont(WritableFont.TIMES, 12); //
	 * Define the cell format times = new WritableCellFormat(times10pt); // Lets
	 * automatically wrap the cells times.setWrap(false); // Create create a
	 * bold font with underlines WritableFont times10ptBoldUnderline = new
	 * WritableFont(WritableFont.TIMES, 12, WritableFont.BOLD,
	 * false,UnderlineStyle.NO_UNDERLINE); timesBoldUnderline = new
	 * WritableCellFormat(times10ptBoldUnderline); // Lets automatically wrap
	 * the cells timesBoldUnderline.setWrap(false);
	 * timesBoldUnderline.setAlignment(Alignment.CENTRE); CellView cv = new
	 * CellView(); cv.setFormat(timesBoldUnderline); // Write a few headers
	 * addCaption(sheet, 0, 0, "Game Name"); addCaption(sheet, 1, 0, "Party
	 * Name"); addCaption(sheet, 2, 0, "Books Sold"); addCaption(sheet, 3, 0,
	 * "Books Return"); }
	 * 
	 * 
	 * 
	 * private void createContent(WritableSheet sheet, List<SaleReportBean>
	 * reportlist) throws WriteException, RowsExceededException {
	 * 
	 * 
	 * int length=reportlist.size(); SaleReportBean bean=null;
	 * sheet.insertRow(1); for(int i=0;i<length;i++) { bean=reportlist.get(i);
	 * System.out.println("get String : $"+bean.getGamename()+"$");
	 * addLabel(sheet, 0, i+2, (bean.getGamename()!=null &&
	 * !"".equals(bean.getGamename().trim())? bean.getGamename():"No Name"));
	 * addLabel(sheet, 1, i+2, bean.getName()); addLabel(sheet, 2, i+2,
	 * bean.getSale()); addNumber(sheet, 3, i+2, bean.getSalereturn()); } }
	 */

}
