package com.skilrock.lms.coreEngine.drawGames.reportMgmt;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.beans.DateBeans;
import com.skilrock.lms.beans.SaleReportBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.utility.FormatNumber;
import com.skilrock.lms.web.drawGames.common.Util;

public class DGSaleReportRetHelper {
	static Log logger = LogFactory.getLog(DGSaleReportRetHelper.class);
	private Connection con = null;

	private Date endDate;
	private int OrgId;
	private Date startDate;

	public DGSaleReportRetHelper(UserInfoBean userInfoBean, DateBeans dateBeans) {
		this.startDate = dateBeans.getFirstdate();
		this.endDate = dateBeans.getLastdate();
		this.OrgId = userInfoBean.getUserOrgId();
		logger.debug("collecting dates : first --- " + startDate
				+ "  last date -- " + endDate);
	}

	public List<SaleReportBean> getDGSaleDetailGameWise() {
		List<SaleReportBean> list = new ArrayList<SaleReportBean>();
		SaleReportBean reportbean = null;
		con = DBConnect.getConnection();
		try {
			List<Integer> gameNumList = Util.getGameNumberList();
			String saleCommonQry = "select rtm.game_id, ifnull(sum(rs.mrp_amt),0) as saleMrp, ifNull(sum(rs.net_amt),0) as saleNet from st_lms_retailer_transaction_master rtm inner join st_dg_ret_sale_";
			String refundCommonQry = "select rtm.game_id, ifnull(sum(rsr.mrp_amt),0) as refundMrp, ifNull(sum(rsr.net_amt),0) as refundNet from st_lms_retailer_transaction_master rtm inner join st_dg_ret_sale_refund_";
			StringBuilder saleQry = new StringBuilder("");
			StringBuilder saleRetQry = new StringBuilder("");
			for (int i = 0; i < gameNumList.size(); i++) {
				saleQry.append(saleCommonQry + gameNumList.get(i) + " rs on rtm.transaction_id = rs.transaction_id where rtm.transaction_type in ('DG_SALE', 'DG_SALE_OFFLINE') and (date(rtm.transaction_date) >= " + "\"" + startDate + "\"" + "and date(rtm.transaction_date) < " + "\"" + endDate + "\"" + ") and rtm.retailer_org_id = " + OrgId + " group by rs.game_id");
				saleRetQry.append(refundCommonQry + gameNumList.get(i) + " rsr on rtm.transaction_id = rsr.transaction_id where rtm.transaction_type in ('DG_REFUND_CANCEL','DG_REFUND_FAILED') and (date(rtm.transaction_date) >= " + "\"" + startDate + "\"" + " and date(rtm.transaction_date) < " + "\"" + endDate  + "\"" + ")" + " and rtm.retailer_org_id = " + OrgId + " group by game_id");
				if(i < gameNumList.size() -1)
				{
					saleQry.append(" union ");
					saleRetQry.append(" union ");
				}
			}
			String SaleReportGameWiseRetQuery = "select naming.game_name as game_name, fine.SaleMrp as SaleMrp, fine.RefundMrp as RefundMrp, (fine.SaleMrp - fine.RefundMrp)as NetMrp, fine.SaleNet as SaleNet, fine.RefundNet as RefundNet, (fine.SaleNet - fine.RefundNet)as NetNet from (select sale.game_id, ifnull(sale.saleMrp,0) as SaleMrp, ifnull(refund.refundMrp,0)as RefundMrp, ifnull(sale.saleNet, 0) as SaleNet, ifnull(refund.refundNet, 0) as RefundNet from ("
					+ saleQry.toString()
					+ ") as sale left outer join (" 
					+ saleRetQry.toString() 
					+ ")as refund on sale.game_id = refund.game_id)as fine,(select game_id, game_name from st_dg_game_master)as naming where fine.game_id = naming.game_id order by fine.game_id asc";
			
			Statement stmt = con
					.prepareStatement(SaleReportGameWiseRetQuery);

			logger.debug("get the DG sale detail query -- " + SaleReportGameWiseRetQuery);
			ResultSet rs = stmt.executeQuery(SaleReportGameWiseRetQuery);

			while (rs.next()) {
				reportbean = new SaleReportBean();
				reportbean.setSaleMrp(FormatNumber.formatNumber(rs
						.getDouble("SaleMrp")));
				reportbean.setSaleReturnMrp(FormatNumber.formatNumber(rs
						.getDouble("RefundMrp")));
				reportbean.setNetMrpAmt(FormatNumber.formatNumber(rs
						.getDouble("SaleMrp")
						- rs.getDouble("RefundMrp")));
				reportbean.setSaleAmt(FormatNumber.formatNumber(rs
						.getDouble("SaleNet")));
				reportbean.setReturnAmt(FormatNumber.formatNumber(rs
						.getDouble("RefundNet")));
				reportbean.setNetAmt(FormatNumber.formatNumber(rs
						.getDouble("SaleNet")
						- rs.getDouble("RefundNet")));
				reportbean.setGamename(rs.getString("game_name"));
				list.add(reportbean);
			}
			rs.close();
			stmt.close();
		}

		catch (SQLException e) {
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

		return list;
	}

}
