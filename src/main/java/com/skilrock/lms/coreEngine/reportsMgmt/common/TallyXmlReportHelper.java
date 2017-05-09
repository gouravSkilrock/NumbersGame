package com.skilrock.lms.coreEngine.reportsMgmt.common;

import java.sql.Connection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.web.reportsMgmt.common.TallyXMLFilesDailyScheduleHelper;
import com.skilrock.lms.web.reportsMgmt.common.TallyXMLFilesMonthlyScheduleHelper;

public class TallyXmlReportHelper {
	private static final Log logger = LogFactory
			.getLog(TallyXmlReportHelper.class);

	public String[] fetchFiles(String startDate, String endDate, String fileType) {
		Connection con = null;
		String[] fileName=null;
		try {
			con = DBConnect.getConnection();
			logger.info("File Type-" + fileType);
			if ("BANK DEPOSIT".equalsIgnoreCase(fileType)) {
				fileName=new String[1];
				TallyXMLFilesDailyScheduleHelper.bankDepositXMLFilesCreation(startDate, con);
				fileName[0] = "Bank" + startDate.replace("-", "");
			} else if ("CASH COLLECTION".equalsIgnoreCase(fileType)) {
				fileName=new String[1];
				TallyXMLFilesDailyScheduleHelper.cashXMLFilesCreation(startDate, con);
				fileName[0] = "Cash" + startDate.replace("-", "");
			} else if ("TRAINING EXPENSES".equalsIgnoreCase(fileType)) {
				fileName=new String[1];
				TallyXMLFilesMonthlyScheduleHelper.trainingExpensesXMLFilesCreation(startDate, endDate,con);
				fileName[0] = "Training" + endDate.replace("-", "");
			} else if ("SALE".equalsIgnoreCase(fileType)) {
				fileName=new String[4];
				TallyXMLFilesMonthlyScheduleHelper.saleXMLFilesCreation(startDate, endDate, con);
				fileName[0] = "Sale-"+"KenoFour"+ endDate.replace("-", "");
				fileName[1] = "Sale-"+"KenoFive"+ endDate.replace("-", "");
				fileName[2] = "Sale-"+"TwelveByTwentyFour"+ endDate.replace("-", "");
				fileName[3] = "Sale-"+"KenoNine"+ endDate.replace("-", "");
			} else if ("PWT".equalsIgnoreCase(fileType)) {
				fileName=new String[4];
				TallyXMLFilesMonthlyScheduleHelper.pwtXMLFilesCreation(startDate, endDate, con);
				fileName[0] = "Pwt-"+"KenoFour"+ endDate.replace("-", "");
				fileName[1] = "Pwt-"+"KenoFive"+ endDate.replace("-", "");
				fileName[2] = "Pwt-"+"TwelveByTwentyFour"+ endDate.replace("-", "");
				fileName[3] = "Pwt-"+"KenoNine"+ endDate.replace("-", "");
			} else if ("SALE_CONSOLIDATED".equalsIgnoreCase(fileType)) {
				fileName = new String[1];
				TallyXMLFilesMonthlyScheduleHelper.saleConsolidatedXMLFilesCreation(startDate, endDate, con);
				fileName[0] = "SaleConsolidated-ALL" + endDate.replace("-", "");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnect.closeCon(con);
		}
		return fileName;
	}
}