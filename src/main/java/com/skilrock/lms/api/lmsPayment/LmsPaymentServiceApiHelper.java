package com.skilrock.lms.api.lmsPayment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.api.beans.DrawDetailsBean;
import com.skilrock.lms.api.beans.PWTApiBean;
import com.skilrock.lms.api.common.TPBoPwtProcessHelper;
import com.skilrock.lms.api.common.TpUtilityHelper;
import com.skilrock.lms.api.lmsPayment.beans.LmsCashPaymentBean;
import com.skilrock.lms.api.lmsPayment.beans.LmsCashPaymentResponseBean;
import com.skilrock.lms.api.lmsPayment.beans.LmsOrgInfoBean;
import com.skilrock.lms.beans.PlayerBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.LMSUtility;
import com.skilrock.lms.coreEngine.accMgmt.common.AgentBankDepositHelper;
import com.skilrock.lms.coreEngine.accMgmt.common.DebitNoteAgentHelper;
import com.skilrock.lms.coreEngine.accMgmt.common.DebitNoteBoHelper;
import com.skilrock.lms.coreEngine.accMgmt.common.RetailerPaymentSubmitHelper;
import com.skilrock.lms.dge.beans.MainPWTDrawBean;


public class LmsPaymentServiceApiHelper {
     static Log logger=LogFactory.getLog(LmsPaymentServiceApiHelper.class);
	public LmsCashPaymentResponseBean depositCashPayment(LmsCashPaymentBean cashPaymentBean,int tpSystemId) throws LMSException {
		LmsCashPaymentResponseBean cashResponseBean=new LmsCashPaymentResponseBean();
		if(TpUtilityHelper.checkDuplicateSystemRefTransId(cashPaymentBean.getRefTransId(),tpSystemId)){
			throw new LMSException("118");
		}
		
		
		PreparedStatement cashPstmt=null;
		Connection con=null;
		ResultSet cashRs=null;
		PreparedStatement bankPstmt=null;
		ResultSet bankRs=null;
		
		String autoGeneRecieptNo = null;
		try{
			int agentOrgId=0;
			String lmsTransId=null;
			con=DBConnect.getConnection();
			con.setAutoCommit(false);
			cashPstmt=con.prepareStatement("select om.organization_id retOrgId ,om.organization_type,user.user_id,user.organization_id,user.organization_type parent_user_type from st_lms_organization_master om inner join st_lms_user_master user on om.parent_id=user.organization_id where org_code = ? and om.organization_type='RETAILER' and isrolehead='Y' AND organization_status <> 'TERMINATE'");
			cashPstmt.setString(1, cashPaymentBean.getOrganizationCode());
			logger.debug("Get Retailer Detail For Cash Deposit="+cashPstmt);
			cashRs=cashPstmt.executeQuery();
			RetailerPaymentSubmitHelper retailerPaymentHelper=new RetailerPaymentSubmitHelper();
			if(cashRs.next()){
				agentOrgId=cashRs.getInt("organization_id");
				
				UserInfoBean userBean=null;
				
				bankPstmt=con.prepareStatement("select om.name,om.organization_type,user.user_id,user.organization_id,user.organization_type parent_user_type from st_lms_organization_master om inner join st_lms_user_master user on om.parent_id=user.organization_id where om.organization_id=? and om.organization_type='AGENT' and isrolehead='Y' and role_id=1  AND organization_status <> 'TERMINATE'");
				bankPstmt.setInt(1, agentOrgId);
				logger.debug("Get Agent Detail For Bank Deposit="+bankPstmt);
				bankRs=bankPstmt.executeQuery();
				AgentBankDepositHelper helper=new AgentBankDepositHelper();
				if(bankRs.next()){
					java.util.Date current_date = new java.sql.Date(new java.util.Date().getTime());
					String depositDate=current_date.toString();
					userBean=new UserInfoBean();
					userBean.setUserId(bankRs.getInt("user_id"));
					userBean.setUserOrgId(bankRs.getInt("organization_id"));
					userBean.setUserType(bankRs.getString("parent_user_type"));
					autoGeneRecieptNo=helper.submitBankDepositAmt(agentOrgId, bankRs.getString("organization_type"),  cashPaymentBean.getAmount(),
						cashPaymentBean.getRefTransId(), cashPaymentBean.getBankName(), cashPaymentBean.getBranchName(), depositDate, userBean,con);
					logger.info(autoGeneRecieptNo);
				}else{
					throw new LMSException("103");
				}
					
				String autoGeneRecieptNoAndId=retailerPaymentHelper.retailerCashPaySubmit(0, cashRs.getString("organization_type"), cashRs.getInt("retOrgId"), cashPaymentBean.getAmount(),cashRs.getInt("user_id"),agentOrgId,cashRs.getString("parent_user_type"),con);
			System.out.println(autoGeneRecieptNoAndId);
			if("ERROR".equals(autoGeneRecieptNoAndId)){
				throw new LMSException("107");
			}
			lmsTransId=autoGeneRecieptNoAndId.split("#")[2];
			}else{
				throw new LMSException("103");
			}
			
			
			TpUtilityHelper.storeTpSystemTxnId(tpSystemId, lmsTransId, cashPaymentBean.getRefTransId(), con);
			TpUtilityHelper.storeTpSystemTxnIdDetail(autoGeneRecieptNo.split("Nxt")[2], lmsTransId, cashPaymentBean, con);
			cashResponseBean.setLmsTranxId(lmsTransId);
			cashResponseBean.setAmount(cashPaymentBean.getAmount());
			con.commit();
		}catch (SQLException se) {
          se.printStackTrace();
          throw new LMSException("500");
		}finally{
			try{
				con.close();
			}catch (SQLException se) {
				se.printStackTrace();
				throw new LMSException("500");
			}
		}
		return cashResponseBean;
	}

	
	public LmsCashPaymentResponseBean depositDebitNotePayment(LmsCashPaymentBean cashPaymentBean,int tpSystemId) throws LMSException {
		LmsCashPaymentResponseBean cashResponseBean=new LmsCashPaymentResponseBean();
		if(TpUtilityHelper.checkDuplicateSystemRefTransId(cashPaymentBean.getRefTransId(),tpSystemId)){
			throw new LMSException("118");
		}
		
		
		PreparedStatement cashPstmt=null;
		Connection con=null;
		ResultSet cashRs=null;
		PreparedStatement debitPstmt=null;
		ResultSet debitRs=null;
		
		String autoGeneRecieptNo = null;
		try{
			int agentOrgId=0;
			String lmsTransId=null;
			con=DBConnect.getConnection();
			con.setAutoCommit(false);
			cashPstmt=con.prepareStatement("select om.organization_id retOrgId,om.organization_type,user.user_id,user.organization_id,user.organization_type parent_user_type from st_lms_organization_master om inner join st_lms_user_master user on om.parent_id=user.organization_id where om.org_code = ? and om.organization_type='RETAILER' and isrolehead='Y' AND organization_status <> 'TERMINATE'");
			cashPstmt.setString(1, cashPaymentBean.getOrganizationCode());
			logger.debug("Get Retailer Detail For Debit Note="+cashPstmt);
			String remarks="API#"+cashPaymentBean.getBankName()+"#"+cashPaymentBean.getBranchName();
			cashRs=cashPstmt.executeQuery();
			DebitNoteAgentHelper retailerDebitHelper=new DebitNoteAgentHelper();
			if(cashRs.next()){
				agentOrgId=cashRs.getInt("organization_id");
				
				debitPstmt=con.prepareStatement("select om.organization_id agtOrgId,om.organization_type,user.user_id,user.organization_id,user.organization_type parent_user_type from st_lms_organization_master om inner join st_lms_user_master user on om.parent_id=user.organization_id where om.organization_id=? and om.organization_type='AGENT' and isrolehead='Y' and role_id=1 AND organization_status <> 'TERMINATE'");
				debitPstmt.setInt(1, agentOrgId);
				logger.debug("Get Agent Detail For Debit Note="+debitPstmt);
				debitRs=debitPstmt.executeQuery();
				DebitNoteBoHelper boHelper=new DebitNoteBoHelper();
				if(debitRs.next()){
						
					autoGeneRecieptNo = boHelper.doDebitNoteBoHelper(debitRs.getInt("agtOrgId"), cashPaymentBean.getAmount()*-1, remarks, "OTHERS", debitRs.getInt("organization_id"), debitRs.getInt("user_id"), debitRs.getString("parent_user_type"), con);
					logger.info(autoGeneRecieptNo);
				}else{
					throw new LMSException("103");
				}
				
				String autoGeneRecieptNoAndId=retailerDebitHelper.doDebitNoteAgtHelper(cashRs.getInt("retOrgId"), cashPaymentBean.getAmount()*-1,remarks , agentOrgId, cashRs.getInt("user_id"), cashRs.getString("parent_user_type"), con);
				//String autoGeneRecieptNoAndId=retailerPaymentHelper.retailerCashPaySubmit(0, cashRs.getString("organization_type"), cashRs.getString("name"), cashPaymentBean.getAmount(),cashRs.getInt("user_id"),agentOrgId,cashRs.getString("parent_user_type"),con);
			System.out.println(autoGeneRecieptNoAndId);
			if("LOGIN".equals(autoGeneRecieptNoAndId)){
				throw new LMSException("108");
			}
			lmsTransId=autoGeneRecieptNoAndId.split("#")[2];
			}else{
				throw new LMSException("103");
			}
			
			TpUtilityHelper.storeTpSystemTxnId(tpSystemId, lmsTransId, cashPaymentBean.getRefTransId(), con);
			TpUtilityHelper.storeTpSystemTxnIdDetail(autoGeneRecieptNo.split("#")[2], lmsTransId, cashPaymentBean, con);
			cashResponseBean.setLmsTranxId(lmsTransId);
			cashResponseBean.setAmount(cashPaymentBean.getAmount());
			con.commit();
		}catch (SQLException se) {
          se.printStackTrace();
          throw new LMSException("500");
		}finally{
			try{
				con.close();
			}catch (SQLException se) {
				se.printStackTrace();
				throw new LMSException("500");
			}
		}
		return cashResponseBean;
	}
	
	
	public LmsOrgInfoBean getOrgInfo(String organizationCode,String orgType) throws LMSException {
		LmsOrgInfoBean orgInfoBean=new LmsOrgInfoBean();
		PreparedStatement orgPstmt=null;
		ResultSet orgRs=null;
		Connection con=null;
		try{
			con=DBConnect.getConnection();
			orgPstmt=con.prepareStatement("select name agt_name,org_code agt_org_code ,retTlb.ret_org_name,retTlb.ret_org_code,retTlb.current_bal,retTlb.user_name from (select name ret_org_name,org_code ret_org_code, available_credit-claimable_bal current_bal,parent_id,user_name from st_lms_organization_master om inner join st_lms_user_master um on om.organization_id=um.organization_id where om.organization_type=? and org_code= ? and isrolehead='Y' AND om.organization_status<>'TERMINATE') retTlb inner join st_lms_organization_master mas on retTlb.parent_id=mas.organization_id");
			orgPstmt.setString(1, orgType);
			orgPstmt.setString(2, organizationCode);
			logger.debug("Get Org Info ="+orgPstmt);
			orgRs=orgPstmt.executeQuery();
			if(orgRs.next()){
				orgInfoBean.setAgtOrgCode(orgRs.getString("agt_org_code"));
				orgInfoBean.setAgtOrgName(orgRs.getString("agt_org_code")+"-"+orgRs.getString("agt_name"));
				orgInfoBean.setRetCurrentBal(orgRs.getDouble("current_bal"));
				orgInfoBean.setRetOrgCode(orgRs.getString("ret_org_code"));
				orgInfoBean.setRetOrgName(orgRs.getString("ret_org_code")+"-"+orgRs.getString("ret_org_name"));
				orgInfoBean.setRetUserName(orgRs.getString("user_name"));
				
			}else{
				throw new LMSException("103");
			}
			
		}catch (SQLException se) {
          se.printStackTrace();
          throw new LMSException("500");
		}finally{
			try{
				con.close();
			}catch (SQLException se) {
				se.printStackTrace();
				throw new LMSException("500");
			}
		}
		return orgInfoBean;
	}

	public LmsCashPaymentResponseBean getPaymentStatus(String refTransId,int tpSystemId) throws LMSException {
		LmsCashPaymentResponseBean cashResponseBean=new LmsCashPaymentResponseBean();
		
		String lmsTransIdAndType=TpUtilityHelper.getLmsTransIdFromTpTransId(refTransId,tpSystemId);
		if(lmsTransIdAndType== null){
			throw new LMSException("118");
		}
		String lmsTransId=lmsTransIdAndType.split("#")[0];
		String userType=lmsTransIdAndType.split("#")[1];
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		PreparedStatement transPstmt=null;
		ResultSet transRs=null;
		Connection con=null;
		try{
			con=DBConnect.getConnection();
			
			if("AGENT".equals(userType)){
				String transactionType="";
				transPstmt=con.prepareStatement("select transaction_type from st_lms_agent_transaction_master where transaction_id=?");
				transPstmt.setString(1, lmsTransId);
				transRs=transPstmt.executeQuery();
				if(transRs.next()){
					transactionType=transRs.getString("transaction_type");
				}else{
					throw new LMSException("101");
				}
				
				if("CASH".equals(transactionType)){
			pstmt=con.prepareStatement("select amount from st_lms_agent_cash_transaction cash inner join st_lms_agent_transaction_master atm on cash.transaction_id=atm.transaction_id where atm.transaction_id=? and transaction_type=?");
				}else{
					pstmt=con.prepareStatement("select -(amount) amount from st_lms_agent_debit_note debit inner join st_lms_agent_transaction_master atm on debit.transaction_id=atm.transaction_id where atm.transaction_id=? and atm.transaction_type=?");

				}
			
			pstmt.setString(1, lmsTransId);
			pstmt.setString(2, transactionType);
			logger.debug("Get Transaction Detail ="+pstmt);
			rs=pstmt.executeQuery();
			if(rs.next()){
				cashResponseBean.setLmsTranxId(lmsTransId);
				cashResponseBean.setAmount(rs.getDouble("amount"));
				
			}
			}
		}catch (SQLException se) {
	          se.printStackTrace();
	          throw new LMSException("500");
			}finally{
				try{
					con.close();
				}catch (SQLException se) {
					se.printStackTrace();
					throw new LMSException("500");
				}
			}
		return cashResponseBean;
	}
	
	public PWTApiBean verifyTicketNo(UserInfoBean userInfoBean,String ticketNbr) throws LMSException{
		PWTApiBean  verifyTicketBean = new PWTApiBean();
		try{
			String pwtAmtForMasterApproval = (String) LMSUtility.sc.getAttribute("PWT_APPROVAL_LIMIT");
			//String highPrizeScheme = (String) LMSUtility.sc.getAttribute("DRAW_GAME_HIGH_PRIZE_SCHEME");
			String refMerchantId = (String) LMSUtility.sc.getAttribute("REF_MERCHANT_ID");
			MainPWTDrawBean  mainPwtBean = new MainPWTDrawBean();
			mainPwtBean.setInpType(Integer.parseInt((String) LMSUtility.sc.getAttribute("InpType")));
			mainPwtBean.setTicketNo(ticketNbr.trim());
			verifyTicketBean.setTicketNo(ticketNbr.trim());
			logger.info("Inside Verification*****ticketNbr***" +ticketNbr);
			TPBoPwtProcessHelper pwtHelper = new TPBoPwtProcessHelper();
			mainPwtBean = pwtHelper.verifyPwt(mainPwtBean, userInfoBean,pwtAmtForMasterApproval,refMerchantId);
			
			if ("ERROR_INVALID".equalsIgnoreCase(mainPwtBean.getStatus())) {
				verifyTicketBean.setSuccess(false);
				verifyTicketBean.setErrorCode("200");//Can Not Verify.Invalid PWT
				logger.info("verifyPwt Response Status " +mainPwtBean.getStatus()+"Error Code 200 Invalid Ticket");
				return verifyTicketBean;				
			} else if ("ERROR".equalsIgnoreCase(mainPwtBean.getStatus())) {
				verifyTicketBean.setSuccess(false);
				verifyTicketBean.setErrorCode("101");
				logger.info("verifyPwt Response Status " +mainPwtBean.getStatus()+"Error Code 101 Invalid Inputs");//Can Not Verify.Invalid PWT
				return verifyTicketBean;	
			} else if ("MAS_APP_REQ"
					.equalsIgnoreCase(mainPwtBean.getStatus())) {
				DrawDetailsBean  drawBean= new DrawDetailsBean();
				drawBean.setDrawDateTime(mainPwtBean.getWinningBeanList().get(0).getDrawWinList().get(0).getDrawDateTime().split("\\*")[0]);
				drawBean.setDrawName(mainPwtBean.getWinningBeanList().get(0).getDrawWinList().get(0).getDrawDateTime().split("\\*")[1]);
				List<DrawDetailsBean> drawBeanList=new ArrayList<DrawDetailsBean>();
				drawBeanList.add(drawBean);
				verifyTicketBean.setDrawBeanList(drawBeanList);				
				verifyTicketBean.setSuccess(false);
				verifyTicketBean.setErrorCode("103");//Invalid Ticket.
				verifyTicketBean.setTotalWinning(mainPwtBean.getTotlticketAmount()+"");
				logger.info("verifyPwt Response Status " +mainPwtBean.getStatus()+"Error Code 103 High Prize Ticket Back Office Approval Required");
				return verifyTicketBean;	
			} else if("NonWin".equalsIgnoreCase(mainPwtBean.getStatus())){
				verifyTicketBean.setSuccess(false);	
				verifyTicketBean.setErrorCode("104");// Non Win
				logger.info("verifyPwt Response Status " +mainPwtBean.getStatus()+"Error Code 104 No Winning In this Ticket");
				return verifyTicketBean;	
			}else if(mainPwtBean.getStatus().equalsIgnoreCase("CLAIMED")){
				verifyTicketBean.setSuccess(false);
				verifyTicketBean.setErrorCode("105");
				logger.info("verifyPwt Response Status " +mainPwtBean.getStatus()+"Error Code 105 PWT Claimed Already ");
				return verifyTicketBean;
			} else if(mainPwtBean.getStatus().equalsIgnoreCase("RES_AWAITED")){
				verifyTicketBean.setSuccess(false);
				verifyTicketBean.setErrorCode("106");
				logger.info("verifyPwt Response Status " +mainPwtBean.getStatus()+"Error Code 106 ResultAwaited ");
				return verifyTicketBean;
			}else if(mainPwtBean.getStatus().equalsIgnoreCase("DRAW_EXPIRED")){
				verifyTicketBean.setSuccess(false);
				verifyTicketBean.setErrorCode("120");
				logger.info("verifyPwt Response Status " +mainPwtBean.getStatus()+"Error Code 120 DRAW_EXPIRED ");
				return verifyTicketBean;
			}else if("SUCCESS"
					.equalsIgnoreCase(mainPwtBean.getStatus())){
				/*//if(mainPwtBean.getWinningBeanList().get(0).getDrawWinList().get(0).getDrawDateTime())
				System.out.println(mainPwtBean.getWinningBeanList().get(0).getDrawWinList().get(0).getDrawDateTime());
				System.out.println(mainPwtBean.getWinningBeanList().get(0).getDrawWinList().get(0).getDrawDateTime().split("\\*")[0]);
				//Dater d = mainPwtBean.getWinningBeanList().get(0).getDrawWinList().get(0).getDrawDateTime().split("*")[0];
				SimpleDateFormat frmt = new SimpleDateFormat("yyyy-MM-dd");
				Date d = frmt.parse(mainPwtBean.getWinningBeanList().get(0).getDrawWinList().get(0).getDrawDateTime().split("\\*")[0]);
				Calendar cal = Calendar.getInstance();
				cal.setTime(d);				
				Calendar calCurrent = Calendar.getInstance();
				System.out.println("Current day: " + calCurrent.get(Calendar.DAY_OF_YEAR));
				System.out.println("Draw day: " + cal.get(Calendar.DAY_OF_YEAR));
				
				if((calCurrent.get(Calendar.DAY_OF_YEAR) - cal.get(Calendar.DAY_OF_YEAR)) <=7){
					verifyTicketBean.setSuccess(true);	
					verifyTicketBean.setErrorCode("120");
					verifyTicketBean.setTotalWinning(mainPwtBean.getTotlticketAmount()+"");
					logger.info("verifyPwt Response Status " +mainPwtBean.getStatus()+" Amount"+mainPwtBean.getTotlticketAmount()+" Error Code 120");
					return verifyTicketBean;
				}else{	*/
				DrawDetailsBean  drawBean= new DrawDetailsBean();
				drawBean.setDrawDateTime(mainPwtBean.getWinningBeanList().get(0).getDrawWinList().get(0).getDrawDateTime().split("\\*")[0]);
				//drawBean.setSymbols(mainPwtBean.getWinningBeanList().get(0).getDrawWinList().get(0).getWinResult());
				drawBean.setDrawName(mainPwtBean.getWinningBeanList().get(0).getDrawWinList().get(0).getDrawDateTime().split("\\*")[1]);
				drawBean.setDrawId(mainPwtBean.getWinningBeanList().get(0).getDrawWinList().get(0).getEventId());
				List<DrawDetailsBean> drawBeanList=new ArrayList<DrawDetailsBean>();
				drawBeanList.add(drawBean);
				verifyTicketBean.setDrawBeanList(drawBeanList);
				verifyTicketBean.setSuccess(true);	
				verifyTicketBean.setErrorCode("100");
				verifyTicketBean.setTotalWinning(String.valueOf(mainPwtBean.getTotlticketAmount() - mainPwtBean.getGovtTaxAmount()));
				logger.info("verifyPwt Response Status " +mainPwtBean.getStatus()+" Amount"+mainPwtBean.getTotlticketAmount()+" Error Code 100");
				return verifyTicketBean;
				//}
			}else{
				verifyTicketBean.setSuccess(false);
				verifyTicketBean.setErrorCode("500");//Can Not Verify.Invalid PWT
				logger.info("verifyPwt Response Status " +mainPwtBean.getStatus()+"Error Code 500");
				return verifyTicketBean;	
			}
				
	
		}catch (Exception e) {
			e.printStackTrace();
			verifyTicketBean.setSuccess(false);
			verifyTicketBean.setErrorCode("500");
			logger.info("Error during Verification : Check Your Inputs");
			throw new LMSException("Error during Verification : Check Your Inputs");
		}
	
	}


	public PWTApiBean pwtVerifyAndPay(UserInfoBean userInfoBean,
			String ticketNbr, double amount, String refTransId,
			PlayerBean plrInfoBean,int tpSystemId ) throws LMSException {
		PWTApiBean pwtApiBean = new PWTApiBean();
		try {
			// check for Duplicate Tp RefTransId
			boolean isDuplicate=TpUtilityHelper.checkDuplicateSystemRefTransId(refTransId, tpSystemId);
			if(isDuplicate){
					pwtApiBean.setSuccess(false);
					pwtApiBean.setErrorCode("118 ");
					//pwtApiBean.setMessage("PWT Payment is already done With This Ref Trans Id");
					logger.info("PWT Payment is already done With This Ref Trans Id  Error Code 118");// Can Not Verify.Invalid PWT
					return pwtApiBean;
				
			}
			
			String pwtAmtForMasterApproval = (String) LMSUtility.sc.getAttribute("PWT_APPROVAL_LIMIT");
			//String pwtAmtForMasterApproval = "5000000";
			// String highPrizeScheme = (String)
			// LMSUtility.sc.getAttribute("DRAW_GAME_HIGH_PRIZE_SCHEME");
			String refMerchantId = (String) LMSUtility.sc
					.getAttribute("REF_MERCHANT_ID");
			String highPrizeAmt = (String) LMSUtility.sc
					.getAttribute("HIGH_PRIZE_AMT");			
			MainPWTDrawBean mainPwtBean = new MainPWTDrawBean();
			mainPwtBean.setInpType(Integer.parseInt((String) LMSUtility.sc.getAttribute("InpType")));
			mainPwtBean.setTicketNo(ticketNbr.trim());
			pwtApiBean.setTicketNo(ticketNbr.trim());
			logger.debug("Inside Verification*****ticketNbr***" + ticketNbr);
			TPBoPwtProcessHelper pwtHelper = new TPBoPwtProcessHelper();
			mainPwtBean = pwtHelper.verifyPwt(mainPwtBean, userInfoBean,
					pwtAmtForMasterApproval, refMerchantId);

			if ("ERROR_INVALID".equalsIgnoreCase(mainPwtBean.getStatus())) {
				pwtApiBean.setSuccess(false);
				pwtApiBean.setErrorCode("200");// Can Not Verify.Invalid PWT
				logger.info("pwtVerifyAndPay Response Status " +mainPwtBean.getStatus()+"Error Code 200 Invalid Ticket");
				return pwtApiBean;
			} else if ("ERROR".equalsIgnoreCase(mainPwtBean.getStatus())) {
				pwtApiBean.setSuccess(false);
				pwtApiBean.setErrorCode("101");
				logger.info("pwtVerifyAndPay Response Status " +mainPwtBean.getStatus()+"Error Code 101 Invalid Inputs");// Can Not Verify.Invalid PWT
				return pwtApiBean;
			} else if ("MAS_APP_REQ".equalsIgnoreCase(mainPwtBean.getStatus())) {
				pwtApiBean.setSuccess(false);
				pwtApiBean.setErrorCode("103");
				logger.info("pwtVerifyAndPay Response Status " +mainPwtBean.getStatus()+"Error Code 103 Back Office Approval Required For Payment");// Back Office Approval Required
				return pwtApiBean;
			}else if("NonWin".equalsIgnoreCase(mainPwtBean.getStatus())){
				pwtApiBean.setSuccess(false);	
				pwtApiBean.setErrorCode("104");// Non Win
				logger.info("pwtVerifyAndPay Response Status " +mainPwtBean.getStatus()+"Error Code 104 No Winning In this Ticket");
				return pwtApiBean;
			}else if(mainPwtBean.getStatus().equalsIgnoreCase("CLAIMED")){
				pwtApiBean.setSuccess(false);
				pwtApiBean.setErrorCode("105");
				logger.info("pwtVerifyAndPay Response Status " +mainPwtBean.getStatus()+"Error Code 105 PWT Claimed Already ");
				return pwtApiBean;
			}  else if((mainPwtBean.getTotlticketAmount() - mainPwtBean.getGovtTaxAmount()) != amount){
				pwtApiBean.setSuccess(false);
				pwtApiBean.setErrorCode("101");// Invalid Amount
				logger.info("pwtVerifyAndPay Response Status " +mainPwtBean.getStatus()+"Error Code 101 Invalid Amount");
				return pwtApiBean;
			} else if(mainPwtBean.getStatus().equalsIgnoreCase("DRAW_EXPIRED")){
				pwtApiBean.setSuccess(false);
				pwtApiBean.setErrorCode("120");
				logger.info("pwtVerifyAndPay Response Status " +mainPwtBean.getStatus()+"Error Code 120 DRAW_EXPIRED ");
				return pwtApiBean;
			}else if ("SUCCESS".equalsIgnoreCase(mainPwtBean.getStatus())) {
				Connection con = DBConnect.getConnection();
				try {
					con.setAutoCommit(false);
					boolean isAnonymous = false;
					if (mainPwtBean.getTotlticketAmount() < Double
							.parseDouble(highPrizeAmt)) {
						isAnonymous = true;
					}

					// Ticket Verified Payment Process Started
					logger.debug("Inside payemnt*****ticketNbr***"
							+ pwtApiBean.getTotalWinning());
					int plrId = pwtHelper.playerRegistration(plrInfoBean,
							isAnonymous, con);
					logger.debug("Player id is " + plrId
							+ " for that approval required");
					if (plrId <= 0) {
						pwtApiBean.setSuccess(false);
						pwtApiBean.setErrorCode("500");
						//pwtApiBean.set send approval request;
						logger.info("Error while player registration process player id is "+plrId);
						throw new LMSException(
								"Error while player registration process player id is"
										+ plrId);
					} else {
						// make PWT Payment
						mainPwtBean.setPwtStatus("Error");
						mainPwtBean =pwtHelper.pwtPayment(mainPwtBean, con, userInfoBean, isAnonymous, plrId,refTransId,tpSystemId);
						if(mainPwtBean.getPwtStatus().equalsIgnoreCase("success")){
							pwtApiBean.setSuccess(true);
							pwtApiBean.setErrorCode("100");
							pwtApiBean.setTotalWinning(String.valueOf(mainPwtBean.getTotlticketAmount() - mainPwtBean.getGovtTaxAmount()));
							pwtApiBean.setLmsTranxIdList(mainPwtBean.getTransactionIdList());
							logger.debug("PWT of Amount" +mainPwtBean.getTotlticketAmount()
									+ " Paid Successfully"+mainPwtBean.getTransactionIdList());
							
						}else{
							pwtApiBean.setSuccess(false);
							pwtApiBean.setErrorCode("500");
							logger.info("pwtVerifyAndPay Response Status " +mainPwtBean.getStatus()+"Error Code 500pay");
						}
						
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (con != null) {
						try {
							con.close();
						} catch (SQLException e) {
							logger.error("Exception: " + e);
							e.printStackTrace();
							throw new LMSException(e);
						}
					}
				}

			} else {
				pwtApiBean.setSuccess(false);
				pwtApiBean.setErrorCode("500");
				logger.info("pwtVerifyAndPay Response Status " +mainPwtBean.getStatus()+"Error Code 500pay");// Can Not Verify.Invalid PWT
				return pwtApiBean;
			}

		} catch (Exception e) {

			e.printStackTrace();
			pwtApiBean.setSuccess(false);
			pwtApiBean.setErrorCode("500");
			throw new LMSException("Error during Payment : Check Your Inputs");

		}

		return pwtApiBean;
	}


	
	
}
