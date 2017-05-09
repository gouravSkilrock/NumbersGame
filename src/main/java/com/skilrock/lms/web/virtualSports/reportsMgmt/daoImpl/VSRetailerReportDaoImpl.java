package com.skilrock.lms.web.virtualSports.reportsMgmt.daoImpl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.GenericException;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.web.virtualSports.reportsMgmt.beans.VSOrgReportRequestBean;
import com.skilrock.lms.web.virtualSports.reportsMgmt.beans.VSOrgReportResponseBean;

public class VSRetailerReportDaoImpl {

	public static Map<Integer, VSOrgReportResponseBean> fetchSaleDataSingleRetailer(
			VSOrgReportRequestBean requestBean, Connection con)
			throws GenericException {
		Map<Integer, VSOrgReportResponseBean> responseBeanMap = null;
		VSOrgReportResponseBean responseBean = null;
		Statement stmt = null;
		ResultSet rs = null;
		StringBuilder saleQueryBuilder = null;
		try {
			responseBeanMap = new HashMap<Integer, VSOrgReportResponseBean>();

			String parentIdAppenderArch = requestBean.getOrgId() == 0 ? "" : "sgr.organization_id="+ requestBean.getOrgId()+ " AND";
			String parentIdAppender = requestBean.getOrgId() == 0 ? "" : " AND om.organization_id="+ requestBean.getOrgId();
			String archiveAppender = " UNION ALL select sgr.organization_id as organization_id,sum(sgr.sale_net) as sale from st_rep_vs_retailer as sgr where "+parentIdAppenderArch+" sgr.finaldate>=date('"
					+ requestBean.getFromDate()
					+ "')"
					+ " AND sgr.finaldate<=date('"
					+ requestBean.getToDate()
					+ "')" + " GROUP BY sgr.organization_id";

			saleQueryBuilder = new StringBuilder();
			saleQueryBuilder
					.append(
							"select main.organization_id,sum(main.sale)as sale from (select organization_id,ifnull(sale,0.0) sale from st_lms_organization_master om left outer join (select srs.retailer_org_id,sum(retailer_net_amt) as sale from st_vs_ret_sale")
					// .append(requestBean.getGameId())
					.append(
							" srs inner join st_lms_retailer_transaction_master rtm on srs.transaction_id=rtm.transaction_id where rtm.transaction_type in('VS_SALE') and rtm.transaction_date>='")
					.append(requestBean.getFromDate())
					.append("'")
					.append(" and rtm.transaction_date<='")
					.append(requestBean.getToDate())
					.append("'")
					.append(
							" group by srs.retailer_org_id) sale on om.organization_id=retailer_org_id where om.organization_type='RETAILER'")
					.append(parentIdAppender).append(archiveAppender)
					.append(") as main group by main.organization_id");

			// logger.debug("fetchDepositDataMultipleAgent - "
			// + saleQueryBuilder.toString());

			stmt = con.createStatement();
			rs = stmt.executeQuery(saleQueryBuilder.toString());
			while (rs.next()) {
				responseBean = new VSOrgReportResponseBean();
				responseBean.setSaleAmt(rs.getDouble("sale"));
				responseBeanMap.put(rs.getInt("organization_id"), responseBean);
			}
		} catch (SQLException se) {
			se.printStackTrace();
			throw new GenericException(LMSErrors.SQL_EXCEPTION_CODE, se);
		} catch (Exception e) {
			e.printStackTrace();
			throw new GenericException(LMSErrors.GENERAL_EXCEPTION_CODE, e);
		} finally {
			DBConnect.closeConnection(stmt, rs);
		}

		return responseBeanMap;
	}

	/**
	 * Fetch Sale Data for all game(For all game ID)
	 * 
	 * @param1-Request bean containing from date, to date, and organization ID
	 * @param2-Connection
	 * @return-data map
	 * @throws-GenericException Exception
	 */
	public static Map<Integer, VSOrgReportResponseBean> fetchSaleDataMultipleRetailer(
			VSOrgReportRequestBean requestBean, Connection con)
			throws GenericException {
		Map<Integer, VSOrgReportResponseBean> responseBeanMap = null;
		VSOrgReportResponseBean responseBean = null;
		Statement stmt = null;
		ResultSet rs = null;
		StringBuilder saleQueryBuilder = null;
		try {
			responseBeanMap = new HashMap<Integer, VSOrgReportResponseBean>();

			// String archiveAppender =
			// " UNION ALL select sgr.organization_id as organization_id,sum(sgr.sale_net) as sale from st_rep_vs_retailer as sgr where sgr.game_id="
			// + requestBean.getGameId()
			String parentIdAppenderArch = requestBean.getOrgId() == 0 ? "" : "sgr.parent_id="+ requestBean.getOrgId()+ " AND";
			String parentIdAppender = requestBean.getOrgId() == 0 ? "" : " AND om.parent_id="+ requestBean.getOrgId();
			String archiveAppender = " UNION ALL select sgr.organization_id as organization_id,sum(sgr.sale_net) as sale from st_rep_vs_retailer as sgr where "+parentIdAppenderArch+" sgr.finaldate>=date('"
					+ requestBean.getFromDate()
					+ "')"
					+ " AND sgr.finaldate<=date('"
					+ requestBean.getToDate()
					+ "')" + " GROUP BY sgr.organization_id";

			saleQueryBuilder = new StringBuilder();
			saleQueryBuilder
					.append(
							"select main.organization_id,sum(main.sale)as sale from (select organization_id,ifnull(sale,0.0) sale from st_lms_organization_master om left outer join (select srs.retailer_org_id,sum(retailer_net_amt) as sale from st_vs_ret_sale")
					// .append(requestBean.getGameId())
					.append(
							" srs inner join st_lms_retailer_transaction_master rtm on srs.transaction_id=rtm.transaction_id where rtm.transaction_type in('VS_SALE') and rtm.transaction_date>='")
					.append(requestBean.getFromDate())
					.append("'")
					.append(" and rtm.transaction_date<='")
					.append(requestBean.getToDate())
					.append("'")
					.append(
							" group by srs.retailer_org_id) sale on om.organization_id=retailer_org_id where om.organization_type='RETAILER'")
					.append(parentIdAppender).append(archiveAppender)
					.append(") as main group by main.organization_id");

			// logger.debug("fetchDepositDataMultipleAgent - "
			// + saleQueryBuilder.toString());

			stmt = con.createStatement();
			rs = stmt.executeQuery(saleQueryBuilder.toString());
			while (rs.next()) {
				responseBean = new VSOrgReportResponseBean();
				responseBean.setSaleAmt(rs.getDouble("sale"));
				responseBeanMap.put(rs.getInt("organization_id"), responseBean);
			}
		} catch (SQLException se) {
			se.printStackTrace();
			throw new GenericException(LMSErrors.SQL_EXCEPTION_CODE, se);
		} catch (Exception e) {
			e.printStackTrace();
			throw new GenericException(LMSErrors.GENERAL_EXCEPTION_CODE, e);
		} finally {
			DBConnect.closeConnection(stmt, rs);
		}

		return responseBeanMap;
	}

	/**
	 * Fetch Sale Data for a given game(For given game ID)
	 * 
	 * @param1-Request bean containing from date, to date, organization ID and
	 *                 game id
	 * @param2-Connection
	 * @return-data map
	 * @throws-GenericException Exception
	 */
	public static Map<Integer, VSOrgReportResponseBean> fetchSaleDataMultipleRetailerGameWise(
			VSOrgReportRequestBean requestBean, Connection con)
			throws GenericException {
		Map<Integer, VSOrgReportResponseBean> responseBeanMap = null;
		VSOrgReportResponseBean responseBean = null;
		Statement stmt = null;
		ResultSet rs = null;
		StringBuilder saleQueryBuilder = null;
		try {
			responseBeanMap = new HashMap<Integer, VSOrgReportResponseBean>();

			String archiveAppender = " UNION ALL select sgr.organization_id as organization_id,sum(sgr.sale_net) as sale from st_rep_vs_retailer as sgr where sgr.game_id="
					+ requestBean.getGameId()
					+ " AND sgr.parent_id="
					+ requestBean.getOrgId()
					+ " AND sgr.finaldate>=date('"
					+ requestBean.getFromDate()
					+ "')"
					+ " AND sgr.finaldate<=date('"
					+ requestBean.getToDate()
					+ "')" + " GROUP BY sgr.organization_id";

			saleQueryBuilder = new StringBuilder();
			saleQueryBuilder
					.append(
							"select main.organization_id,sum(main.sale)as sale from (select organization_id,ifnull(sale,0.0) sale from st_lms_organization_master om left outer join (select srs.retailer_org_id,sum(retailer_net_amt) as sale from st_vs_ret_sale")
					// .append(requestBean.getGameId())
					.append(
							" srs inner join st_lms_retailer_transaction_master rtm on srs.transaction_id=rtm.transaction_id where rtm.transaction_type in('VS_SALE') and rtm.transaction_date>='")
					.append(requestBean.getFromDate())
					.append("'")
					.append(" and rtm.transaction_date<='")
					.append(requestBean.getToDate())
					.append("'")
					.append(
							" group by srs.retailer_org_id) sale on om.organization_id=retailer_org_id where om.organization_type='RETAILER' and om.parent_id=")
					.append(requestBean.getOrgId()).append(archiveAppender)
					.append(") as main group by main.organization_id");

			// logger.debug("fetchDepositDataMultipleAgent - "
			// + saleQueryBuilder.toString());

			stmt = con.createStatement();
			rs = stmt.executeQuery(saleQueryBuilder.toString());
			while (rs.next()) {
				responseBean = new VSOrgReportResponseBean();
				responseBean.setSaleAmt(rs.getDouble("sale"));
				responseBeanMap.put(rs.getInt("organization_id"), responseBean);
			}
		} catch (SQLException se) {
			se.printStackTrace();
			throw new GenericException(LMSErrors.SQL_EXCEPTION_CODE, se);
		} catch (Exception e) {
			e.printStackTrace();
			throw new GenericException(LMSErrors.GENERAL_EXCEPTION_CODE, e);
		} finally {
			DBConnect.closeConnection(stmt, rs);
		}

		return responseBeanMap;
	}
	
	/**
	 * Fetch Sale Data for all game Single Retailer Game Wise(For all game ID)
	 * 
	 * @param1-Request bean containing from date, to date, and organization ID
	 * @param2-Connection
	 * @return-data map
	 * @throws-GenericException Exception
	 */
	public static Map<String, VSOrgReportResponseBean> fetchSaleDataDateWiseSingleRetailerAllGame(
			VSOrgReportRequestBean requestBean, Connection con)
			throws GenericException {
		Map<String, VSOrgReportResponseBean> responseBeanMap = null;
		VSOrgReportResponseBean responseBean = null;
		Statement stmt = null;
		ResultSet rs = null;
		StringBuilder saleQueryBuilder = null;
		try {
			responseBeanMap = new HashMap<String, VSOrgReportResponseBean>();

			String archiveAppender = " UNION ALL select finaldate,organization_id,sum(sale_net) as sale from st_rep_vs_retailer where finaldate>='"
					+ requestBean.getFromDate()
					+ "' and finaldate<='"
					+ requestBean.getToDate()
					+ "' and organization_id=" 
					+ requestBean.getOrgId()
					+ " GROUP BY finaldate";

			saleQueryBuilder = new StringBuilder();
			saleQueryBuilder
					.append("select date(transaction_date) date,retailer_org_id,sum(sale) as sale from ((select retailer_org_id,sum(retailer_net_amt) as sale,transaction_date from st_vs_ret_sale where transaction_date>='")
					.append(requestBean.getFromDate())
					.append("'and transaction_date<='")
					.append(requestBean.getToDate())
					.append("' and retailer_org_id=")
					.append(requestBean.getOrgId())
					.append("  group by date(transaction_date))) saletable group by date(transaction_date)")
					.append(archiveAppender);

			// logger.debug("fetchDepositDataMultipleAgent - "
			// + saleQueryBuilder.toString());

			stmt = con.createStatement();
			rs = stmt.executeQuery(saleQueryBuilder.toString());
			while (rs.next()) {
				responseBean = new VSOrgReportResponseBean();
				responseBean.setSaleAmt(rs.getDouble("sale"));
				responseBeanMap.put(rs.getString("date"), responseBean);
			}
		} catch (SQLException se) {
			se.printStackTrace();
			throw new GenericException(LMSErrors.SQL_EXCEPTION_CODE, se);
		} catch (Exception e) {
			e.printStackTrace();
			throw new GenericException(LMSErrors.GENERAL_EXCEPTION_CODE, e);
		}  finally {
		    DBConnect.closeConnection(stmt, rs);
		}

		return responseBeanMap;
	}

	public static Map<Integer, VSOrgReportResponseBean> fetchCancelDataSingleRetailer(
			VSOrgReportRequestBean requestBean, Connection con)
			throws GenericException {
		Map<Integer, VSOrgReportResponseBean> responseBeanMap = null;
		VSOrgReportResponseBean responseBean = null;
		Statement stmt = null;
		ResultSet rs = null;
		StringBuilder cancelQueryBuilder = null;
		try {
			responseBeanMap = new HashMap<Integer, VSOrgReportResponseBean>();
			
			String parentIdAppenderArch = requestBean.getOrgId() == 0 ? "" : "sgr.organization_id="+ requestBean.getOrgId()+ " AND";
			String parentIdAppender = requestBean.getOrgId() == 0 ? "" : " AND organization_id="+ requestBean.getOrgId();
			
			String archiveAppender = " UNION ALL select sgr.organization_id as organization_id,sum(sgr.ref_net_amt) as cancel from st_rep_vs_retailer as sgr where "+parentIdAppenderArch+" sgr.finaldate>=date('"
					+ requestBean.getFromDate()
					+ "')"
					+ " AND sgr.finaldate<=date('"
					+ requestBean.getToDate()
					+ "')" + " GROUP BY sgr.organization_id";

			cancelQueryBuilder = new StringBuilder();
			cancelQueryBuilder
					.append(
							"select main.organization_id,sum(main.cancel)as cancel from (select organization_id,ifnull(cancel,0.0) cancel from st_lms_organization_master om left outer join (select srr.retailer_org_id,sum(retailer_net_amt) as cancel from st_vs_ret_sale_refund")
					.append(
							" srr inner join st_lms_retailer_transaction_master rtm on srr.transaction_id=rtm.transaction_id where transaction_type in('VS_SALE_REFUND') and rtm.transaction_date>='")
					.append(requestBean.getFromDate())
					.append("'")
					.append(" and rtm.transaction_date<='")
					.append(requestBean.getToDate())
					.append("'")
					.append(
							" GROUP BY srr.retailer_org_id) cancel on om.organization_id=retailer_org_id where om.organization_type='RETAILER'")
					.append(parentIdAppender).append(archiveAppender)
					.append(") as main group by main.organization_id");

			// logger.debug("fetchDepositDataMultipleAgent - "
			// + saleQueryBuilder.toString());

			stmt = con.createStatement();
			rs = stmt.executeQuery(cancelQueryBuilder.toString());
			while (rs.next()) {
				responseBean = new VSOrgReportResponseBean();
				responseBean.setCancelAmt(rs.getDouble("cancel"));
				responseBeanMap.put(rs.getInt("organization_id"), responseBean);
			}
		} catch (SQLException se) {
			se.printStackTrace();
			throw new GenericException(LMSErrors.SQL_EXCEPTION_CODE, se);
		} catch (Exception e) {
			e.printStackTrace();
			throw new GenericException(LMSErrors.GENERAL_EXCEPTION_CODE, e);
		} finally {
			DBConnect.closeConnection(stmt, rs);
		}

		return responseBeanMap;
	}

	public static Map<Integer, VSOrgReportResponseBean> fetchCancelDataMultipleRetailer(
			VSOrgReportRequestBean requestBean, Connection con)
			throws GenericException {
		Map<Integer, VSOrgReportResponseBean> responseBeanMap = null;
		VSOrgReportResponseBean responseBean = null;
		Statement stmt = null;
		ResultSet rs = null;
		StringBuilder cancelQueryBuilder = null;
		try {
			responseBeanMap = new HashMap<Integer, VSOrgReportResponseBean>();
			
			String parentIdAppenderArch = requestBean.getOrgId() == 0 ? "" : "sgr.parent_id="+ requestBean.getOrgId()+ " AND";
			String parentIdAppender = requestBean.getOrgId() == 0 ? "" : " AND om.parent_id="+ requestBean.getOrgId();
			
			String archiveAppender = " UNION ALL select sgr.organization_id as organization_id,sum(sgr.ref_net_amt) as cancel from st_rep_vs_retailer as sgr where "+parentIdAppenderArch+" sgr.finaldate>=date('"
					+ requestBean.getFromDate()
					+ "')"
					+ " AND sgr.finaldate<=date('"
					+ requestBean.getToDate()
					+ "')" + " GROUP BY sgr.organization_id";

			cancelQueryBuilder = new StringBuilder();
			cancelQueryBuilder
					.append(
							"select main.organization_id,sum(main.cancel)as cancel from (select organization_id,ifnull(cancel,0.0) cancel from st_lms_organization_master om left outer join (select srr.retailer_org_id,sum(retailer_net_amt) as cancel from st_vs_ret_sale_refund")
					.append(
							" srr inner join st_lms_retailer_transaction_master rtm on srr.transaction_id=rtm.transaction_id where transaction_type in('VS_SALE_REFUND') and rtm.transaction_date>='")
					.append(requestBean.getFromDate())
					.append("'")
					.append(" and rtm.transaction_date<='")
					.append(requestBean.getToDate())
					.append("'")
					.append(
							" GROUP BY srr.retailer_org_id) cancel on om.organization_id=retailer_org_id where om.organization_type='RETAILER'")
					.append(parentIdAppender).append(archiveAppender)
					.append(") as main group by main.organization_id");

			// logger.debug("fetchDepositDataMultipleAgent - "
			// + saleQueryBuilder.toString());

			stmt = con.createStatement();
			rs = stmt.executeQuery(cancelQueryBuilder.toString());
			while (rs.next()) {
				responseBean = new VSOrgReportResponseBean();
				responseBean.setCancelAmt(rs.getDouble("cancel"));
				responseBeanMap.put(rs.getInt("organization_id"), responseBean);
			}
		} catch (SQLException se) {
			se.printStackTrace();
			throw new GenericException(LMSErrors.SQL_EXCEPTION_CODE, se);
		} catch (Exception e) {
			e.printStackTrace();
			throw new GenericException(LMSErrors.GENERAL_EXCEPTION_CODE, e);
		} finally {
			DBConnect.closeConnection(stmt, rs);
		}

		return responseBeanMap;
	}

	public static Map<Integer, VSOrgReportResponseBean> fetchCancelDataMultipleRetailerGameWise(
			VSOrgReportRequestBean requestBean, Connection con)
			throws GenericException {
		Map<Integer, VSOrgReportResponseBean> responseBeanMap = null;
		VSOrgReportResponseBean responseBean = null;
		Statement stmt = null;
		ResultSet rs = null;
		StringBuilder cancelQueryBuilder = null;
		try {
			responseBeanMap = new HashMap<Integer, VSOrgReportResponseBean>();

			String archiveAppender = " UNION ALL select sgr.organization_id as organization_id,sum(sgr.ref_net_amt) as cancel from st_rep_vs_retailer as sgr where sgr.game_id="
					+ requestBean.getGameId()
					+ " AND sgr.parent_id="
					+ requestBean.getOrgId()
					+ " AND sgr.finaldate>=date('"
					+ requestBean.getFromDate()
					+ "')"
					+ " AND sgr.finaldate<=date('"
					+ requestBean.getToDate()
					+ "')" + " GROUP BY sgr.organization_id";

			cancelQueryBuilder = new StringBuilder();
			cancelQueryBuilder
					.append(
							"select main.organization_id,sum(main.cancel)as cancel from (select organization_id,ifnull(cancel,0.0) cancel from st_lms_organization_master om left outer join (select srr.retailer_org_id,sum(retailer_net_amt) as cancel from st_vs_ret_sale_refund")
					// .append(requestBean.getGameId())
					.append(
							" srr inner join st_lms_retailer_transaction_master rtm on srr.transaction_id=rtm.transaction_id where srr.game_id=")
					.append(requestBean.getGameId())
					.append(
							" AND transaction_type in('VS_SALE_REFUND') and rtm.transaction_date>='")
					.append(requestBean.getFromDate())
					.append("'")
					.append(" and rtm.transaction_date<='")
					.append(requestBean.getToDate())
					.append("'")
					.append(
							" GROUP BY srr.retailer_org_id) cancel on om.organization_id=retailer_org_id where om.organization_type='RETAILER' and om.parent_id=")
					.append(requestBean.getOrgId()).append(archiveAppender)
					.append(") as main group by main.organization_id");

			// logger.debug("fetchDepositDataMultipleAgent - "
			// + saleQueryBuilder.toString());

			stmt = con.createStatement();
			rs = stmt.executeQuery(cancelQueryBuilder.toString());
			while (rs.next()) {
				responseBean = new VSOrgReportResponseBean();
				responseBean.setCancelAmt(rs.getDouble("cancel"));
				responseBeanMap.put(rs.getInt("organization_id"), responseBean);
			}
		} catch (SQLException se) {
			se.printStackTrace();
			throw new GenericException(LMSErrors.SQL_EXCEPTION_CODE, se);
		} catch (Exception e) {
			e.printStackTrace();
			throw new GenericException(LMSErrors.GENERAL_EXCEPTION_CODE, e);
		} finally {
			DBConnect.closeConnection(stmt, rs);
		}

		return responseBeanMap;
	}
	
	/**
	 * Fetch Cancel Data for all game Single Retailer Game Wise(For all game ID)
	 * 
	 * @param1-Request bean containing from date, to date, and organization ID
	 * @param2-Connection
	 * @return-data map
	 * @throws-GenericException Exception
	 */
	public static Map<String, VSOrgReportResponseBean> fetchCancelDataDateWiseSingleRetailerAllGame(
			VSOrgReportRequestBean requestBean, Connection con)
			throws GenericException {
		Map<String, VSOrgReportResponseBean> responseBeanMap = null;
		VSOrgReportResponseBean responseBean = null;
		Statement stmt = null;
		ResultSet rs = null;
		StringBuilder cancelQueryBuilder = null;
		try {
			responseBeanMap = new HashMap<String, VSOrgReportResponseBean>();

			String archiveAppender = " UNION ALL select finaldate,organization_id,sum(ref_net_amt) as cancel from st_rep_vs_retailer where finaldate>='"
					+ requestBean.getFromDate()
					+ "' and finaldate<='"
					+ requestBean.getToDate()
					+ "' and organization_id=" 
					+ requestBean.getOrgId()
					+ " GROUP BY finaldate";

			cancelQueryBuilder = new StringBuilder();
			cancelQueryBuilder
					.append("select date(transaction_date) date,retailer_org_id,sum(cancel) as cancel from ((select retailer_org_id,sum(retailer_net_amt) as cancel,transaction_date from st_vs_ret_sale_refund where transaction_date>='")
					.append(requestBean.getFromDate())
					.append("'and transaction_date<='")
					.append(requestBean.getToDate())
					.append("' and retailer_org_id=")
					.append(requestBean.getOrgId())
					.append("  group by date(transaction_date))) canceltable group by date(transaction_date)")
					.append(archiveAppender);

			// logger.debug("fetchDepositDataMultipleAgent - "
			// + saleQueryBuilder.toString());

			stmt = con.createStatement();
			rs = stmt.executeQuery(cancelQueryBuilder.toString());
			while (rs.next()) {
				responseBean = new VSOrgReportResponseBean();
				responseBean.setCancelAmt(rs.getDouble("cancel"));
				responseBeanMap.put(rs.getString("date"), responseBean);
			}
		} catch (SQLException se) {
			se.printStackTrace();
			throw new GenericException(LMSErrors.SQL_EXCEPTION_CODE, se);
		} catch (Exception e) {
			e.printStackTrace();
			throw new GenericException(LMSErrors.GENERAL_EXCEPTION_CODE, e);
		}  finally {
		    DBConnect.closeConnection(stmt, rs);
		}

		return responseBeanMap;
	}

	public static Map<Integer, VSOrgReportResponseBean> fetchPWTDataSingleRetailer(
			VSOrgReportRequestBean requestBean, Connection con)
			throws GenericException {
		Map<Integer, VSOrgReportResponseBean> responseBeanMap = null;
		VSOrgReportResponseBean responseBean = null;
		Statement stmt = null;
		ResultSet rs = null;
		StringBuilder pwtQueryBuilder = null;
		try {
			responseBeanMap = new HashMap<Integer, VSOrgReportResponseBean>();

			String parentIdAppenderArch = requestBean.getOrgId() == 0 ? "" : "sgr.organization_id="+ requestBean.getOrgId()+ " AND";
			String parentIdAppender = requestBean.getOrgId() == 0 ? "" : " AND om.organization_id="+ requestBean.getOrgId();
			
			// String archiveAppender =
			// " UNION ALL select sgr.organization_id as organization_id,sum(sgr.ref_net_amt) as cancel from st_rep_vs_retailer as sgr where sgr.game_id="
			// + requestBean.getGameId()
			// + " AND sgr.parent_id="
			String archiveAppender = " UNION ALL select sgr.organization_id as organization_id,sum(sgr.pwt_net_amt) as pwt from st_rep_vs_retailer as sgr where "+parentIdAppenderArch+" sgr.finaldate>=date('"
					+ requestBean.getFromDate()
					+ "')"
					+ " AND sgr.finaldate<=date('"
					+ requestBean.getToDate()
					+ "')" + " GROUP BY sgr.organization_id";

			pwtQueryBuilder = new StringBuilder();
			pwtQueryBuilder
					.append(
							"select main.organization_id,sum(main.pwt)as pwt from (select organization_id,ifnull(pwt,0.0) pwt from st_lms_organization_master om left outer join (select srp.retailer_org_id,sum(pwt_amt+retailer_claim_comm) as pwt from st_vs_ret_pwt")
					// .append(requestBean.getGameId())
					.append(
							" srp inner join st_lms_retailer_transaction_master rtm on srp.transaction_id=rtm.transaction_id where transaction_type in('VS_PWT_AUTO','VS_PWT_PLR','VS_PWT') and rtm.transaction_date>='")
					.append(requestBean.getFromDate())
					.append("'")
					.append(" and rtm.transaction_date<='")
					.append(requestBean.getToDate())
					.append("'")
					.append(
							" GROUP BY srp.retailer_org_id) pwt on om.organization_id=retailer_org_id where om.organization_type='RETAILER'")
					.append(
							parentIdAppender).append(archiveAppender)
					.append(") as main group by main.organization_id");

			// logger.debug("fetchDepositDataMultipleAgent - "
			// + saleQueryBuilder.toString());

			stmt = con.createStatement();
			rs = stmt.executeQuery(pwtQueryBuilder.toString());
			while (rs.next()) {
				responseBean = new VSOrgReportResponseBean();
				responseBean.setPwtAmt(rs.getDouble("pwt"));
				responseBeanMap.put(rs.getInt("organization_id"), responseBean);
			}
		} catch (SQLException se) {
			se.printStackTrace();
			throw new GenericException(LMSErrors.SQL_EXCEPTION_CODE, se);
		} catch (Exception e) {
			e.printStackTrace();
			throw new GenericException(LMSErrors.GENERAL_EXCEPTION_CODE, e);
		} finally {
			DBConnect.closeConnection(stmt, rs);
		}

		return responseBeanMap;
	}

	public static Map<Integer, VSOrgReportResponseBean> fetchPWTDataMultipleRetailer(
			VSOrgReportRequestBean requestBean, Connection con)
			throws GenericException {
		Map<Integer, VSOrgReportResponseBean> responseBeanMap = null;
		VSOrgReportResponseBean responseBean = null;
		Statement stmt = null;
		ResultSet rs = null;
		StringBuilder pwtQueryBuilder = null;
		try {
			responseBeanMap = new HashMap<Integer, VSOrgReportResponseBean>();

			String parentIdAppenderArch = requestBean.getOrgId() == 0 ? "" : "sgr.parent_id="+ requestBean.getOrgId()+ " AND";
			String parentIdAppender = requestBean.getOrgId() == 0 ? "" : " AND om.parent_id="+ requestBean.getOrgId();
			
			// String archiveAppender =
			// " UNION ALL select sgr.organization_id as organization_id,sum(sgr.ref_net_amt) as cancel from st_rep_vs_retailer as sgr where sgr.game_id="
			// + requestBean.getGameId()
			// + " AND sgr.parent_id="
			String archiveAppender = " UNION ALL select sgr.organization_id as organization_id,sum(sgr.pwt_net_amt) as pwt from st_rep_vs_retailer as sgr where "+parentIdAppenderArch+" sgr.finaldate>=date('"
					+ requestBean.getFromDate()
					+ "')"
					+ " AND sgr.finaldate<=date('"
					+ requestBean.getToDate()
					+ "')" + " GROUP BY sgr.organization_id";

			pwtQueryBuilder = new StringBuilder();
			pwtQueryBuilder
					.append(
							"select main.organization_id,sum(main.pwt)as pwt from (select organization_id,ifnull(pwt,0.0) pwt from st_lms_organization_master om left outer join (select srp.retailer_org_id,sum(pwt_amt+retailer_claim_comm) as pwt from st_vs_ret_pwt")
					// .append(requestBean.getGameId())
					.append(
							" srp inner join st_lms_retailer_transaction_master rtm on srp.transaction_id=rtm.transaction_id where transaction_type in('VS_PWT_AUTO','VS_PWT_PLR','VS_PWT') and rtm.transaction_date>='")
					.append(requestBean.getFromDate())
					.append("'")
					.append(" and rtm.transaction_date<='")
					.append(requestBean.getToDate())
					.append("'")
					.append(
							" GROUP BY srp.retailer_org_id) pwt on om.organization_id=retailer_org_id where om.organization_type='RETAILER'")
					.append(
							parentIdAppender).append(archiveAppender)
					.append(") as main group by main.organization_id");

			// logger.debug("fetchDepositDataMultipleAgent - "
			// + saleQueryBuilder.toString());

			stmt = con.createStatement();
			rs = stmt.executeQuery(pwtQueryBuilder.toString());
			while (rs.next()) {
				responseBean = new VSOrgReportResponseBean();
				responseBean.setPwtAmt(rs.getDouble("pwt"));
				responseBeanMap.put(rs.getInt("organization_id"), responseBean);
			}
		} catch (SQLException se) {
			se.printStackTrace();
			throw new GenericException(LMSErrors.SQL_EXCEPTION_CODE, se);
		} catch (Exception e) {
			e.printStackTrace();
			throw new GenericException(LMSErrors.GENERAL_EXCEPTION_CODE, e);
		} finally {
			DBConnect.closeConnection(stmt, rs);
		}

		return responseBeanMap;
	}

	public static Map<Integer, VSOrgReportResponseBean> fetchPWTDataMultipleRetailerGameWise(
			VSOrgReportRequestBean requestBean, Connection con)
			throws GenericException {
		Map<Integer, VSOrgReportResponseBean> responseBeanMap = null;
		VSOrgReportResponseBean responseBean = null;
		Statement stmt = null;
		ResultSet rs = null;
		StringBuilder pwtQueryBuilder = null;
		try {
			responseBeanMap = new HashMap<Integer, VSOrgReportResponseBean>();

			String archiveAppender = " UNION ALL select sgr.organization_id as organization_id,sum(sgr.pwt_net_amt) as pwt from st_rep_vs_retailer as sgr where sgr.game_id="
					+ requestBean.getGameId()
					+ " AND sgr.parent_id="
					+ requestBean.getOrgId()
					+ " AND sgr.finaldate>=date('"
					+ requestBean.getFromDate()
					+ "')"
					+ " AND sgr.finaldate<=date('"
					+ requestBean.getToDate()
					+ "')" + " GROUP BY sgr.organization_id";

			pwtQueryBuilder = new StringBuilder();
			pwtQueryBuilder
					.append(
							"select main.organization_id,sum(main.pwt)as pwt from (select organization_id,ifnull(pwt,0.0) pwt from st_lms_organization_master om left outer join (select srp.retailer_org_id,sum(pwt_amt+retailer_claim_comm) as pwt from st_vs_ret_pwt")
					.append(
							" srp inner join st_lms_retailer_transaction_master rtm on srp.transaction_id=rtm.transaction_id where srp.game_id=")
					.append(requestBean.getGameId())
					.append(
							" AND transaction_type in('VS_PWT_AUTO','VS_PWT_PLR','VS_PWT') and rtm.transaction_date>='")
					.append(requestBean.getFromDate())
					.append("'")
					.append(" and rtm.transaction_date<='")
					.append(requestBean.getToDate())
					.append("'")
					.append(
							" GROUP BY srp.retailer_org_id) pwt on om.organization_id=retailer_org_id where om.organization_type='RETAILER' and om.parent_id=")
					.append(requestBean.getOrgId()).append(archiveAppender)
					.append(") as main group by main.organization_id");

			// logger.debug("fetchDepositDataMultipleAgent - "
			// + saleQueryBuilder.toString());

			stmt = con.createStatement();
			rs = stmt.executeQuery(pwtQueryBuilder.toString());
			while (rs.next()) {
				responseBean = new VSOrgReportResponseBean();
				responseBean.setPwtAmt(rs.getDouble("pwt"));
				responseBeanMap.put(rs.getInt("organization_id"), responseBean);
			}
		} catch (SQLException se) {
			se.printStackTrace();
			throw new GenericException(LMSErrors.SQL_EXCEPTION_CODE, se);
		} catch (Exception e) {
			e.printStackTrace();
			throw new GenericException(LMSErrors.GENERAL_EXCEPTION_CODE, e);
		} finally {
			DBConnect.closeConnection(stmt, rs);
		}

		return responseBeanMap;
	}
	
	/**
	 * Fetch PWT Data for all game Single Retailer Game Wise(For all game ID)
	 * 
	 * @param1-Request bean containing from date, to date, and organization ID
	 * @param2-Connection
	 * @return-data map
	 * @throws-GenericException Exception
	 */
	public static Map<String, VSOrgReportResponseBean> fetchPWTDataDateWiseSingleRetailerAllGame(
			VSOrgReportRequestBean requestBean, Connection con)
			throws GenericException {
		Map<String, VSOrgReportResponseBean> responseBeanMap = null;
		VSOrgReportResponseBean responseBean = null;
		Statement stmt = null;
		ResultSet rs = null;
		StringBuilder pwtQueryBuilder = null;
		try {
			responseBeanMap = new HashMap<String, VSOrgReportResponseBean>();

			String archiveAppender = " UNION ALL select finaldate,organization_id,sum(pwt_net_amt) as pwt from st_rep_vs_retailer where finaldate>='"
					+ requestBean.getFromDate()
					+ "' and finaldate<='"
					+ requestBean.getToDate()
					+ "' and organization_id=" 
					+ requestBean.getOrgId()
					+ " GROUP BY finaldate";

			pwtQueryBuilder = new StringBuilder();
			pwtQueryBuilder
					.append("select date(transaction_date) date,retailer_org_id,sum(pwt) as pwt from ((select retailer_org_id,sum(pwt_amt+retailer_claim_comm) as pwt,transaction_date from st_vs_ret_pwt where transaction_date>='")
					.append(requestBean.getFromDate())
					.append("'and transaction_date<='")
					.append(requestBean.getToDate())
					.append("' and retailer_org_id=")
					.append(requestBean.getOrgId())
					.append(" group by date(transaction_date))) pwttable group by date(transaction_date)")
					.append(archiveAppender);

			// logger.debug("fetchDepositDataMultipleAgent - "
			// + saleQueryBuilder.toString());

			stmt = con.createStatement();
			rs = stmt.executeQuery(pwtQueryBuilder.toString());
			while (rs.next()) {
				responseBean = new VSOrgReportResponseBean();
				responseBean.setPwtAmt(rs.getDouble("pwt"));
				responseBeanMap.put(rs.getString("date"), responseBean);
			}
		} catch (SQLException se) {
			se.printStackTrace();
			throw new GenericException(LMSErrors.SQL_EXCEPTION_CODE, se);
		} catch (Exception e) {
			e.printStackTrace();
			throw new GenericException(LMSErrors.GENERAL_EXCEPTION_CODE, e);
		}  finally {
		    DBConnect.closeConnection(stmt, rs);
		}

		return responseBeanMap;
	}

}
