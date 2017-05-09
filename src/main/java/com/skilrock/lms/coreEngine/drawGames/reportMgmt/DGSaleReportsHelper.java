package com.skilrock.lms.coreEngine.drawGames.reportMgmt;

import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.skilrock.lms.beans.DateBeans;
import com.skilrock.lms.beans.LiveReportBean;
import com.skilrock.lms.beans.SaleReportBean;
import com.skilrock.lms.beans.ServiceRequest;
import com.skilrock.lms.beans.ServiceResponse;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.ConfigurationVariables;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.FormatNumber;
import com.skilrock.lms.coreEngine.drawGames.drawMgmt.DrawGameMgmtHelper;
import com.skilrock.lms.coreEngine.service.IServiceDelegate;
import com.skilrock.lms.coreEngine.service.ServiceDelegate;
import com.skilrock.lms.coreEngine.service.dge.ServiceMethodName;
import com.skilrock.lms.coreEngine.service.dge.ServiceName;
import com.skilrock.lms.dge.beans.DGConsolidateDrawBean;
import com.skilrock.lms.dge.beans.DGConsolidateGameDataBean;
import com.skilrock.lms.dge.beans.DrawDataBean;
import com.skilrock.lms.dge.beans.DrawGameMtnDataBean;
import com.skilrock.lms.dge.beans.JackpotBean;
import com.skilrock.lms.dge.beans.JackpotViewBean;
import com.skilrock.lms.dge.beans.ReportDrawBean;
import com.skilrock.lms.web.drawGames.common.Util;

public class DGSaleReportsHelper {
	static Log logger = LogFactory.getLog(DGSaleReportsHelper.class);
	private Connection con = null;
	private Date endDate;
	private int OrgId;
	private Date startDate;

	public DGSaleReportsHelper(){
		
	}

	public DGSaleReportsHelper(UserInfoBean userInfoBean, DateBeans dateBeans) {
		this.startDate = dateBeans.getFirstdate();
		this.endDate = dateBeans.getLastdate();
		this.OrgId = userInfoBean.getUserOrgId();
		logger.debug(this.startDate + "  " + this.endDate);
	}

	public Map<Integer, String> ajaxAgentList() throws LMSException {
		Map<Integer, String> map = new TreeMap<Integer, String>();
		con = DBConnect.getConnection();
		try {
			PreparedStatement pstmt = con
					.prepareStatement("select name, organization_id from st_lms_organization_master where organization_type='AGENT'");

			logger.debug("get the DG sale detail query -- " + pstmt);

			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				map.put(rs.getInt("organization_id"), rs.getString("name"));
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

	// End of Added by Neeraj

	/**
	 * Added by Neeraj for Jackpot View
	 * 
	 * @return
	 * @throws LMSException
	 */
	public List<JackpotViewBean> fetchDGJackpotViewDetail(String drawId,
			int gameId) throws LMSException {
		ServiceResponse sRes = new ServiceResponse();
		ServiceRequest sReq = new ServiceRequest();
		String drawStr = "";
		JackpotBean jackpotBean = new JackpotBean();
		List<JackpotViewBean> jackpotList = new ArrayList<JackpotViewBean>();

		if (!drawId.equalsIgnoreCase(" ")) {
			drawStr = " and gm.draw_id = " + drawId;
		}
		String jackpotquery = QueryManager
				.getST_DG_JACKPOT_REPORT_GAME_WISE_BO()
				+ drawStr;

		jackpotBean.setJackpotQuery(jackpotquery);
		jackpotBean.setGameId(gameId);
		jackpotBean.setStartDate(startDate);
		jackpotBean.setEndDate(endDate);
		sReq.setServiceName(ServiceName.REPORTS_MGMT);
		sReq.setServiceMethod(ServiceMethodName.FETCH_JACKPOT_DETAIL);
		sReq.setServiceData(jackpotBean);
		IServiceDelegate delegate = ServiceDelegate.getInstance();
		sRes = delegate.getResponse(sReq);
		logger.debug("sRes:" + sRes);
		if (sRes != null && sRes.getIsSuccess()) {
			Type type = new TypeToken<List<JackpotViewBean>>(){}.getType();
			jackpotList=new Gson().fromJson((JsonElement)sRes.getResponseData(), type);
		}
		logger.debug("List obtained from DGE:  " + jackpotList);
		return jackpotList;
	}

	public List<SaleReportBean> fetchDGSaleDetailAgentWise()
			throws LMSException {
		List<SaleReportBean> list = new ArrayList<SaleReportBean>();
		SaleReportBean reportbean = null;
		con = DBConnect.getConnection();
		try {
			PreparedStatement pstmt = con.prepareStatement(QueryManager
					.getST_DG_SALE_REPORT_AGENT_WISE_BO());
			pstmt.setDate(1, startDate);
			pstmt.setDate(2, endDate);
			pstmt.setDate(3, startDate);
			pstmt.setDate(4, endDate);

			logger.debug("get the DG sale detail query -- " + pstmt);

			ResultSet rs = pstmt.executeQuery();
			double mrpSale = 0.0;
			double mrpSaleRef = 0.0;
			double mrpAmt = 0.0;
			double netSale = 0.0;
			double netSaleRef = 0.0;
			double netAmt = 0.0;
			while (rs.next()) {
				reportbean = new SaleReportBean();
				reportbean.setAgentName(rs.getString("agent_name"));
				reportbean.setSaleMrp(FormatNumber.formatNumber(rs
						.getDouble("SaleMrp")));
				mrpSale += rs.getDouble("SaleMrp");
				reportbean.setSaleReturnMrp(FormatNumber.formatNumber(rs
						.getDouble("RefundMrp")));
				mrpSaleRef += rs.getDouble("RefundMrp");
				reportbean.setNetMrpAmt(FormatNumber.formatNumber(rs
						.getDouble("NetMrp")));
				mrpAmt += rs.getDouble("NetMrp");
				reportbean.setSaleAmt(FormatNumber.formatNumber(rs
						.getDouble("SaleNet")));
				netSale += rs.getDouble("SaleNet");
				reportbean.setReturnAmt(FormatNumber.formatNumber(rs
						.getDouble("RefundNet")));
				netSaleRef += rs.getDouble("RefundNet");
				reportbean.setNetAmt(FormatNumber.formatNumber(rs
						.getDouble("NetNet")));
				netAmt += rs.getDouble("NetNet");
				list.add(reportbean);
			}
			rs.close();
			pstmt.close();
			reportbean = new SaleReportBean();
			reportbean.setAgentName("Total");
			reportbean.setSaleMrp(FormatNumber.formatNumber(mrpSale));
			reportbean.setSaleReturnMrp(FormatNumber.formatNumber(mrpSaleRef));
			reportbean.setNetMrpAmt(FormatNumber.formatNumber(mrpAmt));
			reportbean.setSaleAmt(FormatNumber.formatNumber(netSale));
			reportbean.setReturnAmt(FormatNumber.formatNumber(netSaleRef));
			reportbean.setNetAmt(FormatNumber.formatNumber(netAmt));
			list.add(reportbean);
			logger.debug("size of list = " + list);
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

		return list;
	}

	public List<SaleReportBean> fetchDGSaleDetailGameWise() throws LMSException {
		List<SaleReportBean> list = new ArrayList<SaleReportBean>();
		SaleReportBean reportbean = null;
		con = DBConnect.getConnection();
		try {
			PreparedStatement pstmt = con.prepareStatement(QueryManager
					.getST_DG_SALE_REPORT_GAME_WISE_BO());
			pstmt.setDate(1, startDate);
			pstmt.setDate(2, endDate);
			pstmt.setDate(3, startDate);
			pstmt.setDate(4, endDate);
			logger.debug("get the DG Sale detail query -- " + pstmt);

			ResultSet rs = pstmt.executeQuery();
			double mrpSale = 0.0;
			double mrpSaleRef = 0.0;
			double mrpAmt = 0.0;
			double netSale = 0.0;
			double netSaleRef = 0.0;
			double netAmt = 0.0;

			while (rs.next()) {
				reportbean = new SaleReportBean();
				reportbean.setGamename(rs.getString("game_name"));
				reportbean.setSaleMrp(FormatNumber.formatNumber(rs
						.getDouble("SaleMrp")));
				mrpSale += rs.getDouble("SaleMrp");
				reportbean.setSaleReturnMrp(FormatNumber.formatNumber(rs
						.getDouble("RefundMrp")));
				mrpSaleRef += rs.getDouble("RefundMrp");
				reportbean.setNetMrpAmt(FormatNumber.formatNumber(rs
						.getDouble("NetMrp")));
				mrpAmt += rs.getDouble("NetMrp");
				reportbean.setSaleAmt(FormatNumber.formatNumber(rs
						.getDouble("SaleNet")));
				netSale += rs.getDouble("SaleNet");
				reportbean.setReturnAmt(FormatNumber.formatNumber(rs
						.getDouble("RefundNet")));
				netSaleRef += rs.getDouble("RefundNet");
				reportbean.setNetAmt(FormatNumber.formatNumber(rs
						.getDouble("NetNet")));
				netAmt += rs.getDouble("NetNet");
				list.add(reportbean);
			}
			rs.close();
			pstmt.close();
			reportbean = new SaleReportBean();
			reportbean.setGamename("Total");
			reportbean.setSaleMrp(FormatNumber.formatNumber(mrpSale));
			reportbean.setSaleReturnMrp(FormatNumber.formatNumber(mrpSaleRef));
			reportbean.setNetMrpAmt(FormatNumber.formatNumber(mrpAmt));
			reportbean.setSaleAmt(FormatNumber.formatNumber(netSale));
			reportbean.setReturnAmt(FormatNumber.formatNumber(netSaleRef));
			reportbean.setNetAmt(FormatNumber.formatNumber(netAmt));
			list.add(reportbean);
			logger.debug("size of list = " + list.size());
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

		return list;
	}

	public List<SaleReportBean> fetchDGSaleDetailRetailerWise(int AgtId)
			throws LMSException {
		List<SaleReportBean> list = new ArrayList<SaleReportBean>();
		SaleReportBean reportbean = null;
		con = DBConnect.getConnection();
		try {
			PreparedStatement pstmt = con.prepareStatement(QueryManager
					.getST_DG_SALE_REPORT_RETAILER_WISE_BO());
			pstmt.setDate(1, startDate);
			pstmt.setDate(2, endDate);
			pstmt.setDate(3, startDate);
			pstmt.setDate(4, endDate);
			pstmt.setInt(5, AgtId);
			logger.debug("get the DG sale detail query -- " + pstmt);

			ResultSet rs = pstmt.executeQuery();
			double mrpSale = 0.0;
			double mrpSaleRef = 0.0;
			double mrpAmt = 0.0;
			double netSale = 0.0;
			double netSaleRef = 0.0;
			double netAmt = 0.0;
			while (rs.next()) {
				reportbean = new SaleReportBean();
				reportbean.setRetName(rs.getString("ret_name"));
				reportbean.setSaleMrp(FormatNumber.formatNumber(rs
						.getDouble("SaleMrp")));
				mrpSale += rs.getDouble("SaleMrp");
				reportbean.setSaleReturnMrp(FormatNumber.formatNumber(rs
						.getDouble("RefundMrp")));
				mrpSaleRef += rs.getDouble("RefundMrp");
				reportbean.setNetMrpAmt(FormatNumber.formatNumber(rs
						.getDouble("NetMrp")));
				mrpAmt += rs.getDouble("NetMrp");
				reportbean.setSaleAmt(FormatNumber.formatNumber(rs
						.getDouble("SaleNet")));
				netSale += rs.getDouble("SaleNet");
				reportbean.setReturnAmt(FormatNumber.formatNumber(rs
						.getDouble("RefundNet")));
				netSaleRef += rs.getDouble("RefundNet");
				reportbean.setNetAmt(FormatNumber.formatNumber(rs
						.getDouble("NetNet")));
				netAmt += rs.getDouble("NetNet");
				list.add(reportbean);
			}
			rs.close();
			pstmt.close();
			reportbean = new SaleReportBean();
			reportbean.setRetName("Total");
			reportbean.setSaleMrp(FormatNumber.formatNumber(mrpSale));
			reportbean.setSaleReturnMrp(FormatNumber.formatNumber(mrpSaleRef));
			reportbean.setNetMrpAmt(FormatNumber.formatNumber(mrpAmt));
			reportbean.setSaleAmt(FormatNumber.formatNumber(netSale));
			reportbean.setReturnAmt(FormatNumber.formatNumber(netSaleRef));
			reportbean.setNetAmt(FormatNumber.formatNumber(netAmt));
			list.add(reportbean);
			logger.debug("size of list = " + list);
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

		return list;
	}

	public List<LiveReportBean> getLiveReportData(int agtOrgId,
			DateBeans dateBeans) throws LMSException {
		List<LiveReportBean> list = new ArrayList<LiveReportBean>();
		LiveReportBean bean = null;
		con = DBConnect.getConnection();
		try {
			List<Integer> gameNumList = Util.getGameNumberList();
			StringBuilder saleUnionQuery = new StringBuilder("");
			StringBuilder saleRefundUnionQuery = new StringBuilder("");
			StringBuilder pwtUnionQuery = new StringBuilder("");

			for (int i = 0; i < gameNumList.size(); i++) {
				saleUnionQuery.append("select * from st_dg_ret_sale_"
						+ gameNumList.get(i) + " union ");
				saleRefundUnionQuery
						.append("select * from st_dg_ret_sale_refund_"
								+ gameNumList.get(i) + " union ");
				pwtUnionQuery.append("select * from st_dg_ret_pwt_"
						+ gameNumList.get(i) + " union ");
			}

			String txnQuery = "select transaction_id, retailer_org_id from st_lms_retailer_transaction_master where date(transaction_date) >= ? and date(transaction_date) < ? and retailer_org_id in (select organization_id from st_lms_organization_master where organization_type='RETAILER' and parent_id = ?)";
			String mainQuery = "select tot.retailer_org_id, org.parent_id, org.name, ifnull(tot.mrpSale,0)as mrpSale, ifnull(tot.netSale,0)as netSale, ifnull(tot.mrpSaleRef,0) as mrpSaleRef, ifnull(tot.netSaleRef,0) as netSaleRef, ifnull(tot.mrpPwt,0) as mrpPwt, ifnull(tot.netPwt,0) as netPwt from (select mrpSale, netSale, mrpSaleRef, netSaleRef, mrpPwt, netPwt, mix.retailer_org_id from (select mrpSale, netSale, mrpSaleRef, netSaleRef, s.retailer_org_id from (select ifnull(sum(sale.mrp_amt),0)mrpSale, ifnull(sum(sale.net_amt),0)netSale, txn.retailer_org_id from ("
					+ saleUnionQuery.toString().substring(0,
							saleUnionQuery.lastIndexOf(" union "))
					+ ")sale, ("
					+ txnQuery.toString()
					+ ")txn where txn.transaction_id = sale.transaction_id group by txn.retailer_org_id )s left outer join (select ifnull(sum(saleRef.mrp_amt),0)mrpSaleRef, ifnull(sum(saleRef.net_amt),0)netSaleRef, txn.retailer_org_id from ("
					+ saleRefundUnionQuery.toString().substring(0,
							saleRefundUnionQuery.lastIndexOf(" union "))
					+ ")saleRef, ("
					+ txnQuery.toString()
					+ ")txn where txn.transaction_id = saleRef.transaction_id group by txn.retailer_org_id) srf on s.retailer_org_id = srf.retailer_org_id)mix left outer join (select ifnull(sum(pwt.pwt_amt),0)mrpPwt, ifnull(sum(pwt.pwt_amt + pwt.retailer_claim_comm),0)netPwt, txn.retailer_org_id from ("
					+ pwtUnionQuery.toString().substring(0,
							pwtUnionQuery.lastIndexOf(" union "))
					+ ")pwt,("
					+ txnQuery.toString()
					+ ")txn where txn.transaction_id = pwt.transaction_id group by txn.retailer_org_id) pt on mix.retailer_org_id = pt.retailer_org_id)tot, st_lms_organization_master as org where tot.retailer_org_id = org.organization_id and org.organization_type = 'RETAILER' order by org.name";
			PreparedStatement pstmt = con.prepareStatement(mainQuery);
			pstmt.setDate(1, dateBeans.getFirstdate());
			pstmt.setDate(2, dateBeans.getLastdate());
			pstmt.setInt(3, agtOrgId);
			pstmt.setDate(4, dateBeans.getFirstdate());
			pstmt.setDate(5, dateBeans.getLastdate());
			pstmt.setInt(6, agtOrgId);
			pstmt.setDate(7, dateBeans.getFirstdate());
			pstmt.setDate(8, dateBeans.getLastdate());
			pstmt.setInt(9, agtOrgId);
			logger.debug("full query for live report :" + pstmt);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new LiveReportBean();
				bean.setRetName(rs.getString("name"));
				bean.setMrpSale(rs.getString("mrpSale"));
				bean.setNetSale(rs.getString("netSale"));
				bean.setMrpSaleRef(rs.getString("mrpSaleRef"));
				bean.setNetSaleRef(rs.getString("netSaleRef"));
				bean.setMrpPwt(rs.getString("mrpPwt"));
				bean.setNetPwt(rs.getString("netPwt"));
				list.add(bean);
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
		return list;
	}
/**
 * 
 * @param drawDataBean
 * @param raffleTktType
 * @return
 */
	public DGConsolidateGameDataBean fetchDrawGameConsolidateData(
			DrawDataBean drawDataBean, String raffleTktType) {

		DGConsolidateGameDataBean consolidateBean = new DGConsolidateGameDataBean();
		ServiceRequest serReq = new ServiceRequest();
		ServiceResponse serResp = new ServiceResponse();
		drawDataBean.setMerchantId("ALL");
		serReq.setServiceName(ServiceName.REPORTS_MGMT);
		serReq.setServiceMethod(ServiceMethodName.FETCH_DRAW_GAME_CONSOLIDATE_DATA);
		serReq.setServiceData(drawDataBean);
		IServiceDelegate delegate = ServiceDelegate.getInstance();
		serResp = delegate.getResponse(serReq);
		try {
			if (serResp.getIsSuccess()) {
				Type type = new TypeToken<DGConsolidateGameDataBean>(){}.getType();
				consolidateBean = (DGConsolidateGameDataBean)new Gson().fromJson((JsonElement)serResp.getResponseData(), type);
				logger.info("Got Draw Game Consolidate Data "+consolidateBean);
//				consolidateBean = (DGConsolidateGameDataBean) serResp
//						.getResponseData();	
				String gameType = Util.getGameType(drawDataBean.getGameNo());
				if ("RAFFLE".equalsIgnoreCase(gameType)) {
					// to cut last four digit in case of raffle GAME
					if ("ORIGINAL".equalsIgnoreCase(raffleTktType)) {
						DGConsolidateDrawBean reportDrawBean = null;
						for (int i = 0; i < consolidateBean
								.getDrawDataBeanList().size(); i++) {
							reportDrawBean = consolidateBean
									.getDrawDataBeanList().get(i);
							String winRes = reportDrawBean.getWinningResult();
							if (winRes != null) {
								String[] winResultArr = winRes.split(",");
								StringBuilder finalresult = new StringBuilder(
										"");
								for (int j = 0; j < winResultArr.length; j++) {
									String winResWithRpCnt = winResultArr[j];
									if (winResWithRpCnt != null
											&& !"null"
													.equalsIgnoreCase(winResWithRpCnt)) {
										int length = winResWithRpCnt.length();
										if (length == ConfigurationVariables.tktLenA
												|| length == ConfigurationVariables.tktLenB) {
											finalresult.append(winResWithRpCnt
													.substring(0, length - 4));
											finalresult.append("xxxx");
											finalresult.append(",");
										}
									}
								}
								if (finalresult != null
										&& !"".equals(finalresult.toString())
										&& !"0".equals(finalresult.toString())) {
									finalresult.deleteCharAt(finalresult
											.length() - 1);
								}
								reportDrawBean.setWinningResult(finalresult
										.toString());
							}
						}
					} else {
						// for swap result with sale ticket number in case of
						// reference ticket
						DrawGameMgmtHelper helper = new DrawGameMgmtHelper();
						helper.getDisplayTktNumber(consolidateBean); // in case
																		// of
																		// reference
																		// ticket
					}
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			
		}

		return consolidateBean;
	}

	@SuppressWarnings("unchecked")
	public List<DrawGameMtnDataBean> fetchDrawWiseMtnData(DrawDataBean drawDataBean) throws LMSException {
		List<DrawGameMtnDataBean> mtnDataList = null;
		ServiceRequest serReq = new ServiceRequest();
		ServiceResponse serResp = new ServiceResponse();
		IServiceDelegate delegate = ServiceDelegate.getInstance();
		try {
			drawDataBean.setMerchantId("ALL");
			serReq.setServiceName(ServiceName.REPORTS_MGMT);
			serReq.setServiceMethod(ServiceMethodName.FETCH_DRAW_WISE_MTN_DATA);
			serReq.setServiceData(drawDataBean);
			serResp = delegate.getResponse(serReq);
			if(!serResp.getIsSuccess()){
				String responseData =  serResp.getResponseData().toString();
				logger.info("response object : " + responseData);
				if(responseData.contains("4052")){
					throw new LMSException(Integer.parseInt(responseData.split(":")[0].replaceAll("\"", "").trim()), responseData.split(":")[1].replaceAll("\"", "").trim());
				}
			}
			if (serResp.getIsSuccess())
				mtnDataList = (List<DrawGameMtnDataBean>) new Gson().fromJson((JsonElement) serResp.getResponseData(), new TypeToken<List<DrawGameMtnDataBean>>() {}.getType());
		} catch (LMSException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return mtnDataList;
	}
}