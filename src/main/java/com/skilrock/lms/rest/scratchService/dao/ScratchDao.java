package com.skilrock.lms.rest.scratchService.dao;

import java.sql.Connection;
import java.sql.SQLException;

import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.scratchService.orderMgmt.beans.ScratchGameDataBean;
import com.skilrock.lms.rest.services.bean.DaoBean;

public interface ScratchDao {

	public DaoBean getUserOrgIdAndUserIdFromTpUserId(String tpUserId) throws SQLException;
	public UserInfoBean getUserBeanFromTpUserId(String tpUserId,Connection connection) throws LMSException;
	public ScratchGameDataBean getGameDataWithPwtEndDateVerifyFromTicketNbr(String ticketNbr,Connection connection) throws LMSException;
	public int getAgentOrgIdFromTPUserId(String tpUserId);
	public int getParentOrgId(int userId, Connection connection) throws LMSException;
	public DaoBean getGameIdFromGameMasterByUsingGameNbrInTicket(int gameNbr) throws SQLException;

}
