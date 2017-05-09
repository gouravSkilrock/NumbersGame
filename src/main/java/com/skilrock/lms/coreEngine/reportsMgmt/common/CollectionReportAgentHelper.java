package com.skilrock.lms.coreEngine.reportsMgmt.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.beans.CollectionReportBean;
import com.skilrock.lms.beans.DateBeans;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;
import com.skilrock.lms.common.utility.FormatNumber;

public class CollectionReportAgentHelper {

	private static Log logger = LogFactory
			.getLog(CollectionReportAgentHelper.class);

	public static String formatDate(Date date) {
		String datestr = new SimpleDateFormat("dd/MM/yyyy").format(date);
		System.out.println(datestr);

		return datestr;
	}

	public static void main(String[] args) {
		(new CollectionReportAgentHelper(null, null)).getRetailerOrgId();
	}

	private int agentOrgId;
	private int agentUserId;
	private Connection con = null;
	private DateBeans dateBean;

	private java.sql.Date end;
	public Map<String, String> lastRowMap = new TreeMap<String, String>();

	private PreparedStatement pstmt = null;

	private java.sql.Date start;

	private String totalOpenBal;

	public CollectionReportAgentHelper(UserInfoBean infoBean,
			DateBeans dateBeans) {
		this.dateBean = dateBeans;
		this.start = new java.sql.Date(dateBeans.getFirstdate().getTime());
		this.end = new java.sql.Date(dateBeans.getLastdate().getTime());
		this.agentUserId = infoBean.getUserId();
		this.agentOrgId = infoBean.getUserOrgId();
		System.out.println(dateBeans.getFirstdate() + "  "
				+ dateBeans.getLastdate() + " userId " + agentUserId
				+ " agentOrgId " + agentOrgId);
	}

	public List<CollectionReportBean> getAgentCollectionDetail(
			Map<String, Integer> partyIdMap, boolean isDraw, boolean isScratch,
			boolean isOla,boolean isCS,boolean forOpenBal) throws ParseException {
		List<CollectionReportBean> list = new ArrayList<CollectionReportBean>();
		CollectionReportBean collectionBean = null;
		con = DBConnect.getConnection();
		ResultSet rs = null, rs1 = null, rs2 = null, rs3 = null, rs4=null;
		System.out.println(con + "size of report list " + partyIdMap.size());
		if (!forOpenBal) {
			this.start = new java.sql.Date(dateBean.getFirstdate().getTime());
			this.end = new java.sql.Date(dateBean.getLastdate().getTime());
		}
		// used to add a row of total player pwt amount
		// PwtReportBean totalPlayerPwt=getPlayerPwtDetail(con);
		// if(totalPlayerPwt!=null) list.add(totalPlayerPwt);
		Set<String> partyIdlist = partyIdMap.keySet();
		DecimalFormat df = new DecimalFormat("##.## ");
		try {

			double totalSaleRet = 0.0;
			double totalCash = 0.0;
			double totalChq = 0.0;
			double totalChqRet = 0.0;
			double totalCredit = 0.0;
			double totalDebit = 0.0;
			double totalDrawSale = 0.0;
			double totalDrawSaleRefund = 0.0;
			double totalDrawPwt = 0.0;
			double totalScratchSale = 0.0;
			double totalScratchPwt = 0.0;
			double totalRecTotal = 0.0;
			double totalScratchTotal = 0.0;
			double totalDrawTotal = 0.0;
			double totalolaTotal = 0.0;

			double totalGrandTotal = 0.0;
			int count = 1;

			double totalRetailerDeposit = 0.0;
			double totalRetailerWithdraw = 0.0;
			double totalNetGaming = 0.0;
			double totalRetailerCsSale=0.0;
			double totalRetailerCsSaleRefund=0.0;
			double totalCsTotal=0.0;
			
			
			System.out
					.println("I AM IN THE collection report agent helper in getAgentCollectionDetail method");
			for (String nameKey : partyIdlist) {
				int partyid = partyIdMap.get(nameKey);
				pstmt = con.prepareStatement(QueryManager
						.getST_COLLECTION_DETAILS_FOR_AGENT());
				pstmt.setInt(1, agentOrgId);
				pstmt.setInt(2, partyid);
				pstmt.setDate(3, start);
				pstmt.setDate(4, end);

				pstmt.setInt(5, agentOrgId);
				pstmt.setInt(6, partyid);
				pstmt.setDate(7, start);
				pstmt.setDate(8, end);

				pstmt.setInt(9, agentOrgId);
				pstmt.setInt(10, partyid);
				pstmt.setDate(11, start);
				pstmt.setDate(12, end);

				pstmt.setInt(13, agentOrgId);
				pstmt.setInt(14, partyid);
				pstmt.setDate(15, start);
				pstmt.setDate(16, end);

				pstmt.setInt(17, agentOrgId);
				pstmt.setInt(18, partyid);
				pstmt.setDate(19, start);
				pstmt.setDate(20, end);

				rs = pstmt.executeQuery();
				logger
						.debug("get Agent accounts collections details query- ==== -"
								+ pstmt);

				if (isScratch) {
					pstmt = con.prepareStatement(QueryManager
							.getST_COLLECTION_DETAILS_FOR_AGENT_SE());
					pstmt.setInt(1, agentOrgId);
					pstmt.setInt(2, partyid);
					pstmt.setDate(3, start);
					pstmt.setDate(4, end);

					pstmt.setInt(5, agentOrgId);
					pstmt.setInt(6, partyid);
					pstmt.setDate(7, start);
					pstmt.setDate(8, end);

					pstmt.setInt(9, agentOrgId);
					pstmt.setInt(10, partyid);
					pstmt.setDate(11, start);
					pstmt.setDate(12, end);
					
					pstmt.setInt(13, agentOrgId);
					pstmt.setInt(14, partyid);
					pstmt.setDate(15, start);
					pstmt.setDate(16, end);

					pstmt.setInt(17, agentOrgId);
					pstmt.setInt(18, partyid);
					pstmt.setDate(19, start);
					pstmt.setDate(20, end);
					
					logger
							.debug("get Agent scratch collections details query- ==== -"
									+ pstmt);
					rs1 = pstmt.executeQuery();
				}
				if (isDraw) {
					pstmt = con.prepareStatement(QueryManager
							.getST_COLLECTION_DETAILS_FOR_AGENT_DG());
					pstmt.setInt(1, agentOrgId);
					pstmt.setInt(2, partyid);
					pstmt.setDate(3, start);
					pstmt.setDate(4, end);

					pstmt.setInt(5, agentOrgId);
					pstmt.setInt(6, partyid);
					pstmt.setDate(7, start);
					pstmt.setDate(8, end);

					pstmt.setInt(9, agentOrgId);
					pstmt.setInt(10, partyid);
					pstmt.setDate(11, start);
					pstmt.setDate(12, end);
					logger
							.debug(" get Agent draw collections details query- ==== -"
									+ pstmt);
					rs2 = pstmt.executeQuery();

				}

				if (isOla) {
					System.out
							.println("Inside OLA Block to execute The Query......");
					pstmt = con.prepareStatement(QueryManager
							.getST_COLLECTION_DETAILS_FOR_AGENT_OLA1());

					pstmt.setDate(1, start);
					pstmt.setDate(2, end);
					pstmt.setInt(3, partyid);

					pstmt.setDate(4, start);
					pstmt.setDate(5, end);
					pstmt.setInt(6, partyid);

					pstmt.setDate(7, start);
					pstmt.setDate(8, end);
					pstmt.setInt(9, partyid);

					pstmt.setDate(10, start);
					pstmt.setDate(11, end);
					pstmt.setInt(12, partyid);

					pstmt.setDate(13, start);
					pstmt.setDate(14, end);
					pstmt.setInt(15, agentOrgId);

					pstmt.setInt(16, partyid);

					logger
							.debug(" get OLA DETAILS ON THe AGENT SIDE ..........- ==== -"
									+ pstmt);
					rs3 = pstmt.executeQuery();

				}
				
				
								
				if(isCS){
					
					System.out
					.println("Inside CS Block to execute The Query......");
			pstmt = con.prepareStatement(QueryManager
					.getST_COLLECTION_DETAILS_FOR_AGENT_CS());
			

			System.out.println(start);
			System.out.println(end);

			pstmt.setInt(1, agentOrgId);
			pstmt.setDate(2, start);
			pstmt.setDate(3, end);
			pstmt.setInt(4, partyid);
			
			pstmt.setInt(5, agentOrgId);
			pstmt.setDate(6, start);
			pstmt.setDate(7, end);
			pstmt.setInt(8, partyid);
			
			logger
			.debug(" get CS DETAILS ON THe AGENT SIDE ..........- ==== -"
					+ pstmt);
			rs4 = pstmt.executeQuery();
			
				}
				
				

				double recTotal = 0.0;
				if (rs.next()) {
					collectionBean = new CollectionReportBean();
					collectionBean.setSrNo(count);
					collectionBean.setName(nameKey);
					collectionBean.setOrgId(partyid);

					double cash = rs.getDouble("cash");
					collectionBean.setCash(FormatNumber.formatNumber(cash));
					double cheque = rs.getDouble("cheque");
					collectionBean.setChq(FormatNumber.formatNumber(cheque));
					double credit = rs.getDouble("credit");
					collectionBean.setCredit(FormatNumber.formatNumber(credit));
					double debit = rs.getDouble("debit");
					collectionBean.setDebit(FormatNumber.formatNumber(debit));
					double chqRet = rs.getDouble("cheque_ret");
					collectionBean.setChqRet(FormatNumber.formatNumber(chqRet));

					recTotal = cash + cheque + credit - debit - chqRet;
					collectionBean.setRecTotal(FormatNumber
							.formatNumber(recTotal));
					totalCash += cash;
					totalChq += cheque;
					totalCredit += credit;
					totalDebit += debit;
					totalChqRet += chqRet;
					totalRecTotal += recTotal;
				}
				double scratchTotal = 0.0;
				double drawTotal = 0.0;
				double olaTotal = 0.0;
				double csTotal=0.0;

				if (rs2 != null) {
					if (rs2.next()) {
						collectionBean.setIsDraw(true);
						double dgSale = rs2.getDouble("dg_sale");
						collectionBean.setDrawSale(FormatNumber
								.formatNumber(dgSale));
						double dgSaleRefund = rs2.getDouble("dg_sale_refund");
						collectionBean.setDrawSaleRefund(FormatNumber
								.formatNumber(dgSaleRefund));
						double dgPwt = rs2.getDouble("dg_pwt");
						collectionBean.setDrawPwt(FormatNumber
								.formatNumber(dgPwt));
						totalDrawSale += dgSale;
						totalDrawSaleRefund += dgSaleRefund;
						totalDrawPwt += dgPwt;
						drawTotal = dgSale - dgSaleRefund - dgPwt;
						totalDrawTotal += drawTotal;
						collectionBean.setDrawTotal(FormatNumber
								.formatNumber(drawTotal));
					}
				}
				if (rs1 != null) {
					if (rs1.next()) {
						collectionBean.setIsScratch(true);
						double seSale = rs1.getDouble("se_sale");
						collectionBean.setScratchSale(FormatNumber
								.formatNumber(seSale));
						double sePwt = rs1.getDouble("se_pwt");
						collectionBean.setScratchPwt(FormatNumber
								.formatNumber(sePwt));
						double seSaleRet = rs1.getDouble("se_sale_ret");
						collectionBean.setSaleRet(FormatNumber
								.formatNumber(seSaleRet));
						totalScratchSale += seSale;
						totalSaleRet += seSaleRet;
						totalScratchPwt += sePwt;
						scratchTotal = seSale - seSaleRet - sePwt;
						totalScratchTotal += scratchTotal;
						collectionBean.setScratchTotal(FormatNumber
								.formatNumber(scratchTotal));
					}
				}

				if (rs3 != null) {
					if (rs3.next()) {
						collectionBean.setIsOla(true);
						double deposit = rs3.getDouble(1);
						System.out.println("DEPOSIT VALUE..." + deposit);
						collectionBean.setOlaDeposit(Double.parseDouble(df
								.format(deposit)));
						double withdraw = rs3.getDouble(2);
						System.out.println("WithDraw VALUE..." + withdraw);
						collectionBean.setOlaWithdraw(Double.parseDouble(df
								.format(withdraw)));
						double netGaming = rs3.getDouble(3);
						System.out.println("NET GAME VALUE..." + netGaming);
						collectionBean.setRetailerNetGaming(Double
								.parseDouble(df.format(netGaming)));

						totalRetailerDeposit += deposit;
						totalRetailerWithdraw += withdraw;
						totalNetGaming += netGaming;
						olaTotal = deposit - withdraw - netGaming;
						System.out.println("TOTAL..." + olaTotal);
						totalolaTotal += olaTotal;
						collectionBean.setOlaRetailerWiseTotal(Double
								.parseDouble(df.format(olaTotal)));
						System.out.println("olaTotal......" + totalolaTotal);

					}
				}
				
				if (rs4 != null) {
					if (rs4.next()) {
						collectionBean.setIsCS(true);
						
						double csSale=rs4.getDouble(2);
						collectionBean.setCsSale(Double.parseDouble(df
								.format(csSale)));
						System.out.println("csSale VALUE..." + csSale);
						double csSaleRefund=rs4.getDouble(3);
						collectionBean.setCsSaleRefund(Double.parseDouble(df
								.format(csSaleRefund)));
						System.out.println("csSaleRefund VALUE..." + csSaleRefund);
						csTotal=rs4.getDouble(4);
						collectionBean.setCsRetailerWiseTotal(Double.parseDouble(df
								.format(csTotal)));
						System.out.println("csTotal VALUE..." + csTotal);
						totalRetailerCsSale=+csSale;
						totalRetailerCsSaleRefund=+csSaleRefund;
						totalCsTotal=+csTotal;
					}
					
				}
				double grandTotal = drawTotal + scratchTotal + olaTotal + csTotal
						- recTotal;

				if (forOpenBal) {
					collectionBean.setOpenBal(FormatNumber
							.formatNumber(grandTotal));
				}
				collectionBean.setGrandTotal(FormatNumber
						.formatNumber(grandTotal));

				totalGrandTotal += grandTotal;
				logger.debug("this is a shit: " + totalGrandTotal);
				logger.debug("this is a shit2: " + grandTotal);
				list.add(collectionBean);
				count += 1;

			}
			System.out.println("totalGrandTotal........" + totalGrandTotal);

			if (totalOpenBal != null) {
				totalGrandTotal += Double.parseDouble(totalOpenBal);
			}
			// create the map for the last row (TOTALS)
			if (!forOpenBal) {
				lastRowMap.put("totalOpenBal", FormatNumber
						.formatNumber(totalOpenBal));
				lastRowMap.put("totalCash", FormatNumber
						.formatNumber(totalCash));
				lastRowMap.put("totalChq", FormatNumber.formatNumber(totalChq));
				lastRowMap.put("totalChqRet", FormatNumber
						.formatNumber(totalChqRet));
				lastRowMap.put("totalCredit", FormatNumber
						.formatNumber(totalCredit));
				lastRowMap.put("totalDebit", FormatNumber
						.formatNumber(totalDebit));
				lastRowMap.put("totalDrawSale", FormatNumber
						.formatNumber(totalDrawSale));
				lastRowMap.put("totalDrawSaleRefund", FormatNumber
						.formatNumber(totalDrawSaleRefund));
				lastRowMap.put("totalDrawPwt", FormatNumber
						.formatNumber(totalDrawPwt));
				lastRowMap.put("totalScratchSale", FormatNumber
						.formatNumber(totalScratchSale));
				lastRowMap.put("totalSaleRet", FormatNumber
						.formatNumber(totalSaleRet));
				lastRowMap.put("totalScratchPwt", FormatNumber
						.formatNumber(totalScratchPwt));
				lastRowMap.put("totalRecTotal", FormatNumber
						.formatNumber(totalRecTotal));
				lastRowMap.put("totalDrawTotal", FormatNumber
						.formatNumber(totalDrawTotal));
				lastRowMap.put("totalScratchTotal", FormatNumber
						.formatNumber(totalScratchTotal));

				lastRowMap.put("totalRetailerDeposit", FormatNumber
						.formatNumber(totalRetailerDeposit));
				lastRowMap.put("totalRetailerWithdraw", FormatNumber
						.formatNumber(totalRetailerWithdraw));
				lastRowMap.put("totalNetGaming", FormatNumber
						.formatNumber(totalNetGaming));
				lastRowMap.put("totalolaTotal", FormatNumber
						.formatNumber(totalolaTotal));
				lastRowMap.put("totalRetailerCsSale", FormatNumber
						.formatNumber(totalRetailerCsSale));
				lastRowMap.put("totalRetailerCsSaleRefund", FormatNumber
						.formatNumber(totalRetailerCsSaleRefund));
				lastRowMap.put("totalCsTotal", FormatNumber
						.formatNumber(totalCsTotal));

				lastRowMap.put("totalGrandTotal", FormatNumber
						.formatNumber(totalGrandTotal));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null && !con.isClosed()) {
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return list;
	}

	public int getAgentOrgId() {
		return agentOrgId;
	}

	public Map<Integer, Double> getRetailerOpeningBalance(
			Map<String, Integer> partyIdMap, boolean isDraw, boolean isScratch,
			boolean isOla,boolean isCS, Timestamp deployDate) throws ParseException {
		Map<Integer, Double> myMap = new TreeMap<Integer, Double>();
		this.end = new java.sql.Date(start.getTime());
		this.start = new java.sql.Date(deployDate.getTime());
		List<CollectionReportBean> tempBeanList = getAgentCollectionDetail(
				partyIdMap, isDraw, isScratch, isOla,isCS, true);
		double tempOpenBalTot = 0.0;
		Iterator<CollectionReportBean> it = tempBeanList.iterator();
		while (it.hasNext()) {
			CollectionReportBean tempBean = it.next();
			myMap.put(new Integer(tempBean.getOrgId()), Double
					.parseDouble(tempBean.getOpenBal()));
			tempOpenBalTot += Double.parseDouble(tempBean.getOpenBal());
		}
		totalOpenBal = FormatNumber.formatNumber(tempOpenBalTot);
		return myMap;
	}

	/**
	 * Retrieve the retailer details (key-name & value-organization id) from the
	 * database
	 * 
	 * @return map<String, Integer> retailer details
	 */
	public Map<String, Integer> getRetailerOrgId() {
		Map<String, Integer> map = new TreeMap<String, Integer>();

		try {
			con = DBConnect.getConnection(); 
			String orgCodeQry = " name orgCode ";

			
			if ((LMSFilterDispatcher.orgFieldType).equalsIgnoreCase("CODE")) {
				orgCodeQry = "	org_code orgCode  ";
	

			} else if ((LMSFilterDispatcher.orgFieldType)
					.equalsIgnoreCase("CODE_NAME")) {
				orgCodeQry = " concat(org_code,'_',name)  orgCode ";
			

			} else if ((LMSFilterDispatcher.orgFieldType)
					.equalsIgnoreCase("NAME_CODE")) {
				orgCodeQry = " concat(name,'_',org_code)  orgCode ";
			

			}
			pstmt = con
					.prepareStatement("select "+orgCodeQry+", organization_id from st_lms_organization_master where organization_type='RETAILER' and parent_id=? order by "+QueryManager.getAppendOrgOrder());
			pstmt.setInt(1, this.agentOrgId);
			ResultSet rss = pstmt.executeQuery();
			logger.debug("get agent org id query :  " + pstmt);
			while (rss.next()) {
				int id = rss.getInt("organization_id");
				String name = rss.getString("orgCode");
				map.put(name, id);
			}
			System.out.println(map);
			rss.close();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null && !con.isClosed()) {
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return map;
	}

	public List<CollectionReportBean> MergeOpenBal(
			List<CollectionReportBean> list, Map<Integer, Double> map) {
		Iterator<CollectionReportBean> it = list.iterator();
		while (it.hasNext()) {
			CollectionReportBean tempBean = it.next();
			if (map.containsKey(tempBean.getOrgId())) {
				tempBean.setOpenBal(map.get(tempBean.getOrgId()) + "");
				Double grndTotal = Double.parseDouble(tempBean.getGrandTotal());
				tempBean.setGrandTotal(Double
						.parseDouble(tempBean.getOpenBal())
						+ grndTotal + "");
				logger.debug("the opening bal of org_id " + tempBean.getOrgId()
						+ " is " + tempBean.getOpenBal());
			}
		}
		return list;
	}

	public void setAgentOrgId(int agentOrgId) {
		this.agentOrgId = agentOrgId;
	}

}
