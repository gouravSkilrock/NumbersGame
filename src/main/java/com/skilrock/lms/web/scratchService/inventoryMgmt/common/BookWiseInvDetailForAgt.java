package com.skilrock.lms.web.scratchService.inventoryMgmt.common;

import java.io.IOException;
import java.io.PrintWriter;
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
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.scratchService.inventoryMgmt.common.BookWiseInvDetailForAgtHelper;

public class BookWiseInvDetailForAgt extends ActionSupport implements
		ServletRequestAware, ServletResponseAware {
	static Log logger = LogFactory.getLog(BookWiseInvDetailForAgt.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int gameid;
	private int orgId;
	private HttpServletRequest request;

	private HttpServletResponse response;
	private HttpSession session;
	private String type;

	@Override
	public String execute() throws LMSException {
		System.out.println("bookWiseinventoryDetails");
		session = request.getSession();
		BookWiseInvDetailForAgtHelper helper = new BookWiseInvDetailForAgtHelper();
		Map<String, String> gameMap = helper.getGameMap();
		System.out.println("gameMap in bookWiseinventoryDetails ==== "
				+ gameMap);
		session.setAttribute("boAgentListGame", gameMap);
		session.setAttribute("boRetList", null);
		return SUCCESS;
	}

	public int getGameid() {
		return gameid;
	}

	public void getInventoryDetailsForBO() throws LMSException {
		try {
			System.out.println("type = " + type + "\t, orgId = " + orgId
					+ "\t, gameId = " + gameid);
			String responseStr = null;
			PrintWriter out = response.getWriter();

			BookWiseInvDetailForAgtHelper helper = new BookWiseInvDetailForAgtHelper();
			if ("AGENT".equalsIgnoreCase(type.trim())) {
				session = request.getSession();
				UserInfoBean infoBean = (UserInfoBean) session
						.getAttribute("USER_INFO");
				responseStr = helper.getTotalBooksWithOrg(gameid, infoBean
						.getUserOrgId(), infoBean.getUserType());
			} else if ("RETAILER".equalsIgnoreCase(type.trim())) {
				responseStr = helper.getTotalBooksWithOrg(gameid, orgId,
						"RETAILER");
			}

			out.print(responseStr);
		} catch (IOException e) {
			throw new LMSException(e);
		} catch (LMSException e) {
			throw new LMSException(e);
		} catch (Exception e) {
			throw new LMSException(e);
		}
	}

	public int getOrgId() {
		return orgId;
	}

	public void getRetailerList() throws Exception {
		PrintWriter out = response.getWriter();
		BookWiseInvDetailForAgtHelper helper = new BookWiseInvDetailForAgtHelper();
		session = request.getSession();
		UserInfoBean infoBean = (UserInfoBean) session
				.getAttribute("USER_INFO");
		int userOrgId = infoBean.getUserOrgId();
		List<String> retList = helper.getRetailerList(userOrgId);
		if (retList.size() < 1) {
			retList = null;
			out.print("NO_RET");
		} else {
			System.out.println(" -- getRetailerList---" + retList.toString());
			out.print(retList.toString());
		}
	}

	public String getType() {
		return type;
	}

	public void setGameid(int gameid) {
		this.gameid = gameid;
	}

	public void setOrgId(int orgId) {
		this.orgId = orgId;
	}

	public void setServletRequest(HttpServletRequest req) {
		this.request = req;
	}

	public void setServletResponse(HttpServletResponse res) {
		this.response = res;
	}

	public void setType(String type) {
		this.type = type;
	}

}
