package com.skilrock.lms.web.reportsMgmt.common;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.skilrock.lms.beans.SaleReportBean;

/**
 * This is used to hold export data for excel related details
 */
public class ExportDataExcel {

	HSSFCellStyle headerStyle = null;
	Log logger = LogFactory.getLog(ExportDataExcel.class);
	HSSFWorkbook workBook = new HSSFWorkbook();

	/**
	 * The method :generateExcel is used to generate excel
	 * 
	 * @param li
	 * @return HSSFWorkbook
	 * @throws FileNotFoundException
	 * @throws IOException
	 */

	public void exportExcel(HttpServletRequest request,
			HttpServletResponse response, File excelfile) {
		try {
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition",
					"attachment; filename=saleReport1.xls");

			// WritableWorkbook w =
			// Workbook.createWorkbook(response.getOutputStream());
			/*
			 * //WritableSheet s = w.createSheet("Demo", 0);
			 * //w.setOutputFile(); //WritableSheet s = w.setOutputFile(arg0);
			 * s.addCell(new Label(0, 0, "Hello World")); w.write(); w.close();
			 */
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public HSSFWorkbook generateExcel(List<SaleReportBean> li)
			throws FileNotFoundException, IOException {
		logger.debug("Inside generateExcel");
		// logger.debug("inside genrate excel");

		HSSFSheet sheetExport = workBook.createSheet("Product Allocations");
		sheetExport.setColumnWidth((short) 0, (short) 10000);
		sheetExport.setColumnWidth((short) 1, (short) 10000);
		sheetExport.setColumnWidth((short) 2, (short) 12000);
		sheetExport.setColumnWidth((short) 3, (short) 12000);
		// sheetExport.setColumnWidth((short) 4,(short) (5000));
		// sheetExport.setColumnWidth((short) 5,(short) (5000));

		HSSFRow headerRow;
		HSSFCell headerCell;

		headerRow = sheetExport.createRow(0);

		headerCell = headerRow.createCell((short) 0);
		headerCell.setCellValue("Game Name");
		headerCell.setCellStyle(styleBold());

		headerCell = headerRow.createCell((short) 1);
		headerCell.setCellValue("Agent Name");
		headerCell.setCellStyle(styleBold());

		headerCell = headerRow.createCell((short) 2);
		headerCell.setCellValue("Book Sold");
		headerCell.setCellStyle(styleBold());

		headerCell = headerRow.createCell((short) 3);
		headerCell.setCellValue("Sale Return");
		headerCell.setCellStyle(styleBold());

		/*
		 * headerCell = headerRow.createCell((short) 4);
		 * headerCell.setCellValue("User Name");
		 * headerCell.setCellStyle(styleBold());
		 * 
		 * headerCell = headerRow.createCell((short) 5);
		 * headerCell.setCellValue("First Name");
		 * headerCell.setCellStyle(styleBold());
		 * 
		 * headerCell = headerRow.createCell((short) 6);
		 * headerCell.setCellValue("Amount");
		 * headerCell.setCellStyle(styleBold());
		 */

		SaleReportBean accountsBoData = null;
		logger.debug("the size of list is--->>  " + li);
		logger.debug("the size of list is--->>  " + li);
		for (int i = 0; i < li.size(); i++) {
			accountsBoData = li.get(i);

			headerRow = sheetExport.createRow((i + 1));

			headerCell = headerRow.createCell((short) 0);
			headerCell.setCellValue(accountsBoData.getGamename());

			headerCell = headerRow.createCell((short) 1);
			headerCell.setCellValue(accountsBoData.getName());

			headerCell = headerRow.createCell((short) 2);
			headerCell.setCellValue(accountsBoData.getSale());

			headerCell = headerRow.createCell((short) 3);
			headerCell.setCellValue(accountsBoData.getSalereturn());

			/*
			 * headerCell = headerRow.createCell((short) 4);
			 * headerCell.setCellValue(accountsBoData.getUserName());
			 * 
			 * headerCell = headerRow.createCell((short) 5);
			 * headerCell.setCellValue(accountsBoData.getFirstName());
			 * 
			 * headerCell = headerRow.createCell((short) 6);
			 * headerCell.setCellValue(accountsBoData.getAmount());
			 */

		}
		logger.debug("Exit genrate Excel");
		logger.debug("Exit genrate Excel");

		return workBook;
	}

	/**
	 * The method :styleBold is used to style the Excel Cell
	 * 
	 * @return HSSFCellStyle
	 */
	public HSSFCellStyle styleBold() {
		headerStyle = workBook.createCellStyle();
		headerStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		HSSFFont font = workBook.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font.setFontHeightInPoints((short) 10);
		headerStyle.setFont(font);
		return headerStyle;
	}

}
