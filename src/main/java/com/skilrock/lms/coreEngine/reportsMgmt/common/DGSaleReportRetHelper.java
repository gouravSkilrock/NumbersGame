package com.skilrock.lms.coreEngine.reportsMgmt.common;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.beans.DGSaleReportBean;
import com.skilrock.lms.beans.DateBeans;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.db.DBConnect;

public class DGSaleReportRetHelper {
	private Connection con = null;
	private Date endDate;

	Log logger = LogFactory.getLog(DGSaleReportRetHelper.class);
	private int retOrgId;
	private Date startDate;

	public DGSaleReportRetHelper(UserInfoBean userInfoBean, DateBeans dateBeans) {
		this.startDate = dateBeans.getFirstdate();
		this.endDate = dateBeans.getLastdate();
		this.retOrgId = userInfoBean.getUserOrgId();
		logger.debug("collecting dates : first --- " + startDate
				+ "  last date -- " + endDate);
	}

	public List<DGSaleReportBean> getDGSaleDetailGameWise() {
		List<DGSaleReportBean> list = new ArrayList<DGSaleReportBean>();
		DGSaleReportBean reportbean = null;
		con = DBConnect.getConnection();

		String gameWiseReportForRet = "select sum_sale_mrp, sum_refund_mrp from ((select ifnull(sum(mrp_amt),0) "
				+ " 'sum_sale_mrp' from st_lms_retailer_transaction_master btm, st_dg_ret_sale_? bo where "
				+ " btm.transaction_id=bo.transaction_id and btm.transaction_type ='DG_SALE' and "
				+ " btm.retailer_org_id =? and ( btm.transaction_date>=? and btm.transaction_date<?) and btm.game_id =?) sale, "
				+ "(select ifnull(sum(mrp_amt), 0) 'sum_refund_mrp' from st_lms_retailer_transaction_master btm,"
				+ " st_dg_ret_sale_refund_? bo where btm.transaction_id=bo.transaction_id and "
				+ " (btm.transaction_type ='DG_REFUND_CANCEL' or btm.transaction_type ='DG_REFUND_FAILED') "
				+ " and btm.retailer_org_id =? and ( btm.transaction_date>=? and btm.transaction_date<?) "
				+ " and btm.game_id =?) refund)";
		try {

			String fetchDrawGameListQuery = "select distinct ret.game_id, gm.game_nbr, gm.game_name, retailer_org_id "
					+ " from st_lms_retailer_transaction_master ret, st_dg_game_master gm where "
					+ " transaction_type = 'DG_SALE' and gm.game_id = ret.game_id and "
					+ " retailer_org_id = ? and ( transaction_date>=? and transaction_date<?) ";

			PreparedStatement fetchDrawGameListPstmt = con
					.prepareStatement(fetchDrawGameListQuery);
			fetchDrawGameListPstmt.setInt(1, retOrgId);
			fetchDrawGameListPstmt.setDate(2, startDate);
			fetchDrawGameListPstmt.setDate(3, endDate);

			ResultSet rss = fetchDrawGameListPstmt.executeQuery();
			int gameId = -1, gameNbr = -1;
			String gameName = null;
			;

			while (rss.next()) {
				gameId = rss.getInt("game_id");
				gameNbr = rss.getInt("game_nbr");
				gameName = gameNbr + "-" + rss.getString("game_name");

				PreparedStatement pstmt = con
						.prepareStatement(gameWiseReportForRet);
				pstmt.setInt(1, gameNbr);
				pstmt.setInt(2, retOrgId);
				pstmt.setDate(3, startDate);
				pstmt.setDate(4, endDate);
				pstmt.setInt(5, gameId);

				pstmt.setInt(6, gameNbr);
				pstmt.setInt(7, retOrgId);
				pstmt.setDate(8, startDate);
				pstmt.setDate(9, endDate);
				pstmt.setInt(10, gameId);

				logger.debug("query -- " + pstmt);

				ResultSet rs = pstmt.executeQuery();
				while (rs.next()) {
					reportbean = new DGSaleReportBean();
					reportbean.setGameName(gameName);
					reportbean.setSumRefundMrp(rs.getDouble("sum_refund_mrp"));
					reportbean.setSumSaleMrp(rs.getDouble("sum_sale_mrp"));
					list.add(reportbean);
				}
				rs.close();
				pstmt.close();
				logger.debug(list);
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
