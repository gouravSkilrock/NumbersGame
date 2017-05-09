package com.skilrock.lms.coreEngine.reportsMgmt.daoImpl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.reportsMgmt.javaBeans.IntakeReportDataBean;
import com.skilrock.lms.dge.beans.GameMasterLMSBean;
import com.skilrock.lms.web.drawGames.common.Util;

public class IntakeReportDaoImpl {
	final static Log logger = LogFactory.getLog(IntakeReportDaoImpl.class);

	private static IntakeReportDaoImpl instance;

	private IntakeReportDaoImpl() {
	}

	public static IntakeReportDaoImpl getInstance() {
		if (instance == null) {
			synchronized (IntakeReportDaoImpl.class) {
				if (instance == null) {
					instance = new IntakeReportDaoImpl();
				}
			}
		}
		return instance;
	}

	public List<IntakeReportDataBean> fetchReportData(String service,
			Timestamp startTime, Timestamp endTime, Connection connection)
			throws LMSException {
		Statement stmt = null;
		ResultSet rs = null;
		SimpleDateFormat dateFormat = null;
		List<IntakeReportDataBean> reportList = new ArrayList<IntakeReportDataBean>();
		IntakeReportDataBean dataBean = null;
		StringBuilder query = new StringBuilder();
		try {
			dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Map<Integer, GameMasterLMSBean> gameMap = Util.getLmsGameMap();
			stmt = connection.createStatement();
			if ("DGE".equalsIgnoreCase(service)) {
				for (Map.Entry<Integer, GameMasterLMSBean> entry : gameMap
						.entrySet()) {
					GameMasterLMSBean gameDataBean=(GameMasterLMSBean)entry.getValue();
					if ("OPEN".equalsIgnoreCase(gameDataBean.getGameStatus())) {
						query.append("select area_name,cc.city,ret_org_code,org_code as agt_org_code ,pwt_amt,transaction_date from (select area_name,city,parent_id,ret_org_code,pwt_amt,transaction_date from (select area_code,city,parent_id,org_code as ret_org_code,pwt_amt,transaction_date from (select ticket_nbr,retailer_org_id,sum(pwt_amt) as pwt_amt,transaction_date from (select ticket_nbr , a.retailer_org_id,a.pwt_amt + retailer_claim_comm  - govt_claim_comm as pwt_amt ,transaction_date from st_dg_ret_pwt_"+ gameDataBean.getGameId()+ " a inner join st_lms_retailer_transaction_master b on a.transaction_id=b.transaction_id inner join st_dg_pwt_inv_"+ gameDataBean.getGameId()+ " c on a.transaction_id=c.retailer_transaction_id where c.status in('CLAIM_AGT_CLM_AUTO'))aa  where transaction_date>='"+ startTime+ "' and transaction_date<='"+ endTime+ "' group by ticket_nbr)bb inner join st_lms_organization_master jj on jj.organization_id=bb.retailer_org_id)ll inner join st_lms_area_master op on op.area_code=ll.area_code )cc inner join st_lms_organization_master jk on jk.organization_id=cc.parent_id union all ");
					}
				}
				query.delete(query.lastIndexOf(" union all"), query.length());
			} else if ("SLE".equalsIgnoreCase(service)) {
				query.append("select area_name,cc.city,ret_org_code,org_code as agt_org_code ,pwt_amt,transaction_date from (select area_name,city,parent_id,ret_org_code,pwt_amt,transaction_date from (select area_code,city,parent_id,org_code as ret_org_code,pwt_amt,transaction_date from (select ticket_nbr,retailer_org_id,sum(pwt_amt) as pwt_amt,transaction_date from (select ticket_nbr , a.retailer_org_id,a.pwt_amt + retailer_claim_comm  - govt_claim_comm as pwt_amt ,transaction_date from st_sle_ret_pwt a inner join st_sle_pwt_inv c on a.transaction_id=c.retailer_transaction_id where c.status in('CLAIM_AGT_CLM_AUTO'))aa  where transaction_date>='"+ startTime+ "' and transaction_date<='"+ endTime+ "' group by ticket_nbr)bb inner join st_lms_organization_master jj on jj.organization_id=bb.retailer_org_id)ll inner join st_lms_area_master op on op.area_code=ll.area_code )cc inner join st_lms_organization_master jk on jk.organization_id=cc.parent_id;");
			}
			logger.info("fetchIntakeReportData - " + query);
			rs = stmt.executeQuery(query.toString());
			while (rs.next()) {
				dataBean = new IntakeReportDataBean();
				dataBean.setAgtOrgCode(rs.getString("agt_org_code"));
				dataBean.setPwtAmount(rs.getDouble("pwt_amt"));
				dataBean.setRetOrgCode(rs.getString("ret_org_code"));
				dataBean.setTransDate(dateFormat.format(rs
						.getTimestamp("transaction_date")));
				dataBean.setAreaName(rs.getString("area_name"));
				dataBean.setCity(rs.getString("city"));
				reportList.add(dataBean);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
			throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE,
					LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,
					LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeStmt(stmt);
			DBConnect.closeRs(rs);
		}

		return reportList;
	}

}