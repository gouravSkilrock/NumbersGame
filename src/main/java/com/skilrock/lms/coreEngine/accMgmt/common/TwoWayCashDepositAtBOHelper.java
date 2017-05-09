package com.skilrock.lms.coreEngine.accMgmt.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.web.bankMgmt.Helpers.BankUtil;
import com.skilrock.lms.web.bankMgmt.beans.BranchDetailsBean;

public class TwoWayCashDepositAtBOHelper {
	static Log logger = LogFactory.getLog(TwoWayCashDepositAtBOHelper.class);

	public String twoWayPaymentSubmit(int agtOrgId,int retOrgId, UserInfoBean userBean,
			double amount) throws LMSException {

		String status = "NONE";
		String retOrgType = null;
	//	int agtOrgId = 0;
		int agtUserId = 0;
		String agtOrgType = null;
		int count = 0;
		//String agtOrgName1=null;
	//	int retOrgId=0;
		RetailerPaymentSubmitHelper retailerPaymentHelper = null;
		AgentBankDepositHelper helper = null;
		DebitNoteBoHelper boHelper=null;
		DebitNoteAgentHelper retailerDebitHelper=null;
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			String lmsTransId = null;
			con = DBConnect.getConnection();
			con.setAutoCommit(false);
			pstmt = con.prepareStatement("select retailer_org_id,ret_user_id,ret_org_type,agt_org_id,agt_user_id,agt_org_type,name,agt_name from (select user_id ret_user_id ,om.organization_type ret_org_type,om.organization_id retailer_org_id , om.name from st_lms_organization_master om ,st_lms_user_master um where om.organization_id=um.organization_id and om.organization_id=?) ret, (select om.organization_type agt_org_type,user_id agt_user_id ,om.organization_id agt_org_id,name agt_name from st_lms_organization_master om ,st_lms_user_master um where om.organization_id=um.organization_id and  om.organization_id=? and isrolehead='Y') agt");
			pstmt.setInt(1, retOrgId);
			pstmt.setInt(2,agtOrgId);
			rs = pstmt.executeQuery();
			java.util.Date current_date = new java.sql.Date(new java.util.Date().getTime());
			String depositDate = current_date.toString();
			while (rs.next()) {
				count++;
				//agtOrgId = rs.getInt("agt_org_id");
				agtUserId = rs.getInt("agt_user_id");
				agtOrgType = rs.getString("agt_org_type");
				retOrgType = rs.getString("ret_org_type");
			//	orgName = rs.getString("name");
				// agtOrgName=rs.getString("agt_name");
				// retOrgId=rs.getInt("retailer_org_id");
			}
			if (count == 0 || count > 1) {
				throw new LMSException("PROBLEM WITH THE SERVER CONTACT BACK OFFICE...!!!");
			}
			
			/**
			 * This Line Is Commented For Ghana Build Without FPFCC . For FPFCC This Line Should Be Uncommented 
			 // int userId =userBean.getUserId();
			//	BranchDetailsBean branchDetailsBean = BankUtil.getBankBranchDetails(userId,con);
			
			*/
			//  Hard Coded Entry for Ghana Build Without FPFCC
			BranchDetailsBean branchDetailsBean=new BranchDetailsBean();
			branchDetailsBean.setBankName("HELLO");
			branchDetailsBean.setBranchName("NEWBRANCH");
			if(branchDetailsBean ==null){
				throw new LMSException("PROBLEM Getting Bank Details CONTACT BACK OFFICE...!!!");
			}
			if(amount>0){
			
			helper = new AgentBankDepositHelper();
			String autoGeneRecieptNo = helper.submitBankDepositAmt(agtOrgId,agtOrgType, amount, "111111", branchDetailsBean.getBankName(),
							branchDetailsBean.getBranchName(),depositDate, userBean, con);
			
			logger.info("Generated Id And Transaction Id For AGENT During Bank DEP is :-"+ autoGeneRecieptNo+ " With Organization_id "
							+ agtOrgId);

			retailerPaymentHelper = new RetailerPaymentSubmitHelper();
			String autoGeneRecieptNoAndId = retailerPaymentHelper.retailerCashPaySubmit(0, retOrgType,retOrgId, amount,
							agtUserId, agtOrgId, agtOrgType, con);
			logger.info("Generated Id, Receipt Id And Transaction Id For RETAILER During Cash DEP is :-"
							+ autoGeneRecieptNoAndId
							+ " With Organization_id "
							+ retOrgId);
			
			if ("ERROR".equals(autoGeneRecieptNoAndId)
					|| autoGeneRecieptNoAndId.split("#")[2]
							.equalsIgnoreCase("0")) {
				throw new LMSException(
						"PROBLEM PROCESSING RETAILER PAYMENTS CONTACT BACK OFFICE...!!!");
			}
			lmsTransId = autoGeneRecieptNoAndId.split("#")[2];
			logger.info("LMS Transaction is :- " + lmsTransId);
			/**
			 * This Line Is Commented For Ghana Build Without FPFCC . For FPFCC This Line Should Be Uncommented 
			 
			//	boolean isInserted = BankUtil.branchTrnMapping(userId,Long.parseLong(lmsTransId),branchDetailsBean.getBankId(),branchDetailsBean.getBranchId(),"CASH","Cash reason",con);
			if(!isInserted){
				throw new LMSException("PROBLEM PROCESSING RETAILER PAYMENTS  For Bank or branch CONTACT BACK OFFICE...!!!");
			}
			
			*/
		
			
			
			}else{
				boHelper=new DebitNoteBoHelper();
				String autoGeneRecieptNo=boHelper.doDebitNoteBoHelper(agtOrgId, amount*-1, "DEBIT NOTE VIA TWO WAY PAYMENT", "OTHERS", userBean.getUserOrgId(), userBean.getUserId(), userBean.getUserType(), con);
				logger.info("Generated Id, Receipt Id And Transaction Id For AGENT During Debit Note is :-"
						+ autoGeneRecieptNo
						+ " With Organization_id "
						+ agtOrgId);

				retailerDebitHelper=new DebitNoteAgentHelper();
				// needs to be change orgName should be org id 
				String autoGeneRecieptNoAndId=retailerDebitHelper.doDebitNoteAgtHelper(retOrgId, amount*-1,"DEBIT NOTE VIA TWO WAY PAYMENT" , agtOrgId, agtUserId, agtOrgType, con);
				logger.info("Generated Id, Receipt Id And Transaction Id For Retailer During Debit Note is :-"+ autoGeneRecieptNoAndId+ " With Organization_id "
								+ retOrgId );
				if("LOGIN".equals(autoGeneRecieptNoAndId)){
					throw new LMSException("PROBLEM PROCESSING RETAILER PAYMENTS CONTACT BACK OFFICE...!!!");
				}
				lmsTransId = autoGeneRecieptNoAndId.split("#")[2];
				logger.info("LMS Transaction is :- " + lmsTransId);
				/**
				 * This Line Is Commented For Ghana Build Without FPFCC . For FPFCC This Line Should Be Uncommented 
				 * 
				 * boolean isInserted = BankUtil.branchTrnMapping(userId,Long.parseLong(lmsTransId),branchDetailsBean.getBankId(),branchDetailsBean.getBranchId(),"DR_NOTE_CASH","DR_NOTE_CASH reason",con);
				if(!isInserted){
					throw new LMSException("PROBLEM PROCESSING RETAILER PAYMENTS  For Bank or branch CONTACT BACK OFFICE...!!!");
				}
				 */
				
			}
			con.commit();
		}catch (SQLException se) {
			se.printStackTrace();
			status = "ERROR";
		} catch (LMSException e) {
			status = e.getMessage();
		} finally {
			try {
				
				if(con!=null){
					con.close();
				}
					
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
		return status;
	}


}