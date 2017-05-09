package com.skilrock.lms.web.reportsMgmt.common;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.skilrock.lms.common.BaseAction;
import com.skilrock.lms.common.Utility;

public class ExportToExcelCommonAction extends BaseAction {
	private static final long serialVersionUID = 1L;

	public ExportToExcelCommonAction() {
		super(ExportToExcelCommonAction.class.getName());
	}

	private String exportData;
	private CellStyle cs = null;
	private CellStyle csHeader = null;
	private CellStyle csBold = null;
	private CellStyle csCaption = null;
	private CellStyle csTop = null;
	private CellStyle csRight = null;
	private CellStyle csBottom = null;
	private CellStyle csLeft = null;
	private CellStyle csTopLeft = null;
	private CellStyle csTopRight = null;
	private CellStyle csBottomLeft = null;
	private CellStyle csBottomRight = null;
	private CellStyle csAmountFormat = null;
	private String otherData = null;
	private String startDate = null;
	private String endDate = null;
	private String curDate=null;
	private String curTime=null;
	private String mainHeader = null;
	private String serviceName = null;

	public String getExportData() {
		return exportData;
	}

	public void setExportData(String exportData) {
		this.exportData = exportData;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public void exportToExcel() throws IOException {
		JsonObject excelDataObj = new JsonParser().parse(exportData).getAsJsonObject();
		JsonArray headerArr = excelDataObj.get("headerData").getAsJsonArray();
		JsonArray rowArr = excelDataObj.get("rowData").getAsJsonArray();
		int noOfColumns = excelDataObj.get("noOfColumns").getAsInt();
		int noOfFooterRow = excelDataObj.get("noOfFootRow").getAsInt();
		otherData = excelDataObj.get("otherData").getAsString();
		startDate = excelDataObj.get("fromDate").getAsString();
		endDate = excelDataObj.get("endDate").getAsString();
		curDate = excelDataObj.get("curDate").getAsString();
		curTime = excelDataObj.get("curTime").getAsString();
		mainHeader = excelDataObj.get("mainHeader").getAsString().trim();
		
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition", "attachment; filename="
				+ mainHeader.replaceAll(" ", "_") + ".xls");

		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet(mainHeader);
		setCellStyles(workbook);
		// Set Header Information
		
//		Add Header rows for Benin
		
		int headerRowIndex = 0;
		Row row = null;
		Cell c = null;

		row = (Row) sheet.createRow(headerRowIndex);
		c = row.createCell(0);
	//	sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, noOfColumns - 1));
		c.setCellValue(Utility.getPropertyValue("JSP_PAGE_TITLE"));
		c.setCellStyle(csHeader);
		
		headerRowIndex++;
		
		row = (Row) sheet.createRow(headerRowIndex);
		c = row.createCell(0);
		c.setCellValue(getText("label.current.date"));
		c.setCellStyle(csLeft);

		c = row.createCell(1);
		c.setCellValue(curDate);
		c.setCellStyle(csRight);
		
		headerRowIndex++;
		
		row = (Row) sheet.createRow(headerRowIndex);
		c = row.createCell(0);
		c.setCellValue(getText("label.current.time"));
		c.setCellStyle(csLeft);

		c = row.createCell(1);
		c.setCellValue(curTime);
		c.setCellStyle(csRight);
		
		headerRowIndex++;
		
		row = (Row) sheet.createRow(headerRowIndex);
		c = row.createCell(0);
		//sheet.addMergedRegion(new CellRangeAddress(headerRowIndex, headerRowIndex, 0, noOfColumns - 1));
		c.setCellValue(mainHeader);
		c.setCellStyle(csHeader);
		
		insertHeaderInfo(sheet, headerRowIndex-1);
		
		if (!"null".equals(startDate) && !"null".equals(endDate))
			headerRowIndex += 4;
		else 
			headerRowIndex += 2;

		if ("BENIN".equalsIgnoreCase(Utility
				.getPropertyValue("COUNTRY_DEPLOYED"))) {
			row = (Row) sheet.createRow(headerRowIndex);
			c = row.createCell(0);
			c.setCellValue("N°_________/LNB/DG/DF");
			c.setCellStyle(csLeft);
		}
		
		if ("GHANA".equalsIgnoreCase(Utility
				.getPropertyValue("COUNTRY_DEPLOYED"))) {
			row = (Row) sheet.createRow(headerRowIndex);
			c = row.createCell(0);
			c.setCellValue(serviceName);
			c.setCellStyle(csLeft);
		}

		int topSpace = headerRowIndex-1 + 3;
		int leftSpace = 0;
		int headerSize = headerArr.size() + topSpace;
		for (int i = 0; i < headerArr.size(); i++) {
			JsonArray cellArray = headerArr.get(i).getAsJsonArray();
			Row header = (Row) sheet.createRow(i + topSpace);
			for (int j = 0; j < noOfColumns; j++) {
				JsonObject cellObj = cellArray.get(j).getAsJsonObject();
				c = header.createCell(j + leftSpace);
				c.setCellValue(cellObj.get("data").getAsString());
				if (cellObj.get("isMergedCell").getAsString().equals("YES")) {
					String[] rangeArr = cellObj.get("mergeRange").getAsString()
							.split(",");
					/*sheet.addMergedRegion(new CellRangeAddress(Integer
							.parseInt(rangeArr[0])
							+ topSpace, Integer.parseInt(rangeArr[1])
							+ topSpace, Integer.parseInt(rangeArr[2])
							+ leftSpace, Integer.parseInt(rangeArr[3])
							+ leftSpace));*/
				}

				c.setCellStyle(csCaption);
			}
		}

		for (int i = headerSize; i < rowArr.size() + headerSize; i++) {
			JsonArray cellArray = rowArr.get(i - headerSize).getAsJsonArray();

			Row dataRow = (Row) sheet.createRow(i);
			for (int j = 1; j < noOfColumns + 1; j++) {
				JsonObject cellObj = cellArray.get(j - 1).getAsJsonObject();
				c = dataRow.createCell(j - 1 + leftSpace);

				if (cellObj.get("dataType").getAsString().trim().equals(
						"amount-format")
						&& !cellObj.get("data").getAsString().equals("")) {
					c.setCellValue(new BigDecimal(cellObj.get("data")
							.getAsString().replaceAll(",", "")).doubleValue());
					c.setCellStyle(csAmountFormat);

				} else {
					c.setCellValue(cellObj.get("data").getAsString());
					c.setCellStyle(cs);
				}
			}
		}

		if (noOfFooterRow > 0) {
			JsonArray footerArr = excelDataObj.get("footerData")
					.getAsJsonArray();
			topSpace = headerSize + rowArr.size();

			for (int i = headerSize + rowArr.size(); i < footerArr.size()
					+ rowArr.size() + headerSize; i++) {
				JsonArray cellArray = footerArr.get(
						i - headerSize - rowArr.size()).getAsJsonArray();
				Row header = (Row) sheet.createRow(i);
				for (int j = 0; j < noOfColumns; j++) {
					JsonObject cellObj = cellArray.get(j).getAsJsonObject();
					c = header.createCell(j + leftSpace);
					c.setCellValue(cellObj.get("data").getAsString());
					if (cellObj.get("isMergedCell").getAsString().equals("YES")) {
						String[] rangeArr = cellObj.get("mergeRange")
								.getAsString().split(",");
						/*sheet.addMergedRegion(new CellRangeAddress(Integer
								.parseInt(rangeArr[0])
								+ topSpace, Integer.parseInt(rangeArr[1])
								+ topSpace, Integer.parseInt(rangeArr[2])
								+ leftSpace, Integer.parseInt(rangeArr[3])
								+ leftSpace));*/
					}

					c.setCellStyle(csCaption);
				}
			}
		}
		
//		Add Footer rows for Benin
		if ("BENIN".equalsIgnoreCase(Utility
				.getPropertyValue("COUNTRY_DEPLOYED"))) {
			int footerIndex = headerSize + rowArr.size() + noOfFooterRow;
			footerIndex++;

			row = (Row) sheet.createRow(footerIndex);
			c = row.createCell(noOfColumns - 2);
			c.setCellValue("Cotonou, le");

			footerIndex += 2;

			row = (Row) sheet.createRow(footerIndex);
			c = row.createCell(0);
			c.setCellValue("Le Directeur des produits de pari");

			c = row.createCell(noOfColumns - 2);
			c.setCellValue("Le Directeur financier");

			footerIndex += 4;

			row = (Row) sheet.createRow(footerIndex);
			c = row.createCell(0);
			c.setCellValue("Saïbou KARIMOU");
			c.setCellStyle(csBold);

			c = row.createCell(noOfColumns - 2);
			c.setCellValue("Raphaël J-L. NOUGBODE");
			c.setCellStyle(csBold);
		}
		// Auto size the column widths
		for (int columnIndex = 0; columnIndex < noOfColumns + leftSpace; columnIndex++) {
			sheet.autoSizeColumn(columnIndex);
		}

		try {
			workbook.write(response.getOutputStream());
			logger.info("Excel written successfully..");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private int insertHeaderInfo(HSSFSheet sheet, int index) {
		int rowIndex = index;
		Row row = null;
		Cell c = null;

		rowIndex++;

		if (!"".equals(otherData.trim())) {
			rowIndex++;
			row = (Row) sheet.createRow(rowIndex);
			c = row.createCell(0);
			c.setCellValue("");
			// c.setCellStyle(csTopLeft);

			c = row.createCell(1);
			c.setCellValue(otherData);
			// c.setCellStyle(csTopRight);
		}

		if (!"null".equals(startDate.trim())) {
			rowIndex++;
			row = (Row) sheet.createRow(rowIndex);
			c = row.createCell(0);
//			c.setCellValue(getText("label.start.date"));
			c.setCellValue(getText("label.report.from.date"));
			c.setCellStyle(csLeft);

			c = row.createCell(1);
			c.setCellValue(startDate);
			c.setCellStyle(csRight);
		}

		if (!"null".equals(endDate.trim())) {
			rowIndex++;
			row = (Row) sheet.createRow(rowIndex);
			c = row.createCell(0);
//			c.setCellValue(getText("label.end.date"));
			c.setCellValue(getText("label.report.to.date"));
			c.setCellStyle(csBottomLeft);

			c = row.createCell(1);
			c.setCellValue(endDate);
			c.setCellStyle(csBottomRight);
		}
//		if(!"null".equals(curDate.trim())){
//			rowIndex++;
//			row = sheet.createRow(rowIndex);
//			c = row.createCell(0);
//			c.setCellValue(getText("label.date"));
//			c.setCellStyle(csBottomLeft);
//
//			c = row.createCell(1);
//			c.setCellValue(curDate);
//			c.setCellStyle(csBottomRight);
//		}

		return rowIndex;

	}

	private void setCellStyles(HSSFWorkbook wb) {

		// font size 10
		Font f = (Font) wb.createFont();
		f.setFontHeightInPoints((short) 10);

		// Simple style
		cs = (CellStyle) wb.createCellStyle();
		cs.setFont(f);
		cs.setBorderBottom(CellStyle.BORDER_THIN);
		cs.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		cs.setBorderRight(CellStyle.BORDER_THIN);
		cs.setRightBorderColor(IndexedColors.BLACK.getIndex());
		cs.setBorderTop(CellStyle.BORDER_THIN);
		cs.setTopBorderColor(IndexedColors.BLACK.getIndex());
		cs.setBorderLeft(CellStyle.BORDER_THIN);
		cs.setLeftBorderColor(IndexedColors.BLACK.getIndex());

		// Bold Fond
		Font bold = (Font) wb.createFont();
		bold.setBoldweight(Font.BOLDWEIGHT_BOLD);
		bold.setFontHeightInPoints((short) 10);

		// Header style
		csHeader = (CellStyle) wb.createCellStyle();
		csHeader.setAlignment(CellStyle.ALIGN_CENTER);
		csHeader.setFont(bold);
		csHeader.setBorderBottom(CellStyle.BORDER_THIN);
		csHeader.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		csHeader.setBorderRight(CellStyle.BORDER_THIN);
		csHeader.setRightBorderColor(IndexedColors.BLACK.getIndex());
		csHeader.setBorderTop(CellStyle.BORDER_THIN);
		csHeader.setTopBorderColor(IndexedColors.BLACK.getIndex());
		csHeader.setBorderLeft(CellStyle.BORDER_THIN);
		csHeader.setLeftBorderColor(IndexedColors.BLACK.getIndex());

		// Header Style
		csCaption = (CellStyle) wb.createCellStyle();
		csCaption.setFillForegroundColor(IndexedColors.GREY_25_PERCENT
				.getIndex());
		csCaption.setFillPattern(CellStyle.SOLID_FOREGROUND);
		csCaption.setAlignment(CellStyle.ALIGN_CENTER);
		csCaption.setVerticalAlignment(CellStyle.ALIGN_CENTER);
		csCaption.setFont(bold);
		csCaption.setBorderBottom(CellStyle.BORDER_THIN);
		csCaption.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		csCaption.setBorderRight(CellStyle.BORDER_THIN);
		csCaption.setRightBorderColor(IndexedColors.BLACK.getIndex());
		csCaption.setBorderTop(CellStyle.BORDER_THIN);
		csCaption.setTopBorderColor(IndexedColors.BLACK.getIndex());
		csCaption.setBorderLeft(CellStyle.BORDER_THIN);
		csCaption.setLeftBorderColor(IndexedColors.BLACK.getIndex());

		// Bold style
		csBold = (CellStyle) wb.createCellStyle();
		csBold.setBorderBottom(CellStyle.BORDER_THIN);
		csBold.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		csBold.setFont(bold);

		// Setup style for Top Border Line
		csTop = (CellStyle) wb.createCellStyle();
		csTop.setBorderTop(CellStyle.BORDER_THIN);
		csTop.setTopBorderColor(IndexedColors.BLACK.getIndex());
		csTop.setFont(f);

		// Setup style for Right Border Line
		csRight = (CellStyle) wb.createCellStyle();
		csRight.setBorderRight(CellStyle.BORDER_THIN);
		csRight.setRightBorderColor(IndexedColors.BLACK.getIndex());
		csRight.setFont(f);

		// Setup style for Bottom Border Line
		csBottom = (CellStyle) wb.createCellStyle();
		csBottom.setBorderBottom(CellStyle.BORDER_THIN);
		csBottom.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		csBottom.setFont(f);

		// Setup style for Left Border Line
		csLeft = (CellStyle) wb.createCellStyle();
		csLeft.setBorderLeft(CellStyle.BORDER_THIN);
		csLeft.setLeftBorderColor(IndexedColors.BLACK.getIndex());
		csLeft.setFont(f);

		// Setup style for Top/Left corner cell Border Lines
		csTopLeft = (CellStyle) wb.createCellStyle();
		csTopLeft.setBorderTop(CellStyle.BORDER_THIN);
		csTopLeft.setTopBorderColor(IndexedColors.BLACK.getIndex());
		csTopLeft.setBorderLeft(CellStyle.BORDER_THIN);
		csTopLeft.setLeftBorderColor(IndexedColors.BLACK.getIndex());
		csTopLeft.setFont(f);

		// Setup style for Top/Right corner cell Border Lines
		csTopRight = (CellStyle) wb.createCellStyle();
		csTopRight.setBorderTop(CellStyle.BORDER_THIN);
		csTopRight.setTopBorderColor(IndexedColors.BLACK.getIndex());
		csTopRight.setBorderRight(CellStyle.BORDER_THIN);
		csTopRight.setRightBorderColor(IndexedColors.BLACK.getIndex());
		csTopRight.setFont(f);

		// Setup style for Bottom/Left corner cell Border Lines
		csBottomLeft = (CellStyle) wb.createCellStyle();
		csBottomLeft.setBorderBottom(CellStyle.BORDER_THIN);
		csBottomLeft.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		csBottomLeft.setBorderLeft(CellStyle.BORDER_THIN);
		csBottomLeft.setLeftBorderColor(IndexedColors.BLACK.getIndex());
		csBottomLeft.setFont(f);

		// Setup style for Bottom/Right corner cell Border Lines
		csBottomRight = (CellStyle) wb.createCellStyle();
		csBottomRight.setBorderBottom(CellStyle.BORDER_THIN);
		csBottomRight.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		csBottomRight.setBorderRight(CellStyle.BORDER_THIN);
		csBottomRight.setRightBorderColor(IndexedColors.BLACK.getIndex());
		csBottomRight.setFont(f);

		csAmountFormat = (CellStyle) wb.createCellStyle();
		csAmountFormat.setAlignment(CellStyle.ALIGN_RIGHT);
		csAmountFormat.setBorderBottom(CellStyle.BORDER_THIN);
		csAmountFormat.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		csAmountFormat.setBorderRight(CellStyle.BORDER_THIN);
		csAmountFormat.setRightBorderColor(IndexedColors.BLACK.getIndex());
		csAmountFormat.setBorderTop(CellStyle.BORDER_THIN);
		csAmountFormat.setTopBorderColor(IndexedColors.BLACK.getIndex());
		csAmountFormat.setBorderLeft(CellStyle.BORDER_THIN);
		csAmountFormat.setLeftBorderColor(IndexedColors.BLACK.getIndex());
	}
}