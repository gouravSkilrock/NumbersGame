package com.skilrock.lms.web.reportsMgmt.common;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.LinkedList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.beans.TallyXMLBankDepositBean;
import com.skilrock.lms.beans.TallyXMLFilesBean;
import com.skilrock.lms.beans.TallyXmlVariablesBean;
import com.skilrock.lms.beans.TallyXMLBankDepositBean.Body.ImportData.RequestData.TallyMessage.Voucher.AllBankersDate;
import com.skilrock.lms.beans.TallyXMLFilesBean.Body.ImportData.RequestData.TallyMessage.Voucher.AllLedgerEntries;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.web.drawGames.common.Util;

public class TallyXMLFilesDailyScheduleHelper {
	private static final long serialVersionUID = 1L;

	private static Log logger = LogFactory
			.getLog(TallyXMLFilesDailyScheduleHelper.class);
/*
	public static String perform(int jobId) {
		Connection con = null;
		SimpleDateFormat formet = new SimpleDateFormat("yyyy/MM/dd");
		try {
			con = DBConnect.getConnection();
			SchedulerCommonFuntionsHelper.updateSchedulerStart(jobId, con);
			Calendar todayDate = Calendar.getInstance();
			todayDate.add(Calendar.DAY_OF_MONTH, -1);
			String date = formet.format(todayDate.getTime()).replace("/", "-");

			logger.info("Tally Xml Cash Collection and Bank Deposit Files for :"+date);
			bankDepositXMLFilesCreation(date, con);
			cashXMLFilesCreation(date, con);

		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		} finally {
			DBConnect.closeCon(con);
		}
		return null;
	}
*/
	public static void cashXMLFilesCreation(String date, Connection con) {
		Statement stmt = null;
		ResultSet rs = null;
		String query = null;
		TallyXmlVariablesBean bean = null;
		try {
			bean = new TallyXmlVariablesBean();
			bean.setEndDate(date);
			bean.setStartDate(date);
			bean.setCashBean();
			stmt = con.createStatement();
			query = "select bb.AgentName name,sum(ab.Cash) amt from (select amount Cash,agent_org_id from st_lms_bo_cash_transaction a inner join st_lms_bo_transaction_master b on a.transaction_id =b.transaction_id where  date(b.transaction_date)='"
					+ date
					+ "' and b.transaction_type='CASH' ) ab inner join (select name AgentName,organization_id from st_lms_organization_master) bb on ab.agent_org_id =bb.organization_id group by bb.AgentName;";
			logger.info("Cash Collection Query:" + query);
			rs = stmt.executeQuery(query);
			xmlFileCreation(bean, rs);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnect.closeConnection(stmt, rs);
		}
	}

	public static void bankDepositXMLFilesCreation(String date, Connection con) {
		Statement stmt = null;
		ResultSet rs = null;
		String query = null;
		TallyXmlVariablesBean bean = null;
		try {
			bean = new TallyXmlVariablesBean();
			bean.setEndDate(date);
			bean.setStartDate(date);
			bean.setBankBean();
			stmt = con.createStatement();
			logger.info("Bank Deposit Query:" + query);
			query = "select bb.AgentName name,sum(ab.BankDeposit) amt from (select amount BankDeposit,agent_org_id from st_lms_bo_bank_deposit_transaction a inner join st_lms_bo_transaction_master b on a.transaction_id =b.transaction_id where  date(b.transaction_date) ='"
					+ date
					+ "' and b.transaction_type='BANK_DEPOSIT' ) ab inner join (select name AgentName,organization_id from st_lms_organization_master) bb on ab.agent_org_id =bb.organization_id group by bb.AgentName;";
			rs = stmt.executeQuery(query);
			xmlFileCreation(bean, rs);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnect.closeConnection(stmt, rs);
		}
	}

	private static synchronized void xmlFileCreation(
			TallyXmlVariablesBean variablesBean, ResultSet rs) {

		String date = null;
		double amt = 0.0;
		String firstName = null;
		String totalName = null;
		boolean flag = true;
		String narration = null;
		try {
			NumberFormat formatter = new DecimalFormat("#0.00");
			date = Util.changeFormat("yyyy-MM-dd", "dd.MM.yy", variablesBean
					.getStartDate());

			if ("Cash".equalsIgnoreCase(variablesBean.getXmlFileType())) {
				totalName = "Cash Collections Account";
				narration = "Being amount received from agents for the day: "
						+ date + ".";
			} else if ("Bank".equalsIgnoreCase(variablesBean.getXmlFileType())) {
				totalName = "Diamond Bank-Collections A/C";
				narration = "Being amount  deposited  into bank by agent directly for  the day: "
						+ date + ".";
			}

			if (!("Bank".equalsIgnoreCase(variablesBean.getXmlFileType()))) {
				LinkedList<AllLedgerEntries> allLedgerList = new LinkedList<AllLedgerEntries>();
				TallyXMLFilesBean bean = new TallyXMLFilesBean();
				TallyXMLFilesBean.Header header = new TallyXMLFilesBean.Header();
				bean.setHeader(header);
				TallyXMLFilesBean.Body body = new TallyXMLFilesBean.Body();
				TallyXMLFilesBean.Body.ImportData importData = new TallyXMLFilesBean.Body.ImportData();
				TallyXMLFilesBean.Body.ImportData.RequestDesc requestDesc = new TallyXMLFilesBean.Body.ImportData.RequestDesc();
				TallyXMLFilesBean.Body.ImportData.RequestDesc.StaticVariables staticVariables = new TallyXMLFilesBean.Body.ImportData.RequestDesc.StaticVariables();
				requestDesc.setStaticVariables(staticVariables);
				importData.setRequestDesc(requestDesc);
				TallyXMLFilesBean.Body.ImportData.RequestData requestData = new TallyXMLFilesBean.Body.ImportData.RequestData();
				TallyXMLFilesBean.Body.ImportData.RequestData.TallyMessage tallyMessage = new TallyXMLFilesBean.Body.ImportData.RequestData.TallyMessage();
				TallyXMLFilesBean.Body.ImportData.RequestData.TallyMessage.Voucher voucher = new TallyXMLFilesBean.Body.ImportData.RequestData.TallyMessage.Voucher();

				// different for bank deposit
				voucher.setVchtype(variablesBean.getVoucherType());

				// toDate
				voucher.setDate(variablesBean.getEndDate().replace("-", ""));

				voucher.setNarration(narration);
				voucher.setVoucherTypeName(variablesBean.getVoucherType());

				// toDate
				voucher.setEffectiveDate(variablesBean.getEndDate().replace(
						"-", ""));
				String regex = "^[0-9]*$";
				while (rs.next()) {
					if (flag) {
						String data = rs.getString("name");
						if(data.length()>8 && data.substring(data.length()-8, data.length()).matches(regex))
							data=data.substring(0,data.length()-8);
						
						firstName=data;
						flag = false;
					}
					amt += rs.getDouble("amt");

					TallyXMLFilesBean.Body.ImportData.RequestData.TallyMessage.Voucher.AllLedgerEntries allLedgerEntries = new TallyXMLFilesBean.Body.ImportData.RequestData.TallyMessage.Voucher.AllLedgerEntries();
					allLedgerEntries.setIsDeemedPositive(variablesBean
							.getIsDeemedForLedger());
					if ("No".equalsIgnoreCase(variablesBean
							.getIsLedgerAmountPositive()))
						allLedgerEntries.setAmount(formatter.format(-1
								* (rs.getDouble("amt"))));
					else
						allLedgerEntries.setAmount(formatter.format(rs
								.getDouble("amt")));
					String data = rs.getString("name");
					if(data.length()>8 && data.substring(data.length()-8, data.length()).matches(regex))
						data=data.substring(0,data.length()-8);
					
					allLedgerEntries.setLedgerName(data);
					allLedgerEntries.setIsPartyLedger(variablesBean
							.getIsPartyForLedger());
					allLedgerList.add(allLedgerEntries);
				}

				TallyXMLFilesBean.Body.ImportData.RequestData.TallyMessage.Voucher.AllLedgerEntries allLedgerEntries = new TallyXMLFilesBean.Body.ImportData.RequestData.TallyMessage.Voucher.AllLedgerEntries();
				if ("No".equalsIgnoreCase(variablesBean
						.getIsLedgerAmountPositive()))
					allLedgerEntries.setAmount(formatter.format(amt));
				else
					allLedgerEntries.setAmount(formatter.format(-1 * amt));
				allLedgerEntries.setLedgerName(totalName);
				allLedgerEntries.setIsDeemedPositive(variablesBean
						.getIsDeemedForTotal());
				allLedgerEntries.setIsPartyLedger(variablesBean
						.getIsPartyForTotal());

				if ("Top".equalsIgnoreCase(variablesBean.getLocationOfTotal()))
					allLedgerList.addFirst(allLedgerEntries);
				else
					allLedgerList.add(allLedgerEntries);

				voucher.setPartyLedgerName(firstName);
				voucher.setAllLedger(allLedgerList);
				tallyMessage.setVoucher(voucher);
				requestData.setTallyMessage(tallyMessage);
				importData.setRequestData(requestData);
				body.setImportData(importData);
				bean.setBody(body);
				jaxbObjectToXML(bean, variablesBean.getXmlFileType(),
						variablesBean.getEndDate().replace("-", ""),
						variablesBean.getGameName(), "Cash");
			} else {
				LinkedList<com.skilrock.lms.beans.TallyXMLBankDepositBean.Body.ImportData.RequestData.TallyMessage.Voucher.AllLedgerEntries> allLedgerList = new LinkedList<com.skilrock.lms.beans.TallyXMLBankDepositBean.Body.ImportData.RequestData.TallyMessage.Voucher.AllLedgerEntries>();
				LinkedList<AllBankersDate> allBankersList = new LinkedList<AllBankersDate>();
				TallyXMLBankDepositBean bean = new TallyXMLBankDepositBean();
				TallyXMLBankDepositBean.Header header = new TallyXMLBankDepositBean.Header();
				bean.setHeader(header);
				TallyXMLBankDepositBean.Body body = new TallyXMLBankDepositBean.Body();
				TallyXMLBankDepositBean.Body.ImportData importData = new TallyXMLBankDepositBean.Body.ImportData();
				TallyXMLBankDepositBean.Body.ImportData.RequestDesc requestDesc = new TallyXMLBankDepositBean.Body.ImportData.RequestDesc();
				TallyXMLBankDepositBean.Body.ImportData.RequestDesc.StaticVariables staticVariables = new TallyXMLBankDepositBean.Body.ImportData.RequestDesc.StaticVariables();
				requestDesc.setStaticVariables(staticVariables);
				importData.setRequestDesc(requestDesc);
				TallyXMLBankDepositBean.Body.ImportData.RequestData requestData = new TallyXMLBankDepositBean.Body.ImportData.RequestData();
				TallyXMLBankDepositBean.Body.ImportData.RequestData.TallyMessage tallyMessage = new TallyXMLBankDepositBean.Body.ImportData.RequestData.TallyMessage();
				TallyXMLBankDepositBean.Body.ImportData.RequestData.TallyMessage.Voucher voucher = new TallyXMLBankDepositBean.Body.ImportData.RequestData.TallyMessage.Voucher();
				TallyXMLBankDepositBean.Body.ImportData.RequestData.TallyMessage.Voucher.AllBankersDate bankerDate = new TallyXMLBankDepositBean.Body.ImportData.RequestData.TallyMessage.Voucher.AllBankersDate();

				bankerDate.setBasicBankersDate(variablesBean.getEndDate()
						.replace("-", ""));
				allBankersList.add(bankerDate);
				voucher.setBankersDate(allBankersList);

				// different for bank deposit
				voucher.setVchtype(variablesBean.getVoucherType());

				// toDate
				voucher.setDate(variablesBean.getEndDate().replace("-", ""));

				voucher.setNarration(narration);
				voucher.setVoucherTypeName(variablesBean.getVoucherType());

				// toDate
				voucher.setEffectiveDate(variablesBean.getEndDate().replace("-", ""));
				String regex = "^[0-9]*$";
				while (rs.next()) {
					if (flag) {
						String data = rs.getString("name");
						if(data.length()>8 && data.substring(data.length()-8, data.length()).matches(regex))
							data=data.substring(0,data.length()-8);
						
						firstName=data;
						flag = false;
					}
					amt += rs.getDouble("amt");

					TallyXMLBankDepositBean.Body.ImportData.RequestData.TallyMessage.Voucher.AllLedgerEntries allLedgerEntries = new TallyXMLBankDepositBean.Body.ImportData.RequestData.TallyMessage.Voucher.AllLedgerEntries();
					allLedgerEntries.setIsDeemedPositive(variablesBean
							.getIsDeemedForLedger());
					if ("No".equalsIgnoreCase(variablesBean
							.getIsLedgerAmountPositive()))
						allLedgerEntries.setAmount(formatter.format(-1
								* (rs.getDouble("amt"))));
					else
						allLedgerEntries.setAmount(formatter.format(rs.getDouble("amt")));
					String data = rs.getString("name");
					if(data.length()>8 && data.substring(data.length()-8, data.length()).matches(regex))
						data=data.substring(0,data.length()-8);
					
					allLedgerEntries.setLedgerName(data);
					allLedgerEntries.setIsPartyLedger(variablesBean.getIsPartyForLedger());
					allLedgerList.add(allLedgerEntries);
				}

				TallyXMLBankDepositBean.Body.ImportData.RequestData.TallyMessage.Voucher.AllLedgerEntries allLedgerEntries = new TallyXMLBankDepositBean.Body.ImportData.RequestData.TallyMessage.Voucher.AllLedgerEntries();
				if ("No".equalsIgnoreCase(variablesBean
						.getIsLedgerAmountPositive()))
					allLedgerEntries.setAmount(formatter.format(amt));
				else
					allLedgerEntries.setAmount(formatter.format(-1 * amt));
				allLedgerEntries.setLedgerName(totalName);
				allLedgerEntries.setIsDeemedPositive(variablesBean
						.getIsDeemedForTotal());
				allLedgerEntries.setIsPartyLedger(variablesBean
						.getIsPartyForTotal());

				if ("Top".equalsIgnoreCase(variablesBean.getLocationOfTotal()))
					allLedgerList.addFirst(allLedgerEntries);
				else
					allLedgerList.add(allLedgerEntries);

				voucher.setPartyLedgerName(firstName);
				voucher.setAllLedger(allLedgerList);
				tallyMessage.setVoucher(voucher);
				requestData.setTallyMessage(tallyMessage);
				importData.setRequestData(requestData);
				body.setImportData(importData);
				bean.setBody(body);
				jaxbObjectToXML(bean, variablesBean.getXmlFileType(),
						variablesBean.getEndDate().replace("-", ""), null,
						"Bank");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void jaxbObjectToXML(Object emp, String type, String date, Object object, String fileType) {
		try {
			JAXBContext context = null;
			if (!("Bank".equalsIgnoreCase(fileType)))
				context = JAXBContext.newInstance(TallyXMLFilesBean.class);
			else
				context = JAXBContext.newInstance(TallyXMLBankDepositBean.class);

			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			m.marshal(emp, System.out);

			File file = null;
			if (object == null)
				file = new File(type + date + ".xml");
			else
				file = new File(type + "-" + object + date + ".xml");

			m.marshal(emp, new FileOutputStream(file));

			String OS = System.getProperty("os.name").toLowerCase();
			String root = "/tmp";
			if (OS.indexOf("win") >= 0) {
				root = "D:\\";
			} else {
				root = "/home/stpl/";
			}

			File folder = new File(root + "upload/");
			if (!folder.exists() || !folder.isDirectory()) {
				folder.mkdir();
			}

			String fileName = null;
			if (object == null)
				fileName = type + date + ".xml";
			else
				fileName = type + "-" + object + date + ".xml";

			file = new File(folder, fileName);
			System.out.println(file);

			m.marshal(emp, new FileOutputStream(file));
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}