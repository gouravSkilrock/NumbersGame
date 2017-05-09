package com.skilrock.lms.coreEngine.drawGames.reportMgmt;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.beans.DateBeans;
import com.skilrock.lms.beans.PwtReportBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.web.drawGames.common.Util;

public class DGPwtReportRetHelper {
	static Log logger = LogFactory.getLog(DGPwtReportRetHelper.class);
	private Connection con = null;
	private Date endDate = null;
	private int OrgId;
	private Date startDate = null;

	public DGPwtReportRetHelper(UserInfoBean userInfoBean, DateBeans dateBeans) {
		this.startDate = dateBeans.getFirstdate();
		this.endDate = dateBeans.getLastdate();
		this.OrgId = userInfoBean.getUserOrgId();
		logger.debug(this.startDate + "  " + this.endDate);
	}

	/*
	 * public List<PwtReportBean> getDGPwtDetail(){ List<PwtReportBean> list =
	 * new ArrayList<PwtReportBean>(); PwtReportBean reportbean=null; con=new
	 * DBConnect().getConnection();
	 * 
	 * try { List<Integer> gameNumList = Util.getGameNumberList(); String
	 * commonQry = "select transaction_id, (pwt_amt) as agtMrp, (pwt_amt +
	 * retailer_claim_comm)as agtNet, game_id from st_dg_ret_pwt_";
	 * StringBuilder pwtClmQry = new StringBuilder(""); StringBuilder
	 * pwtUnclmQry = new StringBuilder(""); for(int i=0;i<gameNumList.size();i++){
	 * pwtClmQry.append(commonQry+gameNumList.get(i)+" where status = 'DONE_CLM'
	 * union "); pwtUnclmQry.append(commonQry+gameNumList.get(i)+" where status =
	 * 'CLAIM_BAL' or 'UNCLAIMED_BAL' union "); }
	 * 
	 * String PwtReportGameWiseRetQuery = "(select top.game_name as gamename,
	 * myn.agtMrpClaimed as agtMrpClaimed, myn.agtNetClaimed as agtNetClaimed,
	 * myn.agtMrpUnclm as agtMrpUnclm, myn.agtNetUnclm as agtNetUnclm from
	 * (select ifnull(unclaimed.gid, claimed.gid) as gid,
	 * ifnull(claimed.agtMrpClaimed, 0) as agtMrpClaimed,
	 * ifnull(claimed.agtNetClaimed,0) as agtNetClaimed,
	 * ifnull(unclaimed.agtMrpUnclm,0) as agtMrpUnclm,
	 * ifnull(unclaimed.agtNetUnclm,0) as agtNetUnclm from (select clm.game_id
	 * as gid, ifnull(sum(agtMrp),0) as agtMrpClaimed, ifnull(sum(agtNet), 0) as
	 * agtNetClaimed from (select transaction_id, game_id from
	 * st_lms_retailer_transaction_master where (date(transaction_date) >= ? and
	 * date(transaction_date) < ?) and transaction_type = 'DG_PWT' and
	 * retailer_org_id = ?)clm,(" +
	 * pwtClmQry.toString().substring(0,pwtClmQry.lastIndexOf(" union "))
	 * +")ret1 where clm.transaction_id = ret1.transaction_id group by gid)as
	 * claimed left outer join(select unclm.game_id as gid,
	 * ifnull(sum(agtMrp),0) as agtMrpUnclm, ifnull(sum(agtNet), 0) as
	 * agtNetUnclm from (select transaction_id, game_id from
	 * st_lms_retailer_transaction_master where (date(transaction_date)>=? and
	 * date(transaction_date) < ?) and transaction_type = 'DG_PWT' and
	 * retailer_org_id = ?)unclm,(" +
	 * pwtUnclmQry.toString().substring(0,pwtUnclmQry.lastIndexOf(" union "))
	 * +")ret1 where unclm.transaction_id = ret1.transaction_id group by gid)as
	 * unclaimed on claimed.gid = unclaimed.gid)as myn,(select game_id,
	 * game_name from st_dg_game_master)as top where myn.gid = top.game_id order
	 * by myn.gid asc) union (select top.game_name as gamename,
	 * myn.agtMrpClaimed as agtMrpClaimed, myn.agtNetClaimed as agtNetClaimed,
	 * myn.agtMrpUnclm as agtMrpUnclm, myn.agtNetUnclm as agtNetUnclm from
	 * (select ifnull(unclaimed.gid, claimed.gid) as gid,
	 * ifnull(claimed.agtMrpClaimed, 0) as agtMrpClaimed,
	 * ifnull(claimed.agtNetClaimed,0) as agtNetClaimed,
	 * ifnull(unclaimed.agtMrpUnclm,0) as agtMrpUnclm,
	 * ifnull(unclaimed.agtNetUnclm,0) as agtNetUnclm from (select clm.game_id
	 * as gid, ifnull(sum(agtMrp),0) as agtMrpClaimed, ifnull(sum(agtNet), 0) as
	 * agtNetClaimed from (select transaction_id, game_id from
	 * st_lms_retailer_transaction_master where (date(transaction_date) >= ? and
	 * date(transaction_date) < ?) and transaction_type = 'DG_PWT' and
	 * retailer_org_id = ?)clm,(" +
	 * pwtClmQry.toString().substring(0,pwtClmQry.lastIndexOf(" union "))
	 * +")ret1 where clm.transaction_id = ret1.transaction_id group by gid)as
	 * claimed right outer join(select unclm.game_id as gid,
	 * ifnull(sum(agtMrp),0) as agtMrpUnclm, ifnull(sum(agtNet), 0) as
	 * agtNetUnclm from (select transaction_id, game_id from
	 * st_lms_retailer_transaction_master where (date(transaction_date)>=? and
	 * date(transaction_date) < ?) and transaction_type = 'DG_PWT' and
	 * retailer_org_id = ?)unclm,(" +
	 * pwtUnclmQry.toString().substring(0,pwtUnclmQry.lastIndexOf(" union "))
	 * +")ret1 where unclm.transaction_id = ret1.transaction_id group by gid)as
	 * unclaimed on claimed.gid = unclaimed.gid)as myn,(select game_id,
	 * game_name from st_dg_game_master)as top where myn.gid = top.game_id order
	 * by myn.gid asc)"; PreparedStatement
	 * pstmt=con.prepareStatement(PwtReportGameWiseRetQuery); // get Retailer's
	 * player PWT details //String getDirPlrPwtDetailQuery =
	 * QueryManager.getST_DG_PWT_REPORT_GAME_WISE_RET(); pstmt.setDate(1,
	 * startDate); pstmt.setDate(2, endDate); pstmt.setInt(3,OrgId);
	 * pstmt.setDate(4, startDate); pstmt.setDate(5, endDate);
	 * pstmt.setInt(6,OrgId); pstmt.setDate(7, startDate); pstmt.setDate(8,
	 * endDate); pstmt.setInt(9,OrgId); pstmt.setDate(10, startDate);
	 * pstmt.setDate(11, endDate); pstmt.setInt(12,OrgId); logger.debug(" get
	 * Player pwt detail query == "+pstmt); rs=pstmt.executeQuery();
	 * 
	 * while(rs.next()) { reportbean=new PwtReportBean();
	 * reportbean.setClmMrp(rs.getString("agtMrpClaimed"));
	 * reportbean.setClmNet(rs.getString("agtNetClaimed")); Double d1 =
	 * Double.parseDouble(rs.getString("agtMrpClaimed")); Double d2 =
	 * Double.parseDouble(rs.getString("agtNetClaimed"));
	 * reportbean.setClmProfit(FormatNumber.formatNumber(d2 - d1));
	 * reportbean.setUnclmMrp(rs.getString("agtMrpUnclm"));
	 * reportbean.setUnclmNet(rs.getString("agtNetUnclm")); Double d3 =
	 * Double.parseDouble(rs.getString("agtMrpUnclm")); Double d4 =
	 * Double.parseDouble(rs.getString("agtNetUnclm"));
	 * reportbean.setUnclmProfit(FormatNumber.formatNumber(d4 - d3));
	 * reportbean.setGameName(rs.getString("gamename")); list.add(reportbean); } }
	 * catch (SQLException e) { logger.error("Exception: "+e);
	 * e.printStackTrace(); }finally { try { if(con!=null && (!con.isClosed()))
	 * con.close(); } catch (SQLException e) { logger.error("Exception: "+e);
	 * e.printStackTrace(); } }
	 * 
	 * return list; }
	 */

	public List<PwtReportBean> getDGPwtDetail() {
		List<PwtReportBean> list = new ArrayList<PwtReportBean>();
		PwtReportBean reportbean = null;
		con = DBConnect.getConnection();

		try {
			List<Integer> gameNumList = Util.getGameNumberList();
			String commonQry = "select transaction_id, (pwt_amt) as agtMrp, (pwt_amt + retailer_claim_comm)as agtNet, game_id from st_dg_ret_pwt_";
			StringBuilder pwtQry = new StringBuilder("");
			for (int i = 0; i < gameNumList.size(); i++) {
				pwtQry
						.append(commonQry
								+ gameNumList.get(i)
								+ " where status = 'DONE_CLM' or status = 'CLAIM_BAL' union ");
			}
			String query = "select mub.game_name as gname, tot.retPwtMrp as pwtMrp, tot.retPwtNet as pwtNet from(select clm.game_id as id, ifnull(sum(agtMrp),0) as retPwtMrp, ifnull(sum(agtNet),0)as retPwtNet from(select transaction_id, game_id from st_lms_retailer_transaction_master where date(transaction_date)>=? and date(transaction_date)<? and transaction_type = 'DG_PWT' and retailer_org_id = ?) as clm, (";
			PreparedStatement pstmt = con
					.prepareStatement(query
							+ pwtQry.toString().substring(0,
									pwtQry.lastIndexOf(" union "))
							+ ")as ret1 where clm.transaction_id = ret1.transaction_id group by clm.game_id)tot, (select game_id, game_name from st_dg_game_master)mub where tot.id = mub.game_id");
			pstmt.setDate(1, startDate);
			pstmt.setDate(2, endDate);
			pstmt.setInt(3, OrgId);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				reportbean = new PwtReportBean();
				reportbean.setGameName(rs.getString("gname"));
				reportbean.setClmMrp(rs.getString("pwtMrp"));
				reportbean.setClmNet(rs.getString("pwtNet"));
				reportbean.setClmProfit(""
						+ (Double.parseDouble(rs.getString("pwtNet")) - Double
								.parseDouble(rs.getString("pwtMrp"))));
				list.add(reportbean);
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
		return list;
	}

}
