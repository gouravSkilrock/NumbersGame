package com.skilrock.lms.coreEngine.accMgmt.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.beans.DateBeans;
import com.skilrock.lms.beans.OrgBean;
import com.skilrock.lms.beans.RecieptDetail;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.MySqlQueries;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.db.TableConstants;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;
import com.skilrock.lms.common.utility.GetDate;

public class RecieptSearchActionHelper {
	static Log logger = LogFactory.getLog(RecieptSearchActionHelper.class);
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet result = null;

	public List<OrgBean> getAgentOrgList() {
		List<OrgBean> list = new ArrayList<OrgBean>(0);
		OrgBean Bean = null;
		try {
			conn = DBConnect.getConnection();
			pstmt = conn.prepareStatement(QueryManager
					.getST_RECEIPT_SEARCH_AGENT_ORGID());
			result = pstmt.executeQuery();
			while (result.next()) {
				Bean = new OrgBean();
				Bean.setOrgId(result.getInt(TableConstants.ORG_ID));
				Bean.setOrgName(result.getString(TableConstants.NAME));
				list.add(Bean);
			}
		} catch (SQLException e) {
			logger.error("Exception: " + e);
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				logger.error("Exception: " + e);
				e.printStackTrace();
			}
		}

		// logger.debug("receipt search list : "+list);
		return list;

	}

	private DateBeans getDateBeans(String startDate, String endDate)
			throws LMSException {
		DateBeans dateBeans = new DateBeans();
		SimpleDateFormat utilDateFormat = new SimpleDateFormat("dd-MM-yyyy");

		try {
			dateBeans.setStartDate(utilDateFormat.parse(startDate));
			dateBeans.setFirstdate(new java.sql.Date(utilDateFormat.parse(
					startDate).getTime()));
			dateBeans.setEndDate(utilDateFormat.parse(endDate));
			dateBeans.setLastdate(new java.sql.Date(GetDate.getNextDayDate(
					utilDateFormat.parse(endDate)).getTime()));
			dateBeans.setReportType("");
		} catch (ParseException e) {
			logger.error("Exception: " + e);
			e.printStackTrace();
			throw new LMSException(e);
		}
		return dateBeans;
	}

	public String getDlNoteSeacrhQueryForAgtRet(String recieptnumber,
			String receiptstatus, String usertype, String agentOrgId,
			String retOrgId, String VStartDate, String VEndDate, String TStartDate, String TEndDate)
	throws LMSException {
		String startDate = null;
		String endDate = null;
		StringBuilder DNQuery = null;
		if(VStartDate != null && VEndDate != null && !"".equals(VStartDate)
				&& !"".equals(VEndDate)){
			startDate = VStartDate;
			endDate = VEndDate;
		}
		else if(TStartDate != null && TEndDate != null && !"".equals(TStartDate)
				&& !"".equals(TEndDate)){
			startDate = TStartDate;
			endDate = TEndDate;
		}

		System.out.println("Receipt Status is::"+receiptstatus);
		
		if("AGENT".equalsIgnoreCase(usertype)){
			DNQuery=new StringBuilder(QueryManager.getST_RECEIPT_SEARCH_DLNOTE());
			if (agentOrgId != null && !"".equals(agentOrgId.trim())) {
				DNQuery.append(" and  umas.organization_id =" + agentOrgId);
			}
		}else{
			DNQuery=new StringBuilder(MySqlQueries.ST_RECEIPT_SEARCH_DLNOTE_RETAILER);
		}
			
		if (startDate != null && endDate != null && !"".equals(startDate)
				&& !"".equals(endDate)) {
			DateBeans dateBeans = getDateBeans(startDate, endDate);
			DNQuery = DNQuery.append(" and ( ind.date>='"
					+ dateBeans.getFirstdate()
					+ "' and ind.date<'"
					+ dateBeans.getLastdate() + "') ");
		}
		DNQuery = DNQuery
		.append(" group by generated_dl_id )aa where aa.parent_id=om.organization_id  and aa.organization_type='"+usertype+"'");


		if (recieptnumber != null && !"".equals(recieptnumber.trim())) {
			DNQuery.append(" and generated_dl_id like '" + recieptnumber + "%' ");
		}


		logger.debug("Delivery Note search query :   " + DNQuery);
		return DNQuery.toString();
	}
	
	public String getReceiptSeacrhQueryForAgtRet(String recieptnumber,
			String receiptstatus, String usertype, String agentOrgId,
			String retOrgId, String VStartDate, String VEndDate, String TStartDate, String TEndDate)
			throws LMSException {
		String startDate = null;
		String endDate = null;
		StringBuilder query = null;
		String appendQuery =" ";
		String appendQueryForMain =" ";
		if(VStartDate != null && VEndDate != null && !"".equals(VStartDate)
				&& !"".equals(VEndDate)){
			startDate = VStartDate;
			endDate = VEndDate;
		}
		else if(TStartDate != null && TEndDate != null && !"".equals(TStartDate)
				&& !"".equals(TEndDate)){
			startDate = TStartDate;
			endDate = TEndDate;
		}
		if((LMSFilterDispatcher.orgFieldType).equalsIgnoreCase("CODE")){
			appendQuery=",umas.org_code orgCode";
			appendQueryForMain= " , orgCode ";
		}else if((LMSFilterDispatcher.orgFieldType).equalsIgnoreCase("CODE_NAME")){
				appendQuery=",concat(umas.org_code,'_',umas.name) orgCode";
				appendQueryForMain= " , orgCode ";
		
		}else if((LMSFilterDispatcher.orgFieldType).equalsIgnoreCase("NAME_CODE")){
			appendQuery=",concat(umas.name,'_',umas.org_code) orgCode";
			appendQueryForMain= " , orgCode ";
			
		}
		
			if ("RETAILER".equalsIgnoreCase(usertype)) {
				query = new StringBuilder("select aa.receipt_id,aa.transaction_type,  aa.transaction_date, aa.voucher_date, aa.generated_id ,aa.receipt_type,aa.organization_type,aa.name,aa.parent_id,om.name 'owner_name' "+appendQueryForMain+" from st_lms_organization_master om,(select borec.voucher_date, borec.receipt_id, borec.generated_id, btm.transaction_date, borec.receipt_type,umas.organization_type,umas.name "+appendQuery+",umas.parent_id,btm.transaction_type  from st_lms_agent_receipts borec,st_lms_organization_master umas,st_lms_agent_receipts_trn_mapping brm, st_lms_agent_transaction_master btm where  brm.receipt_id= borec.receipt_id and borec.party_id=umas.organization_id and btm.transaction_id = brm.transaction_id and borec.party_type='RETAILER'");
				if (retOrgId != null && !"".equals(retOrgId.trim())) {
					query.append(" and  umas.organization_id =" + retOrgId);
				}
				if (startDate != null && endDate != null && !"".equals(startDate)
						&& !"".equals(endDate)) {
					DateBeans dateBeans = getDateBeans(startDate, endDate);
					query = query.append(" and ( borec.voucher_date>='"
							+ dateBeans.getFirstdate()
							+ "' and borec.voucher_date<'"
							+ dateBeans.getLastdate() + "') ");
				}
				query = query
				.append(" group by generated_id )aa where aa.parent_id=om.organization_id ");
				if (agentOrgId != null && !"".equals(agentOrgId.trim())) {
					query.append(" and aa.parent_id=" + agentOrgId);
				}
				logger.debug(" inside retailer-----------------------");
			} else {
				query = new StringBuilder("select aa.receipt_id,aa.transaction_type,  aa.transaction_date, aa.voucher_date, aa.generated_id, aa.receipt_type,aa.organization_type,aa.name,om.name 'owner_name' "+appendQueryForMain+" from st_lms_organization_master om,( select borec.voucher_date, borec.receipt_id, borec.generated_id,  btm.transaction_date, borec.receipt_type,umas.organization_type,umas.name "+appendQuery+",umas.parent_id,btm.transaction_type  from  st_lms_bo_receipts borec, st_lms_organization_master umas,st_lms_bo_receipts_trn_mapping brm, st_lms_bo_transaction_master btm where brm.receipt_id= borec.receipt_id and borec.party_id=umas.organization_id and btm.transaction_id = brm.transaction_id  and borec.party_type='AGENT'");
				if (agentOrgId != null && !"".equals(agentOrgId.trim())) {
					query.append(" and  umas.organization_id =" + agentOrgId);
				}
				if (startDate != null && endDate != null && !"".equals(startDate)
						&& !"".equals(endDate)) {
					DateBeans dateBeans = getDateBeans(startDate, endDate);
					query = query.append(" and ( borec.voucher_date>='"
							+ dateBeans.getFirstdate()
							+ "' and borec.voucher_date<'"
							+ dateBeans.getLastdate() + "') ");
				}
				query = query
				.append(" group by generated_id )aa where aa.parent_id=om.organization_id  and aa.organization_type='AGENT'");
			}
		if (receiptstatus != null && !"".equals(receiptstatus.trim())) {
			query
					.append("  and aa.receipt_type like '" + receiptstatus
							+ "%' ");
		}
		if (recieptnumber != null && !"".equals(recieptnumber.trim())) {
			query.append(" and generated_id like '" + recieptnumber + "%' ");
		}
		// if want to search the reciept of any match case
		// query.append(" and generated_id like '%"+recieptnumber+"%' ")
		
		//Query for OLA_DISTRIBUTOR if the receipt type is OLA_INVOICE
		StringBuilder queryForDistributor=null;
		if ("AGENT".equalsIgnoreCase(usertype)) {
		if ((agentOrgId == null || "".equals(agentOrgId.trim())) && (receiptstatus.equalsIgnoreCase("OLA_INVOICE") || receiptstatus.equalsIgnoreCase(""))) {
			
			queryForDistributor	=new StringBuilder(" union select borec.receipt_id,btm.transaction_type,btm.transaction_date,borec.voucher_date, borec.generated_id,   borec.receipt_type,borec.party_type organization_type,dt.distributor as name ,'KhelPlay Rummy' as owner_name "+appendQuery+" from  st_lms_bo_receipts borec,st_lms_organization_master umas, st_lms_bo_receipts_trn_mapping brm, st_lms_bo_transaction_master btm,st_ola_bo_distributor_transaction dt where brm.receipt_id= borec.receipt_id and dt.transaction_id = brm.transaction_id and btm.transaction_id = brm.transaction_id  and borec.party_type='OLA_DISTRIBUTOR' ");
			if (startDate != null && endDate != null && !"".equals(startDate)
					&& !"".equals(endDate)) {
				DateBeans dateBeans = getDateBeans(startDate, endDate);
				queryForDistributor.append(" and  (borec.voucher_date>= '"+dateBeans.getFirstdate()+"' and borec.voucher_date<='"+dateBeans.getLastdate()+" 23:59:59"+"') ");
			}
			
			if (receiptstatus != null && !"".equals(receiptstatus.trim())) {
				queryForDistributor.append(" and receipt_type='"+receiptstatus+"' " );
			}
			if (recieptnumber != null && !"".equals(recieptnumber.trim())) {
				queryForDistributor.append(" and generated_id='"+recieptnumber+"' " );
			}
			queryForDistributor.append(" group by generated_id ");
			query.append(queryForDistributor.toString());
			
		}
		
		}
		
		logger.debug("Reciept search query :   " + query);
		return query.toString();
	}

	public String getReceiptSeacrhQueryForGovPLR(String recieptnumber,
			String receiptstatus, String usertype, String agentOrgId,
			String user, String VStartDate, String VEndDate, String TStartDate, String TEndDate) throws LMSException {
		StringBuilder query = null;

		if ("GOVT".equalsIgnoreCase(usertype)) {
			if ("BO".equalsIgnoreCase(user)) {
				query = new StringBuilder(
						"select borec.receipt_id, 'GOVERNMENT' as name,  btm.transaction_date, borec.voucher_date, transaction_type, borec.generated_id, btm.party_type,  borec.receipt_type, user_org_id from  st_lms_bo_receipts borec, st_lms_bo_transaction_master btm, st_lms_bo_receipts_trn_mapping brm  where brm.receipt_id= borec.receipt_id and btm.party_type = 'GOVT' and btm.transaction_id = brm.transaction_id ");
			} else {
				query = new StringBuilder(
						"select borec.receipt_id, 'GOVERNMENT' as name, btm.transaction_date, borec.voucher_date, transaction_type, borec.generated_id, btm.party_type,  borec.receipt_type, user_org_id  from  st_lms_agent_receipts borec, st_lms_agent_transaction_master btm, st_lms_agent_receipts_trn_mapping brm  where brm.receipt_id= borec.receipt_id and btm.party_type = 'GOVT' and  btm.user_org_id ="	+ agentOrgId+ " and btm.transaction_id = brm.transaction_id  ");
			}
		} else if ("PLAYER".equalsIgnoreCase(usertype)) {
			if ("BO".equalsIgnoreCase(user)) {
				query = new StringBuilder(
						"select borec.receipt_id, btm.transaction_date, borec.voucher_date, transaction_type, concat(first_name, ' ', last_name) as name,  borec.generated_id, borec.receipt_type, btm.party_type, user_org_id "
						+ " from  st_lms_bo_receipts borec, st_lms_bo_transaction_master btm, st_lms_player_master pm, st_lms_bo_receipts_trn_mapping brm  "
						+ " where brm.receipt_id= borec.receipt_id and btm.party_type = 'PLAYER' and btm.transaction_id = brm.transaction_id and btm.party_id = pm.player_id");
			} else {
				query = new StringBuilder(
						"select borec.receipt_id, btm.transaction_date, borec.voucher_date, transaction_type, "
						+ " concat(first_name, ' ', last_name) as name,  borec.generated_id, borec.receipt_type, "
						+ " btm.party_type, user_org_id from  st_lms_agent_receipts borec, st_lms_agent_transaction_master "
						+ " btm, st_lms_player_master pm, st_lms_agent_receipts_trn_mapping brm  where brm.receipt_id = borec.receipt_id "
						+ " and btm.party_type = 'PLAYER' and  btm.user_org_id ="
						+ agentOrgId
						+ " and  btm.transaction_id = brm.transaction_id "
						+ " and btm.party_id = pm.player_id");

			}
		}
		if(VStartDate != null && VEndDate != null && !"".equals(VStartDate)
				&& !"".equals(VEndDate)){
			String startDate = VStartDate;
			String endDate = VEndDate;
			if (startDate != null && endDate != null && !"".equals(startDate)
					&& !"".equals(endDate)) {
				DateBeans dateBeans = getDateBeans(startDate, endDate);
				query = query.append(" and ( borec.voucher_date>='"
						+ dateBeans.getFirstdate()
						+ "' and borec.voucher_date<'"
						+ dateBeans.getLastdate() + "') ");
			}
		}
		else if(TStartDate != null && TEndDate != null && !"".equals(TStartDate)
				&& !"".equals(TEndDate)){
			String startDate = TStartDate;
			String endDate = TEndDate;
			if (startDate != null && endDate != null && !"".equals(startDate)
					&& !"".equals(endDate)) {
				DateBeans dateBeans = getDateBeans(startDate, endDate);
				query = query.append(" and ( btm.transaction_date>='"
						+ dateBeans.getFirstdate() + "' and btm.transaction_date<'"
						+ dateBeans.getLastdate() + "') ");
			}
		}
		if (receiptstatus != null && !"".equals(receiptstatus.trim())) {
			query.append("  and transaction_type like '%" + receiptstatus
					+ "%' ");
		}
		if (recieptnumber != null && !"".equals(recieptnumber.trim())) {
			query.append(" and generated_id like '" + recieptnumber + "%' ");
		}
		query.append(" group by generated_id ");
		logger.debug("Reciept search query :   " + query);
		return query.toString();
	}

	public List<RecieptDetail> getRecieptDNList(String query) {
		List<RecieptDetail> recList = new ArrayList<RecieptDetail>(0);
		RecieptDetail recieptBean = null;
		try {
			conn = DBConnect.getConnection();
			pstmt = conn.prepareStatement(query);
			result = pstmt.executeQuery();
			String selectField =" ";
			if((LMSFilterDispatcher.orgFieldType).equalsIgnoreCase("NAME")){
				
				selectField =TableConstants.SOM_ORG_NAME;
							
			}else{
				selectField ="orgCode";
				
			}
			while (result.next()) {
				recieptBean = new RecieptDetail();
				recieptBean.setReceiptId(result
						.getString("dl_id"));
				recieptBean.setGenratedId(result.getString("generated_dl_id"));
				// logger.debug("generated id ======
				// "+recieptBean.getGenratedId());
				recieptBean.setRecieptType("DLNOTE");
				recieptBean.setOrgName(result
						.getString(selectField));
				recieptBean.setTransactionDate(result
						.getDate("date"));
				recieptBean.setOrgType(result
						.getString(TableConstants.SOM_ORG_TYPE));
				recieptBean.setOwnerName(result.getString("owner_name"));
				recList.add(recieptBean);
			}
		} catch (SQLException e) {
			logger.error("Exception: " + e);
			e.printStackTrace();
		} finally {
			logger.debug(" closing connection  ");
			try {
				conn.close();
			} catch (SQLException e) {
				logger.error("Exception: " + e);
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		logger.debug("receipt search list : " + recList);
		return recList;
	}
	
	public List<RecieptDetail> getRecieptList(String query,String userType) {
		List<RecieptDetail> recList = new ArrayList<RecieptDetail>(0);
		RecieptDetail recieptBean = null;
		try {
			conn = DBConnect.getConnection();
			String selectField=null;
			if((LMSFilterDispatcher.orgFieldType).equalsIgnoreCase("NAME")){
			
				selectField =TableConstants.SOM_ORG_NAME;
							
			}else{
				selectField ="orgCode";
				
			}
			
			pstmt = conn.prepareStatement(query);
			result = pstmt.executeQuery();
			PreparedStatement pstmt = null;
			String qry = null;
			ResultSet set = null;
			double amt = 0.0;
			List<Long> list = new ArrayList<Long>();
			while (result.next()) {
				list = new ArrayList<Long>();
				if (userType.equalsIgnoreCase("AGENT") && result.getString("receipt_type")!=null && !(result.getString("receipt_type").equalsIgnoreCase("DLCHALLAN"))&& !(result.getString("receipt_type").equalsIgnoreCase("DSRCHALLAN"))) {
					pstmt = conn
							.prepareStatement("select transaction_id from st_lms_bo_receipts_trn_mapping st_lms_bo_receipts_trn_mapping where receipt_id="
									+ result
											.getString(TableConstants.BO_RECEIPT_ID)
									+ "");
					set = pstmt.executeQuery();
					while (set.next()) {
						list.add(set.getLong("transaction_id"));
					}
					qry = getAgentWiseVoucherAmount(list, result
							.getString(TableConstants.BO_RECEIPT_TYPE), result
							.getString("transaction_type"));
						pstmt = conn.prepareStatement(qry);
					set = pstmt.executeQuery();
					if (set.next()) {
						amt = set.getDouble("amount");
					}
				} else if (userType.equalsIgnoreCase("RETAILER") && result.getString("receipt_type")!=null && !(result.getString("receipt_type").equalsIgnoreCase("DLCHALLAN")) && !(result.getString("receipt_type").equalsIgnoreCase("DSRCHALLAN"))) {
					pstmt = conn
							.prepareStatement("select transaction_id from st_lms_agent_receipts_trn_mapping where receipt_id="
									+ result
											.getString(TableConstants.BO_RECEIPT_ID)
									+ "");
					set = pstmt.executeQuery();
					while (set.next()) {
						list.add(set.getLong("transaction_id"));
					}
					qry = getRetailerWiseVoucherAmount(list, result
							.getString(TableConstants.BO_RECEIPT_TYPE), result
							.getString("transaction_type"));
					
					pstmt = conn.prepareStatement(qry);
					set = pstmt.executeQuery();
					if (set.next()) {
						amt = set.getDouble("amount");
					}
				}
				
				recieptBean = new RecieptDetail();
				recieptBean.setAmount(amt);
				recieptBean.setReceiptId(result
			.getString(TableConstants.BO_RECEIPT_ID));
				recieptBean.setGenratedId(result.getString("generated_id"));
				// logger.debug("generated id ======
				// "+recieptBean.getGenratedId());
				recieptBean.setRecieptType(result
						.getString(TableConstants.BO_RECEIPT_TYPE));
		/*	recieptBean.setOrgName(result
						.getString(TableConstants.SOM_ORG_NAME));*/
				recieptBean.setOrgName(result.getString(selectField));
				recieptBean.setTransactionDate(result
						.getDate("voucher_date"));
				recieptBean.setOrgType(result
						.getString(TableConstants.SOM_ORG_TYPE));
				recieptBean.setOwnerName(result.getString("owner_name"));
				recList.add(recieptBean);
			}

		
			
		} catch (SQLException e) {
			logger.error("Exception: " + e);
			e.printStackTrace();
		} finally {
			logger.debug(" closing connection  ");
			try {
				conn.close();
			} catch (SQLException e) {
				logger.error("Exception: " + e);
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		logger.debug("receipt search list : " + recList);
		return recList;
	}

	public List<RecieptDetail> getRecieptListForGovPLR(String query,String userType) {
		List<RecieptDetail> recList = new ArrayList<RecieptDetail>(0);
		RecieptDetail recieptBean = null;
		try {
			conn = DBConnect.getConnection();
			pstmt = conn.prepareStatement(query);
			result = pstmt.executeQuery();
			PreparedStatement pstmt = null;
			String qry = null;
			ResultSet set = null;
			double amt = 0.0;
			List<Long> list = new ArrayList<Long>();
			while (result.next()) {
				list = new ArrayList<Long>();
				if (userType.equalsIgnoreCase("BO") && result.getString("receipt_type")!=null && !(result.getString("receipt_type").equalsIgnoreCase("DLCHALLAN"))) {
					pstmt = conn
							.prepareStatement("select transaction_id from st_lms_bo_receipts_trn_mapping where receipt_id="
									+ result
											.getString(TableConstants.BO_RECEIPT_ID)
									+ "");
					set = pstmt.executeQuery();
					while (set.next()) {
						list.add(set.getLong("transaction_id"));
					}
					qry = getAgentWiseVoucherAmount(list, result
							.getString(TableConstants.BO_RECEIPT_TYPE), result
							.getString("transaction_type"));
				} else if (userType.equalsIgnoreCase("AGENT") && result.getString("receipt_type")!=null && !(result.getString("receipt_type").equalsIgnoreCase("DLCHALLAN"))) {
					pstmt = conn
							.prepareStatement("select transaction_id from st_lms_agent_receipts_trn_mapping where receipt_id="
									+ result
											.getString(TableConstants.BO_RECEIPT_ID)
									+ "");
					set = pstmt.executeQuery();
					while (set.next()) {
						list.add(set.getLong("transaction_id"));
					}
					qry = getRetailerWiseVoucherAmount(list, result
							.getString(TableConstants.BO_RECEIPT_TYPE), result
							.getString("transaction_type"));
				}
				pstmt = conn.prepareStatement(qry);
				set = pstmt.executeQuery();
				if (set.next()) {
					amt = set.getDouble("amount");
				}
				recieptBean = new RecieptDetail();
				recieptBean.setAmount(amt);
				recieptBean.setReceiptId(result
						.getString(TableConstants.BO_RECEIPT_ID));
				recieptBean.setGenratedId(result.getString("generated_id"));
				recieptBean
						.setRecieptType(result.getString("transaction_type"));
				recieptBean.setTransactionDate(result
						.getDate("transaction_date"));
				recieptBean.setOrgName(result.getString("name"));
				recieptBean.setOrgType(result.getString("party_type"));
				recList.add(recieptBean);
			}
		} catch (SQLException e) {
			logger.error("Exception: " + e);
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				logger.error("Exception: " + e);
				e.printStackTrace();
			}
		}
		logger.debug("receipt search list : " + recList);
		return recList;
	}

	public List<OrgBean> getRetailerOrgList(String agentOrgId) {
		List<OrgBean> list = new ArrayList<OrgBean>(0);
		OrgBean Bean = null;
		try {
			conn = DBConnect.getConnection();
			String query = "select name, organization_id from st_lms_organization_master where organization_type='RETAILER' ";
			if (Integer.parseInt(agentOrgId) != -1) {
				query = query + "and parent_id = " + agentOrgId;
			}
			query = query + " order by name";

			pstmt = conn.prepareStatement(query);
			result = pstmt.executeQuery();
			while (result.next()) {
				Bean = new OrgBean();
				Bean.setOrgId(result.getInt(TableConstants.ORG_ID));
				Bean.setOrgName(result.getString(TableConstants.NAME));
				list.add(Bean);
			}
			logger.debug(query + "\nreceipt search list : " + list);
		} catch (SQLException e) {
			logger.error("Exception: " + e);
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				logger.error("Exception: " + e);
				e.printStackTrace();
			}
		}

		return list;
	}
	public String getAgentWiseVoucherAmount(List<Long> transIdList, String receiptType, String transactionType)
	{
		String tableName = null;
		String columnName = null;
		String query = null;
		if(receiptType.equalsIgnoreCase("RECEIPT"))
		{
			if(transactionType.equalsIgnoreCase("cheque"))
			{
				tableName = "st_lms_bo_sale_chq";
				columnName = "cheque_amt";
			}
			else if(transactionType.equalsIgnoreCase("bank_deposit"))
			{
				tableName = "st_lms_bo_bank_deposit_transaction"; 
				columnName = "amount";
			}

			else if(transactionType.equalsIgnoreCase("CASH"))
			{
				tableName = "st_lms_bo_cash_transaction";
				columnName = "amount";
			}

			else if(transactionType.equalsIgnoreCase("PWT")||transactionType.equalsIgnoreCase("PWT_AUTO") )
			{
				tableName = "st_se_bo_pwt";
				columnName = "net_amt";
			}
			/*else if(transactionType.equalsIgnoreCase("OLA_COMMISSION"))
			{
				tableName = "st_ola_bo_agt_commission";
				columnName = "commission_calculated";
			}*/
			else if(transactionType.equalsIgnoreCase("PWT_PLR"))
			{
				tableName = "st_se_direct_player_pwt";
				columnName = "net_amt";
			}
			else if(transactionType.equalsIgnoreCase("DG_PWT_PLR"))
			{
				tableName = "st_dg_bo_direct_plr_pwt";
				columnName = "net_amt";
			}
		}

		else if(receiptType.equalsIgnoreCase("DG_INVOICE")){

			if(transactionType.equalsIgnoreCase("DG_SALE"))
			{
				tableName = "st_dg_bo_sale";
				columnName = "net_amt";
			}

		}
		

		else if(receiptType.equalsIgnoreCase("SLE_INVOICE")){

			if(transactionType.equalsIgnoreCase("SLE_SALE"))
			{
				tableName = "st_sle_bo_sale";
				columnName = "net_amt";
			}

		}

		else if(receiptType.equalsIgnoreCase("CS_INVOICE")){

			if(transactionType.equalsIgnoreCase("CS_SALE"))
			{
				tableName = "st_cs_bo_sale ";
				columnName = "net_amt";
			}

		}

		else if(receiptType.equalsIgnoreCase("OLA_INVOICE")){

			if(transactionType.equalsIgnoreCase("OLA_DEPOSIT"))
			{
				tableName = "st_ola_bo_deposit";
				columnName = "net_amt";
			}

			// check the OLA_CASHCARD_SALE transaction type
			if(transactionType.equalsIgnoreCase("OLA_CASHCARD_SALE"))
			{
				tableName = "st_ola_bo_distributor_transaction";
				columnName = "amount";
			}

		}

		else if(receiptType.equalsIgnoreCase("OLA_RECEIPT")){

			if(transactionType.equalsIgnoreCase("OLA_WITHDRAWL"))
			{
				tableName = "st_ola_bo_withdrawl";
				columnName = "net_amt";
			}
			
			if(transactionType.equalsIgnoreCase("OLA_COMMISSION"))
			{
				tableName = "st_ola_bo_comm";
				columnName = "agt_net_claim_comm";
			}
			
		}


		else if(receiptType.equalsIgnoreCase("INVOICE")){

			if(transactionType.equalsIgnoreCase("SALE"))
			{
				tableName = "st_se_bo_agent_transaction ";
				columnName = "net_amt";
			}
			else if(transactionType.equalsIgnoreCase("LOOSE_SALE"))
			{
				tableName = "st_se_bo_agent_loose_book_transaction";
				columnName = "net_amt";
			}
			else
			{
				tableName = "st_dg_bo_sale";
				columnName = "net_amt";
			}


		}

		else if(receiptType.equalsIgnoreCase("DG_RECEIPT")){

			if(transactionType.equalsIgnoreCase(" DG_PWT")||transactionType.equalsIgnoreCase("DG_PWT_AUTO"))
			{
				tableName = "st_dg_bo_pwt";
				columnName = "net_amt";
			}

		}
		
		else if(receiptType.equalsIgnoreCase("SLE_RECEIPT")){

			if(transactionType.equalsIgnoreCase(" SLE_PWT")||transactionType.equalsIgnoreCase("SLE_PWT_AUTO"))
			{
				tableName = "st_sle_bo_pwt";
				columnName = "net_amt";
			}

		}

		else if(receiptType.equalsIgnoreCase("DR_NOTE")){

			tableName = "st_lms_bo_sale_chq";
			columnName = "cheque_amt";

		}	
		else if(receiptType.equalsIgnoreCase("DR_NOTE_CASH")){

			tableName = "st_lms_bo_debit_note";
			columnName = "amount";

		}

		else if(receiptType.equalsIgnoreCase("CR_NOTE"))
		{
			if(transactionType.equalsIgnoreCase("SALE_RET"))
			{
				tableName = "st_se_bo_agent_transaction";
				columnName = "net_amt";
			}
			else if(transactionType.equalsIgnoreCase("LOOSE_SALE_RET"))
			{
				tableName = "st_se_bo_agent_loose_book_transaction";
				columnName = "net_amt";
			}

			else if(transactionType.equalsIgnoreCase("DG_REFUND_CANCEL")||transactionType.equalsIgnoreCase("DG_REFUND_FAILED") )
			{
				tableName = "st_dg_bo_sale_refund";
				columnName = "net_amt";
			} 

			else if(transactionType.equalsIgnoreCase("CS_CANCEL_SERVER")||transactionType.equalsIgnoreCase("CS_CANCEL_RET") )
			{
				tableName = " st_cs_bo_refund";
				columnName = "net_amt";
			}

			else if(transactionType.equalsIgnoreCase("OLA_DEPOSIT_REFUND"))
			{
				tableName = "st_ola_bo_deposit_refund";
				columnName = "net_amt";
			}


		}

		else if(receiptType.equalsIgnoreCase("CR_NOTE_CASH")){

			tableName = "st_lms_bo_credit_note";
			columnName = "amount";

		}	

		else if(receiptType.equalsIgnoreCase("GOVT_RCPT")){

			if(transactionType.equalsIgnoreCase("VAT"))
			{
				tableName = "st_lms_bo_govt_transaction";
				columnName = "amount";
			}
			else if(transactionType.equalsIgnoreCase("GOVT_COMM"))
			{
				tableName = "st_lms_bo_govt_transaction";
				columnName = "amount";
			}
			else if(transactionType.equalsIgnoreCase("TDS"))
			{
				tableName = "st_se_direct_player_pwt";
				columnName = "pwt_amt";
			}
		}		

		query = "select sum("+columnName+") as amount from "+tableName+" where transaction_id in ("+transIdList.toString().replace("[", "").replace("]", "")+")";	


		return query;
	}
	public String getRetailerWiseVoucherAmount(List<Long> transIdList, String receiptType, String transactionType)
	{
		String tableName = null;
		String columnName = null;
		String query = null;
		if(receiptType.equalsIgnoreCase("RECEIPT"))
		{
			if(transactionType.equalsIgnoreCase("cheque"))
			{
				tableName = "st_lms_agent_sale_chq";
				columnName = "cheque_amt";
			}
			
			else if(transactionType.equalsIgnoreCase("CASH"))
			{
				tableName = "st_lms_agent_cash_transaction";
				columnName = "amount";
			}

			else if(transactionType.equalsIgnoreCase("PWT")||transactionType.equalsIgnoreCase("PWT_AUTO") )
			{
				tableName = "st_se_agent_pwt";
				columnName = "net_amt";
			}
			/*else if(transactionType.equalsIgnoreCase("OLA_COMMISSION"))
			{
				tableName = "st_ola_agt_ret_commission";
				columnName = "commission_calculated";
			}*/

			else if(transactionType.equalsIgnoreCase("PWT_PLR"))
			{
				tableName = "st_se_agt_direct_player_pwt";
				columnName = "net_amt";
			}
			else if(transactionType.equalsIgnoreCase("DG_PWT_PLR"))
			{
				tableName = "st_dg_agt_direct_plr_pwdest";
				columnName = "net_amt";
			}
		}

		else if(receiptType.equalsIgnoreCase("DG_INVOICE")){

			if(transactionType.equalsIgnoreCase("DG_SALE"))
			{
				tableName = "st_dg_agt_sale";
				columnName = "net_amt";
			}

		}
		else if(receiptType.equalsIgnoreCase("SLE_INVOICE")){

			if(transactionType.equalsIgnoreCase("SLE_SALE"))
			{
				tableName = "st_sle_agt_sale";
				columnName = "net_amt";
			}

		}

		else if(receiptType.equalsIgnoreCase("CS_INVOICE")){

			if(transactionType.equalsIgnoreCase("CS_SALE"))
			{
				tableName = "st_cs_agt_sale";
				columnName = "net_amt";
			}

		}

		else if(receiptType.equalsIgnoreCase("OLA_INVOICE")){

			if(transactionType.equalsIgnoreCase("OLA_DEPOSIT"))
			{
				tableName = "st_ola_agt_deposit";
				columnName = "net_amt";
			}

		}

		else if(receiptType.equalsIgnoreCase("OLA_RECEIPT")){

			if(transactionType.equalsIgnoreCase("OLA_WITHDRAWL"))
			{
				tableName = "st_ola_agt_withdrawl";
				columnName = "net_amt";
			}
			if(transactionType.equalsIgnoreCase("OLA_COMMISSION"))
			{
				tableName = "st_ola_agt_comm";
				columnName = "ret_claim_comm";
			}

		}


		else if(receiptType.equalsIgnoreCase("INVOICE")){

			if(transactionType.equalsIgnoreCase("SALE"))
			{
				tableName = "st_se_agent_retailer_transaction";
				columnName = "net_amt";
			}
			else if(transactionType.equalsIgnoreCase("LOOSE_SALE"))
			{
				tableName = "st_se_agent_ret_loose_book_transaction";
				columnName = "net_amt";
			}
			else
			{
				tableName = "st_dg_agt_sale";
				columnName = "net_amt";
			}


		}

		else if(receiptType.equalsIgnoreCase("DG_RECEIPT")){

			if(transactionType.equalsIgnoreCase(" DG_PWT")||transactionType.equalsIgnoreCase("DG_PWT_AUTO"))
			{
				tableName = "st_dg_agt_pwt";
				columnName = "net_amt";
			}

		}
		else if(receiptType.equalsIgnoreCase("SLE_RECEIPT")){

			if(transactionType.equalsIgnoreCase(" SLE_PWT")||transactionType.equalsIgnoreCase("SLE_PWT_AUTO"))
			{
				tableName = "st_sle_agt_pwt";
				columnName = "net_amt";
			}

		}

		else if(receiptType.equalsIgnoreCase("DR_NOTE")){

			tableName = "st_lms_agent_sale_chq";
			columnName = "cheque_amt";

		}	
		else if(receiptType.equalsIgnoreCase("DR_NOTE_CASH")){

			tableName = "st_lms_agent_debit_note";
			columnName = "amount";

		}

		else if(receiptType.equalsIgnoreCase("CR_NOTE"))
		{
			if(transactionType.equalsIgnoreCase("SALE_RET"))
			{
				tableName = "st_se_agent_retailer_transaction";
				columnName = "net_amt";
			}
			else if(transactionType.equalsIgnoreCase("LOOSE_SALE_RET"))
			{
				tableName = "st_se_agent_ret_loose_book_transaction";
				columnName = "net_amt";
			}

			else if(transactionType.equalsIgnoreCase("DG_REFUND_CANCEL")||transactionType.equalsIgnoreCase("DG_REFUND_FAILED") )
			{
				tableName = "st_dg_agt_sale_refund";
				columnName = "bo_net_amt";
			} 

			else if(transactionType.equalsIgnoreCase("CS_CANCEL_SERVER")||transactionType.equalsIgnoreCase("CS_CANCEL_RET") )
			{
				tableName = " st_cs_agt_refund";
				columnName = "net_amt";
			}

			else if(transactionType.equalsIgnoreCase("OLA_DEPOSIT_REFUND"))
			{
				tableName = "st_ola_agt_deposit_refund";
				columnName = "net_amt";
			}


		}

		else if(receiptType.equalsIgnoreCase("CR_NOTE_CASH")){

			tableName = "st_lms_agent_credit_note";
			columnName = "amount";

		}	

		else if(receiptType.equalsIgnoreCase("GOVT_RCPT")){

			if(transactionType.equalsIgnoreCase("VAT"))
			{
				tableName = "st_lms_agent_govt_transaction";
				columnName = "amount";
			}
		}		

		query = "select sum("+columnName+") as amount from "+tableName+" where transaction_id in ("+transIdList.toString().replace("[", "").replace("]", "")+")";	

		
		
		return query;
}
}