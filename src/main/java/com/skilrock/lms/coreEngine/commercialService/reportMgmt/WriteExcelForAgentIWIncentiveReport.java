package com.skilrock.lms.coreEngine.commercialService.reportMgmt;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
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

import com.opensymphony.xwork2.util.LocalizedTextUtil;
import com.skilrock.lms.beans.IWUserIncentiveBean;
public class WriteExcelForAgentIWIncentiveReport extends LocalizedTextUtil{

		private WritableCellFormat dateFormat;

		private WritableCellFormat headerDateFormat;
		private WritableCellFormat headerFormat;
		private WritableCellFormat headingLabel;
		private WritableCellFormat headingNumberFormat;
		Log logger = LogFactory.getLog(WriteExcelForAgentIWIncentiveReport.class);
		private WritableCellFormat numberFormat;
		private String reportType;
		private WritableCellFormat times;
		private WritableCellFormat timesBoldUnderline;
		private Locale locale=Locale.getDefault();
		
		public WriteExcelForAgentIWIncentiveReport() throws WriteException {

			numberFormat = new WritableCellFormat(NumberFormats.DEFAULT);
			numberFormat.setFont(new WritableFont(WritableFont.TIMES, 10));
			numberFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
			numberFormat.setWrap(false);

			times = new WritableCellFormat(new WritableFont(WritableFont.TIMES, 10));
			times.setWrap(false);
			times.setBorder(Border.ALL, BorderLineStyle.THIN);

			timesBoldUnderline = new WritableCellFormat(new WritableFont(WritableFont.TIMES, 10, WritableFont.BOLD, false,UnderlineStyle.NO_UNDERLINE));
			timesBoldUnderline.setWrap(false);
			timesBoldUnderline.setAlignment(Alignment.CENTRE);
			timesBoldUnderline.setBorder(Border.ALL, BorderLineStyle.MEDIUM);
			timesBoldUnderline.setBackground(Colour.GREY_25_PERCENT);
			timesBoldUnderline.setShrinkToFit(true);

			dateFormat = new WritableCellFormat(DateFormats.FORMAT2);
			dateFormat.setFont(new WritableFont(WritableFont.TIMES, 10, WritableFont.BOLD));
			dateFormat.setWrap(false);
			dateFormat.setAlignment(Alignment.RIGHT);

			headerFormat = new WritableCellFormat(new WritableFont(WritableFont.TIMES, 12,WritableFont.BOLD));
			headerFormat.setWrap(false);
			headerFormat.setAlignment(Alignment.CENTRE);

			headerDateFormat = new WritableCellFormat(DateFormats.FORMAT4);
			headerDateFormat.setFont(new WritableFont(WritableFont.TIMES, 10, WritableFont.BOLD));
			headerDateFormat.setWrap(false);
			headerDateFormat.setAlignment(Alignment.LEFT);

			headingLabel = new WritableCellFormat(new WritableFont(WritableFont.TIMES, 10));
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

		private void createContent(WritableSheet sheet,HashMap<Integer, IWUserIncentiveBean> agentMap, String startDate, String endDate) throws WriteException,
				RowsExceededException, ParseException {
			String reportHeader = "From "+startDate+" to "+endDate;
			sheet.addCell(new Label(2, 1, reportHeader, headerFormat));
			sheet.mergeCells(2, 1, 8, 1);
			
			addCaption(sheet, 1, 3, "S.No",25);
			addCaption(sheet, 2, 3, "Agent Name",25);
			addCaption(sheet, 3, 3, "Sale Amount", 25);
			addCaption(sheet, 4, 3, "Winning Amount", 25);
			addCaption(sheet, 5, 3, "Non-Winning Amount", 25);
			addCaption(sheet, 6, 3, "Incentive Amount", 25);
			int i = 1;
			double totalSale = 0.0;
			double totalWin = 0.0;
			double totalNonWinAmount = 0.0;
			double totalIncentiveAmt = 0.0;
			for(Map.Entry<Integer, IWUserIncentiveBean> userKey : agentMap.entrySet()){
				addLabel(sheet, 1, i + 3, i);
				addLabel(sheet, 2, i + 3, userKey.getValue().getOrganizationName());
				addNumber(sheet, 3, i + 3, userKey.getValue().getSale());
				addNumber(sheet, 4, i + 3, userKey.getValue().getWinning());
				addNumber(sheet, 5, i + 3, userKey.getValue().getSale() - userKey.getValue().getWinning());
				addNumber(sheet, 6, i + 3, userKey.getValue().getIncentiveAmount());
				totalSale += userKey.getValue().getSale();
				totalWin += userKey.getValue().getWinning();
				totalNonWinAmount += (userKey.getValue().getSale() - userKey.getValue().getWinning());
				totalIncentiveAmt += userKey.getValue().getIncentiveAmount();
				i++;
			}
			addCaption(sheet, 1, agentMap.size() + 4, "Total", 10);
			addCaption(sheet, 2, agentMap.size() + 4, "", 10);
			sheet.addCell(new Number(3, agentMap.size() + 4, totalSale, numberFormat));
			sheet.addCell(new Number(4, agentMap.size() + 4, totalWin, numberFormat));
			sheet.addCell(new Number(5, agentMap.size() + 4, totalNonWinAmount, numberFormat));
			sheet.addCell(new Number(6, agentMap.size() + 4, totalIncentiveAmt, numberFormat));
		}

		public void write(HashMap<Integer, IWUserIncentiveBean> agentMap, String startDate, String endDate, WritableWorkbook workbk) throws IOException,WriteException, ParseException {
			WorkbookSettings wbSettings = new WorkbookSettings();
			wbSettings.setLocale(new Locale("en", "EN"));
			WritableWorkbook workbook = workbk;
			workbook.createSheet("Incentive Report", 0);
			WritableSheet excelSheet = workbook.getSheet(0);
			int i=1;
			createContent(excelSheet, agentMap, startDate, endDate);
			workbook.write();
			workbook.close();

		}

	}
