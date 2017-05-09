package com.skilrock.lms.coreEngine.reportsMgmt.common;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.skilrock.lms.beans.SecurityDepositAndLevyBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.utility.FormatNumber;

public class SecurityDepositAndLevyReportHelper {
	public List<SecurityDepositAndLevyBean> fetchReportData(int agtOrgId) {
		List<SecurityDepositAndLevyBean> list = new ArrayList<SecurityDepositAndLevyBean>();
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";
		try {
			SecurityDepositAndLevyBean bean;
			con = DBConnect.getConnection();
			stmt = con.createStatement();
			if (agtOrgId == -1) {
				query = "select agent_name,ret_name,initial_security_deposit,expected_security_deposit,collected_security_deposit,levy_cat_type from (select concat(a.org_code,'_',a.name) agent_name,concat(b.org_code,'_',b.name) ret_name,b.organization_id from  st_lms_organization_master a inner join st_lms_organization_master b on a.organization_id=b.parent_id where b.organization_type='RETAILER') org_master left join st_lms_organization_security_levy_master levy_master on org_master.organization_id=levy_master.organization_id";
			} else {
				query = "select agent_name,ret_name,initial_security_deposit,expected_security_deposit,collected_security_deposit,levy_cat_type from (select concat(a.org_code,'_',a.name) agent_name,concat(b.org_code,'_',b.name) ret_name,b.organization_id from  st_lms_organization_master a inner join st_lms_organization_master b on a.organization_id=b.parent_id where b.organization_type='RETAILER' AND b.parent_id="+agtOrgId+") org_master left join st_lms_organization_security_levy_master levy_master on org_master.organization_id=levy_master.organization_id";
			}
			
			rs = stmt.executeQuery(query);
			while (rs.next()) {
				bean = new SecurityDepositAndLevyBean();
				bean.setAgentName(rs.getString("agent_name"));
				bean.setRetailerName(rs.getString("ret_name"));
				bean.setInitialSD(FormatNumber.formatNumber(rs.getDouble("initial_security_deposit")));
				bean.setCollectedSD(FormatNumber.formatNumber(rs.getDouble("collected_security_deposit")));
				bean.setExpectedSD(FormatNumber.formatNumber(rs.getDouble("expected_security_deposit")));
				bean.setLevyRate(("CAT-1").equals(rs.getString("levy_cat_type")) ? "1%" : "5%");
				bean.setRemainingSD(FormatNumber.formatNumber(rs.getDouble("expected_security_deposit")- rs.getDouble("collected_security_deposit")));
				list.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnect.closeConnection(con, stmt, rs);
		}
		return list;
	}
}
