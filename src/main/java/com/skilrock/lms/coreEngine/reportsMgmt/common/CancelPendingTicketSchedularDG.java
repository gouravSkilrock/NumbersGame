package com.skilrock.lms.coreEngine.reportsMgmt.common;

import java.lang.reflect.Type;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.skilrock.lms.beans.ServiceRequest;
import com.skilrock.lms.beans.ServiceResponse;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.service.IServiceDelegate;
import com.skilrock.lms.coreEngine.service.ServiceDelegate;
import com.skilrock.lms.coreEngine.service.dge.ServiceMethodName;
import com.skilrock.lms.coreEngine.service.dge.ServiceName;
import com.skilrock.lms.dge.beans.CancelTicketDataBean;
import com.skilrock.lms.scheduler.SchedulerCommonFuntionsHelper;

public class CancelPendingTicketSchedularDG {
	private static Log logger = LogFactory.getLog(CancelPendingTicketSchedularDG.class);

	public static void pendingCancelTicketInsertSchedular(int jobId) throws LMSException {
		Connection con = null;
		Statement stmt = null;
		Statement insertStmt = null;
		ResultSet rs = null;
		CallableStatement callStmt = null;
		Timestamp startDate = null;
		SimpleDateFormat dateFormat = null;
		try {
			con = DBConnect.getConnection();
			con.setAutoCommit(false);
			stmt = con.createStatement();
			rs = stmt.executeQuery("SELECT last_success_time FROM st_lms_scheduler_master WHERE dev_name='Cancel_Pending_Ticket_DG_SCHEDULER';");
			if(rs.next()) {
				startDate = rs.getTimestamp("last_success_time");
			} else {
				throw new LMSException("Job 'Cancel_Pending_Ticket_DG_SCHEDULER' Not Found.");
			}

			logger.info("Scheduler Start From - "+startDate);
			SchedulerCommonFuntionsHelper.updateSchedulerStart(jobId, con);
			callStmt = con.prepareCall("call PendingCancelTktDG(?)");
			callStmt.setTimestamp(1, startDate);
			callStmt.executeUpdate();

			dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			List<CancelTicketDataBean> dateList = null;
			CancelTicketDataBean dataBean = null;
			ServiceRequest sRequest = null;
			ServiceResponse sResponse = null;
			IServiceDelegate delegate = ServiceDelegate.getInstance();

			stmt = con.createStatement();
			rs = stmt.executeQuery("SELECT game_id FROM st_dg_game_master WHERE game_status<>'SALE_CLOSE';");
			while(rs.next()) {
				int gameId = rs.getInt("game_id");
				dataBean = new CancelTicketDataBean();
				dataBean.setGameId(gameId);
				dataBean.setFromDate(dateFormat.format(startDate));

				sRequest = new ServiceRequest();
				sResponse = new ServiceResponse();
				sRequest.setServiceName(ServiceName.REPORTS_MGMT);
				sRequest.setServiceMethod(ServiceMethodName.FETCH_CANCEL_TICKET_FROM_DATE);
				sRequest.setServiceData(dataBean);
				sResponse = delegate.getResponse(sRequest);
				if (sResponse.getIsSuccess()) {
					Type elementType = new TypeToken<List<CancelTicketDataBean>>() {}.getType();
					dateList = new Gson().fromJson(sResponse.getResponseData().toString(), elementType);

					if(dateList!=null && dateList.size()>0) {
						StringBuilder ticketQueryBuilder = new StringBuilder();

						int counter = 0;
						int size = dateList.size();
						int limit = dateList.size()-dateList.size()%50;
						for(int i=0; i<limit; i++) {
							counter++;
							CancelTicketDataBean tempBean = dateList.get(i);
							ticketQueryBuilder.append("SELECT ").append(tempBean.getTicketNumber()).append(" dg_ticket UNION ");
							if(counter%50 == 0) {
								String ticketQuery = ticketQueryBuilder.substring(0, ticketQueryBuilder.lastIndexOf(" UNION "));
								insertStmt = con.createStatement();
								String query = "INSERT INTO st_dg_ret_pending_cancel (sale_ref_transaction_id, ticket_nbr, mrp_amt, ret_net_amt, agent_net_amt, game_id, cancel_attempt_time, transaction_date, retailer_org_id, reason) " +
													"SELECT transaction_id, dg_ticket, mrp_amt, net_amt, agent_net_amt, game_id, NOW(), transaction_date, retailer_org_id, 'DG_ERROR' FROM (" +
													"SELECT dg_ticket FROM ("+ticketQuery+")aa LEFT JOIN (SELECT ticket_nbr FROM st_dg_ret_sale_refund_"+gameId+" UNION SELECT ticket_nbr FROM st_dg_ret_pending_cancel) refund ON aa.dg_ticket=refund.ticket_nbr WHERE ticket_nbr IS NULL)aa " +
													"INNER JOIN (SELECT rs.transaction_id, rs.ticket_nbr, rs.mrp_amt, rs.net_amt, rs.agent_net_amt, rs.game_id, transaction_date, rs.retailer_org_id FROM st_dg_ret_sale_"+gameId+" rs INNER JOIN st_lms_retailer_transaction_master rtm ON rs.transaction_id=rtm.transaction_id " +
													"WHERE transaction_date>='"+startDate+"')bb ON aa.dg_ticket=bb.ticket_nbr;";
								logger.info("Insert In st_dg_ret_pending_cancel for GameId("+gameId+") - "+query);
								insertStmt.executeUpdate(query);

								ticketQueryBuilder = new StringBuilder();
							}
						}

						ticketQueryBuilder = new StringBuilder();

						for(int i=limit; i<size; i++) {
							CancelTicketDataBean tempBean = dateList.get(i);
							ticketQueryBuilder.append("SELECT ").append(tempBean.getTicketNumber()).append(" dg_ticket UNION ");
						}
						String ticketQuery = ticketQueryBuilder.substring(0, ticketQueryBuilder.lastIndexOf(" UNION "));
						insertStmt = con.createStatement();
						String query = "INSERT INTO st_dg_ret_pending_cancel (sale_ref_transaction_id, ticket_nbr, mrp_amt, ret_net_amt, agent_net_amt, game_id, cancel_attempt_time, transaction_date, retailer_org_id, reason) " +
											"SELECT transaction_id, dg_ticket, mrp_amt, net_amt, agent_net_amt, game_id, NOW(), transaction_date, retailer_org_id, 'DG_ERROR' FROM (" +
											"SELECT dg_ticket FROM ("+ticketQuery+")aa LEFT JOIN (SELECT ticket_nbr FROM st_dg_ret_sale_refund_"+gameId+" UNION SELECT ticket_nbr FROM st_dg_ret_pending_cancel) refund ON aa.dg_ticket=refund.ticket_nbr WHERE ticket_nbr IS NULL)aa " +
											"INNER JOIN (SELECT rs.transaction_id, rs.ticket_nbr, rs.mrp_amt, rs.net_amt, rs.agent_net_amt, rs.game_id, transaction_date, rs.retailer_org_id FROM st_dg_ret_sale_"+gameId+" rs INNER JOIN st_lms_retailer_transaction_master rtm ON rs.transaction_id=rtm.transaction_id " +
											"WHERE transaction_date>='"+startDate+"')bb ON aa.dg_ticket=bb.ticket_nbr;";
						logger.info("Insert In st_dg_ret_pending_cancel for GameId("+gameId+") - "+query);
						insertStmt.executeUpdate(query);
					}
				} else {
					throw new LMSException();
				}
			}
			SchedulerCommonFuntionsHelper.updateSchedulerEnd(jobId, con);

			con.commit();
		} catch (SQLException e) {
			logger.info("SQL Exception ",e);
			throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE,LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception e) {
			logger.info("Exception ",e);
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeCon(con);
			DBConnect.closeStmt(stmt);
		}
	}
}