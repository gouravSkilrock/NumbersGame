package com.skilrock.lms.coreEngine.reportsMgmt.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.beans.DateBeans;
import com.skilrock.lms.beans.RetActivityBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;
import com.skilrock.lms.web.drawGames.common.Util;

public class RGReportHelper {
	static Log logger = LogFactory.getLog(RGReportHelper.class);
	private Connection con = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;

	public String getActiveRGLimits(String type) {
		String html = "";
		con = DBConnect.getConnection();
		try {
			pstmt = con
					.prepareStatement("select criteria from st_lms_rg_criteria_limit where crit_status = 'ACTIVE' and criteria_type = ? and organization_type = 'RETAILER' order by criteria");
			if (type.equalsIgnoreCase("all")) {
				pstmt.setString(1, "DAILY");
			} else {
				pstmt.setString(1, type);
			}
			rs = pstmt.executeQuery();
			int i = 0;
			while (rs.next()) {
				if (i == 5) {
					html += "<br/><br/>";
				}
				html += "<input type=\"checkbox\" checked=\"checked\" onclick=\"return toggleLimitDisplay()\" name=\""
						+ rs.getString("criteria")
						+ "\" value=\""
						+ rs.getString("criteria")
						+ "\" />&nbsp;"
						+ rs.getString("criteria").toUpperCase() + "&nbsp&nbsp";
				i++;
			}

		} catch (SQLException e) {
			logger.error("Exception: " + e);
			e.printStackTrace();
		} finally {
			try {

				if (con != null && !con.isClosed()) {
					con.close();
				}
			} catch (SQLException e) {
				logger.error("Exception: " + e);
				e.printStackTrace();
			}
		}
		return html;
	}

	public Map<String, Double> getRGLimitsMap(String filter) {
		Map<String, Double> map = new LinkedHashMap<String, Double>();
		con = DBConnect.getConnection();
		try {
			pstmt = con
					.prepareStatement("select criteria, criteria_limit from st_lms_rg_criteria_limit where crit_status = 'ACTIVE' and criteria_type = ? and organization_type = 'RETAILER' order by criteria");
			if (!filter.equalsIgnoreCase("All")) {
				pstmt.setString(1, filter);
			} else {
				pstmt.setString(1, "Daily");
			}
			rs = pstmt.executeQuery();
			logger.debug(" get RG limit amount query- ==== -" + pstmt);
			while (rs.next()) {
				map.put(rs.getString("criteria"), rs
						.getDouble("criteria_limit"));
			}
		} catch (SQLException e) {
			logger.error("Exception: " + e);
			e.printStackTrace();
		} finally {
			try {

				if (con != null && !con.isClosed()) {
					con.close();
				}
			} catch (SQLException e) {
				logger.error("Exception: " + e);
				e.printStackTrace();
			}
		}
		return map;
	}

	public List<RetActivityBean> getRGRepMap(String filter, int agtOrgId,
			DateBeans dateBeans) {
		List<RetActivityBean> repList = new ArrayList<RetActivityBean>();
		Map<String, Double> limitMap = new LinkedHashMap<String, Double>();
		Map<String, Double> retCritAmtMap = null;
		limitMap = getRGLimitsMap(filter);
		con = DBConnect.getConnection();
		try {
			
			String orgCodeQry = " rom.name orgCode,aom.name parentorgCode ";

			
			if ((LMSFilterDispatcher.orgFieldType).equalsIgnoreCase("CODE")) {
				orgCodeQry = " rom.org_code orgCode,aom.org_code parentorgCode ";
	

			} else if ((LMSFilterDispatcher.orgFieldType)
					.equalsIgnoreCase("CODE_NAME")) {
				orgCodeQry = " concat(rom.org_code,'_',rom.name)  orgCode,concat(aom.org_code,'_',aom.name)  parentorgCode ";
			

			} else if ((LMSFilterDispatcher.orgFieldType)
					.equalsIgnoreCase("NAME_CODE")) {
				orgCodeQry = " concat(rom.name,'_',rom.org_code)  orgCode,concat(aom.name,'_',aom.org_code)  parentorgCode ";
			

			}
			String appendOrder ="orgCode ASC ";
			
				if( (LMSFilterDispatcher.orgOrderBy).equalsIgnoreCase("ORG_ID")){
					appendOrder="retId";
					
				}else if( (LMSFilterDispatcher.orgOrderBy).equalsIgnoreCase("DESC")){
					
					appendOrder="orgCode DESC ";
				}
			
			if (filter.equalsIgnoreCase("Daily")) {
				if (agtOrgId == -1) {
					pstmt = con
							.prepareStatement("select rom.organization_id as retId, "+orgCodeQry+", rom.city,"
									+ Util.convertCollToStr(limitMap.keySet())
									+ " from st_lms_organization_master rom, st_lms_organization_master aom, st_lms_rg_org_daily_tx dtx where rom.organization_type = 'RETAILER' and aom.organization_status != 'TERMINATE' and  aom.organization_type = 'AGENT' and rom.parent_id = aom.organization_id and rom.organization_id = dtx.organization_id order by "+appendOrder);
				} else {
					pstmt = con
							.prepareStatement("select rom.organization_id as retId, "+orgCodeQry+", rom.city,"
									+ Util.convertCollToStr(limitMap.keySet())
									+ " from st_lms_organization_master rom, st_lms_organization_master aom, st_lms_rg_org_daily_tx dtx where rom.organization_type = 'RETAILER' and aom.organization_type = 'AGENT' and aom.organization_id = ? and rom.parent_id = aom.organization_id and rom.organization_id = dtx.organization_id order by "+appendOrder);
					pstmt.setInt(1, agtOrgId);
				}
			} else if (filter.equalsIgnoreCase("Weekly")) {
				if (agtOrgId == -1) {
					pstmt = con
							.prepareStatement("select rom.organization_id as retId,  "+orgCodeQry+",  rom.city,"
									+ Util.convertCollToStr(limitMap.keySet())
									+ " from st_lms_organization_master rom, st_lms_organization_master aom, st_lms_rg_org_weekly_tx dtx where rom.organization_type = 'RETAILER' and aom.organization_status != 'TERMINATE' and aom.organization_type = 'AGENT' and rom.parent_id = aom.organization_id and rom.organization_id = dtx.organization_id  order by "+appendOrder);
				} else {
					pstmt = con
							.prepareStatement("select rom.organization_id as retId,  "+orgCodeQry+",  rom.city,"
									+ Util.convertCollToStr(limitMap.keySet())
									+ " from st_lms_organization_master rom, st_lms_organization_master aom, st_lms_rg_org_weekly_tx dtx where rom.organization_type = 'RETAILER' and aom.organization_type = 'AGENT' and aom.organization_id = ? and rom.parent_id = aom.organization_id and rom.organization_id = dtx.organization_id   order by "+appendOrder);
					pstmt.setInt(1, agtOrgId);
				}
			} else if (filter.equalsIgnoreCase("All")) {
				if (dateBeans != null) {
					String[] sb = Util.convertCollToStr(limitMap.keySet())
							.split(",");
					int i = 0;
					StringBuilder st = new StringBuilder();
					while (i < sb.length) {
						st.append(", ifnull(sum(" + sb[i] + "),0) as x_"
								+ sb[i].trim());
						i++;
					}
					if (agtOrgId == -1) {
						pstmt = con
								.prepareStatement("select rom.organization_id as retId,"+orgCodeQry+",  rom.city"
										+ st.toString()
										+ " from st_lms_organization_master rom, st_lms_organization_master aom, st_lms_rg_org_daily_tx_history dtx where dtx.date>=? and dtx.date<? and rom.organization_type = 'RETAILER' and aom.organization_status != 'TERMINATE' and aom.organization_type = 'AGENT' and rom.parent_id = aom.organization_id and rom.organization_id = dtx.organization_id group by dtx.organization_id order by "+appendOrder);
						pstmt.setDate(1, dateBeans.getFirstdate());
						pstmt.setDate(2, dateBeans.getLastdate());
					} else {
						pstmt = con
								.prepareStatement("select rom.organization_id as retId, "+orgCodeQry+",  rom.city"
										+ st.toString()
										+ " from st_lms_organization_master rom, st_lms_organization_master aom, st_lms_rg_org_daily_tx_history dtx where dtx.date>=? and dtx.date<? and rom.organization_type = 'RETAILER' and aom.organization_type = 'AGENT' and aom.organization_id = ? and rom.parent_id = aom.organization_id and rom.organization_id = dtx.organization_id group by dtx.organization_id  order by "+appendOrder);
						pstmt.setDate(1, dateBeans.getFirstdate());
						pstmt.setDate(2, dateBeans.getLastdate());
						pstmt.setInt(3, agtOrgId);
					}
				}
			}
			logger.debug("RG Limit Values Query: " + pstmt);
			rs = pstmt.executeQuery();
			Iterator<Map.Entry<String, Double>> it = null;
			RetActivityBean tempBean = new RetActivityBean();
			while (rs.next()) {
				it = limitMap.entrySet().iterator();
				tempBean = new RetActivityBean();
				tempBean.setRetOrgId(rs.getInt("retId"));
				tempBean.setRetName(rs.getString("orgCode"));
				tempBean.setRetParentName(rs.getString("parentorgCode"));
				tempBean.setLocation(rs.getString("city"));
				retCritAmtMap = new LinkedHashMap<String, Double>();
				while (it.hasNext()) {
					Map.Entry<String, Double> pair = it.next();
					if (!filter.equalsIgnoreCase("All")) {
						retCritAmtMap.put(pair.getKey(), rs.getDouble(pair
								.getKey()));
					} else {
						retCritAmtMap.put(pair.getKey(), rs.getDouble("x_"
								+ pair.getKey()));
					}
				}
				tempBean.setCritMap(retCritAmtMap);
				repList.add(tempBean);
			}
			return repList;
		} catch (SQLException e) {
			logger.error("Exception: " + e);
			e.printStackTrace();
		} finally {
			try {

				if (con != null && !con.isClosed()) {
					con.close();
				}
			} catch (SQLException e) {
				logger.error("Exception: " + e);
				e.printStackTrace();
			}
		}
		return repList;
	}
}
