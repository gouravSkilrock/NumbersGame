package com.skilrock.lms.web.drawGames.reportsMgmt.common;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.common.BaseAction;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.drawGames.reportMgmt.BlockTicketsReportHelper;
import com.skilrock.lms.dge.beans.BlockTicketUserBean;
import com.skilrock.lms.web.drawGames.common.Util;
import com.skilrock.lms.web.reportsMgmt.common.ReportUtility;

public class BlockTicketsReportAction extends BaseAction{
	public BlockTicketsReportAction() {
		super(BlockTicketsReportAction.class);
		// TODO Auto-generated constructor stub
	}

	private static final long serialVersionUID = 1L;

	Log logger = LogFactory.getLog(BlockTicketsReportAction.class);
	private String end_Date;
	private String start_date;
	private int gameId;
	private int gameName;
	public String reportData;
    public HashMap<Integer,String> drawGameList;
	private List<BlockTicketUserBean> blockTicketList;

	public String menu() throws Exception {
		
				drawGameList = ReportUtility.fetchDrawDataMenu();
		return SUCCESS;
	}
	public String search() throws LMSException {
		logger.info("--Block Tickets Report Search ----" + gameId + "---");
		BlockTicketsReportHelper helper = new BlockTicketsReportHelper();
		try {
			if (start_date != null && end_Date != null && gameId > 0) {
				blockTicketList = helper.fetchBlockTickets(start_date + " 00:00:00",
						end_Date + " 23:59:59", gameId);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	public void exportExcel() throws IOException {
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition",
					"attachment; filename=BlockTicketReport.xls");
			PrintWriter out = response.getWriter();
			if (reportData != null) {
				reportData = reportData.replaceAll("<tbody>", "").replaceAll(
						"</tbody>", "").trim();
				//reportData="Block Ticket Report From "+start_date+" To "+end_Date+"\n"+reportData;
				out.write(reportData);
			}
			out.flush();
			out.close();
		}
	

	public HashMap<Integer, String> getDrawGameList() {
		return drawGameList;
	}

	public void setDrawGameList(HashMap<Integer, String> drawGameList) {
		this.drawGameList = drawGameList;
	}

	public String getEnd_Date() {
		return end_Date;
	}

	public void setEnd_Date(String endDate) {
		end_Date = endDate;
	}


	public String getStart_date() {
		return start_date;
	}

	public void setStart_date(String startDate) {
		start_date = startDate;
	}


	public int getGameId() {
		return gameId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	

	public int getGameName() {
		return gameName;
	}

	public void setGameName(int gameName) {
		this.gameName = gameName;
	}



	public List<BlockTicketUserBean> getBlockTicketList() {
		return blockTicketList;
	}

	public void setBlockTicketList(List<BlockTicketUserBean> blockTicketList) {
		this.blockTicketList = blockTicketList;
	}
	public String getReportData() {
		return reportData;
	}

	public void setReportData(String reportData) {
		this.reportData = reportData;
	}

}
