package com.skilrock.lms.web.scratchService.reportsMgmt.common;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

/*
 * � copyright 2007, SkilRock Technologies, A division of Sugal & Damani Lottery Agency Pvt. Ltd.
 * All Rights Reserved
 * The contents of this file are the property of Sugal & Damani Lottery Agency Pvt. Ltd.
 * and are subject to a License agreement with Sugal & Damani Lottery Agency Pvt. Ltd.; you may
 * not use this file except in compliance with that License.  You may obtain a
 * copy of that license from:
 * Legal Department
 * Sugal & Damani Lottery Agency Pvt. Ltd.
 * 6/35,WEA, Karol Bagh,
 * New Delhi
 * India - 110005
 * This software is distributed under the License and is provided on an �AS IS�
 * basis, without warranty of any kind, either express or implied, unless
 * otherwise provided in the License.  See the License for governing rights and
 * limitations under the License.
 */



import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.beans.DateBeans;
import com.skilrock.lms.beans.SaleReportBean;
import com.skilrock.lms.beans.ScratchTicketDetailStatusBean;
import com.skilrock.lms.beans.ScratchTicketStatusBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.scratchService.inventoryMgmt.common.BookWiseInvDetailForAgtHelper;
import com.skilrock.lms.coreEngine.scratchService.inventoryMgmt.common.TicketStatusReportHelper;

import jxl.Workbook;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

/**
 * This class provides methods for verify packs,books and to get valid packs and
 * books
 * 
 * @author Skilrock Technologies
 * 
 */
public class TicketStatusAgentAction extends ActionSupport implements
		ServletRequestAware, ServletResponseAware {

	/**
	 * 
	 */
	static Log logger = LogFactory.getLog(TicketStatusAgentAction.class);
	private static final long serialVersionUID = 1L;
	
	private HttpServletRequest request;
	private HttpServletResponse response;
	private  List<ScratchTicketStatusBean> ticketStatusList;
	private List<ScratchTicketDetailStatusBean> ticketDetailStatusList;
	private int ret_org_name;
	private int gameId;
	private String book_no;
	private String is_remain;
	private HttpSession session;
	/**
	 * This method is used to get the retailers organizations name to display on
	 * sale return page
	 * 
	 * @return String
	 */
	public String execute() throws LMSException {
		session = request.getSession();
		BookWiseInvDetailForAgtHelper helper = new BookWiseInvDetailForAgtHelper();
		Map<String, String> gameMap = helper.getGameMap();
		session.setAttribute("boAgentListGame", gameMap);
		session.setAttribute("boRetList", null);
		return SUCCESS;
	}
	public String agtRepTicketStatusSearch() {
		HttpSession session = getRequest().getSession();
		UserInfoBean userBean =(UserInfoBean) session.getAttribute("USER_INFO");
		ticketStatusList=	TicketStatusReportHelper.fetchTicketStatus(ret_org_name, gameId, userBean.getUserOrgId());
		session.setAttribute("ticketStatusList", ticketStatusList);

		return SUCCESS;
	}
	
	public String agtRepTicketDetailStatusSearch() {
		ticketDetailStatusList = TicketStatusReportHelper.fetchTicketDetailStatus(book_no, is_remain);

		return SUCCESS;
	}

	public String retRepTicketStatusSearch() {
		HttpSession session = getRequest().getSession();
		UserInfoBean userBean =(UserInfoBean) session.getAttribute("USER_INFO");
		ticketStatusList=	TicketStatusReportHelper.fetchTicketStatus(userBean.getUserOrgId(), gameId, userBean.getParentOrgId());
		session.setAttribute("ticketStatusList", ticketStatusList);

		return SUCCESS;
	}
	public String exportExcel() {
		HttpSession session = getRequest().getSession();
		List<ScratchTicketStatusBean> data = null;;
		data = (List<ScratchTicketStatusBean>) getRequest().getSession()
				.getAttribute("ticketStatusList");
		try {
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-disposition",
					"attachment;filename=Ticket Status Report.xls");
			WritableWorkbook w = Workbook.createWorkbook(response
					.getOutputStream());
		/*	DateBeans dateBeans = (DateBeans) request.getSession()
					.getAttribute("datebean");
			System.out.println(" date bean object is=============" + dateBeans);*/
			WriteExcelForTicketStatusReport excel = new WriteExcelForTicketStatusReport();
			UserInfoBean userBean =(UserInfoBean) session.getAttribute("USER_INFO");
            if(userBean.getUserType().equalsIgnoreCase("Retailer")){
            	excel.writeRetailerExcel(data, w,userBean);
            }else{
            	excel.writeAgentExcel(data, w,userBean);
            }
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (WriteException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;

	}
	
	public HttpServletRequest getRequest() {
		return request;
	}

	public HttpServletResponse getResponse() {
		return response;
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
	public List<ScratchTicketStatusBean> getTicketStatusList() {
		return ticketStatusList;
	}
	public void setTicketStatusList(List<ScratchTicketStatusBean> ticketStatusList) {
		this.ticketStatusList = ticketStatusList;
	}
	public List<ScratchTicketDetailStatusBean> getTicketDetailStatusList() {
		return ticketDetailStatusList;
	}
	public void setTicketDetailStatusList(List<ScratchTicketDetailStatusBean> ticketDetailStatusList) {
		this.ticketDetailStatusList = ticketDetailStatusList;
	}
	public int getRet_org_name() {
		return ret_org_name;
	}
	public void setRet_org_name(int ret_org_name) {
		this.ret_org_name = ret_org_name;
	}
	public int getGameId() {
		return gameId;
	}
	public void setGameId(int gameId) {
		this.gameId = gameId;
	}
	public String getBook_no() {
		return book_no;
	}
	public void setBook_no(String book_no) {
		this.book_no = book_no;
	}
	public String getIs_remain() {
		return is_remain;
	}
	public void setIs_remain(String is_remain) {
		this.is_remain = is_remain;
	}
	
}
