package com.skilrock.lms.coreEngine.virtualSport.pwtMgmt.controllerImpl;

import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.BaseAction;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.CommonMethods;
import com.skilrock.lms.common.utility.LMSUtility;
import com.skilrock.lms.common.utility.OrgCreditUpdation;
import com.skilrock.lms.common.utility.ResponsibleGaming;
import com.skilrock.lms.coreEngine.virtualSport.common.VSErrors;
import com.skilrock.lms.coreEngine.virtualSport.common.VSException;
import com.skilrock.lms.coreEngine.virtualSport.common.controllerImpl.CommonMethodsControllerImpl;
import com.skilrock.lms.coreEngine.virtualSport.common.daoImpl.CommonMethodsDaoImpl;
import com.skilrock.lms.coreEngine.virtualSport.playMgmt.daoImpl.VirtualSportGamePlayDaoImpl;
import com.skilrock.lms.coreEngine.virtualSport.pwtMgmt.controllerImpl.WinningTransactionManagerVS.DirectPlrPwtBean;
import com.skilrock.lms.coreEngine.virtualSport.pwtMgmt.controllerImpl.WinningTransactionManagerVS.PwtInvBean;
import com.skilrock.lms.coreEngine.virtualSport.pwtMgmt.controllerImpl.WinningTransactionManagerVS.TransactionBean;
import com.skilrock.lms.rest.services.bean.TPPwtRequestBean;
import com.skilrock.lms.rest.services.bean.TPPwtResponseBean;
import com.skilrock.lms.web.drawGames.common.Util;

public class PayPwtTicketControllerImpl extends BaseAction{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PayPwtTicketControllerImpl() {
		super(PayPwtTicketControllerImpl.class);
	}

	private static PayPwtTicketControllerImpl classInstance = null;
	private static Logger logger = LoggerFactory
			.getLogger(PayPwtTicketControllerImpl.class);

	public static PayPwtTicketControllerImpl getInstance() {
		if (classInstance == null)
			classInstance = new PayPwtTicketControllerImpl();
		return classInstance;
	}

	public boolean authenticateRequest(String message, String sign) {
		String signature = null;
		MessageDigest md5;
		String security = null;
		try {
			signature = (String) LMSUtility.sc.getAttribute("VIRTUAL_BETTING_AUTHENTICATION_SIGNATURE");
			security = message + signature;
			md5 = MessageDigest.getInstance("MD5");
			byte[] hashMD5 = md5.digest(security.getBytes());
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < hashMD5.length; ++i) {
				sb.append(Integer.toHexString((hashMD5[i] & 0xFF) | 0x100)
						.substring(1, 3));
			}
			if (sb.toString().equalsIgnoreCase(sign)) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}
	
	
	/**
	 * 
	 * @param unitId
	 * @return credit
	 * @throws VSException
	 * @author Rishi
	 */
	public double fetchUserCredit(int unitId) throws VSException{
		double credit = 0.0;
		Connection conn = null;
		try{
			conn = DBConnect.getConnection();
			credit = CommonMethodsDaoImpl.getInstance().fetchVSUserCredit(unitId, conn);
		} catch(VSException le){
			throw le;
		} catch(Exception e){
			e.printStackTrace();
			throw new VSException(VSErrors.GENERAL_EXCEPTION_ERROR_CODE, VSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally{
			DBConnect.closeCon(conn);
		}
		return credit;
	}
	
	
	public TPPwtResponseBean retailerNormalPay(int unitId, TPPwtRequestBean requestBean, String serviceCode, String interfaceType) throws VSException {
		logger.info("-- Inside retailerNormalPay --");
		Connection connection = null;
		UserInfoBean userBean = null;
		TPPwtResponseBean responseBean = new TPPwtResponseBean();
		String userName = null;
		int gameId = 0;
		long transId = 0L;
		double balance = 0L;
		boolean isFraud = false;
		double govtClaimComm = 0.0;
		try {
			userBean = new UserInfoBean();
			String transactionTime = Util.getCurrentTimeString();
			connection = DBConnect.getConnection();

			userName = VirtualSportGamePlayDaoImpl.getInstance().getUserNameFromRetPrinterId(unitId, connection);
			userBean = getUserBean(userName);
			gameId = CommonMethodsDaoImpl.getInstance().verifyTktAndFetchGameId(requestBean.getTicketNumber(), connection);
			requestBean.setGameId(gameId);
			
			connection.setAutoCommit(false);

			isFraud = ResponsibleGaming.respGaming(userBean, "VS_PWT", String.valueOf(requestBean.getTotalAmount()), connection);
			if (isFraud) {
				throw new VSException(VSErrors.RG_LIMIT_EXCEPTION_ERROR_CODE, VSErrors.RG_LIMIT_EXCEPTION_ERROR_MESSAGE);
			}
			double retailerComm = Util.getVSPwtCommVariance(userBean.getUserOrgId(), requestBean.getGameId());
			double agentComm = Util.getVSPwtCommVariance(userBean.getParentOrgId(), requestBean.getGameId());
			govtClaimComm  = CommonMethodsControllerImpl.getInstance().getGameMap().get(gameId).getGovtClaimComm();
			responseBean.setStatus("NORMAL_PAY");
			double retailerNet = CommonMethods.fmtToTwoDecimal(requestBean.getTotalAmount()) + CommonMethods.fmtToTwoDecimal(requestBean.getTotalAmount() * retailerComm * .01);
			double agentNet = CommonMethods.fmtToTwoDecimal(requestBean.getTotalAmount()) + CommonMethods.fmtToTwoDecimal(requestBean.getTotalAmount() * agentComm * .01);
			
			PwtInvBean invBean = new PwtInvBean().setGameId(requestBean.getGameId()).setWinningAmount(requestBean.getTotalAmount()).setTicketNumber(requestBean.getTicketNumber()).setEngineTransactionId(String.valueOf(requestBean.getTxnIdIw())).setClaimAt("RETAILER").setStatus("CLAIM_AT_RETAILER").setDirPly(false);
			int pwtInvId = WinningTransactionManagerVS.insertPwtInventory(invBean, connection);
			
			TransactionBean transBean = new TransactionBean().setUserBean(userBean).setServiceCode(serviceCode).setInterfaceType(interfaceType).setPartyId(requestBean.getGameId()).setTransType("VS_PWT").setTransTime(transactionTime);
			transId = WinningTransactionManagerVS.insertRetailerTransaction(transBean, connection);

			DirectPlrPwtBean pwtBean = new DirectPlrPwtBean().setTransId(transId).setPwtInvId(pwtInvId).setTicketNumber(requestBean.getTicketNumber()).setGameId(requestBean.getGameId()).setUserBean(userBean).setWinningAmount(requestBean.getTotalAmount()).setRetailerClaimComm(CommonMethods.fmtToTwoDecimal(requestBean.getTotalAmount() * retailerComm * .01)).setRetailerNetAmt(retailerNet).setAgentClaimComm(CommonMethods.fmtToTwoDecimal(requestBean.getTotalAmount() * agentComm * .01)).setAgentNetAmt(agentNet).setTransTime(transactionTime).setPwtClaimStatus("DONE_CLM").setGovtClaimComm(requestBean.getTotalAmount()*govtClaimComm*.01);
			WinningTransactionManagerVS.insertRetailerPwt(pwtBean, connection);

			WinningTransactionManagerVS.updateRetailerPwtInv(requestBean.getGameId(), requestBean.getTicketNumber(), transId, connection);

			boolean isValid = OrgCreditUpdation.updateOrganizationBalWithValidate(retailerNet, "TRANSACTION", "VS_PWT", userBean.getUserOrgId(), userBean.getParentOrgId(), "RETAILER", 0, connection);
			if (!isValid) {
				throw new VSException(VSErrors.RETAILER_BALANCE_INSUFFICIENT_ERROR_CODE, VSErrors.RETAILER_BALANCE_INSUFFICIENT_ERROR_MESSAGE);
			}

			isValid = OrgCreditUpdation.updateOrganizationBalWithValidate(agentNet, "TRANSACTION", "VS_PWT", userBean.getParentOrgId(), 0, "AGENT", 0, connection);
			if (!isValid) {
				throw new LMSException(VSErrors.AGENT_BALANCE_INSUFFICIENT_ERROR_CODE, VSErrors.AGENT_BALANCE_INSUFFICIENT_ERROR_MESSAGE);
			}
			Util.setHeartBeatAndSaleTime(userBean.getUserOrgId(), "VS_PWT", connection);
			connection.commit();
			
			balance = CommonMethodsDaoImpl.getInstance().fetchVSUserCredit(unitId, connection);
			responseBean.setBalance(balance);
			responseBean.setOldBalance(balance-retailerNet);
		} catch (SQLException se) {
			se.printStackTrace();
			throw new VSException(VSErrors.SQL_EXCEPTION_ERROR_CODE, VSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		} catch (VSException le) {
			throw le;
		} catch(LMSException le){
			throw new VSException(VSErrors.SESSION_TIME_OUT_ERROR_CODE,VSErrors.SESSION_TIME_OUT_ERROR_MESSAGE);
		}catch (Exception e) {
			e.printStackTrace();
			if(((LMSException) e).getErrorCode() == 118){
				throw new VSException(VSErrors.SESSION_TIME_OUT_ERROR_CODE, VSErrors.SESSION_TIME_OUT_ERROR_MESSAGE);
			} else {
				throw new VSException(VSErrors.GENERAL_EXCEPTION_ERROR_CODE, VSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
			}
		} finally {
			DBConnect.closeCon(connection);
		}
		return responseBean;
	}
	
}