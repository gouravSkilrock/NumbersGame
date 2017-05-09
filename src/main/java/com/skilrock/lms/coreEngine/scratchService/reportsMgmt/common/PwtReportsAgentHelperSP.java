package com.skilrock.lms.coreEngine.scratchService.reportsMgmt.common;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.skilrock.lms.beans.DateBeans;
import com.skilrock.lms.beans.PwtReportBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;
import com.skilrock.lms.common.utility.FormatNumber;

public class PwtReportsAgentHelperSP implements IPwtReportsAgentHelper {

	private int agentOrgId = -1;
	private int agentUserId = -1;
	private Connection con = null;
	private Date endDate = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	private Date startDate = null;

	public PwtReportsAgentHelperSP(UserInfoBean userbean, DateBeans dateBeans) {
		this.agentUserId = userbean.getUserId();
		this.agentOrgId = userbean.getUserOrgId();
		this.startDate = dateBeans.getFirstdate();
		this.endDate = dateBeans.getLastdate();
		System.out.println(this.startDate + "  " + this.endDate);
	}

	public PwtReportBean getBOPwtDetail(Connection conn) {
		PwtReportBean bean = null;
		try {
			CallableStatement pst = conn.prepareCall("{call getAgentScratchPwtDetails(?,?,?)}");
			pst.setInt(1, agentOrgId);
			pst.setDate(2, startDate);
			pst.setDate(3, endDate);
			System.out.println("get bo pwt === " + pst);
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				bean = new PwtReportBean();
				bean.setPartyName("BO PWT");
				bean.setPwtAmt(FormatNumber.formatNumber(rs
						.getDouble("total_pwt_amt")));
				if (Double.parseDouble(bean.getPwtAmt()) == 0.0) {
					bean = null;
				}
			}
			rs.close();
			pst.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return bean;
	}

	public List<PwtReportBean> getPwtDetail() {
		List<PwtReportBean> list = new ArrayList<PwtReportBean>();
		PwtReportBean reportbean = null;
		con = DBConnect.getConnection();
		// used to add a row of total player PWT amount
		PwtReportBean bopwtdetail = getBOPwtDetail(con);
		list.add(bopwtdetail);
		try {
			// get agent player PWT details
			pstmt = con.prepareStatement(QueryManager
					.getST_PWT_PLR_REPORT_AGENT());
			pstmt.setInt(1, agentOrgId);
			pstmt.setDate(2, startDate);
			pstmt.setDate(3, endDate);
			rs = pstmt.executeQuery();
			System.out.println(" get Player pwt detail query == " + pstmt);
			while (rs.next()) {
				reportbean = new PwtReportBean();
				double pwtAmt = rs.getDouble("total_pwt_amt");
				reportbean.setPwtAmt(FormatNumber.formatNumber(pwtAmt));
				reportbean.setPartyName(rs.getString("name"));
				if (pwtAmt > 0) {
					list.add(reportbean);
				}
			}
			
			String orgCode ="bb.name orgCode";
			if ((LMSFilterDispatcher.orgFieldType).equalsIgnoreCase("CODE")) {
				orgCode = " bb.org_code orgCode ";
			

			} else if ((LMSFilterDispatcher.orgFieldType)
					.equalsIgnoreCase("CODE_NAME")) {
				orgCode = " concat(bb.org_code,'_',bb.name)  orgCode  ";
			
				
				
			} else if ((LMSFilterDispatcher.orgFieldType)
					.equalsIgnoreCase("NAME_CODE")) {
				orgCode = " concat(bb.name,'_',bb.org_code)  orgCode  ";
			
				
			}
			// get retailer PWT Details
			String query ="select "+orgCode+", ifnull(sum(apwt.pwt_amt),0) total_pwt_amt   from st_se_agent_pwt apwt, st_lms_agent_transaction_master atm, st_lms_organization_master bb where atm.transaction_id=apwt.transaction_id and bb.organization_id = apwt.retailer_org_id and atm.user_org_id=? and ( atm.transaction_date>=? and atm.transaction_date<?) group by retailer_org_id order by "+QueryManager.getAppendOrgOrder();
			pstmt = con.prepareStatement(query);
			pstmt.setInt(1, agentOrgId);
			pstmt.setDate(2, startDate);
			pstmt.setDate(3, endDate);
			rs = pstmt.executeQuery();
			System.out.println(" get pwt detail query == " + pstmt);
			while (rs.next()) {
				reportbean = new PwtReportBean();
				double pwtAmt = rs.getDouble("total_pwt_amt");
				reportbean.setPwtAmt(FormatNumber.formatNumber(pwtAmt));
				reportbean.setPartyName("Retailer : " + rs.getString("orgCode"));
				if (pwtAmt > 0) {
					list.add(reportbean);
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (con != null && !con.isClosed()) {
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return list;
	}

}
