package com.skilrock.lms.coreEngine.reportsMgmt.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
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
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;
import com.skilrock.lms.common.utility.FormatNumber;
import com.skilrock.lms.common.utility.GetDate;
import com.skilrock.lms.web.reportsMgmt.common.CollectionReportAction;

public class CollectionReportHelper {
	public static String formatDate(Date date) {
		String datestr = new SimpleDateFormat("dd/MM/yyyy").format(date);
		System.out.println(datestr);

		return datestr;
	}

	public static void main(String[] args) {
		CollectionReportHelper helper = new CollectionReportHelper(GetDate
				.getDate("Current Week"));
		List<CollectionReportBean> list = null;// helper.getBOCollectionDetail(helper.getAgentOrgId(),true,
		// true);
		for (CollectionReportBean bean : list) {
			System.out.println(bean.getSrNo() + ", " + bean.getName() + ", "
					+ bean.getCash() + ", " + bean.getChq() + ", "
					+ bean.getChqRet() + ", " + bean.getCredit() + ", "
					+ bean.getDebit() + ", " + bean.getDrawSale() + ", "
					+ bean.getDrawPwt() + ", " + bean.getScratchSale() + ", "
					+ bean.getScratchPwt());
		}

	}

	private Connection con = null;
	private DateBeans dateBean;
	private java.sql.Date end;
	public Map<String, String> lastRowMap = new TreeMap<String, String>();
	Log logger = LogFactory.getLog(CollectionReportAction.class);
	private PreparedStatement pstmt = null;

	private ResultSet rs = null;

	private java.sql.Date start;

	private String totalOpenBal;

	public CollectionReportHelper(DateBeans dateBeans) {
		this.dateBean = dateBeans;
		start = new java.sql.Date(dateBeans.getFirstdate().getTime());
		end = new java.sql.Date(dateBeans.getLastdate().getTime());
	}

	public Map<Integer, Double> getAgentOpeningBalance(
			Map<String, Integer> partyIdMap, boolean isDraw, boolean isScratch,
			boolean isCS, Timestamp deployDate) {
		Map<Integer, Double> myMap = new TreeMap<Integer, Double>();
		this.end = new java.sql.Date(start.getTime());
		this.start = new java.sql.Date(deployDate.getTime());
		logger.debug("startdate at the time of agent Open. Bal. fetch" + start);
		logger.debug("enddate at the time of agent Open. Bal. fetch" + end);
		List<CollectionReportBean> tempBeanList = getBOCollectionDetail(
				partyIdMap, isDraw, isScratch, isCS, true);
		Iterator<CollectionReportBean> it = tempBeanList.iterator();
		double tempOpenBalTot = 0.0;
		while (it.hasNext()) {
			CollectionReportBean tempBean = it.next();
			myMap.put(new Integer(tempBean.getOrgId()), Double
					.parseDouble(tempBean.getOpenBal()));
			tempOpenBalTot += Double.parseDouble(tempBean.getOpenBal());
		}
		totalOpenBal = FormatNumber.formatNumber(tempOpenBalTot);
		return myMap;
	}

	public Map<String, Integer> getAgentOrgId() {
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
					.prepareStatement("select "+orgCodeQry+", organization_id from st_lms_organization_master where organization_type='AGENT' order by "+QueryManager.getAppendOrgOrder());

			ResultSet rss = pstmt.executeQuery();
			System.out.println("get agent org id query :  " + pstmt);
			while (rss.next()) {
				int id = rss.getInt("organization_id");
				String name = rss.getString("orgCode");
				map.put(name, id);
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

		return map;
	}

	public List<CollectionReportBean> getBOCollectionDetail(
			Map<String, Integer> partyIdMap, boolean isDraw, boolean isScratch,
			boolean isCS, boolean forOpenBal) {
		List<CollectionReportBean> list = new ArrayList<CollectionReportBean>();
		CollectionReportBean collectionBean = null;
		con = DBConnect.getConnection();
		ResultSet rs1 = null, rs2 = null, rs3 =null;
		System.out.println(con + "size of report list " + partyIdMap.size());
		if (!forOpenBal) {
			this.start = new java.sql.Date(dateBean.getFirstdate().getTime());
			this.end = new java.sql.Date(dateBean.getLastdate().getTime());
			logger.debug("startdate at the time of main report data fetch"
					+ start);
			logger
					.debug("enddate at the time of main report data  fetch"
							+ end);
		}
		// used to add a row of total player pwt amount
		// PwtReportBean totalPlayerPwt=getPlayerPwtDetail(con);
		// if(totalPlayerPwt!=null) list.add(totalPlayerPwt);

		Set<String> partyIdlist = partyIdMap.keySet();
		try {
			// double totalSale=0.0;
			double totalSaleRet = 0.0;
			// double TotalSaleNRetTotal=0.0;
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
			double totalCSSale = 0.0;
			double totalCSSaleRefund = 0.0;
			double totalRecTotal = 0.0;
			double totalScratchTotal = 0.0;
			double totalDrawTotal = 0.0;
			double totalCSTotal = 0.0;
			double totalGrandTotal = 0.0;
			double totalBankDeposit = 0.0;
			int count = 1;

			for (String nameKey : partyIdlist) {
				int partyid = partyIdMap.get(nameKey);
				pstmt = con.prepareStatement(QueryManager
						.getST_COLLECTION_DETAILS());
				pstmt.setInt(1, partyid);
				pstmt.setDate(2, start);
				pstmt.setDate(3, end);

				pstmt.setInt(4, partyid);
				pstmt.setDate(5, start);
				pstmt.setDate(6, end);

				pstmt.setInt(7, partyid);
				pstmt.setDate(8, start);
				pstmt.setDate(9, end);

				pstmt.setInt(10, partyid);
				pstmt.setDate(11, start);
				pstmt.setDate(12, end);

				pstmt.setInt(13, partyid);
				pstmt.setDate(14, start);
				pstmt.setDate(15, end);
				
				pstmt.setInt(16, partyid);
				pstmt.setDate(17, start);
				pstmt.setDate(18, end);

				rs = pstmt.executeQuery();
				System.out
						.println("get accounts collections details query- ==== -"
								+ pstmt);

				if (isScratch) {
					pstmt = con.prepareStatement(QueryManager
							.getST_COLLECTION_DETAILS_FOR_BO_SE());
					pstmt.setInt(1, partyid);
					pstmt.setDate(2, start);
					pstmt.setDate(3, end);

					pstmt.setInt(4, partyid);
					pstmt.setDate(5, start);
					pstmt.setDate(6, end);
					pstmt.setInt(7, partyid);
					pstmt.setDate(8, start);
					pstmt.setDate(9, end);
					System.out
							.println("get scratch collections details query- ==== -"
									+ pstmt);
					rs1 = pstmt.executeQuery();
				}
				if (isDraw) {
					pstmt = con.prepareStatement(QueryManager
							.getST_COLLECTION_DETAILS_FOR_BO_DG());
					pstmt.setInt(1, partyid);
					pstmt.setDate(2, start);
					pstmt.setDate(3, end);

					pstmt.setInt(4, partyid);
					pstmt.setDate(5, start);
					pstmt.setDate(6, end);

					pstmt.setInt(7, partyid);
					pstmt.setDate(8, start);
					pstmt.setDate(9, end);
					System.out
							.println(" get draw collections details query- ==== -"
									+ pstmt);
					rs2 = pstmt.executeQuery();
				}
				if(isCS){
					pstmt = con.prepareStatement(QueryManager
							.getST_COLLECTION_DETAILS_FOR_BO_CS());
					pstmt.setInt(1, partyid);
					pstmt.setDate(2, start);
					pstmt.setDate(3, end);

					pstmt.setInt(4, partyid);
					pstmt.setDate(5, start);
					pstmt.setDate(6, end);

					System.out
							.println(" get cs collections details query- ==== -"
									+ pstmt);
					rs3 = pstmt.executeQuery();
				}
				double recTotal = 0.0;
				if (rs.next()) {
					collectionBean = new CollectionReportBean();
					collectionBean.setSrNo(count);
					collectionBean.setName(nameKey);
					collectionBean.setOrgId(partyid);

					// sale and sale return details
					/*
					 * double sale=rs.getDouble("sale");
					 * collectionBean.setSale(FormatNumber.formatNumber(sale));
					 */

					// double saleNRetTotal=sale-saleRet;
					// collectionBean.setSaleTotal(FormatNumber.formatNumber(saleNRetTotal));
					// totals of the sale and sale return for the last row
					/*
					 * totalSale+=sale; totalSalRet+=saleRet;
					 * TotalSaleNRetTotal+=saleNRetTotal;
					 */

					// received details
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
					double bnkDep = rs.getDouble("bank_deposit");
					collectionBean.setBankDeposit(FormatNumber.formatNumber(bnkDep));

					recTotal = cash + cheque + credit - debit - chqRet + bnkDep;
					collectionBean.setRecTotal(FormatNumber
							.formatNumber(recTotal));
					totalCash += cash;
					totalChq += cheque;
					totalCredit += credit;
					totalDebit += debit;
					totalChqRet += chqRet;
					totalBankDeposit += bnkDep;
					totalRecTotal += recTotal;
					

				}
				double scratchTotal = 0.0;
				double drawTotal = 0.0;
				double CSTotal = 0.0;

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
						collectionBean.setIsCS(true);
						double CSSale = rs3.getDouble("cs_sale");
						collectionBean.setCSSale(FormatNumber
								.formatNumber(CSSale));
						double CSSaleRefund = rs3.getDouble("cs_sale_refund");
						collectionBean.setCSSaleRefund(FormatNumber
								.formatNumber(CSSaleRefund));
						
						totalCSSale += CSSale;
						totalCSSaleRefund += CSSaleRefund;
						CSTotal = CSSale - CSSaleRefund;
						totalCSTotal += CSTotal;
						collectionBean.setCSTotal(FormatNumber
								.formatNumber(CSTotal));
					}
				}

				double grandTotal = drawTotal + scratchTotal + CSTotal - recTotal;
				if (forOpenBal) {
					collectionBean.setOpenBal(FormatNumber
							.formatNumber(grandTotal));
				}
				collectionBean.setGrandTotal(FormatNumber
						.formatPDFNumbers(grandTotal));

				totalGrandTotal += grandTotal;
				// logger.info(rs.getFetchSize() +" **** " +
				// rs2.getFetchSize());
				logger.debug(FormatNumber.formatPDFNumbers(grandTotal)
						+ "in case of for Opening Balance" + forOpenBal
						+ "the grandTotal is: " + grandTotal);
				list.add(collectionBean);
				count += 1;
			}
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
				lastRowMap.put("totalBankDeposit", FormatNumber
						.formatNumber(totalBankDeposit));
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
				lastRowMap.put("totalCSSale", FormatNumber
						.formatNumber(totalCSSale));
				lastRowMap.put("totalCSSaleRefund", FormatNumber
						.formatNumber(totalCSSaleRefund));
				lastRowMap.put("totalRecTotal", FormatNumber
						.formatNumber(totalRecTotal));
				lastRowMap.put("totalDrawTotal", FormatNumber
						.formatNumber(totalDrawTotal));
				lastRowMap.put("totalScratchTotal", FormatNumber
						.formatNumber(totalScratchTotal));
				lastRowMap.put("totalCSTotal", FormatNumber
						.formatNumber(totalCSTotal));
				lastRowMap.put("totalGrandTotal", FormatNumber
						.formatPDFNumbers(totalGrandTotal));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
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

}
