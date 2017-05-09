package com.skilrock.lms.coreEngine.scratchService.reportsMgmt.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.beans.SalePwtReportsBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;

public class ScratchSaleReportHelper implements IScratchSaleReportHelper{
	Log logger = LogFactory.getLog(ScratchSaleReportHelper.class);

	public List<SalePwtReportsBean> scratchSaleAgentWise(Timestamp startDate,
			Timestamp endDate) throws SQLException {
		Connection con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		SalePwtReportsBean reportsBean = null;
		List<SalePwtReportsBean> beanList = new ArrayList<SalePwtReportsBean>();
		try {
			String saleQry = "";
			logger.info("----Type Select ---"+ LMSFilterDispatcher.seSaleReportType);
			
			if ("BOOK_WISE".equals(LMSFilterDispatcher.seSaleReportType)) {
				saleQry = "select organization_id,"+QueryManager.getOrgCodeQuery()+",ifnull(mrpAmt,0.0) mrpAmt,ifnull(netAmt,0.0) netAmt from st_lms_organization_master left outer join (select sale.agent_org_id,sum(ifnull(mrpAmt,0.0)-ifnull(mrpAmtRet,0.0)) mrpAmt,sum(ifnull(netAmt,0.0)-ifnull(netAmtRet,0.0)) netAmt from (select agent_org_id,sum(mrpAmt) mrpAmt,sum(netAmt) netAmt,0.0 mrpAmtRet,0.0 netAmtRet from(select agent_org_id,sum(mrp_amt) mrpAmt,sum(net_amt) netAmt,0.0 mrpAmtRet,0.0 netAmtRet from st_se_bo_agent_transaction where transaction_id in (select transaction_id from st_lms_bo_transaction_master where transaction_type='SALE' and transaction_date>='"
					+ startDate
					+ "' and transaction_date<='"
					+ endDate
					+ "') group by agent_org_id union all select agent_org_id,sum(mrp_amt) mrpAmt,sum(net_amt) netAmt,0.0 mrpAmtRet,0.0 netAmtRet from st_se_bo_agent_loose_book_transaction where transaction_id in (select transaction_id from st_lms_bo_transaction_master where transaction_type='LOOSE_SALE' and transaction_date>='"
					+ startDate
					+ "' and transaction_date<='"
					+ endDate
					+ "') group by agent_org_id)sale  group by agent_org_id union all select agent_org_id,0.0 mrpAmt,0.0 netAmt,sum(mrpAmtRet) mrpAmtRet,sum(netAmtRet) netAmtRet from(select agent_org_id,0.0 mrpAmt,0.0 netAmt,sum(mrp_amt) mrpAmtRet,sum(net_amt) netAmtRet from st_se_bo_agent_transaction where transaction_id in (select transaction_id from st_lms_bo_transaction_master where transaction_type='SALE_RET' and transaction_date>='"
					+ startDate
					+ "' and transaction_date<='"
					+ endDate
					+ "') group by agent_org_id union all select agent_org_id,0.0 mrpAmt,0.0 netAmt,sum(mrp_amt) mrpAmtRet,sum(net_amt) netAmtRet from st_se_bo_agent_loose_book_transaction where transaction_id in (select transaction_id from st_lms_bo_transaction_master where transaction_type='LOOSE_SALE_RET' and transaction_date>='"
					+ startDate
					+ "' and transaction_date<='"
					+ endDate
					+ "') group by agent_org_id)saleRet group by agent_org_id ) sale group by agent_org_id) saleTlb on organization_id=agent_org_id where organization_type='AGENT' order by "+QueryManager.getAppendOrgOrder();
			} else if ("TICKET_WISE"
					.equals(LMSFilterDispatcher.seSaleReportType)) {
				saleQry = "select organization_id,"+QueryManager.getOrgCodeQuery()+",ifnull(mrpAmt,0.0) mrpAmt,ifnull(netAmt,0.0) netAmt from st_lms_organization_master left outer join (select current_owner_id,sum(soldTkt*ticket_price) mrpAmt,sum((soldTkt*ticket_price)-(soldTkt*ticket_price*transacrion_sale_comm_rate*0.01)) netAmt from st_se_game_master gm,st_se_game_inv_detail gid,(select game_id,book_nbr,sum(sold_tickets) soldTkt from st_se_game_ticket_inv_history where date>='"
						+ startDate
						+ "' and date<='"
						+ endDate
						+ "' and current_owner='RETAILER' group by book_nbr) TktTlb where gm.game_id=TktTlb.game_id and TktTlb.book_nbr=gid.book_nbr and gid.current_owner='AGENT' group by current_owner_id) saleTlb on organization_id=current_owner_id where organization_type='AGENT' order by "+QueryManager.getAppendOrgOrder();
			}
			pstmt = con.prepareStatement(saleQry);
			logger.info("----Agent Qry---" + pstmt);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				reportsBean = new SalePwtReportsBean();
				reportsBean.setGameName(rs.getString("orgCode"));
				reportsBean.setGameNo(rs.getInt("organization_id"));
				reportsBean.setSaleMrpAmt(rs.getDouble("mrpAmt"));
				reportsBean.setSaleNetAmt(rs.getDouble("netAmt"));
				beanList.add(reportsBean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.close();
		}
		return beanList;
	}

	public List<SalePwtReportsBean> scratchSaleAgentWiseExpand(
			Timestamp startDate, Timestamp endDate, int agentOrgId)
			throws SQLException {
		Connection con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		SalePwtReportsBean reportsBean = null;
		List<SalePwtReportsBean> beanList = new ArrayList<SalePwtReportsBean>();
		try {
			String saleQry = "";
			logger.info("----Type Select ---"
					+ LMSFilterDispatcher.seSaleReportType);
		if ("BOOK_WISE".equals(LMSFilterDispatcher.seSaleReportType)) {
				saleQry = "select game_nbr gameNo,game_name gameName,ifnull((nbr_of_tickets_per_book*ticket_price),0.0) unitPriceAmt,ifnull(noOfTkt,0) noOfTkt,ifnull(mrpAmt,0.0) mrpAmt,ifnull(netAmt,0.0) netAmt,ifnull(noLosTkts,0) noOfLosTkts from st_se_game_master gm left outer join (select sale.game_id,sum(ifnull(noOfTkt,0)-ifnull(noOfTktRet,0)) noOfTkt,sum(ifnull(mrpAmt,0.0)-ifnull(mrpAmtRet,0.0)) mrpAmt,sum(ifnull(netAmt,0.0)-ifnull(netAmtRet,0.0)) netAmt,sum(ifnull(noLosTkts,0)-ifnull(noLosTktsRet,0)) noLosTkts from (select game_id, sum(noLosTkts) noLosTkts, sum(noOfTkt) noOfTkt,sum(mrpAmt) mrpAmt,sum(netAmt) netAmt,0 noLosTktsRet,0 noOfTktRet,0.0 mrpAmtRet,0.0 netAmtRet from (select game_id, 0 noLosTkts, sum(nbr_of_books) noOfTkt,sum(mrp_amt) mrpAmt,sum(net_amt) netAmt,0 noOfTktRet,0.0 mrpAmtRet,0.0 netAmtRet from st_se_bo_agent_transaction where transaction_id in (select transaction_id from st_lms_bo_transaction_master where transaction_type='SALE' and transaction_date>='"
					+ startDate
					+ "' and transaction_date<='"
					+ endDate
					+ "') and agent_org_id="
					+agentOrgId
					+" group by game_id union all select game_id,sum(nbrOfTickets) noLosTkts ,0 noOfTkt ,sum(mrp_amt) mrpAmt,sum(net_amt) netAmt,0 noOfTktRet,0.0 mrpAmtRet,0.0 netAmtRet from st_se_bo_agent_loose_book_transaction where transaction_id in (select transaction_id from st_lms_bo_transaction_master where transaction_type='LOOSE_SALE' and transaction_date>='"
					+ startDate
					+ "' and transaction_date<='"
					+ endDate
					+ "') and agent_org_id="
					+agentOrgId
					+" group by game_id ) sale group by game_id union all select game_id,sum(noLosTkts) noLosTkts,sum(noOfTkt) noOfTkt,sum(mrpAmt) mrpAmt,sum(netAmt) netAmt,sum(noLosTktsRet) noLosTktsRet,sum(noOfTktRet) noOfTktRet,sum(mrpAmtRet) mrpAmtRet,sum(netAmtRet) netAmtRet from (select game_id,0 noLosTkts,0 noOfTkt,0.0 mrpAmt,0.0 netAmt,0 noLosTktsRet,sum(nbr_of_books) noOfTktRet,sum(mrp_amt) mrpAmtRet,sum(net_amt) netAmtRet from st_se_bo_agent_transaction where transaction_id in (select transaction_id from st_lms_bo_transaction_master where transaction_type='SALE_RET' and transaction_date>='"
					+ startDate
					+ "' and transaction_date<='"
					+ endDate
					+ "') and agent_org_id="
					+agentOrgId
					+" group by game_id  union all select game_id,0 noLosTkts,0 noOfTkt,0.0 mrpAmt,0.0 netAmt,sum(nbrOfTickets) noLosTktsRet,0 noOfTktRet,sum(mrp_amt) mrpAmtRet,sum(net_amt) netAmtRet  from st_se_bo_agent_loose_book_transaction where transaction_id in (select transaction_id from st_lms_bo_transaction_master where transaction_type='LOOSE_SALE_RET' and transaction_date>='"
					+ startDate
					+ "' and transaction_date<='"
					+ endDate
					+ "') and agent_org_id="
					+agentOrgId
					+" group by game_id) saleRet group by game_id ) sale group by game_id) saleTlb on saleTlb.game_id=gm.game_id";
				
				pstmt = con.prepareStatement(saleQry);
				logger.info("----Agent Expand Qry Book Wise---" + pstmt);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					reportsBean = new SalePwtReportsBean();
					reportsBean.setGameName(rs.getString("gameName"));
					reportsBean.setGameNo(rs.getInt("gameNo"));
					reportsBean.setSaleMrpAmt(rs.getDouble("mrpAmt"));
					reportsBean.setSaleNetAmt(rs.getDouble("netAmt"));
					reportsBean.setUnitPriceAmt(rs.getDouble("unitPriceAmt"));
					reportsBean.setNoOfTkt(rs.getInt("noOfTkt"));
					reportsBean.setNoOfLosTkt(rs.getInt("noOfLosTkts"));
					
					beanList.add(reportsBean);
				}
			} else if ("TICKET_WISE"
					.equals(LMSFilterDispatcher.seSaleReportType)) {
				saleQry = "select game_nbr gameNo,game_name gameName,ticket_price unitPriceAmt,sum(soldTkt) noOfTkt,sum(soldTkt*ticket_price) mrpAmt,sum((soldTkt*ticket_price)-(soldTkt*ticket_price*transacrion_sale_comm_rate*0.01)) netAmt from st_se_game_master gm,st_se_game_inv_detail gid,(select game_id,book_nbr,sum(sold_tickets) soldTkt from st_se_game_ticket_inv_history where date>='"
						+ startDate
						+ "' and date<='"
						+ endDate
						+ "' and current_owner_id in(select organization_id from st_lms_organization_master where parent_id="
						+ agentOrgId
						+ ") group by book_nbr) TktTlb where gm.game_id=TktTlb.game_id and TktTlb.book_nbr=gid.book_nbr and gid.current_owner='AGENT' group by gm.game_id";
				
				
				pstmt = con.prepareStatement(saleQry);
				logger.info("----Agent Expand Qry Ticket Wise---" + pstmt);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					reportsBean = new SalePwtReportsBean();
					reportsBean.setGameName(rs.getString("gameName"));
					reportsBean.setGameNo(rs.getInt("gameNo"));
					reportsBean.setSaleMrpAmt(rs.getDouble("mrpAmt"));
					reportsBean.setSaleNetAmt(rs.getDouble("netAmt"));
					reportsBean.setUnitPriceAmt(rs.getDouble("unitPriceAmt"));
					reportsBean.setNoOfTkt(rs.getInt("noOfTkt"));
					beanList.add(reportsBean);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.close();
		}
		return beanList;
	}

	public List<SalePwtReportsBean> scratchSaleGameWise(Timestamp startDate,
			Timestamp endDate) throws SQLException {
		logger.info("---Scratch Sale Report Game Wise Helper---");
		Connection con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		SalePwtReportsBean reportsBean = null;
		List<SalePwtReportsBean> beanList = new ArrayList<SalePwtReportsBean>();
		try {
			String saleQry = "";
			logger.info("----Type Select ---"
					+ LMSFilterDispatcher.seSaleReportType);
			if ("BOOK_WISE".equals(LMSFilterDispatcher.seSaleReportType)) {
				saleQry = "select game_nbr gameNo,game_name gameName,ifnull(mrpAmt,0.0) mrpAmt,ifnull(netAmt,0.0) netAmt from st_se_game_master gm left outer join (select game_id,sum(ifnull(mrpAmt,0.0)-ifnull(mrpAmtRet,0.0)) mrpAmt,sum(ifnull(netAmt,0.0)-ifnull(netAmtRet,0.0)) netAmt from (select game_id,sum(mrpAmt) mrpAmt,sum(netAmt) netAmt,0.0 mrpAmtRet,0.0 netAmtRet  from (select game_id,sum(mrp_amt) mrpAmt,sum(net_amt) netAmt,0.0 mrpAmtRet,0.0 netAmtRet from st_se_bo_agent_transaction where transaction_id in (select transaction_id from st_lms_bo_transaction_master where transaction_type='SALE' and transaction_date>='"
					+ startDate
					+ "' and transaction_date<='"
					+ endDate
					+ "') group by game_id union all select game_id,sum(mrp_amt) mrpAmt,sum(net_amt) netAmt,0.0 mrpAmtRet,0.0 netAmtRet from st_se_bo_agent_loose_book_transaction where transaction_id in (select transaction_id from st_lms_bo_transaction_master where transaction_type='LOOSE_SALE' and transaction_date>='"
					+ startDate
					+ "' and transaction_date<='"
					+ endDate
					+ "') group by game_id )sale group by game_id union all select game_id,0.0 mrpAmt,0.0 netAmt,sum(mrpAmtRet) mrpAmtRet,sum(netAmtRet) netAmtRet from (select game_id,0.0 mrpAmt,0.0 netAmt,sum(mrp_amt) mrpAmtRet,sum(net_amt) netAmtRet from st_se_bo_agent_transaction where transaction_id in (select transaction_id from st_lms_bo_transaction_master where transaction_type='SALE_RET' and transaction_date>='"
					+ startDate
					+ "' and transaction_date<='"
					+ endDate
					+ "') group by game_id union all select game_id,0.0 mrpAmt,0.0 netAmt,sum(mrp_amt) mrpAmtRet,sum(net_amt) netAmtRet from st_se_bo_agent_loose_book_transaction where transaction_id in (select transaction_id from st_lms_bo_transaction_master where transaction_type='LOOSE_SALE_RET' and transaction_date>='"
					+ startDate
					+ "' and transaction_date<='"
					+ endDate
					+ "') group by game_id) saleRet group by game_id ) sale group by game_id) saleTlb on saleTlb.game_id=gm.game_id";
			} else if ("TICKET_WISE"
					.equals(LMSFilterDispatcher.seSaleReportType)) {
				saleQry = "select game_nbr gameNo,game_name gameName,sum(soldTkt*ticket_price) mrpAmt,sum((soldTkt*ticket_price)-(soldTkt*ticket_price*transacrion_sale_comm_rate*0.01)) netAmt from st_se_game_master gm,st_se_game_inv_detail gid,(select game_id,book_nbr,sum(sold_tickets) soldTkt from st_se_game_ticket_inv_history where date>='"
						+ startDate
						+ "' and date<='"
						+ endDate
						+ "' and current_owner='RETAILER' group by book_nbr) TktTlb where gm.game_id=TktTlb.game_id and TktTlb.book_nbr=gid.book_nbr and gid.current_owner='AGENT' group by gm.game_id";
			}
			pstmt = con.prepareStatement(saleQry);
			logger.info("----Game Qry---" + pstmt);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				reportsBean = new SalePwtReportsBean();
				reportsBean.setGameName(rs.getString("gameName"));
				reportsBean.setGameNo(rs.getInt("gameNo"));
				reportsBean.setSaleMrpAmt(rs.getDouble("mrpAmt"));
				reportsBean.setSaleNetAmt(rs.getDouble("netAmt"));
				beanList.add(reportsBean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.close();
		}
		return beanList;
	}

	public List<SalePwtReportsBean> scratchSaleGameWiseExpand(
			Timestamp startDate, Timestamp endDate) throws SQLException {
		logger.info("---Scratch Sale Report Game Wise Expand Helper---");
		Connection con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		SalePwtReportsBean reportsBean = null;
		List<SalePwtReportsBean> beanList = new ArrayList<SalePwtReportsBean>();
		try {

			String saleQry = "";
			logger.info("----Type Select ---"
					+ LMSFilterDispatcher.seSaleReportType);
		
			if ("BOOK_WISE".equals(LMSFilterDispatcher.seSaleReportType)) {
				saleQry = "select game_nbr gameNo,game_name gameName,ifnull((nbr_of_tickets_per_book*ticket_price),0.0) unitPriceAmt,ifnull(noOfTkt,0) noOfTkt,ifnull(mrpAmt,0.0) mrpAmt,ifnull(netAmt,0.0) netAmt,ifnull(noLosTkts,0) noOfLosTkts from st_se_game_master gm left outer join (select sale.game_id,sum(ifnull(noOfTkt,0)-ifnull(noOfTktRet,0)) noOfTkt,sum(ifnull(mrpAmt,0.0)-ifnull(mrpAmtRet,0.0)) mrpAmt,sum(ifnull(netAmt,0.0)-ifnull(netAmtRet,0.0)) netAmt,sum(ifnull(noLosTkts,0)-ifnull(noLosTktsRet,0)) noLosTkts from (select game_id, sum(noLosTkts) noLosTkts, sum(noOfTkt) noOfTkt,sum(mrpAmt) mrpAmt,sum(netAmt) netAmt,0 noLosTktsRet,0 noOfTktRet,0.0 mrpAmtRet,0.0 netAmtRet from (select game_id, 0 noLosTkts, sum(nbr_of_books) noOfTkt,sum(mrp_amt) mrpAmt,sum(net_amt) netAmt,0 noOfTktRet,0.0 mrpAmtRet,0.0 netAmtRet from st_se_bo_agent_transaction where transaction_id in (select transaction_id from st_lms_bo_transaction_master where transaction_type='SALE' and transaction_date>='"
					+ startDate
					+ "' and transaction_date<='"
					+ endDate
					+ "') group by game_id union all select game_id,sum(nbrOfTickets) noLosTkts ,0 noOfTkt ,sum(mrp_amt) mrpAmt,sum(net_amt) netAmt,0 noOfTktRet,0.0 mrpAmtRet,0.0 netAmtRet from st_se_bo_agent_loose_book_transaction where transaction_id in (select transaction_id from st_lms_bo_transaction_master where transaction_type='LOOSE_SALE' and transaction_date>='"
					+ startDate
					+ "' and transaction_date<='"
					+ endDate
					+ "') group by game_id ) sale group by game_id union all select game_id,sum(noLosTkts) noLosTkts,sum(noOfTkt) noOfTkt,sum(mrpAmt) mrpAmt,sum(netAmt) netAmt,sum(noLosTktsRet) noLosTktsRet,sum(noOfTktRet) noOfTktRet,sum(mrpAmtRet) mrpAmtRet,sum(netAmtRet) netAmtRet from (select game_id,0 noLosTkts,0 noOfTkt,0.0 mrpAmt,0.0 netAmt,0 noLosTktsRet,sum(nbr_of_books) noOfTktRet,sum(mrp_amt) mrpAmtRet,sum(net_amt) netAmtRet from st_se_bo_agent_transaction where transaction_id in (select transaction_id from st_lms_bo_transaction_master where transaction_type='SALE_RET' and transaction_date>='"
					+ startDate
					+ "' and transaction_date<='"
					+ endDate
					+ "') group by game_id  union all select game_id,0 noLosTkts,0 noOfTkt,0.0 mrpAmt,0.0 netAmt,sum(nbrOfTickets) noLosTktsRet,0 noOfTktRet,sum(mrp_amt) mrpAmtRet,sum(net_amt) netAmtRet  from st_se_bo_agent_loose_book_transaction where transaction_id in (select transaction_id from st_lms_bo_transaction_master where transaction_type='LOOSE_SALE_RET' and transaction_date>='"
					+ startDate
					+ "' and transaction_date<='"
					+ endDate
					+ "') group by game_id) saleRet group by game_id ) sale group by game_id) saleTlb on saleTlb.game_id=gm.game_id";
				pstmt = con.prepareStatement(saleQry);
				logger.info("----Game Expand Qry Book Wise---" + pstmt);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					reportsBean = new SalePwtReportsBean();
					reportsBean.setGameName(rs.getString("gameName"));
					reportsBean.setGameNo(rs.getInt("gameNo"));
					reportsBean.setSaleMrpAmt(rs.getDouble("mrpAmt"));
					reportsBean.setSaleNetAmt(rs.getDouble("netAmt"));
					reportsBean.setUnitPriceAmt(rs.getDouble("unitPriceAmt"));
					reportsBean.setNoOfTkt(rs.getInt("noOfTkt"));
					reportsBean.setNoOfLosTkt(rs.getInt("noOfLosTkts"));
					beanList.add(reportsBean);
				}
			} else if ("TICKET_WISE"
					.equals(LMSFilterDispatcher.seSaleReportType)) {
				saleQry = "select game_nbr gameNo,game_name gameName,ticket_price unitPriceAmt,sum(soldTkt) noOfTkt,sum(soldTkt*ticket_price) mrpAmt,sum((soldTkt*ticket_price)-(soldTkt*ticket_price*transacrion_sale_comm_rate*0.01)) netAmt from st_se_game_master gm,st_se_game_inv_detail gid,(select game_id,book_nbr,sum(sold_tickets) soldTkt from st_se_game_ticket_inv_history where date>='"
						+ startDate
						+ "' and date<='"
						+ endDate
						+ "' and current_owner='RETAILER' group by book_nbr) TktTlb where gm.game_id=TktTlb.game_id and TktTlb.book_nbr=gid.book_nbr and gid.current_owner='AGENT' group by gm.game_id";
				
				pstmt = con.prepareStatement(saleQry);
				logger.info("----Game Expand Qry Ticket Wise---" + pstmt);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					reportsBean = new SalePwtReportsBean();
					reportsBean.setGameName(rs.getString("gameName"));
					reportsBean.setGameNo(rs.getInt("gameNo"));
					reportsBean.setSaleMrpAmt(rs.getDouble("mrpAmt"));
					reportsBean.setSaleNetAmt(rs.getDouble("netAmt"));
					reportsBean.setUnitPriceAmt(rs.getDouble("unitPriceAmt"));
					reportsBean.setNoOfTkt(rs.getInt("noOfTkt"));
					beanList.add(reportsBean);
				}
			}

		
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.close();
		}
		return beanList;
	}

	public String getOrgAdd(int orgId) throws LMSException {
		String orgAdd = "";
		Connection con = null;
		con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = con
					.prepareStatement("select addr_line1, addr_line2, city from st_lms_organization_master where organization_id = ?");
			pstmt.setInt(1, orgId);
			rs = pstmt.executeQuery();
			logger.debug(pstmt);
			while (rs.next()) {
				orgAdd = rs.getString("addr_line1") + ", "
						+ rs.getString("addr_line2") + ", "
						+ rs.getString("city");
			}
		} catch (SQLException e) {
			logger.error("Exception: " + e);
			e.printStackTrace();
			throw new LMSException(e);
		} finally {
			try {
				if (con != null && !con.isClosed()) {
					con.close();
				}
			} catch (SQLException e) {
				logger.error("Exception: " + e);
				e.printStackTrace();
				throw new LMSException(e);
			}
		}
		return orgAdd;
	}

	public Map<Integer, List<String>> fetchOrgAddMap() throws LMSException {
		Map<Integer, List<String>> map = new TreeMap<Integer, List<String>>();
		Connection con = null;
		String orgAdd = "";
		con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<String> tempList = null;
		try {
			pstmt = con
					.prepareStatement("select organization_id, name,addr_line1, addr_line2, city from st_lms_organization_master where organization_type = 'AGENT'");
			rs = pstmt.executeQuery();
			logger.debug(pstmt);
			while (rs.next()) {
				tempList = new ArrayList<String>();
				orgAdd = rs.getString("addr_line1") + ", "
						+ rs.getString("addr_line2") + ", "
						+ rs.getString("city");
				tempList.add(rs.getString("name"));
				tempList.add(orgAdd);
				map.put(rs.getInt("organization_id"), tempList);
			}
		} catch (SQLException e) {
			logger.error("Exception: " + e);
			e.printStackTrace();
			throw new LMSException(e);
		} finally {
			try {
				if (con != null && !con.isClosed()) {
					con.close();
				}
			} catch (SQLException e) {
				logger.error("Exception: " + e);
				e.printStackTrace();
				throw new LMSException(e);
			}
		}
		return map;
	}
}
