package com.skilrock.lms.controllerImpl;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.skilrock.lms.beans.BOMasterApprovalBean;
import com.skilrock.lms.beans.OrgPwtLimitBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.Utility;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.controller.accMgmtController.WinningMgmtController;
import com.skilrock.lms.coreEngine.drawGames.common.CommonFunctionsHelper;
import com.skilrock.lms.coreEngine.sportsLottery.beans.DrawTicketDataBean;
import com.skilrock.lms.coreEngine.sportsLottery.common.controllerImpl.CommonMethodsControllerImpl;
import com.skilrock.lms.dao.WinningMgmtDao;
import com.skilrock.lms.daoImpl.WinningMgmtDaoImplSLE;
import com.skilrock.lms.dge.beans.PlayerBean;
import com.skilrock.lms.rest.services.bean.TPPwtRequestBean;
import com.skilrock.lms.rest.services.bean.TPPwtResponseBean;
import com.skilrock.lms.rest.services.bean.TPRequestBean;
import com.skilrock.lms.rest.services.bean.TPResponseBean;
import com.skilrock.lms.sportsLottery.common.SLE;

public class WinningMgmtControllerImplSLE implements WinningMgmtController {
	private static Logger logger = LoggerFactory.getLogger(WinningMgmtController.class);

	private volatile static WinningMgmtControllerImplSLE winMgmtController = null;

	private WinningMgmtControllerImplSLE(){}

	public static WinningMgmtControllerImplSLE getInstance() {
		if (winMgmtController == null) {
			synchronized (WinningMgmtControllerImplSLE.class) {
				if (winMgmtController == null) {
					winMgmtController = new WinningMgmtControllerImplSLE();
				}
			}
		}
	
		return winMgmtController;
	}

	@Override
	public String checkTicketPWTStatus(double winningAmount) throws LMSException {
		double highPrizeAmt = Double.parseDouble(Utility.getPropertyValue("SLE_HIGH_PRIZE_AMT"));
		double masApproveLimit = Double.parseDouble(Utility.getPropertyValue("SLE_MAS_APPROVE_LIMIT"));
		if(highPrizeAmt>winningAmount) {
			return SLE.Status.NORMAL_PAY;
		} else if(highPrizeAmt<=winningAmount && winningAmount<masApproveLimit) {
			return SLE.Status.HIGH_PRIZE;
		} else if(masApproveLimit <= winningAmount) {
			return SLE.Status.MAS_APPROVAL;
		} else {
			return null;
		}
	}

	@Override
	public String checkRetailerClaimStatus(double winningAmount, UserInfoBean userBean) throws LMSException {
		//boolean claimStatus = false;
		Connection connection = null;
		OrgPwtLimitBean limitBean = null;
		String statusMsg = null;
		try {
			double highPrizeAmt = Double.parseDouble(Utility.getPropertyValue("SLE_HIGH_PRIZE_AMT"));
			if(highPrizeAmt>winningAmount) {
				connection = DBConnect.getConnection();
				limitBean = new CommonFunctionsHelper().fetchPwtLimitsOfOrgnization(userBean.getUserOrgId(), connection);
				if (limitBean == null) {
					throw new LMSException("PWT Limits Are Not defined Properly!!");
				}
				if (winningAmount <= limitBean.getPayLimit()) {
					//claimStatus = true;
					statusMsg = "NORMAL_PAY";
				} else {
					statusMsg = "ORG_LMT_EXCEED";
				}
			} else {
				//claimStatus = false;
				statusMsg = "WINNING_EXCEED_HIGH_PRIZE";
			}
		} catch (LMSException le) {
			throw le;
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE, LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeCon(connection);
		}

		return statusMsg;
	}

	@Override
	public boolean checkPayoutLimits(String ticketNumber, UserInfoBean userBean, double winningAmount) throws LMSException {
		return true;
	}

	@Override
	public TPResponseBean manageWinning(UserInfoBean userBean, TPRequestBean requestBean) throws LMSException {
		logger.info("--Inside manageWinning Controller--");
		long startTime = System.currentTimeMillis();

		TPResponseBean responseBean = null;
		TPPwtRequestBean pwtRequestBean = null;
		TPPwtResponseBean pwtResponseBean = null;
		try {
			pwtRequestBean = new Gson().fromJson(requestBean.getRequestData().toString(), new TypeToken<TPPwtRequestBean>(){}.getType());

			WinningMgmtDao winningDao = WinningMgmtDaoImplSLE.getInstance();

			if("BO".equals(userBean.getUserType())) {
				if(SLE.Status.NORMAL_PAY.equals(pwtRequestBean.getPwtType())) {
					pwtResponseBean = winningDao.boNormalPay(userBean, pwtRequestBean, requestBean.getServiceCode(), requestBean.getInterfaceType());
				} else if(SLE.Status.HIGH_PRIZE.equals(pwtRequestBean.getPwtType())) {
					pwtResponseBean = winningDao.boHighPrize(userBean, pwtRequestBean, requestBean.getServiceCode(), requestBean.getInterfaceType());
				} else if(SLE.Status.MAS_APPROVAL.equals(pwtRequestBean.getPwtType())) {
					pwtResponseBean = winningDao.masApprovalInit(userBean, pwtRequestBean, requestBean.getServiceCode(), requestBean.getInterfaceType());
				} else if(SLE.Status.MAS_APPROVAL_DONE.equals(pwtRequestBean.getPwtType())) {
					pwtResponseBean = winningDao.masApprovalDone(userBean, pwtRequestBean, requestBean.getServiceCode(), requestBean.getInterfaceType());
				}
			} else if("AGENT".equals(userBean.getUserType())) {
				//	fetch pwt limit for org
				if(SLE.Status.NORMAL_PAY.equals(pwtRequestBean.getPwtType())) {
					pwtResponseBean = winningDao.agentNormalPay(userBean, pwtRequestBean, requestBean.getServiceCode(), requestBean.getInterfaceType());
				} else if(SLE.Status.HIGH_PRIZE.equals(pwtRequestBean.getPwtType())) {
					pwtResponseBean = winningDao.agentHighPrize(userBean, pwtRequestBean, requestBean.getServiceCode(), requestBean.getInterfaceType());
				} else if(SLE.Status.MAS_APPROVAL.equals(pwtRequestBean.getPwtType())) {
					pwtResponseBean = winningDao.masApprovalInit(userBean, pwtRequestBean, requestBean.getServiceCode(), requestBean.getInterfaceType());
				}
			} else if("RETAILER".equals(userBean.getUserType())) {
				if(SLE.Status.NORMAL_PAY.equals(pwtRequestBean.getPwtType())) {
					pwtResponseBean = winningDao.retailerNormalPay(userBean, pwtRequestBean, requestBean.getServiceCode(), requestBean.getInterfaceType());
				}
			}
			pwtResponseBean.setAdvMsg(CommonMethodsControllerImpl.getInstance().getSLEAdvMessages(userBean.getUserOrgId(), pwtRequestBean.getGameTypeId()));
			responseBean = new TPResponseBean(0, "SUCCESS", pwtResponseBean);
		} catch (LMSException le) {
			throw le;
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE, LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}

		long endTime = System.currentTimeMillis();
		logger.info("Time Taken By Controller - "+((endTime-startTime)/1000));

		return responseBean;
	}

	public List<BOMasterApprovalBean> getMasOrPendingRequests(BOMasterApprovalBean requestBean, String requestType) throws LMSException {
		logger.info("--Inside getMasterApprovalRequests Controller--");
		Connection connection = null;
		List<BOMasterApprovalBean> masterApprovalList = null;
		try {
			connection = DBConnect.getConnection();
			WinningMgmtDaoImplSLE winningDao = WinningMgmtDaoImplSLE.getInstance();
			masterApprovalList = winningDao.getMasOrPendingRequests(requestBean, requestType, connection);
		} catch (LMSException le) {
			throw le;
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE, LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeCon(connection);
		}

		return masterApprovalList;
	}

	public boolean processMasterApproval(int taskId, String processType, int userId, String userType) throws LMSException {
		logger.info("--Inside getMasterApprovalRequests Controller--");
		Connection connection = null;
		boolean status = false;
		try {
			processType = "APPROVE".equals(processType) ? "PND_PAY" : "CANCEL";

			connection = DBConnect.getConnection();
			WinningMgmtDaoImplSLE winningDao = WinningMgmtDaoImplSLE.getInstance();
			status = winningDao.processMasterApproval(taskId, processType, userId, userType, connection);
		} catch (LMSException le) {
			throw le;
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE, LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeCon(connection);
		}

		return status;
	}

	public boolean processPaymentRequests(BOMasterApprovalBean approvalBean, String processType, UserInfoBean userBean) throws LMSException {
		logger.info("--Inside processPaymentRequests Controller--");
		Connection connection = null;
		boolean status = false;
		try {
			connection = DBConnect.getConnection();
			connection.setAutoCommit(false);

			WinningMgmtDaoImplSLE winningDao = WinningMgmtDaoImplSLE.getInstance();
			if("APPROVE".equals(processType)) {
				List<DrawTicketDataBean> drawDataList = new ArrayList<DrawTicketDataBean>();
				DrawTicketDataBean drawDataBean = new DrawTicketDataBean();
				drawDataBean.setDrawId(approvalBean.getDrawId());
				drawDataBean.setTaskId(approvalBean.getTaskId());
				drawDataBean.setTaxAmt(approvalBean.getTaxAmount());
				drawDataBean.setNetAmt(approvalBean.getNetAmount());
				drawDataBean.setWinningAmt(approvalBean.getWinningAmount());
				drawDataList.add(drawDataBean);

				TPPwtRequestBean requestBean = new TPPwtRequestBean();
				requestBean.setGameId(approvalBean.getGameId());
				requestBean.setGameTypeId(approvalBean.getGameTypeId());
				requestBean.setTicketNumber(approvalBean.getTicketNumber());
				requestBean.setRemarks("Approved By BO");
				requestBean.setTotalAmount(approvalBean.getWinningAmount());
				requestBean.setDrawDataList(drawDataList);
				requestBean.setPlayerBean(new PlayerBean());
				requestBean.getPlayerBean().setPlayerId(approvalBean.getPlayerId());

				winningDao.masApprovalDone(userBean, requestBean, "SLE", "WEB");
			} else if("DENY".equals(processType)) {
				status = winningDao.processMasterApproval(approvalBean.getTaskId(), "CANCEL", userBean.getUserId(), userBean.getUserType(), connection);
			}

			connection.commit();
		} catch (LMSException le) {
			throw le;
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE, LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		} finally {
			DBConnect.closeCon(connection);
		}

		return status;
	}
}