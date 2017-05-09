package com.skilrock.lms.web.scratchService.reportsMgmt.common;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.DateBeans;
import com.skilrock.lms.beans.PwtDetailsBean;
import com.skilrock.lms.coreEngine.scratchService.reportsMgmt.common.PwtDetailsReportHelper;

public class PwtDetailsReportAction extends ActionSupport implements
		ServletRequestAware {

	static Log logger = LogFactory.getLog(PwtDetailsReportAction.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int id;
	private String name;

	private HttpServletRequest request;

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String pwtDetails() {
		System.out.println("id is ==================== " + id + "   name "
				+ name);
		HttpSession session = request.getSession();
		if (session.getAttribute("datebean") instanceof DateBeans) {
			DateBeans new_name = (DateBeans) session.getAttribute("datebean");
			System.out.println("type casting is ok");
		} else {
			System.out.println("problem in type casting");
		}

		DateBeans dateBean = (DateBeans) session.getAttribute("datebean");
		PwtDetailsReportHelper detailHelper = new PwtDetailsReportHelper(
				dateBean);

		// when clicked on player pwt
		List<PwtDetailsBean> pwtDetailsbeanList = null;
		if (id == 0) {
			pwtDetailsbeanList = detailHelper.getPlayerPwtDetails();
			session.setAttribute("agent", null);
		} else {
			pwtDetailsbeanList = detailHelper.getAgentPwtDetails(id, name);
			session.setAttribute("agent", "agent");
		}
		session.setAttribute("pwtDetails", pwtDetailsbeanList);

		return SUCCESS;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setServletRequest(HttpServletRequest req) {
		// TODO Auto-generated method stub
		this.request = req;
	}

}
