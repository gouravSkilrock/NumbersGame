package com.skilrock.lms.coreEngine.drawGames.reportMgmt;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.beans.RetWiseSummaryTxnBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.web.reportsMgmt.common.ReportUtility;

public class RetWiseSummeryTxnReportHelper {
	static Log logger = LogFactory.getLog(RetWiseSummeryTxnReportHelper.class);

	public List<RetWiseSummaryTxnBean> getSummeryTxnReport(String startDate, String endDate) throws LMSException {

		SimpleDateFormat simpleDateFormat = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<RetWiseSummaryTxnBean> txnBeans = null;
		RetWiseSummaryTxnBean bean = null;
		try {
			con = DBConnect.getConnection();
			txnBeans = new ArrayList<RetWiseSummaryTxnBean>();

			Date lastArchDate = ReportUtility.getLastArchDateInDateFormat(con);
			simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			if(lastArchDate.after(new Date(simpleDateFormat.parse(startDate).getTime())))
				throw new LMSException(LMSErrors.INVALIDATE_TO_ARCHIVE_DATE_ERROR_CODE, LMSErrors.INVALIDATE_TO_ARCHIVE_DATE_ERROR_MESSAGE);

			//pstmt = con.prepareStatement("SELECT retailer_org_id retOrgId, "+QueryManager.getOrgCodeQuery()+", SUM(dgSaleCount) dgSaleCount, SUM(dgRefundCount) dgRefundCount, SUM(dgPwtCount) dgPwtCount, SUM(totalCount) totalCount FROM (SELECT retailer_org_id, IF(transaction_type IN ('DG_SALE','DG_SALE_OFFLINE'),COUNT(transaction_id),0) dgSaleCount, IF(transaction_type IN ('DG_REFUND_CANCEL','DG_REFUND_FAILED'), COUNT(transaction_id),0) dgRefundCount, IF(transaction_type IN ('DG_PWT','DG_PWT_PLR','DG_PWT_AUTO'),COUNT(transaction_id),0) dgPwtCount, COUNT(transaction_id) totalCount FROM st_lms_retailer_transaction_master WHERE transaction_date>=? AND transaction_date<=? GROUP BY transaction_type, retailer_org_id)rtm INNER JOIN st_lms_organization_master om ON rtm.retailer_org_id = om.organization_id WHERE organization_type='RETAILER' GROUP BY retailer_org_id;");
			pstmt = con.prepareStatement("SELECT retailer_org_id retOrgId, "+QueryManager.getOrgCodeQuery()+", SUM(dgSaleCount) dgSaleCount, SUM(dgRefundCount) dgRefundCount, SUM(dgPwtCount) dgPwtCount, SUM(totalCount) totalCount FROM (SELECT retailer_org_id, IF(transaction_type IN ('DG_SALE','DG_SALE_OFFLINE'),COUNT(transaction_id),0) dgSaleCount, IF(transaction_type IN ('DG_REFUND_CANCEL','DG_REFUND_FAILED'), COUNT(transaction_id),0) dgRefundCount, IF(transaction_type IN ('DG_PWT','DG_PWT_PLR','DG_PWT_AUTO'),COUNT(transaction_id),0) dgPwtCount, IF(transaction_type IN ('DG_SALE','DG_SALE_OFFLINE','DG_REFUND_CANCEL','DG_REFUND_FAILED','DG_PWT','DG_PWT_PLR','DG_PWT_AUTO'), COUNT(transaction_id),0) totalCount FROM st_lms_retailer_transaction_master WHERE transaction_date>=? AND transaction_date<=? GROUP BY transaction_type, retailer_org_id)rtm INNER JOIN st_lms_organization_master om ON rtm.retailer_org_id = om.organization_id WHERE organization_type='RETAILER' GROUP BY retailer_org_id;");

			pstmt.setString(1, startDate+" 00:00:00");
			pstmt.setString(2, endDate+" 23:59:59");
			logger.debug("Retailer Wise Summary Transaction Report - "+pstmt);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				if (rs.getInt("totalCount") != 0) {
					bean = new RetWiseSummaryTxnBean();
					bean.setRetOrgId(rs.getInt("retOrgId"));
					bean.setRetName(rs.getString("orgCode"));
					bean.setDgSaleCount(rs.getInt("dgSaleCount"));
					bean.setDgRefundCount(rs.getInt("dgRefundCount"));
					bean.setDgPwtCount(rs.getInt("dgPwtCount"));
					bean.setTotalCount(rs.getInt("totalCount"));
					txnBeans.add(bean);
				}
			}
		} catch (SQLException se) {
			logger.error("SQLException - ", se);
			throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE,LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (LMSException le) {
			logger.error("LMSException - ", le);
			throw le;
		} catch (Exception e) {
			logger.error("Exception - ", e);
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeConnection(con, pstmt, rs);
		}

		return txnBeans;
	}
}