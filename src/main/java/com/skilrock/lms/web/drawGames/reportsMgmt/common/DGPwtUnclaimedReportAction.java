package com.skilrock.lms.web.drawGames.reportsMgmt.common;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.DateBeans;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.utility.GetDate;
import com.skilrock.lms.coreEngine.drawGames.reportMgmt.DGPwtUnclaimedReportHelper;
import com.skilrock.lms.dge.beans.AgentPwtDetailBean;

public class DGPwtUnclaimedReportAction extends ActionSupport implements
		ServletRequestAware, ServletResponseAware {

	/**
	 * @author Amit Aggarwal
	 * 
	 *         Added for new report of finding unclaimed pwt ... on 16-JUN-2011
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String end_Date;
	Log logger = LogFactory.getLog(DGPwtUnclaimedReportAction.class);
	private String reportType;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private String start_date;
	private String totaltime;
	private int agtOrgId;
	
	
	public int getAgtOrgId() {
		return agtOrgId;
	}

	public void setAgtOrgId(int agtOrgId) {
		this.agtOrgId = agtOrgId;
	}

	public String getEnd_Date() {
		return end_Date;
	}

	public void setEnd_Date(String endDate) {
		end_Date = endDate;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	public String getStart_date() {
		return start_date;
	}

	public void setStart_date(String startDate) {
		start_date = startDate;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;

	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;

	}

	public String getTotaltime() {
		return totaltime;
	}

	public void setTotaltime(String totaltime) {
		this.totaltime = totaltime;
	}

	public String fetchPwtDetailsRetWise() throws Exception {
		HttpSession session = request.getSession();
		System.out.println("inside pwt calculation reports for agents ...");
		UserInfoBean userBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		DateBeans dateBean1 = new DateBeans();
		if ("Date Wise".equalsIgnoreCase(totaltime)) {
			dateBean1 = GetDate.getDate(start_date, end_Date);
		} else {
			dateBean1 = GetDate.getDate(totaltime);
		}
		DGPwtUnclaimedReportHelper helper = new DGPwtUnclaimedReportHelper();
		session.setAttribute("datebean", dateBean1);
		session.setAttribute("orgName", userBean.getOrgName());
		session.setAttribute("orgAdd", helper
				.getOrgAdd(userBean.getUserOrgId()));
		session.setAttribute("AGENT_PWT_LIST", helper.fetchDGPwtUnclaimed(
				userBean, dateBean1.getFirstdate().toString(), dateBean1
						.getLastdate().toString()));
		return SUCCESS;
	}
	
	public String fetchRetailerWise() throws Exception{
		System.out.println("inside fetching retailer wise ... ");
		HttpSession session = request.getSession();
		List<AgentPwtDetailBean> agtList = (List<AgentPwtDetailBean>)session.getAttribute("AGENT_PWT_LIST");
		for (int i = 0; i < agtList.size(); i++) {
			if(agtList.get(i).getAgtOrgId()==agtOrgId){
				session.setAttribute("RET_PWT_LIST", agtList.get(i).getRetDetailList());
				break;
			}
			
		}
		
		return SUCCESS;
	}

}
