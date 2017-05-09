package com.skilrock.lms.common.utility;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import com.skilrock.lms.common.Utility;
import com.skilrock.lms.dge.beans.ReportDrawBean;

public class PPRSheetHelper {
	private WritableWorkbook workBook;
	private WorkbookSettings wbSettings;
	private File fileObj = null;
	private String inputFile;

	public void setOutputFile(String inputFile) {
		this.inputFile = inputFile;
	}

	private void addInteger(WritableSheet sheet, WritableCellFormat cellFormat,
			int column, int row, Integer value) throws WriteException,
			RowsExceededException {
		Number number;
		number = new Number(column, row, value, cellFormat);
		sheet.addCell(number);
	}

	private void addDouble(WritableSheet sheet, WritableCellFormat cellFormat,
			int column, int row, Double value) throws WriteException,
			RowsExceededException {
		Number number;
		number = new Number(column, row, value, cellFormat);
		sheet.addCell(number);
	}

	private void addString(WritableSheet sheet, WritableCellFormat cellFormat,
			int column, int row, String s) throws WriteException,
			RowsExceededException {
		Label label;
		label = new Label(column, row, s, cellFormat);
		sheet.addCell(label);
	}

	private void fillExcelSheet(WritableSheet excelSheet,
			List<ReportDrawBean> mailPerformedDraw) {
		WritableCellFormat headerCellFormat = null, normalCellFormat = null;
		WritableFont headerCellFont = null, normalCellFont = null;
		int count = 0, rowCount = 1, columnCount = 0;

		try {
			headerCellFont = new WritableFont(WritableFont.TIMES, 11);
			headerCellFont.setBoldStyle(WritableFont.BOLD);
			headerCellFormat = new WritableCellFormat(headerCellFont);
			headerCellFormat.setAlignment(Alignment.CENTRE);
			headerCellFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
			headerCellFormat.setBorder(Border.ALL, BorderLineStyle.THICK);

			normalCellFont = new WritableFont(WritableFont.TIMES, 11);
			normalCellFormat = new WritableCellFormat(normalCellFont);
			normalCellFormat.setAlignment(Alignment.CENTRE);
			normalCellFormat.setVerticalAlignment(VerticalAlignment.CENTRE);

			excelSheet.mergeCells(0, 0, 5, 0);

			 addString(excelSheet, headerCellFormat, 0, 0,
			 Utility.getPropertyValue("COUNTRY_DEPLOYED"));

			// addString(excelSheet, headerCellFormat, 0, 0,
			// "Test");

			addString(excelSheet, headerCellFormat, 0, 1, "S.No");
			addString(excelSheet, headerCellFormat, 1, 1, "Dates");
			addString(excelSheet, headerCellFormat, 2, 1, "Draw Name");
			addString(excelSheet, headerCellFormat, 3, 1, "Total Sale");
			addString(excelSheet, headerCellFormat, 4, 1, "Total Winning");
			addString(excelSheet, headerCellFormat, 5, 1, "PPR Ratio");

			for (ReportDrawBean reportDrawBean : mailPerformedDraw) {
				count++;
				rowCount++;
				columnCount = 0;
				addInteger(excelSheet, normalCellFormat, columnCount++,
						rowCount, count);
				addString(excelSheet, normalCellFormat, columnCount++,
						rowCount, reportDrawBean.getDrawDateTime());
				addString(excelSheet, normalCellFormat, columnCount++,
						rowCount, reportDrawBean.getDrawName());
				addDouble(excelSheet, normalCellFormat, columnCount++,
						rowCount, reportDrawBean.getTotalSaleValue());
				addDouble(excelSheet, normalCellFormat, columnCount++,
						rowCount, reportDrawBean.getTotalWinningAmount());
				if (reportDrawBean.getTotalSaleValue() == 0.0) {
					addDouble(excelSheet, normalCellFormat, columnCount++,
							rowCount, 0.0);
				} else {
					addDouble(
							excelSheet,
							normalCellFormat,
							columnCount++,
							rowCount,
							((reportDrawBean.getTotalWinningAmount() / reportDrawBean
									.getTotalSaleValue()) * 100));
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public WritableSheet createExcelSheet(String sheetName, int sheetIndex) {
		WritableSheet excelSheet = null;
		try {
			workBook = Workbook.createWorkbook(fileObj, wbSettings);
			workBook.createSheet(sheetName, sheetIndex);
			excelSheet = workBook.getSheet(sheetIndex);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return excelSheet;
	}

	public WritableWorkbook createWorkBook() throws IOException, WriteException {
		WritableWorkbook workbook = null;
		try {
			fileObj = new File(inputFile);
			wbSettings = new WorkbookSettings();

			wbSettings.setLocale(new Locale("en", "EN"));

			workbook = Workbook.createWorkbook(fileObj, wbSettings);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return workbook;
	}

	public File preparePPRSheet(List<ReportDrawBean> mailPerformedDraw,
			String fileName) {
		WritableSheet excelSheet = null;
		setOutputFile(fileName);

		try {
			workBook = createWorkBook();
			excelSheet = createExcelSheet("PPR Sheet", 0);
			fillExcelSheet(excelSheet, mailPerformedDraw);
			workBook.write();
			workBook.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fileObj;
	}

	public static void main(String[] args) throws WriteException, IOException {
		// PPRSheetMailer test = new PPRSheetMailer();
		// test.setOutputFile("/home/stpl/TestExcel.xls");
		// // test.write();
		// System.out
		// .println("Please check the result file under /home/stpl/TestExcel.xls ");

		ReportDrawBean reportDrawBean = null;
		List<ReportDrawBean> mailPerformedDraw = null;

		mailPerformedDraw = new ArrayList<ReportDrawBean>();

		reportDrawBean = new ReportDrawBean();
		reportDrawBean.setDrawDateTime("2014-07-10 12:12:12");
		reportDrawBean.setDrawName("Draw Name");
		reportDrawBean.setTotalSaleValue(1212.12);
		reportDrawBean.setTotalWinningAmount(12.12);

		mailPerformedDraw.add(reportDrawBean);

		reportDrawBean = new ReportDrawBean();
		reportDrawBean.setDrawDateTime("2014-07-10 12:12:12");
		reportDrawBean.setDrawName("Draw Name");
		reportDrawBean.setTotalSaleValue(1212.12);
		reportDrawBean.setTotalWinningAmount(12.12);
		mailPerformedDraw.add(reportDrawBean);
		new PPRSheetHelper().preparePPRSheet(mailPerformedDraw, "Test.xls");
	}
}
