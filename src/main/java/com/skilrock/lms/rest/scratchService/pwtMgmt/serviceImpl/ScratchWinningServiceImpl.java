package com.skilrock.lms.rest.scratchService.pwtMgmt.serviceImpl;

import java.sql.Connection;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.skilrock.lms.beans.OrgPwtLimitBean;
import com.skilrock.lms.beans.PwtBean;
import com.skilrock.lms.beans.ScratchPayPWTBean;
import com.skilrock.lms.beans.TicketBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.Utility;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.MD5Encoder;
import com.skilrock.lms.coreEngine.scratchService.common.ScratchErrors;
import com.skilrock.lms.coreEngine.scratchService.orderMgmt.beans.ScratchGameDataBean;
import com.skilrock.lms.coreEngine.scratchService.pwtMgmt.common.CommonFunctionsHelper;
import com.skilrock.lms.coreEngine.scratchService.pwtMgmt.common.RetailerPwtProcessHelper;
import com.skilrock.lms.rest.scratchService.dao.ScratchDao;
import com.skilrock.lms.rest.scratchService.daoImpl.ScratchDaoImpl;
import com.skilrock.lms.rest.scratchService.pwtMgmt.beans.ScratchTicketVirnData;
import com.skilrock.lms.rest.scratchService.pwtMgmt.beans.ScratchWinningDataBean;
import com.skilrock.lms.rest.scratchService.pwtMgmt.daoImpl.ScratchWinningDaoImpl;
import com.skilrock.lms.rest.services.bean.ScratchWinningPaymentRequest;

/**
 * This is service class for verify and pay winning for scratch.
 * 
 * @author Nikhil K. Bansal
 */
public class ScratchWinningServiceImpl {

	private static Logger logger = LoggerFactory.getLogger(ScratchWinningServiceImpl.class);

	private ScratchDao scratchDao;
	private CommonFunctionsHelper commonFunction;
	private RetailerPwtProcessHelper retailerPwtHelper;
	private ScratchWinningDaoImpl scratchWinDao;
	private ScratchGameDataBean scratchGameDataBean;
	private PwtBean pwtBean;
	private ScratchPayPWTBean scratchPayPWTBean;
	private TicketBean ticketBean;
	private OrgPwtLimitBean orgPwtLimitBean;
	
	public ScratchWinningServiceImpl(ScratchDao scratchDao, CommonFunctionsHelper commonFunction, RetailerPwtProcessHelper retailerPwtHelper, ScratchWinningDaoImpl scratchWinDao) {
		this.scratchDao = scratchDao;
		this.commonFunction = commonFunction;
		this.retailerPwtHelper = retailerPwtHelper;
		this.scratchWinDao = scratchWinDao;
	}

	public ScratchWinningServiceImpl() {
		scratchDao = new ScratchDaoImpl();
		commonFunction = new CommonFunctionsHelper();
		retailerPwtHelper = new RetailerPwtProcessHelper();
		scratchWinDao = new ScratchWinningDaoImpl();
	}

	public ScratchWinningDataBean verifyTicketAndVirn(String ticketNo, String virnNo, UserInfoBean userInfoBean)
			throws LMSException {
		Connection connection = null;
		ScratchWinningDataBean winDataBean = null;
		try {
			winDataBean = new ScratchWinningDataBean();
			String highPrizeCriteria = Utility.getPropertyValue("HIGH_PRIZE_CRITERIA");
			String highPrizeAmt = Utility.getPropertyValue("HIGH_PRIZE_AMT");
			connection = DBConnect.getConnection();
			ScratchGameDataBean gameDataBean = scratchDao.getGameDataWithPwtEndDateVerifyFromTicketNbr(ticketNo,
					connection);
			TicketBean tktBean = commonFunction.isTicketFormatValid(ticketNo, gameDataBean.getGameId(), connection);
			if (tktBean != null && !tktBean.getIsValid()) {
				throw new LMSException(ScratchErrors.INVALID_TICKET_NUMBER_ERROR_CODE,ScratchErrors.INVALID_TICKET_NUMBER_ERROR_MESSAGE);
			}
			String tktArr[] = tktBean.getTicketNumber().split("-");
			tktBean = retailerPwtHelper.checkTicketStatus(tktArr[0], tktArr[0] + "-" + tktArr[1], tktArr[2],
					gameDataBean.getGameId(), connection);
			if (tktBean==null) {
				throw new LMSException(ScratchErrors.INVALID_TICKET_NUMBER_ERROR_CODE,
						ScratchErrors.INVALID_TICKET_NUMBER_ERROR_MESSAGE);
			}

			if (tktBean != null && !tktBean.getIsValid()) {
				if("221002".equals(tktBean.getMessageCode()) || "222006".equals(tktBean.getMessageCode())){
					winDataBean.setStatus("CLAIMED");
					return winDataBean;
				}else{
					throw new LMSException(ScratchErrors.INVALID_TICKET_NUMBER_ERROR_CODE,
							ScratchErrors.INVALID_TICKET_NUMBER_ERROR_MESSAGE);
				}
			}

			tktBean.setTicketGameId(gameDataBean.getGameId());
			tktBean.setTicketNumber(tktArr[0] + "-" + tktArr[1] + "-" + tktArr[2]);
			tktBean.setBook_nbr(tktArr[0] + "-" + tktArr[1]);
			tktBean.setGameNbr(Integer.parseInt(tktArr[0]));
			ScratchTicketVirnData ticketVirnData = scratchWinDao.verifyAndGetTicketVirnData(virnNo, gameDataBean,
					connection, tktBean);
			logger.info("ticket status",ticketVirnData);
			if (ticketVirnData.getPrizeStatus().equalsIgnoreCase("UNCLM_PWT")
					|| ticketVirnData.getPrizeStatus().equalsIgnoreCase("UNCLM_CANCELLED")) {
				OrgPwtLimitBean orgPwtLimit = commonFunction.fetchPwtLimitsOfOrgnization(userInfoBean.getUserOrgId(),
						connection);
				winDataBean.setWinningAmount(Double.parseDouble(ticketVirnData.getPwtAmount()));
				double pwtAmt = winDataBean.getWinningAmount();
				boolean isHighPrizeFlag = "level".equals(highPrizeCriteria)
						&& "HIGH".equals(ticketVirnData.getPrizeLevel())
						|| "amt".equals(highPrizeCriteria) && pwtAmt > Double.parseDouble(highPrizeAmt);
				if (pwtAmt > orgPwtLimit.getVerificationLimit() || pwtAmt > orgPwtLimit.getApprovalLimit()
						|| isHighPrizeFlag) {
					winDataBean.setStatus("HIGH_PRIZE");
				} else if (pwtAmt <= orgPwtLimit.getPayLimit()) {
					winDataBean.setStatus("UNCLAIMED");
				}
			}

			else if (ticketVirnData.getPrizeStatus().equalsIgnoreCase("NO_PRIZE_PWT")) {
				winDataBean.setStatus("NON_WIN");
			} else {
				winDataBean.setStatus("CLAIMED");
			}
		} catch (LMSException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException(ScratchErrors.GENERAL_EXCEPTION_ERROR_CODE, ScratchErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
		return winDataBean;
	}
	
	public void payPWTProcess(UserInfoBean userInfoBean, ScratchWinningPaymentRequest scratchWinningPaymentRequest,ScratchWinningDataBean winningDataBean) throws LMSException{
		Connection connection = null;
		try{
			connection = DBConnect.getConnection();
			connection.setAutoCommit(false);
			pwtBean = setPWTBeanParameters(scratchWinningPaymentRequest, winningDataBean);
			fetchAndVerfiyScratchGameDataBean(scratchWinningPaymentRequest, connection);
			ticketBean = validateTicketNumberAndRequiredParameters(scratchWinningPaymentRequest, connection);
			fetchAndVerifyOrgPWTLimitBean(userInfoBean, connection);
			scratchPayPWTBean = setScratchPayPWTBeanParameters(userInfoBean);
			scratchWinDao.payPWTProcess(scratchPayPWTBean, connection);
			connection.commit();
		} catch(LMSException e){
			throw e;
		} catch(SQLException e){
			logger.error("Exception Occurred", e);
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE, LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} catch(Exception e){
			logger.error("Exception Occurred", e);
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE, LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
	}

	private PwtBean setPWTBeanParameters(ScratchWinningPaymentRequest scratchWinningPaymentRequest, ScratchWinningDataBean winningDataBean) {
		PwtBean pwtBean = new PwtBean();
		pwtBean.setEncVirnCode(MD5Encoder.encode(scratchWinningPaymentRequest.getVirnNumber()));
		pwtBean.setEnctktNumber(MD5Encoder.encode(scratchWinningPaymentRequest.getTicketNumber()));
		pwtBean.setValid(true);
		pwtBean.setVerificationStatus("Valid Virn");
		pwtBean.setMessage("Credited To Concerned Party");
		pwtBean.setPwtAmount(Double.toString(winningDataBean.getWinningAmount()));
		return pwtBean;
	}

	private void fetchAndVerfiyScratchGameDataBean(ScratchWinningPaymentRequest scratchWinningPaymentRequest, Connection connection) throws LMSException {
		scratchGameDataBean = scratchDao.getGameDataWithPwtEndDateVerifyFromTicketNbr(scratchWinningPaymentRequest.getTicketNumber(),connection);
		if(scratchGameDataBean == null){
			throw new LMSException(ScratchErrors.SCRATCH_GAME_NOT_AVAILABLE_ERROR_CODE, ScratchErrors.SCRATCH_GAME_NOT_AVAILABLE_ERROR_MESSAGE);
		}
	}
	
	private TicketBean validateTicketNumberAndRequiredParameters(ScratchWinningPaymentRequest scratchWinningPaymentRequest, Connection connection) throws SQLException,LMSException {
		TicketBean tktBean = null;
		tktBean = commonFunction.isTicketFormatValid(scratchWinningPaymentRequest.getTicketNumber(), scratchGameDataBean.getGameId(), connection);
		if (tktBean != null && !tktBean.getIsValid()) {
			throw new LMSException(ScratchErrors.INVALID_TICKET_NUMBER_ERROR_CODE,ScratchErrors.INVALID_TICKET_NUMBER_ERROR_MESSAGE);
		}
		String tktArr[] = tktBean.getTicketNumber().split("-");
		tktBean = retailerPwtHelper.checkTicketStatus(tktArr[0], tktArr[0] + "-" + tktArr[1], tktArr[2],scratchGameDataBean.getGameId(), connection);
		tktBean.setTicketGameId(scratchGameDataBean.getGameId());
		tktBean.setTicketNumber(tktArr[0] + "-" + tktArr[1] + "-" + tktArr[2]);
		tktBean.setBook_nbr(tktArr[0] + "-" + tktArr[1]);
		tktBean.setGameNbr(Integer.parseInt(tktArr[0]));
		return tktBean;
	}
	
	private void fetchAndVerifyOrgPWTLimitBean(UserInfoBean userInfoBean, Connection connection) throws SQLException, LMSException {
		orgPwtLimitBean = commonFunction.fetchPwtLimitsOfOrgnization(userInfoBean.getUserOrgId(), connection);
		if(orgPwtLimitBean == null){
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE, LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}
	}
	
	private ScratchPayPWTBean setScratchPayPWTBeanParameters(UserInfoBean userInfoBean) {
		ScratchPayPWTBean scratchPayPWTBean = new ScratchPayPWTBean();
		scratchPayPWTBean.setUserInfoBean(userInfoBean);
		scratchPayPWTBean.setPwtBean(pwtBean);
		scratchPayPWTBean.setTicketBean(ticketBean);
		scratchPayPWTBean.setOrgPwtLimitBean(orgPwtLimitBean);
		return scratchPayPWTBean;
	}

}
