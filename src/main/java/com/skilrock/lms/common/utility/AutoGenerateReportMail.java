package com.skilrock.lms.common.utility;

import java.sql.Connection;
import java.sql.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.common.db.DBConnect;

/**
 * This class is used to automatically generate a report(Sale, PWT, Cash Cheque)
 * mail to all Agents Daily,Weakly & Monthly.
 * 
 * @author Arun Upadhyay
 * 
 */
public class AutoGenerateReportMail {

	static Log logger = LogFactory.getLog(AutoGenerateReportMail.class);

	private Connection con = null;

	public AutoGenerateReportMail(Date date) {
		this.con = DBConnect.getConnection();
	}

}
