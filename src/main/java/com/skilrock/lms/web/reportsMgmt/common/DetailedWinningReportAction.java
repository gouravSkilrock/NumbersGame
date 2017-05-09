package com.skilrock.lms.web.reportsMgmt.common;

import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import com.skilrock.lms.common.BaseAction;
import com.skilrock.lms.common.utility.CommonMethods;
import com.skilrock.lms.coreEngine.reportsMgmt.controllerImpl.DetailedWinningControllerImpl;
import com.skilrock.lms.coreEngine.reportsMgmt.javaBeans.DetailedPaymentTransactionalBean;
import com.skilrock.lms.coreEngine.sportsLottery.beans.GameMasterBean;
import com.skilrock.lms.coreEngine.sportsLottery.beans.GameTypeMasterBean;
import com.skilrock.lms.coreEngine.sportsLottery.common.SLEUtil;
import com.skilrock.lms.web.drawGames.common.Util;

public class DetailedWinningReportAction extends BaseAction {
	private static final long serialVersionUID = 1L;

	public DetailedWinningReportAction() {
		super(DetailedWinningReportAction.class);
	}

	private String startDate;
	private String endDate;
	private String reportType;
	private String detailType;
	private int agentOrgId;
	private int retOrgId;
	private int gameNo;
	private String serviceName;
	private Map<String, Integer> serviceMap;
	private Map<Integer, String> serviceGameMap;
	private List<DetailedPaymentTransactionalBean> reportData = null;
	private String depDate;
	private int gameTypeId;

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getReportType() {
		return reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	public String getDetailType() {
		return detailType;
	}

	public void setDetailType(String detailType) {
		this.detailType = detailType;
	}

	public int getAgentOrgId() {
		return agentOrgId;
	}

	public void setAgentOrgId(int agentOrgId) {
		this.agentOrgId = agentOrgId;
	}

	public int getRetOrgId() {
		return retOrgId;
	}

	public void setRetOrgId(int retOrgId) {
		this.retOrgId = retOrgId;
	}

	public int getGameNo() {
		return gameNo;
	}

	public void setGameNo(int gameNo) {
		this.gameNo = gameNo;
	}

	public Map<String, Integer> getServiceMap() {
		return serviceMap;
	}

	public void setServiceMap(Map<String, Integer> serviceMap) {
		this.serviceMap = serviceMap;
	}

	public Map<Integer, String> getServiceGameMap() {
		return serviceGameMap;
	}

	public void setServiceGameMap(Map<Integer, String> serviceGameMap) {
		this.serviceGameMap = serviceGameMap;
	}

	public List<DetailedPaymentTransactionalBean> getReportData() {
		return reportData;
	}

	public void setReportData(List<DetailedPaymentTransactionalBean> reportData) {
		this.reportData = reportData;
	}

	public String getDepDate() {
		return depDate;
	}

	public void setDepDate(String depDate) {
		this.depDate = depDate;
	}
	
	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	
	public int getGameTypeId() {
		return gameTypeId;
	}

	public void setGameTypeId(int gameTypeId) {
		this.gameTypeId = gameTypeId;
	}

	public String reportSearch() {
		SimpleDateFormat dateFormat = null;
		try {
			dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Timestamp startTime = new Timestamp(dateFormat.parse(startDate).getTime());
			Timestamp endTime = new Timestamp(dateFormat.parse(endDate).getTime() + (24 * 60 * 60 * 1000 - 1000));

			reportData = DetailedWinningControllerImpl.getInstance().fetchData(serviceName, startTime, gameNo, endTime, reportType, detailType, agentOrgId, retOrgId, gameTypeId);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return SUCCESS;
	}

	public String fetchGameListMenu() {
		HttpSession session = getRequest().getSession();
		ServletContext sc = session.getServletContext();

		session.setAttribute("presentDate", new java.sql.Date(new Date().getTime()).toString());

		setDepDate(CommonMethods.convertDateInGlobalFormat((String) sc.getAttribute("DEPLOYMENT_DATE"), "yyyy-mm-dd", (String) sc.getAttribute("date_format")));
		Map<Integer, String> gameMap = new HashMap<Integer, String>();
		try {
			for (Map.Entry<Integer, GameMasterBean> entry : SLEUtil.gameInfoMap.entrySet()) {
				gameMap.put(entry.getKey(), entry.getValue().getGameDispName());
			}
			serviceMap = Util.serviceCodeIDMap;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}

	public String fetchServiceGameList() {
		PrintWriter out = null;
		JSONObject jsonObject = new JSONObject();
		try {
			serviceGameMap = new HashMap<Integer, String>();
			if ("DG".equals(serviceName))
				serviceGameMap = ReportUtility.fetchDrawDataMenu();
			else if ("SLE".equals(serviceName)) {
				if(gameNo == 0) {
					for (Map.Entry<Integer, GameMasterBean> entry : SLEUtil.gameInfoMap.entrySet()) {
						serviceGameMap.put(entry.getKey(), entry.getValue().getGameDispName());
					}
				} else {
					for (Map.Entry<Integer, GameTypeMasterBean> entry : SLEUtil.gameTypeInfoMap.entrySet()) {
						if(gameNo == entry.getValue().getGameId())
							serviceGameMap.put(entry.getKey(), entry.getValue().getGameTypeDispName());
					}
				}
			}
			out = response.getWriter();
			response.setContentType("application/json");
			jsonObject.put("isSuccess", true);
			jsonObject.put("responseData", serviceGameMap);
		} catch (Exception ex) {
			ex.printStackTrace();
			jsonObject.put("isSuccess", false);
			jsonObject.put("errorMsg", getText("msg.some.error"));
		} finally {
			out.print(jsonObject);
			out.flush();
			out.close();
		}

		return SUCCESS;
	}
}