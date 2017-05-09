package com.skilrock.lms.web.reportsMgmt.common;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.mail.MessagingException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.coreEngine.reportsMgmt.common.TallyXmlGeneratorBOHelper;

public class TallyXmlGeneratorBO extends ActionSupport implements
		ServletRequestAware, ServletResponseAware {
	static Log logger = LogFactory.getLog(TallyXmlGeneratorBO.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {

		/*
		 * String filename="D:/vinuuuu/test.xml";
		 * filename=filename.substring(filename
		 * .lastIndexOf("/")+1,filename.indexOf("."))+".xml";
		 * System.out.println(filename);
		 */

		File file = new File("D:/q/w/test/");
		File file2[] = file.listFiles();
		for (int i = 0; i < file2.length; i++) {
			logger.debug(file2[i].delete() + " ===== " + i);
			System.out.println(file2[i].delete() + " ===== " + i);
		}

	}

	int BUFFER = 10048;
	Calendar calendar = Calendar.getInstance();
	String compName = null;

	Date date5 = null;
	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
	SimpleDateFormat dateformat1 = null;
	Date dateFrDtParse = null;
	Date dateToDtParse = null;
	String date = dateFormat.format(new Date());

	String destFile;
	Timestamp dt = null;
	String filePath;
	String formatString = null;
	private String fromDate = null;
	Date fromFinYear = null;
	Timestamp fromTimeStamp = null;
	String partyLedger = null;
	private HttpServletRequest request;
	private HttpServletResponse response;
	ServletContext servletContext;
	HttpSession session = null;
	Timestamp startDateId = null;
	OutputStream stream = null;
	String streamFile;
	String tallyAccount = null;
	String tallyVersion = null;
	private String toDate = null;
	String type = null;
	String voucherType = null;
	Element vouchLedAmountElement = null;
	Element vouchLedEntryElement = null;
	Element vouchLedFromItemElement = null;
	Element vouchLedgerNameElement = null;
	Element vouchLedIsDeemPostvElement = null;
	Element vouchLedRemZeroElement = null;
	String xmlType = null;

	String zipFile;

	public void boXMLAjax() throws Exception {
		session = request.getSession();

		PrintWriter out = getResponse().getWriter();
		String html = "";
		ArrayList<String> selectedList = null;
		if (getXmlType().equalsIgnoreCase("Accountwise")) {
			html = "<td align='left'>Select Account: </td><td align='right'><select class='option' name='tallyAccountSel' id='tallyAccountSel'>"
					+ "<OPTION VALUE=-1>--Please Select--"
					+ "<OPTION VALUE=\"Gross Sales\">Gross Sales"
					+ "<OPTION VALUE=\"Receipt\">Receipt"
					+ "<OPTION VALUE=\"CHEQUE BOUNCE\">Cheque Bounce"
					+ "<OPTION VALUE=\"DEBIT NOTE\">Debit Note"
					+ "<OPTION VALUE=\"TDS\">Deduction Payable"
					+ "<OPTION VALUE=\"Govt Contribution\">Good Cause"
					+ "<OPTION VALUE=\"Sales Return\">Sales Return"
					+ "<OPTION VALUE=\"Player TDS\">Player Deduction"
					+ "<OPTION VALUE=\"PWT Collection\">PWT Collection"
					+ "<OPTION VALUE=\"Collection Charges\">Collection Charges"
					+ "<OPTION VALUE=\"Player PWT\">Player PWT"
					+ "<OPTION VALUE=\"Player Net\">Player Net"
					+ "<OPTION class=\"option\" VALUE=\"VAT Contribution\">VAT Contribution</SELECT></td>";
			session.setAttribute("selectedList", selectedList);
		} else {
			selectedList = new ArrayList<String>();
			selectedList.add("Gross Sales");
			selectedList.add("Receipt");
			selectedList.add("CHEQUE BOUNCE");
			selectedList.add("DEBIT NOTE");
			selectedList.add("TDS");
			selectedList.add("Govt Contribution");
			selectedList.add("Sales Return");
			selectedList.add("Player TDS");
			selectedList.add("PWT Collection");
			selectedList.add("Collection Charges");
			selectedList.add("Player PWT");
			selectedList.add("Player Net");
			selectedList.add("VAT Contribution");
			session.setAttribute("selectedList", selectedList);
			html = "";
			logger.debug("XML BO ajax" + selectedList);
			System.out.println("XML BO ajax" + selectedList);
		}

		response.setContentType("text/html");
		out.print(html);
	}

	public String displayXML() {

		return SUCCESS;
	}

	public String generate(String tallyAccount, Timestamp fromTimeStamp,
			Timestamp dt, Connection connection, String pCompName,
			String pTallyVersion, String type) {

		if (tallyAccount.equalsIgnoreCase("Gross Sales")) {// done
			voucherType = "Sales";
			partyLedger = "Gross Sales";
		}

		else if (tallyAccount.equalsIgnoreCase("Receipt")) {// done
			voucherType = "Receipt";
			partyLedger = "Receipt";
		} else if (tallyAccount.equalsIgnoreCase("CHEQUE BOUNCE")) {// done
			voucherType = "Journal";
			partyLedger = "Receipt";
		} else if (tallyAccount.equalsIgnoreCase("DEBIT NOTE")) {// done
			voucherType = "Journal";
			partyLedger = "Receipt";
		} else if (tallyAccount.equalsIgnoreCase("TDS")) {// done
			voucherType = "Payment";
			partyLedger = "Receipt";
		} else if (tallyAccount.equalsIgnoreCase("Govt Contribution")) {// done
			voucherType = "Payment";
			partyLedger = "Receipt";
		} else if (tallyAccount.equalsIgnoreCase("Sales Return")) {// done
			voucherType = "Journal";
			partyLedger = "Sales Return";
		}

		else if (tallyAccount.equalsIgnoreCase("Player TDS")) {// done
			voucherType = "Journal";
			partyLedger = "Player Deduction";
		} else if (tallyAccount.equalsIgnoreCase("PWT Collection")) {// done
			voucherType = "Journal";
			partyLedger = "PWT Collection";
		} else if (tallyAccount.equalsIgnoreCase("Collection Charges")) {// done
			voucherType = "Journal";
			partyLedger = "Collection Charges";
		} else if (tallyAccount.equalsIgnoreCase("Player PWT")) {// done
			voucherType = "Journal";
			partyLedger = "Player PWT";
		} else if (tallyAccount.equalsIgnoreCase("Player Net")) {// done
			voucherType = "Payment";
			partyLedger = "Player Net";
		} else if (tallyAccount.equalsIgnoreCase("VAT Contribution")) {// done
			voucherType = "Payment";
			partyLedger = "Receipt";
		}

		/*
		 * Outer most Main element
		 */
		Element envElement = new Element("ENVELOPE");
		Element headElement = new Element("HEADER");
		Element talReqElement = new Element("TALLYREQUEST");

		talReqElement.addContent("Import Data");
		headElement.addContent(talReqElement);
		envElement.addContent(headElement);

		Element bodyElement = new Element("BODY");
		Element impDatElement = new Element("IMPORTDATA");

		Element reqDesElement = new Element("REQUESTDESC");
		Element repNameElement = new Element("REPORTNAME");
		repNameElement.addContent("All Masters");

		Element staticVarElement = new Element("STATICVARIABLES");
		Element svCurrCompElement = new Element("SVCURRENTCOMPANY");
		Element reqDataElement = new Element("REQUESTDATA");

		svCurrCompElement.addContent(pCompName);
		staticVarElement.addContent(svCurrCompElement);

		reqDesElement.addContent(repNameElement);
		reqDesElement.addContent(staticVarElement);

		TallyXmlGeneratorBOHelper helper = new TallyXmlGeneratorBOHelper();
		Element tallyVouchElement = helper.getTallyVouchElements(tallyAccount,
				fromTimeStamp, dt, connection, voucherType, partyLedger,
				reqDataElement, pTallyVersion);

		impDatElement.addContent(reqDesElement);
		impDatElement.addContent(reqDataElement);

		bodyElement.addContent(impDatElement);

		envElement.addContent(bodyElement);

		Document myDocument = new Document(envElement);
		logger.debug("XML--------------------"
				+ myDocument.getDocument().toString());
		System.out.println("XML--------------------"
				+ myDocument.getDocument().toString());

		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");
			// writing xml
			// String fileName = tallyAccount +dateFormat.format(new Date())+
			// ".xml";
			// System.out.println(fileName);
			XMLOutputter xmlOutputter = new XMLOutputter(Format
					.getPrettyFormat());

			/*
			 * response.setContentType("text/xml");
			 * response.setHeader("Content-disposition"
			 * ,"attachment;filename="+fileName);
			 */
			servletContext = ServletActionContext.getServletContext();
			String path = servletContext.getRealPath("/");

			path = path.substring(0, path.indexOf("LMSLinuxNew.war"))
					+ "LMSLinuxNew.war/tallyXML/";
			// path=path+"/tallyXML/"+tallyAccount+".xml";
			path = path.replace('\\', '/');
			File file = new File(path);
			file.mkdir();
			logger.debug(" in generate context path " + path + "  real path "
					+ servletContext.getRealPath("/"));
			System.out.println(" in generate context path " + path
					+ "  real path " + servletContext.getRealPath("/"));
			path = path + tallyAccount + ".xml";
			file = new File(path);
			// file.createTempFile("test", ".xml");
			/*
			 * FileChannel sourceChannel=new
			 * FileOutputStream(file).getChannel(); long size =
			 * sourceChannel.size(); byte[] bytes = new byte[10]; ByteBuffer buf
			 * = ByteBuffer.wrap(bytes); sourceChannel.write(buf);
			 */
			logger.debug(" sourceChannel  created");

			System.out.println(" sourceChannel  created");
			// System.out.println(file.createNewFile()+ " *************");
			// System.out.println(" BOLLEAN "+file.exists());
			// File file= new File(path);

			FileWriter fileWriter = new FileWriter(file);
			logger.debug(" input file created");
			System.out.println(" input file created");
			// BufferedOutputStream bufferedOutputStream= new
			// BufferedOutputStream(OutStrm);
			// bufferedOutputStream.
			if ("all".equalsIgnoreCase(type)) {
				xmlOutputter.output(myDocument, fileWriter);
				fileWriter.flush();
				fileWriter.close();
			} else {
				OutputStream OutStrm = response.getOutputStream();
				xmlOutputter.output(myDocument, OutStrm);
				OutStrm.flush();
				OutStrm.close();

			}
			logger.debug(myDocument.getContentSize() + " contenctsize ");
			System.out.println(myDocument.getContentSize() + " contenctsize ");

			return path;
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SecurityException e) {
			logger.error("Unable to delete " + e.getMessage() + ")");
			System.err.println("Unable to delete " + e.getMessage() + ")");
		}
		return null;
	}

	public String generateXmlBO() {
		logger.info("Inside generateXmlBO");
		session = request.getSession();
		formatString = (String) session.getAttribute("date_format");
		dateformat1 = new SimpleDateFormat(formatString);
		servletContext = ServletActionContext.getServletContext();
		// dateformat1 = new SimpleDateFormat("yyyy-MM-dd");
		try {

			Connection connection = DBConnect.getConnection();
			dateFrDtParse = dateformat1.parse(getFromDate());

			fromTimeStamp = new Timestamp(dateFrDtParse.getTime());
			if (!getToDate().equalsIgnoreCase("")) {

				dateToDtParse = dateformat1.parse(getToDate());
				calendar.setTime(dateToDtParse);
				calendar.add(Calendar.DAY_OF_MONTH, 1);
				dateToDtParse = calendar.getTime();
				dt = new Timestamp(dateToDtParse.getTime());
				logger.debug(dt + "TOOOOOOOOOOOOO DATEEEEEEEE 1111111111");
				System.out
						.println(dt + "TOOOOOOOOOOOOO DATEEEEEEEE 1111111111");

			} else {

				Calendar calendar2 = Calendar.getInstance();

				calendar.set(calendar2.get(Calendar.YEAR), calendar2
						.get(Calendar.MONTH), calendar2.get(Calendar.DATE), 0,
						0, 0);
				calendar.add(Calendar.DAY_OF_MONTH, 1);

				date5 = calendar.getTime();

				dt = new Timestamp(date5.getTime());
				logger.debug(dt + "TOOOOOOOOOOOOO DATEEEEEEEE 111111111#####");
				System.out.println(dt
						+ "TOOOOOOOOOOOOO DATEEEEEEEE 111111111#####");
				// toDate = dateformat1.format(date5);
			}
			TallyXmlGeneratorBOHelper boHelper = new TallyXmlGeneratorBOHelper();
			/*
			 * Map<String, Timestamp> map= boHelper.getDuration(fromTimeStamp,
			 * dt, getTallyAccount()); fromTimeStamp=map.get("fromDate");
			 * dt=map.get("toDate"); startDateId=map.get("startDate");
			 */
			ArrayList typeList = (ArrayList) session
					.getAttribute("selectedList");
			logger.debug("getTallyAccount " + getTallyAccount() + " list "
					+ typeList + " getFromFinYear " + getFromFinYear());
			System.out.println("getTallyAccount " + getTallyAccount()
					+ " list " + typeList + " getFromFinYear "
					+ getFromFinYear());
			File file = new File(servletContext.getRealPath("/") + "/tallyXML/");
			File file2[] = file.listFiles();
			for (int i = 0; i < file2.length; i++) {
				logger.debug(file2[i].delete() + " ===== " + i);

				System.out.println(file2[i].delete() + " ===== " + i);
			}
			if (typeList == null) {
				String fileName = getTallyAccount()
						+ dateFormat.format(new Date()) + ".xml";
				response.setContentType("text/xml");
				response.setHeader("Content-disposition",
						"attachment;filename=" + fileName);
				logger.debug(" in ******* context path "
						+ servletContext.getContextPath() + "  real path "
						+ servletContext.getRealPath("/"));
				logger.debug("########  "
						+ servletContext.getRealPath("/").substring(
								0,
								servletContext.getRealPath("/").indexOf(
										"default")) + "default/log");
				System.out.println(" in ******* context path "
						+ servletContext.getContextPath() + "  real path "
						+ servletContext.getRealPath("/"));
				System.out.println("########  "
						+ servletContext.getRealPath("/").substring(
								0,
								servletContext.getRealPath("/").indexOf(
										"default")) + "default/log");
				filePath = generate(getTallyAccount(), fromTimeStamp, dt,
						connection, getCompName(), getTallyVersion(), "one");
				streamFile = filePath;
				// boHelper.updateDuration(fromTimeStamp,dt,getTallyAccount(),startDateId);

			} else {
				logger.debug(typeList.size() + " typeList.size()");
				System.out.println(typeList.size() + " typeList.size()");
				destFile = servletContext.getRealPath("/") + "/tallyXML/";
				file = new File(destFile);
				if (!file.exists()) {
					file.mkdir();
				}
				file = new File(destFile + "allaccounts.zip");
				if (!file.exists()) {
					file.createNewFile();
				}
				FileOutputStream dest = new FileOutputStream(destFile
						+ "allaccounts.zip");
				List<String> list = new ArrayList<String>();
				for (int j = 0; j < typeList.size(); j++) {
					filePath = generate((String) typeList.get(j),
							fromTimeStamp, dt, connection, getCompName(),
							getTallyVersion(), "all");
					list.add(filePath);

					// boHelper.updateDuration(fromTimeStamp,dt,getTallyAccount(),startDateId);
				}
				boHelper.zipLog(dest, list);
				response.setContentType("application/zip");
				response.setHeader("Content-disposition",
						"attachment;filename=allaccounts.zip");
				OutputStream outStrm = response.getOutputStream();
				byte data[] = new byte[BUFFER];
				File f = new File(destFile + "allaccounts.zip");
				FileInputStream fi = new FileInputStream(f);
				BufferedInputStream origin = new BufferedInputStream(fi, BUFFER);
				int count;
				while ((count = origin.read(data, 0, BUFFER)) != -1) {
					outStrm.write(data, 0, count);
				}
				origin.close();

				outStrm.close();
			}

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} /*
		 * catch (IOException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); } catch (MessagingException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); }
		 */catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public String getCompName() {
		return compName;
	}

	public String getFromDate() {
		return fromDate;
	}

	public Date getFromFinYear() {
		return fromFinYear;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public String getTallyAccount() {
		return tallyAccount;
	}

	public String getTallyVersion() {
		return tallyVersion;
	}

	public String getToDate() {
		return toDate;
	}

	public String getType() {
		return type;
	}

	public String getXmlType() {
		return xmlType;
	}

	public void setCompName(String compName) {
		this.compName = compName;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public void setFromFinYear(Date fromFinYear) {
		this.fromFinYear = fromFinYear;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;

	}

	public void setTallyAccount(String tallyAccount) {
		this.tallyAccount = tallyAccount;
	}

	public void setTallyVersion(String tallyVersion) {
		this.tallyVersion = tallyVersion;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setXmlType(String xmlType) {
		this.xmlType = xmlType;
	}

}
