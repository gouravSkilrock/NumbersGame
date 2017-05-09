package com.skilrock.lms.coreEngine.drawGames.reportMgmt;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.beans.DateBeans;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;
import com.skilrock.lms.web.reportsMgmt.common.ReportUtility;

public class DGFullReportHelper implements IDGFullReportHelper{
	static Log logger = LogFactory.getLog(DGSaleReportsHelper.class);

	private Date endDate;
	private Date startDate;

	public DGFullReportHelper(DateBeans dateBeans) {
		this.startDate = dateBeans.getFirstdate();
		this.endDate = dateBeans.getLastdate();
		logger.debug(this.startDate + "  " + this.endDate);
	}

	public Map<String, String> fetchDGFullReport(String cityCode, String stateCode) throws LMSException {
		Connection con = null;
		Map repMap = new LinkedHashMap();
		con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		HashMap saleList = null;
		HashMap pwtList = null;
		HashMap directPlrList = null;
		List completeList = null;
		try {
			Map<Integer, String> gameMap=ReportUtility.getActiveGameNumMap(startDate.toString());
			String orgCodeQry = " name orgCode  ";
			String selColForPwt="som.name orgCode ";
			
			if ((LMSFilterDispatcher.orgFieldType).equalsIgnoreCase("CODE")) {
				orgCodeQry = " org_code orgCode ";
				selColForPwt =" som.org_code orgCode" ;

			} else if ((LMSFilterDispatcher.orgFieldType)
					.equalsIgnoreCase("CODE_NAME")) {
				orgCodeQry = " concat(org_code,'_',name)  orgCode ";
				selColForPwt ="concat(som.org_code,'_',som.name)  orgCode" ;
			

			} else if ((LMSFilterDispatcher.orgFieldType)
					.equalsIgnoreCase("NAME_CODE")) {
				orgCodeQry = " concat(name,'_',org_code)  orgCode ";
				selColForPwt ="concat(som.name,'_',som.org_code)  orgCode" ;
			

			}	
	//		pstmt = con
				//	.prepareStatement("select "+orgCodeQry+", addr_line1, addr_line2, city from st_lms_organization_master where organization_type='AGENT' order by "+QueryManager.getAppendOrgOrder());
			pstmt = con
            .prepareStatement("select "+orgCodeQry+", addr_line1, addr_line2, city from st_lms_organization_master om inner join st_lms_user_master um on om.organization_id = um.organization_id where om.organization_status !='TERMINATE' and om.organization_type='AGENT' order by "+QueryManager.getAppendOrgOrder());

			rs = pstmt.executeQuery();
			while (rs.next()) {
				completeList = new ArrayList();
				completeList.add(new HashMap());
				completeList.add(new HashMap());
				completeList.add(new HashMap());
				completeList.add(rs.getString("city"));
				repMap.put(rs.getString("orgCode"), completeList);
			}
			for(Map.Entry<Integer, String> entry: gameMap.entrySet())
			 {
				int gameId=entry.getKey();
				String SaleQueryLeft = "select ifnull(sale.orgCode, saleRet.orgCode) as agtName, ifnull(sale.totSaleAgt,0) as netSale, ifnull(saleRet.totSaleRetAgt,0) as netSaleRef from (select "+orgCodeQry+", totSaleAgt  from(select som.parent_id as agt_id, sum(totSale) as totSaleAgt from(select retailer_org_id as ret_id, sum(agent_net_amt)as totSale from st_dg_ret_sale_"
						+gameId
						+ " where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_type in ('DG_SALE', 'DG_SALE_OFFLINE') and date(transaction_date)>=? and date(transaction_date)<?) group by retailer_org_id)ret, st_lms_organization_master som where som.organization_type= 'RETAILER' and som.organization_id = ret.ret_id group by som.parent_id)agt, st_lms_organization_master som2 where som2.organization_type='AGENT' and agt.agt_id = som2.organization_id order by "+QueryManager.getAppendOrgOrder()+")sale left outer join (select "+orgCodeQry+", totSaleRetAgt  from(select som.parent_id as agt_id, sum(totSaleRet) as totSaleRetAgt from(select retailer_org_id as ret_id, sum(agent_net_amt)as totSaleRet from st_dg_ret_sale_refund_"
						+ gameId
						+ " where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where date(transaction_date)>=? and date(transaction_date)<? and transaction_type in ('DG_REFUND_CANCEL', 'DG_REFUND_FAILED')) group by retailer_org_id)ret, st_lms_organization_master som where som.organization_type= 'RETAILER' and som.organization_id = ret.ret_id group by som.parent_id)agt, st_lms_organization_master som2 where som2.organization_type='AGENT' and agt.agt_id = som2.organization_id order by "+QueryManager.getAppendOrgOrder()+")saleRet on sale.orgCode = saleRet.orgCode";

				String SaleQueryRight = "select ifnull(sale.orgCode, saleRet.orgCode) as agtName , ifnull(sale.totSaleAgt,0) as netSale, ifnull(saleRet.totSaleRetAgt,0) as netSaleRef from (select "+orgCodeQry+", totSaleAgt  from(select som.parent_id as agt_id, sum(totSale) as totSaleAgt from(select retailer_org_id as ret_id, sum(agent_net_amt)as totSale from st_dg_ret_sale_"
						+ gameId
						+ " where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where transaction_type in ('DG_SALE', 'DG_SALE_OFFLINE') and date(transaction_date)>=? and date(transaction_date)<?) group by retailer_org_id)ret, st_lms_organization_master som where som.organization_type= 'RETAILER' and som.organization_id = ret.ret_id group by som.parent_id)agt, st_lms_organization_master som2 where som2.organization_type='AGENT' and agt.agt_id = som2.organization_id order by "+QueryManager.getAppendOrgOrder()+")sale right outer join (select "+orgCodeQry+", totSaleRetAgt  from(select som.parent_id as agt_id, sum(totSaleRet) as totSaleRetAgt from(select retailer_org_id as ret_id, sum(agent_net_amt)as totSaleRet from st_dg_ret_sale_refund_"
						+ gameId
						+ " where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where date(transaction_date)>=? and date(transaction_date)<? and transaction_type in ('DG_REFUND_CANCEL', 'DG_REFUND_FAILED')) group by retailer_org_id)ret, st_lms_organization_master som where som.organization_type= 'RETAILER' and som.organization_id = ret.ret_id group by som.parent_id)agt, st_lms_organization_master som2 where som2.organization_type='AGENT' and agt.agt_id = som2.organization_id order by "+QueryManager.getAppendOrgOrder()+")saleRet on sale.orgCode = saleRet.orgCode";

				String SaleQueryUnion = "select typ.agtName, (typ.netSale - typ.netSaleRef) as netSalefinal from ("
						+ SaleQueryLeft + " union " + SaleQueryRight + ")typ";
				pstmt = con.prepareStatement(SaleQueryUnion);
				pstmt.setDate(1, startDate);
				pstmt.setDate(2, endDate);
				pstmt.setDate(3, startDate);
				pstmt.setDate(4, endDate);
				pstmt.setDate(5, startDate);
				pstmt.setDate(6, endDate);
				pstmt.setDate(7, startDate);
				pstmt.setDate(8, endDate);
				logger.debug(pstmt);
				rs = pstmt.executeQuery();

				while (rs.next()) {
					saleList = (HashMap) ((ArrayList) repMap.get(rs
							.getString("agtName"))).get(0);
					saleList.put(gameId, rs
							.getDouble("netSalefinal"));
				}

			}
			for(Map.Entry<Integer, String> entry: gameMap.entrySet())
			 {
				int gameId=entry.getKey();
				String PwtQuery = "select "+orgCodeQry+", totPwtAgt  from(select som.parent_id as agt_id, sum(totPwt) as totPwtAgt from(select retailer_org_id as ret_id, sum(pwt_amt + agt_claim_comm)as totPwt from st_dg_ret_pwt_"
						+ gameId
						+ " where transaction_id in (select transaction_id from st_lms_retailer_transaction_master where date(transaction_date)>=? and date(transaction_date)<? and transaction_type = 'DG_PWT') group by retailer_org_id)ret, st_lms_organization_master som where som.organization_type= 'RETAILER' and som.organization_id = ret.ret_id group by som.parent_id)agt, st_lms_organization_master som2 where som2.organization_type='AGENT' and agt.agt_id = som2.organization_id order by "+QueryManager.getAppendOrgOrder();
				pstmt = con.prepareStatement(PwtQuery);
				pstmt.setDate(1, startDate);
				pstmt.setDate(2, endDate);
				rs = pstmt.executeQuery();
				logger.debug(pstmt);
				while (rs.next()) {
					pwtList = (HashMap) ((ArrayList) repMap.get(rs
							.getString("orgCode"))).get(1);
					pwtList.put(gameId, rs.getDouble("totPwtAgt"));
				}

			}
			for(Map.Entry<Integer, String> entry: gameMap.entrySet())
			 {
				int gameId=entry.getKey();
				String agtDirPlrPwtQuery = "select "+selColForPwt+", totDirPlrPwt from(select agent_org_id as agt_id, sum(net_amt + agt_claim_comm)as totDirPlrPwt from st_dg_agt_direct_plr_pwt where transaction_id in (select transaction_id from st_lms_agent_transaction_master where date(transaction_date)>=? and date(transaction_date)<? and transaction_type = 'DG_PWT_PLR') and game_id =? group by agent_org_id)agt, st_lms_organization_master som where som.organization_type= 'AGENT' and som.organization_id = agt.agt_id order by "+QueryManager.getAppendOrgOrder();

				pstmt = con.prepareStatement(agtDirPlrPwtQuery);
				pstmt.setDate(1, startDate);
				pstmt.setDate(2, endDate);
				pstmt.setInt(3, gameId);
				logger.debug(pstmt);
				rs = pstmt.executeQuery();

				while (rs.next()) {
					directPlrList = (HashMap) ((ArrayList) repMap.get(rs
							.getString("orgCode"))).get(2);
					directPlrList.put(gameId, rs
							.getDouble("totDirPlrPwt"));
				}

			}
			logger.debug(repMap);

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

		return repMap;
	}

	
}