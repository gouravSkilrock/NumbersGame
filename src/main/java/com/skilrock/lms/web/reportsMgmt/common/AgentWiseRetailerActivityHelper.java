package com.skilrock.lms.web.reportsMgmt.common;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.beans.AgentWiseRetActivityBean;
import com.skilrock.lms.beans.GameSaleDetailsBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.web.drawGames.common.Util;


public class AgentWiseRetailerActivityHelper {
static Log logger = LogFactory.getLog(AgentWiseRetailerActivityHelper.class);
	public static void main(String[] args) {
		new AgentWiseRetailerActivityHelper().fetchRetAcitivtyData("2013-05-22");
	}
	
	
	public Map<String, AgentWiseRetActivityBean> fetchRetAcitivtyData(
			String date) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Map<String, AgentWiseRetActivityBean> retActivityMap = new LinkedHashMap<String, AgentWiseRetActivityBean>();
		try {
			con = DBConnect.getConnection();
			String query = " select agent_id,city,"
					+ QueryManager.getOrgCodeQuery()
					+ ",organization_id, status,active_Ret,newLogin_Ret,assigned_total,notAssigned_total,group_concat(game_id order by game_id asc) gameIds ,group_concat(totalSale) totalSales from st_lms_ret_activityHistory_agentwise retH"
					+ " inner join st_lms_ret_saleHistory_agentwise saleH on retH.task_id=saleH.task_id inner join st_lms_organization_master on agent_id=organization_id where date =?  group by orgCode  order by "
					+ QueryManager.getAppendOrgOrder();
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, date);
			logger.info("activity query:" + pstmt);
			rs = pstmt.executeQuery();

			DecimalFormat df = new DecimalFormat(".##");
			int totalactiveRets = 0;
			int totalNewLogin = 0;
			int totalassigned = 0;
			int totalNotassigned = 0;
			double totalPercentage = 0;
			List<GameSaleDetailsBean> totalgameSaleDataList = new ArrayList<GameSaleDetailsBean>();
			Map<Integer, GameSaleDetailsBean> totalSaleMap = new HashMap<Integer, GameSaleDetailsBean>();
			int count = 0;
			while (rs.next()) {
				count++;
				AgentWiseRetActivityBean retActivtiyBean = new AgentWiseRetActivityBean();
				int notAssgined = rs.getInt("notAssigned_total");
				int assgined = rs.getInt("assigned_total");
				int totalPos = notAssgined + assgined;
				int activeRet = rs.getInt("active_Ret");
				int newLoign = rs.getInt("newLogin_Ret");
				double activePercentage = 0;
				if (activeRet != 0 && totalPos != 0) {
					activePercentage = ((double) activeRet / totalPos) * 100;
				}

				retActivtiyBean.setActiveRet(activeRet);
				retActivtiyBean.setNewLoginRet(newLoign);
				retActivtiyBean.setNotAssignedTotal(notAssgined);
				retActivtiyBean.setAssignedTotal(assgined);
				retActivtiyBean.setOrgId(rs.getString("organization_id"));
				retActivtiyBean.setOrgStatus(rs.getString("status"));
				retActivtiyBean.setAgentName(rs.getString("orgCode"));
				retActivtiyBean.setCity(rs.getString("city"));
				retActivtiyBean.setActiveRetPercentage(Double.valueOf(df
						.format(activePercentage)));
				Blob blob = rs.getBlob("gameIds");
				Blob saleblob = rs.getBlob("totalSales");

				String[] gameArray = new String(blob.getBytes(1,
						(int) blob.length())).split(",");
				String[] saleArray = new String(saleblob.getBytes(1,
						(int) saleblob.length())).split(",");
				List<GameSaleDetailsBean> gameSaleDataList = new ArrayList<GameSaleDetailsBean>();
				for (int i = 0; i < gameArray.length; i++) {
					GameSaleDetailsBean gameDetailBean = new GameSaleDetailsBean();
					gameDetailBean.setGameName(Util.getGameDisplayName(Integer
							.parseInt(gameArray[i])));
					gameDetailBean.setGameId(Integer.parseInt(gameArray[i]));
					gameDetailBean.setTotalSale(Double
							.parseDouble(saleArray[i]));
					double avgSalePerTerminal = 0.0;
					if (activeRet != 0) {
						avgSalePerTerminal = (Double.parseDouble(saleArray[i]))
								/ activeRet;
					}

					gameDetailBean.setAvgSalePerTerminal(Double.valueOf(df
							.format(avgSalePerTerminal)));
					gameSaleDataList.add(gameDetailBean);
					if (count == 1) {
						GameSaleDetailsBean gameDetailBean1 = new GameSaleDetailsBean();
						gameDetailBean1.setAvgSalePerTerminal(gameDetailBean
								.getAvgSalePerTerminal());
						gameDetailBean1.setTotalSale(gameDetailBean
								.getTotalSale());
						gameDetailBean1.setGameId(gameDetailBean.getGameId());
						totalSaleMap.put(gameDetailBean.getGameId(),
								gameDetailBean1);
					} else {
						GameSaleDetailsBean gameDetailBean1 = totalSaleMap
								.get(gameDetailBean.getGameId());
						gameDetailBean1.setGameId(gameDetailBean.getGameId());
						gameDetailBean1.setAvgSalePerTerminal(gameDetailBean1
								.getAvgSalePerTerminal()
								+ gameDetailBean.getAvgSalePerTerminal());
						gameDetailBean1
								.setTotalSale(gameDetailBean1.getTotalSale()
										+ gameDetailBean.getTotalSale());
						totalSaleMap.put(gameDetailBean.getGameId(),
								gameDetailBean1);
					}
				}

				retActivtiyBean.setGameSaleDataList(gameSaleDataList);
				retActivityMap.put(retActivtiyBean.getOrgId(), retActivtiyBean);

				totalactiveRets = totalactiveRets + activeRet;
				totalNewLogin = totalNewLogin + newLoign;
				totalNotassigned = totalNotassigned + notAssgined;
				totalassigned = totalassigned + assgined;
				totalPercentage = totalPercentage + activePercentage;

			}
			if (count > 0) {
				AgentWiseRetActivityBean totalretActivtiyBean = new AgentWiseRetActivityBean();
				totalretActivtiyBean.setActiveRet(totalactiveRets);
				totalretActivtiyBean.setNewLoginRet(totalNewLogin);
				totalretActivtiyBean.setNotAssignedTotal(totalNotassigned);
				totalretActivtiyBean.setAssignedTotal(totalassigned);
				totalretActivtiyBean.setAgentName("Tot@l");
				totalretActivtiyBean.setActiveRetPercentage(Double.valueOf(df
						.format((totalPercentage / retActivityMap.size()))));
				for (Map.Entry<Integer, GameSaleDetailsBean> entry : totalSaleMap
						.entrySet()) {
					totalgameSaleDataList.add(entry.getValue());
				}
				totalretActivtiyBean.setGameSaleDataList(totalgameSaleDataList);
				retActivityMap.put(totalretActivtiyBean.getAgentName(),
						totalretActivtiyBean);
			}
		} catch (Exception e) {

			e.printStackTrace();
		}
		return retActivityMap;
	}
	
}
