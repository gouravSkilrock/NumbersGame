package com.skilrock.lms.coreEngine.reportsMgmt.common;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.skilrock.lms.beans.DateBeans;
import com.skilrock.lms.beans.PwtReportBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.utility.FormatNumber;

public class DGPwtReportsRetHelper {

	private Connection con = null;
	private Date endDate = null;
	private int parOrgId = -1;
	private PreparedStatement pstmt = null;
	private int retOrgId = -1;
	private ResultSet rs = null;
	private Date startDate = null;

	public DGPwtReportsRetHelper(UserInfoBean userbean, DateBeans dateBeans) {
		this.parOrgId = userbean.getParentOrgId();
		this.retOrgId = userbean.getUserOrgId();
		this.startDate = dateBeans.getFirstdate();
		this.endDate = dateBeans.getLastdate();
		System.out.println(this.startDate + "  " + this.endDate);
	}

	private PwtReportBean getAGTPwtDetail(Connection conn) {
		PwtReportBean bean = null;
		try {
			String getAgtPwtDetQuery = "select retailer_org_id ,bb.name, ifnull(sum(apwt.pwt_amt),0) "
					+ " total_pwt_amt from st_dg_agt_pwt apwt, st_lms_agent_transaction_master atm, "
					+ " st_lms_organization_master bb where atm.transaction_id=apwt.transaction_id and "
					+ " bb.organization_id = apwt.retailer_org_id and atm.user_org_id=? and retailer_org_id=? "
					+ " and ( atm.transaction_date>=? and atm.transaction_date<?) group by retailer_org_id "
					+ " order by name";
			PreparedStatement pst = conn.prepareStatement(getAgtPwtDetQuery);
			pst.setInt(1, parOrgId);
			pst.setInt(2, retOrgId);
			pst.setDate(3, startDate);
			pst.setDate(4, endDate);
			System.out.println(" get pwt detail query == " + pst);
			ResultSet rss = pst.executeQuery();

			while (rss.next()) {
				bean = new PwtReportBean();
				double pwtAmt = rss.getDouble("total_pwt_amt");
				bean.setPwtAmt(FormatNumber.formatNumber(pwtAmt));
				bean.setPartyName("To Agent PWT");
				if (Double.parseDouble(bean.getPwtAmt()) == 0.0) {
					bean = null;
				}
			}

			rss.close();
			pst.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return bean;
	}

	public Map<String, List<PwtReportBean>> getPwtDetail() {

		Map<String, List<PwtReportBean>> pwtMap = new TreeMap<String, List<PwtReportBean>>();

		List<PwtReportBean> list = new ArrayList<PwtReportBean>();
		PwtReportBean reportbean = null;
		con = DBConnect.getConnection();

		try {
			// get Retailer's player PWT details
			String getDirPlrPwtDetailQuery = "select 'Direct Player' as name, ifnull(sum(pwt_amt),0) "
					+ " total_pwt_amt from st_dg_ret_direct_plr_pwt aa, st_lms_retailer_transaction_master atm "
					+ " where aa.transaction_id = atm.transaction_id and  atm.retailer_org_id = ? and "
					+ "( atm.transaction_date>=? and atm.transaction_date<?) ";
			pstmt = con.prepareStatement(getDirPlrPwtDetailQuery);
			pstmt.setInt(1, retOrgId);
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
			// get PWT Details for Retailer
			String getPwtDetailForRetQuery = "select ifnull(sum(rpwt.pwt_amt),0) total_pwt_amt  "
					+ " from st_dg_ret_pwt_? rpwt, st_lms_retailer_transaction_master atm where atm.transaction_id "
					+ " = rpwt.transaction_id and atm.retailer_org_id=? and ( atm.transaction_date>=? "
					+ " and atm.transaction_date<?) ";
			pstmt = con.prepareStatement(getPwtDetailForRetQuery);

			String fetchDrawGameListQuery = "select distinct ret.game_id, gm.game_nbr, gm.game_name, "
					+ "  retailer_org_id from st_lms_retailer_transaction_master ret, st_dg_game_master gm where "
					+ " transaction_type = 'DG_PWT' and gm.game_id = ret.game_id and "
					+ " retailer_org_id = ? and ( transaction_date>=? and transaction_date<?) ";

			PreparedStatement fetchDrawGameListPstmt = con
					.prepareStatement(fetchDrawGameListQuery);
			fetchDrawGameListPstmt.setInt(1, retOrgId);
			fetchDrawGameListPstmt.setDate(2, startDate);
			fetchDrawGameListPstmt.setDate(3, endDate);
			System.out.println("fetchDrawGameListPstmt = "
					+ fetchDrawGameListPstmt);
			ResultSet rss = fetchDrawGameListPstmt.executeQuery();

			int gameId = -1, gameNbr = -1;
			String gameName = null;
			;
			double sumPwtAmt = 0.0;
			reportbean = new PwtReportBean();

			while (rss.next()) {
				gameId = rss.getInt("game_id");
				gameNbr = rss.getInt("game_nbr");
				gameName = gameNbr + "-" + rss.getString("game_name");

				pstmt.setInt(1, gameNbr);
				pstmt.setInt(2, retOrgId);
				pstmt.setDate(3, startDate);
				pstmt.setDate(4, endDate);
				System.out.println(" Anonymous pwt detail Query == " + pstmt);
				rs = pstmt.executeQuery();

				while (rs.next()) {

					double pwtAmt = rs.getDouble("total_pwt_amt");
					sumPwtAmt += pwtAmt;
				}
			}
			if (sumPwtAmt > 0) {
				reportbean
						.setPwtAmt(FormatNumber.formatNumberForJSP(sumPwtAmt));
				reportbean.setPartyName("Anonymous Player");
				list.add(reportbean);
			}
			pwtMap.put("plrPwtDtlList", list);

			// used to add a row of total player PWT amount
			PwtReportBean agtpwtdetail = getAGTPwtDetail(con);
			List<PwtReportBean> agtList = new ArrayList<PwtReportBean>();
			agtList.add(agtpwtdetail);
			pwtMap.put("agtPwtDtlList", agtList);

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

		return pwtMap;
	}

}
