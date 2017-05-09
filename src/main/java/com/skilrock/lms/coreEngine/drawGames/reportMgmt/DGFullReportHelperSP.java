package com.skilrock.lms.coreEngine.drawGames.reportMgmt;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.beans.DateBeans;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;

import com.skilrock.lms.web.reportsMgmt.common.ReportUtility;

import freemarker.template.utility.Execute;

public class DGFullReportHelperSP implements IDGFullReportHelper{
	static Log logger = LogFactory.getLog(DGSaleReportsHelper.class);

	private Date endDate;
	private Date startDate;

	public DGFullReportHelperSP(DateBeans dateBeans) {
		this.startDate = dateBeans.getFirstdate();
		this.endDate = dateBeans.getLastdate();
		logger.debug(this.startDate + "  " + this.endDate);
	}

	public Map<String, String> fetchDGFullReport(String cityCode, String stateCode) throws LMSException {
		Connection con = null;
		Map repMap = new LinkedHashMap();
		con = DBConnect.getConnection();
		Statement stmt=null;
		CallableStatement cstmt = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ResultSet rsGame = null ;
		HashMap saleList = null;
		HashMap pwtList = null;
		HashMap saleListIW = null;
		HashMap pwtListIW = null;
		HashMap directPlrList = null;
		List completeList = null;
		try {
			String orgCodeQry = " name orgCode  ";

			
			if ((LMSFilterDispatcher.orgFieldType).equalsIgnoreCase("CODE")) {
				orgCodeQry = " org_code orgCode ";
	

			} else if ((LMSFilterDispatcher.orgFieldType)
					.equalsIgnoreCase("CODE_NAME")) {
				orgCodeQry = " concat(org_code,'_',name)  orgCode ";
			

			} else if ((LMSFilterDispatcher.orgFieldType)
					.equalsIgnoreCase("NAME_CODE")) {
				orgCodeQry = " concat(name,'_',org_code)  orgCode ";
			

			}	
			Map<Integer, String> gameMap=ReportUtility.getActiveGameNumMap(startDate.toString());
			
			String locAppender = ""; //" and sm.state_code = '"+stateCode+"' and cm.city_name = '"+cityCode+"'";
			if(!("ALL").equalsIgnoreCase(stateCode) || !("ALL").equalsIgnoreCase(cityCode)){
				if(!("ALL").equalsIgnoreCase(stateCode))
					locAppender = locAppender.concat(" and sm.state_code = '"+stateCode+"'") ;
				
				if(!("ALL").equalsIgnoreCase(stateCode))
					locAppender = locAppender.concat(" and cm.city_name = '"+cityCode+"'") ;
			}
			//pstmt = con
				//	.prepareStatement("select "+orgCodeQry+", addr_line1, addr_line2, city from st_lms_organization_master where organization_type='AGENT' order by "+QueryManager.getAppendOrgOrder());
			 pstmt = con
	            .prepareStatement("select om."+orgCodeQry+", addr_line1, addr_line2, city from st_lms_organization_master om inner join st_lms_user_master um on om.organization_id = um.organization_id inner join st_lms_state_master sm on om.state_code = sm.state_code inner join st_lms_city_master cm on om.city = cm.city_name  where om.organization_type='AGENT' "+locAppender+"  order by "+QueryManager.getAppendOrgOrder());
			rs = pstmt.executeQuery();
			while (rs.next()) {
				completeList = new ArrayList();
				completeList.add(new HashMap());
				completeList.add(new HashMap());
				completeList.add(new HashMap());
				if(ReportUtility.isIW) {
					completeList.add(new HashMap());
					completeList.add(new HashMap());
					completeList.add(new HashMap());
				}
				completeList.add(rs.getString("city"));
				repMap.put(rs.getString("orgCode"), completeList);
			}
			for(Map.Entry<Integer, String> entry: gameMap.entrySet())
			 {
				int gameId=entry.getKey();
				cstmt = con.prepareCall("{call getSaleData(?,?,?,?,?)}");
				cstmt.setDate(1, startDate);
				cstmt.setDate(2, endDate);
				cstmt.setInt(3, gameId);
				cstmt.setString(4, cityCode);
				cstmt.setString(5, stateCode);
				logger.debug(cstmt);
				rs = cstmt.executeQuery();

				while (rs.next()) {
					if(repMap.keySet().contains(rs.getString("agtName")))
					{
						saleList = (HashMap) ((ArrayList) repMap.get(rs
								.getString("agtName"))).get(0);
						saleList.put(gameId, rs
								.getDouble("netSalefinal"));	
					}
					
				}

			}
			for(Map.Entry<Integer, String> entry: gameMap.entrySet())
			 {
				int gameId=entry.getKey();
			cstmt = con.prepareCall("{call getPwtDetails(?,?,?,?,?)}");
				cstmt.setDate(1, startDate);
				cstmt.setDate(2, endDate);
				cstmt.setInt(3, gameId);
				cstmt.setString(4, cityCode);
				cstmt.setString(5, stateCode);
				rs = cstmt.executeQuery();
				logger.debug(cstmt);
				while (rs.next()) {
					if(repMap.keySet().contains(rs.getString("orgCode")))
					{
					pwtList = (HashMap) ((ArrayList) repMap.get(rs
							.getString("orgCode"))).get(1);
					pwtList.put(gameId, rs.getDouble("totPwtAgt"));
					}
				}

			}
			for(Map.Entry<Integer, String> entry: gameMap.entrySet())
			 {
				int gameId=entry.getKey();
				cstmt = con.prepareCall("{call getAgtDirPlrPwt(?,?,?,?,?)}");
				cstmt.setDate(1, startDate);
				cstmt.setDate(2, endDate);
				cstmt.setInt(3, gameId);
				cstmt.setString(4, cityCode);
				cstmt.setString(5, stateCode);
				logger.debug(cstmt);
				rs = cstmt.executeQuery();

				while (rs.next()) {
					if(repMap.keySet().contains(rs.getString("agtname")))
					{
					directPlrList = (HashMap) ((ArrayList) repMap.get(rs
							.getString("agtname"))).get(2);
					directPlrList.put(gameId, rs
							.getDouble("totDirPlrPwt"));
					}
				}

			}
			
			if (ReportUtility.isIW) {
				String gameQry = ReportUtility.getIWGameMapQuery(new Timestamp(
						startDate.getTime()));
				PreparedStatement gamePstmt = con.prepareStatement(gameQry);
				rsGame = gamePstmt.executeQuery();
				while (rsGame.next()) {
					int gameId = rsGame.getInt("game_id");
					cstmt = con.prepareCall("{call getSaleDataIW(?,?,?,?,?)}");
					cstmt.setDate(1, startDate);
					cstmt.setDate(2, endDate);
					cstmt.setInt(3, gameId);
					cstmt.setString(4, cityCode);
					cstmt.setString(5, stateCode);
					logger.debug(cstmt);
					rs = cstmt.executeQuery();

					while (rs.next()) {
						if (repMap.keySet().contains(
								rs.getString("agtName"))) {
							saleListIW = (HashMap) ((ArrayList) repMap.get(rs
									.getString("agtName"))).get(3);
							saleListIW.put(gameId, rs
									.getDouble("netSalefinal"));
						}

					}

				
					cstmt = con
							.prepareCall("{call getPwtDetailsIW(?,?,?,?,?)}");
					cstmt.setDate(1, startDate);
					cstmt.setDate(2, endDate);
					cstmt.setInt(3, gameId);
					cstmt.setString(4, cityCode);
					cstmt.setString(5, stateCode);
					rs = cstmt.executeQuery();
					logger.debug(cstmt);
					while (rs.next()) {
						if (repMap.keySet().contains(
								rs.getString("orgCode"))) {
							pwtListIW = (HashMap) ((ArrayList) repMap.get(rs
									.getString("orgCode"))).get(4);
							pwtListIW.put(gameId, rs.getDouble("totPwtAgt"));
						}
					}
					
					cstmt = con.prepareCall("{call getAgtDirPlrPwtIW(?,?,?,?,?)}");
					cstmt.setDate(1, startDate);
					cstmt.setDate(2, endDate);
					cstmt.setInt(3, gameId);
					cstmt.setString(4, cityCode);
					cstmt.setString(5, stateCode);
					logger.debug(cstmt);
					rs = cstmt.executeQuery();

					while (rs.next()) {
						if(repMap.keySet().contains(rs.getString("agtname")))
						{
						directPlrList = (HashMap) ((ArrayList) repMap.get(rs
								.getString("agtname"))).get(5);
						directPlrList.put(gameId, rs
								.getDouble("totDirPlrPwt"));
						}
					}
				}
			}

			Set<String> terminatedAgents = new HashSet<String> ();
			stmt =con.createStatement();
			String query="select name from st_lms_organization_master om inner join st_lms_user_master um on om.organization_id = um.organization_id where (termination_date <= '"+startDate+"' or registration_date>= '"+endDate+"') and om.organization_type='AGENT' ";
			rs=stmt.executeQuery(query);
			while(rs.next()){
				terminatedAgents.add(rs.getString("name"));
			}
			logger.info("Terminated Agents:"+terminatedAgents);

			repMap.keySet().removeAll(terminatedAgents);
		} catch (SQLException e) {
			logger.error("Exception: " + e);
			e.printStackTrace();
			throw new LMSException(e);
		} finally {
			try {
				if (con != null && !con.isClosed()) {
					con.close();
				}
			} catch (SQLException e) {
				logger.error("Exception: " + e);
				e.printStackTrace();
				throw new LMSException(e);
			}
		}

		return repMap;
	}




}