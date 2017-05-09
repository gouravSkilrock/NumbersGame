package com.skilrock.lms.coreEngine.commercialService.reportMgmt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.embedded.drawGames.common.EmbeddedErrors;
import com.skilrock.lms.embedded.loginMgmt.AuthenticationAction;

public class CSTerminalReportHelper {

	static Log logger = LogFactory.getLog(CSTerminalReportHelper.class);

	public String getRetLastTenTransaction(UserInfoBean userBean)
			throws Exception {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		PreparedStatement pstmt1 = null;
		ResultSet rs1 = null;
		Connection connection = null;
		boolean isTxnFetched = false;
		try {
			connection = DBConnect.getConnection();
			int catId = 0;
			int txnId = 0;
			double mrpAmt = 0.0;
			StringBuilder repString = new StringBuilder("");
			String date = new Timestamp(new Date().getTime()).toString();
			repString
					.append("Date:" + date.substring(0, date.lastIndexOf(".")));
			String qry = "select rtm.transaction_id, rtm.transaction_date, pcm.category_code, com.operator_name, pm.denomination, pm.category_id, rtm.transaction_type from st_lms_retailer_transaction_master rtm, st_cs_product_master pm, st_cs_product_category_master pcm, st_cs_operator_master com "
					+ "where rtm.transaction_type = 'CS_SALE' and rtm.retailer_user_id = "
					+ userBean.getUserId()
					+ " and rtm.game_id = pm.product_id and pm.category_id = pcm.category_id and com.operator_code = pm.operator_code order by rtm.transaction_date desc limit 10";
			pstmt = connection.prepareStatement(qry);
			rs = pstmt.executeQuery();
			logger.debug("fetchin last 10 CS Txns....." + pstmt);
			while (rs.next()) {
				catId = rs.getInt("category_id");
				txnId = rs.getInt("transaction_id");
				String refQry = "select * from st_cs_refund_" + catId
						+ " where rms_ref_transaction_id = " + txnId;
				pstmt1 = connection.prepareStatement(refQry);
				rs1 = pstmt1.executeQuery();
				if (rs1.next()) {
					// cancelled transaction
				} else {// sale transaction
					isTxnFetched = true;
					String saleQry = "select mrp_amt from st_cs_sale_" + catId
							+ " where transaction_id = " + txnId;
					pstmt1 = connection.prepareStatement(saleQry);
					rs1 = pstmt1.executeQuery();
					if (rs1.next()) {
						mrpAmt = rs1.getDouble("mrp_amt");
					}
					repString
							.append("|"
									+ rs.getString("transaction_date")
											.subSequence(
													0,
													rs.getString(
															"transaction_date")
															.lastIndexOf("."))
									+ ","
									+ rs.getString("operator_name")
											.substring(
													0,
													rs.getString("operator_name")
															.length() > 3 ? 3
															: rs.getString(
																	"operator_name")
																	.length())
											.toUpperCase() + "," + mrpAmt);
				}
			}
			repString.append("|ret_org:" + userBean.getOrgName().toUpperCase()
					+ "|");
			logger.debug("report returned .....>" + repString.toString());
			if(isTxnFetched){
				return repString.toString();
			}else{
				return "ErrorMsg:"+EmbeddedErrors.REPORT_DATA_NOT_AVAILABLE+"|";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
