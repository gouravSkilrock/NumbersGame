package com.skilrock.lms.web.reportsMgmt.common;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.DateBeans;
import com.skilrock.lms.beans.RetActivityBean;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.CommonMethods;
import com.skilrock.lms.common.utility.GetDate;
import com.skilrock.lms.coreEngine.reportsMgmt.common.RGReportHelper;

public class RGReportAction extends ActionSupport implements
		ServletRequestAware, ServletResponseAware {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String end_date;
	private String filter;
	private int organization_Name;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private String start_date;
	private String archDate;
	
	public String getArchDate() {
		return archDate;
	}

	public void setArchDate(String archDate) {
		this.archDate = archDate;
	}

	@Override
	public String execute() {
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
		Date strtDate = null;
		try {
			strtDate = new SimpleDateFormat("yyyy-MM-dd").parse(CommonMethods.getLastArchDate());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(strtDate);
		cal1.add(Calendar.DATE, 1);
		
		setArchDate(format.format(cal1.getTime()));
		return SUCCESS;
	}

	public void getActiveRGLimits() throws IOException {
		RGReportHelper help = new RGReportHelper();
		PrintWriter out = getResponse().getWriter();
		String html = help.getActiveRGLimits(filter);
		response.setContentType("text/html");
		out.print(html);
	}

	public String getFilter() {
		return filter;
	}

	public int getOrganization_Name() {
		return organization_Name;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public String getRGReport() throws LMSException {
		HttpSession session = request.getSession();
		RGReportHelper helper = new RGReportHelper();
		List<RetActivityBean> repMap = null;
		Map<String, Double> critMap = helper.getRGLimitsMap(filter);
		if (!filter.equalsIgnoreCase("All")) {
			repMap = helper.getRGRepMap(filter, organization_Name, null);
		} else {
			DateBeans dateBeans = GetDate.getDate(start_date, end_date);
			repMap = helper.getRGRepMap(filter, organization_Name, dateBeans);
		}
		session.setAttribute("rgRawRepMap", repMap);
		session.setAttribute("criteriaMap", critMap);
		return SUCCESS;
	}

	public String getStart_date() {
		return start_date;
	}

	

	public String getEnd_date() {
		return end_date;
	}

	public void setEnd_date(String endDate) {
		end_date = endDate;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}

	public void setOrganization_Name(int organization_Name) {
		this.organization_Name = organization_Name;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}

	public void setStart_date(String start_date) {
		this.start_date = start_date;
	}
}
