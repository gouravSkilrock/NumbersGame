package com.skilrock.lms.coreEngine.virtualSport.playMgmt.controllerImpl;

import java.sql.Connection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.BaseAction;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.ResponsibleGaming;
import com.skilrock.lms.coreEngine.virtualSport.beans.TPSaleRequestBean;
import com.skilrock.lms.coreEngine.virtualSport.beans.TPTxRequestBean;
import com.skilrock.lms.coreEngine.virtualSport.common.VSErrors;
import com.skilrock.lms.coreEngine.virtualSport.common.VSException;
import com.skilrock.lms.coreEngine.virtualSport.common.VSUtil;
import com.skilrock.lms.coreEngine.virtualSport.common.daoImpl.CommonMethodsDaoImpl;
import com.skilrock.lms.coreEngine.virtualSport.playMgmt.daoImpl.VirtualSportGamePlayDaoImpl;
import com.skilrock.lms.web.drawGames.common.Util;

public class VirtualSportGamePlayControllerImpl  extends BaseAction{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static VirtualSportGamePlayControllerImpl classInstance = null;
	private static Logger logger = LoggerFactory
			.getLogger(VirtualSportGamePlayControllerImpl.class);
	
	public VirtualSportGamePlayControllerImpl() {
		super(VirtualSportGamePlayControllerImpl.class.getName());
	}

	public static VirtualSportGamePlayControllerImpl getInstance() {
		if (classInstance == null)
			classInstance = new VirtualSportGamePlayControllerImpl();
		return classInstance;
	}

	/**
	 * 
	 * @param userInfoBean
	 * @param tpTransactionBean
	 * @author Nikhil K. Bansal
	 */
	public void virtualSportsPurchaseTicket(UserInfoBean userInfoBean , TPSaleRequestBean tpTransactionBean) throws VSException{
		Connection con = null;
		String userName=null;
		try {
			con = DBConnect.getConnection();
			userName=VirtualSportGamePlayDaoImpl.getInstance().getUserNameFromRetPrinterId(tpTransactionBean.getUnitId(), con);
			if(userName==null){
				throw new VSException(VSErrors.INVALID_USER_NAME_CODE,VSErrors.INVALID_USER_NAME_MESSAGE);
			}
			userInfoBean = getUserBean(userName);
			con.setAutoCommit(false);
			boolean isFraud = ResponsibleGaming.respGaming(userInfoBean, "VS_SALE", tpTransactionBean.getAmount() + "", con);
			if (!isFraud) {
				try {
					tpTransactionBean.setGameId(VSUtil.getGameId(tpTransactionBean.getEventType()));
					
					//purchase Ticket
					VirtualSportGamePlayDaoImpl.virtualBettingPurchaseTicket(tpTransactionBean, userInfoBean, con);

					// Update Heart Beat After Sale Completion
					Util.setHeartBeatAndSaleTime(userInfoBean.getUserOrgId(), "VS_SALE", con);
					con.commit();
				} catch (VSException e) {
					throw e;
				} catch (Exception e) {
					e.printStackTrace();
					throw new VSException(VSErrors.GENERAL_EXCEPTION_ERROR_CODE,VSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
				}
			} else {
				throw new VSException(VSErrors.PURCHASE_FRAUD_ERROR_CODE,VSErrors.PURCHASE_FRAUD_ERROR_MESSAGE);
			}
		} catch (VSException e) {
			throw e;
		} catch(LMSException le){
			throw new VSException(VSErrors.SESSION_TIME_OUT_ERROR_CODE,VSErrors.SESSION_TIME_OUT_ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new VSException(VSErrors.GENERAL_EXCEPTION_ERROR_CODE,VSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}finally{
			DBConnect.closeCon(con);
		}		
	}
	
	public void virtualSportsRefund(UserInfoBean userInfoBean , TPSaleRequestBean tpTransactionBean) throws VSException{
		Connection con = null;
		String userName=null;
		try {
			con = DBConnect.getConnection();
			userName=VirtualSportGamePlayDaoImpl.getInstance().getUserNameFromRetPrinterId(tpTransactionBean.getUnitId(), con);
			if(userName==null){
				throw new VSException(VSErrors.INVALID_USER_NAME_CODE,VSErrors.INVALID_USER_NAME_MESSAGE);
			}
			userInfoBean = getUserBean(userName);
			con.setAutoCommit(false);
				try {
					tpTransactionBean.setGameId(CommonMethodsDaoImpl.getInstance().verifyTktAndFetchGameId(tpTransactionBean.getTicketNumber(), con));
					
					//refund Ticket
					long transId = VirtualSportGamePlayDaoImpl.virtualBettingRefundTicket(tpTransactionBean, userInfoBean, con);
					if(transId == 0)
						throw new VSException(VSErrors.GENERAL_EXCEPTION_ERROR_CODE,VSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
					
					con.commit();
				} catch (VSException e) {
					throw e;
				} catch (Exception e) {
					e.printStackTrace();
					throw new VSException(VSErrors.GENERAL_EXCEPTION_ERROR_CODE,VSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
				}
			
		} catch (VSException e) {
			throw e;
		}catch(LMSException le){
			throw new VSException(VSErrors.SESSION_TIME_OUT_ERROR_CODE,VSErrors.SESSION_TIME_OUT_ERROR_MESSAGE);
		}catch (Exception e) {
			e.printStackTrace();
			throw new VSException(VSErrors.GENERAL_EXCEPTION_ERROR_CODE,VSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}finally{
			DBConnect.closeCon(con);
		}		
	}
	
	
	public void updateTicketInfo(TPTxRequestBean reqBean, boolean isLoginReq) throws VSException{
		Connection conn = null;
		String userName = null;
		try{
			conn = DBConnect.getConnection();
			userName = VirtualSportGamePlayDaoImpl.getInstance().getUserNameFromRetPrinterId(reqBean.getUnitId(), conn);
			if(isLoginReq)
				getUserBean(userName);
			VirtualSportGamePlayDaoImpl.getInstance().updateTicketInfo(reqBean, conn);
		} catch(VSException le){
			throw le;
		} catch(LMSException le){
			throw new VSException(VSErrors.SESSION_TIME_OUT_ERROR_CODE,VSErrors.SESSION_TIME_OUT_ERROR_MESSAGE);
		}catch(Exception e){
			e.printStackTrace();
			throw new VSException(VSErrors.INTERNAL_SYSTEM_ERROR_CODE, VSErrors.INTERNAL_SYSTEM_ERROR_MESSAGE);
		} finally{
			DBConnect.closeCon(conn);
		}
	}
	
}
