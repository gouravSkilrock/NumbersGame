package com.skilrock.lms.coreEngine.reportsMgmt.common;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.mail.MessagingException;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import com.skilrock.lms.common.db.DBConnect;

public class TallyXmlGeneratorBOHelper {
	static final int BUFFER = 10048;

	public static void main(String[] args) {
		try {
			FileWriter fileWriter = new FileWriter(new File("d:/XXXXXXXX.xml"));
			XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
			Document myDocument = new Document(new Element("Vineeetttttt"));
			new Element("Vineeetttttt").addContent(new Element("wqwqw"));
			new Element("wqwqw").addContent(new Element("fsdf"));
			new Element("fsdf").addContent(new Element("wqwqgw"));
			new Element("wqwqgw").addContent(new Element("fsdf3"));
			new Element("fsdf3").addContent(new Element("wqwq1w"));
			new Element("wqwq1w").addContent(new Element("fsd1f"));

			Document myDocument1 = new Document(new Element("ANANT"));
			Document myDocument2 = new Document(new Element("GAURavvvvvvvvv"));
			outputter.output(myDocument2, fileWriter);

			// outputter.output(myDocument, fileWriter.toString());

			System.out
					.println(new File("d:/C.xml").length() + "Doneeeeeeeeeee");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	String destination = null;

	String sourceFile;

	public Map<String, Timestamp> getDuration(Timestamp fromDate,
			Timestamp toDate, String voucherType) {

		Connection connection = null;

		connection = DBConnect.getConnection();
		Statement stmt = null;
		PreparedStatement prepstmt = null;
		ResultSet rs = null;
		Timestamp frDate = null;
		Timestamp tDate = null;
		Timestamp startDate = null;
		String vouchType = null;
		Boolean repeat = false;
		Map<String, Timestamp> map = new HashMap<String, Timestamp>();
		try {
			stmt = connection.createStatement();

			rs = stmt
					.executeQuery("select from_date,to_date,start_date from st_bo_xml_period where voucher_type='"
							+ voucherType + "'");
			while (rs.next()) {
				frDate = rs.getTimestamp("from_date");
				tDate = rs.getTimestamp("to_date");
				startDate = rs.getTimestamp("start_date");
			}
			if (frDate != null) {
				if (fromDate.equals(frDate)) {
					if (tDate.after(toDate)) {
						map.put("fromDate", fromDate);
						map.put("toDate", tDate);
						map.put("startDate", startDate);
						// updateDuration(fromDate,tDate,voucherType);
					} else {
						map.put("fromDate", fromDate);
						map.put("toDate", toDate);
						map.put("startDate", startDate);
						// updateDuration(fromDate,toDate,voucherType);
					}
					// updateDuration(fromTimeStamp,dt,getTallyAccount());

				} else if (fromDate.after(frDate)) {
					if (toDate.before(tDate)) {
						map.put("fromDate", frDate);
						map.put("toDate", tDate);
						map.put("startDate", startDate);
					} else {
						map.put("fromDate", frDate);
						map.put("toDate", toDate);
						map.put("startDate", startDate);
					}

				} else if (fromDate.before(frDate)) {
					if (toDate.before(tDate)) {
						map.put("fromDate", fromDate);
						map.put("toDate", tDate);
						map.put("startDate", startDate);
					} else {
						map.put("fromDate", fromDate);
						map.put("toDate", toDate);
						map.put("startDate", startDate);
					}

				}
				System.out.println("st_bo_xml_period is not Empty  fromDate : "
						+ frDate + " tDate " + tDate);
			} else {
				prepstmt = connection
						.prepareStatement("insert into   st_bo_xml_period (from_date,to_date,voucher_type,start_date) values(?,?,?,?)");
				prepstmt.setTimestamp(1, fromDate);
				prepstmt.setTimestamp(2, toDate);
				prepstmt.setString(3, voucherType);
				prepstmt.setTimestamp(4, fromDate);
				prepstmt.execute();
				map.put("fromDate", fromDate);
				map.put("toDate", toDate);
				map.put("startDate", fromDate);
				System.out.println("st_bo_xml_period is  Empty  fromDate : "
						+ fromDate + " toDate " + toDate);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		return map;

	}

	private String getNarration(long transactionId, Connection connection,
			String transactionType) {
		try {
			String sqlQuery = null;
			Statement statement = null;
			ResultSet resultSet = null;
			String chqNum = null;
			String bankName = null;
			String partyName = null;
			String remarks = null;
			if (transactionType.equalsIgnoreCase("CHQ_BOUNCE")) {
				sqlQuery = "select cheque_nbr,drawee_bank,issuing_party_name from st_lms_bo_sale_chq where transaction_id='"
						+ transactionId + "'";
			} else {
				sqlQuery = "select remarks from st_lms_bo_debit_note where transaction_id='"
						+ transactionId + "'";
			}

			statement = connection.createStatement();
			resultSet = statement.executeQuery(sqlQuery);
			if (transactionType.equalsIgnoreCase("CHQ_BOUNCE")) {
				while (resultSet.next()) {
					chqNum = resultSet.getString("cheque_nbr");
					bankName = resultSet.getString("drawee_bank");
					partyName = resultSet.getString("issuing_party_name");
				}
				remarks = "Cheque No: " + chqNum + " bounced.  bank: "
						+ bankName + " issued by: " + partyName;
				System.out.println("reamrks 1 " + remarks);
				return remarks;
			} else {
				while (resultSet.next()) {
					remarks = resultSet.getString("remarks");
				}
				System.out.println("reamrks 2 " + remarks);
				return remarks;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public Element getTallyVouchElements(String tallyAccount,
			Timestamp fromTimeStamp, Timestamp dt, Connection connection,
			String voucherType, String partyLedger, Element reqDataElement,
			String tallyVersion) {
		Statement statement = null;
		ResultSet rs = null;
		String transactionType = null;
		String accountType = null;
		double amount = 0.0;
		double sumAmount = 0.0;
		long transactionId = 0;
		String transationWith = null;
		Timestamp timestamp = null;
		String DateString = null;

		String string = "select transaction_type,transaction_id,account_type,balance,amount,transaction_date,transaction_with from st_lms_bo_ledger where  transaction_date >= '"
				+ fromTimeStamp + "'and transaction_date<='" + dt + "'";
		String ledgerName = "";
		List<Double> list = new ArrayList<Double>();
		List<String> nameList = new ArrayList<String>();
		ListIterator iterator = null;
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		Element tallyVouchElement = null;
		String narration = null;
		// /

		// /
		try {

			statement = connection.createStatement();
			rs = statement.executeQuery(string);
			while (rs.next()) {
				transactionType = rs.getString("transaction_type");
				accountType = rs.getString("account_type");
				amount = rs.getDouble("amount");
				timestamp = rs.getTimestamp("transaction_date");
				transactionId = rs.getInt("transaction_id");
				transationWith = rs.getString("transaction_with");
				NumberFormat format = NumberFormat.getInstance();
				format.setGroupingUsed(false);
				format.setMinimumIntegerDigits(10);
				format.setMaximumFractionDigits(0);

				if (tallyAccount.equalsIgnoreCase("RECEIPT")) {// done
					DateString = dateFormat.format(timestamp);

					if (accountType.contains("_")
							&& transactionType.equalsIgnoreCase("CASH")
							|| accountType.contains("_")
							&& transactionType.equalsIgnoreCase("CHEQUE")) {
						tallyVouchElement = getVouchInnerElements(format
								.format(transactionId), DateString,
								voucherType, fromTimeStamp, partyLedger, null,
								tallyVersion);

						ledgerName = transationWith;

						saveElements(tallyVouchElement, -1.0 * amount,
								ledgerName, tallyAccount, tallyVersion,
								voucherType);
						System.out.println("111 transactionId " + transactionId
								+ " amount " + amount + " transactionWith "
								+ transationWith);

					} else if (transactionType.equalsIgnoreCase("CASH")
							&& !accountType.contains("_")
							|| transactionType.equalsIgnoreCase("CHEQUE")
							&& !accountType.contains("_")) {
						ledgerName = "Receipt";
						System.out.println("4444transactionId " + transactionId
								+ " amount " + amount);
						saveElements(tallyVouchElement, -1.0 * amount,
								ledgerName, tallyAccount, tallyVersion,
								voucherType);
						Element tallyMesgElement = new Element("TALLYMESSAGE");
						tallyMesgElement.setNamespace(Namespace
								.getNamespace("TallyUDF"));
						tallyMesgElement.addContent(tallyVouchElement);
						reqDataElement.addContent(tallyMesgElement);

					}
					System.out.println(" sumAmount " + sumAmount);
				}
				if (tallyAccount.equalsIgnoreCase("CHEQUE BOUNCE")) {// done

					DateString = dateFormat.format(timestamp);

					if (transactionType.equalsIgnoreCase("CHQ_BOUNCE")
							&& !accountType.contains("_")) {
						ledgerName = transationWith;
						narration = getNarration(transactionId, connection,
								transactionType);
						System.out.println("narration11111   " + narration);
						tallyVouchElement = getVouchInnerElements(format
								.format(transactionId), DateString,
								voucherType, fromTimeStamp, partyLedger,
								narration, tallyVersion);
						saveElements(tallyVouchElement, amount, ledgerName,
								tallyAccount, tallyVersion, voucherType);
						System.out.println("333 transactionId " + transactionId
								+ " amount " + amount);
					} else if (transactionType.equalsIgnoreCase("CHQ_BOUNCE")
							&& accountType.contains("_")) {
						ledgerName = "Receipt";
						saveElements(tallyVouchElement, amount, ledgerName,
								tallyAccount, tallyVersion, voucherType);
						System.out.println("222 transactionId " + transactionId
								+ " amount " + amount);
						Element tallyMesgElement = new Element("TALLYMESSAGE");
						tallyMesgElement.setNamespace(Namespace
								.getNamespace("TallyUDF"));
						tallyMesgElement.addContent(tallyVouchElement);
						reqDataElement.addContent(tallyMesgElement);
					}

				}
				if (tallyAccount.equalsIgnoreCase("DEBIT NOTE")) {// done

					DateString = dateFormat.format(timestamp);

					if ((transactionType.equalsIgnoreCase("DR_NOTE") || transactionType
							.equalsIgnoreCase("DR_NOTE_CASH"))
							&& !accountType.contains("_")) {
						ledgerName = transationWith;
						narration = getNarration(transactionId, connection,
								transactionType);
						tallyVouchElement = getVouchInnerElements(format
								.format(transactionId), DateString,
								voucherType, fromTimeStamp, partyLedger,
								narration, tallyVersion);
						saveElements(tallyVouchElement, amount, ledgerName,
								tallyAccount, tallyVersion, voucherType);
						System.out.println("333 transactionId " + transactionId
								+ " amount " + amount);
					} else if ((transactionType.equalsIgnoreCase("DEBIT NOTE") || transactionType
							.equalsIgnoreCase("DR_NOTE_CASH"))
							&& accountType.contains("_")) {
						ledgerName = "Receipt";
						saveElements(tallyVouchElement, amount, ledgerName,
								tallyAccount, tallyVersion, voucherType);
						System.out.println("222 transactionId " + transactionId
								+ " amount " + amount);
						Element tallyMesgElement = new Element("TALLYMESSAGE");
						tallyMesgElement.setNamespace(Namespace
								.getNamespace("TallyUDF"));
						tallyMesgElement.addContent(tallyVouchElement);
						reqDataElement.addContent(tallyMesgElement);
					}

				}

				if (tallyAccount.equalsIgnoreCase("TDS")) { // /done

					DateString = dateFormat.format(timestamp);

					if (accountType.equalsIgnoreCase("TDS_PAY")
							&& transactionType.equalsIgnoreCase("TDS")) {
						tallyVouchElement = getVouchInnerElements(format
								.format(transactionId), DateString,
								voucherType, fromTimeStamp, partyLedger, null,
								tallyVersion);
						// ledgerName="By TDS";
						ledgerName = "Deduction Payable";
						saveElements(tallyVouchElement, amount, ledgerName,
								tallyAccount, tallyVersion, voucherType);

					} else if (accountType.equalsIgnoreCase("BANK_ACC")
							&& transactionType.equalsIgnoreCase("TDS")) {
						// ledgerName="To Receipt";
						ledgerName = "Receipt";
						// saveElements(tallyVouchElement,amount,ledgerName);
						saveElements(tallyVouchElement, amount, ledgerName,
								tallyAccount, tallyVersion, voucherType);
						System.out.println("222 transactionId " + transactionId
								+ " amount " + amount);
						Element tallyMesgElement = new Element("TALLYMESSAGE");
						tallyMesgElement.setNamespace(Namespace
								.getNamespace("TallyUDF"));
						tallyMesgElement.addContent(tallyVouchElement);
						reqDataElement.addContent(tallyMesgElement);

					}

				}

				if (tallyAccount.equalsIgnoreCase("Govt Contribution")) {// /done

					DateString = dateFormat.format(timestamp);
					if (accountType.equalsIgnoreCase("GOVT_COMM")) {
						tallyVouchElement = getVouchInnerElements(format
								.format(transactionId), DateString,
								voucherType, fromTimeStamp, partyLedger, null,
								tallyVersion);
						// ledgerName="By Govt Contribution";
						ledgerName = "Good Cause";
						saveElements(tallyVouchElement, amount, ledgerName,
								tallyAccount, tallyVersion, voucherType);

					} else if (accountType.equalsIgnoreCase("BANK_ACC")
							&& transactionType.equalsIgnoreCase("GOVT_COMM")) {
						// ledgerName="To Receipt";
						ledgerName = "Receipt";
						saveElements(tallyVouchElement, amount, ledgerName,
								tallyAccount, tallyVersion, voucherType);
						System.out.println("222 transactionId " + transactionId
								+ " amount " + amount);
						Element tallyMesgElement = new Element("TALLYMESSAGE");
						tallyMesgElement.setNamespace(Namespace
								.getNamespace("TallyUDF"));
						tallyMesgElement.addContent(tallyVouchElement);
						reqDataElement.addContent(tallyMesgElement);

					}
				} else if (tallyAccount.equalsIgnoreCase("VAT Contribution")) {// /done

					DateString = dateFormat.format(timestamp);
					if (accountType.equalsIgnoreCase("VAT_PAY")) {
						tallyVouchElement = getVouchInnerElements(format
								.format(transactionId), DateString,
								voucherType, fromTimeStamp, partyLedger, null,
								tallyVersion);
						// ledgerName="By Govt Contribution";
						ledgerName = "VAT Contribution";
						saveElements(tallyVouchElement, amount, ledgerName,
								tallyAccount, tallyVersion, voucherType);

					} else if (accountType.equalsIgnoreCase("BANK_ACC")
							&& transactionType.equalsIgnoreCase("VAT")) {
						// ledgerName="To Receipt";
						ledgerName = "Receipt";
						saveElements(tallyVouchElement, amount, ledgerName,
								tallyAccount, tallyVersion, voucherType);
						System.out.println("222 transactionId " + transactionId
								+ " amount " + amount);
						Element tallyMesgElement = new Element("TALLYMESSAGE");
						tallyMesgElement.setNamespace(Namespace
								.getNamespace("TallyUDF"));
						tallyMesgElement.addContent(tallyVouchElement);
						reqDataElement.addContent(tallyMesgElement);

					}
				}

				else if (tallyAccount.equalsIgnoreCase("Sales Return")) {// /done

					DateString = dateFormat.format(timestamp);
					if (accountType.equalsIgnoreCase("SALE_RET")) {
						tallyVouchElement = tallyVouchElement = getVouchInnerElements(
								format.format(transactionId), DateString,
								voucherType, fromTimeStamp, partyLedger, null,
								tallyVersion);
						// ledgerName="By Sales Return";
						ledgerName = "Sales Return";
						saveElements(tallyVouchElement, amount, ledgerName,
								tallyAccount, tallyVersion, voucherType);

					} else if (transactionType.equalsIgnoreCase("SALE_RET")
							&& !accountType.contains("_")) {
						// ledgerName="To Agent";
						ledgerName = transationWith;
						saveElements(tallyVouchElement, amount, ledgerName,
								tallyAccount, tallyVersion, voucherType);
						System.out.println("222 transactionId " + transactionId
								+ " amount " + amount);
						Element tallyMesgElement = new Element("TALLYMESSAGE");
						tallyMesgElement.setNamespace(Namespace
								.getNamespace("TallyUDF"));
						tallyMesgElement.addContent(tallyVouchElement);
						reqDataElement.addContent(tallyMesgElement);

					}
				}
				// ///done/
				if (tallyAccount.equalsIgnoreCase("Gross Sales")) {

					DateString = dateFormat.format(timestamp);

					if (transactionType.equalsIgnoreCase("SALE")
							&& !accountType.contains("_")) {
						tallyVouchElement = getVouchInnerElements(format
								.format(transactionId), DateString,
								voucherType, fromTimeStamp, partyLedger, null,
								tallyVersion);
						// ledgerName="By Agent";
						ledgerName = transationWith;
						System.out.println(ledgerName + "   ledgerName "
								+ format.format(transactionId) + "  "
								+ format.getMaximumFractionDigits() + "  "
								+ format.getMinimumIntegerDigits());
						// sumAmount = sumAmount + amount;

						// list.add(amount);
						saveElements(tallyVouchElement, amount, ledgerName,
								tallyAccount, tallyVersion, voucherType);

					} else if (transactionType.equalsIgnoreCase("SALE")
							&& accountType.contains("_")) {
						// ledgerName="To Gross Sales";
						ledgerName = "Gross Sales";
						saveElements(tallyVouchElement, amount, ledgerName,
								tallyAccount, tallyVersion, voucherType);

						Element tallyMesgElement = new Element("TALLYMESSAGE");
						tallyMesgElement.setNamespace(Namespace
								.getNamespace("TallyUDF"));
						tallyMesgElement.addContent(tallyVouchElement);
						reqDataElement.addContent(tallyMesgElement);
					}
				}

				if (tallyAccount.equalsIgnoreCase("Player TDS")) {

					DateString = dateFormat.format(timestamp);

					if (transactionType.equalsIgnoreCase("PWT_PLR")
							&& accountType.equalsIgnoreCase("PLAYER_TDS")) {
						tallyVouchElement = getVouchInnerElements(format
								.format(transactionId), DateString,
								voucherType, fromTimeStamp, partyLedger, null,
								tallyVersion);
						// ledgerName="By Player TDS";
						ledgerName = "Player Deduction";
						// sumAmount = sumAmount + amount;
						// list.add(amount);
						saveElements(tallyVouchElement, amount, ledgerName,
								tallyAccount, tallyVersion, voucherType);

					} else if (accountType.equalsIgnoreCase("TDS_PAY")
							&& transactionType.equalsIgnoreCase("PWT_PLR")) {
						// ledgerName="To TDS Payable";
						ledgerName = "Deduction Payable";
						saveElements(tallyVouchElement, amount, ledgerName,
								tallyAccount, tallyVersion, voucherType);

						Element tallyMesgElement = new Element("TALLYMESSAGE");
						tallyMesgElement.setNamespace(Namespace
								.getNamespace("TallyUDF"));
						tallyMesgElement.addContent(tallyVouchElement);
						reqDataElement.addContent(tallyMesgElement);

					}
				}

				/*
				 * if (tallyAccount.equalsIgnoreCase("PWT Collection")) {
				 * DateString=dateFormat.format(timestamp);
				 * 
				 * if (((transactionType.equalsIgnoreCase("PWT")) ||
				 * (transactionType .equalsIgnoreCase("PWT_AUTO"))) &&
				 * (accountType.equalsIgnoreCase("PWT_PAY"))) {
				 * tallyVouchElement
				 * =getVouchInnerElements(format.format(transactionId
				 * ),DateString
				 * ,voucherType,fromTimeStamp,partyLedger,null,tallyVersion); //
				 * ledgerName="By PWT Collection"; ledgerName = "PWT
				 * Collection";
				 * saveElements(tallyVouchElement,amount,ledgerName,
				 * tallyAccount,tallyVersion,voucherType); } else if
				 * (((transactionType.equalsIgnoreCase("PWT")) ||
				 * (transactionType .equalsIgnoreCase("PWT_AUTO"))) &&
				 * !(accountType.contains("_"))) { // ledgerName="To Agent";
				 * ledgerName = transationWith;
				 * saveElements(tallyVouchElement,amount
				 * ,ledgerName,tallyAccount,tallyVersion,voucherType);
				 * 
				 * Element tallyMesgElement = new Element("TALLYMESSAGE");
				 * tallyMesgElement
				 * .setNamespace(Namespace.getNamespace("TallyUDF"));
				 * tallyMesgElement.addContent(tallyVouchElement);
				 * reqDataElement.addContent(tallyMesgElement); } }
				 */
				if (tallyAccount.equalsIgnoreCase("Collection Charges")) {

					DateString = dateFormat.format(timestamp);
					if ((transactionType.equalsIgnoreCase("PWT") || transactionType
							.equalsIgnoreCase("PWT_AUTO"))
							&& accountType.equalsIgnoreCase("PWT_CHARGES")) {
						System.out.println("coll charges 111");
						tallyVouchElement = getVouchInnerElements(format
								.format(transactionId), DateString,
								voucherType, fromTimeStamp, partyLedger, null,
								tallyVersion);
						// ledgerName="By PWT Collection";
						ledgerName = "Collection Charges";
						saveElements(tallyVouchElement, amount, ledgerName,
								tallyAccount, tallyVersion, voucherType);

					} else if ((transactionType.equalsIgnoreCase("PWT") || transactionType
							.equalsIgnoreCase("PWT_AUTO"))
							&& accountType.contains("#")) {
						System.out.println("coll charges 22");
						// ledgerName="To Agent";
						ledgerName = transationWith;
						saveElements(tallyVouchElement, amount, ledgerName,
								tallyAccount, tallyVersion, voucherType);

						Element tallyMesgElement = new Element("TALLYMESSAGE");
						tallyMesgElement.setNamespace(Namespace
								.getNamespace("TallyUDF"));
						tallyMesgElement.addContent(tallyVouchElement);
						reqDataElement.addContent(tallyMesgElement);

					}

				}

				if (tallyAccount.equalsIgnoreCase("Player PWT")) {

					DateString = dateFormat.format(timestamp);

					if (transactionType.equalsIgnoreCase("PWT_PLR")
							&& accountType.equalsIgnoreCase("PWT_PAY")) {
						tallyVouchElement = getVouchInnerElements(format
								.format(transactionId), DateString,
								voucherType, fromTimeStamp, partyLedger, null,
								tallyVersion);
						// ledgerName="By PWT Collection";
						ledgerName = "PWT Collection";
						saveElements(tallyVouchElement, amount, ledgerName,
								tallyAccount, tallyVersion, voucherType);

					} else if (transactionType.equalsIgnoreCase("PWT_PLR")
							&& accountType.equalsIgnoreCase("PLAYER_PWT")) {
						// ledgerName="To Player PWT";
						ledgerName = "Player PWT";
						saveElements(tallyVouchElement, amount, ledgerName,
								tallyAccount, tallyVersion, voucherType);

						Element tallyMesgElement = new Element("TALLYMESSAGE");
						tallyMesgElement.setNamespace(Namespace
								.getNamespace("TallyUDF"));
						tallyMesgElement.addContent(tallyVouchElement);
						reqDataElement.addContent(tallyMesgElement);

					}
				}

				if (tallyAccount.equalsIgnoreCase("Player Net")) {

					DateString = dateFormat.format(timestamp);
					if (accountType.equalsIgnoreCase("PLAYER_CAS")) {
						tallyVouchElement = getVouchInnerElements(format
								.format(transactionId), DateString,
								voucherType, fromTimeStamp, partyLedger, null,
								tallyVersion);
						// ledgerName="By Player Net";
						ledgerName = "Player Net";
						saveElements(tallyVouchElement, amount, ledgerName,
								tallyAccount, tallyVersion, voucherType);

					} else if (transactionType.equalsIgnoreCase("PWT_PLR")
							&& accountType.equalsIgnoreCase("BANK_ACC")) {
						// ledgerName="To Reciept";
						ledgerName = "Receipt";
						saveElements(tallyVouchElement, amount, ledgerName,
								tallyAccount, tallyVersion, voucherType);

						Element tallyMesgElement = new Element("TALLYMESSAGE");
						tallyMesgElement.setNamespace(Namespace
								.getNamespace("TallyUDF"));
						tallyMesgElement.addContent(tallyVouchElement);
						reqDataElement.addContent(tallyMesgElement);

					}
				}

			}

			System.out.println(" Final   sumAmount " + sumAmount);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tallyVouchElement;
	}

	public Element getVouchInnerElements(String id, String date,
			String voucherType, Timestamp fromTimeStamp, String partyLedger,
			String narration, String tallyVersion) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

		Element tallyVouchElement = new Element("VOUCHER");
		Element vouchIsOptionalElement = new Element("ISOPTIONAL");
		Element vouchUseGainElement = new Element("USEFORGAINLOSS");
		Element vouchUseCompElement = new Element("USEFORCOMPOUND");
		Element vouchTypeElement = new Element("VOUCHERTYPENAME");
		Element vouchDateElement = new Element("DATE");
		Element vouchEffDateElement = new Element("EFFECTIVEDATE");
		Element vouchIsCancElement = new Element("ISCANCELLED");
		Element reqIsOptionalElement = new Element("USETRACKINGNUMBER");
		Element vouchIsPostDtElement = new Element("ISPOSTDATED");
		Element vouchIsInvoiceElement = new Element("ISINVOICE");
		Element vouchDifActElement = new Element("DIFFACTUALQTY");
		Element vouchNumElement = new Element("VOUCHERNUMBER");
		Element vouchPartyLedElement = new Element("PARTYLEDGERNAME");
		Element vouchNarrationElement = new Element("NARRATION");
		Element vouchPaySlipElement = new Element("ASPAYSLIP");
		Element vouchGuidElement = new Element("GUID");
		Element vouchAlterIdElement = new Element("ALTERID");
		Element vouchIsCostCentreElement = null;
		Element vouchUseFrFinalProdElement = null;
		Element vouchCstFrmIssueTypElement = null;
		Element vouchCstFrmRecTypElement = null;
		Element vouchFbtPmntTypElement = null;
		Element vouchVchGstClassElement = null;
		Element vouchAuditedElement = null;
		Element vouchForJobCostingElement = null;
		Element vouchUseFrIntrstElement = null;
		Element vouchUseGdwnTrnfrElement = null;
		Element vouchExciseOpeningElement = null;
		Element vouchHasCashFlowElement = null;
		Element vouchMfgJrnlElement = null;

		Element vouchHasDiscntsElement = null;
		Element vouchIsDeletedElement = null;
		Element vouchAsOriginalElement = null;

		if (tallyVersion.equalsIgnoreCase("Tally9")) {
			vouchCstFrmIssueTypElement = new Element("CSTFORMISSUETYPE");
			vouchCstFrmRecTypElement = new Element("CSTFORMRECVTYPE");
			vouchFbtPmntTypElement = new Element("FBTPAYMENTTYPE");
			vouchVchGstClassElement = new Element("VCHGSTCLASS");
			vouchAuditedElement = new Element("AUDITED");
			vouchForJobCostingElement = new Element("FORJOBCOSTING");
			vouchUseFrIntrstElement = new Element("USEFORINTEREST");
			vouchUseGdwnTrnfrElement = new Element("USEFORGODOWNTRANSFER");
			vouchExciseOpeningElement = new Element("EXCISEOPENING");
			vouchHasCashFlowElement = new Element("HASCASHFLOW");
			vouchMfgJrnlElement = new Element("MFGJOURNAL");

			vouchHasDiscntsElement = new Element("HASDISCOUNTS");
			vouchIsDeletedElement = new Element("ISDELETED");
			vouchAsOriginalElement = new Element("ASORIGINAL");

			vouchCstFrmIssueTypElement.addContent("");
			vouchCstFrmRecTypElement.addContent("");
			vouchFbtPmntTypElement.addContent("Default");
			vouchVchGstClassElement.addContent("");
			vouchAuditedElement.addContent("No");
			vouchForJobCostingElement.addContent("No");
			vouchUseFrIntrstElement.addContent("No");
			vouchUseGdwnTrnfrElement.addContent("No");
			vouchExciseOpeningElement.addContent("No");
			if (voucherType.equalsIgnoreCase("Sales")
					|| voucherType.equalsIgnoreCase("Journal")) {
				vouchHasCashFlowElement.addContent("No");
			} else {
				vouchHasCashFlowElement.addContent("Yes");
			}

			vouchMfgJrnlElement.addContent("No");
			vouchHasDiscntsElement.addContent("No");
			vouchIsDeletedElement.addContent("No");
			vouchAsOriginalElement.addContent("No");
			if (voucherType.equalsIgnoreCase("Sales")) {
				vouchUseFrFinalProdElement = new Element(
						"USEFORFINALPRODUCTION");
				vouchIsCostCentreElement = new Element("ISCOSTCENTRE");
				vouchUseFrFinalProdElement.addContent("No");
				vouchIsCostCentreElement.addContent("No");
			}

		}

		tallyVouchElement.setAttribute("REMOTEID", id);
		tallyVouchElement.setAttribute("VCHTYPE", voucherType);
		tallyVouchElement.setAttribute("ACTION", "Create");

		// /Element vouchIsOptionalElement = new Element("ISOPTIONAL");
		vouchIsOptionalElement.addContent("No");
		// / Element vouchUseGainElement = new Element("USEFORGAINLOSS");
		vouchUseGainElement.addContent("No");
		// / Element vouchUseCompElement = new Element("USEFORCOMPOUND");
		vouchUseCompElement.addContent("No");
		// /Element vouchTypeElement = new Element("VOUCHERTYPENAME");
		vouchTypeElement.addContent(voucherType);
		// /Element vouchDateElement = new Element("DATE");
		vouchDateElement.addContent(date);
		// / Element vouchEffDateElement = new Element("EFFECTIVEDATE");
		System.out.println(date);
		vouchEffDateElement.addContent(date);
		// / Element vouchIsCancElement = new Element("ISCANCELLED");
		vouchIsCancElement.addContent("No");
		// / Element reqIsOptionalElement = new Element("USETRACKINGNUMBER");
		reqIsOptionalElement.addContent("No");
		// / Element vouchIsPostDtElement = new Element("ISPOSTDATED");
		vouchIsPostDtElement.addContent("No");
		// / Element vouchIsInvoiceElement = new Element("ISINVOICE");
		vouchIsInvoiceElement.addContent("No");
		// / Element vouchDifActElement = new Element("DIFFACTUALQTY");
		vouchDifActElement.addContent("No");
		// / Element vouchNumElement = new Element("VOUCHERNUMBER");
		vouchNumElement.addContent("1");
		// / Element vouchPartyLedElement = new Element("PARTYLEDGERNAME");
		vouchPartyLedElement.addContent(partyLedger);
		// / Element vouchNarrationElement = new Element("NARRATION");
		if (narration != null) {
			vouchNarrationElement.addContent(narration);
		} else {
			vouchNarrationElement.addContent("Bill No");
		}

		// / Element vouchPaySlipElement = new Element("ASPAYSLIP");
		vouchPaySlipElement.addContent("No");
		// / Element vouchGuidElement = new Element("GUID");
		vouchGuidElement.addContent(id);
		// / Element vouchAlterIdElement = new Element("ALTERID");
		vouchAlterIdElement.addContent(id);

		tallyVouchElement.addContent(vouchIsOptionalElement);
		tallyVouchElement.addContent(vouchUseGainElement);
		tallyVouchElement.addContent(vouchUseCompElement);
		tallyVouchElement.addContent(vouchTypeElement);
		tallyVouchElement.addContent(vouchDateElement);
		tallyVouchElement.addContent(vouchEffDateElement);
		tallyVouchElement.addContent(vouchIsCancElement);
		tallyVouchElement.addContent(reqIsOptionalElement);
		tallyVouchElement.addContent(vouchIsPostDtElement);
		tallyVouchElement.addContent(vouchIsInvoiceElement);
		tallyVouchElement.addContent(vouchDifActElement);
		tallyVouchElement.addContent(vouchNumElement);
		tallyVouchElement.addContent(vouchPartyLedElement);
		tallyVouchElement.addContent(vouchNarrationElement);
		tallyVouchElement.addContent(vouchPaySlipElement);
		tallyVouchElement.addContent(vouchGuidElement);
		tallyVouchElement.addContent(vouchAlterIdElement);
		if (tallyVersion.equalsIgnoreCase("Tally9")) {
			tallyVouchElement.addContent(vouchCstFrmIssueTypElement);
			tallyVouchElement.addContent(vouchCstFrmRecTypElement);
			tallyVouchElement.addContent(vouchFbtPmntTypElement);
			tallyVouchElement.addContent(vouchVchGstClassElement);
			tallyVouchElement.addContent(vouchAuditedElement);
			tallyVouchElement.addContent(vouchForJobCostingElement);
			tallyVouchElement.addContent(vouchUseFrIntrstElement);
			tallyVouchElement.addContent(vouchUseGdwnTrnfrElement);
			tallyVouchElement.addContent(vouchExciseOpeningElement);
			tallyVouchElement.addContent(vouchHasCashFlowElement);
			tallyVouchElement.addContent(vouchMfgJrnlElement);
			tallyVouchElement.addContent(vouchHasDiscntsElement);
			tallyVouchElement.addContent(vouchIsDeletedElement);
			tallyVouchElement.addContent(vouchAsOriginalElement);
			if (voucherType.equalsIgnoreCase("Sales")) {
				tallyVouchElement.addContent(vouchUseFrFinalProdElement);
				tallyVouchElement.addContent(vouchIsCostCentreElement);
			}
		}

		return tallyVouchElement;

	}

	public Element saveElements(Element tallyVouchElement, double amount,
			String ledgerName, String tallyAccount, String tallyVersion,
			String voucherType) {
		Element vouchLedEntryElement = null;

		Element vouchLedRemZeroElement = null;
		Element vouchLedIsDeemPostvElement = null;
		Element vouchLedFromItemElement = null;
		Element vouchLedgerNameElement = null;
		Element vouchLedAmountElement = null;
		Element vouchLedGstClassElement = null;
		Element vouchLedIsPartyLedElement = null;

		NumberFormat numFormat = NumberFormat.getInstance();
		String amt = numFormat.format(amount).replaceAll(",", "");
		if (amount < 0.0) {
			vouchLedEntryElement = new Element("ALLLEDGERENTRIES.LIST");

			vouchLedRemZeroElement = new Element("REMOVEZEROENTRIES");
			vouchLedIsDeemPostvElement = new Element("ISDEEMEDPOSITIVE");
			vouchLedFromItemElement = new Element("LEDGERFROMITEM");
			vouchLedgerNameElement = new Element("LEDGERNAME");
			vouchLedAmountElement = new Element("AMOUNT");

			if (tallyVersion.equalsIgnoreCase("Tally9")) {

				vouchLedGstClassElement = new Element("GSTCLASS");
				vouchLedIsPartyLedElement = new Element("ISPARTYLEDGER");

				vouchLedGstClassElement.addContent("");
				if (voucherType.equalsIgnoreCase("Journal")) {
					vouchLedIsPartyLedElement.addContent("No");
				} else {
					vouchLedIsPartyLedElement.addContent("Yes");
				}
			}
			vouchLedRemZeroElement.addContent("No");

			vouchLedIsDeemPostvElement.addContent("Yes");

			vouchLedFromItemElement.addContent("No");
			vouchLedgerNameElement.addContent(ledgerName);

			vouchLedAmountElement.addContent(amt);

			if (tallyVersion.equalsIgnoreCase("Tally9")) {

				vouchLedEntryElement.addContent(vouchLedGstClassElement);
				vouchLedEntryElement.addContent(vouchLedIsPartyLedElement);

			}

			vouchLedEntryElement.addContent(vouchLedRemZeroElement);
			vouchLedEntryElement.addContent(vouchLedIsDeemPostvElement);
			vouchLedEntryElement.addContent(vouchLedFromItemElement);
			vouchLedEntryElement.addContent(vouchLedgerNameElement);
			vouchLedEntryElement.addContent(vouchLedAmountElement);

			tallyVouchElement.addContent(vouchLedEntryElement);
		} else {

			vouchLedEntryElement = new Element("ALLLEDGERENTRIES.LIST");

			vouchLedRemZeroElement = new Element("REMOVEZEROENTRIES");
			vouchLedIsDeemPostvElement = new Element("ISDEEMEDPOSITIVE");
			vouchLedFromItemElement = new Element("LEDGERFROMITEM");
			vouchLedgerNameElement = new Element("LEDGERNAME");
			vouchLedAmountElement = new Element("AMOUNT");
			if (tallyVersion.equalsIgnoreCase("Tally9")) {

				vouchLedGstClassElement = new Element("GSTCLASS");
				vouchLedIsPartyLedElement = new Element("ISPARTYLEDGER");

				vouchLedGstClassElement.addContent("");
				if (voucherType.equalsIgnoreCase("Sales")) {
					vouchLedIsPartyLedElement.addContent("No");
				} else {
					vouchLedIsPartyLedElement.addContent("Yes");
				}
			}
			vouchLedRemZeroElement.addContent("No");
			vouchLedIsDeemPostvElement.addContent("No");
			vouchLedFromItemElement.addContent("No");
			vouchLedgerNameElement.addContent(ledgerName);
			if (amt.equals("-0")) {
				amt = "0";
			}
			vouchLedAmountElement.addContent(amt);
			if (tallyVersion.equalsIgnoreCase("Tally9")) {

				vouchLedEntryElement.addContent(vouchLedGstClassElement);
				vouchLedEntryElement.addContent(vouchLedIsPartyLedElement);

			}
			vouchLedEntryElement.addContent(vouchLedRemZeroElement);
			vouchLedEntryElement.addContent(vouchLedIsDeemPostvElement);
			vouchLedEntryElement.addContent(vouchLedFromItemElement);
			vouchLedEntryElement.addContent(vouchLedgerNameElement);
			vouchLedEntryElement.addContent(vouchLedAmountElement);

			tallyVouchElement.addContent(vouchLedEntryElement);

		}
		return tallyVouchElement;

	}

	/*
	 * public String zipLog(String filePath,String destPath) throws IOException,
	 * MessagingException{ sourceFile=source; destination=source+".zip";
	 * sourceFile=filePath; destination=destPath; BufferedInputStream origin =
	 * null; FileOutputStream dest = new FileOutputStream(destination);
	 * ZipOutputStream out = new ZipOutputStream(new
	 * BufferedOutputStream(dest));
	 * 
	 * byte data[] = new byte[BUFFER];
	 * 
	 * File f = new File(sourceFile);
	 * 
	 * FileInputStream fi = new FileInputStream(f); origin = new
	 * BufferedInputStream(fi, BUFFER); ZipEntry entry = new ZipEntry(filePath);
	 * out.putNextEntry(entry); int count; while((count = origin.read(data,
	 * 0,BUFFER)) != -1) { out.write(data, 0, count); } origin.close();
	 * 
	 * out.close();
	 * 
	 * System.out.println("Zipping Completed"); long fileSize = new
	 * File(destination).length();
	 * 
	 * return destination; }
	 */

	public ZipOutputStream zipLog(FileOutputStream dest, List<String> fileList)
			throws IOException, MessagingException {
		// destination=source+".zip";
		BufferedInputStream origin = null;
		// FileOutputStream dest = new FileOutputStream(destination);
		ZipOutputStream out = new ZipOutputStream(
				new BufferedOutputStream(dest));
		Iterator iterator = fileList.listIterator();
		int noOfFiles = 0;
		String filename = null;
		while (iterator.hasNext()) {
			noOfFiles++;
			byte data[] = new byte[BUFFER];
			filename = (String) iterator.next();
			File f = new File(filename);

			FileInputStream fi = new FileInputStream(f);
			origin = new BufferedInputStream(fi, BUFFER);
			System.out.println(filename + " |||||||||||||| ");
			System.out.println("filename.lastIndexOf(/) "
					+ filename.lastIndexOf("/"));
			System.out.println("filename.lastIndexOf(.) "
					+ filename.indexOf("."));
			filename = filename.substring(filename.lastIndexOf("/") + 1,
					filename.lastIndexOf("."))
					+ ".xml";
			ZipEntry entry = new ZipEntry(filename);
			out.putNextEntry(entry);
			int count;
			while ((count = origin.read(data, 0, BUFFER)) != -1) {
				out.write(data, 0, count);
			}
			origin.close();

			System.out.println(noOfFiles + " ========= file count");
		}
		System.out.println("Zipping Completed");
		out.close();
		return out;

	}

}
