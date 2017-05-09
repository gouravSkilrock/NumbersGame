package com.skilrock.lms.coreEngine.drawGames.playMgmt;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.skilrock.lms.beans.OrgPwtLimitBean;
import com.skilrock.lms.beans.ServiceRequest;
import com.skilrock.lms.beans.ServiceResponse;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.ConfigurationVariables;
import com.skilrock.lms.common.Utility;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;
import com.skilrock.lms.common.utility.CommonMethods;
import com.skilrock.lms.common.utility.MailSender;
import com.skilrock.lms.common.utility.OrgCreditUpdation;
import com.skilrock.lms.coreEngine.drawGames.common.CommonFunctionsHelper;
import com.skilrock.lms.coreEngine.service.IServiceDelegate;
import com.skilrock.lms.coreEngine.service.ServiceDelegate;
import com.skilrock.lms.coreEngine.service.dge.ServiceMethodName;
import com.skilrock.lms.coreEngine.service.dge.ServiceName;
import com.skilrock.lms.dge.beans.FileUploadBean;
import com.skilrock.lms.dge.beans.GameMasterLMSBean;
import com.skilrock.lms.dge.beans.OfflineTicketBean;
import com.skilrock.lms.web.drawGames.common.Util;

public class OfflineRetSaleHelper {

	public String processOfflineFile(List<FileUploadBean> beanList,
			UserInfoBean userInfoBean,
			Map<Integer, Map<Integer, String>> drawIdTableMap, int uploadedBy,
			String refMarId, String purChannel) {

		Connection con = DBConnect.getConnection();
		ServiceResponse sRes = new ServiceResponse();
		ServiceRequest sReq = new ServiceRequest();
		sReq.setServiceName(ServiceName.DRAWGAME);
		IServiceDelegate delegate = ServiceDelegate.getInstance();
		StringBuilder result = new StringBuilder("");

		try {
			con.setAutoCommit(false);
			uploadTktFile(beanList, drawIdTableMap, uploadedBy);
			for (int i = 0; i < beanList.size(); i++) {
				FileUploadBean fileUploadBean = beanList.get(i);
				String fileStatus = fileUploadBean.getStatus();
				int gameNo = fileUploadBean.getGameNo();
				if (!LMSFilterDispatcher.isOfflineFileApproval) {
					if ("UPLOADED".equalsIgnoreCase(fileStatus)) {

						// Sale Balance Deduct
						Connection newCon = DBConnect.getConnection();
						newCon.setAutoCommit(false);
						saleBalDeductionOffline(userInfoBean, gameNo,
								fileUploadBean.getTicketBeanList(), newCon);

						List<Object> offDgeData = new ArrayList<Object>();
						offDgeData.add(gameNo);
						offDgeData.add(fileUploadBean.getTicketBeanList());
						offDgeData.add(userInfoBean.getUserOrgId() + ","
								+ userInfoBean.getUserId() + ",'"
								+ userInfoBean.getUserType() + "'");
						offDgeData.add("'" + refMarId + "','" + purChannel
								+ "'");
						sReq
								.setServiceMethod(ServiceMethodName.INSERT_QUERY_DGE);
						sReq.setServiceData(offDgeData);

						delegate = ServiceDelegate.getInstance();
						sRes = delegate.getResponse(sReq);
						if (sRes.getIsSuccess()) {
							fileUploadBean.setStatus("SUCCESS");
							newCon.commit();
							newCon.close();
						} else {
							fileUploadBean.setStatus("ERROR");
							newCon.rollback();
							newCon.close();
							// System.out.println("Mail for upload: ERROR");
							sendMailToBo(fileUploadBean.getFile(), ""
									+ fileUploadBean.getStatus(),
									fileUploadBean.getFileName());
						}
					} else if ("LATE_UPLOAD".equalsIgnoreCase(fileStatus)) {
						// System.out.println("Mail for upload: LATE_UPLOAD");
						sendMailToBo(fileUploadBean.getFile(), fileUploadBean
								.getStatus(), fileUploadBean.getFileName());
					}
				}
				String updateQuery = "update st_dg_offline_files_? set status='"
						+ fileUploadBean.getStatus() + "' where reference_no=?";
				PreparedStatement pstmt = con.prepareStatement(updateQuery);
				pstmt.setInt(1, gameNo);
				pstmt.setString(2, fileUploadBean.getRefNo());
				pstmt.executeUpdate();

				result.append(fileUploadBean.getFileName());
				result.append(":");
				result.append(fileUploadBean.getRefNo());
				result.append(":");
				result.append(fileUploadBean.getStatus());
				result.append("|");
				con.commit();
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnect.closeCon(con);
		}
		return result.toString();
	}

	public static synchronized void saleBalDeductionOffline(
			UserInfoBean userInfoBean, int gameNo,
			List<OfflineTicketBean> ticketBeanList, Connection con) {
		double retCommRate = 0.0;
		double agtCommRate = 0.0;
		double levyRate = 0.0;
		double agtLevyRate = 0.0;
		double secDepRate = 0.0;
		double govt_comm = 0.0;
		double vat = 0.0;
		double prize_payout_ratio = 0.0;
		double totalRetNet = 0.0;
		double totalAgtNet = 0.0;
		int gameId = 0;
		OfflineTicketBean ticketBean = null;
		List<OfflineTicketBean> promoTktBean = new ArrayList<OfflineTicketBean>();
		Map<String, String> promoTktNoMap = new HashMap<String, String>();
		try {
			PreparedStatement pstmt = null;

			GameMasterLMSBean gameMasterLMSBean = Util
					.getGameMasterLMSBean(gameNo);
			if (gameMasterLMSBean != null) {
				retCommRate = gameMasterLMSBean.getRetSaleCommRate();
				agtCommRate = gameMasterLMSBean.getAgentSaleCommRate();
				govt_comm = gameMasterLMSBean.getGovtComm();
				vat = gameMasterLMSBean.getVatAmount();
				prize_payout_ratio = gameMasterLMSBean.getPrizePayoutRatio();
				gameId = gameMasterLMSBean.getGameId();
			} else {
				pstmt = con
						.prepareStatement("select * from st_dg_game_master where game_nbr="
								+ gameNo);
				ResultSet gameDetails = pstmt.executeQuery();

				if (gameDetails.next()) {
					retCommRate = gameDetails
							.getDouble("retailer_sale_comm_rate");
					agtCommRate = gameDetails.getDouble("agent_sale_comm_rate");
					govt_comm = gameDetails.getDouble("govt_comm");
					vat = gameDetails.getDouble("vat_amt");
					prize_payout_ratio = gameDetails
							.getDouble("prize_payout_ratio");
					gameId = gameDetails.getInt("game_id");
				}
			}

			// get sale comm variance from util
			retCommRate = Util.getSaleCommVariance(userInfoBean.getUserOrgId(),
					gameId);
			agtCommRate = Util.getSaleCommVariance(userInfoBean
					.getParentOrgId(), gameId);

			StringBuilder insTranMas = new StringBuilder(
					"insert into st_lms_transaction_master(user_type,service_code,interface)values ");
			for (int i = 0, j = ticketBeanList.size(); i < j; i++) {
				insTranMas.append("('RETAILER', 'DG', 'TERMINAL'),");
			}
			insTranMas.deleteCharAt(insTranMas.length() - 1);
			pstmt = con.prepareStatement(insTranMas.toString());
			// System.out.println("*********1**********" + pstmt);
			pstmt.executeUpdate();
			ResultSet rsTrns = pstmt.getGeneratedKeys();
			StringBuilder insRetTranMas = new StringBuilder(
					"insert into st_lms_retailer_transaction_master (transaction_id,retailer_user_id,retailer_org_id,game_id,transaction_date,transaction_type) values");
			StringBuilder retSaleTlb = new StringBuilder(
					"insert into st_dg_ret_sale_?(transaction_id,mrp_amt,retailer_comm,net_amt,agent_comm,agent_net_amt,retailer_org_id,claim_status,good_cause_amt,vat_amt,taxable_sale,game_id,ticket_nbr,ret_sd_amt,agt_vat_amt) values");
			String retTrxMas = "," + userInfoBean.getUserId() + ","
					+ userInfoBean.getUserOrgId() + "," + gameId + ",'"
					+ new Timestamp(new Date().getTime())
					+ "','DG_SALE_OFFLINE'),";
			for (int i = 0, j = ticketBeanList.size(); i < j && rsTrns.next(); i++) {
				int transId = rsTrns.getInt(1);
				insRetTranMas.append("(" + transId + retTrxMas);
				ticketBean = ticketBeanList.get(i);
				ticketBean.setSaleTrxId(transId);
				double retNet = 0.0;
				double agtNet = 0.0;
				double retSdAmt = 0.0;
				double vatAmount = 0.0;
				double agtVatAmt = 0.0;
				double ticketMrp = ticketBean.getTktCost();
				double retComm = 0.0;
				double agtComm = 0.0;
				double goodCauseAmt=0.0;
				if("BENIN".equalsIgnoreCase(Utility.getPropertyValue("COUNTRY_DEPLOYED"))){
					OrgPwtLimitBean orgLimit = new CommonFunctionsHelper().fetchPwtLimitsOfOrgnization(userInfoBean.getUserOrgId(),con);
					OrgPwtLimitBean parentOrgLimit = new CommonFunctionsHelper().fetchPwtLimitsOfOrgnization(userInfoBean.getParentOrgId(),con);
					if(orgLimit != null){
						levyRate = orgLimit.getLevyRate();
						secDepRate = orgLimit.getSecurityDepositRate();
					}
					if(parentOrgLimit != null){
						agtLevyRate = parentOrgLimit.getLevyRate();
					}
					goodCauseAmt=CommonMethods.fmtToTwoDecimal(ticketMrp-(ticketMrp*100)/(100+govt_comm));
					retComm = CommonMethods.fmtToTwoDecimal((ticketMrp-goodCauseAmt)* retCommRate * .01);
					agtComm = retComm;
					vatAmount = CommonMethods.fmtToTwoDecimal(retComm*levyRate*.01);
					retSdAmt = CommonMethods.fmtToTwoDecimal(retComm*secDepRate*.01);
					agtVatAmt = vatAmount;
					retNet = CommonMethods.fmtToTwoDecimal(ticketMrp)- (retComm-vatAmount-retSdAmt);
					agtNet = retNet;	
				} else{
					goodCauseAmt=CommonMethods.fmtToTwoDecimal(ticketMrp* govt_comm * .01);
					retComm = CommonMethods.fmtToTwoDecimal(ticketMrp * retCommRate* .01);
					agtComm = CommonMethods.fmtToTwoDecimal(ticketMrp * agtCommRate* .01);
					retNet = CommonMethods.fmtToTwoDecimal(ticketMrp) - retComm;
					agtNet = CommonMethods.fmtToTwoDecimal(ticketMrp) - agtComm;
					vatAmount = CommonMethods.calculateDrawGameVatPlr(ticketMrp, 0, prize_payout_ratio, govt_comm, vat);
					retSdAmt = 0.0;
					agtVatAmt = CommonMethods.calculateDrawGameVatRet(ticketMrp, 0, prize_payout_ratio, govt_comm, vat);
				}
			
				double taxableSale = CommonMethods.calTaxableSale(ticketMrp, 0,
						prize_payout_ratio, govt_comm, vat);
				totalRetNet += retNet;
				totalAgtNet += agtNet;
				retSaleTlb.append("(").append(transId).append(",").append(ticketMrp).append(",").
					append(retComm).append(",").append(CommonMethods.fmtToTwoDecimal(retNet)).
					append(",").append(agtComm).append(",").append(CommonMethods.fmtToTwoDecimal(agtNet)).
					append(",").append(userInfoBean.getUserOrgId()).append(",'CLAIM_BAL',").
					append(goodCauseAmt).
					append(",").append(CommonMethods.fmtToTwoDecimal(vatAmount)).
					append(",").append(CommonMethods.fmtToTwoDecimal(taxableSale)).
					append(",").append(gameId).append(",").append(ticketBean.getTicketNo()).append(",").
					append(retSdAmt).append(",").append(agtVatAmt).append("),");
				if ("0".equals(ticketBean.getPromoCheck())) {
					promoTktBean.add(ticketBean.getPromoBean());
					promoTktNoMap.put(ticketBean.getTicketNo(), ticketBean
							.getPromoBean().getTicketNo());
				}
			}

			// insert into retailer transaction master
			insRetTranMas.deleteCharAt(insRetTranMas.length() - 1);
			pstmt = con.prepareStatement(insRetTranMas.toString());
			// System.out.println("*********2**********" + pstmt);
			pstmt.executeUpdate();

			// insert in ret draw sale table
			retSaleTlb.deleteCharAt(retSaleTlb.length() - 1);
			pstmt = con.prepareStatement(retSaleTlb.toString());
			pstmt.setInt(1, gameNo);
			// System.out.println("*********3**********" + pstmt);
			pstmt.executeUpdate();

			if (promoTktBean.size() > 0) {
				int promoGameNo = Integer.parseInt(promoTktBean.get(0)
						.getTicketNo().charAt(4)
						+ "");

				StringBuilder promoMappingQry = new StringBuilder(
						"insert into ge_sale_promo_ticket_mapping (promo_id, sale_ticket_nbr, promo_ticket_nbr) values ");

				Iterator<Map.Entry<String, String>> promoEtr = promoTktNoMap
						.entrySet().iterator();
				while (promoEtr.hasNext()) {
					Map.Entry<String, String> pair = promoEtr.next();
					promoMappingQry.append("(1," + pair.getKey() + ","
							+ pair.getValue() + "),");
				}
				promoMappingQry.deleteCharAt(promoMappingQry.length() - 1);
				PreparedStatement mappingPstmt = con
						.prepareStatement(promoMappingQry.toString());
				mappingPstmt.executeUpdate();

				PreparedStatement pstmtPromo = con
						.prepareStatement("select * from st_dg_game_master where game_nbr="
								+ promoGameNo);
				ResultSet promoGameDetails = pstmtPromo.executeQuery();

				if (promoGameDetails.next()) {
					retCommRate = promoGameDetails
							.getDouble("retailer_sale_comm_rate");
					agtCommRate = promoGameDetails
							.getDouble("agent_sale_comm_rate");
					govt_comm = promoGameDetails.getDouble("govt_comm");
					vat = promoGameDetails.getDouble("vat_amt");
					prize_payout_ratio = promoGameDetails
							.getDouble("prize_payout_ratio");
					gameId = promoGameDetails.getInt("game_id");
				}
				insTranMas = new StringBuilder(
						"insert into st_lms_transaction_master(user_type,service_code,interface)values ");
				for (int i = 0, j = promoTktBean.size(); i < j; i++) {
					insTranMas.append("('RETAILER', 'DG', 'TERMINAL'),");
				}
				insTranMas.deleteCharAt(insTranMas.length() - 1);
				pstmt = con.prepareStatement(insTranMas.toString());
				// System.out.println("****promo*****1**********" + pstmt);
				pstmt.executeUpdate();
				rsTrns = pstmt.getGeneratedKeys();
				insRetTranMas = new StringBuilder(
						"insert into st_lms_retailer_transaction_master (transaction_id,retailer_user_id,retailer_org_id,game_id,transaction_date,transaction_type) values");
				retSaleTlb = new StringBuilder(
						"insert into st_dg_ret_sale_?(transaction_id,mrp_amt,retailer_comm,net_amt,agent_comm,agent_net_amt,retailer_org_id,claim_status,good_cause_amt,vat_amt,taxable_sale,game_id,ticket_nbr,ret_sd_amt,agt_vat_amt) values");
				retTrxMas = "," + userInfoBean.getUserId() + ","
						+ userInfoBean.getUserOrgId() + "," + gameId + ",'"
						+ new Timestamp(new Date().getTime())
						+ "','DG_SALE_OFFLINE'),";
				for (int i = 0, j = promoTktBean.size(); i < j && rsTrns.next(); i++) {
					int transId = rsTrns.getInt(1);
					insRetTranMas.append("(" + transId + retTrxMas);
					ticketBean = promoTktBean.get(i);
					ticketBean.setSaleTrxId(transId);
					double ticketMrp = 0.0;// Zero Cause its Promo ticket

					double retNet = 0.0;
					double agtNet = 0.0;
					double retSdAmt = 0.0;
					double vatAmount = 0.0;
					double agtVatAmt = 0.0;
					double retComm = 0.0;
					double agtComm = 0.0;
					double goodCauseAmt=0.0;
					if("BENIN".equalsIgnoreCase(Utility.getPropertyValue("COUNTRY_DEPLOYED"))){
						OrgPwtLimitBean orgLimit = new CommonFunctionsHelper().fetchPwtLimitsOfOrgnization(userInfoBean.getUserOrgId(),con);
						OrgPwtLimitBean parentOrgLimit = new CommonFunctionsHelper().fetchPwtLimitsOfOrgnization(userInfoBean.getParentOrgId(),con);
						
						if(orgLimit != null){
							levyRate = orgLimit.getLevyRate();
							secDepRate = orgLimit.getSecurityDepositRate();
						}
						if(parentOrgLimit != null){
							agtLevyRate = parentOrgLimit.getLevyRate();
						}
						goodCauseAmt=CommonMethods.fmtToTwoDecimal(ticketMrp-(ticketMrp*100)/(100+govt_comm));
						retComm = CommonMethods.fmtToTwoDecimal((ticketMrp-goodCauseAmt)* retCommRate * .01);
						agtComm = retComm;
						vatAmount = CommonMethods.fmtToTwoDecimal(retComm*levyRate*.01);
						retSdAmt = CommonMethods.fmtToTwoDecimal(retComm*secDepRate*.01);
						agtVatAmt = vatAmount;
						retNet = CommonMethods.fmtToTwoDecimal(ticketMrp)- (retComm-vatAmount-retSdAmt);
						agtNet = retNet;
					} else{
						goodCauseAmt=CommonMethods.fmtToTwoDecimal(ticketMrp* govt_comm * .01);
						retComm = CommonMethods.fmtToTwoDecimal(ticketMrp * retCommRate* .01);
						agtComm = CommonMethods.fmtToTwoDecimal(ticketMrp * agtCommRate* .01);
						retNet = CommonMethods.fmtToTwoDecimal(ticketMrp) - retComm;
						agtNet = CommonMethods.fmtToTwoDecimal(ticketMrp) - agtComm;
						vatAmount = CommonMethods.calculateDrawGameVatPlr(ticketMrp, 0, prize_payout_ratio, govt_comm, vat);
						retSdAmt = 0.0;
						agtVatAmt = CommonMethods.calculateDrawGameVatRet(ticketMrp, 0, prize_payout_ratio, govt_comm, vat);
					}
					
					
					double taxableSale = CommonMethods.calTaxableSale(ticketMrp, 0, prize_payout_ratio, govt_comm, vat);

					totalRetNet += retNet;
					totalAgtNet += agtNet;
					retSaleTlb.append("(").append(transId).append(",").append(ticketMrp).append(",").
					append(retComm).append(",").append(CommonMethods.fmtToTwoDecimal(retNet)).
					append(",").append(agtComm).append(",").append(CommonMethods.fmtToTwoDecimal(agtNet)).
					append(",").append(userInfoBean.getUserOrgId()).append(",'CLAIM_BAL',").
					append(goodCauseAmt).
					append(",").append(CommonMethods.fmtToTwoDecimal(vatAmount)).
					append(",").append(CommonMethods.fmtToTwoDecimal(taxableSale)).
					append(",").append(gameId).append(",").append(ticketBean.getTicketNo()).append(",").
					append(retSdAmt).append(",").append(agtVatAmt).append("),");
					
				}
				// insert into retailer transaction master
				insRetTranMas.deleteCharAt(insRetTranMas.length() - 1);
				pstmt = con.prepareStatement(insRetTranMas.toString());
				// System.out.println("****promo*****2**********" + pstmt);
				pstmt.executeUpdate();

				// insert in ret draw sale table
				retSaleTlb.deleteCharAt(retSaleTlb.length() - 1);
				pstmt = con.prepareStatement(retSaleTlb.toString());
				pstmt.setInt(1, promoGameNo);
				// System.out.println("****promo*****3**********" + pstmt);
				pstmt.executeUpdate();
			}

			
			//Now make payment updte method only one
			OrgCreditUpdation.updateOrganizationBalWithValidate(totalRetNet, "CLAIM_BAL", "CREDIT", userInfoBean.getUserOrgId(), userInfoBean.getParentOrgId(), "RETAILER", 0, con);
			
			OrgCreditUpdation.updateOrganizationBalWithValidate(totalAgtNet, "CLAIM_BAL", "CREDIT", userInfoBean.getParentOrgId(),0, "AGENT", 0, con);
		
			
			/*// update st_lms_organization_master for claimable balance for
			// retailer
			CommonFunctionsHelper commHelper = new CommonFunctionsHelper();
			commHelper.updateOrgBalance("CLAIM_BAL", totalRetNet, userInfoBean
					.getUserOrgId(), "CREDIT", con);

			// update st_lms_organization_master for claimable balance for agent
			commHelper.updateOrgBalance("CLAIM_BAL", totalAgtNet, userInfoBean
					.getParentOrgId(), "CREDIT", con);*/

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void uploadTktFile(List<FileUploadBean> beanList,
			Map<Integer, Map<Integer, String>> drawIdTableMap, int uploadedBy) {
		Connection con = null;
		FileUploadBean fileUploadBean = null;
		File file = null;
		String fileStatus = null;
		String fileName = null;
		int gameNo = 0;
		int promoGameNo = 0;
		int gameId = 0;
		int retOrgId = 0;
		int retUserId = 0;
		OfflineTicketBean ticketBean = null;
		List<OfflineTicketBean> offlineTktList = null;
		Set<Integer> activeDrawIDs = null;
		Set<Integer> activePromoDrawIDs = null;
		Set<String> curDrawIds = null;
		Set<Integer> curDrawIdSet = null;
		Set<String> curPromoDrawIds = null;
		Set<Integer> curPromoDrawIdSet = null;

		for (int i = 0; i < beanList.size(); i++) {
			try {
				con = DBConnect.getConnection();
				offlineTktList = new ArrayList<OfflineTicketBean>();

				fileUploadBean = beanList.get(i);
				file = fileUploadBean.getFile();
				fileName = fileUploadBean.getFileName();
				gameNo = fileUploadBean.getGameNo();
				gameId = fileUploadBean.getGameId();
				retOrgId = fileUploadBean.getRetailerOrgId();
				retUserId = fileUploadBean.getRetailerUserId();

				activeDrawIDs = drawIdTableMap.get(gameNo).keySet();
				curDrawIds = new HashSet<String>();
				curDrawIdSet = new HashSet<Integer>();
				curPromoDrawIds = new HashSet<String>();
				curPromoDrawIdSet = new HashSet<Integer>();
				fileStatus = "UPLOADED";
				String selQry = "select reference_no,status from st_dg_offline_files_? where fileName=? and status!='UPLOADED'";
				PreparedStatement pstmt = con.prepareStatement(selQry);
				pstmt.setInt(1, gameNo);
				pstmt.setString(2, fileName);
				ResultSet rs = pstmt.executeQuery();
				if (rs.next()) {
					fileStatus = rs.getString("status");
					fileUploadBean.setRefNo(rs.getString("reference_no"));
				} else {
					if (file.length() < 5) {
						fileStatus = "EMPTY";
					}

					String insertQuery = "insert into st_dg_offline_files_? (game_no,game_id,retailer_user_id,"
							+ "retailer_org_id,upload_time,filename,file,status, uploaded_by) values(?,?,?,?,?,?,?,?,?)";
					pstmt = con.prepareStatement(insertQuery);
					pstmt.setInt(1, gameNo);
					pstmt.setInt(2, gameNo);
					pstmt.setInt(3, gameId);
					pstmt.setInt(4, retUserId);
					pstmt.setInt(5, retOrgId);
					pstmt.setTimestamp(6, new Timestamp(new Date().getTime()));
					pstmt.setString(7, fileName);
					pstmt.setAsciiStream(8, new FileInputStream(file),
							(int) file.length());
					pstmt.setString(9, fileStatus);
					pstmt.setInt(10, uploadedBy);
					pstmt.executeUpdate();
					ResultSet rsKey = pstmt.getGeneratedKeys();

					StringBuilder refNo = null;
					if (rsKey.next()) {
						refNo = new StringBuilder("FILE");
						String key = "00000000" + rsKey.getString(1);
						refNo.append(key.substring(key.length() - 8, key
								.length()));
						String updateQuery = "update st_dg_offline_files_? set reference_no=? where file_id=?";
						PreparedStatement pstmtUpdate = con
								.prepareStatement(updateQuery);
						pstmtUpdate.setInt(1, gameNo);
						pstmtUpdate.setString(2, refNo.toString());
						pstmtUpdate.setInt(3, Integer.parseInt(key));
						pstmtUpdate.executeUpdate();
					}

					fileUploadBean.setRefNo(refNo.toString());
					String gameName = Util.getGameName(gameNo);
					int startRange = 0, endRange = 0;
					if ("LOTTO".equalsIgnoreCase(gameName)) {
						startRange = ConfigurationVariables.LOTTO_START_RANGE;
						endRange = ConfigurationVariables.LOTTO_END_RANGE;
					} else if ("FASTLOTTO".equalsIgnoreCase(gameName)) {
						startRange = ConfigurationVariables.FASTLOTTO_START_RANGE;
						endRange = ConfigurationVariables.FASTLOTTO_END_RANGE;
					} else if ("ZIMLOTTO".equalsIgnoreCase(gameName)) {
						startRange = ConfigurationVariables.ZIMLOTTO_START_RANGE;
						endRange = ConfigurationVariables.ZIMLOTTO_END_RANGE;
					} else if ("ZIMLOTTOTWO".equalsIgnoreCase(gameName)) {
						startRange = ConfigurationVariables.ZIMLOTTOTWO_START_RANGE;
						endRange = ConfigurationVariables.ZIMLOTTOTWO_END_RANGE;
					} else if ("KENO".equalsIgnoreCase(gameName)) {
						startRange = ConfigurationVariables.KENO_START_RANGE;
						endRange = ConfigurationVariables.KENO_END_RANGE;
					} else if ("KENOTWO".equalsIgnoreCase(gameName)) {
						startRange = ConfigurationVariables.KENOTWO_START_RANGE;
						endRange = ConfigurationVariables.KENOTWO_END_RANGE;
					}

					int totalTkt = 0;
					double totalSaleValue = 0;

					if ("UPLOADED".equalsIgnoreCase(fileStatus)) {
						FileInputStream in = new FileInputStream(file);

						byte[] line = new byte[in.available()];
						in.read(line, 0, in.available());
						String fileString = new String(line);
						String recSeparator = fileString.substring(2, 3);
						fileString = fileString.substring(4);
						String dataArr[] = fileString.split(recSeparator);
						Aes encDec = new Aes("RETAILERRETAILER".toCharArray(),
								128);
						for (String element : dataArr) {

							char[] decRec = encDec.decrypt(element
									.toCharArray());
							String str = new String(decRec).trim(); //
							System.out.println("------file on tkt data---"
									+ str);

							String strArr[] = str.split("\\|");
							// create beans here
							if (strArr.length == 6 || strArr.length == 12) {
								fileStatus = "ERROR";
								return;
							}
							totalTkt = totalTkt + 1;
							totalSaleValue = totalSaleValue
									+ Double.parseDouble(strArr[3]);
							ticketBean = new OfflineTicketBean();
							ticketBean.setTicketNo(strArr[0].substring(0,
									strArr[0].length() - 2));
							ticketBean.setPurchaseTime(strArr[1]);
							ticketBean.setGameData(strArr[2]);
							ticketBean
									.setTktCost(Double.parseDouble(strArr[3]));
							ticketBean.setIsAdvancePlay(Integer
									.parseInt(strArr[4]));
							ticketBean.setDrawIdList(Arrays.asList(strArr[5]
									.split(","))); // draw Ids

							curDrawIds.addAll(Arrays.asList(strArr[5]
									.split(",")));

							ticketBean.setPromoCheck(strArr[6]);
							if ("0".equalsIgnoreCase(strArr[6])) {
								OfflineTicketBean promoBean = new OfflineTicketBean();
								promoGameNo = Integer.parseInt(strArr[7]
										.charAt(4)
										+ "");
								promoBean.setTicketNo(strArr[7].substring(0,
										strArr[7].length() - 2));
								promoBean.setPurchaseTime(strArr[8]);
								promoBean.setGameData(strArr[9]);
								promoBean.setTktCost(Double
										.parseDouble(strArr[10]));
								promoBean.setIsAdvancePlay(Integer
										.parseInt(strArr[11]));
								promoBean.setDrawIdList(Arrays
										.asList(strArr[12].split(",")));
								promoBean.setPromoCheck("1");
								ticketBean.setPromoBean(promoBean);
								curPromoDrawIds.addAll(Arrays.asList(strArr[12]
										.split(",")));
							}
							offlineTktList.add(ticketBean);
						}

						String updateQuery = "update st_dg_offline_files_? set totalTicket=?,totalSaleValue=? where reference_no=?";
						PreparedStatement pstmtUpdate = con
								.prepareStatement(updateQuery);
						pstmtUpdate.setInt(1, gameNo);
						pstmtUpdate.setInt(2, totalTkt);
						pstmtUpdate.setDouble(3, totalSaleValue);
						pstmtUpdate.setString(4, refNo.toString());

						fileStatus = validateFileData(offlineTktList, gameNo,
								gameName, startRange, endRange);

						for (String drawId : curDrawIds) {
							curDrawIdSet.add(Integer.parseInt(drawId));
						}
						for (String drawId : curPromoDrawIds) {
							curPromoDrawIdSet.add(Integer.parseInt(drawId));
						}
						activePromoDrawIDs = (promoGameNo != 0) ? drawIdTableMap
								.get(promoGameNo).keySet()
								: null;
						if (activePromoDrawIDs != null) {
							curPromoDrawIdSet.retainAll(activePromoDrawIDs);
						}
						curDrawIdSet.retainAll(activeDrawIDs);
						if ("UPLOADED".equals(fileStatus)
								&& (curDrawIds.size() != curDrawIdSet.size() || curPromoDrawIds
										.size() != curPromoDrawIdSet.size())) {
							fileStatus = "LATE_UPLOAD";
						}

					}
					fileUploadBean.setTicketBeanList(offlineTktList);
				}

			} catch (Exception e) {
				fileStatus = "ERROR";
				e.printStackTrace();
			} finally {
				fileUploadBean.setStatus(fileStatus);
				DBConnect.closeCon(con);
			}
		}
	}

	public static String validateFileData(
			List<OfflineTicketBean> offlineTktList, int gameNo,
			String gameName, int startRange, int endRange) {
		double unitPrice = 0.0;
		boolean fileCheck = false;
		boolean hasBetType = false;
		String gameFamily = Util.getGameType(gameNo);
		if ("KENO".equalsIgnoreCase(gameFamily)
				|| "ZIMLOTTOTWO".equalsIgnoreCase(gameName)) {
			hasBetType = true;
		} else {
			unitPrice = Util.getUnitPrice(gameNo, null);
		}

		for (OfflineTicketBean ticketBean : offlineTktList) {
			if (ticketBean.getTicketNo().length() != 14) {
				fileCheck = true;
				break;
			}
			double ticketMrp = ticketBean.getTktCost();
			if (!hasBetType && ticketMrp != 0.0 && unitPrice != 0.0
					&& ticketMrp % unitPrice != 0.0) {
				fileCheck = true;
				break;
			}
			String ticketDataArr[] = ticketBean.getGameData().split("#");
			boolean panalChk = false;
			for (String panelData : ticketDataArr) {
				String panelDataArr[] = panelData.split(";");
				if (hasBetType) {
					unitPrice = Util.getUnitPrice(gameNo, panelDataArr[0]);
					double panelPrice = Double.parseDouble(panelDataArr[3])
							* unitPrice;
					if (panelPrice % unitPrice != 0.0
							&& Util.validateNumber(startRange, endRange,
									panelDataArr[1].replace(",UL", "").replace(
											",BL", ""), false)) {
						panalChk = true;
						break;
					}
				} else {
					if (startRange != 0
							&& endRange != 0
							&& !Util.validateNumber(startRange, endRange,
									panelDataArr[0], false)) {
						panalChk = true;
						break;
					}
				}
			}

			if (panalChk) {
				fileCheck = true;
				break;
			}

		}

		if (fileCheck) {
			return "ERROR";
		}
		return "UPLOADED";
	}

	public static void sendMailToBo(File file, String errorMsg, String fileName) {
		String FROM = "lms.user@skilrock.com";
		String PASSWORD = "skilrock";
		List<String> to = new ArrayList<String>();
		to.add("support.wgrl@skilrock.com");
		to.add("error.wgrl@skilrock.com");
		String subject = "Offline File Upload Having Problem: " + errorMsg;
		String bodyText = errorMsg;
		try {
			MailSender ms = new MailSender(FROM, PASSWORD, to, subject,
					bodyText, file, fileName);
			ms.setDaemon(true);
			ms.start();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {

		}
	}
}