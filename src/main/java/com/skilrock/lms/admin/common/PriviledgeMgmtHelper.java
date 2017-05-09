package com.skilrock.lms.admin.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.skilrock.lms.beans.MainPriviledgeBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSException;

public class PriviledgeMgmtHelper {

	public List<MainPriviledgeBean> fetchPriviledge(String privTlbName)
			throws LMSException {
		List<MainPriviledgeBean> privList = new ArrayList<MainPriviledgeBean>();
		MainPriviledgeBean bean = null;
		Connection con = DBConnect.getConnection();
		try {
			Statement configStmt = con.createStatement();
			String fetchProperty = "select action_id,priv_id,priv_disp_name,action_mapping,priv_owner,related_to,group_name,status from "
					+ privTlbName
					+ " where is_start='Y' order by priv_owner,related_to";
			ResultSet privRS = configStmt.executeQuery(fetchProperty);

			while (privRS.next()) {
				bean = new MainPriviledgeBean();
				bean.setActionId(privRS.getInt("action_id"));
				bean.setPrivId(privRS.getInt("priv_id"));
				bean.setPrivDispName(privRS.getString("priv_disp_name"));
				bean.setActionMapping(privRS.getString("action_mapping"));
				bean.setPrivOwner(privRS.getString("priv_owner"));
				bean.setRelatedTo(privRS.getString("related_to"));
				bean.setManuDispName(privRS.getString("group_name"));
				bean.setStatus(privRS.getString("status"));
				privList.add(bean);
			}

			return privList;

		} catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException("sqlException", e);
		} finally {
			DBConnect.closeCon(con);
		}

	}

	public Map<String, String> fetchTableName() throws LMSException {
		Connection con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs;
		Map<String, String> tlbMap = new HashMap<String, String>();
		try {
			String qry = "select distinct priv_rep_table from st_lms_service_delivery_master";
			pstmt = con.prepareStatement(qry);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				tlbMap.put(rs.getString("priv_rep_table"), rs
						.getString("priv_rep_table"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException("sqlException", e);
		} finally {
			DBConnect.closeCon(con);
		}
		return tlbMap;
	}

	public void savePriviledge(int[] privId, String[] manuDispName,
			String[] status, String privTlbName, int[] actionId)
			throws LMSException {
		Connection con = DBConnect.getConnection();
		Statement stmt = null;
		try {
			stmt = con.createStatement();

			for (int i = 0; i < privId.length; i++) {
				String updatePrivQry = "update " + privTlbName
						+ " set group_name='" + manuDispName[i] + "',status='"
						+ status[i] + "' where priv_id=" + privId[i];
				stmt.addBatch(updatePrivQry);
				String updateMenuQry = "update "+privTlbName.replace("priviledge_rep", "menu_master")+" set menu_name='"
						+ manuDispName[i]
						+ "',menu_disp_name='"
						+ manuDispName[i] + "' where action_id=" + actionId[i];
				stmt.addBatch(updateMenuQry);

			}
			stmt.executeBatch();

		} catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException("sqlException", e);
		} finally {
			DBConnect.closeCon(con);
		}
	}
}
