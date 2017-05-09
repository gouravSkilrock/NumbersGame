package com.skilrock.lms.scheduler;

import java.io.File;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.DateBeans;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.SendReportMailerMain;
import com.skilrock.lms.coreEngine.loginMgmt.common.UserAuthenticationHelper;

/**
 * This Class is used to scheduling the jobs at specific interval of time.
 * 
 * @author Arun Upadhyay
 */
public class SendingEmailReports extends ActionSupport implements
		ServletRequestAware, ServletResponseAware {

	public static Log logger = LogFactory
			.getLog(UserAuthenticationHelper.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static String[] Type = { "Current Day", "Current Week",
			"Current Month" };

	public synchronized static DateBeans getDateForSchedular(String type,
			Calendar calendar) {
		int index = -1;
		DateBeans dateBean = new DateBeans();
		for (int i = 0; i < Type.length; i++) {
			if (Type[i].equalsIgnoreCase(type)) {
				index = i;
				dateBean.setReportType(Type[i]);
				break;
			}
		}

		switch (index) {
		case 0: {
			Calendar cal = Calendar.getInstance();
			cal.setTime(calendar.getTime());
			cal.add(Calendar.DATE, -1);
			dateBean.setFirstdate(new java.sql.Date(cal.getTime().getTime()));
			dateBean.setLastdate(new java.sql.Date(cal.getTime().getTime()));
			dateBean.setReportday(cal.getTime());

			System.out.println("Query dates ===== First Date = "
					+ dateBean.getFirstdate() + "\t Last Date = "
					+ dateBean.getLastdate());
			dateBean.setStartDate(new java.sql.Date(cal.getTime().getTime()));
			dateBean.setEndDate(new java.sql.Date(cal.getTime().getTime()));

			System.out.println("Show in mail dates ====== start Date = "
					+ dateBean.getStartDate() + "\t End Date = "
					+ dateBean.getEndDate());

			return dateBean;

		}
		case 1: { // weakly reports

			Calendar cal = Calendar.getInstance();
			cal.setTime(calendar.getTime());

			// called when DAY_OF_WEAK is SUNDAY
			if (cal.get(Calendar.DAY_OF_WEEK) == 1) {
				cal.add(Calendar.DATE, -1);
			} 

			// set the Last Date from DAY_OF_WEAK as MONDAY of current weak
			cal.set(Calendar.DAY_OF_WEEK, 2);
			dateBean.setLastdate(new java.sql.Date(cal.getTime().getTime()-24*60*60*1000));
			// set the actual end date of reports till e-mail reports send
			cal.add(Calendar.DATE, -1);
			dateBean.setEndDate(cal.getTime());

			// undo the changes done in calculated Calendar date instance
			cal.add(Calendar.DATE, 1);

			// set the First Day of 'DAY_OF_WEAK' as MONDAY of previous weak
			cal.add(Calendar.WEEK_OF_MONTH, -1);
			dateBean.setFirstdate(new java.sql.Date(cal.getTimeInMillis()));
			dateBean.setStartDate(cal.getTime());

			System.out.println("Query dates ===== First Date = "
					+ dateBean.getFirstdate() + "\t Last Date = "
					+ dateBean.getLastdate());
			System.out.println("Show in mail dates ====== start Date = "
					+ dateBean.getStartDate() + "\t End Date = "
					+ dateBean.getEndDate());

			return dateBean;

		}
		case 2: {

			Calendar cal = Calendar.getInstance();
			cal.setTime(calendar.getTime());
			cal.set(Calendar.DATE, 1);
			dateBean.setLastdate(new java.sql.Date(cal.getTime().getTime()-24*60*60*1000));
			dateBean.setEndDate(cal.getTime());

			cal.setTime(calendar.getTime());
			cal.set(Calendar.DATE, 1);
			cal.add(Calendar.MONTH, -1);
			dateBean.setFirstdate(new java.sql.Date(cal.getTime().getTime()));
			dateBean.setStartDate(cal.getTime());

			System.out.println("Query dates ===== First Date = "
					+ dateBean.getFirstdate() + "\t Last Date = "
					+ dateBean.getLastdate());
			System.out.println("Show in mail dates ====== start Date = "
					+ dateBean.getStartDate() + "\t End Date = "
					+ dateBean.getEndDate());

			return dateBean;

		}
		}
		return dateBean;
	}

	public static void main(String[] args) throws Exception {

		Calendar cal = Calendar.getInstance();
		cal.set(2009, 5, 7);
		System.out.println("passed calender == " + cal.getTime());
		DateBeans datebean = getDateForSchedular(Type[0], cal);
		getDateForSchedular(Type[1], cal);
		getDateForSchedular(Type[2], cal);
	}

	private String end_Date;
	private String reportsTo;

	private HttpServletResponse response;

	private String start_date;

	private String totaltime;

	private void createTempFolder() {
		String projectName = AutoQuartzMain.scx.getContextPath();
		String folderName = "Mail_Excel_Files" + projectName;
		System.out.println("Created folder Name = " + folderName);
		File tempFolder = new File(folderName);

		int i = 0;
		while (!tempFolder.exists()) {
			tempFolder.mkdirs();
			i++;
			if (i > 5) {
				break;
			}
		}
	}

	public void forward(ServletRequest req, ServletResponse res) {

	}

	public String getEnd_Date() {
		return end_Date;
	}

	public String getReportsTo() {
		return reportsTo;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public String getStart_date() {
		return start_date;
	}

	public String getTotaltime() {
		return totaltime;
	}

	public void include(ServletRequest req, ServletResponse res) {

	}

	public String sendEmailReports() throws LMSException, SQLException, ParseException {
		createTempFolder();
		System.out.println("totaltime == " + totaltime + "   reportsTo == "
				+ reportsTo + " start_date=" + start_date + "  end_Date = "
				+ end_Date);
		DateBeans dateBeans = null;
		if (!totaltime.equalsIgnoreCase("Date Wise")) {
			dateBeans = getDateForSchedular(totaltime, Calendar.getInstance());
		} else {
			dateBeans = new DateBeans();
			SimpleDateFormat utilDateFormat = new SimpleDateFormat("dd-MM-yyyy");

			try {
				dateBeans.setStartDate(utilDateFormat.parse(start_date));
				dateBeans.setFirstdate(new java.sql.Date(utilDateFormat.parse(
						start_date).getTime()));
				dateBeans.setEndDate(utilDateFormat.parse(end_Date));
				dateBeans.setLastdate(new java.sql.Date(utilDateFormat.parse(
						end_Date).getTime()));
				dateBeans.setReportType("");
			} catch (ParseException e) {
				e.printStackTrace();
				throw new LMSException(e);
			}
		}

		SendReportMailerMain sendmail = new SendReportMailerMain(dateBeans);

		if ("ALL".equalsIgnoreCase(reportsTo.trim())
				|| "BO".equalsIgnoreCase(reportsTo.trim())) {
			System.out
					.println("=================== mail sending to Agent Started =============");
			sendmail.sendMailToBO(totaltime);
			System.out
					.println("=========== mail sending to BO completed ==========");
		}

		if ("ALL".equalsIgnoreCase(reportsTo.trim())
				|| "AGENT".equalsIgnoreCase(reportsTo.trim())) {
			System.out
					.println("=================== mail sending to Agent Started =============");
			sendmail.sendMailToAgent(totaltime);
			System.out
					.println("=========== mail sending to Agents completed ==========");
		}

		return SUCCESS;
	}

	public void setEnd_Date(String end_Date) {
		this.end_Date = end_Date;
		if (this.end_Date != null) {
			// this.end_Date = GetDate.getSqlToUtilFormatStr(end_Date);
			this.end_Date = end_Date;
		}
	}

	public void setReportsTo(String reportsTo) {
		this.reportsTo = reportsTo;
	}

	public void setServletRequest(HttpServletRequest req) {

	}

	public void setServletResponse(HttpServletResponse res) {
		response = res;

	}

	public void setStart_date(String start_date) {
		this.start_date = start_date;
		if (this.start_date != null) {
			// this.start_date = GetDate.getSqlToUtilFormatStr(start_date);
			this.start_date = start_date;
		}
	}

	public void setTotaltime(String totaltime) {
		this.totaltime = totaltime;
	}

}