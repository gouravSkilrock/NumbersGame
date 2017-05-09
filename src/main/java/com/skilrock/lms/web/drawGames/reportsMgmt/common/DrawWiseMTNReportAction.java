package com.skilrock.lms.web.drawGames.reportsMgmt.common;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.common.BaseAction;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.drawGames.reportMgmt.DGSaleReportsHelper;
import com.skilrock.lms.dge.beans.DrawDataBean;
import com.skilrock.lms.dge.beans.DrawGameMtnDataBean;
import com.skilrock.lms.web.drawGames.common.Util;
import com.skilrock.lms.web.reportsMgmt.common.ReportUtility;

public class DrawWiseMTNReportAction extends BaseAction {
	private static final long serialVersionUID = 1L;
	private static Log logger = LogFactory.getLog(DGSaleReportAction.class);

	public DrawWiseMTNReportAction() {
		super("DrawWiseMTNReportAction");
	}

	private int gameNo;
	private String startDate;
	private String endDate;
	private HashMap<Integer, String> mtnGameMap = new HashMap<Integer, String>();
	private List<DrawGameMtnDataBean> mtnDataList = new ArrayList<DrawGameMtnDataBean>();
	private String reportData;
	private String drawStatus;
	private String drawName;
	private String message ;

	public String getDrawStatus() {
		return drawStatus;
	}

	public void setDrawStatus(String drawStatus) {
		this.drawStatus = drawStatus;
	}
	
	public String getDrawName() {
		return drawName;
	}

	public void setDrawName(String drawName) {
		this.drawName = drawName;
	}

	public int getGameNo() {
		return gameNo;
	}

	public void setGameNo(int gameNo) {
		this.gameNo = gameNo;
	}

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

	public HashMap<Integer, String> getMtnGameMap() {
		return mtnGameMap;
	}

	public void setMtnGameMap(HashMap<Integer, String> mtnGameMap) {
		this.mtnGameMap = mtnGameMap;
	}

	public List<DrawGameMtnDataBean> getMtnDataList() {
		return mtnDataList;
	}

	public void setMtnDataList(List<DrawGameMtnDataBean> mtnDataList) {
		this.mtnDataList = mtnDataList;
	}

	public String getReportData() {
		return reportData;
	}

	public void setReportData(String reportData) {
		this.reportData = reportData;
	}


	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String fetchMtnGameListMenu() {
		HashMap<Integer, String> gameMap = null;
		try {
			gameMap = ReportUtility.fetchDrawDataMenu();
			for (Map.Entry<Integer, String> entry : gameMap.entrySet()) {
				String gameName = entry.getValue();
				if(Util.getGameId("KenoFour") == entry.getKey() || Util.getGameId("KenoFive") == entry.getKey() || Util.getGameId("OneToTwelve") == entry.getKey())
					mtnGameMap.put(entry.getKey(), gameName);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return SUCCESS;
	}
	public String fetchDrawWiseMtnResult() {
 		try {
			if (drawStatus!=null && drawName!=null && gameNo > 0 && startDate != null) {
				DrawDataBean drawDataBean = new DrawDataBean();
				drawDataBean.setGameNo(gameNo);
				drawDataBean.setFromDate(startDate + " 00:00:00");
				drawDataBean.setDrawName(drawName);
				drawDataBean.setDrawStatus(drawStatus);
				drawDataBean.setToDate(endDate + " 23:59:59");
				try{
				mtnDataList = new DGSaleReportsHelper().fetchDrawWiseMtnData(drawDataBean);
			} catch (LMSException e) {
				message = e.getErrorMessage();
				return SUCCESS;
			}
				if (mtnDataList != null) {
					logger.info("Draw Game Data - " + mtnDataList.size());
					return SUCCESS;
				}
			} else {
				logger.info("Incorrect Inputs");
				addActionMessage("Please Enter Correct Values");
				return ERROR;
			}
		} catch (Exception e) {
			addActionMessage("Some Error In Draw Data ");
			e.printStackTrace();
		}

		return ERROR;
	}

	public void exportToExcel() throws IOException {
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition", "attachment; filename=USSD_Draw_Wise_Report.xls");
		PrintWriter out = response.getWriter();
		if (reportData != null) {
			reportData = reportData.replaceAll("<tbody>", "").replaceAll("</tbody>", "").trim();
			reportData =reportData.replaceAll("<br>", "").replaceAll("</br>", "").trim();
			reportData =reportData.replaceAll("</div>", "</div></br>").trim();
			out.write(reportData);
		}

		out.flush();
		out.close();
	}
}