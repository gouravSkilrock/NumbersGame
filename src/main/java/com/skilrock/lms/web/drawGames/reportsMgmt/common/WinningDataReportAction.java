package com.skilrock.lms.web.drawGames.reportsMgmt.common;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.opensymphony.xwork2.ActionContext;
import com.skilrock.lms.beans.ReportStatusBean;
import com.skilrock.lms.common.BaseAction;
import com.skilrock.lms.coreEngine.drawGames.drawMgmt.javaBeans.WinningDataReportBean;
import com.skilrock.lms.coreEngine.drawGames.reportMgmt.controllerImpl.WinningDataReportControllerImpl;
import com.skilrock.lms.dge.beans.GameMasterLMSBean;
import com.skilrock.lms.web.drawGames.common.Util;
import com.skilrock.lms.web.reportsMgmt.common.ReportUtility;

public class WinningDataReportAction extends BaseAction {
	private static final long serialVersionUID = 1L;

	private String archDate;
	private Map<Integer, GameMasterLMSBean> gameMap = new HashMap<Integer, GameMasterLMSBean>();
	private int gameId;
	private String drawDate;

	public WinningDataReportAction() {
		super(WinningDataReportAction.class);
	}

	public String getArchDate() {
		return archDate;
	}

	public void setArchDate(String archDate) {
		this.archDate = archDate;
	}

	public Map<Integer, GameMasterLMSBean> getGameMap() {
		return gameMap;
	}

	public void setGameMap(Map<Integer, GameMasterLMSBean> gameMap) {
		this.gameMap = gameMap;
	}

	public int getGameId() {
		return gameId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public String getDrawDate() {
		return drawDate;
	}

	public void setDrawDate(String drawDate) {
		this.drawDate = drawDate;
	}

	private void setGameMap() {
		Integer gameId = 0;
		GameMasterLMSBean bean = null;
		Set<Map.Entry<Integer, GameMasterLMSBean>> entrySet = Util.getGameMap().entrySet();
		for(Map.Entry<Integer, GameMasterLMSBean> entry : entrySet) {
			gameId = entry.getKey();
			bean = entry.getValue();
			if("DGRaffle".equals(bean.getGameNameDev())) {
				continue;
			}
			gameMap.put(gameId, bean);
		}
	}

	public String winningDataReportMenu() throws Exception {
		String actionName = ActionContext.getContext().getName();
		ReportStatusBean reportStatusBean = ReportUtility.getReportStatus(actionName);
		SimpleDateFormat dateFormat = null;
		if("SUCCESS".equals(reportStatusBean.getReportStatus())) {
			dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date archivingDate = dateFormat.parse(WinningDataReportControllerImpl.getSingleInstance().getArchiveDate(reportStatusBean));
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(archivingDate);
			calendar.add(Calendar.DATE, 1); 
			archDate = dateFormat.format(calendar.getTime());
			setGameMap();
		} else {
			return "RESULT_TIMING_RESTRICTION";
		}

		return SUCCESS;
	}

	public String winningDataReportExport() {
		List<WinningDataReportBean> winningDataList = null;
		PrintWriter out = null;
		try {
			String actionName = ActionContext.getContext().getName();
			ReportStatusBean reportStatusBean = ReportUtility.getReportStatus(actionName);

			if("SUCCESS".equals(reportStatusBean.getReportStatus())) {
				winningDataList = WinningDataReportControllerImpl.getSingleInstance().getWinningData(gameId, drawDate);
				if(winningDataList != null && winningDataList.size()>0) {
					response.setContentType("application/vnd.ms-excel");
					response.setHeader("Content-Disposition", "attachment; filename="+drawDate+"_win_report.xls");
					out = response.getWriter();
					StringBuilder responseMessage = new StringBuilder();
					responseMessage.append("<table border=1px><tbody>");
					responseMessage.append("<tr><th>S.No</th><th>Draw Number</th><th>Draw Date</th><th>Ticket Number</th><th>Stake</th><th>Amount</th><th>Prize</th></tr>");
					int count = 1;
					for(WinningDataReportBean reportBean : winningDataList) {
						responseMessage.append("<tr><td>").append(count++).append("</td>");
						responseMessage.append("<td>").append(reportBean.getEventId()).append("</td>");
						responseMessage.append("<td>").append(reportBean.getDrawDateTime()).append("</td>");
						responseMessage.append("<td>").append(reportBean.getTicketNumber()).append("</td>");
						responseMessage.append("<td>").append(reportBean.getStakeData()).append("</td>");
						responseMessage.append("<td>").append(reportBean.getPurchaseAmount()).append("</td>");
						responseMessage.append("<td>").append(reportBean.getWinningAmount()).append("</td></tr>");
					}
					responseMessage.append("</tbody></table>");
					out.write(responseMessage.toString());
					out.flush();
					out.close();
				} else {
					addActionError("Data Not Available.");
					setGameMap();
				}
			} else {
				return "RESULT_TIMING_RESTRICTION";
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return SUCCESS;
	}
}