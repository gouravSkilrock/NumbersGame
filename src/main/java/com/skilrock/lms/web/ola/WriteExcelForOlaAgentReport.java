package com.skilrock.lms.web.ola;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.write.DateFormats;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.NumberFormats;
import jxl.write.WritableCell;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import com.opensymphony.xwork2.util.LocalizedTextUtil;
import com.skilrock.lms.beans.OlaAgentReportBean;

public class WriteExcelForOlaAgentReport extends LocalizedTextUtil {
	private WritableCellFormat numberFormat;
	private WritableCellFormat times;
	private WritableCellFormat timesBoldUnderline;
	private WritableCellFormat dateFormat;
	private WritableCellFormat headerDateFormat;
	private WritableCellFormat headerFormat;
	private WritableCellFormat headingLabel;
	private WritableCellFormat headingNumberFormat;
	private Locale locale=Locale.getDefault();
	public WriteExcelForOlaAgentReport() throws WriteException {
		System.out.println("3dfd");
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

	public void writeAgentExcelRetailerWise(
			List<OlaAgentReportBean> reportDetail, WritableWorkbook workbk)
			throws IOException, WriteException, ParseException {
		System.out.println("4dfvd");
		workbk.createSheet("report", 0);
		WritableSheet excelSheet = workbk.getSheet(0);
		// creating header
		createHeaderForRetailer(excelSheet);
		// create Caption
		addCaption(excelSheet, 1, 4, findDefaultText("label.retailer.detail", locale));
		excelSheet.mergeCells(1, 4, 6, 4);
		addCaption(excelSheet, 1, 6, findDefaultText("label.ret.name", locale));
		addCaption(excelSheet, 2, 6, findDefaultText("label.plr.net.gaming", locale));
		addCaption(excelSheet, 3, 6, findDefaultText("label.total.commission", locale));
		addCaption(excelSheet, 4, 6, findDefaultText("label.done.commission", locale));
		excelSheet.mergeCells(4, 6, 4, 6);
		addCaption(excelSheet, 5, 6, findDefaultText("label.total.pending.commission", locale));
		int i = 0;
		for (OlaAgentReportBean bean : reportDetail) {

			i++;
			if (reportDetail != null) {
				String playerName = bean.getPlayerName();
				System.out.println(playerName);
				if (playerName.equalsIgnoreCase("Total")) {
					excelSheet.addCell(new Label(1, 8+i, bean.getPlayerName(),times));
					//addDoubleNumber(excelSheet, 2, 8 + i, bean.getPlayerName());
					addDoubleNumber(excelSheet, 2, 8 + i, bean
							.getPlayerNetGaming());
					addDoubleNumber(excelSheet, 3, 8 + i, bean
							.getCommissionCalculated());
					addDoubleNumber(excelSheet, 4, 8 + i, bean
							.getDoneCommission());
					addDoubleNumber(excelSheet, 5, 8 + i, bean
							.getPendingCommission());
				}

				else {
					excelSheet.addCell(new Label(1, 8+i, bean.getPlayerName(),times));
					//addDoubleNumber(excelSheet, 1, 8 + i, bean.getRetailerId());
					addDoubleNumber(excelSheet, 2, 8 + i, bean
							.getPlayerNetGaming());
					addDoubleNumber(excelSheet, 3, 8 + i, bean
							.getCommissionCalculated());
					addDoubleNumber(excelSheet, 4, 8 + i, bean
							.getDoneCommission());
					addDoubleNumber(excelSheet, 5, 8 + i, bean
							.getPendingCommission());
				}

			} else {
				addDoubleNumber(excelSheet, 1, 8 + i, 0.0);
				addDoubleNumber(excelSheet, 2, 8 + i, 0.0);
				addDoubleNumber(excelSheet, 3, 8 + i, 0.0);
				addDoubleNumber(excelSheet, 4, 8 + i, 0.0);
				addDoubleNumber(excelSheet, 5, 8 + i, 0.0);
			}

			/*
			 * int retailerId = bean.getRetailerId(); double totalComm =
			 * bean.getTotalCommissionCalculated(); double
			 * playerNetGaming=bean.getTotalPlayerNetGaming(); double doneComm=
			 * bean.getTotalDoneCommission(); double pendingComm =
			 * bean.getTotalPendingCommission();
			 * 
			 * excelSheet.addCell(new Number(1, length + 12, retailerId,
			 * headingNumberFormat)); excelSheet.addCell(new Number(2, length +
			 * 12, playerNetGaming, headingNumberFormat));
			 * excelSheet.addCell(new Number(3, length + 12, totalComm,
			 * headingNumberFormat)); excelSheet.addCell(new Number(4, length +
			 * 12, doneComm, headingNumberFormat)); excelSheet.addCell(new
			 * Number(5, length + 12, pendingComm, headingNumberFormat));
			 */

		}
		workbk.write();
		workbk.close();

	}

	private void createHeaderForRetailer(WritableSheet sheet)
			throws WriteException, ParseException {
		DateFormat format = new SimpleDateFormat("dd-MMM-yy");

		sheet.addCell(new Label(2, 0, " "+findDefaultText("label.date",locale)+"  :  " + format.format(new Date()),
				dateFormat));
		sheet.mergeCells(2, 0, 3, 0);

		setHeadingForAgentReport(sheet);
		sheet.mergeCells(0, 1, 3, 1);
		sheet.mergeCells(0, 2, 3, 2);

	}
	private void setHeadingForAgentReport(WritableSheet sheet)
			throws RowsExceededException, WriteException {
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
	
}
