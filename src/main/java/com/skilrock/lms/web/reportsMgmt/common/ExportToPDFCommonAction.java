package com.skilrock.lms.web.reportsMgmt.common;

import java.io.ByteArrayOutputStream;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;
import com.skilrock.lms.common.BaseAction;
import com.skilrock.lms.common.Utility;

public class ExportToPDFCommonAction extends BaseAction {
	private static final long serialVersionUID = 1L;

	public ExportToPDFCommonAction() {
		super(ExportToPDFCommonAction.class);
	}
	
	private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
	private static Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.RED);
	private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD);
	private static Font fontFourteenBold = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.BOLD);
	private static Font fontFourteenu = new Font(Font.FontFamily.TIMES_ROMAN, 15, Font.UNDERLINE);
	private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD);
	private static Font verySmallNormal = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL);
	
	private float [] columnWidths = null; 

	private String exportData;
	private String otherData = null;
	private String startDate = null;
	private String endDate = null;
	private String curDate = null;
	private String curTime=null;
	private String mainHeader = null;
	private JsonObject excelDataObj = null;

	public String getExportData() {
		return exportData;
	}

	public void setExportData(String exportData) {
		this.exportData = exportData;
	}

	public void exportAsPDF() throws Exception {
		excelDataObj = new JsonParser().parse(exportData).getAsJsonObject();
		startDate = excelDataObj.get("fromDate").getAsString();
		endDate = excelDataObj.get("endDate").getAsString();
		curDate = excelDataObj.get("curDate").getAsString();
		curTime = excelDataObj.get("curTime").getAsString();
		mainHeader = excelDataObj.get("mainHeader").getAsString().trim();

		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/pdf");
		response.setHeader("Content-Disposition", "attachment; filename=" + mainHeader.replaceAll(" ", "_") + ".pdf");

		Document document = new Document(PageSize.A4.rotate());
		
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		
		PdfWriter.getInstance(document, stream);
		
		document.open();
		addContent(document);
		document.close();
		
		PdfReader reader = new PdfReader(stream.toByteArray());
        // Create a stamper
        PdfStamper stamper = new PdfStamper(reader, response.getOutputStream());
        // Loop over the pages and add a header to each page
        int n = reader.getNumberOfPages();
        for (int i = 1; i <= n; i++) {
            getHeaderTable(i, n).writeSelectedRows(0, -1, 34, 30, stamper.getOverContent(i));
        }
        // Close the stamper
        stamper.close();
        reader.close();
        
        logger.info("Exported to pdf successfully!");
	    
	}
	
	public PdfPTable getHeaderTable(int x, int y) {
        PdfPTable table = new PdfPTable(1);
        table.setTotalWidth(770);
        table.setLockedWidth(true);
        table.getDefaultCell().setFixedHeight(20);
        table.getDefaultCell().disableBorderSide(Rectangle.BOX);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(String.format(getText("label.page")+" %d "+getText("label.of")+" %d", x, y));
        return table;
    }
	
	private void addContent(Document document) throws DocumentException {
		Paragraph para = null;
		PdfPTable tbl = null;
		PdfPCell cell = null;
		
		//Add Header data...
		if ("BENIN".equals(Utility.getPropertyValue("COUNTRY_DEPLOYED"))) {
			para = new Paragraph(Utility.getPropertyValue("JSP_PAGE_TITLE"), subFont);
			para.setSpacingBefore(5);
			document.add(para);
			
			document.add(new Paragraph(getText("label.current.date")+":  "+curDate));
			document.add(new Paragraph(getText("label.current.time")+":          "+curTime));
		}  
		
		para = new Paragraph(mainHeader, subFont);
		para.setAlignment(Element.ALIGN_CENTER);
		para.setSpacingBefore(5);
		document.add(para);
		
		//Start date and end date from property file...
		if (!"null".equals(startDate) && !"null".equals(endDate)) {
			para = new Paragraph(getText("label.report.from.date")+":  "+startDate+"    "+getText("label.report.to.date")+":  "+endDate);
			para.setAlignment(Element.ALIGN_CENTER);
			document.add(para);
		}
		
		if ("BENIN".equals(Utility.getPropertyValue("COUNTRY_DEPLOYED"))) {
			document.add(new Phrase("N°_________/LNB/DG/DF"));
		}
		
		addEmptyLines(document, 1);

		//Add table
		createTable(document);
		
//		addEmptyLines(document, 1);
//		
//		//Add footer data here...
//		if ("BENIN".equals(Utility.getPropertyValue("COUNTRY_DEPLOYED"))) {
//			tbl = new PdfPTable(2);
//			tbl.setWidthPercentage(90);
//			tbl.setKeepTogether(true);
//			
//			cell = new PdfPCell(new Phrase("Cotonou, le"));
//			cell.setColspan(2);
//			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//			cell.setPaddingRight(100);
//			cell.disableBorderSide(Rectangle.BOX);
//			tbl.addCell(cell);
//			
//			cell = new PdfPCell(new Phrase(" "));
//			cell.setColspan(2);
//			cell.disableBorderSide(Rectangle.BOX);
//			tbl.addCell(cell);
//			
//			cell = new PdfPCell(new Phrase("Le Directeur des produits de pari"));
//			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
//			cell.disableBorderSide(Rectangle.BOX);
//			tbl.addCell(cell);
//			cell = new PdfPCell(new Phrase("Le Directeur financier"));
//			cell.disableBorderSide(Rectangle.BOX);
//			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//			cell.setPaddingRight(50);
//			tbl.addCell(cell);
//			
//			for (int i = 0; i < 3; i++) {
//				cell = new PdfPCell(new Phrase(" "));
//				cell.setColspan(2);
//				cell.disableBorderSide(Rectangle.BOX);
//				tbl.addCell(cell);
//			}
//			
//			cell = new PdfPCell(new Phrase("Saïbou KARIMOU", fontFourteenu));
//			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
//			cell.disableBorderSide(Rectangle.BOX);
//			tbl.addCell(cell);
//			cell = new PdfPCell(new Phrase("Raphaël J-L. NOUGBODE", fontFourteenu));
//			cell.disableBorderSide(Rectangle.BOX);
//			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//			tbl.addCell(cell);
//			
//			System.out.println("Rows:" +tbl.calculateHeights(false));
//			
//			document.add(tbl);
//		}
		
	}

	private void createTable(Document subCatPart) throws BadElementException, DocumentException {
		JsonArray headerArr = excelDataObj.get("headerData").getAsJsonArray();
		JsonArray rowArr = excelDataObj.get("rowData").getAsJsonArray();
		int noOfFooterRow = excelDataObj.get("noOfFootRow").getAsInt();
		int noOfColumns = excelDataObj.get("noOfColumns").getAsInt();
		PdfPTable table = null;
		
		columnWidths = new float[noOfColumns];
		for (int i = 0; i < noOfColumns; i++) {
			if (i > 0)
				columnWidths[i] = 1f;
			else 
				columnWidths[i] = 1.5f;
		}

		table = new PdfPTable(noOfColumns);
		table.setWidthPercentage(100);
		table.setWidths(columnWidths);
		
		PdfPCell cell = null;

		for (int i = 0; i < headerArr.size(); i++) {
			JsonArray cellArray = headerArr.get(i).getAsJsonArray();
			for (int j = 0; j < noOfColumns; j++) {
				JsonObject cellObj = cellArray.get(j).getAsJsonObject();
				cell = new PdfPCell(new Phrase(cellObj.get("data").getAsString(), smallBold));
				addPaddingAll(cell, 2);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(cell);
			}
		}

		table.setHeaderRows(1);
		
		if (rowArr.size() <= 2) {
			for (int i = 0; i < rowArr.size(); i++) {
				JsonArray cellArray = rowArr.get(i).getAsJsonArray();

				for (int j = 1; j < noOfColumns + 1; j++) {
					JsonObject cellObj = cellArray.get(j - 1).getAsJsonObject();
					cell = new PdfPCell(new Phrase(cellObj.get("data").getAsString(),verySmallNormal));
					
					if (cellObj.get("dataType").getAsString().trim().equals("amount-format") && !cellObj.get("data").getAsString().equals(""))
						cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					
					addPaddingAll(cell, 2);
					
					table.addCell(cell);
				}
			}
		} else {
			for (int i = 0; i < rowArr.size()-2; i++) {
				JsonArray cellArray = rowArr.get(i).getAsJsonArray();

				for (int j = 1; j < noOfColumns + 1; j++) {
					JsonObject cellObj = cellArray.get(j - 1).getAsJsonObject();
					cell = new PdfPCell(new Phrase(cellObj.get("data").getAsString(),verySmallNormal));
					
					if (cellObj.get("dataType").getAsString().trim().equals("amount-format") && !cellObj.get("data").getAsString().equals(""))
						cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					
					addPaddingAll(cell, 2);
					
					table.addCell(cell);
				}
			}
			
			subCatPart.add(table);
			
//			Code added for footer data together...
			table = new PdfPTable(noOfColumns);
			table.setWidthPercentage(100);
			table.setWidths(columnWidths);
			table.setKeepTogether(true);
			
			for (int i = 0; i < headerArr.size(); i++) {
				JsonArray cellArray = headerArr.get(i).getAsJsonArray();
				for (int j = 0; j < noOfColumns; j++) {
					JsonObject cellObj = cellArray.get(j).getAsJsonObject();
					cell = new PdfPCell(new Phrase(cellObj.get("data").getAsString(), smallBold));
					addPaddingAll(cell, 2);
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					table.addCell(cell);
				}
			}
			table.setHeaderRows(1);
            table.setSkipFirstHeader(true);

			for (int i = rowArr.size()-2; i < rowArr.size(); i++) {
				JsonArray cellArray = rowArr.get(i).getAsJsonArray();

				for (int j = 1; j < noOfColumns + 1; j++) {
					JsonObject cellObj = cellArray.get(j - 1).getAsJsonObject();
					cell = new PdfPCell(new Phrase(cellObj.get("data").getAsString(),verySmallNormal));
					
					if (cellObj.get("dataType").getAsString().trim().equals("amount-format") && !cellObj.get("data").getAsString().equals(""))
						cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					
					addPaddingAll(cell, 2);
					
					table.addCell(cell);
				}
			}
		}
		
		if (noOfFooterRow > 0) {
			JsonArray footerArr = excelDataObj.get("footerData").getAsJsonArray();

			for (int i = 0; i < footerArr.size(); i++) {
				JsonArray cellArray = footerArr.get(i).getAsJsonArray();
				for (int j = 0; j < noOfColumns; j++) {
					JsonObject cellObj = cellArray.get(j).getAsJsonObject();
					cell = new PdfPCell(new Phrase(cellObj.get("data").getAsString(), smallBold));
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					addPaddingAll(cell, 2);
					table.addCell(cell);
				}
			}
		}
		
		//Add footer data here...
		if ("BENIN".equals(Utility.getPropertyValue("COUNTRY_DEPLOYED"))) {
			cell = new PdfPCell(new Phrase(" "));
			cell.setColspan(noOfColumns);
			cell.disableBorderSide(Rectangle.BOX);
			table.addCell(cell);
			
			cell = new PdfPCell(new Phrase("Cotonou, le"));
			cell.setColspan(noOfColumns);
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			cell.setPaddingRight(100);
			cell.disableBorderSide(Rectangle.BOX);
			table.addCell(cell);
			
			cell = new PdfPCell(new Phrase(" "));
			cell.setColspan(noOfColumns);
			cell.disableBorderSide(Rectangle.BOX);
			table.addCell(cell);
			
			int colspanLeft = 0, colspanRight = 0;
			if (noOfColumns %2 == 0) {
				colspanLeft = colspanRight = noOfColumns/2;
			} else {
				colspanLeft = (noOfColumns + 1)/2;
				colspanRight = colspanLeft -1;
			}
			cell = new PdfPCell(new Phrase("Le Directeur des produits de pari"));
			cell.setColspan(colspanLeft);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.disableBorderSide(Rectangle.BOX);
			table.addCell(cell);
			cell = new PdfPCell(new Phrase("Le Directeur financier"));
			cell.setColspan(colspanRight);
			cell.disableBorderSide(Rectangle.BOX);
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			cell.setPaddingRight(50);
			table.addCell(cell);
			
			for (int i = 0; i < 3; i++) {
				cell = new PdfPCell(new Phrase(" "));
				cell.setColspan(noOfColumns);
				cell.disableBorderSide(Rectangle.BOX);
				table.addCell(cell);
			}
			
			cell = new PdfPCell(new Phrase("Saïbou KARIMOU", fontFourteenu));
			cell.setColspan(colspanLeft);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.disableBorderSide(Rectangle.BOX);
			table.addCell(cell);
			cell = new PdfPCell(new Phrase("Raphaël J-L. NOUGBODE", fontFourteenu));
			cell.setColspan(colspanRight);
			cell.disableBorderSide(Rectangle.BOX);
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			table.addCell(cell);
			
		}
		
		subCatPart.add(table);
		
	}
	
	private void addPaddingAll(PdfPCell cell, int size){
		cell.setPaddingTop(size);
		cell.setPaddingRight(2);
		cell.setPaddingBottom(2);
		cell.setPaddingLeft(2);
	}

	private static void addEmptyLines(Document doc, int numLine) throws DocumentException {
		for (int i = 0; i < numLine; i++) {
			doc.add(new Paragraph(" "));
		}
	}
	
}
