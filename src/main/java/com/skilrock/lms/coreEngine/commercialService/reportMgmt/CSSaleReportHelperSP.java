package com.skilrock.lms.coreEngine.commercialService.reportMgmt;

import java.sql.CallableStatement;
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

import com.skilrock.lms.beans.CSSaleReportBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSException;

public class CSSaleReportHelperSP implements CSSaleReportIF {

	Log logger = LogFactory.getLog(CSSaleReportHelperSP.class);

	public List<CSSaleReportBean> CSSaleCategoryWise(Timestamp startDate,
			Timestamp endDate) throws SQLException{
		Connection con = DBConnect.getConnection();
		CallableStatement cstmt = null;
		ResultSet rs = null;
		CSSaleReportBean reportsBean = null;
		List<CSSaleReportBean> beanList = new ArrayList<CSSaleReportBean>();
		try {
			cstmt = con.prepareCall("call csSaleCategoryWise(?,?)");
			cstmt.setTimestamp(1, startDate);
			cstmt.setTimestamp(2, endDate);
		logger.debug("----CS Report Category Wise Qry---" + cstmt);
		rs = cstmt.executeQuery();
		while (rs.next()) {
			reportsBean = new CSSaleReportBean();
			reportsBean.setCategoryCode(rs.getString("category_code"));
			reportsBean.setCategoryId(rs.getInt("category_id"));
			reportsBean.setMrpAmt(rs.getDouble("mrp"));
			reportsBean.setNetAmt(rs.getDouble("buyCost"));
			beanList.add(reportsBean);
		}
	} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.close();
		}
		return beanList;
	}
	
	public List<CSSaleReportBean> CSSaleAgentWise(Timestamp startDate,
			Timestamp endDate) throws SQLException {
		Connection con = DBConnect.getConnection();
		CallableStatement cstmt = null;
		ResultSet rs = null;
		CSSaleReportBean reportsBean = null;
		List<CSSaleReportBean> beanList = new ArrayList<CSSaleReportBean>();
		try {


		cstmt = con.prepareCall("call csSaleAgentWise(?,?)");
		cstmt.setTimestamp(1, startDate);
		cstmt.setTimestamp(2, endDate);
		logger.debug("----CS Report Agent Wise Qry---" + cstmt);
		rs = cstmt.executeQuery();
		while (rs.next()) {
			reportsBean = new CSSaleReportBean();
			reportsBean.setPartyName(rs.getString("orgCode"));
			reportsBean.setCategoryId(rs.getInt("organization_id"));
			reportsBean.setMrpAmt(rs.getDouble("mrp"));
			reportsBean.setNetAmt(rs.getDouble("netAmt"));
			reportsBean.setBuyCost(rs.getDouble("buyCost"));
			beanList.add(reportsBean);
		}
	} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.close();
		}
		return beanList;
	}
	
	public List<CSSaleReportBean> CSSaleRetailerWise(Timestamp startDate,
			Timestamp endDate,int agtOrgId) throws SQLException {
		Connection con = DBConnect.getConnection();
		CallableStatement cstmt = null;
		ResultSet rs = null;
		CSSaleReportBean reportsBean = null;
		List<CSSaleReportBean> beanList = new ArrayList<CSSaleReportBean>();
		try {
		cstmt = con.prepareCall("call csSaleRetailerWise(?,?,?)");
		cstmt.setTimestamp(1, startDate);
		cstmt.setTimestamp(2, endDate);
		cstmt.setInt(3, agtOrgId);
		logger.debug("----CS Report Retailer Wise Qry---" + cstmt);
		rs = cstmt.executeQuery();
		while (rs.next()) {
			reportsBean = new CSSaleReportBean();
			reportsBean.setPartyName(rs.getString("name"));
			reportsBean.setCategoryId(rs.getInt("organization_id"));
			reportsBean.setMrpAmt(rs.getDouble("mrp"));
			reportsBean.setBuyCost(rs.getDouble("buyCost"));
			reportsBean.setNetAmt(rs.getDouble("net"));
			beanList.add(reportsBean);
		}
	} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.close();
		}
		return beanList;
	}

	/*public List<CSSaleReportBean> drawSaleAgentWiseExpand(
			Timestamp startDate, Timestamp endDate, int agentOrgId)
			throws SQLException {
		Connection con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		CSSaleReportBean reportsBean = null;
		List<CSSaleReportBean> beanList = new ArrayList<CSSaleReportBean>();
		try {
			String indGameQry = "(select game_id,unitPriceAmt,noOfTkt,mrpAmt,netAmt from (select sale.game_id,unitPriceAmt,(ifnull(noOfTkt,0)-ifnull(noOfTktRet,0)) noOfTkt,(ifnull(mrpAmt,0.0)-ifnull(mrpAmtRet,0.0))mrpAmt,(ifnull(netAmt,0.0)-ifnull(netAmtRet,0.0))netAmt from (select game_id,mrp_amt unitPriceAmt,count(mrp_amt) noOfTkt,sum(mrp_amt) mrpAmt,sum(agent_net_amt) netAmt from st_cs_sale_* where transaction_id in(select transaction_id from st_lms_retailer_transaction_master where transaction_type ='CS_SALE' and transaction_date>='"
					+ startDate
					+ "' and transaction_date<='"
					+ endDate
					+ "' ) and retailer_org_id in (select organization_id from st_lms_organization_master where parent_id ="
					+ agentOrgId
					+ " )  group by mrp_amt order by mrp_amt) sale left outer join (select game_id,mrp_amt unitPriceAmtRet,count(mrp_amt) noOfTktRet,sum(mrp_amt) mrpAmtRet,sum(agent_net_amt) netAmtRet from st_dg_ret_sale_refund_* where transaction_id in(select transaction_id from st_lms_retailer_transaction_master where transaction_type in('CS_CANCEL_SERVER','CS_CANCEL_RET') and transaction_date>='"
					+ startDate
					+ "' and transaction_date<='"
					+ endDate
					+ "' ) and retailer_org_id in (select organization_id from st_lms_organization_master where parent_id ="
					+ agentOrgId
					+ " )  group by mrp_amt order by mrp_amt) saleRet on unitPriceAmt=unitPriceAmtRet) sale where noOfTkt!=0) union all ";
			StringBuilder saleQry = new StringBuilder(
					"select product_id, product_number,product_code,unit_price,noOfTkt,mrpAmt,netAmt from st_cs_product_master pm,(");
			String gameQry = "select product_num from st_cs_product_master";
			PreparedStatement pstmtGame = con.prepareStatement(gameQry);
			ResultSet rsGame = pstmtGame.executeQuery();

			while (rsGame.next()) {
				saleQry.append(indGameQry.replaceAll("\\*", rsGame
						.getString("product_num")));
			}
			saleQry.delete(saleQry.length() - 10, saleQry.length());
			saleQry.append(") saleTlb where gm.game_nbr=saleTlb.game_id");

			pstmt = con.prepareStatement(saleQry.toString());
			logger.info("----Game Expand Qry---" + pstmt);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				/*reportsBean = new CSSaleReportBean();
				reportsBean.setProductCode(rs.getString("gameName"));
				reportsBean.setGameNo(rs.getInt("gameNo"));
				reportsBean.setSaleMrpAmt(rs.getDouble("mrpAmt"));
				reportsBean.setSaleNetAmt(rs.getDouble("netAmt"));
				reportsBean.setUnitPriceAmt(rs.getDouble("unitPriceAmt"));
				reportsBean.setNoOfTkt(rs.getInt("noOfTkt"));
				beanList.add(reportsBean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.close();
		}
		return beanList;
	}
	
	public List<CSSaleReportBean> drawSaleRetailerWiseExpand(
			Timestamp startDate, Timestamp endDate, int agentOrgId)
			throws SQLException {
		Connection con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		CSSaleReportBean reportsBean = null;
		List<CSSaleReportBean> beanList = new ArrayList<CSSaleReportBean>();
		try {
			String indGameQry = "(select game_id,unitPriceAmt,noOfTkt,mrpAmt,netAmt from (select sale.game_id,unitPriceAmt,(ifnull(noOfTkt,0)-ifnull(noOfTktRet,0)) noOfTkt,(ifnull(mrpAmt,0.0)-ifnull(mrpAmtRet,0.0))mrpAmt,(ifnull(netAmt,0.0)-ifnull(netAmtRet,0.0))netAmt from (select game_id,mrp_amt unitPriceAmt,count(mrp_amt) noOfTkt,sum(mrp_amt) mrpAmt,sum(net_amt) netAmt from st_dg_ret_sale_* where transaction_id in(select transaction_id from st_lms_retailer_transaction_master where transaction_type in('DG_SALE','DG_SALE_OFFLINE') and transaction_date>='"
					+ startDate
					+ "' and transaction_date<='"
					+ endDate
					+ "' ) and retailer_org_id in (select organization_id from st_lms_organization_master where organization_id ="
					+ agentOrgId
					+ " )  group by mrp_amt order by mrp_amt) sale left outer join (select game_id,mrp_amt unitPriceAmtRet,count(mrp_amt) noOfTktRet,sum(mrp_amt) mrpAmtRet,sum(net_amt) netAmtRet from st_dg_ret_sale_refund_* where transaction_id in(select transaction_id from st_lms_retailer_transaction_master where transaction_type in('DG_REFUND_CANCEL','DG_REFUND_FAILED') and transaction_date>='"
					+ startDate
					+ "' and transaction_date<='"
					+ endDate
					+ "' ) and retailer_org_id in (select organization_id from st_lms_organization_master where organization_id ="
					+ agentOrgId
					+ " )  group by mrp_amt order by mrp_amt) saleRet on unitPriceAmt=unitPriceAmtRet) sale where noOfTkt!=0 ) union all ";
			StringBuilder saleQry = new StringBuilder(
					"select game_nbr gameNo,game_name gameName,unitPriceAmt,noOfTkt,mrpAmt,netAmt from st_dg_game_master gm,(");
			String gameQry = "select game_nbr from st_dg_game_master";
			PreparedStatement pstmtGame = con.prepareStatement(gameQry);
			ResultSet rsGame = pstmtGame.executeQuery();

			while (rsGame.next()) {
				saleQry.append(indGameQry.replaceAll("\\*", rsGame
						.getString("game_nbr")));
			}
			saleQry.delete(saleQry.length() - 10, saleQry.length());
			saleQry.append(") saleTlb where gm.game_nbr=saleTlb.game_id");

			pstmt = con.prepareStatement(saleQry.toString());
			logger.info("----Game Expand Qry---" + pstmt);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				/*reportsBean = new CSSaleReportBean();
				reportsBean.setGameName(rs.getString("gameName"));
				reportsBean.setGameNo(rs.getInt("gameNo"));
				reportsBean.setSaleMrpAmt(rs.getDouble("mrpAmt"));
				reportsBean.setSaleNetAmt(rs.getDouble("netAmt"));
				reportsBean.setUnitPriceAmt(rs.getDouble("unitPriceAmt"));
				reportsBean.setNoOfTkt(rs.getInt("noOfTkt"));
				beanList.add(reportsBean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.close();
		}
		return beanList;
	}*/

	public List<CSSaleReportBean> CSSaleProductWise(Timestamp startDate,
			Timestamp endDate, int catId) throws SQLException {
		logger.debug("---CS Sale Report Product Wise Helper---");
		Connection con = DBConnect.getConnection();
		CallableStatement cstmt = null;
		ResultSet rs = null;
		CSSaleReportBean reportsBean = null;
		List<CSSaleReportBean> beanList = new ArrayList<CSSaleReportBean>();
		try {
		cstmt = con.prepareCall("call csSaleProductWise(?,?,?)");
		cstmt.setTimestamp(1, startDate);
		cstmt.setTimestamp(2, endDate);
		cstmt.setInt(3,catId);
		logger.debug("----CS Report Product Wise Qry---" + cstmt);
		rs = cstmt.executeQuery();
		while (rs.next()) {
			reportsBean = new CSSaleReportBean();
			reportsBean.setProductCode(rs.getString("description"));
			reportsBean.setProvider(rs.getString("operator_name"));
			reportsBean.setDenomination(rs.getString("denomination"));
			reportsBean.setProductId(rs.getInt("product_id"));
			reportsBean.setMrpAmt(rs.getDouble("mrpAmt"));
			reportsBean.setBuyCost(rs.getDouble("buyCostAmt"));
			beanList.add(reportsBean);
		}
	} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.close();
		}
		return beanList;
	}
	
	public List<CSSaleReportBean> CSSaleProductWiseAgentWise(Timestamp startDate,
			Timestamp endDate, int agtOrgId) throws SQLException {
		logger.debug("---CS Sale Report Product Wise Agent Wise Helper---");
		Connection con = DBConnect.getConnection();
		CallableStatement cstmt = null;
		ResultSet rs = null;
		CSSaleReportBean reportsBean = null;
		List<CSSaleReportBean> beanList = new ArrayList<CSSaleReportBean>();
		try {
		cstmt = con.prepareCall("call csSaleProductWiseAgentWise(?,?,?)");
		cstmt.setTimestamp(1, startDate);
		cstmt.setTimestamp(2, endDate);
		cstmt.setInt(3, agtOrgId);
		logger.debug("----CS Report Product Wise Agent Wise Qry---" + cstmt);
		rs = cstmt.executeQuery();
		while (rs.next()) {
			reportsBean = new CSSaleReportBean();
			reportsBean.setProductCode(rs.getString("description"));
			reportsBean.setProvider(rs.getString("operator_name"));
			reportsBean.setDenomination(rs.getString("denomination"));
			reportsBean.setProductId(rs.getInt("product_id"));
			reportsBean.setMrpAmt(rs.getDouble("mrp"));
			reportsBean.setBuyCost(rs.getDouble("buyCost"));
			reportsBean.setNetAmt(rs.getDouble("net"));
			beanList.add(reportsBean);
		}
	} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.close();
		}
		return beanList;
	}
	
	public List<CSSaleReportBean> CSSaleProductWiseRetailerWise(Timestamp startDate,
			Timestamp endDate, int retOrgId)throws SQLException{
		logger.debug("---CS Sale Report Product Wise Retailer Wise Helper---");
		Connection con = DBConnect.getConnection();
		CallableStatement cstmt = null;
		ResultSet rs = null;
		CSSaleReportBean reportsBean = null;
		List<CSSaleReportBean> beanList = new ArrayList<CSSaleReportBean>();
		try {
			

		cstmt = con.prepareCall("call csSaleProductWiseRetailerwise(?,?,?)");
		cstmt.setTimestamp(1, startDate);
		cstmt.setTimestamp(2, endDate);
		cstmt.setInt(3, retOrgId);
		logger.debug("----CS Report Product Wise Retailer Wise Qry---" + cstmt);
		rs = cstmt.executeQuery();
		while (rs.next()) {
			reportsBean = new CSSaleReportBean();
			reportsBean.setProductCode(rs.getString("description"));
			reportsBean.setProvider(rs.getString("operator_name"));
			reportsBean.setDenomination(rs.getString("denomination"));
			reportsBean.setProductId(rs.getInt("product_id"));
			reportsBean.setMrpAmt(rs.getDouble("mrp"));
			reportsBean.setBuyCost(rs.getDouble("buyCost"));
			reportsBean.setNetAmt(rs.getDouble("net"));
			beanList.add(reportsBean);
		}
	} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.close();
		}
		return beanList;
	}

	public List<CSSaleReportBean> getCSSaleRetailerWise(Timestamp startDate,
			Timestamp endDate, int retOrgId)throws SQLException{
		logger.debug("---CS Sale Report Product Wise Retailer Wise Helper---");
		Connection con = DBConnect.getConnection();
		CallableStatement cstmt = null;
		ResultSet rs = null;
		CSSaleReportBean reportsBean = null;
		List<CSSaleReportBean> beanList = new ArrayList<CSSaleReportBean>();
		try {
			

		cstmt = con.prepareCall("call csSaleProductWiseRetailerwise(?,?,?)");
		cstmt.setTimestamp(1, startDate);
		cstmt.setTimestamp(2, endDate);
		cstmt.setInt(3, retOrgId);
		logger.debug("----CS Report Product Wise Retailer Wise Qry---" + cstmt);
		rs = cstmt.executeQuery();
		while (rs.next()) {
			reportsBean = new CSSaleReportBean();
			reportsBean.setProductCode(rs.getString("description"));
			reportsBean.setProvider(rs.getString("operator_name"));
			reportsBean.setDenomination(rs.getString("denomination"));
			reportsBean.setProductId(rs.getInt("product_id"));
			reportsBean.setMrpAmt(rs.getDouble("mrp"));
			reportsBean.setBuyCost(rs.getDouble("buyCost"));
			reportsBean.setNetAmt(rs.getDouble("net"));
			beanList.add(reportsBean);
		}
	} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.close();
		}
		return beanList;
	}
	/*public List<CSSaleReportBean> CSSaleProductWiseForAgent(Timestamp startDate,
			Timestamp endDate,int agtOrgId) throws SQLException {
		logger.debug("---Draw Sale Report Game Wise Helper---");
		Connection con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		CSSaleReportBean reportsBean = null;
		List<CSSaleReportBean> beanList = new ArrayList<CSSaleReportBean>();
		try {
			String indGameQry = "select sale.product_id,(ifnull(mrpAmt,0.0)-ifnull(mrpAmtRet,0.0))mrpAmt,(ifnull(netAmt,0.0)-ifnull(netAmtRet,0.0))netAmt from (select product_id,sum(mrp_amt) mrpAmt,sum(net_amt) netAmt from st_cs_sale_* where transaction_id in(select transaction_id from st_lms_retailer_transaction_master where transaction_type = 'CS_SALE' and transaction_date>='"
					+ startDate
					+ "' and transaction_date<='"
					+ endDate
					+ "' ) and retailer_org_id in(select organization_id from st_lms_organization_master where parent_id="+agtOrgId+") group by product_id) sale left outer join (select product_id,sum(mrp_amt) mrpAmtRet,sum(net_amt) netAmtRet from st_cs_refund_* where transaction_id in(select transaction_id from st_lms_retailer_transaction_master where transaction_type in('CS_CANCEL_SERVER','CS_CANCEL_RET') and transaction_date>='"
					+ startDate
					+ "' and transaction_date<='"
					+ endDate
					+ "' ) and retailer_org_id in(select organization_id from st_lms_organization_master where parent_id="+agtOrgId+") group by game_id)saleRet on sale.product_id=saleRet.product_id union all ";
			StringBuilder saleQry = new StringBuilder(
					"select product_id, product_num, product_code,provider,mrpAmt,netAmt from st_cs_product_master pm,(");
			String gameQry = "select category_id,product_num from st_cs_product_master";
			PreparedStatement pstmtGame = con.prepareStatement(gameQry);
			ResultSet rsGame = pstmtGame.executeQuery();

			while (rsGame.next()) {
				saleQry.append(indGameQry.replaceAll("\\*", rsGame
						.getString("category_id")));
			}
			saleQry.delete(saleQry.length() - 10, saleQry.length());
			saleQry.append(") saleTlb where pm.product_num=saleTlb.product_id");

			pstmt = con.prepareStatement(saleQry.toString());
			logger.debug("----Game Qry---" + pstmt);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				reportsBean = new CSSaleReportBean();
				reportsBean.setProductCode(rs.getString("product_code"));
				reportsBean.setProductNum(rs.getInt("product_num"));
				reportsBean.setProductId(rs.getInt("product_id"));
				reportsBean.setProvider(rs.getString("product_provider"));
				reportsBean.setMrpAmt(rs.getDouble("mrpAmt"));
				reportsBean.setNetAmt(rs.getDouble("netAmt"));
				beanList.add(reportsBean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.close();
		}
		return beanList;
	}*/
	
	/*public List<CSSaleReportBean> CSSaleGameWiseExpand(Timestamp startDate,
			Timestamp endDate) throws SQLException {
		logger.info("---Draw Sale Report Game Wise Expand Helper---");
		Connection con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		CSSaleReportBean reportsBean = null;
		List<CSSaleReportBean> beanList = new ArrayList<CSSaleReportBean>();
		try {
			String indGameQry = "(select game_id,unitPriceAmt,noOfTkt,mrpAmt,netAmt from (select sale.game_id,unitPriceAmt,(ifnull(noOfTkt,0)-ifnull(noOfTktRet,0)) noOfTkt,(ifnull(mrpAmt,0.0)-ifnull(mrpAmtRet,0.0)) mrpAmt,(ifnull(netAmt,0.0)-ifnull(netAmtRet,0.0)) netAmt from (select game_id,mrp_amt unitPriceAmt,count(mrp_amt) noOfTkt,sum(mrp_amt) mrpAmt,sum(agent_net_amt) netAmt from st_dg_ret_sale_* where transaction_id in(select transaction_id from st_lms_retailer_transaction_master where transaction_type in('DG_SALE','DG_SALE_OFFLINE') and transaction_date>='"
					+ startDate
					+ "' and transaction_date<='"
					+ endDate
					+ "' ) group by mrp_amt order by mrp_amt) sale left outer join (select game_id,mrp_amt unitPriceAmtRet,count(mrp_amt) noOfTktRet,sum(mrp_amt) mrpAmtRet,sum(agent_net_amt) netAmtRet from st_dg_ret_sale_refund_* where transaction_id in(select transaction_id from st_lms_retailer_transaction_master where transaction_type in('DG_REFUND_CANCEL','DG_REFUND_FAILED') and transaction_date>='"
					+ startDate
					+ "' and transaction_date<='"
					+ endDate
					+ "' ) group by mrp_amt order by mrp_amt) saleRet on sale.game_id=saleRet.game_id and unitPriceAmt=unitPriceAmtRet) sale where noOfTkt!=0) union all ";
			StringBuilder saleQry = new StringBuilder(
					"select game_nbr gameNo,game_name gameName,unitPriceAmt,noOfTkt,mrpAmt,netAmt from st_dg_game_master gm,(");
			String gameQry = "select game_nbr from st_dg_game_master";
			PreparedStatement pstmtGame = con.prepareStatement(gameQry);
			ResultSet rsGame = pstmtGame.executeQuery();

			while (rsGame.next()) {
				saleQry.append(indGameQry.replaceAll("\\*", rsGame
						.getString("game_nbr")));
			}
			saleQry.delete(saleQry.length() - 10, saleQry.length());
			saleQry.append(") saleTlb where gm.game_nbr=saleTlb.game_id");

			pstmt = con.prepareStatement(saleQry.toString());
			logger.info("----Game Expand Qry---" + pstmt);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				reportsBean = new CSSaleReportBean();
				reportsBean.setGameName(rs.getString("gameName"));
				reportsBean.setGameNo(rs.getInt("gameNo"));
				reportsBean.setSaleMrpAmt(rs.getDouble("mrpAmt"));
				reportsBean.setSaleNetAmt(rs.getDouble("netAmt"));
				reportsBean.setUnitPriceAmt(rs.getDouble("unitPriceAmt"));
				reportsBean.setNoOfTkt(rs.getInt("noOfTkt"));
				beanList.add(reportsBean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.close();
		}
		return beanList;
	}*/
	
	/*public List<CSSaleReportBean> drawSaleGameWiseExpandForAgent(Timestamp startDate,
			Timestamp endDate,int agtOrgId) throws SQLException {
		logger.info("---Draw Sale Report Game Wise Expand Helper---");
		Connection con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		CSSaleReportBean reportsBean = null;
		List<CSSaleReportBean> beanList = new ArrayList<CSSaleReportBean>();
		try {
			String indGameQry = "(select game_id,unitPriceAmt,noOfTkt,mrpAmt,netAmt from (select sale.game_id,unitPriceAmt,(ifnull(noOfTkt,0)-ifnull(noOfTktRet,0)) noOfTkt,(ifnull(mrpAmt,0.0)-ifnull(mrpAmtRet,0.0)) mrpAmt,(ifnull(netAmt,0.0)-ifnull(netAmtRet,0.0)) netAmt from (select game_id,mrp_amt unitPriceAmt,count(mrp_amt) noOfTkt,sum(mrp_amt) mrpAmt,sum(net_amt) netAmt from st_dg_ret_sale_* where transaction_id in(select transaction_id from st_lms_retailer_transaction_master where transaction_type in('DG_SALE','DG_SALE_OFFLINE') and transaction_date>='"
					+ startDate
					+ "' and transaction_date<='"
					+ endDate
					+ "' ) and retailer_org_id in(select organization_id from st_lms_organization_master where parent_id="+agtOrgId+") group by mrp_amt order by mrp_amt) sale left outer join (select game_id,mrp_amt unitPriceAmtRet,count(mrp_amt) noOfTktRet,sum(mrp_amt) mrpAmtRet,sum(net_amt) netAmtRet from st_dg_ret_sale_refund_* where transaction_id in(select transaction_id from st_lms_retailer_transaction_master where transaction_type in('DG_REFUND_CANCEL','DG_REFUND_FAILED') and transaction_date>='"
					+ startDate
					+ "' and transaction_date<='"
					+ endDate
					+ "' ) and retailer_org_id in(select organization_id from st_lms_organization_master where parent_id="+agtOrgId+") group by mrp_amt order by mrp_amt) saleRet on sale.game_id=saleRet.game_id and unitPriceAmt=unitPriceAmtRet) sale where noOfTkt!=0) union all ";
			StringBuilder saleQry = new StringBuilder(
					"select game_nbr gameNo,game_name gameName,unitPriceAmt,noOfTkt,mrpAmt,netAmt from st_dg_game_master gm,(");
			String gameQry = "select game_nbr from st_dg_game_master";
			PreparedStatement pstmtGame = con.prepareStatement(gameQry);
			ResultSet rsGame = pstmtGame.executeQuery();

			while (rsGame.next()) {
				saleQry.append(indGameQry.replaceAll("\\*", rsGame
						.getString("game_nbr")));
			}
			saleQry.delete(saleQry.length() - 10, saleQry.length());
			saleQry.append(") saleTlb where gm.game_nbr=saleTlb.game_id");

			pstmt = con.prepareStatement(saleQry.toString());
			logger.info("----Game Expand Qry---" + pstmt);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				reportsBean = new CSSaleReportBean();
				reportsBean.setGameName(rs.getString("gameName"));
				reportsBean.setGameNo(rs.getInt("gameNo"));
				reportsBean.setSaleMrpAmt(rs.getDouble("mrpAmt"));
				reportsBean.setSaleNetAmt(rs.getDouble("netAmt"));
				reportsBean.setUnitPriceAmt(rs.getDouble("unitPriceAmt"));
				reportsBean.setNoOfTkt(rs.getInt("noOfTkt"));
				beanList.add(reportsBean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.close();
		}
		return beanList;
	}*/
	
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
	
	public Map<Integer, List<String>> fetchOrgAddMap(String orgType, Integer agtOrgId)throws LMSException{
		Map<Integer, List<String>> map = new TreeMap<Integer, List<String>>();
		Connection con = null;
		String orgAdd = "";
		con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<String> tempList = null;
		try {
			if(orgType.equalsIgnoreCase("AGENT")){
				pstmt = con.prepareStatement("select organization_id, name,addr_line1, addr_line2, city from st_lms_organization_master where organization_type = '"+ orgType +"'");
			}else{
				pstmt = con.prepareStatement("select organization_id, name,addr_line1, addr_line2, city from st_lms_organization_master where organization_type = '"+ orgType +"' and parent_id = "+ agtOrgId);
			}
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

	public Map<Integer, String> fetchActiveCategoryMap() throws LMSException {
		Map<Integer, String> map = new TreeMap<Integer, String>();
		Connection con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = con.prepareStatement("select category_id, category_code from st_cs_product_category_master where status = 'ACTIVE'");
			rs = pstmt.executeQuery();
			logger.debug(pstmt);
			while (rs.next()) {
				map.put(rs.getInt("category_id"), rs.getString("category_code"));
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
