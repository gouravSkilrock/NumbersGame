package com.skilrock.lms.coreEngine.reportsMgmt.daoImpl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.reportsMgmt.javaBeans.GoodCauseDataBean;

public class GoodCauseDaoImpl {
	final static Log logger = LogFactory.getLog(GoodCauseDaoImpl.class);

	private static GoodCauseDaoImpl instance;

	private GoodCauseDaoImpl() {
	}

	public static GoodCauseDaoImpl getInstance() {
		if (instance == null) {
			synchronized (GoodCauseDaoImpl.class) {
				if (instance == null) {
					instance = new GoodCauseDaoImpl();
				}
			}
		}
		return instance;
	}

	public List<GoodCauseDataBean> fetchGoodCauseData(Timestamp startTime, Timestamp endTime, Connection connection) throws LMSException {
		Statement stmt = null;
		ResultSet rs = null;
		SimpleDateFormat dateFormat = null;
		List<GoodCauseDataBean> reportList = new ArrayList<GoodCauseDataBean>();
		GoodCauseDataBean dataBean = null;
		try {
			dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			stmt = connection.createStatement();
			String query = "SELECT game_id, game_name, transaction_type, SUM(sale_unapproved_amount) sale_unapproved_amount, SUM(winning_unapproved_amount) winning_unapproved_amount, SUM(sale_approved_amount) sale_approved_amount, SUM(winning_approved_amount) winning_approved_amount, SUM(sale_done_amount) sale_done_amount, SUM(winning_done_amount) winning_done_amount, end_date, service_code FROM (SELECT bt.game_id, game_name, transaction_type, IF(STATUS='UNAPPROVED',(IF(transaction_type='GOVT_COMM',amount,0)),0) sale_unapproved_amount, IF(STATUS='UNAPPROVED',(IF(transaction_type='GOVT_COMM_PWT',amount,0)),0) winning_unapproved_amount, IF(STATUS='APPROVED',(IF(transaction_type='GOVT_COMM',amount,0)),0) sale_approved_amount, IF(STATUS='APPROVED',(IF(transaction_type='GOVT_COMM_PWT',amount,0)),0) winning_approved_amount, IF(STATUS='DONE',(IF(transaction_type='GOVT_COMM',amount,0)),0) sale_done_amount, IF(STATUS='DONE',(IF(transaction_type='GOVT_COMM_PWT',amount,0)),0) winning_done_amount, end_date, service_code FROM st_lms_bo_tasks bt INNER JOIN st_dg_game_master gm ON bt.game_id=gm.game_id WHERE end_date>='"+startTime+"' AND end_date<='"+endTime+"' AND service_code='DG' UNION ALL SELECT bt.game_id, type_disp_name game_name, transaction_type, IF(STATUS='UNAPPROVED',(IF(transaction_type='GOVT_COMM',amount,0)),0) sale_unapproved_amount, IF(STATUS='UNAPPROVED',(IF(transaction_type='GOVT_COMM_PWT',amount,0)),0) winning_unapproved_amount, IF(STATUS='APPROVED',(IF(transaction_type='GOVT_COMM',amount,0)),0) sale_approved_amount, IF(STATUS='APPROVED',(IF(transaction_type='GOVT_COMM_PWT',amount,0)),0) winning_approved_amount, IF(STATUS='DONE',(IF(transaction_type='GOVT_COMM',amount,0)),0) sale_done_amount, IF(STATUS='DONE',(IF(transaction_type='GOVT_COMM_PWT',amount,0)),0) winning_done_amount, end_date, service_code FROM st_lms_bo_tasks bt INNER JOIN st_sle_game_type_master gm ON bt.game_id=gm.game_type_id WHERE end_date>='"+startTime+"' AND end_date<='"+endTime+"' AND service_code='SLE')aa GROUP BY service_code, game_name;";
			logger.info("fetchGoodCauseData - "+query);
			rs = stmt.executeQuery(query);
			while(rs.next()) {
				dataBean = new GoodCauseDataBean();
				dataBean.setGameId(rs.getInt("game_id"));
				dataBean.setGameName(rs.getString("game_name"));
				dataBean.setSaleUnapprovedAmount(rs.getDouble("sale_unapproved_amount"));
				dataBean.setWinningUnapprovedAmount(rs.getDouble("winning_unapproved_amount"));
				dataBean.setSaleApprovedAmount(rs.getDouble("sale_approved_amount"));
				dataBean.setWinningApprovedAmount(rs.getDouble("winning_approved_amount"));
				dataBean.setSaleDoneAmount(rs.getDouble("sale_done_amount"));
				dataBean.setWinningDoneAmount(rs.getDouble("winning_done_amount"));
				dataBean.setTransactionDate(dateFormat.format(rs.getTimestamp("end_date")));
				dataBean.setServiceCode(rs.getString("service_code"));
				reportList.add(dataBean);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
			throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE, LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE, LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeStmt(stmt);
			DBConnect.closeRs(rs);
		}

		return reportList;
	}
}