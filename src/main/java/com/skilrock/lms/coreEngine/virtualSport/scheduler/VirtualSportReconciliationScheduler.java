package com.skilrock.lms.coreEngine.virtualSport.scheduler;

import com.skilrock.lms.api.lmsWrapper.LmsWrapperServiceApiHelper;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.BaseAction;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.coreEngine.userMgmt.common.VirtualSportsControllerImpl;
import com.skilrock.lms.coreEngine.virtualSport.beans.TPSaleRequestBean;
import com.skilrock.lms.coreEngine.virtualSport.beans.VSRequestBean;
import com.skilrock.lms.coreEngine.virtualSport.beans.VSResponseBean;
import com.skilrock.lms.coreEngine.virtualSport.common.VSException;
import com.skilrock.lms.coreEngine.virtualSport.common.daoImpl.CommonMethodsDaoImpl;
import com.skilrock.lms.coreEngine.virtualSport.playMgmt.daoImpl.VirtualSportGamePlayDaoImpl;
import com.skilrock.lms.web.drawGames.common.Util;

import java.sql.Connection;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

public class VirtualSportReconciliationScheduler extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public VirtualSportReconciliationScheduler() {
		super(VirtualSportReconciliationScheduler.class.getName());
		// TODO Auto-generated constructor stub
	}

	public void settleVSSaleTransactions() throws VSException, Exception {
		Connection conn = null;
		Map<String, VSRequestBean> salePendingDetailsMap = null;
		UserInfoBean userInfoBean = null;

		VSResponseBean vsResponseBean = null;
		VSRequestBean vsRequestBean = new VSRequestBean();
		TPSaleRequestBean tpTransactionBean = new TPSaleRequestBean();;
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"+ " HH:mm:ss");

		String startTimeString = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());

		Timestamp startTime = new Timestamp(dateFormat.parse(startTimeString + " 00:00:00").getTime());

		String endTimeString = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
		Timestamp endTime = new Timestamp(dateFormat.parse(endTimeString + " 23:59:59").getTime());

		vsRequestBean.setStartDate(startTime);
		vsRequestBean.setEndDate(endTime);
		try {
			conn = DBConnect.getConnection();
			LmsWrapperServiceApiHelper helper = new LmsWrapperServiceApiHelper() ;
			salePendingDetailsMap = CommonMethodsDaoImpl.getInstance().getPendingSaleTxns(vsRequestBean, conn, "PENDING");

			for (Map.Entry<String, VSRequestBean> map : salePendingDetailsMap.entrySet()) {
				vsRequestBean.setTxnId(map.getKey());
				vsResponseBean = VirtualSportsControllerImpl.Single.INSTANCE.getInstance().getTxnStatus(vsRequestBean);
				if ("error".equalsIgnoreCase(vsResponseBean.getVsCommonResponseBean().getResult())) {
					userInfoBean = helper.getUserDataFromUserId(map.getValue().getUserId());
					tpTransactionBean.setGameId(map.getValue().getGameId());
					tpTransactionBean.setTicketNumber(map.getValue().getTicketNumber());
					tpTransactionBean.setTxnId(map.getKey());
					VirtualSportGamePlayDaoImpl.virtualBettingRefundTicket(tpTransactionBean,userInfoBean, conn);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{ 
			DBConnect.closeResource(conn);
		}

	}

}
