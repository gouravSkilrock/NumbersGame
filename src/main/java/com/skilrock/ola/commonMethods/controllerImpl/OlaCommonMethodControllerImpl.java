package com.skilrock.ola.commonMethods.controllerImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.beans.OrgPwtLimitBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.GenericException;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.ola.OlaHelper;
import com.skilrock.ola.api.PlayerLotteryIntegration;
import com.skilrock.ola.common.OLAUtility;
import com.skilrock.ola.commonMethods.daoImpl.OlaCommonMethodDaoImpl;
import com.skilrock.ola.javaBeans.OlaWalletBean;
import com.skilrock.ola.userMgmt.daoImpl.OlaPlrRegistrationDaoImpl;
import com.skilrock.ola.userMgmt.javaBeans.OlaPlayerRegistrationRequestBean;

public class OlaCommonMethodControllerImpl {
	
	static Log logger = LogFactory.getLog(OlaCommonMethodControllerImpl.class);
	
	public Map<Integer, OlaWalletBean> getWalletDetails(){
		OlaCommonMethodDaoImpl olaCommonMethodDaoHandler = new OlaCommonMethodDaoImpl();
		return olaCommonMethodDaoHandler.olaWalletDetails();
	}
	
	public boolean verifyRefCode(String refCode,String walletName) throws LMSException, GenericException{
			boolean isSuccess=false;
			Connection con=null;
			try{
			String verificationType=OLAUtility.getVerificationType(walletName);
			if ("PLAYER_LOTTERY".equalsIgnoreCase(walletName)){		
				PlayerLotteryIntegration integrationHelper = new PlayerLotteryIntegration();
				if("MOBILE_NUM".equals(verificationType)){
					isSuccess=integrationHelper.verifyMobileNumber(refCode);				
				}else if("USER_NAME".equalsIgnoreCase(verificationType)){
					isSuccess=integrationHelper.verifyPlrName(refCode);
				}	
			} else if ("TabletGaming".equalsIgnoreCase(walletName) || "GroupRummy".equalsIgnoreCase(walletName) || "KhelPlayRummy".equalsIgnoreCase(walletName) || "ALA_WALLET".equalsIgnoreCase(walletName)) {
				OlaHelper olaHelper=new OlaHelper();
				con=DBConnect.getConnection();
				if("MOBILE_NUM".equals(verificationType)){
					
					//check player in LMS System
					String userName=OlaCommonMethodDaoImpl.getUserNameFromMobileNo(refCode, OLAUtility.getWalletId(walletName),con);
					
					//player Registration
					if(userName==null){
						OlaPlayerRegistrationRequestBean plrDataBean=OlaHelper.getPlayerInfoFromKP(OLAUtility.getWalletId(walletName), refCode,walletName);
						if(plrDataBean==null || plrDataBean.getErrorCode()!=null || plrDataBean.getErrorMsg()!=null){
							isSuccess=false;
							return isSuccess;
						}
						userName=plrDataBean.getUsername();
						plrDataBean.setWalletId(OLAUtility.getWalletId(walletName));
						plrDataBean.setDateOfBirth("0000-00-00");
						plrDataBean.setAddress("");
						plrDataBean.setRegType("DIRECT");
						new OlaPlrRegistrationDaoImpl().registerPlayer(plrDataBean, con);
						
					}
					//Player Deposit/withdrawal availability
					Map<String, String> resMap=olaHelper.verifyPlrName(userName,OLAUtility.getWalletId(walletName),"USER_DEPOSIT_AVAILABILITY");
					logger.info("Khelplay Verify ref Code response"+resMap.toString());
					if("AVAIL".equalsIgnoreCase(resMap.get("userError"))){
						isSuccess=true;		
					}		
				}else if("USER_NAME".equals(verificationType)){
					//check player in LMS System
					boolean isInLMS=OlaCommonMethodDaoImpl.checkRefCodeinOLA(refCode, verificationType, OLAUtility.getWalletId(walletName));
					
					//player Registration
					if(!isInLMS){
						OlaPlayerRegistrationRequestBean plrDataBean=OlaHelper.getPlayerInfoFromKP(OLAUtility.getWalletId(walletName), refCode,walletName);
						if(plrDataBean==null || plrDataBean.getErrorCode()!=null || plrDataBean.getErrorMsg()!=null){
							isSuccess=false;
							return isSuccess;
						}
						refCode=plrDataBean.getUsername();
						plrDataBean.setWalletId(OLAUtility.getWalletId(walletName));
						plrDataBean.setDateOfBirth("0000-00-00");
						plrDataBean.setAddress("");
						plrDataBean.setRegType("DIRECT");
						new OlaPlrRegistrationDaoImpl().registerPlayer(plrDataBean, con);
						
					}
					//Player Deposit/withdrawal availability
					Map<String, String> resMap=olaHelper.verifyPlrName(refCode,OLAUtility.getWalletId(walletName),"USER_DEPOSIT_AVAILABILITY");
					logger.info("Khelplay Verify ref Code response"+resMap.toString());
					if("AVAIL".equalsIgnoreCase(resMap.get("userError"))){
						isSuccess=true;		
					}	
					
				}
			}
			
				if(("PLAYER_LOTTERY".equalsIgnoreCase(walletName)) && isSuccess){
					boolean plrExistInOLA = OlaCommonMethodDaoImpl.checkRefCodeinOLA(refCode, verificationType,OLAUtility.getWalletId(walletName));
					if(!plrExistInOLA){
						registerPlayerAtOLA(refCode,walletName);
					}
				}
			}catch (LMSException e) {
				throw e;
			}catch(SQLException se){
				throw new GenericException(LMSErrors.SQL_EXCEPTION_CODE, se);
			}catch(Exception e){
				throw new GenericException(LMSErrors.GENERAL_EXCEPTION_CODE, e);				
			}finally{
				DBConnect.closeCon(con);
			}
			return isSuccess;
	}
		
	public static int fetchPlayerIdFromRefCode(String refCode, int walletId,Connection con,StringBuilder userName) throws LMSException, GenericException{
		int playerId = 0;
		try{
			String verificationType=OLAUtility.getVerificationType(walletId);
			playerId = OlaCommonMethodDaoImpl.fetchPlayerIdFromRefCode(refCode, walletId,con, verificationType,userName);	
			if(playerId ==0){
				throw new LMSException(LMSErrors.INVALID_PLAYER_ERROR_CODE);
			}
		}catch (LMSException e) {
			throw e;
		}catch(SQLException se){
			throw new GenericException(LMSErrors.SQL_EXCEPTION_CODE, se);
		}catch(Exception e){
			throw new GenericException(LMSErrors.GENERAL_EXCEPTION_CODE, e);
			
		}
		return playerId;
	}
	
	public static boolean checkOrgBalance(int userOrgId, double userAmt, String userType, Connection con) throws LMSException, GenericException {
		boolean isSaleBalanceAval =  false;
		try{
				isSaleBalanceAval = OlaCommonMethodDaoImpl.checkOrgBalance( userOrgId, userAmt, userType, con);			
		}catch (LMSException e) {
			throw e;
		}catch(SQLException se){
			throw new GenericException(LMSErrors.SQL_EXCEPTION_CODE, se);
		}catch(Exception e){
			throw new GenericException(LMSErrors.GENERAL_EXCEPTION_CODE, e);			
		}
		return isSaleBalanceAval;
	}
	
	public static double fetchOLACommOfOrganization(int walletId, int orgId,
			String commType, String orgType, Connection con)
			throws SQLException {
		String fetCommAmount = "";
		if ("NETGAMING".equalsIgnoreCase(commType)) {

			if ("RETAILER".equalsIgnoreCase(orgType)) {
				fetCommAmount = "select a.wallet_id, a.default_ret_net_gaming_comm, b.net_gaming_comm_variance , "
						+ "(ifnull(b.net_gaming_comm_variance, 0)+ a.default_ret_net_gaming_comm) 'total_comm' from"
						+ " ( select wallet_id ,ret_net_gaming_comm as default_ret_net_gaming_comm from st_ola_wallet_master"
						+ " where wallet_id = ?) a left join  ( select retailer_org_id, net_gaming_comm_variance,"
						+ " wallet_id  from st_ola_agent_retailer_comm_variance where wallet_id = ? and  "
						+ "retailer_org_id = ?)   b on a.wallet_id = b.wallet_id ";
				// tem.out.println("PWT Commission Variance.");
			} else if ("AGENT".equalsIgnoreCase(orgType)) {
				fetCommAmount = "select a.wallet_id, a.default_agt_net_gaming_comm, b.net_gaming_comm_variance ,"
						+ " (ifnull(b.net_gaming_comm_variance, 0)+ a.default_agt_net_gaming_comm) 'total_comm' "
						+ "from (select wallet_id ,agt_net_gaming_comm as default_agt_net_gaming_comm "
						+ "from st_ola_wallet_master where wallet_id = ?) a  left join "
						+ "( select agent_org_id, net_gaming_comm_variance, wallet_id  "
						+ "from st_ola_bo_agent_comm_variance where wallet_id = ? and  agent_org_id = ?)"
						+ " b on a.wallet_id = b.wallet_id";
				// tem.out.println("PWT Commision Variance.");
			} else {

			}

		}
		if ("DEPOSIT".equalsIgnoreCase(commType)) {

			if ("RETAILER".equalsIgnoreCase(orgType)) {
				fetCommAmount = "select a.wallet_id, a.default_ret_deposit_comm, b.deposit_comm_variance , "
						+ "(ifnull(b.deposit_comm_variance, 0)+ a.default_ret_deposit_comm) 'total_comm' from"
						+ " ( select wallet_id ,ret_deposit_comm as default_ret_deposit_comm from st_ola_wallet_master"
						+ " where wallet_id = ?) a left join  ( select retailer_org_id, deposit_comm_variance,"
						+ " wallet_id  from st_ola_agent_retailer_comm_variance where wallet_id = ? and  "
						+ "retailer_org_id = ?)   b on a.wallet_id = b.wallet_id ";
				// tem.out.println("PWT Commission Variance.");
			} else if ("AGENT".equalsIgnoreCase(orgType)) {
				fetCommAmount = "select a.wallet_id, a.default_agt_deposit_comm, b.deposit_comm_variance ,"
						+ " (ifnull(b.deposit_comm_variance, 0)+ a.default_agt_deposit_comm) 'total_comm' "
						+ "from (select wallet_id ,agt_deposit_comm as default_agt_deposit_comm "
						+ "from st_ola_wallet_master where wallet_id = ?) a  left join "
						+ "( select agent_org_id, deposit_comm_variance, wallet_id  "
						+ "from st_ola_bo_agent_comm_variance where wallet_id = ? and  agent_org_id = ?)"
						+ " b on a.wallet_id = b.wallet_id";
				// tem.out.println("PWT Commision Variance.");
			} else {

			}

		}
		if ("WITHDRAWAL".equalsIgnoreCase(commType)) {

			if ("RETAILER".equalsIgnoreCase(orgType)) {
				fetCommAmount = "select a.wallet_id, a.default_ret_withdrawl_comm, b.withdrawl_comm_variance , "
						+ "(ifnull(b.withdrawl_comm_variance, 0)+ a.default_ret_withdrawl_comm) 'total_comm' from"
						+ " ( select wallet_id ,ret_withdrawl_comm as default_ret_withdrawl_comm from st_ola_wallet_master"
						+ " where wallet_id = ?) a left join  ( select retailer_org_id, withdrawl_comm_variance,"
						+ " wallet_id  from st_ola_agent_retailer_comm_variance where wallet_id = ? and  "
						+ "retailer_org_id = ?)   b on a.wallet_id = b.wallet_id ";
				// tem.out.println("PWT Commission Variance.");
			} else if ("AGENT".equalsIgnoreCase(orgType)) {
				fetCommAmount = "select a.wallet_id, a.default_agt_withdrawl_comm, b.withdrawl_comm_variance ,"
						+ " (ifnull(b.withdrawl_comm_variance, 0)+ a.default_agt_withdrawl_comm) 'total_comm' "
						+ "from (select wallet_id ,agt_withdrawl_comm as default_agt_withdrawl_comm "
						+ "from st_ola_wallet_master where wallet_id = ?) a  left join "
						+ "( select agent_org_id, withdrawl_comm_variance, wallet_id  "
						+ "from st_ola_bo_agent_comm_variance where wallet_id = ? and  agent_org_id = ?)"
						+ " b on a.wallet_id = b.wallet_id";
				// tem.out.println("PWT Commision Variance.");
			} else {
			}

		}

		PreparedStatement fetCommAmountPstmt = con
				.prepareStatement(fetCommAmount);
		fetCommAmountPstmt.setInt(1, walletId);
		fetCommAmountPstmt.setInt(2, walletId);
		fetCommAmountPstmt.setInt(3, orgId);
		ResultSet rs = fetCommAmountPstmt.executeQuery();
		double commAmt = 0.0;
		if (rs.next()) {
			commAmt = rs.getDouble("total_comm");
		}
		// logger.debug(" commAmt = " + commAmt + " ,   fetCommAmountPStmt = "
		// + fetCommAmountPstmt);
		// tem.out.println(" commAmt = " + commAmt + " , fetCommAmountPStmt = "
		// + fetCommAmountPstmt);
		return commAmt;
	}
	
	public static OrgPwtLimitBean fetchPwtLimitsOfOrgnization(int organizationId,
			Connection connection) throws SQLException, LMSException {
		PreparedStatement pstmt = null;
		ResultSet result = null;
		OrgPwtLimitBean bean = null;
		String query = "select aa.organization_id, verification_limit, approval_limit, pay_limit, scrap_limit,ola_deposit_limit ,ola_withdrawal_limit, bb.pwt_scrap from st_lms_oranization_limits aa, st_lms_organization_master bb where  aa.organization_id = bb.organization_id and  aa.organization_id = ?";
		pstmt = connection.prepareStatement(query);
		pstmt.setInt(1, organizationId);
		result = pstmt.executeQuery();
		// tem.out.println("query that fetch limit details = " + pstmt);
		if (result.next()) {
			bean = new OrgPwtLimitBean();
			bean.setOrganizationId(organizationId);
			bean.setVerificationLimit(result.getDouble("verification_limit"));
			bean.setApprovalLimit(result.getDouble("approval_limit"));
			bean.setPayLimit(result.getDouble("pay_limit"));
			bean.setScrapLimit(result.getDouble("scrap_limit"));
			bean.setOlaDepositLimit(result.getDouble("ola_deposit_limit"));
			bean.setOlaWithdrawlLimit(result.getDouble("ola_withdrawal_limit"));
			bean.setIsPwtAutoScrap(result.getString("pwt_scrap"));
		}else{
			throw new LMSException(LMSErrors.INV_PWT_LIMITS_ERROR_CODE);
		}
		return bean;
	}
	
	public static boolean affiliatePlrBinding(String playerRef,String requestType,String depositAnyWhere,int plrId,UserInfoBean userBean,int walletId,Connection con)throws GenericException, LMSException {
		boolean isPlayerBind = false;
		try{
			int refAffiliateId = OlaCommonMethodDaoImpl.checkPlrAffiliateMapping(con, plrId,walletId);
			logger.info("refAffiliateId" + refAffiliateId);
			boolean PlrExist = false;
				if (refAffiliateId >0) {
					PlrExist = true;
				} 
				if (PlrExist) {
					if ("YES".equalsIgnoreCase(depositAnyWhere)) {
						isPlayerBind = true;
					} else {
						// check mapping
						if (refAffiliateId == userBean.getUserId()) {
							isPlayerBind = true;
						} else {
							throw new LMSException(LMSErrors.AFFILIATE_PLAYER_MAPPING_ERROR_CODE);
						}
					}
				} else {
					if("DEPOSIT".equalsIgnoreCase(requestType) && ("TabletGaming".equalsIgnoreCase(OLAUtility.getWalletName(walletId)) ||"GroupRummy".equalsIgnoreCase(OLAUtility.getWalletName(walletId)) ||"KhelPlayRummy".equalsIgnoreCase(OLAUtility.getWalletName(walletId)) || "ALA_WALLET".equalsIgnoreCase(OLAUtility.getWalletName(walletId)))){
						boolean isBind =new com.skilrock.lms.coreEngine.ola.common.OLAUtility().bindPlrAtKpRummy(playerRef,userBean.getUserOrgId(),walletId);
						if(isBind){
							OlaCommonMethodDaoImpl.bindPlrNAffiliate(con, plrId,userBean,walletId);
							//con.commit();
							isPlayerBind = true;
						}else{
							throw new LMSException(0,LMSErrors.PLAYER_BINDING_ERROR_MESSAGE);
						}
						
					} else if("WITHDRAWAL".equalsIgnoreCase(requestType) && "TabletGaming".equalsIgnoreCase(OLAUtility.getWalletName(walletId))){
						throw new LMSException(LMSErrors.AFFILIATE_PLAYER_MAPPING_ERROR_CODE);
					}else{
						// means player does not exists in OLA System
						// hence Bind player with affiliate in OLA DB
						OlaCommonMethodDaoImpl.bindPlrNAffiliate(con, plrId,userBean,walletId);
						//con.commit();
						isPlayerBind = true;
					}
						
					
					
				}
		}catch (LMSException e) {
			throw e;
		}catch(SQLException se){
			throw new GenericException(LMSErrors.SQL_EXCEPTION_CODE, se);
		}catch(Exception e){
			throw new GenericException(LMSErrors.GENERAL_EXCEPTION_CODE, e);
		}
		return isPlayerBind;
	}
	
	public boolean verifyUserName(String userName,String walletName) throws LMSException{
		boolean isSuccess=false;
		OlaHelper olaHelper=new OlaHelper();
		Map<String, String> errorMap=null;
		PlayerLotteryIntegration integrationHelper = new PlayerLotteryIntegration();
		if ("PLAYER_LOTTERY".equalsIgnoreCase(walletName)){
				isSuccess=integrationHelper.verifyPlrName(userName);		
		}else if("GroupRummy".equalsIgnoreCase(walletName) || "KhelPlayRummy".equalsIgnoreCase(walletName) ||"TabletGaming".equalsIgnoreCase(walletName) || "ALA_WALLET".equalsIgnoreCase(walletName)){
			errorMap =olaHelper.verifyPlrName(userName, OLAUtility.getWalletId(walletName),"USER_AVAILABILITY");
			logger.info("verify mobile num"+errorMap.toString());
			isSuccess="AVAIL".equalsIgnoreCase(errorMap.get("userError"))?false:true;
		}
		return isSuccess;
	}
	
	public boolean verifyMobileNum(String phoneNum,String walletName) throws LMSException{
		boolean isSuccess=false;
		OlaHelper olaHelper=new OlaHelper();
		Map<String, String> errorMap=null;
		PlayerLotteryIntegration integrationHelper = new PlayerLotteryIntegration();
		if ("PLAYER_LOTTERY".equalsIgnoreCase(walletName)){
				isSuccess=integrationHelper.verifyMobileNumber(phoneNum);		
		}else if("TabletGaming".equalsIgnoreCase(walletName)){
			errorMap =olaHelper.verifyPlrName(phoneNum, OLAUtility.getWalletId(walletName),"USER_AVAILABILITY");
			logger.info("verify mobile num"+errorMap.toString());
			isSuccess="AVAIL".equalsIgnoreCase(errorMap.get("userError"))?false:true;
		}else if("GroupRummy".equalsIgnoreCase(walletName) || "KhelPlayRummy".equalsIgnoreCase(walletName) || "ALA_WALLET".equalsIgnoreCase(walletName)){
			errorMap =olaHelper.verifyPlrName(phoneNum, OLAUtility.getWalletId(walletName),"MOBILE_AVAILABILITY");
			logger.info("verify mobile num"+errorMap.toString());
			isSuccess="AVAIL".equalsIgnoreCase(errorMap.get("userError"))?false:true;
		}
			
		
		return isSuccess;
	}
	
	
	public boolean verifyEmail(String email,String walletName) throws LMSException{
		boolean isSuccess=false;
		OlaHelper olaHelper=new OlaHelper();
		Map<String, String> errorMap=null;
		if("GroupRummy".equalsIgnoreCase(walletName) || "KhelPlayRummy".equalsIgnoreCase(walletName) ||"TabletGaming".equalsIgnoreCase(walletName)){
			errorMap =olaHelper.verifyPlrName(email, OLAUtility.getWalletId(walletName),"EMAIL_AVAILABILITY");
			logger.info("verify mobile num"+errorMap.toString());
			isSuccess="AVAIL".equalsIgnoreCase(errorMap.get("userError"))?false:true;
		}
		return isSuccess;
	}
	
	public void registerPlayerAtOLA(String refCode, String walletName) throws LMSException, GenericException{
		OlaPlayerRegistrationRequestBean plrDataBean = null;
		PlayerLotteryIntegration integrationHelper = new PlayerLotteryIntegration();
		try{
			if ("PLAYER_LOTTERY".equalsIgnoreCase(walletName)){
				plrDataBean = integrationHelper.getPlayerInfo(refCode);
				Connection con = DBConnect.getConnection();
				plrDataBean.setWalletId(OLAUtility.getWalletId(walletName));
				plrDataBean.setDateOfBirth("0000-00-00");
				plrDataBean.setPassword("");
				plrDataBean.setRegType("DIRECT");
				new OlaPlrRegistrationDaoImpl().registerPlayer(plrDataBean, con);
				//con.commit();
				logger.info("Player Registered at OLA successfully !!");
			}
		}catch (LMSException e) {
			throw e;
		}
	}
	
	public static String getPlayerNameFromPlayerId(int plrId) throws GenericException{
		String plrName = null;
		try{
			 plrName = OlaCommonMethodDaoImpl.getPlayerNameFromPlayerId(plrId);
		}catch(SQLException e){
			throw new GenericException(LMSErrors.SQL_EXCEPTION_ERROR_CODE);
		}		
		return plrName;
	}
	
	
	
}
