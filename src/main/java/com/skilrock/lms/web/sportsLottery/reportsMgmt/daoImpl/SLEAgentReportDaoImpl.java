
package com.skilrock.lms.web.sportsLottery.reportsMgmt.daoImpl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.GenericException;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.web.sportsLottery.reportsMgmt.beans.SLEOrgReportRequestBean;
import com.skilrock.lms.web.sportsLottery.reportsMgmt.beans.SLEOrgReportResponseBean;

public class SLEAgentReportDaoImpl {

	// private static final Logger logger = LoggerFactory
	// .getLogger("SLAgentReportDaoImpl");

	/**
	 * @param args
	 * @throws Exception
	 */
	/**
	 * @param args
	 * @throws Exception
	 */
	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SLEOrgReportRequestBean requestBean = new SLEOrgReportRequestBean();
		requestBean.setOrgId(3);
		requestBean.setFromDate(new Timestamp(dateFormat.parse(
				"2014-11-01 00:00:00").getTime()));
		requestBean.setToDate(new Timestamp(dateFormat.parse(
				"2014-11-11 00:00:00").getTime()));

		/*
		 * requestBean.setGameId(2); Map<Integer, SLEOrgReportResponseBean>
		 * responseMap = fetchDirPlyPWTDataGameWiseMultipleAgent( requestBean,
		 * DBConnect.getConnection()); System.out.println("Res Map:" +
		 * responseMap);
		 */

		requestBean.setGameId(1);
		requestBean.setOrgId(1);
		Map<Integer, SLEOrgReportResponseBean> responseMap = fetchCancelDataGameWiseMultipleAgent(
				requestBean, DBConnect.getConnection());

		System.out.println("Res Map:" + responseMap);
		// System.out.println("Res Map:" + responseMap.get(1));
	}

	/**
	 * 
	 * @param requestBean
	 * @param connection
	 * @return
	 * @throws GenericException
	 */
	public static Map<Integer, SLEOrgReportResponseBean> fetchSaleDataGameWiseMultipleAgent(
			SLEOrgReportRequestBean requestBean, Connection connection)
			throws GenericException {
		Map<Integer, SLEOrgReportResponseBean> responseBeanMap = null;
		SLEOrgReportResponseBean responseBean = null;
		Statement stmt = null;
		ResultSet rs = null;
		StringBuilder saleQueryBuilder = null;
		try {
			responseBeanMap = new HashMap<Integer, SLEOrgReportResponseBean>();

			// String walletAppender = (requestBean.getWalletId() == 0) ? ""
			// : " AND wallet_id=" + requestBean.getWalletId();

			String archiveAppender = " UNION ALL select organization_id,ifnull(sum(sale_net),0.0) as sale from st_rep_sle_agent where game_id="
					+ requestBean.getGameId()
					+ " AND finaldate>=date('"
					+ requestBean.getFromDate()
					+ "')"
					+ " AND finaldate<=date('"
					+ requestBean.getToDate()
					+ "')"
					+ " group by organization_id";

			saleQueryBuilder = new StringBuilder();
			saleQueryBuilder
					.append(
							"select parent_id,sum(main.sale) as sale from(select sale.parent_id,sum(sale) sale from(select organization_id,parent_id,SUM(ifnull(sale,0.0)) sale from st_lms_organization_master om left outer join (select srs.retailer_org_id,sum(agent_net_amt) as sale from st_sle_ret_sale srs inner join st_lms_retailer_transaction_master rtm on srs.transaction_id=rtm.transaction_id")
					.append(" WHERE srs.game_id=")
					.append(requestBean.getGameId())
					.append(
							" AND rtm.transaction_type in('SLE_SALE') and rtm.transaction_date>='")
					.append(requestBean.getFromDate())
					.append("'")
					.append(" and rtm.transaction_date<='")
					.append(requestBean.getToDate())
					.append("'")
					.append(
							" group by srs.retailer_org_id) sale on om.organization_id=retailer_org_id where om.organization_type='RETAILER' group by parent_id ) sale group by parent_id")
					.append(archiveAppender).append(
							") as main group by main.parent_id");

			// logger.debug("fetchDepositDataMultipleAgent - "
			// + saleQueryBuilder.toString());

			stmt = connection.createStatement();
			rs = stmt.executeQuery(saleQueryBuilder.toString());
			while (rs.next()) {
				responseBean = new SLEOrgReportResponseBean();
				responseBean.setSaleAmt(rs.getDouble("sale"));
				responseBeanMap.put(rs.getInt("parent_id"), responseBean);
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
	 * 
	 * @param requestBean
	 * @param connection
	 * @return
	 * @throws GenericException
	 */
	public static Map<Integer, SLEOrgReportResponseBean> fetchSaleDataAllGameMultipleAgent(
			SLEOrgReportRequestBean requestBean, Connection connection)
			throws GenericException {
		Map<Integer, SLEOrgReportResponseBean> responseBeanMap = null;
		SLEOrgReportResponseBean responseBean = null;
		Statement stmt = null;
		ResultSet rs = null;
		StringBuilder saleQueryBuilder = null;
		try {
			responseBeanMap = new HashMap<Integer, SLEOrgReportResponseBean>();

			// String walletAppender = (requestBean.getWalletId() == 0) ? ""
			// : " AND wallet_id=" + requestBean.getWalletId();

			String archiveAppender = " UNION ALL select organization_id,ifnull(sum(sale_net),0.0) as sale from st_rep_sle_agent where organization_id in (select organization_id from st_lms_user_master where termination_date>'"+requestBean.getFromDate()+"' or registration_date <'"+requestBean.getToDate()+"' and isrolehead='Y' and organization_type='AGENT' ) and  finaldate>=date('"
					+ requestBean.getFromDate()
					+ "')"
					+ " AND finaldate<=date('"
					+ requestBean.getToDate()
					+ "')"
					+ " group by organization_id";

			saleQueryBuilder = new StringBuilder();
			saleQueryBuilder
					.append(
							"select parent_id,sum(main.sale) as sale from(select sale.parent_id,sum(sale) sale from(select organization_id,parent_id,ifnull(sum(sale),0.0) sale from st_lms_organization_master om left outer join (select drs.retailer_org_id,sum(agent_net_amt) as sale from st_sle_ret_sale")
					.append(
							" drs inner join st_lms_retailer_transaction_master rtm on drs.transaction_id=rtm.transaction_id where rtm.retailer_org_id in (select organization_id from st_lms_user_master where parent_user_id not in (select user_id from st_lms_user_master where termination_date<'"+requestBean.getFromDate()+"' or registration_date >'"+requestBean.getToDate()+"' and isrolehead='Y' and organization_type='AGENT' ) AND  organization_type='RETAILER' ) and  rtm.transaction_type in('SLE_SALE') and rtm.transaction_date>='")
					.append(requestBean.getFromDate())
					.append("'")
					.append(" and rtm.transaction_date<='")
					.append(requestBean.getToDate())
					.append("'")
					.append(
							" group by drs.retailer_org_id) sale on om.organization_id=retailer_org_id where om.organization_type='RETAILER' group by parent_id ) sale group by parent_id")
					.append(archiveAppender).append(
							") as main group by main.parent_id");

			// logger.debug("fetchDepositDataMultipleAgent - "
			// + saleQueryBuilder.toString());

			System.out.println("fetchSaleDataMultipleAgent - "+saleQueryBuilder.toString());
			
			stmt = connection.createStatement();
			rs = stmt.executeQuery(saleQueryBuilder.toString());
			while (rs.next()) {
				responseBean = new SLEOrgReportResponseBean();
				responseBean.setSaleAmt(rs.getDouble("sale"));
				responseBeanMap.put(rs.getInt("parent_id"), responseBean);
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
	 * 
	 * @param requestBean
	 * @param connection
	 * @return
	 * @throws GenericException
	 */
	public static void fetchSaleDataAllGameSingleAgent(
			SLEOrgReportRequestBean requestBean, SLEOrgReportResponseBean responseBean, Connection connection)
			throws GenericException {
		Statement stmt = null;
		ResultSet rs = null;
		StringBuilder saleQueryBuilder = null;
		try {
			String archiveAppender = " UNION ALL SELECT organization_id,IFNULL(SUM(sale_net),0.0) AS sale FROM st_rep_sle_agent WHERE organization_id="
					+ requestBean.getOrgId()
					+ " AND finaldate>=DATE('"
					+ requestBean.getFromDate()
					+ "')"
					+ " AND finaldate<=DATE('"
					+ requestBean.getToDate()
					+ "')";

			saleQueryBuilder = new StringBuilder();
			saleQueryBuilder
					.append(
							"select parent_id,sum(main.sale) as sale from (select sale.parent_id,sum(sale) sale from (select organization_id,parent_id,ifnull(sale,0.0) sale from st_lms_organization_master om left outer join (select retailer_org_id,sum(agent_net_amt) as sale from st_sle_ret_sale WHERE transaction_date>='")
					.append(requestBean.getFromDate())
					.append("'")
					.append(" and transaction_date<='")
					.append(requestBean.getToDate())
					.append("'")
					.append(" group by retailer_org_id) sale on om.organization_id=retailer_org_id where om.organization_type='RETAILER' and om.parent_id=")
					.append(requestBean.getOrgId())
					.append(") sale")
					.append(archiveAppender).append(
							") as main group by main.parent_id");

			// logger.debug("fetchDepositDataMultipleAgent - "
			// + saleQueryBuilder.toString());

			System.out.println("fetchSaleDataAllGameSingleAgent - "+saleQueryBuilder.toString());
			
			stmt = connection.createStatement();
			rs = stmt.executeQuery(saleQueryBuilder.toString());
			while (rs.next()) {
				responseBean.setSaleAmt(responseBean.getSaleAmt()+ rs.getDouble("sale"));
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
		
	}

	/**
	 * 
	 * @param requestBean
	 * @param connection
	 * @return
	 * @throws GenericException
	 */
	public static Map<String, SLEOrgReportResponseBean> fetchSaleDataDayWisePerGameForBO(
			SLEOrgReportRequestBean requestBean, Connection connection)
			throws GenericException {
		Map<String, SLEOrgReportResponseBean> responseBeanMap = null;
		SLEOrgReportResponseBean responseBean = null;
		Statement stmt = null;
		ResultSet rs = null;
		StringBuilder saleQueryBuilder = null;
		try {
			responseBeanMap = new HashMap<String, SLEOrgReportResponseBean>();

			String archiveAppender = " union all select finaldate as alldate, SUM(sale_net) as sale from st_rep_sle_agent where organization_id in (select organization_id from st_lms_user_master  where (termination_date > '"+requestBean.getFromDate()+"' or registration_date< '"+requestBean.getToDate()+"') and isrolehead='Y' and organization_type='AGENT') and  game_id="
					+ requestBean.getGameId()
					+ " AND finaldate>=date('"
					+ requestBean.getFromDate()
					+ "')"
					+ " AND finaldate<=date('"
					+ requestBean.getToDate()
					+ "')"
					+ " GROUP BY alldate";

			saleQueryBuilder = new StringBuilder();
			saleQueryBuilder
					.append(
							"select main.alldate,sum(main.sale)as sale from (select sale.alldate,ifnull(sale,0.0) sale from(select alldate,sale from tempdate left outer join (select date(rtm.transaction_date) tx_date,sum(agent_net_amt) sale from st_sle_ret_sale")
					.append(
							" srs inner join st_lms_retailer_transaction_master rtm on srs.transaction_id=rtm.transaction_id where rtm.retailer_org_id in (select organization_id from st_lms_user_master where parent_user_id not in (select user_id from st_lms_user_master  where (termination_date < '"+requestBean.getFromDate()+"' or registration_date> '"+requestBean.getToDate()+"') and isrolehead='Y' and organization_type='AGENT') and organization_type='RETAILER' ) and srs.game_id=")
					.append(requestBean.getGameId())
					.append(" AND rtm.transaction_date>='")
					.append(requestBean.getFromDate())
					.append("'")
					.append(" and rtm.transaction_date<='")
					.append(requestBean.getToDate())
					.append("'")
					.append(
							" and transaction_type in('SLE_SALE') group by tx_date) sale on alldate=sale.tx_date) sale")
					.append(archiveAppender).append(
							") as main group by main.alldate");

			// logger.debug("fetchDepositDataMultipleAgent - "
			// + saleQueryBuilder.toString());

			System.out.println("fetchSaleDataSingleAgent - "
					+ saleQueryBuilder.toString());

			stmt = connection.createStatement();
			rs = stmt.executeQuery(saleQueryBuilder.toString());
			while (rs.next()) {
				responseBean = new SLEOrgReportResponseBean();
				responseBean.setSaleAmt(rs.getDouble("sale"));
				responseBeanMap.put(rs.getDate("alldate").toString(), responseBean);
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
	 * 
	 * @param requestBean
	 * @param connection
	 * @return
	 * @throws GenericException
	 */
	public static Map<String, SLEOrgReportResponseBean> fetchSaleDataDayWiseAllGameForBO(
			SLEOrgReportRequestBean requestBean, Connection connection)
			throws GenericException {
		Map<String, SLEOrgReportResponseBean> responseBeanMap = null;
		SLEOrgReportResponseBean responseBean = null;
		Statement stmt = null;
		ResultSet rs = null;
		StringBuilder saleQueryBuilder = null;
		try {
			responseBeanMap = new HashMap<String, SLEOrgReportResponseBean>();

			String archiveAppender = " union all select finaldate as alldate, SUM(sale_net) as sale from st_rep_sle_agent where finaldate>=date('"
					+ requestBean.getFromDate()
					+ "')"
					+ " AND finaldate<=date('"
					+ requestBean.getToDate()
					+ "')"
					+ " GROUP BY organization_id";

			saleQueryBuilder = new StringBuilder();
			saleQueryBuilder
					.append(
							"select main.alldate,sum(main.sale)as sale from (select sale.alldate,ifnull(sale,0.0) sale from(select alldate,sale from tempdate left outer join (select date(rtm.transaction_date) tx_date,sum(agent_net_amt) sale from st_sle_ret_sale")
					.append(
							" drs inner join st_lms_retailer_transaction_master rtm on drs.transaction_id=rtm.transaction_id where rtm.transaction_date>='")
					.append(requestBean.getFromDate())
					.append("'")
					.append(" and rtm.transaction_date<='")
					.append(requestBean.getToDate())
					.append("'")
					.append(
							" and transaction_type in('SLE_SALE') group by tx_date) sale on alldate=sale.tx_date) sale")
					.append(archiveAppender).append(
							") as main group by main.alldate");

			// logger.debug("fetchDepositDataMultipleAgent - "
			// + saleQueryBuilder.toString());

			System.out.println("fetchSaleDataSingleAgent - "
					+ saleQueryBuilder.toString());

			stmt = connection.createStatement();
			rs = stmt.executeQuery(saleQueryBuilder.toString());
			while (rs.next()) {
				responseBean = new SLEOrgReportResponseBean();
				responseBean.setSaleAmt(rs.getDouble("sale"));
				responseBeanMap.put(rs.getString("alldate"), responseBean);
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
	 * For Sale-PWT Report view by Agent
	 * 
	 * @param requestBean
	 * @param connection
	 * @return
	 * @throws GenericException
	 */
	public static Map<String, SLEOrgReportResponseBean> fetchSaleDataDayWisePerGameForAgent(
			SLEOrgReportRequestBean requestBean, Connection connection)
			throws GenericException {
		Map<String, SLEOrgReportResponseBean> responseBeanMap = null;
		SLEOrgReportResponseBean responseBean = null;
		Statement stmt = null;
		ResultSet rs = null;
		StringBuilder saleQueryBuilder = null;
		try {
			responseBeanMap = new HashMap<String, SLEOrgReportResponseBean>();
//TODO: Check this for single Agent...
			String archiveAppender = " UNION ALL select finaldate,ifnull(sum(sale_net),0.0) as sale from st_rep_sle_agent where organization_id in (select organization_id from st_lms_user_master where termination_date>'"+requestBean.getFromDate()+"' or registration_date <'"+requestBean.getToDate()+"' and isrolehead='Y' and organization_type='AGENT' and organization_id = "
					+ requestBean.getOrgId() + ") and  finaldate>=date('"
				+ requestBean.getFromDate()
				+ "')"
				+ " AND finaldate<=date('"
				+ requestBean.getToDate()
				+ "')"
				+ " group by finaldate";

			saleQueryBuilder = new StringBuilder();
			saleQueryBuilder
					.append(
							"select main.alldate,sum(main.sale)as sale from (select sale.alldate,ifnull(sale,0.0) sale from(select alldate,sale1.sale from tempdate left outer join (select organization_id,sale.tx_date,sale.sale from st_lms_organization_master om left outer join (select date(rtm.transaction_date) tx_date,sum(agent_net_amt) sale, rtm.retailer_org_id from st_sle_ret_sale drs inner join st_lms_retailer_transaction_master rtm on drs.transaction_id=rtm.transaction_id where drs.game_id=")
					.append(requestBean.getGameId())
					.append(" AND rtm.transaction_date>='")
					.append(requestBean.getFromDate())
					.append("'")
					.append(" AND rtm.transaction_date<='")
					.append(requestBean.getToDate())
					.append("'")
					.append(
							" and transaction_type in('SLE_SALE') group by tx_date,retailer_org_id) sale on retailer_org_id=om.organization_id where parent_id=")
					.append(requestBean.getOrgId())
					.append(
							" group by sale.tx_date,organization_id) sale1 on alldate=sale1.tx_date) sale")
					.append(archiveAppender).append(
							") as main group by main.alldate");

			// logger.debug("fetchDepositDataMultipleAgent - "
			// + saleQueryBuilder.toString());

			System.out.println("fetchSaleDataSingleAgentViewByAgent - "
					+ saleQueryBuilder.toString());

			stmt = connection.createStatement();
			rs = stmt.executeQuery(saleQueryBuilder.toString());
			while (rs.next()) {
				responseBean = new SLEOrgReportResponseBean();
				responseBean.setSaleAmt(rs.getDouble("sale"));
				responseBeanMap.put(rs.getString("alldate").split(" ")[0], responseBean);
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
	 * For Sale-PWT Report view by Agent
	 * 
	 * @param requestBean
	 * @param connection
	 * @return
	 * @throws GenericException
	 */
	public static Map<String, SLEOrgReportResponseBean> fetchSaleDataDayWiseAllGameForAgent(
			SLEOrgReportRequestBean requestBean, Connection connection)
			throws GenericException {
		Map<String, SLEOrgReportResponseBean> responseBeanMap = null;
		SLEOrgReportResponseBean responseBean = null;
		Statement stmt = null;
		ResultSet rs = null;
		StringBuilder saleQueryBuilder = null;
		try {
			responseBeanMap = new HashMap<String, SLEOrgReportResponseBean>();

			String archiveAppender = " union all select sgr.finaldate as alldate,sum(sgr.sale_net) as sale from st_rep_sle_retailer as sgr left outer join  st_rep_sle_agent as sga on sgr.finaldate=sga.finaldate and sgr.parent_id=sga.parent_id where sgr.parent_id="
					+ requestBean.getOrgId()
					// + " AND sgr.game_id="
					// + requestBean.getGameId()
					+ " AND sgr.finaldate>=date('"
					+ requestBean.getFromDate()
					+ "')"
					+ " AND sgr.finaldate<=date('"
					+ requestBean.getToDate()
					+ "')"
					+ " group by sgr.finaldate";

			saleQueryBuilder = new StringBuilder();
			saleQueryBuilder
					.append(
							"select main.alldate,sum(main.sale)as sale from (select sale.alldate,ifnull(sale,0.0) sale from(select alldate,sale1.sale from tempdate left outer join (select organization_id,sale.tx_date,sale.sale from st_lms_organization_master om left outer join (select date(rtm.transaction_date) tx_date,sum(retailer_net_amt) sale, rtm.retailer_org_id from st_sle_ret_sale")
					// .append(requestBean.getGameId())
					.append(
							" drs inner join st_lms_retailer_transaction_master rtm on drs.transaction_id=rtm.transaction_id where rtm.transaction_date>='")
					.append(requestBean.getFromDate())
					.append("'")
					.append(" and rtm.transaction_date<='")
					.append(requestBean.getToDate())
					.append("'")
					.append(
							" and transaction_type in('SLE_SALE')) sale on retailer_org_id=om.organization_id where parent_id=")
					.append(requestBean.getOrgId())
					.append(
							" group by sale.tx_date) sale1 on alldate=sale1.tx_date) sale")
					.append(archiveAppender).append(
							") as main group by main.alldate");

			// logger.debug("fetchDepositDataMultipleAgent - "
			// + saleQueryBuilder.toString());

			System.out.println("fetchSaleDataSingleAgentViewByAgent - "
					+ saleQueryBuilder.toString());

			stmt = connection.createStatement();
			rs = stmt.executeQuery(saleQueryBuilder.toString());
			while (rs.next()) {
				responseBean = new SLEOrgReportResponseBean();
				responseBean.setSaleAmt(rs.getDouble("sale"));
				responseBeanMap.put(rs.getString("alldate"), responseBean);
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
	 * For Payment-Ledger Report view by BO
	 * 
	 * @param requestBean
	 * @param connection
	 * @return
	 * @throws GenericException
	 */
	public static Map<String, SLEOrgReportResponseBean> fetchSaleDataDayWiseAllGameSingleAgent(
			SLEOrgReportRequestBean requestBean, Connection connection)
			throws GenericException {
		Map<String, SLEOrgReportResponseBean> responseBeanMap = null;
		SLEOrgReportResponseBean responseBean = null;
		Statement stmt = null;
		ResultSet rs = null;
		StringBuilder saleQueryBuilder = null;
		try {
			responseBeanMap = new HashMap<String, SLEOrgReportResponseBean>();

			String archiveAppender = " union all SELECT finaldate,organization_id,SUM(sale_net) AS sale FROM st_rep_sle_agent WHERE finaldate>='"
					+ requestBean.getFromDate()
					+ "' AND finaldate<='"
					+ requestBean.getToDate()
					+ "'AND organization_id="
					+ requestBean.getOrgId()
					+ " GROUP BY finaldate";

			saleQueryBuilder = new StringBuilder();
			saleQueryBuilder
					.append(
							"SELECT DATE(transaction_date) DATE,parent_id,SUM(sale) AS sale FROM (SELECT retailer_org_id,SUM(agent_net_amt) AS sale,transaction_DATE FROM st_sle_ret_sale WHERE transaction_date>='")
					.append(requestBean.getFromDate())
					.append("' AND transaction_date<='")
					.append(requestBean.getToDate())
					.append("' AND retailer_org_id IN (SELECT organization_id FROM st_lms_organization_master WHERE organization_type='RETAILER') GROUP BY DATE(transaction_date),retailer_org_id)aa,st_lms_organization_master bb WHERE retailer_org_id=organization_id AND parent_id=")
					.append(requestBean.getOrgId())
					.append(" GROUP BY DATE(transaction_date)")
					.append(archiveAppender);

			// logger.debug("fetchDepositDataMultipleAgent - "
			// + saleQueryBuilder.toString());

			System.out.println("fetchSaleDataDayWiseAllGameSingleAgent - "
					+ saleQueryBuilder.toString());

			stmt = connection.createStatement();
			rs = stmt.executeQuery(saleQueryBuilder.toString());
			while (rs.next()) {
				responseBean = new SLEOrgReportResponseBean();
				responseBean.setSaleAmt(rs.getDouble("sale"));
				responseBeanMap.put(rs.getString("date"), responseBean);
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
	 * 
	 * @param requestBean
	 * @param connection
	 * @return
	 * @throws GenericException
	 */
	public static Map<Integer, SLEOrgReportResponseBean> fetchCancelDataGameWiseMultipleAgent(
			SLEOrgReportRequestBean requestBean, Connection connection)
			throws GenericException {
		Map<Integer, SLEOrgReportResponseBean> responseBeanMap = null;
		SLEOrgReportResponseBean responseBean = null;
		Statement stmt = null;
		ResultSet rs = null;
		StringBuilder cancelQueryBuilder = null;
		try {
			responseBeanMap = new HashMap<Integer, SLEOrgReportResponseBean>();

			String archiveAppender = " UNION ALL select organization_id,sum(ref_net_amt) as cancel from st_rep_sle_agent where game_id="
					+ requestBean.getGameId()
					+ " AND finaldate>=date('"
					+ requestBean.getFromDate()
					+ "')"
					+ " AND finaldate<=date('"
					+ requestBean.getToDate()
					+ "')"
					+ " group by organization_id";

			cancelQueryBuilder = new StringBuilder();
			cancelQueryBuilder
					.append(
							"select parent_id,sum(main.cancel) as cancel from (select cancel.parent_id,sum(cancel) cancel from (select organization_id,parent_id,ifnull(sum(cancel),0.0) cancel from st_lms_organization_master om left outer join (select drs.retailer_org_id,sum(agent_net_amt) as cancel from st_sle_ret_sale_refund drs inner join st_lms_retailer_transaction_master rtm on drs.transaction_id=rtm.transaction_id where drs.game_id=")
					.append(requestBean.getGameId())
					.append(
							" AND rtm.transaction_type in('SLE_REFUND_CANCEL','SLE_REFUND_FAILED') and rtm.transaction_date>='")
					.append(requestBean.getFromDate())
					.append("'")
					.append(" AND rtm.transaction_date<='")
					.append(requestBean.getToDate())
					.append("'")
					.append(
							" group by drs.retailer_org_id)cancel on om.organization_id=retailer_org_id where om.organization_type='RETAILER' group by parent_id ) cancel group by parent_id")
					.append(archiveAppender).append(
							") as main group by main.parent_id");
			// logger.debug("fetchDepositDataMultipleAgent - "
			// + cancelQueryBuilder.toString());

			System.out.println("fetchCancelDataMultipleAgent - "
					+ cancelQueryBuilder.toString());

			stmt = connection.createStatement();
			rs = stmt.executeQuery(cancelQueryBuilder.toString());
			while (rs.next()) {
				responseBean = new SLEOrgReportResponseBean();
				responseBean.setCancelAmt(rs.getDouble("cancel"));
				responseBeanMap.put(rs.getInt("parent_id"), responseBean);
			}
			System.out.println("fetchCancelDataMultipleAgent - "
					+ cancelQueryBuilder.toString());
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
	 * 
	 * @param requestBean
	 * @param connection
	 * @return
	 * @throws GenericException
	 */
	public static Map<Integer, SLEOrgReportResponseBean> fetchCancelDataAllGameMultipleAgent(
			SLEOrgReportRequestBean requestBean, Connection connection)
			throws GenericException {
		Map<Integer, SLEOrgReportResponseBean> responseBeanMap = null;
		SLEOrgReportResponseBean responseBean = null;
		Statement stmt = null;
		ResultSet rs = null;
		StringBuilder cancelQueryBuilder = null;
		try {
			responseBeanMap = new HashMap<Integer, SLEOrgReportResponseBean>();

			String archiveAppender = " UNION ALL select organization_id,sum(ref_net_amt) as cancel from st_rep_sle_agent where organization_id in (select organization_id from st_lms_user_master where termination_date>'"+requestBean.getFromDate()+"' or registration_date <'"+requestBean.getToDate()+"' and isrolehead='Y' and organization_type='AGENT' ) and finaldate>=date('"
					// + requestBean.getGameId()
					// + " AND finaldate>=date('"
					+ requestBean.getFromDate()
					+ "')"
					+ " AND finaldate<=date('"
					+ requestBean.getToDate()
					+ "')"
					+ " group by organization_id";

			cancelQueryBuilder = new StringBuilder();
			cancelQueryBuilder
					.append(
							"select parent_id,sum(main.cancel) as cancel from (select cancel.parent_id,sum(cancel) cancel from (select organization_id,parent_id,ifnull(sum(cancel),0.0) cancel from st_lms_organization_master om left outer join (select drs.retailer_org_id,sum(agent_net_amt) as cancel from st_sle_ret_sale_refund")
					// .append(requestBean.getGameId())
					.append(
							" drs inner join st_lms_retailer_transaction_master rtm on drs.transaction_id=rtm.transaction_id where rtm.retailer_org_id in (select organization_id from st_lms_user_master where parent_user_id not in (select user_id from st_lms_user_master where termination_date<'"+requestBean.getFromDate()+"' or registration_date >'"+requestBean.getToDate()+"' and isrolehead='Y' and organization_type='AGENT' ) AND  organization_type='RETAILER' ) and  rtm.transaction_type in('SLE_REFUND_CANCEL','SLE_REFUND_FAILED') and rtm.transaction_date>='")
					.append(requestBean.getFromDate())
					.append("'")
					.append(" and rtm.transaction_date<='")
					.append(requestBean.getToDate())
					.append("'")
					.append(
							" group by drs.retailer_org_id)cancel on om.organization_id=retailer_org_id where om.organization_type='RETAILER' group by parent_id ) cancel group by parent_id")
					.append(archiveAppender).append(
							") as main group by main.parent_id");
			// logger.debug("fetchDepositDataMultipleAgent - "
			// + cancelQueryBuilder.toString());

			System.out.println("fetchCancelDataMultipleAgent - "
					+ cancelQueryBuilder.toString());

			stmt = connection.createStatement();
			rs = stmt.executeQuery(cancelQueryBuilder.toString());
			while (rs.next()) {
				responseBean = new SLEOrgReportResponseBean();
				responseBean.setCancelAmt(rs.getDouble("cancel"));
				responseBeanMap.put(rs.getInt("parent_id"), responseBean);
			}
			System.out.println("fetchCancelDataMultipleAgent - "
					+ cancelQueryBuilder.toString());
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
	 * 
	 * @param requestBean
	 * @param connection
	 * @return
	 * @throws GenericException
	 */
	public static Map<String, SLEOrgReportResponseBean> fetchCancelDataDayWisePerGameForBO(
			SLEOrgReportRequestBean requestBean, Connection connection)
			throws GenericException {
		Map<String, SLEOrgReportResponseBean> responseBeanMap = null;
		SLEOrgReportResponseBean responseBean = null;
		Statement stmt = null;
		ResultSet rs = null;
		StringBuilder cancelQueryBuilder = null;
		try {
			responseBeanMap = new HashMap<String, SLEOrgReportResponseBean>();

			// String walletAppender = (requestBean.getWalletId() == 0) ? ""
			// : " AND wallet_id=" + requestBean.getWalletId();

			String archiveAppender = " union all select finaldate as alldate, SUM(ref_net_amt) as cancel from st_rep_sle_agent where organization_id in (select organization_id from st_lms_user_master  where (termination_date > '"+requestBean.getFromDate()+"' or registration_date< '"+requestBean.getToDate()+"') and isrolehead='Y' and organization_type='AGENT') and  game_id="
					+ requestBean.getGameId()
					+ " AND finaldate>=date('"
					+ requestBean.getFromDate()
					+ "')"
					+ " AND finaldate<=date('"
					+ requestBean.getToDate()
					+ "')"
					+ " GROUP BY alldate";

			cancelQueryBuilder = new StringBuilder();
			cancelQueryBuilder
					.append(
							"select main.alldate,sum(main.cancel)as cancel from (select cancel.alldate,ifnull(cancel,0.0) cancel from(select alldate,cancel from tempdate left outer join (select date(rtm.transaction_date) tx_date,sum(agent_net_amt) cancel from st_sle_ret_sale_refund drs inner join st_lms_retailer_transaction_master rtm on drs.transaction_id=rtm.transaction_id where rtm.retailer_org_id in (select organization_id from st_lms_user_master where parent_user_id not in (select user_id from st_lms_user_master  where (termination_date < '"+requestBean.getFromDate()+"' or registration_date> '"+requestBean.getToDate()+"') and isrolehead='Y' and organization_type='AGENT') and organization_type='RETAILER' ) and  drs.game_id=")
					.append(requestBean.getGameId())
					.append(" AND rtm.transaction_date>='")
					.append(requestBean.getFromDate())
					.append("'")
					.append(" AND rtm.transaction_date<='")
					.append(requestBean.getToDate())
					.append("'")
					.append(
							" and transaction_type in('SLE_REFUND_CANCEL','SLE_REFUND_FAILED') group by tx_date) cancel on alldate=cancel.tx_date) cancel")
					.append(archiveAppender).append(
							") as main group by main.alldate");

			// logger.debug("fetchCancelDataDayWisePerGameSingleAgent - "
			// + cancelQueryBuilder.toString());

			System.out.println("fetchCancelDataDayWisePerGameSingleAgent - "
					+ cancelQueryBuilder.toString());

			stmt = connection.createStatement();
			rs = stmt.executeQuery(cancelQueryBuilder.toString());
			while (rs.next()) {
				responseBean = new SLEOrgReportResponseBean();
				responseBean.setCancelAmt(rs.getDouble("cancel"));
				responseBeanMap.put(rs.getDate("alldate").toString(), responseBean);
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
	 * 
	 * @param requestBean
	 * @param connection
	 * @return
	 * @throws GenericException
	 */
	public static Map<String, SLEOrgReportResponseBean> fetchCancelDataDayWiseAllGameForBO(
			SLEOrgReportRequestBean requestBean, Connection connection)
			throws GenericException {
		Map<String, SLEOrgReportResponseBean> responseBeanMap = null;
		SLEOrgReportResponseBean responseBean = null;
		Statement stmt = null;
		ResultSet rs = null;
		StringBuilder cancelQueryBuilder = null;
		try {
			responseBeanMap = new HashMap<String, SLEOrgReportResponseBean>();

			// String walletAppender = (requestBean.getWalletId() == 0) ? ""
			// : " AND wallet_id=" + requestBean.getWalletId();

			String archiveAppender = " union all select finaldate as alldate, SUM(ref_net_amt) as cancel from st_rep_sle_agent where finaldate>=date('"
					// + requestBean.getGameId()
					// + " AND finaldate>=date('"
					+ requestBean.getFromDate()
					+ "')"
					+ " AND finaldate<=date('"
					+ requestBean.getToDate()
					+ "')"
					+ " GROUP BY organization_id";

			cancelQueryBuilder = new StringBuilder();
			cancelQueryBuilder
					.append(
							"select main.alldate,sum(main.cancel)as cancel from (select cancel.alldate,ifnull(cancel,0.0) cancel from(select alldate,cancel from tempdate left outer join (select date(transaction_date) tx_date,sum(agent_net_amt) cancel from st_sle_ret_sale_refund drs inner join st_lms_retailer_transaction_master rtm on drs.transaction_id=rtm.transaction_id where rtm.transaction_date>='")
					// .append(requestBean.getGameId())
					// .append(
					// " drs inner join st_lms_retailer_transaction_master rtm on drs.transaction_id=rtm.transaction_id where rtm.transaction_date>='")
					.append(requestBean.getFromDate())
					.append("'")
					.append(" and rtm.transaction_date<='")
					.append(requestBean.getToDate())
					.append("'")
					.append(
							" and transaction_type in('SLE_REFUND_CANCEL','SLE_REFUND_FAILED') group by tx_date) cancel on alldate=cancel.tx_date) cancel")
					.append(archiveAppender).append(
							") as main group by main.alldate");

			// logger.debug("fetchCancelDataDayWisePerGameSingleAgent - "
			// + cancelQueryBuilder.toString());

			System.out.println("fetchCancelDataDayWisePerGameSingleAgent - "
					+ cancelQueryBuilder.toString());

			stmt = connection.createStatement();
			rs = stmt.executeQuery(cancelQueryBuilder.toString());
			while (rs.next()) {
				responseBean = new SLEOrgReportResponseBean();
				responseBean.setCancelAmt(rs.getDouble("cancel"));
				responseBeanMap.put(rs.getString("alldate"), responseBean);
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
	 * 
	 * @param requestBean
	 * @param connection
	 * @return
	 * @throws GenericException
	 */
	public static Map<String, SLEOrgReportResponseBean> fetchCancelDataDayWisePerGameForAgent(
			SLEOrgReportRequestBean requestBean, Connection connection)
			throws GenericException {
		Map<String, SLEOrgReportResponseBean> responseBeanMap = null;
		SLEOrgReportResponseBean responseBean = null;
		Statement stmt = null;
		ResultSet rs = null;
		StringBuilder cancelQueryBuilder = null;
		try {
			responseBeanMap = new HashMap<String, SLEOrgReportResponseBean>();

			// String walletAppender = (requestBean.getWalletId() == 0) ? ""
			// : " AND wallet_id=" + requestBean.getWalletId();

			String archiveAppender = " union all select sgr.finaldate as alldate,sum(sgr.ref_net_amt) as cancel from st_rep_sle_retailer as sgr left outer join  st_rep_sle_agent as sga on sgr.finaldate=sga.finaldate and sgr.parent_id=sga.parent_id where sgr.parent_id="
					+ requestBean.getOrgId()
					+ " AND sgr.game_id="
					+ requestBean.getGameId()
					+ " AND sgr.finaldate>=date('"
					+ requestBean.getFromDate()
					+ "')"
					+ " AND sgr.finaldate<=date('"
					+ requestBean.getToDate()
					+ "')" + " group by sgr.finaldate";

			cancelQueryBuilder = new StringBuilder();
			cancelQueryBuilder
					.append(
							"select main.alldate,sum(main.cancel)as cancel from (select cancel.alldate,ifnull(cancel,0.0) cancel from(select alldate,cancel1.cancel from tempdate left outer join (select organization_id,cancel.tx_date,cancel.cancel from st_lms_organization_master om left outer join (select date(rtm.transaction_date) tx_date,sum(retailer_net_amt) cancel, rtm.retailer_org_id from st_sle_ret_sale_refund srs inner join st_lms_retailer_transaction_master rtm on srs.transaction_id=rtm.transaction_id where srs.game_id=")
					.append(requestBean.getGameId())
					.append(" AND rtm.transaction_date>='")
					.append(requestBean.getFromDate())
					.append("'")
					.append(" AND rtm.transaction_date<='")
					.append(requestBean.getToDate())
					.append("'")
					.append(
							" and transaction_type in('SLE_REFUND_CANCEL','SLE_REFUND_FAILED') group by tx_date,retailer_org_id) cancel on retailer_org_id=om.organization_id where parent_id=")
					.append(requestBean.getOrgId())
					.append(
							" group by cancel.tx_date,organization_id) cancel1 on alldate=cancel1.tx_date) cancel")
					.append(archiveAppender).append(
							") as main group by main.alldate");

			// logger.debug("fetchCancelDataDayWisePerGameSingleAgent - "
			// + cancelQueryBuilder.toString());

			System.out
					.println("fetchCancelDataDayWisePerGameSingleAgentViewByAgent - "
							+ cancelQueryBuilder.toString());

			stmt = connection.createStatement();
			rs = stmt.executeQuery(cancelQueryBuilder.toString());
			while (rs.next()) {
				responseBean = new SLEOrgReportResponseBean();
				responseBean.setCancelAmt(rs.getDouble("cancel"));
				responseBeanMap.put(rs.getString("alldate").split(" ")[0], responseBean);
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
	 * 
	 * @param requestBean
	 * @param connection
	 * @return
	 * @throws GenericException
	 */
	public static void fetchCancelDataAllGameSingleAgent(
			SLEOrgReportRequestBean requestBean, SLEOrgReportResponseBean responseBean, Connection connection)
			throws GenericException {
		Statement stmt = null;
		ResultSet rs = null;
		StringBuilder saleQueryBuilder = null;
		try {
			String archiveAppender = " UNION ALL SELECT organization_id,IFNULL(SUM(ref_net_amt),0.0) AS cancel FROM st_rep_sle_agent WHERE organization_id="
					+ requestBean.getOrgId()
					+ " AND finaldate>=DATE('"
					+ requestBean.getFromDate()
					+ "')"
					+ " AND finaldate<=DATE('"
					+ requestBean.getToDate()
					+ "')";

			saleQueryBuilder = new StringBuilder();
			saleQueryBuilder
					.append(
							"SELECT parent_id,sum(main.cancel) as cancel from (select cancel.parent_id,sum(cancel) cancel from (select organization_id,parent_id,ifnull(cancel,0.0) cancel from st_lms_organization_master om left outer join (select retailer_org_id,sum(agent_net_amt) as cancel from st_sle_ret_sale_refund WHERE transaction_date>='")
					.append(requestBean.getFromDate())
					.append("'")
					.append(" AND transaction_date<='")
					.append(requestBean.getToDate())
					.append("'")
					.append(" group by retailer_org_id) cancel on om.organization_id=retailer_org_id where om.organization_type='RETAILER' and om.parent_id=")
					.append(requestBean.getOrgId())
					.append(") cancel")
					.append(archiveAppender).append(
							") as main group by main.parent_id");

			// logger.debug("fetchDepositDataMultipleAgent - "
			// + saleQueryBuilder.toString());

			System.out.println("fetchCancelDataAllGameSingleAgent - "+saleQueryBuilder.toString());
			
			stmt = connection.createStatement();
			rs = stmt.executeQuery(saleQueryBuilder.toString());
			while (rs.next()) {
				responseBean.setCancelAmt(responseBean.getCancelAmt() + rs.getDouble("cancel"));
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

	}

	/**
	 * 
	 * @param requestBean
	 * @param connection
	 * @return
	 * @throws GenericException
	 */
	public static Map<String, SLEOrgReportResponseBean> fetchCancelDataDayWiseAllGameForAgent(
			SLEOrgReportRequestBean requestBean, Connection connection)
			throws GenericException {
		Map<String, SLEOrgReportResponseBean> responseBeanMap = null;
		SLEOrgReportResponseBean responseBean = null;
		Statement stmt = null;
		ResultSet rs = null;
		StringBuilder cancelQueryBuilder = null;
		try {
			responseBeanMap = new HashMap<String, SLEOrgReportResponseBean>();

			// String walletAppender = (requestBean.getWalletId() == 0) ? ""
			// : " AND wallet_id=" + requestBean.getWalletId();

			String archiveAppender = " union all select sgr.finaldate as alldate,sum(sgr.ref_net_amt) as cancel from st_rep_sle_retailer as sgr left outer join  st_rep_sle_agent as sga on sgr.finaldate=sga.finaldate and sgr.parent_id=sga.parent_id where sgr.parent_id="
					+ requestBean.getOrgId()
					// + " AND sgr.game_id="
					// + requestBean.getGameId()
					+ " AND sgr.finaldate>=date('"
					+ requestBean.getFromDate()
					+ "')"
					+ " AND sgr.finaldate<=date('"
					+ requestBean.getToDate()
					+ "')"
					+ " group by sgr.finaldate";

			cancelQueryBuilder = new StringBuilder();
			cancelQueryBuilder
					.append(
							"select main.alldate,sum(main.cancel)as cancel from (select cancel.alldate,ifnull(cancel,0.0) cancel from(select alldate,cancel1.cancel from tempdate left outer join (select organization_id,cancel.tx_date,cancel.cancel from st_lms_organization_master om left outer join (select date(rtm.transaction_date) tx_date,sum(agent_net_amt) cancel, rtm.retailer_org_id from st_sle_ret_sale_refund srs inner join st_lms_retailer_transaction_master rtm on srs.transaction_id=rtm.transaction_id where rtm.transaction_date>='")
					// .append(requestBean.getGameId())
					// .append(
					// " srs inner join st_lms_retailer_transaction_master rtm on srs.transaction_id=rtm.transaction_id where rtm.transaction_date>='")
					.append(requestBean.getFromDate())
					.append("'")
					.append(" and rtm.transaction_date<='")
					.append(requestBean.getToDate())
					.append("'")
					.append(
							" and transaction_type in('SLE_REFUND_CANCEL','SLE_REFUND_FAILED')) cancel on retailer_org_id=om.organization_id where parent_id=")
					.append(requestBean.getOrgId())
					.append(
							" group by cancel.tx_date) cancel1 on alldate=cancel1.tx_date) cancel")
					.append(archiveAppender).append(
							") as main group by main.alldate");

			// logger.debug("fetchCancelDataDayWisePerGameSingleAgent - "
			// + cancelQueryBuilder.toString());

			System.out
					.println("fetchCancelDataDayWisePerGameSingleAgentViewByAgent - "
							+ cancelQueryBuilder.toString());

			stmt = connection.createStatement();
			rs = stmt.executeQuery(cancelQueryBuilder.toString());
			while (rs.next()) {
				responseBean = new SLEOrgReportResponseBean();
				responseBean.setCancelAmt(rs.getDouble("cancel"));
				responseBeanMap.put(rs.getString("alldate"), responseBean);
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
	 * For Payment-Ledger Report view by BO
	 * 
	 * @param requestBean
	 * @param connection
	 * @return
	 * @throws GenericException
	 */
	public static Map<String, SLEOrgReportResponseBean> fetchCancelDataDayWiseAllGameSingleAgent(
			SLEOrgReportRequestBean requestBean, Connection connection)
			throws GenericException {
		Map<String, SLEOrgReportResponseBean> responseBeanMap = null;
		SLEOrgReportResponseBean responseBean = null;
		Statement stmt = null;
		ResultSet rs = null;
		StringBuilder saleQueryBuilder = null;
		try {
			responseBeanMap = new HashMap<String, SLEOrgReportResponseBean>();

			String archiveAppender = " UNION ALL SELECT finaldate,organization_id,SUM(ref_net_amt) AS cancel FROM st_rep_sle_agent WHERE finaldate>='"
					+ requestBean.getFromDate()
					+ "' AND finaldate<='"
					+ requestBean.getToDate()
					+ "'AND organization_id="
					+ requestBean.getOrgId()
					+ " GROUP BY finaldate";

			saleQueryBuilder = new StringBuilder();
			saleQueryBuilder
					.append("SELECT DATE(transaction_date) DATE,parent_id,SUM(cancel) AS cancel FROM (SELECT retailer_org_id,SUM(agent_net_amt) AS cancel,transaction_date FROM st_sle_ret_sale_refund WHERE transaction_date>='")
					.append(requestBean.getFromDate())
					.append("' AND transaction_date<='")
					.append(requestBean.getToDate())
					.append("' AND retailer_org_id IN (SELECT organization_id FROM st_lms_organization_master WHERE organization_type='RETAILER') GROUP BY DATE(transaction_date),retailer_org_id)aa,st_lms_organization_master bb WHERE retailer_org_id=organization_id AND parent_id=")
					.append(requestBean.getOrgId())
					.append(" GROUP BY DATE(transaction_date)")
					.append(archiveAppender);

			// logger.debug("fetchDepositDataMultipleAgent - "
			// + saleQueryBuilder.toString());

			System.out.println("fetchCancelDataDayWiseAllGameSingleAgent - "+ saleQueryBuilder.toString());

			stmt = connection.createStatement();
			rs = stmt.executeQuery(saleQueryBuilder.toString());
			while (rs.next()) {
				responseBean = new SLEOrgReportResponseBean();
				responseBean.setCancelAmt(rs.getDouble("cancel"));
				responseBeanMap.put(rs.getString("date"), responseBean);
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
	 * 
	 * @param requestBean
	 * @param connection
	 * @return
	 * @throws GenericException
	 */
	public static Map<Integer, SLEOrgReportResponseBean> fetchPWTDataGameWiseMultipleAgent(
			SLEOrgReportRequestBean requestBean, Connection connection)
			throws GenericException {
		Map<Integer, SLEOrgReportResponseBean> responseBeanMap = null;
		SLEOrgReportResponseBean responseBean = null;
		Statement stmt = null;
		ResultSet rs = null;
		StringBuilder pwtQueryBuilder = null;
		try {
			responseBeanMap = new HashMap<Integer, SLEOrgReportResponseBean>();

			String archiveAppender = " UNION ALL select organization_id,sum(pwt_net_amt) as pwt from st_rep_sle_agent where game_id="
					+ requestBean.getGameId()
					+ " AND finaldate>=date('"
					+ requestBean.getFromDate()
					+ "')"
					+ " AND finaldate<=date('"
					+ requestBean.getToDate()
					+ "')"
					+ " group by organization_id";

			pwtQueryBuilder = new StringBuilder();
			pwtQueryBuilder
					.append(
							"select parent_id,sum(main.pwt) as pwt from (select pwt.parent_id,sum(pwt) pwt from (select organization_id,parent_id,ifnull(sum(pwt),0.0) pwt from st_lms_organization_master om left outer join (select drs.retailer_org_id,sum(pwt_amt+agt_claim_comm-govt_claim_comm) as pwt from st_sle_ret_pwt drs inner join st_lms_retailer_transaction_master rtm on drs.transaction_id=rtm.transaction_id where drs.game_id=")
					.append(requestBean.getGameId())
					.append(
							" AND rtm.transaction_type in('SLE_PWT_AUTO','SLE_PWT_PLR','SLE_PWT') and rtm.transaction_date>='")
					.append(requestBean.getFromDate())
					.append("'")
					.append(" and rtm.transaction_date<='")
					.append(requestBean.getToDate())
					.append("'")
					.append(
							" group by drs.retailer_org_id) pwt on om.organization_id=retailer_org_id where om.organization_type='RETAILER' group by parent_id ) pwt group by parent_id")
					.append(archiveAppender).append(
							") as main group by main.parent_id");
			// logger.debug("fetchPWTDataMultipleAgent - "
			// + saleQueryBuilder.toString());

			System.out.println("fetchPWTDataMultipleAgent - "
					+ pwtQueryBuilder.toString());

			stmt = connection.createStatement();
			rs = stmt.executeQuery(pwtQueryBuilder.toString());
			while (rs.next()) {
				responseBean = new SLEOrgReportResponseBean();
				responseBean.setPwtAmt(rs.getDouble("pwt"));
				responseBeanMap.put(rs.getInt("parent_id"), responseBean);
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
	 * 
	 * @param requestBean
	 * @param connection
	 * @return
	 * @throws GenericException
	 */
	public static Map<Integer, SLEOrgReportResponseBean> fetchPWTDataAllGameMultipleAgent(
			SLEOrgReportRequestBean requestBean, Connection connection)
			throws GenericException {
		Map<Integer, SLEOrgReportResponseBean> responseBeanMap = null;
		SLEOrgReportResponseBean responseBean = null;
		Statement stmt = null;
		ResultSet rs = null;
		StringBuilder pwtQueryBuilder = null;
		try {
			responseBeanMap = new HashMap<Integer, SLEOrgReportResponseBean>();

			String archiveAppender = " UNION ALL select organization_id,sum(pwt_net_amt) as pwt from st_rep_sle_agent where organization_id in (select organization_id from st_lms_user_master where termination_date>'"+requestBean.getFromDate()+"' or registration_date <'"+requestBean.getToDate()+"' and isrolehead='Y' and organization_type='AGENT' ) and  finaldate>=date('"
					// + requestBean.getGameId()
					// + " AND finaldate>=date('"
					+ requestBean.getFromDate()
					+ "')"
					+ " AND finaldate<=date('"
					+ requestBean.getToDate()
					+ "')"
					+ " group by organization_id";

			pwtQueryBuilder = new StringBuilder();
			pwtQueryBuilder
					.append(
							"select parent_id,sum(main.pwt) as pwt from (select pwt.parent_id,sum(pwt) pwt from (select organization_id,parent_id,ifnull(sum(pwt),0.0) pwt from st_lms_organization_master om left outer join (select drs.retailer_org_id,sum(pwt_amt+agt_claim_comm-govt_claim_comm) as pwt from st_sle_ret_pwt drs inner join st_lms_retailer_transaction_master rtm on drs.transaction_id=rtm.transaction_id where  rtm.retailer_org_id in (select organization_id from st_lms_user_master where parent_user_id not in (select user_id from st_lms_user_master where termination_date<'"+requestBean.getFromDate()+"' or registration_date >'"+requestBean.getToDate()+"' and isrolehead='Y' and organization_type='AGENT' ) AND  organization_type='RETAILER' ) and  rtm.transaction_type in('SLE_PWT_AUTO','SLE_PWT_PLR','SLE_PWT') and rtm.transaction_date>='")
					// .append(requestBean.getGameId())
					// .append(
					// " drs inner join st_lms_retailer_transaction_master rtm on drs.transaction_id=rtm.transaction_id where rtm.transaction_type in('SLE_PWT_AUTO','SLE_PWT_PLR','SLE_PWT') and rtm.transaction_date>='")
					.append(requestBean.getFromDate())
					.append("'")
					.append(" and rtm.transaction_date<='")
					.append(requestBean.getToDate())
					.append("'")
					.append(
							" group by drs.retailer_org_id) pwt on om.organization_id=retailer_org_id where om.organization_type='RETAILER' group by parent_id ) pwt group by parent_id")
					.append(archiveAppender).append(
							") as main group by main.parent_id");
			// logger.debug("fetchPWTDataMultipleAgent - "
			// + saleQueryBuilder.toString());

			System.out.println("fetchPWTDataMultipleAgent - "
					+ pwtQueryBuilder.toString());

			stmt = connection.createStatement();
			rs = stmt.executeQuery(pwtQueryBuilder.toString());
			while (rs.next()) {
				responseBean = new SLEOrgReportResponseBean();
				responseBean.setPwtAmt(rs.getDouble("pwt"));
				responseBeanMap.put(rs.getInt("parent_id"), responseBean);
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
	 * 
	 * @param requestBean
	 * @param connection
	 * @return
	 * @throws GenericException
	 */
	public static Map<String, SLEOrgReportResponseBean> fetchPWTDataDayWisePerGameForBO(
			SLEOrgReportRequestBean requestBean, Connection connection)
			throws GenericException {
		Map<String, SLEOrgReportResponseBean> responseBeanMap = null;
		SLEOrgReportResponseBean responseBean = null;
		Statement stmt = null;
		ResultSet rs = null;
		StringBuilder pwtQueryBuilder = null;
		try {
			responseBeanMap = new HashMap<String, SLEOrgReportResponseBean>();

			// String walletAppender = (requestBean.getWalletId() == 0) ? ""
			// : " AND wallet_id=" + requestBean.getWalletId();

			String archiveAppender = " union all select finaldate as alldate, SUM(pwt_net_amt) as pwt from st_rep_sle_agent where organization_id in (select organization_id from st_lms_user_master  where (termination_date > '"+requestBean.getFromDate()+"' or registration_date< '"+requestBean.getToDate()+"') and isrolehead='Y' and organization_type='AGENT') and  game_id="
					+ requestBean.getGameId()
					+ " AND finaldate>=date('"
					+ requestBean.getFromDate()
					+ "')"
					+ " AND finaldate<=date('"
					+ requestBean.getToDate()
					+ "')"
					+ " GROUP BY alldate";

			pwtQueryBuilder = new StringBuilder();
			pwtQueryBuilder
					.append(
							"select main.alldate,sum(main.pwt)as pwt from (select pwt.alldate,ifnull(pwt,0.0) pwt from(select alldate,pwt from tempdate left outer join (select date(rtm.transaction_date) tx_date,sum(pwt_amt+agt_claim_comm - govt_claim_comm) pwt from st_sle_ret_pwt drs inner join st_lms_retailer_transaction_master rtm on drs.transaction_id=rtm.transaction_id where rtm.retailer_org_id in (select organization_id from st_lms_user_master where parent_user_id not in (select user_id from st_lms_user_master  where (termination_date < '"+requestBean.getFromDate()+"' or registration_date> '"+requestBean.getToDate()+"') and isrolehead='Y' and organization_type='AGENT') and organization_type='RETAILER' ) and drs.game_id=")
					.append(requestBean.getGameId())
					.append(" AND rtm.transaction_date>='")
					.append(requestBean.getFromDate())
					.append("'")
					.append(" AND rtm.transaction_date<='")
					.append(requestBean.getToDate())
					.append("'")
					.append(
							" and transaction_type in('SLE_PWT_AUTO','SLE_PWT_PLR','SLE_PWT') group by tx_date) pwt on alldate=pwt.tx_date) pwt")
					.append(archiveAppender).append(
							") as main group by main.alldate");

			// logger.debug("fetchPWTDataDayWisePerGameSingleAgent - "
			// + cancelQueryBuilder.toString());

			System.out.println("fetchPWTDataDayWisePerGameSingleAgent - "
					+ pwtQueryBuilder.toString());

			stmt = connection.createStatement();
			rs = stmt.executeQuery(pwtQueryBuilder.toString());
			while (rs.next()) {
				responseBean = new SLEOrgReportResponseBean();
				responseBean.setPwtAmt(rs.getDouble("pwt"));
				responseBeanMap.put(rs.getDate("alldate").toString(), responseBean);
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
	 * 
	 * @param requestBean
	 * @param connection
	 * @return
	 * @throws GenericException
	 */
	public static Map<String, SLEOrgReportResponseBean> fetchPWTDataDayWiseAllGameForBO(
			SLEOrgReportRequestBean requestBean, Connection connection)
			throws GenericException {
		Map<String, SLEOrgReportResponseBean> responseBeanMap = null;
		SLEOrgReportResponseBean responseBean = null;
		Statement stmt = null;
		ResultSet rs = null;
		StringBuilder pwtQueryBuilder = null;
		try {
			responseBeanMap = new HashMap<String, SLEOrgReportResponseBean>();

			// String walletAppender = (requestBean.getWalletId() == 0) ? ""
			// : " AND wallet_id=" + requestBean.getWalletId();

			String archiveAppender = " union all select finaldate as alldate, SUM(pwt_net_amt) as pwt from st_rep_sle_agent where finaldate>=date('"
					// + requestBean.getGameId()
					// + " AND finaldate>=date('"
					+ requestBean.getFromDate()
					+ "')"
					+ " AND finaldate<=date('"
					+ requestBean.getToDate()
					+ "')"
					+ " GROUP BY organization_id";

			pwtQueryBuilder = new StringBuilder();
			pwtQueryBuilder
					.append(
							"select main.alldate,sum(main.pwt)as pwt from (select pwt.alldate,ifnull(pwt,0.0) pwt from(select alldate,pwt from tempdate left outer join (select date(rtm.transaction_date) tx_date,sum(pwt_amt+agt_claim_comm-govt_claim_comm) pwt from st_sle_ret_pwt drs inner join st_lms_retailer_transaction_master rtm on drs.transaction_id=rtm.transaction_id where rtm.transaction_date>='")
					// .append(requestBean.getGameId())
					// .append(
					// " drs inner join st_lms_retailer_transaction_master rtm on drs.transaction_id=rtm.transaction_id where rtm.transaction_date>='")
					.append(requestBean.getFromDate())
					.append("'")
					.append(" and rtm.transaction_date<='")
					.append(requestBean.getToDate())
					.append("'")
					.append(
							" and transaction_type in('SLE_PWT_AUTO','SLE_PWT_PLR','SLE_PWT') group by tx_date) pwt on alldate=pwt.tx_date) pwt")
					.append(archiveAppender).append(
							") as main group by main.alldate");

			// logger.debug("fetchPWTDataDayWisePerGameSingleAgent - "
			// + cancelQueryBuilder.toString());

			System.out.println("fetchPWTDataDayWisePerGameSingleAgent - "
					+ pwtQueryBuilder.toString());

			stmt = connection.createStatement();
			rs = stmt.executeQuery(pwtQueryBuilder.toString());
			while (rs.next()) {
				responseBean = new SLEOrgReportResponseBean();
				responseBean.setPwtAmt(rs.getDouble("pwt"));
				responseBeanMap.put(rs.getString("alldate"), responseBean);
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
	 * 
	 * @param requestBean
	 * @param connection
	 * @return
	 * @throws GenericException
	 */
	public static Map<String, SLEOrgReportResponseBean> fetchPWTDataDayWisePerGameForAgent(
			SLEOrgReportRequestBean requestBean, Connection connection)
			throws GenericException {
		Map<String, SLEOrgReportResponseBean> responseBeanMap = null;
		SLEOrgReportResponseBean responseBean = null;
		Statement stmt = null;
		ResultSet rs = null;
		StringBuilder pwtQueryBuilder = null;
		try {
			responseBeanMap = new HashMap<String, SLEOrgReportResponseBean>();

			// String walletAppender = (requestBean.getWalletId() == 0) ? ""
			// : " AND wallet_id=" + requestBean.getWalletId();

			String archiveAppender = " union all select sgr.finaldate as alldate,sum(sgr.pwt_net_amt) as pwt from st_rep_sle_retailer as sgr left outer join  st_rep_sle_agent as sga on sgr.finaldate=sga.finaldate and sgr.parent_id=sga.parent_id where sgr.parent_id="
					+ requestBean.getOrgId()
					+ " AND sgr.game_id="
					+ requestBean.getGameId()
					+ " AND sgr.finaldate>=date('"
					+ requestBean.getFromDate()
					+ "')"
					+ " AND sgr.finaldate<=date('"
					+ requestBean.getToDate()
					+ "')" + " GROUP BY sgr.finaldate";

			pwtQueryBuilder = new StringBuilder();
			pwtQueryBuilder
					.append(
							"select main.alldate,sum(main.pwt)as pwt from (select pwt.alldate,ifnull(pwt,0.0) pwt from(select alldate,pwt1.pwt from tempdate left outer join (select organization_id,pwt.tx_date,pwt.pwt from st_lms_organization_master om left outer join (select date(rtm.transaction_date) tx_date,sum(pwt_amt+retailer_claim_comm-govt_claim_comm) pwt, rtm.retailer_org_id from st_sle_ret_pwt srs inner join st_lms_retailer_transaction_master rtm on srs.transaction_id=rtm.transaction_id where srs.game_id=")
					.append(requestBean.getGameId())
					.append(" AND rtm.transaction_date>='")
					.append(requestBean.getFromDate())
					.append("'")
					.append(" AND rtm.transaction_date<='")
					.append(requestBean.getToDate())
					.append("'")
					.append(
							" and transaction_type in('SLE_PWT_AUTO','SLE_PWT_PLR','SLE_PWT') group by tx_date,retailer_org_id) pwt on retailer_org_id=om.organization_id where parent_id=")
					.append(requestBean.getOrgId())
					.append(
							" group by pwt.tx_date, organization_id) pwt1 on alldate=pwt1.tx_date) pwt")
					.append(archiveAppender).append(
							") as main group by main.alldate");

			// logger.debug("fetchPWTDataDayWisePerGameSingleAgent - "
			// + cancelQueryBuilder.toString());

			System.out.println("fetchPWTDataDayWisePerGameForAgent - "
					+ pwtQueryBuilder.toString());

			stmt = connection.createStatement();
			rs = stmt.executeQuery(pwtQueryBuilder.toString());
			while (rs.next()) {
				responseBean = new SLEOrgReportResponseBean();
				responseBean.setPwtAmt(rs.getDouble("pwt"));
				responseBeanMap.put(rs.getString("alldate").split(" ")[0], responseBean);
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
	 * 
	 * @param requestBean
	 * @param connection
	 * @return
	 * @throws GenericException
	 */
	public static Map<String, SLEOrgReportResponseBean> fetchPWTDataDayWiseAllGameForAgent(
			SLEOrgReportRequestBean requestBean, Connection connection)
			throws GenericException {
		Map<String, SLEOrgReportResponseBean> responseBeanMap = null;
		SLEOrgReportResponseBean responseBean = null;
		Statement stmt = null;
		ResultSet rs = null;
		StringBuilder pwtQueryBuilder = null;
		try {
			responseBeanMap = new HashMap<String, SLEOrgReportResponseBean>();

			// String walletAppender = (requestBean.getWalletId() == 0) ? ""
			// : " AND wallet_id=" + requestBean.getWalletId();

			String archiveAppender = " union all select sgr.finaldate as alldate,sum(sgr.pwt_net_amt) as pwt from st_rep_sle_retailer as sgr left outer join  st_rep_sle_agent as sga on sgr.finaldate=sga.finaldate and sgr.parent_id=sga.parent_id where sgr.parent_id="
					+ requestBean.getOrgId()
					// + " AND sgr.game_id="
					// + requestBean.getGameId()
					+ " AND sgr.finaldate>=date('"
					+ requestBean.getFromDate()
					+ "')"
					+ " AND sgr.finaldate<=date('"
					+ requestBean.getToDate()
					+ "')"
					+ " GROUP BY sgr.finaldate";

			pwtQueryBuilder = new StringBuilder();
			pwtQueryBuilder
					.append(
							"select main.alldate,sum(main.pwt)as pwt from (select pwt.alldate,ifnull(pwt,0.0) pwt from(select alldate,pwt1.pwt from tempdate left outer join (select organization_id,pwt.tx_date,pwt.pwt from st_lms_organization_master om left outer join (select date(rtm.transaction_date) tx_date,sum(pwt_amt+retailer_claim_comm) pwt, rtm.retailer_org_id from st_sle_ret_pwt srs inner join st_lms_retailer_transaction_master rtm on srs.transaction_id=rtm.transaction_id where rtm.transaction_date>='")
					// .append(requestBean.getGameId())
					// .append(
					// " srs inner join st_lms_retailer_transaction_master rtm on srs.transaction_id=rtm.transaction_id where rtm.transaction_date>='")
					.append(requestBean.getFromDate())
					.append("'")
					.append(" and rtm.transaction_date<='")
					.append(requestBean.getToDate())
					.append("'")
					.append(
							" and transaction_type in('SLE_PWT_AUTO','SLE_PWT_PLR','SLE_PWT')) pwt on retailer_org_id=om.organization_id where parent_id=")
					.append(requestBean.getOrgId())
					.append(
							" group by pwt.tx_date) pwt1 on alldate=pwt1.tx_date) pwt")
					.append(archiveAppender).append(
							") as main group by main.alldate");

			// logger.debug("fetchPWTDataDayWisePerGameSingleAgent - "
			// + cancelQueryBuilder.toString());

			System.out.println("fetchPWTDataDayWisePerGameForAgent - "
					+ pwtQueryBuilder.toString());

			stmt = connection.createStatement();
			rs = stmt.executeQuery(pwtQueryBuilder.toString());
			while (rs.next()) {
				responseBean = new SLEOrgReportResponseBean();
				responseBean.setPwtAmt(rs.getDouble("pwt"));
				responseBeanMap.put(rs.getString("alldate"), responseBean);
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
	 * For Payment-Ledger Report view by BO
	 * 
	 * @param requestBean
	 * @param connection
	 * @return
	 * @throws GenericException
	 */
	public static Map<String, SLEOrgReportResponseBean> fetchPWTDataDayWiseAllGameSingleAgent(
			SLEOrgReportRequestBean requestBean, Connection connection)
			throws GenericException {
		Map<String, SLEOrgReportResponseBean> responseBeanMap = null;
		SLEOrgReportResponseBean responseBean = null;
		Statement stmt = null;
		ResultSet rs = null;
		StringBuilder saleQueryBuilder = null;
		try {
			responseBeanMap = new HashMap<String, SLEOrgReportResponseBean>();

			String archiveAppender = " UNION ALL SELECT finaldate,organization_id,SUM(pwt_net_amt) AS pwt FROM st_rep_sle_agent WHERE finaldate>='"
					+ requestBean.getFromDate()
					+ "' AND finaldate<='"
					+ requestBean.getToDate()
					+ "'AND organization_id="
					+ requestBean.getOrgId()
					+ " GROUP BY finaldate";

			saleQueryBuilder = new StringBuilder();
			saleQueryBuilder
					.append("SELECT DATE(transaction_date) DATE,parent_id,SUM(pwt) AS pwt FROM (SELECT retailer_org_id,SUM(pwt_amt+agt_claim_comm- govt_claim_comm) AS pwt,transaction_date FROM st_sle_ret_pwt WHERE transaction_date>='")
					.append(requestBean.getFromDate())
					.append("' AND transaction_date<='")
					.append(requestBean.getToDate())
					.append("' AND retailer_org_id IN (SELECT organization_id FROM st_lms_organization_master WHERE organization_type='RETAILER') GROUP BY DATE(transaction_date),retailer_org_id)aa,st_lms_organization_master bb WHERE retailer_org_id=organization_id AND parent_id=")
					.append(requestBean.getOrgId())
					.append(" GROUP BY DATE(transaction_date)")
					.append(archiveAppender);

			// logger.debug("fetchDepositDataMultipleAgent - "
			// + saleQueryBuilder.toString());

			System.out.println("fetchPWTDataDayWiseAllGameSingleAgent - "+ saleQueryBuilder.toString());

			stmt = connection.createStatement();
			rs = stmt.executeQuery(saleQueryBuilder.toString());
			while (rs.next()) {
				responseBean = new SLEOrgReportResponseBean();
				responseBean.setPwtAmt(rs.getDouble("pwt"));
				responseBeanMap.put(rs.getString("date"), responseBean);
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
	 * 
	 * @param requestBean
	 * @param connection
	 * @return
	 * @throws GenericException
	 */
	public static Map<Integer, SLEOrgReportResponseBean> fetchDirPlyPWTDataGameWiseMultipleAgent(
			SLEOrgReportRequestBean requestBean, Connection connection)
			throws GenericException {
		Map<Integer, SLEOrgReportResponseBean> responseBeanMap = null;
		SLEOrgReportResponseBean responseBean = null;
		Statement stmt = null;
		ResultSet rs = null;
		StringBuilder pwtDirQueryBuilder = null;
		try {
			responseBeanMap = new HashMap<Integer, SLEOrgReportResponseBean>();

			String archiveAppender = " UNION ALL select organization_id,sum(direct_pwt_net_amt) as pwtDir from st_rep_sle_agent where organization_id in (select organization_id from st_lms_user_master where termination_date>'"+requestBean.getFromDate()+"' or registration_date <'"+requestBean.getToDate()+"' and isrolehead='Y' and organization_type='AGENT' )"
					//+ requestBean.getGameId()
					+ " AND finaldate>=date('"
					+ requestBean.getFromDate()
					+ "')"
					+ " AND finaldate<=date('"
					+ requestBean.getToDate()
					+ "')"
					+ " group by organization_id";

			pwtDirQueryBuilder = new StringBuilder();
			pwtDirQueryBuilder
					.append(
							"select parent_id,sum(main.pwtDir) as pwtDir from (select organization_id as parent_id,ifnull(sum(pwt_amt+agt_claim_comm-tax_amt),0.0) pwtDir from st_lms_organization_master left outer join st_sle_agent_direct_plr_pwt on organization_id=agent_org_id where agent_org_id in (select organization_id from st_lms_user_master where termination_date>'"+requestBean.getFromDate()+"' or registration_date <'"+requestBean.getToDate()+"' and isrolehead='Y' and organization_type='AGENT' ) and  transaction_date>='")
					.append(requestBean.getFromDate()).append("'").append(
							" and transaction_date<='").append(
							requestBean.getToDate()).append("'").append(
							" and organization_type='AGENT'")
					//.append(requestBean.getGameId())
							.append(
							" group by agent_org_id").append(archiveAppender)
					.append(")as main group by main.parent_id");
			// logger.debug("fetchDepositDataMultipleAgent - "
			// + saleQueryBuilder.toString());

			System.out.println("fetchPWTDirDataMultipleAgent - "
					+ pwtDirQueryBuilder.toString());

			stmt = connection.createStatement();
			rs = stmt.executeQuery(pwtDirQueryBuilder.toString());
			while (rs.next()) {
				responseBean = new SLEOrgReportResponseBean();
				responseBean.setSaleAmt(rs.getDouble("pwtDir"));
				responseBeanMap.put(rs.getInt("parent_id"), responseBean);
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
	 * 
	 * @param requestBean
	 * @param connection
	 * @return
	 * @throws GenericException
	 */
	public static Map<Integer, SLEOrgReportResponseBean> fetchDirPlyPWTDataAllGameMultipleAgent(
			SLEOrgReportRequestBean requestBean, Connection connection)
			throws GenericException {
		Map<Integer, SLEOrgReportResponseBean> responseBeanMap = null;
		SLEOrgReportResponseBean responseBean = null;
		Statement stmt = null;
		ResultSet rs = null;
		StringBuilder pwtDirQueryBuilder = null;
		try {
			responseBeanMap = new HashMap<Integer, SLEOrgReportResponseBean>();

			String archiveAppender = " UNION ALL select organization_id,sum(direct_pwt_net_amt) as pwtDir from st_rep_sle_agent where finaldate>=date('"
					// + requestBean.getGameId()
					// + " AND finaldate>=date('"
					+ requestBean.getFromDate()
					+ "')"
					+ " AND finaldate<=date('"
					+ requestBean.getToDate()
					+ "')"
					+ " group by organization_id";

			pwtDirQueryBuilder = new StringBuilder();
			pwtDirQueryBuilder
					.append(
							"select parent_id,sum(main.pwtDir) as pwtDir from (select organization_id as parent_id,ifnull(sum(pwt_amt+agt_claim_comm-tax_amt),0.0) pwtDir from st_lms_organization_master left outer join st_sle_agent_direct_plr_pwt on organization_id=agent_org_id where transaction_date>='")
					.append(requestBean.getFromDate()).append("'").append(
							" and transaction_date<='").append(
							requestBean.getToDate()).append("'").append(
					// " and organization_type='AGENT' and game_id=")
							// .append(requestBean.getGameId()).append(
							" and organization_type='AGENT'").append(
							" group by agent_org_id").append(archiveAppender)
					.append(")as main group by main.parent_id");
			// logger.debug("fetchDepositDataMultipleAgent - "
			// + saleQueryBuilder.toString());

			System.out.println("fetchPWTDirDataMultipleAgent - "
					+ pwtDirQueryBuilder.toString());

			stmt = connection.createStatement();
			rs = stmt.executeQuery(pwtDirQueryBuilder.toString());
			while (rs.next()) {
				responseBean = new SLEOrgReportResponseBean();
				responseBean.setSaleAmt(rs.getDouble("pwtDir"));
				responseBeanMap.put(rs.getInt("parent_id"), responseBean);
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
	 * 
	 * @param requestBean
	 * @param connection
	 * @return
	 * @throws GenericException
	 */
	public static Map<String, SLEOrgReportResponseBean> fetchDirPlyPWTDataDayWisePerGameForBO(
			SLEOrgReportRequestBean requestBean, Connection connection)
			throws GenericException {
		Map<String, SLEOrgReportResponseBean> responseBeanMap = null;
		SLEOrgReportResponseBean responseBean = null;
		Statement stmt = null;
		ResultSet rs = null;
		StringBuilder dirPWTQueryBuilder = null;
		try {
			responseBeanMap = new HashMap<String, SLEOrgReportResponseBean>();

			// String walletAppender = (requestBean.getWalletId() == 0) ? ""
			// : " AND wallet_id=" + requestBean.getWalletId();

			String archiveAppender = " union all select finaldate as alldate, SUM(direct_pwt_net_amt) as pwtDir from st_rep_sle_agent where organization_id in (select organization_id from st_lms_user_master  where (termination_date > '"+requestBean.getFromDate()+"' or registration_date< '"+requestBean.getToDate()+"') and isrolehead='Y' and organization_type='AGENT') and  game_id="
					+ requestBean.getGameId()
					+ " AND finaldate>=date('"
					+ requestBean.getFromDate()
					+ "')"
					+ " AND finaldate<=date('"
					+ requestBean.getToDate()
					+ "')"
					+ " GROUP BY organization_id";

			dirPWTQueryBuilder = new StringBuilder();
			dirPWTQueryBuilder
					.append(
							"select main.alldate,sum(main.pwtDir)as pwtDir from (select pwtDir.alldate,ifnull(pwtDir,0.0) pwtDir from(select alldate,pwtDir from tempdate left outer join (select date(transaction_date) tx_date ,sum(pwt_amt+agt_claim_comm - tax_amt) pwtDir from st_sle_agent_direct_plr_pwt where agent_org_id not in (select organization_id from st_lms_user_master where (termination_date > '"+requestBean.getFromDate()+"' or registration_date< '"+requestBean.getToDate()+"') and isrolehead='Y' and organization_type='AGENT' ) and transaction_date>='")
					.append(requestBean.getFromDate())
					.append("'")
					.append(" and transaction_date<='")
					.append(requestBean.getToDate())
					.append("'")
					.append(" and game_id=")
					.append(requestBean.getGameId())
					.append(
							" group by tx_date) pwtDir on alldate=pwtDir.tx_date) pwtDir")
					.append(archiveAppender).append(
							") as main group by main.alldate");

			// logger.debug("fetchPWTDataDayWisePerGameSingleAgent - "
			// + cancelQueryBuilder.toString());

			System.out.println("fetchPWTDataDayWisePerGameSingleAgent - "
					+ dirPWTQueryBuilder.toString());

			stmt = connection.createStatement();
			rs = stmt.executeQuery(dirPWTQueryBuilder.toString());
			while (rs.next()) {
				responseBean = new SLEOrgReportResponseBean();
				responseBean.setPwtDirAmt(rs.getDouble("pwtDir"));
				responseBeanMap.put(rs.getDate("alldate").toString(), responseBean);
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
	 * 
	 * @param requestBean
	 * @param connection
	 * @return
	 * @throws GenericException
	 */
	public static Map<String, SLEOrgReportResponseBean> fetchDirPlyPWTDataDayWiseAllGameForBO(
			SLEOrgReportRequestBean requestBean, Connection connection)
			throws GenericException {
		Map<String, SLEOrgReportResponseBean> responseBeanMap = null;
		SLEOrgReportResponseBean responseBean = null;
		Statement stmt = null;
		ResultSet rs = null;
		StringBuilder dirPWTQueryBuilder = null;
		try {
			responseBeanMap = new HashMap<String, SLEOrgReportResponseBean>();

			// String walletAppender = (requestBean.getWalletId() == 0) ? ""
			// : " AND wallet_id=" + requestBean.getWalletId();

			String archiveAppender = " union all select finaldate as alldate, SUM(direct_pwt_net_amt) as pwtDir from st_rep_sle_agent where game_id="
					+ requestBean.getGameId()
					+ " AND finaldate>=date('"
					+ requestBean.getFromDate()
					+ "')"
					+ " AND finaldate<=date('"
					+ requestBean.getToDate()
					+ "')"
					+ " GROUP BY organization_id";

			dirPWTQueryBuilder = new StringBuilder();
			dirPWTQueryBuilder
					.append(
							"select main.alldate,sum(main.pwtDir)as pwtDir from (select pwtDir.alldate,ifnull(pwtDir,0.0) pwtDir from(select alldate,pwtDir from tempdate left outer join (select date(transaction_date) tx_date ,sum(pwt_amt+agt_claim_comm) pwtDir from st_sle_agent_direct_plr_pwt where transaction_date>='")
					.append(requestBean.getFromDate())
					.append("'")
					.append(" and transaction_date<='")
					.append(requestBean.getToDate())
					.append("'")
					// .append(" and game_id=")
					// .append(requestBean.getGameId())
					.append(
							" group by tx_date) pwtDir on alldate=pwtDir.tx_date) pwtDir")
					.append(archiveAppender).append(
							") as main group by main.alldate");

			// logger.debug("fetchPWTDataDayWisePerGameSingleAgent - "
			// + cancelQueryBuilder.toString());

			System.out.println("fetchPWTDataDayWisePerGameSingleAgent - "
					+ dirPWTQueryBuilder.toString());

			stmt = connection.createStatement();
			rs = stmt.executeQuery(dirPWTQueryBuilder.toString());
			while (rs.next()) {
				responseBean = new SLEOrgReportResponseBean();
				responseBean.setPwtDirAmt(rs.getDouble("pwtDir"));
				responseBeanMap.put(rs.getString("alldate"), responseBean);
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
	 * 
	 * @param requestBean
	 * @param connection
	 * @return
	 * @throws GenericException
	 */
	public static Map<String, SLEOrgReportResponseBean> fetchDirPlyPWTDataDayWisePerGameForAgent(
			SLEOrgReportRequestBean requestBean, Connection connection)
			throws GenericException {
		Map<String, SLEOrgReportResponseBean> responseBeanMap = null;
		SLEOrgReportResponseBean responseBean = null;
		Statement stmt = null;
		ResultSet rs = null;
		StringBuilder dirPWTQueryBuilder = null;
		try {
			responseBeanMap = new HashMap<String, SLEOrgReportResponseBean>();

			// String walletAppender = (requestBean.getWalletId() == 0) ? ""
			// : " AND wallet_id=" + requestBean.getWalletId();

			String archiveAppender = " union all select sgr.finaldate as alldate,sum(ifnull(sga.direct_pwt_net_amt,0.00))as pwtDir from st_rep_sle_retailer as sgr left outer join  st_rep_sle_agent as sga on sgr.finaldate=sga.finaldate and sgr.parent_id=sga.parent_id where sgr.parent_id="
					+ requestBean.getOrgId()
					+ " AND sgr.game_id="
					+ requestBean.getGameId()
					+ " AND sgr.finaldate>=date('"
					+ requestBean.getFromDate()
					+ "')"
					+ " AND sgr.finaldate<=date('"
					+ requestBean.getToDate()
					+ "')" + " GROUP BY sgr.finaldate";

			dirPWTQueryBuilder = new StringBuilder();
			dirPWTQueryBuilder
					.append(
							"select main.alldate,sum(main.pwtDir)as pwtDir from (select pwtDir.alldate,ifnull(pwtDir,0.0) pwtDir from(select alldate,pwtDir from tempdate left outer join (select date(transaction_date) tx_date ,sum(pwt_amt+agt_claim_comm-tax_amt) pwtDir from st_sle_agent_direct_plr_pwt where transaction_date>='")
					.append(requestBean.getFromDate())
					.append("'")
					.append(" and transaction_date<='")
					.append(requestBean.getToDate())
					.append("'")
					.append(" and game_id=")
					.append(requestBean.getGameId())
					.append(" and agent_org_id=")
					.append(requestBean.getOrgId())
					.append(
							" group by tx_date) pwtDir on alldate=pwtDir.tx_date) pwtDir")
					.append(archiveAppender).append(
							") as main group by main.alldate");

			// logger.debug("fetchPWTDataDayWisePerGameSingleAgent - "
			// + cancelQueryBuilder.toString());

			System.out.println("fetchDirPWTDataDayWisePerGameForAgent - "
					+ dirPWTQueryBuilder.toString());

			stmt = connection.createStatement();
			rs = stmt.executeQuery(dirPWTQueryBuilder.toString());
			while (rs.next()) {
				responseBean = new SLEOrgReportResponseBean();
				responseBean.setPwtDirAmt(rs.getDouble("pwtDir"));
				responseBeanMap.put(rs.getString("alldate").split(" ")[0], responseBean);
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
	 * 
	 * @param requestBean
	 * @param connection
	 * @return
	 * @throws GenericException
	 */
	public static void fetchPWTDataAllGameSingleAgent(
			SLEOrgReportRequestBean requestBean, SLEOrgReportResponseBean responseBean, Connection connection)
			throws GenericException {
		Statement stmt = null;
		ResultSet rs = null;
		StringBuilder saleQueryBuilder = null;
		try {
			String archiveAppender = " UNION ALL SELECT organization_id as parent_id,IFNULL(SUM(pwt_net_amt),0.0) AS pwt FROM st_rep_sle_agent WHERE organization_id="
					+ requestBean.getOrgId()
					+ " AND finaldate>=DATE('"
					+ requestBean.getFromDate()
					+ "')"
					+ " AND finaldate<=DATE('"
					+ requestBean.getToDate()
					+ "')";

			saleQueryBuilder = new StringBuilder();
			saleQueryBuilder
					.append(
							"SELECT parent_id,SUM(main.pwt) AS pwt FROM (SELECT pwt.parent_id,SUM(pwt) pwt FROM (SELECT organization_id,parent_id,IFNULL(pwt,0.0) pwt FROM st_lms_organization_master om LEFT OUTER JOIN (SELECT retailer_org_id,SUM(agent_net_amt) AS pwt FROM st_sle_ret_pwt WHERE transaction_date>='")
					.append(requestBean.getFromDate())
					.append("'")
					.append(" AND transaction_date<='")
					.append(requestBean.getToDate())
					.append("'")
					.append(" GROUP BY retailer_org_id) pwt on om.organization_id=retailer_org_id WHERE om.organization_type='RETAILER' AND om.parent_id=")
					.append(requestBean.getOrgId())
					.append(") pwt")
					.append(archiveAppender).append(
							") as main group by main.parent_id");

			// logger.debug("fetchDepositDataMultipleAgent - "
			// + saleQueryBuilder.toString());

			System.out.println("fetchPWTDataAllGameSingleAgent - "+saleQueryBuilder.toString());
			
			stmt = connection.createStatement();
			rs = stmt.executeQuery(saleQueryBuilder.toString());
			while (rs.next()) {
				responseBean.setPwtAmt(responseBean.getPwtAmt() + rs.getDouble("pwt"));
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


	}

	/**
	 * 
	 * @param requestBean
	 * @param connection
	 * @return
	 * @throws GenericException
	 */
	public static Map<String, SLEOrgReportResponseBean> fetchDirPlyPWTDataDayWiseAllGameForAgent(
			SLEOrgReportRequestBean requestBean, Connection connection)
			throws GenericException {
		Map<String, SLEOrgReportResponseBean> responseBeanMap = null;
		SLEOrgReportResponseBean responseBean = null;
		Statement stmt = null;
		ResultSet rs = null;
		StringBuilder dirPWTQueryBuilder = null;
		try {
			responseBeanMap = new HashMap<String, SLEOrgReportResponseBean>();

			// String walletAppender = (requestBean.getWalletId() == 0) ? ""
			// : " AND wallet_id=" + requestBean.getWalletId();

			String archiveAppender = " union all select sgr.finaldate as alldate,sum(ifnull(sga.direct_pwt_net_amt,0.00))as pwtDir from st_rep_sle_retailer as sgr left outer join  st_rep_sle_agent as sga on sgr.finaldate=sga.finaldate and sgr.parent_id=sga.parent_id where sgr.parent_id="
					+ requestBean.getOrgId()
					// + " AND sgr.game_id="
					// + requestBean.getGameId()
					+ " AND sgr.finaldate>=date('"
					+ requestBean.getFromDate()
					+ "')"
					+ " AND sgr.finaldate<=date('"
					+ requestBean.getToDate()
					+ "')"
					+ " GROUP BY sgr.finaldate";

			dirPWTQueryBuilder = new StringBuilder();
			dirPWTQueryBuilder
					.append(
							"select main.alldate,sum(main.pwtDir)as pwtDir from (select pwtDir.alldate,ifnull(pwtDir,0.0) pwtDir from(select alldate,pwtDir from tempdate left outer join (select date(transaction_date) tx_date ,sum(pwt_amt+agt_claim_comm-tax_amt) pwtDir from st_sle_agent_direct_plr_pwt where transaction_date>='")
					.append(requestBean.getFromDate())
					.append("'")
					.append(" and transaction_date<='")
					.append(requestBean.getToDate())
					.append("'")
					// .append(" and game_id=")
					// .append(requestBean.getGameId())
					.append(" and agent_org_id=")
					.append(requestBean.getOrgId())
					.append(
							" group by tx_date) pwtDir on alldate=pwtDir.tx_date) pwtDir")
					.append(archiveAppender).append(
							") as main group by main.alldate");

			// logger.debug("fetchPWTDataDayWisePerGameSingleAgent - "
			// + cancelQueryBuilder.toString());

			System.out.println("fetchDirPWTDataDayWisePerGameForAgent - "
					+ dirPWTQueryBuilder.toString());

			stmt = connection.createStatement();
			rs = stmt.executeQuery(dirPWTQueryBuilder.toString());
			while (rs.next()) {
				responseBean = new SLEOrgReportResponseBean();
				responseBean.setPwtDirAmt(rs.getDouble("pwtDir"));
				responseBeanMap.put(rs.getString("alldate"), responseBean);
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

	public static void fetchSLEDirectPlyPwtofAgent(
			SLEOrgReportRequestBean requestBean, SLEOrgReportResponseBean responseBean, Connection connection)
			throws GenericException {
		Statement stmt = null;
		ResultSet rs = null;
		StringBuilder dirPWTQueryBuilder = null;
		try {
			String archiveAppender = " UNION ALL select organization_id as agent_org_id,sum(direct_pwt_amt+direct_pwt_comm-direct_pwt_tax)as pwtDir from st_rep_sle_agent where organization_id="
					+ requestBean.getOrgId()
					+ " AND finaldate>='"
					+ requestBean.getFromDate()
					+ "'"
					+ " AND finaldate<='"
					+ requestBean.getToDate()
					+ "'"
					+ " GROUP BY organization_id";

			dirPWTQueryBuilder = new StringBuilder();
			dirPWTQueryBuilder
					.append(
							"select agent_org_id,sum(main.pwtDir)as pwtDir from (select agent_org_id,sum(pwt_amt+agt_claim_comm-tax_amt) pwtDir from st_sle_agent_direct_plr_pwt where transaction_date>='")
					.append(requestBean.getFromDate()).append("'").append(
							" AND transaction_date<='").append(
							requestBean.getToDate()).append("'").append(
							" AND agent_org_id=")
					.append(requestBean.getOrgId()).append(
							" GROUP BY agent_org_id").append(archiveAppender)
					.append(")as main GROUP BY main.agent_org_id");

			// logger.debug("fetchPWTDataDayWisePerGameSingleAgent - "
			// + cancelQueryBuilder.toString());

			stmt = connection.createStatement();
			rs = stmt.executeQuery(dirPWTQueryBuilder.toString());
			if (rs.next()) {
				responseBean.setPwtDirAmt(rs.getDouble("pwtDir"));
			} else {
				responseBean.setPwtDirAmt(0.0);
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

	}
	
	/**
	 * For Payment-Ledger Report view by BO
	 * 
	 * @param requestBean
	 * @param connection
	 * @return
	 * @throws GenericException
	 */
	public static Map<String, SLEOrgReportResponseBean> fetchDirectPlyPWTDataDayWiseAllGameSingleAgent(
			SLEOrgReportRequestBean requestBean, Connection connection)
			throws GenericException {
		Map<String, SLEOrgReportResponseBean> responseBeanMap = null;
		SLEOrgReportResponseBean responseBean = null;
		Statement stmt = null;
		ResultSet rs = null;
		StringBuilder saleQueryBuilder = null;
		try {
			responseBeanMap = new HashMap<String, SLEOrgReportResponseBean>();

			String archiveAppender = " UNION ALL SELECT finaldate,organization_id,SUM(direct_pwt_net_amt) AS pwtDir FROM st_rep_sle_agent WHERE finaldate>='"
					+ requestBean.getFromDate()
					+ "' AND finaldate<='"
					+ requestBean.getToDate()
					+ "'AND organization_id="
					+ requestBean.getOrgId()
					+ " GROUP BY finaldate";

			saleQueryBuilder = new StringBuilder();
			saleQueryBuilder
					.append("SELECT DATE(transaction_date) date,agent_org_id,SUM(pwt_amt+agt_claim_comm-tax_amt) pwtDir FROM st_sle_agent_direct_plr_pwt WHERE transaction_date>='")
					.append(requestBean.getFromDate())
					.append("' AND transaction_date<='")
					.append(requestBean.getToDate())
					.append("' AND agent_org_id=")
					.append(requestBean.getOrgId())
					.append(" GROUP BY DATE(transaction_date)")
					.append(archiveAppender);

			// logger.debug("fetchDepositDataMultipleAgent - "
			// + saleQueryBuilder.toString());

			System.out.println("fetchDirectPlyPWTDataDayWiseAllGameSingleAgent - "+ saleQueryBuilder.toString());

			stmt = connection.createStatement();
			rs = stmt.executeQuery(saleQueryBuilder.toString());
			while (rs.next()) {
				responseBean = new SLEOrgReportResponseBean();
				responseBean.setPwtDirAmt(rs.getDouble("pwtDir"));
				responseBeanMap.put(rs.getString("date"), responseBean);
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

}
