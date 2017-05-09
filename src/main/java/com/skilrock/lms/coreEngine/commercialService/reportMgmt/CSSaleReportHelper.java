package com.skilrock.lms.coreEngine.commercialService.reportMgmt;

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

import com.mysql.jdbc.CallableStatement;
import com.skilrock.lms.beans.CSSaleReportBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;

public class CSSaleReportHelper implements CSSaleReportIF{
	Log logger = LogFactory.getLog(CSSaleReportHelper.class);

	public List<CSSaleReportBean> CSSaleCategoryWise(Timestamp startDate,
			Timestamp endDate) throws SQLException{
		Connection con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		CSSaleReportBean reportsBean = null;
		List<CSSaleReportBean> beanList = new ArrayList<CSSaleReportBean>();
		try {
			String indGameQry = "select sale.product_id,(ifnull(mrpAmt,0.0)-ifnull(mrpAmtRet,0.0))mrpAmt,(ifnull(buyCost,0.0)-ifnull(buyCostRet,0.0))buyCostAmt from (select product_id,sum(mrp_amt) mrpAmt,sum(mrp_amt - (mrp_amt*jv_comm/100)) buyCost from st_cs_sale_# where transaction_id in(select transaction_id from st_lms_retailer_transaction_master where transaction_type ='CS_SALE' and transaction_date>='"
				+ startDate
				+ "' and transaction_date<='"
				+ endDate
				+ "' ) group by product_id) sale left outer join (select product_id,sum(mrp_amt) mrpAmtRet,sum(mrp_amt - (mrp_amt*jv_comm/100)) buyCostRet from st_cs_refund_# where transaction_id in(select transaction_id from st_lms_retailer_transaction_master where transaction_type in('CS_CANCEL_SERVER','CS_CANCEL_RET') and transaction_date>='"
				+ startDate
				+ "' and transaction_date<='"
				+ endDate
				+ "' ) group by product_id)saleRet on sale.product_id=saleRet.product_id union all ";
		StringBuilder saleQry = new StringBuilder(
				"select pcm.category_id,pcm.category_code,sum(mrpAmt)mrp,sum(buyCostAmt)buyCost from st_cs_product_category_master pcm ,st_cs_product_master pm,(");
		String gameQry = "select category_id from st_cs_product_category_master group by category_id";
		PreparedStatement pstmtGame = con.prepareStatement(gameQry);
		ResultSet rsGame = pstmtGame.executeQuery();

		while (rsGame.next()) {
			saleQry.append(indGameQry.replaceAll("\\#", rsGame
					.getString("category_id")));
		}
		saleQry.delete(saleQry.length() - 10, saleQry.length());
		saleQry.append(") saleTlb where pm.product_id=saleTlb.product_id and pcm.category_id = pm.category_id group by category_id");

		pstmt = con.prepareStatement(saleQry.toString());
		logger.debug("----CS Report Category Wise Qry---" + pstmt);
		rs = pstmt.executeQuery();
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
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		CSSaleReportBean reportsBean = null;
		List<CSSaleReportBean> beanList = new ArrayList<CSSaleReportBean>();
		try {
			 String orgCodeQry = " agm.name orgCode ";
			if ((LMSFilterDispatcher.orgFieldType).equalsIgnoreCase("CODE")) {
				orgCodeQry = " agm.org_code orgCode ";
			

			} else if ((LMSFilterDispatcher.orgFieldType)
					.equalsIgnoreCase("CODE_NAME")) {
				orgCodeQry = " concat(agm.org_code,'_',agm.name)  orgCode  ";
			
				
				
			} else if ((LMSFilterDispatcher.orgFieldType)
					.equalsIgnoreCase("NAME_CODE")) {
				orgCodeQry = " concat(agm.name,'_',agm.org_code)  orgCode  ";
			
				
			}
			String appendOrder ="orgCode ASC ";

			if( (LMSFilterDispatcher.orgOrderBy).equalsIgnoreCase("ORG_ID")){
				appendOrder="agm.organization_id";
				
			}else if( (LMSFilterDispatcher.orgOrderBy).equalsIgnoreCase("DESC")){
				
				appendOrder="orgCode DESC ";
			}
			String indGameQry = "select sale.retailer_org_id,(ifnull(mrpAmt,0.0)-ifnull(mrpAmtRet,0.0))mrpAmt,(ifnull(buyCost,0.0)-ifnull(buyCostRet,0.0))buyCostAmt,(ifnull(netAmt,0.0)-ifnull(netAmtRet,0.0))netAmt from (select retailer_org_id,sum(mrp_amt) mrpAmt,sum(mrp_amt - (mrp_amt*jv_comm/100)) buyCost, sum(agent_net_amt) netAmt from st_cs_sale_# where transaction_id in(select transaction_id from st_lms_retailer_transaction_master where transaction_type ='CS_SALE' and transaction_date>='"
				+ startDate
				+ "' and transaction_date<='"
				+ endDate
				+ "' ) group by retailer_org_id) sale left outer join (select retailer_org_id,sum(mrp_amt) mrpAmtRet,sum(mrp_amt - (mrp_amt*jv_comm/100)) buyCostRet, sum(agent_net_amt)netAmtRet from st_cs_refund_# where transaction_id in(select transaction_id from st_lms_retailer_transaction_master where transaction_type in('CS_CANCEL_SERVER','CS_CANCEL_RET') and transaction_date>='"
				+ startDate
				+ "' and transaction_date<='"
				+ endDate
				+ "' ) group by retailer_org_id)saleRet on sale.retailer_org_id=saleRet.retailer_org_id union all ";
		StringBuilder saleQry = new StringBuilder(
				"select agm.organization_id,"+orgCodeQry+",sum(mrpAmt)mrp,sum(buyCostAmt)buyCost,sum(netAmt)netAmt from st_lms_organization_master agm , st_lms_organization_master rtm,(");
		String gameQry = "select category_id from st_cs_product_category_master group by category_id  ";
		PreparedStatement pstmtGame = con.prepareStatement(gameQry);
		ResultSet rsGame = pstmtGame.executeQuery();

		while (rsGame.next()) {
			saleQry.append(indGameQry.replaceAll("\\#", rsGame
					.getString("category_id")));
		}
		saleQry.delete(saleQry.length() - 10, saleQry.length());
		saleQry.append(") saleTlb where rtm.organization_type = 'RETAILER' and rtm.organization_id=saleTlb.retailer_org_id and agm.organization_type='AGENT' and rtm.parent_id = agm.organization_id group by agm.organization_id order by "+appendOrder);

		pstmt = con.prepareStatement(saleQry.toString());
		logger.debug("----CS Report Agent Wise Qry---" + pstmt);
		rs = pstmt.executeQuery();
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
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		CSSaleReportBean reportsBean = null;
		List<CSSaleReportBean> beanList = new ArrayList<CSSaleReportBean>();
		String agtCond = "";
		if(agtOrgId != -2){
			agtCond = " and rtm.parent_id ="+agtOrgId;
		}
		try {
			String indGameQry = "select sale.retailer_org_id,(ifnull(mrpAmt,0.0)-ifnull(mrpAmtRet,0.0))mrpAmt,(ifnull(buyCost,0.0)-ifnull(buyCostRet,0.0))buyCost,(ifnull(netAmt,0.0)-ifnull(netAmtRet,0.0))netAmt  from (select retailer_org_id,sum(mrp_amt) mrpAmt,sum(mrp_amt - (mrp_amt*jv_comm/100)) buyCost, sum(net_Amt) netAmt from st_cs_sale_# where transaction_id in(select transaction_id from st_lms_retailer_transaction_master where transaction_type ='CS_SALE' and transaction_date>='"
				+ startDate
				+ "' and transaction_date<='"
				+ endDate
				+ "' ) group by retailer_org_id) sale left outer join (select retailer_org_id,sum(mrp_amt) mrpAmtRet,sum(mrp_amt - (mrp_amt*jv_comm/100)) buyCostRet, sum(net_amt)netAmtRet from st_cs_refund_# where transaction_id in(select transaction_id from st_lms_retailer_transaction_master where transaction_type in('CS_CANCEL_SERVER','CS_CANCEL_RET') and transaction_date>='"
				+ startDate
				+ "' and transaction_date<='"
				+ endDate
				+ "' ) group by retailer_org_id)saleRet on sale.retailer_org_id=saleRet.retailer_org_id union all ";
		StringBuilder saleQry = new StringBuilder(
				"select rtm.organization_id,rtm.name,ifnull(sum(mrpAmt),0)mrp,ifnull(sum(buyCost),0)buyCost,ifnull(sum(netAmt),0)net from st_lms_organization_master rtm, (");
		String gameQry = "select category_id from st_cs_product_category_master group by category_id";
		PreparedStatement pstmtGame = con.prepareStatement(gameQry);
		ResultSet rsGame = pstmtGame.executeQuery();

		while (rsGame.next()) {
			saleQry.append(indGameQry.replaceAll("\\#", rsGame
					.getString("category_id")));
		}
		saleQry.delete(saleQry.length() - 10, saleQry.length());
		saleQry.append(") saleTlb where saleTlb.retailer_org_id = rtm.organization_id and rtm.organization_type='RETAILER' "+ agtCond +" group by rtm.organization_id order by rtm.name");

		pstmt = con.prepareStatement(saleQry.toString());
		logger.debug("----CS Report Retailer Wise Qry---" + pstmt);
		rs = pstmt.executeQuery();
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
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		CSSaleReportBean reportsBean = null;
		List<CSSaleReportBean> beanList = new ArrayList<CSSaleReportBean>();
		try {
			String indGameQry = "select sale.product_id,(ifnull(mrpAmt,0.0)-ifnull(mrpAmtRet,0.0))mrpAmt,(ifnull(buyCost,0.0)-ifnull(buyCostRet,0.0))buyCostAmt from (select product_id,sum(mrp_amt) mrpAmt,sum(mrp_amt - (mrp_amt*jv_comm/100)) buyCost from st_cs_sale_# where transaction_id in(select transaction_id from st_lms_retailer_transaction_master where transaction_type ='CS_SALE' and transaction_date>='"
				+ startDate
				+ "' and transaction_date<='"
				+ endDate
				+ "' ) group by product_id) sale left outer join (select product_id,sum(mrp_amt) mrpAmtRet,sum(mrp_amt - (mrp_amt*jv_comm)/100) buyCostRet from st_cs_refund_# where transaction_id in(select transaction_id from st_lms_retailer_transaction_master where transaction_type in('CS_CANCEL_SERVER','CS_CANCEL_RET') and transaction_date>='"
				+ startDate
				+ "' and transaction_date<='"
				+ endDate
				+ "' ) group by product_id)saleRet on sale.product_id=saleRet.product_id union all ";
		StringBuilder saleQry = new StringBuilder(
				"select saleTlb.product_id,pcm.category_id,product_code,pm.description,operator_name,denomination,mrpAmt,buyCostAmt from st_cs_product_category_master pcm ,st_cs_product_master pm, st_cs_operator_master com,(");
		String gameQry = "select category_id from st_cs_product_category_master group by category_id";
		PreparedStatement pstmtGame = con.prepareStatement(gameQry);
		ResultSet rsGame = pstmtGame.executeQuery();

		while (rsGame.next()) {
			saleQry.append(indGameQry.replaceAll("\\#", rsGame
					.getString("category_id")));
		}
		saleQry.delete(saleQry.length() - 10, saleQry.length());
		saleQry.append(") saleTlb where pm.product_id=saleTlb.product_id and pcm.category_id = pm.category_id and pm.operator_code = com.operator_code and pcm.category_id ="+catId);

		pstmt = con.prepareStatement(saleQry.toString());
		logger.debug("----CS Report Product Wise Qry---" + pstmt);
		rs = pstmt.executeQuery();
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
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		CSSaleReportBean reportsBean = null;
		List<CSSaleReportBean> beanList = new ArrayList<CSSaleReportBean>();
		try {
			String indGameQry = "select sale.product_id,(ifnull(mrpAmt,0.0)-ifnull(mrpAmtRet,0.0))mrpAmt,(ifnull(buyCost,0.0)-ifnull(buyCostRet,0.0))buyCostAmt,(ifnull(netAmt,0.0)-ifnull(netAmtRet,0.0))netAmt from (select product_id,sum(mrp_amt) mrpAmt,sum(mrp_amt - (mrp_amt*jv_comm/100)) buyCost, sum(agent_net_amt)netAmt from st_cs_sale_# cs, st_lms_organization_master rtm where transaction_id in(select transaction_id from st_lms_retailer_transaction_master where transaction_type ='CS_SALE' and transaction_date>='"
				+ startDate
				+ "' and transaction_date<='"
				+ endDate
				+ "' ) and cs.retailer_org_id = rtm.organization_id and rtm.parent_id = "+ agtOrgId +" and rtm.organization_type = 'RETAILER'  group by product_id) sale left outer join (select product_id,sum(mrp_amt) mrpAmtRet,sum(mrp_amt - (mrp_amt*jv_comm)/100) buyCostRet, sum(agent_net_amt)netAmtRet from st_cs_refund_# csret, st_lms_organization_master rtm where transaction_id in(select transaction_id from st_lms_retailer_transaction_master where transaction_type in('CS_CANCEL_SERVER','CS_CANCEL_RET') and transaction_date>='"
				+ startDate
				+ "' and transaction_date<='"
				+ endDate
				+ "' )and csret.retailer_org_id = rtm.organization_id and rtm.parent_id = "+ agtOrgId +" and rtm.organization_type = 'RETAILER' group by product_id)saleRet on sale.product_id=saleRet.product_id union all ";
		StringBuilder saleQry = new StringBuilder(
				"select saleTlb.product_id,pm.product_id,product_code,pm.description,operator_name,denomination,sum(mrpAmt)mrp,sum(buyCostAmt)buyCost,sum(netAmt)net from st_cs_product_master pm, st_cs_operator_master com,(");
		String gameQry = "select category_id from st_cs_product_category_master group by category_id";
		PreparedStatement pstmtGame = con.prepareStatement(gameQry);
		ResultSet rsGame = pstmtGame.executeQuery();

		while (rsGame.next()) {
			saleQry.append(indGameQry.replaceAll("\\#", rsGame
					.getString("category_id")));
		}
		saleQry.delete(saleQry.length() - 10, saleQry.length());
		saleQry.append(") saleTlb where pm.product_id=saleTlb.product_id and pm.operator_code=com.operator_code group by saleTlb.product_id");

		pstmt = con.prepareStatement(saleQry.toString());
		logger.debug("----CS Report Product Wise Agent Wise Qry---" + pstmt);
		rs = pstmt.executeQuery();
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
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		CSSaleReportBean reportsBean = null;
		List<CSSaleReportBean> beanList = new ArrayList<CSSaleReportBean>();
		try {
			String indGameQry = "select sale.product_id,(ifnull(mrpAmt,0.0)-ifnull(mrpAmtRet,0.0))mrpAmt,(ifnull(buyCost,0.0)-ifnull(buyCostRet,0.0))buyCostAmt,(ifnull(netAmt,0.0)-ifnull(netAmtRet,0.0))netAmt from (select product_id,sum(mrp_amt) mrpAmt,sum(mrp_amt - (mrp_amt*jv_comm/100)) buyCost, sum(agent_net_amt)netAmt from st_cs_sale_# cs where transaction_id in(select transaction_id from st_lms_retailer_transaction_master where transaction_type ='CS_SALE' and transaction_date>='"
				+ startDate
				+ "' and transaction_date<='"
				+ endDate
				+ "' ) and cs.retailer_org_id = "+retOrgId +"  group by product_id) sale left outer join (select product_id,sum(mrp_amt) mrpAmtRet,sum(mrp_amt - (mrp_amt*jv_comm)/100) buyCostRet, sum(agent_net_amt)netAmtRet from st_cs_refund_# csret where transaction_id in(select transaction_id from st_lms_retailer_transaction_master where transaction_type in('CS_CANCEL_SERVER','CS_CANCEL_RET') and transaction_date>='"
				+ startDate
				+ "' and transaction_date<='"
				+ endDate
				+ "' )and csret.retailer_org_id = "+retOrgId +" group by product_id)saleRet on sale.product_id=saleRet.product_id union all ";
		StringBuilder saleQry = new StringBuilder(
				"select saleTlb.product_id,pm.product_id,product_code,operator_name,pm.description,denomination,sum(mrpAmt)mrp,sum(buyCostAmt)buyCost,sum(netAmt)net from st_cs_product_master pm,st_cs_operator_master com,(");
		String gameQry = "select category_id from st_cs_product_category_master group by category_id";
		PreparedStatement pstmtGame = con.prepareStatement(gameQry);
		ResultSet rsGame = pstmtGame.executeQuery();

		while (rsGame.next()) {
			saleQry.append(indGameQry.replaceAll("\\#", rsGame
					.getString("category_id")));
		}
		saleQry.delete(saleQry.length() - 10, saleQry.length());
		saleQry.append(") saleTlb where pm.product_id=saleTlb.product_id and pm.operator_code = com.operator_code group by saleTlb.product_id");

		pstmt = con.prepareStatement(saleQry.toString());
		logger.debug("----CS Report Product Wise Retailer Wise Qry---" + pstmt);
		rs = pstmt.executeQuery();
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
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		CSSaleReportBean reportsBean = null;
		List<CSSaleReportBean> beanList = new ArrayList<CSSaleReportBean>();
		try {
			String indGameQry = "select sale.product_id,(ifnull(mrpAmt,0.0)-ifnull(mrpAmtRet,0.0))mrpAmt,(ifnull(netAmt,0.0)-ifnull(netAmtRet,0.0))netAmt from (select product_id,sum(mrp_amt) mrpAmt, sum(agent_net_amt)netAmt from st_cs_sale_# cs where transaction_id in(select transaction_id from st_lms_retailer_transaction_master where transaction_type ='CS_SALE' and transaction_date>='"
				+ startDate
				+ "' and transaction_date<='"
				+ endDate
				+ "' ) and cs.retailer_org_id = "+retOrgId +"  group by product_id) sale left outer join (select product_id,sum(mrp_amt) mrpAmtRet,sum(agent_net_amt)netAmtRet from st_cs_refund_# csret where transaction_id in(select transaction_id from st_lms_retailer_transaction_master where transaction_type in('CS_CANCEL_SERVER','CS_CANCEL_RET') and transaction_date>='"
				+ startDate
				+ "' and transaction_date<='"
				+ endDate
				+ "' )and csret.retailer_org_id = "+retOrgId +" group by product_id)saleRet on sale.product_id=saleRet.product_id union all ";
		StringBuilder saleQry = new StringBuilder(
				"select saleTlb.product_id,pm.product_id,product_code,operator_name,pm.description,denomination,sum(mrpAmt)mrp,sum(netAmt)net from st_cs_product_master pm,st_cs_operator_master com,(");
		String gameQry = "select category_id from st_cs_product_category_master group by category_id";
		PreparedStatement pstmtGame = con.prepareStatement(gameQry);
		ResultSet rsGame = pstmtGame.executeQuery();
		while (rsGame.next()) {
			saleQry.append(indGameQry.replaceAll("\\#", rsGame
					.getString("category_id")));
		}
		saleQry.delete(saleQry.length() - 10, saleQry.length());
		saleQry.append(") saleTlb where pm.product_id=saleTlb.product_id and pm.operator_code = com.operator_code group by saleTlb.product_id");

		pstmt = con.prepareStatement(saleQry.toString());
		logger.debug("----CS Report Product Wise Retailer Wise Qry---" + pstmt);
		rs = pstmt.executeQuery();
		while (rs.next()) {
			reportsBean = new CSSaleReportBean();
			reportsBean.setProductCode(rs.getString("description"));
			reportsBean.setProvider(rs.getString("operator_name"));
			reportsBean.setDenomination(rs.getString("denomination"));
			reportsBean.setProductId(rs.getInt("product_id"));
			reportsBean.setMrpAmt(rs.getDouble("mrp"));
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
	
	public Map<Integer, String> fetchActiveCategoryMap()throws LMSException{
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
