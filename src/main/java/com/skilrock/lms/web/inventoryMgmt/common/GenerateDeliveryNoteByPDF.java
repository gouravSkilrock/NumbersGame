package com.skilrock.lms.web.inventoryMgmt.common;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPCellEvent;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.opensymphony.xwork2.ActionSupport;

public class GenerateDeliveryNoteByPDF extends ActionSupport implements
		ServletRequestAware, PdfPCellEvent, ServletResponseAware {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	final Font B_FONT = new Font(Font.HELVETICA, 10, Font.BOLD);

	final Font H_FONT = new Font(Font.HELVETICA, 13, Font.BOLD);
	final Font HN_FONT = new Font(Font.HELVETICA, 9, Font.BOLD);
	final Font N_FONT = new Font(Font.HELVETICA, 9, Font.NORMAL);
	private HttpServletRequest request;
	private HttpServletResponse response;
	final Font THN_FONT = new Font(Font.HELVETICA, 14, Font.BOLD);
	final Font U_FONT = new Font(Font.HELVETICA, 11, Font.BOLD);

	public void cellLayout(PdfPCell cell, Rectangle position,
			PdfContentByte[] canvases) {
		float x1 = position.getLeft() + 2;
		float x2 = position.getRight() - 2;
		float y1 = position.getTop() - 2;
		float y2 = position.getBottom() + 2;
		PdfContentByte canvas = canvases[PdfPTable.LINECANVAS];
		// canvas.setRGBColorStroke(0xFF, 0x00, 0x00);
		canvas.rectangle(x1, y1, x2 - x1, y2 - y1);
		canvas.stroke();
		canvas.resetRGBColorStroke();
	}

	public String generatePDF() {
		byte[] arrayOutPut = new byte[0];
		try {
			// setting some response headers
			response.setHeader("Expires", "0");
			response.setHeader("Cache-Control",
					"must-revalidate, post-check=0, pre-check=0");
			response.setHeader("Pragma", "public");

			response.setContentType("application/pdf");
			OutputStream OutStrm = response.getOutputStream();

			// this method return the byte of pdf output streams
			String type = (String) request.getSession().getAttribute(
					"deliveryChallanType");
			// if("DSRCHALLAN".equalsIgnoreCase(type.trim())) {
			// arrayOutPut=genrateSaleReturnPDFTable();
			// }
			// else
			arrayOutPut = genratePDFTable();

			OutStrm.write(arrayOutPut);
			// System.out.println("in the last ................ ");
			OutStrm.flush();
			// System.out.println("before out put stream closed");
			OutStrm.close();
			// System.out.println("after out put stream closed");
		} catch (IOException e) {
			System.out.println(" exception occured in ");
		}
		if (arrayOutPut.length > 0) {
			return null;
		} else {
			return SUCCESS;
		}
	}

	private byte[] genratePDFTable() {

		System.out
				.println("inside PDF ledger Report 111111 ........................");

		HttpSession session = request.getSession();
		LinkedHashMap<String,Map<String,String>> reportBean = (LinkedHashMap<String,Map<String,String>>) session.getAttribute("detailsMap");
		Map headerInfoMap = (Map) session.getAttribute("staticReportMap");
		PdfWriter writer = null;
		Document document = new Document(PageSize.A4, 22, 15, 30, 60);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		try {
			writer = PdfWriter.getInstance(document, baos);
			document.open();

			PdfPTable reportTable = new PdfPTable(2);
			// reportTable.setSplitRows(false);
			int headerwidths[] = new int[] { 85, 15 };
			reportTable.setWidths(headerwidths);
			reportTable.setSplitLate(false);
			reportTable.setWidthPercentage(100);
			PdfPCell cell = null;

			int totalBookQty = 0;
			boolean flag = true;
			// static header added
			String type = (String) session.getAttribute("deliveryChallanType");
		/*	if ("DSRCHALLAN".equalsIgnoreCase(type.trim())) {
				cell = new PdfPCell(
						getStaticHeaderOfSaleRetReport(headerInfoMap));
				cell.setBorder(Rectangle.NO_BORDER);
				cell.setColspan(2);
				reportTable.addCell(cell);
				System.out.println("static header in if........... ");

				System.out.println(" bean of gmaes " + reportBean);

				totalBookQty = 0;
				flag = true;

				Set<Integer> returnSet = reportBean.keySet();
				for (Integer returnkey : returnSet) {
					System.out.println("transaction id = " + returnkey);
					Map KeysMap = (Map) reportBean.get(returnkey);
					Set<String> mapKey = (Set) KeysMap.keySet();
					for (String key : mapKey) {
						System.out.println(" gmae ===== " + key);
						InvoiceGameDetailBean bean = (InvoiceGameDetailBean) KeysMap
								.get(key);
						cell = new PdfPCell(new Phrase("Game Name : "
								+ bean.getGameName()
								+ "      Sale Commission Rate : "
								+ bean.getSalCommVar(), U_FONT));
						cell.setBorder(Rectangle.NO_BORDER);
						cell.setColspan(1);
						reportTable.addCell(cell);

						if (flag) {
							cell = new PdfPCell(new Phrase("QTY OF BOOKS",
									B_FONT));
							cell.setBorder(Rectangle.NO_BORDER);
							cell.setHorizontalAlignment(Element.ALIGN_CENTER);
							reportTable.addCell(cell);
							System.out
									.println(" inside qty of books ............");
							flag = false;
						} else {
							cell = new PdfPCell(new Phrase(" ", B_FONT));
							cell.setBorder(Rectangle.NO_BORDER);
							cell.setHorizontalAlignment(Element.ALIGN_CENTER);
							System.out
									.println(" not inside qty of books ............");
							reportTable.addCell(cell);
						}

						reportTable.addCell(getBlankCell(3));

						if (bean.getPackNbrList().size() > 0) {
							// pack details header columns
							cell = new PdfPCell(new Phrase("Pack No.", B_FONT));
							cell.setBorder(Rectangle.NO_BORDER);
							cell.setColspan(2);
							reportTable.addCell(cell);

							// pack details columns

							cell = new PdfPCell(getPackDetails(bean
									.getPackNbrList()));
							cell.setBorder(Rectangle.NO_BORDER);
							reportTable.addCell(cell);
							cell = new PdfPCell(new Phrase(" ", B_FONT));
							cell.setBorder(Rectangle.NO_BORDER);
							reportTable.addCell(cell);
							System.out.println(" packs detail ............"
									+ bean.getPackNbrList());

							int totalBooksPack = bean.getPackNbrList().size()
									* bean.getNbrBooks();
							totalBookQty = totalBookQty + totalBooksPack;

							cell = new PdfPCell(new Phrase(" ", B_FONT));
							cell.setBorder(Rectangle.NO_BORDER);
							reportTable.addCell(cell);
							cell = new PdfPCell(new Phrase(
									" " + totalBooksPack, B_FONT));
							cell.setBorder(Rectangle.BOX);
							/*
							 * GenerateDeliveryChallenByPDF event =new
							 * GenerateDeliveryChallenByPDF();
							 * cell.setCellEvent(event);
							 */
							/*cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
							cell.setHorizontalAlignment(Element.ALIGN_CENTER);
							reportTable.addCell(cell);

							// blank column
							cell = new PdfPCell(getBlankCell(3));
							cell.setBorder(Rectangle.NO_BORDER);
							cell.setColspan(2);
							reportTable.addCell(cell);

						}

						if (bean.getBookNbrList().size() > 0) {
							cell = new PdfPCell(new Phrase("Book No.", B_FONT));
							cell.setBorder(Rectangle.NO_BORDER);
							cell.setColspan(2);
							reportTable.addCell(cell);

							cell = new PdfPCell(getBookDetails(bean
									.getBookNbrList()));
							cell.setBorder(Rectangle.NO_BORDER);
							reportTable.addCell(cell);
							cell = new PdfPCell(new Phrase(" ", B_FONT));
							cell.setBorder(Rectangle.NO_BORDER);
							reportTable.addCell(cell);

							totalBookQty = totalBookQty
									+ bean.getBookNbrList().size();

							cell = new PdfPCell(new Phrase(" ", B_FONT));
							cell.setBorder(Rectangle.NO_BORDER);
							reportTable.addCell(cell);
							cell = new PdfPCell(new Phrase(" "
									+ bean.getBookNbrList().size(), B_FONT));
							cell.setBorder(Rectangle.BOX);
							// cell.setCellEvent(event);
							cell.setHorizontalAlignment(Element.ALIGN_CENTER);
							cell.setVerticalAlignment(Element.ALIGN_CENTER);
							reportTable.addCell(cell);
							System.out.println(" books detail ............"
									+ bean.getBookNbrList().size());

							cell = new PdfPCell(getBlankCell(14));
							cell.setBorder(Rectangle.BOTTOM);
							cell.setColspan(2);
							reportTable.addCell(cell);
						}
					}

				}
			} */ 
				cell = new PdfPCell(getStaticHeaderOfReport(headerInfoMap));
				cell.setBorder(Rectangle.NO_BORDER);
				cell.setColspan(2);
				reportTable.addCell(cell);
				System.out.println("static header in else........... ");

				// Iterable table header row
				System.out.println(" details of con and non cons " + reportBean);
				Set<String> mapKey = reportBean.get("Non Consumable").keySet();
				Set<String> conMapKey = reportBean.get("Consumable").keySet();
				
				
				
				
				
				
				
				
				
				
				

				for (String key : mapKey) {
					System.out.println(" gmae ===== " + key);
					//InvoiceGameDetailBean bean = (InvoiceGameDetailBean) reportBean
					//		.get(key);
					String []serialNo=reportBean.get("Non Consumable").get(key).split(",");
					cell = new PdfPCell(
							new Phrase("Non Consumable : " + key, U_FONT));
					cell.setBorder(Rectangle.NO_BORDER);
					cell.setColspan(1);
					reportTable.addCell(cell);

					if (flag) {
						cell = new PdfPCell(new Phrase("Count", B_FONT));
						cell.setBorder(Rectangle.NO_BORDER);
						cell.setHorizontalAlignment(Element.ALIGN_CENTER);
						reportTable.addCell(cell);
						System.out.println(" inside qty of items ............");
						flag = false;
					} else {
						cell = new PdfPCell(new Phrase(" ", B_FONT));
						cell.setBorder(Rectangle.NO_BORDER);
						cell.setHorizontalAlignment(Element.ALIGN_CENTER);
						System.out
								.println(" not inside qty of non consumable ............");
						reportTable.addCell(cell);
					}

					reportTable.addCell(getBlankCell(3));

					/*if (bean.getPackNbrList().size() > 0) {
						// pack details header columns
						cell = new PdfPCell(new Phrase("Pack No.", B_FONT));
						cell.setBorder(Rectangle.NO_BORDER);
						cell.setColspan(2);
						reportTable.addCell(cell);

						// pack details columns
						cell = new PdfPCell(getPackDetails(bean
								.getPackNbrList()));
						cell.setBorder(Rectangle.NO_BORDER);
						reportTable.addCell(cell);
						cell = new PdfPCell(new Phrase(" ", B_FONT));
						cell.setBorder(Rectangle.NO_BORDER);
						reportTable.addCell(cell);
						System.out.println(" packs detail ............"
								+ bean.getPackNbrList());
						int totalBooksPack = bean.getPackNbrList().size()
								* bean.getNbrBooks();
						totalBookQty = totalBookQty + totalBooksPack;

						cell = new PdfPCell(new Phrase(" ", B_FONT));
						cell.setBorder(Rectangle.NO_BORDER);
						reportTable.addCell(cell);
						cell = new PdfPCell(new Phrase(" " + totalBooksPack,
								B_FONT));
						cell.setBorder(Rectangle.BOX);
						/*
						 * GenerateDeliveryChallenByPDF event =new
						 * GenerateDeliveryChallenByPDF();
						 * cell.setCellEvent(event);
						 */
						/*cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
						cell.setHorizontalAlignment(Element.ALIGN_CENTER);
						reportTable.addCell(cell);

						// blank column
						cell = new PdfPCell(getBlankCell(3));
						cell.setBorder(Rectangle.NO_BORDER);
						cell.setColspan(2);
						reportTable.addCell(cell);
					}
*/
					if (serialNo.length > 0) {
						cell = new PdfPCell(new Phrase("Serial No.", B_FONT));
						cell.setBorder(Rectangle.NO_BORDER);
						cell.setColspan(2);
						reportTable.addCell(cell);

						cell = new PdfPCell(getBookDetails(serialNo));
						cell.setBorder(Rectangle.NO_BORDER);
						reportTable.addCell(cell);
						cell = new PdfPCell(new Phrase(" ", B_FONT));
						cell.setBorder(Rectangle.NO_BORDER);
						reportTable.addCell(cell);

						totalBookQty = totalBookQty
								+ serialNo.length;

						cell = new PdfPCell(new Phrase(" ", B_FONT));
						cell.setBorder(Rectangle.NO_BORDER);
						reportTable.addCell(cell);
						cell = new PdfPCell(new Phrase(" "
								+ serialNo.length, B_FONT));
						cell.setBorder(Rectangle.BOX);
						// cell.setCellEvent(event);
						cell.setHorizontalAlignment(Element.ALIGN_CENTER);
						cell.setVerticalAlignment(Element.ALIGN_CENTER);
						reportTable.addCell(cell);
						System.out.println(" non consumable detail ............"
								+ serialNo.length);

						cell = new PdfPCell(getBlankCell(14));
						cell.setBorder(Rectangle.BOTTOM);
						cell.setColspan(2);
						reportTable.addCell(cell);
					}

				}
				flag = true;
				for (String key : conMapKey) {
					System.out.println(" gmae ===== " + key);
					//InvoiceGameDetailBean bean = (InvoiceGameDetailBean) reportBean
					//		.get(key);
					String Quantity=reportBean.get("Consumable").get(key);
					cell = new PdfPCell(
							new Phrase("Consumable : " + key, U_FONT));
					cell.setBorder(Rectangle.NO_BORDER);
					cell.setColspan(1);
					reportTable.addCell(cell);

					if (flag) {
						cell = new PdfPCell(new Phrase("Count", B_FONT));
						cell.setBorder(Rectangle.NO_BORDER);
						cell.setHorizontalAlignment(Element.ALIGN_CENTER);
						reportTable.addCell(cell);
						System.out.println(" inside qty of books ............");
						flag = false;
					} else {
						cell = new PdfPCell(new Phrase(" ", B_FONT));
						cell.setBorder(Rectangle.NO_BORDER);
						cell.setHorizontalAlignment(Element.ALIGN_CENTER);
						System.out
								.println(" not inside qty of consumable ............");
						reportTable.addCell(cell);
					}

					reportTable.addCell(getBlankCell(3));

					
					if (Integer.parseInt(Quantity) > 0) {
						cell = new PdfPCell(new Phrase("", B_FONT));
						cell.setBorder(Rectangle.NO_BORDER);
						cell.setColspan(2);
						reportTable.addCell(cell);

						cell = new PdfPCell(getConsDetails(Quantity));
						cell.setBorder(Rectangle.NO_BORDER);
						reportTable.addCell(cell);
						cell = new PdfPCell(new Phrase(" ", B_FONT));
						cell.setBorder(Rectangle.NO_BORDER);
						reportTable.addCell(cell);

						totalBookQty = totalBookQty
								+ Integer.parseInt(Quantity);

						cell = new PdfPCell(new Phrase(" ", B_FONT));
						cell.setBorder(Rectangle.NO_BORDER);
						reportTable.addCell(cell);
						cell = new PdfPCell(new Phrase(" "
								+ Integer.parseInt(Quantity), B_FONT));
						cell.setBorder(Rectangle.BOX);
						// cell.setCellEvent(event);
						cell.setHorizontalAlignment(Element.ALIGN_CENTER);
						cell.setVerticalAlignment(Element.ALIGN_CENTER);
						reportTable.addCell(cell);
						System.out.println(" books detail ............"
								+ Integer.parseInt(Quantity));

						cell = new PdfPCell(getBlankCell(14));
						cell.setBorder(Rectangle.BOTTOM);
						cell.setColspan(2);
						reportTable.addCell(cell);
					}

				}
			// blank column
			cell = new PdfPCell(getBlankCell(3));
			cell.setBorder(Rectangle.NO_BORDER);
			cell.setColspan(18);
			reportTable.addCell(cell);

			cell = new PdfPCell(new Phrase("Total Quantity ",
					THN_FONT));
			cell.setBorder(Rectangle.NO_BORDER);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			reportTable.addCell(cell);
			System.out.println("total quantity of delivered items");

			cell = new PdfPCell(new Phrase(" " + totalBookQty + " ", THN_FONT));
			cell.setBorder(Rectangle.BOX);
			/*
			 * GenerateDeliveryChallenByPDF event =new
			 * GenerateDeliveryChallenByPDF(); cell.setCellEvent(event);
			 */
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			reportTable.addCell(cell);
			System.out.println("total quantity of delivered items");

			// blank column
			cell = new PdfPCell(getBlankCell(30));
			cell.setBorder(Rectangle.NO_BORDER);
			cell.setColspan(2);
			reportTable.addCell(cell);

			cell = new PdfPCell(getLastPageFooter());
			cell.setBorder(Rectangle.NO_BORDER);
			cell.setColspan(2);
			reportTable.addCell(cell);
			System.out.println("footer");
			cell = new PdfPCell(getBlankCell(10));
			cell.setBorder(Rectangle.NO_BORDER);
			cell.setColspan(2);
			reportTable.addCell(cell);

			cell = new PdfPCell(getLastPageFooterAcknowladge());
			cell.setBorder(Rectangle.BOX);
			cell.setColspan(2);
			reportTable.addCell(cell);
			System.out.println("acknowledge");

			document.add(reportTable);

			document.close();
		} catch (DocumentException e) {
			e.printStackTrace();
		}

		byte[] arrayOutPut = ((ByteArrayOutputStream) baos).toByteArray();

		return arrayOutPut;

	}

	private PdfPCell getBlankCell(int height) {
		PdfPCell cell = new PdfPCell(new Phrase(" ", N_FONT));
		cell.setFixedHeight(height);
		cell.setColspan(2);
		cell.setBorder(Rectangle.NO_BORDER);
		// cell.setColspan(3);

		return cell;
	}
	private PdfPTable getConsDetails(String Quantity) throws DocumentException {
		PdfPTable headerTable = new PdfPTable(10);
		headerTable.setWidthPercentage(100);
		int headerwidths[] = new int[] { 3, 17, 3, 17, 3, 17, 3, 17, 3, 17 };
		headerTable.setWidths(headerwidths);

		headerTable.getDefaultCell().setPadding(3f);

		PdfPCell cell;
		System.out.println("hello ............................ ");
		// first row of header
		boolean flag = true;
		
		
		
		GenerateDeliveryNoteByPDF event = new GenerateDeliveryNoteByPDF();
		cell = new PdfPCell(new Phrase(" ", N_FONT));
		cell.setCellEvent(event);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setBorder(Rectangle.NO_BORDER);
		headerTable.addCell(cell);

		cell = new PdfPCell(
				new Phrase((String) Quantity, N_FONT));
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setBorder(Rectangle.NO_BORDER);
		headerTable.addCell(cell);
		
		
		return headerTable;
	}
	private PdfPTable getBookDetails(String[] serial) throws DocumentException {

		PdfPTable headerTable = new PdfPTable(10);
		headerTable.setWidthPercentage(100);
		int headerwidths[] = new int[] { 3, 17, 3, 17, 3, 17, 3, 17, 3, 17 };
		headerTable.setWidths(headerwidths);

		headerTable.getDefaultCell().setPadding(3f);

		PdfPCell cell;
		System.out.println("hello ............................ ");
		// first row of header
		boolean flag = true;
		int length = serial.length;
		for (int i = 0; i < length;) {
			for (int j = 0; j < 5; j++) {
				if (i < length) {

					GenerateDeliveryNoteByPDF event = new GenerateDeliveryNoteByPDF();
					cell = new PdfPCell(new Phrase(" ", N_FONT));
					cell.setCellEvent(event);
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setBorder(Rectangle.NO_BORDER);
					headerTable.addCell(cell);

					cell = new PdfPCell(
							new Phrase((String) serial[i], N_FONT));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setBorder(Rectangle.NO_BORDER);
					headerTable.addCell(cell);
				} else {

					cell = new PdfPCell(new Phrase(" ", N_FONT));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setBorder(Rectangle.NO_BORDER);
					// cell.setPaddingBottom(2);
					headerTable.addCell(cell);

					cell = new PdfPCell(new Phrase(" ", N_FONT));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setBorder(Rectangle.NO_BORDER);
					// cell.setPaddingBottom(2);
					headerTable.addCell(cell);

				}
				i++;
			}
		}

		return headerTable;
	}

	private PdfPTable getBox(int totalBooks) {
		PdfPTable headerTable = new PdfPTable(1);
		// int length=(""+totalBooks).length();
		headerTable.setWidthPercentage(60);
		int headerwidths[] = new int[] { 100 };
		try {
			headerTable.setWidths(headerwidths);
		} catch (DocumentException e) {
			e.printStackTrace();
		}

		PdfPCell cell;
		cell = new PdfPCell(new Phrase(" " + totalBooks + " ", THN_FONT));
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		GenerateDeliveryNoteByPDF event = new GenerateDeliveryNoteByPDF();
		cell.setCellEvent(event);
		cell.setFixedHeight(5);
		cell.setBorder(Rectangle.NO_BORDER);
		headerTable.addCell(cell);

		return headerTable;
	}

	private PdfPTable getLastPageFooter() throws DocumentException {

		PdfPTable headerTable = new PdfPTable(7);
		headerTable.setWidthPercentage(100);
		int headerwidths[] = new int[] { 27, 3, 22, 3, 22, 3, 20 };
		headerTable.setWidths(headerwidths);

		PdfPCell cell;

		cell = new PdfPCell(new Phrase(" Checked By ", B_FONT));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setBorder(Rectangle.TOP);
		cell.setColspan(2);
		headerTable.addCell(cell);

		cell = new PdfPCell(new Phrase("  ", B_FONT));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setColspan(3);
		headerTable.addCell(cell);

		cell = new PdfPCell(new Phrase(" Prepared By ", B_FONT));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setBorder(Rectangle.TOP);
		cell.setColspan(2);
		headerTable.addCell(cell);

		cell = new PdfPCell(new Phrase(" Delivery Mode : ", B_FONT));
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setBorder(Rectangle.NO_BORDER);
		headerTable.addCell(cell);

		GenerateDeliveryNoteByPDF event = new GenerateDeliveryNoteByPDF();

		cell = new PdfPCell(new Phrase(" ", N_FONT));
		cell.setCellEvent(event);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setBorder(Rectangle.NO_BORDER);
		headerTable.addCell(cell);

		cell = new PdfPCell(new Phrase(" By Courier ", B_FONT));
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setBorder(Rectangle.NO_BORDER);
		headerTable.addCell(cell);

		cell = new PdfPCell(new Phrase(" ", N_FONT));
		cell.setCellEvent(event);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setBorder(Rectangle.NO_BORDER);
		headerTable.addCell(cell);

		cell = new PdfPCell(new Phrase(" By Person ", B_FONT));
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setBorder(Rectangle.NO_BORDER);
		headerTable.addCell(cell);

		cell = new PdfPCell(new Phrase(" ", N_FONT));
		cell.setCellEvent(event);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setBorder(Rectangle.NO_BORDER);
		headerTable.addCell(cell);

		cell = new PdfPCell(new Phrase(" By Self ", B_FONT));
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setBorder(Rectangle.NO_BORDER);
		headerTable.addCell(cell);

		return headerTable;
	}

	private PdfPTable getLastPageFooterAcknowladge() throws DocumentException {
		PdfPTable headerTable = new PdfPTable(3);
		headerTable.setWidthPercentage(100);
		int headerwidths[] = new int[] { 25, 30, 45 };
		headerTable.setWidths(headerwidths);

		PdfPCell cell;
		cell = new PdfPCell(new Phrase(" Acknowledgement ", B_FONT));
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setColspan(3);
		headerTable.addCell(cell);

		System.out.println("upadted ------------------------ ");

		cell = new PdfPCell(new Phrase(" Received By :  ", B_FONT));
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		// cell.setColspan(3);
		cell.setBorder(Rectangle.NO_BORDER);
		headerTable.addCell(cell);

		cell = new PdfPCell(new Phrase("  ", B_FONT));
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setBorder(Rectangle.BOTTOM);
		headerTable.addCell(cell);

		cell = new PdfPCell(new Phrase("  ", B_FONT));
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setBorder(Rectangle.NO_BORDER);
		headerTable.addCell(cell);

		cell = new PdfPCell(new Phrase(" Received On :  ", B_FONT));
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setBorder(Rectangle.NO_BORDER);
		// cell.setColspan(3);
		headerTable.addCell(cell);

		cell = new PdfPCell(new Phrase("  ", B_FONT));
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setBorder(Rectangle.BOTTOM);
		headerTable.addCell(cell);

		cell = new PdfPCell(new Phrase(" Signature / Stamp", B_FONT));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setBorder(Rectangle.NO_BORDER);
		headerTable.addCell(cell);
		//blank cell
		cell = new PdfPCell(getBlankCell(25));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setColspan(18);
		headerTable.addCell(cell);
		return headerTable;
	}

	private PdfPTable getPackDetails(List pack) throws DocumentException {

		PdfPTable headerTable = new PdfPTable(10);
		headerTable.setWidthPercentage(100);
		int headerwidths[] = new int[] { 3, 17, 3, 17, 3, 17, 3, 17, 3, 17 };
		headerTable.setWidths(headerwidths);
		headerTable.getDefaultCell().setPadding(3f);

		PdfPCell cell;

		// first row of header
		boolean flag = true;
		int length = pack.size();

		for (int i = 0; i < length;) {
			for (int j = 0; j < 5; j++) {
				if (i < length) {

					GenerateDeliveryNoteByPDF event = new GenerateDeliveryNoteByPDF();
					cell = new PdfPCell(new Phrase(" ", N_FONT));
					cell.setCellEvent(event);
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setBorder(Rectangle.NO_BORDER);
					headerTable.addCell(cell);

					// System.out.println(" inside if .............");

					cell = new PdfPCell(
							new Phrase((String) pack.get(i), N_FONT));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setBorder(Rectangle.NO_BORDER);
					headerTable.addCell(cell);
				} else {

					cell = new PdfPCell(new Phrase(" ", N_FONT));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setBorder(Rectangle.NO_BORDER);
					// cell.setPaddingBottom(2);
					headerTable.addCell(cell);

					cell = new PdfPCell(new Phrase(" ", N_FONT));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setBorder(Rectangle.NO_BORDER);
					headerTable.addCell(cell);

				}
				i++;
			}
		}

		return headerTable;
	}

	private PdfPTable getStaticHeaderOfReport(Map staticMap)
			throws DocumentException {

		PdfPTable headerTable = new PdfPTable(4);
		headerTable.setWidthPercentage(100);
		int headerwidths[] = new int[] { 53, 12, 15, 20 };
		headerTable.setWidths(headerwidths);

		System.out.println("static header map : == " + staticMap);

		PdfPCell cell;
		String headerName = "SN".equalsIgnoreCase(((String) request.getSession().getAttribute("deliveryChallanType"))) ? "Surrender Note" : "Delivery Note" ;
		cell = new PdfPCell(new Phrase(headerName, H_FONT));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setColspan(4);
		headerTable.addCell(cell);
		System.out.println("1");

		// first row of header
		cell = new PdfPCell(new Phrase("Generation Time : ", HN_FONT));
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setColspan(3);
		headerTable.addCell(cell);

		String time = (String) staticMap.get("dcGenerationTime");
		time = time != null ? time : " ";
		cell = new PdfPCell(new Phrase(time, N_FONT));
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setColspan(1);
		headerTable.addCell(cell);

		System.out.println("3");

		/*
		 * //Second row of header cell=new PdfPCell(new Phrase(" ", N_FONT));
		 * cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		 * cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		 * cell.setBorder(Rectangle.NO_BORDER); cell.setColspan(3);
		 * headerTable.addCell(cell);
		 */
		System.out.println("4");
		// first row of header
		cell = new PdfPCell(new Phrase("Address :", HN_FONT));
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setColspan(2);
		headerTable.addCell(cell);

		String address = (String) staticMap.get("boOrgAdd");
		address = address != null ? address : " ";
		cell = new PdfPCell(new Phrase(address, N_FONT));
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setColspan(2);
		headerTable.addCell(cell);
		// Third row of Header
		String companyName = (String) staticMap.get("boOrgName");
		companyName = companyName != null ? companyName : " ";
		cell = new PdfPCell(new Paragraph(companyName, new Font(Font.HELVETICA,
				27, Font.BOLD)));
		cell.setNoWrap(true);
		cell.setColspan(4);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		cell.setBorder(Rectangle.NO_BORDER);
		// cell.setBorderWidth(1);
		headerTable.addCell(cell);
/*
		cell = new PdfPCell(new Phrase("Address : ", B_FONT));
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.setBorder(Rectangle.NO_BORDER);
		// cell.setBorderWidth(1);
		headerTable.addCell(cell);
		System.out.println("6");

		String address = (String) staticMap.get("boOrgAdd");
		address = address != null ? address : " ";
		cell = new PdfPCell(new Phrase(address, N_FONT));
	
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setBorder(Rectangle.NO_BORDER);
		// cell.setBorderWidth(2);
		cell.setColspan(2);
		headerTable.addCell(cell);
	*/
		System.out.println("7");
		
/*
		String vatRef = (String) staticMap.get("vatRef");
		address = vatRef != null ? vatRef : " ";
		if (!"".equals(vatRef.trim())) {
			cell = new PdfPCell(new Phrase("VAT Ref Number :  " + vatRef,
					HN_FONT));
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			cell.setBorder(Rectangle.NO_BORDER);
			cell.setBorderWidth(1);
			cell.setColspan(4);
			headerTable.addCell(cell);
			System.out.println("8");
		}
*/
		// forth blank row (used to insert a line) of header
		cell = new PdfPCell(new Phrase(" ", N_FONT));
		cell.setFixedHeight(4);
		cell.setBorder(Rectangle.BOTTOM);
		cell.setBorderWidth(1);
		cell.setColspan(4);
		headerTable.addCell(cell);
		System.out.println("9");

		// fifth blank row (used to insert a gap) of header
		cell = new PdfPCell(new Phrase(" ", N_FONT));
		cell.setFixedHeight(4);
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setColspan(4);
		headerTable.addCell(cell);
		System.out.println("10");

		/*
		 * //fifth blank row (used to insert a gap) of header cell=new
		 * PdfPCell(new Phrase("To ", N_FONT)); //cell.setFixedHeight(4);
		 * cell.setBackgroundColor(Color.LIGHT_GRAY);
		 * cell.setBorder(Rectangle.NO_BORDER); cell.setColspan(4);
		 * headerTable.addCell(cell);
		 */

		// sixth row of header
		String customerName = (String) staticMap.get("customerName");
		customerName = customerName != null ? customerName : " ";
		cell = new PdfPCell(new Phrase(customerName, H_FONT));
		// cell.setBackgroundColor(Color.LIGHT_GRAY);
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setColspan(2);
		headerTable.addCell(cell);
		System.out.println("11");

		cell = new PdfPCell(new Phrase("Voucher Id : ", B_FONT));
		cell.setBorder(Rectangle.NO_BORDER);
		// cell.setBackgroundColor(Color.LIGHT_GRAY);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setColspan(1);
		headerTable.addCell(cell);

		String dcNo = (String) staticMap.get("dcNo");
		dcNo = dcNo != null ? dcNo : " ";
		cell = new PdfPCell(new Phrase(dcNo, N_FONT));
		cell.setBorder(Rectangle.NO_BORDER);
		// cell.setBackgroundColor(Color.LIGHT_GRAY);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.setColspan(1);
		headerTable.addCell(cell);
		System.out.println("12");

		// seventh row of header
		String customerAdd = (String) staticMap.get("customerAdd1") + ", "
				+ (String) staticMap.get("customerAdd2");
		customerAdd = customerAdd != null ? customerAdd : " ";
		cell = new PdfPCell(new Phrase(customerAdd,H_FONT));
		cell.setBorder(Rectangle.NO_BORDER);
		// cell.setBackgroundColor(Color.LIGHT_GRAY);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setColspan(2);
		headerTable.addCell(cell);

		cell = new PdfPCell(new Phrase("Voucher Date : ", B_FONT));
		cell.setBorder(Rectangle.NO_BORDER);
		// cell.setBackgroundColor(Color.LIGHT_GRAY);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setColspan(1);
		headerTable.addCell(cell);

		String dcDate = (String) staticMap.get("dcDate");
		dcDate = dcDate != null ? dcDate : " ";
		cell = new PdfPCell(new Phrase(dcDate, N_FONT));
		cell.setBorder(Rectangle.NO_BORDER);
		// cell.setBackgroundColor(Color.LIGHT_GRAY);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.setColspan(1);
		headerTable.addCell(cell);

		// Eight row of header
		/*
		 * cell=new PdfPCell(new Phrase(" ", B_FONT));
		 * cell.setBorder(Rectangle.NO_BORDER); cell.setFixedHeight(8);
		 * cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		 * cell.setBackgroundColor(Color.LIGHT_GRAY); cell.setBorderWidth(1);
		 * cell.setColspan(3); headerTable.addCell(cell);
		 * System.out.println("13");
		 */

		PdfPTable sheaderTable = new PdfPTable(5);
		sheaderTable.setWidthPercentage(100);
		int sheaderwidths[] = new int[] { 12, 41, 12, 15, 20 };
		sheaderTable.setWidths(sheaderwidths);

		// seventh row of header
		/*cell = new PdfPCell(new Phrase("Invoice Id : ", B_FONT));
		cell.setBorder(Rectangle.NO_BORDER);
		// cell.setBackgroundColor(Color.LIGHT_GRAY);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		sheaderTable.addCell(cell);

		String invoiceNo = (String) staticMap.get("invoiceId");
		invoiceNo = invoiceNo != null ? invoiceNo : " ";
		cell = new PdfPCell(new Phrase(invoiceNo, N_FONT));
		cell.setBorder(Rectangle.NO_BORDER);
		// cell.setBackgroundColor(Color.LIGHT_GRAY);
		cell.setColspan(2);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		sheaderTable.addCell(cell);
		System.out.println("14");

		cell = new PdfPCell(new Phrase("Invoice Date : ", B_FONT));
		cell.setBorder(Rectangle.NO_BORDER);
		// cell.setBackgroundColor(Color.LIGHT_GRAY);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setColspan(1);
		sheaderTable.addCell(cell);

		String invoiceDate = (String) staticMap.get("invoiceDate");
		invoiceDate = invoiceDate != null ? invoiceDate : " ";
		cell = new PdfPCell(new Phrase(invoiceDate, N_FONT));
		cell.setBorder(Rectangle.NO_BORDER);
		// cell.setBackgroundColor(Color.LIGHT_GRAY);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.setColspan(1);
		sheaderTable.addCell(cell);
		System.out.println("15");
*/
		// 8 row of header
/*
		cell = new PdfPCell(new Phrase("Order Id : ", B_FONT));
		cell.setBorder(Rectangle.NO_BORDER);
		// cell.setBackgroundColor(Color.LIGHT_GRAY);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		sheaderTable.addCell(cell);

		String orderNo = (String) staticMap.get("orderNo");
		orderNo = orderNo != null ? orderNo : " ";
		cell = new PdfPCell(new Phrase(orderNo, N_FONT));
		cell.setBorder(Rectangle.NO_BORDER);
		// cell.setBackgroundColor(Color.LIGHT_GRAY);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setColspan(2);
		sheaderTable.addCell(cell);
		System.out.println("16");

		cell = new PdfPCell(new Phrase("Order Date : ", B_FONT));
		cell.setBorder(Rectangle.NO_BORDER);
		// cell.setBackgroundColor(Color.LIGHT_GRAY);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setColspan(1);
		sheaderTable.addCell(cell);

		String orderDate = (String) staticMap.get("orderDate");
		orderDate = orderDate != null ? orderDate : " ";
		cell = new PdfPCell(new Phrase(orderDate, N_FONT));
		cell.setBorder(Rectangle.NO_BORDER);
		// cell.setBackgroundColor(Color.LIGHT_GRAY);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.setColspan(1);
		sheaderTable.addCell(cell);
		System.out.println("17");

		// Eight row of header
		cell = new PdfPCell(sheaderTable);
		cell.setBorder(Rectangle.NO_BORDER);
		// cell.setFixedHeight(3);
		// cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.setBorderWidth(0);
		cell.setColspan(4);
		headerTable.addCell(cell);
*/
		// Eight row of header
		cell = new PdfPCell(new Phrase(" ", B_FONT));
		cell.setBorder(Rectangle.BOTTOM);
		cell.setFixedHeight(3);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.setBorderWidth(1);
		cell.setColspan(4);
		headerTable.addCell(cell);
		System.out.println("18");

		return headerTable;
	}

	private PdfPTable getStaticHeaderOfSaleRetReport(Map staticMap)
			throws DocumentException {

		PdfPTable headerTable = new PdfPTable(4);
		headerTable.setWidthPercentage(100);
		int headerwidths[] = new int[] { 53, 12, 15, 20 };
		headerTable.setWidths(headerwidths);

		System.out.println("static header map : == " + staticMap);

		PdfPCell cell;

		cell = new PdfPCell(new Phrase("Sales Return Note", H_FONT));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setColspan(4);
		headerTable.addCell(cell);

		System.out.println("1");

		/*
		 * cell=new PdfPCell(new Phrase(" ", N_FONT)); cell.setFixedHeight(4);
		 * cell.setBorder(Rectangle.NO_BORDER); cell.setColspan(6);
		 * headerTable.addCell(cell); System.out.println("2");
		 */

		// first row of header
		cell = new PdfPCell(new Phrase("Generation Time : ", HN_FONT));
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setColspan(3);
		headerTable.addCell(cell);

		String time = (String) staticMap.get("dcGenerationTime");
		time = time != null ? time : " ";
		cell = new PdfPCell(new Phrase(time, N_FONT));
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setColspan(1);
		headerTable.addCell(cell);

		System.out.println("3");

		/*
		 * //Second row of header cell=new PdfPCell(new Phrase(" ", N_FONT));
		 * cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		 * cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		 * cell.setBorder(Rectangle.NO_BORDER); cell.setColspan(3);
		 * headerTable.addCell(cell);
		 */
		System.out.println("4");

		// Third row of Header
		String companyName = (String) staticMap.get("boOrgName");
		companyName = companyName != null ? companyName : " ";
		cell = new PdfPCell(new Paragraph(companyName, new Font(Font.HELVETICA,
				27, Font.BOLD)));
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		cell.setBorder(Rectangle.NO_BORDER);
		// cell.setBorderWidth(1);
		headerTable.addCell(cell);

		cell = new PdfPCell(new Phrase("Address : ", B_FONT));
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.setBorder(Rectangle.NO_BORDER);
		// cell.setBorderWidth(1);
		headerTable.addCell(cell);
		System.out.println("6");

		String address = (String) staticMap.get("boOrgAdd");
		address = address != null ? address : " ";
		cell = new PdfPCell(new Phrase(address, N_FONT));
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setBorder(Rectangle.NO_BORDER);
		// cell.setBorderWidth(2);
		cell.setColspan(2);
		headerTable.addCell(cell);
		System.out.println("7");
/*
		String vatRef = (String) staticMap.get("vatRef");
		address = vatRef != null ? vatRef : " ";
		if (!"".equals(vatRef.trim())) {
			cell = new PdfPCell(new Phrase("VAT Ref Number :  " + vatRef,
					HN_FONT));
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			cell.setBorder(Rectangle.NO_BORDER);
			cell.setBorderWidth(1);
			cell.setColspan(4);
			headerTable.addCell(cell);
		}*/
		System.out.println("8");

		// forth blank row (used to insert a line) of header
		cell = new PdfPCell(new Phrase(" ", N_FONT));
		cell.setFixedHeight(4);
		cell.setBorder(Rectangle.BOTTOM);
		cell.setBorderWidth(1);
		cell.setColspan(4);
		headerTable.addCell(cell);
		System.out.println("9");

		// fifth blank row (used to insert a gap) of header
		cell = new PdfPCell(new Phrase(" ", N_FONT));
		cell.setFixedHeight(4);
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setColspan(4);
		headerTable.addCell(cell);
		System.out.println("10");

		/*
		 * //fifth blank row (used to insert a gap) of header cell=new
		 * PdfPCell(new Phrase("To ", N_FONT)); //cell.setFixedHeight(4);
		 * cell.setBackgroundColor(Color.LIGHT_GRAY);
		 * cell.setBorder(Rectangle.NO_BORDER); cell.setColspan(4);
		 * headerTable.addCell(cell);
		 */

		// sixth row of header
		// sixth row of header
		String customerName = (String) staticMap.get("customerName");
		customerName = customerName != null ? customerName : " ";
		cell = new PdfPCell(new Phrase(customerName, N_FONT));
		// cell.setBackgroundColor(Color.LIGHT_GRAY);
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setColspan(2);
		headerTable.addCell(cell);
		System.out.println("11");

		cell = new PdfPCell(new Phrase("Voucher Id : ", B_FONT));
		cell.setBorder(Rectangle.NO_BORDER);
		// cell.setBackgroundColor(Color.LIGHT_GRAY);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setColspan(1);
		headerTable.addCell(cell);

		String srnNo = (String) staticMap.get("crNote");
		srnNo = srnNo != null ? srnNo : " ";
		cell = new PdfPCell(new Phrase(srnNo, N_FONT));
		cell.setBorder(Rectangle.NO_BORDER);
		// cell.setBackgroundColor(Color.LIGHT_GRAY);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.setColspan(1);
		headerTable.addCell(cell);
		System.out.println("12");

		// seventh row of header
		String customerAdd = (String) staticMap.get("customerAdd1") + ", "
				+ (String) staticMap.get("customerAdd2");
		customerAdd = customerAdd != null ? customerAdd : " ";
		cell = new PdfPCell(new Phrase(customerAdd, N_FONT));
		cell.setBorder(Rectangle.NO_BORDER);
		// cell.setBackgroundColor(Color.LIGHT_GRAY);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setColspan(2);
		headerTable.addCell(cell);

		cell = new PdfPCell(new Phrase("Voucher Date : ", B_FONT));
		cell.setBorder(Rectangle.NO_BORDER);
		// cell.setBackgroundColor(Color.LIGHT_GRAY);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setColspan(1);
		headerTable.addCell(cell);

		String srnDate = (String) staticMap.get("srnDate");
		srnDate = srnDate != null ? srnDate : " ";
		cell = new PdfPCell(new Phrase(srnDate, N_FONT));
		cell.setBorder(Rectangle.NO_BORDER);
		// cell.setBackgroundColor(Color.LIGHT_GRAY);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.setColspan(1);
		headerTable.addCell(cell);

		/*
		 * //Eight row of header cell=new PdfPCell(new Phrase(" ", B_FONT));
		 * cell.setBorder(Rectangle.NO_BORDER); cell.setFixedHeight(8);
		 * cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		 * cell.setBackgroundColor(Color.LIGHT_GRAY); cell.setBorderWidth(1);
		 * cell.setColspan(4); headerTable.addCell(cell);
		 * System.out.println("13");
		 */

		// seventh row of header
		PdfPTable sheaderTable = new PdfPTable(5);
		sheaderTable.setWidthPercentage(100);
		int sheaderwidths[] = new int[] { 18, 35, 12, 17, 18 };
		sheaderTable.setWidths(sheaderwidths);

		// seventh row of header
		cell = new PdfPCell(new Phrase("Credit Note Id :", B_FONT));
		cell.setBorder(Rectangle.NO_BORDER);
		// cell.setBackgroundColor(Color.LIGHT_GRAY);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		sheaderTable.addCell(cell);

		String crNote = (String) staticMap.get("srnNo");
		crNote = crNote != null ? crNote : " ";
		cell = new PdfPCell(new Phrase(" : " + crNote, N_FONT));
		cell.setBorder(Rectangle.NO_BORDER);
		// cell.setBackgroundColor(Color.LIGHT_GRAY);
		cell.setColspan(2);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		sheaderTable.addCell(cell);
		System.out.println("14");

		cell = new PdfPCell(new Phrase("Credit Note Date : ", B_FONT));
		cell.setBorder(Rectangle.NO_BORDER);
		// cell.setBackgroundColor(Color.LIGHT_GRAY);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setColspan(1);
		sheaderTable.addCell(cell);

		String crDate = (String) staticMap.get("crDate");
		crDate = crDate != null ? crDate : " ";
		cell = new PdfPCell(new Phrase(crDate, N_FONT));
		cell.setBorder(Rectangle.NO_BORDER);
		// cell.setBackgroundColor(Color.LIGHT_GRAY);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.setColspan(1);
		sheaderTable.addCell(cell);
		System.out.println("17");

		// Eight row of header
		cell = new PdfPCell(sheaderTable);
		cell.setBorder(Rectangle.NO_BORDER);
		// cell.setFixedHeight(3);
		// cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.setBorderWidth(0);
		cell.setColspan(4);
		headerTable.addCell(cell);

		cell = new PdfPCell(new Phrase(" ", B_FONT));
		cell.setBorder(Rectangle.BOTTOM);
		cell.setFixedHeight(3);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.setBorderWidth(1);
		cell.setColspan(4);
		headerTable.addCell(cell);
		System.out.println("18");

		return headerTable;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;

	}

}
