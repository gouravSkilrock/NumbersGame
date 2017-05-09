package com.skilrock.lms.web.accMgmt.common;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.accMgmt.common.UpdateGovCommRateHelper;
import com.skilrock.lms.coreEngine.userMgmt.common.AgentSalePWTCommVarianceHelper;

public class UpdateGovCommRate extends ActionSupport implements
		ServletRequestAware {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String gameId;
	private double newGovCommRate;
	private HttpServletRequest request;

	// select ifnull(sum(bat.mrp_amt), 0) 'SALE_AMT' from
	// st_se_bo_agent_transaction bat, st_lms_bo_transaction_master btm
	// where btm.transaction_id = bat.transaction_id and
	// btm.transaction_type='SALE' and (btm.transaction_date>='2009-01-01' and
	// btm.transaction_date<'2009-02-02') group by agent_org_id=2

	private String serviceName;
	private String type;
	private HttpSession session;

	@Override
	public String execute() {
		session = request.getSession();
		AgentSalePWTCommVarianceHelper helper = new AgentSalePWTCommVarianceHelper();
		Map<String, String> serviceNameMap = helper.getServiceList();
		session.setAttribute("serviceNameMap", serviceNameMap);

		/*
		 * UpdateGovCommRateHelper helper= new UpdateGovCommRateHelper(); Map<String,
		 * String> gameDetails=helper.getGameMap();
		 * session.setAttribute("GAME_DETAILS_FOR_GOV_COMM", gameDetails);
		 */
		return SUCCESS;
	}

	public String getGameId() {
		return gameId;
	}

	public double getNewGovCommRate() {
		return newGovCommRate;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	public void setNewGovCommRate(double newGovCommRate) {
		this.newGovCommRate = newGovCommRate;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setServletRequest(HttpServletRequest req) {
		request = req;
	}

	public String updateGovCommRate() throws LMSException, Exception {
		System.out.println("service Name" + serviceName);
		System.out.println("updateGovCommRate for == " + gameId
				+ "  is called and   newGovCommRate= " + newGovCommRate);
		String spliValue[] = gameId.split(":");
		int gId = Integer.parseInt(spliValue[0]);
		// UpdateGovCommRateHelper helper= new UpdateGovCommRateHelper();

		UpdateGovCommRateHelper updatehelper = new UpdateGovCommRateHelper();
		Boolean flag = false;
		if("DG".equals(serviceName))
			flag = updatehelper.updateGovCommRateDG(type, gId, newGovCommRate);
		if("SLE".equals(serviceName))
			flag = updatehelper.updateGovCommRateSLE(type, gId, newGovCommRate);
		if("SE".equals(serviceName))
			flag = updatehelper.updateGovCommRateSE(gId, newGovCommRate);
		if("IW".equals(serviceName))
			flag = updatehelper.updateGovCommRateIW(gId, newGovCommRate);
		

		// boolean flag = helper.updateGovCommRate(gId, newGovCommRate);
		if (!flag) {
			throw new LMSException("Gov Comm Not Updated");
		}
		return SUCCESS;
	}
}
