package com.skilrock.lms.web.scratchService.inventoryMgmt.daoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.web.drawGames.common.Util;
import com.skilrock.lms.web.scratchService.inventoryMgmt.javaBeans.AgentInvoicingMethodBean;

public class UpdateInvoicingMethodDaoImpl {
	final static Log logger = LogFactory.getLog(UpdateInvoicingMethodDaoImpl.class);

	private static UpdateInvoicingMethodDaoImpl instance;

	private UpdateInvoicingMethodDaoImpl() {
	}

	public static UpdateInvoicingMethodDaoImpl getInstance() {
		if (instance == null) {
			synchronized (UpdateInvoicingMethodDaoImpl.class) {
				if (instance == null) {
					instance = new UpdateInvoicingMethodDaoImpl();
				}
			}
		}
		return instance;
	}

	public Map<Integer, String> activeGameMap(Connection connection) throws LMSException {
		Statement stmt = null;
		ResultSet rs = null;
		Map<Integer, String> gameMap = new TreeMap<Integer, String>();
		try {
			stmt = connection.createStatement();
			//String query = "SELECT SQL_CACHE game_id, game_name FROM st_se_game_master WHERE game_status='OPEN';";
			String query = "SELECT SQL_CACHE game_id, game_name FROM st_se_game_master;";
			logger.info("activeGameMap Query - "+query);
			rs = stmt.executeQuery(query);
			while(rs.next()) {
				gameMap.put(rs.getInt("game_id"), rs.getString("game_name"));
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

		return gameMap;
	}

	public List<AgentInvoicingMethodBean> getAgentInvoicingMethod(int gameId, Connection connection) throws LMSException {
		Statement stmt = null;
		ResultSet rs = null;
		List<AgentInvoicingMethodBean> agentDetailList = new ArrayList<AgentInvoicingMethodBean>();
		AgentInvoicingMethodBean methodBean = null;
		try {
			stmt = connection.createStatement();
			String query = "SELECT om.organization_id, name, im.invoice_method_id, scheme_type, scheme_value_type, invoice_method_value FROM st_lms_organization_master om INNER JOIN st_se_org_game_invoice_methods rim ON om.organization_id=rim.organization_id INNER JOIN st_se_invoicing_methods im ON rim.invoice_method_id=im.invoice_method_id WHERE game_id="+gameId+" AND organization_type='AGENT' AND organization_status IN ('ACTIVE','INACTIVE','BLOCK');";
			logger.info("getAgentInvoicingMethod Query - "+query);
			rs = stmt.executeQuery(query);
			while(rs.next()) {
				methodBean = new AgentInvoicingMethodBean();
				methodBean.setOrgId(rs.getInt("organization_id"));
				methodBean.setOrgName(rs.getString("name"));
				methodBean.setMethodId(rs.getInt("invoice_method_id"));
				methodBean.setMethodName(rs.getString("scheme_type"));
				methodBean.setMethodType(rs.getString("scheme_value_type"));
				methodBean.setMethodValue(rs.getString("invoice_method_value"));
				agentDetailList.add(methodBean);
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

		return agentDetailList;
	}

	public Map<Integer,String> getInvoicingMethods (Connection connection) throws LMSException{
		Statement stmt = null;
		ResultSet rs = null;
		Map<Integer,String> invoiceMap = new HashMap<Integer,String>();
		try {
			stmt = connection.createStatement();
			String query = "select invoice_method_id,scheme_type from st_se_invoicing_methods;";
			logger.info("getAgentInvoicingMethod Query - "+query);
			rs = stmt.executeQuery(query);
			while(rs.next()) {
				invoiceMap.put(rs.getInt("invoice_method_id"), rs.getString("scheme_type"));
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

		return invoiceMap;
	}

	public Map<Integer, String> getInvoiceMethodIdMap(Connection connection) throws LMSException {
		Statement stmt = null;
		ResultSet rs = null;
		Map<Integer,String> methodIdMap = new HashMap<Integer,String>();
		try {
			stmt = connection.createStatement();
			String query = "select invoice_method_id,scheme_value_type from st_se_invoicing_methods;";
			logger.info("getAgentInvoicingMethod Query - "+query);
			rs = stmt.executeQuery(query);
			while(rs.next()) {
				methodIdMap.put(rs.getInt("invoice_method_id"), rs.getString("scheme_value_type"));
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

		return methodIdMap;
	}

	public void updateInvoicingMethod(int gameId, List<AgentInvoicingMethodBean> agentDetailList, int doneByUserId, String requestIp, Connection connection) throws LMSException {
		PreparedStatement insertPstmt = null;
		PreparedStatement updatePstmt = null;
		try {
			String insertQuery = "INSERT INTO st_se_org_game_invoice_method_history (retailer_id, game_id, invoice_method_id, invoice_method_value, date_changed, changed_by_user_id) SELECT organization_id, game_id, invoice_method_id, invoice_method_value,'"+Util.getCurrentTimeString()+"',"+doneByUserId+" FROM st_se_org_game_invoice_methods WHERE game_id="+gameId+" AND organization_id IN (SELECT organization_id FROM st_lms_organization_master WHERE parent_id=? OR organization_id=?);";
			logger.info("updateInvoicingMethod Insert History Query - "+insertQuery);
			String updateQuery = "UPDATE st_se_org_game_invoice_methods SET invoice_method_id=?, invoice_method_value=? WHERE game_id="+gameId+" AND organization_id IN (SELECT organization_id FROM st_lms_organization_master WHERE parent_id=? OR organization_id=?);";
			logger.info("updateInvoicingMethod Update Query - "+updateQuery);

			insertPstmt = connection.prepareStatement(insertQuery);
			updatePstmt = connection.prepareStatement(updateQuery);
			for(AgentInvoicingMethodBean methodBean : agentDetailList) {
				insertPstmt.setInt(1, methodBean.getOrgId());
				insertPstmt.setInt(2, methodBean.getOrgId());
				insertPstmt.addBatch();

				updatePstmt.setInt(1, methodBean.getMethodId());
				updatePstmt.setString(2, methodBean.getMethodValue());
				updatePstmt.setInt(3, methodBean.getOrgId());
				updatePstmt.setInt(4, methodBean.getOrgId());
				updatePstmt.addBatch();
			}
			insertPstmt.executeBatch();
			updatePstmt.executeBatch();
		} catch (SQLException ex) {
			ex.printStackTrace();
			throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE, LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE, LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closePstmt(insertPstmt);
			DBConnect.closePstmt(updatePstmt);
		}
	}
}