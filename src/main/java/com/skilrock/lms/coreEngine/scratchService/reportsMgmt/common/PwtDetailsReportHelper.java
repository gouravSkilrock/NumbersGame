package com.skilrock.lms.coreEngine.scratchService.reportsMgmt.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.skilrock.lms.beans.DateBeans;
import com.skilrock.lms.beans.PwtDetailsBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.utility.FormatNumber;
import com.skilrock.lms.common.utility.GetDate;

public class PwtDetailsReportHelper {

	public static void main(String[] args) {
		List<PwtDetailsBean> list = new PwtDetailsReportHelper(GetDate
				.getDate("Current Week"))
				.getAgentPwtDetails(4, "arun upadhyay");
		for (PwtDetailsBean bean : list) {
			System.out.println(bean.getSrNo() + ",  " + bean.getGameName()
					+ " ,  " + bean.getPrize() + " ,  " + bean.getNoOfTkt()
					+ " ,  " + bean.getAmount());
		}

		List<PwtDetailsBean> plist = new PwtDetailsReportHelper(GetDate
				.getDate("Current Week")).getPlayerPwtDetails();
		for (PwtDetailsBean bean : plist) {
			System.out.println(bean.getPlayerName() + ",  " + bean.getSrNo()
					+ ",  " + bean.getGameName() + " ,  " + bean.getPrize()
					+ " ,  " + bean.getNoOfTkt() + " ,  " + bean.getAmount());
		}
	}

	private Connection con = null;
	private DateBeans dateBean = null;

	private PreparedStatement pstmt = null;

	public PwtDetailsReportHelper(DateBeans dateBean) {
		this.dateBean = dateBean;
	}

	/**
	 * It get the details from 'bo_pwt table' from database as agent wise
	 * 
	 * @param id
	 * @param name
	 * @return p
	 */
	public List<PwtDetailsBean> getAgentPwtDetails(int id, String name) {
		List<PwtDetailsBean> list = new ArrayList<PwtDetailsBean>();
		con = DBConnect.getConnection();
		String query = QueryManager.getST_BO_AGENT_PWT_DETAILS();
		try {
			pstmt = con.prepareStatement(query);
			pstmt.setInt(1, id);
			pstmt.setDate(2, dateBean.getFirstdate());
			pstmt.setDate(3, dateBean.getLastdate());
			ResultSet rs = pstmt.executeQuery();
			System.out.println(" ST_BO_PLAYER_PWT_DETAILS = " + pstmt);
			int count = 1;
			PwtDetailsBean bean = null;

			while (rs.next()) {
				bean = new PwtDetailsBean();
				bean.setSrNo(count);
				bean.setPlayerName(name);
				bean.setGameName(rs.getString("game_name"));
				bean.setPrize(FormatNumber
						.formatNumber(rs.getDouble("pwt_amt")));
				bean.setNoOfTkt(rs.getInt("no_of_tkt"));
				bean.setAmount(FormatNumber
						.formatNumber(rs.getDouble("amount")));
				list.add(bean);
				count += 1;
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {

				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null && !con.isClosed()) {
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return list;
	}

	/**
	 * It get the details from 'bo_player_pwt table' from database
	 * 
	 * @param id
	 * @return p
	 */
	public List<PwtDetailsBean> getPlayerPwtDetails() {
		List<PwtDetailsBean> list = new ArrayList<PwtDetailsBean>();
		con = DBConnect.getConnection();
		String query = QueryManager.getST_BO_PLAYER_PWT_DETAILS();
		try {
			pstmt = con.prepareStatement(query);
			pstmt.setDate(1, dateBean.getFirstdate());
			pstmt.setDate(2, dateBean.getLastdate());
			ResultSet rs = pstmt.executeQuery();
			System.out.println(" ST_BO_PLAYER_PWT_DETAILS = " + pstmt);
			int count = 1;
			PwtDetailsBean bean = null;

			while (rs.next()) {
				bean = new PwtDetailsBean();
				bean.setSrNo(count);
				bean.setPlayerName(rs.getString("player_name"));
				bean.setGameName(rs.getString("game_name"));
				bean.setPrize(FormatNumber
						.formatNumber(rs.getDouble("pwt_amt")));
				bean.setNoOfTkt(rs.getInt("no_of_tkt"));
				bean.setAmount(FormatNumber
						.formatNumber(rs.getDouble("amount")));
				list.add(bean);
				count += 1;
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}
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
