package com.skilrock.lms.scheduler;

import java.util.ArrayList;
import java.util.List;

public class MailBean {

	public static final String DAILY_MAIL_FROM = "lms.dailyreport@skilrock.com";
	public static final String MONTHLY_MAIL_FROM = "lms.monthlyreport@skilrock.com";
	public static final String PASSWORD = "skilrock";
	public static final String WEEKLY_MAIL_FROM = "lms.weeklyreport@skilrock.com";
	String projectName = AutoQuartzMain.scx.getContextPath();

	public String AGENT_FILE_NAME = "Mail_Excel_Files" + projectName + "/";
	public String BO_FILE_NAME = "Mail_Excel_Files" + projectName + "/";
	public String boTextBody = "";

	public String reciepient = "";
	public String subject = "";
	public List<String> to = new ArrayList<String>();

}
